package gov.ca.cwds.rest.services.hoi;

import static gov.ca.cwds.rest.core.Api.DS_CMS;
import static gov.ca.cwds.rest.core.Api.DS_CMS_REP;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.apache.commons.lang3.NotImplementedException;
import org.hibernate.FlushMode;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import gov.ca.cwds.data.Dao;
import gov.ca.cwds.data.cms.CaseDao;
import gov.ca.cwds.data.cms.ClientDao;
import gov.ca.cwds.data.cms.ClientRelationshipDao;
import gov.ca.cwds.data.cms.StaffPersonDao;
import gov.ca.cwds.data.persistence.cms.Client;
import gov.ca.cwds.data.persistence.cms.ClientRelationship;
import gov.ca.cwds.data.persistence.cms.CmsCase;
import gov.ca.cwds.data.persistence.cms.CmsKeyIdGenerator;
import gov.ca.cwds.data.persistence.cms.StaffPerson;
import gov.ca.cwds.rest.api.domain.LegacyDescriptor;
import gov.ca.cwds.rest.api.domain.cms.LegacyTable;
import gov.ca.cwds.rest.api.domain.cms.SystemCodeCache;
import gov.ca.cwds.rest.api.domain.cms.SystemCodeDescriptor;
import gov.ca.cwds.rest.api.domain.hoi.HOICase;
import gov.ca.cwds.rest.api.domain.hoi.HOICaseResponse;
import gov.ca.cwds.rest.api.domain.hoi.HOIRelatedPerson;
import gov.ca.cwds.rest.api.domain.hoi.HOIRequest;
import gov.ca.cwds.rest.api.domain.hoi.HOISocialWorker;
import gov.ca.cwds.rest.api.domain.hoi.HOIVictim;
import gov.ca.cwds.rest.resources.SimpleResourceService;
import gov.ca.cwds.rest.services.ServiceException;
import gov.ca.cwds.rest.services.auth.AuthorizationService;
import io.dropwizard.hibernate.UnitOfWork;

/**
 * <p>
 * This service handles user requests to get all the cases involved for the given client id.
 * </p>
 *
 * @author CWDS API Team
 */
public class HOICaseService extends SimpleResourceService<HOIRequest, HOICase, HOICaseResponse>
    implements HOIBaseService {

  private static final Logger LOGGER = LoggerFactory.getLogger(HOICaseService.class);

  private CaseDao caseDao;
  private ClientDao clientDao;
  private ClientRelationshipDao clientRelationshipDao;
  private StaffPersonDao staffPersonDao;
  private AuthorizationService authorizationService;
  private HOIParentsFactory hoiParentsFactory;

  /**
   * @param caseDao {@link Dao} handling {@link gov.ca.cwds.data.persistence.cms.CmsCase} objects
   * @param clientDao {@link Dao} handling {@link gov.ca.cwds.data.persistence.cms.Client} objects
   * @param clientRelationshipDao {@link Dao} handling
   *        {@link gov.ca.cwds.data.persistence.cms.ClientRelationship}
   * @param staffPersonDao {@link Dao} handling {@link gov.ca.cwds.data.persistence.cms.StaffPerson}
   *        objects
   * @param authorizationService - authorizationService
   */
  @Inject
  public HOICaseService(CaseDao caseDao, ClientDao clientDao,
      ClientRelationshipDao clientRelationshipDao, StaffPersonDao staffPersonDao,
      AuthorizationService authorizationService) {
    super();
    this.caseDao = caseDao;
    this.clientDao = clientDao;
    this.clientRelationshipDao = clientRelationshipDao;
    this.staffPersonDao = staffPersonDao;
    this.authorizationService = authorizationService;
    this.hoiParentsFactory = new HOIParentsFactory();
  }

  /**
   * SNAP-49: HOI not shown for client.
   *
   * <p>
   * Sometimes Cases or Referrals link to clients that the current user is not authorized to view
   * due to sealed/sensitivity restriction, county access privileges, or a short-coming with
   * authorization rules. The client authorizer throws an UnauthorizedException, then skip that
   * client and move on. Don't bomb all History of Involvement because the user is not authorized to
   * view a client's half-sister's foster sibling.
   * </p>
   *
   * @param hoiRequest HOI REST request
   * @return HOI REST response
   */
  @Override
  @UnitOfWork(value = DS_CMS, readOnly = true, transactional = false, flushMode = FlushMode.MANUAL)
  public HOICaseResponse handleFind(HOIRequest hoiRequest) {
    return handleFindInternal(hoiRequest);
  }

  @UnitOfWork(value = DS_CMS_REP, readOnly = true, transactional = false,
      flushMode = FlushMode.MANUAL)
  protected HOICaseResponse handleFindInternal(HOIRequest hoiRequest) {
    final Collection<String> authorizedClientIds = authorizationService.filterClientIds(
        hoiRequest.getClientIds().stream().filter(Objects::nonNull).collect(Collectors.toSet()));
    if (authorizedClientIds.isEmpty()) {
      LOGGER.debug("NOT AUTHORIZED TO VIEW REQUESTED CLIENTS!");
      return new HOICaseResponse();
    }

    final HOICasesData hcd = new HOICasesData(authorizedClientIds);
    loadRelationshipsByClients(authorizedClientIds, hcd);
    hcd.getAllClientIds().addAll(getClientIdsFromRelations(hcd));
    loadRelationshipsByClients(hcd.getAllClientIds(), hcd);
    loadClients(hcd);
    loadCmsCases(hcd);

    final Map<String, CmsCase> cmsCases = hcd.getCmsCases();
    final List<HOICase> cases = new ArrayList<>(cmsCases.size());
    if (!cmsCases.isEmpty()) {
      for (CmsCase cmsCase : cmsCases.values()) {
        cases.add(constructHOICase(cmsCase, hcd));
      }
      cases.sort((c1, c2) -> c2.getStartDate().compareTo(c1.getStartDate()));
    }

    return new HOICaseResponse(cases);
  }

  private void loadRelationshipsByClients(Collection<String> clientIds, HOICasesData hcd) {
    final Map<String, Collection<ClientRelationship>> relationshipsByPrimaryClients =
        clientRelationshipDao.findByPrimaryClientIds(clientIds);
    hcd.setRelationshipsByPrimaryClients(relationshipsByPrimaryClients);

    final Map<String, Collection<ClientRelationship>> relationshipsBySecondaryClients =
        clientRelationshipDao.findBySecondaryClientIds(clientIds);
    hcd.setRelationshipsBySecondaryClients(relationshipsBySecondaryClients);

    final Collection<ClientRelationship> allRelationshipsByPrimaryClients =
        new ArrayList<>(relationshipsByPrimaryClients.size());
    relationshipsByPrimaryClients.values().forEach(allRelationshipsByPrimaryClients::addAll);
    hcd.setAllRelationshipsByPrimaryClients(allRelationshipsByPrimaryClients);

    final Collection<ClientRelationship> allRelationshipsBySecondaryClients =
        new ArrayList<>(relationshipsBySecondaryClients.size());
    relationshipsBySecondaryClients.values().forEach(allRelationshipsBySecondaryClients::addAll);
    hcd.setAllRelationshipsBySecondaryClients(allRelationshipsBySecondaryClients);
  }

  private void loadClients(HOICasesData hcd) {
    final Collection<String> ids = new HashSet<>(hcd.getAllClientIds());
    ids.addAll(getClientIdsFromRelations(hcd));
    hcd.setAllClients(clientDao.findClientsByIds(ids));
  }

  private Collection<String> getClientIdsFromRelations(HOICasesData hcd) {
    final Collection<String> ids = new HashSet<>();
    final Predicate<ClientRelationship> relationshipFilter = rel -> HOIRelationshipTypeService
        .isParentChildOrSiblingRelationshipType(rel.getClientRelationshipType());
    hcd.getAllRelationshipsByPrimaryClients().stream().filter(relationshipFilter)
        .forEach(rel -> ids.add(rel.getSecondaryClientId()));
    hcd.getAllRelationshipsBySecondaryClients().stream().filter(relationshipFilter)
        .forEach(rel -> ids.add(rel.getPrimaryClientId()));
    return ids.stream().filter(Objects::nonNull).collect(Collectors.toSet());
  }

  private void loadCmsCases(HOICasesData hcd) {
    final Map<String, CmsCase> cmsCases = caseDao.findByClientIds(hcd.getAllClientIds());
    if (!cmsCases.isEmpty()) {
      final Collection<String> staffPersonIds =
          cmsCases.values().stream().map(CmsCase::getFkstfperst).collect(Collectors.toSet());

      // DRS: SNAP-370: HOI Performance
      // Staff user data change very infrequently. Cache it.
      final Map<String, StaffPerson> staffPersons = staffPersonDao.findByIds(staffPersonIds);
      cmsCases.values().forEach(c -> c.setStaffPerson(staffPersons.get(c.getFkstfperst())));
      hcd.setCmsCases(cmsCases);
    }
  }

  private HOICase constructHOICase(CmsCase cmsCase, HOICasesData hcd) {
    final Client focusChildClient = hcd.getAllClients().get(cmsCase.getFkchldClt());
    if (focusChildClient == null) {
      throw new ServiceException("Inconsistent CWS/CMS data: there is a case for child client id "
          + cmsCase.getFkchldClt() + " but the client entity is absent.");
    }

    final List<HOIRelatedPerson> parents = new ArrayList<>();
    parents.addAll(hoiParentsFactory.buildParentsByPrimaryRelationship(focusChildClient, hcd));
    parents.addAll(hoiParentsFactory.buildParentsBySecondaryRelationship(focusChildClient, hcd));

    return new HOICaseFactory().createHOICase(cmsCase,
        constructCounty(cmsCase.getGovernmentEntityType()), constructServiceComponent(cmsCase),
        constructFocusChild(focusChildClient), constructAssignedSocialWorker(cmsCase), parents);
  }

  private HOISocialWorker constructAssignedSocialWorker(CmsCase cmsCase) {
    final StaffPerson staffPerson = cmsCase.getStaffPerson();
    final String staffId = staffPerson.getId();
    final LegacyDescriptor legacyDescriptor =
        new LegacyDescriptor(staffId, staffId, new DateTime(staffPerson.getLastUpdatedTime()),
            LegacyTable.STAFF_PERSON.getName(), LegacyTable.STAFF_PERSON.getDescription());

    return new HOISocialWorker(staffId, staffPerson.getFirstName(), staffPerson.getLastName(),
        staffPerson.getNameSuffix(), legacyDescriptor);
  }

  private SystemCodeDescriptor constructServiceComponent(CmsCase cmsCase) {
    return new SystemCodeDescriptor(cmsCase.getActiveServiceComponentType(), SystemCodeCache
        .global().getSystemCodeShortDescription(cmsCase.getActiveServiceComponentType()));
  }

  private HOIVictim constructFocusChild(Client client) {
    final String clientId = client.getId();
    final LegacyDescriptor legacyDescriptor =
        new LegacyDescriptor(clientId, CmsKeyIdGenerator.getUIIdentifierFromKey(clientId),
            new DateTime(client.getLastUpdatedTime()), LegacyTable.CLIENT.getName(),
            LegacyTable.CLIENT.getDescription());
    return new HOIVictim(client.getId(), client.getCommonFirstName(), client.getCommonLastName(),
        client.getNameSuffix(), legacyDescriptor);
  }

  @Override
  protected HOICaseResponse handleRequest(HOICase req) {
    LOGGER.info("HOICaseService handle request not implemented");
    throw new NotImplementedException("handle request not implemented");
  }

  @Override
  public Logger getLogger() {
    return LOGGER;
  }

}

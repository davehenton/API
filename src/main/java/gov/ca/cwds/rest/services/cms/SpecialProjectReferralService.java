package gov.ca.cwds.rest.services.cms;

import gov.ca.cwds.rest.exception.BusinessValidationException;
import gov.ca.cwds.rest.exception.IssueDetails;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.List;
import java.util.Set;

import javax.validation.Validator;

import org.apache.commons.lang3.NotImplementedException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import gov.ca.cwds.auth.realms.PerryUserIdentity;
import gov.ca.cwds.data.legacy.cms.dao.NonCWSNumberDao;
import gov.ca.cwds.data.legacy.cms.dao.SafelySurrenderedBabiesDao;
import gov.ca.cwds.data.legacy.cms.dao.SpecialProjectDao;
import gov.ca.cwds.data.legacy.cms.dao.SpecialProjectReferralDao;
import gov.ca.cwds.data.legacy.cms.entity.NonCWSNumber;
import gov.ca.cwds.data.legacy.cms.entity.SafelySurrenderedBabies;
import gov.ca.cwds.data.legacy.cms.entity.SpecialProject;
import gov.ca.cwds.data.legacy.cms.entity.SpecialProjectReferral;
import gov.ca.cwds.data.persistence.cms.CmsKeyIdGenerator;
import gov.ca.cwds.drools.DroolsConfiguration;
import gov.ca.cwds.drools.DroolsService;
import gov.ca.cwds.rest.api.domain.DomainChef;
import gov.ca.cwds.rest.api.domain.cms.LegacyTable;
import gov.ca.cwds.rest.api.domain.cms.SystemCode;
import gov.ca.cwds.rest.api.domain.cms.SystemCodeCache;
import gov.ca.cwds.rest.business.rules.SafelySurrenderBabiesDroolsConfiguration;
import gov.ca.cwds.rest.filters.RequestExecutionContext;
import gov.ca.cwds.rest.messages.MessageBuilder;
import gov.ca.cwds.rest.services.ServiceException;
import gov.ca.cwds.rest.services.TypedCrudsService;
import gov.ca.cwds.rest.services.referentialintegrity.RISpecialProjectReferral;
import io.dropwizard.hibernate.UnitOfWork;


/**
 * Business layer object to work on {@link SpecialProjectReferral}
 * 
 * @author CWDS API Team
 */
public class SpecialProjectReferralService implements
    TypedCrudsService<String, gov.ca.cwds.rest.api.domain.cms.SpecialProjectReferral, gov.ca.cwds.rest.api.domain.cms.SpecialProjectReferral> {

  private static final Logger LOGGER = LoggerFactory.getLogger(SpecialProjectReferralService.class);

  private static final String S_CSEC_REFERRAL = "S-CSEC Referral";
  private static final short MEDICAL_RECORD_SYSTEM_CODE_ID = 1331;

  @Inject
  private SpecialProjectReferralDao specialProjectReferralDao;

  @Inject
  private SpecialProjectDao specialProjectDao;

  @Inject
  private RISpecialProjectReferral riSpecialProjectReferral;

  @Inject
  private SafelySurrenderedBabiesDao safelySurrenderedBabiesDao;

  @Inject
  private NonCWSNumberDao nonCWSNumberDao;

  @Inject
  private DroolsService droolsService;

  @Inject
  private Validator validator;

  /**
   * Default no-argument constructor.
   */
  public SpecialProjectReferralService() {
    super();
  }

  /**
   * Constructor
   * 
   * @param specialProjectReferralDao - special project referral DAO
   * @param specialProjectDao - special Project DAO
   * @param riSpecialProjectReferral - referential integrity special project referral
   * 
   */
  @Inject
  public SpecialProjectReferralService(SpecialProjectReferralDao specialProjectReferralDao,
      SpecialProjectDao specialProjectDao, RISpecialProjectReferral riSpecialProjectReferral) {
    this.specialProjectReferralDao = specialProjectReferralDao;
    this.specialProjectDao = specialProjectDao;
    this.riSpecialProjectReferral = riSpecialProjectReferral;
  }


  /**
   * create Special Project Referral
   * 
   * @param sprDomain - Special Project Referral domain object
   * 
   * @return PostedSpecialProjectReferral - posted Special Project Referral
   * 
   */
  @Override
  @UnitOfWork(value = "cms")
  public gov.ca.cwds.rest.api.domain.cms.SpecialProjectReferral create(
      gov.ca.cwds.rest.api.domain.cms.SpecialProjectReferral sprDomain) {
    SpecialProjectReferral persisted = new SpecialProjectReferral();
    persisted.setCountySpecificCode(sprDomain.getCountySpecificCode());
    persisted.setPartEndDate(DomainChef.uncookLocalDateString(sprDomain.getParticipationEndDate()));
    persisted
        .setPartStartDate(DomainChef.uncookLocalDateString(sprDomain.getParticipationStartDate()));
    persisted.setReferralId(sprDomain.getReferralId());
    persisted.setSpecialProjectId(sprDomain.getSpecialProjectId());
    persisted.setSsbIndicator(sprDomain.getSafelySurrenderedBabiesIndicator());
    persisted
        .setId(CmsKeyIdGenerator.getNextValue(RequestExecutionContext.instance().getStaffId()));
    persisted.setLastUpdateId(RequestExecutionContext.instance().getStaffId());
    LocalDateTime localDateTime = RequestExecutionContext.instance().getRequestStartTime()
        .toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    persisted.setLastUpdateTime(localDateTime);

    return new gov.ca.cwds.rest.api.domain.cms.SpecialProjectReferral(
        specialProjectReferralDao.create(persisted));
  }

  /**
   * save CSEC Special Project Referral
   * 
   * @param referralId - referral ID
   * @param incidentCounty - county Code
   * @param startDate - start date of screening
   * @param messageBuilder - message builder
   * 
   * @return PostedSpecialProjectReferral - posted Special Project Referral
   * 
   */
  public gov.ca.cwds.rest.api.domain.cms.SpecialProjectReferral saveCsecSpecialProjectReferral(String referralId, 
      String incidentCounty, String startDate, MessageBuilder messageBuilder) {
    
    if (!validSpecialProjectReferral(incidentCounty, messageBuilder)) {
      return null;
    }

    short governmentEntityType = convertLogicalIdToSystemCodeFor(incidentCounty, 
        LegacyTable.GOVERNMENT_ORGANIZATION_ENTITY.getName());
    String specialProjectId = findSpecialProjectId(S_CSEC_REFERRAL, governmentEntityType);
    
    try {
      gov.ca.cwds.rest.api.domain.cms.SpecialProjectReferral sprDomain =
          new gov.ca.cwds.rest.api.domain.cms.SpecialProjectReferral(incidentCounty, referralId,
              specialProjectId, null,
              startDate, 
              /**
               * <blockquote>
               * 
               * <pre>
               * BUSINESS RULE: "R - 7539" 
               * 
               * If (selected) SPECIAL_PROJECT_REFERRAL > SAFELY_SURRENDERED_BABIES exist, 
               * set SPECIAL_PROJECT_REFERRAL.Safely_Surrenderd_Babies_Ind_Var ='Y', else set to 'N'.
               * 
               * </blockquote>
               * </pre>
               */
              Boolean.FALSE);
      messageBuilder.addDomainValidationError(validator.validate(sprDomain));

      if (!specialProjectReferralExists(referralId, specialProjectId)) {
        return this.create(sprDomain);
      } else {
        return null;
      }

    } catch (Exception e) {
        messageBuilder.addMessageAndLog(e.getMessage(), e, LOGGER);
        return null;
    }
  }
  
  private Boolean validSpecialProjectReferral(String incidentCounty, MessageBuilder messageBuilder) {
    short governmentEntityType = convertLogicalIdToSystemCodeFor(incidentCounty, 
        LegacyTable.GOVERNMENT_ORGANIZATION_ENTITY.getName());
    if (governmentEntityType == 0) {
      String message = "Invalid Government Entity Type for county code : " + incidentCounty;
      messageBuilder.addMessageAndLog(message, LOGGER);
      return Boolean.FALSE;
    }
 
    String specialProjectId = findSpecialProjectId(S_CSEC_REFERRAL, governmentEntityType);   
    if (StringUtils.isBlank(specialProjectId)) {
      String message = "Special Project does not exist for: " 
        + S_CSEC_REFERRAL + " " + governmentEntityType;
      messageBuilder.addMessageAndLog(message, LOGGER);
      return Boolean.FALSE;
    }

    return Boolean.TRUE;
  }
  
  private Boolean specialProjectReferralExists(String referralId, String specialProjectId) {

    List<gov.ca.cwds.data.legacy.cms.entity.SpecialProjectReferral> specialProjectReferrals =
        specialProjectReferralDao.findSpecialProjectReferralsByReferralIdAndSpecialProjectId(
            referralId, specialProjectId);
    return specialProjectReferrals.isEmpty() ? Boolean.FALSE : Boolean.TRUE;
  }

  private String findSpecialProjectId(String specialProjectName, Short governmentEntityType) {
    List<SpecialProject> specialProjects = specialProjectDao
        .findSpecialProjectByGovernmentEntityAndName(specialProjectName, governmentEntityType);
    String specialProjectId = null;

    // use the first matching Special Project that is not end dated
    for (SpecialProject specialProject : specialProjects) {
      if (null == specialProject.getEndDate()) {
        specialProjectId = specialProject.getId();
        break;
      }
    }
    return specialProjectId;
  }

  /**
   * Process special project for Safely Surrendered Babies.
   * 
   * @param childClientId Child client ID.
   * @param referralId Referral ID
   * @param referralReceivedDate - referral received date
   * @param referralRecievedTime - referral received time
   * @param ssb Safely Surrendered Babies
   */
  public void processSafelySurrenderedBabies(String childClientId, String referralId,
      LocalDate referralReceivedDate, LocalTime referralRecievedTime,
      gov.ca.cwds.rest.api.domain.SafelySurrenderedBabies ssb) {

    LocalDateTime now = LocalDateTime.now();

    PerryUserIdentity perryUser = RequestExecutionContext.instance().getUserIdentity();

    String staffId = RequestExecutionContext.instance().getStaffId();
    String staffCountySpecificCode = perryUser.getCountyCode(); // 2 characters
    String staffCountyCode = perryUser.getCountyCwsCode(); // 4 digit

    /**
     * Find SSB special project for staff county
     */
    List<SpecialProject> ssbSpecialProjects =
        specialProjectDao.findActiveSafelySurrenderedBabiesSpecialProjectByGovernmentEntity(
            Short.valueOf(staffCountyCode));
    SpecialProject ssbSpecialProject =
        (ssbSpecialProjects != null && !ssbSpecialProjects.isEmpty()) ? ssbSpecialProjects.get(0)
            : null;

    if (ssbSpecialProject == null) {
      throw new ServiceException(
          "Special project not found for Safely Surrendered Babies for staff county "
              + staffCountyCode);
    }

    /**
     * Create SpecialProjectReferral persistence record.
     */
    gov.ca.cwds.rest.api.domain.cms.SpecialProjectReferral spr =
        new gov.ca.cwds.rest.api.domain.cms.SpecialProjectReferral();
    spr.setCountySpecificCode(staffCountySpecificCode);
    if (null == referralReceivedDate) {
      spr.setParticipantStartDate("");
    } else {
      spr.setParticipantStartDate(referralReceivedDate.toString());
    }
    spr.setReferralId(referralId);
    spr.setSpecialProjectId(ssbSpecialProject.getId());
    /**
     * <blockquote>
     * 
     * <pre>
     * BUSINESS RULE: "R - 7539" 
     * 
     * If (selected) SPECIAL_PROJECT_REFERRAL > SAFELY_SURRENDERED_BABIES exist, 
     * set SPECIAL_PROJECT_REFERRAL.Safely_Surrenderd_Babies_Ind_Var ='Y', else set to 'N'.
     * 
     * </blockquote>
     * </pre>
     */
    spr.setSafelySurrenderedBabiesIndicator(Boolean.TRUE);

    gov.ca.cwds.rest.api.domain.cms.SpecialProjectReferral createdSpr = this.create(spr);

    /**
     * Create SafelySurrenderedBabies persistence record.
     */
    SafelySurrenderedBabies ssbEntity = new SafelySurrenderedBabies();
    ssbEntity.setBraceletIdInfoCode(ssb.getBraceletInfoCode());
    ssbEntity.setChildClientId(childClientId);
    ssbEntity.setCommentDescription(ssb.getComments());
    ssbEntity.setCpsNotofiedIndicator(Boolean.TRUE);
    ssbEntity.setCpsNotofiedDate(referralReceivedDate);
    ssbEntity.setMedicalQuestionnaireCode(ssb.getMedicalQuestionaireCode());
    ssbEntity.setQuestionnaireReceivedDate(ssb.getMedicalQuestionaireReturnDate());
    ssbEntity.setCpsNotofiedTime(referralRecievedTime);
    ssbEntity.setLastUpdateId(staffId);
    ssbEntity.setLastUpdateTime(now);
    ssbEntity.setRelationToClient(ssb.getRelationToChild().shortValue());
    ssbEntity.setSpecialProjectReferral(createdSpr.getId());
    ssbEntity.setSurrenderedByName(ssb.getSurrenderedByName());
    ssbEntity.setSurrenderedDate(referralReceivedDate);
    ssbEntity.setSurrenderedTime(referralRecievedTime);

    /**
     * Create bracelet ID.
     */
    NonCWSNumber braceltInfo = new NonCWSNumber();
    braceltInfo.setClientId(childClientId);
    braceltInfo.setThirdId(CmsKeyIdGenerator.getNextValue(staffId));
    braceltInfo.setLastUpdateId(staffId);
    braceltInfo.setLastUpdateTime(now);
    braceltInfo.setOtherId(ssb.getBraceletId());
    braceltInfo.setOtherIdCode(MEDICAL_RECORD_SYSTEM_CODE_ID);

    Set<IssueDetails> detailsSet =
        droolsService.performBusinessRules(createConfiguration(), ssbEntity, braceltInfo);
    if (!detailsSet.isEmpty()) {
      throw new BusinessValidationException(detailsSet);
    }

    safelySurrenderedBabiesDao.create(ssbEntity);
    nonCWSNumberDao.create(braceltInfo);
  }

  private DroolsConfiguration<SafelySurrenderedBabies> createConfiguration() {
    return SafelySurrenderBabiesDroolsConfiguration.INSTANCE;
  }

  /**
   * {@inheritDoc}
   * 
   * @see gov.ca.cwds.rest.services.CrudsService#delete(java.io.Serializable)
   */
  @Override
  public gov.ca.cwds.rest.api.domain.cms.SpecialProjectReferral delete(String primaryKey) {
    throw new NotImplementedException("delete not implemented");
  }

  /**
   * {@inheritDoc}
   * 
   * @see gov.ca.cwds.rest.services.CrudsService#find(java.io.Serializable)
   */
  @Override
  public gov.ca.cwds.rest.api.domain.cms.SpecialProjectReferral find(String primaryKey) {
    throw new NotImplementedException("find not implemented");
  }

  /**
   * {@inheritDoc}
   * 
   * @see gov.ca.cwds.rest.services.CrudsService#update(java.io.Serializable,
   *      gov.ca.cwds.rest.api.Request)
   */
  @Override
  public gov.ca.cwds.rest.api.domain.cms.SpecialProjectReferral update(String primaryKey,
      gov.ca.cwds.rest.api.domain.cms.SpecialProjectReferral request) {
    throw new NotImplementedException("update not implemented");
  }

  /**
   * look up the sysId of the row in the SystemCode table that matches the passed user defined
   * logical code and FKSMeta
   * 
   * @param logicalCode
   * @param metaTableName
   * @return - sysId of the SystemCode
   */
  private short convertLogicalIdToSystemCodeFor(String logicalCode, String metaTableName) {
    short foundCode = 0;
    Set<SystemCode> systemCodes = SystemCodeCache.global().getSystemCodesForMeta(metaTableName);
    for (SystemCode systemCode : systemCodes) {
      if (systemCode.getLogicalId().equals(logicalCode)) {
        foundCode = systemCode.getSystemId();
        break;
      }
    }
    return foundCode;
  }

  public SpecialProjectDao getSpecialProjectDao() {
    return specialProjectDao;
  }

  public void setSpecialProjectDao(SpecialProjectDao specialProjectDao) {
    this.specialProjectDao = specialProjectDao;
  }

  public SpecialProjectReferralDao getSpecialProjectReferralDao() {
    return specialProjectReferralDao;
  }

  public void setSpecialProjectReferralDao(SpecialProjectReferralDao specialProjectReferralDao) {
    this.specialProjectReferralDao = specialProjectReferralDao;
  }

  public SafelySurrenderedBabiesDao getSafelySurrenderedBabiesDao() {
    return safelySurrenderedBabiesDao;
  }

  public void setSafelySurrenderedBabiesDao(SafelySurrenderedBabiesDao safelySurrenderedBabiesDao) {
    this.safelySurrenderedBabiesDao = safelySurrenderedBabiesDao;
  }

  public NonCWSNumberDao getNonCWSNumberDao() {
    return nonCWSNumberDao;
  }

  public void setNonCWSNumberDao(NonCWSNumberDao nonCWSNumberDao) {
    this.nonCWSNumberDao = nonCWSNumberDao;
  }

  public DroolsService getDroolsService() {
    return droolsService;
  }

  public void setDroolsService(DroolsService droolsService) {
    this.droolsService = droolsService;
  }

  public Validator getValidator() {
    return validator;
  }

  public void setValidator(Validator validator) {
    this.validator = validator;
  }

  /**
   * @return the riSpecialProjectReferral
   */
  public RISpecialProjectReferral getRiSpecialProjectReferral() {
    return riSpecialProjectReferral;
  }

  /**
   * @param riSpecialProjectReferral - riSpecialProjectReferral
   */
  public void setRiSpecialProjectReferral(RISpecialProjectReferral riSpecialProjectReferral) {
    this.riSpecialProjectReferral = riSpecialProjectReferral;
  }

}

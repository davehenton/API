package gov.ca.cwds.data.cms;

import static gov.ca.cwds.data.persistence.cms.Allegation.FIND_ALLEGATIONS_WITH_CLIENTS_BY_REFERRAL_IDS;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;

import com.google.inject.Inject;

import gov.ca.cwds.data.CrudsDaoImpl;
import gov.ca.cwds.data.persistence.cms.Allegation;
import gov.ca.cwds.inject.CmsSessionFactory;

/**
 * DAO for {@link Allegation}.
 *
 * @author CWDS API Team
 */
public class AllegationDao extends CrudsDaoImpl<Allegation> {

  /**
   * Constructor
   *
   * @param sessionFactory The sessionFactory
   */
  @Inject
  public AllegationDao(@CmsSessionFactory SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  /**
   * @param referralIds legacy referral ID-s
   * @return map where key is a referralId and value is a collection of Allegations of the referral
   */
  public Map<String, Set<Allegation>> findAllegationsWithClientsByReferralIds(
      Collection<String> referralIds) {
    if (referralIds == null || referralIds.isEmpty()) {
      return new HashMap<>();
    }

    @SuppressWarnings("unchecked") final Query<Allegation> query = this.grabSession()
        .getNamedQuery(FIND_ALLEGATIONS_WITH_CLIENTS_BY_REFERRAL_IDS);
    query.setParameter("referralIds", referralIds);

    final Map<String, Set<Allegation>> referralAllegationsMap = new HashMap<>(referralIds.size());
    for (Allegation allegation : query.list()) {
      if (!referralAllegationsMap.containsKey(allegation.getReferralId())) {
        referralAllegationsMap.put(allegation.getReferralId(), new HashSet<>());
      }
      referralAllegationsMap.get(allegation.getReferralId()).add(allegation);
    }
    return referralAllegationsMap;
  }
}

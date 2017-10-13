package gov.ca.cwds.rest.services.referentialintegrity;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import gov.ca.cwds.data.ApiHibernateInterceptor;
import gov.ca.cwds.data.ApiReferentialCheck;
import gov.ca.cwds.data.cms.CrossReportDao;
import gov.ca.cwds.data.cms.GovernmentOrganizationDao;
import gov.ca.cwds.data.cms.ReferralDao;
import gov.ca.cwds.data.persistence.cms.GovernmentOrganizationCrossReport;
import gov.ca.cwds.rest.validation.ReferentialIntegrityException;

/**
 * Verifies that a record refers to a valid referral, crossReport and governmentOrganization.
 * Returns true if all parent foreign keys exist when the transaction commits, otherwise false.
 * 
 * <p>
 * Validate any other constraints or business rules here before committing a transaction to the
 * database.
 * </p>
 * 
 * <p>
 * Enforce foreign key constraints using "normal" Hibernate mechanisms, such as this typical FK:
 * </p>
 * <blockquote>
 * 
 * <pre>
 * &#64;ManyToOne(optional = false)
 * &#64;JoinColumn(name = "FKCRSS_RPT", nullable = false, updatable = false, insertable = false)
 * private Referral referral;
 * 
 * &#64;ManyToOne(optional = false)
 * &#64;JoinColumn(name = "FKCRSS_RP0", nullable = false, updatable = false, insertable = false)
 * private CrossReport crossReport;
 * 
 * &#64;ManyToOne(optional = true)
 * &#64;JoinColumn(name = "FKGV_ORG_T", nullable = true, updatable = false, insertable = false)
 * private GovernmentOrganization governmentOrganization;
 * </pre>
 * 
 * </blockquote>
 * 
 * @author CWDS API Team
 * @see ApiHibernateInterceptor
 */
public class RIGovernmentOrganizationCrossReport
    implements ApiReferentialCheck<GovernmentOrganizationCrossReport> {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  private static final Logger LOGGER =
      LoggerFactory.getLogger(RIGovernmentOrganizationCrossReport.class);

  private transient CrossReportDao crossReportDao;
  private transient ReferralDao referralDao;
  private transient GovernmentOrganizationDao governmentOrganizationDao;


  /**
   * Constructor
   * 
   * @param referralDao - referralDao
   * @param crossReportDao - crossReportDao
   * @param governmentOrganizationDao - governmentOrganizationDao
   */
  @Inject
  public RIGovernmentOrganizationCrossReport(final CrossReportDao crossReportDao,
      ReferralDao referralDao, GovernmentOrganizationDao governmentOrganizationDao) {
    this.crossReportDao = crossReportDao;
    this.referralDao = referralDao;
    this.governmentOrganizationDao = governmentOrganizationDao;
    ApiHibernateInterceptor.addHandler(GovernmentOrganizationCrossReport.class,
        governmentOrganizationCrossReport -> apply(
            (GovernmentOrganizationCrossReport) governmentOrganizationCrossReport));
  }

  @Override
  public Boolean apply(GovernmentOrganizationCrossReport t) {
    LOGGER.debug("RI: GovernmentOrganizationCrossReport");
    if (crossReportDao.find(t.getCrossReportThirdId()) == null) {
      throw new ReferentialIntegrityException(
          "GovernmentOrganizationCrossReport => CrossReport with given Identifier is not present in database");
    } else if (referralDao.find(t.getReferralId()) == null) {
      throw new ReferentialIntegrityException(
          "GovernmentOrganizationCrossReport => Referral with given Identifier is not present in database");
    } else if (t.getGovernmentOrganizationId() != null
        && governmentOrganizationDao.find(t.getGovernmentOrganizationId()) == null) {
      throw new ReferentialIntegrityException(
          "GovernmentOrganizationCrossReport => GovernmentOrganization with given Identifier is not present in database");
    }
    return Boolean.TRUE;
  }

}
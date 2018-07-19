package gov.ca.cwds.data.ns;

import java.util.List;

import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import gov.ca.cwds.data.BaseDaoImpl;
import gov.ca.cwds.data.DaoException;
import gov.ca.cwds.data.persistence.ns.IntakeLov;
import gov.ca.cwds.inject.NsSessionFactory;

/**
 * DAO for Intake LOV codes.
 * 
 * @author CWDS API Team
 */
public class IntakeLovDao extends BaseDaoImpl<IntakeLov> {

  private static final Logger LOGGER = LoggerFactory.getLogger(IntakeLovDao.class);

  /**
   * Constructor.
   * 
   * @param sessionFactory The session factory
   */
  @Inject
  public IntakeLovDao(@NsSessionFactory SessionFactory sessionFactory) {
    super(sessionFactory);
  }

  /**
   * @param legacyCategoryId - legacyCategoryId
   * @return the intake code based on the category id
   */
  @SuppressWarnings("unchecked")
  public List<IntakeLov> findByLegacyMetaId(String legacyCategoryId) {
    final String namedQueryName = IntakeLov.class.getName() + ".findByLegacyCategoryId";
    final Session session = grabSession();
    joinTransaction(session);

    try {
      final Query<IntakeLov> query =
          session.getNamedQuery(namedQueryName).setParameter("legacyCategoryId", legacyCategoryId);

      query.setReadOnly(true);
      query.setCacheable(false);
      query.setHibernateFlushMode(FlushMode.MANUAL);
      return query.list();
    } catch (HibernateException h) {
      LOGGER.error("ERROR FINDING META! {}", h.getMessage(), h);
      throw new DaoException(h);
    }
  }

  /**
   * @param legacySystemCodeId - legacySystemCodeId
   * @return the intakeLov
   */
  @SuppressWarnings("unchecked")
  public IntakeLov findByLegacySystemCodeId(Number legacySystemCodeId) {
    final String namedQueryName = IntakeLov.class.getName() + ".findByLegacySystemId";
    final Session session = grabSession();
    joinTransaction(session);

    try {
      final Query<IntakeLov> query = session.getNamedQuery(namedQueryName).setReadOnly(true)
          .setCacheable(false).setHibernateFlushMode(FlushMode.MANUAL)
          .setShort("legacySystemCodeId", legacySystemCodeId.shortValue());
      return query.getSingleResult();
    } catch (HibernateException h) {
      LOGGER.error("ERROR FINDING CODE! {}", h.getMessage(), h);
      throw new DaoException(h);
    }
  }

}
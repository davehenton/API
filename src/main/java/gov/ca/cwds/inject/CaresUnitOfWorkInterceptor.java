package gov.ca.cwds.inject;

import static gov.ca.cwds.data.HibernateStatisticsConsumerRegistry.prepareHibernateStatisticsConsumer;
import static gov.ca.cwds.data.HibernateStatisticsConsumerRegistry.provideHibernateStatistics;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;
import com.google.inject.Inject;

import gov.ca.cwds.data.dao.cms.BaseAuthorizationDao;
import gov.ca.cwds.data.persistence.xa.PhineasMethodLoggerInterceptor;
import gov.ca.cwds.data.persistence.xa.WorkFerbUserInfo;
import gov.ca.cwds.rest.core.Api;
import gov.ca.cwds.rest.filters.RequestExecutionContext;
import gov.ca.cwds.rest.filters.RequestExecutionContext.Parameter;
import io.dropwizard.hibernate.UnitOfWork;
import io.dropwizard.hibernate.UnitOfWorkAspect;

/**
 * AOP method interceptor manages database transactions outside of DropWizard resource classes using
 * annotation {@code @UnitOfWork}.
 * 
 * <p>
 * Note that this interceptor is re-entrant; nested {@code @UnitOfWork} annotations for a previously
 * encountered datasource will not grab new sessions or connections.
 * </p>
 * 
 * <h3>Sample Usage</h3>
 *
 * <blockquote>
 *
 * <pre>
 * final UnitOfWorkInterceptor interceptor = new UnitOfWorkInterceptor();
 * bindInterceptor(Matchers.any(), Matchers.annotatedWith(UnitOfWork.class), interceptor);
 * requestInjection(interceptor);
 * </pre>
 *
 * </blockquote>
 *
 * @author CWDS API Team
 */
public class CaresUnitOfWorkInterceptor extends PhineasMethodLoggerInterceptor {

  private static final long serialVersionUID = 1L;

  private static final Logger LOGGER = LoggerFactory.getLogger(CaresUnitOfWorkInterceptor.class);

  @Inject
  @CmsSessionFactory
  SessionFactory cmsSessionFactory;

  @Inject
  @CwsRsSessionFactory
  SessionFactory rsSessionFactory;

  @Inject
  @NsSessionFactory
  SessionFactory nsSessionFactory;

  // SessionFactory by name.
  private static final ThreadLocal<Map<String, UnitOfWorkAspect>> requestAspects =
      ThreadLocal.withInitial(HashMap::new);

  @Override
  protected void resetRequest() {
    super.resetRequest();
    requestAspects.get().clear();
  }

  @Override
  public Object invoke(org.aopalliance.intercept.MethodInvocation mi) throws Throwable {
    final Method m = mi.getMethod();
    final RequestExecutionContext ctx = RequestExecutionContext.instance();
    LOGGER.info("Unit of work interceptor: class: {}, method: {}", m.getDeclaringClass(),
        m.getName());

    // If already in an XA transaction, skip this @UnitOfWork.
    if (ctx != null && RequestExecutionContext.instance().isXaTransaction()) {
      LOGGER.warn("******* XA TRANSACTION: SKIP @UnitOfWork! class: {}, method: {}******* ",
          m.getDeclaringClass(), m.getName());
      return mi.proceed();
    }

    // Use our wrapped (Candace) session factories and related wrappers.
    final UnitOfWork annotation = mi.getMethod().getAnnotation(UnitOfWork.class);
    final String name = annotation.value().trim();
    SessionFactory currentSessionFactory;

    // Find the right session factory.
    switch (name) {
      case Api.DS_CMS:
        currentSessionFactory = cmsSessionFactory;
        break;
      case Api.DATASOURCE_CMS_REP:
        currentSessionFactory = rsSessionFactory;
        break;
      case Api.DS_NS:
        currentSessionFactory = nsSessionFactory;
        break;
      default:
        throw new IllegalStateException("Unknown datasource! " + annotation.value());
    }

    LOGGER.debug("{}", name);

    // Not XA, so clear XA flags.
    BaseAuthorizationDao.clearXaMode();
    RequestExecutionContext.instance().put(Parameter.XA_TRANSACTION, Boolean.FALSE);

    // Does this request already have an aspect for this session factory?
    UnitOfWorkAspect aspect;
    boolean firstUnitOfWork = false;

    if (requestAspects.get().containsKey(name)) {
      LOGGER.info("RE-USE @UnitOfWork aspect: class: {}, method: {}, session factory: {}",
          m.getDeclaringClass(), m.getName(), name);
      aspect = requestAspects.get().get(name);
    } else {
      LOGGER.info("NEW @UnitOfWork aspect: class: {}, method: {}, session factory: {}",
          m.getDeclaringClass(), m.getName(), name);
      firstUnitOfWork = true;
      aspect = UnitOfWorkModule.getUnitOfWorkProxyFactory(name, currentSessionFactory)
          .newAspect(ImmutableMap.of(name, currentSessionFactory));
      requestAspects.get().put(name, aspect);
      aspect.beforeStart(annotation);

      // Set client information on the JDBC connection.
      currentSessionFactory.getCurrentSession().doWork(new WorkFerbUserInfo());
      prepareHibernateStatisticsConsumer(name, currentSessionFactory.getStatistics());
    }

    boolean barfed = false;
    try {
      final Object result = mi.proceed();
      final long totalCalls = incrementTotalCount(m);
      final long requestCalls = incrementRequestCount(m);
      LOGGER.info("@UnitOfWork interceptor: after  method: {}, total: {}, request: {}", m,
          totalCalls, requestCalls);

      if (firstUnitOfWork) {
        provideHibernateStatistics(name, currentSessionFactory.getStatistics());
        aspect.afterEnd(); // commit
      }

      return result;
    } catch (Exception e) {
      LOGGER.error("UNIT OF WORK FAILED! {}", e.getMessage(), e);
      barfed = true;
      aspect.onError();
      throw e;
    } finally {
      LOGGER.debug("Unit of work finished");
      if (barfed || firstUnitOfWork) {
        aspect.onFinish();
        resetRequest();
      }
    }
  }

  public SessionFactory getCmsSessionFactory() {
    return cmsSessionFactory;
  }

  public void setCmsSessionFactory(SessionFactory cmsSessionFactory) {
    this.cmsSessionFactory = cmsSessionFactory;
  }

  public SessionFactory getRsSessionFactory() {
    return rsSessionFactory;
  }

  public void setRsSessionFactory(SessionFactory rsSessionFactory) {
    this.rsSessionFactory = rsSessionFactory;
  }

  public SessionFactory getNsSessionFactory() {
    return nsSessionFactory;
  }

  public void setNsSessionFactory(SessionFactory nsSessionFactory) {
    this.nsSessionFactory = nsSessionFactory;
  }

}

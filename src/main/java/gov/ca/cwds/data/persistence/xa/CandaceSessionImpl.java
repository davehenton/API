package gov.ca.cwds.data.persistence.xa;

import java.io.Serializable;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManagerFactory;
import javax.persistence.FlushModeType;
import javax.persistence.LockModeType;
import javax.persistence.StoredProcedureQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaDelete;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.CriteriaUpdate;
import javax.persistence.metamodel.Metamodel;

import org.hibernate.CacheMode;
import org.hibernate.Criteria;
import org.hibernate.Filter;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.IdentifierLoadAccess;
import org.hibernate.LobHelper;
import org.hibernate.LockMode;
import org.hibernate.LockOptions;
import org.hibernate.MultiIdentifierLoadAccess;
import org.hibernate.NaturalIdLoadAccess;
import org.hibernate.ReplicationMode;
import org.hibernate.Session;
import org.hibernate.SessionEventListener;
import org.hibernate.SessionFactory;
import org.hibernate.SharedSessionBuilder;
import org.hibernate.SimpleNaturalIdLoadAccess;
import org.hibernate.Transaction;
import org.hibernate.TypeHelper;
import org.hibernate.UnknownProfileException;
import org.hibernate.jdbc.ReturningWork;
import org.hibernate.jdbc.Work;
import org.hibernate.procedure.ProcedureCall;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.stat.SessionStatistics;

public class CandaceSessionImpl implements Session {

  private static final long serialVersionUID = 1L;

  protected final Session session;

  public CandaceSessionImpl(Session session) {
    this.session = session;
  }

  @Override
  public Query getNamedQuery(String queryName) {
    return session.getNamedQuery(queryName);
  }

  @Override
  public String getTenantIdentifier() {
    return session.getTenantIdentifier();
  }

  @Override
  public Session getSession() {
    return session.getSession();
  }

  @Override
  public void close() throws HibernateException {
    session.close();
  }

  @Override
  public boolean isOpen() {
    return session.isOpen();
  }

  @Override
  public boolean isConnected() {
    return session.isConnected();
  }

  @Override
  public Transaction beginTransaction() {
    return session.beginTransaction();
  }

  @Override
  public Transaction getTransaction() {
    return session.getTransaction();
  }

  @Override
  public Query createNamedQuery(String name) {
    return session.createNamedQuery(name);
  }

  @Override
  public ProcedureCall getNamedProcedureCall(String name) {
    return session.getNamedProcedureCall(name);
  }

  @Override
  public ProcedureCall createStoredProcedureCall(String procedureName) {
    return session.createStoredProcedureCall(procedureName);
  }

  @Override
  public ProcedureCall createStoredProcedureCall(String procedureName, Class... resultClasses) {
    return session.createStoredProcedureCall(procedureName, resultClasses);
  }

  @Override
  public ProcedureCall createStoredProcedureCall(String procedureName,
      String... resultSetMappings) {
    return session.createStoredProcedureCall(procedureName, resultSetMappings);
  }

  @Override
  public NativeQuery createSQLQuery(String queryString) {
    return session.createSQLQuery(queryString);
  }

  @Override
  public void remove(Object entity) {
    session.remove(entity);
  }

  @Override
  public Criteria createCriteria(Class persistentClass) {
    return session.createCriteria(persistentClass);
  }

  @Override
  public NativeQuery createNativeQuery(String sqlString) {
    return session.createNativeQuery(sqlString);
  }

  @Override
  public SharedSessionBuilder sessionWithOptions() {
    return session.sessionWithOptions();
  }

  @Override
  public Criteria createCriteria(Class persistentClass, String alias) {
    return session.createCriteria(persistentClass, alias);
  }

  @Override
  public <T> T find(Class<T> entityClass, Object primaryKey) {
    return session.find(entityClass, primaryKey);
  }

  @Override
  public void flush() throws HibernateException {
    session.flush();
  }

  @Override
  public Criteria createCriteria(String entityName) {
    return session.createCriteria(entityName);
  }

  @Override
  public NativeQuery createNativeQuery(String sqlString, String resultSetMapping) {
    return session.createNativeQuery(sqlString, resultSetMapping);
  }

  @Override
  public void setFlushMode(FlushMode flushMode) {
    session.setFlushMode(flushMode);
  }

  @Override
  public <T> T find(Class<T> entityClass, Object primaryKey, Map<String, Object> properties) {
    return session.find(entityClass, primaryKey, properties);
  }

  @Override
  public Criteria createCriteria(String entityName, String alias) {
    return session.createCriteria(entityName, alias);
  }

  @Override
  public NativeQuery getNamedSQLQuery(String name) {
    return session.getNamedSQLQuery(name);
  }

  @Override
  public Integer getJdbcBatchSize() {
    return session.getJdbcBatchSize();
  }

  @Override
  public FlushModeType getFlushMode() {
    return session.getFlushMode();
  }

  @Override
  public NativeQuery getNamedNativeQuery(String name) {
    return session.getNamedNativeQuery(name);
  }

  @Override
  public void setJdbcBatchSize(Integer jdbcBatchSize) {
    session.setJdbcBatchSize(jdbcBatchSize);
  }

  @Override
  public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode) {
    return session.find(entityClass, primaryKey, lockMode);
  }

  @Override
  public void setHibernateFlushMode(FlushMode flushMode) {
    session.setHibernateFlushMode(flushMode);
  }

  @Override
  public FlushMode getHibernateFlushMode() {
    return session.getHibernateFlushMode();
  }

  @Override
  public void setCacheMode(CacheMode cacheMode) {
    session.setCacheMode(cacheMode);
  }

  @Override
  public CacheMode getCacheMode() {
    return session.getCacheMode();
  }

  @Override
  public SessionFactory getSessionFactory() {
    return session.getSessionFactory();
  }

  @Override
  public void cancelQuery() throws HibernateException {
    session.cancelQuery();
  }

  @Override
  public boolean isDirty() throws HibernateException {
    return session.isDirty();
  }

  @Override
  public boolean isDefaultReadOnly() {
    return session.isDefaultReadOnly();
  }

  @Override
  public <T> T find(Class<T> entityClass, Object primaryKey, LockModeType lockMode,
      Map<String, Object> properties) {
    return session.find(entityClass, primaryKey, lockMode, properties);
  }

  @Override
  public void setDefaultReadOnly(boolean readOnly) {
    session.setDefaultReadOnly(readOnly);
  }

  @Override
  public Serializable getIdentifier(Object object) {
    return session.getIdentifier(object);
  }

  @Override
  public boolean contains(String entityName, Object object) {
    return session.contains(entityName, object);
  }

  @Override
  public void evict(Object object) {
    session.evict(object);
  }

  @Override
  public <T> T load(Class<T> theClass, Serializable id, LockMode lockMode) {
    return session.load(theClass, id, lockMode);
  }

  @Override
  public <T> T getReference(Class<T> entityClass, Object primaryKey) {
    return session.getReference(entityClass, primaryKey);
  }

  @Override
  public <T> T load(Class<T> theClass, Serializable id, LockOptions lockOptions) {
    return session.load(theClass, id, lockOptions);
  }

  @Override
  public Object load(String entityName, Serializable id, LockMode lockMode) {
    return session.load(entityName, id, lockMode);
  }

  @Override
  public void setFlushMode(FlushModeType flushMode) {
    session.setFlushMode(flushMode);
  }

  @Override
  public Object load(String entityName, Serializable id, LockOptions lockOptions) {
    return session.load(entityName, id, lockOptions);
  }

  @Override
  public void lock(Object entity, LockModeType lockMode) {
    session.lock(entity, lockMode);
  }

  @Override
  public <T> T load(Class<T> theClass, Serializable id) {
    return session.load(theClass, id);
  }

  @Override
  public Object load(String entityName, Serializable id) {
    return session.load(entityName, id);
  }

  @Override
  public void load(Object object, Serializable id) {
    session.load(object, id);
  }

  @Override
  public void lock(Object entity, LockModeType lockMode, Map<String, Object> properties) {
    session.lock(entity, lockMode, properties);
  }

  @Override
  public void replicate(Object object, ReplicationMode replicationMode) {
    session.replicate(object, replicationMode);
  }

  @Override
  public void replicate(String entityName, Object object, ReplicationMode replicationMode) {
    session.replicate(entityName, object, replicationMode);
  }

  @Override
  public Serializable save(Object object) {
    return session.save(object);
  }

  @Override
  public Serializable save(String entityName, Object object) {
    return session.save(entityName, object);
  }

  @Override
  public void saveOrUpdate(Object object) {
    session.saveOrUpdate(object);
  }

  @Override
  public void saveOrUpdate(String entityName, Object object) {
    session.saveOrUpdate(entityName, object);
  }

  @Override
  public void refresh(Object entity, Map<String, Object> properties) {
    session.refresh(entity, properties);
  }

  @Override
  public void update(Object object) {
    session.update(object);
  }

  @Override
  public void update(String entityName, Object object) {
    session.update(entityName, object);
  }

  @Override
  public void refresh(Object entity, LockModeType lockMode) {
    session.refresh(entity, lockMode);
  }

  @Override
  public Object merge(Object object) {
    return session.merge(object);
  }

  @Override
  public Object merge(String entityName, Object object) {
    return session.merge(entityName, object);
  }

  @Override
  public void persist(Object object) {
    session.persist(object);
  }

  @Override
  public void refresh(Object entity, LockModeType lockMode, Map<String, Object> properties) {
    session.refresh(entity, lockMode, properties);
  }

  @Override
  public void persist(String entityName, Object object) {
    session.persist(entityName, object);
  }

  @Override
  public void delete(Object object) {
    session.delete(object);
  }

  @Override
  public void delete(String entityName, Object object) {
    session.delete(entityName, object);
  }

  @Override
  public void lock(Object object, LockMode lockMode) {
    session.lock(object, lockMode);
  }

  @Override
  public void lock(String entityName, Object object, LockMode lockMode) {
    session.lock(entityName, object, lockMode);
  }

  @Override
  public void detach(Object entity) {
    session.detach(entity);
  }

  @Override
  public boolean contains(Object entity) {
    return session.contains(entity);
  }

  @Override
  public LockRequest buildLockRequest(LockOptions lockOptions) {
    return session.buildLockRequest(lockOptions);
  }

  @Override
  public LockModeType getLockMode(Object entity) {
    return session.getLockMode(entity);
  }

  @Override
  public void refresh(Object object) {
    session.refresh(object);
  }

  @Override
  public void setProperty(String propertyName, Object value) {
    session.setProperty(propertyName, value);
  }

  @Override
  public Map<String, Object> getProperties() {
    return session.getProperties();
  }

  @Override
  public void refresh(String entityName, Object object) {
    session.refresh(entityName, object);
  }

  @Override
  public void refresh(Object object, LockMode lockMode) {
    session.refresh(object, lockMode);
  }

  @Override
  public void refresh(Object object, LockOptions lockOptions) {
    session.refresh(object, lockOptions);
  }

  @Override
  public void refresh(String entityName, Object object, LockOptions lockOptions) {
    session.refresh(entityName, object, lockOptions);
  }

  @Override
  public LockMode getCurrentLockMode(Object object) {
    return session.getCurrentLockMode(object);
  }

  @Override
  public Query createFilter(Object collection, String queryString) {
    return session.createFilter(collection, queryString);
  }

  @Override
  public void clear() {
    session.clear();
  }

  @Override
  public <T> T get(Class<T> entityType, Serializable id) {
    return session.get(entityType, id);
  }

  @Override
  public <T> T get(Class<T> entityType, Serializable id, LockMode lockMode) {
    return session.get(entityType, id, lockMode);
  }

  @Override
  public <T> T get(Class<T> entityType, Serializable id, LockOptions lockOptions) {
    return session.get(entityType, id, lockOptions);
  }

  @Override
  public StoredProcedureQuery createNamedStoredProcedureQuery(String name) {
    return session.createNamedStoredProcedureQuery(name);
  }

  @Override
  public Object get(String entityName, Serializable id) {
    return session.get(entityName, id);
  }

  @Override
  public StoredProcedureQuery createStoredProcedureQuery(String procedureName) {
    return session.createStoredProcedureQuery(procedureName);
  }

  @Override
  public Object get(String entityName, Serializable id, LockMode lockMode) {
    return session.get(entityName, id, lockMode);
  }

  @Override
  public StoredProcedureQuery createStoredProcedureQuery(String procedureName,
      Class... resultClasses) {
    return session.createStoredProcedureQuery(procedureName, resultClasses);
  }

  @Override
  public Object get(String entityName, Serializable id, LockOptions lockOptions) {
    return session.get(entityName, id, lockOptions);
  }

  @Override
  public StoredProcedureQuery createStoredProcedureQuery(String procedureName,
      String... resultSetMappings) {
    return session.createStoredProcedureQuery(procedureName, resultSetMappings);
  }

  @Override
  public String getEntityName(Object object) {
    return session.getEntityName(object);
  }

  @Override
  public IdentifierLoadAccess byId(String entityName) {
    return session.byId(entityName);
  }

  @Override
  public <T> MultiIdentifierLoadAccess<T> byMultipleIds(Class<T> entityClass) {
    return session.byMultipleIds(entityClass);
  }

  @Override
  public void joinTransaction() {
    session.joinTransaction();
  }

  @Override
  public MultiIdentifierLoadAccess byMultipleIds(String entityName) {
    return session.byMultipleIds(entityName);
  }

  @Override
  public boolean isJoinedToTransaction() {
    return session.isJoinedToTransaction();
  }

  @Override
  public <T> IdentifierLoadAccess<T> byId(Class<T> entityClass) {
    return session.byId(entityClass);
  }

  @Override
  public <T> T unwrap(Class<T> cls) {
    return session.unwrap(cls);
  }

  @Override
  public NaturalIdLoadAccess byNaturalId(String entityName) {
    return session.byNaturalId(entityName);
  }

  @Override
  public Object getDelegate() {
    return session.getDelegate();
  }

  @Override
  public <T> NaturalIdLoadAccess<T> byNaturalId(Class<T> entityClass) {
    return session.byNaturalId(entityClass);
  }

  @Override
  public SimpleNaturalIdLoadAccess bySimpleNaturalId(String entityName) {
    return session.bySimpleNaturalId(entityName);
  }

  @Override
  public <T> SimpleNaturalIdLoadAccess<T> bySimpleNaturalId(Class<T> entityClass) {
    return session.bySimpleNaturalId(entityClass);
  }

  @Override
  public EntityManagerFactory getEntityManagerFactory() {
    return session.getEntityManagerFactory();
  }

  @Override
  public Filter enableFilter(String filterName) {
    return session.enableFilter(filterName);
  }

  @Override
  public Filter getEnabledFilter(String filterName) {
    return session.getEnabledFilter(filterName);
  }

  @Override
  public CriteriaBuilder getCriteriaBuilder() {
    return session.getCriteriaBuilder();
  }

  @Override
  public void disableFilter(String filterName) {
    session.disableFilter(filterName);
  }

  @Override
  public Metamodel getMetamodel() {
    return session.getMetamodel();
  }

  @Override
  public SessionStatistics getStatistics() {
    return session.getStatistics();
  }

  @Override
  public boolean isReadOnly(Object entityOrProxy) {
    return session.isReadOnly(entityOrProxy);
  }

  @Override
  public <T> EntityGraph<T> createEntityGraph(Class<T> rootType) {
    return session.createEntityGraph(rootType);
  }

  @Override
  public EntityGraph<?> createEntityGraph(String graphName) {
    return session.createEntityGraph(graphName);
  }

  @Override
  public void setReadOnly(Object entityOrProxy, boolean readOnly) {
    session.setReadOnly(entityOrProxy, readOnly);
  }

  @Override
  public EntityGraph<?> getEntityGraph(String graphName) {
    return session.getEntityGraph(graphName);
  }

  @Override
  public <T> List<EntityGraph<? super T>> getEntityGraphs(Class<T> entityClass) {
    return session.getEntityGraphs(entityClass);
  }

  @Override
  public void doWork(Work work) throws HibernateException {
    session.doWork(work);
  }

  @Override
  public <T> T doReturningWork(ReturningWork<T> work) throws HibernateException {
    return session.doReturningWork(work);
  }

  @Override
  public Connection disconnect() {
    return session.disconnect();
  }

  @Override
  public void reconnect(Connection connection) {
    session.reconnect(connection);
  }

  @Override
  public boolean isFetchProfileEnabled(String name) throws UnknownProfileException {
    return session.isFetchProfileEnabled(name);
  }

  @Override
  public void enableFetchProfile(String name) throws UnknownProfileException {
    session.enableFetchProfile(name);
  }

  @Override
  public void disableFetchProfile(String name) throws UnknownProfileException {
    session.disableFetchProfile(name);
  }

  @Override
  public TypeHelper getTypeHelper() {
    return session.getTypeHelper();
  }

  @Override
  public LobHelper getLobHelper() {
    return session.getLobHelper();
  }

  @Override
  public void addEventListeners(SessionEventListener... listeners) {
    session.addEventListeners(listeners);
  }

  @Override
  public Query createQuery(String queryString) {
    return session.createQuery(queryString);
  }

  @Override
  public <T> Query<T> createQuery(String queryString, Class<T> resultType) {
    return session.createQuery(queryString, resultType);
  }

  @Override
  public <T> Query<T> createQuery(CriteriaQuery<T> criteriaQuery) {
    return session.createQuery(criteriaQuery);
  }

  @Override
  public Query createQuery(CriteriaUpdate updateQuery) {
    return session.createQuery(updateQuery);
  }

  @Override
  public Query createQuery(CriteriaDelete deleteQuery) {
    return session.createQuery(deleteQuery);
  }

  @Override
  public <T> Query<T> createNamedQuery(String name, Class<T> resultType) {
    return session.createNamedQuery(name, resultType);
  }

  /*
   * Confusing and contradictory method signatures.
   * 
   * @see org.hibernate.query.QueryProducer#createNativeQuery(java.lang.String, java.lang.Class)
   */
  @Override
  // public <R> NativeQuery<R> createNativeQuery(String sqlString, Class<R> resultClass) {
  public NativeQuery createNativeQuery(String sqlString, Class resultClass) {
    return session.createNativeQuery(sqlString, resultClass);
  }

}

package gov.ca.cwds.data.es.transform;

import static gov.ca.cwds.rest.core.Api.DS_CMS;

import org.apache.commons.lang3.NotImplementedException;
import org.hibernate.FlushMode;

import com.google.inject.Inject;

import gov.ca.cwds.rest.resources.SimpleResourceService;
import gov.ca.cwds.rest.services.TypedCrudsService;
import io.dropwizard.hibernate.UnitOfWork;

/**
 * Business service for live Elasticsearch client.
 * 
 * @author CWDS API Team
 */
public class LiveElasticClientService
    extends SimpleResourceService<String[], LiveElasticClientRequest, LiveElasticClientResponse>
    implements TypedCrudsService<String[], LiveElasticClientRequest, LiveElasticClientResponse> {

  /**
   * Constructor
   */
  @Inject
  public LiveElasticClientService() {
    // Default, no-op.
  }

  @Override
  protected LiveElasticClientResponse handleRequest(LiveElasticClientRequest req) {
    return handleFind(req.getClientIds().toArray(new String[0]));
  }

  @UnitOfWork(value = DS_CMS, readOnly = true, transactional = false, flushMode = FlushMode.MANUAL)
  @Override
  protected LiveElasticClientResponse handleFind(String[] key) {
    // TODO: call LiveElasticClientHandler
    return new LiveElasticClientResponse("placeholder");
  }

  @Override
  public LiveElasticClientResponse create(LiveElasticClientRequest arg0) {
    throw new NotImplementedException("create not implemented");
  }

  @Override
  public LiveElasticClientResponse delete(String[] arg0) {
    throw new NotImplementedException("delete not implemented");
  }

  @Override
  public LiveElasticClientResponse update(String[] arg0, LiveElasticClientRequest arg1) {
    throw new NotImplementedException("update not implemented");
  }

}

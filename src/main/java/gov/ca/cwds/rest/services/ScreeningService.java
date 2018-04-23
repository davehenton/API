package gov.ca.cwds.rest.services;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.NotImplementedException;
import org.elasticsearch.action.index.IndexRequestBuilder;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.rest.RestStatus;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.name.Named;

import gov.ca.cwds.ObjectMapperUtils;
import gov.ca.cwds.data.es.ElasticsearchDao;
import gov.ca.cwds.data.ns.ScreeningDao;
import gov.ca.cwds.data.persistence.ns.ScreeningWrapper;
import gov.ca.cwds.rest.api.Request;
import gov.ca.cwds.rest.api.Response;
import gov.ca.cwds.rest.api.domain.Screening;
import gov.ca.cwds.rest.api.domain.ScreeningDashboard;
import gov.ca.cwds.rest.api.domain.ScreeningDashboardList;
import gov.ca.cwds.rest.filters.RequestExecutionContext;

/**
 * Business layer object to work on {@link Screening}
 * 
 * @author CWDS API Team
 */
public class ScreeningService implements CrudsService {

  private static final ObjectMapper OBJECT_MAPPER = ObjectMapperUtils.createObjectMapper();

  private ElasticsearchDao esDao;
  private ScreeningDao screeningDao;

  /**
   * Construct the object
   * 
   * @param esDao Screenings ES DAO
   * @param screeningDao - Screening DAO
   */
  @Inject
  public ScreeningService(@Named("screenings.index") ElasticsearchDao esDao,
      ScreeningDao screeningDao) {
    this.esDao = esDao;
    this.screeningDao = screeningDao;
  }

  /**
   * {@inheritDoc}
   * 
   * @see gov.ca.cwds.rest.services.CrudsService#find(java.io.Serializable)
   */
  @Override
  public Response find(Serializable primaryKey) {
    throw new NotImplementedException("Find is not implemented");
  }

  /**
   * Return the screening dashboard of the logged in user.
   * 
   * @return - array of screening dashboard objects
   * @see gov.ca.cwds.rest.services.CrudsService#delete(java.io.Serializable)
   */
  public Response findScreeningDashboard() {
    return getScreeningsOfUser(RequestExecutionContext.instance().getStaffId());
  }

  private ScreeningDashboardList getScreeningsOfUser(String staffId) {
    List<ScreeningWrapper> screenings = screeningDao.findScreeningsByUserId(staffId);
    List<ScreeningDashboard> screeningDashboard = new ArrayList<>(screenings.size());
    for (ScreeningWrapper screening : screenings) {
      ScreeningDashboard thisScreening = new ScreeningDashboard(screening);
      screeningDashboard.add(thisScreening);
    }
    return new ScreeningDashboardList(screeningDashboard);
  }

  /**
   * {@inheritDoc}
   * 
   * @see gov.ca.cwds.rest.services.CrudsService#delete(java.io.Serializable)
   */
  @Override
  public Response delete(Serializable primaryKey) {
    throw new NotImplementedException("Delete is not implemented");
  }

  /**
   * {@inheritDoc}
   * 
   * @param request the request
   * 
   * @see gov.ca.cwds.rest.services.CrudsService#create(gov.ca.cwds.rest.api.Request)
   */
  @Override
  public Screening create(Request request) {
    index(request);
    return (Screening) request;
  }

  /**
   * {@inheritDoc}
   * 
   * @see gov.ca.cwds.rest.services.CrudsService#update(java.io.Serializable,
   *      gov.ca.cwds.rest.api.Request)
   */
  @Override
  public Screening update(Serializable primaryKey, Request request) {
    assert primaryKey instanceof String;
    assert request instanceof Screening;
    Screening screening = (Screening) request;

    if (!primaryKey.equals(screening.getId())) {
      throw new ServiceException(
          "Primary key mismatch, [" + primaryKey + " != " + screening.getId() + "]");
    }

    index(request);
    return (Screening) request;
  }

  /**
   * Convert given screening to JSON.
   * 
   * @param screening Screening to convert to JSON format
   * @return Screening as JSON format
   */
  private String toJson(Screening screening) {
    String screeningJson;
    try {
      screeningJson = OBJECT_MAPPER.writeValueAsString(screening);
    } catch (JsonProcessingException e) {
      throw new ServiceException(e);
    }
    return screeningJson;
  }

  /**
   * Index given screening request.
   * 
   * @param request The screening request
   * @return The IndexResponse
   */
  private IndexResponse index(Request request) {
    assert request instanceof Screening;
    Screening screening = (Screening) request;
    String screeningJson = toJson(screening);

    IndexRequestBuilder builder =
        esDao.getClient().prepareIndex(esDao.getConfig().getElasticsearchAlias(),
            esDao.getConfig().getElasticsearchDocType(), screening.getId());
    builder.setSource(screeningJson, XContentType.JSON);

    IndexResponse response = builder.get();
    RestStatus status = response.status();
    boolean success = RestStatus.OK == status || RestStatus.CREATED == status;

    if (!success) {
      throw new ServiceException(
          "Could not index screening. Status: " + status.getStatus() + ", Response: " + response);
    }

    return response;
  }

}

package gov.ca.cwds.data.es.transform;

import static gov.ca.cwds.rest.core.Api.RESOURCE_CLIENT;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;

import gov.ca.cwds.inject.LiveElasticClientServiceResource;
import gov.ca.cwds.rest.resources.SimpleResourceDelegate;
import gov.ca.cwds.rest.resources.converter.ResponseConverter;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * Light-weight endpoint to retrieve <strong>live</strong> client results in the exact same format
 * as Elasticsearch query results.
 * 
 * @author CWDS API Team
 */
@Api(value = RESOURCE_CLIENT, tags = {RESOURCE_CLIENT})
@Path(value = RESOURCE_CLIENT)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class LiveElasticClientResource {

  /**
   * Java lacks short-hand notation for typed interfaces and classes, such as C++ "typedef" or
   * "using alias", resulting in verbose type declarations.
   * 
   * <h4>Resource Delegate Type Parameters</h4>
   * 
   * <table>
   * <tr>
   * <th>Param</th>
   * <th>Purpose</th>
   * <th>Class</th>
   * </tr>
   * <tr>
   * <td>K</td>
   * <td>Key</td>
   * <td>String</td>
   * </tr>
   * <tr>
   * <td>Q</td>
   * <td>API Request</td>
   * <td>LiveElasticClientRequest</td>
   * </tr>
   * <tr>
   * <td>P</td>
   * <td>API Response</td>
   * <td>LiveElasticClientResponse</td>
   * </tr>
   * <tr>
   * <td>S</td>
   * <td>Service</td>
   * <td>LiveElasticClientService</td>
   * </tr>
   * </table>
   */
  private SimpleResourceDelegate<String[], LiveElasticClientRequest, LiveElasticClientResponse, LiveElasticClientService> resourceDelegate;

  /**
   * Preferred constructor.
   *
   * @param liveElasticClientService - LiveElasticClientService
   */
  @Inject
  public LiveElasticClientResource(
      @LiveElasticClientServiceResource SimpleResourceDelegate<String[], LiveElasticClientRequest, LiveElasticClientResponse, LiveElasticClientService> resourceDelegate) {
    this.resourceDelegate = resourceDelegate;
  }

  /**
   * Finds live Elasticsearch-like search results from a list of client ids.
   *
   * @param clientIds client id list
   * @return the response
   */
  @GET
  @Path("/live_elastic_client")
  @ApiResponses(value = {@ApiResponse(code = 401, message = "Not Authorized"),
      @ApiResponse(code = 404, message = "Not found"),
      @ApiResponse(code = 406, message = "Accept Header not supported")})
  @ApiOperation(value = "Find 'live' Elasticsearch person documents by client id's",
      response = LiveElasticClientResponse.class)
  public javax.ws.rs.core.Response get(@QueryParam("clientIds") @ApiParam(required = true,
      name = "clientIds", value = "The clients' id's") final List<String> clientIds) {
    gov.ca.cwds.rest.api.Response clients = null;
    // resourceDelegate.findInvolvementHistoryByClientIds(clientIds);
    return new ResponseConverter().withDataResponse(clients);
  }

}

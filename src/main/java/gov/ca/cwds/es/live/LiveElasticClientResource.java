package gov.ca.cwds.es.live;

import static gov.ca.cwds.rest.core.Api.RESOURCE_CLIENT;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import com.google.inject.Inject;

import gov.ca.cwds.inject.LiveElasticClientServiceResource;
import gov.ca.cwds.rest.resources.TypedResourceDelegate;
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
   * <td>String[]</td>
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
  private TypedResourceDelegate<String[], LiveElasticClientRequest> resourceDelegate;

  /**
   * Preferred constructor.
   *
   * @param resourceDelegate LiveElasticClientService
   */
  @Inject
  public LiveElasticClientResource(
      @LiveElasticClientServiceResource TypedResourceDelegate<String[], LiveElasticClientRequest> resourceDelegate) {
    this.resourceDelegate = resourceDelegate;
  }

  /**
   * Finds live Elasticsearch-like search results from a list of client ids.
   *
   * @param clientIds client id list
   * @return the response
   */
  @GET
  @Path("/any")
  @ApiResponses(value = {@ApiResponse(code = 401, message = "Not Authorized"),
      @ApiResponse(code = 404, message = "Not found"),
      @ApiResponse(code = 406, message = "Accept Header not supported")})
  @ApiOperation(value = "Find live Elasticsearch results by client ids",
      response = LiveElasticClientResponse.class)
  public javax.ws.rs.core.Response getClients(@QueryParam("clientIds") @ApiParam(required = true,
      name = "clientIds", value = "The id's of the clients") final List<String> clientIds) {
    return resourceDelegate.get(clientIds.toArray(new String[0]));
  }

  /**
   * Finds live Elasticsearch-like search results for a single client id.
   *
   * @param id client id to pull
   * @return the response
   */
  @GET
  @Path("/{id}")
  @ApiResponses(value = {@ApiResponse(code = 401, message = "Not Authorized"),
      @ApiResponse(code = 404, message = "Not found"),
      @ApiResponse(code = 406, message = "Accept Header not supported")})
  @ApiOperation(value = "Find live client data by client id",
      response = LiveElasticClientResponse.class)
  public javax.ws.rs.core.Response get(@PathParam("id") @ApiParam(required = true, name = "id",
      value = "The id of the client to find relationships for") String id) {
    final String[] ids = {id};
    return resourceDelegate.get(ids);
  }

}

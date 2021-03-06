package gov.ca.cwds.rest.resources;

import static gov.ca.cwds.rest.core.Api.DATASOURCE_CMS;
import static gov.ca.cwds.rest.core.Api.RESOURCE_REFERRALS;

import javax.validation.Valid;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.http.HttpStatus;

import com.google.inject.Inject;

import gov.ca.cwds.data.persistence.xa.XAUnitOfWork;
import gov.ca.cwds.inject.ScreeningToReferralServiceBackedResource;
import gov.ca.cwds.rest.api.Request;
import gov.ca.cwds.rest.api.domain.Id;
import gov.ca.cwds.rest.api.domain.PostedScreeningToReferral;
import gov.ca.cwds.rest.api.domain.Screening;
import gov.ca.cwds.rest.api.domain.ScreeningToReferral;
import io.dropwizard.hibernate.UnitOfWork;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

/**
 * A resource providing a RESTful interface for {@link ScreeningToReferral}. It delegates functions
 * to {@link ServiceBackedResourceDelegate}. It decorates the {@link ServiceBackedResourceDelegate}
 * not in functionality but with @see
 * <a href= "https://github.com/swagger-api/swagger-core/wiki/Annotations-1.5.X">Swagger
 * Annotations</a> and
 * <a href="https://jersey.java.net/documentation/latest/user-guide.html#jaxrs-resources">Jersey
 * Annotations</a>
 * 
 * @author CWDS API Team
 */
@Api(value = RESOURCE_REFERRALS, tags = {RESOURCE_REFERRALS})
@Path(value = RESOURCE_REFERRALS)
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public class ScreeningToReferralResource {

  private ResourceDelegate resourceDelegate;

  /**
   * Constructor
   * 
   * @param resourceDelegate The resource delegate to delegate to.
   */
  @Inject
  public ScreeningToReferralResource(
      @ScreeningToReferralServiceBackedResource ResourceDelegate resourceDelegate) {
    this.resourceDelegate = resourceDelegate;
  }

  /**
   * Finds a CMS referral by id.
   * 
   * @param id the id
   * 
   * @return the response
   */
  @UnitOfWork(DATASOURCE_CMS)
  @GET
  @Path("/{id}")
  @ApiResponses(value = {@ApiResponse(code = 401, message = "Not Authorized"),
      @ApiResponse(code = 404, message = "Not found"),
      @ApiResponse(code = 406, message = "Accept Header not supported")})
  @ApiOperation(value = "Find referral by id", response = PostedScreeningToReferral.class,
      code = 200)
  public Response get(@PathParam("id") @ApiParam(required = true, name = "id",
      value = "The id of the Referral to find") String id) {
    return resourceDelegate.get(id);
  }

  /**
   * Create a {@link ScreeningToReferral}.
   * 
   * @param screeningToReferral The {@link ScreeningToReferral}
   * 
   * @return The {@link Response}
   */
  @XAUnitOfWork
  @POST
  @ApiResponses(value = {@ApiResponse(code = 400, message = "Unable to process JSON"),
      @ApiResponse(code = 406, message = "Accept Header not supported"),
      @ApiResponse(code = 409, message = "Conflict - already exists"),
      @ApiResponse(code = 422, message = "Unable to validate ScreeningToReferral")})
  @Consumes(value = MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Create Referral from Screening Object", code = HttpStatus.SC_CREATED,
      response = PostedScreeningToReferral.class)
  public Response create(
      @Valid @ApiParam(hidden = false, required = true) ScreeningToReferral screeningToReferral) {
    return resourceDelegate.create(screeningToReferral);
  }

  /**
   * Promotes a Screening To A Referral. Creates a Referral From the Screening and Updates the
   * Screening with the Referral Legacy Id.
   *
   * @param id - the id of the screening
   * @return The {@link Response}
   */
  @XAUnitOfWork
  @POST
  @Path("screenings/{screeningId}")
  @ApiResponses(value = {@ApiResponse(code = 400, message = "Unable to process"),
      @ApiResponse(code = 401, message = "Not Authorized"),
      @ApiResponse(code = 406, message = "Accept Header not supported"),
      @ApiResponse(code = 409, message = "Conflict - already exists"),
      @ApiResponse(code = 422, message = "Unable to validate")})
  @Consumes(value = MediaType.APPLICATION_JSON)
  @ApiOperation(value = "Create a Referral from a Screening Id", code = HttpStatus.SC_CREATED,
      response = Screening.class)
  public Response create(@PathParam("screeningId") @ApiParam(required = true, name = "screeningId",
      value = "Screening id") String id) {
    Request screeningId = new Id(id);
    return resourceDelegate.create(screeningId);
  }


}

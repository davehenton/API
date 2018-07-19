package gov.ca.cwds.api.builder;

import static io.restassured.RestAssured.given;

import io.restassured.http.ContentType;
import io.restassured.response.Response;

/**
 * Functional Test Builder to used handle request for test, make it more cleaner and easier to build
 * the test classes.
 * 
 * @author CWDS API Team
 *
 */
public class FunctionalTestingBuilder {

  private static final String TOKEN = "token";

  /**
   * This method process the POST processing and return the appropriate body or Json response.
   * 
   * @param object - object
   * @param resourcePath - resourcePath
   * @param token - token
   * @return the response
   */
  public Response processPostRequest(Object object, String resourcePath, String token) {
    return given().queryParam(TOKEN, token).contentType(ContentType.JSON).accept(ContentType.JSON)
        .body(object).when().post(resourcePath).then().contentType(ContentType.JSON).extract()
        .response();
  }

  /**
   * This methods process the GET processing.
   * 
   * @param resourcePath - resourcePath
   * @param parameter - parameter
   * @param ParameterValue - ParameterValue
   * @param token - token
   * @return the response
   */
  public Response processGetRequest(String resourcePath, String parameter, String ParameterValue,
      String token) {
    return given().queryParam(parameter, ParameterValue).queryParam(TOKEN, token)
        .get(resourcePath).then().contentType(ContentType.JSON).extract().response();
  }

  /**
   * Method to process the POST with path parameters
   * 
   * @param object - object
   * @param resourcePath - resourcePath
   * @param pathParam - pathParam
   * @param pathParamValue - pathParamValue
   * @param token - token
   * @return the post response
   */
  public Response processPostRequestWithPathParameter(Object object, String resourcePath,
      String pathParam, String pathParamValue, String token) {
    return given().queryParam(TOKEN, token).contentType(ContentType.JSON)
        .pathParam(pathParam, pathParamValue).accept(ContentType.JSON).body(object).when()
        .post(resourcePath).then().contentType(ContentType.JSON).extract().response();
  }

}

package gov.ca.cwds.rest.resources;

import com.fasterxml.jackson.core.type.TypeReference;
import gov.ca.cwds.IntakeBaseTest;
import gov.ca.cwds.rest.api.domain.ScreeningRelationship;
import java.io.InputStream;
import java.util.List;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import org.json.JSONException;
import org.junit.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.io.IOException;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class ScreeningResourceIRT extends IntakeBaseTest {

  public static final String SCREENING_PATH = "screenings";
  public static final String RELATIONSHIPS = "relationships";
  public static final String RELATIONSHIP_PATH = "screening_relationships";

  public static final String FIXTURE_GET_LIST_RELATIONSHIPS_RESPONSE_ONLY_POSTGRES_PATH = "fixtures/gov/ca/cwds/rest/resources/relationships/relationships-by-screening-id-only-postgres.json";
  public static final String SCREENING_ID_1 = "1101";

  public static final String FIXTURE_GET_ONE_RELATIONSHIP_RESPONSE_ONLY_POSTGRES_PATH = "fixtures/gov/ca/cwds/rest/resources/relationships/relationship-by-screening-id-only-postgres.json";
  public static final String SCREENING_ID_2 = "1102";

  public static final String SCREENING_ID_3 = "1103";
  public static final String PARTICIPANT_ID_3_1 = "1006";
  public static final String PARTICIPANT_ID_3_2 = "1007";
  public static final String PARTICIPANT_ID_3_3 = "1008";
  public static final String PARTICIPANT_ID_3_4 = "1009";
  public static final String RELATIONSHIP_LEGACY_ID_3 = "0btlf7s000";

  public static final String SCREENING_ID_4 = "1104";
  public static final String PARTICIPANT_ID_4_1 = "1010";
  public static final String PARTICIPANT_ID_4_2 = "1011";
  public static final String PARTICIPANT_ID_4_3 = "1012";
  public static final String RELATIONSHIP_LEGACY_ID_4_1 = "0btlf7s001";
  public static final String RELATIONSHIP_LEGACY_ID_4_2 = "0btlf7s002";


  public static final String FIXTURE_GET_ONE_RELATIONSHIP_RESPONSE_AFTER_UPDATE = "fixtures/gov/ca/cwds/rest/resources/relationships/relationship-by-screening-id-after-update.json";
  public static final String RELATIONSHIP_ID_5 = "7";
  public static final String SCREENING_ID_5 = "1105";
  public static final int RELATIONSHIP_TYPE_5 = 192;
  public static final int RELATIONSHIP_TYPE_AFTER_UPDATE_5 = 182;
  public static final String PARTICIPANT_ID_5_1 = "1013";
  public static final String PARTICIPANT_ID_5_2 = "1014";
  public static final String RELATIONSHIP_LEGACY_ID_5_1 = "0btlf7s003";

  public static final String FIXTURE_GET_ONE_RELATIONSHIP_RESPONSE_SAME_DATES = "fixtures/gov/ca/cwds/rest/resources/relationships/relationship-by-screening-id-same-dates.json";
  public static final String SCREENING_ID_6 = "1106";

  public static final String FIXTURE_GET_RELATIONSHIPS_RESPONSE_UPDATED_ONE = "fixtures/gov/ca/cwds/rest/resources/relationships/relationships-by-screening-id-cms-date-less.json";
  public static final String SCREENING_ID_7 = "1107";

  public static final String BAD_SCREENING_ID = "3393";

  @Test
  public void getRelationshipsByScreeningId_twoRelationsExist() throws IOException, JSONException {
    String actualJson = getStringResponse(
        doGetCall(SCREENING_PATH + "/" + SCREENING_ID_1 + "/" + RELATIONSHIPS));
    String expectedResponse =
        fixture(FIXTURE_GET_LIST_RELATIONSHIPS_RESPONSE_ONLY_POSTGRES_PATH);
    JSONAssert.assertEquals(expectedResponse, actualJson, JSONCompareMode.NON_EXTENSIBLE);
  }

  @Test
  public void getRelationshipsByScreeningId_oneRelationExist() throws IOException, JSONException {
    String actualJson = getStringResponse(
        doGetCall(SCREENING_PATH + "/" + SCREENING_ID_2 + "/" + RELATIONSHIPS));
    String expectedResponse =
        fixture(FIXTURE_GET_ONE_RELATIONSHIP_RESPONSE_ONLY_POSTGRES_PATH);
    JSONAssert.assertEquals(expectedResponse, actualJson, JSONCompareMode.NON_EXTENSIBLE);
  }

  @Test
  public void getRelationshipsByScreeningId_oneRelationInPostgress_oneRelationInDB2()
      throws IOException, JSONException {
    String actualJson = getStringResponse(
        doGetCall(SCREENING_PATH + "/" + SCREENING_ID_3 + "/" + RELATIONSHIPS));
    List<ScreeningRelationship> relationships = objectMapper
        .readValue(actualJson.getBytes(), new TypeReference<List<ScreeningRelationship>>() {
        });

    assertNotNull(relationships);
    assertEquals(2, relationships.size());

    ScreeningRelationship relationship1 = relationships.get(0);
    ScreeningRelationship relationship2 = relationships.get(1);

    assertEquals(PARTICIPANT_ID_3_1, relationship1.getClientId());
    assertEquals(PARTICIPANT_ID_3_2, relationship1.getRelativeId());
    assertEquals(PARTICIPANT_ID_3_3, relationship2.getClientId());
    assertEquals(PARTICIPANT_ID_3_4, relationship2.getRelativeId());
    assertEquals(RELATIONSHIP_LEGACY_ID_3, relationship1.getLegacyId());
    assertNull(relationship2.getLegacyId());
  }

  @Test
  public void getRelationshipsByScreeningId_twoRelationsInDB2() throws IOException {
    String actualJson = getStringResponse(
        doGetCall(SCREENING_PATH + "/" + SCREENING_ID_4 + "/" + RELATIONSHIPS));
    List<ScreeningRelationship> relationships = objectMapper
        .readValue(actualJson.getBytes(), new TypeReference<List<ScreeningRelationship>>() {
        });

    assertNotNull(relationships);
    assertEquals(2, relationships.size());

    ScreeningRelationship relationship1 = relationships.get(0);
    ScreeningRelationship relationship2 = relationships.get(1);

    assertEquals(PARTICIPANT_ID_4_1, relationship1.getClientId());
    assertEquals(PARTICIPANT_ID_4_2, relationship1.getRelativeId());
    assertEquals(PARTICIPANT_ID_4_1, relationship2.getClientId());
    assertEquals(PARTICIPANT_ID_4_3, relationship2.getRelativeId());
    assertEquals(RELATIONSHIP_LEGACY_ID_4_1, relationship1.getLegacyId());
    assertEquals(RELATIONSHIP_LEGACY_ID_4_2, relationship2.getLegacyId());
  }

  @Test
  public void getRelationshipsByScreeningId_differentDates() throws IOException, JSONException {
    Response response = doGetCall(RELATIONSHIP_PATH + "/" + RELATIONSHIP_ID_5);
    ScreeningRelationship relationshipBeforeUpdate = objectMapper
        .readValue((InputStream) response.getEntity(), ScreeningRelationship.class);

    assertNotNull(relationshipBeforeUpdate);
    assertEquals(RELATIONSHIP_ID_5, relationshipBeforeUpdate.getId());
    assertEquals(PARTICIPANT_ID_5_1, relationshipBeforeUpdate.getClientId());
    assertEquals(PARTICIPANT_ID_5_2, relationshipBeforeUpdate.getRelativeId());
    assertEquals(RELATIONSHIP_LEGACY_ID_5_1, relationshipBeforeUpdate.getLegacyId());

    int relationshipType = relationshipBeforeUpdate.getRelationshipType();
    assertEquals(RELATIONSHIP_TYPE_5, relationshipType);

    String actualJson = getStringResponse(
        doGetCall(SCREENING_PATH + "/" + SCREENING_ID_5 + "/" + RELATIONSHIPS));
    String expectedResponse =
        fixture(FIXTURE_GET_ONE_RELATIONSHIP_RESPONSE_AFTER_UPDATE);
    JSONAssert.assertEquals(expectedResponse, actualJson, JSONCompareMode.NON_EXTENSIBLE);

    response = doGetCall(RELATIONSHIP_PATH + "/" + RELATIONSHIP_ID_5);
    ScreeningRelationship relationshipAfterUpdate = objectMapper
        .readValue((InputStream) response.getEntity(), ScreeningRelationship.class);

    assertNotNull(relationshipAfterUpdate);
    assertEquals(RELATIONSHIP_ID_5, relationshipAfterUpdate.getId());
    assertEquals(PARTICIPANT_ID_5_1, relationshipAfterUpdate.getClientId());
    assertEquals(PARTICIPANT_ID_5_2, relationshipAfterUpdate.getRelativeId());
    assertEquals(RELATIONSHIP_LEGACY_ID_5_1, relationshipAfterUpdate.getLegacyId());

    int relationshipTypeAfterUpdate = relationshipAfterUpdate.getRelationshipType();
    assertEquals(RELATIONSHIP_TYPE_AFTER_UPDATE_5, relationshipTypeAfterUpdate);
  }

  @Test
  public void getRelationshipsByScreeningId_sameDates() throws IOException, JSONException {
    String actualJson = getStringResponse(
        doGetCall(SCREENING_PATH + "/" + SCREENING_ID_6 + "/" + RELATIONSHIPS));
    String expectedResponse =
        fixture(FIXTURE_GET_ONE_RELATIONSHIP_RESPONSE_SAME_DATES);
    JSONAssert.assertEquals(expectedResponse, actualJson, JSONCompareMode.NON_EXTENSIBLE);
  }

  @Test
  public void getRelationshipsByScreeningId_cmsDateLess() throws IOException, JSONException {
    String actualJson = getStringResponse(
        doGetCall(SCREENING_PATH + "/" + SCREENING_ID_7 + "/" + RELATIONSHIPS));
    String expectedResponse =
        fixture(FIXTURE_GET_RELATIONSHIPS_RESPONSE_UPDATED_ONE);
    JSONAssert.assertEquals(expectedResponse, actualJson, JSONCompareMode.NON_EXTENSIBLE);
  }

  @Test
  public void getRelationshipsByScreeningId_badRequest() throws IOException, JSONException {
    String actualJson = getStringResponse(
        doGetCall(SCREENING_PATH + "/" + BAD_SCREENING_ID + "/" + RELATIONSHIPS));
    JSONAssert.assertEquals("[]", actualJson, JSONCompareMode.NON_EXTENSIBLE);
  }
}

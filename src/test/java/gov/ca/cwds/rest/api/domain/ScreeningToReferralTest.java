package gov.ca.cwds.rest.api.domain;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import gov.ca.cwds.rest.core.Api;
import gov.ca.cwds.rest.resources.ScreeningToReferralResource;
import io.dropwizard.jackson.Jackson;

@SuppressWarnings("javadoc")
public class ScreeningToReferralTest {

  private static final String ROOT_RESOURCE = "/" + Api.RESOURCE_REFERRALS + "/";;
  private static final ObjectMapper MAPPER = Jackson.newObjectMapper();
  private Validator validator;


  private static final ScreeningToReferralResource mockedScreeningToReferralResource =
      mock(ScreeningToReferralResource.class);

  private String agencyType = "Law enforcement";
  private String agencyName = "Sacramento County Sheriff Deparment";
  private String method = "electronic report";
  private String informDate = "2017-03-15";
  private Set<Participant> participants = new HashSet<Participant>();
  private Set<CrossReport> crossReports = new HashSet<CrossReport>();
  private Set<Allegation> allegations = new HashSet<Allegation>();
  private long id = 2;

  @Before
  public void setup() {
    MAPPER.configure(SerializationFeature.INDENT_OUTPUT, true);
    ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
    validator = factory.getValidator();
  }

  /*
   * Serialization and deserialization
   */
  @Test
  public void serializesToJSON() throws Exception {

    Address address = validAddress();
    Participant participant = validParticipant();
    participants.add(participant);
    CrossReport crossReport = new CrossReport("", "", agencyType, agencyName, method, informDate);
    crossReports.add(crossReport);
    Allegation allegation = validAllegation();
    allegations.add(allegation);


    String expected = MAPPER.writeValueAsString(
        new ScreeningToReferral(id, "", "", "2016-08-03T01:00:00.000Z", "sacramento", "2016-08-02",
            "Foster Home", "Phone", "The Rocky Horror Show", "Narrative 123 test", "123ABC",
            "immediate", "2016-08-03T01:00:00.000Z", "Michael Bastow", "addtional information",
            "Response time", "Detail", address, participants, crossReports, allegations));

    // System.out.println(expected);
    String serialized = MAPPER.writeValueAsString(
        MAPPER.readValue(fixture("fixtures/domain/ScreeningToReferral/valid/validstr.json"),
            ScreeningToReferral.class));
    assertThat(serialized, is(expected));
  }

  @Test
  public void deserializesFromJSON() throws Exception {
    MAPPER.configure(SerializationFeature.INDENT_OUTPUT, true);
    Address address = validAddress();
    Participant participant = validParticipant();
    participants.add(participant);
    CrossReport crossReport = new CrossReport("", "", agencyType, agencyName, method, informDate);
    crossReports.add(crossReport);
    Allegation allegation = validAllegation();
    allegations.add(allegation);

    ScreeningToReferral expected =
        new ScreeningToReferral(id, "", "", "2016-08-03T01:00:00.000Z", "sacramento", "2016-08-02",
            "Foster Home", "Phone", "The Rocky Horror Show", "Narrative 123 test", "123ABC",
            "immediate", "2016-08-03T01:00:00.000Z", "Michael Bastow", "addtional information",
            "Response time", "Detail", address, participants, crossReports, allegations);


    ScreeningToReferral serialized =
        MAPPER.readValue(fixture("fixtures/domain/ScreeningToReferral/valid/validstr.json"),
            ScreeningToReferral.class);

    assertThat(serialized, is(expected));

  }

  @Test
  public void testWithValidSuccess() throws Exception {
    ScreeningToReferral toValidate = MAPPER.readValue(
        fixture("fixtures/domain/ScreeningToReferral/valid/validDomainScreeningToReferral.json"),
        ScreeningToReferral.class);
    Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
        validator.validate(toValidate);
    assertEquals(0, constraintViolations.size());
  }

  @Test
  public void testWithNullParticipantsFail() throws Exception {

    ScreeningToReferral toValidate = MAPPER.readValue(
        fixture("fixtures/domain/ScreeningToReferral/invalid/nullParticipants.json"),
        ScreeningToReferral.class);
    Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
        validator.validate(toValidate);
    assertEquals(2, constraintViolations.size());
    String[] expectedMessages = { "must contain a Victim, Perpetrator, and Reporter", "may not be empty"};
    Iterator itr = constraintViolations.iterator();
    String[] actualMessages = {((ConstraintViolation)itr.next()).getMessage(),((ConstraintViolation)itr.next()).getMessage()};
    assertArrayEquals(expectedMessages,actualMessages);
  }

  @Test
  public void testWithEmptyParticipantsFail() throws Exception {
    ScreeningToReferral toValidate = MAPPER.readValue(
        fixture("fixtures/domain/ScreeningToReferral/invalid/emptyParticipants.json"),
        ScreeningToReferral.class);
    Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
        validator.validate(toValidate);
    assertEquals(2, constraintViolations.size());
    String[] expectedMessages = { "must contain a Victim, Perpetrator, and Reporter", "may not be empty"};
    Iterator itr = constraintViolations.iterator();
    String[] actualMessages = {((ConstraintViolation)itr.next()).getMessage(),((ConstraintViolation)itr.next()).getMessage()};
    assertArrayEquals(expectedMessages,actualMessages);
  }

  @Test
  public void testWithNullAllegationsFail() throws Exception {
    ScreeningToReferral toValidate = MAPPER.readValue(
        fixture("fixtures/domain/ScreeningToReferral/invalid/nullAllegations.json"),
        ScreeningToReferral.class);
    Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
        validator.validate(toValidate);
    assertEquals(1, constraintViolations.size());
    assertEquals("may not be empty", constraintViolations.iterator().next().getMessage());
  }

  @Test
  public void testWithEmptyAllegationsFail() throws Exception {
    ScreeningToReferral toValidate = MAPPER.readValue(
        fixture("fixtures/domain/ScreeningToReferral/invalid/emptyAllegations.json"),
        ScreeningToReferral.class);
    Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
        validator.validate(toValidate);
    assertEquals(1, constraintViolations.size());
    assertEquals("may not be empty", constraintViolations.iterator().next().getMessage());
  }

  @Test
  public void testWithEmptyCrossReportFails() throws Exception {
    ScreeningToReferral toValidate =
        MAPPER.readValue(fixture("fixtures/domain/ScreeningToReferral/valid/emptyCrossReport.json"),
            ScreeningToReferral.class);
    Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
        validator.validate(toValidate);
    assertEquals(1, constraintViolations.size());
  }

  @Test
  public void testWithNullCrossReportFails() throws Exception {
    ScreeningToReferral toValidate =
        MAPPER.readValue(fixture("fixtures/domain/ScreeningToReferral/valid/nullCrossReport.json"),
            ScreeningToReferral.class);
    Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
        validator.validate(toValidate);
    assertEquals(1, constraintViolations.size());
  }

  @Test
  public void testWithMultipleCrossReportsSuccess() throws Exception {
    ScreeningToReferral toValidate = MAPPER.readValue(
        fixture("fixtures/domain/ScreeningToReferral/valid/validMultipleCrossReports.json"),
        ScreeningToReferral.class);
    Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
        validator.validate(toValidate);
    assertEquals(0, constraintViolations.size());

  }

  @Test
  public void testWithAgencyTypeMissingFail() throws Exception {
    ScreeningToReferral toValidate = MAPPER.readValue(
        fixture("fixtures/domain/ScreeningToReferral/invalid/missingAgencyType.json"),
        ScreeningToReferral.class);
    Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
        validator.validate(toValidate);
    assertEquals(1, constraintViolations.size());
    assertEquals("may not be empty", constraintViolations.iterator().next().getMessage());
  }

  @Test
  public void testWithInvalidIncidentDateFormatFail() throws Exception {
    ScreeningToReferral toValidate = MAPPER.readValue(
        fixture("fixtures/domain/ScreeningToReferral/invalid/invalidIncidentDateFormat.json"),
        ScreeningToReferral.class);
    Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
        validator.validate(toValidate);
    assertEquals(1, constraintViolations.size());
    assertEquals("must be in the format of yyyy-MM-dd",
        constraintViolations.iterator().next().getMessage());
  }

  @Test
  public void testBlankLegacySourceTableSuccess() throws Exception {
    ScreeningToReferral toValidate = MAPPER.readValue(
        fixture("fixtures/domain/screeningToReferral/valid/blankLegacySourceTable.json"),
        ScreeningToReferral.class);

    Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
        validator.validate(toValidate);
    assertEquals(0, constraintViolations.size());
  }

  @Test
  public void testNullLegacySourceTableSuccess() throws Exception {
    ScreeningToReferral toValidate = MAPPER.readValue(
        fixture("fixtures/domain/ScreeningToReferral/valid/nullLegacySourceTable.json"),
        ScreeningToReferral.class);

    Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
        validator.validate(toValidate);
    assertEquals(0, constraintViolations.size());
  }

  @Test
  public void testMissingLegacySourceTableSuccess() throws Exception {
    ScreeningToReferral toValidate = MAPPER.readValue(
        fixture("fixtures/domain/ScreeningToReferral/valid/missingLegacySourceTable.json"),
        ScreeningToReferral.class);
    Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
        validator.validate(toValidate);
    assertEquals(0, constraintViolations.size());
  }

  @Test
  public void testWithEmptyLegacyReferralIdSuccess() throws Exception {
    ScreeningToReferral toValidate =
        MAPPER.readValue(fixture("fixtures/domain/ScreeningToReferral/valid/emptyReferralId.json"),
            ScreeningToReferral.class);
    Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
        validator.validate(toValidate);
    assertEquals(0, constraintViolations.size());
  }

  @Test
  public void testWithNullLegacyReferralIdSuccess() throws Exception {
    ScreeningToReferral toValidate =
        MAPPER.readValue(fixture("fixtures/domain/ScreeningToReferral/valid/nullReferralId.json"),
            ScreeningToReferral.class);
    Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
        validator.validate(toValidate);
    assertEquals(0, constraintViolations.size());
  }

  @Test
  public void testWithMissingLegacyReferralIdSuccess() throws Exception {
    ScreeningToReferral toValidate = MAPPER.readValue(
        fixture("fixtures/domain/ScreeningToReferral/valid/missingReferralId.json"),
        ScreeningToReferral.class);
    Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
        validator.validate(toValidate);
    assertEquals(0, constraintViolations.size());
  }

  @Test
  public void testLegacyIdTooLongFail() throws Exception {
    ScreeningToReferral toValidate = MAPPER.readValue(
        fixture("fixtures/domain/screeningToReferral/invalid/legacyIdTooLong.json"),
        ScreeningToReferral.class);
    Set<ConstraintViolation<ScreeningToReferral>> constraintViolations =
        validator.validate(toValidate);
    assertEquals(1, constraintViolations.size());
    assertEquals("size must be between 0 and 10",
        constraintViolations.iterator().next().getMessage());
  }

  private ScreeningToReferral validScreeningToReferral() {
    ScreeningToReferral str = null;
    try {
      str = MAPPER.readValue(
          fixture("fixtures/domain/ScreeningToReferral/valid/validDomainScreeningToReferral.json"),
          ScreeningToReferral.class);
    } catch (JsonParseException e) {
      e.printStackTrace();
      return null;
    } catch (JsonMappingException e) {
      e.printStackTrace();
      return null;
    } catch (IOException e) {
      e.printStackTrace();
    }
    return str;
  }

  private Address validAddress() {

    try {
      Address validAddress =
          MAPPER.readValue(fixture("fixtures/domain/address/valid/valid.json"), Address.class);

      return validAddress;

    } catch (JsonParseException e) {
      e.printStackTrace();
      return null;
    } catch (JsonMappingException e) {
      e.printStackTrace();
      return null;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  private Participant validParticipant() {

    try {
      Participant validParticipant = MAPPER
          .readValue(fixture("fixtures/domain/participant/valid/valid.json"), Participant.class);
      return validParticipant;

    } catch (JsonParseException e) {
      e.printStackTrace();
      return null;
    } catch (JsonMappingException e) {
      e.printStackTrace();
      return null;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }

  private Allegation validAllegation() {

    try {
      Allegation validAllegation = MAPPER
          .readValue(fixture("fixtures/domain/Allegation/valid/valid.json"), Allegation.class);
      return validAllegation;

    } catch (JsonParseException e) {
      e.printStackTrace();
      return null;
    } catch (JsonMappingException e) {
      e.printStackTrace();
      return null;
    } catch (IOException e) {
      e.printStackTrace();
      return null;
    }
  }
}

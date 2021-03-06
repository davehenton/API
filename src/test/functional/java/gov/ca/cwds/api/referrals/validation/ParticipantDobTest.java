package gov.ca.cwds.api.referrals.validation;

import static org.hamcrest.Matchers.equalTo;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import gov.ca.cwds.api.FunctionalTest;
import gov.ca.cwds.api.builder.HttpRequestHandler;
import gov.ca.cwds.api.builder.ResourceEndPoint;
import gov.ca.cwds.fixture.ParticipantResourceBuilder;
import gov.ca.cwds.fixture.ScreeningToReferralResourceBuilder;
import gov.ca.cwds.rest.api.domain.Participant;
import gov.ca.cwds.rest.api.domain.ScreeningToReferral;

/**
 * @author CWDS API Team
 *
 */
public class ParticipantDobTest extends FunctionalTest {

  String referralPath;
  private HttpRequestHandler httpRequestHandler;

  /**
   * 
   */
  @Before
  public void setup() {
    referralPath = getResourceUrlFor(ResourceEndPoint.REFERRALS.getResourcePath());
    httpRequestHandler = new HttpRequestHandler();
  }

  /**
   * 
   */
  @Test
  public void shouldReturn422WhenDateOfBirthInFuture() {
    LocalDate today = LocalDate.now();
    LocalDate tomorrow = today.plus(1, ChronoUnit.DAYS);
    String tomrrowString = tomorrow.toString();
    String approximateAge = null;
    String approximateAgeUnits = null;
    ScreeningToReferral referral =
        buildScreeningToReferral(tomrrowString, approximateAge, approximateAgeUnits);
    httpRequestHandler.postRequest(referral, referralPath, token).then()
        .body("issue_details.user_message[0]", equalTo("Date of Birth cannot be in the future"))
        .and().statusCode(422);
  }

  protected ScreeningToReferral buildScreeningToReferral(String dateOfBith, String approximateAge,
      String approximateAgeUnits) {
    Participant victim = new ParticipantResourceBuilder().setDateOfBirth(dateOfBith)
        .setApproximateAge(approximateAge).setApproximateAgeUnits(approximateAgeUnits)
        .setLegacyDescriptor(null).createVictimParticipant();
    Participant Perp = new ParticipantResourceBuilder().setGender("M").setLegacyDescriptor(null)
        .createPerpParticipant();
    Participant reporter = new ParticipantResourceBuilder().setGender("M").setLegacyDescriptor(null)
        .createReporterParticipant();
    Set<Participant> participants = new HashSet<>(Arrays.asList(victim, Perp, reporter));
    ScreeningToReferral referral = new ScreeningToReferralResourceBuilder()
        .setassigneeStaffId(userInfo.getStaffId()).setIncidentCounty(userInfo.getIncidentCounty())
        .setParticipants(participants).createScreeningToReferral();
    return referral;
  }

}

package gov.ca.cwds.rest.business.rules;

import org.joda.time.DateTime;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import gov.ca.cwds.rest.api.domain.Participant;
import gov.ca.cwds.rest.api.domain.ScreeningToReferral;
import gov.ca.cwds.rest.business.RuleValidatator;

/**
 * 
 * <p>
 * BUSINESS RULE: "R - 00824"
 * 
 * IF referralResponseTypeCode is set to Evaluate Out, Approval Status is set to Approved and Client
 * age is below 19
 * 
 * THEN referralClient - dispositionCode is set to the "A"
 * <p>
 * 
 * @author CWDS API Team
 *
 */
public class R00824SetDispositionCode implements RuleValidatator {

  private static final int APPROVED = 122;
  private static final int ADULT = 19;
  private static final short EVALUATE_OUT = 1519;

  private ScreeningToReferral screeningToReferral;
  private Participant incomingParticipant;

  /**
   * @param screeningToReferral - screeningToReferral
   * @param incomingParticipant - incomingParticipant
   */
  public R00824SetDispositionCode(ScreeningToReferral screeningToReferral,
      Participant incomingParticipant) {
    super();
    this.screeningToReferral = screeningToReferral;
    this.incomingParticipant = incomingParticipant;
  }

  @Override
  public boolean isValid() {
    return clientAge() < ADULT && screeningToReferral.getResponseTime() == EVALUATE_OUT
        && screeningToReferral.getApprovalStatus() == APPROVED;
  }

  private int clientAge() {
    String date = screeningToReferral.getStartedAt().split("T")[0];
    String dob = incomingParticipant.getDateOfBirth();
    DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
    DateTime receivedDate = formatter.parseDateTime(date);
    DateTime clientDob = formatter.parseDateTime(dob);
    return Years.yearsBetween(clientDob, receivedDate).getYears();
  }

}
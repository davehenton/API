package gov.ca.cwds.rest.business.rules;

import static gov.ca.cwds.data.legacy.cms.entity.enums.ReferralResponseType.EVALUATE_OUT;

import org.apache.commons.lang3.StringUtils;
import org.joda.time.DateTime;
import org.joda.time.Years;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import gov.ca.cwds.rest.api.domain.DomainChef;
import gov.ca.cwds.rest.api.domain.Participant;
import gov.ca.cwds.rest.api.domain.ScreeningToReferral;
import gov.ca.cwds.rest.business.RuleValidator;
import gov.ca.cwds.rest.filters.RequestExecutionContext;

/**
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
 */
public class R00824SetDispositionCode implements RuleValidator {

  private static final int APPROVED = 122;
  private static final int ADULT = 19;

  private ScreeningToReferral screeningToReferral;
  private Participant incomingParticipant;

  /**
   * <blockquote>
   *
   * <pre>
   * BUSINESS RULE: "R - 00824"
   *
   * IF    referralResponseTypeCode is set to Evaluate Out 
   * THEN  referralClient - dispositionCode is set to the "A"
   *
   * </pre>
   *
   * </blockquote>
   * 
   * @throws Exception - Exception
   */

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
    if (screeningToReferral != null && screeningToReferral.getResponseTime() != null
        && StringUtils.isNotBlank(incomingParticipant.getDateOfBirth())) {
      return clientAge() < ADULT
          && screeningToReferral.getResponseTime().equals(EVALUATE_OUT.getCode())
          && screeningToReferral.getApprovalStatus() == APPROVED;
    }
    return false;
  }

  private int clientAge() {
    String date =
        DomainChef.cookISO8601Timestamp(RequestExecutionContext.instance().getRequestStartTime())
            .split("T")[0];
    String dob = incomingParticipant.getDateOfBirth();
    DateTimeFormatter formatter = DateTimeFormat.forPattern("yyyy-MM-dd");
    DateTime receivedDate = formatter.parseDateTime(date);
    DateTime clientDob = formatter.parseDateTime(dob);
    return Years.yearsBetween(clientDob, receivedDate).getYears();
  }

}

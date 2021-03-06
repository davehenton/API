package gov.ca.cwds.rest.business.rules;

import static org.junit.Assert.assertEquals;

import gov.ca.cwds.fixture.ClientResourceBuilder;
import gov.ca.cwds.fixture.ReferralClientResourceBuilder;
import gov.ca.cwds.rest.api.domain.cms.Client;
import gov.ca.cwds.rest.api.domain.cms.ReferralClient;
import org.junit.Test;

/**
 * CWDS API Team
 */
public class R04880EstimatedDOBCodeSettingTest {

  private final String dateStarted = "2018-01-01";

  @Test
  public void shouldSetEstimatedDOBCodeNoWhenBirthDateSet() {
    Client client = new ClientResourceBuilder().setBirthDate("2010-01-01").build();
    ReferralClient referralClient = new ReferralClientResourceBuilder().setAgeNumber(null)
        .setAgePeriodCode("").buildReferralClient();

    R04880EstimatedDOBCodeSetting rule = new R04880EstimatedDOBCodeSetting(client, referralClient,
        dateStarted);
    rule.execute();
    assertEquals(gov.ca.cwds.rest.api.domain.cms.Client.ESTIMATED_DOB_CODE_NO,
        client.getEstimatedDobCode());
  }

  @Test
  public void shouldSetEstimatedDOBCodeYesWhenAgeAndAgePeriodNotBlank() {
    Client client = new ClientResourceBuilder().setBirthDate(null).build();
    ReferralClient referralClient = new ReferralClientResourceBuilder().setAgeNumber((short) 10)
        .setAgePeriodCode("Y").buildReferralClient();
    R04880EstimatedDOBCodeSetting rule = new R04880EstimatedDOBCodeSetting(client, referralClient,
        dateStarted);
    rule.execute();
    assertEquals(gov.ca.cwds.rest.api.domain.cms.Client.ESTIMATED_DOB_CODE_YES,
        client.getEstimatedDobCode());
  }

  @Test
  public void shouldSetEstimatedDOBCodeUnknownWhenBirthDateAgeAgePeriodCodeBlank() {
    Client client = new ClientResourceBuilder().setBirthDate("").build();
    ReferralClient referralClient = new ReferralClientResourceBuilder().setAgeNumber(null)
        .setAgePeriodCode("").buildReferralClient();

    R04880EstimatedDOBCodeSetting rule = new R04880EstimatedDOBCodeSetting(client, referralClient,
        dateStarted);
    rule.execute();
    assertEquals(gov.ca.cwds.rest.api.domain.cms.Client.ESTIMATED_DOB_CODE_UNKNOWN,
        client.getEstimatedDobCode());
  }

  @Test
  public void shouldSetEstimatedDOBCodeUnknownWhenBirthDateNullAgeAgePeriodCodeInvalid() {
    Client client = new ClientResourceBuilder().setBirthDate(null).build();
    ReferralClient referralClient = new ReferralClientResourceBuilder().setAgeNumber((short) 10)
        .setAgePeriodCode("").buildReferralClient();

    R04880EstimatedDOBCodeSetting rule = new R04880EstimatedDOBCodeSetting(client, referralClient,
        dateStarted);
    rule.execute();
    assertEquals(gov.ca.cwds.rest.api.domain.cms.Client.ESTIMATED_DOB_CODE_UNKNOWN,
        client.getEstimatedDobCode());

  }

  @Test
  public void shouldEstimateDOBWhenAgeAgeUnitProvided() {
    Client client = new ClientResourceBuilder().setBirthDate(null).build();
    ReferralClient referralClient = new ReferralClientResourceBuilder().setAgeNumber((short) 10)
        .setAgePeriodCode("Y").buildReferralClient();
    R04880EstimatedDOBCodeSetting rule = new R04880EstimatedDOBCodeSetting(client, referralClient,
        dateStarted);
    rule.execute();
    assertEquals("2008-01-01", client.getBirthDate());

    referralClient = new ReferralClientResourceBuilder().setAgeNumber((short) 10)
        .setAgePeriodCode("M").buildReferralClient();
    rule = new R04880EstimatedDOBCodeSetting(client, referralClient, dateStarted);
    rule.execute();
    assertEquals("2017-03-01", client.getBirthDate());

    referralClient = new ReferralClientResourceBuilder().setAgeNumber((short) 10)
        .setAgePeriodCode("W").buildReferralClient();
    rule = new R04880EstimatedDOBCodeSetting(client, referralClient, dateStarted);
    rule.execute();
    assertEquals("2017-10-23", client.getBirthDate());

    referralClient = new ReferralClientResourceBuilder().setAgeNumber((short) 10)
        .setAgePeriodCode("D").buildReferralClient();
    rule = new R04880EstimatedDOBCodeSetting(client, referralClient, dateStarted);
    rule.execute();
    assertEquals("2017-12-22", client.getBirthDate());

    referralClient = new ReferralClientResourceBuilder().setAgeNumber((short) 10)
        .setAgePeriodCode("U").buildReferralClient();
    rule = new R04880EstimatedDOBCodeSetting(client, referralClient, dateStarted);
    rule.execute();
    assertEquals(dateStarted, client.getBirthDate());
  }

}
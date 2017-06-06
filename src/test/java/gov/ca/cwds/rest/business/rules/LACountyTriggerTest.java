package gov.ca.cwds.rest.business.rules;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import gov.ca.cwds.data.cms.CountyTriggerDao;
import gov.ca.cwds.data.persistence.cms.ClientAddress;
import gov.ca.cwds.data.persistence.cms.CountyTrigger;
import gov.ca.cwds.data.persistence.cms.Referral;
import gov.ca.cwds.data.persistence.cms.ReferralClient;
import gov.ca.cwds.data.persistence.cms.SystemCodeTestHarness;

/**
 * @author CWDS API Team
 *
 */
public class LACountyTriggerTest {

  private static final ObjectMapper MAPPER = SystemCodeTestHarness.MAPPER;

  private CountyTriggerDao countyTriggerDao;
  private LACountyTrigger laCountyTrigger;

  private static CountyTrigger countyTrigger;

  @SuppressWarnings("javadoc")
  @Rule
  public ExpectedException thrown = ExpectedException.none();

  /**
   * @throws Exception on initialization error
   */
  @Before
  public void setup() throws Exception {
    countyTriggerDao = mock(CountyTriggerDao.class);
    laCountyTrigger = new LACountyTrigger(countyTriggerDao);
    countyTrigger = null;
  }

  /*
   * Test for checking the referral county trigger created with the FKAddress_T
   */
  @SuppressWarnings("javadoc")
  @Test
  public void createReferralCountyTriggerTest()
      throws JsonParseException, JsonMappingException, IOException {

    when(countyTriggerDao.find(any(String.class))).thenReturn(null);

    gov.ca.cwds.rest.api.domain.cms.Referral validDomainReferral = MAPPER.readValue(
        fixture("fixtures/legacy/business/rules/laCountyTrigger/validReferral.json"),
        gov.ca.cwds.rest.api.domain.cms.Referral.class);

    when(countyTriggerDao.create(any(CountyTrigger.class))).thenAnswer(new Answer<CountyTrigger>() {

      @Override
      public CountyTrigger answer(InvocationOnMock invocation) throws Throwable {
        CountyTrigger report = (CountyTrigger) invocation.getArguments()[0];
        countyTrigger = report;
        return report;
      }
    });

    Referral referral = new Referral("ABC1234567", validDomainReferral, "BTr");
    laCountyTrigger.createCountyTrigger(referral);
    assertThat(countyTrigger.getCountyTriggerEmbeddable().getCountyOwnershipT(),
        is(equalTo("1234567ABC")));
  }

  /*
   * Test for checking referral county trigger not created as FkAddress_T is null
   */
  @SuppressWarnings("javadoc")
  @Test
  public void referralFkAddresssIdNullTest()
      throws JsonParseException, JsonMappingException, IOException {

    when(countyTriggerDao.find(any(String.class))).thenReturn(null);

    gov.ca.cwds.rest.api.domain.cms.Referral validDomainReferral = MAPPER.readValue(
        fixture(
            "fixtures/legacy/business/rules/laCountyTrigger/allegesAbuseOccurredAtAddressIdNull.json"),
        gov.ca.cwds.rest.api.domain.cms.Referral.class);

    when(countyTriggerDao.create(any(CountyTrigger.class))).thenAnswer(new Answer<CountyTrigger>() {

      @Override
      public CountyTrigger answer(InvocationOnMock invocation) throws Throwable {
        CountyTrigger report = (CountyTrigger) invocation.getArguments()[0];
        countyTrigger = report;
        return report;
      }
    });

    Referral referral = new Referral("ABC1234567", validDomainReferral, "BTr");
    laCountyTrigger.createCountyTrigger(referral);
    assertThat(countyTrigger, is(equalTo(null)));
  }

  /*
   * Test for checking the referralClient county trigger created with the FKClient_T
   */
  @SuppressWarnings("javadoc")
  @Test
  public void createReferralClientCountyTriggerTest()
      throws JsonParseException, JsonMappingException, IOException {

    when(countyTriggerDao.find(any(String.class))).thenReturn(null);

    gov.ca.cwds.rest.api.domain.cms.ReferralClient validDomainReferralClient = MAPPER.readValue(
        fixture("fixtures/legacy/business/rules/laCountyTrigger/validReferralClient.json"),
        gov.ca.cwds.rest.api.domain.cms.ReferralClient.class);

    when(countyTriggerDao.create(any(CountyTrigger.class))).thenAnswer(new Answer<CountyTrigger>() {

      @Override
      public CountyTrigger answer(InvocationOnMock invocation) throws Throwable {
        CountyTrigger report = (CountyTrigger) invocation.getArguments()[0];
        countyTrigger = report;
        return report;
      }
    });

    ReferralClient referralClient = new ReferralClient(validDomainReferralClient, "BTr");
    laCountyTrigger.createCountyTrigger(referralClient);
    assertThat(countyTrigger.getCountyTriggerEmbeddable().getCountyOwnershipT(),
        is(equalTo("ABC1234567")));
  }

  /*
   * Test for checking referralClient county trigger not created as FkClient_T is null
   */
  @SuppressWarnings("javadoc")
  @Test
  public void referralClientFkClientIdNullTest()
      throws JsonParseException, JsonMappingException, IOException {

    when(countyTriggerDao.find(any(String.class))).thenReturn(null);

    gov.ca.cwds.rest.api.domain.cms.ReferralClient validDomainReferralClient = MAPPER.readValue(
        fixture(
            "fixtures/legacy/business/rules/laCountyTrigger/referralClientFkClientTIdNull.json"),
        gov.ca.cwds.rest.api.domain.cms.ReferralClient.class);

    when(countyTriggerDao.create(any(CountyTrigger.class))).thenAnswer(new Answer<CountyTrigger>() {

      @Override
      public CountyTrigger answer(InvocationOnMock invocation) throws Throwable {
        CountyTrigger report = (CountyTrigger) invocation.getArguments()[0];
        countyTrigger = report;
        return report;
      }
    });

    ReferralClient referralClient = new ReferralClient(validDomainReferralClient, "BTr");
    laCountyTrigger.createCountyTrigger(referralClient);
    assertThat(countyTrigger, is(equalTo(null)));
  }

  /*
   * Test for checking the clientAddress county trigger created with the CL_ADDRT
   */
  @SuppressWarnings("javadoc")
  @Test
  public void createClientAddressCountyTriggerTest()
      throws JsonParseException, JsonMappingException, IOException {

    when(countyTriggerDao.find(any(String.class))).thenReturn(null);

    gov.ca.cwds.rest.api.domain.cms.ClientAddress validDomainClientAddress = MAPPER.readValue(
        fixture("fixtures/legacy/business/rules/laCountyTrigger/validClientAddress.json"),
        gov.ca.cwds.rest.api.domain.cms.ClientAddress.class);

    when(countyTriggerDao.create(any(CountyTrigger.class))).thenAnswer(new Answer<CountyTrigger>() {

      @Override
      public CountyTrigger answer(InvocationOnMock invocation) throws Throwable {
        CountyTrigger report = (CountyTrigger) invocation.getArguments()[0];
        countyTrigger = report;
        return report;
      }
    });

    ClientAddress clientAddress = new ClientAddress("1234567ABC", validDomainClientAddress, "BTr");
    laCountyTrigger.createClientAddressCountyTrigger(clientAddress);
    assertThat(countyTrigger.getCountyTriggerEmbeddable().getCountyOwnershipT(),
        is(equalTo("1234567ABC")));
  }

  /*
   * Test for checking clientAddress county trigger not created as FkAddress_T is null
   */
  @SuppressWarnings("javadoc")
  @Test
  public void clientAddressfkAddressIdNullTest()
      throws JsonParseException, JsonMappingException, IOException {

    when(countyTriggerDao.find(any(String.class))).thenReturn(null);

    gov.ca.cwds.rest.api.domain.cms.ClientAddress validDomainClientAddress = MAPPER.readValue(
        fixture("fixtures/legacy/business/rules/laCountyTrigger/fkAddressTNull.json"),
        gov.ca.cwds.rest.api.domain.cms.ClientAddress.class);

    when(countyTriggerDao.create(any(CountyTrigger.class))).thenAnswer(new Answer<CountyTrigger>() {

      @Override
      public CountyTrigger answer(InvocationOnMock invocation) throws Throwable {
        CountyTrigger report = (CountyTrigger) invocation.getArguments()[0];
        countyTrigger = report;
        return report;
      }
    });

    ClientAddress clientAddress = new ClientAddress("ABC1234567", validDomainClientAddress, "BTr");
    laCountyTrigger.createClientAddressCountyTrigger(clientAddress);
    assertThat(countyTrigger, is(equalTo(null)));
  }

  /*
   * Test for checking clientAddress county trigger not created as FkClient_T is null
   */
  @SuppressWarnings("javadoc")
  @Test
  public void clientAddressfkclientIdNullTest()
      throws JsonParseException, JsonMappingException, IOException {

    when(countyTriggerDao.find(any(String.class))).thenReturn(null);

    gov.ca.cwds.rest.api.domain.cms.ClientAddress validDomainClientAddress = MAPPER.readValue(
        fixture("fixtures/legacy/business/rules/laCountyTrigger/fkClientTNull.json"),
        gov.ca.cwds.rest.api.domain.cms.ClientAddress.class);

    when(countyTriggerDao.create(any(CountyTrigger.class))).thenAnswer(new Answer<CountyTrigger>() {

      @Override
      public CountyTrigger answer(InvocationOnMock invocation) throws Throwable {
        CountyTrigger report = (CountyTrigger) invocation.getArguments()[0];
        countyTrigger = report;
        return report;
      }
    });

    ClientAddress clientAddress = new ClientAddress("ABC1234567", validDomainClientAddress, "BTr");
    laCountyTrigger.createClientAddressCountyTrigger(clientAddress);
    assertThat(countyTrigger, is(equalTo(null)));
  }

}
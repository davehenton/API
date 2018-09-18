package gov.ca.cwds.rest.services.screening.participant;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.powermock.api.mockito.PowerMockito.when;

import javax.persistence.EntityNotFoundException;

import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;

import gov.ca.cwds.data.cms.ClientDao;
import gov.ca.cwds.data.cms.TestIntakeCodeCache;
import gov.ca.cwds.data.ns.ScreeningDao;
import gov.ca.cwds.data.persistence.ns.ScreeningEntity;
import gov.ca.cwds.fixture.ParticipantIntakeApiResourceBuilder;
import gov.ca.cwds.fixture.ScreeningEntityBuilder;
import gov.ca.cwds.rest.api.domain.LegacyDescriptor;
import gov.ca.cwds.rest.api.domain.ParticipantIntakeApi;
import gov.ca.cwds.rest.api.domain.cms.LegacyTable;
import gov.ca.cwds.rest.api.domain.enums.ScreeningStatus;
import gov.ca.cwds.rest.services.ServiceException;
import gov.ca.cwds.rest.services.screeningparticipant.ParticipantDaoFactoryImpl;
import gov.ca.cwds.rest.services.screeningparticipant.ParticipantMapperFactoryImpl;

/**
 * @author CWDS API Team
 */
public class ParticipantTransformerTest {

  /**
   * Initialize intake code cache
   */
  private TestIntakeCodeCache testIntakeCodeCache = new TestIntakeCodeCache();

  private ParticipantTransformer participantTransformer = new ParticipantTransformer();
  private ScreeningDao screeningDao;
  private ClientDao clientDao;
  private ParticipantService participantService;
  private ParticipantDaoFactoryImpl participantDaoFactory;
  private ParticipantMapperFactoryImpl participantMapperFactoryImpl;

  @Before
  public void setup() {
    screeningDao = mock(ScreeningDao.class);
    participantService = mock(ParticipantService.class);
    participantDaoFactory = mock(ParticipantDaoFactoryImpl.class);
    participantMapperFactoryImpl = mock(ParticipantMapperFactoryImpl.class);
    clientDao = mock(ClientDao.class);

    participantTransformer.setScreeningDao(screeningDao);
    participantTransformer.setParticipantDaoFactory(participantDaoFactory);
    participantTransformer.setParticipantMapperFactoryImpl(participantMapperFactoryImpl);
  }

  /**
   *
   */
  @Test(expected = EntityNotFoundException.class)
  public void testScreeningIdNotFoundInPostgres() {
    LegacyDescriptor legacyDescriptor = new LegacyDescriptor("Abc1234567", null, new DateTime(),
        LegacyTable.REPORTER.getName(), null);
    ParticipantIntakeApi participantIntakeApi = new ParticipantIntakeApiResourceBuilder()
        .setScreeningId("18").setLegacyDescriptor(legacyDescriptor).build();
    participantTransformer.prepareParticipantObject(participantIntakeApi);
  }

  /**
   *
   */
  @Test(expected = ServiceException.class)
  public void testScreeningIdNull() {
    LegacyDescriptor legacyDescriptor = new LegacyDescriptor("Abc1234567", null, new DateTime(),
        LegacyTable.REPORTER.getName(), null);
    ParticipantIntakeApi participantIntakeApi = new ParticipantIntakeApiResourceBuilder()
        .setScreeningId(null).setLegacyDescriptor(legacyDescriptor).build();
    participantTransformer.prepareParticipantObject(participantIntakeApi);
    assertThat("issue_details[0].technical_message", is(equalTo("Screening not found")));
  }

  /**
   *
   */
  @Test(expected = ServiceException.class)
  public void testScreeningIsSubmitted() {
    LegacyDescriptor legacyDescriptor = new LegacyDescriptor("Abc1234567", null, new DateTime(),
        LegacyTable.REPORTER.getName(), null);
    ParticipantIntakeApi participantIntakeApi = new ParticipantIntakeApiResourceBuilder()
        .setScreeningId("12").setLegacyDescriptor(legacyDescriptor).build();
    ScreeningEntity screeningEntity = new ScreeningEntityBuilder().setId("12")
        .setScreeningStatus(ScreeningStatus.SUBMITTED.getStatus()).build();
    when(screeningDao.find(anyString())).thenReturn(screeningEntity);
    participantTransformer.prepareParticipantObject(participantIntakeApi);
    assertThat("issue_details[0].technical_message",
        is(equalTo("Screeening is already Submitted")));
  }

  /**
   *
   */
  @Test
  public void prepareParticipantObjectNewParticipant() {
    ParticipantIntakeApi participantIntakeApi = new ParticipantIntakeApiResourceBuilder()
        .setScreeningId("18").setLegacyDescriptor(null).build();
    when(screeningDao.find(any(String.class))).thenReturn(new ScreeningEntity());
    when(participantService.persistParticipantObjectInNS(any())).thenReturn(participantIntakeApi);
    ParticipantIntakeApi expected =
        participantTransformer.prepareParticipantObject(participantIntakeApi);
    assertThat(expected, is(notNullValue()));
  }

}
package gov.ca.cwds;

import com.fasterxml.jackson.databind.ObjectMapper;
import gov.ca.cwds.rest.ApiApplication;
import gov.ca.cwds.rest.ApiConfiguration;
import gov.ca.cwds.test.support.BaseApiTest;
import gov.ca.cwds.test.support.BaseDropwizardApplication;
import gov.ca.cwds.test.support.DatabaseHelper;
import io.dropwizard.jackson.Jackson;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import org.junit.BeforeClass;
import org.junit.ClassRule;


import static org.assertj.core.api.Assertions.assertThat;

/**
 * CWDS API Team
 */
public class IntakeBaseTest extends BaseApiTest<ApiConfiguration> {

  @ClassRule
  public static final BaseDropwizardApplication<ApiConfiguration> application =
      new BaseDropwizardApplication<>(ApiApplication.class, "config/test-api.yml");

  @Override
  protected BaseDropwizardApplication<ApiConfiguration> getApplication() {
    return application;
  }

  @BeforeClass
  public static void beforeClass() throws Exception {
    ApiConfiguration configuration = application.getConfiguration();
/*
    new DatabaseHelper(
        configuration.getNsDataSourceFactory().getUrl(),
        configuration.getNsDataSourceFactory().getUser(),
        configuration.getNsDataSourceFactory().getPassword()).runScript("liquibase/intake_ns_database_master.xml");
*/

    new DatabaseHelper(
        configuration.getCmsDataSourceFactory().getUrl(),
        configuration.getCmsDataSourceFactory().getUser(),
        configuration.getCmsDataSourceFactory().getPassword()).runScript("liquibase/intake-cwsint-database-master.xml");

    new DatabaseHelper(
        configuration.getRsDataSourceFactory().getUrl(),
        configuration.getRsDataSourceFactory().getUser(),
        configuration.getRsDataSourceFactory().getPassword()).runScript("liquibase/cwsrs1-database-master.xml");
  }

  public void assertEqualJsonArrays(String expectedJson, String actualJson) throws IOException {
    ObjectMapper objectMapper = Jackson.newObjectMapper();
    List expectedList = objectMapper.readValue(expectedJson, List.class);
    List actualList = objectMapper.readValue(actualJson, List.class);
    assertThat(expectedList).isEqualTo(actualList);
  }

  public void assertEqualJson(String expectedJson, String actualJson) throws IOException {
    ObjectMapper objectMapper = Jackson.newObjectMapper();
    Map expectedMap = objectMapper.readValue(expectedJson, Map.class);
    Map actualMap = objectMapper.readValue(actualJson, Map.class);
    assertThat(actualMap).isEqualTo(expectedMap);
  }
}
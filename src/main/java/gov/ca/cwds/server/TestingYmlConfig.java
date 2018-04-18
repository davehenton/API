package gov.ca.cwds.server;

import java.io.InputStream;

import org.yaml.snakeyaml.Yaml;

import gov.ca.cwds.authenticate.config.CwdsAuthenticationClientConfig;
import gov.ca.cwds.rest.ApiConfiguration;

/**
 * @author CWDS API Team
 *
 */
public class TestingYmlConfig {
  ApiConfiguration configuration;

  /**
   * @param configuration - configuration
   */
  public TestingYmlConfig(ApiConfiguration configuration) {
    this.configuration = configuration;
  }

  /**
   * @return the createdTestConfig
   */
  public CwdsAuthenticationClientConfig createTestConfig() {
    Yaml yaml = new Yaml();
    InputStream in = getClass().getResourceAsStream(configuration.getTestConfig().getConfigFile());
    return yaml.loadAs(in, CwdsAuthenticationClientConfig.class);
  }
}

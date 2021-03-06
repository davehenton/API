package gov.ca.cwds.inject;

import javax.ws.rs.client.Client;
import javax.ws.rs.core.MediaType;
import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.name.Named;
import com.google.inject.name.Names;
import gov.ca.cwds.health.resource.AuthServer;
import gov.ca.cwds.health.resource.IntakeLovCheck;
import gov.ca.cwds.health.resource.MQTExistCheck;
import gov.ca.cwds.health.resource.SpGenclncntyExistCheck;
import gov.ca.cwds.health.resource.SpSpssaname3ExistCheck;
import gov.ca.cwds.health.resource.SwaggerEndpoint;
import gov.ca.cwds.health.resource.SystemCodeCheck;
import gov.ca.cwds.health.resource.ViewExistCheck;
import gov.ca.cwds.rest.SwaggerConfiguration;
import io.dropwizard.client.JerseyClientBuilder;
import io.dropwizard.setup.Environment;

public class HealthCheckModule extends AbstractModule {

  /**
   * Default, no-op constructor.
   */
  public HealthCheckModule() {
    // Default, no-op.
  }

  @Override
  protected void configure() {
    bind(String.class).annotatedWith(Names.named("http-media")).toInstance(MediaType.TEXT_HTML);
    bind(String.class).annotatedWith(Names.named("json-media"))
        .toInstance(MediaType.APPLICATION_JSON);
    bind(AuthServer.class);
    bind(SwaggerEndpoint.class);
    bind(IntakeLovCheck.class);
    bind(SystemCodeCheck.class);
    bind(MQTExistCheck.class);
    bind(ViewExistCheck.class);
    bind(SpGenclncntyExistCheck.class);
    bind(SpSpssaname3ExistCheck.class);
  }

  @Named("swaggerTokenClient")
  @Provides
  public Client getAuthClient(final Environment environment) {
    return buildClient(environment, "SwaggerTokenHealthCheckRestClient");
  }

  @Named("swaggerClient")
  @Provides
  public Client getSwaggerClient(final Environment environment) {
    return buildClient(environment, "SwaggerHealthCheckRestClient");
  }

  private Client buildClient(Environment environment, String name) {
    return new JerseyClientBuilder(environment).build(name);
  }

  @Named("swagger-token-url")
  @Provides
  private String getAuthUrl(SwaggerConfiguration swaggerConfiguration) {
    return swaggerConfiguration.getTokenUrl();
  }

  @Named("swagger-url")
  @Provides
  private String getSwaggerUrl(SwaggerConfiguration swaggerConfiguration) {
    return swaggerConfiguration.getCallbackUrl();
  }

}

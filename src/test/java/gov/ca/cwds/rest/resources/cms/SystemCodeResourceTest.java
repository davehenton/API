package gov.ca.cwds.rest.resources.cms;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import gov.ca.cwds.rest.resources.ResourceDelegate;
import gov.ca.cwds.rest.resources.ServiceBackedResourceDelegate;
import io.dropwizard.testing.junit.ResourceTestRule;

import javax.ws.rs.core.MediaType;

import org.hamcrest.junit.ExpectedException;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * NOTE : The CWDS API Team has taken the pattern of delegating Resource functions to
 * {@link ServiceBackedResourceDelegate}. As such the tests in here reflect that assumption.
 * 
 * @author CWDS API Team
 */
public class SystemCodeResourceTest {

  private static final String FOUND_RESOURCE = "/lov/GVR_ENTC";

  @SuppressWarnings("javadoc")
  @Rule
  public ExpectedException thrown = ExpectedException.none();

  private final static ResourceDelegate resourceDelegate = mock(ResourceDelegate.class);

  @SuppressWarnings("javadoc")
  @ClassRule
  public final static ResourceTestRule inMemoryResource = ResourceTestRule.builder()
      .addResource(new SystemCodeResource(resourceDelegate)).build();

  @SuppressWarnings("javadoc")
  @Before
  public void setup() throws Exception {
    Mockito.reset(resourceDelegate);
  }

  /**
   * Get Tests
   * 
   * @throws Exception required for test compilation
   */
  @Test
  public void getDelegatesToResourceDelegate() throws Exception {
    inMemoryResource.client().target(FOUND_RESOURCE).request().accept(MediaType.APPLICATION_JSON)
        .get();
    verify(resourceDelegate).get("GVR_ENTC");
  }

}

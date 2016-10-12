package gov.ca.cwds.rest.resources;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.mockito.Mockito.mock;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.MediaType;

import org.glassfish.jersey.test.grizzly.GrizzlyWebTestContainerFactory;
import org.hamcrest.junit.ExpectedException;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import gov.ca.cwds.rest.api.domain.Address;
import gov.ca.cwds.rest.api.domain.Person;
import io.dropwizard.jackson.Jackson;
import io.dropwizard.testing.junit.ResourceTestRule;

public class AddressResourceTest {
	private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

	private static final String ROOT_RESOURCE = "/addresses/";
	private static final String FOUND_RESOURCE = "/addresses/1";
	private static final String NOT_FOUND_RESOURCE = "/addresses/2";

	@Rule
	public ExpectedException thrown = ExpectedException.none();

	@SuppressWarnings({ "unchecked" })
	private final static CrudsResource<Address> mockedCrudsResource = mock(CrudsResource.class);

	@ClassRule
	public final static ResourceTestRule inMemoryResource = ResourceTestRule.builder().addResource(new AddressResource(mockedCrudsResource))
			.build();

	@ClassRule
	public final static ResourceTestRule grizzlyResource = ResourceTestRule.builder()
			.setTestContainerFactory(new GrizzlyWebTestContainerFactory()).addResource(new AddressResource(mockedCrudsResource)).build();

	@Before
	public void setup() throws Exception {

	}

	/*
	 * Get Tests
	 */
	@Test
	public void getReturns200OnSuccess() throws Exception {
		int receivedStatus = inMemoryResource.client().target(FOUND_RESOURCE).request()
				.accept(MediaType.APPLICATION_JSON).get().getStatus();
		int expectedStatus = 200;
		assertThat(receivedStatus, is(expectedStatus));
	}

	@Test
	public void getReturns406OnWrongAcceptType() throws Exception {
		int receivedStatus = inMemoryResource.client().target(FOUND_RESOURCE).request().accept("UNSUPPORTED_VERSION")
				.get().getStatus();
		int expectedStatus = 406;
		assertThat(receivedStatus, is(equalTo(expectedStatus)));
	}

	@Test
	public void getReturns404WhenNotFound() throws Exception {
		int receivedStatus = inMemoryResource.client().target(NOT_FOUND_RESOURCE).request()
				.accept(MediaType.APPLICATION_JSON).get().getStatus();
		int expectedStatus = 404;
		assertThat(receivedStatus, is(equalTo(expectedStatus)));
	}

	// @Test
	// public void getDelegatesToCrudsResourceImpl() throws Exception {
	// inMemoryResource.client().target(FOUND_RESOURCE).request().accept(MediaType.APPLICATION_JSON).get().getStatus();
	// verify(mockedCrudsResource).get("1", MediaType.APPLICATION_JSON);
	// }

	@Test
	public void getReturnsJsonWhichCanBeSerializedIntoAddress() throws Exception {
		String json = inMemoryResource.client().target(FOUND_RESOURCE).request().accept(MediaType.APPLICATION_JSON)
				.get().readEntity(String.class);
		Address getresult = MAPPER.readValue(json, Address.class);
		Address expected = new Address("742 Evergreen Terrace", "Springfield", "WA", 98700);
		assertThat(getresult, is(expected));
	}

	/*
	 * Create Tests
	 */
	@Test
	public void createReturns201OnSuccess() throws Exception {
		Address address = new Address("742 Evergreen Terrace", "Springfield", "WA", 98700);
		int status = grizzlyResource.getJerseyTest().target(ROOT_RESOURCE).request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(address, MediaType.APPLICATION_JSON)).getStatus();
		assertThat(status, is(201));
	}

	@Test
	public void createReturns400WhenCannotProcessJson() throws Exception {
		// create expects to deserialize the payload to a person - lets give it
		// an address instead.
		Person person = new Person("first", "last", "male", "4/1/1990", "123456789", null);
		int status = grizzlyResource.getJerseyTest().target(ROOT_RESOURCE).request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(person, MediaType.APPLICATION_JSON)).getStatus();
		assertThat(status, is(400));

	}

	@Test
	public void createReturns406OnWrongAcceptType() throws Exception {
		Address address = new Address("742 Evergreen Terrace", "Springfield", "WA", 98700);
		int status = grizzlyResource.getJerseyTest().target(ROOT_RESOURCE).request().accept("UNSUPPORTED_TYPE")
				.post(Entity.entity(address, MediaType.APPLICATION_JSON)).getStatus();
		assertThat(status, is(406));
	}

	@Test
	public void createReturns422WhenCannotValidateAddress() throws Exception {
		//NOTE : address currently has no validation/constraints on it.
//		Address address = new Address("742 Evergreen Terrace", "Springfield", "WA", 98700);
//		int status = grizzlyResource.getJerseyTest().target(ROOT_RESOURCE).request().accept(MediaType.APPLICATION_JSON)
//				.post(Entity.entity(address, MediaType.APPLICATION_JSON)).getStatus();
//		assertThat(status, is(422));
	}

	@Test
	public void createReturnsLocationHeader() throws Exception {
		Address address = new Address("742 Evergreen Terrace", "Springfield", "WA", 98700);
		Object o = grizzlyResource.getJerseyTest().target(ROOT_RESOURCE).request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(address, MediaType.APPLICATION_JSON)).getHeaders().get("Location");
		assertThat(o, is(notNullValue()));
	}

	@Test
	public void createReturnsProperJson() throws Exception {
		String expected = "{\"id\": \"1\",\"Address\": { \"street_address\": \"742 Evergreen Terrace\", \"city\": \"Springfield\", \"state\": \"WA\", \"zip\": 98700 } }";
		
		Address address = new Address("742 Evergreen Terrace", "Springfield", "WA", 98700);
		String json = grizzlyResource.getJerseyTest().target(ROOT_RESOURCE).request().accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(address, MediaType.APPLICATION_JSON)).readEntity(String.class);

		expected = expected.replaceAll("\\s+","");
		json = json.replaceAll("\\s+","");
		
		assertThat(json, is(expected));
	}
	
	/*
	 * Delete Tests
	 */
	@Test
	public void deleteReturns501() throws Exception {
		int receivedStatus = inMemoryResource.client().target(FOUND_RESOURCE).request()
				.accept(MediaType.APPLICATION_JSON).delete().getStatus();
		int expectedStatus = 501;
		assertThat(receivedStatus, is(expectedStatus));
		
	}
	
	/*
	 * Update Tests
	 */
	@Test
	public void udpateReturns501() throws Exception {
		Address address = new Address("742 Evergreen Terrace", "Springfield", "WA", 98700);
		int status = grizzlyResource.getJerseyTest().target(ROOT_RESOURCE).request().accept(MediaType.APPLICATION_JSON)
				.put(Entity.entity(address, MediaType.APPLICATION_JSON)).getStatus();
		assertThat(status, is(501));		
	}
}

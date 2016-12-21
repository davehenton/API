package gov.ca.cwds.rest.api.persistence.cms;

import static io.dropwizard.testing.FixtureHelpers.fixture;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.dropwizard.jackson.Jackson;
import nl.jqno.equalsverifier.EqualsVerifier;
import nl.jqno.equalsverifier.Warning;


public class OtherChildInPlacemtHomeTest {

  private static final ObjectMapper MAPPER = Jackson.newObjectMapper();

  @Test
  public void equalsHashCodeWork() {
    EqualsVerifier.forClass(OtherChildInPlacemtHome.class).suppress(Warning.NONFINAL_FIELDS)
        .verify();
  }

  /*
   * Constructor test
   */
  @Test
  public void emtpyConstructorIsNotNull() throws Exception {
    assertThat(OtherChildInPlacemtHome.class.newInstance(), is(notNullValue()));
  }

  @Test
  public void persistentConstructorTest() throws Exception {
    OtherChildInPlacemtHome vocph = validOtherChildInPlacemtHome();

    OtherChildInPlacemtHome persistent =
        new OtherChildInPlacemtHome(vocph.getAnnualUnearnedIncomeAmount(), vocph.getBirthDate(),
            vocph.getFkplcHmT(), vocph.getGenderCode(), vocph.getId(), vocph.getName());

    assertThat(persistent.getAnnualUnearnedIncomeAmount(),
        is(equalTo(vocph.getAnnualUnearnedIncomeAmount())));
    assertThat(persistent.getBirthDate(), is(equalTo(vocph.getBirthDate())));
    assertThat(persistent.getFkplcHmT(), is(equalTo(vocph.getFkplcHmT())));
    assertThat(persistent.getGenderCode(), is(equalTo(vocph.getGenderCode())));
    assertThat(persistent.getId(), is(equalTo(vocph.getId())));
    assertThat(persistent.getName(), is(equalTo(vocph.getName())));
  }

  private OtherChildInPlacemtHome validOtherChildInPlacemtHome()
      throws JsonParseException, JsonMappingException, IOException {

    OtherChildInPlacemtHome validOtherChildInPlacemtHome =
        MAPPER.readValue(fixture("fixtures/domain/legacy/OtherChildInPlacemtHome/valid/valid.json"),
            OtherChildInPlacemtHome.class);

    return validOtherChildInPlacemtHome;

  }
}

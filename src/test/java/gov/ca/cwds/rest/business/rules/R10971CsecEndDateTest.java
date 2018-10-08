package gov.ca.cwds.rest.business.rules;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import gov.ca.cwds.fixture.CsecBuilder;
import gov.ca.cwds.rest.api.domain.Csec;

public class R10971CsecEndDateTest {

  private List<Csec> csecs = new ArrayList<>();
  private Csec csec;
  private static final String VICTIM_WHILE_ABSENT_FROM_PLACEMENT = "6871";

  @Test
  public void shouldReturnTrueIfCsecsIsNull() {

    assertEquals(new R10971CsecEndDate(csecs).isValid(), Boolean.TRUE);

  }

  @Test
  public void shouldReturnTrueIfACsecCodeIdIsNull() {
    csec = new CsecBuilder().setCsecCodeId(null).build();
    csecs.add(csec);
    assertEquals(new R10971CsecEndDate(csecs).isValid(), Boolean.TRUE);
  }

  @Test
  public void shouldReturnTrueIfValidCsecs() {
    csec = new CsecBuilder()
        .setCsecCodeId("6867")
        .setEndDate(null)
        .build();
    csecs.add(csec);
    assertEquals(new R10971CsecEndDate(csecs).isValid(), Boolean.TRUE);
  }

  @Test
  public void shouldReturnTrueWhenMultipleValidCsecs() {
    csec = new CsecBuilder()
        .setCsecCodeId("6867")
        .build();
    Csec csec1 = new CsecBuilder()
        .setCsecCodeId("6867")
        .build();
    csecs.add(csec);
    csecs.add(csec1);
    assertEquals(new R10971CsecEndDate(csecs).isValid(), Boolean.TRUE);

  }

  @Test
  public void shouldReturnTrueIfVictimWhileAbsentFromPlacementWithEndDate() {
    csec = new CsecBuilder()
        .setCsecCodeId(VICTIM_WHILE_ABSENT_FROM_PLACEMENT)
        .build();
    csecs.add(csec);
    assertEquals(new R10971CsecEndDate(csecs).isValid(), Boolean.TRUE);

  }

  @Test
  public void shouldReturnFalseIfVictimWhileAbsentFromPlacementWithoutEndDate() {
    csec = new CsecBuilder()
        .setCsecCodeId(VICTIM_WHILE_ABSENT_FROM_PLACEMENT)
        .setEndDate(null)
        .build();
    csecs.add(csec);
    assertEquals(new R10971CsecEndDate(csecs).isValid(), Boolean.FALSE);
  }

}

package gov.ca.cwds.rest.jdbi.cms;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;

import javax.persistence.EntityExistsException;
import javax.persistence.EntityNotFoundException;

import org.hamcrest.junit.ExpectedException;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
// import org.hibernate.mapping.PrimaryKey;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import gov.ca.cwds.rest.api.persistence.cms.CrossReport;
// import gov.ca.cwds.rest.api.persistence.cms.CrossReport.PrimaryKey;

public class CrossReportDaoIT {
  private SessionFactory sessionFactory;
  private CrossReportDao crossreportDao;

  @Rule
  public ExpectedException thrown = ExpectedException.none();

  @Before
  public void setup() {
    sessionFactory = new Configuration().configure().buildSessionFactory();
    sessionFactory.getCurrentSession().beginTransaction();
    crossreportDao = new CrossReportDao(sessionFactory);
  }

  @After
  public void tearndown() {
    sessionFactory.close();
  }

  @Test
  public void testFind() {
    CrossReport found = crossreportDao.find(new CrossReport.PrimaryKey("925q4As0AB", "7wviAIk0AB"));
    assertThat(found.getThirdId(), is(equalTo("7wviAIk0AB")));
    assertThat(found.getReferralId(), is(equalTo("925q4As0AB")));

  }

  @Test
  public void testCreate() {
    CrossReport crossreport = new CrossReport("925q4As0AC", "7wviAIk0AC", (short) 2094, "N", "N",
        null, "  ", 0, BigDecimal.ZERO, null, " ", " ", "925q4As0AC", "0AC", " ", " ", " ", "34",
        "N", "N", "N");
    CrossReport created = crossreportDao.create(crossreport);
    assertThat(created, is(crossreport));

  }

  @Test
  public void testCreateExistingEntityException() {
    thrown.expect(EntityExistsException.class);
    CrossReport crossreport = new CrossReport("Aj20cK10WS", "CVDUfmj0WS", (short) 2094, "N", "N",
        null, "  ", 0, BigDecimal.ZERO, null, " ", " ", "925q4As0AC", "0AC", " ", " ", " ", "34",
        "N", "N", "N");
    crossreportDao.create(crossreport);
  }

  @Test
  public void testDelete() {
    String thirdId = "FsMh09h00J";
    String referralId = "AAKhYyf00J";
    CrossReport.PrimaryKey primaryKey = new CrossReport.PrimaryKey(referralId, thirdId);
    CrossReport delete = crossreportDao.delete(primaryKey);
    assertThat(delete.getThirdId(), is(thirdId));
    assertThat(delete.getReferralId(), is(referralId));
  }

  @Test
  public void testUpdate() {
    CrossReport crossreport = new CrossReport("Aj20cK10WS", "CVDUfmj0WS", (short) 2094, "N", "N",
        null, "  ", 0, BigDecimal.ZERO, null, " ", " ", "925q4As0AC", "0AC", " ", " ", " ", "34",
        "N", "N", "N");
    CrossReport updated = crossreportDao.update(crossreport);
    assertThat(updated, is(crossreport));
  }

  @Test
  public void testUpdateEntityNotFoundException() {
    thrown.expect(EntityNotFoundException.class);
    CrossReport crossreport = new CrossReport("ZZ20cK10WS", "ZZDUfmj0WS", (short) 2094, "N", "N",
        null, "  ", 0, BigDecimal.ZERO, null, " ", " ", "925q4As0AC", "0AC", " ", " ", " ", "34",
        "N", "N", "N");
    crossreportDao.update(crossreport);
  }

}

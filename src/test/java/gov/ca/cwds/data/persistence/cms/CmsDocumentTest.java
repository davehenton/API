package gov.ca.cwds.data.persistence.cms;

import java.io.Serializable;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OrderBy;
import javax.persistence.Table;
import org.hibernate.annotations.Type;
import gov.ca.cwds.data.persistence.PersistentObject;
import gov.ca.cwds.rest.api.domain.DomainChef;
import gov.ca.cwds.data.persistence.cms.CmsDocument.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;
import static org.mockito.BDDMockito.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.junit.Test;

import gov.ca.cwds.data.persistence.cms.CmsDocument;

public class CmsDocumentTest {
  private final static DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
  private final static DateFormat tf = new SimpleDateFormat("HH:mm:ss");

  private String id = "0131351421120020*JONESMF 00004";
  private Short segmentCount = 1234;
  private Long docLength = (long) 1000;
  private String docAuth = "RAMESHA";
  private String docServ = "D7706001";
  private String docDate = "2007-01-31";
  private String docTime = "19:59:07";
  private String docName = "1234";
  private String compressionMethod = "CWSCMP01";
  private String baseBlob = "string";

  /*
   * Constructor test
   */
  @Test
  public void emtpyConstructorIsNotNull() throws Exception {
    assertThat(CmsDocument.class.newInstance(), is(notNullValue()));
  }

  @Test
  public void domainCmsDocumentConstructorTest() throws Exception {

    gov.ca.cwds.rest.api.domain.cms.CmsDocument domain =
        new gov.ca.cwds.rest.api.domain.cms.CmsDocument(id, segmentCount, docLength, docAuth,
            docServ, docDate, docTime, docName, compressionMethod, baseBlob);
    CmsDocument persistent = new CmsDocument(domain);
    assertThat(persistent.getId(), is(equalTo(id)));
    assertThat(persistent.getSegmentCount(), is(equalTo(segmentCount)));
    assertThat(persistent.getDocAuth(), is(equalTo(docAuth)));
    assertThat(persistent.getDocServ(), is(equalTo(docServ)));
    assertThat(persistent.getDocDate(), is(equalTo(df.parse(docDate))));
    assertThat(persistent.getDocTime(), is(equalTo(tf.parse(docTime))));
    assertThat(persistent.getDocName(), is(equalTo(docName)));
    assertThat(persistent.getCompressionMethod(), is(equalTo(compressionMethod)));

  }

  @Test
  public void persistentCmsDocumentContructorTest() throws Exception {

    CmsDocument persistent = new CmsDocument(id, segmentCount, docLength, docAuth, docServ,
        df.parse(docDate), tf.parse(docTime), docName, compressionMethod);
    assertThat(persistent.getId(), is(equalTo(id)));
    assertThat(persistent.getSegmentCount(), is(equalTo(segmentCount)));
    assertThat(persistent.getDocAuth(), is(equalTo(docAuth)));
    assertThat(persistent.getDocServ(), is(equalTo(docServ)));
    assertThat(persistent.getDocDate(), is(equalTo(df.parse(docDate))));
    assertThat(persistent.getDocTime(), is(equalTo(tf.parse(docTime))));
    assertThat(persistent.getDocName(), is(equalTo(docName)));
    assertThat(persistent.getCompressionMethod(), is(equalTo(compressionMethod)));

  }
	@Test
	public void type() throws Exception {
		// TODO auto-generated by JUnit Helper.
		assertThat(CmsDocument.class, notNullValue());
	}

	@Test
	public void instantiation() throws Exception {
		// TODO auto-generated by JUnit Helper.
		CmsDocument target = new CmsDocument();
		assertThat(target, notNullValue());
	}

	@Test
	public void getPrimaryKey_A$() throws Exception {
		// TODO auto-generated by JUnit Helper.
		CmsDocument target = new CmsDocument();
		// given
		// e.g. : given(mocked.called()).willReturn(1);
		// when
		Serializable actual = target.getPrimaryKey();
		// then
		// e.g. : verify(mocked).called();
		Serializable expected = null;
		assertThat(actual, is(equalTo(expected)));
	}

	@Test
	public void addBlobSegment_A$CmsDocumentBlobSegment() throws Exception {
		// TODO auto-generated by JUnit Helper.
		CmsDocument target = new CmsDocument();
		// given
		CmsDocumentBlobSegment blobSegment = mock(CmsDocumentBlobSegment.class);
		// e.g. : given(mocked.called()).willReturn(1);
		// when
		target.addBlobSegment(blobSegment);
		// then
		// e.g. : verify(mocked).called();
	}

}

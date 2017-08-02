package gov.ca.cwds.data.persistence.cms;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import gov.ca.cwds.data.persistence.PersistentObject;

/**
 * {@link PersistentObject} representing an County Trigger Embeddable
 * 
 * <p>
 * ReferralClientDeliveredServiceEmbeddable is the primaryKey representation of
 * ReferralClientDeliveredService and making the three columns(deliveredServiceId, referralId and
 * clientId) as composite keys, as the primary can be repeatable.
 * <p>
 * 
 * @author CWDS API Team
 * 
 */
@Embeddable
public class ReferralClientDeliveredServiceEmbeddable implements Serializable {

  /**
   * 
   */
  private static final long serialVersionUID = 1L;

  protected static final int CMS_ID_LEN = CmsPersistentObject.CMS_ID_LEN;

  @Column(name = "FKDL_SVC_T", length = CMS_ID_LEN)
  private String deliveredServiceId;

  @Column(name = "FKREFR_CL0", length = CMS_ID_LEN)
  private String referralId;

  @Column(name = "FKREFR_CLT", length = CMS_ID_LEN)
  private String clientId;

  /**
   * Default constructor
   * 
   * Required for Hibernate
   */
  public ReferralClientDeliveredServiceEmbeddable() {
    // no-opt
  }

  /**
   * @param deliveredServiceId - deliveredServiceId
   * @param referralId - referralId
   * @param clientId - clientId
   */
  public ReferralClientDeliveredServiceEmbeddable(String deliveredServiceId, String referralId,
      String clientId) {
    super();
    this.deliveredServiceId = deliveredServiceId;
    this.referralId = referralId;
    this.clientId = clientId;
  }

  /**
   * @return the deliveredServiceId
   */
  public String getDeliveredServiceId() {
    return deliveredServiceId;
  }

  /**
   * @return the referralId
   */
  public String getReferralId() {
    return referralId;
  }

  /**
   * @return the clientId
   */
  public String getClientId() {
    return clientId;
  }

  /**
   * {@inheritDoc}
   * 
   * @see java.lang.Object#hashCode()
   */
  @Override
  public final int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this, false);
  }

  /**
   * {@inheritDoc}
   * 
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public final boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj, false);
  }

}

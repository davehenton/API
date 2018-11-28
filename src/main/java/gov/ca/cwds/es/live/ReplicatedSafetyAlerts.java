package gov.ca.cwds.es.live;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import gov.ca.cwds.data.es.ElasticSearchSafetyAlert;

/**
 * Pseudo-normalized container for CMS safety alerts.
 * 
 * @author CWDS API Team
 */
@SuppressWarnings({"findsecbugs:SQL_INJECTION_JDBC", "squid:S2095",
    "findbugs:SE_TRANSIENT_FIELD_NOT_RESTORED", "squid:S1206", "serial"})
public class ReplicatedSafetyAlerts implements ReadablePerson {

  private static final long serialVersionUID = 8733181688462933133L;

  private String clientId;

  private List<ElasticSearchSafetyAlert> safetyAlerts = new ArrayList<>();

  /**
   * Default constructor.
   */
  public ReplicatedSafetyAlerts() {
    // Default, no-op
  }

  /**
   * Construct the object
   * 
   * @param clientId The referral client id.
   */
  public ReplicatedSafetyAlerts(String clientId) {
    this.clientId = clientId;
  }

  /**
   * Adds a safety alert to this container.
   * 
   * @param safetyAlert The safety alert to add.
   */
  public void addSafetyAlert(ElasticSearchSafetyAlert safetyAlert) {
    safetyAlerts.add(safetyAlert);
  }

  public List<ElasticSearchSafetyAlert> getSafetyAlerts() {
    return safetyAlerts;
  }

  public void setSafetyAlerts(List<ElasticSearchSafetyAlert> safetyAlerts) {
    this.safetyAlerts = safetyAlerts;
  }

  @Override
  public Serializable getPrimaryKey() {
    return this.clientId;
  }

  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this, false);
  }

  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj, false);
  }

}

package gov.ca.cwds.data.persistence.ns;

import gov.ca.cwds.data.persistence.PersistentObject;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * {@link PersistentObject} representing IntakeLOVCode.
 *
 * @author CWDS API Team
 */
@Entity
@Table(name = "intake_lov_codes")
@NamedQuery(name = "gov.ca.cwds.data.persistence.ns.IntakeLOVCode.findIntakeLOVCodeByIntakeCode",
    query = "SELECT c FROM IntakeLOVCode c WHERE c.intakeCode = :intakeCode")
public class IntakeLOVCode implements PersistentObject {

  @Id
  @Column(name = "cat_id")
  private Long catId;

  @Column(name = "lg_sys_id")
  private Long lgSysId;

  @Column(name = "intake_code")
  private String intakeCode;

  @Column(name = "intake_display")
  private String intakeDisplay;

  @Column(name = "omit_ind")
  private String omitInd;

  @Column(name = "parent_lg_sys_id")
  private Long parentLgSysId;

  /**
   * Default constructor
   *
   * Required for Hibernate
   */
  public IntakeLOVCode() {
    super();
  }

  public Long getCatId() {
    return catId;
  }

  public Long getLgSysId() {
    return lgSysId;
  }

  public String getIntakeCode() {
    return intakeCode;
  }

  public String getIntakeDisplay() {
    return intakeDisplay;
  }

  public String getOmitInd() {
    return omitInd;
  }

  public Long getParentLgSysId() {
    return parentLgSysId;
  }

  @Override
  public Serializable getPrimaryKey() {
    return getCatId();
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

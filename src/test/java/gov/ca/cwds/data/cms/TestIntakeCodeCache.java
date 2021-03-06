package gov.ca.cwds.data.cms;

import java.util.ArrayList;
import java.util.List;

import gov.ca.cwds.data.persistence.ns.IntakeLov;
import gov.ca.cwds.rest.api.domain.IntakeCodeCache;
import gov.ca.cwds.rest.api.domain.SystemCodeCategoryId;
import gov.ca.cwds.rest.api.domain.enums.AddressType;

/**
 * @author CWDS API Team
 */
public class TestIntakeCodeCache implements IntakeCodeCache {

  private static final long serialVersionUID = 1L;

  /**
   * 
   */
  public TestIntakeCodeCache() {
    register();
  }

  @Override
  public List<IntakeLov> getAllLegacySystemCodesForMeta(String metaId) {
    if (SystemCodeCategoryId.COMMERCIALLY_SEXUALLY_EXPLOITED_CHILDREN.equals(metaId)) {
      List<IntakeLov> lovs = new ArrayList<>();
      lovs.add(
          new IntakeLov((long) 6871, SystemCodeCategoryId.COMMERCIALLY_SEXUALLY_EXPLOITED_CHILDREN,
              "Victim while Absent from Placement", null, false, null, null, null, null,
              "Victim while Absent from Placement", null));
      lovs.add(
          new IntakeLov((long) 6867, SystemCodeCategoryId.COMMERCIALLY_SEXUALLY_EXPLOITED_CHILDREN,
              "At Risk", null, false, null, null, null, null, "At Risk", null));

      return lovs;
    }
    return null;
  }

  @Override
  public Short getLegacySystemCodeForIntakeCode(String metaId, String intakeCode) {
    if ("DSP_RSNC".contains(metaId) && "Abandoned".contains(intakeCode)) {
      return 6351;
    }
    if (SystemCodeCategoryId.STATE_CODE.contains(metaId) && "CA".contains(intakeCode)) {
      return 1828;
    }
    if (SystemCodeCategoryId.ADDRESS_TYPE.contains(metaId) && "Day Care".equals(intakeCode)) {
      return 28;
    }
    if (SystemCodeCategoryId.CROSS_REPORT_METHOD.contains(metaId)
        && "Electronic Report".contains(intakeCode)) {
      return 2095;
    }
    if (SystemCodeCategoryId.LANGUAGE_CODE.contains(metaId) && "English".contains(intakeCode)) {
      return 1253;
    }
    if (SystemCodeCategoryId.LANGUAGE_CODE.contains(metaId) && "Russian".contains(intakeCode)) {
      return 1271;
    }
    if (SystemCodeCategoryId.INJURY_HARM_TYPE.contains(metaId)
        && "General neglect".contains(intakeCode)) {
      return 2178;
    }
    if (SystemCodeCategoryId.REFERRAL_RESPONSE.contains(metaId)
        && "evaluate_out".contains(intakeCode)) {
      return 1519;
    }
    if (SystemCodeCategoryId.COMMUNICATION_METHOD.contains(metaId)
        && "in_person".contains(intakeCode)) {
      return 408;
    }
    if (SystemCodeCategoryId.COMMERCIALLY_SEXUALLY_EXPLOITED_CHILDREN.contains(metaId)
        && "At Risk".contains(intakeCode)) {
      return 6867;
    }
    if (SystemCodeCategoryId.SAFETY_ALERTS.contains(metaId)
        && "Dangerous Animal on Premises".contains(intakeCode)) {
      return 6401;
    }
    return null;
  }

  @Override
  public String getIntakeCodeForLegacySystemCode(Number systemCodeId, String intakeType) {
    if (1828 == systemCodeId.intValue()) {
      return "CA";
    }
    if (AddressType.HOME.getCode() == systemCodeId.intValue()) {
      return AddressType.HOME.getValue();
    }
    if (AddressType.DAY_CARE.getCode() == systemCodeId.intValue()) {
      return AddressType.DAY_CARE.getValue();
    }
    if (AddressType.COMMON.getCode() == systemCodeId.intValue()) {
      return AddressType.COMMON.getValue();
    }
    if (AddressType.HOMELESS.getCode() == systemCodeId.intValue()) {
      return AddressType.HOMELESS.getValue();
    }
    if (AddressType.OTHER.getCode() == systemCodeId.intValue()) {
      return AddressType.OTHER.getValue();
    }
    if (AddressType.PENAL_INSTITUTION.getCode() == systemCodeId.intValue()) {
      return AddressType.PENAL_INSTITUTION.getValue();
    }
    if (AddressType.PERMANENT_MAILING_ADDRESS.getCode() == systemCodeId.intValue()) {
      return AddressType.PERMANENT_MAILING_ADDRESS.getValue();
    }
    if (AddressType.RESIDENCE_2.getCode() == systemCodeId.intValue()) {
      return AddressType.RESIDENCE_2.getValue();
    }
    if (AddressType.WORK.getCode() == systemCodeId.intValue()) {
      return AddressType.WORK.getValue();
    }
    if (1248 == systemCodeId.intValue()) {
      return "American Sign Language";
    }
    if (1253 == systemCodeId.intValue()) {
      return "English";
    }
    return null;
  }

  @Override
  public List<IntakeLov> getAll() {
    return new ArrayList<>();
  }

}

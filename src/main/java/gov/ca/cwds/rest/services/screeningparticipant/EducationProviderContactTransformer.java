package gov.ca.cwds.rest.services.screeningparticipant;

import gov.ca.cwds.data.persistence.cms.EducationProvider;
import gov.ca.cwds.data.persistence.cms.EducationProviderContact;
import gov.ca.cwds.rest.api.domain.AddressIntakeApi;
import gov.ca.cwds.rest.api.domain.IntakeCodeCache;
import gov.ca.cwds.rest.api.domain.IntakeLovType;
import gov.ca.cwds.rest.api.domain.LegacyDescriptor;
import gov.ca.cwds.rest.api.domain.ParticipantIntakeApi;
import gov.ca.cwds.rest.api.domain.PhoneNumber;
import gov.ca.cwds.rest.api.domain.cms.LegacyTable;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import org.joda.time.DateTime;

/**
 * Business layer object to transform a {@link EducationProviderContact} to an
 * {@link ParticipantIntakeApi}
 * 
 * @author CWDS API Team
 *
 */
public class EducationProviderContactTransformer
    implements ParticipantMapper<EducationProviderContact> {

  @Override
  public ParticipantIntakeApi transform(EducationProviderContact educationProviderContact) {

    LegacyDescriptor educationProviderContactLegacyDescriptor =
        new LegacyDescriptor(educationProviderContact.getId(), null,
            new DateTime(educationProviderContact.getLastUpdatedTime()),
            LegacyTable.EDUCATION_PROVIDER_CONTACT.getName(),
            LegacyTable.EDUCATION_PROVIDER_CONTACT.getDescription());

    String firstName = educationProviderContact.getFirstName();
    String lastName = educationProviderContact.getLastName();
    String middleName = educationProviderContact.getMiddleName();
    String suffixTitle = educationProviderContact.getNameSuffix();
    String ssn = educationProviderContact.getSsn();
    String sensitivityIndicator = educationProviderContact.getSensitivityIndicator() != null
        ? educationProviderContact.getSensitivityIndicator()
        : "";

    EducationProvider educationProvider = educationProviderContact.getEducationProvider();
    String streetAddress =
        educationProvider.getStreetNumber() + " " + educationProvider.getStreetName();
    String state = IntakeCodeCache.global().getIntakeCodeForLegacySystemCode(
        educationProvider.getStateCd(), IntakeLovType.US_STATE.getValue());

    LegacyDescriptor educationProviderLegacyDescriptor = new LegacyDescriptor(
        educationProvider.getId(), null,
        new org.joda.time.DateTime(educationProvider.getLastUpdatedTime()),
        LegacyTable.EDUCATION_PROVIDER.getName(), LegacyTable.EDUCATION_PROVIDER.getDescription());

    List<AddressIntakeApi> addresses = Collections.singletonList(
        new AddressIntakeApi(null, null, streetAddress, educationProvider.getCityName(), state,
            getZip(educationProvider), null, educationProviderLegacyDescriptor));

    List<PhoneNumber> phoneNumbers = Arrays
        .asList(new PhoneNumber(educationProviderContact.getPhoneNumber(), null));

    return new ParticipantIntakeApi(null, null, null, educationProviderContactLegacyDescriptor,
        firstName, middleName, lastName, suffixTitle, educationProviderContact.getGender(), null,
        null, null, educationProviderContact.getBirthDate(),
        educationProviderContact.getDeathDate(), new LinkedList<>(), null, null, ssn,
        new HashSet<>(), addresses, phoneNumbers, "R".equals(sensitivityIndicator),
        "S".equals(sensitivityIndicator));
  }

  private String getZip(EducationProvider educationProvider) {
    return educationProvider.getZipNumber().toString();
    /**
     * This line can be added once the referrals started accepting zip suffix
     * 
     * if (educationProvider.getZipSuffixNumber() != null) { return educationProvider.getZipNumber()
     * + "-" + educationProvider.getZipSuffixNumber(); } return zip;
     */
  }

}

package gov.ca.cwds.rest.services.screeningparticipant;

import gov.ca.cwds.rest.api.domain.PhoneNumber;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.apache.commons.lang3.ObjectUtils;
import org.joda.time.DateTime;

import gov.ca.cwds.data.legacy.cms.entity.OutOfHomePlacement;
import gov.ca.cwds.data.legacy.cms.entity.PlacementEpisode;
import gov.ca.cwds.data.legacy.cms.entity.PlacementHome;
import gov.ca.cwds.data.persistence.cms.Address;
import gov.ca.cwds.data.persistence.cms.Client;
import gov.ca.cwds.data.persistence.cms.ClientAddress;
import gov.ca.cwds.data.std.ApiAddressAware;
import gov.ca.cwds.rest.api.domain.AddressIntakeApi;
import gov.ca.cwds.rest.api.domain.IntakeCodeCache;
import gov.ca.cwds.rest.api.domain.IntakeLovType;
import gov.ca.cwds.rest.api.domain.LegacyDescriptor;
import gov.ca.cwds.rest.api.domain.cms.LegacyTable;

/**
 * Intake Address Converter transforms the legacy {@link Address} into a intake {@link
 * AddressIntakeApi}, From legacy find the recent open address residence and placement home for the
 * client.
 *
 * @author CWDS API Team
 */
public class IntakeAddressConverter extends IntakeConverter {

  public static final String PLACEMENT_HOME_INTAKE_CODE = "Placement Home";

  /**
   * @param client - client
   * @return the addressIntakeApi
   */
  public List<AddressIntakeApi> convert(Client client) {
    List<AddressIntakeApi> addresses = new ArrayList<>();
    if (client.getClientAddress() != null) {
      List<ClientAddress> clientAddressList = convertPersonalData(client);
      clientAddressList.forEach(clientAddress -> addresses.add(convertToAddress(clientAddress)));
    }
    return addresses;
  }

  /**
   * @param placementEpisodes - placementEpisodes
   * @return the client placementHome
   */
  public List<AddressIntakeApi> getPlacementHomeAddresses(
      List<PlacementEpisode> placementEpisodes) {

    List<AddressIntakeApi> addresses = new ArrayList<>();

    LocalDate now = LocalDate.now();
    placementEpisodes.stream().forEach(placementEpisode -> {
      List<OutOfHomePlacement> outOfHomePlacements = placementEpisode.getOutOfHomePlacements()
          .stream()
          .filter(
              outOfHomePlacement -> ObjectUtils.compare(outOfHomePlacement.getStartDt(), now) <= 0
                  && ObjectUtils.compare(outOfHomePlacement.getEndDt(), now, true) >= 0)
          .collect(Collectors.toList());
      processAddresses(addresses, outOfHomePlacements);
    });
    return addresses;
  }

  private void processAddresses(List<AddressIntakeApi> addresses, List<OutOfHomePlacement> outOfHomePlacements) {
    IntakePhoneConverter intakePhoneConverter = new IntakePhoneConverter();
    for (OutOfHomePlacement outOfHomePlacement : outOfHomePlacements) {
      PlacementHome placementHome = outOfHomePlacement.getPlacementHome();
      AddressIntakeApi addressIntakeApi = new AddressIntakeApi();
      addressIntakeApi
          .setStreetAddress(placementHome.getStreetNo() + " " + placementHome.getStreetNm());
      addressIntakeApi.setCity(placementHome.getCityNm());
      String state = IntakeCodeCache.global().getIntakeCodeForLegacySystemCode(
          placementHome.getStateCode(), IntakeLovType.US_STATE.getValue());
      addressIntakeApi.setState(state);
      addressIntakeApi.setZip(placementHome.getZipNo());
      addressIntakeApi.setType(PLACEMENT_HOME_INTAKE_CODE);

      LocalDateTime lastUpdateTime = placementHome.getLastUpdateTime();
      ZoneOffset zoneOffset = ZoneOffset.systemDefault().getRules().getOffset(lastUpdateTime);

      LegacyDescriptor legacyDescriptor = new LegacyDescriptor(placementHome.getIdentifier(),
          null, new DateTime(1000 * lastUpdateTime.toEpochSecond(zoneOffset)),
          LegacyTable.PLACEMENT_HOME.getName(), LegacyTable.PLACEMENT_HOME.getDescription());
      addressIntakeApi.setLegacyDescriptor(legacyDescriptor);
      addressIntakeApi.getPhoneNumbers().addAll(intakePhoneConverter.getPhones(placementHome));

      addresses.add(addressIntakeApi);
    }
  }

  private AddressIntakeApi convertToAddress(ClientAddress clientAddress) {
    Address address = clientAddress.getAddresses();
    LegacyDescriptor legacyDescriptor =
        new LegacyDescriptor(address.getId(), null, new DateTime(address.getLastUpdatedTime()),
            LegacyTable.ADDRESS.getName(), LegacyTable.ADDRESS.getDescription());
    String streetAddress = address.getStreetNumber() + " " + address.getStreetName();
    String state = IntakeCodeCache.global().getIntakeCodeForLegacySystemCode(address.getStateCd(),
        IntakeLovType.US_STATE.getValue());
    String type = IntakeCodeCache.global().getIntakeCodeForLegacySystemCode(
        clientAddress.getAddressType(), IntakeLovType.ADDRESS_TYPE.getValue());
    IntakePhoneConverter intakePhoneConverter = new IntakePhoneConverter();
    List<PhoneNumber> phones = intakePhoneConverter.getPhones(clientAddress);
    return new AddressIntakeApi(null, null, streetAddress, address.getCity(), state,
        getZip(address), type, legacyDescriptor, phones);
  }

  private String getZip(ApiAddressAware address) {
    return address.getZip();
    /**
     * This line can be added once the referrals started accepting zip suffix
     *
     * if (address.getZip4() != null) { return address.getZip() + "-" + address.getZip4(); } return
     * zip;
     */
  }
}

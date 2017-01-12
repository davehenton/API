package gov.ca.cwds.rest.validation;

import java.io.IOException;
import java.util.ArrayList;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.inject.Inject;
import com.smartystreets.api.exceptions.SmartyException;
import com.smartystreets.api.us_street.Candidate;
import com.smartystreets.api.us_street.Client;
import com.smartystreets.api.us_street.ClientBuilder;
import com.smartystreets.api.us_street.Lookup;

import gov.ca.cwds.data.validation.SmartyStreetsDao;
import gov.ca.cwds.rest.api.ApiException;
import gov.ca.cwds.rest.api.domain.ValidatedAddress;

public class SmartyStreet {
  private static final Logger LOGGER = LoggerFactory.getLogger(SmartyStreet.class);

  private SmartyStreetsDao smartyStreetsDao;

  @Inject
  public SmartyStreet() {

  }

  /**
   * @param smartyStreetsDao the smartyStreetsDao to set
   */
  @Inject
  public void setSmartyStreetsDao(SmartyStreetsDao smartyStreetsDao) {
    this.smartyStreetsDao = smartyStreetsDao;
  }

  @Inject
  public SmartyStreet(SmartyStreetsDao smartyStreetsDao) {
    this.smartyStreetsDao = smartyStreetsDao;
  }

  String streetAddress;
  String cityName;
  String stateName;
  Integer zip;
  boolean delPoint;
  Double latitude;
  Double longitude;

  public ValidatedAddress[] usStreetSingleAddress(String street, String city, String state,
      Integer zipCode) {

    ArrayList<Candidate> results = getSmartyStreetsCandidates(street, city, state, zipCode);

    ArrayList<ValidatedAddress> returnValidatedAddresses = new ArrayList<>();

    if (results.isEmpty()) {
      delPoint = false;
      longitude = null;
      latitude = null;
      ValidatedAddress address = new ValidatedAddress(streetAddress, cityName, stateName, zip,
          longitude, latitude, delPoint);
      returnValidatedAddresses.add(address);
      return returnValidatedAddresses.toArray(new ValidatedAddress[0]);
    }

    for (int i = 0; i < results.size(); i++) {

      Candidate candidate = results.get(i);

      if (("Y").equals(candidate.getAnalysis().getDpvMatchCode())
          || ("S").equals(candidate.getAnalysis().getDpvMatchCode())
          || ("D").equals(candidate.getAnalysis().getDpvMatchCode())) {
        delPoint = true;
      } else {
        delPoint = false;
      }
      longitude = candidate.getMetadata().getLongitude();
      latitude = candidate.getMetadata().getLatitude();
      streetAddress = candidate.getDeliveryLine1();
      cityName = candidate.getComponents().getCityName();
      stateName = candidate.getComponents().getState();
      zip = Integer.parseInt(candidate.getComponents().getZipCode());

      ValidatedAddress address = new ValidatedAddress(streetAddress, cityName, stateName, zip,
          longitude, latitude, delPoint);
      returnValidatedAddresses.add(address);

    }
    return returnValidatedAddresses.toArray(new ValidatedAddress[returnValidatedAddresses.size()]);
  }

  public ArrayList<Candidate> getSmartyStreetsCandidates(String street, String city, String state,
      Integer zipCode) {

    Client client =
        new ClientBuilder(smartyStreetsDao.getClientId(), smartyStreetsDao.getToken()).build();

    Lookup lookup = createSmartyStreetsLookup(street, city, state, zipCode);

    try {
      client.send(lookup);
    } catch (SmartyException e) {
      LOGGER.error("SmartyStreet error, Unable to validate the address", e);
      throw new ApiException("ERROR calling SmartyStreet - ", e);
    } catch (IOException e) {
      LOGGER.error("SmartyStreet IO error, Unable to validate the address", e);
      throw new ApiException("ERROR calling SmartyStreet - ", e);
    }

    ArrayList<Candidate> results = lookup.getResult();
    return results;
  }

  public Lookup createSmartyStreetsLookup(String street, String city, String state,
      Integer zipCode) {
    Lookup lookup = new Lookup();
    lookup.setStreet(street);
    lookup.setCity(city);
    lookup.setState(state);
    if (zipCode > 0) {
      lookup.setZipCode(Integer.toString(zipCode));
    } else {
      lookup.setZipCode("");
    }
    lookup.setMaxCandidates(smartyStreetsDao.getMaxCandidates());
    return lookup;
  }

}

package gov.ca.cwds.es.live;

import java.util.Date;

import gov.ca.cwds.data.persistence.PersistentObject;
import gov.ca.cwds.data.std.ApiMarker;
import gov.ca.cwds.data.std.ApiPersonAware;

/**
 * Empty person implementation. Useful for jobs which POJOs that return no personal information but
 * still implement {@link ApiPersonAware}.
 * 
 * @author CWDS API Team
 */
public interface ReadablePerson extends ApiPersonAware, PersistentObject, ApiMarker {

  @Override
  default Date getBirthDate() {
    return null;
  }

  @Override
  default String getFirstName() {
    return null;
  }

  @Override
  default String getGender() {
    return null;
  }

  @Override
  default String getLastName() {
    return null;
  }

  @Override
  default String getMiddleName() {
    return null;
  }

  @Override
  default String getNameSuffix() {
    return null;
  }

  @Override
  default String getSsn() {
    return null;
  }

}

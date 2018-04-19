package gov.ca.cwds.rest.api.domain.cms;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.annotation.JsonProperty;

import gov.ca.cwds.rest.api.Response;
import gov.ca.cwds.rest.services.ServiceException;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

/**
 * {@link Response} adding an id to the {@link Client}
 * 
 * @author CWDS API Team
 */
public class PostedClient extends Client {
  /**
   * Serialization version
   */
  private static final long serialVersionUID = 1L;
  @JsonProperty("id")
  private String id;

  /**
   * Constructor
   * 
   * @param client The persisted client
   * @param isExist The inbound Client
   */
  public PostedClient(gov.ca.cwds.data.persistence.cms.Client client, boolean isExist) {

    super(client, isExist);

    if (StringUtils.isBlank(client.getId())) {
      throw new ServiceException("Client ID cannot be empty");
    }

    this.id = client.getId();
  }

  /**
   * @return the id
   */
  public String getId() {
    return id;
  }

  /**
   * {@inheritDoc}
   *
   * @see java.lang.Object#hashCode()
   */
  @Override
  public int hashCode() {
    return HashCodeBuilder.reflectionHashCode(this, false);
  }

  /**
   * {@inheritDoc}
   *
   * @see java.lang.Object#equals(java.lang.Object)
   */
  @Override
  public boolean equals(Object obj) {
    return EqualsBuilder.reflectionEquals(this, obj, false);
  }

}

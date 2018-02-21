package gov.ca.cwds.rest.api.domain.auth;

import gov.ca.cwds.rest.api.Request;

public class AuthorizationCheckingRequest implements Request {

  private static final long serialVersionUID = 1L;

  private String id;

  public AuthorizationCheckingRequest(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }
}

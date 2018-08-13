package gov.ca.cwds.health;

import gov.ca.cwds.rest.services.ServiceException;

/**
 * Health check exception indicates that the given health check failed to execute.
 * 
 * @author CWDS API Team
 */
public class FerbHealthCheckException extends ServiceException {

  private static final long serialVersionUID = 1L;

  public FerbHealthCheckException() {
    super();
  }

  public FerbHealthCheckException(String message, Throwable cause, boolean enableSuppression,
      boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }

  public FerbHealthCheckException(String message, Throwable cause) {
    super(message, cause);
  }

  public FerbHealthCheckException(String message) {
    super(message);
  }

  public FerbHealthCheckException(Throwable cause) {
    super(cause);
  }

}

package gov.ca.cwds.rest.filters;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.ext.Provider;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;

import com.google.inject.Inject;

import gov.ca.cwds.rest.api.domain.DomainChef;
import gov.ca.cwds.rest.filters.RequestExecutionContext.Parameter;
import gov.ca.cwds.security.web.PerryAuthenticatingFilter;

/**
 * Store common info when an HTTP request begins. Can be merged with
 * {@link PerryAuthenticatingFilter} when ready. See Pivotal story #147865633.
 * 
 * @author CWDS API Team
 * @see RequestExecutionContextImpl
 */
@Provider
@WebFilter
public class RequestExecutionContextFilter implements Filter {

  private static final Logger LOGGER = LoggerFactory.getLogger(RequestExecutionContextFilter.class);

  private static final String CTX_USER_ID = "userId";

  /**
   * Constructor
   */
  @Inject
  public RequestExecutionContextFilter() {
    // No-op.
  }

  @Override
  public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
      throws IOException, ServletException {

    if (request instanceof HttpServletRequest) {
      final HttpServletRequest httpServletRequest = (HttpServletRequest) request;
      final HttpServletResponse httpServletResponse = (HttpServletResponse) response;
      RequestExecutionContextImpl.startRequest();

      final HttpServletRequest httpRequest = (HttpServletRequest) request;
      final String requestUri = httpRequest.getRequestURI();
      final String queryString = httpRequest.getQueryString();
      final String requestMethod = httpRequest.getMethod(); // POST, GET, PUT, etc.
      final Map<String, String[]> requestParams = httpRequest.getParameterMap();

      LOGGER.info("request context filter: uri: {}, query string: {}, method: {}, params: {}",
          requestUri, queryString, requestMethod, requestParams);

      final Date requestStartTime =
          (Date) RequestExecutionContext.instance().get(Parameter.REQUEST_START_TIME);
      final String requestStartTimeStr = DomainChef.cookTimestamp(requestStartTime);

      try {
        final String userId = RequestExecutionContext.instance().getUserId();
        LOGGER.info("user id {} started request at {}", userId, requestStartTimeStr);
        MDC.put(CTX_USER_ID, userId);

        chain.doFilter(httpServletRequest, httpServletResponse);
      } finally {
        RequestExecutionContextImpl.stopRequest(); // mark request as "done", no matter what happens
        MDC.remove(CTX_USER_ID); // remove the logging context, no matter what happens
      }
    }
  }

  @Override
  public void init(FilterConfig filterConfig) throws ServletException {
    LOGGER.info("filterConfig={}", filterConfig.getFilterName());
  }

  @Override
  public void destroy() {
    // No-op.
  }

}

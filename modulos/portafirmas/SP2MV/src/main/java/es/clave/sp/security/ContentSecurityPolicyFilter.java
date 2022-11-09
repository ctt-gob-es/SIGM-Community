package es.clave.sp.security;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This filter set CSP policies using all HTTP headers defined into W3C specification.<br/>
 *
 * Purposes :
 *
 * XSS countermeasures :
 *   1. Content Security Policy (CSP)
 *      Sample generated : X-Content-Security-Policy:default-src 'none'; object-src 'self'; style-src 'self'; img-src 'self'; xhr-src 'self'; connect-src 'self';script-src 'self'; report-uri http://node:8080/Node/cspReportHandler
 *    - X-Content-Security-Policy for backward compatibility
 *    - X-WebKit-CSP for backward compatibility
 *    - Content-Security-Policy
 *    - Report handler logging all the CSP violations
 *   2. X-XSS-Protection header
 *   3. X-Content-Type-Options: nosniff
 * Click-jacking countermeasures :
 *  X-Frame-Options header
 */
public class ContentSecurityPolicyFilter implements Filter {
    /**
     * Logger object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(ContentSecurityPolicyFilter.class.getName());

    protected SecurityResponseHeaderHelper securityResponseHeaderHelper;

    /**
     * Used to prepare (one time for all) set of CSP policies that will be applied on each HTTP response.
     *
     * @see javax.servlet.Filter#init(javax.servlet.FilterConfig)
     */
    @Override
    public void init(FilterConfig fConfig) throws ServletException {
        LOGGER.info("Init of CSP filter");
        securityResponseHeaderHelper = new SecurityResponseHeaderHelper();
    }

    /**
     * Add CSP policies on each HTTP response.
     *
     * @see javax.servlet.Filter#doFilter(javax.servlet.ServletRequest, javax.servlet.ServletResponse, javax.servlet.FilterChain)
     */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fchain) throws IOException, ServletException {
        try {
        	String path = ((HttpServletRequest) request).getRequestURI();
        	if (path != null && path.endsWith("ServiceProvider")) {
        		fchain.doFilter(request, response);
        	} else {
        		HttpServletRequest httpRequest = (HttpServletRequest) request;
        		ExtendedServletResponseWrapper httpResponse = new ExtendedServletResponseWrapper((HttpServletResponse)response);
        		LOGGER.trace("ContentSecurityPolicy FILTER for " + httpRequest.getServletPath());
        		securityResponseHeaderHelper.populateResponseHeader(httpRequest, httpResponse);
        		fchain.doFilter(httpRequest, httpResponse);
        	}
        } catch(Exception e){
            LOGGER.info("ERROR : ", e.getMessage());
            LOGGER.debug("ERROR : ", e);
            throw new ServletException(e);
        }
    }

    @Override
    public void destroy() {
        LOGGER.info("Destroy of CSP filter");
    }
}

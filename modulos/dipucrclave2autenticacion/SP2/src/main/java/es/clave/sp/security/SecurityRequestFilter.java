package es.clave.sp.security;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.owasp.esapi.StringUtilities;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;

import es.clave.sp.ApplicationContextProvider;
import es.clave.sp.SessionHolder;
import eu.eidas.auth.commons.EIDASValues;
import eu.eidas.auth.commons.EidasErrorKey;
import eu.eidas.auth.commons.EidasErrors;
import eu.eidas.auth.commons.exceptions.SecurityEIDASException;

public class SecurityRequestFilter  extends AbstractSecurityRequest implements Filter {

	/**
	 * Logger object.
	 */
	private static final Logger LOG = LoggerFactory.getLogger(SecurityRequestFilter.class.getName());
	/**
	 * Configured on the web.xml
	 * Servlets to which apply this filter
	 * Its a kind of interceptor as how was with struts
	 */
	private String includedServlets;

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		LOG.info("Init of SecurityRequestFilter filter");
		this.includedServlets = filterConfig.getInitParameter("includedServlets");
	}

	@Override
	public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
		LOG.trace("Execution Of filter");
		try {
			servletRequest.setCharacterEncoding("UTF-8");
			final HttpServletRequest request = (HttpServletRequest) servletRequest;
			ApplicationContext context = ApplicationContextProvider.getApplicationContext();
			this.setConfigurationSecurityBean((ConfigurationSecurityBean) context.getBean("springManagedSecurityConfig"));

			// Class Name of the Action being invoked
			final String pathInvoked = StringUtils.remove(request.getServletPath(),"/");

			if (!matchIncludedServlets(pathInvoked)) {
				LOG.debug("Not filtered");
				filterChain.doFilter(servletRequest, servletResponse);
				return;
			}

			// get domain
			String domain = request.getHeader(EIDASValues.REFERER.toString());

			boolean performDomainCheck = !getConfigurationSecurityBean().getBypassValidation();

			if("cspReportHandler".equals(pathInvoked)) {
				performDomainCheck = false;
			}

			if (performDomainCheck) {
				LOG.debug("Performing domain check");
				if (domain == null) {
					LOG.info("Domain is null");
					final String errorCode = EidasErrors.get(EidasErrorKey.DOMAIN.errorCode(pathInvoked));
					final String errorMsg = EidasErrors.get(EidasErrorKey.DOMAIN.errorMessage(pathInvoked));
					throw new SecurityEIDASException(errorCode, errorMsg);
				}

				domain = domain.substring(domain.indexOf("://") + SecurityRequestFilter.THREE);
				// Validate if URL ends with "/"
				final int indexStr = domain.indexOf('/');
				if (indexStr > 0) {
					domain = domain.substring(0, indexStr);
				}
				// ***CHECK DOMAIN**/
				if (this.getConfigurationSecurityBean().getValidationMethod().equalsIgnoreCase(EIDASValues.DOMAIN.toString())) {
					this.checkDomain(domain, pathInvoked, request);
				}
				// ***CHECK IPS**/

				if (this.getConfigurationSecurityBean().getMaxRequests() != -1) {
					this.checkRequest(request.getRemoteAddr(), this.getConfigurationSecurityBean().getMaxTime(),
						this.getConfigurationSecurityBean().getMaxRequests(), pathInvoked, this.spIps);
				}
			}
			filterChain.doFilter(servletRequest, servletResponse);
		} finally {
			SessionHolder.clear();
		}
	}

	private boolean matchIncludedServlets(String url) {
		if(!StringUtilities.isEmpty(url) && !StringUtilities.isEmpty(this.includedServlets)){
			List<String> servlets = Arrays.asList(this.includedServlets.split("\\s*,\\s*"));
			if(servlets.contains(url)){
				return true;
			}
		}
		return false;
	}

	@Override
	public void destroy() {
		LOG.info("Destroy of SecurityRequestFilter filter");
	}
}

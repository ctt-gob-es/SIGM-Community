package es.clave.sp.security;

import eu.eidas.auth.commons.EidasErrorKey;
import eu.eidas.auth.commons.EidasParameterKeys;
import eu.eidas.auth.commons.EIDASValues;
import eu.eidas.auth.commons.EidasErrors;
import eu.eidas.auth.commons.exceptions.SecurityEIDASException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * Utility class used to define the markers used for logging.
 */
public class AbstractSecurityRequest {
    /**
     * Logger object.
     */
    private static final Logger LOG = LoggerFactory.getLogger(AbstractSecurityRequest.class.getName());

    /**
     * Static variable to get the number of milliseconds (seconds * MILLIS).
     */
    private static final long MILLIS = 1000L;

    /**
     * The three int value.
     */
    protected static final int THREE = 3;

    /**
     * Map containing the IP addresses of the citizens.
     */
    protected transient Map<String, List<Long>> spIps = new HashMap<String, List<Long>>();

    /**
     * Map containing the IP addresses from the Service Providers.
     */
    protected transient Map<String, List<Long>> spRequests = new HashMap<String, List<Long>>();

    //Contains the security configuration
    private ConfigurationSecurityBean configurationSecurityBean;

    /**
     * Validate if for a remote address the threshold for requests within a time
     * span has been reached.
     *
     * @param remoteAddr      The remote address of the incoming request
     * @param maxTime         The time span for receiving an amount of requests
     * @param threshold       The number of requests the same remoteAddr can issue.
     *                        within a time span.
     * @param pathInvoked The name of the class (in case of exception).
     * @param listIP          The list of allowed IP.
     * @see Map
     * @see java.util.ArrayList
     */
    protected final void checkRequest(final String remoteAddr, final int maxTime,
                              final int threshold, final String pathInvoked,
                              final Map<String, List<Long>> listIP) {
        final String errorMsg = EidasErrors.get(EidasErrorKey.REQUESTS.errorMessage(pathInvoked));
        final String errorCode = EidasErrors.get(EidasErrorKey.REQUESTS.errorCode(pathInvoked));

        synchronized (listIP) {
            // query the table for the request address
            List<Long> times = listIP.get(remoteAddr);

            // if it is new ip address
            if (times == null) {
                times = new ArrayList<Long>();
                times.add(System.currentTimeMillis());
                // add it to the table
                listIP.put(remoteAddr, times);
            } else {
                // if it is a known ip address
                final long currTime = System.currentTimeMillis();

                // if the number of requests from remoteAddr achieves the threshold
                if (times.size() + 1 > threshold) {
                    final List<Long> nTimes = new ArrayList<Long>(times);

                    // compute the time span currentTime is in milliseconds so we must multiply maxTime by 1000.
                    final long limitTime = currTime - maxTime * AbstractSecurityRequest.MILLIS;

                    // remove every time not within the previously computed time span
                    for (final long t : times) {
                        if (t < limitTime) {
                            nTimes.remove(t);
                        }
                    }
                    times = nTimes;

                    // if the number of requests from remoteAddr, is still, bigger than the threshold throw exception
                    if (times.size() + 1 > threshold) {
                        LOG.warn("Requests/Minute reached for IP: " + remoteAddr);
                        throw new SecurityEIDASException(errorCode, errorMsg);
                    }
                }
                times.add(currTime);

                // fill the table with this request
                listIP.put(remoteAddr, times);
            }
        }
    }

    /**
     * Checks if the domain is trustworthy.
     *
     * @param requestDomain   The Domain to validate.
     * @param servletClassName The Servlet Class's name that will be invoked.
     * @param request         The {@link HttpServletRequest}.
     * @see HttpServletRequest
     */
    protected final void checkDomain(final String requestDomain,
                             final String servletClassName, final HttpServletRequest request) {

        final String errorCode = EidasErrors.get(EidasErrorKey.DOMAIN.errorCode(servletClassName));
        final String errorMsg = EidasErrors.get(EidasErrorKey.DOMAIN.errorMessage(servletClassName));

        final List<String> ltrustedDomains = new ArrayList<String>(Arrays.asList(configurationSecurityBean.getTrustedDomains().split(EIDASValues.ATTRIBUTE_SEP.toString())));

        final boolean hasNoTrustedD = ltrustedDomains.size() == 1 && ltrustedDomains.contains(EIDASValues.NONE.toString());

        final boolean areAllTrustedD = ltrustedDomains.size() == 1 && ltrustedDomains.contains(EIDASValues.ALL.toString());

        if (hasNoTrustedD
                || (!ltrustedDomains.contains(requestDomain) && !areAllTrustedD)) {
            LOG.warn("Domain {} is not trusted", requestDomain);
            throw new SecurityEIDASException(errorCode, errorMsg);
        }

        // substring starts after 'http(s)://'
        final String spUrl = request.getParameter(EidasParameterKeys.SP_URL.toString());
        if (StringUtils.isNotEmpty(spUrl) && !spUrl.substring(spUrl.indexOf("://")
                + AbstractSecurityRequest.THREE).startsWith(requestDomain + '/')) {
            LOG.warn("spUrl {} does not belong to the domain : {}", spUrl, requestDomain);
            throw new SecurityEIDASException(errorCode, errorMsg);
        }
    }

    protected final ConfigurationSecurityBean getConfigurationSecurityBean() {
        return configurationSecurityBean;
    }

    public final void setConfigurationSecurityBean(ConfigurationSecurityBean configurationSecurityBean) {
        this.configurationSecurityBean = configurationSecurityBean;
    }
}

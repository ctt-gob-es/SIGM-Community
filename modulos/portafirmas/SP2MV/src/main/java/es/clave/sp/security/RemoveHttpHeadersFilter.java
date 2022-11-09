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
 * This filter remove unwanted http headers sent by the server
 * Purposes :
 *
 * Remove X-Powered-By header to avoid revealing information about software
 */
public class RemoveHttpHeadersFilter implements Filter {
    /**
     * Logger object.
     */
    private static final Logger LOGGER = LoggerFactory.getLogger(RemoveHttpHeadersFilter.class.getName());

    @Override
    public void init(FilterConfig fConfig) throws ServletException {
        LOGGER.info("Init of RemoveHttpHeadersFilter filter");
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain fchain) throws IOException, ServletException {
        try {
            HttpServletRequest httpRequest = (HttpServletRequest) request;
            HttpServletResponse httpResponse = (HttpServletResponse) response;
            LOGGER.trace("RemoveHttpHeadersFilter FILTER for " + httpRequest.getServletPath());

            httpResponse.setHeader("X-Powered-By","");
            httpResponse.setHeader("Server","");

            fchain.doFilter(request, response);
        }catch(Exception e){
            LOGGER.info("ERROR : ",e.getMessage());
            LOGGER.debug("ERROR : ",e);
            throw new ServletException(e);
        }
    }

    @Override
    public void destroy() {
        LOGGER.info("Destroy of RemoveHttpHeadersFilter filter");
    }
}

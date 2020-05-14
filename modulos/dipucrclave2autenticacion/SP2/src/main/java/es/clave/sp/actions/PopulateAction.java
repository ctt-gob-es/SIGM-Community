package es.clave.sp.actions;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.clave.sp.AbstractSPServlet;
import es.clave.sp.Constants;
import es.clave.sp.SPUtil;
import eu.eidas.auth.commons.EidasParameterKeys;

/**
 *  This Action Generates a SAML Request with the data given by the user, then sends it to the selected node
 */
public class PopulateAction extends AbstractSPServlet {

    private static final long serialVersionUID = 3660074009157921579L;

    private static final Logger LOGGER = LoggerFactory.getLogger(PopulateAction.class);

    private static Properties configs;
    private static String nodeServiceUrl;
    private static String providerName;
    private static String spApplication;
    private static String returnUrl;

    private static void loadGlobalConfig() {
        configs = SPUtil.loadSPConfigs();
        nodeServiceUrl = configs.getProperty("service.url");
        providerName = configs.getProperty(Constants.PROVIDER_NAME);
        spApplication = configs.getProperty(Constants.SP_APLICATION);
        returnUrl = configs.getProperty(Constants.SP_RETURN);
    }    

	@Override
	protected Logger getLogger() {
		return LOGGER;
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
			doPost(request, response);
	}

	/**
	 * Post method
	 *
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		PopulateAction.loadGlobalConfig();
		
		request.setAttribute("providerName", providerName);
		request.setAttribute("spApplication", spApplication);
		request.setAttribute("returnUrl", returnUrl);
		request.setAttribute("nodeServiceUrl", nodeServiceUrl);
		request.setAttribute(EidasParameterKeys.BINDING.toString(), getRedirectMethod());
		RequestDispatcher dispatcher = request.getRequestDispatcher("/selectAttributes.jsp");
		dispatcher.forward(request, response);
    }

	public static String getProviderName() {
        return providerName;
    }

    public static String getReturnUrl() {
        return returnUrl;
    }   



	/**
     * Method to be used by configuration. See sp.properties -> redirect.method key.
     *  This allows to be able to function eihter in
     * EIDAS or STORK mode respectively.
     *
     * @return a redirect method
     */
    public String getRedirectMethod() {
    	String ret = "post";
    	if (configs.containsKey(Constants.REDIRECT_METHOD)) {
    		ret = configs.getProperty(Constants.REDIRECT_METHOD);
			if (ret == null || ret.trim().isEmpty() || !(ret.equals("post") || ret.equals("get"))) {
				LOGGER.error("La variable de configuración redirect.method no contiene un valor adecuado: " + ret);
				ret = "post";
			}
    	}
    	return ret;
    }
}

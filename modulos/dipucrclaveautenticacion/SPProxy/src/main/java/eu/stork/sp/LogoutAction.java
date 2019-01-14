package eu.stork.sp;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.Action;
import com.opensymphony.xwork2.ActionSupport;

import eu.stork.peps.auth.commons.PEPSUtil;
import eu.stork.peps.auth.commons.STORKLogoutRequest;
import eu.stork.peps.auth.commons.STORKLogoutResponse;
import eu.stork.peps.auth.commons.exceptions.InternalErrorPEPSException;
import eu.stork.peps.auth.engine.STORKSAMLEngine;
import eu.stork.peps.exceptions.STORKSAMLEngineException;

public class LogoutAction extends ActionSupport {

	private static final long serialVersionUID = 3660074009157921579L;

	static final Logger logger = LoggerFactory.getLogger(LogoutAction.class
			.getName());

	private String samlRequestLogout;
	
	private String samlResponseLogout;

	private static Properties configs;
	
	private String proxySelected;

	private String proxyUrl;

	private String homepage;

	/**
	 * Genera una solicitud de Logout
	 */
	public String execute() {
		
		try {
			configs = SPUtil.loadConfigs(Constants.SP_PROPERTIES);
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new ApplicationSpecificServiceException(
					"No se ha podido cargar el fichero de configuraciones", e.getMessage());
		}
		
		STORKSAMLEngine engine = STORKSAMLEngine.getInstance(Constants.SP_CONF);
		STORKLogoutRequest req = new STORKLogoutRequest();
		byte[] requestBytes;

		// set available information about SP
		req.setSpProvidedId(configs.getProperty(Constants.PROVIDER_NAME));
		req.setDestination(proxySelected + configs.getProperty(Constants.LOGOUT_CPEPS_URL));
		// set Issuer as the response url of SP so that PEPS can respond to it
		req.setIssuer(configs.getProperty(Constants.LOGOUT_RESPONSE_URL));

		try {
			STORKLogoutRequest logoutReq = engine.generateSTORKLogoutRequest(req);
			requestBytes = logoutReq.getTokenSaml();

			samlRequestLogout = PEPSUtil.encodeSAMLToken(requestBytes);
			
			proxyUrl = proxySelected + configs.getProperty(Constants.LOGOUT_CPEPS_URL);

		} catch (STORKSAMLEngineException e) {
			logger.error(e.getMessage());
			throw new ApplicationSpecificServiceException("Could not generate token for Saml Logout Request",
					e.getErrorMessage());
		}
		return Action.SUCCESS;
	}
	
	/**
	 * Valida la respuesta
	 */
	public String validateResponse() throws Exception {	
		try {
			configs = SPUtil.loadConfigs(Constants.SP_PROPERTIES);
		} catch (IOException e) {
			logger.error(e.getMessage());
			throw new ApplicationSpecificServiceException(
					"No se ha podido cargar el fichero de configuraciones", e.getMessage());
		}
		
		// Se valida la respuesta
		STORKLogoutResponse logr = processLogoutResponse(PEPSUtil.decodeSAMLToken(samlResponseLogout));
		
		setHomepage(configs.getProperty("sp.url"));
		
		// A continuación iría la logica de negocio específica del SP
		
		return "loggedout";
	}
	
	private STORKLogoutResponse processLogoutResponse(byte[] samlToken) {

		// validates SAML Token
		try {
			STORKSAMLEngine engine = STORKSAMLEngine.getInstance(Constants.SP_CONF);
			final STORKLogoutResponse logoutReq = engine.validateSTORKLogoutResponse(samlToken, null);
			return logoutReq;
		} catch (STORKSAMLEngineException e) {
			LOG.error("Error validating SAMLToken", e);
			throw new InternalErrorPEPSException("No se pudo validar la respuesta", e.getMessage());
		}
	}

	public String getProxyUrl() {
		return proxyUrl;
	}

	public void setProxyUrl(String proxyUrl) {
		this.proxyUrl = proxyUrl;
	}

	public String getSamlRequestLogout() {
		return samlRequestLogout;
	}

	public void setSamlRequestLogout(String samlRequestLogout) {
		this.samlRequestLogout = samlRequestLogout;
	}

	public String getSamlResponseLogout() {
		return samlResponseLogout;
	}

	public void setSamlResponseLogout(String samlResponseLogout) {
		this.samlResponseLogout = samlResponseLogout;
	}

	public String getProxySelected() {
		return proxySelected;
	}

	public void setProxySelected(String proxySelected) {
		this.proxySelected = proxySelected;
	}

	/**
	 * @return the homepage
	 */
	public String getHomepage() {
		return homepage;
	}

	/**
	 * @param homepage the homepage to set
	 */
	public void setHomepage(String homepage) {
		this.homepage = homepage;
	}

}

package es.clave.sp.actions;

import java.io.IOException;
import java.util.Properties;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import javax.servlet.http.HttpSession;

import org.opensaml.saml2.core.LogoutResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;

import es.clave.sp.AbstractSPServlet;
import es.clave.sp.ApplicationSpecificServiceException;
import es.clave.sp.Constants;
import es.clave.sp.SPUtil;
import es.clave.sp.SessionHolder;
import es.clave.sp.SpProtocolEngineFactory;
import eu.eidas.auth.commons.EidasErrorKey;
import eu.eidas.auth.commons.EidasErrors;
import eu.eidas.auth.commons.EidasParameterKeys;
import eu.eidas.auth.commons.EidasStringUtil;
import eu.eidas.auth.commons.attribute.AttributeDefinition;
import eu.eidas.auth.commons.attribute.AttributeValue;
import eu.eidas.auth.commons.exceptions.InvalidParameterEIDASException;
import eu.eidas.auth.commons.protocol.IAuthenticationResponseNoMetadata;
import eu.eidas.auth.engine.ProtocolEngineNoMetadataI;
import eu.eidas.auth.engine.xml.opensaml.SAMLEngineUtils;
import eu.eidas.auth.engine.xml.opensaml.SecureRandomXmlIdGenerator;
import eu.eidas.engine.exceptions.EIDASSAMLEngineException;

/**
 * This Action receives a SAML Response, shows it to the user and then validates it getting the attributes values
 */
public class ReturnAction extends AbstractSPServlet {

    private static final long serialVersionUID = 3660074009157921579L;

    private static final String SAML_VALIDATION_ERROR = "Could not validate token for Saml Response";

    static final Logger logger = LoggerFactory.getLogger(IndexAction.class.getName());
    
    private final ProtocolEngineNoMetadataI protocolEngine = 
    		SpProtocolEngineFactory.getSpProtocolEngine(Constants.SP_CONF);

    private String SAMLResponse = null;
    private String logoutResponse = null;
    private ImmutableMap<AttributeDefinition<?>, ImmutableSet<? extends AttributeValue<?>>> attrMap = null;
    private Properties configs = null;
    private static boolean proxy_sigem = false;
    private static boolean proxy_webempleado = false;
    private static boolean proxy_comparece= false;
    private static boolean proxy_webempleado_aytos= false;
    private static boolean proxy_portafirmas= false;
    private static boolean proxy_sigem_registro= false;
    private static boolean proxy_sigem_tramitador= false;

    @Override
	protected Logger getLogger() {
		return logger;
	}

    @Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		if (acceptsHttpRedirect()) {
			doPost(request, response);
		} else {
			logger.warn("BUSINESS EXCEPTION : redirect binding is not allowed");
		}
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
		SAMLResponse = request.getParameter("SAMLResponse");
		logoutResponse = request.getParameter("logoutResponse");
		String relayState = request.getParameter("RelayState");
		RequestDispatcher dispatcher = null;
		if (SAMLResponse != null && !SAMLResponse.trim().isEmpty()) {
			configs = SPUtil.loadSPConfigs();
			byte[] decSamlToken = EidasStringUtil.decodeBytesFromBase64(SAMLResponse);
			IAuthenticationResponseNoMetadata authnResponse = null;
			try {
				
				String peticion = configs.getProperty(Constants.SP_RETURN);
				
				//validate SAML Token
				authnResponse = protocolEngine.unmarshallResponseAndValidate(decSamlToken, request.getRemoteHost(), 0, 0, peticion);

				// Check session
				String prevRelayState = SessionHolder.sessionsSAML.get(authnResponse.getInResponseToId());
				if (prevRelayState == null || !prevRelayState.equals(relayState)) {
					throw new EIDASSAMLEngineException("La respuesta recibida no corresponde con ninguna "
							+ "request o no coincide el RelayState");
				}
			} catch (EIDASSAMLEngineException e) {
				logger.error(e.getMessage(), e);
				throw new ApplicationSpecificServiceException(SAML_VALIDATION_ERROR, e.getMessage());
			}

			if (authnResponse.isFailure()) {
				throw new ApplicationSpecificServiceException("Saml Response is fail", authnResponse.getStatusMessage());
			} else {
				attrMap = authnResponse.getAttributes().getAttributeMap();
			}

			
			//CARGA DE PARAMETROS PARA PASARLOS AL CONTEXTO QUE CORRESPONDA
			request.setAttribute("attrMap", attrMap);
			
			request.setAttribute("FamilyName",authnResponse.getAttributes().getAttributeValuesByFriendlyName("FamilyName"));
			request.setAttribute("FirstName",authnResponse.getAttributes().getAttributeValuesByFriendlyName("FirstName"));
			request.setAttribute("PersonIdentifier",authnResponse.getAttributes().getAttributeValuesByFriendlyName("PersonIdentifier"));
			request.setAttribute("FirstSurname",authnResponse.getAttributes().getAttributeValuesByFriendlyName("FirstSurname"));
			request.setAttribute("PartialAfirma",authnResponse.getAttributes().getAttributeValuesByFriendlyName("PartialAfirma"));
			request.setAttribute("SelectedIdP",authnResponse.getAttributes().getAttributeValuesByFriendlyName("SelectedIdP"));
			request.setAttribute("RelayState",authnResponse.getAttributes().getAttributeValuesByFriendlyName("RelayState"));			
	
			this.getServletContext().setAttribute("FamilyName",authnResponse.getAttributes().getAttributeValuesByFriendlyName("FamilyName"));
			this.getServletContext().setAttribute("FirstName",authnResponse.getAttributes().getAttributeValuesByFriendlyName("FirstName"));
			this.getServletContext().setAttribute("PersonIdentifier",authnResponse.getAttributes().getAttributeValuesByFriendlyName("PersonIdentifier"));
			this.getServletContext().setAttribute("FirstSurname",authnResponse.getAttributes().getAttributeValuesByFriendlyName("FirstSurname"));
			this.getServletContext().setAttribute("PartialAfirma",authnResponse.getAttributes().getAttributeValuesByFriendlyName("PartialAfirma"));
			this.getServletContext().setAttribute("SelectedIdP",authnResponse.getAttributes().getAttributeValuesByFriendlyName("SelectedIdP"));
			this.getServletContext().setAttribute("RelayState",authnResponse.getAttributes().getAttributeValuesByFriendlyName("RelayState"));			
			
			//LEER CONFIGURACION PARA SABER EN QUE ENTORNO ESTOY, SOLO UNO PUEDE SER TRUE			
			proxy_sigem = Boolean.valueOf(configs.getProperty(Constants.SP_PROXY_SIGEM));
			proxy_comparece = Boolean.valueOf(configs.getProperty(Constants.SP_PROXY_COMPARECE));
			proxy_webempleado = Boolean.valueOf(configs.getProperty(Constants.SP_PROXY_PORTALEMPLEADO));
			proxy_webempleado = Boolean.valueOf(configs.getProperty(Constants.SP_PROXY_PORTALEMPLEADO));
			proxy_webempleado_aytos = Boolean.valueOf(configs.getProperty(Constants.SP_PROXY_PORTALEMPLEADO));	
			proxy_portafirmas = Boolean.valueOf(configs.getProperty(Constants.SP_PROXY_PORTAFIRMAS));	
			String redirect = "";			
			
			
			//LEER LA URL DE SALTO UNA VEZ RECIVIDA LA RESPUESTA DE LA PASARELA DE CLAVE			
			if(proxy_sigem_registro)
				redirect = redirect.concat(configs.getProperty(Constants.SP_RETURN_SIGEM));
			else if(proxy_sigem_tramitador)
				redirect = redirect.concat(configs.getProperty(Constants.SP_RETURN_COMPARECE));
			else if(proxy_webempleado)
				redirect = redirect.concat(configs.getProperty(Constants.SP_RETURN_PORTALEMPLEADO));
			else if(proxy_webempleado_aytos)
				redirect = redirect.concat(configs.getProperty(Constants.SP_PROXY_PORTALEMPLEADO_AYTOS));
			else if(proxy_portafirmas)
				redirect = configs.getProperty(Constants.SP_RETURN_PORTAFIRMAS);
			
			response.setHeader("Location", redirect);						
			response.setStatus(HttpServletResponse.SC_MOVED_PERMANENTLY);			

			//logout data
			String samlId = SAMLEngineUtils.generateNCName();
			request.setAttribute("logoutRequest", generateLogout(configs.getProperty(Constants.SP_RETURN),
					configs.getProperty(Constants.PROVIDER_NAME), 
					configs.getProperty("service.url"),
					samlId));
			String nonce = SecureRandomXmlIdGenerator.INSTANCE.generateIdentifier(8);
			SessionHolder.sessionsSAML.put(nonce, samlId);
			request.setAttribute("RelayState", nonce);
			request.setAttribute("nodeServiceUrl", configs.getProperty("service.url"));
			request.setAttribute(EidasParameterKeys.BINDING.toString(), getRedirectMethod());
		} else if (logoutResponse != null && !logoutResponse.trim().isEmpty()) {
			// Se valida la request recibida del SP			
			if (logger.isDebugEnabled()) {
				logger.debug("Se procede a validar una respuesta de logout recibida desde un IdP");
				logger.debug("Respuesta a validar: " + response);
			}
			LogoutResponse logoutResp = null;
			try {
				byte[] decSamlToken = EidasStringUtil.decodeBytesFromBase64(logoutResponse);
				//validate SAML Token
				logoutResp = protocolEngine.unmarshallLogoutResponseAndValidate(decSamlToken, 
						request.getRemoteHost(), 0, 0, configs.getProperty(Constants.SP_RETURN));

			} catch (EIDASSAMLEngineException e) {
				logger.error("No se pudo validar la respuesta", e);
				throw new ApplicationSpecificServiceException(SAML_VALIDATION_ERROR, e.getMessage());
			} catch (Exception e) {
				logger.error("No se pudo validar la respuesta", e);
				throw new ApplicationSpecificServiceException(SAML_VALIDATION_ERROR, e.getMessage());
			}

			logger.info("Saml LogoutResponse is SUCCESS", logoutResp.getStatus());		
			
			// Check session
			String requestSamlId = SessionHolder.sessionsSAML.get(relayState);
			if (requestSamlId == null || !requestSamlId.equals(logoutResp.getInResponseTo())) {
				throw new InvalidParameterEIDASException("La respuesta recibida no corresponde con ninguna "
						+ "request o no coincide el RelayState");
			}
			// Se redirige al usuario
			dispatcher = request.getRequestDispatcher("/");
		} else {
			logger.error("Se ha recibido una respuesta vacia");
			throw new InvalidParameterEIDASException(
					EidasErrors.get(EidasErrorKey.COLLEAGUE_RESP_INVALID_SAML.errorCode()),
					EidasErrors.get(EidasErrorKey.COLLEAGUE_RESP_INVALID_SAML.errorMessage()));
		}		

    }
	
	private String generateLogout(String assertionConsumerUrl, String providerName, 
			String destination, String nonce) {
		try {
			final byte[] token = protocolEngine.generateLogoutRequestMessage(assertionConsumerUrl, providerName,
					destination, nonce);
			return EidasStringUtil.encodeToBase64(token);
		} catch (EIDASSAMLEngineException e) {
			logger.error(e.getMessage());
			logger.error("", e);
			throw new ApplicationSpecificServiceException("Could not generate token for Saml Request",
					e.getMessage());
		}
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
				logger.error("La variable de configuración redirect.method no contiene un valor adecuado: " + ret);
				ret = "post";
			}
    	}
    	return ret;
    }
}
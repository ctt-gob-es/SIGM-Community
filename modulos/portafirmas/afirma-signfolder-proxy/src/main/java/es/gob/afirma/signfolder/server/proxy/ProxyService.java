package es.gob.afirma.signfolder.server.proxy;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Signature;
import java.security.SignatureException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.activation.DataHandler;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.xml.bind.JAXBElement;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.SOAPFaultException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import es.gob.afirma.core.misc.AOUtil;
import es.gob.afirma.core.misc.Base64;
import es.gob.afirma.core.signers.TriphaseData;
import es.gob.afirma.signfolder.client.AuthorizationInfo;
import es.gob.afirma.signfolder.client.MobileAccesoClave;
import es.gob.afirma.signfolder.client.MobileApplication;
import es.gob.afirma.signfolder.client.MobileApplicationList;
import es.gob.afirma.signfolder.client.MobileDocSignInfo;
import es.gob.afirma.signfolder.client.MobileDocSignInfoList;
import es.gob.afirma.signfolder.client.MobileDocument;
import es.gob.afirma.signfolder.client.MobileDocumentList;
import es.gob.afirma.signfolder.client.MobileException;
import es.gob.afirma.signfolder.client.MobileFireDocument;
import es.gob.afirma.signfolder.client.MobileFireRequest;
import es.gob.afirma.signfolder.client.MobileFireRequestList;
import es.gob.afirma.signfolder.client.MobileFireTrasactionResponse;
import es.gob.afirma.signfolder.client.MobileRequest;
import es.gob.afirma.signfolder.client.MobileRequestFilter;
import es.gob.afirma.signfolder.client.MobileRequestFilterList;
import es.gob.afirma.signfolder.client.MobileRequestList;
import es.gob.afirma.signfolder.client.MobileSIMUser;
import es.gob.afirma.signfolder.client.MobileSIMUserStatus;
import es.gob.afirma.signfolder.client.MobileService;
import es.gob.afirma.signfolder.client.MobileService_Service;
import es.gob.afirma.signfolder.client.MobileSignLine;
import es.gob.afirma.signfolder.client.MobileStringList;
import es.gob.afirma.signfolder.client.RolesList;
import es.gob.afirma.signfolder.client.UserList;
import es.gob.afirma.signfolder.server.proxy.SignLine.SignLineType;
import es.gob.afirma.signfolder.server.proxy.sessions.SessionCollector;

/**
 * Servicio Web para firma trif&aacute;sica.
 * 
 * @author Tom&aacute;s Garc&iacute;a-;er&aacute;s
 */
public final class ProxyService extends HttpServlet {

	private static final long serialVersionUID = 1L;

	private static final String DEFAULT_CHARSET = "utf-8"; //$NON-NLS-1$

	private static final String SIGNATURE_SERVICE_URL = "TRIPHASE_SERVER_URL"; //$NON-NLS-1$

	private static final String PARAMETER_NAME_OPERATION = "op"; //$NON-NLS-1$
	private static final String PARAMETER_NAME_DATA = "dat"; //$NON-NLS-1$
	private static final String PARAMETER_NAME_SHARED_SESSION_ID = "ssid"; //$NON-NLS-1$

	private static final String OPERATION_PRESIGN = "0"; //$NON-NLS-1$
	private static final String OPERATION_POSTSIGN = "1"; //$NON-NLS-1$
	private static final String OPERATION_REQUEST = "2"; //$NON-NLS-1$
	private static final String OPERATION_REJECT = "3"; //$NON-NLS-1$
	private static final String OPERATION_DETAIL = "4"; //$NON-NLS-1$
	private static final String OPERATION_DOCUMENT_PREVIEW = "5"; //$NON-NLS-1$
	private static final String OPERATION_CONFIGURING = "6"; //$NON-NLS-1$
	private static final String OPERATION_APPROVE = "7"; //$NON-NLS-1$
	private static final String OPERATION_SIGN_PREVIEW = "8"; //$NON-NLS-1$
	private static final String OPERATION_REPORT_PREVIEW = "9"; //$NON-NLS-1$
	private static final String OPERATION_REQUEST_LOGIN = "10"; //$NON-NLS-1$
	private static final String OPERATION_VALIDATE_LOGIN = "11"; //$NON-NLS-1$
	private static final String OPERATION_LOGOUT = "12"; //$NON-NLS-1$
	private static final String OPERATION_REGISTER_NOTIFICATION_SYSTEM = "13"; //$NON-NLS-1$
	private static final String OPERATION_CLAVE_LOGIN = "14"; //$NON-NLS-1$
	private static final String OPERATION_FIRE_LOAD_DATA = "16"; //$NON-NLS-1$
	private static final String OPERATION_FIRE_SIGN = "17"; //$NON-NLS-1$
	private static final String OPERATION_FIND_USER_BY_ROLE = "18"; //$NON-NLS-1$
	private static final String OPERATION_FIND_USER = "19"; //$NON-NLS-1$
	private static final String OPERATION_VERIFY = "20"; //$NON-NLS-1$
	private static final String OPERATION_CREATE_ROLE = "21"; //$NON-NLS-1$
	private static final String OPERATION_CONFIGURING_NEW = "22"; //$NON-NLS-1$

	private static final String[] OPERATIONS_CREATE_SESSION = new String[] { OPERATION_REQUEST_LOGIN,
			OPERATION_CLAVE_LOGIN };

	private static final String[] OPERATIONS_WITHOUT_LOGIN = new String[] { OPERATION_REQUEST_LOGIN,
			OPERATION_VALIDATE_LOGIN, OPERATION_CLAVE_LOGIN };

	private static final String CRYPTO_PARAM_NEED_DATA = "NEED_DATA"; //$NON-NLS-1$

	private static final String DATE_TIME_FORMAT = "dd/MM/yyyy  HH:mm"; //$NON-NLS-1$

	private static final String LOGIN_SIGNATURE_ALGORITHM = "SHA256withRSA"; //$NON-NLS-1$

	private static final String PAGE_CLAVE_LOADING = "clave-loading.jsp"; //$NON-NLS-1$

	private static final String SYSTEM_PROPERTY_DEBUG = "proxy.debug"; //$NON-NLS-1$

	static final Logger LOGGER = LoggerFactory.getLogger(ProxyService.class); // :

	private static boolean DEBUG;

	private final DocumentBuilder documentBuilder;

	private final MobileService_Service mobileService;

	static {
		try {
			DEBUG = Boolean.parseBoolean(System.getProperty(SYSTEM_PROPERTY_DEBUG));
			if (DEBUG) {
				disabledSslSecurity();
			}
		} catch (final Exception e) {
			// Error al establecer las opciones de depuracion
		}
	}

	/** Construye un Servlet que sirve operaciones de firma trif&aacute;sica. */
	public ProxyService() {

		LOGGER.info("Cargamos el fichero de configuracion del Proxy"); //$NON-NLS-1$
		ConfigManager.checkInitialized();

		try {
			this.documentBuilder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
		} catch (final Exception e) {
			throw new IllegalStateException("Error interno en la configuracion del parser XML", e); //$NON-NLS-1$
		}

		try {
			this.mobileService = new MobileService_Service(ConfigManager.getSignfolderUrl());
		} catch (final Exception e) {
			throw new IllegalStateException("Error en la configuracion de la conexion con el Portafirmas web", e); //$NON-NLS-1$
		}

		// Si esta configurada la variable SIGNATURE_SERVICE_URL en el sistema,
		// se utiliza en lugar de propiedad
		// interna de la aplicacion
		try {
			final String systemSignatureServiceUrl = System.getProperty(SIGNATURE_SERVICE_URL);
			if (systemSignatureServiceUrl != null) {
				ConfigManager.setTriphaseServiceUrl(systemSignatureServiceUrl);
				LOGGER.info("Se sustituye la URL del servicio de firma por la configurada en la propiedad del sistema " //$NON-NLS-1$
						+ SIGNATURE_SERVICE_URL + " con el valor: " + systemSignatureServiceUrl); //$NON-NLS-1$
			}
		} catch (final Exception e) {
			LOGGER.warn("No se ha podido recuperar la URL del servicio de firma configurado en la variable " //$NON-NLS-1$
					+ SIGNATURE_SERVICE_URL + " del sistema: " + e); //$NON-NLS-1$
		}
	}

	private static TrustManager[] DUMMY_TRUST_MANAGER = null;
	private static HostnameVerifier HOSTNAME_VERIFIER = null;

	private static void disabledSslSecurity() {

		// Si ya esta establecido, no repetimos
		if (HOSTNAME_VERIFIER == null) {

			DUMMY_TRUST_MANAGER = new TrustManager[] { new X509TrustManager() {
				@Override
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				@Override
				public void checkClientTrusted(final X509Certificate[] certs, final String authType) {
					/* No hacemos nada */ }

				@Override
				public void checkServerTrusted(final X509Certificate[] certs, final String authType) {
					/* No hacemos nada */ }

			} };

			HOSTNAME_VERIFIER = new HostnameVerifier() {
				@Override
				public boolean verify(final String hostname, final SSLSession session) {
					return true;
				}
			};
		}

		if (HOSTNAME_VERIFIER != HttpsURLConnection.getDefaultHostnameVerifier()) {
			try {
				final SSLContext sc = SSLContext.getInstance("SSL"); //$NON-NLS-1$
				sc.init(null, DUMMY_TRUST_MANAGER, new java.security.SecureRandom());

				HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
				HttpsURLConnection.setDefaultHostnameVerifier(HOSTNAME_VERIFIER);
			} catch (final Exception e) {
				LOGGER.warn("No se pudo deshabilitar la verificacion del SSL", e); //$NON-NLS-1$
			}
		}
	}

	/**
	 * Realiza una operaci&oacute;n de firma en tres fases. Acepta los
	 * siguientes c&oacute;digos de operaci&oacute;n en el par&aacute;metro
	 * <code>op</code>:
	 * <dl>
	 * <dt>1</dt>
	 * <dd>Firma</dd>
	 * <dt>2</dt>
	 * <dd>Petici&oacute;n de solicitudes</dd>
	 * <dt>3</dt>
	 * <dd>Rechazo de solicitudes</dd>
	 * <dt>4</dt>
	 * <dd>Detalle</dd>
	 * <dt>5</dt>
	 * <dd>Previsualizaci&oacute;n</dd>
	 * </dl>
	 * 
	 * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void service(final HttpServletRequest request, final HttpServletResponse response) {

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("Peticion al proxy Portafirmas"); //$NON-NLS-1$
			final Cookie[] cookies = request.getCookies();
			LOGGER.debug("Cookies de la peticion: " + (cookies != null ? cookies.length : null)); //$NON-NLS-1$
			if (cookies != null) {
				for (final Cookie tempCookie : cookies) {
					LOGGER.debug("Cookie name: " + tempCookie.getName()); //$NON-NLS-1$
					LOGGER.debug("Cookie value: " + tempCookie.getValue()); //$NON-NLS-1$
					LOGGER.debug("--------------"); //$NON-NLS-1$
				}
			}
		}

		final Responser responser = new Responser(response);

		final String operation = request.getParameter(PARAMETER_NAME_OPERATION);
		if (operation == null) {
			LOGGER.warn("No se han proporcionado identificador de operacion"); //$NON-NLS-1$
			responser.print(ErrorManager.genError(ErrorManager.ERROR_MISSING_OPERATION_NAME, null));
			return;
		}

		final String data = request.getParameter(PARAMETER_NAME_DATA);
		if (data == null) {
			LOGGER.warn("No se han proporcionado los datos"); //$NON-NLS-1$
			responser.print(ErrorManager.genError(ErrorManager.ERROR_MISSING_DATA, null));
			return;
		}

		byte[] xml;
		try {
			xml = Base64.decode(data, true);
		} catch (final Exception ex) {
			LOGGER.warn("Los datos de entrada no estan correctamente codificados: " + ex); //$NON-NLS-1$
			return;
		}

		try {
			xml = GzipCompressorImpl.gunzip(xml);
		} catch (final IOException e) {
			LOGGER.debug("Los datos de entrada no estaban comprimidos: " + e); //$NON-NLS-1$
		}

		if (LOGGER.isDebugEnabled()) {
			LOGGER.debug("XML de la peticion:\n{}", new String(xml)); //$NON-NLS-1$
		}

		Object ret;

		try {
			String ssid = null;

			// Salvo que sea una operacion de creacion de sesion, permitimos que
			// se nos pase el ID de sesion compartida
			if (!isCreatingLoginOperation(operation)) {
				ssid = request.getParameter(PARAMETER_NAME_SHARED_SESSION_ID);
			}

			// Obtenemos la sesion que corresponda y, si requiere estar validada
			// para realizar
			// la operacion solicitada y no lo esta, se establecera un error de
			// autenticacion
			final HttpSession session = SessionCollector.getSession(request, ssid);
			if (isLoginRequieredOperation(operation) && !isValidatedSession(session)) {
				LOGGER.warn("Se ha solicitado la siguiente operacion del proxy sin estar autenticado: " + operation); //$NON-NLS-1$
				ret = ErrorManager.genError(ErrorManager.ERROR_AUTHENTICATING_REQUEST);
				if (session != null) {
					SessionCollector.removeSession(session);
				}
			} else {
				ret = processRequest(operation, xml, request, session);
			}

		} catch (final Exception e) {
			LOGGER.warn("El Portafirmas devolvio un error", e); //$NON-NLS-1$
			if (e instanceof SAXException) {
				ret = ErrorManager.genError(ErrorManager.ERROR_BAD_XML);
			} else if (e instanceof CertificateException) {
				ret = ErrorManager.genError(ErrorManager.ERROR_BAD_CERTIFICATE);
			} else if (e instanceof MobileException) {
				ret = ErrorManager.genError(ErrorManager.ERROR_COMMUNICATING_PORTAFIRMAS);
			} else if (e instanceof IOException) {
				ret = ErrorManager.genError(ErrorManager.ERROR_COMMUNICATING_PORTAFIRMAS);
			} else if (e instanceof SOAPFaultException) {
				ret = ErrorManager.genError(ErrorManager.ERROR_AUTHENTICATING_REQUEST);
			} else if (e instanceof WebServiceException) {
				ret = ErrorManager.genError(ErrorManager.ERROR_COMMUNICATING_SERVICE);
			} else {
				ret = ErrorManager.genError(ErrorManager.ERROR_UNKNOWN_ERROR);
			}
		}

		if (ret instanceof InputStream) {
			LOGGER.debug("La respuesta es un flujo de datos de salida"); //$NON-NLS-1$
			responser.write((InputStream) ret);
			try {
				((InputStream) ret).close();
			} catch (final IOException e) {
				LOGGER.warn("No se pudo cerrar el flujo de datos: " + e); //$NON-NLS-1$
			}
		} else {
			LOGGER.debug("XML de respuesta:\n{}", ret); //$NON-NLS-1$
			responser.print((String) ret);
		}
		LOGGER.debug("Fin peticion ProxyService"); //$NON-NLS-1$
	}

	/**
	 * Indica si una sesi&oacute;n con certificado local se ha validado.
	 * 
	 * @param session
	 *            Sesi&oacute;n que se desea comprobar.
	 * @return {@code true} si la sesi&oacute;n est&aacute; validada,
	 *         {@code false} si la sesi&oacute;n es nula, si se inicio con
	 *         certificado en la nube o si a&uacute;n no se ha validado.
	 */
	private static boolean isValidatedSession(final HttpSession session) {
		return session != null && Boolean.parseBoolean((String) session.getAttribute(SessionParams.VALID_SESSION));
	}

	private Object processRequest(final String operation, final byte[] xml, final HttpServletRequest request,
			final HttpSession session) throws CertificateException, SAXException, IOException, MobileException {

		Object ret;
		if (OPERATION_REQUEST_LOGIN.equals(operation)) {
			LOGGER.info("Solicitud de login"); //$NON-NLS-1$
			ret = processRequestLogin(request, session, xml);
		} else if (OPERATION_VALIDATE_LOGIN.equals(operation)) {
			LOGGER.info("Validacion de login"); //$NON-NLS-1$
			ret = processValidateLogin(session, xml);
		} else if (OPERATION_CLAVE_LOGIN.equals(operation)) {
			LOGGER.info("Solicitud de login con Cl@ve"); //$NON-NLS-1$
			ret = processRequestClaveLogin(request, session, xml);
		} else if (OPERATION_LOGOUT.equals(operation)) {
			LOGGER.info("Solicitud de logout"); //$NON-NLS-1$
			ret = processLogout(session, xml);
		} else if (OPERATION_REGISTER_NOTIFICATION_SYSTEM.equals(operation)) {
			LOGGER.info("Solicitud de registro en el sistema de notificaciones"); //$NON-NLS-1$
			ret = processNotificationRegistry(session, xml);
		} else if (OPERATION_PRESIGN.equals(operation)) {
			LOGGER.info("Solicitud de prefirma"); //$NON-NLS-1$
			ret = processPreSigns(session, xml);
		} else if (OPERATION_POSTSIGN.equals(operation)) {
			LOGGER.info("Solicitud de postfirma"); //$NON-NLS-1$
			ret = processPostSigns(session, xml);
		} else if (OPERATION_REQUEST.equals(operation)) {
			LOGGER.info("Solicitud del listado de peticiones"); //$NON-NLS-1$
			ret = processRequestsList(session, xml);
		} else if (OPERATION_REJECT.equals(operation)) {
			LOGGER.info("Solicitud de rechazo peticiones"); //$NON-NLS-1$
			ret = processRejects(session, xml);
		} else if (OPERATION_DETAIL.equals(operation)) {
			LOGGER.info("Solicitud de detalle de una peticion"); //$NON-NLS-1$
			ret = processRequestDetail(session, xml);
		} else if (OPERATION_DOCUMENT_PREVIEW.equals(operation)) {
			LOGGER.info("Solicitud de previsualizacion de un documento"); //$NON-NLS-1$
			ret = processDocumentPreview(session, xml);
		} else if (OPERATION_CONFIGURING.equals(operation)) {
			LOGGER.info("Solicitud de la configuracion"); //$NON-NLS-1$
			ret = processConfigueApp(session, xml);
		} else if (OPERATION_APPROVE.equals(operation)) {
			LOGGER.info("Solicitud de aprobacion de una peticion"); //$NON-NLS-1$
			ret = processApproveRequest(session, xml);
		} else if (OPERATION_SIGN_PREVIEW.equals(operation)) {
			LOGGER.info("Solicitud de previsualizacion de una firma"); //$NON-NLS-1$
			ret = processSignPreview(session, xml);
		} else if (OPERATION_REPORT_PREVIEW.equals(operation)) {
			LOGGER.info("Solicitud de previsualizacion de un informe de firma"); //$NON-NLS-1$
			ret = processSignReportPreview(session, xml);
		} else if (OPERATION_FIRE_LOAD_DATA.equals(operation)) {
			LOGGER.info("Solicitud de carga de datos con FIRe"); //$NON-NLS-1$
			ret = processFireLoadData(session, xml);
		} else if (OPERATION_FIRE_SIGN.equals(operation)) {
			LOGGER.info("Solicitud de firma con FIRe"); //$NON-NLS-1$
			ret = processFireSign(session, xml);
		} else if (OPERATION_FIND_USER_BY_ROLE.equals(operation)) {
			LOGGER.info("Solicitud de recuperación de usuarios autorizados/validadores."); //$NON-NLS-1$
			ret = processFindUserbyRole(session, xml);
		} else if (OPERATION_FIND_USER.equals(operation)) {
			LOGGER.info("Solicitud de recuperación de usuarios."); //$NON-NLS-1$
			ret = processFindUser(session, xml);
		} else if (OPERATION_VERIFY.equals(operation)) {
			LOGGER.info("Solicitud de validación de petición."); //$NON-NLS-1$
			ret = processVerifyPetitions(session, xml);
		} else if (OPERATION_CREATE_ROLE.equals(operation)) {
			LOGGER.info("Solicitud de creación de rol."); //$NON-NLS-1$
			ret = processCreateRole(session, xml);
		} else if (OPERATION_CONFIGURING_NEW.equals(operation)) {
			LOGGER.info("Solicitud de la configuracion"); //$NON-NLS-1$
			ret = processConfigueNewApp(session, xml);
		} else {
			LOGGER.warn("Se ha indicado un codigo de operacion no valido"); //$NON-NLS-1$
			ret = ErrorManager.genError(ErrorManager.ERROR_UNSUPPORTED_OPERATION_NAME);
		}

		return ret;
	}

	/**
	 * Indica si una operaci&oacute;n requiere que antes se haya hecho login
	 * para que sea atendida por el proxy.
	 * 
	 * @param operation
	 *            C&oacute;digo de la operaci&oacute;n que se quiere comprobar.
	 * @return {@code true} si el c&oacute;digo proporcionado no est&aacute; en
	 *         el listado de operaciones que se pueden realizar sin login,
	 *         {@code false} en caso contrario.
	 */
	private static boolean isLoginRequieredOperation(final String operation) {
		for (final String opwl : OPERATIONS_WITHOUT_LOGIN) {
			if (opwl.equals(operation)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Indica si una operaci&oacute;n es de las que crean una nueva
	 * sesi&oacute;n de usuario.
	 * 
	 * @param operation
	 *            C&oacute;digo de la operaci&oacute;n que se quiere comprobar.
	 * @return {@code true} si el c&oacute;digo proporcionado est&aacute; en el
	 *         listado de operaciones que crean una nueva sesi&oacute;n,
	 *         {@code false} en caso contrario.
	 */
	private static boolean isCreatingLoginOperation(final String operation) {
		for (final String opwl : OPERATIONS_CREATE_SESSION) {
			if (opwl.equals(operation)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Procesa una petici&oacute;n de acceso a la aplicaci&oacute;n. Como
	 * respuesta a esta petici&oacute;n, se emitir&aacute; un token para la
	 * firma por parte del cliente y posterior validaci&oacute;n de la
	 * sesi&oacute;n.
	 * 
	 * @param request
	 *            Petici&oacute;n realizada al servicio.
	 * @param xml
	 *            XML con los datos para el proceso de autenticaci&oacute;n.
	 * @return XML con el resultado a la petici&oacute;n.
	 * @throws SAXException
	 *             Cuando ocurre alg&uacute;n error al procesar los XML.
	 * @throws IOException
	 *             Cuando ocurre alg&uacute;n problema de comunicaci&oacute;n
	 *             con el servidor.
	 */
	private String processRequestLogin(final HttpServletRequest request, final HttpSession session, final byte[] xml)
			throws SAXException, IOException {

		final Document doc = this.documentBuilder.parse(new ByteArrayInputStream(xml));
		try {
			LoginRequestParser.parse(doc);
		} catch (final Exception e) {
			throw new SAXException("No se ha proporcionado una peticion de login valida", e); //$NON-NLS-1$
		}

		if (session != null) {
			SessionCollector.removeSession(session);
		}

		final HttpSession newSession = SessionCollector.createSession(request);

		final LoginRequestData loginRequestData = createLoginRequestData(newSession);

		newSession.setAttribute(SessionParams.INIT_TOKEN, loginRequestData.getData());

		// Si hay que compartir la sesion, se obtiene el ID de sesion compartida
		String sessionId = null;
		if (ConfigManager.isShareSessionEnabled() && ConfigManager.isShareSessionWithCertEnabled()) {
			sessionId = SessionCollector.createSharedSession(newSession);
		}

		return XmlResponsesFactory.createRequestLoginResponse(loginRequestData, sessionId);
	}

	/**
	 * Obtiene los datos necesarios para el login de la aplicaci&oacute;n
	 * cliente.
	 * 
	 * @param session
	 *            Sesi&oacute;n sobre la que se desea autenticar el usuario.
	 * @return Datos de inicio de sesi&oacute;n.
	 */
	private static LoginRequestData createLoginRequestData(final HttpSession session) {

		final LoginRequestData loginRequestData = new LoginRequestData(session.getId());
		// Establecemos los datos a firmar (Token)
		loginRequestData.setData(new StringBuilder().append(session.getCreationTime()).append("|") //$NON-NLS-1$
				.append(UUID.randomUUID().toString()).toString().getBytes());

		return loginRequestData;
	}

	/**
	 * Valida el acceso de la aplicaci&oacute;n al Portafirmas. Como resultado,
	 * se enviar&aacute; el DNI del usuario autenticado.
	 * 
	 * @param session
	 *            Sesi&oacute;n pendiente de validar.
	 * @param xml
	 *            XML con los datos para el proceso de autenticaci&oacute;n.
	 * @return XML con el resultado a la petici&oacute;n.
	 * @throws SAXException
	 *             Cuando ocurre alg&uacute;n error al procesar los XML.
	 * @throws IOException
	 *             Cuando ocurre alg&uacute;n problema de comunicaci&oacute;n
	 *             con el servidor.
	 */
	private String processValidateLogin(final HttpSession session, final byte[] xml) throws SAXException, IOException {

		final Document doc = this.documentBuilder.parse(new ByteArrayInputStream(xml));
		final ValidateLoginRequest loginRequest = ValidateLoginRequestParser.parse(doc);

		final ValidateLoginResult validateLoginResult = validateLoginData(session, loginRequest);

		if (validateLoginResult.isLogged()) {
			session.setAttribute(SessionParams.VALID_SESSION, Boolean.TRUE.toString());
			// Se guarda el certificado en sesion para realizar peticiones. En
			// el futuro no se enviara
			if (loginRequest.getCertificate() != null) {
				session.setAttribute(SessionParams.CERT, Base64.encode(loginRequest.getCertificate()));
			}

			session.setAttribute(SessionParams.DNI, validateLoginResult.getDni());

			session.removeAttribute(SessionParams.INIT_TOKEN);

			SessionCollector.updateSession(session);
		} else {
			SessionCollector.removeSession(session);
		}

		LOGGER.info("Devolvemos el resultado del proceso de login para la sesion " + //$NON-NLS-1$
				(session == null ? "null" : session.getId()) + ": " + //$NON-NLS-1$ //$NON-NLS-2$
				validateLoginResult.isLogged());

		return XmlResponsesFactory.createValidateLoginResponse(validateLoginResult);
	}

	private ValidateLoginResult validateLoginData(final HttpSession session, final ValidateLoginRequest loginRequest) {

		final ValidateLoginResult result = new ValidateLoginResult();
		if (session == null) {
			LOGGER.warn("No se ha realizado previamente el inicio de sesion"); //$NON-NLS-1$
			result.setError("No se ha realizado previamente el inicio de sesion"); //$NON-NLS-1$
			return result;
		}

		// Comprobamos la validez de la firma PKCS1 remitida contra el
		// certificado recibido
		// y el token que se envio originalmente, y se manda a validar el
		// certificado para
		// el inicio de sesion
		try {
			checkPkcs1(loginRequest.getPkcs1(), loginRequest.getCertificate(),
					(byte[]) session.getAttribute(SessionParams.INIT_TOKEN));
		} catch (final Exception e) {
			LOGGER.warn("La firma del token de sesion no es valida", e); //$NON-NLS-1$
			result.setError("La firma del token de sesi\u00F3n no es v\u00E1lida"); //$NON-NLS-1$
			return result;
		}

		String dni = null;
		try {
			// Validacion contra el portafirmas (que valide que
			// la firma es valida y el certificado usado se corresponde con
			// un usuario)
			dni = getService().validateUser(loginRequest.getCertificate());
		} catch (final MobileException e) {
			LOGGER.warn("Error devuelto por el servicio de validacion", e); //$NON-NLS-1$
			final String errMsg = processLoginErrorMessage(e.getMessage());
			result.setError(errMsg != null ? errMsg
					: "El usuario no dispone de cuenta en este portafirmas o utiliz\u00F3 un certificado no v\u00E1lido."); //$NON-NLS-1$
		} catch (final Exception e) {
			LOGGER.warn("Error al validar la firma del token de login", e); //$NON-NLS-1$
			result.setError("El certificado utilizado no es v\u00E1lido."); //$NON-NLS-1$
		}

		// El Portafirmas nunca devolveria un DNI nulo, pero lo comprobamos
		if (dni != null) {
			result.setDni(dni);
		} else if (result.getError() == null) {
			LOGGER.warn("No se pudo obtener el DNI del usuario del certificado indicado"); //$NON-NLS-1$
			result.setError("No se ha podido identificar al usuario"); //$NON-NLS-1$
		}

		return result;
	}

	/**
	 * Procesa el mensaje de error remitido por el Portafirmas para hacerlo
	 * legible para el usuario.
	 * 
	 * @param rawErrorMessage
	 *            Mensaje devuelto por el Portafirmas.
	 * @return Mensaje procesado o {@code null} si no hay ninguno.
	 */
	private static String processLoginErrorMessage(final String rawErrorMessage) {
		String msg = null;
		if (rawErrorMessage != null) {
			msg = rawErrorMessage;
			if (msg.indexOf(':') > -1) {
				msg = msg.substring(0, msg.indexOf(':'));
			}
			if (!msg.endsWith(".")) { //$NON-NLS-1$
				msg += "."; //$NON-NLS-1$
			}
		}
		return msg;
	}

	/**
	 * Procesa una petici&oacute;n de acceso al Portafirmas
	 * autentic&aacute;ndose con Cl@ve. Como respuesta a esta petici&oacute;n,
	 * se generar&aacute; la informaci&oacute;n de inicio de sesi&oacute;n y se
	 * remitir&aacute; una URL para la redirecci&oacute;n del usuario a la
	 * p&aacute;gina de Cl@ve para que autorice el acceso.
	 * 
	 * @param request
	 *            Petici&oacute;n realizada al servicio.
	 * @param session
	 *            Ses&iacute;n que pueda haber actualmente iniciada.
	 * @param xml
	 *            XML con los datos para el proceso de autenticaci&oacute;n.
	 * @return XML con el resultado a la petici&oacute;n.
	 * @throws SAXException
	 *             Cuando ocurre alg&uacute;n error al procesar los XML.
	 * @throws IOException
	 *             Cuando ocurre alg&uacute;n problema de comunicaci&oacute;n
	 *             con el servidor.
	 * @throws MobileException
	 *             Cuando ocurre un error al contactar con el servidor.
	 */
	private String processRequestClaveLogin(final HttpServletRequest request, final HttpSession session,
			final byte[] xml) throws SAXException, IOException, MobileException {

		final Document doc = this.documentBuilder.parse(new ByteArrayInputStream(xml));
		try {
			LoginClaveRequestParser.parse(doc);
		} catch (final Exception e) {
			throw new SAXException("No se ha proporcionado una peticion de login valida", e); //$NON-NLS-1$
		}

		// Si ya existe una sesion, la invalidamos
		if (session != null) {
			SessionCollector.removeSession(session);
		}

		// Creamos una nueva sesion
		final HttpSession newSession = SessionCollector.createSession(request);

		// Si hay que compartir la sesion, se obtiene el ID de sesion compartida
		String sessionId = null;
		if (ConfigManager.isShareSessionEnabled()) {
			sessionId = SessionCollector.createSharedSession(newSession);
		}

		final String baseUrl = getProxyBaseUrl(request);
		String resultUrl = baseUrl + "claveResultService"; //$NON-NLS-1$
		if (sessionId != null) {
			resultUrl += "?" + PARAMETER_NAME_SHARED_SESSION_ID + "=" + sessionId; //$NON-NLS-1$ //$NON-NLS-2$
		}

		// Conectamos con el Portafirmas web
		final MobileAccesoClave claveResponse = getService().solicitudAccesoClave(resultUrl, resultUrl);

		if (claveResponse.getClaveServiceUrl() == null) {
			SessionCollector.removeSession(newSession);
			throw new IOException("No se recupero la URL de redireccion para el inicio de sesion en Cl@ve"); //$NON-NLS-1$
		}
		newSession.setAttribute(SessionParams.CLAVE_URL, claveResponse.getClaveServiceUrl());

		if (claveResponse.getSamlRequest() == null) {
			SessionCollector.removeSession(newSession);
			throw new IOException("No se recupero el token SAML para el inicio de sesion en Cl@ve"); //$NON-NLS-1$
		}
		newSession.setAttribute(SessionParams.CLAVE_REQUEST_TOKEN, claveResponse.getSamlRequest());

		if (claveResponse.getExcludedIdPList() != null) {
			newSession.setAttribute(SessionParams.CLAVE_EXCLUDED_IDPS, claveResponse.getExcludedIdPList());
		}
		if (claveResponse.getForcedIdP() != null) {
			newSession.setAttribute(SessionParams.CLAVE_FORCED_IDP, claveResponse.getForcedIdP());
		}

		// Damos por iniciado el proceso de login con Clave
		newSession.setAttribute(SessionParams.INIT_WITH_CLAVE, Boolean.TRUE);

		// Generamos un identificador de inicio para comprobar mas adelante
		final String authenticationId = generateAuthenticationId();
		newSession.setAttribute(SessionParams.CLAVE_AUTHENTICATION_ID, authenticationId);

		// Se actualiza la sesion compatida con los nuevos datos asignados
		SessionCollector.updateSession(newSession);

		// Obtenemos la URL de redireccion
		String redirectionUrl = baseUrl + PAGE_CLAVE_LOADING;
		if (sessionId != null) {
			redirectionUrl += "?" + PARAMETER_NAME_SHARED_SESSION_ID + "=" + sessionId; //$NON-NLS-1$ //$NON-NLS-2$
		}

		return XmlResponsesFactory.createRequestClaveLoginResponse(redirectionUrl, sessionId);
	}

	/**
	 * Proporciona la URL base de las p&aacute;ginas y servicios del proxy.
	 * 
	 * @param request
	 *            Petici&oacute;n realizada.
	 * @return URL base del proxy terminada en '/'.
	 */
	private static String getProxyBaseUrl(final HttpServletRequest request) {
		final String baseUrl = request.getRequestURL().substring(0, request.getRequestURL().lastIndexOf("/") + 1); //$NON-NLS-1$
		return ConfigManager.getProxyBaseUrl() != null ? ConfigManager.getProxyBaseUrl() : baseUrl;
	}

	/**
	 * Genera un c&oacute;digo de autenticaci&oacute;n aleatorio.
	 * 
	 * @return Identificador de acceso aleatorio.
	 */
	private static String generateAuthenticationId() {
		return UUID.randomUUID().toString();
	}

	/**
	 * Procesa una petici&oacute;n de cierre de sesi&oacute;n.
	 * 
	 * @param request
	 *            Petici&oacute;n realizada al servicio.
	 * @param xml
	 *            XML con los datos para el proceso de autenticaci&oacute;n.
	 * @return XML con el resultado a la petici&oacute;n.
	 * @throws SAXException
	 *             Cuando ocurre alg&uacute;n error al procesar los XML.
	 * @throws IOException
	 *             Cuando ocurre alg&uacute;n problema de comunicaci&oacute;n
	 *             con el servidor.
	 */
	private String processLogout(final HttpSession session, final byte[] xml) throws SAXException, IOException {

		final Document doc = this.documentBuilder.parse(new ByteArrayInputStream(xml));
		try {
			LogoutRequestParser.parse(doc);
		} catch (final Exception e) {
			throw new SAXException("No se ha proporcionado una peticion de logout valida", e); //$NON-NLS-1$
		}

		if (session != null) {
			SessionCollector.removeSession(session);
		}

		return XmlResponsesFactory.createRequestLogoutResponse();
	}

	/**
	 * Comprueba que un PKCS#1 se genero en base a un certificado y sobre unos
	 * datos concretos.
	 * 
	 * @param pkcs1
	 *            Firma PKCS#1 calculada sobre el algoritmo SHA-256.
	 * @param certEncoded
	 *            Certificado electr&oacute;nico con el que se gener&oacute; el
	 *            PKCS#1.
	 * @param data
	 *            Datos que se firmaron.
	 * @throws NoSuchAlgorithmException
	 *             Cuando el algoritmo de firma no este soportado.
	 * @throws CertificateException
	 *             Cuando el certificado de la firma no sea valido o no coincida
	 *             con el indicado.
	 * @throws InvalidKeyException
	 *             Cuando el certificado no contenga una clave publica
	 *             v&aacute;lida.
	 * @throws Exception
	 *             Cuando no se puede completar la validaci&oacute;n de la
	 *             estructura.
	 */
	private static void checkPkcs1(final byte[] pkcs1, final byte[] certEncoded, final byte[] data)
			throws SignatureException, NoSuchAlgorithmException, CertificateException, InvalidKeyException {

		final Certificate cert = CertificateFactory.getInstance("X.509").generateCertificate( //$NON-NLS-1$
				new ByteArrayInputStream(certEncoded));

		final Signature signer = Signature.getInstance(LOGIN_SIGNATURE_ALGORITHM);
		signer.initVerify(cert.getPublicKey());
		signer.update(data);

		if (!signer.verify(pkcs1)) {
			throw new SignatureException(
					"La firma no se realizo sobre el token proporcionado o con el certificado indicado"); //$NON-NLS-1$
		}
	}

	private String processNotificationRegistry(final HttpSession session, final byte[] xml)
			throws SAXException, IOException, MobileException {

		final Document xmlDoc = this.documentBuilder.parse(new ByteArrayInputStream(xml));
		final NotificationRegistry registry = NotificationRegistryParser.parse(xmlDoc);

		final String dni = (String) session.getAttribute(SessionParams.DNI);
		final NotificationRegistryResult result = doNotificationRegistry(dni, registry);

		return XmlResponsesFactory.createNotificationRegistryResponse(result);
	}

	private NotificationRegistryResult doNotificationRegistry(final String dni, final NotificationRegistry registry)
			throws MobileException {

		final MobileSIMUser user = new MobileSIMUser();
		user.setIdDispositivo(registry.getDeviceId()); // Identificador unico
														// del dispositivo
		user.setPlataforma(registry.getPlatform()); // Plataforma de
													// notificacion ("GCM" para
													// Android y "APNS" para
													// iOS)
		user.setIdRegistro(registry.getIdRegistry()); // Token de registro

		LOGGER.info("Registro de dispositivo: \nDispositivo: {}\nPlataforma: {}\nToken de registro: {}", //$NON-NLS-1$
				user.getIdDispositivo(), user.getPlataforma(), user.getIdRegistro());

		final MobileSIMUserStatus status = getService().registerSIMUser(dni.getBytes(), user);

		LOGGER.info("Resultado del registro:\nResultado: {}\nTexto: {}\nDetalle: {}", //$NON-NLS-1$
				status.getDetails(), status.getStatusCode(), status.getStatusText(), status.getDetails());

		final NotificationRegistryResult result = new NotificationRegistryResult(status.getStatusCode(),
				status.getStatusText());
		if (!result.isRegistered()) {
			result.setErrorDetails(status.getDetails());
		}
		return result;
	}

	/**
	 * Procesa las peticiones de prefirma. Se realiza la prefirma de cada uno de
	 * los documentos de las peticiones indicadas. Si se produce alg&uacute;n
	 * error al procesar un documento de alguna de las peticiones, se establece
	 * como incorrecta la petici&oacute;n al completo.
	 * 
	 * @param xml
	 *            XML con los datos para el proceso de las prefirmas.
	 * @return XML con el resultado a la petici&oacute;n de prefirma.
	 * @throws SAXException
	 *             Cuando ocurre alg&uacute;n error al procesar los XML.
	 * @throws IOException
	 *             Cuando ocurre alg&uacute;n problema de comunicaci&oacute;n
	 *             con el servidor.
	 * @throws CertificateException
	 *             Cuando ocurre alg&uacute;n problema con el certificado de
	 *             firma.
	 */
	private String processPreSigns(final HttpSession session, final byte[] xml)
			throws SAXException, IOException, CertificateException {

		// Cargamos los datos trifasicos
		final Document xmlDoc = this.documentBuilder.parse(new ByteArrayInputStream(xml));
		final TriphaseRequestBean triRequests = SignRequestsParser.parse(xmlDoc,
				Base64.decode((String) session.getAttribute(SessionParams.CERT)));

		// Prefirmamos
		preSign(triRequests);

		// Generamos la respuesta
		return XmlResponsesFactory.createPresignResponse(triRequests);
	}

	/**
	 * Procesa las peticiones de postfirma. Se realiza la postfirma de cada uno
	 * de los documentos de las peticiones indicadas. Si se produce alg&uacute;n
	 * error al procesar un documento de alguna de las peticiones, se establece
	 * como incorrecta la petici&oacute;n al completo.
	 * 
	 * @param xml
	 *            XML con los datos para el proceso de las prefirmas.
	 * @return XML con el resultado a la petici&oacute;n de prefirma.
	 * @throws SAXException
	 *             Cuando ocurre alg&uacute;n error al procesar los XML.
	 * @throws IOException
	 *             Cuando ocurre alg&uacute;n problema de comunicaci&oacute;n
	 *             con el servidor.
	 * @throws CertificateException
	 *             Cuando ocurre alg&uacute;n problema con el certificado de
	 *             firma.
	 */
	private String processPostSigns(final HttpSession session, final byte[] xml)
			throws SAXException, IOException, CertificateException {

		final Document xmlDoc = this.documentBuilder.parse(new ByteArrayInputStream(xml));

		final byte[] cer = Base64.decode((String) session.getAttribute(SessionParams.CERT));
		final TriphaseRequestBean triRequests = SignRequestsParser.parse(xmlDoc, cer);

		// Ejecutamos las postfirmas y se registran las firmas en el servidor
		postSign(triRequests);

		// Generamos la respuesta
		return XmlResponsesFactory.createPostsignResponse(triRequests);
	}

	/**
	 * Transforma una peticion de tipo TriphaseRequest en un
	 * MobileDocSignInfoList.
	 * 
	 * @param req
	 *            Petici&oacute;n de firma con el resultado asociado a cada
	 *            documento.
	 * @return Listado de firmas de documentos.
	 */
	private static MobileDocSignInfoList transformToWsParams(final TriphaseRequest req) {

		final MobileDocSignInfoList signInfoList = new MobileDocSignInfoList();
		final List<MobileDocSignInfo> list = signInfoList.getMobileDocSignInfo();

		MobileDocSignInfo signInfo;
		for (final TriphaseSignDocumentRequest docReq : req) {
			signInfo = new MobileDocSignInfo();
			signInfo.setDocumentId(docReq.getId());
			signInfo.setSignFormat(docReq.getSignatureFormat());
			signInfo.setSignature(new DataHandler(new ByteArrayDataSource(docReq.getResult(), null)));
			list.add(signInfo);
		}

		return signInfoList;
	}

	/**
	 * Procesa la petici&oacute;n de un listado de peticiones de firma.
	 * 
	 * @param xml
	 *            XML con la solicitud.
	 * @return XML con la respuesta a la petici&oacute;n.
	 * @throws SAXException
	 *             Cuando ocurre alg&uacute;n error al procesar los XML.
	 * @throws IOException
	 *             Cuando ocurre alg&uacute;n errlr al leer el XML.
	 * @throws MobileException
	 *             Cuando ocurre un error al contactar con el servidor.
	 */
	private String processRequestsList(final HttpSession session, final byte[] xml)
			throws SAXException, IOException, MobileException {

		final Document doc = this.documentBuilder.parse(new ByteArrayInputStream(xml));
		final ListRequest listRequest = ListRequestParser.parse(doc);

		final String dni = (String) session.getAttribute(SessionParams.DNI);
		final PartialSignRequestsList signRequests = getRequestsList(dni, listRequest);

		return XmlResponsesFactory.createRequestsListResponse(signRequests);
	}

	/**
	 * Recupera un listado de peticiones del Portafirmas a partir de la
	 * solicitud proporcionada.
	 * 
	 * @param dni
	 *            DNI del usuario.
	 * @param listRequest
	 *            Solicitud de peticiones de firma.
	 * @return Listado de peticiones.
	 * @throws MobileException
	 *             Cuando ocurre un error al contactar con el Portafirmas.
	 */
	private PartialSignRequestsList getRequestsList(final String dni, final ListRequest listRequest)
			throws MobileException {

		// Listado de formatos de firma soportados
		final MobileStringList formatsList = new MobileStringList();
		for (final String supportedFormat : listRequest.getFormats()) {
			formatsList.getStr().add(supportedFormat);
		}

		// Listado de filtros para la consulta
		final MobileRequestFilterList filterList = new MobileRequestFilterList();
		if (listRequest.getFilters() != null) {
			for (final String filterKey : listRequest.getFilters().keySet()
					.toArray(new String[listRequest.getFilters().size()])) {
				final MobileRequestFilter filter = new MobileRequestFilter();
				filter.setKey(filterKey);
				filter.setValue(listRequest.getFilters().get(filterKey));
				filterList.getRequestFilter().add(filter);
			}
		}

		// Solicitud de lista de peticiones
		final MobileRequestList mobileRequestsList = getService().queryRequestList(dni.getBytes(),
				listRequest.getState(), Integer.toString(listRequest.getNumPage()),
				Integer.toString(listRequest.getPageSize()), formatsList, filterList);

		final SimpleDateFormat dateFormater = new SimpleDateFormat("dd/MM/yyyy"); //$NON-NLS-1$
		final List<SignRequest> signRequests = new ArrayList<>(mobileRequestsList.getSize().intValue());
		for (final MobileRequest request : mobileRequestsList.getRequest()) {

			final List<MobileDocument> docList = request.getDocumentList() != null
					? request.getDocumentList().getDocument() : new ArrayList<MobileDocument>();

			final SignRequestDocument[] docs = new SignRequestDocument[docList.size()];

			try {
				for (int j = 0; j < docs.length; j++) {
					final MobileDocument doc = docList.get(j);

					docs[j] = new SignRequestDocument(doc.getIdentifier(), doc.getName(), doc.getSize().getValue(),
							doc.getMime(), doc.getOperationType(), doc.getSignatureType().getValue().value(),
							doc.getSignAlgorithm().getValue(),
							prepareSignatureParamenters(doc.getSignatureParameters()));
				}
			} catch (final Exception e) {
				final String id = request.getIdentifier() != null ? request.getIdentifier().getValue() : "null"; //$NON-NLS-1$
				LOGGER.warn("Se ha encontrado un error al analizar los datos de los documentos de la peticion con ID '" //$NON-NLS-1$
						+ id + "' y no se mostrara: " + e.toString()); //$NON-NLS-1$
				continue;
			}

			signRequests
					.add(new SignRequest(request.getRequestTagId(), request.getSubject().getValue(),
							request.getSenders().getStr().get(0), request.getApplication().getValue(),request.getView(),
							dateFormater.format(request.getFentry().getValue().toGregorianCalendar().getTime()),
							request.getFexpiration() != null ? dateFormater
									.format(request.getFexpiration().getValue().toGregorianCalendar().getTime()) : null,
							request.getImportanceLevel().getValue(), request.getWorkflow().getValue().booleanValue(),
							request.getForward().getValue().booleanValue(), request.getRequestType(), docs));
		}

		return new PartialSignRequestsList(signRequests.toArray(new SignRequest[signRequests.size()]),
				mobileRequestsList.getSize().intValue());
	}

	private static String prepareSignatureParamenters(final JAXBElement<String> parameters) {
		if (parameters == null) {
			return null;
		}
		return parameters.getValue();
	}

	private String processRejects(final HttpSession session, final byte[] xml) throws SAXException, IOException {
		final Document doc = this.documentBuilder.parse(new ByteArrayInputStream(xml));
		final RejectRequest request = RejectsRequestParser.parse(doc);

		final String dni = (String) session.getAttribute(SessionParams.DNI);
		final RequestResult[] requestResults = doReject(dni, request);

		return XmlResponsesFactory.createRejectsResponse(requestResults);
	}

	/**
	 * Rechaza el listado de solicitudes indicado en la petici&oacute;n de
	 * rechazo.
	 * 
	 * @param dni
	 *            DNI del usuario.
	 * @param rejectRequest
	 *            Petici&oacute;n de rechazo.
	 * @return Resultado del rechazo de cada solicitud.
	 */
	private RequestResult[] doReject(final String dni, final RejectRequest rejectRequest) {

		final MobileService service = getService();
		final List<Boolean> rejectionsResults = new ArrayList<>();
		for (final String id : rejectRequest) {
			// Si devuelve cualquier texto es que la operacion ha terminado
			// correctamente. Por defecto,
			// devuelve el mismo identificador de la peticion, aunque no es
			// obligatorio
			// Si falla devuelve una excepcion.
			try {
				service.rejectRequest(dni.getBytes(), id, rejectRequest.getRejectReason());
				rejectionsResults.add(Boolean.TRUE);
			} catch (final Exception e) {
				LOGGER.warn("Error en el rechazo de la peticion " + id, e); //$NON-NLS-1$
				rejectionsResults.add(Boolean.FALSE);
			}
		}

		final RequestResult[] result = new RequestResult[rejectRequest.size()];
		for (int i = 0; i < rejectRequest.size(); i++) {
			result[i] = new RequestResult(rejectRequest.get(i), rejectionsResults.get(i).booleanValue());
		}

		return result;
	}

	private String processRequestDetail(final HttpSession session, final byte[] xml)
			throws SAXException, IOException, MobileException {
		final Document doc = this.documentBuilder.parse(new ByteArrayInputStream(xml));
		final DetailRequest detRequest = DetailRequestParser.parse(doc);

		final String dni = (String) session.getAttribute(SessionParams.DNI);
		final Detail requestDetails = getRequestDetail(dni, detRequest);

		return XmlResponsesFactory.createRequestDetailResponse(requestDetails);
	}

	/**
	 * Obtiene el detalle de un solicitud de firma a partir de una
	 * petici&oacute;n de detalle.
	 * 
	 * @param dni
	 *            DNI del usuario.
	 * @param request
	 *            Petici&oacute;n que debe realizarse.
	 * @return Detalle de la solicitud.
	 * @throws MobileException
	 *             Cuando ocurre un error al contactar con el Portafirmas.
	 */
	private Detail getRequestDetail(final String dni, final DetailRequest request) throws MobileException {

		// Solicitud de lista de peticiones
		final MobileRequest mobileRequest = getService().queryRequest(dni.getBytes(), request.getRequestId());

		// Listado de documentos de la peticion
		final List<MobileDocument> mobileDocs = mobileRequest.getDocumentList().getDocument();
		final SignRequestDocument[] docs = new SignRequestDocument[mobileDocs.size()];
		for (int i = 0; i < mobileDocs.size(); i++) {
			final MobileDocument doc = mobileDocs.get(i);
			docs[i] = new SignRequestDocument(doc.getIdentifier(), doc.getName(), doc.getSize().getValue(),
					doc.getMime(), doc.getOperationType(), doc.getSignatureType().getValue().value(),
					doc.getSignAlgorithm().getValue(), prepareSignatureParamenters(doc.getSignatureParameters()));
		}

		// Listado de adjuntos de la peticion
		final List<MobileDocument> mobileAttached = mobileRequest.getAttachList().getValue().getDocument();
		final SignRequestDocument[] attached = new SignRequestDocument[mobileAttached.size()];
		for (int i = 0; i < mobileAttached.size(); i++) {
			final MobileDocument att = mobileAttached.get(i);
			attached[i] = new SignRequestDocument(att.getIdentifier(), att.getName(), att.getSize().getValue(),
					att.getMime());
		}

		// Listado de remitentes de la peticion
		final List<MobileSignLine> mobileSignLines = mobileRequest.getSignLineList().getValue().getMobileSignLine();
		final SignLine[] signLines = new SignLine[mobileSignLines.size()];
		for (int i = 0; i < signLines.length; i++) {
			final List<String> lines = new ArrayList<>();
			for (final String line : mobileSignLines.get(i).getMobileSignerList().getValue().getStr()) {
				lines.add(line);
			}
			signLines[i] = new SignLine(lines.toArray(new String[lines.size()]));
			if (mobileSignLines.get(i).getType() != null) {
				signLines[i].setType(SignLineType.valueOf(mobileSignLines.get(i).getType().getValue()));
			}
		}

		final SimpleDateFormat df = new SimpleDateFormat(DATE_TIME_FORMAT);

		// Creamos el objeto de detalle
		final Detail detail = new Detail(mobileRequest.getRequestTagId());
		detail.setApp(mobileRequest.getApplication() != null ? mobileRequest.getApplication().getValue() : ""); //$NON-NLS-1$
		detail.setDate(mobileRequest.getFentry() != null
				? df.format(mobileRequest.getFentry().getValue().toGregorianCalendar().getTime()) : ""); //$NON-NLS-1$
		detail.setExpDate(mobileRequest.getFexpiration() != null
				? df.format(mobileRequest.getFexpiration().getValue().toGregorianCalendar().getTime()) : ""); //$NON-NLS-1$
		detail.setSubject(mobileRequest.getSubject().getValue());
		detail.setText(mobileRequest.getText() != null ? mobileRequest.getText().getValue() : ""); //$NON-NLS-1$
		detail.setWorkflow(mobileRequest.getWorkflow().getValue().booleanValue());
		detail.setForward(mobileRequest.getForward().getValue().booleanValue());
		detail.setPriority(mobileRequest.getImportanceLevel().getValue());
		detail.setType(mobileRequest.getRequestType());
		detail.setRef(mobileRequest.getRef() != null ? mobileRequest.getRef().getValue() : ""); //$NON-NLS-1$
		detail.setRejectReason(
				mobileRequest.getRejectedText() != null ? mobileRequest.getRejectedText().getValue() : null);
		detail.setSignLinesFlow(
				mobileRequest.isCascadeSign() ? Detail.SIGN_LINES_FLOW_CASCADE : Detail.SIGN_LINES_FLOW_PARALLEL);
		detail.setSenders(
				mobileRequest.getSenders().getStr().toArray(new String[mobileRequest.getSenders().getStr().size()]));
		detail.setDocs(docs);
		detail.setAttached(attached);
		detail.setSignLines(signLines);

		return detail;
	}

	private InputStream processDocumentPreview(final HttpSession session, final byte[] xml)
			throws SAXException, IOException, MobileException {
		final Document doc = this.documentBuilder.parse(new ByteArrayInputStream(xml));
		final PreviewRequest request = PreviewRequestParser.parse(doc);

		final String dni = (String) session.getAttribute(SessionParams.DNI);
		final DocumentData documentData = previewDocument(dni, request);

		return documentData.getDataIs();
	}

	private InputStream processSignPreview(final HttpSession session, final byte[] xml)
			throws SAXException, IOException, MobileException {
		final Document doc = this.documentBuilder.parse(new ByteArrayInputStream(xml));
		final PreviewRequest request = PreviewRequestParser.parse(doc);

		final String dni = (String) session.getAttribute(SessionParams.DNI);
		final DocumentData documentData = previewSign(dni, request);

		return documentData.getDataIs();
	}

	private InputStream processSignReportPreview(final HttpSession session, final byte[] xml)
			throws SAXException, IOException, MobileException {
		final Document doc = this.documentBuilder.parse(new ByteArrayInputStream(xml));
		final PreviewRequest request = PreviewRequestParser.parse(doc);

		final String dni = (String) session.getAttribute(SessionParams.DNI);
		final DocumentData documentData = previewSignReport(dni, request);

		return documentData.getDataIs();
	}

	/**
	 * Recupera los datos para la previsualizaci&oacute;n de un documento a
	 * partir del identificador del documento.
	 * 
	 * @param dni
	 *            DNI del usuario.
	 * @param request
	 *            Petici&oacute;n de visualizaci&oacute;n de un documento.
	 * @return Datos necesarios para la previsualizaci&oacute;n.
	 * @throws MobileException
	 *             Cuando ocurre un error al contactar con el Portafirmas.
	 * @throws IOException
	 *             Cuando no ha sido posible leer el documento.
	 */
	private DocumentData previewDocument(final String dni, final PreviewRequest request)
			throws MobileException, IOException {
		return buildDocumentData(getService().documentPreview(dni.getBytes(), request.getDocId()));
	}

	/**
	 * Recupera los datos para la descarga de una firma a partir del hash del
	 * documento firmado.
	 * 
	 * @param dni
	 *            DNI del usuario.
	 * @param request
	 *            Petici&oacute;n de visualizaci&oacute;n de un documento.
	 * @return Datos necesarios para la previsualizaci&oacute;n.
	 * @throws MobileException
	 *             Cuando ocurre un error al contactar con el Portafirmas.
	 * @throws IOException
	 *             Cuando no ha sido posible leer el documento.
	 */
	private DocumentData previewSign(final String dni, final PreviewRequest request)
			throws MobileException, IOException {
		return buildDocumentData(getService().signPreview(dni.getBytes(), request.getDocId()));
	}

	/**
	 * Recupera los datos para la visualizaci&oacute;n de un informe de firma a
	 * partir del hash del documento firmado.
	 * 
	 * @param dni
	 *            DNI del usuario.
	 * @param request
	 *            Petici&oacute;n de visualizaci&oacute;n de un documento.
	 * @return Datos necesarios para la previsualizaci&oacute;n.
	 * @throws MobileException
	 *             Cuando ocurre un error al contactar con el Portafirmas.
	 * @throws IOException
	 *             Cuando no ha sido posible leer el documento.
	 */
	private DocumentData previewSignReport(final String dni, final PreviewRequest request)
			throws MobileException, IOException {
		return buildDocumentData(getService().reportPreview(dni.getBytes(), request.getDocId()));
	}

	/**
	 * Construye un objeto documento para previsualizaci&oacute;n.
	 * 
	 * @param document
	 *            Datos del documento.
	 * @return Contenido y metadatos del documento.
	 * @throws IOException
	 *             Cuando ocurre un error en la lectura de los datos.
	 */
	private static DocumentData buildDocumentData(final MobileDocument document) throws IOException {

		final InputStream contentIs;
		final Object content = document.getData().getValue().getContent();
		if (content instanceof InputStream) {
			contentIs = (InputStream) content;
		} else if (content instanceof String) {
			contentIs = new ByteArrayInputStream(Base64.decode((String) content));
		} else {
			final String msg = "No se puede manejar el tipo de objeto devuelto por el servicio de previsualizacion de documentos: " //$NON-NLS-1$
					+ (content == null ? null : content.getClass());
			throw new IOException(msg);
		}

		return new DocumentData(document.getIdentifier(), document.getName(), document.getMime(), contentIs);
	}

	/**
	 * Procesa la petición de recuperación de la configuración de aplicación.
	 * 
	 * @param session
	 *            Sesión de usuario.
	 * @param xml
	 *            Petición recibida.
	 * @return Un XML con la configuración de la aplicación solicitada.
	 * @throws SAXException
	 *             Cuando ocurre un error de parseo de petición.
	 * @throws IOException
	 *             Cuando ocurre un error de lectura/escritura.
	 * @throws MobileException
	 *             Cuando ocurre un error durante la comunicación con
	 *             portafirmas-web.
	 */
	private String processConfigueApp(final HttpSession session, final byte[] xml)
			throws SAXException, IOException, MobileException {

		final Document doc = this.documentBuilder.parse(new ByteArrayInputStream(xml));
		final ConfigurationRequest request = ConfigurationRequestParser.parse(doc);

		final String dni = (String) session.getAttribute(SessionParams.DNI);
		final AppConfiguration appConfig = loadConfiguration(dni, request);

		return XmlResponsesFactory.createConfigurationResponse(appConfig);
	}

	/**
	 * Procesa la petición de recuperación de la configuración de aplicación.
	 * 
	 * @param session
	 *            Sesión de usuario.
	 * @param xml
	 *            Petición recibida.
	 * @return Un XML con la configuración de la aplicación solicitada.
	 * @throws SAXException
	 *             Cuando ocurre un error de parseo de petición.
	 * @throws IOException
	 *             Cuando ocurre un error de lectura/escritura.
	 * @throws MobileException
	 *             Cuando ocurre un error durante la comunicación con
	 *             portafirmas-web.
	 */
	private String processConfigueNewApp(final HttpSession session, final byte[] xml)
			throws SAXException, IOException, MobileException {

		final Document doc = this.documentBuilder.parse(new ByteArrayInputStream(xml));
		final ConfigurationRequest request = ConfigurationRequestParser.parse(doc);

		final String dni = (String) session.getAttribute(SessionParams.DNI);
		final AppConfiguration appConfig = loadConfiguration(dni, request);

		return XmlResponsesFactory.createConfigurationNewResponse(appConfig);
	}

	/**
	 * Recupera los datos de confguracion de la aplicaci&oacute;n. Hasta el
	 * momento:
	 * <ul>
	 * <li>Listado de aplicaciones.</li>
	 * </ul>
	 * 
	 * @param dni
	 *            DNI del usuario.
	 * @param request
	 *            Datos gen&eacute;ricos necesarios para la petici&oacute;n.
	 * @return Configuraci&oacute;n de la aplicaci&oacute;n.
	 * @throws MobileException
	 *             Cuando ocurre un error al contactar con el Portafirmas.
	 * @throws IOException
	 *             Cuando no ha sido posible leer el documento.
	 */
	private AppConfiguration loadConfiguration(final String dni, final ConfigurationRequest request)
			throws MobileException, IOException {

		final MobileApplicationList appList = getService().queryApplicationsMobile(dni.getBytes());

		final List<String> appIds = new ArrayList<>();
		final List<String> appNames = new ArrayList<>();
		final List<String> roles = new ArrayList<>();

		for (final MobileApplication app : appList.getApplicationList()) {
			appIds.add(app.getId());
			appNames.add(app.getName() != null ? app.getName() : app.getId());
		}
		//roles.addAll(appList.getRoles());

		return new AppConfiguration(appIds, appNames, roles);
	}

	private String processApproveRequest(final HttpSession session, final byte[] xml) throws SAXException, IOException {

		final Document xmlDoc = this.documentBuilder.parse(new ByteArrayInputStream(xml));
		final ApproveRequestList appRequests = ApproveRequestParser.parse(xmlDoc);

		final String dni = (String) session.getAttribute(SessionParams.DNI);
		final ApproveRequestList approvedList = approveRequests(dni, appRequests);

		return XmlResponsesFactory.createApproveRequestsResponse(approvedList);
	}

	/**
	 * Aprueba el listado de solicitudes indicado en la petici&oacute;n de
	 * aprobaci$oacute;n.
	 * 
	 * @param dni
	 *            DNI del usuario.
	 * @param appRequest
	 *            Petici&oacute;n de aprobaci&oacute;n.
	 * @return Resultado de la aprobaci&oacute;n de cada solicitud.
	 */
	private ApproveRequestList approveRequests(final String dni, final ApproveRequestList appRequests) {
		final MobileService service = getService();

		for (final ApproveRequest appReq : appRequests) {
			try {
				service.approveRequest(dni.getBytes(), appReq.getRequestTagId());
			} catch (final MobileException e) {
				appReq.setOk(false);
			}
		}
		return appRequests;
	}

	/**
	 * Procesa las peticiones de firma con FIRe. Se realiza la prefirma de cada
	 * uno de los documentos de las peticiones indicadas, se envian a FIRe para
	 * que realice una firma PKCS#1 y se vuelve la URL a la que este redirigio.
	 * Si se produce alg&uacute;n error al procesar un documento de alguna de
	 * las peticiones, se establece como incorrecta la petici&oacute;n al
	 * completo.
	 * 
	 * @param session
	 *            Sesi&oacute;n establecida con el portafirmas m&oacute;vil.
	 * @param xml
	 *            XML con los datos para el proceso de firma.
	 * @return XML con la URL de redirecci&oacute;n a FIRe.
	 * @throws SAXException
	 *             Cuando ocurre alg&uacute;n error al procesar los XML.
	 * @throws IOException
	 *             Cuando ocurre alg&uacute;n problema de comunicaci&oacute;n
	 *             con el servidor.
	 * @throws CertificateException
	 *             Cuando ocurre alg&uacute;n problema con el certificado de
	 *             firma.
	 */
	private String processFireLoadData(final HttpSession session, final byte[] xml)
			throws SAXException, IOException, CertificateException {

		final String dni = (String) session.getAttribute(SessionParams.DNI);

		final Document xmlDoc = this.documentBuilder.parse(new ByteArrayInputStream(xml));
		final TriphaseRequestBean triRequests = SignRequestsParser.parse(xmlDoc, null);

		final MobileStringList requestList = new MobileStringList();
		final List<String> idRequestList = requestList.getStr();
		for (final TriphaseRequest request : triRequests) {
			idRequestList.add(request.getRef());
		}

		final FireLoadDataResult loadDataResult = fireLoadData(dni, requestList);

		if (loadDataResult.isStatusOk()) {
			session.setAttribute(SessionParams.FIRE_TRID, loadDataResult.getTransactionId());
			session.setAttribute(SessionParams.FIRE_REQUESTS, idRequestList.toArray(new String[0]));
			SessionCollector.updateSession(session);
		}

		return XmlResponsesFactory.createFireLoadDataResponse(loadDataResult);
	}

	/**
	 * Realiza la carga de peticiones a firmar en FIRe.
	 * 
	 * @param dni
	 *            DNI del usuario.
	 * @param requestsRefList
	 *            Listado de referencias de las peticiones a firmar.
	 * @return
	 */
	private FireLoadDataResult fireLoadData(final String dni, final MobileStringList requestsRefList) {

		MobileFireTrasactionResponse response;
		try {
			response = getService().fireTransaction(dni.getBytes(), requestsRefList);
		} catch (final MobileException e) {
			LOGGER.warn("Error durante la carga de documentos en FIRe", e); //$NON-NLS-1$
			return new FireLoadDataResult();
		}

		final FireLoadDataResult loadDataResult = new FireLoadDataResult();
		loadDataResult.setStatusOk(true);
		loadDataResult.setTransactionId(response.getTransactionId());
		loadDataResult.setUrlRedirect(response.getUrlRedirect());

		return loadDataResult;
	}

	/**
	 * Procesa las peticiones de firma con ClaveFirma. Se realiza la prefirma de
	 * cada uno de los documentos de las peticiones indicadas, se envian a FIRe
	 * para que realice una firma PKCS#1 y se vuelve la URL a la que este
	 * redirigio. Si se produce alg&uacute;n error al procesar un documento de
	 * alguna de las peticiones, se establece como incorrecta la petici&oacute;n
	 * al completo.
	 * 
	 * @param request
	 *            Petici&oacute;n HTTP recibida.
	 * @param xml
	 *            XML con los datos para el proceso de firma.
	 * @return Resultado del proceso de firma.
	 * @throws SAXException
	 *             Cuando ocurre alg&uacute;n error al procesar los XML.
	 * @throws IOException
	 *             Cuando ocurre alg&uacute;n problema de comunicaci&oacute;n
	 *             con el servidor.
	 * @throws CertificateException
	 *             Cuando ocurre alg&uacute;n problema con el certificado de
	 *             firma.
	 */
	private String processFireSign(final HttpSession session, final byte[] xml)
			throws SAXException, IOException, CertificateException {

		final Document xmlDoc = this.documentBuilder.parse(new ByteArrayInputStream(xml));

		// Comprobamos que el XML de peticion esta bien formado
		FireSignRequestParser.parse(xmlDoc);

		final String dni = (String) session.getAttribute(SessionParams.DNI);
		final String transactionId = (String) session.getAttribute(SessionParams.FIRE_TRID);
		final String[] requestsToSign = (String[]) session.getAttribute(SessionParams.FIRE_REQUESTS);

		session.removeAttribute(SessionParams.FIRE_TRID);
		session.removeAttribute(SessionParams.FIRE_REQUESTS);

		final FireSignResult response = fireSign(dni, transactionId, requestsToSign);

		return XmlResponsesFactory.createFireSignResponse(!response.isError(), response.getErrorType());
	}

	/**
	 * Método que procesa la petición del servicio "getUserByRole".
	 * 
	 * @param session
	 *            Sesión HTTP.
	 * @param xml
	 *            Petición XML.
	 * @return la respuesta del servicio.
	 * @throws SAXException
	 *             Si algo falla.
	 * @throws IOException
	 *             Si algo falla.
	 */
	private String processFindUserbyRole(final HttpSession session, byte[] xml) throws SAXException, IOException {

		final Document xmlDoc = this.documentBuilder.parse(new ByteArrayInputStream(xml));

		// Comprobamos que el XML de petición esta bien formado.
		GetRoleRequestParser.parse(xmlDoc);

		final String dni = (String) session.getAttribute(SessionParams.DNI);
		final String role = (String) getRole(xmlDoc);
		final String pageNum = xmlDoc.getElementsByTagName("rqrolels").item(0).getAttributes().getNamedItem("pg")
				.getNodeValue();
		final String pageSize = xmlDoc.getElementsByTagName("rqrolels").item(0).getAttributes().getNamedItem("sz")
				.getNodeValue();

		if (role == null) {
			throw new IllegalArgumentException("No ha sido posible recuperar el rol de la petición");
		}

		final GetUserByRoleResult response = getUserByRole(dni, role, pageNum, pageSize);

		return XmlResponsesFactory.createGetUserByRoleResponse(response);
	}

	/**
	 * Método que obtiene el valor del rol recibido de la respuesta del
	 * portafirmas-android.
	 * 
	 * @param xmlDoc
	 *            Petición recibida.
	 * @return el rol recibido o null si no es posible recuperar el valor del
	 *         rol.
	 */
	private String getRole(Document xmlDoc) {
		String res = null;
		NodeList roleNodeList = xmlDoc.getElementsByTagName("role");
		if (roleNodeList != null && roleNodeList.getLength() == 1) {
			res = roleNodeList.item(0).getTextContent();
		}
		return res;
	}

	/**
	 * Método que realiza la operación de búsqueda de usuarios.
	 * 
	 * @param session
	 *            Sesión HTTP.
	 * @param xml
	 *            Petición XML.
	 * @return la respuesta del servicio.
	 * @throws SAXException
	 *             Si el proceso falla.
	 * @throws IOException
	 *             Si el proceso falla.
	 */
	private String processFindUser(final HttpSession session, byte[] xml) throws SAXException, IOException {

		final Document xmlDoc = this.documentBuilder.parse(new ByteArrayInputStream(xml));

		// Comprobamos que el XML de petición esta bien formado.
		GetUserRequestParser.parse(xmlDoc);

		final String dni = (String) session.getAttribute(SessionParams.DNI);
		final String pageNum = xmlDoc.getElementsByTagName("rquserls").item(0).getAttributes().getNamedItem("pg")
				.getNodeValue();
		final String pageSize = xmlDoc.getElementsByTagName("rquserls").item(0).getAttributes().getNamedItem("sz")
				.getNodeValue();

		final GetUserResult response = getUsers(dni, pageNum, pageSize);

		return XmlResponsesFactory.createGetUserResponse(response);
	}

	/**
	 * Método que recupera la lista de usuario a partir de su DNI.
	 * 
	 * @param dni
	 *            DNI del usuario a recuperar.
	 * @return la respuesta con el usuario encontrado.
	 */
	private GetUserResult getUsers(String dni, String pageNum, String pageSize) {
//		UserList response;
//		try {
//			response = getService().getUsers(dni.getBytes(), pageNum, pageSize);
//		} catch (final MobileException e) {
//			LOGGER.warn("Error durante la recuperación de usuarios", e);
//			return new GetUserResult(FireSignResult.ERROR_TYPE_COMMUNICATION);
//		}
//
//		final GetUserResult result = new GetUserResult(response.getUsers());
//
//		return result;
		return null;
	}

	/**
	 * Método que procesa la petición de validar una petición de firma.
	 * 
	 * @param sessionsesión
	 *            HTTP.
	 * @param xml
	 *            Petición XML.
	 * @return el resultado del servicio de validar una petición.
	 * @throws IOException
	 *             Si algo falla.
	 * @throws SAXException
	 *             Si algo falla.
	 */
	private String processVerifyPetitions(final HttpSession session, byte[] xml) throws SAXException, IOException {

		final Document xmlDoc = this.documentBuilder.parse(new ByteArrayInputStream(xml));

		// Comprobamos que el XML de petición esta bien formado.
		VerifyPetitionParser.parse(xmlDoc);

		// Recuperamos el DNI del validador y la lista de peticiones a validar.
		final String dni = (String) session.getAttribute(SessionParams.DNI);
		final List<String> petitionsIds = getListPetitionsIds(xmlDoc);

		final VerifyPetitionsResult response = veriyPetitions(dni, petitionsIds);

		return XmlResponsesFactory.createVerifyPetitionsResponse(response);
	}

	/**
	 * Método que realiza la llamada al servicio de validar peticiones del
	 * portafirmas-web.
	 * 
	 * @param dni
	 *            DNI del validador.
	 * @param petitionsIds
	 *            Lista de identificadores de peticiones a validar.
	 * @return un objeto de tipo GetVerifyPetitionsResult que representa la
	 *         respuesta del servicio.
	 */
	private VerifyPetitionsResult veriyPetitions(String dni, List<String> petitionsIds) {
//		VerifyPetitionsResult response;
//		try {
//			response = new VerifyPetitionsResult(getService().verifyPetitions(dni.getBytes(), petitionsIds));
//		} catch (final MobileException e) {
//			LOGGER.warn("Error durante la recuperación de usuarios", e);
//			return new VerifyPetitionsResult(FireSignResult.ERROR_TYPE_COMMUNICATION);
//		}
//		return response;
		return null;
	}

	/**
	 * Método que procesa la petición de crear un nuevo rol.
	 * 
	 * @param session
	 *            Sesión HTTP.
	 * @param xml
	 *            Petición XML.
	 * @return el resultado del servicio de creación de rol.
	 * @throws SAXException
	 *             Si algo falla.
	 * @throws IOException
	 *             Si algo falla.
	 */
	private String processCreateRole(final HttpSession session, byte[] xml) throws SAXException, IOException {
		final Document xmlDoc = this.documentBuilder.parse(new ByteArrayInputStream(xml));

		// Comprobamos que el XML de petición esta bien formado.
		CreateRoleParser.parse(xmlDoc);

		// Recuperamos los campos necesarios para realizar la llamada.
		final String dni = (String) session.getAttribute(SessionParams.DNI);
		final String userId = xmlDoc.getElementsByTagName("userId").item(0).getTextContent();
		final String selectedRole = xmlDoc.getElementsByTagName("role").item(0).getTextContent();
		final AuthorizationInfo authInfo = getAuthInfo(xmlDoc);
		final List<String> appIds = getListApps(xmlDoc);

		final CreateRoleResult response = createRole(dni, userId, selectedRole, authInfo, appIds);

		return XmlResponsesFactory.createCreationRoleResponse(response);
	}

	/**
	 * Método que realiza la llamada al servicio de creación de roles del
	 * portafirmas-web.
	 * 
	 * @param dni
	 *            DNI del usuario.
	 * @param userId
	 *            DNI del autorizado o validador.
	 * @param selectedRole
	 *            Rol seleccionado (autorizado o validador).
	 * @param authInfo
	 *            Información de la autorización.
	 * @param appIds
	 *            Lista de identificadores de aplicaciones del validador.
	 * @return un objeto de tipo CreateRoleResult que representa la respuesta
	 *         del servicio.
	 */
	private CreateRoleResult createRole(String dni, String userId, String selectedRole, AuthorizationInfo authInfo,
			List<String> appIds) {
//		CreateRoleResult response;
//		try {
//			response = new CreateRoleResult(
//					getService().createRole(dni.getBytes(), userId, selectedRole, authInfo, appIds));
//		} catch (final MobileException e) {
//			LOGGER.warn("Error durante la recuperación de usuarios", e);
//			return new CreateRoleResult(FireSignResult.ERROR_TYPE_COMMUNICATION);
//		}
//		return response;
		return null;
	}

	/**
	 * Método encargado de extraer de la petición la información asociada a la
	 * autorización.
	 * 
	 * @param xmlDoc
	 *            Documento que representa la petición XML.
	 * @return un objeto de tipo AuthorizationInfo que contiene la información
	 *         contenido en la petición sobre la autorización.
	 */
	private AuthorizationInfo getAuthInfo(Document xmlDoc) {
		AuthorizationInfo res = null;

		if (xmlDoc.getElementsByTagName("authParams").item(0) != null) {

			res = new AuthorizationInfo();

			String initDateAsString = xmlDoc.getElementsByTagName("initDate").item(0).getTextContent();
			String endDateAsString = xmlDoc.getElementsByTagName("endDate").item(0).getTextContent();
			String authType = xmlDoc.getElementsByTagName("authType").item(0).getTextContent();
			String observations = xmlDoc.getElementsByTagName("obs").item(0).getTextContent();

			Date initDate = new Date(Long.valueOf(initDateAsString));
			Date endDate = new Date(Long.valueOf(endDateAsString));

			res.setInitDate(initDate);
			res.setEndDate(endDate);
			res.setType(authType);
			res.setObservations(observations);

		}

		return res;
	}

	/**
	 * Método encargado de extraer de la petición la lista de aplicaciones
	 * asociadas a la creación del validador.
	 * 
	 * @param xmlDoc
	 *            Documento que representa la petición XML.
	 * @return una lista con los identificadores de las aplicaciones a las que
	 *         permitir el acceso al validador.
	 */
	private List<String> getListApps(Document xmlDoc) {
		List<String> res = null;

		if (xmlDoc.getElementsByTagName("apps").item(0) != null) {

			res = new ArrayList<>();
			NodeList apps = xmlDoc.getElementsByTagName("appId");

			for (int i = 0; i < apps.getLength(); i++) {
				res.add(apps.item(i).getTextContent());
			}
		}

		return res;
	}

	/**
	 * Método que recupera de la petición recibida la lista de identificadores
	 * de las aplicaciones a actualizar.
	 * 
	 * @param xmlDoc
	 *            Petición XML recibida, como objeto Document.
	 * @return la lista de identificadores de aplicaciones a actualizar.
	 */
	private List<String> getListPetitionsIds(Document xmlDoc) {
		NodeList elemList = xmlDoc.getElementsByTagName("reqs");
		if (elemList != null) {
			List<String> res = new ArrayList<>();
			Node node = null;
			NodeList childs = elemList.item(0).getChildNodes();
			for (int i = 0; i < childs.getLength(); i++) {
				node = childs.item(i);
				res.add(node.getAttributes().item(0).getNodeValue());

			}
			return res;
		}
		return null;
	}

	/**
	 * Envia a firmar las peticiones cargadas en FIRe.
	 * 
	 * @param dni
	 *            DNI del usuario.
	 * @param transactionId
	 *            Identificador de la transacci&oacute;n de FIRe.
	 * @param requestRefs
	 *            Listado de referencias de las peticiones enviadas a firmar.
	 * @return Resultado de la operaci&oacute;n de firma.
	 */
	private FireSignResult fireSign(final String dni, final String transactionId, final String[] requestRefs) {

		final MobileStringList requestList = new MobileStringList();
		final List<String> idRequestList = requestList.getStr();
		for (final String ref : requestRefs) {
			idRequestList.add(ref);
		}

		MobileFireRequestList response;
		try {
			response = getService().signFireCloud(dni.getBytes(), requestList, transactionId);
		} catch (final MobileException e) {
			LOGGER.warn("Error durante la firma de documentos con FIRe", e); //$NON-NLS-1$
			return new FireSignResult(FireSignResult.ERROR_TYPE_COMMUNICATION);
		}

		final FireSignResult result = new FireSignResult(transactionId);

		final List<MobileFireRequest> fireRequestResults = response.getMobileFireRequest();
		for (int i = 0; !result.isError() && i < fireRequestResults.size(); i++) {
			final MobileFireRequest fireRequest = fireRequestResults.get(i);
			if (fireRequest.getErrorPeticion() != null) {
				result.setErrorType(FireSignResult.ERROR_TYPE_REQUEST);
			} else {
				final List<MobileFireDocument> documentResults = fireRequest.getDocumentos()
						.getMobileFireDocumentList();
				for (int j = 0; !result.isError() && j < documentResults.size(); j++) {
					final MobileFireDocument docResult = documentResults.get(j);
					if (docResult.getError() != null) {
						result.setErrorType(FireSignResult.ERROR_TYPE_DOCUMENT);
					}
				}
			}
		}
		return result;
	}

	/**
	 * Solicita la lista de usuarios asociados a un rol para un determinado
	 * usuario.
	 * 
	 * @param dni
	 *            Identificador del usuario.
	 * @param role
	 *            Rol a usar para el filtrado de usuarios.
	 * @param pageNum
	 *            Número de página solicitada.
	 * @param pageSize
	 *            Tamaño de la página.
	 * @return Lista de usuarios asociados al usuario.
	 */
	private GetUserByRoleResult getUserByRole(String dni, String role, String pageNum, String pageSize) {
//		RolesList response;
//		try {
//			response = getService().getRoles(dni.getBytes(), role, pageNum, pageSize);
//		} catch (final MobileException e) {
//			LOGGER.warn("Error durante la recuperación de usuarios por rol", e);
//			return new GetUserByRoleResult(FireSignResult.ERROR_TYPE_COMMUNICATION);
//		}
//
//		final GetUserByRoleResult result = new GetUserByRoleResult(response.getRoles());
//
//		return result;
		return null;
	}

	/**
	 * Genera las prefirmas.
	 * 
	 * @param triRequests
	 *            Listado de datos trif&aacute;sicos que prefirmar.
	 */
	private void preSign(final TriphaseRequestBean triRequests) {

		// Prefirmamos cada uno de los documentos de cada una de las peticiones.
		// Si falla la prefirma de
		// un documento, se da por erronea la prefirma de toda la peticion
		final MobileService service = getService();
		for (final TriphaseRequest singleRequest : triRequests) {

			LOGGER.debug("Prefirma de la peticion: " + singleRequest.getRef()); //$NON-NLS-1$

			try {
				LOGGER.info("Recuperamos los documentos de la peticion " + singleRequest.getRef()); //$NON-NLS-1$
				final MobileDocumentList downloadedDocs = service
						.getDocumentsToSign(triRequests.getCertificate().getEncoded(), singleRequest.getRef());
				if (singleRequest.size() != downloadedDocs.getDocument().size()) {
					throw new Exception("No se han recuperado tantos documentos como los indicados en la peticion " //$NON-NLS-1$
							+ singleRequest.getRef());
				}

				// Prefirmamos cada documento de la peticion
				for (final TriphaseSignDocumentRequest docRequest : singleRequest) {
					// Buscamos para la prefirma el documento descargado que
					// corresponde para la peticion
					// de firma del documento actual
					for (final MobileDocument downloadedDoc : downloadedDocs.getDocument()) {
						if (downloadedDoc.getIdentifier().equals(docRequest.getId())) {

							LOGGER.debug("Procesamos documento con el id: " + downloadedDoc.getIdentifier()); //$NON-NLS-1$

							docRequest.setCryptoOperation(downloadedDoc.getOperationType());

							// Del servicio remoto obtener los parametros de
							// configuracion, tal como deben pasarse al cliente
							// Lo pasamos a base 64 URL_SAFE para que no afecten
							// al envio de datos
							final String extraParams = downloadedDoc.getSignatureParameters() != null
									? downloadedDoc.getSignatureParameters().getValue() : null;

							if (extraParams != null) {
								docRequest.setParams(Base64.encode(extraParams.getBytes(), true));
							}

							final DataHandler dataHandler = downloadedDoc.getData() != null
									? downloadedDoc.getData().getValue() : null;
							if (dataHandler == null) {
								throw new IllegalArgumentException("No se han recuperado los datos del documento"); //$NON-NLS-1$
							}
							final Object content = dataHandler.getContent();
							if (content instanceof InputStream) {
								docRequest.setContent(
										Base64.encode(AOUtil.getDataFromInputStream((InputStream) content), true));
							} else if (content instanceof String) {
								docRequest.setContent(((String) content).replace('+', '-').replace('/', '_'));
							} else {
								throw new IOException(
										"El tipo con el que se devuelve el contenido del documento no esta soportado: " //$NON-NLS-1$
												+ (content == null ? null : content.getClass()));
							}
							break;
						}
					}
					if (docRequest.getContent() == null) {
						throw new Exception("No se obtuvo el contenido del documento: " + docRequest.getId()); //$NON-NLS-1$
					}

					LOGGER.debug("Procedemos a realizar la prefirma del documento " + docRequest.getId()); //$NON-NLS-1$
					TriSigner.doPreSign(docRequest, triRequests.getCertificate(), ConfigManager.getTriphaseServiceUrl(),
							ConfigManager.getForcedExtraParams());
				}
			} catch (final Exception e) {
				LOGGER.warn("Error en la prefirma de la peticion " + //$NON-NLS-1$
						singleRequest.getRef(), e);
				singleRequest.setStatusOk(false);
				singleRequest.setThrowable(e);
			}
		}
	}

	/**
	 * Genera las postfirmas.
	 * 
	 * @param triRequests
	 *            Listado de datos trif&aacute;sicos con las prefirmas y firmas
	 *            PKCS#1.
	 * @param service
	 *            Servicio de conexion con el proxy.
	 */
	private void postSign(final TriphaseRequestBean triRequests) {

		final MobileService service = getService();

		// Postfirmamos cada uno de los documentos de cada una de las
		// peticiones. Si falla la
		// postfirma de un solo documento, se da por erronea la postfirma de
		// toda la peticion
		for (final TriphaseRequest triRequest : triRequests) {

			LOGGER.debug("Postfirma de la peticion: " + triRequest.getRef()); //$NON-NLS-1$

			// Sustituir. Algunos formatos de firma no requeriran que se vuelva
			// a descargar el
			// documento. Solo los descargaremos si es necesario para al menos
			// una de las firmas.

			// Tomamos nota de que firmas requieren el documento original
			final Set<String> requestNeedContent = new HashSet<>();
			for (final TriphaseSignDocumentRequest docRequest : triRequest) {

				final TriphaseData triData = docRequest.getPartialResult();
				if (triData.getSignsCount() > 0 && (!triData.getSign(0).getDict().containsKey(CRYPTO_PARAM_NEED_DATA)
						|| Boolean.parseBoolean(triData.getSign(0).getDict().get(CRYPTO_PARAM_NEED_DATA)))) {
					LOGGER.debug("Descargamos el documento '" + docRequest.getId() + "' para su uso en la postfirma"); //$NON-NLS-1$ //$NON-NLS-2$
					requestNeedContent.add(docRequest.getId());
				}
			}

			// Descargamos los documentos originales si los necesitamos
			MobileDocumentList downloadedDocs = null;
			if (!requestNeedContent.isEmpty()) {
				try {
					downloadedDocs = service.getDocumentsToSign(triRequests.getCertificate().getEncoded(),
							triRequest.getRef());
				} catch (final Exception ex) {
					LOGGER.warn("Ocurrio un error al descargar los documentos de la peticion " + triRequest.getRef() //$NON-NLS-1$
							+ ": " + ex); //$NON-NLS-1$
					triRequest.setStatusOk(false);
					continue;
				}
			}

			// Para cada documento, le asignamos su documento (si es necesario)
			// y lo postfirmamos
			try {
				for (final TriphaseSignDocumentRequest docRequest : triRequest) {

					// Asignamos el documento a la peticion si es necesario
					if (downloadedDocs != null && requestNeedContent.contains(docRequest.getId())) {
						// Buscamos para la postfirma el documento descargado
						// que corresponde para la peticion
						// de firma del documento actual
						for (final MobileDocument downloadedDoc : downloadedDocs.getDocument()) {
							if (downloadedDoc.getIdentifier().equals(docRequest.getId())) {
								final Object content = downloadedDoc.getData().getValue().getContent();
								if (content instanceof InputStream) {
									docRequest.setContent(
											Base64.encode(AOUtil.getDataFromInputStream((InputStream) content), true));
								} else {
									docRequest.setContent((String) content);
								}
								// Del servicio remoto obtener los parametros de
								// configuracion, tal como deben pasarse al
								// cliente
								// Lo pasamos a base 64 URL_SAFE para que no
								// afecten al envio de datos
								final String extraParams = downloadedDoc.getSignatureParameters() != null
										? downloadedDoc.getSignatureParameters().getValue() : null;
								if (extraParams != null) {
									docRequest.setParams(Base64.encode(extraParams.getBytes(), true));
								}
							}
						}
					}

					LOGGER.debug("Procedemos a realizar la postfirma del documento"); //$NON-NLS-1$
					TriSigner.doPostSign(docRequest, triRequests.getCertificate(),
							ConfigManager.getTriphaseServiceUrl(), ConfigManager.getForcedExtraParams());
				}
			} catch (final Exception ex) {
				LOGGER.warn("Ocurrio un error al postfirmar un documento", ex); //$NON-NLS-1$
				triRequest.setStatusOk(false);
				continue;
			}

			LOGGER.debug("Registramos el resultado en el portafirmas"); //$NON-NLS-1$

			// Guardamos las firmas de todos los documentos de cada peticion
			try {
				service.saveSign(triRequests.getCertificate().getEncoded(), triRequest.getRef(),
						transformToWsParams(triRequest));
			} catch (final Exception ex) {
				LOGGER.warn("Ocurrio un error al guardar la peticion de firma " + triRequest.getRef(), ex); //$NON-NLS-1$
				triRequest.setStatusOk(false);
			}
		}
	}

	/**
	 * Obtiene el servicio para realizar las peticiones al Portafirmas web. Si
	 * se tienen habilitadas las opciones de depuraci&oacute;n, se desactivan
	 * las comprobaciones SSL.
	 * 
	 * @return Servicio para la conexi&oacute;n con el Portafirmas web.
	 */
	private MobileService getService() {
		if (DEBUG) {
			disabledSslSecurity();
		}
		return this.mobileService.getMobileServicePort();
	}

	/**
	 * Permite enviar respuestas al cliente de un servicio.
	 */
	private final class Responser {

		final HttpServletResponse response;

		/**
		 * Crea el Responser enlaz&aacute;ndolo con una petici&oacute;n concreta
		 * al servicio.
		 * 
		 * @param response
		 *            Manejador para el env&iacute;o de la respuesta.
		 */
		public Responser(final HttpServletResponse response) {
			this.response = response;
		}

		/**
		 * Imprime una respuesta en la salida del servicio y cierra el flujo.
		 * 
		 * @param message
		 *            Mensaje que imprimir como respuesta.
		 */
		public void print(final String message) {
			try (final OutputStream os = this.response.getOutputStream();) {
				os.write(message.getBytes(Charset.forName(DEFAULT_CHARSET)));
			} catch (final Exception e) {
				LOGGER.error("Error al devolver el resultado al cliente a traves del metodo print", e); //$NON-NLS-1$
			}
		}

		public void write(final InputStream message) {
			int n = -1;
			final byte[] buffer = new byte[1024];
			try (final OutputStream os = this.response.getOutputStream();) {
				while ((n = message.read(buffer)) > 0) {
					os.write(buffer, 0, n);
				}
			} catch (final Exception e) {
				LOGGER.error("Error al devolver el resultado al cliente a traves del metodo write", e); //$NON-NLS-1$
			}
		}
	}
}

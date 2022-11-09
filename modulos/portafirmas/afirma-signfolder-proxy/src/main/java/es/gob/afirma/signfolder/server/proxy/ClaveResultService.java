package es.gob.afirma.signfolder.server.proxy;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.gob.afirma.signfolder.client.MobileException;
import es.gob.afirma.signfolder.client.MobileService;
import es.gob.afirma.signfolder.client.MobileService_Service;
import es.gob.afirma.signfolder.server.proxy.sessions.SessionCollector;

/**
 * Servicio para la obtenci&oacute;n del resultado del inicio de sesi&oacute;n con Cl@ve.
 */
public class ClaveResultService extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = LoggerFactory.getLogger(ClaveResultService.class);

	private static final String REQUEST_PARAM_RESPONSE = "SAMLResponse"; //$NON-NLS-1$
	private static final String REQUEST_PARAM_SHARED_SESSION_ID = "ssid"; //$NON-NLS-1$

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ClaveResultService() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	@Override
	protected void doPost(final HttpServletRequest request, final HttpServletResponse response) throws ServletException, IOException {

		LOGGER.debug("Se recibe la respuesta de Cl@ve"); //$NON-NLS-1$

		final String sessionId = request.getParameter(REQUEST_PARAM_SHARED_SESSION_ID);
		final HttpSession session = SessionCollector.getSession(request, sessionId);
		if (session == null) {
			LOGGER.warn("Se intenta acceder a traves del servicio de resultado de Clave sin iniciar sesion"); //$NON-NLS-1$
			response.sendRedirect("error.jsp?type=session"); //$NON-NLS-1$
			return;
		}

		if (session.getAttribute(SessionParams.INIT_WITH_CLAVE) == null) {
			LOGGER.warn("Se intenta acceder a traves del servicio de resultado de Clave sin iniciar sesion con Clave"); //$NON-NLS-1$
			SessionCollector.removeSession(session);
			response.sendRedirect("error.jsp?type=session"); //$NON-NLS-1$
			return;
		}

		// Obtenemos el parametro con la respuesta SAML
		final String samlResponse = request.getParameter(REQUEST_PARAM_RESPONSE);

		// Conectamos con el Portafirmas web para descifrarla
		final MobileService service = new MobileService_Service(ConfigManager.getSignfolderUrl()).getMobileServicePort();
		String dni;
		try {
			dni = service.procesarRespuestaClave(samlResponse, "https://www.prueba.es");	// XXX: Esta URL ahora carece de utilidad
		} catch (final MobileException e) {
			LOGGER.warn("Error al solicitar al Portafirmas que procese la respuesta SAML de Clave", e); //$NON-NLS-1$
			SessionCollector.removeSession(session);
			response.sendRedirect("error.jsp?type=validation"); //$NON-NLS-1$
			return;
		}

		LOGGER.debug("DNI del usuario logueado: " + dni); //$NON-NLS-1$

		// Se guarda el DNI en sesion para realizar peticiones
		session.setAttribute(SessionParams.DNI, dni);
		session.setAttribute(SessionParams.VALID_SESSION, Boolean.TRUE.toString());

		// Se eliminan atributos de sesion innecesarios
		session.removeAttribute(SessionParams.INIT_WITH_CLAVE);
		session.removeAttribute(SessionParams.CLAVE_REQUEST_TOKEN);
		session.removeAttribute(SessionParams.CLAVE_EXCLUDED_IDPS);
		session.removeAttribute(SessionParams.CLAVE_FORCED_IDP);

		SessionCollector.updateSession(session);

		response.sendRedirect("ok.jsp?dni=" + dni); //$NON-NLS-1$
	}
}

package com.tsol.modulos.buscador.utils;

import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.impl.SessionAPIFactory;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.audit.business.vo.AuditContext;
import ieci.tdw.ispac.audit.context.AuditContextHolder;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.StateContext;
import ieci.tdw.ispac.ispaclib.directory.DirectoryConnectorFactory;
import ieci.tdw.ispac.ispaclib.directory.IDirectoryConnector;
import ieci.tdw.ispac.ispaclib.directory.IDirectoryEntry;
import ieci.tdw.ispac.ispaclib.session.OrganizationUserInfo;
import ieci.tdw.ispac.ispaclib.utils.LocaleHelper;
import ieci.tecdoc.sgm.base.exception.IeciTdException;
import ieci.tecdoc.sgm.core.admin.web.AutenticacionBackOffice;
import ieci.tecdoc.sgm.core.services.gestion_backoffice.ConstantesGestionUsuariosBackOffice;
import ieci.tecdoc.sgm.core.web.locale.LocaleFilterHelper;
import ieci.tecdoc.sgm.sesiones.backoffice.ws.client.Sesion;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForward;

public class BuscadorDocsUtils {
	
	private static final String APLICACION = "BUSCADOR_DOCS";
	private static final Logger logger = Logger.getLogger(BuscadorDocsUtils.class);
	
	/**
	 * Método para imprimir el documento en una página del navegador
	 * @param response
	 * @param searchBean
	 * @param volumeBean
	 * @throws IeciTdException
	 * @throws Exception
	 * @throws IOException
	 */
	public static ActionForward imprimirDocumento(HttpServletResponse response, ClientContext ctx, IItem itemDoc) throws IeciTdException, Exception, IOException {
		
		// Obtener el GUID del documento en el repositorio de documentos
		String documentGUID = itemDoc.getString("INFOPAG_RDE");
		if (StringUtils.isBlank(documentGUID)) {
			documentGUID = itemDoc.getString("INFOPAG");
		}

		return imprimirDocumento(response, ctx, itemDoc, documentGUID);
	}
	
	/**
	 * Método para imprimir el documento original firmado en una página del navegador
	 * @param response
	 * @param searchBean
	 * @param volumeBean
	 * @throws IeciTdException
	 * @throws Exception
	 * @throws IOException
	 */
	public static ActionForward imprimirDocumentoOriginal(HttpServletResponse response, ClientContext ctx, IItem itemDoc) throws IeciTdException, Exception, IOException {
		
		// Obtener el GUID del documento en el repositorio de documentos
		String documentGUID = itemDoc.getString("INFOPAG_RDE_ORIGINAL");
		if (StringUtils.isBlank(documentGUID)) {
			documentGUID = itemDoc.getString("INFOPAG_RDE");
			if (StringUtils.isBlank(documentGUID)) {
				documentGUID = itemDoc.getString("INFOPAG");
			}
		}
		
		return imprimirDocumento(response, ctx, itemDoc, documentGUID);
	}
	
	public static ActionForward imprimirDocumento(HttpServletResponse response, ClientContext ctx, IItem itemDoc, String documentGUID) throws IeciTdException, Exception, IOException {

		ServletOutputStream out = response.getOutputStream();
		IGenDocAPI genDocAPI = ctx.getAPI().getGenDocAPI();
		String idDoc = itemDoc.getString("ID");
		
		if (StringUtils.isBlank(documentGUID)) {
			return null;
		}
		
		Object connectorSession = null;

		try {

			connectorSession = genDocAPI.createConnectorSession();

			if (!genDocAPI.existsDocument(connectorSession, documentGUID)) {
				// Se saca el mensaje de error en la propia ventana, que habra
				// sido lanzada con un popup
				response.setContentType("text/html");
				logger.error("No se ha encontrado el documento físico con identificador: '" + documentGUID + "' en el repositorio de documentos");
				String message = new ISPACInfo("exception.documents.notExists").getExtendedMessage(ctx.getLocale());
				out.write(message.getBytes());
				out.close();
				return null;
			}

			response.setHeader("Pragma", "public");
			response.setHeader("Cache-Control", "max-age=0");
			response.setHeader("Content-Transfer-Encoding", "binary");
			response.setHeader("X-Robots-Tag", "noindex"); //[eCenpri-Felipe #1032]

			String mimetype = genDocAPI.getMimeType(connectorSession, documentGUID);
			response.setContentType(mimetype);

			// Extensión del documento
			String extension = BuscadorDocsUtils.getDocumentExtension(itemDoc);
			
			if ("application/pdf".equalsIgnoreCase(mimetype) || StringUtils.isBlank(extension)) {
				StringBuffer name = new StringBuffer(idDoc);
				if (StringUtils.isNotBlank(extension)) {
					name.append(".").append(extension);
				}
				response.setHeader("Content-Disposition",
						"attachment; filename=\"" + name.toString() + "\"");
			} else {
				response.setHeader("Content-Disposition", "inline; filename=\""
						+ idDoc + "." + extension + "\"");
			}

			response.setContentLength(genDocAPI.getDocumentSize(connectorSession, documentGUID));

			try {
				genDocAPI.getDocument(connectorSession, documentGUID, out);
			} catch (ISPACException e) {
				// Se saca el mensaje de error en la propia ventana, que habra
				// sido lanzada con un popup
				response.setContentType("text/html");
				out.write(e.getCause().getMessage().getBytes());
			} finally {
				out.close();
			}

		} catch (Exception e) {

			logger.error("Error al descargar el documento", e);

			// Se saca el mensaje de error en la propia ventana, que habra sido
			// lanzada con un popup
			response.setContentType("text/html");
			out.write(e.getCause().getMessage().getBytes());

		} finally {
			if (connectorSession != null) {
				genDocAPI.closeConnectorSession(connectorSession);
			}
		}
		
		return null;
	}
	
	/**
	 * Obtiene el GUID del documento en el repositorio de documentos.
	 *
	 * @param document
	 *            Información del documento
	 * @return GUID del documento
	 * @throws ISPACException
	 */
	public static String getDocumentGUID(IItem document) throws ISPACException {

		String guid = document.getString("INFOPAG_RDE");
		if (StringUtils.isNotBlank(guid)) {
			return guid;
		}

		return document.getString("INFOPAG");
	}

    /**
     * Obtiene la extensión del documento a descargar
	 * @param document
	 *            Información del documento
     * @return Extensión del documento
     * @throws ISPACException
     */
    public static String getDocumentExtension(IItem document) throws ISPACException {

    	// Obtener la extensión del documento del repositorio de documentos electronicos
    	String guid = document.getString("INFOPAG_RDE");
    	if (StringUtils.isNotBlank(guid)) {
    		return document.getString("EXTENSION_RDE");
    	} else {
    		return document.getString("EXTENSION");
    	}
    }

	/**
	 * @param request
	 */
    public static void setAuditContext(HttpServletRequest request, SessionAPI session) {
		AuditContext auditContext = new AuditContext();
		auditContext.setUserHost(request.getRemoteHost());
		auditContext.setUserIP(request.getRemoteAddr());
		auditContext.setUser(session.getUserName());
		auditContext.setUserId(session.getClientContext().getUser().getUID());
		AuditContextHolder.setAuditContext(auditContext);
	}
    
    /**
     * Creación de la sesión para la búsqueda documental
     * @param request
     * @param response
     * @param entidad
     * @return
     * @throws ISPACException 
     */
    public static SessionAPI createSession
    	(HttpServletRequest request, HttpServletResponse response, String entidad, String usuario) throws ISPACException
    {
    	
		if (StringUtils.isBlank(entidad)) {

			// Información de la sesión del usuario
			Sesion sesionBO = AutenticacionBackOffice.obtenerDatos(request);
			if (sesionBO != null) {
				entidad = sesionBO.getIdEntidad();
				usuario = sesionBO.getUsuario();
			}
		}

		request.getSession().setAttribute("entidad", entidad);

		// Almacenamos la entidad en sesión, útil si queremos realizar
		// personalizaciones de imagenes, css, etc en función
		// de la entidad contra la que se está trabajando
		request.getSession().setAttribute(
				ConstantesGestionUsuariosBackOffice.PARAMETRO_ID_ENTIDAD, entidad);

		// Obtener el locale seleccionado por el filtro
		Locale locale = LocaleFilterHelper.getCurrentLocale(request);

		// Almacenamos el idioma seleccionado en sesión
		LocaleHelper.setLocale(request, locale);

		String remoteHost = request.getRemoteHost();
		if (StringUtils.isBlank(remoteHost)) {
			remoteHost = request.getRemoteAddr();
		}

		try {

			OrganizationUserInfo info = new OrganizationUserInfo();
			info.setOrganizationId(entidad);
			info.setUserName(usuario);

			SessionAPI sessionAPI = SessionAPIFactory.getSessionAPI(request, info);

			//Se eliminan las sesiones activas del usuario actual para esta aplicacion
			IItemCollection activeSessions = sessionAPI.getActiveSessions(usuario, APLICACION);
			while(activeSessions.next()){
				IItem activeSession = activeSessions.value();
				sessionAPI.deleteSession(activeSession.getString("ID"));
			}

			IDirectoryConnector directory = DirectoryConnectorFactory.getConnector();
			IDirectoryEntry userEntry = directory.getEntryFromUserName(usuario);
			info.setUserId(userEntry.getUID());

			sessionAPI.login(remoteHost, usuario, userEntry, APLICACION, locale);

			// Generar el ticket que se guarda en una cookie
			String ticket = sessionAPI.getTicket();
			Cookie cookieUser = new Cookie("user", ticket);
			response.addCookie(cookieUser);

			// creamos el objeto de estado del contexto
			StateContext stateContext = new StateContext();
			String stateticket = stateContext.getStateTicket();
			Cookie cookieInfo = new Cookie("contextInfo",stateticket);
			response.addCookie(cookieInfo);
			
			return sessionAPI;

		} catch (ISPACException e) {

			logger.error("Se ha producido un error al crear la sesión", e);
			throw e;
		}

    }
}

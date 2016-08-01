package es.dipucr.sigem.arbolDocumental.actions;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.impl.SessionAPIFactory;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.audit.business.vo.AuditContext;
import ieci.tdw.ispac.audit.context.AuditContextHolder;
import ieci.tdw.ispac.ispaccatalog.action.BaseAction;
import ieci.tdw.ispac.ispaclib.directory.DirectoryConnectorFactory;
import ieci.tdw.ispac.ispaclib.directory.IDirectoryConnector;
import ieci.tdw.ispac.ispaclib.directory.IDirectoryEntry;
import ieci.tdw.ispac.ispaclib.session.OrganizationUserInfo;
import ieci.tdw.ispac.ispaclib.utils.LocaleHelper;
import ieci.tdw.ispac.ispacweb.util.ActionRedirect;
import ieci.tecdoc.sgm.core.user.web.ConstantesSesionUser;
import ieci.tecdoc.sgm.core.user.web.WebAuthenticationHelper;
import ieci.tecdoc.sgm.core.web.locale.LocaleFilterHelper;

import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ShowProcedimientoAction extends BaseAction {
	
	public final static String CUADRO_PROCEDIMIENTO = "CUADRO_PROCEDIMIENTO";
	
	private static final String APLICACION = "AD";
	
	private static final String CONTENT_TYPE = "text/html; charset=ISO-8859-15";
	
	private static final String ERROR_FORWARD_KEY="appError";
	
	public static final String GLOBAL_FORWARD_ERROR = "error";
	public static final String GLOBAL_FORWARD_LOGIN = "errorAutenticacion";
	private static final String TIPO_APLICACION = "ArbolDocumental";


	
	/**
	 * Ejecuta la lógica de la acción.
	 * @param mapping El ActionMapping utilizado para seleccionar esta instancia
	 * @param form El ActionForm bean (opcional) para esta petición
	 * @param request La petición HTTP que se está procesando
	 * @param response La respuesta HTTP que se está creando
	 * @param session Información de la sesión del usuario
	 * @return La redirección a la que se va a transferir el control.
	 * @throws ISPACException si ocurre algún error.
	 */
 	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {
 		
 		request.getSession().removeAttribute(CUADRO_PROCEDIMIENTO);
		
		ActionRedirect ret = new ActionRedirect(mapping.findForward("home"));
		String path = ret.getPath();
//		path = path +"&entityId="+request.getParameter("entityId") + "&";
		path = path + "&entityId=22&";
		path = path + ConstantesSesionUser.ID_SESION + "=" + request.getSession().getAttribute(ConstantesSesionUser.ID_SESION);
		path = path + "&regId=" + request.getParameter("regId");
		ret.setPath(path);
		ret.setRedirect(true);
		return ret;
	}
 	
 	/**
	 * This is the main action called from the Struts framework.
	 *
	 * @param mapping The ActionMapping used to select this instance.
	 * @param form The optional ActionForm bean for this request.
	 * @param request The HTTP Request we are processing.
	 * @param response The HTTP Response we are processing.
	 */
 	
 	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {

		ActionForward forward = null;
		SessionAPI sesion = null;

		try{
			
			String entityId = obtenerIdentificadorEntidad(request);
			boolean tieneSesion = false;
			
			if( !isNuloOVacio(request.getParameter(ConstantesSesionUser.ID_SESION)) || !isNuloOVacio(request.getAttribute(ConstantesSesionUser.ID_SESION))  || !isNuloOVacio(request.getSession().getAttribute(ConstantesSesionUser.ID_SESION))){
				// Ya existe una sesión de usuario iniciada.
				if(isNuloOVacio(entityId)){
					tieneSesion = false;
				}
				tieneSesion = true;
			}
			if(!tieneSesion){
				response.sendRedirect(WebAuthenticationHelper.getWebAuthURL(request,TIPO_APLICACION));
				return null;
			}
			
			Locale locale = LocaleFilterHelper.getCurrentLocale(request);
			
			String remoteHost = request.getRemoteHost();
			if (StringUtils.isBlank(remoteHost)) {
				remoteHost = request.getRemoteAddr();
			}
			
			// Almacenamos el idioma seleccionado en sesión
			LocaleHelper.setLocale(request, locale);
			
			String entityName = null;
			String userName = null;
			
			OrganizationUserInfo info = new OrganizationUserInfo();
			info.setOrganizationId(entityId);
			info.setOrganizationName(entityName);
			info.setUserName(userName);
			
			sesion = SessionAPIFactory.getSessionAPI(request, info);
//			sesion.init(locale);
			
			//Se eliminan las sesiones activas del usuario actual para esta aplicacion
			IItemCollection activeSessions = sesion.getActiveSessions(userName, APLICACION);
			while(activeSessions.next()){
				IItem activeSession = activeSessions.value();
				sesion.deleteSession(activeSession.getString("ID"));
			}

			IDirectoryConnector directory = DirectoryConnectorFactory.getConnector();
			IDirectoryEntry userEntry = directory.getEntryFromRoot();
//			IDirectoryEntry userEntry = directory.getEntryFromUserName(userName);
			info.setUserId(userEntry.getUID());

			sesion.login(remoteHost, userName, userEntry, APLICACION, locale);

			request.getSession().setAttribute(ConstantesSesionUser.ID_ENTIDAD, entityId);
//			request.getSession().setAttribute(ConstantesSesionUser.ID_SESION, request.getParameter(ConstantesSesionUser.ID_SESION));
	    	forward = executeAction(mapping, form, request, response, sesion);

		}catch(Throwable e){
			logger.error("Error inesperado ejecutando acción.", e);
			return mapping.findForward(GLOBAL_FORWARD_ERROR);
		}

		return forward;
	}
 	
// 	
//	public ActionForward execute(ActionMapping mapping, ActionForm form,
//			HttpServletRequest request, HttpServletResponse response) throws Exception
//	{
//		
//		// Establecer el ContentType
//		response.setContentType(CONTENT_TYPE);
//
//		SessionAPI sesion = null;
//		
//		try {
//			
//			sesion = SessionAPIFactory.getSessionAPI(request, response);
//
//		} catch (ISPACException e) {
//			
//			if (AutenticacionBackOffice.autenticar(request)) {
//
//				// Información de la sesión del usuario
//				Sesion sesionBO = AutenticacionBackOffice.obtenerDatos(request);
//				String entityId = sesionBO.getIdEntidad();
//
//				HttpSession session = request.getSession();
//
//				//Almacenamos la entidad en sesión, útil si queremos realizar personalizaciones de imagenes, css, etc en función
//				//de la entidad contra la que se está trabajando
//				session.setAttribute(ConstantesGestionUsuariosBackOffice.PARAMETRO_ID_ENTIDAD, entityId);
//
//				// Obtener el locale seleccionado por el filtro
//				Locale locale = LocaleFilterHelper.getCurrentLocale(request);
//
//				// Almacenamos el idioma seleccionado en sesión
//				LocaleHelper.setLocale(request, locale);
//
//				String entityName = null;
//				String userName = sesionBO.getUsuario();
//
//				String remoteHost = request.getRemoteHost();
//				if (StringUtils.isBlank(remoteHost)) {
//					remoteHost = request.getRemoteAddr();
//				}
//				
//				OrganizationUserInfo info = new OrganizationUserInfo();
//				info.setOrganizationId(entityId);
//				info.setOrganizationName(entityName);
//				info.setUserName(userName);
//				
//				sesion = SessionAPIFactory.getSessionAPI(request, info);
//				
////				sesion.init(locale);
//				
//				//Se eliminan las sesiones activas del usuario actual para esta aplicacion
//				IItemCollection activeSessions = sesion.getActiveSessions(userName, APLICACION);
//				while(activeSessions.next()){
//					IItem activeSession = activeSessions.value();
//					sesion.deleteSession(activeSession.getString("ID"));
//				}
//
//				IDirectoryConnector directory = DirectoryConnectorFactory.getConnector();
//				IDirectoryEntry userEntry = directory.getEntryFromRoot();
////				IDirectoryEntry userEntry = directory.getEntryFromUserName(userName);
//				info.setUserId(userEntry.getUID());
//
//				sesion.login(remoteHost, userName, userEntry, APLICACION, locale);
//
//			}
//		}
//		
//		String username = sesion.getRespName();
//		request.setAttribute("User", username);
//
//		try {
//			
//			setAuditContext(request, sesion);
//			
//			ActionForward forward = executeAction(mapping, form, request, response, sesion);
//			
//			// Contexto para la vista
//			// utilizado en el Tag de Calendario para obtener el idioma de la aplicación
//			request.setAttribute("ClientContext", sesion.getClientContext());
//			
//			// Guardamos la ultima URL accedida correctamente.
//			if ((forward != null) && (!forward.getRedirect())) {
//				setLastURL(request);
//			}
//			
//			return forward;
//		}
//		catch(ISPACInfo e) {
//			
//			// Obtener el recurso para el mensaje de error de la excepción
//			saveAppErrors(request, getActionMessages(request, e));
//			
//			// Si no se ha definido la gestión de error para la acción actual
//			// en el struts-config se vuelve a invocar la anterior.
//			ActionForward actionForward = mapping.findForward(ERROR_FORWARD_KEY);
//			if(actionForward != null)
//			    return actionForward;
//			
//			//Si la acción anterior coincide con la actual
//			//hemos entrado en un bucle: lanzamos una ISPACException
//			//que será capturada por el manejador definido en el struts-config
//			if(compareLastURL(request))
//				throw new ISPACException("No se encuentra un forward válido para la gestión del error", e);
//
//			return new ActionForward((String)request.getSession().getAttribute(BaseAction.LAST_URL_SESSION_KEY) );
//		}
//		finally {
//			if(null != sesion) sesion.release();
//		}
//	}
	
	/**
	 * @param request
	 */
	private void setAuditContext(HttpServletRequest request, SessionAPI session) {
		AuditContext auditContext = new AuditContext();
		auditContext.setUserHost(request.getRemoteHost());
		auditContext.setUserIP(request.getRemoteAddr());
		if (null != session){
			auditContext.setUser(session.getUserName());
			//auditContext.setUserId(session.getClientContext().getUser().getUID());
		}
		AuditContextHolder.setAuditContext(auditContext);
	}
	
	private boolean compareLastURL(HttpServletRequest request){
		String lastURL = (String)request.getSession().getAttribute(LAST_URL_SESSION_KEY);
		String actual = getUrl(request);
		if(actual.equals(lastURL)){
			return true;
		}
		return false;
	}
	
	private String getUrl(HttpServletRequest request){
		StringBuffer sUrl = new StringBuffer(request.getServletPath());
		String sQueryString = request.getQueryString();
		if((sQueryString != null) && (!sQueryString.equals(""))){
			sUrl.append("?");
			sUrl.append(sQueryString);
		}
		return sUrl.toString();
	}
	
	private void setLastURL(HttpServletRequest request){
		request.getSession().setAttribute(LAST_URL_SESSION_KEY, getUrl(request));
	}
	
	public static String obtenerIdentificadorEntidad(HttpServletRequest request){
		String idEntidad = (String)request.getSession().getAttribute(ConstantesSesionUser.ID_ENTIDAD);
		if(isNuloOVacio(idEntidad)){
			idEntidad = request.getParameter(ConstantesSesionUser.ID_ENTIDAD);
		}
		return idEntidad;
	}
	
	public static boolean isNuloOVacio(Object cadena)
	{
		if((cadena == null) || ("".equals(cadena)) || ("null".equals(cadena))) {
			return true;
		}
		return false;
	}
}

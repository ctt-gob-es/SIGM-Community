package es.dipucr.sigem.actions.mensajes.catalogo;

import ieci.tdw.ispac.api.ICatalogAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IMensajeAPI;
import ieci.tdw.ispac.api.IRespManagerAPI;
import ieci.tdw.ispac.api.ISPACEntities;
import ieci.tdw.ispac.api.ISecurityAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IResponsible;
import ieci.tdw.ispac.ispaccatalog.action.BaseAction;
import ieci.tdw.ispac.ispaccatalog.action.form.UploadForm;
import ieci.tdw.ispac.ispaccatalog.helpers.FunctionHelper;
import ieci.tdw.ispac.ispaclib.app.EntityApp;
import ieci.tdw.ispac.ispaclib.bean.ValidationError;
import ieci.tdw.ispac.ispaclib.dao.cat.CTMensaje;
import ieci.tdw.ispac.ispaclib.dao.cat.CTMensajeDAO;
import ieci.tdw.ispac.ispaclib.dao.session.SessionDAO;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.resp.OrgUnit;
import ieci.tdw.ispac.ispaclib.resp.User;
import ieci.tdw.ispac.ispaclib.util.ISPACConfiguration;
import ieci.tdw.ispac.ispaclib.utils.DBUtil;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.dto.Entidad;
import ieci.tecdoc.sgm.core.services.entidades.ServicioEntidades;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

public class StoreMensajeAction extends BaseAction {
	
	public ActionForward executeAction( ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, SessionAPI session) throws Exception {
		
 		// Comprobar si el usuario tiene asignadas las funciones adecuadas
		FunctionHelper.checkFunctions(request, session.getClientContext(), new int[] { ISecurityAPI.FUNC_INV_DOCTYPES_EDIT, ISecurityAPI.FUNC_INV_TEMPLATES_EDIT });		
		
		IInvesflowAPI invesFlowAPI = session.getAPI();
        ICatalogAPI catalogAPI = invesFlowAPI.getCatalogAPI();
    	IMensajeAPI mensajeAPI = invesFlowAPI.getMensajeAPI();
    	IRespManagerAPI respAPI = invesFlowAPI.getRespManagerAPI();

    	// Formulario asociado a la acción
		UploadForm defaultForm = (UploadForm) form;

		int keyId = Integer.parseInt(defaultForm.getKey());
		int entityId = Integer.parseInt(defaultForm.getEntity());
		String idSesion = defaultForm.getProperty(CTMensajeDAO.ID_SESION);
		String soloConectados = defaultForm.getProperty(CTMensajeDAO.SOLO_CONECTADOS);
		String texto = defaultForm.getProperty(CTMensajeDAO.TEXTO);
		String usuario = defaultForm.getProperty(CTMensajeDAO.USUARIO);
		String idUsuario = defaultForm.getProperty(CTMensajeDAO.ID_USUARIO);
		String tipo = defaultForm.getProperty(CTMensajeDAO.TIPO);		
		String masivo = defaultForm.getProperty(CTMensajeDAO.MASIVO);
		String passWord = defaultForm.getProperty(CTMensajeDAO.PASSADM);
		
		if(StringUtils.isEmpty(tipo)){
			tipo = "form.mensaje.propertyLabel.info";
		}
		
		String accion = "success";
		CTMensaje mensaje = null;		

		try {
			if("true".equals(masivo)) {
				String passwordEnvioMasivo = ISPACConfiguration.getInstance().get(ISPACConfiguration.PASSWORD_ENVIO_MENSAJES_MASIVO);
				
				if(StringUtils.isNotEmpty(passwordEnvioMasivo) && passwordEnvioMasivo.equals(passWord)){
					DbCnt conexionOriginal = session.getClientContext().getConnection();
					
					@SuppressWarnings("rawtypes")
					List oLista = obtenerListaEntidades();
					
					for (Object oEntidad : oLista){					
						Entidad entidad = (Entidad)oEntidad;
						
						String DATASOURCE_NAME = "java:comp/env/jdbc/tramitadorDS_" + entidad.getIdentificador();
						String dsName = DATASOURCE_NAME;
						DbCnt conexionAyto = new DbCnt(dsName);
						
						session.getClientContext().getConnection().setConnection(conexionAyto.getConnection());
						IItemCollection sesionesActivas = SessionDAO.getSesiones(session.getClientContext().getConnection(), null).disconnect();
					
						enviaMensajeSesion(mensajeAPI, sesionesActivas, texto, tipo);
					}
					
					session.getClientContext().getConnection().setConnection(conexionOriginal.getConnection());
				}
			} else {
				if (keyId == ISPACEntities.ENTITY_NULLREGKEYID) {
					if(StringUtils.isNotEmpty(idUsuario)){
						IResponsible destinatario = respAPI.getResp(idUsuario);
						enviarMensaje(session, respAPI, mensajeAPI, destinatario, soloConectados, texto, tipo);
	
					} else {					
						IItemCollection sesionesActivas = SessionDAO.getSesiones(session.getClientContext().getConnection(), null).disconnect();
						enviaMensajeSesion(mensajeAPI, sesionesActivas, texto, tipo);
					}
					
					EntityApp entityapp = catalogAPI.getCTDefaultEntityApp(entityId, getRealPath(""));
					
					if (!entityapp.validate()) {
						ActionMessages errors = new ActionMessages();
						List<?> errorList = entityapp.getErrors();
						Iterator<?> iteError = errorList.iterator();
						
						while (iteError.hasNext()) {
							ValidationError validError = (ValidationError) iteError.next();
							ActionMessage error = new ActionMessage(validError.getErrorKey(), validError.getArgs());
							errors.add(ActionMessages.GLOBAL_MESSAGE, error);
						}
						
						saveAppErrors(request, errors);
						
						return new ActionForward(mapping.getInput());
					}	
					accion = "success";
				} else {
					mensaje = mensajeAPI.updateMensaje(keyId, idSesion, texto, usuario, tipo);
					accion = "successUpdate";
				}
			}
			
		} catch(Exception e) {
			ActionForward action = mapping.findForward("success");
			String urlRedirec = action.getPath() + "?entity="+ entityId + "&type="+ defaultForm.getProperty("ID_TPDOC") + "&key="+ keyId;

			request.getSession().setAttribute(BaseAction.LAST_URL_SESSION_KEY, urlRedirec);
			
			if (e instanceof ISPACInfo) {
				throw e;
			} else {
				throw new ISPACInfo(e.getMessage());
			}
		}
		
		if (mensaje != null) {
			keyId = mensaje.getId();
		}
		
		ActionForward action = mapping.findForward(accion);
		String redirected = action.getPath() + "?entity=" + entityId + "&regId="+ keyId;
		return new ActionForward( action.getName(), redirected, true);
	}
	
	public CTMensaje enviaMensajeSesion(IMensajeAPI mensajeAPI, IItemCollection sesionesActivas, String texto, String tipo) throws ISPACException {
		
		CTMensaje mensaje = null;
		
		while (sesionesActivas.next()) {
			IItem sesion = sesionesActivas.value();						
			mensaje = mensajeAPI.newMensaje(sesion.getString("ID"), texto, sesion.getString("USUARIO"), tipo);
		}
		
		return mensaje;		
	}

	public CTMensaje enviarMensaje(SessionAPI session, IRespManagerAPI respAPI, IMensajeAPI mensajeAPI, IResponsible destinatario, String soloConectados, String texto, String tipo) throws ISPACException{
		
		CTMensaje mensaje = null;
		String idSesion = "";
		
		ISPACConfiguration config = ISPACConfiguration.getInstance();
  		String sPeriod = config.get(ISPACConfiguration.SESSION_TIMEOUT);
  		long m_timeoutSession = Long.parseLong(sPeriod);
  		
        long timeout = System.currentTimeMillis() - m_timeoutSession;
        Timestamp ts = new Timestamp(timeout);
        String sesionExpirada = " AND KEEP_ALIVE >= " + DBUtil.getToTimestampByBD(session.getClientContext().getConnection(), ts);
        
		if (destinatario.isUser()){			
			String nombreUsuario = destinatario.getName();
			boolean enviar = false;
			
			if("1".equals(soloConectados)){
                String sQuery = new StringBuffer().append("WHERE USUARIO = '" + nombreUsuario + "' ").append(sesionExpirada).toString();

    			IItemCollection sesionUsuario = SessionDAO.getSesiones(session.getClientContext().getConnection(), sQuery).disconnect();
    			if(sesionUsuario.next()){
    				IItem sesion = sesionUsuario.value();
    				idSesion = sesion.getString("ID");
    				enviar = true;
    			}	                				                		
        	} else {
        		enviar = true;
        	}
			
        	if(enviar){
        		mensaje = mensajeAPI.newMensaje(idSesion, texto, nombreUsuario, tipo);
        	}
        	
        } else if(destinatario.isGroup() || destinatario.isOrgUnit()){
	        		
			IItemCollection usuarios = destinatario.getRespUsers();
	        List<?> usuariosIterator = usuarios.toList();
			
	        if(null != usuariosIterator && 0 < usuariosIterator.size()){
		        for (Object usr : usuariosIterator){
		        	String id_usuario = ((User)usr).getUID();	        	
		        	IResponsible usuarioHijo = respAPI.getResp(id_usuario);	        	
		        	enviarMensaje(session, respAPI, mensajeAPI, usuarioHijo, soloConectados, texto, tipo);
		        }
	        }
	        
	        if(destinatario.isOrgUnit()){
	        	IItemCollection departamentosHijos = destinatario.getRespOrgUnits();
		        List<?> departamentosHijosIterator = departamentosHijos.toList();
		        
		        if(null != departamentosHijosIterator && 0 < departamentosHijosIterator.size()){
		        	for (Object dep : departamentosHijosIterator){
		        		String id_usuario = ((OrgUnit)dep).getUID();	        	
		        		IResponsible departamentoHijo = respAPI.getResp(id_usuario);	        	
		        		enviarMensaje(session, respAPI, mensajeAPI, departamentoHijo, soloConectados, texto, tipo);
		        	}
		        }
	        }
        }
        
        return mensaje;
	}
	
	/**
	 * Obtiene la lista de entidades del sistema SIGEM
	 * @return List ArrayList de objetos eci.tecdoc.sgm.core.services.dto.Entidad
	 */
	public static List obtenerListaEntidades(){
		try {
			ServicioEntidades oServicio = LocalizadorServicios.getServicioEntidades();
			List oLista = oServicio.obtenerEntidades();
			return getEntidades(oLista);
		} catch (Exception e) {
			return new ArrayList();
		}
	}
	
	private static List getEntidades(List oLista){
		if (oLista == null)
			return new ArrayList();
		
		for(int i=0; i<oLista.size(); i++){
			oLista.set(i, getEntidad((ieci.tecdoc.sgm.core.services.entidades.Entidad)oLista.get(i)));
		}
		
		return oLista;
	}
	
	private static Entidad getEntidad(ieci.tecdoc.sgm.core.services.entidades.Entidad oEntidad) {
		if (oEntidad == null)
			return null;
		
		Entidad poEntidad = new Entidad();
		
		poEntidad.setIdentificador(oEntidad.getIdentificador());
		poEntidad.setNombre(oEntidad.getNombreLargo());
		
		return poEntidad;
	}
}
package ieci.tecdoc.mvc.action;

import ieci.tecdoc.sgm.core.admin.web.AutenticacionAdministracion;
import ieci.tecdoc.sgm.core.services.gestion_administracion.ConstantesGestionUsuariosAdministracion;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ChequearSesion extends BaseAction{

	protected ActionForward executeLogic(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if(!autenticarEntidad(request, ConstantesGestionUsuariosAdministracion.APLICACION_ESTRUCTURA_ORGANIZATIVA)) {
			response.getWriter().print(true);
		} else {
			response.getWriter().print(false);
		}
		return null;
	}

	private static boolean autenticarEntidad(HttpServletRequest request, String idAplicacion) {
		
    	String key_entidad = request.getParameter(ConstantesGestionUsuariosAdministracion.PARAMETRO_KEY_SESION_USUARIO_ADM_ENTIDAD);
    	if(AutenticacionAdministracion.isNuloOVacio(key_entidad)) {
    		key_entidad = (String) request.getSession().getAttribute(ConstantesGestionUsuariosAdministracion.PARAMETRO_KEY_SESION_USUARIO_ADM_ENTIDAD);
    	}
    	
    	if(AutenticacionAdministracion.isNuloOVacio(key_entidad)) {
    		return false;
    	} else {
   			request.getSession().setAttribute(ConstantesGestionUsuariosAdministracion.PARAMETRO_KEY_SESION_USUARIO_ADM_ENTIDAD, key_entidad);
   			return true;
    	}
	}
	
}

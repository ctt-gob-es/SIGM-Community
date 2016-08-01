package es.dipucr.sigem.arbolDocumental.actions;

import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.admsesion.backoffice.ServicioAdministracionSesionesBackOffice;
import ieci.tecdoc.sgm.core.services.gestion_backoffice.ConstantesGestionUsuariosBackOffice;
import ieci.tecdoc.sgm.core.user.web.ConstantesSesionUser;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ExitAction extends Action{
	
	private static final Logger logger = Logger.getLogger(ExitAction.class);
			
	public ActionForward execute(ActionMapping mapping , ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		try{
			HttpSession session = request.getSession();
			
			if (session.getAttribute("keySesionUsuarioRP") != null) {
				session.removeAttribute("keySesionUsuarioRP");
			}
			
			session.removeAttribute(ConstantesSesionUser.ID_ENTIDAD);
			session.removeAttribute(ConstantesSesionUser.ID_SESION);
			session.removeAttribute(ConstantesSesionUser.IDIOMA);
			session.removeAttribute(ConstantesSesionUser.LANG);
			session.removeAttribute(ConstantesSesionUser.TRAMITE_ID);
//			
//			request.getParameterMap().remove(ConstantesSesionUser.IDIOMA);
//			request.getParameterMap().remove(ConstantesSesionUser.ID_ENTIDAD);
//			request.getParameterMap().remove(ConstantesSesionUser.ID_SESION);			

		}catch(Exception e){
			logger.error("Se ha producido un error al deslogar.", e.fillInStackTrace());
			request.setAttribute("error_logout", e.getCause());
			return mapping.findForward("success");
	   	}		
		return mapping.findForward("success");
	}
	
	public static boolean isNuloOVacio(Object cadena) {
		if((cadena == null) || ("".equals(cadena)) || ("null".equals(cadena))) {
			return true;
		}
		return false;
	}
	
}

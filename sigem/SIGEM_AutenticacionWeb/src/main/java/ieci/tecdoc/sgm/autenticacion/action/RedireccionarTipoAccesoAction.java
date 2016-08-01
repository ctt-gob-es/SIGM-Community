package ieci.tecdoc.sgm.autenticacion.action;

import ieci.tecdoc.sgm.autenticacion.util.TipoAutenticacionCodigos;
import ieci.tecdoc.sgm.autenticacion.form.TipoAccesoForm;
import ieci.tecdoc.sgm.autenticacion.utils.Defs;
//import ieci.tecdoc.sgm.registro.utils.Defs;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.struts.action.Action;
//import org.apache.struts.action.ActionError;
//import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

/**
 * [eCenpri-Felipe Ticket#691] 
 * Copiada de la clase SeleccionarTipoAccesoAction
 * @since 23.08.12
 * @author Felipe
 */
public class RedireccionarTipoAccesoAction extends Action{
	public ActionForward execute(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		
		TipoAccesoForm listTiposBean = (TipoAccesoForm)form;
		
		//INICIO [eCenpri-Felipe Ticket#691]
		//Esta es la parte nueva respecto de la clase SeleccionarTipoAccesoAction
		//Si los parámetros no están en sesión los cogemos de request y los ponemos en sesión
		HttpSession session = request.getSession();
		setSessionParamFromRequest(request, session, Defs.ENTIDAD_ID);
		setSessionParamFromRequest(request, session, Defs.TRAMITE_ID);
		setSessionParamFromRequest(request, session, Defs.REDIRECCION);
		//FIN [eCenpri-Felipe #691]
		
		try{
			int tipoAceptado;
			try {
				tipoAceptado = new Integer(listTiposBean.getSelTipoAcceso()).intValue();
			} catch(NumberFormatException e) {
				tipoAceptado = new Integer((String)request.getSession().getAttribute(Defs.ACCESO_SEL)).intValue();
			}
			if (tipoAceptado == TipoAutenticacionCodigos.WEB_USER){
				request.getSession().setAttribute(Defs.ACCESO_SEL, ""+TipoAutenticacionCodigos.WEB_USER);
				return mapping.findForward("login");
			}else if (tipoAceptado == TipoAutenticacionCodigos.X509_CERTIFICATE){
				request.getSession().setAttribute(Defs.ACCESO_SEL, ""+TipoAutenticacionCodigos.X509_CERTIFICATE);
				return mapping.findForward("certificado");
			}else return mapping.findForward("failure");
		}catch(Exception e){
	   		return mapping.findForward("failure");
	   	}
	}

	/**
	 * [eCenpri-Felipe #691]
	 * Verifica si el parametro 'nombreParametro' está en sesión
	 * Si no está, lo toma de la request y lo pone en sesión
	 * @param request
	 * @param session
	 * @param nombreParametro
	 */
	private void setSessionParamFromRequest(HttpServletRequest request,
			HttpSession session, String nombreParametro) {
		
		String param = (String)session.getAttribute(nombreParametro);
		if (Defs.isNuloOVacio(param)){
			param = (String)request.getParameter(nombreParametro);
			if(!Defs.isNuloOVacio(param)){
				session.setAttribute(nombreParametro, param);
			}
		}
	}
}

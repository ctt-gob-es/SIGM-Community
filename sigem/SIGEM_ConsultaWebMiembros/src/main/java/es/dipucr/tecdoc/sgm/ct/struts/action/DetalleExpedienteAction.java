package es.dipucr.tecdoc.sgm.ct.struts.action;

import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.ct.ServicioConsultaExpedientes;
import ieci.tecdoc.sgm.ct.ServicioConsultaExpedientesAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.dipucr.tecdoc.sgm.ct.struts.form.DetalleExpedienteForm;
import es.dipucr.tecdoc.sgm.ct.utilities.Misc;

public class DetalleExpedienteAction extends ConsultaWebMiembroAction {
	
	/** 
	 * Se sobrescribe el metodo execute de la clase Action
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @return ActionForward
	 */
	 public ActionForward executeAction(
	        ActionMapping mapping,
	        ActionForm form,
	        HttpServletRequest request,
	        HttpServletResponse response) {
		 
		DetalleExpedienteForm detalleExpedienteForm = (DetalleExpedienteForm) form;
	    String sessionId = "";
	    
	    try{
	    	sessionId = request.getParameter("SESION_ID");
			if(Misc.isEmpty(sessionId)){
				sessionId = (String)request.getSession().getAttribute("SESION_ID");
				if(Misc.isEmpty(sessionId)){
					return mapping.findForward("NoAutenticado");
			    }
			}
			
			//Numero de expediente
	    	String numexp = request.getParameter("id");
	    	ServicioConsultaExpedientes oServicio = new ServicioConsultaExpedientes();
	    	String NIF = Misc.getNIFUsuario(request, sessionId);
	    	
	    	ieci.tecdoc.sgm.core.services.consulta.Propuestas propuestas =  oServicio.obtenerPropuestas(NIF, Misc.obtenerEntidad(request), numexp);
	    	
	    	detalleExpedienteForm.setPropuestas(propuestas);
	    	detalleExpedienteForm.setNumeroExpediente("numepero");
	    	    	
	    	request.setAttribute("busqueda", request.getParameter("busqueda"));
	    } catch (Exception ex){
	    	request.setAttribute("MENSAJE_ERROR", ex.getMessage());
	    	return mapping.findForward("Failure");
	    }
	    return mapping.findForward("Success");
	 }
	
}

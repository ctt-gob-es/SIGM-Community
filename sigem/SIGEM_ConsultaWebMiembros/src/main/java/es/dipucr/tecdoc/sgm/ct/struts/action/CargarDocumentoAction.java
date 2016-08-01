package es.dipucr.tecdoc.sgm.ct.struts.action;
/*
 *  $Id: CargarDocumentoAction.java,v 1.2.2.1 2008/02/05 13:31:39 jconca Exp $
 */
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.consulta.ServicioConsultaExpedientes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.dipucr.tecdoc.sgm.ct.struts.form.CargarDocumentoForm;
import es.dipucr.tecdoc.sgm.ct.utilities.Misc;

public class CargarDocumentoAction extends ConsultaWebMiembroAction {
	
	private static final String URL_DOCUMENTOS = "/consulta/doctmp/";
	
	/** 
	 * Method execute
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
		 
		 CargarDocumentoForm detalleExpedienteForm = (CargarDocumentoForm) form;
	    try{
	    	ServicioConsultaExpedientes oServicio = LocalizadorServicios.getServicioConsultaExpedientes();
	    	String path = oServicio.cargarDocumento(request.getParameter("guid"), Misc.obtenerEntidad(request));
//	    	String path = GestorConsulta.cargarDocumento(request.getSession().getId(), request.getParameter("guid"));
	    	String URL = URL_DOCUMENTOS + path.substring(path.lastIndexOf("\\") + 1,path.length());
//	    	String URL = Configuracion.getURLDocumentos() + path.substring(path.lastIndexOf("\\") + 1,path.length());
	    	detalleExpedienteForm.setURL(URL);
	    	//ActionForward forward = new ActionForward(url);
	    	//return forward;
	    } catch (Exception ex){
	    	request.setAttribute("MENSAJE_ERROR", ex.getMessage());
	    	return mapping.findForward("Failure");
	    }
	    return mapping.findForward("Success");
	 }

}

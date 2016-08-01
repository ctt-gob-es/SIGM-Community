package es.dipucr.tecdoc.sgm.ct.struts.action;
/*
 *  $Id: BusquedaExpedientesAction.java,v 1.6.2.1 2008/02/05 13:31:39 jconca Exp $
 */

import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.consulta.CriterioBusquedaExpedientes;
import ieci.tecdoc.sgm.core.services.consulta.Expediente;
import ieci.tecdoc.sgm.core.services.consulta.Expedientes;
import ieci.tecdoc.sgm.core.services.consulta.ServicioConsultaExpedientes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.dipucr.tecdoc.sgm.ct.struts.form.BusquedaExpedientesForm;
import es.dipucr.tecdoc.sgm.ct.utilities.Misc;

public class BusquedaExpedientesAction extends ConsultaWebMiembroAction {
	
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
		 
		String sessionId = "";
		BusquedaExpedientesForm busquedaExpedientesForm = (BusquedaExpedientesForm) form;
	    try{
	    	//String NIF = Misc.getNIFDesdeCertificado(request);
	    	
	    	sessionId = request.getParameter("SESION_ID");
			 if(Misc.isEmpty(sessionId)){
				 sessionId = (String)request.getSession().getAttribute("SESION_ID");
				 if(Misc.isEmpty(sessionId)){
					 return mapping.findForward("NoAutenticado");
			     }
			 }
	    	
	    	String NIF = Misc.getNIFUsuario(request, (String)request.getSession().getAttribute("SESION_ID"));
	    	busquedaExpedientesForm.setNIF(NIF);
	    	
	    	String rebuscar = request.getParameter("rebuscar");
	    	if ("false".equals(rebuscar)){
	    		String fechaDesde = (String)request.getSession().getAttribute("fechaDesde");
	    		String fechaHasta = (String)request.getSession().getAttribute("fechaHasta");
	    		String operadorConsulta  = (String)request.getSession().getAttribute("operadorConsulta");
	    		ServicioConsultaExpedientes oServicio = LocalizadorServicios.getServicioConsultaExpedientes();
	    		CriterioBusquedaExpedientes oCriterio = new CriterioBusquedaExpedientes();
	    		oCriterio.setNIF(NIF);
	    		oCriterio.setFechaDesde(fechaDesde);
	    		oCriterio.setFechaHasta(fechaHasta);
	    		oCriterio.setOperadorConsulta(operadorConsulta);
//	    		oServicio.consultarExpedientes(oCriterio);
	    		Expedientes expedientes = oServicio.busquedaExpedientes(oCriterio, Misc.obtenerEntidad(request));
//	    		Expedientes expedientes = GestorConsulta.busquedaExpedientes(request.getSession().getId(), NIF, fechaDesde, fechaHasta, operadorConsulta, estado);
	    		for (int i=0; i< expedientes.count(); i++){
	    			Expediente expediente = (Expediente)expedientes.get(i);
	    			if (expediente.getEstado() == Expediente.COD_ESTADO_EXPEDIENTE_FINALIZADO)
	    				expediente.setEstado("cerrado");
	    			else expediente.setEstado("abierto");
	    			
	    			String proc = expediente.getProcedimiento();
	    			if (proc.length() > 60){
	    				proc = proc.substring(0,57) + "...";
	    				expediente.setProcedimiento(proc);
	    			}
	    			
	    			boolean existeNotificacion = oServicio.existenNotificaciones(expediente.getNumero(), Misc.obtenerEntidad(request));
	    			if(existeNotificacion)
	    				expediente.setNotificacion("S");
	    			else expediente.setNotificacion("N");
	    			
	    			boolean existeSubsanacion = oServicio.existenSubsanaciones(expediente.getNumero(), Misc.obtenerEntidad(request));
	    			if(existeSubsanacion)
	    				expediente.setAportacion("S");
	    			else expediente.setAportacion("N");
	    			
	    			boolean existePago = oServicio.existenPagos(expediente.getNumero(), Misc.obtenerEntidad(request));
	    			if(existePago)
	    				expediente.setPagos("S");
	    			else expediente.setPagos("N");
	    			
//	    			expedientes.add(expediente);
//	    			expedientes.set(i, expediente);
	    		}
	    		request.setAttribute("expedientes", expedientes.getExpedientes());
	    		request.setAttribute("buscado", "true");
	    	}else if (!"true".equals(rebuscar)){
	    		request.getSession().removeAttribute("fechaDesde");
	    		request.getSession().removeAttribute("fechaHasta");
	    		request.getSession().removeAttribute("estado");
	    		request.getSession().removeAttribute("operadorConsulta");
	    		request.setAttribute("buscado", "false");
	    	}else{
	    		request.setAttribute("buscado", "true");
	    	}
	    	
	    	/*Expedientes expedientes = (Expedientes)request.getAttribute("expedientes");
	    	if (expedientes != null)
	    		request.setAttribute("expedientes", expedientes);*/
	    } catch (Exception ex){
	    	request.setAttribute("MENSAJE_ERROR", ex.getMessage());
	    	return mapping.findForward("Failure");
	    } 
	    return mapping.findForward("Success");
	 }
	 

}

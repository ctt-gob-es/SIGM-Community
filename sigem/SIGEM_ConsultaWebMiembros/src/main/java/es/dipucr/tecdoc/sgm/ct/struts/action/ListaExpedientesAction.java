package es.dipucr.tecdoc.sgm.ct.struts.action;
/*
 *  $Id: ListaExpedientesAction.java,v 1.8.2.1 2008/02/05 13:31:39 jconca Exp $
 */
import ieci.tdw.ispac.api.impl.InvesflowAPI;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tecdoc.sgm.core.services.consulta.CriterioBusquedaExpedientes;
import ieci.tecdoc.sgm.core.services.consulta.Expediente;
import ieci.tecdoc.sgm.core.services.consulta.Expedientes;
import ieci.tecdoc.sgm.ct.ServicioConsultaExpedientes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.apache.struts.action.ActionMessage;
import org.apache.struts.action.ActionMessages;

import es.dipucr.tecdoc.sgm.ct.struts.form.ListaExpedientesForm;
import es.dipucr.tecdoc.sgm.ct.utilities.Misc;

public class ListaExpedientesAction extends ConsultaWebMiembroAction {
	
	private static final Logger logger = Logger.getLogger(ListaExpedientesAction.class);
	
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
		 	String NIF = "";
		 	String sessionId = "";
		 try{
			 ClientContext context = new ClientContext();

			context.setAPI(new InvesflowAPI(context));
			
			 //NIF = Misc.getNIFDesdeCertificado(request);
			 //Misc.getNombreDesdeCertificado(request);
			 sessionId = request.getParameter("SESION_ID");
			 if(Misc.isEmpty(sessionId)){
				 sessionId = (String)request.getSession().getAttribute("SESION_ID");
				 if(Misc.isEmpty(sessionId)){
					 return mapping.findForward("NoAutenticado");
			     }
			 }
			 NIF = Misc.getNIFUsuario(request, sessionId);
			 
			 
			 
			 
		 } catch(Exception ex){
			 request.setAttribute("MENSAJE_ERROR",ex.getMessage());
		 }
		ListaExpedientesForm listaExpedienteForm = (ListaExpedientesForm) form;
	          
		boolean completo = true;
	    try{
	    	Expedientes expedientes = null;
	    	Expedientes nuevoExpedientes = new Expedientes();
	    	CriterioBusquedaExpedientes oCriterio = new CriterioBusquedaExpedientes();
	    	ServicioConsultaExpedientes oServicio = new ServicioConsultaExpedientes();
	    	if(request.getParameter("fechaDesde")== null && request.getParameter("estado")==null) {
	    		oCriterio.setNIF(NIF);
	    		String nombreCertificado = Misc.getNombreDesdeCertificado(request);
	    		logger.warn("USUARIO DOCUMENTACIÓN ORGANOS COLEGIADOS: "+NIF+" - "+nombreCertificado);
	    		expedientes = oServicio.consultarExpedientesMiembros(NIF, Misc.obtenerEntidad(request), nombreCertificado);
	    		//expedientes = oServicio.consultarExpedientesNIF(NIF, Misc.obtenerEntidad(request));
//	    		expedientes = GestorConsulta.consultarExpedientes(request.getSession().getId(), NIF);
	    	} else {
	    		String fechaDesdeRequest = request.getParameter("fechaDesde");
	    		String fechaHastaRequest = request.getParameter("fechaHasta");
	    		String estado = request.getParameter("estado");
	    		String fechaParseadaDesde = null;
	    		String fechaParseadaHasta = null;

	    		try {
	    			fechaParseadaDesde = parsearFecha(fechaDesdeRequest);
	    			fechaParseadaHasta = parsearFecha(fechaHastaRequest);
	    		} catch (Throwable conEx){

	    			 ActionMessages errors = new ActionMessages();
	    			 if(request.getParameter("fechaDesde") == null || request.getParameter("fechaDesde").length() < 1){
	    				 errors.add("error",new ActionMessage("formatoFechaIncorrecto"));
	    			 }
	    			 errors.add("error", new ActionMessage("formatoFechaIncorrecto"));
	    			 return mapping.findForward("Busqueda");
	    		}
	    		
	    		request.getSession().setAttribute("fechaDesde", fechaParseadaDesde);
	    		request.getSession().setAttribute("fechaDesdeBusqueda", fechaDesdeRequest);
	    		request.getSession().setAttribute("fechaHasta", fechaParseadaHasta);
	    		request.getSession().setAttribute("fechaHastaBusqueda", fechaHastaRequest);
	    		request.getSession().setAttribute("estado", estado);
	    		request.getSession().setAttribute("operadorConsulta", request.getParameter("operadorConsulta"));
	    		completo = false;
	    		oCriterio.setNIF(NIF);
	    		oCriterio.setFechaDesde(fechaParseadaDesde);
	    		oCriterio.setFechaHasta(fechaParseadaHasta);
	    		oCriterio.setOperadorConsulta(request.getParameter("operadorConsulta"));
	    		oCriterio.setEstado(estado);
	    		expedientes = oServicio.busquedaExpedientes(oCriterio, Misc.obtenerEntidad(request));
//	    		expedientes = GestorConsulta.busquedaExpedientes(request.getSession().getId(), NIF, fechaParseadaDesde, fechaParseadaHasta, request.getParameter("operadorConsulta"), estado);
	    	}
	    	

	    	listaExpedienteForm.setURLAportacion(oServicio.obtenerURLAportacionExpedientes());
    		listaExpedienteForm.setURLNotificacion(oServicio.obtenerURLNotificacionExpedientes());
//	    	listaExpedienteForm.setURLAportacion(GestorConsulta.obtenerURLAportacionExpedientes());
//    		listaExpedienteForm.setURLNotificacion(GestorConsulta.obtenerURLNotificacionExpedientes());
    		
    		if(expedientes != null){
    			for(int a=0; a<expedientes.count(); a++){
        			Expediente expediente = (Expediente)expedientes.get(a);
        			
        			//[Manu Ticket #1090] - INICIO Poner en marcha la opción Consulta de Expedientes.
        			if (expediente.getEstado().equals(Expediente.COD_ESTADO_EXPEDIENTE_FINALIZADO))
        				expediente.setEstado("cerrado");
        			else expediente.setEstado("abierto");
        			//[Manu Ticket #1090] - FIN Poner en marcha la opción Consulta de Expedientes.
        			
        			String proc = expediente.getProcedimiento();
        			if (proc.length() > 60){
        				proc = proc.substring(0,57) + "...";
        				expediente.setProcedimiento(proc);
        			}
        			      			       			
        			nuevoExpedientes.add(expediente);
        		}
    		}
    		
    		listaExpedienteForm.setExpedientes(nuevoExpedientes);
    		request.setAttribute("expedientes", nuevoExpedientes.getExpedientes());
	    } catch (Exception ex){
	    	request.setAttribute("MENSAJE_ERROR",ex.getMessage());
	    }
	    if (completo)
	    	return mapping.findForward("Success");
	    else return mapping.findForward("Success_Search");
	 }
	 
	 private String parsearFecha (String fecha) {
		 
		 String fechaParseada = null;
		 if(fecha != null && !fecha.equals("")){
			 try {
			 String[] porcionesFecha = fecha.split("-");
			 fechaParseada = porcionesFecha[2] + "-" + porcionesFecha[1] + "-" + porcionesFecha[0];
			 } catch (java.lang.IndexOutOfBoundsException ex) {
				 throw ex;
			 }
		 }
		 return fechaParseada;
	 }

}

package es.dipucr.tecdoc.sgm.ct.struts.action;

import ieci.tdw.ispac.api.impl.InvesflowAPI;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.consulta.Actas;
import ieci.tecdoc.sgm.ct.ServicioConsultaActaAudio;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.dipucr.tecdoc.sgm.ct.struts.form.ActaForm;
import es.dipucr.tecdoc.sgm.ct.utilities.Misc;

public class ListadoActasAudioAction extends ConsultaWebMiembroAction {
	
	private static final Logger logger = Logger.getLogger(ListadoActasAudioAction.class);

	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response) {
		String NIF = "";
	 	String sessionId = "";
	 	
	 	ActaForm actaForm = (ActaForm) form;
	 	boolean completo = true;
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
			 completo = false;
		 }
		
	    try{

	    	ServicioConsultaActaAudio oServicio = new ServicioConsultaActaAudio();
	    	Actas actas = oServicio.obtenerActas(NIF, Misc.obtenerEntidad(request));
	    	actaForm.setActas(actas);
	    	
	    } catch (Exception ex){
	    	request.setAttribute("MENSAJE_ERROR",ex.getMessage());
	    	completo = false;
	    }
	    if (completo)
	    	return mapping.findForward("Success");
	    else return mapping.findForward("Success_Search");
		

	}

}

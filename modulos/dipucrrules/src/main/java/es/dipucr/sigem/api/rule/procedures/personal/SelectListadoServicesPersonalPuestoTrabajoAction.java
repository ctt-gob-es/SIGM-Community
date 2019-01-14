package es.dipucr.sigem.api.rule.procedures.personal;


import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.dipucr.webempleado.domain.beans.PuestoComplementos;
import es.dipucr.webempleado.model.mapping.Puestos;
import es.dipucr.webempleado.model.mapping.Servicios;
import es.dipucr.webempleado.services.personal.PersonalWSProxy;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.ispaclib.bean.ItemBean;
import ieci.tdw.ispac.ispacmgr.action.SelectSubstituteAction;


public class SelectListadoServicesPersonalPuestoTrabajoAction  extends SelectSubstituteAction
{
	  //private Logger logger = Logger.getLogger(SelectListadoCodicePliegoAction.class);

	public ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, SessionAPI session)
	    throws Exception
	  {
    
	    ArrayList<ItemBean> vect = new ArrayList<ItemBean>();
	    
	    String campo = request.getParameter("campo");
	    String departamento = request.getParameter("departamento");
	    String year = request.getParameter("anyo");

		PersonalWSProxy personal = new PersonalWSProxy();
		
		if(campo!=null){
			if(campo.equals("SERVICIOS")){
				Servicios [] servicios = personal.getServiciosByCodigo();		
				for (int i = 0; i < servicios.length; i++) {			
					Servicios servicio = servicios[i];
					String key = servicio.getCodServ();
					String value = servicio.getNomServ();
					LOGGER.warn("key "+key+" value "+value);
					ItemBean itemB = new ItemBean();
					itemB.setProperty("VALOR", key);
					itemB.setProperty("SUSTITUTO", key+" - "+value);
					vect.add(itemB);
				}
			}
			if(campo.equals("PUESTO")){
				if(departamento!=null){
					String [] vDepart = departamento.split(" - ");
					String codDep = "";
					if(vDepart.length >1){
						codDep = vDepart[0];
					}
					PuestoComplementos[] vPuestos =  personal.getPuestosComplementosVacantes(codDep, year);
					//Puestos [] vPuestos = personal.getPuestos(codDep);
					for (int i = 0; vPuestos!=null && i < vPuestos.length; i++) {			
						PuestoComplementos puestoComplemento = vPuestos[i];
						Puestos puesto = puestoComplemento.getDatosPuesto();
						String key = puestoComplemento.getPuesto().getId().getP2Ord();
						String value = puesto.getNCompletoPto();
						LOGGER.warn("key "+key+" value "+value);
						ItemBean itemB = new ItemBean();
						itemB.setProperty("VALOR", key);
						itemB.setProperty("SUSTITUTO", key+" - "+value + " - "+puestoComplemento.getPuesto().getP2Sit() +" - "+ puestoComplemento.getEmpleado().getP0NomHaci());
						vect.add(itemB);
					}
					request.setAttribute("setAction", "setSubstitutePersonal.do");
					request.setAttribute("SERVICIO", codDep);
					request.setAttribute("YEAR", year);
				}
				
			}
			/*if(campo.equals("GRUPO")){
				String [] servicios = personal.getGrupos();		
				for (int i = 0; i < servicios.length; i++) {			
					logger.warn("key "+servicios[i]+" value "+servicios[i]);
					ItemBean itemB = new ItemBean();
					itemB.setProperty("VALOR", servicios[i]);
					itemB.setProperty("SUSTITUTO", servicios[i]);
					vect.add(itemB);
				}
			}
			if(campo.equals("PROVISION")){
				Provision[] vProvision = personal.getProvisiones();
				for (int i = 0; i < vProvision.length; i++) {			
					Provision provision = vProvision[i];
					String key = provision.getCProvi();
					String value = provision.getNProvi();
					logger.warn("key "+key+" value "+value);
					ItemBean itemB = new ItemBean();
					itemB.setProperty("VALOR", key);
					itemB.setProperty("SUSTITUTO", key+" - "+value);
					vect.add(itemB);
				}
			}
			
			if(campo.equals("COMP_ESP")){
				if(departamento!=null && year!=null){
					String [] vDepart = departamento.split(" - ");
					String codDep = "";
					if(vDepart.length >1){
						codDep = vDepart[0];
					}
					PuestoComplementos [] vPuestosComple = personal.getPuestosComplementos(codDep, year);
					for (int i = 0; vPuestosComple!=null && i < vPuestosComple.length; i++) {			
						PuestoComplementos puesto = vPuestosComple[i];						
						String key = puesto.getCompEspecifico()+"";
						String value = puesto.getCompEspecifico()+"";
						double enteroComplemento = 0.0;
						if(!complemento.equals("")){
							enteroComplemento = Double.parseDouble(complemento);
						}
						if(complemento!=null && (enteroComplemento==puesto.getCompDestino() || complemento.equals(""))){
							logger.warn("key "+key+" value "+value);
							ItemBean itemB = new ItemBean();
							itemB.setProperty("VALOR", key);
							itemB.setProperty("SUSTITUTO", value);
							vect.add(itemB);
						}
						
					}
				}	
			}
			if(campo.equals("COMP_DEST")){
				if(departamento!=null && year!=null){
					String [] vDepart = departamento.split(" - ");
					String codDep = "";
					if(vDepart.length >1){
						codDep = vDepart[0];
					}
					PuestoComplementos [] vPuestosComple = personal.getPuestosComplementos(codDep, year);
					for (int i = 0; vPuestosComple!=null && i < vPuestosComple.length; i++) {			
						PuestoComplementos puesto = vPuestosComple[i];						
						String key = puesto.getCompDestino()+"";
						String value = puesto.getCompDestino()+"";
						double enteroComplemento = 0.0;
						if(!complemento.equals("")){
							enteroComplemento = Double.parseDouble(complemento);
						}
								
						if(complemento!=null && (enteroComplemento==puesto.getCompEspecifico() || complemento.equals(""))){
							logger.warn("key "+key+" value "+value);
							ItemBean itemB = new ItemBean();
							itemB.setProperty("VALOR", key);
							itemB.setProperty("SUSTITUTO", value);
							vect.add(itemB);
						}
					}
				}
			}

			if(campo.equals("JORNADA")){
				if(year!=null){
					
					HorariosId[] vJornada = personal.getHorarios(year);
					for (int i = 0; vJornada!=null && i < vJornada.length; i++) {		
						HorariosId jornada = vJornada[i];
						String key = jornada.getTurnos();
						String value = jornada.getDescripcion();
						logger.warn("key "+key+" value "+value);
						ItemBean itemB = new ItemBean();
						itemB.setProperty("VALOR", key);
						itemB.setProperty("SUSTITUTO", key+" - "+value);
						vect.add(itemB);
					}
				}
				
			}*/
		}	
		
	    LOGGER.warn("tamaño. "+vect.size());
	    List<ItemBean> list = vect.subList(0, vect.size());
	    request.setAttribute("SubstituteList", list);
		processFormatter(request, "/digester/subtitutoformatter.xml");
		
		return mapping.findForward("success");
	  }	  	  
}

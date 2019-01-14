package es.dipucr.sigem.api.rule.procedures.personal;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.dipucr.webempleado.domain.beans.PuestoComplementos;
import es.dipucr.webempleado.model.mapping.Categorias;
import es.dipucr.webempleado.model.mapping.Clases;
import es.dipucr.webempleado.model.mapping.Escalas;
import es.dipucr.webempleado.model.mapping.HorariosId;
import es.dipucr.webempleado.model.mapping.Provision;
import es.dipucr.webempleado.model.mapping.Subescalas;
import es.dipucr.webempleado.model.mapping.TipoP;
import es.dipucr.webempleado.services.personal.PersonalWSProxy;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.ispaclib.bean.ItemBean;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispacmgr.action.SetSubstituteAction;

public class SetListadoPuestoTrabajoAction extends SetSubstituteAction {

	public ActionForward executeAction(ActionMapping mapping,
									   ActionForm form,
									   HttpServletRequest request,
									   HttpServletResponse response,
									   SessionAPI session) throws Exception {
				
		// Cogemos los valores del bean
		String valor = request.getParameter("value");
		String sustituto = request.getParameter("sustituto");
		String servicio = request.getParameter("servicio");
		String year = request.getParameter("year");
		
		ItemBean item = new ItemBean();
		item.setProperty("VALOR", valor);
		item.setProperty("SUSTITUTO", sustituto);
		request.setAttribute("Value", item);
		
		if(!StringUtils.isEmpty(valor) && !StringUtils.isEmpty(servicio) && !StringUtils.isEmpty(year)){
			
			PersonalWSProxy personal = new PersonalWSProxy();
			PuestoComplementos vPuestosComp =  personal.getPuestoComplemento(servicio, valor, year);
			
			String grupo = "";
			if(StringUtils.isNotEmpty(vPuestosComp.getPuesto().getP2Gru())){
				grupo = vPuestosComp.getPuesto().getP2Gru();
			}
			String formaProvision = "";
			if(StringUtils.isNotEmpty(vPuestosComp.getPuesto().getP2Prov())){
				Provision provision = personal.getProvision(vPuestosComp.getPuesto().getP2Prov());
				formaProvision = provision.getCProvi() +" - "+ provision.getNProvi();
			}
			
			String complementoEspecifico = "";
			if(vPuestosComp.getPuesto().getP2Esp().doubleValue()>0){
				//Del complemento específico de los 3 que hay, cogemos el primero.
				//complementoEspecifico = vPuestosComp.getPuesto().getP2Esp()+" - "+vPuestosComp.getPuesto().getP2EspVal()+" - "+vPuestosComp.getPuesto().getP2Cpt();//Pregunta??
				complementoEspecifico = vPuestosComp.getPuesto().getP2Esp()+"";
			}
			
			String complementoDestino = "";
			if(vPuestosComp.getCompDestino()>0){
				complementoDestino = vPuestosComp.getCompDestino()+"";
			}

			String nivelEspecifico = "";
			if(StringUtils.isNotEmpty(vPuestosComp.getPuesto().getP2Niv())){
				nivelEspecifico = vPuestosComp.getPuesto().getP2Niv();
			}						

			String tipoPuesto = "";
			if(StringUtils.isNotEmpty(vPuestosComp.getPuesto().getId().getP2Tip())){
				TipoP tipoP = personal.getTipoPuesto(vPuestosComp.getPuesto().getId().getP2Tip());
				if(tipoP!=null && StringUtils.isNotEmpty(tipoP.getCodigo()) && StringUtils.isNotEmpty(tipoP.getDenominacion())){
					tipoPuesto = tipoP.getCodigo() +" - "+tipoP.getDenominacion();
				}
			}
			
			String escala = "";
			if(StringUtils.isNotEmpty(vPuestosComp.getPuesto().getP2Esca1())){
				Escalas escalas = personal.getEscala(vPuestosComp.getPuesto().getP2Esca1());
				if(escalas!=null && StringUtils.isNotEmpty(escalas.getCEsc()) && StringUtils.isNotEmpty(escalas.getNEsc())){
					escala = escalas.getCEsc() +" - "+escalas.getNEsc();
				}
			}

			String subescala =  "";
			if(StringUtils.isNotEmpty(vPuestosComp.getPuesto().getP2Sesc1())){
				Subescalas subescalas = personal.getSubescala(vPuestosComp.getPuesto().getP2Sesc1());
				if(subescalas!=null && StringUtils.isNotEmpty(subescalas.getCSesc()) && StringUtils.isNotEmpty(subescalas.getNSesc())){
					subescala = subescalas.getCSesc() +" - "+subescalas.getNSesc();
				}

			}
			
			String clase = "";
			if(StringUtils.isNotEmpty(vPuestosComp.getPuesto().getP2Clase1())){
				Clases clases = personal.getClase(vPuestosComp.getPuesto().getP2Clase1());
				if(clases!=null && StringUtils.isNotEmpty(clases.getCClase()) && StringUtils.isNotEmpty(clases.getNClase())){
					clase = clases.getCClase()+" - "+clases.getNClase();
				}				
			}
			
			String categoria = "";
			if(StringUtils.isNotEmpty(vPuestosComp.getPuesto().getP2Categ1())){
				Categorias categorias = personal.getCategoria(vPuestosComp.getPuesto().getP2Categ1());
				if(categorias!=null && StringUtils.isNotEmpty(categorias.getCCateg()) && StringUtils.isNotEmpty(categorias.getNCateg())){
					categoria = categorias.getCCateg() +" - "+categorias.getNCateg();
				}				
			}
			
			String titulacion = "";
			if(StringUtils.isNotEmpty(vPuestosComp.getPuesto().getP2Titu())){
				titulacion = vPuestosComp.getPuesto().getP2Titu();
			}
			String tipoJornada = "";
			if(vPuestosComp.getHorario()!=null && vPuestosComp.getHorario().getId()!=null ){
				if(vPuestosComp.getHorario().getId().getTipoHorario()>0){
					HorariosId horario = personal.getTipoHorario(vPuestosComp.getHorario().getId().getTipoHorario()+"", year);
					if(horario!=null && StringUtils.isNotEmpty(horario.getDescripcion())){
						tipoJornada = vPuestosComp.getHorario().getId().getTipoHorario() +" - "+ horario.getDescripcion();
					}
					
				}	
			}				
			
			
			request.setAttribute("GRUPO", grupo);
			request.setAttribute("PROVISION", formaProvision);
			request.setAttribute("COMP_ESP", complementoEspecifico);
			request.setAttribute("COMP_DEST", complementoDestino);
			request.setAttribute("NIVEL_ESP", nivelEspecifico);
			request.setAttribute("TIPO_PUESTO", tipoPuesto);
			request.setAttribute("ESCALA", escala);
			request.setAttribute("SUBESCALA", subescala);
			request.setAttribute("CLASE", clase);
			request.setAttribute("CATEGORIA", categoria);
			request.setAttribute("TITULACION", titulacion);
			request.setAttribute("JORNADA", tipoJornada);
		}		

		return mapping.findForward("success");
	}
	
}
package es.dipucr.sigem.importarParticipantes.action;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.ISecurityAPI;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IProcess;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispacmgr.action.BatchSignAction;
import ieci.tdw.ispac.ispacweb.api.IManagerAPI;
import ieci.tdw.ispac.ispacweb.api.IState;
import ieci.tdw.ispac.ispacweb.api.ManagerAPIFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;
import es.dipucr.sigem.api.rule.procedures.Constants;
import es.dipucr.sigem.importarParticipantes.bean.ImportarParticipantesBean;

public class CopiarParticipantesAction extends BatchSignAction{

	public ActionForward importarParticipantes(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {

			ClientContext cct = session.getClientContext();			
		
			ImportarParticipantesBean importarParticipantesBean = (ImportarParticipantesBean) form;			
			String expedienteCuyosParticipantesImportamos= importarParticipantesBean.getExpedienteDestino();
						
			//MQE Si nos devuelve aunque sea una fase, sí es responsable del expediente, si nos devuelve null, no lo es
			boolean isResponsible = false;
			try{
				//MQE modificaciones ticket #242 No se pueden importar participantes de expedietnes cerrados
//				isResponsible = (cct.getAPI().getWorkListAPI().getStage(expedienteCuyosParticipantesImportamos)==null?false:true);
				IProcess process = cct.getAPI().getProcess(expedienteCuyosParticipantesImportamos);
				String sUID = process.getString("ID_RESP");
				isResponsible = cct.getAPI().getWorkListAPI().isInResponsibleList(sUID, ISecurityAPI.SUPERV_ANY);
				//MQE Fin modificaciones ticket #242
			}
			catch(Exception e){
				//MQE si devuelve una excepción no copiamos los participantes
				isResponsible=false;
			}
			if(!isResponsible){
				return mapping.findForward("failure");
			}
			else{				
				IEntitiesAPI entitiesAPI = session.getAPI().getEntitiesAPI();
				IManagerAPI managerAPI = ManagerAPIFactory.getInstance().getManagerAPI(cct);
			    IState state = managerAPI.currentState(getStateticket(request));				
				
				IItemCollection participantes = ParticipantesUtil.getParticipantes(cct, expedienteCuyosParticipantesImportamos);
				
				IItem nuevoParticipante = null;
				String id_ext = "";
		        String dni = "";
		        String nombre = "";
	        	IItemCollection participantes2 = null;
				boolean existe = false;

			    for(Object part : participantes.toList()){
			    	IItem participante = (IItem) part;
			    	nuevoParticipante = entitiesAPI.createEntity(Constants.TABLASBBDD.SPAC_DT_INTERVINIENTES, state.getNumexp());

			        //MQE Comprobamos que no exista ya el participante
			        id_ext = ((IItem)part).getString("ID_EXT");
			        dni = ((IItem)part).getString("NDOC");
			        nombre = ((IItem)part).getString("NOMBRE");
		        	participantes2 = ParticipantesUtil.getParticipantes(cct, state.getNumexp());
					existe = false;
									
					for (Object participante2 : participantes2.toList()){
						//MQE si el id_ext es nulo lo consideramos como que no existe
						if(id_ext != null){
							//MQE si el id_ext es igual es el mismo participante
							if(((IItem)participante2).getString("ID_EXT")!= null && ((IItem)participante2).getString("ID_EXT").equals(id_ext)) existe = true;
						}
						//MQE si el dni es nulo lo consideramos como que no existe
						if(dni != null){
							//MQE si el dni es igual es el mismo participante
							if(((IItem)participante2).getString("NDOC")!= null && ((IItem)participante2).getString("NDOC").equals(dni)) existe = true;
						}
						//MQE si el nombre es igual es el mismo participante
						if(((IItem)participante2).getString("NOMBRE")!= null && ((IItem)participante2).getString("NOMBRE").toUpperCase().equals(nombre.toUpperCase())) existe = true;										
					}//Fin while it2
					if(!existe){
			            try{
			            	nuevoParticipante.set("ID_EXT", participante.getString("ID_EXT"));
			            	nuevoParticipante.set("ROL", participante.getString("ROL"));
			            	nuevoParticipante.set("TIPO", participante.getString("TIPO"));
			            	nuevoParticipante.set("TIPO_PERSONA", participante.getString("TIPO_PERSONA"));
			            	nuevoParticipante.set("NDOC", participante.getString("NDOC"));
			            	nuevoParticipante.set("NOMBRE", participante.getString("NOMBRE"));
			            	nuevoParticipante.set("DIRNOT", participante.getString("DIRNOT"));
			            	nuevoParticipante.set("EMAIL", participante.getString("EMAIL"));
			            	nuevoParticipante.set("C_POSTAL", participante.getString("C_POSTAL"));
			            	nuevoParticipante.set("LOCALIDAD", participante.getString("LOCALIDAD"));
			            	nuevoParticipante.set("CAUT", participante.getString("CAUT"));
			            	nuevoParticipante.set("TFNO_FIJO", participante.getString("TFNO_FIJO"));
			            	nuevoParticipante.set("TFNO_MOVIL", participante.getString("TFNO_MOVIL"));
			            	nuevoParticipante.set("TIPO_DIRECCION", participante.getString("TIPO_DIRECCION"));
			            	nuevoParticipante.set("DIRECCIONTELEMATICA", participante.getString("DIRECCIONTELEMATICA"));
			            	nuevoParticipante.set("IDDIRECCIONPOSTAL", participante.getString("IDDIRECCIONPOSTAL"));
			            	nuevoParticipante.set("RECURSO", participante.getString("RECURSO"));
			            	nuevoParticipante.set("OBSERVACIONES", participante.getString("OBSERVACIONES"));
			            	nuevoParticipante.set("ASISTE", participante.getString("ASISTE"));
			            	nuevoParticipante.set("TIPO_PODER", participante.getString("TIPO_PODER"));
			            	nuevoParticipante.set("FECHA_INI", participante.getString("FECHA_INI"));
			            	nuevoParticipante.set("FECHA_FIN", participante.getString("FECHA_FIN"));
			            	nuevoParticipante.set("SOLICITAR_OFERTA", participante.getString("SOLICITAR_OFERTA"));
			            	nuevoParticipante.set("CCC", participante.getString("CCC"));
			            	
			            	nuevoParticipante.store(cct);
			            }
			            catch(Exception e){
			            	logger.error("Error al guardar el participante con id: " + id_ext + ". " + e.getMessage(), e);
			            }
					}//fin si !existe
		        }//Fin while it
			}//Fin si no es responsable
			return mapping.findForward("success");
	}

	public ActionForward refrescar(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {		
		return mapping.findForward("volver");	
	}

	public ActionForward expedienteAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session)	throws Exception {
		return mapping.findForward("expedienteImportar");
	}
}

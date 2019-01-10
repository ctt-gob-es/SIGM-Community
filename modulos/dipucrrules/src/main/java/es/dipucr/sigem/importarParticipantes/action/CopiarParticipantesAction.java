package es.dipucr.sigem.importarParticipantes.action;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.ISecurityAPI;
import ieci.tdw.ispac.api.impl.SessionAPI;
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
import es.dipucr.sigem.importarParticipantes.bean.ImportarParticipantesBean;

public class CopiarParticipantesAction extends BatchSignAction{

	public ActionForward importarParticipantes(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {

			ClientContext cct = session.getClientContext();			
		
			ImportarParticipantesBean importarParticipantesBean = (ImportarParticipantesBean) form;			
			String expOrigen= importarParticipantesBean.getExpedienteDestino();
						
			//MQE Si nos devuelve aunque sea una fase, sí es responsable del expediente, si nos devuelve null, no lo es
			boolean isResponsible = false;
			try{
				//MQE modificaciones ticket #242 No se pueden importar participantes de expedietnes cerrados
//				isResponsible = (cct.getAPI().getWorkListAPI().getStage(expedienteCuyosParticipantesImportamos)==null?false:true);
				IProcess process = cct.getAPI().getProcess(expOrigen);
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
			    
			    String expDestino = state.getNumexp();
			    ParticipantesUtil.importarParticipantes(cct, entitiesAPI, expOrigen, expDestino); //[dipucr-Felipe #780]
				
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

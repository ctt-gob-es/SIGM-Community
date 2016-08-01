package es.dipucr.sigem.api.action;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.app.EntityApp;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispacmgr.action.BaseAction;
import ieci.tdw.ispac.ispacmgr.action.form.EntityForm;
import ieci.tdw.ispac.ispacweb.api.IManagerAPI;
import ieci.tdw.ispac.ispacweb.api.IState;
import ieci.tdw.ispac.ispacweb.api.ManagerAPIFactory;
import ieci.tdw.ispac.ispacweb.api.ManagerState;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.dipucr.sigem.api.rule.procedures.Constants;

public class DeleteCompareceAction extends BaseAction {

    public ActionForward executeAction(ActionMapping mapping,
    								   ActionForm form,
    								   HttpServletRequest request,
    								   HttpServletResponse response,
    								   SessionAPI session) throws Exception {
    	
    	ClientContext cct = session.getClientContext();
    	// Estado del contexto de tramitación
		IManagerAPI managerAPI = ManagerAPIFactory.getInstance().getManagerAPI(cct);
    	IState currentstate = managerAPI.currentState(getStateticket(request));
    	IInvesflowAPI invesFlowAPI = session.getAPI();
    	IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
		
    	int entityId = currentstate.getEntityId();
		//int keyId = currentstate.getEntityRegId();
    	int regId=cct.getStateContext().getKey();
    	

		// Ejecución en un contexto transaccional
		boolean bCommit = false;
	
		try {

			// Iniciar transacción
			cct.beginTX();
			
			// Formulario asociado a la acción
			EntityForm defaultForm = (EntityForm) form;
			
			String dni = defaultForm.getProperty("SPAC_DT_INTERVINIENTES:NDOC");
			
			//Eliminar los registros de la tabla dpcr_participantes_comparece
			String strQuery = "WHERE IDENT_PARTICIPANTE=" + regId;
			entitiesAPI.deleteEntities(Constants.TABLASBBDD.DPCR_PARTICIPANTES_COMPARECE, strQuery);
			
			
			
			//Tenemos que eliminar tb de la entidad spac_dt_documentos los docs que pudieran estar asociados a regId y entityId
			
			//Obtener todos los items de tipo documento que estan asociados a la entidad y al registro de la entidad
			IItemCollection itemcol= entitiesAPI.queryEntities(SpacEntities.SPAC_DT_DOCUMENTOS, " where id_entidad="+entityId +" and id_reg_entidad="+regId);
			//Comprobamos que no exista ningún documento bloqueado, en caso de que exista no permitiremos eliminar el registro de la entidad
		
			while (itemcol.next()) {
				
				entitiesAPI.deleteDocumentFromRegEntity(itemcol.value(), false);
				
			}

			EntityApp entityapp = defaultForm.getEntityApp();
			entityapp.delete();

			// Si todo ha sido correcto se hace commit de la transacción
			bCommit = true;
			
		}
		finally{
			cct.endTX(bCommit);
		}
		
   		String displayTagOrderParams = getDisplayTagOrderParams(request);
   		if (!StringUtils.isEmpty(displayTagOrderParams)) {
   			displayTagOrderParams = "&" + displayTagOrderParams;
   		}
		
		// Establecer el retorno
		String action = "/showExpedient.do";
		String params = "?entity=" + entityId;
		
		if (currentstate.getState() == ManagerState.TASK) {
			
			action = "/showTask.do";
			
			if (request.getParameter("taskId") != null) {
				params += "&taskId=" + request.getParameter("taskId");
			}
			if (request.getParameter("numexp") != null) {
				params += "&numexp=" + request.getParameter("numexp");
			}
		}
		else if (currentstate.getState() == ManagerState.SUBPROCESS) {
			
			action = "/showSubProcess.do";
			
			if (request.getParameter("taskId") != null) {
				params += "&taskId=" + request.getParameter("taskId");
			}
			if (request.getParameter("activityId") != null) {
				params += "&activityId=" + request.getParameter("activityId");
			}
		}
		else {
			if (request.getParameter("stageId") != null) {
				params += "&stageId=" + request.getParameter("stageId");
			}
		}
		
		if (request.getParameter("form") != null) {
			params += "&form=" + request.getParameter("form");
		}
		params += displayTagOrderParams;

		ActionForward actionForward = new ActionForward();
		actionForward.setPath(action + params);
		actionForward.setRedirect(true);

		
		
		return actionForward;
    }



}

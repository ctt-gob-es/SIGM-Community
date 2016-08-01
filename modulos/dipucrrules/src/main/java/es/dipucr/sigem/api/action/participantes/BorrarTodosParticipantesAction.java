package es.dipucr.sigem.api.action.participantes;

import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispacmgr.action.BatchSignAction;
import ieci.tdw.ispac.ispacweb.api.IManagerAPI;
import ieci.tdw.ispac.ispacweb.api.IState;
import ieci.tdw.ispac.ispacweb.api.ManagerAPIFactory;
import ieci.tdw.ispac.ispacweb.api.ManagerState;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;

public class BorrarTodosParticipantesAction extends BatchSignAction{

	@SuppressWarnings("rawtypes")
	public ActionForward borrarParticipantes(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {

			ClientContext cct = session.getClientContext();			
	
			IManagerAPI managerAPI = ManagerAPIFactory.getInstance().getManagerAPI(cct);
		    IState state = managerAPI.currentState(getStateticket(request));				

		    IState currentstate = managerAPI.currentState(getStateticket(request));
		    int entityId = currentstate.getEntityId();
		    
		    IItemCollection participantesCollection = ParticipantesUtil.getParticipantes(cct, state.getNumexp(), "", "");
		    Iterator participantesIterator = participantesCollection.iterator();		        

		    while (participantesIterator.hasNext()){
				IItem participante = (IItem)participantesIterator.next();
				participante.delete(cct);
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

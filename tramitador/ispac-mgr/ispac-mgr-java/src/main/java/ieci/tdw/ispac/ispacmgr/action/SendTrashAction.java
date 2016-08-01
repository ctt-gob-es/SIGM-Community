package ieci.tdw.ispac.ispacmgr.action;

import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IStage;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispacweb.api.IManagerAPI;
import ieci.tdw.ispac.ispacweb.api.IState;
import ieci.tdw.ispac.ispacweb.api.ManagerAPIFactory;
import ieci.tdw.ispac.ispacweb.context.NextActivity;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class SendTrashAction extends BaseAction {

	
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {


		ClientContext cct = session.getClientContext();
		// API de invesFlow
		IInvesflowAPI invesflowAPI =cct.getAPI();
		IManagerAPI managerAPI = ManagerAPIFactory.getInstance().getManagerAPI(cct);
		IState state = managerAPI.currentState(getStateticket(request));
		//Si tenemos el parámetro idsStage quiere decir que estamos en el listado de expedientes de un procedimiento
		//en una misma fase
		String idsStage = request.getParameter("idsStage");
		
		if (StringUtils.isNotBlank(idsStage)){
			int nidstage = 0;
			int nIdProcess = 0;
			String[] stageids = idsStage.split("-");
			
			boolean bContinuar = true; //[dipucr-Felipe #226]
			for (int i = 0; i < stageids.length && bContinuar; i++) {
				IStage stage = null;
				nidstage = Integer.parseInt(stageids[i]);
				stage = invesflowAPI.getStage(nidstage);				
				nIdProcess = stage.getInt("ID_EXP");
				
				//INICIO [dipucr-Felipe #226]
				if (!invesflowAPI.getTransactionAPI().sendProcessToTrash(nIdProcess)){
					showBlockedDocumentsMessage(request);
					bContinuar = false;
				}
				//FIN [dipucr-Felipe #226]
			}
			return NextActivity.refresh(request, mapping, state);
		}
	
		else{
			//INICIO [dipucr-Felipe #226]
			if (!invesflowAPI.getTransactionAPI().sendProcessToTrash(state.getProcessId())){
				showBlockedDocumentsMessage(request);
		    	ActionForward showexp =mapping.findForward("showexp");		
				return new ActionForward(showexp.getName(), showexp.getPath() + "?stageId=" + state.getStageId(), true);
			}
			//FIN [dipucr-Felipe #226]
		}
	
		if (logger.isInfoEnabled()) {
			logger.info("Expediente [" 
					+ cct.getStateContext().getNumexp() + "] enviado a la papelera");
		}

		return mapping.findForward("success");
	}

	/**
	 * [dicpur-Felipe #226]
	 * @param request
	 */
	private void showBlockedDocumentsMessage(HttpServletRequest request) {
		ISPACInfo info=null;
		info=new ISPACInfo(getResources(request).getMessage("exception.exception.sendtrash"));
		request.getSession().setAttribute("infoAlert", info);
	}
		

}

package es.dipucr.ispacmgr.action;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInboxAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.sicres.vo.Intray;
import ieci.tdw.ispac.ispacmgr.action.BaseAction;
import ieci.tdw.ispac.ispacweb.api.IManagerAPI;
import ieci.tdw.ispac.ispacweb.api.IState;
import ieci.tdw.ispac.ispacweb.api.ManagerAPIFactory;
import ieci.tdw.ispac.ispacweb.context.NextActivity;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.dipucr.sigem.api.rule.common.utils.RegistrosDistribuidosUtil;

public class CreateExpedientAddDoc  extends BaseAction {

	@SuppressWarnings("rawtypes")
	@Override
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {
		
		/****************************************************************/
		IInvesflowAPI invesflowAPI = session.getAPI();
    	IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
    	ClientContext cct = (ClientContext) session.getClientContext();
    	IInboxAPI inbox = invesflowAPI.getInboxAPI();
    	/****************************************************************/
    	
    	// Identificador del registro distribuido
		String register = request.getParameter("register");
    	// Numero de expediente
		String numexpRegistro = request.getParameter("numexp");
    	
    	//Calcula el procedimiento apartir del tipo de asunto.
		Intray intray = inbox.getIntray(register);
				
    	//Spac_fases
		//Obtiene el número de fase 
		String strQueryAux = "WHERE NUMEXP='" + numexpRegistro + "'";
		IItemCollection collExpsAux = entitiesAPI.queryEntities("SPAC_FASES", strQueryAux);
		if(collExpsAux.toList().size() != 0){
			Iterator itExpsAux = collExpsAux.iterator();
			IItem iExpedienteAux = ((IItem)itExpsAux.next());
			String idFase = iExpedienteAux.getString("ID_FASE_BPM");
			
			//Almacenar los documentos en el trámite de subsanación.
			// Adjuntar los documentos del registro distribuido al expediente
			RegistrosDistribuidosUtil.addDocuments(cct, numexpRegistro, intray);
			
			ISPACInfo informacion=new ISPACInfo("Agregado el/los documento/s al expediente a subsanar", "",false);
			request.getSession().setAttribute("infoAlert", informacion);
			
			ActionForward actionForward = new ActionForward();
			String action = "/showExpedient.do";
			String params = "?stageId="+idFase;
			actionForward.setPath(action + params);
			actionForward.setRedirect(true);
			return actionForward;
		}
		else{
			ISPACInfo informacion=new ISPACInfo("El número de expediente insertado no existe o es erróneo", "",false);
			request.getSession().setAttribute("infoAlert", informacion);
			IManagerAPI managerAPI = ManagerAPIFactory.getInstance().getManagerAPI(cct);
			IState currentstate = managerAPI.currentState(getStateticket(request));
			return NextActivity.refresh(request, mapping, currentstate);
		}

	}

}

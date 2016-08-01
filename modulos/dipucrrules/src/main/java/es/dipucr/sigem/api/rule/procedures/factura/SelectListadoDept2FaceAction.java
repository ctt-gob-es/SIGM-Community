package es.dipucr.sigem.api.rule.procedures.factura;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.ITask;
import ieci.tdw.ispac.ispaclib.bean.ItemBean;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispacmgr.action.SelectSubstituteAction;
import ieci.tdw.ispac.ispacweb.api.IManagerAPI;
import ieci.tdw.ispac.ispacweb.api.IState;
import ieci.tdw.ispac.ispacweb.api.ManagerAPIFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class SelectListadoDept2FaceAction extends SelectSubstituteAction {
	
	protected int MAX_TBL_SEARCH_VALUES_DEFAULT = 50;
	
	public ActionForward executeAction(ActionMapping mapping, 
									   ActionForm form,
									   HttpServletRequest request,
									   HttpServletResponse response,
									   SessionAPI session) throws Exception {

		//Creación de los API
    	ClientContext cct = session.getClientContext();
        IInvesflowAPI invesflowAPI = session.getAPI();
        IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
        IManagerAPI managerAPI = ManagerAPIFactory.getInstance().getManagerAPI(cct);
    	
        //Recuperamos el número de expediente del anuncio
    	IState state = managerAPI.currentState(getStateticket(request));
        int idTask = state.getTaskId();
        
        //Trámite
        ITask task = invesflowAPI.getTask(idTask);
        String sIdRespTramite = task.getString("ID_RESP");
        
        //Obtenemos el grupo seleccionado
        String strQuery = "WHERE SUSTITUTO = '" + sIdRespTramite + "'";
		IItemCollection collection = entitiesAPI.queryEntities("EFACIL_VLDTBL_CIRCUITOS", strQuery);
		IItem itemCircuitoDept = (IItem) collection.iterator().next();
		String sIdServicio = itemCircuitoDept.getString("VALOR");
		
		strQuery = "WHERE VALOR LIKE '" + sIdServicio + "%'";
		collection = entitiesAPI.queryEntities("EFACIL_VLDTBL_DEPT_2", strQuery);
		
		ArrayList<ItemBean> vect = new ArrayList<ItemBean>();
		IItem itemDept = null;
		@SuppressWarnings("rawtypes")
		Iterator it = collection.iterator();
		
		while (it.hasNext()){
			
			itemDept = (IItem) it.next();
			ItemBean itembean = new ItemBean();
			itembean.setProperty("VALOR", itemDept.getString("VALOR"));
			itembean.setProperty("SUSTITUTO", itemDept.getString("SUSTITUTO"));
			vect.add(itembean);
		}

		@SuppressWarnings("rawtypes")
		List list = vect.subList(0, vect.size());
		request.setAttribute("SubstituteList", list);
				
		// Obtener formateador
		processFormatter(request, "/digester/substituteformatter.xml");
		
		return mapping.findForward("success");
	}
	
}
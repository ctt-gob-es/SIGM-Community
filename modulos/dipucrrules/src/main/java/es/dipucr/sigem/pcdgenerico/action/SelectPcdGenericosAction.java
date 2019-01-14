package es.dipucr.sigem.pcdgenerico.action;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.bean.ItemBean;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispacmgr.action.SelectSubstituteAction;
import ieci.tdw.ispac.ispacweb.api.IManagerAPI;
import ieci.tdw.ispac.ispacweb.api.IState;
import ieci.tdw.ispac.ispacweb.api.ManagerAPIFactory;

import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class SelectPcdGenericosAction extends SelectSubstituteAction {
	
	protected int MAX_TBL_SEARCH_VALUES_DEFAULT = 50;
	
	public ActionForward executeAction(ActionMapping mapping, 
									   ActionForm form,
									   HttpServletRequest request,
									   HttpServletResponse response,
									   SessionAPI session) throws Exception {

		//Obtenemos el tipo de materia/competencia del procedimiento (si la hubiera)
		IClientContext cct = session.getClientContext();
		IManagerAPI managerAPI = ManagerAPIFactory.getInstance().getManagerAPI((ClientContext)cct);
		IState state = managerAPI.currentState(getStateticket(request));
		String numexp = state.getNumexp();
		
		IInvesflowAPI invesflowAPI = cct.getAPI();
		IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();
		IProcedureAPI pcdAPI = invesflowAPI.getProcedureAPI();
		IItem itemExpediente = entitiesAPI.getExpedient(numexp);
		int idPcd = itemExpediente.getInt("ID_PCD");
		
		IItem itemProcedimiento = pcdAPI.getProcedureById(idPcd);
		String codMateriaComp = "";
		Object objMateriaComp = itemProcedimiento.get("CTPROCEDIMIENTOS:MTRS_COMP");
		if (null != objMateriaComp){
			codMateriaComp = String.valueOf(objMateriaComp);
		}
		
		StringBuffer sbQuery = new StringBuffer();
		sbQuery.append("WHERE VALOR LIKE '");
		sbQuery.append(codMateriaComp);
		sbQuery.append("%'");

		IItemCollection colPcdGenericos = entitiesAPI.queryEntities("DPCR_PROC_GENERICOS", sbQuery.toString());
		@SuppressWarnings("rawtypes")
		Iterator it = colPcdGenericos.iterator();
		
		ArrayList<ItemBean> listPcdGenericos = new ArrayList<ItemBean>();
		while (it.hasNext()){
			
			IItem itemPcdGenerico = (IItem) it.next();
			ItemBean itembean = new ItemBean();
			itembean.setProperty("VALOR", itemPcdGenerico.getString("VALOR"));
			itembean.setProperty("SUSTITUTO", itemPcdGenerico.getString("SUSTITUTO"));
			listPcdGenericos.add(itembean);
		}

		request.setAttribute("SubstituteList", listPcdGenericos);
		
//		request.setAttribute("maxResultados", 20);
		
		// Obtener formateador
		processFormatter(request, "/digester/substituteformatter.xml");
		
		return mapping.findForward("success");
	}
	
}
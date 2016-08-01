package es.dipucr.sigem.actions.manualesUsuario;

import ieci.tdw.ispac.api.IProcedureAPI;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.bean.CollectionBean;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispacmgr.action.BaseAction;
import ieci.tdw.ispac.ispacweb.api.IManagerAPI;
import ieci.tdw.ispac.ispacweb.api.ManagerAPIFactory;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ShowManualesListAction extends BaseAction {
	
	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {
		
		ClientContext cct = session.getClientContext();
		String tipo=request.getParameter("tipo");
    	
		//Cargamos el stateContext
		IManagerAPI managerAPI = ManagerAPIFactory.getInstance().getManagerAPI(cct);
        managerAPI.currentState(getStateticket(request));
		
        IProcedureAPI procedureAPI = session.getAPI().getProcedureAPI();
        IItemCollection itemcol = null;
        
        if (StringUtils.isNotEmpty(tipo)){
        	itemcol = procedureAPI.getGlobalManuales();
        }
        else{
        	itemcol = procedureAPI.getManuales(cct.getStateContext());
        }
        
		request.setAttribute("ManualesList", CollectionBean.getBeanList(itemcol));

		return mapping.findForward("success");
	}
}
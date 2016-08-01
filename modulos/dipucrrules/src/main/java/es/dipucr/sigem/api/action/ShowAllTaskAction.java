package es.dipucr.sigem.api.action;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.ispaclib.bean.CacheFormatterFactory;
import ieci.tdw.ispac.ispaclib.bean.CollectionBean;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.utils.DBUtil;
import ieci.tdw.ispac.ispacmgr.action.BaseAction;
import ieci.tdw.ispac.ispacweb.api.IManagerAPI;
import ieci.tdw.ispac.ispacweb.api.IState;
import ieci.tdw.ispac.ispacweb.api.ManagerAPIFactory;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.dipucr.sigem.api.rule.procedures.Constants;

public class ShowAllTaskAction extends BaseAction {

    @SuppressWarnings({ "rawtypes", "unchecked" })
	public ActionForward executeAction(ActionMapping mapping, 
    								   ActionForm form,
    								   HttpServletRequest request,
    								   HttpServletResponse response,
    								   SessionAPI session) throws Exception {

        ClientContext cct = session.getClientContext();
        IInvesflowAPI invesFlowAPI = session.getAPI();
        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
        IManagerAPI managerAPI = ManagerAPIFactory.getInstance().getManagerAPI(cct);

        // Estado actual
        IState state = managerAPI.currentState(getStateticket(request));
        String numExp = state.getNumexp();
        String stageId=request.getSession().getAttribute("stageId").toString();

        Map map = new LinkedHashMap();
        map.put("TRAM", Constants.TABLASBBDD.SPAC_DT_TRAMITES);
        
        // Obtenemos los expedientes descendientes del actual
        IItemCollection itemCol = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_DT_TRAMITES,"WHERE NUMEXP = '" + DBUtil.replaceQuotes(numExp) + "' AND ID_FASE_EXP = "+DBUtil.replaceQuotes(stageId) + " ORDER BY ID"); 
        //request.setAttribute("ChildValueList", CollectionBean.getBeanList(itemCol));
        List tramites = CollectionBean.getBeanList(itemCol);
        
        //request.setAttribute("ParentValueList", CollectionBean.getBeanList(itemCol));
        //tramites.addAll(CollectionBean.getBeanList(itemCol));
        request.setAttribute("ValueList", tramites);
        
        // Cargamos el formateador
        CacheFormatterFactory factory = CacheFormatterFactory.getInstance();
		request.setAttribute("Formatter", factory.getFormatter(getISPACPath("/formattersCustom/showAllTaskformatter.xml")));
        
        return mapping.findForward("success");
    }

}
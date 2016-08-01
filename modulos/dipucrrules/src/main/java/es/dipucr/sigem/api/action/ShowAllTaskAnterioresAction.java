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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.dipucr.sigem.api.rule.procedures.Constants;

public class ShowAllTaskAnterioresAction extends BaseAction {

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

        //[Manu Ticket #98] - INICIO - ALSIGM3 Añadir la fase a la que pertenecen los trámites en la entana de Datos de Trámites anteriores      
        //IItemCollection itemCol = entitiesAPI.queryEntities(Constants.TABLASBBDD.SPAC_DT_TRAMITES,"WHERE NUMEXP = '" + DBUtil.replaceQuotes(numExp) + "' AND ID_FASE_EXP != "+DBUtil.replaceQuotes(stageId) + " ORDER BY ID");
        Map<String, String> tablas = new HashMap<String, String>();
		tablas.put(Constants.TABLASBBDD.SPAC_DT_TRAMITES, "SPAC_DT_TRAMITES");
		tablas.put(Constants.TABLASBBDD.SPAC_P_FASES, "SPAC_P_FASES");
		
		String consulta = " WHERE SPAC_DT_TRAMITES.ID_FASE_PCD = SPAC_P_FASES.ID AND NUMEXP = '" + DBUtil.replaceQuotes(numExp) + "' AND ID_FASE_EXP != "+DBUtil.replaceQuotes(stageId) + " ORDER BY SPAC_DT_TRAMITES.FECHA_INICIO";
		
    	IItemCollection itemCol = entitiesAPI.queryEntities(tablas, consulta);
    	//[Manu Ticket #98] - FIN - ALSIGM3 Añadir la fase a la que pertenecen los trámites en la entana de Datos de Trámites anteriores

        List<?> tramites = CollectionBean.getBeanList(itemCol);  
        request.setAttribute("ValueList", tramites);
        
        // Cargamos el formateador
        CacheFormatterFactory factory = CacheFormatterFactory.getInstance();
		request.setAttribute("Formatter", factory.getFormatter(getISPACPath("/formattersCustom/showAllTaskAnterioresformatter.xml")));
        
        return mapping.findForward("success");
    }

}
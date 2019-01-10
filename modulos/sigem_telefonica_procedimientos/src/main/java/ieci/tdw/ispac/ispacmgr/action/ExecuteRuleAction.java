package ieci.tdw.ispac.ispacmgr.action;

import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IProcess;
import ieci.tdw.ispac.api.rule.EventManager;
import ieci.tdw.ispac.api.rule.RuleProperties;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispacweb.api.IManagerAPI;
import ieci.tdw.ispac.ispacweb.api.IState;
import ieci.tdw.ispac.ispacweb.api.ManagerAPIFactory;
import ieci.tdw.ispac.ispacweb.context.NextActivity;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

public class ExecuteRuleAction extends BaseAction{

    private static final Logger LOGGER = Logger.getLogger(ExecuteRuleAction.class);

    public ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, SessionAPI session) throws ISPACException {

        IInvesflowAPI invesflowAPI = session.getAPI();
        ClientContext cct = session.getClientContext();

        //Se obtiene el estado de tramitación(informacion de contexto del usuario)
        IManagerAPI managerAPI = ManagerAPIFactory.getInstance().getManagerAPI(cct);
        IState currentstate = managerAPI.currentState(getStateticket(request));
        IProcess process = invesflowAPI.getProcess(currentstate.getProcessId());
            
        //Nombre de la regla a ejecutar
        String strRuleName = request.getParameter("rule");
        LOGGER.warn("Regla a ejecutar: "+strRuleName);
        
        //Ejecutar la regla asociada
        if (strRuleName != null) {
            LOGGER.warn("Buscando regla '"+strRuleName+"'");
            IItemCollection reglas = invesflowAPI.getCatalogAPI().getCTRules(strRuleName);
            Iterator<?> it = reglas.iterator();
            
            if (it.hasNext()) {
                IItem regla = (IItem)it.next();
                int ruleId = regla.getInt("ID");
                LOGGER.warn("Regla número: "+ruleId);
                EventManager eventmgr = new EventManager(cct);
                eventmgr.newContext();
                eventmgr.getRuleContextBuilder().addContext(process);
                String strTaskId = request.getParameter("taskId");
                eventmgr.getRuleContextBuilder().addContext(RuleProperties.RCTX_TASK,strTaskId);
                eventmgr.processRule(ruleId);
            }
        }

        return NextActivity.refresh(currentstate, mapping);
    }

}

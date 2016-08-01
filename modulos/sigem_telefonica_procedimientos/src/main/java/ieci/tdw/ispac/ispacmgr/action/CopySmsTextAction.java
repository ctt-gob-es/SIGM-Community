package ieci.tdw.ispac.ispacmgr.action;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.IProcess;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispacweb.context.NextActivity;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import ieci.tdw.ispac.ispacweb.api.IManagerAPI;
import ieci.tdw.ispac.ispacweb.api.IState;
import ieci.tdw.ispac.ispacweb.api.ManagerAPIFactory;

import org.apache.log4j.Logger;

public class CopySmsTextAction extends BaseAction{

	private static final Logger logger = Logger.getLogger(CopySmsTextAction.class);

	public ActionForward executeAction(ActionMapping mapping,
			ActionForm form,
			HttpServletRequest request,
			HttpServletResponse response,
			SessionAPI session) throws Exception {

		IInvesflowAPI invesflowAPI = session.getAPI();
		ClientContext cct = session.getClientContext();
		IEntitiesAPI entitiesAPI = invesflowAPI.getEntitiesAPI();

		//Se obtiene el estado de tramitación(informacion de contexto del usuario)
		IManagerAPI managerAPI = ManagerAPIFactory.getInstance().getManagerAPI(cct);
		IState currentstate = managerAPI.currentState(getStateticket(request));
		IProcess process = invesflowAPI.getProcess(currentstate.getProcessId());
		String strNumexp = process.getString("NUMEXP");
			
		//Texto a copiar
		String strTexto = request.getParameter("txt");
		logger.warn("Texto a copiar: "+strTexto);
		
		//Se copia el texto en todos los destinatarios
        String strQuery = "WHERE NUMEXP='"+strNumexp+"'";
        IItemCollection col = entitiesAPI.queryEntities("TSOL_SMS", strQuery);
        Iterator it = col.iterator();
        while(it.hasNext())
        {
        	IItem iDest = (IItem)it.next();
			iDest.set("TEXTO", strTexto);
			iDest.store(cct);
        }

		return NextActivity.refresh(currentstate, mapping);
	}

}

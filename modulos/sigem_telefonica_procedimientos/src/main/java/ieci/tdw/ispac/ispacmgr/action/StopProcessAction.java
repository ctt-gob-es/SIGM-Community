package ieci.tdw.ispac.ispacmgr.action;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.ITask;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import java.util.Iterator;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;

/**
 * Cierra la tramitación del expediente cuando se pulsa el botón "Paralizar
 * expediente"
 * 
 */
public class StopProcessAction extends BaseAction {

	public ActionForward executeAction(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {

		// Trámite
		String taskId = request.getParameter("taskId");

		// Fase
		IInvesflowAPI invesflowAPI = session.getAPI();
		ITask task = null;
		task = invesflowAPI.getTask(Integer.parseInt(taskId));
		//int idsStage = task.getInt("ID_FASE_EXP");
		String numExp = "";

		// Abrir transacción. En caso de error, ejecutar rollback
		ClientContext cct = session.getClientContext();
		try{
			cct.beginTX();
	        
			//----------------------------------------------------------------------------------------------
	        //ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------

	        IItem exp = null;
	        int idExp = 0;
	        //numExp = rulectx.getNumExp();
	        numExp = task.getString("NUMEXP");
    		// Cerramos el expediente
	        String strQuery = "WHERE NUMEXP='" + numExp + "'";
	        logger.warn("Query: " + strQuery);
	        IItemCollection collExps = entitiesAPI.queryEntities("SPAC_EXPEDIENTES", strQuery);
	        Iterator itExps = collExps.iterator();
	        if (itExps.hasNext()) 
	        {
	        	exp = (IItem)itExps.next();
	        	idExp = exp.getInt("ID");
	        }else{
	        	throw new ISPACInfo("Se ha producido un error al obtener la información del expediente.");
	        }
			
			
			// Poner al campo Estado Administrativo el valor Paralizado 
			// --> comprobación en la regla ValidateFirmaDocRule para no ejecutarse 
			task = invesflowAPI.getTask(Integer.parseInt(taskId));
			IEntitiesAPI entapi = invesflowAPI.getEntitiesAPI();
			numExp = task.getString("NUMEXP");
			// Poner al campo Estado Administrativo el valor Paralizado, 
			// ya que puede haber reglas en el acabar fase y acabar trámite que cambien el Estado Administrativo
			cct.beginTX();
	        IItem item = entapi.getExpedient(numExp);
	        item.set("ESTADOADM","RC");
	        item.store(cct);
	        cct.endTX(true);
	        
			// Guarda el motivo de rechazo
			//this.storeEntity(mapping, form, request, response, session);
			// Cierra el trámite
			this.closeTask(mapping, form, request, response, session);
	        
			// Cierra la tramitación del expediente
	        request.setAttribute("idExp", String.valueOf(idExp));
	        
	        ExpedientesUtil.cerrarExpediente(cct, numExp);
	        //this.closeProcess(mapping, form, request, response, session);
	        
			cct.endTX(true);
			session.getClientContext().deleteSsVariable("ESTADOADM");
			
		}catch (Exception e) {
			cct.endTX(false);
			logger.error("Se ha producido un error al finalizar el expediente: " + numExp + ". " + e.getMessage(), e);
        	throw new ISPACRuleException("Se ha producido un error al finalizar el expediente: " + numExp + ". " + e.getMessage(), e);
		}
		
		return mapping.findForward("success");
	}

	/**
	 * Cierra el trámite
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @param session
	 * @throws Exception
	 */
	public void closeTask(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {

		String taskId = request.getParameter("taskId");
		String[] taskids = taskId.split("-");

		int ntaskid = 0;

		IInvesflowAPI invesflowAPI = session.getAPI();
		ITXTransaction tx = invesflowAPI.getTransactionAPI();

		for (int i = 0; i < taskids.length; i++) {

			ntaskid = Integer.parseInt(taskids[i]);
			// En el caso de cerrar un solo trámite se intentará
			// seguir trabajando con el expediente
			if (taskids.length == 1) {
				ntaskid = Integer.parseInt(taskids[0]);
			}

			tx.closeTask(ntaskid);
		}
	}

	/**
	 * Cerrar Expediente
	 * 
	 * @param mapping
	 * @param form
	 * @param request
	 * @param response
	 * @param session
	 * @throws Exception
	 */
	public void closeProcess(ActionMapping mapping, ActionForm form,
			HttpServletRequest request, HttpServletResponse response,
			SessionAPI session) throws Exception {

		String idExp = (String) request.getAttribute("idExp");
		
		if (idExp != null && !idExp.equals("")){
			IInvesflowAPI invesflowAPI = session.getAPI();
			ITXTransaction tx = invesflowAPI.getTransactionAPI();
			int nidExp = Integer.parseInt(idExp);
			tx.closeProcess(nidExp);
		}else{
			throw new ISPACInfo("Se ha producido un error al obtener el expediente");
		}
	}

}
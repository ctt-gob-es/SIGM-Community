package ieci.tdw.ispac.ispacmgr.action;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.impl.SessionAPI;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.ITask;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

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

    public ActionForward executeAction(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, SessionAPI session) throws NumberFormatException, ISPACException {

        // Trámite
        String taskId = request.getParameter("taskId");

        // Fase
        IInvesflowAPI invesflowAPI = session.getAPI();
        ITask task = null;
        task = invesflowAPI.getTask(Integer.parseInt(taskId));
        //int idsStage = task.getInt("ID_FASE_EXP");
        String numExp = "";

        // Abrir transacción. En caso de error, ejecutar rollback
        IClientContext cct = session.getClientContext();
        
        try{
            cct.beginTX();
            
            int idExp = 0;
            //numExp = rulectx.getNumExp();
            numExp = task.getString("NUMEXP");
            // Cerramos el expediente
        
            
            IItem exp = ExpedientesUtil.getExpediente(cct, numExp);
            
            if (null != exp) {                
                idExp = exp.getInt(ExpedientesUtil.ID);
                
            } else {
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
            LOGGER.error("Se ha producido un error al finalizar el expediente: " + numExp + ". " + e.getMessage(), e);
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
     * @throws ISPACException 
     */
    public void closeTask(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, SessionAPI session) throws ISPACException{

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
     * @throws ISPACException 
     */
    public void closeProcess(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response, SessionAPI session) throws ISPACException {

        String idExp = (String) request.getAttribute("idExp");
        
        if (StringUtils.isNotEmpty(idExp)){
            IInvesflowAPI invesflowAPI = session.getAPI();
            ITXTransaction tx = invesflowAPI.getTransactionAPI();
            int nidExp = Integer.parseInt(idExp);
            tx.closeProcess(nidExp);
            
        } else {
            throw new ISPACInfo("Se ha producido un error al obtener el expediente");
        }
    }

}
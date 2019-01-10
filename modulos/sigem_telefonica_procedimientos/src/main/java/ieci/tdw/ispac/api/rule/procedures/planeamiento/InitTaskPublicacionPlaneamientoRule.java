package ieci.tdw.ispac.api.rule.procedures.planeamiento;

import java.util.Iterator;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.item.ITask;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.api.rule.procedures.CommonFunctions;
import ieci.tdw.ispac.ispaclib.context.IClientContext;

public class InitTaskPublicacionPlaneamientoRule implements IRule {

    protected String strEntidad = "";
    protected String strTemplate = "";

    public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        try {
            //----------------------------------------------------------------------------------------------
            IClientContext cct =  rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------
            
            //Actualiza el campo estado de la entidad
            //de modo que permita mostrar el enlace para crear la Solicitud de Anuncio en el BOP
            String numexp = rulectx.getNumExp();
            IItemCollection col = entitiesAPI.getEntities(strEntidad, numexp);
            Iterator<?> it = col.iterator();
            
            if (it.hasNext()) {
                IItem entidad = (IItem)it.next();
                entidad.set("ESTADO", "Inicio");
                entidad.store(cct);
            }
            
            //Creación de variables de sesión para las plantillas
            int taskId = rulectx.getTaskId();
            ITask iTask = invesFlowAPI.getTask(taskId);
            String strTaskName = iTask.getString("NOMBRE");
            cct.setSsVariable("NOMBRE_TRAMITE", strTaskName);

            //Generación del anuncio a partir de la plantilla
            String strTpDocName = "Publicación";
            String strTemplateName = strTemplate;
            CommonFunctions.generarDocumento(rulectx, strTpDocName, strTemplateName, null);
            
            //Borra las variables de sesion utilizadas
            cct.deleteSsVariable("NOMBRE_TRAMITE");
            
        } catch(ISPACRuleException e) {
            throw new ISPACRuleException(e);
        
        } catch(Exception e) {
            throw new ISPACRuleException("No se ha podido iniciar la tarea de publicación en BOP.",e);
        }
        return Boolean.TRUE;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException{
        //No se da nunca este caso
    }
}

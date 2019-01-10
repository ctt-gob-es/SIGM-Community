package ieci.tdw.ispac.api.rule.procedures.decretos;

import java.util.Iterator;

import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

/**
 * 
 * @author teresa
 * @date 17/03/2010
 * @propósito Valida que no se haya creado anteriormente un trámite de Acuses de recibo en la fase actual
 * y que se hayan cerrado los trámites anteriores
 */
public class ValidateTaskAcusesRule implements IRule{

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        try{
            IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
            IItemCollection itemCollection = entitiesAPI.getStageTasks(rulectx.getNumExp(), rulectx.getStageProcedureId());

            Iterator<?> iteratorCollection = itemCollection.iterator();
            IItem task = null;
            boolean creacionCerrado = false;
            boolean preparacionCerrado = false;
            boolean notificacionesCerrado = false;
            int numCreacion = 0;
            int numPreparacion = 0;
            int numNotificaciones = 0;
            int numAcuses = 0;
            
            while (iteratorCollection.hasNext()){
                task = (IItem)iteratorCollection.next();
                
                if (task.getString("NOMBRE").equalsIgnoreCase(TramitesUtil.getTramiteByCode(rulectx, "CREAR_DECRETOS").getString("NOMBRE"))){
                    numCreacion++;
                    
                    if (task.getInt("ESTADO") == 3){
                        creacionCerrado = true;
                    } else {
                        rulectx.setInfoMessage("No se ha terminado el trámite de "+TramitesUtil.getTramiteByCode(rulectx, "CREAR_DECRETOS").getString("NOMBRE"));
                    }
                    
                } else if ("Preparación de firmas y traslado del Decreto".equalsIgnoreCase(task.getString("NOMBRE"))){
                    numPreparacion++;
                    
                    if (task.getInt("ESTADO") == 3){
                        preparacionCerrado = true;
                    } else {
                        rulectx.setInfoMessage("No se ha terminado el trámite de Preparación de firmas y traslado del Decreto");
                    }
                    
                } else if ("Notificaciones".equalsIgnoreCase(task.getString("NOMBRE"))){
                    numNotificaciones++;
                    
                    if (task.getInt("ESTADO") == 3){
                        notificacionesCerrado = true;
                    } else {
                        rulectx.setInfoMessage("No se ha terminado el trámite de Notificaciones");
                    }
                    
                } else if ("Acuses de recibo".equalsIgnoreCase(task.getString("NOMBRE"))){
                    numAcuses++;
                }
            }
            
            if (isCerrados(creacionCerrado, preparacionCerrado, notificacionesCerrado) && isUnTramiteTipo(numCreacion, numPreparacion, numNotificaciones, numAcuses)){
                return true;    
            } else {
                if (!creacionCerrado && numCreacion == 1){
                    rulectx.setInfoMessage("No se ha terminado el trámite de "+TramitesUtil.getTramiteByCode(rulectx, "CREAR_DECRETOS").getString("NOMBRE"));
                } else if (!preparacionCerrado && numPreparacion == 1){
                    rulectx.setInfoMessage("No se ha terminado el trámite de Preparación de firmas y traslado del Decreto");
                } else if (!notificacionesCerrado && numNotificaciones == 1){
                    rulectx.setInfoMessage("No se ha terminado el trámite de Notificaciones");
                } else if (numCreacion == 0){
                    rulectx.setInfoMessage("No se ha creado el trámite de "+TramitesUtil.getTramiteByCode(rulectx, "CREAR_DECRETOS").getString("NOMBRE"));
                } else if (numPreparacion == 0){
                    rulectx.setInfoMessage("No se ha creado el trámite de Preparación de firmas y traslado del Decreto");
                } else if (numNotificaciones == 0){
                    rulectx.setInfoMessage("No se ha creado el trámite de Notificaciones");
                } else if (numCreacion > 1 || numPreparacion > 1 || numNotificaciones > 1 || numAcuses > 1){
                    rulectx.setInfoMessage("Sólo se puede crear un trámite de cada tipo");
                }
                return false;
            }
        } catch (Exception e) {
            throw new ISPACRuleException("Error al comprobar el número de trámites de la fase actual", e);
        } 
    }
    
    private boolean isCerrados(boolean creacionCerrado, boolean preparacionCerrado, boolean notificacionesCerrado) {        
        return creacionCerrado && preparacionCerrado && notificacionesCerrado;
    }
    
    private boolean isUnTramiteTipo(int numCreacion, int numPreparacion, int numNotificaciones, int numAcuses) {
           return numCreacion == 1 && numPreparacion == 1 && numNotificaciones == 1 && numAcuses == 1 ;
    }


    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        return null;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        // No se da nunca este caso
    }
}

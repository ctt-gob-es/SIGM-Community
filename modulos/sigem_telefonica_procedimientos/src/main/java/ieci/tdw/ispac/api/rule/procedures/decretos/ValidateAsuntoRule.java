package ieci.tdw.ispac.api.rule.procedures.decretos;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

/**
 * 
 * @author diezp
 * @date 27/11/2008
 * @propósito Valida que el campo Asunto esté relleno
 */
public class ValidateAsuntoRule implements IRule{

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        try{
            IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();

            // Comprobar que se ha rellenado el campo Asunto de la solapa Expediente
            IItem item = (IItem)entitiesAPI.getExpedient(rulectx.getNumExp());
            
            if (item.getString("ASUNTO")==null || item.getString("ASUNTO").trim().length()==0){
                rulectx.setInfoMessage("No se puede iniciar el tramite ya que no se introducido ningún valor para el campo Asunto");
                return false;
            }else{
                return true;
            }
        } catch (Exception e) {
            throw new ISPACRuleException("Error al comprobar el valor del Asunto", e);
        } 
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        return null;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        // No se da nunca este caso
    }
}

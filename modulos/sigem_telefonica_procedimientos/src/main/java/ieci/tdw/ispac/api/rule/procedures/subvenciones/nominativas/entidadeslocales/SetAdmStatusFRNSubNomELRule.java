package ieci.tdw.ispac.api.rule.procedures.subvenciones.nominativas.entidadeslocales;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

/**
 * 
 * @author teresa
 * @date 20/03/2009
 * @propósito Modificar el campo Estado Administrativo con el valor "FASE DE RESOLUCION NO ATENDIDO"
 */
public class SetAdmStatusFRNSubNomELRule implements IRule {

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        
        try{
            IEntitiesAPI entitiesAPI = rulectx.getClientContext().getAPI().getEntitiesAPI();
            IItem item = entitiesAPI.getExpedient(rulectx.getNumExp());
            item.set("ESTADOADM", "FRN");
            item.store(rulectx.getClientContext());
        }catch(ISPACException e){
            throw new ISPACRuleException(e);
        }
        return null;
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        // No se da nunca este caso
    }

}

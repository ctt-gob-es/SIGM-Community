package ieci.tdw.ispac.api.rule.procedures.negociado;

import org.apache.log4j.Logger;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

/**
 * 
 * @author teresa
 * @proposito Calcula el IVA (16%) y el presupuesto con IVA (presupuesto_adjudicacion) a partir del presupuesto base (adjudicacion_sin_iva).
 * 
 */
public class CalculateIVAAdjudicacionRule implements IRule {
    
    private static final Logger LOGGER = Logger.getLogger(CalculateIVAAdjudicacionRule.class);

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {
        try{
            IItem item = rulectx.getItem();
            
            item.set("ADJUDICACION_IVA", item.getDouble("ADJUDICACION_SIN_IVA")*0.16);
            item.set("PRESUPUESTO_ADJUDICACION", item.getDouble("ADJUDICACION_SIN_IVA")+item.getDouble("ADJUDICACION_SIN_IVA")*0.16);
            
            return null;
            
        } catch(ISPACRuleException e) {
            LOGGER.info("No hace nada, solo captura el posible error: " + e.getMessage(), e);
               throw new ISPACRuleException(e);
            
        } catch(Exception e) {
            throw new ISPACRuleException(e);
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        // No se da nunca este caso
    }
}
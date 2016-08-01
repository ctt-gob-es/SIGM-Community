package ieci.tdw.ispac.api.rule.procedures.negociado;

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
		} catch(Exception e) {
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException(e);
        }
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
		
	}
}
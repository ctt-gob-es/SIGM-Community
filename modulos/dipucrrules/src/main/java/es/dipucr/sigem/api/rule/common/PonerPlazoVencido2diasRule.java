package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

/**
 * Ticket #603
 * @author Felipe-ecenpri
 * @since 14.05.2012
 */
public class PonerPlazoVencido2diasRule extends PonerPlazoVencidoRule {
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException
    {
        super.numDias = 2;
        return true;
    }

}

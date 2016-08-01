package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import es.dipucr.sigem.api.rule.common.utils.ExpedientesUtil;

/**
 * Ticket #504 - Avanzar la fase del expediente
 * @author Felipe-ecenpri
 * @since 04.04.2012
 */
public class AvanzarFaseRule implements IRule {
	public boolean init(IRuleContext rulectx) throws ISPACRuleException
    {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException
    {
    	return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException
    {        
    	String numexp = rulectx.getNumExp();
    	
    	try{ 
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        ExpedientesUtil.avanzarFase(cct, numexp);
	        return true;
			
		} catch (Exception e) {
	        throw new ISPACRuleException("Error al avanzar fase del expediente. " +
	        		" Numexp:" + numexp, e);
	    } 
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException
    {
    }

}

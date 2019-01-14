package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import es.dipucr.sigem.api.rule.common.utils.TramitesUtil;

/**
 * Termina el trámite actual
 * @author FELIPE
 *
 */
public class TerminarTramiteRule implements IRule {
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
    	TramitesUtil.cerrarTramite(rulectx.getTaskId(), rulectx);
		return true;
			
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException
    {
    }

}

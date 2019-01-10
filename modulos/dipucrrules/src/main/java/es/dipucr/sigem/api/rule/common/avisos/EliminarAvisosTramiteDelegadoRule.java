package es.dipucr.sigem.api.rule.common.avisos;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import es.dipucr.sigem.api.rule.common.utils.AvisosUtil;

/**
 * Ticket #473 - Aviso trámite delegado
 * @author Manu-ecenpri
 * @since 28.08.2013
 */
public class EliminarAvisosTramiteDelegadoRule implements IRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		return AvisosUtil.borrarAvisos(rulectx, AvisoTramiteDelegadoRule._DELEGATETASK_MESSAGE);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}

package es.dipucr.sigem.api.rule.common.avisos;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

/**
 * Ticket #36 - Aviso nuevo trámite
 * @author Felipe-ecenpri
 * @since 14.06.2010
 */
public class EliminarAvisosNuevoTramiteRule implements IRule {
	
//	private static final Logger logger = Logger.getLogger(AvisoFinFirmaRule.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		return AvisosUtil.borrarAvisos(rulectx, AvisoNuevoTramiteRule._NEWTASK_MESSAGE);
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}
}

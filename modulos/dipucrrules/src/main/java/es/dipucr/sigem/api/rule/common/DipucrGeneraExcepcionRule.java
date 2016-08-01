package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

/**
 * [dipucr-Felipe]
 * Regla que lanza una excepción para pruebas de errores
 * @author Felipe
 * @since 09.10.14
 */
public class DipucrGeneraExcepcionRule implements IRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		
		throw new ISPACRuleException("Excepción lanzada intencionadamente");
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}

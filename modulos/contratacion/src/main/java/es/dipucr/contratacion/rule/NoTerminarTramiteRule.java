package es.dipucr.contratacion.rule;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class NoTerminarTramiteRule implements IRule{

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return false;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		rulectx.setInfoMessage("No se permite termina trámite, se deberá eliminar el trámite generado.");
		
		return false;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		return null;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

	}

}

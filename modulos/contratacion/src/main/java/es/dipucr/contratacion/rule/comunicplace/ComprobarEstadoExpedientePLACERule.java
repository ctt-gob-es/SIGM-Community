package es.dipucr.contratacion.rule.comunicplace;

import es.dipucr.contratacion.common.DipucrFuncionesComunes;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class ComprobarEstadoExpedientePLACERule implements IRule{

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {		
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		DipucrFuncionesComunes.envioEstadoExpediente(rulectx, null, "Pliego");
		DipucrFuncionesComunes.envioEstadoExpediente(rulectx, null, "Licitacion");
		return new Boolean(true);
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

}

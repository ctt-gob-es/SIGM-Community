package es.dipucr.sigem.api.rule.common.excel;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class CargarDatosBBDDRule  implements IRule {

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {

		
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		CargarDatosExcell extraerTextoXls = new CargarDatosExcell();
		extraerTextoXls.extraerTextoXls(rulectx);
		return new Boolean(true);
	}

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {

		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {

		return true;
	}

}

package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;

public class SedePiePaginaTagRule implements IRule{

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}

	public Object execute(IRuleContext rulectx) throws ISPACRuleException {
		String sede = "";
		try {
			sede = ConfigurationMgr.getVarGlobal(rulectx.getClientContext(), "SEDE_ELECTRONICA");

		} catch (ISPACException e) {
			throw new ISPACRuleException("Error al obtener la variable del sistema SEDE_ELECTRONICA en el número de expediente "+rulectx.getNumExp()+" - "+ e.getMessage(),e);
		}
		return sede;
	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}

}

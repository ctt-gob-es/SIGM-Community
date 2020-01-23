package es.dipucr.contratacion.rule.comunicplace;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;
import es.dipucr.contratacion.common.MandarPublicacionPLACSPRule;

public class PublicacionAnuncioAdjudicacionRule extends MandarPublicacionPLACSPRule{
	
	

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		OPERACIONPLACE = "PUB_ANUNC";
		return true;
	}
}

package ieci.tdw.ispac.api.rule.procedures.subvenciones;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitPropuestaFinReintegroRule extends InitPropuestaSubvRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException
	{
		STR_entidad = "SUBV_REINTEGRO";
		STR_extracto = "Propuesta de resolución de reintegro de subvención";
        return true;
    }
}

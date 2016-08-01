package ieci.tdw.ispac.api.rule.procedures.subvenciones;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitDecretoInicioReintegroRule extends InitDecretoSubvRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException
	{
		STR_entidad = "SUBV_REINTEGRO";
		STR_extracto = "Inicio de reintegro de subvención";
        return true;
    }
}

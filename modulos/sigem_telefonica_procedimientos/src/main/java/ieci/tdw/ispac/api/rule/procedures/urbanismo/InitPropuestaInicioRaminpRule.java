package ieci.tdw.ispac.api.rule.procedures.urbanismo;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitPropuestaInicioRaminpRule extends InitPropuestaRaminpRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException
	{
		STR_entidad = "URB_LIC_RAMINP";
		STR_extracto = "Propuesta inicial Licencia actividad sujeta al RAMINP";
        return true;
    }
}

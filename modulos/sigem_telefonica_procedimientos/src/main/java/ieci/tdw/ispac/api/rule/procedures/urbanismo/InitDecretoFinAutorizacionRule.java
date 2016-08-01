package ieci.tdw.ispac.api.rule.procedures.urbanismo;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitDecretoFinAutorizacionRule extends InitDecretoSegregacionRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException
	{
		STR_entidad = "URB_AUTORIZACION";
		STR_extracto = "Resolución de licencia de autorización";
        return true;
    }
}

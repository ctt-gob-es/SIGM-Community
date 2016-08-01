package ieci.tdw.ispac.api.rule.procedures.cuenta;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitTaskPublicacionInicioCuentaRule extends InitTaskPublicacionCuentaRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException
	{
		STR_entidad = "CUEN_CUENTA";
		STR_template = "Anuncio Cuenta";		
        return true;
    }
}

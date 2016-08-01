package ieci.tdw.ispac.api.rule.procedures.presupuesto;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitTaskPublicacionFinPresupuestoRule extends InitTaskPublicacionPresupuestoRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException
	{
		STR_entidad = "PRES_PRESUPUESTO";
		STR_template = "Anuncio definitivo Presupuesto";		
        return true;
    }
}

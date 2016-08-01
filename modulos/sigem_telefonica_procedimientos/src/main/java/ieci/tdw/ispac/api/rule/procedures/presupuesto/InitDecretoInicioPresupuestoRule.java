package ieci.tdw.ispac.api.rule.procedures.presupuesto;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitDecretoInicioPresupuestoRule extends InitDecretoPresupuestoRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException
	{
		STR_entidad = "PRES_PRESUPUESTO";
		STR_extracto = "Aprobación inicial del Presupuesto General";
        return true;
    }
}

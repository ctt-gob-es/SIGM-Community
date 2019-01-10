package ieci.tdw.ispac.api.rule.procedures.presupuesto;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitPropuestaInicioPresupuestoRule extends InitPropuestaPresupuestoRule {

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        strEntidad = "PRES_PRESUPUESTO";
        strExtracto = "Propuesta de aprobación inicial del Presupuesto General";
        return true;
    }
}

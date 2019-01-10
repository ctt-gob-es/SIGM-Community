package ieci.tdw.ispac.api.rule.procedures.presupuesto;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitTaskPublicacionInicioPresupuestoRule extends InitTaskPublicacionPresupuestoRule {

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        strEntidad = "PRES_PRESUPUESTO";
        strTemplate = "Anuncio inicial Presupuesto";        
        return true;
    }
}

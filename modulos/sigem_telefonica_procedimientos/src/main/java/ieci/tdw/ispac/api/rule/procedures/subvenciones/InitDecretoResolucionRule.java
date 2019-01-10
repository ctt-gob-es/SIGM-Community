package ieci.tdw.ispac.api.rule.procedures.subvenciones;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitDecretoResolucionRule extends InitDecretoSubvRule {

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        strEntidad = "SUBV_CONVOCATORIA";
        strExtracto = "Resolución de convocatoria de subvenciones";
        return true;
    }
}

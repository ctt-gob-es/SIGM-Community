package ieci.tdw.ispac.api.rule.procedures.subvenciones;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitDecretoAprobacionRule extends InitDecretoSubvRule {

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        strEntidad = "SUBV_CONVOCATORIA";
        strExtracto = "Aprobación de convocatoria de subvenciones";
        return true;
    }
}

package ieci.tdw.ispac.api.rule.procedures.subvenciones;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitDecretoFinReintegroRule extends InitDecretoSubvRule {

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        strEntidad = "SUBV_REINTEGRO";
        strExtracto = "Resolución de reintegro de subvención";
        return true;
    }
}

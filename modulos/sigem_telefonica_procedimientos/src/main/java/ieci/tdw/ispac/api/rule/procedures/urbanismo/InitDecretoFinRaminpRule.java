package ieci.tdw.ispac.api.rule.procedures.urbanismo;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitDecretoFinRaminpRule extends InitDecretoRaminpRule {

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        strEntidad = "URB_LIC_RAMINP";
        strExtracto = "Resolución final Licencia actividad sujeta al RAMINP";
        return true;
    }
}

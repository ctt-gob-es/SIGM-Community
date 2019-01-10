package ieci.tdw.ispac.api.rule.procedures.urbanismo;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitDecretoInicioRaminpRule extends InitDecretoRaminpRule {

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        strEntidad = "URB_LIC_RAMINP";
        strExtracto = "Resolución inicial Licencia actividad sujeta al RAMINP";
        return true;
    }
}

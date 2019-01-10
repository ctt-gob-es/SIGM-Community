package ieci.tdw.ispac.api.rule.procedures.urbanismo;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitPropuestaFinSegregacionRule extends InitPropuestaSegregacionRule {

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        strEntidad = "URB_LIC_SEGREGACION";
        strExtracto = "Resolución de licencia de segregación";
        return true;
    }
}

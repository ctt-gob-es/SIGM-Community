package ieci.tdw.ispac.api.rule.procedures.cuenta;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitDecretoInicioCuentaRule extends InitDecretoCuentaRule {

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        strEntidad = "CUEN_CUENTA";
        strExtracto = "Aprobación inicial de la Cuenta General";
        return true;
    }
}

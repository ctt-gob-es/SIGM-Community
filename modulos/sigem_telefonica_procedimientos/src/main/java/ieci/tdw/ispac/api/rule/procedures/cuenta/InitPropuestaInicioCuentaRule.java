package ieci.tdw.ispac.api.rule.procedures.cuenta;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitPropuestaInicioCuentaRule extends InitPropuestaCuentaRule {

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        strEntidad = "CUEN_CUENTA";
        strExtracto = "Propuesta de aprobación inicial de la Cuenta General";
        return true;
    }
}

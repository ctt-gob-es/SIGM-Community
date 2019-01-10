package ieci.tdw.ispac.api.rule.procedures.subvenciones;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitPropuestaInicioReintegroRule extends InitPropuestaSubvRule {

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        strEntidad = "SUBV_REINTEGRO";
        strExtracto = "Propuesta de inicio de reintegro de subvención";
        return true;
    }
}

package ieci.tdw.ispac.api.rule.procedures.sancionador;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

/**
 * 
 * @author teresa
 * Inicia propuesta sanción Procedimiento Sancionador
 *
 */

public class InitPropuestaSancionSancionadorRule extends InitPropuestaSancionadorRule {

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        strEntidad = "URB_SANCIONADOR";
        strExtracto = "Resolución de sanción del procedimiento Sancionador";
        return true;
    }
}

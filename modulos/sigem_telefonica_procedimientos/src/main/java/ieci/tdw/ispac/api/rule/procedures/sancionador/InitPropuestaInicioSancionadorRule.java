package ieci.tdw.ispac.api.rule.procedures.sancionador;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

/**
 * 
 * @author teresa
 * Inicia propuesta inicial Procedimiento Sancionador
 *
 */

public class InitPropuestaInicioSancionadorRule extends InitPropuestaSancionadorRule {

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        strEntidad = "URB_SANCIONADOR";
        strExtracto = "Resolución de inicio del procedimiento Sancionador";
        return true;
    }
}

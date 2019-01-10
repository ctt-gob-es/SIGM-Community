package ieci.tdw.ispac.api.rule.procedures.sancionador;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

/**
 * 
 * @author teresa
 * Inicia tarea de Publicación del Procedimiento Sancionador
 *
 */

public class InitTaskPublicacionInicioSancionadorRule extends InitTaskPublicacionSancionadorRule {

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        strEntidad = "URB_SANCIONADOR";
        strTemplate = "Anuncio Sancionador";        
        return true;
    }
}

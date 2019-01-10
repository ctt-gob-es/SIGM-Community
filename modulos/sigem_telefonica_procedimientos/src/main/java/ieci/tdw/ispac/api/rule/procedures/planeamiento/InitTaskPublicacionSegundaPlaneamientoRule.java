package ieci.tdw.ispac.api.rule.procedures.planeamiento;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

/**
 * 
 * @author teresa
 * Inicia tarea de Publicación segunda del Procedimiento Aprobación y Modificación del Plan de Ordenación Municipal
 *
 */

public class InitTaskPublicacionSegundaPlaneamientoRule extends InitTaskPublicacionPlaneamientoRule {

    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        strEntidad = "PLAN_POM";
        strTemplate = "Anuncio BOP aprobación provisional POM";        
        return true;
    }
}

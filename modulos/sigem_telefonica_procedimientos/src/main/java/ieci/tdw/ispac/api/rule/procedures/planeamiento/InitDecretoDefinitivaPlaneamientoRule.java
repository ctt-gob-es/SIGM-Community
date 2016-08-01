package ieci.tdw.ispac.api.rule.procedures.planeamiento;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

/**
 * 
 * @author teresa
 * Inicia decreto aprobación definitiva Procedimiento Aprobación y Modificación del Plan de Ordenación Municipal
 *
 */

public class InitDecretoDefinitivaPlaneamientoRule extends InitDecretoPlaneamientoRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException
	{
		STR_entidad = "PLAN_POM";
		STR_extracto = "Resolución de aprobación definitiva del procedimiento de Aprobación y Modificación del Plan de Ordenación Municipal";
        return true;
    }
}

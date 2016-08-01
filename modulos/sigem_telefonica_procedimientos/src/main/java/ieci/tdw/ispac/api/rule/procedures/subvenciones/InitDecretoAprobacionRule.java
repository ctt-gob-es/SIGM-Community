package ieci.tdw.ispac.api.rule.procedures.subvenciones;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitDecretoAprobacionRule extends InitDecretoSubvRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException
	{
		STR_entidad = "SUBV_CONVOCATORIA";
		STR_extracto = "Aprobación de convocatoria de subvenciones";
        return true;
    }
}

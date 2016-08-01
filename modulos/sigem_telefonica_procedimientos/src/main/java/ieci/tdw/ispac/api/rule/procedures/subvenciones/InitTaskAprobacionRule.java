package ieci.tdw.ispac.api.rule.procedures.subvenciones;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitTaskAprobacionRule extends InitTaskAcuerdoDecretoRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException
	{
		STR_entidad = "SUBV_CONVOCATORIA";
		STR_queryDocumentos = 
			"DESCRIPCION = 'Propuesta de aprobación de convocatoria' OR " +
			"DESCRIPCION = 'Informe jurídico aprobación convocatoria' OR " +
			"DESCRIPCION = 'Informe intervención aprobación convocatoria'" ;		
        return true;
    }
}

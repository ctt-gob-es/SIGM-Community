package ieci.tdw.ispac.api.rule.procedures.subvenciones;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitTaskInicioReintegroRule extends InitTaskAcuerdoDecretoRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException
	{
		STR_entidad = "SUBV_REINTEGRO";
		STR_queryDocumentos = 
			"DESCRIPCION = 'Propuesta de inicio del expediente' OR " +
			"DESCRIPCION = 'Informe de inicio del expediente'" ;		
        return true;
    }
}

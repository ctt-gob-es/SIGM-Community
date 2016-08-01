package ieci.tdw.ispac.api.rule.procedures.urbanismo;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitTaskFinSegregacionRule extends InitTaskAcuerdoDecretoRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException
	{
		STR_entidad = "URB_LIC_SEGREGACION";
		STR_queryDocumentos = 
			"DESCRIPCION = 'Providencia' OR " +
			"DESCRIPCION = 'Informe técnico' OR " +
			"DESCRIPCION = 'Informe jurídico'" ;		
        return true;
    }
}

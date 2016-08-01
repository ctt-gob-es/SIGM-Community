package ieci.tdw.ispac.api.rule.procedures.recaudacion;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitPropuestaBenefIBIRule extends InitPropuestaRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException
	{
		STR_entidad = "REC_RESOLUCION";
		STR_extracto = "Propuesta expediente de Beneficios IBI";
        return true;
    }
}

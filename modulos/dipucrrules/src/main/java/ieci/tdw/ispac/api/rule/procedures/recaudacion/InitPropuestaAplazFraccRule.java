package ieci.tdw.ispac.api.rule.procedures.recaudacion;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitPropuestaAplazFraccRule extends InitPropuestaRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException
	{
		STR_entidad = "REC_RESOLUCION";
		STR_extracto = "Propuesta expediente de aplazamiento-fraccionamiento";
        return true;
    }
}

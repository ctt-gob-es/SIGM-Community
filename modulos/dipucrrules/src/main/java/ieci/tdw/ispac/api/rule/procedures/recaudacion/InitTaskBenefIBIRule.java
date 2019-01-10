package ieci.tdw.ispac.api.rule.procedures.recaudacion;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitTaskBenefIBIRule extends InitTaskResolucionRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException
	{
		strEntidad = "REC_RESOLUCION";
		strQueryDocumentos = "";
        return true;
    }
}

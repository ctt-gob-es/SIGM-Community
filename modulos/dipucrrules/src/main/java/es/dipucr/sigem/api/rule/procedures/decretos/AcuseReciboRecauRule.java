package es.dipucr.sigem.api.rule.procedures.decretos;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;
import es.dipucr.sigem.api.rule.common.DipucrGenerateAcuseVertical;

public class AcuseReciboRecauRule extends DipucrGenerateAcuseVertical{
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		STR_NombreDocAcuse  = "Acuse de Recibo Vertical";
		return true;
	}
}

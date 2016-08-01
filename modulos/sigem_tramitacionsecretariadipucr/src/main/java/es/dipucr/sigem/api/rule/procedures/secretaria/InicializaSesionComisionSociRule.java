package es.dipucr.sigem.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InicializaSesionComisionSociRule extends InicializaSesionRule {
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		STR_Organo	= "COMI";
		STR_Area	= "0040";
		return true;
	}
	
}
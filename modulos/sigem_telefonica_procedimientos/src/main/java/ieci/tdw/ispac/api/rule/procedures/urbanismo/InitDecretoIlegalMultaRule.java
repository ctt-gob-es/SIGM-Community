package ieci.tdw.ispac.api.rule.procedures.urbanismo;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitDecretoIlegalMultaRule extends InitDecretoIlegalRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException
	{
		STR_entidad = "URB_ILEGAL";
		STR_extracto = "Resolución de multa";
		STR_estado = "ESTADO_MULTA";
		STR_queryDocumento = "DESCRIPCION = 'Decreto de multa - Obra ilegal'";		
        return true;
    }
}

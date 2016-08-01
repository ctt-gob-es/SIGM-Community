package ieci.tdw.ispac.api.rule.procedures.urbanismo;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitDecretoIlegalPrecintoRule extends InitDecretoIlegalRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException
	{
		STR_entidad = "URB_ILEGAL";
		STR_extracto = "Resolución de precinto de obras";
		STR_estado = "ESTADO_PRECINTO";
		STR_queryDocumento = "DESCRIPCION = 'Decreto de precinto de obra - Obra ilegal'";		
        return true;
    }
}

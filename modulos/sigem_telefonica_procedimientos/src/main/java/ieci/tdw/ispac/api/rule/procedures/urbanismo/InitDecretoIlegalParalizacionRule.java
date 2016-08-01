package ieci.tdw.ispac.api.rule.procedures.urbanismo;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class InitDecretoIlegalParalizacionRule extends InitDecretoIlegalRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException
	{
		STR_entidad = "URB_ILEGAL";
		STR_extracto = "Resolución de paralización de obras";
		STR_estado = "ESTADO_PARALIZACION";
		STR_queryDocumento = "DESCRIPCION = 'Decreto de paralización de obra - Obra ilegal'";		
        return true;
    }
}

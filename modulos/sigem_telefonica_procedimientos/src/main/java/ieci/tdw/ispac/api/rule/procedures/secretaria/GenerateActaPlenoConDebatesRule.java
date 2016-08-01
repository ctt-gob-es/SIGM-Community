package ieci.tdw.ispac.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class GenerateActaPlenoConDebatesRule extends GenerateActaBaseRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {

		STR_prefijo                 = "Borrador de Acta de Pleno";
		STR_nombreTramite           = STR_prefijo + " con debates";
		STR_nombreCabecera          = STR_prefijo + " - Cabecera";
		STR_nombrePie               = STR_prefijo + " - Pie";
		STR_nombreCabeceraPropuesta = STR_prefijo + " - Propuesta - Cabecera";
		STR_nombrePiePropuesta      = STR_prefijo + " - Propuesta - Pie con debate";
		STR_nombreRuegos            = STR_prefijo + " - Ruegos y preguntas";
		return true;
	}
}


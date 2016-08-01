package ieci.tdw.ispac.api.rule.procedures.secretaria;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class GenerateActaConDebatesRule extends GenerateActaRule {

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		STR_docName = "Borrador de Acta de Sesión con debates";
		STR_footerDocName = "Borrador de Acta de Sesión - Propuesta - Pie con debate";
		return true;
	}
}


package es.dipucr.sigem.api.rule.procedures.subvenciones;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.documento.DipucrAutoGeneraDocIniTramiteRule;

public class DipucrGeneraComEntradaSubvOtrasEntRule extends DipucrAutoGeneraDocIniTramiteRule{
	private static final Logger logger = Logger.getLogger(DipucrGeneraComEntradaSubvOtrasEntRule.class);	

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		tipoDocumento = "Comunicación de Recepción y Plazo de Resolución";
		plantilla = "Comunicación de Recepción y Plazo de Resolución (SNE)";
		return true;
	}
}
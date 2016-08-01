package es.dipucr.sigem.api.rule.procedures.subvenciones;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.documento.DipucrAutoGeneraDocIniTramiteRule;

public class DipucrGeneraInfJurSubvOtrasEntRule extends DipucrAutoGeneraDocIniTramiteRule{
	private static final Logger logger = Logger.getLogger(DipucrGeneraInfJurSubvOtrasEntRule.class);	

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		tipoDocumento = "Informe jurídico";
		plantilla = "Informe jurídico (SNE)";
		return true;
	}
}
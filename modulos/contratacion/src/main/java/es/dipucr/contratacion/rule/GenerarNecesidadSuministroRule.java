package es.dipucr.contratacion.rule;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.documento.DipucrAutoGeneraDocIniTramiteRule;

public class GenerarNecesidadSuministroRule extends DipucrAutoGeneraDocIniTramiteRule {

	private static final Logger logger = Logger.getLogger(GenerarNecesidadSuministroRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		logger.warn("INICIO - " + GenerarNecesidadSuministroRule.class);

		tipoDocumento = "Solicitud Razonada de la Necesidad de un Suministro";
		plantilla = "Solicitud Razonada de la Necesidad de un Suministro";

		logger.warn("FIN - " + GenerarNecesidadSuministroRule.class);
		return true;
	}

}


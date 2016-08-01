package es.dipucr.contratacion.rule;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.documento.DipucrAutoGeneraDocIniTramiteRule;

public class GenerarActaRecepcionRule  extends DipucrAutoGeneraDocIniTramiteRule {

	private static final Logger logger = Logger.getLogger(GenerarActaRecepcionRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		logger.warn("INICIO - " + GenerarActaRecepcionRule.class);

		tipoDocumento = "Acta de Recepción";
		plantilla = "Acta de Recepción";

		logger.warn("FIN - " + GenerarActaRecepcionRule.class);
		return true;
	}

}


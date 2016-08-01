package es.dipucr.contratacion.rule;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.documento.DipucrAutoGeneraDocIniTramiteRule;

public class GenerarSubsanacionInformeRule extends DipucrAutoGeneraDocIniTramiteRule {

	private static final Logger logger = Logger.getLogger(GenerarSubsanacionInformeRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		logger.warn("INICIO - " + GenerarSubsanacionInformeRule.class);

		tipoDocumento = "Subsanación del Informe";
		plantilla = "Subsanación del Informe";

		logger.warn("FIN - " + GenerarSubsanacionInformeRule.class);
		return true;
	}

}


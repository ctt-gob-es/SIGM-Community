package es.dipucr.sigem.api.rule.common.expFol;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.DipucrGenerarExpedienteFoliadoConIndiceRule;

public class DpcrGenerarExpFolTramite extends DipucrGenerarExpedienteFoliadoConIndiceRule {
	
	private static final Logger logger = Logger.getLogger(DipucrGenerarExpedienteFoliadoConIndiceRule.class);

	public String getNumExpFoliar(IRuleContext rulectx, IEntitiesAPI entitiesAPI){		
		String resultado = "";
		try {
			resultado = rulectx.getNumExp();
		} catch (ISPACRuleException e) {
			logger.error("Error al recuperar el expediente a foliar.", e);	
		}
		return resultado;
	}
}

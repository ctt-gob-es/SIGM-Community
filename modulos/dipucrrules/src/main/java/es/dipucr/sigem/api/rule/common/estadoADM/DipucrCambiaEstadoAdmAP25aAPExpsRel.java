package es.dipucr.sigem.api.rule.common.estadoADM;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;


public class DipucrCambiaEstadoAdmAP25aAPExpsRel extends DipucrCambiaEstadoExpRel{
	
	private static final Logger logger = Logger.getLogger(DipucrCambiaEstadoAdmAP25aAPExpsRel.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		logger.info("INICIO - "+this.getClass().getName());
		
		estadoIni = "AP25";
		estadofin = "AP";
		
		logger.info("FIN - "+this.getClass().getName());
		
		return true;
	}
}
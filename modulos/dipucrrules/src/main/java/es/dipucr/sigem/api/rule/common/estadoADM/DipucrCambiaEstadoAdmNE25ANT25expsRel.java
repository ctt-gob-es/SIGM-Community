package es.dipucr.sigem.api.rule.common.estadoADM;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;


public class DipucrCambiaEstadoAdmNE25ANT25expsRel extends DipucrCambiaEstadoExpRel{
	
	private static final Logger logger = Logger.getLogger(DipucrCambiaEstadoAdmNE25ANT25expsRel.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		logger.info("INICIO - "+this.getClass().getName());
		
		estadoIni = "NE25";
		estadofin = "NT25";
		
		logger.info("FIN - "+this.getClass().getName());
		
		return true;
	}
}
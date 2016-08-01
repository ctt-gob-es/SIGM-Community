package es.dipucr.sigem.api.rule.procedures.planesProvinciales;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.bop.DipucrIniciaSolicitudAnuncioInternoBOP;

public class DipucrIniciaSolicitudAnuncioInternoBOPPlanes extends DipucrIniciaSolicitudAnuncioInternoBOP{

	private static final Logger logger = Logger.getLogger(DipucrIniciaSolicitudAnuncioInternoBOPPlanes.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {	
		logger.info("INICIO - " + this.getClass().getName());
		
		idCircuitoFirma = 90;
		
		logger.info("FIN- " + this.getClass().getName());
		return true;
	}
}

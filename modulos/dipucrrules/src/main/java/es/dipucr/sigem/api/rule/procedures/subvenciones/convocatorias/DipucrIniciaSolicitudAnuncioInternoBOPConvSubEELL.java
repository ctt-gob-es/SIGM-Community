package es.dipucr.sigem.api.rule.procedures.subvenciones.convocatorias;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.bop.DipucrIniciaSolicitudAnuncioInternoBOP;

public class DipucrIniciaSolicitudAnuncioInternoBOPConvSubEELL extends DipucrIniciaSolicitudAnuncioInternoBOP{
	
	private static final Logger logger = Logger.getLogger(DipucrIniciaSolicitudAnuncioInternoBOPConvSubEELL.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {	
		logger.warn("INICIO - DipucrIniciaSolicitudAnuncioInternoBOPConvSubEELL");
		
		idCircuitoFirma = 90;
		
		logger.warn("FIN - DipucrIniciaSolicitudAnuncioInternoBOPConvSubEELL");
		return true;
	}
}

package es.dipucr.contratacion.common.avisosMail;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.convocatorias.DipucrEnviaDocEmailConAcuse;

public class DipucrEnviaMailCartaLiquidacion extends DipucrEnviaDocEmailConAcuse {

	private static final Logger logger = Logger.getLogger(DipucrEnviaMailCartaLiquidacion.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {

		logger.info("INICIO - " + this.getClass().getName());
				
		VAR_EMAILS = "EMAIL_CARTA_LIQUIDACION"; //Variable de sistema de la que tomamos los emails
		
		conDocumento = true;
			
		asunto= "[SIGEM] Comunicación relativa a fecha de firma de Acta de Recepción. Numexp: "+rulectx.getNumExp();			

		logger.info("FIN - " + this.getClass().getName());
		return true;
	}

}

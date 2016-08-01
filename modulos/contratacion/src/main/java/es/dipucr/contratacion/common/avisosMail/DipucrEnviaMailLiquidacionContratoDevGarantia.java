package es.dipucr.contratacion.common.avisosMail;

import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.convocatorias.DipucrEnviaDocEmailConAcuse;
import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;

public class DipucrEnviaMailLiquidacionContratoDevGarantia extends DipucrEnviaDocEmailConAcuse {

	private static final Logger logger = Logger.getLogger(DipucrEnviaMailLiquidacionContratoDevGarantia.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {

		logger.info("INICIO - " + this.getClass().getName());
				
		VAR_EMAILS = "EMAIL_LIQUIDACION_CONTRATO"; //Variable de sistema de la que tomamos los emails
		
		String plantilla = DocumentosUtil.getPlantillaDefecto(rulectx.getClientContext(), rulectx.getTaskProcedureId());
		
		conDocumento = true;
			
		asunto= "[SIGEM] "+plantilla+". Numexp: "+rulectx.getNumExp();			

		logger.info("FIN - " + this.getClass().getName());
		return true;
	}

}

package es.dipucr.contratacion.common.avisosMail;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.convocatorias.DipucrEnviaDocEmailConAcuse;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class EnvioAvisoEmailTerminarTramiteIncidenciaContrato extends DipucrEnviaDocEmailConAcuse {

	/**
	 * [Ticket #486 TCG](SIGEM creación método genérico para el envío de avisos por mail)
	 */
	protected static final Logger logger = Logger.getLogger(EnvioAvisoEmailTerminarTramiteIncidenciaContrato.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		try{
			
			VAR_EMAILS = "EMAIL_INCIDCONTRATO_FINALIZADA";
	        conDocumento = false;
	        contenido = "Ha sido añadido una nueva Incidencidencia de contrato del expediente "+rulectx.getNumExp();
			asunto = "Ha sido añadido una nueva Incidencidencia de contrato del expediente "+rulectx.getNumExp();
	 
		 } catch (ISPACException e) {
			 logger.error("Se produjo una excepción en el expediente "+rulectx.getNumExp()+" - "+e.getMessage(), e);
			 throw new ISPACRuleException("Se produjo una excepción en el expediente "+rulectx.getNumExp()+" - "+e.getMessage(), e);
		}
		return true;
	}
}

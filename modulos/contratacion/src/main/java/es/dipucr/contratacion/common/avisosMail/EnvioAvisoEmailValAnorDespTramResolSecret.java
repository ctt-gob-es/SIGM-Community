package es.dipucr.contratacion.common.avisosMail;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.convocatorias.DipucrEnviaDocEmailConAcuse;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class EnvioAvisoEmailValAnorDespTramResolSecret extends DipucrEnviaDocEmailConAcuse {

	protected static final Logger logger = Logger.getLogger(EnvioAvisoEmailValAnorDespTramResolSecret.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		try{
			
			VAR_EMAILS = "EMAIL_VALORESANORMALES_SECRETARIA";
	        conDocumento = false;
	        contenido = "Realizado el informe de contratación para los Valores Anormales y Desproporcionados "+rulectx.getNumExp();
			asunto = "Realizado el informe de contratación para los Valores Anormales y Desproporcionados "+rulectx.getNumExp();
	 
		 } catch (ISPACException e) {
			 logger.error("Se produjo una excepción en el expediente "+rulectx.getNumExp()+" - "+e.getMessage(), e);
			 throw new ISPACRuleException("Se produjo una excepción en el expediente "+rulectx.getNumExp()+" - "+e.getMessage(), e);
		}
		return true;
	}
}

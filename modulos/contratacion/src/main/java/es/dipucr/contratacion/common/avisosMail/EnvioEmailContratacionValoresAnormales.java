package es.dipucr.contratacion.common.avisosMail;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.convocatorias.DipucrEnviaDocEmailConAcuse;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class EnvioEmailContratacionValoresAnormales extends DipucrEnviaDocEmailConAcuse {
	
	protected static final Logger logger = Logger.getLogger(EnvioEmailContratacionValoresAnormales.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		try{
			
			VAR_EMAILS = "EMAIL_VALORESANORMALES_CONTRATACION";
	        conDocumento = false;
	        contenido = "Tiene en el expediente del Licitador "+rulectx.getNumExp()+ " valores anormales o desproporcionados";
			asunto = "Tiene en el expediente del Licitador "+rulectx.getNumExp()+ " valores anormales o desproporcionados";
	 
		 } catch (ISPACException e) {
			 logger.error("Se produjo una excepción en el expediente "+rulectx.getNumExp()+" - "+e.getMessage(), e);
			 throw new ISPACRuleException("Se produjo una excepción en el expediente "+rulectx.getNumExp()+" - "+e.getMessage(), e);
		}
		return true;
	}	
}

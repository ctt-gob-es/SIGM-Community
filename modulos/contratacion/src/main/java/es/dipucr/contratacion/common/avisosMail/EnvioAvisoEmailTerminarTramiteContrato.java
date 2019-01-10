package es.dipucr.contratacion.common.avisosMail;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.convocatorias.DipucrEnviaDocEmailConAcuse;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRuleContext;

public class EnvioAvisoEmailTerminarTramiteContrato  extends DipucrEnviaDocEmailConAcuse {

	protected static final Logger logger = Logger.getLogger(EnvioAvisoEmailTerminarTramiteContrato.class);

	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		 try{

			VAR_EMAILS = "EMAILS_CONTRATACION_PROCCONTRATACION";
			asunto = "Fin trámite con el número de expediente "+rulectx.getNumExp();
			conDocumento = false;

	 
		 } catch (ISPACException e) {
			 logger.error("Se produjo una excepción en el expediente "+rulectx.getNumExp()+" - "+e.getMessage(), e);
			 throw new ISPACRuleException("Se produjo una excepción en el expediente "+rulectx.getNumExp()+" - "+e.getMessage(), e);
		}
		 return new Boolean(true);

	}


}

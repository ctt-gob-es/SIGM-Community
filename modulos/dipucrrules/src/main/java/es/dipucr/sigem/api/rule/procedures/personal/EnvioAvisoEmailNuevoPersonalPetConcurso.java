package es.dipucr.sigem.api.rule.procedures.personal;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.MailUtil;

public class EnvioAvisoEmailNuevoPersonalPetConcurso   implements IRule 
{

	/**
	 * [Ticket #486 TCG](SIGEM creación método genérico para el envío de avisos por mail)
	 */
	protected static final Logger logger = Logger.getLogger(EnvioAvisoEmailNuevoPersonalPetConcurso.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	 public Object execute(IRuleContext rulectx) throws ISPACRuleException{
		 try{
			//Se adjunta la direccion de la persona de tesoreria a la que hay que mandarle el email
			 
			 String emailNotif = ConfigurationMgr.getVarGlobal(rulectx.getClientContext(), "EMAILS_DEPPERSONAL_PETPUESTOTRAB");
			 
	        //String emailNotif = "jluis_ruiz@dipucr.es;eloy_sanz@dipucr.es;teresa_sanz@dipucr.es;";
			String contenido = "Se ha creado un nuevo procedimiento de Petición de provisión de puesto de trabajo en el expediente: "+rulectx.getNumExp();
			String asunto = "Se ha creado un nuevo procedimiento de Petición de provisión de puesto de trabajo en el expediente: "+rulectx.getNumExp();

			MailUtil.enviarCorreoVarios(rulectx, emailNotif, asunto, contenido, false);
			logger.warn("[EnvioMailTesoreriaRule:execute()]Email enviado a: "+emailNotif);
	 
		 } catch (ISPACException e) {
			 logger.error("Se produjo una excepción en el expediente "+rulectx.getNumExp()+" - "+e.getMessage(), e);
			 throw new ISPACRuleException("Se produjo una excepción en el expediente "+rulectx.getNumExp()+" - "+e.getMessage(), e);
		}
		 return new Boolean(true);

	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}

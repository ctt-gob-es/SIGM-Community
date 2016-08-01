package es.dipucr.contratacion.common.avisosMail;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.MailUtil;

public class EnvioAvisoEmailTerminarTramiteActaReplanteo   implements IRule 
{

	/**
	 * [Ticket #486 TCG](SIGEM creación método genérico para el envío de avisos por mail)
	 */
	protected static final Logger logger = Logger.getLogger(EnvioAvisoEmailTerminarTramiteActaReplanteo.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	 public Object execute(IRuleContext rulectx) throws ISPACRuleException{
		 try{
				
			//Se adjunta la direccion de la persona de tesoreria a la que hay que mandarle el email
	        String emailNotif = "compras@dipucr.es";
			String contenido = "Ha sido añadido un Acta de replanteo del expediente "+rulectx.getNumExp();
			String asunto = "Ha sido añadido un Acta de replanteo del expediente "+rulectx.getNumExp();

			MailUtil.enviarCorreo(rulectx, emailNotif, asunto, contenido);
	 
		 } catch (ISPACException e) {
			 logger.error("Se produjo una excepciÃ³n "+e.getMessage(), e);
			 throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		 return new Boolean(true);

	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}

package es.dipucr.contratacion.common.avisosMail;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.utils.DocumentosUtil;
import es.dipucr.sigem.api.rule.common.utils.MailUtil;

public class EnvioAvisoEmailFinFirmaRule implements IRule 
{

	/**
	 * [Ticket #486 TCG](SIGEM creación método genérico para el envío de avisos por mail)
	 */
	protected static final Logger logger = Logger.getLogger(EnvioAvisoEmailFinFirmaRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
		return true;
	}
	
	 public Object execute(IRuleContext rulectx) throws ISPACRuleException{
		 try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------
	        
	        int idDoc = rulectx.getInt("ID_DOCUMENTO");
			//int idCircuito = rulectx.getInt("ID_CIRCUITO");//TODO
			IItem itemDocumento = DocumentosUtil.getDocumento(entitiesAPI, idDoc);//TODO
			String numexp = itemDocumento.getString("NUMEXP");
	
				
			//Se adjunta la direccion de la persona de tesoreria a la que hay que mandarle el email
			//Maria del Carmen Villaverde Menchen TESORERIA
			//String emailNotif = "javier_garcia@dipucr.es";
	        String emailNotif = "javier_garcia@dipucr.es";
			//String nombreNotif = "Javier Garcia Caballero";
			String contenido = "Fin de firma de Trámite de Intervención en SIGEM a visualizar con el número de expediente "+numexp;
			String asunto = "Fin de firma de Trámite de Intervención en SIGEM a visualizar con el número de expediente "+numexp;
	
			MailUtil.enviarCorreo(rulectx.getClientContext(), emailNotif, asunto, contenido);
			logger.warn("[EnvioMailTesoreriaRule:execute()]Email enviado a: "+emailNotif);
	 
		 } catch (ISPACException e) {
			 logger.error("Se produjo una excepciÃ³n "+e.getMessage(), e);
			 throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		 return new Boolean(true);

	}

	public void cancel(IRuleContext rulectx) throws ISPACRuleException {
	}
}

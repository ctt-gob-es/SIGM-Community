package ieci.tdw.ispac.api.rule.procedures.sms;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tecdoc.sgm.core.exception.SigemException;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.mensajes_cortos.ServicioMensajesCortos;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.sms.SMSConfiguration;

public class EnvioMensajesRule implements IRule {
 
	private static final Logger logger = Logger.getLogger(EnvioMensajesRule.class);
	
	public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException{
    	try{
			//----------------------------------------------------------------------------------------------
	        ClientContext cct = (ClientContext) rulectx.getClientContext();
	        IInvesflowAPI invesFlowAPI = cct.getAPI();
	        IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
	        //----------------------------------------------------------------------------------------------

	        String strQuery = "WHERE NUMEXP='"+rulectx.getNumExp()+"'";
	        IItemCollection col = entitiesAPI.queryEntities("TSOL_SMS", strQuery);
	        Iterator it = col.iterator();
	        while(it.hasNext())
	        {
	        	IItem iDest = (IItem)it.next();
	        	logger.warn("Envío a: "+ iDest.getString("NOMBRE"));
	        	if (iDest.getString("ENVIAR_SMS").compareTo("SI")==0 &&
	        		iDest.getString("ENVIADO_SMS").compareTo("SI")!=0)
	        	{
	        		logger.warn("Enviar: "+ iDest.getString("ENVIAR_SMS"));
		        	String strTlf = iDest.getString("TFNO_MOVIL");
		        	String strTxt = iDest.getString("TEXTO");
		        	String mtid = EnviarSMS(rulectx, strTlf, strTxt);
	        		logger.warn("Resultado: "+ mtid);
		        	if (mtid!=null)
		        	{
		        		iDest.set("MTID", mtid);
		        		iDest.set("ENVIADO_SMS", "SI");
		        		iDest.store(cct);
		        	}
	        	}
	        }
        	return new Boolean(true);
        } 
	    catch(Exception e) {
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("No se han podido enviar los SMS",e);
        }
    }

	public void cancel(IRuleContext rulectx) throws ISPACRuleException{
    }

	private String EnviarSMS(IRuleContext rulectx, String strTo, String strTxt) throws SigemException, ISPACException
    {
		SMSConfiguration smsConfig = SMSConfiguration.getInstance(rulectx.getClientContext());

		String strUser = smsConfig.get(SMSConfiguration.USER_SMS);
		String strPwd = smsConfig.get(SMSConfiguration.PWD_SMS);
		String strRte = smsConfig.get(SMSConfiguration.REMITENTE_SMS);
		String mtid = null;

        logger.warn("Destinatario: " + strTo);
        logger.warn("Texto a enviar: " + strTxt);
        ServicioMensajesCortos servicio = LocalizadorServicios.getServicioMensajesCortos();
		mtid = servicio.sendCertSMS(strUser, strPwd, strRte, strTo, strTxt, "ES");
		logger.warn("Envío OK. mtid: " + mtid);

		String [] parts= mtid.split(":");
		return parts[0];
    }

 }
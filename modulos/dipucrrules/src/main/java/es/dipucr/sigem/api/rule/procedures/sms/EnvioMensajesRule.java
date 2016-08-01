package es.dipucr.sigem.api.rule.procedures.sms;

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

import java.rmi.RemoteException;
import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.enviosms.objetos.Respuesta;
import es.dipucr.enviosms.ws.MensajeSMSProxy;
import es.dipucr.sigem.api.rule.common.sms.SMSConfiguration;
import es.dipucr.sigem.api.rule.common.utils.EntidadesAdmUtil;

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

	        String strQuery = "WHERE NUMEXP='"+rulectx.getNumExp()+"' AND TFNO_MOVIL IS NOT NULL";
	        IItemCollection col = entitiesAPI.queryEntities("TSOL_SMS", strQuery);
	        Iterator<?> it = col.iterator();
	        while(it.hasNext())
	        {
	        	IItem iDest = (IItem)it.next();
	        	if(iDest.getString("NOMBRE") != null){
	        		//logger.warn("Envío a: "+ iDest.getString("NOMBRE"));
		        	if (iDest.getString("ENVIAR_SMS").compareTo("SI")==0 &&
		        		iDest.getString("ENVIADO_SMS").compareTo("SI")!=0)
		        	{
		        		//logger.warn("Enviar: "+ iDest.getString("ENVIAR_SMS"));
			        	String strTlf = iDest.getString("TFNO_MOVIL");
			        	String strTxt = iDest.getString("TEXTO");
			        	String identPartic = iDest.getString("ID_PARTICIPANTE");
			        	Respuesta resultadoEnvio = EnviarSMSServicioWebDipu(rulectx, strTlf, strTxt, identPartic);
		        		//logger.warn("Resultado: "+ resultadoEnvio.getMensaje());
		        		if(resultadoEnvio.getMensaje() == null){
		        			iDest.set("ENVIADO_SMS", "NO");
			        		iDest.store(cct);
		        		}
		        		else{
		        			iDest.set("ENVIADO_SMS", "SI");
			        		iDest.store(cct);
		        		}
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

	@SuppressWarnings("unused")
	private String EnviarSMS(IRuleContext rulectx, String strTo, String strTxt) throws SigemException, ISPACException
    {
		SMSConfiguration smsConfig = SMSConfiguration.getInstance(rulectx.getClientContext());

		String strUser = smsConfig.get(SMSConfiguration.USER_SMS);
		String strPwd = smsConfig.get(SMSConfiguration.PWD_SMS);
		String strRte = smsConfig.get(SMSConfiguration.REMITENTE_SMS);
		String mtid = null;

        //logger.warn("Destinatario: " + strTo);
        //logger.warn("Texto a enviar: " + strTxt);
        ServicioMensajesCortos servicio = LocalizadorServicios.getServicioMensajesCortos();
		mtid = servicio.sendCertSMS(strUser, strPwd, strRte, strTo, strTxt, "ES");
		//logger.warn("Envío OK. mtid: " + mtid);

		String [] parts= mtid.split(":");
		return parts[0];
    }
	
	private Respuesta EnviarSMSServicioWebDipu(IRuleContext rulectx, String strTo, String strTxt, String identPartic) throws SigemException, ISPACException
    {
        //logger.warn("Destinatario: " + strTo);
        //logger.warn("Texto a enviar: " + strTxt);
		Respuesta res = new Respuesta();
		if(!strTo.equals("")){
			MensajeSMSProxy sms = new MensajeSMSProxy();
	        
			try {
				String [] movil = new String [1];
				movil[0] = "+34"+strTo;
				res = sms.envioSMS(EntidadesAdmUtil.obtenerEntidad(rulectx.getClientContext()), "ALSIGM", movil, strTxt);
				//res = sms.envioSMS("SIGEM", identPartic, strTo, strTxt);
			} catch (RemoteException e) {
				logger.error(e.getMessage(), e);
			}
		}
        
		//logger.warn("Envío OK. mtid: " + res.isEnvio());

		return res;
    }

 }
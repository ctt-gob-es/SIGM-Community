package ieci.tdw.ispac.api.rule.procedures.sms;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tecdoc.sgm.core.exception.SigemException;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.mensajes_cortos.ServicioMensajesCortos;

import java.util.Iterator;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.sms.SMSConfiguration;

public class EnvioMensajesRule implements IRule {
 
    private static final Logger LOGGER = Logger.getLogger(EnvioMensajesRule.class);
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException{
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException{
        try{
            //----------------------------------------------------------------------------------------------
            IClientContext cct =  rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            //----------------------------------------------------------------------------------------------

            IItemCollection col = entitiesAPI.getEntities("TSOL_SMS", rulectx.getNumExp());
            Iterator<?> it = col.iterator();
            
            while(it.hasNext()) {
                IItem iDest = (IItem)it.next();
                LOGGER.warn("Envío a: "+ iDest.getString("NOMBRE"));
                
                if (iDest.getString("ENVIAR_SMS").compareTo("SI")==0 && iDest.getString("ENVIADO_SMS").compareTo("SI")!=0) {
                    LOGGER.warn("Enviar: "+ iDest.getString("ENVIAR_SMS"));
                    String strTlf = iDest.getString("TFNO_MOVIL");
                    String strTxt = iDest.getString("TEXTO");
                    String mtid = enviarSMS(rulectx, strTlf, strTxt);
                    LOGGER.warn("Resultado: "+ mtid);
                    
                    if (mtid!=null) {
                        iDest.set("MTID", mtid);
                        iDest.set("ENVIADO_SMS", "SI");
                        iDest.store(cct);
                    }
                }
            }
            return Boolean.TRUE;
            
        } catch(ISPACRuleException e) {
            throw new ISPACRuleException(e);
        
        } catch(Exception e) {
            throw new ISPACRuleException("No se han podido enviar los SMS",e);
        }
    }

    public void cancel(IRuleContext rulectx) throws ISPACRuleException{
        //No se da nunca este caso
    }

    private String enviarSMS(IRuleContext rulectx, String strTo, String strTxt) throws SigemException, ISPACException {
        SMSConfiguration smsConfig = SMSConfiguration.getInstance(rulectx.getClientContext());

        String strUser = smsConfig.get(SMSConfiguration.USER_SMS);
        String strPwd = smsConfig.get(SMSConfiguration.PWD_SMS);
        String strRte = smsConfig.get(SMSConfiguration.REMITENTE_SMS);
        String mtid = null;

        LOGGER.warn("Destinatario: " + strTo);
        LOGGER.warn("Texto a enviar: " + strTxt);
        ServicioMensajesCortos servicio = LocalizadorServicios.getServicioMensajesCortos();
        mtid = servicio.sendCertSMS(strUser, strPwd, strRte, strTo, strTxt, "ES");
        LOGGER.warn("Envío OK. mtid: " + mtid);

        String [] parts= mtid.split(":");
        return parts[0];
    }
 }
package ieci.tdw.ispac.api.rule.procedures.negociado;

import ieci.tdw.ispac.api.IEntitiesAPI;
import ieci.tdw.ispac.api.IInvesflowAPI;
import ieci.tdw.ispac.api.ITXTransaction;
import ieci.tdw.ispac.api.entities.SpacEntities;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.messages.Messages;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Iterator;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.correo.CorreoConfiguration;
import es.dipucr.sigem.api.rule.common.utils.ParticipantesUtil;

/**
 * 
 * @author teresa
 * @date 19/11/2009
 * @propósito Se genera automáticamente el trámite de Designación de participantes y se envía un correo de notificación a los trasladados
 * solicitando el documento de Designación de participantes.
 */
public class CreateTaskDesignacionParticipantesRule implements IRule {

    /** Logger de la clase. */
    protected static final Logger LOGGER = Logger.getLogger(CreateTaskDesignacionParticipantesRule.class);

    protected String strTramDesignacionParticipantes = "Des particip";
    
    public boolean init(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public boolean validate(IRuleContext rulectx) throws ISPACRuleException {
        return true;
    }

    public Object execute(IRuleContext rulectx) throws ISPACRuleException {

        try{
            //----------------------------------------------------------------------------------------------
            IClientContext cct =  rulectx.getClientContext();
            IInvesflowAPI invesFlowAPI = cct.getAPI();
            IEntitiesAPI entitiesAPI = invesFlowAPI.getEntitiesAPI();
            ITXTransaction tx = invesFlowAPI.getTransactionAPI();
            //----------------------------------------------------------------------------------------------

            //Se inicia el trámite de "Prescripciones técnicas" si el campo "Motivo de rechazo" no está vacío
            //if (motivoRechazo != null || motivoRechazo.equals("")){
                
                int stageId = rulectx.getStageId();//Id de la fase en el proceso (en ejecución)
                int taskIdCat = 0;
                
                //Obtener el taskId (id del trámite) del trámite de "Designación de participantes"
                
                //Obtenemos el id del trámite de "Designación de participantes" en el Catálogo a partir de su código
                String strQuery = "WHERE COD_TRAM = '"+ strTramDesignacionParticipantes +"'";
                LOGGER.debug("strQuery: "+strQuery);
                //IItemCollection collection = entitiesAPI.queryEntities(SpacEntities.SPAC_CT_TPDOC, strQuery);
                IItemCollection collection = entitiesAPI.queryEntities("SPAC_CT_TRAMITES", strQuery);
                Iterator<?> it = collection.iterator();
                
                if (it.hasNext()){
                    taskIdCat = ((IItem)it.next()).getInt("ID");
                    
                } else {
                    throw new ISPACInfo("No se ha encontrado el trámite de Prescripciones técnicas.");
                }
                
                //Obtenemos el id de la fase actual "Fase petición de ofertas" en el procedimiento, no en el catálogo
                int idStagePcd = 0;
                strQuery = "WHERE ID = '"+ stageId +"'";
                LOGGER.debug("strQuery: "+strQuery);
                collection = entitiesAPI.queryEntities(SpacEntities.SPAC_FASES, strQuery);
                it = collection.iterator();
                
                if (it.hasNext()){
                    idStagePcd = ((IItem)it.next()).getInt("ID_FASE");
                    
                } else {
                    throw new ISPACInfo("No se ha encontrado la fase actual del expediente.");
                }
                
                //Obtenemos el id del trámite de "Designación de participantes" en el procedimiento, no en el catálogo
                int idTaskPcd = 0;
                strQuery = "WHERE ID_FASE = '"+ idStagePcd +"' AND ID_CTTRAMITE = '"+ taskIdCat +"'";
                LOGGER.debug("strQuery: "+strQuery);
                collection = entitiesAPI.queryEntities(SpacEntities.SPAC_P_TRAMITES, strQuery);
                it = collection.iterator();
                
                if (it.hasNext()){
                    idTaskPcd = ((IItem)it.next()).getInt("ID");
                    
                } else {
                    throw new ISPACInfo("No se ha encontrado la fase actual del expediente.");
                }
                
                
                tx.createTask(stageId, idTaskPcd);
                
                //Una vez creado el trámite de "Designación de participantes" se envía un correo de notificación
                //simpleMail();
                
                // 1. Obtener Participantes del expediente actual, con relación "Trasladado"
                String numExp = rulectx.getNumExp();
                IItemCollection participantes = ParticipantesUtil.getParticipantesByRol(cct, numExp, ParticipantesUtil._TIPO_TRASLADO, ParticipantesUtil.ID);
                
                // Para cada participante seleccionado enviar email
                for (int i=0; i<participantes.toList().size(); i++) {
                    IItem participante = (IItem) participantes.toList().get(i);
                    String direccionesCorreo = participante.getString(ParticipantesUtil.DIRECCIONTELEMATICA);
                    
                    if (direccionesCorreo != null){
                        StringTokenizer tokens = new StringTokenizer(direccionesCorreo, ";");
                        
                        while (tokens.hasMoreTokens()){
                            String strDestinatario = tokens.nextToken();
                            
                            if (participante != null && StringUtils.isNotEmpty(strDestinatario)) {
                                enviarCorreo(rulectx, strDestinatario);
                            }
                        }
                    }
                }        
            //}

        } catch(Exception e){            
            LOGGER.error(e.getMessage(), e);
        }
        return null;

    }

    private void enviarCorreo(IRuleContext rulectx, String strTo) throws ISPACException {
        try {
            //Se cambia la configuración del correo de ApplicationResources.properties a su archivo de configuración por entidad
            CorreoConfiguration correoConfig = CorreoConfiguration.getInstance(rulectx.getClientContext());
            
            String strHost = correoConfig.get(CorreoConfiguration.HOST_MAIL);
            String strPort = correoConfig.get(CorreoConfiguration.PORT_MAIL);
            // Variables comunes para el envío de email
            //String cCorreoOrigen = "SIGEM-DCR";
            String strFrom = correoConfig.get(CorreoConfiguration.DECR_FROM);
            
            String strAsunto = "[SIGEM] Solicitud documento de Designación de participantes "+rulectx.getNumExp();
            String strContenido = strAsunto + "</br></br>" + "Se solicita el documento de <b>Designación de participantes</b> para el expediente <b>" + rulectx.getNumExp() + "</b>";
            
            // Configurar correo
            Properties props = new Properties();
            props.put("mail.transport.protocol", "smtp");
            props.put("mail.smtp.host", strHost);
            props.put("mail.smtp.port", strPort);
            props.put("mail.smtp.auth", "true");
            Authenticator auth = new SMTPAuthenticator();
            Session mailSession = Session.getDefaultInstance(props, auth);
            //mailSession.setDebug(true);
            Transport transport = mailSession.getTransport();

            // Definir mensaje
            MimeMessage message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(strFrom));
            message.addRecipient(Message.RecipientType.TO, new InternetAddress(strTo));
            message.setSubject(strAsunto);
            message.setContent(strContenido, "text/html; charset=ISO-8859-1");

            // Envío del mensaje
            transport.connect();
            transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
            transport.close();
        
        } catch(ISPACRuleException e) {
                throw new ISPACRuleException(e);
            
        } catch(Exception e) {
            throw new ISPACRuleException("Error al enviar el correo electrónico de Convocatoria",e);
        }
    }
    
    
    public static void simpleMail() throws MessagingException {
        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", "smtp");
        props.setProperty("mail.host", "172.18.193.11");
        props.setProperty("mail.user", "tecsidel-vll\\rjr");
        props.setProperty("mail.password", "lediscet");

        Session mailSession = Session.getDefaultInstance(props, null);
        Transport transport = mailSession.getTransport();

        MimeMessage message = new MimeMessage(mailSession);
        message.setSubject("Testing javamail plain");
        message.setContent("This is a test", "text/plain");
        message.addRecipient(Message.RecipientType.TO, new InternetAddress("diego.teresa@tecsidel.es"));

        transport.connect();
        transport.sendMessage(message, message.getRecipients(Message.RecipientType.TO));
        transport.close();
      }
    
    private class SMTPAuthenticator extends javax.mail.Authenticator {
        public PasswordAuthentication getPasswordAuthentication() {
            String username = Messages.getString("cUsr_mail");
            String password = Messages.getString("cPwd_mail");
            return new PasswordAuthentication(username, password);
        }
    }
    
    public void cancel(IRuleContext rulectx) throws ISPACRuleException {
        // No se da nunca este caso
    }
}

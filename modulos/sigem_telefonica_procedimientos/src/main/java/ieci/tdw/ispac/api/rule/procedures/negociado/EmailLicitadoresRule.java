package ieci.tdw.ispac.api.rule.procedures.negociado;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.item.IItemCollection;
import ieci.tdw.ispac.api.messages.Messages;
import ieci.tdw.ispac.api.rule.IRule;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Properties;
import java.util.StringTokenizer;

import javax.mail.Authenticator;
import javax.mail.Message;
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
 * @date 26/11/2009
 * @propósito Se genera y envía automáticamente un email con el listado de licitadores a los participantes con rol trasladado.
 */
public class EmailLicitadoresRule implements IRule {

    /** Logger de la clase. */
    protected static final Logger LOGGER = Logger.getLogger(EmailLicitadoresRule.class);

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
            //----------------------------------------------------------------------------------------------
                
            String numExp = rulectx.getNumExp();

            // 1. Obtener el listado de Participantes del expediente actual con relación "Licitador"
            StringBuilder listadoLicitadores = new StringBuilder();
            IItemCollection participantes = ParticipantesUtil.getParticipantesByRol(cct, numExp, ParticipantesUtil._TIPO_LICITADOR, ParticipantesUtil.ID);

            // Para cada participante seleccionado añadirlo al listado de licitadores
            for (int i = 0; i < participantes.toList().size(); i++){
                IItem participante = (IItem) participantes.toList().get(i);
                
                if (listadoLicitadores.length() > 0){
                    listadoLicitadores.append("</br></br>");
                }
                
                listadoLicitadores.append(" - ").append(participante.getString(ParticipantesUtil.NDOC));
                
                if (StringUtils.isNotEmpty(participante.getString(ParticipantesUtil.NOMBRE))){
                    listadoLicitadores.append(", \t").append(participante.getString(ParticipantesUtil.NOMBRE));
                }
                
                if (StringUtils.isNotEmpty(participante.getString(ParticipantesUtil.DIRNOT))){
                    listadoLicitadores.append(", \t").append(participante.getString(ParticipantesUtil.DIRNOT));
                }
            }
            String sListadoLicitadores = null;
            
            if (listadoLicitadores != null){
                sListadoLicitadores = listadoLicitadores.toString();
            }
            
            // 2. Obtener Participantes del expediente actual con relación "Trasladado"
            participantes = ParticipantesUtil.getParticipantesByRol(cct, numExp, ParticipantesUtil._TIPO_TRASLADO, ParticipantesUtil.ID);

            // Para cada participante seleccionado enviar email
            for (int i = 0; i < participantes.toList().size(); i++){
                IItem participante = (IItem) participantes.toList().get(i);
                String direccionesCorreo = participante.getString(ParticipantesUtil.DIRECCIONTELEMATICA);
                
                if (direccionesCorreo != null){
                    StringTokenizer tokens = new StringTokenizer(direccionesCorreo, ";");
                    
                    while (tokens.hasMoreTokens()) {
                        String strDestinatario = tokens.nextToken();
                        
                        if (participante!=null && StringUtils.isNotEmpty(strDestinatario)) {
                            enviarCorreo(rulectx, strDestinatario, sListadoLicitadores);
                        }
                    }
                }
            }        
            
        } catch(Exception e){
            LOGGER.error(e.getMessage(), e);
        }
        return null;

    }

    private void enviarCorreo(IRuleContext rulectx, String strTo, String sListadoLicitadores) throws ISPACException {
        try {
            //Se cambia la configuración del correo de ApplicationResources.properties a su archivo de configuración por entidad
            CorreoConfiguration correoConfig = CorreoConfiguration.getInstance(rulectx.getClientContext());
            
            String strHost = correoConfig.get(CorreoConfiguration.HOST_MAIL);
            String strPort = correoConfig.get(CorreoConfiguration.PORT_MAIL);
            // Variables comunes para el envío de email
            //String cCorreoOrigen = "SIGEM-DCR";
            String strFrom = correoConfig.get(CorreoConfiguration.DECR_FROM);
            
            String strAsunto = "[SIGEM] Listado de licitadores del expediente "+rulectx.getNumExp();
            String strContenido = strAsunto + "</br></br>" + "A continuación se indica el listado de licitadores para el expediente <b>" + rulectx.getNumExp() + "</b> de Convocatoria de contratación:";
            strContenido+= "</br></br>" + sListadoLicitadores;
            
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

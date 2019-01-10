package es.dipucr.sigem.api.rule.common.utils;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACInfo;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.api.item.IItem;
import ieci.tdw.ispac.api.rule.IRuleContext;
import ieci.tdw.ispac.ispaclib.configuration.ConfigurationMgr;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tecdoc.sgm.core.exception.SigemException;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.mensajes_cortos.ServicioMensajesCortos;
import ieci.tecdoc.sgm.core.services.mensajes_cortos.dto.Attachment;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.log4j.Logger;

import Saicar.Recursos.Correo.Correo;
import es.dipucr.sigem.api.rule.common.correo.CorreoConfiguration;

/**
 * @author [eCenpri-Felipe]
 * @since 23.09.2010
 */
public class MailUtil {
	
	private static final Logger logger = Logger.getLogger(MailUtil.class);
	
	//Constantes de envío de mensajes
	private static final String NOTIF_TYPE_EMAIL = "EM";
//	private static final String NOTIF_TYPE_SMS 	 = "SM";
	//Variable FROM para el envío de mails
	private static final String EMAIL_FROM_VAR_NAME = "AVISO_FIRMANTE_EMAIL_FROM";
	
	private static final String NOTA_NO_RESPONDER = "NOTA_NO_RESPONDER";
	private static final String NOTA_NO_RESPONDER_TEXTO_PLANO = "NOTA_NO_RESPONDER_TEXTOPLANO"; //[eCenpri-Felipe #817]
	
	//[eCenpri-Felipe #739]
	//Separador de correos. En la clase oCorreo se usa la coma (,)
	public static String DEFAULT_MAIL_SEPARATOR = ";";
	private static String OTRO_MAIL_SEPARATOR = ",";
	
	public static String ESPACIADO_PRIMERA_LINEA = "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;";
	
	
	/**
	 * [eCenpri-Felipe] Llama al enviarCorreo de Teresa
	 * Evitamos ciertos parámetros que podemos obtener del rulectx
	 * Lo llamamos enviarCorreoConAcuses para aclararnos
	 * @param rulectx
	 * @param emailNotif
	 * @param contenido
	 * @param asunto
	 * @param attachment
	 * @param nombreAttachment
	 * @throws ISPACException
	 */
	public static void enviarCorreoConAcuses(IRuleContext rulectx, String emailNotif, String asunto, String contenido, 
			File attachment, String nombreAttachment, String nombreAcuse, boolean bAnadirCita) throws ISPACException 
	{
		if (StringUtils.isEmpty(nombreAcuse)) nombreAcuse = emailNotif;
		enviarCorreoConAcuses(rulectx, emailNotif, attachment, contenido, asunto, nombreAttachment, nombreAcuse, bAnadirCita);
	}
	
	/**
	 * [eCenpri-Felipe] Llama al enviarCorreo de Teresa
	 * Evitamos ciertos parámetros que podemos obtener del rulectx
	 * Lo llamamos enviarCorreoConAcuses para aclararnos
	 * @param rulectx
	 * @param emailNotif
	 * @param contenido
	 * @param asunto
	 * @param attachment
	 * @param nombreAttachment
	 * @throws ISPACException
	 */
	public static void enviarCorreoConAcusesYVariables(IRuleContext rulectx, String emailNotif, String spacVarAsunto, String spacVarContenido, 
			Map<String,String> variables, File attachment, String nombreAttachment, String nombreAcuse, boolean bAnadirCita) throws ISPACException{ 
			enviarCorreoConAcusesYVariables(rulectx, emailNotif, spacVarAsunto, spacVarContenido, 
					variables, attachment, nombreAttachment, nombreAcuse, bAnadirCita, null);
			
	}
	public static String[] enviarCorreoConAcusesYVariables(IRuleContext rulectx, String emailNotif, String spacVarAsunto, String spacVarContenido, Map<String,String> variables, File attachment, String nombreAttachment, String nombreAcuse, boolean bAnadirCita, List<Object[]> embebed) throws ISPACException{
		
		String[] resultado = null;
		String descripError = "";
		boolean enviadoEmail = false;
		Date dFechaEnvio = new Date();
		
		ClientContext cct = (ClientContext) rulectx.getClientContext();
		
		if (StringUtils.isEmpty(nombreAcuse)) nombreAcuse = emailNotif;
		
		String asunto = ConfigurationMgr.getVarGlobal(cct, spacVarAsunto);
		String contenido = ConfigurationMgr.getVarGlobal(cct, spacVarContenido);
		
		if (StringUtils.isNotBlank(asunto)) {
			asunto = StringUtils.replaceVariables(asunto, variables);
		}

		if (StringUtils.isNotBlank(contenido)) {
			contenido = StringUtils.replaceVariables(contenido, variables);
		}
		
		if(embebed == null){ //para que sigan funcionando las llamadas anteriores de la misma forma
			enviarCorreoConAcuses(rulectx, emailNotif, attachment, contenido, 
				asunto, nombreAttachment, nombreAcuse, bAnadirCita);
		}
		else{
			try{
				resultado = enviarCorreo(rulectx.getClientContext(), "", emailNotif, asunto, contenido, attachment, embebed);
				enviadoEmail = true;
				dFechaEnvio = new Date();
				DipucrCommonFunctions.insertarAcuseEmail(nombreAcuse ,dFechaEnvio, nombreAttachment, nombreAttachment, enviadoEmail,
						emailNotif, descripError, rulectx);
			}
			catch(ISPACException e){				
				if(e.getCause() instanceof SendFailedException){
					descripError = "Error en el envío a '" + emailNotif + "'. ";
				}
				else if(e.getCause() instanceof AddressException){
					descripError = "Error en la dirección de correo '" + emailNotif + "'. ";				
				}
				else{
					descripError = "Error al enviar el correo electrónico a '" + emailNotif + "'. ";
				}				
				DipucrCommonFunctions.insertarAcuseEmail(nombreAcuse ,dFechaEnvio, nombreAttachment, nombreAttachment, enviadoEmail, emailNotif, descripError, rulectx);
				throw new ISPACRuleException(descripError ,e);
			}
		}
		return resultado;
	}

	
	/** [Ticket #486 TCG](SIGEM creación método genérico para el envío de avisos por mail)
	 * @param rulectx
	 * @param emailNotif
	 * @param attachment
	 * @param contenido
	 * @param asunto
	 * @param nombreFichero: debe llevar el nombre con la extension, por ejemplo, Certificado de Asistencia.pdf
	 * @param nombreNotif
	 * @param bAnadirCita [eCenpri-Felipe] Si es true, se añade la cita al correo
	 * CUIDADO: Si añadimos cita, el texto del correo no podrá ser multilínea
	 * @throws ISPACException
	 */
	public static void enviarCorreoConAcuses(IRuleContext rulectx, String emailNotif, File attachment, String contenido, String asunto, String nombreFichero, String nombreNotif, boolean bAnadirCita) throws ISPACException{
		enviarCorreoConAcuses(rulectx, emailNotif, attachment, contenido, asunto, nombreFichero, nombreNotif, bAnadirCita, null);
	}
	
	public static void enviarCorreoConAcuses(IRuleContext rulectx, String emailNotif, File attachment, String contenido, String asunto, String nombreFichero, String nombreNotif, boolean bAnadirCita, List<Object[]> embebed) throws ISPACException{
		//Variable para saber si se ha enviado el email
		boolean bEnviadoEmail = false;
		//Variable de fecha de envío del email
		Date dFechaEnvio = null;
		//Descripcioón del error
		String sDescripError = "";
		
		try{
			//Se cambia la configuración del correo de ApplicationResources.properties a su archivo de configuración por entidad
			CorreoConfiguration correoConfig = CorreoConfiguration.getInstance(rulectx.getClientContext());
			
			String strHost = correoConfig.get(CorreoConfiguration.HOST_MAIL);
			String strPort = correoConfig.get(CorreoConfiguration.PORT_MAIL);
			String strFrom = correoConfig.get(CorreoConfiguration.CONV_FROM);
			String strLocation = correoConfig.get(CorreoConfiguration.CONV_LUGAR);
			
			String strContenido = contenido;
			
			String notasNoResponder = ConfigurationMgr.getVarGlobal(rulectx.getClientContext(), NOTA_NO_RESPONDER_TEXTO_PLANO);
			strContenido += notasNoResponder;
			
			String strAsunto = asunto;
	        SimpleDateFormat dateformat = new SimpleDateFormat("yyyyMMdd'T'HHmmss", new Locale("es"));
			String strDtStamp = dateformat.format(new Date());	

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
			message.addHeaderLine("method=REQUEST");
			//message.addHeaderLine("charset=UTF-8");
			message.addHeaderLine("component=VEVENT");
			message.setFrom(new InternetAddress(strFrom));
			message.addRecipient(Message.RecipientType.TO, new InternetAddress(emailNotif));
			message.setSubject(strAsunto);
			
			//Insertar el pie del correo
			strContenido = anadePieEmail(rulectx.getClientContext(), strContenido);

			//[eCenpri-Felipe] Si tenemos que añadir cita, añadimos el fichero ics
			BodyPart messageBodyPart = new MimeBodyPart();
			if (bAnadirCita){
				//Fichero iCalencar (ics)
				StringBuffer sbCalendar = new StringBuffer();
				sbCalendar.append("BEGIN:VCALENDAR\n");
				sbCalendar.append("PRODID:-//Microsoft Corporation//Outlook 9.0 MIMEDIR//EN\n"); 
				sbCalendar.append("VERSION:2.0\n");
				sbCalendar.append("METHOD:REQUEST\n");
				sbCalendar.append("BEGIN:VEVENT\n");
				sbCalendar.append("ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:MAILTO:"+emailNotif+"\n");
				sbCalendar.append("ORGANIZER:MAILTO:"+strFrom+"\n");
				sbCalendar.append("LOCATION:"+strLocation+"\n");
				sbCalendar.append("TRANSP:OPAQUE\n");
				sbCalendar.append("SEQUENCE:0\n");
				sbCalendar.append("DTSTAMP:"+strDtStamp+"\n");
				//sbCalendar.append("CATEGORIES:Meeting\n");
				sbCalendar.append("DESCRIPTION:"+strContenido+"\n\n");
				sbCalendar.append("SUMMARY:"+strAsunto+"\n");
				sbCalendar.append("PRIORITY:5\n");
				sbCalendar.append("CLASS:PUBLIC\n");
				//sbCalendar.append("BEGIN:VALARM\n");
				//sbCalendar.append("TRIGGER:PT1440M\n");
				//sbCalendar.append("ACTION:DISPLAY\n");
				//sbCalendar.append("DESCRIPTION:Reminder\n");
				//sbCalendar.append("END:VALARM\n");
				sbCalendar.append("END:VEVENT\n");
				sbCalendar.append("END:VCALENDAR");
	
				// Crear la parte con el texto y la cita 
				messageBodyPart.setHeader("Content-Class", "urn:content-classes:calendarmessage");
				messageBodyPart.setHeader("Content-ID","calendar_message");
				messageBodyPart.setDataHandler(new DataHandler(
					new ByteArrayDataSource(sbCalendar.toString(), "text/calendar")));
			}
			else{
				messageBodyPart.setText(strContenido);
			}
				
			// Crear la parte con el fichero adjunto
			MimeBodyPart attachFilePart = null;
			if (attachment != null){
				attachFilePart = new MimeBodyPart();
				FileDataSource fds = new FileDataSource(attachment);
				attachFilePart.setDataHandler(new DataHandler(fds));
				attachFilePart.setFileName(nombreFichero);
			}

			// Crear un Multipart y añadirlo al mensaje 
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			if (attachment!=null) multipart.addBodyPart(attachFilePart);
			message.setContent(multipart);

			// Envío del mensaje
	        transport.connect();
	        transport.sendMessage(message,
	            message.getRecipients(Message.RecipientType.TO));
	        transport.close();
	        bEnviadoEmail = true;
			dFechaEnvio = new Date();
			DipucrCommonFunctions.insertarAcuseEmail(nombreNotif ,dFechaEnvio, nombreFichero, nombreFichero, bEnviadoEmail, 
					emailNotif, sDescripError, rulectx);
		}
		catch(SendFailedException e){
			sDescripError = "Error en el envío a '" + emailNotif + "'. ";
			bEnviadoEmail = false;
			DipucrCommonFunctions.insertarAcuseEmail(nombreNotif ,dFechaEnvio, nombreFichero, nombreFichero, bEnviadoEmail, 
					emailNotif, sDescripError, rulectx);
			throw new ISPACRuleException(sDescripError ,e);

		}
		catch(AddressException e){
			sDescripError = "Error en la dirección de correo '" + emailNotif + "'. ";
			bEnviadoEmail = false;
			DipucrCommonFunctions.insertarAcuseEmail(nombreNotif ,dFechaEnvio, nombreFichero, nombreFichero, bEnviadoEmail, 
					emailNotif, sDescripError, rulectx);
        	throw new ISPACRuleException(sDescripError ,e);
		}
		catch(Exception e){
			sDescripError = "Error al enviar el correo electrónico de Certificado de Asistencia a '" + emailNotif + "'. ";
			bEnviadoEmail = false;
			DipucrCommonFunctions.insertarAcuseEmail(nombreNotif ,dFechaEnvio, nombreFichero, nombreFichero, bEnviadoEmail, 
					emailNotif, sDescripError, rulectx);
						
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException(sDescripError ,e);
        }
	}
	
	private static class SMTPAuthenticator extends javax.mail.Authenticator {
    	public PasswordAuthentication getPasswordAuthentication() {
    		
    		CorreoConfiguration correoConfig;
        	String username =  "";
        	String password =  "";
			try {
				correoConfig = CorreoConfiguration.getInstance(new ClientContext());
				username =  correoConfig.get(CorreoConfiguration.USR_MAIL);
				password =  correoConfig.get(CorreoConfiguration.PWD_MAIL);
			} catch (ISPACRuleException e) {
				logger.error("Error al obtener el usuario y la contraseña "+ e.getMessage(), e);
			}
        	return new PasswordAuthentication(username, password);
        }
    }
	
	/**
	 * [eCenpri-Felipe #739]
	 * Correo normal con acuses de recibo
	 * Posibilidad de enviar a varios destinatarios separados por , o ;
	 * @param rulectx
	 * @param strTo
	 * @param strAsunto
	 * @param strContenido
	 * @throws ISPACException 
	 */
	public static void enviarCorreoVarios(IRuleContext rulectx, String strTo, 
			String strAsunto, String strContenido, boolean bAnadirCita) throws ISPACException{
		enviarCorreoVarios(rulectx, strTo, strAsunto, strContenido, bAnadirCita, null);
	}
	
	public static void enviarCorreoVarios(IRuleContext rulectx, String strTo, 
			String strAsunto, String strContenido, boolean bAnadirCita, List<Object[]> embebed)
			throws ISPACException{
		enviarCorreoVarios(rulectx, strTo, strAsunto, strContenido, bAnadirCita, null, embebed);
	}
	

	public static void enviarCorreoVarios(IRuleContext rulectx, String strTo, 
			String strAsunto, String strContenido, boolean bAnadirCita, File docAnexo, List<Object[]> embebed)
			throws ISPACException{
		
		String[] resultado = null;
		String descripError = "";
		boolean enviadoEmail = false;
		Date dFechaEnvio = new Date();
		
		String[] arrTo = separarDestinatarios(strTo);
		
		String destinatario = null;
		for (int i = 0; i < arrTo.length; i++){
			destinatario = arrTo[i];
			//Método de Teresa. Con acuses de recibo
			if(embebed == null){
				MailUtil.enviarCorreoConAcuses(rulectx, destinatario, strAsunto, 
					strContenido, docAnexo, (docAnexo == null ? "" : docAnexo.getName()), destinatario, bAnadirCita);
			}
			else{
				resultado = enviarCorreo(rulectx.getClientContext(), "", destinatario, strAsunto, strContenido, docAnexo, embebed);
				enviadoEmail = true;
				dFechaEnvio = new Date();
				if (null != docAnexo){
					DipucrCommonFunctions.insertarAcuseEmail(destinatario, dFechaEnvio, docAnexo.getName(), docAnexo.getName(), enviadoEmail, destinatario, descripError, rulectx);
				}
				else{
					DipucrCommonFunctions.insertarAcuseEmail(destinatario, dFechaEnvio, "", "", enviadoEmail, destinatario, descripError, rulectx);
				}
			}
		}
	}
	
	public static void enviarCorreoVarios(ClientContext cct, String strTo, 
			String strAsunto, String strContenido, boolean bAnadirCita, File docAnexo, List<Object[]> embebed)
			throws ISPACException{
		
		String[] arrTo = separarDestinatarios(strTo);
		
		String destinatario = null;
		for (int i = 0; i < arrTo.length; i++){
			destinatario = arrTo[i];
			enviarCorreo(cct, "", destinatario, strAsunto, strContenido, docAnexo, embebed);
		}
	}
	
	/**
	 * Envía un correo al destinatario especificado
	 *
	 * @param rulectx Contexto de la regla
	 * @param strTo Destinatario
	 * @param strAsunto Asunto del correo
	 * @param strContenido Contenido del mail
	 * @throws ISPACException
	 */
	public static void enviarCorreo(IClientContext cct, String strTo, String strAsunto, String strContenido) throws ISPACException {
		enviarCorreo(cct, strTo, strAsunto, strContenido, null, null);
	}
	
	public static void enviarCorreo(IClientContext cct, String strTo, String strAsunto, String strContenido, List<Object[]> embebed) throws ISPACException {		
		enviarCorreo(cct, strTo, strAsunto, strContenido, null, embebed);		
	}
	/**
	 * Envía un correo al destinatario especificado con un fichero adjunto
	 *
	 * @param rulectx Contexto de la regla
	 * @param strTo Destinatario
	 * @param strAsunto Asunto del correo
	 * @param strContenido Contenido del mail
	 * @param attachment Fichero que se adjunta
	 * @throws ISPACException
	 */
	public static void enviarCorreo(IRuleContext rulectx, String strTo, String strAsunto, String strContenido, File attachment) throws ISPACException 
	{
		enviarCorreo(rulectx.getClientContext(), strTo, strAsunto, strContenido, attachment, null);
	}
	
	public static void enviarCorreo(IClientContext cct, String strTo, String strAsunto, String strContenido, File attachment, List<Object[]> embebed) throws ISPACException{
		enviarCorreo(cct, "", strTo, strAsunto, strContenido, attachment, embebed);
	}
	public static String[] enviarCorreo(IClientContext cct, String from, String strTo, String strAsunto, String strContenido, File attachment, List<Object[]> embebed) throws ISPACException{
		try{
			String dir[] = null;
			
			CorreoConfiguration correoConfig = CorreoConfiguration.getInstance(cct);
			
			String strHost = correoConfig.get(CorreoConfiguration.HOST_MAIL);
			String strPort = correoConfig.get(CorreoConfiguration.PORT_MAIL);

			//MQE ticket #817 Añadir información de no responder
//			String strFrom = Messages.getString("cDecr_From"); //[eCenpri-Felipe #306]
			String strFrom = from;
			if(StringUtils.isEmpty(strFrom))
				strFrom = ConfigurationMgr.getVarGlobal(cct, EMAIL_FROM_VAR_NAME);
			//MQE Fin modificaciones No responder
			
        	String strUser = correoConfig.get(CorreoConfiguration.USR_MAIL);
        	String strPwd = correoConfig.get(CorreoConfiguration.PWD_MAIL);

        	//MQE ticket #817 Añadir información de no responder
        	String notasNoResponder = ConfigurationMgr.getVarGlobal(cct, NOTA_NO_RESPONDER);
//			strContenido = strContenido +  "</br></br>"+notasNoResponder; //[eCenpri-Felipe #817] Eliminar los br del mensaje, añadirlos a la constante
        	strContenido += notasNoResponder;
			//MQE Fin modificaciones no responder
        	
        	strContenido = MailUtil.anadePieEmail(cct, strContenido);
        	       	
			// Configurar correo
			Correo oCorreo = new Correo(strHost, Integer.parseInt(strPort), strUser, strPwd);
			oCorreo.ponerTo(0, strTo);
			if (strFrom != null && !strFrom.equals("")){
				oCorreo.ponerFrom(strFrom);
				oCorreo.ponerAsunto(strAsunto);
				oCorreo.ponerContenido(strContenido, true);
				// Adjuntar fichero al email
				if (null != attachment){
					oCorreo.adjuntar(attachment.getParent(), true, attachment.getName());
				}
				
				if(embebed != null){
					for(Object[] archivo : embebed){
						oCorreo.embeber((String)archivo[0], (Boolean)archivo[1], (String)archivo[2], (String)archivo[3]);
					}
					
				}
				// Enviar email
				dir = oCorreo.enviar();
				String error = "";
				if (dir != null){
					for (int nI = 0; nI < dir.length; nI++){
						error = error + '\r' + dir[nI];
					}
					throw new ISPACRuleException(error);
				}
			} 
			else{
				String cTexto = 
					"No es posible enviar el correo electrónico a '" + strTo + "'" +
					"Por favor, póngase en contacto con el administrador del sistema";
				throw new ISPACInfo(cTexto);
			}
			return dir;
		}
		catch(SendFailedException e){
			String descripError = "Error en el envío a '" + strTo + "'. " + e.getMessage();
			throw new ISPACException(descripError, e);

		}
		catch(AddressException e){
			String descripError = "Error en la dirección de correo '" + strTo + "'. " + e.getMessage();
        	throw new ISPACException(descripError ,e);
		}
		catch(Exception e) {
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("Error al enviar el correo electrónico a '" + strTo + "'. " + e.getMessage(), e);
        }
	}
	
	public static String[] enviarCorreo(ClientContext cct, String from, String strTo, String strAsunto, String strContenido, File attachment, List<Object[]> embebed) throws ISPACException{
		try{
			String dir[] = null;
			
			CorreoConfiguration correoConfig = CorreoConfiguration.getInstance(cct);
			
			String strHost = correoConfig.get(CorreoConfiguration.HOST_MAIL);
			String strPort = correoConfig.get(CorreoConfiguration.PORT_MAIL);

			//MQE ticket #817 Añadir información de no responder
//			String strFrom = Messages.getString("cDecr_From"); //[eCenpri-Felipe #306]
			String strFrom = from;
			if(StringUtils.isEmpty(strFrom))
				strFrom = ConfigurationMgr.getVarGlobal(cct, EMAIL_FROM_VAR_NAME);
			//MQE Fin modificaciones No responder
			
        	String strUser = correoConfig.get(CorreoConfiguration.USR_MAIL);
        	String strPwd = correoConfig.get(CorreoConfiguration.PWD_MAIL);

        	//MQE ticket #817 Añadir información de no responder
        	String notasNoResponder = ConfigurationMgr.getVarGlobal(cct, NOTA_NO_RESPONDER);
//			strContenido = strContenido +  "</br></br>"+notasNoResponder; //[eCenpri-Felipe #817] Eliminar los br del mensaje, añadirlos a la constante
        	strContenido += notasNoResponder;
			//MQE Fin modificaciones no responder
        	
        	strContenido = MailUtil.anadePieEmail(cct, strContenido);
        	       	
			// Configurar correo
			Correo oCorreo = new Correo(strHost, Integer.parseInt(strPort), strUser, strPwd);
			oCorreo.ponerTo(0, strTo);
			if (strFrom != null && !strFrom.equals("")){
				oCorreo.ponerFrom(strFrom);
				oCorreo.ponerAsunto(strAsunto);
				oCorreo.ponerContenido(strContenido, true);
				// Adjuntar fichero al email
				if (null != attachment){
					oCorreo.adjuntar(attachment.getParent(), true, attachment.getName());
				}
				
				if(embebed != null){
					for(Object[] archivo : embebed){
						oCorreo.embeber((String)archivo[0], (Boolean)archivo[1], (String)archivo[2], (String)archivo[3]);
					}
					
				}
				// Enviar email
				dir = oCorreo.enviar();
				String error = "";
				if (dir != null){
					for (int nI = 0; nI < dir.length; nI++){
						error = error + '\r' + dir[nI];
					}
					throw new ISPACRuleException(error);
				}
			} 
			else{
				String cTexto = 
					"No es posible enviar el correo electrónico a '" + strTo + "'" +
					"Por favor, póngase en contacto con el administrador del sistema";
				throw new ISPACInfo(cTexto);
			}
			return dir;
		}
		catch(SendFailedException e){
			String descripError = "Error en el envío a '" + strTo + "'. " + e.getMessage();
			throw new ISPACException(descripError, e);

		}
		catch(AddressException e){
			String descripError = "Error en la dirección de correo '" + strTo + "'. " + e.getMessage();
        	throw new ISPACException(descripError ,e);
		}
		catch(Exception e) {
        	if (e instanceof ISPACRuleException)
			    throw new ISPACRuleException(e);
        	throw new ISPACRuleException("Error al enviar el correo electrónico a '" + strTo + "'. " + e.getMessage(), e);
        }
	}
	
	
	/**
	 * [eCenpri-Felipe #739]
	 * Separa una lista de mails en formato cadena y separados por ; o , y devuelve un vector de String
	 * @param strListaMails
	 * @return
	 */
	public static String[] separarDestinatarios(String strListaMails){
		
		if (strListaMails.contains(OTRO_MAIL_SEPARATOR)){
			strListaMails = strListaMails.replace(OTRO_MAIL_SEPARATOR, DEFAULT_MAIL_SEPARATOR);
		}
		String[] arrListaMails = strListaMails.split(DEFAULT_MAIL_SEPARATOR);
		for (int i = 0; i < arrListaMails.length; i++){
			arrListaMails[i] = arrListaMails[i].trim();
		}
		return arrListaMails;
	}
	
	/**
	 * [eCenpri-Felipe #743]
	 * Sobrecarga del método para envío a circuitos
	 * @param rulectx
	 * @param signStep
	 * @param strVarAsunto
	 * @param strVarContent
	 * @param variables
	 * @throws ISPACException
	 * @throws SigemException
	 */
	public static void enviarCorreoCircuitoConVariables
			(IRuleContext rulectx, IItem signStep, String spacVarAsunto, String spacVarContent, Map<String,String> variables)
			throws ISPACException, SigemException
	{
		logger.warn(signStep.getString("ID_CIRCUITO") + "; " + signStep.getString("NOMBRE_FIRMANTE") + "; " + signStep.getString("DIR_NOTIF"));
		String strTipoNotif = signStep.getString("TIPO_NOTIF");
		String strDirNotif = signStep.getString("DIR_NOTIF");
		
		enviarCorreoConVariables(rulectx.getClientContext(), strTipoNotif, strDirNotif, 
				spacVarAsunto, spacVarContent, null, variables);
	}
	
	/**
	 * [eCenpri-Felipe #743]
	 * Sobrecarga del método para envío directo a mail
	 * @param rulectx
	 * @param strDirNotif
	 * @param spacVarAsunto
	 * @param spacVarContent
	 * @param variables
	 * @throws ISPACException
	 * @throws SigemException
	 */
	public static void enviarCorreoConVariables
			(IRuleContext rulectx, String strDirNotif, String spacVarAsunto, 
					String spacVarContent, Attachment attachment, Map<String,String> variables)
			throws ISPACException, SigemException
	{
		enviarCorreoConVariables(rulectx.getClientContext(), NOTIF_TYPE_EMAIL, strDirNotif, 
				spacVarAsunto, spacVarContent, attachment, variables);
	}
	
	/**
	 * [eCenpri-Felipe 3#304]
	 * Sobrecarga del método para envío directo a mail (con clientContext)
	 * @param ctx
	 * @param strDirNotif
	 * @param spacVarAsunto
	 * @param spacVarContent
	 * @param variables
	 * @throws ISPACException
	 * @throws SigemException
	 */
	public static void enviarCorreoConVariables
			(IClientContext ctx, String strDirNotif, String spacVarAsunto, 
					String spacVarContent, Attachment attachment, Map<String,String> variables)
			throws ISPACException, SigemException
	{
		enviarCorreoConVariables(ctx, NOTIF_TYPE_EMAIL, strDirNotif, 
				spacVarAsunto, spacVarContent, attachment, variables);
	}
	
	/**
	 * Envia un correo utilizando las constantes de la tabla SPAC_VARS
	 * y un mapeo "variables" de etiquetas y valores
	 * Enviando el correo de esta forma, con la clase ServicioMensajesCortos, nos
	 * respeta los saltos de línea, cosa que no sucede 
	 * @param rulectx
	 * @param strTipoNotif
	 * @param strDirNotif
	 * @param strVarAsunto
	 * @param strVarContent
	 * @param variables
	 * @throws ISPACException
	 * @throws SigemException
	 */
	private static void enviarCorreoConVariables
			(IClientContext ctx, String strTipoNotif, String strDirNotif, String spacVarAsunto, 
					String spacVarContent, Attachment attachment, Map<String,String> variables)
			throws ISPACException, SigemException
	{
		
		if (NOTIF_TYPE_EMAIL.equals(strTipoNotif) && StringUtils.isNotEmpty(strDirNotif)){
			
			String from = ConfigurationMgr.getVarGlobal(ctx, EMAIL_FROM_VAR_NAME);
			String subject = ConfigurationMgr.getVarGlobal(ctx, spacVarAsunto);
			String content = ConfigurationMgr.getVarGlobal(ctx, spacVarContent);

			if (StringUtils.isNotBlank(subject)) {
				subject = StringUtils.replaceVariables(subject, variables);
			}

			if (StringUtils.isNotBlank(content)) {
				content = StringUtils.replaceVariables(content, variables);
			}
			
			//MQE ticket #817 Añadir información de no responder
			String notasNoResponder = ConfigurationMgr.getVarGlobal(ctx, NOTA_NO_RESPONDER_TEXTO_PLANO);
//			content += "</br></br>"+notasNoResponder; //[eCenpri-Felipe #817] Eliminar los br del mensaje, añadirlos a la constante
			content += notasNoResponder;
			//MQE Fin modificaciones no responder
			
			content = MailUtil.anadePieEmail(ctx, content);
			
			ServicioMensajesCortos svc = LocalizadorServicios.getServicioMensajesCortos();
			if (null == attachment){
				svc.sendMail(from, new String[] { strDirNotif }, null, null, subject, content, null);
			}
			else{
				svc.sendMail(from, new String[] { strDirNotif }, null, null, 
						subject, content, new Attachment[] { attachment });
			}
		}
	}
	
	/**
	 * [eCenpri-Felipe #1224]
	 * Sobrecarga del método para envío directo a mail
	 * @param rulectx
	 * @param strDirNotif
	 * @param spacVarAsunto
	 * @param spacVarContent
	 * @param attachment
	 * @param variables
	 * @param bHTML
	 * @throws ISPACException
	 * @throws SigemException
	 */
	public static void enviarCorreoConVariablesUsoExterno
			(IRuleContext rulectx, String strDirNotif, String spacVarAsunto, 
			String spacVarContent, File attachment, Map<String,String> variables, boolean bHTML)
			throws ISPACException, SigemException
	{
		enviarCorreoConVariablesUsoExterno(rulectx, NOTIF_TYPE_EMAIL, strDirNotif, 
				spacVarAsunto, spacVarContent, attachment, variables, bHTML);
	}
	
	/**
	 * [eCenpri-Felipe #1224]
	 * Modificamos el método enviarCorreoConVariables pues la clase ServicioMensajesCortos no
	 * está funcionando correctamente cuando se envían correos fuera de Diputación.
	 * Utilizamos la clase oCorreo
	 * @param rulectx
	 * @param strTipoNotif
	 * @param strDirNotif
	 * @param strVarAsunto
	 * @param strVarContent
	 * @param attachment
	 * @param variables
	 * @throws ISPACException
	 * @throws SigemException
	 */
	private static void enviarCorreoConVariablesUsoExterno
			(IRuleContext rulectx, String strTipoNotif, String strDirNotif, String spacVarAsunto, 
					String spacVarContent, File attachment, Map<String,String> variables, boolean bHTML)
			throws ISPACException, SigemException
	{
		
		if (NOTIF_TYPE_EMAIL.equals(strTipoNotif) && StringUtils.isNotEmpty(strDirNotif)){
			
			IClientContext ctx = rulectx.getClientContext();
			
			String subject = ConfigurationMgr.getVarGlobal(ctx, spacVarAsunto);
			String content = ConfigurationMgr.getVarGlobal(ctx, spacVarContent);

			if (StringUtils.isNotBlank(subject)) {
				subject = StringUtils.replaceVariables(subject, variables);
			}

			if (StringUtils.isNotBlank(content)) {
				content = StringUtils.replaceVariables(content, variables);
				
				if(!bHTML){
					content = content.replace("\n", "<br/>");
				}
			}
			
			enviarCorreo(rulectx, strDirNotif, subject, content, attachment);
		}
	}
	
	/**
	 * Envia un correo utilizando las constantes de la tabla SPAC_VARS
	 * y un mapeo "variables" de etiquetas y valores
	 * Enviando el correo de esta forma, con la clase ServicioMensajesCortos, nos
	 * respeta los saltos de línea, cosa que no sucede 
	 * @param rulectx
	 * @param signStep
	 * @param subject
	 * @param content
	 * @param variables
	 * @throws ISPACException
	 * @throws SigemException
	 */
	public static void enviarCorreoCircuito
			(IRuleContext rulectx, IItem signStep, String subject, String content)
			throws ISPACException, SigemException
	{
		String strTipoNotif = signStep.getString("TIPO_NOTIF");
		String strDirNotif = signStep.getString("DIR_NOTIF");
		
		if (NOTIF_TYPE_EMAIL.equals(strTipoNotif) && StringUtils.isNotEmpty(strDirNotif)){
			
			IClientContext ctx = rulectx.getClientContext();
			
			//MQE ticket #817 Añadir información de no responder
			String from = ConfigurationMgr.getVarGlobal(ctx, EMAIL_FROM_VAR_NAME);
			
			String notasNoResponder = ConfigurationMgr.getVarGlobal(ctx, NOTA_NO_RESPONDER_TEXTO_PLANO);
//			content += "</br></br>"+notasNoResponder; //[eCenpri-Felipe #817] Eliminar los br del mensaje, añadirlos a la constante
			content += notasNoResponder;
			//MQE fin modificaciones Ticket #817			
			
			ServicioMensajesCortos svc = LocalizadorServicios.getServicioMensajesCortos();
			svc.sendMail(from, new String[] { strDirNotif }, null, null, subject, content, null);
		}
	}
	
	/**
	 * Comprueba que el correo sea correcto
	 * @param correo
	 * @return boolean
	 */
	
	public static boolean isEmail(String correo) {
        Pattern pat = null;
        Matcher mat = null;        
        pat = Pattern.compile("^([0-9a-zA-Z]([_.w]*[0-9a-zA-Z])*@([0-9a-zA-Z][-w]*[0-9a-zA-Z].)+([a-zA-Z]{2,9}.)+[a-zA-Z]{2,3})$");
        mat = pat.matcher(correo);
        if (mat.find()) {
            return true;
        }else{
            return false;
        }        
    }
	
	/**
	 * Formatea el contenido para que añada la cabecera con las imágenes de la diputación y el pie de página
	 * @param ctx
	 * @param asunto
	 * @param contenido
	 * @return String
	 * @throws ISPACRuleException 
	 */
	private static String anadePieEmail(IClientContext ctx, String contenido) throws ISPACRuleException{
		try{
			CorreoConfiguration correoConfig = CorreoConfiguration.getInstance(ctx);
			
			String lugar = correoConfig.get(CorreoConfiguration.CONV_LUGAR);
			String entidad = correoConfig.get(CorreoConfiguration.CONV_ENTIDAD);
			String sede = correoConfig.get(CorreoConfiguration.CONV_SEDE);
			
			contenido = contenido+  "<p align=center>"
	        		+ entidad
	        		+ "<br/>"
	        		+ lugar
	        		+ "<br/>"
	        		+ "<b>Sede electrónica: <a href=\""+sede+"\">"+sede+"</a><b/>"
	        		+ "<br/>"
	        		+ "</p>";
		}
		catch(Exception e){
			throw new ISPACRuleException("Error al añadir el pie del email", e);
		}	
		
		return contenido;
		
	}
	
	/**
	 * Formatea el contenido para que añada la cabecera con las imágenes de la diputación y el pie de página
	 * @param rulectx
	 * @param asunto
	 * @param contenido
	 * @return String
	 * @throws ISPACRuleException 
	 */
	public static String formateContenidoEmail(IRuleContext rulectx, String asunto, String contenido) throws ISPACRuleException{
		try{
			CorreoConfiguration correoConfig = CorreoConfiguration.getInstance(rulectx.getClientContext());
			
			String lugar = correoConfig.get(CorreoConfiguration.CONV_LUGAR);
			String entidad = correoConfig.get(CorreoConfiguration.CONV_ENTIDAD);
			String sede = correoConfig.get(CorreoConfiguration.CONV_SEDE);
			
			contenido = contenido
					+ "<br/>"
					+ MailUtil.ESPACIADO_PRIMERA_LINEA + "Expediente: <b>" + rulectx.getNumExp() + "</b>."
					+ "<br/>"
					+ MailUtil.ESPACIADO_PRIMERA_LINEA + "Asunto: <b>" + asunto + "</b>."
					+ "<br/>";
			
			contenido = "<img src='cid:escudo' width='200px'/>"
	        		+ "<p align=justify>"
	        		+ MailUtil.ESPACIADO_PRIMERA_LINEA + "Estimado señor/a:" 
	        		+ "<br/> <br/>" 
	        		+ MailUtil.ESPACIADO_PRIMERA_LINEA +  contenido
	        		+ " </p>"
	        		+ "<br/> <br/>";
		}
		catch(Exception e){
			throw new ISPACRuleException("Error al recuperar el expediente: " + rulectx.getNumExp(), e);
		}	
		
		return contenido;
		
	}
	
	/**
	 * Envío de correos para tareas de monitoriación/demonios
	 * @param rulectx
	 * @param strTo
	 * @param strAsunto
	 * @param strContenido
	 * @throws Exception 
	 */
	public static void enviarCorreoInfoTask(IClientContext ctx, String strTo, String strAsunto, String strContenido)
			throws Exception{
		
		String from = ConfigurationMgr.getVarGlobal(ctx, EMAIL_FROM_VAR_NAME);
		
		String notasNoResponder = ConfigurationMgr.getVarGlobal(ctx, NOTA_NO_RESPONDER_TEXTO_PLANO);
		strContenido += notasNoResponder;
		
		ServicioMensajesCortos svc = LocalizadorServicios.getServicioMensajesCortos();
		String[] arrTo = separarDestinatarios(strTo);
		svc.sendMail(from, arrTo, null, null, strAsunto, strContenido, null);
	}
	
}

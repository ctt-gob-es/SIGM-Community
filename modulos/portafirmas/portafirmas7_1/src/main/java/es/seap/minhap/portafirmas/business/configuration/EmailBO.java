/* Copyright (C) 2012-13 MINHAP, Gobierno de España
   This program is licensed and may be used, modified and redistributed under the terms
   of the European Public License (EUPL), either version 1.1 or (at your
   option) any later version as soon as they are approved by the European Commission.
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
   or implied. See the License for the specific language governing permissions and
   more details.
   You should have received a copy of the EUPL1.1 license
   along with this program; if not, you may find it at
   http://joinup.ec.europa.eu/software/page/eupl/licence-eupl */

package es.seap.minhap.portafirmas.business.configuration;

import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import org.apache.log4j.Logger;
import org.joda.time.DateTime;
import org.quartz.JobDataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.notice.configuration.EmailNoticeConfiguration;
import es.seap.minhap.portafirmas.utils.notice.configuration.NoticeConfiguration;
import es.seap.minhap.portafirmas.utils.notice.message.EmailNoticeMessage;
import es.seap.minhap.portafirmas.utils.notice.message.NoticeMessage;
import es.seap.minhap.portafirmas.utils.notice.service.EmailNoticeServiceJob;
import es.seap.minhap.portafirmas.utils.quartz.QuartzInvoker;
import es.seap.minhap.portafirmas.web.beans.FileAttachedDTO;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class EmailBO implements Serializable {

	private static final long serialVersionUID = 3067958522784443540L;

	private static final Logger log = Logger.getLogger(EmailBO.class);
	
	private String user = null;
	private String password = null;

	@Autowired
	private QuartzInvoker quartzInvoker;
	
	/**
	 * M&eacute;todo que comprueba que notificaci&oacute;n por email tiene todos los par&aacute;metros v&aacute;lidos.
	 * @param emailConfiguration Configuraci&oacute;n del email.
	 * @param emailMessage Datos del email.
	 * @return "True" si todo es v&aacute;lido, "false" en caso contrario.
	 */
	public boolean checkEmailNotice(
			EmailNoticeConfiguration emailConfiguration,
			EmailNoticeMessage emailMessage) {
		log.info("checkEmailNotice init");
		boolean valid = true;
		if (emailConfiguration != null && emailMessage != null) {
			// check connection data
			valid = checkEmailConnectionData(emailConfiguration);
			// check message data
			if (valid){
				valid = checkEmailMessageData(emailMessage);
			}
		} else {
			valid = false;
		}
		log.info("checkEmailNotice false");
		return valid;
	}

	/**
	 * M&eacute;todo que comprueba que los par&aacute;metros de conexi&oacute;n de una configuraci&oacute;n de email sean v&aacute;lidos.
	 * @param noticeConfiguration Configuraci&oacute;n de notificaci&oacute;n de email.
	 * @return "True" si la configuraci&oacute;n es v&aacute;lida, "false" en caso contrario.
	 */
	private boolean checkEmailConnectionData(EmailNoticeConfiguration noticeConfiguration) {
		log.info("checkConnectionData init");
		boolean valid = true;
		if (noticeConfiguration.getMailUser() == null ||
			noticeConfiguration.getMailUser().equals("") ||
			noticeConfiguration.getMailPassword() == null ||
			noticeConfiguration.getMailPassword().equals("") ||
			noticeConfiguration.getMailRemitter() == null ||
			noticeConfiguration.getMailRemitter().equals("") ||
			noticeConfiguration.getSmtpHost() == null ||
			noticeConfiguration.getSmtpHost().equals("")) {
			valid = false;
		}
		log.info("checkConnectionData end");
		return valid;
	}

	/**
	 * M&eacute;todo que comprueba que los datos de un mensaje de correo son v&aacute;lidos.
	 * @param emailMessage mensaje de correo a validar.
	 * @return "True" si el mensaje es v&aacute;lido, "false" en caso contrario.
	 */
	private boolean checkEmailMessageData(EmailNoticeMessage emailMessage) {
		boolean valid = true;
		log.info("checkEmailMessageData init");
		if (emailMessage.getPlainText() == null ||
			emailMessage.getPlainText().toString().isEmpty() ||
			emailMessage.getSubject() == null ||
			emailMessage.getSubject().equals("") ||
			emailMessage.getReceivers() == null ||
			emailMessage.getReceivers().isEmpty()) {
			valid = false;
		}
		log.info("checkEmailMessageData end");
		return valid;
	}

	/**
	 * M&eacute;todo que obtiene la configuraci&oacute;n y lanza un env&iacute;o de un email.
	 * @param emailConfiguration Configuraci&oacute;n del email.
	 * @param emailMessage Datos del email.
	 * @throws UnsupportedEncodingException Error de codificaci&oacute;n del email.
	 * @throws MessagingException Error de env&iacute;o.
	 */
	public void doNotice(NoticeConfiguration emailConfiguration,
			NoticeMessage emailMessage, List<FileAttachedDTO> ficheros) throws UnsupportedEncodingException,
			MessagingException {

		// Configuration attributes
		String host = ((EmailNoticeConfiguration) emailConfiguration).getSmtpHost();
		String fromName = ((EmailNoticeConfiguration) emailConfiguration).getFromName();
		String from = ((EmailNoticeConfiguration) emailConfiguration).getMailRemitter();
		String port = ((EmailNoticeConfiguration) emailConfiguration).getSmtpPort();
		// Authentication
		String authenticate = ((EmailNoticeConfiguration) emailConfiguration).getAuthenticate();
		user = ((EmailNoticeConfiguration) emailConfiguration).getMailUser();
		password = ((EmailNoticeConfiguration) emailConfiguration).getMailPassword();
		// Message attributes
		String subject = ((EmailNoticeMessage) emailMessage).getSubject();
		StringBuilder plainText = ((EmailNoticeMessage) emailMessage).getPlainText();
		StringBuilder htmlText = ((EmailNoticeMessage) emailMessage).getHtmlText();
		List<String> receivers = ((EmailNoticeMessage) emailMessage).getReceivers();
		
		// Send mail
		sendMail(host, port, from, fromName, receivers, subject, plainText, htmlText, authenticate, ficheros);

	}

	/**
	 * M&eacute;todo que configura y env&iacute;a un email.
	 * @param host Servidor smtp.
	 * @param port Puerto del servidor smtp.
	 * @param from Direcci&oacute;n de origen.
	 * @param fromName Nombre del usuario que env&iacute;a el email.
	 * @param receivers Listado con las direcciones de destino.
	 * @param subject Asunto del email.
	 * @param plainText Texto del email.
	 * @param htmlText Texto del email en html.
	 * @param authenticate 'S' cuando el host requiere autenticaci&oacute;n, 'N' en caso contrario.
	 * @throws UnsupportedEncodingException Error en la codificaci&oacute;n del mensaje.
	 * @throws MessagingException Error en el env&iacute;o.
	 */
	private void sendMail(String host, String port, String from,
			String fromName, List<String> receivers, String subject,
			StringBuilder plainText, StringBuilder htmlText, String authenticate, List<FileAttachedDTO> ficheros)
			throws UnsupportedEncodingException, MessagingException {
		// Get system properties
		Properties props = System.getProperties();

		// Setup mail server
		props.put(Constants.MAIL_SERVER, host);
		// If smtp port is not given, use default port (25)
		if (port != null && !port.equals("")) {
			props.put(Constants.MAIL_SERVER_PORT, port);
		}
		
		// Seguridad STARTLS activada
		props.put(Constants.MAIL_SERVER_STARTTLS, true);

		if (authenticate != null && authenticate.equals(Constants.C_YES)) {
			props.put(Constants.MAIL_SERVER_AUTH, Constants.MAIL_CONS_TRUE);
		} else if (authenticate != null && authenticate.equals(Constants.C_NOT)) {
			props.put(Constants.MAIL_SERVER_AUTH, Constants.MAIL_CONS_FALSE);
		}

		//TODO: Habr&aacute; que ver si se encuentra un m&eacute;todo mejor que este.
		// Por lo que he visto, parece que no hay manera de enviar mails con UTF-8. El mensaje indica en la cabecera que el mail est&aacute;
		// en UTF-8, pero el texto est&aacute; en otro juego de caracteres y en el mail salen cosas raras
		props.put(Constants.MAIL_MIME_CHARSET, Constants.MAIL_MIME_CHARSET_UTF8);
		
//		props.setProperty("mail.host", "smtp.gmail.com");
//        props.setProperty("mail.smtp.port", "587");
//        props.setProperty("mail.smtp.auth", "true");
//        props.setProperty("mail.smtp.starttls.enable", "true");
		
		Session session = Session.getInstance(props, new MailAuthenticator());
		// Define message
		MimeMessage message = new MimeMessage(session);
		InternetAddress ia = new InternetAddress(from);
		ia.setPersonal(fromName);
		message.setFrom(ia);

		// Add recipients (always BCC)
		if (receivers != null && !receivers.isEmpty()) {
			for (String aux: receivers) {
				message.addRecipient(Message.RecipientType.BCC,	new InternetAddress(aux));
			}
		}

		// Set subject
		message.setSubject(subject);
		
		if (htmlText == null) {
			message.setText(plainText.toString());
		} else if (ficheros!=null && !ficheros.isEmpty()){
			
			Multipart multipart = new MimeMultipart();
			
			BodyPart messageBodyPart = new MimeBodyPart();
			
	         // Now set the actual message
	         messageBodyPart.setText(plainText.toString());
			
	         multipart.addBodyPart(messageBodyPart);
	         
			for(int i=0; i<ficheros.size(); ++i){
				messageBodyPart = new MimeBodyPart();
				ByteArrayDataSource source = new ByteArrayDataSource(ficheros.get(i).getContenido(), ficheros.get(i).getMime());
				messageBodyPart.setDataHandler(new DataHandler(source));
				messageBodyPart.setFileName(ficheros.get(i).getNombre());
				multipart.addBodyPart(messageBodyPart);
			}

			message.setContent(multipart);
		} else {
			MimeMultipart multipart = new MimeMultipart(Constants.MAIL_ALTERNATIVE_TYPE);

			// Create new message part (plain)
			BodyPart messageBodyPartText = new MimeBodyPart();
			messageBodyPartText.setContent(plainText.toString(), Constants.MAIL_MIME_TYPE_PLAIN_UTF8);
			multipart.addBodyPart(messageBodyPartText);
			// Create new message part (html)
			BodyPart messageBodyPart = new MimeBodyPart();
			messageBodyPart.setContent(htmlText.toString(), Constants.MAIL_MIME_TYPE_HTML_UTF8);
			// Create a related multi-part to combine the parts
			multipart.addBodyPart(messageBodyPart);
			
			// Associate multi-part with message
			message.setContent(multipart);
		}
		// Send message

		Transport.send(message);

		
	}

	class MailAuthenticator extends Authenticator {
		@Override
		public PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(user, password);
		}
	}
	
	/**
	 * Notificación 
	 * @param abstractDTO
	 * @param event
	 * @throws NoticeException
	 */
	public void doNoticeQuartzEmail(NoticeConfiguration noticeConf, NoticeMessage noticeMessage,  String noticeName, DateTime dt ) {
		try {
			if(!noticeMessage.getReceivers().isEmpty()) {
				
				JobDataMap jobDataMap = new JobDataMap();

				jobDataMap.put(Constants.NOTICE_MESSAGE, noticeMessage);
				jobDataMap.put(Constants.NOTICE_CONFIGURATION, noticeConf);
				// Planificación de tarea Quartz
				quartzInvoker.scheduleJobIntervalDesdeFecha(
					jobDataMap, EmailNoticeServiceJob.class, noticeName, Constants.NOTICE, Constants.NOTICE_RETRY, Constants.NOTICE_MS_INTERVAL, dt);

			}	
		
		} catch (Throwable e) {
			log.error("Error al enviar notificacion email diferida.", e);
		}
	}


}

package es.msssi.sgm.registropresencial.mail;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.apache.log4j.Logger;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Service;
import org.springframework.ui.velocity.VelocityEngineUtils;

import core.error.CoreException;
import core.error.ErrorCode;

@Service
public class EmailServiceManager {

	private static final String MAIL_SMTP_AUTH = "mail.smtp.auth";

	private static final String MAIL_SMTP_PASSWORD = "mail.smtp.password";

	private static final String MAIL_SMTP_USER = "mail.smtp.user";

	private JavaMailSender mailSender;

	private SimpleMailMessage preConfiguredMailMessage;

	private VelocityEngine velocityEngine;

	//private String mailSubject;

	private String mailFrom;
	
	protected static Logger logger = Logger.getLogger(EmailServiceManager.class);

	/**
	 * Extrae un objeto session a partir de nombre jndi del mismo.
	 *
	 * @param jndiName
	 *            nombre jndi del objeto session.
	 * @return objeto session.
	 */
	public static Session getSessionFromJNDI(String jndiName) {
		Session result = null;
		Context ctx;
		try {
			ctx = new InitialContext();
			result = (Session) ctx.lookup(jndiName);
		}
		catch (NamingException ne) {
			throw new CoreException(ErrorCode.ARGUMENT_ERROR);
		}
		return result;
	}

	/**
	 * Metodo que recibe un objeto session y sigue la siguiente logica
	 * (recomendado por Oracle para weblogic):
	 * 
	 * 1. Extrae sus JavaMail properties
	 * 
	 * 2. Crea un nuevo objeto session con autenticacion si lo precisa
	 * 
	 * 3. Inserta el nuevo session en el mailSender.
	 * 
	 * @param session
	 *            Objeto session recibido.
	 */
	public void overwriteSession(Session session) {
		Session sessionOverwritten = null;
		Properties props = session.getProperties();
		if (props.getProperty(MAIL_SMTP_AUTH) != null
				&& "true".equals(props.getProperty(MAIL_SMTP_AUTH))) {
			final String smtpPass = props.getProperty(MAIL_SMTP_PASSWORD);
			final String smtpUser = props.getProperty(MAIL_SMTP_USER);
			sessionOverwritten = session.getInstance(props, new javax.mail.Authenticator() {
				protected PasswordAuthentication getPasswordAuthentication() {
					return new PasswordAuthentication(smtpUser, smtpPass);
				}
			});
		}
		else {
			sessionOverwritten = session.getInstance(props);
		}
		((JavaMailSenderImpl) mailSender).setSession(sessionOverwritten);
	}

	/**
	 * Este metodo enviara el mensaje compuesto que se recibe como parametro.
	 * Reemplaza los valores %s del campo text del mensaje por los parametros
	 * que recibe e params.
	 * 
	 * @param message
	 *            mensaje a enviar.
	 * @param params
	 *            parametros para incluir en el cuerpo del mensaje.
	 */
	public void sendMail(SimpleMailMessage message, String... params) throws MailException{
		String textModified = String.format(message.getText(), params);
		message.setText(textModified);
		mailSender.send(message);
	}

	/**
	 * Envia un correo electronico haciendo uso de la tecnologia velocity y la
	 * plantilla situada en el directorio ctemplatePath.
	 * 
	 * Sobrescribe el subject con el valor de las propiedad mailSubject.
	 * 
	 * Carga con la plantilla con los valores del hashmap
	 * 
	 * @param model
	 *            parametros que iran en la plantilla.
	 * @param userEmail
	 *            email to.
	 * @param templatePath
	 *            directorio con la plantilla del correo.
	 * 
	 * @throws MailException
	 */
	public void sendVelocityMail(final String userEmail, final String mailSubject, final String templatePath, final Map model) throws MailException {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {

			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage);
				message.setSubject(mailSubject);
				message.setTo(userEmail);
				String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templatePath,model);
				message.setText(text, true);
			}
		};
		this.mailSender.send(preparator);

	}

	/**
	 * Envia un correo electronico haciendo uso de la tecnologia velocity y la
	 * plantilla situada en el directorio ctemplatePath.
	 * También adjunta ficheros al correo
	 * 
	 * Sobrescribe el subject con el valor de las propiedad mailSubject.
	 * 
	 * Carga con la plantilla con los valores del hashmap
	 * 
	 * @param model
	 *            parametros que iran en la plantilla.
	 * @param userEmail
	 *            email to.
	 * @param templatePath
	 *            directorio con la plantilla del correo.
	 * 
	 * @throws MailException
	 */
	public void sendVelocityMail(final String userEmail, final String mailSubject, final String templatePath, final Map<String,String> model,final ArrayList<File> arrayFile, final HashMap<String,String> hashMapNombres) throws MailException {
		MimeMessagePreparator preparator = new MimeMessagePreparator() {

			public void prepare(MimeMessage mimeMessage) throws Exception {
				MimeMessageHelper message = new MimeMessageHelper(mimeMessage,true);
				message.setSubject(mailSubject);
				message.setTo(userEmail);
				String text = VelocityEngineUtils.mergeTemplateIntoString(velocityEngine, templatePath,model);

				logger.error("texto mail: " + text);
				
				message.setText(text, true);

				if(arrayFile != null){
					Iterator<File> itt = arrayFile.iterator();
					while (itt.hasNext()) {
						File file = itt.next();
						logger.error("Se añade el fichero: " + hashMapNombres.get(file.getName()));
						message.addAttachment(hashMapNombres.get(file.getName()), file);
					}
				}
			}
		};
		this.mailSender.send(preparator);

	}

	/**
	 * Este metodo enviara el mensaje predefinido en la configuracion. Reemplaza
	 * los valores %s del campo text del mensaje por los parametros que recibe e
	 * params.WSSecSOAPHandler
	 * 
	 * @param params
	 *            parametros para incluir en el cuerpo del mensaje.
	 */
	public void sendPreConfiguredMail(String... params) throws MailException{
		sendMail(preConfiguredMailMessage, params);
	}

	/**
	 * Getter mailSender.
	 * 
	 * @return mailSender.
	 */
	public JavaMailSender getMailSender() {
		return mailSender;
	}

	/**
	 * Setter mailSender
	 * 
	 * @param mailSender
	 *            JavaMailSender.
	 */
	public void setMailSender(JavaMailSender mailSender) {
		this.mailSender = mailSender;
	}

	/**
	 * Getter preConfiguredMailMessage.
	 * 
	 * @return preConfiguredMailMessage.
	 */
	public SimpleMailMessage getPreConfiguredMailMessage() {
		return preConfiguredMailMessage;
	}

	/**
	 * Setter preConfiguredMailMessage.
	 * 
	 * @param preConfiguredMailMessage
	 *            SimpleMailMessage.
	 */
	public void setPreConfiguredMailMessage(SimpleMailMessage preConfiguredMailMessage) {
		this.preConfiguredMailMessage = preConfiguredMailMessage;
	}

	/**
	 * Getter velocityEngine.
	 * 
	 * @return velocityEngine.
	 */
	public VelocityEngine getVelocityEngine() {
		return velocityEngine;
	}

	/**
	 * Setter velocityEngine.
	 * 
	 * @param velocityEngine
	 *            VelocityEngine.
	 */
	public void setVelocityEngine(VelocityEngine velocityEngine) {
		this.velocityEngine = velocityEngine;
	}

	//    /**
	//     * Getter mailSubject.
	//     * 
	//     * @return mailSubject.
	//     */
	//    public String getMailSubject() {
	//	return mailSubject;
	//    }
	//
	//    /**
	//     * Setter mailSubject
	//     * 
	//     * @param mailSubject
	//     *            String.
	//     */
	//    public void setMailSubject(String mailSubject) {
	//	this.mailSubject = mailSubject;
	//    }

	/**
	 * Getter mailFrom.
	 * 
	 * @return mailFrom.
	 */
	public String getMailFrom() {
		return mailFrom;
	}

	/**
	 * Setter mailFrom.
	 * 
	 * @param mailFrom
	 *            String.
	 */
	public void setMailFrom(String mailFrom) {
		this.mailFrom = mailFrom;
	}
}

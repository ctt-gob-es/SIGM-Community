package es.msssi.sgm.registropresencial.mail;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.mail.Session;

import org.apache.log4j.Logger;
import org.springframework.context.ApplicationContext;
import org.springframework.mail.MailException;
import core.spring.mail.EmailServiceManager;
import es.msssi.sgm.registropresencial.config.RegistroPresencialMSSSIWebSpringApplicationContext;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;


/**
 * Clase que gestiona el envio de correos del registro.
 * 
 * @author cmorenog
 */
public class RegistroPresencialMail {
    private static final Logger LOG = Logger.getLogger(RegistroPresencialMail.class);
    private static ApplicationContext appContext;


    /**
     * Inicio de tag li.
     */
    private static final String LI_BEGIN = "<li>";

    /**
     * Fin de tag li.
     */
    private static final String LI_END = "</li>";

    /**
     * Parametro con el nombre de usuario.
     */
    private static final String USER_NAME_PARAM = "userName";

    /**
     * Manager de mail.
     */
    private static final String EMAIL_SERVICE_MANAGER = "emailServiceManager";
    
    /**
     * Parametro con la lista de registros.
     */
    private static final String REGISTER_LIST_PARAM = "registerList";

    
    /**
     * Parametro con el motivo de la distribucion.
     */
    private static final String REASON_PARAM = "reason";
    /**
     * Parametro con el tipo de los registros de la distribucion.
     */
    private static final String TYPEREGISTER_PARAM = "typeRegister";
    
    /**
     * Template para el mail de notificacion de peticion pendiente.
     */
    private static final String PENDING_DISTRIBUTION_TEMPLATE_PATH =
	    "core/spring/mail/mailPendingTemplate.vm";
    
    /**
     * Template para el mail de notificacion de peticion rechazada.
     */
    private static final String REJECTED_DISTRIBUTION_TEMPLATE_PATH =
	    "core/spring/mail/mailRejectedTemplate.vm";
    
    /**
     * Template para el mail de notificacion de peticion rechazada.
     */
    private static final String REJECTED_INTERCAMBIOREG_TEMPLATE_PATH =
	    "core/spring/mail/mailIntercambioRegTemplate.vm";
    

    /**
     * JNDI del servicio de mail.
     */
    private static final String MAIL_SESSION_JNDI_NAME = "java:comp/env/mail/mailsession";


    static {
	appContext =
		RegistroPresencialMSSSIWebSpringApplicationContext.getInstance()
			.getApplicationContext();
    }
    
    /**
     * Envio de correo electronico desde el registro para indicar que hay una
     * distribucion pendiente.
     * 
     * @param name
     *            Nombre del usuario al que va dirigido el correo.
     * @param email
     *            Correo electronico del usuario.
     * @param subject
     *            Asunto del correo.
     * @param distRegister
     * 		lista de registros distribuidos.
     * @param typeRegister
     * 		tipo de registro.
     * @param reason
     * 		motivo de la distribucion.
     * @throws RPGenericException
     */
    public void sendMailPendingDistribution(String name, String email, String subject,
	    List<String> distRegister, String typeRegister, String reason) {
	EmailServiceManager emailServiceManager =
		(EmailServiceManager) appContext.getBean(EMAIL_SERVICE_MANAGER);
	try {
	    Session mailSession = EmailServiceManager.getSessionFromJNDI(MAIL_SESSION_JNDI_NAME);
	    emailServiceManager.overwriteSession(mailSession);
	    Map<String, String> model = new HashMap<String, String>();
	    model.put(USER_NAME_PARAM, name);
	    model.put(REASON_PARAM, reason);
	    model.put(TYPEREGISTER_PARAM, typeRegister);
	    // registros distribuidos
	    StringBuffer sb = new  StringBuffer(); 
	    for (String register : distRegister) {
		sb.append(LI_BEGIN);  
	     	sb.append(register); 
	     	sb.append(LI_END); 
	     } 
	    model.put(REGISTER_LIST_PARAM, sb.toString()); 
	    emailServiceManager.sendVelocityMail(email, subject,
		    PENDING_DISTRIBUTION_TEMPLATE_PATH, model);
	}
	catch (MailException me) {
	    LOG.error(ErrorConstants.ERROR_SENDING_EMAIL_MESSAGE + ":"+email, me);
	}
    }
    
    
    /**
     * Envio de correo electronico desde el registro para indicar que hay un
     * intercambio registral pendiente.
     * 
     * @param name
     *            Nombre del usuario al que va dirigido el correo.
     * @param email
     *            Correo electronico del usuario.
     * @param subject
     *            Asunto del correo.
     * @param register
     * 		registro con intercambio registral.
     * @param typeRegister
     * 		tipo de registro.
     * @throws RPGenericException
     */
    public void sendMailIntercambioRegistral(String name, String email, String subject,
    		List<String> distRegister, String typeRegister) {
	EmailServiceManager emailServiceManager =
		(EmailServiceManager) appContext.getBean(EMAIL_SERVICE_MANAGER);
	try {
	    Session mailSession = es.msssi.sgm.registropresencial.mail.EmailServiceManager.getSessionFromJNDI(MAIL_SESSION_JNDI_NAME);
	    emailServiceManager.overwriteSession(mailSession);
	    Map<String, String> model = new HashMap<String, String>();
	    model.put(USER_NAME_PARAM, name);
	    //model.put(REASON_PARAM, reason);
	    model.put(TYPEREGISTER_PARAM, typeRegister);
	    // registros de intercambio registral
	    StringBuffer sb = new  StringBuffer(); 
	    for (String register : distRegister) {
		sb.append(LI_BEGIN);  
	     	sb.append(register); 
	     	sb.append(LI_END); 
	     } 
	     
	    model.put(REGISTER_LIST_PARAM, sb.toString()); 
	    emailServiceManager.sendVelocityMail(email, subject,
	    		REJECTED_INTERCAMBIOREG_TEMPLATE_PATH, model);
	}
	catch (MailException me) {
	    LOG.error(ErrorConstants.ERROR_SENDING_EMAIL_MESSAGE_IR_RECH + ":"+email, me);
	}
    }
    
    /**
     * Envio de correo electronico desde el registro para indicar que hay una
     * distribucion rechazada.
     * 
     * @param name
     *            Nombre del usuario al que va dirigido el correo.
     * @param email
     *            Correo electronico del usuario.
     * @param subject
     *            Asunto del correo.
     * @param distRegister
     * 		lista de registros distribuidos.
     * @param typeRegister
     * 		tipo de registro.
     * @param reason
     * 		motivo de la distribucion rechazada.
     * @throws RPGenericException
     */
    public void sendMailRejectedDistribution(String name, String email, String subject,
	    List<String> distRegister, String typeRegister, String reason) {
	EmailServiceManager emailServiceManager =
		(EmailServiceManager) appContext.getBean(EMAIL_SERVICE_MANAGER);
	try {
	    Session mailSession = EmailServiceManager.getSessionFromJNDI(MAIL_SESSION_JNDI_NAME);
	    emailServiceManager.overwriteSession(mailSession);
	    Map<String, String> model = new HashMap<String, String>();
	    model.put(USER_NAME_PARAM, name);
	    model.put(REASON_PARAM, reason);
	    model.put(TYPEREGISTER_PARAM, typeRegister);
	    // registros distribuidos
	    StringBuffer sb = new  StringBuffer(); 
	    for (String register : distRegister) {
		sb.append(LI_BEGIN);  
	     	sb.append(register); 
	     	sb.append(LI_END); 
	     } 
	    model.put(REGISTER_LIST_PARAM, sb.toString()); 
	    emailServiceManager.sendVelocityMail(email, subject,
	    		REJECTED_DISTRIBUTION_TEMPLATE_PATH, model);
	}
	catch (MailException me) {
	    LOG.error(ErrorConstants.ERROR_SENDING_EMAIL_MESSAGE_DIST_RECH + ":"+email, me);
	}
    }

}
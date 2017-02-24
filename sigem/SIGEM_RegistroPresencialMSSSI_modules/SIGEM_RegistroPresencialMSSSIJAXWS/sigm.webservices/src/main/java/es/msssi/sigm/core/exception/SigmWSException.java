/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sigm.core.exception;

import java.text.MessageFormat;

import org.apache.log4j.Logger;

 
public class SigmWSException extends Exception {

    private static final long serialVersionUID = 3433427539337567545L;
    private Logger log = Logger.getLogger(SigmWSException.class.getName());
    protected String msgID;
    
    public SigmWSException() {
	super("Missing message string");
	this.msgID = null;
    }

    public SigmWSException(String msgID) {
	super(I18nSigmWS.getExceptionMessage(msgID));
	log.error(I18nSigmWS.getExceptionMessage(msgID));
	this.msgID = msgID;
    }

    public SigmWSException(String msgID, Object[] exArgs) {
	super(MessageFormat.format(I18nSigmWS.getExceptionMessage(msgID), exArgs));
	log.error(MessageFormat.format(I18nSigmWS.getExceptionMessage(msgID), exArgs));
	this.msgID = msgID;
    }

    public SigmWSException(Exception originalException) {
	super(originalException.getMessage(), originalException);
	log.error(originalException.getMessage(), originalException);
    }

    public SigmWSException(String msgID, Exception originalException) {
	super(I18nSigmWS.getExceptionMessage(msgID, originalException), originalException);
	log.error(I18nSigmWS.getExceptionMessage(msgID, originalException),
		originalException);
	this.msgID = msgID;
    }

    public SigmWSException(String msgID, Object[] exArgs, Exception originalException) {
	super(MessageFormat.format(I18nSigmWS.getExceptionMessage(msgID), exArgs), originalException);
	log.error(MessageFormat.format(I18nSigmWS.getExceptionMessage(msgID), exArgs),
		originalException);
	this.msgID = msgID;
    }

    public String getMsgID() {
	if (this.msgID == null) {
	    return "Missing message ID";
	}
	return this.msgID;
    }
//
    public String toString() {
	String s = getClass().getName();
	String message = super.getLocalizedMessage();
	if (message != null)
	    message = s + ": " + message;
	else {
	    message = s;
	}
	if (super.getCause() != null) {
	    message = message + "\nOriginal Exception was " + super.getCause().toString();
	}
	return message;
    }

    public void printStackTrace() {
	synchronized (System.err) {
	    super.printStackTrace(System.err);
	}
    }
//
//    public Exception getOriginalException() {
//	if ((getCause() instanceof Exception)) {
//	    return (Exception) getCause();
//	}
//	return null;
//    }

	public static String getErrorCode(String msgID) {
		String exceptionMessage = I18nSigmWS.getExceptionMessage(msgID+".errorCode");
		if(exceptionMessage.contains("No message with"))
			return I18nSigmWS.getExceptionMessage("err.general"+".errorCode");
		else
			return exceptionMessage; 
	}

	public static String getErrorDescription(String message) {
		if(message.contains("No message with"))
			return I18nSigmWS.getExceptionMessage("err.general"+".errorDes");
		else
			return message;
	}
}

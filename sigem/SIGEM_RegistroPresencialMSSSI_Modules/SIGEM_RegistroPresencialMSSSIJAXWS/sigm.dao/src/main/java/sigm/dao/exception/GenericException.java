/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package sigm.dao.exception;

import java.text.MessageFormat;

import core.log.ApplicationLogger;

/**
 * Generic exception.
 *
 * @author jviota
 * @version version, 07/01/2015 12:28
 */
public class GenericException extends Exception {

    private static final long serialVersionUID = 3433427539337567545L;
    protected String msgID;

    public GenericException() {
	super("Missing message string");
	this.msgID = null;
    }

    public GenericException(String msgID) {
	super(I18n.getExceptionMessage(msgID));
	ApplicationLogger.error(I18n.getExceptionMessage(msgID));
	this.msgID = msgID;
    }

    public GenericException(String msgID, Object[] exArgs) {
	super(MessageFormat.format(I18n.getExceptionMessage(msgID), exArgs));
	ApplicationLogger.error(MessageFormat.format(I18n.getExceptionMessage(msgID), exArgs));
	this.msgID = msgID;
    }

    public GenericException(Exception originalException) {
	super(originalException.getMessage(), originalException);
	ApplicationLogger.error(originalException.getMessage(), originalException);
    }

    public GenericException(String msgID, Exception originalException) {
	super(I18n.getExceptionMessage(msgID, originalException), originalException);
	ApplicationLogger.error(I18n.getExceptionMessage(msgID, originalException),
		originalException);
	this.msgID = msgID;
    }

    public GenericException(String msgID, Object[] exArgs, Exception originalException) {
	super(MessageFormat.format(I18n.getExceptionMessage(msgID), exArgs), originalException);
	ApplicationLogger.error(MessageFormat.format(I18n.getExceptionMessage(msgID), exArgs),
		originalException);
	this.msgID = msgID;
    }

    public String getMsgID() {
	if (this.msgID == null) {
	    return "Missing message ID";
	}
	return this.msgID;
    }

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

    public Exception getOriginalException() {
	if ((getCause() instanceof Exception)) {
	    return (Exception) getCause();
	}
	return null;
    }
}

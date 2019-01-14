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

import sigm.dao.language.Sigmdao;

/**
 * Short description for Java Class.
 *
 * @author jviota
 * @version version, 07/01/2015 12:46
 */
public class I18n {

	public static String translate(String message, Object[] args) {
		return getExceptionMessage(message, args);
	}

	public static String translate(String message) {
		return getExceptionMessage(message);
	}

	public static String getExceptionMessage(String msgID) {
		try {
			return Sigmdao.getInstance().getProperty(msgID);
		} catch (Throwable t) {
			return "No message with ID \"" + msgID
					+ "\" found in resource bundle \""
					+ Sigmdao.class.getName() + "\"";
		}
	}

	public static String getExceptionMessage(String msgID,
			Exception originalException) {
		try {
			Object[] exArgs = { originalException.getMessage() };
			return MessageFormat.format(
					Sigmdao.getInstance().getProperty(msgID), exArgs);
		} catch (Throwable t) {
			return "No message with ID \""
					+ msgID
					+ "\" found in resource bundle \"sigm.dao.exception.init();\" . Original Exception was a "
					+ originalException.getClass().getName() + " and message "
					+ originalException.getMessage();
		}
	}

	public static String getExceptionMessage(String msgID, Object[] exArgs) {
		try {
			return MessageFormat.format(
					Sigmdao.getInstance().getProperty(msgID), exArgs);
		} catch (Throwable t) {
			return "No message with ID \"" + msgID
					+ "\" found in resource bundle \""
					+ Sigmdao.class.getName() + "\"";
		}

	}

}

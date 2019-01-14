/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.core.errors;

import java.io.Serializable;

import core.error.ErrorCode;
import core.log.ApplicationLogger;

/**
 * Clase base para el manejo del resto de excepciones.
 * 
 * @author jortizs
 */
public class BaseException extends Exception implements Serializable {
    private static final long serialVersionUID = 1L;

    protected ErrorCode code;

    /**
     * Crea un nuevo RPBaseException.
     * 
     * @param code
     *            Código de error.
     */
    public BaseException(ErrorCode code) {
	super();
	this.code = code;
	notifyException();
    }

    /**
     * Crea un nuevo RPBaseException.
     * 
     * @param code
     *            Código de error.
     * @param msg
     *            Mensaje de error.
     */
    public BaseException(ErrorCode code, String msg) {
	super(msg);
	this.code = code;
	notifyException();
    }

    /**
     * Crea un nuevo RPBaseException.
     * 
     * @param code
     *            Código de error.
     * @param msg
     *            Mensaje de error.
     * @param cause
     *            Excepcion que ha causado el error.
     */
    public BaseException(ErrorCode code, String msg, Throwable cause) {
	super(msg, cause);
	this.code = code;
	notifyException();
    }

    /**
     * Crea un nuevo RPBaseException.
     * 
     * @param code
     *            Código de error.
     * @param cause
     *            Excepcion que ha causado el error.
     */
    public BaseException(ErrorCode code, Throwable cause) {
	super(cause.getMessage(), cause);
	this.code = code;
	notifyException();
    }

    /**
     * Retorna el codigo de error de la excepcion.
     * 
     * @return Código de error.
     */
    public ErrorCode getCode() {
	return code;
    }

    /**
     * Retorna el mensaje de la excepcion.
     * 
     * @return result El mensaje de excepción.
     */
    public String getMessage() {
	String result = code.toString();
	String message = super.getMessage();
	if (message != null ||
	    !"".equals(message)) {
	    result += ": " +
		message;
	}
	return result;
    }

    /**
     * Retorna el mensaje de la excepcion.
     * 
     * @return El mensaje de excepción.
     */
    public String getShortMessage() {
	return super.getMessage();
    }

    /**
     * Notifica sobre la excepción ocurrida.
     */
    private void notifyException() {
	notifyException(this);
    }

    /**
     * Imprime traza de la excepcion.
     * 
     * @param throwable
     *            La excepción a notificar.
     */
    public static void notifyException(
	Throwable throwable) {
	ApplicationLogger.error("Exception notified:");
	ApplicationLogger.error(throwable);
    }
}
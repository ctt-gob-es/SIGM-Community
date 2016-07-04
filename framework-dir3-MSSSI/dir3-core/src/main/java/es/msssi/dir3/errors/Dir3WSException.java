/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.errors;

/**
 * Clase base para el manejo del resto de excepciones.
 * 
 * @author cmorenog
 */
public class Dir3WSException extends Exception {
    private static final long serialVersionUID = 1L;
    private static LogHandlerLog4j h = new LogHandlerLog4j();
    protected String code;

    /**
     * Crea un nuevo RPBaseException.
     * 
     * @param code
     *            Código de error.
     */
    public Dir3WSException(String code) {
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
    public Dir3WSException(String code, String msg) {
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
    public Dir3WSException(String code, String msg, Throwable cause) {
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
    public Dir3WSException(String code, Throwable cause) {
	super(cause.getMessage(), cause);
	this.code = code;
	notifyException();
    }

    /**
     * Retorna el codigo de error de la excepcion.
     * 
     * @return Código de error.
     */
    public String getCode() {
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
	h.doerror("Exception notified:");
	h.doexception(throwable);
    }
}
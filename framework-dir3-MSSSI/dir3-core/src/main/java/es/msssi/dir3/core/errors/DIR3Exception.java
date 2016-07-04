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

/**
 * Clase para controlar excepciones que puedan darse en las operaciones del
 * dir3.
 * 
 * @author jortizs
 */
public class DIR3Exception extends BaseException {
    private static final long serialVersionUID = 1L;

    /**
     * Constructor con código de error, mensaje y excepción capturada.
     * 
     * @param code
     *            Código de error
     * @param message
     *            Mensaje de error
     * @param throwable
     *            Excepción capturada
     */
    public DIR3Exception(DIR3ErrorCode code, String message, Throwable throwable) {
	super(code, message, throwable);
    }

    /**
     * Constructor con código de error y mensaje.
     * 
     * @param code
     *            Código de error
     * @param message
     *            Mensaje
     */
    public DIR3Exception(DIR3ErrorCode code, String message) {
	super(code, message);
    }

    /**
     * Constructor con código de error y excepción capturada.
     * 
     * @param code
     *            Código de error
     * @param throwable
     *            Excepción capturada
     */
    public DIR3Exception(DIR3ErrorCode code, Throwable throwable) {
	super(code, throwable);
    }
}
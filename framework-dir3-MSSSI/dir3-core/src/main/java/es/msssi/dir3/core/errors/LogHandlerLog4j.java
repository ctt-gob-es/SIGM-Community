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

import org.apache.log4j.Logger;

/**
 * Clase manejadora de los logs.
 * 
 * @author cmorenog
 */
public class LogHandlerLog4j {

    private static final String SEPARATOR = ": ";

    private static final String EXCEPTION_TRACE = "Exception Trace:";

    private static final Logger LOG = Logger.getLogger(LogHandlerLog4j.class);

    /**
     * Registra un log de nivel info.
     * 
     * @param s
     *            mensaje.
     */
    public void doinfo(
	String s) {
	LOG.info(s);
    }

    /**
     * Registra un log de nivel warn.
     * 
     * @param s
     *            mensaje.
     */
    public void dowarn(
	String s) {
	LOG.warn(s);
    }

    /**
     * Registra un log de nivel error.
     * 
     * @param s
     *            mensaje.
     */
    public void doerror(
	String s) {
	LOG.error(s);
    }

    /**
     * Registra una excepción en el log.
     * 
     * @param t
     *            la excepción a registrar.
     */
    public void doexception(
	Throwable t) {
	LOG.error(t.getClass().getName() +
	    SEPARATOR + t.getMessage());
	LOG.error(
	    EXCEPTION_TRACE, t);
    }

    /**
     * Registra un log de nivel info en la clase indicada.
     * 
     * @param s
     *            mensaje.
     * @param clase
     *            clase donde se ha producido el mensaje de log.
     */
    public void doinfo(
	String s, Class<? extends Object> clase) {
	Logger logger = Logger.getLogger(clase);
	logger.info(s);
    }

    /**
     * Registra un log de nivel warn en la clase indicada.
     * 
     * @param s
     *            mensaje.
     * @param clase
     *            clase donde se ha producido el mensaje de log.
     */
    public void dowarn(
	String s, Class<? extends Object> clase) {
	Logger logger = Logger.getLogger(clase);
	logger.warn(s);
    }

    /**
     * Registra un log de nivel error en la clase indicada.
     * 
     * @param s
     *            mensaje.
     * @param clase
     *            clase donde se ha producido el mensaje de log.
     */
    public void doerror(
	String s, Class<? extends Object> clase) {
	Logger logger = Logger.getLogger(clase);
	logger.error(s);
    }

    /**
     * Registra una excepción en el log en la clase indicada.
     * 
     * @param t
     *            la excepción a registrar.
     * @param clase
     *            clase donde se ha producido la excepción.
     */
    public void doexception(
	Throwable t, Class<? extends Object> clase) {
	Logger logger = Logger.getLogger(clase);
	logger.error(t.getClass().getName() +
	    SEPARATOR + t.getMessage());
	logger.error(
	    EXCEPTION_TRACE, t);
    }
}

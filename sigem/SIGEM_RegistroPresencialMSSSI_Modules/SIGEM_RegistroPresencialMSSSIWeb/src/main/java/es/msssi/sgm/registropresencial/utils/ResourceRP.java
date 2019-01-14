/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto sean aprobadas por laComisión Europea- versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.utils;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.MissingResourceException;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import es.msssi.sgm.registropresencial.actions.ListBooksAction;

/**
 * Clase que recoge el archivo de propiedades de la aplicación.
 * 
 * @author cmorenog
 */
public class ResourceRP {
    /** Archivo de mensajes de configuración. */
    private static final String RB_NAME = "RPMSSSImessages";
    private static final Logger LOG = Logger.getLogger(ListBooksAction.class.getName());
    private static final int COUNTFILE = 3;
    private static Map<Locale, ResourceRP> resourceBundles = new HashMap<Locale, ResourceRP>(
	COUNTFILE);
    private ResourceBundle rb = null;

    /**
     * Constructor con opción de locale.
     * 
     * @param locale
     *            el locale del archivo.
     * */
    public ResourceRP(Locale locale) {
	rb = ResourceBundle.getBundle(
	    RB_NAME, locale);
    }

    /**
     * Método estático que instancia la clase si no lo está ya.
     * 
     * @param locale
     *            el locale del archivo.
     * @return la propia clase.
     * */
    public static synchronized ResourceRP getInstance(
	Locale locale) {
	ResourceRP rbUtil = null;
	// if(locale == null) {
	// locale = Locale.getDefault();
	// }
	if (resourceBundles.containsKey(locale)) {
	    rbUtil = (ResourceRP) resourceBundles.get(locale);
	}
	else {
	    rbUtil = new ResourceRP(
		locale);
	    resourceBundles.put(
		locale, rbUtil);
	}
	return rbUtil;
    }

    /**
     * Devuelve el valor de la propiedad solicitada del archivo de propiedades.
     * 
     * @param key
     *            la clave de la propiedad a recuperar.
     * @return valor de la propiedad.
     * @throws MissingResourceException
     *             Si no existe la propiedad.
     */
    public String getProperty(
	String key)
	throws MissingResourceException {
	String delimiter = "@@";
	return getProperty(
	    key, delimiter +
		key + delimiter);
    }

    /**
     * Devuelve el valor de la propiedad solicitada del archivo de propiedades.
     * Si ese valor no existe se devuelve el introducido por defecto.
     * 
     * @param key
     *            la clave de la propiedad a recuperar.
     * @param defaultValue
     *            valor por defecto si la propiedad no se encuentra.
     * @return valor de la propiedad.
     * @throws MissingResourceException
     *             Si no existe la propiedad.
     */
    public String getProperty(
	String key, String defaultValue)
	throws MissingResourceException {
	LOG.trace("Entrando en ResourceRP.getProperty()");
	String result = defaultValue;
	LOG.trace("Recogemos la propiedad: " +
	    key);
	if (key != null) {
	    result = rb.getString(key);
	}
	return result;
    }
}
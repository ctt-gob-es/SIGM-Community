/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.beans;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Contiene los parámetros de inicialización del web.xml.
 * 
 * @author cmorenog
 */
public class WebParameter implements Serializable {
	private static final long serialVersionUID = 1L;
	/** parametros de contexto del web.xml. */
	private static HashMap<String, String> initParameters = new HashMap<String, String>();
	/** parametro env-entry del web.xml. */
	private static HashMap<String, Object> entryParameters =
			new HashMap<String, Object>();
	
	/**
	 * Devuelve los parámetros de contexto de web.xml.
	 * 
	 * @return hashMap con los parámetros
	 */
	public HashMap<String, String> getInitParameters() {
		return initParameters;
	}
	
	/**
	 * Introduce los parámetros de inicialización del contexto.
	 * 
	 * @param initParameters
	 *            hashMap con los parámetros de inicialización.
	 */
	public static void setInitParameters(HashMap<String, String> initParameters) {
		WebParameter.initParameters = initParameters;
	}
	
	/**
	 * Devuelve los parámetros de env-entry de web.xml.
	 * 
	 * @return hashMap con los parámetros.
	 */
	public HashMap<String, Object> getEntryParameters() {
		return entryParameters;
	}
	
	/**
	 * Introduce los parámetros de env-entry del contexto.
	 * 
	 * @param entryParameters
	 *            hashMap con los parámetros env-entry.
	 */
	public static void setEntryParameters(HashMap<String, Object> entryParameters) {
		WebParameter.entryParameters = entryParameters;
	}
	
	/**
	 * Retorna un parámetro de contexto del web.xml.
	 * 
	 * @param key
	 *            String con el nombre del parámetro.
	 * 
	 * @return String con el valor.
	 */
	public static String getInitParameter(String key) {
		return initParameters.get(key);
	}
	
	/**
	 * Retorna un parámetro env-entry del web.xml.
	 * 
	 * @param key
	 *            String con el nombre del parámetro.
	 * @return String con el valor.
	 */
	public static Object getEntryParameter(String key) {
		return entryParameters.get(key);
	}
}
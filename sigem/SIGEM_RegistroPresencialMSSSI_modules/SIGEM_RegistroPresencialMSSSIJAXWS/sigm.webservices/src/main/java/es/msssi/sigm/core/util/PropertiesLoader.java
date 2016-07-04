/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sigm.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Clase que carga los archivos de propiedades.
 * 
 * @author jortizs
 */
public final class PropertiesLoader {
	private static Map<Object, Object> cache = new HashMap<Object, Object>();
	private static String propertiesDir = null;
	private static PropertiesLoader me = null;
	
	/**
	 * Constructor.
	 */
	private PropertiesLoader() {
		
	}
	
	/**
	 * Obtiene una instancia nueva de la clase.
	 * 
	 * @return me Nueva instancia de la clase.
	 */
	public static PropertiesLoader getInstance() {
		if (me == null) {
			me = new PropertiesLoader();
		}
		return me;
	}
	
	/**
	 * Comprueba la existencia del fichero de propiedades.
	 * 
	 * @param filename
	 *            Fichero a comprobar.
	 */
	private void checkState(String filename) {
		if (propertiesDir == null) {
			System.err.println("ERROR: PopertiesLoader - propertiesDir not set");
			throw new IllegalStateException("propertiesDir not set!");
		}
		
		if (filename == null) {
			System.err.println("ERROR: PopertiesLoader - null filename");
			throw new IllegalArgumentException("null filename");
		}
	}
	
	/**
	 * Obtiene las propiedades de un fichero.
	 * 
	 * @param filename
	 *            Fichero de propiedades.
	 * @return result Propiedades obtenidas.
	 */
	public Properties getProperties(String filename) {
		checkState(filename);
		Properties result = (Properties) cache.get(filename);
		if (result == null) {
			result = loadProperties(filename);
			cache.put(filename, result);
		}
		return result;
	}
	
	/**
	 * Lee el archivo de propiedades.
	 * 
	 * @param filename
	 *            Archivo a leer.
	 * @return result archivo obtenido.
	 */
	public Properties loadProperties(String filename) {
		checkState(filename);
		try {
			File f = new File(propertiesDir, filename);
			if (f.exists()) {
				FileInputStream fis = new FileInputStream(f);
				Properties result = new Properties();
				result.load(fis);
				fis.close();
				return result;
			}
		}
		catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return null;
	}
	
	/**
	 * Limpia los datos de un fichero de propiedades de caché.
	 * 
	 * @param filename
	 *            Fichero a limpiar.
	 */
	public void clear(String filename) {
		if (filename == null) {
			return;
		}
		if ("all".equals(filename)) {
			clear();
		}
		cache.remove(filename);
	}
	
	/**
	 * Limpia la caché de propiedades.
	 */
	public void clear() {
		cache.clear();
	}
	
	/**
	 * Obtiene las propiedades de caché.
	 * 
	 * @return cache Propiedades obtenidas.
	 */
	public Map<Object, Object> getProperties() {
		return cache;
	}
	
	/**
	 * Obtiene el directorio de los archivos de propiedades.
	 * 
	 * @return propertiesDir Directorio de los archivos de propiedades.
	 */
	public String getPropertiesDir() {
		return propertiesDir;
	}
	
	/**
	 * Establece el directorio donde se encuentran los archivos de propiedades.
	 * 
	 * @param propertiesDir
	 *            Directorio de los archivos de propiedades.
	 */
	public void setPropertiesDir(String propertiesDir) {
		File f = new File(propertiesDir);
		if (!f.exists() || !f.isDirectory()) {
			System.err.println("ERROR:" + propertiesDir
					+ " no existe o no es un directorio.");
			throw new IllegalArgumentException(propertiesDir
					+ " no existe o no es un directorio");
		}
		PropertiesLoader.propertiesDir = propertiesDir;
	}
}
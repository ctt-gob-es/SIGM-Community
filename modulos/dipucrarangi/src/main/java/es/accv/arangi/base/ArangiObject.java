/**
 * LICENCIA LGPL:
 * 
 * Esta librería es Software Libre; Usted puede redistribuirla y/o modificarla
 * bajo los términos de la GNU Lesser General Public License (LGPL) tal y como 
 * ha sido publicada por la Free Software Foundation; o bien la versión 2.1 de 
 * la Licencia, o (a su elección) cualquier versión posterior.
 * 
 * Esta librería se distribuye con la esperanza de que sea útil, pero SIN 
 * NINGUNA GARANTÍA; tampoco las implícitas garantías de MERCANTILIDAD o 
 * ADECUACIÓN A UN PROPÓSITO PARTICULAR. Consulte la GNU Lesser General Public 
 * License (LGPL) para más detalles
 * 
 * Usted debe recibir una copia de la GNU Lesser General Public License (LGPL) 
 * junto con esta librería; si no es así, escriba a la Free Software Foundation 
 * Inc. 51 Franklin Street, 5º Piso, Boston, MA 02110-1301, USA o consulte
 * <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011 Agencia de Tecnología y Certificación Electrónica
 */
package es.accv.arangi.base;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.security.Provider;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import es.accv.arangi.base.exception.NoSuchProviderException;
import es.accv.arangi.base.exception.signature.XMLDocumentException;
import es.accv.arangi.base.util.Util;

/**
 * Clase que inicializa el proveedor criptográfico de Arangi, actualmente Bouncy Castle.
 * Todas las clases de la API son hijas de esta clase, de forma que siempre se ejecutarán
 * con el proveedor criptográfico instalado.<br><br>
 * 
 * Otro de los valores que se inicializan es la carpeta temporal que Arangi usará en alguna
 * de sus operaciones. Esta carpeta se encuentra en el directorio del usuario, en el path
 * <i>.ACCV/ArangiTemp</i>. Por ejemplo, si el nombre del usuario es <i>miuser</i> y se 
 * encuentra ejecutando la aplicación en Windows, la carpeta temporal de Arangi será 
 * <i>C:\Documents and Settings\miuser\.ACCV\ArangiTemp</i>.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José M Gutiérrez</a>
 */
public class ArangiObject {

	private static Logger logger = Logger.getLogger(ArangiObject.class);	
	
	/**
	 * Nombre del proveedor criptográfico
	 */
	public static final String CRYPTOGRAPHIC_PROVIDER_NAME = "BC";
	
	/**
	 * Proveedor criptográfico
	 */
	public static final Provider CRYPTOGRAPHIC_PROVIDER = new BouncyCastleProvider ();
	
	/**
	 * Algoritmo de hashing por defecto
	 */
	public static final String DEFAULT_HASHING_ALGORITHM = "SHA1";

	/**
	 * Algoritmo de firma por defecto
	 */
	public static final String DEFAULT_SIGNING_ALGORITHM = "SHA1WithRSA";
	
	/**
	 * Número de intentos antes de dar una excepción (normalmente de PKCS#11) por ERROR
	 */
	public static final int NUM_RETRIES		= 3;
	
	/**
	 * Paquete donde se guardan las librerías que hay que instalar para 32 bits
	 */
	protected static final String LIBRARIES_PACKAGE_X86	= "es/accv/arangi/resource/libraries/";
	
	/**
	 * Paquete donde se guardan las librerías que hay que instalar para 64 bits
	 */
	protected static final String LIBRARIES_PACKAGE_X64	= "es/accv/arangi/resource/libraries/x64/";
	
	/**
	 * Nombre de la carpeta dentro del directorio temporal de Arangí donde se guardan las librerías
	 */
	protected static final String LIBRARIES_FOLDER_NAME	= "libraries";
	
	/**
	 * Carpeta temporal
	 */
	public static File arangiTemporalFolder;
	
	/**
	 * Inicializa el proveedor criptográfico
	 */
	static {
		//-- Cargar el proveedor criptográfico
		try {
			Util.setProvider(CRYPTOGRAPHIC_PROVIDER_NAME, CRYPTOGRAPHIC_PROVIDER);
		} catch (Throwable t) {
			//-- Excepción HORRIBLE: no se puede inicializar el proveedor criptográfico
			logger.error ("[ArangiObject.static]::No se ha podido inicializar el proveedor criptográfico", t);
			throw new NoSuchProviderException ("No se ha podido cargar el proveedor de BouncyCastle. Revise que dispone " +
					"de las librerías y que tiene permisos para añadir nuevos proveedores criptográficos", t);
		}
		
	}

	/**
	 * El constructor no hace nada
	 */
	public ArangiObject() {
		super();
	}
	
	/**
	 * Devuelve el directorio temporal de Arangi: &lt;user_home&gt;/.ACCV/ArangiTemp
	 * 
	 * @return Directorio temporal de Arangi
	 */
	public static File getArangiTemporalFolder () {
		//-- Obtener el fichero temporal
		if (arangiTemporalFolder == null) {

			// Cargar las propiedades
			String sUserHome = System.getProperty("user.home");
			if (System.getenv("USERPROFILE") != null) {
				sUserHome = System.getenv("USERPROFILE");
			}
			File userHome = new File (sUserHome);
			arangiTemporalFolder = new File (new File (userHome, ".ACCV"), "ArangiTemp");
			arangiTemporalFolder.mkdirs();
			logger.debug("[ArangiObject.getArangiTemporalFolder]::Creada la carpeta temporal de Arangi en: " + arangiTemporalFolder.getAbsolutePath());
		}

		return arangiTemporalFolder;
	}
	
	/**
	 * Lee el stream de lectura y lo guarda con el nombre del fichero de destino
	 * en la carpeta temporal de Arangi.
	 * 
	 * @param iStream Stream de lectura
	 * @param fileName Fichero destino
	 * @return Fichero donde se ha guardado la información
	 * @throws IOException No se puede leer o escribir
	 */
	public static File saveToArangiTemporalFolder (InputStream iStream, String fileName) throws IOException {
		logger.debug("[ArangiObject.saveToArangiTemporalFolder]::Entrada::" + Arrays.asList(new Object[] { iStream, fileName }));
		
		//-- Crear el fichero
		File file = new File (getArangiTemporalFolder(), fileName);
		
		//-- Guardar
		Util.saveFile(file, iStream);
		
		//-- Devolver el fichero
		return file;
	}
	
	/**
	 * Guarda un fichero temporal
	 * 
	 * @param iStream Stream de lectura
	 * @return Fichero temporal
	 * @throws IOException No es posible guardar
	 */
	public synchronized static File saveTemporalFile (InputStream iStream) throws IOException {
		logger.debug("[ArangiObject.saveTemporalFile]::Entrada::" + Arrays.asList(new Object[] { iStream }));
		
		//-- Buscar un nombre de fichero que no exista
		int i = 0;
		File file;
		do {
			file = new File (getArangiTemporalFolder(), "temporal_" + i);
			i++;
		} while (file.exists());
		
		//-- Guardar
		Util.saveFile(file, iStream);
		
		//-- Devolver el fichero
		return file;
	}
	
	/**
	 * Guarda (si todavía no lo están) la lista de librerías en la carpeta de Arangí. 
	 * Una vez en disco, se cargan en el System para luego poder trabajar con ellas.
	 * Del mismo modo, se guardan los recursos indicados en el parámetro.<br><br>
	 * 
	 * Tanto las librerías como los recursos se deben encontrar en el classpath. En
	 * la carpeta indicada en la constante LIBRARIES_PACKAGE de esta clase.
	 */
	protected Map<String,String> saveAndLoadLibrariesAndResources(String[] libraries, String[] resources) {
		logger.debug ("[Pkcs11Manufacturer.saveAndLoadLibraries]::Entrada");
		
		//-- Obtener la versión de las librerías arangi_libraries
		String arangiLibrariesVersion = getArangiLibrariesVersion();
		
		//-- Resultado
		Map<String,String> resultado = new HashMap<String, String>();
		
		if (libraries != null) {
			//-- Para cada librería comprobar si existe en el classpath. Si existe y aun no se encuentra en la
			//-- carpeta temporal de librerías -> guardarla. En cualquier caso, si existe en el classpath
			//-- cargarla como módulo para poder ser usada.
			for (int i = 0; i < libraries.length; i++) {
				InputStream is = new Util().getClass().getClassLoader().getResourceAsStream(getLibrariesPackage() + libraries[i]);
				logger.debug ("[Pkcs11Manufacturer.saveAndLoadLibraries]::Librería " + libraries[i] + " stream: " + is);
				if (is != null) {
					File library = new File (getLibrariesFolder(arangiLibrariesVersion), libraries[i]);
					if (!library.exists() || library.length() == 0) {
						//-- Guardar
						logger.debug ("[Pkcs11Manufacturer.saveAndLoadLibraries]::Librería " + libraries[i] + " no existe. Comenzando el guardado.");
						try {
							Util.saveFile(library, is);
						} catch (IOException e) {
							logger.info ("[Pkcs11Manufacturer.saveAndLoadLibraries]::Librería " + libraries[i] + " no ha podido ser guardada.", e);
							continue;
						}
						logger.debug ("[Pkcs11Manufacturer.saveAndLoadLibraries]::Librería " + libraries[i] + " guardada.");
						
					}
					
					//-- Cargar
					logger.debug ("[Pkcs11Manufacturer.saveAndLoadLibraries]::Librería " + libraries[i] + " cargando.");
					System.load(library.getAbsolutePath());
					logger.debug ("[Pkcs11Manufacturer.saveAndLoadLibraries]::Librería " + libraries[i] + " cargada.");
					
					//-- Todo ok -> añadir al resultado
					resultado.put(libraries[i], new File (getLibrariesFolder(arangiLibrariesVersion), libraries[i]).getAbsolutePath());
				}
			}
		}
		
		if (resources != null) {
			//-- Para cada recuro comprobar si existe en el classpath. Si existe y aun no se encuentra en la
			//-- carpeta temporal de librerías -> guardarlo. 
			for (int i = 0; i < resources.length; i++) {
				InputStream is = new Util().getClass().getClassLoader().getResourceAsStream(getLibrariesPackage() + resources[i]);
				logger.debug ("[Pkcs11Manufacturer.saveAndLoadLibraries]::Recurso " + resources[i] + " stream: " + is);
				if (is != null) {
					File resource = new File (getLibrariesFolder(arangiLibrariesVersion), resources[i]);
					if (!resource.exists() || resource.length() == 0) {
						//-- Guardar
						logger.debug ("[Pkcs11Manufacturer.saveAndLoadLibraries]::Recurso " + resources[i] + " no existe. Comenzando el guardado.");
						try {
							Util.saveFile(resource, is);
						} catch (IOException e) {
							logger.info ("[Pkcs11Manufacturer.saveAndLoadLibraries]::Recurso " + resources[i] + " no ha podido ser guardado.", e);
							continue;
						}
						logger.debug ("[Pkcs11Manufacturer.saveAndLoadLibraries]::Recurso " + resources[i] + " guardado.");
					}
				}
			}
		}
		
		return resultado;
	}
	
    /*
     * Obtiene el document builder para generar documentos DOM
     */
	protected static DocumentBuilder getDocumentBuilder() throws XMLDocumentException {

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
    	dbf.setNamespaceAware(true);

		DocumentBuilder db;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e1) {
			logger.info ("[XAdESBESDocument.getXAdESBESDocument]::No se puede inicializar el parseador XML", e1);
			throw new XMLDocumentException("No se puede inicializar el parseador XML", e1);
		}
		
		return db;
	}

	//-- Métodos privados

	/**
	 * Obtiene el paquete de las librerías adecuadas
	 * @return
	 */
	private String getLibrariesPackage() {
		if (System.getProperty("os.arch") != null && System.getProperty("os.arch").indexOf("64") > -1) {
			return LIBRARIES_PACKAGE_X64;
		} else {
			return LIBRARIES_PACKAGE_X86;
		}
	}

	/*
	 * Obtiene la carpeta donde se guardan las librerías dentro de la carpeta
	 * de Arangí.
	 */
	private File getLibrariesFolder(String arangiLibrariesVersion) {
		File librariesFolder = new File (getArangiTemporalFolder(), LIBRARIES_FOLDER_NAME);
		if (System.getProperty("os.arch") != null && System.getProperty("os.arch").indexOf("64") > -1) {
			librariesFolder = new File (librariesFolder, "x64");
		} else {
			librariesFolder = new File (librariesFolder, "x86");
		}
		if (arangiLibrariesVersion != null) {
			librariesFolder = new File (librariesFolder, arangiLibrariesVersion);
		}
		if (!librariesFolder.exists()) {
			librariesFolder.mkdirs();
		}
		
		return librariesFolder;
	}

	/**
	 * Busca la clase es.accv.arangi.libraries.Constants en el classpath. Si no
	 * la encuentra devuelve un null. Si la encuentra obtiene la  versión por 
	 * introspeccion.
	 * 
	 * @return Versión de la librería arangi_libraries
	 */
	private String getArangiLibrariesVersion() {
		
		try {
			Class constantsClass = this.getClass().getClassLoader().loadClass("es.accv.arangi.libraries.Constants");
			Field versionField = constantsClass.getField("ARANGI_LIBRARIES_VERSION");
			return (String) versionField.get(new String());
		} catch (ClassNotFoundException e) {
			logger.debug("[Pkcs11Manufacturer.getArangiLibrariesVersion]::No se encuentra la constante de arangi_libraries");
		} catch (SecurityException e) {
			logger.info("[Pkcs11Manufacturer.getArangiLibrariesVersion]::Un problema de seguridad impide obtener la constante de arangi_libraries", e);
		} catch (NoSuchFieldException e) {
			logger.info("[Pkcs11Manufacturer.getArangiLibrariesVersion]::No se encuentra el campo con la constante de arangi_libraries", e);
		} catch (IllegalArgumentException e) {
			logger.info("[Pkcs11Manufacturer.getArangiLibrariesVersion]::La constante de arangi_libraries no es un String", e);
		} catch (IllegalAccessException e) {
			logger.info("[Pkcs11Manufacturer.getArangiLibrariesVersion]::No es posible acceder al valor de la constante de arangi_libraries", e);
		}
		
		return null;
	}

	
}

// Copyright (C) 2012-13 MINHAP, Gobierno de España
// This program is licensed and may be used, modified and redistributed under the terms
// of the European Public License (EUPL), either version 1.1 or (at your
// option) any later version as soon as they are approved by the European Commission.
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
// or implied. See the License for the specific language governing permissions and
// more details.
// You should have received a copy of the EUPL1.1 license
// along with this program; if not, you may find it at
// http://joinup.ec.europa.eu/software/page/eupl/licence-eupl

/*
 * Este fichero forma parte de la plataforma de @firma.
 * La plataforma de @firma es de libre distribución cuyo código fuente puede ser consultado
 * y descargado desde http://forja-ctt.administracionelectronica.gob.es
 *
 * Copyright 2009-,2011 Gobierno de España
 * Este fichero se distribuye bajo las licencias EUPL versión 1.1  y GPL versión 3, o superiores, según las
 * condiciones que figuran en el fichero 'LICENSE.txt' que se acompaña.  Si se   distribuyera este
 * fichero individualmente, deben incluirse aquí las condiciones expresadas allí.
 */

/**
 * <b>File:</b><p>es.gob.afirma.transformers.TransformersProperties.java.</p>
 * <b>Description:</b><p>This Class provides properties used for to parser and to generate xml.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * <b>Date:</b><p>16/03/2011.</p>
 * @author Gobierno de España.
 * @version 1.0, 16/03/2011.
 */
package es.gob.afirma.transformers;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.Enumeration;
import java.util.Properties;

import org.apache.log4j.Logger;

import es.dipucr.afirma.AfirmaConfigFilePathResolverImpl;
import es.gob.afirma.afirma5ServiceInvoker.Afirma5ServiceInvokerConstants;
import es.gob.afirma.i18n.ILogConstantKeys;
import es.gob.afirma.i18n.Language;

/**
 * Clase encargada del acceso a las propiedades requeridas por las clases que necesitan parsear o formar los parametros XML de entrada y salida
 * requeridos por los servicios publicados por la plataforma @Firma 5.
 * 
 * Las propiedades se encuentran organizadas por servicio de la siguiente forma:
 * 	#Peticion
 * 	serviceName.version.request.transformerClass=es.gob.afirma.transformers.requestTransformers.CommonRequestTransformer
 * 	serviceName.version.request.template=certificateValidation_V10.xml
 *  serviceName.version.request.schemaLocationAddress=localhost
 *  #Respuesta
 *  serviceNam.version.response.transformerClass=es.gob.afirma.transformers.responseTransformers.CommonResponseTransformer
 *  serviceNam.version.response.rootElement=respuesta/ResultadoProcesamiento
 *  serviceName.version.response.schemaLocationAddress=localhost
 * 
 * donde, en versi&oacute;n son sustituidos los caracteres '.' por '_'.
 * @author sepaot
 *
 */
public final class TransformersProperties {

    /**
     * Constructor method for the class TransformersProperties.java.
     */
    private TransformersProperties() {
    }

    /**
     * Attribute that represents .
     */
    private static Logger logger = Logger.getLogger(TransformersProperties.class);

    /**
     * Attribute that represents .
     */
    private static long propsFileLastUpdate = -1;

    /**
     * Attribute that represents .
     */
    private static Properties properties = new Properties();
    
    private static AfirmaConfigFilePathResolverImpl afirmaConfigFilePathResolver = new AfirmaConfigFilePathResolverImpl();

    static {
	init();
    }

    /**
     * Obtiene las propiedades del API de transformaci&oacute;n de parametros de entrada y salida XML de @Firma 5.
     * @return conjunto de propiedades.
     */
    public static Properties getTransformersProperties() {
	init();

	return properties;
    }

    /**
     * Inicializa las propiedades del API de manipulaci&oacute;n de par&aacute;metros de peticiones y respuestas xml de @Firma.<br/>
     * Si ocurre alg&uacute;n error devuelve un conjunto de propiedades vac&iacute;o.
     *
     */
    private static synchronized void init() {
	File file;
	InputStream in;

	try {
	    file = getPropertiesResource();

	    if (propsFileLastUpdate != file.lastModified()) {
		logger.debug(Language.getResIntegra(ILogConstantKeys.TP_LOG001));
		properties = new Properties();
		in = new FileInputStream(file);
		properties.load(in);
		propsFileLastUpdate = file.lastModified();

		logger.debug(Language.getFormatResIntegra(ILogConstantKeys.TP_LOG002, new Object[ ] { properties }));
		logger.debug(Language.getFormatResIntegra(ILogConstantKeys.TP_LOG003, new Object[ ] { new Date(propsFileLastUpdate) }));
	    }
	} catch (URISyntaxException e) {
	    String errorMsg = Language.getFormatResIntegra(ILogConstantKeys.TP_LOG004, new Object[ ] { TransformersConstants.TRANSFORMERS_FILE_PROPERTIES });
	    logger.error(errorMsg, e);
	    properties = new Properties();
	} catch (IOException e) {
	    String errorMsg = Language.getFormatResIntegra(ILogConstantKeys.TP_LOG004, new Object[ ] { TransformersConstants.TRANSFORMERS_FILE_PROPERTIES });
	    logger.error(errorMsg, e);
	    properties = new Properties();
	}
    }

    /**
     * Obtiene el fichero de propiedades del API de manipulaci&oacute;n de par&aacute;metros de peticiones y respuestas xml de @Firma.<br/>
     * Este fichero se obtiene como recurso del sistema.
     * @return fichero de propiedades del API de manipulaci&oacute;n de par&aacute;metros de peticiones y respuestas xml de @Firma.
     * @throws URISyntaxException si la ruta no est&aacute; bien formada.
     */
    private static File getPropertiesResource() throws URISyntaxException {
	File res;

	String path = afirmaConfigFilePathResolver.getConfigFilePath(afirmaConfigFilePathResolver.ISPAC_BASE_CONFIG_SUBDIR_KEY_AFIRMA + File.separator + TransformersConstants.TRANSFORMERS_FILE_PROPERTIES);	
		
	if (path == null) {
	    throw new URISyntaxException("Error", Language.getFormatResIntegra(ILogConstantKeys.TP_LOG005, new Object[ ] { TransformersConstants.TRANSFORMERS_FILE_PROPERTIES }));
	}
	
	res = new File(path);

	return res;
    }

    /**
     * Obtiene las propiedades del API de transformaci&oacute;n de parametros de entrada XML de @Firma.
     * @param serviceName nombre del servicio @Firma del que se desea obtener las propiedades.
     * @param method nombre del método del servicio web.
     * @param version versi&oacute;n del servicio.
     * @return conjunto de propiedades.
     */
    public static Properties getMethodRequestTransformersProperties(String serviceName, String method, String version) {
	Properties res;

	logger.debug(Language.getFormatResIntegra(ILogConstantKeys.TP_LOG006, new Object[ ] { serviceName }));

	res = getMethodTransformersProperties(serviceName, method, version, TransformersConstants.REQUEST_CTE);

	return res;
    }

    /**
     * Obtiene las propiedades del API de transformaci&oacute;n de parametros de salida XML de @Firma.
     * @param serviceName nombre del servicio @Firma del que se desea obtener las propiedades para parsear una respuesta.
     * @param method nombre del método del servicio web.
     * @param version versi&oacute;n del servicio.
     * @return conjunto de propiedades.
     */
    public static Properties getMethodResponseTransformersProperties(String serviceName, String method, String version) {
	Properties res;

	logger.debug(Language.getFormatResIntegra(ILogConstantKeys.TP_LOG007, new Object[ ] { serviceName }));

	res = getMethodTransformersProperties(serviceName, method, version, TransformersConstants.RESPONSE_CTE);

	return res;
    }

    /**
     * Obtiene las propiedades del API de parseo de parametros de salida XML de @Firma.
     * @param serviceName nombre del servicio @Firma del que se desea obtener las propiedades para parsear una respuesta.
     * @param method nombre del método del servicio web.
     * @param version versi&oacute;n del servicio.
     * @return conjunto de propiedades.
     */
    public static Properties getMethodParseTransformersProperties(String serviceName, String method, String version) {
	Properties res;

	logger.debug(Language.getFormatResIntegra(ILogConstantKeys.TP_LOG008, new Object[ ] { serviceName }));

	res = getMethodTransformersProperties(serviceName, method, version, TransformersConstants.PARSER_CTE);

	return res;
    }

    /**
     * Obtiene las propiedades del API de transformaci&oacute;n de par&aacute;metros XML para un servicio publicado por @Firma.
     * @param serviceName nombre del servicio @Firma del que se desea obtener las propiedades.
     * @param method method name.
     * @param version versi&oacute;n del servicio.
     * @return conjunto de propiedades.
     */
    public static Properties getMethodTransformersProperties(String serviceName, String method, String version) {
	Properties res;

	logger.debug(Language.getFormatResIntegra(ILogConstantKeys.TP_LOG008, new Object[ ] { serviceName }));

	res = getMethodTransformersProperties(serviceName, method, version, null);

	return res;
    }

    /**
     * 
     * @param serviceName serviceName
     * @param method method name.
     * @param version version
     * @param type type
     * @return Properties
     */
    private static Properties getMethodTransformersProperties(String serviceName, String method, String version, String type) {
	Enumeration<?> enumeration;
	Properties res;
	String header, key;

	res = new Properties();
	header = serviceName + "." + method + "." + version + "." + (type == null ? "" : type);
	enumeration = getTransformersProperties().propertyNames();

	while (enumeration.hasMoreElements()) {
	    key = (String) enumeration.nextElement();

	    if (key.startsWith(header)) {
		res.put(key, properties.getProperty(key));
	    }
	}

	logger.debug(Language.getFormatResIntegra(ILogConstantKeys.TP_LOG002, new Object[ ] { res }));

	return res;
    }
}

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
 * <b>File:</b><p>es.gob.afirma.transformers.ParserParameterProperties.java.</p>
 * <b>Description:</b><p>This Class provides the names of the parsed parameters.
 * These names identify the path to the node that derives its value.</p>
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
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Date;
import java.util.Properties;

import org.apache.log4j.Logger;

import es.dipucr.afirma.AfirmaConfigFilePathResolverImpl;
import es.gob.afirma.i18n.ILogConstantKeys;
import es.gob.afirma.i18n.Language;

/**
 * <p>Class provides the names of the parsed parameters.
 * These names identify the path to the node that derives its value.<br>
 * Parameter names are obtained form a properties file with the syntax:<br>
 * <code>"parameter_name" = "node_path"</code></p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * @version 1.0, 16/03/2011.
 */
public final class ParserParameterProperties {

    /**
     * Attribute that represents the object that manages the log of the class.
     */
    private static final Logger LOGGER = Logger.getLogger(ParserParameterProperties.class);

    /**
     * Attribute that represents date of file modificated.
     */
    private static long propsFileLastUpdate = -1;

    /**
     * Attribute that represents properties loaded.
     */
    private static Properties properties = new Properties();
    
    private static AfirmaConfigFilePathResolverImpl afirmaConfigFilePathResolver = new AfirmaConfigFilePathResolverImpl();

    /**
     * Constructor method for the class ParserParameterProperties.java.
     */
    private ParserParameterProperties() {
    }

    static {
	init();
    }

    /**Retrieves all properties names used for to indentify parsed parameters.
     * @return properties set.
     */
    public static Properties getParserParametersProperties() {
	init();

	return properties;
    }

    /**
     * Initializes the process of load of properties.
     */
    private static synchronized void init() {
	File file;
	try {
	    file = getPropertiesResource();

	    if (propsFileLastUpdate != file.lastModified()) {
		LOGGER.debug(Language.getResIntegra(ILogConstantKeys.PPP_LOG001));
		properties = new Properties();
		properties.load(new FileInputStream(file));
		propsFileLastUpdate = file.lastModified();

		LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.PPP_LOG002, new Object[ ] { properties }));
		LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.PPP_LOG003, new Object[ ] { new Date(propsFileLastUpdate) }));
	    }
	} catch (URISyntaxException e) {
	    LOGGER.error(Language.getFormatResIntegra(ILogConstantKeys.PPP_LOG004, new Object[ ] { TransformersConstants.PARSED_PARAMETERS_FILE }), e);
	    properties = new Properties();
	} catch (IOException e) {
	    LOGGER.error(Language.getFormatResIntegra(ILogConstantKeys.PPP_LOG004, new Object[ ] { TransformersConstants.PARSED_PARAMETERS_FILE }), e);
	    properties = new Properties();
	}
    }

    /**
     * Retrieves properties file of parsed parameters. This file is retrieved as system resource.
     * @return properties file.
     * @throws URISyntaxException if path isn't well formed.
     */
    private static File getPropertiesResource() throws URISyntaxException {
	File res;;
	
	String path = afirmaConfigFilePathResolver.getConfigFilePath(afirmaConfigFilePathResolver.ISPAC_BASE_CONFIG_SUBDIR_KEY_AFIRMA + File.separator + TransformersConstants.PARSED_PARAMETERS_FILE);
	
	if (path == null) {
	    throw new URISyntaxException("Error", Language.getFormatResIntegra(ILogConstantKeys.PPP_LOG005, new Object[ ] { TransformersConstants.PARSED_PARAMETERS_FILE }));
	}
	
	res = new File(path);

	return res;
    }

}

/* 
 * Licensed under the EUPL, Version 1.1 or – as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence. You may
 * obtain a copy of the Licence at:
 * 
 * http://www.osor.eu/eupl/european-union-public-licence-eupl-v.1.1
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * Licence for the specific language governing permissions and limitations under
 * the Licence.
 */

package eu.stork.peps.configuration;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import eu.stork.peps.exceptions.SAMLEngineException;
import eu.stork.peps.exceptions.STORKSAMLEngineRuntimeException;

/**
 * The Class ConfigurationReader.
 * 
 * @author fjquevedo
 */
public final class ConfigurationReader {

    /** The Constant SAML_ENGINE_CONFIGURATION_FILE. */
    private static final String ENGINE_CONF_FILE = "SamlEngine.xml";

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory
    .getLogger(ConfigurationReader.class.getName());

    /** The Constant NODE_CONFIGURATION. */
    private static final String NODE_CONF = "configuration";

    /** The Constant NODE_CONFIGURATION_NAME. */
    private static final String NODE_CONF_NAME = "name";

    /** The Constant NODE_INSTANCE_NAME. */
    private static final String NODE_INST_NAME = "name";

    /** The Constant NODE_INSTANCE. */
    private static final String NODE_INSTANCE = "instance";

    /** The Constant NODE_CONFIGURATION_NAME. */
    private static final String NODE_PARAM_NAME = "name";

    /** The Constant NODE_CONFIGURATION_NAME. */
    private static final String NODE_PARAM_VALUE = "value";

    /** The Constant NODE_CONFIGURATION_NAME. */
    private static final String NODE_PARAMETER = "parameter";

    /**
     * Generate parameters.
     * 
     * @param configurationNode the configuration node
     * 
     * @return the map< string, string>
     */
    private static Map<String, String> generateParam(
	    final Element configurationNode) {

	final HashMap<String, String> parameters = new HashMap<String, String>();

	final NodeList parameterNodes = configurationNode
	.getElementsByTagName(NODE_PARAMETER);

	String parameterName;
	String parameterValue;

	for (int k = 0; k < parameterNodes.getLength(); ++k) {
	    // for every parameter find, process.
	    final Element parameterNode = (Element) parameterNodes.item(k);
	    parameterName = parameterNode.getAttribute(NODE_PARAM_NAME);
	    parameterValue = parameterNode.getAttribute(NODE_PARAM_VALUE);

	    // verified the content.
	    if (StringUtils.isBlank(parameterName)
		    || StringUtils.isBlank(parameterValue)) {
		throw new STORKSAMLEngineRuntimeException(
		"Error reader parameters (name - value).");
	    } else {
		parameters.put(parameterName.trim(), parameterValue.trim());
	    }
	}
	return parameters;
    }

    /**
     * Read configuration.
     * 
     * @return the map< string, instance engine>
     * 
     * @throws SAMLEngineException the STORKSAML engine runtime
     *             exception
     */
    public static Map<String, InstanceEngine> readConfiguration()
    throws SAMLEngineException {

	LOGGER.info("Init reader: " + ENGINE_CONF_FILE);
	final Map<String, InstanceEngine> instanceConfs = 
	    new HashMap<String, InstanceEngine>();

	Document document = null;
	// Load configuration file
	final DocumentBuilderFactory factory = DocumentBuilderFactory
	.newInstance();
	DocumentBuilder builder;

	InputStream engineConf = null;
	try {

	    factory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);

	    builder = factory.newDocumentBuilder();
	    
	    engineConf = ConfigurationReader.class
	    .getResourceAsStream("/" + ENGINE_CONF_FILE);
	    
	    document = builder.parse(engineConf);

	    // Read instance
	    final NodeList list = document.getElementsByTagName(NODE_INSTANCE);

	    for (int indexElem = 0; indexElem < list.getLength(); ++indexElem) {
		final Element element = (Element) list.item(indexElem);

		final InstanceEngine instanceConf = new InstanceEngine();

		// read every configuration.
		final String instanceName = element
		.getAttribute(NODE_INST_NAME);

		if (StringUtils.isBlank(instanceName)) {
		    throw new STORKSAMLEngineRuntimeException(
		    "Error reader instance name.");
		}
		instanceConf.setName(instanceName.trim());

		final NodeList confNodes = element
		.getElementsByTagName(NODE_CONF);

		for (int indexNode = 0; indexNode < confNodes.getLength(); ++indexNode) {

		    final Element configurationNode = (Element) confNodes
		    .item(indexNode);

		    final String configurationName = configurationNode
		    .getAttribute(NODE_CONF_NAME);

		    if (StringUtils.isBlank(configurationName)) {
			throw new STORKSAMLEngineRuntimeException(
				"Error reader configuration name.");
		    }

		    final ConfigurationEngine confSamlEngine = 
			new ConfigurationEngine();

		    // Set configuration name.
		    confSamlEngine.setName(configurationName.trim());

		    // Read every parameter for this configuration.
		    final Map<String, String> parameters = 
			generateParam(configurationNode);

		    // Set parameters
		    confSamlEngine.setParameters(parameters);

		    // Add parameters to the configuration.
		    instanceConf.getConfiguration().add(confSamlEngine);
		}

		// Add to the list of configurations.
		instanceConfs.put(element.getAttribute(NODE_INST_NAME),
			instanceConf);
	    }

	} catch (SAXException e) {
	    LOGGER.error("Error: init library parser.");
	    throw new SAMLEngineException(e);
	} catch (ParserConfigurationException e) {
	    LOGGER.error("Error: parser configuration file xml.");
	    throw new SAMLEngineException(e);
	} catch (IOException e) {
	    LOGGER.error("Error: read configuration file.");
	    throw new SAMLEngineException(e);
	} finally {
	    IOUtils.closeQuietly(engineConf);
	}

	return instanceConfs;
    }

    /**
     * Instantiates a new configuration reader.
     */
    private ConfigurationReader() {

    }

}

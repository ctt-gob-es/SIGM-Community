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

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.stork.peps.exceptions.SAMLEngineException;
import eu.stork.peps.exceptions.STORKSAMLEngineException;
import eu.stork.peps.exceptions.STORKSAMLEngineRuntimeException;

/**
 * The Class InstanceCreator.
 * 
 * @author fjquevedo
 */
public final class ConfigurationSingleton {

    /** The instance of every engine SAML. */
    private static Map<String, InstanceEngine> instanceConfigs;

    /** The instances of SAML engine. */
    private static Map<String, Map<String, Object>> instances;

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory
	    .getLogger(ConfigurationSingleton.class.getName());

    static {
	LOGGER.debug("Read all file configurations. (instances of SAMLEngine)");
	try {
	    instanceConfigs = ConfigurationReader.readConfiguration();
	} catch (SAMLEngineException e) {
	    LOGGER.error("Error read configuration file.");
	    throw new STORKSAMLEngineRuntimeException(e);
	}
    }

    /**
     * Gets the new instance.
     * 
     * @param fileName the file name
     * 
     * @return the properties from the new instance
     * 
     * @throws STORKSAMLEngineException the STORKSAML engine runtime exception
     */
    private static Map<String, Map<String, Object>> getInstance(
	    final String fileName) throws STORKSAMLEngineException {
	return ConfigurationCreator.createConfiguration(instanceConfigs);
    }

    /**
     * Instantiates a new instance creator.
     */
    private ConfigurationSingleton() {
    }

}
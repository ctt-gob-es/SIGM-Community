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

package eu.stork.peps.auth.engine.core.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.stork.peps.auth.engine.core.SAMLEngineSignI;
import eu.stork.peps.exceptions.STORKSAMLEngineException;

/**
 * The Class ModuleSignFactory.
 * 
 * @author fjquevedo
 * 
 */

public final class SignModuleFactory {

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory
	    .getLogger(SignModuleFactory.class.getName());

    /**
     * Instantiates a new module sign factory.
     */
    private SignModuleFactory() {

    }

    /**
     * Gets the single instance of SignModuleFactory.
     * 
     * @param className the class name
     * 
     * @return single instance of SignModuleFactory
     * 
     * @throws STORKSAMLEngineException the STORKSAML engine exception
     */
    public static SAMLEngineSignI getInstance(final String className)
	    throws STORKSAMLEngineException {
	LOG.info("[START]SignModuleFactory static");
	try {
	    final Class cls = Class.forName(className);
	    return (SAMLEngineSignI) cls.newInstance();
	} catch (Exception e) {
	    throw new STORKSAMLEngineException(e);
	}

    }
}

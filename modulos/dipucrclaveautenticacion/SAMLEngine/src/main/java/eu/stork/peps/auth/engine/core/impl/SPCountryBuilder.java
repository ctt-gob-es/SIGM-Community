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

import org.opensaml.common.impl.AbstractSAMLObjectBuilder;

import eu.stork.peps.auth.engine.core.SPCountry;

/**
 * The Class SPCountryBuilder.
 * 
 * @author fjquevedo
 */
public class SPCountryBuilder extends AbstractSAMLObjectBuilder<SPCountry> {

    
    /**
     * Builds the object SPCountry.
     *
     * @return the service provider country
     */
    public final SPCountry buildObject() {
	return buildObject(SPCountry.DEF_ELEMENT_NAME);
    }

    
    /**
     * Builds the object SPCountry.
     *
     * @param namespaceURI the namespace uri
     * @param localName the local name
     * @param namespacePrefix the namespace prefix
     * @return the service provider country
     */
    public final SPCountry buildObject(final String namespaceURI,
	    final String localName, final String namespacePrefix) {
	return new SPCountryImpl(namespaceURI, localName, namespacePrefix);
    }
}
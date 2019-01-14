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

import eu.stork.peps.auth.engine.core.SAMLCore;
import eu.stork.peps.auth.engine.core.VIDPAuthenticationAttributes;

/**
 * The Class VIDPAuthenticationAttributesBuilder.
 * 
 * @author fjquevedo
 */
public final class VIDPAuthenticationAttributesBuilder extends AbstractSAMLObjectBuilder<VIDPAuthenticationAttributes> {
    

    /** {@inheritDoc} */
    public VIDPAuthenticationAttributes buildObject() {
        return buildObject(SAMLCore.STORK10P_NS.getValue(), VIDPAuthenticationAttributes.DEF_LOCAL_NAME, SAMLCore.STORK10P_PREFIX.getValue());
    }

    /** {@inheritDoc} */
    public VIDPAuthenticationAttributes buildObject(final String namespaceURI, final String localName, final String namespacePrefix) {
        return new VIDPAuthenticationAttributesImpl(namespaceURI, localName, namespacePrefix);
    }
    
}
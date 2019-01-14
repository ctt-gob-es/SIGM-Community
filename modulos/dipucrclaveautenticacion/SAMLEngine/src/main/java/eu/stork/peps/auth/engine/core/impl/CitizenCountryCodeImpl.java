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

import java.util.List;

import org.opensaml.common.impl.AbstractSAMLObject;
import org.opensaml.xml.XMLObject;

import eu.stork.peps.auth.engine.core.CitizenCountryCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class CitizenCountryCodeImpl.
 * 
 * @author fjquevedo
 */
public class CitizenCountryCodeImpl extends AbstractSAMLObject implements CitizenCountryCode {

    private static final Logger LOGGER = LoggerFactory.getLogger(CitizenCountryCodeImpl.class.getName());
    /** The citizen country code. */
    private String citizenCountryCode;
    
    /**
     * Instantiates a new sP country impl.
     * 
     * @param namespaceURI the namespace uri
     * @param elementLocalName the element local name
     * @param namespacePrefix the namespace prefix
     */   
    protected CitizenCountryCodeImpl(final String namespaceURI,
	    final String elementLocalName, final String namespacePrefix) {
	super(namespaceURI, elementLocalName, namespacePrefix);	
    }

    /**
     * Gets the citizen country code.
     *
     * @return the citizen country code
     */
    public final String getCitizenCountryCode() {
	return citizenCountryCode;
    }

    /**
     * Sets the citizen country code.
     *
     * @param newCitizenCountryCode the new citizen country code
     */
    public final void setCitizenCountryCode(final String newCitizenCountryCode) {
	this.citizenCountryCode = prepareForAssignment(this.citizenCountryCode, newCitizenCountryCode);
    }

    /**
     * Gets the ordered children.
     *
     * @return the ordered children
     */
    public final List<XMLObject> getOrderedChildren() {
	return null;
    }

    @Override
    public int hashCode() {
        LOGGER.warn("Hashcode has been called, passed to super. Nothing foreseen here");
        return super.hashCode();
    }
}

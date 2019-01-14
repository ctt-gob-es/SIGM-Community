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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opensaml.common.impl.AbstractSignableSAMLObject;
import org.opensaml.xml.XMLObject;

import eu.stork.peps.auth.engine.core.CitizenCountryCode;
import eu.stork.peps.auth.engine.core.SPInformation;
import eu.stork.peps.auth.engine.core.VIDPAuthenticationAttributes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class VIDPAuthenticationAttributesImpl.
 * 
 * @author fjquevedo
 */
public final class VIDPAuthenticationAttributesImpl extends AbstractSignableSAMLObject implements
VIDPAuthenticationAttributes {

    private static final Logger LOGGER = LoggerFactory.getLogger(VIDPAuthenticationAttributesImpl.class.getName());
    /** The citizen country code. */
    private CitizenCountryCode citizenCountryCode;
    
    /** The SP information. */
    private SPInformation spInformation;

	/**
     * Instantiates a new requested attributes implement.
     * 
     * @param namespaceURI the namespace URI
     * @param elementLocalName the element local name
     * @param namespacePrefix the namespace prefix
     */
    protected VIDPAuthenticationAttributesImpl(final String namespaceURI,
	    final String elementLocalName, final String namespacePrefix) {	
	super(namespaceURI, elementLocalName, namespacePrefix);
    }
    
   
    /**
     * getCitizenCountryCode.
     * 
     * @return the citizen country code
     */    
    public CitizenCountryCode getCitizenCountryCode() {
    	return citizenCountryCode;
    }
    
    /**
     * getSPInformation
     * 
     * @return the SP information
     */
    public SPInformation getSPInformation() {
		return spInformation;
	}

    /**
     * Gets the ordered children.
     * 
     * @return the ordered children
     *
     */
    public List<XMLObject> getOrderedChildren() {
	final ArrayList<XMLObject> children = new ArrayList<XMLObject>();

	children.add(citizenCountryCode);
	children.add(spInformation);
		
	if (getSignature() != null) {
	    children.add(getSignature());
	}
	
	return Collections.unmodifiableList(children);

    }

    /**
     * Gets the signature reference id.
     * 
     * @return the signature reference id
     *
     */
    public String getSignatureReferenceID() {
	return null;
    }
    
    /**
     * Sets the citizen country code.
     * 
     * @param newCitizenCountryCode the new citizen country code
     *
     */
    public void setCitizenCountryCode(CitizenCountryCode newCitizenCountryCode) {
	this.citizenCountryCode = prepareForAssignment(this.citizenCountryCode, newCitizenCountryCode);	
    }    
    
    /**
     * Sets the SP information.
     * 
     * @param newSPInformation the new SP information
     *
     */
    public void setSPInformation(SPInformation newSPInformation) {
		this.spInformation = prepareForAssignment(this.spInformation, newSPInformation);
	}

    @Override
    public int hashCode() {
        LOGGER.warn("Hashcode has been called, passed to super. Nothing foreseen here");
        return super.hashCode();
    }
}

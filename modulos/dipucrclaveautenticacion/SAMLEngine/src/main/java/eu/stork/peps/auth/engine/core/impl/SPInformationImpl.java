/* 
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
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

import eu.stork.peps.auth.engine.core.SPID;
import eu.stork.peps.auth.engine.core.SPInformation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class SPInformationImpl.
 * 
 * @author iinigo
 */
public final class SPInformationImpl extends AbstractSignableSAMLObject implements
SPInformation {

    private static final Logger LOGGER = LoggerFactory.getLogger(SPInformationImpl.class.getName());
    /** The citizen country code. */
    private SPID spId;
    
    
	/**
     * Instantiates a new requested attributes implement.
     * 
     * @param namespaceURI the namespace URI
     * @param elementLocalName the element local name
     * @param namespacePrefix the namespace prefix
     */
    protected SPInformationImpl(final String namespaceURI,
	    final String elementLocalName, final String namespacePrefix) {	
	super(namespaceURI, elementLocalName, namespacePrefix);
    }
    
   
    /**
     * getSPId.
     * 
     * @return the SP ID
     */    
    public SPID getSPID() {
    	return spId;
    }        

    /**
     * Gets the ordered children.
     * 
     * @return the ordered children
     *
     */
    public List<XMLObject> getOrderedChildren() {
	final ArrayList<XMLObject> children = new ArrayList<XMLObject>();

	children.add(spId);
		
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
     * Sets the SP Id.
     * 
     * @param newSPId the new SP Id
     *
     */
    public void setSPID(SPID newSPId) {
    	this.spId = prepareForAssignment(this.spId, newSPId);	
    }

    @Override
    public int hashCode() {
        LOGGER.warn("Hashcode has been called, passed to super. Nothing foreseen here");
        return super.hashCode();
    }
}

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

import eu.stork.peps.auth.engine.core.SPID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class SPIDImpl.
 * 
 * @author iinigo
 */
public class SPIDImpl extends AbstractSAMLObject implements SPID {

    private static final Logger LOGGER = LoggerFactory.getLogger(SPIDImpl.class.getName());
    /** The citizen country code. */
    private String spId;
    
    /**
     * Instantiates a new sP country impl.
     * 
     * @param namespaceURI the namespace uri
     * @param elementLocalName the element local name
     * @param namespacePrefix the namespace prefix
     */   
    protected SPIDImpl(final String namespaceURI,
	    final String elementLocalName, final String namespacePrefix) {
	super(namespaceURI, elementLocalName, namespacePrefix);	
    }

    /**
     * Gets the SP ID.
     *
     * @return the SP ID
     */
    public final String getSPID() {
    	return spId;
    }

    /**
     * Sets the SP ID.
     *
     * @param newSPID the new SP ID
     */
    public final void setSPID(final String newSPID) {
    	this.spId = prepareForAssignment(this.spId, newSPID);
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

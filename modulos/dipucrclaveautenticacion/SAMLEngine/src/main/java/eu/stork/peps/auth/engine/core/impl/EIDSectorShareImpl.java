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

import eu.stork.peps.auth.engine.core.EIDSectorShare;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

// TODO: Auto-generated Javadoc
/**
 * The Class EIDSectorShareImpl.
 * 
 * @author fjquevedo
 */
public class EIDSectorShareImpl extends AbstractSAMLObject implements
	EIDSectorShare {

    private static final Logger LOGGER = LoggerFactory.getLogger(EIDSectorShareImpl.class.getName());
	
    /** The e id sector share. */
    private String eIDSectorShare;
    /**
     * Instantiates a new eID sector share implementation.
     * 
     * @param namespaceURI the namespace URI
     * @param elementLocalName the element local name
     * @param namespacePrefix the namespace prefix
     */
    protected EIDSectorShareImpl(final String namespaceURI,
	    final String elementLocalName, final String namespacePrefix) {
	super(namespaceURI, elementLocalName, namespacePrefix);
    }
    
    
    /**
     * Gets the eID sector share.
     *
     * @return the eID sector share
     */
    public final String getEIDSectorShare() {
    	return eIDSectorShare;	
    }

    /**
     * Sets the eID sector share.
     *
     * @param newEIDSectorShare the new eID sector share
     */
    public final void setEIDSectorShare(String newEIDSectorShare) {
    	this.eIDSectorShare = prepareForAssignment(this.eIDSectorShare, newEIDSectorShare);
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
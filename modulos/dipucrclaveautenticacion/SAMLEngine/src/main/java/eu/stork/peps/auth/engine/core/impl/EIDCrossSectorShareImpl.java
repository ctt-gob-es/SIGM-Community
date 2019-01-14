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

import eu.stork.peps.auth.engine.core.EIDCrossSectorShare;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * The Class EIDCrossSectorShareImpl.
 * 
 * @author fjquevedo
 */
public class EIDCrossSectorShareImpl extends AbstractSAMLObject implements
	EIDCrossSectorShare {

    private static final Logger LOGGER = LoggerFactory.getLogger(EIDCrossSectorShareImpl.class.getName());
	/** The citizen country code. */
    private String eIDCrossSectorShare;


    /**
     * Instantiates a new eID cross sector share implementation.
     * 
     * @param namespaceURI the namespace URI
     * @param elementLocalName the element local name
     * @param namespacePrefix the namespace prefix
     */
    protected EIDCrossSectorShareImpl(final String namespaceURI,
	    final String elementLocalName, final String namespacePrefix) {
	super(namespaceURI, elementLocalName, namespacePrefix);
    }
    
    
  
    /**
     * Gets the eID cross sector share.
     *
     * @return the eID cross sector share
     */
    public final String getEIDCrossSectorShare() {
    	return eIDCrossSectorShare;
    }


    /**
     * Sets the eID cross sector share.
     *
     * @param newEIDCrossSectorShare the new eID cross sector share
     */
    public final void setEIDCrossSectorShare(String newEIDCrossSectorShare) {
    	this.eIDCrossSectorShare = prepareForAssignment(this.eIDCrossSectorShare, newEIDCrossSectorShare);
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
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

import eu.stork.peps.auth.engine.core.SPSector;


/**
 * The Class SPSectorImpl.
 * 
 * @author fjquevedo
 */
public class SPSectorImpl extends AbstractSAMLObject implements SPSector {

    /** The service provider sector. */
    private String spSector;

    /**
     * Instantiates a new Service provider sector implementation.
     * 
     * @param namespaceURI the namespace URI
     * @param elementLocalName the element local name
     * @param namespacePrefix the namespace prefix
     */
    protected SPSectorImpl(final String namespaceURI,
	    final String elementLocalName, final String namespacePrefix) {
	super(namespaceURI, elementLocalName, namespacePrefix);
    }

    
    /**
     * Gets the service provider sector.
     * 
     * @return the SP sector
     * 
     * @see eu.stork.peps.auth.engine.core.SPSector#getSPSector()
     */
    public final String getSPSector() {
	return spSector;
    }

    
    /**
     * Sets the service provider sector.
     * 
     * @param newSpSector the new service provider sector
     */
    public final void setSPSector(final String newSpSector) {
	this.spSector = prepareForAssignment(this.spSector, newSpSector);
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
        throw new UnsupportedOperationException("hashCode method not implemented");
    }
}

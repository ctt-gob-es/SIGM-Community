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

import eu.stork.peps.auth.engine.core.QAAAttribute;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * The Class QAAAttributeImpl.
 * 
 * @author fjquevedo
 */
public class QAAAttributeImpl extends AbstractSAMLObject implements
	QAAAttribute {

    private static final Logger LOGGER = LoggerFactory.getLogger(QAAAttributeImpl.class.getName());
    /** The quality authentication assurance level. */
    private String qaaLevel;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object
     * represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected QAAAttributeImpl(final String namespaceURI,
	    final String elementLocalName, final String namespacePrefix) {
	super(namespaceURI, elementLocalName, namespacePrefix);
    }

    /**
     * Gets the quality authentication assurance level.
     *
     * @return the quality authentication assurance level
     */
    public final String getQaaLevel() {
	return qaaLevel;
    }

    /**
     * Sets the quality authentication assurance level.
     *
     * @param newQaaLevel the new quality authentication assurance level
     */
    public final void setQaaLevel(final String newQaaLevel) {
	this.qaaLevel = prepareForAssignment(this.qaaLevel, newQaaLevel);
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

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

import java.util.Map.Entry;

import javax.xml.namespace.QName;

import org.opensaml.Configuration;
import org.opensaml.common.impl.AbstractSAMLObjectMarshaller;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

import eu.stork.peps.auth.engine.core.RequestedAttribute;

/**
 * The Class RequestedAttributeMarshaller.
 * 
 * @author fjquevedo
 */
public class RequestedAttributeMarshaller extends AbstractSAMLObjectMarshaller {

    /**
     * Marshall attributes.
     *
     * @param samlElement the SAML element
     * @param domElement the DOM element
     * @throws MarshallingException the marshalling exception
     */
    protected final void marshallAttributes(final XMLObject samlElement,
	    final Element domElement) throws MarshallingException {
	final RequestedAttribute requestedAttr = (RequestedAttribute) samlElement;

	if (requestedAttr.getName() != null) {
	    domElement.setAttributeNS(null,
		    RequestedAttribute.NAME_ATTRIB_NAME, requestedAttr
			    .getName());
	}

	if (requestedAttr.getNameFormat() != null) {
	    domElement.setAttributeNS(null,
		    RequestedAttribute.NAME_FORMAT_ATTR, requestedAttr
			    .getNameFormat());
	}

	if (requestedAttr.getFriendlyName() != null) {
	    domElement.setAttributeNS(null,
		    RequestedAttribute.FRIENDLY_NAME_ATT, requestedAttr
			    .getFriendlyName());
	}

	if (requestedAttr.getIsRequiredXSBoolean() != null) {
	    domElement.setAttributeNS(null,
		    RequestedAttribute.IS_REQUIRED_ATTR, requestedAttr
			    .getIsRequiredXSBoolean().toString());
	}

	Attr attr;
	for (Entry<QName, String> entry : requestedAttr.getUnknownAttributes()
		.entrySet()) {
	    attr = XMLHelper.constructAttribute(domElement.getOwnerDocument(),
		    entry.getKey());
	    attr.setValue(entry.getValue());
	    domElement.setAttributeNodeNS(attr);
	    if (Configuration.isIDAttribute(entry.getKey())
		    || requestedAttr.getUnknownAttributes().isIDAttribute(
			    entry.getKey())) {
		attr.getOwnerElement().setIdAttributeNode(attr, true);
	    }
	}
    }

}

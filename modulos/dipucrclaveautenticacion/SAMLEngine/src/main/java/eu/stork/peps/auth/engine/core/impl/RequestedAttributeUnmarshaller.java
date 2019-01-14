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

import javax.xml.namespace.QName;

import org.opensaml.common.impl.AbstractSAMLObjectUnmarshaller;

import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.schema.XSBooleanValue;
import org.opensaml.xml.util.XMLHelper;
import org.w3c.dom.Attr;

import eu.stork.peps.auth.engine.core.RequestedAttribute;
import eu.stork.peps.auth.engine.core.SAMLCore;

/**
 * The Class RequestedAttributeUnmarshaller.
 * 
 * @author fjquevedo
 */
public class RequestedAttributeUnmarshaller extends
	AbstractSAMLObjectUnmarshaller {

    /**
     * Process child element.
     * 
     * @param parentSAMLObject parent SAMLObject
     * @param childSAMLObject child SAMLObject
     * 
     * @throws UnmarshallingException error at unmarshall XML object
     */
    protected final void processChildElement(final XMLObject parentSAMLObject,
	    final XMLObject childSAMLObject) throws UnmarshallingException {

	final RequestedAttribute requestedAttr = (RequestedAttribute) parentSAMLObject;

	final QName childQName = childSAMLObject.getElementQName();
	if (childQName.getLocalPart().equals("AttributeValue")
		&& childQName.getNamespaceURI().equals(SAMLCore.STORK10_NS.getValue())) {
	    requestedAttr.getAttributeValues().add(childSAMLObject);
	} else {
	    super.processChildElement(parentSAMLObject, childSAMLObject);
	}
    }

    /**
     * Process attribute.
     *
     * @param samlObject the SAML object
     * @param attribute the attribute
     * @throws UnmarshallingException the unmarshalling exception
     */
    protected final void processAttribute(final XMLObject samlObject,
	    final Attr attribute) throws UnmarshallingException {

	final RequestedAttribute requestedAttr = (RequestedAttribute) samlObject;

	if (attribute.getLocalName()
		.equals(RequestedAttribute.NAME_ATTRIB_NAME)) {
	    requestedAttr.setName(attribute.getValue());
	} else if (attribute.getLocalName().equals(
		RequestedAttribute.NAME_FORMAT_ATTR)) {
	    requestedAttr.setNameFormat(attribute.getValue());
	} else if (attribute.getLocalName().equals(
		RequestedAttribute.FRIENDLY_NAME_ATT)) {
	    requestedAttr.setFriendlyName(attribute.getValue());
	} else if (attribute.getLocalName().equals(
		RequestedAttribute.IS_REQUIRED_ATTR)) {
	    requestedAttr.setIsRequired(attribute
		    .getValue());

	} else {
	    final QName attribQName = XMLHelper.getNodeQName(attribute);
	    if (attribute.isId()) {
		requestedAttr.getUnknownAttributes().registerID(attribQName);
	    }
	    requestedAttr.getUnknownAttributes().put(attribQName,
		    attribute.getValue());
	}
    }
}

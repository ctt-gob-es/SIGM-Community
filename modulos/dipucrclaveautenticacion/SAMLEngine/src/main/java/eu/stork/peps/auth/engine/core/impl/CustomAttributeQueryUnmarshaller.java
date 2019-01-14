package eu.stork.peps.auth.engine.core.impl;

import org.joda.time.DateTime;
import org.joda.time.chrono.ISOChronology;
import org.opensaml.common.SAMLVersion;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.RequestAbstractType;
import org.opensaml.saml2.core.impl.SubjectQueryUnmarshaller;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.AbstractXMLObjectUnmarshaller;
import org.opensaml.xml.io.UnmarshallingException;
import org.opensaml.xml.util.DatatypeHelper;
import org.opensaml.xml.util.XMLHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import eu.stork.peps.auth.engine.core.CustomAttributeQuery;
import eu.stork.peps.auth.engine.core.CustomRequestAbstractType;

public class CustomAttributeQueryUnmarshaller extends SubjectQueryUnmarshaller {
	
	private final Logger log = LoggerFactory.getLogger(AbstractXMLObjectUnmarshaller.class);
	/** {@inheritDoc} */
    protected void processChildElement(XMLObject parentSAMLObject, XMLObject childSAMLObject)
            throws UnmarshallingException {
        CustomAttributeQuery query = (CustomAttributeQuery) parentSAMLObject;

        if (childSAMLObject instanceof Attribute) {
            query.getAttributes().add((Attribute) childSAMLObject);
        } else {
            super.processChildElement(parentSAMLObject, childSAMLObject);
        }
    }
    
    /** {@inheritDoc} */
    public XMLObject unmarshall(Element domElement) throws UnmarshallingException {
        if (log.isTraceEnabled()) {
            log.trace("Starting to unmarshall DOM element {}", XMLHelper.getNodeQName(domElement));
        }
        
        checkElementIsTarget(domElement);

        //String namespaceURI, String elementLocalName, String namespacePrefix
        XMLObject xmlObject = new CustomAttributeQueryImpl(SAMLConstants.SAML20P_NS, CustomAttributeQuery.DEFAULT_ELEMENT_LOCAL_NAME,
                SAMLConstants.SAML20P_PREFIX);

        if (log.isTraceEnabled()) {
            log.trace("Unmarshalling attributes of DOM Element {}", XMLHelper.getNodeQName(domElement));
        }
        
        NamedNodeMap attributes = domElement.getAttributes();
        Node attribute;
        for (int i = 0; i < attributes.getLength(); i++) {
            attribute = attributes.item(i);

            // These should allows be attribute nodes, but just in case...
            if (attribute.getNodeType() == Node.ATTRIBUTE_NODE) {
                unmarshallAttribute(xmlObject, (Attr) attribute);
            }
        }

        if (log.isTraceEnabled()) {
            log.trace("Unmarshalling other child nodes of DOM Element {}", XMLHelper.getNodeQName(domElement));
        }
        
        Node childNode = domElement.getFirstChild();
        while (childNode != null) {

            if (childNode.getNodeType() == Node.ATTRIBUTE_NODE) {
                unmarshallAttribute(xmlObject, (Attr) childNode);
            } else if (childNode.getNodeType() == Node.ELEMENT_NODE) {
                unmarshallChildElement(xmlObject, (Element) childNode);
            } else if (childNode.getNodeType() == Node.TEXT_NODE
                    || childNode.getNodeType() == Node.CDATA_SECTION_NODE) {
                unmarshallTextContent(xmlObject, (Text) childNode);
            }
            
            childNode = childNode.getNextSibling();
        }

        xmlObject.setDOM(domElement);
        return xmlObject;
    }
    
    /** {@inheritDoc} */
    protected void processAttribute(XMLObject samlObject, Attr attribute) throws UnmarshallingException {
        CustomRequestAbstractType req = (CustomRequestAbstractType) samlObject;

        if (attribute.getLocalName().equals(RequestAbstractType.VERSION_ATTRIB_NAME)) {
            req.setVersion(SAMLVersion.valueOf(attribute.getValue()));
        } else if (attribute.getLocalName().equals(RequestAbstractType.ID_ATTRIB_NAME)) {
            req.setID(attribute.getValue());
            attribute.getOwnerElement().setIdAttributeNode(attribute, true);
        } else if (attribute.getLocalName().equals(RequestAbstractType.ISSUE_INSTANT_ATTRIB_NAME)
                && !DatatypeHelper.isEmpty(attribute.getValue())) {
            req.setIssueInstant(new DateTime(attribute.getValue(), ISOChronology.getInstanceUTC()));
        } else if (attribute.getLocalName().equals(RequestAbstractType.DESTINATION_ATTRIB_NAME)) {
            req.setDestination(attribute.getValue());
        } else if (attribute.getLocalName().equals(RequestAbstractType.CONSENT_ATTRIB_NAME)) {
            req.setConsent(attribute.getValue());
        } else if (attribute.getLocalName().equals(CustomRequestAbstractType.ASSERTION_CONSUMER_SERVICE_URL_ATTRIB_NAME)) {
        	req.setAssertionConsumerServiceURL(attribute.getValue());
        }else {
            super.processAttribute(samlObject, attribute);
        }
    }

}

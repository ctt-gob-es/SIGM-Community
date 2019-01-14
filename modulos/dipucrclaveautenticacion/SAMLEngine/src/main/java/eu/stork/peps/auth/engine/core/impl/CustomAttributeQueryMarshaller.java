package eu.stork.peps.auth.engine.core.impl;

import org.opensaml.Configuration;
import org.opensaml.saml2.core.RequestAbstractType;
import org.opensaml.saml2.core.impl.SubjectQueryMarshaller;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.io.MarshallingException;
import org.w3c.dom.Element;

import eu.stork.peps.auth.engine.core.CustomRequestAbstractType;

public class CustomAttributeQueryMarshaller extends SubjectQueryMarshaller {
	
	/** {@inheritDoc} */
    protected void marshallAttributes(XMLObject samlObject, Element domElement) throws MarshallingException {
        CustomRequestAbstractType req = (CustomRequestAbstractType) samlObject;

        if (req.getVersion() != null) {
            domElement.setAttributeNS(null, RequestAbstractType.VERSION_ATTRIB_NAME, req.getVersion().toString());
        }

        if (req.getID() != null) {
            domElement.setAttributeNS(null, RequestAbstractType.ID_ATTRIB_NAME, req.getID());
            domElement.setIdAttributeNS(null, RequestAbstractType.ID_ATTRIB_NAME, true);
        }

        if (req.getVersion() != null) {
            domElement.setAttributeNS(null, RequestAbstractType.VERSION_ATTRIB_NAME, req.getVersion().toString());
        }

        if (req.getIssueInstant() != null) {
            String iiStr = Configuration.getSAMLDateFormatter().print(req.getIssueInstant());
            domElement.setAttributeNS(null, RequestAbstractType.ISSUE_INSTANT_ATTRIB_NAME, iiStr);
        }

        if (req.getDestination() != null) {
            domElement.setAttributeNS(null, RequestAbstractType.DESTINATION_ATTRIB_NAME, req.getDestination());
        }
        
        if (req.getAssertionConsumerServiceURL() != null) {
            domElement.setAttributeNS(null, CustomRequestAbstractType.ASSERTION_CONSUMER_SERVICE_URL_ATTRIB_NAME, req.getAssertionConsumerServiceURL());
        }

        if (req.getConsent() != null) {
            domElement.setAttributeNS(null, RequestAbstractType.CONSENT_ATTRIB_NAME, req.getConsent());
        }
    }


}

package eu.stork.peps.auth.engine.core;

import java.util.List;
import javax.xml.namespace.QName;

import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.Subject;
import org.opensaml.saml2.core.SubjectQuery;

public interface CustomAttributeQuery extends CustomRequestAbstractType {
	/** Element local name. */
    public static final String DEFAULT_ELEMENT_LOCAL_NAME = "AttributeQuery";

    /** Default element name. */
    public static final QName DEFAULT_ELEMENT_NAME = new QName(SAMLConstants.SAML20P_NS, DEFAULT_ELEMENT_LOCAL_NAME,
            SAMLConstants.SAML20P_PREFIX);

    /** Local name of the XSI type. */
    public static final String TYPE_LOCAL_NAME = "CustomAttributeQueryType";

    /** QName of the XSI type. */
    public static final QName TYPE_NAME = new QName(SAMLConstants.SAML20P_NS, TYPE_LOCAL_NAME,
            SAMLConstants.SAML20P_PREFIX);
    
    /** AssertionConsumerServiceURL attribute name. */
    public static final String ASSERTION_CONSUMER_SERVICE_URL_ATTRIB_NAME 
    	= "AssertionConsumerServiceURL";

    /**
     * Gets the Attributes of this query.
     * 
     * @return the list of Attributes of this query
     */
    public List<Attribute> getAttributes();       
    
    /**
     * Gets the Subject of this request.
     * 
     * @return the Subject of this request
     */
    public Subject getSubject();

    /**
     * Sets the Subject of this request.
     * 
     * @param newSubject the Subject of this request
     */
    public void setSubject(Subject newSubject);

}

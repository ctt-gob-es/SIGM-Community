package eu.stork.peps.auth.engine.core.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.impl.SubjectQueryImpl;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.util.XMLObjectChildrenList;

import eu.stork.peps.auth.engine.core.CustomAttributeQuery;


public class CustomAttributeQueryImpl extends SubjectQueryImpl implements CustomAttributeQuery {
	/** Attribute child elements. */
    private final XMLObjectChildrenList<Attribute> attributes;
    private String serviceURL;

    /**
     * Constructor.
     * 
     * @param namespaceURI the namespace the element is in
     * @param elementLocalName the local name of the XML element this Object represents
     * @param namespacePrefix the prefix for the given namespace
     */
    protected CustomAttributeQueryImpl(String namespaceURI, String elementLocalName, String namespacePrefix) {
        super(namespaceURI, elementLocalName, namespacePrefix);
        attributes = new XMLObjectChildrenList<Attribute>(this);
    }

    /** {@inheritDoc} */
    public List<Attribute> getAttributes() {
        return attributes;
    }

    /** {@inheritDoc} */
    public List<XMLObject> getOrderedChildren() {
        ArrayList<XMLObject> children = new ArrayList<XMLObject>();

        if (super.getOrderedChildren() != null) {
            children.addAll(super.getOrderedChildren());
        }
        children.addAll(attributes);

        if (children.size() == 0) {
            return null;
        }

        return Collections.unmodifiableList(children);
    }

	@Override
	public String getAssertionConsumerServiceURL() {
		// TODO Auto-generated method stub
		return this.serviceURL;
	}

	@Override
	public void setAssertionConsumerServiceURL(String newServiceUrl) {
		// TODO Auto-generated method stub
		this.serviceURL = newServiceUrl;
	}

}

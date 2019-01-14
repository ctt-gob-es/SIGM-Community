package eu.stork.peps.auth.engine.core.impl;

import org.opensaml.common.impl.AbstractSAMLObjectBuilder;
import org.opensaml.common.xml.SAMLConstants;
import eu.stork.peps.auth.engine.core.CustomAttributeQuery;

public class CustomAttributeQueryBuilder extends AbstractSAMLObjectBuilder<CustomAttributeQuery> {
	/**
     * Constructor.
     */
    public CustomAttributeQueryBuilder() {

    }

    /** {@inheritDoc} */
    public CustomAttributeQuery buildObject() {
        return buildObject(SAMLConstants.SAML20P_NS, CustomAttributeQuery.DEFAULT_ELEMENT_LOCAL_NAME,
                SAMLConstants.SAML20P_PREFIX);
    }

    /** {@inheritDoc} */
    public CustomAttributeQuery buildObject(String namespaceURI, String localName, String namespacePrefix) {
        return new CustomAttributeQueryImpl(namespaceURI, localName, namespacePrefix);
    }

}

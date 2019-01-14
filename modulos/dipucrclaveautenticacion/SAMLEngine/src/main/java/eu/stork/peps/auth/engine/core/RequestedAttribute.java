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

package eu.stork.peps.auth.engine.core;

import java.util.List;

import javax.xml.namespace.QName;

import org.opensaml.common.SAMLObject;
import org.opensaml.xml.AttributeExtensibleXMLObject;
import org.opensaml.xml.schema.XSBooleanValue;
import org.opensaml.xml.XMLObject;

/**
 * The Interface RequestedAttribute.
 * 
 * @author fjquevedo
 */
public interface RequestedAttribute extends SAMLObject,
	AttributeExtensibleXMLObject {

    /** Element local name. */
    String DEF_LOCAL_NAME = "RequestedAttribute";

    /** Default element name. */
    QName DEF_ELEMENT_NAME = new QName(SAMLCore.STORK10_NS.getValue(), DEF_LOCAL_NAME,
	    SAMLCore.STORK10_PREFIX.getValue());

    /** Local name of the XSI type. */
    String TYPE_LOCAL_NAME = "RequestedAttributeAbstractType";

    /** QName of the XSI type. */
    QName TYPE_NAME = new QName(SAMLCore.STORK10_NS.getValue(), TYPE_LOCAL_NAME,
	    SAMLCore.STORK10_PREFIX.getValue());

    /** NAME_ATTRIB_NAME attribute name. */
    String NAME_ATTRIB_NAME = "Name";

    /** NAME_FORMAT_ATTRIB_NAME attribute name. */
    String NAME_FORMAT_ATTR = "NameFormat";

    /** IS_REQUIRED_ATTRIB_NAME attribute name. */
    String IS_REQUIRED_ATTR = "isRequired";

    /** FRIENDLY_NAME_ATTRIB_NAME attribute name. */
    String FRIENDLY_NAME_ATT = "FriendlyName";

    /** Unspecified attribute format ID. */
    String UNSPECIFIED = "urn:oasis:names:tc:SAML:2.0:attrname-format:unspecified";

    /** URI reference attribute format ID. */
    String URI_REFERENCE = "urn:oasis:names:tc:SAML:2.0:attrname-format:uri";

    /** Basic attribute format ID. */
    String BASIC = "urn:oasis:names:tc:SAML:2.0:attrname-format:basic";

    /**
     * Gets the name.
     * 
     * @return the name
     */
    String getName();

    /**
     * Sets the name.
     * 
     * @param name the new name
     */
    void setName(String name);

    /**
     * Gets the name format.
     * 
     * @return the name format
     */
    String getNameFormat();

    /**
     * Sets the name format.
     * 
     * @param nameFormat the new name format
     */
    void setNameFormat(String nameFormat);

    /**
     * Gets the friendly name.
     * 
     * @return the friendly name
     */
    String getFriendlyName();

    /**
     * Sets the friendly name.
     * 
     * @param friendlyName the new friendly name
     */
    void setFriendlyName(String friendlyName);

    /**
     * Gets the checks if is required.
     * 
     * @return the checks if is required
     */
    String isRequired();

    /**
     * Gets the checks if is required xs boolean.
     * 
     * @return the checks if is required xs boolean
     */
    String getIsRequiredXSBoolean();

    /**
     * Sets the checks if is required.
     * 
     * @param newIsRequired the new checks if is required
     */
    void setIsRequired(String newIsRequired);

    /**
     * Gets the attribute values.
     * 
     * @return the attribute values
     */
    List<XMLObject> getAttributeValues();

}

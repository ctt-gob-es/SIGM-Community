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

import javax.xml.namespace.QName;

import org.opensaml.common.SAMLObject;

/**
 * The Interface VIDPAuthenticationAttributes.
 * 
 * @author fjquevedo
 */
public interface VIDPAuthenticationAttributes extends SAMLObject {

    /** The Constant DEFAULT_ELEMENT_LOCAL_NAME. */
    String DEF_LOCAL_NAME = "VIDPAuthenticationAttributes";

    /** Default element name. */
    QName DEF_ELEMENT_NAME = new QName(SAMLCore.STORK10P_NS.getValue(), DEF_LOCAL_NAME,
	    SAMLCore.STORK10P_PREFIX.getValue());

    /** Local name of the XSI type. */
    String TYPE_LOCAL_NAME = "VIDPAuthenticationAttributesType";

    /** QName of the XSI type. */
    QName TYPE_NAME = new QName(SAMLCore.STORK10P_NS.getValue(), TYPE_LOCAL_NAME,
	    SAMLCore.STORK10P_PREFIX.getValue());


    /**
     * Gets the citizen country code.
     * 
     * @return the citizen country code
     */
    CitizenCountryCode getCitizenCountryCode();
    
    /**
     * Sets the citizen country code.
     * 
     * @param newCitizenCountryCode the new citizen country code
     */
    void setCitizenCountryCode(CitizenCountryCode newCitizenCountryCode);
    
    /**
     * Gets the SP information
     * 
     * @return the SP information
     */
    SPInformation getSPInformation();
    
    /**
     * Sets the SP information
     * 
     * @param newSPInformation the new SPInformation
     */
    void setSPInformation(SPInformation newSPInformation);
     
}

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
 * The Interface EIDCrossBorderShare.
 * 
 * @author fjquevedo
 */
public interface SPInstitution extends SAMLObject {

    /** The Constant DEFAULT_ELEMENT_LOCAL_NAME. */
    String DEF_LOCAL_NAME = "spInstitution";

    /** The Constant DEFAULT_ELEMENT_NAME. */
    QName DEF_ELEMENT_NAME = new QName(SAMLCore.STORK10_NS.getValue(), DEF_LOCAL_NAME,
	    SAMLCore.STORK10_PREFIX.getValue());

    /** The Constant TYPE_LOCAL_NAME. */
    String TYPE_LOCAL_NAME = "spInstitutionType";

    /** The Constant TYPE_NAME. */
    QName TYPE_NAME = new QName(SAMLCore.STORK10_NS.getValue(), TYPE_LOCAL_NAME,
	    SAMLCore.STORK10_PREFIX.getValue());

    /**
     * Gets the service provider institution.
     * 
     * @return the service provider institution
     */
    String getSPInstitution();

    /**
     * Sets the service provider institution.
     * 
     * @param spInstitution the new service provider institution
     */
    void setSPInstitution(String spInstitution);
}

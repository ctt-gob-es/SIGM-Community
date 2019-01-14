/*
 * This work is Open Source and licensed by the European Commission under the
 * conditions of the European Public License v1.1 
 *  
 * (http://www.osor.eu/eupl/european-union-public-licence-eupl-v.1.1); 
 * 
 * any use of this file implies acceptance of the conditions of this license. 
 * Unless required by applicable law or agreed to in writing, software 
 * distributed under the License is distributed on an "AS IS" BASIS,  WITHOUT 
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the 
 * License for the specific language governing permissions and limitations 
 * under the License.
 */
package eu.stork.peps.auth.commons;

/**
 * This enum class contains the SAML Token Sub Status Code.
 *
 * @author ricardo.ferreira@multicert.com, renato.portela@multicert.com,
 * luis.felix@multicert.com, hugo.magalhaes@multicert.com,
 * paulo.ribeiro@multicert.com
 * @version $Revision: 1.7 $, $Date: 2010-11-17 05:15:28 $
 */
public enum STORKSubStatusCode {

    /**
     * URI for AuthnFailed status code.
     */
    AUTHN_FAILED_URI("urn:oasis:names:tc:SAML:2.0:status:AuthnFailed"),
    /**
     * URI for InvalidAttrNameOrValue status code.
     */
    INVALID_ATTR_NAME_VALUE_URI(
            "urn:oasis:names:tc:SAML:2.0:status:InvalidAttrNameOrValue"),
    /**
     * URI for InvalidNameIDPolicy status code.
     */
    INVALID_NAMEID_POLICY_URI(
            "urn:oasis:names:tc:SAML:2.0:status:InvalidNameIDPolicy"),
    /**
     * URI for VersionMismatch status code.
     */
    VERSION_MISMATCH_URI("urn:oasis:names:tc:SAML:2.0:status:VersionMismatch"),
    /**
     * URI for RequestDenied status code.
     */
    REQUEST_DENIED_URI("urn:oasis:names:tc:SAML:2.0:status:RequestDenied"),
    /**
     * URI for QaaNotSupported status code.
     */
    QAA_NOT_SUPPORTED(
            "http://www.stork.gov.eu/saml20/statusCodes/QAANotSupported");

    /**
     * Represents the constant's value.
     */
    private String value;

    /**
     * Solo Constructor.
     *
     * @param val The Constant value.
     */
    private STORKSubStatusCode(final String val) {

        this.value = val;
    }

    /**
     * Return the Constant Value.
     *
     * @return The constant value.
     */
    public String toString() {

        return value;
    }
}

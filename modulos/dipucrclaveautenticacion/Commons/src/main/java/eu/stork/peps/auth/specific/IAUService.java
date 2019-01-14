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
package eu.stork.peps.auth.specific;

import java.util.Map;

import eu.stork.peps.auth.commons.IPersonalAttributeList;
import eu.stork.peps.auth.commons.IStorkSession;
import eu.stork.peps.auth.commons.STORKAttrQueryResponse;
import eu.stork.peps.auth.commons.STORKAuthnResponse;

/**
 * Interface for Specific Authentication methods.
 *
 * @author ricardo.ferreira@multicert.com, renato.portela@multicert.com,
 * luis.felix@multicert.com, hugo.magalhaes@multicert.com
 */
public interface IAUService {

    /**
     * Prepares the citizen to be redirected to the IdP.
     *
     * @param personalList The Personal Attribute List.
     * @param parameters The parameters.
     * @param session The session object.
     * @param requestAttributes The Requested attributes.
     *
     * @return byte[] containing a SAML Request.
     *
     * @see IPersonalAttributeList
     * @see IStorkSession
     */
    byte[] prepareCitizenAuthentication(IPersonalAttributeList personalList,
            Map<String, Object> parameters, Map<String, Object> requestAttributes,
            IStorkSession session);

    /**
     * Prepares the citizen to be redirected to the PV.
     *
     * @param personalList The Personal Attribute List.
     * @param parameters The parameters.
     * @param session The session object.
     * @param requestAttributes The Requested attributes.
     *
     * @return byte[] containing a SAML Request.
     *
     * @see IPersonalAttributeList
     * @see IStorkSession
     */
    byte[] preparePVRequest(IPersonalAttributeList personalList,
            Map<String, Object> parameters, Map<String, Object> requestAttributes,
            IStorkSession session);

    /**
     * Authenticates a citizen.
     *
     * @param personalList The Personal Attribute List.
     * @param parameters The parameters.
     * @param requestAttributes The requested attributes.
     *
     * @return The updated Personal Attribute List.
     *
     * @see IPersonalAttributeList
     */
    IPersonalAttributeList authenticateCitizen(
            IPersonalAttributeList personalList, Map<String, Object> parameters,
            Map<String, Object> requestAttributes);

    /**
     * Validates a power.
     *
     * @param personalList The Personal Attribute List.
     * @param parameters The parameters.
     * @param requestAttributes The requested attributes.
     *
     * @return The updated Personal Attribute List (power validated).
     *
     * @see IPersonalAttributeList
     */
    IPersonalAttributeList powerValidation(
            IPersonalAttributeList personalList, Map<String, Object> parameters,
            Map<String, Object> requestAttributes);

    /**
     * Prepares the Citizen browser to be redirected to the AP.
     *
     * @param personalList The Personal Attribute List.
     * @param parameters The parameters.
     * @param session The session object.
     * @param requestAttributes The requested attributes.
     *
     * @return true in case of no error.
     *
     * @see IPersonalAttributeList
     * @see IStorkSession
     */
    boolean prepareAPRedirect(IPersonalAttributeList personalList,
            Map<String, Object> parameters, Map<String, Object> requestAttributes,
            IStorkSession session);

    /**
     * Returns the attributes values from the AP.
     *
     * @param personalList The Personal Attribute List.
     * @param parameters The parameters.
     * @param requestAttributes The request attributes.
     *
     * @return The updated Personal Attribute List.
     *
     * @see IPersonalAttributeList
     */
    IPersonalAttributeList getAttributesFromAttributeProviders(
            IPersonalAttributeList personalList, Map<String, Object> parameters,
            Map<String, Object> requestAttributes);

    /**
     * Get the attributes from the AP with verification.
     *
     * @param personalList The Personal Attribute List.
     * @param parameters The HTTP Parameters.
     * @param requestAttributes The requested Attributes.
     * @param session The session object.
     * @param auProcessId The SAML identifier.
     *
     * @return true if the attributes were correctly verified.
     *
     * @see IPersonalAttributeList
     * @see IStorkSession
     */
    boolean getAttributesWithVerification(IPersonalAttributeList personalList,
            Map<String, Object> parameters, Map<String, Object> requestAttributes,
            IStorkSession session, String auProcessId);

    /**
     * Validates a SAML Response.
     *
     * @param samlToken The SAML Token.
     * @param session The session object.
     *
     * @return the STORKAuthnResponse associated with the validated response.
     *
     * @see IStorkSession
     */
    STORKAuthnResponse processAuthenticationResponse(byte[] samlToken,
            IStorkSession session);

    /**
     * Generates a SAML Response in case of error.
     *
     * @param inResponseTo The SAML's identifier to response.
     * @param issuer The issuer value.
     * @param assertionURL The assertion URL.
     * @param code The error code.
     * @param subcode The sub error code.
     * @param message The error message.
     * @param ipUserAddress The user IP address.
     *
     * @return byte[] containing the SAML Response.
     */
    byte[] generateErrorAuthenticationResponse(String inResponseTo,
            String issuer, String assertionURL, String code, String subcode,
            String message, String ipUserAddress);

    /**
     * Compares two given personal attribute lists.
     *
     * @param original The original Personal Attribute List.
     * @param modified The modified Personal Attribute List.
     * @return true if the original list contains the modified one. False
     * otherwise.
     *
     * @see IPersonalAttributeList
     */
    boolean comparePersonalAttributeLists(IPersonalAttributeList original,
            IPersonalAttributeList modified);

    /**
     * Prepares the citizen to be redirected to the AtP.
     *
     * @param personalList The Personal Attribute List.
     * @param parameters The parameters.
     * @param session The session object.
     *
     * @return byte[] containing a SAML Request.
     *
     * @see IPersonalAttributeList
     * @see IStorkSession
     */
    byte[] prepareAttributeRequest(IPersonalAttributeList personalList,
            Map<String, Object> parameters, IStorkSession session);

    /**
     * Validates a SAML Response.
     *
     * @param samlToken The SAML Token.
     * @param session The session object.
     *
     * @return the STORKAttrQueryResponse associated with the validated
     * response.
     *
     * @see IStorkSession
     */
    STORKAttrQueryResponse processAttributeResponse(byte[] samlToken,
            IStorkSession session);
}

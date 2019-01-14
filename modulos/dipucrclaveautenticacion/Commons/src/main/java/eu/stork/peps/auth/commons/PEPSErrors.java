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
 * This enum class contains all the STORK PEPS, Commons and Specific errors
 * constant identifiers.
 *
 * @author ricardo.ferreira@multicert.com, renato.portela@multicert.com,
 * luis.felix@multicert.com, hugo.magalhaes@multicert.com,
 * paulo.ribeiro@multicert.com
 * @version $Revision: 1.10 $, $Date: 2011-02-17 22:44:34 $
 */
public enum PEPSErrors {

    /**
     * Represents the 'authenticationFailed' constant error identifier.
     */
    AUTHENTICATION_FAILED_ERROR("authenticationFailed"),
    /**
     * Represents the 'spCountrySelector.errorCreatingSAML' constant error
     * identifier.
     */
    SP_COUNTRY_SELECTOR_ERROR_CREATE_SAML("spCountrySelector.errorCreatingSAML"),
    /**
     * Represents the 'spCountrySelector.destNull' constant error identifier.
     */
    SP_COUNTRY_SELECTOR_DESTNULL("spCountrySelector.destNull"),
    /**
     * Represents the 'spCountrySelector.invalidAttr' constant error identifier.
     */
    SP_COUNTRY_SELECTOR_INVALID_ATTR("spCountrySelector.invalidAttr"),
    /**
     * Represents the 'spCountrySelector.invalidProviderName' constant error
     * identifier.
     */
    SP_COUNTRY_SELECTOR_INVALID_PROVIDER_NAME(
            "spCountrySelector.invalidProviderName"),
    /**
     * Represents the 'spCountrySelector.invalidQaaSPid' constant error
     * identifier.
     */
    SP_COUNTRY_SELECTOR_INVALID_QAASPID("spCountrySelector.invalidQaaSPid"),
    /**
     * Represents the 'spCountrySelector.invalidSpId' constant error identifier.
     */
    SP_COUNTRY_SELECTOR_INVALID_SPID("spCountrySelector.invalidSpId"),
    /**
     * Represents the 'spCountrySelector.invalidSPQAA' constant error
     * identifier.
     */
    SP_COUNTRY_SELECTOR_INVALID_SPQAA("spCountrySelector.invalidSPQAA"),
    /**
     * Represents the 'spCountrySelector.invalidSpURL' constant error
     * identifier.
     */
    SP_COUNTRY_SELECTOR_INVALID_SPURL("spCountrySelector.invalidSpURL"),
    /**
     * Represents the 'spCountrySelector.spNotAllowed' constant error
     * identifier.
     */
    SP_COUNTRY_SELECTOR_SPNOTALLOWED("spCountrySelector.spNotAllowed"),
    /**
     * Represents the 'sProviderAction.errorCreatingSAML' constant error
     * identifier.
     */
    SPROVIDER_SELECTOR_ERROR_CREATE_SAML("sProviderAction.errorCreatingSAML"),
    /**
     * Represents the 'sProviderAction.attr' constant error identifier.
     */
    SPROVIDER_SELECTOR_INVALID_ATTR("sProviderAction.invalidAttr"),
    /**
     * Represents the 'sProviderAction.country' constant error identifier.
     */
    SPROVIDER_SELECTOR_INVALID_COUNTRY("sProviderAction.invalidCountry"),
    /**
     * Represents the 'sProviderAction.relayState' constant error identifier.
     */
    SPROVIDER_SELECTOR_INVALID_RELAY_STATE("sProviderAction.invalidRelayState"),
    /**
     * Represents the 'sProviderAction.saml' constant error identifier.
     */
    SPROVIDER_SELECTOR_INVALID_SAML("sProviderAction.invalidSaml"),
    /**
     * Represents the 'sProviderAction.spAlias' constant error identifier.
     */
    SPROVIDER_SELECTOR_INVALID_SPALIAS("sProviderAction.invalidSPAlias"),
    /**
     * Represents the 'sProviderAction.spDomain' constant error identifier.
     */
    SPROVIDER_SELECTOR_INVALID_SPDOMAIN("sProviderAction.invalidSPDomain"),
    /**
     * Represents the 'sProviderAction.spId' constant error identifier.
     */
    SPROVIDER_SELECTOR_INVALID_SPID("sProviderAction.invalidSPId"),
    /**
     * Represents the 'sProviderAction.spQAA' constant error identifier.
     */
    SPROVIDER_SELECTOR_INVALID_SPQAA("sProviderAction.invalidSPQAA"),
    /**
     * Represents the 'sProviderAction.spQAAId' constant error identifier.
     */
    SPROVIDER_SELECTOR_INVALID_SPQAAID("sProviderAction.invalidSPQAAId"),
    /**
     * Represents the 'sProviderAction.spRedirect' constant error identifier.
     */
    SPROVIDER_SELECTOR_INVALID_SPREDIRECT("sProviderAction.invalidSPRedirect"),
    /**
     * Represents the 'sPPowerValidationAction.invalidSPPVAttrList' constant
     * error identifier.
     */
    SPPOWERVALIDATION_SELECTOR_INVALID_SP_PV_ATTR_LIST("sPPowerValidationAction.invalidSPPVAttrList"),
    /**
     * Represents the 'sProviderAction.invalidSPProviderName' constant error
     * identifier.
     */
    SPROVIDER_SELECTOR_INVALID_SP_PROVIDERNAME(
            "sProviderAction.invalidSPProviderName"),
    /**
     * Represents the 'sProviderAction.spNotAllowed' constant error identifier.
     */
    SPROVIDER_SELECTOR_SPNOTALLOWED("sProviderAction.spNotAllowed"),
    /**
     * Represents the 'internalError' constant error identifier.
     */
    INTERNAL_ERROR("internalError"),
    /**
     * Represents the 'colleagueRequest.attrNull' constant error identifier.
     */
    COLLEAGUE_REQ_ATTR_NULL("colleagueRequest.attrNull"),
    /**
     * Represents the 'colleagueRequest.errorCreatingSAML' constant error
     * identifier.
     */
    COLLEAGUE_REQ_ERROR_CREATE_SAML("colleagueRequest.errorCreatingSAML"),
    /**
     * Represents the 'colleagueRequest.invalidCountryCode' constant error
     * identifier.
     */
    COLLEAGUE_REQ_INVALID_COUNTRYCODE("colleagueRequest.invalidCountryCode"),
    /**
     * Represents the 'colleagueRequest.invalidDestUrl' constant error
     * identifier.
     */
    COLLEAGUE_REQ_INVALID_DEST_URL("colleagueRequest.invalidDestUrl"),
    /**
     * Represents the 'colleagueRequest.invalidQaa' constant error identifier.
     */
    COLLEAGUE_REQ_INVALID_QAA("colleagueRequest.invalidQaa"),
    /**
     * Represents the 'colleagueRequest.invalidRedirect' constant error
     * identifier.
     */
    COLLEAGUE_REQ_INVALID_REDIRECT("colleagueRequest.invalidRedirect"),
    /**
     * Represents the 'colleagueRequest.invalidSAML' constant error identifier.
     */
    COLLEAGUE_REQ_INVALID_SAML("colleagueRequest.invalidSAML"),
    /**
     * Represents the 'colleaguePVRequest.invalidPVAttrList' constant error
     * identifier.
     */
    COLLEAGUE_PV_REQ_INVALID_PV_ATTR_LIST("colleaguePVRequest.invalidPVAttrList"),
    /**
     * Represents the 'cpepsRedirectUrl' constant error identifier.
     */
    CPEPS_REDIRECT_URL("cpepsRedirectUrl"),
    /**
     * Represents the 'spepsRedirectUrl' constant error identifier.
     */
    SPEPS_REDIRECT_URL("spepsRedirectUrl"),
    /**
     * Represents the 'sProviderAction.invCountry' constant error identifier.
     */
    SP_ACTION_INV_COUNTRY("sProviderAction.invCountry"),
    /**
     * Represents the 'providernameAlias.invalid' constant error identifier.
     */
    PROVIDER_ALIAS_INVALID("providernameAlias.invalid"),
    /**
     * Represents the 'cPeps.attrNull' constant error identifier.
     */
    CPEPS_ATTR_NULL("cPeps.attrNull"),
    /**
     * Represents the 'colleagueResponse.invalidSAML' constant error identifier.
     */
    COLLEAGUE_RESP_INVALID_SAML("colleagueResponse.invalidSAML"),
    /**
     * Represents the 'citizenNoConsent.mandatory' constant error identifier.
     */
    CITIZEN_NO_CONSENT_MANDATORY("citizenNoConsent.mandatory"),
    /**
     * Represents the 'citizenResponse.mandatory' constant error identifier.
     */
    CITIZEN_RESPONSE_MANDATORY("citizenResponse.mandatory"),
    /**
     * Represents the 'attVerification.mandatory' constant error identifier.
     */
    ATT_VERIFICATION_MANDATORY("attVerification.mandatory"),
    /**
     * Represents the 'attrValue.verification' constant error identifier.
     */
    ATTR_VALUE_VERIFICATION("attrValue.verification"),
    /**
     * Represents the 'audienceRestrictionError' constant error identifier.
     */
    AUDIENCE_RESTRICTION("audienceRestrictionError"),
    /**
     * Represents the 'auRequestIdError' constant error identifier.
     */
    AU_REQUEST_ID("auRequestIdError"),
    /**
     * Represents the 'domain' constant error identifier.
     */
    DOMAIN("domain"),
    /**
     * Represents the 'hash.error' constant error identifier.
     */
    HASH_ERROR("hash.error"),
    /**
     * Represents the 'invalidAttributeList' constant error identifier.
     */
    INVALID_ATTRIBUTE_LIST("invalidAttributeList"),
    /**
     * Represents the 'invalidAttributeValue' constant error identifier.
     */
    INVALID_ATTRIBUTE_VALUE("invalidAttributeValue"),
    /**
     * Represents the 'qaaLevel' constant error identifier.
     */
    QAALEVEL("qaaLevel"),
    /**
     * Represents the 'requests' constant error identifier.
     */
    REQUESTS("requests"),
    /**
     * Represents the 'SPSAMLRequest' constant error identifier.
     */
    SP_SAML_REQUEST("SPSAMLRequest"),
    /**
     * Represents the 'spepsSAMLRequest' constant error identifier.
     */
    SPEPS_SAML_REQUEST("spepsSAMLRequest"),
    /**
     * Represents the 'IdPSAMLResponse' constant error identifier.
     */
    IDP_SAML_RESPONSE("IdPSAMLResponse"),
    /**
     * Represents the 'cpepsSAMLResponse' constant error identifier.
     */
    CPEPS_SAML_RESPONSE("cpepsSAMLResponse"),
    /**
     * Represents the 'cpepsSAMLResponse' constant error identifier.
     */
    SPEPS_SAML_RESPONSE("spepsSAMLResponse"),
    /**
     * Represents the 'session' constant error identifier.
     */
    SESSION("session"),
    /**
     * Represents the 'invalid.session' constant error identifier.
     */
    INVALID_SESSION("invalid.session"),
    /**
     * Represents the 'invalid.sessionId' constant error identifier.
     */
    INVALID_SESSION_ID("invalid.sessionId"),
    /**
     * Represents the 'missing.sessionId' constant error identifier.
     */
    MISSING_SESSION_ID("sessionError"),
    /**
     * Represents the 'missing.mandate' constant error identifier.
     */
    MISSING_MANDATE("missing.mandate"),
    /**
     * Represents the 'AtPSAMLResponse' constant error identifier.
     */
    ATP_SAML_RESPONSE("AtPSAMLResponse"),
    /**
     * Represents the 'AtPSAMLResponse' constant error identifier.
     */
    ATP_RESPONSE_ERROR("atp.response.error"),
    /**
     * Represents the 'apepsSAMLRequest' constant error identifier.
     */
    APEPS_SAML_REQUEST("apepsSAMLRequest"),
    /**
     * Represents the 'apepsSAMLResponse' constant error identifier.
     */
    APEPS_SAML_RESPONSE("apepsSAMLResponse"),
    /**
     * Represents the 'invalid.apepsRedirectUrl' constant error identifier.
     */
    INVALID_APEPS_REDIRECT_URL("invalid.apepsRedirectUrl"),
    /**
     * Represents the 'invalid.apepsCallbackUrl' constant error identifier.
     */
    INVALID_APEPS_CALLBACK_URL("invalid.apepsCallbackUrl"),
    /**
     * Represents the 'colleagueAttributeRequest.invalidSAML' constant error
     * identifier.
     */
    COLLEAGUE_ATTR_REQ_INVALID_SAML("colleagueAttributeRequest.invalidSAML"),
    /**
     * Represents the 'invalid.attr.country.code' constant error identifier.
     */
    INVALID_COUNTRY_CODE("invalid.attr.country.code"),
    /**
     * DTL error codes.
     */
    DTL_ERROR_ADD("dtl.error.adding.doc"),
    DTL_ERROR_GET("dtl.error.getting.doc"),
    DTL_ERROR_REQUEST("dtl.error.request.attribute"),
    DTL_INVALID_XML("dtl.invalid.xml"),
    DTL_EMPTY_REQUEST("dtl.empty.request"),
    DTL_ERROR_DOCUMENT_URL("dtl.error.no.document.url"),
    DTL_ERROR_NO_DOCUMENT("dtl.error.no.document"),
    DTL_ERROR_MARSHALL_SIGNREQUEST("dtl.error.marshall.signrequest"),
    DTL_ERROR_MARSHALL_SIGNRESPONSE("dtl.error.marshall.signresponse"),
    /**
     * Represents the 'colleagueAttributeRequest.invalidSAML' constant error
     * identifier.
     */
    COLLEAGUE_LOGOUT_INVALID_SAML("colleagueLogoutRequest.invalidSAML");

    /**
     * Represents the constant's value.
     */
    private String error;

    /**
     * Solo Constructor.
     *
     * @param nError The Constant error value.
     */
    PEPSErrors(final String nError) {
        this.error = nError;
    }

    /**
     * Construct the errorCode Constant value.
     *
     * @return The errorCode Constant.
     */
    public String errorCode() {
        return error + ".code";
    }

    /**
     * Construct the errorCode Constant value with the given code text.
     *
     * @param text the code text to append to the constant.
     *
     * @return The errorCode Constant for the given code text.
     */
    public String errorCode(final String text) {
        return error + "." + text + ".code";
    }

    /**
     * Construct the errorMessage constant value.
     *
     * @return The errorMessage constant.
     */
    public String errorMessage() {
        return error + ".message";
    }

    /**
     * Construct the errorMessage Constant value with the given message text.
     *
     * @param text the message text to append to the constant.
     *
     * @return The errorMessage Constant for the given text.
     */
    public String errorMessage(final String text) {
        return error + "." + text + ".message";
    }

    /**
     * Return the Constant Value.
     *
     * @return The constant value.
     */
    public String toString() {
        return error;
    }
}

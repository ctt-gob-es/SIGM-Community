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
 * This enum class contains all the value constants.
 *
 * @author ricardo.ferreira@multicert.com, renato.portela@multicert.com,
 * luis.felix@multicert.com, hugo.magalhaes@multicert.com,
 * paulo.ribeiro@multicert.com
 * @version $Revision: 1.17 $, $Date: 2011-07-07 20:48:45 $
 */
public enum PEPSValues {

    /**
     * Represents the 'all' constant value.
     */
    ALL("all"),
    /**
     * Represents the 'none' constant value.
     */
    NONE("none"),
    /**
     * Represents the 'true' constant value.
     */
    TRUE("true"),
    /**
     * Represents the 'false' constant value.
     */
    FALSE("false"),
    /**
     * Represents the empty string constant value.
     */
    EMPTY_STRING(""),
    /**
     * Represents the ',' separator constant value.
     */
    ATTRIBUTE_VALUE_SEP(","),
    /**
     * Represents the ';' separator constant value.
     */
    ATTRIBUTE_SEP(";"),
    /**
     * Represents the ':' separator constant value.
     */
    ATTRIBUTE_TUPLE_SEP(":"),
    /**
     * Represents the '/' separator constant value.
     */
    EID_SEPARATOR("/"),
    /**
     * Represents the ' - ' separator constant value.
     */
    ERROR_MESSAGE_SEP(" - "),
    /**
     * Represents the '#' parameter constant value.
     */
    LOGGER_SEP("#"),
    /**
     * Represents the 'NOT_AVAILABLE' parameter constant value.
     */
    NOT_AVAILABLE("NotAvailable"),
    /**
     * Represents the ';' parameter constant value.
     */
    SPEPS_AUTHORIZED_SEP(";"),
    /**
     * Represents the 'ap' constant value.
     */
    AP("ap"),
    /**
     * Represents the 'C-PEPS' constant value.
     */
    CPEPS("C-PEPS"),
    /**
     * Represents the 'cpeps' constant value.
     */
    CPEPS_PREFIX("cpeps"),
    /**
     * Represents the 'peps' constant value.
     */
    PEPS("peps"),
    /**
     * Represents the '-PEPS' constant value.
     */
    PEPS_SUFFIX("-PEPS"),
    /**
     * Represents the 'SP' constant value.
     */
    SP("SP"),
    /**
     * Represents the 'S-PEPS' constant value.
     */
    SPEPS("S-PEPS"),
    /**
     * Represents the 'speps' constant value.
     */
    SPEPS_PREFIX("speps"),
    /**
     * Represents the 'sp.default.parameters' constant value.
     */
    DEFAULT("sp.default.parameters"),
    /**
     * Represents the default saml id constant value.
     */
    DEFAULT_SAML_ID("1"),
    /**
     * Represents the 'hashDigest.className' constant value.
     */
    HASH_DIGEST_CLASS("hashDigest.className"),
    /**
     * Represents the 'eu.stork.communication.requests' constant value.
     */
    STORK_PACKAGE_REQUEST_LOGGER_VALUE("eu.stork.communication.requests"),
    /**
     * Represents the 'eu.stork.communication.responses' constant value.
     */
    STORK_PACKAGE_RESPONSE_LOGGER_VALUE("eu.stork.communication.responses"),
    /**
     * Represents the 'S-PEPS receives request from SP' constant value.
     */
    SP_REQUEST("S-PEPS receives request from SP"),
    /**
     * Represents the 'Get Citizen Consent' constant value.
     */
    CITIZEN_CONSENT_LOG("Get Citizen Consent"),
    /**
     * Represents the 'C-PEPS receives request from S-PEPS' constant value.
     */
    CPEPS_REQUEST("C-PEPS receives request from S-PEPS"),
    /**
     * Represents the 'C-PEPS generates response to S-PEPS' constant value.
     */
    CPEPS_RESPONSE("C-PEPS generates response to S-PEPS"),
    /**
     * Represents the 'S-PEPS generates request to C-PEPS' constant value.
     */
    SPEPS_REQUEST("S-PEPS generates request to C-PEPS"),
    /**
     * Represents the 'S-PEPS receives response from C-PEPS' constant value.
     */
    SPEPS_RESPONSE("S-PEPS receives response from C-PEPS"),
    /**
     * Represents the 'S-PEPS generates response to SP' constant value.
     */
    SP_RESPONSE("S-PEPS generates response to SP"),
    /**
     * Represents the 'Success' constant value.
     */
    SUCCESS("Success"),
    /**
     * Represents the December's month number constant value.
     */
    LAST_MONTH("12"),
    /**
     * Represents the yyyyMM constant value.
     */
    NO_DAY_DATE_FORMAT("yyyyMM"),
    /**
     * Represents the 'attrValue' constant value.
     */
    ATTRIBUTE("attrValue"),
    /**
     * Represents the 'derivedAttr' constant value.
     */
    DERIVE_ATTRIBUTE("deriveAttr"),
    /**
     * Represents the 'storkAttribute' constant value.
     */
    STORK_ATTRIBUTE("storkAttribute"),
    /**
     * Represents the 'properties' constant value.
     */
    PROPERTIES("properties"),
    /**
     * Represents the 'referer' constant value.
     */
    REFERER("referer"),
    /**
     * Represents the 'host' constant value.
     */
    HOST("host"),
    /**
     * Represents the 'spid' constant value.
     */
    SPID("spid"),
    /**
     * Represents the 'domain' constant value.
     */
    DOMAIN("domain"),
    /**
     * Represents the '.validation' constant value.
     */
    VALIDATION_SUFFIX(".validation"),
    /**
     * Represents the 'jsessionid' constant value.
     */
    EQUAL("="),
    /**
     * Represents the 'HttpOnly' constant value.
     */
    HTTP_ONLY("HttpOnly"),
    /**
     * Represents the 'SET-COOKIE' constant value.
     */
    JSSESSION("JSESSIONID"),
    /**
     * Represents the '=' constant value.
     */
    SETCOOKIE("SET-COOKIE"),
    /**
     * Represents the ';' constant value.
     */
    SEMICOLON(";"),
    /**
     * Represents the ' ' constant value.
     */
    SPACE(" "),
    /**
     * Represents the 'atp' constant value.
     */
    APROVIDER_PREFIX("atp"),
    /**
     * Represents the 'atn' constant value.
     */
    ANAME_PREFIX("atn"),
    /**
     * Represents the 'ap-cpeps' constant value.
     */
    AP_CPEPS_PREFIX("ap-cpeps"),
    /**
     * Represents the 'attr-filter' constant value.
     */
    AP_ATTRFILTER_PREFIX("attr-filter"),
    /**
     * Represents the 'save-session' constant value.
     */
    SAVED_SESSION("saved-session");

    /**
     * Represents the constant's value.
     */
    private String value;

    /**
     * Solo Constructor.
     *
     * @param val The Constant value.
     */
    PEPSValues(final String val) {

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

    /**
     * Construct the return value with the following structure
     * CONSTANT_VALUE+index+".id".
     *
     * @param index the number.
     *
     * @return The concatenated String value.
     */
    public String index(final int index) {

        return value + index + ".id";
    }

    /**
     * Construct the return value with the following structure
     * CONSTANT_VALUE+index+".value".
     *
     * @param index the number.
     *
     * @return The concatenated string value.
     */
    public String value(final int index) {

        return value + index + ".value";
    }

    /**
     * Construct the return value with the following structure
     * CONSTANT_VALUE+index+".name".
     *
     * @param index the number.
     *
     * @return The concatenated String value.
     */
    public String name(final int index) {

        return value + index + ".name";
    }

    /**
     * Construct the return value with the following structure
     * CONSTANT_VALUE+index+".url".
     *
     * @param index the number.
     *
     * @return The concatenated String value.
     */
    public String url(final int index) {

        return value + index + ".url";
    }

    /**
     * Construct the return value with the following structure
     * CONSTANT_VALUE+index+".allowedGroups".
     *
     * @param index the number.
     *
     * @return The concatenated String value.
     */
    public String allowedGroups(final int index) {

        return value + index + ".allowedGroups";
    }
}

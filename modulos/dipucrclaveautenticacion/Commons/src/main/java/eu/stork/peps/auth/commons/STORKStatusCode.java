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
 * This enum class contains the SAML Token Status Code.
 *
 * @author ricardo.ferreira@multicert.com, renato.portela@multicert.com,
 * luis.felix@multicert.com, hugo.magalhaes@multicert.com,
 * paulo.ribeiro@multicert.com
 * @version $Revision: 1.13 $, $Date: 2010-11-17 05:15:28 $
 */
public enum STORKStatusCode {

    /**
     * URI for Requester status code.
     */
    REQUESTER_URI("urn:oasis:names:tc:SAML:2.0:status:Requester"),
    /**
     * URI for Responder status code.
     */
    RESPONDER_URI("urn:oasis:names:tc:SAML:2.0:status:Responder"),
    /**
     * URI for Success status code.
     */
    SUCCESS_URI("urn:oasis:names:tc:SAML:2.0:status:Success"),
    /**
     * Attribute is Available.
     */
    STATUS_AVAILABLE("Available"),
    /**
     * Attribute is NotAvailable.
     */
    STATUS_NOT_AVAILABLE("NotAvailable"),
    /**
     * Attribute is Withheld.
     */
    STATUS_WITHHELD("Withheld");

    /**
     * Represents the constant's value.
     */
    private String value;

    /**
     * Solo Constructor.
     *
     * @param val The Constant value.
     */
    private STORKStatusCode(final String val) {

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

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
package eu.stork.peps.auth.commons.exceptions;

/**
 * Invalid Parameter Exception class.
 *
 * @author ricardo.ferreira@multicert.com, renato.portela@multicert.com,
 * luis.felix@multicert.com, hugo.magalhaes@multicert.com,
 * paulo.ribeiro@multicert.com
 * @version $Revision: 1.11 $, $Date: 2010-11-17 05:15:28 $
 *
 * @see InvalidParameterPEPSException
 */
public class InvalidParameterPEPSException extends AbstractPEPSException {

    /**
     * Unique identifier.
     */
    private static final long serialVersionUID = 2046282148740524875L;

    /**
     * Exception Constructor with two Strings representing the errorCode and
     * errorMessage as parameters.
     *
     * @param errorCode The error code value.
     * @param errorMessage The error code message value.
     */
    public InvalidParameterPEPSException(final String errorCode,
            final String errorMessage) {
        super(errorCode, errorMessage);
    }

    /**
     * Exception Constructor with one String representing the encoded samlToken.
     *
     * @param samlTokenFail The error SAML Token.
     */
    public InvalidParameterPEPSException(final String samlTokenFail) {
        super(samlTokenFail);
    }

}

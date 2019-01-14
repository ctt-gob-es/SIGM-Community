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

import java.io.Serializable;

/**
 * Abstract class to represent the various PEPS exceptions.
 *
 * @author ricardo.ferreira@multicert.com, renato.portela@multicert.com,
 * luis.felix@multicert.com, hugo.magalhaes@multicert.com,
 * paulo.ribeiro@multicert.com
 * @version $Revision: 1.13 $, $Date: 2010-11-17 05:15:28 $
 */
public abstract class AbstractPEPSException extends RuntimeException implements
        Serializable {

    /**
     * Unique identifier.
     */
    private static final long serialVersionUID = -1884417567740138022L;

    /**
     * Error code.
     */
    private String errorCode;

    /**
     * Error message.
     */
    private String errorMessage;

    /**
     * SAML token.
     */
    private String samlTokenFail;

    /**
     * Exception Constructor with two Strings representing the errorCode and
     * errorMessage as parameters.
     *
     * @param code The error code value.
     * @param message The error message value.
     */
    public AbstractPEPSException(final String code, final String message) {

        super(message);
        this.errorCode = code;
        this.errorMessage = message;
    }

    /**
     * Exception Constructor with the errorMessage as parameters and the
     * Throwable cause.
     *
     * @param message The error message value.
     * @param cause The throwable object.
     */
    public AbstractPEPSException(final String message, final Throwable cause) {

        super(message, cause);
        this.errorMessage = message;
    }

    /**
     * Exception Constructor with two Strings representing the errorCode and
     * errorMessage as parameters and the Throwable cause.
     *
     * @param code The error code value.
     * @param message The error message value.
     * @param cause The throwable object.
     */
    public AbstractPEPSException(final String code, final String message,
            final Throwable cause) {

        super(message, cause);
        this.errorCode = code;
        this.errorMessage = message;
    }

    /**
     * Exception Constructor with three Strings representing the errorCode,
     * errorMessage and encoded samlToken as parameters.
     *
     * @param code The error code value.
     * @param message The error message value.
     * @param samlToken The error SAML Token.
     */
    public AbstractPEPSException(final String code, final String message,
            final String samlToken) {

        super(message);
        this.errorCode = code;
        this.errorMessage = message;
        this.samlTokenFail = samlToken;
    }

    /**
     * Constructor with SAML Token as argument. Error message and error code are
     * embedded in the SAML.
     *
     * @param samlToken The error SAML Token.
     */
    public AbstractPEPSException(final String samlToken) {
        super();
        this.samlTokenFail = samlToken;
    }

    /**
     * Getter for errorCode.
     *
     * @return The errorCode value.
     */
    public final String getErrorCode() {
        return errorCode;
    }

    /**
     * Setter for errorCode.
     *
     * @param code The error code value.
     */
    public final void setErrorCode(final String code) {
        this.errorCode = code;
    }

    /**
     * Getter for errorMessage.
     *
     * @return The error Message value.
     */
    public final String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Setter for errorMessage.
     *
     * @param message The error message value.
     */
    public final void setErrorMessage(final String message) {
        this.errorMessage = message;
    }

    /**
     * Getter for SAMLTokenFail.
     *
     * @return The error SAML Token.
     */
    public final String getSamlTokenFail() {
        return samlTokenFail;
    }

    /**
     * Setter for SAMLTokenFail.
     *
     * @param samlToken The error SAML token.
     */
    public final void setSamlTokenFail(final String samlToken) {
        this.samlTokenFail = samlToken;
    }
}

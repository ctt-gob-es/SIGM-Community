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
 * This exception is thrown by the C-PEPS service and holds the relative
 * information to present to the citizen.
 *
 * @author ricardo.ferreira@multicert.com, renato.portela@multicert.com,
 * luis.felix@multicert.com, hugo.magalhaes@multicert.com,
 * paulo.ribeiro@multicert.com
 * @version $Revision: 1.9 $, $Date: 2010-11-17 05:15:28 $
 */
public final class CPEPSException extends RuntimeException {

    /**
     * Serial id.
     */
    private static final long serialVersionUID = -4012295047127999362L;

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
     * @param samlToken The SAML Token.
     * @param code The error code value.
     * @param message The error message value.
     */
    public CPEPSException(final String samlToken, final String code,
            final String message) {

        super(message);
        this.setErrorCode(code);
        this.setErrorMessage(message);
        this.setSamlTokenFail(samlToken);
    }

    /**
     * Exception Constructor with two Strings representing the errorCode and
     * errorMessage as parameters.
     *
     * @param samlToken The SAML Token.
     * @param code The error code value.
     * @param message The error message value.
     * @param cause The original exception;
     */
    public CPEPSException(final String samlToken, final String code,
            final String message, final Throwable cause) {

        super(message, cause);
        this.setErrorCode(code);
        this.setErrorMessage(message);
        this.setSamlTokenFail(samlToken);
    }

    /**
     * {@inheritDoc}
     */
    public String getMessage() {
        return this.getErrorMessage() + " (" + this.getErrorCode() + ")";
    }

    /**
     * Getter for the error code.
     *
     * @return The errorCode value.
     */
    public String getErrorCode() {
        return errorCode;
    }

    /**
     * Setter for the error code.
     *
     * @param code The error code.
     */
    public void setErrorCode(final String code) {
        this.errorCode = code;
    }

    /**
     * Getter for the error message.
     *
     * @return The errorMessage value.
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * Setter for the error message.
     *
     * @param message The error message.
     */
    public void setErrorMessage(final String message) {
        this.errorMessage = message;
    }

    /**
     * Getter for the samlTokenFail.
     *
     * @return The samlTokenFail value.
     */
    public String getSamlTokenFail() {
        return samlTokenFail;
    }

    /**
     * Setter for the samlTokenFail.
     *
     * @param samlToken The error Saml Token.
     */
    public void setSamlTokenFail(final String samlToken) {
        this.samlTokenFail = samlToken;
    }

}

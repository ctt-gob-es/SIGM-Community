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

package eu.stork.peps.exceptions;

/**
 * The Class STORKSAMLEngineException.
 * 
 * @author fjquevedo
 */
public class STORKSAMLEngineException extends Exception {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = -8319723167019122930L;

    /** The error code. */
    private String errorCode;

    /** The error detail. */
    private String errorDetail;

    /**
     * Instantiates a new sTORKSAML engine exception.
     * 
     * @param wrappedException the wrapped exception
     */
    public STORKSAMLEngineException(final Exception wrappedException) {
	super(wrappedException);
    }

    /**
     * Instantiates a new sTORKSAML engine exception.
     * 
     * @param errorMessage the error message
     */
    public STORKSAMLEngineException(final String errorMessage) {
	super(errorMessage);
    }

    /**
     * Instantiates a new sTORKSAML engine exception.
     * 
     * @param message the message
     * @param wrappedException the wrapped exception
     */
    public STORKSAMLEngineException(final String message,
	    final Exception wrappedException) {
	super(message, wrappedException);
    }

    /**
     * Instantiates a new sTORKSAML engine exception.
     * 
     * @param newErrorCode the error code
     * @param errorMessage the error message
     * @param newErrorDetail the error detail
     */
    public STORKSAMLEngineException(final String newErrorCode,
	    final String errorMessage, final String newErrorDetail) {
	super(errorMessage);
	this.errorCode = newErrorCode;
	this.errorDetail = newErrorDetail;
    }

    /**
     * Gets the error code.
     * 
     * @return the error code
     */
    public final String getErrorCode() {
	return this.errorCode;
    }

    /**
     * Gets the error detail.
     * 
     * @return the error detail
     */
    public final String getErrorDetail() {
	return errorDetail;
    }

    /**
     * Gets the error message.
     * 
     * @return the error message
     */
    public final String getErrorMessage() {
	return super.getMessage();
    }


    /**
     * Gets the message.
     * 
     * @return the message of the exception.
     * 
     * @see java.lang.Throwable#getMessage()
     */
    public final String getMessage() {
	return "Error (no. " + errorCode + ") processing request : "
		+ super.getMessage();
    }

    /**
     * Sets the error code.
     * 
     * @param newErrorCode the new error code
     */
    public final void setErrorCode(final String newErrorCode) {
        this.errorCode = newErrorCode;
    }

    /**
     * Sets the error detail.
     * 
     * @param newErrorDetail the new error detail
     */
    public final void setErrorDetail(final String newErrorDetail) {
        this.errorDetail = newErrorDetail;
    }

}

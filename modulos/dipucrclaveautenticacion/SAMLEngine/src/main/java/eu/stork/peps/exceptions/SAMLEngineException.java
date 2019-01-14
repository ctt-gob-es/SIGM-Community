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
 * The Class SAMLEngineException.
 * 
 * @author fjquevedo
 */
public class SAMLEngineException extends Exception {

    /** The Constant serialVersionUID. */
    private static final long serialVersionUID = 2611361164977849837L;

    /**
     * Instantiates a new SAMLEngine exception.
     * 
     * @param wrappedException the wrapped exception
     */
    public SAMLEngineException(final Exception wrappedException) {
	super(wrappedException);
    }

    /**
     * Instantiates a new SAMLEngine exception.
     * 
     * @param message the message
     */
    public SAMLEngineException(final String message) {
	super(message);
    }

    /**
     * Instantiates a new SAMLEngine exception.
     * 
     * @param message the message
     * @param wrappedException the wrapped exception
     */
    public SAMLEngineException(final String message,
	    final Exception wrappedException) {
	super(message, wrappedException);
    }

}

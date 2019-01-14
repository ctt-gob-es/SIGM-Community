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
 * Security PEPS Exception class.
 *
 * @author ricardo.ferreira@multicert.com, renato.portela@multicert.com,
 * luis.felix@multicert.com, hugo.magalhaes@multicert.com,
 * paulo.ribeiro@multicert.com
 * @version $Revision: 1.15 $, $Date: 2010-11-17 05:15:28 $
 *
 * @see AbstractPEPSException
 */
public final class StorkPEPSException extends AbstractPEPSException {

    /**
     * Unique identifier.
     */
    private static final long serialVersionUID = 8048033129798427574L;

    /**
     * Exception Constructor with two Strings representing the errorCode and
     * errorMessage as parameters.
     *
     * @param errorCode The error code value.
     * @param errorMsg The error message value.
     */
    public StorkPEPSException(final String errorCode, final String errorMsg) {
        super(errorCode, errorMsg);
    }

    /**
     * {@inheritDoc}
     */
    public String getMessage() {

        return "Security Error (" + this.getErrorCode() + ") processing request : "
                + this.getErrorMessage();
    }

}

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
 * Invalid session Exception class.
 *
 * @author ricardo.ferreira@multicert.com, renato.portela@multicert.com,
 * luis.felix@multicert.com, hugo.magalhaes@multicert.com,
 * paulo.ribeiro@multicert.com
 * @version $Revision: 1.14 $, $Date: 2010-11-17 05:15:28 $
 *
 * @see InvalidParameterPEPSException
 */
public class InvalidSessionPEPSException extends InvalidParameterPEPSException {

    /**
     * Unique identifier.
     */
    private static final long serialVersionUID = 7147090160978319016L;

    /**
     * Exception Constructor with two Strings representing the errorCode and
     * errorMessage as parameters.
     *
     * @param errorCode The error code value.
     * @param errorMessage The error message value.
     */
    public InvalidSessionPEPSException(final String errorCode,
            final String errorMessage) {

        super(errorCode, errorMessage);
    }

}

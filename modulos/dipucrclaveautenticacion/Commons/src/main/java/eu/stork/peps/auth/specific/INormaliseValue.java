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
package eu.stork.peps.auth.specific;

import eu.stork.peps.auth.commons.PersonalAttribute;

/**
 * Interface for attribute's value normalisation.
 *
 *
 * @author ricardo.ferreira@multicert.com, renato.portela@multicert.com,
 * luis.felix@multicert.com, hugo.magalhaes@multicert.com
 */
public interface INormaliseValue {

    /**
     * Translates the attribute's value from local format to STORK format.
     *
     * @param personalAttribute The Personal Attribute to normalise the value.
     *
     * @see PersonalAttribute
     */
    void normaliseAttributeValueToStork(PersonalAttribute personalAttribute);
}

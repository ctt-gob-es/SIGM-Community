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

import eu.stork.peps.auth.commons.IStorkSession;
import eu.stork.peps.auth.commons.PersonalAttribute;

/**
 * Interface that defines the methods to work with derivation of attributes.
 *
 * @author ricardo.ferreira@multicert.com, renato.portela@multicert.com,
 * luis.felix@multicert.com, hugo.magalhaes@multicert.com
 */
public interface IDeriveAttribute {

    /**
     * Derives the attribute value. Set the Personal Attribute value to null if
     * the value in session or the value of age are invalid (non-numeric or
     * null).
     *
     * @param personalAttrList The Personal Attribute List.
     * @param session The session object.
     *
     * @see PersonalAttribute The personal Attribute
     * @see IStorkSession The session object.
     */
    void deriveAttributeToData(PersonalAttribute personalAttrList,
            IStorkSession session);

}

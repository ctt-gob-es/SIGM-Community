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

import eu.stork.peps.auth.commons.IPersonalAttributeList;
import eu.stork.peps.auth.commons.IStorkSession;
import eu.stork.peps.auth.commons.STORKAuthnRequest;

/**
 * Interface for attributes normalization.
 *
 * @author ricardo.ferreira@multicert.com, renato.portela@multicert.com,
 * luis.felix@multicert.com, hugo.magalhaes@multicert.com
 */
public interface ITranslatorService {

    /**
     * Translates the attributes from local format to STORK format.
     *
     * @param personalList The Personal Attribute List.
     *
     * @return The Personal Attribute List with normalised attributes.
     *
     * @see IPersonalAttributeList
     */
    IPersonalAttributeList normaliseAttributeNamesToStork(
            IPersonalAttributeList personalList);

    /**
     * Translates the attributes values from local format to STORK format.
     *
     * @param personalList The Personal Attribute List.
     *
     * @return The PersonalAttributeList with normalised values.
     *
     * @see IPersonalAttributeList
     */
    IPersonalAttributeList normaliseAttributeValuesToStork(
            IPersonalAttributeList personalList);

    /**
     * Translates the attributes from STORK format to local format.
     *
     * @param personalList The Personal Attribute List.
     *
     * @return The PersonalAttributeList with normalised attributes.
     *
     * @see IPersonalAttributeList
     */
    IPersonalAttributeList normaliseAttributeNamesFromStork(
            IPersonalAttributeList personalList);

    /**
     * Derive Attribute Names To Stork format.
     *
     * @param personalList The Personal Attribute List,
     *
     * @return The PersonalAttributeList with derived attributes.
     *
     * @see IPersonalAttributeList
     */
    IPersonalAttributeList deriveAttributeFromStork(
            IPersonalAttributeList personalList);

    /**
     * Derive Attribute Names from Stork format.
     *
     * @param session The session object.
     * @param modifiedList The Personal Attribute List.
     *
     * @return The PersonalAttributeList with derived attributes.
     *
     * @see IStorkSession
     * @see IPersonalAttributeList
     */
    IPersonalAttributeList deriveAttributeToStork(IStorkSession session,
            IPersonalAttributeList modifiedList);

    /**
     * Validate the values of the attributes.
     *
     * @param pal The attribute list
     *
     * @return True, if all the attributes have values. False, otherwise.
     *
     * @see STORKAuthnRequest
     */
    boolean checkAttributeValues(IPersonalAttributeList pa);
}

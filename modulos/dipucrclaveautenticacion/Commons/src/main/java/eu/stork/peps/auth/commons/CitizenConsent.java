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
package eu.stork.peps.auth.commons;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a bean used to store the information relative to the Citizen
 * Consent.
 *
 * @author ricardo.ferreira@multicert.com, renato.portela@multicert.com,
 * luis.felix@multicert.com, hugo.magalhaes@multicert.com,
 * paulo.ribeiro@multicert.com
 * @version $Revision: 1.15 $, $Date: 2010-11-17 05:15:28 $
 */
public final class CitizenConsent {

    /**
     * Mandatory attributes list.
     */
    private List<String> mandatoryList;

    /**
     * Optional attributes list.
     */
    private List<String> optionalList;

    /**
     * Citizen Consent default Constructor.
     */
    public CitizenConsent() {
        this.mandatoryList = new ArrayList<String>();
        this.optionalList = new ArrayList<String>();
    }

    /**
     * Getter for the mandatoryList value.
     *
     * @return The mandatoryList value.
     */
    public List<String> getMandatoryList() {
        return this.mandatoryList;
    }

    /**
     * Setter for the mandatoryList value.
     *
     * @param mandatoryAttrList Mandatory parameters list.
     */
    public void setMandatoryList(final List<String> mandatoryAttrList) {
        this.mandatoryList = mandatoryAttrList;
    }

    /**
     * Setter for some mandatoryAttribute. Adds the input parameter to the
     * mandatoryList.
     *
     * @param mandatoryAttr Attribute to add to the mandatoryList.
     */
    public void setMandatoryAttribute(final String mandatoryAttr) {
        this.mandatoryList.add(mandatoryAttr);
    }

    /**
     * Getter for the optionalList value.
     *
     * @return The optionalList value.
     */
    public List<String> getOptionalList() {
        return optionalList;
    }

    /**
     * Setter for the optionalList value.
     *
     * @param optAttrList Optional parameters list.
     */
    public void setOptionalList(final List<String> optAttrList) {
        this.optionalList = optAttrList;
    }

    /**
     * Setter for some optionalAttr. Adds the input parameter to the
     * optionalList.
     *
     * @param optionalAttr Attribute to add to the optionalList.
     */
    public void setOptionalAttribute(final String optionalAttr) {
        this.optionalList.add(optionalAttr);
    }

    /**
     * Returns a string in the following format. "Mandatory attributes:
     * mandatoryAttr1;mandatoryAttr2;mandatoryAttrN Optional attributes:
     * optionalAttr1;optionalAttr2;optionalAttrN"
     *
     * @return {@inheritDoc}
     */
    public String toString() {
        final StringBuilder strbldr = new StringBuilder(46);
        strbldr.append("Mandatory attributes: ");
        for (final String str : mandatoryList) {
            strbldr.append(str).append(';');
        }
        strbldr.append(" Optional attributes: ");
        for (final String str : optionalList) {
            strbldr.append(str).append(';');
        }
        return strbldr.toString();
    }

}

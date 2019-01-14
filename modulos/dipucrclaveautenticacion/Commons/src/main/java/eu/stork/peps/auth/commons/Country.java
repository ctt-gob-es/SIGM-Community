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

import java.io.Serializable;

/**
 * This class is a bean used to store the information relative to the Country.
 *
 * @author ricardo.ferreira@multicert.com, renato.portela@multicert.com,
 * luis.felix@multicert.com, hugo.magalhaes@multicert.com,
 * paulo.ribeiro@multicert.com
 * @version $Revision: 1.10 $, $Date: 2010-11-17 05:15:28 $
 */
public final class Country implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1135994036496370993L;

    /**
     * Country Id.
     */
    private String countryId;

    /**
     * Country Name.
     */
    private String countryName;

    /**
     * Country Constructor.
     *
     * @param cId Id of the Country.
     * @param cName Name of the Country.
     */
    public Country(final String cId, final String cName) {

        this.countryId = cId;
        this.countryName = cName;
    }

    /**
     * Getter for the countryId value.
     *
     * @return The countryId value.
     */
    public String getCountryId() {

        return countryId;
    }

    /**
     * Setter for the countryId value.
     *
     * @param cId Id of the Country.
     */
    public void setCountryId(final String cId) {

        this.countryId = cId;
    }

    /**
     * Getter for the countryName value.
     *
     * @return The countryName value.
     */
    public String getCountryName() {

        return countryName;
    }

    /**
     * Setter for the countryName value.
     *
     * @param name Name of the Country.
     */
    public void setCountryName(final String name) {

        this.countryName = name;
    }

}

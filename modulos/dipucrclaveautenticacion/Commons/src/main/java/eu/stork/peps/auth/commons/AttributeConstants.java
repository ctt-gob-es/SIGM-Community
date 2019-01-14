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

/**
 * This enum class contains all the STORK PEPS, Commons and Specific errors
 * constant identifiers.
 *
 * @author ricardo.ferreira@multicert.com, renato.portela@multicert.com,
 * luis.felix@multicert.com, hugo.magalhaes@multicert.com,
 * paulo.ribeiro@multicert.com
 * @version $Revision: 1.2 $, $Date: 2010-11-17 05:15:28 $
 */
public enum AttributeConstants {

    /**
     * Represents the attribute's name index.
     */
    ATTR_NAME_INDEX(0),
    /**
     * Represents the attribute's type index.
     */
    ATTR_TYPE_INDEX(1),
    /**
     * Represents the attribute's value index.
     */
    ATTR_VALUE_INDEX(2),
    /**
     * Represents the attribute's status index.
     */
    ATTR_STATUS_INDEX(3),
    /**
     * Represents the number of allowed tuples.
     */
    NUMBER_TUPLES(4);

    /**
     * Represents the constant's value.
     */
    private int attribute;

    /**
     * Solo Constructor.
     *
     * @param attr The Attribute Constant value.
     */
    AttributeConstants(final int attr) {

        this.attribute = attr;
    }

    /**
     * Return the Constant Value.
     *
     * @return The constant value.
     */
    public int intValue() {

        return attribute;
    }
}

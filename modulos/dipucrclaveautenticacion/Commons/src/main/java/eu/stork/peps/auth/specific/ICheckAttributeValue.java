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

import java.util.List;

/**
 * Interface that defines the methods to work with the validation of attributes.
 *
 * @author ricardo.ferreira@multicert.com, renato.portela@multicert.com,
 * luis.felix@multicert.com, hugo.magalhaes@multicert.com
 */
public interface ICheckAttributeValue {

    /**
     * Checks if the list of values contains the expected value.
     *
     * @param values The List of values.
     * @param expectedValue The value to check if it exists on the list.
     *
     * @return boolean true, if the value is present in the list. False,
     * otherwise.
     */
    boolean checkValue(List<String> values, String expectedValue);

}

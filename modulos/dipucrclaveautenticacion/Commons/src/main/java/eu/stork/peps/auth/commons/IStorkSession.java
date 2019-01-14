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
 * Interface for stork session.
 *
 * @author ricardo.ferreira@multicert.com, renato.portela@multicert.com,
 * luis.felix@multicert.com, hugo.magalhaes@multicert.com,
 * paulo.ribeiro@multicert.com
 * @version $Revision: 1.9 $, $Date: 2010-11-17 05:15:28 $
 */
public interface IStorkSession {

    /**
     * Returns the object associated the the given key.
     *
     * @param key with which the specified value is to be associated.
     *
     * @return The object associated the the given key.
     */
    Object get(Object key);

    /**
     * Associates a key to a value, and inserts them in the session object.
     *
     * @param key with which the specified value is to be associated.
     * @param value to be associated with the specified key.
     *
     * @return previous value associated with specified key, or null if there
     * was no mapping for key. A null return can also indicate that the map
     * previously associated null with the specified key.
     */
    Object put(String key, Object value);

    /**
     * Removes the mapping for this key.
     *
     * @param key with which the specified value is to be associated.
     *
     * @return previous value associated with specified key, or null if there
     * was no mapping for key. A null return can also indicate that the map
     * previously associated null with the specified key.
     */
    Object remove(Object key);

    /**
     * Returns the number of key-value mappings in this map.
     *
     * @return the number of key-value mappings in this map.
     */
    int size();

    /**
     * Returns true if this map contains a mapping for the specified key.
     *
     * @param key with which the specified value is to be associated.
     *
     * @return true if this map contains a mapping for the specified key.
     */
    boolean containsKey(Object key);

    /**
     * Removes all mappings from this map.
     */
    void clear();

    /**
     * Returns true if this map contains no key-value mappings.
     *
     * @return true if this map contains no key-value mappings.
     */
    boolean isEmpty();
}

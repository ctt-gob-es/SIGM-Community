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

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * Interface for {@link PersonalAttributeList}.
 *
 * @author ricardo.ferreira@multicert.com, renato.portela@multicert.com,
 * luis.felix@multicert.com, hugo.magalhaes@multicert.com,
 * paulo.ribeiro@multicert.com
 * @version $Revision: 1.16 $, $Date: 2010-11-17 05:15:28 $
 *
 * @see PersonalAttribute
 */
@SuppressWarnings("PMD.CloneMethodMustImplementCloneable")
public interface IPersonalAttributeList extends Iterable<PersonalAttribute>,
        Cloneable {

    /**
     * Associates the specified value with the specified key in this Personal
     * Attribute List.
     *
     * @param key with which the specified value is to be associated.
     * @param value to be associated with the specified key.
     *
     * @return the previous value associated with key, or null if there was no
     * mapping for key.
     *
     * @see PersonalAttribute
     */
    PersonalAttribute put(String key, PersonalAttribute value);

    /**
     * Replaces the specified value with the specified key in this Personal
     * Attribute List.
     *
     * @param key with which the specified value is to be replaced.
     * @param value to be associated with the specified key.
     *
     * @return the previous value associated with key, or null if there was no
     * mapping for key.
     *
     * @see PersonalAttribute
     */
    PersonalAttribute replace(String key, PersonalAttribute value);

    /**
     * Returns the value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     *
     * @param key whose associated value is to be returned.
     *
     * @return The value to which the specified key is mapped, or null if this
     * map contains no mapping for the key.
     *
     * @see PersonalAttribute
     */
    PersonalAttribute get(Object key);

    /**
     * Adds to the PersonalAttributeList the given PersonalAttribute. It sets
     * the attribute name as the key to the attribute value.
     *
     * @param value PersonalAttribute to add to the PersonalAttributeList
     */
    void add(PersonalAttribute value);

    /**
     * Get the size of the Personal Attribute List.
     *
     * @return size of the Personal Attribute List.
     */
    int size();

    /**
     * Checks if the Personal Attribute List contains the given key.
     *
     * @param key with which the specified value is to be associated.
     *
     * @return true if the Personal Attribute List contains the given key, false
     * otherwise.
     */
    boolean containsKey(Object key);

    /**
     * Getter for the iterator of the Personal Attribute List values.
     *
     * @return The iterator for the Personal Attribute List values.
     *
     * @see PersonalAttribute
     */
    Iterator<PersonalAttribute> iterator();

    /**
     * Creates a Personal Attribute List from a String representing an Attribute
     * List.
     *
     * @param attrList String Object representing the attribute list.
     */
    void populate(String attrList);

    /**
     * Removes the mapping for this key from this map if present.
     *
     * @param key key whose mapping is to be removed from the map.
     * @return previous value associated with specified key, or <tt>null</tt> if
     * there was no mapping for key. A <tt>null</tt> return can also indicate
     * that the map previously associated <tt>null</tt> with the specified key.
     */
    PersonalAttribute remove(Object key);

    /**
     * Returns a collection view of the values contained in this map. The
     * collection is backed by the map, so changes to the map are reflected in
     * the collection, and vice-versa. The collection supports element removal,
     * which removes the corresponding mapping from this map, via the
     * <tt>Iterator.remove</tt>, <tt>Collection.remove</tt>, <tt>removeAll</tt>,
     * <tt>retainAll</tt>, and <tt>clear</tt> operations. It does not support
     * the
     * <tt>add</tt> or <tt>addAll</tt> operations.
     *
     * @return a collection view of the values contained in this map.
     */
    Collection<PersonalAttribute> values();

    /**
     * Returns a {@link Set} view of the keys contained in this map. The set is
     * backed by the map, so changes to the map are reflected in the set, and
     * vice-versa. If the map is modified while an iteration over the set is in
     * progress (except through the iterator's own <tt>remove</tt> operation),
     * the results of the iteration are undefined. The set supports element
     * removal, which removes the corresponding mapping from the map, via the
     * <tt>Iterator.remove</tt>, <tt>Set.remove</tt>,
     * <tt>removeAll</tt>, <tt>retainAll</tt>, and <tt>clear</tt>
     * operations. It does not support the <tt>add</tt> or <tt>addAll</tt>
     * operations.
     *
     * @return a set view of the keys contained in this map
     */
    Set<String> keySet();

    /**
     * Returns a IPersonalAttributeList of the complex attributes.
     *
     * @return an IPersonalAttributeList of the complex attributes.
     */
    IPersonalAttributeList getComplexAttributes();

    /**
     * Returns a IPersonalAttributeList of the simple value attributes.
     *
     * @return an IPersonalAttributeList of the simple value attributes.
     */
    IPersonalAttributeList getSimpleValueAttributes();

    /**
     * Returns a IPersonalAttributeList of the mandatory attributes in this map.
     *
     * @return an IPersonalAttributeList of the mandatory attributes contained
     * in this map.
     */
    IPersonalAttributeList getMandatoryAttributes();

    /**
     * Returns a IPersonalAttributeList merged with provided one.
     *
     * @return an IPersonalAttributeList the attribute list to merge with.
     */
    IPersonalAttributeList merge(IPersonalAttributeList attrList);

    /**
     * Returns a IPersonalAttributeList of the optional attributes in this map.
     *
     * @return an IPersonalAttributeList of the optional attributes contained in
     * this map.
     */
    IPersonalAttributeList getOptionalAttributes();

    /**
     * Returns <tt>true</tt> if this map contains no key-value mappings.
     *
     * @return <tt>true</tt> if this map contains no key-value mappings.
     */
    boolean isEmpty();

    /**
     * Returns <tt>true</tt> if this map contains at least one element that
     * doesn't have value.
     *
     * @return <tt>true</tt> if this map contains at least one element that
     * doesn't have value.
     */
    boolean hasMissingValues();

    /**
     * Returns a copy of this <tt>IPersonalAttributeList</tt> instance.
     *
     * @return The copy of this IPersonalAttributeList.
     */
    Object clone();

}

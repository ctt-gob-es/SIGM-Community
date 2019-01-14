package eu.stork.peps.auth.commons;

import java.util.Iterator;

/**
 * Interface for Attributes Providers map.
 *
 * @author Stelios Lelis (stelios.lelis@aegean.gr), Elias Pastos
 * (ilias@aegean.gr)
 *
 * @version $Revision: 1.00 $, $Date: 2013-09-20 $
 */
public interface IAttributeProvidersMap {

    /**
     * Returns the object associated the the given key.
     *
     * @param key with which the specified value is to be associated.
     *
     * @return The object associated the the given key.
     */
    IPersonalAttributeList get(AttributeSource key);

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
    Object put(AttributeSource key, IPersonalAttributeList value);

    /**
     * Removes the mapping for this key.
     *
     * @param key with which the specified value is to be associated.
     *
     * @return previous value associated with specified key, or null if there
     * was no mapping for key. A null return can also indicate that the map
     * previously associated null with the specified key.
     */
    IPersonalAttributeList remove(AttributeSource key);

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
    boolean containsKey(AttributeSource key);

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

    /**
     * Returns an Iterator of the keys contained in this map. The implementation
     * must take care in order for the Iterator to have predictable order of the
     * returned keys.
     *
     * @return an iterator of the keys contained in this map
     */
    Iterator<AttributeSource> keyIterator();

    /**
     * Merges this Attribute Providers Map with another providers map changes
     * the contents of this map so it returns null
     *
     * @param aPMap
     */
    void mergeWith(IAttributeProvidersMap aPMap);
}

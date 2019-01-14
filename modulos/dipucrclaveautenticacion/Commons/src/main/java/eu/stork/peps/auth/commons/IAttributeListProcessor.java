package eu.stork.peps.auth.commons;

import java.util.List;
import java.util.Map;

/**
 * Interface for {@link AttributeListProcessor}.
 *
 * @author ricardo.ferreira@multicert.com
 *
 * @version $Revision: $, $Date: $
 *
 * @see IPersonalAttributeList
 */
public interface IAttributeListProcessor {

    /**
     * Checks if attribute list only contains allowed attributes.
     *
     * @param attrList the requested attribute list
     * @param attributes the allowed attributes
     *
     * @return true is all the attributes are allowed.
     *
     * @see IPersonalAttributeList
     */
    boolean hasAllowedAttributes(final IPersonalAttributeList attrList, final List<String> attributes);

    /**
     * Lookup for business attribute.
     *
     * @param attrList the requested attribute list
     * @param normalAttributes the normal attributes
     *
     * @return true is at least one business attribute was requested.
     *
     * @see IPersonalAttributeList
     */
    boolean hasBusinessAttributes(final IPersonalAttributeList attrList, final List<String> normalAttributes);

    /**
     * Lookup for business attribute in normal attribute list (loaded by
     * implementation).
     *
     * @param attrList the requested attribute list
     *
     * @return true is at least one business attribute was requested.
     *
     * @see IPersonalAttributeList
     */
    boolean hasBusinessAttributes(final IPersonalAttributeList attrList);

    /**
     * Adds eIdentifier, name, surname, and DateOfBirth attributes to get
     * business attributes from some AP.
     *
     * @param attrList the requested attribute list
     * @param attributes the list of attributes to add (eIdentifier, name,
     * surname, and DateOfBirth).
     *
     * @return the requested attribute list and the new attributes added
     * (eIdentifier, name, surname, and DateOfBirth).
     *
     * @see IPersonalAttributeList
     */
    IPersonalAttributeList addAPMandatoryAttributes(final IPersonalAttributeList attrList, final List<String> attributes);

    /**
     * Adds eIdentifier, name, surname, and DateOfBirth attributes, loaded by
     * implementation, to get business attributes from some AP.
     *
     * @param attrList the requested attribute list
     *
     * @return the requested attribute list and the new attributes added
     * (eIdentifier, name, surname, and DateOfBirth).
     *
     * @see IPersonalAttributeList
     */
    IPersonalAttributeList addAPMandatoryAttributes(final IPersonalAttributeList attrList);

    /**
     * Removes from attribute list the given list of attributes.
     *
     * @param attrList the requested attribute list
     * @param attributes the list of attributes to remove.
     *
     * @return the requested attribute list and the attributes removed.
     *
     * @see IPersonalAttributeList
     */
    IPersonalAttributeList removeAPMandatoryAttributes(final IPersonalAttributeList attrList, final List<String> attributes);

    /**
     * Removes from attribute list the given list of attributes and change
     * attributes status if attribute was optional in the request.
     *
     * @param attrList the requested attribute list
     * @param attributes the map of attributes (attribute name,
     * mandatory/optional) to remove.
     *
     * @return the requested attribute list and the attributes removed
     *
     * @see IPersonalAttributeList
     */
    IPersonalAttributeList removeAPMandatoryAttributes(IPersonalAttributeList attrList, Map<String, Boolean> attributes);

    /**
     * Removes from attribute list the Stork list of attributes.
     *
     * @param attrList the requested attribute list
     *
     * @return the attribute list without rejected attributes.
     *
     * @see IPersonalAttributeList
     */
    IPersonalAttributeList removeAPRejectedAttributes(IPersonalAttributeList attrList);

    /**
     * Checks if mandate attribute exist in the requested Attribute List. Power
     * attribute name to lookup is loaded by implementation.
     *
     * @param attrList the requested attribute list.
     *
     * @return true if mandate attribute exists or false otherwise.
     *
     * @see IPersonalAttributeList
     */
    boolean hasPowerAttribute(final IPersonalAttributeList attrList);

    /**
     * Checks if attribute name was requested and has value.
     *
     * @param attrList the requested attribute list.
     * @param attrName the attribute name to lookup for .
     *
     * @return true if attribute was requested and has value or false otherwise.
     *
     * @see IPersonalAttributeList
     */
    boolean hasAttributeValue(final IPersonalAttributeList attrList, final String attrName);

    /**
     * Checks if attribute has value.
     *
     * @param attr the attribute to check.
     *
     * @return true if has value;
     *
     * @see PersonalAttribute
     */
    boolean hasAttributeValue(final PersonalAttribute attr);

    /**
     * Gets a map (attribute name, attribute isRequired) of attributes added to
     * attribute list.
     *
     * @return the Map of attributes added and if is required to attribute list.
     */
    Map<String, Boolean> getNormalAttributesAdded();

    /**
     * Updates list by filtering any attribute that must be requested instead of
     * using a value obtained from cache (business and legal attrs)
     *
     * @param attrList the list which will be updated
     * @return the filtered list
     */
    IPersonalAttributeList filterAttrList(IPersonalAttributeList attrList);

    /**
     * Updates the list of cached attrs by inserting the business and/or legal
     * attrs requested by the user
     *
     * @param cachedAttrList
     * @param requestedAttrsList
     */
    void updateAttrList(IPersonalAttributeList cachedAttrList, IPersonalAttributeList requestedAttrsList);

    /**
     * Verifies if normal attribute list contains any attribute that we must
     * always request (usually business attributes)
     */
    boolean hasAlwaysRequestAttributes(IPersonalAttributeList attributeList);

}

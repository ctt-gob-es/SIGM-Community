package eu.stork.peps.auth.commons;

import java.io.Serializable;
import java.util.List;

/**
 * This class is a bean used to store the information relative to the Attribute
 * Provider.
 *
 * @author Stelios Lelis (stelios.lelis@aegean.gr), Elias Pastos
 * (ilias@aegean.gr)
 *
 * @version $Revision: 1.01 $, $Date: 2014-01-13 $
 */
public final class AttributeProvider implements Serializable {

    /**
     * Unique identifier.
     */
    private static final long serialVersionUID = 7210186241917444559L;

    /**
     * Provider Id.
     */
    private String providerId;

    /**
     * Provider Name.
     */
    private String providerName;

    /**
     * Allowed groups of attribute names.
     */
    private List<String> allowedGroups;

    /**
     * Attribute Provider Constructor.
     *
     * @param pId Id of the Attribute Provider.
     * @param pName Name of the Attribute Provider.
     */
    public AttributeProvider(final String pId, final String pName, final List<String> pAllowedGroups) {

        this.providerId = pId;
        this.providerName = pName;
        this.allowedGroups = pAllowedGroups;
    }

    /**
     * Getter for the providerId value.
     *
     * @return The providerId value.
     */
    public String getProviderId() {

        return providerId;
    }

    /**
     * Setter for the providerId value.
     *
     * @param pId Id of the Attribute Provider.
     */
    public void setProviderId(final String pId) {

        this.providerId = pId;
    }

    /**
     * Getter for the providerName value.
     *
     * @return The providerName value.
     */
    public String getProviderName() {

        return providerName;
    }

    /**
     * Setter for the providerName value.
     *
     * @param name Name of the Attribute Provider.
     */
    public void setProviderName(final String name) {

        this.providerName = name;
    }

    /**
     * Getter for the allowedGroups value.
     *
     * @return The allowedGroups value.
     */
    public List<String> getAllowedGroups() {

        return allowedGroups;
    }

    /**
     * Setter for the allowedGroups value.
     *
     * @param name AllowedGroups of the Attribute Provider.
     */
    public void setAllowedGroups(final List<String> pAllowedGroups) {

        this.allowedGroups = pAllowedGroups;
    }

    /**
     * Check if an attribute name is allowed for this Attribute Provider
     *
     * @param pAttrName the Attribute Name to check
     * @return true if the name is allowed, false otherwise
     */
    public boolean isAttributeNameAllowed(final String pAttrName) {
        if (allowedGroups == null || pAttrName == null) {
            return false;
        } else if (allowedGroups.contains(PEPSParameters.ATTRIBUTE_GROUPS_ALL.toString())) {
            return true;
        } else if (allowedGroups.contains(PEPSParameters.ATTRIBUTE_GROUPS_NONE.toString())) {
            return false;
        } else {
            return allowedGroups.contains(pAttrName);
        }
    }
}

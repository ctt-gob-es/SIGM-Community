package eu.stork.peps.auth.commons;

import java.io.Serializable;

/**
 * This class is a bean used to store information relative to Attribute Names.
 *
 * @author Stelios Lelis (stelios.lelis@aegean.gr), Elias Pastos
 * (ilias@aegean.gr)
 *
 * @version $Revision: 1.00 $, $Date: 2013-11-26 $
 */
public final class AttributeName implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = -3537736618869722308L;

    /**
     * Attribute Id.
     */
    private String attributeId;

    /**
     * Attribute Name.
     */
    private String attributeName;

    /**
     * Attribute Name Constructor.
     *
     * @param aId Id of the Attribute Name.
     * @param aName Name of the Attribute Name.
     */
    public AttributeName(final String aId, final String aName) {

        this.attributeId = aId;
        this.attributeName = aName;
    }

    /**
     * Getter for the attributeId value.
     *
     * @return The attributeId value.
     */
    public String getAttributeId() {

        return attributeId;
    }

    /**
     * Setter for the attributeId value.
     *
     * @param aId Id of the Attribute Name.
     */
    public void setAttributeId(final String aId) {

        this.attributeId = aId;
    }

    /**
     * Getter for the attributeName value.
     *
     * @return The attributeName value.
     */
    public String getAttributeName() {

        return attributeName;
    }

    /**
     * Setter for the attributeName value.
     *
     * @param name Name of the Attribute Name.
     */
    public void setAttributeName(final String name) {

        this.attributeName = name;
    }

}

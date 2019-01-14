package eu.stork.peps.auth.commons;

import java.io.Serializable;

import org.apache.log4j.Logger;

/**
 * This class is a bean used to store the information relative to the Attribute
 * Source (either AttributeProvider or Country).
 *
 * @author Stelios Lelis (stelios.lelis@aegean.gr), Elias Pastos
 * (ilias@aegean.gr)
 *
 * @version $Revision: 1.10 $, $Date: 2013-11-29 $
 */
public final class AttributeSource implements Serializable {

    /**
     * Unique identifier.
     */
    private static final long serialVersionUID = 432243595968469014L;

    public static final int SOURCE_LOCAL_APROVIDER = 1;
    public static final int SOURCE_REMOTE_COUNTRY = 2;

    /**
     * Logger object.
     */
    private static final Logger LOG = Logger.getLogger(AttributeSource.class.getName());

    /**
     * Provider source.
     */
    private int sourceType;

    /**
     * Provider URL.
     */
    private String providerURL;

    /**
     * The local Attribute Provider.
     */
    private AttributeProvider provider;

    /**
     * The remote Country.
     */
    private Country country;

    /**
     * Attribute Source Constructor.
     *
     * @param provider The local Attribute Provider.
     * @param pURL URL of the Attribute Provider.
     */
    public AttributeSource(final AttributeProvider provider, final String pURL) {
        this.setSourceType(SOURCE_LOCAL_APROVIDER);

        this.setProvider(provider);
        this.setProviderURL(pURL);
    }

    /**
     * Attribute Source Constructor.
     *
     * @param country The remote Country.
     * @param pURL URL of the Country.
     */
    public AttributeSource(final Country country, final String pURL) {
        this.setSourceType(SOURCE_REMOTE_COUNTRY);

        this.setCountry(country);
        this.setProviderURL(pURL);
    }

    /**
     * @param sourceType the sourceType to set
     */
    public void setSourceType(final int sourceType) {
        this.sourceType = sourceType;
    }

    /**
     * @return the sourceType
     */
    public int getSourceType() {
        return sourceType;
    }

    /**
     * @param providerURL the providerURL to set
     */
    public void setProviderURL(final String providerURL) {
        this.providerURL = providerURL;
    }

    /**
     * @return the providerURL
     */
    public String getProviderURL() {
        return providerURL;
    }

    /**
     * @param provider the provider to set
     */
    public void setProvider(final AttributeProvider provider) {
        this.setSourceType(SOURCE_LOCAL_APROVIDER);

        this.provider = provider;
    }

    /**
     * @return the provider
     */
    public AttributeProvider getProvider() {
        return provider;
    }

    /**
     * @param country the country to set
     */
    public void setCountry(final Country country) {
        this.setSourceType(SOURCE_REMOTE_COUNTRY);

        this.country = country;
    }

    /**
     * @return the country
     */
    public Country getCountry() {
        return country;
    }

    /**
     * {@inheritDoc}
     */
    public boolean equals(Object obj) {
        boolean outcome = false;

        LOG.debug("Calling equals with Object.");
        if (obj instanceof AttributeSource) {
            LOG.debug("Calling equals with AttributeSource.");
            outcome = this.innerEquals((AttributeSource) obj);
        }

        LOG.debug("Object equals outcome: " + outcome);
        return outcome;
    }

    /**
     * Compare the given AttributeSource with the current object in order to
     * determinine if they are equal.
     *
     * @param obj The AttributeSource to compare to
     *
     * @return true if the two objects are equal
     */
    public boolean innerEquals(AttributeSource obj) {
        boolean outcome = false;

        if (this.sourceType == obj.getSourceType()) {
            if (this.sourceType == AttributeSource.SOURCE_LOCAL_APROVIDER) {
                if (this.provider.getProviderId().equals(obj.getProvider().getProviderId())) {
                    outcome = true;
                }
            } else if (this.sourceType == AttributeSource.SOURCE_REMOTE_COUNTRY) {
                if (this.country.getCountryId().equals(obj.getCountry().getCountryId())) {
                    outcome = true;
                }
            }
        }

        LOG.debug("AttributeSource equals outcome: " + outcome);
        return outcome;
    }

    /**
     * {@inheritDoc}
     */
    public int hashCode() {
        int hash = 1;
        hash = hash * 17 + this.sourceType;
        if (this.sourceType == AttributeSource.SOURCE_LOCAL_APROVIDER) {
            hash = hash * 31 + this.provider.getProviderName().hashCode();
        } else {
            hash = hash * 31 + this.country.getCountryName().hashCode();
        }
        return hash;
    }
}

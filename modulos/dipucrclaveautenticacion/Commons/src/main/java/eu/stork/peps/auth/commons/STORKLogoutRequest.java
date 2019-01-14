package eu.stork.peps.auth.commons;

import java.io.Serializable;

public class STORKLogoutRequest implements Serializable, Cloneable {

    private static final long serialVersionUID = 4778480781609392750L;

    /**
     * The samlId.
     */
    private String samlId;

    /**
     * The destination.
     */
    private String destination;

    /**
     * The distinguished name.
     */
    private String distinguishedName;

    /**
     * The qaa.
     */
    private int qaa;

    /**
     * The token saml.
     */
    private byte[] tokenSaml = new byte[0];

    /**
     * The issuer.
     */
    private String issuer;

    /**
     * The country.
     */
    private String country;

    /**
     * The Alias used at the keystore for saving this certificate.
     */
    private String alias;

    /**
     * The ID of principal as known to SP *
     */
    private String spProvidedId;

    /**
     * Gets the SP's Certificate Alias.
     *
     * @return alias The SP's Certificate Alias.
     */
    public String getAlias() {
        return alias;
    }

    /**
     * Sets the SP's Certificate Alias.
     *
     * @param nAlias The SP's Certificate Alias.
     */
    public void setAlias(final String nAlias) {
        this.alias = nAlias;
    }

    /**
     * Gets the issuer.
     *
     * @return The issuer value.
     */
    public String getIssuer() {
        return issuer;
    }

    /**
     * Sets the issuer.
     *
     * @param samlIssuer the new issuer value.
     */
    public void setIssuer(final String samlIssuer) {
        this.issuer = samlIssuer;
    }

    /**
     * Gets the SAML Token.
     *
     * @return The SAML Token value.
     */
    public byte[] getTokenSaml() {
        return tokenSaml.clone();
    }

    /**
     * Sets the SAML Token.
     *
     * @param samlToken The new SAML Token value.
     */
    public void setTokenSaml(final byte[] samlToken) {
        if (samlToken != null) {
            this.tokenSaml = samlToken.clone();
        }
    }

    /**
     * Gets the country.
     *
     * @return The country value.
     */
    public String getCountry() {
        return country;
    }

    /**
     * Sets the country.
     *
     * @param nCountry the new country value.
     */
    public void setCountry(final String nCountry) {
        this.country = nCountry;
    }

    /**
     * Getter for the qaa value.
     *
     * @return The qaa value value.
     */
    public int getQaa() {
        return qaa;
    }

    /**
     * Setter for the qaa value.
     *
     * @param qaaLevel The new qaa value.
     */
    public void setQaa(final int qaaLevel) {
        this.qaa = qaaLevel;
    }

    /**
     * Getter for the destination value.
     *
     * @return The destination value.
     */
    public String getDestination() {
        return destination;
    }

    /**
     * Setter for the destination value.
     *
     * @param detination the new destination value.
     */
    public void setDestination(final String detination) {
        this.destination = detination;
    }

    /**
     * Getter for the samlId value.
     *
     * @return The samlId value.
     */
    public String getSamlId() {
        return samlId;
    }

    /**
     * Setter for the samlId value.
     *
     * @param newSamlId the new samlId value.
     */
    public void setSamlId(final String newSamlId) {
        this.samlId = newSamlId;
    }

    /**
     * Getter for the distinguishedName value.
     *
     * @return The distinguishedName value.
     */
    public String getDistinguishedName() {
        return distinguishedName;
    }

    /**
     * Setter for the distinguishedName value.
     *
     * @param certDN the distinguished name value.
     */
    public void setDistinguishedName(final String certDN) {
        this.distinguishedName = certDN;
    }

    /**
     * Getter for spProvidedId *
     */
    public String getSpProvidedId() {
        return spProvidedId;
    }

    public void setSpProvidedId(final String nSpProvidedId) {
        this.spProvidedId = nSpProvidedId;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        STORKLogoutRequest storkLogoutRequest = null;
        storkLogoutRequest = (STORKLogoutRequest) super.clone();
        storkLogoutRequest.setTokenSaml(getTokenSaml());
        return storkLogoutRequest;
    }
}

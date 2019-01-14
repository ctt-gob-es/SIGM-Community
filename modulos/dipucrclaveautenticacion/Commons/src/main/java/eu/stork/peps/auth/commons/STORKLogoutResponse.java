package eu.stork.peps.auth.commons;

import java.io.Serializable;

public class STORKLogoutResponse implements Serializable, Cloneable {

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
     * Status code.
     */
    private String statusCode;

    /**
     * Secondary status code.
     */
    private String subStatusCode;

    /**
     * Status message.
     */
    private String statusMessage;

    /**
     * Logout failed?
     */
    private boolean fail;

    /**
     * The inResponseTo
     */
    private String inResponseTo;

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
     * Getter for the fail value.
     *
     * @return The fail value.
     */
    public boolean isFail() {
        return fail;
    }

    /**
     * Setter for the fail value.
     *
     * @param failVal the new fail value.
     */
    public void setFail(final boolean failVal) {
        this.fail = failVal;
    }

    /**
     * Getter for the statusCode value.
     *
     * @return The statusCode value.
     */
    public String getStatusCode() {
        return statusCode;
    }

    /**
     * Getter for the subStatusCode.
     *
     * @return The subStatusCode value.
     */
    public String getSubStatusCode() {
        return subStatusCode;
    }

    /**
     * Setter for the subStatusCode.
     *
     * @param samlSubStatusCode the new subStatusCode value.
     */
    public void setSubStatusCode(final String samlSubStatusCode) {
        this.subStatusCode = samlSubStatusCode;
    }

    /**
     * Setter for the statusMessage value.
     *
     * @param status the new statusMessage value.
     */
    public void setStatusMessage(final String status) {
        this.statusMessage = status;
    }

    /**
     * Getter for the statusMessage value.
     *
     * @return The statusMessage value.
     */
    public String getStatusMessage() {
        return statusMessage;
    }

    /**
     * Setter for the statusCode value.
     *
     * @param status the new statusCode value.
     */
    public void setStatusCode(final String status) {
        this.statusCode = status;
    }

    @Override
    public Object clone() throws CloneNotSupportedException {
        STORKLogoutResponse storkLogoutResponse = null;
        storkLogoutResponse = (STORKLogoutResponse) super.clone();
        storkLogoutResponse.setTokenSaml(getTokenSaml());
        return storkLogoutResponse;
    }

    /**
     * Getter for the inResponseTo value.
     *
     * @return The inResponseTo value.
     */
    public String getInResponseTo() {
        return inResponseTo;
    }

    /**
     * Setter for the inResponseTo value.
     *
     * @param inResponseTo the new inResponseTo value.
     */
    public void setInResponseTo(String inResponseTo) {
        this.inResponseTo = inResponseTo;
    }

}

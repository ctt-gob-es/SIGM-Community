/* 
 * Licensed under the EUPL, Version 1.1 or â€“ as soon they will be approved by
 * the European Commission - subsequent versions of the EUPL (the "Licence");
 * You may not use this work except in compliance with the Licence. You may
 * obtain a copy of the Licence at:
 * 
 * http://www.osor.eu/eupl/european-union-public-licence-eupl-v.1.1
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the Licence is distributed on an "AS IS" basis, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * Licence for the specific language governing permissions and limitations under
 * the Licence.
 */

package eu.stork.peps.auth.engine.core;

import java.util.Properties;

import org.apache.commons.lang.StringUtils;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.core.NameIDType;
import org.opensaml.saml2.core.RequestAbstractType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.stork.peps.exceptions.SAMLEngineException;
import eu.stork.peps.exceptions.STORKSAMLEngineRuntimeException;

/**
 * The Class SAMLCore.
 *
 * @author fjquevedo
 */
public final class STORKSAMLCore {

    /** The Constant LOGGER. */
    private static final Logger LOGGER = LoggerFactory
    .getLogger(STORKSAMLCore.class.getName());

    /** The consent authentication request. */
    private String consentAuthnReq = null;

    /** The consent authentication response. */
    private String consentAuthnResp = null;

    /** The id cross border share. */
    private String eIDCrossBordShare = null;

    /** The e id cross sect share. */
    private String eIDCrossSectShare = null;

    /** The e id sector share. */
    private String eIDSectorShare = null;

    /** The format entity. */
    private String formatEntity = null;

    /** The friendly name. */
    private boolean friendlyName = false;

    /** The IP validation. */
    private boolean ipValidation = false;

    /** The one time use. */
    private boolean oneTimeUse = true;

    /** The protocol binding. */
    private String protocolBinding = null;

    /** The requester. */
    private String requester = null;


    /** The responder. */
    private String responder = null;

    /** The SAML core properties. */
    private Properties samlCoreProp = null;

    /** The time not on or after. */
    private Integer timeNotOnOrAfter = null;

    /** The is required parameter. */
    private boolean isRequired = true;

    private static final String SAML_ENGINE_LITERAL = "SamlEngine.xml: ";

    /**
    * Gets the isRequired.
    *
    * @return the isRequired
    */
    public boolean isRequired() {
    	return isRequired;
	}

    /**
     * Sets the isRequired.
     *
     * @param isRequired the required.
     */
	public void setRequired(boolean isRequired) {
		this.isRequired = isRequired;
	}

	/**
     * Instantiates a new sAML core.
     *
     * @param instance the instance
     */
    public STORKSAMLCore(final Properties instance) {
	loadConfiguration(instance);
    }

    /**
     * Gets the consent.
     * 
     * @return the consent
     */
    public String getConsentAuthnRequest() {
	return consentAuthnReq;
    }

    /**
     * Gets the consent authentication response.
     * 
     * @return the consent authentication response.
     */
    public String getConsentAuthnResp() {
	return consentAuthnResp;
    }

    /**
     * Gets the consent authentication response.
     * 
     * @return the consent authentication response
     */
    public String getConsentAuthnResponse() {
	return consentAuthnResp;
    }

    /**
     * Gets the format entity.
     * 
     * @return the format entity
     */
    public String getFormatEntity() {
	return formatEntity;
    }

    /**
     * Gets the property.
     * 
     * @param key the key
     * 
     * @return the property
     */
    public String getProperty(final String key) {
	return samlCoreProp.getProperty(key);
    }

    /**
     * Gets the protocol binding.
     * 
     * @return the protocol binding
     */
    public String getProtocolBinding() {
	return protocolBinding;
    }

    /**
     * Gets the requester.
     * 
     * @return the requester
     */
    public String getRequester() {
	return requester;
    }

    /**
     * Gets the responder.
     * 
     * @return the responder
     */
    public String getResponder() {
	return responder;
    }

    /**
     * Gets the time not on or after.
     * 
     * @return the time not on or after
     */
    public Integer getTimeNotOnOrAfter() {
	return timeNotOnOrAfter;
    }

    /**
     * Checks if is e id cross border share.
     * 
     * @return true, if is e id cross border share
     */
    public String iseIDCrossBorderShare() {
	return eIDCrossBordShare;
    }

    /**
     * Checks if is e id cross border share.
     * 
     * @return true, if is e id cross border share
     */
    public String iseIDCrossBordShare() {
	return eIDCrossBordShare;
    }

    /**
     * Checks if is e id cross sector share.
     * 
     * @return true, if is e id cross sector share
     */
    public String iseIDCrossSectorShare() {
	return eIDCrossSectShare;
    }

    /**
     * Checks if is e id cross sect share.
     * 
     * @return true, if is e id cross sect share
     */
    public String iseIDCrossSectShare() {
	return eIDCrossSectShare;
    }

    /**
     * Checks if is e id sector share.
     * 
     * @return true, if is e id sector share
     */
    public String iseIDSectorShare() {
	return eIDSectorShare;
    }

    /**
     * Checks if is friendly name.
     * 
     * @return true, if checks if is friendly name
     */
    public boolean isFriendlyName() {
	return friendlyName;
    }

    /**
     * Checks if is IP validation.
     * 
     * @return true, if is IP validation
     */
    public boolean isIpValidation() {
	return ipValidation;
    }

    /**
     * Checks if is one time use.
     * 
     * @return true, if is one time use
     */
    public boolean isOneTimeUse() {
	return oneTimeUse;
    }

    /**
     * Method that loads the configuration file for the SAML Engine.
     * 
     * @param instance the instance of the Engine properties.
     */
    private void loadConfiguration(final Properties instance) {

        try {
            LOGGER.info("SAMLCore: Loading SAMLEngine properties.");

            samlCoreProp = instance;

            final String parameter = samlCoreProp
                    .getProperty(SAMLCore.FORMAT_ENTITY.getValue());

            if ("entity".equalsIgnoreCase(parameter)) {
                formatEntity = NameIDType.ENTITY;
            }

            friendlyName = Boolean.valueOf(samlCoreProp
                    .getProperty(SAMLCore.FRIENDLY_NAME.getValue()));

            String isRequiredValue = samlCoreProp.
                    getProperty(SAMLCore.IS_REQUIRED.getValue());
            if (isRequiredValue != null) {
                isRequired = Boolean.valueOf(isRequiredValue);
            }

            eIDSectorShare = samlCoreProp
                    .getProperty("eIDSectorShare");
            eIDCrossSectShare = samlCoreProp
                    .getProperty("eIDCrossSectorShare");
            eIDCrossBordShare = samlCoreProp
                    .getProperty("eIDCrossBorderShare");

            ipValidation = Boolean.valueOf(samlCoreProp
                    .getProperty("ipAddrValidation"));

            final String oneTimeUseProp = samlCoreProp
                    .getProperty(SAMLCore.ONE_TIME_USE.getValue());

            if (StringUtils.isNotBlank(oneTimeUseProp)) {
                oneTimeUse = Boolean.valueOf(oneTimeUseProp);
            }

            // Protocol Binding
            loadProtocolBiding();

            // Consent Authentication Request
            consentAuthnReq = samlCoreProp
                    .getProperty(SAMLCore.CONSENT_AUTHN_REQ.getValue());

            if ("unspecified".equalsIgnoreCase(consentAuthnReq)) {
                consentAuthnReq = RequestAbstractType.UNSPECIFIED_CONSENT;
            }

            loadConsentAuthResp();

            timeNotOnOrAfter = Integer.valueOf(samlCoreProp
                    .getProperty("timeNotOnOrAfter"));

            if (timeNotOnOrAfter.intValue() < 0) {
                LOGGER.error(SAML_ENGINE_LITERAL + "timeNotOnOrAfter"
                        + " is negative number.");

                throw new SAMLEngineException(SAML_ENGINE_LITERAL
                        + "timeNotOnOrAfter" + " is negative number.");
            }

            requester = samlCoreProp.getProperty(SAMLCore.REQUESTER_TAG.getValue());
            responder = samlCoreProp.getProperty(SAMLCore.RESPONDER_TAG.getValue());

        } catch (SAMLEngineException e) {
            LOGGER.error("SAMLCore: error loadConfiguration. ", e);
            throw new STORKSAMLEngineRuntimeException(e);
        } catch (RuntimeException e) {
            LOGGER.error("SAMLCore: error loadConfiguration. ", e);
            throw new STORKSAMLEngineRuntimeException(e);
        }
    }

    /**
     * Load consent authentication response.
     */
    private void loadConsentAuthResp() {
	// Consent Authentication Response
	consentAuthnResp = samlCoreProp
		.getProperty(SAMLCore.CONSENT_AUTHN_RES.getValue());

	if ("obtained".equalsIgnoreCase(consentAuthnResp)) {
	    consentAuthnResp = RequestAbstractType.OBTAINED_CONSENT;
	} else if ("prior".equalsIgnoreCase(consentAuthnResp)) {
	    consentAuthnResp = RequestAbstractType.PRIOR_CONSENT;
	} else if ("curent-implicit".equalsIgnoreCase(consentAuthnResp)) {
	    consentAuthnResp = 
		"urn:oasis:names:tc:SAML:2.0:consent:current-implicit";
	} else if ("curent-explicit".equalsIgnoreCase(consentAuthnResp)) {
	    consentAuthnResp = 
		"urn:oasis:names:tc:SAML:2.0:consent:current-explicit";
	} else if ("unspecified".equalsIgnoreCase(consentAuthnResp)) {
	    consentAuthnResp = RequestAbstractType.UNSPECIFIED_CONSENT;
	}
    }

    /**
     * Load protocol biding.
     * 
     * @throws SAMLEngineException the SAML engine exception
     */
    private void loadProtocolBiding() throws SAMLEngineException {
	// Protocol Binding
	protocolBinding = samlCoreProp.getProperty(SAMLCore.PROT_BINDING_TAG.getValue());

	if (StringUtils.isBlank(protocolBinding)) {
	    LOGGER.error(SAML_ENGINE_LITERAL + SAMLCore.PROT_BINDING_TAG
		    + " it's mandatory.");
	    throw new SAMLEngineException(SAML_ENGINE_LITERAL
		    + SAMLCore.PROT_BINDING_TAG + " it's mandatory.");
	} else if (protocolBinding.equalsIgnoreCase("HTTP-POST")) {
	    protocolBinding = SAMLConstants.SAML2_POST_BINDING_URI;
	} else {
	    LOGGER.error(SAML_ENGINE_LITERAL + SAMLCore.PROT_BINDING_TAG
		    + " it's not supporting.");

	    throw new SAMLEngineException(SAML_ENGINE_LITERAL
		    + SAMLCore.PROT_BINDING_TAG + " it's not supporting.");
	}
    }

    /**
     * Sets the consent authentication response.
     * 
     * @param newConsAuthnResp the new consent authentication response
     */
    public void setConsentAuthnResp(final String newConsAuthnResp) {
	this.consentAuthnResp = newConsAuthnResp;
    }

    /**
     * Sets an eID that can be shared outside of the  Service Provider’s member state.
     * 
     * @param newEIDCrossBord the new eid cross border share
     */
    public void setEIDCrossBordShare(final String newEIDCrossBord) {
	this.eIDCrossBordShare = newEIDCrossBord;
    }

    /**
     * Sets an eID that can be shared outside of the  Service Provider’s sector.
     * 
     * @param newEIDCrossSect the new eid cross sect share
     */
    public void setEIDCrossSectShare(final String newEIDCrossSect) {
	this.eIDCrossSectShare = newEIDCrossSect;
    }

    /**
     * Sets an eID that can be shared within the Service Provider’s sector.
     * 
     * @param newEIDSectorShare the new eid sector share
     */
    public void seteIDSectorShare(final String newEIDSectorShare) {
	this.eIDSectorShare = newEIDSectorShare;
    }

    /**
     * Sets the format entity.
     * 
     * @param newFormatEntity the new format entity
     */
    public void setFormatEntity(final String newFormatEntity) {
	this.formatEntity = newFormatEntity;
    }

    /**
     * Sets the friendly name.
     * 
     * @param newFriendlyName the new friendly name
     */
    public void setFriendlyName(final boolean newFriendlyName) {
	this.friendlyName = newFriendlyName;
    }

    /**
     * Sets the IP validation.
     * 
     * @param newIpValidation the new IP validation
     */
    public void setIpValidation(final boolean newIpValidation) {
	this.ipValidation = newIpValidation;
    }

    /**
     * Sets the one time use.
     * 
     * @param newOneTimeUse the new one time use
     */
    public void setOneTimeUse(final boolean newOneTimeUse) {
	this.oneTimeUse = newOneTimeUse;
    }

    /**
     * Sets the protocol binding.
     * 
     * @param newProtBinding the new protocol binding
     */
    public void setProtocolBinding(final String newProtBinding) {
	this.protocolBinding = newProtBinding;
    }

    /**
     * Sets the requester.
     * 
     * @param newRequester the new requester
     */
    public void setRequester(final String newRequester) {
	this.requester = newRequester;
    }

    /**
     * Sets the responder.
     * 
     * @param newResponder the new responder
     */
    public void setResponder(final String newResponder) {
	this.responder = newResponder;
    }

    /**
     * Sets the time not on or after.
     * 
     * @param newTimeNotOnOrAft the new time not on or after
     */
    public void setTimeNotOnOrAfter(final Integer newTimeNotOnOrAft) {
	this.timeNotOnOrAfter = newTimeNotOnOrAft;
    }

}

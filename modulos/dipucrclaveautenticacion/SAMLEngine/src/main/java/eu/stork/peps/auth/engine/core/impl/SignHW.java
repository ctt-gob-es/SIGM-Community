/* 
 * Licensed under the EUPL, Version 1.1 or – as soon they will be approved by
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

package eu.stork.peps.auth.engine.core.impl;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;

import eu.stork.peps.auth.engine.X509PrincipalUtil;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.NotImplementedException;
import org.bouncycastle.jce.X509Principal;
import org.opensaml.Configuration;
import org.opensaml.common.SAMLObject;
import org.opensaml.common.SignableSAMLObject;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.LogoutResponse;
import org.opensaml.saml2.core.Response;
import org.opensaml.security.SAMLSignatureProfileValidator;
import org.opensaml.xml.io.MarshallingException;
import org.opensaml.xml.security.SecurityConfiguration;
import org.opensaml.xml.security.SecurityException;
import org.opensaml.xml.security.credential.Credential;
import org.opensaml.xml.security.keyinfo.KeyInfoGenerator;
import org.opensaml.xml.security.keyinfo.KeyInfoGeneratorFactory;
import org.opensaml.xml.security.keyinfo.KeyInfoGeneratorManager;
import org.opensaml.xml.security.keyinfo.KeyInfoHelper;
import org.opensaml.xml.security.keyinfo.NamedKeyInfoGeneratorManager;
import org.opensaml.xml.security.trust.ExplicitKeyTrustEvaluator;
import org.opensaml.xml.security.x509.BasicX509Credential;
import org.opensaml.xml.signature.KeyInfo;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureConstants;
import org.opensaml.xml.signature.SignatureException;
import org.opensaml.xml.signature.SignatureValidator;
import org.opensaml.xml.signature.Signer;
import org.opensaml.xml.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;



import eu.stork.peps.auth.engine.core.CustomAttributeQuery;
import eu.stork.peps.auth.engine.core.SAMLEngineSignI;
import eu.stork.peps.exceptions.SAMLEngineException;

/**
 * The Class HWSign. Module of sign.
 * 
 * @author fjquevedo
 */
public final class SignHW implements SAMLEngineSignI {

    /** The Constant CONFIGURATION_FILE. */
    private static final String CONF_FILE = "configurationFile";

    /** The Constant KEYSTORE_TYPE.
    private static final String KEYSTORE_TYPE = "keystoreType" */

    /** The logger. */
    private static final Logger LOG = LoggerFactory.getLogger(SignHW.class
	    .getName());

    /** The stork own key store. */
    private KeyStore storkOwnKeyStore = null;

    /**
     * Gets the stork own key store.
     * 
     * @return the stork own key store
     */
    public KeyStore getStorkOwnKeyStore() {
	return storkOwnKeyStore;
    }
    
    /**
     * Gets the stork trustStore.
     * 
     * @return the stork own key store
     */
    public KeyStore getTrustStore() {
	return storkOwnKeyStore;
    }

    /**
     * Sets the stork own key store.
     * 
     * @param newkOwnKeyStore the new stork own key store
     */
    public void setStorkOwnKeyStore(final KeyStore newkOwnKeyStore) {
	this.storkOwnKeyStore = newkOwnKeyStore;
    }

    /**
     * Gets the properties.
     * 
     * @return the properties
     */
    public Properties getProperties() {
	return properties;
    }

    /**
     * Sets the properties.
     * 
     * @param newProperties the new properties
     */
    public void setProperties(final Properties newProperties) {
	this.properties = newProperties;
    }

    /** The HW sign prop. */
    private Properties properties = null;

    /**
     * @see
     * eu.stork.peps.auth.engine.core.SAMLEngineSignI#init(java.lang.String)
     * @param fileConf file of configuration
     * @throws SAMLEngineException error in read file
     */
    public void init(final String fileConf)
	    throws SAMLEngineException {
	InputStream inputStr = null;
	try {
	inputStr = SignHW.class.getResourceAsStream("/"
		+ fileConf);
	properties = new Properties();
	
	    properties.loadFromXML(inputStr);
	} catch (final InvalidPropertiesFormatException e) {
	    LOG.info("Exception: invalid properties format.");
	    throw new SAMLEngineException(e);
	} catch (IOException e) {
	    LOG.info("Exception: invalid file: " + fileConf);
	    throw new SAMLEngineException(e);
	} finally {
	    IOUtils.closeQuietly(inputStr);
	}
    }


    /**
     * @see eu.stork.peps.auth.engine.core.SAMLEngineSignI#getCertificate()
     * @return the X509Certificate.
     */
    public X509Certificate getCertificate() {
	throw new NotImplementedException();
    }

    /**
     * @see
     * eu.stork.peps.auth.engine.core.SAMLEngineSignI#sign(SignableSAMLObject tokenSaml)
     * @param tokenSaml signable SAML Object
     * @return the SAMLObject signed.
     * @throws SAMLEngineException error in sign token saml
     */
    public SAMLObject sign(final SignableSAMLObject tokenSaml) throws SAMLEngineException {

	try {
	    LOG.info("Star procces of sign");
	    final char[] pin = properties.getProperty("keyPassword")
		    .toCharArray();

	    storkOwnKeyStore.load(null, pin);

	    final String serialNumber = properties.getProperty("serialNumber");
	    final String issuer = properties.getProperty("issuer");

	    String alias = null;
	    String aliasCert;
	    X509Certificate certificate;

	    boolean find = false;
	    for (final Enumeration<String> e = storkOwnKeyStore.aliases(); e
		    .hasMoreElements() && !find;) {
		aliasCert = e.nextElement();
		certificate = (X509Certificate) storkOwnKeyStore
			.getCertificate(aliasCert);
		// Verified serial number, issuer

		final String serialNum = certificate.getSerialNumber()
			.toString(16);
		X509Principal issuerDN = new X509Principal(certificate.getIssuerDN().getName());
		X509Principal issuerDNConf = new X509Principal(issuer);

            if(serialNum.equalsIgnoreCase(serialNumber)
                    && X509PrincipalUtil.equals(issuerDN, issuerDNConf)){
		              alias = aliasCert;
		              find = true;
		}
		
	    }

        if (!find) {
            throw new SAMLEngineException("Certificate cannot be found in keystore ");
        }
	    certificate = (X509Certificate) storkOwnKeyStore.getCertificate(alias);
	    final PrivateKey privateKey = (PrivateKey) storkOwnKeyStore.getKey(
		    alias, pin);

	    LOG.info("Recover BasicX509Credential.");
	    final BasicX509Credential credential = new BasicX509Credential();

	    LOG.debug("Load certificate");
	    credential.setEntityCertificate(certificate);

	    LOG.debug("Load privateKey");
	    credential.setPrivateKey(privateKey);

	    LOG.info("Star procces of sign");
	    final Signature signature = (Signature) org.opensaml.xml.Configuration
		    .getBuilderFactory().getBuilder(
			    Signature.DEFAULT_ELEMENT_NAME).buildObject(
			    Signature.DEFAULT_ELEMENT_NAME);

	    LOG.debug("Begin signature with openSaml");
	    signature.setSigningCredential(credential);

	    /*signature.setSignatureAlgorithm(
	    SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1);*/
	    signature.setSignatureAlgorithm(
		    SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA256);

	 

	    final SecurityConfiguration securityConf =
		org.opensaml.xml.Configuration.getGlobalSecurityConfiguration();
	    final NamedKeyInfoGeneratorManager keyInfoManager = securityConf
		    .getKeyInfoGeneratorManager();
	    final KeyInfoGeneratorManager keyInfoGenManager = keyInfoManager
		    .getDefaultManager();
	    final KeyInfoGeneratorFactory keyInfoGenFac = keyInfoGenManager
		    .getFactory(credential);
	    final KeyInfoGenerator keyInfoGenerator = keyInfoGenFac
		    .newInstance();
	    
	    final KeyInfo keyInfo = keyInfoGenerator.generate(credential);

	    signature.setKeyInfo(keyInfo);

	    LOG.debug("Set Canonicalization Algorithm");
	    signature.setCanonicalizationAlgorithm(
		    SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);

	  //Create a second signature which will be used when signing assertion and response
	    final Signature signature2 = (Signature) Configuration
	    .getBuilderFactory().getBuilder(
		    Signature.DEFAULT_ELEMENT_NAME).buildObject(
		    Signature.DEFAULT_ELEMENT_NAME);
	    final SecurityConfiguration secConfiguration2 = Configuration
	    .getGlobalSecurityConfiguration();
	    final NamedKeyInfoGeneratorManager keyInfoManager2 = secConfiguration2
		    .getKeyInfoGeneratorManager();
	    final KeyInfoGeneratorManager keyInfoGenManager2 = keyInfoManager2
		    .getDefaultManager();
	    final KeyInfoGeneratorFactory keyInfoGenFac2 = keyInfoGenManager2
		    .getFactory(credential);
	    final KeyInfoGenerator keyInfoGenerator2 = keyInfoGenFac2
		    .newInstance();
	    
	    KeyInfo keyInfo2 = keyInfoGenerator2.generate(credential);
	    signature2.setSigningCredential(credential);
	    signature2.setSignatureAlgorithm(
			    SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA256);
	    signature2.setKeyInfo(keyInfo2);
	    signature2.setCanonicalizationAlgorithm(
		    SignatureConstants.ALGO_ID_C14N_EXCL_OMIT_COMMENTS);
	    	 	    

	    LOG.info("Marshall samlToken.");
	    String qn = tokenSaml.getElementQName().toString();
	    
	    if (qn.endsWith(CustomAttributeQuery.DEFAULT_ELEMENT_LOCAL_NAME))
	    {	    
	  		tokenSaml.setSignature(signature);
	    	CustomAttributeQueryMarshaller mars = new CustomAttributeQueryMarshaller();
	    	mars.marshall(tokenSaml);
	    	Signer.signObject(signature);
	    }
	    else if (qn.endsWith(Response.DEFAULT_ELEMENT_LOCAL_NAME) && !qn.contains(LogoutResponse.DEFAULT_ELEMENT_LOCAL_NAME))
	    {
	    	Response res = (Response)tokenSaml;
	    	List<Assertion> asserts = res.getAssertions();
	    	//If multiple assertions we just sign the response and not the assertion
	    	if (asserts.size() > 1)
	    	{	    		
	    		tokenSaml.setSignature(signature);
	    		Configuration.getMarshallerFactory().getMarshaller(tokenSaml)
			    .marshall(tokenSaml);
	    		LOG.info("Sign samlToken.");
	    	    Signer.signObject(signature);
	    	}
	    	//If single assertion we sign the assertion and response
	    	else
	    	{
			    Assertion assertion = (Assertion)asserts.get(0);
			    assertion.setSignature(signature);			    
			    tokenSaml.setSignature(signature2);
			    Configuration.getMarshallerFactory().getMarshaller(tokenSaml)
			    .marshall(tokenSaml);
			    LOG.info("Sign samlToken.");
			    Signer.signObject(signature);
			    Signer.signObject(signature2);
	    	}
	    }
	    //Normally we just sign the total saml response
	    else
	    {
	    	tokenSaml.setSignature(signature);
	    	Configuration.getMarshallerFactory().getMarshaller(tokenSaml)
		    .marshall(tokenSaml);
	    	LOG.info("Sign samlToken.");
		    Signer.signObject(signature);
	    }

	} catch (final MarshallingException e) {
	    LOG.error("MarshallingException");
	    throw new SAMLEngineException(e);
	} catch (final NoSuchAlgorithmException e) {
	    LOG.error("A 'xmldsig#rsa-sha1' cryptographic algorithm is requested but is not available in the environment.");
	    throw new SAMLEngineException(e);
	} catch (final KeyStoreException e) {
	    LOG.error("Generic KeyStore exception.");
	    throw new SAMLEngineException(e);
	} catch (final SignatureException e) {
	    LOG.error("Signature exception.");
	    throw new SAMLEngineException(e);
	} catch (final SecurityException e) {
	    LOG.error("Security exception.");
	    throw new SAMLEngineException(e);
	} catch (final CertificateException e) {
	    LOG.error("Certificate exception.");
	    throw new SAMLEngineException(e);
	} catch (final IOException e) {
	    LOG.error("IO exception.");
	    throw new SAMLEngineException(e);
	} catch (final UnrecoverableKeyException e) {
	    LOG.error("UnrecoverableKeyException exception.");
	    throw new SAMLEngineException(e);
	}

	return tokenSaml;
    }

    /**
     * @see
     * eu.stork.peps.auth.engine.core.SAMLEngineSignI#validateSignature(SignableSAMLObject)
     * @param tokenSaml the token saml
     * @return the SAMLObject validated.
     * @throws SAMLEngineException exception in validate signature
     */
    public SAMLObject validateSignature(final SignableSAMLObject tokenSaml)
	    throws SAMLEngineException {
	LOG.info("Start signature validation.");
	try {

	    // Validate structure signature
	    final SAMLSignatureProfileValidator signProfValidator = 
		new SAMLSignatureProfileValidator();

	    // Indicates signature id conform to SAML Signature profile
	    signProfValidator.validate(tokenSaml.getSignature());

	    String aliasCert;
	    X509Certificate certificate;

	    final List<Credential> trustedCred = new ArrayList<Credential>();

	    for (final Enumeration<String> e = storkOwnKeyStore.aliases(); e
		    .hasMoreElements();) {
		aliasCert = e.nextElement();
		final BasicX509Credential credential = new BasicX509Credential();
		certificate = (X509Certificate) storkOwnKeyStore
			.getCertificate(aliasCert);
		credential.setEntityCertificate(certificate);
		trustedCred.add(credential);
	    }

	    final KeyInfo keyInfo = tokenSaml.getSignature().getKeyInfo();
	    final List<X509Certificate> listCertificates = KeyInfoHelper
		    .getCertificates(keyInfo);

	    if (listCertificates.size() != 1) {
		throw new SAMLEngineException("Only must be one certificate");
	    }

	    // Exist only one certificate
	    final BasicX509Credential entityX509Cred = new BasicX509Credential();
	    entityX509Cred.setEntityCertificate(listCertificates.get(0));

	    final ExplicitKeyTrustEvaluator keyTrustEvaluator = 
		new ExplicitKeyTrustEvaluator();
	    if (!keyTrustEvaluator.validate(entityX509Cred, trustedCred)) {
		throw new SAMLEngineException("Certificate it is not trusted.");
	    }

	    final SignatureValidator sigValidator = new SignatureValidator(
		    entityX509Cred);

	    sigValidator.validate(tokenSaml.getSignature());

	} catch (final ValidationException e) {
	    LOG.error("ValidationException.", e);
	    throw new SAMLEngineException(e);
	} catch (final KeyStoreException e) {
	    LOG.error("ValidationException.", e);
	    throw new SAMLEngineException(e);
	} catch (final CertificateException e) {
	    LOG.error("CertificateException.", e);
	    throw new SAMLEngineException(e);
	}
	return tokenSaml;
    }

    /**
     * load cryptographic service provider.
     * 
     * @throws SAMLEngineException the SAML engine exception
     * Note this class was using pkcs11Provider
     * final Provider pkcs11Provider = new sun.security.pkcs11.SunPKCS11(inputStream)
     * if (Security.getProperty(pkcs11Provider.getName()) == null) {
     * Security.insertProviderAt(pkcs11Provider, Security .getProviders().length)
     * }
     * storkOwnKeyStore = KeyStore.getInstance(properties.getProperty(KEYSTORE_TYPE))
     */
    public void loadCryptServiceProvider() throws SAMLEngineException {
	LOG.info("Load Cryptographic Service Provider");
	InputStream inputStream = null; 
	 
	try {
	    inputStream = SignHW.class.getResourceAsStream("/"
		    + properties.getProperty(CONF_FILE));

	} catch (final Exception e) {	       
	    throw new SAMLEngineException(
		    "Error loading CryptographicServiceProvider", e);
	} finally {
	    IOUtils.closeQuietly(inputStream);
	}
    }

}

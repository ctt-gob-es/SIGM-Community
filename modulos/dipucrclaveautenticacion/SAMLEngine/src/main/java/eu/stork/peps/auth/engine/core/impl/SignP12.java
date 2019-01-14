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

import java.io.ByteArrayInputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.Provider;
import java.security.Security;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.Enumeration;
import java.util.InvalidPropertiesFormatException;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.NotImplementedException;
import org.bouncycastle.jce.X509Principal;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.util.Arrays;
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
import org.opensaml.xml.security.keyinfo.KeyInfoGenerator;
import org.opensaml.xml.security.keyinfo.KeyInfoGeneratorFactory;
import org.opensaml.xml.security.keyinfo.KeyInfoGeneratorManager;
import org.opensaml.xml.security.keyinfo.NamedKeyInfoGeneratorManager;
import org.opensaml.xml.security.x509.BasicX509Credential;
import org.opensaml.xml.signature.KeyInfo;
import org.opensaml.xml.signature.Signature;
import org.opensaml.xml.signature.SignatureConstants;
import org.opensaml.xml.signature.SignatureException;
import org.opensaml.xml.signature.SignatureValidator;
import org.opensaml.xml.signature.Signer;
import org.opensaml.xml.util.Base64;
import org.opensaml.xml.validation.ValidationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import eu.stork.peps.auth.engine.X509PrincipalUtil;
import eu.stork.peps.auth.engine.core.CustomAttributeQuery;
import eu.stork.peps.auth.engine.core.SAMLEngineSignI;
import eu.stork.peps.exceptions.SAMLEngineException;

/**
 * The Class SWSign. Class responsible for signing and validating of messages
 * SAML with a certificate store software.
 * 
 * @author fjquevedo
 */
public final class SignP12 implements SAMLEngineSignI {

	/** The logger. */
	private static final Logger LOG = LoggerFactory.getLogger(SignP12.class
			.getName());


	/** The p12 store. */
	private KeyStore p12Store = null;


	/** The trust store. */
	private KeyStore trustStore = null;


	/**
	 * Gets the trust store.
	 * 
	 * @return the trust store
	 */
	public KeyStore getTrustStore() {
		return trustStore;
	}

	/**
	 * Sets the trust store.
	 * 
	 * @param newTrustStore the new trust store
	 */
	public void setTrustStore(final KeyStore newTrustStore) {
		this.trustStore = newTrustStore;
	}

	/**
	 * The instance.
	 * 
	 * @return the properties
	 */

	public Properties getProperties() {
		return properties;
	}



	/**
	 * Gets the p12 store.
	 * 
	 * @return the p12 store
	 */
	public KeyStore getP12Store() {
		return p12Store;
	}



	/**
	 * Sets the p12 store.
	 * 
	 * @param newP12Store the new p12 store
	 */
	public void setP12Store(final KeyStore newP12Store) {
		this.p12Store = newP12Store;
	}



	/**
	 * Sets the properties.
	 * 
	 * @param newProperties the new properties
	 */
	public void setProperties(final Properties newProperties) {
		this.properties = newProperties;
	}

	/** The SW sign prop. */
	private Properties properties = null;


	/**
	 * Initialize the file configuration.
	 * 
	 * @param fileConf name of the file configuration
	 * 
	 * @throws SAMLEngineException error at the load from  file configuration
	 */
	public void init(final String fileConf) throws SAMLEngineException {
		InputStream fileProperties = null;
		properties = new Properties();
		try {
			try {
				LOG.debug("Fichero a cargar " + fileConf);
				fileProperties = new FileInputStream(fileConf);
				properties.loadFromXML(fileProperties);
			} catch (Exception e) {
				LOG.error("Fallo al cargar el recurso externo. Se reintenta como fichero interno.");
				fileProperties = SignP12.class.getResourceAsStream("/" + fileConf);
				if (fileProperties == null) {
					fileProperties = Thread.currentThread().getContextClassLoader().getResourceAsStream(fileConf);
					if (fileProperties == null) {
						Enumeration<URL> files = ClassLoader.getSystemClassLoader().getResources(fileConf);
						if (files != null && files.hasMoreElements()) {
							LOG.info("Se han encontrado recurso/s. Se toma el primero.");
							fileProperties = ClassLoader.getSystemClassLoader().getResourceAsStream(files.nextElement().getFile());
						} else {
							throw new IOException("No se pudo recuperar el fichero: " + fileConf, e);
						}
					}
				}
				LOG.debug("Recuperados " + fileProperties.available() + " bytes");
				properties.loadFromXML(fileProperties);
			}
		} catch (InvalidPropertiesFormatException e) {
			LOG.info("Exception: invalid properties format.");
			throw new SAMLEngineException(e);
		} catch (IOException e) {
			LOG.info("Exception: invalid file: " + fileConf);
			throw new SAMLEngineException(e);
		} finally {
			IOUtils.closeQuietly(fileProperties);
		}
	}

	/**
	 * Gets the certificate.
	 * 
	 * @return the X509Certificate
	 *     
	 */
	public X509Certificate getCertificate() {
		throw new NotImplementedException();
	}

	/**
	 * Sign the token SAML.
	 * 
	 * @param tokenSaml token SAML
	 * 
	 * @return the X509Certificate signed.
	 * 
	 * @throws SAMLEngineException error at sign SAML token
	 *
	 */
	public SAMLObject sign(final SignableSAMLObject tokenSaml)
			throws SAMLEngineException {
		LOG.info("Start Sign process");
		try {

			final String serialNumber = properties.getProperty("serialNumber");
			final String issuer = properties.getProperty("issuer");

			String alias = null;
			String aliasCert;
			X509Certificate certificate;

			boolean find = false;
			for (final Enumeration<String> e = p12Store.aliases(); e
					.hasMoreElements() && !find;) {
				aliasCert = e.nextElement();
				certificate = (X509Certificate) p12Store
						.getCertificate(aliasCert);

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

			certificate = (X509Certificate) p12Store
					.getCertificate(alias);
			final PrivateKey privateKey = (PrivateKey) p12Store.getKey(
					alias, properties.getProperty("keyPassword").toCharArray());

			LOG.info("Recover BasicX509Credential.");
			final BasicX509Credential credential = new BasicX509Credential();

			LOG.debug("Load certificate");
			credential.setEntityCertificate(certificate);

			LOG.debug("Load privateKey");
			credential.setPrivateKey(privateKey);

			LOG.debug("Begin signature with openSaml");
			final Signature signature = (Signature) Configuration
					.getBuilderFactory().getBuilder(
							Signature.DEFAULT_ELEMENT_NAME).buildObject(
									Signature.DEFAULT_ELEMENT_NAME);

			signature.setSigningCredential(credential);

			/*signature.setSignatureAlgorithm(
	    SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA1);*/
			signature.setSignatureAlgorithm(
					SignatureConstants.ALGO_ID_SIGNATURE_RSA_SHA256);

			final SecurityConfiguration secConfiguration = Configuration
					.getGlobalSecurityConfiguration();
			final NamedKeyInfoGeneratorManager keyInfoManager = secConfiguration
					.getKeyInfoGeneratorManager();
			final KeyInfoGeneratorManager keyInfoGenManager = keyInfoManager
					.getDefaultManager();
			final KeyInfoGeneratorFactory keyInfoGenFac = keyInfoGenManager
					.getFactory(credential);
			final KeyInfoGenerator keyInfoGenerator = keyInfoGenFac
					.newInstance();

			final KeyInfo keyInfo = keyInfoGenerator.generate(credential);

			signature.setKeyInfo(keyInfo);
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

		} catch (MarshallingException e) {
			LOG.error("MarshallingException");
			throw new SAMLEngineException(e);
		} catch (NoSuchAlgorithmException e) {
			LOG.error("A 'xmldsig#rsa-sha1' cryptographic algorithm is requested but is not available in the environment.");
			throw new SAMLEngineException(e);
		} catch (KeyStoreException e) {
			LOG.error("Generic KeyStore exception.");
			throw new SAMLEngineException(e);
		} catch (SignatureException e) {
			LOG.error("Signature exception.");
			throw new SAMLEngineException(e);
		} catch (SecurityException e) {
			LOG.error("Security exception.");
			throw new SAMLEngineException(e);
		} catch (UnrecoverableKeyException e) {
			LOG.error("UnrecoverableKey exception.");
			throw new SAMLEngineException(e);
		}

		return tokenSaml;
	}

	/**
	 * Validate signature.
	 * 
	 * @param tokenSaml token SAML
	 * 
	 * @return the SAMLObject validated.
	 * 
	 * @throws SAMLEngineException error validate signature
	 * 
	 */
	public SAMLObject validateSignature(final SignableSAMLObject tokenSaml)
			throws SAMLEngineException {
		LOG.info("Start signature validation.");
		try {

			// Validate structure signature
			final SAMLSignatureProfileValidator sigProfValidator = 
					new SAMLSignatureProfileValidator();
			try {
				// Indicates signature id conform to SAML Signature profile
				sigProfValidator.validate(tokenSaml.getSignature());
			} catch (ValidationException e) {
				LOG.error("ValidationException: signature isn't conform to SAML Signature profile.");
				throw new SAMLEngineException(e);
			}

			String aliasCert = null;
			X509Certificate certificate;

			/*final List<Credential> trustCred = new ArrayList<Credential>();

	    for (final Enumeration<String> e = trustStore.aliases(); e
		    .hasMoreElements();) {
		aliasCert = e.nextElement();
		final BasicX509Credential credential = new BasicX509Credential();
		certificate = (X509Certificate) trustStore
			.getCertificate(aliasCert);
		credential.setEntityCertificate(certificate);
		trustCred.add(credential);
	    }*/

			final KeyInfo keyInfo = tokenSaml.getSignature().getKeyInfo();

			final org.opensaml.xml.signature.X509Certificate xmlCert = keyInfo
					.getX509Datas().get(0).getX509Certificates().get(0);

			final CertificateFactory certFact = CertificateFactory
					.getInstance("X.509");
			final ByteArrayInputStream bis = new ByteArrayInputStream(Base64
					.decode(xmlCert.getValue()));
			final X509Certificate cert = (X509Certificate) certFact
					.generateCertificate(bis);

			// Exist only one certificate
			final BasicX509Credential entityX509Cred = new BasicX509Credential();
			entityX509Cred.setEntityCertificate(cert);

			boolean trusted = false;

			for (final Enumeration<String> e = trustStore.aliases(); e.hasMoreElements();) 
			{
				aliasCert = e.nextElement();		
				certificate = (X509Certificate) trustStore.getCertificate(aliasCert);					
				try {
					cert.verify(certificate.getPublicKey());
					trusted = true;
					break;
				}
				catch (Exception ex) {
					//Do nothing - cert not trusted yet
				}
				if (Arrays.areEqual(cert.getEncoded(), certificate.getEncoded())) {
					trusted = true;
					break;
				}
			}

			if (!trusted)
				throw new SAMLEngineException("Certificate is not trusted.");

			/*
	    // Validate trust certificates
	    final ExplicitKeyTrustEvaluator keyTrustEvaluator = 
		new ExplicitKeyTrustEvaluator();
	    if (!keyTrustEvaluator.validate(entityX509Cred, trustCred)) {
		throw new SAMLEngineException("Certificate it is not trusted.");
	    }*/

			// Validate signature
			final SignatureValidator sigValidator = new SignatureValidator(
					entityX509Cred);
			sigValidator.validate(tokenSaml.getSignature());

		} catch (ValidationException e) {
			LOG.error("ValidationException.");
			throw new SAMLEngineException(e);
		} catch (KeyStoreException e) {
			LOG.error("KeyStoreException.", e);
			throw new SAMLEngineException(e);
		} catch (GeneralSecurityException e) {
			LOG.error("GeneralSecurityException.", e);
			throw new SAMLEngineException(e);
		}
		return tokenSaml;
	}


	/**
	 * Load cryptographic service provider.
	 * 
	 * @throws SAMLEngineException the SAML engine exception
	 */
	public void loadCryptServiceProvider() throws SAMLEngineException {
		LOG.info("Load Cryptographic Service Provider");

		FileInputStream fis = null;
		FileInputStream fisTrustStore = null;

		try {
			// Dynamically register Bouncy Castle provider.
			boolean found = false;
			// Check if BouncyCastle is already registered as a provider
			final Provider[] providers = Security.getProviders();
			for (int i = 0; i < providers.length; i++) {
				if (providers[i].getName().equals(
						BouncyCastleProvider.PROVIDER_NAME)) {
					found = true;
				}
			}

			// Register only if the provider has not been previously registered
			if (!found) {
				LOG.debug("SAMLCore: Register Bouncy Castle provider.");
				Security.insertProviderAt(new BouncyCastleProvider(), Security
						.getProviders().length);
			}

			p12Store = KeyStore.getInstance(properties
					.getProperty("keystoreType"));

			fis = new FileInputStream(properties
					.getProperty("keystorePath"));

			p12Store.load(fis, properties.getProperty(
					"keyStorePassword").toCharArray());


			trustStore = KeyStore.getInstance(properties
					.getProperty("trustStoreType"));

			fisTrustStore = new FileInputStream(properties
					.getProperty("trustStorePath"));
			trustStore.load(fisTrustStore, properties.getProperty(
					"trustStorePassword").toCharArray());

		} catch (Exception e) {
			throw new SAMLEngineException(
					"Error loading CryptographicServiceProvider", e);
		}  finally {
			IOUtils.closeQuietly(fis);
			IOUtils.closeQuietly(fisTrustStore);
		}	
	}
}

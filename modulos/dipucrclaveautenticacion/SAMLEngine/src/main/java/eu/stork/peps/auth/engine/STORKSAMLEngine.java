/* 
 * Licensed under the EUPL, Version 1.1 or - as soon they will be approved by
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

package eu.stork.peps.auth.engine;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang.StringUtils;
import org.bouncycastle.jce.X509Principal;
import org.joda.time.DateTime;
import org.opensaml.Configuration;
import org.opensaml.common.SAMLVersion;
import org.opensaml.common.SignableSAMLObject;
import org.opensaml.common.xml.SAMLConstants;
import org.opensaml.saml2.common.Extensions;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeStatement;
import org.opensaml.saml2.core.AttributeValue;
import org.opensaml.saml2.core.Audience;
import org.opensaml.saml2.core.AudienceRestriction;
import org.opensaml.saml2.core.AuthnContext;
import org.opensaml.saml2.core.AuthnContextDecl;
import org.opensaml.saml2.core.AuthnRequest;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.Conditions;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.LogoutRequest;
import org.opensaml.saml2.core.LogoutResponse;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.OneTimeUse;
import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.core.Status;
import org.opensaml.saml2.core.StatusCode;
import org.opensaml.saml2.core.StatusMessage;
import org.opensaml.saml2.core.Subject;
import org.opensaml.saml2.core.SubjectConfirmation;
import org.opensaml.saml2.core.SubjectConfirmationData;
import org.opensaml.saml2.core.SubjectLocality;
import org.opensaml.saml2.core.impl.SubjectConfirmationBuilder;
import org.opensaml.xml.Namespace;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.schema.XSAny;
import org.opensaml.xml.schema.impl.XSAnyBuilder;
import org.opensaml.xml.schema.impl.XSAnyImpl;
import org.opensaml.xml.schema.impl.XSAnyMarshaller;
import org.opensaml.xml.schema.impl.XSAnyUnmarshaller;
import org.opensaml.xml.schema.impl.XSStringImpl;
import org.opensaml.xml.signature.KeyInfo;
import org.opensaml.xml.util.Base64;
import org.opensaml.xml.validation.ValidationException;
import org.opensaml.xml.validation.Validator;
import org.opensaml.xml.validation.ValidatorSuite;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import eu.stork.peps.auth.commons.IPersonalAttributeList;
import eu.stork.peps.auth.commons.PersonalAttribute;
import eu.stork.peps.auth.commons.PersonalAttributeList;
import eu.stork.peps.auth.commons.STORKAttrQueryRequest;
import eu.stork.peps.auth.commons.STORKAttrQueryResponse;
import eu.stork.peps.auth.commons.STORKAuthnRequest;
import eu.stork.peps.auth.commons.STORKAuthnResponse;
import eu.stork.peps.auth.commons.STORKLogoutRequest;
import eu.stork.peps.auth.commons.STORKLogoutResponse;
import eu.stork.peps.auth.engine.core.AuthenticationAttributes;
import eu.stork.peps.auth.engine.core.CitizenCountryCode;
import eu.stork.peps.auth.engine.core.CustomAttributeQuery;
import eu.stork.peps.auth.engine.core.CustomRequestAbstractType;
import eu.stork.peps.auth.engine.core.EIDCrossBorderShare;
import eu.stork.peps.auth.engine.core.EIDCrossSectorShare;
import eu.stork.peps.auth.engine.core.EIDSectorShare;
import eu.stork.peps.auth.engine.core.QAAAttribute;
import eu.stork.peps.auth.engine.core.RequestedAttribute;
import eu.stork.peps.auth.engine.core.RequestedAttributes;
import eu.stork.peps.auth.engine.core.SAMLCore;
import eu.stork.peps.auth.engine.core.SPApplication;
import eu.stork.peps.auth.engine.core.SPCountry;
import eu.stork.peps.auth.engine.core.SPID;
import eu.stork.peps.auth.engine.core.SPInformation;
import eu.stork.peps.auth.engine.core.SPInstitution;
import eu.stork.peps.auth.engine.core.SPSector;
import eu.stork.peps.auth.engine.core.VIDPAuthenticationAttributes;
import eu.stork.peps.auth.engine.core.impl.AuthenticationAttributesBuilder;
import eu.stork.peps.auth.engine.core.impl.AuthenticationAttributesMarshaller;
import eu.stork.peps.auth.engine.core.impl.AuthenticationAttributesUnmarshaller;
import eu.stork.peps.auth.engine.core.impl.CitizenCountryCodeBuilder;
import eu.stork.peps.auth.engine.core.impl.CitizenCountryCodeMarshaller;
import eu.stork.peps.auth.engine.core.impl.CitizenCountryCodeUnmarshaller;
import eu.stork.peps.auth.engine.core.impl.EIDCrossBorderShareBuilder;
import eu.stork.peps.auth.engine.core.impl.EIDCrossBorderShareMarshaller;
import eu.stork.peps.auth.engine.core.impl.EIDCrossBorderShareUnmarshaller;
import eu.stork.peps.auth.engine.core.impl.EIDCrossSectorShareBuilder;
import eu.stork.peps.auth.engine.core.impl.EIDCrossSectorShareMarshaller;
import eu.stork.peps.auth.engine.core.impl.EIDCrossSectorShareUnmarshaller;
import eu.stork.peps.auth.engine.core.impl.EIDSectorShareBuilder;
import eu.stork.peps.auth.engine.core.impl.EIDSectorShareMarshaller;
import eu.stork.peps.auth.engine.core.impl.EIDSectorShareUnmarshaller;
import eu.stork.peps.auth.engine.core.impl.QAAAttributeBuilder;
import eu.stork.peps.auth.engine.core.impl.QAAAttributeMarshaller;
import eu.stork.peps.auth.engine.core.impl.QAAAttributeUnmarshaller;
import eu.stork.peps.auth.engine.core.impl.RequestedAttributeBuilder;
import eu.stork.peps.auth.engine.core.impl.RequestedAttributeMarshaller;
import eu.stork.peps.auth.engine.core.impl.RequestedAttributeUnmarshaller;
import eu.stork.peps.auth.engine.core.impl.RequestedAttributesBuilder;
import eu.stork.peps.auth.engine.core.impl.RequestedAttributesMarshaller;
import eu.stork.peps.auth.engine.core.impl.RequestedAttributesUnmarshaller;
import eu.stork.peps.auth.engine.core.impl.SPApplicationBuilder;
import eu.stork.peps.auth.engine.core.impl.SPApplicationMarshaller;
import eu.stork.peps.auth.engine.core.impl.SPApplicationUnmarshaller;
import eu.stork.peps.auth.engine.core.impl.SPCountryBuilder;
import eu.stork.peps.auth.engine.core.impl.SPCountryMarshaller;
import eu.stork.peps.auth.engine.core.impl.SPCountryUnmarshaller;
import eu.stork.peps.auth.engine.core.impl.SPIDBuilder;
import eu.stork.peps.auth.engine.core.impl.SPIDMarshaller;
import eu.stork.peps.auth.engine.core.impl.SPIDUnmarshaller;
import eu.stork.peps.auth.engine.core.impl.SPInformationBuilder;
import eu.stork.peps.auth.engine.core.impl.SPInformationMarshaller;
import eu.stork.peps.auth.engine.core.impl.SPInformationUnmarshaller;
import eu.stork.peps.auth.engine.core.impl.SPInstitutionBuilder;
import eu.stork.peps.auth.engine.core.impl.SPInstitutionMarshaller;
import eu.stork.peps.auth.engine.core.impl.SPInstitutionUnmarshaller;
import eu.stork.peps.auth.engine.core.impl.SPSectorBuilder;
import eu.stork.peps.auth.engine.core.impl.SPSectorMarshaller;
import eu.stork.peps.auth.engine.core.impl.SPSectorUnmarshaller;
import eu.stork.peps.auth.engine.core.impl.VIDPAuthenticationAttributesBuilder;
import eu.stork.peps.auth.engine.core.impl.VIDPAuthenticationAttributesMarshaller;
import eu.stork.peps.auth.engine.core.impl.VIDPAuthenticationAttributesUnmarshaller;
import eu.stork.peps.auth.engine.core.validator.CustomAttributeQueryValidator;
import eu.stork.peps.auth.engine.core.validator.ExtensionsSchemaValidator;
import eu.stork.peps.auth.engine.core.validator.QAAAttributeSchemaValidator;
import eu.stork.peps.exceptions.SAMLEngineException;
import eu.stork.peps.exceptions.STORKSAMLEngineException;
import eu.stork.peps.exceptions.STORKSAMLEngineRuntimeException;

/**
 * Class that wraps the operations over SAML tokens, both generation and
 * validation of SAML STORK requests and SAML STORK responses. Complaint with
 * "OASIS Secure Assertion Markup Language (SAML) 2.0, May 2005", but taking
 * into account STORK specific requirements.
 * 
 * @author fjquevedo
 * @author iinigo
 */
public final class STORKSAMLEngine extends SAMLEngine {

	/** The Constant LOG. */
	private static final Logger LOG = LoggerFactory
			.getLogger(STORKSAMLEngine.class.getName());

	private static final String ATTRIBUTE_EMPTY_LITERAL = "Attribute name is null or empty.";
	/**
	 * Gets the single instance of STORKSAMLEngine.
	 * 
	 * @param nameInstance the name instance
	 * 
	 * @return single instance of STORKSAMLEngine
	 */
	public static synchronized STORKSAMLEngine getInstance(
			final String nameInstance) {
		STORKSAMLEngine engine = null;
		LOG.info("Get instance: " + nameInstance);
		try {
			engine = new STORKSAMLEngine(nameInstance.trim());
		} catch (Exception e) {
			LOG.error("Error get instance: " + nameInstance);
		}
		return engine;
	}

	/**
	 * Instantiate a new STORKSAML engine.
	 * 
	 * @param nameInstance the name instance
	 * 
	 * @throws STORKSAMLEngineException the STORKSAML engine exception
	 */
	private STORKSAMLEngine(final String nameInstance)
			throws STORKSAMLEngineException {
		// Initialization OpenSAML.
		super(nameInstance);
		LOG.info("Register STORK objects provider.");
		Configuration.registerObjectProvider(QAAAttribute.DEF_ELEMENT_NAME,
				new QAAAttributeBuilder(), new QAAAttributeMarshaller(),
				new QAAAttributeUnmarshaller());

		Configuration.registerObjectProvider(EIDSectorShare.DEF_ELEMENT_NAME,
				new EIDSectorShareBuilder(), new EIDSectorShareMarshaller(),
				new EIDSectorShareUnmarshaller());

		Configuration.registerObjectProvider(
				EIDCrossSectorShare.DEF_ELEMENT_NAME,
				new EIDCrossSectorShareBuilder(),
				new EIDCrossSectorShareMarshaller(),
				new EIDCrossSectorShareUnmarshaller());

		Configuration.registerObjectProvider(
				EIDCrossBorderShare.DEF_ELEMENT_NAME,
				new EIDCrossBorderShareBuilder(),
				new EIDCrossBorderShareMarshaller(),
				new EIDCrossBorderShareUnmarshaller());

		Configuration.registerObjectProvider(SPSector.DEF_ELEMENT_NAME,
				new SPSectorBuilder(), new SPSectorMarshaller(),
				new SPSectorUnmarshaller());

		Configuration.registerObjectProvider(SPInstitution.DEF_ELEMENT_NAME,
				new SPInstitutionBuilder(), new SPInstitutionMarshaller(),
				new SPInstitutionUnmarshaller());

		Configuration.registerObjectProvider(SPApplication.DEF_ELEMENT_NAME,
				new SPApplicationBuilder(), new SPApplicationMarshaller(),
				new SPApplicationUnmarshaller());

		Configuration.registerObjectProvider(SPCountry.DEF_ELEMENT_NAME,
				new SPCountryBuilder(), new SPCountryMarshaller(),
				new SPCountryUnmarshaller());

		Configuration.registerObjectProvider(XSAny.TYPE_NAME,
				new XSAnyBuilder(), new XSAnyMarshaller(),
				new XSAnyUnmarshaller());

		Configuration.registerObjectProvider(
				RequestedAttribute.DEF_ELEMENT_NAME,
				new RequestedAttributeBuilder(),
				new RequestedAttributeMarshaller(),
				new RequestedAttributeUnmarshaller());

		Configuration.registerObjectProvider(
				RequestedAttributes.DEF_ELEMENT_NAME,
				new RequestedAttributesBuilder(),
				new RequestedAttributesMarshaller(),
				new RequestedAttributesUnmarshaller());

		Configuration.registerObjectProvider(
				AuthenticationAttributes.DEF_ELEMENT_NAME,
				new AuthenticationAttributesBuilder(),
				new AuthenticationAttributesMarshaller(),
				new AuthenticationAttributesUnmarshaller());

		Configuration.registerObjectProvider(
				VIDPAuthenticationAttributes.DEF_ELEMENT_NAME,
				new VIDPAuthenticationAttributesBuilder(),
				new VIDPAuthenticationAttributesMarshaller(),
				new VIDPAuthenticationAttributesUnmarshaller());

		Configuration.registerObjectProvider(
				CitizenCountryCode.DEF_ELEMENT_NAME,
				new CitizenCountryCodeBuilder(),
				new CitizenCountryCodeMarshaller(),
				new CitizenCountryCodeUnmarshaller());

		Configuration.registerObjectProvider(
				SPID.DEF_ELEMENT_NAME,
				new SPIDBuilder(),
				new SPIDMarshaller(),
				new SPIDUnmarshaller());

		Configuration.registerObjectProvider(
				SPInformation.DEF_ELEMENT_NAME,
				new SPInformationBuilder(),
				new SPInformationMarshaller(),
				new SPInformationUnmarshaller());

		LOG.info("Register STORK object validators.");
		final ValidatorSuite validatorSuite = new ValidatorSuite(
				QAAAttribute.DEF_LOCAL_NAME);

		validatorSuite.registerValidator(QAAAttribute.DEF_ELEMENT_NAME,
				new QAAAttributeSchemaValidator());
		final Extensions extensions = SAMLEngineUtils.generateExtension();
		validatorSuite.registerValidator(extensions.getElementQName(),
				new ExtensionsSchemaValidator());

		Configuration.registerValidatorSuite(
				"stork:QualityAuthenticationAssuranceLevel", validatorSuite);

	}

	/**
	 * Generate authentication response base.
	 * 
	 * @param status the status
	 * @param assertConsumerURL the assert consumer URL.
	 * @param inResponseTo the in response to
	 * 
	 * @return the response
	 * 
	 * @throws STORKSAMLEngineException the STORKSAML engine exception
	 */
	private Response genAuthnRespBase(final Status status,
			final String assertConsumerURL, final String inResponseTo)
					throws STORKSAMLEngineException {
		return genAuthnRespBase(status, assertConsumerURL, inResponseTo, null);
	}

	/**
	 * Generate authentication response base.
	 * 
	 * @param status the status
	 * @param assertConsumerURL the assert consumer URL.
	 * @param inResponseTo the in response to
	 * 
	 * @return the response
	 * 
	 * @throws STORKSAMLEngineException the STORKSAML engine exception
	 */
	private Response genAuthnRespBase(final Status status,
			final String assertConsumerURL, final String inResponseTo, final String issuerVal)
					throws STORKSAMLEngineException {
		LOG.debug("Generate Authentication Response base.");
		final Response response = SAMLEngineUtils.generateResponse(
				SAMLVersion.VERSION_20, SAMLEngineUtils.generateNCName(),
				SAMLEngineUtils.getCurrentTime(), status);

		// Set name Spaces
		this.setNameSpaces(response);

		// Mandatory STORK
		LOG.debug("Generate Issuer");
		final Issuer issuer = SAMLEngineUtils.generateIssuer();

		if (issuerVal == null) {
			issuer.setValue(super.getSamlCoreProperties().getResponder());
		} else {
			issuer.setValue(issuerVal);
		}

		// Format Entity Optional STORK
		issuer.setFormat(super.getSamlCoreProperties().getFormatEntity());
		response.setIssuer(issuer);

		// destination Mandatory Stork
		response.setDestination(assertConsumerURL.trim());

		// inResponseTo Mandatory Stork
		response.setInResponseTo(inResponseTo.trim());

		// Optional STORK
		response.setConsent(super.getSamlCoreProperties()
				.getConsentAuthnResponse());

		return response;
	}

	/**
	 * Generate attribute query response base.
	 * 
	 * @param status the status
	 * @param destinationURL the assert consumer URL.
	 * @param inResponseTo the in response to
	 * 
	 * @return the response
	 * 
	 * @throws STORKSAMLEngineException the STORKSAML engine exception
	 */
	private Response genAttrQueryRespBase(final Status status,
			final String destinationURL, final String inResponseTo)
					throws STORKSAMLEngineException {
		LOG.debug("Generate Attribute query Response base.");
		final Response response = SAMLEngineUtils.generateResponse(
				SAMLVersion.VERSION_20, SAMLEngineUtils.generateNCName(),
				SAMLEngineUtils.getCurrentTime(), status);

		// Set name Spaces
		this.setNameSpaces(response);

		// Mandatory STORK
		LOG.debug("Generate Issuer");
		final Issuer issuer = SAMLEngineUtils.generateIssuer();
		issuer.setValue(super.getSamlCoreProperties().getResponder());

		// Format Entity Optional STORK
		issuer.setFormat(super.getSamlCoreProperties().getFormatEntity());

		response.setIssuer(issuer);

		// destination Mandatory Stork
		response.setDestination(destinationURL.trim());

		// inResponseTo Mandatory Stork
		response.setInResponseTo(inResponseTo.trim());

		// Optional STORK
		response.setConsent(super.getSamlCoreProperties()
				.getConsentAuthnResponse());

		return response;
	}

	/**
	 * Generate assertion.
	 * 
	 * @param ipAddress the IP address.
	 * @param assertConsumerURL the assert consumer URL.
	 * @param inResponseTo the in response to
	 * @param issuer the issuer
	 * @param notOnOrAfter the not on or after
	 * 
	 * @return the assertion
	 * 
	 * @throws STORKSAMLEngineException the STORKSAML engine exception
	 */
	private Assertion generateAssertion(final String ipAddress,
			final String assertConsumerURL, final String inResponseTo,
			final String issuer, final DateTime notOnOrAfter)
					throws STORKSAMLEngineException {

		return generateAssertion(ipAddress, assertConsumerURL, inResponseTo, issuer, notOnOrAfter, null);
	}

	/**
	 * Generate assertion.
	 * 
	 * @param ipAddress the IP address.
	 * @param assertConsumerURL the assert consumer URL.
	 * @param inResponseTo the in response to
	 * @param issuer the issuer
	 * @param notOnOrAfter the not on or after
	 * 
	 * @return the assertion
	 * 
	 * @throws STORKSAMLEngineException the STORKSAML engine exception
	 */
	private Assertion generateAssertion(final String ipAddress,
			final String assertConsumerURL, final String inResponseTo,
			final String issuer, final DateTime notOnOrAfter, String issuerVal)
					throws STORKSAMLEngineException {
		LOG.info("Generate Assertion.");

		// Mandatory STORK
		LOG.debug("Generate Issuer to Assertion");
		final Issuer issuerAssertion = SAMLEngineUtils.generateIssuer();
		if (issuerVal == null) {
			issuerAssertion.setValue(super.getSamlCoreProperties().getResponder());
		} else {
			issuerAssertion.setValue(issuerVal);
		}

		// Format Entity Optional STORK
		issuerAssertion.setFormat(super.getSamlCoreProperties()
				.getFormatEntity());

		final Assertion assertion = SAMLEngineUtils.generateAssertion(
				SAMLVersion.VERSION_20, SAMLEngineUtils.generateNCName(),
				SAMLEngineUtils.getCurrentTime(), issuerAssertion);

		final Subject subject = SAMLEngineUtils.generateSubject();

		// Mandatory STORK verified
		// String format = NameID.UNSPECIFIED
		// specification: 'SAML:2.0' exist
		// opensaml: "urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified"
		// opensaml  "urn:oasis:names:tc:SAML:2.0:nameid-format:unspecified"
		final String format = "urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified";

		final String nameQualifier = "";

		LOG.debug("Generate NameID");
		final NameID nameId = SAMLEngineUtils.generateNameID(super
				.getSamlCoreProperties().getResponder(), format, nameQualifier);
		nameId.setValue(format);
		subject.setNameID(nameId);

		// Mandatory if urn:oasis:names:tc:SAML:2.0:cm:bearer.
		// Optional in other case.
		LOG.debug("Generate SubjectConfirmationData.");
		final SubjectConfirmationData dataBearer = SAMLEngineUtils
				.generateSubjectConfirmationData(SAMLEngineUtils
						.getCurrentTime(), assertConsumerURL, inResponseTo);

		// Mandatory if urn:oasis:names:tc:SAML:2.0:cm:bearer.
		// Optional in other case.
		LOG.debug("Generate SubjectConfirmation");
		final SubjectConfirmation subjectConf = SAMLEngineUtils
				.generateSubjectConfirmation(SubjectConfirmation.METHOD_BEARER,
						dataBearer);

		final ArrayList<SubjectConfirmation> listSubjectConf = new ArrayList<SubjectConfirmation>();
		listSubjectConf.add(subjectConf);

		for (final Iterator<SubjectConfirmation> iter = listSubjectConf
				.iterator(); iter.hasNext();) {
			final SubjectConfirmation element = iter.next();

			if (SubjectConfirmation.METHOD_BEARER.equals(element.getMethod())) {
				// ipAddress Mandatory if method is Bearer.

				if (StringUtils.isBlank(ipAddress)) {
					throw new STORKSAMLEngineException(
							"ipAddress is null or empty");
				}
				element.getSubjectConfirmationData().setAddress(
						ipAddress.trim());
			}

			element.getSubjectConfirmationData()
			.setRecipient(assertConsumerURL);
			element.getSubjectConfirmationData().setNotOnOrAfter(notOnOrAfter);
		}

		// The SAML 2.0 specification allows multiple SubjectConfirmations
		subject.getSubjectConfirmations().addAll(listSubjectConf);

		// Mandatory Stork
		assertion.setSubject(subject);

		// Conditions that MUST be evaluated when assessing the validity of
		// and/or when using the assertion.
		final Conditions conditions = this.generateConditions(SAMLEngineUtils
				.getCurrentTime(), notOnOrAfter, issuer);

		assertion.setConditions(conditions);

		LOG.debug("Generate stork Authentication Statement.");
		final AuthnStatement storkAuthnStat = this
				.generateStorkAuthStatement(ipAddress);
		assertion.getAuthnStatements().add(storkAuthnStat);

		return assertion;
	}

	private String getAttributeName(final PersonalAttribute attribute) throws STORKSAMLEngineException {
		if (StringUtils.isBlank(attribute.getName())) {
			LOG.error(ATTRIBUTE_EMPTY_LITERAL);
			throw new STORKSAMLEngineException(ATTRIBUTE_EMPTY_LITERAL);
		}

		final String attributeName = super.getSamlCoreProperties()
				.getProperty(attribute.getName());

		if (StringUtils.isBlank(attributeName)) {
			LOG.error("Attribute name: {} it is not known.", attribute
					.getName());
			throw new STORKSAMLEngineException("Attribute name: "
					+ attribute.getName() + " it is not known.");
		}
		return attributeName;
	}
	/**
	 * Generate attribute statement.
	 * 
	 * @param personalAttrList the personal attribute list
	 * @param isHashing the is hashing
	 * 
	 * @return the attribute statement
	 * 
	 * @throws STORKSAMLEngineException the STORKSAML engine exception
	 * @throws IOException
	 */
	private AttributeStatement generateAttributeStatement(
			final IPersonalAttributeList personalAttrList,
			final boolean isHashing) throws STORKSAMLEngineException {
		LOG.debug("Generate attribute statement");

		final AttributeStatement attrStatement = (AttributeStatement) SAMLEngineUtils
				.createSamlObject(AttributeStatement.DEFAULT_ELEMENT_NAME);

		for (PersonalAttribute attribute : personalAttrList) {

			String attributeName = getAttributeName(attribute);

			// Verification that only one value it's permitted, simple or
			// complex, not both.

			final boolean simpleNull = (attribute.getValue() == null);
			final boolean simpleEmpty = (simpleNull || (!simpleNull && attribute
					.getValue().isEmpty()));

			final boolean complexNull = (attribute.getComplexValue() == null);
			final boolean complexEmpty = (complexNull || (!complexNull && attribute
					.getComplexValue().isEmpty()));

			if ((!simpleEmpty && !complexEmpty)) {
				throw new STORKSAMLEngineException(
						"Attribute name: "
								+ attribute.getName()
								+ " must be contain one value, simple or complex value.");
			} else {

				if (!simpleEmpty) {
					attrStatement.getAttributes().add(
							this.generateAttrSimple(attributeName, attribute
									.getStatus(), attribute.getValue(),
									isHashing));
				} else if (!complexEmpty) {
					attrStatement.getAttributes().add(
							SAMLEngineUtils.generateAttrComplex(attributeName,
									attribute.getStatus(), attribute
									.getComplexValue(), isHashing));
				} else if (!simpleNull) {
					attrStatement.getAttributes().add(
							this.generateAttrSimple(attributeName, attribute
									.getStatus(), new ArrayList<String>(),
									isHashing));
				} else {
					// Add attribute complex.
					attrStatement.getAttributes().add(
							SAMLEngineUtils.generateAttrComplex(attributeName,
									attribute.getStatus(),
									new HashMap<String, String>(), isHashing));
				}
			}
		}
		return attrStatement;
	}
	private XSAny createAttributeValueForSignedDoc(final String value, final boolean isHashing) throws STORKSAMLEngineException {
		DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
		domFactory.setNamespaceAware(true);
		Document document = null;
		DocumentBuilder builder;

		// Parse the signedDoc value into an XML DOM Document
		try {
			builder = domFactory.newDocumentBuilder();
			InputStream is;
			is = new ByteArrayInputStream(value.trim().getBytes("UTF-8"));
			document = builder.parse(is);
			is.close();
		} catch (SAXException e1) {
			LOG.error("SAX Error while parsing signModule attribute", e1);
			throw new STORKSAMLEngineRuntimeException(e1);
		} catch (ParserConfigurationException e2) {
			LOG.error("Parser Configuration Error while parsing signModule attribute", e2);
			throw new STORKSAMLEngineRuntimeException(e2);
		} catch (UnsupportedEncodingException e3) {
			LOG.error("Unsupported encoding Error while parsing signModule attribute", e3);
			throw new STORKSAMLEngineRuntimeException(e3);
		} catch (IOException e4) {
			LOG.error("IO Error while parsing signModule attribute", e4);
			throw new STORKSAMLEngineRuntimeException(e4);
		}

		// Create the attribute statement
		final XSAny xmlValue = (XSAny) SAMLEngineUtils
				.createSamlObject(
						AttributeValue.DEFAULT_ELEMENT_NAME,
						XSAny.TYPE_NAME);

		//Set the signedDoc XML content to this element
		xmlValue.setDOM(document.getDocumentElement());

		// Create the attribute statement
		final XSAny attrValue = (XSAny) SAMLEngineUtils
				.createSamlObject(
						AttributeValue.DEFAULT_ELEMENT_NAME,
						XSAny.TYPE_NAME);

		//Add previous signedDocXML to the AttributeValue Element

		// if it's necessary encode the information.
		if (!isHashing) {
			attrValue.getUnknownXMLObjects().add(xmlValue);
		}
		return attrValue;
	}

	private XSAny createAttributeValueForNonSignedDoc(final String value, final boolean isHashing) throws STORKSAMLEngineException {
		// Create the attribute statement
		final XSAny attrValue = (XSAny) SAMLEngineUtils
				.createSamlObject(
						AttributeValue.DEFAULT_ELEMENT_NAME,
						XSAny.TYPE_NAME);
		// if it's necessary encode the information.
		if (isHashing) {
			attrValue.setTextContent(SAMLEngineUtils.encode(value, SAMLEngineUtils.SHA_512));
		} else {
			attrValue.setTextContent(value);
		}
		return attrValue;
	}

	/**
	 * Generate attribute from a list of values.
	 * 
	 * @param name the name of the attribute.
	 * @param values the value of the attribute.
	 * @param isHashing the is hashing with "SHA-512" algorithm.
	 * @param status the status of the parameter: "Available", "NotAvailable" or
	 *            "Withheld".
	 * 
	 * @return the attribute
	 * 
	 * @throws STORKSAMLEngineException the STORKSAML engine exception
	 */
	private Attribute generateAttrSimple(final String name,
			final String status, final List<String> values,
			final boolean isHashing) throws STORKSAMLEngineException {
		LOG.debug("Generate attribute simple: " + name);
		final Attribute attribute = (Attribute) SAMLEngineUtils
				.createSamlObject(Attribute.DEFAULT_ELEMENT_NAME);

		attribute.setName(name);
		attribute.setNameFormat(Attribute.URI_REFERENCE);

		attribute.getUnknownAttributes().put(
				new QName(SAMLCore.STORK10_NS.getValue(), "AttributeStatus",
						SAMLCore.STORK10_PREFIX.getValue()), status);

		if (values != null) {
			LOG.debug("Add attribute values.");
			for (int i = 0; i < values.size(); i++) {
				final String value = values.get(i);
				if (StringUtils.isNotBlank(value)) {
					XSAny attrValue = null;
					if (!name.equals("http://www.stork.gov.eu/1.0/signedDoc")) {
						// Create the attribute statement
						attrValue = createAttributeValueForNonSignedDoc(value, isHashing);

					} else {
						attrValue = createAttributeValueForSignedDoc(value, isHashing);
						attribute.getAttributeValues().add(attrValue);
					}
					attribute.getAttributeValues().add(attrValue);
				}
			}
		}
		return attribute;
	}

	/**
	 * Generate conditions that MUST be evaluated when assessing the validity of
	 * and/or when using the assertion.
	 * 
	 * @param notBefore the not before
	 * @param notOnOrAfter the not on or after
	 * @param audienceURI the audience URI.
	 * 
	 * @return the conditions
	 */
	private Conditions generateConditions(final DateTime notBefore,
			final DateTime notOnOrAfter, final String audienceURI) {
		LOG.debug("Generate conditions.");
		final Conditions conditions = (Conditions) SAMLEngineUtils
				.createSamlObject(Conditions.DEFAULT_ELEMENT_NAME);
		conditions.setNotBefore(notBefore);
		conditions.setNotOnOrAfter(notOnOrAfter);

		final AudienceRestriction restrictions = (AudienceRestriction) SAMLEngineUtils
				.createSamlObject(AudienceRestriction.DEFAULT_ELEMENT_NAME);

		final Audience audience = (Audience) SAMLEngineUtils
				.createSamlObject(Audience.DEFAULT_ELEMENT_NAME);
		audience.setAudienceURI(audienceURI);

		restrictions.getAudiences().add(audience);
		conditions.getAudienceRestrictions().add(restrictions);

		if (super.getSamlCoreProperties().isOneTimeUse()) {
			final OneTimeUse oneTimeUse = (OneTimeUse) SAMLEngineUtils
					.createSamlObject(OneTimeUse.DEFAULT_ELEMENT_NAME);
			conditions.getConditions().add(oneTimeUse);
		}
		return conditions;
	}

	/**
	 * Generate personal attribute list.
	 * 
	 * @param assertion the assertion
	 * 
	 * @return the personal attribute list
	 * 
	 * @throws STORKSAMLEngineException the STORKSAML engine exception
	 */
	private IPersonalAttributeList generatePersonalAttributeList(
			final Assertion assertion) throws STORKSAMLEngineException {
		LOG.debug("Generate personal attribute list from XMLObject.");
		final List<XMLObject> listExtensions = assertion.getOrderedChildren();

		boolean find = false;
		AttributeStatement requestedAttr = null;

		// Search the attribute statement.
		for (int i = 0; i < listExtensions.size() && !find; i++) {
			final XMLObject xml = listExtensions.get(i);
			if (xml instanceof AttributeStatement) {
				requestedAttr = (AttributeStatement) xml;
				find = true;
			}
		}

		if (!find) {
			LOG.error("Error: AttributeStatement it's not present.");
			throw new STORKSAMLEngineException(
					"AttributeStatement it's not present.");
		}

		final List<Attribute> reqAttrs = requestedAttr.getAttributes();

		final IPersonalAttributeList personalAttrList = new PersonalAttributeList();
		String attributeName;

		// Process the attributes.
		for (int nextAttribute = 0; nextAttribute < reqAttrs.size(); nextAttribute++) {
			final Attribute attribute = reqAttrs.get(nextAttribute);

			final PersonalAttribute personalAttribute = new PersonalAttribute();

			attributeName = attribute.getName();
			personalAttribute.setName(attributeName.substring(attributeName
					.lastIndexOf('/') + 1));

			personalAttribute.setStatus(attribute.getUnknownAttributes().get(
					new QName(SAMLCore.STORK10_NS.getValue(),
							"AttributeStatus", SAMLCore.STORK10_PREFIX
							.getValue())));

			final ArrayList<String> simpleValues = new ArrayList<String>();
			final HashMap<String, String> multiValues = new HashMap<String, String>();

			final List<XMLObject> values = attribute.getOrderedChildren();


			// Process the values.
			for (int nextValue = 0; nextValue < values.size(); nextValue++) {

				final XMLObject xmlObject = values.get(nextValue);

				if (xmlObject instanceof XSStringImpl) {

					simpleValues.add(((XSStringImpl) xmlObject).getValue());

				} else if (xmlObject instanceof XSAnyImpl) {

					if (attributeName.equals("http://www.stork.gov.eu/1.0/signedDoc")) {

						final XSAnyImpl xmlString = (XSAnyImpl) values
								.get(nextValue);

						TransformerFactory transFactory = TransformerFactory
								.newInstance();
						Transformer transformer = null;
						try {
							transformer = transFactory.newTransformer();
							transformer.setOutputProperty(
									OutputKeys.OMIT_XML_DECLARATION, "yes");
						} catch (TransformerConfigurationException e) {
							LOG.error("Error transformer configuration exception", e);
						}
						StringWriter buffer = new StringWriter();
						try {
							if (xmlString != null && xmlString.getUnknownXMLObjects() != null && xmlString.getUnknownXMLObjects().size() > 0 ){
								transformer.transform(new DOMSource(xmlString
										.getUnknownXMLObjects().get(0).getDOM()),
										new StreamResult(buffer));
							}
						} catch (TransformerException e) {
							LOG.error("Error transformer exception", e);
						}
						String str = buffer.toString();

						simpleValues.add(str);

					} else if (isComplex(xmlObject))
					{
						LOG.info(attributeName + " found");
						// Process complex value.
						final XSAnyImpl complexValue = (XSAnyImpl) xmlObject;

						for (int nextComplexValue = 0; nextComplexValue < complexValue
								.getUnknownXMLObjects().size(); nextComplexValue++) {

							final XSAnyImpl simple = (XSAnyImpl) complexValue
									.getUnknownXMLObjects().get(
											nextComplexValue);

							multiValues.put(simple.getElementQName()
									.getLocalPart(), simple.getTextContent());
						}

					} 					
					else {
						// Process simple value.
						simpleValues.add(((XSAnyImpl) xmlObject)
								.getTextContent());
					}

				} else {
					LOG.error("Error: attribute value it's unknown.");
					throw new STORKSAMLEngineException(
							"Attribute value it's unknown.");
				}
			}

			personalAttribute.setValue(simpleValues);
			personalAttribute.setComplexValue(multiValues);
			personalAttrList.add(personalAttribute);
		}

		return personalAttrList;
	}

	/**
	 * Generate stork authentication request.
	 * 
	 * @param request the request that contain all parameters for generate an
	 *            authentication request.
	 * 
	 * @return the STORK authentication request that has been processed.
	 * 
	 * @throws STORKSAMLEngineException the STORKSAML engine exception
	 */
	public STORKAuthnRequest generateSTORKAuthnRequest(
			final STORKAuthnRequest request) throws STORKSAMLEngineException {
		LOG.info("Generate SAMLAuthnRequest.");

		// Validate Parameters mandatories
		validateParamAuthnReq(request);

		final AuthnRequest authnRequestAux = SAMLEngineUtils
				.generateSAMLAuthnRequest(SAMLEngineUtils.generateNCName(),
						SAMLVersion.VERSION_20, SAMLEngineUtils
						.getCurrentTime());

		// Set name spaces.
		setNameSpaces(authnRequestAux);

		// Add parameter Mandatory STORK
		authnRequestAux.setForceAuthn(request.getForceAuthN());

		// Add parameter Mandatory STORK
		authnRequestAux.setIsPassive(Boolean.FALSE);

		authnRequestAux.setAssertionConsumerServiceURL(request
				.getAssertionConsumerServiceURL());

		authnRequestAux.setProviderName(request.getProviderName());

		// Add protocol binding
		authnRequestAux.setProtocolBinding(super.getSamlCoreProperties()
				.getProtocolBinding());

		// Add parameter optional STORK
		// Destination is mandatory if the destination is a C-PEPS
		// The application must to know if the destination is a C-PEPS.
		if (StringUtils.isNotBlank(request.getDestination())) {
			authnRequestAux.setDestination(request.getDestination());
		}

		// Consent is optional. Set from SAMLEngine.xml - consent.
		authnRequestAux.setConsent(super.getSamlCoreProperties()
				.getConsentAuthnRequest());

		final Issuer issuer = SAMLEngineUtils.generateIssuer();

		if(request.getIssuer()!=null){
			issuer.setValue(request.getIssuer());
		} else {
			issuer.setValue(super.getSamlCoreProperties().getRequester());
		}

		// Optional STORK
		final String formatEntity = super.getSamlCoreProperties()
				.getFormatEntity();
		if (StringUtils.isNotBlank(formatEntity)) {
			issuer.setFormat(formatEntity);
		}

		authnRequestAux.setIssuer(issuer);

		// Generate stork extensions.
		final Extensions storkExtensions = this
				.generateSTORKExtensions(request);
		// add the extensions to the SAMLAuthnRequest
		authnRequestAux.setExtensions(storkExtensions);

		// the result contains an authentication request token (byte[]),
		// identifier of the token, and all parameters from the request.
		final STORKAuthnRequest authRequest = processExtensions(authnRequestAux
				.getExtensions());

		if (request.getSamlId() != null){
			authnRequestAux.setID(request.getSamlId());
		}

		try {
			authRequest.setTokenSaml(super.signAndMarshall(authnRequestAux));
		} catch (SAMLEngineException e) {
			LOG.error("Sign and Marshall.", e);
			throw new STORKSAMLEngineException(e);
		}

		authRequest.setSamlId(authnRequestAux.getID());
		authRequest.setDestination(authnRequestAux.getDestination());
		authRequest.setAssertionConsumerServiceURL(authnRequestAux
				.getAssertionConsumerServiceURL());

		authRequest.setProviderName(authnRequestAux.getProviderName());
		authRequest.setIssuer(authnRequestAux.getIssuer().getValue());

		return authRequest;
	}

	/**
	 * Generate stork authentication response.
	 * 
	 * @param request the request
	 * @param responseAuthReq the response authentication request
	 * @param ipAddress the IP address
	 * @param isHashing the is hashing
	 * 
	 * @return the sTORK authentication response
	 * 
	 * @throws STORKSAMLEngineException the STORKSAML engine exception
	 */
	public STORKAuthnResponse generateSTORKAuthnResponse(
			final STORKAuthnRequest request,
			final STORKAuthnResponse responseAuthReq, final String ipAddress,
			final boolean isHashing) throws STORKSAMLEngineException {
		return generateSTORKAuthnResponse(request, responseAuthReq, ipAddress, isHashing, null);
	}

	/**
	 * Generate stork authentication response.
	 * 
	 * @param request the request
	 * @param responseAuthReq the response authentication request
	 * @param ipAddress the IP address
	 * @param isHashing the is hashing
	 * 
	 * @return the sTORK authentication response
	 * 
	 * @throws STORKSAMLEngineException the STORKSAML engine exception
	 */
	public STORKAuthnResponse generateSTORKAuthnResponse(
			final STORKAuthnRequest request,
			final STORKAuthnResponse responseAuthReq, final String ipAddress,
			final boolean isHashing, String issuer) throws STORKSAMLEngineException {
		LOG.info("generateSTORKAuthnResponse");

		// Validate parameters
		validateParamResponse(request, responseAuthReq);

		// Mandatory SAML
		LOG.debug("Generate StatusCode");
		final StatusCode statusCode = SAMLEngineUtils
				.generateStatusCode(StatusCode.SUCCESS_URI);

		LOG.debug("Generate Status");
		final Status status = SAMLEngineUtils.generateStatus(statusCode);

		LOG.debug("Generate StatusMessage");
		final StatusMessage statusMessage = (StatusMessage) SAMLEngineUtils
				.generateStatusMessage(StatusCode.SUCCESS_URI);

		status.setStatusMessage(statusMessage);

		LOG.debug("Generate Response");

		// RESPONSE
		final Response response = genAuthnRespBase(status, request
				.getAssertionConsumerServiceURL(), request.getSamlId(), issuer);

		DateTime notOnOrAfter = null;
		
		if (responseAuthReq.getNotOnOrAfter() != null)  {
			notOnOrAfter = responseAuthReq.getNotOnOrAfter();
		} else {
			notOnOrAfter = new DateTime();
			notOnOrAfter = notOnOrAfter.plusSeconds(super.getSamlCoreProperties()
				.getTimeNotOnOrAfter());
		}

		final Assertion assertion = this.generateAssertion(ipAddress, request
				.getAssertionConsumerServiceURL(), request.getSamlId(), request
				.getIssuer(), notOnOrAfter, issuer);

		final AttributeStatement attrStatement = this
				.generateAttributeStatement(responseAuthReq
						.getPersonalAttributeList(), isHashing);

		assertion.getAttributeStatements().add(attrStatement);

		// Add assertions
		response.getAssertions().add(assertion);

		final STORKAuthnResponse authresponse = new STORKAuthnResponse();

		try {
			authresponse.setTokenSaml(super.signAndMarshall(response));
			authresponse.setSamlId(response.getID());
		} catch (SAMLEngineException e) {
			LOG.error("Sign and Marshall.", e);
			throw new STORKSAMLEngineException(e);
		}

		return authresponse;
	}

	/**
	 * Generate stork authentication response.
	 * 
	 * @param request the request
	 * @param responseAuthReq the response authentication request
	 * @param ipAddress the IP address
	 * @param isHashing the is hashing
	 * 
	 * @return the sTORK authentication response
	 * 
	 * @throws STORKSAMLEngineException the STORKSAML engine exception
	 */
	public STORKAuthnResponse generateSTORKAuthnResponseAfterQuery(
			final STORKAuthnRequest request,
			final STORKAuthnResponse responseAuthReq, final String ipAddress,
			final boolean isHashing, List<STORKAttrQueryResponse> res) throws STORKSAMLEngineException {
		LOG.info("generateSTORKAuthnResponse");

		// Validate parameters
		validateParamResponse(request, responseAuthReq);

		// Mandatory SAML
		LOG.debug("Generate StatusCode");
		final StatusCode statusCode = SAMLEngineUtils
				.generateStatusCode(StatusCode.SUCCESS_URI);

		LOG.debug("Generate Status");
		final Status status = SAMLEngineUtils.generateStatus(statusCode);

		LOG.debug("Generate StatusMessage");
		final StatusMessage statusMessage = (StatusMessage) SAMLEngineUtils
				.generateStatusMessage(StatusCode.SUCCESS_URI);

		status.setStatusMessage(statusMessage);

		LOG.debug("Generate Response");

		// RESPONSE
		final Response response = genAuthnRespBase(status, request
				.getAssertionConsumerServiceURL(), request.getSamlId());

		DateTime notOnOrAfter = new DateTime();

		notOnOrAfter = notOnOrAfter.plusSeconds(super.getSamlCoreProperties()
				.getTimeNotOnOrAfter());

		final Assertion assertion = this.generateAssertion(ipAddress, request
				.getAssertionConsumerServiceURL(), request.getSamlId(), request
				.getIssuer(), notOnOrAfter);

		final AttributeStatement attrStatement = this
				.generateAttributeStatement(responseAuthReq
						.getPersonalAttributeList(), isHashing);

		assertion.getAttributeStatements().add(attrStatement);

		// Add assertions
		response.getAssertions().add(assertion);
		// Check for response queries
		if (res != null && res.size() > 0)
		{
			//Iterate through them
			for (int i = 0; i < res.size(); i++)
			{
				//If response contains multiple assertions iterate through them as well
				if (res.get(i).getAssertions().size() > 1)
				{
					for (int j = 0; j < res.get(i).getAssertions().size(); j++)
					{
						Assertion tempAssertion = res.get(i).getAssertions().get(j);
						tempAssertion.setParent(response);
						response.getAssertions().add(tempAssertion);
					}
				} else {
					Assertion tempAssertion = res.get(i).getAssertion();
					tempAssertion.setParent(response);
					response.getAssertions().add(tempAssertion);
				}
			}
		}

		final STORKAuthnResponse authresponse = new STORKAuthnResponse();

		try {
			authresponse.setTokenSaml(super.signAndMarshall(response));
			authresponse.setSamlId(response.getID());
		} catch (SAMLEngineException e) {
			LOG.error("Sign and Marshall.", e);
			throw new STORKSAMLEngineException(e);
		}
		return authresponse;
	}

	/**
	 * Generate stork authentication response fail.
	 * 
	 * @param request the request
	 * @param response the response
	 * @param ipAddress the IP address
	 * @param isHashing the is hashing
	 * 
	 * @return the sTORK authentication response
	 * 
	 * @throws STORKSAMLEngineException the STORKSAML engine exception
	 */
	public STORKAuthnResponse generateSTORKAuthnResponseFail(
			final STORKAuthnRequest request, final STORKAuthnResponse response,
			final String ipAddress, final boolean isHashing)
					throws STORKSAMLEngineException {
		LOG.info("generateSTORKAuthnResponseFail");

		validateParamResponseFail(request, response);

		// Mandatory
		final StatusCode statusCode = SAMLEngineUtils
				.generateStatusCode(response.getStatusCode());

		// Mandatory SAML
		LOG.debug("Generate StatusCode.");
		// Subordinate code it's optional in case not covered into next codes:
		// - urn:oasis:names:tc:SAML:2.0:status:AuthnFailed
		// - urn:oasis:names:tc:SAML:2.0:status:InvalidAttrNameOrValue
		// - urn:oasis:names:tc:SAML:2.0:status:InvalidNameIDPolicy
		// - urn:oasis:names:tc:SAML:2.0:status:RequestDenied
		// - http://www.stork.gov.eu/saml20/statusCodes/QAANotSupported

		if (StringUtils.isNotBlank(response.getSubStatusCode())) {
			final StatusCode newStatusCode = SAMLEngineUtils
					.generateStatusCode(response.getSubStatusCode());
			statusCode.setStatusCode(newStatusCode);
		}

		LOG.debug("Generate Status.");
		final Status status = SAMLEngineUtils.generateStatus(statusCode);

		if (StringUtils.isNotBlank(response.getMessage())) {
			final StatusMessage statusMessage = (StatusMessage) SAMLEngineUtils
					.generateStatusMessage(response.getMessage());

			status.setStatusMessage(statusMessage);
		}

		LOG.debug("Generate Response.");
		// RESPONSE
		final Response responseFail = genAuthnRespBase(status, request
				.getAssertionConsumerServiceURL(), request.getSamlId());

		DateTime notOnOrAfter = new DateTime();

		notOnOrAfter = notOnOrAfter.plusSeconds(super.getSamlCoreProperties()
				.getTimeNotOnOrAfter());

		final Assertion assertion = this.generateAssertion(ipAddress, request
				.getAssertionConsumerServiceURL(), request.getSamlId(), request
				.getIssuer(), notOnOrAfter);

		responseFail.getAssertions().add(assertion);

		LOG.debug("Sign and Marshall ResponseFail.");

		final STORKAuthnResponse storkResponse = new STORKAuthnResponse();

		try {
			storkResponse.setTokenSaml(super.signAndMarshall(responseFail));
			storkResponse.setSamlId(responseFail.getID());
		} catch (SAMLEngineException e) {
			LOG.error("SAMLEngineException.", e);
			throw new STORKSAMLEngineException(e);
		}
		return storkResponse;
	}

	/**
	 * Generate stork attribute query request.
	 * 
	 * @param request the request that contain all parameters for generate an
	 *            attribute query request.
	 * 
	 * @return the STORK attribute query request that has been processed.
	 * 
	 * @throws STORKSAMLEngineException the STORKSAML engine exception
	 */
	public STORKAttrQueryRequest generateSTORKAttrQueryRequest(
			final STORKAttrQueryRequest request) throws STORKSAMLEngineException {
		LOG.info("Generate STORKAttrQueryRequest.");

		// Validate Parameters mandatories
		validateParamAttrQueryReq(request);

		//final AttributeQuery attrQueryRequestAux = SAMLEngineUtils
		final CustomAttributeQuery attrQueryRequestAux = SAMLEngineUtils
				.generateSAMLAttrQueryRequest(SAMLEngineUtils.generateNCName(),
						SAMLVersion.VERSION_20, SAMLEngineUtils
						.getCurrentTime());

		// Set name spaces.
		setNameSpaces(attrQueryRequestAux);


		// Add parameter optional STORK
		// Destination is mandatory if the destination is a C-PEPS
		// The application must to know if the destination is a C-PEPS.
		if (StringUtils.isNotBlank(request.getDestination())) {
			attrQueryRequestAux.setDestination(request.getDestination());
		}

		// Add parameter optional STORK
		// Consumer URL is needed if using HTTP-Post
		if (StringUtils.isNotBlank(request.getAssertionConsumerServiceURL())) {
			attrQueryRequestAux.setAssertionConsumerServiceURL(request.getAssertionConsumerServiceURL());
		}

		// Consent is optional. Set from SAMLEngine.xml - consent.
		attrQueryRequestAux.setConsent(super.getSamlCoreProperties()
				.getConsentAuthnRequest());

		final Issuer issuer = SAMLEngineUtils.generateIssuer();

		//Set the subject - needed for attribute query validation
		Subject subject = SAMLEngineUtils.generateSubject();
		SubjectConfirmationBuilder builder = new SubjectConfirmationBuilder();
		SubjectConfirmation subjectConfirmation = builder.buildObject();		
		subjectConfirmation.setMethod("urn:oasis:names:tc:SAML:2.0:cm:bearer");
		subject.getSubjectConfirmations().add(subjectConfirmation);
		attrQueryRequestAux.setSubject(subject);

		if(request.getIssuer()!=null){
			issuer.setValue(request.getIssuer());
		} else {
			issuer.setValue(super.getSamlCoreProperties().getRequester());
		}

		// Optional STORK
		final String formatEntity = super.getSamlCoreProperties()
				.getFormatEntity();
		if (StringUtils.isNotBlank(formatEntity)) {
			issuer.setFormat(formatEntity);
		}

		attrQueryRequestAux.setIssuer(issuer);

		// Generate stork extensions.
		final Extensions storkExtensions = this
				.generateSTORKAttrExtensions(request);
		// add the extensions to the SAMLAuthnRequest
		attrQueryRequestAux.setExtensions(storkExtensions);

		// the result contains an authentication request token (byte[]),
		// identifier of the token, and all parameters from the request.
		final STORKAttrQueryRequest attrQueryRequest = processAttrExtensions(attrQueryRequestAux
				.getExtensions());

		try {
			attrQueryRequest.setTokenSaml(super.signAndMarshall(attrQueryRequestAux));
		} catch (SAMLEngineException e) {
			LOG.error("Sign and Marshall.", e);
			throw new STORKSAMLEngineException(e);
		}

		attrQueryRequest.setSamlId(attrQueryRequestAux.getID());
		attrQueryRequest.setDestination(attrQueryRequestAux.getDestination());
		attrQueryRequest.setAssertionConsumerServiceURL(attrQueryRequestAux.getAssertionConsumerServiceURL());
		attrQueryRequest.setIssuer(attrQueryRequestAux.getIssuer().getValue());

		return attrQueryRequest;
	}

	/**
	 * Generate stork attribute query response.
	 * 
	 * @param request the request
	 * @param responseAttrQueryRes the response authentication request
	 * @param ipAddress the IP address
	 * @param isHashing the hashing of values
	 * 
	 * @return the sTORK authentication response
	 * 
	 * @throws STORKSAMLEngineException the STORKSAML engine exception
	 */
	public STORKAttrQueryResponse generateSTORKAttrQueryResponse(
			final STORKAttrQueryRequest request, 
			final STORKAttrQueryResponse responseAttrQueryRes, final String ipAddress,
			final String destinationUrl, final boolean isHashing) throws STORKSAMLEngineException {
		LOG.info("generateSTORKAttrQueryResponse");

		// Validate parameters
		validateParamAttrQueryResponse(request, responseAttrQueryRes);

		// Mandatory SAML
		LOG.debug("Generate StatusCode");
		final StatusCode statusCode = SAMLEngineUtils
				.generateStatusCode(StatusCode.SUCCESS_URI);

		LOG.debug("Generate Status");
		final Status status = SAMLEngineUtils.generateStatus(statusCode);

		LOG.debug("Generate StatusMessage");
		final StatusMessage statusMessage = (StatusMessage) SAMLEngineUtils
				.generateStatusMessage(StatusCode.SUCCESS_URI);

		status.setStatusMessage(statusMessage);

		LOG.debug("Generate Response");

		// RESPONSE
		final Response response = genAuthnRespBase(status, destinationUrl,
				request.getSamlId());

		DateTime notOnOrAfter = new DateTime();

		notOnOrAfter = notOnOrAfter.plusSeconds(super.getSamlCoreProperties()
				.getTimeNotOnOrAfter());

		final Assertion assertion = this.generateAssertion(ipAddress, ""
				,request.getSamlId(), request.getIssuer(), notOnOrAfter);

		final AttributeStatement attrStatement = this
				.generateAttributeStatement(responseAttrQueryRes
						.getPersonalAttributeList(), isHashing);

		assertion.getAttributeStatements().add(attrStatement);

		// Add assertions
		response.getAssertions().add(assertion);

		final STORKAttrQueryResponse attrQueryResponse = new STORKAttrQueryResponse();

		try {
			attrQueryResponse.setTokenSaml(super.signAndMarshall(response));
			attrQueryResponse.setSamlId(response.getID());
		} catch (SAMLEngineException e) {
			LOG.error("Sign and Marshall.", e);
			throw new STORKSAMLEngineException(e);
		}
		return attrQueryResponse;
	}

	/**
	 * Generate stork attribute query response from multiple assertions 
	 * 
	 * @param request the request
	 * @param responseAttrQueryRes the response to the query request
	 * @param responses the responses to include in the response (aggregation)
	 * @param ipAddress the IP address
	 * @param isHashing the hashing of values
	 * 
	 * @return the sTORK attribute query response
	 * 
	 * @throws STORKSAMLEngineException the STORKSAML engine exception
	 */
	public STORKAttrQueryResponse generateSTORKAttrQueryResponseWithAssertions(
			final STORKAttrQueryRequest request, final STORKAttrQueryResponse responseAttrQueryRes,
			final List<STORKAttrQueryResponse> responses, final String ipAddress,
			final String destinationUrl, final boolean isHashing) throws STORKSAMLEngineException {
		LOG.info("generateSTORKAttrQueryResponse");

		// Validate parameters
		validateParamAttrQueryResponse(request, responseAttrQueryRes);

		// Mandatory SAML
		LOG.debug("Generate StatusCode");
		final StatusCode statusCode = SAMLEngineUtils
				.generateStatusCode(StatusCode.SUCCESS_URI);

		LOG.debug("Generate Status");
		final Status status = SAMLEngineUtils.generateStatus(statusCode);

		LOG.debug("Generate StatusMessage");
		final StatusMessage statusMessage = (StatusMessage) SAMLEngineUtils
				.generateStatusMessage(StatusCode.SUCCESS_URI);

		status.setStatusMessage(statusMessage);

		LOG.debug("Generate Response");

		// RESPONSE
		final Response response = genAuthnRespBase(status, destinationUrl,
				request.getSamlId());

		DateTime notOnOrAfter = new DateTime();

		notOnOrAfter = notOnOrAfter.plusSeconds(super.getSamlCoreProperties()
				.getTimeNotOnOrAfter());

		final Assertion assertion = this.generateAssertion(ipAddress, ""
				,request.getSamlId(), request.getIssuer(), notOnOrAfter);

		final AttributeStatement attrStatement = this
				.generateAttributeStatement(responseAttrQueryRes
						.getPersonalAttributeList(), isHashing);

		assertion.getAttributeStatements().add(attrStatement);

		// Add the assertions from the former Query responses
		response.getAssertions().add(assertion);
		if (responses != null && responses.size() > 0)
		{
			for (int i = 0; i < responses.size(); i++)
			{
				Assertion tempAssertion = responses.get(i).getAssertion();
				tempAssertion.setParent(response);
				response.getAssertions().add(tempAssertion);
			}
		}

		final STORKAttrQueryResponse attrQueryResponse = new STORKAttrQueryResponse();

		try {
			attrQueryResponse.setTokenSaml(super.signAndMarshall(response));
			attrQueryResponse.setSamlId(response.getID());
		} catch (SAMLEngineException e) {
			LOG.error("Sign and Marshall.", e);
			throw new STORKSAMLEngineException(e);
		}
		return attrQueryResponse;
	}

	/**
	 * Generate stork attribute query response fail.
	 * 
	 * @param request the request
	 * @param response the response
	 * @param ipAddress the IP address
	 * @param isHashing the is hashing
	 * 
	 * @return the STORK attribute query response
	 * 
	 * @throws STORKSAMLEngineException the STORKSAML engine exception
	 */
	public STORKAttrQueryResponse generateSTORKAttrQueryResponseFail(
			final STORKAttrQueryRequest request, final STORKAttrQueryResponse response,
			final String ipAddress, final String destinationUrl, final boolean isHashing)
					throws STORKSAMLEngineException {
		LOG.info("generateSTORKAttrQueryResponseFail");

		validateParamAttrQueryResponseFail(request, response);

		// Mandatory
		final StatusCode statusCode = SAMLEngineUtils
				.generateStatusCode(response.getStatusCode());

		// Mandatory SAML
		LOG.debug("Generate StatusCode.");
		// Subordinate code it's optional in case not covered into next codes:
		// - urn:oasis:names:tc:SAML:2.0:status:AuthnFailed
		// - urn:oasis:names:tc:SAML:2.0:status:InvalidAttrNameOrValue
		// - urn:oasis:names:tc:SAML:2.0:status:InvalidNameIDPolicy
		// - urn:oasis:names:tc:SAML:2.0:status:RequestDenied
		// - http://www.stork.gov.eu/saml20/statusCodes/QAANotSupported

		if (StringUtils.isNotBlank(response.getSubStatusCode())) {
			final StatusCode newStatusCode = SAMLEngineUtils
					.generateStatusCode(response.getSubStatusCode());
			statusCode.setStatusCode(newStatusCode);
		}

		LOG.debug("Generate Status.");
		final Status status = SAMLEngineUtils.generateStatus(statusCode);

		if (StringUtils.isNotBlank(response.getMessage())) {
			final StatusMessage statusMessage = (StatusMessage) SAMLEngineUtils
					.generateStatusMessage(response.getMessage());

			status.setStatusMessage(statusMessage);
		}

		LOG.debug("Generate Response.");
		// RESPONSE
		final Response responseFail = genAuthnRespBase(status, destinationUrl,
				request.getSamlId());

		DateTime notOnOrAfter = new DateTime();

		notOnOrAfter = notOnOrAfter.plusSeconds(super.getSamlCoreProperties()
				.getTimeNotOnOrAfter());

		final Assertion assertion = this.generateAssertion(ipAddress, "", 
				request.getSamlId(), request
				.getIssuer(), notOnOrAfter);

		responseFail.getAssertions().add(assertion);

		LOG.debug("Sign and Marshall ResponseFail.");

		final STORKAttrQueryResponse storkResponse = new STORKAttrQueryResponse();

		try {
			storkResponse.setTokenSaml(super.signAndMarshall(responseFail));
			storkResponse.setSamlId(responseFail.getID());
		} catch (SAMLEngineException e) {
			LOG.error("SAMLEngineException.", e);
			throw new STORKSAMLEngineException(e);
		}
		return storkResponse;
	}

	/**
	 * Generate stork logout request.
	 * 
	 * @param request the request that contain all parameters for generate an
	 *            logout request.
	 * 
	 * @return the STORK logout request that has been processed.
	 * 
	 * @throws STORKSAMLEngineException the STORKSAML engine exception
	 */
	public STORKLogoutRequest generateSTORKLogoutRequest(
			final STORKLogoutRequest request) throws STORKSAMLEngineException {
		LOG.info("Generate STORKLogoutRequest.");

		// Validate Parameters mandatories
		validateParamLogoutReq(request);
		
		String identifier = null;
		if (request.getSamlId() != null) {
			identifier = request.getSamlId();
		} else {
			identifier = SAMLEngineUtils.generateNCName();
		}
		final LogoutRequest logoutRequestAux = SAMLEngineUtils
				.generateSAMLLogoutRequest(identifier,
						SAMLVersion.VERSION_20, SAMLEngineUtils
						.getCurrentTime());
		

		// Set name spaces.
		setNameSpaces(logoutRequestAux);


		// Add parameter optional STORK
		// Destination is mandatory if the destination is a C-PEPS
		// The application must to know if the destination is a C-PEPS.
		if (StringUtils.isNotBlank(request.getDestination())) {
			logoutRequestAux.setDestination(request.getDestination());
		}

		// Consent is optional. Set from SAMLEngine.xml - consent.
		logoutRequestAux.setConsent(super.getSamlCoreProperties()
				.getConsentAuthnRequest());

		final Issuer issuer = SAMLEngineUtils.generateIssuer();


		if(request.getIssuer()!=null){
			issuer.setValue(request.getIssuer());
		} else {
			issuer.setValue(super.getSamlCoreProperties().getRequester());
		}

		// Optional STORK
		final String formatEntity = super.getSamlCoreProperties()
				.getFormatEntity();
		if (StringUtils.isNotBlank(formatEntity)) {
			issuer.setFormat(formatEntity);
		}

		logoutRequestAux.setIssuer(issuer);

		// Set the name ID
		final NameID newNameID = SAMLEngineUtils.generateNameID();
		newNameID.setValue(request.getSpProvidedId());		
		logoutRequestAux.setNameID(newNameID);


		// the result contains an authentication request token (byte[]),
		// identifier of the token, and all parameters from the request.
		final STORKLogoutRequest logoutRequest = new STORKLogoutRequest();

		try {
			logoutRequest.setTokenSaml(super.signAndMarshall(logoutRequestAux));
		} catch (SAMLEngineException e) {
			LOG.error("Sign and Marshall.", e);
			throw new STORKSAMLEngineException(e);
		}

		logoutRequest.setSamlId(logoutRequestAux.getID());
		logoutRequest.setDestination(logoutRequestAux.getDestination());
		logoutRequest.setIssuer(logoutRequestAux.getIssuer().getValue());
		logoutRequest.setSpProvidedId(logoutRequestAux.getNameID().getValue());

		return logoutRequest;
	}


	/**
	 * Generate stork logout response.
	 * @param request the request thats being responded to
	 * @param response the tesponse that contain all parameters for generate an
	 *            logout request.
	 * 
	 * @return the STORK logout response that has been processed.
	 * 
	 * @throws STORKSAMLEngineException the STORKSAML engine exception
	 */
	public STORKLogoutResponse generateSTORKLogoutResponse(
			final STORKLogoutRequest request, 
			final STORKLogoutResponse response) throws STORKSAMLEngineException {
		LOG.info("Generate STORKLogoutResponse.");

		// Validate Parameters mandatories
		validateParamLogoutRes(request, response);

		// Mandatory SAML
		LOG.debug("Generate StatusCode");
		final StatusCode statusCode = SAMLEngineUtils
				.generateStatusCode(StatusCode.SUCCESS_URI);

		LOG.debug("Generate Status");
		final Status status = SAMLEngineUtils.generateStatus(statusCode);

		LOG.debug("Generate StatusMessage");
		final StatusMessage statusMessage = (StatusMessage) SAMLEngineUtils
				.generateStatusMessage(StatusCode.SUCCESS_URI);

		status.setStatusMessage(statusMessage);

		final LogoutResponse logoutResponseAux= SAMLEngineUtils
				.generateSAMLLogoutResponse(SAMLEngineUtils.generateNCName(),
						SAMLVersion.VERSION_20, SAMLEngineUtils
						.getCurrentTime(), status, request.getSamlId());

		// Set name spaces.
		setNameSpaces(logoutResponseAux);


		// Add parameter optional STORK
		// Destination is mandatory if the destination is a C-PEPS
		// The application must to know if the destination is a C-PEPS.
		if (StringUtils.isNotBlank(response.getDestination())) {
			logoutResponseAux.setDestination(response.getDestination());
		}

		// Consent is optional. Set from SAMLEngine.xml - consent.
		logoutResponseAux.setConsent(super.getSamlCoreProperties()
				.getConsentAuthnRequest());

		final Issuer issuer = SAMLEngineUtils.generateIssuer();


		if(response.getIssuer()!=null){
			issuer.setValue(response.getIssuer());
		} else {
			issuer.setValue(super.getSamlCoreProperties().getRequester());
		}

		// Optional STORK
		final String formatEntity = super.getSamlCoreProperties()
				.getFormatEntity();
		if (StringUtils.isNotBlank(formatEntity)) {
			issuer.setFormat(formatEntity);
		}

		logoutResponseAux.setIssuer(issuer);


		// the result contains an authentication request token (byte[]),
		// identifier of the token, and all parameters from the request.
		final STORKLogoutResponse logoutResponse = new STORKLogoutResponse();

		try {
			logoutResponse.setTokenSaml(super.signAndMarshall(logoutResponseAux));
		} catch (SAMLEngineException e) {
			LOG.error("Sign and Marshall.", e);
			throw new STORKSAMLEngineException(e);
		}

		logoutResponse.setSamlId(logoutResponseAux.getID());
		logoutResponse.setDestination(logoutResponseAux.getDestination());
		logoutResponse.setIssuer(logoutResponseAux.getIssuer().getValue());
		logoutResponse.setStatusCode(logoutResponseAux.getStatus().getStatusCode().toString());
		logoutResponse.setStatusMessage(logoutResponseAux.getStatus().getStatusMessage().toString());

		return logoutResponse;
	}

	/**
	 * Generate failed stork logout response.
	 * 
	 * @param response the response that contain all parameters for generate an
	 *            logout request.
	 * 
	 * @return the STORK logout response that has been processed.
	 * 
	 * @throws STORKSAMLEngineException the STORKSAML engine exception
	 */
	public STORKLogoutResponse generateSTORKLogoutResponseFail(
			final STORKLogoutRequest request, 
			final STORKLogoutResponse response ) throws STORKSAMLEngineException {
		LOG.info("Generate STORKLogoutResponse.");

		// Validate Parameters mandatories
		validateParamLogoutResFail(request, response);

		// Mandatory
		final StatusCode statusCode = SAMLEngineUtils
				.generateStatusCode(response.getStatusCode());

		// Mandatory SAML
		LOG.debug("Generate StatusCode.");
		// Subordinate code it's optional in case not covered into next codes:
		// - urn:oasis:names:tc:SAML:2.0:status:AuthnFailed
		// - urn:oasis:names:tc:SAML:2.0:status:InvalidAttrNameOrValue
		// - urn:oasis:names:tc:SAML:2.0:status:InvalidNameIDPolicy
		// - urn:oasis:names:tc:SAML:2.0:status:RequestDenied
		// - http://www.stork.gov.eu/saml20/statusCodes/QAANotSupported

		if (StringUtils.isNotBlank(response.getSubStatusCode())) {
			final StatusCode newStatusCode = SAMLEngineUtils
					.generateStatusCode(response.getSubStatusCode());
			statusCode.setStatusCode(newStatusCode);
		}

		LOG.debug("Generate Status.");
		final Status status = SAMLEngineUtils.generateStatus(statusCode);

		if (StringUtils.isNotBlank(response.getStatusMessage())) {
			final StatusMessage statusMessage = (StatusMessage) SAMLEngineUtils
					.generateStatusMessage(response.getStatusMessage());

			status.setStatusMessage(statusMessage);
		}

		final LogoutResponse logoutResponseAux= SAMLEngineUtils
				.generateSAMLLogoutResponse(SAMLEngineUtils.generateNCName(),
						SAMLVersion.VERSION_20, SAMLEngineUtils
						.getCurrentTime(), status, request.getSamlId());

		// Set name spaces.
		setNameSpaces(logoutResponseAux);


		// Add parameter optional STORK
		// Destination is mandatory if the destination is a C-PEPS
		// The application must to know if the destination is a C-PEPS.
		if (StringUtils.isNotBlank(response.getDestination())) {
			logoutResponseAux.setDestination(response.getDestination());
		}

		// Consent is optional. Set from SAMLEngine.xml - consent.
		logoutResponseAux.setConsent(super.getSamlCoreProperties()
				.getConsentAuthnRequest());

		final Issuer issuer = SAMLEngineUtils.generateIssuer();


		if(response.getIssuer()!=null){
			issuer.setValue(response.getIssuer());
		} else {
			issuer.setValue(super.getSamlCoreProperties().getRequester());
		}

		// Optional STORK
		final String formatEntity = super.getSamlCoreProperties()
				.getFormatEntity();
		if (StringUtils.isNotBlank(formatEntity)) {
			issuer.setFormat(formatEntity);
		}

		logoutResponseAux.setIssuer(issuer);


		// the result contains an authentication request token (byte[]),
		// identifier of the token, and all parameters from the request.
		final STORKLogoutResponse logoutResponse = new STORKLogoutResponse();

		try {
			logoutResponse.setTokenSaml(super.signAndMarshall(logoutResponseAux));
		} catch (SAMLEngineException e) {
			LOG.error("Sign and Marshall.", e);
			throw new STORKSAMLEngineException(e);
		}

		logoutResponse.setSamlId(logoutResponseAux.getID());
		logoutResponse.setDestination(logoutResponseAux.getDestination());
		logoutResponse.setIssuer(logoutResponseAux.getIssuer().getValue());
		logoutResponse.setStatusCode(logoutResponseAux.getStatus().getStatusCode().toString());
		logoutResponse.setStatusMessage(logoutResponseAux.getStatus().getStatusMessage().toString());

		return logoutResponse;
	}

	/**
	 * Generate stork authentication statement for the authentication statement.
	 * 
	 * @param ipAddress the IP address
	 * 
	 * @return the authentication statement
	 */
	private AuthnStatement generateStorkAuthStatement(final String ipAddress) {
		LOG.debug("Generate stork authenticate statement.");
		final SubjectLocality subjectLocality = SAMLEngineUtils
				.generateSubjectLocality(ipAddress);

		final AuthnContext authnContext = (AuthnContext) SAMLEngineUtils
				.createSamlObject(AuthnContext.DEFAULT_ELEMENT_NAME);

		final AuthnContextDecl authnContextDecl = (AuthnContextDecl) SAMLEngineUtils
				.createSamlObject(AuthnContextDecl.DEFAULT_ELEMENT_NAME);

		authnContext.setAuthnContextDecl(authnContextDecl);

		final AuthnStatement authnStatement = SAMLEngineUtils
				.generateAthnStatement(new DateTime(), authnContext);

		// Optional STORK
		authnStatement.setSessionIndex(null);
		authnStatement.setSubjectLocality(subjectLocality);

		return authnStatement;
	}

	/**
	 * Generate stork extensions.
	 * 
	 * @param request the request
	 * 
	 * @return the extensions
	 * 
	 * @throws STORKSAMLEngineException the STORKSAML engine exception
	 */
	private Extensions generateSTORKExtensions(final STORKAuthnRequest request)
			throws STORKSAMLEngineException {
		LOG.debug("Generate STORKExtensions");

		final Extensions extensions = SAMLEngineUtils.generateExtension();

		LOG.debug("Generate QAAAttribute");
		final QAAAttribute qaaAttribute = SAMLEngineUtils
				.generateQAAAttribute(request.getQaa());
		extensions.getUnknownXMLObjects().add(qaaAttribute);


		if (StringUtils.isNotEmpty(request
				.getSpSector())) {
			// Add information about service provider.
			LOG.debug("Generate SPSector");
			final SPSector sector = SAMLEngineUtils.generateSPSector(request
					.getSpSector());
			extensions.getUnknownXMLObjects().add(sector);
		}

		//Delete from specification. Kept for compatibility with Provider Name value
		LOG.debug("Generate SPInstitution");
		final SPInstitution institution = SAMLEngineUtils
				.generateSPInstitution(request.getProviderName());
		extensions.getUnknownXMLObjects().add(institution);


		if (StringUtils.isNotEmpty(request.getSpApplication())) {
			LOG.debug("Generate SPApplication");
			final SPApplication application = SAMLEngineUtils
					.generateSPApplication(request.getSpApplication());
			extensions.getUnknownXMLObjects().add(application);
		}

		if (StringUtils.isNotEmpty(request.getSpCountry())) {
			LOG.debug("Generate SPCountry");
			final SPCountry country = SAMLEngineUtils.generateSPCountry(request
					.getSpCountry());
			extensions.getUnknownXMLObjects().add(country);
		}

		//eIDSectorShare: optional; default value: false.
		String valueSectorShare = super.getSamlCoreProperties()
				.iseIDSectorShare();

		if (StringUtils.isNotEmpty(valueSectorShare)) {
			// Add information about the use of the SAML message.
			LOG.debug("Generate EIDSectorShare");
			final EIDSectorShare eIdSectorShare = (EIDSectorShare) SAMLEngineUtils
					.createSamlObject(EIDSectorShare.DEF_ELEMENT_NAME);

			eIdSectorShare.setEIDSectorShare(String.valueOf(Boolean.valueOf(valueSectorShare)));

			extensions.getUnknownXMLObjects().add(eIdSectorShare);
		} 

		String valueCrossSectorShare = super.getSamlCoreProperties()
				.iseIDCrossSectorShare();

		if (StringUtils.isNotEmpty(valueCrossSectorShare)) {
			LOG.debug("Generate EIDCrossSectorShare");
			final EIDCrossSectorShare eIdCrossSecShare = (EIDCrossSectorShare) SAMLEngineUtils
					.createSamlObject(EIDCrossSectorShare.DEF_ELEMENT_NAME);
			eIdCrossSecShare.setEIDCrossSectorShare(String.valueOf(Boolean.valueOf(valueCrossSectorShare)));
			extensions.getUnknownXMLObjects().add(eIdCrossSecShare);
		}


		String valueCrossBorderShare = super.getSamlCoreProperties()
				.iseIDCrossBorderShare();

		if (StringUtils.isNotEmpty(valueCrossBorderShare)) {
			LOG.debug("Generate EIDCrossBorderShare");
			final EIDCrossBorderShare eIdCrossBordShare = (EIDCrossBorderShare) SAMLEngineUtils
					.createSamlObject(EIDCrossBorderShare.DEF_ELEMENT_NAME);
			eIdCrossBordShare.setEIDCrossBorderShare(String.valueOf(Boolean.valueOf(valueCrossBorderShare)));
			extensions.getUnknownXMLObjects().add(eIdCrossBordShare);
		}


		// Add information about requested attributes.
		LOG.debug("Generate RequestedAttributes.");
		final RequestedAttributes reqAttributes = (RequestedAttributes) SAMLEngineUtils
				.createSamlObject(RequestedAttributes.DEF_ELEMENT_NAME);

		LOG.debug("SAML Engine configuration properties load.");
		final Iterator<PersonalAttribute> iterator = request
				.getPersonalAttributeList().iterator();

		while (iterator.hasNext()) {

			final PersonalAttribute attribute = iterator.next();

			if (attribute == null || StringUtils.isBlank(attribute.getName())) {
				LOG.error(ATTRIBUTE_EMPTY_LITERAL);
				throw new STORKSAMLEngineException(ATTRIBUTE_EMPTY_LITERAL);
			}

			// Verified if exits the attribute name.
			final String attributeName = super.getSamlCoreProperties()
					.getProperty(attribute.getName());

			if (StringUtils.isBlank(attributeName)) {
				LOG.debug("Attribute name: {} was not found.", attribute
						.getName());
				throw new STORKSAMLEngineException("Attribute name: "
						+ attribute.getName() + " was not found.");
			}

			// Friendly name it's an optional attribute.
			String friendlyName = null;

			if (super.getSamlCoreProperties().isFriendlyName()) {
				friendlyName = attribute.getName();
			}


			String isRequired = null;
			if (super.getSamlCoreProperties().isRequired()) {
				isRequired = String.valueOf(attribute.isRequired());
			}


			LOG.debug("Generate requested attribute: " + attributeName);
			final RequestedAttribute requestedAttr = SAMLEngineUtils
					.generateReqAuthnAttributeSimple(attributeName,
							friendlyName, isRequired, attribute
							.getValue());

			// Add requested attribute.
			reqAttributes.getAttributes().add(requestedAttr);
		}

		// Add requested attributes.
		extensions.getUnknownXMLObjects().add(reqAttributes);

		CitizenCountryCode citizenCountryCode = null;
		if (request.getCitizenCountryCode() != null && StringUtils.isNotBlank(request.getCitizenCountryCode())){
			LOG.debug("Generate CitizenCountryCode");
			citizenCountryCode = (CitizenCountryCode) SAMLEngineUtils
					.createSamlObject(CitizenCountryCode.DEF_ELEMENT_NAME);

			citizenCountryCode.setCitizenCountryCode(request
					.getCitizenCountryCode().toUpperCase());
		}		

		SPID spid = null;
		if(request.getSPID()!=null && StringUtils.isNotBlank(request.getSPID())) {
			LOG.debug("Generate SPID");
			spid = (SPID) SAMLEngineUtils
					.createSamlObject(SPID.DEF_ELEMENT_NAME);

			spid.setSPID(request.getSPID().toUpperCase());
		}	   

		AuthenticationAttributes authenticationAttr = (AuthenticationAttributes) SAMLEngineUtils
				.createSamlObject(AuthenticationAttributes.DEF_ELEMENT_NAME);

		final VIDPAuthenticationAttributes vIDPauthenticationAttr = (VIDPAuthenticationAttributes) SAMLEngineUtils
				.createSamlObject(VIDPAuthenticationAttributes.DEF_ELEMENT_NAME);

		final SPInformation spInformation = (SPInformation) SAMLEngineUtils
				.createSamlObject(SPInformation.DEF_ELEMENT_NAME);

		if(citizenCountryCode!=null){
			vIDPauthenticationAttr.setCitizenCountryCode(citizenCountryCode);
		}

		if(spid!=null){
			spInformation.setSPID(spid);
		}

		vIDPauthenticationAttr.setSPInformation(spInformation);

		authenticationAttr
		.setVIDPAuthenticationAttributes(vIDPauthenticationAttr);
		extensions.getUnknownXMLObjects().add(authenticationAttr);


		return extensions;

	}

	/**
	 * Generate stork extensions.
	 * 
	 * @param request the attribute query request
	 * 
	 * @return the extensions
	 * 
	 * @throws STORKSAMLEngineException the STORKSAML engine exception
	 */
	private Extensions generateSTORKAttrExtensions(final STORKAttrQueryRequest request)
			throws STORKSAMLEngineException {
		LOG.debug("Generate STORKExtensions");

		final Extensions extensions = SAMLEngineUtils.generateExtension();

		LOG.debug("Generate QAAAttribute");
		final QAAAttribute qaaAttribute = SAMLEngineUtils
				.generateQAAAttribute(request.getQaa());
		extensions.getUnknownXMLObjects().add(qaaAttribute);


		if (StringUtils.isNotEmpty(request
				.getSpSector())) {
			// Add information about service provider.
			LOG.debug("Generate SPSector");
			final SPSector sector = SAMLEngineUtils.generateSPSector(request
					.getSpSector());
			extensions.getUnknownXMLObjects().add(sector);
		}


		if (StringUtils.isNotEmpty(request.getSpApplication())) {
			LOG.debug("Generate SPApplication");
			final SPApplication application = SAMLEngineUtils
					.generateSPApplication(request.getSpApplication());
			extensions.getUnknownXMLObjects().add(application);
		}

		if (StringUtils.isNotEmpty(request.getSpCountry())) {
			LOG.debug("Generate SPCountry");
			final SPCountry country = SAMLEngineUtils.generateSPCountry(request
					.getSpCountry());
			extensions.getUnknownXMLObjects().add(country);
		}

		final EIDSectorShare eIdSectorShare = (EIDSectorShare) SAMLEngineUtils
				.createSamlObject(EIDSectorShare.DEF_ELEMENT_NAME);

		eIdSectorShare.setEIDSectorShare(String.valueOf(request.isEIDSectorShare()));

		extensions.getUnknownXMLObjects().add(eIdSectorShare);

		final EIDCrossSectorShare eIdCrossSecShare = (EIDCrossSectorShare) SAMLEngineUtils
				.createSamlObject(EIDCrossSectorShare.DEF_ELEMENT_NAME);
		eIdCrossSecShare.setEIDCrossSectorShare(String.valueOf(request.isEIDCrossSectorShare()));
		extensions.getUnknownXMLObjects().add(eIdCrossSecShare);

		final EIDCrossBorderShare eIdCrossBordShare = (EIDCrossBorderShare) SAMLEngineUtils
				.createSamlObject(EIDCrossBorderShare.DEF_ELEMENT_NAME);
		eIdCrossBordShare.setEIDCrossBorderShare(String.valueOf(request.isEIDCrossBorderShare()));
		extensions.getUnknownXMLObjects().add(eIdCrossBordShare);


		// Add information about requested attributes.
		LOG.debug("Generate RequestedAttributes.");
		final RequestedAttributes reqAttributes = (RequestedAttributes) SAMLEngineUtils
				.createSamlObject(RequestedAttributes.DEF_ELEMENT_NAME);

		LOG.debug("SAML Engine configuration properties load.");
		final Iterator<PersonalAttribute> iterator = request
				.getPersonalAttributeList().iterator();

		while (iterator.hasNext()) {

			final PersonalAttribute attribute = iterator.next();

			if (attribute == null || StringUtils.isBlank(attribute.getName())) {
				LOG.error(ATTRIBUTE_EMPTY_LITERAL);
				throw new STORKSAMLEngineException(ATTRIBUTE_EMPTY_LITERAL);
			}

			// Verified if exits the attribute name.
			final String attributeName = super.getSamlCoreProperties()
					.getProperty(attribute.getName());

			if (StringUtils.isBlank(attributeName)) {
				LOG.debug("Attribute name: {} was not found.", attribute
						.getName());
				throw new STORKSAMLEngineException("Attribute name: "
						+ attribute.getName() + " was not found.");
			}

			// Friendly name it's an optional attribute.
			String friendlyName = null;

			if (super.getSamlCoreProperties().isFriendlyName()) {
				friendlyName = attribute.getName();
			}


			String isRequired = null;
			if (super.getSamlCoreProperties().isRequired()) {
				isRequired = String.valueOf(attribute.isRequired());
			}


			LOG.debug("Generate requested attribute: " + attributeName);
			final RequestedAttribute requestedAttr = SAMLEngineUtils
					.generateReqAuthnAttributeSimple(attributeName,
							friendlyName, isRequired, attribute
							.getValue());

			// Add requested attribute.
			reqAttributes.getAttributes().add(requestedAttr);
		}

		// Add requested attributes.
		extensions.getUnknownXMLObjects().add(reqAttributes);

		CitizenCountryCode citizenCountryCode = null;
		if (request.getCitizenCountryCode() != null && StringUtils.isNotBlank(request.getCitizenCountryCode())){
			LOG.debug("Generate CitizenCountryCode");
			citizenCountryCode = (CitizenCountryCode) SAMLEngineUtils
					.createSamlObject(CitizenCountryCode.DEF_ELEMENT_NAME);

			citizenCountryCode.setCitizenCountryCode(request
					.getCitizenCountryCode().toUpperCase());
		}		

		SPID spid = null;
		if(request.getSPID()!=null && StringUtils.isNotBlank(request.getSPID())) {
			LOG.debug("Generate SPID");
			spid = (SPID) SAMLEngineUtils
					.createSamlObject(SPID.DEF_ELEMENT_NAME);

			spid.setSPID(request.getSPID().toUpperCase());
		}	   


		return extensions;

	}

	/**
	 * Gets the alias from X.509 Certificate at keystore.
	 * 
	 * @param keyInfo the key info
	 * @param storkOwnKeyStore 
	 * @param storkOwnKeyStore 
	 * 
	 * @return the alias
	 */
	private String getAlias(final KeyInfo keyInfo, KeyStore storkOwnKeyStore, String filter) {

		LOG.debug("Recover alias information");

		String alias = null;
		try {
			final org.opensaml.xml.signature.X509Certificate xmlCert = keyInfo
					.getX509Datas().get(0).getX509Certificates().get(0);

			// Transform the KeyInfo to X509Certificate.
			CertificateFactory certFact;
			certFact = CertificateFactory.getInstance("X.509");

			final ByteArrayInputStream bis = new ByteArrayInputStream(Base64
					.decode(xmlCert.getValue()));

			final X509Certificate cert = (X509Certificate) certFact
					.generateCertificate(bis);

			final String tokenSerialNumber = cert.getSerialNumber().toString(16);
			final X509Principal tokenIssuerDN = new X509Principal(cert.getIssuerDN().getName());


			String aliasCert;
			X509Certificate certificate;
			boolean find = false;

			for (final Enumeration<String> e = storkOwnKeyStore.aliases(); e
					.hasMoreElements()
					&& !find; ) {
				aliasCert = e.nextElement();
				if (filter != null && !filter.trim().equals("") && !aliasCert.startsWith(filter)) {
					continue;                	
				}
				certificate = (X509Certificate) storkOwnKeyStore
						.getCertificate(aliasCert);                

				final String serialNum = certificate.getSerialNumber()
						.toString(16);

				X509Principal issuerDN = new X509Principal(certificate
						.getIssuerDN().getName());

				if(serialNum.equalsIgnoreCase(tokenSerialNumber)
						&& X509PrincipalUtil.equals2(issuerDN, tokenIssuerDN)){
					if (!storkOwnKeyStore.isKeyEntry(aliasCert)) {
						alias = aliasCert;
						find = true;
					}
				}

			}

		} catch (KeyStoreException e) {
			LOG.error("Procces getAlias from certificate associated into the signing keystore..", e);
		} catch (CertificateException e) {
			LOG.error("Procces getAlias from certificate associated into the signing keystore..", e);
		} catch (RuntimeException e) {
			LOG.error("Procces getAlias from certificate associated into the signing keystore..", e);
		}
		return alias;
	}

	/**
	 * Gets the country from X.509 Certificate.
	 * 
	 * @param keyInfo the key info
	 * 
	 * @return the country
	 */
	private String getCountry(final KeyInfo keyInfo) {
		LOG.debug("Recover country information.");

		String result = "";
		try {
			final org.opensaml.xml.signature.X509Certificate xmlCert = keyInfo
					.getX509Datas().get(0).getX509Certificates().get(0);

			// Transform the KeyInfo to X509Certificate.
			CertificateFactory certFact;
			certFact = CertificateFactory.getInstance("X.509");

			final ByteArrayInputStream bis = new ByteArrayInputStream(Base64
					.decode(xmlCert.getValue()));

			final X509Certificate cert = (X509Certificate) certFact
					.generateCertificate(bis);

			String distName = cert.getSubjectDN().toString();

			distName = StringUtils.deleteWhitespace(StringUtils
					.upperCase(distName));

			final String countryCode = "C=";
			final int init = distName.indexOf(countryCode);

			if (init > StringUtils.INDEX_NOT_FOUND) { // Exist country code.
				int end = distName.indexOf(',', init);

				if (end <= StringUtils.INDEX_NOT_FOUND) {
					end = distName.length();
				}

				if (init < end && end > StringUtils.INDEX_NOT_FOUND) {
					result = distName.substring(init + countryCode.length(),
							end);
					//It must be a two characters value
					if(result.length()>2){
						result = result.substring(0, 2);
					}
				}
			}

		} catch (CertificateException e) {
			LOG.error("Procces getCountry from certificate.");
		}
		return result.trim();
	}

	/**
	 * Process all elements XMLObjects from the extensions.
	 * 
	 * @param extensions the extensions from the authentication request.
	 * 
	 * @return the STORK authentication request
	 * 
	 * @throws STORKSAMLEngineException the STORKSAML engine exception
	 */
	private STORKAuthnRequest processExtensions(final Extensions extensions)
			throws STORKSAMLEngineException {
		LOG.debug("Procces the extensions.");

		final STORKAuthnRequest request = new STORKAuthnRequest();

		final QAAAttribute qaa = (QAAAttribute) extensions
				.getUnknownXMLObjects(QAAAttribute.DEF_ELEMENT_NAME).get(0);
		request.setQaa(Integer.parseInt(qaa.getQaaLevel()));

		List optionalElements = extensions.getUnknownXMLObjects(
				SPSector.DEF_ELEMENT_NAME);

		if (!optionalElements.isEmpty()) {
			final SPSector sector = (SPSector) extensions.getUnknownXMLObjects(
					SPSector.DEF_ELEMENT_NAME).get(0);
			request.setSpSector(sector.getSPSector());
		}

		optionalElements = extensions.getUnknownXMLObjects(SPApplication.DEF_ELEMENT_NAME);

		if (!optionalElements.isEmpty()) {
			final SPApplication application = (SPApplication) extensions
					.getUnknownXMLObjects(SPApplication.DEF_ELEMENT_NAME).get(0);
			request.setSpApplication(application.getSPApplication());
		}

		optionalElements = extensions.getUnknownXMLObjects(SPCountry.DEF_ELEMENT_NAME);

		if (!optionalElements.isEmpty()) {
			final SPCountry application = (SPCountry) extensions
					.getUnknownXMLObjects(SPCountry.DEF_ELEMENT_NAME).get(0);
			request.setSpCountry(application.getSPCountry());
		}	


		List listCrossBorderShare = extensions
				.getUnknownXMLObjects(EIDCrossBorderShare.DEF_ELEMENT_NAME);

		if (!listCrossBorderShare .isEmpty()) {
			final EIDCrossBorderShare crossBorderShare = (EIDCrossBorderShare) listCrossBorderShare.get(0);
			request.setEIDCrossBorderShare(Boolean.parseBoolean(crossBorderShare
					.getEIDCrossBorderShare()));		
		}


		List listCrosSectorShare = extensions
				.getUnknownXMLObjects(EIDCrossSectorShare.DEF_ELEMENT_NAME);

		if (!listCrosSectorShare.isEmpty()) {
			final EIDCrossSectorShare crossSectorShare = (EIDCrossSectorShare) listCrosSectorShare.get(0);
			request.setEIDCrossSectorShare(Boolean.parseBoolean(crossSectorShare
					.getEIDCrossSectorShare()));
		}

		List listSectorShareExtension = extensions
				.getUnknownXMLObjects(EIDSectorShare.DEF_ELEMENT_NAME);
		if (!listSectorShareExtension.isEmpty()) {
			final EIDSectorShare sectorShare = (EIDSectorShare) listSectorShareExtension.get(0);
			request.setEIDSectorShare(Boolean.parseBoolean(sectorShare.getEIDSectorShare()));
		}



		List<XMLObject> authAttrs = extensions
				.getUnknownXMLObjects(AuthenticationAttributes.DEF_ELEMENT_NAME);

		if (authAttrs != null && !authAttrs.isEmpty()) {

			final AuthenticationAttributes authnAttr = (AuthenticationAttributes) authAttrs
					.get(0);	

			VIDPAuthenticationAttributes vidpAuthnAttr = null;
			if (authnAttr != null && !authAttrs.isEmpty()){
				vidpAuthnAttr = authnAttr.getVIDPAuthenticationAttributes();
			}

			CitizenCountryCode citizenCountryCodeElement = null;
			SPInformation spInformation = null;
			if (vidpAuthnAttr != null){
				citizenCountryCodeElement = vidpAuthnAttr.getCitizenCountryCode();
				spInformation = vidpAuthnAttr.getSPInformation();
			}

			String citizenCountryCode = null;
			if(citizenCountryCodeElement!=null){
				citizenCountryCode = citizenCountryCodeElement.getCitizenCountryCode();
			}

			if(citizenCountryCode!= null && StringUtils.isNotBlank(citizenCountryCode)){
				request.setCitizenCountryCode(citizenCountryCode);
			}	  	    

			SPID spidElement = null;
			if (spInformation != null){
				spidElement = spInformation.getSPID();
			}

			String spid = null;
			if(spidElement!=null){
				spid = spidElement.getSPID();
			}

			if (spid != null && StringUtils.isNotBlank(spid)) {
				request.setSPID(spid);
			}
		}

		if (extensions
				.getUnknownXMLObjects(RequestedAttributes.DEF_ELEMENT_NAME) == null) {
			LOG.error("Extensions not contains any requested attribute.");
			throw new STORKSAMLEngineException(
					"Extensions not contains any requested attribute.");
		}

		final RequestedAttributes requestedAttr = (RequestedAttributes) extensions
				.getUnknownXMLObjects(RequestedAttributes.DEF_ELEMENT_NAME)
				.get(0);

		final List<RequestedAttribute> reqAttrs = requestedAttr.getAttributes();

		final IPersonalAttributeList personalAttrList = new PersonalAttributeList();

		String attributeName;
		for (int nextAttribute = 0; nextAttribute < reqAttrs.size(); nextAttribute++) {
			final RequestedAttribute attribute = reqAttrs.get(nextAttribute);
			final PersonalAttribute personalAttribute = new PersonalAttribute();
			personalAttribute.setIsRequired(Boolean.valueOf(attribute.isRequired()));
			personalAttribute.setFriendlyName(attribute.getFriendlyName());
			attributeName = attribute.getName();

			// recover the last name from the string.
			personalAttribute.setName(attributeName.substring(attributeName
					.lastIndexOf('/') + 1));

			final ArrayList<String> valores = new ArrayList<String>();
			final List<XMLObject> values = attribute.getOrderedChildren();

			for (int nextSimpleValue = 0; nextSimpleValue < values.size(); nextSimpleValue++) {

				// Process attributes simples. An AuthenticationRequest only
				// must contains simple values.

				final XMLObject xmlObject = values.get(nextSimpleValue);

				if(xmlObject instanceof XSStringImpl){

					final XSStringImpl xmlString = (XSStringImpl) values
							.get(nextSimpleValue);
					valores.add(xmlString.getValue());

				}else{

					if (attributeName.equals("http://www.stork.gov.eu/1.0/signedDoc")) {

						final XSAnyImpl xmlString = (XSAnyImpl) values
								.get(nextSimpleValue);

						TransformerFactory transFactory = TransformerFactory.newInstance();
						Transformer transformer = null;
						try {
							transformer = transFactory.newTransformer();
							transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
						} catch (TransformerConfigurationException e) {
							LOG.error("Error transformer configuration exception", e);
						}
						StringWriter buffer = new StringWriter();					
						try {
							if (xmlString != null && xmlString.getUnknownXMLObjects() != null && xmlString.getUnknownXMLObjects().size() > 0 ){
								transformer.transform(new DOMSource(xmlString.getUnknownXMLObjects().get(0).getDOM()),
										new StreamResult(buffer));
							}
						} catch (TransformerException e) {
							LOG.error("Error transformer exception", e);
						}
						String str = buffer.toString();

						valores.add(str);	

					}else{

						final XSAnyImpl xmlString = (XSAnyImpl) values
								.get(nextSimpleValue);
						valores.add(xmlString.getTextContent());
					}



				}
			}
			personalAttribute.setValue(valores);
			personalAttrList.add(personalAttribute);
		}

		request.setPersonalAttributeList(personalAttrList);

		return request;
	}


	/**
	 * Process all elements XMLObjects from the extensions.
	 * 
	 * @param extensions the extensions from the authentication request.
	 * 
	 * @return the STORK authentication request
	 * 
	 * @throws STORKSAMLEngineException the STORKSAML engine exception
	 */
	private STORKAttrQueryRequest processAttrExtensions(final Extensions extensions)
			throws STORKSAMLEngineException {
		LOG.debug("Procces the atribute query extensions.");

		final STORKAttrQueryRequest request = new STORKAttrQueryRequest();

		final QAAAttribute qaa = (QAAAttribute) extensions
				.getUnknownXMLObjects(QAAAttribute.DEF_ELEMENT_NAME).get(0);
		request.setQaa(Integer.parseInt(qaa.getQaaLevel()));

		List optionalElements = extensions.getUnknownXMLObjects(
				SPSector.DEF_ELEMENT_NAME);

		if (!optionalElements.isEmpty()) {
			final SPSector sector = (SPSector) extensions.getUnknownXMLObjects(
					SPSector.DEF_ELEMENT_NAME).get(0);
			request.setSpSector(sector.getSPSector());
		}

		optionalElements = extensions.getUnknownXMLObjects(SPApplication.DEF_ELEMENT_NAME);

		if (!optionalElements.isEmpty()) {
			final SPApplication application = (SPApplication) extensions
					.getUnknownXMLObjects(SPApplication.DEF_ELEMENT_NAME).get(0);
			request.setSpApplication(application.getSPApplication());
		}

		optionalElements = extensions.getUnknownXMLObjects(SPCountry.DEF_ELEMENT_NAME);

		if (!optionalElements.isEmpty()) {
			final SPCountry application = (SPCountry) extensions
					.getUnknownXMLObjects(SPCountry.DEF_ELEMENT_NAME).get(0);
			request.setSpCountry(application.getSPCountry());
		}	


		List listCrossBorderShare = extensions
				.getUnknownXMLObjects(EIDCrossBorderShare.DEF_ELEMENT_NAME);

		if (!listCrossBorderShare .isEmpty()) {
			final EIDCrossBorderShare crossBorderShare = (EIDCrossBorderShare) listCrossBorderShare.get(0);
			request.setEIDCrossBorderShare(Boolean.parseBoolean(crossBorderShare
					.getEIDCrossBorderShare()));		
		}


		List listCrosSectorShare = extensions
				.getUnknownXMLObjects(EIDCrossSectorShare.DEF_ELEMENT_NAME);

		if (!listCrosSectorShare.isEmpty()) {
			final EIDCrossSectorShare crossSectorShare = (EIDCrossSectorShare) listCrosSectorShare.get(0);
			request.setEIDCrossSectorShare(Boolean.parseBoolean(crossSectorShare
					.getEIDCrossSectorShare()));
		}

		List listSectorShareExtension = extensions
				.getUnknownXMLObjects(EIDSectorShare.DEF_ELEMENT_NAME);
		if (!listSectorShareExtension.isEmpty()) {
			final EIDSectorShare sectorShare = (EIDSectorShare) listSectorShareExtension.get(0);
			request.setEIDSectorShare(Boolean.parseBoolean(sectorShare.getEIDSectorShare()));
		}



		List<XMLObject> authAttrs = extensions
				.getUnknownXMLObjects(AuthenticationAttributes.DEF_ELEMENT_NAME);

		if (authAttrs != null && !authAttrs.isEmpty()) {

			final AuthenticationAttributes authnAttr = (AuthenticationAttributes) authAttrs
					.get(0);	

			VIDPAuthenticationAttributes vidpAuthnAttr = null;
			if (authnAttr != null && !authAttrs.isEmpty()){
				vidpAuthnAttr = authnAttr.getVIDPAuthenticationAttributes();
			}

			CitizenCountryCode citizenCountryCodeElement = null;
			SPInformation spInformation = null;
			if (vidpAuthnAttr != null){
				citizenCountryCodeElement = vidpAuthnAttr.getCitizenCountryCode();
				spInformation = vidpAuthnAttr.getSPInformation();
			}

			String citizenCountryCode = null;
			if(citizenCountryCodeElement!=null){
				citizenCountryCode = citizenCountryCodeElement.getCitizenCountryCode();
			}

			if(citizenCountryCode!= null && StringUtils.isNotBlank(citizenCountryCode)){
				request.setCitizenCountryCode(citizenCountryCode);
			}	  	    

			SPID spidElement = null;
			if (spInformation != null){
				spidElement = spInformation.getSPID();
			}

			String spid = null;
			if(spidElement!=null){
				spid = spidElement.getSPID();
			}

			if (spid != null && StringUtils.isNotBlank(spid)) {
				request.setSPID(spid);
			}
		}

		if (extensions
				.getUnknownXMLObjects(RequestedAttributes.DEF_ELEMENT_NAME) == null) {
			LOG.error("Extensions not contains any requested attribute.");
			throw new STORKSAMLEngineException(
					"Extensions not contains any requested attribute.");
		}

		final RequestedAttributes requestedAttr = (RequestedAttributes) extensions
				.getUnknownXMLObjects(RequestedAttributes.DEF_ELEMENT_NAME)
				.get(0);

		final List<RequestedAttribute> reqAttrs = requestedAttr.getAttributes();

		final IPersonalAttributeList personalAttrList = new PersonalAttributeList();

		String attributeName;
		for (int nextAttribute = 0; nextAttribute < reqAttrs.size(); nextAttribute++) {
			final RequestedAttribute attribute = reqAttrs.get(nextAttribute);
			final PersonalAttribute personalAttribute = new PersonalAttribute();
			personalAttribute.setIsRequired(Boolean.valueOf(attribute.isRequired()));
			personalAttribute.setFriendlyName(attribute.getFriendlyName());
			attributeName = attribute.getName();

			// recover the last name from the string.
			personalAttribute.setName(attributeName.substring(attributeName
					.lastIndexOf('/') + 1));

			final ArrayList<String> valores = new ArrayList<String>();
			final List<XMLObject> values = attribute.getOrderedChildren();

			for (int nextSimpleValue = 0; nextSimpleValue < values.size(); nextSimpleValue++) {

				// Process attributes simples. An AuthenticationRequest only
				// must contains simple values.

				final XMLObject xmlObject = values.get(nextSimpleValue);

				if(xmlObject instanceof XSStringImpl){

					final XSStringImpl xmlString = (XSStringImpl) values
							.get(nextSimpleValue);
					valores.add(xmlString.getValue());

				}else{

					if (attributeName.equals("http://www.stork.gov.eu/1.0/signedDoc")) {

						final XSAnyImpl xmlString = (XSAnyImpl) values
								.get(nextSimpleValue);

						TransformerFactory transFactory = TransformerFactory.newInstance();
						Transformer transformer = null;
						try {
							transformer = transFactory.newTransformer();
							transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
						} catch (TransformerConfigurationException e) {
							LOG.error("Error transformer configuration exception", e);
						}
						StringWriter buffer = new StringWriter();					
						try {
							if (xmlString != null && xmlString.getUnknownXMLObjects() != null && xmlString.getUnknownXMLObjects().size() > 0 ){
								transformer.transform(new DOMSource(xmlString.getUnknownXMLObjects().get(0).getDOM()),
										new StreamResult(buffer));
							}
						} catch (TransformerException e) {
							LOG.error("Error transformer exception", e);
						}
						String str = buffer.toString();

						valores.add(str);	

					}else{

						final XSAnyImpl xmlString = (XSAnyImpl) values
								.get(nextSimpleValue);
						valores.add(xmlString.getTextContent());
					}



				}
			}
			personalAttribute.setValue(valores);
			personalAttrList.add(personalAttribute);
		}

		request.setPersonalAttributeList(personalAttrList);

		return request;
	}

	/**
	 * Sets the name spaces.
	 * 
	 * @param tokenSaml the new name spaces
	 */
	private void setNameSpaces(final XMLObject tokenSaml) {
		LOG.debug("Set namespaces.");

		final Namespace saml2 = new Namespace(SAMLConstants.SAML20_NS,
				SAMLConstants.SAML20_PREFIX);
		tokenSaml.addNamespace(saml2);

		final Namespace digSig = new Namespace(
				"http://www.w3.org/2000/09/xmldsig#", "ds");
		tokenSaml.addNamespace(digSig);

		final Namespace storkp = new Namespace(SAMLCore.STORK10P_NS.getValue(),
				SAMLCore.STORK10P_PREFIX.getValue());
		tokenSaml.addNamespace(storkp);

		final Namespace stork = new Namespace(SAMLCore.STORK10_NS.getValue(),
				SAMLCore.STORK10_PREFIX.getValue());

		tokenSaml.addNamespace(stork);
	}

	/**
	 * Validate parameters from authentication request.
	 * 
	 * @param request the request.
	 * 
	 * @throws STORKSAMLEngineException the STORKSAML engine exception
	 */
	private void validateParamAuthnReq(final STORKAuthnRequest request)
			throws STORKSAMLEngineException {
		LOG.info("Validate parameters from authentication request.");

		// URL to which Authentication Response must be sent.
		if (StringUtils.isBlank(request.getAssertionConsumerServiceURL())) {
			throw new STORKSAMLEngineException(
					"StorkSamlEngine: Assertion Consumer Service URL it's mandatory.");
		}

		// the name of the original service provider requesting the
		// authentication.
		if (StringUtils.isBlank(request.getProviderName())) {
			throw new STORKSAMLEngineException(
					"StorkSamlEngine: Service Provider it's mandatory.");
		}

		// object that contain all attributes requesting.
		if (request.getPersonalAttributeList() == null
				|| request.getPersonalAttributeList().isEmpty()) {
			throw new STORKSAMLEngineException(
					"attributeQueries is null or empty.");
		}

		// Quality authentication assurance level.
		if ((request.getQaa() < QAAAttribute.MIN_VALUE)
				|| (request.getQaa() > QAAAttribute.MAX_VALUE)) {
			throw new STORKSAMLEngineException("Qaal: " + request.getQaa()
					+ ", is invalid.");
		}

	}

	/**
	 * Validate parameters from attribute query request.
	 * 
	 * @param request the request.
	 * 
	 * @throws STORKSAMLEngineException the STORKSAML engine exception
	 */
	private void validateParamAttrQueryReq(final STORKAttrQueryRequest request)
			throws STORKSAMLEngineException {
		LOG.info("Validate parameters from attribute query request.");

		// URL to which AP Response must be sent.
		if (StringUtils.isBlank(request.getAssertionConsumerServiceURL())) {
			throw new STORKSAMLEngineException(
					"StorkSamlEngine: Assertion Consumer Service URL it's mandatory.");
		}

		// Destination of the request - not mandatory
		/*if (StringUtils.isBlank(request.getDestination())) {
			throw new STORKSAMLEngineException(
					"StorkSamlEngine: Destination is mandatory.");
		}*/

		// SP country is empty
		if (StringUtils.isBlank(request.getSpCountry())) {
			throw new STORKSAMLEngineException(
					"StorkSamlEngine: SP country is mandatory.");
		}

		// object that contain all attributes requesting.
		if (request.getPersonalAttributeList() == null
				|| request.getPersonalAttributeList().isEmpty()) {
			throw new STORKSAMLEngineException(
					"attributeQueries is null or empty.");
		}

		// Quality authentication assurance level.
		if ((request.getQaa() < QAAAttribute.MIN_VALUE)
				|| (request.getQaa() > QAAAttribute.MAX_VALUE)) {
			throw new STORKSAMLEngineException("Qaal: " + request.getQaa()
					+ ", is invalid.");
		}
	}

	/**
	 * Validate parameters from logout request.
	 * 
	 * @param request the request.
	 * 
	 * @throws STORKSAMLEngineException the STORKSAML engine exception
	 */
	private void validateParamLogoutReq(final STORKLogoutRequest request)
			throws STORKSAMLEngineException {
		LOG.info("Validate parameters from logout request.");

		// URL to which AP Response must be sent.
		/*if (StringUtils.isBlank(request.get())) {
			throw new STORKSAMLEngineException(
					"StorkSamlEngine: Assertion Consumer Service URL it's mandatory.");
		}*/

		// Destination of the request
		if (StringUtils.isBlank(request.getDestination())) {
			throw new STORKSAMLEngineException(
					"StorkSamlEngine: Destination is mandatory.");
		}

		// SP Provided Id
		if (StringUtils.isBlank(request.getSpProvidedId())) {
			throw new STORKSAMLEngineException(
					"StorkSamlEngine: SP provided Id is mandatory.");
		}
	}

	/**
	 * Validate parameters from logout response.
	 * 
	 * @param response the response.
	 * 
	 * @throws STORKSAMLEngineException the STORKSAML engine exception
	 */
	private void validateParamLogoutRes(final STORKLogoutRequest request, 
			final STORKLogoutResponse response)	throws STORKSAMLEngineException {
		LOG.info("Validate parameters from logout request.");

		// Issuer is mandatory
		if (StringUtils.isBlank(request.getIssuer())) {
			throw new STORKSAMLEngineException(
					"Issuer must be not empty or null.");
		}

		// Destination of the request
		if (StringUtils.isBlank(response.getDestination())) {
			throw new STORKSAMLEngineException(
					"StorkSamlEngine: Destination is mandatory.");
		}

		// SP Provided Id
		if (StringUtils.isBlank(request.getSpProvidedId())) {
			throw new STORKSAMLEngineException(
					"StorkSamlEngine: SP provided Id is mandatory.");
		}

		if (StringUtils.isBlank(request.getSamlId())) {
			throw new STORKSAMLEngineException("request ID is null or empty.");
		}
	}


	/**
	 * Validate parameters from response.
	 * 
	 * @param request the request
	 * @param responseAuthReq the response authentication request
	 * 
	 * @throws STORKSAMLEngineException the STORKSAML engine exception
	 */
	private void validateParamResponse(final STORKAuthnRequest request,
			final STORKAuthnResponse responseAuthReq)
					throws STORKSAMLEngineException {
		LOG.info("Validate parameters response.");
		if (StringUtils.isBlank(request.getIssuer())) {
			throw new STORKSAMLEngineException(
					"Issuer must be not empty or null.");
		}

		if (responseAuthReq.getPersonalAttributeList() == null
				|| responseAuthReq.getPersonalAttributeList().isEmpty()) {
			LOG.error("PersonalAttributeList is null or empty.");
			throw new STORKSAMLEngineException(
					"PersonalAttributeList is null or empty.");
		}

		if (StringUtils.isBlank(request.getAssertionConsumerServiceURL())) {
			throw new STORKSAMLEngineException(
					"assertionConsumerServiceURL is null or empty.");
		}

		if (StringUtils.isBlank(request.getSamlId())) {
			throw new STORKSAMLEngineException("request ID is null or empty.");
		}
	}

	/**
	 * Validate parameters from response.
	 * 
	 * @param request the request
	 * @param responseAttrQueryReq the response authentication request
	 * 
	 * @throws STORKSAMLEngineException the STORKSAML engine exception
	 */
	private void validateParamAttrQueryResponse(final STORKAttrQueryRequest request,
			final STORKAttrQueryResponse responseAttrQueryReq)
					throws STORKSAMLEngineException {
		LOG.info("Validate attribute querey parameters response.");
		if (StringUtils.isBlank(request.getIssuer())) {
			throw new STORKSAMLEngineException(
					"Issuer must be not empty or null.");
		}

		if (responseAttrQueryReq.getPersonalAttributeList() == null
				|| responseAttrQueryReq.getPersonalAttributeList().isEmpty()) {
			LOG.error("PersonalAttributeList is null or empty.");
			throw new STORKSAMLEngineException(
					"PersonalAttributeList is null or empty.");
		}

		/*if (StringUtils.isBlank(request.getAssertionConsumerServiceURL())) {
			throw new STORKSAMLEngineException(
					"assertionConsumerServiceURL is null or empty.");
		}*/

		if (StringUtils.isBlank(request.getSamlId())) {
			throw new STORKSAMLEngineException("request ID is null or empty.");
		}
	}

	/**
	 * Validate parameter from response fail.
	 * 
	 * @param request the request
	 * @param response the response
	 * 
	 * @throws STORKSAMLEngineException the STORKSAML engine exception
	 */
	private void validateParamResponseFail(final STORKAuthnRequest request,
			final STORKAuthnResponse response) throws STORKSAMLEngineException {
		LOG.info("Validate parameters response fail.");
		if (StringUtils.isBlank(response.getStatusCode())) {
			throw new STORKSAMLEngineException("Code error it's null or empty.");
		}

		if (StringUtils.isBlank(request.getAssertionConsumerServiceURL())) {
			throw new STORKSAMLEngineException(
					"assertionConsumerServiceURL is null or empty.");
		}

		if (StringUtils.isBlank(request.getSamlId())) {
			throw new STORKSAMLEngineException("request ID is null or empty.");
		}
	}

	/**
	 * Validate parameter from response fail.
	 * 
	 * @param request the request
	 * @param response the response
	 * 
	 * @throws STORKSAMLEngineException the STORKSAML engine exception
	 */
	private void validateParamAttrQueryResponseFail(final STORKAttrQueryRequest request,
			final STORKAttrQueryResponse response) throws STORKSAMLEngineException {
		LOG.info("Validate parameters response fail.");
		if (StringUtils.isBlank(response.getStatusCode())) {
			throw new STORKSAMLEngineException("Code error it's null or empty.");
		}

		if (StringUtils.isBlank(request.getSamlId())) {
			throw new STORKSAMLEngineException("request ID is null or empty.");
		}
	}

	/**
	 * Validate parameter from response fail.
	 * 
	 * @param request the request
	 * @param response the response
	 * 
	 * @throws STORKSAMLEngineException the STORKSAML engine exception
	 */
	private void validateParamLogoutResFail(final STORKLogoutRequest request,
			final STORKLogoutResponse response) throws STORKSAMLEngineException {
		LOG.info("Validate parameters response fail.");

		if (StringUtils.isBlank(request.getIssuer())) {
			throw new STORKSAMLEngineException(
					"Issuer must be not empty or null.");
		}

		if (StringUtils.isBlank(response.getStatusCode())) {
			throw new STORKSAMLEngineException("Code error it's null or empty.");
		}

		if (StringUtils.isBlank(request.getSamlId())) {
			throw new STORKSAMLEngineException("request ID is null or empty.");
		}
	}

	/**
	 * Validate stork authentication request.
	 * 
	 * @param tokenSaml the token SAML
	 * 
	 * @return the sTORK authentication request
	 * 
	 * @throws STORKSAMLEngineException the STORKSAML engine exception
	 */
	public STORKAuthnRequest validateSTORKAuthnRequest(final byte[] tokenSaml, String aliasFilter)
			throws STORKSAMLEngineException {
		LOG.info("validateSTORKAuthnRequest");

		final AuthnRequest samlRequest = (AuthnRequest) validateStorkSaml(tokenSaml);

		LOG.debug("Validate Extensions.");
		final Validator<Extensions> validatorExt = new ExtensionsSchemaValidator();
		try {
			validatorExt.validate(samlRequest.getExtensions());
		} catch (ValidationException e) {
			LOG.error("ValidationException: validate Extensions.", e);
			throw new STORKSAMLEngineException(e);
		}

		LOG.debug("Generate STORKAuthnRequest.");
		final STORKAuthnRequest authnRequest = processExtensions(samlRequest
				.getExtensions());

		authnRequest.setCountry(this.getCountry(samlRequest.getSignature()
				.getKeyInfo()));

		authnRequest.setAlias(this.getAlias(samlRequest.getSignature()
				.getKeyInfo(), super.getSigner().getTrustStore(), aliasFilter));

		authnRequest.setSamlId(samlRequest.getID());
		authnRequest.setDestination(samlRequest.getDestination());
		authnRequest.setAssertionConsumerServiceURL(samlRequest
				.getAssertionConsumerServiceURL());

		authnRequest.setProviderName(samlRequest.getProviderName());
		authnRequest.setIssuer(samlRequest.getIssuer().getValue());
		authnRequest.setForceAuthN(samlRequest.isForceAuthn());

		//Delete unknown elements from requested ones
		final Iterator<PersonalAttribute> iterator = authnRequest.getPersonalAttributeList().iterator();
		IPersonalAttributeList cleanPerAttrList = (PersonalAttributeList) authnRequest.getPersonalAttributeList();
		while (iterator.hasNext()) {

			final PersonalAttribute attribute = iterator.next();

			// Verify if the attribute name exits.
			final String attributeName = super.getSamlCoreProperties()
					.getProperty(attribute.getName());

			if (StringUtils.isBlank(attributeName)) {
				LOG.info("Attribute name: {} was not found. It will be removed from the request object", attribute.getName());
				cleanPerAttrList.remove(attribute.getName());
			}

		}	
		authnRequest.setPersonalAttributeList(cleanPerAttrList);

		return authnRequest;

	}

	/**
	 * Validate stork attribute query request.
	 * 
	 * @param tokenSaml the token SAML
	 * 
	 * @return the STORK attribute query request
	 * 
	 * @throws STORKSAMLEngineException the STORKSAML engine exception
	 */
	public STORKAttrQueryRequest validateSTORKAttrQueryRequest(final byte[] tokenSaml, String aliasFilter)
			throws STORKSAMLEngineException {
		LOG.info("validateSTORKAttrQueryRequest");

		//final AttributeQuery samlRequest = (AttributeQuery) validateStorkSaml(tokenSaml);
		final CustomRequestAbstractType samlRequest = (CustomRequestAbstractType) validateStorkSaml(tokenSaml);

		LOG.debug("Validate Extensions.");
		final Validator<Extensions> validatorExt = new ExtensionsSchemaValidator();
		try {
			validatorExt.validate(samlRequest.getExtensions());
		} catch (ValidationException e) {
			LOG.error("ValidationException: validate Extensions.", e);
			throw new STORKSAMLEngineException(e);
		}

		LOG.debug("Generate STORKAttrQueryRequest.");
		final STORKAttrQueryRequest attrRequest = processAttrExtensions(samlRequest
				.getExtensions());

		attrRequest.setCountry(this.getCountry(samlRequest.getSignature()
				.getKeyInfo()));

		attrRequest.setAlias(this.getAlias(samlRequest.getSignature()
				.getKeyInfo(), super.getSigner().getTrustStore(), aliasFilter));

		attrRequest.setSamlId(samlRequest.getID());
		attrRequest.setDestination(samlRequest.getDestination());
		attrRequest.setAssertionConsumerServiceURL(samlRequest
				.getAssertionConsumerServiceURL());

		/*authnRequest.setProviderName(samlRequest.getProviderName());*/
		attrRequest.setIssuer(samlRequest.getIssuer().getValue());

		//Delete unknown elements from requested ones
		final Iterator<PersonalAttribute> iterator = attrRequest.getPersonalAttributeList().iterator();
		IPersonalAttributeList cleanPerAttrList = (PersonalAttributeList) attrRequest.getPersonalAttributeList();
		while (iterator.hasNext()) {

			final PersonalAttribute attribute = iterator.next();

			// Verify if the attribute name exits.
			final String attributeName = super.getSamlCoreProperties()
					.getProperty(attribute.getName());

			if (StringUtils.isBlank(attributeName)) {
				LOG.info("Attribute name: {} was not found. It will be removed from the request object", attribute.getName());
				cleanPerAttrList.remove(attribute.getName());
			}

		}	
		attrRequest.setPersonalAttributeList(cleanPerAttrList);

		return attrRequest;

	}

	/**
	 * Validate stork logout request.
	 * 
	 * @param tokenSaml the token SAML
	 * 
	 * @return the STORK logout request
	 * 
	 * @throws STORKSAMLEngineException the STORKSAML engine exception
	 */
	public STORKLogoutRequest validateSTORKLogoutRequest(final byte[] tokenSaml, String aliasFilter)
			throws STORKSAMLEngineException {
		LOG.info("validateSTORKLogoutRequest");

		final LogoutRequest samlRequest = (LogoutRequest)validateStorkSaml(tokenSaml);

		if (samlRequest.getExtensions() != null) {
			LOG.debug("Validate Extensions.");
			final Validator<Extensions> validatorExt = new ExtensionsSchemaValidator();
			try {
				validatorExt.validate(samlRequest.getExtensions());
			} catch (ValidationException e) {
				LOG.error("ValidationException: validate Extensions.", e);
				throw new STORKSAMLEngineException(e);
			}
		}

		LOG.debug("Generate STORKLogoutRequest.");
		final STORKLogoutRequest logoutRequest = new STORKLogoutRequest();

		logoutRequest.setCountry(this.getCountry(samlRequest.getSignature()
				.getKeyInfo()));

		logoutRequest.setAlias(this.getAlias(samlRequest.getSignature()
				.getKeyInfo(), super.getSigner().getTrustStore(), aliasFilter));

		logoutRequest.setSamlId(samlRequest.getID());
		logoutRequest.setDestination(samlRequest.getDestination());

		logoutRequest.setIssuer(samlRequest.getIssuer().getValue());

		logoutRequest.setSpProvidedId(samlRequest.getNameID().getValue());

		return logoutRequest;

	}

	/**
	 * Validate stork logout response.
	 *
	 * @param tokenSaml The SAML token
	 *
	 * @return the STORK logout response
	 *
	 * @throws STORKSAMLEngineException the STORKSAML engine exception
	 */
	public STORKLogoutResponse validateSTORKLogoutResponse(final byte[] tokenSaml, String aliasFilter) throws STORKSAMLEngineException {

		LOG.info("validate STORK Logout Response");

		final LogoutResponse samlRes = (LogoutResponse) validateStorkSaml(tokenSaml);

		LOG.debug("Generate STORKLogoutResponse.");
		final STORKLogoutResponse logoutRes = new STORKLogoutResponse();

		try {
			logoutRes.setTokenSaml(super.signAndMarshall(samlRes));
		} catch (SAMLEngineException e) {
			LOG.error("Sign and Marshall.", e);
			throw new STORKSAMLEngineException(e);
		}

		logoutRes.setAlias(this.getAlias(samlRes.getSignature().getKeyInfo(), super.getSigner().getTrustStore(), aliasFilter));
		logoutRes.setSamlId(samlRes.getID());
		logoutRes.setDestination(samlRes.getDestination());
		logoutRes.setIssuer(samlRes.getIssuer().getValue());
		logoutRes.setStatusCode(samlRes.getStatus().getStatusCode().getValue().toString());
		logoutRes.setStatusMessage(samlRes.getStatus().getStatusMessage().getMessage().toString());
		logoutRes.setInResponseTo(samlRes.getInResponseTo());
		return logoutRes;
	}
	
	/**
	 * Validate stork authentication response.
	 * 
	 * @param tokenSaml the token SAML
	 * @param userIP the user IP
	 * 
	 * @return the Stork authentication response
	 * 
	 * @throws STORKSAMLEngineException the STORKSAML engine exception
	 */
	public STORKAuthnResponse validateSTORKAuthnResponse(
			final byte[] tokenSaml, final String userIP)
					throws STORKSAMLEngineException {

		LOG.info("validateSTORKAuthnResponse");
		final Response samlResponse = (Response) validateStorkSaml(tokenSaml);

		LOG.debug("Create StorkAuthResponse.");
		final STORKAuthnResponse authnResponse = new STORKAuthnResponse();

		authnResponse.setCountry(this.getCountry(samlResponse.getSignature()
				.getKeyInfo()));

		LOG.debug("Set ID.");
		authnResponse.setSamlId(samlResponse.getID());
		LOG.debug("Set InResponseTo.");
		authnResponse.setInResponseTo(samlResponse.getInResponseTo());
		LOG.debug("Set statusCode.");
		authnResponse.setStatusCode(samlResponse.getStatus().getStatusCode()
				.getValue());

		// Subordinate code.
		if (samlResponse.getStatus().getStatusCode().getStatusCode() != null) {
			authnResponse.setSubStatusCode(samlResponse.getStatus()
					.getStatusCode().getStatusCode().getValue());
		}

		if (samlResponse.getStatus().getStatusMessage() != null) {
			LOG.debug("Set statusMessage.");
			authnResponse.setMessage(samlResponse.getStatus()
					.getStatusMessage().getMessage());
		}

		LOG.debug("validateStorkResponse");
		final Assertion assertion = (Assertion) validateStorkResponse(
				samlResponse, userIP);	

		if(assertion!=null){
			final DateTime serverDate = new DateTime();

			if (assertion.getConditions().getNotOnOrAfter().isBefore(serverDate)) {
				LOG.error("Token date expired (getNotOnOrAfter =  "
						+ assertion.getConditions().getNotOnOrAfter()
						+ ", server_date: " + serverDate + ")");
				throw new STORKSAMLEngineException(
						"Token date expired (getNotOnOrAfter =  "
								+ assertion.getConditions().getNotOnOrAfter()
								+ " ), server_date: " + serverDate);
			}

			LOG.debug("Set notOnOrAfter.");
			authnResponse.setNotOnOrAfter(assertion.getConditions()
					.getNotOnOrAfter());

			LOG.debug("Set notBefore.");
			authnResponse.setNotBefore(assertion.getConditions().getNotBefore());

			authnResponse.setNotBefore(assertion.getConditions().getNotBefore());

			authnResponse.setAudienceRestriction(((AudienceRestriction) assertion
					.getConditions().getAudienceRestrictions().get(0))
					.getAudiences().get(0).getAudienceURI());
			authnResponse.setAssertions(samlResponse.getAssertions());
		}

		// Case no error.
		if (assertion!=null && StatusCode.SUCCESS_URI.equalsIgnoreCase(authnResponse
				.getStatusCode())) {
			LOG.debug("Status Success. Set PersonalAttributeList.");
			authnResponse
			.setPersonalAttributeList(generatePersonalAttributeList(assertion));
			authnResponse.setFail(false);
		} else {
			LOG.debug("Status Fail.");
			authnResponse.setFail(true);
		}		
		LOG.debug("Return result.");
		return authnResponse;
	}

	/**
	 * Validate stork authentication response.
	 * 
	 * @param tokenSaml the token SAML
	 * @param userIP the user IP
	 * 
	 * @return the Stork authentication response
	 * 
	 * @throws STORKSAMLEngineException the STORKSAML engine exception
	 */
	public STORKAuthnResponse validateSTORKAuthnResponseWithQuery(
			final byte[] tokenSaml, final String userIP)
					throws STORKSAMLEngineException {

		LOG.info("validateSTORKAuthnResponse");
		final Response samlResponse = (Response) validateStorkSaml(tokenSaml);

		LOG.debug("Create StorkAuthResponse.");
		final STORKAuthnResponse authnResponse = new STORKAuthnResponse();

		authnResponse.setCountry(this.getCountry(samlResponse.getSignature()
				.getKeyInfo()));

		LOG.debug("Set ID.");
		authnResponse.setSamlId(samlResponse.getID());
		LOG.debug("Set InResponseTo.");
		authnResponse.setInResponseTo(samlResponse.getInResponseTo());
		LOG.debug("Set statusCode.");
		authnResponse.setStatusCode(samlResponse.getStatus().getStatusCode()
				.getValue());

		// Subordinate code.
		if (samlResponse.getStatus().getStatusCode().getStatusCode() != null) {
			authnResponse.setSubStatusCode(samlResponse.getStatus()
					.getStatusCode().getStatusCode().getValue());
		}

		if (samlResponse.getStatus().getStatusMessage() != null) {
			LOG.debug("Set statusMessage.");
			authnResponse.setMessage(samlResponse.getStatus()
					.getStatusMessage().getMessage());
		}

		LOG.debug("validateStorkResponse");
		final Assertion assertion = (Assertion) validateStorkResponse(
				samlResponse, userIP);	

		if(assertion!=null){
			final DateTime serverDate = new DateTime();

			if (assertion.getConditions().getNotOnOrAfter().isBefore(serverDate)) {
				LOG.error("Token date expired (getNotOnOrAfter =  "
						+ assertion.getConditions().getNotOnOrAfter()
						+ ", server_date: " + serverDate + ")");
				throw new STORKSAMLEngineException(
						"Token date expired (getNotOnOrAfter =  "
								+ assertion.getConditions().getNotOnOrAfter()
								+ " ), server_date: " + serverDate);
			}

			LOG.debug("Set notOnOrAfter.");
			authnResponse.setNotOnOrAfter(assertion.getConditions()
					.getNotOnOrAfter());

			LOG.debug("Set notBefore.");
			authnResponse.setNotBefore(assertion.getConditions().getNotBefore());

			authnResponse.setNotBefore(assertion.getConditions().getNotBefore());

			authnResponse.setAudienceRestriction(((AudienceRestriction) assertion
					.getConditions().getAudienceRestrictions().get(0))
					.getAudiences().get(0).getAudienceURI());
		}

		// Case no error.
		if (assertion!=null && StatusCode.SUCCESS_URI.equalsIgnoreCase(authnResponse
				.getStatusCode())) {
			LOG.debug("Status Success. Set PersonalAttributeList.");
			authnResponse
			.setPersonalAttributeList(generatePersonalAttributeList(assertion));
			authnResponse.setFail(false);
		} else {
			LOG.debug("Status Fail.");
			authnResponse.setFail(true);
		}

		authnResponse.setAssertions(samlResponse.getAssertions());
		if (samlResponse.getAssertions().size() > 1)
		{
			PersonalAttributeList total = new PersonalAttributeList();
			List<IPersonalAttributeList> attrList = new ArrayList();
			for (int i = 0; i < samlResponse.getAssertions().size(); i++)
			{
				Assertion tempAssertion = (Assertion)samlResponse.getAssertions().get(i);
				IPersonalAttributeList temp = generatePersonalAttributeList(tempAssertion);
				if (temp != null)
				{ 
					attrList.add(temp);
					for (PersonalAttribute attribute : temp) {
						PersonalAttribute attr = (PersonalAttribute)attribute.clone();
						attr.setName(attr.getName()+tempAssertion.getID());
						total.add(attr);
					}
				}
			}
			authnResponse.setPersonalAttributeLists(attrList);
			authnResponse.setTotalPersonalAttributeList(total);
		}

		LOG.debug("Return result.");
		return authnResponse;

	}

	/**
	 * Validate stork attribute query response.
	 * 
	 * @param tokenSaml the token SAML
	 * @param userIP the user IP
	 * 
	 * @return the Stork attribute query response
	 * 
	 * @throws STORKSAMLEngineException the STORKSAML engine exception
	 */
	public STORKAttrQueryResponse validateSTORKAttrQueryResponse(
			final byte[] tokenSaml, final String userIP)
					throws STORKSAMLEngineException {

		LOG.info("validateSTORKAttrQueryResponse");
		final Response samlResponse = (Response) validateStorkSaml(tokenSaml);

		LOG.debug("Create StorkAttrQueryResponse.");
		final STORKAttrQueryResponse attrQueryResponse = new STORKAttrQueryResponse();

		attrQueryResponse.setCountry(this.getCountry(samlResponse.getSignature()
				.getKeyInfo()));

		LOG.debug("Set ID.");
		attrQueryResponse.setSamlId(samlResponse.getID());
		LOG.debug("Set InResponseTo.");
		attrQueryResponse.setInResponseTo(samlResponse.getInResponseTo());
		LOG.debug("Set statusCode.");
		attrQueryResponse.setStatusCode(samlResponse.getStatus().getStatusCode()
				.getValue());


		// Subordinate code.
		if (samlResponse.getStatus().getStatusCode().getStatusCode() != null) {
			attrQueryResponse.setSubStatusCode(samlResponse.getStatus()
					.getStatusCode().getStatusCode().getValue());
		}

		if (samlResponse.getStatus().getStatusMessage() != null) {
			LOG.debug("Set statusMessage.");
			attrQueryResponse.setMessage(samlResponse.getStatus()
					.getStatusMessage().getMessage());
		}

		LOG.debug("validateStorkResponse");
		final Assertion assertion = (Assertion) validateStorkResponse(
				samlResponse, userIP);	

		if(assertion!=null){
			final DateTime serverDate = new DateTime();

			attrQueryResponse.setAssertion(assertion);

			if (assertion.getConditions().getNotOnOrAfter().isBefore(serverDate)) {
				LOG.error("Token date expired (getNotOnOrAfter =  "
						+ assertion.getConditions().getNotOnOrAfter()
						+ ", server_date: " + serverDate + ")");
				throw new STORKSAMLEngineException(
						"Token date expired (getNotOnOrAfter =  "
								+ assertion.getConditions().getNotOnOrAfter()
								+ " ), server_date: " + serverDate);
			}

			LOG.debug("Set notOnOrAfter.");
			attrQueryResponse.setNotOnOrAfter(assertion.getConditions()
					.getNotOnOrAfter());

			LOG.debug("Set notBefore.");
			attrQueryResponse.setNotBefore(assertion.getConditions().getNotBefore());

			attrQueryResponse.setNotBefore(assertion.getConditions().getNotBefore());

			attrQueryResponse.setAudienceRestriction(((AudienceRestriction) assertion
					.getConditions().getAudienceRestrictions().get(0))
					.getAudiences().get(0).getAudienceURI());
		}

		// Case no error.
		if (assertion!=null && StatusCode.SUCCESS_URI.equalsIgnoreCase(attrQueryResponse
				.getStatusCode())) {
			LOG.debug("Status Success. Set PersonalAttributeList.");
			attrQueryResponse
			.setPersonalAttributeList(generatePersonalAttributeList(assertion));
			attrQueryResponse.setFail(false);
		} else {
			LOG.debug("Status Fail.");
			attrQueryResponse.setFail(true);
		}

		attrQueryResponse.setAssertions(samlResponse.getAssertions());
		if (samlResponse.getAssertions().size() > 1)
		{
			PersonalAttributeList total = new PersonalAttributeList();
			List<IPersonalAttributeList> attrList = new ArrayList();
			for (int i = 0; i < samlResponse.getAssertions().size(); i++)
			{
				Assertion tempAssertion = (Assertion)samlResponse.getAssertions().get(i);
				IPersonalAttributeList temp = generatePersonalAttributeList(tempAssertion);
				if (temp != null)
				{ 
					attrList.add(temp);
					for (PersonalAttribute attribute : temp) {
						PersonalAttribute attr = (PersonalAttribute)attribute.clone();
						attr.setName(attr.getName()+tempAssertion.getID());
						total.add(attr);
					}
				}
			}
			attrQueryResponse.setPersonalAttributeLists(attrList);
			attrQueryResponse.setTotalPersonalAttributeList(total);
		}

		LOG.debug("Return result.");
		return attrQueryResponse;

	}

	/**
	 * Validate stork response.
	 * 
	 * @param samlResponse the SAML response
	 * @param userIP the user IP
	 * 
	 * @return the assertion
	 * 
	 * @throws STORKSAMLEngineException the STORKSAML engine exception
	 */
	private Assertion validateStorkResponse(final Response samlResponse,
			final String userIP) throws STORKSAMLEngineException {
		// Exist only one Assertion

		if (samlResponse.getAssertions() == null
				|| samlResponse.getAssertions().isEmpty()) {
			LOG.info("Assertion is null or empty."); //in replace of throw new STORKSAMLEngineException("Assertion is null or empty.")
			return null;
		}

		final Assertion assertion = (Assertion) samlResponse.getAssertions()
				.get(0);

		LOG.debug("Verified method Bearer");
		for (final Iterator<SubjectConfirmation> iter = assertion.getSubject()
				.getSubjectConfirmations().iterator(); iter.hasNext();) {
			final SubjectConfirmation element = iter.next();
			final boolean isBearer = SubjectConfirmation.METHOD_BEARER
					.equals(element.getMethod());

			final boolean ipValidate = super.getSamlCoreProperties()
					.isIpValidation();

			if (ipValidate) {
				if (isBearer) {
					if (StringUtils.isBlank(userIP)) {
						LOG.error("browser_ip is null or empty.");
						throw new STORKSAMLEngineException(
								"browser_ip is null or empty.");
					} else if (StringUtils.isBlank(element
							.getSubjectConfirmationData().getAddress())) {
						LOG.error("token_ip attribute is null or empty.");
						throw new STORKSAMLEngineException(
								"token_ip attribute is null or empty.");
					}
				}

				final boolean ipEqual = element.getSubjectConfirmationData()
						.getAddress().equals(userIP);

				// Validation ipUser
				if (!ipEqual && ipValidate) {
					LOG.error("SubjectConfirmation BEARER: ");
					throw new STORKSAMLEngineException(
							"IPs doesn't match : token_ip ("
									+ element.getSubjectConfirmationData()
									.getAddress() + ") browser_ip ("
									+ userIP + ")");
				}
			}

		}
		return assertion;
	}

	/**
	 * Validate stork SAML.
	 * 
	 * @param tokenSaml the token SAML
	 * 
	 * @return the signable SAML object
	 * 
	 * @throws STORKSAMLEngineException the STORKSAML engine exception
	 */
	private SignableSAMLObject validateStorkSaml(final byte[] tokenSaml)
			throws STORKSAMLEngineException {

		LOG.info("Validate StorkSaml message.");

		if (tokenSaml == null) {
			LOG.error("Saml authentication request is null.");
			throw new STORKSAMLEngineException(
					"Saml authentication request is null.");
		}

		LOG.debug("Generate AuthnRequest from request.");
		SignableSAMLObject samlObject;

		try {			
			samlObject = (SignableSAMLObject) super.unmarshall(tokenSaml);
		} catch (SAMLEngineException e) {
			LOG.error("SAMLEngineException unmarshall.", e);
			throw new STORKSAMLEngineException(e);
		}

		boolean validateSign = true;

		if (StringUtils.isNotBlank(super.getSamlCoreProperties().getProperty(
				"validateSignature"))) {
			validateSign = Boolean.valueOf(super.getSamlCoreProperties()
					.getProperty("validateSignature"));
		}

		if (validateSign) {
			LOG.debug("Validate Signature.");
			try {
				super.validateSignature(samlObject);
			} catch (SAMLEngineException e) {
				LOG.error("SAMLEngineException validateSignature.", e);
				throw new STORKSAMLEngineException(e);
			}
		}

		LOG.debug("Validate Schema.");
		final ValidatorSuite validatorSuite = Configuration
				.getValidatorSuite("saml2-core-schema-validator");
		try {
			if (samlObject.getElementQName().toString().endsWith(CustomAttributeQuery.DEFAULT_ELEMENT_LOCAL_NAME))
			{
				CustomAttributeQueryValidator val = 
						new CustomAttributeQueryValidator();
				val.validate((CustomAttributeQuery)samlObject);
			}
			else
				validatorSuite.validate(samlObject);
		} catch (ValidationException e) {
			LOG.error("ValidationException.", e);
			throw new STORKSAMLEngineException(e);
		}

		return samlObject;
	}

	public Certificate getTrustedCertificate(String alias) {
		try {
			return super.getSigner().getTrustStore().getCertificate(alias);
		} catch (KeyStoreException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	private boolean isComplex(XMLObject xmlObject)
	{
		boolean isComplex = false;

		final XSAnyImpl complexValue = (XSAnyImpl) xmlObject;

		for (int nextComplexValue = 0; nextComplexValue < complexValue
				.getUnknownXMLObjects().size(); nextComplexValue++) {

			final XSAnyImpl simple = (XSAnyImpl) complexValue
					.getUnknownXMLObjects().get(
							nextComplexValue);

			if (simple.getElementQName().getLocalPart() != null)
			{
				isComplex = true;
				break;
			}
		}

		return isComplex;
	}	
}

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

package eu.stork.peps.auth.engine;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.commons.lang.StringUtils;
import org.joda.time.DateTime;
import org.opensaml.Configuration;
import org.opensaml.common.SAMLVersion;
import org.opensaml.common.impl.SecureRandomIdentifierGenerator;
import org.opensaml.saml2.common.Extensions;
import org.opensaml.saml2.common.impl.ExtensionsBuilder;
import org.opensaml.saml2.core.Assertion;
import org.opensaml.saml2.core.Attribute;
import org.opensaml.saml2.core.AttributeQuery;
import org.opensaml.saml2.core.AttributeValue;
import org.opensaml.saml2.core.AuthnContext;
import org.opensaml.saml2.core.AuthnRequest;
import org.opensaml.saml2.core.AuthnStatement;
import org.opensaml.saml2.core.Issuer;
import org.opensaml.saml2.core.LogoutRequest;
import org.opensaml.saml2.core.LogoutResponse;
import org.opensaml.saml2.core.NameID;
import org.opensaml.saml2.core.Response;
import org.opensaml.saml2.core.Status;
import org.opensaml.saml2.core.StatusCode;
import org.opensaml.saml2.core.StatusMessage;
import org.opensaml.saml2.core.Subject;
import org.opensaml.saml2.core.SubjectConfirmation;
import org.opensaml.saml2.core.SubjectConfirmationData;
import org.opensaml.saml2.core.SubjectLocality;
import org.opensaml.saml2.core.impl.AssertionBuilder;
import org.opensaml.xml.XMLObject;
import org.opensaml.xml.schema.XSAny;
import org.opensaml.xml.schema.XSString;
import org.opensaml.xml.signature.KeyInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import eu.stork.peps.auth.engine.core.CustomAttributeQuery;
import eu.stork.peps.auth.engine.core.QAAAttribute;
import eu.stork.peps.auth.engine.core.RequestedAttribute;
import eu.stork.peps.auth.engine.core.SAMLCore;
import eu.stork.peps.auth.engine.core.SPApplication;
import eu.stork.peps.auth.engine.core.SPCountry;
import eu.stork.peps.auth.engine.core.SPInstitution;
import eu.stork.peps.auth.engine.core.SPSector;
import eu.stork.peps.auth.engine.core.impl.CustomAttributeQueryBuilder;
import eu.stork.peps.exceptions.STORKSAMLEngineException;
import eu.stork.peps.exceptions.STORKSAMLEngineRuntimeException;

/**
 * The Class SAMLEngineUtils.
 * 
 * @author fjquevedo
 * @author iinigo
 */
public final class SAMLEngineUtils {

    /** The Constant UTF_8. */
    public static final String UTF_8 = "UTF-8";
    
    /** The Constant SHA_512. */
    public static final String SHA_512 = "SHA-512";
    

    /** The generator. */
    private static SecureRandomIdentifierGenerator generator;

    /** The Constant LOG. */
    private static final Logger LOG = LoggerFactory
	    .getLogger(SAMLEngineUtils.class.getName());

    /**
     * Method that generates a random value according to NCName grammar.
     * 
     * NCName ::= NCNameStartChar NCNameChar* NCNameChar ::= NameChar - ':'
     * NCNameStartChar ::= Letter | '_' NameStartChar ::= ":" | [A-Z] | "_" |
     * [a-z] | [#xC0-#xD6] | [#xD8-#xF6] | [#xF8-#x2FF] | [#x370-#x37D] |
     * [#x37F-#x1FFF] | [#x200C-#x200D] | [#x2070-#x218F] | [#x2C00-#x2FEF] |
     * [#x3001-#xD7FF] | [#xF900-#xFDCF] | [#xFDF0-#xFFFD] | [#x10000-#xEFFFF]
     * NameChar ::= NameStartChar | "-" | "." | [0-9] | #xB7 | [#x0300-#x036F] |
     * [#x203F-#x2040] Name ::= NameStartChar (NameChar)* Letter ::= BaseChar |
     * Ideographic BaseChar ::= [#x0041-#x005A] | [#x0061-#x007A] |
     * [#x00C0-#x00D6] | [#x00D8-#x00F6] | [#x00F8-#x00FF] | [#x0100-#x0131] |
     * [#x0134-#x013E] | [#x0141-#x0148] | [#x014A-#x017E] | [#x0180-#x01C3] |
     * [#x01CD-#x01F0] | [#x01F4-#x01F5] | [#x01FA-#x0217] | [#x0250-#x02A8] |
     * [#x02BB-#x02C1] | #x0386 | [#x0388-#x038A] | #x038C | [#x038E-#x03A1] |
     * [#x03A3-#x03CE] | [#x03D0-#x03D6] | #x03DA | #x03DC | #x03DE | #x03E0 |
     * [#x03E2-#x03F3] | [#x0401-#x040C] | [#x040E-#x044F] | [#x0451-#x045C] |
     * [#x045E-#x0481] | [#x0490-#x04C4] | [#x04C7-#x04C8] | [#x04CB-#x04CC] |
     * [#x04D0-#x04EB] | [#x04EE-#x04F5] | [#x04F8-#x04F9] | [#x0531-#x0556] |
     * #x0559 | [#x0561-#x0586] | [#x05D0-#x05EA] | [#x05F0-#x05F2] |
     * [#x0621-#x063A] | [#x0641-#x064A] | [#x0671-#x06B7] | [#x06BA-#x06BE] |
     * [#x06C0-#x06CE] | [#x06D0-#x06D3] | #x06D5 | [#x06E5-#x06E6] |
     * [#x0905-#x0939] | #x093D | [#x0958-#x0961] | [#x0985-#x098C] |
     * [#x098F-#x0990] | [#x0993-#x09A8] | [#x09AA-#x09B0] | #x09B2 |
     * [#x09B6-#x09B9] | [#x09DC-#x09DD] | [#x09DF-#x09E1] | [#x09F0-#x09F1] |
     * [#x0A05-#x0A0A] | [#x0A0F-#x0A10] | [#x0A13-#x0A28] | [#x0A2A-#x0A30] |
     * [#x0A32-#x0A33] | [#x0A35-#x0A36] | [#x0A38-#x0A39] | [#x0A59-#x0A5C] |
     * #x0A5E | [#x0A72-#x0A74] | [#x0A85-#x0A8B] | #x0A8D | [#x0A8F-#x0A91] |
     * [#x0A93-#x0AA8] | [#x0AAA-#x0AB0] | [#x0AB2-#x0AB3] | [#x0AB5-#x0AB9] |
     * #x0ABD | #x0AE0 | [#x0B05-#x0B0C] | [#x0B0F-#x0B10] | [#x0B13-#x0B28] |
     * [#x0B2A-#x0B30] | [#x0B32-#x0B33] | [#x0B36-#x0B39] | #x0B3D |
     * [#x0B5C-#x0B5D] | [#x0B5F-#x0B61] | [#x0B85-#x0B8A] | [#x0B8E-#x0B90] |
     * [#x0B92-#x0B95] | [#x0B99-#x0B9A] | #x0B9C | [#x0B9E-#x0B9F] |
     * [#x0BA3-#x0BA4] | [#x0BA8-#x0BAA] | [#x0BAE-#x0BB5] | [#x0BB7-#x0BB9] |
     * [#x0C05-#x0C0C] | [#x0C0E-#x0C10] | [#x0C12-#x0C28] | [#x0C2A-#x0C33] |
     * [#x0C35-#x0C39] | [#x0C60-#x0C61] | [#x0C85-#x0C8C] | [#x0C8E-#x0C90] |
     * [#x0C92-#x0CA8] | [#x0CAA-#x0CB3] | [#x0CB5-#x0CB9] | #x0CDE |
     * [#x0CE0-#x0CE1] | [#x0D05-#x0D0C] | [#x0D0E-#x0D10] | [#x0D12-#x0D28] |
     * [#x0D2A-#x0D39] | [#x0D60-#x0D61] | [#x0E01-#x0E2E] | #x0E30 |
     * [#x0E32-#x0E33] | [#x0E40-#x0E45] | [#x0E81-#x0E82] | #x0E84 |
     * [#x0E87-#x0E88] | #x0E8A | #x0E8D | [#x0E94-#x0E97] | [#x0E99-#x0E9F] |
     * [#x0EA1-#x0EA3] | #x0EA5 | #x0EA7 | [#x0EAA-#x0EAB] | [#x0EAD-#x0EAE] |
     * #x0EB0 | [#x0EB2-#x0EB3] | #x0EBD | [#x0EC0-#x0EC4] | [#x0F40-#x0F47] |
     * [#x0F49-#x0F69] | [#x10A0-#x10C5] | [#x10D0-#x10F6] | #x1100 |
     * [#x1102-#x1103] | [#x1105-#x1107] | #x1109 | [#x110B-#x110C] |
     * [#x110E-#x1112] | #x113C | #x113E | #x1140 | #x114C | #x114E | #x1150 |
     * [#x1154-#x1155] | #x1159 | [#x115F-#x1161] | #x1163 | #x1165 | #x1167 |
     * #x1169 | [#x116D-#x116E] | [#x1172-#x1173] | #x1175 | #x119E | #x11A8 |
     * #x11AB | [#x11AE-#x11AF] | [#x11B7-#x11B8] | #x11BA | [#x11BC-#x11C2] |
     * #x11EB | #x11F0 | #x11F9 | [#x1E00-#x1E9B] | [#x1EA0-#x1EF9] |
     * [#x1F00-#x1F15] | [#x1F18-#x1F1D] | [#x1F20-#x1F45] | [#x1F48-#x1F4D] |
     * [#x1F50-#x1F57] | #x1F59 | #x1F5B | #x1F5D | [#x1F5F-#x1F7D] |
     * [#x1F80-#x1FB4] | [#x1FB6-#x1FBC] | #x1FBE | [#x1FC2-#x1FC4] |
     * [#x1FC6-#x1FCC] | [#x1FD0-#x1FD3] | [#x1FD6-#x1FDB] | [#x1FE0-#x1FEC] |
     * [#x1FF2-#x1FF4] | [#x1FF6-#x1FFC] | #x2126 | [#x212A-#x212B] | #x212E |
     * [#x2180-#x2182] | [#x3041-#x3094] | [#x30A1-#x30FA] | [#x3105-#x312C] |
     * [#xAC00-#xD7A3] Ideographic ::= [#x4E00-#x9FA5] | #x3007 |
     * [#x3021-#x3029]
     * 
     * @return Random ID value
     */

    //Initialization of a generator of identifiers for all token SAML.

    static {
	loadRandomIdentifierGenerator();
    }


    /**
     * Load random identifier generator.
     *
     *@throws STORKSAMLEngineRuntimeException the STORKSAML engine runtime exception
     */
    private static void loadRandomIdentifierGenerator() {

	try {
	    generator = new SecureRandomIdentifierGenerator();
	} catch (NoSuchAlgorithmException ex) {
	    LOG.error("Error init SecureRandomIdentifierGenerator", ex);
	    throw new STORKSAMLEngineRuntimeException(ex);
	}

    }

    /**
     * Creates the SAML object.
     * 
     * @param qname the QName
     * 
     * @return the XML object
     */
    public static XMLObject createSamlObject(final QName qname) {
    	if (qname.toString().endsWith(CustomAttributeQuery.DEFAULT_ELEMENT_LOCAL_NAME))
    	{
    		CustomAttributeQueryBuilder builder = new CustomAttributeQueryBuilder();
    		return builder.buildObject(qname);
    	}
    	else
    	{
    		return Configuration.getBuilderFactory().getBuilder(qname).buildObject(
    				qname);
    	}
    }

    /**
     * Creates the SAML object.
     * 
     * @param qname the quality name
     * @param qname1 the qname1
     * 
     * @return the xML object
     */
    public static XMLObject createSamlObject(final QName qname,
	    final QName qname1) {
	return Configuration.getBuilderFactory().getBuilder(qname1)
		.buildObject(qname, qname1);
    }

    /**
     * Encode value with an specific algorithm.
     * 
     * @param value the value
     * @param alg the algorithm
     * 
     * @return the string
     * 
     * @throws STORKSAMLEngineException the STORKSAML engine exception
     */
    public static String encode(final String value, final String alg)
	    throws STORKSAMLEngineException {
	LOG.debug("Encode value with  " + alg + " algorithm.");
	byte[] buffer;
	
	final StringBuffer hash = new StringBuffer("");
	try {
	    buffer = value.getBytes(UTF_8);
	    MessageDigest msgDig;
	    msgDig = MessageDigest.getInstance(alg);

	    
	    msgDig.update(buffer);
	    final byte[] digest = msgDig.digest();

	    final int signedByte = 0xff;
	    for (byte aux : digest) {
		final int byt = aux & signedByte;
		if (Integer.toHexString(byt).length() == 1) {
		    hash.append('0');
		}
		hash.append(Integer.toHexString(byt));
	    }
	    
	} catch (UnsupportedEncodingException e1) {
	    LOG.error("UnsupportedEncodingException: " + UTF_8);
	    throw new STORKSAMLEngineException(e1);
	} catch (NoSuchAlgorithmException e) {
	    LOG.error("NoSuchAlgorithmException: " + alg);
	    throw new STORKSAMLEngineException(e);
	}
	
	return hash.toString();
    }

    /**
     * Generate assertion.
     * 
     * @param version the version
     * @param identifier the identifier
     * @param issueInstant the issue instant
     * @param issuer the issuer
     * 
     * @return the assertion
     */
    public static Assertion generateAssertion(final SAMLVersion version,
	    final String identifier, final DateTime issueInstant,
	    final Issuer issuer) {
	final AssertionBuilder assertionBuilder = new AssertionBuilder();
	final Assertion assertion = assertionBuilder.buildObject();
	assertion.setVersion(version);
	assertion.setID(identifier);
	assertion.setIssueInstant(issueInstant);

	// <saml:Issuer>
	assertion.setIssuer(issuer);
	return assertion;
    }

    /**
     * Generate authentication statement.
     * 
     * @param authnInstant the authentication instant
     * @param authnContext the authentication context
     * 
     * @return the authentication statement
     */
    public static AuthnStatement generateAthnStatement(final DateTime authnInstant,
	    final AuthnContext authnContext) {
	// <saml:AuthnStatement>
	final AuthnStatement authnStatement = (AuthnStatement) SAMLEngineUtils
		.createSamlObject(AuthnStatement.DEFAULT_ELEMENT_NAME);

	authnStatement.setAuthnInstant(authnInstant);
	authnStatement.setAuthnContext(authnContext);

	return authnStatement;
    }
    
    
    
    
    
    /**
     * Generate attribute from a list of values.
     *
     * @param name the name of the attribute.
     * @param status the status of the parameter: "Available", "NotAvailable" or
     * "Withheld".
     * @param values the value of the attribute.
     * @param isHashing the is hashing with "SHA-512" algorithm.
     * @return the attribute
     * @throws STORKSAMLEngineException the STORKSAML engine exception
     */
    public static Attribute generateAttrComplex(final String name,
	    final String status, final Map<String, String> values,
	    final boolean isHashing) throws STORKSAMLEngineException {
	LOG.debug("Generate attribute complex: " + name);
	final Attribute attribute = (Attribute) SAMLEngineUtils
		.createSamlObject(Attribute.DEFAULT_ELEMENT_NAME);

	attribute.setName(name);
	attribute.setNameFormat(Attribute.URI_REFERENCE);

	attribute.getUnknownAttributes().put(
		new QName(SAMLCore.STORK10_NS.getValue(), "AttributeStatus",
			SAMLCore.STORK10_PREFIX.getValue()), status);

	if (!values.isEmpty()) {
	    LOG.debug("Add attribute values.");

	    // Create an attribute that contains all XSAny elements.
	    final XSAny attrValue = (XSAny) SAMLEngineUtils.createSamlObject(
		    AttributeValue.DEFAULT_ELEMENT_NAME, XSAny.TYPE_NAME);

	    final Iterator<Entry<String, String>> iterator = values.entrySet()
		    .iterator();
	    while (iterator.hasNext()) {
		final Map.Entry<String, String> pairs = iterator.next();

		final String value = pairs.getValue();

		if (StringUtils.isNotBlank(value)) {
		    // Create the attribute statement
		    final XSAny attrValueSimple = (XSAny) SAMLEngineUtils
			    .createSamlObject(new QName(SAMLCore.STORK10_NS.getValue(),
				    pairs.getKey().toString(),
				    SAMLCore.STORK10_PREFIX.getValue()), XSAny.TYPE_NAME);

		    // if it's necessary encode the information.
		    if (isHashing) {
			attrValueSimple
				.setTextContent(encode(value, SHA_512));
		    } else {
		    	attrValueSimple.setTextContent(value);
		    }		    

		    attrValue.getUnknownXMLObjects().add(attrValueSimple);
		    attribute.getAttributeValues().add(attrValue);
		}
	    }

	}
	return attribute;
    }
    
    /**
     * Generate extension.
     * 
     * @return the extensions
     */
    public static Extensions generateExtension() {
	final ExtensionsBuilder extensionsBuilder = new ExtensionsBuilder();
	return extensionsBuilder.buildObject(
		"urn:oasis:names:tc:SAML:2.0:protocol", "Extensions", "saml2p");
    }
    
    
    
    
    /**
     * Generate issuer.
     * 
     * @return the issuer
     */
    public static Issuer generateIssuer() {
	return (Issuer) SAMLEngineUtils
		.createSamlObject(Issuer.DEFAULT_ELEMENT_NAME);
    }

    /**
     * Generate key info.
     * 
     * @return the key info
     */
    public static KeyInfo generateKeyInfo() {
	return (KeyInfo) SAMLEngineUtils
		.createSamlObject(KeyInfo.DEFAULT_ELEMENT_NAME);
    }

    /**
     * Generate name id.
     * 
     * @return the name id
     */
    public static NameID generateNameID() {
	return (NameID) SAMLEngineUtils
		.createSamlObject(NameID.DEFAULT_ELEMENT_NAME);
    }

    /**
     * Generate name id.
     * 
     * @param nameQualifier the name qualifier
     * @param format the format
     * @param spNameQualifier the sP name qualifier
     * 
     * @return the name id
     */
    public static NameID generateNameID(final String nameQualifier,
	    final String format, final String spNameQualifier) {
	// <saml:NameID>
	final NameID nameId = (NameID) Configuration.getBuilderFactory()
		.getBuilder(NameID.DEFAULT_ELEMENT_NAME).buildObject(
			NameID.DEFAULT_ELEMENT_NAME);

	// optional
	nameId.setNameQualifier(nameQualifier);

	// optional
	nameId.setFormat(format);

	// optional
	nameId.setSPNameQualifier(spNameQualifier);

	return nameId;
    }

    /**
     * Generate NCName.
     * 
     * @return the string
     */
    public static String generateNCName() {
	return generator.generateIdentifier();
    }

    
    /**
     * Generate the quality authentication assurance level.
     * 
     * @param qaal the level of quality authentication assurance.
     * 
     * @return the quality authentication assurance attribute
     * 
     * @throws STORKSAMLEngineException the STORKSAML engine exception
     */
    public static QAAAttribute generateQAAAttribute(final int qaal)
	    throws STORKSAMLEngineException {
	LOG.debug("Generate QAAAttribute.");

	final QAAAttribute qaaAttribute = (QAAAttribute) SAMLEngineUtils
		.createSamlObject(QAAAttribute.DEF_ELEMENT_NAME);
	qaaAttribute.setQaaLevel(String.valueOf(qaal));
	return qaaAttribute;
    }

    /**
     * Generate requested attribute.
     * 
     * @param name the name
     * @param friendlyName the friendly name
     * @param isRequired the is required
     * @param value the value
     * 
     * @return the requested attribute
     */
    public static RequestedAttribute generateReqAuthnAttributeSimple(
	    final String name, final String friendlyName,
	    final String isRequired, final List<String> value) {
	LOG.debug("Generate the requested attribute.");

	final RequestedAttribute requested = (RequestedAttribute) SAMLEngineUtils
		.createSamlObject(RequestedAttribute.DEF_ELEMENT_NAME);
	requested.setName(name);
	requested.setNameFormat(RequestedAttribute.URI_REFERENCE);

	requested.setFriendlyName(friendlyName);

	requested.setIsRequired(isRequired);

	// The value is optional in an authentication request.
	if (!value.isEmpty()) {
	    for (int nextValue = 0; nextValue < value.size(); nextValue++) {
		final String valor = value.get(nextValue);
		if (StringUtils.isNotBlank(valor)) {			
		    	
			if(!name.equals("http://www.stork.gov.eu/1.0/signedDoc")){
		    	
		    	// Create the attribute statement
			    final XSAny attrValue = (XSAny) SAMLEngineUtils
				    .createSamlObject(
					    new QName(SAMLCore.STORK10_NS.getValue(),
						    "AttributeValue",
						    SAMLCore.STORK10_PREFIX.getValue()),
						    XSAny.TYPE_NAME);
		    	
		    	attrValue.setTextContent(valor.trim());
			    requested.getAttributeValues().add(attrValue);
		    	
		    }else{		    	
		    				    
			    DocumentBuilderFactory domFactory = DocumentBuilderFactory.newInstance();
			    domFactory.setNamespaceAware(true); 		 
				Document document = null;				
				DocumentBuilder builder;
				
				// Parse the signedDoc value into an XML DOM Document
				try {
					builder = domFactory.newDocumentBuilder();
					InputStream is;
					is = new ByteArrayInputStream(valor.trim().getBytes("UTF-8"));
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
											 
				// Create the XML statement(this will be overwritten with the previous DOM structure)
				final XSAny xmlValue = (XSAny) SAMLEngineUtils
				    .createSamlObject(
					    new QName(SAMLCore.STORK10_NS.getValue(),
						    "XMLValue",
						    SAMLCore.STORK10_PREFIX.getValue()),
						    XSAny.TYPE_NAME);	

				//Set the signedDoc XML content to this element
				xmlValue.setDOM(document.getDocumentElement());
			    			    
				// Create the attribute statement
				final XSAny attrValue = (XSAny) SAMLEngineUtils
				    .createSamlObject(
					    new QName(SAMLCore.STORK10_NS.getValue(),
						    "AttributeValue",
						    SAMLCore.STORK10_PREFIX.getValue()),
						    XSAny.TYPE_NAME);	
				
				//Add previous signedDocXML to the AttributeValue Element
				attrValue.getUnknownXMLObjects().add(xmlValue);
			    
				requested.getAttributeValues().add(attrValue);		    	
		    }


		}
	    }
	}

	return requested;
    }

    /**
     * Generate response.
     * 
     * @param version the version
     * @param identifier the identifier
     * @param issueInstant the issue instant
     * @param status the status
     * 
     * @return the response
     */
    public static Response generateResponse(final SAMLVersion version,
	    final String identifier, final DateTime issueInstant,
	    final Status status) {
	final Response response = (Response) SAMLEngineUtils
		.createSamlObject(Response.DEFAULT_ELEMENT_NAME);
	response.setID(identifier);
	response.setIssueInstant(issueInstant);
	response.setStatus(status);
	return response;
    }

    /**
     * Method that generates a SAML Authentication Request basing on the
     * provided information.
     * 
     * @param identifier the identifier
     * @param version the version
     * @param issueInstant the issue instant
     * 
     * @return the authentication request
     */
    public static AuthnRequest generateSAMLAuthnRequest(final String identifier,
	    final SAMLVersion version, final DateTime issueInstant) {
	LOG.debug("Generate basic authentication request.");
	final AuthnRequest authnRequest = (AuthnRequest) SAMLEngineUtils
		.createSamlObject(AuthnRequest.DEFAULT_ELEMENT_NAME);

	authnRequest.setID(identifier);
	authnRequest.setVersion(version);
	authnRequest.setIssueInstant(issueInstant);
	return authnRequest;
    }
    
    /*public static AttributeQuery generateSAMLAttrQueryRequest(final String identifier,
    	    final SAMLVersion version, final DateTime issueInstant) {
    	LOG.debug("Generate attribute query request.");
    	final AttributeQuery attrQueryRequest = (AttributeQuery) SAMLEngineUtils
    		.createSamlObject(AttributeQuery.DEFAULT_ELEMENT_NAME);

    	attrQueryRequest.setID(identifier);
    	attrQueryRequest.setVersion(version);
    	attrQueryRequest.setIssueInstant(issueInstant);
    	return attrQueryRequest;
        }*/
    
    public static CustomAttributeQuery generateSAMLAttrQueryRequest(final String identifier,
    	    final SAMLVersion version, final DateTime issueInstant) {
    	LOG.debug("Generate attribute query request.");
    	final CustomAttributeQuery attrQueryRequest = (CustomAttributeQuery) SAMLEngineUtils
    		.createSamlObject(CustomAttributeQuery.DEFAULT_ELEMENT_NAME);

    	attrQueryRequest.setID(identifier);
    	attrQueryRequest.setVersion(version);
    	attrQueryRequest.setIssueInstant(issueInstant);
    	return attrQueryRequest;
        }
    
    public static LogoutRequest generateSAMLLogoutRequest(final String identifier,
    	    final SAMLVersion version, final DateTime issueInstant) {
    	LOG.debug("Generate logout request.");
    	final LogoutRequest logoutRequest = (LogoutRequest)SAMLEngineUtils.
    		createSamlObject(LogoutRequest.DEFAULT_ELEMENT_NAME);
    	

    	logoutRequest.setID(identifier);
    	logoutRequest.setVersion(version);
    	logoutRequest.setIssueInstant(issueInstant);
    	return logoutRequest;
     }
    
    public static LogoutResponse generateSAMLLogoutResponse(final String identifier,
    	    final SAMLVersion version, final DateTime issueInstant, 
    	    final Status status, final String inResponseTo) {
    	LOG.debug("Generate logout response.");
    	final LogoutResponse logoutResponse = (LogoutResponse)SAMLEngineUtils.
    		createSamlObject(LogoutResponse.DEFAULT_ELEMENT_NAME);
    	
    	logoutResponse.setInResponseTo(inResponseTo);
    	logoutResponse.setStatus(status);
    	logoutResponse.setID(identifier);
    	logoutResponse.setVersion(version);
    	logoutResponse.setIssueInstant(issueInstant);
    	return logoutResponse;
     }

    /**
     * Generate service provider application.
     * 
     * @param spApplication the service provider application
     * 
     * @return the sP application
     * 
     * @throws STORKSAMLEngineException the STORKSAML engine exception
     */
    public static SPApplication generateSPApplication(final String spApplication)
	    throws STORKSAMLEngineException {
	LOG.debug("Generate SPApplication.");

	final SPApplication applicationAttr = (SPApplication) SAMLEngineUtils
		.createSamlObject(SPApplication.DEF_ELEMENT_NAME);
	applicationAttr.setSPApplication(spApplication);
	return applicationAttr;
    }

    /**
     * Generate service provider country.
     * 
     * @param spCountry the service provider country
     * 
     * @return the service provider country
     * 
     * @throws STORKSAMLEngineException the STORKSAML engine exception
     */
    public static SPCountry generateSPCountry(final String spCountry)
	    throws STORKSAMLEngineException {
	LOG.debug("Generate SPApplication.");

	final SPCountry countryAttribute = (SPCountry) SAMLEngineUtils
		.createSamlObject(SPCountry.DEF_ELEMENT_NAME);
	countryAttribute.setSPCountry(spCountry);
	return countryAttribute;
    }

    /**
     * Generate service provider institution.
     * 
     * @param spInstitution the service provider institution
     * 
     * @return the service provider institution
     * 
     * @throws STORKSAMLEngineException the STORKSAML engine exception
     */
    public static SPInstitution generateSPInstitution(final String spInstitution)
	    throws STORKSAMLEngineException {
	LOG.debug("Generate SPInstitution.");

	final SPInstitution institutionAttr = (SPInstitution) SAMLEngineUtils
		.createSamlObject(SPInstitution.DEF_ELEMENT_NAME);
	institutionAttr.setSPInstitution(spInstitution);
	return institutionAttr;
    }
   
    /**
     * Generate service provider sector.
     * 
     * @param spSector the service provider sector
     * 
     * @return the service provider sector
     * 
     * @throws STORKSAMLEngineException the STORKSAML engine exception
     */
    public static SPSector generateSPSector(final String spSector)
	    throws STORKSAMLEngineException {
	LOG.debug("Generate SPSector.");

	final SPSector sectorAttribute = (SPSector) SAMLEngineUtils
		.createSamlObject(SPSector.DEF_ELEMENT_NAME);
	sectorAttribute.setSPSector(spSector);
	return sectorAttribute;
    }

    /**
     * Generate status.
     * 
     * @param statusCode the status code
     * 
     * @return the status
     */
    public static Status generateStatus(final StatusCode statusCode) {
	final Status status = (Status) SAMLEngineUtils
		.createSamlObject(Status.DEFAULT_ELEMENT_NAME);
	status.setStatusCode(statusCode);
	return status;
    }
    
    /**
     * Generate status code.
     * 
     * @param value the value
     * 
     * @return the status code
     */
    public static StatusCode generateStatusCode(final String value) {
	final StatusCode statusCode = (StatusCode) SAMLEngineUtils
		.createSamlObject(StatusCode.DEFAULT_ELEMENT_NAME);
	statusCode.setValue(value);
	return statusCode;
    }

    
    /**
     * Generate status message.
     * 
     * @param message the message
     * 
     * @return the status message
     */
    public static StatusMessage generateStatusMessage(final String message) {
	final StatusMessage statusMessage = (StatusMessage) SAMLEngineUtils
		.createSamlObject(StatusMessage.DEFAULT_ELEMENT_NAME);
	statusMessage.setMessage(message);
	return statusMessage;
    }
    
    /**
     * Generate subject.
     * 
     * @return the subject
     */
    public static Subject generateSubject() {
	return (Subject) SAMLEngineUtils
		.createSamlObject(Subject.DEFAULT_ELEMENT_NAME);
    }
    
    /**
     * Generate subject confirmation.
     * 
     * @param method the method
     * @param data the data
     * 
     * @return the subject confirmation
     */
    public static SubjectConfirmation generateSubjectConfirmation(
	    final String method, final SubjectConfirmationData data) {	
	final SubjectConfirmation subjectConf = (SubjectConfirmation) Configuration
		.getBuilderFactory().getBuilder(
			SubjectConfirmation.DEFAULT_ELEMENT_NAME).buildObject(
			SubjectConfirmation.DEFAULT_ELEMENT_NAME);

	subjectConf.setMethod(method);

	subjectConf.setSubjectConfirmationData(data);

	return subjectConf;
    }
    

    /**
     * Generate subject confirmation data.
     * 
     * @param notOnOrAfter the not on or after
     * @param recipient the recipient
     * @param inResponseTo the in response to
     * 
     * @return the subject confirmation data
     */
    public static SubjectConfirmationData generateSubjectConfirmationData(
	    final DateTime notOnOrAfter, final String recipient,
	    final String inResponseTo) {
	final SubjectConfirmationData subjectConfData = (SubjectConfirmationData) SAMLEngineUtils
		.createSamlObject(SubjectConfirmationData.DEFAULT_ELEMENT_NAME);
	subjectConfData.setNotOnOrAfter(notOnOrAfter);
	subjectConfData.setRecipient(recipient);
	subjectConfData.setInResponseTo(inResponseTo);
	return subjectConfData;
    }

    
    /**
     * Generate subject locality.
     * 
     * @param address the address
     * 
     * @return the subject locality
     */
    public static SubjectLocality generateSubjectLocality(final String address) {
	final SubjectLocality subjectLocality = (SubjectLocality) SAMLEngineUtils
		.createSamlObject(SubjectLocality.DEFAULT_ELEMENT_NAME);
	subjectLocality.setAddress(address);
	return subjectLocality;
    }


    
    
    /**
     * Method that returns the current time.
     * 
     * @return the current time
     */
    public static DateTime getCurrentTime() {
	return new DateTime();
    }

    
    /**
     * Instantiates a new SAML engine utilities.
     */
    private SAMLEngineUtils() {
    }
    
}

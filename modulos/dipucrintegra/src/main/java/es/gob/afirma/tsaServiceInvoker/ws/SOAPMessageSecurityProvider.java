// Copyright (C) 2012-13 MINHAP, Gobierno de España
// This program is licensed and may be used, modified and redistributed under the terms
// of the European Public License (EUPL), either version 1.1 or (at your
// option) any later version as soon as they are approved by the European Commission.
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
// or implied. See the License for the specific language governing permissions and
// more details.
// You should have received a copy of the EUPL1.1 license
// along with this program; if not, you may find it at
// http://joinup.ec.europa.eu/software/page/eupl/licence-eupl

/**
 * <b>File:</b><p>es.gob.afirma.tsaServiceInvoker.ws.SOAPHeaderSecurityProvider.java.</p>
 * <b>Description:</b><p>Class that allows to secure and encrypt the SOAP requests.</p>
 * <b>Project:</b><p>@Firma and TS@ Web Services Integration Platform.</p>
 * <b>Date:</b><p>14/01/2014.</p>
 * @author Gobierno de España.
 * @version 1.0, 14/01/2014.
 */
package es.gob.afirma.tsaServiceInvoker.ws;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

/**
 * <p>Class that allows to secure and encrypt the SOAP requests.</p>
 * <b>Project:</b><p>@Firma and TS@ Web Services Integration Platform.</p>
 * @version 1.0, 14/01/2014.
 */
public final class SOAPMessageSecurityProvider {

    /**
     * Constant attribute that identifies the Web Services Security UsernameToken Profile, used to secure the SOAP messages.
     */
    public static final String AUTHENTICATION_USER_PASSWORD = "UserNameToken";

    /**
     * Constant attribute that identifies the Web Services Security X.509 Certificate Token Profile, used to secure the SOAP messages.
     */
    public static final String AUTHENTICATION_CERTIFICATE = "X509CertificateToken";

    /**
     * Constant attribute that identifies the Web Services Security SAML Token Profile, used to secure the SOAP messages.
     */
    public static final String AUTHENTICATION_SAML = "SAMLToken";

    /**
     * Constant attribute that identifies the <i>direct inclusion mode</i> (BinarySecurityToken) of the X.509 certificate for the Web Services Security X.509
     * Certificate Token Profile.
     */
    public static final String INCLUSION_METHOD_DIRECT = "Direct";

    /**
     * Constant attribute that identifies the <i>subject key identifier inclusion mode</i> (KeyIdentifier) of the X.509 certificate for the Web Services
     * Security X.509 Certificate Token Profile.
     */
    public static final String INCLUSION_METHOD_IDENTIFIER = "Identifier";

    /**
     * Constant attribute that identifies the <i>issuer and serial number inclusion mode</i> (X509IssuerSerial) of the X.509 certificate for the Web Services
     * Security X.509 Certificate Token Profile.
     */
    public static final String INCLUSION_METHOD_ISSUERSERIALNUMBER = "IssuerSerialNumber";

    /**
     * Constant attribute that identifies the <i>holder-of-key</i> mandatory subject confirmation method for the Web Services Security SAML Token Profile.
     */
    public static final String SAML_HOLDER_OF_KEY = "HOK";

    /**
     * Constant attribute that identifies the <i>sender-vouchez</i> mandatory subject confirmation method for the Web Services Security SAML Token Profile.
     */
    public static final String SAML_SENDER_VOUCHEZ = "SV";

    /**
     * Constant attribute that identifies the tag to replace with the value of the symmetric key to encrypt the SOAP requests.
     */
    public static final String TAG_ENCRYPT = "$$$ENCRYPT$$$";

    /**
     * Constant attribute that identifies the tag to replace with the value of the user to secure the SOAP requests with UsernameToken.
     */
    public static final String TAG_USER = "$$$USER$$$";

    /**
     * Constant attribute that identifies the tag to replace with the value of the password to secure the SOAP requests with UsernameToken.
     */
    public static final String TAG_PASSWORD = "$$$PASSWORD$$$";

    /**
     * Constant attribute that identifies the tag to replace with the alias of the symmetric key to encrypt the SOAP requests.
     */
    public static final String TAG_ALIAS = "$$$ALIAS$$$";

    /**
     * Constant attribute that identifies the tag to replace with the inclusion mode of the certificate used to secure the SOAP requests with X.509
     * Certificate Token.
     */
    public static final String TAG_REFERENCE_TYPE = "$$$REFERENCE_TYPE$$$";

    /**
     * Constant attribute that represents the security header to secure the SOAP requests with UsernameToken.
     */
    public static final String XML_USERNAMETOKEN = "<xwss:SecurityConfiguration dumpMessages=\"true\"  xmlns:xwss=\"http://java.sun.com/xml/ns/xwss/config\" >" + TAG_ENCRYPT + "<xwss:UsernameToken name=\"" + TAG_USER + "\" password=\"" + TAG_PASSWORD + "\"/><xwss:RequireSignature/></xwss:SecurityConfiguration>";

    /**
     * Constant attribute that represents the tag to encrypt the SOAP requests with symmetric key.
     */
    public static final String XML_SYMMETRIC_KEY = "<xwss:Encrypt><xwss:SymmetricKey keyAlias=\"" + TAG_ALIAS + "\"/><xwss:EncryptionTarget type=\"qname\" value=\"SOAP-BODY\"/></xwss:Encrypt>";

    /**
     * Constant attribute that represents the security header to secure the SOAP requests with X.509 Certificate Token.
     */
    public static final String XML_X509CERTIFICATETOKEN = "<xwss:SecurityConfiguration dumpMessages=\"true\"  xmlns:xwss=\"http://java.sun.com/xml/ns/xwss/config\" >" + TAG_ENCRYPT + "<xwss:Sign includeTimestamp=\"true\"><xwss:X509Token certificateAlias=\"xws-security\"  keyReferenceType=\"" + TAG_REFERENCE_TYPE + "\"/><xwss:SignatureTarget type=\"qname\" value=\"SOAP-BODY\"/></xwss:Sign><xwss:RequireSignature/></xwss:SecurityConfiguration>";

    /**
     * Constant attribute that represents the security header to secure the SOAP requests with SAML Token (holder-of-key).
     */
    public static final String XML_SAMLTOKEN_HOK = "<xwss:SecurityConfiguration dumpMessages=\"true\"  xmlns:xwss=\"http://java.sun.com/xml/ns/xwss/config\" >" + TAG_ENCRYPT + "<xwss:Sign includeTimestamp=\"false\"><xwss:SAMLAssertion type=\"HOK\"/><xwss:Target type=\"qname\">{http://schemas.xmlsoap.org/soap/envelope/}Body</xwss:Target></xwss:Sign><xwss:Timestamp/></xwss:SecurityConfiguration>";

    /**
     * Constant attribute that represents the security header to secure the SOAP requests with SAML Token (sender-vouchez).
     */
    public static final String XML_SAMLTOKEN_SV = "<xwss:SecurityConfiguration dumpMessages=\"true\" xmlns:xwss=\"http://java.sun.com/xml/ns/xwss/config\" >" + TAG_ENCRYPT + "<xwss:SAMLAssertion type=\"SV\" strId=\"SV-123\"/><xwss:Sign includeTimestamp=\"false\"><xwss:X509Token certificateAlias=\"xws-security-client\"/><xwss:Target type=\"qname\">{http://schemas.xmlsoap.org/soap/envelope/}Body</xwss:Target><xwss:SignatureTarget type=\"uri\" value=\"SV-123\"><xwss:Transform algorithm=\"http://docs.oasis-open.org/wss/2004/01/oasis-200401-wss-soap-message-security-1.0#STR-Transform\"><xwss:AlgorithmParameter name=\"CanonicalizationMethod\" value=\"http://www.w3.org/2001/10/xml-exc-c14n#\"/></xwss:Transform></xwss:SignatureTarget></xwss:Sign><xwss:Timestamp /></xwss:SecurityConfiguration>";

    /**
     * Constant attribute that represents the security header to secure the SOAP messages.
     */
    public static final String XML_XWSS = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><xwss:SecurityConfiguration xmlns:xwss=\"http://java.sun.com/xml/ns/xwss/config\"></xwss:SecurityConfiguration>";

    /**
     * Constructor method for the class SOAPMessageSecurityProvider.java.
     */
    private SOAPMessageSecurityProvider() {
    }

    /**
     * Method that obtains an {@link InputStream} as the security header to secure a SOAP message with UsernameToken.
     * @param userName Parameter that represents the user name.
     * @param userPassword Parameter that represents the user password.
     * @param useSymmetricKey Parameter that indicates if to use symmetric key to encrypt the SOAP message (true), or not (false).
     * @param symmetricKeyAlias Parameter that represents the alias of the symmetric key used to encrypt the SOAP message.
     * @return the {@link InputStream} as the security header to secure a SOAP message with UsernameToken.
     */
    public static InputStream generateXMLUserNameToken(String userName, String userPassword, boolean useSymmetricKey, String symmetricKeyAlias) {
	// Obtenemos el patrón XML para securizar la petición con Username Token
	String xmlPattern = XML_USERNAMETOKEN;
	// Establecemos el nombre de usuario
	xmlPattern = xmlPattern.replace(TAG_USER, userName);
	// Establecemos la contraseña del usuario
	xmlPattern = xmlPattern.replace(TAG_PASSWORD, userPassword);
	// Añadimos lo relativo al cifrado con clave simétrica, en caso de ser
	// necesario
	xmlPattern = encryptXMLPattern(xmlPattern, useSymmetricKey, symmetricKeyAlias);
	return generateInputStream(xmlPattern);
    }

    /**
     * Method that obtains an {@link InputStream} as the security header to secure a SOAP message with X.509 Certificate Token.
     * @param certificateInclussionMode Parameter that represents the certificate inclusion mode.
     * @param useSymmetricKey Parameter that indicates if to use symmetric key to encrypt the SOAP message (true), or not (false).
     * @param symmetricKeyAlias Parameter that represents the alias of the symmetric key used to encrypt the SOAP message.
     * @return the {@link InputStream} as the security header to secure a SOAP message with X.509 Certificate Token.
     */
    public static InputStream generateXMLX509CertificateToken(String certificateInclussionMode, boolean useSymmetricKey, String symmetricKeyAlias) {
	// Obtenemos el patrón XML para securizar la petición con X509
	// Certificate Token
	String xmlPattern = XML_X509CERTIFICATETOKEN;
	// Establecemos la manera de inclusión del certificado
	xmlPattern = xmlPattern.replace(TAG_REFERENCE_TYPE, certificateInclussionMode);
	// Añadimos lo relativo al cifrado con clave simétrica, en caso de ser
	// necesario
	xmlPattern = encryptXMLPattern(xmlPattern, useSymmetricKey, symmetricKeyAlias);
	return generateInputStream(xmlPattern);
    }

    /**
     * Method that obtains an {@link InputStream} as the security header to secure a SOAP message with SAML Token.
     * @param mandatorySubjectConfirmationMethod Parameter that represents the mandatory subject confirmation method.
     * @param useSymmetricKey Parameter that indicates if to use symmetric key to encrypt the SOAP message (true), or not (false).
     * @param symmetricKeyAlias Parameter that represents the alias of the symmetric key used to encrypt the SOAP message.
     * @return the {@link InputStream} as the security header to secure a SOAP message with SAML Token.
     */
    public static InputStream generateXMLSAMLToken(String mandatorySubjectConfirmationMethod, boolean useSymmetricKey, String symmetricKeyAlias) {
	String xmlPattern = null;
	// Obtenemos el patrón XML para securizar la petición con SAML Token
	if (mandatorySubjectConfirmationMethod.equals(SAML_HOLDER_OF_KEY)) {
	    xmlPattern = XML_SAMLTOKEN_HOK;
	} else {
	    xmlPattern = XML_SAMLTOKEN_SV;
	}
	// Añadimos lo relativo al cifrado con clave simétrica, en caso de ser
	// necesario
	xmlPattern = encryptXMLPattern(xmlPattern, useSymmetricKey, symmetricKeyAlias);
	return generateInputStream(xmlPattern);
    }

    /**
     * Method that obtains an {@link InputStream} from a String.
     * @param xmlInput Parameter that represents the source String.
     * @return the generated {@link InputStream}.
     */
    public static InputStream generateInputStream(String xmlInput) {
	InputStream is = null;
	try {
	    is = new ByteArrayInputStream(xmlInput.getBytes("UTF8"));
	} catch (UnsupportedEncodingException e) {
	    // Esta excepción se produciría si la codificación indicada fuera
	    // incorrecta, por lo que nunca se lanzará
	}
	return is;
    }

    /**
     * Method that obtains the XML pattern related to the encryption of the SOAP message.
     * @param xmlPattern Parameter that represents the security header to secure the SOAP message.
     * @param useSymmetricKey Parameter that indicates if to use symmetric key to encrypt the SOAP message (true), or not (false).
     * @param symmetricKeyAlias Parameter that represents the alias of the symmetric key used to encrypt the SOAP message.
     * @return the XML pattern related to the encryption, or not, of the SOAP message.
     */
    private static String encryptXMLPattern(String xmlPattern, boolean useSymmetricKey, String symmetricKeyAlias) {
	String res = xmlPattern;
	if (useSymmetricKey) {
	    // Si es necesario cifrar la petición con clave simétrica
	    res = res.replace(TAG_ENCRYPT, XML_SYMMETRIC_KEY);
	    res = res.replace(TAG_ALIAS, symmetricKeyAlias);
	} else {
	    // Si no es necesario cifrar la petición con clave simétrica
	    res = res.replace(TAG_ENCRYPT, "");
	}
	return res;
    }
}

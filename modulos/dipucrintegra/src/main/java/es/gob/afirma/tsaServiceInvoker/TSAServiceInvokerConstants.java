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
 * <b>File:</b><p>es.gob.afirma.tsaServiceInvoker.TSAServiceInvokerConstants.java.</p>
 * <b>Description:</b><p>Interface that defines all the constants related to the invocation of TS@ services (DSS and RFC 3161).</p>
 * <b>Project:</b><p>@Firma and TS@ Web Services Integration Platform.</p>
 * <b>Date:</b><p>09/01/2014.</p>
 * @author Gobierno de España.
 * @version 1.0, 09/01/2014.
 */
package es.gob.afirma.tsaServiceInvoker;

/**
 * <p>Interface that defines all the constants related to the invocation of TS@ services (DSS and RFC 3161).</p>
 * <b>Project:</b><p>@Firma and TS@ Web Services Integration Platform.</p>
 * @version 1.0, 09/01/2014.
 */
public interface TSAServiceInvokerConstants {

    /**
     * Constant attribute that identifies the call for a TS@ web service.
     */
    String TSA_SERVICE = "tsaService";

    /**
     * Constant attribute that identifies the application context.
     */
    String APPLICATION_NAME = "applicationName";

    /**
     * Constant attribute that identifies the name of the properties file where to configure the invoke of TS@ web services.
     */
    String TSA_INVOKER_PROPERTIES = "tsaServiceInvoker.properties";

    /**
     * Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ web services with the path
     * to the trusted keystore for secure connections.
     */
    String TRUSTEDSTORE_PATH = "com.trustedstorePath";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ web services with the password
     *  of the trusted keystore for secure connections.
     */
    String TRUSTEDSTORE_PASSWORD = "com.trustedstorePassword";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ web services with the path to
     *  the descriptor file of TS@ web services (wsdl file).
     */
    String WS_WSDL_PATH = "com.serviceWSDLPath";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ web services with the connection
     *  and request timeout for the TS@ web service, in milliseconds.
     */
    String WS_CALL_TIMEOUT_PROPERTY = "callTimeout";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ web services with the authorization
     *  method used to secure the SOAP requests to invoke the TS@ web service.
     */
    String WS_AUTHORIZATION_METHOD_PROPERTY = "authorizationMethod";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ web services with the user name
     *  used to secure the SOAP request with UsernameToken.
     */
    String WS_USERNAMETOKEN_USER_NAME_PROPERTY = "UserNameToken.userName";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ web services with the user password
     *  used to secure the SOAP request with UsernameToken.
     */
    String WS_USERNAMETOKEN_USER_PASSWORD_PROPERTY = "UserNameToken.userPassword";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ web services with the inclusion mode
     *  for the certificate used to secure the SOAP request with X509 Certificate Token.
     */
    String WS_X509CERTIFICATETOKEN_INCLUSION_METHOD = "X509CertificateToken.inclusionMethod";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ web services with the path of the
     *  keystore which contains the certificate used to secure the SOAP request with X509 Certificate Token.
     */
    String WS_X509CERTIFICATETOKEN_KEYSTORE_PATH = "X509CertificateToken.keystorePath";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ web services with the type of the
     *  keystore which contains the certificate used to secure the SOAP request with X509 Certificate Token.
     */
    String WS_X509CERTIFICATETOKEN_KEYSTORE_TYPE = "X509CertificateToken.keystoreType";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ web services with the password of the
     *  keystore which contains the certificate used to secure the SOAP request with X509 Certificate Token.
     */
    String WS_X509CERTIFICATETOKEN_KEYSTORE_PASSWORD = "X509CertificateToken.keystorePassword";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ web services with the alias of the
     *  private key used to secure the SOAP request with X509 Certificate Token.
     */
    String WS_X509CERTIFICATETOKEN_PRIVATE_KEY_ALIAS = "X509CertificateToken.privateKeyAlias";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ web services with the password of the
     *  private key used to secure the SOAP request with X509 Certificate Token.
     */
    String WS_X509CERTIFICATETOKEN_PRIVATE_KEY_PASSWORD = "X509CertificateToken.privateKeyPassword";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ web services with the mandatory subject
     *  confirmation method used to secure the SOAP request with SAML Token.
     */
    String WS_SAMLTOKEN_METHOD = "SAMLToken.method";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ web services with the path of the
     *  keystore which contains the certificate used to secure the SOAP request with SAML Token.
     */
    String WS_SAMLTOKEN_KEYSTORE_PATH = "SAMLToken.keystorePath";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ web services with the type of the
     *  keystore which contains the certificate used to secure the SOAP request with SAML Token.
     */
    String WS_SAMLTOKEN_KEYSTORE_TYPE = "SAMLToken.keystoreType";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ web services with the password of the
     *  keystore which contains the certificate used to secure the SOAP request with SAML Token.
     */
    String WS_SAMLTOKEN_KEYSTORE_PASSWORD = "SAMLToken.keystorePassword";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ web services with the alias of the
     *  private key used to secure the SOAP request with SAML Token.
     */
    String WS_SAMLTOKEN_PRIVATE_KEY_ALIAS = "SAMLToken.privateKeyAlias";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ web services with the password of the
     *  private key used to secure the SOAP request with SAML Token.
     */
    String WS_SAMLTOKEN_PRIVATE_KEY_PASSWORD = "SAMLToken.privateKeyPassword";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ web services with the indicator for
     *  encrypt with symmetric key, or not, the SOAP requests.
     */
    String WS_REQUEST_SYMMETRICKEY_USE = "request.symmetricKey.use";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ web services with the alias of the
     *  symmetric key used to encrypt the SOAP request.
     */
    String WS_REQUEST_SYMMETRICKEY_ALIAS = "request.symmetricKey.alias";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ web services with the value of the
     *  symmetric key used to encrypt the SOAP request.
     */
    String WS_REQUEST_SYMMETRICKEY_VALUE = "request.symmetricKey.value";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ web services with the path of the
     *  keystore which contains the certificate used to secure the SOAP responses from TS@ with X509 Certificate Token.
     */
    String WS_RESPONSE_KEYSTORE_PATH = "response.keystorePath";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ web services with the type of the
     *  keystore which contains the certificate used to secure the SOAP responses from TS@ with X509 Certificate Token.
     */
    String WS_RESPONSE_KEYSTORE_TYPE = "response.keystoreType";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ web services with the password of the
     *  keystore which contains the certificate used to secure the SOAP responses from TS@ with X509 Certificate Token.
     */
    String WS_RESPONSE_KEYSTORE_PASSWORD = "response.keystorePassword";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ web services with the alias of the
     *  certificate used to secure the SOAP responses from TS@ with X509 Certificate Token.
     */
    String WS_RESPONSE_CERTIFICATE_ALIAS = "response.certificateAlias";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ web services with the path of the
     *  keystore which contains the certificate used to secure the SOAP responses from TS@ with SAML Token.
     */
    String WS_RESPONSE_SAML_KEYSTORE_PATH = "response.SAML.keystorePath";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ web services with the type of the
     *  keystore which contains the certificate used to secure the SOAP responses from TS@ with SAML Token.
     */
    String WS_RESPONSE_SAML_KEYSTORE_TYPE = "response.SAML.keystoreType";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ web services with the password of the
     *  keystore which contains the certificate used to secure the SOAP responses from TS@ with SAML Token.
     */
    String WS_RESPONSE_SAML_KEYSTORE_PASSWORD = "response.SAML.keystorePassword";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ web services with the alias of the
     *  certificate used to secure the SOAP responses from TS@ with SAML Token.
     */
    String WS_RESPONSE_SAML_CERTIFICATE_ALIAS = "response.SAML.certificateAlias";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ web services with the alias of the
     *  symmetric key used to encrypt the SOAP responses from TS@.
     */
    String WS_RESPONSE_SYMMETRICKEY_ALIAS = "response.symmetricKey.alias";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ web services with the value of the
     *  symmetric key used to encrypt the SOAP responses from TS@.
     */
    String WS_RESPONSE_SYMMETRICKEY_VALUE = "response.symmetricKey.value";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ RFC 3161 service with the value of the
     *  host where is deployed the RFC 3161 service.
     */
    String RFC3161_HOST = "rfc3161.host";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ RFC 3161 service with the value of the
     *  timestamp policy OID to include in the request.
     */
    String RFC3161_TIMESTAMP_POLICY_OID = "rfc3161.timestampPolicyOID";
    
    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ RFC 3161 service with the value of the
     *  application OID to include in the request.
     */
    String RFC3161_APPLICATION_OID = "rfc3161.applicationOID";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ RFC 3161 service with the value of the
     *  timeout for the request.
     */
    String RFC3161_TIMEOUT = "rfc3161.Timeout";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ RFC 3161 service with the value of the
     *  hash algorithm used to encode the data.
     */
    String RFC3161_HASH_ALGORITHM = "rfc3161.shaAlgorithm";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ RFC 3161 service with the value of the
     *  port number where is deployed the RFC 3161 service.
     */
    String RFC3161_PORT_NUMBER = "rfc3161.portNumber";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ RFC 3161 service with the value of the
     *  port number where is deployed the RFC 3161 - HTTPS service.
     */
    String RFC3161_HTTPS_PORT_NUMBER = "rfc3161HTTPS.portNumber";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ RFC 3161 service with the value of the
     *  context where is deployed the RFC 3161 - HTTPS service.
     */
    String RFC3161_HTTPS_CONTEXT = "rfc3161HTTPS.context";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ RFC 3161 service with the indicator
     *  to use the client authentication for the RFC 3161 - HTTPS service, or not.
     */
    String RFC3161_HTTPS_AUTH = "rfc3161HTTPS.useAuthClient";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ RFC 3161 service with the path of the
     *  keystore which contains the certificate used to the client authentication for the RFC 3161 - HTTPS service.
     */
    String RFC3161_HTTPS_KEYSTORE_PATH = "rfc3161HTTPS.keystorePath";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ RFC 3161 service with the type of the
     *  keystore which contains the certificate used to the client authentication for the RFC 3161 - HTTPS service.
     */
    String RFC3161_HTTPS_KEYSTORE_TYPE = "rfc3161HTTPS.keystoreType";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ RFC 3161 service with the password 
     *  of the keystore which contains the certificate used to the client authentication for the RFC 3161 - HTTPS service.
     */
    String RFC3161_HTTPS_KEYSTORE_PASSWORD = "rfc3161HTTPS.keystorePassword";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ RFC 3161 service with the value of the
     *  port number where is deployed the RFC 3161 - SSL service.
     */
    String RFC3161_SSL_PORT_NUMBER = "rfc3161SSL.portNumber";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ RFC 3161 service with the path of the
     *  keystore which contains the certificate used to the client authentication for the RFC 3161 - SSL service.
     */
    String RFC3161_SSL_KEYSTORE_PATH = "rfc3161SSL.keystorePath";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ RFC 3161 service with the type of the
     *  keystore which contains the certificate used to the client authentication for the RFC 3161 - SSL service.
     */
    String RFC3161_SSL_KEYSTORE_TYPE = "rfc3161SSL.keystoreType";

    /**
     *  Constant attribute that identifies the key defined on the properties file where to configure the invoke of TS@ RFC 3161 service with the password 
     *  of the keystore which contains the certificate used to the client authentication for the RFC 3161 - SSL service.
     */
    String RFC3161_SSL_KEYSTORE_PASSWORD = "rfc3161SSL.keystorePassword";

    /** 
     * <p>Interface that defines all the modes to communicate with the TS@ RFC 3161 service.</p>
     * <b>Project:</b><p>@Firma and TS@ Web Services Integration Platform.</p>
     * @version 1.0, 22/01/2014.
     */
    interface RFC3161Protocol {

	/**
	 * Constant attribute that identifies the Transmission Control Protocol used to communicate with TS@ RFC 3161 service. 
	 */
	String TCP = "TCP";

	/**
	 * Constant attribute that identifies the Hypertext Transfer Protocol Secure used to communicate with TS@ RFC 3161 service. 
	 */
	String HTTPS = "HTTPS";

	/**
	 * Constant attribute that identifies the cryptographic protocol Secure Sockets Layer used to communicate with TS@ RFC 3161 service. 
	 */
	String SSL = "SSL";
    }

}

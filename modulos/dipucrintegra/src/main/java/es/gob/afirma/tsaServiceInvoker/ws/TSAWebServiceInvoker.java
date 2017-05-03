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
 * <b>File:</b><p>es.gob.afirma.tsaServiceInvoker.ws.TSAWebServiceInvoker.java.</p>
 * <b>Description:</b><p>Class that manages the invoke of TS@ web services.</p>
 * <b>Project:</b><p>@Firma and TS@ Web Services Integration Platform.</p>
 * <b>Date:</b><p>13/01/2014.</p>
 * @author Gobierno de España.
 * @version 1.0, 13/01/2014.
 */
package es.gob.afirma.tsaServiceInvoker.ws;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.Iterator;
import java.util.Properties;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.soap.MessageFactory;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Dispatch;
import javax.xml.ws.Service;

import org.apache.log4j.Logger;

import com.sun.xml.wss.ProcessingContext;
import com.sun.xml.wss.XWSSProcessor;
import com.sun.xml.wss.XWSSProcessorFactory;

import es.gob.afirma.i18n.ILogConstantKeys;
import es.gob.afirma.i18n.Language;
import es.gob.afirma.tsaServiceInvoker.TSAServiceInvokerConstants;
import es.gob.afirma.tsaServiceInvoker.TSAServiceInvokerException;
import es.gob.afirma.utils.GenericUtils;
import es.gob.afirma.utils.UtilsCertificate;
import es.gob.afirma.utils.UtilsKeystore;
import es.gob.afirma.utils.UtilsResources;

/**
 * <p>Class that manages the invoke of TS@ web services.</p>
 * <b>Project:</b><p>@Firma and TS@ Web Services Integration Platform.</p>
 * @version 1.0, 13/01/2014.
 */
public class TSAWebServiceInvoker {

    /**
     * Attribute that represents the object that manages the log of the class.
     */
    private static final Logger LOGGER = Logger.getLogger(TSAWebServiceInvoker.class);

    /**
     * Attribute that represents the properties defined on the configuration file.
     */
    private Properties properties;

    /**
     * Attribute that indicates if to use a symmetric key to encode the SOAP requests (true) or not (false).
     */
    private boolean useSymmetricKey = false;

    /**
     * Attribute that represents the alias of the symmetric key use to encode the SOAP requests.
     */
    private String symmetricKeyAlias;

    /**
     * Attribute that represents the alias of the symmetric key use to encode the SOAP requests.
     */
    private String symmetricKeyValue;

    /**
     * Constructor method for the class TSAWebServiceInvoker.java.
     * @param propertiesParam Parameter that represents the properties defined on the configuration file.
     */
    public TSAWebServiceInvoker(Properties propertiesParam) {
	properties = propertiesParam;
    }

    /**
     * Method that performs the invocation to a method form TS@ web services.
     * @param serviceName Parameter that represents the name of the service to invoke.
     * @param params List of parameters related to the method to invoke.
     * @return the response of TS@.
     * @throws TSAServiceInvokerException If the method fails.
     */
    public final Object performCall(String serviceName, Object[ ] params) throws TSAServiceInvokerException {

	// Rescatamos el nombre de aplicación seleccionada
	String applicationName = properties.getProperty(TSAServiceInvokerConstants.APPLICATION_NAME);

	// Calculamos la cabecera para acceder a las propiedades del servicio
	// web
	String propertiesHeader = applicationName == null ? "" : applicationName + ".";

	// Rescatamos la ruta al fichero descriptor de los servicios web de TS@
	String wsdlPath = properties.getProperty(TSAServiceInvokerConstants.WS_WSDL_PATH);
	checkSvcInvokerParams(TSAServiceInvokerConstants.WS_WSDL_PATH, wsdlPath);
	URL wsdlURL = null;
	try {
	    wsdlURL = new URL(TSAWebServiceInvoker.class.getResource("."), wsdlPath);
	} catch (MalformedURLException e) {
	    throw new TSAServiceInvokerException(Language.getFormatResIntegra(ILogConstantKeys.TWSI_LOG001, new Object[ ] { wsdlPath }), e);
	}
	LOGGER.info(Language.getFormatResIntegra(ILogConstantKeys.TWSI_LOG002, new Object[ ] { wsdlPath }));

	// Instanciamos el servicio
	Service service = Service.create(wsdlURL, new QName("http://www.map.es/TSA/V1/TSA.wsdl", serviceName));

	// Obtenemos el nombre del puerto para el servicio instanciado
	QName portName = null;
	Iterator<QName> it = service.getPorts();
	while (it.hasNext()) {
	    portName = it.next();
	}

	// Establecemos los datos relativos al almacén de claves para conexiones
	// seguras
	configureSSLTrustStore();

	// Rescatamos el tiempo de vida de las peticiones y respuestas
	String serviceTimeOut = properties.getProperty(propertiesHeader + TSAServiceInvokerConstants.WS_CALL_TIMEOUT_PROPERTY);
	checkSvcInvokerParams(TSAServiceInvokerConstants.WS_CALL_TIMEOUT_PROPERTY, serviceTimeOut);
	Integer timeOut = null;
	try {
	    timeOut = Integer.valueOf(serviceTimeOut);
	} catch (NumberFormatException e) {
	    throw new TSAServiceInvokerException(Language.getFormatResIntegra(ILogConstantKeys.TWSI_LOG003, new Object[ ] { serviceTimeOut }), e);
	}

	LOGGER.info(Language.getFormatResIntegra(ILogConstantKeys.TWSI_LOG004, new Object[ ] { timeOut }));

	TSACallBackHandler callBackHandler = new TSACallBackHandler();

	// Establecemos los valores relativos a usar cifrado, o no, con clave
	// simétrica
	configureSymmetricKeyAttributes(propertiesHeader, callBackHandler);

	// Establecemos los valores relativos al certificado usado para
	// securizar, con X509 Certificate Token, las respuestas SOAP desde la
	// plataforma TS@
	configureSOAPResponseCertificate(propertiesHeader, callBackHandler);

	// Establecemos los valores relativos al certificado usado para
	// securizar, con SAML Token, las respuestas SOAP desde la plataforma
	// TS@
	configureSOAPResponseSAMLCertificate(propertiesHeader, callBackHandler);

	// Establecemos el valor de la clave simétrica usada para encriptar las
	// respuestas SOAP desde la plataforma TS@
	configureSOAPResponseSymmetricKey(propertiesHeader, callBackHandler);

	// Rescatamos el tipo de securización para la petición SOAP
	String securityOption = properties.getProperty(propertiesHeader + TSAServiceInvokerConstants.WS_AUTHORIZATION_METHOD_PROPERTY);
	checkSvcInvokerParams(TSAServiceInvokerConstants.WS_AUTHORIZATION_METHOD_PROPERTY, securityOption);
	LOGGER.info(Language.getFormatResIntegra(ILogConstantKeys.TWSI_LOG005, new Object[ ] { securityOption }));

	// Configuramos la securización de la petición SOAP
	InputStream clientConfig = configureSecureSOAPRequest(securityOption, propertiesHeader, callBackHandler);

	// Obtenemos los parámetros obtenidos procesando la plantilla XML
	String templateXML = params[0].toString();
	// Eliminamos de la plantilla XML la cabecera XML
	templateXML = templateXML.replace("<?xml version=\"1.0\" encoding=\"UTF-8\"?>", "");
	// Definimos el mensaje SOAP
	String msgString = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><SOAP-ENV:Envelope xmlns:SOAP-ENV=\"http://schemas.xmlsoap.org/soap/envelope/\"><SOAP-ENV:Body>";
	msgString = msgString + templateXML;
	msgString = msgString + "</SOAP-ENV:Body></SOAP-ENV:Envelope>";
	// Eliminamos los saltos de línea, retornos de carro y sangrado
	msgString = msgString.replaceAll("[\n\r\t]", "");

	SOAPMessage message = null;
	Dispatch<SOAPMessage> smDispatch = null;
	ProcessingContext context = null;
	SOAPMessage response = null;
	XWSSProcessorFactory xwssProcessorFactory = null;
	try {
	    MessageFactory factory = MessageFactory.newInstance();
	    message = factory.createMessage();
	    message.getSOAPPart().setContent((Source) new StreamSource(new StringReader(msgString)));
	    message.saveChanges();
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    dbf.setNamespaceAware(true);
	    xwssProcessorFactory = XWSSProcessorFactory.newInstance();
	    XWSSProcessor cprocessor = xwssProcessorFactory.createProcessorForSecurityConfiguration(clientConfig, callBackHandler);
	    context = new ProcessingContext();
	    context.setSOAPMessage(message);
	    SOAPMessage secureMsg = cprocessor.secureOutboundMessage(context);

	    smDispatch = service.createDispatch(portName, SOAPMessage.class, Service.Mode.MESSAGE);
	    smDispatch.getRequestContext().put("com.sun.xml.ws.connect.timeout", timeOut);
	    smDispatch.getRequestContext().put("com.sun.xml.ws.request.timeout", timeOut);

	    response = smDispatch.invoke(secureMsg);

	} catch (Exception e) {
	    throw new TSAServiceInvokerException(Language.getResIntegra(ILogConstantKeys.TWSI_LOG006), e);
	}

	// Configuramos la securización de la respuesta SOAP
	InputStream serverConfig = configureSecureSOAPResponse();
	try {

	    XWSSProcessor sprocessor = xwssProcessorFactory.createProcessorForSecurityConfiguration(serverConfig, callBackHandler);

	    context = new ProcessingContext();
	    context.setSOAPMessage(response);

	    SOAPMessage verifiedMsg = sprocessor.verifyInboundMessage(context);
	    return traduceResponse(verifiedMsg);
	} catch (Exception e) {
	    throw new TSAServiceInvokerException(Language.getResIntegra(ILogConstantKeys.TWSI_LOG007), e);
	}
    }

    /**
     * Method that obtains the SOAP body from the SOAP response.
     * @param message Parameter that represents the SOAP response from TS@.
     * @return the SOAP body from the SOAP response.
     * @throws TransformerException If an unrecoverable error occurs during the course of the transformation.
     * @throws SOAPException If the SOAP body does not exist or cannot be retrieved.
     */
    private String traduceResponse(SOAPMessage message) throws TransformerException, SOAPException {
	TransformerFactory tf = TransformerFactory.newInstance();
	Transformer transformer = tf.newTransformer();
	transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
	StringWriter writer = new StringWriter();
	transformer.transform(new DOMSource(message.getSOAPBody().getFirstChild()), new StreamResult(writer));
	return writer.getBuffer().toString();
    }

    /**
     * Method that verifies if a value is not empty and not null.
     * @param parameterName Parameter that represents the name of the element to check.
     * @param parameterValue Parameter that represents the value to check.
     * @throws TSAServiceInvokerException If the value is empty or null.
     */
    private void checkSvcInvokerParams(String parameterName, String parameterValue) throws TSAServiceInvokerException {
	if (!GenericUtils.assertStringValue(parameterValue)) {
	    throw new TSAServiceInvokerException(Language.getFormatResIntegra(ILogConstantKeys.TWSI_LOG008, new Object[ ] { parameterName, properties.getProperty(TSAServiceInvokerConstants.APPLICATION_NAME) }));
	}
    }

    /**
     * Method that obtains the properties related to the trusted keystore from {@link #properties} and configure it.
     */
    private void configureSSLTrustStore() {
	// Rescatamos la ruta al almacén de confianza
	String trustsorePath = properties.getProperty(TSAServiceInvokerConstants.TRUSTEDSTORE_PATH);
	if (trustsorePath == null && System.getProperty("javax.net.ssl.trustStore") == null) {
	    LOGGER.warn(Language.getResIntegra(ILogConstantKeys.TWSI_LOG009));
	} else if (trustsorePath != null) {
	    // Actualizamos la ruta al almacén de confianza
	    System.setProperty("javax.net.ssl.trustStore", trustsorePath);
	}
	LOGGER.info(Language.getFormatResIntegra(ILogConstantKeys.TWSI_LOG010, new Object[ ] { trustsorePath }));

	// Rescatamos la clave del almacén de confianza
	String truststorePassword = properties.getProperty(TSAServiceInvokerConstants.TRUSTEDSTORE_PASSWORD);
	if (truststorePassword == null && System.getProperty("javax.net.ssl.trustStorePassword") == null) {
	    LOGGER.warn(Language.getResIntegra(ILogConstantKeys.TWSI_LOG011));
	} else if (truststorePassword != null) {
	    System.setProperty("javax.net.ssl.trustStorePassword", truststorePassword);
	}
    }

    /**
     * Method that obtains the properties related to the encryption of SOAP requests from {@link #properties}.
     * @param propertiesHeader Parameter that represents the name of the application defined for accessing to the configuration file.
     * @param callBackHandler Parameter that represents the class used to proccess the SOAP messages.
     */
    private void configureSymmetricKeyAttributes(String propertiesHeader, TSACallBackHandler callBackHandler) {
	// Rescatamos el indicador para cifrar, o no, la petición SOAP con clave
	// simétrica
	String encryptRequest = properties.getProperty(propertiesHeader + TSAServiceInvokerConstants.WS_REQUEST_SYMMETRICKEY_USE);

	if (encryptRequest != null) {
	    if (encryptRequest.equals(Boolean.toString(true))) {
		LOGGER.debug(Language.getResIntegra(ILogConstantKeys.TWSI_LOG012));
		// Rescatamos el alias de la clave simétrica
		symmetricKeyAlias = properties.getProperty(propertiesHeader + TSAServiceInvokerConstants.WS_REQUEST_SYMMETRICKEY_ALIAS);
		if (symmetricKeyAlias == null) {
		    LOGGER.warn(Language.getResIntegra(ILogConstantKeys.TWSI_LOG013));
		} else {
		    LOGGER.info(Language.getFormatResIntegra(ILogConstantKeys.TWSI_LOG014, new Object[ ] { symmetricKeyAlias }));
		    // Rescatamos el valor de la clave simétrica
		    symmetricKeyValue = properties.getProperty(propertiesHeader + TSAServiceInvokerConstants.WS_REQUEST_SYMMETRICKEY_VALUE);
		    if (symmetricKeyValue == null) {
			LOGGER.warn(Language.getResIntegra(ILogConstantKeys.TWSI_LOG015));
		    } else {
			useSymmetricKey = true;
			// Establecemos en el callbackhandler el valor de la
			// clave simétrica
			callBackHandler.setSymmetricKeyRequest(symmetricKeyValue);
		    }
		}
	    } else if (encryptRequest.equals(Boolean.toString(false))) {
		LOGGER.debug(Language.getResIntegra(ILogConstantKeys.TWSI_LOG016));
	    } else {
		LOGGER.warn(Language.getResIntegra(ILogConstantKeys.TWSI_LOG017));
	    }
	} else {
	    LOGGER.warn(Language.getResIntegra(ILogConstantKeys.TWSI_LOG018));
	}
    }

    /**
     * Method tha obtains the InputStream as the representation of the SOAP header used to validate the SOAP responses.
     * @return the InputStream as the representation of the SOAP header used to validate the SOAP responses.
     */
    private InputStream configureSecureSOAPResponse() {
	return SOAPMessageSecurityProvider.generateInputStream(SOAPMessageSecurityProvider.XML_XWSS);
    }

    /**
     * Method that obtains the InputStream as the representation for the SOAP header used to generate the SOAP requests.
     * @param securityOption Parameter that represents the method used to secure the SOAP requests.
     * @param propertiesHeader Parameter that represents the name of the application defined for accessing to the configuration file.
     * @param callBackHandler Parameter that represents the class used to proccess the SOAP messages.
     * @return the InputStream as the representation for the SOAP header used to generate the SOAP requests.
     * @throws TSAServiceInvokerException If the method fails.
     */
    private InputStream configureSecureSOAPRequest(String securityOption, String propertiesHeader, TSACallBackHandler callBackHandler) throws TSAServiceInvokerException {
	InputStream is = null;
	// Autenticación por usuario y contraseña
	if (securityOption.equals(SOAPMessageSecurityProvider.AUTHENTICATION_USER_PASSWORD)) {
	    LOGGER.debug(Language.getResIntegra(ILogConstantKeys.TWSI_LOG019));
	    // Rescatamos el nombre de usuario
	    String userName = properties.getProperty(propertiesHeader + TSAServiceInvokerConstants.WS_USERNAMETOKEN_USER_NAME_PROPERTY);
	    checkSvcInvokerParams(TSAServiceInvokerConstants.WS_USERNAMETOKEN_USER_NAME_PROPERTY, userName);
	    LOGGER.info(Language.getFormatResIntegra(ILogConstantKeys.TWSI_LOG020, new Object[ ] { userName }));

	    // Rescatamos la contraseña del usuario
	    String userPassword = properties.getProperty(propertiesHeader + TSAServiceInvokerConstants.WS_USERNAMETOKEN_USER_NAME_PROPERTY);
	    checkSvcInvokerParams(TSAServiceInvokerConstants.WS_USERNAMETOKEN_USER_NAME_PROPERTY, userPassword);

	    // Obtenemos el InputStream que se refiere a la securización
	    // seleccionada
	    is = SOAPMessageSecurityProvider.generateXMLUserNameToken(userName, userPassword, useSymmetricKey, symmetricKeyAlias);

	}
	// Autenticación por certificado
	else if (securityOption.equals(SOAPMessageSecurityProvider.AUTHENTICATION_CERTIFICATE)) {
	    LOGGER.debug(Language.getResIntegra(ILogConstantKeys.TWSI_LOG021));
	    // Rescatamos el método de inclusión del certificado en la petición
	    // SOAP
	    String certificateInclussionMethod = properties.getProperty(propertiesHeader + TSAServiceInvokerConstants.WS_X509CERTIFICATETOKEN_INCLUSION_METHOD);
	    checkSvcInvokerParams(TSAServiceInvokerConstants.WS_X509CERTIFICATETOKEN_INCLUSION_METHOD, certificateInclussionMethod);
	    LOGGER.info(Language.getFormatResIntegra(ILogConstantKeys.TWSI_LOG022, new Object[ ] { certificateInclussionMethod }));

	    // Comprobamos que el tipo de método de inclusión es reconocido
	    checkCertificateInclussionMethod(certificateInclussionMethod);

	    // Rescatamos la ruta al almacén de claves donde se encuentra
	    // almacenada la clave privada a usar para firmar
	    // la petición SOAP
	    String keystorePath = properties.getProperty(propertiesHeader + TSAServiceInvokerConstants.WS_X509CERTIFICATETOKEN_KEYSTORE_PATH);
	    checkSvcInvokerParams(TSAServiceInvokerConstants.WS_X509CERTIFICATETOKEN_KEYSTORE_PATH, keystorePath);
	    LOGGER.info(Language.getFormatResIntegra(ILogConstantKeys.TWSI_LOG023, new Object[ ] { keystorePath }));

	    // Rescatamos el tipo de almacén de claves donde se encuentra
	    // almacenada la clave privada a usar para firmar la petición SOAP
	    String keystoreType = properties.getProperty(propertiesHeader + TSAServiceInvokerConstants.WS_X509CERTIFICATETOKEN_KEYSTORE_TYPE);
	    checkSvcInvokerParams(TSAServiceInvokerConstants.WS_X509CERTIFICATETOKEN_KEYSTORE_TYPE, keystoreType);
	    // Comprobamos que el tipo de almacén de claves está soportado
	    checkKeystoreType(keystoreType, Language.getResIntegra(ILogConstantKeys.TWSI_LOG026));
	    LOGGER.info(Language.getFormatResIntegra(ILogConstantKeys.TWSI_LOG024, new Object[ ] { keystoreType }));

	    // Rescatamos la contraseña del almacén de claves donde se encuentra
	    // almacenada la clave privada a usar para firmar la petición SOAP
	    String keystorePassword = properties.getProperty(propertiesHeader + TSAServiceInvokerConstants.WS_X509CERTIFICATETOKEN_KEYSTORE_PASSWORD);
	    checkSvcInvokerParams(TSAServiceInvokerConstants.WS_X509CERTIFICATETOKEN_KEYSTORE_PASSWORD, keystorePassword);

	    // Rescatamos el alias de la clave privada a usar para firmar la
	    // petición SOAP
	    String privateKeyAlias = properties.getProperty(propertiesHeader + TSAServiceInvokerConstants.WS_X509CERTIFICATETOKEN_PRIVATE_KEY_ALIAS);
	    checkSvcInvokerParams(TSAServiceInvokerConstants.WS_X509CERTIFICATETOKEN_PRIVATE_KEY_ALIAS, privateKeyAlias);
	    LOGGER.info(Language.getFormatResIntegra(ILogConstantKeys.TWSI_LOG025, new Object[ ] { privateKeyAlias }));

	    // Rescatamos la contraseña de la clave privada a usar para firmar
	    // la petición SOAP
	    String privateKeyPassword = properties.getProperty(propertiesHeader + TSAServiceInvokerConstants.WS_X509CERTIFICATETOKEN_PRIVATE_KEY_PASSWORD);
	    checkSvcInvokerParams(TSAServiceInvokerConstants.WS_X509CERTIFICATETOKEN_PRIVATE_KEY_PASSWORD, privateKeyPassword);

	    // Accedemos al almacén de claves para rescatar la clave privada y
	    // el certificado usados para firmar la petición SOAP
	    byte[ ] keystoreBytes = getBytesFromFile(keystorePath);
	    try {
		PrivateKey privateKeySOAP = UtilsKeystore.getPrivateKeyEntry(keystoreBytes, keystorePassword, privateKeyAlias, keystoreType, privateKeyPassword);
		X509Certificate certificateSOAP = UtilsCertificate.generateCertificate(UtilsKeystore.getCertificateEntry(keystoreBytes, keystorePassword, privateKeyAlias, keystoreType));
		// Asociamos los valores al CallBackHanlder
		callBackHandler.setPrivateKeySOAPRequest(privateKeySOAP);
		callBackHandler.setCertificateSOAPRequest(certificateSOAP);
	    } catch (Exception e) {
		throw new TSAServiceInvokerException(Language.getFormatResIntegra(ILogConstantKeys.TWSI_LOG027, new Object[ ] { privateKeyAlias, keystorePath }), e);
	    }

	    // Obtenemos el InputStream que se refiere a la securización
	    // seleccionada
	    is = SOAPMessageSecurityProvider.generateXMLX509CertificateToken(certificateInclussionMethod, useSymmetricKey, symmetricKeyAlias);
	}
	// Autenticación por SAML
	else if (securityOption.equals(SOAPMessageSecurityProvider.AUTHENTICATION_SAML)) {
	    LOGGER.debug(Language.getResIntegra(ILogConstantKeys.TWSI_LOG028));
	    // Rescatamos el método de confirmación del sujeto
	    String mandatorySubjectConfirmationMethod = properties.getProperty(propertiesHeader + TSAServiceInvokerConstants.WS_SAMLTOKEN_METHOD);
	    checkSvcInvokerParams(TSAServiceInvokerConstants.WS_SAMLTOKEN_METHOD, mandatorySubjectConfirmationMethod);
	    // Comprobamos que el método de confirmación del sujeto es
	    // reconocido
	    checkMandatorySubjectConfirmationMethod(mandatorySubjectConfirmationMethod);
	    LOGGER.info(Language.getFormatResIntegra(ILogConstantKeys.TWSI_LOG029, new Object[ ] { mandatorySubjectConfirmationMethod }));

	    // Rescatamos la ruta al almacén de claves donde se encuentra
	    // almacenada la clave privada a usar para firmar
	    // la petición SOAP
	    String keystorePath = properties.getProperty(propertiesHeader + TSAServiceInvokerConstants.WS_SAMLTOKEN_KEYSTORE_PATH);
	    checkSvcInvokerParams(TSAServiceInvokerConstants.WS_SAMLTOKEN_KEYSTORE_PATH, keystorePath);
	    LOGGER.info(Language.getFormatResIntegra(ILogConstantKeys.TWSI_LOG023, new Object[ ] { keystorePath }));

	    // Rescatamos el tipo de almacén de claves donde se encuentra
	    // almacenada la clave privada a usar para firmar la petición SOAP
	    String keystoreType = properties.getProperty(propertiesHeader + TSAServiceInvokerConstants.WS_SAMLTOKEN_KEYSTORE_TYPE);
	    checkSvcInvokerParams(TSAServiceInvokerConstants.WS_SAMLTOKEN_KEYSTORE_TYPE, keystoreType);
	    // Comprobamos que el tipo de almacén de claves está soportado
	    checkKeystoreType(keystoreType, Language.getResIntegra(ILogConstantKeys.TWSI_LOG026));
	    LOGGER.info(Language.getFormatResIntegra(ILogConstantKeys.TWSI_LOG024, new Object[ ] { keystoreType }));

	    // Rescatamos la contraseña del almacén de claves donde se encuentra
	    // almacenada la clave privada a usar para firmar la petición SOAP
	    String keystorePassword = properties.getProperty(propertiesHeader + TSAServiceInvokerConstants.WS_SAMLTOKEN_KEYSTORE_PASSWORD);
	    checkSvcInvokerParams(TSAServiceInvokerConstants.WS_SAMLTOKEN_KEYSTORE_PASSWORD, keystorePassword);

	    // Rescatamos el alias de la clave privada a usar para firmar la
	    // petición SOAP
	    String privateKeyAlias = properties.getProperty(propertiesHeader + TSAServiceInvokerConstants.WS_SAMLTOKEN_PRIVATE_KEY_ALIAS);
	    checkSvcInvokerParams(TSAServiceInvokerConstants.WS_SAMLTOKEN_PRIVATE_KEY_ALIAS, privateKeyAlias);
	    LOGGER.info(Language.getFormatResIntegra(ILogConstantKeys.TWSI_LOG025, new Object[ ] { privateKeyAlias }));

	    // Rescatamos la contraseña de la clave privada a usar para firmar
	    // la petición SOAP
	    String privateKeyPassword = properties.getProperty(propertiesHeader + TSAServiceInvokerConstants.WS_SAMLTOKEN_PRIVATE_KEY_PASSWORD);
	    checkSvcInvokerParams(TSAServiceInvokerConstants.WS_SAMLTOKEN_PRIVATE_KEY_PASSWORD, privateKeyPassword);

	    // Accedemos al almacén de claves para rescatar la clave privada y
	    // el certificado usados para firmar la petición SOAP
	    byte[ ] keystoreBytes = getBytesFromFile(keystorePath);
	    try {
		PrivateKey privateKeySOAP = UtilsKeystore.getPrivateKeyEntry(keystoreBytes, keystorePassword, privateKeyAlias, keystoreType, privateKeyPassword);
		X509Certificate certificateSOAP = UtilsCertificate.generateCertificate(UtilsKeystore.getCertificateEntry(keystoreBytes, keystorePassword, privateKeyAlias, keystoreType));
		// Asociamos los valores al CallBackHanlder
		callBackHandler.setPrivateKeySOAPRequest(privateKeySOAP);
		callBackHandler.setCertificateSOAPRequest(certificateSOAP);
	    } catch (Exception e) {
		throw new TSAServiceInvokerException(Language.getFormatResIntegra(ILogConstantKeys.TWSI_LOG027, new Object[ ] { privateKeyAlias, keystorePath }), e);
	    }

	    // Obtenemos el InputStream que se refiere a la securización
	    // seleccionada
	    is = SOAPMessageSecurityProvider.generateXMLSAMLToken(mandatorySubjectConfirmationMethod, useSymmetricKey, symmetricKeyAlias);
	}
	// Autenticación no reconocida
	else {
	    throw new TSAServiceInvokerException(Language.getResIntegra(ILogConstantKeys.TWSI_LOG030));
	}
	return is;
    }

    /**
     * Method that verifies if the inclussion method for the certificate used to secure the SOAP request has a correct value. The allowed
     * values are:
     * <ul>
     * <li>{@link SOAPMessageSecurityProvider#INCLUSION_METHOD_DIRECT}</li>
     * <li>{@link SOAPMessageSecurityProvider#INCLUSSION_METHOD_IDENTIFIER}</li>
     * <li>{@link SOAPMessageSecurityProvider#INCLUSSION_METHOD_ISSUERSERIALNUMBER}</li>
     * </ul>
     * @param certificateInclussionMethod Parameter that represents the inclussion method for the certificate used to secure the SOAP request.
     * @throws TSAServiceInvokerException If the inclussion method for the certificate used to secure the SOAP request has an incorrect value.
     */
    private void checkCertificateInclussionMethod(String certificateInclussionMethod) throws TSAServiceInvokerException {
	if (!certificateInclussionMethod.equals(SOAPMessageSecurityProvider.INCLUSION_METHOD_DIRECT) && !certificateInclussionMethod.equals(SOAPMessageSecurityProvider.INCLUSION_METHOD_IDENTIFIER) && !certificateInclussionMethod.equals(SOAPMessageSecurityProvider.INCLUSION_METHOD_ISSUERSERIALNUMBER)) {
	    throw new TSAServiceInvokerException(Language.getResIntegra(ILogConstantKeys.TWSI_LOG031));
	}
    }

    /**
     * Method that verifies if the mandatory subject confirmation method used to secure the SOAP request with SAML has a correct value. The allowed
     * values are:
     * <ul>
     * <li>{@link SOAPMessageSecurityProvider#SAML_HOLDER_OF_KEY}</li>
     * <li>{@link SOAPMessageSecurityProvider#SAML_SENDER_VOUCHEZ}</li>
     * </ul>
     * @param mandatorySubjectConfirmationMethod Parameter that represents the mandatory subject confirmation method used to secure the SOAP request with SAML.
     * @throws TSAServiceInvokerException If the mandatory subject confirmation method used to secure the SOAP request with SAML has an incorrect value.
     */
    private void checkMandatorySubjectConfirmationMethod(String mandatorySubjectConfirmationMethod) throws TSAServiceInvokerException {
	if (!mandatorySubjectConfirmationMethod.equals(SOAPMessageSecurityProvider.SAML_HOLDER_OF_KEY) && !mandatorySubjectConfirmationMethod.equals(SOAPMessageSecurityProvider.SAML_SENDER_VOUCHEZ)) {
	    throw new TSAServiceInvokerException(Language.getResIntegra(ILogConstantKeys.TWSI_LOG032));
	}
    }

    /**
     * Method that verifies if the type of a keystore has a correct value. The allowed
     * values are:
     * <ul>
     * <li>{@link UtilsKeystore#PKCS12}</li>
     * <li>{@link UtilsKeystore#JCEKS}</li>
     * <li>{@link UtilsKeystore#JKS}</li>
     * </ul>
     * @param keystoreType Parameter that represents the type of the keystore.
     * @param msg Parameter that represents the error message if the type of the keystore is incorrect.
     * @throws TSAServiceInvokerException If the type of the keystore is incorrect.
     */
    private void checkKeystoreType(String keystoreType, String msg) throws TSAServiceInvokerException {
	if (!keystoreType.equals(UtilsKeystore.PKCS12) && !keystoreType.equals(UtilsKeystore.JCEKS) && !keystoreType.equals(UtilsKeystore.JKS)) {
	    throw new TSAServiceInvokerException(msg);
	}
    }

    /**
     * Method that checks if a value isn't null and empty.
     * @param value Parameter that represents the value to check.
     * @param msg Parameter that represents the message used for the log if the value is null or empty.
     * @return a boolean that indicates if the value is null or empty (true), or not (false).
     */
    private boolean checkValue(String value, String msg) {
	if (value == null || value.isEmpty()) {
	    LOGGER.info(msg);
	    return false;
	}
	return true;
    }

    /**
     * Method that obtains the value of the symmetric key used to encrypt the SOAP responses from {@link #properties}.
     * @param propertiesHeader Parameter that represents the name of the application defined for accessing to the configuration file.
     * @param callBackHandler Parameter that represents the class used to proccess the SOAP messages.
     */
    private void configureSOAPResponseSymmetricKey(String propertiesHeader, TSACallBackHandler callBackHandler) {
	// Rescatamos el alias de la clave simétrica usada para encriptar las
	// respuestas SOAP por parte de TS@
	String responseSymmetricKeyAlias = properties.getProperty(propertiesHeader + TSAServiceInvokerConstants.WS_RESPONSE_SYMMETRICKEY_ALIAS);
	if (checkValue(responseSymmetricKeyAlias, Language.getResIntegra(ILogConstantKeys.TWSI_LOG033))) {
	    // Asociamos el valor al CallBackHanlder
	    callBackHandler.setAliasSymmetricKeyResponse(responseSymmetricKeyAlias);
	    LOGGER.info(Language.getFormatResIntegra(ILogConstantKeys.TWSI_LOG034, new Object[ ] { responseSymmetricKeyAlias }));
	}
	// Rescatamos el valor de la clave simétrica usada para encriptar las
	// respuestas SOAP por parte de TS@
	String responseSymmetricKeyValue = properties.getProperty(propertiesHeader + TSAServiceInvokerConstants.WS_RESPONSE_SYMMETRICKEY_VALUE);
	if (checkValue(responseSymmetricKeyValue, Language.getResIntegra(ILogConstantKeys.TWSI_LOG035))) {
	    // Asociamos el valor al CallBackHanlder
	    callBackHandler.setSymmetricKeyResponse(responseSymmetricKeyValue);
	}
    }

    /**
     * Method that obtains the properties related to the certificate used to sign the SOAP responses from {@link #properties}.
     * @param propertiesHeader Parameter that represents the name of the application defined for accessing to the configuration file.
     * @param callBackHandler Parameter that represents the class used to proccess the SOAP messages.
     * @throws TSAServiceInvokerException If any of the properties has an incorrect value.
     */
    private void configureSOAPResponseCertificate(String propertiesHeader, TSACallBackHandler callBackHandler) throws TSAServiceInvokerException {
	// Rescatamos la ruta al almacén de claves donde se encuentra almacenado
	// el certificado usado por TS@ para firmar las respuestas SOAP
	String keystorePath = properties.getProperty(propertiesHeader + TSAServiceInvokerConstants.WS_RESPONSE_KEYSTORE_PATH);
	if (checkValue(keystorePath, Language.getResIntegra(ILogConstantKeys.TWSI_LOG036))) {
	    LOGGER.info(Language.getFormatResIntegra(ILogConstantKeys.TWSI_LOG037, new Object[ ] { keystorePath }));
	    // Rescatamos el tipo de almacén de claves donde se encuentra
	    // almacenado el certificado usado por TS@ para firmar las
	    // respuestas SOAP
	    String keystoreType = properties.getProperty(propertiesHeader + TSAServiceInvokerConstants.WS_RESPONSE_KEYSTORE_TYPE);
	    if (checkValue(keystoreType, Language.getResIntegra(ILogConstantKeys.TWSI_LOG038))) {
		LOGGER.info(Language.getFormatResIntegra(ILogConstantKeys.TWSI_LOG039, new Object[ ] { keystoreType }));
		// Comprobamos que el tipo de almacén de claves está soportado
		checkKeystoreType(keystoreType, Language.getResIntegra(ILogConstantKeys.TWSI_LOG040));
		// Rescatamos la contraseña del almacén de claves donde se
		// encuentra almacenado el certificado usado por TS@ para firmar
		// las respuestas SOAP
		String keystorePassword = properties.getProperty(propertiesHeader + TSAServiceInvokerConstants.WS_RESPONSE_KEYSTORE_PASSWORD);
		if (checkValue(keystorePassword, Language.getResIntegra(ILogConstantKeys.TWSI_LOG041))) {
		    // Rescatamos el alias del certificado usado por TS@ para
		    // firmar las respuestas SOAP
		    String certificateAlias = properties.getProperty(propertiesHeader + TSAServiceInvokerConstants.WS_RESPONSE_CERTIFICATE_ALIAS);
		    if (checkValue(certificateAlias, Language.getResIntegra(ILogConstantKeys.TWSI_LOG042))) {
			LOGGER.info(Language.getFormatResIntegra(ILogConstantKeys.TWSI_LOG043, new Object[ ] { certificateAlias }));
			// Accedemos al almacén de claves para rescatar el
			// certificado usado por TS@ para firmar las respuestas
			// SOAP
			byte[ ] keystoreBytes = getBytesFromFile(keystorePath);
			try {
			    X509Certificate certificateSOAP = UtilsCertificate.generateCertificate(UtilsKeystore.getCertificateEntry(keystoreBytes, keystorePassword, certificateAlias, keystoreType));
			    // Asociamos el valor al CallBackHanlder
			    callBackHandler.setCertificateSOAPResponse(certificateSOAP);
			} catch (Exception e) {
			    throw new TSAServiceInvokerException(Language.getFormatResIntegra(ILogConstantKeys.TWSI_LOG044, new Object[ ] { certificateAlias, keystorePath }), e);
			}
		    }
		}
	    }
	}
    }

    /**
     * Method that obtains the properties related to the certificate used to sign the SOAP responses with SAML from {@link #properties}.
     * @param propertiesHeader Parameter that represents the name of the application defined for accessing to the configuration file.
     * @param callBackHandler Parameter that represents the class used to proccess the SOAP messages.
     * @throws TSAServiceInvokerException If any of the properties has an incorrect value.
     */
    private void configureSOAPResponseSAMLCertificate(String propertiesHeader, TSACallBackHandler callBackHandler) throws TSAServiceInvokerException {
	// Rescatamos la ruta al almacén de claves donde se encuentra almacenado
	// el certificado usado por TS@ para firmar las respuestas SOAP con SAML
	String keystorePath = properties.getProperty(propertiesHeader + TSAServiceInvokerConstants.WS_RESPONSE_SAML_KEYSTORE_PATH);
	if (checkValue(keystorePath, Language.getResIntegra(ILogConstantKeys.TWSI_LOG045))) {
	    LOGGER.info(Language.getFormatResIntegra(ILogConstantKeys.TWSI_LOG046, new Object[ ] { keystorePath }));
	    // Rescatamos el tipo de almacén de claves donde se encuentra
	    // almacenado el certificado usado por TS@ para firmar las
	    // respuestas SOAP con SAML
	    String keystoreType = properties.getProperty(propertiesHeader + TSAServiceInvokerConstants.WS_RESPONSE_SAML_KEYSTORE_TYPE);
	    if (checkValue(keystoreType, Language.getResIntegra(ILogConstantKeys.TWSI_LOG047))) {
		LOGGER.info(Language.getFormatResIntegra(ILogConstantKeys.TWSI_LOG048, new Object[ ] { keystoreType }));
		// Comprobamos que el tipo de almacén de claves está soportado
		checkKeystoreType(keystoreType, Language.getResIntegra(ILogConstantKeys.TWSI_LOG049));
		// Rescatamos la contraseña del almacén de claves donde se
		// encuentra almacenado el certificado usado por TS@ para firmar
		// las respuestas SOAP con SAML
		String keystorePassword = properties.getProperty(propertiesHeader + TSAServiceInvokerConstants.WS_RESPONSE_SAML_KEYSTORE_PASSWORD);
		if (checkValue(keystorePassword, Language.getResIntegra(ILogConstantKeys.TWSI_LOG050))) {
		    // Rescatamos el alias del certificado usado por TS@ para
		    // firmar las respuestas SOAP
		    String certificateAlias = properties.getProperty(propertiesHeader + TSAServiceInvokerConstants.WS_RESPONSE_SAML_CERTIFICATE_ALIAS);
		    if (checkValue(certificateAlias, Language.getResIntegra(ILogConstantKeys.TWSI_LOG051))) {
			LOGGER.info(Language.getFormatResIntegra(ILogConstantKeys.TWSI_LOG052, new Object[ ] { certificateAlias }));
			// Accedemos al almacén de claves para rescatar el
			// certificado usado por TS@ para firmar las respuestas
			// SOAP con SAML
			byte[ ] keystoreBytes = getBytesFromFile(keystorePath);
			try {
			    X509Certificate certificateSOAP = UtilsCertificate.generateCertificate(UtilsKeystore.getCertificateEntry(keystoreBytes, keystorePassword, certificateAlias, keystoreType));
			    // Asociamos el valor al CallBackHanlder
			    callBackHandler.setCertificateSAMLResponse(certificateSOAP);
			} catch (Exception e) {
			    throw new TSAServiceInvokerException(Language.getFormatResIntegra(ILogConstantKeys.TWSI_LOG044, new Object[ ] { certificateAlias, keystorePath }), e);
			}
		    }
		}
	    }
	}
    }

    /**
     * Method that obtains a file as a bytes array.
     * @param filePath Parameter that represents the path of the file.
     * @return the bytes array of the file.
     * @throws TSAServiceInvokerException If the file does not exist, is a directory rather than a regular file, or for some other reason cannot be
     * opened for reading.
     */
    private byte[ ] getBytesFromFile(String filePath) throws TSAServiceInvokerException {
	InputStream fis = null;
	try {
	    fis = new FileInputStream(filePath);
	    return GenericUtils.getDataFromInputStream(fis);
	} catch (IOException e) {
	    throw new TSAServiceInvokerException("Se ha producido un error al tratar de acceder  al fichero: " + filePath, e);
	} finally {
	    UtilsResources.safeCloseInputStream(fis);
	}
    }
}

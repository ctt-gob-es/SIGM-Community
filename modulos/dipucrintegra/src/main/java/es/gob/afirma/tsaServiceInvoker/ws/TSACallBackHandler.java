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
 * <b>File:</b><p>es.gob.afirma.tsaServiceInvoker.ws.TSACallBackHandler.java.</p>
 * <b>Description:</b><p>Class that represents a CallbackHandler for managing the SOAP messages.</p>
 * <b>Project:</b><p>@Firma and TS@ Web Services Integration Platform.</p>
 * <b>Date:</b><p>13/01/2014.</p>
 * @author Gobierno de España.
 * @version 1.0, 13/01/2014.
 */
package es.gob.afirma.tsaServiceInvoker.ws;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.sun.org.apache.xml.internal.security.keys.KeyInfo;
import com.sun.xml.wss.XWSSecurityException;
import com.sun.xml.wss.impl.callback.CertificateValidationCallback;
import com.sun.xml.wss.impl.callback.CertificateValidationCallback.CertificateValidationException;
import com.sun.xml.wss.impl.callback.DecryptionKeyCallback;
import com.sun.xml.wss.impl.callback.DynamicPolicyCallback;
import com.sun.xml.wss.impl.callback.EncryptionKeyCallback;
import com.sun.xml.wss.impl.callback.SignatureKeyCallback;
import com.sun.xml.wss.impl.callback.SignatureVerificationKeyCallback;
import com.sun.xml.wss.impl.callback.TimestampValidationCallback;
import com.sun.xml.wss.impl.callback.TimestampValidationCallback.Request;
import com.sun.xml.wss.impl.callback.TimestampValidationCallback.TimestampValidationException;
import com.sun.xml.wss.impl.callback.TimestampValidationCallback.TimestampValidator;
import com.sun.xml.wss.impl.policy.SecurityPolicy;
import com.sun.xml.wss.impl.policy.mls.AuthenticationTokenPolicy;
import com.sun.xml.wss.impl.policy.mls.PrivateKeyBinding;
import com.sun.xml.wss.saml.Assertion;
import com.sun.xml.wss.saml.Conditions;
import com.sun.xml.wss.saml.NameIdentifier;
import com.sun.xml.wss.saml.SAMLAssertionFactory;
import com.sun.xml.wss.saml.SAMLException;
import com.sun.xml.wss.saml.Subject;
import com.sun.xml.wss.saml.SubjectConfirmation;

import es.gob.afirma.i18n.ILogConstantKeys;
import es.gob.afirma.i18n.Language;
import es.gob.afirma.utils.NumberConstants;
import es.gob.afirma.utils.UtilsCertificate;
import es.gob.afirma.utils.UtilsResources;

/**
 * <p>Class that represents a CallbackHandler for managing the SOAP messages.</p>
 * <b>Project:</b><p>@Firma and TS@ Web Services Integration Platform.</p>
 * @version 1.0, 13/01/2014.
 */
public class TSACallBackHandler implements CallbackHandler {

    /**
     * Attribute that represents the object that manages the log of the class.
     */
    private static final Logger LOGGER = Logger.getLogger(TSACallBackHandler.class);

    /**
     * Attribute that represents the certificate used for signing the SOAP requests.
     */
    private X509Certificate certificateSOAPRequest;

    /**
     * Attribute that represents the private key used for signing the SOAP requests.
     */
    private PrivateKey privateKeySOAPRequest;

    /**
     * Attribute that represents the certificate used for signing the responses from TS@ using SAML.
     */
    private X509Certificate certificateSAMLResponse;

    /**
     * Attribute that represents the certificate used for signing the SOAP responses from TS@.
     */
    private X509Certificate certificateSOAPResponse;

    /**
     * Attribute that indicates if the CallBack is processing a SOAP response secured with SAML.
     */
    private boolean processingSAMLResponse = false;

    /**
     * Attribute that represents the value of the symmetric key used to encode the SOAP request.
     */
    private String symmetricKeyRequest;

    /**
     * Attribute that represents the value of the symmetric key used to encode the SOAP response.
     */
    private String symmetricKeyResponse;

    /**
     * Attribute that represents the alias of the symmetric key used to encode the SOAP response.
     */
    private String aliasSymmetricKeyResponse;

    /**
     * {@inheritDoc}
     * @see javax.security.auth.callback.CallbackHandler#handle(javax.security.auth.callback.Callback[])
     */
    @Override
    public final void handle(Callback[ ] callbacks) throws IOException, UnsupportedCallbackException {
	// Recorremos la lista de Callbacks para procesarlos
	for (int i = 0; i < callbacks.length; i++) {
	    // Accedemos al Callback y comprobamos de qué tipo es
	    Callback callback = callbacks[i];

	    // Callback para firmar un mensaje
	    if (callback instanceof SignatureKeyCallback) {
		// Procesamos el Callback
		SignatureKeyCallback cb = (SignatureKeyCallback) callback;
		processSignatureKeyCallback(cb, callback);
	    }
	    // Callback para validar un certificado
	    else if (callback instanceof CertificateValidationCallback) {
		CertificateValidationCallback request = (CertificateValidationCallback) callback;
		// Definimos un validador para validar el certificado
		request.setValidator(new X509CertificateValidatorImpl());
	    }
	    // Callback para validar una firma
	    else if (callback instanceof SignatureVerificationKeyCallback) {
		SignatureVerificationKeyCallback cb = (SignatureVerificationKeyCallback) callback;
		// Procesamos el Callback
		processSignatureVerificationKeyCallback(cb, callback);
	    }
	    // Callback para SAML
	    else if (callback instanceof DynamicPolicyCallback) {
		DynamicPolicyCallback cb = (DynamicPolicyCallback) callback;
		processDynamicPolicyCallback(cb, callback);
	    }
	    // Callback para encriptar un mensaje
	    else if (callback instanceof EncryptionKeyCallback) {
		EncryptionKeyCallback cb = (EncryptionKeyCallback) callback;
		processEncryptionKeyCallback(cb, callback);
	    }
	    // Callback para desencriptar un mensaje
	    else if (callback instanceof DecryptionKeyCallback) {
		DecryptionKeyCallback cb = (DecryptionKeyCallback) callback;
		processDecryptionKeyCallback(cb, callback);
	    }
	    // Callback para validar una fecha
	    else if (callback instanceof TimestampValidationCallback) {
		TimestampValidationCallback tsv = (TimestampValidationCallback) callback;
		// Definimos un validador para validar la fecha de creación de
		// la petición SOAP
		tsv.setValidator(new TimestampValidatorImpl());
	    }
	}
    }

    /**
     * Method that process a Callback of type {@link SignatureKeyCallback}.
     * @param cb Parameter that represents the Callback to process.
     * @param callback Parameter that represents the Callback parent.
     * @throws IOException If the method fails.
     * @throws UnsupportedCallbackException If the implementation of this method does not support one or more of the Callbacks
     * specified in the callbacks parameter.
     */
    private void processSignatureKeyCallback(SignatureKeyCallback cb, Callback callback) throws IOException, UnsupportedCallbackException {
	LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.TCBH_LOG001, new Object[ ] { cb.getClass().getSimpleName() }));
	// Comprobamos que tengamos los datos del certificado usado para firmar
	// la petición SOAP
	if (certificateSOAPRequest != null && privateKeySOAPRequest != null) {
	    SignatureKeyCallback.Request request = cb.getRequest();
	    // Si el request es de tipo
	    // SignatureKeyCallback.DefaultPrivKeyCertRequest
	    if (request instanceof SignatureKeyCallback.DefaultPrivKeyCertRequest) {
		((SignatureKeyCallback.DefaultPrivKeyCertRequest) request).setX509Certificate(certificateSOAPRequest);
		((SignatureKeyCallback.DefaultPrivKeyCertRequest) request).setPrivateKey(privateKeySOAPRequest);
	    }
	    // Si el request es de tipo
	    // SignatureKeyCallback.AliasPrivKeyCertRequest
	    else if (request instanceof SignatureKeyCallback.AliasPrivKeyCertRequest) {
		((SignatureKeyCallback.AliasPrivKeyCertRequest) request).setX509Certificate(certificateSOAPRequest);
		((SignatureKeyCallback.AliasPrivKeyCertRequest) request).setPrivateKey(privateKeySOAPRequest);
	    }
	    // Si el request es de un tipo no reconocido
	    else {
		String callBackError = Language.getResIntegra(ILogConstantKeys.TCBH_LOG002);
		LOGGER.error(callBackError);
		throw new UnsupportedCallbackException(callback, callBackError);
	    }
	}
    }

    /**
     * Method that process a Callback of type {@link SignatureVerificationKeyCallback}.
     * @param cb Parameter that represents the Callback to process.
     * @param callback Parameter that represents the Callback parent.
     * @throws IOException If the method fails.
     * @throws UnsupportedCallbackException If the implementation of this method does not support one or more of the Callbacks
     * specified in the callbacks parameter.
     */
    private void processSignatureVerificationKeyCallback(SignatureVerificationKeyCallback cb, Callback callback) throws UnsupportedCallbackException, IOException {
	LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.TCBH_LOG001, new Object[ ] { cb.getClass().getSimpleName() }));
	// Si el request del Callback es de tipo
	// SignatureVerificationKeyCallback.X509SubjectKeyIdentifierBasedRequest.
	// Mecanismo de inclusión del certificado: Identifier
	if (cb.getRequest() instanceof SignatureVerificationKeyCallback.X509SubjectKeyIdentifierBasedRequest) {
	    processSignatureVerificationKeyCallbackX509SubjectKeyIdentifierBasedRequest(cb);
	}

	// Si el request del Callback es de tipo
	// SignatureVerificationKeyCallback.X509IssuerSerialBasedRequest.
	// Mecanismo de inclusión del certificado: IssuerSerialNumber
	else if (cb.getRequest() instanceof SignatureVerificationKeyCallback.X509IssuerSerialBasedRequest) {
	    processSignatureVerificationKeyCallbackX509IssuerSerialBasedRequest(cb);
	}
	// Si el request es de un tipo no reconocido
	else {
	    String callBackError = Language.getResIntegra(ILogConstantKeys.TCBH_LOG002);
	    LOGGER.error(callBackError);
	    throw new UnsupportedCallbackException(callback, callBackError);
	}
    }

    /**
     * Method that process a Callback of type {@link SignatureVerificationKeyCallback.X509SubjectKeyIdentifierBasedRequest}.
     * @param cb Parameter that represents the Callback to process.
     * @throws IOException If the method fails.
     */
    private void processSignatureVerificationKeyCallbackX509SubjectKeyIdentifierBasedRequest(SignatureVerificationKeyCallback cb) throws IOException {
	// Comprobamos que haya definido un certificado para validar las
	// respuestas SOAP
	if (certificateSOAPResponse != null) {
	    SignatureVerificationKeyCallback.X509SubjectKeyIdentifierBasedRequest request = (SignatureVerificationKeyCallback.X509SubjectKeyIdentifierBasedRequest) cb.getRequest();
	    // Obtenemos el SubjectKeyIdentifier del certificado de la petición
	    SubjectKeyIdentifier skiRequest = new SubjectKeyIdentifier(request.getSubjectKeyIdentifier());

	    InputStream is = null;
	    ASN1InputStream asn1is = null;
	    try {
		// Obtenemos el SubjectKeyIdentifier del certificado definido
		// para
		// firmar las respuestas SOAP
		is = new ByteArrayInputStream(certificateSOAPResponse.getPublicKey().getEncoded());
		asn1is = new ASN1InputStream(is);
		ASN1Sequence asn1Sequence = (ASN1Sequence) asn1is.readObject();
		SubjectPublicKeyInfo spki = new SubjectPublicKeyInfo(asn1Sequence);
		SubjectKeyIdentifier skiApp = new SubjectKeyIdentifier(spki);
		// Comprobamos si el SubjectKeyIdentifier es el mismo
		if (skiRequest.equals(skiApp)) {
		    // Asignamos el certificado al request
		    request.setX509Certificate(certificateSOAPResponse);
		}

		// Si no es el mismo
		else {
		    String callBackError = Language.getResIntegra(ILogConstantKeys.TCBH_LOG003);
		    LOGGER.error(callBackError);
		    throw new IOException(callBackError);
		}

	    } finally {
		UtilsResources.safeCloseInputStream(asn1is);
		UtilsResources.safeCloseInputStream(is);
	    }
	} else {
	    String callBackError = Language.getResIntegra(ILogConstantKeys.TCBH_LOG004);
	    LOGGER.error(callBackError);
	    throw new IOException(callBackError);
	}
    }

    /**
     * Method that process a Callback of type {@link SignatureVerificationKeyCallback.X509IssuerSerialBasedRequest}.
     * @param cb Parameter that represents the Callback to process.
     * @throws IOException If the method fails.
     */
    private void processSignatureVerificationKeyCallbackX509IssuerSerialBasedRequest(SignatureVerificationKeyCallback cb) throws IOException {
	// Comprobamos que haya definido un certificado para validar las
	// respuestas SOAP
	if (certificateSOAPResponse != null) {
	    SignatureVerificationKeyCallback.X509IssuerSerialBasedRequest request = (SignatureVerificationKeyCallback.X509IssuerSerialBasedRequest) cb.getRequest();
	    // Obtenemos de la petición SOAP el IssuerName y el SerialNumber
	    String issuerNameReq = request.getIssuerName();
	    BigInteger serialNumberReq = request.getSerialNumber();

	    // Obtenemos el issuerName y el SerialNumber del certificado
	    // definido para firmar las respuestas SOAP
	    String issuerNameApp = certificateSOAPResponse.getIssuerDN().getName();
	    BigInteger serialNumberApp = certificateSOAPResponse.getSerialNumber();

	    // Normalizamos ambos issuerName y comparamos
	    String canonicalizedIssuerNameReq = UtilsCertificate.canonicalizeX500Principal(issuerNameReq);
	    String canonicalizedIssuerNameApp = UtilsCertificate.canonicalizeX500Principal(issuerNameApp);

	    if (canonicalizedIssuerNameReq.equals(canonicalizedIssuerNameApp) && serialNumberReq.equals(serialNumberApp)) {
		// Asignamos el certificado al request
		request.setX509Certificate(certificateSOAPResponse);
	    } else {
		String callBackError = Language.getResIntegra(ILogConstantKeys.TCBH_LOG003);
		LOGGER.error(callBackError);
		throw new IOException(callBackError);
	    }
	} else {
	    String callBackError = Language.getResIntegra(ILogConstantKeys.TCBH_LOG004);
	    LOGGER.error(callBackError);
	    throw new IOException(callBackError);
	}
    }

    /**
     * Method that process a Callback of type {@link EncryptionKeyCallback}.
     * @param cb Parameter that represents the Callback to process.
     * @param callback Parameter that represents the Callback parent.
     * @throws IOException If the method fails.
     * @throws UnsupportedCallbackException If the implementation of this method does not support one or more of the Callbacks
     * specified in the callbacks parameter.
     */
    private void processEncryptionKeyCallback(EncryptionKeyCallback cb, Callback callback) throws UnsupportedCallbackException, IOException {
	LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.TCBH_LOG001, new Object[ ] { cb.getClass().getSimpleName() }));
	// Si la encriptación está basada en clave simétrica
	if (cb.getRequest() instanceof EncryptionKeyCallback.AliasSymmetricKeyRequest) {
	    EncryptionKeyCallback.AliasSymmetricKeyRequest cbSymmetric = (EncryptionKeyCallback.AliasSymmetricKeyRequest) cb.getRequest();
	    // Procesamos la clave simétrica
	    StringTokenizer tripleDes = new StringTokenizer(symmetricKeyRequest, ",");
	    byte[ ] tripleDESKey = new byte[tripleDes.countTokens()];

	    int j = 0;
	    while (tripleDes.hasMoreTokens()) {
		String ss = tripleDes.nextToken();
		tripleDESKey[j] = (byte) Integer.decode(ss).intValue();
		j++;
	    }
	    SecretKey secret3 = new SecretKeySpec(tripleDESKey, "DESede");
	    cbSymmetric.setSymmetricKey(secret3);
	}
	// Si la encriptación está basada en clave pública
	else if (cb.getRequest() instanceof EncryptionKeyCallback.PublicKeyBasedRequest) {
	    processEncryptionKeyCallbackPublicKeyBasedRequest(cb);
	}
	// Si la encriptación no es ninguna de las anteriores
	else {
	    String callBackError = Language.getResIntegra(ILogConstantKeys.TCBH_LOG002);
	    LOGGER.error(callBackError);
	    throw new UnsupportedCallbackException(callback, callBackError);
	}
    }

    /**
     * Method that process a Callback of type {@link EncryptionKeyCallback.PublicKeyBasedRequest}.
     * @param cb Parameter that represents the Callback to process.
     * @throws IOException If the method fails.
     */
    private void processEncryptionKeyCallbackPublicKeyBasedRequest(EncryptionKeyCallback cb) throws IOException {
	LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.TCBH_LOG001, new Object[ ] { cb.getClass().getSimpleName() }));
	// Indicamos que estamos en la etapa de validar una respuesta securizada
	// con SAML
	processingSAMLResponse = true;
	// Comprobamos que esté definido el certificado usado para validar las
	// respuestas SOAP securizadas con SAML
	if (certificateSAMLResponse != null) {
	    EncryptionKeyCallback.PublicKeyBasedRequest cbPublicKey = (EncryptionKeyCallback.PublicKeyBasedRequest) cb.getRequest();
	    PublicKey publicKeyRequest = cbPublicKey.getPublicKey();
	    // Comprobamos si la clave pública del certificado indicado en la
	    // respuesta SOAP es igual al que hay configurado
	    if (certificateSAMLResponse.getPublicKey().equals(publicKeyRequest)) {
		cbPublicKey.setX509Certificate(certificateSAMLResponse);
	    }
	    // Si las claves públicas no coinciden lanzamos una excepción
	    else {
		String callBackError = Language.getResIntegra(ILogConstantKeys.TCBH_LOG005);
		LOGGER.error(callBackError);
		throw new IOException(callBackError);
	    }
	} else {
	    String callBackError = Language.getResIntegra(ILogConstantKeys.TCBH_LOG006);
	    LOGGER.error(callBackError);
	    throw new IOException(callBackError);
	}
    }

    /**
     * Method that process a Callback of type {@link DecryptionKeyCallback}.
     * @param cb Parameter that represents the Callback to process.
     * @param callback Parameter that represents the Callback parent.
     * @throws UnsupportedCallbackException If the implementation of this method does not support one or more of the Callbacks
     * specified in the callbacks parameter.
     * @throws IOException If the method fails.
     */
    private void processDecryptionKeyCallback(DecryptionKeyCallback cb, Callback callback) throws UnsupportedCallbackException, IOException {
	LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.TCBH_LOG001, new Object[ ] { cb.getClass().getSimpleName() }));
	// Si la encriptación está basada en clave simétrica
	if (cb.getRequest() instanceof DecryptionKeyCallback.AliasSymmetricKeyRequest) {
	    // Comprobamos que esté definida la clave simétrica, y su alias, con
	    // la que descifrar la respuesta SOAP
	    if (symmetricKeyResponse != null && aliasSymmetricKeyResponse != null) {
		DecryptionKeyCallback.AliasSymmetricKeyRequest cbSymmetric = (DecryptionKeyCallback.AliasSymmetricKeyRequest) cb.getRequest();
		// Comprobamos que el alias indicado es el correcto
		String alias = cbSymmetric.getAlias();
		if (!aliasSymmetricKeyResponse.equals(alias)) {
		    String callBackError = Language.getResIntegra(ILogConstantKeys.TCBH_LOG007);
		    LOGGER.error(callBackError);
		    throw new IOException(callBackError);
		}
		// Procesamos la clave simétrica
		StringTokenizer tripleDes = new StringTokenizer(symmetricKeyResponse, ",");
		byte[ ] tripleDESKey = new byte[tripleDes.countTokens()];

		int j = 0;
		while (tripleDes.hasMoreTokens()) {
		    String ss = tripleDes.nextToken();
		    tripleDESKey[j] = (byte) Integer.decode(ss).intValue();
		    j++;
		}
		SecretKey secret3 = new SecretKeySpec(tripleDESKey, "DESede");
		cbSymmetric.setSymmetricKey(secret3);
	    }
	    // Si la encriptación no está basada en clave simétrica
	    else {
		String callBackError = Language.getResIntegra(ILogConstantKeys.TCBH_LOG008);
		LOGGER.error(callBackError);
		throw new IOException(callBackError);
	    }
	} else {
	    String callBackError = Language.getResIntegra(ILogConstantKeys.TCBH_LOG002);
	    LOGGER.error(callBackError);
	    throw new UnsupportedCallbackException(callback, callBackError);
	}
    }

    /**
     * Method that process a Callback of type {@link DynamicPolicyCallback}.
     * @param dp Parameter that represents the Callback to process.
     * @param callback Parameter that represents the Callback parent.
     * @throws IOException If the method fails.
     * @throws UnsupportedCallbackException If the implementation of this method does not support one or more of the Callbacks
     * specified in the callbacks parameter.
     */
    private void processDynamicPolicyCallback(DynamicPolicyCallback dp, Callback callback) throws UnsupportedCallbackException, IOException {
	LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.TCBH_LOG001, new Object[ ] { dp.getClass().getSimpleName() }));
	// Obtenemos la política de seguridad
	SecurityPolicy policy = dp.getSecurityPolicy();
	// Si la política de seguridad es
	// AuthenticationTokenPolicy.SAMLAssertionBinding
	if (policy instanceof AuthenticationTokenPolicy.SAMLAssertionBinding) {
	    AuthenticationTokenPolicy.SAMLAssertionBinding samlBinding = (AuthenticationTokenPolicy.SAMLAssertionBinding) ((AuthenticationTokenPolicy.SAMLAssertionBinding) policy).clone();
	    // Si se carece de afirmaciones y de proveedor de identidad es
	    // necesario publicar la afirmación
	    if (samlBinding.getAssertion() == null && samlBinding.getAuthorityBinding() == null) {
		// Creamos la afirmación en función del tipo de securización
		if (samlBinding.getAssertionType() == AuthenticationTokenPolicy.SAMLAssertionBinding.SV_ASSERTION) {
		    Element svAssertion = createSVSAMLAssertion();
		    samlBinding.setAssertion(svAssertion);
		    dp.setSecurityPolicy(samlBinding);
		} else {
		    Element hokAssertion = createHOKSAMLAssertion();
		    samlBinding.setAssertion(hokAssertion);
		    PrivateKeyBinding pkBinding = (PrivateKeyBinding) samlBinding.newPrivateKeyBinding();
		    pkBinding.setPrivateKey(privateKeySOAPRequest);
		    dp.setSecurityPolicy(samlBinding);
		}
	    }
	    // Si el mensaje viene securizado con SAML lo validamos
	    else if (samlBinding.getAssertion() != null) {
		validateSAMLAssertion(samlBinding);
	    }

	}
	// Si la política de seguridad es de un tipo no reconocido
	else {
	    String callBackError = Language.getResIntegra(ILogConstantKeys.TCBH_LOG002);
	    LOGGER.error(callBackError);
	    throw new UnsupportedCallbackException(callback, callBackError);
	}
    }

    /**
     * Method that validates a SAML assert from the SOAP request.
     * @param samlBinding Parameter that represents the SAML assert.
     * @throws IOException If the method fails.
     */
    private void validateSAMLAssertion(AuthenticationTokenPolicy.SAMLAssertionBinding samlBinding) throws IOException {
	// Indicamos que estamos procesando la etapa de validación de la
	// securización SAML de la respuesta SOAP
	processingSAMLResponse = true;

	try {
	    // Obtenemos la afirmación SAML
	    SAMLAssertionFactory factory = SAMLAssertionFactory.newInstance(SAMLAssertionFactory.SAML1_1);
	    Assertion a = factory.createAssertion(samlBinding.getAssertion());

	    BigInteger minorVersion = a.getMinorVersion();
	    BigInteger majorVersion = a.getMajorVersion();
	    if (!minorVersion.equals(BigInteger.ONE) || !majorVersion.equals(BigInteger.ONE)) {
		String callBackError = Language.getFormatResIntegra(ILogConstantKeys.TCBH_LOG009, new Object[ ] { majorVersion, minorVersion });
		LOGGER.error(callBackError);
		throw new IOException(callBackError);
	    }
	} catch (SAMLException e) {
	    String callBackError = Language.getResIntegra(ILogConstantKeys.TCBH_LOG010);
	    LOGGER.error(callBackError);
	    throw new IOException(callBackError, e);
	} catch (XWSSecurityException e) {
	    String callBackError = Language.getResIntegra(ILogConstantKeys.TCBH_LOG010);
	    LOGGER.error(callBackError);
	    throw new IOException(callBackError, e);
	}
    }

    /**
     * Method that creates a SAML assertion using the method Holder-of-Key (HOV).
     * @return an element that represents the SAML assertion
     * @throws IOException If the method fails.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Element createHOKSAMLAssertion() throws IOException {
	Assertion assertion = null;

	// Obtenemos el Issuer del certificado
	String issuer = certificateSOAPRequest.getIssuerDN().toString();
	// Creamos el ID de la afirmación en base a la fecha actual
	String assertionID = Long.toString(System.currentTimeMillis());

	GregorianCalendar c = new GregorianCalendar();
	long beforeTime = c.getTimeInMillis();
	// roll the time by one hour
	long offsetHours = NumberConstants.INT_60 * NumberConstants.INT_60 * NumberConstants.INT_1000;

	c.setTimeInMillis(beforeTime - offsetHours);
	GregorianCalendar before = (GregorianCalendar) c.clone();

	c = new GregorianCalendar();
	long afterTime = c.getTimeInMillis();
	c.setTimeInMillis(afterTime + offsetHours);
	GregorianCalendar after = (GregorianCalendar) c.clone();

	GregorianCalendar issueInstant = new GregorianCalendar();

	try {
	    SAMLAssertionFactory factory = SAMLAssertionFactory.newInstance(SAMLAssertionFactory.SAML1_1);

	    // statements
	    List statements = new LinkedList();
	    NameIdentifier nmId = factory.createNameIdentifier(issuer, null, "urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName");

	    PublicKey pubKey = certificateSOAPRequest.getPublicKey();
	    DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();

	    Document doc = docFactory.newDocumentBuilder().newDocument();

	    KeyInfo keyInfo = new KeyInfo(doc);
	    keyInfo.addKeyValue(pubKey);

	    List subConfirmation = new ArrayList();
	    subConfirmation.add("urn:oasis:names:tc:SAML:1.0:cm:holder-of-key");

	    SubjectConfirmation scf = factory.createSubjectConfirmation(subConfirmation, null, keyInfo.getElement());

	    Subject subj = factory.createSubject(nmId, scf);

	    List attributes = new LinkedList();

	    statements.add(factory.createAttributeStatement(subj, attributes));

	    Conditions conditions = factory.createConditions(before, after, null, null, null);

	    assertion = factory.createAssertion(assertionID, issuer, issueInstant, conditions, null, statements);
	    assertion.setMajorVersion(certificateSOAPRequest.getSerialNumber());
	    assertion.setMinorVersion(BigInteger.ONE);

	    return assertion.sign(pubKey, privateKeySOAPRequest);
	} catch (Exception e) {
	    String callBackError = Language.getResIntegra(ILogConstantKeys.TCBH_LOG011);
	    LOGGER.error(callBackError);
	    throw new IOException(callBackError, e);
	}

    }

    /**
     * Method that creates a SAML assertion using the method Sender-Vouches (SV).
     * @return an element that represents the SAML assertion
     * @throws IOException If the method fails.
     */
    @SuppressWarnings({ "rawtypes", "unchecked" })
    private Element createSVSAMLAssertion() throws IOException {
	Assertion assertion = null;
	// Obtenemos el Issuer del certificado
	String issuer = certificateSOAPRequest.getIssuerDN().toString();
	// Creamos el ID de la afirmación en base a la fecha actual
	String assertionID = Long.toString(System.currentTimeMillis());

	GregorianCalendar c = new GregorianCalendar();
	long beforeTime = c.getTimeInMillis();
	// roll the time by one hour
	long offsetHours = NumberConstants.INT_60 * NumberConstants.INT_60 * NumberConstants.INT_1000;

	c.setTimeInMillis(beforeTime - offsetHours);
	GregorianCalendar before = (GregorianCalendar) c.clone();

	c = new GregorianCalendar();
	long afterTime = c.getTimeInMillis();
	c.setTimeInMillis(afterTime + offsetHours);
	GregorianCalendar after = (GregorianCalendar) c.clone();

	GregorianCalendar issueInstant = new GregorianCalendar();
	// statements
	List statements = new LinkedList();
	try {
	    SAMLAssertionFactory factory = SAMLAssertionFactory.newInstance(SAMLAssertionFactory.SAML1_1);

	    NameIdentifier nmId = factory.createNameIdentifier(issuer, null, "urn:oasis:names:tc:SAML:1.1:nameid-format:X509SubjectName");

	    SubjectConfirmation scf = factory.createSubjectConfirmation("urn:oasis:names:tc:SAML:1.0:cm:sender-vouches");

	    Subject subj = factory.createSubject(nmId, scf);

	    List attributes = new LinkedList();

	    statements.add(factory.createAttributeStatement(subj, attributes));

	    Conditions conditions = factory.createConditions(before, after, null, null, null);

	    assertion = factory.createAssertion(assertionID, issuer, issueInstant, conditions, null, statements);
	    assertion.setMajorVersion(certificateSOAPRequest.getSerialNumber());
	    assertion.setMinorVersion(BigInteger.ONE);

	    return assertion.toElement(null);
	} catch (Exception e) {
	    String callBackError = Language.getResIntegra(ILogConstantKeys.TCBH_LOG012);
	    LOGGER.error(callBackError);
	    throw new IOException(callBackError, e);
	}
    }

    /**
     * <p>Private class that represents a validator of the creation date of a SOAP request.</p>
     * <b>Project:</b><p>@Firma and TS@ Web Services Integration Platform.</p>
     * @version 1.0, 13/01/2014.
     */
    public class TimestampValidatorImpl implements TimestampValidator {

	/**
	 * {@inheritDoc}
	 * @see com.sun.xml.wss.impl.callback.TimestampValidationCallback.TimestampValidator#validate(com.sun.xml.wss.impl.callback.TimestampValidationCallback.Request)
	 */
	public void validate(Request arg0) throws TimestampValidationException {
	}
    }

    /**
     * <p>Private class that represents a validator of certificates.</p>
     * <b>Project:</b><p>@Firma and TS@ Web Services Integration Platform.</p>
     * @version 1.0, 13/01/2014.
     */
    public class X509CertificateValidatorImpl implements CertificateValidationCallback.CertificateValidator {

	/**
	 * {@inheritDoc}
	 * @see com.sun.xml.wss.impl.callback.CertificateValidationCallback.CertificateValidator#validate(java.security.cert.X509Certificate)
	 */
	@Override
	public final boolean validate(X509Certificate certificate) throws CertificateValidationException {
	    // Comprobamos si estabamos procesado la etapa de validación de la
	    // securización SAML, en ese caso, como llevamos a cabo la
	    // validación
	    // anteriormente, el certificado es correcto
	    if (processingSAMLResponse) {
		return true;
	    }
	    // En otro caso significa que estamos procesando la etapa de
	    // validación de la
	    // firma de la petición SOAP
	    else {
		// Comprobamos que esté definido el certificado con el que
		// validar las respuestas SOAP firmadas
		if (certificateSOAPResponse != null) {
		    // Obtenemos el número de serie del certificado usado para
		    // firmar la respuesta SOAP
		    BigInteger serialNumberSOAPResponse = certificate.getSerialNumber();
		    // Obtenemos el emisor del certificado usado para firmar la
		    // respuesta SOAP
		    String issuerDNSOAPResponse = UtilsCertificate.canonicalizeX500Principal(certificate.getIssuerDN().getName());

		    // Obtenemos el número de serie del certificado definido
		    // para
		    // firmar las respuestas SOAP
		    BigInteger serialNumber = certificateSOAPResponse.getSerialNumber();
		    // Obtenemos el emisor del certificado definido para firmar
		    // las
		    // respuestas SOAP
		    String issuerDN = UtilsCertificate.canonicalizeX500Principal(certificateSOAPResponse.getIssuerDN().getName());

		    // Comprobamos si el certificado usado para firmar la
		    // respuesta
		    // SOAP por parte de la plataforma TS@ coincide con el que
		    // tenemos
		    // definido en el archivo de propiedades.
		    if (!serialNumberSOAPResponse.equals(serialNumber) || !issuerDNSOAPResponse.equals(issuerDN)) {
			return false;
		    }
		    return true;
		} else {
		    String callBackError = Language.getResIntegra(ILogConstantKeys.TCBH_LOG004);
		    LOGGER.error(callBackError);
		    return false;
		}
	    }
	}
    }

    /**
     * Sets the value of the attribute {@link #certificateSOAPRequest}.
     * @param certificateSOAPRequestParam The value for the attribute {@link #certificateSOAPRequest}.
     */
    public final void setCertificateSOAPRequest(X509Certificate certificateSOAPRequestParam) {
	this.certificateSOAPRequest = certificateSOAPRequestParam;
    }

    /**
     * Sets the value of the attribute {@link #privateKeySOAPRequest}.
     * @param privateKeySOAPRequestParam The value for the attribute {@link #privateKeySOAPRequest}.
     */
    public final void setPrivateKeySOAPRequest(PrivateKey privateKeySOAPRequestParam) {
	this.privateKeySOAPRequest = privateKeySOAPRequestParam;
    }

    /**
     * Sets the value of the attribute {@link #certificateSAMLResponse}.
     * @param certificateSAMLResponseParam The value for the attribute {@link #certificateSAMLResponse}.
     */
    public final void setCertificateSAMLResponse(X509Certificate certificateSAMLResponseParam) {
	this.certificateSAMLResponse = certificateSAMLResponseParam;
    }

    /**
     * Sets the value of the attribute {@link #certificateSOAPResponse}.
     * @param certificateSOAPResponseParam The value for the attribute {@link #certificateSOAPResponse}.
     */
    public final void setCertificateSOAPResponse(X509Certificate certificateSOAPResponseParam) {
	this.certificateSOAPResponse = certificateSOAPResponseParam;
    }

    /**
     * Sets the value of the attribute {@link #symmetricKeyRequest}.
     * @param symmetricKeyRequestParam The value for the attribute {@link #symmetricKeyRequest}.
     */
    public final void setSymmetricKeyRequest(String symmetricKeyRequestParam) {
	this.symmetricKeyRequest = symmetricKeyRequestParam;
    }

    /**
     * Sets the value of the attribute {@link #symmetricKeyResponse}.
     * @param symmetricKeyResponseParam The value for the attribute {@link #symmetricKeyResponse}.
     */
    public final void setSymmetricKeyResponse(String symmetricKeyResponseParam) {
	this.symmetricKeyResponse = symmetricKeyResponseParam;
    }

    /**
     * Sets the value of the attribute {@link #aliasSymmetricKeyResponse}.
     * @param aliasSymmetricKeyResponseParam The value for the attribute {@link #aliasSymmetricKeyResponse}.
     */
    public final void setAliasSymmetricKeyResponse(String aliasSymmetricKeyResponseParam) {
	this.aliasSymmetricKeyResponse = aliasSymmetricKeyResponseParam;
    }

}

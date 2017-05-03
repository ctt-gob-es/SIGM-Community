/*
 * Este fichero forma parte de la plataforma TS@.
 * La plataforma TS@ es de libre distribución cuyo código fuente puede ser consultado
 * y descargado desde http://forja-ctt.administracionelectronica.gob.es
 *
 * Copyright 2013-,2014 Gobierno de España
 * Este fichero se distribuye bajo las licencias EUPL versión 1.1  y GPL versión 3, o superiores, según las
 * condiciones que figuran en el fichero 'LICENSE.txt' que se acompaña.  Si se   distribuyera este
 * fichero individualmente, deben incluirse aquí las condiciones expresadas allí.
 */

/**
 * <b>File:</b><p>es.gob.afirma.rfc3161.RFC3161ServiceInvoker.java.</p>
 * <b>Description:</b><p>Class that manages the generation of timestamps via TS@ RFC 3161 service (TCP, HTTPS and SSL).</p>
 * <b>Project:</b><p>Time Stamping Authority.</p>
 * <b>Date:</b><p>22/01/2014.</p>
 * @author Gobierno de España.
 * @version 1.0, 22/01/2014.
 */
package es.gob.afirma.rfc3161TSAServiceInvoker;

import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.Socket;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Properties;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.RIPEMD160Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.crypto.digests.SHA512Digest;
import org.bouncycastle.tsp.TSPAlgorithms;
import org.bouncycastle.tsp.TimeStampRequest;
import org.bouncycastle.tsp.TimeStampRequestGenerator;

import es.gob.afirma.i18n.ILogConstantKeys;
import es.gob.afirma.i18n.Language;
import es.gob.afirma.tsaServiceInvoker.TSAServiceInvokerConstants;
import es.gob.afirma.tsaServiceInvoker.TSAServiceInvokerException;
import es.gob.afirma.tsaServiceInvoker.TSAServiceInvokerProperties;
import es.gob.afirma.utils.CryptoUtil;
import es.gob.afirma.utils.GenericUtils;
import es.gob.afirma.utils.NumberConstants;
import es.gob.afirma.utils.UtilsKeystore;
import es.gob.afirma.utils.UtilsResources;

/**
 * <p>Class that manages the generation of timestamps via TS@ RFC 3161 service (TCP, HTTPS and SSL).</p>
 * <b>Project:</b><p>Time Stamping Authority.</p>
 * @version 1.0, 22/01/2014.
 */
public class RFC3161TSAServiceInvoker {

    /**
     * Constant attribute that identifies the provider Sun for X.509 content type.
     */
    private static final String TRUST_MANAGER_FACTORY_SUN_X509 = "SunX509";

    /**
     * Attribute that represents the object that manages the log of the class.
     */
    private static final Logger LOGGER = Logger.getLogger(RFC3161TSAServiceInvoker.class);

    /**
     * Attribute that represents the properties defined on the configuration file.
     */
    private Properties properties;

    /**
     * Attribute that represents the application identifier.
     */
    private String application;

    /**
     * Attribute that represents the header for each key defined on the configuration file.
     */
    private String propertyHeader;

    /**
     * Attribute that represents the OID of the timestamp policy.
     */
    private String policyOID;

    /**
     * Attribute that represents the OID of the application.
     */
    private String applicationOID;

    /**
     * Attribute that represents the data to stamp with a timestamp.
     */
    private byte[ ] dataToStamp;

    /**
     * Attribute that represents the timeout for the connection with TS@.
     */
    private int timeOut;

    /**
     * Attribute that represents the digest of the data to stamp.
     */
    private Digest digest;

    /**
     * Attribute that represents the hash algorithm used to calculate the digest of the data to stamp.
     */
    private String digestAlgorithm;

    /**
     * Attribute that represents the host where is deployed the TS@ RFC 3161 service.
     */
    private String host;

    /**
     * Constructor method for the class RFC3161TSAServiceInvoker.java.
     * @throws TSAServiceInvokerException If the method fails.
     */
    public RFC3161TSAServiceInvoker() throws TSAServiceInvokerException {
	properties = TSAServiceInvokerProperties.getTsaServiceInvokerProperties();
	if (properties == null) {
	    throw new TSAServiceInvokerException(Language.getResIntegra(ILogConstantKeys.RTSI_LOG001));
	}
    }

    /**
     * Method that invokes the RFC 3161 timestamp generation service from TS@.
     * @param protocol Parameter that represents the protocol used to communicate with the RFC 3161 service. The allowed values are:
     * <ul>
     * <li>{@link RFC3161TSAServiceInvokerConstants.RFC3161Protocol#TCP}</li>
     * <li>{@link RFC3161TSAServiceInvokerConstants.RFC3161Protocol#HTTPS}</li>
     * <li>{@link RFC3161TSAServiceInvokerConstants.RFC3161Protocol#SSL}</li>
     * </ul>
     * @param applicationParam Parameter that represents the name of the application which invokes the service.
     * @param dataParam Parameter that represents the data to stamp
     * @return the timestamp response.
     * @throws TSAServiceInvokerException If the method fails.
     */
    public final byte[ ] generateTimeStampToken(String protocol, String applicationParam, byte[ ] dataParam) throws TSAServiceInvokerException {
	// Comprobamos que se han indicado datos de entrada
	if (!GenericUtils.assertStringValue(protocol)) {
	    throw new TSAServiceInvokerException(Language.getResIntegra(ILogConstantKeys.RTSI_LOG002));
	}
	if (!GenericUtils.assertStringValue(applicationParam)) {
	    throw new TSAServiceInvokerException(Language.getResIntegra(ILogConstantKeys.RTSI_LOG003));
	}
	if (dataParam == null) {
	    throw new TSAServiceInvokerException(Language.getResIntegra(ILogConstantKeys.RTSI_LOG004));
	}
	// Rescatamos el identificador de aplicación seleccionada
	LOGGER.info(Language.getFormatResIntegra(ILogConstantKeys.RTSI_LOG005, new Object[ ] { applicationParam }));
	application = applicationParam;
	propertyHeader = application + ".";

	// Rescatamos los datos a sellar
	dataToStamp = dataParam.clone();

	// Obtenemos el OID de la política de sello de tiempo
	policyOID = properties.getProperty(propertyHeader + TSAServiceInvokerConstants.RFC3161_TIMESTAMP_POLICY_OID);
	checkSvcInvokerParams(TSAServiceInvokerConstants.RFC3161_TIMESTAMP_POLICY_OID, policyOID);
	LOGGER.info(Language.getFormatResIntegra(ILogConstantKeys.RTSI_LOG006, new Object[ ] { policyOID }));

	// Obtenemos el OID de la aplicación
	applicationOID = properties.getProperty(propertyHeader + TSAServiceInvokerConstants.RFC3161_APPLICATION_OID);
	checkSvcInvokerParams(TSAServiceInvokerConstants.RFC3161_APPLICATION_OID, applicationOID);
	LOGGER.info(Language.getFormatResIntegra(ILogConstantKeys.RTSI_LOG007, new Object[ ] { applicationOID }));

	// Obtenemos el algoritmo de resumen para los datos
	String hashAlgorithm = properties.getProperty(propertyHeader + TSAServiceInvokerConstants.RFC3161_HASH_ALGORITHM);
	checkSvcInvokerParams(TSAServiceInvokerConstants.RFC3161_HASH_ALGORITHM, hashAlgorithm);
	// Comprobamos que el algoritmo de resumen está soportado
	getDigestData(hashAlgorithm);
	LOGGER.info(Language.getFormatResIntegra(ILogConstantKeys.RTSI_LOG008, new Object[ ] { hashAlgorithm }));

	// Obtenemos la dirección del host donde está desplegado el servicio
	host = properties.getProperty(propertyHeader + TSAServiceInvokerConstants.RFC3161_HOST);
	checkSvcInvokerParams(TSAServiceInvokerConstants.RFC3161_HOST, host);
	LOGGER.info(Language.getFormatResIntegra(ILogConstantKeys.RTSI_LOG009, new Object[ ] { host }));

	// Obtenemos el tiempo de espera máximo para las peticiones
	String timeOutStr = properties.getProperty(propertyHeader + TSAServiceInvokerConstants.RFC3161_TIMEOUT);
	checkSvcInvokerParams(TSAServiceInvokerConstants.RFC3161_TIMEOUT, timeOutStr);
	try {
	    timeOut = Integer.valueOf(timeOutStr);
	} catch (NumberFormatException e) {
		throw new TSAServiceInvokerException(Language.getFormatResIntegra(ILogConstantKeys.RTSI_LOG050, new Object[ ] { timeOutStr }));
	}
	LOGGER.info(Language.getFormatResIntegra(ILogConstantKeys.RTSI_LOG010, new Object[ ] { timeOut }));

	// Comprobamos que el protocolo indicado es correcto, cargamos los datos
	// asociados al mismo y llamamos al servicio correspondiente
	LOGGER.info(Language.getFormatResIntegra(ILogConstantKeys.RTSI_LOG011, new Object[ ] { protocol }));
	if (protocol.equals(TSAServiceInvokerConstants.RFC3161Protocol.TCP)) {
	    return callRFC3161Service();
	} else if (protocol.equals(TSAServiceInvokerConstants.RFC3161Protocol.HTTPS)) {
	    return callRFC3161HTTPSService();
	} else if (protocol.equals(TSAServiceInvokerConstants.RFC3161Protocol.SSL)) {
	    return callRFC3161SSLService();
	} else {
	    throw new TSAServiceInvokerException(Language.getFormatResIntegra(ILogConstantKeys.RTSI_LOG012, new Object[ ] { protocol }));
	}
    }

    /**
     * Method that sets the values of {@link #digest} and {@link #digestAlgorithm} from the hash algorithm defined inside of the configuration properties file.
     * @param hashAlgorithm Parameter that represents the hash algorithm defined defined inside of the configuration properties file for calculating the
     * hash of the data to stamp.
     * @throws TSAServiceInvokerException If the method fails.
     */
    private void getDigestData(String hashAlgorithm) throws TSAServiceInvokerException {
	if (hashAlgorithm.equals(CryptoUtil.HASH_ALGORITHM_SHA1)) {
	    digest = new SHA1Digest();
	    digestAlgorithm = TSPAlgorithms.SHA1;
	} else if (hashAlgorithm.equals(CryptoUtil.HASH_ALGORITHM_SHA256)) {
	    digest = new SHA256Digest();
	    digestAlgorithm = TSPAlgorithms.SHA256;
	} else if (hashAlgorithm.equals(CryptoUtil.HASH_ALGORITHM_SHA512)) {
	    digest = new SHA512Digest();
	    digestAlgorithm = TSPAlgorithms.SHA512;
	} else if (hashAlgorithm.equals(CryptoUtil.HASH_ALGORITHM_RIPEMD160)) {
	    digest = new RIPEMD160Digest();
	    digestAlgorithm = TSPAlgorithms.RIPEMD160;
	} else {
	    throw new TSAServiceInvokerException(Language.getFormatResIntegra(ILogConstantKeys.RTSI_LOG013, new Object[ ] { hashAlgorithm }));
	}
    }

    /**
     * Method that invokes the TS@ RFC 3161 service to obtain a timestamp.
     * @return a bytes array that represents a TimeStampResponse.
     * @throws TSAServiceInvokerException If the method fails.
     */
    private byte[ ] callRFC3161Service() throws TSAServiceInvokerException {
	LOGGER.info(Language.getResIntegra(ILogConstantKeys.RTSI_LOG014));
	OutputStream dataoutputstream = null;
	OutputStream os = null;
	Socket socket = null;
	String msgError = null;
	try {
	    // Obtenemos el puerto donde está desplegado el servicio
	    String portStr = properties.getProperty(propertyHeader + TSAServiceInvokerConstants.RFC3161_PORT_NUMBER);
	    Integer portNumber = null;
	    checkSvcInvokerParams(TSAServiceInvokerConstants.RFC3161_PORT_NUMBER, portStr);
	    try {
		portNumber = Integer.valueOf(portStr);
	    } catch (NumberFormatException e) {
		throw new TSAServiceInvokerException(Language.getFormatResIntegra(ILogConstantKeys.RTSI_LOG015, new Object[ ] { portStr }), e);
	    }
	    LOGGER.info(Language.getFormatResIntegra(ILogConstantKeys.RTSI_LOG016, new Object[ ] { portNumber }));

	    // Generamos la petición de sello de tiempo
	    byte[ ] request = generateTimeStampRequest();

	    // Iniciamos el socket
	    msgError = Language.getResIntegra(ILogConstantKeys.RTSI_LOG017);
	    LOGGER.info(Language.getResIntegra(ILogConstantKeys.RTSI_LOG018));
	    socket = new Socket(host, Integer.valueOf(portNumber));
	    socket.setSoTimeout(timeOut);
	    dataoutputstream = new DataOutputStream(socket.getOutputStream());
	    os = socket.getOutputStream();
	    ((DataOutputStream) dataoutputstream).writeInt(request.length + 1);
	    ((DataOutputStream) dataoutputstream).writeByte(0);
	    dataoutputstream.write(request);
	    dataoutputstream.flush();
	    os.flush();
	    LOGGER.info(Language.getResIntegra(ILogConstantKeys.RTSI_LOG019));
	    // Obtenemos el TimeStampResponse
	    return getTimeStampResponse(socket);
	} catch (IOException e) {
	    throw new TSAServiceInvokerException(msgError, e);
	} finally {
	    // Cerramos recursos
	    UtilsResources.safeCloseSocket(socket);
	    UtilsResources.safeCloseOutputStream(os);
	    UtilsResources.safeCloseOutputStream(dataoutputstream);
	}
    }

    /**
     * Method that obtains a timestamp from a socket.
     * @param socket Parameter that represents the socket.
     * @return a bytes array that represents a TimeStampResponse.
     * @throws TSAServiceInvokerException If the method fails.
     */
    private byte[ ] getTimeStampResponse(Socket socket) throws TSAServiceInvokerException {
	InputStream datainputstream = null;
	byte[ ] resp = null;

	try {
	    datainputstream = new DataInputStream(socket.getInputStream());
	    int size = ((DataInputStream) datainputstream).readInt();
	    byte byte0 = ((DataInputStream) datainputstream).readByte();
	    resp = new byte[size - 1];
	    ((DataInputStream) datainputstream).readFully(resp);
	    if (byte0 != NumberConstants.INT_5 && byte0 != NumberConstants.INT_6) {
		throw new TSAServiceInvokerException(Language.getFormatResIntegra(ILogConstantKeys.RTSI_LOG020, new Object[ ] { new String(resp) }));
	    }
	} catch (IOException e) {
	    throw new TSAServiceInvokerException(Language.getResIntegra(ILogConstantKeys.RTSI_LOG021), e);
	} finally {
	    UtilsResources.safeCloseInputStream(datainputstream);
	}

	return resp;
    }

    /**
     * Method that invokes the TS@ RFC 3161 - HTTPS service to obtain a timestamp.
     * @return a bytes array that represents a TimeStampResponse.
     * @throws TSAServiceInvokerException If the method fails.
     */
    private byte[ ] callRFC3161HTTPSService() throws TSAServiceInvokerException {
	LOGGER.info(Language.getResIntegra(ILogConstantKeys.RTSI_LOG022));
	String msgError = null;
	OutputStream out = null;
	InputStream inp = null;
	OutputStream baos = null;
	HttpsURLConnection tsaConnection = null;
	try {
	    // Rescatamos la ruta al almacén de confianza
	    String trustsorePath = properties.getProperty(TSAServiceInvokerConstants.TRUSTEDSTORE_PATH);
	    checkSvcInvokerParams(TSAServiceInvokerConstants.TRUSTEDSTORE_PATH, trustsorePath);
	    LOGGER.info(Language.getFormatResIntegra(ILogConstantKeys.RTSI_LOG023, new Object[ ] { trustsorePath }));

	    // Rescatamos la clave del almacén de confianza
	    String truststorePassword = properties.getProperty(TSAServiceInvokerConstants.TRUSTEDSTORE_PASSWORD);
	    checkSvcInvokerParams(TSAServiceInvokerConstants.TRUSTEDSTORE_PASSWORD, truststorePassword);

	    // Cargamos el almacén de confianza
	    msgError = Language.getResIntegra(ILogConstantKeys.RTSI_LOG024);
	    KeyStore cer = UtilsKeystore.loadKeystore(trustsorePath, truststorePassword, UtilsKeystore.JKS);

	    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TRUST_MANAGER_FACTORY_SUN_X509);
	    tmf.init(cer);

	    SSLContext ctx = SSLContext.getInstance("SSL");
	    
	    // Obtenemos el indicador para saber si es necesaria la
	    // autenticación del cliente
	    String authClientStr = properties.getProperty(propertyHeader + TSAServiceInvokerConstants.RFC3161_HTTPS_AUTH);
	    checkSvcInvokerParams(TSAServiceInvokerConstants.RFC3161_HTTPS_AUTH, authClientStr);
	    if (authClientStr.equals(Boolean.toString(true))) {
		LOGGER.info(Language.getResIntegra(ILogConstantKeys.RTSI_LOG025));

		// Rescatamos la ruta al almacén de claves al almacén de claves
		// donde se encuentra almacenada la clave privada a usar para la
		// autenticación HTTPS
		String keystorePath = properties.getProperty(propertyHeader + TSAServiceInvokerConstants.RFC3161_HTTPS_KEYSTORE_PATH);
		checkSvcInvokerParams(TSAServiceInvokerConstants.RFC3161_HTTPS_KEYSTORE_PATH, keystorePath);
		LOGGER.info(Language.getFormatResIntegra(ILogConstantKeys.RTSI_LOG026, new Object[ ] { keystorePath }));

		// Rescatamos el tipo de almacén de claves donde se encuentra
		// almacenada la clave privada a usar para la autenticación
		// HTTPS
		String keystoreType = properties.getProperty(propertyHeader + TSAServiceInvokerConstants.RFC3161_HTTPS_KEYSTORE_TYPE);
		checkSvcInvokerParams(TSAServiceInvokerConstants.RFC3161_HTTPS_KEYSTORE_TYPE, keystoreType);
		// Comprobamos que el tipo de almacén de claves está soportado
		checkKeystoreType(keystoreType, Language.getFormatResIntegra(ILogConstantKeys.RTSI_LOG027, new Object[ ] { keystoreType }));
		LOGGER.info(Language.getFormatResIntegra(ILogConstantKeys.RTSI_LOG028, new Object[ ] { keystoreType }));

		// Rescatamos la contraseña del almacén de claves donde se
		// encuentra
		// almacenada la clave privada a usar para la autenticación
		// HTTPS
		String keystorePassword = properties.getProperty(propertyHeader + TSAServiceInvokerConstants.RFC3161_HTTPS_KEYSTORE_PASSWORD);
		checkSvcInvokerParams(TSAServiceInvokerConstants.RFC3161_HTTPS_KEYSTORE_PASSWORD, keystorePassword);

		// Obtenemos el almacén de claves para la autenticación cliente
		msgError = Language.getResIntegra(ILogConstantKeys.RTSI_LOG029);
		KeyStore ks = UtilsKeystore.loadKeystore(keystorePath, keystorePassword, keystoreType);
		KeyManagerFactory kmf = KeyManagerFactory.getInstance(TRUST_MANAGER_FACTORY_SUN_X509);
		kmf.init(ks, keystorePassword.toCharArray());
		msgError = Language.getResIntegra(ILogConstantKeys.RTSI_LOG030);
		ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

	    } else if (authClientStr.equals(Boolean.toString(false))) {
		LOGGER.info(Language.getResIntegra(ILogConstantKeys.RTSI_LOG031));
		msgError = Language.getResIntegra(ILogConstantKeys.RTSI_LOG030);
		ctx.init(null, tmf.getTrustManagers(), null);
	    } else {
		throw new TSAServiceInvokerException(Language.getFormatResIntegra(ILogConstantKeys.RTSI_LOG032, new Object[ ] { authClientStr }));
	    }

	    // Generamos la petición de sello de tiempo
	    byte request[] = generateTimeStampRequest();

	    // Obtenemos la URL de conexión con el servicio RFC 3161
	    URL url = getTSAURL();
	    LOGGER.info(Language.getResIntegra(ILogConstantKeys.RTSI_LOG018));

	    msgError = Language.getResIntegra(ILogConstantKeys.RTSI_LOG017);
	    tsaConnection = (HttpsURLConnection) url.openConnection();
	    tsaConnection.setHostnameVerifier(new NameVerifier());

	    SSLSocketFactory factory = ctx.getSocketFactory();
	    tsaConnection.setSSLSocketFactory(factory);
	    tsaConnection.setDoInput(true);
	    tsaConnection.setDoOutput(true);
	    tsaConnection.setUseCaches(false);
	    tsaConnection.setRequestProperty("Content-Type", "application/timestamp-query");
	    tsaConnection.setAllowUserInteraction(false);
	    tsaConnection.setRequestProperty("Content-Transfer-Encoding", "binary");
	    
	    tsaConnection.toString();

	    out = tsaConnection.getOutputStream();
	    out.write(request);
	    out.close();

	    // Obtenemos la respuesta
	    LOGGER.info(Language.getResIntegra(ILogConstantKeys.RTSI_LOG019));
	    msgError = Language.getResIntegra(ILogConstantKeys.RTSI_LOG021);
	    inp = tsaConnection.getInputStream();
	    baos = new ByteArrayOutputStream();
	    byte[ ] buffer = new byte[NumberConstants.INT_1024];
	    int bytesRead = 0;
	    while ((bytesRead = inp.read(buffer, 0, buffer.length)) >= 0) {
		baos.write(buffer, 0, bytesRead);
	    }
	    return ((ByteArrayOutputStream) baos).toByteArray();
	} catch (IOException e) {
	    throw new TSAServiceInvokerException(msgError, e);
	} catch (NoSuchAlgorithmException e) {
	    throw new TSAServiceInvokerException(msgError, e);
	} catch (KeyStoreException e) {
	    throw new TSAServiceInvokerException(msgError, e);
	} catch (CertificateException e) {
	    throw new TSAServiceInvokerException(msgError, e);
	} catch (UnrecoverableKeyException e) {
	    throw new TSAServiceInvokerException(msgError, e);
	} catch (KeyManagementException e) {
	    throw new TSAServiceInvokerException(msgError, e);
	} finally {
	    UtilsResources.safeCloseOutputStream(baos);
	    UtilsResources.safeCloseInputStream(inp);
	    UtilsResources.safeCloseOutputStream(out);
	}
    }

    /**
     * Method that obtains the URL for connecting to TS@ RFC 3161 - HTTPS service from the configuration properties file.
     * @return the URL for connecting to TS@ RFC 3161 - HTTPS service.
     * @throws TSAServiceInvokerException If the method fails.
     */
    private URL getTSAURL() throws TSAServiceInvokerException {
	// Obtenemos el puerto donde está desplegado el servicio HTTPS
	String portStr = properties.getProperty(propertyHeader + TSAServiceInvokerConstants.RFC3161_HTTPS_PORT_NUMBER);
	Integer portNumber = null;
	checkSvcInvokerParams(TSAServiceInvokerConstants.RFC3161_HTTPS_PORT_NUMBER, portStr);
	try {
	    portNumber = Integer.valueOf(portStr);
	} catch (NumberFormatException e) {
	    throw new TSAServiceInvokerException(Language.getFormatResIntegra(ILogConstantKeys.RTSI_LOG033, new Object[ ] { portStr }), e);
	}
	LOGGER.info(Language.getFormatResIntegra(ILogConstantKeys.RTSI_LOG034, new Object[ ] { portNumber }));

	// Obtenemos el contexto donde está desplegado el servicio HTTPS
	String context = properties.getProperty(propertyHeader + TSAServiceInvokerConstants.RFC3161_HTTPS_CONTEXT);
	checkSvcInvokerParams(TSAServiceInvokerConstants.RFC3161_HTTPS_CONTEXT, context);
	LOGGER.info(Language.getFormatResIntegra(ILogConstantKeys.RTSI_LOG035, new Object[ ] { context }));

	String tsaURL = "https://" + host + ":" + portNumber + context;
	LOGGER.info(Language.getFormatResIntegra(ILogConstantKeys.RTSI_LOG036, new Object[ ] { tsaURL }));
	try {
	    return new URL(tsaURL);
	} catch (MalformedURLException e) {
	    throw new TSAServiceInvokerException(Language.getResIntegra(ILogConstantKeys.RTSI_LOG037), e);
	}
    }

    /**
     * Method that invokes the TS@ RFC 3161 - SSL service to obtain a timestamp.
     * @return a bytes array that represents a TimeStampResponse.
     * @throws TSAServiceInvokerException If the method fails.
     */
    private byte[ ] callRFC3161SSLService() throws TSAServiceInvokerException {
	LOGGER.info(Language.getResIntegra(ILogConstantKeys.RTSI_LOG038));
	String msgError = null;
	OutputStream dataoutputstream = null;
	OutputStream os = null;
	SSLSocket sslsocket = null;
	try {
	    // Obtenemos el puerto donde está desplegado el servicio SSL
	    String portStr = properties.getProperty(propertyHeader + TSAServiceInvokerConstants.RFC3161_SSL_PORT_NUMBER);
	    Integer portNumber = null;
	    checkSvcInvokerParams(TSAServiceInvokerConstants.RFC3161_SSL_PORT_NUMBER, portStr);
	    try {
		portNumber = Integer.valueOf(portStr);
	    } catch (NumberFormatException e) {
		throw new TSAServiceInvokerException(Language.getFormatResIntegra(ILogConstantKeys.RTSI_LOG039, new Object[ ] { portStr }), e);
	    }
	    LOGGER.info(Language.getFormatResIntegra(ILogConstantKeys.RTSI_LOG040, new Object[ ] { portNumber }));

	    // Rescatamos la ruta al almacén de confianza
	    String trustsorePath = properties.getProperty(TSAServiceInvokerConstants.TRUSTEDSTORE_PATH);
	    checkSvcInvokerParams(TSAServiceInvokerConstants.TRUSTEDSTORE_PATH, trustsorePath);
	    LOGGER.info(Language.getFormatResIntegra(ILogConstantKeys.RTSI_LOG023, new Object[ ] { trustsorePath }));

	    // Rescatamos la clave del almacén de confianza
	    String truststorePassword = properties.getProperty(TSAServiceInvokerConstants.TRUSTEDSTORE_PASSWORD);
	    checkSvcInvokerParams(TSAServiceInvokerConstants.TRUSTEDSTORE_PASSWORD, truststorePassword);

	    // Cargamos el almacén de confianza
	    msgError = Language.getResIntegra(ILogConstantKeys.RTSI_LOG024);
	    KeyStore cer = UtilsKeystore.loadKeystore(trustsorePath, truststorePassword, UtilsKeystore.JKS);

	    TrustManagerFactory tmf = TrustManagerFactory.getInstance(TRUST_MANAGER_FACTORY_SUN_X509);
	    tmf.init(cer);

	    // Rescatamos la ruta al almacén de claves al almacén de claves
	    // donde se encuentra almacenada la clave privada a usar para la
	    // autenticación SSL
	    String keystorePath = properties.getProperty(propertyHeader + TSAServiceInvokerConstants.RFC3161_SSL_KEYSTORE_PATH);
	    checkSvcInvokerParams(TSAServiceInvokerConstants.RFC3161_SSL_KEYSTORE_PATH, keystorePath);
	    LOGGER.info(Language.getFormatResIntegra(ILogConstantKeys.RTSI_LOG041, new Object[ ] { keystorePath }));

	    // Rescatamos el tipo de almacén de claves donde se encuentra
	    // almacenada la clave privada a usar para la autenticación
	    // SSL
	    String keystoreType = properties.getProperty(propertyHeader + TSAServiceInvokerConstants.RFC3161_SSL_KEYSTORE_TYPE);
	    checkSvcInvokerParams(TSAServiceInvokerConstants.RFC3161_SSL_KEYSTORE_TYPE, keystoreType);
	    // Comprobamos que el tipo de almacén de claves está soportado
	    checkKeystoreType(keystoreType, Language.getFormatResIntegra(ILogConstantKeys.RTSI_LOG042, new Object[ ] { keystoreType }));
	    LOGGER.info(Language.getFormatResIntegra(ILogConstantKeys.RTSI_LOG043, new Object[ ] { keystoreType }));

	    // Rescatamos la contraseña del almacén de claves donde se
	    // encuentra
	    // almacenada la clave privada a usar para la autenticación
	    // SSL
	    String keystorePassword = properties.getProperty(propertyHeader + TSAServiceInvokerConstants.RFC3161_SSL_KEYSTORE_PASSWORD);
	    checkSvcInvokerParams(TSAServiceInvokerConstants.RFC3161_SSL_KEYSTORE_PASSWORD, keystorePassword);

	    msgError = Language.getResIntegra(ILogConstantKeys.RTSI_LOG044);
	    KeyStore ks = UtilsKeystore.loadKeystore(keystorePath, keystorePassword, keystoreType);
	    KeyManagerFactory kmf = KeyManagerFactory.getInstance(TRUST_MANAGER_FACTORY_SUN_X509);
	    kmf.init(ks, keystorePassword.toCharArray());

	    SSLContext ctx = SSLContext.getInstance("SSL");
	    ctx.init(kmf.getKeyManagers(), tmf.getTrustManagers(), null);

	    LOGGER.info(Language.getResIntegra(ILogConstantKeys.RTSI_LOG018));
	    msgError = Language.getResIntegra(ILogConstantKeys.RTSI_LOG017);
	    SSLSocketFactory factory = ctx.getSocketFactory();
	    sslsocket = (SSLSocket) factory.createSocket(host, Integer.valueOf(portNumber));
	    sslsocket.setSoTimeout(timeOut);

	    // Generamos la petición de sello de tiempo
	    byte request[] = generateTimeStampRequest();

	    // Iniciamos el socket
	    dataoutputstream = new DataOutputStream(sslsocket.getOutputStream());
	    os = sslsocket.getOutputStream();
	    ((DataOutputStream) dataoutputstream).writeInt(request.length + 1);
	    ((DataOutputStream) dataoutputstream).writeByte(0);
	    dataoutputstream.write(request);
	    dataoutputstream.flush();
	    os.flush();
	    LOGGER.info(Language.getResIntegra(ILogConstantKeys.RTSI_LOG019));
	    // Obtenemos el TimeStampResponse
	    return getTimeStampResponse(sslsocket);
	} catch (IOException e) {
	    throw new TSAServiceInvokerException(msgError, e);
	} catch (KeyStoreException e) {
	    throw new TSAServiceInvokerException(msgError, e);
	} catch (NoSuchAlgorithmException e) {
	    throw new TSAServiceInvokerException(msgError, e);
	} catch (CertificateException e) {
	    throw new TSAServiceInvokerException(msgError, e);
	} catch (UnrecoverableKeyException e) {
	    throw new TSAServiceInvokerException(msgError, e);
	} catch (KeyManagementException e) {
	    throw new TSAServiceInvokerException(msgError, e);
	} finally {
	    // Cerramos recursos
	    UtilsResources.safeCloseSocket(sslsocket);
	    UtilsResources.safeCloseOutputStream(os);
	    UtilsResources.safeCloseOutputStream(dataoutputstream);
	}
    }

    /**
     * Method that verifies if a value is not empty and not null.
     * @param parameterName Parameter that represents the name of the element to check.
     * @param parameterValue Parameter that represents the value to check.
     * @throws TSAServiceInvokerException If the value is empty or null.
     */
    private void checkSvcInvokerParams(String parameterName, String parameterValue) throws TSAServiceInvokerException {
	if (!GenericUtils.assertStringValue(parameterValue)) {
	    throw new TSAServiceInvokerException(Language.getFormatResIntegra(ILogConstantKeys.RTSI_LOG045, new Object[ ] { parameterName, application }));
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
     * Method that builds the timestamp request.
     * @return a bytes array that represents the timestamp request.
     * @throws TSAServiceInvokerException If the method fails.
     */
    private byte[ ] generateTimeStampRequest() throws TSAServiceInvokerException {
	String msgError = null;
	LOGGER.info(Language.getResIntegra(ILogConstantKeys.RTSI_LOG046));
	try {
	    // Iniciamos la petición de sello de tiempo
	    TimeStampRequestGenerator reqgen = new TimeStampRequestGenerator();
	    reqgen.setCertReq(true);

	    // Añadimos la extensión para el identificador de aplicación
	    msgError = Language.getResIntegra(ILogConstantKeys.RTSI_LOG047);
	    reqgen.addExtension(new ASN1ObjectIdentifier(applicationOID), false, new DEROctetString(application.getBytes()));

	    // Añadimos la extensión para el OID de la política de sello de
	    // tiempo
	    reqgen.setReqPolicy(policyOID);

	    // Calculamos el resumen de los datos a sellar
	    digest.update(dataToStamp, 0, dataToStamp.length);
	    byte[ ] digestValue = new byte[digest.getDigestSize()];
	    digest.doFinal(digestValue, 0);

	    // Generamos la petición de sello de tiempo
	    TimeStampRequest req = reqgen.generate(digestAlgorithm, digestValue);
	    msgError = Language.getResIntegra(ILogConstantKeys.RTSI_LOG048);
	    LOGGER.info(Language.getResIntegra(ILogConstantKeys.RTSI_LOG049));
	    return req.getEncoded();
	} catch (IOException e) {
	    throw new TSAServiceInvokerException(msgError, e);
	}
    }

    /**
     * <p>Private class that allows to verify the host of the TS@ RFC 3161 - HTTPS service.</p>
     * <b>Project:</b><p>@Firma and TS@ Web Services Integration Platform.</p>
     * @version 1.0, 23/01/2014.
     */
    private class NameVerifier implements HostnameVerifier {

	/**
	 * {@inheritDoc}
	 * @see javax.net.ssl.HostnameVerifier#verify(java.lang.String, javax.net.ssl.SSLSession)
	 */
	public boolean verify(String hostname, SSLSession session) {
	    return true;
	}
    }
}

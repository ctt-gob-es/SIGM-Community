/**
 * LICENCIA LGPL:
 * 
 * Esta librería es Software Libre; Usted puede redistribuirla y/o modificarla
 * bajo los términos de la GNU Lesser General Public License (LGPL) tal y como 
 * ha sido publicada por la Free Software Foundation; o bien la versión 2.1 de 
 * la Licencia, o (a su elección) cualquier versión posterior.
 * 
 * Esta librería se distribuye con la esperanza de que sea útil, pero SIN 
 * NINGUNA GARANTÍA; tampoco las implícitas garantías de MERCANTILIDAD o 
 * ADECUACIÓN A UN PROPÓSITO PARTICULAR. Consulte la GNU Lesser General Public 
 * License (LGPL) para más detalles
 * 
 * Usted debe recibir una copia de la GNU Lesser General Public License (LGPL) 
 * junto con esta librería; si no es así, escriba a la Free Software Foundation 
 * Inc. 51 Franklin Street, 5º Piso, Boston, MA 02110-1301, USA o consulte
 * <http://www.gnu.org/licenses/>.
 *
 * Copyright 2011 Agencia de Tecnología y Certificación Electrónica
 */
package es.accv.arangi.base.timestamp;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.ASN1Encoding;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.cmp.PKIFailureInfo;
import org.bouncycastle.asn1.cmp.PKIFreeText;
import org.bouncycastle.asn1.cmp.PKIStatus;
import org.bouncycastle.asn1.cmp.PKIStatusInfo;
import org.bouncycastle.asn1.tsp.TimeStampResp;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignatureAlgorithmNameGenerator;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.DefaultCMSSignatureAlgorithmNameGenerator;
import org.bouncycastle.cms.SignerInformationVerifier;
import org.bouncycastle.operator.DefaultDigestAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DefaultSignatureAlgorithmIdentifierFinder;
import org.bouncycastle.operator.DigestCalculatorProvider;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.operator.SignatureAlgorithmIdentifierFinder;
import org.bouncycastle.operator.bc.BcDigestCalculatorProvider;
import org.bouncycastle.operator.bc.BcRSAContentVerifierProviderBuilder;
import org.bouncycastle.tsp.TSPException;
import org.bouncycastle.tsp.TSPValidationException;
import org.bouncycastle.tsp.TimeStampRequest;
import org.bouncycastle.tsp.TimeStampRequestGenerator;
import org.bouncycastle.tsp.TimeStampResponse;
import org.bouncycastle.tsp.TimeStampToken;
import org.bouncycastle.util.Store;

import es.accv.arangi.base.ArangiObject;
import es.accv.arangi.base.algorithm.HashingAlgorithm;
import es.accv.arangi.base.certificate.Certificate;
import es.accv.arangi.base.document.ByteArrayDocument;
import es.accv.arangi.base.document.Document;
import es.accv.arangi.base.document.FileDocument;
import es.accv.arangi.base.document.InputStreamDocument;
import es.accv.arangi.base.document.URLDocument;
import es.accv.arangi.base.exception.certificate.NormalizeCertificateException;
import es.accv.arangi.base.exception.document.HashingException;
import es.accv.arangi.base.exception.document.InitDocumentException;
import es.accv.arangi.base.exception.timestamp.MalformedTimeStampException;
import es.accv.arangi.base.exception.timestamp.ResponseTimeStampException;
import es.accv.arangi.base.exception.timestamp.TimeStampServerConnectionException;
import es.accv.arangi.base.util.Util;

/**
 * Clase para trabajar con sellos de tiempo según la 
 * <a href="http://tools.ietf.org/rfc/rfc3161.txt" target="rfc">RFC-3161</a>.<br><br>
 * 
 * Si lo único que se quiere es obtener la hora actual es mejor utilizar la clase
 * {@link es.accv.arangi.base.util.time.Time Time}.<br><br>
 * 
 * NOTA: En la clase se utilizan indistintamente los términos <i>servidor de sello de 
 * tiempos</i> y <i>TSA - Time Stamp Authority</i><br><br>
 * 
 * Un ejemplo de uso sería: <br><br>
 * 
 * <code>
 * 	byte[] dataToStamp = "data to stamp".getBytes();<br>
 * 	TimeStamp timeStamp = TimeStamp.stampDocument (dataToStamp, new URL ("http://server/tsa"));<br><br>
 * 
 * 	//guardar sello<br>
 * 	Util.saveFile(new File ("/sellos/sello.ts"), timeStamp.toDER());<br><br>
 * 
 * 	//cargar el sello<br>
 *  TimeStamp timeStamp2 = new TimeStamp (new File ("/sellos/sello.ts"));
 * </code><br><br>
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José M Gutiérrez</a>
 */
public class TimeStamp extends ArangiObject implements Comparable<TimeStamp>{

	/*
	 * Logger de la clase
	 */
	static Logger logger = Logger.getLogger(TimeStamp.class);
	
	/*
	 * Sello de tiempo 
	 */
	private TimeStampToken timeStamp;
	
	//-- Constructores
	
	/**
	 * Constructor en base a un objeto sello de tiempos de Bouncy Castle
	 * 
	 * @param timeStamp Sello de tiempos de Bouncy Castle
	 */
	public TimeStamp (TimeStampToken timeStamp) {
		logger.debug("[TimeStamp(File)]::Entrada::" + timeStamp);
		
		this.timeStamp = timeStamp;
	}
	
	/**
	 * Constructor en base a un fichero que contiene un sello de tiempo.
	 * 
	 * @param fileTimeStamp Fichero que contiene un objeto sello de tiempo
	 * @throws MalformedTimeStampException El objeto contenido en el stream de lectura no parece
	 * 	ser un sello de tiempo
	 * @throws FileNotFoundException El fichero no existe
	 */
	public TimeStamp (File fileTimeStamp) throws MalformedTimeStampException, FileNotFoundException {
		logger.debug("[TimeStamp(File)]::Entrada::" + fileTimeStamp);
		
		FileInputStream fis = null;
		try {
			//-- Obtener stream de lectura al fichero
			fis = new FileInputStream (fileTimeStamp);
			
			//-- Inicializar objeto
			initialize (fis);
		} catch (FileNotFoundException e) {
			logger.info("[TimeStamp(File)]::No se ha podido encontrar el fichero " + fileTimeStamp.getAbsolutePath(), e);
			throw e;
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					logger.info("[TimeStamp(File)]::No se ha podido cerrar el stream de lectura al fichero " + fileTimeStamp.getAbsolutePath());
				}
			}
		}
	}
	
	/**
	 * Constructor en base a un stream de lectura que contiene el sello de tiempo.
	 * 
	 * @param isTimeStamp Stream de lectura a un objeto sello de tiempo
	 * @throws MalformedTimeStampException El objeto contenido en el stream de lectura no parece
	 * 	ser un sello de tiempo
	 */
	public TimeStamp (InputStream isTimeStamp) throws MalformedTimeStampException {
		logger.debug("[TimeStamp(InputStream)]::Entrada::" + isTimeStamp);
		
		//-- Inicializar objeto
		initialize (isTimeStamp);
	}
	
	/**
	 * Constructor en base a un array de bytes que contiene un sello de tiempo.
	 * 
	 * @param bytesTimeStamp Contenido de un objeto sello de tiempo
	 * @throws MalformedTimeStampException El objeto contenido en el stream de lectura no parece
	 * 	ser un sello de tiempo
	 */
	public TimeStamp (byte[] bytesTimeStamp) throws MalformedTimeStampException {
		logger.debug("[TimeStamp(byte[])]::Entrada::" + bytesTimeStamp);
		
		ByteArrayInputStream bais = null;
		try {
			//-- Obtener stream de lectura 
			bais = new ByteArrayInputStream (bytesTimeStamp);
			
			//-- Inicializar objeto
			initialize (bais);
		} finally {
			if (bais != null) {
				try {
					bais.close();
				} catch (IOException e) {
					logger.info("[TimeStamp(byte[])]::No se ha podido cerrar el stream de lectura");
				}
			}
		}
	}
	
	//-- Métodos públicos
	
	/**
	 * Devuelve la fecha y la hora en la que se estampó el sello de tiempo.
	 * 
	 */
	public Date getTime () {
		logger.debug("[TimeStamp.getTime]::Entrada");
		
		return timeStamp.getTimeStampInfo().getGenTime();
	}
	
	/**
	 * Devuelve el nonce o null si no existe 
	 */
	public BigInteger getNonce () {
		logger.debug("[TimeStamp.getNonce]::Entrada");
		
		return timeStamp.getTimeStampInfo().getNonce();
	}
	
	/**
	 * Obtiene el sello en formato DER
	 * 
	 * @return Array de bytes con el sello de tiempos
	 */
	public byte[] toDER () {
		logger.debug("[TimeStamp.toDER]::Entrada");
		
		try {
			return timeStamp.getEncoded();
		} catch (IOException e) {
			// No se dará
			return null;
		}
	}
	
	/**
	 * Obtiene el hash que fue sellado por la TSA
	 * 
	 * @return Hash sellado por la TSA
	 */
	public byte[] getHash () {
		logger.debug("[TimeStamp.getDataStamped]::Entrada");
		
		return timeStamp.getTimeStampInfo().getMessageImprintDigest();
	}
	
	/**
	 * Obtiene el OID del algoritmo de hashing con el que fue obtenido el hash que 
	 * selló la TSA
	 * 
	 * @return OID del algoritmo de hashing con el que se creó el hash sellado por la TSA
	 */
	public String getHashAlgorithm () {
		logger.debug("[TimeStamp.getHashAlgorithm]::Entrada");
		
		return timeStamp.getTimeStampInfo().getMessageImprintAlgOID().getId();
	}
	
	/**
	 * Obtiene el algoritmo de hashing con el que fue obtenido el hash que 
	 * selló la TSA
	 * 
	 * @return Nombre del algoritmo de hashing con el que se creó el hash sellado por la TSA
	 * @throws NoSuchAlgorithmException El algoritmo no existe en Arangí
	 */
	public String getHashAlgorithmName () throws NoSuchAlgorithmException {
		logger.debug("[TimeStamp.getHashAlgorithmName]::Entrada");
		
		return HashingAlgorithm.getAlgorithmName(timeStamp.getTimeStampInfo().getMessageImprintAlgOID().getId());
	}
	
	/**
	 * Obtiene el OID de la política con la que se realizó el firmado
	 * 
	 * @return OID de la política
	 */
	public String getPolicyOID () {
		logger.debug("[TimeStamp.getPolicyOID]::Entrada");
		
		return timeStamp.getTimeStampInfo().getPolicy().getId();
	}
	
	/**
	 * Obtiene el campo TSA
	 * 
	 * @return TSA
	 */
	public String getTSA () {
		logger.debug("[TimeStamp.getTSA]::Entrada");
		if (timeStamp.getTimeStampInfo() == null || timeStamp.getTimeStampInfo().getTsa() == null) {
			return null;
		}
		
		return timeStamp.getTimeStampInfo().getTsa().toString();
	}
	
	/**
	 * Método que obtiene el certificado de la TSA: el certificado con el que está
	 * firmado el sello de tiempos.
	 * 
	 * @return Certificado de la TSA
	 * @throws MalformedTimeStampException El sello de tiempos no está bien formado y no es posible
	 * 	obtener el certificado de la TSA
	 */
	public Certificate getSignatureCertificate () throws MalformedTimeStampException {
		logger.debug("[TimeStamp.getSignatureCertificate]::Entrada");
		
		Store certs = timeStamp.getCertificates();

		Iterator<X509CertificateHolder> iterator1 = certs.getMatches(timeStamp.getSID()).iterator();
		Iterator<X509CertificateHolder> iterator2 = certs.getMatches(timeStamp.getSID()).iterator();
		
		List<String> lIssuers = new ArrayList<String>();
		
		while (iterator1.hasNext()) {
			try {
				Certificate certificate = new Certificate (iterator1.next());
				lIssuers.add(certificate.getIssuerDN());
			} catch (NormalizeCertificateException e) {
	    		logger.info("[TimeStamp.getSignatureCertificate]::El certificado contenido en el sello de tiempos no puede " +
	    				"ser normalizado de acuerdo al proveedor criptográfico de Arangi", e);
			}
		}
		
		while (iterator2.hasNext()) {
			try {
				Certificate certificate = new Certificate (iterator2.next());
				if (!lIssuers.contains(certificate.getSubjectDN())) {
					logger.debug("[TimeStamp.getSignatureCertificate]::Encontrado certificado de sello: " + certificate.getCommonName());
					return certificate;
				}
			} catch (NormalizeCertificateException e) {
	    		logger.info("[TimeStamp.getSignatureCertificate]::El certificado contenido en el sello de tiempos no puede " +
	    				"ser normalizado de acuerdo al proveedor criptográfico de Arangi", e);
			}
		}
		
		throw new MalformedTimeStampException ("No se ha encontrado un certificado normalizable de sello de" +
				" tiempos en el sello de tiempos");

	}
	
	/**
	 * Método que comprueba que el sello de tiempos sea correcto, lo que implica
	 * las siguientes validaciones:
	 * <ul>
	 * 	<li>El certificado de la firma está correctamente definido dentro de la 
	 * 	firma del sello.</li>
	 * 	<li>El certificado de la firma se encontraba dentro de su periodo de validez 
	 * 	en el momento en que se realizó el sello.</li>
	 * 	<li>El certificado de la firma posee el uso extendido de clave necesario 
	 * 	para ser un certificado de TSA: KeyPurposeId.id_kp_timeStamping.</li>
	 * 	<li>La firma fue creada efectivamente con el certificado indicado</li>
	 * 	<li>La firma es un CMS bien formado</li>
	 * 	<li>La firma es correcta</li>
	 * </ul>
	 * 
	 * @return Cierto si la firma del sello de tiempos es correcta
	 * @throws MalformedTimeStampException Error realizando las comprobaciones
	 */
	public boolean isValid () throws MalformedTimeStampException {
		logger.debug("[TimeStamp.isValid]::Entrada::" + timeStamp);
		
		//-- Obtener el certificado de firma
		Certificate certificate = getSignatureCertificate();
		logger.debug("Certificado timestamp: " + certificate);
		
		//-- Validar firma
		try {
			X509CertificateHolder certHolder = new X509CertificateHolder(certificate.toDER());
			BcRSAContentVerifierProviderBuilder qaz = new BcRSAContentVerifierProviderBuilder(new DefaultDigestAlgorithmIdentifierFinder()); 
			CMSSignatureAlgorithmNameGenerator sang = new DefaultCMSSignatureAlgorithmNameGenerator(); 
			SignatureAlgorithmIdentifierFinder saif = new DefaultSignatureAlgorithmIdentifierFinder();
			DigestCalculatorProvider dcp = new BcDigestCalculatorProvider();
			SignerInformationVerifier siv = new SignerInformationVerifier(sang, saif, qaz.build(certHolder), dcp);
			timeStamp.validate(siv);
		} catch (TSPValidationException e) {
    		logger.debug("[TimeStamp.isValid]::La firma no es válida", e);
			return false;
		} catch (TSPException e) {
    		logger.debug("[TimeStamp.isValid]::El CMS de la firma no es correcto", e);
			return false;
		} catch (NormalizeCertificateException e) {
    		logger.debug("[TimeStamp.isValid]::No es posible normalizar el certificado de firma de la TSA", e);
			return false;
		} catch (IOException e) {
    		logger.debug("[TimeStamp.isValid]::No es posible obtener el objeto de Bouncy para tratar el certificado", e);
			return false;
		} catch (OperatorCreationException e) {
    		logger.debug("[TimeStamp.isValid]::No es posible obtener el validador del certificado de firma de la TSA", e);
			return false;
		}
		
		//-- Si no hay excepciones es que todo va bien
		return true;
	}
	
	/**
	 * Método estático que comprueba que el sello de tiempos pasado como parámetro
	 * sea correcto, lo que implica las siguientes validaciones:
	 * <ul>
	 * 	<li>El certificado de la firma está correctamente definido dentro de la 
	 * 	firma del sello.</li>
	 * 	<li>El certificado de la firma se encontraba dentro de su periodo de validez 
	 * 	en el momento en que se realizó el sello.</li>
	 * 	<li>El certificado de la firma posee el uso extendido de clave necesario 
	 * 	para ser un certificado de TSA: KeyPurposeId.id_kp_timeStamping.</li>
	 * 	<li>La firma fue creada efectivamente con el certificado indicado</li>
	 * 	<li>La firma es un CMS bien formado</li>
	 * 	<li>La firma es correcta</li>
	 * </ul>
	 * 
	 * @return Cierto si la firma del sello de tiempos es correcta
	 * @throws MalformedTimeStampException Error realizando las comprobaciones
	 */
	public static boolean validate (TimeStamp timeStamp) throws MalformedTimeStampException {
		return timeStamp.isValid ();
	}
	
	//-- Implementación de Comparable
	
	public int compareTo(TimeStamp ts) {
		return getTime().compareTo(ts.getTime());
	}
	
	//-- Métodos estáticos
	
	/**
	 * Método que obtiene un sello de tiempo de un servidor de sello de tiempos. 
	 * 
	 * @param file Fichero con el documento cuyo contenido se desea sellar
	 * @param serverTimeStampURL URL del servidor de sello de tiempos
	 * @param parameters Parámetros de la llamada al sello de tiempos
	 * @throws FileNotFoundException El fichero no existe
	 * @throws TimeStampServerConnectionException Errores en la conexión con el servidor de sello
	 * 	de tiempos
	 * @throws MalformedTimeStampException El objeto devuelto por el servidor no parece ser un 
	 * 	sello de tiempos
	 * @throws ResponseTimeStampException La TSA ha devuelto una respuesta con un error
	 * @throws HashingException Error obteniendo el hash
	 * @throws InitDocumentException El fichero es nulo o no existe
	 */
	public static TimeStamp stampDocument (File file, URL serverTimeStampURL, TimeStampRequestParameters parameters) throws HashingException, TimeStampServerConnectionException, MalformedTimeStampException, ResponseTimeStampException, InitDocumentException {
		FileDocument document = new FileDocument(file);
		try {
			return stampHash (document.getHash(), serverTimeStampURL, HashingAlgorithm.getDefault(), parameters);
		} catch (NoSuchAlgorithmException e) {
			// No se va a dar porque el algoritmo de hashing por defecto si existe
			return null;
		}
	}

	/**
	 * Método que obtiene un sello de tiempo de un servidor de sello de tiempos. 
	 * 
	 * @param file Fichero con el documento cuyo contenido se desea sellar
	 * @param serverTimeStampURL URL del servidor de sello de tiempos
	 * @throws FileNotFoundException El fichero no existe
	 * @throws TimeStampServerConnectionException Errores en la conexión con el servidor de sello
	 * 	de tiempos
	 * @throws MalformedTimeStampException El objeto devuelto por el servidor no parece ser un 
	 * 	sello de tiempos
	 * @throws ResponseTimeStampException La TSA ha devuelto una respuesta con un error
	 * @throws HashingException Error obteniendo el hash
	 * @throws InitDocumentException El fichero es nulo o no existe
	 */
	public static TimeStamp stampDocument (File file, URL serverTimeStampURL) throws HashingException, TimeStampServerConnectionException, MalformedTimeStampException, ResponseTimeStampException, InitDocumentException {
		FileDocument document = new FileDocument(file);
		return TimeStamp.stampHash(document.getHash(), serverTimeStampURL);
	}

	/**
	 * Método que obtiene un sello de tiempo de un servidor de sello de tiempos. 
	 * 
	 * @param bDocument Contenido del documento que se desea sellar
	 * @param serverTimeStampURL URL del servidor de sello de tiempos
	 * @param parameters Parámetros de la llamada al sello de tiempos
	 * @throws FileNotFoundException El fichero no existe
	 * @throws TimeStampServerConnectionException Errores en la conexión con el servidor de sello
	 * 	de tiempos
	 * @throws MalformedTimeStampException El objeto devuelto por el servidor no parece ser un 
	 * 	sello de tiempos
	 * @throws ResponseTimeStampException La TSA ha devuelto una respuesta con un error
	 * @throws HashingException Error obteniendo el hash
	 */
	public static TimeStamp stampDocument (byte[] bDocument, URL serverTimeStampURL, TimeStampRequestParameters parameters) throws HashingException, TimeStampServerConnectionException, MalformedTimeStampException, ResponseTimeStampException {
		ByteArrayDocument document = new ByteArrayDocument(bDocument);
		try {
			return stampHash (document.getHash(), serverTimeStampURL, HashingAlgorithm.getDefault(), parameters);
		} catch (NoSuchAlgorithmException e) {
			// No se va a dar porque el algoritmo de hashing por defecto si existe
			return null;
		}
	}

	/**
	 * Método que obtiene un sello de tiempo de un servidor de sello de tiempos. 
	 * 
	 * @param bDocument Contenido del documento que se desea sellar
	 * @param serverTimeStampURL URL del servidor de sello de tiempos
	 * @throws FileNotFoundException El fichero no existe
	 * @throws TimeStampServerConnectionException Errores en la conexión con el servidor de sello
	 * 	de tiempos
	 * @throws MalformedTimeStampException El objeto devuelto por el servidor no parece ser un 
	 * 	sello de tiempos
	 * @throws ResponseTimeStampException La TSA ha devuelto una respuesta con un error
	 * @throws HashingException Error obteniendo el hash
	 */
	public static TimeStamp stampDocument (byte[] bDocument, URL serverTimeStampURL) throws HashingException, TimeStampServerConnectionException, MalformedTimeStampException, ResponseTimeStampException {
		ByteArrayDocument document = new ByteArrayDocument(bDocument);
		return TimeStamp.stampHash(document.getHash(), serverTimeStampURL);
	}

	/**
	 * Método que obtiene un sello de tiempo de un servidor de sello de tiempos. 
	 * 
	 * @param stream Stream de lectura al contenido del documento que se desea sellar
	 * @param serverTimeStampURL URL del servidor de sello de tiempos
	 * @param parameters Parámetros de la llamada al sello de tiempos
	 * @throws FileNotFoundException El fichero no existe
	 * @throws TimeStampServerConnectionException Errores en la conexión con el servidor de sello
	 * 	de tiempos
	 * @throws MalformedTimeStampException El objeto devuelto por el servidor no parece ser un 
	 * 	sello de tiempos
	 * @throws ResponseTimeStampException La TSA ha devuelto una respuesta con un error
	 * @throws HashingException Error obteniendo el hash
	 */
	public static TimeStamp stampDocument (InputStream stream, URL serverTimeStampURL, TimeStampRequestParameters parameters) throws HashingException, TimeStampServerConnectionException, MalformedTimeStampException, ResponseTimeStampException {
		InputStreamDocument document = new InputStreamDocument(stream);
		try {
			return stampHash (document.getHash(), serverTimeStampURL, HashingAlgorithm.getDefault(), parameters);
		} catch (NoSuchAlgorithmException e) {
			// No se va a dar porque el algoritmo de hashing por defecto si existe
			return null;
		}
	}

	/**
	 * Método que obtiene un sello de tiempo de un servidor de sello de tiempos. 
	 * 
	 * @param stream Stream de lectura al contenido del documento que se desea sellar
	 * @param serverTimeStampURL URL del servidor de sello de tiempos
	 * @throws FileNotFoundException El fichero no existe
	 * @throws TimeStampServerConnectionException Errores en la conexión con el servidor de sello
	 * 	de tiempos
	 * @throws MalformedTimeStampException El objeto devuelto por el servidor no parece ser un 
	 * 	sello de tiempos
	 * @throws ResponseTimeStampException La TSA ha devuelto una respuesta con un error
	 * @throws HashingException Error obteniendo el hash
	 */
	public static TimeStamp stampDocument (InputStream stream, URL serverTimeStampURL) throws HashingException, TimeStampServerConnectionException, MalformedTimeStampException, ResponseTimeStampException {
		InputStreamDocument document = new InputStreamDocument(stream);
		return TimeStamp.stampHash(document.getHash(), serverTimeStampURL);
	}

	/**
	 * Método que obtiene un sello de tiempo de un servidor de sello de tiempos. 
	 * 
	 * @param urlDocument URL del documento cuyo contenido se desea sellar
	 * @param serverTimeStampURL URL del servidor de sello de tiempos
	 * @param parameters Parámetros de la llamada al sello de tiempos
	 * @throws FileNotFoundException El fichero no existe
	 * @throws TimeStampServerConnectionException Errores en la conexión con el servidor de sello
	 * 	de tiempos
	 * @throws MalformedTimeStampException El objeto devuelto por el servidor no parece ser un 
	 * 	sello de tiempos
	 * @throws ResponseTimeStampException La TSA ha devuelto una respuesta con un error
	 * @throws HashingException Error obteniendo el hash
	 * @throws InitDocumentException No se puede obtener el documento en la URL
	 */
	public static TimeStamp stampDocument (URL urlDocument, URL serverTimeStampURL, TimeStampRequestParameters parameters) throws HashingException, TimeStampServerConnectionException, MalformedTimeStampException, ResponseTimeStampException, InitDocumentException {
		URLDocument document = new URLDocument(urlDocument);
		try {
			return stampHash (document.getHash(), serverTimeStampURL, HashingAlgorithm.getDefault(), parameters);
		} catch (NoSuchAlgorithmException e) {
			// No se va a dar porque el algoritmo de hashing por defecto si existe
			return null;
		}
	}

	/**
	 * Método que obtiene un sello de tiempo de un servidor de sello de tiempos. 
	 * 
	 * @param urlDocument URL del documento cuyo contenido se desea sellar
	 * @param serverTimeStampURL URL del servidor de sello de tiempos
	 * @throws FileNotFoundException El fichero no existe
	 * @throws TimeStampServerConnectionException Errores en la conexión con el servidor de sello
	 * 	de tiempos
	 * @throws MalformedTimeStampException El objeto devuelto por el servidor no parece ser un 
	 * 	sello de tiempos
	 * @throws ResponseTimeStampException La TSA ha devuelto una respuesta con un error
	 * @throws HashingException Error obteniendo el hash
	 * @throws InitDocumentException No se puede obtener el documento en la URL
	 */
	public static TimeStamp stampDocument (URL urlDocument, URL serverTimeStampURL) throws HashingException, TimeStampServerConnectionException, MalformedTimeStampException, ResponseTimeStampException, InitDocumentException {
		URLDocument document = new URLDocument(urlDocument);
		return TimeStamp.stampHash(document.getHash(), serverTimeStampURL);
	}

	/**
	 * Método que obtiene un sello de tiempo de un servidor de sello de tiempos. 
	 * 
	 * @param document Documento cuyo contenido se desea sellar
	 * @param serverTimeStampURL URL del servidor de sello de tiempos
	 * @param parameters Parámetros de la llamada al sello de tiempos
	 * @throws FileNotFoundException El fichero no existe
	 * @throws TimeStampServerConnectionException Errores en la conexión con el servidor de sello
	 * 	de tiempos
	 * @throws MalformedTimeStampException El objeto devuelto por el servidor no parece ser un 
	 * 	sello de tiempos
	 * @throws ResponseTimeStampException La TSA ha devuelto una respuesta con un error
	 * @throws HashingException Error obteniendo el hash
	 */
	public static TimeStamp stampDocument (Document document, URL serverTimeStampURL, TimeStampRequestParameters parameters) throws HashingException, TimeStampServerConnectionException, MalformedTimeStampException, ResponseTimeStampException {
		try {
			return stampHash (document.getHash(), serverTimeStampURL, HashingAlgorithm.getDefault(), parameters);
		} catch (NoSuchAlgorithmException e) {
			// No se va a dar porque el algoritmo de hashing por defecto si existe
			return null;
		}
	}

	/**
	 * Método que obtiene un sello de tiempo de un servidor de sello de tiempos. 
	 * 
	 * @param document Documento cuyo contenido se desea sellar
	 * @param serverTimeStampURL URL del servidor de sello de tiempos
	 * @throws FileNotFoundException El fichero no existe
	 * @throws TimeStampServerConnectionException Errores en la conexión con el servidor de sello
	 * 	de tiempos
	 * @throws MalformedTimeStampException El objeto devuelto por el servidor no parece ser un 
	 * 	sello de tiempos
	 * @throws ResponseTimeStampException La TSA ha devuelto una respuesta con un error
	 * @throws HashingException Error obteniendo el hash
	 */
	public static TimeStamp stampDocument (Document document, URL serverTimeStampURL) throws HashingException, TimeStampServerConnectionException, MalformedTimeStampException, ResponseTimeStampException {
		return TimeStamp.stampHash(document.getHash(), serverTimeStampURL);
	}

	/**
	 * Método que obtiene un sello de tiempo de un servidor de sello de tiempos. 
	 * 
	 * @param file Fichero con el documento cuyo contenido se desea sellar
	 * @param serverTimeStampURL URL del servidor de sello de tiempos
	 * @param hashingAlgorithm Algoritmo de hashing
	 * @param parameters Parámetros de la llamada al sello de tiempos
	 * @throws FileNotFoundException El fichero no existe
	 * @throws TimeStampServerConnectionException Errores en la conexión con el servidor de sello
	 * 	de tiempos
	 * @throws MalformedTimeStampException El objeto devuelto por el servidor no parece ser un 
	 * 	sello de tiempos
	 * @throws ResponseTimeStampException La TSA ha devuelto una respuesta con un error
	 * @throws HashingException Error obteniendo el hash
	 * @throws InitDocumentException El fichero es nulo o no existe
	 * @throws NoSuchAlgorithmException No existe el algoritmo de hashing
	 */
	public static TimeStamp stampDocument (File file, URL serverTimeStampURL, String hashingAlgorithm, TimeStampRequestParameters parameters) throws HashingException, TimeStampServerConnectionException, MalformedTimeStampException, ResponseTimeStampException, InitDocumentException, NoSuchAlgorithmException {
		FileDocument document = new FileDocument(file);
		return TimeStamp.stampHash(document.getHash(hashingAlgorithm), serverTimeStampURL, hashingAlgorithm, parameters);
	}

	/**
	 * Método que obtiene un sello de tiempo de un servidor de sello de tiempos. 
	 * 
	 * @param file Fichero con el documento cuyo contenido se desea sellar
	 * @param serverTimeStampURL URL del servidor de sello de tiempos
	 * @param hashingAlgorithm Algoritmo de hashing
	 * @throws FileNotFoundException El fichero no existe
	 * @throws TimeStampServerConnectionException Errores en la conexión con el servidor de sello
	 * 	de tiempos
	 * @throws MalformedTimeStampException El objeto devuelto por el servidor no parece ser un 
	 * 	sello de tiempos
	 * @throws ResponseTimeStampException La TSA ha devuelto una respuesta con un error
	 * @throws HashingException Error obteniendo el hash
	 * @throws InitDocumentException El fichero es nulo o no existe
	 */
	public static TimeStamp stampDocument (File file, URL serverTimeStampURL, String hashingAlgorithm) throws HashingException, TimeStampServerConnectionException, MalformedTimeStampException, ResponseTimeStampException, InitDocumentException {
		FileDocument document = new FileDocument(file);
		return TimeStamp.stampHash(document.getHash(hashingAlgorithm), serverTimeStampURL);
	}

	/**
	 * Método que obtiene un sello de tiempo de un servidor de sello de tiempos. 
	 * 
	 * @param bDocument Contenido del documento que se desea sellar
	 * @param serverTimeStampURL URL del servidor de sello de tiempos
	 * @param hashingAlgorithm Algoritmo de hashing
	 * @param parameters Parámetros de la llamada al sello de tiempos
	 * @throws FileNotFoundException El fichero no existe
	 * @throws TimeStampServerConnectionException Errores en la conexión con el servidor de sello
	 * 	de tiempos
	 * @throws MalformedTimeStampException El objeto devuelto por el servidor no parece ser un 
	 * 	sello de tiempos
	 * @throws ResponseTimeStampException La TSA ha devuelto una respuesta con un error
	 * @throws HashingException Error obteniendo el hash
	 * @throws NoSuchAlgorithmException No existe el algoritmo de hashing
	 */
	public static TimeStamp stampDocument (byte[] bDocument, URL serverTimeStampURL, String hashingAlgorithm, TimeStampRequestParameters parameters) throws HashingException, TimeStampServerConnectionException, MalformedTimeStampException, ResponseTimeStampException, NoSuchAlgorithmException {
		ByteArrayDocument document = new ByteArrayDocument(bDocument);
		return TimeStamp.stampHash(document.getHash(hashingAlgorithm), serverTimeStampURL, hashingAlgorithm, parameters);
	}

	/**
	 * Método que obtiene un sello de tiempo de un servidor de sello de tiempos. 
	 * 
	 * @param bDocument Contenido del documento que se desea sellar
	 * @param serverTimeStampURL URL del servidor de sello de tiempos
	 * @param hashingAlgorithm Algoritmo de hashing
	 * @throws FileNotFoundException El fichero no existe
	 * @throws TimeStampServerConnectionException Errores en la conexión con el servidor de sello
	 * 	de tiempos
	 * @throws MalformedTimeStampException El objeto devuelto por el servidor no parece ser un 
	 * 	sello de tiempos
	 * @throws ResponseTimeStampException La TSA ha devuelto una respuesta con un error
	 * @throws HashingException Error obteniendo el hash
	 */
	public static TimeStamp stampDocument (byte[] bDocument, URL serverTimeStampURL, String hashingAlgorithm) throws HashingException, TimeStampServerConnectionException, MalformedTimeStampException, ResponseTimeStampException {
		ByteArrayDocument document = new ByteArrayDocument(bDocument);
		return TimeStamp.stampHash(document.getHash(hashingAlgorithm), serverTimeStampURL);
	}

	/**
	 * Método que obtiene un sello de tiempo de un servidor de sello de tiempos. 
	 * 
	 * @param stream Stream de lectura al contenido del documento que se desea sellar
	 * @param serverTimeStampURL URL del servidor de sello de tiempos
	 * @param hashingAlgorithm Algoritmo de hashing
	 * @param parameters Parámetros de la llamada al sello de tiempos
	 * @throws FileNotFoundException El fichero no existe
	 * @throws TimeStampServerConnectionException Errores en la conexión con el servidor de sello
	 * 	de tiempos
	 * @throws MalformedTimeStampException El objeto devuelto por el servidor no parece ser un 
	 * 	sello de tiempos
	 * @throws ResponseTimeStampException La TSA ha devuelto una respuesta con un error
	 * @throws HashingException Error obteniendo el hash
	 * @throws NoSuchAlgorithmException No existe el algoritmo de hashing
	 */
	public static TimeStamp stampDocument (InputStream stream, URL serverTimeStampURL, String hashingAlgorithm, TimeStampRequestParameters parameters) throws HashingException, TimeStampServerConnectionException, MalformedTimeStampException, ResponseTimeStampException, NoSuchAlgorithmException {
		InputStreamDocument document = new InputStreamDocument(stream);
		return TimeStamp.stampHash(document.getHash(hashingAlgorithm), serverTimeStampURL, hashingAlgorithm, parameters);
	}

	/**
	 * Método que obtiene un sello de tiempo de un servidor de sello de tiempos. 
	 * 
	 * @param stream Stream de lectura al contenido del documento que se desea sellar
	 * @param serverTimeStampURL URL del servidor de sello de tiempos
	 * @param hashingAlgorithm Algoritmo de hashing
	 * @throws FileNotFoundException El fichero no existe
	 * @throws TimeStampServerConnectionException Errores en la conexión con el servidor de sello
	 * 	de tiempos
	 * @throws MalformedTimeStampException El objeto devuelto por el servidor no parece ser un 
	 * 	sello de tiempos
	 * @throws ResponseTimeStampException La TSA ha devuelto una respuesta con un error
	 * @throws HashingException Error obteniendo el hash
	 */
	public static TimeStamp stampDocument (InputStream stream, URL serverTimeStampURL, String hashingAlgorithm) throws HashingException, TimeStampServerConnectionException, MalformedTimeStampException, ResponseTimeStampException {
		InputStreamDocument document = new InputStreamDocument(stream);
		return TimeStamp.stampHash(document.getHash(hashingAlgorithm), serverTimeStampURL);
	}

	/**
	 * Método que obtiene un sello de tiempo de un servidor de sello de tiempos. 
	 * 
	 * @param urlDocument URL del documento cuyo contenido se desea sellar
	 * @param serverTimeStampURL URL del servidor de sello de tiempos
	 * @param hashingAlgorithm Algoritmo de hashing
	 * @param parameters Parámetros de la llamada al sello de tiempos
	 * @throws FileNotFoundException El fichero no existe
	 * @throws TimeStampServerConnectionException Errores en la conexión con el servidor de sello
	 * 	de tiempos
	 * @throws MalformedTimeStampException El objeto devuelto por el servidor no parece ser un 
	 * 	sello de tiempos
	 * @throws ResponseTimeStampException La TSA ha devuelto una respuesta con un error
	 * @throws HashingException Error obteniendo el hash
	 * @throws InitDocumentException No se puede obtener el documento en la URL
	 * @throws NoSuchAlgorithmException No existe el algoritmo de hashing
	 */
	public static TimeStamp stampDocument (URL urlDocument, URL serverTimeStampURL, String hashingAlgorithm, TimeStampRequestParameters parameters) throws HashingException, TimeStampServerConnectionException, MalformedTimeStampException, ResponseTimeStampException, InitDocumentException, NoSuchAlgorithmException {
		URLDocument document = new URLDocument(urlDocument);
		return TimeStamp.stampHash(document.getHash(hashingAlgorithm), serverTimeStampURL, hashingAlgorithm, parameters);
	}

	/**
	 * Método que obtiene un sello de tiempo de un servidor de sello de tiempos. 
	 * 
	 * @param urlDocument URL del documento cuyo contenido se desea sellar
	 * @param serverTimeStampURL URL del servidor de sello de tiempos
	 * @param hashingAlgorithm Algoritmo de hashing
	 * @throws FileNotFoundException El fichero no existe
	 * @throws TimeStampServerConnectionException Errores en la conexión con el servidor de sello
	 * 	de tiempos
	 * @throws MalformedTimeStampException El objeto devuelto por el servidor no parece ser un 
	 * 	sello de tiempos
	 * @throws ResponseTimeStampException La TSA ha devuelto una respuesta con un error
	 * @throws HashingException Error obteniendo el hash
	 * @throws InitDocumentException No se puede obtener el documento en la URL
	 */
	public static TimeStamp stampDocument (URL urlDocument, URL serverTimeStampURL, String hashingAlgorithm) throws HashingException, TimeStampServerConnectionException, MalformedTimeStampException, ResponseTimeStampException, InitDocumentException {
		URLDocument document = new URLDocument(urlDocument);
		return TimeStamp.stampHash(document.getHash(hashingAlgorithm), serverTimeStampURL);
	}

	/**
	 * Método que obtiene un sello de tiempo de un servidor de sello de tiempos. 
	 * 
	 * @param document Documento cuyo contenido se desea sellar
	 * @param serverTimeStampURL URL del servidor de sello de tiempos
	 * @param hashingAlgorithm Algoritmo de hashing
	 * @param parameters Parámetros de la llamada al sello de tiempos
	 * @throws FileNotFoundException El fichero no existe
	 * @throws TimeStampServerConnectionException Errores en la conexión con el servidor de sello
	 * 	de tiempos
	 * @throws MalformedTimeStampException El objeto devuelto por el servidor no parece ser un 
	 * 	sello de tiempos
	 * @throws ResponseTimeStampException La TSA ha devuelto una respuesta con un error
	 * @throws HashingException Error obteniendo el hash
	 * @throws NoSuchAlgorithmException No existe el algoritmo de hashing
	 */
	public static TimeStamp stampDocument (Document document, URL serverTimeStampURL, String hashingAlgorithm, TimeStampRequestParameters parameters) throws HashingException, TimeStampServerConnectionException, MalformedTimeStampException, ResponseTimeStampException, NoSuchAlgorithmException {
		return TimeStamp.stampHash(document.getHash(hashingAlgorithm), serverTimeStampURL, hashingAlgorithm, parameters);
	}

	/**
	 * Método que obtiene un sello de tiempo de un servidor de sello de tiempos. 
	 * 
	 * @param document Documento cuyo contenido se desea sellar
	 * @param serverTimeStampURL URL del servidor de sello de tiempos
	 * @param hashingAlgorithm Algoritmo de hashing
	 * @throws FileNotFoundException El fichero no existe
	 * @throws TimeStampServerConnectionException Errores en la conexión con el servidor de sello
	 * 	de tiempos
	 * @throws MalformedTimeStampException El objeto devuelto por el servidor no parece ser un 
	 * 	sello de tiempos
	 * @throws ResponseTimeStampException La TSA ha devuelto una respuesta con un error
	 * @throws HashingException Error obteniendo el hash
	 */
	public static TimeStamp stampDocument (Document document, URL serverTimeStampURL, String hashingAlgorithm) throws HashingException, TimeStampServerConnectionException, MalformedTimeStampException, ResponseTimeStampException {
		return TimeStamp.stampHash(document.getHash(hashingAlgorithm), serverTimeStampURL);
	}

	/**
	 * Método que obtiene un sello de tiempo de un servidor de sello de tiempos.
	 * 
	 * @param hash Hash del documento cuyo contenido se desea sellar
	 * @param serverTimeStampURL URL del servidor de sello de tiempos
	 * @param hashingAlgorithm Algoritmo de hashing
	 * @param parameters Parámetros de la llamada al sello de tiempos
	 * @throws FileNotFoundException El fichero no existe
	 * @throws TimeStampServerConnectionException Errores en la conexión con el servidor de sello
	 * 	de tiempos
	 * @throws MalformedTimeStampException El objeto devuelto por el servidor no parece ser un 
	 * 	sello de tiempos
	 * @throws ResponseTimeStampException La TSA ha devuelto una respuesta con un error
	 * @throws NoSuchAlgorithmException No existe el algoritmo de hashing
	 */
	public static TimeStamp stampHash (byte[] hash, URL serverTimeStampURL, String hashingAlgorithm, TimeStampRequestParameters parameters) throws TimeStampServerConnectionException, MalformedTimeStampException, ResponseTimeStampException, NoSuchAlgorithmException {
		return new TimeStamp (getTimeStamp(hash, serverTimeStampURL, hashingAlgorithm, parameters));
	}

	/**
	 * Método que obtiene un sello de tiempo de un servidor de sello de tiempos.
	 * 
	 * @param hash Hash del documento cuyo contenido se desea sellar
	 * @param serverTimeStampURL URL del servidor de sello de tiempos
	 * @throws FileNotFoundException El fichero no existe
	 * @throws TimeStampServerConnectionException Errores en la conexión con el servidor de sello
	 * 	de tiempos
	 * @throws MalformedTimeStampException El objeto devuelto por el servidor no parece ser un 
	 * 	sello de tiempos
	 * @throws ResponseTimeStampException La TSA ha devuelto una respuesta con un error
	 */
	public static TimeStamp stampHash (byte[] hash, URL serverTimeStampURL) throws TimeStampServerConnectionException, MalformedTimeStampException, ResponseTimeStampException {
		try {
			return stampHash (hash, serverTimeStampURL, HashingAlgorithm.getDefault(), null);
		} catch (NoSuchAlgorithmException e) {
			// No se va a dar porque el algoritmo de hashing por defecto si existe
			return null;
		}
	}
	
	/**
	 * Devuelve una respuesta de sello de tiempo de error: punto 2.4.2 de la
	 * <a href="http://tools.ietf.org/rfc/rfc3161.txt" target="rfc">RFC-3161</a>
	 * 
	 * @param status Estado de la respuesta (obligatorio)
	 * @param statusString Descripción del estado de la respuesta (opcional)
	 * @param failInfo Código de error (opcional)
	 * @return Respuesta de error 
	 */
	public static TimeStampResponse getErrorTimestamp (PKIStatus status, String statusString, PKIFailureInfo failInfo) {
		logger.debug("[TimeStamp.getErrorTimestamp]::Entrada::" + Arrays.asList(new Object[] {status, statusString, failInfo}));
		
		PKIFreeText textoEstado = null;
		if (statusString != null) {
			textoEstado = new PKIFreeText(statusString);
		}
		
		PKIStatusInfo statusInfo = new PKIStatusInfo(status, textoEstado, failInfo);
		TimeStampResp response = new TimeStampResp(statusInfo, null);
		try {
			return new TimeStampResponse(response);
		} catch (Exception e) {
			logger.info("[TimeStamp.getErrorTimestamp]::Error inesperado", e);
			return null;
		} 
	}

	/**
	 * Obtiene una petición de sello de tiempos
	 * 
	 * @param hash Hash
	 * @param hashingAlgorithm Algoritmo de hash
	 * @param parameters Parámetros a incluir en la petición
	 * @return Petición de sello de tiempos
	 * @throws NoSuchAlgorithmException No existe el algoritmo de hash
	 */
	public static TimeStampRequest getTimeStampRequest(byte[] hash, String hashingAlgorithm, TimeStampRequestParameters parameters) throws NoSuchAlgorithmException {
		logger.debug("[TimeStamp.getTimeStampRequest]::Entrada::" + Arrays.asList(new Object[] { hash, parameters }));
		
	    // Obtener el generador de peticiones a la TSA
	    TimeStampRequestGenerator tsrGenerator = new TimeStampRequestGenerator();
	    tsrGenerator.setCertReq(true);
	    
	    // Añadir parámetros
	    if (parameters != null) {
		    if (parameters.getOid() != null) {
		    	tsrGenerator.setReqPolicy(new ASN1ObjectIdentifier(parameters.getOid()));
		    }
		    
		    // Obtener las extensiones
		    if (parameters.getExtensions() != null && !parameters.getExtensions().isEmpty()) {
		    	for(TimeStampRequestParameters.TimeStampRequestExtension extension : parameters.getExtensions()) {
		    		tsrGenerator.addExtension(new ASN1ObjectIdentifier(extension.getOid()), false, extension.getValue());
		    	}
		    }
	    }
	    
	    // Obtener el algoritmo por el tamaño del hash
	    String hashingAlgorithmOID = HashingAlgorithm.getOID(hashingAlgorithm);
	    try {
		    switch (hash.length) {
				case 20:
					hashingAlgorithmOID = HashingAlgorithm.getOID(HashingAlgorithm.SHA1);
					break;
				case 32:
					hashingAlgorithmOID = HashingAlgorithm.getOID(HashingAlgorithm.SHA256);
					break;
				case 48:
					hashingAlgorithmOID = HashingAlgorithm.getOID(HashingAlgorithm.SHA384);
					break;
				case 64:
					hashingAlgorithmOID = HashingAlgorithm.getOID(HashingAlgorithm.SHA512);
			}
	    } catch (NoSuchAlgorithmException e) {
	    	logger.debug("[TimeStamp.getTimeStamp]::No existe el algoritmo para el tamaño: " + hash.length);
	    }
	    
	    //-- Obtener la petición
	    BigInteger nonce = BigInteger.valueOf(System.currentTimeMillis());
	    if (parameters != null && parameters.getNonce() != null) {
	    	nonce = parameters.getNonce();
	    }
	    
	    return tsrGenerator.generate(new ASN1ObjectIdentifier(hashingAlgorithmOID), hash, nonce);
	}
	
	//-- Métodos privados
	
	/*
	 * Obtiene un sello de tiempos de la URL
	 */
	protected static TimeStampToken getTimeStamp (byte[] hash, URL serverTimeStampURL, String hashingAlgorithm, TimeStampRequestParameters parameters) throws TimeStampServerConnectionException, MalformedTimeStampException, ResponseTimeStampException, NoSuchAlgorithmException {
		logger.debug("[TimeStamp.getTimeStamp]::Entrada::" + Arrays.asList(new Object[] { hash, serverTimeStampURL, hashingAlgorithm, parameters }));
		
	    TimeStampRequest request = TimeStamp.getTimeStampRequest(hash, hashingAlgorithm, parameters);
	    byte[] requestBytes = null;
		try {
			requestBytes = request.getEncoded();
		} catch (IOException e) {
			logger.info("[TimeStamp.getTimeStamp]:: Error obteniendo la petición de sello de tiempo", e);
		}

		//-- Conectar a la TSA y obtener la respuesta
	    Hashtable reqProperties = new Hashtable();

	    if (parameters != null && parameters.getUser() != null && parameters.getPassword() != null) {
		    String userPassword = parameters.getUser() + ":" + parameters.getPassword();
		    String reqString = "Basic " + new String(Util.encodeBase64(userPassword.getBytes()));
		    reqProperties.put("Authorization", reqString);
		    reqProperties.put("Content-Length", String.valueOf(reqString.length()));
	    }

	    reqProperties.put("Content-Type", "application/timestamp-query");
	    reqProperties.put("Content-Transfer-Encoding", "binary");

	    TimeStampResponse response;
		try {
			response = connect(serverTimeStampURL, reqProperties, requestBytes);
		} catch (IOException e) {
			logger.info("[TimeStamp.getTimeStamp]::Error conectando con la TSA", e);
			throw new TimeStampServerConnectionException ("Error conectando con la TSA", e);
		}

	    //-- Validar la respuesta
	    if (response.getTimeStampToken()== null) {
	    	
	    	if (response.getFailInfo() == null) {
	    		logger.info("[TimeStamp.getTimeStamp]::Por algún motivo desconocido la TSA no ha podido devolver un sello de tiempos");
	    		throw new ResponseTimeStampException ("Por algún motivo desconocido la TSA no ha podido devolver un sello de tiempos");
	    	} else {
	    	
		    	switch (response.getFailInfo().intValue()) {
		    		case PKIFailureInfo.badAlg:
			    		logger.info("[TimeStamp.getTimeStamp]::El algoritmo del hashing no está reconocido por la TSA");
		    			throw new ResponseTimeStampException ("El algoritmo del hashing no está reconocido por la TSA");
		    		case PKIFailureInfo.badRequest:
			    		logger.info("[TimeStamp.getTimeStamp]::La TSA considera que la petición no es correcta");
		    			throw new ResponseTimeStampException ("La TSA considera que la petición no es correcta");
		    		case PKIFailureInfo.badDataFormat:
			    		logger.info("[TimeStamp.getTimeStamp]::La información a sellar tiene un formato incorrecto");
		    			throw new ResponseTimeStampException ("La información a sellar tiene un formato incorrecto");
		    		case PKIFailureInfo.timeNotAvailable:
			    		logger.info("[TimeStamp.getTimeStamp]::En estos momentos la fuente de tiempos de la TSA no se encuentra disponible");
		    			throw new ResponseTimeStampException ("En estos momentos la fuente de tiempos de la TSA no se encuentra disponible");
		    		case PKIFailureInfo.unacceptedPolicy:
			    		logger.info("[TimeStamp.getTimeStamp)]::La política pedida a la TSA no es aceptada por ésta");
		    			throw new ResponseTimeStampException ("La política pedida a la TSA no es aceptada por ésta");
		    		case PKIFailureInfo.unacceptedExtension:
			    		logger.info("[TimeStamp.getTimeStamp]::La extensión pedida no es aceptada por la TSA");
		    			throw new ResponseTimeStampException ("La extensión pedida no es aceptada por la TSA");
		    		case PKIFailureInfo.addInfoNotAvailable:
			    		logger.info("[TimeStamp.getTimeStamp]::La información adicional pedida no es entendida o no está disponible en la TSA");
		    			throw new ResponseTimeStampException ("La información adicional pedida no es entendida o no está disponible en la TSA");
		    		case PKIFailureInfo.systemFailure:
			    		logger.info("[TimeStamp.getTimeStamp]::La respuesta no puede ser atendida debido a un error interno del servidor");
		    			throw new ResponseTimeStampException ("La respuesta no puede ser atendida debido a un error interno del servidor");
		    		default:
			    		logger.info("[TimeStamp.getTimeStamp]::Por algún motivo desconocido la TSA no ha podido devolver un sello de tiempos");
		    			throw new ResponseTimeStampException ("Por algún motivo desconocido la TSA no ha podido devolver un sello de tiempos");
		    	}
	    	}
	    }
	    
	    return response.getTimeStampToken();
		
	}
	
	/*
	 * Obtiene un sello de tiempos de la URL
	 */
	protected static TimeStampToken getTimeStampIntegra (byte[] hash, URL serverTimeStampURL, String hashingAlgorithm, TimeStampRequestParameters parameters) throws TimeStampServerConnectionException, MalformedTimeStampException, ResponseTimeStampException, NoSuchAlgorithmException {
		logger.debug("[TimeStamp.getTimeStamp]::Entrada::" + Arrays.asList(new Object[] { hash, serverTimeStampURL, hashingAlgorithm, parameters }));
		
	    TimeStampRequest request = TimeStamp.getTimeStampRequest(hash, hashingAlgorithm, parameters);
	    byte[] requestBytes = null;
		try {
			requestBytes = request.getEncoded();
		} catch (IOException e) {
			logger.info("[TimeStamp.getTimeStamp]:: Error obteniendo la petición de sello de tiempo", e);
		}

		//-- Conectar a la TSA y obtener la respuesta
	    Hashtable reqProperties = new Hashtable();

	    if (parameters != null && parameters.getUser() != null && parameters.getPassword() != null) {
		    String userPassword = parameters.getUser() + ":" + parameters.getPassword();
		    String reqString = "Basic " + new String(Util.encodeBase64(userPassword.getBytes()));
		    reqProperties.put("Authorization", reqString);
		    reqProperties.put("Content-Length", String.valueOf(reqString.length()));
	    }

	    reqProperties.put("Content-Type", "application/timestamp-query");
	    reqProperties.put("Content-Transfer-Encoding", "binary");

	    TimeStampResponse response;
		try {
			response = connect(serverTimeStampURL, reqProperties, requestBytes);
		} catch (IOException e) {
			logger.info("[TimeStamp.getTimeStamp]::Error conectando con la TSA", e);
			throw new TimeStampServerConnectionException ("Error conectando con la TSA", e);
		}

	    //-- Validar la respuesta
	    if (response.getTimeStampToken()== null) {
	    	
	    	if (response.getFailInfo() == null) {
	    		logger.info("[TimeStamp.getTimeStamp]::Por algún motivo desconocido la TSA no ha podido devolver un sello de tiempos");
	    		throw new ResponseTimeStampException ("Por algún motivo desconocido la TSA no ha podido devolver un sello de tiempos");
	    	} else {
	    	
		    	switch (response.getFailInfo().intValue()) {
		    		case PKIFailureInfo.badAlg:
			    		logger.info("[TimeStamp.getTimeStamp]::El algoritmo del hashing no está reconocido por la TSA");
		    			throw new ResponseTimeStampException ("El algoritmo del hashing no está reconocido por la TSA");
		    		case PKIFailureInfo.badRequest:
			    		logger.info("[TimeStamp.getTimeStamp]::La TSA considera que la petición no es correcta");
		    			throw new ResponseTimeStampException ("La TSA considera que la petición no es correcta");
		    		case PKIFailureInfo.badDataFormat:
			    		logger.info("[TimeStamp.getTimeStamp]::La información a sellar tiene un formato incorrecto");
		    			throw new ResponseTimeStampException ("La información a sellar tiene un formato incorrecto");
		    		case PKIFailureInfo.timeNotAvailable:
			    		logger.info("[TimeStamp.getTimeStamp]::En estos momentos la fuente de tiempos de la TSA no se encuentra disponible");
		    			throw new ResponseTimeStampException ("En estos momentos la fuente de tiempos de la TSA no se encuentra disponible");
		    		case PKIFailureInfo.unacceptedPolicy:
			    		logger.info("[TimeStamp.getTimeStamp)]::La política pedida a la TSA no es aceptada por ésta");
		    			throw new ResponseTimeStampException ("La política pedida a la TSA no es aceptada por ésta");
		    		case PKIFailureInfo.unacceptedExtension:
			    		logger.info("[TimeStamp.getTimeStamp]::La extensión pedida no es aceptada por la TSA");
		    			throw new ResponseTimeStampException ("La extensión pedida no es aceptada por la TSA");
		    		case PKIFailureInfo.addInfoNotAvailable:
			    		logger.info("[TimeStamp.getTimeStamp]::La información adicional pedida no es entendida o no está disponible en la TSA");
		    			throw new ResponseTimeStampException ("La información adicional pedida no es entendida o no está disponible en la TSA");
		    		case PKIFailureInfo.systemFailure:
			    		logger.info("[TimeStamp.getTimeStamp]::La respuesta no puede ser atendida debido a un error interno del servidor");
		    			throw new ResponseTimeStampException ("La respuesta no puede ser atendida debido a un error interno del servidor");
		    		default:
			    		logger.info("[TimeStamp.getTimeStamp]::Por algún motivo desconocido la TSA no ha podido devolver un sello de tiempos");
		    			throw new ResponseTimeStampException ("Por algún motivo desconocido la TSA no ha podido devolver un sello de tiempos");
		    	}
	    	}
	    }
	    
	    return response.getTimeStampToken();
		
	}


	/*
	 * Metodo que inicializa este objeto en base al stream de lectura que se pasa como
	 * parámetro.
	 * 
	 * @param isTimeStamp Stream de lectura a un objeto sello de tiempos
	 * @throws TimeStampException El objeto contenido en el stream de lectura no parece
	 * 	ser un sello de tiempos
	 */
	private void initialize (InputStream isTimeStamp) throws MalformedTimeStampException {
		logger.debug("[TimeStamp.initialize]::Entrada::" + isTimeStamp);
		
		// Obtenemos el objeto DER
		ASN1Sequence derTS;
		try {
			derTS = (ASN1Sequence)Util.toDER(isTimeStamp);
		} catch (IOException e) {
			logger.info("[TimeStamp.initialize]::El stream no parece contener un objeto en formato DER", e);
			throw new MalformedTimeStampException ("El stream no parece contener un objeto en formato DER", e);
		}
		
		try {
			this.timeStamp = new TimeStampToken (new CMSSignedData(derTS.getEncoded(ASN1Encoding.DER)));
		} catch (TSPException e) {
			logger.info("[TimeStamp.initialize]::El stream no parece contener un sello de tiempos", e);
			throw new MalformedTimeStampException ("El stream no parece contener un sello de tiempos", e);
		} catch (IOException e) {
			logger.info("[TimeStamp.initialize]::El stream no parece contener un objeto en formato DER", e);
			throw new MalformedTimeStampException ("El stream no parece contener un objeto en formato DER", e);
		} catch (CMSException e) {
			logger.info("[TimeStamp.initialize]::El stream no parece contener un sello de tiempos", e);
			throw new MalformedTimeStampException ("El stream no parece contener un sello de tiempos", e);
		}

	}
	
	/*
	 * Se conecta a una TSA, le pasa la petición con las propiedades reqProperties y
	 * obtiene una respuesta
	 */
	private static TimeStampResponse connect(URL serverTimeStampURL, Hashtable reqProperties, byte[] requestBytes) throws IOException, MalformedTimeStampException {
	    logger.debug("[TimeStamp.connect]:: " + Arrays.asList(new Object[] { serverTimeStampURL, reqProperties, requestBytes }));

	    HttpURLConnection urlConn;
	    OutputStream printout = null;
	    DataInputStream input = null;

	    // Abrir la conexión a la URL
	    urlConn = (HttpURLConnection) serverTimeStampURL.openConnection();
	    
	    // La conexión será en las dos direcciones
	    urlConn.setDoInput(true);
	    urlConn.setDoOutput(true);
	    urlConn.setRequestMethod("POST");
	    
	    // No usar caché
	    urlConn.setUseCaches(false);
	    
	    // Propiedades de la petición
	    Iterator iter = reqProperties.entrySet().iterator();
	    while (iter.hasNext()) {
	    	Map.Entry entry = (Map.Entry) iter.next();
	    	urlConn.setRequestProperty((String) entry.getKey(), (String) entry.getValue());
	    }
	    
	    // Escribir la petición
	    try {
		    printout = urlConn.getOutputStream();
		    printout.write(requestBytes);
		    printout.flush();
	    } finally {
	    	if (printout != null) {
	    		try { printout.close(); } catch (IOException e) { }
	    	}
	    }
	    
	    // Obtener la respuesta
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    try {
		    input = new DataInputStream(urlConn.getInputStream());
		    
		    byte[] buffer = new byte[1024];
		    int bytesRead = 0;
		    while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
		    	baos.write(buffer, 0, bytesRead);
		    }
	    } finally {
	    	if (input != null) {
	    		try { input.close(); } catch (IOException e) { }
	    	}
	    }
	    
	    try {
			return new TimeStampResponse (baos.toByteArray());
		} catch (TSPException e) {
			logger.info("[TimeStamp.connect]::La respuesta del servidor no parece una respuesta de sello de tiempos", e);
			throw new MalformedTimeStampException ("La respuesta del servidor no parece una respuesta de sello de tiempos", e);
		}
	    

	}
	
	/*
	 * Se conecta a una TSA, le pasa la petición con las propiedades reqProperties y
	 * obtiene una respuesta
	 */
	private static TimeStampResponse connectHttps(URL serverTimeStampURL, Hashtable reqProperties, byte[] requestBytes) throws IOException, MalformedTimeStampException {
	    logger.debug("[TimeStamp.connect]:: " + Arrays.asList(new Object[] { serverTimeStampURL, reqProperties, requestBytes }));

	    HttpURLConnection urlConn;
	    OutputStream printout = null;
	    DataInputStream input = null;

	    // Abrir la conexión a la URL
	    urlConn = (HttpURLConnection) serverTimeStampURL.openConnection();
	    
	    // La conexión será en las dos direcciones
	    urlConn.setDoInput(true);
	    urlConn.setDoOutput(true);
	    urlConn.setRequestMethod("POST");
	    
	    // No usar caché
	    urlConn.setUseCaches(false);
	    
	    // Propiedades de la petición
	    Iterator iter = reqProperties.entrySet().iterator();
	    while (iter.hasNext()) {
	    	Map.Entry entry = (Map.Entry) iter.next();
	    	urlConn.setRequestProperty((String) entry.getKey(), (String) entry.getValue());
	    }
	    
	    // Escribir la petición
	    try {
		    printout = urlConn.getOutputStream();
		    printout.write(requestBytes);
		    printout.flush();
	    } finally {
	    	if (printout != null) {
	    		try { printout.close(); } catch (IOException e) { }
	    	}
	    }
	    
	    // Obtener la respuesta
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    try {
		    input = new DataInputStream(urlConn.getInputStream());
		    
		    byte[] buffer = new byte[1024];
		    int bytesRead = 0;
		    while ((bytesRead = input.read(buffer, 0, buffer.length)) >= 0) {
		    	baos.write(buffer, 0, bytesRead);
		    }
	    } finally {
	    	if (input != null) {
	    		try { input.close(); } catch (IOException e) { }
	    	}
	    }
	    
	    try {
			return new TimeStampResponse (baos.toByteArray());
		} catch (TSPException e) {
			logger.info("[TimeStamp.connect]::La respuesta del servidor no parece una respuesta de sello de tiempos", e);
			throw new MalformedTimeStampException ("La respuesta del servidor no parece una respuesta de sello de tiempos", e);
		}
	    

	}


}

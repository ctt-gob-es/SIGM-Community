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
package es.accv.arangi.base.signature;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.ASN1Primitive;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.ASN1Set;
import org.bouncycastle.asn1.DERInteger;
import org.bouncycastle.asn1.DERNull;
import org.bouncycastle.asn1.DERObjectIdentifier;
import org.bouncycastle.asn1.DEROctetString;
import org.bouncycastle.asn1.DEROutputStream;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.asn1.DERSet;
import org.bouncycastle.asn1.DERTaggedObject;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.asn1.cms.IssuerAndSerialNumber;
import org.bouncycastle.asn1.cms.SignedData;
import org.bouncycastle.asn1.cms.SignerIdentifier;
import org.bouncycastle.asn1.cms.SignerInfo;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.CMSSignedData;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.SignerInformationStore;
import org.bouncycastle.cms.SignerInformationVerifier;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.util.Store;

import es.accv.arangi.base.algorithm.CipherAlgorithm;
import es.accv.arangi.base.algorithm.DigitalSignatureAlgorithm;
import es.accv.arangi.base.algorithm.HashingAlgorithm;
import es.accv.arangi.base.certificate.Certificate;
import es.accv.arangi.base.certificate.validation.CAList;
import es.accv.arangi.base.certificate.validation.ValidateCertificate;
import es.accv.arangi.base.certificate.validation.service.CertificateValidationService;
import es.accv.arangi.base.document.IDocument;
import es.accv.arangi.base.document.InputStreamDocument;
import es.accv.arangi.base.exception.certificate.CertificateCANotFoundException;
import es.accv.arangi.base.exception.certificate.CertificateFieldException;
import es.accv.arangi.base.exception.certificate.NormalizeCertificateException;
import es.accv.arangi.base.exception.document.HashingException;
import es.accv.arangi.base.exception.signature.NoDocumentToSignException;
import es.accv.arangi.base.exception.signature.SignatureException;
import es.accv.arangi.base.exception.timestamp.MalformedTimeStampException;
import es.accv.arangi.base.timestamp.TimeStamp;
import es.accv.arangi.base.util.Util;
import es.accv.arangi.base.util.validation.ValidationResult;

/**
 * Clase que se encarga de realizar firmas en formato CMS y PKCS#7.<br><br>
 * 
 * El formato CMS deriva de la versión 1.5 de PKCS#7, documentada en la
 * <a href="http://tools.ietf.org/rfc/rfc2315.txt" target="rfc">RFC-2315</a>. Desde
 * entonces ha sufrido varios cambios hasta su última versión, que viene
 * definida en la <a href="http://tools.ietf.org/rfc/rfc3852.txt" target="rfc">RFC-3852</a>.
 * Aunque existen diferencias entre los dos formatos, básicamente una firma
 * CMS es igual a una firma PKCS#7 con algunas extensiones.<br><br>
 * 
 * Las clases de Bouncy Castle tratan los dos formatos como uno sólo: CMS.
 * En Arangi se va a seguir este principio, aunque conservando una clase para
 * cada formato, principalmente por las diferencias que puede haber en firmas
 * en navegadores: IExplorer firma en CMS y Firefox en PKCS#7.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José M Gutiérrez</a>
 */
public abstract class CMSPKCS7Signature extends Signature{

	/**
	 * Logger de la clase
	 */
	static Logger logger = Logger.getLogger(CMSPKCS7Signature.class);
	
	/**
	 * Constante con el OID de la sección 'data' en formato DER
	 */
	public static final String OID_DATA = "1.2.840.113549.1.7.1";
	
	/**
	 * Constante con el OID donde se encuentra el sello de tiempos en un CMS
	 */
	public static final String OID_CMS_TIMESTAMP = "1.2.840.113549.1.9.16.2.14";
	
	/*
	 * Firma en CMS(PKCS#7)
	 */
	protected byte[] signature;
	
	/*
	 * Certificados con los que se realizó la firma
	 */
	protected Certificate[] certificates;
	
	/*
	 * Documento firmado
	 */
	protected IDocument document;
	
	/*
	 * Algoritmos de hashing. Si hay varias firmas con distintos algoritmos no funcionará
	 */
	String hashingAlgorithm;
	
	/**
	 * Obtiene la firma de un fichero.
	 * 
	 * @param fileSignature Fichero con la firma en formato CMS (PKCS#7)
	 * @throws IOException Error leyendo el fichero o la firma proporcionada no parece estar en formato DER
	 * @throws NormalizeCertificateException El certificado de la firma no puede ser normalizado
	 * 	al formato esperado por el proveedor criptográfico de Arangi
	 * @throws SignatureException Error obteniendo los certificados de la firma
	 */
	public CMSPKCS7Signature (File fileSignature) throws NormalizeCertificateException, SignatureException, IOException {
		
		logger.debug("[CMSPKCS7Signature(file)]:: Entrada :: " + fileSignature.getAbsolutePath());
		
		//-- Obtener array de bytes
		byte[] signature = Util.loadFile(fileSignature);
		
		//-- Inicializar el objeto
		initialize (signature);
	}
	
	/**
	 * Obtiene la firma de un stream de lectura.
	 * 
	 * @param isSignature Stream de lectura a la firma en formato CMS (PKCS#7)
	 * @throws IOException Error leyendo el stream de lectura o la firma proporcionada no parece 
	 * 	estar en formato DER
	 * @throws NormalizeCertificateException El certificado de la firma no puede ser normalizado
	 * 	al formato esperado por el proveedor criptográfico de Arangi
	 * @throws SignatureException Error obteniendo los certificados de la firma
	 */
	public CMSPKCS7Signature (InputStream isSignature) throws NormalizeCertificateException, SignatureException, IOException {
		
		logger.debug("[CMSPKCS7Signature(InputStream)]:: Entrada :: " + isSignature);
		
		//-- Obtener array de bytes
		byte[] signature = Util.readStream(isSignature);
		
		//-- Inicializar el objeto
		initialize (signature);
	}
	
	/**
	 * Obtiene la firma de un array de bytes.
	 * 
	 * @param signature Firma en formato CMS (PKCS#7)
	 * @throws NormalizeCertificateException El certificado de la firma no puede ser normalizado
	 * 	al formato esperado por el proveedor criptográfico de Arangi
	 * @throws SignatureException Error obteniendo los certificados de la firma
	 */
	public CMSPKCS7Signature (byte[] signature) throws NormalizeCertificateException, SignatureException {
		
		logger.debug("[CMSPKCS7Signature(byte[])]:: Entrada :: " + signature);
		
		//-- Inicializar el objeto
		initialize (signature);
	}
	
	/**
	 * Construye una firma en formato CMS(PKCS#7) en base a los bytes de las firmas y
	 * los certificados con los que se realizaron éstas, con el algoritmo de 
	 * firma por defecto (SHA1WithRSA).
	 * 
	 * @param signatureBytes Bytes de las firmas
	 * @param certificates Certificados con los que se realizó la firma
	 * @throws SignatureException Error construyendo la firma
	 */
	public CMSPKCS7Signature(byte[][] signatureBytes, Certificate[] certificates) throws SignatureException {
		
		//-- Obtener el array de algoritmos
		String[] algorithms = new String [certificates.length];
		for (int i=0;i<algorithms.length;i++) {
			algorithms[i] = DigitalSignatureAlgorithm.getDefault();
		}
		
		initialize (signatureBytes, certificates, null, algorithms);
	}
	
	/**
	 * Construye un firma en formato CMS(PKCS#7) en base a los bytes de las firmas y
	 * los certificados con los que se realizaron éstas, con los algoritmos de 
	 * firma indicados. 
	 * 
	 * @param signatureBytes Bytes de las firmas
	 * @param certificates Certificados con los que se realizó la firma
	 * @param digitalSignatureAlgorithms Algoritmos de firma
	 * @throws NoSuchAlgorithmException El algoritmo de firma no existe en Arangi
	 * @throws SignatureException Error construyendo la firma
	 */
	public CMSPKCS7Signature(byte[][] signatureBytes, Certificate[] certificates, String[] digitalSignatureAlgorithms) throws SignatureException {
		initialize (signatureBytes, certificates, null, digitalSignatureAlgorithms);
	}

	/**
	 * Construye un firma en formato CMS(PKCS#7) en base a los bytes de las firmas y
	 * los certificados con los que se realizaron éstas, con el algoritmo de 
	 * firma por defecto (SHA1WithRSA). El documento se añadira a la firma 
	 * (attached).
	 * 
	 * @param signatureBytes Bytes de las firmas
	 * @param certificates Certificados con los que se realizó la firma
	 * @param document Documento que se ha firmado
	 * @throws SignatureException Error construyendo la firma
	 */
	public CMSPKCS7Signature(byte[][] signatureBytes, Certificate[] certificates, IDocument document) throws SignatureException {
		
		//-- Obtener el array de algoritmos
		String[] algorithms = new String [certificates.length];
		for (int i=0;i<algorithms.length;i++) {
			algorithms[i] = DigitalSignatureAlgorithm.getDefault();
		}
		
		initialize (signatureBytes, certificates, document, algorithms);
	}
	
	/**
	 * Construye un firma en formato CMS(PKCS#7) en base a los bytes de las firmas y
	 * los certificados con los que se realizaron éstas, con el algoritmo de 
	 * firma indicado. El documento se añadira a la firma (attached).
	 * 
	 * @param signatureBytes Bytes de la firma
	 * @param certificates Certificados con los que se realizó la firma
	 * @param digitalSignatureAlgorithms Algoritmos de firma
	 * @param document Documento que se ha firmado
	 * @throws SignatureException Error construyendo la firma
	 */
	public CMSPKCS7Signature(byte[][] signatureBytes, Certificate[] certificates, IDocument document, String[] digitalSignatureAlgorithms) throws SignatureException {
		initialize (signatureBytes, certificates, document, digitalSignatureAlgorithms);
	}
	
	/*
	 * Constructor protegido de la clase
	 * 
	 * @param signature Firma CMS(PKCS#7)
	 * @param certificates Certificados con los que se realizaron las firmaa
	 * @param document Documento a firmar
	 */
	protected CMSPKCS7Signature(byte[] signature, Certificate[] certificates, IDocument document, boolean isAttached) {
		
		logger.debug ("[CMSPKCS7Signature(byte[],certificate[],idocument,boolean)]::Inicio::" + Arrays.asList(new Object[] { signature, certificates, document, new Boolean (isAttached) } ));
		
		this.signature = signature;
		this.certificates = certificates;
		if (isAttached) {
			this.document = document;
		}
		
	}

	//-- Implementación de ISignature
	
	/**
	 * Obtiene los bytes resultantes del proceso de firma para cada uno de los 
	 * certificados. No hay que confundir la respuesta de este método con la 
	 * respuesta dada por {@link #toByteArray() toByteArray}.<br><br>
	 * 
	 * Por ejemplo, en un objeto CMS, el método toByteArray devolverá un CMS,
	 * mientras que este método devolverá los bytes de la firma contenida dentro
	 * del CMS.
	 * 
	 * @return Bytes de la firma para cada certificado
	 */
	public byte[][] getSignaturesBytes() {
		
		logger.debug ("[CMSPKCS7Signature.getSignaturesBytes]::Entrada");
		
		//-- Obtener el objeto DER
		ASN1Primitive derObject;
		try {
			derObject = Util.getDER(this.signature);
		} catch (IOException e) {
			logger.info("[CMSPKCS7Signature.getSignatureBytes]::No se pueden obtener los bytes de la firma del objeto CMS(PKCS#7)", e);
			return null;
		}
		
		//-- Obtener los signerInfos
		HashMap<SignerIdentifier,byte[]> hmSignerInfos = new HashMap<SignerIdentifier,byte[]> ();
		ContentInfo contentInfo = ContentInfo.getInstance(derObject);
		SignedData signedData = SignedData.getInstance(contentInfo.getContent());
		Enumeration enumeration = signedData.getSignerInfos().getObjects();
		while (enumeration.hasMoreElements()) {
			DERSequence sequence = (DERSequence) enumeration.nextElement();
			SignerInfo signerInfo = SignerInfo.getInstance(sequence);
			hmSignerInfos.put (signerInfo.getSID(), signerInfo.getEncryptedDigest().getOctets());
		}

		//-- Ordenarlos de acuerdo al orden de los certificados
		byte[][] signaturesBytes = new byte [hmSignerInfos.size()][];
		for (int i = 0; i < this.certificates.length; i++) {
			try {
				signaturesBytes[i] = (byte[]) hmSignerInfos.get(this.certificates[i].getIssuerAndSerialNumber());
			} catch (CertificateFieldException e) {
				logger.info("[CMSPKCS7Signature.getSignatureBytes]::No se pueden obtener el issuer and serial number del certificado", e);
			} 
		}

		logger.debug ("[CMSPKCS7Signature.getSignaturesBytes]::Devolviendo " + signaturesBytes);
		
		return signaturesBytes;
	}

	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.signature.ISignature#getCertificate()
	 */
	public Certificate[] getCertificates() {
		logger.debug ("[CMSPKCS7Signature.getCertificates]::Entrada");
		return certificates;
	}

	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.signature.ISignature#getDocument()
	 */
	public IDocument getDocument() {
		logger.debug ("[CMSPKCS7Signature.getDocument]::Entrada");
		return document;
	}

	/**
	 * Método que indica si la firma es attached (el documento se halla contenido
	 * en ella)
	 * 
	 * @return Cierto si la firma es attached
	 */
	public boolean isAttached() {
		logger.debug ("[CMSPKCS7Signature.isAttached]::Entrada");
		if (document == null) {
			return false;
		} else {
			return true;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.signature.ISignature#isValidSignatureOnly()
	 */
	public ValidationResult[] isValidSignatureOnly() throws HashingException, SignatureException, NoDocumentToSignException {

		logger.debug ("[CMSPKCS7Signature.isValidSignatureOnly]::Entrada");

		//-- Si la firma no es attached no podemos validar
		if (!isAttached()) {
			logger.info("[CMSPKCS7Signature.isValidSignatureOnly]::No se puede validar una firma sin el documento que la originó");
			throw new NoDocumentToSignException ("No se puede validar una firma sin el documento que la originó");
		}
		
		return isValidSignatureOnly(this.document);
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.signature.ISignature#isValidSignatureOnly(IDocument)
	 */
	public ValidationResult[] isValidSignatureOnly(IDocument document) throws HashingException, SignatureException {
		return isValidSignatureOnlyWithHash (document.getHash(hashingAlgorithm));
	}
	
	/**
	 * Valida únicamente la correspondencia entre la firma y el hash, sin
	 * validar los certificados de la firma
	 * 
	 * @param hash Hash del documento que originó la firma
	 * @return Validación de la firma
	 * @throws SignatureException Error tratando la firma
	 */
	public ValidationResult[] isValidSignatureOnlyWithHash(byte[] hash) throws SignatureException {
		
		logger.debug ("[CMSPKCS7Signature.isValidSignatureOnly]::Entrada::" + hash);
		
		try {
			
			CMSSignedData signedData = getCMSSignedData (hash);
			
			Store certs = signedData.getCertificates();
			SignerInformationStore signers = signedData.getSignerInfos();
			Collection<SignerInformation> c = signers.getSigners();
			List<ValidationResult> alResponses = new ArrayList<ValidationResult> ();
			for (SignerInformation signer : c) {
				Collection<X509CertificateHolder> certCollection = certs.getMatches(signer.getSID());
				for (X509CertificateHolder certificate : certCollection) {
					SignerInformationVerifier siv = new JcaSimpleSignerInfoVerifierBuilder().setProvider(CRYPTOGRAPHIC_PROVIDER).build(Util.getCertificate(certificate).getPublicKey());
					if (signer.verify (siv)) {
						alResponses.add (new ValidationResult (Util.getCertificate(certificate)));
					} else {
						alResponses.add (new ValidationResult (ValidationResult.RESULT_SIGNATURE_NOT_MATCH_DATA, Util.getCertificate(certificate)));
					}
					
				}
			}
			
			return (ValidationResult[]) alResponses.toArray(new ValidationResult[0]);

		} catch (HashingException e) {
			//-- No se va a dar porque ya se pasa un hash
			logger.info("[CMSPKCS7Signature.isValidSignatureOnly]::No se puede obtener el hash del documento que originó la firma", e);
			return null;
		} catch (CMSException e) {
			//-- Ya se comprobó, por lo que es raro que aparezca esta excepción
			logger.info("[CMSPKCS7Signature.isValidSignatureOnly]::No se puede obtener la zona firmada del objeto firma", e);
			throw new SignatureException ("No se puede obtener la zona firmada del objeto firma", e);
		} catch (NoSuchAlgorithmException e) {
			//-- Es el algoritmo por defecto, por lo que es raro que aparezca esta excepción
			logger.info("[CMSPKCS7Signature.isValidSignatureOnly]::No se puede tratar el algoritmo de hash " + HashingAlgorithm.getDefault(), e);
			throw new SignatureException ("No se puede tratar el algoritmo de hash " + HashingAlgorithm.getDefault(), e);
		} catch (OperatorCreationException e) {
			logger.info("[CMSPKCS7Signature.isValidSignatureOnly]::No se puede obtener el verificador de la firma PKCS#7/CMS", e);
			throw new SignatureException ("No se puede obtener el verificador de la firma PKCS#7/CMS", e);
		} catch (CertificateException e) {
			logger.info("[CMSPKCS7Signature.isValidSignatureOnly]::No se puede obtener el certificado de la firma", e);
			throw new SignatureException ("No se puede obtener el certificado de la firma", e);
		} 
		
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.signature.ISignature#isValid(CAList)
	 */
	public ValidationResult[] isValid(CAList caList) throws NoDocumentToSignException, SignatureException, NormalizeCertificateException, HashingException {
		
		logger.debug ("[CMSPKCS7Signature.isValid]::Entrada::" + caList);
		
		//-- Si la firma no es attached no podemos validar
		if (!isAttached()) {
			logger.info("[CMSPKCS7Signature.isValid]::No se puede validar una firma sin el documento que la originó");
			throw new NoDocumentToSignException ("No se puede validar una firma sin el documento que la originó");
		}
		
		return isValid(this.document, caList);

	}
	
	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.signature.ISignature#isValid(IDocument,CAList)
	 */
	public ValidationResult[] isValid(IDocument document, CAList caList) throws SignatureException, NormalizeCertificateException, HashingException {
		return isValidWithHash(document.getHash(hashingAlgorithm), caList);	
	}
	
	/**
	 * Comprueba que la firma es válida a partir del hash del documento
	 * 
	 * @param hash Hash del documento
	 * @param caList Lista de CAs
	 * @return Validación de la firma
	 * @throws SignatureException Error tratando la firma
	 * @throws NormalizeCertificateException No se puede normalizar el certificado
	 * 	de firma al proveedor criptográfico de Arangí
	 */
	public ValidationResult[] isValidWithHash(byte[] hash, CAList caList) throws SignatureException, NormalizeCertificateException {
		
		logger.debug ("[CMSPKCS7Signature.isValid]::Entrada::" + Arrays.asList(new Object [] { document, caList }));
		
		//-- Validar las firmas
		ValidationResult[] validarFirmas = isValidSignatureOnlyWithHash(hash);
		
		//-- Comprobar si hay sello de tiempos. Si lo hay verificar el certificado en la fecha indicada.
		Date validationDate;
		TimeStamp timeStamp;
		try {
			timeStamp = getTimeStamp();
		} catch (MalformedTimeStampException e) {
			logger.info("[CMSPKCS7Signature.isValid]::El sello de tiempos de la firma está mal formado", e);
			throw new SignatureException ("El sello de tiempos de la firma está mal formado", e);
		} 
		
		if (timeStamp != null) {
			validationDate = timeStamp.getTime();
		} else {
			validationDate = new Date();
		}
				
		//-- Validar los certificados
		Certificate[] certificates = getCertificates ();
		for (int i = 0; i < certificates.length; i++) {
			ValidateCertificate validateCertificate;
			try {
				validateCertificate = new ValidateCertificate(certificates[i].toX509Certificate(), caList);
			} catch (CertificateCANotFoundException e) {
				logger.info("[CMSPKCS7Signature.isValid]::La lista de CAs no contiene todos los certificados de la cadena de confianza del certificado: " + certificates[i].getCommonName(), e);
				validarFirmas[i] = new ValidationResult (ValidationResult.RESULT_CERTIFICATE_NOT_BELONGS_TRUSTED_CAS, certificates[i].toX509Certificate(), timeStamp==null?null:validationDate, timeStamp, null);
				continue;
			}
			if (validarFirmas[i].isValid()) {
				validarFirmas[i] = new ValidationResult (validateCertificate.validate(validationDate), validateCertificate.toX509Certificate(), timeStamp==null?null:validationDate, timeStamp, null);
			} else {
				validarFirmas[i] = new ValidationResult (validarFirmas[i].getResult(), validateCertificate.toX509Certificate(), timeStamp==null?null:validationDate, timeStamp, null);
			}
		}
		
		return validarFirmas;
		
	}
	
	/**
	 * Comprueba que la firma es válida a partir del hash del documento
	 * 
	 * @param hash Hash del documento
	 * @param caList Lista de CAs
	 * @param validationServices Lista de servicios de validación
	 * @return Validación de la firma
	 * @throws SignatureException Error tratando la firma
	 * @throws NormalizeCertificateException No se puede normalizar el certificado
	 * 	de firma al proveedor criptográfico de Arangí
	 */
	public ValidationResult[] isValidWithHash(byte[] hash, CAList caList, List<CertificateValidationService> validationServices) throws SignatureException, NormalizeCertificateException {
		
		logger.debug ("[CMSPKCS7Signature.isValidWithHash]::Entrada::" + Arrays.asList(new Object [] { document, caList }));
		
		//-- Validar las firmas
		ValidationResult[] validationResult = isValidSignatureOnlyWithHash(hash);
		
		//-- Comprobar si hay sello de tiempos. Si lo hay verificar el certificado en la fecha indicada.
		Date validationDate;
		TimeStamp timeStamp;
		try {
			timeStamp = getTimeStamp();
		} catch (MalformedTimeStampException e) {
			logger.info("[CMSPKCS7Signature.isValidWithHash]::El sello de tiempos de la firma está mal formado", e);
			throw new SignatureException ("El sello de tiempos de la firma está mal formado", e);
		} 
		
		if (timeStamp != null) {
			validationDate = timeStamp.getTime();
		} else {
			validationDate = new Date();
		}
				
		//-- Validar los certificados contra calist
		Certificate[] certificates = getCertificates ();
		boolean unknownCertificate = false;
		for (int i = 0; i < certificates.length; i++) {
			ValidateCertificate validateCertificate;
			try {
				validateCertificate = new ValidateCertificate(certificates[i].toX509Certificate(), caList);
			} catch (CertificateCANotFoundException e) {
				logger.info("[CMSPKCS7Signature.isValidWithHash]::La lista de CAs no contiene todos los certificados de la cadena de confianza del certificado: " + certificates[i].getCommonName(), e);
				unknownCertificate = true;
				break;
			}
			if (validationResult[i].isValid()) {
				validationResult[i] = new ValidationResult (validateCertificate.validate(validationDate), validateCertificate.toX509Certificate(), timeStamp==null?null:validationDate, timeStamp, null);
			} else {
				validationResult[i] = new ValidationResult (validationResult[i].getResult(), validateCertificate.toX509Certificate(), timeStamp==null?null:validationDate, timeStamp, null);
			}
		}
		
		if (!unknownCertificate) {		
			return validationResult;
		}
		
		//-- Validar los certificados contra servicios de validación
		validationResult = isValidSignatureOnlyWithHash(hash);
		for (int i = 0; i < certificates.length; i++) {
			if (validationResult[i].isValid()) {
				validationResult[i] = new ValidationResult (certificates[i].validate(validationServices, validationDate).getResult(), certificates[i].toX509Certificate(), timeStamp==null?null:validationDate, timeStamp, null);
			} else {
				validationResult[i] = new ValidationResult (validationResult[i].getResult(), certificates[i].toX509Certificate(), timeStamp==null?null:validationDate, timeStamp, null);
			}
		}
		
		return validationResult;
	}
	
	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.signature.ISignature#isValid(List<CertificateValidationService>)
	 */
	public ValidationResult[] isValid (List<CertificateValidationService> validationServices) throws HashingException, SignatureException, NormalizeCertificateException, NoDocumentToSignException {
		
		logger.debug ("[CMSPKCS7Signature.isValid]::Entrada::" + validationServices);
		
		//-- Si la firma no es attached no podemos validar
		if (!isAttached()) {
			logger.info("[CMSPKCS7Signature.isValid]::No se puede validar una firma sin el documento que la originó");
			throw new NoDocumentToSignException ("No se puede validar una firma sin el documento que la originó");
		}
		
		return isValid(this.document, validationServices);

	}

	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.signature.ISignature#isValid(IDocument,List<CertificateValidationService>)
	 */
	public ValidationResult[] isValid(IDocument document, List<CertificateValidationService> validationServices) throws HashingException, SignatureException, NormalizeCertificateException {
		return isValidWithHash(document.getHash(hashingAlgorithm), validationServices);
	}
	
	/**
	 * Comprueba que la firma es válida a partir del hash del documento
	 * 
	 * @param hash Hash del documento
	 * @param validationServices Lista de servicios de validación
	 * @return Validación de la firma
	 * @throws SignatureException Error tratando la firma
	 * @throws NormalizeCertificateException No se puede normalizar el certificado
	 * 	de firma al proveedor criptográfico de Arangí
	 */
	public ValidationResult[] isValidWithHash(byte[] hash, List<CertificateValidationService> validationServices) throws SignatureException, NormalizeCertificateException {
		
		logger.debug ("[CMSPKCS7Signature.isValid]::Entrada::" + Arrays.asList(new Object [] { document, validationServices }));
		
		//-- Validar las firmas
		ValidationResult[] validarFirmas = isValidSignatureOnlyWithHash(hash);
		
		//-- Comprobar si hay sello de tiempos. Si lo hay verificar el certificado en la fecha indicada.
		Date validationDate;
		TimeStamp timeStamp;
		try {
			timeStamp = getTimeStamp();
		} catch (MalformedTimeStampException e) {
			logger.info("[CMSPKCS7Signature.isValid]::El sello de tiempos de la firma está mal formado", e);
			throw new SignatureException ("El sello de tiempos de la firma está mal formado", e);
		} 
		
		if (timeStamp != null) {
			validationDate = timeStamp.getTime();
		} else {
			validationDate = new Date();
		}
				
		//-- Validar los certificados
		Certificate[] certificates = getCertificates ();
		for (int i = 0; i < certificates.length; i++) {
			if (validarFirmas[i].isValid()) {
				validarFirmas[i] = new ValidationResult (certificates[i].validate(validationServices, validationDate).getResult(), certificates[i].toX509Certificate(), timeStamp==null?null:validationDate, timeStamp, null);
			} else {
				validarFirmas[i] = new ValidationResult (validarFirmas[i].getResult(), certificates[i].toX509Certificate(), timeStamp==null?null:validationDate, timeStamp, null);
			}
		}
		
		return validarFirmas;
		
	}
	
	/**
	 * Obtiene el sello de tiempos de la firma. 
	 * 
	 * @return Sello de tiempos de la firma. Si no hay devuelve null.
	 * @throws MalformedTimeStampException El objeto contenido en la firma no parece ser un sello de tiempo
	 */
	public TimeStamp getTimeStamp() throws MalformedTimeStampException {

		logger.debug("[CMSPKCS7Signature.getTimeStamp]::Buscando sello de tiempos en el PKCS7/CMS");
		ASN1Primitive derObject;
		try {
			derObject = Util.getDER(this.signature);
		} catch (IOException e) {
			// No se va a dar, esta misma acción se hace al inicializar el objeto
			return null;
		}
		ContentInfo contentInfo = ContentInfo.getInstance(derObject);
		SignedData signedData = SignedData.getInstance(contentInfo.getContent());
		ASN1Set set = signedData.getSignerInfos();
		if (set.size() == 1) {
			DERSet derSello = (DERSet) buscarSelloTiempo (((DERSequence) set.getObjectAt(0)).getObjects(), OID_CMS_TIMESTAMP);
			if (derSello == null || derSello.size() == 0) {
				logger.debug("[CMSPKCS7Signature.getTimeStamp]::No se ha encontrado sello de tiempos en el PKCS7/CMS");
				return null;
			}
			
			//-- Obtener sello de tiempos
			logger.debug("[CMSPKCS7Signature.getTimeStamp]::Encontrado sello de tiempos en el PKCS7/CMS");
			try {
				return new TimeStamp (derSello.getObjectAt(0).toASN1Primitive().getEncoded());
			} catch (IOException e) {
				logger.debug("[CMSPKCS7Signature.getTimeStamp]::No se ha podido leer el sello de tiempos en el PKCS7/CMS");
				throw new MalformedTimeStampException("No se ha podido leer el sello de tiempos en el PKCS7/CMS");
			}
		} 
		
		logger.debug("[CMSPKCS7Signature.getTimeStamp]::No se ha encontrado sello de tiempos en el PKCS7/CMS");
		return null;
	}

	/**
	 * Guarda la firma en disco
	 * 
	 * @param file Fichero donde se guardará la firma
	 * @throws IOException Errores de entrada / salida
	 */
	public void save (File file) throws IOException {
		logger.debug ("[CMSPKCS7Signature.save]::Entrada::" + file);
		
		Util.saveFile(file, signature);
	}
	
	/**
	 * Guarda la firma en un stream de escritura.
	 * 
	 * @param out Stream de escritura
	 * @throws IOException Errores de entrada / salida
	 */
	public void save (OutputStream out) throws IOException {
		logger.debug ("[CMSPKCS7Signature.save]::Entrada::" + out);
		
		Util.save(out, signature);
	}
	
	//-- Métodos públicos
	
	/**
	 * Obtiene la firma en formato CMS (PKCS#7) como un array de bytes
	 */
	public byte[] toByteArray() {
		return signature;
	}
	
	/**
	 * Método para poder validar con el método Signature.validateSignature.<br><br>
	 * 
	 * Analiza el parámetro y, si se trata de un objeto CMS o PKCS#7,
	 * devuelve un objeto de alguno de estos tipos.
	 * 
	 * @param bSignature Firma como array de bytes
	 * @return CMS o PKCS#7
	 * @throws Exception El parámetro no es un CMS o PKCS#7
	 */
	public static ISignature getSignatureInstance (byte[] bSignature) throws Exception {
		return new CMSSignature(bSignature);
	}

	/**
	 * Transforma un CMS o PKCS#7 para que el OID del mismo sea 1.2.840.113549.1.7.2
	 * 
	 * @throws Exception No se ha podido realizar la transformación
	 */
	public void transform () throws Exception {
		try {
			CMSSignedData signedData = new CMSSignedData(signature);
			ContentInfo ci = signedData.toASN1Structure();
			if (!ci.getContentType().getId().equals("1.2.840.113549.1.7.2")) {
				DERSequence e = ((DERSequence)ci.getContent());
				DERSet dd = (DERSet)e.getObjectAt(4); 
				DERSequence e1 = ((DERSequence)dd.getObjectAt(0));
				DEROctetString dos = ((DEROctetString)e1.getObjectAt(4));
				byte[][] signatureBytes = new byte[][] { dos.getOctets() };
				byte[] firma = createPKCS7CMS(signatureBytes, certificates, document, new String[] {DigitalSignatureAlgorithm.SHA1_RSA}, "1.2.840.113549.1.7.2");
				initialize(firma);
			}
		} catch (Exception e) {
			logger.error(e.getMessage());
			throw new Exception ("No se puede realizar la transformación", e);
		}
	}
	
	//-- Métodos privados
	
	
	/*
	 * Construye un firma en formato CMS(PKCS#7) en base a los bytes de la firma y
	 * el certificado asociado a la clave privada con la que se firmó, con el
	 * algoritmo de firma indicado. El documento se añadira a la firma (attached).
	 * 
	 * @param signatureBytes Bytes de las firmas
	 * @param certificates Certificados con los que se realizaron las firmas
	 * @param digitalSignatureAlgorithms Algoritmos de firma
	 * @param document Documento que se ha firmado
	 * @throws SignatureException Error construyendo la firma
	 */
	private void initialize(byte[][] signatureBytes, Certificate[] certificates, IDocument document, String[] digitalSignatureAlgorithms) throws SignatureException {
		logger.debug ("[CMSPKCS7Signature.initialize]:: Entrada ::" + Arrays.asList(new Object [] { signatureBytes, certificates, document, digitalSignatureAlgorithms }));
		
		this.signature = createPKCS7CMS(signatureBytes, certificates, document, digitalSignatureAlgorithms, getOIDFormatoFirma ());
		this.certificates = certificates;
		this.document = document;
	}

	/*
	 * Construye un firma en formato CMS(PKCS#7) en base a los bytes de la firma y
	 * el certificado asociado a la clave privada con la que se firmó, con el
	 * algoritmo de firma indicado. El documento se añadira a la firma (attached).
	 * 
	 * @param signatureBytes Bytes de las firmas
	 * @param certificates Certificados con los que se realizaron las firmas
	 * @param digitalSignatureAlgorithms Algoritmos de firma
	 * @param document Documento que se ha firmado
	 * @throws SignatureException Error construyendo la firma
	 */
	private void initialize(byte[] signature) throws NormalizeCertificateException, SignatureException {
		
		logger.debug ("[CMSPKCS7Signature.initialize]:: Entrada ::" + Arrays.asList(new Object [] { signature }));
		
		//-- Cargar el objeto firma
		this.signature = signature;
		
		//-- Obtener el certificado		
		ASN1Primitive derObject;
		try {
			derObject = Util.getDER(this.signature);
		} catch (IOException e) {
			logger.info("[CMSPKCS7Signature.initialize]:: No se pueden obtener los bytes de la firma del objeto CMS(PKCS#7)", e);
			return;
		}
		
		List<Certificate> alCertificates = new ArrayList<Certificate> ();
		try {
			// Montamos datos y certificados en un CMSSignedData
			CMSSignedData signedData = new CMSSignedData(signature);
		 
			Store certs = signedData.getCertificates();
			SignerInformationStore signers = signedData.getSignerInfos();
			Collection<SignerInformation> c = signers.getSigners();
			for (SignerInformation signer : c) {
				Collection<X509CertificateHolder> certCollection = certs.getMatches(signer.getSID());
				for (X509CertificateHolder certificate : certCollection) {
					alCertificates.add(new Certificate (certificate));
				}
				hashingAlgorithm = signer.getDigestAlgOID();
			}
		} catch (CMSException e) {
			//-- Ya se comprobó, por lo que es raro que aparezca esta excepción
			logger.info("[CMSPKCS7Signature.isValidSignatureOnly]::No se puede obtener la zona firmada del objeto firma", e);
			throw new SignatureException ("No se puede obtener la zona firmada del objeto firma", e);
		} 
		
		this.certificates = (Certificate[]) alCertificates.toArray(new Certificate [0]);
		
		//-- Obtener el documento, si existe
		ContentInfo contentInfo = ContentInfo.getInstance(derObject);
		SignedData signedData = SignedData.getInstance(contentInfo.getContent());
		ContentInfo encapsulatedContentInfo = signedData.getEncapContentInfo();
		if (encapsulatedContentInfo.getContent() != null) {
			this.document = new InputStreamDocument (new ByteArrayInputStream(((DEROctetString)encapsulatedContentInfo.getContent()).getOctets()));
		}

	}

	/*
	 * Crea un CMS(PKCS#7) a partir de las firmas y los certificados.
	 * 
	 * @param signatureBytes Bytes de las firmas
	 * @param certificates Certificados con los que se realizaron las firmas
	 * @param document Documento a añadir al CMS(PKCS#7). Si es nulo la firma es dettached
	 * @param hashingAlgorithm Algoritmo de hash
	 * @param cipherAlgorithm Algoritmo de cifrado
	 * @param oidFormatoFirma OID del formato de firma (PKCS#7 o CMS)
	 */
	protected static  byte[] createPKCS7CMS(byte[][] signatureBytes, Certificate[] certificates, IDocument document, String[] digitalSignatureAlgorithms, String oidFormatoFirma) throws SignatureException {
		
		//-- Creamos el DERSet con los algoritmos de hashing
    	ASN1EncodableVector vector = new ASN1EncodableVector();
		for (int i = 0; i < digitalSignatureAlgorithms.length; i++) {
			//-- Comprobar si se quiere utilizar el algoritmo por defecto
			if (digitalSignatureAlgorithms[i] == null) {
				digitalSignatureAlgorithms[i] = DigitalSignatureAlgorithm.getDefault();
			}
			
			try {
				String hashingAlgorithmID = HashingAlgorithm.getOID(DigitalSignatureAlgorithm.getHashingAlgorithm(digitalSignatureAlgorithms[i]));
		    	vector.add(new AlgorithmIdentifier(new ASN1ObjectIdentifier(hashingAlgorithmID), null));
			} catch (NoSuchAlgorithmException e1) {
				logger.info("[CMSPKCS7Signature.createPKCS7CMS]::No se puede utilizar el algoritmo " + digitalSignatureAlgorithms[i] + " en Arangi", e1);
				throw new SignatureException ("No se puede utilizar el algoritmo " + digitalSignatureAlgorithms[i] + " en Arangi", e1);
			}
		}
    	DERSet setAlgorithm = new DERSet(vector);

   	 	//-- Creamos el contentInfo, añadimos los datos si se pasa un documento
    	ASN1Encodable contentInfo = new DERSequence(new DERObjectIdentifier(OID_DATA));
    	if (document != null) {
    		try {
				contentInfo = new ContentInfo (new ASN1ObjectIdentifier(OID_DATA), new DEROctetString (Util.readStream(document.getInputStream())));
			} catch (Exception e) {
				logger.info("[CMSPKCS7Signature.createPKCS7CMS]::No se puede leer el documento para añadirlo a la firma", e);
				throw new SignatureException ("No se puede leer el documento para añadirlo a la firma", e);
			}
    	}
    	
	    try {
	    	//-- Añadimos los certificados
	    	vector = new ASN1EncodableVector();
	    	for (int i = 0; i < certificates.length; i++) {
		    	ASN1InputStream inputStream = new ASN1InputStream(new ByteArrayInputStream(certificates[i].toX509Certificate().getEncoded()));
		    	vector.add(inputStream.readObject());
			}
	    	DERSet setCertificates = new DERSet(vector);
	
	        //-- Creamos los signerInfo
	    	List<DERSequence> alSignerInfos = new ArrayList<DERSequence> ();
	    	for (int i = 0; i < certificates.length; i++) {
	    		
	    		//-- Obtener algoritmo de hashing y de cifrado
	    		String hashingAlgorithmID, cipherAlgorithmID;
	    		try {
		    		hashingAlgorithmID = HashingAlgorithm.getOID(DigitalSignatureAlgorithm.getHashingAlgorithm(digitalSignatureAlgorithms[i]));
		    		cipherAlgorithmID = CipherAlgorithm.getOID(DigitalSignatureAlgorithm.getCipherAlgorithm(digitalSignatureAlgorithms[i]));
				} catch (NoSuchAlgorithmException e1) {
					logger.info("[CMSPKCS7Signature.createPKCS7CMS]::No se puede utilizar el algoritmo " + digitalSignatureAlgorithms[i] + " en Arangi", e1);
					throw new SignatureException ("No se puede utilizar el algoritmo " + digitalSignatureAlgorithms[i] + " en Arangi", e1);
				}
	    		
	    		//-- Construir el signer info
		        ASN1EncodableVector signerInfo = new ASN1EncodableVector();
		        signerInfo.add(new DERInteger(1));
		        
		        IssuerAndSerialNumber issuerSN = certificates[i].getIssuerAndSerialNumber();
		        signerInfo.add(issuerSN);
		
		        // Algoritmo de hashing
		        signerInfo.add(new AlgorithmIdentifier(
		                     		new ASN1ObjectIdentifier(hashingAlgorithmID),
		                            DERNull.INSTANCE));
				
				// Algoritmo de cifrado
		        signerInfo.add(new AlgorithmIdentifier(
		                     		new ASN1ObjectIdentifier(cipherAlgorithmID),
		                     		DERNull.INSTANCE));
		       
		       	// Bytes de la firma
		        signerInfo.add(new DEROctetString(signatureBytes[i]));
		        
		        //-- Añadir el signer info al set
		        alSignerInfos.add (new DERSequence (signerInfo));
			}
	
			// Conxtruimos el objeto final
	        ASN1EncodableVector content = new ASN1EncodableVector();
	        content.add(new DERInteger(1));
	        content.add(setAlgorithm);
	        content.add(contentInfo);
	        content.add(new DERTaggedObject(false, 0, setCertificates));
	        
	        content.add(new DERSet((DERSequence[])alSignerInfos.toArray(new DERSequence [0])));
	
	        vector = new ASN1EncodableVector();
	        vector.add(new DERObjectIdentifier(oidFormatoFirma));
	        vector.add(new DERTaggedObject(0, new DERSequence(content)));
	
	        ByteArrayOutputStream baos = new ByteArrayOutputStream();
	
	        DEROutputStream derOut = new DEROutputStream(baos);
	        derOut.writeObject(new DERSequence(vector));
	        derOut.close();
	        
	        return baos.toByteArray();
	        
    	} catch (CertificateEncodingException e) {
			logger.info("[CMSPKCS7Signature.createPKCS7CMS]::No ha sido posible codificar a DER el certificado de la firma", e);
			throw new SignatureException ("No ha sido posible codificar a DER el certificado de la firma", e);
		} catch (IOException e) {
			logger.info("[CMSPKCS7Signature.createPKCS7CMS]::Error de entrada/salida obteniendo la firma", e);
			throw new SignatureException ("Error de entrada/salida obteniendo la firma", e);
		} catch (CertificateFieldException e) {
			logger.info("[CMSPKCS7Signature.createPKCS7CMS]::Error obteniendo el issuer and serial number de un certificado", e);
			throw new SignatureException (e.getMessage(), e);
		} 
    	
	}
	
	/*
	 * Obtiene el objeto bouncy castle de la firma
	 */
	private CMSSignedData getCMSSignedData (byte[] hash) throws HashingException, NoSuchAlgorithmException, CMSException {
		// Obtener el hash del documento y colocarlo en un map tal y como quiere CMSSignedData
		if (hash == null) {
			//-- attached
			hash = this.document.getHash(hashingAlgorithm);
		}
		Map<String,byte[]> hmHashes = new HashMap<String,byte[]> ();
		hmHashes.put(hashingAlgorithm, hash);
		
		// Montamos datos y certificados en un CMSSignedData
		 return new CMSSignedData(hmHashes, signature );
	}

	/*
	 * Recorre una serie de objetos DER de forma recursiva hasta encontrar un
	 * elemento con el oid que se pasa como parámetro.
	 */
	private static ASN1Primitive buscarSelloTiempo(Enumeration en, String oid) {
		
	      while (en.hasMoreElements()) {
	    	  ASN1Primitive derObject = (ASN1Primitive)en.nextElement();
	    	  if (derObject instanceof DERSequence) {
	    		  ASN1Primitive resultado;
	    		  if  ((resultado = buscarSelloTiempo (((ASN1Sequence) derObject).getObjects(), oid)) != null) {
	    			  return resultado;
	    		  }
	    	  }
	    	  if (derObject instanceof DERTaggedObject) {
	    		  ASN1Primitive resultado;
	    		  if  ((resultado = buscarSelloTiempo (((ASN1Sequence)((DERTaggedObject) derObject).getObject()).getObjects(), oid)) != null) {
	    			  return resultado;
	    		  }
	    	  }
	    	  if (derObject instanceof DERSet) {
	    		  ASN1Primitive resultado;
	    		  if  ((resultado = buscarSelloTiempo (((DERSet) derObject).getObjects(), oid)) != null) {
	    			  return resultado;
	    		  }
	    	  }
	    	  if (derObject instanceof DERObjectIdentifier) {
	    		  if (((DERObjectIdentifier) derObject).getId().equals(oid)) {
	    			  return (ASN1Primitive) en.nextElement();
	    		  }
	    	  }
	      }
	      
	      return null;
	}


	//-- Métodos abstractos
	
	/**
	 * Devuelve el OID del formato de la firma
	 */
	protected abstract String getOIDFormatoFirma();


}

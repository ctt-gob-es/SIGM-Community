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

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.cms.ContentInfo;
import org.bouncycastle.cms.CMSSignedData;

import es.accv.arangi.base.certificate.Certificate;
import es.accv.arangi.base.device.DeviceManager;
import es.accv.arangi.base.document.IDocument;
import es.accv.arangi.base.exception.certificate.NormalizeCertificateException;
import es.accv.arangi.base.exception.device.AliasNotFoundException;
import es.accv.arangi.base.exception.device.LoadingObjectException;
import es.accv.arangi.base.exception.device.SearchingException;
import es.accv.arangi.base.exception.document.HashingException;
import es.accv.arangi.base.exception.signature.SignatureException;
import es.accv.arangi.base.util.Util;

/**
 * Clase que se encarga de realizar firmas en formato PKCS#7 de acuerdo a la
 * <a href="http://tools.ietf.org/rfc/rfc2315.txt" target="rfc">RFC-2315</a>.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José M Gutiérrez</a>
 */
public class PKCS7Signature extends CMSPKCS7Signature{

	/**
	 * Logger de la clase
	 */
	static Logger logger = Logger.getLogger(PKCS7Signature.class);
	
	/**
	 * OID para PKCS#7
	 */
	public static final String OID_FORMATO_FIRMA = "1.2.840.113549.1.7.2";

	/**
	 * Obtiene la firma de un fichero.
	 * 
	 * @param fileSignature Fichero con la firma en formato PKCS#7
	 * @throws IOException Error leyendo el fichero o la firma proporcionada no parece estar en formato DER
	 * @throws NormalizeCertificateException El certificado de la firma no puede ser normalizado
	 * 	al formato esperado por el proveedor criptográfico de Arangi
	 * @throws SignatureException Error construyendo la firma
	 */
	public PKCS7Signature(File fileSignature) throws IOException,
			NormalizeCertificateException, SignatureException {
		super(fileSignature);
	}

	/**
	 * Obtiene la firma de un stream de lectura.
	 * 
	 * @param isSignature Stream de lectura a la firma en formato PKCS#7
	 * @throws IOException Error leyendo el stream de lectura o la firma proporcionada no parece 
	 * 	estar en formato DER
	 * @throws NormalizeCertificateException El certificado de la firma no puede ser normalizado
	 * 	al formato esperado por el proveedor criptográfico de Arangi
	 * @throws SignatureException Error construyendo la firma
	 */
	public PKCS7Signature(InputStream isSignature) throws IOException,
			NormalizeCertificateException, SignatureException {
		super(isSignature);
	}

	/**
	 * Obtiene la firma de un array de bytes.
	 * 
	 * @param signature Firma en formato PKCS#7
	 * @throws NormalizeCertificateException El certificado de la firma no puede ser normalizado
	 * 	al formato esperado por el proveedor criptográfico de Arangi
	 * @throws SignatureException Error construyendo la firma
	 */
	public PKCS7Signature(byte[] signature) throws NormalizeCertificateException, SignatureException {
		super(signature);
	}

	/**
	 * Construye un firma en formato PKCS#7 en base a los bytes de las firmas y
	 * los certificados con los que se realizaron éstas, con el algoritmo de 
	 * firma indicado. El documento se añadira a la firma (attached).
	 * 
	 * @param signatureBytes Bytes de la firma
	 * @param certificates Certificados con los que se realizó la firma
	 * @param digitalSignatureAlgorithms Algoritmos de firma
	 * @param document Documento que se ha firmado
	 * @throws SignatureException Error construyendo la firma
	 */
	public PKCS7Signature(byte[][] signatureBytes, Certificate[] certificates,
			IDocument document, String[] digitalSignatureAlgorithms) throws SignatureException {
		super(signatureBytes, certificates, document, digitalSignatureAlgorithms);
	}

	/**
	 * Construye un firma en formato PKCS#7 en base a los bytes de las firmas y
	 * los certificados con los que se realizaron éstas, con el algoritmo de 
	 * firma por defecto (SHA1WithRSA). El documento se añadira a la firma 
	 * (attached).
	 * 
	 * @param signatureBytes Bytes de las firmas
	 * @param certificates Certificados con los que se realizó la firma
	 * @param document Documento que se ha firmado
	 * @throws SignatureException Error construyendo la firma
	 */
	public PKCS7Signature(byte[][] signatureBytes, Certificate[] certificates,
			IDocument document) throws SignatureException {
		super(signatureBytes, certificates, document);
	}

	/**
	 * Construye un firma en formato PKCS#7 en base a los bytes de las firmas y
	 * los certificados con los que se realizaron éstas, con los algoritmos de 
	 * firma indicados. 
	 * 
	 * @param signatureBytes Bytes de las firmas
	 * @param certificates Certificados con los que se realizó la firma
	 * @param digitalSignatureAlgorithms Algoritmos de firma
	 * @throws SignatureException Error construyendo la firma
	 */
	public PKCS7Signature(byte[][] signatureBytes, Certificate[] certificates,
			String[] digitalSignatureAlgorithms) throws SignatureException {
		super(signatureBytes, certificates, digitalSignatureAlgorithms);
	}

	/**
	 * Construye una firma en formato PKCS#7 en base a los bytes de las firmas y
	 * los certificados con los que se realizaron éstas, con el algoritmo de 
	 * firma por defecto (SHA1WithRSA).
	 * 
	 * @param signatureBytes Bytes de las firmas
	 * @param certificates Certificados con los que se realizó la firma
	 * @throws SignatureException Error construyendo la firma
	 */
	public PKCS7Signature(byte[][] signatureBytes, Certificate[] certificates)
			throws SignatureException {
		super(signatureBytes, certificates);
	}
	
	/*
	 * Constructor protegido de la clase
	 * 
	 * @param signature Firma PKCS#7
	 * @param certificates Certificados con los que se realizaron las firmaa
	 * @param document Documento a firmar
	 */
	protected PKCS7Signature(byte[] signature, Certificate[] certificates, IDocument document, boolean isAttached) {
		super (signature, certificates, document, isAttached);
	}

	
	/**
	 * Obtiene un objeto {@link PKCS7Signature PKCS7Signature}. Los arrays que se pasan como
	 * parámetro deben tener el mismo tamaño. Dado un elemento i del array de managers, se 
	 * debe corresponder con el elemento i de los alias y de los algoritmos de firma
	 * 
	 * @param managers Dispositivos criptográficos
	 * @param alias Alias donde se encuentrann las claves privada dentro de los dispositivos
	 * @param document Documento a firmar
	 * @param digitalSignatureAlgorithms Algoritmos de firma a utilizar
	 * @throws AliasNotFoundException El alias donde se encuentra la clave privada usada para
	 * 	realizar la firma no existe
	 * @throws HashingException No es posible obtener el hash del documento o su versión en 
	 * 	formato DER durante el proceso de firma
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada usada para
	 * 	realizar la firma
	 * @throws SignatureException Error durante el proceso de firma
	 * @throws SearchingException No se ha podido encontrar el certificado asociado a la clave 
	 * 	privada
	 */
	public static PKCS7Signature sign (DeviceManager[] managers, String[] alias, IDocument document, String[] digitalSignatureAlgorithms, boolean isAttached) throws AliasNotFoundException, HashingException, LoadingObjectException, SignatureException, SearchingException {
		
		logger.debug ("[PKCS7Signature.sign]::Entrada::" + Arrays.asList(new Object[] { managers, alias, document, digitalSignatureAlgorithms, new Boolean (isAttached) }));
		
		//-- Obtener la lista de bytes de firma y certificados con los que se realizan
		ArrayList alSignatureBytes = new ArrayList ();
		ArrayList alCertificates = new ArrayList ();
		for (int i = 0; i < managers.length; i++) {
			//-- Obtener la firma
			alSignatureBytes.add(managers[i].signDocument(document, alias[i]));
			
			//-- Obtener el certificado
			try {
				alCertificates.add (new Certificate (managers[i].getCertificate(alias[i])));
			} catch (NormalizeCertificateException e) {
				logger.info("[PKCS7Signature.sign]::El certificado de la firma no ha podido ser normalizado a un formato reconocido " +
						"por el proveedor criptográfico de Arangi ", e);
				throw new SignatureException ("El certificado de la firma no ha podido ser normalizado a un formato reconocido por el " +
						"proveedor criptográfico de Arangi ", e);
			}
		}
		byte[][] signaturesBytes = (byte[][])alSignatureBytes.toArray(new byte[0][0]);
		Certificate [] certificates = (Certificate[]) alCertificates.toArray (new Certificate[0]);
		
		//-- Obtener la firma PKCS#7
		byte[] pkcs7Signature;
		if (isAttached) {
			pkcs7Signature = createPKCS7CMS (signaturesBytes, certificates, document, digitalSignatureAlgorithms, OID_FORMATO_FIRMA);
		} else {
			pkcs7Signature = createPKCS7CMS (signaturesBytes, certificates, null, digitalSignatureAlgorithms, OID_FORMATO_FIRMA);
		}
		return new PKCS7Signature (pkcs7Signature, certificates, document, isAttached);
	}

	/*
	 * (non-Javadoc)
	 * @see es.accv.arangi.base.signature.CMSPKCS7Signature#getOIDFormatoFirma()
	 */
	protected String getOIDFormatoFirma() {
		return OID_FORMATO_FIRMA;
	}

	/**
	 * Devuelve una cadena de texto con el tipo de la firma
	 * 
	 * @return Cadena de texto con el tipo de la firma
	 */
	public String getSignatureType () {
		return "PKCS#7";
	}

}

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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.cert.CertificateEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.bouncycastle.asn1.x509.KeyPurposeId;

import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPKCS7;
import com.itextpdf.text.pdf.PdfReader;

import es.accv.arangi.base.certificate.Certificate;
import es.accv.arangi.base.document.IDocument;
import es.accv.arangi.base.exception.CertificateException;
import es.accv.arangi.base.exception.certificate.NormalizeCertificateException;
import es.accv.arangi.base.exception.document.HashingException;
import es.accv.arangi.base.exception.signature.SignatureException;
import es.accv.arangi.base.util.Util;
import es.accv.arangi.base.util.validation.ValidationResult;

/**
 * @author <a href="mailto:jgutierrez@accv.es">José M Gutiérrez</a>
 *
 */
public abstract class BasePDFSignature extends Signature {

	static Logger logger = Logger.getLogger(BasePDFSignature.class);
	
	/*
	 * Constante con el texto de "firmado por" en firmas visibles 
	 */
	protected static final String DIGITALLY_SIGNED_TEXT = "Firmado digitalmente por";

	/*
	 * Constante con el texto de "estado" en firmas visibles 
	 */
	protected static final String STATUS_TEXT = "Estado";

	/**
	 * Objeto PDF
	 */
	protected File pdfFile;
	
	/**
	 * Flag para indicar si se desea borrar el fichero al final
	 */
	protected static boolean deleteFileWhenFinalize = true;
	
	//-- Método para finalizar los objetos que borra el fichero temporal
	@Override
	protected void finalize() throws Throwable {
		super.finalize();
		
		if (deleteFileWhenFinalize) {
			logger.debug("[BasePDFSignature.finalize]::Eliminando el fichero temporal: " + pdfFile);
			pdfFile.delete();
			logger.debug("[BasePDFSignature.finalize]::Fichero temporal eliminado: " + pdfFile);
		}
	}
	
	//-- Métodos implementados de Signature
	
	/* (non-Javadoc)
	 * @see es.accv.arangi.base.signature.ISignature#getCertificates()
	 */
	public Certificate[] getCertificates() {
		logger.debug("[PDFSignature.getCertificates]::Entrada");
		
		PdfReader reader;
		try {
			reader = new PdfReader(this.pdfFile.getAbsolutePath());
		} catch (IOException e) {
			// El fichero ya pasó la validación
			logger.info("[PDFSignature.getCertificates]::No se puede leer el contenido de este objeto", e);
			return null;
		}
		AcroFields af = reader.getAcroFields();
		List<String> names = getRealSignatureNames(af);
		List<Certificate> result = new ArrayList<Certificate>(); 
		for (int i = 0; i < names.size(); i++) {
			String name = (String)names.get(i);
			
			//-- Validar que el PKCS#7 se corresponde con el documento
			PdfPKCS7 pkcs7 = af.verifySignature(name, CRYPTOGRAPHIC_PROVIDER_NAME);
			try {
				Certificate signingCertificate = getSigningCertificate(pkcs7.getCertificates());
				if (signingCertificate != null) {
					result.add(getSigningCertificate (pkcs7.getCertificates()));
				}
			} catch (NormalizeCertificateException e) {
				logger.info("[PDFSignature.getCertificates]::No se puede normalizar el certificado::" + pkcs7.getCertificates()[0], e);
			} 
		}
		
		reader.close();
		
		return result.toArray(new Certificate[0]);
	}

	/* (non-Javadoc)
	 * @see es.accv.arangi.base.signature.ISignature#isValidSignatureOnly()
	 */
	public ValidationResult[] isValidSignatureOnly() throws SignatureException {
		
		logger.debug("[BasePDFSignature.isValidSignatureOnly]::Entrada");
		
		PdfReader reader;
		try {
			reader = new PdfReader(this.pdfFile.getAbsolutePath());
		} catch (IOException e) {
			// El fichero ya pasó la validación
			logger.info("[BasePDFSignature.isValidSignatureOnly]::No se puede leer el contenido de este objeto", e);
			return null;
		}
		AcroFields af = reader.getAcroFields();
		ArrayList names = getRealSignatureNames(af);
		ValidationResult[] result = new ValidationResult [names.size()]; 
		for (int i = 0; i < names.size(); i++) {
			String name = (String)names.get(i);
			PdfPKCS7 pkcs7 = af.verifySignature(name, CRYPTOGRAPHIC_PROVIDER_NAME);
			try {
				if (pkcs7.verify()) {
					result [i] = new ValidationResult (pkcs7.getSigningCertificate());
				} else {
					result [i] = new ValidationResult (ValidationResult.RESULT_SIGNATURE_NOT_MATCH_DATA, pkcs7.getSigningCertificate());
				}
			} catch (java.security.SignatureException e) {
				logger.info("[BasePDFSignature.isValidSignatureOnly]::Error en una de las firmas del PDF", e);
				throw new SignatureException ("Error en una de las firmas del PDF", e);
			}
		}
		
		return result;
	}

	/**
	 * En el caso de los PDF no tiene sentido realizar la validación sobre un 
	 * documento que no sea el mismo PDF. Por ello, el resultado de este método 
	 * será igual a llamar a {@link #isValidSignatureOnly() isValidSignatureOnly} 
	 * sin parámetros. 
	 */
	public ValidationResult[] isValidSignatureOnly(IDocument document)
			throws HashingException, SignatureException {
		return isValidSignatureOnly();
	}
	
	/**
	 * Guarda la firma en disco
	 * 
	 * @param file Fichero donde se guardará la firma
	 * @throws IOException Errores de entrada / salida
	 */
	public void save (File file) throws IOException {
		logger.debug ("[PDFSignature.save]::Entrada::" + file);
		
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(this.pdfFile);
			Util.saveFile(file, fis);
		} finally {
			if (fis != null) {
				fis.close();
			}
		}
	}
	
	/**
	 * Guarda la firma en un stream de escritura.
	 * 
	 * @param out Stream de escritura
	 * @throws IOException Errores de entrada / salida
	 */
	public void save (OutputStream out) throws IOException {
		logger.debug ("[PDFSignature.save]::Entrada::" + out);
		
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(this.pdfFile);
			Util.save(out, fis);
		} finally {
			if (fis != null) {
				fis.close();
			}
		}
	}
	
	/**
	 * Devuelve el PDF en forma de array de bytes
	 * 
	 * @return Array de bytes con el contenido del PDF
	 * @throws IOException 
	 */
	public byte[] toByteArray () throws IOException {
		return Util.loadFile(this.pdfFile);
	}
	
	/**
	 * Método que obtiene un objeto de firma PDF o PADES-LTV en base
	 * a la firma que se pasa como parámetro. 
	 * 
	 * @param signature Firma PDF o PADES-LTV
	 * @return Objeto de firma PDF adecuado a la firma pasada como parámetro
	 * @throws SignatureException La firma no parece ser un PDF
	 */
	public static BasePDFSignature getPDFObject (byte[] signature) throws SignatureException {
		return getPDFObject(new ByteArrayInputStream(signature));
	}
	
	/**
	 * Método que obtiene un objeto de firma PDF o PADES-LTV en base
	 * a la firma que se pasa como parámetro. 
	 * 
	 * @param signature Firma PDF o PADES-LTV
	 * @return Objeto de firma PDF adecuado a la firma pasada como parámetro
	 * @throws SignatureException La firma no parece ser un PDF
	 * @throws FileNotFoundException El fichero no existe
	 */
	public static BasePDFSignature getPDFObject (File signature) throws SignatureException, FileNotFoundException {
		return getPDFObject(new FileInputStream(signature));
	}
	
	/**
	 * Método que obtiene un objeto de firma PDF o PADES-LTV en base
	 * a la firma que se pasa como parámetro. 
	 * 
	 * @param signature Firma PDF o PADES-LTV
	 * @return Objeto de firma PDF adecuado a la firma pasada como parámetro
	 * @throws SignatureException La firma no parece ser un PDF
	 */
	public static BasePDFSignature getPDFObject (InputStream signature) throws SignatureException {
		logger.debug("[BasePDFSignature.getPDFObject]::Entrada::" + signature);
		
		//-- Guardar en un fichero temporal
		FileOutputStream fos = null;
		File fileTemp;
		try {
			fileTemp = getFileTemp ();
            Util.saveFile(fileTemp, signature);
		} catch (IOException e) {
			logger.info("[PAdESLTVSignature(byte[])::No se puede crear el fichero temporal o no se puede escribir en él", e);
			throw new SignatureException ("No se puede crear el fichero temporal o no se puede escribir en él", e);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					logger.info("[PAdESLTVSignature(byte[])::No se puede cerrar el stream de lectura al fichero temporal", e);
					throw new SignatureException ("No se puede cerrar el stream de lectura al fichero temporal", e);
				}
			}
		}
		
		PdfReader reader;
		try {
			reader = new PdfReader(fileTemp.getAbsolutePath());
		} catch (IOException e) {
			// El fichero ya pasó la validación
			logger.info("[BasePDFSignature.getPDFObject]::La firma no parece ser un PDF", e);
			throw new SignatureException("La firma no parece ser un PDF", e);
		}
		
		try {
			AcroFields af = reader.getAcroFields();
			ArrayList names = af.getSignatureNames();
			if (names == null || names.isEmpty()) {
				logger.info("[BasePDFSignature.getPDFObject]::El documento PDF no está firmado");
				throw new SignatureException ("El documento PDF no está firmado");
			}
			if (names.size() == 1) {
				logger.debug("[BasePDFSignature.getPDFObject]::El documento PDF no es PAdES-LTV: sólo hay una firma y debería haber firma y sello de tiempos");
				return new PDFSignature(fileTemp);
			}
			
			//-- Comprobar que existe el diccionario DSS
			if (reader.getCatalog().get(new PdfName("DSS")) == null) {
				logger.debug("[BasePDFSignature.getPDFObject]::El documento PDF no es PAdES-LTV: no contiene un diccionario Document Security Store (DSS)");
				return new PDFSignature(fileTemp);
			}
			
			//-- Comprobar que existe el sello de tiempos para el documento
			boolean encontrado = false;
			for (Iterator iterator = names.iterator(); iterator.hasNext();) {
				String name = (String) iterator.next();
				if (af.getSignatureDictionary(name) != null && af.getSignatureDictionary(name).get(PdfName.SUBFILTER) != null &&
						af.getSignatureDictionary(name).get(PdfName.SUBFILTER).equals(new PdfName("ETSI.RFC3161"))) {
					encontrado = true;
					break;
				}
			}
			if (!encontrado) {
				logger.debug("[BasePDFSignature.getPDFObject]::El documento PDF no es PAdES-LTV: no contiene un sello de tiempos para todo el documento)");
				return new PDFSignature(fileTemp);
			} else {
				return new PAdESLTVSignature(fileTemp);
			}
			
		} catch (Exception e) {
			logger.info("[BasePDFSignature.getPDFObject]::Ha ocurrido un error cargando el objeto", e);
			throw new SignatureException ("El documento PDF no está firmado");
		}
	}
	
	/**
	 * Método para poder validar con el método Signature.validateSignature.<br><br>
	 * 
	 * Analiza el parámetro y, si se trata de un objeto PDF, devuelve un 
	 * objeto del tipo adecuado.
	 * 
	 * @param bSignature Firma como array de bytes
	 * @return PDF o PAdES
	 * @throws Exception El parámetro no es un PDF
	 */
	public static ISignature getSignatureInstance (byte[] bSignature) throws Exception {
		return getPDFObject(bSignature);
	}
	
	/**
	 * Método que se encargará de borrar el fichero temporal generado por
	 * este objeto.
	 */
	public void close() {
		logger.debug("[BasePDFSignature.close]::Eliminando el fichero temporal: " + pdfFile);
		pdfFile.delete();
		logger.debug("[BasePDFSignature.close]::Fichero temporal eliminado: " + pdfFile);
	}
	
	/**
	 * Marca un flag que determina si el fichero temporal de la firma debe
	 * ser borrado automáticamente al finalizar la vida de este objeto
	 * 
	 * @param deleteFileWhenFinalize Flag
	 */
	public static void setDeleteFileWhenFinalize (boolean deleteFileWhenFinalize) {
		BasePDFSignature.deleteFileWhenFinalize = deleteFileWhenFinalize;
	}
	
	//-- Métodos protected
	
	/*
	 * Se pasa una cadena de certificación, se devolverá el que no pertenece a una CA.
	 */
	protected static Certificate getSigningCertificate(java.security.cert.Certificate[] arrayCertificates) throws NormalizeCertificateException {
		
		//-- Comprobar que no sea un autofirmado
		if (arrayCertificates.length == 1) {
			Certificate cert;
			try {
				cert = new Certificate (Util.getCertificate(arrayCertificates[0].getEncoded()));
			} catch (CertificateEncodingException e) {
				throw new NormalizeCertificateException("No es posible obtener el certificado", e);
			}
			if (cert.isSelfSigned()) {
				logger.debug("El certificado de firma es un autofirmado");
				return cert;
			}
		}
		
		//-- No es un autofirmado
		HashMap<String, Certificate> hmCertificates = new HashMap<String, Certificate>();
		Certificate[] certificates = new Certificate[arrayCertificates.length];
		for (int i = 0; i < arrayCertificates.length; i++) {
			try {
				certificates[i] = new Certificate (Util.getCertificate(arrayCertificates[i].getEncoded()));
			} catch (CertificateEncodingException e) {
				throw new NormalizeCertificateException("No es posible obtener el certificado", e);
			}
			hmCertificates.put(certificates[i].getSubjectDN(), certificates[i]);
		}
		
		//-- Se mira el padre de todos y se quitan los padres del map
		for (int i = 0; i < certificates.length; i++) {
			hmCertificates.remove(certificates[i].getIssuerDN());
			
			//-- Si el certificado es de OCSP también hay que quitarlo
			try {
				List<String>  extendedKeyUsages = certificates[i].getExtendedKeyUsage();
				if (extendedKeyUsages.contains(KeyPurposeId.id_kp_OCSPSigning.getId())) {
					hmCertificates.remove(certificates[i].getSubjectDN());
				}
			} catch (CertificateException e) {
				logger.debug("No es posible obtener los usos de clave extendidos del certificado: " + certificates[i].getCommonName());
			}
		}
		
		//-- Si no queda ninguno devolver null
		if (hmCertificates.isEmpty()) {	
			return null;
		}
		
		return hmCertificates.get(hmCertificates.keySet().iterator().next());
	}

	/**
	 * Devuelve los nombres de los campos de firma. No se incluyen los nombres
	 * de los campos de firma que realmente son sellos de tiempo para el documento.
	 * 
	 * @param af AcroFields
	 * @return Nombres de los campos reales de firma
	 */
	protected static ArrayList<String> getRealSignatureNames(AcroFields af) {
		
		ArrayList<String> originalNames = af.getSignatureNames();
		ArrayList<String> realNames = new ArrayList<String>();
		for (int i = originalNames.size() - 1; i > -1; i--) {
			String name = (String) originalNames.get(i);
			if (af.getSignatureDictionary(name).get(PdfName.SUBFILTER) == null ||
					!af.getSignatureDictionary(name).get(PdfName.SUBFILTER).equals(new PdfName("ETSI.RFC3161"))) {
				realNames.add(name);
			}

		}
		
		return realNames;
	}


	/*
	 * Obtiene un fichero temporal en el directorio temporal de Arangi
	 */
	protected static File getFileTemp() throws IOException {
		File temp = File.createTempFile("signed", "pdf", getArangiTemporalFolder());
		temp.deleteOnExit();
		
		return temp;
	}


}

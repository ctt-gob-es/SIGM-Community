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
import java.net.URL;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;

import org.apache.log4j.Logger;

import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.AcroFields;
import com.itextpdf.text.pdf.PdfDate;
import com.itextpdf.text.pdf.PdfDictionary;
import com.itextpdf.text.pdf.PdfDocumentSecurityStore;
import com.itextpdf.text.pdf.PdfName;
import com.itextpdf.text.pdf.PdfPKCS7;
import com.itextpdf.text.pdf.PdfReader;
import com.itextpdf.text.pdf.PdfSigGenericPKCS;
import com.itextpdf.text.pdf.PdfSignature;
import com.itextpdf.text.pdf.PdfSignatureAppearance;
import com.itextpdf.text.pdf.PdfStamper;
import com.itextpdf.text.pdf.PdfString;
import com.itextpdf.text.pdf.TSAClient;
import com.itextpdf.text.pdf.TSAClientBouncyCastle;

import es.accv.arangi.base.algorithm.DigitalSignatureAlgorithm;
import es.accv.arangi.base.algorithm.HashingAlgorithm;
import es.accv.arangi.base.certificate.Certificate;
import es.accv.arangi.base.certificate.validation.CAList;
import es.accv.arangi.base.certificate.validation.CRL;
import es.accv.arangi.base.certificate.validation.CertificateValidator;
import es.accv.arangi.base.certificate.validation.OCSPClient;
import es.accv.arangi.base.certificate.validation.OCSPResponse;
import es.accv.arangi.base.certificate.validation.ValidateCertificate;
import es.accv.arangi.base.certificate.validation.service.CertificateValidationService;
import es.accv.arangi.base.device.DeviceManager;
import es.accv.arangi.base.document.IDocument;
import es.accv.arangi.base.document.InputStreamDocument;
import es.accv.arangi.base.exception.certificate.CRLParsingException;
import es.accv.arangi.base.exception.certificate.CertificateCANotFoundException;
import es.accv.arangi.base.exception.certificate.NormalizeCertificateException;
import es.accv.arangi.base.exception.certificate.validation.MalformedOCSPResponseException;
import es.accv.arangi.base.exception.device.AliasNotFoundException;
import es.accv.arangi.base.exception.device.LoadingObjectException;
import es.accv.arangi.base.exception.device.SearchingException;
import es.accv.arangi.base.exception.document.HashingException;
import es.accv.arangi.base.exception.signature.AlgorithmNotSuitableException;
import es.accv.arangi.base.exception.signature.InvalidCertificateException;
import es.accv.arangi.base.exception.signature.PDFDocumentException;
import es.accv.arangi.base.exception.signature.RetrieveOCSPException;
import es.accv.arangi.base.exception.signature.SignatureException;
import es.accv.arangi.base.exception.signature.SignatureNotFoundException;
import es.accv.arangi.base.exception.timestamp.MalformedTimeStampException;
import es.accv.arangi.base.exception.timestamp.ResponseTimeStampException;
import es.accv.arangi.base.exception.timestamp.TimeStampServerConnectionException;
import es.accv.arangi.base.timestamp.TimeStamp;
import es.accv.arangi.base.timestamp.TimeStampRequestParameters;
import es.accv.arangi.base.util.Util;
import es.accv.arangi.base.util.validation.ValidationResult;

/**
 * Clase para manejar firmas en PDF (PAdES-A) según los estándares 
 * <a href="http://www.etsi.org/deliver/etsi_ts/102700_102799/10277804/01.01.01_60/ts_10277804v010101p.pdf" target="estandar">ETSI TS 102 778-4 V1.2.1</a> 
 * y <a href="http://www.telecomforum.eu/deliver/etsi_ts/119100_119199/11914402/01.01.01_60/ts_11914402v010101p.pdf" target="estandar">ETSI TS 119 144-2 V1.1.1</a>,
 * basado en la norma <a href="http://www.iso.org/iso/catalogue_detail.htm?csnumber=51502" target="estandar">ISO 32000-1</a><br><br>
 * 
 * Por motivos de compatibilidad la clase sigue llamándose PAdESLTVSignature 
 * aunque sería más correcto llamarla PAdESASignature, ya que siempre se
 * trabajará con sellos de tiempo de documento, que son lo que diferencia
 * a un PAdES-LTV de un PAdES-A según la norma ETSI TS 119 144-2.<br><br>
 * 
 * La norma ETSI TS 119 144-2 exige añadir más información de validación que
 * la que se establecía en la ETSI TS 102 778-4. Por ello, las firmas realizadas 
 * con versiones de Arangí anteriores a la 1.1.4 no serán consideradas válidas por 
 * dicha versión o versiones posteriores. Para evitar este incoveniente a aquellos 
 * desarrollos que se hayan realizado con versiones antiguas es posible activar un 
 * flag estático antes de la validación que permitirá que en las validaciones no 
 * se tenga en cuenta la información extra exigida por la nueva norma:<br><br>
 * 
 * <code>
 * PAdESLTVSignature.olderVersionsAllowed = true;
 * </code><br><br>
 * 
 * Este tipo de firmas cumplen con los requisitos para ser firmas longevas. En 
 * ellas se incluye toda la información necesaria para validar los certificados
 * de las firmas. También se añade un sello de tiempos para el documento que
 * da garantias sobre la fecha en la que se realizaron las firmas.<br><br>
 * 
 * Las últimas versiones de Adobe Acrobat ya utilizan este tipo de firmas, aunque 
 * existen algunas diferencias entre las firmas PAdES-LTV obtenidas por Adobe y las
 * normas de la ETSI. En la documentación de Arangí debe haber una explicación de
 * cuales son estas diferencias.<br><br>
 * 
 * Para evitar problemas de saturación de memoria con ficheros PDF muy grandes,
 * esta clase siempre trabajará sobre un objeto java.io.File. Si el objeto no se 
 * inicializa con un fichero se creará un archivo temporal en la carpeta temporal 
 * de Arangi: {@link #getArangiTemporalFolder() getArangiTemporalFolder}.<br><br>
 * 
 * Existen dos métodos para obtener una firma PAdES-LTV, dependiendo de si se desea 
 * una firma visible o invisible. En el caso de las firmas visibles hay que proporcionar 
 * al método las coordenadas de las esquinas inferior izquierda y superior derecha, así 
 * como el número de página donde se desea ubicar la firma. También es posible asociar
 * una imagen a la firma.<br><br>
 * 
 * <code>
 * KeyStoreManager manager = new KeyStoreManager (...,...);<br>
 * String alias = ...;<br>
 * ByteArrayDocument documentPDF = new ByteArrayDocument (...);<br>
 * URL urlTSA = new URL (...);<br>
 * CAList caList = new CAList (...);<br><br>
 * 
 * //-- Firma invisible<br>
 * PAdESLTVSignature signatureInv = PAdESLTVSignature.sign (new KeyStoreManager[] {manager}, new String[] {alias}, documentPDF, urlTSA, caList, "Porque quiero firmarlo");<br><br>
 * 
 * //-- Firma visible<br>
 * PAdESLTVSignature signatureVis = PAdESLTVSignature.sign (new KeyStoreManager[] {manager}, new String[] {alias},documentPDF, urlTSA, caList, "Porque quiero firmarlo",
 *      true, Util.readStream(ClassLoader.getSystemResourceAsStream("signature/chip.gif")), 100, 100, 300, 200, 1);<br><br>
 * </code>
 * 
 * En la página 1 de la segunda firma, en la ubicación indicada por las coordenadas, se 
 * verá la imagen chip.gif como una firma realizada sobre el PDF.<br><br>
 * 
 * El primer parámetro de la firma es un array de managers, ya que es posible realizar 
 * varias firmas a la vez. Es importante destacar que una vez se han añadido los campos 
 * que hacen de un PDF firmado un PAdES-LTV ya no es posible volver a firmar el documento. 
 * Si un documento se va firmar por varias personas de forma no simultanea lo que se debe 
 * hacer es realizar las firmas en un PDF con firma simple y, tras la última firma 
 * completar a PAdES-LTV. Por supuesto, entre el principio y el fin del proceso ninguno 
 * de los certificados implicados podrá caducar o ser revocado.<br><br>
 * 
 * <code>
 * URL urlTSA = new URL (...);<br>
 * CAList caList = new CAList (...);<br><br>

 * //-- Primera firma<br>
 * KeyStoreManager manager1 = new KeyStoreManager (...,...);<br>
 * ByteArrayDocument documentPDF = new ByteArrayDocument (...);<br>
 * String alias1 = ...;<br>
 * PDFSignature signature = PDFSignature.sign (new KeyStoreManager[] {manager1}, new String[] {alias1}, documentPDF, urlTSA, caList, "Firma 1");<br><br>
 * 
 * //-- Segunda firma (días más tarde)<br>
 * KeyStoreManager manager2 = new KeyStoreManager (...,...);<br>
 * String alias2 = ...;<br>
 * documentPDF = new ByteArrayDocument (signature.toByteArray());<br>
 * signature = PDFSignature.sign (new KeyStoreManager[] {manager2}, new String[] {alias2}, documentPDF, urlTSA, caList, "Firma 2");<br><br>
 * 
 * //-- Completar la firma para que sea PAdES-LTV<br>
 * PAdESLTVSignature padesLTV = PAdESLTVSignature.completeToPAdESLTV(signature, urlTSA, caList);<br><br>
 * </code><br><br>
 * 
 * La validez de una firma longeva se halla limitada a la vida del certificado del último sello 
 * de tiempos de documento que contiene el PDF, aunque erroneamente se suele pensar que una firma 
 * longeva puede validarse eternamente. Por ejemplo, el certificado de la TSA de la ACCV caducará 
 * el 18 de Noviembre de 2016, lo que implica que a partir de esa fecha las firmas PAdES-LTV 
 * realizadas con la TSA de la ACCV dejarán de ser válidas. Con el objeto de alargar la vida de
 * una firma longeva será necesario realizar un resellado de la misma cuando se cambie el certificado
 * de la TSA. Este sería el código para realizar un resellado: <br><br>
 * 
 * <code>
 * URL urlTSA = new URL (...);<br>
 * CAList caList = new CAList (...);<br><br>
 * ByteArrayDocument document = new ByteArrayDocument (...);<br>
 * PAdESLTVSignature signature = new PAdESLTVSignature(document);<br>
 * signature.addDocumentTimeStamp(urlTSA, caList);<br>
 * signature.save(...);<br>
 * </code>
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José M Gutiérrez</a>
 */
public class PAdESLTVSignature extends BasePDFSignature {

	/*
	 * Constante para incluir como motivo de la firma cuando éste no es pasado 
	 * como parámetro
	 */
	private static final String DEFAULT_SIGNATURE_REASON = "Firma realizada con Arangi (ACCV)";

    private final static boolean embedOcspInSignature = false;

    private static final String HASH_ALGORITHM = embedOcspInSignature ?"SHA256" : "SHA1";
    
    /**
     * Si es cierto en las validaciones se permitirán firmas PAdES-LTV realizadas
     * con versiones de Arangí anteriores a la 1.1.4. Por defecto tiene un valor
     * de falso.
     */
    public static boolean olderVersionsAllowed = false; 

	/*
	 * Logger de la clase 
	 */
	static Logger logger = Logger.getLogger(PAdESLTVSignature.class);
	
	//-- Constructores
	
	/**
	 * Inicializa el objeto con un fichero PDF firmado.
	 * 
	 * @param pdfFile Fichero PDF firmado
	 * @throws PDFDocumentException El fichero no es un PDF correcto o bien no puede 
	 * 	ser leído
	 * @throws SignatureNotFoundException El fichero es un PDF pero no está firmado
	 */
	public PAdESLTVSignature (File pdfFile) throws PDFDocumentException, SignatureNotFoundException {
		initialize(pdfFile);
	}
	
	/**
	 * Inicializa el objeto con el contenido de un fichero PDF firmado.
	 * 
	 * @param pdfContentBytes Array de bytes con el contenido del fichero PDF firmado
	 * @throws PDFDocumentException El fichero no es un PDF correcto o bien no puede 
	 * 	ser leído
	 * @throws SignatureNotFoundException El fichero es un PDF pero no está firmado
	 * @throws IOException No se puede crear el fichero temporal
	 */
	public PAdESLTVSignature (byte[] pdfContentBytes) throws PDFDocumentException, SignatureNotFoundException, IOException {
		//-- Guardar en un fichero temporal
		File fileTemp;
		try {
			fileTemp = getFileTemp ();
			Util.saveFile(fileTemp, pdfContentBytes);
		} catch (IOException e) {
			logger.info("[PAdESLTVSignature(byte[])::No se puede crear el fichero temporal o no se puede escribir en él", e);
			throw e;
		}
		
		initialize(fileTemp);
	}
	
	/**
	 * Inicializa el objeto con un stream de lectura al contenido de un fichero PDF firmado.
	 * 
	 * @param streamPDF Stream de lectura al contenido del fichero PDF firmado
	 * @throws PDFDocumentException El fichero no es un PDF correcto o bien no puede 
	 * 	ser leído
	 * @throws SignatureNotFoundException El fichero es un PDF pero no está firmado
	 * @throws IOException No se puede crear el fichero temporal
	 */
	public PAdESLTVSignature (InputStream streamPDF) throws PDFDocumentException, SignatureNotFoundException, IOException {
		//-- Guardar en un fichero temporal
		FileOutputStream fos = null;
		File fileTemp;
		try {
			fileTemp = getFileTemp ();
			Util.saveFile(fileTemp, streamPDF);
		} catch (IOException e) {
			logger.info("[PAdESLTVSignature(byte[])::No se puede crear el fichero temporal o no se puede escribir en él", e);
			throw e;
		} finally {
			if (fos != null) {
				fos.close();
			}
		}
		
		//-- Inicializar
		initialize(fileTemp);
	}
	
	/**
	 * Inicializa el objeto con un documento que debe contener un fichero PDF firmado.
	 * 
	 * @param document Documento con el contenido del fichero PDF firmado
	 * @throws PDFDocumentException El fichero no es un PDF correcto o bien no puede 
	 * 	ser leído
	 * @throws SignatureNotFoundException El fichero es un PDF pero no está firmado
	 * @throws IOException No se puede crear el fichero temporal
	 */
	public PAdESLTVSignature (IDocument document) throws PDFDocumentException, SignatureNotFoundException, IOException {
		//-- Guardar en un fichero temporal
		FileOutputStream fos = null;
		InputStream fis = null;
		File fileTemp;
		try {
			fileTemp = getFileTemp ();
			fis = document.getInputStream();
			Util.saveFile(fileTemp, fis);
		} catch (IOException e) {
			logger.info("[PAdESLTVSignature(byte[])::No se puede crear el fichero temporal o no se puede escribir en él", e);
			throw e;
		} finally {
			if (fos != null) {
				fos.close();
			}
			if (fis != null) {
				fis.close();
			}
		}
		
		//-- Inicializar
		initialize(fileTemp);
	}
	
	/*
	 * Inicializa el objeto con un documento igual.
	 */
	protected PAdESLTVSignature (PAdESLTVSignature signature) {
		this.pdfFile = signature.pdfFile;
	}
	
	//-- Métodos estáticos
	
	/**
	 * Obtiene un objeto {@link PAdESLTVSignature} tras firmar un documento PDF.
	 * La firma es invisible.
	 * 
	 * @param managers Dispositivos criptográfico que realizarán la firma
	 * @param alias Alias donde se encuentran las claves privada dentro de los dispositivos
	 * @param pdfDocument Documento PDF a firmar
	 * @param urlTimestamp URL del servidor de sello de tiempos
	 * @param caList Lista de certificados de CA, uno de ellos ha de ser el emisor del 
	 * 	certificado con el que se realiza la firma
	 * @param reason Texto que aparecerá junto a la firma como razón. Si se pasa un valor
	 * 	nulo se escribirá un texto por defecto.
	 * @return Documento PDF firmado, con sello de tiempos y respuesta OCSP
	 * @throws AliasNotFoundException El alias donde se encuentra la clave privada usada para
	 * 	realizar la firma no existe
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada usada para
	 * 	realizar la firma
	 * @throws PDFDocumentException El documento no es un fichero PDF o es un PDF mal formado
	 * @throws SignatureException No se puede realizar la firma
	 * @throws RetrieveOCSPException No es posible obtener una respuesta OCSP para
	 * 	asociarla a la firma
	 * @throws HashingException Excepción obteniendo el hash que será sellado por la TSA
	 * @throws CertificateCANotFoundException La lista de certificado de CA no contiene el 
	 * 	emisor del certificado de firma o existe pero tiene un formato no normalizable por 
	 * 	el proveedor criptográfico de Arangi
	 * @throws InvalidCertificateException El certificado con el que se firma está revocado
	 * @throws NormalizeCertificateException Alguno de los certificados de firma o de sus cadenas
	 * 	de certificación no puede ser normalizado
	 */
	public static PAdESLTVSignature sign (DeviceManager[] managers, String[] alias, IDocument pdfDocument, URL urlTimestamp, 
			CAList caList, String reason) throws AliasNotFoundException, LoadingObjectException, PDFDocumentException, SignatureException, RetrieveOCSPException, HashingException, CertificateCANotFoundException, InvalidCertificateException, NormalizeCertificateException {
		try {
			return sign (managers, alias, pdfDocument, null, urlTimestamp, null, null, caList, reason, false, null, -1, -1, -1, -1, 0);
		} catch (AlgorithmNotSuitableException e) {
			logger.info("El algoritmo por defecto no debería provocar este error", e);
			throw new SignatureException("El algoritmo por defecto no debería provocar este error", e);
		}
	}
	
	/**
	 * Obtiene un objeto {@link PAdESLTVSignature} tras firmar un documento PDF.
	 * El servidor de la TSA requiere autenticación y la firma obtenida es invisible.
	 * 
	 * @param managers Dispositivos criptográfico que realizarán la firma
	 * @param alias Alias donde se encuentran las claves privada dentro de los dispositivos
	 * @param pdfDocument Documento PDF a firmar
	 * @param urlTimestamp URL del servidor de sello de tiempos
	 * @param userTSA Usuario para acceder al servidor de sello de tiempos. Nulo si no
	 * 	requiere autenticación.
	 * @param passwordTSA Contraseña para acceder al servidor de sello de tiempos. Nula si 
	 * no requiere autenticación.
	 * @param caList Lista de certificados de CA, uno de ellos ha de ser el emisor del 
	 * 	certificado con el que se realiza la firma
	 * @param reason Texto que aparecerá junto a la firma como razón. Si se pasa un valor
	 * 	nulo se escribirá un texto por defecto.
	 * @return Documento PDF firmado, con sello de tiempos y respuesta OCSP
	 * @throws AliasNotFoundException El alias donde se encuentra la clave privada usada para
	 * 	realizar la firma no existe
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada usada para
	 * 	realizar la firma
	 * @throws PDFDocumentException El documento no es un fichero PDF o es un PDF mal formado
	 * @throws SignatureException No se puede realizar la firma
	 * @throws RetrieveOCSPException No es posible obtener una respuesta OCSP para
	 * 	asociarla a la firma
	 * @throws HashingException Excepción obteniendo el hash que será sellado por la TSA
	 * @throws CertificateCANotFoundException La lista de certificado de CA no contiene el 
	 * 	emisor del certificado de firma o existe pero tiene un formato no normalizable por 
	 * 	el proveedor criptográfico de Arangi
	 * @throws InvalidCertificateException El certificado con el que se firma está revocado
	 * @throws NormalizeCertificateException Alguno de los certificados de firma o de sus cadenas
	 * 	de certificación no puede ser normalizado
	 */
	public static PAdESLTVSignature sign (DeviceManager[] managers, String[] alias, IDocument pdfDocument, URL urlTimestamp, 
			String userTSA, String passwordTSA, CAList caList, String reason) throws AliasNotFoundException, LoadingObjectException, PDFDocumentException, SignatureException, RetrieveOCSPException, HashingException, CertificateCANotFoundException, InvalidCertificateException, NormalizeCertificateException {
		try {
			return sign (managers, alias, pdfDocument, null, urlTimestamp, userTSA, passwordTSA, caList, reason, false, null, -1, -1, -1, -1, 0);
		} catch (AlgorithmNotSuitableException e) {
			logger.info("El algoritmo por defecto no debería provocar este error", e);
			throw new SignatureException("El algoritmo por defecto no debería provocar este error", e);
		}
	}
	
	/**
	 * Obtiene un objeto {@link PAdESLTVSignature} tras firmar un documento PDF.<br><br>
	 * 
	 * Si la firma es visible se le puede asociar una imagen. El punto 0,0 de la página 
	 * se encuentra en la esquina inferior izquierda de la misma. Un página tiene 
	 * aproximadamente unas dimensiones de 580x850. 
	 * 
	 * @param managers Dispositivos criptográfico que realizarán la firma
	 * @param alias Alias donde se encuentran las claves privada dentro de los dispositivos
	 * @param pdfDocument Documento PDF a firmar
	 * @param digitalSignatureAlgorithm Algoritmo de firma (si nulo algoritmo por defecto)
	 * @param urlTimestamp URL del servidor de sello de tiempos
	 * @param userTSA Usuario para acceder al servidor de sello de tiempos. Nulo si no
	 * 	requiere autenticación.
	 * @param passwordTSA Contraseña para acceder al servidor de sello de tiempos. Nula si 
	 * no requiere autenticación.
	 * @param caList Lista de certificados de CA, uno de ellos ha de ser el emisor del 
	 * 	certificado con el que se realiza la firma
	 * @param reason Texto que aparecerá junto a la firma como razón. Si se pasa un valor
	 * 	nulo se escribirá un texto por defecto.
	 * @param isVisible Si tiene un valor cierto se creará una firma visible.
	 * @param image Imagen de la firma. Puede tener un valor nulo.
	 * @param llX Posición X de la esquina inferior izquierda de la firma en la página (caso de ser visible)
	 * @param llY Posición Y de la esquina inferior izquierda de la firma en la página (caso de ser visible) 
	 * @param urX Posición X de la esquina superior derecha de la firma en la página (caso de ser visible)
	 * @param urY Posición Y de la esquina superior derecha de la firma en la página (caso de ser visible)
	 * @param page Página en la que se situará la firma si ésta es visible (1 es la primera página)
	 * @return Documento PDF firmado, con sello de tiempos y respuesta OCSP
	 * @throws AliasNotFoundException El alias donde se encuentra la clave privada usada para
	 * 	realizar la firma no existe
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada usada para
	 * 	realizar la firma
	 * @throws PDFDocumentException El documento no es un fichero PDF o es un PDF mal formado
	 * @throws SignatureException No se puede realizar la firma
	 * @throws RetrieveOCSPException No es posible obtener una respuesta OCSP para
	 * 	asociarla a la firma
	 * @throws HashingException Excepción obteniendo el hash que será sellado por la TSA
	 * @throws CertificateCANotFoundException La lista de certificado de CA no contiene el 
	 * 	emisor del certificado de firma o existe pero tiene un formato no normalizable por 
	 * 	el proveedor criptográfico de Arangi
	 * @throws InvalidCertificateException El certificado con el que se firma está revocado
	 * @throws NormalizeCertificateException Alguno de los certificados de firma o de sus cadenas
	 * 	de certificación no puede ser normalizado
	 * @throws AlgorithmNotSuitableException El algoritmo de firma pasado no sirve para realizar la firma
	 */
	public static PAdESLTVSignature sign (DeviceManager[] managers, String[] alias, IDocument pdfDocument, 
			String digitalSignatureAlgorithm, URL urlTimestamp, String userTSA, String passwordTSA, 
			CAList caList, String reason, boolean isVisible, byte[] image, float llX, float llY, 
			float urX, float urY, int page) throws AliasNotFoundException, LoadingObjectException, PDFDocumentException, SignatureException, RetrieveOCSPException, HashingException, CertificateCANotFoundException, InvalidCertificateException, NormalizeCertificateException, AlgorithmNotSuitableException {
		
		logger.debug("[PAdESLTVSignature.sign]::Entrada::" + Arrays.asList (new Object [] { managers, alias, pdfDocument, digitalSignatureAlgorithm, urlTimestamp, userTSA, passwordTSA, caList, reason, new Boolean (isVisible), image, new Float (llX), new Float (llY), new Float (urX), new Float (urY), new Integer (page) }));
		
		FileOutputStream fos = null;
		try {
		
			//-- Comprobar parámetros
			if (reason == null) {
				reason = DEFAULT_SIGNATURE_REASON;
			}
			
			//-- Algoritmos
			if (digitalSignatureAlgorithm == null) {
				digitalSignatureAlgorithm = DigitalSignatureAlgorithm.getDefault();
			}
			String hashingAlgorithm = DigitalSignatureAlgorithm.getHashingAlgorithm(digitalSignatureAlgorithm);
			if (hashingAlgorithm == null) {
				logger.info("No se puede tratar el algoritmo de firma " + digitalSignatureAlgorithm);
				throw new AlgorithmNotSuitableException("No se puede tratar el algoritmo de firma " + digitalSignatureAlgorithm);
			}
	
			//-- Cliente TSA (de iText)
			TSAClient tsc = new TSAClientBouncyCastle(urlTimestamp.toString(), userTSA, passwordTSA);
			
			//-- Obtener el fichero temporal donde se dejará el resultado
			File fileResult1 = getFileTemp();
			Util.saveFile(fileResult1, pdfDocument.getInputStream());
			
			for (int i = 0; i < managers.length; i++) {
				
				//-- Obtener la clave privada
				PrivateKey pk = managers[i].getPrivateKey(alias[i]);
				if (pk == null) {
					logger.info("[PDFSignature.sign]::No se ha podido encontrar el alias o bien éste no contiene una clave privada");
					throw new AliasNotFoundException ("No se ha podido encontrar el alias o bien éste no contiene una clave privada");
				}
				
				//--Obtener y validar el certificado
				X509Certificate x509Certificate = managers[i].getCertificate(alias[i]);
				ValidateCertificate validateCertificate = new ValidateCertificate (x509Certificate, caList);
				int validationResult;
				if ((validationResult = validateCertificate.validate()) != ValidationResult.RESULT_VALID) {
					logger.info("[PDFSignature.sign]::El certificado de firma no es válido: " + validationResult);
					throw new InvalidCertificateException ("El certificado de firma no es válido: " + validationResult, validationResult);
				}
				
				//-- Cadena de confianza
				java.security.cert.Certificate[] chain = new java.security.cert.Certificate[] { x509Certificate, validateCertificate.getIssuerCertificate().toX509Certificate() };
		
				//-- Leer el PDF y crear la firma. Dependiendo de si el PDF ya
				//-- está firmado o no se crea el PdfStamper de una manera u otra
				PdfReader reader = new PdfReader(new FileInputStream(fileResult1));
				File fileResult2 = getFileTemp();
				fos = new FileOutputStream (fileResult2);
				AcroFields af = reader.getAcroFields();
				ArrayList names = af.getSignatureNames();
				PdfStamper stp;
				if (names == null || names.isEmpty()) {
					stp = PdfStamper.createSignature(reader, fos, '\0');
				} else {
					stp = PdfStamper.createSignature(reader, fos, '\0', null, true);
				}
				
				//-- Apariencia de la firma
				PdfSignatureAppearance sap = stp.getSignatureAppearance();
				sap.setCrypto(null, chain, null, PdfSignatureAppearance.SELF_SIGNED);
				sap.setReason(reason);
				if (isVisible) {
					sap.setVisibleSignature(new Rectangle(llX, llY, urX, urY), page, null);
					StringBuffer detail = new StringBuffer();
					detail.append(DIGITALLY_SIGNED_TEXT + ": " + validateCertificate.getCommonName() + "\n");
					SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm");
					detail.append("Fecha: " + sdf.format(new Date()) + "\n");
					if (reason != null && !reason.equals("")) {
						detail.append("Motivo: " + reason + "\n");
					}
					sap.setLayer2Text(detail.toString());
					if (image != null) {
						sap.setImage(Image.getInstance(image));
					}
				}
		
				//-- Cargar valores en el diccionario
				PdfSignature dic = new PdfSignature(PdfName.ADOBE_PPKLITE, new PdfName("adbe.pkcs7.detached"));
				dic.setReason(sap.getReason());
				dic.setLocation(sap.getLocation());
				dic.setContact(sap.getContact());
				dic.setDate(new PdfDate(sap.getSignDate()));
				sap.setCryptoDictionary(dic);
				sap.setExternalDigest(new byte[128], null, "RSA"); 
		
				int contentEstimated = 15000;
				HashMap exc = new HashMap();
				exc.put(PdfName.CONTENTS, new Integer(contentEstimated * 2 + 2));
				sap.preClose(exc);
		
				//-- Construir el PKCS#7 (firmando con el manager de Arangi)
				Calendar cal = Calendar.getInstance();
				PdfPKCS7 pkcs7 = new PdfPKCS7(null, chain, null, hashingAlgorithm, CRYPTOGRAPHIC_PROVIDER_NAME, false);
				byte hash[] = new InputStreamDocument (sap.getRangeStream()).getHash(hashingAlgorithm);
				byte sh[] = pkcs7.getAuthenticatedAttributeBytes(hash, cal, null);
				byte[] signatureBytes = managers[i].signDocument(new ByteArrayInputStream (sh), alias[i], digitalSignatureAlgorithm);
				pkcs7.setExternalDigest(signatureBytes, sh, "RSA"); 
				byte[] encodedSig = pkcs7.getEncodedPKCS7(hash, cal, tsc, hashingAlgorithm.getBytes());
				
				//-- Añadir la firma al diccionario
				if (contentEstimated + 2 < encodedSig.length) {
					logger.info("[PDFSignature.sign]::No se puede añadir la firma al PDF por falta de espacio en el mismo");
					throw new SignatureException ("No se puede añadir la firma al PDF por falta de espacio en el mismo");
				}
		
				byte[] paddedSig = new byte[contentEstimated];
				System.arraycopy(encodedSig, 0, paddedSig, 0, encodedSig.length);
		
				PdfDictionary dic2 = new PdfDictionary();
				dic2.put(PdfName.CONTENTS, new PdfString(paddedSig).setHexWriting(true));
				sap.close(dic2);
				
	            reader.close();
	            fos.close();
	            fos = null;
	            
	            fileResult1.delete();
	            fileResult1 = fileResult2;
	            
			}

			//-- Completar a PAdES-LTV)
			try {
				PdfReader reader = new PdfReader(new FileInputStream(fileResult1));
				return PAdESLTVSignature.completeToPAdESLTV(reader, urlTimestamp, userTSA, passwordTSA, hashingAlgorithm, caList);
			} catch (PDFDocumentException e) {
				//-- No se va a dar, el fichero se acaba de firmar
				return null;
			}
			
		} catch (LoadingObjectException e) {
			logger.info("[PAdESLTVSignature.sign]::No se ha podido obtener la clave privada para firma", e);
			throw new LoadingObjectException ("No se ha podido obtener la clave privada para firma", e);
		} catch (SearchingException e) {
			logger.info("[PAdESLTVSignature.sign]::No se ha podido encontrar la clave privada para firma", e);
			throw new LoadingObjectException ("No se ha podido encontrar la clave privada para firma", e);
		} catch (IOException e) {
			logger.info("[PAdESLTVSignature.sign]::No se ha podido leer el fichero PDF o no se ha podido crear el fichero temporal con la firma", e);
			throw new PDFDocumentException ("No se ha podido leer el fichero PDF o no se ha podido crear el fichero temporal con la firma", e);
		} catch (DocumentException e) {
			logger.info("[PAdESLTVSignature.sign]::No se ha podido manejar alguna de las partes del fichero PDF", e);
			throw new PDFDocumentException ("No se ha podido manejar alguna de las partes del fichero PDF", e);
		} catch (InvalidKeyException e) {
			logger.info("[PAdESLTVSignature.sign]::La clave privada no es válida para realizar la firma", e);
			throw new SignatureException ("La clave privada no es válida para realizar la firma", e);
		} catch (NoSuchProviderException e) {
			logger.info("[PAdESLTVSignature.sign]::No se ha podido obtener el proveedor criptográfico", e);
			return null;
		} catch (NoSuchAlgorithmException e) {
			logger.info("[PAdESLTVSignature.sign]::No existen en el proveedor criptográfico los algoritmos de firma (SHA1WithRSA)", e);
			throw new SignatureException ("No existen en el proveedor criptográfico los algoritmos de firma (SHA1WithRSA)", e);
		} catch (CertificateCANotFoundException e) {
			logger.info("[PAdESLTVSignature.sign]::La lista de certificado de CA no contiene el emisor del certificado de firma", e);
			throw new CertificateCANotFoundException ("La lista de certificado de CA no contiene el emisor del certificado de firma", e);
		} 

	}
	
	/**
	 * Método que completa un fichero PDF firmado a PAdES-LTV. La firma del PDF firmado ha de ser
	 * correcta y los certificados han de ser válidos en este momento.
	 * 
	 * @param signature PDF firmado
	 * @param urlTimestamp URL del servidor de sello de tiempos
	 * @param caList Lista de certificados de CA, uno de ellos ha de ser el emisor del 
	 * 	certificado con el que se realiza la firma
	 * @return PAdES-LTV
	 * @throws PDFDocumentException El documento no es un fichero PDF o es un PDF mal formado
	 * @throws SignatureException Error completando la firma
	 * @throws RetrieveOCSPException No es posible obtener una respuesta OCSP para
	 * 	asociarla a la firma
	 * @throws HashingException Excepción obteniendo el hash que será sellado por la TSA
	 * @throws CertificateCANotFoundException La lista de certificado de CA no contiene el 
	 * 	emisor del certificado de firma o existe pero tiene un formato no normalizable por 
	 * 	el proveedor criptográfico de Arangi
	 * @throws InvalidCertificateException El certificado con el que se firma está revocado
	 * @throws NormalizeCertificateException Alguno de los certificados de firma o de sus cadenas
	 * 	de certificación no puede ser normalizado
	 */
	public static PAdESLTVSignature completeToPAdESLTV (PDFSignature signature, URL urlTimestamp, CAList caList) throws SignatureException, 
		RetrieveOCSPException, InvalidCertificateException, NormalizeCertificateException, PDFDocumentException, CertificateCANotFoundException, 
		HashingException {
		
		return PAdESLTVSignature.completeToPAdESLTV(signature, urlTimestamp, null, caList);
		
	}

	/**
	 * Método que completa un fichero PDF firmado a PAdES-LTV. La firma del PDF firmado ha de ser
	 * correcta y los certificados han de ser válidos en este momento.
	 * 
	 * @param signature PDF firmado
	 * @param urlTimestamp URL del servidor de sello de tiempos
	 * @param tsaHashingAlgorithm Algoritmo de hash para llamar a la TSA
	 * @param caList Lista de certificados de CA, uno de ellos ha de ser el emisor del 
	 * 	certificado con el que se realiza la firma
	 * @return PAdES-LTV
	 * @throws PDFDocumentException El documento no es un fichero PDF o es un PDF mal formado
	 * @throws SignatureException Error completando la firma
	 * @throws RetrieveOCSPException No es posible obtener una respuesta OCSP para
	 * 	asociarla a la firma
	 * @throws HashingException Excepción obteniendo el hash que será sellado por la TSA
	 * @throws CertificateCANotFoundException La lista de certificado de CA no contiene el 
	 * 	emisor del certificado de firma o existe pero tiene un formato no normalizable por 
	 * 	el proveedor criptográfico de Arangi
	 * @throws InvalidCertificateException El certificado con el que se firma está revocado
	 * @throws NormalizeCertificateException Alguno de los certificados de firma o de sus cadenas
	 * 	de certificación no puede ser normalizado
	 */
	public static PAdESLTVSignature completeToPAdESLTV (PDFSignature signature, URL urlTimestamp, String tsaHashingAlgorithm, CAList caList) throws SignatureException, 
		RetrieveOCSPException, InvalidCertificateException, NormalizeCertificateException, PDFDocumentException, CertificateCANotFoundException, 
		HashingException {
		
		return PAdESLTVSignature.completeToPAdESLTV(signature, urlTimestamp, null, null, caList);
		
	}

	/**
	 * Método que completa un fichero PDF firmado a PAdES-LTV. La firma del PDF firmado ha de ser
	 * correcta y los certificados han de ser válidos en este momento.
	 * 
	 * @param signature PDF firmado
	 * @param urlTimestamp URL del servidor de sello de tiempos
	 * @param userTSA Usuario para acceder al servidor de sello de tiempos. Nulo si no
	 * 	requiere autenticación.
	 * @param passwordTSA Contraseña para acceder al servidor de sello de tiempos. Nula si 
	 * no requiere autenticación.
	 * @param caList Lista de certificados de CA, uno de ellos ha de ser el emisor del 
	 * 	certificado con el que se realiza la firma
	 * @return PAdES-LTV
	 * @throws PDFDocumentException El documento no es un fichero PDF o es un PDF mal formado
	 * @throws SignatureException Error completando la firma
	 * @throws RetrieveOCSPException No es posible obtener una respuesta OCSP para
	 * 	asociarla a la firma
	 * @throws HashingException Excepción obteniendo el hash que será sellado por la TSA
	 * @throws CertificateCANotFoundException La lista de certificado de CA no contiene el 
	 * 	emisor del certificado de firma o existe pero tiene un formato no normalizable por 
	 * 	el proveedor criptográfico de Arangi
	 * @throws InvalidCertificateException El certificado con el que se firma está revocado
	 * @throws NormalizeCertificateException 
	 * @throws NormalizeCertificateException Alguno de los certificados de firma o de sus cadenas
	 * 	de certificación no puede ser normalizado
	 */
	public static PAdESLTVSignature completeToPAdESLTV (PDFSignature signature, URL urlTimestamp, 
			String userTSA, String passwordTSA, CAList caList) throws SignatureException, RetrieveOCSPException, InvalidCertificateException, 
			PDFDocumentException, CertificateCANotFoundException, HashingException, NormalizeCertificateException {
		return completeToPAdESLTV(signature, urlTimestamp, userTSA, passwordTSA, null, caList);
	}
	
	/**
	 * Método que completa un fichero PDF firmado a PAdES-LTV. La firma del PDF firmado ha de ser
	 * correcta y los certificados han de ser válidos en este momento.
	 * 
	 * @param signature PDF firmado
	 * @param urlTimestamp URL del servidor de sello de tiempos
	 * @param userTSA Usuario para acceder al servidor de sello de tiempos. Nulo si no
	 * 	requiere autenticación.
	 * @param passwordTSA Contraseña para acceder al servidor de sello de tiempos. Nula si 
	 * no requiere autenticación.
	 * @param tsaHashingAlgorithm Algoritmo de hash para llamar a la TSA
	 * @param caList Lista de certificados de CA, uno de ellos ha de ser el emisor del 
	 * 	certificado con el que se realiza la firma
	 * @return PAdES-LTV
	 * @throws PDFDocumentException El documento no es un fichero PDF o es un PDF mal formado
	 * @throws SignatureException Error completando la firma
	 * @throws RetrieveOCSPException No es posible obtener una respuesta OCSP para
	 * 	asociarla a la firma
	 * @throws HashingException Excepción obteniendo el hash que será sellado por la TSA
	 * @throws CertificateCANotFoundException La lista de certificado de CA no contiene el 
	 * 	emisor del certificado de firma o existe pero tiene un formato no normalizable por 
	 * 	el proveedor criptográfico de Arangi
	 * @throws InvalidCertificateException El certificado con el que se firma está revocado
	 * @throws NormalizeCertificateException 
	 * @throws NormalizeCertificateException Alguno de los certificados de firma o de sus cadenas
	 * 	de certificación no puede ser normalizado
	 */
	public static PAdESLTVSignature completeToPAdESLTV (PDFSignature signature, URL urlTimestamp, 
			String userTSA, String passwordTSA, String tsaHashingAlgorithm, CAList caList) throws SignatureException, RetrieveOCSPException, InvalidCertificateException, 
			PDFDocumentException, CertificateCANotFoundException, HashingException, NormalizeCertificateException {
		logger.debug ("[PAdESLTVSignature.completeToPAdESLTV]::Entrada::" + signature);
		
		try {
			//-- Obtener el fichero temporal donde se dejará el resultado
			File fileResult = getFileTemp();
			signature.save(fileResult);
			
			PdfReader reader = new PdfReader(new FileInputStream(fileResult));
			
			return completeToPAdESLTV(reader, urlTimestamp, userTSA, passwordTSA, tsaHashingAlgorithm, caList);
			
		} catch (IOException e) {
			logger.info("[PAdESLTVSignature.completeToPAdESLTV]::No se ha podido leer el fichero PDF o no se ha podido crear el fichero temporal con la firma", e);
			throw new PDFDocumentException ("No se ha podido leer el fichero PDF o no se ha podido crear el fichero temporal con la firma", e);
		} 
		
	}
	
	//-- Métodos públicos
	
	/* (non-Javadoc)
	 * @see es.accv.arangi.base.signature.ISignature#isValid(es.accv.arangi.base.certificate.validation.CAList)
	 */
	public ValidationResult[] isValid(CAList caList) throws HashingException, SignatureException, NormalizeCertificateException {
		
		logger.debug("[PAdESLTVSignature.isValid]::Entrada::" + caList);
		
		return isValidCommon(caList, null);
		
	}
	
	/**
	 * En el caso de los PDF no tiene sentido realizar la validación sobre un 
	 * documento que no sea el mismo PDF. Por ello, el resultado de este método 
	 * será igual a llamar a {@link #isValid(CAList) isValid} con sólo un parámetro. 
	 */
	public ValidationResult[] isValid(IDocument document, CAList caList)
			throws HashingException, SignatureException,
			NormalizeCertificateException {
		return isValid (caList);
	}
	
	/* (non-Javadoc)
	 * @see es.accv.arangi.base.signature.ISignature#isValid(java.util.List(es.accv.arangi.base.certificate.validation.service.CertificateValidationService))
	 */
	public ValidationResult[] isValid(List<CertificateValidationService> validationServices)
			throws HashingException, SignatureException, NormalizeCertificateException {
		
		logger.debug("[PAdESLTVSignature.isValid]::Entrada::" + validationServices);
		
		return isValidCommon(null, validationServices);
		
	}

	/**
	 * En el caso de los PDF no tiene sentido realizar la validación sobre un 
	 * documento que no sea el mismo PDF. Por ello, el resultado de este método 
	 * será igual a llamar a {@link #isValid(List) isValid} con sólo un parámetro. 
	 */
	public ValidationResult[] isValid(IDocument document, List<CertificateValidationService> validationServices)
			throws HashingException, SignatureException, NormalizeCertificateException {
		return isValid(validationServices);
	}

	/**
	 * Devuelve una cadena de texto con el tipo de la firma
	 * 
	 * @return Cadena de texto con el tipo de la firma
	 */
	public String getSignatureType () {
		return "PAdES-LTV";
	}
	
	/**
	 * Obtiene la fecha en que caduca el certificado del sello de tiempo que cubre la 
	 * firma longeva.
	 * 
	 * @return Fecha de caducidad del sello de tiempo del documento
	 * @throws SignatureException No hay sello de tiempo del documento o éste no se 
	 * 	puede leer
	 */
	public Date getTimeStampCertificateExpiration() throws SignatureException {
		logger.debug("[PAdESLTVSignature.getTimeStampCertificateExpiration]::Entrada");
		
		PdfReader reader;
		try {
			reader = new PdfReader(this.pdfFile.getAbsolutePath());
		} catch (IOException e) {
			// El fichero ya pasó la validación
			logger.info("[PAdESLTVSignature.getTimeStampCertificateExpiration]::No se puede leer el contenido de este objeto", e);
			return null;
		}
		AcroFields af = reader.getAcroFields();
		ArrayList<String> tempNames = af.getSignatureNames();
		ArrayList<String> names = new ArrayList<String>();
		
		//-- Comprobar que el sello de tiempos para el documento es correcto
		for (Iterator iterator = tempNames.iterator(); iterator.hasNext();) {
			String name = (String) iterator.next();
			if (af.getSignatureDictionary(name).get(PdfName.SUBFILTER) != null &&
					af.getSignatureDictionary(name).get(PdfName.SUBFILTER).equals(new PdfName("ETSI.RFC3161"))) {
				
				TimeStamp ts;
				try {
					ts = new TimeStamp (af.getSignatureDictionary(name).getAsString(PdfName.CONTENTS).getOriginalBytes());
					logger.debug("[PAdESLTVSignature.getTimeStampCertificateExpiration]::Devolviendo: " + ts.getSignatureCertificate().getValidityPeriodEnd());
					return ts.getSignatureCertificate().getValidityPeriodEnd();
				} catch (MalformedTimeStampException e) {
					logger.info("[PAdESLTVSignature.getTimeStampCertificateExpiration]::El sello de tiempos del documento no es correcto", e);
					throw new SignatureException ("El sello de tiempos del documento no es correcto", e);
				}

			} else {
				names.add(name);
			}
		}
		
		//-- No hay sello de tiempos del documento
		logger.info("[PAdESLTVSignature.getTimeStampCertificateExpiration]::No hay sello de tiempos del documento");
		throw new SignatureException ("No hay sello de tiempos del documento");
		
	}

	/**
	 * Añade un sello de tiempos al documento PDF (document time-stamp).
	 * 
	 * @param urlTSA URL del servidor de sello de tiempos
	 * @param caList Lista de certificados de CA admitidos. Sirve para obtener las cadenas
	 * 	de certificación de los certificados implicados en la firma y/o sello de tiempos
	 * @throws SignatureException Error leyendo o guardando objetos de la firma
	 * @throws RetrieveOCSPException No es posible obtener una respuesta OCSP para el 
	 * 	certificado del último sello de tiempos del documento
	 * @throws ResponseTimeStampException No es posible obtener una respuesta del servidor
	 * 	de sello de tiempos
	 * @throws CertificateCANotFoundException El certificado del último sello de tiempos del 
	 * 	documento no pertenece a ninguna de las Autoridades de Certificación de confianza
	 */
	public void addDocumentTimeStamp (URL urlTSA, CAList caList) throws SignatureException, RetrieveOCSPException, ResponseTimeStampException, CertificateCANotFoundException {
		addDocumentTimeStamp(urlTSA, null, null, caList);
	}
	
	/**
	 * Añade un sello de tiempos al documento PDF (document time-stamp), convirtiéndolo, si 
	 * no lo era ya, en un PAdES-A.
	 * 
	 * @param urlTSA URL del servidor de sello de tiempos
	 * @param userTSA Usuario para acceder al servidor de sello de tiempos. Nulo si no
	 * 	requiere autenticación.
	 * @param passwordTSA Contraseña para acceder al servidor de sello de tiempos. Nula si 
	 * no requiere autenticación.
	 * @param caList Lista de certificados de CA admitidos. Sirve para obtener las cadenas
	 * 	de certificación de los certificados implicados en la firma y/o sello de tiempos
	 * @throws SignatureException Error leyendo o guardando objetos de la firma
	 * @throws RetrieveOCSPException No es posible obtener una respuesta OCSP para el 
	 * 	certificado del último sello de tiempos del documento
	 * @throws ResponseTimeStampException No es posible obtener una respuesta del servidor
	 * 	de sello de tiempos
	 * @throws CertificateCANotFoundException El certificado del último sello de tiempos del 
	 * 	documento no pertenece a ninguna de las Autoridades de Certificación de confianza
	 */
	public void addDocumentTimeStamp (URL urlTSA, String userTSA, String passwordTSA, CAList caList) throws SignatureException, RetrieveOCSPException, ResponseTimeStampException, CertificateCANotFoundException {
		logger.debug("[PAdESLTVSignature.addDocumentTimeStamp]::Entrada::"+ Arrays.asList(new Object[] { urlTSA, userTSA, passwordTSA, caList } ));
		
		FileOutputStream fos = null;
		try {
		
			//-- Obtener el fichero temporal donde se dejará el resultado
			File fileResult = getFileTemp();
			
			//-- Objeto con el diccionario DSS
    		PdfDocumentSecurityStore dss = new PdfDocumentSecurityStore();
			
    		PdfReader reader;
    		try {
    			reader = new PdfReader(this.pdfFile.getAbsolutePath());
    		} catch (IOException e) {
    			// El fichero ya pasó la validación
    			logger.info("[PAdESLTVSignature.addDocumentTimeStamp]::No se puede leer el contenido de este objeto", e);
    			return;
    		}
    		
   		//-- Obtener el último sello de tiempos
    		TimeStampDictionary[] diccionariosSellosTiempoDocumento = getOrderedDocumentTimestamp(reader);
    		if (diccionariosSellosTiempoDocumento.length == 0) {
    			
    		}
    		TimeStampDictionary diccionarioSellosTiempoDocumento = diccionariosSellosTiempoDocumento[diccionariosSellosTiempoDocumento.length - 1];
    		
			//-- Obtener el último certificado del sello de tiempos
			TimeStamp ts = diccionarioSellosTiempoDocumento.getTs();
			
			//-- Obtener el algoritmo de hashing
			String hashingAlgorithm = ts.getHashAlgorithmName();
			
			//-- Obtener la cadena del certificado de sello de tiempos
			ValidateCertificate certificateTimestamp;
			try {
				certificateTimestamp = new ValidateCertificate (ts.getSignatureCertificate().toX509Certificate(), caList);
			} catch (MalformedTimeStampException e) {
				logger.info("[PAdESLTVSignature.addDocumentTimeStamp]::El último sello de tiempos del documento no está bien formado", e);
				throw new SignatureException ("El último sello de tiempos del documento no está bien formado", e);
			} 
			List<ValidateCertificate> cadenaTimestamp = certificateTimestamp.getCertificationChainAsList();
			
			//-- Añadir información de validación (DSS)
			List<ValidateCertificate> certificadosDSS = juntarListasDSS (null, certificateTimestamp, null, cadenaTimestamp);
			cargarDSS(certificadosDSS, dss, caList);

			//-- Añadir el diccionario DSS (estándar PAdES-LTV)
			try {
	            fos = new FileOutputStream (fileResult);
	            PdfStamper stamper = new PdfStamper(reader, fos, '\0', true);
				stamper.addDocumentSecurityStore(dss);
				stamper.close();
			} finally {
				if (fos != null) {
					fos.close();
				}
			}
			
			//-- Añadir sello de tiempos
			File fileResult2 = getFileTemp();
			addDocumentTimeStamp(fileResult, fileResult2, urlTSA, hashingAlgorithm, userTSA, passwordTSA);
			fileResult.delete();
			initialize(fileResult2);
			
		} catch (DocumentException e) {
			logger.info("[PAdESLTVSignature.addDocumentTimeStamp]::No se ha podido manejar alguna de las partes del fichero PDF", e);
			throw new SignatureException ("No se ha podido manejar alguna de las partes del fichero PDF", e);
		} catch (ResponseTimeStampException e) {
			logger.info("[PAdESLTVSignature.addDocumentTimeStamp]::El servidos de sello de tiempos devuelve un error en la respuesta", e);
			throw e;
		} catch (TimeStampServerConnectionException e) {
			logger.info("[PAdESLTVSignature.addDocumentTimeStamp]::No ha sido posible conectarse al servidor de sello de tiempos", e);
			throw new ResponseTimeStampException ("No ha sido posible conectarse al servidor de sello de tiempos", e);
		} catch (MalformedTimeStampException e) {
			logger.info("[PAdESLTVSignature.addDocumentTimeStamp]::El sello de tiempos obtenido no está bien formado", e);
			throw new ResponseTimeStampException ("El sello de tiempos obtenido no está bien formado", e);
		} catch (CertificateCANotFoundException e) {
			logger.info("[PAdESLTVSignature.addDocumentTimeStamp]::El certificado del último sello de tiempos de documento no pertenece a ninguna de las CAs de confianza", e);
			throw e;
		} catch (NormalizeCertificateException e) {
			logger.info("[PAdESLTVSignature.addDocumentTimeStamp]::El certificado del último sello de tiempos de documento no puede ser normalizado", e);
			throw new SignatureException ("El certificado del último sello de tiempos de documento no puede ser normalizado", e);
		} catch (RetrieveOCSPException e) {
			logger.info("[PAdESLTVSignature.addDocumentTimeStamp]::No se puede obtener la respuesta OCSP", e);
			throw e;
		} catch (InvalidCertificateException e) {
			logger.info("[PAdESLTVSignature.addDocumentTimeStamp]::El certificado del último sello de tiempos de documento no es válido", e);
			throw new SignatureException ("El certificado del último sello de tiempos de documento no es válido", e);
		} catch (IOException e) {
			logger.info("[PAdESLTVSignature.addDocumentTimeStamp]::Error de entrada/salida tratando el PDF", e);
			throw new SignatureException ("Error de entrada/salida tratando el PDF", e);
		} catch (HashingException e) {
			logger.info("[PAdESLTVSignature.addDocumentTimeStamp]::Error obteniendo el hash para sellar", e);
			throw new SignatureException ("Error obteniendo el hash para sellar", e);
		} catch (PDFDocumentException e) {
			// No se va a dar, el PDF ya se abrió y era correcto
			logger.info("[PAdESLTVSignature.addDocumentTimeStamp]::El PDF tras el resellado no puede ser abierto", e);
			throw new SignatureException ("El PDF tras el resellado no puede ser abierto", e);
		} catch (SignatureNotFoundException e) {
			// No se va a dar, el PDF está firmado
			logger.info("[PAdESLTVSignature.addDocumentTimeStamp]::El PDF tras el resellado no presenta firma", e);
			throw new SignatureException ("El PDF tras el resellado no presenta firma", e);
		} catch (NoSuchAlgorithmException e) {
			logger.info("[PAdESLTVSignature.addDocumentTimeStamp]::No existe el algoritmo de hash con el que se quiere realizar el sellado de tiempos", e);
			throw new SignatureException ("No existe el algoritmo de hash con el que se quiere realizar el sellado de tiempos", e);
		} 
		
	}
	
	/**
	 * Obtiene una lista ordenada por fecha de los sellos de tiempo de documento
	 * 
	 * @param reader Lectura del PDF
	 * @return Lista ordenada de sellos de tiempo del documento
	 * @throws SignatureException No es posible parsear un sello de tiempos al
	 * 	objeto TimeStamp del proveedor criptográfico
	 */
	public TimeStampDictionary[] getOrderedDocumentTimestamp () throws SignatureException {
		PdfReader reader;
		try {
			reader = new PdfReader(this.pdfFile.getAbsolutePath());
		} catch (IOException e) {
			// El fichero ya pasó la validación
			logger.info("[PAdESLTVSignature.addDocumentTimeStamp]::No se puede leer el contenido de este objeto", e);
			throw new SignatureException ("No se puede leer el contenido de este objeto");
		}
		
		return PAdESLTVSignature.getOrderedDocumentTimestamp(reader);
	}
	
	//-- Métodos privados
	
	private void initialize (File pdfFile) throws PDFDocumentException, SignatureNotFoundException {
		logger.debug("[PAdESLTVSignature.initialize]::Entrada::" + pdfFile);
		
		//-- Comprobar que el fichero es un PDF firmado
		try {
			PdfReader reader = new PdfReader(pdfFile.getAbsolutePath());
			AcroFields af = reader.getAcroFields();
			ArrayList names = af.getSignatureNames();
			if (names == null || names.isEmpty()) {
				logger.info("[PAdESLTVSignature.initialize]::El documento PDF no está firmado");
				throw new SignatureNotFoundException ("El documento PDF no está firmado");
			}
			if (names.size() == 1) {
				logger.info("[PAdESLTVSignature.initialize]::El documento PDF no es PAdES-LTV: sólo hay una firma y debería haber firma y sello de tiempos");
				throw new SignatureNotFoundException ("El documento PDF no es PAdES-LTV: sólo hay una firma y debería haber firma y sello de tiempos");
			}
			
			//-- Comprobar que existe el diccionario DSS
			if (reader.getCatalog().get(new PdfName("DSS")) == null) {
				logger.info("[PAdESLTVSignature.initialize]::El documento PDF no es PAdES-LTV: no contiene un diccionario Document Security Store (DSS)");
				throw new SignatureNotFoundException ("El documento PDF no es PAdES-LTV: no contiene un diccionario Document Security Store (DSS)");
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
				logger.info("[PAdESLTVSignature.initialize]::El documento PDF no es PAdES-LTV: no contiene un sello de tiempos para todo el documento)");
				throw new SignatureNotFoundException ("El documento PDF no es PAdES-LTV: no contiene un sello de tiempos para todo el documento)");
			}
			
		} catch (IOException e) {
			logger.info("[PAdESLTVSignature.initialize]::No se ha podido leer el fichero PDF", e);
			throw new PDFDocumentException ("No se ha podido leer el fichero PDF", e);
		} 
		
		//-- Todo correcto, se inicializa
		this.pdfFile = pdfFile;

	}

	/*
	 * Método que añade el sello de tiempos al documento
	 */
	private static void addDocumentTimeStamp (File sourcePdf, File destPdf, URL urlTimestamp, String hashingAlgorithm,
			String userTSA, String passwordTSA) throws FileNotFoundException, IOException, DocumentException, MalformedTimeStampException, HashingException, ResponseTimeStampException, TimeStampServerConnectionException, NoSuchAlgorithmException {
		
		ResourceBundle rs = ResourceBundle.getBundle("configuracionClienteTSA");
		
		if (hashingAlgorithm == null) {
			hashingAlgorithm = HashingAlgorithm.getDefault();
		}
		
		PdfReader reader = new PdfReader(new FileInputStream(sourcePdf));
		AcroFields af = reader.getAcroFields();
		ArrayList names = af.getSignatureNames();
        FileOutputStream fos = null;
        try {
	        fos = new FileOutputStream (destPdf);
	        PdfStamper stp;
			if (names == null || names.isEmpty()) {
				stp = PdfStamper.createSignature(reader, fos, '\0');
			} else {
				stp = PdfStamper.createSignature(reader, fos, '\0', null, true);
			}
	        PdfSignatureAppearance sap = stp.getSignatureAppearance();
	
	        PdfSignature timeStampSignature = new PdfSignature(PdfName.ADOBE_PPKLITE, new PdfName("ETSI.RFC3161"));
	        timeStampSignature.put(PdfName.TYPE, new PdfName("DocTimeStamp"));
	        sap.setCryptoDictionary(timeStampSignature);
	
	        int csize = 15000;
	        HashMap<PdfName,Integer> exc = new HashMap<PdfName,Integer>();
	        exc.put(PdfName.CONTENTS, new Integer(csize * 2 + 2));
	        sap.preClose(exc);
	
	        TimeStampRequestParameters tsParameters = new TimeStampRequestParameters();
	        //tsParameters.setOid(rs.getString("oid.rfc3161"));
	        tsParameters.setUser(userTSA);
	        tsParameters.setPassword(passwordTSA);
	        TimeStamp ts = TimeStamp.stampDocument(Util.readStream(sap.getRangeStream()), urlTimestamp, hashingAlgorithm, tsParameters);
	        byte[] timestampToken = ts.toDER();
	
	        byte[] outc = new byte[csize];
	        PdfDictionary dic = new PdfDictionary();
	        System.arraycopy(timestampToken, 0, outc, 0, timestampToken.length);
	        dic.put(PdfName.CONTENTS, new PdfString(outc).setHexWriting(true));
	        sap.close(dic);
        } finally {
        	if (fos != null) {
        		fos.close();
        	}
        }

	}
	
	/*
	 * Obtiene una respuesta OCSP para el certificado pasado como parámetro
	 */
	private static OCSPResponse getOcspBasicResponse(ValidateCertificate validateCertificate) throws RetrieveOCSPException, InvalidCertificateException, IOException {
		OCSPClient[] ocspClient = validateCertificate.getOCSPClients();
		OCSPResponse ocspResponse = null;
		for (int i = 0; i < ocspClient.length; i++) {
			try {
				ocspResponse = ocspClient[i].getOCSPResponse(validateCertificate);
				logger.debug ("[PAdESLTVSignature.sign]::Encontrada respuesta OCSP en " + ocspClient[i].getURL());
			} catch (Exception e) {
				//-- Pasamos al siguiente OCSPClient
				logger.debug ("[PAdESLTVSignature.sign]::Excepción buscando respuesta OCSP en " + ocspClient[i].getURL(), e);
			}
		}
		if (ocspResponse == null) {
			logger.info("[PAdESLTVSignature.sign]::No se puede obtener una respuesta de un servidor OCSP para validar el certificado de firma");
			throw new RetrieveOCSPException ("No se puede obtener una respuesta de un servidor OCSP para validar el certificado de firma");
		}
		if (ocspResponse.getStatus() != ValidationResult.RESULT_VALID) {
			logger.info("[PAdESLTVSignature.sign]::Según la respuesta OCSP el certificado de firma no es válido::" + CertificateValidator.getString(ocspResponse.getStatus()));
			throw new InvalidCertificateException ("Según la respuesta OCSP el certificado de firma no es válido: " + CertificateValidator.getString(ocspResponse.getStatus()), ocspResponse.getStatus());
		}

		return ocspResponse;
	}

	/*
	 * Método que completa un fichero PDF firmado a PAdES-LTV. La firma del PDF firmado ha de ser
	 * correcta y los certificados han de ser válidos en este momento.
	 * 
	 * @param signature PDF firmado
	 * @param urlTimestamp URL del servidor de sello de tiempos
	 * @param userTSA Usuario para acceder al servidor de sello de tiempos. Nulo si no
	 * 	requiere autenticación.
	 * @param passwordTSA Contraseña para acceder al servidor de sello de tiempos. Nula si 
	 * no requiere autenticación.
	 * @param caList Lista de certificados de CA, uno de ellos ha de ser el emisor del 
	 * 	certificado con el que se realiza la firma
	 * @return PAdES-LTV
	 * @throws PDFDocumentException El documento no es un fichero PDF o es un PDF mal formado
	 * @throws SignatureException Error completando la firma
	 * @throws RetrieveOCSPException No es posible obtener una respuesta OCSP para
	 * 	asociarla a la firma
	 * @throws HashingException Excepción obteniendo el hash que será sellado por la TSA
	 * @throws CertificateCANotFoundException La lista de certificado de CA no contiene el 
	 * 	emisor del certificado de firma o existe pero tiene un formato no normalizable por 
	 * 	el proveedor criptográfico de Arangi
	 * @throws InvalidCertificateException El certificado con el que se firma está revocado
	 * @throws NormalizeCertificateException 
	 * @throws NormalizeCertificateException Alguno de los certificados de firma o de sus cadenas
	 * 	de certificación no puede ser normalizado
	 */
	private static PAdESLTVSignature completeToPAdESLTV (PdfReader reader, URL urlTimestamp, 
			String userTSA, String passwordTSA, String hashingAlgorithm, CAList caList) throws SignatureException, RetrieveOCSPException, InvalidCertificateException, 
			PDFDocumentException, CertificateCANotFoundException, HashingException, NormalizeCertificateException {
		logger.debug ("[PAdESLTVSignature.completeToPAdESLTV]::Entrada");
		
		try {
		
			//-- Objeto con el diccionario DSS
    		PdfDocumentSecurityStore dss = new PdfDocumentSecurityStore();
    		
    		//-- Algoritmo de hashing. Si hay alguna firma con un algoritmo superior
    		//-- al que hay por defecto se hará el sello con dicho algoritmo
    		if (hashingAlgorithm == null) {
    			hashingAlgorithm = HashingAlgorithm.getDefault();
    		}
			
			//-- Leer el PDF y crear la firma. Dependiendo de si el PDF ya
			//-- está firmado o no se crea el PdfStamper de una manera u otra
			AcroFields af = reader.getAcroFields();
			ArrayList<String> names = getRealSignatureNames(af);
			for (Iterator<String> iterator = names.iterator(); iterator.hasNext();) {
				String name = iterator.next();
            
				//-- Validar que el PKCS#7 se corresponde con el documento
				PdfPKCS7 pkcs7 = af.verifySignature(name, CRYPTOGRAPHIC_PROVIDER_NAME);
				boolean valido;
				try {
					valido = pkcs7.verify();
				} catch (java.security.SignatureException e) {
					logger.info("[PAdESLTVSignature.completeToPAdESLTV]::Error en una de las firmas del PDF", e);
					throw new SignatureException ("Error en una de las firmas del PDF", e);
				}
				if (!valido) {
					logger.info("[PAdESLTVSignature.completeToPAdESLTV]::Una de las firmas del PDF no es correcta");
					throw new SignatureException ("Una de las firmas del PDF no es correcta");
				}
				
				if (HashingAlgorithm.isGreater(pkcs7.getHashAlgorithm(), hashingAlgorithm)) {
					try {
						hashingAlgorithm = HashingAlgorithm.getAlgorithmFromExternalName(pkcs7.getHashAlgorithm());
					} catch (NoSuchAlgorithmException e) {
						logger.info("No se puede hacer el sello para el algoritmo de hash: " + pkcs7.getHashAlgorithm()) ;
					}
				}
				
				//-- Si se ha definido un hueco para firmar pero no se ha rellenado llegamos hasta aquí pero
				//-- no hay certificado de firma
				Certificate signingCertificate = getSigningCertificate(pkcs7.getCertificates());
				if (signingCertificate != null) {
					//-- Obtener el certificado de la firma
					ValidateCertificate certificate = new ValidateCertificate (signingCertificate.toDER(), caList);
					
					//-- Obtener el certificado del sello de tiempos
					ValidateCertificate certificateTimestamp = null;
					List<ValidateCertificate> cadenaTimestamp = null;
					if (pkcs7.getTimeStampToken() != null) {
						TimeStamp ts = new TimeStamp (pkcs7.getTimeStampToken().getEncoded());
						certificateTimestamp = new ValidateCertificate (ts.getSignatureCertificate().toX509Certificate(), caList);
						cadenaTimestamp = certificateTimestamp.getCertificationChainAsList();
					}
					
		            //-- Añadir información al objeto con el diccionario DSS
					List<ValidateCertificate> cadenaCertificate = certificate.getCertificationChainAsList();
					List<ValidateCertificate> certificadosDSS = juntarListasDSS (certificate, certificateTimestamp, cadenaCertificate, cadenaTimestamp);
					
					cargarDSS(certificadosDSS, dss, caList);
				}
				
			}
			
			//-- Añadir DSS (estándar PAdES-LTV)------------------------------
			FileOutputStream fos = null;
			File fileResult = getFileTemp();
			try {
	            fos = new FileOutputStream (fileResult);
	            PdfStamper stamper = new PdfStamper(reader, fos, '\0', true);
				stamper.addDocumentSecurityStore(dss);
				stamper.close();
			} finally {
				if (fos != null) {
					fos.close();
				}
			}
			
			//-- Añadir sello de tiempos
			File fileResult2 = getFileTemp();
			addDocumentTimeStamp(fileResult, fileResult2, urlTimestamp, hashingAlgorithm, userTSA, passwordTSA);
			fileResult.delete();
			try {
				return new PAdESLTVSignature (fileResult2);
			} catch (SignatureNotFoundException e) {
				//-- No se va a dar, el fichero se acaba de completar
				return null;
			} catch (PDFDocumentException e) {
				//-- No se va a dar, el fichero se acaba de completar
				return null;
			}
			
		} catch (IOException e) {
			logger.info("[PAdESLTVSignature.completeToPAdESLTV]::No se ha podido leer el fichero PDF o no se ha podido crear el fichero temporal con la firma", e);
			throw new PDFDocumentException ("No se ha podido leer el fichero PDF o no se ha podido crear el fichero temporal con la firma", e);
		} catch (CertificateCANotFoundException e) {
			logger.info("[PAdESLTVSignature.completeToPAdESLTV]::La lista de certificado de CA no contiene el emisor del certificado de firma", e);
			throw new CertificateCANotFoundException ("La lista de certificado de CA no contiene el emisor del certificado de firma", e);
		} catch (MalformedTimeStampException e) {
			logger.info("[PAdESLTVSignature.sign]::El sello de tiempos obtenido no está bien formado", e);
			throw new SignatureException ("El sello de tiempos obtenido no está bien formado", e);
		} catch (ResponseTimeStampException e) {
			logger.info("[PAdESLTVSignature.sign]::El servidos de sello de tiempos devuelve un error en la respuesta", e);
			throw new SignatureException ("El servidos de sello de tiempos devuelve un error en la respuesta", e);
		} catch (DocumentException e) {
			logger.info("[PAdESLTVSignature.completeToPAdESLTV]::No se ha podido manejar alguna de las partes del fichero PDF", e);
			throw new PDFDocumentException ("No se ha podido manejar alguna de las partes del fichero PDF", e);
		} catch (TimeStampServerConnectionException e) {
			logger.info("[PAdESLTVSignature.sign]::No ha sido posible conectarse al servidor de sello de tiempos", e);
			throw new SignatureException ("No ha sido posible conectarse al servidor de sello de tiempos", e);
		} catch (NoSuchAlgorithmException e) {
			logger.info("[PAdESLTVSignature.addDocumentTimeStamp]::No existe el algoritmo de hash con el que se quiere realizar el sellado de tiempos", e);
			throw new SignatureException ("No existe el algoritmo de hash con el que se quiere realizar el sellado de tiempos", e);
		} 
		
	}
	
	/*
	 * Junta los certificados y los arrays en un único array sin elementos repetidos
	 */
    private static List<ValidateCertificate> juntarListasDSS(ValidateCertificate certificate,
			ValidateCertificate certificateTimestamp, List<ValidateCertificate> cadenaCertificate,
			List<ValidateCertificate> cadenaTimestamp) {
		
    	HashSet<String> setHuellas = new HashSet<String>();
    	ArrayList<ValidateCertificate> certificates = new ArrayList<ValidateCertificate>();
    	
    	if (certificate != null) {
	    	certificates.add(certificate);
	    	setHuellas.add(certificate.getFingerPrint());
    	}
    	
    	if (certificateTimestamp != null && !setHuellas.contains(certificateTimestamp.getFingerPrint())) {
        	certificates.add(certificateTimestamp);
        	setHuellas.add(certificateTimestamp.getFingerPrint());
    	}
    	
    	if (cadenaCertificate != null) {
	    	for (Iterator<ValidateCertificate> iterator = cadenaCertificate.iterator();iterator.hasNext();) {
	    		ValidateCertificate cert = iterator.next();
	        	if (!setHuellas.contains(cert.getFingerPrint())) {
	            	certificates.add(cert);
	            	setHuellas.add(cert.getFingerPrint());
	        	}
			}
    	}
    	if (cadenaTimestamp != null) {
	    	for (Iterator<ValidateCertificate> iterator = cadenaTimestamp.iterator();iterator.hasNext();) {
	    		ValidateCertificate cert = iterator.next();
	        	if (!setHuellas.contains(cert.getFingerPrint())) {
	            	certificates.add(cert);
	            	setHuellas.add(cert.getFingerPrint());
	        	}
			}
    	}
    	
		return certificates;
	}

    /*
     * Pasa de lista de Integers a array de int
     */
	private static int[] toArray(ArrayList<Integer> certIds) {
		int[] result = new int [certIds.size()];
		int i = 0;
		for (Iterator<Integer> iterator = certIds.iterator(); iterator.hasNext();) {
			result[i] = iterator.next();
			i++;			
		}
		return result;
	}

	/*
	 * Obtiene la lista de certificados del DSS
	 */
	private List<Certificate> getCertificadosDeDSS(PdfDocumentSecurityStore dss) throws NormalizeCertificateException {
		ArrayList<Certificate> lCertificates = new ArrayList<Certificate>();
		Set<Integer> setKeys = dss.getCertificates().keySet();
		for (Integer key : setKeys) {
			lCertificates.add(new Certificate(dss.getCertificates().get(key)));
		}
		return lCertificates;
	}
	
	/**
	 * Obtiene una lista ordenada por fecha de los sellos de tiempo de documento
	 * 
	 * @param reader Lectura del PDF
	 * @return Lista ordenada de sellos de tiempo del documento
	 * @throws SignatureException No es posible parsear un sello de tiempos al
	 * 	objeto TimeStamp del proveedor criptográfico
	 */
	private static TimeStampDictionary[] getOrderedDocumentTimestamp (PdfReader reader) throws SignatureException {
		logger.debug("[PAdESLTVSignature.getOrderedDocumentTimestamp]::Entrada");

		AcroFields af = reader.getAcroFields();
		ArrayList<String> tempNames = af.getSignatureNames();
		ArrayList<TimeStampDictionary> lTimeStamps = new ArrayList<TimeStampDictionary>();
		for (Iterator iterator = tempNames.iterator(); iterator.hasNext();) {
			String name = (String) iterator.next();
			if (af.getSignatureDictionary(name).get(PdfName.SUBFILTER) != null &&
					af.getSignatureDictionary(name).get(PdfName.SUBFILTER).equals(new PdfName("ETSI.RFC3161"))) {
				
				try {
					lTimeStamps.add(new PAdESLTVSignature.TimeStampDictionary (
							name,
							new TimeStamp (af.getSignatureDictionary(name).getAsString(PdfName.CONTENTS).getOriginalBytes()),
							af.getSignatureDictionary(name)));
				} catch (MalformedTimeStampException e) {
					logger.info("[PAdESLTVSignature.isValid]::El sello de tiempos del documento no es correcto", e);
					throw new SignatureException ("El sello de tiempos del documento no es correcto", e);
				}

			} 
		}
		
		logger.debug("[PAdESLTVSignature.getOrderedDocumentTimestamp]::Ordenando " + lTimeStamps.size() + " sellos de tiempo de documento");
		Collections.sort(lTimeStamps);
		
		logger.debug("[PAdESLTVSignature.getOrderedDocumentTimestamp]::Devolviendo " + lTimeStamps.size() + " sellos de tiempo de documento");
		return lTimeStamps.toArray(new TimeStampDictionary[0]);
		
	}

	/*
	 * Carga un DSS con la información proveniente de los certificados pasados como parámetro
	 */
	private static void cargarDSS (List<ValidateCertificate> certificadosDSS, PdfDocumentSecurityStore dss, CAList caList) throws NormalizeCertificateException, RetrieveOCSPException, InvalidCertificateException, IOException {
		ArrayList<ValidateCertificate> selfSignedCertificates = new ArrayList<ValidateCertificate>(); 
		ArrayList<ValidateCertificate> ocspCertificatesWithoutNoRevocationCheck = new ArrayList<ValidateCertificate>(); 
		for (Iterator<ValidateCertificate> iterator = certificadosDSS.iterator();iterator.hasNext();) {
			ValidateCertificate certificadoDSS = iterator.next();
			if (!certificadoDSS.isSelfSigned()) {
				dss.registerCertificate(certificadoDSS.toDER());
				try {
					logger.debug("Intentando cargar respuesta OCSP en DSS para el certificado " + certificadoDSS.getCommonName());
					OCSPResponse ocspResponse = getOcspBasicResponse (certificadoDSS);
					dss.registerOcspBasicResp(ocspResponse.getBasicOCSPResponse().getEncoded());
					if (!ocspResponse.getSignatureCertificate().hasNoRevocationCheck()) {
						ocspCertificatesWithoutNoRevocationCheck.add(new ValidateCertificate (ocspResponse.getSignatureCertificate().toX509Certificate(), caList));
					}
				} catch (Exception eOcsp) {
					logger.debug("No es posible obtener una respuesta OCSP para el certificado " + certificadoDSS.getCommonName(), eOcsp);
					logger.debug("Intentando cargar CRL en DSS para el certificado " + certificadoDSS.getCommonName());
					try {
						dss.registerCrl(certificadoDSS.getCRL().toDER());
					} catch (Exception e1) {
						logger.debug("No es posible obtener una CRL para el certificado " + certificadoDSS.getCommonName(), e1);
						if (eOcsp instanceof RetrieveOCSPException) { throw (RetrieveOCSPException) eOcsp; }
						if (eOcsp instanceof InvalidCertificateException) { throw (InvalidCertificateException) eOcsp; }
						if (eOcsp instanceof IOException) { throw (IOException) eOcsp; }
					} 
				} 
				
			} else {
				selfSignedCertificates.add(certificadoDSS);
			}
		}
		for (Iterator<ValidateCertificate> iterator2 = selfSignedCertificates.iterator(); iterator2.hasNext();) {
			dss.registerCertificate(iterator2.next().toDER());
		}
		for (Iterator<ValidateCertificate> iteratorOcsp = ocspCertificatesWithoutNoRevocationCheck.iterator(); iteratorOcsp.hasNext();) {
			ValidateCertificate ocspCertificate = iteratorOcsp.next();
			dss.registerCertificate(ocspCertificate.toDER());
			try {
				logger.debug("Intentando cargar respuesta OCSP en DSS para el certificado " + ocspCertificate.getCommonName());
				OCSPResponse ocspResponse = getOcspBasicResponse (ocspCertificate);
				dss.registerOcspBasicResp(ocspResponse.getBasicOCSPResponse().getEncoded());
			} catch (Exception eOcsp) {
				logger.debug("No es posible obtener una respuesta OCSP para el certificado " + ocspCertificate.getCommonName(), eOcsp);
				logger.debug("Intentando cargar CRL en DSS para el certificado " + ocspCertificate.getCommonName());
				try {
					dss.registerCrl(ocspCertificate.getCRL().toDER());
				} catch (Exception e1) {
					logger.debug("No es posible obtener una CRL para el certificado " + ocspCertificate.getCommonName(), e1);
					if (eOcsp instanceof RetrieveOCSPException) { throw (RetrieveOCSPException) eOcsp; }
					if (eOcsp instanceof InvalidCertificateException) { throw (InvalidCertificateException) eOcsp; }
					if (eOcsp instanceof IOException) { throw (IOException) eOcsp; }
				} 
			} 
		}

	}
	
	/*
	 * Método que realizará la validación, tanto si se valida con caList como con
	 * servicio de validación
	 */
	private ValidationResult[] isValidCommon(CAList caList, List<CertificateValidationService> validationServices) throws HashingException, SignatureException, NormalizeCertificateException {
		
		logger.debug("[PAdESLTVSignature.isValidCommon]::Entrada::" + Arrays.asList(new Object[] { caList, validationServices }));
		
		PdfReader reader;
		try {
			reader = new PdfReader(this.pdfFile.getAbsolutePath());
		} catch (IOException e) {
			// El fichero ya pasó la validación
			logger.info("[PAdESLTVSignature.isValidCommon]::No se puede leer el contenido de este objeto", e);
			return null;
		}
		AcroFields af = reader.getAcroFields();
		
		TimeStampDictionary[] sellosTiempoDocumento = PAdESLTVSignature.getOrderedDocumentTimestamp(reader);
		logger.debug("[PAdESLTVSignature.isValidCommon]::Se han obtenido " + sellosTiempoDocumento.length + " sellos de tiempo de documento");
		if (sellosTiempoDocumento.length == 0) {
			logger.info("[PAdESLTVSignature.isValidCommon]::No hay ningún sello de tiempos de documento");
			throw new SignatureException ("No hay ningún sello de tiempos de documento");
		}
		
		//-- Validar el último sello a día de hoy
		TimeStamp ts = sellosTiempoDocumento[sellosTiempoDocumento.length - 1].getTs();
		try {
			if (!ts.isValid()) {
				logger.info("[PAdESLTVSignature.isValidCommon]::El último sello de tiempos del documento de fecha " + ts.getTime() + " no es correcto");
				throw new SignatureException ("El último sello de tiempos del documento de fecha " + ts.getTime() + " no es correcto");
			}
		} catch (MalformedTimeStampException e) {
			logger.info("[PAdESLTVSignature.isValidCommon]::El último sello de tiempos del documento está mal formado", e);
			throw new SignatureException ("El último sello de tiempos del documento está mal formado", e);
		}
		logger.debug("[PAdESLTVSignature.isValidCommon]::El sello de tiempos de fecha " + ts.getTime() + " es correcto");
		
		//-- Validar el certificado del sello de tiempos a día de hoy
		int validationResult;
		if (caList != null) {
			//-- Validar mediante CAList
			logger.debug("[PAdESLTVSignature.isValidCommon]::Validar el certificado de la tsa con CAList");
			ValidateCertificate tsCertificate;
			try {
				tsCertificate = new ValidateCertificate(ts.getSignatureCertificate().toX509Certificate(), caList);
			} catch (CertificateCANotFoundException e) {
				logger.info("[PAdESLTVSignature.isValidCommon]::El certificado del último sello de tiempos del documento no pertenece a una de las CA de confianza", e);
				throw new SignatureException ("El certificado del último sello de tiempos del documento no pertenece a una de las CA de confianza", e);
			} catch (MalformedTimeStampException e) {
				logger.info("[PAdESLTVSignature.isValidCommon]::El último sello de tiempos del documento está mal formado", e);
				throw new SignatureException ("El último sello de tiempos del documento está mal formado", e);
			}
			
			validationResult = tsCertificate.validate();
		} else {
			//-- Validar mediante servicio de validación
			logger.debug("[PAdESLTVSignature.isValidCommon]::Validar el certificado de la tsa con servicio de validación");
			try {
				validationResult = ts.getSignatureCertificate().validate(validationServices).getResult();
			} catch (MalformedTimeStampException e) {
				logger.info("[PAdESLTVSignature.isValidCommon]::El último sello de tiempos del documento está mal formado", e);
				throw new SignatureException ("El último sello de tiempos del documento está mal formado", e);
			}
		}
		
		//-- Si el certificado del sello de tiempos no es válido
		if (validationResult != ValidationResult.RESULT_VALID) {
			//-- Devolver todas las firmas con el error de que el certificado del sello de tiempos del documento no es válido 
			logger.info("[PAdESLTVSignature.isValidCommon]::El último certificado del sello de tiempos no es válido:" + CertificateValidator.getString(validationResult));
			ArrayList<String> allSignatureNames = af.getSignatureNames();
			ArrayList<ValidationResult> result = new ArrayList<ValidationResult>();
			for (Iterator<String> iterator = allSignatureNames.iterator(); iterator.hasNext();) {
				String name = iterator.next();
				if (af.getSignatureDictionary(name).get(PdfName.SUBFILTER) == null ||
						!af.getSignatureDictionary(name).get(PdfName.SUBFILTER).equals(new PdfName("ETSI.RFC3161"))) {
					PdfPKCS7 pkcs7 = af.verifySignature(name, CRYPTOGRAPHIC_PROVIDER_NAME);
					result.add(new ValidationResult(ValidationResult.RESULT_INVALID_TIMESTAMP, pkcs7.getSigningCertificate(), ts.getTime(), ts, null));
				}
			}
			return result.toArray(new ValidationResult[0]);
		}
		
		//-- Obtener el DSS (para que funcione Adobe)
		PdfDocumentSecurityStore dss;
		try {
			dss = new PdfDocumentSecurityStore ((PdfDictionary)reader.getCatalog().getAsDict(new PdfName("DSS")));
		} catch (IOException e) {
			logger.info("[PAdESLTVSignature.isValidCommon]::No ha sido posible leer alguno de los objetos contenidos en el DSS", e);
			throw new SignatureException ("No ha sido posible leer alguno de los objetos contenidos en el DSS", e);
		}
		
		//-- Validar el resto del documento
		try {
			return isValid(ts, dss, caList, af.extractRevision(sellosTiempoDocumento[sellosTiempoDocumento.length - 1].getName()));
		} catch (IOException e) {
			logger.info("[PAdESLTVSignature.isValidCommon]::No es posible obtener la parte del PDF que sella el último sello de tiempos del documento", e);
			throw new SignatureException ("No es posible obtener la parte del PDF que sella el último sello de tiempos del documento", e);
		}
	}
	
	/*
	 * Valida un "trozo" del PDF que se halla envuelto por el sello de tiempos que se pasa
	 * como parámetro. Si en el "trozo" hay otro sello de documento se tendrá que validar
	 * y llamar recursivamente a esta función para que valide su "trozo" interno.
	 */
	private ValidationResult[] isValid (TimeStamp ts, PdfDocumentSecurityStore dssAdobe, CAList caList, InputStream is) throws HashingException, SignatureException, NormalizeCertificateException {
		
		PdfReader reader = null;
		try {
			try {
				reader = new PdfReader(is);
			} catch (IOException e) {
				// El fichero ya pasó la validación
				logger.info("[PAdESLTVSignature.isValid*]::No se puede leer el contenido de este objeto", e);
				return null;
			}
			AcroFields af = reader.getAcroFields();
	
			//-- La fecha de comprobación es la del sello de tiempos anterior
			Date fechaComprobacion = ts.getTime();
			
			//-- Obtener el DSS
			PdfDocumentSecurityStore dss;
			try {
				PdfDictionary dssDictionary = (PdfDictionary)reader.getCatalog().getAsDict(new PdfName("DSS"));
				if (dssDictionary != null) {
					dss = new PdfDocumentSecurityStore (dssDictionary);
				} else {
					dss = dssAdobe; // para que funcione Adobe
				}
			} catch (IOException e) {
				logger.info("[PAdESLTVSignature.isValid*]::No ha sido posible leer alguno de los objetos contenidos en el DSS", e);
				throw new SignatureException ("No ha sido posible leer alguno de los objetos contenidos en el DSS", e);
			}
			
			//-- Obtener todos los certificados que están en el dss (debe coincidir con certificadosDSS) y,
			//-- si no se ha pasado un CAList, con ellos construir el CAList
			List<Certificate> certificadosDSS;
			try {
				certificadosDSS = getCertificadosDeDSS (dss);
				if (caList == null) {
					caList = new CAList(certificadosDSS);
				}
			} catch (NormalizeCertificateException e) {
				logger.info("[PAdESLTVSignature.isValid]::Alguno de los certificados del DSS no puede ser normalizado", e);
				throw new SignatureException ("Alguno de los certificados del DSS no puede ser normalizado");
			}
			
			//-- Obtener los sellos de tiempo
			TimeStampDictionary[] sellosTiempoDocumento = PAdESLTVSignature.getOrderedDocumentTimestamp(reader);
			logger.debug("[PAdESLTVSignature.isValid*]::Se han obtenido " + sellosTiempoDocumento.length + " sellos de tiempo de documento");
			
			//-- Si hay más de un sello de tiempos estamos en una zona donde hay que validar
			//-- un sello de documento
			if (sellosTiempoDocumento.length > 1) {
				logger.debug("[PAdESLTVSignature.isValid*]::Validando otro sello de tiempos de documento");
				//-- Hay que validar el sello y continuar con la siguiente parte del documento
				TimeStamp siguienteTS = sellosTiempoDocumento[sellosTiempoDocumento.length - 2].getTs();
				try {
					if (!siguienteTS.isValid()) {
						logger.info("[PAdESLTVSignature.isValid*]::El sello de tiempos del documento de fecha " + siguienteTS.getTime() + " no es válido");
						throw new SignatureException ("El sello de tiempos del documento de fecha " + siguienteTS.getTime() + " no es válido");
					}
				} catch (MalformedTimeStampException e) {
					logger.info("[PAdESLTVSignature.isValid*]::El sello de tiempos del documento no es correcto", e);
					throw new SignatureException ("El sello de tiempos del documento no es correcto", e);
				}
				logger.debug("[PAdESLTVSignature.isValid*]::El sello de tiempos de documento de fecha " + siguienteTS.getTime() + " es válido");
				
				//-- Validar su certificado con el DSS
				ValidateCertificate tsCertificate;
				try {
					tsCertificate = new ValidateCertificate(siguienteTS.getSignatureCertificate().toX509Certificate(), caList);
				} catch (CertificateCANotFoundException e) {
					logger.info("[PAdESLTVSignature.isValid]::El sello de tiempos del documento de fecha " + siguienteTS.getTime() + " no pertenece a una de las CA de confianza", e);
					throw new SignatureException ("El sello de tiempos del documento de fecha " + siguienteTS.getTime() + " no pertenece a una de las CA de confianza", e);
				} catch (MalformedTimeStampException e) {
					logger.info("[PAdESLTVSignature.isValid]::El sello de tiempos del documento de fecha " + siguienteTS.getTime() + " está mal formado", e);
					throw new SignatureException ("El sello de tiempos del documento de fecha " + siguienteTS.getTime() + " está mal formado", e);
				}
				List<ValidateCertificate> chainCertificate = tsCertificate.getCompleteCertificationChainAsList();
				ValidationResult tsCertValidationResult = validarCertificadosContraDSS(chainCertificate, tsCertificate, null, dss, certificadosDSS, fechaComprobacion, siguienteTS, caList);
				if (!tsCertValidationResult.isValid()) {
					//-- Devolver todas las firmas con el error de que el certificado del sello de tiempos del documento no es válido 
					logger.info("[PAdESLTVSignature.isValid]::El certificado del sello de tiempos del documento de fecha " + siguienteTS.getTime() + "  no es válido:" + CertificateValidator.getString(tsCertValidationResult.getResult()));
					ArrayList<String> allSignatureNames = af.getSignatureNames();
					ArrayList<ValidationResult> result = new ArrayList<ValidationResult>();
					for (Iterator<String> iterator = allSignatureNames.iterator(); iterator.hasNext();) {
						String name = iterator.next();
						if (af.getSignatureDictionary(name).get(PdfName.SUBFILTER) == null ||
								!af.getSignatureDictionary(name).get(PdfName.SUBFILTER).equals(new PdfName("ETSI.RFC3161"))) {
							PdfPKCS7 pkcs7 = af.verifySignature(name, CRYPTOGRAPHIC_PROVIDER_NAME);
							result.add(new ValidationResult(ValidationResult.RESULT_INVALID_TIMESTAMP, pkcs7.getSigningCertificate(), siguienteTS.getTime(), siguienteTS, null));
						}
					}
					return result.toArray(new ValidationResult[0]);
				}
				
				//-- Obtener el DSS
				PdfDocumentSecurityStore dssSiguiente;
				try {
					dssSiguiente = new PdfDocumentSecurityStore ((PdfDictionary)reader.getCatalog().getAsDict(new PdfName("DSS")));
				} catch (IOException e) {
					logger.info("[PAdESLTVSignature.isValid*]::No ha sido posible leer alguno de los objetos contenidos en el DSS", e);
					throw new SignatureException ("No ha sido posible leer alguno de los objetos contenidos en el DSS", e);
				}
				
				//-- Validar el resto del documento
				try {
					return isValid(siguienteTS, dssSiguiente, caList, af.extractRevision(sellosTiempoDocumento[sellosTiempoDocumento.length - 2].getName()));
				} catch (IOException e) {
					logger.info("[PAdESLTVSignature.isValid*]::No es posible obtener la parte del PDF que sella el sello de tiempos del documento de fecha " + siguienteTS.getTime(), e);
					throw new SignatureException ("No es posible obtener la parte del PDF que sella el sello de tiempos del documento de fecha " + siguienteTS.getTime(), e);
				}
			}
			
			//-- Ya estamos en la última parte del documento, hay que validar las firmas
			logger.debug("[PAdESLTVSignature.isValid*]::Se han acabado los sellos de tiempo de documento, validar las firmas");
			ArrayList<String> allSignatureNames = af.getSignatureNames();
			Collections.sort(allSignatureNames);
			ArrayList<String> noTimestampNames = new ArrayList<String>();
			for (Iterator iterator = allSignatureNames.iterator(); iterator.hasNext();) {
				String name = (String) iterator.next();
				if (af.getSignatureDictionary(name).get(PdfName.SUBFILTER) == null ||
						!af.getSignatureDictionary(name).get(PdfName.SUBFILTER).equals(new PdfName("ETSI.RFC3161"))) {
	
					noTimestampNames.add(name);
					
				}
			}
	
			logger.debug("[PAdESLTVSignature.isValid*]::Se van a validar " + noTimestampNames.size() + " firmas");
			List<ValidationResult> results = new ArrayList<ValidationResult>(); 
			for (int i = 0; i < noTimestampNames.size(); i++) {
				String name = noTimestampNames.get(i);
				
				//-- Obtener el pkcs7
				PdfPKCS7 pkcs7 = af.verifySignature(name, CRYPTOGRAPHIC_PROVIDER_NAME);
				
				//-- Validar que el PKCS#7 se corresponde con el documento
				try {
					if (!pkcs7.verify()) {
						logger.info("[PAdESLTVSignature.isValid]::La firma no se corresponde con el documento");
						results.add(new ValidationResult(ValidationResult.RESULT_SIGNATURE_NOT_MATCH_DATA, pkcs7.getSigningCertificate(), ts.getTime(), ts, null));
						continue;
					}
				} catch (java.security.SignatureException e) {
					logger.info("[PAdESLTVSignature.isValid]::Error en una de las firmas del PDF", e);
					throw new SignatureException ("Error en una de las firmas del PDF", e);
				}
				
				//-- Si se ha definido un hueco para firmar pero no se ha rellenado llegamos hasta aquí pero
				//-- no hay certificado de firma
				Certificate signingCertificate = getSigningCertificate(pkcs7.getCertificates());
				if (signingCertificate != null) {
					//-- Obtener el certificado de la firma y la cadena de certificación de éste
					ValidateCertificate signatureCertificate;
					try {
						signatureCertificate = new ValidateCertificate (signingCertificate.toDER(), caList);
					} catch (CertificateCANotFoundException e) {
						logger.info("[PAdESLTVSignature.isValid]::Certificado" + i + " no pertenece a ninguna de las CAs de confianza");
						results.add(new ValidationResult(ValidationResult.RESULT_CERTIFICATE_NOT_BELONGS_TRUSTED_CAS, pkcs7.getSigningCertificate(), ts.getTime(), ts, null));
						continue;
					}
					List<ValidateCertificate> chainCertificate = signatureCertificate.getCompleteCertificationChainAsList();
					
					//-- Obtener el certificado del sello de tiempos y la cadena de certificación de éste: en las versiones
					//-- antiguas no se tiene en cuenta el certificado del sello de tiempos
					TimeStamp timestamp = null;
					List<ValidateCertificate> chainTimestamp = null;
					ValidateCertificate timestampCertificate = null;
					try {
						if (pkcs7.getTimeStampToken() != null) {
							timestamp = new TimeStamp(pkcs7.getTimeStampToken().getEncoded());
						}
					} catch (Exception e) {
						logger.info("[PAdESLTVSignature.isValid]::No es posible leer el sello de tiempos interno de la firma", e);
					}
					
					//-- La fecha de validación será la de este sello de tiempos
					if (timestamp != null) {
						ts = timestamp;
					}
					
					if (timestamp != null && !olderVersionsAllowed) {
						
						//-- Validar el certificado del sello
						try {
							timestampCertificate = new ValidateCertificate (timestamp.getSignatureCertificate().toX509Certificate(), caList);
						} catch (CertificateCANotFoundException e) {
							logger.info("[PAdESLTVSignature.isValid]::Certificado de sello de tiempos " + i + " no pertenece a ninguna de las CAs de confianza");
							results.add(new ValidationResult(ValidationResult.RESULT_CERTIFICATE_NOT_BELONGS_TRUSTED_CAS, pkcs7.getSigningCertificate(), ts.getTime(), ts, null));
							continue;
						} catch (MalformedTimeStampException e) {
							logger.info("[PAdESLTVSignature.isValid]::No se ha podido obtener el certificado del sello de tiempos " + i, e);
							results.add(new ValidationResult(ValidationResult.RESULT_CERTIFICATE_CHAIN_VALIDATION_INVALID, pkcs7.getSigningCertificate(), ts.getTime(), ts, null));
							continue;
						}
						chainTimestamp = timestampCertificate.getCompleteCertificationChainAsList();
					}
					
					//-- Obtener todos los certificados que deben estar en el dss
					List<ValidateCertificate> certificadosFirma = juntarListasDSS (signatureCertificate, timestampCertificate, chainCertificate, chainTimestamp);
					
					//-- Validar
					results.add(validarCertificadosContraDSS(certificadosFirma, signatureCertificate, timestampCertificate, dss, 
							certificadosDSS, fechaComprobacion, ts, caList));
				}
			}
			
			//-- Devolver resultado
			return results.toArray(new ValidationResult[0]);

		} finally {
			if (reader != null) {
				reader.close();
			}
		}
	}
	
	/*
	 * Valida una lista de certificados contra una DSS
	 */
	private ValidationResult validarCertificadosContraDSS (List<ValidateCertificate> certificadosAValidar, ValidateCertificate certificadoFirma,
			ValidateCertificate certificadoTS, PdfDocumentSecurityStore dss, List<Certificate> certificadosDSS, 
			Date fechaComprobacion, TimeStamp ts, CAList caList) throws SignatureException {
		
		int resultadoValidacion = ValidationResult.RESULT_VALID;
		
		//-- Para cada certificado comprobar que o es autofirmado o existe una respuesta OCSP válida
		List<OCSPResponse> lOcspResponses = new ArrayList<OCSPResponse>();
		for (Iterator<ValidateCertificate> iterator = certificadosAValidar.iterator(); iterator.hasNext();) {
			
			ValidateCertificate certificadoAValidar = iterator.next();
			
			//-- Comprobar que el certificado existe en el DSS (es opcional - sólo lo pondremos en los logs)
			if (!certificadosDSS.contains(certificadoAValidar)) {
				logger.info("[PAdESLTVSignature.isValid]::El certificado no se encuentra en el DSS::" + certificadoAValidar);
//				if(certificadoAValidar.equals(certificadoFirma) || (certificadoTS != null && certificadoAValidar.equals(certificadoTS))) {
//					resultadoValidacion = ValidationResult.RESULT_CERTIFICATE_CANNOT_BE_VALIDATED;
//				} else {
//					resultadoValidacion = ValidationResult.RESULT_CERTIFICATE_CHAIN_VALIDATION_INVALID;
//				}
//				break;
			}
			
			//-- Si es autofirmado se continua
			try {
				if(certificadoAValidar.isSelfSigned()) {
					continue;
				}
			} catch (NormalizeCertificateException e) {
				logger.info("[PAdESLTVSignature.isValid]::Uno de los certificados no puede ser normalizado:: " + certificadoAValidar.getCommonName(), e);
				throw new SignatureException ("Uno de los certificados no puede ser normalizado", e);
			}
			
			//-- Obtener y validar la respuesta OCSP
			boolean encontradaRespuestaOCSP = false;
			Set<Integer> setKeys = dss.getOcsps().keySet();
			for (Integer key : setKeys) {
				OCSPResponse ocspResponse;
				try {
					ocspResponse = new OCSPResponse (dss.getOcsps().get(key));
				} catch (MalformedOCSPResponseException e) {
					logger.info("[PAdESLTVSignature.isValid]::Una de las respuestas OCSP del DSS está mal formada", e);
					break;
				}
				
				if (ocspResponse.getSingleResponses()[0].match(certificadoAValidar)) {
					logger.debug("[PAdESLTVSignature.isValid]::Encontrada respuesta OCSP para el certificado. Estado: " + ocspResponse.getSingleResponses()[0].getStatus());
					encontradaRespuestaOCSP = true;
					
					//-- Comprobar que la respuesta OCSP sea válida
					if (!ocspResponse.isSignatureValid()) {
						if(certificadoAValidar.equals(certificadoFirma) || (certificadoTS != null && certificadoAValidar.equals(certificadoTS))) {
							resultadoValidacion = ValidationResult.RESULT_INVALID_VALIDITY_ITEM;
						} else {
							resultadoValidacion = ValidationResult.RESULT_CERTIFICATE_CHAIN_VALIDATION_INVALID;
						}
					}
					
					//-- Comprobar el estado de la respuesta
					if (ocspResponse.getSingleResponses()[0].getStatus() != ValidationResult.RESULT_VALID) {
						if(certificadoAValidar.equals(certificadoFirma) || (certificadoTS != null && certificadoAValidar.equals(certificadoTS))) {
							resultadoValidacion = ocspResponse.getSingleResponses()[0].getStatus();
						} else {
							resultadoValidacion = ValidationResult.RESULT_CERTIFICATE_CHAIN_VALIDATION_INVALID;
						}
						continue;
					}
					
					//-- Comprobar que la fecha de la respuesta OCSP sea posterior a la fecha (del sello de tiempos del documento 
					//-- o del sello de tiempos de la firma)
					if (ocspResponse.getSingleResponses()[0].getValidityPeriodEnd() != null && 
							ocspResponse.getSingleResponses()[0].getValidityPeriodEnd().before(fechaComprobacion)) {
						logger.debug("[PAdESLTVSignature.isValid]::La fecha de la respuesta OCSP (" + ocspResponse.getSingleResponses()[0].getValidityPeriodEnd() + 
								") es anterior a la fecha de comprobación (" + fechaComprobacion + ")");
						if(certificadoAValidar.equals(certificadoFirma) || (certificadoTS != null && certificadoAValidar.equals(certificadoTS))) {
							resultadoValidacion = ValidationResult.RESULT_TIMESTAMP_AFTER_VALIDITY_ITEM;
						} else {
							resultadoValidacion = ValidationResult.RESULT_CERTIFICATE_CHAIN_VALIDATION_INVALID;
						}
						continue;
					}
					
					//-- Respuesta OCSP OK
					logger.debug("[PAdESLTVSignature.isValid]::La respuesta OCSP es correcta para el certificado de CN=" + certificadoFirma.getCommonName());
					resultadoValidacion = ValidationResult.RESULT_VALID;
					break;
				}
			}
			
			//-- Si se ha encontrado una respuesta válida pasamos al siguiente certificado
			if (encontradaRespuestaOCSP && resultadoValidacion == ValidationResult.RESULT_VALID) {
				continue;
			}
			
			//-- Si no se encuentra una respuesta OCSP o esta no es válida probar con CRL
			logger.debug("[PAdESLTVSignature.isValid]::No se ha encontrado una respuesta OCSP para el certificado. Probar con CRLs");
			
			boolean encontradaCRL = false;
			setKeys = dss.getCrls().keySet();
			for (Integer key : setKeys) {
				try {
					CRL crl = new CRL (dss.getCrls().get(key));
					
					logger.debug("[PAdESLTVSignature.isValid]::Obtenida CRL, comprobar si sirve para validar el certificado");
					if (crl.match(certificadoAValidar)) {
					
						//-- Comprobar que la CRL sea válida
						try {
							crl.validate(caList);
						} catch (Exception e) {
							if(certificadoAValidar.equals(certificadoFirma) || (certificadoTS != null && certificadoAValidar.equals(certificadoTS))) {
								resultadoValidacion = ValidationResult.RESULT_INVALID_VALIDITY_ITEM;
							} else {
								resultadoValidacion = ValidationResult.RESULT_CERTIFICATE_CHAIN_VALIDATION_INVALID;
							}
							continue;
						} 
						
						//-- Comprobar que el certificado no esté entre los revocados
						encontradaCRL = true;
						logger.debug("[PAdESLTVSignature.isValid]::Obtenida CRL, comprobar si el certificado está entre los revocados");
						if (crl.isRevoked(certificadoAValidar.getSerialNumberBigInteger())) {
							if(certificadoAValidar.equals(certificadoFirma) || (certificadoTS != null && certificadoAValidar.equals(certificadoTS))) {
								resultadoValidacion = ValidationResult.RESULT_CERTIFICATE_REVOKED;
							} else {
								resultadoValidacion = ValidationResult.RESULT_CERTIFICATE_CHAIN_VALIDATION_INVALID;
							}
							continue;
						}
						
						//-- Comprobar que la fecha de la CRL sea posterior a la fecha (del sello de tiempos del documento 
						//-- o del sello de tiempos de la firma)
						if (crl.getValidityPeriodEnd().before(fechaComprobacion)) {
							logger.debug("[PAdESLTVSignature.isValid]::La fecha de la CRL (" + crl.getValidityPeriodEnd() + 
									") es anterior a la fecha de comprobación (" + fechaComprobacion + ")");
							if(certificadoAValidar.equals(certificadoFirma) || (certificadoTS != null && certificadoAValidar.equals(certificadoTS))) {
								resultadoValidacion = ValidationResult.RESULT_TIMESTAMP_AFTER_VALIDITY_ITEM;
							} else {
								resultadoValidacion = ValidationResult.RESULT_CERTIFICATE_CHAIN_VALIDATION_INVALID;
							}
							continue;
						}

						//-- CRL OK
						logger.debug("[PAdESLTVSignature.isValid]::La CRL es correcta para el certificado de CN=" + certificadoFirma.getCommonName());
						resultadoValidacion = ValidationResult.RESULT_VALID;
						break;
					}
					
				} catch (CRLParsingException e) {
					logger.info("[PAdESLTVSignature.isValid]::No se ha podido cargar una de las CRLs contenidas en el DSS", e);
				}
			}

			//-- Si se ha encontrado una CRL correcta pasamos al siguiente certificado
			if (encontradaCRL && resultadoValidacion == ValidationResult.RESULT_VALID) {
				continue;
			}
			
			//-- Si no se ha encontrado ni CRL ni respuesta OCSP la validación es incorrecta
			if (!encontradaRespuestaOCSP && !encontradaCRL) {
				logger.debug("[PAdESLTVSignature.isValid]::No se ha encontrado una CRL para validar el certificado");
				if(certificadoAValidar.equals(certificadoFirma)) {
					resultadoValidacion = ValidationResult.RESULT_CERTIFICATE_CANNOT_BE_VALIDATED;
				} else {
					resultadoValidacion = ValidationResult.RESULT_CERTIFICATE_CHAIN_VALIDATION_INVALID;
				}
			}
			
			//-- Si llegamos aquí no se ha encontrado ni respuesta OCSP ni CRL que confirmen la validez del certificado,
			//-- así que ya no se miran los demás porque ya hay un error				
			break;
		}
		
		return new ValidationResult(resultadoValidacion, certificadoFirma.toX509Certificate(), ts.getTime(), ts, lOcspResponses.toArray(new OCSPResponse[0]));
		
	}
	
	//-- Classes
	
	static class MyPdfPKCS extends PdfSigGenericPKCS {
        /**
         * The constructor for the default provider.
         */
        public MyPdfPKCS() {
                super(PdfName.ADOBE_PPKMS, (embedOcspInSignature) ? PdfName.ADBE_PKCS7_DETACHED : PdfName.ADBE_PKCS7_SHA1);
                hashAlgorithm = HASH_ALGORITHM;
        }

        /**
         * The constructor for an explicit provider.
         *
         * @param provider the crypto provider
         */
        public MyPdfPKCS(String provider) {
                this();
                this.provider = provider;
        }
    }
	
	public static class TimeStampDictionary implements Comparable<TimeStampDictionary>{
		String name;
		TimeStamp ts;
		PdfDictionary dictionary;
		public TimeStampDictionary(String name, TimeStamp ts, PdfDictionary dictionary) {
			super();
			this.name = name;
			this.ts = ts;
			this.dictionary = dictionary;
		}
		public String getName() {
			return name;
		}
		public void setName(String name) {
			this.name = name;
		}
		public TimeStamp getTs() {
			return ts;
		}
		public void setTs(TimeStamp ts) {
			this.ts = ts;
		}
		public PdfDictionary getDictionary() {
			return dictionary;
		}
		public void setDictionary(PdfDictionary dictionary) {
			this.dictionary = dictionary;
		}
		public int compareTo(TimeStampDictionary o) {
			return this.ts.getTime().compareTo(o.getTs().getTime());
		}
		
	}

}

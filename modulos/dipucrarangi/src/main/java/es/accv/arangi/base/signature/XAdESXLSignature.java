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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.MessageDigest;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.signature.XMLSignatureException;
import org.apache.xml.security.transforms.Transforms;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.accv.arangi.base.certificate.Certificate;
import es.accv.arangi.base.certificate.validation.CAList;
import es.accv.arangi.base.device.DeviceManager;
import es.accv.arangi.base.document.FileDocument;
import es.accv.arangi.base.document.IDocument;
import es.accv.arangi.base.document.URLDocument;
import es.accv.arangi.base.exception.TimeStampException;
import es.accv.arangi.base.exception.certificate.NormalizeCertificateException;
import es.accv.arangi.base.exception.device.LoadingObjectException;
import es.accv.arangi.base.exception.device.SearchingException;
import es.accv.arangi.base.exception.document.HashingException;
import es.accv.arangi.base.exception.document.InitDocumentException;
import es.accv.arangi.base.exception.signature.CounterSignatureException;
import es.accv.arangi.base.exception.signature.NoCoincidentDocumentException;
import es.accv.arangi.base.exception.signature.NoDocumentToSignException;
import es.accv.arangi.base.exception.signature.RetrieveOCSPException;
import es.accv.arangi.base.exception.signature.SignatureException;
import es.accv.arangi.base.exception.signature.SignatureNotFoundException;
import es.accv.arangi.base.exception.signature.XMLDocumentException;
import es.accv.arangi.base.exception.timestamp.MalformedTimeStampException;
import es.accv.arangi.base.exception.timestamp.ResponseTimeStampException;
import es.accv.arangi.base.exception.timestamp.TimeStampServerConnectionException;
import es.accv.arangi.base.mityc.CAListCertStatusRecover;
import es.accv.arangi.base.mityc.ContraFirmaXML;
import es.accv.arangi.base.signature.util.TSAData;
import es.accv.arangi.base.signature.util.XAdESAttachedNodeToSignObject;
import es.accv.arangi.base.signature.util.XAdESAttachedSignatureOptions;
import es.accv.arangi.base.signature.util.XAdESDataObjectFormat;
import es.accv.arangi.base.signature.util.XAdESDetachedSignatureOptions;
import es.accv.arangi.base.timestamp.TimeStamp;
import es.accv.arangi.base.util.Util;
import es.accv.arangi.base.util.validation.ValidationResult;
import es.mityc.firmaJava.libreria.ConstantesXADES;
import es.mityc.firmaJava.libreria.excepciones.AddXadesException;
import es.mityc.firmaJava.libreria.utilidades.Base64Coder;
import es.mityc.firmaJava.libreria.utilidades.I18n;
import es.mityc.firmaJava.libreria.utilidades.NombreNodo;
import es.mityc.firmaJava.libreria.utilidades.UtilidadFechas;
import es.mityc.firmaJava.libreria.utilidades.UtilidadFicheros;
import es.mityc.firmaJava.libreria.utilidades.UtilidadFirmaElectronica;
import es.mityc.firmaJava.libreria.utilidades.UtilidadTratarNodo;
import es.mityc.firmaJava.libreria.xades.CanonicalizationEnum;
import es.mityc.firmaJava.libreria.xades.DataToSign;
import es.mityc.firmaJava.libreria.xades.EnumFormatoFirma;
import es.mityc.firmaJava.libreria.xades.RespYCerts;
import es.mityc.firmaJava.libreria.xades.ResultadoEnum;
import es.mityc.firmaJava.libreria.xades.ResultadoValidacion;
import es.mityc.firmaJava.libreria.xades.UtilidadXadesA;
import es.mityc.firmaJava.libreria.xades.XAdESSchemas;
import es.mityc.firmaJava.libreria.xades.elementos.xades.CRLRef;
import es.mityc.firmaJava.libreria.xades.elementos.xades.EncapsulatedX509Certificate;
import es.mityc.firmaJava.libreria.xades.errores.BadFormedSignatureException;
import es.mityc.firmaJava.libreria.xades.errores.FirmaXMLError;
import es.mityc.firmaJava.ts.ConstantesTSA;
import es.mityc.firmaJava.ts.TSCliente;
import es.mityc.firmaJava.ts.TSClienteError;
import es.mityc.javasign.certificate.CertStatusException;
import es.mityc.javasign.certificate.ICertStatus;
import es.mityc.javasign.certificate.IOCSPCertStatus;
import es.mityc.javasign.certificate.IX509CRLCertStatus;

/**
 * Clase que maneja firmas en formato XAdES-X-L de acuerdo al estándar 
 * <a href="http://uri.etsi.org/01903/v1.3.2/ts_101903v010302p.pdf" target="etsi">
 * ETSI TS 101 903</a><br><br>
 * 
 * El formato XAdES-A también es tratado por esta clase. Gracias al método 
 * {@link #getTimeStampCertificateExpiration() getTimeStampCertificateExpiration} es
 * posible determinar la fecha de caducidad del certificado de TSA. Antes de que se
 * llegue a esta fecha es conveniente resellar la firma con un sello de archivo
 * mediante el método {@link #addArchiveTimeStamp(URL) addArchiveTimeStamp}.<br><br>
 *  
 * Ejemplo de uso: <br><br>
 * 
 * <code> 
 * KeyStoreManager manager = new KeyStoreManager (..., ...);<br>
 * String alias = ...;<br>
 * InputStreamDocument documentTexto = new InputStreamDocument (new FileInputStream (...));<br>
 * InputStreamDocument documentXML = new InputStreamDocument (new FileInputStream (...));<br>
 * File file = new File (...);<br>
 * URL url = new URL (...);<br>
 * URL urlTSA = new URL (...);<br>
 * CAList caList = new CAList (...);<br><br>
 * 
 * //-- Genera una firma attached. El documento se guardará en la firma en base64<br>
 * XAdESXLSignature signature1 = XAdESXLSignature.signAttached(manager, alias, documentTexto, urlTSA, caList);<br><br>
 * 
 * //-- Genera una firma detached que referencia al fichero en disco<br>
 * XAdESXLSignature signature2 = XAdESXLSignature.signDetached(manager, alias, file, urlTSA, caList);<br><br>
 * 
 * //-- Genera una firma detached que referencia a "2011/04/29/certificados/CER-2584665.pdf"<br>
 * XAdESXLSignature signature3 = XAdESXLSignature.signDetached(manager, alias, file, "2011/04/29/certificados/CER-2584665.pdf", urlTSA, caList);<br><br>
 * 
 * //-- Genera una firma detached que referencia al fichero ubicado en la URL<br>
 * XAdESXLSignature signature4 = XAdESXLSignature.signDetached(manager, alias, url, urlTSA, caList);<br><br>
 * 
 * //-- Genera una firma attached dentro del propio documento<br>
 * XAdESXLSignature signature5 = XAdESXLSignature.signAttached(manager, alias, documentoXML, "titulo", "documento", urlTSA, caList);<br><br>
 * </code>
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José M Gutiérrez</a>
 */
public class XAdESXLSignature extends XAdESSignature {
	
	/*
	 * Logger de la clase
	 */
	static Logger logger = Logger.getLogger(XAdESXLSignature.class);
	
	/**
	 * Tipo de la firma
	 */
	public static final String SIGNATURE_TYPE	= "XAdES-X-L";
	
	/*
	 * Referencia por defecto al node SignatureTimeStamp
	 */
	protected static final String DEFAULT_SIGNATURE_TIMESTAMP_ID 	= "SignTimeStamp";
	
	/*
	 * URI para espacios de nombres XADES
	 */
	protected static final String NAMESPACE_XADES_URI = "http://uri.etsi.org/01903/v1.3.2#";

	/*
	 * Constante que indica el algoritmo de canonicalización por defecto
	 */
	protected static final String DEFAULT_CANONICALIZATION_ALGORITHM	= CanonicalizationMethod.INCLUSIVE;
	

	//-- Constructores
	
	/**
	 * Construye el objeto en base a un XML que tiene el formato
	 * XAdES-X-L
	 * 
	 * @param xmlDocument Documento XML
	 */
	public XAdESXLSignature(Document xmlDocument) {
		initialize (xmlDocument);
	}
	
	/**
	 * Construye el objeto en base a un fichero XAdES-X-L
	 * 
	 * @param xmlFile Fichero XAdES-X-L
	 * @throws FileNotFoundException El fichero no existe
	 * @throws XMLDocumentException El fichero no parece un XML válido
	 */
	public XAdESXLSignature(File xmlFile) throws FileNotFoundException, XMLDocumentException {
		initialize (xmlFile);
	}

	/**
	 * Construye el objeto en base a un array de bytes.
	 * 
	 * @param signature Firma XAdES-X-L
	 * @throws XMLDocumentException El fichero no parece un XML válido
	 */
	public XAdESXLSignature(byte[] signature) throws XMLDocumentException {
		initialize (signature);
	}

	/**
	 * Construye el objeto en base a un stream de lectura.
	 * 
	 * @param isSignature Stream de lectura a una firma XAdES-X-L
	 * @throws XMLDocumentException El fichero no parece un XML válido
	 */
	public XAdESXLSignature(InputStream isSignature) throws XMLDocumentException {
		initialize(isSignature);
	}

	/**
	 * Construye el objeto en base a otro XAdES-X-L
	 * 
	 * @param xlSignature Firma XAdES-X-L
	 */
	protected XAdESXLSignature(XAdESXLSignature xlSignature) {
		xadesDocument = xlSignature.xadesDocument;
	}
	
	//-- Métodos públicos
	
	/**
	 * Obtiene la fecha del sello de tiempos de la firma (Punto 7.3 del estándar).
	 * 
	 * @return Fecha del sello de tiempos
	 * @throws MalformedTimeStampException El sello de tiempos guardado en el fichero XAdES-X-L no 
	 * está bien formado
	 */
	public Date getTimeStampTime () throws MalformedTimeStampException {
		return getXAdESTimeStampTime();

	}
	
	/**
	 * Realiza una firma XAdES-X-L detached (el fichero no se incluirá en la firma). No completa los campos 
	 * no obligatorios del tag 'SignedSignatureProperties':'signaturePolicyIdentifier', 
	 * 'signatureProductionPlace' y 'signerRole'.
	 * 
	 * La referencia a la que apuntará la firma será el path del fichero.
	 * 
	 * @param manager Dispositivo criptográfico que realizará la firma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param document Fichero a firmar
	 * @param urlTSA URL del servidor de sello de tiempos
	 * @param caList Lista de CAs para obtener información de validación
	 * @return Firma XADES-XL
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 * @throws NoDocumentToSignException El fichero a firmar no existe o es nulo
	 * @deprecated Usar {@link #signDetached(DeviceManager,String,IDocument,String,TSAData,CAList,XAdESDetachedSignatureOptions) signDetached}
	 */
	public static XAdESXLSignature signDetached (DeviceManager manager, String alias, File document, URL urlTSA, CAList caList) throws LoadingObjectException, SignatureException, NoDocumentToSignException {
		return signDetached(manager, alias, document, null, urlTSA, caList, null, null);
	}
	
	/**
	 * Realiza una firma XAdES-X-L detached (el fichero no se incluirá en la firma). No completa los campos 
	 * no obligatorios del tag 'SignedSignatureProperties':'signaturePolicyIdentifier' y
	 * 'signatureProductionPlace'.
	 * 
	 * La referencia a la que apuntará la firma será el path del fichero.
	 * 
	 * @param manager Dispositivo criptográfico que realizará la firma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param document Fichero a firmar
	 * @param digitalSignatureAlgorithm Algoritmo de firma (si nulo algoritmo por defecto)
	 * @param urlTSA URL del servidor de sello de tiempos
	 * @param caList Lista de CAs para obtener información de validación
	 * @param dof Información para construir el tag DataObjectFormat (puede ser null)
	 * @param claimedRoles Roles de la firma (puede ser null)
	 * @return Firma XADES-XL
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 * @throws NoDocumentToSignException El fichero a firmar no existe o es nulo
	 * @deprecated Usar {@link #signDetached(DeviceManager,String,IDocument,String,TSAData,CAList,XAdESDetachedSignatureOptions) signDetached}
	 */
	public static XAdESXLSignature signDetached (DeviceManager manager, String alias, File document, 
			String digitalSignatureAlgorithm, URL urlTSA, CAList caList, XAdESDataObjectFormat dof, 
			String[] claimedRoles) throws LoadingObjectException, SignatureException, NoDocumentToSignException {
		
		logger.debug("[XAdESXLSignature.signDetached]::Entrada::" + Arrays.asList(new Object[] { manager, alias, document, digitalSignatureAlgorithm, urlTSA, caList, dof, claimedRoles }));
		
		//-- Obtener el document de Arangi y llamar a la firma attached
		FileDocument fileDocument;
		try {
			fileDocument = new FileDocument(document);
		} catch (InitDocumentException e) {
			logger.info("[XAdESXLSignature.signDetached]::El fichero a firmar no existe o es nulo:: " + document);
			throw new NoDocumentToSignException("El fichero a firmar no existe o es nulo: " + document);
		}
		
		//-- Obtener la referencia al fichero
		String reference = UtilidadFicheros.relativizeRute("#", document);
		
		return signDetached(manager, alias, fileDocument, digitalSignatureAlgorithm, reference, urlTSA, caList, dof, claimedRoles);
		
	}
	
	/**
	 * Realiza una firma XAdES-XL detached (el fichero no se incluirá en la firma). No completa los campos 
	 * no obligatorios del tag 'SignedSignatureProperties':'signaturePolicyIdentifier', 
	 * 'signatureProductionPlace' y 'signerRole'.
	 * 
	 * La referencia a la que apuntará la firma será la URL del documento.
	 * 
	 * @param manager Dispositivo criptográfico que realizará la firma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param urlDocument Documento a firmar. Se encuentra en una URL accesible.
	 * @param urlTSA URL del servidor de sello de tiempos
	 * @param caList Lista de CAs para obtener información de validación
	 * @return Firma XADES-XL
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 * @throws NoDocumentToSignException La URL a firmar no existe o es nula
	 * @deprecated Usar {@link #signDetached(DeviceManager,String,IDocument,String,TSAData,CAList,XAdESDetachedSignatureOptions) signDetached}
	 */
	public static XAdESXLSignature signDetached (DeviceManager manager, String alias, URL urlDocument, URL urlTSA, CAList caList) throws LoadingObjectException, SignatureException, NoDocumentToSignException {
		return signDetached(manager, alias, urlDocument, null, urlTSA, caList, null, null);
	}
	
	/**
	 * Realiza una firma XAdES-XL detached (el fichero no se incluirá en la firma). No completa los campos 
	 * no obligatorios del tag 'SignedSignatureProperties':'signaturePolicyIdentifier' y 
	 * 'signatureProductionPlace'.
	 * 
	 * La referencia a la que apuntará la firma será la URL del documento.
	 * 
	 * @param manager Dispositivo criptográfico que realizará la firma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param urlDocument Documento a firmar. Se encuentra en una URL accesible.
	 * @param digitalSignatureAlgorithm Algoritmo de firma (si nulo algoritmo por defecto)
	 * @param urlTSA URL del servidor de sello de tiempos
	 * @param caList Lista de CAs para obtener información de validación
	 * @param dof Información para construir el tag DataObjectFormat (puede ser null)
	 * @param claimedRoles Roles de la firma (puede ser null)
	 * @return Firma XADES-XL
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 * @throws NoDocumentToSignException La URL a firmar no existe o es nula
	 * @deprecated Usar {@link #signDetached(DeviceManager,String,IDocument,String,TSAData,CAList,XAdESDetachedSignatureOptions) signDetached}
	 */
	public static XAdESXLSignature signDetached (DeviceManager manager, String alias, URL urlDocument, 
			String digitalSignatureAlgorithm, URL urlTSA, CAList caList, XAdESDataObjectFormat dof, 
			String[] claimedRoles) throws LoadingObjectException, SignatureException, NoDocumentToSignException {
		
		logger.debug("[XAdESXLSignature.signDetached]::Entrada::" + Arrays.asList(new Object[] { manager, alias, urlDocument, digitalSignatureAlgorithm, urlTSA, caList, dof, claimedRoles }));
		
		//-- Obtener el document de Arangi y llamar a la firma attached
		URLDocument document;
		try {
			document = new URLDocument(urlDocument);
		} catch (InitDocumentException e) {
			logger.info("[XAdESXLSignature.signDetached]::La URL a firmar no existe o es nula:: " + urlDocument);
			throw new NoDocumentToSignException("La URL a firmar no existe o es nula: " + urlDocument);
		}
		
		//-- Obtener la referencia al fichero
		String reference = urlDocument.toString();
		
		return signDetached(manager, alias, document, digitalSignatureAlgorithm, reference, urlTSA, caList, dof, claimedRoles);
	}
	
	/**
	 * Realiza una firma XAdES-XL detached (el fichero no se incluirá en la firma). No completa los campos 
	 * no obligatorios del tag 'SignedSignatureProperties':'signaturePolicyIdentifier', 
	 * 'signatureProductionPlace' y 'signerRole'.
	 * 
	 * @param manager Dispositivo criptográfico que realizará la firma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param document Documento a firmar. 
	 * @param reference Referencia a la que apuntará la firma
	 * @param urlTSA URL del servidor de sello de tiempos
	 * @param caList Lista de CAs para obtener información de validación
	 * @return Firma XADES-XL
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 * @deprecated Usar {@link #signDetached(DeviceManager,String,IDocument,String,TSAData,CAList,XAdESDetachedSignatureOptions) signDetached}
	 */
	public static XAdESXLSignature signDetached (DeviceManager manager, String alias, IDocument document, String reference, URL urlTSA, CAList caList) throws LoadingObjectException, SignatureException {
		return signDetached(manager, alias, document, reference, null, urlTSA, caList, null, null);
	}
	
	/**
	 * Realiza una firma XAdES-XL detached (el fichero no se incluirá en la firma). No completa los campos 
	 * no obligatorios del tag 'SignedSignatureProperties':'signaturePolicyIdentifier' y
	 * 'signatureProductionPlace'.
	 * 
	 * @param manager Dispositivo criptográfico que realizará la firma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param document Documento a firmar. 
	 * @param digitalSignatureAlgorithm Algoritmo de firma (si nulo algoritmo por defecto)
	 * @param reference Referencia a la que apuntará la firma
	 * @param urlTSA URL del servidor de sello de tiempos
	 * @param caList Lista de CAs para obtener información de validación
	 * @param dof Información para construir el tag DataObjectFormat (puede ser null)
	 * @param claimedRoles Roles de la firma (puede ser null)
	 * @return Firma XADES-XL
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 * @deprecated Usar {@link #signDetached(DeviceManager,String,IDocument,String,TSAData,CAList,XAdESDetachedSignatureOptions) signDetached}
	 */
	public static XAdESXLSignature signDetached (DeviceManager manager, String alias, IDocument document, 
			String digitalSignatureAlgorithm, String reference, URL urlTSA, CAList caList, XAdESDataObjectFormat dof, String[] claimedRoles) throws LoadingObjectException, SignatureException {
		
		logger.debug("[XAdESXLSignature.signDetached]::Entrada::" + Arrays.asList(new Object[] { manager, alias, document, digitalSignatureAlgorithm, reference, urlTSA, caList, dof, claimedRoles }));
		
		TSAData tsaData = new TSAData(urlTSA);
		XAdESDetachedSignatureOptions options = new XAdESDetachedSignatureOptions(digitalSignatureAlgorithm, null, dof, claimedRoles, null, null);
		return signDetached(manager, alias, document, reference, tsaData, caList, options);
	}
	
	/**
	 * Realiza una firma XAdES-XL detached (el fichero no se incluirá en la firma). Si no
	 * se puede obtener la información de validación mediante OCSP se producirá una
	 * excepción.
	 * 
	 * @param manager Dispositivo criptográfico que realizará la firma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param document Documento a firmar. 
	 * @param reference Referencia a la que apuntará la firma
	 * @param tsaData Información de conexión a la TSA
	 * @param caList Lista de CAs para obtener información de validación
	 * @param options Opciones de la firma
	 * @return Firma XADES-XL
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 */
	public static XAdESXLSignature signDetached (DeviceManager manager, String alias, IDocument document, 
			String reference, TSAData tsaData, CAList caList, XAdESDetachedSignatureOptions options) throws LoadingObjectException, SignatureException {
		
		return signDetached(manager, alias, document, reference, tsaData, caList, options, false);
	}
	
	/**
	 * Realiza una firma XAdES-XL detached (el fichero no se incluirá en la firma). <br><br>
	 * 
	 * Utilizar este método si se desea permitir que se obtenga la información de validación mediante 
	 * CRL (en caso de que no se pueda mediante OCSP). Cuidado con esta opción ya que las CRLs pueden 
	 * tener un tamaño considerable, por lo que la obtención de la firma será más lenta y la misma firma
	 * puede acabar con un tamaño muy grande.
	 * 
	 * @param manager Dispositivo criptográfico que realizará la firma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param document Documento a firmar. 
	 * @param reference Referencia a la que apuntará la firma
	 * @param tsaData Información de conexión a la TSA
	 * @param caList Lista de CAs para obtener información de validación
	 * @param options Opciones de la firma
	 * @param allowCRLValidation Permitir generar la firma con CRLs si no se puede
	 *  realizar la validación mediante OCSP
	 * @return Firma XADES-XL
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 */
	public static XAdESXLSignature signDetached (DeviceManager manager, String alias, IDocument document, 
			String reference, TSAData tsaData, CAList caList, XAdESDetachedSignatureOptions options,
			boolean allowCRLValidation) throws LoadingObjectException, SignatureException {
		
		logger.debug("[XAdESXLSignature.signDetached]::Entrada::" + Arrays.asList(new Object[] { manager, alias, document, reference, tsaData, caList, options, allowCRLValidation }));
		
		return (XAdESXLSignature) signDetached(manager, alias, document, reference, tsaData, new CAListCertStatusRecover(caList, allowCRLValidation), options, EnumFormatoFirma.XAdES_XL, XAdESXLSignature.class);
	}
	
	/**
	 * Realiza una firma XAdES-XL atached (el documento se incluye en la firma). No completa los campos no 
	 * obligatorios del tag 'SignedSignatureProperties':'signaturePolicyIdentifier', 'signatureProductionPlace' 
	 * y 'signerRole'.
	 * 
	 * Si el documento es un XML y los parámetros <code>idToSign</code> y <code>signatureParent</code>
	 * no son nulos la firma y los campos propios de XAdES se añadirán al XML. En caso contrario el fichero 
	 * XAdES resultante seguirá la plantilla de Arangí, por ejemplo:<br>
	 * <code>
	 * 	&lt;arangi-xades&gt;<br>
	 *  &nbsp;&nbsp;&lt;document&gt;...&lt;/document&gt;
	 *  &nbsp;&nbsp;&lt;ds:Signature&gt;...&lt;/ds:Signature&gt;
	 * 	&lt;/arangi-xades&gt;<br>
	 * </code>
	 * 
	 * @param manager Dispositivo criptográfico que realizará la firma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param document Documento a firmar
	 * @param idToSign Valor del atributo 'ID' que indica lo que se firmará dentro del documento. Si tiene 
	 *  valor nulo el XML de la firma tendrá el formato por defecto de las firmas XAdES de Arangí.
	 * @param signatureParent Nombre del tag que será el padre de los nodos de firma. Si tiene valor nulo
	 * 	la firma colgará del nodo raíz.
	 * @param urlTSA URL del servidor de sello de tiempos
	 * @param caList Lista de CAs para obtener información de validación
	 * @return Firma XADES-XL
	 * @throws XMLDocumentException Error montando el fichero XML
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 * @deprecated Usar {@link #signAttached(DeviceManager,String,IDocument,TSAData,CAList,XAdESAttachedSignatureOptions) signAttached}
	 */
	public static XAdESXLSignature signAttached (DeviceManager manager, String alias, IDocument document, String idToSign,
			String signatureParent, URL urlTSA, CAList caList) throws XMLDocumentException, LoadingObjectException, SignatureException  {
		return signAttached(manager, alias, document, null, idToSign, signatureParent, urlTSA, caList, null, null);
	}
	
	/**
	 * Realiza una firma XAdES-XL atached (el documento se incluye en la firma). No completa los campos no 
	 * obligatorios del tag 'SignedSignatureProperties':'signaturePolicyIdentifier' y 'signatureProductionPlace'. 
	 * 
	 * Si el documento es un XML y los parámetros <code>idToSign</code> y <code>signatureParent</code>
	 * no son nulos la firma y los campos propios de XAdES se añadirán al XML. En caso contrario el fichero 
	 * XAdES resultante seguirá la plantilla de Arangí, por ejemplo:<br>
	 * <code>
	 * 	&lt;arangi-xades&gt;<br>
	 *  &nbsp;&nbsp;&lt;document&gt;...&lt;/document&gt;
	 *  &nbsp;&nbsp;&lt;ds:Signature&gt;...&lt;/ds:Signature&gt;
	 * 	&lt;/arangi-xades&gt;<br>
	 * </code>
	 * 
	 * @param manager Dispositivo criptográfico que realizará la firma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param document Documento a firmar
	 * @param digitalSignatureAlgorithm Algoritmo de firma (si nulo algoritmo por defecto)
	 * @param idToSign Valor del atributo 'ID' que indica lo que se firmará dentro del documento. Si tiene 
	 *  valor nulo el XML de la firma tendrá el formato por defecto de las firmas XAdES de Arangí.
	 * @param signatureParent Nombre del tag que será el padre de los nodos de firma. Si tiene valor nulo
	 * 	la firma colgará del nodo raíz.
	 * @param urlTSA URL del servidor de sello de tiempos
	 * @param caList Lista de CAs para obtener información de validación
	 * @param dof Información para construir el tag DataObjectFormat (puede ser null)
	 * @param claimedRoles Roles de la firma (puede ser null)
	 * @return Firma XADES-XL
	 * @throws XMLDocumentException Error montando el fichero XML
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 * @deprecated Usar {@link #signAttached(DeviceManager,String,IDocument,TSAData,CAList,XAdESAttachedSignatureOptions) signAttached}
	 */
	public static XAdESXLSignature signAttached (DeviceManager manager, String alias, IDocument document, 
			String digitalSignatureAlgorithm, String idToSign, String signatureParent, URL urlTSA, 
			CAList caList, XAdESDataObjectFormat dof, String[] claimedRoles) 
					throws XMLDocumentException, LoadingObjectException, SignatureException  {
		
		TSAData tsaData = new TSAData(urlTSA);
		XAdESAttachedNodeToSignObject nodeToSign = null;
		if (idToSign != null) {
			nodeToSign = new XAdESAttachedNodeToSignObject(idToSign);
		}
		XAdESAttachedSignatureOptions options = new XAdESAttachedSignatureOptions(digitalSignatureAlgorithm, null, dof, claimedRoles, null, null, nodeToSign);
		return signAttached(manager, alias, document, tsaData, caList, options);
	}
	
	/**
	 * Realiza una firma XAdES-XL atached (el documento se incluye en la firma). No completa los campos no 
	 * obligatorios del tag 'SignedSignatureProperties':'signaturePolicyIdentifier', 'signatureProductionPlace' 
	 * y 'signerRole'.<br><br>
	 * 
	 * El fichero XAdES seguirá la plantilla de Arangí. Ejemplo:<br>
	 * <code>
	 * 	&lt;arangi-xades&gt;<br>
	 *  &nbsp;&nbsp;&lt;document&gt;...&lt;/document&gt;
	 *  &nbsp;&nbsp;&lt;ds:Signature&gt;...&lt;/ds:Signature&gt;
	 * 	&lt;/arangi-xades&gt;<br>
	 * </code>
	 * 
	 * @param manager Dispositivo criptográfico que realizará la firma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param document Documento a firmar
	 * @param urlTSA URL del servidor de sello de tiempos
	 * @param caList Lista de CAs para obtener información de validación
	 * @return Firma XADES-XL
	 * @throws XMLDocumentException Error montando el fichero XML
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 * @deprecated Usar {@link #signAttached(DeviceManager,String,IDocument,TSAData,CAList,XAdESAttachedSignatureOptions) signAttached}
	 */
	public static XAdESXLSignature signAttached (DeviceManager manager, String alias, IDocument document, 
			URL urlTSA, CAList caList) throws XMLDocumentException, LoadingObjectException, SignatureException  {
		return signAttached(manager, alias, document, null, urlTSA, caList, null, null);
	}
	
	/**
	 * Realiza una firma XAdES-XL atached (el documento se incluye en la firma). No completa los campos no 
	 * obligatorios del tag 'SignedSignatureProperties':'signaturePolicyIdentifier' y 'signatureProductionPlace' 
	 * .<br><br>
	 * 
	 * El fichero XAdES seguirá la plantilla de Arangí. Ejemplo:<br>
	 * <code>
	 * 	&lt;arangi-xades&gt;<br>
	 *  &nbsp;&nbsp;&lt;document&gt;...&lt;/document&gt;
	 *  &nbsp;&nbsp;&lt;ds:Signature&gt;...&lt;/ds:Signature&gt;
	 * 	&lt;/arangi-xades&gt;<br>
	 * </code>
	 * 
	 * @param manager Dispositivo criptográfico que realizará la firma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param document Documento a firmar
	 * @param digitalSignatureAlgorithm Algoritmo de firma (si nulo algoritmo por defecto)
	 * @param urlTSA URL del servidor de sello de tiempos
	 * @param caList Lista de CAs para obtener información de validación
	 * @param dof Información para construir el tag DataObjectFormat (puede ser null)
	 * @param claimedRoles Roles de la firma (puede ser null)
	 * @return Firma XADES-XL
	 * @throws XMLDocumentException Error montando el fichero XML
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 * @deprecated Usar {@link #signAttached(DeviceManager,String,IDocument,TSAData,CAList,XAdESAttachedSignatureOptions) signAttached}
	 */
	public static XAdESXLSignature signAttached (DeviceManager manager, String alias, IDocument document, 
			String digitalSignatureAlgorithm, URL urlTSA, CAList caList, XAdESDataObjectFormat dof, 
			String[] claimedRoles) throws XMLDocumentException, LoadingObjectException, SignatureException  {
		
		logger.debug("[XAdESXLSignature.signAttached]::Entrada::" + Arrays.asList(new Object[] { manager, alias, document, digitalSignatureAlgorithm, urlTSA, caList, dof, claimedRoles }));
	
		TSAData tsaData = new TSAData(urlTSA);
		XAdESAttachedSignatureOptions options = new XAdESAttachedSignatureOptions(digitalSignatureAlgorithm, null, dof, claimedRoles, null, null, null);
		return signAttached(manager, alias, document, tsaData, caList, options);
	}
	
	/**
	 * Realiza una firma XAdES-XL atached (el documento se incluye en la firma). No completa los campos no 
	 * obligatorios del tag 'SignedSignatureProperties':'signaturePolicyIdentifier', 'signatureProductionPlace' 
	 * y 'signerRole'.<br><br>
	 * 
	 * El fichero XAdES seguirá la plantilla de Arangí. Ejemplo:<br>
	 * <code>
	 * 	&lt;arangi-xades&gt;<br>
	 *  &nbsp;&nbsp;&lt;document&gt;...&lt;/document&gt;
	 *  &nbsp;&nbsp;&lt;ds:Signature&gt;...&lt;/ds:Signature&gt;
	 * 	&lt;/arangi-xades&gt;<br>
	 * </code>
	 * 
	 * @param manager Dispositivo criptográfico que realizará la firma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param document Fichero a firmar
	 * @param urlTSA URL del servidor de sello de tiempos
	 * @param caList Lista de CAs para obtener información de validación
	 * @return Firma XADES-XL
	 * @throws XMLDocumentException Error montando el fichero XML
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 * @deprecated Usar {@link #signAttached(DeviceManager,String,IDocument,TSAData,CAList,XAdESAttachedSignatureOptions) signAttached}
	 */
	public static XAdESXLSignature signAttached (DeviceManager manager, String alias, File document, URL urlTSA, CAList caList) throws LoadingObjectException, SignatureException, NoDocumentToSignException, XMLDocumentException {
		return signAttached(manager, alias, document, null, urlTSA, caList, null, null);
	}
	
	/**
	 * Realiza una firma XAdES-XL atached (el documento se incluye en la firma). No completa los campos no 
	 * obligatorios del tag 'SignedSignatureProperties':'signaturePolicyIdentifier' y 'signatureProductionPlace' 
	 * .<br><br>
	 * 
	 * El fichero XAdES seguirá la plantilla de Arangí. Ejemplo:<br>
	 * <code>
	 * 	&lt;arangi-xades&gt;<br>
	 *  &nbsp;&nbsp;&lt;document&gt;...&lt;/document&gt;
	 *  &nbsp;&nbsp;&lt;ds:Signature&gt;...&lt;/ds:Signature&gt;
	 * 	&lt;/arangi-xades&gt;<br>
	 * </code>
	 * 
	 * @param manager Dispositivo criptográfico que realizará la firma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param document Fichero a firmar
	 * @param digitalSignatureAlgorithm Algoritmo de firma (si nulo algoritmo por defecto)
	 * @param urlTSA URL del servidor de sello de tiempos
	 * @param caList Lista de CAs para obtener información de validación
	 * @param dof Información para construir el tag DataObjectFormat (puede ser null)
	 * @param claimedRoles Roles de la firma (puede ser null)
	 * @return Firma XADES-XL
	 * @throws XMLDocumentException Error montando el fichero XML
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 * @deprecated Usar {@link #signAttached(DeviceManager,String,IDocument,TSAData,CAList,XAdESAttachedSignatureOptions) signAttached}
	 */
	public static XAdESXLSignature signAttached (DeviceManager manager, String alias, File document, 
			String digitalSignatureAlgorithm, URL urlTSA, CAList caList, XAdESDataObjectFormat dof, 
			String[] claimedRoles) throws LoadingObjectException, SignatureException, NoDocumentToSignException, XMLDocumentException {
		
		logger.debug("[XAdESXLSignature.signAttached]::Entrada::" + Arrays.asList(new Object[] { manager, alias, document, digitalSignatureAlgorithm,  urlTSA, caList, dof, claimedRoles }));
		
		//-- Obtener el document de Arangi y llamar a la firma attached
		FileDocument fileDocument;
		try {
			fileDocument = new FileDocument(document);
		} catch (InitDocumentException e) {
			logger.info("[XAdESXLSignature.signAttached]::El fichero a firmar no existe o es nulo:: " + document);
			throw new NoDocumentToSignException("El fichero a firmar no existe o es nulo: " + document);
		}
		
		return signAttached(manager, alias, fileDocument, digitalSignatureAlgorithm, urlTSA, caList, dof, claimedRoles);
		
	}
	
	/**
	 * Realiza una firma XAdES-XL atached (el documento se incluye en la firma). No completa los campos no 
	 * obligatorios del tag 'SignedSignatureProperties':'signaturePolicyIdentifier', 'signatureProductionPlace' 
	 * y 'signerRole'.<br><br>
	 * 
	 * El fichero XAdES seguirá la plantilla de Arangí. Ejemplo:<br>
	 * <code>
	 * 	&lt;arangi-xades&gt;<br>
	 *  &nbsp;&nbsp;&lt;document&gt;...&lt;/document&gt;
	 *  &nbsp;&nbsp;&lt;ds:Signature&gt;...&lt;/ds:Signature&gt;
	 * 	&lt;/arangi-xades&gt;<br>
	 * </code>
	 * 
	 * @param manager Dispositivo criptográfico que realizará la firma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param document Fichero a firmar (se encuentra en una URL accesible)
	 * @param urlTSA URL del servidor de sello de tiempos
	 * @param caList Lista de CAs para obtener información de validación
	 * @return Firma XADES-XL
	 * @throws XMLDocumentException Error montando el fichero XML
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 * @deprecated Usar {@link #signAttached(DeviceManager,String,IDocument,TSAData,CAList,XAdESAttachedSignatureOptions) signAttached}
	 */
	public static XAdESXLSignature signAttached (DeviceManager manager, String alias, URL document, URL urlTSA, CAList caList) throws LoadingObjectException, SignatureException, NoDocumentToSignException, XMLDocumentException {
		return signAttached(manager, alias, document, null, urlTSA, caList, null, null);
	}
	
	/**
	 * Realiza una firma XAdES-XL atached (el documento se incluye en la firma). No completa los campos no 
	 * obligatorios del tag 'SignedSignatureProperties':'signaturePolicyIdentifier' y 'signatureProductionPlace' 
	 * .<br><br>
	 * 
	 * El fichero XAdES seguirá la plantilla de Arangí. Ejemplo:<br>
	 * <code>
	 * 	&lt;arangi-xades&gt;<br>
	 *  &nbsp;&nbsp;&lt;document&gt;...&lt;/document&gt;
	 *  &nbsp;&nbsp;&lt;ds:Signature&gt;...&lt;/ds:Signature&gt;
	 * 	&lt;/arangi-xades&gt;<br>
	 * </code>
	 * 
	 * @param manager Dispositivo criptográfico que realizará la firma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param document Fichero a firmar (se encuentra en una URL accesible)
	 * @param digitalSignatureAlgorithm Algoritmo de firma (si nulo algoritmo por defecto)
	 * @param urlTSA URL del servidor de sello de tiempos
	 * @param caList Lista de CAs para obtener información de validación
	 * @param dof Información para construir el tag DataObjectFormat (puede ser null)
	 * @param claimedRoles Roles de la firma (puede ser null)
	 * @return Firma XADES-XL
	 * @throws XMLDocumentException Error montando el fichero XML
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 * @deprecated Usar {@link #signAttached(DeviceManager,String,IDocument,TSAData,CAList,XAdESAttachedSignatureOptions) signAttached}
	 */
	public static XAdESXLSignature signAttached (DeviceManager manager, String alias, URL document, 
			String digitalSignatureAlgorithm, URL urlTSA, CAList caList, XAdESDataObjectFormat dof, 
			String[] claimedRoles) throws LoadingObjectException, SignatureException, NoDocumentToSignException, XMLDocumentException {
		
		logger.debug("[XAdESTSignature.signAttached]::Entrada::" + Arrays.asList(new Object[] { manager, alias, document, digitalSignatureAlgorithm, urlTSA, caList, dof, claimedRoles }));
		
		//-- Obtener el document de Arangi y llamar a la firma attached
		URLDocument urlDocument;
		try {
			urlDocument = new URLDocument(document);
		} catch (InitDocumentException e) {
			logger.info("[XAdESTSignature.signAttached]::La URL a firmar no existe o es nula:: " + document);
			throw new NoDocumentToSignException("La URL a firmar no existe o es nula: " + document);
		}
		
		return signAttached(manager, alias, urlDocument, digitalSignatureAlgorithm, urlTSA, caList, dof, claimedRoles);
		
	}
	
	/**
	 * Realiza una firma XAdES-XL atached (el documento se incluye en la firma). Si no
	 * se puede obtener la información de validación mediante OCSP se producirá una
	 * excepción.
	 * 
	 * @param manager Dispositivo criptográfico que realizará la firma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param document Documento a firmar
	 * @param tsaData Información de conexión a la TSA
	 * @param caList Lista de CAs para obtener información de validación
	 * @param options Opciones de la firma
	 * @return Firma XADES-XL
	 * @throws XMLDocumentException Error montando el fichero XML
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 */
	public static XAdESXLSignature signAttached (DeviceManager manager, String alias, IDocument document, 
			TSAData tsaData, CAList caList, XAdESAttachedSignatureOptions options) throws XMLDocumentException, LoadingObjectException, SignatureException  {
		
		return signAttached(manager, alias, document, tsaData, caList, options, false);
	}
	
	/**
	 * Realiza una firma XAdES-XL atached (el documento se incluye en la firma).  <br><br>
	 * 
	 * Utilizar este método si se desea permitir que se obtenga la información de validación mediante 
	 * CRL (en caso de que no se pueda mediante OCSP). Cuidado con esta opción ya que las CRLs pueden 
	 * tener un tamaño considerable, por lo que la obtención de la firma será más lenta y la misma firma
	 * puede acabar con un tamaño muy grande.
	 * 
	 * @param manager Dispositivo criptográfico que realizará la firma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param document Documento a firmar
	 * @param tsaData Información de conexión a la TSA
	 * @param caList Lista de CAs para obtener información de validación
	 * @param options Opciones de la firma
	 * @param allowCRLValidation Permitir generar la firma con CRLs si no se puede
	 *  realizar la validación mediante OCSP
	 * @return Firma XADES-XL
	 * @throws XMLDocumentException Error montando el fichero XML
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 */
	public static XAdESXLSignature signAttached (DeviceManager manager, String alias, IDocument document, 
			TSAData tsaData, CAList caList, XAdESAttachedSignatureOptions options, boolean allowCRLValidation) 
					throws XMLDocumentException, LoadingObjectException, SignatureException  {
		
		logger.debug("[XAdESXLSignature.signAttached]::Entrada::" + Arrays.asList(new Object[] { manager, alias, document, tsaData, caList, options, allowCRLValidation }));
	
		return (XAdESXLSignature) signAttached(manager, alias, document, tsaData, new CAListCertStatusRecover(caList, allowCRLValidation), options, EnumFormatoFirma.XAdES_XL, XAdESXLSignature.class);
	}
	
	/**
	 * La definición de las contrafirmas en XAdES puede observarse en el punto 7.2.4
	 * del estándar de la ETSI.<br><br>
	 * 
	 * Este método realiza una contrafirma para la última firma del XAdES. Es útil 
	 * cuando se sabe que el XAdES contiene sólo una firma.<br><br>
	 * 
	 * Como resultado el XAdES a la que hace referencia este objeto se modificará 
	 * para añadir la contrafirma.
	 * 
	 * @param manager Dispositivo criptográfico que realizará la contrafirma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param urlTSA URL del servidor de sello de tiempos para incluir en la contrafirma
	 * @param caList Lista de CAs para obtener información de validación
	 * @throws LoadingObjectException No es posible obtener la clave privada o el
	 * 	certificado del alias
	 * @throws CounterSignatureException Errores durante el proceso de contrafirma
	 */
	public void counterSign (DeviceManager manager, String alias, URL urlTSA, CAList caList) throws LoadingObjectException, CounterSignatureException {
		counterSign(manager, alias, null, null, urlTSA, caList);
	}
	
	/**
	 * La definición de las contrafirmas en XAdES puede observarse en el punto 7.2.4
	 * del estándar de la ETSI.<br><br>
	 * 
	 * Este método realiza una contrafirma para la firma cuyo certificado se pasa
	 * en el parámetro 'signatureToCounterSignCertificate'. Es útil cuando se quiere
	 * contrafirmar un XAdES que contiene varias firmas. Para saber qué firma se
	 * desea contrafirmar se puede llamar primero a 
	 * {@link #getCertificates() getCertificates} para ver los certificados de cada
	 * una de las firmas que contiene el XAdES.<br><br>
	 * 
	 * Como resultado el XAdES a la que hace referencia este objeto se modificará 
	 * para añadir la contrafirma.
	 * 
	 * @param manager Dispositivo criptográfico que realizará la contrafirma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param signatureToCounterSignCertificate Certificado de la firma que se 
	 * 	contrafirmará
	 * @param digitalSignatureAlgorithm Algoritmo de firma (si nulo algoritmo por defecto)
	 * @param urlTSA URL del servidor de sello de tiempos para incluir en la contrafirma
	 * @param caList Lista de CAs para obtener información de validación
	 * @throws LoadingObjectException No es posible obtener la clave privada o el
	 * 	certificado del alias
	 * @throws CounterSignatureException Errores durante el proceso de contrafirma
	 */
	public void counterSign (DeviceManager manager, String alias, Certificate signatureToCounterSignCertificate,
			String digitalSignatureAlgorithm, URL urlTSA, CAList caList) throws LoadingObjectException, CounterSignatureException {
		counterSign(manager, alias, signatureToCounterSignCertificate, digitalSignatureAlgorithm, urlTSA, caList, false);
	}
	
	/**
	 * La definición de las contrafirmas en XAdES puede observarse en el punto 7.2.4
	 * del estándar de la ETSI.<br><br>
	 * 
	 * Este método realiza una contrafirma para la firma cuyo certificado se pasa
	 * en el parámetro 'signatureToCounterSignCertificate'. Es útil cuando se quiere
	 * contrafirmar un XAdES que contiene varias firmas. Para saber qué firma se
	 * desea contrafirmar se puede llamar primero a 
	 * {@link #getCertificates() getCertificates} para ver los certificados de cada
	 * una de las firmas que contiene el XAdES.<br><br>
	 * 
	 * Como resultado el XAdES a la que hace referencia este objeto se modificará 
	 * para añadir la contrafirma.
	 * 
	 * @param manager Dispositivo criptográfico que realizará la contrafirma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param signatureToCounterSignCertificate Certificado de la firma que se 
	 * 	contrafirmará
	 * @param digitalSignatureAlgorithm Algoritmo de firma (si nulo algoritmo por defecto)
	 * @param urlTSA URL del servidor de sello de tiempos para incluir en la contrafirma
	 * @param caList Lista de CAs para obtener información de validación
	 * @param allowCRLValidation Permitir generar la firma con CRLs si no se puede
	 *  realizar la validación mediante OCSP
	 * @throws LoadingObjectException No es posible obtener la clave privada o el
	 * 	certificado del alias
	 * @throws CounterSignatureException Errores durante el proceso de contrafirma
	 */
	public void counterSign (DeviceManager manager, String alias, Certificate signatureToCounterSignCertificate,
			String digitalSignatureAlgorithm, URL urlTSA, CAList caList, boolean allowCRLValidation) throws LoadingObjectException, CounterSignatureException {
		
		logger.debug("[XAdESXLSignature.counterSign]::Entrada::" + Arrays.asList(new Object[] { manager, alias, signatureToCounterSignCertificate, digitalSignatureAlgorithm, urlTSA, caList, allowCRLValidation }));
		
		//-- El documento a firmar es la firma en la que nos encontramos
        DataToSign dataToSign = new DataToSign();
        dataToSign.setXadesFormat(getXAdESSignatureFormat());
        dataToSign.setEsquema(DEFAULT_XADES_SCHEMA);
        dataToSign.setXMLEncoding(DEFAULT_XML_ENCODING);
        dataToSign.setEnveloped(true);
        dataToSign.setDocument(this.xadesDocument);
       	dataToSign.setCertStatusManager (new CAListCertStatusRecover(caList, allowCRLValidation));
        
		Document doc;
		try {
			doc = ContraFirmaXML.counterSign(manager.getCertificate(alias), dataToSign, 
					signatureToCounterSignCertificate!=null?signatureToCounterSignCertificate.toX509Certificate():null, 
					manager.getPrivateKey(alias), digitalSignatureAlgorithm, urlTSA, DEFAULT_XADES_SCHEMA_URI);
		} catch (LoadingObjectException e) {
			logger.info("[XAdESXLSignature.counterSign]::No es posible obtener la clave privada del alias '" + alias + "'", e);
			throw e;
		} catch (SearchingException e) {
			logger.info("[XAdESXLSignature.counterSign]::No es posible obtener el certificado del alias '" + alias + "'", e);
			throw new LoadingObjectException ("No es posible obtener el certificado del alias '" + alias + "'", e);
		} catch (CounterSignatureException e) {
			logger.info("[XAdESXLSignature.counterSign]::No es posible realizar la contrafirma", e);
			throw e;
		}
		
		logger.debug("[XAdESXLSignature.counterSign]::Se ha obtenido la contrafirma");
		this.xadesDocument = doc;
	}
	
	/**
	 * Añade una Cofirma a la firma XAdES-XL. Realizará una firma de las mismas características que 
	 * la primera que encuentre (attached o dettached).<br><br>
	 * 
	 * Si la firma es dettached i la referencia al documento que hay en la firma no
	 * es una URL será necesario usar el método {@link #coSign(DeviceManager, String, IDocument, URL, CAList)}
	 * al que le proporcionaremos este documento.  
	 * 
	 * @param manager Dispositivo criptográfico que realizará la cofirma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param urlTSA URL del servidor de sello de tiempos para incluir en la contrafirma
	 * @param caList Lista de CAs para obtener información de validación
	 * @throws SignatureNotFoundException No existe ninguna firma que cofirmar
	 * @throws NoDocumentToSignException El fichero a firmar no existe o es nulo
	 * @throws HashingException Error realizando el hash del documento
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No ha sido posible parsear la firma XAdES o no se puede realizar la cofirma
	 * @throws NoCoincidentDocumentException El documento que se quiere firmar no se corresponde con el de
	 * 	la firma XAdES  
	 */
	public void coSign (DeviceManager manager, String alias, URL urlTSA, CAList caList) throws SignatureNotFoundException, 
		NoDocumentToSignException, HashingException, LoadingObjectException, SignatureException, NoCoincidentDocumentException {
		
		logger.debug("[XAdESXLSignature.coSign]::Entrada::" + Arrays.asList(new Object[] { manager, alias }));
		coSign (manager, alias, null, null, urlTSA, caList);
	}
	
	
	/**
	 * Añade una Cofirma a la firma XAdES-XL. Realizará una firma de las mismas características que 
	 * la primera que encuentre (attached o dettached).<br><br>
	 * 
	 * Este método es útil si la firma es dettached i la referencia al documento que hay en la firma no
	 * es una URL.  
	 * 
	 * @param manager Dispositivo criptográfico que realizará la cofirma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param signedDoc contenido a firmar. El mismo utilizado en la generación de las otras firmas
	 * @param digitalSignatureAlgorithm Algoritmo de firma (si nulo algoritmo por defecto)
	 * @param urlTSA URL del servidor de sello de tiempos para incluir en la contrafirma
	 * @param caList Lista de CAs para obtener información de validación
	 * @throws SignatureNotFoundException No existe ninguna firma que cofirmar
	 * @throws NoDocumentToSignException El fichero a firmar no existe o es nulo
	 * @throws HashingException Error realizando el hash del documento
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No ha sido posible parsear la firma XAdES o no se puede realizar la cofirma
	 * @throws NoCoincidentDocumentException El documento que se quiere firmar no se corresponde con el de
	 * 	la firma XAdES  
	 */
	public void coSign (DeviceManager manager, String alias, IDocument signedDoc, 
			String digitalSignatureAlgorithm, URL urlTSA, CAList caList) 
					throws SignatureNotFoundException, NoDocumentToSignException, HashingException, 
					LoadingObjectException, SignatureException, NoCoincidentDocumentException {
		coSign(manager, alias, signedDoc, digitalSignatureAlgorithm, urlTSA, caList, false);
	}
	
	/**
	 * Añade una Cofirma a la firma XAdES-XL. Realizará una firma de las mismas características que 
	 * la primera que encuentre (attached o dettached).<br><br>
	 * 
	 * Este método es útil si la firma es dettached i la referencia al documento que hay en la firma no
	 * es una URL.  
	 * 
	 * @param manager Dispositivo criptográfico que realizará la cofirma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param signedDoc contenido a firmar. El mismo utilizado en la generación de las otras firmas
	 * @param digitalSignatureAlgorithm Algoritmo de firma (si nulo algoritmo por defecto)
	 * @param urlTSA URL del servidor de sello de tiempos para incluir en la contrafirma
	 * @param caList Lista de CAs para obtener información de validación
	 * @param allowCRLValidation Permitir generar la firma con CRLs si no se puede
	 *  realizar la validación mediante OCSP
	 * @throws SignatureNotFoundException No existe ninguna firma que cofirmar
	 * @throws NoDocumentToSignException El fichero a firmar no existe o es nulo
	 * @throws HashingException Error realizando el hash del documento
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No ha sido posible parsear la firma XAdES o no se puede realizar la cofirma
	 * @throws NoCoincidentDocumentException El documento que se quiere firmar no se corresponde con el de
	 * 	la firma XAdES  
	 */
	public void coSign (DeviceManager manager, String alias, IDocument signedDoc, 
			String digitalSignatureAlgorithm, URL urlTSA, CAList caList, boolean allowCRLValidation) 
					throws SignatureNotFoundException, NoDocumentToSignException, HashingException, 
					LoadingObjectException, SignatureException, NoCoincidentDocumentException {
		
		coSign(manager, alias, signedDoc, digitalSignatureAlgorithm, urlTSA, new CAListCertStatusRecover(caList, allowCRLValidation), EnumFormatoFirma.XAdES_XL, XAdESXLSignature.class);
		
	}
	
	/**
	 * Devuelve una cadena de texto con el tipo de la firma
	 * 
	 * @return Cadena de texto con el tipo de la firma
	 */
	public String getSignatureType () {
		return SIGNATURE_TYPE;
	}
	
	/**
	 * Añade lo que falta para completar el XAdES a XAdES-X-L. La firma tiene que ser attached. 
	 * Si no es así es necesario
	 * usar {@link #completeToXAdESXL (XAdESSignature, IDocument, CAList, URL)}.
	 * 
	 * @param xades Firma XAdES
	 * @param caList lista de certificados de CA
	 * @param urlTSA URL del servidor de sello de tiempos
	 * @return Firma XAdES-X-L
	 * @throws NormalizeCertificateException Alguno de los certificados no puede ser 
	 * 	normalizado al formato reconocido por el proveedor criptográfico de Arangí o su 
	 * 	firma no es correcta o no puede ser analizada
	 * @throws SignatureException Error tratando el objeto firma o la firma XAdES-T no es
	 * 	válida
	 * @throws RetrieveOCSPException Error obteniendo las respuestas OCSP
	 * @throws XMLDocumentException Error completando el XML del XAdES-T a XAdES-X-L
	 */
	public static XAdESXLSignature completeToXAdESXL (XAdESSignature xades, CAList caList, URL urlTSA) throws SignatureException, NormalizeCertificateException, RetrieveOCSPException, XMLDocumentException {
		return completeToXAdESXL (xades, null, caList, urlTSA);
	}
	
	/**
	 * Añade lo que falta para completar el XAdES a XAdES-X-L. Si no
	 * se puede obtener la información de validación mediante OCSP se producirá una
	 * excepción.
	 * 
	 * @param xades Firma XAdES
	 * @param document documento firmado en el XAdES. Útil en el caso que el XAdES sea detached.
	 * @param caList lista de certificados de CA
	 * @param urlTSA URL del servidor de sello de tiempos
	 * @return Firma XAdES-X-L
	 * @throws NormalizeCertificateException Alguno de los certificados no puede ser 
	 * 	normalizado al formato reconocido por el proveedor criptográfico de Arangí o su 
	 * 	firma no es correcta o no puede ser analizada
	 * @throws SignatureException Error tratando el objeto firma o la firma XAdES-T no es
	 * 	válida
	 * @throws RetrieveOCSPException Error obteniendo las respuestas OCSP
	 * @throws XMLDocumentException Error completando el XML del XAdES-T a XAdES-X-L
	 */
	public static XAdESXLSignature completeToXAdESXL (XAdESSignature xades, IDocument document, CAList caList, URL urlTSA) throws SignatureException, NormalizeCertificateException, RetrieveOCSPException, XMLDocumentException {
		return completeToXAdESXL(xades, document, caList, urlTSA, false);
	}
	
	/**
	 * Añade lo que falta para completar el XAdES a XAdES-X-L.  <br><br>
	 * 
	 * Utilizar este método si se desea permitir que se obtenga la información de validación mediante 
	 * CRL (en caso de que no se pueda mediante OCSP). Cuidado con esta opción ya que las CRLs pueden 
	 * tener un tamaño considerable, por lo que la obtención de la firma será más lenta y la misma firma
	 * puede acabar con un tamaño muy grande.
	 * 
	 * @param xades Firma XAdES
	 * @param document documento firmado en el XAdES. Útil en el caso que el XAdES sea detached.
	 * @param caList lista de certificados de CA
	 * @param urlTSA URL del servidor de sello de tiempos
	 * @param allowCRLValidation Permitir generar la firma con CRLs si no se puede
	 *  realizar la validación mediante OCSP
	 * @return Firma XAdES-X-L
	 * @throws NormalizeCertificateException Alguno de los certificados no puede ser 
	 * 	normalizado al formato reconocido por el proveedor criptográfico de Arangí o su 
	 * 	firma no es correcta o no puede ser analizada
	 * @throws SignatureException Error tratando el objeto firma o la firma XAdES-T no es
	 * 	válida
	 * @throws RetrieveOCSPException Error obteniendo las respuestas OCSP
	 * @throws XMLDocumentException Error completando el XML del XAdES-T a XAdES-X-L
	 */
	public static XAdESXLSignature completeToXAdESXL (XAdESSignature xades, IDocument document, CAList caList, 
			URL urlTSA, boolean allowCRLValidation) throws SignatureException, NormalizeCertificateException, RetrieveOCSPException, XMLDocumentException {
		logger.debug("[XAdESXLSignature.completeToXAdESXL]::Entrada::" + Arrays.asList(new Object[] { xades, urlTSA, allowCRLValidation }));
		
		Document doc = xades.getDOM();
		
		//-- Comprobar que el XAdES-T es correcto
		logger.debug("[XAdESXLSignature.completeToXAdESXL]::Verificando que el XAdES-T es correcto");
		ValidationResult[] resultadoValidacion = null;
		if (document == null) {
			resultadoValidacion = xades.isValid(caList);			
		} else {
			resultadoValidacion = xades.isValid(document, caList);
		}
		
		String[] tiposFirmas = new String[resultadoValidacion.length]; 
		for (int i = 0; i < resultadoValidacion.length; i++) {
			if (!resultadoValidacion[i].isValid()) {
				logger.info("[XAdESXLSignature.completeToXAdESXL]::El XAdES-T no es válido: " + resultadoValidacion[i].getResultText());
				throw new SignatureException("El XAdES-T no es válido: " + resultadoValidacion[i].getResultText());
			}
			
			//-- Obtener el tipo de firma
			if (resultadoValidacion[i].getOcspResponses() != null && resultadoValidacion[i].getOcspResponses().length > 0) {
				tiposFirmas[i] = XAdESXLSignature.SIGNATURE_TYPE;
			} else {
				if (resultadoValidacion[i].getTimeStamp() != null) {
					tiposFirmas[i] = XAdESTSignature.SIGNATURE_TYPE;
				} else {
					tiposFirmas[i] = XAdESBESSignature.SIGNATURE_TYPE;
				}
			}

		}
		
		NodeList listaFirmas = doc.getElementsByTagNameNS(ConstantesXADES.SCHEMA_DSIG, ConstantesXADES.LIBRERIAXADES_SIGNATURE);
		for(int i=0;i<listaFirmas.getLength();i++) {
			Element nodeFirma = (Element) listaFirmas.item(i);
			
			//-- Comprobar el tipo de firma
			if (tiposFirmas[i].equals(XAdESXLSignature.SIGNATURE_TYPE)) {
				logger.debug("[XAdESXLSignature.completeToXAdESXL]::La firma ya es XL, no es necesario completar");
				continue;
			}
			if (tiposFirmas[i].equals(XAdESBESSignature.SIGNATURE_TYPE)) {
				logger.debug("[XAdESXLSignature.completeToXAdESXL]::La firma es BES, primero completar a T");
				Node idAttribute = nodeFirma.getAttributes().getNamedItem(ConstantesXADES.ID);

				//-- Obtener el sello de tiempos
				TimeStamp timeStamp;
				try {
					byte[] byteSignature = UtilidadTratarNodo.obtenerByteNodo(nodeFirma, ConstantesXADES.SCHEMA_DSIG, ConstantesXADES.SIGNATURE_VALUE, CanonicalizationEnum.C14N_OMIT_COMMENTS, 5);
					timeStamp = TimeStamp.stampDocument(byteSignature, urlTSA);
				} catch (FirmaXMLError e) {
					logger.info ("[XAdESXLSignature.completeToXAdESXL]::Error obteniendo el elemento a sellar", e);
					throw new SignatureException("Error obteniendo el elemento a sellar", e);
				} catch (TimeStampServerConnectionException e) {
					logger.info("[XAdESXLSignature.completeToXAdESXL]::No ha sido posible conectarse al servidor de sello de tiempos", e);
					throw new SignatureException ("No ha sido posible conectarse al servidor de sello de tiempos", e);
				} catch (HashingException e) {
					logger.info("[XAdESXLSignature.completeToXAdESXL]::No ha sido posible obtener el hash para obtener el sello de tiempos", e);
					throw new SignatureException ("No ha sido posible obtener el hash para obtener el sello de tiempos", e);
				} catch (MalformedTimeStampException e) {
					logger.info("[XAdESXLSignature.completeToXAdESXL]::El sello de tiempos está mal formado", e);
					throw new SignatureException ("El sello de tiempos está mal formado", e);
				} catch (ResponseTimeStampException e) {
					logger.info("[XAdESXLSignature.completeToXAdESXL]::Error en la respuesta del servidor de sello de tiempos", e);
					throw new SignatureException ("Error en la respuesta del servidor de sello de tiempos", e);
				} 
				
				try {
					doc = addXadesT(nodeFirma, idAttribute.getNodeValue(), timeStamp.toDER(), searchXAdESNamespace (doc.getDocumentElement()));
					logger.debug("[XAdESXLSignature.completeToXAdESXL]::Completado a XAdES-T");
				} catch (Exception e) {
					throw new XMLDocumentException(e);
				}

			}
			
			// Se obtiene el certificado firmante
	    	Element certificateNode = (Element) nodeFirma.getElementsByTagNameNS(ConstantesXADES.SCHEMA_DSIG, "X509Certificate").item(0);
	    	byte[] certificateContent = Util.decodeBase64(certificateNode.getFirstChild().getNodeValue());
	    	X509Certificate signatureCertificate = (X509Certificate) Util.getCertificate(certificateContent);

			// Se obtiene el certificado firmante del sello de tiempos
	    	X509Certificate timeStampCertificate;
	    	try {
	    		timeStampCertificate = getXAdESTimeStampCertificate(doc).toX509Certificate();
			} catch (MalformedTimeStampException e1) {
				logger.info("[XAdESXLSignature.completeToXAdESXL]::No se puede obtener el sello de tiempos del xades", e1);
				throw new SignatureException ("No se puede obtener el sello de tiempos del xades", e1);
			}
	    	
	    	//-- Lista de certificados y respuestas OCSP: cadena de los certificados de firma y cadena de los certificados de tsa 
			ArrayList<RespYCerts> respuestasFirma = new ArrayList<RespYCerts>();
			try {
				logger.debug("[XAdESXLSignature.completeToXAdESXL]::Obteniendo respuestas OCSP para el certificado de firma");
				respuestasFirma = convertICertStatus2RespYCerts(new CAListCertStatusRecover(caList,allowCRLValidation).getCertChainStatus(signatureCertificate));
			} catch (CertStatusException e) {
				logger.info("[XAdESXLSignature.completeToXAdESXL]::Error obteniendo respuestas OCSP para el certificado de firma", e);
				throw new RetrieveOCSPException("Error obteniendo respuestas OCSP para el certificado de firma", e);
			}
			ArrayList<RespYCerts> respuestasSello = new ArrayList<RespYCerts>();
			try {
				logger.debug("[XAdESXLSignature.completeToXAdESXL]::Obteniendo respuestas OCSP para el certificado de sello");
				respuestasSello = convertICertStatus2RespYCerts(new CAListCertStatusRecover(caList,allowCRLValidation).getCertChainStatus(timeStampCertificate));
			} catch (CertStatusException e) {
				logger.info("[XAdESXLSignature.completeToXAdESXL]::Error obteniendo respuestas OCSP para el certificado de sello", e);
				throw new RetrieveOCSPException("Error obteniendo respuestas OCSP para el certificado de sello", e);
			}
			
			//-- Completar a XAdES-C
			try {
				doc = addXadesC((Element) nodeFirma, respuestasFirma, respuestasSello, DEFAULT_XADES_SCHEMA, 
						UtilidadFirmaElectronica.DIGEST_ALG_SHA1, searchXAdESNamespace (doc.getDocumentElement()));
				logger.debug("[XAdESXLSignature.completeToXAdESXL]::Completado a XAdES-C");
			} catch (Exception e) {
				throw new XMLDocumentException(e);
			}
			
			//-- Completar a XAdES-X
			// A partir del nodo raíz de la firma se obtiene el nodo UnsignedSignatureProperties
			Element unsignedSignaturePropertiesElement = null;
			NodeList unsignedSignaturePropertiesNodes = 
					nodeFirma.getElementsByTagNameNS(DEFAULT_XADES_SCHEMA_URI, ConstantesXADES.UNSIGNED_SIGNATURE_PROPERTIES);
			
			if (unsignedSignaturePropertiesNodes.getLength() != 1) {
				// El nodo UnsignedSignatureProperties no existe o no es único 
				logger.info("[XAdESXLSignature.completeToXAdESXL]::El nodo UnsignedSignatureProperties no existe o no es único");
				// El sistema no soporta nodos UnsignedSignatureProperties múltiples
				throw new XMLDocumentException("El nodo UnsignedSignatureProperties no existe o no es único");
			} else {
				unsignedSignaturePropertiesElement = (Element) unsignedSignaturePropertiesNodes.item(0);
			}
			
			// Se añaden los elementos propios de la firma XADES-X
			try {
				doc = addXadesX(unsignedSignaturePropertiesElement, urlTSA, ConstantesTSA.SHA1, searchXAdESNamespace (doc.getDocumentElement()));
				logger.debug("[XAdESXLSignature.completeToXAdESXL]::Completado a XAdES-X");
			} catch (Exception e) {
				throw new XMLDocumentException(e);
			}

			//-- Completar a XAdES-X-L
			try {
				doc = addXadesXL(nodeFirma, respuestasFirma, respuestasSello, XAdESSchemas.getXAdESSchema(DEFAULT_XADES_SCHEMA_URI),
						searchXAdESNamespace (doc.getDocumentElement()));
				logger.debug("[XAdESXLSignature.completeToXAdESXL]::Completado a XAdES-X-L");
			} catch (Exception e) {
				throw new XMLDocumentException(e);
			}
		}
		
		return new XAdESXLSignature(doc);
	}
	
	/**
	 * Añade un sello de tiempos de archivado a todas las firmas incluidas en este
	 * fichero XAdES-X-L (convirtiéndolo, si no lo era ya, en un XAdES-A).
	 * 
	 * @param urlTSA URL del servidor de sello de tiempos
	 * @throws SignatureException Error tratando con las firmas contenidas en el XAdES-X-L
	 * @throws TimeStampException Error obteniendo el sello de tiempos
	 * @throws XMLDocumentException Error modificando el XML para añadirle los nuevos sellos
	 * 	de tiempos
	 */
	public void addArchiveTimeStamp (URL urlTSA) throws SignatureException, TimeStampException, XMLDocumentException {
		logger.debug("[XAdESXLSignature.addArchiveTimeStamp]::Entrada");
		
		//-- Por si acaso inicializar el xmlsec
		org.apache.xml.security.Init.init();
		
		try {
			TSCliente tsCliente = new TSCliente(urlTSA.toString(), ConstantesTSA.SHA1);
			
			//-- Obtener los nodos de firma
			String uriXmlNS = "http://www.w3.org/2000/09/xmldsig#";
			NodeList signatureList = xadesDocument.getElementsByTagNameNS(uriXmlNS, "Signature");
			
			//-- Recorrer los nodos de firma
			for(int i=0;i<signatureList.getLength();i++) {
				XMLSignature firma = new XMLSignature((Element)signatureList.item(i), "./");
				logger.debug("[XAdESXLSignature.addArchiveTimeStamp]::Resellando la firma: " + firma.getId());
				
				// Para los esquemas 1.1.1 y 1.2.2 se añaden includes con los nodos tomados
				ArrayList<String> inc = null;
				if (ConstantesXADES.SCHEMA_XADES_111.equals(DEFAULT_XADES_SCHEMA_URI) || ConstantesXADES.SCHEMA_XADES_122.equals(DEFAULT_XADES_SCHEMA_URI)) {
					inc = UtilidadXadesA.obtenerListadoIdsElementosXadesA(DEFAULT_XADES_SCHEMA_URI, firma, null);
				}
				
				// Se obtiene la cadena de bytes de entrada del sello XAdES-A
				byte[] input = UtilidadXadesA.obtenerListadoXadesA(DEFAULT_XADES_SCHEMA_URI, firma, null);
				addXadesA(firma.getElement(), tsCliente.generarSelloTiempo(input), inc) ;
				logger.debug("[XAdESXLSignature.addArchiveTimeStamp]::Resellada la firma: " + firma.getId());
			}
		} catch (XMLSignatureException e) {
			logger.info("[XAdESXLSignature.addArchiveTimeStamp]::Uno de los elementos 'Signature' está mal formado", e);
			throw new SignatureException("Uno de los elementos 'Signature' está mal formado", e);
		} catch (XMLSecurityException e) {
			logger.info("[XAdESXLSignature.addArchiveTimeStamp]::Error de seguridad accediendo a uno de los elementos 'Signature'", e);
			throw new SignatureException("Error de seguridad accediendo a uno de los elementos 'Signature'", e);
		} catch (BadFormedSignatureException e) {
			logger.info("[XAdESXLSignature.addArchiveTimeStamp]::Uno de los elementos 'Signature' está mal formado", e);
			throw new SignatureException("Uno de los elementos 'Signature' está mal formado", e);
		} catch (FirmaXMLError e) {
			logger.info("[XAdESXLSignature.addArchiveTimeStamp]::Uno de los elementos 'Signature' está mal formado", e);
			throw new SignatureException("Uno de los elementos 'Signature' está mal formado", e);
		} catch (IOException e) {
			logger.info("[XAdESXLSignature.addArchiveTimeStamp]::No se puede obtener o leer el documento que originó la firma, o no es posible escribir en memoria", e);
			throw new SignatureException("No se puede obtener o leer el documento que originó la firma, o no es posible escribir en memoria", e);
		} catch (TSClienteError e) {
			logger.info("[XAdESXLSignature.addArchiveTimeStamp]::No se puede obtener el sello de tiempos", e);
			throw new TimeStampException("No se puede obtener el sello de tiempos", e);
		} catch (Exception e) {
			logger.info("[XAdESXLSignature.addArchiveTimeStamp]::No se puede añadir el sello de tiempos de archivo a una de las firmas", e);
			throw new XMLDocumentException("No se puede añadir el sello de tiempos de archivo a una de las firmas", e);
		} 
		
	}
	
	/**
	 * Devuelve la fecha en la que caducará el certificado de la TSA incluido en el
	 * sello de tiempos. En caso de que los sellos de tiempos estén firmados por 
	 * varias TSA se devolverá la fecha de caducidad del certificado de TSA más cercana 
	 * a caducar.<br><br>
	 * 
	 * En el caso de que ya se haya resellado (las firmas sean XAdES-A) se devolverá
	 * la fecha de caducidad del certificado de TSA del último resellado.
	 * 
	 * @return Fecha de caducidad del certificado de TSA 
	 * @throws MalformedTimeStampException Alguno de los sellos de tiempos no está bien formado
	 */
	public Date getTimeStampCertificateExpiration () throws MalformedTimeStampException {
		logger.debug("[XAdESXLSignature.getTimeStampCertificateExpiration]::Entrada");
		
		//-- Buscar los nodos ArchiveTimeStamp (si existen)
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		NodeList nodeList = null;
		try {
			XPathExpression expr = xpath.compile("//*[local-name()='" + ConstantesXADES.ARCHIVE_TIME_STAMP + "']/*[local-name()='EncapsulatedTimeStamp']");
			nodeList = (NodeList) expr.evaluate (xadesDocument, XPathConstants.NODESET);
		} catch (Exception e) {
			logger.info ("[XAdESXLSignature.getTimeStampCertificateExpiration]::Error inesperado", e);
			return null;
		}
		
		Date fecha = null;
		for (int i = 0; i < nodeList.getLength(); i++) {
			//-- Obtener el sello de tiempos
			TimeStamp timeStamp = new TimeStamp(new ByteArrayInputStream (Util.decodeBase64(nodeList.item(i).getTextContent())));
			
			//-- Obtener fecha 
			Date timeStampDate = timeStamp.getSignatureCertificate().getValidityPeriodEnd();
			if (fecha == null) {
				fecha = timeStampDate;
			}
			if (fecha.before(timeStampDate)) {
				fecha = timeStampDate;
			}
		}
		
		if (fecha != null) {
			logger.debug("[XAdESXLSignature.getTimeStampCertificateExpiration]::Fecha obtenida del ultimo resellado: " + fecha);
			return fecha;
		}

		//-- Obtener todos los nodos sello de tiempo
		try {
			XPathExpression expr = xpath.compile("//*[local-name()='EncapsulatedTimeStamp']");
			nodeList = (NodeList) expr.evaluate (xadesDocument, XPathConstants.NODESET);
		} catch (Exception e) {
			logger.info ("[XAdESXLSignature.getTimeStampCertificateExpiration]::Error inesperado", e);
			return null;
		}
		
		fecha = null;
		for (int i = 0; i < nodeList.getLength(); i++) {
			//-- Obtener el sello de tiempos
			TimeStamp timeStamp = new TimeStamp(new ByteArrayInputStream (Util.decodeBase64(nodeList.item(i).getTextContent())));
			
			//-- Obtener fecha 
			Date timeStampDate = timeStamp.getSignatureCertificate().getValidityPeriodEnd();
			if (fecha == null) {
				fecha = timeStampDate;
			}
			if (fecha.after(timeStampDate)) {
				fecha = timeStampDate;
			}
		}
		
		logger.debug("[XAdESXLSignature.getTimeStampCertificateExpiration]::Fecha obtenida del certificado TSA más próximo a caducar: " + fecha);
		return fecha;
		

	}
	
	/**
	 * Método empleado para añadir los certificados de sello de tiempo y
	 * su cadena al XAdES. ¡Emplear sólo en los casos en que sea necesario:
	 * firmas completadas con versiones anteriores a la 1.4.5 de Arangí!
	 * 
	 * @param caList Lista de CAs admitidas
	 * @param urlTSA URL de la TSA
	 * @throws Exception Errores durante la modificación del XML
	 */
	public void fix (CAList caList, URL urlTSA) throws Exception {

		// Se obtiene el certificado firmante del sello de tiempos
    	X509Certificate timeStampCertificate;
    	try {
    		timeStampCertificate = getXAdESTimeStampCertificate(xadesDocument).toX509Certificate();
		} catch (MalformedTimeStampException e1) {
			logger.info("[XAdESXLSignature.completeToXAdESXL]::No se puede obtener el sello de tiempos del xades", e1);
			throw new SignatureException ("No se puede obtener el sello de tiempos del xades", e1);
		}
    	
    	//-- Lista de certificados y respuestas OCSP: cadena del certificado de tsa 
		ArrayList<RespYCerts> respuestasSello = new ArrayList<RespYCerts>();
		try {
			logger.debug("[XAdESXLSignature.completeToXAdESXL]::Obteniendo respuestas OCSP para el certificado de sello");
			respuestasSello = convertICertStatus2RespYCerts(new CAListCertStatusRecover(caList, false).getCertChainStatus(timeStampCertificate));
		} catch (CertStatusException e) {
			logger.info("[XAdESXLSignature.completeToXAdESXL]::Error obteniendo respuestas OCSP para el certificado de sello", e);
			throw new RetrieveOCSPException("Error obteniendo respuestas OCSP para el certificado de sello", e);
		}
		
		//-- Completar a XAdES-C
		NodeList listaCertRefs = xadesDocument.getElementsByTagNameNS(DEFAULT_XADES_SCHEMA_URI, ConstantesXADES.CERT_REFS);
		NodeList listaOcspRefs = xadesDocument.getElementsByTagNameNS(DEFAULT_XADES_SCHEMA_URI, ConstantesXADES.OCSP_REFS);
		NodeList listaCrlRefs = xadesDocument.getElementsByTagNameNS(DEFAULT_XADES_SCHEMA_URI, ConstantesXADES.CRL_REFS);
		NodeList listaCertificateValues = xadesDocument.getElementsByTagNameNS(DEFAULT_XADES_SCHEMA_URI, ConstantesXADES.CERTIFICATE_VALUES);
		NodeList listaOcspValues = xadesDocument.getElementsByTagNameNS(DEFAULT_XADES_SCHEMA_URI, ConstantesXADES.OCSP_VALUES);
		NodeList listaCrlValues = xadesDocument.getElementsByTagNameNS(DEFAULT_XADES_SCHEMA_URI, ConstantesXADES.CRL_VALUES);
		NodeList listaSigAndRefsTimestamp = xadesDocument.getElementsByTagNameNS(DEFAULT_XADES_SCHEMA_URI, ConstantesXADES.SIG_AND_REFS_TIME_STAMP);
		
		String namespace = searchXAdESNamespace (xadesDocument.getDocumentElement());
		String algDigestXML = UtilidadFirmaElectronica.DIGEST_ALG_SHA1;

        if(respuestasSello != null && !respuestasSello.isEmpty()) {
        	
        	int i = -1;
        	for (RespYCerts respYCerts : respuestasSello) {
        		i++;
        		
        		X509Certificate certificado = respYCerts.getCertstatus().getCertificate();
        		
        		//-- COMPLETE CERTIFICATE REFS
            	// Se agrega una id al certificado
            	String idCertificado = UtilidadTratarNodo.newID(xadesDocument, ConstantesXADES.LIBRERIAXADES_CERT_PATH);
            	respuestasSello.get(i).setIdCertificado(idCertificado);
            	
         		Element elementCertRef = xadesDocument.createElementNS(DEFAULT_XADES_SCHEMA_URI, namespace + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.CERT);
        		
        		// Creamos los atributos de UnSignedProperties
            	Attr uris = xadesDocument.createAttributeNS(null, "URI");
            	uris.setValue( ConstantesXADES.ALMOHADILLA + idCertificado );
            	NamedNodeMap atributosURI = elementCertRef.getAttributes();
            	atributosURI.setNamedItem(uris);

    	        Element resumenElementoCert = xadesDocument.createElementNS(DEFAULT_XADES_SCHEMA_URI, namespace + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.CERT_DIGEST);

    	        // Creamos el xades:DigestMethod
    	        Element metodoResumenElemento = xadesDocument.createElementNS(ConstantesXADES.SCHEMA_DSIG, xmldsigNS + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.DIGEST_METHOD);

    	        // Creamos los atributos de DigestMethod
    	        Attr propiedadesFirmaAlgoritmo = xadesDocument.createAttributeNS(null, ConstantesXADES.ALGORITHM);
    	        propiedadesFirmaAlgoritmo.setValue(algDigestXML);
    	        NamedNodeMap cualidadesMetodoResumenElemento =
    	                metodoResumenElemento.getAttributes();
    	        cualidadesMetodoResumenElemento.setNamedItem(propiedadesFirmaAlgoritmo);

    	        // Creamos el xades:DigestValue
    	        String resumenCertificado = ConstantesXADES.CADENA_VACIA;
    	        try
    	        {
        			MessageDigest resumenCertificadoTemp = UtilidadFirmaElectronica.getMessageDigest(algDigestXML);
        			if (resumenCertificadoTemp == null)
        	        	throw new Exception("No se ha podido realizar el digest");
    	            byte[] resumenMensajeByte =resumenCertificadoTemp.digest(certificado.getEncoded());
    	            resumenCertificado = new String(Base64Coder.encode(resumenMensajeByte));
    	        } catch (CertificateEncodingException e) {
    	        	logger.error(e);
    	        	throw new Exception("Error obteniendo la huella del certificado");				
    	        }

    	        Element elementDigestValue =
    	        		xadesDocument.createElementNS(ConstantesXADES.SCHEMA_DSIG, xmldsigNS + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.DIGEST_VALUE);
    	        elementDigestValue.appendChild(
    	        		xadesDocument.createTextNode(resumenCertificado));

    	        // Creamos el xades:IssuerSerial
    	        Element elementoEmisorSerial =
    	        		xadesDocument.createElementNS(DEFAULT_XADES_SCHEMA_URI, namespace + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.ISSUER_SERIAL);
    	        // Creamos el xades:X509IssuerName
    	        Element elementoX509EmisorNombre =
    	        		xadesDocument.createElementNS(ConstantesXADES.SCHEMA_DSIG, xmldsigNS + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.X_509_ISSUER_NAME);
    	        elementoX509EmisorNombre.appendChild(
    	        		xadesDocument.createTextNode(certificado.getIssuerX500Principal().getName()));

    	        // Creamos el xades:X509SerialNumber
    	        Element elementoX509NumeroSerial =
    	        		xadesDocument.createElementNS(ConstantesXADES.SCHEMA_DSIG, xmldsigNS + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.X_509_SERIAL_NUMBER);
    	        elementoX509NumeroSerial.appendChild(
    	        		xadesDocument.createTextNode(certificado.getSerialNumber().toString()));

    	        //Add references
    	        elementoEmisorSerial.appendChild(elementoX509EmisorNombre);
    	        elementoEmisorSerial.appendChild(elementoX509NumeroSerial);

    	        resumenElementoCert.appendChild(metodoResumenElemento);
    	        resumenElementoCert.appendChild(elementDigestValue);

    	        elementCertRef.appendChild(resumenElementoCert);
    	        elementCertRef.appendChild(elementoEmisorSerial);

    	        appendChild (listaCertRefs, elementCertRef);
    	        
        		//-- COMPLETE REVOCATION REFS
    	    	Element elementOCSPRef = null;
    	    	String tiempoRespuesta = null;
    	    	byte[] mensajeRespuesta = null;
            	int nOCSPRefs = 0;
            	int nCRLRefs = 0;
        		ICertStatus certStatus = respYCerts.getCertstatus();
        		if (certStatus instanceof IOCSPCertStatus) {
        			nOCSPRefs++;
        			IOCSPCertStatus respOcsp = (IOCSPCertStatus) certStatus;

	        		tiempoRespuesta = UtilidadFechas.formatFechaXML(respOcsp.getResponseDate());
	        		IOCSPCertStatus.TYPE_RESPONDER tipoResponder = respOcsp.getResponderType();
	        		String valorResponder = respOcsp.getResponderID();
	        		mensajeRespuesta = respOcsp.getEncoded();
	
	        		elementOCSPRef = xadesDocument.createElementNS(DEFAULT_XADES_SCHEMA_URI, namespace + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.OCSP_REF);
	        		
	        		// Creamos los atributos de UnSignedProperties
	        		String idOcsp = UtilidadTratarNodo.newID(xadesDocument, ConstantesXADES.OCSP);
	
	            	Element identificadorElementoOCSP = xadesDocument.createElementNS(DEFAULT_XADES_SCHEMA_URI, namespace + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.OCSP_IDENTIFIER);
	            	uris = xadesDocument.createAttributeNS(null, "URI");
	            	uris.setValue( ConstantesXADES.ALMOHADILLA + idOcsp );
	            	atributosURI = identificadorElementoOCSP.getAttributes();
	            	atributosURI.setNamedItem(uris);
	
	        		// Creamos el xades:DigestMethod
	        		Element elementoRespondedorId = xadesDocument.createElementNS(DEFAULT_XADES_SCHEMA_URI, namespace + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.RESPONDER_ID);
	
	        		
	        		Element responderFinal = elementoRespondedorId;
	        		if (!(ConstantesXADES.SCHEMA_XADES_111.equals(DEFAULT_XADES_SCHEMA_URI)) && !(ConstantesXADES.SCHEMA_XADES_122.equals(DEFAULT_XADES_SCHEMA_URI))) {
	        			Element hijo = null;
	        			if (tipoResponder.equals(IOCSPCertStatus.TYPE_RESPONDER.BY_NAME)) {
	        				hijo = xadesDocument.createElementNS(DEFAULT_XADES_SCHEMA_URI, namespace + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.BY_NAME);
	        			}
	        			else {
	        				hijo = xadesDocument.createElementNS(DEFAULT_XADES_SCHEMA_URI, namespace + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.BY_KEY);
	        			}
	        			// TODO: tener en cuenta que podria no ser ninguno de estos valores en un futuro
	            		elementoRespondedorId.appendChild(hijo);
	            		responderFinal = hijo;
	        		}
	        		responderFinal.appendChild(xadesDocument.createTextNode(valorResponder));
	    			
	
	        		Element elementoProdujoEn = xadesDocument.createElementNS(DEFAULT_XADES_SCHEMA_URI, namespace + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.PRODUCE_AT);
	
	
	        		elementoProdujoEn.appendChild(xadesDocument.createTextNode(tiempoRespuesta));
	
	        		identificadorElementoOCSP.appendChild(elementoRespondedorId);
	        		identificadorElementoOCSP.appendChild(elementoProdujoEn);
	        		Element valorYResumenElemento = xadesDocument.createElementNS(DEFAULT_XADES_SCHEMA_URI, namespace + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.DIGEST_ALG_AND_VALUE);
	
	        		// Creamos el xades:DigestMethod
	        		metodoResumenElemento = xadesDocument.createElementNS(ConstantesXADES.SCHEMA_DSIG, xmldsigNS + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.DIGEST_METHOD);
	
	        		// Creamos los atributos de DigestMethod
	        		Attr propiedadesAlgoritmoFirmado = xadesDocument.createAttributeNS(null, ConstantesXADES.ALGORITHM);
	        		propiedadesAlgoritmoFirmado.setValue(algDigestXML);
	        		NamedNodeMap atributosMetodoResumenElemento = metodoResumenElemento.getAttributes();
	        		atributosMetodoResumenElemento.setNamedItem(propiedadesAlgoritmoFirmado);
	
	        		// Creamos el xades:DigestValue
	        		// El mensaje de la respuesta es el OCSPResponse
	        		String digestCertificado =ConstantesXADES.CADENA_VACIA;
        			MessageDigest resumenCertificadoTemp = UtilidadFirmaElectronica.getMessageDigest(algDigestXML);
        			if (resumenCertificadoTemp == null)
        				throw new Exception("No se ha podido generar el digest");
        			byte[] resumenMensajeByte = resumenCertificadoTemp.digest(mensajeRespuesta);
        			digestCertificado = new String(Base64Coder.encode(resumenMensajeByte));
	
	        		Element valorResumenElemento = xadesDocument.createElementNS(ConstantesXADES.SCHEMA_DSIG, xmldsigNS + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.DIGEST_VALUE);
	
	        		valorResumenElemento.appendChild(xadesDocument.createTextNode(digestCertificado));
	
	        		valorYResumenElemento.appendChild(metodoResumenElemento);
	        		valorYResumenElemento.appendChild(valorResumenElemento);
	
	        		elementOCSPRef.appendChild(identificadorElementoOCSP);
	        		elementOCSPRef.appendChild(valorYResumenElemento);
	
	        		appendChild(listaOcspRefs, elementOCSPRef);
	        		
	        		// REVOCATION VALUES
	        		Element valorElementoEncapsuladoOCSP = xadesDocument.createElementNS(DEFAULT_XADES_SCHEMA_URI, namespace + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.ENCAPSULATED_OCSP_VALUE);
    	            valorElementoEncapsuladoOCSP.appendChild(
    	            		xadesDocument.createTextNode(new String(Base64Coder.encode(respOcsp.getEncoded()))));
    	            valorElementoEncapsuladoOCSP.setAttributeNS(null, ConstantesXADES.ID, idOcsp);
    	            appendChild(listaOcspValues, valorElementoEncapsuladoOCSP);

        		}
        		else if (certStatus instanceof IX509CRLCertStatus) {
        			nCRLRefs++;
        			IX509CRLCertStatus respCRL = (IX509CRLCertStatus) certStatus;
        			CRLRef crlRef = new CRLRef(DEFAULT_XADES_SCHEMA, algDigestXML, respCRL.getX509CRL());

					String idCrl = UtilidadTratarNodo.newID(xadesDocument, ConstantesXADES.CRL);
					crlRef.getCrlIdentifier().setUri(ConstantesXADES.ALMOHADILLA + idCrl);
	
					appendChild(listaCrlRefs, crlRef.createElement(xadesDocument, xmldsigNS, namespace));
					
	        		// REVOCATION VALUES
	        		Element valorElementoEncapsuladoCRL = xadesDocument.createElementNS(DEFAULT_XADES_SCHEMA_URI, namespace + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.ENCAPSULATED_CRL_VALUE);
	        		valorElementoEncapsuladoCRL.appendChild(
    	            		xadesDocument.createTextNode(new String(Base64Coder.encode(respCRL.getX509CRL().getEncoded()))));
	        		valorElementoEncapsuladoCRL.setAttributeNS(null, ConstantesXADES.ID, idCrl);
    	            appendChild(listaCrlValues, valorElementoEncapsuladoCRL);
        		}

        		//-- CERTIFICATE VALUES
        		EncapsulatedX509Certificate encapsulatedX509certificate = new EncapsulatedX509Certificate(DEFAULT_XADES_SCHEMA, idCertificado);
	        	try {
					encapsulatedX509certificate.setX509Certificate(certificado);
				} catch (CertificateException e) {
					logger.info(e.getMessage(), e);
					throw new Exception("No ha sido posible crear el elemento 'encapsulatedX509certificate' ");
				}
	        	appendChild(listaCertificateValues, encapsulatedX509certificate.createElement(xadesDocument, namespace));

        	}
        	
        	//-- SIGANDREFSTIMESTAMP
        	if (listaSigAndRefsTimestamp != null && listaSigAndRefsTimestamp.getLength() == 1) {
	        	// Se crea el nodo SigAndRefsTimeStamp
	            Element sigAndRefsTimeStampElement = (Element) listaSigAndRefsTimestamp.item(0);
	            sigAndRefsTimeStampElement.removeChild(sigAndRefsTimeStampElement.getFirstChild());
	            sigAndRefsTimeStampElement.removeChild(sigAndRefsTimeStampElement.getFirstChild());
	            Element unsignedSignatureProperties = (Element) sigAndRefsTimeStampElement.getParentNode();
	            
	            addXadesX(unsignedSignatureProperties, sigAndRefsTimeStampElement, urlTSA, ConstantesTSA.SHA1, namespace);
        	}       	
        }
		
	}

	//-- Implementación de XAdESSignature
	
	@Override
	protected int tratarResultadoValidacion(ResultadoValidacion resultadoValidacion) {
		if (resultadoValidacion.isValidate() && resultadoValidacion.getEnumNivel() == EnumFormatoFirma.XAdES_XL) {
			//válido
			logger.debug("[XAdESXLSignature.isValidSignatureOnly]::La firma ha pasado la validación");
			return ValidationResult.RESULT_VALID;
		} else if (resultadoValidacion.getNivelValido() == null || resultadoValidacion.getNivelValido().equals("")) {
			if (resultadoValidacion.getLog().toLowerCase().indexOf("firma inválida") > -1) {
				//certificado válido, el problema será con la firma
				logger.debug("[XAdESXLSignature.isValidSignatureOnly]::La firma no es válida");
				return ValidationResult.RESULT_SIGNATURE_NOT_MATCH_DATA;
			} else {
				//certificado caducado
				logger.debug("[XAdESXLSignature.isValidSignatureOnly]::El certificado está caducado");
				return ValidationResult.RESULT_CERTIFICATE_NOT_ACTIVE;
			}
		} else if (resultadoValidacion.getEnumNivel() == EnumFormatoFirma.XAdES_BES ||
				resultadoValidacion.getEnumNivel() == EnumFormatoFirma.XAdES_C) {
			//El BES es válido, o sea que es un problema con el sello de tiempos
			logger.debug("[XAdESXLSignature.isValidSignatureOnly]::El sello de tiempos no es válido");
			return ValidationResult.RESULT_INVALID_TIMESTAMP;
		} else if (resultadoValidacion.getResultado() == ResultadoEnum.INVALID) {
			//revocado
			logger.debug("[XAdESXLSignature.isValidSignatureOnly]::El certificado de la firma está revocado");
			return ValidationResult.RESULT_CERTIFICATE_REVOKED;
		} else if (resultadoValidacion.getEnumNivel() == EnumFormatoFirma.XAdES_T) {
			//no hay información de revocación (respuestas OCSP)
			logger.debug("[XAdESXLSignature.isValidSignatureOnly]::El XAdES es un XAdES-T");
			return ValidationResult.RESULT_CERTIFICATE_CANNOT_BE_VALIDATED;
		} else  {
			//desconocido
			logger.debug("[XAdESXLSignature.isValidSignatureOnly]::La firma no ha pasado la validación");
			return ValidationResult.RESULT_CERTIFICATE_UNKNOWN;
		}
	}
	
	/**
	 * Formato de firma: XAdES-X-L
	 */
	protected EnumFormatoFirma getXAdESSignatureFormat () {
		return EnumFormatoFirma.XAdES_XL;
	}
	

	//-- Métodos privados

	/*
	 * Añade el sello de archivo a una firma
	 */
    private void addXadesA (Element firma, byte[] selloTiempo, ArrayList<String> inc) throws Exception {
    	
    	Document doc = firma.getOwnerDocument();
    	
    	ArrayList<Element> UnsignedSignaturePropertiesNodes = UtilidadTratarNodo.obtenerNodos(firma, 4, 
				new NombreNodo(DEFAULT_XADES_SCHEMA_URI, ConstantesXADES.UNSIGNED_SIGNATURE_PROPERTIES));
    	Element UnsignedSignaturePropertiesNode = null;
    	if(UnsignedSignaturePropertiesNodes.size() == 1)
    		UnsignedSignaturePropertiesNode = (Element)UnsignedSignaturePropertiesNodes.get(0);
    	else
    		// No se encuentra el nodo UnsignedSignatureProperties
    		throw new AddXadesException(I18n.getResource(ConstantesXADES.LIBRERIAXADES_FIRMAXML_ERROR_19)) ;

    	Element archiveTimeStamp =
    		doc.createElementNS(DEFAULT_XADES_SCHEMA_URI, xadesNS + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.ARCHIVE_TIME_STAMP);
    	
    	// Creamos los atributos de ArchiveTimeStamp (Id)
    	Attr archiveTimeStampId = doc.createAttributeNS(null, ConstantesXADES.ID);
    	archiveTimeStampId.setValue(UtilidadTratarNodo.newID(doc, 
    			ConstantesXADES.ARCHIVE_TIME_STAMP + ConstantesXADES.GUION));
    	NamedNodeMap archiveTimeStampAttributesElement =
    		archiveTimeStamp.getAttributes();
    	archiveTimeStampAttributesElement.setNamedItem(archiveTimeStampId);
    	
    	// Se agrega el nodo EncapsulatedTimeStamp, con Id y Encoding como atributos
    	Element encapsulatedTimeStamp =
    		doc.createElementNS(DEFAULT_XADES_SCHEMA_URI, xadesNS + ConstantesXADES.DOS_PUNTOS + 
    				ConstantesXADES.ENCAPSULATED_TIME_STAMP);
    	
    	// Se escribe una Id Ãºnica
    	Attr informacionElementoSigTimeStamp = doc.createAttributeNS(null, ConstantesXADES.ID);
    	String idSelloTiempo = UtilidadTratarNodo.newID(doc, ConstantesXADES.SELLO_TIEMPO_TOKEN);
    	informacionElementoSigTimeStamp.setValue(idSelloTiempo);
    	encapsulatedTimeStamp.getAttributes().setNamedItem(informacionElementoSigTimeStamp);
    	
    	// Se agrega el CanonicalizationMethod
    	Element canonicalizationElemento = doc.createElementNS(ConstantesXADES.SCHEMA_DSIG, xmldsigNS + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.CANONICALIZATION_METHOD);		
		Attr canonicalizationAttribute = doc.createAttributeNS(null, ConstantesXADES.ALGORITHM);
		canonicalizationAttribute.setValue(Transforms.TRANSFORM_C14N_OMIT_COMMENTS);
		canonicalizationElemento.getAttributes().setNamedItem(canonicalizationAttribute);

		archiveTimeStamp.appendChild(canonicalizationElemento);
        
        encapsulatedTimeStamp.appendChild(doc.createTextNode(new String(Base64Coder.encode(selloTiempo))));       
        
        // Se agregan, si existen, los nodos include
        if (inc != null) {
        	Element includeNode = null;
        	for (int i = 0; i < inc.size(); ++i) {
        		includeNode = doc.createElementNS(DEFAULT_XADES_SCHEMA_URI, xadesNS + ConstantesXADES.DOS_PUNTOS + 
        				ConstantesXADES.INCLUDE);      	
        		includeNode.setAttributeNS(null, ConstantesXADES.URI_MAYUS, inc.get(i));
        		archiveTimeStamp.appendChild(includeNode);
        	}
        }
        
        archiveTimeStamp.appendChild(encapsulatedTimeStamp);
        
        // Se agrega el sello creado a las propiedades no firmadas
        UnsignedSignaturePropertiesNode.appendChild(archiveTimeStamp);
        
    }

    /*
     * Añade un elemento a una lista de nodos
     */
	private void appendChild(NodeList nodeList, Element element) {
		if (nodeList != null && element != null) {
			for(int i=0; i<nodeList.getLength(); i++) {
				nodeList.item(i).appendChild(element);
			}
		}
	}

    @Override
    public String toString() {
    	StringBuffer sb = new StringBuffer();
    	sb.append(getString (xadesDocument));
    	return sb.toString();
    }

	private String getString(Node node) {
    	StringBuffer sb = new StringBuffer();
    	sb.append(node.getNodeName() + ": " + node.getNodeValue() + "\n");
    	if (node.getAttributes() != null) {
	        for (int i=0; i< node.getAttributes().getLength();i++) {
	        	sb.append("@" + node.getAttributes().item(i).getNodeName() + ": " + node.getAttributes().item(i).getNodeValue() + "\n");
	        }
    	}
        for (int i=0; i< node.getChildNodes().getLength();i++) {
    		sb.append(getString(node.getChildNodes().item(i)));
    	}
		return sb.toString();
	}
}

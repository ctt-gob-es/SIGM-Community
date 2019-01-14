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
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.security.cert.X509Certificate;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.xml.crypto.dsig.CanonicalizationMethod;

import org.apache.log4j.Logger;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.accv.arangi.base.certificate.Certificate;
import es.accv.arangi.base.certificate.validation.CAList;
import es.accv.arangi.base.certificate.validation.service.CertificateValidationService;
import es.accv.arangi.base.device.DeviceManager;
import es.accv.arangi.base.document.FileDocument;
import es.accv.arangi.base.document.IDocument;
import es.accv.arangi.base.document.URLDocument;
import es.accv.arangi.base.exception.certificate.NormalizeCertificateException;
import es.accv.arangi.base.exception.device.LoadingObjectException;
import es.accv.arangi.base.exception.device.SearchingException;
import es.accv.arangi.base.exception.document.HashingException;
import es.accv.arangi.base.exception.document.InitDocumentException;
import es.accv.arangi.base.exception.signature.CounterSignatureException;
import es.accv.arangi.base.exception.signature.NoCoincidentDocumentException;
import es.accv.arangi.base.exception.signature.NoDocumentToSignException;
import es.accv.arangi.base.exception.signature.SignatureException;
import es.accv.arangi.base.exception.signature.SignatureNotFoundException;
import es.accv.arangi.base.exception.signature.XMLDocumentException;
import es.accv.arangi.base.exception.timestamp.MalformedTimeStampException;
import es.accv.arangi.base.exception.timestamp.ResponseTimeStampException;
import es.accv.arangi.base.exception.timestamp.TimeStampServerConnectionException;
import es.accv.arangi.base.mityc.ContraFirmaXML;
import es.accv.arangi.base.signature.util.TSAData;
import es.accv.arangi.base.signature.util.XAdESAttachedNodeToSignObject;
import es.accv.arangi.base.signature.util.XAdESAttachedSignatureOptions;
import es.accv.arangi.base.signature.util.XAdESDataObjectFormat;
import es.accv.arangi.base.signature.util.XAdESDetachedSignatureOptions;
import es.accv.arangi.base.timestamp.TimeStamp;
import es.accv.arangi.base.util.validation.ValidationResult;
import es.mityc.firmaJava.libreria.ConstantesXADES;
import es.mityc.firmaJava.libreria.utilidades.UtilidadFicheros;
import es.mityc.firmaJava.libreria.utilidades.UtilidadTratarNodo;
import es.mityc.firmaJava.libreria.xades.CanonicalizationEnum;
import es.mityc.firmaJava.libreria.xades.DataToSign;
import es.mityc.firmaJava.libreria.xades.EnumFormatoFirma;
import es.mityc.firmaJava.libreria.xades.ResultadoEnum;
import es.mityc.firmaJava.libreria.xades.ResultadoValidacion;
import es.mityc.firmaJava.libreria.xades.errores.FirmaXMLError;

/**
 * Clase que maneja firmas en formato XAdES-T de acuerdo al estándar 
 * <a href="http://uri.etsi.org/01903/v1.3.2/ts_101903v010302p.pdf" target="etsi">
 * ETSI TS 101 903</a><br><br>
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
 * URL urlTSA = new URL (...);<br><br>
 * 
 * //-- Genera una firma attached. El documento se guardará en la firma en base64<br>
 * XAdESTSignature signature1 = XAdESTSignature.signAttached(manager, alias, documentTexto, urlTSA);<br><br>
 * 
 * //-- Genera una firma detached que referencia al fichero en disco<br>
 * XAdESTSignature signature2 = XAdESTSignature.signDetached(manager, alias, file, urlTSA);<br><br>
 * 
 * //-- Genera una firma detached que referencia a "2011/04/29/certificados/CER-2584665.pdf"<br>
 * XAdESTSignature signature3 = XAdESTSignature.signDetached(manager, alias, file, "2011/04/29/certificados/CER-2584665.pdf", urlTSA);<br><br>
 * 
 * //-- Genera una firma detached que referencia al fichero ubicado en la URL<br>
 * XAdESTSignature signature4 = XAdESTSignature.signDetached(manager, alias, url, urlTSA);<br><br>
 * 
 * //-- Genera una firma attached dentro del propio documento<br>
 * XAdESTSignature signature5 = XAdESTSignature.signAttached(manager, alias, documentoXML, "titulo", "documento", urlTSA);<br><br>
 * </code>
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José M Gutiérrez</a>
 */
public class XAdESTSignature extends XAdESSignature {
	
	/*
	 * Logger de la clase
	 */
	static Logger logger = Logger.getLogger(XAdESTSignature.class);
	
	/**
	 * Tipo de la firma
	 */
	public static final String SIGNATURE_TYPE	= "XAdES-T";
	
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
	 * XAdES-T
	 * 
	 * @param xmlDocument Documento XML
	 */
	public XAdESTSignature(Document xmlDocument) {
		initialize(xmlDocument);
	}
	
	/**
	 * Construye el objeto en base a un fichero XAdES-T
	 * 
	 * @param xmlFile Fichero XAdES-T
	 * @throws FileNotFoundException El fichero no existe
	 * @throws XMLDocumentException El fichero no parece un XML válido
	 */
	public XAdESTSignature(File xmlFile) throws FileNotFoundException, XMLDocumentException {
		initialize(xmlFile);
	}

	/**
	 * Construye el objeto en base a un array de bytes.
	 * 
	 * @param signature Firma XAdES-T
	 * @throws XMLDocumentException El fichero no parece un XML válido
	 */
	public XAdESTSignature(byte[] signature) throws XMLDocumentException {
		initialize(signature);
	}

	/**
	 * Construye el objeto en base a un stream de lectura.
	 * 
	 * @param isSignature Stream de lectura a una firma XAdES-T
	 * @throws XMLDocumentException El fichero no parece un XML válido
	 */
	public XAdESTSignature(InputStream isSignature) throws XMLDocumentException {
		initialize(isSignature);
	}

	/**
	 * Construye el objeto en base a un XAdES-BES y la URL de una TSA.
	 * 
	 * @param xadesBES Firma XAdES-BES
	 * @param caList CAList para comprobar la validez de la firma XAdES-BES antes de completarla
	 * 	a XAdES-T
	 * @param urlTSA URL del servidor de sello de tiempos
	 * @throws HashingException Error obteniendo el hash del documento
	 * @throws NormalizeCertificateException Alguno de los certificados no puede ser 
	 * 	normalizado al formato reconocido por el proveedor criptográfico de Arangí o su 
	 * 	firma no es correcta o no puede ser analizada
	 * @throws NoDocumentToSignException La firma no es attached por lo que no hay documento con
	 * 	el que validarla. 
	 * @throws XMLDocumentException Error completando el XML del XAdES-BES a XAdES-T
	 * @throws MalformedTimeStampException El sello de tiempos obtenido no está bien formado
	 * @throws ResponseTimeStampException No se ha podido obtener correctamente el sello de tiempos
	 */
	public XAdESTSignature(XAdESBESSignature xadesBES, CAList caList, URL urlTSA) throws HashingException, SignatureException, NormalizeCertificateException, NoDocumentToSignException, XMLDocumentException, MalformedTimeStampException, ResponseTimeStampException {
		initialize (completeToXAdEST(xadesBES, caList, urlTSA).getDOM());
	}

	//-- Implementación de métodos de Signature

	//-- Métodos públicos
	
	/**
	 * Obtiene la fecha del sello de tiempos de la firma (Punto 7.3 del estándar).
	 * 
	 * @return Fecha del sello de tiempos
	 * @throws MalformedTimeStampException El sello de tiempos guardado en el fichero XAdES-T no 
	 * está bien formado
	 */
	public Date getTimeStampTime () throws MalformedTimeStampException {
		return getXAdESTimeStampTime();

	}
	
	/**
	 * Realiza una firma XAdES-T detached (el fichero no se incluirá en la firma). No completa los campos 
	 * no obligatorios del tag 'SignedSignatureProperties':'signaturePolicyIdentifier', 
	 * 'signatureProductionPlace' y 'signerRole'.
	 * 
	 * La referencia a la que apuntará la firma será el path del fichero.
	 * 
	 * @param manager Dispositivo criptográfico que realizará la firma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param document Fichero a firmar
	 * @param urlTSA URL del servidor de sello de tiempos
	 * @return Firma XADES-T
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 * @throws NoDocumentToSignException El fichero a firmar no existe o es nulo
	 * @deprecated Usar {@link #signDetached(DeviceManager,String,IDocument,String,TSAData,XAdESDetachedSignatureOptions) signDetached}
	 */
	public static XAdESTSignature signDetached (DeviceManager manager, String alias, File document, URL urlTSA) throws LoadingObjectException, SignatureException, NoDocumentToSignException {
		return signDetached(manager, alias, document, null, urlTSA, null, null);
	}
	
	/**
	 * Realiza una firma XAdES-T detached (el fichero no se incluirá en la firma). No completa los campos 
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
	 * @param dof Información para construir el tag DataObjectFormat (puede ser null)
	 * @param claimedRoles Roles de la firma (puede ser null)
	 * @return Firma XADES-T
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 * @throws NoDocumentToSignException El fichero a firmar no existe o es nulo
	 * @deprecated Usar {@link #signDetached(DeviceManager,String,IDocument,String,TSAData,XAdESDetachedSignatureOptions) signDetached}
	 */
	public static XAdESTSignature signDetached (DeviceManager manager, String alias, File document, 
			String digitalSignatureAlgorithm, URL urlTSA, XAdESDataObjectFormat dof, String[] claimedRoles) 
					throws LoadingObjectException, SignatureException, NoDocumentToSignException {
		
		logger.debug("[XAdESTSignature.signDetached]::Entrada::" + Arrays.asList(new Object[] { manager, alias, document, digitalSignatureAlgorithm, urlTSA, dof, claimedRoles }));
		
		//-- Obtener el document de Arangi y llamar a la firma attached
		FileDocument fileDocument;
		try {
			fileDocument = new FileDocument(document);
		} catch (InitDocumentException e) {
			logger.info("[XAdESTSignature.signAttached]::El fichero a firmar no existe o es nulo:: " + document);
			throw new NoDocumentToSignException("El fichero a firmar no existe o es nulo: " + document);
		}
		
		//-- Obtener la referencia al fichero
		String reference = UtilidadFicheros.relativizeRute("#", document);

		return signDetached(manager, alias, fileDocument, digitalSignatureAlgorithm, reference, urlTSA, dof, claimedRoles);
		
	}
	
	/**
	 * Realiza una firma XAdES-T detached (el fichero no se incluirá en la firma). No completa los campos 
	 * no obligatorios del tag 'SignedSignatureProperties':'signaturePolicyIdentifier', 
	 * 'signatureProductionPlace' y 'signerRole'.
	 * 
	 * La referencia a la que apuntará la firma será la URL del documento.
	 * 
	 * @param manager Dispositivo criptográfico que realizará la firma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param urlDocument Documento a firmar. Se encuentra en una URL accesible.
	 * @param urlTSA URL del servidor de sello de tiempos
	 * @return Firma XADES-T
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 * @throws NoDocumentToSignException La URL a firmar no existe o es nula
	 * @deprecated Usar {@link #signDetached(DeviceManager,String,IDocument,String,TSAData,XAdESDetachedSignatureOptions) signDetached}
	 */
	public static XAdESTSignature signDetached (DeviceManager manager, String alias, URL urlDocument, URL urlTSA) throws LoadingObjectException, SignatureException, NoDocumentToSignException {
		return signDetached(manager, alias, urlDocument, null, urlTSA, null, null);
	}
	
	/**
	 * Realiza una firma XAdES-T detached (el fichero no se incluirá en la firma). No completa los campos 
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
	 * @param dof Información para construir el tag DataObjectFormat (puede ser null)
	 * @param claimedRoles Roles de la firma (puede ser null)
	 * @return Firma XADES-T
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 * @throws NoDocumentToSignException La URL a firmar no existe o es nula
	 * @deprecated Usar {@link #signDetached(DeviceManager,String,IDocument,String,TSAData,XAdESDetachedSignatureOptions) signDetached}
	 */
	public static XAdESTSignature signDetached (DeviceManager manager, String alias, URL urlDocument, 
			String digitalSignatureAlgorithm, URL urlTSA, XAdESDataObjectFormat dof, String[] claimedRoles) 
					throws LoadingObjectException, SignatureException, NoDocumentToSignException {
		
		logger.debug("[XAdESTSignature.signDetached]::Entrada::" + Arrays.asList(new Object[] { manager, alias, urlDocument, digitalSignatureAlgorithm, urlTSA, dof }));
		
		//-- Obtener el document de Arangi y llamar a la firma attached
		URLDocument document;
		try {
			document = new URLDocument(urlDocument);
		} catch (InitDocumentException e) {
			logger.info("[XAdESTSignature.signAttached]::La URL a firmar no existe o es nula:: " + urlDocument);
			throw new NoDocumentToSignException("La URL a firmar no existe o es nula: " + urlDocument);
		}
		
		//-- Obtener la referencia al fichero
		String reference = urlDocument.toString();
		
		return signDetached(manager, alias, document, digitalSignatureAlgorithm, reference, urlTSA, dof, claimedRoles);
	}
	
	/**
	 * Realiza una firma XAdES-T detached (el fichero no se incluirá en la firma). No completa los campos 
	 * no obligatorios del tag 'SignedSignatureProperties':'signaturePolicyIdentifier', 
	 * 'signatureProductionPlace' y 'signerRole'.
	 * 
	 * @param manager Dispositivo criptográfico que realizará la firma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param document Contenido a firmar. 
	 * @param reference Referencia a la que apuntará la firma
	 * @param urlTSA URL del servidor de sello de tiempos
	 * @return Firma XADES-T
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 * @deprecated Usar {@link #signDetached(DeviceManager,String,IDocument,String,TSAData,XAdESDetachedSignatureOptions) signDetached}
	 */
	public static XAdESTSignature signDetached (DeviceManager manager, String alias, IDocument document, String reference, URL urlTSA) throws LoadingObjectException, SignatureException {
		return signDetached(manager, alias, document, reference, null, urlTSA, null, null);
	}
	
	/**
	 * Realiza una firma XAdES-T detached (el fichero no se incluirá en la firma). No completa los campos 
	 * no obligatorios del tag 'SignedSignatureProperties':'signaturePolicyIdentifier' y
	 * 'signatureProductionPlace'.
	 * 
	 * @param manager Dispositivo criptográfico que realizará la firma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param document Contenido a firmar. 
	 * @param digitalSignatureAlgorithm Algoritmo de firma (si nulo algoritmo por defecto)
	 * @param reference Referencia a la que apuntará la firma
	 * @param urlTSA URL del servidor de sello de tiempos
	 * @param dof Información para construir el tag DataObjectFormat (puede ser null)
	 * @param claimedRoles Roles de la firma (puede ser null)
	 * @return Firma XADES-T
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 * @deprecated Usar {@link #signDetached(DeviceManager,String,IDocument,String,TSAData,XAdESDetachedSignatureOptions) signDetached}
	 */
	public static XAdESTSignature signDetached (DeviceManager manager, String alias, IDocument document, 
			String digitalSignatureAlgorithm, String reference, URL urlTSA, XAdESDataObjectFormat dof, String[] claimedRoles) throws LoadingObjectException, SignatureException {
		
		logger.debug("[XAdESTSignature.signDetached]::Entrada::" + Arrays.asList(new Object[] { manager, alias, document, digitalSignatureAlgorithm, reference, urlTSA, dof, claimedRoles }));
		
		TSAData tsaData = new TSAData(urlTSA);
		XAdESDetachedSignatureOptions options = new XAdESDetachedSignatureOptions(digitalSignatureAlgorithm, null, dof, claimedRoles, null, null);
		return signDetached(manager, alias, document, reference, tsaData, options);
	}
	
	/**
	 * Realiza una firma XAdES-T detached (el fichero no se incluirá en la firma).
	 * 
	 * @param manager Dispositivo criptográfico que realizará la firma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param document Contenido a firmar. 
	 * @param reference Referencia a la que apuntará la firma
	 * @param tsaData Información de conexión a la TSA
	 * @param options Opciones de la firma
	 * @return Firma XADES-T
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 */
	public static XAdESTSignature signDetached (DeviceManager manager, String alias, IDocument document, 
			String reference, TSAData tsaData, XAdESDetachedSignatureOptions options) throws LoadingObjectException, SignatureException {
		
		logger.debug("[XAdESTSignature.signDetached]::Entrada::" + Arrays.asList(new Object[] { manager, alias, document, reference, tsaData, options }));
		
		return (XAdESTSignature) signDetached(manager, alias, document, reference, tsaData, null, options, EnumFormatoFirma.XAdES_T, XAdESTSignature.class);
	}
	
	/**
	 * Realiza una firma XAdES-T attached (el documento se incluye en la firma). No completa los campos no 
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
	 * @return Firma XADES-T
	 * @throws XMLDocumentException Error montando el fichero XML
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 * @deprecated Usar {@link #signAttached(DeviceManager,String,IDocument,TSAData,XAdESAttachedSignatureOptions) signAttached}
	 */
	public static XAdESTSignature signAttached (DeviceManager manager, String alias, IDocument document, String idToSign,
			String signatureParent, URL urlTSA) throws XMLDocumentException, LoadingObjectException, SignatureException  {
		return signAttached(manager, alias, document, null, idToSign, signatureParent, urlTSA, null, null);
	}
	
	/**
	 * Realiza una firma XAdES-T attached (el documento se incluye en la firma). No completa los campos no 
	 * obligatorios del tag 'SignedSignatureProperties':'signaturePolicyIdentifier' y 'signatureProductionPlace' 
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
	 * @param dof Información para construir el tag DataObjectFormat (puede ser null)
	 * @param claimedRoles Roles de la firma (puede ser null)
	 * @return Firma XADES-T
	 * @throws XMLDocumentException Error montando el fichero XML
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 * @deprecated Usar {@link #signAttached(DeviceManager,String,IDocument,TSAData,XAdESAttachedSignatureOptions) signAttached}
	 */
	public static XAdESTSignature signAttached (DeviceManager manager, String alias, IDocument document, 
			String digitalSignatureAlgorithm, String idToSign, String signatureParent, URL urlTSA, 
			XAdESDataObjectFormat dof, String[] claimedRoles) 
					throws XMLDocumentException, LoadingObjectException, SignatureException  {
		
		logger.debug("[XAdESTSignature.signAttached]::Entrada::" + Arrays.asList(new Object[] { manager, alias, document, digitalSignatureAlgorithm, idToSign, signatureParent, dof, claimedRoles }));
	
		TSAData tsaData = new TSAData(urlTSA);
		XAdESAttachedNodeToSignObject nodeToSign = null;
		if (idToSign != null) {
			nodeToSign = new XAdESAttachedNodeToSignObject(idToSign);
		}
		XAdESAttachedSignatureOptions options = new XAdESAttachedSignatureOptions(digitalSignatureAlgorithm, null, dof, claimedRoles, null, null, nodeToSign);
		return signAttached(manager, alias, document, tsaData, options);
	}
	
	/**
	 * Realiza una firma XAdES-T attached (el documento se incluye en la firma). No completa los campos no 
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
	 * @return Firma XADES-T
	 * @throws XMLDocumentException Error montando el fichero XML
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 * @deprecated Usar {@link #signAttached(DeviceManager,String,IDocument,TSAData,XAdESAttachedSignatureOptions) signAttached}
	 */
	public static XAdESTSignature signAttached (DeviceManager manager, String alias, IDocument document, 
			URL urlTSA) throws XMLDocumentException, LoadingObjectException, SignatureException  {
		return signAttached(manager, alias, document, null, urlTSA, null, null);
	}
	
	/**
	 * Realiza una firma XAdES-T attached (el documento se incluye en la firma). No completa los campos no 
	 * obligatorios del tag 'SignedSignatureProperties':'signaturePolicyIdentifier' y 'signatureProductionPlace'.
	 * <br><br>
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
	 * @param dof Información para construir el tag DataObjectFormat (puede ser null)
	 * @param claimedRoles Roles de la firma (puede ser null)
	 * @return Firma XADES-T
	 * @throws XMLDocumentException Error montando el fichero XML
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 * @deprecated Usar {@link #signAttached(DeviceManager,String,IDocument,TSAData,XAdESAttachedSignatureOptions) signAttached}
	 */
	public static XAdESTSignature signAttached (DeviceManager manager, String alias, IDocument document, 
			String digitalSignatureAlgorithm, URL urlTSA, XAdESDataObjectFormat dof, String[] claimedRoles) throws XMLDocumentException, LoadingObjectException, SignatureException  {
		
		logger.debug("[XAdESTSignature.signDetached]::Entrada::" + Arrays.asList(new Object[] { manager, alias, document, digitalSignatureAlgorithm, dof, claimedRoles }));
	
		TSAData tsaData = new TSAData(urlTSA);
		XAdESAttachedSignatureOptions options = new XAdESAttachedSignatureOptions(digitalSignatureAlgorithm, null, dof, claimedRoles, null, null, null);
		return signAttached(manager, alias, document, tsaData, options);
	}
	
	/**
	 * Realiza una firma XAdES-T attached (el documento se incluye en la firma). No completa los campos no 
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
	 * @return Firma XADES-T
	 * @throws XMLDocumentException Error montando el fichero XML
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 * @deprecated Usar {@link #signAttached(DeviceManager,String,IDocument,TSAData,XAdESAttachedSignatureOptions) signAttached}
	 */
	public static XAdESTSignature signAttached (DeviceManager manager, String alias, File document, URL urlTSA) throws LoadingObjectException, SignatureException, NoDocumentToSignException, XMLDocumentException {
		return signAttached(manager, alias, document, null, urlTSA, null, null);
	}
	
	/**
	 * Realiza una firma XAdES-T attached (el documento se incluye en la firma). No completa los campos no 
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
	 * @param dof Información para construir el tag DataObjectFormat (puede ser null)
	 * @param claimedRoles Roles de la firma (puede ser null)
	 * @return Firma XADES-T
	 * @throws XMLDocumentException Error montando el fichero XML
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 * @deprecated Usar {@link #signAttached(DeviceManager,String,IDocument,TSAData,XAdESAttachedSignatureOptions) signAttached}
	 */
	public static XAdESTSignature signAttached (DeviceManager manager, String alias, File document, 
			String digitalSignatureAlgorithm, URL urlTSA, XAdESDataObjectFormat dof, String[] claimedRoles) 
					throws LoadingObjectException, SignatureException, NoDocumentToSignException, XMLDocumentException {
		
		logger.debug("[XAdESTSignature.signAttached]::Entrada::" + Arrays.asList(new Object[] { manager, alias, document, digitalSignatureAlgorithm, urlTSA, dof, claimedRoles }));
		
		//-- Obtener el document de Arangi y llamar a la firma attached
		FileDocument fileDocument;
		try {
			fileDocument = new FileDocument(document);
		} catch (InitDocumentException e) {
			logger.info("[XAdESTSignature.signAttached]::El fichero a firmar no existe o es nulo:: " + document);
			throw new NoDocumentToSignException("El fichero a firmar no existe o es nulo: " + document);
		}
		
		return signAttached(manager, alias, fileDocument, digitalSignatureAlgorithm, urlTSA, dof, claimedRoles);
		
	}
	
	/**
	 * Realiza una firma XAdES-T attached (el documento se incluye en la firma). No completa los campos no 
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
	 * @param document Fichero a firmar en una URL
	 * @param urlTSA URL del servidor de sello de tiempos
	 * @return Firma XADES-T
	 * @throws XMLDocumentException Error montando el fichero XML
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 * @deprecated Usar {@link #signAttached(DeviceManager,String,IDocument,TSAData,XAdESAttachedSignatureOptions) signAttached}
	 */
	public static XAdESTSignature signAttached (DeviceManager manager, String alias, URL document, URL urlTSA) throws LoadingObjectException, SignatureException, NoDocumentToSignException, XMLDocumentException {
		return signAttached(manager, alias, document, null, urlTSA, null, null);
		
	}
	
	/**
	 * Realiza una firma XAdES-T attached (el documento se incluye en la firma). No completa los campos no 
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
	 * @param document Fichero a firmar en una URL
	 * @param digitalSignatureAlgorithm Algoritmo de firma (si nulo algoritmo por defecto)
	 * @param urlTSA URL del servidor de sello de tiempos
	 * @param dof Información para construir el tag DataObjectFormat (puede ser null)
	 * @param claimedRoles Roles de la firma (puede ser null)
	 * @return Firma XADES-T
	 * @throws XMLDocumentException Error montando el fichero XML
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 * @deprecated Usar {@link #signAttached(DeviceManager,String,IDocument,TSAData,XAdESAttachedSignatureOptions) signAttached}
	 */
	public static XAdESTSignature signAttached (DeviceManager manager, String alias, URL document, 
			String digitalSignatureAlgorithm, URL urlTSA, XAdESDataObjectFormat dof, String[] claimedRoles) 
					throws LoadingObjectException, SignatureException, NoDocumentToSignException, XMLDocumentException {
		
		logger.debug("[XAdESTSignature.signAttached]::Entrada::" + Arrays.asList(new Object[] { manager, alias, document, digitalSignatureAlgorithm, urlTSA, dof, claimedRoles }));
		
		//-- Obtener el document de Arangi y llamar a la firma attached
		URLDocument urlDocument;
		try {
			urlDocument = new URLDocument(document);
		} catch (InitDocumentException e) {
			logger.info("[XAdESTSignature.signAttached]::La URL a firmar no existe o es nula:: " + document);
			throw new NoDocumentToSignException("La URL a firmar no existe o es nula: " + document);
		}
		
		return signAttached(manager, alias, urlDocument, digitalSignatureAlgorithm, urlTSA, dof, claimedRoles);
		
	}
	
	/**
	 * Realiza una firma XAdES-T attached (el documento se incluye en la firma). 
	 * 
	 * @param manager Dispositivo criptográfico que realizará la firma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param document Fichero a firmar
	 * @param tsaData Información de acceso a una TSA
	 * @param options Opciones de la firma
	 * @return Firma XADES-T
	 * @throws XMLDocumentException Error montando el fichero XML
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 */
	public static XAdESTSignature signAttached (DeviceManager manager, String alias, IDocument document, 
			TSAData tsaData, XAdESAttachedSignatureOptions options) 
					throws XMLDocumentException, LoadingObjectException, SignatureException {
		
		logger.debug("[XAdESTSignature.signAttached]::Entrada::" + Arrays.asList(new Object[] { manager, alias, document, tsaData, options }));
		
		return (XAdESTSignature) signAttached(manager, alias, document, tsaData, null, options, EnumFormatoFirma.XAdES_T, XAdESTSignature.class);
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
	 * @throws LoadingObjectException No es posible obtener la clave privada o el
	 * 	certificado del alias
	 * @throws CounterSignatureException Errores durante el proceso de contrafirma
	 */
	public void counterSign (DeviceManager manager, String alias, URL urlTSA) throws LoadingObjectException, CounterSignatureException {
		counterSign(manager, alias, null, null, urlTSA);
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
	 * @throws LoadingObjectException No es posible obtener la clave privada o el
	 * 	certificado del alias
	 * @throws CounterSignatureException Errores durante el proceso de contrafirma
	 */
	public void counterSign (DeviceManager manager, String alias, Certificate signatureToCounterSignCertificate, 
			String digitalSignatureAlgorithm, URL urlTSA) throws LoadingObjectException, CounterSignatureException {
		
		logger.debug("[XAdESTSignature.counterSign]::Entrada::" + Arrays.asList(new Object[] { manager, alias, signatureToCounterSignCertificate, digitalSignatureAlgorithm, urlTSA }));
		
		//-- El documento a firmar es la firma en la que nos encontramos
        DataToSign dataToSign = new DataToSign();
        dataToSign.setXadesFormat(getXAdESSignatureFormat());
        dataToSign.setEsquema(DEFAULT_XADES_SCHEMA);
        dataToSign.setXMLEncoding(DEFAULT_XML_ENCODING);
        dataToSign.setEnveloped(true);
        dataToSign.setDocument(this.xadesDocument);
        
		Document doc;
		try {
			doc = ContraFirmaXML.counterSign(manager.getCertificate(alias), dataToSign, signatureToCounterSignCertificate!=null?signatureToCounterSignCertificate.toX509Certificate():null, 
					manager.getPrivateKey(alias), digitalSignatureAlgorithm, urlTSA, DEFAULT_XADES_SCHEMA_URI);
		} catch (LoadingObjectException e) {
			logger.info("[XAdESTSignature.counterSign]::No es posible obtener la clave privada del alias '" + alias + "'", e);
			throw e;
		} catch (SearchingException e) {
			logger.info("[XAdESTSignature.counterSign]::No es posible obtener el certificado del alias '" + alias + "'", e);
			throw new LoadingObjectException ("No es posible obtener el certificado del alias '" + alias + "'", e);
		} catch (CounterSignatureException e) {
			logger.info("[XAdESTSignature.counterSign]::No es posible realizar la contrafirma", e);
			throw e;
		}
		
		logger.debug("[XAdESTSignature.counterSign]::Se ha obtenido la contrafirma");
		this.xadesDocument = doc;
	}
	
	/**
	 * Añade una Cofirma a la firma XAdES-T. Realizará una firma de las mismas características que 
	 * la primera que encuentre (attached o dettached).<br><br>
	 * 
	 * Si la firma es dettached y la referencia al documento que hay en la firma no
	 * es una URL será necesario usar el método {@link #coSign(DeviceManager, String, IDocument, URL)}
	 * al que le proporcionaremos este documento.  
	 * 
	 * @param manager Dispositivo criptográfico que realizará la cofirma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param urlTSA URL del servidor de sello de tiempos para incluir en la contrafirma
	 * @throws SignatureNotFoundException No existe ninguna firma que cofirmar
	 * @throws NoDocumentToSignException El fichero a firmar no existe o es nulo
	 * @throws HashingException Error realizando el hash del documento
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No ha sido posible parsear la firma XAdES o no se puede realizar la cofirma
	 * @throws NoCoincidentDocumentException El documento que se quiere firmar no se corresponde con el de
	 * 	la firma XAdES  
	 */
	public void coSign (DeviceManager manager, String alias, URL urlTSA)  throws SignatureNotFoundException, 
		NoDocumentToSignException, HashingException, LoadingObjectException, SignatureException, NoCoincidentDocumentException {
		
		logger.debug("[XAdESTSignature.coSign]::Entrada::" + Arrays.asList(new Object[] { manager, alias }));
		coSign (manager, alias, null, null, urlTSA);
	}
	
	
	/**
	 * Añade una Cofirma a la firma XAdES-T. Realizará una firma de las mismas características que 
	 * la primera que encuentre (attached o dettached).<br><br>
	 * 
	 * Este método es útil si la firma es dettached i la referencia al documento que hay en la firma no
	 * es una URL.  
	 * 
	 * @param manager Dispositivo criptográfico que realizará la cofirma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param signedDoc contenido a firmar. El mismo utilizado en la generación de las otras firmas.
	 * @param digitalSignatureAlgorithm Algoritmo de firma (si nulo algoritmo por defecto)
	 * @param urlTSA URL del servidor de sello de tiempos para incluir en la contrafirma
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
			String digitalSignatureAlgorithm, URL urlTSA) throws SignatureNotFoundException, 
				NoDocumentToSignException, HashingException, LoadingObjectException, SignatureException, NoCoincidentDocumentException {
		
		coSign(manager, alias, signedDoc, digitalSignatureAlgorithm, urlTSA, null, EnumFormatoFirma.XAdES_T, XAdESTSignature.class);

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
	 * Añade un sello de tiempos a la firma siempre que sea XAdES-BES. La firma tiene que ser attached. 
	 * Si no es así es necesario
	 * usar {@link #completeToXAdEST (XAdESSignature, IDocument, CAList, URL)}.
	 * 
	 * @param xades firma XAdES
	 * @param caList Lista de certificados de CA
	 * @param urlTSA URL del servidor de sello de tiempos
	 * @return XAdES-T
	 * @throws HashingException Error obteniendo el hash del documento
	 * @throws SignatureException Error tratando el objeto firma o la firma XAdES-BES no es
	 * 	válida
	 * @throws XMLDocumentException Error completando el XML del XAdES-BES a XAdES-T
	 * @throws MalformedTimeStampException El sello de tiempos obtenido no está bien formado
	 * @throws ResponseTimeStampException No se ha podido obtener correctamente el sello de tiempos
	 */
	public static XAdESTSignature completeToXAdEST (XAdESSignature xades, CAList caList, URL urlTSA) throws SignatureException, MalformedTimeStampException, ResponseTimeStampException, HashingException, XMLDocumentException  {
		return completeToXAdEST (xades, null, caList, urlTSA);
	}
	
	/**
	 * Añade un sello de tiempos a la firma siempre que sea XAdES-BES
	 * 
	 * @param xades firma XAdES
	 * @param document documento firmado en el XAdES. Necesario en el caso que el XAdES sea detached.
	 * @param caList Lista de certificados de CA
	 * @param urlTSA URL del servidor de sello de tiempos
	 * @return XAdES-T
	 * @throws HashingException Error obteniendo el hash del documento
	 * @throws SignatureException Error tratando el objeto firma o la firma XAdES-BES no es
	 * 	válida
	 * @throws XMLDocumentException Error completando el XML del XAdES-BES a XAdES-T
	 * @throws MalformedTimeStampException El sello de tiempos obtenido no está bien formado
	 * @throws ResponseTimeStampException No se ha podido obtener correctamente el sello de tiempos
	 */
	public static XAdESTSignature completeToXAdEST (XAdESSignature xades, IDocument document, CAList caList, URL urlTSA) throws SignatureException, MalformedTimeStampException, ResponseTimeStampException, HashingException, XMLDocumentException  {
		return completeToXAdEST(xades, document, caList, null, urlTSA);
	}
	
	/**
	 * Añade un sello de tiempos a la firma siempre que sea XAdES-BES. La firma tiene que ser attached. 
	 * Si no es así es necesario
	 * usar {@link #completeToXAdEST (XAdESSignature, IDocument, CAList, URL)}.
	 * 
	 * @param xades firma XAdES
	 * @param validationServices Lista de servicios de validación
	 * @param urlTSA URL del servidor de sello de tiempos
	 * @return XAdES-T
	 * @throws HashingException Error obteniendo el hash del documento
	 * @throws SignatureException Error tratando el objeto firma o la firma XAdES-BES no es
	 * 	válida
	 * @throws XMLDocumentException Error completando el XML del XAdES-BES a XAdES-T
	 * @throws MalformedTimeStampException El sello de tiempos obtenido no está bien formado
	 * @throws ResponseTimeStampException No se ha podido obtener correctamente el sello de tiempos
	 */
	public static XAdESTSignature completeToXAdEST (XAdESSignature xades, List<CertificateValidationService> validationServices, URL urlTSA) throws SignatureException, MalformedTimeStampException, ResponseTimeStampException, HashingException, XMLDocumentException  {
		return completeToXAdEST (xades, null, validationServices, urlTSA);
	}
	
	/**
	 * Añade un sello de tiempos a la firma siempre que sea XAdES-BES
	 * 
	 * @param xades firma XAdES
	 * @param document documento firmado en el XAdES. Necesario en el caso que el XAdES sea detached.
	 * @param validationServices Lista de servicios de validación
	 * @param urlTSA URL del servidor de sello de tiempos
	 * @return XAdES-T
	 * @throws HashingException Error obteniendo el hash del documento
	 * @throws SignatureException Error tratando el objeto firma o la firma XAdES-BES no es
	 * 	válida
	 * @throws XMLDocumentException Error completando el XML del XAdES-BES a XAdES-T
	 * @throws MalformedTimeStampException El sello de tiempos obtenido no está bien formado
	 * @throws ResponseTimeStampException No se ha podido obtener correctamente el sello de tiempos
	 */
	public static XAdESTSignature completeToXAdEST (XAdESSignature xades, IDocument document, List<CertificateValidationService> validationServices, URL urlTSA) throws SignatureException, MalformedTimeStampException, ResponseTimeStampException, HashingException, XMLDocumentException  {
		return completeToXAdEST(xades, document, null, validationServices, urlTSA);
	}
	
	//-- Implementación de XAdESSignature
	
	@Override
	protected int tratarResultadoValidacion(ResultadoValidacion resultadoValidacion) {
		if (resultadoValidacion.isValidate()) {
			if (resultadoValidacion.getLog().equalsIgnoreCase("Se aconseja validar el estado del certificado firmante")) {
				//-- Comprobar la validez de los certificados
				X509Certificate x509Certificate = (X509Certificate) resultadoValidacion.getDatosFirma().getCadenaFirma().getCertificates().get(0);
				Certificate certificate = null;
				try {
					certificate = new Certificate (x509Certificate);
				} catch (NormalizeCertificateException e) {
					logger.debug("[XAdESTSignature.tratarResultadoValidacion]::El certificado no ha podido ser normalizado: \n" + x509Certificate, e);
				}
				if (certificate == null || !certificate.isActive()) {
					logger.debug("[XAdESTSignature.tratarResultadoValidacion]::El certificado está caducado");
					return ValidationResult.RESULT_CERTIFICATE_NOT_ACTIVE;
				}
				logger.debug("[XAdESTSignature.tratarResultadoValidacion]::La firma ha pasado la validación");
				return ValidationResult.RESULT_VALID;
			} else {
				//válido
				logger.debug("[XAdESTSignature.tratarResultadoValidacion]::La firma ha pasado la validación");
				return ValidationResult.RESULT_VALID;
			}
		} else if (resultadoValidacion.getNivelValido() == null || resultadoValidacion.getNivelValido().equals("")) {
			if (resultadoValidacion.getLog().toLowerCase().indexOf("firma inválida") > -1) {
				//certificado válido, el problema será con la firma
				logger.debug("[XAdESTSignature.tratarResultadoValidacion]::La firma no es válida");
				return ValidationResult.RESULT_SIGNATURE_NOT_MATCH_DATA;
			} else {
				//certificado caducado
				logger.debug("[XAdESTSignature.tratarResultadoValidacion]::El certificado está caducado");
				return ValidationResult.RESULT_CERTIFICATE_NOT_ACTIVE;
			}
		} else if (resultadoValidacion.getEnumNivel() == EnumFormatoFirma.XAdES_BES) {
			//El BES es válido, o sea que es un problema con el sello de tiempos
			logger.debug("[XAdESTSignature.tratarResultadoValidacion]::El sello de tiempos no es válido");
			return ValidationResult.RESULT_INVALID_TIMESTAMP;
		} else if (resultadoValidacion.getResultado() == ResultadoEnum.INVALID) {
			//revocado
			logger.debug("[XAdESTSignature.tratarResultadoValidacion]::El certificado de la firma está revocado");
			return ValidationResult.RESULT_CERTIFICATE_REVOKED;
		} else  {
			//desconocido
			logger.debug("[XAdESTSignature.tratarResultadoValidacion]::La firma no ha pasado la validación");
			return ValidationResult.RESULT_CERTIFICATE_UNKNOWN;
		}
	}
	
	/**
	 * Formato de firma: XAdES-T
	 */
	protected EnumFormatoFirma getXAdESSignatureFormat () {
		return EnumFormatoFirma.XAdES_T;
	}
	

	//-- Métodos privados

	/**
	 * Añade un sello de tiempos a la firma siempre que sea XAdES-BES
	 * 
	 * @param xades firma XAdES
	 * @param document documento firmado en el XAdES. Necesario en el caso que el XAdES sea detached.
	 * @param caList Lista de certificados de CA
	 * @param validationServices Servicios de validación
	 * @param urlTSA URL del servidor de sello de tiempos
	 * @return XAdES-T
	 * @throws HashingException Error obteniendo el hash del documento
	 * @throws SignatureException Error tratando el objeto firma o la firma XAdES-BES no es
	 * 	válida
	 * @throws XMLDocumentException Error completando el XML del XAdES-BES a XAdES-T
	 * @throws MalformedTimeStampException El sello de tiempos obtenido no está bien formado
	 * @throws ResponseTimeStampException No se ha podido obtener correctamente el sello de tiempos
	 */
	private static XAdESTSignature completeToXAdEST (XAdESSignature xades, IDocument document, 
			CAList caList, List<CertificateValidationService> validationServices, URL urlTSA) 
					throws SignatureException, MalformedTimeStampException, ResponseTimeStampException, HashingException, XMLDocumentException  {
		logger.debug("[XAdESTSignature.completeToXAdEST]::Entrada::" + Arrays.asList(new Object[] { xades, document, caList, validationServices, urlTSA }));
		
		Document doc = xades.getDOM();
		
		//-- Comprobar que el XAdES-BES es correcto
		logger.debug("[XAdESTSignature.completeToXAdEST]::Verificando que el XAdES-BES es correcto");
		ValidationResult[] resultadoValidacion = null;
		if (caList != null) {
			if (document == null) {
				resultadoValidacion = xades.isValid(caList);			
			} else {
				resultadoValidacion = xades.isValid(document, caList);	
			}
		} else {
			if (document == null) {
				resultadoValidacion = xades.isValid(validationServices);			
			} else {
				resultadoValidacion = xades.isValid(document, validationServices);	
			}
		}
		
		String[] tiposFirmas = new String[resultadoValidacion.length]; 
		for (int i = 0; i < resultadoValidacion.length; i++) {
			if (!resultadoValidacion[i].isValid()) {
				logger.info("[XAdESTSignature.completeToXAdEST]::El XAdES-BES no es válido: " + resultadoValidacion[i].getResultText());
				throw new SignatureException("El XAdES-BES no es válido: " + resultadoValidacion[i].getResultText());
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
			if (!tiposFirmas[i].equals(XAdESBESSignature.SIGNATURE_TYPE)) {
				logger.debug("[XAdESTSignature.completeToXAdEST]::La firma ya es T o XL, no es necesario completar");
				continue;
			}

			Node idAttribute = nodeFirma.getAttributes().getNamedItem(ConstantesXADES.ID);
			
			//-- Obtener el sello de tiempos
			TimeStamp timeStamp;
			try {
				byte[] byteSignature = UtilidadTratarNodo.obtenerByteNodo(nodeFirma, ConstantesXADES.SCHEMA_DSIG, ConstantesXADES.SIGNATURE_VALUE, CanonicalizationEnum.C14N_OMIT_COMMENTS, 5);
				timeStamp = TimeStamp.stampDocument(byteSignature, urlTSA);
			} catch (FirmaXMLError e) {
				logger.info ("[XAdESXLSignature.completeToXAdEST]::Error obteniendo el elemento a sellar", e);
				throw new SignatureException("Error obteniendo el elemento a sellar", e);
			} catch (TimeStampServerConnectionException e) {
				logger.info("[XAdESXLSignature.completeToXAdEST]::No ha sido posible conectarse al servidor de sello de tiempos", e);
				throw new SignatureException ("No ha sido posible conectarse al servidor de sello de tiempos", e);
			} 
			
			try {
				doc = addXadesT(nodeFirma, idAttribute.getNodeValue(), timeStamp.toDER(), searchXAdESNamespace (doc.getDocumentElement()));
				logger.debug("[XAdESXLSignature.completeToXAdEST]::Completado a XAdES-T");
			} catch (Exception e) {
				throw new XMLDocumentException(e);
			}

		}

		//-- Crear el XAdES-T
		try {
			return new XAdESTSignature (doc);
		} catch (DOMException e) {
			throw new XMLDocumentException(e);
		} catch (Exception e) {
			throw new XMLDocumentException(e);
		}
	}
	

}

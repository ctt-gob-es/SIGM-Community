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

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

import es.accv.arangi.base.certificate.Certificate;
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
import es.accv.arangi.base.mityc.ContraFirmaXML;
import es.accv.arangi.base.signature.util.XAdESAttachedNodeToSignObject;
import es.accv.arangi.base.signature.util.XAdESDataObjectFormat;
import es.accv.arangi.base.signature.util.XAdESAttachedSignatureOptions;
import es.accv.arangi.base.signature.util.XAdESDetachedSignatureOptions;
import es.accv.arangi.base.util.validation.ValidationResult;
import es.mityc.firmaJava.libreria.utilidades.UtilidadFicheros;
import es.mityc.firmaJava.libreria.xades.DataToSign;
import es.mityc.firmaJava.libreria.xades.EnumFormatoFirma;
import es.mityc.firmaJava.libreria.xades.ResultadoEnum;
import es.mityc.firmaJava.libreria.xades.ResultadoValidacion;

/**
 * Clase que maneja firmas en formato XAdES-BES de acuerdo al estándar 
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
 * URL url = new URL (...);<br><br>
 * 
 * //-- Genera una firma attached. El documento se guardará en la firma en base64<br>
 * XAdESBESSignature signature1 = XAdESBESSignature.signAttached(manager, alias, documentTexto);<br><br>
 * 
 * //-- Genera una firma detached que referencia al fichero en disco<br>
 * XAdESBESSignature signature2 = XAdESBESSignature.signDetached(manager, alias, file);<br><br>
 * 
 * //-- Genera una firma detached que referencia a "2011/04/29/certificados/CER-2584665.pdf"<br>
 * XAdESBESSignature signature3 = XAdESBESSignature.signDetached(manager, alias, file, "2011/04/29/certificados/CER-2584665.pdf");<br><br>
 * 
 * //-- Genera una firma detached que referencia al fichero ubicado en la URL<br>
 * XAdESBESSignature signature4 = XAdESBESSignature.signDetached(manager, alias, url);<br><br>
 * 
 * //-- Genera una firma attached dentro del propio documento<br>
 * XAdESBESSignature signature5 = XAdESBESSignature.signAttached(manager, alias, documentoXML, "titulo", "documento");<br><br>
 * </code>
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José M Gutiérrez</a>
 */
public class XAdESBESSignature extends XAdESSignature {
	
	/*
	 * Logger de la clase
	 */
	static Logger logger = Logger.getLogger(XAdESBESSignature.class);
	
	/**
	 * Tipo de la firma
	 */
	public static final String SIGNATURE_TYPE	= "XAdES-BES";
	
	/*
	 * Nodos de firma 
	 */
	protected NodeList signatureNodes;
	

	//-- Constructores
	
	/**
	 * Construye el objeto en base a un XML que tiene el formato
	 * XAdES-BES
	 * 
	 * @param xmlDocument Documento XML
	 */
	public XAdESBESSignature(Document xmlDocument) {
		//TODO Validar que el XML es un XAdES
		initialize(xmlDocument);
	}
	
	/**
	 * Construye el objeto en base a un fichero XAdES-BES
	 * 
	 * @param xmlFile Fichero XAdES-BES
	 * @throws FileNotFoundException El fichero no existe
	 * @throws XMLDocumentException El fichero no parece un XML válido
	 */
	public XAdESBESSignature(File xmlFile) throws FileNotFoundException, XMLDocumentException {
		initialize(xmlFile);
	}

	/**
	 * Construye el objeto en base a un array de bytes.
	 * 
	 * @param signature Firma XAdES-BES
	 * @throws XMLDocumentException El fichero no parece un XML válido
	 */
	public XAdESBESSignature(byte[] signature) throws XMLDocumentException {
		initialize(signature);
	}

	/**
	 * Construye el objeto en base a un stream de lectura.
	 * 
	 * @param isSignature Stream de lectura a una firma XAdES-BES
	 * @throws XMLDocumentException El fichero no parece un XML válido
	 */
	public XAdESBESSignature(InputStream isSignature) throws XMLDocumentException {
		initialize(isSignature);
	}

	/**
	 * Construye el objeto en base a otro XAdES-BES
	 * 
	 * @param besSignature Firma XAdES-BES
	 */
	protected XAdESBESSignature(XAdESBESSignature besSignature) {
		xadesDocument = besSignature.xadesDocument;
	}
	
	//-- Métodos públicos
	
	/**
	 * Realiza una firma XAdES-BES detached (el fichero no se incluirá en la firma). No completa los campos 
	 * no obligatorios del tag 'SignedSignatureProperties':'signaturePolicyIdentifier', 
	 * 'signatureProductionPlace' y 'signerRole'.<br><br>
	 * 
	 * La referencia a la que apuntará la firma será el path del fichero.
	 * 
	 * @param manager Dispositivo criptográfico que realizará la firma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param document Fichero a firmar
	 * @return Firma XADES-BES
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 * @throws NoDocumentToSignException El fichero a firmar no existe o es nulo
	 * @deprecated Usar {@link #signDetached(DeviceManager,String,IDocument,String,XAdESDetachedSignatureOptions) signDetached}
	 */
	public static XAdESBESSignature signDetached (DeviceManager manager, String alias, File document) throws LoadingObjectException, SignatureException, NoDocumentToSignException {
		return signDetached(manager, alias, document, null, null, null);
		
	}
	
	/**
	 * Realiza una firma XAdES-BES detached (el fichero no se incluirá en la firma). No completa los campos 
	 * no obligatorios del tag 'SignedSignatureProperties':'signaturePolicyIdentifier' y 
	 * 'signatureProductionPlace'.<br><br>
	 * 
	 * La referencia a la que apuntará la firma será el path del fichero.
	 * 
	 * @param manager Dispositivo criptográfico que realizará la firma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param document Fichero a firmar
	 * @param digitalSignatureAlgorithm Algoritmo de firma (si nulo algoritmo por defecto)
	 * @param dof Información para construir el tag DataObjectFormat (puede ser null)
	 * @param claimedRoles Roles de la firma (puede ser null)
	 * @return Firma XADES-BES
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 * @throws NoDocumentToSignException El fichero a firmar no existe o es nulo
	 * @deprecated Usar {@link #signDetached(DeviceManager,String,IDocument,String,XAdESDetachedSignatureOptions) signDetached}
	 */
	public static XAdESBESSignature signDetached (DeviceManager manager, String alias, File document, 
			String digitalSignatureAlgorithm, XAdESDataObjectFormat dof, String[] claimedRoles) 
					throws LoadingObjectException, SignatureException, NoDocumentToSignException {
		
		logger.debug("[XAdESBESSignature.signDetached]::Entrada::" + Arrays.asList(new Object[] { manager, alias, document, digitalSignatureAlgorithm, dof, claimedRoles }));
		
		//-- Obtener el document de Arangi y llamar a la firma attached
		FileDocument fileDocument;
		try {
			fileDocument = new FileDocument(document);
		} catch (InitDocumentException e) {
			logger.info("[XAdESBESSignature.signAttached]::El fichero a firmar no existe o es nulo:: " + document);
			throw new NoDocumentToSignException("El fichero a firmar no existe o es nulo: " + document);
		}
		
		//-- Obtener la referencia al fichero
		String reference = UtilidadFicheros.relativizeRute("#", document);
		
		return signDetached(manager, alias, fileDocument, digitalSignatureAlgorithm, reference, dof, claimedRoles);
		
	}
	
	/**
	 * Realiza una firma XAdES-BES detached (el fichero no se incluirá en la firma). No completa los campos 
	 * no obligatorios del tag 'SignedSignatureProperties':'signaturePolicyIdentifier', 
	 * 'signatureProductionPlace' y 'signerRole'.<br><br>
	 * 
	 * La referencia a la que apuntará la firma será la URL del documento.
	 * 
	 * @param manager Dispositivo criptográfico que realizará la firma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param urlDocument Documento a firmar. Se encuentra en una URL accesible.
	 * @return Firma XADES-BES
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 * @throws NoDocumentToSignException La URL es nula o no existe
	 * @deprecated Usar {@link #signDetached(DeviceManager,String,IDocument,String,XAdESDetachedSignatureOptions) signDetached}
	 */
	public static XAdESBESSignature signDetached (DeviceManager manager, String alias, URL urlDocument) throws LoadingObjectException, SignatureException, NoDocumentToSignException {
		return signDetached(manager, alias, urlDocument, null, null, null);
	}
	
	/**
	 * Realiza una firma XAdES-BES detached (el fichero no se incluirá en la firma). No completa los campos 
	 * no obligatorios del tag 'SignedSignatureProperties':'signaturePolicyIdentifier' y 
	 * 'signatureProductionPlace'.<br><br>
	 * 
	 * La referencia a la que apuntará la firma será la URL del documento.
	 * 
	 * @param manager Dispositivo criptográfico que realizará la firma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param urlDocument Documento a firmar. Se encuentra en una URL accesible.
	 * @param digitalSignatureAlgorithm Algoritmo de firma (si nulo algoritmo por defecto)
	 * @param dof Información para construir el tag DataObjectFormat (puede ser null)
	 * @param claimedRoles Roles de la firma (puede ser null)
	 * @return Firma XADES-BES
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 * @throws NoDocumentToSignException La URL es nula o no existe
	 * @deprecated Usar {@link #signDetached(DeviceManager,String,IDocument,String,XAdESDetachedSignatureOptions) signDetached}
	 */
	public static XAdESBESSignature signDetached (DeviceManager manager, String alias, URL urlDocument, 
			String digitalSignatureAlgorithm, XAdESDataObjectFormat dof, String[] claimedRoles) 
					throws LoadingObjectException, SignatureException, NoDocumentToSignException {
		
		logger.debug("[XAdESBESSignature.signDetached]::Entrada::" + Arrays.asList(new Object[] { manager, alias, urlDocument, digitalSignatureAlgorithm, dof, claimedRoles }));
		
		//-- Obtener el document de Arangi y llamar a la firma attached
		URLDocument document;
		try {
			document = new URLDocument(urlDocument);
		} catch (InitDocumentException e) {
			logger.info("[XAdESBESSignature.signAttached]::La URL a firmar no existe o es nula:: " + urlDocument);
			throw new NoDocumentToSignException("La URL a firmar no existe o es nula: " + urlDocument);
		}
		
		//-- Obtener la referencia al fichero
		String reference = urlDocument.toString();
		
		return signDetached(manager, alias, document, digitalSignatureAlgorithm, reference, dof, claimedRoles);
	}
	
	/**
	 * Realiza una firma XAdES-BES detached (el fichero no se incluirá en la firma). No completa los campos 
	 * no obligatorios del tag 'SignedSignatureProperties':'signaturePolicyIdentifier', 
	 * 'signatureProductionPlace' y 'signerRole'.<br><br>
	 * 
	 * @param manager Dispositivo criptográfico que realizará la firma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param document Contenido a firmar
	 * @param reference Referencia a la que apuntará la firma
	 * @return Firma XADES-BES
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 * @deprecated Usar {@link #signDetached(DeviceManager,String,IDocument,String,XAdESDetachedSignatureOptions) signDetached}
	 */
	public static XAdESBESSignature signDetached (DeviceManager manager, String alias, IDocument document, String reference) throws LoadingObjectException, SignatureException {
		return signDetached(manager, alias, document, null, reference, null, null);
		
	}
	
	/**
	 * Realiza una firma XAdES-BES detached (el fichero no se incluirá en la firma). No completa los campos 
	 * no obligatorios del tag 'SignedSignatureProperties':'signaturePolicyIdentifier' y
	 * 'signatureProductionPlace'.<br><br>
	 * 
	 * @param manager Dispositivo criptográfico que realizará la firma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param document Contenido a firmar
	 * @param digitalSignatureAlgorithm Algoritmo de firma (si nulo algoritmo por defecto)
	 * @param reference Referencia a la que apuntará la firma
	 * @param dof Información para construir el tag DataObjectFormat (puede ser null)
	 * @param claimedRoles Roles de la firma (puede ser null)
	 * @return Firma XADES-BES
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 * @deprecated Usar {@link #signDetached(DeviceManager,String,IDocument,String,XAdESDetachedSignatureOptions) signDetached}
	 */
	public static XAdESBESSignature signDetached (DeviceManager manager, String alias, IDocument document, 
			String digitalSignatureAlgorithm, String reference, XAdESDataObjectFormat dof, String[] claimedRoles) 
					throws LoadingObjectException, SignatureException {
		
		logger.debug("[XAdESBESSignature.signDetached]::Entrada::" + Arrays.asList(new Object[] { manager, alias, document, digitalSignatureAlgorithm, reference, dof, claimedRoles }));
		
		//-- Si la referencia es nula se asigna una por defecto
		if (reference == null) {
			logger.debug("[XAdESBESSignature.signDetached]::Referencia nula, se asigna como #no-reference");
			reference = "#no-reference";
		}
		
		XAdESDetachedSignatureOptions options = new XAdESDetachedSignatureOptions(digitalSignatureAlgorithm, null, dof, claimedRoles, null, null);
		return signDetached(manager, alias, document, reference, options);
		
	}
	
	/**
	 * Realiza una firma XAdES-BES detached (el fichero no se incluirá en la firma). 
	 * 
	 * @param manager Dispositivo criptográfico que realizará la firma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param document Contenido a firmar
	 * @param reference Referencia a la que apuntará la firma
	 * @param options Opciones de la firma
	 * @return Firma XADES-BES
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 */
	public static XAdESBESSignature signDetached (DeviceManager manager, String alias, IDocument document, 
			String reference, XAdESDetachedSignatureOptions options) 
					throws LoadingObjectException, SignatureException {
		
		logger.debug("[XAdESBESSignature.signDetached]::Entrada::" + Arrays.asList(new Object[] { manager, alias, document, reference, options }));
		
		//-- Si la referencia es nula se asigna una por defecto
		if (reference == null) {
			logger.debug("[XAdESBESSignature.signDetached]::Referencia nula, se asigna como #no-reference");
			reference = "#no-reference";
		}
		
		return (XAdESBESSignature) signDetached(manager, alias, document, reference, null, null, options, EnumFormatoFirma.XAdES_BES, XAdESBESSignature.class);
		
	}
	
	/**
	 * Realiza una firma XAdES-BES attached (el documento se incluye en la firma). No completa los campos no 
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
	 * @return Firma XADES-BES
	 * @throws XMLDocumentException Error montando el fichero XML
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 * @deprecated Usar {@link #signAttached(DeviceManager,String,IDocument,XAdESAttachedSignatureOptions) signAttached}
	 */
	public static XAdESBESSignature signAttached (DeviceManager manager, String alias, IDocument document, String idToSign,
			String signatureParent) throws XMLDocumentException, LoadingObjectException, SignatureException  {
		return signAttached(manager, alias, document, null, idToSign, signatureParent, null, null);
	}
	
	/**
	 * Realiza una firma XAdES-BES attached (el documento se incluye en la firma). No completa los campos no 
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
	 * @param dof Información para construir el tag DataObjectFormat (puede ser null)
	 * @param claimedRoles Roles de la firma (puede ser null)
	 * @return Firma XADES-BES
	 * @throws XMLDocumentException Error montando el fichero XML
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 * @deprecated Usar {@link #signAttached(DeviceManager,String,IDocument,XAdESAttachedSignatureOptions) signAttached}
	 */
	public static XAdESBESSignature signAttached (DeviceManager manager, String alias, IDocument document, 
			String digitalSignatureAlgorithm, String idToSign, String signatureParent, 
			XAdESDataObjectFormat dof, String[] claimedRoles) 
					throws XMLDocumentException, LoadingObjectException, SignatureException  {
		
		logger.debug("[XAdESBESSignature.signAttached]::Entrada::" + Arrays.asList(new Object[] { manager, alias, document, digitalSignatureAlgorithm, idToSign, signatureParent, dof, claimedRoles }));
		
		XAdESAttachedNodeToSignObject nodeToSign = null;
		if (idToSign != null) {
			nodeToSign = new XAdESAttachedNodeToSignObject(idToSign);
		}
		XAdESAttachedSignatureOptions options = new XAdESAttachedSignatureOptions(digitalSignatureAlgorithm, null, dof, claimedRoles, null, null, nodeToSign);
		return signAttached(manager, alias, document, options);
	}
	
	/**
	 * Realiza una firma XAdES-BES attached (el documento se incluye en la firma). No completa los campos no 
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
	 * @return Firma XADES-BES
	 * @throws XMLDocumentException Error montando el fichero XML
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 * @deprecated Usar {@link #signAttached(DeviceManager,String,IDocument,XAdESAttachedSignatureOptions) signAttached}
	 */
	public static XAdESBESSignature signAttached (DeviceManager manager, String alias, IDocument document) throws XMLDocumentException, LoadingObjectException, SignatureException  {
		return signAttached(manager, alias, document, (String)null, null);
	}
	
	/**
	 * Realiza una firma XAdES-BES attached (el documento se incluye en la firma). No completa los campos no 
	 * obligatorios del tag 'SignedSignatureProperties':'signaturePolicyIdentifier' y 'signatureProductionPlace' 
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
	 * @param dof Información para construir el tag DataObjectFormat (puede ser null)
	 * @param claimedRoles Roles de la firma (puede ser null)
	 * @return Firma XADES-BES
	 * @throws XMLDocumentException Error montando el fichero XML
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 * @deprecated Usar {@link #signAttached(DeviceManager,String,IDocument,XAdESAttachedSignatureOptions) signAttached}
	 */
	public static XAdESBESSignature signAttached (DeviceManager manager, String alias, IDocument document, 
			String digitalSignatureAlgorithm, XAdESDataObjectFormat dof, String[] claimedRoles) 
					throws XMLDocumentException, LoadingObjectException, SignatureException  {
		
		logger.debug("[XAdESBESSignature.signAttached]::Entrada::" + Arrays.asList(new Object[] { manager, alias, document, dof, claimedRoles }));
	
		XAdESAttachedSignatureOptions options = new XAdESAttachedSignatureOptions(digitalSignatureAlgorithm, null, dof, claimedRoles, null, null, null);
		return signAttached(manager, alias, document, options);
	}
	
	/**
	 * Realiza una firma XAdES-BES attached (el fichero se incluirá en la firma). No completa los campos 
	 * no obligatorios del tag 'SignedSignatureProperties':'signaturePolicyIdentifier', 
	 * 'signatureProductionPlace' y 'signerRole'.<br><br>
	 * 
	 * @param manager Dispositivo criptográfico que realizará la firma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param document Fichero a firmar
	 * @return Firma XADES-BES
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 * @throws NoDocumentToSignException El fichero a firmar no existe o es nulo
	 * @throws XMLDocumentException Error montando el fichero XML
	 * @deprecated Usar {@link #signAttached(DeviceManager,String,IDocument,XAdESAttachedSignatureOptions) signAttached}
	 */
	public static XAdESBESSignature signAttached (DeviceManager manager, String alias, File document) throws LoadingObjectException, SignatureException, NoDocumentToSignException, XMLDocumentException {
		return signAttached(manager, alias, document, null, null, null);
	}
	
	/**
	 * Realiza una firma XAdES-BES attached (el fichero se incluirá en la firma). No completa los campos 
	 * no obligatorios del tag 'SignedSignatureProperties':'signaturePolicyIdentifier' y 
	 * 'signatureProductionPlace'.<br><br>
	 * 
	 * @param manager Dispositivo criptográfico que realizará la firma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param document Fichero a firmar
	 * @param digitalSignatureAlgorithm Algoritmo de firma (si nulo algoritmo por defecto)
	 * @param dof Información para construir el tag DataObjectFormat (puede ser null)
	 * @param claimedRoles Roles de la firma (puede ser null)
	 * @return Firma XADES-BES
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 * @throws NoDocumentToSignException El fichero a firmar no existe o es nulo
	 * @throws XMLDocumentException Error montando el fichero XML
	 * @deprecated Usar {@link #signAttached(DeviceManager,String,IDocument,XAdESAttachedSignatureOptions) signAttached}
	 */
	public static XAdESBESSignature signAttached (DeviceManager manager, String alias, File document, 
			String digitalSignatureAlgorithm, XAdESDataObjectFormat dof, String[] claimedRoles) throws LoadingObjectException, SignatureException, NoDocumentToSignException, XMLDocumentException {
		
		logger.debug("[XAdESBESSignature.signAttached]::Entrada::" + Arrays.asList(new Object[] { manager, alias, document, digitalSignatureAlgorithm, dof, claimedRoles }));
		
		//-- Obtener el document de Arangi y llamar a la firma attached
		FileDocument fileDocument;
		try {
			fileDocument = new FileDocument(document);
		} catch (InitDocumentException e) {
			logger.info("[XAdESBESSignature.signAttached]::El fichero a firmar no existe o es nulo:: " + document);
			throw new NoDocumentToSignException("El fichero a firmar no existe o es nulo: " + document);
		}
		
		return signAttached(manager, alias, fileDocument, digitalSignatureAlgorithm, dof, claimedRoles);
		
	}
	
	/**
	 * Realiza una firma XAdES-BES attached (el fichero se incluirá en la firma). No completa los campos 
	 * no obligatorios del tag 'SignedSignatureProperties':'signaturePolicyIdentifier', 
	 * 'signatureProductionPlace' y 'signerRole'.<br><br>
	 * 
	 * @param manager Dispositivo criptográfico que realizará la firma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param document Documento a firmar. Se encuentra en una URL accesible.
	 * @return Firma XADES-BES
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 * @throws NoDocumentToSignException El fichero a firmar no existe o es nulo
	 * @throws XMLDocumentException Error montando el fichero XML
	 * @deprecated Usar {@link #signAttached(DeviceManager,String,IDocument,XAdESAttachedSignatureOptions) signAttached}
	 */
	public static XAdESBESSignature signAttached (DeviceManager manager, String alias, URL document) throws LoadingObjectException, SignatureException, NoDocumentToSignException, XMLDocumentException {
		return signAttached(manager, alias, document, null, null, null);
		
	}
	
	/**
	 * Realiza una firma XAdES-BES attached (el fichero se incluirá en la firma). No completa los campos 
	 * no obligatorios del tag 'SignedSignatureProperties':'signaturePolicyIdentifier' y 
	 * 'signatureProductionPlace'.<br><br>
	 * 
	 * @param manager Dispositivo criptográfico que realizará la firma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param document Documento a firmar. Se encuentra en una URL accesible.
	 * @param digitalSignatureAlgorithm Algoritmo de firma (si nulo algoritmo por defecto)
	 * @param dof Información para construir el tag DataObjectFormat (puede ser null)
	 * @param claimedRoles Roles de la firma (puede ser null)
	 * @return Firma XADES-BES
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 * @throws NoDocumentToSignException El fichero a firmar no existe o es nulo
	 * @throws XMLDocumentException Error montando el fichero XML
	 * @deprecated Usar {@link #signAttached(DeviceManager,String,IDocument,XAdESAttachedSignatureOptions) signAttached}
	 */
	public static XAdESBESSignature signAttached (DeviceManager manager, String alias, URL document, 
			String digitalSignatureAlgorithm, XAdESDataObjectFormat dof, String[] claimedRoles) 
					throws LoadingObjectException, SignatureException, NoDocumentToSignException, XMLDocumentException {
		
		logger.debug("[XAdESBESSignature.signAttached]::Entrada::" + Arrays.asList(new Object[] { manager, alias, document, digitalSignatureAlgorithm, dof, claimedRoles }));
		
		//-- Obtener el document de Arangi y llamar a la firma attached
		URLDocument urlDocument;
		try {
			urlDocument = new URLDocument(document);
		} catch (InitDocumentException e) {
			logger.info("[XAdESBESSignature.signAttached]::La URL a firmar no existe o es nula:: " + document);
			throw new NoDocumentToSignException("La URL a firmar no existe o es nula: " + document);
		}
		
		return signAttached(manager, alias, urlDocument, digitalSignatureAlgorithm, dof, claimedRoles);
		
	}
	
	/**
	 * Realiza una firma XAdES-BES attached (el documento se incluye en la firma).
	 * 
	 * @param manager Dispositivo criptográfico que realizará la firma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param document Documento a firmar
	 * @param options Opciones para la firma
	 * @return Firma XADES-BES
	 * @throws XMLDocumentException Error montando el fichero XML
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 */
	public static XAdESBESSignature signAttached (DeviceManager manager, String alias, IDocument document, 
			XAdESAttachedSignatureOptions options) throws XMLDocumentException, LoadingObjectException, SignatureException  {
		
		logger.debug("[XAdESBESSignature.signAttached]::Entrada::" + Arrays.asList(new Object[] { manager, alias, document, options }));
	
		return (XAdESBESSignature) signAttached(manager, alias, document, null, null, options, EnumFormatoFirma.XAdES_BES, XAdESBESSignature.class);
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
	 * @throws LoadingObjectException No es posible obtener la clave privada o el
	 * 	certificado del alias
	 * @throws CounterSignatureException Errores durante el proceso de contrafirma
	 */
	public void counterSign (DeviceManager manager, String alias) throws LoadingObjectException, CounterSignatureException {
		counterSign(manager, alias, null, null);
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
	 * @throws LoadingObjectException No es posible obtener la clave privada o el
	 * 	certificado del alias
	 * @throws CounterSignatureException Errores durante el proceso de contrafirma
	 */
	public void counterSign (DeviceManager manager, String alias, Certificate signatureToCounterSignCertificate,
			String digitalSignatureAlgorithm) throws LoadingObjectException, CounterSignatureException {
		
		logger.debug("[XAdESBESSignature.counterSign]::Entrada::" + Arrays.asList(new Object[] { manager, alias }));
		
		//-- El documento a firmar es la firma en la que nos encontramos
        DataToSign dataToSign = new DataToSign();
        dataToSign.setXadesFormat(getXAdESSignatureFormat());
        dataToSign.setEsquema(DEFAULT_XADES_SCHEMA);
        dataToSign.setXMLEncoding(DEFAULT_XML_ENCODING);
        dataToSign.setEnveloped(true);
        dataToSign.setDocument(this.xadesDocument);
        
		Document doc;
		try {
			doc = ContraFirmaXML.counterSign(manager.getCertificate(alias), dataToSign, 
					signatureToCounterSignCertificate!=null?signatureToCounterSignCertificate.toX509Certificate():null, 
					manager.getPrivateKey(alias), digitalSignatureAlgorithm, null, DEFAULT_XADES_SCHEMA_URI);
		} catch (LoadingObjectException e) {
			logger.info("[XAdESBESSignature.counterSign]::No es posible obtener la clave privada del alias '" + alias + "'", e);
			throw e;
		} catch (SearchingException e) {
			logger.info("[XAdESBESSignature.counterSign]::No es posible obtener el certificado del alias '" + alias + "'", e);
			throw new LoadingObjectException ("No es posible obtener el certificado del alias '" + alias + "'", e);
		} catch (CounterSignatureException e) {
			logger.info("[XAdESBESSignature.counterSign]::No es posible realizar la contrafirma", e);
			throw e;
		}
		
		logger.debug("[XAdESBESSignature.counterSign]::Se ha obtenido la contrafirma");
		this.xadesDocument = doc;
	}
	
	/**
	 * Añade una Cofirma a la firma XAdES-BES. Realizará una firma de las mismas características que 
	 * la primera que encuentre (attached o dettached).<br><br>
	 * 
	 * Si la firma es dettached i la referencia al documento que hay en la firma 
	 * no es una URL será necesario usar el método {@link #coSign(DeviceManager, String, IDocument)}
	 * al que le proporcionaremos este documento.  
	 * 
	 * @param manager Dispositivo criptográfico que realizará la cofirma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @throws SignatureNotFoundException No existe ninguna firma que cofirmar
	 * @throws NoDocumentToSignException El fichero a firmar no existe o es nulo
	 * @throws HashingException Error realizando el hash del documento
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No ha sido posible parsear la firma XAdES o no se puede realizar la cofirma
	 * @throws NoCoincidentDocumentException El documento que se quiere firmar no se corresponde con el de
	 * 	la firma XAdES  
	 */
	public void coSign (DeviceManager manager, String alias) throws SignatureNotFoundException, NoDocumentToSignException, HashingException, LoadingObjectException, SignatureException, NoCoincidentDocumentException{
		
		logger.debug("[XAdESBESSignature.coSign]::Entrada::" + Arrays.asList(new Object[] { manager, alias }));
		coSign (manager, alias, null, null);
	}
	
	
	/**
	 * Añade una Cofirma a la firma XAdES-BES. Realizará una firma de las mismas características que 
	 * la primera que encuentre (attached o dettached).<br><br>
	 * 
	 * Este método es útil si la firma es dettached i la referencia al documento que hay en la firma no
	 * es una URL.  
	 * 
	 * @param manager Dispositivo criptográfico que realizará la cofirma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param signedDoc contenido a firmar. El mismo utilizado en la generación de las otras firmas.
	 * @param digitalSignatureAlgorithm Algoritmo de firma (si nulo algoritmo por defecto)
	 * @throws SignatureNotFoundException No existe ninguna firma que cofirmar
	 * @throws NoDocumentToSignException El fichero a firmar no existe o es nulo
	 * @throws HashingException Error realizando el hash del documento
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No ha sido posible parsear la firma XAdES o no se puede realizar la cofirma
	 * @throws NoCoincidentDocumentException El documento que se quiere firmar no se corresponde con el de
	 * 	la firma XAdES  
	 */
	public void coSign (DeviceManager manager, String alias, IDocument signedDoc, String digitalSignatureAlgorithm) 
			throws SignatureNotFoundException, NoDocumentToSignException, HashingException, 
				LoadingObjectException, SignatureException, NoCoincidentDocumentException {
		
		coSign(manager, alias, signedDoc, digitalSignatureAlgorithm, null, null, EnumFormatoFirma.XAdES_BES, XAdESBESSignature.class);
		
	}
	
	/**
	 * Devuelve una cadena de texto con el tipo de la firma
	 * 
	 * @return Cadena de texto con el tipo de la firma
	 */
	public String getSignatureType () {
		return SIGNATURE_TYPE;
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
					logger.debug("[XAdESBESSignature.tratarResultadoValidacion]::El certificado no ha podido ser normalizado: \n" + x509Certificate, e);
				}
				if (certificate == null || !certificate.isActive()) {
					logger.debug("[XAdESBESSignature.tratarResultadoValidacion]::El certificado está caducado");
					return ValidationResult.RESULT_CERTIFICATE_NOT_ACTIVE;
				}
				logger.debug("[XAdESBESSignature.tratarResultadoValidacion]::La firma ha pasado la validación");
				return ValidationResult.RESULT_VALID;
			} else {
				//válido
				logger.debug("[XAdESBESSignature.tratarResultadoValidacion]::La firma ha pasado la validación");
				return ValidationResult.RESULT_VALID;
			}
		} else if (resultadoValidacion.getNivelValido() == null || resultadoValidacion.getNivelValido().equals("")) {
			if (resultadoValidacion.getLog().toLowerCase().indexOf("firma inválida") > -1) {
				//certificado válido, el problema será con la firma
				logger.debug("[XAdESBESSignature.tratarResultadoValidacion]::La firma no es válida");
				return ValidationResult.RESULT_SIGNATURE_NOT_MATCH_DATA;
			} else {
				//certificado caducado
				logger.debug("[XAdESBESSignature.tratarResultadoValidacion]::El certificado está caducado");
				return ValidationResult.RESULT_CERTIFICATE_NOT_ACTIVE;
			}
		} else if (resultadoValidacion.getResultado() == ResultadoEnum.INVALID) {
			if (resultadoValidacion.getLog() != null && resultadoValidacion.getLog().indexOf("El certificado firmante ha caducado") > -1) {
				//certificado caducado
				logger.debug("[XAdESBESSignature.tratarResultadoValidacion]::El certificado está caducado");
				return ValidationResult.RESULT_CERTIFICATE_NOT_ACTIVE;
			} else {
				//revocado
				logger.debug("[XAdESBESSignature.tratarResultadoValidacion]::El certificado de la firma está revocado");
				return ValidationResult.RESULT_CERTIFICATE_REVOKED;
			}
		} else  {
			//desconocido
			logger.debug("[XAdESBESSignature.tratarResultadoValidacion]::La firma no ha pasado la validación");
			return ValidationResult.RESULT_CERTIFICATE_UNKNOWN;
		}
	}
	
	/**
	 * Formato de firma: XAdES-BES
	 */
	protected EnumFormatoFirma getXAdESSignatureFormat () {
		return EnumFormatoFirma.XAdES_BES;
	}
	
	//-- Métodos privados


}

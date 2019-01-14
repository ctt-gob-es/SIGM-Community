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
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.lang.reflect.Constructor;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.MessageDigest;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.apache.xml.security.exceptions.XMLSecurityException;
import org.apache.xml.security.signature.Reference;
import org.apache.xml.security.signature.SignedInfo;
import org.apache.xml.security.signature.XMLSignature;
import org.apache.xml.security.transforms.Transforms;
import org.bouncycastle.cert.ocsp.OCSPResp;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import es.accv.arangi.base.algorithm.HashingAlgorithm;
import es.accv.arangi.base.certificate.Certificate;
import es.accv.arangi.base.certificate.validation.CAList;
import es.accv.arangi.base.certificate.validation.OCSPResponse;
import es.accv.arangi.base.certificate.validation.service.CertificateValidationService;
import es.accv.arangi.base.device.DeviceManager;
import es.accv.arangi.base.document.FileDocument;
import es.accv.arangi.base.document.IDocument;
import es.accv.arangi.base.document.InputStreamDocument;
import es.accv.arangi.base.document.URLDocument;
import es.accv.arangi.base.exception.certificate.validation.MalformedOCSPResponseException;
import es.accv.arangi.base.exception.device.LoadingObjectException;
import es.accv.arangi.base.exception.device.SearchingException;
import es.accv.arangi.base.exception.document.HashingException;
import es.accv.arangi.base.exception.document.InitDocumentException;
import es.accv.arangi.base.exception.signature.AlgorithmNotSuitableException;
import es.accv.arangi.base.exception.signature.NoCoincidentDocumentException;
import es.accv.arangi.base.exception.signature.NoDocumentToSignException;
import es.accv.arangi.base.exception.signature.SignatureException;
import es.accv.arangi.base.exception.signature.SignatureNotFoundException;
import es.accv.arangi.base.exception.signature.XMLDocumentException;
import es.accv.arangi.base.exception.timestamp.MalformedTimeStampException;
import es.accv.arangi.base.mityc.ArangiDocumentPrivateData;
import es.accv.arangi.base.mityc.CAListCertStatusRecover;
import es.accv.arangi.base.mityc.FileResourceData;
import es.accv.arangi.base.mityc.ToxicResourceData;
import es.accv.arangi.base.mityc.URLResourceData;
import es.accv.arangi.base.mityc.UnknownFileResourceData;
import es.accv.arangi.base.mityc.ValidationServicesCertStatusRecover;
import es.accv.arangi.base.mityc.XAdESUtil;
import es.accv.arangi.base.signature.util.ArangiXAdESPolicyIdentifier;
import es.accv.arangi.base.signature.util.ArangiXAdESProductionPlace;
import es.accv.arangi.base.signature.util.ObjectIdentifier;
import es.accv.arangi.base.signature.util.TSAData;
import es.accv.arangi.base.signature.util.XAdESAttachedNodeToSign;
import es.accv.arangi.base.signature.util.XAdESAttachedNodeToSignEnveloped;
import es.accv.arangi.base.signature.util.XAdESAttachedNodeToSignObject;
import es.accv.arangi.base.signature.util.XAdESAttachedSignatureOptions;
import es.accv.arangi.base.signature.util.XAdESDataObjectFormat;
import es.accv.arangi.base.signature.util.XAdESDetachedSignatureOptions;
import es.accv.arangi.base.timestamp.TimeStamp;
import es.accv.arangi.base.util.Util;
import es.accv.arangi.base.util.validation.ValidationResult;
import es.mityc.firmaJava.libreria.ConstantesXADES;
import es.mityc.firmaJava.libreria.utilidades.Base64Coder;
import es.mityc.firmaJava.libreria.utilidades.NombreNodo;
import es.mityc.firmaJava.libreria.utilidades.UtilidadFechas;
import es.mityc.firmaJava.libreria.utilidades.UtilidadFirmaElectronica;
import es.mityc.firmaJava.libreria.utilidades.UtilidadTratarNodo;
import es.mityc.firmaJava.libreria.xades.CanonicalizationEnum;
import es.mityc.firmaJava.libreria.xades.DataToSign;
import es.mityc.firmaJava.libreria.xades.DatosFirma;
import es.mityc.firmaJava.libreria.xades.EnumFormatoFirma;
import es.mityc.firmaJava.libreria.xades.ExtraValidators;
import es.mityc.firmaJava.libreria.xades.FirmaXML;
import es.mityc.firmaJava.libreria.xades.RespYCerts;
import es.mityc.firmaJava.libreria.xades.ResultadoValidacion;
import es.mityc.firmaJava.libreria.xades.UtilidadXadesX;
import es.mityc.firmaJava.libreria.xades.ValidarFirmaXML;
import es.mityc.firmaJava.libreria.xades.XAdESSchemas;
import es.mityc.firmaJava.libreria.xades.elementos.xades.CRLRef;
import es.mityc.firmaJava.libreria.xades.elementos.xades.CRLRefs;
import es.mityc.firmaJava.libreria.xades.elementos.xades.CRLValues;
import es.mityc.firmaJava.libreria.xades.elementos.xades.CertificateValues;
import es.mityc.firmaJava.libreria.xades.elementos.xades.EncapsulatedX509Certificate;
import es.mityc.firmaJava.libreria.xades.errores.BadFormedSignatureException;
import es.mityc.firmaJava.libreria.xades.errores.FirmaXMLError;
import es.mityc.firmaJava.libreria.xades.errores.InvalidInfoNodeException;
import es.mityc.firmaJava.role.SimpleClaimedRole;
import es.mityc.firmaJava.ts.TSCliente;
import es.mityc.firmaJava.ts.TSClienteError;
import es.mityc.javasign.ConstantsXAdES;
import es.mityc.javasign.certificate.ICertStatus;
import es.mityc.javasign.certificate.ICertStatusRecoverer;
import es.mityc.javasign.certificate.IOCSPCertStatus;
import es.mityc.javasign.certificate.IX509CRLCertStatus;
import es.mityc.javasign.xml.refs.AllXMLToSign;
import es.mityc.javasign.xml.refs.InternObjectToSign;
import es.mityc.javasign.xml.refs.ObjectToSign;
import es.mityc.javasign.xml.refs.UnknownExternObjectToSign;

/**
 * Clase base de los tipos de firma XAdES.
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José M Gutiérrez</a>
 */
public abstract class XAdESSignature extends Signature {

	/**
	 * Logger de la clase
	 */
	static Logger logger = Logger.getLogger(XAdESSignature.class);
	
	/*
	 * Documento XML con el formato XAdES
	 */
	protected Document xadesDocument;
	
	/*
	 * ID del tag que contiene el documento
	 */
	protected static final String DEFAULT_ID_TAG_DOCUMENT = "ArangiXadesDocument";

	/*
	 * Nombre del tag raíz por defecto
	 */
	protected static final String DEFAULT_ROOT_TAG = "arangi-xades";

	/*
	 * Nombre de la plantilla para generar los ficheros XAdES
	 */
	protected static final String TEMPLATE_ARANGI_XADES = "es/accv/arangi/base/template/arangi-xades_template.xml";

	/**
	 * Esquema de XAdES para realizar las firmas
	 */
	public static final XAdESSchemas DEFAULT_XADES_SCHEMA = XAdESSchemas.XAdES_132;
	
	/**
	 * URI del esquema de XAdES para realizar las firmas
	 */
	public static final String DEFAULT_XADES_SCHEMA_URI = DEFAULT_XADES_SCHEMA.getSchemaUri();
	
	/**
	 * Encoding de los XML construidos
	 */
	public static final String DEFAULT_XML_ENCODING = "UTF-8";
	
	/**
	 * Espacio de nombres de XAdES
	 */
	protected static String 	xadesNS = ConstantsXAdES.DEFAULT_NS_XADES;
	
	/**
	 * Espacio de nombres para XMLDSig
	 */
	protected static String 	xmldsigNS = ConstantsXAdES.DEFAULT_NS_XMLSIG;

	/*
	 * Formato de fechas según el tipo dateTime del XML Schema Part 2 (http://www.w3.org/TR/xmlschema-2/#dateTime)
	 */
	protected static final SimpleDateFormat xsdDateTimeFormat = new SimpleDateFormat ("yyyy-MM-dd'T'HH:mm:ss");

	//-- Métodos abstractos
	
	/**
	 * Dado un resultado de validación obtenido con <code>validator.validar</code> devuelve
	 * un resultado de validación de Arangí. Tratamiento incluyendo validez de certificados.
	 */
	protected abstract int tratarResultadoValidacion(ResultadoValidacion resultadoValidacion);
	
	/**
	 * Cada clase hija debe determinar cual es su formato de firma: XAdES-BES, XAdES-T...
	 * 
	 * @return Formato de firma
	 */
	protected abstract EnumFormatoFirma getXAdESSignatureFormat ();
	
	//-- Métodos públicos
	
	/**
	 * Obtiene las fechas de las firmas de acuerdo al tag firmado 'SigningTime'. No se incluye
	 * la fecha de las contrafirmas. Esta fecha se obtiene del ordenador en el que se realizó la 
	 * firma, por lo que no es de confianza.
	 * 
	 * @return Fechas de las firmas
	 */
	public Date[] getSigningTimes() {
		logger.debug("[XAdESSignature.getSigningTimes]::Entrada");
		
		//-- Obtener el nodo
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		ArrayList<Date> alDates = new ArrayList<Date>();
		try {
			XPathExpression expr = xpath.compile("//*[local-name()='Signature']");
			NodeList signatureNodes = (NodeList) expr.evaluate (xadesDocument, XPathConstants.NODESET);
			for (int i=0;i<signatureNodes.getLength();i++) {
				
				Node signatureNode = signatureNodes.item(i);
				
				//-- No tenemos en cuenta las contrafirmas
				if (signatureNode.getParentNode() == null || signatureNode.getParentNode().getLocalName() == null ||
						!signatureNode.getParentNode().getLocalName().equals("CounterSignature")) {
					expr = xpath.compile("*[local-name()='Object']/*[local-name()='QualifyingProperties']/*[local-name()='SignedProperties']/" +
							"*[local-name()='SignedSignatureProperties']/*[local-name()='SigningTime']");
					Element signingTimeNode = (Element) expr.evaluate (signatureNode, XPathConstants.NODE);
					alDates.add(getXsdDateTimeParse(signingTimeNode.getTextContent()));
				}
			}
			
			return  alDates.toArray(new Date[0]);
			
		} catch (Exception e) {
			logger.info ("[XAdESSignature.getSigningTimes]::Error inesperado", e);
			return null;
		}

	}
	
	/**
	 * Obtiene la descripción del documento, siempre que al firmar se añadiese
	 * este tag dentro del DataObjectFormat. Si no se añadió devolverá null.
	 * 
	 * @return Descripción del documento firmado o null
	 */
	public String getDocumentDescription () {
		return getDataObjectFormatValue("Description");
	}
	
	/**
	 * Obtiene el tipo MIME del documento, siempre que al firmar se añadiese
	 * este tag dentro del DataObjectFormat. Si no se añadió devolverá null.
	 * 
	 * @return Tipo MIME del documento firmado o null
	 */
	public String getDocumentMIMEType () {
		return getDataObjectFormatValue("MimeType");
	}
	
	/**
	 * Obtiene la codificación del documento, siempre que al firmar se añadiese
	 * este tag dentro del DataObjectFormat. Si no se añadió devolverá null.
	 * 
	 * @return Tipo MIME del documento firmado o null
	 */
	public String getDocumentEncoding () {
		return getDataObjectFormatValue("Encoding");
	}
	
	/**
	 * Obtiene la firma XAdES como un documento DOM.
	 * 
	 * @return XML DOM
	 */
	public Document getDOM () {
		return xadesDocument;
	}
	
	/**
	 * Método que obtiene un objeto de una de las 3 clases de firmas XAdES en base
	 * a la firma que se pasa como parámetro. Para obtener de qué tipo de XAdES se
	 * trata se recurrirá a las clases del MITyC.
	 * 
	 * @param signature Firma XAdES
	 * @return Objeto de firma XAdES adecuado a la firma pasada como parámetro
	 * @throws FileNotFoundException El fichero no existe
	 * @throws XMLDocumentException El fichero no es un XML
	 * @throws SignatureException El XML no es un XAdES
	 */
	public static XAdESSignature getXAdESObject (File signature) throws SignatureException, XMLDocumentException, FileNotFoundException {
		return XAdESSignature.getXAdESObject (getDocument(signature));
	}
	
	/**
	 * Método que obtiene un objeto de una de las 3 clases de firmas XAdES en base
	 * a la firma que se pasa como parámetro. Para obtener de qué tipo de XAdES se
	 * trata se recurrirá a las clases del MITyC.
	 * 
	 * @param isSignature Firma XAdES
	 * @return Objeto de firma XAdES adecuado a la firma pasada como parámetro
	 * @throws XMLDocumentException El fichero no es un XML
	 * @throws SignatureException El XML no es un XAdES
	 */
	public static XAdESSignature getXAdESObject (InputStream isSignature) throws SignatureException, XMLDocumentException{
		return XAdESSignature.getXAdESObject (getDocument(isSignature));
	}
	
	/**
	 * Método que obtiene un objeto de una de las 3 clases de firmas XAdES en base
	 * a la firma que se pasa como parámetro. Para obtener de qué tipo de XAdES se
	 * trata se recurrirá a las clases del MITyC.
	 * 
	 * @param signature Firma XAdES
	 * @return Objeto de firma XAdES adecuado a la firma pasada como parámetro
	 * @throws XMLDocumentException 
	 * @throws SignatureException 
	 */
	public static XAdESSignature getXAdESObject (byte[] signature) throws SignatureException, XMLDocumentException {
		return XAdESSignature.getXAdESObject (getDocument(signature));
	}
	
	/**
	 * Método que obtiene un objeto de una de las 3 clases de firmas XAdES en base
	 * a la firma que se pasa como parámetro. Para obtener de qué tipo de XAdES se
	 * trata se recurrirá a las clases del MITyC.
	 * 
	 * @param signature Firma XAdES
	 * @return Objeto de firma XAdES adecuado a la firma pasada como parámetro
	 * @throws SignatureException El XML no es un XAdES
	 * @throws SignatureException No se puede obtener el tipo de XAdES, posiblemente
	 * 	porque no se trata de una firma XAdES
	 */
	public static XAdESSignature getXAdESObject (Document signature) throws SignatureException {
		logger.debug("[XAdESSignature.getXAdESObject]::Entrada::" + signature);
		
		//-- Buscar el elementos que caracterizan al tipo XAdES-X-L
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		NodeList certificateNodes;
		try {
			XPathExpression expr = xpath.compile("//*[local-name()='RevocationValues']");
			certificateNodes = (NodeList) expr.evaluate (signature, XPathConstants.NODESET);
			if (certificateNodes != null && certificateNodes.getLength() > 0) {
				logger.debug("[XAdESSignature.getXAdESObject]::La firma es un XAdES-X-L");
				return new XAdESXLSignature (signature);
			}
		} catch (Exception e) {
			logger.info ("[XAdESSignature.getXAdESObject]::Error buscando el nodo RevocationValues", e);
		}

		//-- Buscar el elementos que caracterizan al tipo XAdES-T
		try {
			XPathExpression expr = xpath.compile("//*[local-name()='SignatureTimeStamp']");
			certificateNodes = (NodeList) expr.evaluate (signature, XPathConstants.NODESET);
			if (certificateNodes != null && certificateNodes.getLength() > 0) {
				logger.debug("[XAdESSignature.getXAdESObject]::La firma es un XAdES-T");
				return new XAdESTSignature (signature);
			}
		} catch (Exception e) {
			logger.info ("[XAdESSignature.getXAdESObject]::Error buscando el nodo SignatureTimeStamp", e);
		}

		//-- Buscar el elementos que caracterizan al tipo XAdES-BES
		try {
			XPathExpression expr = xpath.compile("//*[local-name()='QualifyingProperties']");
			certificateNodes = (NodeList) expr.evaluate (signature, XPathConstants.NODESET);
			if (certificateNodes != null && certificateNodes.getLength() > 0) {
				logger.debug("[XAdESSignature.getXAdESObject]::La firma es un XAdES-BES");
				return new XAdESBESSignature(signature);
			}
		} catch (Exception e) {
			logger.info ("[XAdESSignature.getXAdESObject]::Error buscando el nodo QualifyingProperties", e);
		}

		//-- No es un XAdES
		logger.info("[XAdESSignature.getXAdESObject]::El XML no es un XAdES");
	    throw new SignatureException("El XML no es un XAdES");
		
	}
	
	/**
	 * Método para poder validar con el método Signature.validateSignature.<br><br>
	 * 
	 * Analiza el parámetro y, si se trata de un objeto XAdES, devuelve un 
	 * objeto del tipo adecuado.
	 * 
	 * @param bSignature Firma como array de bytes
	 * @return XAdES
	 * @throws Exception El parámetro no es un XAdES
	 */
	public static ISignature getSignatureInstance (byte[] bSignature) throws Exception {
		return getXAdESObject(bSignature);
	}
	
	//-- Métodos de Signature
	
	/**
	 * Devuelve los certificados con los que se han realizado las firmas
	 * 
	 * @return Certificados con los que se ha realizado las firmas
	 */
	public Certificate[] getCertificates() {

		logger.debug ("[XAdESSignature.getCertificates]::Entrada");
		
		//-- Localizar los certificados de la firma
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		NodeList certificateNodes;
		try {
			XPathExpression expr = xpath.compile("//*[local-name()='Signature']/*[local-name()='KeyInfo']/*[local-name()='X509Data']/*[local-name()='X509Certificate']");
			certificateNodes = (NodeList) expr.evaluate (this.xadesDocument, XPathConstants.NODESET);
		} catch (Exception e) {
			logger.info ("[XAdESSignature.getCertificates]::Error inesperado", e);
			return null;
		}

		if (certificateNodes == null || certificateNodes.getLength() == 0) {
			logger.info("[XAdESSignature.getCertificates]::Falta el elemento 'X509Certificate' de la firma");
			return null;
		}
		
		//-- Obtener los certificados
		Certificate[] certificates = new Certificate [certificateNodes.getLength()];
		for (int i = 0; i < certificateNodes.getLength(); i++) {
			try {
				X509Certificate x509Cert = Util.getCertificate(Util.decodeBase64(certificateNodes.item(i).getTextContent()));
				certificates[i] = new Certificate (x509Cert);
			} catch (Exception e) {
				logger.info ("[XAdESSignature.getCertificates]::Error inesperado", e);
				return null;
			}
		}

		return certificates;
	}

	/**
	 * Devuelve el documento contenido en la firma attached. La cadena devuelta por 
	 * este método será la representación en base64 del documento original.
	 * 
	 * @return Documento contenido en la firma attached o null si la firma es detached
	 * @throws SignatureException Error obteniendo el documento de la firma
	 * @throws SignatureNotFoundException No se encuentra ninguna firma dentro del 
	 * 	fichero XML
	 */
	public String getAttachedDocument() throws SignatureException, SignatureNotFoundException {

		logger.debug ("[XAdESSignature.getAttachedDocument]::Entrada");
		
		//-- Obtenemos el nodo donde se encuentra la firma. Cogemos la primera, en principio daría igual la elegida 
		// si todas son multifirmas
		String uriXmlNS = "http://www.w3.org/2000/09/xmldsig#";
		NodeList signatureList = xadesDocument.getElementsByTagNameNS(uriXmlNS, "Signature");
		Node nodeSignature = null;
		if (signatureList.getLength() < 1) {
			logger.info("[XAdESSignature.getAttachedDocument]:: No existe ninguna firma en el XAdES");
			throw new SignatureNotFoundException("No existe ninguna firma en el XAdES");
		} else {
			nodeSignature = signatureList.item(0);
		}
		
		logger.debug("[XAdESSignature.getAttachedDocument]:: Obtenido el nodo de firma");
		
		//-- Obtenemos la referencia al documento. Será la que no empieze por SignedProperties y tenga ID
		try {
			XMLSignature firmaDocumento = new XMLSignature((Element)nodeSignature, "./");
			SignedInfo signedInfo = firmaDocumento.getSignedInfo();
			for(int i = 0; i < signedInfo.getLength(); i++){
				Reference ref = signedInfo.item(i);
	            if (ref.getId() != null && !ref.getId().startsWith("SignedProperties") && !ref.getId().equals("")) {
	            	String uri = ref.getURI();
	            	if (!uri.startsWith("#")) {
						// Sera una firma Dettached
						logger.debug("[XAdESSignature.getAttachedDocument]:: Se trata de una firma detached, el método devolverá un valor nulo");
						return null;
					} else {
						// Sera una firma Attached
	            		logger.debug("[XAdESSignature.getAttachedDocument]:: Se trata de una firma attached");
	            		uri = uri.substring(1);
	            		XPathFactory factory = XPathFactory.newInstance();
	            		XPath xpath = factory.newXPath();
	            		Node node = null;
	            		try {
	            			XPathExpression expr = xpath.compile("//*[@id='" + uri + "'] | //*[@Id='" + uri + "'] | //*[@iD='" + uri + "'] | //*[@ID='" + uri + "']");
	            			node = (Node) expr.evaluate (this.xadesDocument, XPathConstants.NODE);
	            		} catch (Exception e) {
	            			logger.info ("[XAdESSignature.getAttachedDocument]::Error inesperado", e);
	            			return null;
	            		}
	            		if (node == null) {
	            			logger.info ("[XAdESSignature.getAttachedDocument]::No se ha encontrado el elemento con ID=" + uri);
	            			return null;
	            		}
	            		
	            		logger.debug("[XAdESSignature.getAttachedDocument]:: Encontrado documento en uri con ID=" + uri);
	            		return node.getTextContent();
					}
				}    
			}
			
		} catch (XMLSecurityException e) {
			logger.info("[XAdESSignature.coSign]::Error buscando elementos en el fichero XAdES", e);
			throw new SignatureException("Error buscando elementos en el fichero XAdES", e);
		}
		
		//-- No se ha encontrado ninguna referencia
		logger.debug("[XAdESSignature.getAttachedDocument]:: No se ha encontrado ninguna referencia con ID=Reference-ID...");
		return null;

	}

	/* (non-Javadoc)
	 * @see es.accv.arangi.base.signature.ISignature#isValid(CAList)
	 */
	public ValidationResult[] isValid(CAList caList) throws SignatureException {

		return isValid(null, caList);
		
	}

	/* (non-Javadoc)
	 * @see es.accv.arangi.base.signature.ISignature#isValid(IDocument,CAList)
	 */
	public ValidationResult[] isValid(IDocument document, CAList caList)
			throws SignatureException {
		
		logger.debug("[XAdESSignature.isValid]::Entrada::" + Arrays.asList(new Object[] { document, caList }));
	
		return isValidCommon(document, caList, null);
	}

	/* (non-Javadoc)
	 * @see es.accv.arangi.base.signature.ISignature#isValid(java.util.List(es.accv.arangi.base.certificate.validation.service.CertificateValidationService))
	 */
	public ValidationResult[] isValid(List<CertificateValidationService> validationServices)
			throws SignatureException {
		
		return isValid(null, validationServices);
		
	}

	/* (non-Javadoc)
	 * @see es.accv.arangi.base.signature.ISignature#isValid(IDocument,java.util.List(es.accv.arangi.base.certificate.validation.service.CertificateValidationService))
	 */
	public ValidationResult[] isValid(IDocument document, List<CertificateValidationService> validationServices)
			throws SignatureException {
		
		logger.debug("[XAdESSignature.isValid]::Entrada::" + Arrays.asList(new Object[] { document, validationServices }));
		
		return isValidCommon(document, null, validationServices);
	}


	/* (non-Javadoc)
	 * @see es.accv.arangi.base.signature.ISignature#isValidSignatureOnly()
	 */
	public ValidationResult[] isValidSignatureOnly() throws SignatureException {
		try {
			return isValidSignatureOnly(null);
		} catch (HashingException e) {
			// no se va a dar
			logger.info("[XAdESSignature.isValidSignatureOnly]::Error inesperado", e);
			throw new SignatureException ("Error inesperado", e);
		}
	}

	/* (non-Javadoc)
	 * @see es.accv.arangi.base.signature.ISignature#isValidSignatureOnly(es.accv.arangi.base.document.IDocument)
	 */
	public ValidationResult[] isValidSignatureOnly(IDocument document) throws HashingException, SignatureException {
		
		logger.debug("[XAdESSignature.isValidSignatureOnly]::Entrada");
		
	    ValidarFirmaXML validator = loadValidator (document);
	    List<ResultadoValidacion> results;
		try {
			results = validator.validar(this.xadesDocument, "./", null);
		} catch (FirmaXMLError e) {
			logger.info("[XAdESSignature.isValidSignatureOnly]::No ha sido posible validar la firma", e);
		    throw new SignatureException("No ha sido posible validar la firma", e);
		}
		
		ArrayList<ValidationResult> result = new ArrayList<ValidationResult>();
	    for (Iterator<ResultadoValidacion> iterator = results.iterator(); iterator.hasNext();) {
			ResultadoValidacion resultadoValidacion = iterator.next();
			ValidationResult validationResult = getValidationResultSignatureOnly(resultadoValidacion, false);
			if (validationResult != null) {
				result.add(validationResult);
			}
		}
		
		return result.toArray(new ValidationResult[0]);
		
	}
	
	/**
	 * Obtiene la codificación del XML que contiene el XAdES
	 * 
	 * @return Codificación del XML que contiene el XAdES
	 */
	public String getEncoding() {
		if(xadesDocument.getInputEncoding() != null) {
			logger.debug("[XAdESSignature.getEncoding]:: Devolviendo " + xadesDocument.getInputEncoding());
			return xadesDocument.getInputEncoding();
		}
		if(xadesDocument.getXmlEncoding() != null) {
			logger.debug("[XAdESSignature.getEncoding]:: Devolviendo " + xadesDocument.getXmlEncoding());
			return xadesDocument.getXmlEncoding();
		}
		logger.debug("[XAdESSignature.getEncoding]:: Devolviendo por defecto " + DEFAULT_XML_ENCODING);
		return DEFAULT_XML_ENCODING;
	}
	
	/**
	 * Obtiene como una cadena de texto el campo SignaturePolicyIdentifier.
	 * 
	 * @return campo SignaturePolicyIdentifier o null si la firma no es EPES
	 */
	public String getSignaturePolicyIdentifier () {
		logger.debug("[XAdESSignature.getSignaturePolicyIdentifier]::Entrada");
		
		//-- Obtener el nodo
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		try {
			XPathExpression expr = xpath.compile("//*[local-name()='Object']/*[local-name()='QualifyingProperties']/*[local-name()='SignedProperties']/" +
					"*[local-name()='SignedSignatureProperties']/*[local-name()='SignaturePolicyIdentifier']");
			Node spiNode = (Node) expr.evaluate (xadesDocument, XPathConstants.NODE);
			if (spiNode == null) {
				logger.debug("[XAdESSignature.getSignaturePolicyIdentifier]::La firma no es EPES");
				return null;
			}
			
			return nodeToString(spiNode);
			
		} catch (Exception e) {
			logger.info ("[XAdESSignature.getSignaturePolicyIdentifier]::Error inesperado", e);
			return null;
		}

	}
	
	/**
	 * Obtiene como una cadena de texto el campo SignaturePolicyIdentifier.
	 * 
	 * @return campo getSignatureProductionPlace o null si la firma no es EPES
	 */
	public ArangiXAdESProductionPlace getSignatureProductionPlace () {
		logger.debug("[XAdESSignature.getSignatureProductionPlace]::Entrada");
		
		//-- Obtener el nodo
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		try {
			XPathExpression expr = xpath.compile("//*[local-name()='Object']/*[local-name()='QualifyingProperties']/*[local-name()='SignedProperties']/" +
					"*[local-name()='SignedSignatureProperties']/*[local-name()='SignatureProductionPlace']");
			Node sppNode = (Node) expr.evaluate (xadesDocument, XPathConstants.NODE);
			if (sppNode == null) {
				logger.debug("[XAdESSignature.getSignatureProductionPlace]::La firma no no contiene el elemento SignatureProductionPlace");
				return null;
			}
			
			String city = ((Node) xpath.compile("//*[local-name()='City']").evaluate (sppNode, XPathConstants.NODE)).getTextContent();
			String state = ((Node) xpath.compile("//*[local-name()='StateOrProvince']").evaluate (sppNode, XPathConstants.NODE)).getTextContent();
			String postalCode = ((Node) xpath.compile("//*[local-name()='PostalCode']").evaluate (sppNode, XPathConstants.NODE)).getTextContent();
			String country = ((Node) xpath.compile("//*[local-name()='CountryName']").evaluate (sppNode, XPathConstants.NODE)).getTextContent();
			
			return new ArangiXAdESProductionPlace(city, state, postalCode, country);
			
		} catch (Exception e) {
			logger.info ("[XAdESSignature.getSignatureProductionPlace]::Error inesperado", e);
			return null;
		}

	}
	
	/**
	 * Guarda la firma XAdES en un fichero
	 * 
	 * @param file Fichero donde se guardará la firma
	 * @throws FileNotFoundException No se puede escribir en el fichero
	 * @throws XMLDocumentException Errores en la transformación de DOM a un stream 
	 * 	de escritura
	 */
	public void save (File file) throws IOException {
		
		logger.debug("[XAdESSignature.save]::Entrada::" + file);
		
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream (file);
	        save(fos);
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					logger.info("[XAdESSignature.save]::No se puede cerrar el stream de escritura al fichero", e);
				}
			}
		}
	}
	
	/**
	 * Guarda la firma en un stream de escritura.
	 * 
	 * @param out Stream de escritura
	 * @throws IOException Errores de entrada / salida o durante la transformación DOM a XML
	 */
	public void save (OutputStream out) throws IOException {
		logger.debug("[XAdESSignature.save]::Entrada::" + out);
		
		try {
	        TransformerFactory tf = TransformerFactory.newInstance();
	    	Transformer trans = tf.newTransformer();
	    	trans.transform(new DOMSource(xadesDocument), new StreamResult(out));
		} catch (TransformerConfigurationException e) {
			logger.info("[XAdESSignature.save]::No está disponible una implementación para transformar el árbol DOM", e);
			throw new IOException ("No está disponible una implementación para transformar el árbol DOM" + e.getMessage());
		} catch (TransformerException e) {
			logger.info("[XAdESSignature.save]::No está disponible una implementación para transformar el árbol DOM", e);
			throw new IOException ("No está disponible una implementación para transformar el árbol DOM" + e.getMessage());
		} 
	}
	
	/**
	 * Devuelve la firma XAdES como un array de bytes
	 * 
	 * @return Firma como array de bytes
	 */
	public byte[] toByteArray () {
		logger.debug("[XAdESSignature.toByteArray]::Entrada");
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			save(baos);
		} catch (IOException e) {
			// Raro raro
			logger.info("[XAdESSignature.toByteArray]::No ha sido posible guardar en memoria", e);
			return null;
		}
		return baos.toByteArray();

	}
	
	//-- Métodos protected
	
	/**
	 * Construye el objeto en base a un XML que tiene el formato
	 * XAdES-BES
	 * 
	 * @param xmlDocument Documento XML
	 */
	protected void initialize(Document xmlDocument) {
		//TODO Validar que el XML es un XAdES
		xadesDocument = xmlDocument;
	}
	
	/**
	 * Construye el objeto en base a un fichero XAdES-BES
	 * 
	 * @param xmlFile Fichero XAdES
	 * @throws FileNotFoundException El fichero no existe
	 * @throws XMLDocumentException El fichero no parece un XML válido
	 */
	protected void initialize(File xmlFile) throws FileNotFoundException, XMLDocumentException {
		xadesDocument = getDocument(xmlFile);
	}

	/**
	 * Construye el objeto en base a un array de bytes.
	 * 
	 * @param signature Firma XAdES
	 * @throws XMLDocumentException El fichero no parece un XML válido
	 */
	protected void initialize(byte[] signature) throws XMLDocumentException {
		xadesDocument = getDocument(signature);
	}

	/**
	 * Construye el objeto en base a un stream de lectura.
	 * 
	 * @param isSignature Stream a la firma XAdES
	 * @throws XMLDocumentException El fichero no parece un XML válido
	 */
	protected void initialize(InputStream isSignature) throws XMLDocumentException {
		xadesDocument = getDocument(isSignature);
	}

	/**
	 * Obtiene un document de XML en base a un fichero XAdES-BES
	 * 
	 * @param xmlFile Fichero XAdES-BES
	 * @throws FileNotFoundException El fichero no existe
	 * @throws XMLDocumentException El fichero no parece un XML válido
	 */
	private static Document getDocument (File xmlFile) throws FileNotFoundException, XMLDocumentException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		try {
			return dbf.newDocumentBuilder().parse(new FileInputStream(xmlFile));
		} catch (Exception e) {
			logger.info("[XAdESSignature(file)]::El fichero no parece ser un XML válido", e);
			throw new XMLDocumentException("El fichero no parece ser un XML válido", e);
		} 
	}

	/**
	 * Obtiene un document de XML en base a un array de bytes.
	 * 
	 * @param signature Firma XAdES-BES
	 * @throws XMLDocumentException El fichero no parece un XML válido
	 */
	private static Document getDocument(byte[] signature) throws XMLDocumentException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		try {
			return dbf.newDocumentBuilder().parse(new ByteArrayInputStream(signature));
		} catch (Exception e) {
			logger.info("[XAdESSignature(byte[])]::La firma no parece ser un XML válido", e);
			throw new XMLDocumentException("La firma no parece ser un XML válido", e);
		} 
	}

	/**
	 * Obtiene un document de XML en base a un stream de lectura.
	 * 
	 * @param isSignature Stream a la firma XAdES-BES
	 * @throws XMLDocumentException El fichero no parece un XML válido
	 */
	private static Document getDocument(InputStream isSignature) throws XMLDocumentException {
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(true);
		try {
			return dbf.newDocumentBuilder().parse(isSignature);
		} catch (Exception e) {
			logger.info("[XAdESSignature(byte[])]::La firma no parece ser un XML válido", e);
			throw new XMLDocumentException("La firma no parece ser un XML válido", e);
		} 
	}

	/*
	 * Obtiene la fecha del sello de tiempos de la firma (Punto 7.3 del estándar).
	 * 
	 * @return Fecha del sello de tiempos
	 * @throws MalformedTimeStampException El sello de tiempos guardado en el fichero XAdES-T no 
	 * está bien formado
	 */
	protected Date getXAdESTimeStampTime () throws MalformedTimeStampException {
		logger.debug("[XAdESSignature.getXAdESTimeStampTime]::Entrada");
		
		//-- Obtener el nodo
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		Element tsNode = null;
		try {
			XPathExpression expr = xpath.compile("//*[local-name()='UnsignedSignatureProperties']/*[local-name()='SignatureTimeStamp']/*[local-name()='EncapsulatedTimeStamp']");
			tsNode = (Element) expr.evaluate (xadesDocument, XPathConstants.NODE);
		} catch (Exception e) {
			logger.info ("[XAdESSignature.getXAdESTimeStampTime]::Error inesperado", e);
			return null;
		}
		
		//-- Obtener el sello de tiempos
		TimeStamp timeStamp = new TimeStamp(new ByteArrayInputStream (Util.decodeBase64(tsNode.getTextContent())));
		
		//-- Obtener fecha
		return timeStamp.getTime();

	}
	
	/*
	 * Obtiene el certificado de firma del sello de tiempos de la firma (Punto 7.3 del estándar).
	 * 
	 * @return Certificado de firma del sello de tiempos
	 * @throws MalformedTimeStampException El sello de tiempos guardado en el fichero XAdES-T no 
	 * está bien formado
	 */
	protected static Certificate getXAdESTimeStampCertificate (Document xadesDocument) throws MalformedTimeStampException {
		logger.debug("[XAdESSignature.getXAdESTimeStampCertificate]::Entrada");
		
		//-- Obtener el nodo
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		Element tsNode = null;
		try {
			XPathExpression expr = xpath.compile("//*[local-name()='UnsignedSignatureProperties']/*[local-name()='SignatureTimeStamp']/*[local-name()='EncapsulatedTimeStamp']");
			tsNode = (Element) expr.evaluate (xadesDocument, XPathConstants.NODE);
		} catch (Exception e) {
			logger.info ("[XAdESSignature.getXAdESTimeStampTime]::Error inesperado", e);
			return null;
		}
		
		//-- Obtener el sello de tiempos
		TimeStamp timeStamp = new TimeStamp(new ByteArrayInputStream (Util.decodeBase64(tsNode.getTextContent())));
		
		//-- Obtener fecha
		return timeStamp.getSignatureCertificate();

	}
	
	/**
	 * Método que define la información a firmar para una firma attached
	 * 
	 * @param docToSign Documento que se firmará
	 * @param nodeToSign Nodo del XML que se debe firmar
	 * @param certStatusRecover Objeto para determinar estado de certificados. Se utiliza
	 * 	a partir del XAdES-C
	 * @param tipoXAdES Tipo de XAdES tal y como se establece en las constantes de la clase
	 * 	<code>EnumFormatoFirma</code>
	 * @parma dof Información para construir el tag DataObjectFormat (puede ser null)
	 * @param claimedRoles Roles de la firma
	 * @param policyIdentifier Identificador de la política (firmas EPES)
	 * @param productionPlace SignatureProductionPlace
	 * @param tsaHashingAlgorithm Algoritmo de hashing para el sello de tiempos
	 * @return Información de la firma
	 */
	protected static DataToSign createDataToSignAtached(Document docToSign, XAdESAttachedNodeToSign nodeToSign, 
			URI encoding, ICertStatusRecoverer certStatusRecover, EnumFormatoFirma tipoXAdES, 
			XAdESDataObjectFormat dof, String[] claimedRoles, ArangiXAdESPolicyIdentifier policyIdentifier,
			ArangiXAdESProductionPlace productionPlace, String tsaHashingAlgorithm) {
        DataToSign dataToSign = new DataToSign();
        dataToSign.setXadesFormat(tipoXAdES);
        if (certStatusRecover != null) {
        	dataToSign.setCertStatusManager (certStatusRecover);
        }
        dataToSign.setEsquema(DEFAULT_XADES_SCHEMA);
        dataToSign.setXMLEncoding(DEFAULT_XML_ENCODING);
        dataToSign.setEnveloped(true);
        dataToSign.setDocument(docToSign);
        
        if (policyIdentifier != null) {
        	dataToSign.setPolicy(policyIdentifier.getSignaturePolicyIdentifier());
        }
        if (productionPlace != null) {
        	dataToSign.setProductionPlace(productionPlace.getCity(), productionPlace.getState(), 
        			productionPlace.getPostalCode(), productionPlace.getCountry());
        }
        
        String dofDescripcion = null;
        ObjectIdentifier dofObjectIdentifier = null;
        String dofMimeType = null;
        URI dofEncoding = null;
        if (dof != null) {
        	dofDescripcion = dof.getDescription();
        	dofObjectIdentifier = dof.getObjectIdentifier();
        	dofMimeType = dof.getMimeType();
        	dofEncoding = dof.getEncoding();
        }
        if (nodeToSign instanceof XAdESAttachedNodeToSignEnveloped) {
        	dataToSign.addObject(new ObjectToSign(new AllXMLToSign(), dofDescripcion, dofObjectIdentifier, dofMimeType, dofEncoding));
        } else {
        	dataToSign.addObject(new ObjectToSign(new InternObjectToSign(((XAdESAttachedNodeToSignObject)nodeToSign).getIdToSign()), dofDescripcion, dofObjectIdentifier, dofMimeType, dofEncoding));
        }
        if (claimedRoles != null) {
        	for(String claimedRole : claimedRoles) {
        		dataToSign.addClaimedRol(new SimpleClaimedRole(claimedRole));
        	}
        }
        
        dataToSign.setParentSignNode(docToSign.getDocumentElement().getNodeName());
        logger.debug("ParentSignNode: " + docToSign.getDocumentElement().getNodeName());

        if (tsaHashingAlgorithm != null) {
			try {
				String tsaHashingAlgorithmXades = XAdESUtil.getXAdESHashingAlgorithm(tsaHashingAlgorithm);
	       		dataToSign.setAlgDigestTSA(tsaHashingAlgorithmXades);
			} catch (AlgorithmNotSuitableException e) {
				logger.info("No existe el algoritmo de hashing para la TSA");
			}
        }

        return dataToSign;
	}

	/**
	 * Método que define la información a firmar para una firma dettached.
	 * 
	 * @param document Documento que se firmará
	 * @param reference Referencia a la que apuntará la firma
	 * @param certStatusRecover Objeto para determinar estado de certificados. Se utiliza
	 * 	a partir del XAdES-C
	 * @param tipoXAdES Tipo de XAdES tal y como se establece en las constantes de la clase
	 * 	<code>EnumFormatoFirma</code>
	 * @param dof Información para construir el tag DataObjectFormat (puede ser null)
	 * @param claimedRoles Roles de la firma
	 * @param policyIdentifier Identificador de la política (firmas EPES)
	 * @param productionPlace SignatureProductionPlace
	 * @param tsaHashingAlgorithm Algoritmo de hashing para el sello de tiempos
	 * @return Información de la firma
	 * @throws XMLDocumentException
	 */
	protected static DataToSign createDataToSignDetached(IDocument document, String reference, 
			ICertStatusRecoverer certStatusRecover, EnumFormatoFirma tipoXAdES, XAdESDataObjectFormat dof,
			String[] claimedRoles, ArangiXAdESPolicyIdentifier policyIdentifier,
			ArangiXAdESProductionPlace productionPlace, String tsaHashingAlgorithm) {
	    DataToSign dataToSign = new DataToSign();
	    dataToSign.setXadesFormat(tipoXAdES);
        if (certStatusRecover != null) {
        	dataToSign.setCertStatusManager (certStatusRecover);
        }
	    dataToSign.setEsquema(DEFAULT_XADES_SCHEMA);
	    dataToSign.setXMLEncoding(DEFAULT_XML_ENCODING);
	    dataToSign.setEnveloped(false);
        if (policyIdentifier != null) {
        	dataToSign.setPolicy(policyIdentifier.getSignaturePolicyIdentifier());
        }
        if (productionPlace != null) {
        	dataToSign.setProductionPlace(productionPlace.getCity(), productionPlace.getState(), 
        			productionPlace.getPostalCode(), productionPlace.getCountry());
        }
        
        String dofDescripcion = null;
        ObjectIdentifier dofObjectIdentifier = null;
        String dofMimeType = null;
        URI dofEncoding = null;
        if (dof != null) {
        	dofDescripcion = dof.getDescription();
        	dofObjectIdentifier = dof.getObjectIdentifier();
        	dofMimeType = dof.getMimeType();
        	dofEncoding = dof.getEncoding();
        }
        
        if (tsaHashingAlgorithm != null) {
			try {
				String tsaHashingAlgorithmXades = XAdESUtil.getXAdESHashingAlgorithm(tsaHashingAlgorithm);
	       		dataToSign.setAlgDigestTSA(tsaHashingAlgorithmXades);
			} catch (AlgorithmNotSuitableException e) {
				logger.info("No existe el algoritmo de hashing para la TSA");
			}
        }

       	dataToSign.addObject(new ObjectToSign(new UnknownExternObjectToSign(reference, new ArangiDocumentPrivateData(document)),  dofDescripcion, dofObjectIdentifier, dofMimeType, dofEncoding));
 
       	if (claimedRoles != null) {
        	for(String claimedRole : claimedRoles) {
        		dataToSign.addClaimedRol(new SimpleClaimedRole(claimedRole));
        	}
        }
	    return dataToSign;
	}

	/*
	 * Realiza una firma XAdES detached (el fichero no se incluirá en la firma). No completa los campos 
	 * no obligatorios del tag 'SignedSignatureProperties':'signaturePolicyIdentifier' y 
	 * 'signatureProductionPlace'.
	 * 
	 * @param manager Dispositivo criptográfico que realizará la firma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param document Documento a firmar. 
	 * @param digitalSignatureAlgorithm Algoritmo de firma
	 * @param reference Referencia a la que apuntará la firma
	 * @param urlTSA URL al servicio de sello de tiempos
	 * @param certStatusRecover Objeto para determinar estado de certificados. Se utiliza
	 * 	a partir del XAdES-C
	 * @param tipoXAdES Tipo de XAdES tal y como se establece en las constantes de la clase
	 * 	<code>EnumFormatoFirma</code>
	 * @param dof Información para construir el tag DataObjectFormat (puede ser null)
	 * @param claimedRoles Roles de la firma
	 * @param returnClassObject Clase del objeto respuesta. Debe ser coincidente con lo indicado
	 * 	en el parámetro tipoXAdES
	 * @return Firma XADES del tipo indicado
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 */
	protected static XAdESSignature signDetached (DeviceManager manager, String alias, IDocument document, 
			String reference, TSAData tsaData, ICertStatusRecoverer certStatusRecover, 
			XAdESDetachedSignatureOptions options, EnumFormatoFirma tipoXAdES, Class<?> returnClassObject) 
					throws LoadingObjectException, SignatureException {
		
		logger.debug("[XAdESSignature.signDetached]::Entrada::" + Arrays.asList(new Object[] { manager, alias, document, reference, certStatusRecover, tipoXAdES, tsaData, options, returnClassObject }));
		
		if (options == null) {
			options = new XAdESDetachedSignatureOptions();
		}
		
		// Obtener el certificado en base64
		X509Certificate certificate;
		try {
			logger.debug ("[XAdESSignature.signDetached]::obteniendo el certificado");
			certificate = manager.getCertificate(alias);
		} catch (SearchingException e) {
			logger.info("[XAdESSignature.signDetached]::Error buscando un certificado en el alias'" + alias + "'", e);
			throw new LoadingObjectException ("Error buscando un certificado en el alias'" + alias + "'", e);
		}
		
		if (certificate == null) {
			logger.info("[XAdESSignature.signDetached]::No se ha podido encontrar un certificado en el alias'" + alias + "'");
			throw new LoadingObjectException ("No se ha podido encontrar un certificado en el alias'" + alias + "'");
		}

		//-- Obtener los datos a firmar
		logger.debug ("[XAdESSignature.signDetached]::obteniendo los datos a firmar");
		DataToSign dataToSign = createDataToSignDetached(document, reference, certStatusRecover, tipoXAdES, 
				options.getDof(), options.getClaimedRoles(), options.getPolicyIdentifier(),
				options.getProductionPlace(), options.getTsaHashingAlgorithm());
		if (dataToSign == null) {
			logger.debug ("[XAdESSignature.signDetached]::El objeto a firmar no es un fichero ni una URL");
			throw new SignatureException ("El objeto a firmar no es un fichero ni una URL");
		}
		
		//-- Firmar
		try {
			logger.debug ("[XAdESSignature.signDetached]::Firmando");
			FirmaXML firma = new FirmaXML();  
			if (tsaData != null) { 
				firma.setTSA(tsaData.getUrl().toString()); 
			}
			Object[] res = firma.signFile(certificate, dataToSign, manager.getPrivateKey(alias), XAdESUtil.getXAdESDigitalSignatureAlgorithm(options.getDigitalSignatureAlgorithm()), null);
			logger.debug ("[XAdESSignature.signDetached]::Firma realizada");
			
			//-- Devolver el tipo adecuado
			Constructor<?> constructor = returnClassObject.getDeclaredConstructor(Document.class);
			return (XAdESSignature)constructor.newInstance((Document) res[0]);
			
		} catch (Exception e) {
			logger.info("[XAdESSignature.signDetached]::No ha sido posible realizar la firma", e);
			throw new SignatureException ("No ha sido posible realizar la firma", e);
		}
		
	}
	
	/*
	 * Realiza una firma XAdES atached (el documento se incluye en la firma). No completa los campos no 
	 * obligatorios del tag 'SignedSignatureProperties':'signaturePolicyIdentifier' y 'signatureProductionPlace' 
	 * 
	 * @param manager Dispositivo criptográfico que realizará la firma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param document Documento a firmar. 
	 * @param tsaData Información de acceso al servicio de sello de tiempos
	 * @param certStatusRecover Objeto para determinar estado de certificados. Se utiliza
	 * 	a partir del XAdES
	 * @param options Opciones de la firma
	 * @param tipoXAdES Tipo de XAdES tal y como se establece en las constantes de la clase
	 * 	<code>EnumFormatoFirma</code>
	 * @param returnClassObject Clase del objeto respuesta. Debe ser coincidente con lo indicado
	 * 	en el parámetro tipoXAdES
	 * @return Firma XADES del tipo indicado
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No se puede realizar la firma
	 */
	protected static XAdESSignature signAttached (DeviceManager manager, String alias, IDocument document, 
			TSAData tsaData, ICertStatusRecoverer certStatusRecover, XAdESAttachedSignatureOptions options, 
			EnumFormatoFirma tipoXAdES,	Class<?> returnClassObject) 
					throws XMLDocumentException, LoadingObjectException, SignatureException  {
		
		logger.debug("[XAdESSignature.signAttached]::Entrada::" + Arrays.asList(new Object[] { manager, alias, document, certStatusRecover, tipoXAdES, tsaData, options, returnClassObject }));
		
		if (options == null) {
			options = new XAdESAttachedSignatureOptions();
		}
		
		// Obtener el certificado en base64
		X509Certificate certificate;
		try {
			logger.debug ("[XAdESSignature.signAttached]::obteniendo el certificado");
			certificate = manager.getCertificate(alias);
		} catch (SearchingException e) {
			logger.info("[XAdESSignature.signAttached]::Error buscando un certificado en el alias'" + alias + "'", e);
			throw new LoadingObjectException ("Error buscando un certificado en el alias'" + alias + "'", e);
		}
		
		if (certificate == null) {
			logger.info("[XAdESSignature.signAttached]::No se ha podido encontrar un certificado en el alias'" + alias + "'");
			throw new LoadingObjectException ("No se ha podido encontrar un certificado en el alias'" + alias + "'");
		}

		//-- Guardar el documento en un fichero temporal (se ha de leer varias veces
		//-- y puede ser un stream de lectura)
		File tempFile;
		try {
			tempFile = saveTemporalFile(document.getInputStream());
		} catch (IOException e) {
			logger.info ("[XAdESSignature.signAttached]::No es posible guardar el fichero temporal con el documento a firmar", e);
			throw new LoadingObjectException ("No es posible guardar el fichero temporal con el documento a firmar", e);
		}
		
		try {
			DocumentBuilder db = getDocumentBuilder ();
			
			//-- Comprobar si el fichero es un XML
			Document documentToSign = null;
			try {
				documentToSign = db.parse(tempFile);
			} catch (SAXException e1) {
				logger.debug ("[XAdESSignature.signAttached]::El fichero no es un XML: " + e1.getMessage());
			} catch (IOException e1) {
				logger.debug ("[XAdESSignature.signAttached]::El fichero no es un XML: " + e1.getMessage());
			}
			
			DataToSign dataToSign;
			if (options.getNodeToSign() != null && documentToSign != null) {
				
				//-- Preparar la firma
				logger.debug ("[XAdESSignature.signAttached]::Se firmará el XML del usuario");
				dataToSign = createDataToSignAtached(documentToSign, options.getNodeToSign(), null, 
						certStatusRecover, tipoXAdES, options.getDof(), options.getClaimedRoles(),
						options.getPolicyIdentifier(), options.getProductionPlace(), options.getTsaHashingAlgorithm());
				
			} else {
				
				logger.debug ("[XAdESSignature.signAttached]::Construir la plantilla por defecto de XAdES");
				
				//-- URI de la codificación (por defecto null)
				URI uri = null;
				
				//-- Obtener la plantilla
				Document doc;
				try {
					doc = db.parse(new Util().getClass().getClassLoader().getResourceAsStream(TEMPLATE_ARANGI_XADES));
				} catch (SAXException e) {
					logger.info("[XAdESSignature.signAttached]::No ha sido posible obtener la plantilla de Arangí-XAdES", e);
					throw new XMLDocumentException ("No ha sido posible obtener la plantilla de Arangí-XAdES", e);
				} catch (IOException e) {
					logger.info("[XAdESSignature.signAttached]::No ha sido posible obtener la plantilla de Arangí-XAdES", e);
					throw new XMLDocumentException ("No ha sido posible obtener la plantilla de Arangí-XAdES", e);
				}
				
				//-- Buscar el elemento documento
				XPathFactory factory = XPathFactory.newInstance();
				XPath xpath = factory.newXPath();
				NodeList signatureNodes;
				try {
					XPathExpression expr = xpath.compile("//*[@Id='" + DEFAULT_ID_TAG_DOCUMENT + "']");
					signatureNodes = (NodeList) expr.evaluate (doc, XPathConstants.NODESET);
				} catch (XPathExpressionException e) {
					logger.info("[XAdESSignature.signAttached]::No ha sido posible obtener el elemento documento dentro de la plantilla", e);
					throw new XMLDocumentException ("No ha sido posible obtener el elemento documento dentro de la plantilla", e);
				}
				Element eDocument = (Element) signatureNodes.item(0);
				
				//-- Añadir el documento
				logger.debug ("[XAdESSignature.signAttached]::Añadir fichero en base64 a la plantilla");
				try {
					eDocument.appendChild(doc.createTextNode(Util.encodeBase64(Util.loadFile(tempFile))));
					uri = new URI (ConstantesXADES.URI_BASE_64);
				} catch (IOException e) {
					logger.info ("[XAdESSignature.signAttached]::No es posible leer el fichero temporal con el documento a firmar", e);
					throw new XMLDocumentException ("No es posible leer el fichero temporal con el documento a firmar", e);
				} catch (URISyntaxException e) {
					//-- No se dará o las clases del mityc estarían mal
					logger.info ("[XAdESSignature.signAttached]::No es posible obtener la URI: " + ConstantesXADES.URI_BASE_64, e);
				}
				
				//-- Preparar la firma. Si es enveloped se firma todo el documento. Si no se firma el tag del documento
				XAdESAttachedNodeToSign nodeToSign = options.getNodeToSign();
				if (nodeToSign == null) {
					nodeToSign = new XAdESAttachedNodeToSignObject(DEFAULT_ID_TAG_DOCUMENT);
				}
				dataToSign = createDataToSignAtached(doc, nodeToSign, uri, certStatusRecover, tipoXAdES, 
						options.getDof(), options.getClaimedRoles(), options.getPolicyIdentifier(),
						options.getProductionPlace(), options.getTsaHashingAlgorithm());
				
			}
			
			//-- Firmar
			try {
				FirmaXML firma = new FirmaXML();  
				if (tsaData != null) { 
					firma.setTSA(tsaData.getUrl().toString());
				}
				Object[] res = firma.signFile(certificate, dataToSign, manager.getPrivateKey(alias), XAdESUtil.getXAdESDigitalSignatureAlgorithm(options.getDigitalSignatureAlgorithm()), null);
				
				//-- Devolver el tipo adecuado
				Constructor<?> constructor = returnClassObject.getDeclaredConstructor(Document.class);
				return (XAdESSignature)constructor.newInstance((Document) res[0]);
	
			} catch (Exception e) {
				logger.info("[XAdESSignature.sign]::No ha sido posible realizar la firma", e);
				throw new SignatureException ("No ha sido posible realizar la firma", e);
			}

		} finally {
			//-- Siempre borrar el fichero temporal
			tempFile.delete();
		}
	}
	
	/*
	 * Añade una Cofirma a la firma XAdES. Realizará una firma de las mismas características que 
	 * la primera que encuentre (attached o dettached).<br><br>
	 * 
	 * @param manager Dispositivo criptográfico que realizará la cofirma
	 * @param alias Alias donde se encuentra la clave privada dentro del dispositivo
	 * @param documentToSign contenido a firmar. El mismo utilizado en la generación de las otras firmas.
	 * @param digitalSignatureAlgorithm Algoritmo de firma
	 * @param urlTSA URL al servicio de sello de tiempos
	 * @param certStatusRecover Objeto para determinar estado de certificados. Se utiliza
	 * 	a partir del XAdES
	 * @param tipoXAdES Tipo de XAdES tal y como se establece en las constantes de la clase
	 * 	<code>EnumFormatoFirma</code>
	 * @param returnClassObject Clase del objeto respuesta. Debe ser coincidente con lo indicado
	 * 	en el parámetro tipoXAdES
	 * @throws SignatureNotFoundException No existe ninguna firma que cofirmar
	 * @throws NoDocumentToSignException El fichero a firmar no existe o es nulo
	 * @throws HashingException Error realizando el hash del documento
	 * @throws LoadingObjectException No ha sido posible cargar la clave privada o el certificado usados
	 *  para realizar la firma
	 * @throws SignatureException No ha sido posible parsear la firma XAdES o no se puede realizar la cofirma
	 * @throws NoCoincidentDocumentException El documento que se quiere firmar no se corresponde con el de
	 * 	la firma XAdES  
	 */
	protected void coSign (DeviceManager manager, String alias, IDocument documentToSign, String digitalSignatureAlgorithm, 
			URL urlTSA, ICertStatusRecoverer certStatusRecover, EnumFormatoFirma tipoXAdES,	
			Class<?> returnClassObject) throws SignatureNotFoundException, NoDocumentToSignException, 
				HashingException, LoadingObjectException, SignatureException, NoCoincidentDocumentException {
		
		logger.debug("[XAdESSignature.coSign]::Entrada::" + Arrays.asList(new Object[] { manager, alias, documentToSign }));
		
		//-- Obtenemos el nodo donde se encuentra la firma. Cogemos la primera, en principio daria igual la elegida 
		// si todas son multifirmas
		String uriXmlNS = "http://www.w3.org/2000/09/xmldsig#";
		NodeList signatureList = xadesDocument.getElementsByTagNameNS(uriXmlNS, "Signature");
		String signatureParent = null;
		Node nodeSignature = null;
		if (signatureList.getLength() < 1) {
			logger.info("[XAdESSignature.coSign]:: No existe ninguna firma en el XAdES");
			throw new SignatureNotFoundException("No existe ninguna firma en el XAdES");
		} else {
			nodeSignature = signatureList.item(0);
			signatureParent = nodeSignature.getParentNode().getLocalName();			
		}
		
		logger.debug("[XAdESSignature.coSign]:: Obtenido el nodo de firma");
		
		//-- Obtenemos la referencia al documento. Suponemos que es la primera com el id que empiece con Reference-ID
		byte[] hashReference = null;
		boolean attached = false;
		XAdESAttachedNodeToSignObject idToSign = null;
		try {
			XMLSignature firmaDocumento = new XMLSignature((Element)nodeSignature, "./");
			SignedInfo signedInfo = firmaDocumento.getSignedInfo();
			for(int i = 0; i < signedInfo.getLength(); i++){
				Reference ref = signedInfo.item(i);
	            if (ref.getId().startsWith("Reference-ID")) {
	            	String uri = ref.getURI();
	            	if (uri.startsWith("#")) {
						// Sera una firma Attached
	            		logger.debug("[XAdESSignature.coSign]:: Se trata de una firma attached");
	            		attached = true;
	            		// Buscar el nodo firmado
	            		idToSign = new XAdESAttachedNodeToSignObject(uri.substring(1, uri.length()));
	            		
					} else {
						// Sera una firma Dettached
						logger.debug("[XAdESSignature.coSign]:: Se trata de una firma detached");
						if (documentToSign == null) {
							
							//-- Si la referencia es un fichero o una URL, ese sera el documentToSign
							if (uri.startsWith("file:/")) {
								logger.debug("[XAdESSignature.coSign]:: Se trata de una firma detached referencia a un fichero: " + uri);
								File file = new File (uri.substring(6));
								if (file.exists()) {
									try {
										documentToSign = new FileDocument(file);
										logger.debug("[XAdESSignature.coSign]:: El fichero existe: " + file);
									} catch (InitDocumentException e) {
										// Ya hemos comprobado que el documento existe
									}
								} else {
									logger.debug("[XAdESSignature.coSign]:: El fichero no existe: " + file);
								}
							} 
							
							if (uri.startsWith("http://") || uri.startsWith("https://")) {
								logger.debug("[XAdESSignature.coSign]:: Se trata de una firma detached referencia a una URL: " + uri);
								try {
									documentToSign = new URLDocument(new URL (uri));
								} catch (InitDocumentException e) {
									logger.info("[XAdESSignature.coSign]:: No es posible conectarse a la URL: " + uri);
								} catch (MalformedURLException e) {
									logger.info("[XAdESSignature.coSign]:: La URL está mal formada: " + uri);
								}
							}
							
							//-- Si no es ni fichero ni URL, entonces si que es un error
							if (documentToSign == null) {
								throw new NoDocumentToSignException("Es necesario proporcionar un documento ya que la referencia a este no es suficiente");
							}
						}
						
						idToSign = new XAdESAttachedNodeToSignObject(uri);
						hashReference = ref.getDigestValue();
					}
				}    
			}
		} catch (XMLSecurityException e) {
			logger.info("[XAdESSignature.coSign]::Error buscando elementos en el fichero XAdES", e);
			throw new SignatureException("Error buscando elementos en el fichero XAdES", e);
		}
		
		//-- Si es un detached comprobamos que el hash del documento que proporcionamos coincide con el de la firma
		if (hashReference != null) {
			logger.debug("[XAdESSignature.coSign]:: La referencia es un path. Comprobamos que los documentos coincidan");
			try {
				byte[] hashDoc = documentToSign.getHash();
				if (!(new String(hashDoc)).equalsIgnoreCase(new String(hashReference))) {
					throw new NoCoincidentDocumentException("El hash del documento proporcionado no coincide con el de la firma existente");
				}
			} catch (HashingException e) {
				throw new HashingException("No se ha podido calcular el hash del documento");
			}
			
		}
		
		DataToSign dataToSign = null;
		if (attached) {
			dataToSign = createDataToSignAtached(xadesDocument, idToSign, null, certStatusRecover, tipoXAdES, null, null, null, null, null);			
		} else {
			dataToSign = createDataToSignDetached(documentToSign, idToSign.getIdToSign(), certStatusRecover, tipoXAdES, null, null, null, null, null);
		}
		
		// Obtener el certificado en base64
		X509Certificate certificate;
		try {
			logger.debug ("[XAdESSignature.coSign]::obteniendo el certificado");
			certificate = manager.getCertificate(alias);
		} catch (SearchingException e) {
			logger.info("[XAdESSignature.coSign]::No se ha podido encontrar un certificado en el alias'" + alias + "'", e);
			throw new LoadingObjectException ("No se ha podido encontrar un certificado en el alias'" + alias + "'", e);
		}
		
		try {
			FirmaXML firma = new FirmaXML();  
			if (urlTSA != null) { firma.setTSA(urlTSA.toString()); }
			
			logger.debug("[XAdESSignature.coSign]:: Realizando la firma");
			Object[] res = firma.signFile(certificate, dataToSign, manager.getPrivateKey(alias), XAdESUtil.getXAdESDigitalSignatureAlgorithm(digitalSignatureAlgorithm), null);
			Constructor<?> constructor = returnClassObject.getDeclaredConstructor(Document.class);
			XAdESSignature xades = (XAdESSignature)constructor.newInstance((Document) res[0]);
			logger.debug("[XAdESSignature.coSign]:: Firma realizada");
			if (attached) {
				//-- "Refrescar" el document XAdES
				xadesDocument = xades.getDOM();
			} else {
				logger.debug("[XAdESSignature.coSign]:: Añadimos la nueva firma al XAdES");
				//-- Extraer la firma y añadirla al documento original
				NodeList signaturesList = xades.getDOM().getElementsByTagNameNS(uriXmlNS, "Signature");
				//-- Sólo existe una firma que acabamos de crear
				Node signature = signaturesList.item(0);
				
				Element rootDocument = xadesDocument.getDocumentElement();
				if (rootDocument.isEqualNode(xadesDocument.getElementsByTagNameNS(uriXmlNS, "Signature").item(0))) {
					logger.debug("[XAdESSignature.coSign]:: La firma es nodo raiz. Cramos un nuevo nodo raiz Signatures");
					// Si el XML con el XAdES tiene como elemento raiz Signature se crea otro raiz llamado 
					// Signatures para ir añadiendo las firmas
					DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
					DocumentBuilder builder = dbf.newDocumentBuilder();
					
					// Create an empty document
					Document doc = builder.newDocument();
					
					Element root = doc.createElement("Signatures");
					doc.appendChild(root);
					
					// Add a copy of the nodes from existing document
					Node oldSignature = doc.importNode((xadesDocument.getElementsByTagNameNS(uriXmlNS, "Signature").item(0)), true);
					root.appendChild(oldSignature);
					Node newSignature = doc.importNode(signature, true);
					root.appendChild(newSignature);
					
					xadesDocument = doc;
					
				} else {
					// Si el XML con el XAdES NI tiene como elemento raiz Signature se busca el nodo
					// padre de la primera firma y se añade allí la nueva
					NodeList siganturesList = xadesDocument.getElementsByTagNameNS(uriXmlNS, "Signature");
					Node parentNode = siganturesList.item(0).getParentNode();
					Node newSignature = xadesDocument.importNode(signature, true);
					parentNode.appendChild(newSignature);
				}

			}

		} catch (Exception e) {
			logger.info("[XAdESSignature.coSign]::No ha sido posible realizar la firma", e);
			throw new SignatureException ("No ha sido posible realizar la firma", e);
		}
	}
	
	/*
	 * Parsea la fecha en formato xsd:timeFormat
	 */
	protected static Date getXsdDateTimeParse(String sDate) throws ParseException {
		
		Date date = xsdDateTimeFormat.parse(sDate.substring (0,19));
//		int offset = Integer.parseInt(sDate.substring (21,22));
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
//		cal.add(Calendar.HOUR_OF_DAY, offset);
		return cal.getTime();
	}

	//-- TEMPORAL: Métodos copiados de la implementación de FirmaXML del MITyC. Se
	//-- utilizarán para promocionar las firmas XAdES hasta que se implementen éstas
	//-- dentro de la librería.
	
    /**
     * Este método añade la implementación para XADES-T
     * @param doc Documento de firma con formato XADES-BES
     * @param firmaID Identificador del nodo de firma
     * @param selloTiempo Respuesta del servidor TSA con el sello de tiempo en formato binario
     * @return Documento de firma con formato XADES-T
     * @throws Exception
     */
    protected static Document addXadesT(Element firma, String firmaID, byte[] selloTiempo,
    		String namespace) throws Exception    {    	 	

    	Document doc = firma.getOwnerDocument();
    	Element elementoPrincipal = null ;
    	NodeList nodos = firma.getElementsByTagNameNS(DEFAULT_XADES_SCHEMA_URI, ConstantesXADES.QUALIFYING_PROPERTIES);
    	if(nodos.getLength() != 0)
    		elementoPrincipal = (Element)nodos.item(0);
    	else
    		throw new Exception("El XML no parece un XAdES") ;

    	Element propiedadesElementosNoFirmados =
    		doc.createElementNS(DEFAULT_XADES_SCHEMA_URI, namespace + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.UNSIGNED_PROPERTIES);
    	
    	// Creamos los atributos de UnSignedProperties
    	Attr propiedadesNoFirmadasId = doc.createAttributeNS(null, ConstantesXADES.ID);
    	propiedadesNoFirmadasId.setValue(UtilidadTratarNodo.newID(doc, 
    			firmaID  + ConstantesXADES.GUION_UNSIGNED_PROPERTIES));
    	NamedNodeMap atributosSinFirmarPropiedadesElemento =
    		propiedadesElementosNoFirmados.getAttributes();
    	atributosSinFirmarPropiedadesElemento.setNamedItem(propiedadesNoFirmadasId);

    	Element propiedadesSinFirmarFirmaElementos =
    		doc.createElementNS(DEFAULT_XADES_SCHEMA_URI, namespace + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.UNSIGNED_SIGNATURE_PROPERTIES);
    	
    	// Se buscan otros sellos de tiempo en la firma y se les asigna una Id si no la tienen
    	NodeList sellosPreexistentes = doc.getElementsByTagNameNS(DEFAULT_XADES_SCHEMA_URI, ConstantesXADES.SIGNATURE_TIME_STAMP);
    	int numSellos = sellosPreexistentes.getLength();
    	ArrayList<String> idNodoSelloTiempo = new ArrayList<String>();
    	for (int i = 0; i < numSellos; ++i) {
    		Element sello = (Element) sellosPreexistentes.item(i);
    		String selloId = sello.getAttribute(ConstantesXADES.ID);
    		if (selloId == null) {
    			Attr informacionElementoSigTimeStamp = doc.createAttributeNS(null, ConstantesXADES.ID);
    			selloId = UtilidadTratarNodo.newID(doc, ConstantesXADES.SELLO_TIEMPO);
    	    	informacionElementoSigTimeStamp.setValue(selloId);
    	    	sello.getAttributes().setNamedItem(informacionElementoSigTimeStamp);
    		}
    		// Se almacena su nombre de Id por si es preciso referenciarlos
    		idNodoSelloTiempo.add(selloId); 		
    	}
    	
    	// Se crea el nodo de sello de tiempo
    	Element tiempoSelloElementoFirma =
    		doc.createElementNS(DEFAULT_XADES_SCHEMA_URI, namespace + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.SIGNATURE_TIME_STAMP);
	
    	// Se escribe una Id única
    	Attr informacionElementoSigTimeStamp = doc.createAttributeNS(null, ConstantesXADES.ID);
    	String idSelloTiempo = UtilidadTratarNodo.newID(doc, ConstantesXADES.SELLO_TIEMPO);
    	informacionElementoSigTimeStamp.setValue(idSelloTiempo);
    	idNodoSelloTiempo.add(idSelloTiempo);
    	tiempoSelloElementoFirma.getAttributes().setNamedItem(informacionElementoSigTimeStamp);

    	// Se incluye un nodo que referencia a la Id de SignatureValue
    	if (ConstantesXADES.SCHEMA_XADES_111.equals(DEFAULT_XADES_SCHEMA_URI) 
    			|| ConstantesXADES.SCHEMA_XADES_122.equals(DEFAULT_XADES_SCHEMA_URI)) {
    		
    		String nombreNodoUri = null;
    		String tipoUri = null;
    		if (ConstantesXADES.SCHEMA_XADES_111.equals(DEFAULT_XADES_SCHEMA_URI)) {
    			nombreNodoUri = ConstantesXADES.HASH_DATA_INFO;
    			tipoUri = ConstantesXADES.URI_MINUS;
    		} else {
    			nombreNodoUri = ConstantesXADES.INCLUDE;
    			tipoUri = ConstantesXADES.URI_MAYUS;
    		}
    		
    		ArrayList<Element> listElements = UtilidadTratarNodo.obtenerNodos(firma, 2, new NombreNodo(ConstantesXADES.SCHEMA_DSIG, ConstantesXADES.SIGNATURE_VALUE));
    		if (listElements.size() != 1) {
    			logger.info("No se encuentra el tag de firma en el XML"); 
          		throw new Exception("No se encuentra el tag de firma en el XML");
    		}
    		String idSignatureValue = listElements.get(0).getAttribute(ConstantesXADES.ID);

    		
        	Element informacionElementoHashDatos = doc.createElementNS(DEFAULT_XADES_SCHEMA_URI, namespace + ConstantesXADES.DOS_PUNTOS + nombreNodoUri);
    		
    		Attr informacionElementoHashDatosUri = doc.createAttributeNS(null, tipoUri);
    		informacionElementoHashDatosUri.setValue(ConstantesXADES.ALMOHADILLA + idSignatureValue);

    		NamedNodeMap informacionAtributosElementoHashDatos = informacionElementoHashDatos.getAttributes();
    		informacionAtributosElementoHashDatos.setNamedItem(informacionElementoHashDatosUri);

    		tiempoSelloElementoFirma.appendChild(informacionElementoHashDatos) ;
    	}
    	
    	// Se crea el nodo canonicalizationMethod en los esquemas 1.2.2 y 1.3.2
    	if (!ConstantesXADES.SCHEMA_XADES_111.equals(DEFAULT_XADES_SCHEMA_URI)) {
    		Element canonicalizationElemento = doc.createElementNS(ConstantesXADES.SCHEMA_DSIG, xmldsigNS + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.CANONICALIZATION_METHOD);		
    		Attr canonicalizationAttribute = doc.createAttributeNS(null, ConstantesXADES.ALGORITHM);
    		canonicalizationAttribute.setValue(Transforms.TRANSFORM_C14N_OMIT_COMMENTS);
    		canonicalizationElemento.getAttributes().setNamedItem(canonicalizationAttribute);

    		tiempoSelloElementoFirma.appendChild(canonicalizationElemento);
    	}
		
		// Se crea el nodo del sello de tiempo
		Element tiempoSelloEncapsulado =
    		doc.createElementNS(DEFAULT_XADES_SCHEMA_URI, namespace + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.ENCAPSULATED_TIME_STAMP);

    	tiempoSelloEncapsulado.appendChild(
    			doc.createTextNode(new String(Base64Coder.encode(selloTiempo))));
    	Attr tiempoSelloEncapsuladoId = doc.createAttributeNS(null, ConstantesXADES.ID);
    	String idEncapsulated = UtilidadTratarNodo.newID(doc, ConstantesXADES.SELLO_TIEMPO_TOKEN);
    	tiempoSelloEncapsuladoId.setValue(idEncapsulated);
    	tiempoSelloEncapsulado.getAttributes().setNamedItem(tiempoSelloEncapsuladoId);
    	
    	
    	tiempoSelloElementoFirma.appendChild(tiempoSelloEncapsulado);

    	propiedadesSinFirmarFirmaElementos.appendChild(tiempoSelloElementoFirma);
    	propiedadesElementosNoFirmados.appendChild(propiedadesSinFirmarFirmaElementos);
    	elementoPrincipal.appendChild(propiedadesElementosNoFirmados);
    	return doc;
    }

    /**
     * Este método añade la implementacion para XADES-C
     */
    protected static Document addXadesC(Element firma, ArrayList<RespYCerts> respuestasFirma,
    		ArrayList<RespYCerts> respuestasSello, XAdESSchemas schema, String algDigestXML, 
    		String namespace) throws Exception {
    	Document doc = firma.getOwnerDocument();
    	// Recogemos el nodo UnsignedSignatureProperties del cual dependen los nodos
    	// que hay que añadir para completar la firma XADES-C
    	Element elementoPrincipal = null ;

		String tipoUri = null;
		if (ConstantesXADES.SCHEMA_XADES_111.equals(DEFAULT_XADES_SCHEMA_URI)) {
			tipoUri = ConstantesXADES.URI_MINUS;
		} else {
			tipoUri = ConstantesXADES.URI_MAYUS;
		}
    	
    	NodeList nodos = firma.getElementsByTagNameNS(DEFAULT_XADES_SCHEMA_URI, ConstantesXADES.UNSIGNED_SIGNATURE_PROPERTIES);
    	if(nodos.getLength() != 0)
    	{
    		elementoPrincipal = (Element)nodos.item(0);
        }
        else
        {
        	throw new Exception("El XML no parece un XAdES") ;
        }
        
        // Aqui vienen las llamadas para los certificados
        Element certificadosElementosFirma =
                doc.createElementNS(DEFAULT_XADES_SCHEMA_URI, namespace + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.COMPLETE_CERTIFICATE_REFS);
        Element revocacionesElementoFirma =
            doc.createElementNS(DEFAULT_XADES_SCHEMA_URI, namespace + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.COMPLETE_REVOCATION_REFS);

        if(respuestasFirma != null && !respuestasFirma.isEmpty()) {
        	
        	// Se le agrega una Id única
        	Attr informacionElementoCertRef = doc.createAttributeNS(null, ConstantesXADES.ID);
        	String idNodoCertificateRefs = UtilidadTratarNodo.newID(doc, ConstantesXADES.COMPLETE_CERTIFICATE_REFS);
        	informacionElementoCertRef.setValue(idNodoCertificateRefs);
        	certificadosElementosFirma.getAttributes().setNamedItem(informacionElementoCertRef);
        	
        	Element elementoCertRefs =
                doc.createElementNS(DEFAULT_XADES_SCHEMA_URI, namespace + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.CERT_REFS);

        	certificadosElementosFirma.appendChild(elementoCertRefs);
        	
        	Set<X509Certificate> setCertificadosIncluidos = new HashSet<X509Certificate>();
        	ArrayList[] listas = new ArrayList[] { respuestasFirma, respuestasSello };
        	boolean primero = true;
        	for (int x=0;x<2;x++) {
        	
        		ArrayList<RespYCerts> respuestas = listas[x];
	        	
	        	int i = -1;
	        	for (RespYCerts respYCerts : respuestas) {
	        		i++;
	        		
	            	// Se agrega una id al certificado
	            	String idNueva = UtilidadTratarNodo.newID(doc, ConstantesXADES.LIBRERIAXADES_CERT_PATH);
	            	respuestas.get(i).setIdCertificado(idNueva);
	            	
	            	if (primero) {
	            		primero = false;
	            		continue;
	            	}
	        		
	        		X509Certificate firmaCertificado = respYCerts.getCertstatus().getCertificate();
	        		if (setCertificadosIncluidos.contains(firmaCertificado)) {
	        			continue;
	        		}
	        		setCertificadosIncluidos.add(firmaCertificado);
	        		
	        		Element elementCertRef = doc.createElementNS(DEFAULT_XADES_SCHEMA_URI, namespace + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.CERT);
	        		
	        		// Creamos los atributos de UnSignedProperties
	            	Attr uris = doc.createAttributeNS(null, tipoUri);
					// AppPerfect: Falso positivo. No son expresiones constantes
	            	idNueva = UtilidadTratarNodo.newID(doc, ConstantesXADES.LIBRERIAXADES_CERT_PATH);
	            	uris.setValue( ConstantesXADES.ALMOHADILLA + idNueva );
	            	respuestas.get(i).setIdCertificado(idNueva);
	            	NamedNodeMap atributosURI = elementCertRef.getAttributes();
	            	atributosURI.setNamedItem(uris);
	
	    	        Element resumenElementoCert = doc.createElementNS(DEFAULT_XADES_SCHEMA_URI, namespace + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.CERT_DIGEST);
	
	    	        // Creamos el xades:DigestMethod
	    	        Element metodoResumenElemento = doc.createElementNS(ConstantesXADES.SCHEMA_DSIG, xmldsigNS + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.DIGEST_METHOD);
	
	    	        // Creamos los atributos de DigestMethod
	    	        Attr propiedadesFirmaAlgoritmo = doc.createAttributeNS(null, ConstantesXADES.ALGORITHM);
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
	    	            byte[] resumenMensajeByte =resumenCertificadoTemp.digest(firmaCertificado.getEncoded());
	    	            resumenCertificado = new String(Base64Coder.encode(resumenMensajeByte));
	    	        } catch (CertificateEncodingException e) {
	    	        	logger.error(e);
	    	        	throw new Exception("Error obteniendo la huella del certificado");				
	    	        }
	
	    	        Element elementDigestValue =
	    	                doc.createElementNS(ConstantesXADES.SCHEMA_DSIG, xmldsigNS + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.DIGEST_VALUE);
	    	        elementDigestValue.appendChild(
	    	                doc.createTextNode(resumenCertificado));
	
	    	        // Creamos el xades:IssuerSerial
	    	        Element elementoEmisorSerial =
	    	                doc.createElementNS(DEFAULT_XADES_SCHEMA_URI, namespace + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.ISSUER_SERIAL);
	    	        // Creamos el xades:X509IssuerName
	    	        Element elementoX509EmisorNombre =
	    	                doc.createElementNS(ConstantesXADES.SCHEMA_DSIG, xmldsigNS + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.X_509_ISSUER_NAME);
	    	        elementoX509EmisorNombre.appendChild(
	    	                doc.createTextNode(firmaCertificado.getIssuerX500Principal().getName()));
	
	    	        // Creamos el xades:X509SerialNumber
	    	        Element elementoX509NumeroSerial =
	    	                doc.createElementNS(ConstantesXADES.SCHEMA_DSIG, xmldsigNS + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.X_509_SERIAL_NUMBER);
	    	        elementoX509NumeroSerial.appendChild(
	    	                doc.createTextNode(firmaCertificado.getSerialNumber().toString()));
	
	    	        //Add references
	    	        elementoEmisorSerial.appendChild(elementoX509EmisorNombre);
	    	        elementoEmisorSerial.appendChild(elementoX509NumeroSerial);
	
	    	        resumenElementoCert.appendChild(metodoResumenElemento);
	    	        resumenElementoCert.appendChild(elementDigestValue);
	
	    	        elementCertRef.appendChild(resumenElementoCert);
	    	        elementCertRef.appendChild(elementoEmisorSerial);
	
	    	        elementoCertRefs.appendChild(elementCertRef);
	        	}
        	}
        }

    	
    	
    	Element elementOCSPRef = null;
    	String tiempoRespuesta = null;
    	byte[] mensajeRespuesta = null;
    	
        if(respuestasFirma != null && !respuestasFirma.isEmpty()) {
        	
        	// Se le agrega una Id única
        	Attr informacionElementoCertRef = doc.createAttributeNS(null, ConstantesXADES.ID);
        	String idNodoRevocationRefs = UtilidadTratarNodo.newID(doc, ConstantesXADES.COMPLETE_REVOCATION_REFS);
        	informacionElementoCertRef.setValue(idNodoRevocationRefs);
        	revocacionesElementoFirma.getAttributes().setNamedItem(informacionElementoCertRef);
        	
        	int nOCSPRefs = 0;
        	int nCRLRefs = 0;
        	
            // Construye el valor de la respuesta del servidor OCSP
            // bajo el nodo completo de la referencia de la revocación
        	Element elementOCSPRefs =
                doc.createElementNS(DEFAULT_XADES_SCHEMA_URI, namespace + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.OCSP_REFS);
        	CRLRefs elementCRLRefs = new CRLRefs(schema); 
        	
        	Set<X509Certificate> setCertificadosIncluidos = new HashSet<X509Certificate>();
        	ArrayList[] listas = new ArrayList[] { respuestasFirma, respuestasSello };
        	for (int x=0;x<2;x++) {
        	
        		ArrayList<RespYCerts> respuestas = listas[x];
	        	
	        	for (RespYCerts respYCerts : respuestas) {
	        		// que no se repitan
	        		X509Certificate firmaCertificado = respYCerts.getCertstatus().getCertificate();
	        		if (setCertificadosIncluidos.contains(firmaCertificado)) {
	        			continue;
	        		}
	        		setCertificadosIncluidos.add(firmaCertificado);
	        		
	        		ICertStatus certStatus = respYCerts.getCertstatus();
	        		if (certStatus instanceof IOCSPCertStatus) {
	        			nOCSPRefs++;
	        			IOCSPCertStatus respOcsp = (IOCSPCertStatus) certStatus;
	
		        		tiempoRespuesta = UtilidadFechas.formatFechaXML(respOcsp.getResponseDate());
		        		IOCSPCertStatus.TYPE_RESPONDER tipoResponder = respOcsp.getResponderType();
		        		String valorResponder = respOcsp.getResponderID();
		        		mensajeRespuesta = respOcsp.getEncoded();
		
		        		elementOCSPRef = doc.createElementNS(DEFAULT_XADES_SCHEMA_URI, namespace + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.OCSP_REF);
		        		
		        		// Creamos los atributos de UnSignedProperties
		        		String idNueva = UtilidadTratarNodo.newID(doc, ConstantesXADES.OCSP);
		        		respYCerts.setIdRespStatus(idNueva);
		
		            	Element identificadorElementoOCSP = doc.createElementNS(DEFAULT_XADES_SCHEMA_URI, namespace + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.OCSP_IDENTIFIER);
		            	Attr uris = doc.createAttributeNS(null, tipoUri);
		            	uris.setValue( ConstantesXADES.ALMOHADILLA + idNueva );
		            	NamedNodeMap atributosURI = identificadorElementoOCSP.getAttributes();
		            	atributosURI.setNamedItem(uris);
		
		        		// Creamos el xades:DigestMethod
		        		Element elementoRespondedorId = doc.createElementNS(DEFAULT_XADES_SCHEMA_URI, namespace + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.RESPONDER_ID);
		
		        		
		        		Element responderFinal = elementoRespondedorId;
		        		if (!(ConstantesXADES.SCHEMA_XADES_111.equals(DEFAULT_XADES_SCHEMA_URI)) && !(ConstantesXADES.SCHEMA_XADES_122.equals(DEFAULT_XADES_SCHEMA_URI))) {
		        			Element hijo = null;
		        			if (tipoResponder.equals(IOCSPCertStatus.TYPE_RESPONDER.BY_NAME)) {
		        				hijo = doc.createElementNS(DEFAULT_XADES_SCHEMA_URI, namespace + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.BY_NAME);
		        			}
		        			else {
		        				hijo = doc.createElementNS(DEFAULT_XADES_SCHEMA_URI, namespace + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.BY_KEY);
		        			}
		        			// TODO: tener en cuenta que podria no ser ninguno de estos valores en un futuro
		            		elementoRespondedorId.appendChild(hijo);
		            		responderFinal = hijo;
		        		}
		        		responderFinal.appendChild(doc.createTextNode(valorResponder));
		    			
		
		        		Element elementoProdujoEn = doc.createElementNS(DEFAULT_XADES_SCHEMA_URI, namespace + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.PRODUCE_AT);
		
		
		        		elementoProdujoEn.appendChild(doc.createTextNode(tiempoRespuesta));
		
		        		identificadorElementoOCSP.appendChild(elementoRespondedorId);
		        		identificadorElementoOCSP.appendChild(elementoProdujoEn);
		        		Element valorYResumenElemento = doc.createElementNS(DEFAULT_XADES_SCHEMA_URI, namespace + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.DIGEST_ALG_AND_VALUE);
		
		        		// Creamos el xades:DigestMethod
		        		Element metodoResumenElemento = doc.createElementNS(ConstantesXADES.SCHEMA_DSIG, xmldsigNS + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.DIGEST_METHOD);
		
		        		// Creamos los atributos de DigestMethod
		        		Attr propiedadesAlgoritmoFirmado = doc.createAttributeNS(null, ConstantesXADES.ALGORITHM);
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
		
		        		Element valorResumenElemento = doc.createElementNS(ConstantesXADES.SCHEMA_DSIG, xmldsigNS + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.DIGEST_VALUE);
		
		        		valorResumenElemento.appendChild(doc.createTextNode(digestCertificado));
		
		        		valorYResumenElemento.appendChild(metodoResumenElemento);
		        		valorYResumenElemento.appendChild(valorResumenElemento);
		
		        		elementOCSPRef.appendChild(identificadorElementoOCSP);
		        		elementOCSPRef.appendChild(valorYResumenElemento);
		
		        		elementOCSPRefs.appendChild(elementOCSPRef);
	        		}
	        		else if (certStatus instanceof IX509CRLCertStatus) {
	        			nCRLRefs++;
	        			IX509CRLCertStatus respCRL = (IX509CRLCertStatus) certStatus;
	        			try {
							CRLRef crlRef = new CRLRef(schema, algDigestXML, respCRL.getX509CRL());
	
							String idNueva = UtilidadTratarNodo.newID(doc, ConstantesXADES.CRL);
							crlRef.getCrlIdentifier().setUri(ConstantesXADES.ALMOHADILLA + idNueva);
							respYCerts.setIdRespStatus(idNueva);
			
							elementCRLRefs.addCRLRef(crlRef);
	        			} catch (InvalidInfoNodeException ex) {
			    			throw new Exception("No se pudo construir las referencias a CRLs", ex);
						}
	        		}
	        	}
        	}

        	if (nCRLRefs > 0) {
        		try {
					Element el = elementCRLRefs.createElement(doc, xmldsigNS, namespace);
					revocacionesElementoFirma.appendChild(el);
				} catch (InvalidInfoNodeException ex) {
	    			throw new Exception("No se pudo construir las referencias a CRLs", ex);
				}
        	}
        	
        	if (nOCSPRefs > 0)
            	revocacionesElementoFirma.appendChild(elementOCSPRefs);
        	
        }
        
        elementoPrincipal.appendChild(certificadosElementosFirma);
        elementoPrincipal.appendChild(revocacionesElementoFirma);

        return doc;
    }

	/**
     * Este metodo añade la implementación del sello de tiempo de tipo 1 (implícito) para 
     * XADES-X según los esquemas 1.2.2 y 1.3.2.
     * Los elementos sobre los que se calcula el sello son los siguientes:
	 * 		- SignatureValue
	 * 		- SignatureTimestamp
	 * 		- CompleteCertificateRefs
	 * 		- CompleteRevocationRefs
	 * 	Opcionalmente en el esquema 1.2.2 y 1.3.2:
	 * 		- AttributeCertificateRefs
	 * 		- AttributeRevocationRefs
	 * 
     * @param Element UnsignedSignatureProperties Nodo a partir del cual se añade el nodo SigAndRefsTimeStamp
     * @param servidorTSA Servidor RSA
     * @param algoritmoTSA Algoritmo de hash
     * @param namespace Espacio de nombres
     * @return Documento de firma con formato XADES-X
     * @throws Exception En caso de error
     */
    protected static Document addXadesX(Element unsignedSignatureProperties, URL servidorTSA, 
    		String algoritmoTSA, String namespace) throws Exception {
    	
    	// Se obtiene el documento que contiene al nodo UnsignedSignatureProperties
    	Document doc = unsignedSignatureProperties.getOwnerDocument();
    	
    	// Se crea el nodo SigAndRefsTimeStamp
        Element sigAndRefsTimeStampElement =
        	doc.createElementNS(DEFAULT_XADES_SCHEMA_URI, namespace + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.SIG_AND_REFS_TIME_STAMP);
        
        // Se escribe una Id única
    	Attr informacionElementoSigTimeStamp = doc.createAttributeNS(null, ConstantesXADES.ID);
    	String idSelloTiempo = UtilidadTratarNodo.newID(doc, ConstantesXADES.SELLO_TIEMPO);
    	informacionElementoSigTimeStamp.setValue(idSelloTiempo);
    	ArrayList<String> idNodoSelloTiempo = new ArrayList<String>();
    	idNodoSelloTiempo.add(idSelloTiempo);
    	sigAndRefsTimeStampElement.getAttributes().setNamedItem(informacionElementoSigTimeStamp);
        
        // Se coloca el nodo creado al final del nodo UnsignedSignatureProperties
        unsignedSignatureProperties.appendChild(sigAndRefsTimeStampElement);
        
        return addXadesX(unsignedSignatureProperties, sigAndRefsTimeStampElement, servidorTSA, algoritmoTSA, namespace);
    }
    
	/**
     * Este metodo añade la implementación del sello de tiempo de tipo 1 (implícito) para 
     * XADES-X según los esquemas 1.2.2 y 1.3.2.
     * Los elementos sobre los que se calcula el sello son los siguientes:
	 * 		- SignatureValue
	 * 		- SignatureTimestamp
	 * 		- CompleteCertificateRefs
	 * 		- CompleteRevocationRefs
	 * 	Opcionalmente en el esquema 1.2.2 y 1.3.2:
	 * 		- AttributeCertificateRefs
	 * 		- AttributeRevocationRefs
	 * 
     * @param unsignedSignatureProperties Nodo a partir del cual se añade el nodo SigAndRefsTimeStamp
     * @param sigAndRefsTimeStampElement Elemento en el que se incluye el sello
     * @param servidorTSA Servidor RSA
     * @param algoritmoTSA Algoritmo de hash
     * @param namespace Espacio de nombres
     * @return Documento de firma con formato XADES-X
     * @throws Exception En caso de error
     */
    protected static Document addXadesX(Element unsignedSignatureProperties, Element sigAndRefsTimeStampElement,
    		URL servidorTSA, String algoritmoTSA, String namespace) throws Exception {
    	
    	// Se obtiene el formato de la constante URI en función del esquema
		String tipoUri = null;
		String nombreNodoUri = null;
		if (ConstantesXADES.SCHEMA_XADES_111.equals(DEFAULT_XADES_SCHEMA_URI)){
			nombreNodoUri = ConstantesXADES.HASH_DATA_INFO;
			tipoUri = ConstantesXADES.URI_MINUS;
		} else {
			nombreNodoUri = ConstantesXADES.INCLUDE;
			tipoUri = ConstantesXADES.URI_MAYUS;
		}
    	
    	// Se obtiene el documento que contiene al nodo UnsignedSignatureProperties
    	Document doc = unsignedSignatureProperties.getOwnerDocument();
    	
    	// Se obtiene el nodo Signature que contiene al nodo UnsignedSignatureProperties (es el 4º padre, según esquema XAdES)
    	Node padre = unsignedSignatureProperties.getParentNode();
    	for (int i = 0; i < 3; ++i) {
    		if (padre != null)
    			padre = padre.getParentNode();
    		else
    			// No se encuentra el nodo Signature
    			throw new Exception("No se encuentra el nodo signature");
    	}
    	
    	Element signatureElement = null;
    	if (padre != null && ConstantesXADES.SIGNATURE.equals(padre.getLocalName()))
    		signatureElement = (Element)padre;
    	else
    		// No se encuentra el nodo Signature
    		throw new Exception("No se encuentra el nodo signature");
    	 
        
        // Se obtiene el listado de elementos de un sello de tiempo XAdES X de tipo 1
        ArrayList<Element> elementosSelloX = null;
        try {
			elementosSelloX = UtilidadXadesX.obtenerListadoXADESX1imp(DEFAULT_XADES_SCHEMA_URI, signatureElement, sigAndRefsTimeStampElement);
		} catch (BadFormedSignatureException e) {
			throw new Exception(e.getMessage(), e);
		} catch (FirmaXMLError e) {
			throw new Exception(e.getMessage(), e);
		}
		
		// Se añaden nodos de referencia a los nodos obtenidos para el cálculo del sello (sólo para esquemas 1.2.2 y 1.1.1)
		if (ConstantesXADES.SCHEMA_XADES_111.equals(DEFAULT_XADES_SCHEMA_URI) ||
				ConstantesXADES.SCHEMA_XADES_122.equals(DEFAULT_XADES_SCHEMA_URI)) {
			// Se obtienen las Ids de los nodos del sello de tiempo X
			ArrayList<String> elementosIdSelloX = UtilidadTratarNodo.obtenerIDs(elementosSelloX);
			
			// Se crea una estructura con los nodos Include (1.2.2) o HashDataInfo (1.1.1) que contienen las URIs que apuntan a estas IDs
			ArrayList<Element> nodosUriReferencia = new ArrayList<Element> (elementosIdSelloX.size());
			Iterator<String> itIds = elementosIdSelloX.iterator();
			while (itIds.hasNext()) {
				String id = itIds.next();
				Element uriNode = 
					doc.createElementNS(DEFAULT_XADES_SCHEMA_URI, namespace + ConstantesXADES.DOS_PUNTOS + nombreNodoUri);
	        	Attr includeNodeUri = doc.createAttributeNS(null, tipoUri);
	        	includeNodeUri.setValue(ConstantesXADES.ALMOHADILLA + id);
	        	NamedNodeMap atributosNodo = uriNode.getAttributes();
	        	atributosNodo.setNamedItem(includeNodeUri);
	        	
	        	nodosUriReferencia.add(uriNode);
			}
			
	        // Se escribe en el nodo SigAndRefsTimeStamp el listado obtenido por orden
			Iterator<Element> itUrisReferencia = nodosUriReferencia.iterator();
			while (itUrisReferencia.hasNext()) {
				Element includeNode = itUrisReferencia.next();			
				sigAndRefsTimeStampElement.appendChild(includeNode);
			}
		}
		
		// Se obtiene el Array de bytes de los nodos obtenidos
		byte[] byteData = null;
		try {
			byteData = UtilidadTratarNodo.obtenerByte(elementosSelloX, CanonicalizationEnum.C14N_OMIT_COMMENTS);
		} catch (FirmaXMLError e) {
			throw new Exception(e.getMessage(), e);
		}
		
		// Calculamos el hash del sello de tiempo y lo escribimos en el nodo como String del array de bytes calculado
		TSCliente tsCli = null;
//        if(estadoProxy) {
//			System.setProperty("http.proxyHost", servidorProxy);
//			System.setProperty("http.proxyPort", Integer.toString(numeroPuertoProxy));
//			if (isProxyAuth) {
//				Authenticator.setDefault(new SimpleAuthenticator(proxyUser, proxyPass));
//			} 
//			else {
//				Authenticator.setDefault(null);
//			}
//        } 
		
		TimeStamp timeStamp = TimeStamp.stampDocument(byteData, servidorTSA);
		String hashSelloX = Util.encodeBase64(timeStamp.toDER());

//        tsCli = new TSCliente(servidorTSA.toString(),algoritmoTSA);
//        
//        try {
//			byteData = tsCli.generarSelloTiempo(byteData);
//			Util.saveFile(new File ("c:/temp/ts.der"), byteData);
//		} catch (TSClienteError e) {
//			throw new Exception("Error generando sello de tiempos", e) ;
//		}
//		String hashSelloX = new String(Base64Coder.encode(byteData));
		
		// Se crea el nodo canonicalizationMethod en los esquemas 1.2.2 y 1.3.2
    	if (!ConstantesXADES.SCHEMA_XADES_111.equals(DEFAULT_XADES_SCHEMA_URI)) {
    		Element canonicalizationElemento = doc.createElementNS(ConstantesXADES.SCHEMA_DSIG, xmldsigNS + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.CANONICALIZATION_METHOD);		
    		Attr canonicalizationAttribute = doc.createAttributeNS(null, ConstantesXADES.ALGORITHM);
    		canonicalizationAttribute.setValue(Transforms.TRANSFORM_C14N_OMIT_COMMENTS);
    		canonicalizationElemento.getAttributes().setNamedItem(canonicalizationAttribute);

    		sigAndRefsTimeStampElement.appendChild(canonicalizationElemento);
    	}
		
		// Escribimos el resultado en el nodo EncapsulatedTimeStamp
		Element encapsulatedTimeStampNode = 
			doc.createElementNS(DEFAULT_XADES_SCHEMA_URI, namespace + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.ENCAPSULATED_TIME_STAMP);
		encapsulatedTimeStampNode.appendChild(doc.createTextNode(hashSelloX));	
		sigAndRefsTimeStampElement.appendChild(encapsulatedTimeStampNode);

        return doc;
    }
    
    /**
     * Este metodo añade la implementación del sello de tiempo de tipo 2 (explícito) para 
     * XADES-X según los esquemas 1.1.1, 1.2.2 y 1.3.2.
     * Los elementos sobre los que se calcula el sello son los siguientes:
	 * 		- CompleteCertificateRefs
	 * 		- CompleteRevocationRefs
	 * 	Opcionalmente en el esquema 1.2.2 y 1.3.2:
	 * 		- AttributeCertificateRefs
	 * 		- AttributeRevocationRefs
	 * 
     * @param Element UnsignedSignatureProperties Nodo a partir del cual se añade el nodo RefsOnlyTimeStamp
     * @return Documento de firma con formato XADES-X
     * @throws Exception En caso de error
     */
    protected static Document addXadesX2(Element UnsignedSignatureProperties, String servidorTSA, 
    		String algoritmoTSA, String namespace) throws Exception {
    	// Se obtiene el formato de la constante URI en función del esquema
		String tipoUri = null;
		String nombreNodoUri = null;
		if (ConstantesXADES.SCHEMA_XADES_111.equals(DEFAULT_XADES_SCHEMA_URI)){
			nombreNodoUri = ConstantesXADES.HASH_DATA_INFO;
			tipoUri = ConstantesXADES.URI_MINUS;
		} else {
			nombreNodoUri = ConstantesXADES.INCLUDE;
			tipoUri = ConstantesXADES.URI_MAYUS;
		}
    	
    	// Se obtiene el documento que contiene al nodo UnsignedSignatureProperties
    	Document doc = UnsignedSignatureProperties.getOwnerDocument();
    	
    	// Se obtiene el nodo Signature que contiene al nodo UnsignedSignatureProperties (es el 4º padre, según esquema XAdES)
    	Node padre = UnsignedSignatureProperties.getParentNode();
    	for (int i = 0; i < 3; ++i) {
    		if (padre != null)
    			padre = padre.getParentNode();
    		else
    			// No se encuentra el nodo Signature
    			throw new Exception("No se encuentra el nodo Signature");
    	}
    	
    	Element signatureElement = null;
    	if (padre != null && ConstantesXADES.SIGNATURE.equals(padre.getLocalName()))
    		signatureElement = (Element)padre;
    	else
    		// No se encuentra el nodo Signature
    		throw new Exception("No se encuentra el nodo Signature");
    	 
    	// Se crea el nodo RefsOnlyTimeStamp
        Element refsOnlyTimeStampElement =
        	doc.createElementNS(DEFAULT_XADES_SCHEMA_URI, namespace + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.REFS_ONLY_TIME_STAMP);
        
        // Se escribe una Id única
    	Attr informacionElementoSigTimeStamp = doc.createAttributeNS(null, ConstantesXADES.ID);
    	String idSelloTiempo = UtilidadTratarNodo.newID(doc, ConstantesXADES.SELLO_TIEMPO);
    	informacionElementoSigTimeStamp.setValue(idSelloTiempo);
    	ArrayList<String> idNodoSelloTiempo = new ArrayList<String>();
    	idNodoSelloTiempo.add(idSelloTiempo);
    	refsOnlyTimeStampElement.getAttributes().setNamedItem(informacionElementoSigTimeStamp);
        
        // Se coloca el nodo creado al final del nodo UnsignedSignatureProperties
        UnsignedSignatureProperties.appendChild(refsOnlyTimeStampElement);
        
        // Se obtiene el listado de elementos de un sello de tiempo XAdES X de tipo 2
        ArrayList<Element> elementosSelloX = null;
        try {
			elementosSelloX = UtilidadXadesX.obtenerListadoXADESX2exp(DEFAULT_XADES_SCHEMA_URI, signatureElement, refsOnlyTimeStampElement);
		} catch (BadFormedSignatureException e) {
			throw new Exception(e.getMessage(), e);
		} catch (FirmaXMLError e) {
			throw new Exception(e.getMessage(), e);
		}
		
		// Se añaden nodos de referencia a los nodos obtenidos para el cálculo del sello (sólo para esquemas 1.2.2 y 1.1.1)
		if (ConstantesXADES.SCHEMA_XADES_111.equals(DEFAULT_XADES_SCHEMA_URI) ||
				ConstantesXADES.SCHEMA_XADES_122.equals(DEFAULT_XADES_SCHEMA_URI)) {
			// Se obtienen las Ids de los nodos del sello de tiempo X
			ArrayList<String> elementosIdSelloX = UtilidadTratarNodo.obtenerIDs(elementosSelloX);
			
			// Se crea una estructura con los nodos Include (1.2.2) o HashDataInfo (1.1.1) que contienen las URIs que apuntan a estas IDs
			ArrayList<Element> nodosUriReferencia = new ArrayList<Element> (elementosIdSelloX.size());
			Iterator<String> itIds = elementosIdSelloX.iterator();
			while (itIds.hasNext()) {
				String id = itIds.next();
				Element uriNode = 
					doc.createElementNS(DEFAULT_XADES_SCHEMA_URI, namespace + ConstantesXADES.DOS_PUNTOS + nombreNodoUri);
	        	Attr includeNodeUri = doc.createAttributeNS(null, tipoUri);
	        	includeNodeUri.setValue(ConstantesXADES.ALMOHADILLA + id);
	        	NamedNodeMap atributosNodo = uriNode.getAttributes();
	        	atributosNodo.setNamedItem(includeNodeUri);
	        	
	        	nodosUriReferencia.add(uriNode);
			}
			
	        // Se escribe en el nodo RefsOnlyTimeStamp el listado obtenido, por orden
			Iterator<Element> itUrisReferencia = nodosUriReferencia.iterator();
			while (itUrisReferencia.hasNext()) {
				Element includeNode = itUrisReferencia.next();			
				refsOnlyTimeStampElement.appendChild(includeNode);
			}
		}
		
		// Se obtiene el Array de bytes de los nodos obtenidos
		byte[] byteData = null;
		try {
			byteData = UtilidadTratarNodo.obtenerByte(elementosSelloX, CanonicalizationEnum.C14N_OMIT_COMMENTS);
		} catch (FirmaXMLError e) {
			throw new Exception(e.getMessage(), e);
		}
		
		// Calculamos el hash del sello de tiempo y lo escribimos en el nodo como String del array de bytes calculado
		TSCliente tsCli = null;
//        if(estadoProxy) {
//			System.setProperty("http.proxyHost", servidorProxy);
//			System.setProperty("http.proxyPort", Integer.toString(numeroPuertoProxy));
//			if (isProxyAuth) {
//				Authenticator.setDefault(new SimpleAuthenticator(proxyUser, proxyPass));
//			} 
//			else {
//				Authenticator.setDefault(null);
//			}
//        } 
        tsCli = new TSCliente(servidorTSA,algoritmoTSA);
        
        try {
			byteData = tsCli.generarSelloTiempo(byteData);
		} catch (TSClienteError e) {
			throw new Exception("No se puede generar el sello de tiempos", e) ;
		}
		String hashSelloX = new String(Base64Coder.encode(byteData));
		
		// Se crea el nodo canonicalizationMethod en los esquemas 1.2.2 y 1.3.2
    	if (!ConstantesXADES.SCHEMA_XADES_111.equals(DEFAULT_XADES_SCHEMA_URI)) {
    		Element canonicalizationElemento = doc.createElementNS(ConstantesXADES.SCHEMA_DSIG, xmldsigNS + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.CANONICALIZATION_METHOD);		
    		Attr canonicalizationAttribute = doc.createAttributeNS(null, ConstantesXADES.ALGORITHM);
    		canonicalizationAttribute.setValue(Transforms.TRANSFORM_C14N_OMIT_COMMENTS);
    		canonicalizationElemento.getAttributes().setNamedItem(canonicalizationAttribute);

    		refsOnlyTimeStampElement.appendChild(canonicalizationElemento);
    	}
		
		// Escribimos el resultado en el nodo EncapsulatedTimeStamp
		Element encapsulatedTimeStampNode = 
			doc.createElementNS(DEFAULT_XADES_SCHEMA_URI, namespace + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.ENCAPSULATED_TIME_STAMP);
		encapsulatedTimeStampNode.appendChild(doc.createTextNode(hashSelloX));	
		refsOnlyTimeStampElement.appendChild(encapsulatedTimeStampNode);

        return doc;
    }

    /**
     * Este metodo añade la implementacion para XADES-XL
     * @param doc Documento de firma con formato XADES-X
     * @param valorCertificado
     * @param valorRevocacion
     * @return Documento de firma con formato XADES-XL
     * @throws Exception
     */
    protected static Document addXadesXL(Element firma, ArrayList<RespYCerts> respuestasFirma, 
    		ArrayList<RespYCerts> respuestasSello, XAdESSchemas schema, String namespace)
    	throws Exception {
    	
    	// Recogemos el nodo UnsignedSignatureProperties del cual dependen los nodos
    	// que hay que añadir para completar la firma XADES-XL
    	Document doc = firma.getOwnerDocument();
        Element elementoPrincipal = null ;

        NodeList nodosUnsignedSignatureProperties = firma.getElementsByTagNameNS(schema.getSchemaUri(), ConstantesXADES.UNSIGNED_SIGNATURE_PROPERTIES);
        if(nodosUnsignedSignatureProperties.getLength() != 0)
            elementoPrincipal = (Element)nodosUnsignedSignatureProperties.item(0);
        else
        	// No se encuentra el nodo UnsignedSignatureProperties
            throw new Exception("No se encuentra el nodo UnsignedSignatureProperties");
        	
        // Se añaden los certificados referenciados en el nodo CertificateValues
        if(respuestasFirma != null) {
        	//-- Juntamos las listas para añadir todos los certificados
        	if (respuestasSello != null) {
        		respuestasFirma.addAll(respuestasSello);
        	}
        	
        	EncapsulatedX509Certificate encapsulatedX509certificate = null;
        	ArrayList<EncapsulatedX509Certificate> certs = new ArrayList<EncapsulatedX509Certificate> (); 
        	List<X509Certificate> lCertificadosIncluidos = new ArrayList<X509Certificate>();
	        for(RespYCerts resp : respuestasFirma) {
        		
        		//-- no repetirlos
        		if (lCertificadosIncluidos.contains(resp.getCertstatus().getCertificate())) {
        			continue;
        		}
        		lCertificadosIncluidos.add(resp.getCertstatus().getCertificate());
        		
        		encapsulatedX509certificate = new EncapsulatedX509Certificate(schema, resp.getIdCertificado());
	        	try {
					encapsulatedX509certificate.setX509Certificate(resp.getCertstatus().getCertificate());
				} catch (CertificateException e) {
					logger.info(e.getMessage(), e);
					throw new Exception("No ha sido posible crear el elemento 'encapsulatedX509certificate' ");
				}
	        	certs.add(encapsulatedX509certificate);
	        }

	        CertificateValues certificateValues = new CertificateValues(schema, certs);
	        Element certificateValuesElement = null;
	        try {
	        	certificateValuesElement = certificateValues.createElement(doc, namespace);
			} catch (InvalidInfoNodeException e) {
				logger.info(e.getMessage(), e);				
				throw new Exception("No ha sido posible crear el elemento 'certificateValues'");
			}  
			
			// Se escribe una Id única
	    	Attr atributoCertVal = doc.createAttributeNS(null, ConstantesXADES.ID);
	    	String idCertVal = UtilidadTratarNodo.newID(doc, ConstantesXADES.CERTIFICATE_VALUES);
	    	atributoCertVal.setValue(idCertVal);
	    	certificateValuesElement.getAttributes().setNamedItem(atributoCertVal);
			
			elementoPrincipal.appendChild(certificateValuesElement);
			
            // Se añade la respuesta del servidor OCSP			
            Element valoresElementosRevocados =
                doc.createElementNS(DEFAULT_XADES_SCHEMA_URI, namespace + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.REVOCATION_VALUES);

            Element valorElementOCSP =
                doc.createElementNS(DEFAULT_XADES_SCHEMA_URI, namespace + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.OCSP_VALUES);
            
            CRLValues valorElementoCRL = new CRLValues(schema);

            lCertificadosIncluidos = new ArrayList<X509Certificate>();
	        int nOcspResps = 0;
	        int nCRLSResps = 0;
 	        for(int i=0;i<respuestasFirma.size();i++) {
        		RespYCerts resp = respuestasFirma.get(i);
        		
        		//-- no repetirlos
        		if (lCertificadosIncluidos.contains(resp.getCertstatus().getCertificate())) {
        			continue;
        		}
        		lCertificadosIncluidos.add(resp.getCertstatus().getCertificate());
        		
        		if (i < respuestasFirma.size()-1) {
            		ICertStatus respStatus = resp.getCertstatus();
	        		if (respStatus instanceof IOCSPCertStatus) {
	        			nOcspResps++;
	        			IOCSPCertStatus respOCSP = (IOCSPCertStatus) respStatus;
	    	            Element valorElementoEncapsuladoOCSP = doc.createElementNS(DEFAULT_XADES_SCHEMA_URI, namespace + ConstantesXADES.DOS_PUNTOS + ConstantesXADES.ENCAPSULATED_OCSP_VALUE);
	    	            valorElementoEncapsuladoOCSP.appendChild(
	    	                    doc.createTextNode(new String(Base64Coder.encode(respOCSP.getEncoded()))));
	    	            valorElementoEncapsuladoOCSP.setAttributeNS(null, ConstantesXADES.ID, resp.getIdRespStatus());
	    	            valorElementOCSP.appendChild(valorElementoEncapsuladoOCSP);
	        		}
	        		else if (respStatus instanceof IX509CRLCertStatus) {
	        			nCRLSResps++;
	        			IX509CRLCertStatus respCRL = (IX509CRLCertStatus) respStatus;
	        			try {
	        				valorElementoCRL.addCRL(respCRL.getX509CRL(), resp.getIdRespStatus());
						} catch (InvalidInfoNodeException ex) {
							throw new Exception("No se pudo generar nodo EncapsulatedCRLValue", ex);
						}
	        		}
        		}
	        }

            if (nCRLSResps > 0) {
            	try {
					Element el = valorElementoCRL.createElement(doc, namespace);
					valoresElementosRevocados.appendChild(el);
				} catch (InvalidInfoNodeException ex) {
					throw new Exception("No se pudo generar nodo CRLValues", ex);
				}
            }

            if (nOcspResps > 0)
            	valoresElementosRevocados.appendChild(valorElementOCSP);
            
			// Se escribe una Id única
	    	Attr atributoRevVal = doc.createAttributeNS(null, ConstantesXADES.ID);
	    	String idRevVal = UtilidadTratarNodo.newID(doc, ConstantesXADES.REVOCATION_VALUES);
	    	atributoRevVal.setValue(idRevVal);
	    	valoresElementosRevocados.getAttributes().setNamedItem(atributoRevVal);
            
            elementoPrincipal.appendChild(valoresElementosRevocados);
        }

        return doc;
    }

    protected static ArrayList<RespYCerts> convertICertStatus2RespYCerts(List<ICertStatus> status) {
    	ArrayList<RespYCerts> resps = new ArrayList<RespYCerts>((status != null) ? status.size() : 0);
    	if (status != null) {
    		Iterator<ICertStatus> itStatus = status.iterator();
    		while (itStatus.hasNext()) {
    			RespYCerts resp = new RespYCerts();
    			resp.setCertstatus(itStatus.next());
    			resps.add(resp);
    		}
     	}
     	return resps;
    }

    /*
     * Método que a partir de un objeto de validación MITyC obtiene un objeto de validación
     * de Arangí. Recursivamente obtiene las contrafirmas. Tiene en cuenta la validez de certificados.
     */
	private ValidationResult getValidationResult(ResultadoValidacion resultadoValidacion, boolean esContrafirma) {
		logger.debug("[XAdESSignature.getValidationResult]::Resultado validación: " + resultadoValidacion.getLog());
		
		//-- Si no estamos tratando una contrafirma, pero lo es, devolvemos null
		if (!esContrafirma && resultadoValidacion.getDatosFirma().getContraFirma() != null &&
				!resultadoValidacion.getDatosFirma().getContraFirma().isEmpty()) {
			return null;
		}
		
		//-- Llamar al método implementado por cada una de las subclases para interpretar
		//-- el resultado.
		int codigoResultadoValidacion = tratarResultadoValidacion (resultadoValidacion);
		
		//-- Completar con la información de la firma
		DatosFirma datosFirma = resultadoValidacion.getDatosFirma();
		X509Certificate certificate = null;
		if (datosFirma.getCadenaFirma() != null) {
			certificate = (X509Certificate)datosFirma.getCadenaFirma().getCertificates().get(0);
		}
		TimeStamp ts = null;
		if (datosFirma.getDatosSelloTiempo() != null && !datosFirma.getDatosSelloTiempo().isEmpty()) {
			ts = new TimeStamp (datosFirma.getDatosSelloTiempo().get(0).getTst());
		}
		OCSPResponse[] ocspResponses = null;
		if (datosFirma.getDatosOCSP() != null && !datosFirma.getDatosOCSP().isEmpty()) {
			ocspResponses = new OCSPResponse [datosFirma.getDatosOCSP().size()];
			for (int j = 0; j < ocspResponses.length; j++) {
				try {
					OCSPResp oscpResp = new OCSPResp(datosFirma.getDatosOCSP().get(j).getRespuestaOCSP().getEncoded());
					ocspResponses[j] = new OCSPResponse(oscpResp);
				} catch (MalformedOCSPResponseException e) {
					logger.info("[XAdESSignature.getValidationResult]::Una de las respuestas OCSP está mal formada", e);
				} catch (IOException e) {
					logger.info("[XAdESSignature.getValidationResult]::Una de las respuestas OCSP está mal formada", e);
				}
			}
		}
		
		ValidationResult result = new ValidationResult(codigoResultadoValidacion, certificate, ts==null?datosFirma.getFechaFirma():ts.getTime(), ts, ocspResponses);
		
		//-- Añadir contrafirmas si las hay (llamada recursiva a este método)
		ArrayList<ResultadoValidacion> lContrafirmas = resultadoValidacion.getContrafirmadoPor();
		if (lContrafirmas != null && !lContrafirmas.isEmpty()) {
			ValidationResult[] arrayContrafirmas = new ValidationResult[lContrafirmas.size()];
			int i = 0;
			for (Iterator<ResultadoValidacion> iterator = lContrafirmas.iterator(); iterator.hasNext();) {
				ResultadoValidacion resultadoValidacionContrafirma = iterator.next();
				arrayContrafirmas[i] = getValidationResult (resultadoValidacionContrafirma, true);
				i++;
			}
			result.setCounterSignatures(arrayContrafirmas);
		}
		
		//-- Devolve resultado
		return result;
	}

    /*
     * Método que a partir de un objeto de validación MITyC obtiene un objeto de validación
     * de Arangí. No tiene en cuenta la validez de certificados.
     */
	private ValidationResult getValidationResultSignatureOnly(ResultadoValidacion resultadoValidacion, boolean esContrafirma) {
		logger.debug("[XAdESSignature.getValidationResultSignatureOnly]::Resultado validación: " + resultadoValidacion.getLog());
		
		//-- Si no estamos tratando una contrafirma, pero lo es, devolvemos null
		if (!esContrafirma && resultadoValidacion.getDatosFirma().getContraFirma() != null &&
				!resultadoValidacion.getDatosFirma().getContraFirma().isEmpty()) {
			return null;
		}
		
		//-- Llamar al método implementado por cada una de las subclases para interpretar
		//-- el resultado.
		int codigoResultadoValidacion = tratarResultadoValidacionSoloFirma (resultadoValidacion);
		
		//-- Completar con la información de la firma
		DatosFirma datosFirma = resultadoValidacion.getDatosFirma();
		X509Certificate certificate = (X509Certificate)datosFirma.getCadenaFirma().getCertificates().get(0);
		TimeStamp ts = null;
		if (datosFirma.getDatosSelloTiempo() != null && !datosFirma.getDatosSelloTiempo().isEmpty()) {
			ts = new TimeStamp (datosFirma.getDatosSelloTiempo().get(0).getTst());
		}
		OCSPResponse[] ocspResponses = null;
		if (datosFirma.getDatosOCSP() != null && !datosFirma.getDatosOCSP().isEmpty()) {
			ocspResponses = new OCSPResponse [datosFirma.getDatosOCSP().size()];
			for (int j = 0; j < ocspResponses.length; j++) {
				try {
					OCSPResp oscpResp = new OCSPResp(datosFirma.getDatosOCSP().get(j).getRespuestaOCSP().getEncoded());
					ocspResponses[j] = new OCSPResponse(oscpResp);
				} catch (MalformedOCSPResponseException e) {
					logger.info("[XAdESSignature.getValidationResult]::Una de las respuestas OCSP está mal formada", e);
				} catch (IOException e) {
					logger.info("[XAdESSignature.getValidationResult]::Una de las respuestas OCSP está mal formada", e);
				}
			}
		}
		
		ValidationResult result = new ValidationResult(codigoResultadoValidacion, certificate, ts==null?datosFirma.getFechaFirma():ts.getTime(), ts, ocspResponses);
		
		//-- Añadir contrafirmas si las hay (llamada recursiva a este método)
		ArrayList<ResultadoValidacion> lContrafirmas = resultadoValidacion.getContrafirmadoPor();
		if (lContrafirmas != null && !lContrafirmas.isEmpty()) {
			ValidationResult[] arrayContrafirmas = new ValidationResult[lContrafirmas.size()];
			int i = 0;
			for (Iterator<ResultadoValidacion> iterator = lContrafirmas.iterator(); iterator.hasNext();) {
				ResultadoValidacion resultadoValidacionContrafirma = iterator.next();
				arrayContrafirmas[i] = getValidationResultSignatureOnly (resultadoValidacionContrafirma, true);
				i++;
			}
			result.setCounterSignatures(arrayContrafirmas);
		}
		
		//-- Devolver resultado
		return result;
	}

	/*
	 * Trata el resultado para comprobar sólo la firma (no los certificados)
	 */
	private int tratarResultadoValidacionSoloFirma(ResultadoValidacion resultadoValidacion) {
		if (resultadoValidacion.getNivelValido() == null || resultadoValidacion.getNivelValido().equals("")) {
			if (resultadoValidacion.getLog().toLowerCase().indexOf("firma inválida") > -1) {
				//certificado válido, el problema será con la firma
				logger.debug("[XAdESBESSignature.tratarResultadoValidacionSoloFirma]::La firma no es válida");
				return ValidationResult.RESULT_SIGNATURE_NOT_MATCH_DATA;
			} 
		} 
		
		//válido
		logger.debug("[XAdESSignature.tratarResultadoValidacionSoloFirma]::La firma ha pasado la validación");
		return ValidationResult.RESULT_VALID;
		
	}
	
	/**
	 * Obtener validador:
	 * 
	 * Si no se pasa documento:
	 * <ul>
	 * 	<li>Se busca una referencia a un fichero</li>
	 * 	<li>Se busca una referencia a una URL</li>
	 * 	<li>Comportamiento por defecto: se busca una referencia dentro del XML</li>
	 * </ul>
	 * 
	 * Si se pasa el documento:
	 * <ul>
	 * 	<li>Si es un attached se mira si el documento corresponde con el attached (él, 
	 * 		su base64 o su hash en base64 (SHA-1 o SHA-256). Si se corresponde se utiliza
	 * 		el comportamiento por defecto.</li>
	 * 	<li>Si es un detached se valida el documento</li>
	 * </ul>
	 * 
	 * @param document
	 * @return
	 */
	private ValidarFirmaXML loadValidator (IDocument document) {
	    ValidarFirmaXML validator = new ValidarFirmaXML();
	    if (document == null) {
		    validator.addResolver(new FileResourceData());
		    validator.addResolver(new URLResourceData());
	    } else {
	    	String attachedDocument = null;
			try {
				attachedDocument = getAttachedDocument();
			} catch (Exception e1) {
				logger.info("[XAdESSignature.loadValidator]::No se puede obtener el documento attached. En la validación se indicará que no existe");
			} 
	    	if (attachedDocument == null) {
	    		//-- es detached
	    		validator.addResolver(new UnknownFileResourceData(document));
	    	} else {
	    		//-- es attached
	    		byte[] bAttachedDocument = Util.decodeBase64(attachedDocument);
	    		boolean documentsMatch = true;
	    		byte[] externalDocument;
				try {
					externalDocument = Util.readStream(document.getInputStream());
					
		    		if (!attachedDocument.equals (new String (externalDocument, getEncoding()))) {
		    			// no son el mismo documento. comprobar base64
		    			if (!Arrays.equals(bAttachedDocument, externalDocument)) {
		    				// no es el documento en base64. comprobar sha-1
	    					ByteArrayInputStream bais = new ByteArrayInputStream (externalDocument);
	    					InputStreamDocument isd = new InputStreamDocument (bais);
	    					byte[] hash = null;
							try {
								hash = isd.getHash(HashingAlgorithm.SHA1);
							} catch (HashingException e) {
				    			logger.debug("[XAdESSignature.loadValidator]::No se puede obtener hash SHA-1", e);
							}
		    				if (hash == null || (!Arrays.equals(bAttachedDocument, hash) && !attachedDocument.equals (Util.toHexadecimal(hash)))) {
		    					// no es el sha-1. comprobar el sha-256
		    					hash = null;
		    					bais = new ByteArrayInputStream (externalDocument);
		    					isd = new InputStreamDocument (bais);
		    					try {
									hash = isd.getHash(HashingAlgorithm.SHA256);
								} catch (HashingException e) {
					    			logger.debug("[XAdESSignature.loadValidator]::No se puede obtener hash SHA-256", e);
								}
		    					if (hash == null || (!Arrays.equals(bAttachedDocument, hash) && !attachedDocument.equals (Util.toHexadecimal(hash)))) {
			    					documentsMatch = false;
			    				}
		    				}
		    			}
		    		}
				} catch (IOException e) {
	    			logger.debug("[XAdESSignature.loadValidator]::No se puede obtener el documento externo", e);
				}
	    		
	    		//-- conclusión: si no coinciden los documentos añadimos un validador que siempre devolverá
				//-- que las firmas no son válidas
	    		if (!documentsMatch) {
	    			validator.addResolver(new ToxicResourceData());
	    		}
	    	}
	    }

	    return validator;
	}

	/**
	 * Obtiene un valor contenido dentro del tag DataObjectFormat de la firma. Si este valor
	 * no se añadió se devolverá null.
	 * 
	 * @param tagName Nombre del tag
	 * @return Valor
	 */
	private String getDataObjectFormatValue (String tagName) {
		logger.debug("[XAdESSignature.getDataObjectFormatValue]::Entrada");
		
		//-- Obtener el nodo
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		try {
			XPathExpression expr = xpath.compile("//*[local-name()='Object']/*[local-name()='QualifyingProperties']/*[local-name()='SignedProperties']/" +
					"*[local-name()='SignedDataObjectProperties']/*[local-name()='DataObjectFormat']/*[local-name()='" + tagName + "']");
			NodeList descriptionNodes = (NodeList) expr.evaluate (xadesDocument, XPathConstants.NODESET);
			if (descriptionNodes == null || descriptionNodes.getLength() == 0) {
				logger.debug("[XAdESSignature.getDataObjectFormatValue]::No existe la descripción del documento en la firma");
				return null;
			}
			
			return descriptionNodes.item(0).getTextContent();
			
		} catch (Exception e) {
			logger.info ("[XAdESSignature.getDataObjectFormatValue]::Error inesperado", e);
			return null;
		}

	}
	
	/*
	 * Método de validación común tanto si se valida con CAList como con
	 * servicios de validación
	 */
	private ValidationResult[] isValidCommon(IDocument document, CAList caList, List<CertificateValidationService> validationServices)
			throws SignatureException {
		
		logger.debug("[XAdESSignature.isValid]::Entrada::" + Arrays.asList(new Object[] { document, caList, validationServices }));
		
		ExtraValidators extraValidator;
		if (caList != null) {
			extraValidator = new ExtraValidators(null, new CAListCertStatusRecover(caList, true), null);
		} else {
			extraValidator = new ExtraValidators(null, new ValidationServicesCertStatusRecover(validationServices), null);
		}
	    ValidarFirmaXML validator = loadValidator (document);
	    List<ResultadoValidacion> results;
		try {
			results = validator.validar(this.xadesDocument, "./", extraValidator);
		} catch (FirmaXMLError e) {
			logger.info("[XAdESSignature.isValid]::No ha sido posible validar la firma", e);
		    throw new SignatureException("No ha sido posible validar la firma", e);
		}
		
	    ArrayList<ValidationResult> result = new ArrayList<ValidationResult>();
	    for (Iterator<ResultadoValidacion> iterator = results.iterator(); iterator.hasNext();) {
			ResultadoValidacion resultadoValidacion = iterator.next();
			ValidationResult validationResult = getValidationResult (resultadoValidacion, false);
			if (validationResult != null) {
				result.add(validationResult);
			}
			
		}
		
		return result.toArray(new ValidationResult[0]);
	}

	/**
	 * Busca el nombre utilizado en el espacio de nombres del ETSI. Las 
	 * clases del MITyC utilizan 'etsi' pero @Firma usa 'xades'.
	 * 
	 * @param element Elemento sobre el que se realiza la búsqueda
	 * @return el nombre del espacio de nombres o el nombre por defecto
	 */
    protected static String searchXAdESNamespace(Element element) {
    	
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		try {
			XPathExpression expr = xpath.compile("//*[local-name()='SignedSignatureProperties']");
			Node sspNode = (Node) expr.evaluate (element, XPathConstants.NODE);
			if (sspNode != null && sspNode.getPrefix() != null) {
				return sspNode.getPrefix();
			}
		} catch (Exception e) {
		}
    	
		return xadesNS;
	}

    protected String nodeToString(Node node) {
    	StringWriter sw = new StringWriter();
    	try {
    		Transformer t = TransformerFactory.newInstance().newTransformer();
    		t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
    		t.setOutputProperty(OutputKeys.INDENT, "yes");
    		t.transform(new DOMSource(node), new StreamResult(sw));
    	} catch (TransformerException te) {
    		System.out.println("nodeToString Transformer Exception");
    	}
    	return sw.toString();
    }
}

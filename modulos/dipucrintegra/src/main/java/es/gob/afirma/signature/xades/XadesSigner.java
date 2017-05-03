// Copyright (C) 2012-13 MINHAP, Gobierno de España
// This program is licensed and may be used, modified and redistributed under the terms
// of the European Public License (EUPL), either version 1.1 or (at your
// option) any later version as soon as they are approved by the European Commission.
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
// or implied. See the License for the specific language governing permissions and
// more details.
// You should have received a copy of the EUPL1.1 license
// along with this program; if not, you may find it at
// http://joinup.ec.europa.eu/software/page/eupl/licence-eupl

/*
 * Este fichero forma parte de la plataforma de @firma.
 * La plataforma de @firma es de libre distribución cuyo código fuente puede ser consultado
 * y descargado desde http://forja-ctt.administracionelectronica.gob.es
 *
 * Copyright 2009-,2011 Gobierno de España
 * Este fichero se distribuye bajo las licencias EUPL versión 1.1  y GPL versión 3, o superiores, según las
 * condiciones que figuran en el fichero 'LICENSE.txt' que se acompaña.  Si se   distribuyera este
 * fichero individualmente, deben incluirse aquí las condiciones expresadas allí.
 */

/**
 * <b>File:</b><p>es.gob.afirma.signature.xades.XadesSigner.java.</p>
 * <b>Description:</b><p>Class for create XAdES signatures.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * <b>Date:</b><p>04/07/2011.</p>
 * @author Gobierno de España.
 * @version 1.0, 04/07/2011.
 */
package es.gob.afirma.signature.xades;

import static es.gob.afirma.signature.SignatureConstants.SIGN_ALGORITHM_URI;
import static es.gob.afirma.signature.SignatureConstants.SIGN_FORMAT_XADES_DETACHED;
import static es.gob.afirma.signature.SignatureConstants.SIGN_FORMAT_XADES_ENVELOPED;
import static es.gob.afirma.signature.SignatureConstants.SIGN_FORMAT_XADES_ENVELOPING;
import static es.gob.afirma.signature.SignatureConstants.SIGN_FORMAT_XADES_EXTERNALLY_DETACHED;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.AccessController;
import java.security.GeneralSecurityException;
import java.security.KeyStore.PrivateKeyEntry;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.xml.crypto.Data;
import javax.xml.crypto.MarshalException;
import javax.xml.crypto.XMLStructure;
import javax.xml.crypto.dom.DOMStructure;
import javax.xml.crypto.dsig.CanonicalizationMethod;
import javax.xml.crypto.dsig.DigestMethod;
import javax.xml.crypto.dsig.Manifest;
import javax.xml.crypto.dsig.Reference;
import javax.xml.crypto.dsig.Transform;
import javax.xml.crypto.dsig.XMLObject;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.XMLValidateContext;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.crypto.dsig.keyinfo.KeyValue;
import javax.xml.crypto.dsig.spec.TransformParameterSpec;
import javax.xml.crypto.dsig.spec.XPathFilterParameterSpec;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.java.xades.security.xml.XMLSignatureElement;
import net.java.xades.security.xml.XAdES.DataObjectFormat;
import net.java.xades.security.xml.XAdES.DataObjectFormatImpl;
import net.java.xades.security.xml.XAdES.SignaturePolicyIdentifier;
import net.java.xades.security.xml.XAdES.SignerRole;
import net.java.xades.security.xml.XAdES.SignerRoleImpl;
import net.java.xades.security.xml.XAdES.XAdES;
import net.java.xades.security.xml.XAdES.XAdES_EPES;

import org.apache.jcp.xml.dsig.internal.DigesterOutputStream;
import org.apache.jcp.xml.dsig.internal.dom.ApacheData;
import org.apache.jcp.xml.dsig.internal.dom.ApacheNodeSetData;
import org.apache.jcp.xml.dsig.internal.dom.DOMReference;
import org.apache.jcp.xml.dsig.internal.dom.DOMTransform;
import org.apache.log4j.Logger;
import org.apache.xml.security.keys.KeyInfo;
import org.apache.xml.security.signature.XMLSignatureInput;
import org.apache.xml.security.utils.UnsyncBufferedOutputStream;
import org.apache.xml.security.utils.resolver.ResourceResolver;
import org.apache.xml.security.utils.resolver.ResourceResolverException;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xml.sax.SAXException;

import es.gob.afirma.i18n.ILogConstantKeys;
import es.gob.afirma.i18n.Language;
import es.gob.afirma.signature.SignatureConstants;
import es.gob.afirma.signature.SignatureProperties;
import es.gob.afirma.signature.Signer;
import es.gob.afirma.signature.SigningException;
import es.gob.afirma.signature.xades.ReferenceData.TransformData;
import es.gob.afirma.transformers.TransformersException;
import es.gob.afirma.utils.Base64Coder;
import es.gob.afirma.utils.CryptoUtil;
import es.gob.afirma.utils.GenericUtils;
import es.gob.afirma.utils.UtilsXML;

/**
 * <p>Class for create XAdES signatures.</p>
 * <b>Project:</b><p>Horizontal platform of validation services of multiPKI
 * certificates and electronic signature.</p>
 * @version 1.0, 21/07/2011.
 */
public final class XadesSigner implements Signer {

    /**
     * Attribute that represents factory for building documents.
     */
    private static DocumentBuilderFactory dBFactory = null;

    /**
     * Attribute that represents the object that manages the log of the class.
     */
    private static final Logger LOGGER = Logger.getLogger(XadesSigner.class);

    /**
     * Attribute that represents data type for XML documents.
     */
    private static final int DATA_TYPE_XML = 1;
    /**
     * Attribute that represents data type for binary documents.
     */
    private static final int DATA_TYPE_BINARY = 2;
    /**
     * Attribute that represents data type for binary documents encoded in base64.
     */
    private static final int DATA_TYPE_BINARY_BASE64 = 3;

    /**
     * Attribute that represents root node name for result signatures.
     */
    private static final String AFIRMA = "AFIRMA";

    /**
     * Attribute that represents prefix used in xmlSignature nodes.
     */
    private static final String XML_SIGNATURE_PREFIX = "ds";

    /**
     * Attribute that represents signature node name (ds:signature).
     */
    private static final String DS_SIGNATURE_NODE_NAME = XML_SIGNATURE_PREFIX + ":Signature";

    /**
     * Attribute that represents coSignature root name.
     */
    private static final String ROOT_TAG = "ROOT_COSIGNATURES";

    /**
     * Attribute that represents tag name used in Manifest objects (&lt;ds:Manifest&gt;).
     */
    private static final String MANIFEST_TAG_NAME = "ds:Manifest";

    /**
     * Attribute that represents prefix used in nodes of type "Xades".
     */
    private static final String XADES_SIGNATURE_PREFIX = "xades";
    /**
     * Attribute that represents detached content element name.
     */
    private static final String DETACHED_CONTENT_ELEMENT_NAME = "CONTENT";

    /**
     * Attribute that represents object name used in reference in enveloping xades signatures.
     */
    private static final String ENVELOPING_OBJECT_ELEMENT_NAME = "Object";

    /**
     * Attribute that represents default digest algorithm in the xml references.
     */
    private String digestAlgorithmRef = DigestMethod.SHA1;

    /**
     * Attribute that represents base64 encoding name.
     */
    private static final String ENCODING_BASE64 = "base64";
    /**
     * Attribute that represents dataElement object.
     */
    private Element dataElement = null;

    /**
     * Attribute that represents contentId element.
     */
    private String contentId = null;

    /**
     * Attribute that represents the referece of data format object.
     */

    /**
     * Attribute that represents the data format object.
     */
    private DataObjectFormat dataObjectFormat = null;

    static {
	AccessController.doPrivileged(new java.security.PrivilegedAction<Void>() {

	    public Void run() {
		try {
		    Security.insertProviderAt(new org.apache.jcp.xml.dsig.internal.dom.XMLDSigRI(), 1);
		} catch (final SecurityException e) {
		    LOGGER.error(Language.getResIntegra(ILogConstantKeys.XS_LOG002), e);
		}
		return null;
	    }
	});
    }

    /**
     * Attribute that represents a factory for creating XMLSignature objects from scratch or for unmarshalling an XMLSignature object.
     */
    private static XMLSignatureFactory xmlSignatureFactory = null;

    /**
     * Attribute that represents data type of a document.
     */
    private int dataType = DATA_TYPE_BINARY;

    /**
     * Attribute that represents URI that define namespace of XMLDSig signatures .
     */
    public static final String DSIGNNS = "http://www.w3.org/2000/09/xmldsig#";

    /**
     * Attribute that represents URI used for XAdES default version .
     */
    public static final String XADESNS = "http://uri.etsi.org/01903/v1.3.2#";

    /**
     * Attribute that represents URI used in reference of OBJECT type.
     */
    public static final String OBJECT_URI = "http://www.w3.org/2000/09/xmldsig#Object";

    /**
     * Attribute that represents URI used in reference of countersignatures.
     */
    public static final String COUNTER_SIGN_URI = "http://uri.etsi.org/01903#CountersignedSignature";

    /**
     * Attribute that represents signature node name (Signature).
     */
    public static final String SIGNATURE_NODE_NAME = "Signature";

    /**
     * Constructor method for the class XadesSigner.java.
     */
    public XadesSigner() {
	super();
	if (dBFactory == null) {
	    dBFactory = DocumentBuilderFactory.newInstance();
	    dBFactory.setNamespaceAware(true);
	}
	if (xmlSignatureFactory == null) {
	    xmlSignatureFactory = XMLSignatureFactory.getInstance("DOM");
	}
    }

    /**
     * Builds a xml element with original document to sign.
     * @param data original document.
     * @param signatureFormat signature format.
     * @throws SigningException in error case.
     */
    private void createDataNode(byte[ ] data, String signatureFormat) throws SigningException {
	if (!SIGN_FORMAT_XADES_EXTERNALLY_DETACHED.equals(signatureFormat)) {
	    contentId = DETACHED_CONTENT_ELEMENT_NAME + "-" + UUID.randomUUID().toString();
	    // Detección del tipo de datos a firmar. Diferenciamos entre
	    // documentos XML y resto de formatos
	    Document docum = null;
	    // se comprueba si es un documento xml
	    try {
		docum = dBFactory.newDocumentBuilder().parse(new ByteArrayInputStream(data));
		LOGGER.debug(Language.getResIntegra(ILogConstantKeys.XS_LOG008));
		if (signatureFormat.equals(SIGN_FORMAT_XADES_DETACHED)) {
		    dataElement = docum.createElement(DETACHED_CONTENT_ELEMENT_NAME);
		    dataElement.setAttribute("Id", contentId);
		    dataElement.setAttribute("MimeType", "text/xml");
		    // Obtenemos el encoding del documento original
		    dataElement.setAttribute("Encoding", docum.getXmlEncoding());
		    dataElement.appendChild(docum.getDocumentElement());

		} else {
		    dataElement = docum.getDocumentElement();
		}
		dataType = DATA_TYPE_XML;

	    } catch (SAXException e) {
		// captura de error en caso de no ser un documento xml y
		// conversión a base64.
		createNodeBase64(data, signatureFormat);
	    } catch (IOException e) {
		throw new SigningException(e);
	    } catch (ParserConfigurationException e) {
		throw new SigningException(e);
	    }
	}
    }

    /**
     * Creates a xml with base64 data.
     * @param data data to include in xml node.
     * @param signatureFormat signature format.
     * @throws SigningException in error case.
     */
    private void createNodeBase64(byte[ ] data, String signatureFormat) throws SigningException {
	if (signatureFormat.equals(SIGN_FORMAT_XADES_ENVELOPED)) {
	    throw new SigningException(Language.getResIntegra(ILogConstantKeys.XS_LOG009));
	}
	// para los formatos de firma internally detached y enveloping se trata
	// de convertir el documento a base64
	LOGGER.debug(Language.getResIntegra(ILogConstantKeys.XS_LOG010));

	try {
	    // crea un nuevo nodo xml para contener los datos en base 64
	    Document docFile = dBFactory.newDocumentBuilder().newDocument();
	    dataElement = docFile.createElement(DETACHED_CONTENT_ELEMENT_NAME);
	    dataElement.setAttribute("Id", contentId);
	    dataElement.setAttribute("Encoding", ENCODING_BASE64);

	    if (Base64Coder.isBase64Encoded(data)) {
		LOGGER.debug(Language.getResIntegra(ILogConstantKeys.XS_LOG011));
		dataElement.setTextContent(new String(data));
		dataType = DATA_TYPE_BINARY;
	    } else {
		LOGGER.debug(Language.getResIntegra(ILogConstantKeys.XS_LOG012));
		dataElement.setTextContent(new String(Base64Coder.encodeBase64(data)));
		dataType = DATA_TYPE_BINARY_BASE64;
	    }
	} catch (final DOMException e2) {
	    throw new SigningException(Language.getResIntegra(ILogConstantKeys.XS_LOG013), e2);
	} catch (TransformersException e2) {
	    throw new SigningException(Language.getResIntegra(ILogConstantKeys.XS_LOG013), e2);
	} catch (ParserConfigurationException e2) {
	    throw new SigningException(Language.getResIntegra(ILogConstantKeys.XS_LOG013), e2);
	}

    }

    /**
     * Builds all references used in the xades signature.
     * @param xmlSignature XadesExt instance.
     * @param signatureFormat signature format
     * @param extraParams optional parameters.
     * @return list with all objects of type {@link javax.xml.crypto.dsig.Reference References}
     * @throws SigningException in error case.
     */
    private List<Reference> buildReferences(XadesExt xmlSignature, String signatureFormat, Properties extraParams) throws SigningException {
	final List<Reference> referenceList = new ArrayList<Reference>();
	DigestMethod digestMethod = getDigestMethod();

	// Canonicalización.
	final List<Transform> transformList = new ArrayList<Transform>();

	final String referenceId = "Reference-" + UUID.randomUUID().toString();

	// crea una referencia al documento insertado en un nodo Object para la
	// firma enveloping
	if (signatureFormat.equals(SIGN_FORMAT_XADES_ENVELOPING)) {
	    addCanonicalization(transformList);
	    XMLObject envelopingObject = newEnvelopingObject(referenceList, transformList, digestMethod, referenceId);
	    // incluimos el nuevo objeto en documento a firmar
	    xmlSignature.addXMLObject(envelopingObject);

	    // crea una referencia al documento mediante la URI hacia el
	    // identificador del nodo CONTENT
	} else if (signatureFormat.equals(SIGN_FORMAT_XADES_DETACHED)) {
	    try {
		addCanonicalization(transformList);

		// incluimos la referencia en el objeto que informa del formato
		// del documento a firmar
		addDataFormatReference("#" + referenceId);

		// crea la referencia a los datos firmados que se encontraran en
		// el mismo documento
		referenceList.add(xmlSignatureFactory.newReference("#" + contentId, digestMethod, transformList, null, referenceId));
	    } catch (final DOMException e) {
		throw new SigningException(Language.getResIntegra(ILogConstantKeys.XS_LOG014), e);
	    }

	    // crea una referencia indicando que se trata de una firma enveloped
	} else if (signatureFormat.equals(SIGN_FORMAT_XADES_ENVELOPED)) {
	    try {

		// Transformacion enveloped
		transformList.add(xmlSignatureFactory.newTransform(Transform.ENVELOPED, (TransformParameterSpec) null));

		// Transformacion XPATH para eliminar el resto de firmas del
		// documento.
		transformList.add(xmlSignatureFactory.newTransform(Transform.XPATH, new XPathFilterParameterSpec("not(ancestor-or-self::" + DS_SIGNATURE_NODE_NAME + ")", Collections.singletonMap(XML_SIGNATURE_PREFIX, XMLSignature.XMLNS))));

		// incluimos la referencia en el objeto que informa del formato
		// del documento a firmar
		addDataFormatReference("#" + referenceId);

		// crea la referencia
		referenceList.add(xmlSignatureFactory.newReference("", digestMethod, transformList, null, referenceId));
	    } catch (final GeneralSecurityException e) {
		throw new SigningException(Language.getResIntegra(ILogConstantKeys.XS_LOG015), e);
	    }

	    // crea una referencia para un objeto Manifest que contedrá las
	    // referencias externas al documento de la firma
	} else if (signatureFormat.equals(SIGN_FORMAT_XADES_EXTERNALLY_DETACHED) && extraParams != null) {
	    addManifestObject(xmlSignature, referenceList, digestMethod, extraParams);

	}
	return referenceList;
    }

    // MÉTODOS DEPENDENDIENTES DE LA CREACIÓN DE REFERENCIAS //
    /**
     * Creates a new instance XMLObject that includes original document.
     * @param referenceList list of references.
     * @param transformList list of transformers.
     * @param digestMethod digest method.
     * @param referenceId reference identificator.
     * @return a new instance XMLObject that includes original document.
     */
    private XMLObject newEnvelopingObject(List<Reference> referenceList, List<Transform> transformList, DigestMethod digestMethod, String referenceId) {
	// crea el nuevo elemento Object que contiene el documento a firmar
	final List<XMLStructure> structures = new ArrayList<XMLStructure>(1);

	// Si los datos se han convertido a base64, bien por ser binarios o
	// explicitos
	if (DATA_TYPE_BINARY_BASE64 == dataType || DATA_TYPE_BINARY == dataType) {
	    structures.add(new DOMStructure(dataElement.getFirstChild()));
	} else {
	    structures.add(new DOMStructure(dataElement));
	}

	final String objectId = "Object-" + UUID.randomUUID().toString();
	final String mime = DATA_TYPE_XML == dataType ? "text/xml" : "application/octet-stream";
	XMLObject envelopingObject = xmlSignatureFactory.newXMLObject(structures, objectId, mime, DATA_TYPE_XML == dataType ? null : ENCODING_BASE64);

	addDataFormatReference("#" + referenceId);
	// crea la referencia al nuevo elemento Object
	referenceList.add(xmlSignatureFactory.newReference("#" + objectId, digestMethod, transformList, OBJECT_URI, referenceId));
	return envelopingObject;
    }

    /**
     * Adds reference of data format object.
     * @param idReference represents identificator of reference.
     */
    private void addDataFormatReference(String idReference) {
	// se almacena la referencia de los datos a firmar para usarla en el
	// objeto de formato de los datos.
	((DataObjectFormatImpl) dataObjectFormat).setObjectReference(idReference);
    }

    /**
     * Adds canonicalization transformation to reference.
     * @param transformList list for include canonicalization data.
     */
    private void addCanonicalization(List<Transform> transformList) {
	// Solo canonicalizo si es XML
	if (DATA_TYPE_XML == dataType) {
	    try {
		// Transformada para la canonicalizacion inclusiva
		transformList.add(xmlSignatureFactory.newTransform(CanonicalizationMethod.INCLUSIVE, (TransformParameterSpec) null));
	    } catch (final GeneralSecurityException e) {
		LOGGER.error(Language.getResIntegra(ILogConstantKeys.XS_LOG016), e);
	    }
	    // Si no era XML y tuve que convertir a Base64 yo mismo declaro la
	    // transformación
	} else if (DATA_TYPE_BINARY_BASE64 == dataType) {
	    try {
		transformList.add(xmlSignatureFactory.newTransform(Transform.BASE64, (TransformParameterSpec) null));
	    } catch (final GeneralSecurityException e) {
		LOGGER.error(Language.getResIntegra(ILogConstantKeys.XS_LOG017), e);
	    }
	}
    }

    /**
     * @return a digest method object.
     * @throws SigningException if instance digest method happens an error.
     */
    private DigestMethod getDigestMethod() throws SigningException {
	DigestMethod digestMethod = null;
	try {
	    digestMethod = xmlSignatureFactory.newDigestMethod(digestAlgorithmRef, null);
	} catch (GeneralSecurityException e) {
	    throw new SigningException(Language.getFormatResIntegra(ILogConstantKeys.XS_LOG018, new Object[ ] { digestAlgorithmRef }), e);
	}
	return digestMethod;
    }

    /**
     * Adds a manifest object in the digital signature.
     * @param xmlSignature xml signature.
     * @param referenceList reference list.
     * @param digestMethod digest method.
     * @param extraParams optional parameters.
     * @throws SigningException in error case.
     */
    private void addManifestObject(XadesExt xmlSignature, List<Reference> referenceList, DigestMethod digestMethod, Properties extraParams) throws SigningException {
	List<ReferenceData> mfReferences;
	try {
	    Object manifestData = extraParams.get(SignatureConstants.MF_REFERENCES_PROPERTYNAME);
	    // creamos el objeto <Manifest>
	    String manifestID = "ManifestObject-" + UUID.randomUUID().toString();
	    Document mfDoc = UtilsXML.newDocument();

	    if (manifestData instanceof List) {
		mfReferences = (List) manifestData;
		Element mfElement = mfDoc.createElement(MANIFEST_TAG_NAME);
		mfDoc.appendChild(mfElement);
		LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.XS_LOG019, new Object[ ] { mfReferences.size() }));
		for (int i = 0; i < mfReferences.size(); i++) {
		    ReferenceData referenceData = mfReferences.get(i);
		    mfElement.appendChild(buildReferenceXmlNode(referenceData, mfDoc));
		}
	    } else if (manifestData instanceof Element) {
		mfDoc.appendChild(mfDoc.importNode((Element) manifestData, true));
	    } else {
		throw new SigningException(Language.getResIntegra(ILogConstantKeys.XS_LOG020));
	    }

	    xmlSignature.addXMLObject(xmlSignatureFactory.newXMLObject(Collections.singletonList(new DOMStructure(mfDoc.getDocumentElement())), manifestID, null, null));

	    // incluimos la referencia en el objeto que informa del formato del
	    // documento a firmar
	    addDataFormatReference("#" + manifestID);

	    // creamos la referencia al objeto manifest.
	    Reference ref = xmlSignatureFactory.newReference("#" + manifestID, digestMethod, null, Manifest.TYPE, null);
	    referenceList.add(ref);
	} catch (ParserConfigurationException e) {
	    throw new SigningException(Language.getResIntegra(ILogConstantKeys.XS_LOG006), e);
	} catch (ClassCastException e) {
	    throw new SigningException(Language.getResIntegra(ILogConstantKeys.XS_LOG021), e);
	}

    }

    /**
     * Builds a xml node with all references used in the signature.
     * @param rfData reference data.
     * @param doc xml document.
     * @return xml element with all references included.
     */
    private Element buildReferenceXmlNode(ReferenceData rfData, Document doc) {
	Element referenceElement = doc.createElement("ds:Reference");
	if (GenericUtils.assertStringValue(rfData.getId())) {
	    referenceElement.setAttribute("Id", rfData.getId());
	}
	if (GenericUtils.assertStringValue(rfData.getUri())) {
	    referenceElement.setAttribute("URI", rfData.getUri());
	}
	if (GenericUtils.assertStringValue(rfData.getType())) {
	    referenceElement.setAttribute("Type", rfData.getType());
	}

	if (rfData.getTransforms() != null) {
	    Element transformElements = UtilsXML.ponHijo(referenceElement, "ds:Transforms");
	    for (TransformData transform: rfData.getTransforms()) {
		if (transform != null) {
		    Element transfElement = UtilsXML.ponHijo(transformElements, "ds:Transform");
		    UtilsXML.insertAttributeValue(transfElement, "@Algorithm", transform.getAlgorithm());
		    if (transform.getXPath() != null) {
			for (String xPath: transform.getXPath()) {
			    if (GenericUtils.assertStringValue(xPath)) {
				UtilsXML.insertValueElement(transfElement, "ds:XPath", xPath);
			    }
			}
		    }
		}
	    }
	}
	UtilsXML.insertAttributeValue(referenceElement, "ds:DigestMethod@Algorithm", rfData.getDigestMethodAlg());
	UtilsXML.insertValueElement(referenceElement, "ds:DigestValue", rfData.getDigestValue());

	return referenceElement;
    }

    // /FIN DE MÉTODOS DEPENDENDIENTES DE LA CREACIÓN DE REFERENCIAS //

    /**
     * Instances a <code>net.java.xades.security.xml.XAdES.XAdES_EPES</code> object.
     * @param elementToSign xml element to sign.
     * @param certificate certificate to sign.
     * @param extraParams optional parameters.
     * @return a <code>net.java.xades.security.xml.XAdES.XAdES_EPES</code> instance.
     */
    private XAdES_EPES newXadesObject(Element elementToSign, X509Certificate certificate, Properties extraParams) {
	// Instancia XADES_EPES
	XAdES_EPES xades = (XAdES_EPES) XAdES.newInstance(XAdES.EPES, // XAdES
	                                                  XADESNS, // XAdES
	                                                  // NameSpace
	                                                  XADES_SIGNATURE_PREFIX, // XAdES
	                                                  // Prefix
	                                                  XML_SIGNATURE_PREFIX, // XMLDSig
	                                                  // Prefix
	                                                  digestAlgorithmRef, // DigestMethod
	                                                  elementToSign // Element
		);

	// Se incluyen las propiedades a firmar:
	/*<SignedProperties>
	<SignedSignatureProperties>
		(SigningTime)?
		(SigningCertificate)?
		(SignaturePolicyIdentifier)
		(SignatureProductionPlace)?
		(SignerRole)?
	</SignedSignatureProperties>
	<SignedDataObjectProperties>
		(DataObjectFormat)*
		(CommitmentTypeIndication)*
		(AllDataObjectsTimeStamp)*
		(IndividualDataObjectsTimeStamp)*
	</SignedDataObjectProperties>
	  </SignedProperties> */

	// SigningTime
	xades.setSigningTime(new Date());
	// SigningCertificate
	xades.setSigningCertificate(certificate);

	// SignaturePolicyIdentifier
	String identifier = extraParams.getProperty(SignatureProperties.XADES_POLICY_IDENTIFIER_PROP);
	String digestValue = extraParams.getProperty(SignatureProperties.XADES_POLICY_DIGESTVALUE_PROP);
	String qualifier = extraParams.getProperty(SignatureProperties.XADES_POLICY_QUALIFIER_PROP);
	String description = extraParams.getProperty(SignatureProperties.XADES_POLICY_DESCRIPTION_PROP);
	if (GenericUtils.assertStringValue(identifier) && GenericUtils.assertStringValue(digestValue)) {
	    LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.XS_LOG022, new Object[ ] { identifier }));
	    SignaturePolicyIdentifier spi = new es.gob.afirma.signature.xades.SignaturePolicyIdentifierImpl(false, identifier, description, qualifier, digestValue);
	    xades.setSignaturePolicyIdentifier(spi);
	}

	// SignedRole value
	String claimedRole = extraParams.getProperty(SignatureProperties.XADES_CLAIMED_ROLE_PROP);
	if (claimedRole != null) {
	    LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.XS_LOG023, new Object[ ] { claimedRole }));
	    SignerRole signerRole = new SignerRoleImpl();
	    signerRole.addClaimedRole(claimedRole);
	    xades.setSignerRole(signerRole);
	}
	/* ||DataObjectFormat||
	<xsd:element name="DataObjectFormat" type="DataObjectFormatType"/>
	<xsd:complexType name="DataObjectFormatType">
		<xsd:sequence>
		<xsd:element name="Description" type="xsd:string" minOccurs="0"/>
		<xsd:element name="ObjectIdentifier" type="ObjectIdentifierType" minOccurs="0"/>
		<xsd:element name="MimeType" type="xsd:string" minOccurs="0"/>
		<xsd:element name="Encoding" type="xsd:anyURI" minOccurs="0"/>
		</xsd:sequence>
		<xsd:attribute name="ObjectReference" type="xsd:anyURI" use="required"/>
	</xsd:complexType> */
	String dataFormDesc = extraParams.getProperty(SignatureProperties.XADES_DATA_FORMAT_DESCRIPTION_PROP);
	String dataFormEnc = extraParams.getProperty(SignatureProperties.XADES_DATA_FORMAT_ENCODING_PROP);
	String dataFormMime = extraParams.getProperty(SignatureProperties.XADES_DATA_FORMAT_MIME_PROP);
	if (!GenericUtils.assertStringValue(dataFormDesc) && !GenericUtils.assertStringValue(dataFormEnc) && !GenericUtils.assertStringValue(dataFormMime)) {
	    dataFormDesc = "Unknown source data.";
	}

	// Creación del objeto que informa el formato del documento (cuando se
	// creen las referencias se asignará la ref del documento)
	dataObjectFormat = new DataObjectFormatImpl(dataFormDesc, null, dataFormMime, dataFormEnc, null /*se asignará la referencia cuando se cree el objeto de firma*/);
	ArrayList<DataObjectFormat> dofList = new ArrayList<DataObjectFormat>(1);
	dofList.add(dataObjectFormat);
	xades.setDataObjectFormats(dofList);

	return xades;
    }

    /**
     * {@inheritDoc} <br>
     * Optional parameters can be:
     * <ul>
     * <li> {@link es.gob.afirma.signature.SignatureProperties#XADES_POLICY_IDENTIFIER_PROP
     * 	SignatureProperties.XADES_POLICY_IDENTIFIER_PROP}.</li>
     * <li> {@link es.gob.afirma.signature.SignatureProperties#XADES_POLICY_DIGESTVALUE_PROP
     * 	SignatureProperties.XADES_POLICY_DIGESTVALUE_PROP}.</li>
     * <li> {@link es.gob.afirma.signature.SignatureProperties#XADES_POLICY_DESCRIPTION_PROP
     * 	SignatureProperties.XADES_POLICY_DESCRIPTION_PROP}.</li>
     * <li> {@link es.gob.afirma.signature.SignatureProperties#XADES_CLAIMED_ROLE_PROP
     * 	SignatureProperties.XADES_CLAIMED_ROLE_PROP}.</li>
     * <li> {@link es.gob.afirma.signature.SignatureConstants#MF_REFERENCES_PROPERTYNAME SignatureConstants.MF_REFERENCES_PROPERTYNAME}.</li>
     * <li> {@link es.gob.afirma.signature.SignatureProperties#XADES_DATA_FORMAT_DESCRIPTION_PROP SignatureProperties.XADES_DATA_FORMAT_DESCRIPTION_PROP}.</li>
     * <li> {@link es.gob.afirma.signature.SignatureProperties#XADES_DATA_FORMAT_ENCODING_PROP SignatureProperties.XADES_DATA_FORMAT_ENCODING_PROP}.</li>
     * <li> {@link es.gob.afirma.signature.SignatureProperties#XADES_DATA_FORMAT_MIME_PROP SignatureProperties.XADES_DATA_FORMAT_MIME_PROP}.</li>
     * </ul>
     * @see es.gob.afirma.signature.Signer#sign(byte[ ], java.lang.String, java.security.KeyStore.PrivateKeyEntry, String, Properties)
     */
    public byte[ ] sign(byte[ ] data, String algorithm, String signatureFormat, PrivateKeyEntry privateKey, Properties extraParams) throws SigningException {
	String xmlResult = null;
	try {
	    LOGGER.debug(Language.getResIntegra(ILogConstantKeys.XS_LOG024));
	    // Comprobamos parámetros de entrada
	    Properties optionalParams = checkInputParameters(algorithm, signatureFormat, extraParams, data, privateKey);
	    LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.XS_LOG025, new Object[ ] { algorithm, signatureFormat, extraParams }));

	    String uriSignAlgorithm = SIGN_ALGORITHM_URI.get(algorithm);
	    digestAlgorithmRef = SignatureConstants.DIGEST_METHOD_ALGORITHMS_XADES.get(algorithm);

	    // Creación del nodo que contendrá los datos del documento a firmar
	    // (para todos los formatos de firma, excepto externally detached)
	    createDataNode(data, signatureFormat);

	    // Crea el nuevo documento org.w3c.dom.Document xml que contendrá la
	    // firma
	    Document docSignature = null;
	    docSignature = dBFactory.newDocumentBuilder().newDocument();
	    // inserta en el nuevo documento de firma el documento a firmar
	    if (signatureFormat.equals(SIGN_FORMAT_XADES_ENVELOPED)) {
		docSignature.appendChild(docSignature.adoptNode(dataElement));
	    } else {
		docSignature.appendChild(docSignature.createElement(AFIRMA));
		if (signatureFormat.equals(SIGN_FORMAT_XADES_DETACHED)) {
		    // inserta en el nuevo documento de firma el documento a
		    // firmar (en un nodo <CONTENT>)
		    docSignature.getDocumentElement().appendChild(docSignature.adoptNode(dataElement));
		}
	    }
	    // //Se registra el nodo de datos (para se localice el atributo Id
	    // en las referencias)
	    // IdRegister.registerElements(dataElement);

	    // instanciación del objeto Xades que creará la firma
	    XAdES_EPES xadesObject = newXadesObject(docSignature.getDocumentElement(), (X509Certificate) privateKey.getCertificate(), optionalParams);

	    final XadesExt xmlSignature = XadesExt.newInstance(xadesObject);
	    xmlSignature.setDigestMethod(digestAlgorithmRef);
	    xmlSignature.setCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE);

	    // creación de las referencias
	    List<Reference> references = buildReferences(xmlSignature, signatureFormat, optionalParams);

	    // generación de la firma
	    xmlSignature.sign((X509Certificate) privateKey.getCertificate(), privateKey.getPrivateKey(), uriSignAlgorithm, references, "Signature-" + UUID.randomUUID().toString(), null /* TSA */
		    );

	    // Si se esta realizando una firma enveloping quitamos el nodo raíz
	    // y extraemos la firma.
	    if (signatureFormat.equals(SIGN_FORMAT_XADES_ENVELOPING) && docSignature.getElementsByTagNameNS(DSIGNNS, SIGNATURE_NODE_NAME).getLength() == 1) {
		final Document newdoc = dBFactory.newDocumentBuilder().newDocument();
		newdoc.appendChild(newdoc.adoptNode(docSignature.getElementsByTagNameNS(DSIGNNS, SIGNATURE_NODE_NAME).item(0)));
		docSignature = newdoc;
	    }
	    xmlResult = UtilsXML.transformDOMtoString(docSignature);
	    LOGGER.debug("Firma XAdES resultante: \n" + xmlResult);
	    return xmlResult.getBytes();
	} catch (TransformersException e) {
	    String errorMsg = Language.getResIntegra(ILogConstantKeys.XS_LOG006);
	    LOGGER.error(errorMsg, e);
	    throw new SigningException(errorMsg, e);
	} catch (MarshalException e) {
	    String errorMsg = Language.getResIntegra(ILogConstantKeys.XS_LOG006);
	    LOGGER.error(errorMsg, e);
	    throw new SigningException(errorMsg, e);
	} catch (XMLSignatureException e) {
	    String errorMsg = Language.getResIntegra(ILogConstantKeys.XS_LOG006);
	    LOGGER.error(errorMsg, e);
	    throw new SigningException(errorMsg, e);
	} catch (GeneralSecurityException e) {
	    String errorMsg = Language.getResIntegra(ILogConstantKeys.XS_LOG006);
	    LOGGER.error(errorMsg, e);
	    throw new SigningException(errorMsg, e);
	} catch (ParserConfigurationException e) {
	    String errorMsg = Language.getResIntegra(ILogConstantKeys.XS_LOG006);
	    LOGGER.error(errorMsg, e);
	    throw new SigningException(errorMsg, e);
	}

    }

    /**
     * Checks whether input parameters are valid.
     * @param algorithm signature algorithm.
     * @param signatureFormat signature Format
     * @param extraParams optional parameters.
     * @param data input document.
     * @param privateKey private key
     * @return optional parameters validated.
     */
    private Properties checkInputParameters(String algorithm, String signatureFormat, Properties extraParams, byte[ ] data, PrivateKeyEntry privateKey) {
	if (privateKey == null || !GenericUtils.assertStringValue(algorithm) || !GenericUtils.assertStringValue(signatureFormat)) {
	    String errorMsg = Language.getResIntegra(ILogConstantKeys.XS_LOG001);
	    LOGGER.error(errorMsg);
	    throw new IllegalArgumentException(errorMsg);
	}

	if (!SignatureConstants.SUPPORTED_XADES_SIGN_FORMAT.contains(signatureFormat)) {
	    String errorMsg = Language.getFormatResIntegra(ILogConstantKeys.XS_LOG026, new Object[ ] { signatureFormat });
	    LOGGER.error(errorMsg);
	    throw new IllegalArgumentException(errorMsg);
	}
	// Se verifica si el documento a firmar no es nulo para todos los modos
	// de firma (excepto externally detached).
	if (!SIGN_FORMAT_XADES_EXTERNALLY_DETACHED.equals(signatureFormat) && data == null) {
	    String errorMsg = Language.getResIntegra(ILogConstantKeys.XS_LOG001);
	    LOGGER.error(errorMsg);
	    throw new IllegalArgumentException(errorMsg);
	}

	if (!SIGN_ALGORITHM_URI.containsKey(algorithm)) {
	    String errorMsg = Language.getFormatResIntegra(ILogConstantKeys.XS_LOG027, new Object[ ] { algorithm });
	    LOGGER.error(errorMsg);
	    throw new IllegalArgumentException(errorMsg);
	}
	return extraParams == null ? new Properties() : extraParams;

    }

    /**
     * Checks whether input parameters are valid.
     * @param algorithm signature algorithm.
     * @param signature signature.
     * @param extraParams optional parameters.
     * @param privateKey private key
     * @return optional parameters validated.
     */
    private Properties checkInputParameters(String algorithm, Properties extraParams, byte[ ] signature, PrivateKeyEntry privateKey) {
	if (GenericUtils.checkNullValues(signature, privateKey) || !GenericUtils.assertStringValue(algorithm)) {
	    String errorMsg = Language.getResIntegra(ILogConstantKeys.XS_LOG001);
	    LOGGER.error(errorMsg);
	    throw new IllegalArgumentException(errorMsg);
	}

	if (!SIGN_ALGORITHM_URI.containsKey(algorithm)) {
	    String errorMsg = Language.getFormatResIntegra(ILogConstantKeys.XS_LOG027, new Object[ ] { algorithm });
	    LOGGER.error(errorMsg);
	    throw new IllegalArgumentException(errorMsg);
	}
	return extraParams == null ? new Properties() : extraParams;

    }

    /**
     * Verifies a xades signature.
     * @param eSignature digital signature.
     * @return true if signature is valid, false otherwise.
     * @throws SigningException an error happends.
     */
    public boolean verifySignature(byte[ ] eSignature) throws SigningException {
	boolean isValid = false;
	try {
	    LOGGER.debug(Language.getResIntegra(ILogConstantKeys.XS_LOG028));
	    org.apache.xml.security.Init.init();

	    // Transformación de la firma electrónica a objeto
	    // org.w3c.dom.Document
	    DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
	    dbf.setNamespaceAware(true);
	    dbf.setAttribute("http://xml.org/sax/features/namespaces", Boolean.TRUE);
	    DocumentBuilder db = dbf.newDocumentBuilder();
	    Document doc = db.parse(new java.io.ByteArrayInputStream(eSignature));
	    NodeList nl = doc.getElementsByTagNameNS(DSIGNNS, SIGNATURE_NODE_NAME);
	    LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.XS_LOG029, new Object[ ] { nl.getLength() }));
	    // registro de los atributos de tipo ID
	    IdRegister.registerElements(doc.getDocumentElement());
	    for (int i = 0; i < nl.getLength(); i++) {
		Element sigElement = (Element) nl.item(i);
		org.apache.xml.security.signature.XMLSignature signature = new org.apache.xml.security.signature.XMLSignature(sigElement, "");
		//
		// Obtención del certificado o clave pública de la firma para
		// verificar su autenticidad y no repudio.
		KeyInfo keyInfo = signature.getKeyInfo();
		if (keyInfo != null) {
		    X509Certificate cert = keyInfo.getX509Certificate();
		    if (cert != null) {
			// Validamos la firma usando un certificado X509
			if (signature.checkSignatureValue(cert)) {
			    LOGGER.info(Language.getFormatResIntegra(ILogConstantKeys.XS_LOG030, new Object[ ] { signature.getId(), cert.getSubjectDN() }));
			    isValid = true;
			} else {
			    LOGGER.info(Language.getFormatResIntegra(ILogConstantKeys.XS_LOG031, new Object[ ] { signature.getId(), cert.getSubjectDN() }));
			    return false;
			}
		    } else {
			// No encontramos un Certificado intentamos validar por
			// la cláve
			// pública
			PublicKey pk = keyInfo.getPublicKey();
			if (pk != null) {
			    // Validamos usando la clave pública
			    if (signature.checkSignatureValue(pk)) {
				LOGGER.info(Language.getFormatResIntegra(ILogConstantKeys.XS_LOG032, new Object[ ] { pk.getEncoded() }));
				isValid = true;
			    } else {
				LOGGER.info(Language.getResIntegra(ILogConstantKeys.XS_LOG033));
				return false;
			    }
			} else {
			    LOGGER.error(Language.getResIntegra(ILogConstantKeys.XS_LOG034));
			    return false;
			}
		    }
		} else {
		    LOGGER.error(Language.getResIntegra(ILogConstantKeys.XS_LOG035));
		    return false;
		}
	    }
	} catch (Exception e) {
	    String errorMsg = Language.getResIntegra(ILogConstantKeys.XS_LOG007);
	    LOGGER.error(errorMsg, e);
	    throw new SigningException(errorMsg, e);
	}
	return isValid;
    }

    /**
     * Builds a co-signature (signature with signers in parallel) according to XAdES ETSI TS 101 903 standard.
     * @param signature original signature.
     * @param document original data to sign in parallel (not necessary in XAdES signatures, because document is gotten of signature).
     * @param algorithm signature algorithm.
     * @param privateKey entry with private key and certificates chain.
     * @param extraParams Optional parameters can be:
     * <ul>
     * <li> {@link es.gob.afirma.signature.SignatureProperties#XADES_POLICY_IDENTIFIER_PROP
     * 	SignatureProperties.XADES_POLICY_IDENTIFIER_PROP}.</li>
     * <li> {@link es.gob.afirma.signature.SignatureProperties#XADES_POLICY_DIGESTVALUE_PROP
     * 	SignatureProperties.XADES_POLICY_DIGESTVALUE_PROP}.</li>
     * <li> {@link es.gob.afirma.signature.SignatureProperties#XADES_POLICY_DESCRIPTION_PROP
     * 	SignatureProperties.XADES_POLICY_DESCRIPTION_PROP}.</li>
     * <li> {@link es.gob.afirma.signature.SignatureProperties#XADES_CLAIMED_ROLE_PROP
     * 	SignatureProperties.XADES_CLAIMED_ROLE_PROP}.</li>
     * <li> {@link es.gob.afirma.signature.SignatureConstants#MF_REFERENCES_PROPERTYNAME SignatureConstants.MF_REFERENCES_PROPERTYNAME}.</li>
     * <li> {@link es.gob.afirma.signature.SignatureProperties#XADES_DATA_FORMAT_DESCRIPTION_PROP SignatureProperties.XADES_DATA_FORMAT_DESCRIPTION_PROP}.</li>
     * <li> {@link es.gob.afirma.signature.SignatureProperties#XADES_DATA_FORMAT_ENCODING_PROP SignatureProperties.XADES_DATA_FORMAT_ENCODING_PROP}.</li>
     * <li> {@link es.gob.afirma.signature.SignatureProperties#XADES_DATA_FORMAT_MIME_PROP SignatureProperties.XADES_DATA_FORMAT_MIME_PROP}.</li>
     * </ul>
     * 
     * @return new generated signature.
     * @throws SigningException is thrown in error case.
     * @see es.gob.afirma.signature.Signer#coSign(byte[], byte[], java.lang.String, java.security.KeyStore.PrivateKeyEntry, java.util.Properties)
     */
    public byte[ ] coSign(byte[ ] signature, byte[ ] document, String algorithm, PrivateKeyEntry privateKey, Properties extraParams) throws SigningException {
	LOGGER.debug(Language.getResIntegra(ILogConstantKeys.XS_LOG036));
	// Comprobamos parámetros de entrada
	Properties optionalParams = checkInputParameters(algorithm, extraParams, signature, privateKey);
	LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.XS_LOG037, new Object[ ] { algorithm, extraParams }));

	String uriSignAlgorithm = SIGN_ALGORITHM_URI.get(algorithm);
	digestAlgorithmRef = SignatureConstants.DIGEST_METHOD_ALGORITHMS_XADES.get(algorithm);
	Document eSignDoc = null;
	try {
	    // Lectura y parseo de la firma xml.
	    eSignDoc = dBFactory.newDocumentBuilder().parse(new ByteArrayInputStream(signature));
	} catch (Exception e) {
	    String errorMsg = Language.getResIntegra(ILogConstantKeys.XS_LOG005);
	    LOGGER.error(errorMsg, e);
	    throw new SigningException(errorMsg, e);
	}

	// Obtención del modo de firma (Enveloping, Enveloped o Detached)
	String signType = getTypeOfESignature(eSignDoc);
	LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.XS_LOG038, new Object[ ] { signType }));
	Document newCoSignDoc = null;
	// Si se necesita validar el documento a firmar descomentar código abajo
	// indicado. FIX_ME: incluir nodo padre en detached(<content>) y
	// enveloping (<object>) para que coincidan los hashes de las
	// transformadas.
	// //Se valida que el documento a firmar corresponda con el firmado
	// (excepto en el modo de firma externaly detached)
	// if(!SIGN_FORMAT_XADES_EXTERNALLY_DETACHED.equals(signType) &&
	// !validateDocument(eSignDoc, document, signType)) {
	// throw new
	// SigningException("Los datos firmados no corresponden con los datos a firmar");
	// } else {
	// LOGGER.debug("Los datos a firmar coinciden con los datos firmados");
	// }
	LOGGER.debug(Language.getResIntegra(ILogConstantKeys.XS_LOG039));
	try {
	    if (signType.equals(SIGN_FORMAT_XADES_ENVELOPING)) {
		// obtención del documento original a partir de la firma.
		byte[ ] originalDoc = getOriginalDoc(eSignDoc);
		// creación de una firma nueva
		byte[ ] tmpSign = sign(originalDoc, algorithm, SIGN_FORMAT_XADES_ENVELOPING, privateKey, extraParams);
		// Creación de un nodo que contenga ambas firmas (antigua y
		// nueva)
		Element newCoSign = dBFactory.newDocumentBuilder().parse(new ByteArrayInputStream(tmpSign)).getDocumentElement();
		newCoSignDoc = composeCoSignaturesDocument(eSignDoc);
		appendCoSignature(newCoSignDoc, newCoSign.getOwnerDocument());

	    } else if (signType.equals(SIGN_FORMAT_XADES_ENVELOPED)) {
		newCoSignDoc = eSignDoc;
		generateCosign(newCoSignDoc, uriSignAlgorithm, privateKey, signType, optionalParams);

	    } else if (signType.equals(SIGN_FORMAT_XADES_EXTERNALLY_DETACHED)) {
		// No se verifica el documento a firmar pues se firmará el
		// objeto Manifest incluido en la firma xml.
		// generación de la cofirma
		newCoSignDoc = eSignDoc;
		// Búsqueda de los datos a firmar externos(objeto Manifest)
		NodeList manifestObjects = newCoSignDoc.getElementsByTagName(MANIFEST_TAG_NAME);
		optionalParams.put(SignatureConstants.MF_REFERENCES_PROPERTYNAME, manifestObjects.item(0));
		generateCosign(newCoSignDoc, uriSignAlgorithm, privateKey, signType, optionalParams);

	    } else { // modo detached
		newCoSignDoc = eSignDoc;
		// Obtención del ID del nodo que contiene el documento a
		// cofirmar (<CONTENT Id="XX">)
		contentId = getSignedElementIdValue(newCoSignDoc);
		generateCosign(newCoSignDoc, uriSignAlgorithm, privateKey, signType, optionalParams);

	    }
	    String xmlResult = UtilsXML.transformDOMtoString(newCoSignDoc);
	    LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.XS_LOG040, new Object[ ] { xmlResult }));
	    return xmlResult.getBytes();

	} catch (ParserConfigurationException e) {
	    throw new SigningException(Language.getResIntegra(ILogConstantKeys.XS_LOG006), e);
	} catch (TransformersException e) {
	    throw new SigningException(Language.getResIntegra(ILogConstantKeys.XS_LOG006), e);
	} catch (SAXException e) {
	    throw new SigningException(Language.getResIntegra(ILogConstantKeys.XS_LOG006), e);
	} catch (IOException e) {
	    throw new SigningException(Language.getResIntegra(ILogConstantKeys.XS_LOG006), e);
	}
    }

    /**
     * Obtains original document from a signature given.
     * @param eSignature electronic signature.
     * @return orginal document in byte [] format.
     * @throws SigningException if method fails.
     * @throws TransformersException if method fails.
     */
    private byte[ ] getOriginalDoc(Document eSignature) throws SigningException, TransformersException {
	LOGGER.debug(Language.getResIntegra(ILogConstantKeys.XS_LOG041));
	byte[ ] result = null;
	// Obtención de cualquiera de las firmas para obtener el documento
	// original.
	NodeList signNodeList = eSignature.getElementsByTagNameNS(XMLSignature.XMLNS, SIGNATURE_NODE_NAME);
	if (signNodeList.getLength() == 0) {
	    throw new SigningException(Language.getResIntegra(ILogConstantKeys.XS_LOG003));
	}
	// Se selecciona la primera firma.
	Element signatureNode = (Element) signNodeList.item(0);
	// registro de los id de los nodos
	IdRegister.registerElements(signatureNode);

	XMLSignature xmlSign;
	try {
	    xmlSign = new XMLSignatureElement((Element) signatureNode).getXMLSignature();
	} catch (MarshalException e1) {
	    throw new SigningException(Language.getResIntegra(ILogConstantKeys.XS_LOG042), e1);
	}

	// Obtención de la referencia del documento original.
	List<?> references = xmlSign.getSignedInfo().getReferences();
	XMLSignatureInput xmlObjectInput = null;
	for (Object tmp: references) {
	    Reference ref = (Reference) tmp;
	    Attr uriAttr = (Attr) ((DOMReference) ref).getHere();
	    LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.XS_LOG043, new Object[ ] { uriAttr.getValue() }));
	    ResourceResolver res;
	    try {
		res = ResourceResolver.getInstance(uriAttr, null);
		xmlObjectInput = res.resolve(uriAttr, null);
	    } catch (ResourceResolverException e) {
		LOGGER.error(e);
		continue;
	    }

	    Node dsObject = xmlObjectInput.getSubNode();
	    LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.XS_LOG044, new Object[ ] { dsObject.getNodeName() }));
	    if ("ds:Object".equals(dsObject.getNodeName()) && !"QualifyingProperties".equals(dsObject.getFirstChild().getLocalName())) {
		NodeList nodeListObject = dsObject.getChildNodes();
		if (nodeListObject.getLength() == 1) {
		    Node children = dsObject.getFirstChild();
		    result = transformNode(children);
		} else {
		    LOGGER.warn(Language.getResIntegra(ILogConstantKeys.XS_LOG045));
		    StringBuffer buffer = new StringBuffer();
		    for (int i = 0; i < nodeListObject.getLength(); i++) {
			Node children = nodeListObject.item(i);
			byte[ ] nodeValue = transformNode(children);
			if (nodeValue != null) {
			    buffer.append(new String(nodeValue));
			}
		    }
		    result = buffer.toString().getBytes();
		}
		LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.XS_LOG046, new Object[ ] { new String(result) }));
		return result;
	    }
	}
	LOGGER.warn(Language.getResIntegra(ILogConstantKeys.XS_LOG047));
	return result;
    }

    /**
     * Transforms a node with childrens in a string.
     * @param node xml element to transform
     * @return string in byte[] format.
     * @throws TransformersException if method fails.
     */
    private byte[ ] transformNode(Node node) throws TransformersException {
	if (node.getNodeType() == Node.TEXT_NODE || node.getNodeType() == Node.CDATA_SECTION_NODE) {
	    String textValue = ((Text) node).getData();
	    return textValue == null ? null : textValue.getBytes();
	} else if (node.getNodeType() == Node.ELEMENT_NODE) {
	    return UtilsXML.transformDOMtoString((Element) node, true).getBytes();
	} else {
	    return null;
	}

    }

    /**
     * Creates a new cosignature on xml-document given.
     * @param eSign signature with original data.
     * @param uriSignAlgorithm signature algorithm (URI)
     * @param privateKey entry with private key and certificate for sign.
     * @param signType signatue mode.
     * @param optionalParams optional parameters.
     * @throws SigningException in error case performing the co-sign.
     */
    private void generateCosign(Document eSign, String uriSignAlgorithm, PrivateKeyEntry privateKey, String signType, Properties optionalParams) throws SigningException {
	try {
	    // instanciación del objeto Xades que creará la firma
	    XAdES_EPES xadesObject = newXadesObject(eSign.getDocumentElement(), (X509Certificate) privateKey.getCertificate(), optionalParams);

	    XadesExt signBuilder = XadesExt.newInstance(xadesObject);
	    signBuilder.setDigestMethod(digestAlgorithmRef);
	    signBuilder.setCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE);

	    // creación de las referencias
	    List<Reference> references = buildReferences(signBuilder, signType, optionalParams);
	    // generación de la firma
	    signBuilder.sign((X509Certificate) privateKey.getCertificate(), privateKey.getPrivateKey(), uriSignAlgorithm, references, "Signature-" + UUID.randomUUID().toString(), null /* TSA */);

	} catch (Exception e) {
	    throw new SigningException(e);
	}
    }

    /**
     * Method that composes and obtains a XML document with the root node &lt;ROOT_COUNTERSIGNATURES&gt; for adding new cosigns
     * over that (on enveloping signature mode). If the received signature is wrapped in this element, the element is returned without
     * modifications. In another case, the wrapper is created and the sign is added, if isn't null.
     * @param eSignature Parameter that represents the XML signature.
     * @return a XML document with the root node &lt;ROOT_COUNTERSIGNATURES&gt; and the signature as a children node.
     * @throws SigningException If the method fails.
     */
    private Document composeCoSignaturesDocument(Document eSignature) throws SigningException {
	Document newDocument = null;

	try {
	    if (eSignature != null) {
		if (eSignature.getFirstChild().getNodeName().equals(ROOT_TAG)) {
		    return eSignature;
		}
		newDocument = dBFactory.newDocumentBuilder().parse(new ByteArrayInputStream(new String("<" + ROOT_TAG + "/>").getBytes()));
		Node tempNode = newDocument.importNode(eSignature.getFirstChild(), true);
		newDocument.getFirstChild().appendChild(tempNode);
	    } else {
		newDocument = dBFactory.newDocumentBuilder().parse(new ByteArrayInputStream(new String("<" + ROOT_TAG + "/>").getBytes()));
	    }

	    return newDocument;
	} catch (Exception e) {
	    throw new SigningException(Language.getResIntegra(ILogConstantKeys.XS_LOG048), e);
	}
    }

    /**
     * Method that adds a new cosign to the document &lt;ROOT_COUNTERSIGNATURES&gt;.
     * @param document Parameter that represents the document &lt;ROOT_COUNTERSIGNATURES&gt;.
     * @param coSignature Parameter that represents the XML signature to insert.
     * @throws SigningException If the method fails.
     */
    private void appendCoSignature(Document document, Document coSignature) throws SigningException {
	try {
	    Node tempNode = document.importNode(coSignature.getFirstChild(), true);
	    document.getFirstChild().appendChild(tempNode);
	} catch (DOMException e) {
	    throw new SigningException(e);
	}
    }

    /**
     * Method that obtains the value of the attribute Id of the node Content of the wrapper, or "" if the signature hasn't that attribute
     * (wrapper signed on enveloped mode).
     * @param document Parameter that represents the wrapper.
     * @return the value of the attribute Id of the node Content of the wrapper, or "" if the signature hasn't that attribute
     * (wrapper signed on enveloped mode).
     * @throws SigningException If the method fails.
     */
    private String getSignedElementIdValue(Document document) throws SigningException {

	NodeList nodes = document.getElementsByTagName(DETACHED_CONTENT_ELEMENT_NAME);
	if (nodes.getLength() != 1) {
	    throw new SigningException(Language.getResIntegra(ILogConstantKeys.XS_LOG049));
	}
	NamedNodeMap attributes = nodes.item(0).getAttributes();
	for (int i = 0; i < attributes.getLength(); i++) {
	    Node attribute = attributes.item(i);
	    if ("ID".equals(attribute.getNodeName().toUpperCase())) {
		return attribute.getNodeValue();
	    }
	}
	throw new SigningException(Language.getResIntegra(ILogConstantKeys.XS_LOG050));

    }

    /**
     * Method that obtains the XML signature mode.
     * @param eSignature xml signature.
     * @return four possible values:
     * <ul>
     * <li>{@link SignatureConstants#SIGN_FORMAT_XADES_ENVELOPING}</li>
     * <li>{@link SignatureConstants#SIGN_FORMAT_XADES_ENVELOPED}</li>
     * <li>{@link SignatureConstants#SIGN_FORMAT_XADES_DETACHED}</li>
     * <li>{@link SignatureConstants#SIGN_FORMAT_XADES_EXTERNALLY_DETACHED}</li>
     * </ul>
     * @throws SigningException If the method fails.
     */
    private String getTypeOfESignature(Document eSignature) throws SigningException {
	String rootName = eSignature.getDocumentElement().getNodeName();

	// Si el primer nodo (raíz) es <ds:Signature>, entonces es una firma
	// XAdES Enveloping
	if (rootName.equalsIgnoreCase(DS_SIGNATURE_NODE_NAME) || rootName.equals(ROOT_TAG)) {
	    return SIGN_FORMAT_XADES_ENVELOPING;
	} else {
	    // Si contiene un nodo <ds:Manifest> es una firma XAdES Externally
	    // Detached
	    NodeList signatureNodeLs = eSignature.getElementsByTagName(MANIFEST_TAG_NAME);
	    if (signatureNodeLs.getLength() > 0) {
		return SIGN_FORMAT_XADES_EXTERNALLY_DETACHED;
	    }

	    NodeList signsList = eSignature.getElementsByTagNameNS(XMLSignature.XMLNS, SIGNATURE_NODE_NAME);
	    if (signsList.getLength() == 0) {
		throw new SigningException(Language.getResIntegra(ILogConstantKeys.XS_LOG003));
	    }
	    // Si contiene alguna referencia con la URI "" se trata de una firma
	    // XAdES Enveloped
	    Node signatureNode = signsList.item(0);
	    XMLSignature xmlSignature;
	    try {
		xmlSignature = new XMLSignatureElement((Element) signatureNode).getXMLSignature();
	    } catch (MarshalException e) {
		throw new SigningException(Language.getResIntegra(ILogConstantKeys.XS_LOG005), e);
	    }
	    // Tomamos las referencias de la firma
	    List<Reference> references = xmlSignature.getSignedInfo().getReferences();

	    // Buscamos la referencia con URI=""
	    for (int i = 0; i < references.size(); i++) {
		if ("".equals(references.get(i).getURI())) {
		    return SIGN_FORMAT_XADES_ENVELOPED;
		}
		if (references.get(i).getURI() != null && references.get(i).getURI().startsWith("#" + DETACHED_CONTENT_ELEMENT_NAME)) {
		    return SIGN_FORMAT_XADES_DETACHED;
		}
	    }
	    // En última instancia se trata de una tipología de firma
	    // desconocida.
	    throw new SigningException(Language.getResIntegra(ILogConstantKeys.XS_LOG004));
	}
    }

    /**
     * Builds a counter-signature (signature with signers in serial) according to XAdES ETSI TS 101 903 standard.
     * @param signature XAdES signature.
     * @param algorithm signature algorithm.
     * @param privateKey entry with private key and certificates chain.
     * @param optionalParams optional parameters that can be:
     * <ul>
     * <li> {@link es.gob.afirma.signature.SignatureProperties#XADES_POLICY_IDENTIFIER_PROP
     * 	SignatureProperties.XADES_POLICY_IDENTIFIER_PROP}.</li>
     * <li> {@link es.gob.afirma.signature.SignatureProperties#XADES_POLICY_DIGESTVALUE_PROP
     * 	SignatureProperties.XADES_POLICY_DIGESTVALUE_PROP}.</li>
     * <li> {@link es.gob.afirma.signature.SignatureProperties#XADES_POLICY_DESCRIPTION_PROP
     * 	SignatureProperties.XADES_POLICY_DESCRIPTION_PROP}.</li>
     * <li> {@link es.gob.afirma.signature.SignatureProperties#XADES_CLAIMED_ROLE_PROP
     * 	SignatureProperties.XADES_CLAIMED_ROLE_PROP}.</li>
     * <li>{@link es.gob.afirma.signature.SignatureConstants#MF_REFERENCES_PROPERTYNAME SignatureConstants.MF_REFERENCES_PROPERTYNAME}</li>
     * <li> {@link es.gob.afirma.signature.SignatureProperties#XADES_DATA_FORMAT_DESCRIPTION_PROP SignatureProperties.XADES_DATA_FORMAT_DESCRIPTION_PROP}.</li>
     * <li> {@link es.gob.afirma.signature.SignatureProperties#XADES_DATA_FORMAT_ENCODING_PROP SignatureProperties.XADES_DATA_FORMAT_ENCODING_PROP}.</li>
     * <li> {@link es.gob.afirma.signature.SignatureProperties#XADES_DATA_FORMAT_MIME_PROP SignatureProperties.XADES_DATA_FORMAT_MIME_PROP}.</li>
     * </ul>
     * @return a byte array of XAdES countersignature.
     * @throws SigningException if any error ocurrs in the signing process.
     * @see es.gob.afirma.signature.Signer#counterSign(byte[], java.lang.String, java.security.KeyStore.PrivateKeyEntry, java.util.Properties).
     */
    public byte[ ] counterSign(byte[ ] signature, String algorithm, PrivateKeyEntry privateKey, Properties optionalParams) throws SigningException {
	LOGGER.debug(Language.getResIntegra(ILogConstantKeys.XS_LOG051));
	// Se comprueban parámetros de entrada
	Properties extraParams = checkInputParameters(algorithm, optionalParams, signature, privateKey);
	LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.XS_LOG037, new Object[ ] { algorithm, optionalParams }));

	String uriSignAlgorithm = SIGN_ALGORITHM_URI.get(algorithm);
	digestAlgorithmRef = SignatureConstants.DIGEST_METHOD_ALGORITHMS_XADES.get(algorithm);
	Document signDocument = null;
	try {
	    // Lectura y parseo de la firma xml.
	    signDocument = dBFactory.newDocumentBuilder().parse(new ByteArrayInputStream(signature));
	} catch (Exception e) {
	    String errorMsg = Language.getResIntegra(ILogConstantKeys.XS_LOG005);
	    LOGGER.error(errorMsg, e);
	    throw new SigningException(errorMsg, e);
	}

	try {
	    boolean isEnvelopingSign = false;
	    Element rootElement = signDocument.getDocumentElement();

	    if (DS_SIGNATURE_NODE_NAME.equals(rootElement.getNodeName())) {
		isEnvelopingSign = true;
		signDocument = insertAfirmaRootNode(signDocument);
		rootElement = signDocument.getDocumentElement();
	    }
	    // Obtiene todas las firmas.
	    NodeList signList = rootElement.getElementsByTagNameNS(DSIGNNS, SIGNATURE_NODE_NAME);
	    LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.XS_LOG052, new Object[ ] { signList.getLength() }));
	    for (int i = 0; i < signList.getLength(); i++) {
		// se busca los nodos hojas de las firmas (aquellos que no
		// tengan a su vez contrafirmas o subnodos de firma).
		Element signElement = (Element) signList.item(i);
		if (signElement.getElementsByTagNameNS(DSIGNNS, SIGNATURE_NODE_NAME).getLength() == 0) {
		    createCounterSign(signElement, privateKey, uriSignAlgorithm, extraParams);
		    i++;
		}

	    }

	    // si el documento recibido no estaba cofirmado se elimina el nodo
	    // raiz temporal AFIRMA
	    // y se vuelve a dejar como raiz el nodo Signature original
	    if (isEnvelopingSign) {

		Document newdoc = dBFactory.newDocumentBuilder().newDocument();
		newdoc.appendChild(newdoc.adoptNode(signDocument.getElementsByTagNameNS(DSIGNNS, "Signature").item(0)));
		signDocument = newdoc;
	    }
	    // Crea respuesta.
	    String xmlResult = UtilsXML.transformDOMtoString(signDocument);
	    LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.XS_LOG053, new Object[ ] { xmlResult }));
	    return xmlResult.getBytes();

	} catch (ParserConfigurationException e) {
	    String errorMsg = Language.getResIntegra(ILogConstantKeys.XS_LOG006);
	    LOGGER.error(errorMsg, e);
	    throw new SigningException(errorMsg, e);
	} catch (TransformersException e) {
	    String errorMsg = Language.getResIntegra(ILogConstantKeys.XS_LOG006);
	    LOGGER.error(errorMsg, e);
	    throw new SigningException(errorMsg, e);
	}
    }

    /**
     * Creates a new counter signature of given signature.
     * @param signElement xml element represents signature data.
     * @param privateKey entry with private key and certificate chain.
     * @param signatureMethod signature method.
     * @param optionalParams optional parameters.
     * @throws SigningException if there is an error in the process.
     */
    private void createCounterSign(Element signElement, PrivateKeyEntry privateKey, String signatureMethod, Properties optionalParams) throws SigningException {
	try {
	    // Crea un nodo CounterSignature
	    Element counterSignature = createElementNS(signElement.getOwnerDocument(), XADES_SIGNATURE_PREFIX, "CounterSignature");

	    // Recupera o crea un nodo UnsignedSignatureProperties
	    Element unsignedSignatureProperties = retrieveNode(signElement, "UnsignedSignatureProperties");
	    unsignedSignatureProperties.appendChild(counterSignature);

	    // Recupera o crea un nodo UnsignedProperties
	    Element unsignedProperties = retrieveNode(signElement, "UnsignedProperties");
	    unsignedProperties.appendChild(unsignedSignatureProperties);

	    // Inserta el nuevo nodo en QualifyingProperties
	    Element qualifyingProperties = getElementsByTagNameNS(signElement, XADESNS, "QualifyingProperties");
	    qualifyingProperties.appendChild(unsignedProperties);

	    // Obtiene el nodo SignatureValue (para calcular el hash y
	    // referenciarlo)
	    Element signatureValue = getElementsByTagNameNS(signElement, DSIGNNS, "SignatureValue");
	    // registramos el atributo id del nodo referenciado
	    IdRegister.registerAttrId(signatureValue);
	    String idSignValue = signatureValue.getAttribute("Id");

	    // Crea la referencia.
	    List<Transform> transformList = Collections.singletonList(xmlSignatureFactory.newTransform(CanonicalizationMethod.INCLUSIVE, (TransformParameterSpec) null));
	    String referenceId = "Reference-" + UUID.randomUUID().toString();
	    Reference reference = xmlSignatureFactory.newReference("#" + idSignValue, getDigestMethod(), transformList, COUNTER_SIGN_URI, referenceId);

	    // Instancia un objeto XadesExt que creará la firma
	    XAdES_EPES xadesData = newXadesObject(counterSignature, (X509Certificate) privateKey.getCertificate(), optionalParams);

	    final XadesExt xadesSigner = XadesExt.newInstance(xadesData);
	    xadesSigner.setDigestMethod(digestAlgorithmRef);
	    xadesSigner.setCanonicalizationMethod(CanonicalizationMethod.INCLUSIVE);

	    LOGGER.debug(Language.getFormatResIntegra(ILogConstantKeys.XS_LOG054, new Object[ ] { signElement.getAttribute("Id"), idSignValue }));
	    // generación de la firma
	    xadesSigner.sign((X509Certificate) privateKey.getCertificate(), privateKey.getPrivateKey(), signatureMethod, Collections.singletonList(reference), "Signature-" + UUID.randomUUID().toString(), null /* TSA */);
	} catch (GeneralSecurityException e) {
	    String errorMsg = Language.getResIntegra(ILogConstantKeys.XS_LOG006);
	    LOGGER.error(errorMsg, e);
	    throw new SigningException(errorMsg, e);
	} catch (MarshalException e) {
	    String errorMsg = Language.getResIntegra(ILogConstantKeys.XS_LOG006);
	    LOGGER.error(errorMsg, e);
	    throw new SigningException(errorMsg, e);
	} catch (XMLSignatureException e) {
	    String errorMsg = Language.getResIntegra(ILogConstantKeys.XS_LOG006);
	    LOGGER.error(errorMsg, e);
	    throw new SigningException(errorMsg, e);
	}
    }

    /**
     * Retrieves a node from a document given. If these node not exists a new node will be created.
     * @param rootNode root node.
     * @param nodeName node name to search.
     * @return element searched.
     */
    private Element retrieveNode(Element rootNode, String nodeName) {
	Element element;
	final NodeList nodeList = rootNode.getElementsByTagNameNS(XADESNS, nodeName);
	if (nodeList.getLength() == 0) {
	    element = createElementNS(rootNode.getOwnerDocument(), XADES_SIGNATURE_PREFIX, nodeName);
	} else {
	    element = (Element) nodeList.item(0);
	}
	return element;
    }

    /**
     * Creates an element of the given qualified name and namespace URI.
     * @param document document.
     * @param prefix prefix.
     * @param nodeName node name to create.
     * @return the new element created.
     */
    private Element createElementNS(Document document, String prefix, String nodeName) {
	Element element = document.createElementNS(DSIGNNS, nodeName);
	element.setPrefix(prefix);
	return element;
    }

    /**
     * Returns the first descendant <code>Element</code> with a given local name and namespace URI in
     * document order.
     * @param element element to search.
     * @param namespace The namespace URI of the elements to match on
     * @param nodeName name of node.
     * @return the first descendant matched.
     * @throws SigningException if element searched is not found.
     */
    private Element getElementsByTagNameNS(Element element, String namespace, String nodeName) throws SigningException {
	final NodeList nodeList = element.getElementsByTagNameNS(namespace, nodeName);
	if (nodeList.getLength() == 0) {
	    throw new SigningException(Language.getFormatResIntegra(ILogConstantKeys.XS_LOG055, new Object[ ] { nodeName }));
	} else {
	    return (Element) nodeList.item(0);
	}
    }

    /**
     * Inserts a new root node with name "AFIRMA".
     * @param signatureDoc document for insert new node.
     * @return a new document with node added.
     * @throws ParserConfigurationException in error case.
     */
    private Document insertAfirmaRootNode(final Document signatureDoc) throws ParserConfigurationException {

	// Crea un nuevo documento con la raiz "AFIRMA"
	final Document docAfirma = dBFactory.newDocumentBuilder().newDocument();
	final Element rootAfirma = docAfirma.createElement(AFIRMA);

	// Inserta el documento pasado por parametro en el nuevo documento
	rootAfirma.appendChild(docAfirma.adoptNode(signatureDoc.getDocumentElement()));
	docAfirma.appendChild(rootAfirma);

	return docAfirma;
    }

    /**
     * Validates if a input document matches with a document signed.
     * @param eSignDoc document signed.
     * @param inputData input document.
     * @param signType xml signature type.
     * @return true if documents match (their hashes) and false otherwise.
     */
    @SuppressWarnings("unused")
    private boolean validateDocument(Document eSignDoc, byte[ ] inputData, String signType) {
	// Obtención de cualquiera de las firmas para verificar si el documento
	// a firmar es correcto.
	if (eSignDoc.getElementsByTagNameNS(XMLSignature.XMLNS, SIGNATURE_NODE_NAME).getLength() == 0) {
	    LOGGER.warn(Language.getResIntegra(ILogConstantKeys.XS_LOG003));
	    return false;
	}
	Element signatureNode = (Element) eSignDoc.getElementsByTagNameNS(XMLSignature.XMLNS, SIGNATURE_NODE_NAME).item(0);
	XMLSignature xmlSign;
	try {
	    xmlSign = new XMLSignatureElement((Element) signatureNode).getXMLSignature();
	} catch (MarshalException e1) {
	    LOGGER.warn(Language.getResIntegra(ILogConstantKeys.XS_LOG042), e1);
	    return false;
	}

	// Obtención de la referencia que contiene los datos del digest del
	// documento.
	Reference reference = getReferenceByUri(xmlSign.getSignedInfo().getReferences(), signType);
	if (reference == null) {
	    LOGGER.warn(Language.getResIntegra(ILogConstantKeys.XS_LOG056));
	    return false;
	}
	// se comprueba si es el documento a firmar es de tipo xml o binario
	try {
	    Document document = dBFactory.newDocumentBuilder().parse(new ByteArrayInputStream(inputData));
	    return verifyDocumentDigest(reference, document, xmlSign);
	} catch (Exception e) {
	    // no es un documento xml; se considera binario.
	    return verifyDocumentDigest(reference, inputData);
	}

    }

    /**
     * Validates if a xml document matches with document signed.
     * @param reference reference data for document signed.
     * @param inputXml xml document to sign.
     * @param xmlSign instance represents the signature data.
     * @return true if the hashes match and false otherwise.
     */
    private boolean verifyDocumentDigest(Reference reference, Document inputXml, XMLSignature xmlSign) {
	try {

	    // Obtención Clave pública empleada en la firma
	    PublicKey publicKey = ((KeyValue) xmlSign.getKeyInfo().getContent().get(0)).getPublicKey();

	    // Creación contexto
	    XMLValidateContext valContext = new DOMValidateContext(publicKey, inputXml);

	    // Obtención del algoritmo empleado para calcular el hash del
	    // documento.
	    String digestAlg = CryptoUtil.translateXmlDigestAlgorithm(reference.getDigestMethod().getAlgorithm());
	    MessageDigest md = MessageDigest.getInstance(digestAlg);
	    md.reset();
	    // Instanciamos un stream que realice el digest de los datos.
	    DigesterOutputStream dos = new DigesterOutputStream(md);
	    OutputStream os = new UnsyncBufferedOutputStream(dos);

	    // Instanciación objeto Data que contendrá el xml a transformar
	    XMLSignatureInput xmlInputTmp = new XMLSignatureInput(inputXml);
	    xmlInputTmp.setSourceURI("");
	    xmlInputTmp.setExcludeComments(true);
	    xmlInputTmp.setMIMEType("text/xml");
	    Data data = new ApacheNodeSetData(xmlInputTmp);

	    // Aplicación de todas las transformadas indicadas en el nodo
	    // Reference.
	    List<?> transforms = reference.getTransforms();
	    for (int i = 0, size = transforms.size(); i < size; i++) {
		DOMTransform transform = (DOMTransform) transforms.get(i);
		if (i < size - 1) {
		    data = transform.transform(data, valContext);
		} else {
		    data = transform.transform(data, valContext, os);
		}
	    }

	    // obtención del digest de los datos transformados
	    if (data != null) {
		((ApacheData) data).getXMLSignatureInput().updateOutputStream(os);
		os.flush();
	    }
	    byte[ ] calculatedDigest = dos.getDigestValue();
	    // Comparación de hash calculado y el indicado en la referencia de
	    // la firma
	    return CryptoUtil.equalHashes(reference.getDigestValue(), calculatedDigest);
	} catch (Exception e) {
	    LOGGER.warn(Language.getResIntegra(ILogConstantKeys.XS_LOG056), e);
	    return false;
	}
    }

    /**
     * Validates if a binary document matches with document signed.
     * @param reference reference data for document signed.
     * @param binaryDoc binary document to sign.
     * @return true if the hashes match and false otherwise.
     */
    private boolean verifyDocumentDigest(Reference reference, byte[ ] binaryDoc) {
	DigestMethod digestMethod = reference.getDigestMethod();
	String digestAlg = CryptoUtil.translateXmlDigestAlgorithm(digestMethod.getAlgorithm());
	byte[ ] digestCalculated;
	try {
	    digestCalculated = CryptoUtil.digest(digestAlg, binaryDoc);
	} catch (SigningException e) {
	    LOGGER.warn(e);
	    return false;
	}
	byte[ ] digestValue = reference.getDigestValue();
	return CryptoUtil.equalHashes(digestCalculated, digestValue);

    }

    /**
     * Gets a reference from a reference list by signature type/mode.
     * @param references list of refeences.
     * @param signatureType signature types.
     * @return the refence found.
     */
    private Reference getReferenceByUri(List<Reference> references, String signatureType) {
	String prefix = "";
	if (SIGN_FORMAT_XADES_ENVELOPING.equals(signatureType)) {
	    prefix = "#" + ENVELOPING_OBJECT_ELEMENT_NAME;
	} else if (SIGN_FORMAT_XADES_DETACHED.equals(signatureType)) {
	    prefix = "#" + DETACHED_CONTENT_ELEMENT_NAME;
	}
	for (Reference reference: references) {
	    if (reference.getURI().startsWith(prefix)) {
		return reference;
	    }
	}
	return null;
    }

}

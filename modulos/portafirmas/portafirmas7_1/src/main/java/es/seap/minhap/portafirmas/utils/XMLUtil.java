/* Copyright (C) 2012-13 MINHAP, Gobierno de España
   This program is licensed and may be used, modified and redistributed under the terms
   of the European Public License (EUPL), either version 1.1 or (at your
   option) any later version as soon as they are approved by the European Commission.
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
   or implied. See the License for the specific language governing permissions and
   more details.
   You should have received a copy of the EUPL1.1 license
   along with this program; if not, you may find it at
   http://joinup.ec.europa.eu/software/page/eupl/licence-eupl */

package es.seap.minhap.portafirmas.utils;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.FactoryConfigurationError;
import javax.xml.parsers.ParserConfigurationException;
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
import javax.xml.xpath.XPathFactoryConfigurationException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class XMLUtil {

	protected static final Log logger = LogFactory.getLog(XMLUtil.class);
	public static final Charset UTF8_CHARSET = Charset.forName("UTF-8");

	/**
	 * Comprueba si un nodo tiene un atributo "MimeType" que contenga la cadena "text/tcn"
	 * @param node
	 * @return
	 */
	public static boolean isTextTcnMimeType (Node node) {
		// TODO nodeValue funciona?
		boolean ret = false;
		if (node.getAttributes() != null &&
				node.getAttributes().getNamedItem("MimeType") != null &&
				node.getAttributes().getNamedItem("MimeType").getNodeValue() != null &&
				node.getAttributes().getNamedItem("MimeType").getNodeValue().contains("text/tcn")) {
			ret = true;
		}
		return ret;
	}

	/**
	 * Obtiene el árbol XML de una fuente, que puede ser File, InputStream o array de bytes.
	 * @param source fuente de la que se quiere obtener el árbol XML.
	 * @return árbol XML.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Document getDOMDocument(Object source, boolean namespaceAware) throws ParserConfigurationException, SAXException, IOException{
		logger.debug ("getDOMDocument init");
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		dbf.setNamespaceAware(namespaceAware);
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = null;
		if (source instanceof File) {
			File f = (File) source;
			doc = db.parse(f);			
		} else if (source instanceof InputStream) {
			InputStream is = (InputStream) source;
			doc = db.parse(is);
		} else if (source instanceof byte[]) {
			byte[] bytes = (byte[]) source;

			ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
			doc = db.parse(bis);

			bis.close();
		}
		logger.debug ("getDOMDocument end");
		return doc;
	}

	/**
	 * Comprueba si se cumplen una serie de expresiones xpath.
	 * @param doc Documento DOM
	 * @param xpathExpressions array de expresiones xpath.
	 * @param xpath instancia de xpath
	 * @return true si se cumplen todas, false en caso contrario.
	 * @throws XPathExpressionException si alguna de las expresiones no es correcta.
	 */
	public static boolean seCumplen (Document doc, String[] xpathExpressions, javax.xml.xpath.XPath xpath) throws XPathExpressionException {
		Boolean seCumplen = true;

		int i=0; 

		while (i < xpathExpressions.length && seCumplen) {
			String expr = xpathExpressions[i];
			seCumplen = (Boolean) xpath.evaluate(expr, doc.getDocumentElement(), javax.xml.xpath.XPathConstants.BOOLEAN);
			xpath.reset();
			i++;
		}

		return seCumplen;
	}

	public static boolean isXMLDSigSignature (Document doc, javax.xml.xpath.XPath xpath) throws XPathFactoryConfigurationException, XPathExpressionException {
		//Existe el nodo Signature, que tiene como hijo SignedInfo. La primera es para XMLDSig Detached y Enveloped, la segunda para Enveloping
		// Existe el nodo Signature, que tiene como hijo SignedValue. La primera es para XMLDSig Detached y Enveloped, la segunda para Enveloping
		//String[] xpathExpressions = new String[]{"Signature/SignedInfo | /Signature/SignedInfo",
		//								   "Signature/SignatureValue | /Signature/SignatureValue"};

		//Existe el nodo Signature, que tiene como hijo SignedInfo, y SignedValue
		String[] xpathExpressions = new String[]{"//Signature/SignedInfo", "//Signature/SignatureValue"};

		return XMLUtil.seCumplen(doc, xpathExpressions, xpath);
	}

	public static boolean isXMLDSigEnvelopedOrDetached (Document doc, javax.xml.xpath.XPath xpath) throws XPathFactoryConfigurationException, XPathExpressionException {

		// Nodo SignedInfo y SignatureValue, hijo de Signature, que no tiene por qué ser root del XML.
		String[] xpathExpressions = new String[]{"Signature/SignedInfo",
		"Signature/SignatureValue"};

		return XMLUtil.seCumplen(doc, xpathExpressions, xpath);
	}

	public static boolean isXMLDSigEnveloping (Document doc, javax.xml.xpath.XPath xpath) throws XPathFactoryConfigurationException, XPathExpressionException {

		// Nodo SignedInfo y SignatureValue, hijo de Signature, que tiene que ser root del XML.
		String[] xpathExpressions = new String[]{"/Signature/SignedInfo",
		"/Signature/SignatureValue"};

		return XMLUtil.seCumplen(doc, xpathExpressions, xpath);

	}

	public static boolean isXMLDSigEnveloped (Document doc, javax.xml.xpath.XPath xpath) throws XPathFactoryConfigurationException, XPathExpressionException {
		String[] xpathExpressions = new String[]{"//Signature/SignedInfo", "//Signature/SignatureValue", "//Transforms/Transform[(@Algorithm|@algorithm|@ALGORITHM)='http://www.w3.org/2000/09/xmldsig#enveloped-signature']" };
		return XMLUtil.seCumplen(doc, xpathExpressions, xpath);
	}


	public static NodeList getNodeListByXpathExpression (Object obj, String xpathExpression) throws XPathFactoryConfigurationException, XPathExpressionException {

		javax.xml.xpath.XPath xpath = javax.xml.xpath.XPathFactory.newInstance("http://java.sun.com/jaxp/xpath/dom").newXPath();

		NodeList nodeList = (NodeList) xpath.evaluate(xpathExpression, obj, javax.xml.xpath.XPathConstants.NODESET);

		return nodeList;

	}

	public static Node getNodeByXpathExpression (Document doc, String xpathExpression) throws XPathFactoryConfigurationException, XPathExpressionException {

		javax.xml.xpath.XPath xpath = javax.xml.xpath.XPathFactory.newInstance("http://java.sun.com/jaxp/xpath/dom").newXPath();

		Node node = (Node) xpath.evaluate(xpathExpression, doc, javax.xml.xpath.XPathConstants.NODE);

		return node;
	}



	/**
	 * Comprueba si un nodo tiene un atributo "MimeType" que contenga la cadena "hash"
	 * @param node
	 * @return
	 */
	public static boolean isHashMimeType (Node node) {
		// TODO nodeValue funciona?
		boolean ret = false;
		if (node.getAttributes() != null &&
				node.getAttributes().getNamedItem("MimeType") != null &&
				node.getAttributes().getNamedItem("MimeType").getNodeValue() != null &&
				node.getAttributes().getNamedItem("MimeType").getNodeValue().contains("hash")) {
			ret = true;
		}
		return ret;
	}

	/**
	 * Devuelve el valor del atributo "Encoding" del nodo.
	 * @param node
	 * @return
	 */
	public static String getEncoding (Node node) {
		String encoding = null;
		encoding = getAttribute(node, "Encoding");
		if (encoding == null) {
			encoding = getAttribute(node, "encoding");
		}
		return encoding;
	}

	public static String getHashAlgorithm (Node node) {
		return getAttribute(node, "hashAlgorithm");		
	}

	/*public static String getEncoding (Node node) {
	return getAttribute(node, "Encoding");
}*/

	public static String getAttribute (Node node, String attName) {
		String attValue = null;

		if (node.getAttributes() != null &&
				node.getAttributes().getNamedItem(attName) != null) {
			//attValue = node.getAttributes().getNamedItem(attName).getTextContent();
			//attValue = node.getAttributes().getNamedItem(attName).getTextContent();
			attValue = node.getAttributes().getNamedItem(attName).getNodeValue();
			attValue = node.getAttributes().getNamedItem(attName).getNodeValue();
		}

		return attValue;
	}

	public static boolean isXMLMimeType (Node node) {
		String mime = null;
		boolean ret = false;

		if (node.getAttributes() != null &&
				node.getAttributes().getNamedItem("MimeType") != null) {
			//mime = node.getAttributes().getNamedItem("MimeType").getTextContent();
			mime = node.getAttributes().getNamedItem("MimeType").getNodeValue();
		}

		if ("text/xml".equalsIgnoreCase(mime) || "application/xml".equalsIgnoreCase(mime)) {
			ret = true;
		}

		return ret;
	}


	/**
	 * Convierte un documento XML de DOM a un ByteArrayOutputStream.
	 * @param doc
	 * @return
	 * @throws UnsupportedEncodingException
	 * @throws TransformerException
	 */
	public static ByteArrayOutputStream getBytesFromNode (Node node, String encoding) throws UnsupportedEncodingException, TransformerException {
		/*TransformerFactory tf = TransformerFactory.newInstance();		
	Transformer t = tf.newTransformer();

    t.setOutputProperty(OutputKeys.INDENT, "yes");
    t.setOutputProperty(OutputKeys.ENCODING, encoding);
    t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");*/

		Transformer t = getGenericTransformer ("yes", encoding, "2");

		ByteArrayOutputStream bout = new ByteArrayOutputStream();
		t.transform(new DOMSource(node),  new StreamResult(new OutputStreamWriter(bout, encoding)));

		return bout;
	}

	public static String getStringFromNode(Node node, String encoding) throws TransformerException {
		/*TransformerFactory tf = TransformerFactory.newInstance();		
	Transformer t = tf.newTransformer();

    t.setOutputProperty(OutputKeys.INDENT, "yes");
    t.setOutputProperty(OutputKeys.ENCODING, encoding);
    t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "2");*/


		Transformer t = getGenericTransformer ("yes", encoding, "2");
		StringWriter sw = new StringWriter();        

		t.transform(new DOMSource(node), new StreamResult(sw));
		return sw.toString();
	}

	private static Transformer getGenericTransformer (String indent, String encoding, String indent_amount) throws TransformerConfigurationException {
		TransformerFactory tf = TransformerFactory.newInstance();		
		Transformer t = tf.newTransformer();

		t.setOutputProperty(OutputKeys.INDENT, indent);
		t.setOutputProperty(OutputKeys.ENCODING, encoding);
		t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", indent_amount);

		return t;

	}

	/**
	 * Elimina los nodos de un documento que cumplan una determinada expresión XPATH
	 * @param xpathExpr
	 * @param doc
	 * @throws XPathFactoryConfigurationException
	 * @throws XPathExpressionException
	 */
	public static void removeNodesFromDocument (String xpathExpr, Document doc) throws XPathFactoryConfigurationException, XPathExpressionException {
		NodeList nodes = XMLUtil.getNodeListByXpathExpression(doc, xpathExpr);

		// Los eliminamos
		if (nodes != null) {
			for (int i=0; i < nodes.getLength(); i++) {
				Node n = nodes.item(i);
				n.getParentNode().removeChild(n);
			}
		}

		doc.normalize();
	}

	public static String getvalorNodoDatosXML(String dataXml, String expresionXPath)throws Exception
	{
		return XMLUtil.getContentNode(dataXml.getBytes("UTF-8"), expresionXPath);

	}

	public static String getContentNode(byte[] data, String path) throws Exception {

		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = factory.newDocumentBuilder();
		Document doc = builder.parse(new ByteArrayInputStream(data));
		XPathFactory xpf = XPathFactory.newInstance();
		XPath xpath = xpf.newXPath();

		XPathExpression exprFirst = xpath.compile(path);

		return (String) exprFirst.evaluate(doc, XPathConstants.STRING);

	}
	
	/** Método para recuperar la versión de facturae
	 * @param document Documento a comprobar
	 * @return Cadena con la version de facturae
	 * @throws IOException en caso de no poder parsear el documento por haber algún problema con los bytes de entrada.
	 */
	public static String checkVersionFacturae(Object document) {
		boolean parseError = false;
		String version = null;

		Document dom = null;
		try {
			dom = XMLUtil.getDOMDocument(document, true);
			// Si se produce alguna excepción al parsear, significará que no es un XML y por tanto no será una firma XML.
		} catch (ParserConfigurationException pce) {
			parseError = true;
		} catch (SAXException se){
			parseError = true;
		} catch (IOException ioe) {
			parseError = true;
		}

		// Si se ha podido parsear el documento, buscamos el nodo con la versión
		if (!parseError) {
	        try {
				//Se obtiene la raiz
				Element rootNode = dom.getDocumentElement();
 
				//Se obtiene la lista de hijos
				NodeList listaNodos = rootNode.getChildNodes();
 
				//Se recorre la lista de hijos para encontrar el nodo FileHeader
				boolean encontradoFileHeader = false;
				boolean encontradoSchemaVersion = false;
				for ( int i = 0; i < listaNodos.getLength() && !encontradoFileHeader && !encontradoSchemaVersion; i++ ){
				    //Se obtiene el nodo
				    Node nodo = listaNodos.item(i);
				    if (nodo!=null){
				        String nodeName = nodo.getNodeName();
						if (nodeName != null && "FileHeader".equals(nodeName)) {
							encontradoFileHeader = true;
							NodeList listaNodos2 = nodo.getChildNodes();
							for ( int j = 0; j < listaNodos2.getLength() && !encontradoSchemaVersion; j++ ){
					            //Se obtiene el nodo
					            Node nodo2 = listaNodos2.item(j);
					            
					            if (nodo2 != null) {
						            String nodeName2 = nodo2.getNodeName();
									if (nodeName2 != null && "SchemaVersion".equals(nodeName2) /*&& nodo2.getNodeValue()!=null*/) {
										Element elemento2 = (Element) listaNodos2.item(j);
										version = elemento2.getFirstChild().getNodeValue();
										encontradoSchemaVersion = true;
									}
								}
					        }
						}
				    }
				}
			} catch (Exception e) {
				
			}
			
		}
		
		return version;		
	}

	public static byte[] obtenerNodoContenidoEni(Object document) {
		boolean parseError = false;
		String valorBinario = null;
		byte[] retorno = null;
		Document dom = null;
		try {
			dom = XMLUtil.getDOMDocument(document, true);
			// Si se produce alguna excepción al parsear, significará que no es un XML y por tanto no será una firma XML.
		} catch (ParserConfigurationException pce) {
			parseError = true;
		} catch (SAXException se){
			parseError = true;
		} catch (IOException ioe) {
			parseError = true;
		}

		// Si se ha podido parsear el documento, buscamos el nodo con el contenido
		if (!parseError) {
	        try {
				//Se obtiene la raiz
				Element rootNode = dom.getDocumentElement();
 
				//Se obtiene la lista de hijos
				NodeList listaNodos = rootNode.getChildNodes();
 
				//Se recorre la lista de hijos para encontrar el nodo FileHeader
				boolean encontradoContenido = false;
				boolean encontradoValorBinario = false;
				for ( int i = 0; i < listaNodos.getLength() && !encontradoContenido && !encontradoValorBinario; i++ ){
				    //Se obtiene el nodo
				    Node nodo = listaNodos.item(i);
				    if (nodo!=null){
				        String nodeName = nodo.getNodeName();
						if (nodeName != null && "contenido".equals(nodeName)) {
							encontradoContenido = true;
							NodeList listaNodos2 = nodo.getChildNodes();
							for ( int j = 0; j < listaNodos2.getLength() && !encontradoValorBinario; j++ ){
					            //Se obtiene el nodo
					            Node nodo2 = listaNodos2.item(j);
					            
					            if (nodo2 != null) {
						            String nodeName2 = nodo2.getNodeName();
									if (nodeName2 != null && ("ValorBinario".equals(nodeName2) || "DatosXML".equals(nodeName2) || ("referenciaFichero".equals(nodeName2)))) {
										Element elemento2 = (Element) listaNodos2.item(j);
										valorBinario = elemento2.getFirstChild().getNodeValue();
										if ("ValorBinario".equals(nodeName2)) {
											//Hay que decodificarlo, está en base64
											retorno = Base64.decodeBase64(valorBinario);
										} else if ("DatosXML".equals(nodeName2)) {
											retorno = valorBinario.getBytes();
										} else {
											String referencia = valorBinario.substring(1);
											String firma = getNodoReferencia(dom , referencia);
											//Hay que decodificarlo, está en base64
											retorno = Base64.decodeBase64(firma);
										}
										encontradoValorBinario = true;
									}
								}
					        }
						}
				    }
				}
			} catch (Exception e) {
				
			}
		}
		
		return retorno;		
	}

	public static String getNodoReferencia (Document dom, String referencia) throws ParserConfigurationException, SAXException, IOException {
		String retorno = null;
		boolean encontradaReferencia = false;
		boolean encontradaFirma = false;
		//Document dom = XMLUtil.getDOMDocument(xml, true);
		NodeList listaNodos = dom.getElementsByTagNameNS("*", "firma");
		for ( int j = 0; j < listaNodos.getLength() && !encontradaReferencia; j++ ){
			Node nodo = listaNodos.item(j);
			if (nodo!=null) {
				String nodeName = nodo.getNodeName();
				Element elemento = (Element) nodo;
				String value = elemento.getFirstChild().getNodeValue();
				String idElemento = elemento.getAttribute("Id");
				if (referencia.equals(idElemento) ){
					encontradaReferencia = true;
					NodeList listaNodos2 = elemento.getElementsByTagName("*");
					for ( int i = 0; i < listaNodos2.getLength() && !encontradaFirma; i++ ){
						Node firma = listaNodos2.item(i);
						if (firma != null) {
				            String nodeName2 = firma.getNodeName();
				            if ("ns3:FirmaBase64".equals(nodeName2)){
				            	encontradaFirma = true;
				            	Element firmaElment = (Element)firma;
								retorno = firmaElment.getFirstChild().getNodeValue();				            	
				            }
						}
					}
				}
			}
		}
		return retorno;
	}
	
	public static String getNodeTextContent(byte[] xml, String tagName) throws ParserConfigurationException, SAXException, IOException {
		Document dom = XMLUtil.getDOMDocument(xml, true);
		Node nodo = dom.getElementsByTagName(tagName).item(0);
		NodeList listaNodos = nodo.getChildNodes();
		return listaNodos.item(0).getNodeValue();
	}
	
}


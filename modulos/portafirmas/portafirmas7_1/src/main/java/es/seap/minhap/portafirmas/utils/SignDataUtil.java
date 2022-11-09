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
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.IOUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import es.gob.afirma.core.AOException;
import es.gob.afirma.core.signers.AOSigner;
import es.gob.afirma.core.signers.AOSignerFactory;
import es.gob.afirma.core.util.tree.AOTreeModel;
import es.gob.afirma.signers.cades.AOCAdESSigner;
import es.gob.afirma.signers.pades.AOPDFSigner;
import es.gob.afirma.signers.xades.AOFacturaESigner;
import es.gob.afirma.signers.xades.AOXAdESSigner;
import es.gob.afirma.signers.xmldsig.AOXMLDSigSigner;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class SignDataUtil {

	protected final Log log = LogFactory.getLog(getClass());

	/**
	 * Método para detectar si un documento es, en realidad, una firma (una firma que no sea firma PDF).
	 * @param document Documento a comprobar
	 * @return Cadena con el tipo de firma, null si no es una firma
	 * @throws IOException en caso de no poder parsear el documento por haber algún problema con los bytes de entrada.
	 */
	public String checkIsSign(Object document) {
		boolean parseError = false;
		String isSigned = null;

		Document dom = null;
		try {
			dom = XMLUtil.getDOMDocument(document, true);
			// Si se produce alguna excepción al parsear, significará que no es un XML y por tanto no será una firma XML.
		} catch (Exception e) {
			//log.warn("El documento no parece que sea una firma con estructura de árbol:", e);
			parseError = true;
		}

		// Si se ha podido parsear el documento, comprobamos que se trata de una firma XML.
		if (!parseError) {
			isSigned = this.isDocumentXMLSignDetached(dom);
			if (isSigned == null) {
				isSigned = this.isDocumentXMLSignEnveloped(dom);
			}
		} else {
			try {
				isSigned = isOtherSign(getBytes(document));
			} catch (Exception e) {
				//log.warn("El documento tampoco es otro tipo de firma:", e);
				isSigned = null;
			}
		}
		return isSigned;		
	}

	/**
	 * Obtiene un objeto SignData a partir de una firma.
	 * @param sign firma.
	 * @return un objeto que contiene el documento que se ha firmado y el mime de ese documento.
	 * @throws Exception cuando no se puedan obtener los datod e la firma.
	 */
	public SignData getDataFromSign (byte[] sign) throws Exception{

		byte[] datosFirma = null;
		String tipoFirma = null;		

		try {
			datosFirma = getDataSignedFromSignXML (sign);
			tipoFirma = Constants.SIGN_FORMAT_XADES_IMPLICIT;

		} catch (Exception e) {
			log.error("Se ha producido un error al obtener los datos firmados de un XML implícito, ", e);
			es.gob.afirma.core.signers.AOSigner signer = obtenerSigner(sign);

			datosFirma = getDataSignedFromSignerGenerico(signer, sign);
			tipoFirma = getTipoFirmaFromSignerGenerico(signer);
		}

		String mime = null;

		//Si no es un XADES y si un CADES, ira al catch y seguira el proceso.

		try {

			/**
			 * Se obtiene el documento XML y se busca si existe el nodo <etsi:MimeType> y, 
			 * si se encuentra, se le asigna el valor "text/tcn" al atributo mime del 
			 * objeto SignData
			 */
			Document dom = XMLUtil.getDOMDocument(sign, true);
			NodeList mimeTypeNodeList = dom.getElementsByTagNameNS("*",  "MimeType");
			if (mimeTypeNodeList != null){
				for(int i=0;i<mimeTypeNodeList.getLength();i++){
					Node mimeTypeNode = mimeTypeNodeList.item(i).getFirstChild();
					if("text/tcn".equals(mimeTypeNode.getNodeValue()) && 
							(mimeTypeNode.getParentNode().getParentNode().getNodeName().contains("DataObjectFormat")) && 
							(mimeTypeNode.getParentNode().getParentNode().getParentNode().getNodeName().contains("SignedDataObjectProperties"))){
						mime = mimeTypeNode.getNodeValue();
					}
				}
			}

		} catch (Exception e) {
			log.error("ERROR: SignDataUtil.getDataFromSign(): el objeto de firma no es un XML, ", e);
		}

		try {
			if(mime == null || (!"text/tcn".equals(mime))){
				mime = Util.getInstance().getMime(datosFirma);
			}
		} catch (MagicParseException|MagicMatchNotFoundException|MagicException e) {
			log.error("ERROR: Se ha producido un error obteniendo una coincidencia del stream de datos", e);
			mime = Constants.AFIRMA_MIME_GENERICO;
		}
		SignData signData = new SignData(mime, datosFirma);
		signData.setTipoFirma(tipoFirma);
		return signData;
	}

	/**
	 * Obtiene los bytes a partir de un objeto.
	 * @param source Objeto del que se quieren obtener los bytes.
	 * @return array de bytes
	 * @throws IOException si no se puede leer la fuente.
	 */
	private byte[] getBytes(Object source) throws IOException{
		InputStream is = null;
		if (source instanceof File) {
			File f = (File) source;
			is = new FileInputStream(f);
		} else if (source instanceof InputStream) {
			is = (InputStream) source;
		} else if (source instanceof byte[]) {
			return (byte[]) source;
		}
		ByteArrayOutputStream bout = new ByteArrayOutputStream ();
		IOUtils.copyLarge(is, bout);
		return bout.toByteArray();
	}

	/**
	 * Se obtiene un objeto para manipular la firma.
	 * @param bytes de la firma
	 * @return Instancia de un objeto para manipular la firma.
	 * @throws IOException 
	 */
	private AOSigner obtenerSigner (byte[] bytes) throws IOException {
		AOSigner signer = AOSignerFactory.getSigner(bytes);			
		return signer;
	}

	/**
	 * Comprueba si unos bytes se corresponden con los de una firma electŕonica (que no sea firma PDF).
	 * @param bytesFirma
	 * @return true si lo es, false en caso contrario.
	 * @throws IOException 
	 */
	private String isOtherSign (byte[] bytesFirma) throws IOException {
		boolean isSigned = false;
		byte[] bytesDatosFirmados = null;
		AOTreeModel arbolFirmantes = null;
		try {			
			AOSigner signer = obtenerSigner(bytesFirma);
			// La firma PDF se trata como un PDF normal, así que no lo consideraremos como documento firmado.
			if (signer != null && (!(signer instanceof AOPDFSigner))) {
				// Consideraremos que se trata de una firma si se pueden obtener los datos firmados y el árbol de firmantes.
				bytesDatosFirmados = signer.getData(bytesFirma);
				arbolFirmantes = signer.getSignersStructure(bytesFirma, true);
			}			
		} catch (AOException e) {

		}
		//TODO: Probar este código
		isSigned = (bytesDatosFirmados != null && arbolFirmantes != null && AOTreeModel.getChildCount(arbolFirmantes.getRoot()) != 0);

		if (isSigned) {
			return "otherSign";
		} else {
			return null;
		}
	}

	/**
	 * Consideraremos que el árbol DOM representa a una firma XML con el documento implícito cuando contenga
	 * el primer nodo con el documento firmado y, al mismo nivel, un nodo Signature.
	 * @param dom árbol DOM de un documento XML.
	 * @return true si representa una firma XML Detached, false en caso contrario.
	 */
	private String isDocumentXMLSignDetached (Document dom) {
		//comprobamos que tenga el nodo con el documento firmado y el nodo Signature.
		//Estos dos nodos tienen que colgar del nodo padre del documento.
		Element elementRoot = dom.getDocumentElement();	

		// Obtenemos el primer hijo del nodo Root.
		Node child = elementRoot.getFirstChild();
		boolean lastChild = false;
		boolean tieneDocFirmado = false;
		boolean tieneSignature = false;

		boolean esXMLSign = false;

		while (!lastChild && !esXMLSign) {
			// Si no hemos encontrado el nodo que contiene al documento firmado, miramos si es el actual.
			if (!tieneDocFirmado) {
				tieneDocFirmado = 	contieneIdEncoding(child);
			}
			// Si no hemos encontrado el nodo que contiene la firma, miramos si es el actual.
			if (!tieneSignature) {
				//tieneSignature = child.getNodeName().equalsIgnoreCase("ds:Signature");
				tieneSignature = child.getLocalName()!= null && child.getLocalName().equals("Signature");				
			}

			if (child == elementRoot.getLastChild()) {
				lastChild = true;
			} else {
				child = child.getNextSibling();
			}
			esXMLSign = tieneDocFirmado && tieneSignature;
		}

		if (esXMLSign) {
			return "dettached";
		} else {
			return null;
		}
	}

	/**
	 * Consideraremos que el árbol DOM representa a una firma XML Enveloped cuando contenga
	 * al menos un nodo Signature cuyo padre sea el nodo raíz
	 * @param dom árbol DOM de un documento XML.
	 * @return true si representa una firma XML Enveloped, false en caso contrario.
	 */
	private String isDocumentXMLSignEnveloped (Document dom) {
		//comprobamos que tenga el nodo con el documento firmado y el nodo Signature.
		//Estos dos nodos tienen que colgar del nodo padre del documento.
		Element elementRoot = dom.getDocumentElement();	

		boolean esXMLSignEnveloped = false;

		NodeList nodosSignature = dom.getElementsByTagName("ds:Signature");

		// Si al menos un nodo es hijo de la raíz es XADES ENVELOPED
		int i = 0;
		while (!esXMLSignEnveloped && (i < nodosSignature.getLength())) {
			Node nodo = nodosSignature.item(i);
			//En el caso de que se trate de un XML Enveloping
			if (nodo.getParentNode().getLocalName() == null){
				esXMLSignEnveloped = false;
			}else if (nodo.getParentNode().getLocalName().equals(elementRoot.getLocalName())) {
				esXMLSignEnveloped = true;
			}
			
			i++;
		}

		if (esXMLSignEnveloped) {
			return "enveloped";
		} else {
			return null;
		}
	}

	/**
	 * Obtiene los datos firmados de una firma XML implícita.
	 * @param sign bytes de la firma
	 * @return bytes de los datos firmados de una firma XML
	 * @throws Exception si no se pueden obtener los datos firmados.
	 */
	private byte[] getDataSignedFromSignXML(byte[] sign) throws Exception {
		byte[] datosFirma = null;

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {			
			throw new Exception ("No se puede parsear la firma " + e.getMessage(), e);
		}
		Document dom = null;
		try {
			dom = db.parse(new ByteArrayInputStream(sign));
		} catch (SAXException e) {
			throw new Exception ("No se puede parsear la firma " + e.getMessage(), e);			
		} catch (IOException e) {
			throw new Exception ("No se puede parsear la firma " + e.getMessage(), e);			
		}

		datosFirma = getContent(dom);		
		return datosFirma;
	}

	public String getCertificateFromSignXML (byte[] sign) throws Exception {
		String certificado = null;

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = null;
		try {
			db = dbf.newDocumentBuilder();
		} catch (ParserConfigurationException e) {			
			throw new Exception ("No se puede parsear la firma " + e.getMessage(), e);
		}
		Document dom = null;
		try {
			dom = db.parse(new ByteArrayInputStream(sign));
		} catch (SAXException e) {
			throw new Exception ("No se puede parsear la firma " + e.getMessage(), e);			
		} catch (IOException e) {
			throw new Exception ("No se puede parsear la firma " + e.getMessage(), e);			
		}

		certificado = getCertificate(dom);		
		return certificado;
	}

	/**
	 * Obtiene el contenido del nodo hijo del nodo padre del documento. Este nodo debe contener
	 * los atributos "id" y "encoding".
	 * @param dom Árbol XML del documento
	 * @return el contenido del nodo hijo del nodo padre del documento.
	 * @throws Exception si no se encuentra el nodo que cumpla estas condiciones.
	 */
	private byte[] getContent (Document dom) throws Exception{
		Element elementRoot = dom.getDocumentElement();

		Node child = elementRoot.getFirstChild();
		boolean lastChild = false;
		boolean encontrado = false;
		while (!lastChild && !encontrado) {

			encontrado = contieneIdEncoding(child);

			if (child == elementRoot.getLastChild()) {
				lastChild = true;
			} else if (!encontrado) {
				child = child.getNextSibling();
			}
		}

		String b64Content = null;
		byte[] document = null;

		if (encontrado) {
			b64Content = child.getFirstChild().getNodeValue();
			document = Base64.decodeBase64(b64Content);
		} else {
			throw new Exception ("No se encuentra el documento Implícito en la firma");
		}

		return document;

	}

	private String getCertificate (Document dom) throws Exception {
		String certificate = null;
		Element elementRoot = dom.getDocumentElement();

		NodeList list = elementRoot.getElementsByTagName("ds:X509Certificate");

		if (list == null) {
			throw new Exception ("No se ha encontrado ningún nodo ds:X509Certificate");
		} else {
			Node node = list.item(0);
			certificate = node.getFirstChild().getNodeValue();
		}
		return certificate;
	}

	private byte[] getDataSignedFromSignerGenerico (AOSigner signer, byte[] bytesFirma) throws Exception {
		byte[] bytesDatosFirmados = null;
		try  {
			if (signer == null) {
				throw new Exception ("No se puede obtener el documento Implícito en la firma");
			}

			bytesDatosFirmados = signer.getData(bytesFirma);
		} catch (AOException e) {
			throw new Exception ("No se puede obtener el documento Implícito en la firma");
		}

		return bytesDatosFirmados;
	}

	/**
	 * Obtiene el tipo de firma a partir del AOSigner
	 * @param signer AOSigner
	 * @return El tipo de firma, según el signer
	 * @throws Exception Cuando el Signer no corresponda con las firmas realizadas en Portafirmas
	 */
	private String getTipoFirmaFromSignerGenerico (AOSigner signer) throws Exception {
		String tipoFirma = null;

		if (signer instanceof AOCAdESSigner) {
			tipoFirma = Constants.SIGN_FORMAT_CADES;
		} else if (signer instanceof AOPDFSigner) {
			tipoFirma = Constants.SIGN_FORMAT_PDF;
		} else if (signer instanceof AOXAdESSigner) {
			tipoFirma = Constants.SIGN_FORMAT_XADES_IMPLICIT;
		} else if (signer instanceof AOXMLDSigSigner) {
			tipoFirma = Constants.SIGN_FORMAT_XADES_ENVELOPING;
		} else if (signer instanceof AOFacturaESigner) {
			tipoFirma = "FACTURAE";
		} else if (signer == null){
			throw new Exception ("El Signer es nulo");
		} else {
			throw new Exception ("El Signer no es conocido");
		}

		return tipoFirma;
	}
	
	/**
	 * Comprueba que un nodo XML tenga el atributo Id y el atributo Encoding.
	 * 
	 * @param nodo
	 * @return
	 */
	private boolean contieneIdEncoding (Node nodo) {

		NamedNodeMap atributos = nodo.getAttributes();

		// Comprobamos que tenga el atributo Id y el atributo Encoding:				
		int i=0;
		boolean encontrados = false;
		boolean idFound = false;
		boolean encodingFound = false;

		while (atributos != null && i < atributos.getLength() && !encontrados) {
			if (atributos.item(i).getNodeName().equalsIgnoreCase("id")){				
				idFound = true;
			} else if (atributos.item(i).getNodeName().equalsIgnoreCase("encoding")) {
				encodingFound = true;
			}

			encontrados = idFound && encodingFound;
			i++;
		}

		return encontrados;
	}
}

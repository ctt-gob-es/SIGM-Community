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
package es.accv.arangi.base.mityc;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.PrivateKey;
import java.security.cert.X509Certificate;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.log4j.Logger;
import org.apache.xml.security.utils.IgnoreAllErrorHandler;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import es.accv.arangi.base.exception.signature.CounterSignatureException;
import es.accv.arangi.base.signature.XAdESSignature;
import es.accv.arangi.base.util.Util;
import es.mityc.firmaJava.libreria.ConstantesXADES;
import es.mityc.firmaJava.libreria.excepciones.AddXadesException;
import es.mityc.firmaJava.libreria.utilidades.I18n;
import es.mityc.firmaJava.libreria.utilidades.NombreNodo;
import es.mityc.firmaJava.libreria.utilidades.UtilidadTratarNodo;
import es.mityc.firmaJava.libreria.xades.DataToSign;
import es.mityc.firmaJava.libreria.xades.FirmaXML;
import es.mityc.javasign.xml.refs.AbstractObjectToSign;
import es.mityc.javasign.xml.refs.InternObjectToSign;
import es.mityc.javasign.xml.refs.ObjectToSign;
import es.mityc.javasign.xml.refs.SignObjectToSign;


/**
 * Clase para realizar contrafirmas en firmas XML 
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José M Gutiérrez</a>
 */
public class ContraFirmaXML{

	static Logger logger = Logger.getLogger(ContraFirmaXML.class);
	
    /**
     * Contrafirma una firma según esquema XAdES. La contrafirma se realiza sobre
     * la última firma en caso de haber más de una.
     * 
     * @param certificadoFirma Certificado para realizar la contrafirma
     * @param xml XAdES
     * @param pk Clave privada con la que se realizará la firma
	 * @param digitalSignatureAlgorithm Algoritmo de firma (si nulo algoritmo por defecto)
	 * @param urlTSA URL del servidor de sello de tiempos para incluir en la contrafirma
	 * 	(puede ser nulo)
     * @param xadesSchema Esquema XAdES a utilizar
     */
    public static Document counterSign(X509Certificate certificadoFirma,
    		DataToSign xml, PrivateKey pk, String xadesSchema, 
    		String digitalSignatureAlgorithm, URL urlTSA) throws Exception {
    
    	return counterSign(certificadoFirma, xml, null, pk, digitalSignatureAlgorithm, urlTSA, xadesSchema);
    }
    
    /**
     * Contrafirma una firma según esquema XAdES. La contrafirma se realiza sobre
     * la firma cuyo certificado se pasa como parámetro en 'certificadoContraFirma'.
     * 
     * @param certificadoFirma Certificado para realizar la contrafirma
     * @param xml XAdES
     * @param certificadoContraFirma Certificado de la firma que se contrafirmará
     * @param pk Clave privada con la que se realizará la firma
	 * @param digitalSignatureAlgorithm Algoritmo de firma (si nulo algoritmo por defecto)
	 * @param urlTSA URL del servidor de sello de tiempos para incluir en la contrafirma
	 * 	(puede ser nulo)
     * @param xadesSchema Esquema XAdES a utilizar
     * @throws CounterSignatureException Errores durante la contrafirma
     */
    public static Document counterSign(X509Certificate certificadoFirma,
    		DataToSign xml, X509Certificate certificadoContraFirma, PrivateKey pk, 
    		String digitalSignatureAlgorithm, URL urlTSA, String xadesSchema) 
    				throws CounterSignatureException {

    	try {
	    	Document doc = xml.getDocument();
	    	if (doc == null) {
		        try {
		        	InputStream is = xml.getInputStream();
		        	if (is != null) {
		            	DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		            	dbf.setNamespaceAware(true);
		            	DocumentBuilder db = dbf.newDocumentBuilder();
		                db.setErrorHandler(new IgnoreAllErrorHandler());
			            InputSource isour = new InputSource(is);
			            String encoding = xml.getXMLEncoding();
			            isour.setEncoding(encoding);
			            doc = db.parse(isour);
		        	}
		        } catch (IOException ex) {
		        	throw new Exception(I18n.getResource(ConstantesXADES.LIBRERIAXADES_FIRMAXML_ERROR_50));
		        }
	    	}
	    	
	    	// Si no se indica nodo a contrafirmar se contrafirma la última firma disponible
	    	Node nodePadreNodoFirmar = null;
	    	if (certificadoContraFirma != null) {
	        	nodePadreNodoFirmar = buscarNodoAFirmar(doc, certificadoContraFirma);
	        	if(nodePadreNodoFirmar == null) {
		    		logger.info(I18n.getResource(ConstantesXADES.LIBRERIAXADES_FIRMAXML_ERROR_33));
		    		throw new AddXadesException(I18n.getResource(ConstantesXADES.LIBRERIAXADES_FIRMAXML_ERROR_51));
	        	}
	    	} 
	    	
	    	if (nodePadreNodoFirmar == null) {
	    		// Busca la última firma
	    		NodeList list = doc.getElementsByTagNameNS(ConstantesXADES.SCHEMA_DSIG, ConstantesXADES.SIGNATURE);
	    		if (list.getLength() < 1) {
	        		logger.info(I18n.getResource(ConstantesXADES.LIBRERIAXADES_FIRMAXML_ERROR_33));
	        		throw new AddXadesException(I18n.getResource(ConstantesXADES.LIBRERIAXADES_FIRMAXML_ERROR_51));
	    		} else {
	    			nodePadreNodoFirmar = list.item(list.getLength() - 1);
	    		}
	    	}
	    	String idSignatureValue = null;
	    	Element padreNodoFirmar = null;
	    	if ((nodePadreNodoFirmar != null) && (nodePadreNodoFirmar.getNodeType() == Node.ELEMENT_NODE)) {
	    		padreNodoFirmar = (Element)nodePadreNodoFirmar;
	        	ArrayList<Element> listElements = UtilidadTratarNodo.obtenerNodos(padreNodoFirmar, 2, new NombreNodo(ConstantesXADES.SCHEMA_DSIG, ConstantesXADES.SIGNATURE_VALUE));
	        	if (listElements.size() != 1) {
	        		// TODO: indicar un error específico (No se puede tener más de un nodo SignatureValue por firma XmlDSig)
	        		logger.info(I18n.getResource(ConstantesXADES.LIBRERIAXADES_FIRMAXML_ERROR_33));
	        		throw new AddXadesException(I18n.getResource(ConstantesXADES.LIBRERIAXADES_FIRMAXML_ERROR_51));
	        	}
	        	idSignatureValue = listElements.get(0).getAttribute(ConstantesXADES.ID);
	        	// TODO: Si este nodo no tiene id, identificarlo vía XPATH
	        	if (idSignatureValue == null) {
	        		// TODO: indicar un error específico (No se puede identificar nodo SignatureValue en firma XmlDSig)
	        		logger.info(I18n.getResource(ConstantesXADES.LIBRERIAXADES_FIRMAXML_ERROR_33));
	        		throw new AddXadesException(I18n.getResource(ConstantesXADES.LIBRERIAXADES_FIRMAXML_ERROR_51));
	        	}
	    	}
	
	    	// Se busca si existe el path hasta el nodo raíz CounterSignature. Si no existe, se crea.
	    	ArrayList<Element> listElements = UtilidadTratarNodo.obtenerNodos(padreNodoFirmar, 2, ConstantesXADES.QUALIFYING_PROPERTIES);
	    	if (listElements.size() != 1) {
	    		// TODO: indicar un error específico (No se puede tener más de un nodo Qualifying por firma XAdES 
	    		logger.info(I18n.getResource(ConstantesXADES.LIBRERIAXADES_FIRMAXML_ERROR_33));
	    		throw new AddXadesException(I18n.getResource(ConstantesXADES.LIBRERIAXADES_FIRMAXML_ERROR_51));
	    	}
	    	String esquemaOrigen = listElements.get(0).getNamespaceURI();
	    	NodeList nodosUnsigSigProp = (padreNodoFirmar).getElementsByTagNameNS(esquemaOrigen, 
	    			ConstantesXADES.UNSIGNED_SIGNATURE_PROPERTIES);
	    	
	    	Element nodoRaiz = null;
	    	if (nodosUnsigSigProp != null && nodosUnsigSigProp.getLength() != 0)
	    		nodoRaiz = (Element)nodosUnsigSigProp.item(0); // Se toma el primero de la lista
	    	else { // Se busca el nodo QualifyingProperties
	    		NodeList nodosQualifying = (padreNodoFirmar).getElementsByTagNameNS(esquemaOrigen, ConstantesXADES.QUALIFYING_PROPERTIES);
	        	
	        	if (nodosQualifying != null && nodosQualifying.getLength() != 0) {
	        		Element nodoQualifying = (Element)nodosQualifying.item(0);
	        		Element unsignedProperties = null;
	        		if (nodoQualifying.getPrefix() != null) {
	        			unsignedProperties =
	        				doc.createElementNS(esquemaOrigen, nodoQualifying.getPrefix() +
	        						ConstantesXADES.DOS_PUNTOS + ConstantesXADES.UNSIGNED_PROPERTIES);
	        			nodoRaiz = doc.createElementNS(esquemaOrigen, nodoQualifying.getPrefix() +
	        					ConstantesXADES.DOS_PUNTOS + ConstantesXADES.UNSIGNED_SIGNATURE_PROPERTIES);
	        		} else {
	        			unsignedProperties =
	        				doc.createElementNS(esquemaOrigen, ConstantesXADES.UNSIGNED_PROPERTIES);
	        			nodoRaiz = doc.createElementNS(esquemaOrigen, ConstantesXADES.UNSIGNED_SIGNATURE_PROPERTIES);
	        		}
	        		
	        		unsignedProperties.appendChild(nodoRaiz);
	        		nodosQualifying.item(0).appendChild(unsignedProperties);       		
	        	} else
	        		throw new AddXadesException(I18n.getResource(ConstantesXADES.LIBRERIAXADES_FIRMAXML_ERROR_52));
	    	}
	    		
	    	// Se genera un nuevo nodo Countersignature donde irá la firma
	    	Element counterSignature = null;
			if (nodoRaiz.getPrefix() != null) {
				counterSignature = doc.createElementNS(esquemaOrigen, nodoRaiz.getPrefix() +
					ConstantesXADES.DOS_PUNTOS + ConstantesXADES.COUNTER_SIGNATURE);
			} else { 
				counterSignature = doc.createElementNS(esquemaOrigen, ConstantesXADES.COUNTER_SIGNATURE);
			}
			nodoRaiz.appendChild(counterSignature);
	
	    	
	    	// Se escribe una Id única
	    	Attr counterSignatureAttrib = doc.createAttributeNS(null, ConstantesXADES.ID);
	    	String counterSignatureId = UtilidadTratarNodo.newID(doc, ConstantesXADES.COUNTER_SIGNATURE + ConstantesXADES.GUION);
	    	counterSignatureAttrib.setValue(counterSignatureId);
	    	counterSignature.getAttributes().setNamedItem(counterSignatureAttrib);
	    	
	    	// Se reemplaza el documento original por el documento preparado para contrafirma
	    	xml.setDocument(doc);
	    	
	        // Se incluye la referencia a la contrafirma
	        AbstractObjectToSign obj = null;
			if (XAdESSignature.DEFAULT_XADES_SCHEMA_URI.equals(xadesSchema)) {
				obj = new SignObjectToSign(idSignatureValue);
			} else {
				obj = new InternObjectToSign(idSignatureValue);
			}
	        xml.addObject(new ObjectToSign(obj, null, null, null, null));		
	
	
	        // Se firma el documento generado, indicando el nodo padre y el identificador del nodo a firmar
	        xml.setParentSignNode(counterSignatureId);
	        FirmaXML firma = new FirmaXML();
	        if (urlTSA != null) {
	        	firma.setTSA(urlTSA.toString());
	        }
	    	Object[] res = firma.signFile(certificadoFirma, xml, pk, XAdESUtil.getXAdESDigitalSignatureAlgorithm(digitalSignatureAlgorithm), null);
			
	
	    	doc = (Document) res[0];
	    	
	    	// Se elimina el identificador del nodo CounterSignature
	    	counterSignature = UtilidadTratarNodo.getElementById(doc, counterSignatureId);
	    	counterSignature.removeAttribute(ConstantesXADES.ID);
	    	
	    	return doc;
    	} catch (Exception e) {
    		throw new CounterSignatureException (e);
    	}
    }

    /*
     * Busca el nodo signature cuyo certificado es igual al pasado como parámetro
     */
	private static Node buscarNodoAFirmar(Document doc, X509Certificate certificadoContraFirma) {
		//-- Localizar los certificados de la firma
		XPathFactory factory = XPathFactory.newInstance();
		XPath xpath = factory.newXPath();
		NodeList certificateNodes;
		try {
			XPathExpression expr = xpath.compile("//*[local-name()='Signature']/*[local-name()='KeyInfo']/*[local-name()='X509Data']/*[local-name()='X509Certificate']");
			certificateNodes = (NodeList) expr.evaluate (doc, XPathConstants.NODESET);
		} catch (Exception e) {
			logger.info ("[ContraFirmaXML.buscarNodoAFirmar]::Error inesperado", e);
			return null;
		}

		if (certificateNodes == null || certificateNodes.getLength() == 0) {
			logger.info("[ContraFirmaXML.buscarNodoAFirmar]::Falta el elemento 'X509Certificate' de la firma, con lo que no es posible obtener la cadena de confianza de un certificado que no existe");
			return null;
		}
		
		//-- Encontrar el certificado
		for (int i = 0; i < certificateNodes.getLength(); i++) {
			try {
				X509Certificate x509Cert = Util.getCertificate(Util.decodeBase64(certificateNodes.item(i).getTextContent()));
				if (x509Cert.equals (certificadoContraFirma)) {
					return certificateNodes.item(i).getParentNode().getParentNode().getParentNode();
				}
			} catch (Exception e) {
				logger.info ("[ContraFirmaXML.buscarNodoAFirmar]::Error inesperado", e);
			}
		}
		
		return null;
	}

	
}

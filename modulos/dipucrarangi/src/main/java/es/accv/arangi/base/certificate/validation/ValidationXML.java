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
package es.accv.arangi.base.certificate.validation;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import es.accv.arangi.base.ArangiObject;
import es.accv.arangi.base.exception.certificate.ValidationXMLException;

/**
 * Clase que almacena la información obtenida del fichero de validación XML. En 
 * este fichero se almacenan, para cada Autoridad de Certificación, la URL donde
 * se pueden validar los certificados (URL a una CRL o a un OCSP).<br><br>
 * 
 * Un ejemplo de fichero de validación XML es el siguiente: <br><br>
 * 
 * &lt;validation-data version="1.1.2"&gt;<br>
 * &nbsp;&nbsp;&lt;ca common-name="CAPrueba"&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;ocsps&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;ocsp&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;url&gt;http://server/ocsp&lt;/url&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;requestor_name&gt;myname.com&lt;/requestor_name&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;signature&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;keystore_file&gt;ksfirma.p12&lt;/keystore_file&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;keystore_type&gt;PKCS12&lt;/keystore_type&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;keystore_password&gt;1234&lt;/keystore_password&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;alias&gt;FIRMA&lt;/alias&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;alias_password&gt;1234&lt;/alias_password&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;/signature&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;/ocsp&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;/ocsps&gt;<br>
 * &nbsp;&nbsp;&lt;/ca&gt;<br>
 * &nbsp;&nbsp;&lt;ca common-name="CAPrueba2"&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;/crls&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;crl&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;url&gt;http://server/crl&lt;/url&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&lt;/crl&gt;<br>
 * &nbsp;&nbsp;&nbsp;&nbsp;&lt;/crls&gt;<br>
 * &nbsp;&nbsp;&lt;/ca&gt;<br>
 * &lt;/validation-data&gt;<br>
 * 
 * @author <a href="mailto:jgutierrez@accv.es">José M Gutiérrez</a>
 *
 */
public class ValidationXML extends ArangiObject {
	
	/*
	 * Map that stores information of OCSPs
	 */
	private HashMap hmValidationOCSPs;

	/*
	 * Map that stores information of CRLs
	 */
	private HashMap hmValidationCRLs;
	
	/*
	 * File with the validationXML
	 */
	private File validationXmlFile;

	/*
	 * List of CA certificates (useful to validate OCSP response signature)
	 */
	CAList caList;
	
	/**
	 * Constructor. Durante la inicialización se obtienen objetos {@link CRL CRL} y {@link OCSPClient OCSPClient},
	 * de ahí que sea necesario iniciar este objeto con una lista de certificados de
	 * CA que permitirán validar los certificados con los que se firman estos
	 * objetos.
	 * 
	 * @param validationXml Fichero de validación
	 * @param caList Lista de certificados de CA
	 * @throws ValidationXMLException El fichero no tiene el formato correcto
	 */
	public ValidationXML (File validationXml, CAList caList) throws ValidationXMLException {
		
		this.caList = caList;
		this.validationXmlFile = validationXml;

		//-- Validate file
		if (validationXml == null || !validationXml.exists()) {
			throw new ValidationXMLException ("El fichero de validación no existe o tiene un valor nulo");
		}
		
		//-- Initialize object
		FileInputStream fis = null;
		try {
			fis = new FileInputStream (validationXml);
			initialize (fis);
		} catch (FileNotFoundException e) {
			throw new ValidationXMLException ("El fichero de validación no existe o tiene un valor nulo", e);
		} finally {
			if (fis != null) {
				try {
					fis.close();
				} catch (IOException e) {
					throw new ValidationXMLException ("No se ha podido cerrar el stream de lectura a la fichero", e);
				}
			}
		}
		
	}
	
	/**
	 * Constructor. Durante la inicialización se obtienen objetos {@link CRL CRL} y {@link OCSPClient OCSPClient},
	 * de ahí que sea necesario iniciar este objeto con una lista de certificados de
	 * CA que permitirán validar los certificados con los que se firman estos
	 * objetos.
	 * 
	 * @param is Stream de lectura al fichero de validación
	 * @param caList Lista de certificados de CA
	 * @throws ValidationXMLException El fichero no tiene el formato correcto
	 */
	public ValidationXML (InputStream is, CAList caList) throws ValidationXMLException {
		initialize (is);
		this.caList = caList;
	}
	
	/**
	 * Constructor. Durante la inicialización se obtienen objetos {@link CRL CRL} y {@link OCSPClient OCSPClient},
	 * de ahí que sea necesario iniciar este objeto con una lista de certificados de
	 * CA que permitirán validar los certificados con los que se firman estos
	 * objetos.
	 * 
	 * @param bytesValidationXml Contenido del fichero de validación
	 * @param caList Lista de certificados de CA
	 * @throws ValidationXMLException El fichero no tiene el formato correcto
	 */
	public ValidationXML(byte[] bytesValidationXml, CAList caList) throws ValidationXMLException {
		ByteArrayInputStream bais = new ByteArrayInputStream (bytesValidationXml);
		this.caList = caList;
		initialize (bais);
	}

	/**
	 * Obtiene la lista de OCSPs para la CA que se pasa como parámetro.
	 * 
	 * @param commonNameCA Common Name del certificado de la CA
	 * @return lista de OCSPs de la CA
	 */
	public List getOCSPList (String commonNameCA) {
		return (List) hmValidationOCSPs.get(commonNameCA);
	}

	/**
	 * Obtiene la lista de CRLs para la CA que se pasa como parámetro.
	 * 
	 * @param commonNameCA Common Name del certificado de la CA
	 * @return lista de CRLs de la CA
	 */
	public List getCRLList (String commonNameCA) {
		return (List) hmValidationCRLs.get(commonNameCA);
	}
	
	//-- Private methods
	
	/*
	 * Constructor
	 */
	private void initialize (InputStream is) throws ValidationXMLException {
		
		//-- Init map
		hmValidationOCSPs = new HashMap ();
		hmValidationCRLs = new HashMap ();
		
		try {
			//-- Get document in DOM format
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document doc = docBuilder.parse(is);
			
			//-- XPath
			XPathFactory factory = XPathFactory.newInstance();
			XPath xpath = factory.newXPath();
	
			//-- Get all cas
			XPathExpression expr = xpath.compile("//ca");
			NodeList caNodes = (NodeList) expr.evaluate (doc, XPathConstants.NODESET);
			if (caNodes != null) {
				//-- For every ca
				for (int i=0;i<caNodes.getLength();i++) {
					//-- Get ocsps
					List lOCSPs = new ArrayList();
					expr = xpath.compile("ocsps/ocsp");
					NodeList ocspNodes = (NodeList) expr.evaluate (caNodes.item(i), XPathConstants.NODESET);
					for (int f=0;f<ocspNodes.getLength();f++) {
						//-- Get OCSP URL
						expr = xpath.compile("url");
						Node n = (Node) expr.evaluate (ocspNodes.item(f), XPathConstants.NODE);
						String url = ((Node) expr.evaluate (ocspNodes.item(f), XPathConstants.NODE)).getTextContent();
						
						//-- Get requestor name
						String requestorName = null;
						expr = xpath.compile("requestor_name");
						if (expr.evaluate(ocspNodes.item(f), XPathConstants.NODE) != null) {
							requestorName = ((Node)expr.evaluate(ocspNodes.item(f), XPathConstants.NODE)).getTextContent();
						}
						
						//-- Get signature information
						KeystoreConfiguration signatureConfiguration = null;
						expr = xpath.compile("signature");
						if (expr.evaluate(ocspNodes.item(f), XPathConstants.NODE) != null) {
							File keystoresFolder = null;
							if (this.validationXmlFile != null) {
								keystoresFolder = new File (this.validationXmlFile.getParentFile(), "keystores");
							}
							signatureConfiguration = new KeystoreConfiguration ((Node)expr.evaluate(ocspNodes.item(f), XPathConstants.NODE), keystoresFolder);
						}
						
						//-- Load new Client OCSP
						if (signatureConfiguration == null) {
							lOCSPs.add (new OCSPClient (new URL (url), requestorName));
						} else {
							lOCSPs.add (new OCSPClient (new URL (url), requestorName, 
									signatureConfiguration.getKeystoreFile(), signatureConfiguration.getKeystoreType(), 
									signatureConfiguration.keystorePassword, signatureConfiguration.alias, 
									signatureConfiguration.aliasPassword));
						}
					}
					hmValidationOCSPs.put(caNodes.item(i).getAttributes().getNamedItem("common-name").getNodeValue(), lOCSPs);
	
					//-- Get crls
					List lCRLs = new ArrayList();
					expr = xpath.compile("//crl");
					NodeList crlNodes = (NodeList) expr.evaluate (caNodes.item(i), XPathConstants.NODESET);
					for (int f=0;f<crlNodes.getLength();f++) {
						lCRLs.add (crlNodes.item(f).getFirstChild().getNodeValue().toString());
					}
					hmValidationCRLs.put(caNodes.item(i).getAttributes().getNamedItem("common-name").getNodeValue(), lCRLs);
				}
			}
		} catch (Exception e) {
			throw new ValidationXMLException ("Error parseando el fichero de validación XML", e);
		}
	}
	
	private class KeystoreConfiguration {
		
		File keystoreFile;
		String keystoreType;
		String keystorePassword;
		String alias;
		String aliasPassword;
		 
		/**
		 * Obtiene los valores en base al nodo que los contiene
		 * 
		 * @param signatureNode Nodo
		 * @param keystoresFolder Carpeta por defecto donde buscar el fichero con el keystore
		 * @throws Exception
		 */
		public KeystoreConfiguration (Node signatureNode, File keystoresFolder) throws Exception {
			if (signatureNode != null) {
				
				//-- XPath
				XPathFactory factory = XPathFactory.newInstance();
				XPath xpath = factory.newXPath();				

				//-- Verificar que existen todos los nodos
				if (xpath.compile("keystore_file").evaluate(signatureNode, XPathConstants.NODE) == null ||
						xpath.compile("keystore_type").evaluate(signatureNode, XPathConstants.NODE) == null ||
						xpath.compile("keystore_password").evaluate(signatureNode, XPathConstants.NODE) == null ||
						xpath.compile("alias").evaluate(signatureNode, XPathConstants.NODE) == null ||
						xpath.compile("alias_password").evaluate(signatureNode, XPathConstants.NODE) == null) {
					
					throw new Exception ("Some keystore configuration parameters are not found in the validation XML " +
							"(keystore_file, keystore_type, keystore_password, alias, alias_password) ");
					
				}
				
				//-- Cargar valores
				String fileName = ((Node)xpath.compile("keystore_file").evaluate(signatureNode, XPathConstants.NODE)).getTextContent();
				keystoreType = ((Node)xpath.compile("keystore_type").evaluate(signatureNode, XPathConstants.NODE)).getTextContent();
				keystorePassword = ((Node)xpath.compile("keystore_password").evaluate(signatureNode, XPathConstants.NODE)).getTextContent();
				alias = ((Node)xpath.compile("alias").evaluate(signatureNode, XPathConstants.NODE)).getTextContent();
				aliasPassword = ((Node)xpath.compile("alias_password").evaluate(signatureNode, XPathConstants.NODE)).getTextContent();
				
				//-- Comprobar que todos los campos tienen valor
				if (fileName == null || fileName.equals("") || keystoreType == null || keystoreType.equals("") || 
						keystorePassword == null || keystorePassword.equals("") || alias == null || alias.equals("") || 
						aliasPassword == null || aliasPassword.equals("")) {
					
					throw new Exception ("Some keystore configuration parameters are void in the validation XML " +
					"(keystore_file, keystore_type, keystore_password, alias, alias_password) ");
					
				}
				
				//-- Validar que existe el fichero
				File file = new File(fileName);
				if (!file.exists() && keystoresFolder != null) {
					file = new File (keystoresFolder, fileName);
				}
				this.keystoreFile = file;
			}

		}
		
		//-- GET/SET
		public File getKeystoreFile() {
			return keystoreFile;
		}
		public void setKeystoreFile(File keystoreFile) {
			this.keystoreFile = keystoreFile;
		}
		public String getKeystoreType() {
			return keystoreType;
		}
		public void setKeystoreType(String keystoreType) {
			this.keystoreType = keystoreType;
		}
		public String getKeystorePassword() {
			return keystorePassword;
		}
		public void setKeystorePassword(String keystorePassword) {
			this.keystorePassword = keystorePassword;
		}
		public String getAlias() {
			return alias;
		}
		public void setAlias(String alias) {
			this.alias = alias;
		}
		public String getAliasPassword() {
			return aliasPassword;
		}
		public void setAliasPassword(String aliasPassword) {
			this.aliasPassword = aliasPassword;
		}
		
	}
	
}

package es.dipucr.integracion.general;

import java.awt.Component;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;
import java.lang.reflect.Method;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;

import javax.swing.ImageIcon;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Source;
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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import sun.misc.BASE64Decoder;
import es.dipucr.integracion.Cliente;
import es.dipucr.integracion.Config;


/**
 * Clase de utilidad donde meter los métodos comunes que pueden ser usados desde varias clases.
 *
 */
public class Util {
	
	private static final Log LOGGER = LogFactory.getLog(Util.class);

	
	/**
	 * Metodo de ejemplo.
	 */
	public static List<Class> getClasses(String pckgname) 	throws ClassNotFoundException {
		ArrayList<Class> classes = new ArrayList<Class>();
		// Get a File object for the package
		File directory = null;
		try {
			ClassLoader cld = Thread.currentThread().getContextClassLoader();
			if (cld == null) {
				throw new ClassNotFoundException("Can't get class loader.");
			}
			//String path = '/' + pckgname.replace('.', '/');
			String path = pckgname.replace('.', '/');
			URL resource = cld.getResource(path);
			if (resource == null) {
				throw new ClassNotFoundException("No resource for " + path);
			}
			directory = new File(resource.getFile());
		} catch (NullPointerException x) {
			throw new ClassNotFoundException(pckgname + " (" + directory
					+ ") does not appear to be a valid package");
		}
		if (directory.exists()) {
			// Get the list of the files contained in the package
			String[] files = directory.list();
			for (int i = 0; i < files.length; i++) {
				// we are only interested in .class files
				if (files[i].endsWith(".class")) {
					// removes the .class extension
					classes.add(Class.forName(pckgname + '.'
							+ files[i].substring(0, files[i].length() - 6)));
				}
			}
		} else {
			throw new ClassNotFoundException(pckgname
					+ " does not appear to be a valid package");
		}
		/*
		Class[] classesA = new Class[classes.size()];
		classes.toArray(classesA);
		return classesA;
		*/
		return classes;
		}
			
	
	/**
	 * @param pckgname Ruta del paquete donde empezará a buscar el resto de paquetes. 
	 * @return Lista con los nombres de paquetes incluidos en la ruta dada por el parámetro
	 * de entrada pckgname.
	 * @throws Exception
	 */
	public static List<String> getPackageList(String pckgname) throws Exception {
		List<String> list = new ArrayList<String>();
		File directory = null;
		try {
			ClassLoader cld = Thread.currentThread().getContextClassLoader();
			if (cld == null) {
				throw new ClassNotFoundException("Can't get class loader.");
			}

			String path = pckgname.replace('.', '/');
			URL resource = cld.getResource(path);
			if (resource == null) {
				LOGGER.error("NO SE HA ENCONTRADO EL RECURSO NECESARIO.");
				throw new ClassNotFoundException("No resource for " + path);
			}
			directory = new File(resource.getFile());
			
		} catch (NullPointerException x) {
			throw new ClassNotFoundException(pckgname + " (" + directory+ ") does not appear to be a valid package");
			}
		
		if (directory.exists()){
			// Get the list of the files contained in the package
			String[] files = directory.list();
			for (int i = 0; i < files.length; i++) {
				if (!files[i].endsWith(".class"))  {		//Solo nos interesan los packages
					LOGGER.debug("++++++ ADDING PACKAGE: "+files[i]);
					list.add(pckgname+"."+files[i]);
					}
			}
		} else {
			throw new ClassNotFoundException(pckgname+ " does not appear to be a valid package");
		}

		return list;

	}

	
	/**
	 * @param pckgname Ruta del paquete donde se buscarán las clases.
	 * @return Lista que contiene las clases pertenecientes al paquete.
	 * @throws Exception
	  */
	public static List<Class<?>> getAllServicesInPackage(String pckgname) throws Exception {
		LOGGER.debug("READING ALL SERVICES IN PACKAGE : "+pckgname);		
		List<Class<?>> list = new ArrayList<Class<?>>();
		File directory = null;
		
		try {
			ClassLoader cld = Thread.currentThread().getContextClassLoader();
			if (cld == null) {
				throw new ClassNotFoundException("Can't get class loader.");
			}

			String path = pckgname.replace('.', '/');
			//log.debug("Path transformado a : "+path);					
			URL resource = cld.getResource(path);
			if (resource == null) {
				throw new ClassNotFoundException("No resource for " + path);
			}
			directory = new File(resource.getFile());
			
		} catch (NullPointerException x) {
			throw new ClassNotFoundException(pckgname + " (" + directory+ ") does not appear to be a valid package");
		}

		if (directory.exists()){
			// Get the list of the files contained in the package
			String[] files = directory.list();
			LOGGER.debug("Nº de ficheros que contiene el package "+directory+" es: "+files.length);
			for (int i = 0; i < files.length; i++) {
				// we are only interested in .class files
				if (files[i].endsWith(".class"))  {
					// removes the .class extension
					String clase = pckgname + '.' + files[i].substring(0, files[i].length() - 6);
					LOGGER.debug("añadiendo clase: "+clase);
					list.add(Class.forName(clase));
					
				} else if (!files[i].endsWith(".class")) {
					LOGGER.debug("Existe algo llamado: "+files[i]+". No se procesa");
				}
			}
		} else {
			throw new ClassNotFoundException(pckgname+ " does not appear to be a valid package");
		}
		
		return list;
	}

	/**
	 * @param ruta Ruta del directorio a buscar.
	 * @return Lista de ficheros que se encuentran en ese directorio.
	 * @throws Exception
	 */
	public static List<String> getFileList(String ruta) throws Exception {
		List<String> list = new ArrayList<String>();
		File directory = new File(ruta);
		if (directory.exists()){
			// Get the list of the files contained in the package
			String[] files = directory.list();
			for (int i = 0; i < files.length; i++) {
				list.add(files[i]);
				}
		} else {
			throw new ClassNotFoundException(ruta+ " does not appear to be a valid path");
		}
		
		return list;
	}

	/**
	 * @param textArea Componente de texto
	 * @param filePath fichero de texto que contiene el texto a añadir en el componente.
	 * @return Componente que incluye el texto correspondiente al fichero.
	 * @throws Exception
	 */
	public static JTextArea appendText(JTextArea textArea, String filePath) throws Exception {
		BufferedReader input = new BufferedReader(new FileReader(filePath));
		
		try {
			String line = null; //not declared within while loop
			
			while (( line = input.readLine()) != null){
				String UTF8Str = new String(line.getBytes(),System.getProperty("file.encoding"));		//"Cp1252" o "ISO-8859-1"
				textArea.append(UTF8Str+"\n");
			}
		} finally {
			input.close();
		}	
		
		return textArea;
	}
	
	/**
	 * @param pckgname Nombre del package donde buscará las clases.
	 * @param tipoServicio Nombre de la Interface que sirve para distinguir.
	 * @return Lista con las clases pertenecientes a un package que implementan una determinada interface.
	 * @throws Exception
	 */
	public static List<Class<?>> getListaServicios(String pckgname, Class<?> tipoServicio) throws Exception {
		List<Class<?>> lista = new ArrayList<Class<?>>();		
		List<Class<?>> listaTodosServicios = getAllServicesInPackage(pckgname);
		for (Class<?> cl : listaTodosServicios) {
			Class<?>[] interfaces = cl.getInterfaces();
			for (int i=0; i<interfaces.length; i++) {
				if (interfaces[i]==tipoServicio) {
					lista.add(cl);
				}
			}
		}
		
		return lista;
	}
	
	/**
	 * Clase de ejemplo.
	 */
	public static String getNameService(Class<?> cl) throws Exception {
		Object iClass = cl.newInstance();
		Class<?> superClass = cl.getSuperclass();
	    Method method = superClass.getMethod("getServiceName", null);
	    String result = (String) method.invoke(iClass);
	    return result;
	}
	
	
	
	/**
	 * Se crea una instancia de una determinada clase llamándose a su constructor predeterminado.
	 * De esta manera se inicializan las propiedades que luego mediante la invocación del
	 * método getServiceInformation se recuperarán. Es decir, es un ejemplo de como conseguir
	 * propiedades de una instancia de tipo Class.
	 * @param cl Clase del servicio del que queremos recuperar información.
	 * @return Array con objetos que normalmente son propiedades de un determinado servicio.
	 * @throws Exception
	 * @see com.mpr.wizard.librerias.Scsp#getServiceInformation();
	 */
	public static Object[] getServiceInformation(Class<?> cl) throws Exception {
		Object iClass = cl.newInstance();
		Class<?> superClass = cl.getSuperclass();
	    Method method = superClass.getMethod("getServiceInformation", null);
	    Object[] result = (Object[]) method.invoke(iClass);
	    return result;
	}
	
	public static void convertirFichero(String ficheroEntrada, String ficheroSalida) {
		
		FileInputStream fis = null;
		FileOutputStream fos = null;
		BASE64Decoder b64 = null;

	    try {
	    	fis = new FileInputStream(new File(ficheroEntrada));
	    	fos = new FileOutputStream(new File(ficheroSalida));
	    	b64 = new BASE64Decoder();
	    	fos.write(b64.decodeBuffer(fis));
			System.out.println("Correcto, fichero generado en: " + ficheroSalida);
			
		} catch (FileNotFoundException e1) {
			LOGGER.error("Error. " + e1.getMessage(), e1);
			
		} catch (IOException e1) {
			LOGGER.error("Error. " + e1.getMessage(), e1);
		
		} finally {
			try {
				if (fis != null) {
					fis.close();
				}
				
				if (fos != null) {
					fos.close();
				}
			} catch (IOException e) {
				LOGGER.error("Error. " + e.getMessage(), e);
			}
		}
	}	
	 
	 
	 public static void deleteFile(String file) throws IOException, Exception {
		File temp = new File(file);
		temp.delete();	
		//TODO: chequear cuando falla
	 }
	 
	 /**
	 * @param doc Documento XML
	 * @param file Nombre del fichero donde se guardará la representación del documento XML. 
	 * @throws Exception
	 */
	public static void writeDocumentToFile(Document doc, String idPeticion, boolean plain) throws Exception {
		// Creating new transformer factory
        javax.xml.transform.TransformerFactory factory = javax.xml.transform.TransformerFactory.newInstance();
 
        // Creating new transformer
        javax.xml.transform.Transformer transformer = factory.newTransformer();
 
        // Creating DomSource with Document you need to write to file
        javax.xml.transform.dom.DOMSource domSource = new javax.xml.transform.dom.DOMSource(doc);
 
        // Creating Output Stream to write XML Content
        java.io.ByteArrayOutputStream bao = new java.io.ByteArrayOutputStream();
 
        // Transforming domSource and getting out put in output stream
        transformer.transform(domSource,new javax.xml.transform.stream.StreamResult(bao));
 
        // writing output stream data to file
        String fileName = (plain ? idPeticion+"-output-plain": idPeticion+"-output") + ".xml";
        java.io.FileOutputStream fos = new java.io.FileOutputStream(Config.getFilesDir()+File.separator+fileName);
        fos.write(bao.toByteArray());
        fos.flush();
        fos.close();
	 }
	 
	/**
	 * @param resourcePath Recurso de la imagen.
	 * @return Icono que representa la imagen.
	 * @throws Exception
	 */
	public static ImageIcon readImage(String resourcePath) throws Exception {
		ImageIcon icon = null;
		String so = System.getProperty("os.name");
		try{
			if (so.startsWith("Windows")) {
				String windowsPath = resourcePath.replace(""+File.separator+"","/");
				icon = new ImageIcon(Util.class.getClassLoader().getResource(windowsPath));
				
			} else {
				icon = new ImageIcon(Util.class.getClassLoader().getResource(resourcePath));
			}
			
		} catch(NullPointerException npe) {
			LOGGER.debug("System Operative: "+so);
			LOGGER.warn("NullPointerException reading the resource :"+resourcePath);
			
			try{
				icon = new ImageIcon(resourcePath);
				if (icon.getIconWidth()==-1){
					throw new Exception("Imposible to read the image: "+resourcePath);
				}
			} catch(Exception e) {
				LOGGER.error("Error. " + e.getMessage(), e);
				throw new Exception(e);
			}
			
			LOGGER.warn("Eventually resource: "+resourcePath+" readed sucessfully.");
		}
		
		return icon; 
	}
		
	/**
	 * @param resourcePath Recurso del fichero.
	 * @return Documento XML que contiene los datos leidos del fichero.
	 * @throws Exception
	 */
	public static Document readFile(String resourcePath) throws Exception {
		Document doc = null;
		java.io.InputStream is = null;
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder(); 
		String so = System.getProperty("os.name");
		LOGGER.debug("System Operative: "+so);
		
		try{
			if (so.startsWith("Windows")) {
				String windowsPath = resourcePath.replace(""+File.separator+"","/");
				is = Util.class.getClassLoader().getResourceAsStream(windowsPath);
				doc = db.parse(is);
			} else {
				is = Util.class.getClassLoader().getResourceAsStream(resourcePath);
				doc = db.parse(is);
			}
			
		} catch(java.lang.IllegalArgumentException iae) {
			LOGGER.warn("IllegalArgumentException throwed reading the file :"+resourcePath);
			File f = new File(resourcePath);
			doc = db.parse(f);
			LOGGER.warn("Eventually file: "+resourcePath+" readed sucessfully.");
		}

		return doc; 
	}
		
	/**
	 * @param doc
	 * @param mainNodeName Nodo a buscar
	 * @return
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws Exception
	 */
	public static List<Node> getNodeList(Element element, String mainNodeName) throws ParserConfigurationException, SAXException, IOException, Exception {
		List<Node> l = new ArrayList<Node>();
		NodeList nodeList = element.getElementsByTagName(mainNodeName);
		
		for (int i=0; i<nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			l.add(node);
		}
		
		return l;
	}


		
		
	/**
	 * @param node Nodo por el que se buscará el elemento.
	 * @param name Nombre del elemento que queremos buscar.
	 * @return Nodo que corresponde con el nombre.
	 * @throws Exception
	 */
	public static Node findNode(Node node, String name) throws Exception {

		if (!node.hasChildNodes()) return null;
		NodeList childList = node.getChildNodes();
		
		for (int j=0;j<childList.getLength();j++) {
			Node child = childList.item(j);
			//System.out.println("@@@@@  "+child.getNodeName());
			if (child.getNodeType()==Node.ELEMENT_NODE) {
				if (child.getNodeName().equals(name)){
					return child;
				}
			}
		}
		
		return null;

	}
		
	/**
	 * @param mainNodeName Nodo a buscar.
	 * @param resourcePath Recurso correspondiente a un documento XML donde se buscará el nodo.
	 * @return Lista con los nodos que coincidan con mainNodeName.
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 * @throws Exception
	 */
	public static NodeList getServicesList(String mainNodeName, String resourcePath) throws ParserConfigurationException, SAXException, IOException, Exception {

		Document doc = readFile(resourcePath);
		NodeList nodeList = doc.getElementsByTagName(mainNodeName);
		
		return nodeList;
	}
		
	/**
	 * Desactiva todos los elementos del Panel.
	 * @param panel
	 * @throws Exception
	 */
	public static void disablePanel(JPanel panel) throws Exception {
		Component[] components = panel.getComponents();
		
		for (Component c : components) {
			c.setEnabled(false);
		}
	}

	/**
	 * @param elemento
	 * @param nodeName
	 * @return
	 * @throws Exception
	 * OJO porque parece que solo funciona bien cuando se buscan nodos del 1º nivel, 
	 * si se buscan nodos hijos o nietos parece ser que no funciona.
	 */
	public static String getTextNode(Element elemento, String nodeName) throws Exception {
		NodeList listaHijos = elemento.getElementsByTagName(nodeName);
		
		if (listaHijos.getLength()==0){
			return null;
		}
		
		Node node = listaHijos.item(0).getChildNodes().item(0);
		
		if (node==null){
			return null;
		} else {
			return node.getNodeValue();
		}
	}
		
	/**
	 * @param elemento
	 * @param nodeName
	 * @return
	 * @throws Exception
	 */
	public static Node getNode(Element elemento, String nodeName) throws Exception {
		NodeList listaHijos = elemento.getElementsByTagName(nodeName);
		
		for (int j=0;j<listaHijos.getLength();j++) {
			Node child = listaHijos.item(j);
			
			if (child.getNodeName().equals(nodeName)){
				return child;
			}
		}
		
		return null;
	}
		
	/**
	 * @param node
	 * @return
	 */
	public static String nodeToString(Node node) {
		StringWriter sw = new StringWriter();
		
		try {
			Transformer t = TransformerFactory.newInstance().newTransformer();
			t.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
			t.transform(new DOMSource(node), new StreamResult(sw));
			
		} catch (TransformerException te) {
			System.out.println("nodeToString Transformer Exception");
		}
		
		return sw.toString();
	}
		
	/**
	 * @param element
	 * @param strExp
	 * @return
	 * @throws XPathExpressionException
	 */
	public static String evaluateString(Element element, String strExp) throws XPathExpressionException {
		XPath xpath = XPathFactory.newInstance().newXPath();
		XPathExpression exp = xpath.compile(strExp);
		String result = (String) exp.evaluate(element, XPathConstants.STRING);
		
		if(result == null || "".equals(result)) {
			LOGGER.warn("No se ha podido evaluar el valor de la expresion [" + strExp + "]");
		}
		
		return result;
	}
		
	/**
	 * @param nodo
	 * @param element
	 * @return
	 * @throws Exception
	 */
	public static List<Element> evaluateXPathAsList(String nodo, Element element) throws Exception {
		XPathExpression exp = null;
		
		try {
			XPath xpath = XPathFactory.newInstance().newXPath();
			exp = xpath.compile("//*[local-name()='" + nodo + "']");
			Object o = exp.evaluate(element, XPathConstants.NODESET);
			NodeList nodes = (NodeList) o;
			List<Element> result = new ArrayList<Element>();
			for (int i = 0; i < nodes.getLength(); i++) {
				result.add((Element) nodes.item(i));
			}
			return result;
		} catch (Exception e) {
			String msg = "Se produjo un error al evaluar la expresion de peticion '" + exp + "'.";
			throw new Exception(msg, e);
		}
	}
		
	/**
	 * @param element
	 * @throws Exception
	 * Método util para la fase de pruebas. 
	 * Una vez terminadas la fase de prueba se deberían quitar todas las referencias a este método.
	 * 
	 */
	public static void writeOutput(Element element) throws Exception {
	
		DOMSource source = new DOMSource(element);
		StreamResult result = new StreamResult(System.out);
		TransformerFactory transformerFactory = TransformerFactory.newInstance();
		Transformer transformer = transformerFactory.newTransformer();
		System.out.println("#############################################################################\n");
		transformer.transform(source, result); 
		System.out.println("\n#############################################################################\n");
	}
		
	public static Element parseToElement(String xml) throws Exception {
		InputSource source = new InputSource(new StringReader(xml));

		DocumentBuilderFactory b = DocumentBuilderFactory.newInstance();
		b.setNamespaceAware(false);
		org.w3c.dom.Document doc = null;
		javax.xml.parsers.DocumentBuilder db = null;

		db = b.newDocumentBuilder();
		doc = db.parse(source);
		
		return doc.getDocumentElement();
	}

	public static Document parseToDocument(String xml) throws Exception {
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		factory.setNamespaceAware(true);
		DocumentBuilder builder = factory.newDocumentBuilder();
		
		return builder.parse(new ByteArrayInputStream(xml.getBytes("UTF-8")));
	}
		
	public static void showTree(Node node) throws Exception {

		if (! node.hasChildNodes()) System.out.println(" Node : "+node.getNodeName()+" sin hijos");
		NodeList childList = node.getChildNodes();
		
		for (int j=0;j<childList.getLength();j++) {
			Node child = childList.item(j);
			System.out.println(" "+child.getNodeName());
		}
	}

	public static String xmlToString(Node node) {
		
        try {
            Source source = new DOMSource(node);
            StringWriter stringWriter = new StringWriter();
            Result result = new StreamResult(stringWriter);
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer();
            transformer.transform(source, result);
            
            return stringWriter.getBuffer().toString();
            
        } catch (TransformerConfigurationException e) {
        	LOGGER.error("Error. " + e.getMessage(), e);
        	
        } catch (TransformerException e) {
        	LOGGER.error("Error. " + e.getMessage(), e);
        }
        
        return null;
    }
		
	/**
	 * @param pathResource Ruta donde se encuentra el fichero de propiedades.
	 * @return Conjunto de propiedades inicializadas a partir del fichero indicado por el parámetro.
	 * @throws Exception
	 */
	public static Properties readFileProperties(String pathResource) throws Exception {
		Properties properties = new Properties();
		
		try {
			FileInputStream fis = new FileInputStream(pathResource);
		    properties.load((InputStream) fis);
		    
		} catch (Exception e) {
			LOGGER.error("Error. " + e.getMessage(), e);
			throw new Exception("THERE IS NO PROPERTIES FILE: "+pathResource);
		}

		return properties;
	}

	/**
	 * @param properties Conjunto de propiedades.
	 * @param pathResources Fichero de propiedades.
	 * @throws Exception
	 * Almacena en el fichero el conjunto de propiedades.
	 */
	public static void writeFileProperties(Properties properties, String pathResources) throws Exception {
		FileOutputStream fos = new FileOutputStream(pathResources);
		properties.store(fos, null);
		
	}
		
	/**
	 * @param properties
	 * @throws Exception
	 * Establece algunas de las propiedades del sistema actual.
	 */
	public static void setSslConfiguracion(Properties properties) throws Exception {
		Properties systemProps = System.getProperties();
		
     	if (properties.getProperty("truststore.file").length()!=0) {
      		systemProps.put("javax.net.ssl.trustStore",properties.getProperty("truststore.file"));
      	}
  		if (properties.getProperty("keystore.file").length()!=0) {
  			systemProps.put("javax.net.ssl.keyStore",properties.getProperty("keystore.file"));
 		}
  		if (properties.getProperty("keystore.type").length()!=0) {
  			systemProps.put("javax.net.ssl.keyStoreType",properties.getProperty("keystore.type"));
		}
  		if (properties.getProperty("keystore.password").length()!=0) {
  			systemProps.put("javax.net.ssl.keyStorePassword",properties.getProperty("keystore.password"));
  		}
	}
		
	public static boolean checkFile(String resourcePath) throws Exception {
		boolean resultado = false;
		java.io.InputStream is = null;

		try{
			String windowsPath = resourcePath.replace("" + File.separator + "", "/");
			is = Util.class.getClassLoader().getResourceAsStream(windowsPath);
			
			if (null == is) {		//Intentamos con Linux
				is = Util.class.getClassLoader().getResourceAsStream(resourcePath);
			}
			
			if (null != is){
				resultado = true;
			} else {
				resultado = false;
			}
			
		} catch(java.lang.IllegalArgumentException iae) {
			System.out.println("IllegalArgumentException throwed reading the file :"+resourcePath);
			File f = new File(resourcePath);
			
			if (null != f){
				resultado =  true;
			}
		}
		
		return resultado; 
	}

	/**
	 * @param listClasses Lista de clases correspondientes a los servicios.
	 * @return una lista ordenada según la condición definida en el método compareTo de la clase Scsp.
	 * Al usar un TreeMap primero aparecerán los servicios V2 y luego los V3.
	 * @throws Exception
	 */
	public static List<Cliente> orderServices(List<Class<?>> listClasses) throws Exception {
		Map<String,ArrayList<Cliente>> map = new TreeMap<String,ArrayList<Cliente>>();
		List<Cliente> lista = new ArrayList<Cliente>();	
		
		for (Class<?> cl : listClasses) {
			Cliente cliente = (Cliente) cl.newInstance();
			//System.out.println(">>>>>>Version: "+scsp.getScspVersion()+" >>>> Order:  "+scsp.getOrder());
			String clave = "V"+cliente.getScspVersion();
			ArrayList<Cliente> auxList = null;
			
			if (!map.containsKey(clave)) {
				auxList = new ArrayList<Cliente>();
			} else {
				auxList = map.get(clave);
			}
			
			auxList.add(cliente);
			map.put(clave,auxList);
		}

		Iterator<Map.Entry<String,ArrayList<Cliente>>> iter = map.entrySet().iterator();
		
		while (iter.hasNext()) {
			Map.Entry<String,ArrayList<Cliente>> entry = iter.next();
		    ArrayList<Cliente> auxList = entry.getValue();
		    Collections.sort(auxList);
		    lista.addAll(auxList);
		}
		
		return lista;
	}
		
	/**
	 * @param obj Objeto de tipo org.apache.xerces.dom.ElementNSImpl que corresponde con el nodo <DatosEspecificos> de la Respuesta.
	 * @param nameNodeMain
	 * @param prefix
	 * @param nameSpace
	 * @return
	 * @throws Exception
	 * No lo entiendo pero no consigo buscar correctamente por xPath usando el objeto DatosEspecificos que se obtiene en la respuesta.
	 * Probé con éxito hace tiempo buscar sobre todo el documento org.w3c.dom.Document pero no funciona para el objeto DatosEspecificos,
	 * que pertenece a la clase org.apache.xerces.dom.ElementNSImpl.
	 * 
	 * Este método copia todos los nodos existentes en un nuevo elemento que será con el que sí funcione la búsqueda mediante xPath.
	 * 
	 * Hay una pequeña pega y es que por ejemplo para Catastro está definido el prefijo "resp" pero no pasaría nada ya que 
	 * un ejemplo de procesar el nodo <DatosEspecificos> sería una salida como la siguiente que sigue funcionando:
	 * <DatosEspecificos xmlns="http://www.map.es/scsp/esquemas/datosespecificos" xmlns:resp="http://www.map.es/scsp/esquemas/datosespecificos">
	 */
	public static org.w3c.dom.Element copyAllNodes(Object obj, String nameNodeMain, String prefix, String nameSpace) throws Exception {
		
		org.apache.xerces.dom.ElementNSImpl xercesObj = (org.apache.xerces.dom.ElementNSImpl) obj;
		Node nodeAux = (Node) xercesObj.cloneNode(true);
		
		DocumentBuilderFactory fabrica = DocumentBuilderFactory.newInstance();
		fabrica.setNamespaceAware(true);		
		DocumentBuilder generador = fabrica.newDocumentBuilder();
		Document doc = generador.newDocument();
		
		Element root = doc.createElementNS(nameSpace,nameNodeMain);
		String pref =(prefix==null ? "xmlns" :"xmlns:"+prefix);
		root.setAttribute(pref,nameSpace);
		doc.appendChild(root);
		
		//Este será el elemento que devolvamos finalmente 
        Element element = doc.getDocumentElement();
        
		NodeList list = nodeAux.getChildNodes();
		for (int i=0; i < list.getLength(); i++) {
			Node subnode = list.item(i);
		    if (subnode.getNodeType() == Node.ELEMENT_NODE) {
		    	Node aux = doc.importNode(subnode,true);
		    	element.appendChild(aux);
		    }
		}

		return element;
	}

	 public static Calendar parseTimestampCoating(String timestamp) throws Exception {
	   //Ejemplo: 2011-09-01T11:43:17.234+02:00
	   //El +02:00 corresponde al Timezone pero se lo voy a quitar ya que sino no me parsea bien.
	   int pos = timestamp.indexOf("+");
	   String aux = timestamp.substring(0,pos);
	   SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
	   Date d = sdf.parse(aux);
	   Calendar cal = Calendar.getInstance();
	   cal.setTime(d);
	   
	   return cal;
	 }
		 
	public static List<String> readTextFile(String path, String fileName) throws Exception {
		List<String> list = new ArrayList<String>();
		File file = new File(path+File.separator+fileName);
		BufferedReader reader = null;
		 
		try {
			reader = new BufferedReader(new FileReader(file));
			String text = null;
			while ((text = reader.readLine()) != null) {
				System.out.println("LEYENDO DEL FICHERO DE LOTES: "+text);
				list.add(text);
			}
		
		} catch (FileNotFoundException e) {
			LOGGER.error("Fichero de entrada no encontrado. " + e.getMessage(), e);
			throw new FileNotFoundException("Fichero entrada no encontrado");
		} catch (IOException e) {
			LOGGER.error("Error. " + e.getMessage(), e);
			throw new Exception(e);
		} finally {
			try {
				if (reader != null) {
					reader.close();
				}
			}catch (IOException ioe) {
				LOGGER.error("Error. " + ioe.getMessage(), ioe);
				throw new Exception(ioe);
			}
		}
		return list;
	}

	public static BufferedWriter createTextFile(String path, String fileName) throws Exception {
		
		BufferedWriter out = null;
		try{
		    out = new BufferedWriter(new FileWriter(path+File.separator+fileName));
			
		} catch(Exception e) {
			LOGGER.error("Error. " + e.getMessage(), e);
			throw new Exception(e);
		}
	
		return out;
	}
		
	public static BufferedWriter writeTextFile(BufferedWriter out, String line) throws Exception {
		try{
			out.write(line+"\n");
			
		} catch(Exception e) {
			LOGGER.error("Error. " + e.getMessage(), e);
			out.write(line+"######EXCEPCION: "+e.getMessage()+"\n");
			throw new Exception(e);
		}
		
		return out;
	}
}
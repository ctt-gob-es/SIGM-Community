package es.dipucr.integracion;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathFactory;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.util.XMLUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import es.dipucr.integracion.beans.ResultAtributosBean;
import es.dipucr.integracion.beans.ResultBean;
import es.dipucr.integracion.beans.ResultEstadoBean;
import es.dipucr.integracion.general.PersonalNamespaceContext;
import es.dipucr.integracion.general.Util;
import es.dipucr.integracion.interfaces.ManejadorServicios;




/**
 * Clase abstracta principal que será usada como base para el manejo de los servicios. 
 * Se situan aquí los métodos que serán usados por el cliente del Recubrimiento, los métodos
 * propios para el uso de las librerías SCSP se encuentran en una clase hija llamada ClienteScsp.java.
 * También existen dos clases descendientes (ScspV2 y ScspV3) que se ocupan de tareas específicas 
 * para esas versiones como por ejemplo los nombres de espacios.
 */
public abstract class Cliente implements ManejadorServicios, Comparable<Cliente> {
	
	private static final Log log = LogFactory.getLog(Cliente.class);


	private final String XMLNS_ENVELOPE_SOAP_1_1 			= "http://schemas.xmlsoap.org/soap/envelope/";
	private final String XMLNS_PETICION_COATING 			= "http://intermediacion.redsara.es/scsp/esquemas/ws/peticion";
	private final String XMLNS_SOLICITUD_RESPUESTA_COATING	= "http://intermediacion.redsara.es/scsp/esquemas/ws/solicitudRespuesta";
	
	
	/**
	 * Donde se encuentran los datos de la petición
	 */
	protected Properties dataStore = null;
	
	/**
	 * Ruta donde se encuentran los ficheros de datos de entrada para las peticiones
	 */
	private String servicePath = null;

	
	
	private String timeStamp = null;					//Ejemplo: "2010-03-12T17:59:46.699+01:00";		
	
	/**
	 * Almacena el identificador de la petición
	 */
	private String idPeticionCliente = null;
	

	/**
	 * Indica la versión del servicio implementado.
	 */
	private int scspVersion = 0;

	
	/**
	 * Cadena de texto que indica el emisor.
	 */
	private String emisor = null;
	
	/**
	 * Cadena de texto que indica el CIF del emisor.
	 */
	private String cifEmisor = null;

	/**
	 * Cadena de texto que indica el código de certificado.
	 */
	private String certificateCode = null;
	
	/**
	 * Para monopeticiones será 1, en multipeticiones será > 1 (en estos casos los datos se leerán de varios ficheros .properties de cada servicio).
	 */
	private Integer numElementos = 0;

	
	
	/**
	 * Sirve para ordenar los servicios en pantalla según este valor. 
	 * Así por ejemplo se puede poner que primero se muestren los servicios V2 antes que los V3.
	 */
	protected int order = 0;
	
	
	/**
	 * Cadena de texto que se mostrará en pantalla para un servicio.
	 */
	private String serviceName = null;
	
	/**
	 * Cadena de texto que muestra una descripción del servicio (se usa como tooltip).
	 */
	private String descriptionServiceName = null;
	
	
	
	/**
	 * Contiene el tag empleado en el nodo principal de las respuestas del servicio 
	 */
	private String prefixRoot = null;
	
	
	/**
	 * Contiene el tag empleado en el nodo correspondiente de la Respuesta. 
	 * Este valor puede variar dependiendo del servicio, por ejemplo:
	 * 
	 * Servicio AEAT101: 		<m:Respuesta xmlns:m="http://www.map.es/scsp/esquemas/V2/respuesta">
	 * Servicio SVDTUWS01: 		<Respuesta xmlns="http://www.map.es/scsp/esquemas/V2/respuesta">
	 * Servicio SVDCDATWS01:  	<resp:Respuesta xmlns:resp="http://www.map.es/scsp/esquemas/V2/respuesta">
	 * 
	 * Para estos ejemplos anteriores se devolverá: 
	 * 
	 * Servicio AEAT101: m 
	 * Servicio SVDTUWS01: [el correspondiente al nodo padre] 
	 * Servicio SVDCDATWS01: resp
	 */
	private String prefixResponse = null;
	
	
	/**
	 * Contiene el tag empleado en el nodo correspondiente de DatosEspecificos. 
	 */
	private String prefixDatosEspecificos = null;
	
	
	/**
	 * Contiene el tag empleado en el nodo correspondiente de ConfirmacionPeticion. 
	 */
	private String prefixConfirmationRequest = null;
	

	
	//===================================================================================
	// 						M E T O D O S      A B S T R A C T O S 
	//===================================================================================
	abstract public String getNameSpaceResponse();
	abstract public String getNameSpaceDatosEspecificos();
	abstract public String getNameSpaceConfirmationRequest();

	abstract public Element createDatosEspecificos() throws Exception;
	abstract public String getDatosEspecificosOutput(Element datosEspecificos) throws Exception;
	//===================================================================================
	//===================================================================================
	
	
	
	public Cliente() { }
	
	
	/**
	 * @param path Ruta completa donde se encuentra el fichero que contiene los datos necesarios
	 * para crear la petición.
	 * @throws Exception
	 */
	public Cliente(String path) throws Exception {
		this.dataStore = new Properties();
		FileInputStream in = new FileInputStream(path);
		dataStore.load(in);
		in.close();
		
		//Indica la ruta donde se pueden obtener los datos de distintas peticiones
		int pos = path.lastIndexOf(File.separator);
		this.servicePath = path.substring(0,pos);
	    
		//TODO: Mirar si al final vale para algo esta propiedad aquí.
		//Creo el timestamp para la petición
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS");
	    String fecha = sdf.format(new Date());
	    this.setTimeStamp(fecha+"+01:00");		//apaño

		
	    //Que sepa todas las respuestas de confirmación tienen este tag. 
	    //En caso de que alguno fuera distinto habría que asignarlo específicamente en cada servicio específico
		this.setPrefixConfirmationRequest("soapenv");
	    
		this.setNumElementos(Integer.valueOf(this.dataStore.getProperty("atritubos.num.elementos")));
	}
	
	
	
	
	
	/* (non-Javadoc)
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 * Para mostrar los servicios por pantalla ordenados según esta propiedad. 
	 * Cada servicio tiene definido esta propiedad para clasificarse según su emisor.
	 */
	public int compareTo(Cliente obj) {
		if (this.order > obj.order) return 1;
		if (this.order < obj.order) return -1;
		return 0;
	}

	
	
	public String getNameSpaceRoot() {
		return XMLNS_ENVELOPE_SOAP_1_1;
	}

	

	public String getNameSpaceRequest() {
		return XMLNS_PETICION_COATING;
	}
	

	public String getNameSpaceSolicitudRespuesta() {
		return XMLNS_SOLICITUD_RESPUESTA_COATING;
	}
	
	
	/**
	 * Dependiendo de si el nodo principal usa un namespace hay que hacer uso
	 * del tag empleado o no. Este método está pensado para buscar un nodo de forma directa cuando
	 * se crea la expresión Xpath ya que ahorra el tener que pensar si se usa namespace o no.  
	 * @param expression Nombre del nodo del que queremos sacar información
	 * @return Expresión Xpath preparada para ser evaluada
	 * @throws Exception
	 */
	public String getPrefixDatosEspecificosExpression(String expression) throws Exception {
		String prefixDatosEspecificos = getPrefixDatosEspecificos();
		return (prefixDatosEspecificos.length()>0 ? "//"+prefixDatosEspecificos+":"+expression : "//"+expression);
	}

	
	/**
	 * Sirve para configurar los espacios de nombres. He tenido problemas si asignaba aquí el prefijo de DatosEspecíficos 
	 * así que en caso de necesitarlo lo haré después de esta llamada. 
	 * Mediante estas asignaciones del contexto del namespace se ejecutarán correctamente las expresiones Xpath 
	 * sobre el documento XML cuando existen nodos con namespaces.
	 * @return
	 * @throws Exception
	 */
	public XPath createXPathResponse() throws Exception {
		XPathFactory factory = XPathFactory.newInstance();
		XPath xPath = factory.newXPath();
		
		xPath.setNamespaceContext(new PersonalNamespaceContext(this.getPrefixRoot(),getNameSpaceRoot()));
		xPath.setNamespaceContext(new PersonalNamespaceContext(this.getPrefixResponse(),getNameSpaceResponse()));
		//xPath.setNamespaceContext(new PersonalNamespaceContext(this.getPrefixConfirmationRequest(),getNameSpaceConfirmationRequest()));
		return xPath;
	}

	
	/**
	 * @param node
	 * @return
	 * @throws Exception
	 */
	public boolean isValidResponse(Node node) throws Exception {
		Element element = null;
		element = (Element) node;
		String codigoEstado = Util.getTextNode(element,"CodigoEstado");
		Integer cod = new Integer(codigoEstado);
		if ( (cod >=200) || (cod==2) ) return false;
		return true;
	}

	
	
	/**
	 * @param node
	 * @return
	 * @throws Exception
	 */
	public boolean is002Response(Node node) throws Exception {
		Element element = null;
		element = (Element) node;
		String codigoEstado = Util.getTextNode(element,"CodigoEstado");
		if (codigoEstado==null) return false;
		Integer cod = new Integer(codigoEstado);
		if (cod==2) return true;
		return false;
	}

	
	

	
	
	
	
	
	
	
	/**
	 * Devuelve el texto formateado estandar que será la cabecera de cualquier servicio.
	 * He visto que en algunos servicios los datos vienen en DatosEspecificos asi que puede
	 * que a veces no se muestren datos aquí, bueno, al menos la documentación sí que se muestra  
	 */
	public ResultBean getResponseOutputAtributos(Element element, ResultBean rb) throws Exception {
		String text = "";
		ResultAtributosBean rab = new ResultAtributosBean();
		
		text += "<P align='center'><font size='+1'><U>"+this.serviceName.toUpperCase()+"</font></U></P><BR>";
		
		String idPeticion = Util.getTextNode(element,"IdPeticion");
		rab.setIdPeticion(idPeticion);
		text += "<TR><TD colspan='2'>"+Config.getResource().getString("request").toUpperCase()+": "+idPeticion+"</TD></TR>";
		String numElementos = Util.getTextNode(element,"NumElementos");
		rab.setNumElementos(numElementos);
		text += "<TR><TD colspan='2'>"+Config.getResource().getString("numelementos").toUpperCase()+": "+numElementos+"</TD></TR>";
		
		String timeStamp = Util.getTextNode(element,"TimeStamp");
		rab.setTimeStamp(timeStamp);
	
		String certificateCode = Util.getTextNode(element,"CodigoCertificado");
		rab.setCodigoCertificado(certificateCode);
		text += "<TR><TD colspan='2'>"+Config.getResource().getString("certificate").toUpperCase()+": "+certificateCode+"</TD></TR>";
		Node estadoNode = Util.getNode(element,"Estado");
		if (estadoNode!=null) {
			ResultEstadoBean reb = new ResultEstadoBean();
			String codigoEstado = Util.getTextNode((org.w3c.dom.Element) estadoNode,"CodigoEstado");
			reb.setCodigoEstado(codigoEstado);
			String literalError = Util.getTextNode((org.w3c.dom.Element) estadoNode,"LiteralError");
			reb.setLiteralError(literalError);
			rab.setResultEstadoBean(reb);
			if (literalError!=null) text += "<TR><TD colspan='2'>["+codigoEstado+" - "+literalError.toUpperCase()+"]</TD></TR>";
			}
		rb.setResultAtributosBean(rab);
		String aux = rb.getTextoSalida() + text;
		rb.setTextoSalida(aux);
		return rb;
	}

	
	
	
	/**
	 * @param element
	 * @return
	 * @throws Exception
	 */
	public ResultBean getResponseOutputTitular(Element element, ResultBean resultBean) throws Exception {
		String text = "";

		String tipoDocumentacion = Util.getTextNode(element,"TipoDocumentacion"); 
		String documentacion = Util.getTextNode(element,"Documentacion"); 
		text += "<P align='left'>"+tipoDocumentacion+" "+documentacion+"</P>";
		
		//Hay servicios donde no vienen los datos del nombre y demás (tgss por eje).
		//Cuando pasa esto simplemente no se muestran así que lo dejo
		String nombreCompleto = Util.getTextNode(element,"NombreCompleto"); 
		if (nombreCompleto!=null) 
			text += "<P align='left'>"+nombreCompleto+"</P>";
		else {
			String nombre = Util.getTextNode(element,"Nombre"); 
			String apellido1 = Util.getTextNode(element,"Apellido1"); 
			String apellido2 = Util.getTextNode(element,"Apellido2"); 
			if (nombre!=null) text += "<TR><TD colspan='2' align='center'>"+nombre+" "+apellido1+" "+apellido2+"</TD></TR>";
			}
		String aux = resultBean.getTextoSalida() + text;
		resultBean.setTextoSalida(aux);
		return resultBean;
	}

	
	
	
	
	
	
	/**
	 * @param fac
	 * @param omNs
	 * @return
	 * @throws Exception
	 */
	public OMElement createSolicitantePeticionRecubrimiento(OMFactory fac, OMNamespace omNs) throws Exception {
		OMElement solicitante = fac.createOMElement("Solicitante", omNs);  
		OMElement identificadorSolicitante = fac.createOMElement("IdentificadorSolicitante", omNs);  
		identificadorSolicitante.setText(this.dataStore.getProperty("solicitante.identificacion.solicitante"));
		OMElement nombreSolicitante = fac.createOMElement("NombreSolicitante", omNs);  
		nombreSolicitante.setText(this.dataStore.getProperty("solicitante.nombre.solicitante"));
		OMElement finalidad = fac.createOMElement("Finalidad", omNs);  
		finalidad.setText(this.dataStore.getProperty("solicitante.finalidad"));
		OMElement consentimiento = fac.createOMElement("Consentimiento", omNs);  
		consentimiento.setText("Si");		//TODO: leer de fichero
		
		solicitante.addChild(identificadorSolicitante);
		solicitante.addChild(nombreSolicitante);
		solicitante.addChild(finalidad);
		solicitante.addChild(consentimiento);

		return solicitante;
		
	}

	
	
	
	
	
	
	
	/**
	 * @param fac
	 * @param omNs
	 * @return
	 * @throws Exception
	 */
	public OMElement createTitularPeticionRecubrimiento(OMFactory fac, OMNamespace omNs) throws Exception {
		OMElement titular = fac.createOMElement("Titular", omNs);  
		OMElement tipoDocumentacion = fac.createOMElement("TipoDocumentacion", omNs);  
		tipoDocumentacion.setText(this.dataStore.getProperty("titular.tipo.documentacion").trim());
		OMElement documentacion = fac.createOMElement("Documentacion", omNs);  
		documentacion.setText(this.dataStore.getProperty("titular.documentacion").trim());
		titular.addChild(tipoDocumentacion);
		titular.addChild(documentacion);

		String nombreCompleto = this.dataStore.getProperty("titular.nombre.completo");
		String nombre = this.dataStore.getProperty("titular.nombre");
		String apellido1 = this.dataStore.getProperty("titular.apellido1");
		String apellido2 = this.dataStore.getProperty("titular.apellido2");
		//Solo ponemos los datos si vienen
		if (nombreCompleto.trim().length()!=0) {
			OMElement nombreCompletoElement = fac.createOMElement("NombreCompleto", omNs);  
			nombreCompletoElement.setText(nombreCompleto);
			titular.addChild(nombreCompletoElement);
			}
		else{
			if (nombre.trim().length()!=0) {
				OMElement nombreElement = fac.createOMElement("Nombre", omNs);  
				nombreElement.setText(nombre);
				titular.addChild(nombreElement);
				}
			if (apellido1.trim().length()!=0) {
				OMElement apellido1Element = fac.createOMElement("Apellido1", omNs);  
				apellido1Element.setText(apellido1);
				titular.addChild(apellido1Element);
				}	
			if (apellido2.trim().length()!=0) {
				OMElement apellido2Element = fac.createOMElement("Apellido2", omNs);  
				apellido2Element.setText(apellido2);
				titular.addChild(apellido2Element);
				}
			}
		
		return titular;
	}

	
	
	
	
	
	
	public OMElement createDatosGenericosPeticionRecubrimiento(OMFactory fac, OMNamespace omNs) throws Exception {
		OMElement datosGenericos = fac.createOMElement("DatosGenericos", omNs);  
		OMElement solicitanteOM = createSolicitantePeticionRecubrimiento(fac, omNs);
		OMElement titularOM = createTitularPeticionRecubrimiento(fac, omNs);
		datosGenericos.addChild(solicitanteOM);
		datosGenericos.addChild(titularOM);
		
		return datosGenericos;
	}


	
	
	
	
	
	
	/**
	 * @param fac
	 * @param omNs
	 * @return
	 * @throws Exception
	 */
	public OMElement getAtributosPeticionRecubrimiento(OMFactory fac, OMNamespace omNs) throws Exception {
		OMElement atributos = fac.createOMElement("Atributos", omNs);  
		OMElement codigoCertificado = fac.createOMElement("CodigoCertificado", omNs);  
		codigoCertificado.setText(this.certificateCode);
		atributos.addChild(codigoCertificado);
		return atributos;
	}
	

	
	
	
	
	
	
	
	/**
	 * @param fac
	 * @param omNs
	 * @return
	 * @throws Exception
	 */
	public List<OMElement> createSolicitudesTransmisionPeticionRecubrimiento(OMFactory fac, OMNamespace omNs) throws Exception {
		List<OMElement> listaSolicitudTransmision = new ArrayList<OMElement> ();
		int numTransmisiones = this.getNumElementos();
		for (int i=0; i<numTransmisiones; i++) {
			//Dependiendo del nº de transmisiones leeré de uno o varios ficheros de propiedades.
			//Normalmente será una sola petición por lo que la ruta ya se asignó en el Constructor.
			if (numTransmisiones!=1) {
				String pathSolicitudTransmision = this.servicePath+File.separator+"SolicitudTransmision"+(i+1)+".properties";
				this.dataStore = new Properties();
				FileInputStream in = new FileInputStream(pathSolicitudTransmision);
				dataStore.load(in);
				in.close();
				}

			OMElement solicitudTransmisionAux = fac.createOMElement("SolicitudTransmision", omNs);  
			OMElement datosGenericosOM = createDatosGenericosPeticionRecubrimiento(fac,omNs);
			solicitudTransmisionAux.addChild(datosGenericosOM);
			Element datosEspecificos = createDatosEspecificos();
			if (datosEspecificos!=null) {
				OMElement datosEspecificosOM = XMLUtils.toOM(datosEspecificos); 
				solicitudTransmisionAux.addChild(datosEspecificosOM);
				}
			listaSolicitudTransmision.add(solicitudTransmisionAux);
			}	//end-for
		return listaSolicitudTransmision;
	}

	
	
	
	
	
	
	/**
	 * @param isSincrono
	 * @return
	 * @throws Exception
	 */

	public OMElement procesarPeticionRecubrimiento(boolean isSincrono) throws Exception {
		OMElement peticion = null;
		OMFactory fac = OMAbstractFactory.getOMFactory();
		OMNamespace omNs = fac.createOMNamespace("http://intermediacion.redsara.es/scsp/esquemas/ws/peticion", "");  

		if (isSincrono) 
			 peticion = fac.createOMElement("PeticionSincrona", omNs);  
		else peticion = fac.createOMElement("PeticionAsincrona", omNs);  
		
		String aux = this.dataStore.getProperty("atritubos.num.elementos");		
		Integer numElementos = Integer.valueOf(aux);
		this.setNumElementos(numElementos);
		
		OMElement solicitudes = fac.createOMElement("Solicitudes", omNs);  
		List<OMElement> solicitudTranmsion = createSolicitudesTransmisionPeticionRecubrimiento(fac,omNs);
		for (OMElement e : solicitudTranmsion) {
			solicitudes.addChild(e);
			}
		
		OMElement atributos = getAtributosPeticionRecubrimiento(fac,omNs);
		peticion.addChild(atributos);  
		peticion.addChild(solicitudes);
	
		//String url = "http://localhost:8080/scsp-ws/ws";  
		//String url = "https://10.253.114.137/scsp-ws/ws";
		//String url = "http://10.253.114.143/scsp-ws-3.1.4/ws";
		String url = Config.getCoatingProperties().getProperty("coating.configuration.url");
		log.info("ENVIANDO PETICION AL RECUBRIMIENTO # ENDPOINT : "+url);
		ServiceClient client = new ServiceClient();  

		
		Options opts = new Options();  
		opts.setTimeOutInMilliSeconds(300000);  
		//opts.setTransportInProtocol(Constants.TRANSPORT_HTTP); 
		opts.setTo(new EndpointReference(url));  
		if (isSincrono) 
			 opts.setAction("peticionSincrona");
		else opts.setAction("peticionAsincrona");
		client.setOptions(opts);  

		System.out.println(peticion.toString());
		OMElement respuesta = client.sendReceive(peticion);  
		System.out.println(respuesta);
		
		//Curioso, si no pongo lo siguiente falla con la 3º invocación al Recubrimiento.
		client.cleanup();
		client.cleanupTransport();

		
		return respuesta;
	}
	
	
	
	
	/**
	 * @param idPeticion
	 * @param codigoCertificado
	 * @param numElementos
	 * @return
	 * @throws Exception
	 */
	final public OMElement procesarSolicitudRespuestaRecubrimiento(String idPeticion, String codigoCertificado, String numElementos) throws Exception {
		OMElement solicitudRespuesta = null;
		
		this.setNumElementos(Integer.valueOf(numElementos));
		
		OMFactory fac = OMAbstractFactory.getOMFactory();
		OMNamespace omNs = fac.createOMNamespace("http://intermediacion.redsara.es/scsp/esquemas/ws/solicitudRespuesta", "");  

		solicitudRespuesta = fac.createOMElement("SolicitudRespuesta", omNs);  
		
		OMElement atributos = fac.createOMElement("Atributos", omNs);  
		OMElement idPeticionElement = fac.createOMElement("IdPeticion", omNs);  
		idPeticionElement.setText(idPeticion);
		OMElement codigoCertificadoElement = fac.createOMElement("CodigoCertificado", omNs);  
		codigoCertificadoElement.setText(codigoCertificado);
		OMElement numElementosElement = fac.createOMElement("NumElementos", omNs);  
		numElementosElement.setText(numElementos);
		
		atributos.addChild(idPeticionElement);
		atributos.addChild(codigoCertificadoElement);
		atributos.addChild(numElementosElement);
		solicitudRespuesta.addChild(atributos);

		//String url = "http://localhost:8080/scsp-ws/ws";
		String url = Config.getCoatingProperties().getProperty("coating.configuration.url");
		ServiceClient client = new ServiceClient();  
		Options opts = new Options();  
		opts.setTimeOutInMilliSeconds(300000);  
		//options.setTransportInProtocol(Constants.TRANSPORT_HTTP); 
		opts.setTo(new EndpointReference(url));  

		opts.setAction("solicitudRespuesta");
		client.setOptions(opts);  

		System.out.println(solicitudRespuesta.toString());
		OMElement respuesta = client.sendReceive(solicitudRespuesta);  
		System.out.println(respuesta);
		
		//Curioso, si no pongo lo siguiente falla con la 3º invocación al Recubrimiento.
		client.cleanup();
		client.cleanupTransport();

		return respuesta;
	}
	
	
	
	
	
	
	/**
	 * @param obj
	 * @param element
	 * @return
	 * @throws Exception
	 * La mayor parte de los servicios devuelven el mensaje de error en el nodo Estado.
	 */
	public String getErrorMsj(java.lang.Object obj, org.w3c.dom.Element element) throws Exception {
		String text = "";
		switch(Config.MODE) {
			case Config.MODE_INVOKE_SERVICES_COATING:
			case Config.MODE_ISSUED_SERVICES_COATING:
				String litError = Util.getTextNode(element,"LiteralError");
				text += "<TR><TD colspan='2'>"+litError+"</TD></TR>";
				break;

			default:break;
			}
		return text;
	}


	
	
	
	/**
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public org.w3c.dom.Element getResponseDatosEspecificos(java.lang.Object obj) throws Exception {
		String expression = null;
		XPathExpression xp =  null;
		Element element = null;
		Element datosEspecificos = null;
		
		if (!(obj instanceof org.w3c.dom.Element)) 
			throw new Exception("DatosEspecificos is not org.w3c.dom.Element type. Current type: "+obj.getClass().toString()); 
		//Respuesta obtenida mediante el recubrimiento
		element = (Element) obj;
		expression = getPrefixDatosEspecificosExpression("DatosEspecificos");	
		XPath xPath = createXPathResponse();
		xPath.setNamespaceContext(new PersonalNamespaceContext(this.getPrefixDatosEspecificos(),getNameSpaceDatosEspecificos()));
		xp = xPath.compile(expression);
		Object node = xp.evaluate(element,XPathConstants.NODE); 
		datosEspecificos = (Element) node;
		return datosEspecificos;
	}
	
	

	
	
	
	/* ##################################################################################
	 * //abstract public String getResponseOutput(Respuesta resp) throws Exception;
	 * //abstract public String getResponseOutput(Document doc) throws Exception;
     *
	 * NOTA: Originalmente para la integración con las librerías SCSP opté por emplear el método getResponseOutput(Respuesta resp).
	 * La llamada del clienteUnico devuelve un objeto de este tipo por lo que se puede hacer un GET de todas las propiedades
	 * a través de las librerías excepto para los <DatosEspecificos>.
	 * 
	 * Al usar el Recubrimiento obtuve un objeto que transformé a org.w3c.dom.Document para poder trabajar sobre él, es decir,
	 * que también tenía un método getResponseOutput(Document doc). 
	 *
	 * En ambos objetos están todos los datos de la respuesta así que al final he decidido usar un método común:
	 * 
	 * String getResponseOutput(java.lang.Object obj) y ya diferencio dependiendo del tipo objeto usado.
	  ##################################################################################*/
	
	/**
	 * @param obj Objeto que contiene la información relativa a la respuesta del servicio.
	 * @return Cadena de texto formateada con la información que se quiere mostrar por pantalla.
	 * @throws Exception
	 * He visto que a través de las librerías SCSP se trabaja con un objeto de tipo Respuesta donde los <DatosEspecificos>
	 * son de tipo org.w3c.dom.Document. Luego veo que mediante el cliente creado para usar el recubrimiento se trabaja
	 * con un objeto de tipo org.w3c.dom.Document. 
	 * Así que voy a intentar tener un solo método para usar ambas maneras.
	 */
	public ResultBean getResponseOutput(java.lang.Object obj) throws Exception {
		String text = "";
		boolean validResponse = false;
		Element element = null;
		Element datosEspecificos = null;
		
		ResultBean rb = new ResultBean();
		rb.setTextoSalida(text);
		
		if (!(obj instanceof org.w3c.dom.Element)) 
			//text = UtilWizard.getTextResponseOutput(obj,this.getNameSpaceResponse(),this.getPrefixResponse(),this.getNameSpaceDatosEspecificos(),this.getPrefixDatosEspecificos()); 
			throw new Exception("Response is not org.w3c.dom.Element type. Current type: "+obj.getClass().toString()); 
		
		
		//Respuesta obtenida mediante el recubrimiento
		element = (Element) obj;
		datosEspecificos = getResponseDatosEspecificos(obj);
		rb = getResponseOutputAtributos(element,rb);
		if ( (is002Response(element)) || (datosEspecificos==null) ) {
			return rb;
			}
		rb = getResponseOutputTitular(element,rb);

		if (this.getNumElementos()==1) {	//ES MONOPETICION.
			text += "<TR><TD colspan='2'><HR/></TD></TR><BR/>";
			validResponse = isValidResponse(element);						
			if (!validResponse) {
				text += getErrorMsj(obj,element);
				String aux = rb.getTextoSalida() + text;
				rb.setTextoSalida(aux);
				return rb;
				}
			//WoW: Curioso porque en ciertas clases con búsqueda mediante xpath, si no llamo a esté método no funciona. 
			Element datosEspecificosAux = Util.copyAllNodes(datosEspecificos,datosEspecificos.getNodeName(),this.getPrefixDatosEspecificos(),this.getNameSpaceDatosEspecificos());
			String textDatosEspeficicos = getDatosEspecificosOutput(datosEspecificosAux);
			String aux = rb.getTextoSalida() + textDatosEspeficicos;
			rb.setTextoSalida(aux);
			}
		else{	//ES MULTIPETICION.
			text = "<HTML><HEAD><TITLE/></HEAD/><BODY>MULTI-PETICION<BR><TABLE>";
			text += "<TR><TD colspan='2'><HR/></TD></TR><BR/>";
			//WoW: No he conseguido obtener los diferentes valores mediante xpath así que he recurrido a este otro método.
			List<Node> listaTransmisiones = Util.getNodeList(element,"TransmisionDatos"); 
			for (Node transmisionDatosNode : listaTransmisiones) {
				Node datosEspecificosNode = Util.findNode(transmisionDatosNode, this.getPrefixDatosEspecificos()+":DatosEspecificos");
				validResponse = isValidResponse(datosEspecificosNode);
				if (!validResponse) {
					String litError = getErrorMsj(obj,(org.w3c.dom.Element) datosEspecificosNode);			
					text += "<TR><TD colspan='2'>"+litError+"</TD></TR>";
					continue;
					}
				else{
					text += getDatosEspecificosOutput((org.w3c.dom.Element) datosEspecificosNode);
					text += "<TR><TD colspan='2'><BR/></TD></TR>";
					}
				String aux = rb.getTextoSalida() + text;
				rb.setTextoSalida(aux);
				}	//end-for
			}

		return rb;
	}

	
	
	
	/**
	 * @param obj
	 * @return
	 * @throws Exception
	 */
	public ResultBean getConfirmacionOutput(java.lang.Object obj) throws Exception {
		String text = "";
		ResultBean resultBean = new ResultBean();
		
		if (!(obj instanceof org.w3c.dom.Element)) 
			throw new Exception("Confirmation is not org.w3c.dom.Element type. Current type: "+obj.getClass().toString()); 
		
		Element element = (Element) obj;
		resultBean = getResponseOutputAtributos(element,resultBean);
		
		
		return resultBean;
	}

	
	/**
	 * Este método se usará mediante reflexión por lo que me interesa obtener el máximo 
	 * de valores en una sola llamada.
	 * @return Diferentes valores que queremos usar 
	 
	public Object[] getServiceInformation() {
		String serviceName = this.serviceName;
		String clave = this.cifEmisor+"#"+this.certificateCode+"#V"+this.scspVersion;
		Object[] objs = new Object[2];
		objs[0] = serviceName;
		objs[1] = clave;
		return objs;
	}
	
	*/
	
	


	public String getServiceName() {
		return serviceName;
	}

	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}

	public String getCertificateCode() {
		return certificateCode;
	}

	public void setCertificateCode(String certificateCode) {
		this.certificateCode = certificateCode;
	}


	public String getDescriptionServiceName() {
		return descriptionServiceName;
	}

	public void setDescriptionServiceName(String descriptionServiceName) {
		this.descriptionServiceName = descriptionServiceName;
	}
	
	public String getPrefixRoot() {
		return prefixRoot;
	}

	public void setPrefixRoot(String prefixRoot) {
		this.prefixRoot = prefixRoot;
	}

	public String getPrefixResponse() {
		return prefixResponse;
	}

	public void setPrefixResponse(String prefixResponse) {
		this.prefixResponse = prefixResponse;
	}

	public String getPrefixDatosEspecificos() {
		return prefixDatosEspecificos;
	}

	public void setPrefixDatosEspecificos(String prefixDatosEspecificos) {
		this.prefixDatosEspecificos = prefixDatosEspecificos;
	}

	public String getPrefixConfirmationRequest() {
		return prefixConfirmationRequest;
	}
	
	public void setPrefixConfirmationRequest(String prefixConfirmationRequest) {
		this.prefixConfirmationRequest = prefixConfirmationRequest;
	}
	
	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
	
	public Integer getNumElementos() {
		return numElementos;
	}
	
	public void setNumElementos(Integer numElementos) {
		this.numElementos = numElementos;
	}
	public String getServicePath() {
		return servicePath;
	}

	public String getTimeStamp() {
		return timeStamp;
	}

	public void setTimeStamp(String timeStamp) {
		this.timeStamp = timeStamp;
	}

	public String getIdPeticionCliente() {
		return idPeticionCliente;
	}

	public void setIdPeticionCliente(String idPeticionCliente) {
		this.idPeticionCliente = idPeticionCliente;
	}
	
	public int getScspVersion() {
		return scspVersion;
	}

	public void setScspVersion(int scspVersion) {
		this.scspVersion = scspVersion;
	}


	public String getCifEmisor() {
		return cifEmisor;
	}

	public void setCifEmisor(String cifEmisor) {
		this.cifEmisor = cifEmisor;
	}
	
	public String getEmisor() {
		return emisor;
	}

	public void setEmisor(String emisor) {
		this.emisor = emisor;
	}
	
	
	
	public Properties getDataStore() {
		return dataStore;
	}
	public void setDataStore(Properties dataStore) {
		this.dataStore = dataStore;
	}
	
	
	


}

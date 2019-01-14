package es.dipucr.integracion.ejemplo.recubrimiento;


import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.util.XMLUtils;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import es.dipucr.integracion.Config;
import es.dipucr.integracion.general.Util;

public class ClienteAxiomPeticionSincrona {
	
	private static final Logger LOGGER = Logger.getLogger(ClienteAxiomPeticionSincrona.class);

	
	public static Map<String,String> datosTitular = new LinkedHashMap<String,String>();

	static {
		datosTitular.put("cdi.SVDTUWS01.nodo.nacionalidad"				,"Nacionalidad");
		datosTitular.put("cdi.SVDTUWS01.nodo.sexo"						,"Sexo");
	}

	
	
	String XML_EXAMPLE = 
		"<DatosEspecificos xmlns='http://www.map.es/scsp/esquemas/datosespecificos'>" +
		"	<SolicitanteDatos>														" +
		"		<Tipo>app</Tipo>													" +
		"	</SolicitanteDatos>														" +
		"	<Solicitud>																" +
		"		<NumSoporte/>														" +
		"	</Solicitud>															" +
		"</DatosEspecificos>														";

	
	
	
	public ClienteAxiomPeticionSincrona() {
		
	}
	
	
	
	public String getResponseCommonInformation(Element element) throws Exception {
		String text = "";
		text += "<P align='center'><font size='+1'><U>SERVICIO EJEMPLO RECUBRIMIENTO</font></U></P><BR/>";
		
		String idPeticion = Util.getTextNode(element,"IdPeticion");
		text += "<TR><TD colspan='2'>"+Config.getResource().getString("request").toUpperCase()+": "+idPeticion+"</TD></TR>";
		String certificateCode = Util.getTextNode(element,"CodigoCertificado");
		text += "<TR><TD colspan='2'>"+Config.getResource().getString("certificate").toUpperCase()+": "+certificateCode+"</TD></TR>";
		
		String tipoDocumentacion = Util.getTextNode(element,"TipoDocumentacion"); 
		String documentacion = Util.getTextNode(element,"Documentacion"); 
		text += "<P align='left'>"+tipoDocumentacion+" "+documentacion+"</P>";
		
		String nombreCompleto = Util.getTextNode(element,"NombreCompleto"); 
		if (nombreCompleto!=null) 
			text += "<P align='left'>"+nombreCompleto+"</P>";
		else {
			String nombre = Util.getTextNode(element,"Nombre"); 
			String apellido1 = Util.getTextNode(element,"Apellido1"); 
			String apellido2 = Util.getTextNode(element,"Apellido2"); 
			if (nombre!=null) text += "<TR><TD colspan='2' align='center'>"+nombre+" "+apellido1+" "+apellido2+"</TD></TR>";
			}
		return text;
	}

	
	
	public String getDatosEspecificosOutput(Element datosEspecificos) throws Exception {
		String result = null;
		String text = "";

		result = Util.getTextNode(datosEspecificos,"ns1:Nombre");
		text += "<TR><TD>"+Config.getResource().getString("cdi.SVDTUWS01.nodo.nombre").toUpperCase()+": </TD><TD>"+result+"</TD></TR>";
    	
		result = Util.getTextNode(datosEspecificos,"ns1:Apellido1");
    	text += "<TR><TD>"+Config.getResource().getString("cdi.SVDTUWS01.nodo.apellido1").toUpperCase()+": </TD><TD>"+result+"</TD></TR>";
    	
		result = Util.getTextNode(datosEspecificos,"ns1:Apellido2");
		result = (result==null ? "" : result);
    	text += "<TR><TD>"+Config.getResource().getString("cdi.SVDTUWS01.nodo.apellido2").toUpperCase()+": </TD><TD>"+result+"</TD></TR>";
   	
    	result = Util.getTextNode(datosEspecificos,"ns1:Identificador");
    	text += "<TR><TD>"+Config.getResource().getString("cdi.SVDTUWS01.nodo.identificador").toUpperCase()+": </TD><TD>"+result+"</TD></TR>";
    	
		Iterator<String> iter = datosTitular.keySet().iterator();
	    while (iter.hasNext()) {
	    	String field = (String) iter.next();
	    	String nodeAux = (String) datosTitular.get(field);
			result = Util.getTextNode(datosEspecificos,"ns1:"+nodeAux);
	    	text += "<TR><TD>"+Config.getResource().getString(field).toUpperCase()+": </TD><TD>"+result+"</TD></TR>";
	    	}
   		return text;
	}

	
	
	
	public String invoke(){
		String text = "";
		OMFactory fac = OMAbstractFactory.getOMFactory();
		OMNamespace omNs = fac.createOMNamespace("http://intermediacion.redsara.es/scsp/esquemas/ws/peticion", "");  
		
		
		OMElement peticionSincrona = fac.createOMElement("PeticionSincrona", omNs);

		OMElement atributos = fac.createOMElement("Atributos", omNs);  
		OMElement codigoCertificado = fac.createOMElement("CodigoCertificado", omNs);  
		codigoCertificado.setText("SVDTUWS01");
		atributos.addChild(codigoCertificado);
 
		OMElement solicitante = fac.createOMElement("Solicitante", omNs);  
		OMElement identificadorSolicitante = fac.createOMElement("IdentificadorSolicitante", omNs);  
		identificadorSolicitante.setText("05695305E");
		OMElement nombreSolicitante = fac.createOMElement("NombreSolicitante", omNs);  
		nombreSolicitante.setText("MARIA TERESA CARMONA GONZALEZ");
		OMElement finalidad = fac.createOMElement("Finalidad", omNs);  
		finalidad.setText("PRUEBA RECUBRIMIENTO");
		OMElement consentimiento = fac.createOMElement("Consentimiento", omNs);  
		consentimiento.setText("Si");
		
		solicitante.addChild(identificadorSolicitante);
		solicitante.addChild(nombreSolicitante);
		solicitante.addChild(finalidad);
		solicitante.addChild(consentimiento);
		
		OMElement titular = fac.createOMElement("Titular", omNs);  
		OMElement tipoDocumentacion = fac.createOMElement("TipoDocumentacion", omNs);  
		tipoDocumentacion.setText("NIF");
		OMElement documentacion = fac.createOMElement("Documentacion", omNs);  
		documentacion.setText("02639958H");
		titular.addChild(tipoDocumentacion);
		titular.addChild(documentacion);
		
		OMElement datosGenericos = fac.createOMElement("DatosGenericos", omNs);  
		datosGenericos.addChild(solicitante);
		datosGenericos.addChild(titular);
		
//		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
//		factory.setNamespaceAware(true);
//		DocumentBuilder generador = factory.newDocumentBuilder();
//		InputStream is = new ByteArrayInputStream(XML_EXAMPLE.getBytes("UTF-8"));
//		Document doc = generador.parse(is);
//		OMElement datosEspecificos = XMLUtils.toOM(doc.getDocumentElement()); 
		
		OMElement solicitudTransmision = fac.createOMElement("SolicitudTransmision", omNs);  
		solicitudTransmision.addChild(datosGenericos);
//		solicitudTransmision.addChild(datosEspecificos);
		
		OMElement solicitudes = fac.createOMElement("Solicitudes", omNs);  
		solicitudes.addChild(solicitudTransmision);
		
		peticionSincrona.addChild(atributos);  
		peticionSincrona.addChild(solicitudes);

		
		String url = "http://10.12.200.200:8081/scsp-ws/ws";  
		//String url = "https://10.253.114.137/scsp-ws/ws/";
		try {
			ServiceClient client = new ServiceClient();
			Options opts = new Options();  
			opts.setTimeOutInMilliSeconds(300000);  
			//options.setTransportInProtocol(Constants.TRANSPORT_HTTP); 
			opts.setTo(new EndpointReference(url));  
			opts.setAction("peticionSincrona");  
			client.setOptions(opts);
			
			

			System.out.println(peticionSincrona.toString());
			OMElement res = client.sendReceive(peticionSincrona);  
			System.out.println(res);
		
			Element element = XMLUtils.toDOM(res);
			text = "<HTML><BODY><TABLE>";
			text += getResponseCommonInformation(element);
			text += "<TR><TD colspan='2' align='center'><HR/></TD></TR>";
			text += getDatosEspecificosOutput(element) ;
			text += "</TABLE></BODY></HTML>";
		} catch (AxisFault e) {
			LOGGER.error("Error. " + e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error("Error. " + e.getMessage(), e);
		}  
		
		return text;
	}
	
}

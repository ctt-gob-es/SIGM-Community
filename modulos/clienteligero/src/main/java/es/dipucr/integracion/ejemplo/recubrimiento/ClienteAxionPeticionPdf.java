package es.dipucr.integracion.ejemplo.recubrimiento;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.axis2.AxisFault;
import org.apache.axis2.addressing.EndpointReference;
import org.apache.axis2.client.Options;
import org.apache.axis2.client.ServiceClient;
import org.apache.axis2.util.XMLUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import es.dipucr.integracion.Config;
import es.dipucr.integracion.general.Util;

public class ClienteAxionPeticionPdf {
	
	private static final Logger LOGGER = Logger.getLogger(ClienteAxionPeticionPdf.class);

	
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

	
	
	
	public ClienteAxionPeticionPdf() {
		
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
		
		/**
		 * PETICIÓN 
		 * **/
		
		OMElement peticionSincrona = fac.createOMElement("PeticionSincrona", omNs);
   
		OMElement atributos = fac.createOMElement("Atributos", omNs);  
		OMElement codigoCertificado = fac.createOMElement("CodigoCertificado", omNs);  
		codigoCertificado.setText("SVDTUWS01");
		atributos.addChild(codigoCertificado);
 
		OMElement solicitante = fac.createOMElement("Solicitante", omNs);  
		
		OMElement identificadorSolicitante = fac.createOMElement("IdentificadorSolicitante", omNs);  
		//identificadorSolicitante.setText("05695305E");
		//identificadorSolicitante.setText("05684076V");
		identificadorSolicitante.setText("P1300000E");
		OMElement nombreSolicitante = fac.createOMElement("NombreSolicitante", omNs);  
		nombreSolicitante.setText("Diputación Provincial de Ciudad Real-Cenpri");
		
		//Finalidad
		OMElement finalidad = fac.createOMElement("Finalidad", omNs);  
		finalidad.setText("PRUEBA-DPCR2011/15");
		
		//Consentimiento
		OMElement consentimiento = fac.createOMElement("Consentimiento", omNs);  
		consentimiento.setText("Si");
		
		//Funcionario
		OMElement funcionario = fac.createOMElement("Funcionario", omNs);
		OMElement nifFuncionario = fac.createOMElement("NifFuncionario", omNs);
		nifFuncionario.setText("05695305E");
		OMElement nombreCompletoFuncionario = fac.createOMElement("NombreCompletoFuncionario", omNs);
		nombreCompletoFuncionario.setText("MARIA TERESA CARMONA GONZALEZ");
		funcionario.addChild(nombreCompletoFuncionario);
		funcionario.addChild(nifFuncionario);
		
		//Unidad Tramitadora
		OMElement unidadTramitadora = fac.createOMElement("UnidadTramitadora", omNs);
		unidadTramitadora.setText("Cenpri");
		
		//Procedimiento
		OMElement procedimiento = fac.createOMElement("Procedimiento", omNs);
		OMElement codProcedimiento = fac.createOMElement("CodProcedimiento", omNs);
		codProcedimiento.setText("TODOS");
		OMElement nombreProcedimiento = fac.createOMElement("NombreProcedimiento", omNs);
		nombreProcedimiento.setText("TODOS LOS SERVICIOS");
		procedimiento.addChild(codProcedimiento);
		procedimiento.addChild(nombreProcedimiento);
		
		//Unidad Tramitadora
		OMElement idExpediente = fac.createOMElement("IdExpediente", omNs);
		unidadTramitadora.setText("DPCR2011/15");
		
		solicitante.addChild(identificadorSolicitante);
		solicitante.addChild(nombreSolicitante);
		solicitante.addChild(unidadTramitadora);
		solicitante.addChild(procedimiento);
		solicitante.addChild(finalidad);
		solicitante.addChild(consentimiento);
		solicitante.addChild(funcionario);
		solicitante.addChild(idExpediente);
		

		
		OMElement titular = fac.createOMElement("Titular", omNs);  
		OMElement tipoDocumentacion = fac.createOMElement("TipoDocumentacion", omNs);  
		tipoDocumentacion.setText("NIF");
		OMElement documentacion = fac.createOMElement("Documentacion", omNs);  
		documentacion.setText("10000944S");
		titular.addChild(tipoDocumentacion);
		titular.addChild(documentacion);
		
		OMElement datosGenericos = fac.createOMElement("DatosGenericos", omNs);  
		datosGenericos.addChild(solicitante);
		datosGenericos.addChild(titular);
		
		OMElement solicitudTransmision = fac.createOMElement("SolicitudTransmision", omNs);  
		solicitudTransmision.addChild(datosGenericos);
		
		OMElement solicitudes = fac.createOMElement("Solicitudes", omNs);  
		solicitudes.addChild(solicitudTransmision);
		
		peticionSincrona.addChild(atributos);  
		peticionSincrona.addChild(solicitudes);

		
		String url = "http://10.12.200.200:8081/scsp-ws/ws";  
		//String url = "https://10.253.114.137/scsp-ws/ws/";
		Element element = null;
		
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
		
			element = XMLUtils.toDOM(res);
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
		
		/**
		 * 
		 * PETICIÓN DEL PDF
		 * 
		 * **/
		OMFactory facPDF = OMAbstractFactory.getOMFactory();
		OMNamespace omNsPDF = facPDF.createOMNamespace("http://intermediacion.redsara.es/scsp/esquemas/ws/peticion", "");
		OMElement peticionPdf = facPDF.createOMElement("PeticionPdf", omNsPDF);

		try {
			//String sIdPeticion = devuelveValor(element, "IdPeticion");
			String sIdPeticion =  Util.getTextNode(element,"IdPeticion");
			OMElement idPeticion = fac.createOMElement("IdPeticion", omNsPDF);
			idPeticion.setText(sIdPeticion);
			
			//String sIdTransmision = devuelveValor(element, "IdTransmision");
			String sIdTransmision = Util.getTextNode(element,"IdTransmision");
			OMElement idTransmision = fac.createOMElement("IdTransmision", omNsPDF);
			idTransmision.setText(sIdTransmision);
			
			
			peticionPdf.addChild(idPeticion);
			peticionPdf.addChild(idTransmision);
			
			
			ServiceClient client = new ServiceClient();
			Options opts = new Options();  
			opts.setTimeOutInMilliSeconds(300000);  
			//options.setTransportInProtocol(Constants.TRANSPORT_HTTP); 
			opts.setTo(new EndpointReference(url));  
			opts.setAction("PeticionPdf");  
			client.setOptions(opts);
			
			System.out.println(peticionPdf.toString());
			OMElement res = client.sendReceive(peticionPdf); 
			Element respuestaPdf = XMLUtils.toDOM(res);
//			Node nodePdf =Util.getNode(respuestaPdf, "pdf");
//			System.out.println(res);
			
			StringBuffer sb = new StringBuffer(Util.getTextNode(respuestaPdf,"pdf"));
			
			byte decoded[] = Base64.decodeBase64(sb.toString());
			
			FileOutputStream  fos = null;
			try {
				fos = new FileOutputStream("C://prueba.pdf");
			} catch (IOException e) {
				LOGGER.error("Error. " + e.getMessage(), e);
			}
			fos.write(decoded);
			fos.close();
			
			


		} catch (AxisFault e) {
			LOGGER.error("Error. " + e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error("Error. " + e.getMessage(), e);
		}  
		
		return text;
	}



	private String devuelveValor(Element elementRespuesta, String string) {
		String valor = "";
		
		System.out.println(elementRespuesta);
		
		return valor;
	}
	
}

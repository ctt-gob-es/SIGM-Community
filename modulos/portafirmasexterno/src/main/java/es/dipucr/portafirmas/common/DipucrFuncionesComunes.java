package es.dipucr.portafirmas.common;

import javax.xml.namespace.QName;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import org.apache.axiom.om.OMElement;
import org.apache.axiom.soap.SOAPEnvelope;
import org.apache.axis.AxisFault;
import org.apache.axis2.client.OperationClient;
import org.apache.axis2.wsdl.WSDLConstants;
import org.apache.log4j.Logger;


public class DipucrFuncionesComunes {
	
	public static final Logger logger = Logger.getLogger(DipucrFuncionesComunes.class);
	
	
	public static void imprimirErrorEnvio(OperationClient mensaje) throws AxisFault, org.apache.axis2.AxisFault {
		SOAPEnvelope result = mensaje.getMessageContext(WSDLConstants.MESSAGE_LABEL_IN_VALUE).getEnvelope();
		  
		  SOAPEnvelope result2 = mensaje.getMessageContext(WSDLConstants.MESSAGE_LABEL_OUT_VALUE).getEnvelope();
		  if(result!=null){
			  String respuesta = result.toString();
			  logger.warn("RESPUESTA: "+respuesta);
		  }
		  if(result2!=null){
			  String Envio = result2.toString();			  
			  logger.warn("ENVIO: "+Envio);
		  }	 
	}
	
	 public static void printResult(SOAPEnvelope result) {
	        try {
	            XMLStreamWriter writer = XMLOutputFactory.newInstance().createXMLStreamWriter(System.out);
	            if (result != null) {
	                OMElement resultOM = result.getBody().getFirstChildWithName(new QName("result"));
	                logger.warn("Result is:" + resultOM.getText());
	            } else
	            	logger.warn("Result is null");
	        } catch (XMLStreamException e) {
	            e.printStackTrace();
	        } catch (FactoryConfigurationError e) {
	            e.printStackTrace();
	        }
	    }
}

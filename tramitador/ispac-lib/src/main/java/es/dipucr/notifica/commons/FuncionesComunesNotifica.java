package es.dipucr.notifica.commons;

import java.io.IOException;
import java.util.Properties;

import javax.xml.soap.SOAPException;

import org.apache.axis.AxisFault;
import org.apache.axis.client.Call;
import org.apache.axis.handlers.soap.SOAPService;
import org.apache.axis.message.SOAPHeaderElement;
import org.apache.log4j.Logger;

import es.dipucr.notifica.firma.ClientHandler;


public class FuncionesComunesNotifica {
	
	public static final Logger logger = Logger.getLogger(FuncionesComunesNotifica.class);
	
	
	/**
	 * @param _call
	 * @throws AxisFault
	 */
	public static void imprimirErrorEnvio(org.apache.axis.client.Call _call) throws AxisFault {
		String requestXML = _call.getMessageContext().getRequestMessage().getSOAPPartAsString();
		String responseXML = _call.getMessageContext().getResponseMessage().getSOAPPartAsString();
		logger.warn("REQUEST");
		logger.warn(requestXML);
		logger.warn("RESPONSE");
		logger.warn(responseXML);
	}
	
	
	/**
	 * @param _call
	 */
		
	public static void firmarPeticion(Call _call){
		try {
            Properties configuration = new Properties();
            configuration.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("securityConfiguration.properties"));
            // Creacion del manejador que securizara la peticion
            ClientHandler sender = new ClientHandler(configuration);
			
			SOAPService soapService=new SOAPService(sender, null, null);
			soapService.setUse(org.apache.axis.constants.Use.LITERAL);
			soapService.getServiceDescription().setTypeMapping(_call.getTypeMapping());
			soapService.getTypeMappingRegistry().register("", _call.getTypeMapping());
			_call.setSOAPService(soapService);

            
        } catch (Exception e) {
        	logger.error("Error al firmar la petición."+e.getMessage(), e);
        }
		
	}
	
	public static SOAPHeaderElement dameApiKeySoapHeader(String apiKey) throws SOAPException {
		
            Properties configuration = new Properties();
            try {
				configuration.load(Thread.currentThread().getContextClassLoader().getResourceAsStream("securityConfiguration.properties"));
			} catch (IOException e) {
				throw new SOAPException("NO ESTÁ CONFIGURADO EL AKI_KEY DE NOTIFICA PARA ESTA ENTIDAD");
			}
            

            //add SOAP header for authentication
            SOAPHeaderElement api_key = new SOAPHeaderElement("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "api_key");
            if(null==apiKey || apiKey.equals(""))
            	throw new SOAPException("NO ESTÁ CONFIGURADO EL AKI_KEY DE NOTIFICA PARA ESTA ENTIDAD");
            //Para obtener el apiKey de un fichero del fichero de configuracion
            //api_key.addTextNode(configuration.getProperty("security.apikey"));            
            api_key.addTextNode(apiKey);
            
            return api_key;                  
		
	}
	
	public static SOAPHeaderElement dameApiKeySoapHeaderPorParametro(String apiKey) throws SOAPException {
		
        //add SOAP header for authentication
        SOAPHeaderElement api_key = new SOAPHeaderElement("https://administracionelectronica.gob.es/notifica/ws/notifica/1.0/", "api_key");
        if(null==apiKey || apiKey.equals(""))
        	throw new SOAPException("NO ESTÁ CONFIGURADO EL AKI_KEY DE NOTIFICA PARA ESTA ENTIDAD");
        //Para obtener el apiKey de un fichero del fichero de configuracion
        //api_key.addTextNode(configuration.getProperty("security.apikey"));            
        api_key.addTextNode(apiKey);
        
        return api_key;                  
	
}
	
	public static String GetBase64EncodedSHA1Hash(String filename)
	{
	   // FileStream fs = new FileStream(filename, FileMode.Open, FileAccess.Read, FileShare.Read
	    //SHA1Managed sha1 = new SHA1Managed();
	   // {
	    //    return Convert.ToBase64String(sha1.ComputeHash(fs));
	   // }
		return null;
	}
	
	

}

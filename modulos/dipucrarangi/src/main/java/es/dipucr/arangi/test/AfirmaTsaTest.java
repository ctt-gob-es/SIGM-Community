package es.dipucr.arangi.test;

import org.apache.log4j.xml.DOMConfigurator;
import org.bouncycastle.tsp.TimeStampToken;

import com.steria.tsa.cliente.RFCGeneradorTS;
import com.steria.tsa.cliente.excepciones.AutenticacionException;
import com.steria.tsa.cliente.excepciones.ConexionException;
import com.steria.tsa.cliente.excepciones.PeticionException;
import com.steria.tsa.cliente.tipos.TiposRFC;

public class AfirmaTsaTest {

	public static void main(String[] args) {
		
		DOMConfigurator.configure("conf/log4j.xml");
		
		 byte[] datos = {'h','o','l','a'};
		 try { 
		 		RFCGeneradorTS rfcGenerator = new RFCGeneradorTS(); 
		 		TimeStampToken token = rfcGenerator.generarTS(TiposRFC.RFC3161_SSL, datos); 
		 		if(token != null) { 
		 				System.out.println(token.toString()); 
		 		} 
		 		else { 
		 				System.out.println("Respuesta vacía."); 
		 		} 
		 		TimeStampToken token2 = rfcGenerator.generarTS(TiposRFC.RFC3161_HTTPS, datos); 
		 		if(token2 != null) { 
		 				System.out.println(token2.toString()); 
		 		} 
		 		else { 
		 				System.out.println("Respuesta vacía."); 
		 		} 
		 	} 
		 	catch(ConexionException conEx) { 
		 			conEx.printStackTrace(); 
		 	} 
		 	catch(PeticionException petEx) { 
		 			petEx.printStackTrace(); 
		 	} 
		 	catch(AutenticacionException autEx) { 
		 			autEx.printStackTrace(); 
		 	} 
	}
}

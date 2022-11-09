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

package es.seap.minhap.portafirmas.dsic.csv.ws;

import java.io.File;
import java.io.FileOutputStream;
import java.net.MalformedURLException;
import java.net.URL;

import es.seap.minhap.portafirmas.utils.Constants;
import net.java.dev.jaxb.array.IntArray;

public class Cliente {
	private static final String RESULTADO_NUEVOS_SERVICIOS_PDF = Constants.FS  + "home" + Constants.FS  + "rus" + Constants.FS +"desarrollo" + Constants.FS  +"ficheros" + Constants.FS + "resultadoNUEVOSERVICIO.pdf";
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		//String urlString = "http://localhost:8080/Portafirma/WSDocSSPP/ObtenerDocCSV?wsdl";
		String urlString = "http://10.1.101.22:8080/Portafirma/WSDocSSPP/ObtenerDocCSV?wsdl";
		//String urlString = "http://portafirma.preapp.mpt.es/portafirma/WSDocSSPP/ObtenerDocCSV?wsdl";
		//String urlString = "http://prejappgal03.mpt.es/portafirma/WSDocSSPP/ObtenerDocCSV?wsdl";
		//String urlString = "https://portafirma.mpt.es/portafirma/WSDocSSPP/ObtenerDocCSV?wsdl";
		try {
			
			System.setProperty("javax.net.ssl.trustStore", "/home/rus/workspace2/ConfiguracionPortafirma/conf/truststore.jks");
			System.setProperty("javax.net.ssl.trustStorePassword", "changeit");

			
			
			URL url = new URL(urlString);
			
			WSDocCSVImplService service = new WSDocCSVImplService(url);
			WSDocCSV cliente = service.getWSDocCSVImplPort();
			
			// CSV DESARROLLO A65C795F2EDC64DF2D360E35728C4D91
			// CSV PREPRODUCCION "AAB40440345D1E4B552B239D3F2E9114"
			
			 // Alternative WS-SecurityPolicy method
	        /*Map ctx = ((BindingProvider)cliente).getRequestContext();
	        ctx.put("ws-security.username", "joe");
	        ctx.put("ws-security.callback-handler", ClientPasswordCallback.class.getName());
	        ctx.put("ws-security.password", "joespassword");*/ // another option for passwords
			
			
			/*org.apache.cxf.endpoint.Client client = ClientProxy.getClient(cliente);
			org.apache.cxf.endpoint.Endpoint cxfEndpoint = client.getEndpoint();
			
			Map<String,Object> outProps = new HashMap<String,Object>();
			
			outProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
			// Specify our username
			outProps.put(WSHandlerConstants.USER, "ACCEDA");
			// Password type : plain text
			outProps.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_TEXT);
			// for hashed password use:
			//properties.put(WSHandlerConstants.PASSWORD_TYPE, WSConstants.PW_DIGEST);
			// Callback used to retrieve password for given user.
			outProps.put(WSHandlerConstants.PW_CALLBACK_CLASS, 
					ClientPasswordCallback.class.getName());
			
			WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(outProps);
			cxfEndpoint.getOutInterceptors().add(wssOut);*/
			
			
			
			IntArray intArray = new IntArray();
			String csv = "noexiste";
			//String csv = "60356DFD06BB886456A53271A1D6017D";
			
			
			Documento d = cliente.obtenerDocumento("hola", csv, intArray, "tedebemos", "una");
			if (d==null) {
				System.out.println("NULL");
			} else {
			
			
				FileOutputStream fout;
				
				fout = new FileOutputStream(new File(RESULTADO_NUEVOS_SERVICIOS_PDF));
				
				fout.write(es.seap.minhap.portafirmas.business.Base64.decode(d.getContenidoDocumento()));
				fout.close();
				
				System.out.println(d.getNombreDocumento());
			}
			
			
			
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
		// TODO Auto-generated method stub

	}

}

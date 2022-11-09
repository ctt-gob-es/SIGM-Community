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

package es.seap.minhap.portafirmas.ws.advice.client;


import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.message.Message;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.utils.WSClientUtil;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class ClientManager {

	Logger log = Logger.getLogger(ClientManager.class);
	
	private static long CONNECTION_TIMEOUT = 120000;
	private static long RECEIVE_TIMEOUT = 120000;
	
	private Map<String, AdviceService> serviciosPorURL = new HashMap<String, AdviceService>();

	public AdviceService getAdviceServiceClient(String wsdlLocation) throws FileNotFoundException, IOException, GeneralSecurityException {
		log.info("getAdviceServiceClient init");
		
		//"file:AdviceServiceCSD.wsdl"
		
		if (serviciosPorURL.containsKey(wsdlLocation)) {
			log.info("getAdviceServiceClient end");
			return serviciosPorURL.get(wsdlLocation);
		} else {
			URL adviceServiceURL = new URL (wsdlLocation);
			//URL adviceServiceURL = new URL("http://afroditaa.map.es/WSDocSSPP/AdviceService?wsdl");
			//URL adviceServiceURL = new URL("file:/D:/workspace/pfDesarrollo/src/main/webapp/WEB-INF/wsdl/AdviceServiceCSD.wsdl");
			AdviceServiceService adviceService = new AdviceServiceService(adviceServiceURL);
			
			AdviceService port = adviceService.getAdviceServicePort();
			
			// Para imprimir en log las peticiones SOAP
			//org.apache.cxf.endpoint.Client cliente = ClientProxy.getClient(port);
			//cliente.getInInterceptors().add(new LoggingInInterceptor());
			//cliente.getOutInterceptors().add(new LoggingOutInterceptor());

			WSClientUtil.setupTimeouts(ClientProxy.getClient(port), RECEIVE_TIMEOUT, CONNECTION_TIMEOUT);
			
			serviciosPorURL.put(wsdlLocation, port);
			log.info("getAdviceServiceClient end");
			return port;			
		}
	}

	/**
	 * Borra la información de las "caches"
	 */
	public void clearCache() {
		serviciosPorURL.clear();
	}
	
}

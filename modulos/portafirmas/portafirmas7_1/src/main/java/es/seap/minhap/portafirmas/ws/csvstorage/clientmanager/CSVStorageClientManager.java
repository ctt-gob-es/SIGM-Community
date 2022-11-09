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

package es.seap.minhap.portafirmas.ws.csvstorage.clientmanager;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.interceptor.LoggingInInterceptor;
import org.apache.cxf.interceptor.LoggingOutInterceptor;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.log4j.Logger;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.utils.WSClientUtil;
import es.seap.minhap.portafirmas.ws.csvstorage.client.CSVDocumentWSService;
import es.seap.minhap.portafirmas.ws.csvstorage.client.CSVDocumentWSService_Service;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class CSVStorageClientManager {

	@Resource(name = "CSVStorageProperties")
	private Properties properties;
	
	Logger log = Logger.getLogger(CSVStorageClientManager.class);
	
	private static long CONNECTION_TIMEOUT = 15000;
	private static long RECEIVE_TIMEOUT = 15000;
	
	private Map<String, Object> clientesCSVStorage = new HashMap<String, Object>();

	public CSVDocumentWSService getCsvDocumentWSService_Cliente() throws MalformedURLException {
		String wsdlLocation = properties.getProperty("csvstorage.wsdlUrl");
		String usuario = properties.getProperty("csvstorage.user");
		String pass = properties.getProperty("csvstorage.password");
		CSVDocumentWSService servicio = null;
		//El servicio se obtiene de la caché..
		if (clientesCSVStorage.containsKey(wsdlLocation)) {
			servicio = (CSVDocumentWSService) clientesCSVStorage.get(wsdlLocation);
		} else {
			// .. o si es la primera vez, se crea y se guarda en la caché.
			URL url = new URL(wsdlLocation);
			CSVDocumentWSService_Service csvDocumentWSService = new CSVDocumentWSService_Service(url);
			servicio = csvDocumentWSService.getCSVDocumentWSServicePort();
			
			// Han publicado el endpoint con referencia al localhost, en pre y en pro, de modo que lo ponemos bien
			Client cliente = ClientProxy.getClient(servicio);
			//cliente.getRequestContext().put(Message.ENDPOINT_ADDRESS, wsdlLocation);
			//Añadimos la autenticación
			Map<String, String> params = new HashMap<String, String> ();
			params.put("USUARIO", usuario);
			params.put("PASS", pass);
			params.put("PASSWORD_TYPE", "PasswordText");
			
			WSS4JOutInterceptor wssOut = interceptorUsernameToken (params);
			cliente.getEndpoint().getOutInterceptors().add(wssOut);
			cliente.getInInterceptors().add(new LoggingInInterceptor());
			cliente.getOutInterceptors().add(new LoggingOutInterceptor());
			disableChunking(cliente);
			//WSClientUtil.setupTimeouts(cliente, RECEIVE_TIMEOUT, CONNECTION_TIMEOUT);
			
			clientesCSVStorage.put(wsdlLocation, servicio);
		}
		
		return servicio;			
	}

	/**
	 * Borra la información de las "caches"
	 */
	public void clearCache() {
		clientesCSVStorage.clear();
	}
	
	/**
	 * Devuelve el interceptor de salida para añadir las cabeceras de WS-Security correspondiente
	 * a la autenticación con usuario/contraseña
	 * @param params parámetros de configuración
	 */
	private WSS4JOutInterceptor interceptorUsernameToken (Map<String, String> params) {
		
		Map<String, Object> outProps = new HashMap<String, Object> ();
		
		outProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
		outProps.put(WSHandlerConstants.USER, params.get("USUARIO"));		
		
		CSVStorageClientPasswordCallback callback = new CSVStorageClientPasswordCallback (params.get("PASS"));
		
		outProps.put(WSHandlerConstants.PASSWORD_TYPE, params.get("PASSWORD_TYPE"));
		outProps.put(WSHandlerConstants.PW_CALLBACK_REF, callback);
		
		WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(outProps);
		return wssOut;
	}
	
	void disableChunking(Client client) {
        HTTPConduit httpConduit = (HTTPConduit) client.getConduit();
        HTTPClientPolicy policy = httpConduit.getClient();
        policy.setAllowChunking(false);
    }
	
}

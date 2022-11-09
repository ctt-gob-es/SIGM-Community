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

package es.seap.minhap.portafirmas.ws.inside;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.xml.ws.soap.MTOMFeature;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.log4j.Logger;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.exceptions.GInsideException;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.WSClientUtil;
import es.seap.minhap.portafirmas.ws.inside.callback.GInsideClientPasswordCallback;
import es.seap.minhap.portafirmas.ws.inside.ginside.webservicefiles.GInsideUserTokenWS;
import es.seap.minhap.portafirmas.ws.inside.ginside.webservicefiles.GInsideUserTokenWebService;

@Service
public class GInsideClientManager {


	Logger log = Logger.getLogger(GInsideClientManager.class);

	private static long CONNECTION_TIMEOUT = 60000;
	private static long RECEIVE_TIMEOUT = 60000;

	private Map<String, Object> ginsideClients = new HashMap<String, Object> ();
	

	/**
	 * @param wsdlLocation
	 * @return
	 * @throws GInsideException
	 */
	public GInsideUserTokenWebService getGInsideClient(Map<String, String> params) throws GInsideException {
		log.info("getGInsideClient init");
		GInsideUserTokenWebService service = null;
		try {
			if (ginsideClients.containsKey(params.get(Constants.C_PARAMETER_GINSIDE_URL))) {
				service = (GInsideUserTokenWebService) ginsideClients.get(params.get(Constants.C_PARAMETER_GINSIDE_URL));
			} else {
				// Se carga porque no se encontró en memoria
				URL url = new URL(params.get(Constants.C_PARAMETER_GINSIDE_URL));
				
				GInsideUserTokenWS ginsideService = 
						new GInsideUserTokenWS(url);
				//service = ginsideService.getGInsideUserTokenWSPort(new MTOMFeature()); Quito lo del mtom
				service = ginsideService.getGInsideUserTokenWSPort();

				
				Client client = ClientProxy.getClient(service);
				
				WSS4JOutInterceptor wssOut = interceptorGInsideManagerUsernameToken(params);
				client.getEndpoint().getOutInterceptors().add(wssOut); 
				
//				BindingProvider bindingProvider = (BindingProvider) service;
//	            Binding binding = bindingProvider.getBinding();
//	            List<Handler> handlerChain = binding.getHandlerChain();
//	            handlerChain.add(new ImprimirPeticionSoap());
//	            binding.setHandlerChain(handlerChain);
				
				// Timeouts
				WSClientUtil.setupTimeouts(client, RECEIVE_TIMEOUT, CONNECTION_TIMEOUT)	;
				
				ginsideClients.put(params.get(Constants.C_PARAMETER_GINSIDE_URL), service); 
			}
			
		} catch (Throwable t) {
			log.error("Error al conectar a GInside: ", t);
		}				
		
		return service;	
	}
	
	
	
	/**
	 * Devuelve el interceptor de salida para añadir las cabeceras de WS-Security correspondiente
	 * a la autenticación con usuario/contraseña
	 * @param params parámetros de configuración
	 */
	private static WSS4JOutInterceptor interceptorGInsideManagerUsernameToken(Map<String, String> params) {
		Map<String, Object> outProps = new HashMap<String, Object>();
		outProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
		outProps.put(WSHandlerConstants.USER, params.get(Constants.C_PARAMETER_GINSIDE_USER));
		GInsideClientPasswordCallback callback = new GInsideClientPasswordCallback(params.get(Constants.C_PARAMETER_GINSIDE_PASSWORD));
		outProps.put(WSHandlerConstants.PASSWORD_TYPE, params.get(Constants.C_PARAMETER_GINSIDE_PASSWORD_TYPE));
		outProps.put(WSHandlerConstants.PW_CALLBACK_REF, callback);
		WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(outProps);
		return wssOut;
	}
	
	
	
}

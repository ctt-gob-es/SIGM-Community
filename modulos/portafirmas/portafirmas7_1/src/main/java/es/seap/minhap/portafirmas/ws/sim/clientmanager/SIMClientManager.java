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

package es.seap.minhap.portafirmas.ws.sim.clientmanager;


import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.message.Message;
import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.utils.WSClientUtil;
import es.seap.minhap.portafirmas.ws.sim.EnvioMensajesService;
import es.seap.minhap.portafirmas.ws.sim.EnvioMensajesServiceWSBindingPortType;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class SIMClientManager {

	Logger log = Logger.getLogger(SIMClientManager.class);
	
	private static long CONNECTION_TIMEOUT = 15000;
	private static long RECEIVE_TIMEOUT = 15000;
	
	private Map<String, EnvioMensajesServiceWSBindingPortType> envioMensajesClientes = new HashMap<String, EnvioMensajesServiceWSBindingPortType>();

	public EnvioMensajesServiceWSBindingPortType getEnvioMensajesCliente(String wsdlLocation) throws MalformedURLException {
		EnvioMensajesServiceWSBindingPortType servicio = null;
		//El servicio se obtiene de la caché..
		if (envioMensajesClientes.containsKey(wsdlLocation)) {
			servicio = envioMensajesClientes.get(wsdlLocation);
		} else {
			// .. o si es la primera vez, se crea y se guarda en la caché.
			URL envioMensajesURL = new URL(wsdlLocation);
			EnvioMensajesService envioMensajesService = new EnvioMensajesService(envioMensajesURL);
			servicio = envioMensajesService.getEnvioMensajesServicePort();
			WSClientUtil.setupTimeouts(ClientProxy.getClient(servicio), RECEIVE_TIMEOUT, CONNECTION_TIMEOUT);
			envioMensajesClientes.put(wsdlLocation, servicio);
		}
		// Han publicado el endpoint con referencia al localhost, en pre y en pro, de modo que lo ponemos bien
		Client cliente = ClientProxy.getClient(servicio);
		cliente.getRequestContext().put(Message.ENDPOINT_ADDRESS, wsdlLocation);
		return servicio;			
	}

	/**
	 * Borra la información de las "caches"
	 */
	public void clearCache() {
		envioMensajesClientes.clear();
	}
	
}

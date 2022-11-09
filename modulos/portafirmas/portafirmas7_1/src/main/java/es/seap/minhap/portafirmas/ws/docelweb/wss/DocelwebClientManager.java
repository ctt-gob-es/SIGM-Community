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

package es.seap.minhap.portafirmas.ws.docelweb.wss;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.message.Message;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.log4j.Logger;
import org.apache.ws.security.WSSecurityException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.seap.minhap.interfazGenerica.domain.Portafirmas;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.WSClientUtil;
import es.seap.minhap.portafirmas.ws.docelweb.DocelwebConstants;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.InterfazGenerica;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.InterfazGenericaService;
import es.seap.minhap.portafirmas.ws.docelweb.notificaciongenerica.NotificacionGenericaWS;
import es.seap.minhap.portafirmas.ws.docelweb.notificaciongenerica.NotificacionGenericaWSService;
import es.seap.minhap.portafirmas.ws.docelweb.wss.interceptor.client.DocelwebClientSecurityManagerInterceptor;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class DocelwebClientManager {

	@Resource(name="interfazGenericaProperties")
	private Properties interfazGenericaProperties;

	@Autowired
	private DocelwebClientSecurityManagerInterceptor docelwebClientSecurityManagerInterceptor;

	private Logger log = Logger.getLogger(DocelwebClientManager.class);

	private static long CONNECTION_TIMEOUT = 15000;
	private static long RECEIVE_TIMEOUT = 15000;

	private Map<String, NotificacionGenericaWS> notificacionGenericaWS = new HashMap<String, NotificacionGenericaWS>();
	private Map<String, InterfazGenerica> interfazGenerica = new HashMap<String, InterfazGenerica>();

	/**
	 * @param portafirmas
	 * @return
	 * @throws Exception
	 */
	public InterfazGenerica getInterfazGenericaClient(Portafirmas portafirmas) throws Exception {

		InterfazGenerica port = null;
		String wsdlLocation = portafirmas.getUrlInterfaz();

		if (interfazGenerica.containsKey(wsdlLocation)) {
			port = interfazGenerica.get(wsdlLocation);
		} else {
			try {
				if(Constants.SOAP_12.equals(portafirmas.getSoapVersion())) {
					port = (new InterfazGenericaService()).getInterfazGenericaSoap12HttpPort();
				} else {
					port = (new InterfazGenericaService()).getInterfazGenericaSoapHttpPort();
				}
				Client clienteCXF = ClientProxy.getClient(port);
				configurarCliente(clienteCXF, wsdlLocation);
				interfazGenerica.put(wsdlLocation, port);
				
			} catch (Throwable t) {
				throw new WSSecurityException("No se puede crear el WS de InterfazGenerica", t);
			}
		}

		log.info("getInterfazGenericaClient end");

		return port;

	}

	/**
	 * @param portafirmas
	 * @return
	 * @throws Exception
	 */
	public NotificacionGenericaWS getNotificacionGenericaWSClient(Portafirmas portafirmas) throws Exception {

		NotificacionGenericaWS port = null;
		String wsdlLocation = portafirmas.getUrlNotificacion();
		
		if (notificacionGenericaWS.containsKey(wsdlLocation)) {
			port = notificacionGenericaWS.get(wsdlLocation);
		} else {
			try {
				port = (new NotificacionGenericaWSService()).getNotificacionGenericaWSPort();
				Client clienteCXF = ClientProxy.getClient(port);
				configurarCliente(clienteCXF, wsdlLocation);
				notificacionGenericaWS.put(wsdlLocation, port);
			
			} catch (Throwable t) {
				log.error("Fallo al crear el servicio de notificación: ", t);
				throw new WSSecurityException("No se puede crear el WS de NotificacionGenericaWS", t);
			}
		}
		
		return port;
	}
	
	private void configurarCliente(Client clienteCXF, String wsdlLocation) throws WSSecurityException {
		WSClientUtil.setupTimeouts(clienteCXF, RECEIVE_TIMEOUT, CONNECTION_TIMEOUT);
		
		docelwebClientSecurityManagerInterceptor.setupClientWSSecurity(clienteCXF);

		clienteCXF.getRequestContext().put(Message.ENDPOINT_ADDRESS, wsdlLocation);

		// Se deshabilita comprobación CN. No recomendado para producción. Basta con no poner o comentar, el parámetro en el fichero properties.
		HTTPConduit httpConduit = (HTTPConduit) clienteCXF.getConduit();
		TLSClientParameters tlsCP = new TLSClientParameters();
		tlsCP.setDisableCNCheck(new Boolean(interfazGenericaProperties.getProperty(DocelwebConstants.DISABLE_CN_CHECK)));
		httpConduit.setTlsClientParameters(tlsCP);
	}

	/**
	 * Borra la información de las "caches"
	 */
	public void clearCache() {
		interfazGenerica.clear();
		notificacionGenericaWS.clear();
	}
	
}

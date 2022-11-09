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

package es.seap.minhap.portafirmas.ws.eeutil;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.frontend.ClientProxy;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.exceptions.EeutilException;
import es.seap.minhap.portafirmas.utils.WSClientUtil;
import es.seap.minhap.portafirmas.ws.eeutil.misc.EeUtilService;
import es.seap.minhap.portafirmas.ws.eeutil.misc.EeUtilServiceImplService;

@Service
public class EeUtilMiscClientManager {


	Logger log = Logger.getLogger(EeUtilMiscClientManager.class);

	private static long CONNECTION_TIMEOUT = 60000;
	private static long RECEIVE_TIMEOUT = 60000;

	private Map<String, Object> eeutilClients = new HashMap<String, Object> ();

	/**
	 * @param wsdlLocation
	 * @return
	 * @throws EeutilException
	 */
	public EeUtilService getEEUtilMiscClient (String wsdlLocation) throws EeutilException {
		log.info("getEEUtilClient init");
		EeUtilService client = null;
		try {
			if (eeutilClients.containsKey(wsdlLocation)) {
				// Ya se cargó en memoria
				log.debug("Firma obtenido de cache");
				client = (EeUtilService) eeutilClients.get(wsdlLocation);
				
			} else {
				// Se carga porque no se encontró en memoria
				URL url = new URL(wsdlLocation);
				
				EeUtilServiceImplService eeutilService = new EeUtilServiceImplService(url);
				client = eeutilService.getEeUtilServiceImplPort();

				// Timeouts
				WSClientUtil.setupTimeouts(ClientProxy.getClient(client), RECEIVE_TIMEOUT, CONNECTION_TIMEOUT)	;	
				WSClientUtil.disableChunking(ClientProxy.getClient(client));
				

				eeutilClients.put(wsdlLocation, client);

			}
		} catch (Exception t) {
			log.error("Error desconocido: ", t);
			throw new EeutilException ("No se puede crear el WS de EEUTIL a partir de la URL: " + wsdlLocation, t);
		}			

		log.info("getEEUtilClient end");

		return client;		
	}
	
}

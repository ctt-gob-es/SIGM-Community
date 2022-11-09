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

package es.seap.minhap.portafirmas.ws.afirma5.clientmanager;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.security.GeneralSecurityException;
import java.util.HashMap;
import java.util.Map;

import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.message.Message;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.utils.WSClientUtil;
import es.seap.minhap.portafirmas.ws.afirma5.Afirma5Constantes;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema.DSSSignature;
import es.seap.minhap.portafirmas.ws.afirma5.dssafirmaverify.client.oasis.names.tc.dss._1_0.core.schema.DSSSignatureService;
import es.seap.minhap.portafirmas.ws.afirma5.exception.Afirma5Exception;
import es.seap.minhap.portafirmas.ws.afirma5.validarcertificado.client.Validacion;
import es.seap.minhap.portafirmas.ws.afirma5.validarcertificado.client.ValidacionService;
import es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client.Firma;
import es.seap.minhap.portafirmas.ws.afirma5.validarfirma.client.FirmaService;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class Afirma5ClientManager{

	@Autowired
	private Afirma5SecurityManager afirma5SecurityManager;
	
	Logger log = Logger.getLogger(Afirma5ClientManager.class);

	private static long CONNECTION_TIMEOUT = 15000;
	private static long RECEIVE_TIMEOUT = 15000;

	private Map<String, Validacion> validacionServices = new HashMap<String, Validacion> ();
	private Map<String, Firma> firmaServices = new HashMap<String, Firma> ();
	private Map<String, DSSSignature> dssSignatureServices = new HashMap<String, DSSSignature> ();

	public Firma getFirmaServiceClient (Map<String, String> configParams) throws FileNotFoundException,
	IOException, GeneralSecurityException, Afirma5Exception, SocketTimeoutException {
		log.info("getFirmaServiceClient init");
		Firma client = null;

		String wsdlLocation = configParams.get(Afirma5Constantes.FIRMA_URL) + Afirma5Constantes.AFIRMA_VALIDAR_FIRMA_SERVICE;

		if (firmaServices.containsKey(wsdlLocation)) {
			log.debug("Firma obtenido de cache");
			client = firmaServices.get(wsdlLocation);
		} else {

			URL validacionURL = new URL(wsdlLocation);
			FirmaService firmaService = null;
			try {
				firmaService = new FirmaService(validacionURL);
				client = firmaService.getValidarFirma();
			}
			catch (Exception e) {
				if(e.getClass().equals(SocketTimeoutException.class)){
					log.error("ERROR: Afirma5ClientManager.getFirmaServiceClient: "
							+ "El servicio validarFirma no está disponible: ", e);
					throw new SocketTimeoutException("El servicio validarFirma no está disponible: " + e);
				}else{
					throw new Afirma5Exception ("No se puede crear el WS de @firma a partir de la URL: " + validacionURL,e);
				}
			}			

			ClientProxy.getClient(client).getRequestContext().put(Message.ENDPOINT_ADDRESS, wsdlLocation);
			// disabled chunking
			WSClientUtil.disableChunking(ClientProxy.getClient(client));
			// Timeouts
			WSClientUtil.setupTimeouts (ClientProxy.getClient(client), RECEIVE_TIMEOUT, CONNECTION_TIMEOUT);
			// Propiedades WS-Security
			afirma5SecurityManager.setupWSSecurity (ClientProxy.getClient(client), configParams);

			firmaServices.put(wsdlLocation, client);
		}

		log.info("getFirmaServiceClient end");
		return client;
	}

	public Validacion getValidacionServiceClient(Map<String, String> configParams) throws FileNotFoundException, IOException, GeneralSecurityException, Afirma5Exception {
		log.info("getValidacionServiceClient init");

		Validacion client = null;
		String wsdlLocation = configParams.get(Afirma5Constantes.FIRMA_URL) + Afirma5Constantes.AFIRMA_VALIDAR_CERTIFICADO_SERVICE;

		if (validacionServices.containsKey(wsdlLocation)) {

			log.info("Validacion obtenido de caché");
			client = validacionServices.get(wsdlLocation);

		} else {

			URL validacionURL = new URL(wsdlLocation);
			ValidacionService validacionService = null;
			try {
				validacionService = new ValidacionService(validacionURL);

				// Propiedades SSL para obtener el servicio
				client = validacionService.getValidarCertificado();
			} catch (Throwable t) {
				throw new Afirma5Exception ("No se puede crear el WS de @firma a partir de la URL: " + validacionURL,t);
			}			

			ClientProxy.getClient(client).getRequestContext().put(Message.ENDPOINT_ADDRESS, wsdlLocation);
			// disabled chunking
			WSClientUtil.disableChunking(ClientProxy.getClient(client));
			// Timeouts
			WSClientUtil.setupTimeouts (ClientProxy.getClient(client), RECEIVE_TIMEOUT, CONNECTION_TIMEOUT);
			// Propiedades WS-Security
			afirma5SecurityManager.setupWSSecurity (ClientProxy.getClient(client), configParams);

			validacionServices.put(wsdlLocation, client);
		}

		log.info("getValidacionServiceClient end");
		return client;
	}

	public DSSSignature getDSSSignatureServiceClient (Map<String, String> configParams) throws FileNotFoundException, IOException, GeneralSecurityException, Afirma5Exception {

		log.info("getDSSSignatureServiceClient init");

		DSSSignature client = null;
		String wsdlLocation = configParams.get(Afirma5Constantes.FIRMA_URL) + Afirma5Constantes.AFIRMA_DSS_AFIRMA_VERIFY_SERVICE;

		if (dssSignatureServices.containsKey(wsdlLocation)) {

			log.info("DSSSignatureService obtenido de caché");
			client = dssSignatureServices.get(wsdlLocation);

		} else {
			URL url = new URL(wsdlLocation);
			try {
				DSSSignatureService dssSignatureService = new DSSSignatureService (url);
				client = dssSignatureService.getDSSAfirmaVerify();
			} catch (Throwable t) {
				t.printStackTrace();
			}

			ClientProxy.getClient(client).getRequestContext().put(Message.ENDPOINT_ADDRESS, wsdlLocation);
			// disabled chunking
			WSClientUtil.disableChunking(ClientProxy.getClient(client));
			// Timeouts
			WSClientUtil.setupTimeouts (ClientProxy.getClient(client), RECEIVE_TIMEOUT, CONNECTION_TIMEOUT);
			// Propiedades WS-Security
			afirma5SecurityManager.setupWSSecurity (ClientProxy.getClient(client), configParams);

			dssSignatureServices.put(wsdlLocation, client);
		}
		return client;
	}

	/**
	 * Borra la información de las "caches"
	 */
	public void clearCache() {
		validacionServices.clear();
		firmaServices.clear();
		dssSignatureServices.clear();
	}
}

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

package es.seap.minhap.portafirmas.ws.docelweb.wss.interceptor.client;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.components.crypto.Crypto;
import org.apache.ws.security.components.crypto.CryptoFactory;
import org.apache.ws.security.handler.RequestData;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.ws.docelweb.DocelwebConstants;
import es.seap.minhap.portafirmas.ws.docelweb.wss.callback.DocelwebClientPasswordCallback;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class DocelwebClientSecurityManagerInterceptor {

	@Resource(name = "interfazGenericaProperties")
	private Properties interfazGenericaProperties;

	/**
	 * Crea el interceptor para enviar peticiones securizadas con certificado
	 * @param client cliente del WS.
	 * @throws WSSecurityException
	 */
	public void setupClientWSSecurity(Client client) throws WSSecurityException {
		Map<String, Object> outProps = new HashMap<String, Object>();
		outProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.SIGNATURE);
		outProps.put(WSHandlerConstants.USER, interfazGenericaProperties.get(Constants.MERLIN_KEYSTORE_ALIAS));
		outProps.put(WSHandlerConstants.SIG_KEY_ID, DocelwebConstants.WSSECURITY_DIRECT_REFERENCE);
		DocelwebClientPasswordCallback callback = new DocelwebClientPasswordCallback((String)interfazGenericaProperties.get(Constants.MERLIN_KEYSTORE_PRIVATE_PASSWORD));
		outProps.put(WSHandlerConstants.PW_CALLBACK_REF, callback);
		outProps.put(WSHandlerConstants.SIG_PROP_FILE, "nombre_no_nulo");

		final Crypto crypto = CryptoFactory.getInstance(interfazGenericaProperties);	

		WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(outProps) {
			@Override
			protected Crypto loadCryptoFromPropertiesFile(String propFilename, RequestData reqData)
					throws WSSecurityException {
				return crypto;
			}
		};
		client.getEndpoint().getOutInterceptors().add(wssOut);

	}

}

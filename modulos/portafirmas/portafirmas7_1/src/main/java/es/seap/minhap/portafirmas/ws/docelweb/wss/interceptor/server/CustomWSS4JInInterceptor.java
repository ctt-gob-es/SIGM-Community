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

package es.seap.minhap.portafirmas.ws.docelweb.wss.interceptor.server;

import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.cxf.ws.security.wss4j.WSS4JInInterceptor;
import org.apache.ws.security.WSSecurityException;
import org.apache.ws.security.components.crypto.Crypto;
import org.apache.ws.security.components.crypto.CryptoFactory;
import org.apache.ws.security.handler.RequestData;

public class CustomWSS4JInInterceptor extends WSS4JInInterceptor {

	@Resource(name = "interfazGenericaProperties")
	private Properties interfazGenericaProperties;
	
	public CustomWSS4JInInterceptor(Map<String, Object> properties) {
		super(properties);
	}

	@Override
	protected Crypto loadCrypto(String cryptoPropertyFile, String cryptoPropertyRefId, RequestData requestData)
			throws WSSecurityException {
		return CryptoFactory.getInstance(interfazGenericaProperties);
	}

	public Properties getCryptoProperties() {
		return interfazGenericaProperties;
	}

	public void setCryptoProperties(Properties interfazGenericaProperties) {
		this.interfazGenericaProperties = interfazGenericaProperties;
	}

}

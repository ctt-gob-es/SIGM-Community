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

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.apache.cxf.endpoint.Client;
import org.apache.cxf.ws.security.wss4j.WSS4JOutInterceptor;
import org.apache.log4j.Logger;
import org.apache.ws.security.handler.WSHandlerConstants;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.ws.afirma5.Afirma5Constantes;
import es.seap.minhap.portafirmas.ws.afirma5.exception.Afirma5Exception;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class Afirma5SecurityManager {
	
	@Resource(name = "afirmaProperties")
	private Properties afirmaProperties;
	
	private static Logger log = Logger.getLogger(Afirma5SecurityManager.class);
	
	/**
	 * Crea los interceptores para añadir las cabeceras de WS-Security necesarias, y los añade al cliente
	 * @param client cliente del WS.
	 * @param params parámetros de configuración
	 * @throws Afirma5Exception
	 */
	public void setupWSSecurity (Client client, Map<String, String> params) throws Afirma5Exception {
		log.debug("Modo de autenticacion: " + params.get(Afirma5Constantes.FIRMA_SECURITY_MODE));
		// Autenticación con usuario/contraseña
		if (Afirma5Constantes.USERNAME_TOKEN_MODE.equals(params.get(Afirma5Constantes.FIRMA_SECURITY_MODE))) {
			
			WSS4JOutInterceptor wssOut = interceptorUsernameToken (params);
			client.getEndpoint().getOutInterceptors().add(wssOut);
			
		// Autenticación con certificado
		} else if (Afirma5Constantes.BINARY_SECURITY_TOKEN_MODE.equals(params.get(Afirma5Constantes.FIRMA_SECURITY_MODE))) {
			
			WSS4JOutInterceptor wssOut = interceptorBinaryToken (params);			
			client.getEndpoint().getOutInterceptors().add(wssOut);
		
		// Autenticación errónea
		} else if (!Afirma5Constantes.NONE_MODE.equals(params.get(Afirma5Constantes.FIRMA_SECURITY_MODE)))  {
			throw new Afirma5Exception ("Authentication mode not correct " + params.get(Afirma5Constantes.FIRMA_SECURITY_MODE));
		}
	}
	
	/**
	 * Devuelve el interceptor de salida para añadir las cabeceras de WS-Security correspondiente
	 * a la autenticación con usuario/contraseña
	 * @param params parámetros de configuración
	 */
	private WSS4JOutInterceptor interceptorUsernameToken (Map<String, String> params) {
		
		Map<String, Object> outProps = new HashMap<String, Object> ();
		
		outProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.USERNAME_TOKEN);
		outProps.put(WSHandlerConstants.USER, params.get(Afirma5Constantes.FIRMA_SECURITY_USER));		
		
		Afirma5ClientPasswordCallback callback = new Afirma5ClientPasswordCallback (params.get(Afirma5Constantes.FIRMA_SECURITY_PASSWORD));
		
		outProps.put(WSHandlerConstants.PASSWORD_TYPE, params.get(Afirma5Constantes.FIRMA_SECURITY_PASSWORD_TYPE));
		outProps.put(WSHandlerConstants.PW_CALLBACK_REF, callback);
		
		WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(outProps);
		return wssOut;
	}
	
	/**
	 * Devuelve el interceptor de salida para añadir las cabeceras de WS-Security correspondiente
	 * a la autenticación con certificado
	 * @param params parámetros de configuración
	 */
	private WSS4JOutInterceptor interceptorBinaryToken (Map<String, String> params) {
		
		Map<String, Object> outProps = new HashMap<String, Object> ();
		
		outProps.put(WSHandlerConstants.ACTION, WSHandlerConstants.SIGNATURE);		
		outProps.put(WSHandlerConstants.USER, params.get(Afirma5Constantes.FIRMA_SECURITY_CERT_ALIAS));
		outProps.put(WSHandlerConstants.SIG_KEY_ID, Afirma5Constantes.WSSECURITY_DIRECT_REFERENCE);
		
		Afirma5ClientPasswordCallback callback = new Afirma5ClientPasswordCallback (params.get(Afirma5Constantes.FIRMA_SECURITY_CERT_PWD));

		outProps.put(WSHandlerConstants.PW_CALLBACK_REF, callback);
		
		outProps.put("afirmaPropertiesRef", afirmaProperties);
		outProps.put(WSHandlerConstants.SIG_PROP_REF_ID, "afirmaPropertiesRef");
		
		WSS4JOutInterceptor wssOut = new WSS4JOutInterceptor(outProps);
		return wssOut;
	}
}

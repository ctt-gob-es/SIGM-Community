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

package es.seap.minhap.portafirmas.utils;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.security.GeneralSecurityException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;
import org.apache.log4j.Logger;


public class WSClientUtil {
	
	protected final static Logger log = Logger.getLogger(WSClientUtil.class);
	
	public static void setupTimeouts (Client client, long receiveTimeout, long connectionTimeout) {
		
		HTTPConduit httpConduit = (HTTPConduit) client.getConduit();
		HTTPClientPolicy policy = httpConduit.getClient();

		// set time to wait for response in milliseconds. zero means unlimited

		policy.setReceiveTimeout(receiveTimeout);
		policy.setConnectionTimeout(connectionTimeout);
		
	}
	
	
	/**
	 * Establece las propiedades SSL
	 */
	public static void setupTLS(Client client, String truststorePath, String truststorePass, String truststoreType) throws FileNotFoundException, IOException, GeneralSecurityException {
		
		HTTPConduit httpConduit = (HTTPConduit) client.getConduit();

		TLSClientParameters tlsCP = new TLSClientParameters();
	    
		KeyStore trustStore = KeyStore.getInstance(truststoreType);		
		trustStore.load(new FileInputStream(getRealPath(truststorePath)), truststorePass.toCharArray());
		TrustManager[] myTrustStoreKeyManagers = getTrustManagers(trustStore);
		tlsCP.setTrustManagers(myTrustStoreKeyManagers);
    
		//The following is not recommended and would not be done in a prodcution environment,
		//this is just for illustrative purpose
		tlsCP.setDisableCNCheck(true);

		httpConduit.setTlsClientParameters(tlsCP);

	}
	
	private static TrustManager[] getTrustManagers(KeyStore trustStore) throws NoSuchAlgorithmException, KeyStoreException {
		String alg = KeyManagerFactory.getDefaultAlgorithm();
		TrustManagerFactory fac = TrustManagerFactory.getInstance(alg);
		fac.init(trustStore);
		return fac.getTrustManagers();
	}
	
	/**
	 * Calcula el path real de un path que puede contener una variable de entorno entre las cadenas "${" y "}".
	 * Si no encuentra estas cadenas lo devuelve tal cual.
	 * @param path
	 * @return
	 */
	private static String getRealPath (String path) {
		String realPath = path;
		int iInicioExp = path.indexOf ("${");
		int iFinExp = path.indexOf("}");
		if (iInicioExp != -1 && iFinExp != -1 && iInicioExp < iFinExp) {
			String varSistema = path.substring(iInicioExp + 2, iFinExp);
			String valorVarSistema = System.getProperty(varSistema);
			realPath = valorVarSistema + path.substring(iFinExp + 1);
		}
		return realPath;
	}
	
	public static void disableChunking(Client client) {
		HTTPConduit httpConduit = (HTTPConduit) client.getConduit();
		HTTPClientPolicy policy = httpConduit.getClient();
		policy.setAllowChunking(false);
		policy.setChunkingThreshold(0);
		log.debug("AllowChunking:" + policy.isAllowChunking());
		log.debug("ChunkingThreshold:" + policy.getChunkingThreshold());
	}

}

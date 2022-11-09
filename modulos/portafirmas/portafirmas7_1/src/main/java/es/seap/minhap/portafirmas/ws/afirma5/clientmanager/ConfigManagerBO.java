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
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfConfigurationsDTO;
import es.seap.minhap.portafirmas.utils.ConfigurationUtil;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.ws.advice.client.ClientManager;
import es.seap.minhap.portafirmas.ws.docelweb.wss.DocelwebClientManager;

@Service
public class ConfigManagerBO {

	Logger log = Logger.getLogger(ConfigManagerBO.class);
	
	private Map<Long, Map<String, String>> configParams = new HashMap<Long, Map<String, String>> ();
	private Long defaultConf;
	
	@Autowired
	private BaseDAO baseDAO;

	@Autowired
	private Afirma5ClientManager afirma5ClientManager;

	@Autowired
	private ClientManager clientManager;

	@Autowired
	private DocelwebClientManager docelwebClientManager;
	
	/**
	 * Devuelve el identificador de la configuracion por defecto.
	 * @return
	 */
	public Long getDefaultConfigurationId() {
		log.debug ("Buscando configuracion por defecto");
		if (defaultConf == null) {
			loadDefaultConfigurationId();
		} 
		return defaultConf;
	}
	
	/**
	 * Carga de BB.DD. el id de la configuración por defecto.
	 */
	public void loadDefaultConfigurationId() {
		log.debug("Configuracion por defecto no encontrada en cache, se calcula");
		AbstractBaseDTO defaultConfDTO = baseDAO.queryElementOneParameter("sign.defaultConfig", null, null);
		defaultConf = defaultConfDTO.getPrimaryKey();
	}
	
	/**
	 * Carga los parámetros de configuración de Afirma de una configuracion determinada.
	 * @param idConf
	 * @return
	 * @throws
	 */
	public Map<String, String> getConfiguration(Long idConf) {
		log.debug ("Cargando parametros de configuracion: " + idConf);
		if (!configParams.containsKey(idConf)) {
			log.debug ("Parametros de configuracion NO encontrados en cache. Configuracion: " + idConf);
			loadConfiguration(idConf);			
		}
		return configParams.get(idConf);
	}
	
	/**
	 * Carga de BB.DD. los parámetros de AFirma y EEUtil de una determinada configuración.
	 * @param cParam
	 * @param idConf
	 */
	public void loadConfiguration(Long idConf) {
		log.debug ("Parametros de configuracion NO encontrados en cache. Configuracion: " + idConf);
		Map<String, String> configParam = new HashMap<String, String>();
		configParam.putAll(loadConfiguration("FIRMA%", idConf));
		
		Map<String, String> mapConfEeutil = loadConfiguration("EEUTIL%", idConf);
		if(mapConfEeutil != null && mapConfEeutil.size()>0){
			configParam.putAll(mapConfEeutil);
		}
		configParams.put(idConf, configParam);
	}

	/**
	 * Carga de BB.DD. determiandos parámetros de una determinada configuración
	 * @param cParam
	 * @param idConf
	 * @return
	 */
	private Map<String, String> loadConfiguration(String cParam, Long idConf) {
		Map<String, String> configParam = null;
		Map<String, Object> queryParam = new HashMap<String, Object>();
		queryParam.put("cparam", cParam);
		queryParam.put("idConf", idConf);
		List<AbstractBaseDTO> configurationParameters = baseDAO.queryListMoreParameters("request.signConfigParam", queryParam);
		if (configurationParameters != null && configurationParameters.size() > 0) {
			configParam = ConfigurationUtil.convierteListaParametrosConfiguracionEnMapa(configurationParameters);
		}
		return configParam;
	}

	/**
	 * Elimina una configuración de las que están precargadas
	 * @param pfConfiguration
	 */
	public void removeConfiguration(PfConfigurationsDTO pfConfiguration) {
		configParams.remove(pfConfiguration.getPrimaryKey());
	}

	/**
	 * Configura el almacén de confianza
	 */
	public void setupTrustStore() {
		List<AbstractBaseDTO> proxyParameters = baseDAO.queryListOneParameter("administration.truststoreParameters", null, null);
		Map<String, String> trustStore = ConfigurationUtil.convierteListaParametrosConfiguracionEnMapa(proxyParameters);
		System.setProperty(Constants.SYSTEM_TRUSTSTORE_FILE,  System.getProperty(Constants.SGTIC_CONFIGPATH) + trustStore.get(Constants.TRUSTSTORE_FILE));
		System.setProperty(Constants.SYSTEM_TRUSTSTORE_PASSWORD, trustStore.get(Constants.TRUSTSTORE_PASSWORD));
		System.setProperty(Constants.SYSTEM_TRUSTSTORE_TYPE, trustStore.get(Constants.TRUSTSTORE_TYPE));
		// se resetean las caches correspondientes
		afirma5ClientManager.clearCache();
		clientManager.clearCache();
		docelwebClientManager.clearCache();
	}

}

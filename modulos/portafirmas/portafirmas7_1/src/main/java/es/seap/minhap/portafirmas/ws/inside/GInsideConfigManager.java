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

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.utils.ConfigurationUtil;

@Service
public class GInsideConfigManager {

Logger log = Logger.getLogger(GInsideConfigManager.class);
	
	private Map<Long, Map<String, String>> configParams = new HashMap<Long, Map<String, String>> ();
	private Long defaultConf;
	
	@Autowired
	private BaseDAO baseDAO;

	/**
	 * Devuelve el identificador de la configuracion por defecto.
	 * @return
	 */
	public Long configuracionPorDefecto () {
				
		log.debug ("Buscando configuracion por defecto");
		if (defaultConf == null) {
			log.debug("Configuracion por defecto no encontrada en cache, se calcula");
			AbstractBaseDTO defaultConfDTO = baseDAO.queryElementOneParameter("sign.defaultConfig", null, null);
			defaultConf = defaultConfDTO.getPrimaryKey();
		} 
		
		return defaultConf;
		
	}
	
	/**
	 * Carga los parámetros de configuración de Afirma de una configuracion determinada.
	 * @param idConf
	 * @return
	 * @throws
	 */
	public Map<String, String> cargarConfiguracion (Long idConf) {
		log.debug ("Cargando parametros de configuracion: " + idConf);
		
		Map<String, String> params = null;
		if (configParams.containsKey(idConf)) {
			log.debug ("Parametros de configuracion encontrados en cache. Configuracion: " + idConf);
			params =  configParams.get(idConf);
		} else {		
			log.debug ("Parametros de configuracion NO encontrados en cache. Configuracion: " + idConf);
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("cparam", "GINSIDE%");
			parameters.put("idConf", idConf);
			List<AbstractBaseDTO> configurationParameters = baseDAO.queryListMoreParameters("request.signConfigParam", parameters);
			
			if (configurationParameters == null || configurationParameters.size() == 0) {
				return null;
			} else {
				params = ConfigurationUtil.convierteListaParametrosConfiguracionEnMapa(configurationParameters);
				configParams.put(idConf, params);
			}
		}
		return params;
	}
}

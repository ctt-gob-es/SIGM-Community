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
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.PfConfigurationsParameterDTO;
import es.seap.minhap.portafirmas.ws.docelweb.DocelwebConstants;

@Service
@Scope(value = "session", proxyMode=ScopedProxyMode.TARGET_CLASS)
public class DocelwebConfigManager  {


	Logger log = Logger.getLogger(DocelwebConfigManager.class);

	@Autowired
	private BaseDAO baseDAO;

	/**
	 * Returns a Map with default portafirma system client param config.
	 * @return The default config.
	 */
	public Map<String, String> defaultPortafirmaSystemConfig() {
		log.debug("Buscando configuracion por defecto del sistema portafirmas");
		List<PfConfigurationsParameterDTO> defaultConfigParams = baseDAO.queryListOneParameter(DocelwebConstants.QUERY_DOCEL_DEFAUTL_SYSTEM_PFIRMA_CLIENT_CONFIG, null, null);
		Map<String, String> defaultConf = new HashMap<String, String>();
		for (Iterator<PfConfigurationsParameterDTO> iterator = defaultConfigParams.iterator(); iterator.hasNext();) {
			PfConfigurationsParameterDTO confParam = iterator.next();
			if (!confParam.getPfParameter().getCparameter().equals(DocelwebConstants.DOCELWEB_SYSTEM_MANAGER_CLIENT_SECURITY_PASSWORD) && 
				!confParam.getPfParameter().getCparameter().equals(DocelwebConstants.DOCELWEB_SYSTEM_PFIRMA_CLIENT_SECURITY_PASSWORD) &&
				!confParam.getPfParameter().getCparameter().equals(DocelwebConstants.DOCELWEB_SYSTEM_MANAGER_CLIENT_SECURITY_CERT_PWD) && 
				!confParam.getPfParameter().getCparameter().equals(DocelwebConstants.DOCELWEB_SYSTEM_PFIRMA_CLIENT_SECURITY_CERT_PWD)) {
				log.debug(confParam.getPfParameter().getCparameter() + " = " + confParam.getTvalue());
			} else {
				log.debug(confParam.getPfParameter().getCparameter() + " = *****");
			}
			defaultConf.put(confParam.getPfParameter().getCparameter(), confParam.getTvalue());
		}
		loadSSLConfig(defaultConf);
		return defaultConf;
	}

	/**
	 * Returns a Map with a concrete portafirma system client param config.
	 * @param idConf The configuration identifier
	 * @return The default config.
	 */
	public Map<String, String> portafirmaSystemConfigById(Long idConf) {
		log.debug("Cargando parametros de configuracion del sistema portafirmas [" + idConf + "]");
		List<PfConfigurationsParameterDTO> defaultConfigParams = baseDAO.queryListOneParameter(DocelwebConstants.QUERY_DOCEL_CONCRETE_SYSTEM_PFIRMA_CLIENT_CONFIG, DocelwebConstants.QUERY_PARAM_DOCEL_CONF_ID, idConf);
		Map<String, String> concreteConf = new HashMap<String, String>();
		for (Iterator<PfConfigurationsParameterDTO> iterator = defaultConfigParams.iterator(); iterator.hasNext();) {
			PfConfigurationsParameterDTO confParam = iterator.next();
			if (!confParam.getPfParameter().getCparameter().equals(DocelwebConstants.DOCELWEB_SYSTEM_MANAGER_CLIENT_SECURITY_PASSWORD) && 
				!confParam.getPfParameter().getCparameter().equals(DocelwebConstants.DOCELWEB_SYSTEM_PFIRMA_CLIENT_SECURITY_PASSWORD) &&
				!confParam.getPfParameter().getCparameter().equals(DocelwebConstants.DOCELWEB_SYSTEM_MANAGER_CLIENT_SECURITY_CERT_PWD) && 
				!confParam.getPfParameter().getCparameter().equals(DocelwebConstants.DOCELWEB_SYSTEM_PFIRMA_CLIENT_SECURITY_CERT_PWD)) {
				log.debug(confParam.getPfParameter().getCparameter() + " = " + confParam.getTvalue());
			} else {
				log.debug(confParam.getPfParameter().getCparameter() + " = *****");
			}
			concreteConf.put(confParam.getPfParameter().getCparameter(), confParam.getTvalue());
		}
		loadSSLConfig(concreteConf, idConf);
		return concreteConf;
	}

	/**
	 * Returns a Map with a concrete manager system client param config.
	 * @param idConf The configuration identifier
	 * @return The default config.
	 */
	public Map<String, String> defaultSystemManagerConfig() {
		log.debug("Buscando configuracion por defecto del sistema de gestion");
		List<PfConfigurationsParameterDTO> defaultConfigParams = baseDAO.queryListOneParameter(DocelwebConstants.QUERY_DOCEL_DEFAUTL_SYSTEM_MANAGER_CLIENT_CONFIG, null, null);
		Map<String, String> defaultConf = new HashMap<String, String>();
		for (Iterator<PfConfigurationsParameterDTO> iterator = defaultConfigParams.iterator(); iterator.hasNext();) {
			PfConfigurationsParameterDTO confParam = iterator.next();
			if (!confParam.getPfParameter().getCparameter().equals(DocelwebConstants.DOCELWEB_SYSTEM_MANAGER_CLIENT_SECURITY_PASSWORD) && 
				!confParam.getPfParameter().getCparameter().equals(DocelwebConstants.DOCELWEB_SYSTEM_PFIRMA_CLIENT_SECURITY_PASSWORD) &&
				!confParam.getPfParameter().getCparameter().equals(DocelwebConstants.DOCELWEB_SYSTEM_MANAGER_CLIENT_SECURITY_CERT_PWD) && 
				!confParam.getPfParameter().getCparameter().equals(DocelwebConstants.DOCELWEB_SYSTEM_PFIRMA_CLIENT_SECURITY_CERT_PWD)) {
				log.debug(confParam.getPfParameter().getCparameter() + " = " + confParam.getTvalue());
			} else {
				log.debug(confParam.getPfParameter().getCparameter() + " = *****");
			}
			defaultConf.put(confParam.getPfParameter().getCparameter(), confParam.getTvalue());
		}
		loadSSLConfig(defaultConf);
		return defaultConf;
	}

	/**
	 * Returns a Map with a concrete manager system client param config.
	 * @param idConf The configuration identifier
	 * @return The default config.
	 */
	public Map<String, String> systemManagerConfigById(Long idConf) {
		log.debug("Cargando parametros de configuracion del sistema de gestión [" + idConf + "]");
		List<PfConfigurationsParameterDTO> defaultConfigParams = baseDAO.queryListOneParameter(DocelwebConstants.QUERY_DOCEL_CONCRETE_SYSTEM_MANAGER_CLIENT_CONFIG, DocelwebConstants.QUERY_PARAM_DOCEL_CONF_ID, idConf);
		Map<String, String> concreteConf = new HashMap<String, String>();
		for (Iterator<PfConfigurationsParameterDTO> iterator = defaultConfigParams.iterator(); iterator.hasNext();) {
			PfConfigurationsParameterDTO confParam = iterator.next();
			if (!confParam.getPfParameter().getCparameter().equals(DocelwebConstants.DOCELWEB_SYSTEM_MANAGER_CLIENT_SECURITY_PASSWORD) && 
				!confParam.getPfParameter().getCparameter().equals(DocelwebConstants.DOCELWEB_SYSTEM_PFIRMA_CLIENT_SECURITY_PASSWORD) &&
				!confParam.getPfParameter().getCparameter().equals(DocelwebConstants.DOCELWEB_SYSTEM_MANAGER_CLIENT_SECURITY_CERT_PWD) && 
				!confParam.getPfParameter().getCparameter().equals(DocelwebConstants.DOCELWEB_SYSTEM_PFIRMA_CLIENT_SECURITY_CERT_PWD)) {
				log.debug(confParam.getPfParameter().getCparameter() + " = " + confParam.getTvalue());
			} else {
				log.debug(confParam.getPfParameter().getCparameter() + " = *****");
			}
			concreteConf.put(confParam.getPfParameter().getCparameter(), confParam.getTvalue());
		}
		loadSSLConfig(concreteConf, idConf);
		return concreteConf;
	}

	private void loadSSLConfig(Map<String, String> conf, Long idConf) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put(DocelwebConstants.QUERY_PARAM_DOCEL_CONF_ID, idConf);
		parameters.put(DocelwebConstants.QUERY_PARAM_DOCEL_CONF_VALUE, DocelwebConstants.DOCELWEB_CLIENT_SSL_TRUSTEDSTORE);
		PfConfigurationsParameterDTO defaultSSLFile = (PfConfigurationsParameterDTO) baseDAO.queryElementMoreParameters(DocelwebConstants.QUERY_DOCEL_PFIRMA_CONFIG_PARAM, parameters);
		conf.put(DocelwebConstants.DOCELWEB_CLIENT_SSL_TRUSTEDSTORE, defaultSSLFile.getTvalue());
		parameters = new HashMap<String, Object>();
		parameters.put(DocelwebConstants.QUERY_PARAM_DOCEL_CONF_ID, idConf);
		parameters.put(DocelwebConstants.QUERY_PARAM_DOCEL_CONF_VALUE, DocelwebConstants.DOCELWEB_CLIENT_SSL_TRUSTEDSTORE_PASS);
		PfConfigurationsParameterDTO defaultSSLFilePass = (PfConfigurationsParameterDTO) baseDAO.queryElementMoreParameters(DocelwebConstants.QUERY_DOCEL_PFIRMA_CONFIG_PARAM, parameters);
		conf.put(DocelwebConstants.DOCELWEB_CLIENT_SSL_TRUSTEDSTORE_PASS, defaultSSLFilePass.getTvalue());
	}

	private void loadSSLConfig(Map<String, String> conf) {
		PfConfigurationsParameterDTO defaultSSLFile = (PfConfigurationsParameterDTO) baseDAO.queryElementOneParameter(DocelwebConstants.QUERY_DOCEL_PFIRMA_GENERAL_PARAM, DocelwebConstants.QUERY_PARAM_DOCEL_CONF_VALUE, DocelwebConstants.DOCELWEB_CLIENT_SSL_TRUSTEDSTORE);
		conf.put(DocelwebConstants.DOCELWEB_CLIENT_SSL_TRUSTEDSTORE, defaultSSLFile.getTvalue());
		PfConfigurationsParameterDTO defaultSSLFilePass = (PfConfigurationsParameterDTO) baseDAO.queryElementOneParameter(DocelwebConstants.QUERY_DOCEL_PFIRMA_GENERAL_PARAM, DocelwebConstants.QUERY_PARAM_DOCEL_CONF_VALUE, DocelwebConstants.DOCELWEB_CLIENT_SSL_TRUSTEDSTORE_PASS);
		conf.put(DocelwebConstants.DOCELWEB_CLIENT_SSL_TRUSTEDSTORE_PASS, defaultSSLFilePass.getTvalue());
	}

}

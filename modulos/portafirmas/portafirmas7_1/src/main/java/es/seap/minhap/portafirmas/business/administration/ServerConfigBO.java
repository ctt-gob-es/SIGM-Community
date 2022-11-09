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

package es.seap.minhap.portafirmas.business.administration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.seap.minhap.portafirmas.business.ApplicationBO;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfApplicationsDTO;
import es.seap.minhap.portafirmas.domain.PfConfigurationsDTO;
import es.seap.minhap.portafirmas.domain.PfConfigurationsParameterDTO;
import es.seap.minhap.portafirmas.domain.PfParametersDTO;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.web.beans.ParameterConfig;
import es.seap.minhap.portafirmas.ws.afirma5.clientmanager.Afirma5ClientManager;
import es.seap.minhap.portafirmas.ws.afirma5.clientmanager.ConfigManagerBO;

/**
 * 
 * @author daniel.palacios
 *
 */
@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class ServerConfigBO {
	
	@Resource(name = "messageProperties")
	private Properties messages;

	@Autowired
	private BaseDAO baseDAO;

	@Autowired
	private ApplicationBO applicationBO;
	
	@Autowired
	private ConfigManagerBO configManagerBO;

	@Autowired
	private Afirma5ClientManager afirma5ClientManager;
	
	/**
	 * Obtiene todas las configuraciones de servidor.
	 * @return Listado de configuraciones.
	 */
	public List<AbstractBaseDTO> queryConfigurationsList() {
		List<AbstractBaseDTO> listConfigurations = baseDAO.queryListMoreParameters("administration.serverConfigurationAll", null);
		return listConfigurations;
	}

	/**
	 * Obtiene todas las configuraciones de servidor.
	 * @return Listado de configuraciones.
	 */
	public List<AbstractBaseDTO> queryConfigurationsList(String search) {
		Long serverPk = new Long(0);
		if(!Util.esVacioONulo(search)) {
			serverPk = Long.parseLong(search);
		}
		return baseDAO.queryListOneParameter("administration.configurationByServer","serverPk", serverPk);
	}
	

	/**
	 * Devuelve una configuración de servidor a partir de su clave primaria
	 * @param configurationPk
	 * @return
	 */
	public AbstractBaseDTO queryConfigurationByPk(Long configurationPk) {
		Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("configPk", configurationPk);
		return baseDAO.queryElementMoreParameters("administration.configurationAdmPkQuery", queryParams);
	}

	/**
	 * Guarda una configuración por defecto
	 * @param pfConfigurationsDTO
	 */
	@Transactional(readOnly = false)
	public void saveConfigurationServer(PfConfigurationsDTO pfConfigurationsDTO) {
		boolean lMainCopy = pfConfigurationsDTO.getLmain();
		// Si va a ser la configuración por defecto, nos aseguramos de que ninguna otra lo es
		if(pfConfigurationsDTO.getLmain()) {
			// Se obtienen las configuraciones del servidor
			List<AbstractBaseDTO> configurationsList = 
					queryConfigurationsList(pfConfigurationsDTO.getPfServer().getPrimaryKeyString());
			// Se actualizan para que no sean configuración por defecto
			for (Iterator<AbstractBaseDTO> it = configurationsList.iterator(); it.hasNext();) {
				PfConfigurationsDTO configuration = (PfConfigurationsDTO) it.next();
				configuration.setLmain(false);
				baseDAO.insertOrUpdate(configuration);
			}
		}
		// Hibernate lo pudo poner a falso en el bucle. Se restituye con la copia
		pfConfigurationsDTO.setLmain(lMainCopy);
		baseDAO.insertOrUpdate(pfConfigurationsDTO);
		// Si es configuración por defecto, se actualiza la que tiene cargada el sistema
		if(lMainCopy) {
			configManagerBO.loadDefaultConfigurationId();
		}
	}

	@Transactional(readOnly = false)
	public void deleteConfigurationServer(PfConfigurationsDTO pfConfigurationsDTO) {
		// Primero se borran las relaciones configuración-parámetro ..
		Set<PfConfigurationsParameterDTO> setPfconfigurationsParameters = 
				pfConfigurationsDTO.getPfConfigurationsParameters();
		for (PfConfigurationsParameterDTO pfConfigurationsParameterDTO : setPfconfigurationsParameters) {
			baseDAO.delete(pfConfigurationsParameterDTO);
		}
		// .. y luego la configuración.
		baseDAO.delete(pfConfigurationsDTO);
	}

	/**
	 * Validación de las reglas de negocio para alta y modificaciones
	 * @param pfConfigurationsDTO
	 * @param warnings
	 */
	public void validateServerConfig(PfConfigurationsDTO pfConfigurationsDTO, ArrayList<String> warnings) {
		PfConfigurationsDTO pfConfigurationsDTO_BD = getConfigurationByCode(pfConfigurationsDTO);
		// Si el código ya existe en base de datos..
		if(pfConfigurationsDTO_BD != null) {
			// Se avisa, si es una alta o, siendo una modificación, el encontrado NO es el que estamos modificando.
			if(pfConfigurationsDTO.getPrimaryKey() == null ||
					!pfConfigurationsDTO_BD.getPrimaryKey().equals(pfConfigurationsDTO.getPrimaryKey())) {
				warnings.add(messages.getProperty("errorCServerConfigurationExists"));
			}
		}
		// Debe seleccionar una configuración de servidor por defecto
		if(!existsMainConfiguration(pfConfigurationsDTO)) {
			warnings.add(messages.getProperty("errorNoDefaultConfigurationSelected"));
		}
	}
	
	/**
	 * Devuelve verdadero si existe una configuración por defecto para 
	 * el servidor al que hace referencia la configuración
	 * @param pfConfigurationsDTO
	 * @return
	 */
	private boolean existsMainConfiguration(PfConfigurationsDTO pfConfigurationsDTO) {
		boolean exists = false;
		// Si no va a ser la configuración por defecto, nos aseguramos de que hay alguna que lo es
		if(!pfConfigurationsDTO.getLmain()) {
			// Se obtienen las configuraciones del servidor
			List<AbstractBaseDTO> configurationsList = 
					queryConfigurationsList(pfConfigurationsDTO.getPfServer().getPrimaryKeyString());
			// Se recorren para ver si alguno tiene la configuración por defecto
			for (Iterator<AbstractBaseDTO> it = configurationsList.iterator(); it.hasNext();) {
				PfConfigurationsDTO configuration = (PfConfigurationsDTO) it.next();
				if(configuration.getLmain()) {
					exists = true;
				}
			}
		} else {
			exists = true;
		}
		return exists;
	}


	/**
	 * Devuelve un configuración que coincide con el código del parámetro
	 * @param pfConfigurationsDTO
	 * @return
	 */
	private PfConfigurationsDTO getConfigurationByCode(PfConfigurationsDTO pfConfigurationsDTO) {
		return (PfConfigurationsDTO) baseDAO.queryElementOneParameter(
				"administration.configurationByCode", "code", pfConfigurationsDTO.getCconfiguration());
	}


	/**
	 * Validación de las reglas de negocio para borrado
	 * @param pfConfigurationsDTO
	 * @param warnings
	 */
	public void validateDeleteServerConfig(PfConfigurationsDTO pfConfigurationsDTO, ArrayList<String> warnings) {
		// La configuracion de servidor por defecto no puede ser eliminada
		if(pfConfigurationsDTO.getLmain()) {
			warnings.add(messages.getProperty("errorDefaultConfiguration"));
		}
		// No se puede eliminar la configuración de servidor. Existen aplicaciones asociadas
		Set<PfApplicationsDTO> applications = pfConfigurationsDTO.getPfApplications();
		if(applications != null && applications.size() > 0) {
			warnings.add(messages.getProperty("errorConfigurationHasApplications"));
		}
	}

	
	/**
	 * Devuelve una lista con todos los parámetros de servidor disponibles y marca
	 * los que tiene la configuración que se pasa como parámetro.
	 * @param configurationPk
	 * @return
	 */
	public List<ParameterConfig> getParameterConfigList(String configurationPk) {
		ArrayList<ParameterConfig> parameterConfigList = new ArrayList<ParameterConfig>();
		// Se obtiene la configuración
		PfConfigurationsDTO pfConfigurationsDTO = 
				(PfConfigurationsDTO) queryConfigurationByPk(Long.parseLong(configurationPk));
		// Relaciones configuración-parámetros
		Set<PfConfigurationsParameterDTO> confParamListDTO = pfConfigurationsDTO.getPfConfigurationsParameters();
		// Catálogo de parámetros de servidor
		List<AbstractBaseDTO> parameterListDTO = applicationBO.queryServerParameterList();
		// Se recorren los parametros cargando el valor y marcando cuando se encuentra entre las relaciones
		for (Iterator<AbstractBaseDTO> it = parameterListDTO.iterator(); it.hasNext();) {
			PfParametersDTO parameterDTO = (PfParametersDTO) it.next();
			ParameterConfig parameterConfig = new ParameterConfig();
			parameterConfig.setId(parameterDTO.getPrimaryKeyString());
			parameterConfig.setName(parameterDTO.getCparameter());
			parameterConfig.setDescription(parameterDTO.getDparameter());
			PfConfigurationsParameterDTO configParam = getConfigParam(parameterDTO, confParamListDTO);
			if(configParam != null) {
				parameterConfig.setValue(configParam.getTvalue());
				parameterConfig.setChecked(true);
			}
			parameterConfigList.add(parameterConfig);
		}
		return parameterConfigList;
	}

	/**
	 * Busca y devuelve la relación configuración-parámetro que contiene al parámetro.
	 * Devuelve null en caso de no hayarla.
	 * @param parameter
	 * @param confParamList
	 * @return
	 */
	private PfConfigurationsParameterDTO getConfigParam(PfParametersDTO parameter,
			Set<PfConfigurationsParameterDTO> confParamList) {
		PfConfigurationsParameterDTO retorno = null;
		for (PfConfigurationsParameterDTO configurationsParameter : confParamList) {
			if(parameter.getPrimaryKey() != null && parameter.getPrimaryKey().equals(configurationsParameter.getPfParameter().getPrimaryKey())) {
				retorno = configurationsParameter;
			}
		}
		return retorno;
	}

	/**
	 * Método que borra las antiguas relaciones configuración-parametro y da de alta a las nuevas
	 * @param pfConfigParamDTOArray
	 * @param pfConfigurationDTO 
	 */
	@Transactional(readOnly = false)
	public void saveParamConfig(ArrayList<PfConfigurationsParameterDTO> pfConfigParamDTOArray, PfConfigurationsDTO pfConfigurationDTO) {
		// Se borran las relaciones parametro-configuración
		for (PfConfigurationsParameterDTO configParamDTO : pfConfigurationDTO.getPfConfigurationsParameters()) {
			baseDAO.delete(configParamDTO);
		}
		// Se guardan las nuevas relaciones configuración-parámetro
		for (PfConfigurationsParameterDTO pfConfigurationsParameterDTO : pfConfigParamDTOArray) {
			baseDAO.insertOrUpdate(pfConfigurationsParameterDTO);
		}
		// Se borra los parametros de configuración asociados a esa configuración de memoria
		configManagerBO.removeConfiguration(pfConfigurationDTO);
		afirma5ClientManager.clearCache();
	}

}

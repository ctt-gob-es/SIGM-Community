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
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfConfigurationsDTO;
import es.seap.minhap.portafirmas.domain.PfConfigurationsParameterDTO;
import es.seap.minhap.portafirmas.domain.PfParametersDTO;
import es.seap.minhap.portafirmas.domain.PfServersDTO;
import es.seap.minhap.portafirmas.utils.ListComparador;
import es.seap.minhap.portafirmas.utils.Util;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class ServerAdmBO {

	@Resource(name = "messageProperties")
	private Properties messages;

	@Autowired
	private BaseDAO baseDAO;
	
	@Autowired
	private ServerConfigBO serverConfigBO;

	/**
	 * Obtiene la lista de servidores existentes
	 * @return lista de servidores
	 */
	/*public List<AbstractBaseDTO> queryList() {
		return baseDAO.queryListMoreParameters("administration.serverAdmAll", null);
	}*/
	
	
	/**
	 * Obtiene la lista de servidores existentes
	 * @return lista de servidores
	 */
	public List<AbstractBaseDTO> queryList() {
		return baseDAO.queryListMoreParameters("administration.serverQueryAll", null);
	}
	
	/**
	 * Obtiene la lista de servidores existentes
	 * @return lista de servidores
	 */
	public PfServersDTO serverByPk(Long pk) {
		return (PfServersDTO) baseDAO.queryElementOneParameter("administration.serverAdmPkQuery", "pk", pk);
	}
	
	/*
	 * OPERATIONS
	 */

	/**
	 * M&eacute;todo que permite guardar en BD los cambios realizados sobre las listas de servidores y configuraciones.
	 * @param serverList Listado de servidores.
	 * @param serverDeleteList Lista de servidores a eliminar.
	 * @param serverConfigurationsDeleteList Listado de configuraciones a eliminar.
	 * @param serverConfigurationParametersDeleteList Listado de par&aacute;metros de configuraci&oacute;n a eliminar.
	 */
	@Transactional(readOnly = false)
	public void saveServerList(List<AbstractBaseDTO> serverList,
			List<AbstractBaseDTO> serverDeleteList,
			List<AbstractBaseDTO> serverConfigurationsDeleteList,
			List<AbstractBaseDTO> serverConfigurationParametersDeleteList) {
		// Update list
		for (AbstractBaseDTO server : serverList) {
			Set<PfConfigurationsDTO> serverConfigSet = ((PfServersDTO) server).getPfConfigurations();
			Set<PfConfigurationsDTO> auxServerConfigSet = new HashSet<PfConfigurationsDTO>();
			auxServerConfigSet.addAll(serverConfigSet);
			((PfServersDTO) server).setPfConfigurations(new HashSet<PfConfigurationsDTO>());
			baseDAO.insertOrUpdate(server);
			server.setUpdated(false);
			for (AbstractBaseDTO serverConfig : auxServerConfigSet) {
				Set<PfConfigurationsParameterDTO> serverConfigParamSet = 
					((PfConfigurationsDTO) serverConfig).getPfConfigurationsParameters();
				Set<PfConfigurationsParameterDTO> auxServerConfigParamSet = new HashSet<PfConfigurationsParameterDTO>();
				auxServerConfigParamSet.addAll(serverConfigParamSet);
				((PfConfigurationsDTO) serverConfig).setPfServer((PfServersDTO) server);
				((PfConfigurationsDTO) serverConfig).setPfConfigurationsParameters(new HashSet<PfConfigurationsParameterDTO>());
				baseDAO.insertOrUpdate(serverConfig);
				serverConfig.setUpdated(false);
				for (AbstractBaseDTO serverConfigParam : auxServerConfigParamSet) {
					((PfConfigurationsParameterDTO) serverConfigParam).setPfConfiguration((PfConfigurationsDTO) serverConfig);
					baseDAO.insertOrUpdate(serverConfigParam);
					serverConfigParam.setUpdated(false);
				}
				((PfConfigurationsDTO) serverConfig).setPfConfigurationsParameters(auxServerConfigParamSet);
			}
			((PfServersDTO) server).setPfConfigurations(auxServerConfigSet);
		}
		// Delete lists
		baseDAO.deleteList(serverConfigurationParametersDeleteList);
		baseDAO.deleteList(serverConfigurationsDeleteList);
		baseDAO.deleteList(serverDeleteList);

	}

	/**
	 * M&eacute;todo que elimina la prefirma de todos los documentos asociados a la configuraci&oacute;n de servidor de entrada.
	 * @param configurations Configuraciones de servidor.
	 */
	@Transactional(readOnly = false)
	public void deletePresign(Set<Long> configurations) {
		for (Long conf : configurations) {
			baseDAO.massiveUpdate("administration.updateMassiveDocs", "conf", conf);
		}
	}

	/**
	 * Obtiene el servidor principal
	 * @return el servidor principal
	 */
	public AbstractBaseDTO mainServerQuery() {
		return baseDAO.queryElementMoreParameters(
				"administration.serverAdmDefaultServerCode", null);
	}

	/**
	 * M&eacute;todo que busca un servidor en una lista de servidores a partir de su c&oacute;digo.
	 * @param serverList Listado de servidores.
	 * @param cServer C&oacute;digo del servidor a buscar.
	 * @return Servidor encontrado.
	 */
	public AbstractBaseDTO serverCodeSearch(List<AbstractBaseDTO> serverList, String cServer) {
		PfServersDTO ret = null;
		boolean flag = false;
		if (serverList != null && !serverList.isEmpty()) {
			Iterator<AbstractBaseDTO> it = serverList.iterator();
			while (it.hasNext() && flag == false) {
				ret = (PfServersDTO) it.next();
				if (ret.getCserver().equals(cServer)) {
					flag = true;
				} else {
					ret = null;
				}
			}
		}
		return ret;
	}

	/**
	 * M&eacute;todo que convierte una lista de configuraciones en un conjunto.
	 * @param list Listado de configuraciones.
	 * @return Conjunto de configuraciones.
	 */
	public Set<PfConfigurationsDTO> getConfigurationSet(List<AbstractBaseDTO> list) {
		Set<PfConfigurationsDTO> set = new HashSet<PfConfigurationsDTO>();
		for (Iterator<AbstractBaseDTO> iterator = list.iterator(); iterator.hasNext();) {
			PfConfigurationsDTO abstractBaseDTO = (PfConfigurationsDTO) iterator.next();
			set.add(abstractBaseDTO);
		}
		return set;
	}

	/**
	 * Devuelve el conjunto de configuraciones pasado como par&aacute;metro en forma de lista ordenada
	 * @param set el conjunto de configuraciones
	 * @return la lista ordenada de configuraciones
	 */
	public List<AbstractBaseDTO> getConfigurationList(
			Set<PfConfigurationsDTO> set) {
		List<AbstractBaseDTO> list = new ArrayList<AbstractBaseDTO>();
		Iterator<PfConfigurationsDTO> iterator = set.iterator();
		while (iterator.hasNext()) {
			list.add(iterator.next());
		}
		ListComparador comp = new ListComparador("serverConfig", 1);
		Collections.sort(list, comp);
		return list;
	}
	/**
	 * Devuelve el conjunto de par&aacute;metro de configuraci&oacute;n pasado como par&aacute;metro en forma de lista ordenada
	 * @param set conjunto de par&aacute;metros de configuraci&oacute;n
	 * @return la lista ordenada de par&aacute;metros de configuraci&oacute;n
	 */
	public List<AbstractBaseDTO> getConfigurationParameterList(
			Set<PfConfigurationsParameterDTO> set) {
		List<AbstractBaseDTO> list = new ArrayList<AbstractBaseDTO>();
		Iterator<PfConfigurationsParameterDTO> iterator = set.iterator();
		while (iterator.hasNext()) {
			list.add(iterator.next());
		}
		// sort the list before to get the elements
		ListComparador comp = new ListComparador("serverParams", 1);
		Collections.sort(list, comp);
		return list;
	}

	/**
	 * M&eacute;todo que genera una lista con los par&aacute;metros seleccionables para una configuraci&oacute;n.
	 * Dicha lista contiene los par&aacute;metros que no han sido previamente asignados a la configuraci&oacute;n.
	 * @param serverParameterDefaultList Listado de par&aacute;metros por defecto del servidor.
	 * @param serverConfigurationParameterList Listado de par&aacute;metros de la configuraci&oacute;n del servidor de entrada.
	 * @return Listado de par&aacute;metros seleccionables para la configuraci&oacute;n.
	 */
	public List<AbstractBaseDTO> loadSelectableParameters(
			List<PfParametersDTO> serverParameterDefaultList,
			List<AbstractBaseDTO> serverConfigurationParameterList) {
		List<AbstractBaseDTO> returnList = new ArrayList<AbstractBaseDTO>();
		// Obtain list of cparameters from configurationparameterlist
		List<String> cParameterList = obtainCParameterList(serverConfigurationParameterList);
		returnList.addAll(serverParameterDefaultList);
		Iterator<PfParametersDTO> it = serverParameterDefaultList.iterator();
		while (it.hasNext()) {
			PfParametersDTO param = it.next();
			if (cParameterList.contains(param.getCparameter())) {
				returnList.remove(param);
			}
		}
		return returnList;
	}

	/**
	 * M&eacute;todo que devuelve una lista con todos los c&oacute;digos de los par&aacute;metros de una configuraci&oacute;n de servidor.
	 * @param serverConfigurationParameterList Listado de par&aacute;metros de una configuraci&oacute;n de servidor.
	 * @return Listado de c&oacute;digos de par&aacute;metros.
	 */
	private List<String> obtainCParameterList(List<AbstractBaseDTO> serverConfigurationParameterList) {
		List<String> returnList = new ArrayList<String>();
		Iterator<AbstractBaseDTO> it = serverConfigurationParameterList.iterator();
		while (it.hasNext()) {
			PfConfigurationsParameterDTO configParameterAux = (PfConfigurationsParameterDTO) it.next();
			returnList.add(configParameterAux.getPfParameter().getCparameter());
		}
		return returnList;
	}

	/**
	 * M&eacute;todo que permite duplicar una configuraci&oacute;n de servidor.
	 * @param duplicated Configuraci&oacute;n duplicada.
	 * @param serverConfig Configuraci&oacute;n a duplicar.
	 */
	public void duplicateServerConfiguration(PfConfigurationsDTO duplicated,
			PfConfigurationsDTO serverConfig) {

		duplicated.setLmain(false);
		duplicated.setPfServer(serverConfig.getPfServer());

		for (Iterator<PfConfigurationsParameterDTO> iterator = serverConfig
				.getPfConfigurationsParameters().iterator(); iterator.hasNext();) {
			PfConfigurationsParameterDTO configutationsParameter = (PfConfigurationsParameterDTO) iterator.next();
			PfConfigurationsParameterDTO newConfigParameter = new PfConfigurationsParameterDTO();

			// setting the new values
			newConfigParameter.setPfConfiguration(duplicated);
			newConfigParameter.setPfParameter(configutationsParameter.getPfParameter());
			newConfigParameter.setTvalue(configutationsParameter.getTvalue());
			duplicated.getPfConfigurationsParameters().add(newConfigParameter);
		}
	}

	/*
	 * VALIDATIONS
	 */

	/**
	 * M&eacute;todo que comprueba si existe un servidor concreto dentro de una lista de servidores.
	 * @param serverList Listado de servidores.
	 * @param checkServer Servidor a buscar.
	 * @return "True" si existe, "false" en caso contrario.
	 */
	/*public boolean existsCServer(List<AbstractBaseDTO> serverList, PfServersDTO checkServer) {
		boolean ret = false;

		for (Iterator<AbstractBaseDTO> iterator = serverList.iterator(); iterator.hasNext();) {
			PfServersDTO server = (PfServersDTO) iterator.next();
			if (checkServer.getPrimaryKey() != server.getPrimaryKey()
					&& checkServer.getCserver() != null
					&& checkServer.getCserver().equals(server.getCserver())) {
				ret = true;
				// server.setCserver("");
			} else {
				if (checkServer.getPrimaryKey() == null
						&& server.getPrimaryKey() == null) {
					if (checkServer.getCserver() != null
							&& checkServer.getCserver().equals(server.getCserver())
							&& !checkServer.equals(server)) {
						ret = true;
						// server.setCserver("");

					}
				}
			}

		}

		return ret;
	}*/

	/**
	 * M&eacute;todo que busca un servidor en una lista de servidores a partir de su clave primaria.
	 * @param serverList Listado de servidores.
	 * @param serverPk Clave primaria del servidor.
	 * @return El servidor buscado.
	 */
	/*public AbstractBaseDTO getServerFromList(List<AbstractBaseDTO> serverList, String serverPk) {
		PfServersDTO server = null;
		boolean flag = false;
		if (serverList != null && !serverList.isEmpty()) {
			Iterator<AbstractBaseDTO> it = serverList.iterator();
			while (it.hasNext() && flag == false) {
				server = (PfServersDTO) it.next();
				if (server.getPrimaryKeyString().equals(serverPk)) {
					flag = true;
				}
			}
		}
		return server;
	}*/

	/**
	 * M&eacute;todo que comprueba si un servidor tiene aplicaciones asociadas.
	 * @param server Servidor a comprobar.
	 * @return "True" si tiene aplicaciones asociadas, "false" en caso contrario.
	 */
	public boolean hasApplications(AbstractBaseDTO server) {
		boolean ret = false;
		Set<PfConfigurationsDTO> configurations = ((PfServersDTO) server).getPfConfigurations();
		if (configurations != null && !configurations.isEmpty()) {
			for (PfConfigurationsDTO pfConfigurationsDTO : configurations) {
				if (pfConfigurationsDTO.getPfApplications() != null
						&& !pfConfigurationsDTO.getPfApplications().isEmpty()) {
					ret = true;
				}
			}
		}
		return ret;
	}

	/**
	 * M&eacute;todo que actualiza desde la BD los par&aacute;metros de configuraci&oacute;n de un servidor.
	 * @param serverConfigurationParameter Par&aacute;metros de configuraci&oacute;n de un servidor.
	 * @return Par&aacute;metros de configuraci&oacute;n del servidor de entrada actualizados.
	 */
	public PfConfigurationsParameterDTO getConfigurationParameter(
		   PfConfigurationsParameterDTO serverConfigurationParameter) {
		if (serverConfigurationParameter.getPrimaryKey() != null) {
			baseDAO.refresh(serverConfigurationParameter);
		}

		return serverConfigurationParameter;
	}

	/**
	 * M&eacute;todo que actualiza desde BD los par&aacute;metros de configuraci&oacute;n de una lista.
	 * @param serverConfigurationParameterList
	 * @param serverParameterDefaultList
	 */
	public void updateList(List<AbstractBaseDTO> serverConfigurationParameterList,
						   List<PfParametersDTO> serverParameterDefaultList) {
		for (Iterator<AbstractBaseDTO> iterator = serverConfigurationParameterList.iterator(); iterator.hasNext();) {
			boolean found = false;
			PfConfigurationsParameterDTO aux = (PfConfigurationsParameterDTO) iterator.next();
			aux.setPfParameter((PfParametersDTO)
					baseDAO.queryElementOneParameter("configuration.parameterByPk",
													 "primaryKey", aux.getPfParameter().getPrimaryKey()));
			for (Iterator<PfParametersDTO> iterator2 = serverParameterDefaultList.iterator(); iterator2.hasNext() && !found;) {
				PfParametersDTO auxParam = iterator2.next();
				if (auxParam.getPrimaryKey() != null && auxParam.getPrimaryKey().equals(aux.getPfParameter().getPrimaryKey())) {
					found = true;
					aux.setPfParameter(auxParam);
				}
			}

		}

	}

	/**
	 * M&eacute;todo que actualiza el valor de un par&aacute;metro perteneciente a una lista de par&aacute;metros.
	 * @param parameterDefaultList Listado de par&aacute;metros.
	 * @param pfParameter Par&aacute;metro a actualizar.
	 * @return el par&aacute;metro actualizado.
	 */
	public PfParametersDTO updateParameter(
			List<PfParametersDTO> parameterDefaultList,
			PfParametersDTO pfParameter) {
		PfParametersDTO returnParameter = new PfParametersDTO();
		if (parameterDefaultList != null && !parameterDefaultList.isEmpty()) {
			Iterator<PfParametersDTO> it = parameterDefaultList.iterator();
			while (it.hasNext()) {
				PfParametersDTO auxParameter = it.next();
				if (auxParameter.getPrimaryKeyString().equals(
						pfParameter.getPrimaryKeyString())) {
					returnParameter = auxParameter;
				}
			}
		}
		return returnParameter;
	}

	/**
	 * M&eacute;todo que comprueba si alg&uacute;n servidor perteneciente a una lista contiene una determinada configuraci&oacute;n.
	 * @param serverList Listado de servidores.
	 * @param configuration Configuraci&oacute;n a buscar.
	 * @return "True" si alg&uacute;n servidor tiene dicha configuraci&oacute;n, "false" en caso contrario.
	 */
	public boolean existsConfiguration(List<AbstractBaseDTO> serverList, PfConfigurationsDTO configuration) {
		boolean exists = false;
		for (Iterator<AbstractBaseDTO> iterator = serverList.iterator(); iterator.hasNext()	&& !exists;) {
			PfServersDTO server = (PfServersDTO) iterator.next();
			for (Iterator<PfConfigurationsDTO> iterator2 = server.getPfConfigurations().iterator(); iterator2.hasNext()
					&& !exists;) {
				PfConfigurationsDTO config = (PfConfigurationsDTO) iterator2.next();
				if (configuration.getCconfiguration() != null
						&& configuration.getCconfiguration().equals(config.getCconfiguration())
						&& (configuration.getPrimaryKey() != null && configuration.getPrimaryKey().equals(config.getPrimaryKey()))) {
					exists = true;
				}
			}
		}
		return exists;
	}

	/**
	 * M&eacute;todo que permite seleccionar los servidores distintos al pasado como entrada.
	 * @param serverList Listado de servidores.
	 * @param serverEdit Servidor de entrada.
	 * @return Listado de servidor distintos al de entrada.
	 */
	public List<AbstractBaseDTO> getOtherServers(
			List<AbstractBaseDTO> serverList, PfServersDTO serverEdit) {
		List<AbstractBaseDTO> result = new ArrayList<AbstractBaseDTO>();
		for (Iterator<AbstractBaseDTO> iterator = serverList.iterator(); iterator.hasNext();) {
			PfServersDTO current = (PfServersDTO) iterator.next();
			if (!current.getCserver().equals(serverEdit.getCserver())) {
				current.setSelected(false);
				result.add(current);
			}
		}
		return result;
	}

	/**
	 * M&eacute;todo que duplica la configuraci&oacute;n de un par&aacute;metro.
	 * @param confParam Configuraci&oacute;n del par&aacute;metro a duplicar.
	 * @return Comfiguraci&oacute;n del par&aacute;metro duplicada.
	 */
	public PfConfigurationsParameterDTO clonePfConfigurationsParameterDTO(
		   PfConfigurationsParameterDTO confParam) {
		PfConfigurationsParameterDTO copy = new PfConfigurationsParameterDTO();
		copy.setCcreated(confParam.getCcreated());
		copy.setCmodified(confParam.getCmodified());
		copy.setFcreated(confParam.getFcreated());
		copy.setFmodified(confParam.getFmodified());
		copy.setPfConfiguration(confParam.getPfConfiguration());
		copy.setPfParameter(confParam.getPfParameter());
		copy.setPrimaryKey(confParam.getPrimaryKey());
		copy.setSelected(confParam.isSelected());
		copy.setTvalue(confParam.getTvalue());
		copy.setUpdated(confParam.isUpdated());
		return copy;
	}

	/**
	 * M&eacute;todo que duplica la configuraci&oacute;n de un servidor.
	 * @param serverConfiguration Configuraci&oacute;n del servidor a duplicar.
	 * @return Copia de la configuraci&oacute;n del servidor.
	 */
	public PfConfigurationsDTO cloneServerConfiguration(PfConfigurationsDTO serverConfiguration) {
		PfConfigurationsDTO newConfiguration = new PfConfigurationsDTO();

		newConfiguration.setCconfiguration(serverConfiguration.getCconfiguration());
		newConfiguration.setCcreated(serverConfiguration.getCcreated());
		newConfiguration.setCmodified(serverConfiguration.getCmodified());
		newConfiguration.setFcreated(serverConfiguration.getFcreated());
		newConfiguration.setFmodified(serverConfiguration.getFmodified());
		newConfiguration.setLmain(serverConfiguration.getLmain());
		newConfiguration.setPfApplications(serverConfiguration.getPfApplications());
		for (Iterator<PfConfigurationsParameterDTO> iterator =
				serverConfiguration.getPfConfigurationsParameters().iterator(); iterator.hasNext();) {
			PfConfigurationsParameterDTO current = (PfConfigurationsParameterDTO) iterator.next();
			newConfiguration.getPfConfigurationsParameters().add(clonePfConfigurationsParameterDTO(current));
		}
		newConfiguration.setPfServer(serverConfiguration.getPfServer());
		newConfiguration.setPrimaryKey(serverConfiguration.getPrimaryKey());
		newConfiguration.setSelected(serverConfiguration.isSelected());
		newConfiguration.setUpdated(serverConfiguration.isUpdated());

		return newConfiguration;
	}

	/**
	 * Marca los servidores de la lista que se pasa como par&aacute;metro como actualizados a false
	 * @param serverList la lista de servidores
	 * @see es.seap.minhap.portafirmas.domain.AbstractBaseDTO#setUpdated(boolean)
	 */
	public void putUpdatedToFalse(List<AbstractBaseDTO> serverList) {
		for (Iterator<AbstractBaseDTO> iterator = serverList.iterator(); iterator.hasNext();) {
			AbstractBaseDTO server = (AbstractBaseDTO) iterator.next();
			server.setUpdated(false);
		}

	}

	/**
	 * M&eacute;todo que devuelve un listado con los par&aacute;metros de servidor disponibles para una
	 * determinada configuraci&oacute;n de servidor. Es decir, devuelve los par&aacute;metros que a&uacute;n no han
	 * recibido un valor para dicha configuraci&oacute;n. 
	 * @param serverConfigurationParameterList Listado de par&aacute;metros de la configuraci&oacute;n de servidor de entrada.
	 * @return Listado de par&aacute;metros disponibles para dicha configuraci&oacute;n.
	 */
	public List<AbstractBaseDTO> queryAvailableConfParameters(
			List<AbstractBaseDTO> serverConfigurationParameterList) {
		List<AbstractBaseDTO> result = new ArrayList<AbstractBaseDTO>();
		List<AbstractBaseDTO> query = new ArrayList<AbstractBaseDTO>();
		// Carga en "query" el listado de todos los par&aacute;metros de servidor definidos en la BD
		query = baseDAO.queryListOneParameter("administration.serverParametersAll", null, null);

		for (Iterator<AbstractBaseDTO> iterator = query.iterator(); iterator.hasNext();) {
			PfParametersDTO parameter = (PfParametersDTO) iterator.next();
			PfParametersDTO current = parameter;
			boolean found = false;
			if (serverConfigurationParameterList != null) {
				for (Iterator<AbstractBaseDTO> iterator2 = serverConfigurationParameterList.iterator();
					iterator2.hasNext() && !found;) {
					PfConfigurationsParameterDTO confParam = (PfConfigurationsParameterDTO) iterator2.next();
					if (parameter.getPrimaryKeyString().equals(
							confParam.getPfParameter().getPrimaryKeyString())) {
						found = true;
					}
				}
			}
			if (!found) {
				result.add(current);
			}
		}
		return result;
	}

	/**
	 * M&eacute;todo que realiza una copia de un par&aacute;metro de configuraci&oacute;n.
	 * @param serverConfigurationParameter Par&aacute;metro a copiar. 
	 * @return Copia del par&aacute;metro de entrada.
	 */
	public PfConfigurationsParameterDTO getCopyOfParameter(
			PfConfigurationsParameterDTO serverConfigurationParameter) {
		PfConfigurationsParameterDTO copy = new PfConfigurationsParameterDTO();

		copy.setCcreated(serverConfigurationParameter.getCcreated());
		copy.setCmodified(serverConfigurationParameter.getCmodified());
		copy.setFcreated(serverConfigurationParameter.getFcreated());
		copy.setFmodified(serverConfigurationParameter.getFmodified());
		copy.setPfConfiguration(serverConfigurationParameter.getPfConfiguration());
		copy.setPfParameter(serverConfigurationParameter.getPfParameter());
		copy.setPrimaryKey(serverConfigurationParameter.getPrimaryKey());
		copy.setSelected(serverConfigurationParameter.isSelected());
		copy.setTvalue(serverConfigurationParameter.getTvalue());
		copy.setUpdated(serverConfigurationParameter.isUpdated());

		return copy;
	}

	/**
	 * M&eacute;todo que elimina una configuraci&oacute;n de un servidor en concreto contenido en una lista.
	 * @param serverList Listado de servidores.
	 * @param server Servidor a buscar.
	 * @param configuration Configuraci&oacute;n a eliminar.
	 * @return Listado de servidores actualizado.
	 */
	public List<AbstractBaseDTO> removeFromList(
			List<AbstractBaseDTO> serverList, PfServersDTO server,
			PfConfigurationsDTO configuration) {
		boolean found = false;
		for (Iterator<AbstractBaseDTO> iterator = serverList.iterator(); iterator.hasNext()	&& !found;) {
			PfServersDTO currentServer = (PfServersDTO) iterator.next();
			if (currentServer.getCserver().equals(server.getCserver())) {
				for (Iterator<PfConfigurationsDTO> iterator2 = currentServer.getPfConfigurations().iterator(); iterator2.hasNext() && !found;) {
					PfConfigurationsDTO currentConf = (PfConfigurationsDTO) iterator2.next();
					if (configuration.getCconfiguration().equals(currentConf.getCconfiguration())) {
						server.getPfConfigurations().remove(configuration);
						currentServer.getPfConfigurations().remove(configuration);
						found = true;
					}

				}
			}
		}
		return serverList;
	}
	
	@Transactional
	public void saveServer (PfServersDTO serverDTO) {
		boolean lMainCopy = serverDTO.getLmain();
		// Si va a ser el servidor por defecto, nos aseguramos de que ningún otro lo es
		if(serverDTO.getLmain()) {
			// Se obtienen las configuraciones del servidor
			List<AbstractBaseDTO> serversList = queryList();
			// Se actualizan para que no sean configuración por defecto
			for (Iterator<AbstractBaseDTO> it = serversList.iterator(); it.hasNext();) {
				PfServersDTO server = (PfServersDTO) it.next();
				server.setLmain(false);
				baseDAO.insertOrUpdate(server);
			}
		}
		// Hibernate lo puso poner a falso en el bucle. Se restituye con la copia
		serverDTO.setLmain(lMainCopy);
		baseDAO.insertOrUpdate(serverDTO);
	}
	
	@Transactional
	public void deleteServer (PfServersDTO serverDTO) {
		// Primero se borran las relaciones configuración que hacen referencia al servidor ..
		List<AbstractBaseDTO> serverConfigList = 
				serverConfigBO.queryConfigurationsList(Util.vacioSiNulo(serverDTO.getPrimaryKeyString()));
		for (AbstractBaseDTO serverConfigDTO : serverConfigList) {
			serverConfigBO.deleteConfigurationServer((PfConfigurationsDTO)serverConfigDTO);
		}
		// .. y luego la configuración.
		baseDAO.delete(serverDTO);
	}
	
	
	/**
	 * Valida las reglas de negocio que se tienen que cumplir para modificar un servidor o crear uno nuevo
	 * @param serverDTO
	 * @param errors
	 */
	public void validateServer (PfServersDTO serverDTO, ArrayList<String> errors) {
		PfServersDTO serverWithCodeDTO = serverByCode (serverDTO.getCserver());
		// Si el código ya existe en base de datos..
		if (serverWithCodeDTO != null) {
			// Se avisa, si es una alta o, siendo una modificación, el encontrado NO es el que estamos modificando
			if  (serverDTO.getPrimaryKey() == null
					|| !serverDTO.getPrimaryKey().equals(serverWithCodeDTO.getPrimaryKey())) {
				errors.add(String.format(messages.getProperty("existsCServer"), serverDTO.getCserver()));
			}
		}
		// Debe seleccionar un servidor por defecto
		if(!existsMainConfiguration(serverDTO)) {
			errors.add(messages.getProperty("hasNoDefaultServer"));
		}
	}
	
	/**
	 * Devuelve verdadero si ya existe un servidor por defecto
	 * @param serverDTO
	 * @return
	 */
	private boolean existsMainConfiguration(PfServersDTO serverDTO) {
		boolean exists = false;
		// Si no va a ser el servidor por defecto, nos aseguramos de que hay alguno que lo es
		if(!serverDTO.getLmain()) {
			// Se obtienen los servidores
			List<AbstractBaseDTO> serversList = queryList();
			// Se recorren para ver si alguno tiene la configuración por defecto
			for (Iterator<AbstractBaseDTO> it = serversList.iterator(); it.hasNext();) {
				PfServersDTO server = (PfServersDTO) it.next();
				if(server.getLmain()) {
					exists = true;
				}
			}
		} else {
			exists = true;
		}
		return exists;
	}

	/**
	 * Valida reglas de negocio a cumplir para poder borrar un servidor.
	 * @param serverDTO
	 * @param errors
	 */
	public void validateDeleteServer (PfServersDTO serverDTO, ArrayList<String> errors) {
		// Si tiene aplicaciones o configuraciones asociadas, no se puede eliminar el servidor
		if (hasApplications(serverDTO)) {
			errors.add(messages.getProperty("errorHasApplications"));
		}
		// El servidor por defecto no puede ser eliminado
		if(serverDTO.getLmain()) {
			errors.add(messages.getProperty("errorDefaultServer"));
		}
	}
	
	/**
	 * Obtiene un servidor por su código.
	 * @param code
	 * @return
	 */
	public PfServersDTO serverByCode (String code) {
		return (PfServersDTO) baseDAO.queryElementOneParameter("administration.serverAdmCodeQuery", "cServer", code);
	}
		
}

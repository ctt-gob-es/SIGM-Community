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

import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfApplicationsDTO;
import es.seap.minhap.portafirmas.domain.PfApplicationsMetadataDTO;
import es.seap.minhap.portafirmas.domain.PfApplicationsParameterDTO;
import es.seap.minhap.portafirmas.domain.PfConfigurationsDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentTypesDTO;
import es.seap.minhap.portafirmas.domain.PfParametersDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.utils.ListComparador;
import es.seap.minhap.portafirmas.utils.metadata.MetadataConverter;
import es.seap.minhap.portafirmas.web.beans.DocumentEni;
import es.seap.minhap.portafirmas.ws.advice.client.ClientManager;

/**
 * 
 * @author daniel.palacios
 *
 */
@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class ApplicationAdmBO  {

	@Autowired
	private BaseDAO baseDAO;
	
	@Autowired
	private ClientManager clientManager;
	
	@Resource(name = "messageProperties")
	private Properties messages;
	
	@Autowired 
	private MetadataConverter metadataConverter;

	/**
	 * Devuelve la lista de aplicaciones de la bbdd ordenadas de forma hierarchy
	 * @return el arbol de aplicaciones de la bbdd
	 */
	public List<AbstractBaseDTO> queryListByHierarchy() {
		List<AbstractBaseDTO> applicationList = baseDAO.queryListMoreParameters(
		"administration.applicationAdmAll", null);
		ListComparador comp = new ListComparador("hierarchy", 1);
		Collections.sort(applicationList, comp);
		return applicationList;
	}
	@Transactional(readOnly=false)
	public List<AbstractBaseDTO> queryList() {
		return baseDAO.queryListMoreParameters("administration.applicationList", null);
	}
	/**
	 * Devuelve el arbol de aplicaciones que no tengan el c&oacute;digo 'PFIRMA' en bbdd
	 * @return el arbol de aplicaciones de la bbdd
	 * @see es.seap.minhap.portafirmas.utils.Util#loadApplicationsAdmTree(List, String)
	 */
	// TODO: Cambiar para adaptarlo a Spring MVC
//	public TreeNode<String> queryApplicationTree() {
//		List<AbstractBaseDTO> applications = baseDAO.queryListMoreParameters(
//				"administration.applicationAdmTree", null);
//		return Util.getInstance().loadApplicationsAdmTree(applications, messages.getProperty("noParent"));
//	}
	/**
	 * Recupera la lista de aplicaciones parametro de la aplicaci&oacute;n 
	 * que pasamos como par&aacute;metro
	 * @param application la aplicaci&oacute;n
	 * @return la lista de aplicaciones parametro
	 */
	public List<AbstractBaseDTO> queryParameterList(
			PfApplicationsDTO application) {
		Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("application", application);
		return baseDAO.queryListMoreParameters(
				"administration.applicationParametersAdmAll", queryParams);
	}
	/**
	 * Obtiene la lista de par&aacute;metros excluyendo los que ya pertenecen a la aplicaci&oacute;n
	 * @param applicationList la lista de par&aacute;metros de aplicaci&oacute;n
	 * @return la lista de par&aacute;metros que no pertencen a la aplicaci&oacute;n
	 * @see #removeExistingParameters(List, List)
	 */
	public List<AbstractBaseDTO> queryApplicationParameterSelectableList(
			List<AbstractBaseDTO> applicationList) {
		List<AbstractBaseDTO> paramList;
		//Obtiene la lista de todos los par&aacute;metros de aplicaci&oacute;n
		paramList = baseDAO.queryListMoreParameters(
				"administration.applicationParametersAll", null);
		// remove existing parameters in application parameters listappBO
		if (applicationList != null) {
			paramList = removeExistingParameters(paramList, applicationList);
		}
		return paramList;
	}
	/**
	 * Borra de la lista de par&aacute;metros que pasamos los que tienen el mismo c&oacute;digo de parametro
	 * que los de la lista de par&aacute;metros de aplicaci&oacute;n
	 * @param paramList la lista de par&aacute;metros
	 * @param applicationList la lista de par&aacute;metros de aplicaci&oacute;n
	 * @return la lista de par&aacute;metros con los par&aacute;metros excluidos
	 */
	private List<AbstractBaseDTO> removeExistingParameters(
			List<AbstractBaseDTO> paramList,
			List<AbstractBaseDTO> applicationList) {
		List<AbstractBaseDTO> ret = new ArrayList<AbstractBaseDTO>();
		ret.addAll(paramList);
		Iterator<AbstractBaseDTO> paramIt = paramList.iterator();
		PfParametersDTO parameterAux;
		PfApplicationsParameterDTO applicationParameterAux;
		Iterator<AbstractBaseDTO> appParamIt;
		while (paramIt.hasNext()) {
			parameterAux = (PfParametersDTO) paramIt.next();
			appParamIt = applicationList.iterator();
			while (appParamIt.hasNext()) {
				applicationParameterAux = (PfApplicationsParameterDTO) appParamIt.next();
				if (parameterAux != null
						&& parameterAux.getCparameter() != null
						&& applicationParameterAux != null
						&& applicationParameterAux.getPfParameter() != null
						&& parameterAux.getCparameter().equals(
								applicationParameterAux.getPfParameter()
										.getCparameter())) {
					ret.remove(parameterAux);
				}
			}
		}
		return ret;
	}
	/**
	 * Obtiene una lista con los tipos de documento de la aplicaci&oacute;n que pasamos como par&aacute;metro
	 * @param application la aplicaci&oacute;n
	 * @return la lista de tipos de documento de la aplicaci&oacute;n
	 */
	public List<AbstractBaseDTO> queryApplicationDocumentTypeList(
			PfApplicationsDTO application) {
		Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("application", application);
		return baseDAO.queryListMoreParameters(
				"administration.applicationDocumentTypesAdm", queryParams);
	}
	/**
	 * Recupera el tipo de documento de la aplicaci&oacute;n con el codigo 
	 * de tipo que pasamos como par&aacute;metro
	 * @param application la aplicaci&oacute;n 
	 * @param documentTypeId codigo tipo de documento
	 * @return el objeto de tipo de documento
	 */
	public PfDocumentTypesDTO queryApplicationDocumentType(
			PfApplicationsDTO application, String documentTypeId) {
		Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("application", application);
		queryParams.put("documentTypeId", documentTypeId);
		return (PfDocumentTypesDTO) baseDAO.queryElementMoreParameters(
				"administration.applicationDocumentTypeAdm", queryParams);
	}
	
	/*
	 * OPERATIONS
	 */
	/**
	 * Crea una nueva aplicaci&oacute;n y pone el servidor de configuraci&oacute;n por defecto para la nueva aplicaci&oacute;n
	 * @param configurationListDefault la lista de configuraciones de la que se coger&aacute;
	 * el primer elemento
	 * @return la nueva aplicaci&oacute;n con la configuraci&oacute;n del primer elemento asignada
	 */
	public PfApplicationsDTO createDefaultApplication(
			List<AbstractBaseDTO> configurationListDefault) {
		PfApplicationsDTO app = new PfApplicationsDTO();
		app.setPfConfiguration((PfConfigurationsDTO) configurationListDefault
				.get(0));
		return app;
	}
	/**
	 * Guarda la lista de aplicaciones, borra la lista de aplicaciones a borrar,
	 * guarda la lista de par&aacute;metros de aplicaciones,borra la lista de par&aacute;metros 
	 * de aplicaciones a borrar de la bbdd
	 * @param applicationList la lista de aplicaciones
	 * @param applicationDeleteList la lista de aplicaciones a borrar
	 * @param applicationParameterListList la lista de par&aacute;metros de aplicaciones
	 * @param applicationDeleteParameterListList la lista de par&aacute;metros de aplicaciones a borrar
	 * @see #saveApplication(PfApplicationsDTO, List, List)
	 */
	@Transactional(readOnly = false)
	public void saveApplicationList(List<AbstractBaseDTO> applicationList,
			List<AbstractBaseDTO> applicationDeleteList,
			List<List<AbstractBaseDTO>> applicationParameterListList,
			List<List<AbstractBaseDTO>> applicationDeleteParameterListList) {
		// Cascade deletion. If application contains parameters, it will be
		// removed too
		if (applicationList != null && !applicationList.isEmpty()) {
			Iterator<AbstractBaseDTO> it = applicationList.iterator();
			int indice = 0;
			while (it.hasNext()) {
				// TODO: incluir control de listas de parametros cuando se
				// refleje en la aplicacion
				PfApplicationsDTO application = (PfApplicationsDTO) it.next();
				if (application.isUpdated()) {
					//A&ntilde;adimos la lista de par&aacute;metros
					List<AbstractBaseDTO> applicationParametersList = applicationParameterListList.get(indice);
					List<AbstractBaseDTO> applicationParametersDeleteList = applicationDeleteParameterListList.get(indice);
					saveApplication(application, applicationParametersList, applicationParametersDeleteList);
					application.setUpdated(false);
				}
				indice++;
			}

		}

		if (applicationDeleteList != null && !applicationDeleteList.isEmpty()) {
			Iterator<AbstractBaseDTO> it = applicationDeleteList.iterator();
			while (it.hasNext()) {
				PfApplicationsDTO app = (PfApplicationsDTO) it.next();
				// Get application parameters
				Map<String, Object> queryParams = new HashMap<String, Object>();
				queryParams.put("cApplication", app.getCapplication());
				List<AbstractBaseDTO> parameterList = baseDAO
						.queryListMoreParameters(
								"administration.applicationAdmParameterList",
								queryParams);
				// Delete application parameters
				if (parameterList != null && !parameterList.isEmpty()) {
					baseDAO.deleteList(parameterList);
				}
				baseDAO.delete(app);
			}
		}
	}
	
	/*
	 * AUXILIARY METHODS
	 */
	/**
	 * Guarda la aplicaci&oacute;n, la lista de par&aacute;metros de la aplicaci&oacute;n y borra
	 * la lista de par&aacute;metros de la aplicaci&oacute;n a borrar en bbdd
	 * @param application la aplicaci&oacute;n
	 * @param applicationParametersList la lista de par&aacute;metros de la aplicaci&oacute;n
	 * @param applicationParametersDeleteList la lista de par&aacute;metros borrados de la aplicaci&oacute;n
	 */
	@Transactional(readOnly = false)
	private void saveApplication(PfApplicationsDTO application,
			List<AbstractBaseDTO> applicationParametersList,
			List<AbstractBaseDTO> applicationParametersDeleteList) {
		//Guarda la aplicaci&oacute;n
		baseDAO.insertOrUpdate(application);
		if (applicationParametersList != null && !applicationParametersList.isEmpty()) {
			for (AbstractBaseDTO abstratParameter: applicationParametersList) {
				PfApplicationsParameterDTO param = (PfApplicationsParameterDTO) abstratParameter;
				param.setPfApplication(application);
				application.getPfApplicationsParameters().add(param);
			}
			//Guarda la lista de par&aacute;metros
			baseDAO.insertOrUpdateList(applicationParametersList);			
		}
		if (applicationParametersDeleteList != null && !applicationParametersDeleteList.isEmpty()) {
			for (AbstractBaseDTO abstratParameter: applicationParametersDeleteList) {
				PfApplicationsParameterDTO param = (PfApplicationsParameterDTO) abstratParameter;
				param.setPfApplication(application);
				application.getPfApplicationsParameters().remove(param);
			}
			//Borra la lista de par&aacute;metros
			baseDAO.deleteList(applicationParametersDeleteList);
		}
		baseDAO.insertOrUpdate(application);
	}
	
	/**
	 * Guarda la aplicaci&oacute;n, la lista de par&aacute;metros de la aplicaci&oacute;n y borra
	 * la lista de par&aacute;metros de la aplicaci&oacute;n a borrar en bbdd
	 * @param application la aplicaci&oacute;n
	 * @param applicationParametersList la lista de par&aacute;metros de la aplicaci&oacute;n
	 */
	@Transactional(readOnly = false)
	public void saveApp(PfApplicationsDTO application,
			List<AbstractBaseDTO> parametersList) {
		//Guarda la aplicaci&oacute;n
		baseDAO.insertOrUpdate(application);
		if (parametersList != null && !parametersList.isEmpty()) {
			for (AbstractBaseDTO abstratParameter: parametersList) {
				PfApplicationsParameterDTO param = (PfApplicationsParameterDTO) abstratParameter;
				param.setPfApplication(application);
				application.getPfApplicationsParameters().add(param);
			}
			//Guarda la lista de par&aacute;metros
			baseDAO.insertOrUpdateList(parametersList);			
		}
	}
	
	
	/**
	 * Obtiene el objeto de aplicaciones con la pk que hemos pasado como par&aacute;metro
	 * @param cApplication el c&oacute;digo de la aplicaci&oacute;n
	 * @return el objeto de aplicaciones con la pk que hemos apsado como par&aacute;metro
	 */
	public AbstractBaseDTO applicationCodeQuery(String cApplication) {
		AbstractBaseDTO ret = null;
		if (cApplication != null && !cApplication.equals(messages.getProperty("noParent"))) {
			Map<String, Object> queryParams = new HashMap<String, Object>();
			queryParams.put("cApplication", cApplication);
			ret = baseDAO.queryElementMoreParameters(
					"administration.applicationAdmCodeQuery", queryParams);
		}
		return ret;
	}
	
	/**
	 * Obtiene el objeto de aplicaciones con la pk que hemos pasado como par&aacute;metro
	 * @param cApplication el c&oacute;digo de la aplicaci&oacute;n
	 * @return el objeto de aplicaciones con la pk que hemos apsado como par&aacute;metro
	 */
	public AbstractBaseDTO applicationPkQuery(Long primaryKey) {
		AbstractBaseDTO ret = null;
		if (primaryKey != null && !primaryKey.toString().equals(messages.getProperty("noParent"))) {
			Map<String, Object> queryParams = new HashMap<>();
			queryParams.put("primaryKey", primaryKey);
			ret = baseDAO.queryElementMoreParameters(
					"administration.applicationAdmPkQuery", queryParams);
		}
		return ret;
	}
	
	@Transactional(readOnly=false)
	public AbstractBaseDTO applicationPkQueryTrans (Long primaryKey) {
		return applicationPkQuery (primaryKey);
	}
	
	/**
	 * Obtiene el objeto de configuraciones con la primary key que pasamos como par&aacute;metro
	 * @param configPk la primary key de configuraciones
	 * @return el objeto de configuraciones
	 */
	public AbstractBaseDTO configurationPkQuery(String configPk) {
		AbstractBaseDTO ret = null;
		if (configPk != null && !configPk.equals("")) {
			Long pk = Long.parseLong(configPk);
			Map<String, Object> queryParams = new HashMap<String, Object>();
			queryParams.put("configPk", pk);
			ret = baseDAO.queryElementMoreParameters(
					"administration.configurationAdmPkQuery", queryParams);
		}
		return ret;
	}


	/*
	 * VALIDATIONS
	 */
	/**
	 * Devuelve si la aplicaci&oacute;n pasada como par&aacute;metro tiene alguna petici&oacute;n asociada
	 * @param baseDTO la aplicaci&oacute;n
	 * @return true si la aplicaci&oacute;n pasada como par&aacute;metro tiene alguna petici&oacute;n asociada,
	 * false en caso contrario
	 */
	public boolean hasRequest(AbstractBaseDTO baseDTO) {
		boolean ret = false;
		if (baseDTO != null && baseDTO.getPrimaryKey() != null) {
			Map<String, Object> queryParams = new HashMap<String, Object>();
			queryParams.put("application", baseDTO);
			Long queryResult = baseDAO.queryCount(
					"administration.applicationAdmHasRequest", queryParams);
			if (queryResult != null && queryResult.longValue() > 0) {
				ret = true;
			}
		}
		return ret;
	}
	/**
	 * Devuelve si la aplicaci&oacute;n que pasamos como par&aacute;meto es
	 * padre de otra aplicaci&oacute;n
	 * @param baseDTO la aplicaci&oacute;n
	 * @return true si la aplicaci&oacute;n pasada como par&aacute;metro es padre de otra,
	 * false en caso contrario
	 */
	public boolean isParent(AbstractBaseDTO baseDTO) {
		boolean ret = false;
		if (baseDTO != null && baseDTO.getPrimaryKey() != null) {
			Map<String, Object> queryParams = new HashMap<String, Object>();
			queryParams.put("application", baseDTO);
			Long queryResult = baseDAO.queryCount(
					"administration.applicationAdmIsParent", queryParams);
			if (queryResult != null && queryResult.longValue() > 0)
				ret = true;
		}
		return ret;
	}
	/**
	 * Examina si la aplicaci&oacute;n que pasamos como par&aacute;metro est&aacute; en la lista
	 * @param applicationList la lista de aplicaciones
	 * @param application la aplicaci&oacute;n
	 * @return true si la aplicaci&oacute;n est&aacute; en la lista de aplicaciones, false
	 * en caso contrario
	 */
	public boolean existsCApplication(List<AbstractBaseDTO> applicationList,
			PfApplicationsDTO application) {
		boolean ret = false;
		PfApplicationsDTO auxApplication;
		if (applicationList != null && !applicationList.isEmpty()) {
			Iterator<AbstractBaseDTO> it = applicationList.iterator();
			while (it.hasNext() && ret == false) {
				auxApplication = (PfApplicationsDTO) it.next();
				// Not the same row and different code
				if (auxApplication.getCapplication().toUpperCase().equals(
						application.getCapplication().toUpperCase())
						&& !application.equals(auxApplication)) {
					ret = true;
				}
			}
		}
		return ret;
	}
	/**application
	 * Devuelve si una aplicaci&oacute;n tiene aplicaciones par&aacute;metro
	 * @param appSelected codigo de la aplicaci&oacute;n
	 * @return true si la aplicaci&oacute;n pasada como par&aacute;metro tiene aplicaciones par&aacute;metro,
	 * false en caso contrario
	 */
	public boolean hasParameters(String appSelected) {
		boolean ret = false;
		if (appSelected != null && !appSelected.equals(messages.getProperty("noParent"))) {
			Map<String, Object> queryParams = new HashMap<String, Object>();
			queryParams.put("cApplication", appSelected);
			Long queryResult = baseDAO.queryCount(
					"administration.applicationAdmHasParametersQuery",
					queryParams);
			if (queryResult != null && queryResult.longValue() > 0) {
				ret = true;
			}
		}
		return ret;
	}

	public boolean hasDocumentTypes(String appSelected) {
		boolean ret = false;
		return ret;
	}
	/**
	 * Hace una copia del objeto aplicaciones par&aacute;metro que pasamos
	 * @param applicationConfigurationParameter el objeto aplicaciones par&aacute;metro
	 * @return la copia del objeto aplicaciones par&aacute;metro
	 */
	public PfApplicationsParameterDTO getCopyOfParameter(PfApplicationsParameterDTO applicationConfigurationParameter) {
		PfApplicationsParameterDTO copy = new PfApplicationsParameterDTO();
		copy.setCcreated(applicationConfigurationParameter.getCcreated());
		copy.setCmodified(applicationConfigurationParameter.getCmodified());
		copy.setFcreated(applicationConfigurationParameter.getFcreated());
		copy.setFmodified(applicationConfigurationParameter.getFmodified());
		copy.setPfApplication(applicationConfigurationParameter.getPfApplication());
		copy.setPfParameter(applicationConfigurationParameter.getPfParameter());
		copy.setPrimaryKey(applicationConfigurationParameter.getPrimaryKey());
		copy.setSelected(applicationConfigurationParameter.isSelected());
		copy.setTvalue(applicationConfigurationParameter.getTvalue());
		copy.setUpdated(applicationConfigurationParameter.isUpdated());
		return copy;
	}
	

	@Transactional(readOnly=false)
	public List<AbstractBaseDTO> prepareApp() {
		List<AbstractBaseDTO> resultado = baseDAO.queryListOneParameter("administration.findAllConfig", "todos", 1);
		return resultado;
	}
	
	@Transactional(readOnly=false)
	public List<AbstractBaseDTO> obtenerListaAppsPadre(PfApplicationsDTO appDTO) {
		List<AbstractBaseDTO> resultado = baseDAO.queryListOneParameter("administration.findAllApp", "todos", 1);
		resultado.remove(appDTO);
		return resultado;
	}
	
	@Transactional(readOnly = false)
	public void saveApplicationAndParameters(PfApplicationsDTO appDTO, List<PfApplicationsParameterDTO> pfApplParameterDTOArray){
		baseDAO.insertOrUpdate(appDTO);
		saveParameters (pfApplParameterDTOArray, appDTO);
	}
	
	@Transactional(readOnly=false)
	public void validateApplication (PfApplicationsDTO pfApplicationDTO, List<String> errors) {
		PfApplicationsDTO appBBDD = (PfApplicationsDTO) applicationCodeQuery(pfApplicationDTO.getCapplication());
		// Si el código ya existe en base de datos..
		if (appBBDD != null) {
			// Se avisa, si es una alta o, siendo una modificación, el encontrado NO es el que estamos modificando
			if  (pfApplicationDTO.getPrimaryKey() == null
					|| !pfApplicationDTO.getPrimaryKey().equals(appBBDD.getPrimaryKey())) {
				errors.add(messages.getProperty("existsCApplication"));
			}
		}
	}
	
	/**
	 * Método que borra las antiguas relaciones aplicación-parametro y da de alta a las nuevas
	 * @param pfApplParameterDTOArray
	 */
	//@Transactional(readOnly = false)
	public void saveParameters(List<PfApplicationsParameterDTO> pfApplParameterDTOArray, PfApplicationsDTO pfApplicationDTO) {
		if(!pfApplParameterDTOArray.isEmpty()) {
			for (PfApplicationsParameterDTO appParamDTO : pfApplicationDTO.getPfApplicationsParameters()) {
				baseDAO.delete(appParamDTO);
			}
		}
		// Se guardan las nuevas relaciones aplicación-parámetro
		for (PfApplicationsParameterDTO pfAppParameterDTO : pfApplParameterDTOArray) {
			pfAppParameterDTO.setPfApplication(pfApplicationDTO);
			baseDAO.insertOrUpdate(pfAppParameterDTO);
		}
		// Se resetea la cache correspondiente
		clientManager.clearCache();
	}
	
	@Transactional(readOnly=false)
	public void validateAndDelete (Long pkApplication, ArrayList<String> errors) {
		// Se obtiene la aplicacion a borrar			
		PfApplicationsDTO pfApplicationsDTO = (PfApplicationsDTO) applicationPkQuery(pkApplication);
		
		// Se validan las reglas de negocio
		validateDelete(pfApplicationsDTO, errors);
		if(!errors.isEmpty()) {
			return;
		}
		
		// Se realiza el borrado
		deleteApplication(pfApplicationsDTO);	
	}
	
	/**
	 * Validación de las reglas de negocio
	 * @param pfApplicationsDTO
	 * @param errors
	 */
	public void validateDelete(PfApplicationsDTO pfApplicationsDTO, ArrayList<String> errors) {
		if(hasRequest(pfApplicationsDTO)) {
			errors.add(messages.getProperty("errorHasRequest"));
		}
		if(isParent(pfApplicationsDTO)) {
			errors.add(messages.getProperty("errorIsParent"));
		}
	}
	
	/**
	 * Borra la aplicación y sus parámetros
	 * @param pfApplicationsDTO
	 */
	@Transactional(readOnly = false)
	public void deleteApplication(PfApplicationsDTO pfApplicationsDTO) {
		Set<PfApplicationsParameterDTO> setPfAppParamDTO = pfApplicationsDTO.getPfApplicationsParameters();
		Iterator<PfApplicationsParameterDTO> it =  setPfAppParamDTO.iterator();
		while (it.hasNext()) {
			baseDAO.delete(it.next());
		}
		
		Set<PfApplicationsMetadataDTO> setPfAppMetaDTO = pfApplicationsDTO.getPfApplicationsMetadatas();
		Iterator<PfApplicationsMetadataDTO> itMeta =  setPfAppMetaDTO.iterator();
		while (itMeta.hasNext()) {
			baseDAO.delete(itMeta.next());
		}
		baseDAO.delete(pfApplicationsDTO);
	}
	
	
	@Transactional(readOnly=false)
	public DocumentEni getDocumentEniByApp(PfApplicationsDTO pfApplicationsDTO) {
		List<PfApplicationsMetadataDTO> appMetadataDtoList = baseDAO.queryListOneParameter("administration.applicationMetadata", "application", pfApplicationsDTO);
		
		DocumentEni documentEni = metadataConverter.applicationsMetadataDTOToDocumentEni(appMetadataDtoList);
		
		return documentEni;
	}
	
	
	@Transactional(readOnly = false)
	public void saveMetadata(PfApplicationsDTO pfApplicationsDTO, DocumentEni documentEni, PfUsersDTO user){
		List<PfApplicationsMetadataDTO> applicationsMetadataDTOList = 
				metadataConverter.documentEniToApplicationsMetadataDTO(documentEni, pfApplicationsDTO, user);
		
		// Elimnamos los metadatos de la bbdd
		/*List<PfApplicationsMetadataDTO> metadataDeleteList = 
				metadataValidator.metadataDeleted(pfApplicationsDTO.getPrimaryKeyString(), applicationsMetadataDTOList);*/
		List<PfApplicationsMetadataDTO> appMetadataDtoList = baseDAO.queryListOneParameter("administration.applicationMetadata", "application", pfApplicationsDTO);
		deleteMetadata(appMetadataDtoList); 
		
		// Insertamos los nuevos metadatos
		Iterator<PfApplicationsMetadataDTO> itApplicationsMetadata =  applicationsMetadataDTOList.iterator();
		while (itApplicationsMetadata.hasNext()) {
			PfApplicationsMetadataDTO applicationsMetadata = itApplicationsMetadata.next();
			baseDAO.insertOrUpdate(applicationsMetadata);
		}
		
	}

	/**
	 * Borra la aplicación y sus parámetros
	 * @param pfApplicationsDTO
	 */
	@Transactional(readOnly = false)
	public void deleteMetadata(List<PfApplicationsMetadataDTO> metadataDeleteList) {

		Iterator<PfApplicationsMetadataDTO> it =  metadataDeleteList.iterator();
		while (it.hasNext()) {
			baseDAO.delete(it.next());
		}

	}
	
//	@Transactional(readOnly=false)
//	public PfApplicationsMetadataDTO applicationMetadataById (Long pk) {
//		PfApplicationsMetadataDTO resultado = (PfApplicationsMetadataDTO) baseDAO.queryElementOneParameter("administration.applicationMetadataById", "pk", pk);
//		return resultado;
//	}
	

	
}


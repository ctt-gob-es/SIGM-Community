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

package es.seap.minhap.portafirmas.business;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfActionsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.action.PLSQLActionService;
import es.seap.minhap.portafirmas.utils.action.WebActionService;
import es.seap.minhap.portafirmas.utils.quartz.QuartzInvoker;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class ActionBO {

	@Autowired
	private BaseDAO baseDAO;

	@Autowired
	private ApplicationBO applicationBO;

	@Autowired
	private QuartzInvoker quartzInvoker;

	Logger log = Logger.getLogger(ActionBO.class);
	/**
	 * Ejecuta las acciones relacionadas con las peticiones que pasamos en la lista
	 * @param hashList lista de identificadores de peticiones
	 * @param dni DNI del usuario o c&oacute;digo de cargo
	 * @param state c&oacute;digo de etiqueta
	 * @see #buildWebAction(String, String, PfActionsDTO, String)
	 * @see #buildPLSQLAction(String, String, PfActionsDTO, String)
	 * @see es.seap.minhap.portafirmas.utils.quartz.QuartzInvoker#scheduleJobInterval(JobDataMap, Class, String, String, int, int)
	 */
	public void executeActions(List<AbstractBaseDTO> hashList, String dni,
			String state) {
		log.info("executeActions init");

		try {

			if (hashList != null && !hashList.isEmpty()) {
				PfRequestsDTO[] list = new PfRequestsDTO[hashList.size()];
				list = hashList.toArray(list);
								
				Map<String, Object[]> parametersList = new HashMap<String, Object[]>();
				parametersList.put("reqs", list);
				
				Map<String, Object> parametersSimple = new HashMap<String, Object>();
				parametersSimple.put("tag", state);
				
				//recupera la lista de accciones por petici&oacute;n
				List<AbstractBaseDTO> actionsRequestList = baseDAO
						.queryListMoreParametersListComplex(
								"request.requestsActionsByRequest",
								parametersList, parametersSimple);

				//recupera la lista de accciones por documento
				List<AbstractBaseDTO> actionsDocumentList = baseDAO
						.queryListMoreParametersListComplex(
								"request.requestsActionsByDocument",
								parametersList, parametersSimple);
				
				List<AbstractBaseDTO> actionsList = new ArrayList<AbstractBaseDTO>();
				//une las acciones encontradas en una lista
				if (actionsDocumentList != null
						&& !actionsDocumentList.isEmpty()) {
					actionsList.addAll(actionsDocumentList);
				}
				if (actionsRequestList != null && !actionsRequestList.isEmpty()) {
					actionsList.addAll(actionsRequestList);
				}
				log.debug("Running thread");

				// check proxy
				//obtiene un mapa con los parametros de configuraci&oacute;n de tipo 'PROXY'
				Map<String, String> proxyParams = applicationBO
						.queryProxyParameters();

				PfActionsDTO actionDTO = null;
				JobDataMap jobDataMap = null;
				Class<? extends Job> jobClass = null;
				String action = null;
				String nameJob = null;

				for (AbstractBaseDTO abstractBaseDTO : actionsList) {
					actionDTO = (PfActionsDTO) abstractBaseDTO;
					//Obtiene la acci&oacute;n a ejecutar
					action = actionDTO.getTaction();
					jobDataMap = new JobDataMap();
					//Si la acci&oacute;n es de tipo web
					if (Constants.C_ACTIONS_WEB.equals(actionDTO.getCtype())) {
						log.debug("Executing web action");
						if (Constants.C_YES.equals(proxyParams
								.get(Constants.PROXY))) {
							jobDataMap.putAll(proxyParams);
							// jobDataMap.put(Constants.PROXY_SERVER,
							// proxyParams.get(Constants.PROXY_SERVER));
							// jobDataMap.put(Constants.PROXY_PORT,
							// proxyParams.get(Constants.PROXY_PORT));
							// jobDataMap.put(Constants.PROXY_USER,
							// proxyParams.get(Constants.PROXY_USER));
							// jobDataMap.put(Constants.PROXY_PASSWORD,
							// proxyParams.get(Constants.PROXY_PASSWORD));
						}
						//construye la acci&oacute;n a ejecutar
						action = buildWebAction(dni, state, actionDTO, action);
						jobClass = WebActionService.class;
						nameJob = "ACTION_WEB_" + System.currentTimeMillis();
					//si la acci&oacute;n no es de tipo web
					} else {
						log.debug("Executing plsql action");
						//construye la acci&oacute;n a ejecutar
						action = buildPLSQLAction(dni, state, actionDTO, action);
						jobClass = PLSQLActionService.class;
						nameJob = "ACTION_PLSQL_" + System.currentTimeMillis();
					}

					jobDataMap.put(Constants.JOB_ACTION, action);
					quartzInvoker.scheduleJobInterval(jobDataMap, jobClass,
							nameJob, Constants.JOB_ACTION, 5, 30000);

				}
			}
		} catch (Exception e) {
			log.error("Error in execute actions 1: " + e.getMessage());
			log.error("Error in execute actions 1.2: " + e.getLocalizedMessage());
			log.error("Error in execute actions 1.3: ", e);
		}

		log.info("executeActions init");
	}
	/**
	 * Construye la acci&oacute;n a ejecutar agreg&aacute;ndole el dni, el estado y los c&oacute;digos
	 * hash de los documentos y la petici&oacute;n que contiene el par&aacute;metro actionDTO
	 * @param dni DNI del usuario o c&oacute;digo de cargo
	 * @param state c&oacute;digo de etiqueta
	 * @param actionDTO acci&oacute;n
	 * @param action la acci&oacute;n a ejecutar
	 * @return la acci&oacute;n a ejecutar con los par&aacute;metros a&ntilde;adidos
	 */
	private String buildPLSQLAction(String dni, String state,
			PfActionsDTO actionDTO, String action) {
		action = action.replace(Constants.ACTION_PARAM_POINTS
				+ Constants.ACTION_PARAM_IDENTIFIER,
				Constants.ACTION_PARAM_QUOTE + dni
						+ Constants.ACTION_PARAM_QUOTE);
		action = action.replace(Constants.ACTION_PARAM_POINTS
				+ Constants.ACTION_PARAM_STATE, Constants.ACTION_PARAM_QUOTE
				+ state + Constants.ACTION_PARAM_QUOTE);
		String requestHash = ((PfActionsDTO) actionDTO).getPfDocument()
				.getPfRequest().getChash();
		action = action.replace(Constants.ACTION_PARAM_POINTS
				+ Constants.ACTION_PARAM_REQ_HASH, Constants.ACTION_PARAM_QUOTE
				+ requestHash + Constants.ACTION_PARAM_QUOTE);
		String documentHash = ((PfActionsDTO) actionDTO).getPfDocument()
				.getChash();
		action = action.replace(Constants.ACTION_PARAM_POINTS
				+ Constants.ACTION_PARAM_DOC_HASH, Constants.ACTION_PARAM_QUOTE
				+ documentHash + Constants.ACTION_PARAM_QUOTE);

		return action;
	}
	/**
	 * Construye la acci&oacute;n a ejecutar agreg&aacute;ndole el dni, el estado y los c&oacute;digos
	 * hash de los documentos y la petici&oacute;n que contiene el par&aacute;metro actionDTO
	 * @param dni DNI del usuario o c&oacute;digo de cargo
	 * @param state c&oacute;digo de etiqueta
	 * @param actionDTO acci&oacute;n
	 * @param action la acci&oacute;n a ejecutar
	 * @return la acci&oacute;n a ejecutar con los par&aacute;metros a&ntilde;adidos
	 */
	private String buildWebAction(String dni, String state,
			PfActionsDTO actionDTO, String action) {
		//a&ntilde;adimos el dni a la acci&oacute;n
		action = action.replace(Constants.ACTION_PARAM_IDENTIFIER,
				Constants.ACTION_PARAM_IDENTIFIER
						+ Constants.ACTION_PARAM_EQUAL + dni);
		//a&ntilde;adimos el estado a la acci&oacute;n
		action = action.replace(Constants.ACTION_PARAM_STATE,
				Constants.ACTION_PARAM_STATE + Constants.ACTION_PARAM_EQUAL
						+ state);
		String requestHash = null;
		//si la acci&oacute;n tiene documentos asociados
		if (((PfActionsDTO) actionDTO).getPfDocument() != null) {
			//obtiene el codigo hash de la petici&oacute;n asociada al documento
			requestHash = ((PfActionsDTO) actionDTO).getPfDocument()
					.getPfRequest().getChash();
			//obtiene el codigo hash del documento
			String documentHash = ((PfActionsDTO) actionDTO).getPfDocument()
					.getChash();
			//a&ntilde;adimos el codigo hash del documento a la acci&oacute;n
			action = action.replace(Constants.ACTION_PARAM_DOC_HASH,
					Constants.ACTION_PARAM_DOC_HASH
							+ Constants.ACTION_PARAM_EQUAL + documentHash);
		// si no tiene documentos asociados 
		} else {
			//obtiene el codigo hash de la petici&oacute;n
			requestHash = ((PfActionsDTO) actionDTO).getPfRequest().getChash();
		}
		action = action.replace(Constants.ACTION_PARAM_REQ_HASH,
				Constants.ACTION_PARAM_REQ_HASH + Constants.ACTION_PARAM_EQUAL
						+ requestHash);

		return action;
	}

}

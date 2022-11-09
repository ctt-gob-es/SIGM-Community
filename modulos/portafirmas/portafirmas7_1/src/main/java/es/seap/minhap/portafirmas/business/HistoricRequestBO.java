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

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfHistoricRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.utils.Constants;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class HistoricRequestBO {


	@Autowired
	private BaseDAO baseDAO;

	@Resource(name = "messageProperties")
	private Properties messages;

	/**
	 * Guarda una petici&oacute;n en bbdd en el hist&oacute;rico de peticiones, esta es creada a partir 
	 * de la petici&oacute;n pasada como par&aacute;metro y del usuario
	 * @param request petici&oacute;n
	 * @param user usuario
	 * @param chistoric c&oacute;digo de texto de hist&oacute;rico
	 * @param isJob si es un job va a true en caso contraio false
	 * @see #createHistoricRequest(PfRequestsDTO, PfUsersDTO, String, boolean)
	 */
	public void saveHistoricRequest(PfRequestsDTO request, PfUsersDTO user,
			String chistoric, boolean isJob) {
		//crea la petici&oacute;n de tipo hist&oacute;rico
		PfHistoricRequestsDTO historicRequestsDTO = createHistoricRequest(
				request, user, chistoric, isJob);
		baseDAO.insertOrUpdate(historicRequestsDTO);
	}
	/**
	 * Crea una petici&oacute;n de tipo hist&oacute;rico a partir de la petici&oacute;n pasada como par&aacute;metro 
	 * y del usuario
	 * @param pfRequest la petici&oacute;n
	 * @param pfUser usuario
	 * @param chistoric c&oacute;digo de texto de hist&oacute;rico
	 * @param isJob si es un job va a true en caso contraio false
	 * @return la petici&oacute;n creada de tipo hist&oacute;rico
	 */
	private PfHistoricRequestsDTO createHistoricRequest(
			PfRequestsDTO pfRequest, PfUsersDTO pfUser, String chistoric,
			boolean isJob) {
		PfHistoricRequestsDTO historicRequestsDTO = new PfHistoricRequestsDTO();
		historicRequestsDTO.setPfRequest(pfRequest);
		if (isJob) {
			historicRequestsDTO.setPfUser(pfUser.getValidJob());
		} else {
			historicRequestsDTO.setPfUser(pfUser);
		}
		historicRequestsDTO
				.setThistoricRequest(Constants.HISTORIC_REQUEST_USER_VAR + "="
						+ pfUser.getFullName()
						+ Constants.HISTORIC_REQUEST_VAR_SEPARATOR);
		historicRequestsDTO.setChistoricRequest(chistoric);
		return historicRequestsDTO;
	}
	/**
	 * Recupera el histórico 
	 * @param requestsDTO la petici&oacute;n actual
	 * @return la lista de peticiones historico del usuario de la petici&oacute;n actual
	 */
	public List<AbstractBaseDTO> getHistoricoByRequestUserValorPet(PfRequestsDTO requestsDTO, PfUsersDTO user, String chistoric) {
		Map<String, Object> params = new HashMap<String, Object> ();
		params.put("usuario", user.getPrimaryKey());
		params.put("request", requestsDTO.getPrimaryKey());
		params.put("valorPeticion", chistoric);
		return baseDAO.queryListMoreParameters("request.historicRequestUserValueRequest", params);
	}
	
	public List<AbstractBaseDTO> getHistoricoByRequestValorPet(PfRequestsDTO requestsDTO, String chistoric) {
		Map<String, Object> params = new HashMap<String, Object> ();
		params.put("request", requestsDTO.getPrimaryKey());
		params.put("valorPeticion", chistoric);
		return baseDAO.queryListMoreParameters("request.historicRequestValueRequest", params);
	}
	
	/**
	 * Recupera el hist&oacute;rico de peticiones del usuario de la petici&oacute;n actual
	 * @param requestsDTO la petici&oacute;n actual
	 * @return la lista de peticiones historico del usuario de la petici&oacute;n actual
	 */
	public List<AbstractBaseDTO> getUsersHistoricRequest(PfRequestsDTO requestsDTO) {
		return baseDAO.queryListOneParameter("request.historicRequestUser", "request", requestsDTO);
	}
	/**
	 * Recupera el hist&oacute;rico de peticiones del usuario de la petici&oacute;n actual
	 * y a&ntilde;ade la descripci&oacute;n con el valor del mensaje a la descripci&oacute;n de peticiones hist&oacute;rica a mostrar.
	 * @param req la petici&oacute;n actual
	 * @return lista de peticiones hit&oacute;ricas del usuario de la petici&oacute;n actual
	 * @see #getUsersHistoricRequest(PfRequestsDTO)
	 * @see #getHistoricJob(PfHistoricRequestsDTO)
	 * @see #getHistoricFullName(PfHistoricRequestsDTO)
	 * @see es.seap.minhap.portafirmas.domain.PfHistoricRequestsDTO#setThistoricRequestShow(String)
	 */
	public List<AbstractBaseDTO> queryHistoricListTranslate(PfRequestsDTO req) {
		//Recupera el hist&oacute;rico de peticiones asociadas a la petici&oacute;n actual
		List<AbstractBaseDTO> userHistoricRequests = getUsersHistoricRequest(req);
		// Iterate over historic requests and set the description with the value
		// of message
		for (AbstractBaseDTO abstractBaseDTO : userHistoricRequests) {
			PfHistoricRequestsDTO historicRequest = (PfHistoricRequestsDTO) abstractBaseDTO;
			String description = "";
			//si es una lectura obtenemos el mensaje de lectura
			if (Constants.C_HISTORIC_REQUEST_READ.equals(historicRequest
					.getChistoricRequest())) {
				description = messages.getProperty(Constants.PROP_HISTORIC_REQUEST_READ);
			//si es una petici&oacute;n de firma
			} else if (Constants.C_HISTORIC_REQUEST_SIGNED
					.equals(historicRequest.getChistoricRequest())) {
				description = messages.getProperty(Constants.PROP_HISTORIC_REQUEST_SIGNED);
			// Si es una petici&oacute;n con visto bueno pasado
			} else if (Constants.C_HISTORIC_REQUEST_PASSED
					.equals(historicRequest.getChistoricRequest())) {
				description = messages.getProperty(Constants.PROP_HISTORIC_REQUEST_PASSED);
				// Si es una petición ha sido validada
			} else if (Constants.C_HISTORIC_REQUEST_VALIDATED
					.equals(historicRequest.getChistoricRequest())) {
				description = messages.getProperty(Constants.PROP_HISTORIC_REQUEST_VALIDATED);
			//Si es una petici&oacute;n de devuelto
			} else if (Constants.C_HISTORIC_REQUEST_REJECTED
					.equals(historicRequest.getChistoricRequest())) {
				description = messages.getProperty(Constants.PROP_HISTORIC_REQUEST_REJECTED);
			//si es una petición retirada
			} else if (Constants.C_HISTORIC_REQUEST_REMOVED
					.equals(historicRequest.getChistoricRequest())) {
				description = messages.getProperty(Constants.PROP_HISTORIC_REQUEST_REMOVED);
			}
			//a&ntilde;ade el nombre completo del usuario en una petici&oacute;n de tipo hist&oacute;rica a la descripci&oacute;n
			description = description.replace(
					Constants.HISTORIC_REQUEST_USER_VAR,
					getHistoricFullName(historicRequest));
			//a&ntilde;ade el nombre completo del job de la petici&oacute;n hist&oacute;rica a la descripci&oacute;n
			description = description.replace(
					Constants.HISTORIC_REQUEST_JOB_VAR,
					getHistoricJob(historicRequest));
			//guarda la descripci&oacute;n de peticiones hist&oacute;rica a mostrar
			historicRequest.setThistoricRequestShow(description);
		}
		return userHistoricRequests;
	}
	/**
	 * Recupera el nombre completo del job de la petici&oacute;n hist&oacute;rica
	 * @param historicRequest la petici&oacute;n hist&oacute;rica
	 * @return el nombre del job
	 */
	public String getHistoricJob(PfHistoricRequestsDTO historicRequest) {
		String job = "";
		//si es un job recupera el nombre completo del job
		if (historicRequest.getPfUser().isJob()) {
			job = " (" + historicRequest.getPfUser().getFullName() + ") ";
		}
		return job;
	}
	/**
	 * Recupera el nombre completo del usuario en una petici&oacute;n de tipo hist&oacute;rica
	 * @param historicRequest petici&oacute;n hist&oacute;rica
	 * @return el nombre completo del usuario
	 */
	public String getHistoricFullName(PfHistoricRequestsDTO historicRequest) {
		boolean found = false;
		String fullName = "";

		if (historicRequest.getThistoricRequest() != null
				&& !historicRequest.getThistoricRequest().equals("")) {
			//busca en la descripci&oacute;n de la petici&oacute;n hist&oacute;rica
			int indexUser = historicRequest.getThistoricRequest().indexOf(
					Constants.HISTORIC_REQUEST_USER_VAR);
			//si lo encuentra
			if (indexUser > -1) {
				found = true;
				int indexSeparator = historicRequest.getThistoricRequest()
						.indexOf(Constants.HISTORIC_REQUEST_VAR_SEPARATOR);
				fullName = historicRequest.getThistoricRequest().substring(
						indexUser + 3, indexSeparator--);
			}
		}
		if (!found) {
			fullName = historicRequest.getPfUser().getFullName();
		}
		return fullName;
	}
}

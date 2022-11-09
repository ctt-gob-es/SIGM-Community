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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestTagsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfSignersDTO;
import es.seap.minhap.portafirmas.domain.PfSignsDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.utils.Constants;


@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class PassBO  {

	@Autowired
	private BaseDAO baseDAO;

	@Autowired
	private TagBO tagBO;

	Logger log = Logger.getLogger(PassBO.class);

	/**
	 * Recupera de bbdd la lista de peticiones contenidas en la lista que pasamos como par&aacute;metro donde las 
	 * etiquetas de la petici&oacute;n sean del usuario o cargo pasado como par&aacute;metro y la etiqueta sea de tipo 'ESTADO' 
	 * @param requestList la lista de peticiones
	 * @param userDTO el usuario
	 * @param userJobDTO el cargo
	 * @return
	 */
	public List<AbstractBaseDTO> queryRequestsData(
			List<AbstractBaseDTO> requestTagList, PfUsersDTO userDTO,
			PfUsersDTO userJobDTO) {
		Object[] pkList = new Object[requestTagList.size()];
		//Recuperamos las cadenas con los c&oacute;digos de las peticiones
		for (int i = 0; i < requestTagList.size(); i++) {
			pkList[i] = new Long(((PfRequestTagsDTO) requestTagList.get(i)).getPrimaryKey());
		}
		//Construimos la query
		Map<String, Object[]> parametersList = new HashMap<String, Object[]>();
		Map<String, Object> parametersSimple = new HashMap<String, Object>();
		parametersList.put("listPks", pkList);
		parametersSimple.put("usr", userDTO);
		parametersSimple.put("usrJob", userJobDTO);
		List<AbstractBaseDTO> requests = baseDAO
				.queryListMoreParametersListComplex(
						"pass.requestTagDataPetitionList", parametersList, parametersSimple);
		return requests;
	}

	/**
	 * Recupera de bbdd la petición donde las 
	 * etiquetas de la petici&oacute;n sean del usuario o cargo pasado como par&aacute;metro y la etiqueta sea de tipo 'ESTADO' 
	 * @param request La petición
	 * @param userDTO el usuario
	 * @param userJobDTO el cargo
	 * @return
	 */
	public AbstractBaseDTO queryRequestData(AbstractBaseDTO requestTag, PfUsersDTO userDTO, PfUsersDTO userJobDTO) {
		//Construimos la query
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("pk", requestTag.getPrimaryKey());
		parameters.put("usr", userDTO);
		parameters.put("usrJob", userJobDTO);
		AbstractBaseDTO request = baseDAO.queryElementMoreParameters("pass.requestTagDataPetition", parameters);
		return request;
	}

	/**
	 * 
	 * @param userDTO el usuario
	 * @param userJobDTO el cargo
	 * @param passList 
	 * @param stateTags mapa de estado de las etiquetas
	 * @param job
	 */
	@Transactional(readOnly = false)
	public void pass(PfUsersDTO userDTO, PfUsersDTO userJobDTO, List<AbstractBaseDTO> passList,	boolean job, boolean lMovil) {
		log.info("endSign init");

		// Recuperamos las peticiones
		List<AbstractBaseDTO> requestTagsList = queryRequestsData(passList, userDTO, userJobDTO);

		//insertSign(requestTagsList, blockDTO, userDTO, userJobDTO, job);
		insertSign(requestTagsList, userDTO, userJobDTO, job, lMovil);

		tagBO.changeStateToPassedList(requestTagsList, userDTO, job);

		log.info("endSign end");
	}

	/**
	 * Método que da el visto bueno a una petición
	 * @param userDTO Usuario
	 * @param userJobDTO Cargo del usuario
	 * @param request Petición
	 * @param job La operación se hace como cargo
	 */
	@Transactional(readOnly = false)
	public void pass(PfUsersDTO userDTO, PfUsersDTO userJobDTO, AbstractBaseDTO request, boolean job, boolean lMovil) {
		log.info("endSign init");

		// Recuperamos las peticiones
		AbstractBaseDTO requestTag = queryRequestData(request, userDTO, userJobDTO);
		List<AbstractBaseDTO> requestTagsList = new ArrayList<AbstractBaseDTO>();
		requestTagsList.add(requestTag);

		insertSign(requestTagsList, userDTO, userJobDTO, job, lMovil);

		tagBO.changeStateToPassedList(requestTagsList, userDTO, job);

		log.info("endSign end");
	}

	/**
	 * 
	 * @param passList la lista de peticiones
	 * @param blockDTO el bloque
	 * @param userDTO  el usuario
	 * @param userJobDTO el cargo
	 * @param job
	 */
	public void insertSign(List<AbstractBaseDTO> passList,
			PfUsersDTO userDTO, PfUsersDTO userJobDTO,
			boolean job, boolean lMovil) {

		// Search signer
		PfSignersDTO signerDTO = null;
		PfSignsDTO signDTO = null;

		for (AbstractBaseDTO reqTag : passList) {
			PfRequestTagsDTO rt = (PfRequestTagsDTO) reqTag;
			PfRequestsDTO req = rt.getPfRequest();
			for (AbstractBaseDTO doc : ((PfRequestsDTO) req).getPfDocuments()) {
				//Si el documento es firmable
				if (((PfDocumentsDTO) doc).getLsign()) {

					signerDTO = tagBO.lastSignerUnresolvedDocumentPass(rt, userDTO, userJobDTO, job);
					signDTO = new PfSignsDTO();

					signDTO.setFstate(Calendar.getInstance().getTime());
					
					// User
					signDTO.setPfUser(userDTO);

					// Signer
					signDTO.setPfSigner(signerDTO);

					signDTO.setCformat(Constants.SIGN_FORMAT_NONE);
					signDTO.setCtype(Constants.C_TYPE_SIGNLINE_PASS);
					signDTO.setPfDocument((PfDocumentsDTO) doc);
					signDTO.setLmovil(lMovil);
					baseDAO.insertOrUpdate(signDTO);
				}
			}
		}
	}
}

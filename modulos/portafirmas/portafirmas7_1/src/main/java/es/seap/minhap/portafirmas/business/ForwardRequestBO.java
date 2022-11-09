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

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;

import es.seap.minhap.portafirmas.actions.ApplicationVO;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentScopesDTO;
import es.seap.minhap.portafirmas.domain.PfImportanceLevelsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestTagsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfTagsDTO;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.web.beans.RequestForward;

/**
 * @author juanmanuel.delgado
 *
 */
@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class ForwardRequestBO {
	
	Logger log = Logger.getLogger(RedactionBO.class);
	
	@Autowired
	private RequestBO requestBO;

	@Autowired
	private ApplicationVO applicationVO;
	
	@Autowired
	private BaseDAO baseDAO;
	
	/**
	 * Configura los valores iniales del formulario de reenvío a partir de la petición seleccionada
	 * @param forward
	 * @param request
	 */
	public void configurarValoresIniciales(RequestForward forward, PfRequestsDTO request) {
		forward.setSigners("");

		// Se crean los objetos de petición y de texto
		forward.setRequest(request);

		// Se definen los parámetros de notificación por defecto		
		forward.getRequest().setLOnlyNotifyActionsToRemitter(false);

	}

	
	/**
	 * Método que carga los formatos de firma disponibles en la configuración
	 * @param model Modelo de datos del formulario
	 */
	public void loadSignatureFormats(ModelMap model) {
		Map<String, Object> signatureConfigurations = requestBO.loadSignatureFormats();
		model.addAttribute("signatureConfigurations", signatureConfigurations);
	}
	
	/**
	 * Método que carga los tipos de prioridad disponibles en la configuración
	 * @param model Modelo de datos del formulario
	 */
	public void loadImportanceLevels(ModelMap model) {
		List<PfImportanceLevelsDTO> levelList = requestBO.queryImportanceLevels();
		Map<String, Object> importanceLevelList = new LinkedHashMap<String, Object>();  
		for (PfImportanceLevelsDTO level : levelList) {
			importanceLevelList.put(level.getCcodigonivel(), level.getCdescription());
		}
		model.addAttribute("importanceLevelList", importanceLevelList);
	}
	
//	/**
//	 * Método que carga los tipos de ámbito disponibles
//	 * @param model Modelo de datos del formulario
//	 */
//	public void loadScopes(ModelMap model) {
//		List<PfDocumentScopesDTO> scopeList = requestBO.queryDocumentScopes();
//		
//		Map<String, Object> scopeListMap = new LinkedHashMap<String, Object>();  
//		for (PfDocumentScopesDTO scope : scopeList) {
//			scopeListMap.put(scope.getPrimaryKeyString(), scope.getCdescription());			
//		}
//		model.addAttribute("scopeList", scopeListMap);
//	}
	
	/**
	 * Método que cambia el estado de las etiquetas de la petición a CADUCADO
	 * @param request 
	 */
	@Transactional
	public void changeStateToExpired(PfRequestsDTO request){
		
		PfTagsDTO tagExpired = applicationVO.getStateTags().get(Constants.C_TAG_EXPIRED);

		// Se obtienen las etiquetas de la petición y sus datos
		List<AbstractBaseDTO> etiquetasPeticion = 
			(List<AbstractBaseDTO>) baseDAO.queryListOneParameter("request.allRequestTags", "request", request);

		for (AbstractBaseDTO requestTag : etiquetasPeticion) {
			PfRequestTagsDTO etiquetaPeticion = (PfRequestTagsDTO) requestTag;
			if (etiquetaPeticion.getPfTag().getCtype().equals(Constants.C_TYPE_TAG_STATE) && 
					(!etiquetaPeticion.getPfTag().getCtag().equals(Constants.C_TAG_SIGNED) &&  
							!etiquetaPeticion.getPfTag().getCtag().equals(Constants.C_TAG_PASSED))) {
				etiquetaPeticion.setPfTag(tagExpired);
				baseDAO.insertOrUpdate(etiquetaPeticion);
			}
		}
	}

}

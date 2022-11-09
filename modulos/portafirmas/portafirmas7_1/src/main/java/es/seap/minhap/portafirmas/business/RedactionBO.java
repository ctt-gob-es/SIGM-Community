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
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.ui.ModelMap;

import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentScopesDTO;
import es.seap.minhap.portafirmas.domain.PfImportanceLevelsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestTemplatesDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsTextDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.web.beans.RequestRedaction;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class RedactionBO {
	
	Logger log = Logger.getLogger(RedactionBO.class);
	
	@Autowired
	private TemplateBO templateBO;
	
	@Autowired
	private RequestBO requestBO;
	
	@Autowired
	private BaseDAO baseDAO;
	
	/**
	 * Método que carga los datos de la plantilla en el formulario de redacción
	 */
	public void cargarPlantilla(String templateId, RequestRedaction redaction) {

		// Se recupera el usuario autenticado
		UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
		PfUsersDTO user = authorization.getUserDTO();
		PfRequestTemplatesDTO template = templateBO.loadTemplateUser(Long.valueOf(templateId), user);

		// Código de la plantilla
		redaction.setTemplateCode(template.getCtemplateCode());

		// Parámetros de la petición
		redaction.getRequest().setDreference(template.getDreference());
		redaction.getRequest().setDsubject(template.getDsubject());
		redaction.getRequest().setFstart(template.getFstart());
		redaction.getRequest().setFexpiration(template.getFend());

		// Configuración de firma
		redaction.setSignatureConfig(template.getCsignConfiguration());

		// Texto de la petición
		redaction.getRequestText().setTrequest(template.getTrequest());

		// Timestamp
		redaction.setAddTimestamp(template.getLtimestamp());
		
		// Firmantes
		String signers = template.getDsigners();
		if (!"".equals(signers) && signers != null) {
			signers = signers.replaceAll(";", ",");
			if (signers.lastIndexOf(",") == signers.length() - 1) {
				signers = signers.substring(0, signers.length() - 1);
			}
		}
		redaction.setSigners(signers);
		
		// Firmas o Vistos buenos
		String signPass = template.getCSignPass();
		if (!"".equals(signPass) && signPass != null) {
			signPass = signPass.replaceAll(";", ",");
			if (signPass.lastIndexOf(",") == signPass.length() - 1) {
				signPass = signPass.substring(0, signPass.length() - 1);
			}
		}
		redaction.setSignlinesType(signPass);
		
		// Acciones de firma
		String accionesFirmaList = template.getCAccionesFirma();
		if (!"".equals(accionesFirmaList) && accionesFirmaList != null) {
			accionesFirmaList = accionesFirmaList.replaceAll(";", ",");
			if (accionesFirmaList.lastIndexOf(",") == accionesFirmaList.length() - 1) {
				accionesFirmaList = accionesFirmaList.substring(0, accionesFirmaList.length() - 1);
			}
		}
		redaction.setSignlinesAccion(accionesFirmaList);
		
		// Notificaciones
		redaction.setNotices(calcularNotificaciones(template));

		// Modo de notificar las acciones
		redaction.getRequest().setLOnlyNotifyActionsToRemitter(template.getLOnlyNotifyActionsToRemitter());

		// Obtiene el nivel de importancia de la petición
		PfImportanceLevelsDTO importanceLevel = requestBO.obtainImportanceLevel(Long.valueOf(template.getCimportanceLevel()));
		redaction.setImportanceLevel(importanceLevel.getCcodigonivel());
		
		// Ámbito de la petición.
		PfDocumentScopesDTO scope = requestBO.getInternalDocumentScope();
		redaction.setScopeType(scope.getPrimaryKeyString());
		
		// Cascada o paralelo
		String signType = null;
		if (Constants.FIRMA_CASCADA.toLowerCase().equals(template.getCsignType()) ||
			Constants.FIRMA_CASCADA.toUpperCase().equals(template.getCsignType())) {
			signType = Constants.SIGN_TYPE_CASCADE;
		} else {
			signType = Constants.SIGN_TYPE_PARALLEL;
		}
		redaction.setSignType(signType);

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
	
	/**
	 * Método que calcula las notificaciones programadas de una plantilla de petición
	 * @param template Plantilla
	 */
	private String[] calcularNotificaciones(PfRequestTemplatesDTO template) {
		ArrayList<String> noticesAux = new ArrayList<String>();
		if (template.getLreadNotice()) {
			noticesAux.add("noticeRead");
		}
		if (template.getLrejectNotice()) {
			noticesAux.add("noticeReject");
		}
		if (template.getLsignNotice()) {
			noticesAux.add("noticeSign");
		}
		if (template.getLvalidateNotice()) {
			noticesAux.add("noticePassed");
		}

		String[] notices = new String[noticesAux.size()];
		for (int i = 0; i < noticesAux.size(); i++) {
			notices[i] = noticesAux.get(i);
		}

		return notices;
	}
	
	/**
	 * Configura los valores iniales del formulario de redacción
	 * @param redaction
	 */
	public void configurarValoresIniciales (RequestRedaction redaction) {
		
		redaction.setEsEnveloped(false);
		redaction.setSigners("");

		// Se crean los objetos de petición y de texto
		redaction.setRequest(new PfRequestsDTO());
		redaction.setRequestText(new PfRequestsTextDTO ());
		

		// Se definen los parámetros de firma por defecto
		redaction.setSignatureConfig("PFIRMA");
		redaction.setSignType(Constants.SIGN_TYPE_CASCADE);

		// Se definen los parámetros de notificación por defecto		
		redaction.setNotices(new String[]{"noticeRead", "noticeReject", "noticeSign","noticePassed"});
		redaction.getRequest().setLOnlyNotifyActionsToRemitter(false);
		
		// indicamos el ámbito por defecto.
		redaction.setScopeType(requestBO.getInternalDocumentScope().getPrimaryKeyString());
	}

	/**
	 * Método que devuelve un usuario por su PrimaryKey
	 * @param user Usuario
	 * @return Listado de provincias
	 */
	public AbstractBaseDTO getUser (String userPk) {
		AbstractBaseDTO user = baseDAO.queryElementOneParameter("request.queryUserPrimaryKey", "usuario", Long.parseLong(userPk));
		return user;
	}
}

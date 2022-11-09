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

package es.seap.minhap.portafirmas.web.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import es.seap.minhap.portafirmas.business.ApplicationBO;
import es.seap.minhap.portafirmas.business.RedactionBO;
import es.seap.minhap.portafirmas.business.RequestBO;
import es.seap.minhap.portafirmas.business.TemplateBO;
import es.seap.minhap.portafirmas.domain.PfDocumentScopesDTO;
import es.seap.minhap.portafirmas.domain.PfImportanceLevelsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestTemplatesDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.web.beans.RequestRedaction;

@Controller
@RequestMapping("template")
public class TemplateController {

	protected final Log log = LogFactory.getLog(getClass());

	@Autowired
	private RedactionBO redactionBO;

	@Autowired
	private TemplateBO templateBO;

	@Autowired
	private RequestBO requestBO;

	@Autowired
	private ApplicationBO applicationBO;

	@RequestMapping(method = RequestMethod.GET)
	public String initForm(ModelMap model, @RequestParam(value = "templateId") final String templateId) {

		// MÉTODO QUE INICIALIZA EL FORMULARIO
		log.debug("Se inicializa el formulario de administración de plantilla");

		// Se crea el modelo del formulario
		RequestRedaction redaction = new RequestRedaction();
		redactionBO.configurarValoresIniciales (redaction);

		// Si se indica un código de plantilla como parámetro se cargan los datos
		if (!"".equals(templateId) && templateId != null) {
			redactionBO.cargarPlantilla(templateId, redaction);
		}

		// Se cargan los formatos de firma
		redactionBO.loadSignatureFormats(model);

		// Se cargan los niveles de importancia
		redactionBO.loadImportanceLevels(model);

		// Se cargan los ámbitos
		//		redactionBO.loadScopes (model);

		model.addAttribute("redaction", redaction);
		model.addAttribute("documentTypeList", applicationBO.queryDocumentTypePfirma());
		model.addAttribute("templateId", templateId);
		return "template";
	}

	/**
	 * Método que procesa el envío del formulario al servidor
	 * @param redaction Modelo de datos del formulario
	 * @param templateId Identificador de plantilla
	 * @return Modelo de datos de respuesta
	 * @throws Exception 
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView processSubmit(@ModelAttribute("redaction") RequestRedaction redaction, final HttpServletResponse response,
			@RequestParam(value = "templateId") final String templateId) throws Exception {
		log.debug("Se guarda la plantilla con id " + templateId);
		ModelAndView result = new ModelAndView();

		// Se recupera el usuario autenticado
		UserAuthentication authorization = (UserAuthentication) SecurityContextHolder
				.getContext().getAuthentication();
		PfUsersDTO user = authorization.getUserDTO();

		if (redaction.getAction().equals("guardar")) {

			// Se recupera la plantilla si estamos editando, si no, se crea una nueva
			PfRequestTemplatesDTO template = null;
			if (templateId != null && !"".equals(templateId)) {
				template = templateBO.loadTemplateUser(Long.valueOf(templateId), user);
			} else {
				template = new PfRequestTemplatesDTO();
			}

			// Se configura el código de plantilla
			template.setCtemplateCode(redaction.getTemplateCode());

			// Se configura el sello de tiempo
			if (redaction.isAddTimestamp()) {
				redaction.getRequest().setLtimestamp(Constants.C_YES);
			} else {
				redaction.getRequest().setLtimestamp(Constants.C_NOT);
			}

			// Se configuran las fechas
			redaction.getRequest().setFstart(Util.getInstance().stringToDate(redaction.getFstart(), "dd/MM/yyyy"));
			redaction.getRequest().setFexpiration(Util.getInstance().stringToDate(redaction.getFexpiration(), "dd/MM/yyyy"));

			// Parámetros de la petición
			template.setDreference(redaction.getRequest().getDreference());
			template.setDsubject(redaction.getRequest().getDsubject());
			template.setFstart(redaction.getRequest().getFstart());
			template.setFend(redaction.getRequest().getFexpiration());

			// Configuración de firma
			template.setCsignConfiguration(redaction.getSignatureConfig());

			// Texto de la petición
			if (redaction.getRequestText() != null) {
				template.setTrequest(redaction.getRequestText().getTrequest());
			}

			// Timestamp
			template.setLtimestamp(redaction.isAddTimestamp());

			// Firmantes
			template.setDsigners(redaction.getSigners());

			// Firma o Visto bueno
			template.setCSignPass(redaction.getSignlinesType());

			// Acciones de Firma
			template.setCAccionesFirma(redaction.getSignlinesAccion());
			
			// Notificaciones
			template.setLreadNotice(false);
			template.setLrejectNotice(false);
			template.setLsignNotice(false);
			template.setLvalidateNotice(false);
			for (String notice : redaction.getNotices()) {
				if ("noticeRead".equals(notice)) {
					template.setLreadNotice(true);
				} else if ("noticeReject".equals(notice)) {
					template.setLrejectNotice(true);
				} else if ("noticeSign".equals(notice)) {
					template.setLsignNotice(true);
				} else if ("noticePassed".equals(notice)) {
					template.setLvalidateNotice(true);
				}
			}
			template.setLOnlyNotifyActionsToRemitter(redaction.getRequest().getLOnlyNotifyActionsToRemitter());

			// Obtiene el nivel de importancia de la petición
			// Se configura la prioridad
			PfImportanceLevelsDTO importanceLevel = requestBO.obtainImportanceLevelByCode(redaction.getImportanceLevel());		
			if (importanceLevel.getPrimaryKey() == null) {
				template.setCimportanceLevel("1");
			} else {
				template.setCimportanceLevel(importanceLevel.getPrimaryKeyString());
			}

			// Ambito
			PfDocumentScopesDTO scope = requestBO.getInternalDocumentScope();
			template.setCdocumentScope(scope.getPrimaryKey());

			// Cascada o paralelo
			if (redaction.getSignType().equals(Constants.SIGN_TYPE_CASCADE)) {
				template.setCsignType(Constants.FIRMA_CASCADA);
			} else {
				template.setCsignType(Constants.FIRMA_PARALELA);
			}

			// Agregar visto bueno no puede ser nulo (en esta versión no se utiliza)
			template.setLaddPass(false);

			// Se guarda la plantilla para el usuario
			template.setPfUser(user);
			templateBO.saveTemplate(template);
		}

		result.setViewName("redirect:/inbox");
		return result;
	}

	/**
	 * Método que elimina una plantilla de redacción de un usuario
	 * @param templateId Identificador de la plantilla
	 * @param response Respuesta del servidor
	 * @throws IOException 
	 */
	@RequestMapping(value = "/deleteTemplate")
	public void deleteTemplate(@RequestParam(value = "templateId") final String templateId,
			final HttpServletResponse response) throws IOException {
		// Se recupera el usuario autenticado
		UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
		PfUsersDTO user = authorization.getUserDTO();
		// Se carga la plantilla a eliminar
		PfRequestTemplatesDTO template = templateBO.loadTemplateUser(Long.valueOf(templateId), user);

		// Se elimina la plantilla
		templateBO.deleteTemplate(template);

		response.setContentType("application/json");
		response.getWriter().write("{\"status\": \"success\"}");
	}

	/**
	 * Método que procesa el envío del formulario al servidor
	 * @param templateName
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(value = "/validate", method = RequestMethod.GET)
	@ResponseBody
	public void validate(@RequestParam(value = "templateName") final String templateName, @RequestParam(value = "templateId") final String templateId,
			final HttpServletResponse response) throws Exception {

		boolean plantillaDuplicada = false;
		boolean nombreModificado = false;
		// Se recupera el usuario autenticado
		UserAuthentication authorization = (UserAuthentication) SecurityContextHolder
				.getContext().getAuthentication();
		PfUsersDTO user = authorization.getUserDTO();

		//Se obtiene correctamente el nombre de la plantilla
		String labelCodeDecode = templateName;
		try {
			labelCodeDecode = java.net.URLDecoder
					.decode(new String(org.apache.commons.codec.binary.Base64.decodeBase64(templateName)), "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		
		//Se comprueba que la plantilla no tenga un nombre repetido para el usuario logado

		List<PfRequestTemplatesDTO> listaPlantillas = templateBO.getUserTemplates(user);

		for (PfRequestTemplatesDTO plantilla : listaPlantillas){				
			String nombrePlantilla = plantilla.getCtemplateCode();				
			if (labelCodeDecode.equalsIgnoreCase(nombrePlantilla)){
				plantillaDuplicada = true;
			}
			if (templateId.equals(plantilla.getPrimaryKeyString()) && !labelCodeDecode.equals(plantilla.getCtemplateCode()) ){
				nombreModificado = true;
			}
		}

		if (plantillaDuplicada && (templateId==null || "".equals(templateId))) {
			response.getWriter().write("{\"status\": \"error\", \"log\": \"" + "El nombre de la plantilla se encuentra duplicado" + "\"}");
		} else if (plantillaDuplicada && nombreModificado) {
			response.getWriter().write("{\"status\": \"error\", \"log\": \"" + "El nuevo nombre de la plantilla se encuentra duplicado" + "\"}");
		} else {
			response.getWriter().write("{\"status\": \"success\", \"log\": \"\"}");
		}
	}
}

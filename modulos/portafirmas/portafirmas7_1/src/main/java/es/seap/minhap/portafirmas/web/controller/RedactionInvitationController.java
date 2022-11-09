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

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import javax.annotation.Resource;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import es.seap.minhap.portafirmas.business.ApplicationBO;
import es.seap.minhap.portafirmas.business.NoticeBO;
import es.seap.minhap.portafirmas.business.ProvinceBO;
import es.seap.minhap.portafirmas.business.RedactionBO;
import es.seap.minhap.portafirmas.business.RequestBO;
import es.seap.minhap.portafirmas.business.beans.RedactionUploadedDocuments;
import es.seap.minhap.portafirmas.business.beans.UploadedDocument;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfApplicationsDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentScopesDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentTypesDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentsDTO;
import es.seap.minhap.portafirmas.domain.PfImportanceLevelsDTO;
import es.seap.minhap.portafirmas.domain.PfInvitedUsersDTO;
import es.seap.minhap.portafirmas.domain.PfProvinceDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.DateComponent;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.utils.document.CustodyServiceException;
import es.seap.minhap.portafirmas.utils.envelope.SeatEnvelope;
import es.seap.minhap.portafirmas.utils.envelope.UserEnvelope;
import es.seap.minhap.portafirmas.web.beans.DocumentType;
import es.seap.minhap.portafirmas.web.beans.RequestRedaction;
import es.seap.minhap.portafirmas.web.beans.UserSelection;
import es.seap.minhap.portafirmas.web.validation.RedactionValidator;

@Controller
@RequestMapping("redactionInvitation")
public class RedactionInvitationController {

	protected final Log log = LogFactory.getLog(getClass());
	
	@Autowired
	DateComponent dateComponent;
	
	@Autowired
	private RequestBO requestBO;

	@Autowired
	private RedactionBO redactionBO;
	
	@Autowired
	private ApplicationBO applicationBO;

	@Autowired
	private ProvinceBO provinceBO;
	@Autowired
	private NoticeBO noticeBO;

	@Autowired
	private RedactionValidator redactionValidator;

	@Resource
	private RedactionUploadedDocuments uploadedFiles;

	@InitBinder
	public void initBinder(WebDataBinder binder) {
		binder.setValidator(redactionValidator);
	}

	@RequestMapping(method = RequestMethod.GET)
	public String initForm(ModelMap model, @RequestParam(value = "templateId") final String templateId) {

		// MÉTODO QUE INICIALIZA EL FORMULARIO
		log.debug("Se inicializa el formulario de redacción de peticiones.");

		// Se crea el modelo del formulario
		RequestRedaction redaction = new RequestRedaction();
		redactionBO.configurarValoresIniciales(redaction);

		// Si se indica un código de plantilla como parámetro se cargan los
		// datos
		if (!"".equals(templateId) && templateId != null) {
			redactionBO.cargarPlantilla(templateId, redaction);
		}

		initValidations(model);

		cargarCombos(model);
		uploadedFiles.removeAlls();
		redaction.getRequest().setLinvited(true);
		redaction.getRequest().setLaccepted(false);
		model.addAttribute("redactionInvitation", redaction);
		model.addAttribute("documentTypeList", applicationBO.queryDocumentTypePfirma());
		return "redactionInvitation";
	}

	private void initValidations(ModelMap model) {
		boolean validacionesAutomaticas = applicationBO.isValidacionesAutomaticas();
		boolean validacionPDFA = applicationBO.isValidacionPDFA();
		boolean validacionTamanio = applicationBO.isValidacionTamanio();

		if (validacionesAutomaticas) {
			validacionPDFA = true;
			validacionTamanio = true;
		}

		model.addAttribute("validacionesAutomaticas", validacionesAutomaticas);
		model.addAttribute("validacionPDFA", validacionPDFA);
		model.addAttribute("validacionTamano", validacionTamanio);
	}

	private void cargarCombos(ModelMap model) {
		// Se cargan los formatos de firma
		redactionBO.loadSignatureFormats(model);

		// Se cargan los niveles de importancia
		redactionBO.loadImportanceLevels(model);

		// Se cargan los ámbitos
		// redactionBO.loadScopes (model);
	}

	/**
	 * Método que procesa el envío del formulario al servidor
	 * 
	 * @param redaction
	 *            Modelo de datos del formulario
	 * @param bindingResult
	 * @param status
	 * @return Modelo de datos de respuesta
	 * @throws Exception
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView processSubmit(@ModelAttribute("redactionInvitation") @Valid RequestRedaction redaction,
			BindingResult bindingResult, SessionStatus status) throws Exception {
		ModelAndView result = new ModelAndView();
		try {
			log.debug("Se envía la petición con hash " + redaction.getRequest().getChash());
			
			// Se recupera el usuario autenticado
			UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext()
					.getAuthentication();
			PfUsersDTO user = authorization.getUserDTO();

			List<PfDocumentsDTO> attachedDocuments = new LinkedList<PfDocumentsDTO>();
			List<File> attachedFiles = new LinkedList<File>();

			if(Util.esVacioONulo(redaction.getSigner())){
				bindingResult.rejectValue("emailEmptyError", "field.required.email");
			}else{
				if(!Util.validateEmail(redaction.getSigner())){
					bindingResult.rejectValue("emailPatternError", "field.wrong.email");
				}
			}
			
			// Solo los documentos son obligatorios, al contrario que los anexos
			if (uploadedFiles.getDocuments().isEmpty()) {
				bindingResult.rejectValue("uploadedDocsError", "field.required.uploadedDocs");
			}
			if (!Util.esVacioONulo(redaction.getRequest().getDreference())) {
				if (redaction.getRequest().getDreference().length() > 30)
					bindingResult.rejectValue("referenceLengthError", "field.length.reference");
			}
			
			if (!Util.esVacioONulo(redaction.getRequest().getDsubject())) {
				if (redaction.getRequest().getDsubject().length() > 255)
					bindingResult.rejectValue("subjectLengthError", "field.length.subject");
			}

			if (bindingResult.hasErrors() && redaction.getAction().equals("guardar")) {
				uploadedFiles.removeAlls();
				cargarCombos(result.getModelMap());
				result.addObject("documentTypeList", applicationBO.queryDocumentTypePfirma());

				initValidations(result.getModelMap());

				result.addObject("redactionInvitation", redaction);
				result.setViewName("redactionInvitation");
				return result;
			}

			// Se adjuntan los documentos
			for (UploadedDocument doc : uploadedFiles.getDocuments().values()) {
				// Se guarda el documento en la lista
				attachedDocuments.add(doc.getDocument());

				// Se recupera el archivo de la carpeta temporal
				File file = new File(Constants.PATH_TEMP + doc.getFile());
				attachedFiles.add(file);
			}

			// Se adjuntan los anexos
			for (UploadedDocument annex : uploadedFiles.getAnnexes().values()) {
				// Se guarda el documento en la lista
				attachedDocuments.add(annex.getDocument());

				// Se recupera el archivo de la carpeta temporal
				File file = new File(Constants.PATH_TEMP + annex.getFile());
				attachedFiles.add(file);
			}

			if (redaction.getAction().equals("guardar")) {
				List<String> errors = new ArrayList<String>();
				List<UserEnvelope> destinatarios = null;
				
				//Se configura la parte de invitación (usuario invitado)
				PfInvitedUsersDTO invitedUser = new PfInvitedUsersDTO();
				invitedUser.setcMail(redaction.getSigner());
				redaction.getRequest().setInvitedUser(invitedUser);
				destinatarios = redactionValidator.validate(redaction, errors);

				if (!errors.isEmpty()) {
					uploadedFiles.removeAlls();
					cargarCombos(result.getModelMap());
					result.addObject("documentTypeList", applicationBO.queryDocumentTypePfirma());
					result.addObject("redactionInvitation", redaction);
					result.addObject("usersNotValid", errors);
					result.setViewName("redactionInvitation");
					return result;
				}

				// Se configura el sello de tiempo
				if (redaction.isAddTimestamp()) {
					redaction.getRequest().setLtimestamp(Constants.C_YES);
				} else {
					redaction.getRequest().setLtimestamp(Constants.C_NOT);
				}

				// Se configura el asunto
				if ("".equals(redaction.getRequest().getDsubject()) || redaction.getRequest().getDsubject() == null) {
					redaction.getRequest().setDsubject("(Sin asunto)");
				}

				// Se configura la prioridad
				PfImportanceLevelsDTO importanceLevel = requestBO
						.obtainImportanceLevelByCode(redaction.getImportanceLevel());
				redaction.getRequest().setPfImportance(importanceLevel);

				// Se configura el ámbito
				PfDocumentScopesDTO scope = requestBO.getInternalDocumentScope();

				// Se configuran las fechas
				redaction.getRequest().setFstart(Util.getInstance().stringToDate(redaction.getFstart()));
				redaction.getRequest().setFexpiration(Util.getInstance().stringToDate(redaction.getFexpiration()));
				
				try {
					// Se crea la petición
					requestBO.saveRequest(redaction.getRequest(), redaction.getRequestText(), attachedDocuments,
							attachedFiles, destinatarios, redaction.getSignType(), redaction.getNotices(), user,
							authorization.getGroup(), getApplication(redaction), applicationBO.queryStateTags(), scope);

					//Envío del correo
					noticeBO.noticeNewInvitationRequest(redaction.getRequest());

				} catch (CustodyServiceException | IOException e) {
					log.error("Error al subir los documentos de la petición con hash: "
							+ redaction.getRequest().getChash(), e);
				}

			}

			// Se borran los ficheros de la carpeta temporal
			uploadedFiles.removeAlls();
			for (File file : attachedFiles) {
				file.delete();
			}

			result.setViewName("redirect:/inbox");

			return result;
		} catch (RuntimeException e) {
			log.error("Error al enviar la redacción: ", e);
			result.getModelMap().addAttribute("errorMessage", Constants.MSG_GENERIC_ERROR);
			result.getModelMap().addAttribute("timeError", new Date(System.currentTimeMillis()));
			result.setViewName("error");
			return result;
		}
	}

	/**
	 * Método que borra un documento de la lista de documentos subidos
	 * 
	 * @param redaction
	 *            Modelo de datos
	 * @param type
	 *            Tipo del documento a borrar
	 * @param id
	 *            Posición del documento a borrar
	 * @return Modelo de datos actualizado
	 */
	@RequestMapping(value = "deleteDocumentInvReq")
	public ModelAndView borraDocumento(@ModelAttribute("redactionInvitation") RequestRedaction redaction,
			@RequestParam(value = "type") final String type, @RequestParam(value = "id") final String id) {
		ModelAndView result = new ModelAndView();
		UploadedDocument doc = null;

		if ("document".equals(type)) {
			doc = uploadedFiles.getDocuments().get(id);
			File file = new File(Constants.PATH_TEMP + doc.getFile());
			file.delete();
			uploadedFiles.removeDocument(id);
		} else {
			doc = uploadedFiles.getAnnexes().get(id);
			File file = new File(Constants.PATH_TEMP + doc.getFile());
			file.delete();
			uploadedFiles.removeAnnex(id);
		}

		result.addObject("documentTypeList", applicationBO.queryDocumentTypePfirma());
		result.addObject("redactionInvitation", redaction);
		result.setViewName("redactionInvitation");

		return result;
	}

	/**
	 * Método que borra un documento de la lista de documentos subidos
	 * 
	 * @param redaction
	 *            Modelo de datos
	 * @param type
	 *            Tipo del documento a borrar
	 * @param id
	 *            Posición del documento a borrar
	 * @return Modelo de datos actualizado
	 */
	@RequestMapping(value = "updateDocumentTypeInvReq")
	public ModelAndView actualizarTipoDocumento(@ModelAttribute("redactionInvitation") RequestRedaction redaction,
			@RequestParam(value = "docId") final String docId, @RequestParam(value = "type") final String type,
			@RequestParam(value = "docType") final String docType) {
		ModelAndView result = new ModelAndView();
		UploadedDocument doc = null;

		if ("document".equals(type)) {
			doc = uploadedFiles.getDocuments().get(docId);
			PfDocumentTypesDTO documentType = applicationBO.queryDocumentTypeById(docType);
			doc.getDocument().setPfDocumentType(documentType);
		} else {
			doc = uploadedFiles.getAnnexes().get(docId);
			PfDocumentTypesDTO documentType = applicationBO.queryDocumentTypeById(docType);
			doc.getDocument().setPfDocumentType(documentType);
		}

		result.addObject("documentTypeList", applicationBO.queryDocumentTypePfirma());
		result.addObject("redactionInvitation", redaction);
		result.setViewName("redactionInvitation");

		return result;
	}


	/**
	 * Método que carga la ventana modal que permite seleccionar los usuarios
	 * destinatarios de la petición
	 * 
	 * @param signers
	 * @param signLinesConfig
	 * @param userNameFilter
	 * @param userTypeFilter
	 * @param userSeatFilter
	 * @return
	 */
	@RequestMapping(value = "selectUsersInvReq")
	public @ResponseBody UserSelection seleccionarDestinatarios(@RequestParam(value = "signers") String signersBase64,
			@RequestParam(value = "signLinesConfig") String signLinesConfig,
			@RequestParam(value = "signLinesAccion") String signLinesAccion,
			@RequestParam(value = "userNameFilter") final String userNameFilter,
			@RequestParam(value = "userTypeFilter") final String userTypeFilter,
			@RequestParam(value = "userSeatFilter") final String userSeatFilter,
			@RequestParam(value = "firstInvocation", required = false) final Boolean firstInvocation) {

		String signers = "";
		try {
			signers = java.net.URLDecoder
					.decode(new String(org.apache.commons.codec.binary.Base64.decodeBase64(signersBase64)), "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		UserSelection userSelection = new UserSelection();

		// Se recupera el usuario autenticado
		UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
		PfUsersDTO user = authorization.getUserDTO();

		// Se obtiene la sede del usuario autenticado
		String codSede = null;
		if (user.getPfProvince() != null) {
			codSede = user.getPfProvince().getCcodigoprovincia();
		}

		// Se obtienen las sedes de los usuarios
		loadUserSeats(userSelection, codSede);

		// Se cargan los usuarios a mostrar
		if (firstInvocation == null || !firstInvocation.booleanValue()) {
			requestBO.loadUsersToPick(userSelection, user, codSede, signers, signLinesConfig, userNameFilter,
					userTypeFilter, userSeatFilter, null, firstInvocation, signLinesAccion);
		}
		return userSelection;
	}

	@RequestMapping(value = "autocompleteSignersInvReq")
	public @ResponseBody List<String> autocompleteSigners(@RequestParam(value = "term") final String term) {

		List<String> results = new ArrayList<String>();

		// Se recupera el usuario autenticado
		UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
		PfUsersDTO user = authorization.getUserDTO();

		// Se obtiene la sede del usuario autenticado
		String codSede = null;
		if (user.getPfProvince() != null) {
			codSede = user.getPfProvince().getCcodigoprovincia();
		}

		// Se toma para el filtro el último valor introducido
		String[] values = term.split(",");

		// Se filtran los usuarios en base a la búsqueda
		List<AbstractBaseDTO> users = requestBO.queryUsersCompleteWithoutVisibles(values[values.length - 1].trim(),
				codSede);

		// Se convierten los resultados
		for (AbstractBaseDTO dto : users) {
			PfUsersDTO autocompletedUser = (PfUsersDTO) dto;
			results.add(autocompletedUser.getFullNameWithProvince());
		}

		return results;
	}

	/**
	 * Método que devuelve la lista de tipos de documentos
	 * 
	 * @return Lista de tipos de documentos
	 */
	@RequestMapping(value = "getDocumentTypesInvReq")
	public @ResponseBody List<DocumentType> getDocumentTypes() {
		List<DocumentType> result = new ArrayList<DocumentType>();
		List<AbstractBaseDTO> documentsType = applicationBO.queryDocumentTypePfirma();
		for (AbstractBaseDTO dto : documentsType) {
			PfDocumentTypesDTO docTypeDTO = (PfDocumentTypesDTO) dto;
			DocumentType docType = new DocumentType();
			docType.setId(docTypeDTO.getPrimaryKeyString());
			docType.setName(docTypeDTO.getCdocumentType());
			result.add(docType);
		}
		return result;
	}

	
	
	/**
	 * Método que carga las sedes de los usuarios
	 * 
	 * @param model
	 *            Modelo de datos del formulario
	 */
	public void loadUserSeats(UserSelection userSelection, String seatCode) {
		List<AbstractBaseDTO> provinceList = provinceBO.getVisibleProvinces(seatCode);
		List<SeatEnvelope> seatList = new LinkedList<SeatEnvelope>();
		for (AbstractBaseDTO province : provinceList) {
			SeatEnvelope newSeat = new SeatEnvelope();
			PfProvinceDTO seat = (PfProvinceDTO) province;
			newSeat.setCode(seat.getCcodigoprovincia());
			newSeat.setName(seat.getCnombre());
			seatList.add(newSeat);
		}
		userSelection.setSeatList(seatList);
	}

	/**
	 * Método que obtiene la aplicación a la que asociar la petición redactada
	 * 
	 * @param redaction
	 *            Datos de redacción
	 * @return Aplicación asociada
	 */
	private PfApplicationsDTO getApplication(RequestRedaction redaction) {
		PfApplicationsDTO application = null;
		String conf = redaction.getSignatureConfig();

		application = applicationBO.loadApplication(conf);

		return application;
	}

}

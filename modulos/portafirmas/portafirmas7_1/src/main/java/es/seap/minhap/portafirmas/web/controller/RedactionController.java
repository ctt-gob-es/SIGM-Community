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
import java.util.Locale;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.lowagie.text.pdf.PdfReader;

import es.seap.minhap.interfazGenerica.domain.Portafirmas;
import es.seap.minhap.interfazGenerica.repository.PortafirmasRepository;
import es.seap.minhap.portafirmas.actions.ApplicationVO;
import es.seap.minhap.portafirmas.business.ApplicationBO;
import es.seap.minhap.portafirmas.business.NoticeBO;
import es.seap.minhap.portafirmas.business.ProvinceBO;
import es.seap.minhap.portafirmas.business.RedactionBO;
import es.seap.minhap.portafirmas.business.RequestBO;
import es.seap.minhap.portafirmas.business.RestrictionBO;
import es.seap.minhap.portafirmas.business.beans.RedactionUploadedDocuments;
import es.seap.minhap.portafirmas.business.beans.UploadedDocument;
import es.seap.minhap.portafirmas.business.configuration.AuthorizationBO;
import es.seap.minhap.portafirmas.business.ws.Afirma5BO;
import es.seap.minhap.portafirmas.business.ws.EEUtilMiscBO;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfAccionFirmanteDTO;
import es.seap.minhap.portafirmas.domain.PfApplicationsDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentScopesDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentTypesDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentsDTO;
import es.seap.minhap.portafirmas.domain.PfImportanceLevelsDTO;
import es.seap.minhap.portafirmas.domain.PfProvinceDTO;
import es.seap.minhap.portafirmas.domain.PfRequestTagsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.domain.PfUsersJobDTO;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.DateComponent;
import es.seap.minhap.portafirmas.utils.SignDataUtil;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.utils.UtilComponent;
import es.seap.minhap.portafirmas.utils.document.CustodyServiceException;
import es.seap.minhap.portafirmas.utils.envelope.PortafirmasEnvelope;
import es.seap.minhap.portafirmas.utils.envelope.SeatEnvelope;
import es.seap.minhap.portafirmas.utils.envelope.UserEnvelope;
import es.seap.minhap.portafirmas.web.beans.DocumentType;
import es.seap.minhap.portafirmas.web.beans.RequestRedaction;
import es.seap.minhap.portafirmas.web.beans.UploadFileBean;
import es.seap.minhap.portafirmas.web.beans.UserAutocomplete;
import es.seap.minhap.portafirmas.web.beans.UserSelection;
import es.seap.minhap.portafirmas.web.validation.RedactionValidator;
import es.seap.minhap.portafirmas.ws.afirma5.clientmanager.ConfigManagerBO;
import es.seap.minhap.portafirmas.ws.afirma5.validarfirma.RespuestaValidarFirma;
import es.seap.minhap.portafirmas.ws.docelweb.interfazgenerica.business.InterfazGenericaBO;

@Controller
@RequestMapping("redaction")
public class RedactionController {

	protected final Log log = LogFactory.getLog(getClass());
	
	@Resource(name = "messageProperties")
	private Properties messages;

	@Autowired
	private BaseDAO baseDAO;

	@Autowired
	DateComponent dateComponent;

	@Autowired
	private RequestBO requestBO;

	@Autowired
	private RedactionBO redactionBO;

	@Autowired
	private ApplicationVO applicationVO;

	@Autowired
	private ApplicationBO applicationBO;

	@Autowired
	private ProvinceBO provinceBO;

	@Autowired
	private AuthorizationBO authorizationBO;

	@Autowired
	private NoticeBO noticeBO;

	@Autowired
	private EEUtilMiscBO eeUtilMiscBO;

	@Autowired
	private PortafirmasRepository portafirmasRepository;
	
	@Autowired
	private RedactionValidator redactionValidator;

	@Autowired
	private SignDataUtil signDataUtil;

	@Resource
	private RedactionUploadedDocuments uploadedFiles;

	@Autowired
	UtilComponent utilComponent;

	@Autowired
	ApplicationContext context;

	@Autowired
	private ConfigManagerBO configManagerBO;

	@Autowired
	private Afirma5BO afirma5BO;
	
	@Autowired
	private InterfazGenericaBO interfazGenericaBO; 

	@Autowired
	private RestrictionBO restrictionBO;

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
		redaction.getRequest().setLinvited(false);
		redaction.getRequest().setLaccepted(false);
		model.addAttribute("redaction", redaction);
		model.addAttribute("documentTypeList", applicationBO.queryDocumentTypePfirma());
		return "redaction";
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
	public ModelAndView processSubmit(@ModelAttribute("redaction") @Valid RequestRedaction redaction,
			BindingResult bindingResult, SessionStatus status) throws Exception {
		ModelAndView result = new ModelAndView();
		try {

			if (redaction.getRequest() != null){

				log.debug("Se envía la petición con hash " + redaction.getRequest().getChash());

				// Se recupera el usuario autenticado
				UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext()
						.getAuthentication();
				PfUsersDTO user = authorization.getUserDTO();

				List<PfDocumentsDTO> attachedDocuments = new LinkedList<PfDocumentsDTO>();
				List<File> attachedFiles = new LinkedList<File>();

				// Solo los documentos son obligatorios, al contrario que los anexos
				if (uploadedFiles.getDocuments().isEmpty()) {
					bindingResult.rejectValue("uploadedDocsError", "field.required.uploadedDocs");
				}
				if (!Util.esVacioONulo(redaction.getRequest().getDreference())) {
					if (redaction.getRequest().getDreference().length() > 30)
						bindingResult.rejectValue("referenceLengthError", "field.length.reference");
				}

				if(!Util.esVacioONulo(redaction.getRequest().getDsubject())){
					if(redaction.getRequest().getDsubject().length()>255){
						bindingResult.rejectValue("subjectLengthError", "field.length.subject");
					}
				}

				if (bindingResult.hasErrors() && redaction.getAction().equals("enviar")) {
					uploadedFiles.removeAlls();
					cargarCombos(result.getModelMap());
					result.addObject("documentTypeList", applicationBO.queryDocumentTypePfirma());

					initValidations(result.getModelMap());

					result.addObject("redaction", redaction);
					result.setViewName("redaction");
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

				if (redaction.getAction().equals("enviar")) {
					List<String> errors = new ArrayList<String>();
					List<UserEnvelope> destinatarios = redactionValidator.validate(redaction, errors);

					if (!errors.isEmpty()) {
						uploadedFiles.removeAlls();
						cargarCombos(result.getModelMap());
						result.addObject("documentTypeList", applicationBO.queryDocumentTypePfirma());
						result.addObject("redaction", redaction);
						result.addObject("usersNotValid", errors);
						result.setViewName("redaction");
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

						// Se envían las notificaciones
						doNoticeAndFilters(redaction.getRequest());

					} catch (CustodyServiceException | IOException e) {
						log.error("Error al subir los documentos de la petición con hash: "	+ redaction.getRequest().getChash(), e);
						throw e;
					}

					// Firma automática
					PfRequestsDTO peticion = requestBO.queryRequest(redaction.getRequest(), user, null);
					PfRequestTagsDTO reqTag = null;
					for (PfRequestTagsDTO tag : peticion.getPfRequestsTags()) {
						if (tag.getPfUser().getPrimaryKey().equals(user.getPrimaryKey())) {
							reqTag = tag;
							break;
						}
					}

					if (peticion.isRemitterInSigners() && (peticion.isRemitterNextToSign() || !peticion.getLcascadeSign())) {

						result.addObject("idRequestToSign", reqTag.getChash());
					}

					interfazGenericaBO.procesamientoExternos(peticion);
					
				}

				// Se borran los ficheros de la carpeta temporal
				uploadedFiles.removeAlls();
				for (File file : attachedFiles) {
					file.delete();
				}

				result.setViewName("redirect:/inbox");
				return result;
				
			}else{
				return null;
			}
		} catch (RuntimeException e) {
			log.error("Error al enviar la redacción: ", e);
			result.getModelMap().addAttribute("errorMessage", Constants.MSG_GENERIC_ERROR);
			result.getModelMap().addAttribute("timeError", new Date(System.currentTimeMillis()));
			result.setViewName("error");
			return result;
		}
	}

	/**
	 * Método que sube al servidor un documento
	 * 
	 * @param redaction
	 *            Modelo de datos
	 * @param multipart
	 *            Documento
	 * @return Modelo actualizado
	 * @throws IOException
	 */
	@RequestMapping(value = "uploadFile")
	public @ResponseBody UploadFileBean subeDocumento(@ModelAttribute("redaction") RequestRedaction redaction,
			@RequestParam(value = "file") final MultipartFile multipart,
			BindingResult bindingResult, Locale locale,
			final HttpServletResponse response) throws IOException {
		UploadFileBean uploadFileBean = new UploadFileBean();
		uploadFileBean.setSuccess(false);
		String tipoObjeto = "documento";
		
		try {

			long size = multipart.getSize();
			String nombreFichero = multipart.getOriginalFilename();
			byte[] fichero = multipart.getBytes();

			
			boolean okTamaño = false;
			boolean okFirma = false;
			
			// Se comprueba el tamaño del documento
			if (isTamañoMaximoNoSuperado(size)) {
				uploadFileBean.setValidacionTamañoOk("true");
				uploadFileBean.setValidacionTamanioDescription(context.getMessage("validacionTamanio.ok",
						new String[] { tipoObjeto, multipart.getOriginalFilename() }, locale));
				okTamaño = true;
				
				//Si el documento está firmado se comprueba que el documento no lleve una firma inválida
				if (isFirmado(nombreFichero, fichero)) {
					RespuestaValidarFirma respuestaValidarFirma = null;
					try {
						respuestaValidarFirma = validarFirmaDocumento (fichero);
						if (respuestaValidarFirma.isError()) {
							String msg = respuestaValidarFirma.getMensaje();
							if(msg.contains("COD_103") 
									&& msg.contains("Se ha producido un error obteniendo el certificado firmante de la firma contenida en el diccionario de firma con nombre")){
								log.error("El certificado de la firma previa está revocado o es inválido: " + respuestaValidarFirma.getMensajeAmpliado());
								throw new Exception("El certificado de la firma previa está revocado o es inválido: " + respuestaValidarFirma.getMensajeAmpliado());
							}else{
								log.error ("Afirma está devolviendo error al validar la firma: " + respuestaValidarFirma.getMensajeAmpliado());
								throw new Exception ("Afirma está devolviendo error al validar la firma: " + respuestaValidarFirma.getMensajeAmpliado());
							}
						} else if (!respuestaValidarFirma.isValido()) {
							if(respuestaValidarFirma.getMensajeAmpliado().contains("El certificado había expirado")){
								uploadFileBean.setValidacionFirmaOk("false");
								uploadFileBean.setValidacionFirmaDescription(context.getMessage("validacionFirma.errorExp",
										new String[] { tipoObjeto, nombreFichero }, locale));
								log.error("El certificado había expirado: " + respuestaValidarFirma.getMensaje());			
							}else{
								uploadFileBean.setValidacionFirmaOk("false");
								uploadFileBean.setValidacionFirmaDescription(context.getMessage("validacionFirma.error",
										new String[] { tipoObjeto, nombreFichero }, locale));
								log.error(("Firma no valida: " + respuestaValidarFirma.getMensajeAmpliado()));
							}
						}	else {
							okFirma = true;
						}		
		
					} catch (Exception e) {
						uploadFileBean.setValidacionFirmaOk("false");
						uploadFileBean.setValidacionFirmaDescription(context.getMessage("validacionFirma.errorExecution", null, locale));
					}
				} else {
					okFirma = true;
				}
				
				try {
					if (nombreFichero.toLowerCase().contains(".pdf") && Constants.SIGN_MIMETYPE_PDF.equals(utilComponent.getMime(fichero))) {
						//Validamos que el pdf sea de una versión posterior a la 3
						PdfReader reader = new PdfReader(fichero);
						String versionPdf = String.valueOf(reader.getPdfVersion());		
						if (Integer.parseInt(versionPdf) <= 3){	
							uploadFileBean.setValidacionVersionPDFOk("false");
							uploadFileBean.setValidacionVersionPDFDescripcion(context.getMessage("validacionPdf.errorVersion",
									new String[] { versionPdf }, locale));
						}
					}
				} catch (Exception e1) {
					log.error("Error al validar la version de PDF ");
				}
				
				try {
					// validamos pdfa
//					if (Boolean.valueOf(checkboxPDFA)) {
						if (nombreFichero.toLowerCase().contains(".pdf")
								&& utilComponent.getMime(fichero).equals(Constants.SIGN_MIMETYPE_PDF)) {
							if (eeUtilMiscBO.isPDFA(fichero)) {
								uploadFileBean.setValidacionPDFAOk("true");
								uploadFileBean.setValidacionPDFADescription(context.getMessage("validacionPdfa.ok",
										new String[] { tipoObjeto, nombreFichero }, locale));
							} else {
								uploadFileBean.setValidacionPDFAOk("false");
								uploadFileBean.setValidacionPDFADescription(context.getMessage("validacionPdfa.erroPdfa",
										new String[] { tipoObjeto, nombreFichero }, locale));
							}
						} else {
							uploadFileBean.setValidacionPDFAOk("false");
							uploadFileBean.setValidacionPDFADescription(context.getMessage("validacionPdfa.erroNoPdf",
									new String[] { tipoObjeto, nombreFichero }, locale));
						}
//					}
				} catch (Exception e) {
					uploadFileBean.setValidacionPDFAOk("false");
					uploadFileBean.setValidacionPDFADescription(context.getMessage("validacionPdfa.errorExecution", null, locale));
				}

				
				
				if (okTamaño && okFirma){

					// Se crea el documento
					PfDocumentsDTO document = createDocument(multipart.getOriginalFilename(), multipart.getContentType(),
							multipart.getBytes(), true, redaction);

					// Se crea el fichero
					String fileName = multipart.getOriginalFilename() + "-" + System.currentTimeMillis() + ".data";
					UploadedDocument uploadedDocument = new UploadedDocument();
					uploadedDocument.setDocument(document);
					uploadedDocument.setName(multipart.getOriginalFilename());
					uploadedDocument.setFile(fileName);
					uploadedDocument.setSize(multipart.getSize());

					multipart.transferTo(new File(Constants.PATH_TEMP + fileName));

					Integer docId = uploadedFiles.addDocument(uploadedDocument);

					// Se envía la respuesta de éxito
					uploadFileBean.setId(docId);
					uploadFileBean.setSuccess(true);
				}
				
				return uploadFileBean;
			} else {
				// Se envía la respuesta de límite excedido
				uploadFileBean.setValidacionTamañoOk("false");
				uploadFileBean.setValidacionTamanioDescription(context.getMessage("validacionTamanio.error",
						new String[] { tipoObjeto, multipart.getOriginalFilename() }, locale));
				return uploadFileBean;
			}

		} catch (Exception e) {
			log.error("Error al adjuntar el documento " + multipart.getOriginalFilename(), e);
			uploadFileBean.setApplicationError("true");
			uploadFileBean.setApplicationErrorDescription("Se ha producido un errror al subir al servidor el documento "+multipart.getOriginalFilename());
			return uploadFileBean;
		}
		 catch (Error e) {
			log.error("Error al adjuntar el documento " + multipart.getOriginalFilename(), e);
			uploadFileBean.setApplicationError("true");
			uploadFileBean.setApplicationErrorDescription("Se ha producido un errror al subir al servidor el documento "+multipart.getOriginalFilename());
			return uploadFileBean;
		}
	}

	private boolean isFirmado (String nombreFichero, byte[] fichero) {
		String tipoFirma = null;
		try {
			tipoFirma = signDataUtil.checkIsSign(fichero);
		} catch (Exception e) {
			log.error("Error al procesar el documento para saber si es firma " + nombreFichero, e);
			throw e;
		}
		return tipoFirma != null;
	}

	/**
	 * Método que sube al servidor un anexo
	 * 
	 * @param redaction
	 *            Modelo de datos
	 * @param multipart
	 *            Anexo
	 * @return Modelo actualizado
	 * @throws IOException
	 */
	@RequestMapping(value = "uploadAnnex")
	public void subeAnexo(@ModelAttribute("redaction") RequestRedaction redaction,
			@RequestParam(value = "file") final MultipartFile multipart, final HttpServletResponse response)
					throws IOException {

		try {
			// Se crea el documento
			PfDocumentsDTO document = createDocument(multipart.getOriginalFilename(), multipart.getContentType(),
					multipart.getBytes(), false, redaction);

			// Se crea el fichero
			String fileName = multipart.getOriginalFilename() + "-" + System.currentTimeMillis() + ".data";
			UploadedDocument uploadedAnnex = new UploadedDocument();
			uploadedAnnex.setDocument(document);
			uploadedAnnex.setName(multipart.getOriginalFilename());
			uploadedAnnex.setFile(fileName);
			uploadedAnnex.setSize(multipart.getSize());

			multipart.transferTo(new File(Constants.PATH_TEMP + fileName));

			Integer annexId = uploadedFiles.addAnnex(uploadedAnnex);

			// Se envía la respuesta de éxito
			response.setContentType("application/json");
			response.getWriter().write("{\"id\":" + annexId + ",\"success\":true}");

		} catch (Exception e) {
			log.error("Error al subir el documento adjunto " + multipart.getOriginalFilename(), e);
			response.setContentType("application/json");
			response.getWriter().write("{\"success\":false,\"error\":\"applicationError\"}");
		} catch (Error e) {
			log.error("Error al adjuntar el documento " + multipart.getOriginalFilename(), e);
			response.setContentType("application/json");
			response.getWriter().write("{\"success\":false,\"error\":\"applicationError\"}");
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
	@RequestMapping(value = "deleteDocument")
	public ModelAndView borraDocumento(@ModelAttribute("redaction") RequestRedaction redaction,
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
		result.addObject("redaction", redaction);
		result.setViewName("redaction");

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
	@RequestMapping(value = "updateDocumentType")
	public ModelAndView actualizarTipoDocumento(@ModelAttribute("redaction") RequestRedaction redaction,
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
		result.addObject("redaction", redaction);
		result.setViewName("redaction");

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
	@RequestMapping(value = "selectUsers")
	public @ResponseBody UserSelection seleccionarDestinatarios(@RequestParam(value = "signers") String signersBase64,
			@RequestParam(value = "signLinesConfig") String signLinesConfig,
			@RequestParam(value = "signLinesAccion") String signLinesAccion,
			@RequestParam(value = "userNameFilter") final String userNameFilter,
			@RequestParam(value = "userTypeFilter") final String userTypeFilter,
			@RequestParam(value = "userSeatFilter") final String userSeatFilter,
			@RequestParam(value = "portafirmasFilter") final String portafirmasFilter,
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
		
		cargarPortafirmas(userSelection);

		// Se cargan los usuarios a mostrar
		if (firstInvocation == null || !firstInvocation.booleanValue()) {
			requestBO.loadUsersToPick(userSelection, user, codSede, signers, signLinesConfig, userNameFilter,
					userTypeFilter, userSeatFilter, portafirmasFilter, firstInvocation, signLinesAccion);
		}
		return userSelection;
	}

	@RequestMapping(value = "autocompleteSigners")
	public @ResponseBody List<UserAutocomplete> autocompleteSigners(@RequestParam(value = "term") final String term) {

		List<UserAutocomplete> results = new ArrayList<UserAutocomplete>();

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
		List<AbstractBaseDTO> usersBusqueda = requestBO.queryUsersCompleteWithoutVisibles(values[values.length - 1].trim(), codSede);
		
		
		// Se filtran los usuarios si el usuario tiene restringidos los destinatarios
		List<AbstractBaseDTO> users = new ArrayList<AbstractBaseDTO>();
		List<PfUsersDTO> validUserList = restrictionBO.queryUserRestrict(user);
		if(!validUserList.isEmpty()){
			for (AbstractBaseDTO userAux : usersBusqueda){
				PfUsersDTO userDTO = (PfUsersDTO) userAux;
				boolean encontrado = false;
				for (PfUsersDTO userValidDTO : validUserList){
					if (userDTO.getPrimaryKey().equals(userValidDTO.getPrimaryKey())){
						encontrado = true;
					}
				}
				if (encontrado){
					users.add(userAux);
				}
			}
		} else {
			users.addAll(usersBusqueda);
		}

		// Se filtran los usuarios eliminando los que sean cargos que no tengan un usuario vigente
		List<AbstractBaseDTO> usersSinCargosNoVigentes = new ArrayList<AbstractBaseDTO>();
		for (AbstractBaseDTO dto : users) {
			PfUsersDTO autocompletedUser = (PfUsersDTO) dto;
			if (autocompletedUser.isJob()){
				List<AbstractBaseDTO> usersJobsAbstract = baseDAO.queryListOneParameter("request.queryUsersFromJob", "job", autocompletedUser);

				for (AbstractBaseDTO userJobsAbstract : usersJobsAbstract) {
					PfUsersJobDTO userJob = (PfUsersJobDTO) userJobsAbstract;
					Date fechaActual = new Date();
					if ((userJob.getFstart() == null || userJob.getFstart().before(fechaActual)) &&
							((userJob.getFend() == null) || (userJob.getFend() != null && userJob.getFend().after(fechaActual)))) {					
						usersSinCargosNoVigentes.add(autocompletedUser);
						break;
					}
				}
			} else {
				usersSinCargosNoVigentes.add(autocompletedUser);
			}
		}

		// Se convierten los resultados
		for (AbstractBaseDTO dto : usersSinCargosNoVigentes) {
			results.add(new UserAutocomplete((PfUsersDTO) dto));
		}
		return results;
	}

	/**
	 * Método que devuelve la lista de tipos de documentos
	 * 
	 * @return Lista de tipos de documentos
	 */
	@RequestMapping(value = "getDocumentTypes")
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
	 * Método que devuelve la lista de acciones de un firmante
	 * 
	 * @return Lista de acciones
	 */
	@RequestMapping(value = "getListaAccionesFirmante")
	public @ResponseBody List<DocumentType> getListaAccionesFirmante() {
		List<DocumentType> result = new ArrayList<DocumentType>();
		List<AbstractBaseDTO> accionesType = applicationBO.queryAccionFirmante();
		for (AbstractBaseDTO dto : accionesType) {
			PfAccionFirmanteDTO accionDTO = (PfAccionFirmanteDTO) dto;
			DocumentType docType = new DocumentType();
			docType.setId(accionDTO.getCcodigo());
			docType.setName(accionDTO.getCdescription());
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

	private void cargarPortafirmas(UserSelection userSelection) {
		List<Portafirmas> portafirmaList = portafirmasRepository.obtenerTodos();
		List<PortafirmasEnvelope> pfList = new LinkedList<PortafirmasEnvelope>();
		for (Portafirmas portafirmas : portafirmaList) {
			PortafirmasEnvelope pfEnv = new PortafirmasEnvelope();
			pfEnv.setIdPortafirmas(portafirmas.getIdPortafirmas().toString());
			pfEnv.setNombre(portafirmas.getNombre());
			pfList.add(pfEnv);
		}
		userSelection.setPortafirmasList(pfList);
	}

	/**
	 * Crea un documento firmable con el tipo de documento asignado al de por
	 * defecto, el mime se asigna o por la extensi&oacute;n si existe o si no
	 * por el fileMime que se le pasa como par&aacute;metro
	 * 
	 * @param fileName
	 *            nombre del documento
	 * @param fileMime
	 *            tipo de contenido del documento
	 * @param file
	 *            el documento firmable
	 * @throws Exception
	 */
	private PfDocumentsDTO createDocument(String fileName, String fileMime, byte[] file, boolean firmable,
			RequestRedaction redaction) throws Exception {
		PfDocumentsDTO documentDTO = new PfDocumentsDTO();

		// Trunca la ruta del nombre del documento
		documentDTO.setDname(Util.getInstance().getNameFile(fileName));

		// Si el documento tiene extensión
		if (Util.getInstance().hasExtension(fileName)) {
			// Ponemos el tipo de documento en el DTO a partir de la extesión
			documentDTO.setDmime(Util.getInstance().getMimeTypeOf(fileName));
			// Si no tiene extensión
		} else {
			// Ponemos el tipo de documento en el DTO a partir del fileMime
			documentDTO.setDmime(fileMime);
		}
		// Indica que el documento es firmable
		documentDTO.setLsign(firmable);
		PfDocumentTypesDTO documentType = new PfDocumentTypesDTO();
		PfDocumentTypesDTO documentTypeDefault = applicationBO.queryDocumentTypeGeneric();
		documentType.setPrimaryKey(documentTypeDefault.getPrimaryKey());
		documentDTO.setPfDocumentType(documentType);
		String tipoFirma = null;
		try {
			tipoFirma = signDataUtil.checkIsSign(file);
		} catch (Exception e) {
			log.error("Error al procesar el documento para saber si es firma " + fileName, e);
			throw e;
		}

		if (tipoFirma != null) {
			documentDTO.setLissign(true);
			if ("enveloped".equals(tipoFirma)) {
				redaction.setEsEnveloped(true);
			}
		} else {
			documentDTO.setLissign(false);
		}

		return documentDTO;
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

	/**
	 * Método que envía las notificaciones a los usuarios al redactar la
	 * petición
	 * 
	 * @param request
	 *            Petición
	 */
	private void doNoticeAndFilters(PfRequestsDTO request) {

		// Aplica las autorizaciones de los firmantes correspondientes
		authorizationBO.applyAuthorizationsTransactional(request);

		// Send notice
		noticeBO.noticeNewRequest(request, applicationVO.getEmail(), applicationVO.getSMS());

		// Enviamos notificación a los validadores
		noticeBO.noticeNewRequestValidador(request, applicationVO.getEmail(), applicationVO.getSMS());

	}

	private boolean isTamañoMaximoNoSuperado(long size) {
		long tamañoMaximoDocumento = Long.parseLong(applicationBO.queryMaxDocumentSizeParameter().getTvalue())
				* (1024 * 1024);
		if (size < tamañoMaximoDocumento) {
			return true;
		} else {
			return false;
		}
	}

	/** Método para comprobar si un documento se encuentra firmado.
	 * @param fichero
	 * @return
	 * @throws Exception
	 */
	private RespuestaValidarFirma validarFirmaDocumento (byte[] fichero) throws Exception{

		RespuestaValidarFirma respuestaValidarFirma = null;		

		try {
			long idConfig = configManagerBO.getDefaultConfigurationId();
			respuestaValidarFirma = afirma5BO.validarFirma(fichero, null, null, null, null, idConfig);
		} catch (Exception e) {
			log.error("Error al procesar el documento para validar una firma", e);
			throw e;
		}

		return respuestaValidarFirma;
	}

	/**
	 * Método que carga los nombres de los firmantes según su id
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(value = "getSigners")
	public @ResponseBody List<UserAutocomplete> getSigners(
			@RequestParam(value = "signers") final String signers, final HttpServletResponse response) throws Exception {

		List<UserAutocomplete> result = new ArrayList<UserAutocomplete>();

		try{
			if (!signers.isEmpty()){
				String [] signersList = signers.split(",");
				for (String userSigner : signersList) {			
					PfUsersDTO user = (PfUsersDTO) redactionBO.getUser(userSigner);
					result.add(new UserAutocomplete((PfUsersDTO) user));
				}
			}
		}catch (NumberFormatException e){
			response.getWriter().write("{\"status\": \"error\", \"log\": \"" + String.format(messages.getProperty("receiverNotFound")) + "\"}");
			throw new Exception (String.format(messages.getProperty("receiverNotFound")));
		}

		return result;
	}
}

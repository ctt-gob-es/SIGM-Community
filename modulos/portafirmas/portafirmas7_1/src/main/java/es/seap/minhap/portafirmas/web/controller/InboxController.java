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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.codec.binary.Base64OutputStream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.propertyeditors.CustomDateEditor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import es.guadaltel.framework.authenticator.exception.AuthenticatorException;
import es.guadaltel.framework.authenticator.impl.util.Base64Coder;
import es.seap.minhap.portafirmas.actions.ApplicationVO;
import es.seap.minhap.portafirmas.annotation.AuditSimulateAnnotation;
import es.seap.minhap.portafirmas.annotation.AuditSimulateMapping;
import es.seap.minhap.portafirmas.business.ApplicationBO;
import es.seap.minhap.portafirmas.business.BinaryDocumentsBO;
import es.seap.minhap.portafirmas.business.HistoricRequestBO;
import es.seap.minhap.portafirmas.business.NoticeBO;
import es.seap.minhap.portafirmas.business.PassBO;
import es.seap.minhap.portafirmas.business.ReportBO;
import es.seap.minhap.portafirmas.business.RequestBO;
import es.seap.minhap.portafirmas.business.SignBO;
import es.seap.minhap.portafirmas.business.TagBO;
import es.seap.minhap.portafirmas.business.TemplateBO;
import es.seap.minhap.portafirmas.business.UserParameterBO;
import es.seap.minhap.portafirmas.business.administration.ThemeBO;
import es.seap.minhap.portafirmas.business.administration.UserAdmBO;
import es.seap.minhap.portafirmas.business.beans.ValidateCertificateResponse;
import es.seap.minhap.portafirmas.business.configuration.LabelBO;
import es.seap.minhap.portafirmas.business.configuration.ValidatorUsersConfBO;
import es.seap.minhap.portafirmas.business.metadata.ApplicationMetadataBO;
import es.seap.minhap.portafirmas.business.ws.CertificateBO;
import es.seap.minhap.portafirmas.business.ws.EEUtilMiscBO;
import es.seap.minhap.portafirmas.business.ws.EEUtilVisBO;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfApplicationsDTO;
import es.seap.minhap.portafirmas.domain.PfDocelwebRequestSpfirmaDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentsDTO;
import es.seap.minhap.portafirmas.domain.PfGroupsDTO;
import es.seap.minhap.portafirmas.domain.PfMessagesDTO;
import es.seap.minhap.portafirmas.domain.PfRequestTagsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestTemplatesDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsTextDTO;
import es.seap.minhap.portafirmas.domain.PfSignLinesDTO;
import es.seap.minhap.portafirmas.domain.PfSignersDTO;
import es.seap.minhap.portafirmas.domain.PfSignsDTO;
import es.seap.minhap.portafirmas.domain.PfTagsDTO;
import es.seap.minhap.portafirmas.domain.PfUserTagsDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.domain.PfUsersMessageDTO;
import es.seap.minhap.portafirmas.domain.PfValidatorApplicationDTO;
import es.seap.minhap.portafirmas.domain.RequestTagListDTO;
import es.seap.minhap.portafirmas.exceptions.CertificateException;
import es.seap.minhap.portafirmas.exceptions.DocumentCantBeDownloadedException;
import es.seap.minhap.portafirmas.exceptions.PortafirmasException;
import es.seap.minhap.portafirmas.exceptions.InvalidSignException;
import es.seap.minhap.portafirmas.exceptions.InvalidSignKOException;
import es.seap.minhap.portafirmas.exceptions.TimeStampingException;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.DateComponent;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.utils.UtilComponent;
import es.seap.minhap.portafirmas.utils.document.CustodyServiceException;
import es.seap.minhap.portafirmas.utils.eni.ComboUtils;
import es.seap.minhap.portafirmas.utils.metadata.MetadataConverter;
import es.seap.minhap.portafirmas.utils.notice.exception.NoticeException;
import es.seap.minhap.portafirmas.utils.previsualizacion.PrevisualizacionBO;
import es.seap.minhap.portafirmas.web.beans.DocumentEni;
import es.seap.minhap.portafirmas.web.beans.Email;
import es.seap.minhap.portafirmas.web.beans.FileAttachedDTO;
import es.seap.minhap.portafirmas.web.beans.Inbox;
import es.seap.minhap.portafirmas.web.beans.Message;
import es.seap.minhap.portafirmas.web.beans.Paginator;
import es.seap.minhap.portafirmas.web.beans.Request;
import es.seap.minhap.portafirmas.web.beans.SignLine;
import es.seap.minhap.portafirmas.web.beans.Signer;
import es.seap.minhap.portafirmas.web.beans.UserAutocomplete;
import es.seap.minhap.portafirmas.web.beans.UserTag;
import es.seap.minhap.portafirmas.web.beans.signature.RequestSignature;
import es.seap.minhap.portafirmas.web.beans.signature.RequestSignatureConfig;
import es.seap.minhap.portafirmas.ws.docelweb.DocelwebConstants;
import es.seap.minhap.portafirmas.ws.exception.PfirmaException;
import es.seap.minhap.portafirmas.ws.inside.GInsideConfigManager;

@Controller
@RequestMapping("inbox")
public class InboxController {

	protected final Log log = LogFactory.getLog(getClass());

	@Autowired
	private RequestBO requestBO;

	@Autowired
	private TagBO tagBO;

	@Autowired
	private HistoricRequestBO historicRequestBO;

	@Autowired
	private LabelBO labelBO;

	@Autowired
	private TemplateBO templateBO;

	@Autowired
	private MessageSource messageSource;

	@Autowired
	private ThemeBO themeBO;
	
	@Autowired
	private ApplicationVO applicationVO;

	@Autowired
	private SignBO signBO;

	@Autowired
	private PassBO passBO;

	@Autowired
	private NoticeBO noticeBO;

	@Autowired
	private CertificateBO certificateBO;

	@Autowired
	private UserParameterBO userParamBO;

	@Autowired
	private UserAdmBO userAdmBO;

	@Resource(name = "messageProperties")
	private Properties messages;

	@Autowired
	ApplicationMetadataBO applicationMetadataBO;

	@Autowired
	MetadataConverter metadataConverter;

	@Autowired
	DateComponent dateComponent;

	@Autowired
	BinaryDocumentsBO binaryDocumentsBO;

	@Autowired
	private ReportBO reportBO;

	@Autowired
	GInsideConfigManager ginsideConfigManager;

	@Autowired
	private UtilComponent util;

	@Autowired
	private EEUtilVisBO eeutilVisBO;
	
	@Autowired
	private EEUtilMiscBO eeUtilMiscBO;
	
	@Autowired
	private ApplicationBO applicationBO;
	
	@Autowired
	private PrevisualizacionBO previsualizacionBO;
	
	@Autowired
	private ValidatorUsersConfBO validatorUsersConfBO;
	
	@Autowired
	private BaseDAO baseDAO;
	
	/**[DipuCR-Agustin] Usamos un espacio como valor por defecto igual que el portafirmas movil. */
	private static final String DEFAULT_REASON = "Rechazado sin indicar motivo, contacte con el firmante"; //$NON-NLS-1$
	
	@InitBinder
	public void initBinder(WebDataBinder binder) {
		SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
		binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, true));
	}

	/**
	 * Método que carga inicialmente, la bandeja de peticiones
	 * 
	 * @param model
	 *            Modelo de datos
	 * @return Página direccionada
	 */
	@RequestMapping(method = RequestMethod.GET)
	@AuditSimulateAnnotation(nombreOperacion="inicializabandejaentrada",datosSimulado = {@AuditSimulateMapping()})
	public ModelAndView initForm(ModelAndView model) {

		try{
			log.debug("Se inicializa la bandeja de entrada");
			// Se recupera el usuario autenticado
			UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
			PfUsersDTO user = authorization.getUserDTO();
			String serialNumber = authorization.getSerialNumber();

			List <PfValidatorApplicationDTO> validatorsByApp = validatorUsersConfBO.queryValidatorsByValidatorList(user);

			// Se carga la bandeja de entrada con las pendientes
			Inbox inbox = new Inbox();

			// Se carga el grupo (a nulo, si no lo hay)
			if (authorization.getGroup() == null) {
				inbox.setRequestBarSelected(Constants.MESSAGES_UNRESOLVED);
			} else {
				inbox.setRequestBarSelected(Constants.MESSAGES_GROUP_SENT);
			}
			inbox.setRequestPagination("none");

			// Si el usuario es el principal y tiene validadores globales se muestran solo las validadas
			PfUsersDTO principal = (PfUsersDTO) authorization.getPrincipal();
			if (user.getPrimaryKeyString().equals(principal.getPrimaryKeyString()) && (
					!user.getValidadores().isEmpty() || !validatorsByApp.isEmpty())) {
				inbox.setHasValidator(true);
			} else {
				inbox.setHasValidator(false);
			}
			
			// se carga el tamaño de página personalizado o por defecto
			loadCustomPageSize(inbox, user);

			// Se cargan las peticiones en el modelo
			loadInbox(inbox, authorization);

			// Si tiene mensajes sin leer se muestra una advertencia
			if (authorization.getGroup() == null && inbox.getNumMessagesUnresolved() != null
					&& Integer.valueOf(inbox.getNumMessagesUnresolved()) > 0) {
				inbox.setHasMessages(true);
			} else {
				inbox.setHasMessages(false);
			}

			// Se guarda el número de serie del certificado del usuario
			if (!"".equals(serialNumber) && serialNumber != null) {
				inbox.setSerialNumber(serialNumber);
			}

			// Se carga el listado de etiquetas del usuario
			loadUserLabels(inbox);

			// Se carga el listado de plantillas del usuario
			loadUserTemplates(inbox);

			// Se carga el listado de aplicaciones del filtro
			loadApplicationInbox(inbox);

			inbox.setMonthList(util.getMonthsExtended());
			inbox.setYearList(util.getYears());

			// Se guarda el bean con los datos
			model.addObject("inbox", inbox);		
			model.setViewName("inbox");

		} catch (Exception e) {
			log.error("Error al cargar la bandeja de entrada", e);
			model.addObject("errorMessage", Constants.MSG_GENERIC_ERROR);
			model.addObject("timeError", new Date(System.currentTimeMillis()));
			model.setViewName("error");
		}

		return model;
	}

	/**
	 * Carga el tamaño personalizado de pagina
	 * 
	 * @param inbox
	 * @param user
	 */
	private void loadCustomPageSize(Inbox inbox, PfUsersDTO user) {
		String customPageSize = themeBO.loadUserPageSize(user);
		if (Util.esVacioONulo(customPageSize)) {
			customPageSize = themeBO.loadAdmPageSize();
		}
		if (!Util.esVacioONulo(customPageSize)) {
			inbox.setCustomPageSize(Integer.valueOf(customPageSize));
		}
	}

	/**
	 * Método que carga los datos de una petición seleccionada
	 * 
	 * @param inbox
	 *            Modelo de datos
	 * @param requestTagId
	 *            Identificador de la petición
	 * @return Modelo de datos
	 * @throws Exception
	 */
	@RequestMapping(value = "/loadRequest", method = RequestMethod.GET)
	public ModelAndView loadRequest(@ModelAttribute("inbox") Inbox inbox,
			@ModelAttribute("requestBarSelected") String requestBarSelected,
			@ModelAttribute("currentRequest") String currentRequest, @ModelAttribute("pageSize") String pageSize,
			@ModelAttribute("inboxSize") String inboxSize, @ModelAttribute("currentPage") String currentPage,
			@RequestParam("requestTagId") String requestTagId) throws Exception {
		try {
			ModelAndView result = new ModelAndView();

			// Se recupera el usuario autenticado
			UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext()
					.getAuthentication();
			PfUsersDTO user = authorization.getUserDTO();
			PfGroupsDTO grupo = authorization.getGroup();

			// Se cargan los datos de la petición seleccionada
			PfRequestTagsDTO requestTagDTO = requestBO.queryRequestTagByHash(requestTagId);
			if (inbox.getRequestBarSelected().equals(Constants.MESSAGES_INVITED)) {
				requestTagDTO = requestBO.queryRequestTagByHashInvited(requestTagId);
			}
			PfRequestsDTO requestDTO = requestTagDTO.getPfRequest();
			
			Request request = new Request();
			request.setcCreated(requestDTO.getCcreated());
			request.setRequestTagHash(requestTagId);
			request.setPrimaryKey(requestDTO.getPrimaryKey());
			request.setApplication(requestDTO.getPfApplication().getDapplication());
			PfDocelwebRequestSpfirmaDTO docelRequestDTO = (PfDocelwebRequestSpfirmaDTO) baseDAO.queryElementOneParameter(DocelwebConstants.QUERY_DOCEL_PFIRMA_REQUEST_BY_HASH, DocelwebConstants.QUERY_PARAM_FILE_HASH, requestDTO.getChash());
			if (docelRequestDTO != null && docelRequestDTO.getdSenderName()!= null && !"".equals(docelRequestDTO.getdSenderName())) {
				requestDTO.setRemitenteInterfazGenerica(docelRequestDTO.getdSenderName());
			}
			

			if (requestDTO.getRemitenteInterfazGenerica()!=null && !"".equals(requestDTO.getRemitenteInterfazGenerica())) {
				request.setSender(requestDTO.getRemitters() + " ("+requestDTO.getRemitenteInterfazGenerica()+")");	
			} else {
				request.setSender(requestDTO.getRemitters());
			}
			
			request.setSignConfig(requestDTO.getPfApplication().getPfConfiguration().getCconfiguration());
			request.setlInvited(requestDTO.getLinvited());
			request.setlAccepted(requestDTO.getLaccepted());
			request.setAnySign(requestDTO.isAnySign());

			if (requestDTO.getFentry() != null) {
				request.setStartDate(Util.getInstance().dateToString(requestDTO.getFentry(), "dd/MM/yyyy HH:mm:ss"));
			}
			if (requestDTO.getFmodified() != null) {
				request.setModifiedDate(
						Util.getInstance().dateToString(requestDTO.getFmodified(), "dd/MM/yyyy HH:mm:ss"));
			}
			if (requestDTO.getFexpiration() != null) {
				request.setExpirationDate(
						Util.getInstance().dateToString(requestDTO.getFexpiration(), "dd/MM/yyyy HH:mm:ss"));
			}
			request.setSubject(requestDTO.getDsubject());
			request.setReference(requestDTO.getDreference());

			// Se obtiene el nivel de importancia
			request.setImportanceLevel(requestDTO.getPfImportance());

			// Se obtiene el texto de la petición
			PfRequestsTextDTO requestText = requestBO.queryRequestText(requestDTO);
			request.setText(requestText.getTrequest());

			// Se obtiene si es en cascada o paralelo
			request.setSignType(requestDTO.getLcascadeSign() ? messages.getProperty(Constants.SIGN_TYPE_CASCADE)
					: messages.getProperty(Constants.SIGN_TYPE_PARALLEL));

			// Se obtienen los firmantes
			PfRequestsDTO requestSignLines = requestBO.queryRequestSignLines(requestDTO.getChash());
			if (request.islInvited() && !request.islAccepted()) {
				requestSignLines = requestBO.queryRequestSignLinesInvited(requestDTO.getChash());
			}

			List<SignLine> signLines = new ArrayList<SignLine>();
			for (PfSignLinesDTO signLineDTO : requestSignLines.getPfSignsLinesList()) {
				SignLine signLine = new SignLine();
				List<Signer> signers = new ArrayList<Signer>();
				// Se recuperan los firmantes de la línea de firma
				boolean isTerminate = false;
				for (PfSignersDTO signerDTO : signLineDTO.getPfSigners()) {
					Signer signer = new Signer();
					signer.setPfUser(signerDTO.getPfUser());
					// Se recupera el estado de la petición por petición,
					// usuario y linea de firma
					PfRequestTagsDTO stateRequestTag = tagBO.queryStateUserSignLine(requestDTO, signerDTO.getPfUser(),
							signLineDTO);
					String status = stateRequestTag.getPfTag().getCtag();
					PfUsersDTO usuarioValidador = stateRequestTag.getPfUsuarioValidador();
					if (usuarioValidador != null && user.getValidadores() != null && user.getValidadores().contains(usuarioValidador)){
						Signer validador = new Signer();
						validador.setPfUser(usuarioValidador);
						validador.setStateTag(stateRequestTag.getPfTag());
						validador.setEsValidador(true);
						signers.add(validador);
					}
					
					isTerminate = status.equals(Constants.C_TAG_SIGNED) || status.equals(Constants.C_TAG_PASSED);
					// Se deja cargado el estado para cada linea de firma y cada
					// usuario por si fuera necesario en el futuro.
					signer.setStateTag(stateRequestTag.getPfTag());
					signer.setEsValidador(false);
					signers.add(signer);
				}
				signLine.setTerminate(isTerminate);
				signLine.setSigners(signers);
				signLine.setType(signLineDTO.getCtype());
				signLines.add(signLine);
			}
			request.setSignLines(signLines);

			if (request.islInvited()) {
				request.setInvitedUser(requestDTO.getInvitedUser());
			}

			if (!authorization.isSimulador()) {
				// Se marca la petición como leída
				if (Constants.MESSAGES_SENT.equals(requestBarSelected)
						|| Constants.MESSAGES_SENT_FINISHED.equals(requestBarSelected)
						|| Constants.MESSAGES_GROUP_SENT.equals(requestBarSelected)
						|| Constants.MESSAGES_GROUP_SENT_FINISHED.equals(requestBarSelected)
						|| Constants.MESSAGES_GROUP_EXPIRED.equals(requestBarSelected)) {
					// En el caso de que se haya seleccionado la bandeja de
					// Enviadas solo se marca como leída
					// si el usuario de la requestTag es el mismo que el usuario
					// logueado en Portafirmas
					if (requestTagDTO.getPfUser().getPrimaryKey() != null
							&& requestTagDTO.getPfUser().getPrimaryKey().equals(user.getPrimaryKey())) {
						tagBO.changeStateToRead(requestTagDTO, user);
						requestBO.markAsReadForRemitter(requestTagDTO.getPfRequest(), user); 
					} else {
						// Esta logica solo aplica para las bandejas del
						// remitente
						if (Constants.MESSAGES_SENT.equals(requestBarSelected)
								|| Constants.MESSAGES_SENT_FINISHED.equals(requestBarSelected)) {
							requestBO.markAsReadForRemitter(requestTagDTO.getPfRequest(), user);
						}
					}
				} else {
					tagBO.changeStateToRead(requestTagDTO, user);
				}
			}
			// Se obtiene la etiqueta de estado
			PfTagsDTO stateTag = new PfTagsDTO();
			;
			if (requestTagDTO.getStateUser() != null) {
				stateTag = requestTagDTO.getStateUser().getPfTag();
			} else if (requestTagDTO.getStateJob() != null) {
				stateTag = requestTagDTO.getStateJob().getPfTag();
			}
			request.setStateTag(stateTag);

			// Se obtiene el tipo de petición
			request.setPass(stateTag.isPass());

			// Se obtienen las etiquetas de usuario
			List<AbstractBaseDTO> userTagsListDTO = requestBO.queryUserTagsForRequest(requestDTO, user, grupo);
			List<UserTag> userTagsList = new ArrayList<>();
			for (AbstractBaseDTO userTagDTO : userTagsListDTO) {
				UserTag userTag = new UserTag();
				PfUserTagsDTO ut = (PfUserTagsDTO) userTagDTO;
				userTag.setId(ut.getPfTag().getPrimaryKey());
				userTag.setName(ut.getPfTag().getCtagCapitalized());
				userTag.setColour(ut.getCcolor());
				userTagsList.add(userTag);
			}
			request.setUserTags(userTagsList);

			// Se obtienen los documentos y anexos de la petición
			// Se ordena la lista de documentos adjuntos 17/08/2016
			List<PfDocumentsDTO> listaOrdenadaDocumentos = requestDTO.getPfDocumentsList();
			Collections.sort(listaOrdenadaDocumentos);
			request.setDocumentList(listaOrdenadaDocumentos);

			// El código del ámbito se obtiene de cualquiera de los documentos y
			// anexos. Lo tomamos del primero que es el obligatorio
			request.setcScope(requestDTO.getPfDocumentsList().get(0).getPfDocumentScope().getPrimaryKey());
			request.setAnnexList(requestDTO.getPfAttachedDocumentsList(user.getPrimaryKey()));

			// Se obtienen los comentarios
			request.setCommentsList(requestDTO.getPfCommentsList());

			// Se obtiene la información de histórico
			request.setHistoricListDTO(historicRequestBO.queryHistoricListTranslate(requestDTO));

			// Se guarda el bean con los datos
			inbox.setSelectedRequest(request);
			inbox.setRequestBarSelected(requestBarSelected);

			if (Integer.parseInt(inboxSize) < Integer.parseInt(pageSize)) {
				pageSize = inboxSize;
			}

			// Se cargan los catálogos necesarios cuando se muestra el detalle
			// de una petición
			loadRedactionCatalogues(result.getModelMap());

			// Se cargan los combos de los metadatos ENI
			loadCombosMetadatos(result.getModelMap(), requestDTO.getPfApplication());

			DocumentEni documentEni = new DocumentEni();
			documentEni.setOrganoList(new ArrayList<String>());

			// indicamos si se muestran los botones de ENI si la aplicación está
			// bien configurada
			setVisibleEniButtons(result, requestDTO.getPfApplication());

			// Se pasan los parámetros en la respuesta
			result.addObject("inbox", inbox);
			result.addObject("currentRequest", currentRequest);
			result.addObject("pageSize", pageSize);
			result.addObject("inboxSize", inboxSize);
			result.addObject("currentPage", currentPage);
			result.addObject("currentRequestHash", requestDTO.getChash());
			result.addObject("documentEni", documentEni);
			result.addObject("email", new Email());
			result.setViewName("inbox_msgBox");
			return result;
		} catch (Exception e) {
			log.error("Error al cargar la petición: ", e);
			throw e;
		}
	}

	private void setVisibleEniButtons(ModelAndView retorno, PfApplicationsDTO aplicacion) {
		Map<String, String> params = ginsideConfigManager
				.cargarConfiguracion(aplicacion.getPfConfiguration().getPrimaryKey());
		if (params == null) {
			retorno.addObject("visibleEniButtons", false);
		} else {
			retorno.addObject("visibleEniButtons", true);
		}
	}

	/**
	 * @param model
	 */
	private void loadRedactionCatalogues(ModelMap model) {
		// Se cargan los formatos de firma
		model.addAttribute("signatureConfigurations", requestBO.loadSignatureFormats());

		// Se cargan los niveles de importancia
		model.addAttribute("importanceLevelList", requestBO.queryImportanceLevels());

	}

	/**
	 * Método que obtiene las peticiones seleccionadas para firmar y prepara su
	 * configuración de firma
	 * 
	 * @param inbox
	 *            Modelo del formulario
	 * @param requestsIds
	 *            Identificadores de las peticiones a firmar
	 * @return Peticiones a firmar
	 */
	@RequestMapping(value = "/loadRequestsToSign", method = RequestMethod.GET)
	public @ResponseBody List<Request> loadRequestsToSign(@ModelAttribute("inbox") Inbox inbox,
			@RequestParam("requestsIds") String[] requestsIds) {

		UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
		PfUsersDTO usuarioConectado = authorization.getUserDTO();
		List<Request> requestsToSign = new ArrayList<Request>();

		for (int i=0; i < requestsIds.length; i++) {
			// Se cargan los datos de la petición seleccionada
			PfRequestTagsDTO requestTagDTO = requestBO.queryRequestTagByHash(requestsIds[i]);

			RequestSignatureConfig signatureConfig = signBO.getSignatureConfiguration(requestTagDTO, usuarioConectado);
			signBO.comprobarFirmaTrifasica(signatureConfig);

			// Se devuelve la información a mostrar de cada petición
			PfRequestsDTO requestDTO = requestTagDTO.getPfRequest();
			Request requestToSign = new Request();
			requestToSign.setRequestTagHash(requestsIds[i]);
			requestToSign.setSubject(requestDTO.getDsubject());
			requestToSign.setSender(requestDTO.getRemitters());
			requestToSign.setSignatureConfig(signatureConfig);			
			requestToSign.setlSignMarked(requestDTO.getlSignMarked());
			requestToSign.setParalela(!requestDTO.getLcascadeSign());

			/**
			 * Parte nueva (Firmas checkbox)
			 */
			requestToSign.setParamUser(userParamBO.rescueParamUser(usuarioConectado.getPrimaryKey()));
			requestsToSign.add(requestToSign);
		}

		return requestsToSign;
	}

	/**
	 * Método que valida el certificado seleccionado por el usuario para firmar
	 * 
	 * @param certificadoB64
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/obtenerNumeroSerie")
	public void obtenerNumeroSerie(@ModelAttribute("inbox") Inbox inbox,
			@RequestParam(value = "certificadoB64") final String certificadoB64, final HttpServletResponse response,
			final HttpServletRequest request) throws IOException {

		boolean errorEnFirma = false;
		String logErrorEnFirma = "No se ha podido validar el certificado. "
				+ "Por favor, compruebe que la opción &quot;Activar el contenido Java en el explorador&quot;"
				+ " de la pestaña &quot;Seguridad&quot; de la consola Java está activa.";

		if (inbox.getSerialNumber() == null || inbox.getSerialNumber().contentEquals("")) {
			try {
				Base64Coder coder = new Base64Coder();
				String certificado = new String(coder.decodeBase64(certificadoB64.getBytes()));
				validarCertificado(certificado, inbox, request);
			} catch (AuthenticatorException e) {
				log.error("Error en firma: ", e);
				errorEnFirma = true;
				logErrorEnFirma = e.getMessage();
			} catch (CertificateException ce) {
				log.error(ce.getMessage(), ce);
				errorEnFirma = true;
				if (ce.getCode().equals(CertificateException.COD_001)) {
					logErrorEnFirma = CertificateException.MESSAGE_001;
				} else if (ce.getCode().equals(CertificateException.COD_002)) {
					logErrorEnFirma = CertificateException.MESSAGE_002;
				}

			} catch (Exception e) {
				log.error("Error en firma: ", e);
				errorEnFirma = true;
			}
		}

		// Se actualiza el objeto de autenticación con el número de serie
		UserAuthentication userAuthentication = (UserAuthentication) SecurityContextHolder.getContext()
				.getAuthentication();
		userAuthentication.setSerialNumber(inbox.getSerialNumber());
		SecurityContextHolder.getContext().setAuthentication(userAuthentication);

		response.setContentType("application/json");
		if (!errorEnFirma) {
			response.getWriter().write("{\"status\": \"success\", \"serialNumber\": \"" + inbox.getSerialNumber() + "\"}");
		} else {
			response.getWriter().write("{\"status\": \"error\", \"log\": \"" + logErrorEnFirma + "\"}");
		}

	}

	/**
	 * Valida el certificado del usuario, bien contra Afirma o bien contra
	 * EEutil. Setea el userDTO de la clase y el número de serie del certificado
	 * en hexadecimal.
	 * 
	 * @param certificado
	 * @param inbox
	 * @param request
	 * @throws AuthenticatorException
	 * @throws CertificateException
	 */
	private void validarCertificado(String certificado, Inbox inbox, HttpServletRequest request)
			throws AuthenticatorException, CertificateException {
		String nifCif = null;
		String numSerieCert = null;
		ValidateCertificateResponse respuesta = null;
		try {
			respuesta = certificateBO.validarCertificado(certificado);
			if (respuesta.isError()) {
				log.error("No se ha podido validar el certificado, la respuesta de Afirma ha sido erronea: "
						+ respuesta.getMensajeAmpliado());
				throw new AuthenticatorException("No se ha podido validar el certificado. " + respuesta.getMensaje());
			}
			if (!respuesta.isValido()) {
				throw new AuthenticatorException("El certificado no es válido. " + respuesta.getMensaje());
			}

			nifCif = respuesta.getNifCif();
			numSerieCert = respuesta.getNumeroSerie();
			request.getSession().setAttribute("serialNumber", respuesta.getNumeroSerie());
			request.getSession().setAttribute("fechaValidezCertificado", respuesta.getDateValidSign());

		} catch (CertificateException e) {
			if (e.getCode().equals(CertificateException.COD_001)) {
				log.error(CertificateException.MESSAGE_001 + ": " + e);
				throw e;
			} else if (e.getCode().equals(CertificateException.COD_002)) {
				log.error(CertificateException.MESSAGE_002 + ": " + e);
				throw e;
			} else {
				log.error("No se ha podido validar el certificado, ", e);
				throw new AuthenticatorException("No se ha podido validar el certificado. ");
			}
		}

		// Si el certificado seleccionado no pertenece al usuario autenticado se
		// muestra un error
		// Se recupera el usuario autenticado
		UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
		PfUsersDTO user = authorization.getUserDTO();

		boolean esMismoUsuario = user.getCidentifier().equals(nifCif);
		
		if (!esMismoUsuario && !respuesta.isSeudonimo()) {
			throw new AuthenticatorException("El certificado seleccionado no pertenece al usuario autenticado");
		}

		java.math.BigInteger big = new BigInteger(numSerieCert);
		inbox.setSerialNumber(big.toString(16));

	}

	/**
	 * Método que guarda en base de datos las firmas realizadas sobre una
	 * petición
	 * 
	 * @param signatures
	 *            Objeto que contiene las firmas de los documentos de la
	 *            petición
	 * @throws IOException
	 * @throws Exception
	 */
	@RequestMapping(value = "/saveSign")
	public void saveSign(@ModelAttribute("inbox") Inbox inbox, @RequestBody final RequestSignature signatures,
			final HttpServletResponse response, final HttpServletRequest request) throws IOException {
		boolean error = false;
		boolean validarFirmas = signatures.getValidarFirma().equalsIgnoreCase("1");

		// La respuesta es un JSON
		response.setContentType("application/json");

		// Se recupera el usuario autenticado
		UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
		PfUsersDTO user = authorization.getUserDTO();

		// Se recupera la petición firmada
		PfRequestTagsDTO requestTagDTO = requestBO.queryRequestTagByHash(signatures.getRequestTagHash());

		// Se comprueba que la etiqueta-peticion no esté en un estado final para
		// el usuario
		if (!tagBO.checkNotFinishedReqTag(requestTagDTO)) {
			error = true;
			String message = "La petición con asunto: " + "\"" + requestTagDTO.getPfRequest().getDsubject() + "\""
					+ " no ha sido firmada por encontrarse en un estado final para el usuario.";
			response.getWriter().write("{\"status\": \"error\", \"log\": \"" + message + "\"}");
		} else {
			// Se registran las firmas de la petición
			try {

				if (request.getSession().getAttribute("fechaValidezCertificado") == null
						|| !(request.getSession().getAttribute("fechaValidezCertificado") instanceof Date)) {
					request.getSession().removeAttribute("serialNumber");
					throw new PortafirmasException(
							"Ha ocurrido un error al seleccionar su certificado. Por favor, cierre el navegador y pruebe de nuevo.");
				}

				signatures.setFechaValidezCertificado(
						(Date) request.getSession().getAttribute("fechaValidezCertificado"));

				signBO.endMassiveSign(requestTagDTO, signatures, user, false, validarFirmas);

				List<AbstractBaseDTO> signedRequests = new ArrayList<AbstractBaseDTO>();
				signedRequests.add(requestTagDTO);
				noticeBO.sendAdviceToAppServer(signedRequests);

			} catch (CustodyServiceException e) {
				error = procesarError(request, response, signatures, e, "Error al guardar la firma");
			} catch (InvalidSignKOException e) {
				error = procesarInvalidSign(request, response, signatures, e, e.getMessage(), "servicioCaido");
			} catch (InvalidSignException e) {
				error = procesarInvalidSign(request, response, signatures, e, e.getMessage(), "firmaInvalida");
			} catch (TimeStampingException e) {
				error = procesarError(request, response, signatures, e, "Fallo al añadir el sello de tiempo");
			} catch (PortafirmasException e) {
				error = procesarError(request, response, signatures, e, e.getMessage());
			} catch (Throwable t) {
				error = procesarError(request, response, signatures, t,
						"Fallo desconocido. Consulte con su administrador.");
			}

			if (!error) {
				response.getWriter().write("{\"status\": \"success\"}");
			}
		}
	}

	private boolean procesarInvalidSign(HttpServletRequest request, HttpServletResponse response,
			RequestSignature signatures, InvalidSignException e, String mensaje, String tipoError) throws IOException {
		borrarAtributosCertificado(request, signatures, e);
		response.getWriter().write("{\"status\": \"" + tipoError + "\", \"log\": \"" + mensaje + "\"}");
		return true;
	}

	private boolean procesarError(HttpServletRequest request, HttpServletResponse response, RequestSignature signatures,
			Throwable e, String mensaje) throws IOException {
		borrarAtributosCertificado(request, signatures, e);
		response.getWriter().write("{\"status\": \"error\", \"log\": \"" + mensaje + "\"}");
		return true;
	}

	private void borrarAtributosCertificado(HttpServletRequest request, RequestSignature signatures, Throwable e) {
		log.error("Error al guardar la firma o firmas de la petición " + signatures.getRequestTagHash(), e);
		log.error(e);
		if (request.getSession().getAttribute("serialNumber") == null
				|| "".equals(request.getSession().getAttribute("serialNumber"))
				|| request.getSession().getAttribute("fechaValidezCertificado") == null
				|| !(request.getSession().getAttribute("fechaValidezCertificado") instanceof Date)) {
			request.getSession().removeAttribute("serialNumber");
			request.getSession().removeAttribute("fechaValidezCertificado");
		}
	}

	/**
	 * Método que da el visto bueno de una petición
	 * 
	 * @param inbox
	 *            Modelo de datos del formulario
	 * @param requestTagId
	 *            Identificador de la petición que recibe el visto bueno
	 * @param response
	 *            Respuesta del servidor
	 * @throws IOException
	 */
	@RequestMapping(value = "/doPass")
	public void doPass(@ModelAttribute("inbox") Inbox inbox,
			@RequestParam(value = "requestTagId") final String requestTagId, final HttpServletResponse response)
					throws IOException {

		// Se recupera el usuario autenticado
		UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
		PfUsersDTO user = authorization.getUserDTO();

		// Se recupera la petición a la que se da el visto bueno
		PfRequestTagsDTO requestTagDTO = requestBO.queryRequestTagByHash(requestTagId);

		// Se da el visto bueno a la petición
		passBO.pass(user, user.getValidJob(), requestTagDTO, false, false);

		requestBO.markAsUnReadForRemitters(requestTagDTO.getPfRequest());

		// La respuesta es un JSON
		response.setContentType("application/json");
		response.getWriter().write("{\"status\": \"success\"}");
	}

	/**
	 * Método que retira una petición enviada
	 * 
	 * @param inbox
	 *            Modelo de datos del formulario
	 * @param requestTagId
	 *            Identificador de la petición
	 * @param text
	 *            Texto de retirada de la petición
	 * @param response
	 *            Respuesta del servidor
	 * @throws IOException
	 */
	@RequestMapping(value = "/doSendToRemove")
	public void doSendToRemove(@ModelAttribute("inbox") Inbox inbox,
			@RequestParam(value = "requestTagId") final String requestTagId,
			@RequestParam(value = "sendToRemoveText") final String text, final HttpServletResponse response)
					throws IOException {

		// Se recupera el usuario autenticado
		UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
		PfUsersDTO user = authorization.getUserDTO();

		// La respuesta es un JSON
		response.setContentType("application/json");

		// Se recupera la petición que se rechaza
		PfRequestTagsDTO requestTagDTO = requestBO.queryRequestTagByHash(requestTagId);
		log.debug("* Usuario: " + requestTagDTO.getPfUser().getDname());
		log.debug("* Asunto: " + requestTagDTO.getPfRequest().getDsubject());

		// Obtenemos las etiquetas-petición que no están en un estado final
		List<AbstractBaseDTO> notFinishedReqTags = new ArrayList<AbstractBaseDTO>();
		List<AbstractBaseDTO> finishedReqTags = new ArrayList<AbstractBaseDTO>();
		List<AbstractBaseDTO> sendToRemoveList = new ArrayList<AbstractBaseDTO>();

		sendToRemoveList.add(requestTagDTO);

		tagBO.addNotFinishedAndFinishedReqTags(sendToRemoveList, finishedReqTags, notFinishedReqTags);
		String signLinesTerminatedError = messageSource.getMessage("signLinesTerminatedError", null,
				LocaleContextHolder.getLocale());

		if (CollectionUtils.isEmpty(notFinishedReqTags)) {
			response.getWriter()
					.write("{\"status\": \"signLinesTerminated\",\"log\": \"" + signLinesTerminatedError + "\"}");
		} else {

			for (int i = 0; i < notFinishedReqTags.size(); i++) {
				// Se devuelven aquellas que no estén en un estado final para el
				// usuario
				if (text == null || "".equals(text)) {
					requestBO.insertRemove(user, (PfRequestTagsDTO) notFinishedReqTags.get(i), " ");
				} else {
					requestBO.insertRemove(user, (PfRequestTagsDTO) notFinishedReqTags.get(i), text);
				}
			}

			// Llamamos al webservice de la aplicación de origen
			try {
				noticeBO.sendAdviceToAppServer(notFinishedReqTags);
			} catch (NoticeException e) {
				log.debug("Error al llamar el servicio de respuesta al rechazar la petición enviada con hash "
						+ requestTagId);
				response.getWriter().write("{\"status\": \"error\"}");
				e.printStackTrace();
			}
			response.getWriter().write("{\"status\": \"success\"}");
		}
	}

	/**
	 * Método que rechaza una petición
	 * 
	 * @param inbox
	 *            Modelo de datos del formulario
	 * @param requestTagId
	 *            Identificador de la petición
	 * @param textRejection
	 *            Texto de rechazo de la petición
	 * @param response
	 *            Rspuesta del servidor
	 * @throws IOException
	 */
	@RequestMapping(value = "/doReject")
	public void doReject(@ModelAttribute("inbox") Inbox inbox,
			@RequestParam(value = "requestTagId") final String requestTagId,
			@RequestParam(value = "textRejection") String textRejection, final HttpServletResponse response)
					throws IOException {

		// Se recupera el usuario autenticado
		UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
		PfUsersDTO user = authorization.getUserDTO();

		// Se recupera la petición que se rechaza
		PfRequestTagsDTO requestTagDTO = requestBO.queryRequestTagByHash(requestTagId);

		// Obtenemos las etiquetas-petición que no están en un estado final, y
		// las que sí.
		List<AbstractBaseDTO> notFinishedReqTags = new ArrayList<AbstractBaseDTO>();
		List<AbstractBaseDTO> finishedReqTags = new ArrayList<AbstractBaseDTO>();

		List<AbstractBaseDTO> rejectList = new ArrayList<AbstractBaseDTO>();
		rejectList.add(requestTagDTO);
		tagBO.addNotFinishedAndFinishedReqTags(rejectList, finishedReqTags, notFinishedReqTags);
		
		//[DipuCR-Agustin]Si no hay motivo de rechazo le asigno uno
		if(textRejection==null || textRejection.isEmpty())
		{
			textRejection = DEFAULT_REASON;
		}

		// Se devuelven aquellas que no estén en un estado final para el usuario
		requestBO.insertReject(user, notFinishedReqTags, textRejection);

		// La respuesta es un JSON
		response.setContentType("application/json");

		// Llamamos al webservice de la aplicación de origen
		try {
			noticeBO.sendAdviceToAppServer(notFinishedReqTags);
		} catch (NoticeException e) {
			log.error("Error al llamar el servicio de respuesta al rechazar la petición con hash " + requestTagId, e);
			// si sólo ha fallado la notificación, no se le indica error al usuario
			response.getWriter().write("{\"status\": \"success\"}");
		}

		response.getWriter().write("{\"status\": \"success\"}");
	}

	/**
	 * Método que marca como validada una petición
	 * 
	 * @param inbox
	 *            Modelo de datos del formulario
	 * @param requestTagId
	 *            Identificador de la petición
	 * @param response
	 *            Respuesta del servidor
	 * @throws IOException
	 */
	@RequestMapping(value = "/doValidation")
	public void doValidation(@ModelAttribute("inbox") Inbox inbox,
			@RequestParam(value = "requestTagId") final String requestTagId, final HttpServletResponse response)
					throws IOException {

		// Se recupera la petición que se rechaza
		PfRequestTagsDTO requestTagDTO = requestBO.queryRequestTagByHash(requestTagId);

		// Obtenemos las etiquetas-petición que no están en un estado final, y
		// lasimportanceLevelList que sí.
		List<AbstractBaseDTO> notFinishedReqTags = new ArrayList<AbstractBaseDTO>();
		List<AbstractBaseDTO> finishedReqTags = new ArrayList<AbstractBaseDTO>();

		List<AbstractBaseDTO> rejectList = new ArrayList<AbstractBaseDTO>();
		rejectList.add(requestTagDTO);
		tagBO.addNotFinishedAndFinishedReqTags(rejectList, finishedReqTags, notFinishedReqTags);

		// Se marcan como validadas aquellas que no estén en un estado final
		// para el usuario
		// Se recupera el usuario autenticado
		UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
		PfUsersDTO user = (PfUsersDTO) authorization.getPrincipal();
		for (AbstractBaseDTO abs : notFinishedReqTags) {
			PfRequestTagsDTO reqTag = (PfRequestTagsDTO) abs;
			tagBO.changeRequestToValidated(reqTag, user);
		}

		// La respuesta es un JSON
		response.setContentType("application/json");
		response.getWriter().write("{\"status\": \"success\"}");
	}

	/**
	 * Método que reenvía una petición a otro usuario
	 * 
	 * @param inbox
	 *            Modelo de datos del formulario
	 * @param requestTagId
	 *            Petición reenviada
	 * @param forwardedUser
	 *            Usuario al que se reenvía
	 * @param response
	 *            Respuesta a la petición
	 * @throws IOException
	 */
	@RequestMapping(value = "/doForward")
	public void doForward(@ModelAttribute("inbox") Inbox inbox,
			@RequestParam(value = "requestsTagId") final String requestsTagId,
			@RequestParam(value = "forwardedUserPk") final String forwardedUserPk, final HttpServletResponse response)
					throws IOException {
		if (util.esVacioONulo(forwardedUserPk)) {
			response.getWriter().write("{\"status\": \"error\",\"log\": \"Por favor, seleccione un usuario.\"}");
		} else {
			// Se recupera el usuario autenticado
			UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext()
					.getAuthentication();
			PfUsersDTO user = authorization.getUserDTO();

			// Se recupera el usuario al que se reenvía la petición
			PfUsersDTO forwardedUser = userAdmBO.queryUsersByPk(Long.parseLong(forwardedUserPk));

			// La respuesta es un JSON
			response.setContentType("application/json");
			if (forwardedUser == null) {
				// Respuesta correcta
				response.getWriter().write("{\"status\": \"error\",\"log\": \"Usuario no encontrado\"}");
			} else {
				// Se reenvían la peticiones
				String[] hashes = requestsTagId.split(",");
				for (String hash : hashes) {
					// Se cargan los datos de la petición seleccionada
					PfRequestTagsDTO requestTagDTO = requestBO.queryRequestTagByHash(hash);

					requestBO.doForward(user, requestTagDTO.getPfRequest(), forwardedUser);

					// Send notice ////Se comentan estas dos lineas porque ya se notifica en el requestBO.doForward anterior
					//PfRequestsDTO request = requestBO.queryRequestHash(requestTagDTO.getPfRequest().getChash());
					//noticeBO.noticeForwardRequest(request);

				}

				// Respuesta correcta
				response.getWriter().write("{\"status\": \"success\"}");
			}

		}
	}

	/**
	 * Método que aplica una etiqueta a una o varias peticiones
	 * 
	 * @param inbox
	 *            Modelo de datos del formulario
	 * @param labelId
	 *            Identificador de la etiqueta
	 * @param requestsIds
	 *            Listado de peticiones
	 * @throws IOException
	 */
	@RequestMapping(value = "/applyLabel")
	public void applyLabel(@ModelAttribute("inbox") Inbox inbox, @RequestParam(value = "labelId") final String labelId,
			@RequestParam(value = "requestsIds") final String requestsIds, final HttpServletResponse response)
					throws IOException {

		// Se recupera el usuario autenticado
		UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
		// PfUsersDTO user = authorization.getUserDTO();

		// Se recupera la etiqueta
		PfUserTagsDTO userTag = tagBO.queryRequestTagByPK(labelId);
		List<AbstractBaseDTO> labelList = new ArrayList<AbstractBaseDTO>();
		labelList.add(userTag);

		// Se asigna la etiqueta a las peticiones
		String[] hashes = requestsIds.split(",");
		for (String hash : hashes) {
			// Se cargan los datos de la petición seleccionada
			PfRequestTagsDTO requestTagDTO = requestBO.queryRequestTagByHash(hash);

			// Aqui se asocia

			tagBO.saveTagsUserRequest(labelList, userTag.getPfUser(), requestTagDTO, authorization.getGroup());
		}

		// La respuesta es un JSON
		response.setContentType("application/json");
		response.getWriter().write("{\"status\": \"success\"}");
	}

	/**
	 * @param inbox
	 * @param labelId
	 * @param requestsIds
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/unApplyLabel")
	public void unApplyLabel(@RequestParam(value = "labelId") final String labelId,
			@RequestParam(value = "requestId") final String requestId, final HttpServletResponse response)
					throws IOException {

		// Se obtienen las etiquetas de usuario
		PfTagsDTO tag = new PfTagsDTO();
		PfRequestsDTO request = new PfRequestsDTO();

		tag.setPrimaryKey(Long.valueOf(labelId));
		request.setPrimaryKey(Long.valueOf(requestId));

		List<AbstractBaseDTO> requestTagsListDTO = tagBO.queryRequestTags(request, tag);

		PfRequestTagsDTO requestTag = (PfRequestTagsDTO) requestTagsListDTO.get(0);
		tagBO.deleteUserIndividualTagRequest(requestTag);

		// La respuesta es un JSON
		response.setContentType("application/json");
	}

	/**
	 * Método que procesa el formulario de la bandeja de peticiones
	 * 
	 * @param inbox
	 *            Modelo de datos de la bandeja
	 * @param bindingResult
	 * @param status
	 * @return Modelo de datos
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ModelAndView processSubmit(@ModelAttribute("inbox") Inbox inbox, BindingResult bindingResult,
			SessionStatus status) {

		ModelAndView result = new ModelAndView();

		try{

			String action = inbox.getAction();

			if ("loadInbox".equals(action)) {
				// Se recupera el usuario autenticado
				UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext()
						.getAuthentication();
				PfUsersDTO user = authorization.getUserDTO();

				// Se carga el listado de aplicaciones del filtro
				loadApplicationInbox(inbox);

				// Se carga el listado de etiquetas del usuario
				loadUserLabels(inbox);

				// Se carga el listado de plantillas del usuario
				loadUserTemplates(inbox);

				// se carga el listado de meses del filtro
				inbox.setMonthList(util.getMonthsExtended());

				// se carga el listado de años del filtro
				inbox.setYearList(util.getYears());

				// se carga el tamaño de página personalizado o por defecto
				loadCustomPageSize(inbox, user);

				// Se prepara la bandeja
				loadInbox(inbox, authorization);

				// Si tiene mensajes sin leer se muestra una advertencia
				inbox.setHasMessages(false);
			}
		} catch (Exception e) {
			log.error("Error al procesar el formulario de la bandeja de peticiones", e);
			result.addObject("errorMessage", Constants.MSG_GENERIC_ERROR);
			result.addObject("timeError", new Date(System.currentTimeMillis()));
			result.setViewName("error");
		}

		return result;
	}

	/**
	 * Método que actualiza una etiqueta de usuario en base de datos
	 * 
	 * @param labelId
	 *            Identificador de la etiqueta
	 * @param labelCode
	 *            Código de la etiqueta
	 * @param response
	 *            Respuesta del servidor
	 * @throws IOException
	 */
	@RequestMapping(value = "/modifyLabel", method = RequestMethod.GET)
	public void modifyLabel(@RequestParam("labelId") final String labelId,
			@RequestParam("labelCode") final String labelCode, @RequestParam("labelColor") final String labelColor,
			final HttpServletResponse response) throws IOException {

		// Recuperamos la etiqueta de usuario
		PfUserTagsDTO userTag = labelBO.queryUserTagById(labelId);

		String labelCodeDecode = labelCode;
		try {
			labelCodeDecode = java.net.URLDecoder
					.decode(new String(org.apache.commons.codec.binary.Base64.decodeBase64(labelCode)), "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		
		// Se actualizan los valores
		userTag.getPfTag().setCtag(labelCodeDecode.toUpperCase());
		userTag.setCcolor(labelColor);

		// Se actualiza en base de datos
		labelBO.insertOrUpdateUserTag(userTag);

		// La respuesta es un JSON
		response.setContentType("application/json");
		response.getWriter().write("{\"status\": \"success\"}");
	}

	/**
	 * Método para crear una etiqueta y asociarla al usuario
	 * 
	 * @param labelId
	 * @param labelCode
	 * @param labelColor
	 * @param response
	 * @throws IOException
	 */
	@RequestMapping(value = "/insertLabel", method = RequestMethod.GET)
	public void insertLabel(@RequestParam("labelCode") final String labelCode,
			@RequestParam("labelColor") final String labelColor, final HttpServletResponse response)
					throws IOException {

		// Se recupera el usuario autenticado
		UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
		PfUsersDTO pfUser = authorization.getUserDTO();

		// DTO de la relacion etiqueta-usuario
		PfUserTagsDTO pfUserTag = new PfUserTagsDTO();
		pfUserTag.setCcolor(labelColor);
		pfUserTag.setPfUser(pfUser);

		String labelCodeDecode = labelCode;
		try {
			labelCodeDecode = java.net.URLDecoder
					.decode(new String(org.apache.commons.codec.binary.Base64.decodeBase64(labelCode)), "UTF-8");
		} catch (UnsupportedEncodingException e) {
		}
		
		// DTO de etiqueta
		PfTagsDTO pfTag = new PfTagsDTO();
		pfTag.setCtag(labelCodeDecode.toUpperCase());
		pfTag.setCtype(Constants.C_TYPE_TAG_USER);

		labelBO.insertLabel(pfTag, pfUserTag);

		// La respuesta es un JSON
		response.setContentType("application/json");
		response.getWriter().write("{\"status\": \"success\"}");
	}

	/**
	 * Método que elimina una etiqueta de usuario de la base de datos
	 * 
	 * @param labelId
	 *            Identificador de la etiqueta de usuario
	 * @param response
	 *            Respuesta del servidor
	 * @throws IOException
	 */
	@RequestMapping(value = "/deleteLabel", method = RequestMethod.GET)
	public void deleteLabel(@RequestParam("labelId") final String labelId, final HttpServletResponse response)
			throws IOException {

		// Se recupera el usuario autenticado
		UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
		PfUsersDTO pfUser = authorization.getUserDTO();

		// Recuperamos la etiqueta de usuario
		PfUserTagsDTO userTag = labelBO.queryUserTagById(labelId);

		labelBO.deleteRequestsTag(pfUser, userTag);

		// La respuesta es un JSON
		response.setContentType("application/json");
		response.getWriter().write("{\"status\": \"success\"}");
	}

	/**
	 * Método que carga las peticiones en la bandeja correspondiente
	 * 
	 */
	private void loadInbox(Inbox inbox, UserAuthentication authorization) {

		PfUsersDTO user = authorization.getUserDTO();

		// Se carga el número de peticiones pendientes del usuario
		String numUnresolved = requestBO.queryListCount(user, Constants.MESSAGES_UNRESOLVED);

		String numMessagesUnresolved = requestBO.getMessagesUnresolved(user);

		// Se prepara el paginador
		if (inbox.getPaginator() == null) {
			Paginator paginator = new Paginator();
			inbox.setPaginator(paginator);
		}
		
		if (!Util.esVacioONulo(inbox.getCustomPageSize()) && (
				inbox.getPaginator().getPageSize() == null || inbox.getPaginator().getPageSize() == 0))	
			
			inbox.getPaginator().setPageSize(inbox.getCustomPageSize());

		// Se obtiene el filtro de validadas
		boolean hasValidator = false;
		if (inbox.isHasValidator() != null) {
			hasValidator = inbox.isHasValidator();
		}

		// Filtro para "no validadas"
		boolean noValidadas = false;
		if (inbox.getViewNoValidate() != null) {
			noValidadas = inbox.getViewNoValidate();
			if (noValidadas)
				hasValidator = false;
		}

		// Transformamos el combo de meses y años en fecha desde y fecha hasta
		Date sinceDate = dateComponent.obtenerFechaDesdeDeFiltros(inbox.getMonthFilter(), inbox.getYearFilter());
		Date untilDate = dateComponent.obtenerFechaHastaDeFiltros(inbox.getMonthFilter(), inbox.getYearFilter());

		List<AbstractBaseDTO> requestList = null;
		RequestTagListDTO requestTagList = null;
		if (Constants.MESSAGES_DIFFUSION.equals(inbox.getRequestBarSelected())) {
			requestList = requestBO.getMessageListPaginatedByUser(inbox.getPaginator(), user);
			requestTagList = (RequestTagListDTO) requestList;
			inbox.setMessageList(requestList);
		}

		else {
			//Esta lista contiene las aplicaciones que estoy validando para el usuario seleccionado (estoy validando)
			List <PfApplicationsDTO> appValidators = validatorUsersConfBO.queryValidatorsAppByValidatorList((PfUsersDTO)authorization.getPrincipal(), user);
			
			// Se carga la bandeja del usuario
			requestList = requestBO.queryListPaginated(inbox.getPaginator().getPageSize(),
					inbox.getPaginator().getCurrentPage(), inbox.getOrderField(), inbox.getOrder(), user,
					authorization.getGroup(), inbox.getSearchFilter(), inbox.getLabelFilter(), inbox.getAppFilter(),
					sinceDate, untilDate, inbox.getRequestBarSelected(), user.getValidJob(),
					inbox.getRequestTypeFilter(), hasValidator, noValidadas, appValidators);

			requestTagList = (RequestTagListDTO) requestList;
			requestBO.fillAccionPrevia(requestTagList, user);			
			requestBO.fillUltimaFirma(requestTagList);
			requestBO.fillRemitenteInterfazGenerica(requestTagList);
			
			inbox.setRequestList(requestList);
		}

		Long inboxSize = requestTagList.getInboxSize();
		inbox.getPaginator().setInboxSize(inboxSize.intValue());
		inbox.setNumUnresolved(numUnresolved);
		inbox.setNumMessagesUnresolved(numMessagesUnresolved);
		
		inbox.setFireActivo(applicationBO.isFIReActivo());
		inbox.setAdministrator(user.isAdministrator());
	}

	/**
	 * Método que carga en el formulario el listado de aplicaciones para el
	 * usuario
	 * 
	 * @param inbox
	 *            Formulario de la bandeja
	 */
	private void loadApplicationInbox(Inbox inbox) {
		// Se recupera el usuario autenticado
		UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
		PfUsersDTO user = authorization.getUserDTO();

		List<AbstractBaseDTO> applicationList = null;
		// Si la petición viene de alguna de las bandejas de peticiones
		// enviadas..
		if (Constants.MESSAGES_SENT.equals(inbox.getRequestBarSelected())
				|| Constants.MESSAGES_SENT_FINISHED.equals(inbox.getRequestBarSelected())) {
			applicationList = requestBO.queryApplicationMenuSent();
		} else {
			// .. si la peticiónes es de las bandejas de peticiones recibidas
			applicationList = requestBO.queryApplicationMenu(user);
		}
		inbox.setApplicationList(applicationList);
	}

	/**
	 * Método que carga en el formulario el listado de etiquetas del usuario
	 * 
	 * @param inbox
	 *            Formulario de la bandeja
	 */
	private void loadUserLabels(Inbox inbox) {
		// Se recupera el usuario autenticado
		UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
		PfUsersDTO user = authorization.getUserDTO();

		List<AbstractBaseDTO> labelList = labelBO.queryList(user);
		inbox.setLabelList(labelList);
		// Aqui es consultar ademas la de los grupos.
		if (authorization.getGroup() != null) {
			List<AbstractBaseDTO> labelListGroup = labelBO.queryList(user, authorization.getGroup());
			inbox.getLabelList().addAll(labelListGroup);
		}

	}

	/**
	 * Método que carga en el formulario el listado de plantillas del usuario
	 * 
	 * @param inbox
	 *            Formulario de la bandeja
	 */
	private void loadUserTemplates(Inbox inbox) {
		// Se recupera el usuario autenticado
		UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
		PfUsersDTO user = authorization.getUserDTO();

		List<PfRequestTemplatesDTO> templateList = templateBO.getUserTemplates(user);
		inbox.setTemplateList(templateList);
	}

	/**
	 * Recupera los usuarios para el autocompletado en el reenvio de peticiones.
	 * 
	 * @param term
	 * @return
	 */
	@RequestMapping(value = "autocompleteForward")
	public @ResponseBody List<UserAutocomplete> autocompleteForward(@RequestParam(value = "term") final String term) {
		// Se obtiene la lista
		List<UserAutocomplete> results = null;
		try {
			// Se recupera el usuario autenticado
			UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext()
					.getAuthentication();
			PfUsersDTO user = authorization.getUserDTO();
			results = getUsersList(user, term);
		} catch (Exception e) {
			log.error("Error en autocompletar nombre en reenvio: ", e);
		}

		return results;
	}

	@RequestMapping(value = "autocompleteTargets")
	public @ResponseBody List<String> autocompleteTargets(@RequestParam(value = "term") final String term) {

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
	 * Recupera la lista de usuario sin el propio usuario
	 * 
	 * @param user
	 * @param term
	 * @return
	 */
	private List<UserAutocomplete> getUsersList(PfUsersDTO user, String term) {
		List<UserAutocomplete> results = new ArrayList<UserAutocomplete>();
		// Se obtiene la sede del usuario autenticado
		String codSede = null;
		if (user.getPfProvince() != null) {
			codSede = user.getPfProvince().getCcodigoprovincia();
		}

		// Se toma para el filtro el último valor introducido
		String[] values = term.split(",");

		// Se filtran los usuarios en base a la búsqueda
		List<AbstractBaseDTO> users = requestBO.queryUsersComplete(values[values.length - 1].trim(), codSede);

		// Se convierten los resultados
		for (AbstractBaseDTO userAux : users) {
			results.add(new UserAutocomplete((PfUsersDTO) userAux));
		}

		// Se quita el usuario logado de la lista
		results.remove(new UserAutocomplete(user));
		return results;
	}

	/**
	 * @param model
	 */
	private void loadCombosMetadatos(ModelMap model, PfApplicationsDTO aplicacion) {

		model.addAttribute("origen", ComboUtils.getOrigen(messages));
		model.addAttribute("estadosElaboracion", ComboUtils.getEstadosElaboracion(messages));
		model.addAttribute("tiposDocumentales", ComboUtils.getTiposDocumentales(messages));
		model.addAttribute("metadatosAdicionales",
				applicationMetadataBO.getMetadatasNameByAplicacion(aplicacion.getCapplication()));
	}

	/**
	 * Método que carga los datos de un mensaje seleccionado
	 * 
	 * @param inbox
	 *            Modelo de datos
	 * @param requestTagId
	 *            Identificador de la petición
	 * @return Modelo de datos
	 * @throws Exception
	 */
	@RequestMapping(value = "/loadMessage", method = RequestMethod.GET)
	public @ResponseBody Message loadMessage(@ModelAttribute("inbox") Inbox inbox, ModelMap model,
			@RequestParam("messageId") String messageId, @RequestParam("userMessageId") String userMessageId)
					throws PfirmaException {
		try {

			// Se recupera el usuario autenticado
			UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext()
					.getAuthentication();
			PfUsersDTO user = authorization.getUserDTO();
			Message message = new Message();
			if (!Util.esVacioONulo(messageId)) {
				PfMessagesDTO pfMessageDTO = new PfMessagesDTO();
				;
				PfUsersMessageDTO pfUserMessageDTO = null;

				if (!Util.esVacioONulo(userMessageId) && !userMessageId.equals("null")) {
					pfUserMessageDTO = requestBO.queryUsersMessageByPk(Long.valueOf(userMessageId));

					if (pfUserMessageDTO != null) {
						pfMessageDTO = pfUserMessageDTO.getPfMessage();
						message.setPrimaryKey(pfUserMessageDTO.getPrimaryKeyString());
						message.setDsubject(pfMessageDTO.getDsubject());
						message.setTtext(pfMessageDTO.getTtext());
						message.setFstart(Util.getInstance().dateToString(pfMessageDTO.getFstart(), "dd/MM/yyyy"));
						message.setFexpiration(
								Util.getInstance().dateToString(pfMessageDTO.getFexpiration(), "dd/MM/yyyy"));

						// Si se encuentra en estado NUEVO se cambia a LEIDO
						if (pfUserMessageDTO.getPfTag().getCtag().equals(Constants.C_TAG_NEW)) {
							requestBO.changeStatusUsersMessage(pfUserMessageDTO, user, Constants.C_TAG_READ);
						}

					}
				} else {
					// Se inserta el mensaje del usuario con estad LEIDO
					pfMessageDTO = requestBO.queryMessageByPk(Long.valueOf(messageId));
					pfUserMessageDTO = requestBO.insertUsersMessage(pfMessageDTO, user, Constants.C_TAG_READ);

				}

				message.setPrimaryKey(pfMessageDTO.getPrimaryKeyString());
				message.setDsubject(pfMessageDTO.getDsubject());
				message.setTtext(pfMessageDTO.getTtext());
				message.setFstart(Util.getInstance().dateToString(pfMessageDTO.getFstart(), "dd/MM/yyyy"));
				message.setFexpiration(Util.getInstance().dateToString(pfMessageDTO.getFexpiration(), "dd/MM/yyyy"));

				if (pfUserMessageDTO != null)
					message.setUserMessagePk(pfUserMessageDTO.getPrimaryKeyString());
			}

			return message;
		} catch (PfirmaException e) {
			log.error("Error al cargar el mensaje: ", e);
			throw e;
		}
	}

	/**
	 * Método que marcar los mensajes seleccionados como leidos
	 * 
	 * @param labelId
	 *            Identificador de la etiqueta de usuario
	 * @param response
	 *            Respuesta del servidor
	 * @throws IOException
	 */
	@RequestMapping(value = "/markRead", method = RequestMethod.GET)
	public @ResponseBody List<Message> markRead(@ModelAttribute("inbox") Inbox inbox,
			@RequestParam("messagesIds") String messagesIds, final HttpServletResponse response)
					throws PfirmaException {

		// Se recupera el usuario autenticado
		UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
		PfUsersDTO user = authorization.getUserDTO();
		List<Message> messageList = new ArrayList<Message>();
		try {
			Message message = null;
			if (!Util.esVacioONulo(messagesIds)) {
				String[] messageIdList = messagesIds.split(",");

				for (String messageId : messageIdList) {

					String[] messageIdSplit = messageId.split("-");
					String messagePk = null;
					String userMessagePk = null;
					if (messageIdSplit.length > 1) {
						messagePk = messageIdSplit[0];
						if (!messageIdSplit[1].equals("null")) {
							userMessagePk = messageIdSplit[1];
						}
					}
					PfUsersMessageDTO pfUserMessageDTO = null;
					if (!Util.esVacioONulo(userMessagePk)) {
						pfUserMessageDTO = requestBO.queryUsersMessageByPk(Long.valueOf(userMessagePk));
						requestBO.changeStatusUsersMessage(pfUserMessageDTO, user, Constants.C_TAG_READ);

					} else {
						PfMessagesDTO pfMessageDTO = requestBO.queryMessageByPk(Long.valueOf(messagePk));

						pfUserMessageDTO = requestBO.insertUsersMessage(pfMessageDTO, user, Constants.C_TAG_READ);
					}

					message = new Message();
					message.setPrimaryKey(messagePk);
					message.setUserMessagePk(pfUserMessageDTO.getPrimaryKeyString());
					messageList.add(message);

				}
			}

		} catch (PfirmaException e) {
			log.error("Error al cargar el mensaje: ", e);
			throw e;
		}
		return messageList;
	}

	/**
	 * Método que marcar los mensajes seleccionados como leidos
	 * 
	 * @param labelId
	 *            Identificador de la etiqueta de usuario
	 * @param response
	 *            Respuesta del servidor
	 * @throws IOException
	 */
	@RequestMapping(value = "/markDelete", method = RequestMethod.GET)
	public @ResponseBody List<Message> markDelete(@ModelAttribute("inbox") Inbox inbox,
			@RequestParam("messagesIds") String messagesIds, final HttpServletResponse response)
					throws PfirmaException {

		// Se recupera el usuario autenticado
		UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext().getAuthentication();
		PfUsersDTO user = authorization.getUserDTO();
		List<Message> messageList = new ArrayList<Message>();
		try {
			Message message = null;
			if (!Util.esVacioONulo(messagesIds)) {
				String[] messageIdList = messagesIds.split(",");

				for (String messageId : messageIdList) {

					String[] messageIdSplit = messageId.split("-");
					String messagePk = null;
					String userMessagePk = null;
					if (messageIdSplit.length > 1) {
						messagePk = messageIdSplit[0];
						if (!messageIdSplit[1].equals("null")) {
							userMessagePk = messageIdSplit[1];
						}
					}

					if (!Util.esVacioONulo(userMessagePk)) {
						PfUsersMessageDTO pfUserMessageDTO = requestBO
								.queryUsersMessageByPk(Long.valueOf(userMessagePk));
						requestBO.changeStatusUsersMessage(pfUserMessageDTO, user, Constants.C_TAG_REMOVED);

					} else {
						PfMessagesDTO pfMessageDTO = requestBO.queryMessageByPk(Long.valueOf(messagePk));

						requestBO.insertUsersMessage(pfMessageDTO, user, Constants.C_TAG_REMOVED);
					}

					message = new Message();
					message.setPrimaryKey(messagePk);
					messageList.add(message);
				}
			}

		} catch (PfirmaException e) {
			log.error("Error al cargar el mensaje: ", e);
			throw e;
		}

		return messageList;
	}

	/**
	 * Método que marcar los mensajes seleccionados como leidos
	 * 
	 * @param labelId
	 *            Identificador de la etiqueta de usuario
	 * @param response
	 *            Respuesta del servidor
	 * @throws IOException
	 * @throws DocumentCantBeDownloadedException
	 */
	@RequestMapping(value = "/loadMetadatasEni", method = RequestMethod.GET)
	public @ResponseBody DocumentEni loadMetadatasEni(@RequestParam("idDocumento") String idDocumento)
			throws IOException, DocumentCantBeDownloadedException {
		PfDocumentsDTO documento = binaryDocumentsBO.getDocumentDTOByHash(idDocumento);

		DocumentEni retorno = metadataConverter.applicationsMetadataDTOToDocumentEni(applicationMetadataBO
				.getMetadatasByAplicacion(documento.getPfRequest().getPfApplication().getCapplication()));
		retorno.setFcaptura(dateComponent.dateToString(new Date()));
		return retorno;
	}

	@RequestMapping(value = "/validarSiTieneVariosInformesDeFirma", method = RequestMethod.GET)
	public @ResponseBody String validarSiTieneVariosInformesDeFirma(@RequestParam("idRequestTag") String idRequestTag)
			throws IOException, DocumentCantBeDownloadedException {
		boolean retorno = reportBO.variosReport(idRequestTag);
		return "" + retorno;
	}

	@RequestMapping(value = "/sendEmail", method = RequestMethod.POST)
	public @ResponseBody String sendEmail(@RequestBody Email email, HttpServletResponse response) {
		String retorno = "";
		String mensajeDeError = "";

		// Obtenemos los id de los documentos pertenecientes a la petición
		PfRequestTagsDTO requestTagDTO = requestBO.queryRequestTagByHash(email.getRequestId());
		List<AbstractBaseDTO> listaDocumentos = requestBO.getPfDocumentsByRequest(requestTagDTO.getPfRequest());
		List<String> hashDocumentList = getHashDocumentListEliminamosAnexos(listaDocumentos);

		List<FileAttachedDTO> ficheros = new ArrayList<FileAttachedDTO>();

		if ("on".equalsIgnoreCase(email.getChkboxFirma())) {
			try {
				for (String hashDocument : hashDocumentList) {
					PfSignsDTO signDTO = binaryDocumentsBO.getSignDTOByDocumentHash (hashDocument);
					if (signDTO != null){
						FileAttachedDTO doc = binaryDocumentsBO.obtenerFirma(hashDocument);
						ficheros.add(doc);
					}else{
						mensajeDeError += "No existe Firma en la petición.";
					}
				}
			} catch (DocumentCantBeDownloadedException e) {
				log.error("Error desconocido enviando por email: ", e);
				mensajeDeError += "Se ha producido un error obteniendo el documento firmado,";
			}
		}
		if ("on".equalsIgnoreCase(email.getChkboxInforme())) {
			try {
				for (String hashDocument : hashDocumentList) {
					PfSignsDTO signDTO = binaryDocumentsBO.getSignDTOByDocumentHash (hashDocument);
					if (signDTO != null){
						FileAttachedDTO doc = binaryDocumentsBO.obtenerInforme(hashDocument);
						ficheros.add(doc);
					}else{
						mensajeDeError += "No existe Informe de Firma en la petición.";
					}
				}
			} catch (DocumentCantBeDownloadedException e) {
				log.error("Error desconocido enviando por email: ", e);
				mensajeDeError += "Se ha producido un error obteniendo el documento de informe de firma,";
			}
		}
		if ("on".equalsIgnoreCase(email.getChkboxNormalizado())) {
			try {
				for (String hashDocument : hashDocumentList) {
					PfSignsDTO signDTO = binaryDocumentsBO.getSignDTOByDocumentHash (hashDocument);
					if (signDTO != null){
						FileAttachedDTO doc = binaryDocumentsBO
								.setNormalizedReportIntoHttpResponseByDocumentHash(hashDocument);
						ficheros.add(doc);
					}else{
						mensajeDeError += "No existe Informe de Firma Normalizado en la petición.";
					}
				}
			} catch (DocumentCantBeDownloadedException e) {
				log.error("Error desconocido enviando por email: ", e);
				mensajeDeError += "Se ha producido un error obteniendo el documento de informe de firma,";
			}
		}
		
		if (mensajeDeError.isEmpty()){
			if (ficheros.isEmpty()){
				mensajeDeError += "No se han seleccionado documentos que enviar,";
			} else {
				noticeBO.noticeReports(email.getTargets(), ficheros, requestTagDTO.getPfRequest().getDsubject());
			}
		} 
		retorno = "{\"message\":\"" + mensajeDeError + "\"}";		
		return retorno;
	}

	private List<String> getHashDocumentListEliminamosAnexos(List<AbstractBaseDTO> listaDocumentos) {
		ArrayList<String> list = new ArrayList<String>();
		for (Iterator<AbstractBaseDTO> it = listaDocumentos.iterator(); it.hasNext();) {
			PfDocumentsDTO pfDocumentsDTO = (PfDocumentsDTO) it.next();
			if (pfDocumentsDTO.getLsign()){
				list.add(pfDocumentsDTO.getChash());
			}
		}
		return list;
	}

	@RequestMapping(value = "/doAnullation")
	public void anularFirma( @ModelAttribute("inbox") Inbox inbox, 
			@RequestParam("requestTagHash") String requestTagHash, 
			@RequestParam("anulled") boolean anulled,
			HttpServletResponse response) throws IOException {

		// La respuesta es un JSON
		response.setContentType("application/json");

		PfRequestTagsDTO requestTagDTO = requestBO.queryRequestTagByHash(requestTagHash);

		requestBO.cancelRequest(requestTagDTO.getPfRequest(), anulled);
		noticeBO.noticeRequestAnulled(requestTagDTO.getPfRequest(), applicationVO.getEmail(), applicationVO.getSMS(), anulled);	

		response.getWriter().write("{\"status\": \"success\"}");
	}

	/**
	 * Método para visualizar el documento
	 * 
	 * @param inbox
	 *            Modelo de datos
	 * @param requestTagId
	 *            Identificador de la petición
	 * @return Modelo de datos
	 * @throws Exception
	 */
	@RequestMapping(value = "/verDocumentoRequest", method = RequestMethod.GET)
	@AuditSimulateAnnotation(nombreOperacion="verDocumentoRequest",datosSimulado = {@AuditSimulateMapping(paramType=String.class,paramName="docHash"),
											  @AuditSimulateMapping(paramType=String.class,paramName="esIE")
											})
	public ModelAndView verDocumentoRequest(@ModelAttribute("idDocumento") String docHash, 
			@ModelAttribute("esIExpl") String esIE) throws Exception {
		try {
			ModelAndView result = new ModelAndView();

			PfDocumentsDTO documentDTO = binaryDocumentsBO.getDocumentDTOByHash(docHash);
			String informe = "";
			if (Constants.C_DOCUMENTTYPE_FACTURAE.equals(documentDTO.getPfDocumentType().getCdocumentType())){
				byte[] bytes = binaryDocumentsBO.getDocumentByDocumentDTO(documentDTO);
				byte[] arraybitesPDF = eeUtilMiscBO.getPdfFromFacturae(bytes);
				ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();
				Base64OutputStream outp = new Base64OutputStream(baosPDF, true);
				outp.write(arraybitesPDF);
				informe = baosPDF.toString();
				outp.close();
				baosPDF.close();
			} else if (documentDTO.isTcn() && applicationVO.getViewTCNActivated() ) {
				byte[] bytes = binaryDocumentsBO.getDocumentByDocumentDTO(documentDTO);
				byte[] 	pdfByte = eeUtilMiscBO.getPdfFromTCN(bytes);
				ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();
				Base64OutputStream outp = new Base64OutputStream(baosPDF, true);
				outp.write(pdfByte);
				informe = baosPDF.toString();
				outp.close();
				baosPDF.close();
			} else if (documentDTO.getLissign() && applicationVO.getViewPreSignActivated()) {
				byte[] bytes = binaryDocumentsBO.getDocumentByDocumentDTO(documentDTO);
				ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();
				Base64OutputStream outp = new Base64OutputStream(baosPDF, true);
				try {
					previsualizacionBO.previsualizacion(new ByteArrayInputStream (bytes), outp, applicationBO.queryApplicationPfirma());
				} catch (Exception e) {
					e.printStackTrace();
					log.error("Error generar la previsualización del documento: ", e);
					byte[] arraybitesPDF = eeutilVisBO.getVisualizarContenidoOriginal(documentDTO.getChash(),
							documentDTO.getDname(), bytes, documentDTO.getDmime());
					outp.write(arraybitesPDF);
				}
				informe = baosPDF.toString();
				outp.close();
				baosPDF.close();
				
			}else if (!documentDTO.getDmime().equals("application/pdf")) {
				byte[] bytes = binaryDocumentsBO.getDocumentByDocumentDTO(documentDTO);
				byte[] arraybitesPDF = eeutilVisBO.getVisualizarContenidoOriginal(documentDTO.getChash(),
						documentDTO.getDname(), bytes, documentDTO.getDmime());
				ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();
				Base64OutputStream outp = new Base64OutputStream(baosPDF, true);
				outp.write(arraybitesPDF);
				informe = baosPDF.toString();
				outp.close();
				baosPDF.close();
			} else {
				
//				binaryDocumentsBO.getDocumentOutputStreamByDocumentDTO(documentDTO, outp);
				byte[] arraybitesPDF = binaryDocumentsBO.getDocumentByDocumentDTO(documentDTO);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				Base64OutputStream outp = new Base64OutputStream(baos, true);
				outp.write(arraybitesPDF);
				informe = baos.toString();
				outp.close();
				baos.close();
			}

			// Se pasan los parámetros en la respuesta
			result.addObject("esIE", esIE);
			result.addObject("documentoBase64", informe);
			// String tipoDoc = "data:" + documentDTO.getDmime() + ";base64,";
			String tipoDoc = "data:application/pdf;base64,";
			result.addObject("tipoDocumento", tipoDoc);
			result.setViewName("modal-view-document");
			return result;
		} catch (Exception e) {
			log.error("Error al cargar el documento: ", e);
			throw e;
		}
	}
	
	//
	@RequestMapping(value = "/verAnexoRequest", method = RequestMethod.GET)
	public ModelAndView verAnexoRequest(@ModelAttribute("idAnexo") String docHash, 
			@ModelAttribute("esIExpl") String esIE) throws Exception {
		try {
			ModelAndView result = new ModelAndView();

			PfDocumentsDTO documentDTO = binaryDocumentsBO.getDocumentDTOByHash(docHash);
			String informe = "";
			if (Constants.C_DOCUMENTTYPE_FACTURAE.equals(documentDTO.getPfDocumentType().getCdocumentType())){
				byte[] bytes = binaryDocumentsBO.getDocumentByDocumentDTO(documentDTO);
				byte[] arraybitesPDF = eeUtilMiscBO.getPdfFromFacturae(bytes);
				ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();
				Base64OutputStream outp = new Base64OutputStream(baosPDF, true);
				outp.write(arraybitesPDF);
				informe = baosPDF.toString();
				outp.close();
				baosPDF.close();
			} else if (documentDTO.isTcn() && applicationVO.getViewTCNActivated() ) {
				byte[] bytes = binaryDocumentsBO.getDocumentByDocumentDTO(documentDTO);
				byte[] 	pdfByte = eeUtilMiscBO.getPdfFromTCN(bytes);
				ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();
				Base64OutputStream outp = new Base64OutputStream(baosPDF, true);
				outp.write(pdfByte);
				informe = baosPDF.toString();
				outp.close();
				baosPDF.close();
			} else if (documentDTO.getLissign() && applicationVO.getViewPreSignActivated()) {
				byte[] bytes = binaryDocumentsBO.getDocumentByDocumentDTO(documentDTO);
				ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();
				Base64OutputStream outp = new Base64OutputStream(baosPDF, true);
				try {
					previsualizacionBO.previsualizacion(new ByteArrayInputStream (bytes), outp, applicationBO.queryApplicationPfirma());
				} catch (Exception e) {
					e.printStackTrace();
					log.error("Error generar la previsualización del documento: ", e);
					byte[] arraybitesPDF = eeutilVisBO.getVisualizarContenidoOriginal(documentDTO.getChash(),
							documentDTO.getDname(), bytes, documentDTO.getDmime());
					outp.write(arraybitesPDF);
				}
				informe = baosPDF.toString();
				outp.close();
				baosPDF.close();
				
			}else if (!documentDTO.getDmime().equals("application/pdf")) {
				byte[] bytes = binaryDocumentsBO.getDocumentByDocumentDTO(documentDTO);
				byte[] arraybitesPDF = eeutilVisBO.getVisualizarContenidoOriginal(documentDTO.getChash(),
						documentDTO.getDname(), bytes, documentDTO.getDmime());
				ByteArrayOutputStream baosPDF = new ByteArrayOutputStream();
				Base64OutputStream outp = new Base64OutputStream(baosPDF, true);
				outp.write(arraybitesPDF);
				informe = baosPDF.toString();
				outp.close();
				baosPDF.close();
			} else {
				
//				binaryDocumentsBO.getDocumentOutputStreamByDocumentDTO(documentDTO, outp);
				byte[] arraybitesPDF = binaryDocumentsBO.getDocumentByDocumentDTO(documentDTO);
				ByteArrayOutputStream baos = new ByteArrayOutputStream();
				Base64OutputStream outp = new Base64OutputStream(baos, true);
				outp.write(arraybitesPDF);
				informe = baos.toString();
				outp.close();
				baos.close();
			}

			// Se pasan los parámetros en la respuesta
			result.addObject("esIE", esIE);
			result.addObject("documentoBase64", informe);
			// String tipoDoc = "data:" + documentDTO.getDmime() + ";base64,";
			String tipoDoc = "data:application/pdf;base64,";
			result.addObject("tipoDocumento", tipoDoc);
			result.setViewName("modal-view-document");
			return result;
		} catch (Exception e) {
			log.error("Error al cargar el documento: ", e);
			throw e;
		}
	}
}
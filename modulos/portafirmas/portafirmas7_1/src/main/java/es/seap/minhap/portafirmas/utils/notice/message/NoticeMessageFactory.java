/* Copyright (C) 2012-13 MINHAP, Gobierno de EspaÃ±a
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

package es.seap.minhap.portafirmas.utils.notice.message;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import es.seap.minhap.portafirmas.actions.ApplicationVO;
import es.seap.minhap.portafirmas.business.ApplicationBO;
import es.seap.minhap.portafirmas.business.HistoricRequestBO;
import es.seap.minhap.portafirmas.business.NoticeBO;
import es.seap.minhap.portafirmas.business.administration.UserAdmBO;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfCommentsDTO;
import es.seap.minhap.portafirmas.domain.PfConfigurationsParameterDTO;
import es.seap.minhap.portafirmas.domain.PfEmailsRequestDTO;
import es.seap.minhap.portafirmas.domain.PfHistoricRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfMobileUsersDTO;
import es.seap.minhap.portafirmas.domain.PfRequestTagsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfSignLinesDTO;
import es.seap.minhap.portafirmas.domain.PfSignersDTO;
import es.seap.minhap.portafirmas.domain.PfUsersAuthorizationDTO;
import es.seap.minhap.portafirmas.domain.PfUsersCommentDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.domain.PfUsersEmailDTO;
import es.seap.minhap.portafirmas.domain.PfUsersGroupsDTO;
import es.seap.minhap.portafirmas.domain.PfUsersJobDTO;
import es.seap.minhap.portafirmas.domain.PfUsersRemitterDTO;
import es.seap.minhap.portafirmas.domain.PfValidatorApplicationDTO;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.notice.bean.UsuarioPush;
import es.seap.minhap.portafirmas.utils.notice.exception.NoticeException;

/**
 * Provides method to create {@link NoticeMessage} implementation depending on
 * type, event which triggered notice and with request data
 * 
 * @author Guadaltel
 */
@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class NoticeMessageFactory  {



	private Logger log = Logger.getLogger(NoticeMessageFactory.class);

	@Autowired
	private NoticeBO noticeBO;

	@Autowired
	private BaseDAO baseDAO;

	@Autowired
	private UserAdmBO userAdmBO;

	@Autowired
	private HistoricRequestBO historicRequestBO;

	@Resource(name = "messageProperties")
	private Properties messages;

	@Autowired
	private ApplicationVO applicationVO;

	@Autowired
	private ApplicationBO appBO;
	
	private static final String DOMINIO_PORTAFIRMAS = "https://portafirmas.dipucr.es/pf";

	/**
	 * Creates a {@link NoticeMessage} implementation depending on the type
	 * received with data extracted from request and format according to the
	 * event which triggered the notice
	 * 
	 * @param type
	 *            Notice type (Email o SMS)
	 * @param event
	 *            Event which trigger notice
	 * @param request
	 *            {@link PfRequestsDTO} associated to notice
	 * @return {@link NoticeMessage} implementation with data from request
	 * @throws NoticeException
	 *             if an error uccurs during message creation
	 */
	public NoticeMessage createNoticeMessage(String type, String event,
			AbstractBaseDTO abstractDTO, List<PfUsersDTO> forwardedUsers) throws NoticeException {
		log.info("createNoticeMessage init");
		NoticeMessage noticeMessage = null;
		AbstractBaseDTO abs = noticeBO.loadMessageData(abstractDTO);

		if (type != null && (type.equals(Constants.EMAIL_NOTICE) || type.equals(Constants.INVITATION_NOTICE))) {
			noticeMessage = buildEmailMessage(abs, event, forwardedUsers);
		} else if (type != null && type.equals(Constants.SMS_NOTICE)) {
			noticeMessage = buildSMSMessage(abs, event);
		} else if (type != null && type.equals(Constants.PUSH_NOTICE)) {
			noticeMessage = buildPUSHMessage(abs, event);
		} else {
			throw new NoticeException("Unrecognized notice type");
		}
		log.info("createNoticeMessage end");
		return noticeMessage;
	}
	
	public NoticeMessage createNoticeMessage(String type, String event,
			AbstractBaseDTO abstractDTO) throws NoticeException {
		return createNoticeMessage(type, event, abstractDTO, null);
	}
	
	public NoticeMessage createNoticeMessage(String type, String event,
			List<String> destinatarios, String subjectRequest) throws NoticeException {
		log.info("createNoticeMessage init");
		log.info("createNoticeMessage init");
		NoticeMessage noticeMessage = null;
		//AbstractBaseDTO abs = noticeBO.loadMessageData(abstractDTO);

		if (type != null && type.equals(Constants.EMAIL_NOTICE)) {
			noticeMessage = buildEmailMessageReports(destinatarios, event, subjectRequest);
		} else if (type != null && type.equals(Constants.SMS_NOTICE)) {
			//noticeMessage = buildSMSMessage(destinatarios, event);
		} else if (type != null && type.equals(Constants.PUSH_NOTICE)) {
			//noticeMessage = buildPUSHMessage(destinatarios, event);
		} else {
			throw new NoticeException("Unrecognized notice type");
		}
		log.info("createNoticeMessage end");
		return noticeMessage;
	}
	
	private NoticeMessage buildPUSHMessage(AbstractBaseDTO abstractDTO, String event) throws NoticeException {
		PushNoticeMessage pushMessage = null;
		if(abstractDTO instanceof PfRequestsDTO) {
			return buildRequestPushMessage((PfRequestsDTO) abstractDTO, event);
		}
		return pushMessage;
	}

	/**
	 * @param request
	 * @param event
	 * @return
	 * @throws NoticeException
	 */
	private NoticeMessage buildRequestPushMessage(PfRequestsDTO request, String event) throws NoticeException {
		log.info("buildPushMessage inicio");
		List<UsuarioPush> destinatarios = null;
		PfHistoricRequestsDTO lastState = getLastState(request);
		if (Constants.NOTICE_REQUEST_EXPIRED.equals(event)) {
			destinatarios = obtenerDestinatariosPushExpirados(request);
		} else {
			destinatarios = obtenerDestinatariosPush(request, event);
		}
		PushNoticeMessage pushNoticeMessage = new PushNoticeMessage();
		pushNoticeMessage.setDestinatarios(destinatarios);
		if (!destinatarios.isEmpty()) {
			StringBuilder cuerpoSB = new StringBuilder();
			//Se comenta de forma provisional, se reemplaza la cadena DNI por el DNI del usuario que recibe la peticiÃ³n.
			//cuerpoSB.append(messages.getProperty(Constants.NOTICE_TEXT_HEADER)).append("\n");

			String titulo = null;
			// EvaluaciÃ³n de los notificaciones disparadas para crear el tÃ­tulo y el cuerpo del mensaje.
			if (Constants.NOTICE_EVENT_STATE_CHANGE.equals(event)) {
				titulo = messages.getProperty(Constants.NOTICE_STATECHANGED_SUBJECT);
				cuerpoSB.append(messages.getProperty(Constants.NOTICE_STATECHANGED_TEXT)).append("\n");
				// Si la peticiÃ³n fue rechazada tambiÃ©n, se aÃ±ade la razÃ³n del rechazo
				if (lastState != null && lastState.getChistoricRequest().equals(Constants.C_HISTORIC_REQUEST_REJECTED)) {
					cuerpoSB.append(messages.getProperty(Constants.NOTICE_STATECHANGED_REJECTIONREASON_TEXT)).append("\n");
				}
			} else if (Constants.NOTICE_EVENT_NEW_COMMENT.equals(event)) {
				titulo = messages.getProperty(Constants.NOTICE_NEWCOMMENT_SUBJECT);
				cuerpoSB.append(messages.getProperty(Constants.NOTICE_NEWCOMMENT_TEXT)).append("\n\"").append(Constants.NOTICE_COMMENT_VAR).append("\"\n");
			} else if (Constants.NOTICE_EVENT_SIGNER_ADDED.equals(event)) {
				titulo = messages.getProperty(Constants.NOTICE_SIGNERADDED_SUBJECT);
				cuerpoSB.append(messages.getProperty(Constants.NOTICE_SIGNERADDED_TEXT)).append("\n");
			} else if (Constants.NOTICE_EVENT_NEW_REQUEST.equals(event)) {
				titulo = messages.getProperty(Constants.NOTICE_NEWREQUEST_SUBJECT);
				cuerpoSB.append(Constants.MOBILE_PROXY_URL + Constants.MOBILE_SEPARATOR + Constants.MOBILE_DNI + Constants.MOBILE_SEPARATOR + Constants.MOBILE_TIPO_PET_ENTRANTE);
				putValidityDates(request, cuerpoSB);
			} else if (Constants.NOTICE_REQUEST_EXPIRED.equals(event)) {
				titulo = messages.getProperty(Constants.NOTICE_REQUEST_EXPIRED);
				cuerpoSB.append(messages.getProperty(Constants.NOTICE_REQUEST_EXPIRED_TEXT)).append("\n\n");
			} else {
				throw new NoticeException("Unrecognized event type");
			}
			String cuerpo = reemplazarVariablesPush(cuerpoSB, request, event, lastState);
			
			pushNoticeMessage .setTitulo(titulo);
			pushNoticeMessage.setCuerpo(cuerpo);
			//pushNoticeMessage.setIcono("");
			//pushNoticeMessage.getSonido("");
		}
		log.info("buildPushMessage fin");
		return pushNoticeMessage;
	}

	/**
	 * @param cuerpoSB
	 * @param request
	 * @param event
	 * @param lastState 
	 * @return
	 */
	private String reemplazarVariablesPush(StringBuilder cuerpoSB, PfRequestsDTO request, String event, PfHistoricRequestsDTO lastState) {
		log.info("reemplazarVariablesPush inicio");
		// Using auxiliary String to use method replace
		String titulo = cuerpoSB.toString();
		
		Iterator <PfSignersDTO> iterator = request.getPfSignsLinesList().get(0).getPfSigners().iterator();
		
		PfSignersDTO signer = null;
		while (iterator.hasNext()) {
			signer = iterator.next();
			break;
		}
		
		HashMap<String,String> parametrosSIM = appBO.obtenerParametrosSIM();
		titulo = titulo.replace(Constants.MOBILE_PROXY_URL, parametrosSIM.get(Constants.SIM_PROXY_URL));
		titulo = titulo.replace(Constants.MOBILE_DNI, signer.getPfUser().getCidentifier());
		
		// Replace common vars (subject and reference)
		titulo = titulo.replace(Constants.NOTICE_SUBJECT_VAR,	request.getDsubject());
		// Reference could be null, in this case replace with ""
		if (request.getDreference() != null) {
			titulo = titulo.replace(Constants.NOTICE_REFERENCE_VAR, request.getDreference());
		} else {
			titulo = titulo.replace(Constants.NOTICE_REFERENCE_VAR, "");
		}

		if (event.equals(Constants.NOTICE_EVENT_STATE_CHANGE) && lastState != null) {
			// Replace request state
			if (lastState.getChistoricRequest().equals(Constants.C_HISTORIC_REQUEST_READ)) {
				titulo = titulo.replace(Constants.NOTICE_STATE_VAR, messages.getProperty(Constants.NOTICE_READ));
			} else if (lastState.getChistoricRequest().equals(Constants.C_HISTORIC_REQUEST_SIGNED)) {
				titulo = titulo.replace(Constants.NOTICE_STATE_VAR,	messages.getProperty(Constants.NOTICE_SIGNED));
			} else if (lastState.getChistoricRequest().equals(Constants.C_HISTORIC_REQUEST_PASSED)) {
				titulo = titulo.replace(Constants.NOTICE_STATE_VAR,	messages.getProperty(Constants.NOTICE_PASSED));
			} else if (lastState.getChistoricRequest().equals(Constants.C_HISTORIC_REQUEST_REMOVED)) {
				titulo = titulo.replace(Constants.NOTICE_STATE_VAR, messages.getProperty(Constants.NOTICE_REMOVED));
			} else if (lastState.getChistoricRequest().equals(Constants.C_HISTORIC_REQUEST_REJECTED)) {
				titulo = titulo.replace(Constants.NOTICE_STATE_VAR,	messages.getProperty(Constants.NOTICE_REJECTED));
				// Se obtiene el motivo del rechazo
				if (request.getPfComments() != null	&& !request.getPfComments().isEmpty()) {
					for (PfCommentsDTO comment : request.getPfComments()) {
						if (Constants.C_TAG_REJECTED.equals(comment.getDsubject())) {
							titulo = titulo.replace(Constants.NOTICE_REJECTIONREASON_VAR, comment.getTcomment());
						}
					}
				}
			}
			// Usuario que realizÃ³ el Ãºltimo cambio
			titulo = titulo.replace(Constants.NOTICE_USERHISTORIC_VAR, historicRequestBO.getHistoricFullName(lastState));
			titulo = titulo.replace(Constants.NOTICE_JOBHISTORIC_VAR, historicRequestBO.getHistoricJob(lastState));

		} else if (event.equals(Constants.NOTICE_EVENT_NEW_COMMENT)) {
			PfCommentsDTO lastComment = getLastComment(request);
			titulo = titulo.replace(Constants.NOTICE_USERWHOCOMMENT_VAR, lastComment.getPfUser().getFullName());
			titulo = titulo.replace(Constants.NOTICE_COMMENT_VAR, lastComment.getTcomment());
		} else if (event.equals(Constants.NOTICE_EVENT_SIGNER_ADDED)) {
			PfSignersDTO lastSigner = getLastSigner(request);
			titulo = titulo.replace(Constants.NOTICE_SIGNER_VAR, lastSigner.getPfUser().getFullName());
		} else if (event.equals(Constants.NOTICE_EVENT_NEW_REQUEST)) {
			String remitters = getRemmitters(request);
			PfUsersDTO usuRemitente = request.getPfUsersRemitters().iterator().next().getPfUser();
			String remitentes = "<br/>["+usuRemitente.getPfProvince().getCnombre()+"]<br/>Tramitador: "+remitters;
			titulo = titulo.replace(Constants.NOTICE_REMITTER_VAR, remitentes);
			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			// Replace the request start date
			if (request.getFstart() != null) {
				String dateTime = dateFormat.format(request.getFstart());
				titulo = titulo.replace(Constants.NOTICE_FSTART_DATE_VAR, dateTime.substring(0, dateTime.indexOf(' ')));
				titulo = titulo.replace(Constants.NOTICE_FSTART_TIME_VAR, dateTime.substring(dateTime.indexOf(' ')));
			}
			// Replace the request expiration date
			if (request.getFexpiration() != null) {
				String dateTime = dateFormat.format(request.getFexpiration());
				titulo = titulo.replace(Constants.NOTICE_FEXPIRATION_DATE_VAR, dateTime.substring(0, dateTime.indexOf(' ')));
				titulo = titulo.replace(Constants.NOTICE_FEXPIRATION_TIME_VAR, dateTime.substring(dateTime.indexOf(' ')));
			}
		}
		log.info("reemplazarVariablesPush fin");
		return titulo;
	}

	/**
	 * @param request
	 * @return
	 */
	private List<UsuarioPush> obtenerDestinatariosPushExpirados(PfRequestsDTO request) {
		List<UsuarioPush> receptores = new ArrayList<UsuarioPush>();
		if (request.getPfUsersRemitters() != null && !request.getPfUsersRemitters().isEmpty()) {
			for (PfUsersRemitterDTO userRemitterDTO : request.getPfUsersRemitters()) {
				agregarDestinatarioPush(receptores, userRemitterDTO.getPfUser());
			}
		}
		return receptores;
	}

	/**
	 * @param request
	 * @param event
	 * @return
	 */
	private List<UsuarioPush> obtenerDestinatariosPush(PfRequestsDTO request, String event) {
		List<UsuarioPush> receptores = new ArrayList<UsuarioPush>();
		// Se envian notificaciones a todos los firmantes en todos los casos, excepto cuando
		// el evento de cambio de estado (leido, firmado, devuelto, vistoBueno) y unicamente si 
		// solo debe notificarse al remitente.
		//if(event != null && !(event.equals(Constants.NOTICE_EVENT_STATE_CHANGE) && request.getLOnlyNotifyActionsToRemitter()) && !event.equals(Constants.NOTICE_EVENT_NEW_COMMENT)) { 
    	if(event != null && !request.getLOnlyNotifyActionsToRemitter()){
			if (request.getPfSignsLines() != null && !request.getPfSignsLines().isEmpty()) {
    			for (PfSignLinesDTO signLine : request.getPfSignsLines()) {
    				if (signLine.getPfSigners() != null	&& !signLine.getPfSigners().isEmpty()) {
    					for (PfSignersDTO signer : signLine.getPfSigners()) {
							if (signer.getPfUser() != null) {
								agregarDestinatarioPush(receptores, signer.getPfUser());
							}
    					}
    				}
    			}
    		}
		}
		// El remitente es agregado excepto en el evento de nueva peticiÃ³n. Si el remitente es tambiÃ©n firmante, no se agrega otra vez
//		if (event != null && (event.equals(Constants.NOTICE_EVENT_STATE_CHANGE) || event.equals(Constants.NOTICE_EVENT_SIGNER_ADDED))) {
//			if (request.getPfUsersRemitters() != null
//					&& !request.getPfUsersRemitters().isEmpty()) {
//				for (PfUsersRemitterDTO userRemitterDTO : request.getPfUsersRemitters()) {
//					if (userRemitterDTO.getPfUser() != null) {
//						agregarDestinatarioPush(receptores, userRemitterDTO.getPfUser());
//					}
//				}
//			}
//		}	
		// Los remitentes son agregados cuando la peticiÃ³n recibe un nuevo comentario
//		if (event != null && event.equals(Constants.NOTICE_EVENT_NEW_COMMENT)) {
//			
//			List<PfCommentsDTO> commentList = request.getPfCommentsList();
//			
//			if (commentList != null && commentList.size() > 0) {
//				PfCommentsDTO pfCommentsDTO = commentList.get(0);
//				if ( pfCommentsDTO.getPfUsersComments() != null &&  pfCommentsDTO.getPfUsersComments().size() > 0) {
//					Iterator<PfUsersCommentDTO> itUsersComment = pfCommentsDTO.getPfUsersComments().iterator();
//					while (itUsersComment.hasNext()) {
//						PfUsersCommentDTO usersCommentDTO = itUsersComment.next();
//						if (usersCommentDTO.getPfUser() != null) {
//							agregarDestinatarioPush(receptores, usersCommentDTO.getPfUser());
//						}
//					}	
//				}
//			}	
//		}
		return receptores;
	}

	/**
	 * @param receptores
	 * @param usuarioDTO
	 */
	private void agregarDestinatarioPush(List<UsuarioPush> receptores, PfUsersDTO usuarioDTO) {
		if(usuarioDTO.getLNotifyPush()){
			UsuarioPush usuarioPush = new UsuarioPush();
			usuarioPush.setIdUsuario(usuarioDTO.getCidentifier());
			usuarioPush.setDocUsuario(usuarioDTO.getFullName());
			usuarioPush.setIdExterno(usuarioDTO.getPrimaryKeyString());
			if(!receptores.contains(usuarioPush)){
				receptores.add(usuarioPush);
			}
		}
	}

	/**
	 * MÃ©todo que crea el mensaje de aviso de error en EEUtil
	 * @param type Tipo de mensaje
	 * @param event Tipo de evento
	 * @param eeutilEvent Tipo de evento de EEUtil
	 * @param csv 
	 * @return mensaje
	 * @throws NoticeException
	 */
	public NoticeMessage createEeutilNoticeMessage(String type, String event, String eeutilEvent, String host, String csv) throws NoticeException {
		log.info("createNoticeMessage init");
		NoticeMessage noticeMessage = null;
		
		if (type != null && type.equals(Constants.EMAIL_NOTICE)) {
			noticeMessage = buildEeutilEmailMessage(eeutilEvent, event, host, csv);
		} else {
			throw new NoticeException("Unrecognized notice type");
		}
		log.info("createNoticeMessage end");
		return noticeMessage;
	}

	/**
	 * MÃ©todo que construye un mensaje de correo electrÃ³nico que se enviarÃ¡ como notificaciÃ³n.
	 * @param req Petici&oacute;n que lanza la notificaci&oacute;n.
	 * @param event Evento que lanza la notificaci&oacute;n.
	 * @return Mensaje de correo electr&oacute;nico.
	 * @throws NoticeException
	 */
	private NoticeMessage buildEmailMessage(AbstractBaseDTO abstractDTO, String event, List<PfUsersDTO> forwardedUsers) throws NoticeException {
		EmailNoticeMessage emailMessage = null;
		
		if(abstractDTO instanceof PfRequestsDTO) {
			if(((PfRequestsDTO) abstractDTO).getLinvited()){
				return buildInvitedRequestEmailMessage((PfRequestsDTO) abstractDTO, event);
			}else{
				return buildRequestEmailMessage((PfRequestsDTO) abstractDTO, event, forwardedUsers);
			}
		}
		if(abstractDTO instanceof PfUsersAuthorizationDTO) {
			return buildAuthorizationEmailMessage((PfUsersAuthorizationDTO) abstractDTO, event);
		}
		
		if(abstractDTO instanceof PfRequestTagsDTO) {
			return buildValidacionEmailMessage((PfRequestTagsDTO) abstractDTO, event);
		}
		
		return emailMessage;
	}
	
	
	private NoticeMessage buildEmailMessageReports(List<String> destinatarios, String event, String subjectRequest) throws NoticeException {	

		return buildReportEmailMessage(destinatarios, event, subjectRequest);
		

	}
	
	/**
	 * Para construir el mensaje de correo para peticiones
	 * @param req
	 * @param event
	 * @return
	 * @throws NoticeException
	 */
	private NoticeMessage buildRequestEmailMessage(PfRequestsDTO req, String event, List<PfUsersDTO> forwardedUsers) throws NoticeException {
		log.info("buildEmailMessage init");
		EmailNoticeMessage emailMessage = null;
		List<String> emails = null;
		PfHistoricRequestsDTO lastState = getLastState(req);
		if (Constants.NOTICE_REQUEST_EXPIRED.equals(event)) {
			emails = getRequestexpiredReceivers(req, Constants.EMAIL_NOTICE);
		} else if(Constants.NOTICE_EVENT_FORWARD_REQUEST.equals(event)){
			emails = getEventForwardRequestReceivers(req, event, Constants.EMAIL_NOTICE, forwardedUsers);
		} else if(Constants.NOTICE_EVENT_FORWARD_REQUEST_REMITENTE.equals(event)){
			emails = getEventForwardRequestRemitente(req, event, Constants.EMAIL_NOTICE);
		} else {
			emails =  getReceivers(req, event, Constants.EMAIL_NOTICE, lastState);
		}
		//			// Correos adicionales de notificaciÃ³n.
		if (!Constants.NOTICE_EVENT_NEW_REQUEST_VALIDADOR.equals(event) && req.getPfEmailsRequest() != null) {
			for (PfEmailsRequestDTO emReqDTO : req.getPfEmailsRequest()) {
				emails.add(emReqDTO.getCemail());
			}
		}
		if (emails != null && !emails.isEmpty()) {
			emailMessage = new EmailNoticeMessage();
			emailMessage.setReceivers(emails);

			// En el caso de cambio de estado porque se ha realizado una firma y no es la ultima (y estamos firmando en casacada), queremos enviar mensaje de nueva peticiÃ³n al siguiente firmante.
			if (Constants.NOTICE_EVENT_STATE_CHANGE.equals(event) && esFirmaVB(lastState) 
					&& req.getLcascadeSign() && !estaFirmadaPorTodos(req)){
				event = Constants.NOTICE_EVENT_NEW_REQUEST;
			}
			
			// Get text header header
			StringBuilder plainText = new StringBuilder("");
			plainText.append(messages.getProperty(Constants.NOTICE_TEXT_HEADER)).append("\n");

			// Evaluate events which trigger notice to create text body and set
			// email subject
			if (Constants.NOTICE_EVENT_STATE_CHANGE.equals(event)) {
				emailMessage.setSubject(messages.getProperty(Constants.NOTICE_STATECHANGED_SUBJECT));
				plainText.append(messages.getProperty(Constants.NOTICE_STATECHANGED_TEXT)).append("\n");
				// if request was rejected also add rejection reason (last state
				// = rejected)
				if (lastState != null && lastState.getChistoricRequest().equals(Constants.C_HISTORIC_REQUEST_REJECTED)) {
					plainText.append(messages.getProperty(Constants.NOTICE_STATECHANGED_REJECTIONREASON_TEXT)).append("\n");
				}
			} else if (Constants.NOTICE_EVENT_NEW_COMMENT.equals(event)) {
				emailMessage.setSubject(messages.getProperty(Constants.NOTICE_NEWCOMMENT_SUBJECT));
				plainText.append(messages.getProperty(Constants.NOTICE_NEWCOMMENT_TEXT))
						 .append("\n").append("\"")
						 .append(Constants.NOTICE_COMMENT_VAR).append("\"")
						 .append("\n");
			} else if (Constants.NOTICE_EVENT_SIGNER_ADDED.equals(event)) {
				emailMessage.setSubject(messages.getProperty(Constants.NOTICE_SIGNERADDED_SUBJECT));
				plainText.append(messages.getProperty(Constants.NOTICE_SIGNERADDED_TEXT)).append("\n");
			} else if (Constants.NOTICE_EVENT_NEW_REQUEST.equals(event)) {
				
				emailMessage.setSubject(messages.getProperty(Constants.NOTICE_NEWREQUEST_SUBJECT));
				
				//Agustin #1406 mostrar el nombre del destinatario de las peticiones de firma
				String siguienteFirmante = dameSiguienteFirmante(req, lastState);
				if(null!=siguienteFirmante)
				{
					plainText.append(messages.getProperty(Constants.NOTICE_NEWREQUEST_TEXT_SIGNER));
					plainText.append(siguienteFirmante);	
					plainText.append("\n\n");	
				}				
				plainText.append(messages.getProperty(Constants.NOTICE_NEWREQUEST_TEXT)).append("\n\n");
				plainText.append(messages.getProperty(Constants.NOTICE_URL)).append("\n\n");
				
				plainText = putValidityDates(req, plainText);
				
			} else if (Constants.NOTICE_EVENT_NEW_REQUEST_VALIDADOR.equals(event)) {
				emailMessage.setSubject(messages.getProperty(Constants.NOTICE_NEWREQUEST_SUBJECT));
				plainText.append(messages.getProperty(Constants.NOTICE_NEWREQUEST_VALIDADOR_TEXT)).append("\n\n");
				plainText.append(messages.getProperty(Constants.NOTICE_URL)).append("\n\n");
				plainText = putValidityDates(req, plainText);
			} else if (Constants.NOTICE_REQUEST_EXPIRED.equals(event)) {
				emailMessage.setSubject(messages.getProperty(Constants.NOTICE_REQUEST_EXPIRED));
				plainText.append(messages.getProperty(Constants.NOTICE_REQUEST_EXPIRED_TEXT)).append("\n\n");
			} else if (Constants.NOTICE_REQUEST_SIGNED.equals(event)) {
				emailMessage.setSubject(messages.getProperty(Constants.NOTICE_REQUEST_SIGNED));
				plainText.append(messages.getProperty(Constants.NOTICE_REQUEST_SIGNED_TEXT)).append("\n\n");
			}else if (Constants.NOTICE_EVENT_FORWARD_REQUEST.equals(event)) {
				emailMessage.setSubject(messages.getProperty(Constants.NOTICE_FORWARDREQUEST_SUBJECT));
				plainText.append(messages.getProperty(Constants.NOTICE_FORWARDREQUEST_TEXT)).append("\n\n");
				plainText.append(messages.getProperty(Constants.NOTICE_URL)).append("\n\n");
				plainText = putValidityDates(req, plainText);
			}else if (Constants.NOTICE_EVENT_FORWARD_REQUEST_REMITENTE.equals(event)) {
				emailMessage.setSubject(messages.getProperty(Constants.NOTICE_FORWARDREQUESTREMITENTE_SUBJECT));
				plainText.append(messages.getProperty(Constants.NOTICE_FORWARDREQUESTREMITENTE_TEXT)).append("\n\n");
				
				for (PfUsersDTO u: forwardedUsers){
					String lineaDestinatario = messages.getProperty(Constants.NOTICE_FORWARDREQUESTREMITENTE_TEXT_DESTINATARIO);
					lineaDestinatario = lineaDestinatario.replace(Constants.NOTICE_SIGNER_VAR, u.getFullName());
					plainText.append(lineaDestinatario).append("\n");
				}
				plainText.append("\n\n");
				//plainText.append(messages.getProperty(Constants.NOTICE_URL)).append("\n\n");
				//plainText = putValidityDates(req, plainText);	
			} else if(Constants.NOTICE_EVENT_ANULLED_REQUEST.equals(event)){
				emailMessage.setSubject(messages.getProperty(Constants.NOTICE_REQUEST_ANULLED));
				plainText.append(messages.getProperty(Constants.NOTICE_REQUEST_ANULLED_TEXT)).append("\n");
			} else if(Constants.NOTICE_EVENT_REMOVE_ANULLED_REQUEST.equals(event)){
				emailMessage.setSubject(messages.getProperty(Constants.NOTICE_REQUEST_ANULLED_RECOVER));
				plainText.append(messages.getProperty(Constants.NOTICE_REQUEST_ANULLED_RECOVER_TEXT)).append("\n");
			}
			else {
				throw new NoticeException("Unrecognized event type");
			}

			// Replace message vars
			plainText = replaceTextVars(plainText, req, event, lastState);

			// plainText = new StringBuilder(new
			// String(plainText.toString().getBytes(), ));

			// Transform plain text to html
			StringBuilder htmlText = plainTextToHtml(plainText, applicationVO.getMailTemplate());
			// Add "no reply" foot for message
			plainText.append(messages.getProperty(Constants.NOTICE_NOREPLY_TEXT));
			emailMessage.setPlainText(plainText);
			emailMessage.setHtmlText(htmlText);
		}

		log.info("buildEmailMessage end");
		return emailMessage;
	}
	
	private String dameSiguienteFirmante(PfRequestsDTO req, PfHistoricRequestsDTO lastState){
		
		log.info("dameSiguienteFirmante init");
		int indiceLineaFirma = 0;
		String receiver = null;
		
		if (req.getPfSignsLinesList() != null && !req.getPfSignsLinesList().isEmpty()) {
    			indiceLineaFirma = obtenerIndiceUltimaLineaFirmada(req);
				int posicionLineaFirma = 0;
    			for (PfSignLinesDTO signLine : req.getPfSignsLinesList()) {
    				posicionLineaFirma++;
    				if (signLine.getPfSigners() != null	&& !signLine.getPfSigners().isEmpty()) {
    					boolean esSiguienteFirmante = indiceLineaFirma + 1 == posicionLineaFirma;
    					for (PfSignersDTO signer : signLine.getPfSigners()) {
    						if (esSiguienteFirmante)
    							receiver = signer.getPfUser().getFullName();
    					}
    				}
    			}
		}
		
		return receiver;
    										
		
	}
	
	private List<String> getEventForwardRequestReceivers(PfRequestsDTO req, String event, String type, List<PfUsersDTO> forwardedUsers) {
		List<String> receivers = new ArrayList<String>();
		boolean esSiguienteFirmante = true;
		for (PfUsersDTO user: forwardedUsers){
			Set<PfUsersEmailDTO> listaCorreos = obtenerCorreos(user);
			if (listaCorreos != null) {
				for (PfUsersEmailDTO userEmail : listaCorreos) {
					if (userEmail.getLnotify()) {
						if(req.getLcascadeSign()){ //Si es firma en cascada
							if (esSiguienteFirmante) { //Solo aÃ±adimos si es el siguiente firmante
								receivers.add(userEmail.getDemail());
							}
						} else { //Si es en paralelo aÃ±adimos siempre
							receivers.add(userEmail.getDemail());
						}
					}
				}
			}
			esSiguienteFirmante = false;
		}
		
		return receivers;
	}


	private List<String> getEventForwardRequestRemitente (PfRequestsDTO req, String event, String type) {
		List<String> receivers = new ArrayList<String>();
		Set<PfUsersRemitterDTO> listaRemitentes = req.getPfUsersRemitters();
		for (PfUsersRemitterDTO userRemiter: listaRemitentes){
			Set<PfUsersEmailDTO> listaCorreos = obtenerCorreos(userRemiter.getPfUser());
			if (listaCorreos != null) {
				for (PfUsersEmailDTO userEmail : listaCorreos) {
					if (userEmail.getLnotify()) {
							receivers.add(userEmail.getDemail());
					}
				}
			}
		}
		return receivers;
	}

	
	private NoticeMessage buildInvitedRequestEmailMessage(PfRequestsDTO req, String event) throws NoticeException {
		log.info("buildEmailMessage init");
		EmailNoticeMessage emailMessage = null;
		List<String> emails = new ArrayList<>();
		
		emails.add(req.getInvitedUser().getcMail());
		
		if (!emails.isEmpty()) {
			emailMessage = new EmailNoticeMessage();
			emailMessage.setReceivers(emails);

			// Get text header header
			StringBuilder plainText = new StringBuilder("");
			plainText.append(messages.getProperty(Constants.NOTICE_TEXT_HEADER)).append("\n");

			// Evaluate events which trigger notice to create text body and set
			// email subject
			if (Constants.NOTICE_EVENT_INVITED_REQUEST.equals(event)) {
				emailMessage.setSubject(messages.getProperty(Constants.NOTICE_NEWINVITEDREQUEST_SUBJECT));
				plainText.append(messages.getProperty(Constants.NOTICE_NEWINVITEDREQUEST_TEXT)).append("\n");
			}
			else {
				throw new NoticeException("Unrecognized event type " + event);
			}
			
			//URL Acceso portafirmas
			plainText.append(messages.getProperty(Constants.NOTICE_URL)).append("\n\n");
			
			// Replace message vars
			plainText = this.replaceInvitedRequestTextVars(plainText, req);

			// Transform plain text to html
			StringBuilder htmlText = plainTextToHtml(plainText, applicationVO.getMailTemplate());
			// Add "no reply" foot for message
			plainText.append(messages.getProperty(Constants.NOTICE_NOREPLY_TEXT));
			emailMessage.setPlainText(plainText);
			emailMessage.setHtmlText(htmlText);
		}

		log.info("buildEmailMessage end");
		return emailMessage;
	}

	private Set<PfUsersEmailDTO> obtenerCorreos(PfSignersDTO signer) {
		Set<PfUsersEmailDTO> listaCorreos = null;
		if (signer.getPfUser().isJob()) {
			if (signer.getPfUser().getUserOfJob()!=null && signer.getPfUser().getUserOfJob().getPfUsersEmails()!=null) {
				listaCorreos  = signer.getPfUser().getUserOfJob().getPfUsersEmails();	
			}
		} else {
			listaCorreos = signer.getPfUser().getPfUsersEmails();
		}
		return listaCorreos;
	}
	
	private Set<PfUsersEmailDTO> obtenerCorreos(PfUsersDTO user) {
		Set<PfUsersEmailDTO> listaCorreos = null;
		if (user.isJob()) {
			listaCorreos  = user.getUserOfJob().getPfUsersEmails();
		} else {
			listaCorreos = user.getPfUsersEmails();
		}
		return listaCorreos;
	}
	
	private Set<PfUsersEmailDTO> obtenerCorreosValidadores(PfSignersDTO signer) {
		Set<PfUsersEmailDTO> listaCorreos = new HashSet<PfUsersEmailDTO>();
		if (signer.getPfUser().getValidadores()!=null && !signer.getPfUser().getValidadores().isEmpty()){
			for (PfUsersDTO usuValidador : signer.getPfUser().getValidadores()){
				listaCorreos.addAll(usuValidador.getPfUsersEmails());
			}
		}
		if (signer.getPfUser().getPfValidatorApplications()!=null && !signer.getPfUser().getPfValidatorApplications().isEmpty()){
			for (PfValidatorApplicationDTO usuValidadorAplicacion : signer.getPfUser().getPfValidatorApplications()){
				if (usuValidadorAplicacion.getPfApplication().getPrimaryKey().equals(signer.getPfSignLine().getPfRequest().getPfApplication().getPrimaryKey()))
					listaCorreos.addAll(usuValidadorAplicacion.getPfValidatorUser().getPfUsersEmails());
			}
		}
		return listaCorreos;
	}

	/**
	 * Obtiene el indice de la linea de firma donde se localiza el usuario logado, es decir, el usuario que firma
	 * @param req
	 * @return
	 */
	private int obtenerIndiceUltimaLineaFirmada(PfRequestsDTO req) {
		int retorno = 0;
		if (req.getLcascadeSign() && req.getPfSignsLinesList() != null && !req.getPfSignsLinesList().isEmpty()) {
			int contador = 0;
			for (PfSignLinesDTO signLine : req.getPfSignsLinesList()) {
				contador++;
				if (signLine.getPfSigners() != null	&& !signLine.getPfSigners().isEmpty()) {
					if (isTeminateSignLine(req, signLine)) {
						retorno = contador;
					}
				}
			}
		}
		return retorno;
	}

	/**
	 * Determina si la 
	 * @param req
	 * @param signLine
	 * @return
	 */
	private boolean isTeminateSignLine(PfRequestsDTO req, PfSignLinesDTO signLine) {
		boolean isTerminate = false;
		for(PfRequestTagsDTO reqTag : req.getPfRequestsTags()) {
			if(reqTag.getPfSignLine().getPrimaryKey().equals(signLine.getPrimaryKey())
					&& reqTag.getPfTag().getCtype().equals(Constants.C_TYPE_TAG_STATE)) {
				String status = reqTag.getPfTag().getCtag();
				isTerminate = status.equals(Constants.C_TAG_SIGNED) || status.equals(Constants.C_TAG_PASSED);
			}
		}
		return isTerminate;
	}

	/**
	 * Para construir el mensaje de correo para autorizaciones
	 * @param aut
	 * @param event
	 * @return
	 * @throws NoticeException
	 */
	private NoticeMessage buildAuthorizationEmailMessage(PfUsersAuthorizationDTO aut, String event) throws NoticeException {
		log.info("buildAuthorizationEmailMessage init");
		EmailNoticeMessage emailMessage = null;
		List<String> emails = (ArrayList<String>) getAuthorizationReceiver(aut, event, Constants.EMAIL_NOTICE);

		if (emails != null && !emails.isEmpty()) {
			emailMessage = new EmailNoticeMessage();
			emailMessage.setReceivers(emails);

			// Get text header header
			StringBuilder plainText = new StringBuilder("");
			plainText.append(messages.getProperty(Constants.NOTICE_TEXT_HEADER)).append("\n");

			// Evaluate events which trigger notice to create text body and set
			// email subject
			if (Constants.NOTICE_NEW_AUTHORIZATION.equals(event)) {
				emailMessage.setSubject(messages.getProperty(Constants.NOTICE_NEWAUT_SUBJECT));
				plainText.append(messages.getProperty(Constants.NOTICE_NEWAUT_TEXT)).append("\n");
				plainText.append(messages.getProperty(Constants.NOTICE_NEWAUT_ADVICE)).append("\n");
			} else if (Constants.NOTICE_AUTHORIZATION_ACCEPTED.equals(event)) {
				emailMessage.setSubject(messages.getProperty(Constants.NOTICE_ACCEPTED_AUT_SUBJECT));
				plainText.append(messages.getProperty(Constants.NOTICE_ACCEPTED_AUT_TEXT)).append("\n");
			} else if (Constants.NOTICE_AUTHORIZATION_DENIED.equals(event)) {
				emailMessage.setSubject(messages.getProperty(Constants.NOTICE_DENIED_AUT_SUBJECT));
				plainText.append(messages.getProperty(Constants.NOTICE_DENIED_AUT_TEXT)).append("\n");
			} else if (Constants.NOTICE_ADMIN_AUTHORIZATION.equals(event)) {
				emailMessage.setSubject(messages.getProperty(Constants.NOTICE_ADMAUT_SUBJECT));
				if (aut.getFrevocation() != null) {
					plainText.append(messages.getProperty(Constants.NOTICE_ADMAUT_TEXT)).append("\n");
				}
				else {
					plainText.append(messages.getProperty(Constants.NOTICE_ADMAUT_TEXT_NO_END_TIME)).append("\n");
				}
			} else {
				throw new NoticeException("Unrecognized event type");
			}

			// Replace message vars
			plainText = replaceAutTextVars(plainText, aut, event);

			// plainText = new StringBuilder(new
			// String(plainText.toString().getBytes(), ));

			// Transform plain text to html
			StringBuilder htmlText = plainTextToHtml(plainText, applicationVO.getMailTemplate());
			// Add "no reply" foot for message
			plainText.append(messages.getProperty(Constants.NOTICE_NOREPLY_TEXT));
			emailMessage.setPlainText(plainText);
			emailMessage.setHtmlText(htmlText);
		}

		log.info("buildAuthorizationEmailMessage end");
		return emailMessage;
	}
	
	
	
	/**
	 * Para construir el mensaje de correo para autorizaciones
	 * @param aut
	 * @param event
	 * @return
	 * @throws NoticeException
	 */
	private NoticeMessage buildReportEmailMessage(List<String> destinatarios, String event, String subjectRequest) throws NoticeException {
		log.info("buildAuthorizationEmailMessage init");
		EmailNoticeMessage emailMessage = null;
		List<String> emails = destinatarios;

		if (emails != null && !emails.isEmpty()) {
			emailMessage = new EmailNoticeMessage();
			emailMessage.setReceivers(emails);

			// Get text header header
			StringBuilder plainText = new StringBuilder("");
			plainText.append(messages.getProperty(Constants.NOTICE_TEXT_HEADER)).append("\n");

			// Evaluate events which trigger notice to create text body and set
			// email subject
			if (Constants.NOTICE_EVENT_SEND_REPORTS.equals(event)) {
				emailMessage.setSubject(messages.getProperty(Constants.NOTICE_SEND_REPORTS_SUBJECT));
				
				String mensaje = messages.getProperty(Constants.NOTICE_SEND_REPORTS_TEXT);
				String peticionComodin = messages.getProperty(Constants.NOTICE_SEND_REPORTS_TEXT_PETICION);
				mensaje = mensaje.replace(peticionComodin, subjectRequest);
				
				plainText.append(mensaje).append("\n");
			} else {
				throw new NoticeException("Unrecognized event type");
			}

			// Transform plain text to html
			StringBuilder htmlText = plainTextToHtml(plainText, applicationVO.getMailTemplate());
			// Add "no reply" foot for message
			plainText.append(messages.getProperty(Constants.NOTICE_NOREPLY_TEXT));
			emailMessage.setPlainText(plainText);
			emailMessage.setHtmlText(htmlText);
		}

		log.info("buildAuthorizationEmailMessage end");
		return emailMessage;
	}

	/**
	 * Para construir el mensaje de correo para validaciones
	 * @param aut
	 * @param event
	 * @return
	 * @throws NoticeException
	 */
	private NoticeMessage buildValidacionEmailMessage(PfRequestTagsDTO reqTag, String event) throws NoticeException {
		
		
		List<String> emails = new ArrayList<String>();
		for (PfUsersEmailDTO userEmail : reqTag.getPfUser().getPfUsersEmails()) {
			if (userEmail.getLnotify() == true
					&& !emails.contains(userEmail.getDemail())) {
				emails.add(userEmail.getDemail());
			}
		}
		log.info("buildValidacionEmailMessage init");
		EmailNoticeMessage emailMessage = null;

		if (emails != null && !emails.isEmpty()) {
			emailMessage = new EmailNoticeMessage();
			emailMessage.setReceivers(emails);

			// Get text header header
			StringBuilder plainText = new StringBuilder("");
			plainText.append(messages.getProperty(Constants.NOTICE_TEXT_HEADER)).append("\n");

			// Evaluate events which trigger notice to create text body and set
			// email subject
			if (Constants.NOTICE_EVENT_VALIDATED_REQUEST.equals(event)) {
				emailMessage.setSubject(messages.getProperty(Constants.NOTICE_REQUEST_VALIDATED_SUBJECT));
				
				plainText.append(messages.getProperty(Constants.NOTICE_REQUEST_VALIDATED_TEXT)).append("\n\n");
				plainText.append(messages.getProperty(Constants.NOTICE_URL)).append("\n\n");

				// Replace message vars
				plainText = replaceValidatedTextVars(plainText, reqTag.getPfRequest(), event);

			} else {
				throw new NoticeException("Unrecognized event type");
			}

			// Transform plain text to html
			StringBuilder htmlText = plainTextToHtml(plainText, applicationVO.getMailTemplate());
			// Add "no reply" foot for message
			plainText.append(messages.getProperty(Constants.NOTICE_NOREPLY_TEXT));
			emailMessage.setPlainText(plainText);
			emailMessage.setHtmlText(htmlText);
		}

		log.info("buildAuthorizationEmailMessage end");
		return emailMessage;
	}

	
	@SuppressWarnings("unused")
	private List<String> getEmailsDestinatarios(List<PfUsersDTO> destinatarios){
		List<String> listaEmails = new ArrayList<>();
		for(PfUsersDTO usuario:destinatarios){
			for(PfUsersEmailDTO email:usuario.getPfUsersEmails()){
				if(email.getLnotify()){
					listaEmails.add(email.getDemail());
				}
			}
		}
		
		return listaEmails;
	}
		
	/**
	 * @param eeutilEvent
	 * @param event
	 * @param host
	 * @param csv 
	 * @return
	 * @throws NoticeException
	 */
	private NoticeMessage buildEeutilEmailMessage(String eeutilEvent, String event, String host, String csv) throws NoticeException {
		EmailNoticeMessage emailMessage = null;
		List<String> emails = getEeutilExceptionReceivers();

		if (emails != null && !emails.isEmpty()) {
			emailMessage = new EmailNoticeMessage();
			emailMessage.setReceivers(emails);

			// Get text header header
			StringBuilder plainText = new StringBuilder("");
			plainText.append(messages.getProperty(Constants.NOTICE_TEXT_HEADER)).append("\n");

			// Evaluate events which trigger notice to create text body and set
			// email subject
			emailMessage.setSubject(messages.getProperty(Constants.NOTICE_EEUTIL_EXCEPTION_SUBJECT)+": " + eeutilEvent + " " + csv);
			plainText.append(messages.getProperty(Constants.NOTICE_EEUTIL_EXCEPTION_TEXT)).append(":\n");
			
			if (eeutilEvent.equals(Constants.EEUTIL_CREAR_SERVICIO)) {
				plainText.append(messages.getProperty(Constants.NOTICE_EEUTIL_EXCEPTION_CREAR_SERVICIO)).append("\n\n");
			} else if (eeutilEvent.equals(Constants.EEUTIL_VALIDAR_FIRMA)) {
				plainText.append(messages.getProperty(Constants.NOTICE_EEUTIL_EXCEPTION_VALIDAR_FIRMA)).append("\n\n");			
			} else if (eeutilEvent.equals(Constants.EEUTIL_GENERAR_INFORME)) {
				plainText.append(messages.getProperty(Constants.NOTICE_EEUTIL_EXCEPTION_GENERAR_INFORME)).append("\n\n");
			} else if (eeutilEvent.equals(Constants.EEUTIL_OBTENER_FIRMANTES)) {
				plainText.append(messages.getProperty(Constants.NOTICE_EEUTIL_EXCEPTION_OBTENER_FIRMANTES)).append("\n\n");
			} else if (eeutilEvent.equals(Constants.EEUTIL_VALIDAR_CERTIFICADO)) {
				plainText.append(messages.getProperty(Constants.NOTICE_EEUTIL_EXCEPTION_VALIDAR_CERTIFICADO)).append("\n\n");
			} else if (eeutilEvent.equals(Constants.EEUTIL_AMPLIAR_FIRMA)) {
				plainText.append(messages.getProperty(Constants.NOTICE_EEUTIL_EXCEPTION_AMPLIAR_FIRMA)).append("\n\n");
			} else if (eeutilEvent.equals(Constants.EEUTIL_CREAR_SERVICIO_UTILFIRMA)) {
				plainText.append(messages.getProperty(Constants.NOTICE_EEUTIL_EXCEPTION_CREAR_SERVICIO_UTILFIRMA)).append("\n\n");
			} else if (eeutilEvent.equals(Constants.EEUTIL_CREAR_SERVICIO_UTILOPERFIRMA)) {
				plainText.append(messages.getProperty(Constants.NOTICE_EEUTIL_EXCEPTION_CREAR_SERVICIO_OPERFIRMA)).append("\n\n");
			} else if (eeutilEvent.equals(Constants.EEUTIL_CREAR_SERVICIO_UTILUTILFIRMA)) {
				plainText.append(messages.getProperty(Constants.NOTICE_EEUTIL_EXCEPTION_CREAR_SERVICIO_UTILTUILFIRMA)).append("\n\n");
			} else if (eeutilEvent.equals(Constants.EEUTIL_CREAR_SERVICIO_UTILVIS)) {
				plainText.append(messages.getProperty(Constants.NOTICE_EEUTIL_EXCEPTION_CREAR_SERVICIO_UTILVIS)).append("\n\n");
			} else if (eeutilEvent.equals(Constants.EEUTIL_CREAR_SERVICIO_UTILMISC)) {
				plainText.append(messages.getProperty(Constants.NOTICE_EEUTIL_EXCEPTION_CREAR_SERVICIO_UTILMISC)).append("\n\n");
			} else if (eeutilEvent.equals(Constants.EEUTIL_CARGAR_CONFIGURACION_UTILFIRMA)) {
				plainText.append(messages.getProperty(Constants.NOTICE_EEUTIL_EXCEPTION_CARGAR_CONF_UTILFIRMA)).append("\n\n");
			} else if (eeutilEvent.equals(Constants.EEUTIL_CARGAR_CONFIGURACION_UTILOPERFIRMA)) {
				plainText.append(messages.getProperty(Constants.NOTICE_EEUTIL_EXCEPTION_CARGAR_CONF_OPERFIRMA)).append("\n\n");
			} else if (eeutilEvent.equals(Constants.EEUTIL_CARGAR_CONFIGURACION_UTILUTILFIRMA)) {
				plainText.append(messages.getProperty(Constants.NOTICE_EEUTIL_EXCEPTION_CARGAR_CONF_UTILUTILFIRMA)).append("\n\n");
			} else if (eeutilEvent.equals(Constants.EEUTIL_CARGAR_CONFIGURACION_UTILVIS)) {
				plainText.append(messages.getProperty(Constants.NOTICE_EEUTIL_EXCEPTION_CARGAR_CONF_UTILVIS)).append("\n\n");
			} else if (eeutilEvent.equals(Constants.EEUTIL_CARGAR_CONFIGURACION_UTILMISC)) {
				plainText.append(messages.getProperty(Constants.NOTICE_EEUTIL_EXCEPTION_CARGAR_CONF_UTILMISC)).append("\n\n");
			} else if (eeutilEvent.equals(Constants.EEUTIL_INFO_CERTIFICADO)) {
				plainText.append(messages.getProperty(Constants.NOTICE_EEUTIL_EXCEPTION_INFO_CERTIFICADO)).append("\n\n");
			} else if (eeutilEvent.equals(Constants.EEUTIL_CONVERTIR_TCN)) {
				plainText.append(messages.getProperty(Constants.NOTICE_EEUTIL_EXCEPTION_CONVERTIR_TCNAPDF)).append("\n\n");
			} else if (eeutilEvent.equals(Constants.EEUTIL_OBTENER_INFORMACION_FIRMA)) {
				plainText.append(messages.getProperty(Constants.NOTICE_EEUTIL_EXCEPTION_OBTENER_INFO_FIRMA)).append("\n\n");
			} else if (eeutilEvent.equals(Constants.EEUTIL_CONVERTIR_EFACTURA_PDF)) {
				plainText.append(messages.getProperty(Constants.NOTICE_EEUTIL_EXCEPTION_VISUALIZAR_FACTURAE_PDF)).append("\n\n");
			} else if (eeutilEvent.equals(Constants.EEUTIL_VISUALIZAR_CONTENIDO_ORIGINAL)) {
				plainText.append(messages.getProperty(Constants.NOTICE_EEUTIL_EXCEPTION_VISUALIZAR_CONTENIDO_ORIGINAL)).append("\n\n");
			} else if (eeutilEvent.equals(Constants.EEUTIL_FIRMA_FICHERO)) {
				plainText.append(messages.getProperty(Constants.NOTICE_EEUTIL_EXCEPTION_FIRMA_FICHERO)).append("\n\n");
			} else if (eeutilEvent.equals(Constants.EEUTIL_GENERAR_CSV)) {
				plainText.append(messages.getProperty(Constants.NOTICE_EEUTIL_EXCEPTION_GENERAR_CSV)).append("\n\n");
			} else if (eeutilEvent.equals(Constants.EEUTIL_COMPROBAR_PDFA)) {
				plainText.append(messages.getProperty(Constants.NOTICE_EEUTIL_EXCEPTION_COMPROBAR_PDFA)).append("\n\n");
			}
			
			// Replace message vars
			plainText = replaceEeutilTextVars(plainText, host, event);

			// Transform plain text to html
			StringBuilder htmlText = plainTextToHtml(plainText, applicationVO.getMailTemplate());
			// Add "no reply" foot for message
			plainText.append(messages.getProperty(Constants.NOTICE_NOREPLY_TEXT));
			emailMessage.setPlainText(plainText);
			emailMessage.setHtmlText(htmlText);
		}

		return emailMessage;
	}

	/**
	 * M&eacute;todo que genera el texto de una notificaci&oacute;n de correo. Para ello sustituye las constantes
	 * de referencia insertadas para codificar el texto por las cadenas reales.
	 * @param plainText Texto plano del mensaje.
	 * @param req Petici&oacute;n que lanza la notificaci&oacute;n.
	 * @param event Evento que lanza la notificaci&oacute;n.
	 * @param lastState 
	 * @return Texto final del mensaje.
	 */
	private StringBuilder replaceTextVars(StringBuilder plainText, PfRequestsDTO req, String event, PfHistoricRequestsDTO lastState) {
		log.info("replaceTextVars init");
		// Using auxiliary String to use method replace
		String auxString = plainText.toString();
		// Replace common vars (subject and reference)
		auxString = auxString.replace(Constants.NOTICE_SUBJECT_VAR,	req.getDsubject());
		// Reference could be null, in this case replace with ""
		if (req.getDreference() != null) {
			auxString = auxString.replace(Constants.NOTICE_REFERENCE_VAR, req.getDreference());
		} else {
			auxString = auxString.replace(Constants.NOTICE_REFERENCE_VAR, "");
		}

		// Ponemos la URL de la aplicaci&oacute;n
		 HttpServletRequest request = null;
		try {
			ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
			if (attr != null) { 
				request = (HttpServletRequest) attr.getRequest();
			}
		} catch (IllegalStateException e) {
			log.info("Se ha capturado excepciÃ³n IllegalStateException (bug de peticiones caducadas)",e);    
		}
		if (request != null) {
			//String direccion = request.getRequestURL().toString();
			String direccion = DOMINIO_PORTAFIRMAS;
			int finDireccionBase = direccion.lastIndexOf(request.getContextPath())
						+ request.getContextPath().length();
			String direccionBase = direccion.substring(0, finDireccionBase) + "/";
			
	
			// Se a&ntilde;ade el identificador de la petici&oacute;n como par&aacute;metro
			String parametroIdPeticion = "?idpeticion=" + req.getPrimaryKeyString();
			log.debug("A&ntilde;adiendo como par&aacute;metro el identificador de la petici&oacute;n: " +
					  req.getPrimaryKeyString());
	
			
			log.debug("A&ntilde;adiendo URL " + direccionBase + " al email de aviso");
			
			// Agustin #1405 Quitar parametro Id peticion por que da problemas en el portafirmas externo
			//String urlPeticion = "<a href=\"" + direccionBase + parametroIdPeticion + "\">" 
			//								  + direccionBase + parametroIdPeticion + "</a>";
			
			String urlPeticion = "<a href=\"" + direccionBase + "\">" 
					  						  + direccionBase + parametroIdPeticion + "</a>";
	
			auxString = auxString.replace(Constants.NOTICE_URL_VAR, urlPeticion);
		}
		
		if (event.equals(Constants.NOTICE_EVENT_STATE_CHANGE) && lastState != null) {
			// Replace request state
			if (lastState.getChistoricRequest().equals(
					Constants.C_HISTORIC_REQUEST_READ)) {
				auxString = auxString.replace(Constants.NOTICE_STATE_VAR,
						messages.getProperty(Constants.NOTICE_READ));
			} else if (lastState.getChistoricRequest().equals(
					Constants.C_HISTORIC_REQUEST_SIGNED)) {
				auxString = auxString.replace(Constants.NOTICE_STATE_VAR,
						messages.getProperty(Constants.NOTICE_SIGNED));
			} else if (lastState.getChistoricRequest().equals(
					Constants.C_HISTORIC_REQUEST_PASSED)) {
				auxString = auxString.replace(Constants.NOTICE_STATE_VAR,
						messages.getProperty(Constants.NOTICE_PASSED));
			} else if (lastState.getChistoricRequest().equals(
					Constants.C_HISTORIC_REQUEST_REMOVED)) {
				auxString = auxString.replace(Constants.NOTICE_STATE_VAR,
						messages.getProperty(Constants.NOTICE_REMOVED));
			} else if (lastState.getChistoricRequest().equals(
					Constants.C_HISTORIC_REQUEST_REJECTED)) {
				auxString = auxString.replace(Constants.NOTICE_STATE_VAR,
						messages.getProperty(Constants.NOTICE_REJECTED));
				// Also replace rejection reason (got from comments)
				// Replace message variable with comment
				if (req.getPfComments() != null
						&& !req.getPfComments().isEmpty()) {
					for (PfCommentsDTO comment : req.getPfComments()) {
						if (Constants.C_TAG_REJECTED.equals(comment.getDsubject())) {
							auxString = auxString.replace(
									Constants.NOTICE_REJECTIONREASON_VAR,
									comment.getTcomment());
						}
					}
				}
			}
			// Replace user who executed last state change
			auxString = auxString.replace(Constants.NOTICE_USERHISTORIC_VAR,
					historicRequestBO.getHistoricFullName(lastState));
			auxString = auxString.replace(Constants.NOTICE_JOBHISTORIC_VAR,
					historicRequestBO.getHistoricJob(lastState));

		} else if (event.equals(Constants.NOTICE_EVENT_NEW_COMMENT)) {
			PfCommentsDTO lastComment = getLastComment(req);
			auxString = auxString.replace(Constants.NOTICE_USERWHOCOMMENT_VAR,
					lastComment.getPfUser().getFullName());
			auxString = auxString.replace(Constants.NOTICE_COMMENT_VAR,
					lastComment.getTcomment());
		} else if (event.equals(Constants.NOTICE_EVENT_SIGNER_ADDED)) {
			PfSignersDTO lastSigner = getLastSigner(req);
			auxString = auxString.replace(Constants.NOTICE_SIGNER_VAR,
					lastSigner.getPfUser().getFullName());
		} else if (event.equals(Constants.NOTICE_EVENT_FORWARD_REQUEST_REMITENTE)) {
			UserAuthentication authorization = (UserAuthentication) SecurityContextHolder.getContext()
					.getAuthentication();
			PfUsersDTO user = authorization.getUserDTO();
			String remitentes = "<br/>["+user.getPfProvince().getCnombre()+"]<br/>Tramitador: "+user.getFullName();
			auxString = auxString.replace(Constants.NOTICE_REMITTER_VAR, remitentes);
		} else if (event.equals(Constants.NOTICE_EVENT_NEW_REQUEST) || event.equals(Constants.NOTICE_EVENT_NEW_REQUEST_VALIDADOR) || event.equals(Constants.NOTICE_EVENT_FORWARD_REQUEST)) {
			String remitters = getRemmitters(req);
			PfUsersDTO usuRemitente = req.getPfUsersRemitters().iterator().next().getPfUser();
			String remitentes = "<br/>["+usuRemitente.getPfProvince().getCnombre()+"]<br/>Tramitador: "+remitters;
			auxString = auxString.replace(Constants.NOTICE_REMITTER_VAR, remitentes);

			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

			// Replace the request start date
			if (req.getFstart() != null) {
				String dateTime = dateFormat.format(req.getFstart());
				auxString = auxString.replace(Constants.NOTICE_FSTART_DATE_VAR, dateTime.substring(0, dateTime.indexOf(' ')));
				auxString = auxString.replace(Constants.NOTICE_FSTART_TIME_VAR, dateTime.substring(dateTime.indexOf(' ')));
			}
			// Replace the request expiration date
			if (req.getFexpiration() != null) {
				String dateTime = dateFormat.format(req.getFexpiration());
				auxString = auxString.replace(Constants.NOTICE_FEXPIRATION_DATE_VAR, dateTime.substring(0, dateTime.indexOf(' ')));
				auxString = auxString.replace(Constants.NOTICE_FEXPIRATION_TIME_VAR, dateTime.substring(dateTime.indexOf(' ')));
			}
		}

		log.info("replaceTextVars end");
		return new StringBuilder(auxString);
	}

	/**
	 * M&eacute;todo que genera el texto de una notificaci&oacute;n de correo. Para ello sustituye las constantes
	 * de referencia insertadas para codificar el texto por las cadenas reales.
	 * @param plainText Texto plano del mensaje.
	 * @param req Petici&oacute;n que lanza la notificaci&oacute;n.
	 * @param event Evento que lanza la notificaci&oacute;n.
	 * @param lastState 
	 * @return Texto final del mensaje.
	 */
	private StringBuilder replaceValidatedTextVars(StringBuilder plainText, PfRequestsDTO req, String event) {
		log.info("replaceValidatedTextVars init");
		// Using auxiliary String to use method replace
		String auxString = plainText.toString();
		// Replace common vars (subject and reference)
		auxString = auxString.replace(Constants.NOTICE_SUBJECT_VAR,	req.getDsubject());
		// Reference could be null, in this case replace with ""
		if (req.getDreference() != null) {
			auxString = auxString.replace(Constants.NOTICE_REFERENCE_VAR, req.getDreference());
		} else {
			auxString = auxString.replace(Constants.NOTICE_REFERENCE_VAR, "");
		}

		// Ponemos la URL de la aplicaci&oacute;n
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = (HttpServletRequest) attr.getRequest();
		if (request != null) {
			//String direccion = request.getRequestURL().toString();
			String direccion = DOMINIO_PORTAFIRMAS;
			int finDireccionBase = direccion.lastIndexOf(request.getContextPath())
						+ request.getContextPath().length();
			String direccionBase = direccion.substring(0, finDireccionBase) + "/";
			
			// Se a&ntilde;ade el identificador de la petici&oacute;n como par&aacute;metro
			String parametroIdPeticion = "?idpeticion=" + req.getPrimaryKeyString();
			log.debug("A&ntilde;adiendo como par&aacute;metro el identificador de la petici&oacute;n: " +
					  req.getPrimaryKeyString());
	
			log.debug("A&ntilde;adiendo URL " + direccionBase + " al email de aviso");
			
			// Agustin #1405 Quitar parametro Id peticion por que da problemas en el portafirmas externo
			//String urlPeticion = "<a href=\"" + direccionBase + parametroIdPeticion + "\">" 
			//		  + direccionBase + parametroIdPeticion + "</a>";
			
			String urlPeticion = "<a href=\"" + direccionBase + "\">" 
					  + direccionBase + parametroIdPeticion + "</a>";
	
			auxString = auxString.replace(Constants.NOTICE_URL_VAR, urlPeticion);
		
			String remitters = getRemmitters(req);
			PfUsersDTO usuRemitente = req.getPfUsersRemitters().iterator().next().getPfUser();
			String remitentes = "<br/>["+usuRemitente.getPfProvince().getCnombre()+"]<br/>Tramitador: "+remitters;
			auxString = auxString.replace(Constants.NOTICE_REMITTER_VAR, remitentes);

			DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

			// Replace the request start date
			if (req.getFstart() != null) {
				String dateTime = dateFormat.format(req.getFstart());
				auxString = auxString.replace(Constants.NOTICE_FSTART_DATE_VAR, dateTime.substring(0, dateTime.indexOf(' ')));
				auxString = auxString.replace(Constants.NOTICE_FSTART_TIME_VAR, dateTime.substring(dateTime.indexOf(' ')));
			}
			// Replace the request expiration date
			if (req.getFexpiration() != null) {
				String dateTime = dateFormat.format(req.getFexpiration());
				auxString = auxString.replace(Constants.NOTICE_FEXPIRATION_DATE_VAR, dateTime.substring(0, dateTime.indexOf(' ')));
				auxString = auxString.replace(Constants.NOTICE_FEXPIRATION_TIME_VAR, dateTime.substring(dateTime.indexOf(' ')));
			}
		}

		log.info("replaceValidatedTextVars end");
		return new StringBuilder(auxString);
	}

	
	private StringBuilder replaceAutTextVars(StringBuilder plainText, PfUsersAuthorizationDTO aut, String event) {
		log.info("replaceAutTextVars init");

		// Using auxiliary String to use method replace
		String auxString = plainText.toString();

		// Formato de las fechas
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		// Ponemos la URL de la aplicaci&oacute;n
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = (HttpServletRequest) attr.getRequest();
		//String direccion = request.getRequestURL().toString();
		String direccion = DOMINIO_PORTAFIRMAS;
		int finDireccionBase = direccion.lastIndexOf(request.getContextPath())
				+ request.getContextPath().length();
		String direccionBase = direccion.substring(0, finDireccionBase) + "/";

		log.debug("AÃ±adiendo URL " + direccionBase + " ${} al email de aviso");
		String urlPeticion = "<a href=\"" + direccionBase + "\">" 
										  + direccionBase + "</a>";

		auxString = auxString.replace(Constants.NOTICE_URL_VAR, urlPeticion);

		if (event.equals(Constants.NOTICE_NEW_AUTHORIZATION) ||
			event.equals(Constants.NOTICE_ADMIN_AUTHORIZATION)) {
			auxString = auxString.replace(Constants.NOTICE_AUTHORIZATION_USER, aut.getPfUser().getFullName());
		} else {
			auxString = auxString.replace(Constants.NOTICE_AUTHORIZED_USER, aut.getPfAuthorizedUser().getFullName());
		} 		

		// Replace the authorization start date
		if (aut.getFrequest() != null) {
			String dateTime = dateFormat.format(aut.getFrequest());
			auxString = auxString.replace(Constants.NOTICE_AUT_FSTART_DATE_VAR, dateTime.substring(0, dateTime.indexOf(' ')));
		}
		// Replace the authorization expiration date
		if (aut.getFrevocation() != null) {
			String dateTime = dateFormat.format(aut.getFrevocation());
			auxString = auxString.replace(Constants.NOTICE_AUT_FREVOCATION_DATE_VAR, dateTime.substring(0, dateTime.indexOf(' ')));
		}

		log.info("replaceAutTextVars end");
		return new StringBuilder(auxString);
	}
	
	private StringBuilder replaceInvitedRequestTextVars(StringBuilder plainText, PfRequestsDTO invReq) {
		log.info("replaceInvitedRequestTextVars init");

		// Using auxiliary String to use method replace
		String auxString = plainText.toString();

		// Formato de las fechas
		//DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		// Ponemos la URL de la aplicaci&oacute;n
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request =  attr.getRequest();
		//String direccion = request.getRequestURL().toString();
		String direccion = DOMINIO_PORTAFIRMAS;
		int finDireccionBase = direccion.lastIndexOf(request.getContextPath())
				+ request.getContextPath().length();
		String direccionBase = direccion.substring(0, finDireccionBase);// + "/loginInvitado/";

		log.debug("AÃ±adiendo URL " + direccionBase + " ${} al email de aviso");
		String urlPeticion = "<a href=\"" + direccionBase + "\">" 
										  + direccionBase + "</a>";

		auxString = auxString.replace(Constants.NOTICE_URL_VAR, urlPeticion);

		
		PfUsersDTO creatorUser = userAdmBO.getUserByPK(Long.parseLong(invReq.getCcreated()));
		auxString = auxString.replace(Constants.NOTICE_INV_REQ_USER_CREATION, creatorUser.getFullName());
		

		// Replace the authorization start date
//		if (aut.getFrequest() != null) {
//			String dateTime = dateFormat.format(aut.getFrequest());
//			auxString = auxString.replace(Constants.NOTICE_AUT_FSTART_DATE_VAR, dateTime.substring(0, dateTime.indexOf(' ')));
//		}

		log.info("replaceAutTextVars end");
		return new StringBuilder(auxString);
	}
	

	private StringBuilder replaceEeutilTextVars(StringBuilder plainText, String host, String event) {
		log.info("replaceAutTextVars init");

		// Using auxiliary String to use method replace
		String auxString = plainText.toString();

		// Formato de las fechas
//		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");

		// Ponemos la URL de la aplicaci&oacute;n
		ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
		HttpServletRequest request = (HttpServletRequest) attr.getRequest();
		//String direccion = request.getRequestURL().toString();
		String direccion = DOMINIO_PORTAFIRMAS;
		int finDireccionBase = direccion.lastIndexOf(request.getContextPath())
				+ request.getContextPath().length();
		String direccionBase = direccion.substring(0, finDireccionBase) + "/";

		log.debug("AÃ±adiendo URL " + direccionBase + " al email de aviso");
		String urlPeticion = "<a href=\"" + direccionBase + "\">" 
										  + direccionBase + "</a>";

		auxString = auxString.replace(Constants.NOTICE_URL_VAR, urlPeticion);
		auxString = auxString.replace(Constants.NOTICE_EEUTIL_HOST_NAME, host);

		log.info("replaceAutTextVars end");
		return new StringBuilder(auxString);
	}

	/**
	 * M&eacute;todo que convierte el texto en claro de un correo electr&oacute;nico en HTML.
	 * @param plainText Texto en claro.
	 * @param template Plantilla HTML que se usa para la conversi&oacute;n del texto.
	 * @return Texto del mensaje codificado en HTML.
	 */
	private StringBuilder plainTextToHtml(StringBuilder plainText, StringBuilder template) {
		log.info("plainTextToHtml init");
		String auxTemplate = template.toString();
		String auxPlainText = plainText.toString().replace("\n", "<br>");
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		// Replace variables in html ($$VAR$$)
		auxTemplate = auxTemplate.replace(Constants.TEMPLATE_TITLE_VAR,	messages.getProperty(Constants.NOTICE_TEXT_HEADER));
		auxTemplate = auxTemplate.replace(Constants.TEMPLATE_DATE_VAR, dateFormat.format(new Date()));
		auxTemplate = auxTemplate.replace(Constants.TEMPLATE_MESSAGE_VAR, auxPlainText);
		auxTemplate = auxTemplate.replace(Constants.TEMPLATE_NOREPLY_VAR, messages.getProperty(Constants.NOTICE_NOREPLY_TEXT));
		log.info("plainTextToHtml end");
		return new StringBuilder(auxTemplate);
	}

	/**
	 * Obtiene el &uacute;ltimo estado del hist&oacute;rico de una petici&oacute;n.
	 * @param req Petici&oacute;n.
	 * @return Ãšltimo estado del hist&oacute;rico.
	 */
	private PfHistoricRequestsDTO getLastState(PfRequestsDTO req) {
		log.info("getLastState init");
		PfHistoricRequestsDTO historicRequest = null;
		// Obtain last state tag from request
		if (req.getPfHistoricRequests() != null
				&& !req.getPfHistoricRequests().isEmpty()) {
			for (PfHistoricRequestsDTO historic : req.getPfHistoricRequests()) {
				if (historicRequest == null
						|| historic.getPrimaryKey().intValue() > historicRequest.getPrimaryKey().intValue()) {
					historicRequest = historic;
				}
			}
		}
		log.info("getLastState end");
		return historicRequest;
	}

	/**
	 * M&eacute;todo que devuelve el &uacute;ltimo comentario asociado a una petici&oacute;n.
	 * @param req Petici&oacute;n.
	 * @return Ultimo comentario de la petici&oacute;n.
	 */
	private PfCommentsDTO getLastComment(PfRequestsDTO req) {
		log.info("getLastComment init");
		PfCommentsDTO comment = null;
		// Obtain last comment from request
		if (req.getPfComments() != null && !req.getPfComments().isEmpty()) {
			for (PfCommentsDTO commentDTO : req.getPfComments()) {
				if (comment == null	||
					commentDTO.getPrimaryKey().intValue() > comment.getPrimaryKey().intValue()) {
					comment = commentDTO;
				}
			}
		}
		log.info("getLastComment end");
		return comment;
	}

	/**
	 * M&eacute;todo que devuelve el &uacute;ltimo firmante de una petici&oacute;n.
	 * @param req Petici&oacute;n.
	 * @return Ãšltimo firmante de la petici&oacute;n.
	 */
	private PfSignersDTO getLastSigner(PfRequestsDTO req) {
		log.info("getLastSigner init");
		PfSignersDTO signer = null;
		// Obtain last signer added
		if (req.getPfSignsLines() != null && !req.getPfSignsLines().isEmpty()) {
			for (PfSignLinesDTO signLine : req.getPfSignsLines()) {
				for (PfSignersDTO signerDTO : signLine.getPfSigners()) {
					if (signer == null
							|| signerDTO.getPrimaryKey().intValue() > signer.getPrimaryKey().intValue()) {
						signer = signerDTO;
					}
				}
			}
		}
		log.info("getLastSigner end");
		return signer;
	}

	/**
	 * M&eacute;todo que devuelve los nombres completos de los remitentes de una petici&oacute;n.
	 * @param req Petici&oacute;n.
	 * @return Cadena de texto con los nombres completos de los remitentes.
	 */
	private String getRemmitters(PfRequestsDTO req) {
		log.info("getRemitters init");
		String remmitters = "";
		for (PfUsersRemitterDTO userRemitter : req.getPfUsersRemitters()) {
			remmitters = remmitters + userRemitter.getPfUser().getFullName() + ", ";
		}
		if (!remmitters.equals("")) {
			// Remove last comma and space
			remmitters = remmitters.substring(0, remmitters.length() - 2);
		} else {
			// if no remmitter (ws legacy), put unknown remmitter
			remmitters = messages.getProperty(Constants.NOTICE_UNKNOWN_REMMITTER);
		}
		log.info("getRemitters end");
		return remmitters;
	}

	/**
	 * MÃ©todo que construye un SMS.
	 * @param req Petici&oacute;n que lanza el mensaje.
	 * @param event Evento que lanza el mensaje.
	 * @return SMS.
	 * @throws NoticeException
	 */
	private NoticeMessage buildSMSMessage(AbstractBaseDTO abstractDTO, String event) throws NoticeException {
		EmailNoticeMessage emailMessage = null;
		
		if(abstractDTO instanceof PfRequestsDTO) {
			return buildRequestSMSMessage((PfRequestsDTO) abstractDTO, event);
		}
		if(abstractDTO instanceof PfUsersAuthorizationDTO) {
			return buildAuthorizationSMSMessage((PfUsersAuthorizationDTO) abstractDTO, event);
		}
		return emailMessage;
	}
	
	/**
	 * MÃ©todo que construye un SMS para peticiones.
	 * @param req
	 * @param event
	 * @return
	 * @throws NoticeException
	 */
	private NoticeMessage buildRequestSMSMessage(PfRequestsDTO req, String event)
			throws NoticeException {
		log.info("buildSMSMessage init");
		NoticeMessage smsMessage = new SMSNoticeMessage();
		List<String> mobileNumbers = (ArrayList<String>) getReceivers(req, event, Constants.SMS_NOTICE, null);
		smsMessage.setReceivers(mobileNumbers);
		// Evaluate events which trigger notice
		if (event != null && event.equals(Constants.NOTICE_EVENT_STATE_CHANGE)) {
		} else if (event != null
				&& event.equals(Constants.NOTICE_EVENT_NEW_COMMENT)) {
		} else if (event != null
				&& event.equals(Constants.NOTICE_EVENT_SIGNER_ADDED)) {
		} else if (event != null
				&& event.equals(Constants.NOTICE_EVENT_NEW_REQUEST)) {
		} else {
			throw new NoticeException("Unrecognized event type");
		}
		log.info("buildSMSMessage end");
		return smsMessage;
	}

	/**
	 * MÃ©todo que construye un SMS para autorizaciones
	 * @param aut
	 * @param event
	 * @return
	 * @throws NoticeException
	 */
	private NoticeMessage buildAuthorizationSMSMessage(PfUsersAuthorizationDTO aut, String event) throws NoticeException {
		log.info("buildAuthorizationSMSMessage init");
		NoticeMessage smsMessage = new SMSNoticeMessage();
//		List<String> mobileNumbers = (ArrayList<String>) getReceivers(req, event, Constants.SMS_NOTICE);
//		smsMessage.setReceivers(mobileNumbers);
//		// Evaluate events which trigger notice
//		if (event != null && event.equals(Constants.NOTICE_EVENT_STATE_CHANGE)) {
//		} else if (event != null
//				&& event.equals(Constants.NOTICE_EVENT_NEW_COMMENT)) {
//		} else if (event != null
//				&& event.equals(Constants.NOTICE_EVENT_SIGNER_ADDED)) {
//		} else if (event != null
//				&& event.equals(Constants.NOTICE_EVENT_NEW_REQUEST)) {
//		} else {
//			throw new NoticeException("Unrecognized event type");
//		}
		log.info("buildAuthorizationSMSMessage end");
		return smsMessage;
	}

	/**
	 * M&eacute;todo que obtiene las direcciones de correo o los tel&eacute;fonos m&oacute;viles de los firmantes de una petici&oacute;n. Seg&uacute;n
	 * el evento que lanza la notificaci&oacute;n, las direcciones o tel&eacute;fonos de los remitentes son a&ntilde;adidos o no a la
	 * lista de destinatarios.
	 * @param req Petici&oacute;n sobre la que se notifica.
	 * @param event Evento que provoca la notificaci&oacute;n.
	 * @param type Tipo de notificaci&oacute;n (EMAIL, M&oacute;VIL).
	 * @param lastState 
	 * @return Listado con las direcciones de correo o m&oacute;viles de los destinatarios.
	 */
	private List<String> getReceivers(PfRequestsDTO req, String event, String type, PfHistoricRequestsDTO lastState) {
		log.info("getReceivers init");
		int indiceLineaFirma = 0;
		boolean esEventoFirmaVB = esFirmaVB(lastState);
		boolean esEventoLeido = esLeida(lastState);
		boolean esEventoDevuelto = esDevuelta(lastState);
		List<String> receivers = new ArrayList<String>();
		//////////////////////////////////////////////////////
		//                                                  //
		//   Correos a los destinatarios de la peticiÃ³n     //
		//                                                  //
		//////////////////////////////////////////////////////
		if(event != null && !(event.equals(Constants.NOTICE_EVENT_STATE_CHANGE) && req.getLOnlyNotifyActionsToRemitter())
				 && !event.equals(Constants.NOTICE_EVENT_NEW_COMMENT)) {
    		if (req.getPfSignsLinesList() != null && !req.getPfSignsLinesList().isEmpty()) {
    			indiceLineaFirma = obtenerIndiceUltimaLineaFirmada(req);
				int posicionLineaFirma = 0;
    			for (PfSignLinesDTO signLine : req.getPfSignsLinesList()) {
    				posicionLineaFirma++;
    				if (signLine.getPfSigners() != null	&& !signLine.getPfSigners().isEmpty()) {
    					boolean esSiguienteFirmante = indiceLineaFirma + 1 == posicionLineaFirma;
    					boolean esPrimeraLineaFirma = posicionLineaFirma == 1;
    					boolean esSegundaLineaFirma = posicionLineaFirma == 2;
    					for (PfSignersDTO signer : signLine.getPfSigners()) {
    						if (type.equals(Constants.EMAIL_NOTICE)) {
    							Set<PfUsersEmailDTO> listaCorreos = null;
    							if (event.equals(Constants.NOTICE_EVENT_NEW_REQUEST_VALIDADOR)) { 
    								listaCorreos = obtenerCorreosValidadores(signer);
    							}else {
    								listaCorreos = obtenerCorreos(signer);
    							}
    							if (listaCorreos != null) {
    								for (PfUsersEmailDTO userEmail : listaCorreos) {
    									if (userEmail.getLnotify()) {
    										// Si es firma o Visto Bueno..
    										if(esEventoFirmaVB) {
												// .. se aÃ±ade si es en cascada y es el siguiente firmante
    											if(req.getLcascadeSign() && esSiguienteFirmante){
   													receivers.add(userEmail.getDemail());
    											}
    										} else if (esEventoLeido) {
    											// ..Agustin #1405 lo lee el firmante actual y va a firmarlo, se aÃ±ade email del siguiente firmante
    											if(req.getLcascadeSign() && esSiguienteFirmante){
   													receivers.add(userEmail.getDemail());
    											}    											
    										} else if (!esEventoDevuelto) {
    											//Si es otro evento..
    											if(req.getLcascadeSign() && (Constants.NOTICE_EVENT_NEW_REQUEST.equals(event)||Constants.NOTICE_EVENT_NEW_REQUEST_VALIDADOR.equals(event))) {
    												// .. solo se aÃ±ade en cascada y nueva peticiÃ³n, si es el primer firmante
    												if(esPrimeraLineaFirma && esSiguienteFirmante) {
            											receivers.add(userEmail.getDemail());
    												}
    												if(esSegundaLineaFirma && esSiguienteFirmante) {
            											receivers.add(userEmail.getDemail());
    												}
    											} else {
        											//..se aÃ±ade siempre si es paralelo
        											receivers.add(userEmail.getDemail());
    											}
    										}
    									}
    								}
    							}
    						} else if (type.equals(Constants.SMS_NOTICE)) {
    							if (signer.getPfUser().getPfUsersMobiles() != null
    									&& !signer.getPfUser().getPfUsersMobiles().isEmpty()) {
    								for (PfMobileUsersDTO userMobile : signer.getPfUser().getPfUsersMobiles()) {
    									if (userMobile.getLnotify() == true) {
    										receivers.add(userMobile.getDmobile());
    									}
    								}
    							}
    						}
    					}
    				}
    			}
    		}
		}
		
		//////////////////////////////////////////////////
		//                                              //
		//   Correos a los remitentes de la peticiÃ³n    //
		//                                              //
		//////////////////////////////////////////////////
		
		// Remitters are added in every event except on new request
		// if remitter is signer too, don't add him again
		if (event != null && 
		   (event.equals(Constants.NOTICE_EVENT_STATE_CHANGE) ||
			//event.equals(Constants.NOTICE_EVENT_NEW_COMMENT)  ||
			event.equals(Constants.NOTICE_EVENT_SIGNER_ADDED))) {

			// Notice signers remitters (if was activated)
			if (req.getPfUsersRemitters() != null
					&& !req.getPfUsersRemitters().isEmpty()) {
				for (PfUsersRemitterDTO userRemitterDTO : req.getPfUsersRemitters()) {
					if (type.equals(Constants.EMAIL_NOTICE)) {
						
						boolean isTerminate = estaFirmadaPorTodos(req);
						// Si esta firmada (o VB) por todos o no es firma (VB)..
						if(isTerminate || !esEventoFirmaVB) {
							
							//Si el remitente es un grupo aÃ±adimos los correos de todos los usuarios del grupo
							if (userRemitterDTO.getPfGroup()!= null && userRemitterDTO.getPfGroup().getPfUsersGroups()!=null && userRemitterDTO.getPfGroup().getPfUsersGroups().size()>0){
								for (PfUsersGroupsDTO usersGroups : userRemitterDTO.getPfGroup().getPfUsersGroups()) {
									for (PfUsersEmailDTO userEmail : usersGroups.getPfUser().getPfUsersEmails()) {
										if (userEmail.getLnotify() && !receivers.contains(userEmail.getDemail())) {
											receivers.add(userEmail.getDemail());
										}
									}
								}
							} else if (userRemitterDTO.getPfUser().getPfUsersEmails() != null) {
								for (PfUsersEmailDTO userEmail : userRemitterDTO.getPfUser().getPfUsersEmails()) {
									if (userEmail.getLnotify() && !receivers.contains(userEmail.getDemail())) {
										receivers.add(userEmail.getDemail());
									}
								}
							}
						}
					} else if (type.equals(Constants.SMS_NOTICE)) {
						if (userRemitterDTO.getPfUser().getPfUsersMobiles() != null
								&& !userRemitterDTO.getPfUser().getPfUsersMobiles().isEmpty()) {
							for (PfMobileUsersDTO userMobile : userRemitterDTO.getPfUser().getPfUsersMobiles()) {
								if (userMobile.getLnotify() == true
										&& !receivers.contains(userMobile.getDmobile())) {
									receivers.add(userMobile.getDmobile());
								}
							}
						}
					}
				}
			}
			// Correos adicionales de notificaciÃ³n.
			if (req.getPfEmailsRequest() != null) {
				for (PfEmailsRequestDTO emReqDTO : req.getPfEmailsRequest()) {
					receivers.add(emReqDTO.getCemail());
				}
			}
		}
		
		
		// Remitters are added in new comment event
		if (event != null && event.equals(Constants.NOTICE_EVENT_NEW_COMMENT)) {
			
			List<PfCommentsDTO> commentList = req.getPfCommentsList();
			
			if (commentList != null && commentList.size() > 0) {
				PfCommentsDTO pfCommentsDTO = commentList.get(commentList.size()-1);
				if ( pfCommentsDTO.getPfUsersComments() != null &&  pfCommentsDTO.getPfUsersComments().size() > 0) {
					Iterator<PfUsersCommentDTO> itUsersComment = pfCommentsDTO.getPfUsersComments().iterator();
					while (itUsersComment.hasNext()) {
						PfUsersCommentDTO usersCommentDTO = itUsersComment.next();
						
						if (type.equals(Constants.EMAIL_NOTICE)) {
							if (usersCommentDTO.getPfUser().isJob()) {
								receivers.addAll(getJobReceivers(usersCommentDTO.getPfUser(), type));
							} else {
								if (usersCommentDTO.getPfUser().getPfUsersEmails() != null
										&& !usersCommentDTO.getPfUser().getPfUsersEmails().isEmpty()) {
									for (PfUsersEmailDTO userEmail : usersCommentDTO.getPfUser().getPfUsersEmails()) {
										if (userEmail.getLnotify() == true
												&& !receivers.contains(userEmail.getDemail())) {
											receivers.add(userEmail.getDemail());
										}
									}
								}
							}
						} else if (type.equals(Constants.SMS_NOTICE)) {
							if (usersCommentDTO.getPfUser().getPfUsersMobiles() != null
									&& !usersCommentDTO.getPfUser().getPfUsersMobiles().isEmpty()) {
								for (PfMobileUsersDTO userMobile : usersCommentDTO.getPfUser().getPfUsersMobiles()) {
									if (userMobile.getLnotify() == true
											&& !receivers.contains(userMobile.getDmobile())) {
										receivers.add(userMobile.getDmobile());
									}
								}
							}
						}
						
					}	
				}
			}
			
		}
		log.info("getReceivers end");
		return receivers;
	}

	private boolean esFirmaVB(PfHistoricRequestsDTO lastState) {
		return lastState != null
				&& (lastState.getChistoricRequest().equals(Constants.C_HISTORIC_REQUEST_SIGNED)
						|| lastState.getChistoricRequest().equals(Constants.C_HISTORIC_REQUEST_PASSED));
	}

	private boolean esDevuelta(PfHistoricRequestsDTO lastState) {
		return lastState != null
				&& (lastState.getChistoricRequest().equals(Constants.C_HISTORIC_REQUEST_REJECTED)
						|| lastState.getChistoricRequest().equals(Constants.C_HISTORIC_REQUEST_REMOVED));
	}
	
	private boolean esLeida(PfHistoricRequestsDTO lastState) {
		return lastState != null
				&& (lastState.getChistoricRequest().equals(Constants.C_HISTORIC_REQUEST_READ));
	}

	private boolean estaFirmadaPorTodos(PfRequestsDTO req) {
		boolean esUltimoFirmante = true;
		for(PfRequestTagsDTO reqTag: req.getPfRequestsTags()){
			if(Constants.C_TYPE_TAG_STATE.equalsIgnoreCase(reqTag.getPfTag().getCtype())){
				if (!Constants.C_TAG_SIGNED.equals(reqTag.getPfTag().getCtag())
						&& !Constants.C_TAG_PASSED.equals(reqTag.getPfTag().getCtag())) {
					esUltimoFirmante = false;
				}
			}
		}
		return esUltimoFirmante;
	}

	private List<String> getAuthorizationReceiver(PfUsersAuthorizationDTO aut, String event, String type) {
		List<String> receivers = new ArrayList<String>();

		PfUsersDTO authorizedUser = (PfUsersDTO) baseDAO.queryElementOneParameter("request.queryUserContact", "user", aut.getPfAuthorizedUser());
		PfUsersDTO user 		  = (PfUsersDTO) baseDAO.queryElementOneParameter("request.queryUserContact", "user", aut.getPfUser());

		if (type.equals(Constants.EMAIL_NOTICE)) {
			if (event.equals(Constants.NOTICE_NEW_AUTHORIZATION) ||
				event.equals(Constants.NOTICE_ADMIN_AUTHORIZATION)) {
				for (PfUsersEmailDTO userEmail : authorizedUser.getPfUsersEmails()) {
					if (userEmail.getLnotify() == true
							&& !receivers.contains(userEmail.getDemail())) {
						receivers.add(userEmail.getDemail());
					}
				}
			}
			else if (event.equals(Constants.NOTICE_AUTHORIZATION_ACCEPTED) ||
					 event.equals(Constants.NOTICE_AUTHORIZATION_DENIED)) {
				for (PfUsersEmailDTO userEmail : user.getPfUsersEmails()) {
					if (userEmail.getLnotify() == true
							&& !receivers.contains(userEmail.getDemail())) {
						receivers.add(userEmail.getDemail());
					}
				}
			}
		}
		else if (type.equals(Constants.SMS_NOTICE)) {
			if (event.equals(Constants.NOTICE_NEW_AUTHORIZATION) ||
				event.equals(Constants.NOTICE_ADMIN_AUTHORIZATION)) {
				for (PfMobileUsersDTO userMobile : authorizedUser.getPfUsersMobiles()) {
					if (userMobile.getLnotify() == true
							&& !receivers.contains(userMobile.getDmobile())) {
						receivers.add(userMobile.getDmobile());
					}
				}
			}
			else if (event.equals(Constants.NOTICE_AUTHORIZATION_ACCEPTED) ||
					 event.equals(Constants.NOTICE_AUTHORIZATION_DENIED)) {
				for (PfMobileUsersDTO userMobile : user.getPfUsersMobiles()) {
					if (userMobile.getLnotify() == true
							&& !receivers.contains(userMobile.getDmobile())) {
						receivers.add(userMobile.getDmobile());
					}
				}
			}
		}

		return receivers;
	}

	private List<String> getRequestexpiredReceivers(PfRequestsDTO req, String type) {
		List<String> receivers = new ArrayList<String>();

		if (req.getPfUsersRemitters() != null
				&& !req.getPfUsersRemitters().isEmpty()) {
			for (PfUsersRemitterDTO userRemitterDTO : req.getPfUsersRemitters()) {
				if (type.equals(Constants.EMAIL_NOTICE)) {
					if (userRemitterDTO.getPfUser().getPfUsersEmails() != null
							&& !userRemitterDTO.getPfUser().getPfUsersEmails().isEmpty()) {
						for (PfUsersEmailDTO userEmail : userRemitterDTO.getPfUser().getPfUsersEmails()) {
							if (userEmail.getLnotify() == true
									&& !receivers.contains(userEmail.getDemail())) {
								receivers.add(userEmail.getDemail());
							}
						}
					}
				} else if (type.equals(Constants.SMS_NOTICE)) {
					if (userRemitterDTO.getPfUser().getPfUsersMobiles() != null
							&& !userRemitterDTO.getPfUser().getPfUsersMobiles().isEmpty()) {
						for (PfMobileUsersDTO userMobile : userRemitterDTO.getPfUser().getPfUsersMobiles()) {
							if (userMobile.getLnotify() == true
									&& !receivers.contains(userMobile.getDmobile())) {
								receivers.add(userMobile.getDmobile());
							}
						}
					}
				}
			}
		}

		return receivers;
	}

	/**
	 * MÃ©todo que obtiene el/los email/emails de los administradores de la aplicaciÃ³n
	 * @return Listado de correos electrÃ³nicos
	 */
	private List<String> getEeutilExceptionReceivers() {
		List<String> resultado = new ArrayList<String>();
		PfConfigurationsParameterDTO emails = (PfConfigurationsParameterDTO)
				baseDAO.queryElementMoreParameters("administration.queryAdminEmailParameter", null);
		String[] emailChain = emails.getTvalue().split(",");
		for (int i = 0; i < emailChain.length; i++)
			resultado.add(emailChain[i]);
		
		return resultado;
	}

	/**
	 * M&eacute;todo que obtiene las direcciones de correo o los m&oacute;viles asociados a los cargos de un usuario.
	 * @param pfUser Usuario.
	 * @param type Tipo de notificaci&oacute;n (EMAIL, SMS).
	 * @return Listado de emails y m&oacute;viles de los cargos.
	 */
	private List<String> getJobReceivers(PfUsersDTO pfUser, String type) {
		ArrayList<String> jobReceivers = new ArrayList<String>();
		if (pfUser.getPfUsersJobs() != null
				&& !pfUser.getPfUsersJobs().isEmpty()) {
			for (PfUsersJobDTO userJob : pfUser.getPfUsersJobs()) {
					
				// Se aÃ±ade al usuario si el cargo no tiene fecha de caducidad o si el cargo tiene fecha de caducidad pero es posterior al momento actual
				if ((userJob.getFend() == null) || 
						(userJob.getFend() != null && userJob.getFend().after((new Date())))) {
	
						if (type.equals(Constants.EMAIL_NOTICE)) {
							if (userJob.getPfUser() != null
									&& !userJob.getPfUser().getPfUsersEmails().isEmpty()) {
								for (PfUsersEmailDTO userEmail : userJob.getPfUser().getPfUsersEmails()) {
									if (userEmail.getLnotify() == true) {
										jobReceivers.add(userEmail.getDemail());
									}
								}
							}
						} else if (type.equals(Constants.SMS_NOTICE)) {
							if (userJob.getPfUser() != null
									&& !userJob.getPfUser().getPfUsersMobiles().isEmpty()) {
								for (PfMobileUsersDTO userMobile : userJob.getPfUser().getPfUsersMobiles()) {
									if (userMobile.getLnotify() == true) {
										jobReceivers.add(userMobile.getDmobile());
									}
								}
							}
						}
	
					}
				}
		}
		return jobReceivers;
	}

	/**
	 * A&ntilde;ade al texto del email las fechas de validez de la petici&oacute;n en caso de que vengan informadas
	 * @param req Petici&oacute;n
	 * @param plainText Texto del email
	 * @return El texto del mail con las fechas a&ntilde;adidas seg&uacute;n el caso
	 */
	private StringBuilder putValidityDates(PfRequestsDTO req, StringBuilder plainText) {

		// If the start date has a not null value it will be written on the email text
		if (req.getFstart() != null) {
			//plainText.append(messages.getProperty(Constants.NOTICE_REQUEST_START_DATE)).append("\n");
		}

		// If the expiration date has a not null value it will be written on the email text
		if (req.getFexpiration() != null) {
			//plainText.append(messages.getProperty(Constants.NOTICE_REQUEST_EXPIRATION_DATE)).append("\n");
		}

		return plainText;
	}
}

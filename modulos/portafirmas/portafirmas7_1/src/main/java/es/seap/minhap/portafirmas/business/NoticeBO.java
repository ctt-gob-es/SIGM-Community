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
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.business.configuration.PushService;
import es.seap.minhap.portafirmas.business.ws.QueryServiceBO;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfCommentsDTO;
import es.seap.minhap.portafirmas.domain.PfConfigurationsParameterDTO;
import es.seap.minhap.portafirmas.domain.PfHistoricRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfNoticeRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestTagsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfSignLinesDTO;
import es.seap.minhap.portafirmas.domain.PfSignersDTO;
import es.seap.minhap.portafirmas.domain.PfUsersAuthorizationDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.domain.PfUsersJobDTO;
import es.seap.minhap.portafirmas.domain.PfUsersRemitterDTO;
import es.seap.minhap.portafirmas.utils.ConfigurationUtil;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.notice.configuration.ExternalAppNoticeConfiguration;
import es.seap.minhap.portafirmas.utils.notice.exception.NoticeException;
import es.seap.minhap.portafirmas.utils.notice.service.NoticeService;
import es.seap.minhap.portafirmas.web.beans.FileAttachedDTO;
import es.seap.minhap.portafirmas.ws.advice.client.WSConstants;
import es.seap.minhap.portafirmas.ws.bean.Request;


@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class NoticeBO {

	@Autowired
	private BaseDAO baseDAO;

	@Autowired
	private NoticeService noticeService;

	@Autowired
	private PushService pushService;

	@Autowired
	private ApplicationBO applicationBO;

	@Autowired
	private QueryServiceBO queryServiceBO;

	Logger log = Logger.getLogger(NoticeBO.class);
	
	
	/**
	 * Env&iacute;a las notificaciones de la lista que pasamos como par&aacute;metro v&iacute;a email a otros
	 * aplicaciones externas si est&aacute; marcado como necesario el env&iacute;o en los par&aacute;metros de 
	 * la aplicaci&oacute;n
	 * @param absList la lista de DTOs, pueden ser PfRequestsDTO o PfRequestsTagsDTO 
	 * @throws NoticeException si se produce alg&uacute;n error en el env&iacute;o
	 * @see es.seap.minhap.portafirmas.utils.notice.service.NoticeService#doExternalAppNotice(ExternalAppNoticeConfiguration)
	 */
	public void sendAdviceToAppServer(List<AbstractBaseDTO> absList) throws NoticeException {
		sendAdviceToAppServer(absList, false);
	}
	
	public void sendAdviceToAppServerViaFile(List<AbstractBaseDTO> absList) throws NoticeException {
		sendAdviceToAppServer(absList, true);
	}

	private void sendAdviceToAppServer(List<AbstractBaseDTO> absList, boolean viaFichero) throws NoticeException {
		try {
			
			//baseDAO.flush();
	        //baseDAO.clearSession(); 
			Long pfRequestPk = null;
			for (AbstractBaseDTO abs: absList) {
				
				if (abs instanceof PfRequestTagsDTO) {
					PfRequestTagsDTO rt = (PfRequestTagsDTO) abs;
					pfRequestPk = rt.getPfRequest().getPrimaryKey();
				} else if (abs instanceof PfRequestsDTO){
					PfRequestsDTO req = (PfRequestsDTO) abs;
					pfRequestPk = req.getPrimaryKey();
				}
				
				//PfRequestTagsDTO rt = (PfRequestTagsDTO) abs;
				//Obtiene la petici&oacute;n de la base de datos por clave primaria
				//PfRequestsDTO requestDTO = (PfRequestsDTO)baseDAO.queryElementOneParameter("request.primaryKey", "requestId", rt.getPfRequest().getPrimaryKey());
				
				//Obtiene la petici&oacute;n de la base de datos por clave primaria
				PfRequestsDTO requestDTO = (PfRequestsDTO)baseDAO.queryElementOneParameter("request.primaryKeyApp", "requestId", pfRequestPk);
				//Obtiene el objeto de petici&oacute;n
				Request request = queryServiceBO.queryRequestNoAuthentication(requestDTO.getChash());
				//Si la petici&oacute;n ha sido aceptada o rechazada

				//Class used to store configuration data for email server and user who sends email notifications
				ExternalAppNoticeConfiguration noticeConfiguration = new ExternalAppNoticeConfiguration();
				noticeConfiguration.setRequest(request);
					
				//Mapa con los valores de los parÃ¡metros de la aplicaciÃ³n
				Map<String, String> mapaParametros = applicationBO.getMapaParametrosAplicacion(requestDTO.getPfApplication());

				String necesarioAviso = mapaParametros.get(Constants.APP_PARAMETER_RESPUESTA_WS_ACTIVA);
				String necesarioAvisoIntermedios = mapaParametros.get(Constants.APP_PARAMETER_RESPUESTA_WS_NOTIFINTERMEDIOS);
					
				//Si es necesario enviar un aviso hacemos el env&iacute;o
				if ((Constants.C_YES.equals(necesarioAvisoIntermedios)
						|| (Constants.C_YES.equals(necesarioAviso) && isStateToSendNoticeReqFinished(request.getRequestStatus())))
						|| viaFichero) {
					
				
					//recuperamos los par&aacute;metros de env&iacute;o
					String wsdlLocation = mapaParametros.get(Constants.APP_PARAMETER_RESPUESTA_WS_WSDLLOCATION);
					String userName = mapaParametros.get(Constants.APP_PARAMETER_RESPUESTA_WS_USUARIO);
					String password = mapaParametros.get(Constants.APP_PARAMETER_RESPUESTA_WS_PASSWORD);
					//Si hemos recuperado los par&aacute;metros se reliza el env&iacute;o
					if (wsdlLocation != null && userName != null && password != null) {
						noticeConfiguration.setWsdlLocation(wsdlLocation);
						noticeConfiguration.setUserName(userName);
						noticeConfiguration.setPassword(password);
							
						/*List<SignatureSerializable> firmas = new ArrayList<SignatureSerializable>();
						//Obtiene las firmas de los documentos de la petici&oacute;n
						for (Document document: request.getDocumentList().getDocument()) {
							SignatureSerializable signature = queryServiceBO.downloadSignSerializable(document.getIdentifier());
							firmas.add(signature);
						}
						noticeConfiguration.setSignatures(firmas.toArray(new SignatureSerializable[0]));*/
						//Enviamos la notificaci&oacute;n por email
						noticeService.doExternalAppNotice(noticeConfiguration, request.getIdentifier());									
					}
				}
			} 

		} catch (Exception e) {
			log.error("Error notifying to external webservice: ", e);		
		}		
	}
	private boolean isStateToSendNoticeReqFinished (String status) {
		boolean is = false;
		if (WSConstants.REQUEST_STATE_ACCEPTED.equals(status) || 
				WSConstants.REQUEST_STATE_REJECTED.equals(status) ||
				WSConstants.REQUEST_STATE_REMOVED.equals(status) ||
				WSConstants.REQUEST_STATE_EXPIRED.equals(status)) {			
			is = true;
		}
		return is;
				
	}
		

	/**
	 * Recupera los par&aacute;metros de configuraci&oacute;n de tipo 'NOTIFICACION.%', en un mapa
	 * que contiene como clave el c&oacute;digo del par&aacute;metro y como valor el valor del par&aacute;metro de configuraci&oacute;n
	 * @return el mapa con los par&aacute;metros de configuraci&oacute;n, la clave el c&oacute;digo del par&aacute;metro
	 * y valor el valor del par&aacute;metro de configuraci&oacute;n
	 */
	public Map<String, String> queryNoticeConfigurationParameterList() {
		log.info("queryNoticeConfigurationParameterList init");
		List<AbstractBaseDTO> configurationParameters = baseDAO.queryListMoreParameters("request.queryNoticeConfigurationParameterList", null);
		Map<String, String> mailParameters = new HashMap<String, String>();
		//Recuperala lista de constantes de par&aacute;metros de notificaci&oacute;n
		List<String> parameterNames = getNotificationParameterNames();
		//Si las listas no estan vac&iacute;as
		//Crea el mapa con clave el c&oacute;digo del par&aacute;metro y como valor el valor del par&aacute;metro de configuraci&oacute;n
		if (configurationParameters != null	&& !configurationParameters.isEmpty()) {
			for (AbstractBaseDTO abstractBaseDTO: configurationParameters) {
				PfConfigurationsParameterDTO configParam = (PfConfigurationsParameterDTO) abstractBaseDTO;				
				if (parameterNames.contains(configParam.getPfParameter().getCparameter())) {
					String valorParametro = ConfigurationUtil.recuperaValorParametroYSustituyeEntorno(configParam);
					mailParameters.put(configParam.getPfParameter().getCparameter(), valorParametro);
				}
			}
		}
		log.info("queryNoticeConfigurationParameterList end");
		return mailParameters;
	}
	/**
	 * Recupera la lista de constantes de par&aacute;metros de notificaci&oacute;n
	 * @return la lista de par&aacute;metros de notificaci&oacute;n
	 * @see es.seap.minhap.portafirmas.utils.Constants#EMAIL_REMITTER_NAME
	 * @see es.seap.minhap.portafirmas.utils.Constants#EMAIL_USER
	 * @see es.seap.minhap.portafirmas.utils.Constants#EMAIL_PASSWORD
	 * @see es.seap.minhap.portafirmas.utils.Constants#EMAIL_REMITTER
	 * @see es.seap.minhap.portafirmas.utils.Constants#SMTP_SERVER	 * 
	 * @see es.seap.minhap.portafirmas.utils.Constants#SMTP_PORT
	 * @see es.seap.minhap.portafirmas.utils.Constants#AUTHENTICATION_MODE
	 */
	private List<String> getNotificationParameterNames() {
		log.info("getNotificationParameterNames init");
		List<String> emailParametersNames = new ArrayList<String>();
		emailParametersNames.add(Constants.EMAIL_REMITTER_NAME);
		emailParametersNames.add(Constants.EMAIL_USER);
		emailParametersNames.add(Constants.EMAIL_PASSWORD);
		emailParametersNames.add(Constants.EMAIL_REMITTER);
		emailParametersNames.add(Constants.SMTP_SERVER);
		emailParametersNames.add(Constants.SMTP_PORT);
		emailParametersNames.add(Constants.AUTHENTICATION_MODE);
		log.info("getNotificationParameterNames end");
		return emailParametersNames;
	}
	
	public AbstractBaseDTO loadMessageData(AbstractBaseDTO abstractDTO) {
		if(abstractDTO instanceof PfRequestsDTO) {
			return loadMessageDataFromRequest((PfRequestsDTO) abstractDTO);
		}
		if(abstractDTO instanceof PfUsersAuthorizationDTO) {
			return loadMessageDataFromAut((PfUsersAuthorizationDTO) abstractDTO);
		}
		if(abstractDTO instanceof PfRequestTagsDTO) {
			return abstractDTO;
		}
		return null;
	}
	
	/**
	 * Carga en la petici&oacute;n que pasamos como par&aacute;metro sus peticiones hist&oacute;ricas, sus comentarios,
	 * sus usuarios_remitente y sus lineas de firma
	 * @param req la petici&oacute;n
	 * @return la petici&oacute;n con los datos cargados
	 */
	public PfRequestsDTO loadMessageDataFromRequest(PfRequestsDTO req) {
		log.info("loadMessageDataFromRequest init");
		// Query data	
		PfRequestsDTO request = (PfRequestsDTO) baseDAO.queryElementOneParameter("request.queryRequest", "req", req);
		List<AbstractBaseDTO> historicRequest = baseDAO.queryListOneParameter("request.queryHistoricFromRequest", "req", request);
		List<AbstractBaseDTO> comments = baseDAO.queryListOneParameter("request.queryCommentsFromRequest", "req", request);	
		List<AbstractBaseDTO> remitters = baseDAO.queryListOneParameter("request.queryRemittersFromRequest", "req", request);
		List<AbstractBaseDTO> signLines = baseDAO.queryListOneParameter("request.querySignLinesFromRequest", "req", request);

		// Load data into dto object
		request.setPfHistoricRequests(abstractListToPfHistoricRequestSet(historicRequest));
		request.setPfComments(abstractListToPfCommentSet(comments));	
		request.setPfUsersRemitters(abstractListToRemitterSet(remitters));

		// Check if any signer is job and query related users to these job
		for (AbstractBaseDTO signLine : signLines) {
			PfSignLinesDTO signLineDTO = (PfSignLinesDTO) signLine;
			if (signLineDTO.getPfSigners() != null	&& !signLineDTO.getPfSigners().isEmpty()) {
				for (AbstractBaseDTO signer : signLineDTO.getPfSigners()) {
					PfSignersDTO signerDTO = (PfSignersDTO) signer;
					if (signerDTO.getPfUser() != null && signerDTO.getPfUser().isJob()) {
						List<AbstractBaseDTO> userJobs = baseDAO.queryListOneParameter("request.queryUsersFromJob", "job", signerDTO.getPfUser());
						Set<PfUsersJobDTO> usersJobSet = abstractListToUsersJobSet(userJobs);
						signerDTO.getPfUser().setPfUsersJobs(usersJobSet);
					}
				}
			}
		}
		request.setPfSignsLines(abstractListToSignLinesSet(signLines));
		log.info("loadMessageDataFromRequest end");
		return request;
	}

	public PfUsersAuthorizationDTO loadMessageDataFromAut(PfUsersAuthorizationDTO authorization) {
		log.info("loadMessageDataFromAut init");
		// Query data	
		PfUsersAuthorizationDTO autorizacion = 
			(PfUsersAuthorizationDTO) baseDAO.queryElementOneParameter("request.queryAuthorization", "aut", authorization);
		log.info("loadMessageDataFromAut end");
		return autorizacion;
	}


	/**
	 * Transforma la lista de historico de peticiones que se pasa como par&aacute;metro
	 * en &uacute;n conjunto de historico de peticiones
	 * @param abstractList la lista de historico de peticiones
	 * @return el conjunto de historico de peticiones
	 */
	private Set<PfHistoricRequestsDTO> abstractListToPfHistoricRequestSet(List<AbstractBaseDTO> abstractList) {		
		Set<PfHistoricRequestsDTO> historicRequestSet = new HashSet<PfHistoricRequestsDTO>();
		if (abstractList != null && !abstractList.isEmpty()) {
			for (AbstractBaseDTO abstractElement : abstractList) {
				PfHistoricRequestsDTO historicRequest = (PfHistoricRequestsDTO) abstractElement;
				historicRequestSet.add(historicRequest);
			}
		}		
		return historicRequestSet;
	}
	/**
	 * Transforma la lista de comentarios que se pasa como par&aacute;metro
	 * en &uacute;n conjunto de comentarios
	 * @param abstractList la lista de comentarios
	 * @return el conjunto de comentarios
	 */
	private Set<PfCommentsDTO> abstractListToPfCommentSet(List<AbstractBaseDTO> abstractList) {
		Set<PfCommentsDTO> commentsSet = new HashSet<PfCommentsDTO>();

		if (abstractList != null && !abstractList.isEmpty()) {
			for (AbstractBaseDTO abstractElement : abstractList) {
				PfCommentsDTO comment = (PfCommentsDTO) abstractElement;
				commentsSet.add(comment);
			}
		}
		return commentsSet;
	}
	/**
	 * Transforma la lista de usuarios_remitente que se pasa como par&aacute;metro
	 * en &uacute;n conjunto de usuarios_remitente
	 * @param abstractList la lista de usuarios_remitente
	 * @return el conjunto de usuarios_remitente
	 */
	private Set<PfUsersRemitterDTO> abstractListToRemitterSet(
			List<AbstractBaseDTO> abstractList) {
		Set<PfUsersRemitterDTO> userRemittersSet = new HashSet<PfUsersRemitterDTO>();
		if (abstractList != null && !abstractList.isEmpty()) {
			for (AbstractBaseDTO abstractElement : abstractList) {
				PfUsersRemitterDTO userRemitter = (PfUsersRemitterDTO) abstractElement;
				userRemittersSet.add(userRemitter);
			}
		}
		return userRemittersSet;
	}
	/**
	 * Transforma la lista de lineas de firma que se pasa como par&aacute;metro
	 * en &uacute;n conjunto de lineas de firma
	 * @param abstractList la lista de lineas de firma
	 * @return el conjunto de lineas de firma
	 */
	private Set<PfSignLinesDTO> abstractListToSignLinesSet(
			List<AbstractBaseDTO> abstractList) {
		Set<PfSignLinesDTO> signLinesSet = new HashSet<PfSignLinesDTO>();
		if (abstractList != null && !abstractList.isEmpty()) {
			for (AbstractBaseDTO abstractElement : abstractList) {
				PfSignLinesDTO signLine = (PfSignLinesDTO) abstractElement;
				signLinesSet.add(signLine);
			}
		}
		return signLinesSet;
	}
	/**
	 * Transforma la lista de usuarios_cargo que se pasa como par&aacute;metro
	 * en &uacute;n conjunto de usuarios_cargo
	 * @param abstractList la lista de usuarios_cargo
	 * @return el conjunto de usuarios_cargo
	 */
	private Set<PfUsersJobDTO> abstractListToUsersJobSet(
			List<AbstractBaseDTO> abstractList) {
		Set<PfUsersJobDTO> usersJobSet = new HashSet<PfUsersJobDTO>();
		if (abstractList != null && !abstractList.isEmpty()) {
			for (AbstractBaseDTO abstractElement : abstractList) {
				PfUsersJobDTO userJob = (PfUsersJobDTO) abstractElement;
				usersJobSet.add(userJob);
			}
		}
		return usersJobSet;
	}

	/**
	 * Check if request received should be noticed when its state is received by
	 * parameter.
	 * 
	 * @param requestInbox
	 *            {@link PfRequestsDTO} request to evaluate.
	 * @param tagRead
	 *            String containing name of state to check.
	 * @return true if request state change should be noticed
	 */
	public boolean noticeWhenState(PfRequestsDTO requestInbox, String tagRead) {
		log.info("noticeWhenState init");
		boolean notice = false;
		Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("req", requestInbox);
		queryParams.put("cTag", tagRead);
		PfNoticeRequestsDTO noticeRequest = (PfNoticeRequestsDTO) baseDAO
				.queryElementMoreParameters("request.queryNoticeRequestByCtag",
						queryParams);
		if (noticeRequest != null) {
			notice = true;
		}
		log.info("noticeWhenState end");
		return notice;
	}
	/**
	 * Env&iacute;a la notificaci&oacute;n v&iacute;a email, sms o ambas dependiendo de los par&aacute;metros de entrada
	 * @param request la petici&oacute;n
	 * @param emailActivated marca si se env&iacute;a la notificaci&oacute;n por email
	 * @param smsActivated marca si se env&iacute;a la notificaci&oacute;n por sms
	 * @see es.seap.minhap.portafirmas.utils.notice.service.NoticeService#doNotice(String, String, PfRequestsDTO)
	 */
	public void noticeStateChange(PfRequestsDTO request,
			boolean emailActivated, boolean smsActivated) {
		try {
			if (emailActivated) {
				// check if email notices are active and execute it
				noticeService.doNotice(Constants.EMAIL_NOTICE, Constants.NOTICE_EVENT_STATE_CHANGE, request);
			}
			// check if SMS notices are active and execute it
			if (smsActivated) {
				// check if email notices are active and execute it
				noticeService.doNotice(Constants.SMS_NOTICE, Constants.NOTICE_EVENT_STATE_CHANGE, request);
			}
			
			//[Agustin]
			//pushService.doNotice(Constants.NOTICE_EVENT_STATE_CHANGE, request);
		} catch (NoticeException e) {
			log.error("Error executing notice: ", e);
		}
	}
	/**
	 * Env&iacute;a un comentario por el servicio de noticias, si el par&aacute;metro emailActivated es true
	 * lo enviar&aacute; por email, si el par&aacute;metro smsActivated es true lo enviar&aacute; tambi&eacute;n por sms
	 * @param request peticion
	 * @param emailActivated activa o no el env&iacute;o por email
	 * @param smsActivated activa o no el env&iacute;o por sms
	 * @see es.seap.minhap.portafirmas.utils.notice.service.NoticeService#doNotice(String, String, PfRequestsDTO)
	 * @see es.seap.minhap.portafirmas.utils.Constants#EMAIL_NOTICE
	 * @see es.seap.minhap.portafirmas.utils.Constants#SMS_NOTICE
	 */
	public void noticeNewComment(PfRequestsDTO request, boolean emailActivated,
			boolean smsActivated) {
		try {
			if (emailActivated) {
				// check if email notices are active and execute it
				noticeService.doNotice(Constants.EMAIL_NOTICE, Constants.NOTICE_EVENT_NEW_COMMENT, request);
			}
			// check if SMS notices are active and execute it
			if (smsActivated) {
				// check if email notices are active and execute it
				noticeService.doNotice(Constants.SMS_NOTICE, Constants.NOTICE_EVENT_NEW_COMMENT, request);
			}
			
//				pushService.doNotice(Constants.NOTICE_EVENT_NEW_COMMENT, request);
		} catch (NoticeException e) {
			log.error("Error executing notice: ", e);
		}
	}
	
	/**
	 * Activa y ejecuta una notificaci&oacute;n por email si est&aacute; activado notificar por eMail
	 * y/o por sms
	 * @param request
	 * @see es.seap.minhap.portafirmas.utils.notice.service.NoticeService#doNotice(String, String, PfRequestsDTO)
	 */
	public void noticeForwardRequest(PfRequestsDTO request) {
		try {
			PfConfigurationsParameterDTO emailNoticeParameter = applicationBO.queryEmailNoticeParameter();
			PfConfigurationsParameterDTO smsNoticeParameter = applicationBO.querySMSNoticeParameter();
			if (emailNoticeParameter != null && emailNoticeParameter.getTvalue().equals(Constants.C_YES)) {
				// check if email notices are active and execute it
				noticeService.doNotice(Constants.EMAIL_NOTICE, Constants.NOTICE_EVENT_FORWARD_REQUEST, request);
			}
			// check if SMS notices are active and execute it
			if (smsNoticeParameter != null && smsNoticeParameter.getTvalue().equals(Constants.C_YES)) {
				// check if email notices are active and execute it
				noticeService.doNotice(Constants.SMS_NOTICE, Constants.NOTICE_EVENT_FORWARD_REQUEST, request);
			}
			
//				pushService.doNotice(Constants.NOTICE_EVENT_NEW_REQUEST, request);
		} catch (NoticeException e) {
			log.error("Error executing notice: ", e);
		}
	}
	
	public void noticeForwardRequest(PfRequestsDTO request, List<PfUsersDTO> forwardedUsers) {
		try {
			PfConfigurationsParameterDTO emailNoticeParameter = applicationBO.queryEmailNoticeParameter();
			PfConfigurationsParameterDTO smsNoticeParameter = applicationBO.querySMSNoticeParameter();
			if (emailNoticeParameter != null && emailNoticeParameter.getTvalue().equals(Constants.C_YES)) {
				// check if email notices are active and execute it
				noticeService.doNotice(Constants.EMAIL_NOTICE, Constants.NOTICE_EVENT_FORWARD_REQUEST, request, forwardedUsers);
				noticeService.doNotice(Constants.EMAIL_NOTICE, Constants.NOTICE_EVENT_FORWARD_REQUEST_REMITENTE, request, forwardedUsers);
			}
			// check if SMS notices are active and execute it
			if (smsNoticeParameter != null && smsNoticeParameter.getTvalue().equals(Constants.C_YES)) {
				// check if email notices are active and execute it
				noticeService.doNotice(Constants.SMS_NOTICE, Constants.NOTICE_EVENT_FORWARD_REQUEST, request, forwardedUsers);
				noticeService.doNotice(Constants.SMS_NOTICE, Constants.NOTICE_EVENT_FORWARD_REQUEST_REMITENTE, request, forwardedUsers);
			}
			
//				pushService.doNotice(Constants.NOTICE_EVENT_NEW_REQUEST, request);
		} catch (NoticeException e) {
			log.error("Error executing notice: ", e);
		}
	}
	
	public void noticeForwardRequest(PfRequestsDTO request, PfUsersDTO forwardedUser) {
		List<PfUsersDTO> forwardedUsers = new ArrayList<PfUsersDTO>();
		forwardedUsers.add(forwardedUser);
		noticeForwardRequest(request, forwardedUsers);
	}

	
	/**
	 * Activa y ejecuta una notificaci&oacute;n por email si est&aacute; activado notificar por eMail
	 * y/o por sms
	 * @param request
	 * @see es.seap.minhap.portafirmas.utils.notice.service.NoticeService#doNotice(String, String, PfRequestsDTO)
	 */
	public void noticeNewRequest(PfRequestsDTO request) {
		try {
			PfConfigurationsParameterDTO emailNoticeParameter = applicationBO.queryEmailNoticeParameter();
			PfConfigurationsParameterDTO smsNoticeParameter = applicationBO.querySMSNoticeParameter();
			if (emailNoticeParameter != null && emailNoticeParameter.getTvalue().equals(Constants.C_YES)) {
				// check if email notices are active and execute it
				noticeService.doNotice(Constants.EMAIL_NOTICE, Constants.NOTICE_EVENT_NEW_REQUEST, request);
			}
			// check if SMS notices are active and execute it
			if (smsNoticeParameter != null && smsNoticeParameter.getTvalue().equals(Constants.C_YES)) {
				// check if email notices are active and execute it
				noticeService.doNotice(Constants.SMS_NOTICE, Constants.NOTICE_EVENT_NEW_REQUEST, request);
			}
			
//				pushService.doNotice(Constants.NOTICE_EVENT_NEW_REQUEST, request);
		} catch (NoticeException e) {
			log.error("Error executing notice: ", e);
		}
	}

	/**
	 * Activa y ejecuta una notificaci&oacute;n a los validadores por email si est&aacute; activado notificar por eMail
	 * y/o por sms
	 * @param request
	 * @see es.seap.minhap.portafirmas.utils.notice.service.NoticeService#doNotice(String, String, PfRequestsDTO)
	 */
	public void noticeNewRequestValidador(PfRequestsDTO request) {
		try {
			PfConfigurationsParameterDTO emailNoticeParameter = applicationBO.queryEmailNoticeParameter();
			if (emailNoticeParameter != null && emailNoticeParameter.getTvalue().equals(Constants.C_YES)) {
				// check if email notices are active and execute it
				noticeService.doNotice(Constants.EMAIL_NOTICE, Constants.NOTICE_EVENT_NEW_REQUEST_VALIDADOR, request);
			}
		} catch (NoticeException e) {
			log.error("Error executing notice: ", e);
		}
	}

	
	/**
	 * Activa y ejecuta una notificaci&oacute;n por email si est&aacute; activado 'emailActivated'
	 * y por sms si est&aacute; activado 'smsActivated'
	 * @param request
	 * @param emailActivated especifica si se env&iacute;a la notificaci&oacute;n por email
	 * @param smsActivated especifica si se env&iacute;a la notificaci&oacute;n por sms
	 * @see es.seap.minhap.portafirmas.utils.notice.service.NoticeService#doNotice(String, String, PfRequestsDTO)
	 */
	public void noticeNewRequest(PfRequestsDTO request, boolean emailActivated, boolean smsActivated) {
		try {
			if (emailActivated) {
				// check if email notices are active and execute it
				noticeService.doNotice(Constants.EMAIL_NOTICE, Constants.NOTICE_EVENT_NEW_REQUEST, request);
			}
			// check if SMS notices are active and execute it
			if (smsActivated) {
				// check if email notices are active and execute it
				noticeService.doNotice(Constants.SMS_NOTICE, Constants.NOTICE_EVENT_NEW_REQUEST, request);
			}
			
			//[Agustin]
			//pushService.doNotice(Constants.NOTICE_EVENT_NEW_REQUEST, request);
			

		} catch (NoticeException e) {
			log.error("Error executing notice: ", e);
		}
	}
	
	/**
	 * Activa y ejecuta una notificaci&oacute;n por email si est&aacute; activado 'emailActivated'
	 * y por sms si est&aacute; activado 'smsActivated'
	 * @param request
	 * @param emailActivated especifica si se env&iacute;a la notificaci&oacute;n por email
	 * @param smsActivated especifica si se env&iacute;a la notificaci&oacute;n por sms
	 * @see es.seap.minhap.portafirmas.utils.notice.service.NoticeService#doNotice(String, String, PfRequestsDTO)
	 */
	public void noticeSiguienteFirmante(PfRequestsDTO request, boolean emailActivated, boolean smsActivated) {
		try {
			if (emailActivated) {
				// check if email notices are active and execute it
				noticeService.doNotice(Constants.EMAIL_NOTICE, Constants.NOTICE_SIGNED, request);
			}
			// check if SMS notices are active and execute it
			if (smsActivated) {
				// check if email notices are active and execute it
				noticeService.doNotice(Constants.SMS_NOTICE, Constants.NOTICE_SIGNED, request);
			}
			
			//[Agustin]
			//pushService.doNotice(Constants.NOTICE_EVENT_NEW_REQUEST, request);
			

		} catch (NoticeException e) {
			log.error("Error executing notice: ", e);
		}
	}
	
	/**
	 * Activa y ejecuta una notificaci&oacute;n por email a validadors si est&aacute; activado 'emailActivated'
	 * y por sms si est&aacute; activado 'smsActivated'
	 * @param request
	 * @param emailActivated especifica si se env&iacute;a la notificaci&oacute;n por email
	 * @param smsActivated especifica si se env&iacute;a la notificaci&oacute;n por sms
	 * @see es.seap.minhap.portafirmas.utils.notice.service.NoticeService#doNotice(String, String, PfRequestsDTO)
	 */
	public void noticeNewRequestValidador(PfRequestsDTO request, boolean emailActivated, boolean smsActivated) {
		try {
			if (emailActivated) {
				// enviamos notificacion a los validadores
				noticeService.doNotice(Constants.EMAIL_NOTICE, Constants.NOTICE_EVENT_NEW_REQUEST_VALIDADOR, request);
			}
			
		} catch (NoticeException e) {
			log.error("Error executing notice: ", e);
		}
	}

	
	public void noticeNewInvitationRequest(PfRequestsDTO invitedRequestsDTO){
		try{
			noticeService.doNotice(Constants.INVITATION_NOTICE, Constants.NOTICE_EVENT_INVITED_REQUEST, invitedRequestsDTO);
		} catch (NoticeException e) {
			log.error("ERROR:NoticeBOError.noticeNewInvitationRequest: executing invitation notice: ", e);
		}
	}

	public void noticeNewAuthorization(PfUsersAuthorizationDTO authorization, boolean emailActivated,
			boolean smsActivated) {
		try {
			if (emailActivated) {
				// check if email notices are active and execute it
				noticeService.doNotice(Constants.EMAIL_NOTICE, Constants.NOTICE_NEW_AUTHORIZATION, authorization);
			}
			// check if SMS notices are active and execute it
			if (smsActivated) {
				// check if email notices are active and execute it
				noticeService.doNotice(Constants.SMS_NOTICE, Constants.NOTICE_NEW_AUTHORIZATION, authorization);
			}

		} catch (NoticeException e) {
			log.error("Error executing notice: ", e);
		}
	}

	public void noticeAdmAuthorization(PfUsersAuthorizationDTO authorization,
									   boolean emailActivated, boolean smsActivated) {
		try {
			if (emailActivated) {
				// check if email notices are active and execute it
				noticeService.doNotice(Constants.EMAIL_NOTICE, Constants.NOTICE_ADMIN_AUTHORIZATION, authorization);
			}
			// check if SMS notices are active and execute it
			if (smsActivated) {
				// check if email notices are active and execute it
				noticeService.doNotice(Constants.SMS_NOTICE, Constants.NOTICE_ADMIN_AUTHORIZATION, authorization);
			}

		} catch (NoticeException e) {
			log.error("Error executing notice: ", e);
		}
	}

	public void noticeAcceptedAuthorization(PfUsersAuthorizationDTO authorization, boolean emailActivated,
			boolean smsActivated) {
		try {
			if (emailActivated) {
				// check if email notices are active and execute it
				noticeService.doNotice(Constants.EMAIL_NOTICE, Constants.NOTICE_AUTHORIZATION_ACCEPTED, authorization);
			}
			// check if SMS notices are active and execute it
			if (smsActivated) {
				// check if email notices are active and execute it
				noticeService.doNotice(Constants.SMS_NOTICE, Constants.NOTICE_AUTHORIZATION_ACCEPTED, authorization);
			}

		} catch (NoticeException e) {
			log.error("Error executing notice: ", e);
		}
	}

	public void noticeDeniedAuthorization(PfUsersAuthorizationDTO authorization, boolean emailActivated,
			boolean smsActivated) {
		try {
			if (emailActivated) {
				// check if email notices are active and execute it
				noticeService.doNotice(Constants.EMAIL_NOTICE, Constants.NOTICE_AUTHORIZATION_DENIED, authorization);
			}
			// check if SMS notices are active and execute it
			if (smsActivated) {
				// check if email notices are active and execute it
				noticeService.doNotice(Constants.SMS_NOTICE, Constants.NOTICE_AUTHORIZATION_DENIED, authorization);
			}

		} catch (NoticeException e) {
			log.error("Error executing notice: ", e);
		}
	}


	public void noticeRequestAnulled(PfRequestsDTO request, boolean emailActivated,
			boolean smsActivated, boolean anulled) {
		try {
			if (emailActivated) {
				// check if email notices are active and execute it
				if(anulled){
					noticeService.doNotice(Constants.EMAIL_NOTICE, Constants.NOTICE_EVENT_ANULLED_REQUEST, request);
				}else{
					noticeService.doNotice(Constants.EMAIL_NOTICE, Constants.NOTICE_EVENT_REMOVE_ANULLED_REQUEST, request);
				}
			}
			// check if SMS notices are active and execute it
			if (smsActivated) {
				// check if email notices are active and execute it
				if(anulled){
					noticeService.doNotice(Constants.SMS_NOTICE, Constants.NOTICE_REQUEST_ANULLED, request);
				}else{
					noticeService.doNotice(Constants.SMS_NOTICE, Constants.NOTICE_REQUEST_ANULLED_RECOVER, request);
				}
			}
			
//				pushService.doNotice(Constants.NOTICE_REQUEST_EXPIRED, request);
		} catch (NoticeException e) {
			log.error("Error executing notice: ", e);
		}
	}
	
	public void noticeRequestExpired(PfRequestsDTO request, boolean emailActivated,
			boolean smsActivated) {
		try {
			if (emailActivated) {
				// check if email notices are active and execute it
				noticeService.doNotice(Constants.EMAIL_NOTICE, Constants.NOTICE_REQUEST_EXPIRED, request);
			}
			// check if SMS notices are active and execute it
			if (smsActivated) {
				// check if email notices are active and execute it
				noticeService.doNotice(Constants.SMS_NOTICE, Constants.NOTICE_REQUEST_EXPIRED, request);
			}
			
//				pushService.doNotice(Constants.NOTICE_REQUEST_EXPIRED, request);
		} catch (NoticeException e) {
			log.error("Error executing notice: ", e);
		}
	}

	
	public void noticeReports(List<String> destinatarios, List<FileAttachedDTO> ficheros, String subjectRequest) {
		try {
			noticeService.doNoticeReports(Constants.EMAIL_NOTICE, Constants.NOTICE_EVENT_SEND_REPORTS, destinatarios, ficheros, subjectRequest);
		} catch (NoticeException e) {
			log.error("Error executing notice: ", e);
		}
	}
	
	public void noticeValidacion(PfRequestTagsDTO reqTag, boolean emailActivated, boolean smsActivated) {
		try {
			if (emailActivated) {
				noticeService.doNotice(Constants.EMAIL_NOTICE, Constants.NOTICE_EVENT_VALIDATED_REQUEST, reqTag);
			}
		} catch (NoticeException e) {
			log.error("Error executing notice: ", e);
		}
	}
	
	
	/**
	 * Método que genera un aviso por email a los administradores acerca de una excepción del servicio EEUtils
	 * @param request Petición que generó la excepción
	 * @param emailActivated Define si están activos los avisos por mail
	 */
	public void noticeEeutilException(PfRequestsDTO request, boolean emailActivated) {
		try {
			if (emailActivated) {
				noticeService.doNotice(Constants.EMAIL_NOTICE, Constants.NOTICE_EEUTIL_EXCEPTION, request);
			}
		} catch (NoticeException e) {
			log.error("Error executing notice: ", e);
		}
	}

	/**
	 * Método que define si está activa la notificación al administrador
	 * @return true si está activa, false en caso contrario
	 */
	public boolean isAdminNoticeEnabled() {
		boolean enabled = false;
		PfConfigurationsParameterDTO parameter = (PfConfigurationsParameterDTO)
			baseDAO.queryElementMoreParameters("administration.queryAdminEmailParameterEnabled", null);
		if (parameter.getTvalue().equals(Constants.C_YES)) {
			enabled = true;
		}
		return enabled;
	}
	
	/**
	 * Método que comprueba si todos los usuarios destinatarios pertenecen a Servicios Centrales
	 * @param request
	 * @return
	 */
//	private boolean isServiciosCentrales(PfRequestsDTO request){
//		boolean resultado = true;
//		request = requestBO.queryRequestHash(request.getChash());
//		for(PfSignLinesDTO lineaFirma : request.getPfSignsLinesList()){
//			for(PfSignersDTO firmante : lineaFirma.getPfSigners()){
//				if(!Constants.SERVICIOS_CENTRALES.equals(firmante.getPfUser().getPfProvince().getCnombre())){
//					resultado = false;
//				}
//			}	
//		}
//		
//		return resultado;
//	}

}

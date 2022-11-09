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

package es.seap.minhap.portafirmas.business.ws;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;
import javax.xml.ws.Holder;

import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.seap.minhap.portafirmas.actions.ApplicationVO;
import es.seap.minhap.portafirmas.business.NoticeBO;
import es.seap.minhap.portafirmas.business.RequestBO;
import es.seap.minhap.portafirmas.business.TagBO;
import es.seap.minhap.portafirmas.business.configuration.AuthorizationBO;
import es.seap.minhap.portafirmas.business.configuration.FilterBO;
import es.seap.minhap.portafirmas.business.configuration.ValidatorUsersConfBO;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfActionsDTO;
import es.seap.minhap.portafirmas.domain.PfApplicationsDTO;
import es.seap.minhap.portafirmas.domain.PfCommentsDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentScopesDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentTypesDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentsDTO;
import es.seap.minhap.portafirmas.domain.PfEmailsRequestDTO;
import es.seap.minhap.portafirmas.domain.PfFilesDTO;
import es.seap.minhap.portafirmas.domain.PfImportanceLevelsDTO;
import es.seap.minhap.portafirmas.domain.PfNoticeRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestTagsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsTextDTO;
import es.seap.minhap.portafirmas.domain.PfSignLinesDTO;
import es.seap.minhap.portafirmas.domain.PfSignersDTO;
import es.seap.minhap.portafirmas.domain.PfSignsDTO;
import es.seap.minhap.portafirmas.domain.PfTagsDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.domain.PfUsersRemitterDTO;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.SignDataUtil;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.utils.document.CustodyServiceException;
import es.seap.minhap.portafirmas.utils.document.CustodyServiceFactory;
import es.seap.minhap.portafirmas.utils.document.bean.CustodyServiceOutputDocument;
import es.seap.minhap.portafirmas.utils.document.service.CustodyServiceOutput;
import es.seap.minhap.portafirmas.utils.ws.WSUtil;
import es.seap.minhap.portafirmas.ws.bean.Action;
import es.seap.minhap.portafirmas.ws.bean.ActionList;
import es.seap.minhap.portafirmas.ws.bean.Authentication;
import es.seap.minhap.portafirmas.ws.bean.Document;
import es.seap.minhap.portafirmas.ws.bean.EnhancedUser;
import es.seap.minhap.portafirmas.ws.bean.EnhancedUserJobInfo;
import es.seap.minhap.portafirmas.ws.bean.ImportanceLevel;
import es.seap.minhap.portafirmas.ws.bean.NoticeList;
import es.seap.minhap.portafirmas.ws.bean.Request;
import es.seap.minhap.portafirmas.ws.bean.Seat;
import es.seap.minhap.portafirmas.ws.bean.SignLine;
import es.seap.minhap.portafirmas.ws.bean.Signer;
import es.seap.minhap.portafirmas.ws.bean.SignerList;
import es.seap.minhap.portafirmas.ws.bean.State;
import es.seap.minhap.portafirmas.ws.bean.StringList;
import es.seap.minhap.portafirmas.ws.bean.User;
import es.seap.minhap.portafirmas.ws.exception.PfirmaException;


@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class ModifyServiceBO {
	

	private static final String MSJ_PETICION_ENVIADA = "La petición de firma ya está enviada y ya no se puede actualizar.";

	Logger log = Logger.getLogger(ModifyServiceBO.class);

	@Autowired
	private BaseDAO baseDAO;

	@Autowired
	private QueryServiceBO queryServiceBO;

	@Autowired
	private NoticeBO noticeBO;

	@Autowired
	private FilterBO filterBO;

	@Autowired
	private TagBO tagBO;

	@Autowired
	private RequestBO requestBO;
	
	@Autowired
	private AdminServiceBO adminBO;

	@Autowired
	private AuthorizationBO authorizationBO;

	@Autowired
	private ApplicationVO applicationVO;
	
	@Autowired
	private AuthenticationWSHelper authenticationWSHelper;		

	@Autowired
	private SignDataUtil signDataUtil;
	
	@Autowired
	private CustodyServiceFactory custodyServiceFactory;
	
	@Autowired
	private ValidatorUsersConfBO validatorUsersConfBO;
		
	@Resource(name = "messageProperties")
	private Properties messages;
	/*
	 * WS METHODS
	 */
	/**
	 * Env&iacute;a una petici&oacute;n previamente creada
	 */
	@Transactional(readOnly = false, rollbackFor=PfirmaException.class)
	public void sendRequest(Authentication authentication, Holder<String> requestId) throws PfirmaException {
		try {
			log.warn("sendRequest init: " + requestId.value + " autenticación: " + authentication.getUserName());
			authenticationWSHelper.authenticateWebservice(authentication, es.seap.minhap.portafirmas.utils.ws.WSUtil.getWsProfiles());
			
			// Check if the request exists
			PfRequestsDTO request = queryServiceBO.checkRequest(requestId.value);

			if (request != null) {
				
				log.debug("Checking user can access request");
				authenticationWSHelper.chequeaAccesoUsuarioPeticion(authentication, request);		
				
				log.debug("Checking application has no children");
				checkApplicationHasNoChildren(request.getPfApplication());
				
				if (!isSendRequest(requestId.value)) {
					// Check that the request has no state.
					log.debug("Checking documents");
					List<AbstractBaseDTO> documentList = baseDAO.queryListOneParameter("request.requestHashDocuments", "hash", requestId.value);
					if (documentList != null && !documentList.isEmpty()) {
						log.debug("Documents found");
						log.debug("Checking sign lines");
						if (request.getPfSignsLines() != null && !request.getPfSignsLines().isEmpty()) {
							log.debug("Sign lines found");
							log.debug("Sending request to signers");
							tagBO.addStateNew(request);
							
							// Execute authorizations
							authorizationBO.applyAuthorizations(request);

							// Execute filters
							filterBO.applyFilterOneRequest(request);

							// Execute notice
							noticeBO.noticeNewRequest(request, applicationVO.getEmail(), applicationVO.getSMS());
							noticeBO.noticeNewRequestValidador(request, applicationVO.getEmail(), applicationVO.getSMS());
						} else {
							log.error("Sign lines not found");
							throw new PfirmaException("Sign lines not found");
						}
					} else {
						log.error("Documents for sign not found");
						throw new PfirmaException("Documents for sign not found");
					}
				} else {
					log.error("Request is already sent. Can't send again");
					throw new PfirmaException("Request is already sent. Can't send again");					
				}
			}

			log.info("sendRequest end ");

		} catch (PfirmaException pf) {
			log.error("Error al enviar petición: ", pf);
			throw pf;
		} catch (Throwable t) {
			log.error("Error inesperado al enviar petición: ", t);
			throw new PfirmaException("Unknown error", t);
			//throw new PfirmaException("Unknown error", e);
		}
	}

	/**
	 * Permite a&ntilde;adir un documento a una petici&oacute;n ya existente
	 * 
	 * @param requestId
	 * @param document
	 * @return
	 * @throws PfirmaException
	 */
	@Transactional(readOnly = false, rollbackFor=PfirmaException.class)
	public String insertDocument(Authentication authentication, String requestId, Document document) throws PfirmaException {
		String idDoc = null;		
		try {
			authenticationWSHelper.authenticateWebservice(authentication, WSUtil.getWsProfiles());
			PfRequestsDTO request = queryServiceBO.checkRequest(requestId);
			if (request != null) {
				authenticationWSHelper.chequeaAccesoUsuarioPeticion(authentication, request);
				
				if (isSendRequest(requestId)) {				
					throw new PfirmaException(MSJ_PETICION_ENVIADA);
				} else {
					idDoc = insertDocumentWS(request, document);
				}
			}
			return idDoc;

		} catch (PfirmaException pf) {
			log.error("Error insertando documentos: ", pf);
			throw pf;
		} catch (Throwable e) {
			log.error("Error inesperado insertando documentos: ", e);
			throw new PfirmaException("Unknown error", e);
		}
	}

	/**
	 * Update the request. It does not change the signers. For adding o deleting
	 * signers use insertSigner and removeSigner. *
	 * 
	 * @param request
	 * @return
	 * @throws PfirmaException
	 */
	@Transactional(readOnly = false, rollbackFor=PfirmaException.class)
	public String updateRequest(Authentication authentication, Request request) throws PfirmaException {
		try {
			log.info("updateRequest: " + request.getIdentifier() + " autenticación: " + authentication.getUserName());
			PfUsersDTO authUser = authenticationWSHelper.authenticateWebservice(authentication, WSUtil.getWsProfiles());
			
			if (!isSendRequest(request.getIdentifier())) {//Solo se pueden modificar las peticiones no enviadas
				log.debug("The request is not sent");
				PfRequestsDTO requestDTO = queryServiceBO.checkRequest(request.getIdentifier());
				if (requestDTO != null) {
					
					//Comprobamos que el usuario tenga acceso a la aplicaci&oacute;n actual de la petici&oacute;n (antes de actualizar)
					log.debug("Checking user can access saved request");
					authenticationWSHelper.chequeaAccesoUsuarioPeticion(authentication, requestDTO);	
					
					PfApplicationsDTO applicationDTO = checkApplication(request.getApplication());
					
					if (applicationDTO != null) {
						
						//Comprobamos que el usuario tenga acceso a la nueva aplicaci&oacute;n que ha asignado a la petici&oacute;n
						log.debug("Checking user can access the new application of the request");
						authenticationWSHelper.chequeaAccesoUsuarioAplicacionPeticion(authentication, applicationDTO);
						
						log.debug("Checking application has no children");
						checkApplicationHasNoChildren(applicationDTO);
						
						requestDTO.setPfApplication(applicationDTO);
						// Check if the sign type has changed
						boolean changeSignType = false;
						log.debug("Check sign type");
						if (requestDTO.getLcascadeSign() != (request.getSignType().equals(Constants.FIRMA_CASCADA))) {
							log.debug("Change sign type");
							changeSignType = true;
						}

						setSignType(requestDTO, request.getSignType());
						log.debug("Changing sign type");
						if (changeSignType) {
							changeSignLinesType(requestDTO);
						}

						// TODO Añadir sello de tiempo

						log.debug("Setting fstart");
						requestDTO.setFstart(null);
						if (request.getFstart() != null) {
							requestDTO.setFstart(request.getFstart().toGregorianCalendar().getTime());
						}

						// Setting fexpiration
						log.debug("Setting fexpiration");
						if (request.getFexpiration() != null) {
							requestDTO.setFexpiration(request.getFexpiration().toGregorianCalendar().getTime());
						}

						// Setting the subject
						log.debug("Setting the subject");
						if (request.getSubject() != null && !"".equals(request.getSubject())) {
							requestDTO.setDsubject(request.getSubject());
						}

						// Setting the reference
						log.debug("Setting the reference");
						if (request.getReference() != null && !"".equals(request.getReference())) {
							requestDTO.setDreference(request.getReference());
						}

						log.debug("Updating request");
						baseDAO.update(requestDTO);

						updateTextRequest(request.getText(), request.getIdentifier());

						// Remitter
						// The remitter list is deleted and the new remitters are added.
						// TODO: Cambiar para actualizar como notices y actions
						log.debug("Deleting remitters from request");
						Set<PfUsersRemitterDTO> remittersDTO = requestDTO.getPfUsersRemitters();
						baseDAO.deleteList(new ArrayList<AbstractBaseDTO>(remittersDTO));
						log.debug("Adding the new remitters to the request");
						addRemitterListToRequest(requestDTO, request, authUser);
						// Notices
						insertOrUpdateNotices(requestDTO, request.getNoticeList());

						// Actions
						insertOrUpdateActions(requestDTO, request.getActionList());
						
						// Emails to notify
						insertEmailToNotifyList(requestDTO, request.getEmailToNotifyList());

					}
				}

			} else {
				log.error("The request is already sent. It cannot be updated");
				throw new PfirmaException("The request is already sent. It cannot be updated");
			}

			log.info("updateRequest end ");

			return request.getIdentifier();

		} catch (PfirmaException pf) {
			log.error("Error actualizando petición: ", pf);
			throw pf;
		} catch (Throwable e) {
			log.error("Error inesperado actualizando petición: ", e);
			throw new PfirmaException("Unknown error", e);
		}
	}

	/**
	 * Permite insertar un conjunto de firmantes en una l&iacute;nea de firma
	 * @param authentication
	 * @param requestId
	 * @param signLine
	 * @param signerList
	 * @param signLineType
	 * @throws PfirmaException
	 */
	@Transactional(readOnly = false, rollbackFor=PfirmaException.class)
	public void insertSigners(Authentication authentication, Holder<String> requestId, int signLine, SignerList signerList, String signLineType) throws PfirmaException {
		try {
						
			log.warn("insertSigners: " + requestId.value + " autenticación: " + authentication.getUserName());
			authenticationWSHelper.authenticateWebservice(authentication, WSUtil.getWsProfiles());
			
			// Check if the request exists.
			log.debug("Checking request");
			PfRequestsDTO requestDTO = queryServiceBO.checkRequestSimple(requestId.value);
			
			//Comprobamos que el usuario tenga acceso a la aplicaci&oacute;n de la petici&oacute;n
			log.debug("Checking user can access saved request");
			authenticationWSHelper.chequeaAccesoUsuarioPeticion(authentication, requestDTO);				
			
			if (!isSendRequest(requestId.value)) {
			
				// Get the sign line
				List<PfSignLinesDTO> signLinesDTO = requestDTO.getPfSignsLinesList();
				PfSignLinesDTO signLineDTO = new PfSignLinesDTO();
	
				if (signLine < 0 || signLine > signLinesDTO.size()) {
					log.error("The specified signLine is wrong");
					throw new PfirmaException("The specified signLine is wrong");
				}
	
				if (signLinesDTO.size() > signLine) {//Estamos modificando una linea de firma que ya existe
					log.debug("An existing sign line");
					// A line that belongs to the request
					signLineDTO = signLinesDTO.get(signLine);
					
					if (isLineSigned(signLineDTO)) {
						throw new PfirmaException("The specified sign line is already signed");
					}	
				} else if (signLinesDTO.size() == signLine) { //A&ntilde;adiendo una nueva linea de firma
					log.debug("New sign line to add");
					// The line does not belong to the request.
					// A new sign line is created
					signLineDTO = new PfSignLinesDTO();
					signLineDTO.setPfRequest(requestDTO);
	
					PfSignLinesDTO signLinePrev = findSignLinePrevious(requestDTO.getChash());
					if (requestDTO.getLcascadeSign() && signLinePrev != null) {
						log.debug("Setting the previous sign line");
						signLineDTO.setPfSignLine(signLinePrev);
					}
	
				}
			
				this.actualizarTipoFirmaEnLineaDeFirma(signLineDTO, signLineType);
				// Insert the new sign line
				baseDAO.insertOrUpdate(signLineDTO);
			
				// Iterate the signers
				log.debug("Iterate the signers");
				this.insertSignersInSignLine(signLineDTO, signerList);
			} else {
				log.error("The request is already sent. It cannot be updated");
				throw new PfirmaException("The request is already sent. It cannot be updated");				
			}

			log.info("insertSigners end");
		} catch (PfirmaException pf) {
			log.error("Error insertando firmantes: ", pf);
			throw pf;
		} catch (Throwable e) {
			log.error("Error inesperado insertando firmantes: ", e);
			throw new PfirmaException("Unknown error", e);
		}
	}
	
	/**
	 * M&eacute;todo que inserta usuario en una linea de firma
	 * 
	 * Comprueba que el usuario se v&aacute;lido, y que el mismo usuario o est&eacute; varias veces en una linea de firma.
	 * 
	 * @param signLineDTO
	 * @param signerList
	 * @throws PfirmaException
	 */
	@Transactional(readOnly = false, rollbackFor=PfirmaException.class)
	private void insertSignersInSignLine(PfSignLinesDTO signLineDTO, SignerList signerList) throws PfirmaException {
		
		List<PfUsersDTO> repeatUserList = new ArrayList<PfUsersDTO>();
		for (PfSignersDTO signer: signLineDTO.getPfSigners()) {
			repeatUserList.add(signer.getPfUser());
		}
		
		// Iterate the signers for the current sign line
		List<Signer> signers = signerList.getSigner();
		for (Signer signer: signers) {
			// Search the user to add it to the signers
			String userId = signer.getUserJob().getIdentifier();
			log.debug("Checking user");
			
			PfUsersDTO userDTO = checkUser(userId);
			if (userDTO != null) {
				if (!repeatUserList.contains(userDTO)) {
					PfSignersDTO signerDTO = new PfSignersDTO();
					signerDTO.setPfSignLine(signLineDTO);
					signerDTO.setPfUser(userDTO);
					log.debug("Inserting the signer");
					baseDAO.insertOrUpdate(signerDTO);
					repeatUserList.add(userDTO);
				} else {
					log.error("The user " + userId + " exist in the same sign line");
					throw new PfirmaException("The user " + userId + " exist in the same sign line");
				}
			} else {
				log.error("The user " + userId + " does not exist");
				throw new PfirmaException("The user " + userId + " does not exist");
			}
		}		
	}	

	/**
	 * Verifica si una l&iacute;nea de firma est&aacute; firmada
	 * @param signLineDTO la linea de firma
	 * @return true si la l&iacute;nea de firma est&aacute; firmada, false en caso contrario
	 */
	private boolean isLineSigned(PfSignLinesDTO signLineDTO) {
		PfRequestsDTO requestDTO = signLineDTO.getPfRequest();
		// Iterate the signers of the request
		log.debug("Iterating the signers");
		//Comprobamos si la linea ya est&aacute; firmada
		for (PfSignersDTO signer: signLineDTO.getPfSigners()) {
			PfUsersDTO user = signer.getPfUser();
			log.debug("Checking state of signer " + user);
			//Recupera la etiqueta de petici&oacute;n de tipo 'ESTADO' para la petici&oacute;n y el usuario pasados como par&aacute;metro
			PfRequestTagsDTO pfRequestTagsDTO = tagBO.queryStateUserSignLine(requestDTO, user, signLineDTO);
			
			if (pfRequestTagsDTO != null) {
				PfTagsDTO tagDTO = pfRequestTagsDTO.getPfTag();
				if (tagDTO != null && Constants.C_TAG_SIGNED.equals(tagDTO.getCtag())) {
					return true;
				}
			}
		}
		return false;		
	}

	/**
	 * Permite borrar una lista de firmantes de una o varias l&iacute;neas de firma
	 * @param authentication el bean de autenticaci&oacute;n
	 * @param requestId el c&oacute;digo de la petici&oacute;n
	 * @param signLineNumber n&uacute;mero de 
	 * @param signerList
	 * @throws PfirmaException
	 */
	// TODO: Revisar los cambios de estado y la eliminaci&oacute;n de firmantes
	// relacionados con el actual por el tema de haber aplicado un filtro
	@Transactional(readOnly = false, rollbackFor=PfirmaException.class)
	public void deleteSigners(Authentication authentication, Holder<String> requestId, Integer signLineNumber, SignerList signerList) throws PfirmaException {
		try {
			
			log.warn("borrando firmantes: " + requestId.value + " authentication: " + authentication.getUserName() );
			authenticationWSHelper.authenticateWebservice(authentication, WSUtil.getWsProfiles());
			
			// Check if the request exists
			log.debug("Check the request");
			PfRequestsDTO requestDTO = queryServiceBO.checkRequest(requestId.value);

			//Comprobamos que el usuario tenga acceso a la aplicaci&oacute;n de la petici&oacute;n
			log.debug("Checking user can access saved request");
			authenticationWSHelper.chequeaAccesoUsuarioPeticion(authentication, requestDTO);
			
			if (!isSendRequest(requestId.value)) {
			
				PfSignLinesDTO signLineToDelete = null;
				if (signLineNumber != null) {
					List<PfSignLinesDTO> signLinesDTO = requestDTO.getPfSignsLinesList();
					if (signLineNumber >= signLinesDTO.size()) {
						throw new PfirmaException("Sign line " + signLineNumber + " does not exist in request");
					} else if (signLineNumber < 0) {
						throw new PfirmaException("Sign line must be 0 or higher");
					} else {
						signLineToDelete = signLinesDTO.get(signLineNumber);
					}
				}
				
				// Iterate the signers to be deleted
				log.debug("Iterate the signers");
				for (Signer signerL: signerList.getSigner()) {
					// Check if the signer exists
					String userId = signerL.getUserJob().getIdentifier();
					log.debug("Check signer");
					
					List<PfSignersDTO> signers = checkSigner(userId, requestId.value, signLineToDelete);
					if (signers.isEmpty()) {
						if (signLineToDelete != null) {
							log.error("The signer " + userId + " does not exist in signLine " + signLineNumber);
							throw new PfirmaException("The signer " + userId + " does not exist in signLine " + signLineNumber);
						} else {
							log.error("The signer " + userId + " does not exist");
							throw new PfirmaException("The signer " + userId + " does not exist");
						}
					}		
									
					for (PfSignersDTO signer: signers) {
						PfSignLinesDTO signLine = signer.getPfSignLine();
						// The signer can be deleted if it has no sign or state
						log.debug("Check state");
						PfRequestTagsDTO reqTag = tagBO.queryStateUserSignLine(requestDTO, signer.getPfUser(), signLine);
						if (reqTag != null && reqTag.getPfTag().getCtag().equals(Constants.C_TAG_SIGNED)) {
							log.error("The signer "	+ userId + "can not be deleted because has signed the petition");
							throw new PfirmaException("The signer " + userId + "can not be deleted because has signed the petition");
						} else {
							// Check if user exists
							PfUsersDTO user = signer.getPfUser();
							log.debug("Deleting signer");
							baseDAO.delete(signer);
							PfSignLinesDTO signLineNext = null;
							if (signLine.getPfSigners().size() == 1) {
								if (signLine.getPfSignLines() != null && !signLine.getPfSignLines().isEmpty()) {
									signLineNext = signLine.getPfSignLines().iterator().next();
									signLineNext.setPfSignLine(signLine.getPfSignLine());
									log.debug("Updating the sign line");
									baseDAO.update(signLineNext);
								}
								log.debug("Deleting sign line");
								baseDAO.delete(signLine);
							}
	
							if (reqTag != null) {
	
								// Change the state of the users of the next sign
								// line
								log.debug("Changing the state of the next line");
								// TODO: Probar bien!!!
								String state = tagBO.nextStateUser(reqTag, user);
								tagBO.changeRequestTagUser(requestDTO, reqTag, signLineNext, 
										applicationVO.getStateTags().get(state), user);
							}
	
							// Change udpate date
							requestDTO.setFmodified(Calendar.getInstance().getTime());
							baseDAO.update(requestDTO);
						}
					}
				}
			} else {
				log.error("Request is already sent. Can't update");
				throw new PfirmaException("Request is already sent. Can't update");		
			}
			log.info("deleteSigners end");

		} catch (PfirmaException pf) {
			log.error("Error al borrar firmantes", pf);
			throw pf;
		} catch (Throwable e) {
			log.error("Error inesperado al borrar firmantes", e);
			throw new PfirmaException("Unknown error", e);
		}
	}

	/**
	 * Permite crear en el servidor de portafirmas una nueva petici&oacute;n. 
	 * Dicha petici&oacute;n no ser&aacute; enviada hasta que se invoque la funci&oacute;n sendRequest
	 * @param request
	 * @return
	 * @throws PfirmaException
	 */
	@Transactional(readOnly = false, rollbackFor=PfirmaException.class)
	public String createRequest(Authentication authentication, Request request) throws PfirmaException {
		try {
					
			boolean continua = false;
			List<AbstractBaseDTO> list = baseDAO.queryListOneParameter("request.requestDReferecia", "dreference", request.getReference());
			
			if (list != null && list.size() == 1) {
				PfRequestsDTO aux1 = null;
				Date date1 =null;
				
				aux1 =  (PfRequestsDTO) list.get(0);

				//COMPROBAR SI EXISTE UNO CON EL MISMO IDENTIFICADOR Y ESTÁ PENDIENTE DE FIRMA
				if(!this.isSign(authentication,aux1.getChash())){
					throw new PfirmaException("Ya existe un identificador para esa petición, y está pendiente de firma.");
				}
				//Si está firmado le deja crear otra
				else {
					continua = true;
				}
				
			}
			else if (list != null && list.size() > 1) {
				throw new PfirmaException("Ya existe un identificador para esa petición, además hay más de una petición con el mismo identificador.");	
			}
			
			if (list == null || list.size()==0 || continua) {
				log.warn("createRequest: " + request.getIdentifier() + " autenticación: " + authentication.getUserName());
				PfUsersDTO authUser = authenticationWSHelper.authenticateWebservice(authentication, WSUtil.getWsProfiles());			
				
				// Check if the request exists
				log.debug("Check request");
				PfRequestsDTO requestDTO = new PfRequestsDTO();				
				
				// Get the application
				log.debug("Getting the application");
				PfApplicationsDTO applicationDTO = checkApplication(request.getApplication());
				if (applicationDTO != null) {
					
					//Comprobamos que el usuario tenga acceso a la aplicaci&oacute;n que quiere asignar a la petici&oacute;n
					log.debug("Checking user can access saved request");
					authenticationWSHelper.chequeaAccesoUsuarioAplicacionPeticion(authentication, applicationDTO);
					
					log.debug("Checking application has no children");
					checkApplicationHasNoChildren(applicationDTO);
					
					log.debug("Setting Application");
					requestDTO.setPfApplication(applicationDTO);

					//Petición no invitada (default)
					requestDTO.setLinvited(false);
					//Petición no aceptada (default)
					requestDTO.setLaccepted(false);
					// Set request values
					// New hash
					log.debug("Setting hash");
					requestDTO.setChash(Util.getInstance().createHash());

					// Entry date
					log.debug("Setting entry date");
					requestDTO.setFentry(new Date());

					// Start date
					log.debug("Setting fstart");
					requestDTO.setFstart(null);
					if (request.getFstart() != null) {
						requestDTO.setFstart(request.getFstart().toGregorianCalendar().getTime());
					}

					// Expiration date
					log.debug("Setting fexpiration");
					if (request.getFexpiration() != null) {
						requestDTO.setFexpiration(request.getFexpiration().toGregorianCalendar().getTime());
					}

					// Sign type
					log.debug("Setting sign type");
					setSignType(requestDTO, request.getSignType());

					// Timestamp
					log.debug("Setting addTimestamp");
					if (request.getTimestampInfo() == null) {
						requestDTO.setLtimestamp(Constants.C_NULL);
					} else if (request.getTimestampInfo().isAddTimestamp()) {
						requestDTO.setLtimestamp(Constants.C_YES);
					} else {
						requestDTO.setLtimestamp(Constants.C_NOT);
					}

					// Setting the subject
					log.debug("Setting the subject");
					if (request.getSubject() != null && !"".equals(request.getSubject())) {
						requestDTO.setDsubject(request.getSubject());
					} else {
						requestDTO.setDsubject("DESCONOCIDO");
					}

					// Setting the reference
					log.debug("Setting the reference");
					if (request.getReference() != null && !"".equals(request.getReference())) {
						requestDTO.setDreference(request.getReference());
					} else {
						requestDTO.setDreference("DESCONOCIDO");
					}

					// Setting importance level
					insertImportanceLevel(requestDTO, request.getImportanceLevel());

					// Añadimos que se notifiquen a todos, remitente y destinatarios.
					// Como no está en la interfaz de servicios web se establece a false.
					requestDTO.setLOnlyNotifyActionsToRemitter(false);					
					
					// Insert the request
					log.debug("Inserting request");
					baseDAO.insertOrUpdate(requestDTO);

					// Remitter
					log.debug("Checking remitter user");
					addRemitterListToRequest(requestDTO, request, authUser);

					// Request text
					log.debug("Updating text request");
					updateTextRequest(request.getText(), requestDTO.getChash());

					// Sign lines
					log.debug("Inserting sign lines");
					List<SignLine> signLines = request.getSignLineList().getSignLine();

					// Iterate sign lines
					log.debug("Setting sign lines");
					for (SignLine signLine: signLines) {	
						if (signLine.getSignerList() == null || signLine.getSignerList().getSigner() == null || signLine.getSignerList().getSigner().size() == 0) {
							throw new PfirmaException ("Sign line must contain signers, one of the Sign Lines is empty");
						}
						log.debug("Creating a new sign line");
						PfSignLinesDTO signLineDTO = new PfSignLinesDTO();
						
						this.actualizarTipoFirmaEnLineaDeFirma(signLineDTO, signLine.getType());
						signLineDTO.setPfRequest(requestDTO);

						PfSignLinesDTO signLinePrev = findSignLinePrevious(requestDTO.getChash());
						if (requestDTO.getLcascadeSign() && signLinePrev != null) {
							log.debug("Setting the previous sign line");
							signLineDTO.setPfSignLine(signLinePrev);
						}
						
						if (request.getSignedMark() != null && 
								request.getSignedMark().equalsIgnoreCase("true"))
							requestDTO.setlSignMarked(true);

						baseDAO.insertOrUpdate(signLineDTO);
						
						this.insertSignersInSignLine(signLineDTO, signLine.getSignerList());
					}
					
					// Notices
					insertOrUpdateNotices(requestDTO, request.getNoticeList());

					// Actions
					insertOrUpdateActions(requestDTO, request.getActionList());
					
					// Emails to notify
					insertEmailToNotifyList(requestDTO, request.getEmailToNotifyList());

					// Documents				
					if (request.getDocumentList() != null) {
						log.debug("Inserting documents");
						List<Document> documents = request.getDocumentList().getDocument();
						for (Document doc: documents) {
							insertDocumentWS(requestDTO, doc);
						}
					}
				}
				log.info("createRequest end ");
				return requestDTO.getChash();
			}
			else {
				throw new PfirmaException("Hay un problema al crear la petición, es posible que la petición que se intenta realizar no tenga referencia.");
			}

		} catch (PfirmaException pf) {	
			log.error ("Error al crear la petición-ModifyService.createRequest: ", pf);
			throw pf;
		} catch (Throwable e) {
			log.error ("Error inesperado al crear la peticion: ", e);
			throw new PfirmaException("Unknown error", e);
		}
	}
	
	/**
	 * M&eacute;todo que a&ntilde;ade la lista de remitentes del objeto request (webservice) al objeto requestDTO (base de datos)
	 * El m&eacute;todo s&oacute;lo acepta como remitente v&aacute;lido al usuario que llega en el par&aacute;metro authUser.
	 * 
	 * Si la lista viene vac&iacute;a, se a&ntilde;ade el usuario authUser como remitente
	 * Si la lista tiene m&aacute;s de un elemento, o si el elemento es diferente de authUser, entonces lanza una excepci&oacute;n.
	 * 
	 * @param requestDTO
	 * @param request
	 * @param authUser
	 * @throws PfirmaException
	 */
	private void addRemitterListToRequest(PfRequestsDTO requestDTO, Request request, PfUsersDTO authUser) throws PfirmaException {
		// Iterate the remitters and add them to the request
		List<User> userList;
		if (request.getRemitterList() != null) {
			userList = request.getRemitterList().getUser();
		} else {
			log.error("Remitter: Remitter list must non be empty");
			throw new PfirmaException("Remitter list must non be empty");
		}
		//S&oacute;lo admitimos un remitente, que debe ser el usuario autenticado.
		if (userList == null || userList.size() == 0) {
			insertRemitterWS(requestDTO, authUser);
		} else if (userList.size() == 1) {
			for (User user: userList) {
				log.debug("Checking user");
				if(user.getIdentifier()!=null){
					PfUsersDTO remitter = checkUser(user.getIdentifier());
					if (remitter != null) {
						insertRemitterWS(requestDTO, remitter);

					} else {
						log.error("User: " + user.getIdentifier() + " does not exist.");
						
						EnhancedUser enhancedUser = new EnhancedUser();
						User usuario = new User();
						usuario.setIdentifier(user.getIdentifier());
						usuario.setName(user.getName());
						usuario.setSurname1(user.getSurname1());
						enhancedUser.setUser(usuario);
						

						Seat sede = new Seat();
						sede.setCode(authUser.getPfProvince().getCcodigoprovincia());
						sede.setDescription(authUser.getPfProvince().getCnombre());
						EnhancedUserJobInfo enhancedUserJobInfo = new EnhancedUserJobInfo();
						enhancedUserJobInfo.setSeat(sede);
						enhancedUser.setEnhancedUserJobInfo(enhancedUserJobInfo);
						adminBO.insertUser(enhancedUser, authUser);
						
						remitter = checkUser(user.getIdentifier());
						insertRemitterWS(requestDTO, remitter);
					}
					
				}														
			}
		} else {
			log.error("Remitter: Remmiter in webservice created request can only be the authenticated user");
			throw new PfirmaException("Several remitters in request. Use authenticated only user or empty list");
		}
	}

	/**
	 * Permite quitar un documento de una petici&oacute;n existente
	 * @param documentId
	 * @throws PfirmaException
	 */
	@Transactional(readOnly = false, rollbackFor=PfirmaException.class)
	public void deleteDocument(Authentication authentication, Holder<String> documentId) throws PfirmaException {

		try {
			authenticationWSHelper.authenticateWebservice(authentication, WSUtil.getWsProfiles());				
			
			PfDocumentsDTO document = requestBO.checkDocument(documentId.value);
			PfRequestsDTO req = document.getPfRequest();
				
			//Comprobamos que el usuario tenga acceso a la peticion
			authenticationWSHelper.chequeaAccesoUsuarioPeticion(authentication, req);
			
			if (isSendRequest(req.getChash())) {
				throw new PfirmaException(MSJ_PETICION_ENVIADA);	
			} else {
				baseDAO.delete(document);
				req.setFmodified(Calendar.getInstance().getTime());
				baseDAO.update(req);
			}

		} catch (PfirmaException pf) {
			log.error ("Error al borrar el documento: ", pf);
			throw pf;
		} catch (Throwable e) {
			log.error ("Error inesperado al borrar el documento: ", e);
			throw new PfirmaException("Error inesperado.", e);
		}
	}
	
	/**
	 * [Ticket1269#Teresa] Anular circuito de firmas
	 * Permite borrar una petición ya enviada.
	 * @param requestId
	 * @throws PfirmaException
	 * **/
	public void deleteRequestSend(Authentication authentication, Holder<String> requestId) throws PfirmaException {
		try {
			
			log.warn("deleteRequest: " + requestId.value + " autenticación: " + authentication.getUserName());
			authenticationWSHelper.authenticateWebservice(authentication, WSUtil.getWsProfiles());
			
			// Check if the request exists
			log.debug("Checking the request");
			PfRequestsDTO request = queryServiceBO.checkRequest(requestId.value);

			if (request != null) {
				
				//Comprobamos que el usuario tenga acceso a la peticion
				log.debug("Checking user can access saved request");
				authenticationWSHelper.chequeaAccesoUsuarioPeticion(authentication, request);				
				
				// Check if the request is sent
				log.debug("Checking if the request is sent");
				//Quitamos el control de si esta firmado para que permita eliminar todo
				//if (!isSign(authentication, requestId.value)) {
					log.debug("Deleting request");							
					
					borraSet(request.getPfRequestsTags());
					borraSet(request.getPfRequestsValues());
					borraSet(request.getPfHistoricRequests());
					for (PfCommentsDTO comment: request.getPfComments()) {
						borraSet(comment.getPfUsersComments());
					}
					borraSet(request.getPfComments());
					borraSet(request.getPfUsersRemitters());
					borraSet(request.getPfNoticesRequests());
					borraSet(request.getPfActions());
					borraSet(request.getPfEmailsRequest());				
					
					
					for (PfDocumentsDTO documento: request.getPfDocumentsList()){
						PfFilesDTO fileSinFirma = documento.getPfFile();
						String storageType = fileSinFirma.getCtype();
										
						
						
						if (documento.getPfSigns() != null) {
							Set<PfSignsDTO> signList = documento.getPfSigns();
							if(!signList.isEmpty()){
								for (PfSignsDTO sign: documento.getPfSigns()){
									CustodyServiceOutput custodyService = custodyServiceFactory.createCustodyServiceOutput(sign.getCtype());
									CustodyServiceOutputDocument custodyDocument = new CustodyServiceOutputDocument();
									custodyDocument.setUri(sign.getCuri());
									custodyService.deleteFile(custodyDocument);
									borraSet(sign.getPfBlockSigns());
								}
								if(documento.getPfSigns().size()!=0) borraSet(documento.getPfSigns());
							}
							
						}
						boolean tieneVariosIdentificadoresAlMismoDoc = false;
						//Quiere decir que no se referencia a mas de un documento
						if(fileSinFirma.getPfDocuments().size()!=1){
							ArrayList<PfDocumentsDTO> pfDocuments =  new ArrayList<PfDocumentsDTO>();
							Set<PfDocumentsDTO> fileRequest =  fileSinFirma.getPfDocuments();
							for (PfDocumentsDTO file: fileSinFirma.getPfDocuments()){
								if(file.getChash()!=documento.getChash()){
									pfDocuments.add(file);
									tieneVariosIdentificadoresAlMismoDoc = true;
								}
							}

							fileRequest.removeAll(pfDocuments);
						}
						borraSet(fileSinFirma.getPfDocuments());
						if(!tieneVariosIdentificadoresAlMismoDoc){
							baseDAO.delete(fileSinFirma);
							
							//Cosas por hacer, estudiar porque no se elimina de la tabla pf_archivo
							//Quitamos esto porque no esta eliminando la línea en el pf_archivo
							//Eliminamos el documento físico
							CustodyServiceOutput custodyService = custodyServiceFactory.createCustodyServiceOutput(storageType);
							CustodyServiceOutputDocument custodyDocument = new CustodyServiceOutputDocument();
							custodyDocument.setUri(fileSinFirma.getCuri());
							custodyService.deleteFile(custodyDocument);
						}										
					}
					if(!request.getPfSignsLines().isEmpty()){
						ArrayList<PfSignLinesDTO> listSignLine = ordenarLineasFirma(request.getPfSignsLines());
						for (int i = listSignLine.size()-1; i>=0; i--) {
							PfSignLinesDTO signLine = listSignLine.get(i);
							Set<PfSignersDTO> signers = signLine.getPfSigners();
							for (PfSignersDTO signer : signers) {
								baseDAO.delete(signer);	
							}
							
							baseDAO.delete(signLine);							
						}
						//borraSet(request.getPfSignsLines());
					}
					
					//if(request.getPfDocuments()!=null)borraSet(request.getPfDocuments());	
					borraSet(request.getPfUsersRemitters());
					
					baseDAO.delete(request);
					//Forzamos un flush. As&iacute; el posible error da dentro del m&eacute;todo
					baseDAO.flush();
					
				/*} else {
					log.error("El documento ya ha sido firmado no se puede anular el circuito de firma");
					throw new PfirmaException("El documento ya ha sido firmado no se puede anular el circuito de firma");	
				}*/
			}

			log.info("deleteRequest end ");

		} catch (PfirmaException pf) {
			log.error ("Error al borrar la petición: "+pf.getMessage(), pf);
			throw pf;
		} catch (Throwable e) {
			log.error ("Error inesperado al borrar la petición: "+e.getMessage(), e);
			throw new PfirmaException("Unknown error", e);
		}
	}


	private ArrayList<PfSignLinesDTO> ordenarLineasFirma(Set<PfSignLinesDTO> signLines) {
		ArrayList<PfSignLinesDTO> lineasResultado = new ArrayList<PfSignLinesDTO>();
		int[] vectorLinea = obtenerPrimaryKeyLinea(signLines);
		Arrays.sort(vectorLinea);
		for (int i = 0; i < vectorLinea.length; i++) {
			lineasResultado.add(i, obtenerPfSignLinesDTO(signLines, vectorLinea[i]));
		}
		return lineasResultado;
	}

	private PfSignLinesDTO obtenerPfSignLinesDTO(Set<PfSignLinesDTO> signLines,	int i) {
		PfSignLinesDTO resultado = new PfSignLinesDTO();
		boolean encontrado = false;
		for (PfSignLinesDTO signLine : signLines) {
			if(!encontrado && signLine.getPrimaryKey().intValue() == i){
				resultado = signLine;
				encontrado = true;
			}
		}
		return resultado;
	}

	private int[] obtenerPrimaryKeyLinea(Set<PfSignLinesDTO> signLines) {
		int [] resultado = new int [signLines.size()];
		int i = 0;
		for (PfSignLinesDTO signLine : signLines) {
			resultado[i] = signLine.getPrimaryKey().intValue();
			i++;
		}
		return resultado;
	}

	private boolean isSign(Authentication authentication, String requestId) throws PfirmaException {
		boolean estaFirmado = false;
		try {
			Request request = queryServiceBO.queryRequest(authentication, requestId);
			for (SignLine signLine:request.getSignLineList().getSignLine()){
				Signer signer = signLine.getSignerList().getSigner().get(0);
				if (signer !=null && "FIRMADO".equals(signer.getState().getIdentifier())){
					estaFirmado = true;
					break;
				}
			}
				
		} catch (PfirmaException e) {
			log.error ("Error al borrar la petición: "+e.getMessage(), e);
			throw e;
		}
		return estaFirmado;
	}

	/**
	 * Permite borrar una petici&oacute;n previamente creada.
	 * @param requestId
	 * @throws PfirmaException
	 */
	@Transactional(readOnly = false, rollbackFor=PfirmaException.class)
	public void deleteRequest(Authentication authentication, Holder<String> requestId) throws PfirmaException {
		try {
			
			log.warn("deleteRequest: " + requestId.value + " autenticación: " + authentication.getUserName());
			authenticationWSHelper.authenticateWebservice(authentication, WSUtil.getWsProfiles());
			
			// Check if the request exists
			log.debug("Checking the request");
			PfRequestsDTO request = queryServiceBO.checkRequest(requestId.value);

			if (request != null) {
				
				//Comprobamos que el usuario tenga acceso a la peticion
				log.debug("Checking user can access saved request");
				authenticationWSHelper.chequeaAccesoUsuarioPeticion(authentication, request);				
				
				// Check if the request is sent
				log.debug("Checking if the request is sent");
				if (!isSendRequest(requestId.value)) {
					log.debug("Deleting request");
					
					borraSet(request.getPfRequestsTags());
					borraSet(request.getPfRequestsValues());
					borraSet(request.getPfHistoricRequests());
					borraSet(request.getPfDocuments());					
					borraSet(request.getPfComments());
					borraSet(request.getPfUsersRemitters());
					borraSet(request.getPfNoticesRequests());
					borraSet(request.getPfActions());
					
					for (PfSignLinesDTO signLine: request.getPfSignsLines()) {
						borraSet(signLine.getPfSigners());
					}
					borraSet(request.getPfSignsLines());
					
					baseDAO.delete(request);
					//Forzamos un flush. As&iacute; el posible error da dentro del m&eacute;todo
					baseDAO.flush();
				} else {
					log.error("Request is already sent. Can't delete");
					throw new PfirmaException("Request is already sent. Can't delete");	
				}
			}

			log.info("deleteRequest end ");

		} catch (PfirmaException pf) {
			log.error ("Error al borrar la petición: ", pf);
			throw pf;
		} catch (Throwable e) {
			log.error ("Error inesperado al borrar la petición: ", e);
			throw new PfirmaException("Unknown error", e);
		}
	}

	/**
	 * M&eacute;todo que elimina de la BD un cojunto de elementos.
	 * @param setElementos
	 */
	//@Transactional(readOnly = false)
	private void borraSet(Set<? extends AbstractBaseDTO> setElementos) {
		for (AbstractBaseDTO elemento: setElementos) {
			baseDAO.delete(elemento);
		}
		setElementos.clear();
	}

	// AUXILIAR METHODS

	/**
	 * Permite a&ntilde;adir un documento a una petici&oacute;n ya existente
	 * @throws PfirmaException
	 */
	//@Transactional(readOnly = false)
	private String insertDocumentWS(PfRequestsDTO requestDTO, Document document) throws PfirmaException, CustodyServiceException {

		String idDoc = "";
		if (document.getName() != null && !"".equals(document.getName())
				&& document.getContent() != null) {
			// Check the document type.
			log.debug("Checking the document type");
			PfDocumentTypesDTO documentType = checkDocumentType(document.getDocumentType().getIdentifier());
			if (documentType != null) {
				log.debug("Creating the document");
				PfDocumentsDTO documentDTO = new PfDocumentsDTO();
				idDoc = Util.getInstance().createHash();
				documentDTO.setChash(idDoc);
				documentDTO.setDmime(document.getMime());
				documentDTO.setDname(document.getName());
				documentDTO.setLsign(document.isSign());
				documentDTO.setPfDocumentType(documentType);
				documentDTO.setPfRequest(requestDTO);

				// Inserta el ámbito INTERNO para los documentos añadidos a traves de WS
				PfDocumentScopesDTO scope =	(PfDocumentScopesDTO) 
				baseDAO.queryElementOneParameter("request.queryInternalScope", null, null);
				documentDTO.setPfDocumentScope(scope);

				try {
					byte[] inputBytes = IOUtils.toByteArray(document.getContent().getInputStream());					
					//String isSign = SignDataUtil.getInstance().checkIsSign(document.getContent().getInputStream());
					String isSign = signDataUtil.checkIsSign(inputBytes);
					if (isSign != null) {
						documentDTO.setLissign(true);
					} else {
						documentDTO.setLissign(false);
					}
					log.debug("Uploading file");
					
					/*InputStream input = document.getContent().getInputStream();
					byte[] inputBytes = IOUtils.toByteArray(input);*/
					String filePath = Constants.PATH_TEMP + "tmp_" + Util.getInstance().createHash(inputBytes) + "_" + System.currentTimeMillis();
					log.debug ("Se va a crear el fichero en la ruta: " + filePath);
					File file = new File(filePath);
					FileOutputStream fileOut = new FileOutputStream(file);
					IOUtils.copy(new ByteArrayInputStream(inputBytes), fileOut);					
					long size = fileOut.getChannel().size();
					log.debug ("Creado el fichero en la ruta: " + filePath + ", tamaño: " + size);
					if (size > 0) {
						fileOut.close();
						InputStream inputCheck = new FileInputStream(file);
						InputStream inputStore = new FileInputStream(file);
						PfFilesDTO fileDTO = requestBO.insertFile(documentDTO, inputStore, inputCheck, size);
						documentDTO.setPfFile(fileDTO);
						log.debug("Inserting document");
						baseDAO.insertOrUpdate(documentDTO);
						file.delete();

						// Change udpate date
						requestDTO.setFmodified(Calendar.getInstance().getTime());
						baseDAO.update(requestDTO);
					} else {
						throw new PfirmaException("Error uploading file. Incorrect size.");
					}

				} catch (IOException e) {
					throw new PfirmaException("Error uploading file");
				} catch (Exception e) {
					throw new PfirmaException("Error uploading file");
				}

			}

		} else {
			log.error ("El nombre del documento o el contenido está vacío.");
			throw new PfirmaException ("El nombre del documento o el contenido está vacío.");
			
		}

		return idDoc;
	}

	/**
	 * Inserta los correos adicionales a notificar.
	 */
	private void insertEmailToNotifyList (PfRequestsDTO requestDTO, StringList emailToNotifyList) {
		
		if (emailToNotifyList != null) {
			List<String> emails = emailToNotifyList.getStr();
			for (String email : emails) {
				PfEmailsRequestDTO pfEmailsRequestDTO = new PfEmailsRequestDTO ();
				pfEmailsRequestDTO.setPfRequest(requestDTO);
				pfEmailsRequestDTO.setCemail(email);
				baseDAO.insertOrUpdate(pfEmailsRequestDTO);
				//requestDTO.setFmodified(Calendar.getInstance().getTime());
				//baseDAO.update(requestDTO);
			}
		}
		
	}
	/**
	 * Check if a user exists in BD.
	 * 
	 * @param cdni
	 * @return
	 * @throws PfirmaException
	 */
	private PfUsersDTO checkUser(String cdni) throws PfirmaException {
		PfUsersDTO user = null;
		log.debug("Checking user");
		if (cdni != null && !"".equals(cdni)) {
			log.debug("Checking user");
			user = (PfUsersDTO) baseDAO.queryElementOneParameter("request.usersAndJobDni", "dni", cdni.toUpperCase());
			if (user != null) {
				log.debug("User found");
			} else {
				// The user does not exist
				log.error("User " + cdni + " not found");
			}
		} else {
			// User not valid
			log.error("User not valid");
		}
		return user;
	}

	/**
	 * Insert the remitter.
	 * 
	 * @param req
	 * @param user
	 */
	//@Transactional(readOnly = false)
	private void insertRemitterWS(PfRequestsDTO req, PfUsersDTO user) {
		PfUsersRemitterDTO remitterDTO = null;

		if (user != null) {
			log.debug("Creating a remitter");
			remitterDTO = new PfUsersRemitterDTO();
			remitterDTO.setPfUser(user);
			remitterDTO.setPfRequest(req);
			remitterDTO.setLnotifyEmail(false);
			remitterDTO.setLnotifyMobile(false);
			log.debug("Inserting remitter");
			baseDAO.insertOrUpdate(remitterDTO);
		}
	}

	/**
	 * Find the previous sign line.
	 * 
	 * @param chash
	 * @return
	 */
	private PfSignLinesDTO findSignLinePrevious(String chash) {
		PfSignLinesDTO result = null;
		List<AbstractBaseDTO> list = baseDAO.queryListOneParameter(
				"request.requestSignLineDesc", "chash", chash);
		if (list != null && !list.isEmpty()) {
			result = (PfSignLinesDTO) list.get(0);
		}

		return result;
	}

	/**
	 * Check if the application exists.
	 * 
	 * @param capplication
	 * @return
	 */
	private PfApplicationsDTO checkApplication(String capplication) {
		PfApplicationsDTO app = null;
		log.debug("Checking application");
		if (capplication != null && !"".equals(capplication)) {
			log.debug("Searching application");
			app = (PfApplicationsDTO) baseDAO.queryElementOneParameter(
					"request.applicationId", "capp", capplication);
			if (app != null) {
				log.debug("Application found");
			} else {
				// application not found
				log.debug("Application: " + capplication + " not found");
			}
		} else {
			// application not valid
			log.debug("Application not valid");
		}
		if (app == null) {
			log.debug("Assigned to default application");
			app = (PfApplicationsDTO) baseDAO.queryElementOneParameter(
					"request.applicationPfirma", null, null);
		}
		return app;
	}

	/**
	 * Set the sign type.
	 * 
	 * @param requestDTO
	 * @param signType
	 */
	private void setSignType(PfRequestsDTO requestDTO, String signType) {
		// Sign type
		log.info("Setting the sign type");
		requestDTO.setLcascadeSign(false);
		requestDTO.setLfirstSignerSign(false);
		if (signType.equals(Constants.FIRMA_PRIMER_FIRMANTE)) {
			requestDTO.setLfirstSignerSign(true);
		} else if (signType.equals(Constants.FIRMA_CASCADA)) {
			requestDTO.setLcascadeSign(true);
		}
	}

	/**
	 * Update the texto of the request.
	 * 
	 * @param textRequest
	 * @param chash
	 */
	//@Transactional(readOnly = false)
	private void updateTextRequest(String textRequest, String chash) {
		// Request text
		log.debug("Checking request text");
		if (textRequest != null && !"".equals(textRequest)) {
			log.debug("Searching request text");
			PfRequestsTextDTO reqText = (PfRequestsTextDTO) baseDAO
					.queryElementOneParameter("request.requestTextHash",
							"hash", chash);
			log.debug("Updating request text");
			reqText.setTrequest(textRequest);
			baseDAO.update(reqText);
		}
	}

	/**
	 * Change the type of the sign lines.
	 * 
	 * @param req
	 */
	@Transactional(readOnly = false, rollbackFor=PfirmaException.class)
	private void changeSignLinesType(PfRequestsDTO req) {
		PfSignLinesDTO signLinePrevious = null;
		List<AbstractBaseDTO> list = baseDAO.queryListOneParameter("request.requestSignLineAsc", "chash", req.getChash());
		if (list != null && !list.isEmpty()) {
			for (AbstractBaseDTO signLine : list) {
				if (req.getLcascadeSign()) {
					if (signLinePrevious != null) {
						((PfSignLinesDTO) signLine)
								.setPfSignLine(signLinePrevious);
						log.debug("Updating the sign line");
						baseDAO.update(signLine);
					}
					signLinePrevious = ((PfSignLinesDTO) signLine);
				} else {
					((PfSignLinesDTO) signLine).setPfSignLine(null);
					log.debug("Updating the sign line");
					baseDAO.update(signLine);
				}
			}
		}
	}

	/**
	 * Check if the signer exists in the request.
	 * 
	 * @param cdni
	 * @param chash
	 * @return
	 */
	private List<PfSignersDTO> checkSigner(String cdni, String chash, PfSignLinesDTO signLine) {
		log.debug("Checking exists signer");
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("hash", chash);
		parameters.put("dni", cdni.toUpperCase());
		List<AbstractBaseDTO> resultado = null;
		
		if (signLine != null) {
			parameters.put("signLineId", signLine.getPrimaryKey());
			resultado = baseDAO.queryListMoreParameters("request.signersRequestUserLine", parameters);
		} else {
			resultado = baseDAO.queryListMoreParameters("request.signersRequestUser", parameters);
		}
		
		if (!resultado.isEmpty()) {
			log.debug("Signer exists");
		} else {
			log.debug("Signer not exists");
		}
		
		List<PfSignersDTO> signers = new ArrayList<PfSignersDTO>();
		for (AbstractBaseDTO signer: resultado) {
			signers.add((PfSignersDTO)signer);
		}
		return signers;
	}

	/**
	 * Check if the request is sent.
	 * 
	 * @param chash
	 * @return
	 */
	private boolean isSendRequest(String chash) {
		boolean result = false;
		log.debug("Checking request send");

		if (chash != null && !"".equals(chash)) {
			Map<String, Object> params = new HashMap<String, Object>();
			params.put("chash", chash);
			Long reqCount = baseDAO.queryCount("request.requestStateTagAssociated", params);
			if (reqCount > 0) {
				result = true;
			}
		}

		return result;
	}

	/**
	 * Check if the request is not finished.
	 * 
	 * @param chash Request hash.
	 * @return True if it's not finished, False otherwise.
	 */
//	public boolean checkNotFinishedRequest(String chash) {
//		boolean result = false;
//		log.debug("Checking request not finished");
//
//		// Si la petición tiene alguna etiqueta del tipo "NUEVA", "LEIDO",
//		// "EN ESPERA" o "VALIDADO" no está finalizada.
//		if (chash != null && !"".equals(chash)) {
//			List<PfRequestTagsDTO> requestTags = baseDAO.queryListOneParameter("request.allRequestTagsFromHash", "chash", chash);
//
//			for (PfRequestTagsDTO requestTag :  requestTags) {
//				if (requestTag.getPfTag().getCtag().equals(Constants.C_TAG_AWAITING) 		||
//					requestTag.getPfTag().getCtag().equals(Constants.C_TAG_AWAITING_PASSED) ||
//					requestTag.getPfTag().getCtag().equals(Constants.C_TAG_NEW)  			||
//					requestTag.getPfTag().getCtag().equals(Constants.C_TAG_READ)) {
//					result = true;
//				}
//			}
//		}
//
//		return result;
//	}

	/**
	 * Elimina una petición que no se encuentre en un estado final.
	 * Solo puede eliminarla el usuario que la creó.
	 * @param authentication Autenticación del usuario.
	 * @param requestId Identificador de la petición.
	 * @throws PfirmaException Excepción del Portafirmas.
	 */
	@Transactional(readOnly = false, rollbackFor=PfirmaException.class)
	public void removeRequest(Authentication authentication, Holder<String> requestId, String removingMessage) throws PfirmaException {
		try {
			log.warn("removeRequest: " + requestId.value + " autenticación: " + requestId.value);
			PfUsersDTO userDTO = authenticationWSHelper.authenticateWebservice(authentication, WSUtil.getWsProfiles());
			String errorMessage = null;
			
			// Check if the request exists
			log.debug("Checking the request");
			PfRequestsDTO request = queryServiceBO.checkRequest(requestId.value);

			if (request != null && request.getChash() != null && !"".equals(request.getChash())) {
				
				//Comprobamos que el usuario tenga acceso a la peticion
				log.debug("Checking user can access saved request");
				authenticationWSHelper.chequeaAccesoUsuarioPeticion(authentication, request);				
				
				// Check if the request is not sended
				log.debug("Checking if the request is not sended");
				if (!requestBO.isSendedRequest(request)){
					errorMessage =  messages.getProperty("errorRequestSended");
					log.error(errorMessage);
					throw new PfirmaException(errorMessage);
				}
				
				// Check if the request is not finished
				log.debug("Checking if the request is not finished");
				//errorMessage = requestBO.checkNotFinishedRequest(request);
				if (!requestBO.isFinishedRequest(request)) {
					log.debug("Removing request");

					// Se retira la petición
					requestBO.insertRemoveRequest(userDTO, request, removingMessage);
				} else {
					errorMessage =  messages.getProperty("errorRequestFinished");
					log.error(errorMessage);
					throw new PfirmaException(errorMessage);
				}
				
			} else {
				log.error("Error al borrar la petición");
				throw new PfirmaException("Error al borrar petición");
			}

			log.info("removeRequest end ");

		} catch (PfirmaException pf) {
			log.error("Error al borrar petición: ", pf);
			throw pf;
		} catch (Throwable e) {
			log.error("Error inesperado al borrar petición: ", e);
			throw new PfirmaException("Unknown error", e);
		}
	}

	/**
	 * Check the document type.
	 * 
	 * @param cdocumentType
	 * @return
	 */
	private PfDocumentTypesDTO checkDocumentType(String cdocumentType) {
		PfDocumentTypesDTO documentType = null;
		log.debug("Checking documentType");
		if (cdocumentType != null && !"".equals(cdocumentType)) {
			log.debug("Searching documentType");
			documentType = (PfDocumentTypesDTO) baseDAO
					.queryElementOneParameter("request.documentTypeId", "type",
							cdocumentType);
			if (documentType != null) {
				log.debug("DocumentType found");
			} else {
				// application not found
				log.debug("DocumentType: " + cdocumentType + " not found");
			}
		} else {
			// application not valid
			log.debug("DocumentType not valid");
		}
		if (documentType == null) {
			log.debug("Assigned to default document type");
			documentType = (PfDocumentTypesDTO) baseDAO
					.queryElementOneParameter("request.documentTypeId", "type",
							Constants.C_DOCUMENTTYPE_GENERIC);
		}
		return documentType;
	}

	// private void insertNotices(PfRequestsDTO requestDTO, NoticeList
	// noticeList)
	// throws PfirmaException {
	// PfNoticeRequestsDTO noticeRequestDTO = null;
	// Set<State> noticeSet = new HashSet<State>();
	// noticeSet.addAll(noticeList.getState());
	// for (State state : noticeSet) {
	// if (stateTags.containsKey(state.getIdentifier())) {
	// noticeRequestDTO = new PfNoticeRequestsDTO();
	// noticeRequestDTO.setPfRequest(requestDTO);
	// noticeRequestDTO.setPfTag(stateTags.get(state.getIdentifier()));
	// baseDAO.insertOrUpdate(noticeRequestDTO);
	// } else {
	// log.error("The state " + state.getIdentifier()
	// + " does not exist");
	// throw new PfirmaException("The state " + state.getIdentifier()
	// + " does not exist");
	// }
	// }
	// }

	// private void insertActions(PfRequestsDTO requestDTO, ActionList
	// actionList)
	// throws PfirmaException {
	// PfActionsDTO actionDTO = null;
	// List<State> stateList = new ArrayList<State>();
	// for (Action action : actionList.getAction()) {
	// if (stateTags.containsKey(action.getState().getIdentifier())) {
	// if (!stateList.contains(action.getState())) {
	// actionDTO = new PfActionsDTO();
	// actionDTO.setPfRequest(requestDTO);
	// actionDTO.setPfTag(stateTags.get(action.getState()
	// .getIdentifier()));
	// actionDTO.setTaction(action.getAction());
	// if (null == action.getAction()
	// || "".equals(action.getAction())
	// || Constants.C_ACTIONS_WEB.equals(action.getType())) {
	// actionDTO.setCtype(Constants.C_ACTIONS_WEB);
	// } else {
	// actionDTO.setCtype(Constants.C_ACTIONS_PLSQL);
	// }
	// baseDAO.insertOrUpdate(actionDTO);
	// stateList.add(action.getState());
	// } else {
	// log.error("The state " + action.getState().getIdentifier()
	// + " is associated to many actions");
	// throw new PfirmaException("The state "
	// + action.getState().getIdentifier()
	// + " is associated to many actions");
	// }
	// } else {
	// log.error("The state " + action.getState().getIdentifier()
	// + " does not exist");
	// throw new PfirmaException("The state "
	// + action.getState().getIdentifier() + " does not exist");
	// }
	// }
	// }

	/**
	 * M&eacute;todo que a&ntilde;ade o actualiza notificaciones de una petici&oacute;n en la BD.
	 * @param requestDTO Petici&oacute;n.
	 * @param noticeList Listado de notificaciones.
	 */
	//@Transactional(readOnly = false)
	private void insertOrUpdateNotices(PfRequestsDTO requestDTO,
			NoticeList noticeList) throws PfirmaException {

		PfNoticeRequestsDTO noticeRequestDTO = null;
		List<AbstractBaseDTO> noticeRequestDTOList = baseDAO
				.queryListOneParameter("request.requestNoticesHash", "hash",
						requestDTO.getChash());
		if (noticeList != null && noticeList.getState() != null
				&& !noticeList.getState().isEmpty()) {
			Set<State> noticeSet = new HashSet<State>();
			noticeSet.addAll(noticeList.getState());
			Map<String, Object> parameters = new HashMap<String, Object>();
			parameters.put("req", requestDTO);
			for (State state : noticeSet) {
				if (applicationVO.getStateTags().containsKey(state.getIdentifier())) {
					parameters.put("cTag", state.getIdentifier());
					noticeRequestDTO = (PfNoticeRequestsDTO) baseDAO
							.queryElementMoreParameters(
									"request.queryNoticeRequestByCtag",
									parameters);
					if (null == noticeRequestDTO) {
						noticeRequestDTO = new PfNoticeRequestsDTO();
						noticeRequestDTO.setPfRequest(requestDTO);
						noticeRequestDTO.setPfTag(applicationVO.getStateTags().get(state
								.getIdentifier()));
						baseDAO.insertOrUpdate(noticeRequestDTO);
					} else {
						noticeRequestDTOList.remove(noticeRequestDTO);
					}
				} else {
					log.error("The state " + state.getIdentifier()
							+ " does not exist");
					throw new PfirmaException("The state "
							+ state.getIdentifier() + " does not exist");
				}
			}
		}
		if (!noticeRequestDTOList.isEmpty()) {
			baseDAO.deleteList(noticeRequestDTOList);
		}
	}

	/**
	 * M&eacute;todo que inserta o actualiza acciones de  una petici&oacute;n en la BD.
	 * @param requestDTO Petici&oacute;n.
	 * @param actionList Listado de acciones.
	 * @throws PfirmaException Excepci&oacute;n del portafirmas.
	 */
	//@Transactional(readOnly = false)
	private void insertOrUpdateActions(PfRequestsDTO requestDTO, ActionList actionList) throws PfirmaException {
		PfActionsDTO actionDTO = null;
		Map<String, Object> parameters = new HashMap<String, Object>();
		List<PfRequestsDTO> requestList = new ArrayList<PfRequestsDTO>();
		requestList.add(requestDTO);
		parameters.put("reqs", requestList);
		List<AbstractBaseDTO> actionDTOList = baseDAO.queryListMoreParameters("request.requestsActionsByRequestAll", parameters);
		if (actionList != null && actionList.getAction() != null && !actionList.getAction().isEmpty()) {
			List<State> stateList = new ArrayList<State>();
			for (Action action : actionList.getAction()) {
				if (applicationVO.getStateTags().containsKey(action.getState().getIdentifier())) {
					if (!stateList.contains(action.getState())) {
						parameters.put("tag", action.getState().getIdentifier());
						actionDTO = (PfActionsDTO) baseDAO.queryElementMoreParameters("request.requestsActionsByRequest", parameters);
						if (null == actionDTO) {
							actionDTO = new PfActionsDTO();
							actionDTO.setPfRequest(requestDTO);
							actionDTO.setPfTag(applicationVO.getStateTags().get(action.getState().getIdentifier()));
						} else {
							actionDTOList.remove(actionDTO);
						}
						actionDTO.setTaction(action.getAction());
						if (null == action.getAction()
								|| "".equals(action.getAction())
								|| Constants.C_ACTIONS_WEB.equals(action.getType())) {
							actionDTO.setCtype(Constants.C_ACTIONS_WEB);
						} else {
							actionDTO.setCtype(Constants.C_ACTIONS_PLSQL);
						}
						baseDAO.insertOrUpdate(actionDTO);
						stateList.add(action.getState());
					} else {
						log.error("The state " + action.getState().getIdentifier() + " is associated to many actions");
						throw new PfirmaException("The state " + action.getState().getIdentifier() + " is associated to many actions");
					}
				} else {
					log.error("The state " + action.getState().getIdentifier() + " does not exist");
					throw new PfirmaException("The state " + action.getState().getIdentifier() + " does not exist");
				}
			}
		}
		if (!actionDTOList.isEmpty()) {
			baseDAO.deleteList(actionDTOList);
		}
	}

	/**
	 * Método que inserta el nivel de importancia de la petición
	 * @param requestDTO Petición
	 * @param importanceLevel Nivel de importancia
	 * @throws PfirmaException
	 */
	private void insertImportanceLevel(PfRequestsDTO requestDTO, ImportanceLevel importanceLevel) throws PfirmaException {
		PfImportanceLevelsDTO level = null;

		if (importanceLevel == null ||
			importanceLevel.getLevelCode() == null ||
			importanceLevel.getLevelCode().equals("")) {
			level = (PfImportanceLevelsDTO) baseDAO.queryElementOneParameter("request.queryNormalLevel", null, null);
		}
		else {
			level = (PfImportanceLevelsDTO) baseDAO.queryElementOneParameter("request.queryImportanceLevelByCode",
																			 "codLevel", importanceLevel.getLevelCode());
		}
		requestDTO.setPfImportance(level);
	}

	/**
	 * Comprueba que una aplicaci&oacute;n no tenga aplicaciones hijo
	 * Este m&eacute;todo se usa al crear, modificar o enviar una petici&oacute;n para 
	 * asegurarnos que no enviamos peticiones asociadas a aplicaciones que tienen hijos
	 */
	private void checkApplicationHasNoChildren(PfApplicationsDTO application) throws PfirmaException {
		if (!application.getPfApplications().isEmpty()) {
			throw new PfirmaException("Application " + application.getCapplication() + " has children. Can't be assigned to a request");
		}
	}
	
	/**
	 * Actualiza en el objeto signLineDTO el tipo de firma, seg&uacute;n venga en el String signLineType
	 * 
	 * <ul>
	 * <li>Si signLineType est&aacute; vac&iacute;o, se conserva el valor antiguo (Se rellena como "FIRMA" si el valor antiguo est&aacute; vac&iacute;o)</li>
	 * <li>Si signLineType tiene un valor v&aacute;lido, se le asigna a la linea de firma</li>
	 * <li>Si signLineType tiene un valor no v&aacute;lido, lanza una excepci&oacute;n</li>
	 * </ul>
	 * 
	 * @param signLineDTO
	 * @param signLineType
	 * @throws PfirmaException
	 */
	private void actualizarTipoFirmaEnLineaDeFirma(PfSignLinesDTO signLineDTO, String signLineType) throws PfirmaException{
		if (signLineType == null || "".equals(signLineType)) {
			//Si la linea ya exist&iacute;a, dejamos el valor que tuviese. En caso contrario ponemos "FIRMA"
			if (signLineDTO.getCtype() == null) { 
				signLineDTO.setCtype(Constants.C_TYPE_SIGNLINE_SIGN);
			}
		} else if (Constants.C_TYPE_SIGNLINE_SIGN.equals(signLineType) || Constants.C_TYPE_SIGNLINE_PASS.equals(signLineType)) {
			signLineDTO.setCtype(signLineType);
		} else {
			log.error("Incorrect sign line type " + signLineType);
			throw new PfirmaException("Incorrect sign line type " + signLineType);
		}		
	}
}

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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.xml.datatype.XMLGregorianCalendar;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.seap.minhap.portafirmas.business.NoticeBO;
import es.seap.minhap.portafirmas.business.administration.ApplicationAdmBO;
import es.seap.minhap.portafirmas.business.administration.JobAdmBO;
import es.seap.minhap.portafirmas.business.administration.UserAdmBO;
import es.seap.minhap.portafirmas.business.configuration.AuthorizationBO;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.dao.RequestDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfApplicationsDTO;
import es.seap.minhap.portafirmas.domain.PfAuthorizationTypesDTO;
import es.seap.minhap.portafirmas.domain.PfDocumentTypesDTO;
import es.seap.minhap.portafirmas.domain.PfFiltersDTO;
import es.seap.minhap.portafirmas.domain.PfParametersDTO;
import es.seap.minhap.portafirmas.domain.PfProfilesDTO;
import es.seap.minhap.portafirmas.domain.PfProvinceDTO;
import es.seap.minhap.portafirmas.domain.PfRequestTagsDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfUsersAuthorizationDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.domain.PfUsersEmailDTO;
import es.seap.minhap.portafirmas.domain.PfUsersJobDTO;
import es.seap.minhap.portafirmas.domain.PfUsersParameterDTO;
import es.seap.minhap.portafirmas.domain.PfUsersProfileDTO;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.utils.ws.WSUtil;
import es.seap.minhap.portafirmas.ws.bean.Authentication;
import es.seap.minhap.portafirmas.ws.bean.DocumentType;
import es.seap.minhap.portafirmas.ws.bean.DocumentTypeList;
import es.seap.minhap.portafirmas.ws.bean.EnhancedJob;
import es.seap.minhap.portafirmas.ws.bean.EnhancedJobList;
import es.seap.minhap.portafirmas.ws.bean.EnhancedUser;
import es.seap.minhap.portafirmas.ws.bean.EnhancedUserList;
import es.seap.minhap.portafirmas.ws.bean.Parameter;
import es.seap.minhap.portafirmas.ws.bean.ParameterList;
import es.seap.minhap.portafirmas.ws.bean.SeatList;
import es.seap.minhap.portafirmas.ws.bean.StringList;
import es.seap.minhap.portafirmas.ws.exception.PfirmaException;


@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class AdminServiceBO {


	private Logger log = Logger.getLogger(AdminServiceBO.class);

	@Autowired
	private ApplicationAdmBO applicationAdmBO;
	
	@Autowired
	private RequestDAO requestDAO;

	@Autowired
	private UserAdmBO userAdmBO;

	@Autowired
	private JobAdmBO jobAdmBO;

	@Autowired
	private BaseDAO baseDAO;
	
	@Autowired
	private NoticeBO noticeBO;
	
	@Autowired
	AuthorizationBO autorizacionBO;

	@Autowired
	private AuthenticationWSHelper authenticationWSHelper;

	@Autowired
	private QueryServiceBO queryServiceBO;

	private static String PARAM_LDAP_IDENTIFIER = "identificador.ldap";
	private static String PARAM_EMAIL = "email";
	private static String PARAM_NOTIFICACION = "notificacion";

	private static String[] ACCEPTED_PARAMS_USER = new String[]{PARAM_LDAP_IDENTIFIER, PARAM_EMAIL,PARAM_NOTIFICACION};

	/*
	 * WS METHODS
	 */

	/**
	 * Inserta los tipos de documento de la lista que pasamos como par&aacute;metro en la aplicaci&oacute;n
	 * @param applicationId el c&oacute;digo de la aplicaci&oacute;n
	 * @param documentTypeList contiene la lista de tipos de documento
	 * @return el c&oacute;digo de la aplicaci&oacute;n
	 * @throws PfirmaException si no recuperamos la aplicaci&oacute;n con el c&oacute;digo pasado como par&aacute;metro
	 * o uno de los tipos de documento que queremos insertar ya existe
	 */
	@Transactional(readOnly = false, rollbackFor=PfirmaException.class)
	public String insertDocumentsType(String applicationId,
			DocumentTypeList documentTypeList) throws PfirmaException {
		log.info("insertDocumentsType init");
		try {
			// Query application
			//Obtiene el objeto de aplicaciones con la pk que hemos pasado como par&aacute;metro
			PfApplicationsDTO applicationDTO = 
					(PfApplicationsDTO) applicationAdmBO.applicationCodeQuery(applicationId);
			//Si hemos recuperado la aplicaci&oacute;n
			if (applicationDTO != null) {
				for (DocumentType documentType : documentTypeList
						.getDocumentType()) {
					// Query document
					//Recuperamos el tipo de documento de la aplicaci&oacute;n
					PfDocumentTypesDTO documentTypeQuery = applicationAdmBO
							.queryApplicationDocumentType(applicationDTO,
									documentType.getIdentifier());
					//Si hemos recuperado el tipo de documento
					//el documento ya ha sido insertado
					if (documentTypeQuery != null) {
						// Document type inserted already
						log.error("Document type "
								+ documentTypeQuery.getCdocumentType()
								+ " inserted already");
						throw new PfirmaException("Document type "
								+ documentTypeQuery.getCdocumentType()
								+ " inserted already");
						//si no ha sido insertado lo insertamos en bbdd
					} else {
						//transformamos el bean en un objetod e negocio
						PfDocumentTypesDTO documentTypeDTO = WSUtil
								.documentTypeToPfDocumentTypeDTO(documentType,
										applicationDTO);
						baseDAO.insertOrUpdate(documentTypeDTO);
					}

				}
				log.info("insertDocumentsType end");

				return applicationId;
			} else {
				// Application not found
				log.error("Application " + applicationId + " not found");
				throw new PfirmaException("Application" + applicationId
						+ " not found");
			}
		} catch (PfirmaException pf) {
			throw pf;
		} catch (Throwable e) {
			throw new PfirmaException("Unknown error", e);
		}
	}
	/**
	 * Borra los tipos de documentos pasados en la lista como par&aacute;metro de la aplicaci&oacute;n 
	 * @param applicationId el id de la aplicaci&oacute;n
	 * @param documentTypeList la lista de beans de tipo de documento
	 * @return el c&oacute;digo de la aplicaci&oacute;n
	 * @throws PfirmaException si no recuperamos la aplicaci&oacute;n con el c&oacute;digo pasado como par&aacute;metro
	 * o uno de los tipos de documento que queremos borrar no existe
	 */
	@Transactional(readOnly = false, rollbackFor=PfirmaException.class)
	public String deleteDocumentsType(String applicationId,
			DocumentTypeList documentTypeList) throws PfirmaException {
		log.info("deleteDocumentsType init");
		try {
			// Query application
			//Obtenemos la aplicaci&oacute;n
			PfApplicationsDTO applicationDTO = (PfApplicationsDTO) applicationAdmBO
					.applicationCodeQuery(applicationId);
			//Si hemos encontrado la aplicaci&oacute;n
			if (applicationDTO != null) {
				for (DocumentType documentType : documentTypeList
						.getDocumentType()) {
					// Query document
					//recuperamos el tipo de documento
					PfDocumentTypesDTO documentTypeQuery = applicationAdmBO
							.queryApplicationDocumentType(applicationDTO,
									documentType.getIdentifier());

					if (documentTypeQuery == null) {
						// Document type not found
						log.error("Document type not found");
						throw new PfirmaException("Document type not found");
					} else {
//						PfDocumentTypesDTO documentTypeDTO = WSUtil
//								.documentTypeToPfDocumentTypeDTO(documentType,
//										applicationDTO);
						baseDAO.delete(documentTypeQuery);
					}

				}
				log.info("deleteDocumentsType end");

				return applicationId;
			} else {
				// Application not found
				log.error("Application " + applicationId + " not found");
				throw new PfirmaException("Application" + applicationId
						+ " not found");
			}
		} catch (PfirmaException pf) {
			throw pf;
		} catch (Throwable e) {
			throw new PfirmaException("Unknown error", e);
		}
	}
	/**
	 * Actualiza los tipos de documentos pasados en la lista como par&aacute;metro de la aplicaci&oacute;n 
	 * @param applicationId el id de la aplicaci&oacute;n
	 * @param documentTypeList la lista de beans de tipo de documento
	 * @return el c&oacute;digo de la aplicaci&oacute;n
	 * @throws PfirmaException si no recuperamos la aplicaci&oacute;n con el c&oacute;digo pasado como par&aacute;metro
	 * o uno de los tipos de documento que queremos actualizar no existe
	 */
	@Transactional(readOnly = false, rollbackFor=PfirmaException.class)
	public String updateDocumentsType(String applicationId,
			DocumentTypeList documentTypeList) throws PfirmaException {
		log.info("updateDocumentsType init");
		try {
			// Query application
			//recuperamos la aplicaci&oacute;n
			PfApplicationsDTO applicationDTO = (PfApplicationsDTO) applicationAdmBO
					.applicationCodeQuery(applicationId);

			if (applicationDTO != null) {
				for (DocumentType documentType : documentTypeList
						.getDocumentType()) {
					// Query document
					PfDocumentTypesDTO documentTypeQuery = applicationAdmBO
							.queryApplicationDocumentType(applicationDTO,
									documentType.getIdentifier());

					if (documentTypeQuery == null) {
						// Document type not found
						log.error("Document type not found");
						throw new PfirmaException("Document type not found");
					} else {
//						PfDocumentTypesDTO documentTypeDTO = WSUtil
//								.documentTypeToPfDocumentTypeDTO(documentType,
//										applicationDTO);
						documentTypeQuery.setCdocumentType(documentType.getIdentifier());
						documentTypeQuery.setDdocumentType(documentType.getDescription());
						documentTypeQuery.setLvalid(documentType.getValid());
						baseDAO.insertOrUpdate(documentTypeQuery);
					}

				}
				log.info("updateDocumentsType end");

				return applicationId;
			} else {
				// Application not found
				log.error("Application " + applicationId + " not found");
				throw new PfirmaException("Application" + applicationId
						+ " not found");
			}
		} catch (PfirmaException pf) {
			throw pf;
		} catch (Throwable e) {
			throw new PfirmaException("Unknown error", e);
		}
	}
	@Transactional(readOnly = false, rollbackFor=PfirmaException.class)
	public Integer insertUsers (Authentication authentication, EnhancedUserList enhancedUserList) throws PfirmaException {
		log.debug("insertUsers init");
		PfUsersDTO userApp = authenticationWSHelper.authenticateWebservice(authentication, WSUtil.getWsUserManagerProfiles());

		for (EnhancedUser enhancedUser : enhancedUserList.getEnhancedUser()) {			
			insertUser(enhancedUser, userApp);
		}
		log.debug("insertUsers end");
		return enhancedUserList.getEnhancedUser().size();

	}

	/*@Transactional
	public Integer insertUser (Authentication authentication, EnhancedUser enhancedUser) throws PfirmaException {
		PfUsersDTO userApp = authenticationWSHelper.authenticateWebservice(authentication, WSUtil.getWsProfiles());

		insertUser(enhancedUser, userApp.getPrimaryKeyString());

		return 1;
	}*/

	@Transactional(readOnly = false, rollbackFor=PfirmaException.class)
	public Integer insertJobs (Authentication authentication, EnhancedJobList enhancedJobList) throws PfirmaException {
		log.debug("insertJobs init");
		PfUsersDTO userApp = authenticationWSHelper.authenticateWebservice(authentication, WSUtil.getWsUserManagerProfiles());

		for (EnhancedJob enhancedJob : enhancedJobList.getEnhancedJob()) {
			insertJob(enhancedJob, userApp);
		}

		log.debug("insertJobs end");
		return enhancedJobList.getEnhancedJob().size();

	}

	@Transactional(readOnly = false, rollbackFor=PfirmaException.class)
	public Integer updateUsers (Authentication authentication, EnhancedUserList enhancedUserList) throws PfirmaException {
		log.debug("updateUsers init");
		PfUsersDTO userApp = authenticationWSHelper.authenticateWebservice(authentication, WSUtil.getWsUserManagerProfiles());

		for (EnhancedUser enhancedUser : enhancedUserList.getEnhancedUser()) {
			updateUser(enhancedUser, userApp);
		}
		log.debug("updateUsers end");
		return enhancedUserList.getEnhancedUser().size();
	}

	@Transactional(readOnly = false, rollbackFor=PfirmaException.class)
	public Integer updateJobs (Authentication authentication, EnhancedJobList enhancedJobList) throws PfirmaException {
		log.debug("updateJobs init");
		PfUsersDTO userApp = authenticationWSHelper.authenticateWebservice(authentication, WSUtil.getWsUserManagerProfiles());

		for (EnhancedJob enhancedJob : enhancedJobList.getEnhancedJob()) {
			updateJob(enhancedJob, userApp);
		}

		log.debug("updateJobs end");
		return enhancedJobList.getEnhancedJob().size();

	}
	@Transactional(readOnly = false, rollbackFor=PfirmaException.class)
	public Integer deleteUsers (Authentication authentication, StringList userIdentifiers) throws PfirmaException {
		log.debug("deleteUsers init");
		PfUsersDTO userApp = authenticationWSHelper.authenticateWebservice(authentication, WSUtil.getWsUserManagerProfiles());
		List<String> ids = userIdentifiers.getStr();
		for (String id: ids) {
			deleteUser(id, userApp);
		}		
		log.debug("deleteUsers end");
		return userIdentifiers.getStr().size();
	}

	@Transactional(readOnly = false, rollbackFor=PfirmaException.class)
	public Integer deleteJobs (Authentication authentication, StringList jobIdentifiers) throws PfirmaException {
		log.debug("deleteJobs init");
		PfUsersDTO userApp = authenticationWSHelper.authenticateWebservice(authentication, WSUtil.getWsUserManagerProfiles());

		List<String> ids = jobIdentifiers.getStr();
		for (String id: ids) {
			deleteJob(id, userApp);
		}				
		log.debug("deleteJobs end");
		return jobIdentifiers.getStr().size();
	}

	@Transactional(readOnly = false, rollbackFor=PfirmaException.class)
	public boolean assignJobToUser (Authentication authentication, String jobIdentifier, String userIdentifier, XMLGregorianCalendar fstart, XMLGregorianCalendar fend) throws PfirmaException {

		log.debug("assignJobToUser init, jobIdentifier: " + jobIdentifier + ", userIdentifier: " + userIdentifier);
		PfUsersDTO userApp = authenticationWSHelper.authenticateWebservice(authentication, WSUtil.getWsUserManagerProfiles());

		PfUsersJobDTO usersJob = null;

		Date dstart = null;
		if (fstart != null) {
			dstart = fstart.toGregorianCalendar().getTime();
		} else {
			dstart = new Date();
		}
		Date dend = null;
		if (fend != null) {
			dend = fend.toGregorianCalendar().getTime();
		}
		// Validación de que la fecha de fin no es anterior a la fecha de inicio.
		if (fend != null) {
			if (!Util.getInstance().checkDate(dstart, dend)) {
				log.debug("Las fechas no son válidas");
				throw new PfirmaException ("Las fechas no son válidas");
			}
		}

		// Validación de que exista el cargo y esté vigente		
		List<AbstractBaseDTO> list = queryServiceBO.queryEnhancedUserJob(jobIdentifier, null, Constants.C_TYPE_USER_JOB, "S");		

		if (list.size() == 0) {
			log.debug("No se ha encontrado cargo vigente con identificador " + jobIdentifier);
			throw new PfirmaException ("No se ha encontrado cargo vigente con identificador " + jobIdentifier);
		}		

		if (list.size() > 1) {
			log.debug("Se ha encontrado más de un cargo con identificador " + jobIdentifier);
			throw new PfirmaException ("Se ha encontrado más de un cargo con identificador " + jobIdentifier);
		}


		PfUsersDTO jobDTO = (PfUsersDTO) list.get(0);

		// Validación de acceso del usuario de la aplicación a la sede del cargo
		authenticationWSHelper.chequeaAccesoSede(userApp, jobDTO.getPfProvince().getCcodigoprovincia());

		list = baseDAO.queryListOneParameter("administration.jobUserAdmAssociated", "job", jobDTO);

		// validación de que el cargo no esté asociado a un usuario
		for (AbstractBaseDTO abs : list) {
			PfUsersJobDTO usersJobDTO = (PfUsersJobDTO) abs;
			if (!usersJobDTO.getPfUser().getCidentifier().equals(userIdentifier)) {
				if (Util.getInstance().checkPeriodOverlap(usersJobDTO.getFstart(), usersJobDTO.getFend(), 
						dstart, dend)) {
					log.debug("El cargo con identificador " + jobIdentifier + " está asociado a otro usuario ");
					throw new PfirmaException ("El cargo con identificador " + jobIdentifier + " está asociado a un usuario ");
				}				
			} else {
				usersJob = usersJobDTO;
				break;
			}				
		}

		// validación de que el usuario exista		
		list = queryServiceBO.queryEnhancedUserJob(userIdentifier, null, Constants.C_TYPE_USER_USER, "S");		

		if (list.size() == 0) {
			log.debug("No se ha encontrado usuario vigente con identificador " + userIdentifier);
			throw new PfirmaException ("No se ha encontrado usuario vigente con identificador " + userIdentifier);
		}		

		if (list.size() > 1) {
			log.debug("Se ha encontrado más de un usuario con identificador " + userIdentifier);
			throw new PfirmaException ("Se ha encontrado más de un usuario con identificador " + userIdentifier);
		}

		PfUsersDTO userDTO = (PfUsersDTO) list.get(0);

		// Validación de acceso del usuario de la aplicación a la sede del usuario
		authenticationWSHelper.chequeaAccesoSede(userApp, userDTO.getPfProvince().getCcodigoprovincia());

		// Si todavía no hemos encontrado la relación
		if (usersJob == null) {		

			// validación de que el usuario no esté asociado a un cargo
			list = baseDAO.queryListOneParameter("request.userJob", "usuario", userDTO);

			for (AbstractBaseDTO abs : list) {
				PfUsersJobDTO usersJobDTO = (PfUsersJobDTO) abs;
				if (!usersJobDTO.getPfUserJob().getCidentifier().equals(jobIdentifier)) {
					if (Util.getInstance().checkPeriodOverlap(usersJobDTO.getFstart(), usersJobDTO.getFend(), 
							dstart, dend)) {
						log.debug("El usuario con identificador " + userIdentifier + " está asociado a otro cargo ");
						throw new PfirmaException ("El cargo con identificador " + userIdentifier + " está asociado a otro cargo ");
					}				
				} else {
					usersJob = usersJobDTO;
					break;
				}				
			}
		}

		if (usersJob == null) {
			usersJob = new PfUsersJobDTO ();
			usersJob.setPfUser(userDTO);
			usersJob.setPfUserJob(jobDTO);
		}
		usersJob.setFstart(dstart);
		usersJob.setFend(dend);
		baseDAO.insertOrUpdate(usersJob);

		return true;
	}

	@Transactional(readOnly = false, rollbackFor=PfirmaException.class)
	public boolean separateJobToUser (Authentication authentication, String jobIdentifier, String userIdentifier) throws PfirmaException {
		PfUsersDTO userApp = authenticationWSHelper.authenticateWebservice(authentication, WSUtil.getWsUserManagerProfiles());
		// Validación de que exista el cargo		
		List<AbstractBaseDTO> list = queryServiceBO.queryEnhancedUserJob(jobIdentifier, null, Constants.C_TYPE_USER_JOB, null);		

		if (list.size() == 0) {
			log.debug("No se ha encontrado cargo con identificador " + jobIdentifier);
			throw new PfirmaException ("No se ha encontrado cargo con identificador " + jobIdentifier);
		}		

		if (list.size() > 1) {
			log.debug("Se ha encontrado más de un cargo con identificador " + jobIdentifier);
			throw new PfirmaException ("Se ha encontrado más de un cargo con identificador " + jobIdentifier);
		}

		PfUsersDTO jobDTO = (PfUsersDTO) list.get(0);

		authenticationWSHelper.chequeaAccesoSede(userApp, jobDTO.getPfProvince().getCcodigoprovincia());

		// validación de que el usuario exista		
		list = queryServiceBO.queryEnhancedUserJob(userIdentifier, null, Constants.C_TYPE_USER_USER, null);		

		if (list.size() == 0) {
			log.debug("No se ha encontrado usuario con identificador " + userIdentifier);
			throw new PfirmaException ("No se ha encontrado usuario con identificador " + userIdentifier);
		}		

		if (list.size() > 1) {
			log.debug("Se ha encontrado más de un usuario con identificador " + userIdentifier);
			throw new PfirmaException ("Se ha encontrado más de un usuario con identificador " + userIdentifier);
		}

		PfUsersDTO userDTO = (PfUsersDTO) list.get(0);
		authenticationWSHelper.chequeaAccesoSede(userApp, userDTO.getPfProvince().getCcodigoprovincia());

		// validación de que el cargo esté asociado al usuario del que se quiere desasignar
		list = baseDAO.queryListOneParameter("administration.jobUserAdmAssociated", "job", jobDTO);

		if (list.size() == 0) {
			log.debug("El cargo con identificador " + jobIdentifier + " no está asociado a ningún usuario");
			throw new PfirmaException ("El cargo con identificador " + jobIdentifier + " no está asociado a ningún usuario ");
		}

		PfUsersJobDTO usersJob = null;
		Iterator<AbstractBaseDTO> it = list.iterator();
		while (it.hasNext() && usersJob == null) {
			PfUsersJobDTO aux = (PfUsersJobDTO) it.next();
			if (aux != null && aux.getPfUser() != null && aux.getPfUser().getCidentifier().contentEquals(userIdentifier)) {
				usersJob = aux;
			}
		}

		if (usersJob == null) {
			log.debug("El cargo con identificador " + jobIdentifier + " no está asociado al usuario con identificador " + userIdentifier);
			throw new PfirmaException ("El cargo con identificador " + jobIdentifier + " no está asociado al usuario con identificador " + userIdentifier);
		}

		baseDAO.delete(usersJob);
		return true;
	}


	public void insertUser (EnhancedUser enhancedUser, PfUsersDTO userApp) throws PfirmaException {
		PfUsersDTO userDTO = checkUser (enhancedUser, userApp, "INSERT");
		insertaOModificaUsuario (userDTO, enhancedUser);	

	}

	private void insertJob (EnhancedJob enhancedJob, PfUsersDTO userApp) throws PfirmaException {
		PfUsersDTO userDTO = checkJob(enhancedJob, userApp, "INSERT");
		insertaOModificaCargo(userDTO, enhancedJob);
	}

	private void updateUser (EnhancedUser enhancedUser, PfUsersDTO userApp) throws PfirmaException {
		PfUsersDTO userDTO = checkUser (enhancedUser, userApp, "UPDATE");
		insertaOModificaUsuario(userDTO, enhancedUser);		
	}

	private void updateJob (EnhancedJob enhancedJob, PfUsersDTO userApp) throws PfirmaException {
		PfUsersDTO userDTO = checkJob (enhancedJob, userApp, "UPDATE");
		insertaOModificaCargo(userDTO, enhancedJob);		

	}

	/**
	 * Elimina un usuario de la aplicación. Si el usuario tiene peticiones asociadas lo pondrá como NO vigente.
	 * @param identifier
	 * @param userAppkey
	 * @throws PfirmaException
	 */
	//@Transactional(readOnly = false)
	private void deleteUser (String identifier, PfUsersDTO userApp) throws PfirmaException {

		List<AbstractBaseDTO> userList = queryServiceBO.queryEnhancedUserJob(identifier, null, Constants.C_TYPE_USER_USER, null);

		if (userList.size() == 0) {
			throw new PfirmaException ("No existe usuario con identificador " + identifier);
		}

		if (userList.size() > 1) {
			throw new PfirmaException ("Existe más de un usuario con identificador " + identifier);
		}

		PfUsersDTO userDTO = (PfUsersDTO) userList.get(0);

		authenticationWSHelper.chequeaAccesoSede(userApp, userDTO.getPfProvince().getCcodigoprovincia());

		ArrayList<String> warning = userAdmBO.requestAssociated(userDTO);

		if (warning.isEmpty()) {
			if (userDTO.getPfUsersProfiles() != null) {
				deleteSet(userDTO.getPfUsersProfiles());
			}
			if (userDTO.getPfUsersParameters() != null) {
				deleteSet(userDTO.getPfUsersParameters());
			}
			if (userDTO.getPfUsersEmails() != null) {
				deleteSet(userDTO.getPfUsersEmails());
			}
			if (userDTO.getPfUsersMobiles() != null) {
				deleteSet(userDTO.getPfUsersMobiles());
			}
			if (userDTO.getPfUsersProvinces() != null) {
				deleteSet(userDTO.getPfUsersProvinces());
			}
			if (userDTO.getPfSessionAttributes() != null) {
				deleteSet(userDTO.getPfSessionAttributes());
			}
			baseDAO.delete(userDTO);
		} else {
			userDTO.setLvalid(false);
			baseDAO.insertOrUpdate(userDTO);
		}
	}

	/**
	 * Elimina un cargo de la aplicación. Si el cargo tiene peticiones asociadas lo pondrá como NO vigente.
	 * @param identifier
	 * @param userAppkey
	 * @throws PfirmaException
	 */
	//@Transactional(readOnly = false, rollbackFor=PfirmaException.class))
	private void deleteJob (String identifier, PfUsersDTO userApp) throws PfirmaException {
		List<AbstractBaseDTO> userList = queryServiceBO.queryEnhancedUserJob(identifier, null, Constants.C_TYPE_USER_JOB, null);

		if (userList.size() == 0) {
			throw new PfirmaException ("No existe cargo con identificador " + identifier);
		}

		if (userList.size() > 1) {
			throw new PfirmaException ("Existe más de un cargo con identificador " + identifier);
		}

		PfUsersDTO userDTO = (PfUsersDTO) userList.get(0);

		authenticationWSHelper.chequeaAccesoSede(userApp, userDTO.getPfProvince().getCcodigoprovincia());

		ArrayList<String> warning = jobAdmBO.requestAssociatedWS(userDTO);

		if (warning.isEmpty()) {
			baseDAO.delete(userDTO);
		} else {
			userDTO.setLvalid(false);
			baseDAO.insertOrUpdate(userDTO);
		}
	}

	//@Transactional(readOnly = false)
	private void deleteSet (Set<?> set) {
		Iterator<?> it = set.iterator();
		while (it.hasNext()) {
			AbstractBaseDTO dto = (AbstractBaseDTO) it.next();
			baseDAO.delete(dto);
		}
	}
	/**
	 * 	 * Obtiene la entidad correspondiente a un usuario, haciendo las validaciones oportunas, que son:
	 * - Validación de campos.
	 * - Formateo de campos
	 * - Validación de existencia de la sede
	 * 
	 * - Si se está insertando:
	 * 	- Si ya existe un usuario con el mismo identificador y está vigente, error.
	 * 	- Si ya existe un usuario con el mismo LDAP, error.
	 * 	- Si ya existe un usuario con el mismo identificador y no está vigente, se devolverá el encontrado
	 *  - Si no existe se crea una nueva entidad.
	 *
	 *- Si se está modificando:
	 *	- Si no existe un usuario con el identificador pasado, dará error.
	 *  - Si sí que existe se devuelve el encontrado.
	 *  - Si ya existe un usuario con el mismo LDAP, error.
	 *	
	 * @param enhancedJob
	 * @param operation
	 * @return
	 * @throws PfirmaException
	 * @param enhancedUser
	 * @param operation
	 * @return
	 * @throws PfirmaException
	 */
	private PfUsersDTO checkUser (EnhancedUser enhancedUser, PfUsersDTO userApp, final String operation) throws PfirmaException {

		String identificadorLdap = getValorParametro (AdminServiceBO.PARAM_LDAP_IDENTIFIER, enhancedUser.getEnhancedUserJobInfo().getParameterList());

		log.debug("checkUser init, operation: " + operation);
		PfUsersDTO userDTO = null;

		log.debug("Validando enhancedUser");
		// Comprobamos los parámetros obligatorios
		validaUsuario (enhancedUser);

		log.debug("Formateando enhancedUser");
		// Formateamos los campos
		formateaUsuario (enhancedUser);

		authenticationWSHelper.chequeaAccesoSede(userApp, enhancedUser.getEnhancedUserJobInfo().getSeat().getCode());		

		// Comprobamos que la sede exista
		log.debug("Comprobando sede");
		SeatList seats = queryServiceBO.querySeatsNoAuthentication(enhancedUser.getEnhancedUserJobInfo().getSeat().getCode());
		if (seats.getSeat().size() == 0) {
			log.debug("La sede con código " + enhancedUser.getEnhancedUserJobInfo().getSeat().getCode() + "no existe");
			throw new PfirmaException ("La sede con código " + enhancedUser.getEnhancedUserJobInfo().getSeat().getCode() + " no existe ");
		}

		// Comprobamos los usuarios con el mismo identificador
		List<AbstractBaseDTO> users = queryServiceBO.queryEnhancedUserJob (enhancedUser.getUser().getIdentifier(), null, Constants.C_TYPE_USER_USER, null);

		// Si se ha encontrado más de un usuario con el mismo identificador, lanzamos excepción
		if (users.size() > 1) {
			log.debug("Existe más de un usuario con identificador " + enhancedUser.getUser().getIdentifier());
			throw new PfirmaException ("Existe más de un usuario con identificador " + enhancedUser.getUser().getIdentifier());
		}

		// Comprobamos los usuarios con el mismo LDAP
		PfUsersDTO usersLDAP = userAdmBO.getUserByLDAP (identificadorLdap);

		// Si se ha encontrado más de un usuario con el mismo LDAP, lanzamos excepción
		if (usersLDAP != null) {
			if(users.get(0).getPrimaryKey() == null || !users.get(0).getPrimaryKey().equals(usersLDAP.getPrimaryKey())){
				log.debug("Existe más de un usuario con LDAP " + identificadorLdap);
				throw new PfirmaException ("Existe más de un usuario con LDAP " + identificadorLdap);
			}
		}

		// Si se está insertando se comprueba que el usuario con mismo identificador no esté vigente.
		if ("INSERT".contentEquals(operation)) {
			if (users.size() == 1) {
				log.debug("El usuario con identificador " + enhancedUser.getUser().getIdentifier() + " existe ");
				userDTO = (PfUsersDTO) users.get(0);					
				if (userDTO.getLvalid()) {
					log.debug("Ya existe un usuario válido con identificador " + enhancedUser.getUser().getIdentifier());
					throw new PfirmaException ("Ya existe un usuario con identificador " + enhancedUser.getUser().getIdentifier());
				}
				authenticationWSHelper.chequeaAccesoSede(userApp, userDTO.getPfProvince().getCcodigoprovincia());
			} else {
				log.debug("El usuario con identificador " + enhancedUser.getUser().getIdentifier() + "no existe, lo crearemos nuevo ");
				userDTO = new PfUsersDTO();
				userDTO.setLshownotifwarning(true);
			}
			// Si se está modificando se comprueba que exista usuario con el mismo identificador
		} else if ("UPDATE".contentEquals(operation)) {
			if (users.size() == 0) {
				log.debug("No existe un usuario con identificador " + enhancedUser.getUser().getIdentifier());
				throw new PfirmaException ("No existe un usuario con identificador " + enhancedUser.getUser().getIdentifier());
			} else {
				userDTO = (PfUsersDTO) users.get(0);
				log.debug("Se ha encontrado usuario con identificador " + enhancedUser.getUser().getIdentifier());
				if (!userDTO.getLvalid()) {
					log.debug("El usuario con identificador " + enhancedUser.getUser().getIdentifier() + " no está vigente y por tanto no puede modificarse ");
					throw new PfirmaException ("El usuario con identificador " + enhancedUser.getUser().getIdentifier() + " no está vigente y por tanto no puede modificarse ");
				}
				authenticationWSHelper.chequeaAccesoSede(userApp, userDTO.getPfProvince().getCcodigoprovincia());
			}
		}

		return userDTO;
	}

	/**
	 * Obtiene la entidad correspondiente a un cargo, haciendo las validaciones oportunas, que son:
	 * - Validación de campos.
	 * - Validación de existencia de la sede
	 * - Se comprueba que no exista un cargo con la misma descripción y la misma sede.
	 * - Si se está insertando:
	 * 	- Si ya existe un cargo con el mismo identificador y está vigente, error.
	 * 	- Si ya existe un cargo con el mismo identificador y no está vigente, se devolverá el encontrado
	 *  - Si no existe se crea una nueva entidad.
	 *
	 *- Si se está modificando:
	 *	- Si no existe un cargo con el identificador pasado, dará error.
	 *  - Si sí que existe se devuelve el encontrado.
	 *	
	 * @param enhancedJob
	 * @param operation
	 * @return
	 * @throws PfirmaException
	 */
	private PfUsersDTO checkJob (EnhancedJob enhancedJob, PfUsersDTO userApp, final String operation) throws PfirmaException {
		log.debug("checkJob init, operation: " + operation);
		PfUsersDTO userDTO = null;

		log.debug("Validando cargo");
		validaCargo (enhancedJob);
		formateaCargo(enhancedJob);

		authenticationWSHelper.chequeaAccesoSede(userApp, enhancedJob.getEnhancedUserJobInfo().getSeat().getCode());

		// Comprobamos que la sede exista
		log.debug("Comprobando la existencia de la sede");
		SeatList seats = queryServiceBO.querySeatsNoAuthentication(enhancedJob.getEnhancedUserJobInfo().getSeat().getCode());
		if (seats.getSeat().size() == 0) {
			log.debug("La sede con código " + enhancedJob.getEnhancedUserJobInfo().getSeat().getCode() + " no existe ");
			throw new PfirmaException ("La sede con código " + enhancedJob.getEnhancedUserJobInfo().getSeat().getCode() + " no existe ");
		}

		// Comprobamos que no exista un cargo con la misma descripción para la misma Sede.
		List<AbstractBaseDTO> jobs = queryServiceBO.queryEnhancedUserJob (enhancedJob.getJob().getDescription(), 
				enhancedJob.getEnhancedUserJobInfo().getSeat().getCode(), 
				Constants.C_TYPE_USER_JOB, null);

		if (jobs.size() > 1) {
			log.debug("Existe más de un cargo con descripción " + enhancedJob.getJob().getDescription() + " para la sede con código: " + enhancedJob.getEnhancedUserJobInfo().getSeat().getCode());
			throw new PfirmaException ("Existe más de un cargo con descripción " + enhancedJob.getJob().getDescription() + " para la sede con código: " + enhancedJob.getEnhancedUserJobInfo().getSeat().getCode());
		} else if (jobs.size() == 1) {
			// Si el identificador del ya existente NO coincide con el nuevo lanzamos una excepción,
			// pues si permitimos la inserción o la modificación tendremos dos usuarios con la misma descripción y la misma sede.
			PfUsersDTO job = (PfUsersDTO) jobs.get(0);				
			if (!job.getCidentifier().equalsIgnoreCase(enhancedJob.getJob().getIdentifier())) {
				log.debug("Ya existe un cargo con descripción " + enhancedJob.getJob().getDescription() + " para la sede con código: " + enhancedJob.getEnhancedUserJobInfo().getSeat().getCode());
				throw new PfirmaException ("Ya existe un cargo con descripción " + enhancedJob.getJob().getDescription() + " para la sede con código: " + enhancedJob.getEnhancedUserJobInfo().getSeat().getCode());
			}
		}



		// Comprobamos los cargos con el mismo identificador
		jobs = queryServiceBO.queryEnhancedUserJob (enhancedJob.getJob().getIdentifier(), null, Constants.C_TYPE_USER_JOB, null);

		if (jobs.size() > 1) {
			log.debug("Existe más de un cargo con identificador " + enhancedJob.getJob().getIdentifier());
			throw new PfirmaException ("Existe más de un cargo con identificador " + enhancedJob.getJob().getIdentifier());
		}

		// Si se está insertando se comprueba que no exista un cargo vigente con el mismo identificador.
		if ("INSERT".contentEquals(operation)) {
			if (jobs.size() == 1) {
				userDTO = (PfUsersDTO) jobs.get(0);
				if (userDTO.getLvalid()) {
					log.debug("Ya existe un cargo con identificador " + enhancedJob.getJob().getIdentifier());
					throw new PfirmaException ("Ya existe un cargo con identificador " + enhancedJob.getJob().getIdentifier());
				} 
				authenticationWSHelper.chequeaAccesoSede(userApp, userDTO.getPfProvince().getCcodigoprovincia());
			} else if (jobs.size() == 0) {
				log.debug("No existe un cargo con identificador " + enhancedJob.getJob().getIdentifier() + " , lo creamos nuevo");
				userDTO = new PfUsersDTO();
				userDTO.setLshownotifwarning(true);
			}
			// Si se está modificando se comprueba que exista un cargo con el mismo identificador.
		} else if ("UPDATE".contentEquals(operation)) {
			if (jobs.size() == 0) {
				log.debug("No existe un cargo con identificador " + enhancedJob.getJob().getIdentifier());
				throw new PfirmaException ("No existe un cargo con identificador " + enhancedJob.getJob().getIdentifier());
			} else if (jobs.size() == 1) {
				log.debug("Existe un cargo con identificador " + enhancedJob.getJob().getIdentifier());
				userDTO = (PfUsersDTO) jobs.get(0);
				if (!userDTO.getLvalid()) {
					log.debug("El cargo con identificador " + enhancedJob.getJob().getIdentifier() + " no está vigente y por tanto no puede modificarse ");
					throw new PfirmaException ("El cargo con identificador " + enhancedJob.getJob().getIdentifier() + " no está vigente y por tanto no puede modificarse ");
				}
				authenticationWSHelper.chequeaAccesoSede(userApp, userDTO.getPfProvince().getCcodigoprovincia());
			}
		}
		return userDTO;

	}

	/**
	 * Inserta o modifica un usuario. 
	 * @param userDTO Objeto que se rellenará con las propiedades del otro parámetro y que se insertará (o modificará).
	 * @param enhancedUser Objeto que contiene las propiedades del objeto que vamos a insertar o modificar
	 * @throws PfirmaException
	 */
	//@Transactional(readOnly = false)
	private void insertaOModificaUsuario (PfUsersDTO userDTO, EnhancedUser enhancedUser) throws PfirmaException {
		log.debug("insertaOModificaUsuario init");

		WSUtil.fillUserDTOWithEnhancedUser(enhancedUser, userDTO);
		userDTO.setPfProvince(getPfProvince(enhancedUser.getEnhancedUserJobInfo().getSeat().getCode()));
		baseDAO.insertOrUpdate(userDTO);

		String emailUsuario = getValorParametro (AdminServiceBO.PARAM_EMAIL, enhancedUser.getEnhancedUserJobInfo().getParameterList());
		String notificar = getValorParametro (AdminServiceBO.PARAM_NOTIFICACION, enhancedUser.getEnhancedUserJobInfo().getParameterList());
		if (emailUsuario != null && !("").equalsIgnoreCase(emailUsuario)) {
			PfUsersEmailDTO userEmail = getUserParameterEmail(userDTO, emailUsuario, notificar);
			baseDAO.insertOrUpdate(userEmail);
		}
		String identificadorLdap = getValorParametro (AdminServiceBO.PARAM_LDAP_IDENTIFIER, enhancedUser.getEnhancedUserJobInfo().getParameterList());
		if (identificadorLdap != null && !("").equalsIgnoreCase(identificadorLdap)) {
			PfUsersParameterDTO userParamLdap = getUserParameterLdap(userDTO, identificadorLdap);			
			baseDAO.insertOrUpdate(userParamLdap);	
		}
		/*if (enhancedUser.getIdentificadorLdap() != null && !("").equalsIgnoreCase(enhancedUser.getIdentificadorLdap())) {
			PfUsersParameterDTO userParamLdap = getUserParameterLdap(userDTO, enhancedUser.getIdentificadorLdap());			
			baseDAO.insertOrUpdate(userParamLdap);			
		}*/

		// Obtenemos los perfiles que tiene que tener el usuario
		//[Ticket1284#Teresa Quito el perfil de redacción]
		//List<PfUsersProfileDTO> usersProfileList = getUsersProfile (userDTO, new String[] {Constants.C_PROFILES_ACCESS, Constants.C_PROFILES_REDACTION, Constants.C_PROFILES_SIGN});
		List<PfUsersProfileDTO> usersProfileList = getUsersProfile (userDTO, new String[] {Constants.C_PROFILES_ACCESS, Constants.C_PROFILES_SIGN});
		
		//[Ticket1284#Teresa]
		userAdmBO.updateMostrarFirmanteAnterior(userDTO, true);
		//userAdmBO.updateLNotifyPush(userDTO, true);

		for (PfUsersProfileDTO usersProfileDTO : usersProfileList) {
			baseDAO.insertOrUpdate(usersProfileDTO);
		}		
		
		log.debug("insertaOModificaUsuario end");
	}

	/**
	 * Inserta o modifica un cargo
	 * @param userDTO Objeto que se rellenará con las propiedades del otro parámetro y se insertará (o modificará)
	 * @param enhancedJob Objeto que contiene las propiedades del cargo que vamos a insertar o modificar
	 * @throws PfirmaException
	 */
	//@Transactional(readOnly = false)
	private void insertaOModificaCargo (PfUsersDTO userDTO, EnhancedJob enhancedJob) throws PfirmaException {
		log.debug("insertaOModificaCargo init");

		WSUtil.fillUserDTOWithEnhancedJob(enhancedJob, userDTO);
		userDTO.setPfProvince(getPfProvince(enhancedJob.getEnhancedUserJobInfo().getSeat().getCode()));

		baseDAO.insertOrUpdate(userDTO);		
		log.debug("insertaOModificaCargo end");
	}
	
	public boolean estaCreadaAutorizacion(Authentication authentication, XMLGregorianCalendar fstart, XMLGregorianCalendar fend, String userIdenAutoriza, String userIdenAutorizado, String descripcion, String entidad) throws PfirmaException {
		boolean estaCreada = false;
		
		List<AbstractBaseDTO> listAutoriza = queryServiceBO.queryEnhancedUserJob(userIdenAutoriza, null, Constants.C_TYPE_USER_USER, null);		
		List<AbstractBaseDTO> listAutorizado = queryServiceBO.queryEnhancedUserJob(userIdenAutorizado, null, Constants.C_TYPE_USER_USER, null);

		if (listAutoriza.size() == 0 || listAutorizado.size()==0) {
			log.debug("No se ha encontrado usuario que AUTORIZA/AUTORIZADO con identificador " + userIdenAutoriza+"/"+userIdenAutorizado);
			throw new PfirmaException ("No se ha encontrado usuario que AUTORIZA/AUTORIZADO con identificador " + userIdenAutoriza+"/"+userIdenAutorizado);
		}		

		if (listAutoriza.size() > 1 || listAutorizado.size() > 1) {
			log.debug("Se ha encontrado más de un usuario que AUTORIZA/AUTORIZADO con identificador " + userIdenAutoriza+"/"+userIdenAutorizado);
			throw new PfirmaException ("Se ha encontrado más de un usuario que AUTORIZA/AUTORIZADO con identificador " + userIdenAutoriza+"/"+userIdenAutorizado);
		}
		
		PfUsersDTO sender = (PfUsersDTO) listAutoriza.get(0);
		PfUsersDTO receiver = (PfUsersDTO) listAutorizado.get(0);
		
		Date dateInici = null;
		Date dateFin = null;
		
		try {
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
			String horaInicio = "00:00";
			DateFormat df2 = new SimpleDateFormat("MM/dd/yyyy");
			String startUserDateString = df2.format(fstart.toGregorianCalendar().getTime());
			startUserDateString = startUserDateString+" "+horaInicio;
			dateInici = df.parse(startUserDateString);
		} catch (ParseException e) {
			log.error ("Error inesperado en insertarAutorizaciones:"+e.getMessage(), e);
			throw new PfirmaException ("Error inesperado en insertarAutorizaciones"+e.getMessage(), e);
		}
		
		try {
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
			String horaFin = "23:59";
			DateFormat df2 = new SimpleDateFormat("MM/dd/yyyy");
			String startUserDateString = df2.format(fend.toGregorianCalendar().getTime());
			startUserDateString = startUserDateString+" "+horaFin;
			dateFin = df.parse(startUserDateString);
		} catch (ParseException e) {
			log.error ("Error inesperado en insertarAutorizaciones:"+e.getMessage(), e);
			throw new PfirmaException ("Error inesperado en insertarAutorizaciones"+e.getMessage(), e);
		}
		
		List<AbstractBaseDTO> queryAuthorizationTypeActivasByAutoriza = autorizacionBO.queryAuthorizationForDate(sender, receiver, dateInici, dateFin, entidad);
		for (AbstractBaseDTO abstractBaseDTO : queryAuthorizationTypeActivasByAutoriza) {
			PfUsersAuthorizationDTO autorActivas = (PfUsersAuthorizationDTO) abstractBaseDTO;
			autorizacionBO.revokeAuthorization(autorActivas);
		}
		return estaCreada;
	}
	
	public boolean insertarAutorizacion(Authentication authentication, XMLGregorianCalendar fstart, XMLGregorianCalendar fend, String userIdenAutoriza, String userIdenAutorizado
			, String descripcion, String entidad) throws PfirmaException {
		
		boolean insertado = false;
		// validación de que el usuario exista		
		List<AbstractBaseDTO> listAutoriza = queryServiceBO.queryEnhancedUserJob(userIdenAutoriza, null, Constants.C_TYPE_USER_USER, null);		
		List<AbstractBaseDTO> listAutorizado = queryServiceBO.queryEnhancedUserJob(userIdenAutorizado, null, Constants.C_TYPE_USER_USER, null);

		if (listAutoriza.size() == 0 || listAutorizado.size()==0) {
			log.debug("No se ha encontrado usuario que AUTORIZA/AUTORIZADO con identificador " + userIdenAutoriza+"/"+userIdenAutorizado);
			throw new PfirmaException ("No se ha encontrado usuario que AUTORIZA/AUTORIZADO con identificador " + userIdenAutoriza+"/"+userIdenAutorizado);
		}		

		if (listAutoriza.size() > 1 || listAutorizado.size() > 1) {
			log.debug("Se ha encontrado más de un usuario que AUTORIZA/AUTORIZADO con identificador " + userIdenAutoriza+"/"+userIdenAutorizado);
			throw new PfirmaException ("Se ha encontrado más de un usuario que AUTORIZA/AUTORIZADO con identificador " + userIdenAutoriza+"/"+userIdenAutorizado);
		}
		
		PfUsersDTO sender = (PfUsersDTO) listAutoriza.get(0);
		PfUsersDTO receiver = (PfUsersDTO) listAutorizado.get(0);
		
		PfUsersAuthorizationDTO usuarioAutori = new PfUsersAuthorizationDTO();
		usuarioAutori.createAuditing();
		
		
		try {
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
			String horaInicio = "00:00";
			DateFormat df2 = new SimpleDateFormat("MM/dd/yyyy");
			String startUserDateString = df2.format(fstart.toGregorianCalendar().getTime());
			startUserDateString = startUserDateString+" "+horaInicio;
			Date dateInici = df.parse(startUserDateString);
			usuarioAutori.setFauthorization(dateInici);
		} catch (ParseException e) {
			log.error ("Error inesperado en insertarAutorizaciones:"+e.getMessage(), e);
			throw new PfirmaException ("Error inesperado en insertarAutorizaciones"+e.getMessage(), e);
		}
		
		try {
			DateFormat df = new SimpleDateFormat("MM/dd/yyyy HH:mm");
			String horaFin = "23:59";
			DateFormat df2 = new SimpleDateFormat("MM/dd/yyyy");
			String startUserDateString = df2.format(fend.toGregorianCalendar().getTime());
			startUserDateString = startUserDateString+" "+horaFin;
			Date dateFin = df.parse(startUserDateString);
			usuarioAutori.setFrevocation(dateFin);
		} catch (ParseException e) {
			log.error ("Error inesperado en insertarAutorizaciones:"+e.getMessage(), e);
			throw new PfirmaException ("Error inesperado en insertarAutorizaciones"+e.getMessage(), e);
		}
		
		usuarioAutori.setPfUser(sender);
		usuarioAutori.setTobservations("Delegación ALSIGM");
		usuarioAutori.setPfAuthorizedUser(receiver);
		usuarioAutori.setFrequest(new Date());
		usuarioAutori.setTobservations(descripcion);
		
		PfAuthorizationTypesDTO pfAuthorizationType = new PfAuthorizationTypesDTO();
		pfAuthorizationType.setPrimaryKeyString("1");
		usuarioAutori.setPfAuthorizationType(pfAuthorizationType);
		usuarioAutori.setEntidad(entidad);
		
		
		
		List<AbstractBaseDTO> queryAuthorizationTypeActivasByAutoriza = autorizacionBO.queryAuthorizationForDate(sender, receiver, usuarioAutori.getFauthorization(), usuarioAutori.getFrevocation(), entidad);
		for (AbstractBaseDTO abstractBaseDTO : queryAuthorizationTypeActivasByAutoriza) {
			PfUsersAuthorizationDTO autorActivas = (PfUsersAuthorizationDTO) abstractBaseDTO;
			autorizacionBO.revokeAuthorization(autorActivas);

		}
		
		// Se realiza la persistencia
		autorizacionBO.saveAuthorization(usuarioAutori);
		

		PfFiltersDTO filter = new PfFiltersDTO();
		filter.setPfUser(sender);
		List<AbstractBaseDTO> resultRequest = requestDAO.queryFilterRequests(filter, null);
		for (AbstractBaseDTO requestTag : resultRequest) {
			PfRequestsDTO  reqTag = (PfRequestsDTO) requestTag;		                                                                  
			// Execute authorizations
			Set<PfRequestTagsDTO> reqTagSet = reqTag.getPfRequestsTags();
			for (PfRequestTagsDTO pfRequestTagsDTO : reqTagSet) {
				autorizacionBO.applyAuthorizations(pfRequestTagsDTO.getPfRequest());
			}
			
		}

		
		
	    //noticeBO.noticeNewAuthorization(usuarioAutori, true, false);
	    insertado = true;
		return insertado;
	}
	
	public boolean revocarAutorizacionActiva(Authentication authentication, String userIdenAutoriza, String userIdenAutorizado, XMLGregorianCalendar fstart, XMLGregorianCalendar fend, String entidad) throws PfirmaException {
		
		boolean insertado = false;
				
		List<AbstractBaseDTO> listAutoriza = queryServiceBO.queryEnhancedUserJob(userIdenAutoriza, null, Constants.C_TYPE_USER_USER, null);
		List<AbstractBaseDTO> listAutorizado = queryServiceBO.queryEnhancedUserJob(userIdenAutorizado, null, Constants.C_TYPE_USER_USER, null);

		if (listAutoriza.size() == 0 || listAutorizado.size()==0) {
			log.debug("No se ha encontrado usuario que AUTORIZA/AUTORIZADO con identificador " + userIdenAutoriza+"/"+userIdenAutorizado);
			throw new PfirmaException ("No se ha encontrado usuario que AUTORIZA/AUTORIZADO con identificador " + userIdenAutoriza+"/"+userIdenAutorizado);
		}		

		if (listAutoriza.size() > 1 || listAutorizado.size() > 1) {
			log.debug("Se ha encontrado más de un usuario que AUTORIZA/AUTORIZADO con identificador " + userIdenAutoriza+"/"+userIdenAutorizado);
			throw new PfirmaException ("Se ha encontrado más de un usuario que AUTORIZA/AUTORIZADO con identificador " + userIdenAutoriza+"/"+userIdenAutorizado);
		}
		
		PfUsersDTO sender = (PfUsersDTO) listAutoriza.get(0);
		PfUsersDTO receiver = (PfUsersDTO) listAutorizado.get(0);	
		List<AbstractBaseDTO> queryAuthorizationTypeActivasByAutoriza = autorizacionBO.queryAuthorizationForDate(sender, receiver, fstart.toGregorianCalendar().getTime(), fend.toGregorianCalendar().getTime(), entidad);
		for (AbstractBaseDTO abstractBaseDTO : queryAuthorizationTypeActivasByAutoriza) {
			PfUsersAuthorizationDTO autorActivas = (PfUsersAuthorizationDTO) abstractBaseDTO;
			autorizacionBO.revokeAuthorization(autorActivas);
			
			//Metodo que obtiene la lista de peticiones a mostrar en la bandeja de peticiones correspondiente.
			PfFiltersDTO filter = new PfFiltersDTO();
			filter.setPfUser(sender);
			List<AbstractBaseDTO> resultRequest = requestDAO.queryFilterRequests(filter, null);
			for (AbstractBaseDTO requestTag : resultRequest) {
				PfRequestsDTO  reqTag = (PfRequestsDTO) requestTag;		                                                                  
				// Execute authorizations
				Set<PfRequestTagsDTO> reqTagSet = reqTag.getPfRequestsTags();
				for (PfRequestTagsDTO pfRequestTagsDTO : reqTagSet) {		                                                                  
				// Execute authorizations
					if(pfRequestTagsDTO.getPfRequest().getDreference().contains(entidad)){
						autorizacionBO.revocarAutorizacionActiva(pfRequestTagsDTO.getPfRequest(), sender, receiver);
					}
				}
			}
		}
			
				
		
		
		insertado = true;
		return insertado;
	}
	
	private String obtenerEntidad(String dreference) {

		String [] vReferencia = dreference.split("_");
		String entidad = "000";
		if(vReferencia!=null && vReferencia.length == 3){
			entidad = vReferencia[vReferencia.length-1];
		}
		return entidad;
	}
	
	/**
	 * Devuelve la entidad correspondiente a la provincia con código el mismo que el parámetro.
	 * @param code
	 * @return
	 * @throws PfirmaException
	 */
	private PfProvinceDTO getPfProvince (String code) throws PfirmaException {
		AbstractBaseDTO province = null;
		try {
			province =  baseDAO.queryElementOneParameter("request.querySeats", "find", code);
		} catch (Exception e) {
			throw new PfirmaException ("No se puede recuperar la provincia con código " + code, e);
		}
		return (PfProvinceDTO) province;
	}

	/**
	 * Devuelve la relación usuario-email que se corresponde con el identificador ldap del usuario
	 * Si no existía, la crea nueva
	 * @param user - usuario.
	 * @param email valor del email
	 * @return 
	 */
	private PfUsersEmailDTO getUserParameterEmail (PfUsersDTO user, String email, String notificar) {
		log.debug("getUserParameterEmail init");
		PfUsersEmailDTO result = null;
		// Si la lista de emails del usuario está vacía, creamos la relación desde cero
		if (user.getPfUsersEmails() == null) {
			result = new PfUsersEmailDTO ();
			result.setDemail(email);
			result.setPfUser(user);
			result.setLnotify(true);

			// Si la lista no está vacía, buscamos si alguna de las relaciones se corresponde con el parámetro email
		} else {
			for (PfUsersEmailDTO useremailparam : user.getPfUsersEmails()) {
				if (useremailparam.getDemail().contentEquals(email)) {
					useremailparam.setLnotify(true);
					result = useremailparam;
					break;
				}
				else{
					useremailparam.setLnotify(false);
				}
			}
			// Si no existía la relación, la creamos desde cero.
			if (result == null) {
				result = new PfUsersEmailDTO ();
				result.setDemail(email);
				result.setPfUser(user);
				result.setLnotify(true);
			}
		}
		if (notificar != null && !("").equalsIgnoreCase(notificar) && ("N".equals(notificar) || "S".equals(notificar) )) {
			if ("N".equals(notificar)){
				result.setLnotify(false);
			}  else {
				result.setLnotify(true);
			}
		}
		log.debug("getUserParameterEmail end");
		return result;
	}



	/**
	 * Devuelve la relación usuario-parámetro que se corresponde con el identificador ldap del usuario
	 * Si no existía, la crea nueva
	 * @param usersParameters - Lista de relaciones usuario-parámetro que tiene el usuario.
	 * @param ldap Nuevo valor del parámetro
	 * @return 
	 */
	private PfUsersParameterDTO getUserParameterLdap (PfUsersDTO user, String ldap) {
		log.debug("getUserParameterLdap init");
		PfUsersParameterDTO result = null;
		// Si la lista de parámetros del usuario está vacía, creamos la relación desde cero
		if (user.getPfUsersParameters() == null) {
			result = new PfUsersParameterDTO ();
			result.setPfParameter(getLdapParameter());
			result.setPfUser(user);
			// Si la lista no está vacía, buscamos si alguna de las relaciones se corresponde con el parámetro ldap
		} else {
			for (PfUsersParameterDTO userparam : user.getPfUsersParameters()) {
				if (userparam.getPfParameter().getCparameter().contentEquals(Constants.LOGIN_LDAP_IDATTRIBUTE)) {
					result = userparam;
					break;
				}
			}
			// Si no existía la relación, la creamos desde cero.
			if (result == null) {
				result = new PfUsersParameterDTO ();
				result.setPfParameter(getLdapParameter());
				result.setPfUser(user);
			}
		}		
		result.setTvalue(ldap);
		log.debug("getUserParameterLdap end");
		return result;
	}

	private PfParametersDTO getLdapParameter () {
		return (PfParametersDTO) baseDAO.queryElementOneParameter("administration.ldapIdParameter",null, null);
	}


	/**
	 * Obtiene la entidad correspondiente al perfil pasado como parámetro
	 * @param profileName
	 * @return
	 */
	public AbstractBaseDTO getProfile (String profileName) {
		return baseDAO.queryElementOneParameter("administration.profileByName", "name", profileName);
	}

	/**
	 * Devuelve la lista de relaciones usuario-perfil que se corresponde con el nombre de cada uno 
	 * de los pefiles que se le pasa como parámetro en la lista de nombres.
	 * Si para alguno de los perfiles no existe dicha relación, la crea
	 * @param usersParameters - Lista de relaciones usuario-parámetro que tiene el usuario.
	 * @param ldap Nuevo valor del parámetro
	 * @return 
	 */
	private List<PfUsersProfileDTO> getUsersProfile (PfUsersDTO userDTO, String[] profileNames) {
		log.debug("getUsersProfile init");
		List<PfUsersProfileDTO> usersProfileList = new ArrayList<PfUsersProfileDTO> ();
		for (String profileName : profileNames) {
			if ((userDTO.getPfUsersProfiles() == null) || (!tienePerfil(userDTO.getPfUsersProfiles(), profileName))) {
				PfUsersProfileDTO usersProfile = new PfUsersProfileDTO ();
				usersProfile.setPfProfile((PfProfilesDTO)getProfile(profileName));
				usersProfile.setPfUser(userDTO);
				usersProfile.setFstart(new Date());
				usersProfileList.add(usersProfile);
			} 
		}
		log.debug("getUsersProfile end");
		return usersProfileList;
	}

	/**
	 * Comprueba si el perfil con nombre pasado como parámetro está en la lista.
	 * @param usersProfileList
	 * @param profileName
	 * @return
	 */
	private boolean tienePerfil (Set<PfUsersProfileDTO> usersProfileList, String profileName) {
		int i=0;
		boolean encontrado = false;
		PfUsersProfileDTO[] arrayDTO = usersProfileList.toArray(new PfUsersProfileDTO[0]);
		while (i<arrayDTO.length && !encontrado) {
			PfUsersProfileDTO usersProfile = arrayDTO[i];
			if (usersProfile.getPfProfile().getCprofile().contentEquals(profileName)) {
				encontrado = true;
			}
			i++;
		}
		return encontrado;
	}

	/**
	 * Valida los atributos de un usuario. Si alguno no es correcto lanza una excepción.
	 * @param user
	 * @throws PfirmaException
	 */
	private void validaUsuario (EnhancedUser user) throws PfirmaException {
		if (user.getUser() == null) {
			throw new PfirmaException ("El usuario es obligatorio");
		}
		if (user.getUser().getIdentifier() == null) {
			throw new PfirmaException ("El identificador del usuario es obligatorio");
		}
		if (user.getUser().getName() == null) {
			throw new PfirmaException ("El nombre del usuario es obligatorio");
		}
		if (user.getEnhancedUserJobInfo() == null) {
			throw new PfirmaException ("La información ampliada es obligatoria");
		}
		if (user.getEnhancedUserJobInfo().getSeat() == null) {
			throw new PfirmaException ("La sede es obligatoria");
		}	
		if (user.getEnhancedUserJobInfo().getSeat().getCode() == null) {
			throw new PfirmaException ("El código de sede es obligatorio");
		}
		if (user.getEnhancedUserJobInfo().getParameterList() != null) {
			validaParametrosUsuario (user.getEnhancedUserJobInfo().getParameterList());
		}
	}

	/**
	 * Valida los parámetros de un usuario
	 * @param parameterList
	 * @throws PfirmaException
	 */
	private void validaParametrosUsuario (ParameterList parameterList) throws PfirmaException{
		for (Parameter param : parameterList.getParameter()) {
			validaParametroUsuario (param);
		}
	}

	/**
	 * Valida un parámetro de un usuario
	 * @param param
	 * @throws PfirmaException
	 */
	private void validaParametroUsuario (Parameter param) throws PfirmaException {
		if (param.getIdentifier() == null) {
			throw new PfirmaException ("El parámetro no tiene identificador");
		}
		if (param.getValue() == null) {
			throw new PfirmaException ("El parámetro no tiene valor");
		}
		int i=0; 
		boolean found = false;
		while (i < ACCEPTED_PARAMS_USER.length && !found) {
			if (ACCEPTED_PARAMS_USER[i].contentEquals(param.getIdentifier())) {
				found = true;
			} else {
				i++;
			}
		}
		if (!found) {
			throw new PfirmaException ("El parámetro " + param.getIdentifier() + " no está admitido ");
		}
	}

	/**
	 * Obtiene el valor de un parámetro
	 * @param parameterList
	 * @param idParametro
	 * @return
	 */
	private String getValorParametro (String idParametro, ParameterList parameterList) {
		String valor = null;
		if (parameterList != null && parameterList.getParameter()!= null) {
			int i=0;
			boolean found = false;
			while (i<parameterList.getParameter().size() && !found) {
				if (parameterList.getParameter().get(i) != null 
						&& parameterList.getParameter().get(i).getIdentifier() != null 
						&& parameterList.getParameter().get(i).getIdentifier().contentEquals(idParametro)) {
					valor = parameterList.getParameter().get(i).getValue();
					found = true;
				} else {
					i++;
				}
			}
		}
		return valor;
	}

	/**
	 * Formatea los campos de texto: elimina espacios al principio y al final. Para los NIF completa con ceros a la izquierda.
	 * @param user
	 * @throws PfirmaException
	 */
	private void formateaUsuario (EnhancedUser user) throws PfirmaException {
		user.getUser().setName(user.getUser().getName().trim());
		if (user.getUser().getSurname1() != null) {
			user.getUser().setSurname1(user.getUser().getSurname1().trim());
		}
		if (user.getUser().getSurname2() != null) {
			user.getUser().setSurname2(user.getUser().getSurname2().trim());
		}
		user.getUser().setIdentifier(formatNif(user.getUser().getIdentifier().toUpperCase().trim()));

	}

	/**
	 * Valida los atributos de un cargo.
	 * @param job
	 * @throws PfirmaException
	 */
	private void validaCargo (EnhancedJob job) throws PfirmaException {
		if (job.getJob() == null) {
			throw new PfirmaException ("El cargo es obligatorio");			
		}
		if (job.getJob().getDescription() == null) {
			throw new PfirmaException ("La descripción es obligatoria");
		}
		if (job.getEnhancedUserJobInfo() == null) {
			throw new PfirmaException ("La información ampliada es obligatoria");
		}
		if (job.getEnhancedUserJobInfo().getSeat() == null) {
			throw new PfirmaException ("La sede es obligatoria");
		}		
		if (job.getEnhancedUserJobInfo().getSeat().getCode() == null) {
			throw new PfirmaException ("El código de sede es obligatorio");
		}
	}

	private String formatNif (String nif) {
		while (nif.length() < 9) {
			nif = "0" + nif;
		}
		return nif;
	}
	
	/**
	 * Formatea el identificador del cargo: elimina espacios al principio y al final y lo pone en mayuscula
	 * @param user
	 * @throws PfirmaException
	 */
	private void formateaCargo (EnhancedJob job) throws PfirmaException {
		
		job.getJob().setIdentifier(job.getJob().getIdentifier().toUpperCase().trim());

	}

	
}

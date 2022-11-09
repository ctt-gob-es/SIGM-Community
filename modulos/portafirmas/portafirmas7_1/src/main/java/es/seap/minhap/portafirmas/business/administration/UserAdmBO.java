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

package es.seap.minhap.portafirmas.business.administration;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import es.seap.minhap.portafirmas.business.exception.PortafirmasExceptionBussinesRollback;
import es.seap.minhap.portafirmas.business.ws.clients.autentica.AdminUser;
import es.seap.minhap.portafirmas.business.ws.clients.autentica.DIR4WS;
import es.seap.minhap.portafirmas.business.ws.clients.autentica.DIR4WSService;
import es.seap.minhap.portafirmas.business.ws.clients.autentica.LdapUser;
import es.seap.minhap.portafirmas.business.ws.clients.autentica.WebUser;
import es.seap.minhap.portafirmas.business.ws.clients.autentica.respuestas.getuser.Respuesta;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.dao.UserDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfConfigurationsParameterDTO;
import es.seap.minhap.portafirmas.domain.PfInvitedUsersDTO;
import es.seap.minhap.portafirmas.domain.PfProfilesDTO;
import es.seap.minhap.portafirmas.domain.PfProvinceAdminDTO;
import es.seap.minhap.portafirmas.domain.PfProvinceDTO;
import es.seap.minhap.portafirmas.domain.PfUnidadOrganizacionalDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.domain.PfUsersEmailDTO;
import es.seap.minhap.portafirmas.domain.PfUsersJobDTO;
import es.seap.minhap.portafirmas.domain.PfUsersParameterDTO;
import es.seap.minhap.portafirmas.domain.PfUsersProfileDTO;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.utils.envelope.UserEnvelope;
import es.seap.minhap.portafirmas.web.beans.AdminSeat;
import es.seap.minhap.portafirmas.web.beans.User;
import es.seap.minhap.portafirmas.web.beans.UserJob;
import es.seap.minhap.portafirmas.web.beans.UsersParameters;
import es.seap.minhap.portafirmas.web.comparator.UserJobStarDateComparator;
import es.seap.minhap.portafirmas.web.converter.JobConverter;
import es.seap.minhap.portafirmas.web.converter.UserJobConverter;
import es.seap.minhap.portafirmas.web.validation.JobValidator;
import es.seap.minhap.portafirmas.web.validation.UserJobValidator;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class UserAdmBO {

	private static final String C_PARAM = "cParam";

	private static final String ADMINISTRATION_QUERY_PARAMETER_BY_NAME = "administration.queryParameterByName";

	private static final String USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA = "UserAdmBO.actualizarCargoConAutentica ";


	protected final Log log = LogFactory.getLog(getClass());	
	
	@Autowired
	private BaseDAO baseDAO;

	@Autowired
	private UserDAO userDAO;

	@Resource(name = "messageProperties")
	private Properties messages;
	
	@Autowired
	private UserJobValidator userJobValidator;
	
	@Autowired
	private UserJobConverter userJobConverter;
	
	@Autowired
	private JobValidator jobValidator;
	
	@Autowired
	private JobConverter jobConverter;
	
	@Autowired
	private JobAdmBO jobAdmBO;

	/*
	 * QUERY LISTS
	 */
	/**
	 * Obtiene todos los usuarios, no los cargos, de la bbdd
	 * @return todos los usuarios de la bbdd
	 */
	public List<AbstractBaseDTO> queryList() {
		return baseDAO.queryListMoreParameters("administration.userAdmAll",	null);
	}
	
	public List<AbstractBaseDTO> queryListActiveByDate(Date fechaDesde) {
		Map<String, Object> parameters = new HashMap<String, Object> ();
		parameters.put("fechaDesde", fechaDesde);
		return baseDAO.queryListMoreParameters("administration.userByDate", parameters);
	}

	/**
	 * Obtiene todos los usuarios que pertenecen a una determinada provincia
	 * @param codProvincia Código de provincia
	 * @return Listado de usuarios
	 */
	public List<AbstractBaseDTO> queryListByProvince(String codProvincia) {
		return baseDAO.queryListOneParameter("administration.allUsersByProvince", "provincia", codProvincia);
	}
	
	
	/**
	 * Recupera el número total de usuarios de una lista de provincias.
	 * @param provinceList
	 * @return
	 */
	public Long countListByProvinceList (List<AbstractBaseDTO> provinceList) {
		Map<String, Object> parameters = new HashMap<String, Object> ();
		parameters.put("list", provinceList);
		return baseDAO.queryCount("administration.countUsersByProvinceList", parameters);
	}

	/*
	 * OPERATIONS
	 */

	/**
	 * M&eacute;todo que guarda en BD una lista de usuarios nuevos o editados.
	 * @param userList Listado de usuario a a&ntilde;adir/modificar en la BD.
	 * @param userDeleteList Listado de usuarios a eliminar de la BD.
	 * @param userProfileDeleteList Listado de perfiles de usuario a eliminar de la BD.
	 * @param userJobDeleteList Listado de cargos a eliminar de la BD.
	 * @param userEmailDeleteList Listado de correos electr&oacute;nicos a eliminar de la BD.
	 * @param userMobileDeleteList Listado de tel&eacute;fonos m&oacute;viles a eliminar de la BD.
	 * @param userParameterDeleteList Listado de par&aacute;metros de usuario a eliminar de la BD.
	 * @param passwordDeleteList Listado de passwords a eliminar de la BD.
	 * @param ldapIdDeleteList Listado de identificadores de LDAP a eliminar de la BD.
	 */
	@Transactional(readOnly = false)
	public void saveUserList(List<AbstractBaseDTO> userList,
			List<AbstractBaseDTO> userDeleteList,
			List<AbstractBaseDTO> userProfileDeleteList,
			List<AbstractBaseDTO> userJobDeleteList,
			List<AbstractBaseDTO> userProvinceDeleteList,
			List<AbstractBaseDTO> userEmailDeleteList,
			List<AbstractBaseDTO> userMobileDeleteList,
			List<AbstractBaseDTO> userParameterDeleteList,
			List<AbstractBaseDTO> passwordDeleteList,
			List<AbstractBaseDTO> ldapIdDeleteList) {
		for (AbstractBaseDTO user : userList) {
			if (user.isUpdated()) {
				user.setUpdated(false);
				// Save user associations
				Set<PfUsersProfileDTO> userProfileSet = ((PfUsersDTO) user).getPfUsersProfiles();
				Set<PfUsersProfileDTO> auxUserProfileSet = new HashSet<PfUsersProfileDTO>();
				auxUserProfileSet.addAll(userProfileSet);

				Set<PfProvinceAdminDTO> userProvinceSet = ((PfUsersDTO) user).getPfUsersProvinces();
				Set<PfProvinceAdminDTO> auxUserProvinceSet = new HashSet<PfProvinceAdminDTO>();
				auxUserProvinceSet.addAll(userProvinceSet);
				
				Set<PfUsersParameterDTO> userParameterSet = ((PfUsersDTO) user).getPfUsersParameters();
				Set<PfUsersParameterDTO> auxUserParameterSet = new HashSet<PfUsersParameterDTO>();
				auxUserParameterSet.addAll(userParameterSet);
				
				Set<PfUsersJobDTO> userJobSet = ((PfUsersDTO) user).getPfUsersJobs();
				Set<PfUsersJobDTO> auxUserJobSet = new HashSet<PfUsersJobDTO>();
				auxUserJobSet.addAll(userJobSet);

				Set<PfUsersEmailDTO> userEmailSet = ((PfUsersDTO) user).getPfUsersEmails();
				Set<PfUsersEmailDTO> auxUserEmailSet = new HashSet<PfUsersEmailDTO>();
				auxUserEmailSet.addAll(userEmailSet);
				
				Set<PfUsersDTO> validadoresSet = ((PfUsersDTO) user).getValidadores();
				Set<PfUsersDTO> auxValidadoresSet = new HashSet<PfUsersDTO>();
				auxValidadoresSet.addAll(validadoresSet);
				
				Set<PfUsersDTO> validadorDeSet = ((PfUsersDTO) user).getValidadorDe();
				Set<PfUsersDTO> auxValidadorDeSet = new HashSet<PfUsersDTO>();
				auxValidadorDeSet.addAll(validadorDeSet);

				// pass to upper case
				((PfUsersDTO) user).setCidentifier(((PfUsersDTO) user).getCidentifier().toUpperCase());

				// remove userJobs
				((PfUsersDTO) user).setPfUsersJobs(new HashSet<PfUsersJobDTO>());

				// remove userProfiles
				((PfUsersDTO) user).setPfUsersProfiles(new HashSet<PfUsersProfileDTO>());

				// remove userProvinces
				((PfUsersDTO) user).setPfUsersProvinces(new HashSet<PfProvinceAdminDTO>());

				// remove parameters
				((PfUsersDTO) user).setPfUsersParameters(new HashSet<PfUsersParameterDTO>());

				// remove emails
				((PfUsersDTO) user).setPfUsersEmails(new HashSet<PfUsersEmailDTO>());
				
				// remove validator of
				((PfUsersDTO) user).setValidadorDe(new HashSet<PfUsersDTO>());
				
				// remove validators
				((PfUsersDTO) user).setValidadores(new HashSet<PfUsersDTO>());

				// Save user
				baseDAO.insertOrUpdate(user);

				// add userJobs
				((PfUsersDTO) user).setPfUsersJobs(auxUserJobSet);

				// add userProfiles
				((PfUsersDTO) user).setPfUsersProfiles(auxUserProfileSet);

				// add userProvinces
				((PfUsersDTO) user).setPfUsersProvinces(auxUserProvinceSet);

				// add userParameters
				((PfUsersDTO) user).setPfUsersParameters(auxUserParameterSet);

				// add userEmails
				((PfUsersDTO) user).setPfUsersEmails(auxUserEmailSet);
				
				//add validators
				((PfUsersDTO) user).setValidadores(auxValidadoresSet);
				
				//add validator of
				((PfUsersDTO) user).setValidadorDe(auxValidadorDeSet);

				for (PfUsersProfileDTO usersProfiles : auxUserProfileSet) {
					// Save profiles
					baseDAO.insertOrUpdate(usersProfiles);
				}
				for (PfProvinceAdminDTO usersProvince : auxUserProvinceSet) {
					// Save provinces
					baseDAO.insertOrUpdate(usersProvince);
				}
				for (PfUsersParameterDTO usersParameters : auxUserParameterSet) {
					// Save pass
					baseDAO.insertOrUpdate(usersParameters);
				}
				for (PfUsersJobDTO usersJobs : auxUserJobSet) {
					// Save jobs
					baseDAO.insertOrUpdate(usersJobs);
				}
				for (PfUsersEmailDTO usersEmails : auxUserEmailSet) {
					// Save emails
					baseDAO.insertOrUpdate(usersEmails);
				}
				
				baseDAO.insertOrUpdate(user);
			}
		}
		baseDAO.deleteList(passwordDeleteList);
		baseDAO.deleteList(ldapIdDeleteList);
		baseDAO.deleteList(userEmailDeleteList);
		baseDAO.deleteList(userMobileDeleteList);
		baseDAO.deleteList(userParameterDeleteList);
		baseDAO.deleteList(userProfileDeleteList);
		baseDAO.deleteList(userProvinceDeleteList);
		baseDAO.deleteList(userJobDeleteList);
		baseDAO.deleteList(userDeleteList);
	}

	/*
	 * AUXILIARY METHODS
	 */

	/**
	 * M&eacute;todo que devuelve una lista con los perfiles de un usuario.
	 * @param Usuario del que se obtienen los perfiles.
	 * @return Listado de perfiles.
	 */
	public List<PfProfilesDTO> getUserProfileList(PfUsersDTO userEdit) {
		List<PfProfilesDTO> userProfileList = new ArrayList<PfProfilesDTO>();
		if (userEdit != null && userEdit.getPfUsersProfiles() != null) {
			Iterator<PfUsersProfileDTO> it = userEdit.getPfUsersProfiles().iterator();
			while (it.hasNext()) {
				PfUsersProfileDTO userProfile = it.next();
				userProfileList.add(userProfile.getPfProfile());
			}
		}
		return userProfileList;
	}

	/**
	 * M&eacute;todo que devuelev una lista con los cargos de un usuario.
	 * @param userEdit Usuario del que se obtienen los perfiles.
	 * @return Listado de cargos.
	 */
	public List<AbstractBaseDTO> getUserJobList(PfUsersDTO userEdit) {
		List<AbstractBaseDTO> userJobList = new ArrayList<AbstractBaseDTO>();
		if (userEdit != null && userEdit.getPfUsersJobs() != null) {
			Iterator<PfUsersJobDTO> it = userEdit.getPfUsersJobs().iterator();
			while (it.hasNext()) {
				PfUsersJobDTO userJob = it.next();
				userJobList.add(userJob);
			}
		}
		return userJobList;
	}
	
	
	/**
	 * Comprueba si alguno de los cargos de la lista está asociado a algún otro usuario
	 * @param userJobList lista de cargos
	 * @return true si alguno de los cargos de la lista está asociado a algún otro usuario, false en caso contrario
	 */
	public boolean isAnyJobAssigned (List<AbstractBaseDTO> userJobList) {
		boolean assigned = false;
		int i=0;
		while (i<userJobList.size() && !assigned) {
			if (isJobAssigned(userJobList.get(i), new Date())) {
				assigned = true;
			}
			i++;			
		}
		
		return assigned;
	}
	
	/**
	 * Comprueba si el cargo pasado como parámetro está asociado a algún usuario
	 * @param baseUserJob cargo del que se quiere saber si está asociado.
	 * @return true si el cargo está asociado a algún usuario, false en caso contrario.
	 */
	public boolean isJobAssigned (AbstractBaseDTO baseUserJob, Date fendNewJob) {
		PfUsersJobDTO userJob = (PfUsersJobDTO) baseUserJob;
		// se recuperan los usuarios asociados a un cargo.
		List<AbstractBaseDTO> baseList = baseDAO.queryListOneParameter(
				"administration.jobUserAdmAssociated",
				"job", userJob.getPfUserJob());
		
		boolean nowValid = false;
		int i=0;
		
		// Si alguno de los usuarios es distinto, y las fechas de vigencia se solapan, el cargo ya está asociado a otro usuario.
		while (i<baseList.size() && !nowValid) {
			PfUsersJobDTO userJobAssigned = (PfUsersJobDTO) baseList.get(i);
			if (!userJob.getPfUser().getPrimaryKeyString().equals(userJobAssigned.getPfUser().getPrimaryKeyString()) &&
				//(userJobAssigned.getFend() == null || userJobAssigned.getFend().after(new Date()))
					(userJobAssigned.getFend() == null || userJobAssigned.getFend().after(fendNewJob))
				) {
				nowValid = true;
			}
			i++;
		}		
		
		return nowValid;
	}

	/**
	 * M&eacute;todo que devuelve el listado de perfiles que el usuario puede seleccionar.
	 * @param profileDefaultList Listado de perfiles por defecto.
	 * @param user Usuario que elige los perfiles.
	 * @return Listado de perfiles seleccionables.
	 */
	public List<PfProfilesDTO> loadSelectableProfiles(
			List<PfProfilesDTO> profileDefaultList, PfUsersDTO user) {
		// Remove from list profiles associated to user
		List<PfProfilesDTO> returnList = new ArrayList<PfProfilesDTO>();
		List<PfProfilesDTO> auxList = new ArrayList<PfProfilesDTO>();
		Boolean isAdmin = false;
		returnList.addAll(profileDefaultList);
		auxList.addAll(profileDefaultList);
		if (user.getPfUsersProfiles() != null
				&& !user.getPfUsersProfiles().isEmpty()) {
			for (PfUsersProfileDTO userProfile : user.getPfUsersProfiles()) {
				for (PfProfilesDTO profileDTO : auxList) {
					if (userProfile.getPfProfile().getCprofile().equals(profileDTO.getCprofile())) {
						returnList.remove(profileDTO);
					}
					if (userProfile.getPfProfile().getCprofile().equals(Constants.C_PROFILES_ADMIN)) {
						isAdmin = true;
					}
				}
			}
		}

		// Si el usuario es administrador, no puede recibir el perfil de administrador de provincia
		if (isAdmin) {
			List<PfProfilesDTO> auxReturnList = new ArrayList<PfProfilesDTO>();
			auxReturnList.addAll(returnList);
			for (PfProfilesDTO profile : auxReturnList) {
				if (profile.getCprofile().equals(Constants.C_PROFILES_ADMIN_PROVINCE)) {
					returnList.remove(profile);
				}
			}
		}

		return returnList;
	}

	/**
	 * M&eacute;todo que convierte un listado de perfiles de usuario en un conjunto.
	 * @param userProfileList Listado de perfiles a convertir.
	 * @return Conjunto de perfiles de usuario.
	 */
	public Set<PfUsersProfileDTO> userProfileList2Set(List<AbstractBaseDTO> userProfileList) {
		Set<PfUsersProfileDTO> returnSet = new HashSet<PfUsersProfileDTO>();
		Iterator<AbstractBaseDTO> it = userProfileList.iterator();
		while (it.hasNext()) {
			// Set to false "selected" property to avoid edit new profiles
			PfUsersProfileDTO profile = (PfUsersProfileDTO) it.next();
			profile.setSelected(false);
			returnSet.add(profile);
		}
		return returnSet;
	}

	/**
	 * M&eacute;todo que convierte un listado de perfiles de usuario en un conjunto.
	 * @param userProfileList Listado de perfiles a convertir.
	 * @return Conjunto de perfiles de usuario.
	 */
	public Set<PfProvinceAdminDTO> userProvinceList2Set(List<AbstractBaseDTO> userProvinceList) {
		Set<PfProvinceAdminDTO> returnSet = new HashSet<PfProvinceAdminDTO>();
		Iterator<AbstractBaseDTO> it = userProvinceList.iterator();
		while (it.hasNext()) {
			// Set to false "selected" property to avoid edit new provinces
			PfProvinceAdminDTO province = (PfProvinceAdminDTO) it.next();
			province.setSelected(false);
			returnSet.add(province);
		}
		return returnSet;
	}

	/**
	 * M&eacute;todo que convierte un listado de cargos de usuario en un conjunto.
	 * @param userJobList Listado de cargos a convertir.
	 * @return Conjunto de cargos de usuario.
	 */
	public Set<PfUsersJobDTO> userJobList2Set(List<AbstractBaseDTO> userJobList) {
		Set<PfUsersJobDTO> returnSet = new HashSet<PfUsersJobDTO>();
		Iterator<AbstractBaseDTO> it = userJobList.iterator();
		while (it.hasNext()) {
			PfUsersJobDTO userJob = (PfUsersJobDTO) it.next();
			returnSet.add(userJob);
		}
		return returnSet;
	}

	

	/*
	 * VALIDATIONS
	 */

	/**
	 * M&eacute;todo que comprueba si un usuario est&aacute; asociado a una petici&oacute;n.
	 * La asociaci&oacute;n puede ser en forma de remitente, firmante, etiquetas, etc.
	 * @param user Usuario a comprobar.
	 * @return Mensaje de texto con la informaci&oacute;n de las asociaciones del usuario. Si no hay asociaciones se devuelve la cadena vac&iacute;a.
	 */
	/*public String requestAssociated(PfUsersDTO user) {
		String message = "";
		PfUsersDTO userAux = null;
		// Check only if user exists in DB
		if (user != null && user.getPrimaryKey() != null) {
			Map<String, Object> queryParams = new HashMap<String, Object>();
			queryParams.put("paramUser", user);
			userAux = (PfUsersDTO) baseDAO.queryElementMoreParameters(
					"administration.userAssociatedToRequest", queryParams);
		}
		if (userAux != null) {
			// check that user isn't signer
			if (userAux.getPfSigners() != null
					&& !userAux.getPfSigners().isEmpty()) {
				message = messages.getProperty("errorUserIsSigner");
			}
			// check that user isn't remitter
			else if (userAux.getPfUsersRemitters() != null
					&& !userAux.getPfUsersRemitters().isEmpty()) {
				message = messages.getProperty("errorUserIsRemitter");
			}
			// check that user doesn't have request tags
			else if (userAux.getPfTagsUsers() != null
					&& !userAux.getPfTagsUsers().isEmpty()) {
				message = errorUserHasRequestTags;
			}
			// check that user hasn't comments
			else if (userAux.getPfComments() != null
					&& !userAux.getPfComments().isEmpty()) {
				message = errorUserHasComments;
			}
		}
		return message;
	}*/
	
	
	/**
	 * M&eacute;todo que comprueba si un usuario est&aacute; asociado a una petici&oacute;n.
	 * La asociaci&oacute;n puede ser en forma de remitente, firmante, etiquetas, etc.
	 * @param user Usuario a comprobar.
	 * @return Mensaje de texto con la informaci&oacute;n de las asociaciones del usuario. Si no hay asociaciones se devuelve la cadena vac&iacute;a.
	 */
	public ArrayList<String> requestAssociated(PfUsersDTO user) {
		ArrayList<String> warnings = new ArrayList<String>();
		PfUsersDTO userAux = null;
		List<AbstractBaseDTO> listAux = null;
		// Check only if user exists in DB
		if (user != null && user.getPrimaryKey() != null) {
			Map<String, Object> queryParams = new HashMap<String, Object>();
			queryParams.put("paramUser", user);

			// Comprobación de que el usuario no sea firmante de ninguna peticion
			userAux = (PfUsersDTO) baseDAO.queryElementMoreParameters("administration.userAssociatedToSigners", queryParams);
			if (userAux != null && userAux.getPfSigners() != null
					&& !userAux.getPfSigners().isEmpty()) {
				warnings.add(messages.getProperty("errorUserIsSigner"));
			}

			// Comprobación de que el usuario no sea remitente de ninguna petición
			userAux = (PfUsersDTO) baseDAO.queryElementMoreParameters("administration.userAssociatedToRemitters", queryParams);
			if (userAux != null && userAux.getPfUsersRemitters() != null
					&& !userAux.getPfUsersRemitters().isEmpty()) {
				warnings.add(messages.getProperty("errorUserIsRemitter"));
			}

			// Comprobación de que el usuario no haya escrito comentarios en peticiones
			userAux = (PfUsersDTO) baseDAO.queryElementMoreParameters("administration.userAssociatedToComments", queryParams);
			if (userAux != null && userAux.getPfComments()!= null
					&& !userAux.getPfComments().isEmpty()) {
				warnings.add(messages.getProperty("errorUserHasComments"));
			}				

			userAux =  (PfUsersDTO) baseDAO.queryElementMoreParameters("administration.userAssociatedToValidators", queryParams);
			if (userAux != null && userAux.getValidadorDe()!= null
					&& !userAux.getValidadorDe().isEmpty()) {
				warnings.add(messages.getProperty("errorUserHasValidators"));
			}

			if (userAux != null && userAux.getValidadores() != null
					&& !userAux.getValidadores().isEmpty()) {
				warnings.add(messages.getProperty("errorUserHasValidators"));
			}

			// Comprobación de que el usuario no tenga etiquetas peticion
			listAux = baseDAO.queryListMoreParameters("administration.userAssociatedToRequestsTags", queryParams);
			if (listAux != null && listAux.size() > 0) {
				warnings.add(messages.getProperty("errorUserHasRequestTags"));
			}				

			listAux =  baseDAO.queryListMoreParameters("administration.userAssociatedToAuthorizations", queryParams);
			if (listAux != null && listAux.size() > 0) {
				warnings.add(messages.getProperty("errorUserHasAuth"));
			}
			
			listAux =  baseDAO.queryListMoreParameters("administration.userAssociatedToJob", queryParams);
			if (listAux != null && listAux.size() > 0) {
				warnings.add(messages.getProperty("errorUserJobAssociated"));
			}
			
			listAux = baseDAO.queryListMoreParameters("administration.userAssociatedToGroup", queryParams);
			if (listAux != null && listAux.size() > 0) {
				warnings.add(messages.getProperty("errorUserGroupAssociated"));
			}
		}
		return warnings;
	}

	/**
	 * M&eacute;todo que busca un usuario dentro de una lista de usuarios.
	 * @param userList Listado de usuarios.
	 * @param user Usuario a buscar en la lista.
	 * @param idEdit Identificador del usuario a buscar en caso de que est&eacute; en proceso de edici&oacute;n.
	 * @param newUser "True" si el usuario a buscar es  nuevo en el sistema. "False" en caso contrario.
	 * @return Devuelve "True" en caso de existir el usuario, "False" en caso contrario.
	 */
	public boolean userIdentifierExists(List<AbstractBaseDTO> userList,
			PfUsersDTO user, String idEdit, boolean newUser) {
		boolean ret = false;
		PfUsersDTO auxUser;
		if (userList != null && !userList.isEmpty()) {
			Iterator<AbstractBaseDTO> it = userList.iterator();
			while (it.hasNext() && ret == false) {
				auxUser = (PfUsersDTO) it.next();
				// Not the same row and different code
				if (auxUser.getCidentifier().toUpperCase().equals(
						user.getCidentifier().toUpperCase())
						&& !user.equals(auxUser)) {
					if (newUser) {
						ret = true;
					} else if (!user.getCidentifier().toUpperCase().equals(idEdit.toUpperCase())) {
						ret = true;
					}
				}
			}
		}
		return ret;
	}

	/**
	 * Método que comprueba si el identificador del usuario nuevo existe en base de datos
	 * @param idEdit Identificador de usuario
	 * @return True si está repetido, False en caso contrario
	 */
	public String userIdentifierExistsInDB(String idEdit) {
		String pk = null;
		AbstractBaseDTO user = baseDAO.queryElementOneParameter("request.identifierExists", "id", idEdit.toUpperCase().trim());
		if (user != null) {
			pk = user.getPrimaryKeyString();
		}
		return pk;
	}

	/**
	 * M&eacute;todo que indica si hay alg&uacute;n cargo v&aacute;lido dentro de una lista de cargos.
	 * @param userJobList Listado de cargos.
	 * @return "True" si existe alg&uacute;n cargo v&aacute;lido. En cualquier otro caso "false".
	 */
	public boolean existValidUserJob(List<AbstractBaseDTO> userJobList) {
		boolean flag = false;
		// Check that doesn't exist any valid job
		if (userJobList != null && !userJobList.isEmpty()) {
			for (AbstractBaseDTO abstractBaseDTO : userJobList) {
				PfUsersJobDTO userJob = (PfUsersJobDTO) abstractBaseDTO;
				if (userJob.getFend() == null || userJob.getFend().after(new Date())) {
					flag = true;
				}
			}
		}
		return flag;
	}

	/**
	 * M&eacute;todo que calcula el n&uacute;mero de cargos de usuario validos contenidos en una lista de cargos.
	 * @param userJobList Listado de cargos de usuario.
	 * @return N&uacute;mero de cargos de usuario validos.
	 */
	public int countValidUserJobs(List<AbstractBaseDTO> userJobList) {
		int ret = 0;
		if (userJobList != null && !userJobList.isEmpty()) {
			for (AbstractBaseDTO abstractBaseDTO : userJobList) {
				PfUsersJobDTO userJob = (PfUsersJobDTO) abstractBaseDTO;
				if (userJob.getFend() == null ||
					userJob.getFend().after(new Date())) {
					ret++;
				}
			}
		}
		return ret;
	}

	/**
	 * M&eacute;todo que comprueba si el nombre completo de un usuario tiene el mismo nombre completo que alg&uacute;n usuario de una lista.
	 * @param userList Listado de usuarios.
	 * @param userEdit Usuario con el nombre a buscar.
	 * @return "True" si alg&uacute;n usuario de la lista tiene el mismo nombre, "false" en caso contrario.
	 */
	public boolean userNameSurnameExists(List<AbstractBaseDTO> userList, PfUsersDTO userEdit) {
		boolean result = false;
		for (Iterator<AbstractBaseDTO> iterator = userList.iterator(); iterator.hasNext();) {
			PfUsersDTO aux = (PfUsersDTO) iterator.next();

			String nameUserEdit = "";
			if (userEdit.getDname() != null) {
				nameUserEdit += userEdit.getDname().trim().toUpperCase();
			}
			if (userEdit.getDsurname1() != null) {
				nameUserEdit += " "	+ userEdit.getDsurname1().trim().toUpperCase();
			}
			if (userEdit.getDsurname2() != null) {
				nameUserEdit += " "	+ userEdit.getDsurname2().trim().toUpperCase();
			}
			nameUserEdit = nameUserEdit.trim();

			String nameAux = "";
			if (aux.getDname() != null) {
				nameAux += aux.getDname().trim().toUpperCase();
			}
			if (aux.getDsurname1() != null) {
				nameAux += " " + aux.getDsurname1().trim().toUpperCase();
			}
			if (aux.getDsurname2() != null) {
				nameAux += " " + aux.getDsurname2().trim().toUpperCase();
			}
			nameAux = nameAux.trim();
			if (!aux.getPrimaryKeyString().equals(
					userEdit.getPrimaryKeyString())
					&& nameUserEdit.equals(nameAux)) {
				result = true;
			}
		}
		return result;
	}

	/**
	 * M&eacute;todo que actualiza la informaci&oacute;n del usuario actual desde la base de datos.
	 * @param userList Listado de usuarios.
	 * @param currentUser Usuario actual.
	 * @return El usuario actual actualizado.
	 */
	public PfUsersDTO updateCurrentUser(List<AbstractBaseDTO> userList,	PfUsersDTO currentUser) {
		if (currentUser.isUpdated()) {
			for (Iterator<AbstractBaseDTO> iterator = userList.iterator(); iterator.hasNext();) {
				PfUsersDTO user = (PfUsersDTO) iterator.next();
				if (user.getPrimaryKeyString().equals(currentUser.getPrimaryKeyString())) {
					return (PfUsersDTO) baseDAO.queryElementOneParameter(
							"administration.userByPK", "pk", user.getPrimaryKey());
				}
			}
		}
		return null;
	}
	
	/**
	 * Obtiene el usuario a partir de su PK
	 * @param primaryKey
	 * @return
	 */
	public PfUsersDTO getUserByPK(Long primaryKey) {
		return (PfUsersDTO) baseDAO.queryElementOneParameter(
							"administration.userByPK", "pk", primaryKey);
	}

	public PfUsersDTO getUserByDni(String dni) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("dni", dni);
		parametros.put("tipo", Constants.C_TYPE_USER_USER);
		return (PfUsersDTO) baseDAO.queryElementMoreParameters("request.userProfilesDni", parametros );
	}

	/**
	 * M&eacute;todo que obtiene un usuario de la BD a partir de su DNI y Password.
	 * @param id Identificador de usuario (DNI).
	 * @param password Password del usuario.
	 * @return El usuario que cumple las condiciones.
	 */
	public PfUsersDTO loadUserIdPass(String id, String password) {
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("dni", id.toUpperCase());
		parameters.put("password", password);

		PfUsersDTO userDTO = (PfUsersDTO) baseDAO.queryElementMoreParametersWithOutLog(
						"request.userProfilesDniPass", parameters);
		return userDTO;
	}

	/**
	 * M&eacute;todo que comprueba si las fechas de inicio de una lista de perfiles son inferiores a sus respectivas fechas de fin.
	 * @param profiles Listado de perfiles de usuario.
	 * @return "True" si todas las fechas son v&aacute;lidas, "false" en caso contrario.
	 */
	public boolean checkProfilesDates(Set<PfUsersProfileDTO> profiles) {
		boolean success = true;
		for (PfUsersProfileDTO usersProfileDTO : profiles) {
			if (usersProfileDTO != null
					&& !Util.getInstance().checkDate(
							usersProfileDTO.getFstart(),
							usersProfileDTO.getFend())) {
				success = false;
			}
		}
		return success;
	}

	/**
	 * M&eacute;todo que comprueba si las fechas de inicio de una lista de cargos son inferiores a sus respectivas fechas de fin.
	 * @param jobs Listado de cargos de usuario.
	 * @return "True" si todas las fechas son v&aacute;lidas, "false" en caso contrario.
	 */
	public boolean checkJobsDates(Set<PfUsersJobDTO> jobs) {
		boolean success = true;
		for (PfUsersJobDTO userJobsDTO : jobs) {
			if (userJobsDTO != null
					&& !Util.getInstance().checkDate(userJobsDTO.getFstart(),
							userJobsDTO.getFend())) {
				success = false;
			}
		}
		return success;
	}

	/**
	 * Comprueba si el perfil de administrador de provincia existe en los perfiles del usuario
	 * @param provinces Lista de provincias
	 * @return True si existe el perfil de administrador de provincias
	 */
	public boolean isAdminSeat(Set<PfUsersProfileDTO> profiles) {
		Boolean adminProv = false;
		for (PfUsersProfileDTO profile : profiles) {
			if (profile.getPfProfile().getCprofile().equals(Constants.C_PROFILES_ADMIN_PROVINCE)
					|| profile.getPfProfile().getCprofile().equals(Constants.C_PROFILES_ADMIN_ORGANISM)) {
				adminProv = true;
			}
		}
		return adminProv;
	}
	
	/**
	 * Comprueba si el perfil de administrador de CAID existe en los perfiles del usuario
	 * @return True si existe el perfil de administrador de CAID
	 */
	public boolean isAdminCAID(Set<PfUsersProfileDTO> profiles) {
		Boolean adminCaid = false;
		for (PfUsersProfileDTO profile : profiles) {
			if (profile.getPfProfile().getCprofile().equals(Constants.C_PROFILES_ADMIN_CAID)) {
				adminCaid = true;
			}
		}
		return adminCaid;
	}
	
	/**
	 * Comprueba si el perfil de administrador existe en los perfiles del usuario 
	 * @param profiles
	 * @return
	 */
	public boolean isAdministrator(Set<PfUsersProfileDTO> profiles) {
		Boolean adminProv = false;
		for (PfUsersProfileDTO profile : profiles) {
			if (profile.getPfProfile().getCprofile().equals(Constants.C_PROFILES_ADMIN)) {
				adminProv = true;
			}
		}
		return adminProv;
	}
	
	/**
	 * Comprueba si el perfil de administrador de organismo existe en los perfiles del usuario 
	 * @param profiles
	 * @return
	 */
	public boolean isOrganismAdministrator(Set<PfUsersProfileDTO> profiles) {
		Boolean adminOrg = false;
		for (PfUsersProfileDTO profile : profiles) {
			if (profile.getPfProfile().getCprofile().equals(Constants.C_PROFILES_ADMIN_ORGANISM)) {
				adminOrg = true;
			}
		}
		return adminOrg;
	}
	
	@Transactional(readOnly = false)
	public void updateVisibility (List<UserEnvelope> userList, boolean visible) {
		for (UserEnvelope userEnv : userList) {
			updateVisibility(userEnv, visible);
		}
	}

	@Transactional(readOnly = false)
	public void updateVisibility (UserEnvelope userEnv, boolean visible) {
		PfUsersDTO userDTO = (PfUsersDTO) baseDAO.queryElementOneParameter("request.usersAndJobWithProvincePk", "pk", userEnv.getPk());
		userDTO.setLvisible(visible);
		baseDAO.insertOrUpdate(userDTO);
	}
	
	@Transactional(readOnly = false)
	public void updateLNotifyPush (PfUsersDTO pfUser, boolean estadoNotifyPush) {
		pfUser.setLNotifyPush(estadoNotifyPush);
		saveUser(pfUser);
	}

	//@Transactional(readOnly = false)
	public PfUsersDTO queryUsersByPk (Long pk) {
		return (PfUsersDTO) baseDAO.queryElementOneParameter("configuration.userPk", "pk", pk);
	}
	
	//@Transactional(readOnly = false)
	public PfUsersDTO queryUsersByIdentifier (String dni) {
		return (PfUsersDTO) baseDAO.queryElementOneParameter("request.usersDni", "dni", dni);
	}
	
	/**
	 * Recupera los usuarios que se ajustan a los parametros recibidos
	 * @param usersParameters
	 * @return
	 */
	public List<AbstractBaseDTO> queryUsersComplete(UsersParameters usersParameters) {
		List<AbstractBaseDTO> usersList = userDAO.queryUserListPaginated(usersParameters);
		removeInvalidJob(usersList);
		return usersList;
	}
	
	/**
	 * Recupera los cargos que se ajustan a los parametros recibidos
	 * @param usersParameters
	 * @return
	 */
	public List<AbstractBaseDTO> queryJobsComplete(UsersParameters usersParameters) {
		usersParameters.getType().add(Constants.C_TYPE_USER_JOB);
		List<AbstractBaseDTO> usersList = userDAO.queryUserListPaginated(usersParameters);
		return usersList;
	}
	
	public List<AbstractBaseDTO> queryUsersByNameByProvince (List<AbstractBaseDTO> provinceList, String find) {
		Map<String, Object> queryParams = new HashMap<String, Object>();
		queryParams.put("find", "%" + find.toUpperCase() + "%");
		queryParams.put("provinceList", provinceList);
		return baseDAO.queryListMoreParameters("administration.usersByProvinces", queryParams);
	}
	
	/**
	 * Elimina los cargos que no están en vigor para cada uno de los usuarios de la lista
	 * @param usersList
	 */
	public void removeInvalidJob(List<AbstractBaseDTO> usersList) {
		// se recorre la lista
		for (Iterator<AbstractBaseDTO> iterator = usersList.iterator(); iterator.hasNext();) {
			PfUsersDTO pfUserDTO = (PfUsersDTO) iterator.next();
			Set<PfUsersJobDTO> usersJobs = pfUserDTO.getPfUsersJobs();
			PfUsersJobDTO validUserJob = null;
			for (Iterator<PfUsersJobDTO> iteratorUserJob = usersJobs.iterator(); iteratorUserJob.hasNext();) {
				PfUsersJobDTO pfUsersJobDTO = iteratorUserJob.next();
				if(Util.getInstance().isValidJob(pfUsersJobDTO)) {
					validUserJob = pfUsersJobDTO;
				}
			}
			usersJobs.clear();
			if(validUserJob != null) {
				usersJobs.add(validUserJob);
			}
		}
	}


	/**
	 * Devuelve un usuario que coincide con el identificador recibido
	 * @param pfUsersDTO
	 * @return
	 */
	@Transactional(readOnly=true)
	private PfUsersDTO getUserByIdentifier(PfUsersDTO pfUsersDTO) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("cidentifier", pfUsersDTO.getCidentifier());
		return (PfUsersDTO) baseDAO.queryElementMoreParameters("administration.userByNif", parameters);
	}

	/**
	 * Devuelve un usuario que coincide con el LDAP recibido
	 * @param user
	 * @return
	 */
	@Transactional(readOnly=true)
	public PfUsersDTO getUserByLDAP (String ldap) {
		HashMap<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ldapId", ldap);
		return (PfUsersDTO) baseDAO.queryElementMoreParametersWithOutLog("request.userProfilesLdapId", parameters);
	}

	/**
	 * Valida que se cumplan las reglas de negocio establecidas
	 * @param pfUsersDTO
	 * @param errors
	 */
	@Transactional(readOnly=true)
	public void validateUser(PfUsersDTO pfUsersDTO, ArrayList<String> errors) {
		PfUsersDTO pfUsersDTO_BD = getUserByIdentifier(pfUsersDTO);
		// Si el identificador ya existe en base de datos..
		if(pfUsersDTO_BD != null) {
			// .. se avisa, si es una alta o, siendo una modificación, el encontrado NO es el que estamos modificando.
			if(pfUsersDTO.getPrimaryKey() == null 
					|| !pfUsersDTO.getPrimaryKey().equals(pfUsersDTO_BD.getPrimaryKey())) {
				errors.add(messages.getProperty("user.nif.repeat"));
			}
		}
	}

	/**
	 * Valida que el LDAP no se encuentre ya dado de Alta en Portafirmas
	 * @param pfUsersDTO	 
	 * @param errors
	 * @param user
	 */
	@Transactional(readOnly=true)
	public void validateLDAP (PfUsersDTO pfUsersDTO, ArrayList<String> errors, User user) {
		if (!Util.esVacioONulo(user.getLdapId())){
			PfUsersDTO pfUsersDTO_BD = getUserByLDAP(user.getLdapId());
			// Si el LDAP ya existe en base de datos.
			if(pfUsersDTO_BD != null) {
				if(pfUsersDTO.getPrimaryKey() == null || !pfUsersDTO.getPrimaryKey().equals(pfUsersDTO_BD.getPrimaryKey()))
					errors.add(messages.getProperty("user.ldap.repeat"));
			}
		}
	}

	/**
	 * Se persiste el usuario, sus perfiles y los parametros clave y contraseña.
	 * @param pfUsersDTO
	 * @param userParameterList 
	 * @param provinceAdminList 
	 */
	@Transactional(readOnly = false)
	public void saveUser(UserAuthentication authorization, PfUsersDTO pfUsersDTO,
			List<PfUsersProfileDTO> userProfileList,
			List<PfUsersParameterDTO> userParameterList,
			List<PfProvinceAdminDTO> provinceAdminList,
			List<PfUsersEmailDTO> userMailList) {
		// Se guarda el usuario
		pfUsersDTO.setCidentifier(pfUsersDTO.getCidentifier().toUpperCase().trim());//Obligamos a que el identificador valla en mayusculas y sin espacios
		baseDAO.insertOrUpdate(pfUsersDTO);

		// Se borran las relaciones antiguas
		if(authorization != null) {
			deleteSet(obtenerPerfilesBorrables(authorization, pfUsersDTO.getPfUsersProfiles()));
		}
		deleteSet(pfUsersDTO.getPfUsersParameters());
		deleteSet(pfUsersDTO.getPfUsersProvinces());
		deleteSet(pfUsersDTO.getPfUsersEmails());

		// Se guardan las nuevas relaciones usuario-perfil
			if(userProfileList != null && !userProfileList.isEmpty()){
			for (Iterator<PfUsersProfileDTO> it = userProfileList.iterator(); it.hasNext();) {
				PfUsersProfileDTO pfUsersProfileDTO =  it.next();
				pfUsersProfileDTO.setPfUser(pfUsersDTO);
				baseDAO.insertOrUpdate(pfUsersProfileDTO);
			}
		}

		// Se guardan las nuevas relaciones usuario-parametro
		if(userParameterList != null && !userParameterList.isEmpty()){
			for (Iterator<PfUsersParameterDTO> it = userParameterList.iterator(); it.hasNext();) {
				PfUsersParameterDTO pfUsersParameterDTO =  it.next();
				if (!Util.esVacioONulo(pfUsersParameterDTO.getTvalue())) {
					pfUsersParameterDTO.setPfUser(pfUsersDTO);
					baseDAO.insertOrUpdate(pfUsersParameterDTO);
				}
			}
		}

		// Se guardan las nuevas relaciones administradores-sedes
		if(userProfileList != null && !userProfileList.isEmpty() && provinceAdminList != null && !provinceAdminList.isEmpty()){
			if(isAdminSeat(new HashSet<PfUsersProfileDTO>(userProfileList))) {
				for (Iterator<PfProvinceAdminDTO> it = provinceAdminList.iterator(); it.hasNext();) {
					PfProvinceAdminDTO pfProvinceAdminDTO = it.next();
					pfProvinceAdminDTO.setPfUser(pfUsersDTO);
					baseDAO.insertOrUpdate(pfProvinceAdminDTO);
				}
			}
		}
		
		//Se guardan las nuevas relaciones usuario-emails
		if(userMailList != null && !userMailList.isEmpty()){
			for(Iterator<PfUsersEmailDTO> it = userMailList.iterator();it.hasNext();){
				PfUsersEmailDTO userMail = it.next();
				userMail.setPfUser(pfUsersDTO);
				baseDAO.insertOrUpdate(userMail);
			}
		}
	}

	
	private Set<PfUsersProfileDTO> obtenerPerfilesBorrables(UserAuthentication authorization, Set<PfUsersProfileDTO> set) {
		Set<PfUsersProfileDTO> perfilesBorrables = new HashSet<PfUsersProfileDTO>();
		for (PfUsersProfileDTO perfilBorrableDTO : set) {
			String codigoPerfil = perfilBorrableDTO.getPfProfile().getCprofile();
			// Si es perfil administrador puede borrar cualquier perfil salvo simular
			// Si es perfil administradorCAID puede borrar cualquier perfil salvo simular y ADMIN
			// y si NO es perfil administrador ni administradorCAID, puede borrar cualquier perfil distinto del ADMIN, admincaid, simular y del WEBSERVICE
			if((authorization.isAdministrator() && !Constants.C_PROFILES_SIMULATE.equals(codigoPerfil)) 
					|| (authorization.isAdministratorCAID() && (!Constants.C_PROFILES_ADMIN.equals(codigoPerfil)
							&& !Constants.C_PROFILES_SIMULATE.equals(codigoPerfil)))
					|| (!Constants.C_PROFILES_ADMIN.equals(codigoPerfil)
							&& !Constants.C_PROFILES_WEBSERVICE.equals(codigoPerfil)
							&& !Constants.C_PROFILES_ADMIN_CAID.equals(codigoPerfil)
							&& !Constants.C_PROFILES_SIMULATE.equals(codigoPerfil))) {
				perfilesBorrables.add(perfilBorrableDTO);
			}
		}
		return perfilesBorrables;
	}

	@Transactional(readOnly = false)
	public void saveUser(PfUsersDTO pfUsersDTO) {
		baseDAO.insertOrUpdate(pfUsersDTO);
	}
	
	@Transactional(readOnly = false)
	public void saveInvitedUser(PfInvitedUsersDTO pfInvitedUsersDTO) {
		baseDAO.insertOrUpdate(pfInvitedUsersDTO);
	}
	
	
	@Transactional(readOnly = false)
	public void deleteUser(PfUsersDTO userDTO) {
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
	}
	
	@Transactional(readOnly = false)
	public void revokeUser(PfUsersDTO userDTO) {
		userDTO.setLvalid(false);
		baseDAO.insertOrUpdate(userDTO);
	}

	private void deleteSet (Set<?> set) {
		Iterator<?> it = set.iterator();
		while (it.hasNext()) {
			baseDAO.delete((AbstractBaseDTO) it.next());
		}
	}

	/**
	 * Validaciones de las reglas de negocio para la relación cargos-usuario
	 * @param pfUsersJobDTO
	 * @param warnings
	 */
	public void validateUserJob(PfUsersJobDTO pfUsersJobDTO, ArrayList<String> warnings) {
		// Las validaciones se realizan en memoria, con la lista de cargos-usuario
		List<AbstractBaseDTO> userJobList = getUserJobList(pfUsersJobDTO.getPfUser());
		
		// Si es una modificación, se quita el cargo que se está modificando,
		// para añadirlo después con las modificaciones que llegan
		if(!Util.esVacioONulo(pfUsersJobDTO.getPrimaryKeyString())) {
			userJobList.remove(pfUsersJobDTO);
		}
		// Tanto para el alta como para la modificación, se añade la relación a la lista
		userJobList.add(pfUsersJobDTO);
		
		if (countValidUserJobs(userJobList) > 1) {
			warnings.add(messages.getProperty("errorMoreThanOneValidUserJob"));
		}
//		if (isAnyJobAssigned(userJobList)) { 
//			warnings.add(messages.getProperty("errorJobAssociatedOtherUser"));
//		}
	}

	@Transactional(readOnly = false)
	public PfUsersJobDTO queryUserJobByPk(Long primaryKey) {
		return (PfUsersJobDTO) baseDAO.queryElementOneParameter("administration.userJobByPk", "primaryKey", primaryKey);
	}

	@Transactional(readOnly = false)
	public void saveUserJob(PfUsersJobDTO pfUsersJobDTO) {
		baseDAO.insertOrUpdate(pfUsersJobDTO);
	}

	@Transactional(readOnly = false)
	public void deleteUserJob(PfUsersJobDTO pfUsersJobDTO) {
		baseDAO.delete(pfUsersJobDTO);
	}

	/**
	 * Obtiene la lista de relaciones cargos-usuario ordenadas por fecha de inicio
	 * @param pfUsersDTO
	 * @return
	 */
	public ArrayList<PfUsersJobDTO> getPfUsersJobs(PfUsersDTO pfUsersDTO) {
		Set<PfUsersJobDTO> pfUsersJobs = pfUsersDTO.getPfUsersJobs();
		ArrayList<PfUsersJobDTO> usersJobList = new ArrayList<PfUsersJobDTO>();
		usersJobList.addAll(pfUsersJobs);
		Collections.sort(usersJobList, new UserJobStarDateComparator());
		return usersJobList;
	}

	public List<AdminSeat> getAdminSeatList(List<AbstractBaseDTO> seatListDTO) {
		return getAdminSeatList(null, seatListDTO);
	}

	/**
	 * Obtiene la lista de sedes y marca las que tiene asignadas el usuario
	 * @param pfUsersDTO
	 * @param seatListDTO
	 * @return
	 */
	public List<AdminSeat> getAdminSeatList(PfUsersDTO pfUsersDTO, List<AbstractBaseDTO> seatListDTO) {
		ArrayList<AdminSeat> adminSeatList = new ArrayList<AdminSeat>();
		for (Iterator<AbstractBaseDTO> it = seatListDTO.iterator(); it.hasNext();) {
			PfProvinceDTO pfProvinceDTO = (PfProvinceDTO) it.next();
			AdminSeat adminSeat = new AdminSeat();
			adminSeat.setId(pfProvinceDTO.getPrimaryKeyString());
			adminSeat.setName(pfProvinceDTO.getCnombre());
			if(pfUsersDTO != null && listContainsProvince(pfUsersDTO.getPfUsersProvinces(), pfProvinceDTO)) {
				adminSeat.setChecked(true);
			}
			adminSeatList.add(adminSeat);
		}
		return adminSeatList;
	}

	/**
	 * Devuelve verdadero si la provincia está entre las relaciones administrador-sede del usuario
	 * @param pfUsersProvinces
	 * @param pfProvinceDTO
	 * @return
	 */
	private boolean listContainsProvince(Set<PfProvinceAdminDTO> pfUsersProvinces, PfProvinceDTO pfProvinceDTO) {
		for (Iterator<PfProvinceAdminDTO> it = pfUsersProvinces.iterator(); it.hasNext();) {
			PfProvinceAdminDTO pfProvinceAdminDTO = (PfProvinceAdminDTO) it.next();
			if(pfProvinceDTO.getPrimaryKey().equals(pfProvinceAdminDTO.getPfProvince().getPrimaryKey())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Devuelve verdadero si el organismo está entre las relaciones administrador-organismo del usuario
	 * @param pfUsersProvinces
	 * @param pfProvinceDTO
	 * @return
	 */
	private boolean listContainsOrganism(List<PfUnidadOrganizacionalDTO> pfUsersOrganismList, PfUnidadOrganizacionalDTO pfUnidadOrganizacional) {
		for (PfUnidadOrganizacionalDTO organisms : pfUsersOrganismList) {
			if(pfUnidadOrganizacional.getPrimaryKey().equals(organisms.getPrimaryKey())) {
				return true;
			}
		}
		return false;
	}

	@Transactional(propagation=Propagation.REQUIRED, rollbackFor = PortafirmasExceptionBussinesRollback.class)
	public void actualizarCargoConAutentica(Long idUsuario, List<String> returnValue) throws MalformedURLException, JAXBException, PortafirmasExceptionBussinesRollback, Error {
		
		int linea = 0;
		
		PfUsersDTO usuario = (PfUsersDTO) baseDAO.findEntitity(PfUsersDTO.class, idUsuario);
		log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
		
		PfConfigurationsParameterDTO parametroEndPointService = (PfConfigurationsParameterDTO) baseDAO.queryElementOneParameter(
				ADMINISTRATION_QUERY_PARAMETER_BY_NAME, C_PARAM,
				Constants.C_PARAMETER_URL_AUTENTICA);
		log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
		
		try {
			getUrl(parametroEndPointService.getTvalue());
		} catch (IOException e) {
			log.error("ERROR: "  + e);
		}
		
		DIR4WSService service = new DIR4WSService(new URL(parametroEndPointService.getTvalue()));
		log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
        DIR4WS port = service.getDIR4WS();
        log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
        
        WebUser usuarioParaAutenticarseEnAutentica = new WebUser();
        log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
        
		PfConfigurationsParameterDTO parametroUsuarioAutentica = (PfConfigurationsParameterDTO) baseDAO.queryElementOneParameter(
				ADMINISTRATION_QUERY_PARAMETER_BY_NAME, C_PARAM,
				Constants.C_PARAMETER_USUARIO_AUTENTICA);
		log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
		
		PfConfigurationsParameterDTO parametroClaveAutentica = (PfConfigurationsParameterDTO) baseDAO.queryElementOneParameter(
				ADMINISTRATION_QUERY_PARAMETER_BY_NAME, C_PARAM,
				Constants.C_PARAMETER_CLAVE_AUTENTICA);
		log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
        
        usuarioParaAutenticarseEnAutentica.setWebName(parametroUsuarioAutentica.getTvalue());
        log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
        usuarioParaAutenticarseEnAutentica.setWebPass(parametroClaveAutentica.getTvalue());
        log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
        
        AdminUser adminUserAutentica = new AdminUser();
        log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
        
        
		PfConfigurationsParameterDTO parametroUsuarioLdapAutentica = (PfConfigurationsParameterDTO) baseDAO.queryElementOneParameter(
				ADMINISTRATION_QUERY_PARAMETER_BY_NAME, C_PARAM,
				Constants.C_PARAMETER_USER_ADMIN_LDAP_AUTENTICA);
		log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
        adminUserAutentica.setAdminName(parametroUsuarioLdapAutentica.getTvalue());
        log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
        
        LdapUser usuarioAConsultar = new LdapUser();
        log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
        
        usuarioAConsultar.setDir4DocumentId(usuario.getCidentifier());
        log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
        
        String usersReturn = port.getUsers(usuarioParaAutenticarseEnAutentica, adminUserAutentica, usuarioAConsultar);
        log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
        
        if(usersReturn.trim().length()>0 && usersReturn.trim().charAt(0) != '<'){
        	log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
        	returnValue.add(usersReturn);
        	log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
        	throw new PortafirmasExceptionBussinesRollback(usersReturn);
        }
        
        JAXBContext jaxbContext = JAXBContext.newInstance(Respuesta.class);
        log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));

		Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
		log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
		Respuesta respuestaServicio = (Respuesta) jaxbUnmarshaller.unmarshal(new StringReader(usersReturn));
		log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));

		if(!"ok".equalsIgnoreCase(respuestaServicio.getResultado())){
			log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
			//Aqui se notifica el error
			returnValue.add("El servicio de autentica no ha respondido un estado satisfactorio");
			log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
			throw new PortafirmasExceptionBussinesRollback("El servicio de autentica no ha respondido un estado satisfactorio");
		}
		
		PfUsersDTO cargoActual = Util.getInstance().getUserValidJob(usuario);
		log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
		
		if( !(respuestaServicio.getUsuario() != null
				&& !respuestaServicio.getUsuario().isEmpty()
				&& respuestaServicio.getUsuario().get(0) != null
				&& respuestaServicio.getUsuario().get(0).getTitle() != null ) ){
			log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
			//No se trajo bien el cargo, excepcion
			returnValue.add("No se encontro el cargo en autentica");
			log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
			throw new PortafirmasExceptionBussinesRollback("No se encontro el cargo en autentica");
		}
		
		if( (cargoActual==null) || ( cargoActual!=null 
				&& cargoActual.getDname()!=null
				&& !cargoActual.getDname().equalsIgnoreCase(respuestaServicio.getUsuario().get(0).getTitle()) ) ){
			log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
			//Aqui es donde se busca o crea el cargo y se asigna
			PfUsersDTO cargo = (PfUsersDTO) baseDAO.queryElementOneParameter("position.getPositionByName", "nombreCargo", respuestaServicio.getUsuario().get(0).getTitle() );
			log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
			
			if( cargo == null ){
				log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
				User job = new User();
				log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
				job.setNif(String.valueOf(Calendar.getInstance().getTimeInMillis()));
				log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
				job.setName(respuestaServicio.getUsuario().get(0).getTitle());
				log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
				if(usuario.getPfProvince()!=null && usuario.getPfProvince().getCcodigoprovincia()!=null){
					job.setProvince(usuario.getPfProvince().getCcodigoprovincia());
				}
				else{
					throw new PortafirmasExceptionBussinesRollback("No se pudo actualizar el usuario con autentica porque se encuentra mal configurado, no tiene sede asociada");
				}
				log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
				job.setValid("S");
				log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
				job.setPublico("S");
				log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
				returnValue = saveJob(job);
				log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
				if(returnValue!= null && !returnValue.isEmpty()){
					log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
					throw new PortafirmasExceptionBussinesRollback(returnValue.toString());
				}
				cargo = (PfUsersDTO) baseDAO.queryElementOneParameter("position.getPositionByName", "nombreCargo", respuestaServicio.getUsuario().get(0).getTitle() );
				log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
			}
			
			if(cargo!=null){
				log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
				if(cargoActual!=null){
					log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
					Map<String, Object> params = new HashMap<>();
					log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
					
					params.put("idJob", cargoActual.getPrimaryKey());
					log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
					params.put("idUsuario", idUsuario);
					log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
					
					PfUsersJobDTO cargoParaCaducar = (PfUsersJobDTO) baseDAO.queryElementMoreParameters("administration.userAssociatedSpecificJob", params);
					log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
					
					if(cargoParaCaducar!=null){
						log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
						
						Calendar fechaDeFinalizacionDelCargoAnterior = Calendar.getInstance();
						log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
						fechaDeFinalizacionDelCargoAnterior.add(Calendar.DAY_OF_MONTH, -1);
						log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
						
						UserJob userJob = new UserJob();
						log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
						
						DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
						log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
						
						userJob.setfEnd(df.format(fechaDeFinalizacionDelCargoAnterior.getTime()).toString());
						log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
						userJob.setfStart(df.format(cargoParaCaducar.getFstart()).toString());
						log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
						userJob.setJobId(cargoActual.getPrimaryKey().toString());
						log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
						userJob.setUserId(idUsuario.toString());
						log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
						userJob.setUserJobPk(cargoParaCaducar.getPrimaryKeyString());
						log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
						returnValue = saveUserJob(userJob);
						log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
						if(returnValue!= null && !returnValue.isEmpty()){
							log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
							throw new PortafirmasExceptionBussinesRollback(returnValue.toString());
						}
						
					}
					//Aqui hay que caducar el cargo actual
				}
				UserJob userJob = new UserJob();
				log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
				userJob.setfEnd(null);
				log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
				DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
				log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
				String requiredDate = df.format(Calendar.getInstance().getTime());
				log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
				userJob.setfStart(requiredDate);
				log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
				userJob.setJobId(cargo.getPrimaryKeyString());
				log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
				userJob.setUserId(idUsuario.toString());
				log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
				returnValue = saveUserJob(userJob);
				log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
				if(returnValue!= null && !returnValue.isEmpty()){
					log.error(USER_ADM_BO_ACTUALIZAR_CARGO_CON_AUTENTICA+(++linea));
					throw new PortafirmasExceptionBussinesRollback(returnValue.toString());
				}
			}
		}
        
	}
	
	@Transactional(propagation=Propagation.REQUIRED)
	public  ArrayList<String> saveUserJob(UserJob userJob) {
		ArrayList<String> warnings = new ArrayList<String>();
		try {
			// Se valida la vista
			userJobValidator.validate(userJob, warnings);
			if(!warnings.isEmpty()) {
				return warnings;
			}
			
			// Se crea el usuario DTO a persistir
			PfUsersJobDTO pfUsersJobDTO = userJobConverter.envelopeToDTO(userJob);

			// Se validan las reglas de negocio
			validateUserJob(pfUsersJobDTO, warnings);
			if(!warnings.isEmpty()) {
				return warnings;
			}
			
			// Se realiza la persistencia del usuario
			saveUserJob(pfUsersJobDTO);
			
		} catch (Exception e) {
			log.error("Error al insertar el cargo del usuario: ", e);
			warnings.add(Constants.MSG_GENERIC_ERROR);
		}
		return warnings;
	}
	
	@Transactional(propagation=Propagation.REQUIRED, readOnly=false)
	public ArrayList<String> saveJob(User job) {
		ArrayList<String> errors = new ArrayList<String>();
		try {
			// Se valida la vista
			jobValidator.validate(job, errors);
			if(!errors.isEmpty()) {
				return errors;
			}	
			// Se vuelcan los datos obtenidos de la vista
			PfUsersDTO pfJobDTO = jobConverter.envelopeToDTO(job);
			pfJobDTO.setCidentifier(pfJobDTO.getCidentifier().toUpperCase().trim());//Obligamos a que el identificador valla en mayusculas y sin espacios

			
			// Se validan las reglas de negocio
			jobAdmBO.validateJob(pfJobDTO, errors);
			if(!errors.isEmpty()) {
				return errors;
			}			
			
			// Se realiza la persistencia del usuario
			jobAdmBO.saveJob(pfJobDTO);
			
		} catch (Exception e) {
			log.error("Error al insertar el cargo: ", e);
			errors.add(Constants.MSG_GENERIC_ERROR);
		}

		return errors;
	}
	
	private void getUrl(String url) throws IOException{
		log.error("el GET es sobre la url "+url);
        URL urlObject = new URL(url);
        BufferedReader in = new BufferedReader(
        new InputStreamReader(urlObject.openStream()));

        String inputLine;
        while ((inputLine = in.readLine()) != null)
            log.error(inputLine);
        in.close();
	}
	
	public List<AdminSeat> getAdminOrgList(List<PfUnidadOrganizacionalDTO> orgListDTO) {
		return getAdminOrgList(null, orgListDTO);
	}
	
	/**
	 * Obtiene la lista de organismos y marca las que tiene asignadas el usuario
	 * @param pfUsersDTO
	 * @param seatListDTO
	 * @return
	 */
	public List<AdminSeat> getAdminOrgList(PfUsersDTO pfUsersDTO, List<PfUnidadOrganizacionalDTO> orgListDTO) {
		ArrayList<AdminSeat> adminSeatList = new ArrayList<AdminSeat>();
		for (PfUnidadOrganizacionalDTO org: orgListDTO) {
			AdminSeat adminSeat = new AdminSeat();
			adminSeat.setId(org.getPrimaryKeyString());
			adminSeat.setName(org.getDenominacion());
			if(pfUsersDTO != null && listContainsOrganism(pfUsersDTO.getPfUnidadOrganizacionalList(), org)) {
				adminSeat.setChecked(true);
			}
			adminSeatList.add(adminSeat);
		}
		return adminSeatList;
	}

	/**
	 * Método para guardar usuarios pertenecientes a otros portafirmas
	 * @param usuario
	 */
	public void saveExternalUser(PfUsersDTO usuario) {
		PfUsersDTO pfUsersBBDD = this.queryUsersByPk((usuario.getPrimaryKey()));
		PfUsersDTO pfUsersDTO = null;
		if(pfUsersBBDD == null) {
			usuario.createAuditing();
			usuario.updateAuditing();
			usuario.setLvalid(Boolean.TRUE);
			pfUsersDTO = usuario;
		} else {
			pfUsersBBDD.setCidentifier(usuario.getCidentifier());
			pfUsersBBDD.setDname(usuario.getDname());
			pfUsersBBDD.setDsurname1(usuario.getDsurname1());
			pfUsersBBDD.setDsurname2(usuario.getDsurname2());
			pfUsersBBDD.setPortafirmas(usuario.getPortafirmas());
			pfUsersBBDD.setLvalid(usuario.getLvalid());
			pfUsersBBDD.updateAuditing();
			pfUsersDTO = pfUsersBBDD;
		}

		pfUsersDTO.setLvisible(Boolean.TRUE);
		pfUsersDTO.setCtype(Constants.C_TYPE_USER_EXTERNAL);
		pfUsersDTO.setLshownotifwarning(Boolean.FALSE);
		
		baseDAO.insertOrUpdate(pfUsersDTO);
	}
	
	@Transactional(readOnly = false)
	public void updateMostrarFirmanteAnterior (PfUsersDTO pfUser, boolean estadoMostrarFirmanteAnterior) {
		pfUser.setMostrarFirmanteAnterior(estadoMostrarFirmanteAnterior);
		saveUser(pfUser);
	}
}

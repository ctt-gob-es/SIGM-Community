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

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;
import javax.persistence.NonUniqueResultException;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.guadaltel.framework.authenticator.exception.AuthenticatorException;
import es.seap.minhap.portafirmas.business.administration.JobAdmBO;
import es.seap.minhap.portafirmas.business.administration.UserAdmBO;
import es.seap.minhap.portafirmas.business.configuration.ValidatorUsersConfBO;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.PfGroupsDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.domain.PfUsersGroupsDTO;
import es.seap.minhap.portafirmas.domain.PfUsersProfileDTO;
import es.seap.minhap.portafirmas.security.authentication.AuthenticationHelper;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.utils.login.LoginService;
import es.seap.minhap.portafirmas.utils.login.LoginServiceConfiguration;
import es.seap.minhap.portafirmas.utils.login.LoginServiceFactory;
import es.seap.minhap.portafirmas.utils.login.LoginServiceResponse;


@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class AuthenticateBO {


	@Resource(name = "messageProperties")
	private Properties messages;

	Logger log = Logger.getLogger(AuthenticateBO.class);

	@Autowired
	private BaseDAO baseDAO;

	@Autowired
	private UserAdmBO userBO;

	@Autowired 
	private AuthenticationHelper authenticationHelper;

	@Autowired
	private JobAdmBO jobAdmBO;
	
	@Autowired
	private ValidatorUsersConfBO validatorUsersConfBO;	

	/**
	 * Recupera el usuario que corresponde con id que pasamos como parámetro
	 * @param id identificador del usuario
	 * @return el usuario que corresponde con el id pasado como par&aacute;metro
	 * @throws AuthenticatorException si no recuperamos el usuario con el id pasado como par&aacute;metro o
	 * el usuario recuperado no tiene acceso a la aplicaci&oacute;n o no es un usuario v&aacute;lido
	 * @throws Exception
	 * @see {@link #authenticateId(String)}
	 */
	public PfUsersDTO authenticate(String nif) throws AuthenticatorException {
		log.info("authenticate init");		
		
		PfUsersDTO userDTO = null;		

		try {
			log.debug("Querying user");
			//Recuperamos el usuario
			userDTO = userBO.getUserByDni(nif);
			// User not found, error page
			if (userDTO == null) {
				log.debug("User not found");
				throw new AuthenticatorException(messages.getProperty("userNotFound"));
			} else if (!checkAccessProfile(userDTO)
					|| !userDTO.getLvalid()) {
				log.debug("Checking access profile");
				log.debug("Acces profile not found");
				throw new AuthenticatorException(messages.getProperty("noAccessProfile"));
			}
		} catch (AuthenticatorException e) {
			throw e;
		}

		log.info("autenthicate end: " + userDTO);

		return userDTO;
	}
	

	/**
	 * Chequea si un usuario tiene acceso a la aplicaci&oacute;n, si lo tiene devuelve el objeto del usuario,
	 * si no tiene acceso genera un mensaje de error y lanza una excepci&oacute;n, si no encuentra el usuario
	 * devuelve nulo.
	 * @param id el id del usuario (es el dni)
	 * @param password las password del usuario
	 * @return el usuario o nulo si no lo encuentra
	 * @throws AuthenticatorException si el usuario existe pero no tiene acceso
	 * o no es un usuario v&aacute;lido o est&aacute; caducado
	 * @see #checkAccessProfile(PfUsersDTO)
	 * @see es.seap.minhap.portafirmas.domain.PfUsersDTO#getLvalid()
	 */
	public PfUsersDTO authenticateIdPass(String id, String password)
			throws AuthenticatorException {
		PfUsersDTO userDTO = null;

		Map<String, Object> parameters = new HashMap<>();
		parameters.put("dni", id.toUpperCase());
		parameters.put("password", password);
		//recupera el usuario con el dni y la password que pasamos en el mapa
		userDTO = (PfUsersDTO) baseDAO.queryElementMoreParametersWithOutLog(
				"request.userProfilesDniPass", parameters);
		//Si el usuario no es nulo y no tiene perfil de acceso o no es valido
		//genera un mensaje de error
		if (userDTO != null
				&& ((!checkAccessProfile(userDTO)) || !userDTO.getLvalid())) {
			log.debug("Checking access profile");
			log.debug("Acces profile not found");
			throw new AuthenticatorException(messages.getProperty("noAccessProfile"));
		}
		return userDTO;
	}
	/**
	 * Recupera el usuario con el c&oacute;digo de par&aacute;metro 'USUARIO.LDAP.IDATRIBUTO'
	 * y el valor de par&aacute;metro de usuario igual a el identificador del usuario 
	 * en LDAP pasamos como par&aacute;metro
	 * @param ldapId el identificador del usuario en LDAP
	 * @return el usuario recuperado de bbdd o nulo si no encuentra nada
	 * @throws AuthenticatorException si hemos recuperado el usuario y no tiene acceso al sistema o no est&aacute; vigente
	 */
	public PfUsersDTO authenticateLdapId(String ldapId)
			throws AuthenticatorException, NonUniqueResultException {
		PfUsersDTO userDTO = null;

		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("ldapId", ldapId);
		//Recupera el usuario con el c&oacute;digo de par&aacute;metro 'USUARIO.LDAP.IDATRIBUTO'
		//y el valor de par&aacute;metro de usuario igual a la variable ldapId que pasamos
		userDTO = (PfUsersDTO) baseDAO.queryElementMoreParametersWithOutLog(
				"request.userProfilesLdapId", parameters);
		//Si hemos recuperado el usuario y no tiene acceso al sistema o no est&aacute; vigente
		//generamos un mensaje de error y lanzamos una excepci&oacute;n
		log.debug("Checking access profile");
		if (userDTO != null
				&& ((!checkAccessProfile(userDTO)) || !userDTO.getLvalid())) {
			log.debug("Acces profile not found");
			throw new AuthenticatorException(messages.getProperty("noAccessProfile"));
		}
		return userDTO;
	}

	/**
	 * Chequea que un usuario tenga perfil de acceso a la aplicaci&oacute;n
	 * @param user el usuario
	 * @return true o false dependiendo si el usuario tiene perfil de acceso o no
	 * y si est&aacute; vigente
	 */
	private boolean checkAccessProfile(PfUsersDTO user) {
		boolean result = false;
		//Si el usuario no es nulo y tiene perfiles
		if (user != null && user.getPfUsersProfiles() != null) {
			for (PfUsersProfileDTO userProfile : user.getPfUsersProfiles()) {
				//Si tiene perfil de acceso y est&aacute; vigente devuelve true
				if (userProfile.getPfProfile().getCprofile().equals(
						Constants.C_PROFILES_ACCESS)
						&& (userProfile.getFend() == null || userProfile
								.getFend().after(new Date()))) {

					result = true;
				}
			}
		}
		return result;
	}

	/**
	 * M&eacute;todo que devuelve el &uacute;ltimo cargo v&aacute;lido que tiene un usuario del Portafirmas en concreto. Se entiende
	 * por "cargo v&aacute;lido" un cargo cuya validez temporal sigue vigente (no ha caducado).
	 * @param userDTO Objeto que define al usuario del Portafirmas del que se desea obtener el cargo v&aacute;lido.
	 * @return Objeto que define el cargo v&aacute;lido del usuario introducido.
	 */
	public PfUsersDTO loadValidJob(PfUsersDTO userDTO) {
		return Util.getInstance().getUserValidJob(userDTO);
	}
	
	/**
	 * Recupera la informaci&oacute;n de respuesta de conexi&oacute;n del usuario a LDAP
	 * @param ldapLoginParameters el mapa con los par&aacute;metros Load LDAP login parameters 
	 * @param id el id del usuario
	 * @param password el password 
	 * @return el objeto con la informaci&oacute;n de respuesta de conexi&oacute;n del usuario a LDAP
	 * @see es.seap.minhap.portafirmas.utils.login.LoginServiceResponse
	 * @see es.seap.minhap.portafirmas.utils.login.LoginServiceConfiguration
	 * @see es.seap.minhap.portafirmas.utils.login.LoginServiceFactory#create(LoginServiceConfiguration)
	 */
	public LoginServiceResponse authenticateLdap(
			Map<String, String> ldapLoginParameters, String id, String password) {
		LoginServiceResponse response = null;
		LoginServiceConfiguration lsc = obtenerLoginServiceConfiguration(ldapLoginParameters);
		String[] dcs = ldapLoginParameters.get(Constants.LOGIN_LDAP_DN).split(Constants.LOGIN_LDAP_SEPARATOR);
		for (int i = 0; i < dcs.length; i++) {
			lsc.setLdapDn(dcs[i]);
			LoginService ls = LoginServiceFactory.create(lsc);
			response = ls.check(id, password);
			if (response.getStatus() == LoginServiceConfiguration.RESPONSE_OK) {
				break;
			}
		}
		return response;
	}


	private LoginServiceConfiguration obtenerLoginServiceConfiguration(Map<String, String> ldapLoginParameters) {
		LoginServiceConfiguration lsc = new LoginServiceConfiguration();
		lsc.setLoginType(LoginServiceConfiguration.LOGIN_TYPE_LDAP);
		lsc.setLdapUrl(ldapLoginParameters.get(Constants.LOGIN_LDAP_URL));
		//El id del atributo del ldap del usuario
		lsc.setLdapIdAttributeName(ldapLoginParameters.get(Constants.LOGIN_LDAP_IDATTRIBUTE));
		//el atributo identificador del ldap
		lsc.setIdentifierAttributeName(ldapLoginParameters.get(Constants.LOGIN_LDAP_ID));
		lsc.setLdapBaseDn(ldapLoginParameters.get(Constants.LOGIN_LDAP_BASEDN));
		return lsc;
	}

	/**
	 * Método que obtiene un usuario a partir de su clave primaria
	 * @param userPk Clave primaria
	 * @return Usuario
	 */
	@Transactional(readOnly=false)
	public PfUsersDTO authenticateByPK(String userPk) {
		Map<String, Object> parametros = new HashMap<String, Object>();
		parametros.put("pk", Long.parseLong(userPk));
		parametros.put("tipo", Constants.C_TYPE_USER_USER);
		// Recupera el usuario a partir de su primary key
		return (PfUsersDTO) baseDAO.queryElementMoreParameters("request.userProfilesPk", parametros );
	}


	/**
	 * Para validar el validador con el que se autentica el usuario
	 * @param user
	 * @param userToValidate
	 * @return
	 */
	public String valiteToValidator(PfUsersDTO user, PfUsersDTO userToValidate) {
		if(userToValidate == null) {
			return messages.getProperty("errorUserNotFound");
		} else {
			Set<PfUsersDTO> validadoresDe = user.getValidadorDe();
			PfUsersDTO job = Util.getInstance().getUserValidJob(user);
			if(job!=null){
				job = jobAdmBO.getJobByCode(job.getCidentifier());
				validadoresDe.addAll(job.getValidadorDe());
			}
			List<PfUsersDTO> validatedByAppUsersList = validatorUsersConfBO.queryValidatorsByValidatorListTest(user);			
			validadoresDe.addAll(validatedByAppUsersList);

			if(!validadoresDe.contains(userToValidate) && !user.equals(userToValidate)) {
				return messages.getProperty("errorForbiddenUser");
			}
		}
		return null;
	}

	/**
	 * Para validar el gestor con el que se autentica el usuario
	 * @param user
	 * @param userToValidate
	 * @return
	 */
	public String valiteToGestor(PfUsersDTO user, PfUsersDTO userToGestionar) {
		if(userToGestionar == null) {
			return messages.getProperty("errorUserNotFound");
		} else {
			Set<PfUsersDTO> gestoresDe = user.getGestorDe();
			if(!gestoresDe.contains(userToGestionar) && !user.equals(userToGestionar)) {
				return messages.getProperty("errorForbiddenUser");
			}
		}
		return null;
	}

	
	
	/**
	 * Para validar el grupo con el que se autentica el usuario
	 * @param user
	 * @param group
	 * @return
	 */
	public String valiteGroup(PfUsersDTO user, PfGroupsDTO group) {
		if(group == null) {
			return messages.getProperty("errorGroupNotFound");
		} else {
			Set<PfUsersGroupsDTO> userGroups = user.getPfUsersGroups();
			for (Iterator<PfUsersGroupsDTO> it = userGroups.iterator(); it.hasNext();) {
				PfUsersGroupsDTO pfUsersGroupsDTO = (PfUsersGroupsDTO) it.next();
				if(pfUsersGroupsDTO.getPfGroup().getPrimaryKey().equals(group.getPrimaryKey())) {
					return null;
				}
			}
			return messages.getProperty("errorForbiddenGroup");
		}
	}
	
	public Authentication autenticarUsuario(String dni) {
		PfUsersDTO userDatabase = null;
		try {
			userDatabase = this.authenticate(dni);
		} catch (Exception e) {
			log.error(e);
			throw new BadCredentialsException( "Credenciales Erróneas. Usuario no válido." );
		} 					
		
		if (userDatabase != null) {
			if(userDatabase.getLvalid()) {
				// Y se define el objeto de autenticación y se mete en el contexto de seguridad
				log.debug("Usuario autentificado: " + userDatabase.getFullName() + " y VIGENTE.");
				return new UserAuthentication(userDatabase, null, authenticationHelper.getRoles(userDatabase), userDatabase, null,userDatabase);
			} else {
				log.debug("Usuario autentificado: " + userDatabase.getFullName() + " y NO VIGENTE.");
				throw new BadCredentialsException("Usuario NO vigente.");
			}
	
		} else {
			throw new BadCredentialsException("Usuario no encontrado o password erróneo");
		}
	}
	
}

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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import es.seap.minhap.portafirmas.business.ApplicationBO;
import es.seap.minhap.portafirmas.business.AuthenticateBO;
import es.seap.minhap.portafirmas.business.GroupBO;
import es.seap.minhap.portafirmas.business.administration.JobAdmBO;
import es.seap.minhap.portafirmas.business.configuration.ValidatorUsersConfBO;
import es.seap.minhap.portafirmas.business.login.LoginBusinessService;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.utils.Util;
import es.seap.minhap.portafirmas.web.beans.Login;
import es.seap.minhap.portafirmas.utils.Constants;

import eu.stork.peps.auth.commons.PEPSUtil;

@Controller
@RequestMapping({"/","login"})
public class LoginController {

	protected final Log log = LogFactory.getLog(getClass());
	
	@Resource(name="autenticaProperties")
	private Properties autenticaProperties;
	
	@Autowired
	private GroupBO groupBO;
	
	@Autowired
	private JobAdmBO jobAdmBO;
	
	@Autowired
	private ApplicationBO applicationBO;
	
	@Autowired
	private ValidatorUsersConfBO validatorUsersConfBO;

	@Resource(name="claveProperties")
	private Properties claveProperties;
	
	@Autowired
	private LoginBusinessService loginBusinessService;
	
	@Autowired
	private AuthenticateBO authenticateBO;
	
	/**
	 * Método que inicializa la vista de login
	 * @param model Modelo de datos
	 * @return Modelo de datos inicializado
	 */
	@RequestMapping(method = RequestMethod.GET)
	public String initForm(ModelMap model, final HttpServletRequest request) {		
		log.debug(  "Addr: " + request.getRemoteAddr()
				+ "\nHost: " + request.getRemoteHost()
				+ "\nPort: " + request.getRemotePort()
				+ "\nUser" + request.getRemoteUser());
		Login login = new Login();
		login.setError("display: none;");
		login.setErrorClave("display: none;");
		login.setInfo("display: none;");

		cargarModelo(model, login);

		return "login";
	}

	/**
	 * Método que procesa el formulario inicial indicando un error de login
	 * @param model Modelo de datos del formulario
	 * @return Modelo de datos indicando el error
	 */
	@RequestMapping(value = "/error", method = RequestMethod.GET)
	public String initErrorForm(@RequestParam("errorType") String errorType, ModelMap model) {

		Login login = new Login();
		if ("BadCredentials".equals(errorType) || "AuthenticationService".equals(errorType)) {
			login.setError("display: ;");
		} else {
			login.setError("display: none;");
		}
		if ( "ErrorClave".equals(errorType)) {
			login.setErrorClave("display: ;");
		} else {
			login.setErrorClave("display: none;");
		}
		login.setInfo("display: none;");
		
		cargarModelo(model, login);

		return "login";
	}
	
	private void cargarModelo(ModelMap model, Login login) {
		model.addAttribute("showLDAP", applicationBO.viewUserPasswordField());
		model.addAttribute("loginForm", login);
		model.addAttribute("autenticaActivo", applicationBO.isAutenticaActivo());
		model.addAttribute("autenticaUrl",obtenerUrlAutentica());
	}

	private String obtenerUrlAutentica() {
		return	autenticaProperties.getProperty("autentica.peticion.url") + 
				autenticaProperties.getProperty("autentica.aplicacion.id");
	}
	
	/**
	 * Método que muestra al usuario la lista de usuarios a los que valida para hacer el login
	 * @param model Modelo de datos
	 * @return Página de validadores
	 */
	@RequestMapping(value = "/validatorLogin")
	public String validatorLogin(ModelMap model) {

		// Se recupera el usuario autenticado
		UserAuthentication authorization = (UserAuthentication) SecurityContextHolder
				.getContext().getAuthentication();
		PfUsersDTO user = (PfUsersDTO) authorization.getPrincipal();
		
		PfUsersDTO job = Util.getInstance().getUserValidJob(user);
		
		if(job!=null){
			job = jobAdmBO.getJobByCode(job.getCidentifier());
		}

		// Obtiene la lista de los usuarios validados
		List<AbstractBaseDTO> validatedUsersList = getUserList(user.getValidadorDe());
		
		obtenerValidadoresPorApp(user, validatedUsersList);
		
		if(job!=null){
			validatedUsersList.addAll(getUserList(job.getValidadorDe()));
		}
		
		validatedUsersList.add(0, user);
		model.addAttribute("validatedUsersList", validatedUsersList);

		List<AbstractBaseDTO> pfUsersGroups = groupBO.getGroupsFromUser(user);
		model.addAttribute("groupsList", pfUsersGroups);

		// Obtiene la lista de los usuarios gestionados
		List<AbstractBaseDTO> gestionatedUsersList = getUserList(user.getGestorDe());
		
		if(job!=null){
			gestionatedUsersList.addAll(getUserList(job.getGestorDe()));
		}
		model.addAttribute("gestionatedUsersList", gestionatedUsersList);
		
		return "validator_login";
	}

	/**Método para extraer los validadores por aplicacion del usuario.
	 * 
	 * @param user
	 * @param validatedUsersList
	 */
	private void obtenerValidadoresPorApp(PfUsersDTO user, List<AbstractBaseDTO> validatedUsersList) {
		
		List<PfUsersDTO> validatedByAppUsersList = validatorUsersConfBO.queryValidatorsByValidatorListTest(user);
		
		validatedUsersList.addAll(validatedByAppUsersList);
	}

	private List<AbstractBaseDTO> getUserList(Set<PfUsersDTO> users){
		List<AbstractBaseDTO> userList = new ArrayList<AbstractBaseDTO>();
		if (users != null && !users.isEmpty()){
			Iterator<PfUsersDTO> usersIt = users.iterator();
			while(usersIt.hasNext()){
				userList.add(usersIt.next());
			}
		}
		return userList;
	}
	
	@RequestMapping(value = "/accesoRedirectClave", method = RequestMethod.GET)
	public String loginClave(ModelMap model) {
		log.debug("Redirigiendo a clave");

		try {
			String claveServiceUrl = claveProperties.getProperty(Constants.PROPERTY_URL_CLAVE);
			String excludedIdPList = claveProperties.getProperty(Constants.PROPERTY_EXCLUDED_IDPLIST);
			String forcedIdP = claveProperties.getProperty(Constants.PROPERTY_FORCED_IDP);

			/** No es necesario generar aquí el token, sólo debe saltar al proxy
			byte[] token = null;
			try {
				token = loginBusinessService.generaTokenClave(claveProperties);
			} catch (Exception e) {
				log.error("Error en la llamada a clave", e);
				model.addAttribute("errorMessage", Constants.CREDENTIALS_ERROR_GENERATE_SAML);
				return initErrorForm("ErrorClave", model);
			}

			String samlRequest = PEPSUtil.encodeSAMLToken(token);
			String samlRequestXML = new String(token);
			if (log.isInfoEnabled()) {
				log.debug(samlRequestXML);
			}
			*/
			
			//Como generar codigos aleatorios si hiciera falta
			//RandomString session = new RandomString();			

			model.addAttribute("claveServiceUrl", claveServiceUrl);
			//model.addAttribute(Constants.ATRIBUTO_EXCLUDED_IDPLIST, excludedIdPList);
			//model.addAttribute(Constants.ATRIBUTO_FORCED_IDP, forcedIdP);
			//model.addAttribute(Constants.ATRIBUTO_SAML_REQUEST, samlRequest);

			return "login-redirect";
		} catch (Exception e) {
			log.error("Error en la llamada a clave", e);
			model.addAttribute("errorMessage", Constants.CREDENTIALS_ERROR_GENERATE_SAML);

			return initErrorForm("ErrorClave", model);
		}
	}

}
	

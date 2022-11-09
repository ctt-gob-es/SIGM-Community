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

package es.seap.minhap.portafirmas.security.authentication;

import java.util.Map;

import javax.persistence.NonUniqueResultException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import es.guadaltel.framework.authenticator.exception.AuthenticatorException;
import es.seap.minhap.portafirmas.business.ApplicationBO;
import es.seap.minhap.portafirmas.business.AuthenticateBO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.login.LoginServiceConfiguration;
import es.seap.minhap.portafirmas.utils.login.LoginServiceResponse;

@Component
public class PfAuthenticationProvider implements AuthenticationProvider {

	protected final Log log = LogFactory.getLog(getClass());

	@Autowired
	private ApplicationBO applicationBO;

	@Autowired
	private AuthenticateBO authenticateBO;
	
	@Autowired 
	private AuthenticationHelper authenticationHelper;

	@Override
	public Authentication authenticate(Authentication authentication)
			throws AuthenticationException {
		String name = authentication.getName();
        String password = authentication.getCredentials().toString();
 
        if ("".equals(name) || name == null || "".equals(password) || password == null) {
        	log.error("Usuario o password no introducidos");
			throw new BadCredentialsException("Debe introducir el identificador de usuario y el password");
        } else {
	        // Obtenemos un mapa con los parámetros Load LDAP login parameters
	     	Map<String, String> ldapLoginParameters = applicationBO.queryLDAPParameterList();
	
	     	PfUsersDTO user = null;
	     	if(ldapLoginParameters.get(Constants.C_PARAMETER_LOGIN_LDAP).equals(Constants.C_YES)) {
	     		user = this.obtenerUsuarioLDAP(name, password, ldapLoginParameters);
	     	} else {
	     		user = this.obtenerUsuarioUserPassword(name, password);
	     	}
	
	     	if (user != null) {
	     		// Se define el objeto de autenticación y se mete en el contexto de seguridad
		     	if(user.getLvalid()) {
					log.debug("Usuario autentificado: " + user.getFullName() + " y VIGENTE.");
					return new UserAuthentication(user, password, authenticationHelper.getRoles(user), user, null,user);
	     		} else {
	     			log.debug("Usuario autentificado: " + user.getFullName() + " y NO VIGENTE.");
	     			throw new BadCredentialsException("Usuario NO vigente.");
	     		}
	     	} else {
	     		throw new BadCredentialsException("Usuario no encontrado o clave incorrecta");
	     	}
        }
	}

	private PfUsersDTO obtenerUsuarioUserPassword(String name, String password) {
     	PfUsersDTO user = null;
     		try {
				user = authenticateBO.authenticateIdPass(name, password);
			} catch (AuthenticatorException e) {
				log.error("Usuario no encontrado o clave incorrecta", e);
				throw new BadCredentialsException("Usuario no encontrado o clave incorrecta", e);
			}
		return user;
	}

	private PfUsersDTO obtenerUsuarioLDAP(String name, String password, Map<String, String> ldapLoginParameters) {
     	PfUsersDTO user = null;
     	LoginServiceResponse lsc = authenticateBO.authenticateLdap(ldapLoginParameters, name, password);
     	if (lsc.getStatus() == LoginServiceConfiguration.RESPONSE_OK) {
     		try {
				user = authenticateBO.authenticateLdapId(name);
			} catch (AuthenticatorException e) {
				log.error("ERROR: PfAuthenticationProvider.authenticate: ", e);
				throw new BadCredentialsException("Error al conectar con el LDAP: ", e);
			}catch(NonUniqueResultException e){
				log.error("ERROR: PfAuthenticationProvider.authenticate: ", e);
				throw new BadCredentialsException("Error al obtener el usuario LDAP: se ha encontrado correspondencia múltiple: ", e);
			}
     	}
		return user;
	}

	@Override
	public boolean supports(Class<?> authentication) {
		return authentication.equals(UsernamePasswordAuthenticationToken.class);
	}
}

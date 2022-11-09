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

package es.seap.minhap.portafirmas.security.handler;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import es.seap.minhap.portafirmas.business.SessionBO;
import es.seap.minhap.portafirmas.business.configuration.ValidatorUsersConfBO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.web.validation.UserValidator;

/**
 * Manejador que se ejecuta cuando el usuario ha realizado el login correctamente
 * @author hugo
 *
 */
@Component
public class PfAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	protected final Log log = LogFactory.getLog(getClass());

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Autowired
	private SessionBO sessionBO;
	
	@Autowired
	private UserValidator userValidator;

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request,
			HttpServletResponse response, Authentication authentication) throws IOException,
			ServletException {

		String targetUrl = "";

		// Se comprueba si el usuario tiene perfil de validador
		UserAuthentication userAuthentication = (UserAuthentication) authentication;

		// Usuario autenticado
		PfUsersDTO authUser = (PfUsersDTO) userAuthentication.getPrincipal();

		// Se guardan los datos de sesión en base de datos
		HttpSession session = request.getSession();
		sessionBO.insertSessionAttributes(session.getId(), request.getRemoteHost(),
										  request.getHeader("User-Agent"), authUser.getFullName(), authUser);

		if (userValidator.necesitaPantallaIntermediaDeLogin(authUser)) {
			// Si el usuario es validador se le muestra la opción de hacer login con ese rol
			targetUrl = "/login/validatorLogin";

			// Se obtiene la lista de usuarios validados y se mete en el objeto de autenticación
			userAuthentication.setValidatedUsers(authUser.getValidadorDe());

		} else {
			targetUrl = "/inbox";
		}

		redirectStrategy.sendRedirect(request, response, targetUrl);
	}

	
}

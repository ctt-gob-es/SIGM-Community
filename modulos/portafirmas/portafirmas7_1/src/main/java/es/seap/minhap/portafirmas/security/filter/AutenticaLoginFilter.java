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

package es.seap.minhap.portafirmas.security.filter;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import es.seap.minhap.portafirmas.business.AuthenticateBO;
import es.seap.minhap.portafirmas.business.autentica.AutenticaBO;
import es.seap.minhap.portafirmas.utils.Constants;

public class AutenticaLoginFilter extends AbstractAuthenticationProcessingFilter {
	
	protected final Log log = LogFactory.getLog(getClass());

	public AutenticaLoginFilter(String defaultFilterProcessesUrl) {
		super(defaultFilterProcessesUrl);
	}
	
	protected AutenticaLoginFilter() {
		super("/autentica/login");
	}
	
	@Autowired
	private AutenticaBO autenticaBO;

	@Autowired
	private AuthenticateBO authenticateBO;

	@Override
	public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
			throws AuthenticationException, IOException, ServletException {
		try {
			
			String samlResponseAutentica = this.leerSamlResponseAutentica(request);

			autenticaBO.validarRespuestaAutentica(samlResponseAutentica);
			
			String dni = autenticaBO.leerDniAutentica(samlResponseAutentica);
			
			return authenticateBO.autenticarUsuario(dni);

		} catch (BadCredentialsException e) {
			log.error(e);
			throw e;
		} catch (Exception e) {
			log.error(e);
			throw new BadCredentialsException(Constants.MSG_GENERIC_ERROR);
		}
	}
	
	private String leerSamlResponseAutentica(HttpServletRequest request) {
		String samlResponse = request.getParameter(Constants.AUTENTICA_ATRIBUTO_SAML_RESPONSE);
		if (samlResponse == null){
			samlResponse = (String) request.getAttribute(Constants.AUTENTICA_ATRIBUTO_SAML_RESPONSE);
		}
		log.debug("Respuesta autentica: " + samlResponse);
		return samlResponse;
	}

}
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

package es.seap.minhap.portafirmas.servlet.restriction;

import javax.servlet.ServletRequest;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.business.SessionBO;
import es.seap.minhap.portafirmas.domain.PfSessionAttributesDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class SessionAttributesRestrictionImpl implements Restriction {

	Logger log = Logger.getLogger(SessionAttributesRestrictionImpl.class);
	
	@Autowired
	private SessionBO sessionBO;

	public boolean check(ServletRequest request) {
		boolean check = false;
		HttpServletRequest req = (HttpServletRequest)request;

		// Se recupera el usuario
		UserAuthentication authorization = (UserAuthentication) SecurityContextHolder
					.getContext().getAuthentication();
		PfUsersDTO user = (PfUsersDTO) authorization.getUserDTO();

		// Obtengo el contexto de Spring
		//ApplicationContext appContext = WebApplicationContextUtils.getRequiredWebApplicationContext(req.getSession().getServletContext());

		String userAgent = req.getHeader("User-Agent");
		String ip = req.getRemoteAddr();
		String userName = user.getFullName();
		
		// Obtenemos los atributos de la sesión
		//SessionBO sessionBO = appContext.getBean(SessionBO.class);
		PfSessionAttributesDTO atts = sessionBO.querySessionAttributes(req.getSession(false).getId());

		if (atts != null  && 
				!esVacio(atts.getCuseragent()) && correctUserAgent(atts.getCuseragent(), userAgent)  &&
				!esVacio(atts.getCip()) && atts.getCip().equalsIgnoreCase(ip) &&
				!esVacio(atts.getCusername()) && atts.getCusername().equalsIgnoreCase(userName)) {		
			check = true;
		}

		if (!check) {
			log.debug("No se han podido verificar los credenciales");
			log.debug("User-agent = " + atts.getCuseragent() + " " + userAgent);
			log.debug("Ip = " + atts.getCip() + " " + ip);
			log.debug("Usuario = " + atts.getCusername() + " " + userName);
		}

		return check;
	}
		
	private boolean esVacio (String cadena) {
		boolean es = true;
		if (cadena != null && !cadena.contentEquals("")) {
			es = false;
		}
		return es;
	}

	private boolean correctUserAgent(String attsUserAgent, String requestUserAgent) {
		boolean correct = false;

		if (attsUserAgent.equalsIgnoreCase(requestUserAgent)) {
			correct = true;
		} else if ((attsUserAgent.contains("MSIE 7.0") && requestUserAgent.contains("MSIE 8.0")) ||
				   (attsUserAgent.contains("MSIE 8.0") && requestUserAgent.contains("MSIE 7.0"))) {
			correct = true;
		}

		return correct;
	}
	
}

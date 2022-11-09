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

package es.seap.minhap.portafirmas.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import es.seap.minhap.portafirmas.business.administration.UserAdmBO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;
import es.seap.minhap.portafirmas.utils.Constants;

/**
 * Estudia todas las peticiones para comprobar si el usuario puede acceder
 * al recurso solicitado.
 * @author domingo
 *
 */
public class PortafirmasInterceptor implements HandlerInterceptor {

	@Autowired
	private UserAdmBO userAdmBO;
	
	@Override
	public boolean preHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler) throws Exception {
		Authentication authorization = SecurityContextHolder.getContext().getAuthentication();
		// Si el usuario ya esta logado..
		if(authorization instanceof UserAuthentication) {
			// .. obtiene el usuario logado y el principal
			PfUsersDTO user = ((UserAuthentication) authorization).getUserDTO();
			PfUsersDTO principal = (PfUsersDTO)((UserAuthentication) authorization).getPrincipal();
			
			boolean isPrincipalUser = user.getPrimaryKey().equals(principal.getPrimaryKey());
			boolean isAdministrator = userAdmBO.isAdministrator(user.getPfUsersProfiles());
			boolean isAdminSeat = userAdmBO.isAdminSeat(user.getPfUsersProfiles());
			boolean isAdminCAID = userAdmBO.isAdminCAID(user.getPfUsersProfiles());
			boolean hasAdminPermissions = isPrincipalUser && (isAdministrator || isAdminCAID);
			boolean hasUserManagementPermissions = isPrincipalUser && (isAdministrator || isAdminSeat || isAdminCAID);
			
			// se restringe el acceso de la parte de administración
			if(isAdministrationArea(request.getRequestURI()) && !(hasAdminPermissions)) {
				response.sendError(Constants.RESPONSE_FORBIDDEN);
			}
			
			// se restringe el acceso de la parte de gestión de usuarios
			if(isUserManagementArea(request.getRequestURI()) && !(hasUserManagementPermissions)) {
				response.sendError(Constants.RESPONSE_FORBIDDEN);
			}
			
			// se restringe el acceso de la parte de configuración
			if(isConfigurationArea(request.getRequestURI()) && !isPrincipalUser) {
				response.sendError(Constants.RESPONSE_FORBIDDEN);
			}
			
			if (isStatsArea (request.getRequestURI()) && !(hasUserManagementPermissions)) {
				response.sendError(Constants.RESPONSE_FORBIDDEN);
			}
		}
		return true;
	}

	/**
	 * Decide si la dirección visitada corresponde a la parte de configuración
	 * @param requestURI
	 * @return
	 */
	private boolean isConfigurationArea(String requestURI) {
		boolean isRestricted = false;
		if(requestURI.contains("/configuration")) {
			isRestricted = true;
		}
		return isRestricted;
	}

	/**
	 * Decide si la dirección visitada corresponde a la parte de administración
	 * @param requestURI
	 * @return
	 */
	private boolean isAdministrationArea(String requestURI) {
		boolean isRestricted = false;
		if(requestURI.contains("/administration")) {
			isRestricted = true;
		}
		return isRestricted;
	}
	
	/**
	 * Decide si la dirección visitada corresponde a la parte de gestión de usuarios
	 * @param requestURI
	 * @return
	 */
	private boolean isUserManagementArea(String requestURI) {
		boolean isRestricted = false;
		if(requestURI.contains("/usersManagement")) {
			isRestricted = true;
		}
		return isRestricted;
	}
	
	/**
	 * Decide si la dirección visitada corresponde a la parte de gestión de usuarios
	 * @param requestURI
	 * @return
	 */
	private boolean isStatsArea(String requestURI) {
		boolean isRestricted = false;
		if(requestURI.contains("/stats")) {
			isRestricted = true;
		}
		return isRestricted;
	}
	

	@Override
	public void postHandle(HttpServletRequest request,
			HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
	}
	
	@Override
	public void afterCompletion(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

}

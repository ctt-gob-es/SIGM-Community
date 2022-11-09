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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.PfApplicationsDTO;
import es.seap.minhap.portafirmas.domain.PfProvinceAdminDTO;
import es.seap.minhap.portafirmas.domain.PfProvinceDTO;
import es.seap.minhap.portafirmas.domain.PfRequestsDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.domain.PfUsersProfileDTO;
import es.seap.minhap.portafirmas.ws.bean.Authentication;
import es.seap.minhap.portafirmas.ws.exception.PfirmaException;


@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class AuthenticationWSHelper {


	private Logger log = Logger.getLogger(AuthenticationWSHelper.class);

	@Autowired
	private BaseDAO baseDAO;

	/**
	 * Recupera el usuario con el perfil de auntenticaci&oacute;n pasado como par&aacute;metro
	 * @param authentication el bean de autenticaci&oacute;n
	 * @return el usuario con el perfil de auntenticaci&oacute;n pasado como par&aacute;metro
	 * @throws PfirmaException si el usuario o la password no est&aacute;n en el objeto de autenticaci&oacute;n o
	 * si el usuario no existe o no es v&aacute;lido o no tiene el perfil requerido {@link es.seap.minhap.portafirmas.utils.Constants#C_PROFILES_WEBSERVICE}
	 */
	public PfUsersDTO authenticateWebservice(Authentication authentication, Set<String> perfilesNecesarios) throws PfirmaException {

		log.warn("PF authenticateWebservice init. UserName = " + authentication.getUserName());
		
		if (authentication.getUserName() == null || authentication.getUserName().equals("")) {
			throw new PfirmaException("UserName must be not-empty");
		}
		
		if (authentication.getPassword() == null || authentication.getPassword().equals("")) {
			throw new PfirmaException("Password must be not-empty");
		}		
		
		//Ponemos el nombre del usuario en may&uacute;sculas, ya que todos los nombres de usuario y de aplicaci&oacute;n son en may&uacute;sculas
		authentication.setUserName(authentication.getUserName().toUpperCase());
		
		Map<String, Object> parameters = new HashMap<String, Object>();
		parameters.put("dni", authentication.getUserName());
		parameters.put("password", authentication.getPassword());

		PfUsersDTO userDTO = (PfUsersDTO) baseDAO.queryElementMoreParametersWithOutLog("request.userProfilesDniPass", parameters);
		
		if (userDTO != null) {
			Boolean valido = userDTO.getLvalid(); //Comprobamos si el usuario est&aacute; vigente
			if (valido) {
				if (!tienePermisos(perfilesNecesarios, userDTO)) {
					imprimirError(authentication, perfilesNecesarios, userDTO);
					throw new PfirmaException("User has not required profiles");
				} else {
					chequeaAccesoUsuarioAplicacionLogin(authentication, userDTO);
				}
			} else {
				throw new PfirmaException("User is not valid");
			}
		} else {
			throw new PfirmaException("Unknown user");
		}
		
		return userDTO;		
	}	
	
	/**
	 * Se comprueba que el usuario tiene los permisos necesarios
	 * 
	 * @param perfilesNecesarios
	 * @param userDTO
	 * @return
	 */
	private boolean tienePermisos(Set<String> perfilesNecesarios, PfUsersDTO userDTO) {
		boolean tienePermisos = true;
		// se copian a un conjunto los códigos de los perfiles que posee el usuario
		Set<PfUsersProfileDTO> userProfiles = userDTO.getPfUsersProfiles();
		Set<String> perfilesUsuario = new HashSet<String>();
		for (PfUsersProfileDTO pfUsersProfileDTO: userProfiles) {
			perfilesUsuario.add(pfUsersProfileDTO.getPfProfile().getCprofile());
		}
		// se estudia si el conjunto de perfiles de usuario contiene todos los permisos necesarios
		for (Iterator<String> it = perfilesNecesarios.iterator(); it.hasNext();) {
			String perfilNecesario = (String) it.next();
			if (!perfilesUsuario.contains(perfilNecesario)) {
				tienePermisos = false;
			}
		}
		return tienePermisos;
	}

	/**
	 * Método para comentar cuando se descubra que ocurre con la aplicación DROGAS  
	 * 
	 * @param authentication
	 * @param perfilesNecesarios
	 * @param userDTO
	 * @throws PfirmaException
	 */
	private void imprimirError(Authentication authentication, Set<String> perfilesNecesarios, PfUsersDTO userDTO) throws PfirmaException {
		try {
			log.warn("authentication user/password: " + authentication.getUserName() + "/" + authentication.getPassword());
			log.warn("perfiles necesarios: ");
			for (Iterator<String> it = perfilesNecesarios.iterator(); it.hasNext();) {
				log.warn("\t" + (String) it.next());
			}
			log.warn("userDTO: " + userDTO.getCidentifier());
			log.warn("perfiles usuario: ");
			Set<PfUsersProfileDTO> userProfiles = userDTO.getPfUsersProfiles();
			for (PfUsersProfileDTO userProfile: userProfiles) {
				log.warn("\t" + userProfile.getPfProfile().getCprofile());
			}
		} catch (Exception e) {
			throw new PfirmaException("User has not required profiles. ¡¡Fallo en el método imprimirError!!");
		}
	}

	public void chequeaAccesoSede (PfUsersDTO userDTO, String seatCode) throws PfirmaException {
		if (!tieneAccesoSede (userDTO, seatCode)) {
			throw new PfirmaException ("No tiene permiso en la sede con codigo " + seatCode);
		}
	}
	
	/**
	 * Comprueba si un usuario tiene acceso a una sede
	 * @param userDTO usuario del que se quiere comprobar si tiene permisos sobre una sede
	 * @param seat sede
	 * @return
	 */
	private boolean tieneAccesoSede (PfUsersDTO userDTO, String seatCode) {
		Iterator<PfProvinceAdminDTO> adminProvs = userDTO.getPfUsersProvinces().iterator();
		boolean found = false;
		while (adminProvs.hasNext() && !found) {
			PfProvinceAdminDTO adminProv = adminProvs.next();
			PfProvinceDTO prov = adminProv.getPfProvince();
			if (prov.getCcodigoprovincia().contentEquals(seatCode)) {
				found = true;
			}
		}
		return found;
	}
	/**
	 * Comprueba si un usuario tiene acceso a la aplicaci&oacute;n con la que se ha autenticado
	 * 
	 * Basta con que tenga acceso a esa aplicaci&oacute;n, o a uno de sus hijos
	 * 
	 * @param authentication
	 * @param userDTO
	 * @throws PfirmaException
	 */
	private PfApplicationsDTO chequeaAccesoUsuarioAplicacionLogin(Authentication authentication, PfUsersDTO userDTO) throws PfirmaException {
		PfApplicationsDTO aplicacion = (PfApplicationsDTO) baseDAO.queryElementOneParameter("request.applicationId", "capp", authentication.getUserName());		
		if (aplicacion != null) {//La query s&oacute;lo devuelve un resultado.
			return aplicacion;
		} else {
			throw new PfirmaException("Application with name: '" + authentication.getUserName() + "', not found");
		}
		
		
	}	
	
	/**
	 * Comprueba si un usuario tiene acceso a la aplicaci&oacute;n a la que pertenece una petici&oacute;n
	 * 
	 * Basta con que la aplicaci&oacute;n en la que se autentica sea la misma de la petici&oacute;n o uno de sus padres
	 * 
	 * @param authentication
	 * @param userDTO
	 * @throws PfirmaException
	 */
	public void chequeaAccesoUsuarioPeticion(Authentication authentication, PfRequestsDTO requestsDTO) throws PfirmaException {
		chequeaAccesoUsuarioAplicacionPeticion(authentication, requestsDTO.getPfApplication());
	}
	
	/**
	 * Comprueba si un usuario tiene acceso a la aplicaci&oacute;n a la que pertenece una petici&oacute;n
	 * 
	 * Basta con que la aplicaci&oacute;n en la que se autentica sea la misma de la petici&oacute;n o uno de sus padres
	 * 
	 * @param authentication
	 * @param userDTO
	 * @throws PfirmaException
	 */
	public void chequeaAccesoUsuarioAplicacionPeticion(Authentication authentication, PfApplicationsDTO aplicacionPeticion) throws PfirmaException {
		boolean usuarioPerteneceAplicacion = chequeaAccesoUsuarioAplicacion(authentication, aplicacionPeticion, true);
		if (!usuarioPerteneceAplicacion) {
			throw new PfirmaException("User has no permission to see requests of application " + aplicacionPeticion.getCapplication());
		}
	}	
	
	/**
	 * Comprueba si el usuario, con los datos de autenticaci&oacute;n enviados, tiene acceso a una aplicaci&oacute;n determinada, o a alguno de sus hijos
	 * 
	 * @param authentication Par&aacute;metros de autenticaci&oacute;n utilizados por el usuario
	 * @param aplicacion Aplicaci&oacute;n a la que el usuario tiene acceso
	 * @param comprobarAplicacionesHijo Cuando vale true, y el nombre de las aplicaciones no coincide, se comprueban las aplicaciones hijo del parametro aplicacion  
	 * @param comprobarAplicacionPadre Cuando vale true, y el nombre de las aplicaciones no coincide, se comprueba la aplicaci&oacute;n padre del parametro aplicacion
	 * @return
	 */
	private boolean chequeaAccesoUsuarioAplicacion(Authentication authentication, PfApplicationsDTO aplicacion, boolean comprobarAplicacionPadre) {
		
		boolean tienePermiso = false;
		String nombreAplicacion = aplicacion.getCapplication(); 
		
		if (nombreAplicacion.equals(authentication.getUserName())) {
			//Si el nombre de la aplicaci&oacute;n coincide con el de la aplicaci&oacute;n del usuario, devolvemos true
			tienePermiso = true;
		}
		if (comprobarAplicacionPadre && aplicacion.getPfApplication() != null && aplicacion.getPfApplication().getPrimaryKey() != null) {
			//Comprobamos el padre, y a su vez si tiene padre
			tienePermiso |= chequeaAccesoUsuarioAplicacion(authentication, aplicacion.getPfApplication(), true);
		}
		return tienePermiso;
	}
	
}

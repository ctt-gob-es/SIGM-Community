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

import java.util.Collection;
import java.util.Set;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import es.seap.minhap.portafirmas.domain.PfGroupsDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.utils.Constants;

/**
 * Clase que extiende el token de autenticación UsernamePassword.
 * El atributo "principal" se usa para almacenar el usuario original autenticado.
 * El atributo "userDTO", se usa para almacenar, o bien el usuario original en caso
 * de no entrar como validador de alguien, o bien el usuario al que se está validando. Es decir, el usuario
 * al que estás interpretando.
 *  Se incluye el objeto PfUserDTO
 * con la información del usuario en el sistema
 * @author hugo
 *
 */
public class UserAuthentication extends UsernamePasswordAuthenticationToken {

	private static final long serialVersionUID = 1L;

	// Usuario autenticado
	private PfUsersDTO userDTO;
	
	//usuario original
	private PfUsersDTO userOriginal;

	// Número de serie del certificado del usuario autenticado
	private String serialNumber;

	// Lista con los usuarios validados por el usuario autenticado
	private Set<PfUsersDTO> validatedUsers;
	
	// Lista con los usuarios gestionados por el usuario autenticado
	private Set<PfUsersDTO> gestionatedUsers;
	
	// Grupo para el que se autentica, en su caso
	private PfGroupsDTO group;
	
	//flag que indica si el usuario de autenticacion es simulado.
	//por defecto false, se activa por set.
	private boolean isUserSimulado=false;
	

	public UserAuthentication(Object principal, Object credentials,
			Collection<? extends GrantedAuthority> authorities) {
		super(principal, credentials, authorities);
	}

	
	public UserAuthentication(Object principal, Object credentials,
			Collection<? extends GrantedAuthority> authorities,
			PfUsersDTO userDTO, String serialNumber, PfUsersDTO userOriginal) {
		super(principal, credentials, authorities);
		this.userDTO = userDTO;
		this.serialNumber = serialNumber;
		this.userOriginal=userOriginal;

		try {
			this.validatedUsers = ((PfUsersDTO) principal).getValidadorDe();
			this.gestionatedUsers = ((PfUsersDTO) principal).getGestorDe();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

//	public UserAuthentication(Object principal, Object credentials,
//			Collection<? extends GrantedAuthority> authorities,
//			PfUsersDTO userDTO, String serialNumber, Set<PfUsersDTO> validatedUsers) {
//		this(principal, credentials, authorities, userDTO, serialNumber);
//		this.validatedUsers = validatedUsers;
//	}

//	public UserAuthentication(Object principal, Object credentials,
//			Collection<? extends GrantedAuthority> authorities,
//			PfUsersDTO userDTO, String serialNumber, Set<PfUsersDTO> validatedUsers, PfGroupsDTO group) {
//		this(principal, credentials, authorities, userDTO, serialNumber, validatedUsers);
//		this.group = group;
//	}
	
	public UserAuthentication(Object principal, Object credentials,
		Collection<? extends GrantedAuthority> authorities,
		PfUsersDTO userDTO, String serialNumber, PfGroupsDTO group, PfUsersDTO userOriginal) {
		this(principal, credentials, authorities, userDTO, serialNumber,userOriginal);
		this.group = group;
	}

	public PfUsersDTO getUserDTO() {
		return userDTO;
	}

	public void setUserDTO(PfUsersDTO userDTO) {
		this.userDTO = userDTO;
	}

	public String getSerialNumber() {
		return serialNumber;
	}

	public void setSerialNumber(String serialNumber) {
		this.serialNumber = serialNumber;
	}

	public Set<PfUsersDTO> getValidatedUsers() {
		return validatedUsers;
	}

	public void setValidatedUsers(Set<PfUsersDTO> validatedUsers) {
		this.validatedUsers = validatedUsers;
	}
	
	public PfGroupsDTO getGroup() {
		return group;
	}

	public void setGroup(PfGroupsDTO group) {
		this.group = group;
	}
	
	public PfUsersDTO getUserOriginal() {
		return userOriginal;
	}


	public void setUserOriginal(PfUsersDTO userOriginal) {
		this.userOriginal = userOriginal;
	}	
	
	public boolean isUserSimulado() {
		return isUserSimulado;
	}


	public void setUserSimulado(boolean isUserSimulado) {
		this.isUserSimulado = isUserSimulado;
	}	

	public boolean isAdminSeat() {
		boolean isAdminSeat = false;
		for(GrantedAuthority authority : getAuthorities()) {
			if(Constants.C_PROFILES_ADMIN_PROVINCE.equals(authority.getAuthority())) {
				isAdminSeat = true;
			}
		}
		return isAdminSeat;
	}
	
	public boolean isAdministrator() {
		boolean isAdministrator = false;
		for(GrantedAuthority authority : getAuthorities()) {
			if(Constants.C_PROFILES_ADMIN.equals(authority.getAuthority())) {
				isAdministrator = true;
			}
		}
		return isAdministrator;
	}
	
	public boolean isAdministratorOrganism() {
		boolean isAdministrator = false;
		for(GrantedAuthority authority : getAuthorities()) {
			if(Constants.C_PROFILES_ADMIN_ORGANISM.equals(authority.getAuthority())) {
				isAdministrator = true;
			}
		}
		return isAdministrator;
	}
	
	public boolean isAdministratorCAID() {
		boolean isAdministratorCaid = false;
		for(GrantedAuthority authority : getAuthorities()) {
			if(Constants.C_PROFILES_ADMIN_CAID.equals(authority.getAuthority())) {
				isAdministratorCaid = true;
			}
		}
		return isAdministratorCaid;
	}
	
	public boolean isSimulador() {
		boolean isSimulador = false;
		for(GrantedAuthority authority : getAuthorities()) {
			if(Constants.C_PROFILES_SIMULATE.equals(authority.getAuthority())) {
				isSimulador = true;
			}
		}
		return isSimulador;
	}
	
	public boolean isGestorLdap() {
		boolean isGestorLdap = false;
		if (getUserDTO()!=null && getUserDTO().getPfProvince() !=null &&  getUserDTO().getPfProvince().getLLdap() != null && getUserDTO().getPfProvince().getLLdap()){
			isGestorLdap= true;
		}
		return isGestorLdap;
	}

	public Set<PfUsersDTO> getGestionatedUsers() {
		return gestionatedUsers;
	}

	public void setGestionatedUsers(Set<PfUsersDTO> gestionatedUsers) {
		this.gestionatedUsers = gestionatedUsers;
	}

}

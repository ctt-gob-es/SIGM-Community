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

package es.seap.minhap.interfazGenerica.domain;

import java.util.Date;

import javax.persistence.Column;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;

/**
 *@
 */
public abstract class AbstractBaseDTO implements java.io.Serializable, Cloneable {

	protected static final long serialVersionUID = 1L;

	protected boolean selected; 
	protected boolean updated;

	private String cCreado;
	private Date fCreado;
	private String cModificado;
	private Date fModificado;

	public AbstractBaseDTO() {
		this.selected = false;
	}

	public boolean isUpdated() {
		return updated;
	}

	public void setUpdated(boolean updated) {
		this.updated = updated;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	@Column(name = "C_CREADO", nullable=false)
	public String getcCreado() {
		return cCreado;
	}

	public void setcCreado(String cCreado) {
		this.cCreado = cCreado;
	}

	@Column(name = "F_CREADO", nullable=false)
	public Date getfCreado() {
		return fCreado;
	}

	public void setfCreado(Date fCreado) {
		this.fCreado = fCreado;
	}

	@Column(name = "C_MODIFICADO", nullable=false)
	public String getcModificado() {
		return cModificado;
	}

	public void setcModificado(String cModificado) {
		this.cModificado = cModificado;
	}

	@Column(name = "F_MODIFICADO", nullable=false)
	public Date getfModificado() {
		return fModificado;
	}

	public void setfModificado(Date fModificado) {
		this.fModificado = fModificado;
	}

	/**
	 *@
	 */
	public void createAuditing() {
		String userId = authenticatedUserId ();
		this.setcCreado(userId);
		this.setcModificado(userId);
		this.setfCreado(new Date());
		this.setfModificado(new Date());
	}

	public void updateAuditing() {
		
		String userId = authenticatedUserId ();
		this.setcModificado(userId);
		this.setfModificado(new Date());
	}

	/**
	 * Devuelve el identificador asociado al usuario autenticado en la aplicación, salvo si fuera
	 * una petición de WS, que devolverá "WS".
	 * @return
	 */
	private String authenticatedUserId () {
		String idAuthenticated = null;
		PfUsersDTO authenticatedUser = authenticatedUser();
		if (authenticatedUser != null) {
			idAuthenticated = authenticatedUser.getPrimaryKeyString();
		} else {
			idAuthenticated = "WS";
		}
		return idAuthenticated;
	}
	
	/**
	 * Devuelve el DTO asociado al usuario autenticado.
	 * Si se entrara en este método por una petición de WS, entonces devolverá null.
	 * @return
	 */
	private PfUsersDTO authenticatedUser () {
		
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		PfUsersDTO user = null;
		// Si es un usuario autenticado por la interfaz web, el objeto de autenticación será de tipo UserAuthentication
		// Si se entra aquí por una petición WS, el objeto de autenticación será AnonymousAuthenticationToken.
		if (authentication instanceof UserAuthentication) {
			UserAuthentication pfUserAuthentication =  (UserAuthentication) authentication;
			user = pfUserAuthentication.getUserDTO();
		}
		return user;
	}

}

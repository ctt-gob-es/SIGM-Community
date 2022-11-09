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

package es.seap.minhap.portafirmas.domain;

import java.util.Date;

import javax.persistence.Transient;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import es.seap.minhap.portafirmas.security.authentication.UserAuthentication;

/**
 *@
 */
public abstract class AbstractBaseDTO implements java.io.Serializable, Cloneable {

	protected static final long serialVersionUID = 5340696721695385233L;

	protected boolean selected; 

	protected boolean updated;

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

	/**
	 *@
	 */
	public void createAuditing() {
		String userId = authenticatedUserId ();
		this.setCcreated(userId);
		this.setCmodified(userId);
		this.setFcreated(new Date());
		this.setFmodified(new Date());
	}

	public void updateAuditing() {
		
		String userId = authenticatedUserId ();
		this.setCmodified(userId);
		this.setFmodified(new Date());
	}

	public abstract void setCcreated(String ccreated);

	public abstract void setFcreated(Date fcreated);

	public abstract void setCmodified(String cmodified);

	public abstract void setFmodified(Date fmodified);

	public abstract String getCcreated();

	public abstract Date getFcreated();

	public abstract String getCmodified();

	public abstract Date getFmodified();

	public abstract Long getPrimaryKey();

	public abstract void setPrimaryKey(Long primaryKey);

	public String getPrimaryKeyString() {
		return String.valueOf(this.getPrimaryKey());
	}

	public void setPrimaryKeyString(String primaryKeyString) {
		if (primaryKeyString != null) {
			this.setPrimaryKey(Long.parseLong(primaryKeyString));
		}
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

	@Transient
	public Object clone(){
        Object obj=null;
        try{
            obj=super.clone();
        } catch(CloneNotSupportedException ex){
        	System.out.println("No se puede duplicar");
        }
        return obj;
    }
	
}

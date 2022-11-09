package es.gob.afirma.signfolder.client;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Clase que representa la petición cliente para el servicio "createRole".
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "id", "userId", "selectedRole", "authInfo", "appIds" })
@XmlRootElement(name = "createRoleRequest")
public class CreateRoleRequest {

	/**
	 * Atributo que representa el identificador de usuario.
	 */
	@XmlElement(required = true)
	private byte[] id;

	/**
	 * Atributo que representa el identificador del rol.
	 */
	@XmlElement(required = true)
	private String userId;

	/**
	 * Atributo que representa el tipo de rol seleccionado.
	 */
	@XmlElement(required = true)
	private String selectedRole;

	/**
	 * Atributo que representa la información asociada a la autorización.
	 */
	@XmlElement(required = false)
	private AuthorizationInfo authInfo;

	/**
	 * Atributo que representa la lista de identificadores de aplicaciones del
	 * validador.
	 */
	@XmlElement(required = false)
	private List<String> appIds;

	/**
	 * @return the id
	 */
	public byte[] getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(byte[] id) {
		this.id = id;
	}

	/**
	 * @return the userId
	 */
	public String getUserId() {
		return userId;
	}

	/**
	 * @param userId the userId to set
	 */
	public void setUserId(String userId) {
		this.userId = userId;
	}

	/**
	 * @return the selectedRole
	 */
	public String getSelectedRole() {
		return selectedRole;
	}

	/**
	 * @param selectedRole the selectedRole to set
	 */
	public void setSelectedRole(String selectedRole) {
		this.selectedRole = selectedRole;
	}

	/**
	 * @return the authInfo
	 */
	public AuthorizationInfo getAuthInfo() {
		return authInfo;
	}

	/**
	 * @param authInfo the authInfo to set
	 */
	public void setAuthInfo(AuthorizationInfo authInfo) {
		this.authInfo = authInfo;
	}

	/**
	 * @return the appIds
	 */
	public List<String> getAppIds() {
		return appIds;
	}

	/**
	 * @param appIds the appIds to set
	 */
	public void setAppIds(List<String> appIds) {
		this.appIds = appIds;
	}

}

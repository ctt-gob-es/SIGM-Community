package es.gob.afirma.signfolder.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Clase que representa la respuesta para el servicio "getUser".
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getUserResponse", namespace = "urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0", propOrder = {
		"userListResult" })
public class GetUserResponse {

	/**
	 * Atributo que representa la lista de usuarios de la respuesta.
	 */
	@XmlElement(required = true)
	UserList userListResult;

	/**
	 * @return the userListResult
	 */
	public UserList getUserListResult() {
		return userListResult;
	}

	/**
	 * @param userListResult
	 *            the userListResult to set
	 */
	public void setUserListResult(UserList userListResult) {
		this.userListResult = userListResult;
	}

}

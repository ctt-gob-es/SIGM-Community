package es.gob.afirma.signfolder.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Clase que representa la respuesta del servicio "getUserByRole".
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getUserByRoleResponse", namespace = "urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0", propOrder = {
		"users" })
public class GetUserByRoleResponse {
	
	/**
	 * Atributo que representa la lista de usuario de la respuesta.
	 */
	@XmlElement(required = true)
	UserList users;

	/**
	 * Get method that gets the <i>users</i> attribute.
	 * @return the attribute value.
	 */
	public UserList getUsers() {
		return users;
	}

	/**
	 * Set method that sets the <i>users</i> attribute value.
	 * @param users New value of the attribute.
	 */
	public void setUsers(UserList users) {
		this.users = users;
	}
	
}

package es.gob.afirma.signfolder.client;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import es.gob.afirma.signfolder.server.proxy.User;

/**
 * Clase que representa la lista de usuarios del portafirmas-web.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "userList", namespace = "urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0", propOrder = { "users" })
public class UserList {
	
	/**
	 * Atributo que representa la lista de usuarios recibida del portafirmas-web.
	 */
	private List<User> users;

	/**
	 * Get method of the <i>users</i> attribute.
	 * @return the value of the attribute.
	 */
	public List<User> getUsers() {
		return users;
	}

}

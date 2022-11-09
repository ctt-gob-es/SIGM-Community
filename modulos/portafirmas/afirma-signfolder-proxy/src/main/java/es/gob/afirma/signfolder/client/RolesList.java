package es.gob.afirma.signfolder.client;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import es.gob.afirma.signfolder.server.proxy.Role;

/**
 * Clase que representa la lista de roles recibidas de portafirmas-web.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "rolesList", namespace = "urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0", propOrder = { "roles" })
public class RolesList {
	
	/**
	 * Atributo que representa la lista de roles recibida del portafirmas-web.
	 */
	private List<Role> roles;

	/**
	 * Get method of the <i>roles</i> attribute.
	 * @return the value of the attribute.
	 */
	public List<Role> getRoles() {
		return roles;
	}

}

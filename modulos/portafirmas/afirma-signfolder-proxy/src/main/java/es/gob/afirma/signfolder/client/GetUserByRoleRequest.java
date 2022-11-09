package es.gob.afirma.signfolder.client;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

/**
 * Clase que representa la petición para el servicio "getUserByRole".
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = { "role", "id" })
@XmlRootElement(name = "getUserByRoleRequest")
public class GetUserByRoleRequest {

	/**
	 * Atributo que representa el rol solicitado.
	 */
	@XmlElement(required = true)
	private String role;
	
	/**
	 * Atributo que representa el identificador de usuario.
	 */
    @XmlElement(required = true)
    private byte[] id;

	/**
	 * Get method that gets the <i>role</i> attribute.
	 * @return the attribute value.
	 */
	public String getRole() {
		return role;
	}

	/**
	 * Set method that sets the <i>role</i> attribute value.
	 * @param role New value of the attribute.
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * Get method that gets the <i>id</i> attribute.
	 * @return the attribute value.
	 */
	public byte[] getId() {
		return id;
	}

	/**
	 * Set method that sets the <i>id</i> attribute value.
	 * @param id New value of the attribute.
	 */
	public void setId(byte[] id) {
		this.id = id;
	}
	
}

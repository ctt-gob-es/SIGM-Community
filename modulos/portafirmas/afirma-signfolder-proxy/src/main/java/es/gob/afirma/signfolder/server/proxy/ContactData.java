package es.gob.afirma.signfolder.server.proxy;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

/**
 * Clase que representa los datos de contacto del usuario.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "contactData", namespace = "urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0", propOrder = { "email",
		"notify" })
public class ContactData implements Serializable {

	/**
	 * serial version UID.
	 */
	private static final long serialVersionUID = 4431854138979408319L;

	/**
	 * Parámetro que representa el email.
	 */
	private String email;

	/**
	 * Bandera que indica si las notificaciones serán enviadas al email proporcionado.
	 */
	private boolean notify;

	/**
	 * Get method for the <i>email</i> attribute.
	 * 
	 * @return the value of the attribute.
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * Set method for the <i>email</i> attribute.
	 * 
	 * @param email
	 *            New value of the contact email.
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Get method for the <i>notify</i> attribute.
	 * 
	 * @return the value of the attribute.
	 */
	public boolean isNotify() {
		return notify;
	}

	/**
	 * Set method for the <i>notify</i> attribute.
	 * 
	 * @param notify
	 *            New value of the notify flag.
	 */
	public void setNotify(boolean notify) {
		this.notify = notify;
	}
}

package es.gob.afirma.signfolder.client;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

import es.gob.afirma.signfolder.server.proxy.UserProfile;

/**
 * Clase que representa el perfil de usuario recibido del portafirmas-web.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "userProfileList", namespace = "urn:juntadeandalucia:cice:pfirma:mobile:type:v2.0", propOrder = { "userProfiles" })
public class UserProfileList {
	
	/**
	 * Atributo que representa la lista de perfiles.
	 */
	private List<UserProfile> userProfiles;

	/**
	 * Get method of the <i>userProfiles</i> attribute.
	 * @return the attribute value.
	 */
	public List<UserProfile> getUserProfiles() {
		return userProfiles;
	}
	
}

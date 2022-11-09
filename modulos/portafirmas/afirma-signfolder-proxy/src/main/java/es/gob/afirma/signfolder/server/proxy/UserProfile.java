package es.gob.afirma.signfolder.server.proxy;

import java.io.Serializable;

/**
 * Enumerado que define el tipo de perfil de usuario.
 */
public enum UserProfile implements Serializable {
	ACCESO("ACCESO"), ADMIN("ADMIN"), ADMINCAID("ADMINCAID"), ADMIN_ORG("ADMIN_ORG"), ADMINPROV("ADMINPROV"), FIRMA(
			"FIRMA"), REDACCION("REDACCION");

	/**
	 * Atributo que define el valor del tipo de perfil.
	 */
	private String value;

	/**
	 * Constructor por defecto.
	 * 
	 * @param value
	 *            Valor del perfil de usuario.
	 */
	UserProfile(String value) {
		this.value = value;
	}

	/**
	 * Método que transforma una cadena de entrada en un enumerado de tipo
	 * UserProfile.
	 * 
	 * @param value
	 *            Valor a proporcionar al objeto.
	 * @return un objeto de tipo UserProfile o null si el valor del parámetro de
	 *         entrada no es válido.
	 */
	public static UserProfile getUserProfile(String value) {
		String val = value.toUpperCase();
		switch (val) {
		case "ACCESO":
			return UserProfile.ACCESO;
		case "ADMIN":
			return UserProfile.ADMIN;
		case "ADMINCAID":
			return UserProfile.ADMINCAID;
		case "ADMIN_ORG":
			return UserProfile.ADMIN_ORG;
		case "ADMINPROV":
			return UserProfile.ADMINPROV;
		case "FIRMA":
			return UserProfile.FIRMA;
		case "REDACCION":
			return UserProfile.REDACCION;
		default:
			return null;

		}
	}

	/**
	 * Get method for the <i>value</i> attribute.
	 * 
	 * @return the attribute value.
	 */
	public String getValue() {
		return value;
	}

}

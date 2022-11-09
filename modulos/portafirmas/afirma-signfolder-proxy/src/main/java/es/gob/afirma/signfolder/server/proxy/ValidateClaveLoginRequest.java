package es.gob.afirma.signfolder.server.proxy;

/**
 * Datos obtenidos de una petici&oacute;n de validaci&oacute;n de acceso con Cl@ve.
 * @author Carlos Gamuci
 */
public class ValidateClaveLoginRequest {

	private String saml;

	private String redirectionUrl;

	/**
	 * Establece la firma de la validaci&oacute;n.
	 * @param saml Firma de acceso.
	 */
	public void setSaml(final String saml) {
		this.saml = saml;
	}

	/**
	 * Recupera la firma de la validaci&oacute;n.
	 * @return Firma de acceso.
	 */
	public String getSaml() {
		return this.saml;
	}

	/**
	 * Establece URL a la que redirigir tras comprobar la validaci&oacute;n del usuario.
	 * @param redirectionUrl URL del proxy.
	 */
	public void setRedirectionUrl(final String redirectionUrl) {
		this.redirectionUrl = redirectionUrl;
	}

	/**
	 * Recupera la URL a la que redirigir tras comprobar la validaci&oacute;n del usuario.
	 * @return URL del proxy.
	 */
	public String getRedirectionUrl() {
		return this.redirectionUrl;
	}
}

package es.gob.afirma.signfolder.server.proxy;

/**
 * Resultado de una operaci&oacute;n de registro de un dispositivo en el sistema de notificaciones.
 * @author Carlos Gamuci
 */
class NotificationRegistryResult {

	private final boolean registered;

	private String errorDetails = null;

	/**
	 * Crea el resultado del proceso de registro.
	 * @param statusCode C&oacute;digo del resultado obtenido.
	 * @param statusText Texto asociado al resultado.
	 */
	public NotificationRegistryResult(final String statusCode, final String statusText) {

		if(statusCode.equals("0")) {
			this.registered = true;	//TODO: Determinar en base a los parametros de entrada
		}
		else {
			this.registered = false;
		}
	}

	/**
	 * Establece los detalles del error en la operaci&oacute;n de registro.
	 * @param errorDetails Texto con los detalles.
	 */
	public void setErrorDetails(final String errorDetails) {
		this.errorDetails = errorDetails;
	}

	String getError() {
		return this.errorDetails;
	}

	boolean isRegistered() {
		return this.registered;
	}
}

package es.gob.afirma.signfolder.server.proxy;

/**
 * Petici&oacute;n del detalle de una solicitud de firma.
 */
public class DetailRequest {

	private final String id;

	/**
	 * Construye una petici&oacute;n de una solicitud de firma.
	 * @param certEncoded Certificado codificado para autenticar la petici&oacute;n.
	 * @param id Identificador de la petici&oacute;n.
	 */
	public DetailRequest(final String id) {
		this.id = id;
	}

	/**
	 * Recupera el identificador de la petici&oacute;n solicitada.
	 * @return Identificador.
	 */
	public String getRequestId() {
		return this.id;
	}
}

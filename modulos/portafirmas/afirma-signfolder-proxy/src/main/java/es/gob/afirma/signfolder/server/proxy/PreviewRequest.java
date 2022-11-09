package es.gob.afirma.signfolder.server.proxy;

/**
 * Petici&oacute;n para la previsualizaci&oacute;n de un documento.
 */
public class PreviewRequest {

	private final String docId;

	/**
	 * Construye la petici&oacute;n para la previsualizaci&oacute;n del documento.
	 * @param docId Identificador del documento.
	 * @param certEncoded Certificado codificado con el que realizar la autenticaci&oacute;n de la petici&oacute;n.
	 */
	public PreviewRequest(final String docId) {
		this.docId = docId;
	}

	/**
	 * Recupera el identificador del documento.
	 * @return Identificador.
	 */
	public String getDocId() {
		return this.docId;
	}
}

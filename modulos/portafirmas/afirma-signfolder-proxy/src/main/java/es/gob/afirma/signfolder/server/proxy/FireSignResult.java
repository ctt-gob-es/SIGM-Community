package es.gob.afirma.signfolder.server.proxy;

/** Resultado final de una operaci&oacute;n de firma con FIRe. */
public class FireSignResult {

	static final int ERROR_TYPE_COMMUNICATION = 1;

	static final int ERROR_TYPE_REQUEST = 2;

	static final int ERROR_TYPE_DOCUMENT = 3;

	/** Referencia de la transacci&oacute;n de FIRe. */
	private String transactionId;

	/** Indica si se ha producido un error durante la operaci&oacute;n. */
	private boolean error = false;

	/** Tipo de error  */
	private int errorType = 0;

	/**
	 * Construye el resultado de una petici&oacute;n de firma a FIRe.
	 * @param transactionId Identificador de la transacci&oacute;n de FIRe.
	 */
	public FireSignResult(final String transactionId) {
		this.transactionId = transactionId;
	}

	/**
	 * Construye un resultado de error.
	 * @param errorType Tipo de error.
	 */
	public FireSignResult(final int errorType) {
		setErrorType(errorType);
	}

	/**
	 * Establece el identificador de la transacci&oacute;n de FIRe en la que se realiza la operaci&oacute;n de firma
	 * de las peticiones.
	 * @param transactionId Identificador de transacci&oacute;n de firma con FIRe.
	 */
	public void setTransactionId(final String transactionId) {
		this.transactionId = transactionId;
	}

	/**
	 * Define que se produjo un error y el tipo del mismo.
	 * @param errorType Tipo de error.
	 */
	public void setErrorType(final int errorType) {
		this.error = true;
		this.errorType = errorType;
	}

	/**
	 * Recupera la referencia de la transaccion de FIRe para la firma de documentos.
	 * @return Identificador de la transacci&oacute;n.
	 */
	public String getTransactionId() {
		return this.transactionId;
	}

	/**
	 * Indica si se notific&oacute; un error en la petici&oacute;n.
	 * @return {@code true} si se produjo un error, {@code false} en caso contrario.
	 */
	public boolean isError() {
		return this.error;
	}

	/**
	 * Recupera el tipo de error recibido.
	 * @return Tipo de error.
	 */
	public int getErrorType() {
		return this.errorType;
	}
}

package es.sigem.dipcoruna.desktop.asyncUploader.service.exception;

/**
 * Excepcion generica del servicio de FTP
 */
public class StateServiceException extends Exception {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -6665663697203785538L;

	public StateServiceException() {
		super();
	}

	public StateServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public StateServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public StateServiceException(String message) {
		super(message);
	}

	public StateServiceException(Throwable cause) {
		super(cause);
	}

}

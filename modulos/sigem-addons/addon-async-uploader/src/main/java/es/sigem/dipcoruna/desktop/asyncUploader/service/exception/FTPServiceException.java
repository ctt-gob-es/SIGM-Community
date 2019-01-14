package es.sigem.dipcoruna.desktop.asyncUploader.service.exception;

/**
 * Excepcion generica del servicio de FTP
 */
public class FTPServiceException extends Exception {

	/**
	 * Generated serialVersionUID
	 */
	private static final long serialVersionUID = -6665663697203785538L;

	public FTPServiceException() {
		super();
	}

	public FTPServiceException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public FTPServiceException(String message, Throwable cause) {
		super(message, cause);
	}

	public FTPServiceException(String message) {
		super(message);
	}

	public FTPServiceException(Throwable cause) {
		super(cause);
	}

}

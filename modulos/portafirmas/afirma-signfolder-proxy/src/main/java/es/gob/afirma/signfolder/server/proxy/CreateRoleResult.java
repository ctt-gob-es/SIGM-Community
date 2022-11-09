package es.gob.afirma.signfolder.server.proxy;

/**
 * Clase que representa el objeto asociado al resultado de la petición de
 * creación de roles.
 */
public class CreateRoleResult {

	static final int ERROR_TYPE_COMMUNICATION = 1;
	static final int ERROR_TYPE_REQUEST = 2;
	static final int ERROR_TYPE_DOCUMENT = 3;

	/**
	 * Bandera que indica si la operación se ha realizado correctamente
	 * <i>true</i> o no <i>false</i>.
	 */
	private boolean success;

	/**
	 * Indica si se ha producido un error durante la operación.
	 */
	private int errorType;
	
	/**
	 * Constructor.
	 * @param success Bandera que indica si el resultado ha sido correcto.
	 */
	public CreateRoleResult(boolean success) {
		super();
		this.success = success;
	}

	/**
	 * Constructor.
	 * @param errorType Número que indica el tipo de error producido.
	 */
	public CreateRoleResult(int errorType) {
		super();
		this.errorType = errorType;
	}



	/**
	 * @return the success
	 */
	public boolean isSuccess() {
		return success;
	}

	/**
	 * @param success the success to set
	 */
	public void setSuccess(boolean success) {
		this.success = success;
	}

	/**
	 * @return the errorType
	 */
	public int getErrorType() {
		return errorType;
	}

	/**
	 * @param errorType the errorType to set
	 */
	public void setErrorType(int errorType) {
		this.errorType = errorType;
	}
	
	
}

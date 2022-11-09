package es.gob.afirma.signfolder.server.proxy;

/**
 * Clase que representa el objeto asociado al resultado de la petición de
 * validación de peticiones.
 */
public class VerifyPetitionsResult {

	static final int ERROR_TYPE_COMMUNICATION = 1;
	static final int ERROR_TYPE_REQUEST = 2;
	static final int ERROR_TYPE_DOCUMENT = 3;

	/**
	 * Bandera que indica si la operación se ha realizado correctamente
	 * <i>true</i> o no <i>false</i>.
	 */
	private boolean result;

	/**
	 * Indica si se ha producido un error durante la operación.
	 */
	private int errorType;

	public VerifyPetitionsResult(boolean resultParam) {
		this.result = resultParam;
	}

	public VerifyPetitionsResult(int errorParam) {
		this.errorType = errorParam;
	}

	/**
	 * @return the result
	 */
	public boolean isResult() {
		return result;
	}

	/**
	 * @param result
	 *            the result to set
	 */
	public void setResult(boolean result) {
		this.result = result;
	}

	/**
	 * @return the errorType
	 */
	public int getErrorType() {
		return errorType;
	}

	/**
	 * @param errorType
	 *            the errorType to set
	 */
	public void setErrorType(int errorType) {
		this.errorType = errorType;
	}

}

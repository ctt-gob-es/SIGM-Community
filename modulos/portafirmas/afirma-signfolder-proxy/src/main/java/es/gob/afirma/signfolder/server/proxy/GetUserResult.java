package es.gob.afirma.signfolder.server.proxy;

import java.util.List;

/**
 * Clase que representa el resultado del servicio "getUser".
 */
public class GetUserResult {
	
	static final int ERROR_TYPE_COMMUNICATION = 1;

	static final int ERROR_TYPE_REQUEST = 2;

	static final int ERROR_TYPE_DOCUMENT = 3;
	
	/** 
	 * Lista de usuarios de la respuesta del servicio. 
	 */
	private List<User> users;

	/** 
	 * Indica si se ha producido un error durante la operación. 
	 */
	private boolean error = false;

	/** 
	 * Tipo de error.
	 */
	private int errorType = 0;
	
	/**
	 * Constructor for the success cases.
	 * @param users List of users.
	 */
	public GetUserResult(final List<User> users) {
		this.users = users;
	}

	/**
	 * Constructor for the fails cases.
	 * @param errorType Error type.
	 */
	public GetUserResult(final int errorType) {
		setErrorType(errorType);
	}

    /**
     * Get method for the <i>users</i> attribute.
     * @return the value of the attribute.
     */
	public List<User> getUsers() {
		return users;
	}

    /**
     * Set method for the <i>users</i> attribute.
     * @param users new value of the attribute.
     */
	public void setUsers(List<User> users) {
		this.users = users;
	}

    /**
     * Get method for the <i>error</i> attribute.
     * @return the value of the attribute.
     */
	public boolean isError() {
		return error;
	}

    /**
     * Set method for the <i>error</i> attribute.
     * @param error new value of the attribute.
     */
	public void setError(boolean error) {
		this.error = error;
	}

    /**
     * Get method for the <i>errorType</i> attribute.
     * @return the value of the attribute.
     */
	public int getErrorType() {
		return errorType;
	}

    /**
     * Set method for the <i>errorType</i> attribute.
     * @param errorType new value of the attribute.
     */
	public void setErrorType(int errorType) {
		this.errorType = errorType;
	}

}

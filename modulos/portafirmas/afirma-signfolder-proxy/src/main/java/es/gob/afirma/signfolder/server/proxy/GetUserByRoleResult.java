package es.gob.afirma.signfolder.server.proxy;

import java.util.List;

/**
 * Clase que representa el resultado del servicio "getUserByRole".
 */
public class GetUserByRoleResult {

	static final int ERROR_TYPE_COMMUNICATION = 1;

	static final int ERROR_TYPE_REQUEST = 2;

	static final int ERROR_TYPE_DOCUMENT = 3;
	
	/** 
	 * Lista de roles de la respuesta. 
	 */
	private List<Role> roles;

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
	 * @param roles List of users.
	 */
	public GetUserByRoleResult(final List<Role> roles) {
		this.roles = roles;
	}

	/**
	 * Constructor for the fails cases.
	 * @param errorType Error type.
	 */
	public GetUserByRoleResult(final int errorType) {
		setErrorType(errorType);
	}

    /**
     * Get method for the <i>roles</i> attribute.
     * @return the value of the attribute.
     */
	public List<Role> getRoles() {
		return roles;
	}

    /**
     * Set method for the <i>roles</i> attribute.
     * @param roles new value of the attribute.
     */
	public void setUsers(List<Role> roles) {
		this.roles = roles;
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

package es.ieci.tecdoc.fwktd.dir3.exception;

import es.ieci.tecdoc.fwktd.core.exception.ApplicationException;

/**
 * Control de excepciones de los servicios de inicialización
 */
public class ObtencionFicheroInicializacionDCOException extends
		ApplicationException {

	/**
	 * Constructor
	 * @param msg
	 */
	public ObtencionFicheroInicializacionDCOException(String msg) {
		super(msg);
	}

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Sobreescribe el método
	 * @return Mensaje el mensaje
	 */
	@Override
	public String getDefaultMessageId() {
		// TODO Auto-generated method stub
		return null;
	}

}

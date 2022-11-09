package es.ieci.tecdoc.fwktd.csv.aplicacionExternaConnector.ws.service.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ieci.tecdoc.fwktd.csv.aplicacionExternaConnector.ws.delegate.AplicacionExternaConnectorDelegate;
import es.ieci.tecdoc.fwktd.csv.aplicacionExternaConnector.ws.service.AplicacionExternaConnectorWS;

/**
 * Implementaci�n del servicio web de conexi�n con aplicaciones externas.
 *
 * @author Iecisa
 * @version $Revision$
 *
 */
public class AplicacionExternaConnectorWSImpl implements AplicacionExternaConnectorWS {

	/**
	 * Logger de la clase.
	 */
	private static final Logger logger = LoggerFactory
			.getLogger(AplicacionExternaConnectorWSImpl.class);

	/**
	 * Delegate.
	 */
	private AplicacionExternaConnectorDelegate aplicacionExternaConnectorDelegate = null;

	/**
	 * Constructor.
	 */
	public AplicacionExternaConnectorWSImpl() {
		super();
	}

	public AplicacionExternaConnectorDelegate getAplicacionExternaConnectorDelegate() {
		return aplicacionExternaConnectorDelegate;
	}

	public void setAplicacionExternaConnectorDelegate(
			AplicacionExternaConnectorDelegate aplicacionExternaConnectorDelegate) {
		this.aplicacionExternaConnectorDelegate = aplicacionExternaConnectorDelegate;
	}

	/**
	 * Comprueba si existe el documento.
	 *
	 * @param csv
	 *            CSV del documento.
	 * @return true si existe el documento, false en caso contrario.
	 */
	public boolean existeDocumento(String csv) {

		logger.info("Llamada a existeDocumento: csv=[{}]", csv);

		return getAplicacionExternaConnectorDelegate().existeDocumento(csv);
	}
	
	/**
	 * Comprueba si existe el documento original firmado.
	 *
	 * @param csv
	 *            CSV del documento.
	 * @return true si existe el documento, false en caso contrario.
	 */
	public boolean existeDocumentoOriginal(String csv) {

		logger.info("Llamada a existeDocumento: csv=[{}]", csv);

		return getAplicacionExternaConnectorDelegate().existeDocumentoOriginal(csv);
	}	

	/**
	 * Obtiene el contenido del documento.
	 *
	 * @param csv
	 *            CSV del documento.
	 * @return Contenido del documento.
	 */
	public byte[] getContenidoDocumento(String csv) {

		logger.info("Llamada a getContenidoDocumento: csv=[{}]", csv);

		return getAplicacionExternaConnectorDelegate().getContenidoDocumento(csv);
	}
	
	/**
	 * Obtiene el contenido del documento original firmado.
	 *
	 * @param csv
	 *            CSV del documento.
	 * @return Contenido del documento.
	 */
	public byte[] getContenidoDocumentoOriginal(String csv) {

		logger.info("Llamada a getContenidoDocumento: csv=[{}]", csv);

		return getAplicacionExternaConnectorDelegate().getContenidoDocumentoOriginal(csv);
	}
}

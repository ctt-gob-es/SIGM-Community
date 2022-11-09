package es.ieci.tecdoc.fwktd.csv.api.connector.aplicacionExterna.impl;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import es.ieci.tecdoc.fwktd.csv.api.connector.BaseConnector;
import es.ieci.tecdoc.fwktd.csv.api.connector.aplicacionExterna.AplicacionExternaConnector;

/**
 * Implementaci�n abstracta del conector por defecto con las aplicaciones
 * externas que almacenan los documentos.
 *
 *
 * @author Iecisa
 * @version $Revision$
 *
 */
public abstract class AbstractAplicacionExternaConnector extends BaseConnector implements
		AplicacionExternaConnector {

	/**
	 * Logger de la clase.
	 */
	private static final Logger logger = LoggerFactory.getLogger(AbstractAplicacionExternaConnector.class);

	/**
	 * Constructor.
	 */
	public AbstractAplicacionExternaConnector() {
		super();
	}

	/**
	 * {@inheritDoc}
	 * @see es.ieci.tecdoc.fwktd.csv.api.connector.aplicacionExterna.AplicacionExternaConnector#existeDocumento(java.lang.String, java.util.Map)
	 */
	public boolean existeDocumento(String csv, Map<String, String> params) {

		logger.info("Llamada a existeDocumento: csv=[{}], params=[{}]", csv, params);

		return false;
	}
	
	/**
	 * {@inheritDoc}
	 * @see es.ieci.tecdoc.fwktd.csv.api.connector.aplicacionExterna.AplicacionExternaConnector#existeDocumentoOriginal(java.lang.String, java.util.Map)
	 */
	public boolean existeDocumentoOriginal(String csv, Map<String, String> params) {

		logger.info("Llamada a existeDocumentoOriginal: csv=[{}], params=[{}]", csv, params);

		return false;
	}

	/**
	 * {@inheritDoc}
	 * @see es.ieci.tecdoc.fwktd.csv.api.connector.aplicacionExterna.AplicacionExternaConnector#getContenidoDocumento(java.lang.String, java.util.Map)
	 */
	public byte[] getContenidoDocumento(String csv, Map<String, String> params) {

		logger.info("Llamada a getContenidoDocumento: csv=[{}], params=[{}]", csv, params);

		return null;
	}
	
	/**
	 * {@inheritDoc}
	 * @see es.ieci.tecdoc.fwktd.csv.api.connector.aplicacionExterna.AplicacionExternaConnector#getContenidoDocumentoOriginal(java.lang.String, java.util.Map)
	 */
	public byte[] getContenidoDocumentoOriginal(String csv, Map<String, String> params) {

		logger.info("Llamada a getContenidoDocumento: csv=[{}], params=[{}]", csv, params);

		return null;
	}

	/**
	 * {@inheritDoc}
	 * @see es.ieci.tecdoc.fwktd.csv.api.connector.aplicacionExterna.AplicacionExternaConnector#writeDocumento(java.lang.String, java.io.OutputStream, java.util.Map)
	 */
	public void writeDocumento(String csv, OutputStream outputStream,
			Map<String, String> params) throws IOException {

		logger.info("Llamada a writeDocumento: csv=[{}], params=[{}]", csv, params);

	}
	
	/**
	 * {@inheritDoc}
	 * @see es.ieci.tecdoc.fwktd.csv.api.connector.aplicacionExterna.AplicacionExternaConnector#writeDocumentoOriginal(java.lang.String, java.io.OutputStream, java.util.Map)
	 */
	public void writeDocumentoOriginal(String csv, OutputStream outputStream, Map<String, String> params) throws IOException {
		logger.info("Llamada a writeDocumentoOriginal: csv=[{}], params=[{}]", csv, params);
	}
}

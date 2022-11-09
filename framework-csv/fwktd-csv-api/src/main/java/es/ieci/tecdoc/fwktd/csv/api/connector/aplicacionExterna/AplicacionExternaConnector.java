package es.ieci.tecdoc.fwktd.csv.api.connector.aplicacionExterna;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;

import es.ieci.tecdoc.fwktd.csv.api.connector.Connector;

/**
 * Interfaz para los conectores con las aplicaciones externas que almacenan los
 * documentos.
 *
 * @author Iecisa
 * @version $Revision$
 *
 */
public interface AplicacionExternaConnector extends Connector {

	/**
	 * Comprueba si existe el documento.
	 *
	 * @param csv
	 *            CSV del documento.
	 * @param params
	 *            Parámetros de configuración de la aplicación externa.
	 * @return true si existe el documento, false en caso contrario.
	 */
	public boolean existeDocumento(String csv, Map<String, String> params);
	
	/**
	 * Comprueba si existe el documento original firmado.
	 *
	 * @param csv
	 *            CSV del documento.
	 * @param params
	 *            Parámetros de configuración de la aplicación externa.
	 * @return true si existe el documento, false en caso contrario.
	 */
	public boolean existeDocumentoOriginal(String csv, Map<String, String> params);

	/**
	 * Obtiene el contenido del documento.
	 *
	 * @param csv
	 *            CSV del documento.
	 * @param params
	 *            Parámetros de configuración de la aplicación externa.
	 * @return Contenido del documento.
	 */
	public byte[] getContenidoDocumento(String csv, Map<String, String> params);
	
	/**
	 * Obtiene el contenido del documento original firmado.
	 *
	 * @param csv
	 *            CSV del documento.
	 * @param params
	 *            Parámetros de configuración de la aplicación externa.
	 * @return Contenido del documento.
	 */
	public byte[] getContenidoDocumentoOriginal(String csv, Map<String, String> params);

	/**
	 * Guarda el contenido del documento en el OutputStream.
	 *
	 * @param csv
	 *            CSV del documento.
	 * @param outputStream
	 *            OutputStream para escribir el documento.
	 * @param params
	 *            Parámetros de configuración de la aplicación externa.
	 * @throws IOException
	 *             si ocurre algún error en la escritura del documento.
	 */
	public void writeDocumento(String csv, OutputStream outputStream,
			Map<String, String> params) throws IOException;
	
	/**
	 * Guarda el contenido del documento original firmado en el OutputStream.
	 *
	 * @param csv
	 *            CSV del documento.
	 * @param outputStream
	 *            OutputStream para escribir el documento.
	 * @param params
	 *            Parámetros de configuración de la aplicación externa.
	 * @throws IOException
	 *             si ocurre algún error en la escritura del documento.
	 */
	public void writeDocumentoOriginal(String csv, OutputStream outputStream, Map<String, String> params) throws IOException;

}

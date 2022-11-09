package es.ieci.tecdoc.fwktd.csv.core.service;

import java.io.IOException;
import java.io.OutputStream;

import es.ieci.tecdoc.fwktd.csv.core.vo.DocumentoCSV;
import es.ieci.tecdoc.fwktd.csv.core.vo.InfoDocumentoCSV;
import es.ieci.tecdoc.fwktd.csv.core.vo.InfoDocumentoCSVForm;

/**
 * Interfaz del servicio de gesti�n de documentos.
 *
 * @author Iecisa
 * @version $Revision$
 *
 */
public interface ServicioDocumentos {

	/**
	 * Genera el CSV para un documento.
	 *
	 * @param infoDocumentoForm
	 *            Informaci�n del documento.
	 * @return Informaci�n del documento
	 */
	public InfoDocumentoCSV generarCSV(InfoDocumentoCSVForm infoDocumentoForm);

	/**
	 * Obtiene la informaci�n almacenada del documento.
	 *
	 * @param id
	 *            Identificador del documento.
	 * @return Informaci�n del documento.
	 */
	public InfoDocumentoCSV getInfoDocumento(String id);

	/**
	 * Obtiene la informaci�n almacenada del documento.
	 *
	 * @param csv
	 *            CSV del documento.
	 * @return Informaci�n del documento.
	 */
	public InfoDocumentoCSV getInfoDocumentoByCSV(String csv);
	
	
	/**
	 * Obtiene la informaci�n almacenada del documento.
	 *
	 * @param nombreDoc
	 *            nombre del documento.
	 * @return Informaci�n del documento.
	 */
	public InfoDocumentoCSV getInfoDocumentoByNombre(String nombreDoc);


	/**
	 * Obtiene la informaci�n almacenada del documento junto con el contenido.
	 *
	 * @param id
	 *            Identificador del documento.
	 * @return Informaci�n del documento con el contenido.
	 */
	public DocumentoCSV getDocumento(String id);

	/**
	 * Obtiene la informaci�n almacenada del documento junto con el contenido.
	 *
	 * @param csv
	 *            CSV del documento.
	 * @return Informaci�n del documento con el contenido.
	 */
	public DocumentoCSV getDocumentoByCSV(String csv);

	/**
	 * Guarda la informaci�n de un documento.
	 *
	 * @param infoDocumento
	 *            Informaci�n del documento.
	 * @return Informaci�n del documento.
	 */
	public InfoDocumentoCSV saveInfoDocumento(InfoDocumentoCSV infoDocumento);

	/**
	 * Actualiza la informaci�n almacenada del documento.
	 *
	 * @param infoDocumento
	 *            Informaci�n del documento.
	 * @return Informaci�n del documento.
	 */
	public InfoDocumentoCSV updateInfoDocumento(InfoDocumentoCSV infoDocumento);

	/**
	 * Elimina la informaci�n almacenada del documento.
	 *
	 * @param id
	 *            Identificador del documento.
	 */
	public void deleteInfoDocumento(String id);

	/**
	 * Comprueba si el contenido del documento se puede descargar de la
	 * aplicaci�n externa.
	 *
	 * @param id
	 *            Identificador del documento.
	 * @return true si el contenido del documento existe, false en caso
	 *         contrario.
	 */
	public boolean existeContenidoDocumento(String id);
	
	/**
	 * Comprueba si el contenido del documento original firmado se puede descargar de la
	 * aplicaci�n externa.
	 *
	 * @param id
	 *            Identificador del documento.
	 * @return true si el contenido del documento existe, false en caso
	 *         contrario.
	 */
	public boolean existeContenidoDocumentoOriginal(String id);

	/**
	 * Obtiene el contenido del documento.
	 *
	 * @param id
	 *            Identificador del documento.
	 * @return Contenido del documento.
	 */
	public byte[] getContenidoDocumento(String id);
	
	/**
	 * Obtiene el contenido del documento original firmado.
	 *
	 * @param id
	 *            Identificador del documento.
	 * @return Contenido del documento.
	 */
	public byte[] getContenidoDocumentoOriginal(String id);

	/**
	 * Guarda el contenido del documento en el OutputStream.
	 *
	 * @param id
	 *            Identificador del documento.
	 * @param outputStream
	 *            OutputStream para escribir el documento.
	 * @throws IOException
	 *             si ocurre alg�n error en la escritura del documento.
	 */
	public void writeDocumento(String id, OutputStream outputStream)
			throws IOException;
	
	/**
	 * Guarda el contenido del documento original firmado en el OutputStream.
	 *
	 * @param id
	 *            Identificador del documento.
	 * @param outputStream
	 *            OutputStream para escribir el documento.
	 * @throws IOException
	 *             si ocurre alg�n error en la escritura del documento.
	 */
	public void writeDocumentoOriginal(String id, OutputStream outputStream) throws IOException;

	/**
	 * Revoca la disponibilidad del documento.
	 *
	 * @param csv
	 *          CSV del documento.
	 */
	public void revocarDocumento(String csv);

}

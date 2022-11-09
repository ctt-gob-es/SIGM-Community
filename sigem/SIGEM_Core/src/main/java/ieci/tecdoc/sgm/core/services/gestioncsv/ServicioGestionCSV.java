/**
 * 
 */
package ieci.tecdoc.sgm.core.services.gestioncsv;

import ieci.tecdoc.sgm.core.services.dto.Entidad;

import java.io.OutputStream;

import es.ieci.tecdoc.fwktd.csv.core.service.ServicioDocumentos;

/**
 * @author IECISA
 * 
 *         Interfaz del servicio para la generaci�n y gesti�n de CSV (C�digo
 *         Seguro de Verificaci�n) de SIGEM.
 * 
 */
public interface ServicioGestionCSV {

	/**
	 * Genera el CSV de un documento y almacena la informaci�n en el modelo de
	 * datos.
	 * 
	 * @param entidad
	 *            Entidad sobre la que estamos trabajando
	 * @param infoDocumentoForm
	 *            Informaci�n del documento
	 * @return Informaci�n del documento con el CSV
	 */
	public InfoDocumentoCSV generarCSV(Entidad entidad, InfoDocumentoCSVForm infoDocumentoForm) throws CSVException;

	/**
	 * Obtiene la informaci�n de un documento a partir de su CSV.
	 * 
	 * @param entidad
	 *            Entidad sobre la que estamos trabajando
	 * @param csv
	 *            C�digo Seguro de Verificaci�n asociado a dicho documento
	 * @return Informaci�n del documento con el CSV
	 */
	public InfoDocumentoCSV getInfoDocumentoByCSV(Entidad entidad, String csv) throws CSVException;
	
	/**
	 * Obtiene la informaci�n de un documento a partir de su Nombre.
	 * 
	 * @param entidad
	 *            Entidad sobre la que estamos trabajando
	 * @param nombreDoc
	 *            Nombre de dicho documento
	 * @return Informaci�n del documento con ese nombre
	 */
	public InfoDocumentoCSV getInfoDocumentoByNombre(Entidad entidad, String nombreDoc) throws CSVException;

	/**
	 * Obtiene la informaci�n de un documento, incluido su contenido, a partir
	 * de su CSV.
	 * 
	 * @param entidad
	 *            Entidad sobre la que estamos trabajando
	 * @param csv
	 *            C�digo Seguro de Verificaci�n asociado a dicho documento
	 * @return Informaci�n del documento con el CSV y su contenido
	 */
	public DocumentoCSV getDocumentoByCSV(Entidad entidad, String csv) throws CSVException;

	/**
	 * Elimina la informaci�n de un documento
	 * 
	 * @param entidad
	 *            Entidad sobre la que estamos trabajando
	 * @param id
	 *            Identificador del documento
	 */
	public void deleteInfoDocumento(Entidad entidad, String id) throws CSVException;

	/**
	 * Comprueba si el contenido del documento se puede descargar de la
	 * aplicaci�n externa
	 * 
	 * @param entidad
	 *            Entidad sobre la que estamos trabajando
	 * @param id
	 *            Identificador del documento
	 * @return true: Si existe el contenido del documento. false: Si no existe.
	 */
	public boolean existeContenidoDocumento(Entidad entidad, String id) throws CSVException;
	
	/**
	 * Comprueba si el contenido del documento original firmado se puede descargar de la
	 * aplicaci�n externa
	 * 
	 * @param entidad
	 *            Entidad sobre la que estamos trabajando
	 * @param id
	 *            Identificador del documento
	 * @return true: Si existe el contenido del documento. false: Si no existe.
	 */
	public boolean existeContenidoDocumentoOriginal(Entidad entidad, String id) throws CSVException;

	/**
	 * Obtiene el contenido de un documento a partir de su identificador
	 * 
	 * @param entidad
	 *            Entidad sobre la que estamos trabajando
	 * @param id
	 *            Identificador del documento
	 * @return Contenido del documento.
	 */
	public byte[] getContenidoDocumento(Entidad entidad, String id) throws CSVException;
	
	/**
	 * Obtiene el contenido de un documento original firmado a partir de su identificador
	 * 
	 * @param entidad
	 *            Entidad sobre la que estamos trabajando
	 * @param id
	 *            Identificador del documento
	 * @return Contenido del documento.
	 */
	public byte[] getContenidoDocumentoOriginal(Entidad entidad, String id) throws CSVException;

	/**
	 * Escribe el contenido de un documento a partir del identificador en el
	 * OutputStrem
	 * 
	 * @param entidad
	 *            Entidad sobre la que estamos trabajando
	 * @param id
	 *            Identificador del documento
	 * @param outputStream
	 *            OutputStream sobre el que escribiremos el contenido del
	 *            documento
	 * 
	 */
	public void writeDocumento(Entidad entidad, String id, OutputStream outputStream) throws CSVException;
	
	/**
	 * Escribe el contenido de un documento original firmado a partir del identificador en el
	 * OutputStrem
	 * 
	 * @param entidad
	 *            Entidad sobre la que estamos trabajando
	 * @param id
	 *            Identificador del documento
	 * @param outputStream
	 *            OutputStream sobre el que escribiremos el contenido del
	 *            documento
	 * 
	 */
	public void writeDocumentoOriginal(Entidad entidad, String id, OutputStream outputStream) throws CSVException;

	/**
	 * @return el servicioDocumentos
	 */
	public ServicioDocumentos getServicioDocumentos();

	/**
	 * @param servicioDocumentos
	 *            el servicioDocumentos a fijar
	 */
	public void setServicioDocumentos(ServicioDocumentos servicioDocumentos);
	
}

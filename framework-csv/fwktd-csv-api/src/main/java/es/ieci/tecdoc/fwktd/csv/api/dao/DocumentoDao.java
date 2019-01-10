package es.ieci.tecdoc.fwktd.csv.api.dao;

import es.ieci.tecdoc.fwktd.csv.api.vo.DocumentoVO;
import es.ieci.tecdoc.fwktd.server.dao.BaseDao;

/**
 * Interfaz de los DAOs de documentos.
 *
 * @author Iecisa
 * @version $Revision$
 *
 */
public interface DocumentoDao extends BaseDao<DocumentoVO, String> {

	/**
	 * Obtiene la información del documento a partir del CSV.
	 *
	 * @param csv
	 *            Código Seguro de Verificación del documento.
	 * @return Información del documento.
	 */
	public DocumentoVO getDocumentoByCSV(String csv);
	
	/**
	 * Obtiene la información del documento a partir del Nombre.
	 *
	 * @param nombre
	 *            Nombre del documento.
	 * @return Información del documento.
	 */
	public DocumentoVO getDocumentoByNombre(String nombredoc);

	/**
	 * Elimina un documento a partir del CSV
	 *
	 * @param csv
	 *            Código Seguro de Verificación del documento.
	 */
	public void deleteDocumentoByCSV(String csv);

}

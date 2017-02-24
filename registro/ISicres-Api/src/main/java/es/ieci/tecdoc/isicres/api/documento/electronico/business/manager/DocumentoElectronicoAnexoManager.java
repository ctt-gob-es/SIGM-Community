package es.ieci.tecdoc.isicres.api.documento.electronico.business.manager;

import java.util.List;

import es.ieci.tecdoc.isicres.api.business.vo.UsuarioVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.ConfiguracionCreateDocumentoElectronicoAnexoVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.DocumentoElectronicoAnexoVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.IdentificadorDocumentoElectronicoAnexoVO;

/**
 * Interfaz del manager del proceso de creacion de un Documento Electronico Anexo al Registro
 * @author IECISA
 *
 */
public interface DocumentoElectronicoAnexoManager {

	public DocumentoElectronicoAnexoVO create(DocumentoElectronicoAnexoVO documento,ConfiguracionCreateDocumentoElectronicoAnexoVO cfg);

	/**
	 * Obtiene a partir de los datos pasados el DocumentoElectronicoAnexoVO
	 * NOTA: no recupera datos de contenido
	 * @param idDocumentoAnexo
	 * @return
	 */
	public DocumentoElectronicoAnexoVO retrieve(IdentificadorDocumentoElectronicoAnexoVO idDocumentoAnexo);

	/**
	 * Metodo que recupera todos los documentos electronicos asociados a un registro
	 * @param idLibro
	 * @param idRegistro
	 * @return
	 */
	public List <DocumentoElectronicoAnexoVO> getDocumentosElectronicoAnexoByRegistro(Long idLibro, Long idRegistro);

	/**
	 *
	 * Metodo que ado el identificador de un documento, nos devolvera el documento al que firma
	 * @param idDocumentoFirma
	 * @return
	 */
	public DocumentoElectronicoAnexoVO getDocumentoFirmado(Long idDocumentoFirma);

	public DocumentoElectronicoAnexoVO create(DocumentoElectronicoAnexoVO documentoElectronico,
		ConfiguracionCreateDocumentoElectronicoAnexoVO cfg, UsuarioVO usuarioRegistro);

	/**
	 * Metodo para copiar el registro origen en el registro destino los documentos electrónicos.
	 * @param idLibroOrigen
	 * 	id libro origen
	 * @param idRegistroOrigen
	 * 	id registro origen
	 * @param idLibroDestino
	 * 	id libro destino
	 * @param idRegistroDestino
	 * 	id registro destino
	 * @param usuarioRegistro
	 * 	usuario que hace la copia.
	 */
	public void copyDocumentosElectronicoAnexoByRegistro(Long idLibroOrigen, Long idRegistroOrigen
			,Long idLibroDestino, Long idRegistroDestino, UsuarioVO usuarioRegistro);

	
}

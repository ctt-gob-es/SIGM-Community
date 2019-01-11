package es.ieci.tecdoc.isicres.api.intercambioregistral.business.dao;

import java.util.List;

import es.ieci.tecdoc.fwktd.sir.core.vo.CriteriosVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.BandejaEntradaItemVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.InfoRegistroPageRepositoryVO;


/**
 * DAO para leer y actualizar datos de la bandeja de entrada de intercambio registral
 *
 */
public interface ExportacionMirDAO {

	

	/**
	 * Metodo que lista los registros del libro MIR 
	 * @param {{@link List} - Listado de objetos ExportacionMir
	 */
	public List<BandejaEntradaItemVO> getRegistrosLibro(Integer idlibro);

	

	/**
	 * Metodo que lista los documentos de un registro del libro MIR 
	 * @param {{@link List} - Listado de objetos ExportacionMir
	 */
	public List<InfoRegistroPageRepositoryVO> getDocumentos(Integer idLibro,Integer idBook);


}

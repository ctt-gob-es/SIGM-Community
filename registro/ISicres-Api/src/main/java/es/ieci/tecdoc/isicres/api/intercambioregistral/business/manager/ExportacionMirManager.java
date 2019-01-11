package es.ieci.tecdoc.isicres.api.intercambioregistral.business.manager;

import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.BandejaEntradaItemVO;
import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.InfoRegistroPageRepositoryVO;

import java.util.List;


public interface ExportacionMirManager {

	/**
	 * Metodo que devuelve todos los registros del libro Mir Especificado por el id
	 * 
	 */
	public List<BandejaEntradaItemVO> getRegistroMir(int idBook);
	
	/**
	 * metodo que devuelve todos los documentos de un registro
	 * @param idLibro 
	 * 
	 */
	public List<InfoRegistroPageRepositoryVO>  getDocusRegistro(int idRegistro, int idLibro);

	public void getExportarDocuMir(BandejaEntradaItemVO registro, int idLibro, String ruta);

	
}

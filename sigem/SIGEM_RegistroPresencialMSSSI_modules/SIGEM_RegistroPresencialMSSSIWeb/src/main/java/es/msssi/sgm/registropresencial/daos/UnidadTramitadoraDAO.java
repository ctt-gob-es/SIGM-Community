/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.daos;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import es.ieci.tecdoc.isicres.api.intercambioregistral.business.vo.UnidadTramitacionIntercambioRegistralSIRVO;
import es.msssi.sgm.registropresencial.beans.QueryCompactSearchOrg;
import es.msssi.sgm.registropresencial.beans.SearchUnitsBean;
import es.msssi.sgm.registropresencial.dao.SqlMapClientBaseDao;

/**
 * Clase que contiene los métodos para recuperar las unidades tramitadoras.
 * 
 * @author cmorenog
 * 
 */
public class UnidadTramitadoraDAO extends SqlMapClientBaseDao {
    private static final Logger LOG = Logger.getLogger(UnidadTramitadoraDAO.class.getName());



    /**
     * Devuelve una lista de unidades tramitadoras.
     * 
     * @param useCaseConf
     *            Usuario que realiza la petición.
     * @param query
     *            criterio de búsqueda.
     * @param queryCompactSearchUnitTram
     *            nivel de búsqueda y ordenación sobre el que se realiza la
     *            búsqueda rápida.
     * @return result Listado de organismos disponibles.
     * 
     * @throws exception
     *             si ha habido algún problema.
     */
    public List<UnidadTramitacionIntercambioRegistralSIRVO> buscarUnidadesTramitacion(
	String query, QueryCompactSearchOrg queryCompactSearchUnitTram) {
	LOG.trace("Entrando en UnidadTramitadoraDAO.buscarUnidadesTramitacion");
	 Map<String, Object> map = new HashMap<String, Object>();
	if (query != null){
	    map.put("text", query);
	}
	if (queryCompactSearchUnitTram != null){
	    map.put("where", queryCompactSearchUnitTram.getWhere());
	    if (queryCompactSearchUnitTram.getOrderBy() != null){
		map.put("orderby", queryCompactSearchUnitTram.getOrderBy()); 
	    }
	}
	@SuppressWarnings("unchecked")
	List<UnidadTramitacionIntercambioRegistralSIRVO> result = 
	    (List<UnidadTramitacionIntercambioRegistralSIRVO>) getSqlMapClientTemplate().queryForList(
		"unidadTramitadora.listUnidadTramitadoraQuery",map);
	return result;
    }

    /**
     * Devuelve una unidad tramitadora.
     * 
     * @param id
     *            id de la unidad a recuperar.
     * @return result la unidad tramitadora recuperada.
     * 
     */
    public UnidadTramitacionIntercambioRegistralSIRVO getUnidadTramitadora(
	Integer id) {
	LOG.trace("Entrando en UnidadTramitadoraDAO.getUnidadTramitadora");
	UnidadTramitacionIntercambioRegistralSIRVO result = 
	    (UnidadTramitacionIntercambioRegistralSIRVO) getSqlMapClientTemplate().queryForObject(
		"unidadTramitadora.getUnidadTramitadora",id);
	return result;
    }

    public List<UnidadTramitacionIntercambioRegistralSIRVO> buscarUnidadesTramitacionPag(
	SearchUnitsBean searchUnitsBean, int firstRow, int pageSize) {
	LOG.trace("Entrando en UnidadTramitadoraDAO.countUnidadesTramitacion");

	@SuppressWarnings("unchecked")
	List<UnidadTramitacionIntercambioRegistralSIRVO> result = 
	    (List<UnidadTramitacionIntercambioRegistralSIRVO>) getSqlMapClientTemplate().queryForList(
		"unidadTramitadora.buscarUnidadesTramitacionPag",searchUnitsBean, firstRow, pageSize);
	return result;
    }
    
    /**
     * Devuelve el número de unidades tramitadoras encontradas.
     * 
     * @param searchUnitsBean
     *            parametros de busqueda de las unidades a recuperar.
     * @return result el numero de unidades encontradas.
     * 
     */
    public int countUnidadesTramitacion(
	SearchUnitsBean searchUnitsBean) {
	LOG.trace("Entrando en UnidadTramitadoraDAO.countUnidadesTramitacion");
	int result = 
	    (Integer) getSqlMapClientTemplate().queryForObject(
		"unidadTramitadora.countUnidadesTramitacion",searchUnitsBean);
	return result;
    }
    

    /**
     * Devuelve true si es una unidad tramitadora.
     * 
     * @param idOrg
     *            id de la unidad a recuperar.
     * @return result true si el organismo es una unidad tramitadora.
     * 
     */
    public boolean esUnidadTramitacion(
	Integer idOrg) {
	LOG.trace("Entrando en UnidadTramitadoraDAO.esUnidadTramitacion");
	boolean result = false;
	Integer  tram = 
	    (Integer) getSqlMapClientTemplate().queryForObject(
		"unidadTramitadora.esUnidadTramitacion",idOrg);
	if (tram != null && ! (new Integer(0)).equals(tram)){
	    result = true;
	}
	return result;
    }

}
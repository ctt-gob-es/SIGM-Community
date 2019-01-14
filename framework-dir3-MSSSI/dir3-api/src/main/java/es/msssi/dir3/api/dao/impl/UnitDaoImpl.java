/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.api.dao.impl;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import com.ibatis.sqlmap.engine.execution.SqlExecutor;

import es.msssi.dir3.api.dao.SqlMapClientBaseDao;
import es.msssi.dir3.api.dao.UnitDao;
import es.msssi.dir3.api.type.UOCriterionEnum;
import es.msssi.dir3.api.vo.BasicDataUnitVO;
import es.msssi.dir3.api.vo.Criteria;
import es.msssi.dir3.api.vo.Criterion;
import es.msssi.dir3.api.vo.UnitVO;
import es.msssi.dir3.core.vo.PageInfo;

/**
 * DAO de datos básicos de unidades orgánicas.
 * 
 * @author cmorenog
 * 
 */
public class UnitDaoImpl extends SqlMapClientBaseDao implements UnitDao {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected static final String COUNTFINDUNITS = "UnitVO.countFindUnits";
    protected static final String FINDUNITS = "UnitVO.findUnits";
    protected static final String GETUNIT = "UnitVO.getUnitVO";
    protected static final String EXISTSUNIT = "UnitVO.existsUnit";
    protected static final String INSERTUNIT = "UnitVO.addUnitVO";
    protected static final String UPDATEUNIT = "UnitVO.updateUnitVO";
    protected static final String FINDBASICDATAUNITS = "UnitVO.findBasicDataUnits";
    /**
     * Logger de la clase.
     */
    private static final Logger LOGGER = Logger.getLogger(UnitDaoImpl.class);

    /**
     * Constructor.
     */
    public UnitDaoImpl() {
    	super();
    }

    /**
     * Devuelve el número de unidades orgánicas existentes que cumplan los
     * criterios introducidos.
     * 
     * @param criteria
     *            Criterios de búsqueda de tipo CriterioUnidadOrganicaEnum.
     * @return número de unidades orgánicas.
     * @throws SQLException.
     */
    public int count( List<Criterion<UOCriterionEnum>> criteria) throws SQLException {
    	int count = 0;
    	
    	// Comprobar si se han definido criterios
    	if ((criteria == null) || CollectionUtils.isEmpty(criteria)) {
    		LOGGER.info("Obteniendo el número de unidades orgánicas sin criterios");
    		count = (Integer) getSqlMapClientTemplate().queryForObject( COUNTFINDUNITS);
    		
    	} else {
    		LOGGER.info("Obteniendo el número de unidades orgánicas en base a los siguientes criterios: {}");
    		Map<String, Object> map = new HashMap<String, Object>();
    		map.put( "criteria", criteria);
    		
    		count = (Integer) getSqlMapClientTemplate().queryForObject( COUNTFINDUNITS, map);
    	}
    	
    	return count;
    }

    /**
     * Método genérico para recuperar todas las unidades orgánicas y que cumplan
     * los criterios introducidos.
     * 
     * @param criteria
     *            Criterios de búsqueda de tipo CriterioUnidadOrganicaEnum.
     * @return la lista de unidades orgánicas.
     * @throws SQLException.
     */
    @SuppressWarnings("unchecked")
    public List<UnitVO> find( Criteria<UOCriterionEnum> criteria) throws SQLException {
    	List<UnitVO> result;
    	
    	// Comprobar si se han definido criterios
    	if (criteria == null) {
    		LOGGER.info("Realizando búsqueda de unidades orgánicas sin criterios");
    		result = (List<UnitVO>) getSqlMapClientTemplate().queryForList( FINDUNITS);
    		
    	} else {
    		LOGGER.info("Realizando búsqueda de unidades orgánicas en base a criterios: {}");
    		
    		Map<String, Object> map = new HashMap<String, Object>();
    		if (!CollectionUtils.isEmpty(criteria.getCriteria())) {
    			map.put( "criteria", criteria.getCriteria());
    		}
    		
    		if (!CollectionUtils.isEmpty(criteria.getOrderBy())) {
    			map.put( "orden", criteria.getOrderBy());
    		}
    		
    		// Comprobar si hay que paginar los resultados
    		PageInfo pageInfo = criteria.getPageInfo();
    		if (pageInfo != null) {
    			
    			// Número de resultados a ignorar
    			int skipResults = SqlExecutor.NO_SKIPPED_RESULTS;
    			
    			// Número máximo de resultados.
    			int maxResults = SqlExecutor.NO_MAXIMUM_RESULTS;
    			
    			if ((pageInfo.getPageNumber() > 0) && (pageInfo.getObjectsPerPage() > 0)) {
    				skipResults = (pageInfo.getPageNumber() - 1) * pageInfo.getObjectsPerPage();
    				maxResults = pageInfo.getObjectsPerPage();
    				
    			} else if (pageInfo.getMaxNumItems() > 0) {
    				maxResults = pageInfo.getMaxNumItems();
    			}

    			// Obtener los resultados a mostrar en la página
    			result = (List<UnitVO>) getSqlMapClientTemplate().queryForList( FINDUNITS, map, skipResults, maxResults);
    		
    		} else {
    			result = (List<UnitVO>) getSqlMapClientTemplate().queryForList( FINDUNITS, map);
    		}
    	}
    	
    	return result;
    }

    /**
     * Método  para recuperar una unidad basándonos en su identificador.
     * 
     * @param id
     *            identificador de la unidad orgánica a recuperar
     * @return unidad orgánica recuperada.
     * @throws SQLException.
     */
    @SuppressWarnings("unchecked")
	public UnitVO get( String id) throws SQLException {
    	LOGGER.info("Obteniendo una unidad con el id: " + id);
    	UnitVO unit = null; 
//    	(UnitVO) getSqlMapClientTemplate().queryForObject( GETUNIT, id);
    	
    	List<UnitVO> result = new ArrayList<UnitVO>();
    	result = (List<UnitVO>) getSqlMapClientTemplate().queryForList( GETUNIT, id);
    	
    	if(null != result && 0 < result.size()){
    		unit = result.get(0);
    	}
    	
    	return unit;
    }

    /**
     * Comprueba la existencia de una unidad orgánica con el identificador dado.
     * 
     * @param id
     *            identificador de la unidade orgánica.
     * @return <code>true</code> si el objeto existe, <code>false</code> en caso
     *         contrario
     * @throws SQLException.
     */
    public boolean exists( String id) throws SQLException {
    	LOGGER.info("Obteniendo si existe una unidad con el id: " + id);
    	
    	boolean exist = false;
    	Integer count = (Integer) getSqlMapClientTemplate().queryForObject( EXISTSUNIT, id);
    	
    	if (!Integer.valueOf(0).equals(count)) {
    		exist = true;
    	}
    	
    	return exist;
    }

    /**
     * Método para guardar una unidad orgánica. 
     * 
     * @param entity
     *            unidad orgánica a guardar.
     * @throws SQLException.
     */
    public void save( UnitVO entity) throws SQLException {
    	LOGGER.info("Guardar una unidad orgánica");
    	
    	getSqlMapClient().insert(INSERTUNIT, entity);
    }

    /**
     * Método  para actualizar una unidad orgánica.
     * 
     * @param entity
     *            unidad orgánica a actualizar.
     * @throws SQLException.
     */
    public void update(	UnitVO entity) throws SQLException {
    	LOGGER.info("Modifico una unidad orgánica");
    	
    	getSqlMapClient().update( UPDATEUNIT, entity);
    }


    /**
     * Método  para recuperar los datos básicos de todas las unidades
     *  que cumplan los criterios introducidos.
     * 
     * @param criterios
     *            Criterios de búsqueda .
     * @return la lista de unidades.
     * @throws SQLException
     */
    @SuppressWarnings("unchecked")
    public List<BasicDataUnitVO> findBasicData( Criteria<UOCriterionEnum> criteria) throws SQLException {
    	
    	List<BasicDataUnitVO> result;
    	
    	// Comprobar si se han definido criterios
    	if (criteria == null) {
    		LOGGER.info("Realizando búsqueda de los datos básicos de unidades orgánicas sin criterios");
    		
    		result = (List<BasicDataUnitVO>) getSqlMapClientTemplate().queryForList( FINDBASICDATAUNITS);
    		
    	} else {
    		LOGGER.info("Realizando búsqueda de los datos básicos de unidades orgánicas en base a criterios: {}");
    		
    		Map<String, Object> map = new HashMap<String, Object>();
    		if (!CollectionUtils.isEmpty(criteria.getCriteria())) {
    			map.put( "criteria", criteria.getCriteria());
    		}
    		
    		if (!CollectionUtils.isEmpty(criteria.getOrderBy())) {
    			map.put( "orden", criteria.getOrderBy());
    		}

    		// Comprobar si hay que paginar los resultados
    		PageInfo pageInfo = criteria.getPageInfo();
    		if (pageInfo != null) {
    			
    			// Número de resultados a ignorar
    			int skipResults = SqlExecutor.NO_SKIPPED_RESULTS;
    			
    			// Número máximo de resultados.
    			int maxResults = SqlExecutor.NO_MAXIMUM_RESULTS;
    			
    			if ((pageInfo.getPageNumber() > 0) && (pageInfo.getObjectsPerPage() > 0)) {
    				skipResults = (pageInfo.getPageNumber() - 1) * pageInfo.getObjectsPerPage();
    				maxResults = pageInfo.getObjectsPerPage();
    				
    			} else if (pageInfo.getMaxNumItems() > 0) {
    				maxResults = pageInfo.getMaxNumItems();
    			}
    			
    			// Obtener los resultados a mostrar en la página
    			result = (List<BasicDataUnitVO>) getSqlMapClientTemplate().queryForList( FINDBASICDATAUNITS, map, skipResults, maxResults);
    			
    		} else {
    			result = (List<BasicDataUnitVO>) getSqlMapClientTemplate().queryForList( FINDBASICDATAUNITS, map);
    		}
    	}
    	
    	return result;
    }
}

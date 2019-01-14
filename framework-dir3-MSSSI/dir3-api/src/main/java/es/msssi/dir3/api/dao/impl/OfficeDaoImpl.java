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
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.util.CollectionUtils;

import com.ibatis.sqlmap.engine.execution.SqlExecutor;

import es.msssi.dir3.api.dao.OfficeDao;
import es.msssi.dir3.api.dao.SqlMapClientBaseDao;
import es.msssi.dir3.api.type.OfficeCriterionEnum;
import es.msssi.dir3.api.vo.BasicDataOfficeVO;
import es.msssi.dir3.api.vo.Criteria;
import es.msssi.dir3.api.vo.Criterion;
import es.msssi.dir3.api.vo.OfficeVO;
import es.msssi.dir3.core.vo.PageInfo;

/**
 * DAO de datos básicos de oficinas.
 * 
 * @author cmorenog
 * 
 */
public class OfficeDaoImpl extends SqlMapClientBaseDao implements OfficeDao {

    /**
     * Nombre de la query.
     * */
    protected static final String COUNTFINDOFFICE = "OfficeVO.countFindOffice";
    protected static final String FINDOFFICES = "OfficeVO.findOffice";
    protected static final String GETOFFICE = "OfficeVO.getOffice";
    protected static final String INSERTOFFICE = "OfficeVO.addOffice";
    protected static final String UPDATEOFFICE = "OfficeVO.updateOfficeVO";
    protected static final String EXISTSOFFICE = "OfficeVO.existsOffice";
    protected static final String FINDBASICDATAOFFICES = "OfficeVO.findBasicDataOffice";
    /**
     * Logger de la clase.
     */
    private static final Logger logger = Logger.getLogger(OfficeDaoImpl.class);

    /**
     * Constructor.
     * 
     */
    public OfficeDaoImpl() {
	super();
    }

    /**
     * Devuelve el número de oficinas existentes que cumplan los criterios
     * introducidos.
     * 
     * @param criteria
     *            Criterios de búsqueda de tipo CriterioOficinaEnum.
     * @return número de oficinas.
     * @throws SQLException .
     */
    public int count(
	List<Criterion<OfficeCriterionEnum>> criteria)
	throws SQLException {
	int count = 0;

	// Comprobar si se han definido criterios
	if ((criteria == null) ||
	    CollectionUtils.isEmpty(criteria)) {
	    logger.info("Obteniendo el número de oficinas sin criterios");
	    count = (Integer) getSqlMapClientTemplate().queryForObject(
		COUNTFINDOFFICE);
	}
	else {
	    logger.info("Obteniendo el número de oficinas en base a los siguientes criterios: {}");
	    Map<String, Object> map = new HashMap<String, Object>();
	    map.put(
		"criteria", criteria);

	    count = (Integer) getSqlMapClientTemplate().queryForObject(
		COUNTFINDOFFICE, map);
	}
	return count;
    }

    /**
     * Método para recuperar todas las oficinas y que cumplan los criterios
     * introducidos.
     * 
     * @param criteria
     *            Criterios de búsqueda de tipo CriterioOficinaEnum.
     * @return la lista de objetos.
     * @throws SQLException .
     */
    @SuppressWarnings("unchecked")
    public List<OfficeVO> find(
	Criteria<OfficeCriterionEnum> criteria)
	throws SQLException {
	List<OfficeVO> result;

	// Comprobar si se han definido criterios
	if (criteria == null) {
	    logger.info("Realizando búsqueda de oficinas sin criterios");
	    result = (List<OfficeVO>) getSqlMapClientTemplate().queryForList(
		FINDOFFICES);
	}
	else {
	    logger.info("Realizando búsqueda de oficinas en base a criterios: {}");
	    Map<String, Object> map = new HashMap<String, Object>();
	    if (!CollectionUtils.isEmpty(criteria.getCriteria())) {
		map.put(
		    "criteria", criteria.getCriteria());
	    }
	    if (!CollectionUtils.isEmpty(criteria.getOrderBy())) {
		map.put(
		    "orden", criteria.getOrderBy());
	    }

	    // Comprobar si hay que paginar los resultados
	    PageInfo pageInfo = criteria.getPageInfo();
	    if (pageInfo != null) {

		// Número de resultados a ignorar
		int skipResults = SqlExecutor.NO_SKIPPED_RESULTS;

		// Número máximo de resultados.
		int maxResults = SqlExecutor.NO_MAXIMUM_RESULTS;

		if ((pageInfo.getPageNumber() > 0) &&
		    (pageInfo.getObjectsPerPage() > 0)) {
		    skipResults = (pageInfo.getPageNumber() - 1) *
			pageInfo.getObjectsPerPage();
		    maxResults = pageInfo.getObjectsPerPage();
		}
		else if (pageInfo.getMaxNumItems() > 0) {
		    maxResults = pageInfo.getMaxNumItems();
		}

		// Obtener los resultados a mostrar en la página
		result = (List<OfficeVO>) getSqlMapClientTemplate().queryForList(
		    FINDOFFICES, map, skipResults, maxResults);

	    }
	    else {
		result = (List<OfficeVO>) getSqlMapClientTemplate().queryForList(
		    FINDOFFICES, map);
	    }
	}
	return result;
    }

    /**
     * Método para recuperar una oficina basándonos en su identificador.
     * 
     * @param id
     *            id del objeto a recuperar
     * @return el objeto recuperado.
     * @throws SQLException.
     */
    public OfficeVO get(
	String id)
	throws SQLException {
	logger.info("Obteniendo una oficina con el id: " +
	    id);
	OfficeVO office;
	office = (OfficeVO) getSqlMapClientTemplate().queryForObject(
	    GETOFFICE, id);

	return office;
    }

    /**
     * Comprueba la existencia de una oficina con el identificador dado.
     * 
     * @param id
     *            id del objeto a recuperar.
     * @return <code>true</code> si el objeto existe, <code>false</code> en caso
     *         contrario
     * @throws SQLException .
     */
    public boolean exists(
	String id)
	throws SQLException {
	logger.info("Obteniendo si existe una oficina con el id: " +
	    id);
	boolean exist = false;
	Integer count = (Integer) getSqlMapClientTemplate().queryForObject(
	    EXISTSOFFICE, id);
	if (!Integer.valueOf(
	    0).equals(
	    count)) {
	    exist = true;
	}
	return exist;
    }

    /**
     * Método para guardar una oficina.
     * 
     * @param entity
     *            oficina a guardar.
     * @throws SQLException.
     */
    public void save(
	OfficeVO entity)
	throws SQLException {
	logger.info("Guardar una oficina");
	getSqlMapClient().insert(
	    INSERTOFFICE, entity);
    }

    /**
     * Método para actualizar una oficina de la clase dada.
     * 
     * @param entity
     *            objeto a actualizar.
     * @throws SQLException.
     */
    public void update(
	OfficeVO entity)
	throws SQLException {
	logger.info("Modifico una oficina");
	getSqlMapClient().update(
	    UPDATEOFFICE, entity);
    }


    /**
     * Método genérico para recuperar los datos básicos de todas las oficinas y
     * que cumplan los criterios introducidos.
     * 
     * @param criteria
     *            Criterios de búsqueda de tipo CriterioOficinaEnum.
     * @return la lista de datos básicos de oficinas.
     * @throws SQLException.
     */
    @SuppressWarnings("unchecked")
    public List<BasicDataOfficeVO> findBasicData(
	Criteria<OfficeCriterionEnum> criteria)
	throws SQLException {
	List<BasicDataOfficeVO> result;

	// Comprobar si se han definido criterios
	if (criteria == null) {
	    logger.info("Realizando búsqueda de datos básicos de oficinas sin criterios");
	    result = (List<BasicDataOfficeVO>) getSqlMapClientTemplate().queryForList(
		FINDBASICDATAOFFICES);
	}
	else {
	    logger
		.info("Realizando búsqueda  de datos básicos de oficinas en base a criterios: {}");
	    Map<String, Object> map = new HashMap<String, Object>();
	    if (!CollectionUtils.isEmpty(criteria.getCriteria())) {
		map.put(
		    "criteria", criteria.getCriteria());
	    }
	    if (!CollectionUtils.isEmpty(criteria.getOrderBy())) {
		map.put(
		    "orden", criteria.getOrderBy());
	    }

	    // Comprobar si hay que paginar los resultados
	    PageInfo pageInfo = criteria.getPageInfo();
	    if (pageInfo != null) {

		// Número de resultados a ignorar
		int skipResults = SqlExecutor.NO_SKIPPED_RESULTS;

		// Número máximo de resultados.
		int maxResults = SqlExecutor.NO_MAXIMUM_RESULTS;

		if ((pageInfo.getPageNumber() > 0) &&
		    (pageInfo.getObjectsPerPage() > 0)) {
		    skipResults = (pageInfo.getPageNumber() - 1) *
			pageInfo.getObjectsPerPage();
		    maxResults = pageInfo.getObjectsPerPage();
		}
		else if (pageInfo.getMaxNumItems() > 0) {
		    maxResults = pageInfo.getMaxNumItems();
		}

		// Obtener los resultados a mostrar en la página
		result = (List<BasicDataOfficeVO>) getSqlMapClientTemplate().queryForList(
		    FINDBASICDATAOFFICES, map, skipResults, maxResults);

	    }
	    else {
		result = (List<BasicDataOfficeVO>) getSqlMapClientTemplate().queryForList(
		    FINDBASICDATAOFFICES, map);
	    }
	}
	return result;
    }

}

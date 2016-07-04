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
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import es.msssi.dir3.api.dao.RelationshipDao;
import es.msssi.dir3.api.dao.SqlMapClientBaseDao;
import es.msssi.dir3.api.vo.RelationshipOFIUOVO;

/**
 * DAO de datos básicos de una relación sir de oficinas y unidades.
 * 
 * @author cmorenog
 * 
 */
public class RelationshipOFIUOSIRDaoImpl extends SqlMapClientBaseDao implements RelationshipDao {

    /**
     * Nombre de la query.
     * */
    protected static final String GETRELATION = "RelationshipOFIUOVO.getRelationshipSIRVO";
    protected static final String INSERTRELATION = "RelationshipOFIUOVO.addRelationshipSIRVO";
    protected static final String UPDATERELATION = "RelationshipOFIUOVO.updateRelationshipSIRVO";
    protected static final String EXISTS = "RelationshipOFIUOVO.getExistsRelationshipSIRVO";
    protected static final String ISSIR = "RelationshipOFIUOVO.getIsSIRVO";
    protected static final String GETUNITDIR = "RelationshipOFIUOVO.getUnitDir";
    
    /**
     * Logger de la clase.
     */
    private static final Logger logger = Logger.getLogger(RelationshipOFIUOSIRDaoImpl.class);

    /**
     * Constructor.
     * 
     */
    public RelationshipOFIUOSIRDaoImpl() {
	super();
    }

    /**
     * Método genérico para recuperar una relación SIR basándonos en su
     * identificador.
     * 
     * @param ids
     *             identificadores de la relación, officeId y unitId.
     * @return el objeto recuperado.
     * @throws SQLException.
     */
    public RelationshipOFIUOVO get(
	Map<String, String> ids)
	throws SQLException {
	logger.info("Obteniendo una relación SIR: ");
	RelationshipOFIUOVO rel;
	rel = (RelationshipOFIUOVO) getSqlMapClientTemplate().queryForObject(
	    GETRELATION, ids);

	return rel;
    }

    /**
     * Comprueba la existencia de una relación SIR con el identificador dado.
     * 
     * @param ids
     *            identificadores de la relación, officeId y unitId.
     * @return <code>true</code> si el objeto existe, <code>false</code> en caso
     *         contrario.
     * @throws SQLException .
     */
    public boolean exists(
	Map<String, String> ids)
	throws SQLException {
	logger.info("Obteniendo si existe una relación SIR");
	boolean exist = false;
	String rel;
	rel = (String) getSqlMapClientTemplate().queryForObject(
	    EXISTS, ids);
	if (rel != null) {
	    exist = true;
	}
	return exist;
    }

    /**
     * Método  para guardar una relación SIR.
     * 
     * @param entity
     *            relación a guardar.
     * @throws SQLException.
     */
    public void save(
	RelationshipOFIUOVO entity)
	throws SQLException {
	logger.info("Guardar una relación SIR");
	getSqlMapClient().insert(
	    INSERTRELATION, entity);
    }

    /**
     * Método genérico para actualizar una relación SIR de la clase dada.
     * 
     * @param entity
     *            objeto a actualizar.
     * @throws SQLException.
     */
    public void update(
	RelationshipOFIUOVO entity)
	throws SQLException {
	logger.info("Modifico una relación SIR:" +
	    entity.getId());
	getSqlMapClient().update(
	    UPDATERELATION, entity);
    }

    /**
     * Comprueba la existencia de una relación SIR con el identificador dado.
     * 
     * @param ids
     *            identificadores de la relación, officeId y unitId.
     * @return <code>true</code> si el objeto existe, <code>false</code> en caso
     *         contrario.
     * @throws SQLException .
     */
    public boolean isSIR(
	    RelationshipOFIUOVO entity)
	throws SQLException {
	logger.info("Obteniendo si una una relación es SIR");
	boolean exist = false;
	Integer rel;
	rel = (Integer) getSqlMapClientTemplate().queryForObject(
	    ISSIR, entity);
	if (rel != null && rel > 0) {
	    exist = true;
	}
	return exist;
    }

    public List<RelationshipOFIUOVO> getUnitDir(String idEntity) {
	// TODO Auto-generated method stub
	logger.info("Obteniendo si una una relación es SIR");
	List<RelationshipOFIUOVO> result = null;
	result = (List<RelationshipOFIUOVO>) getSqlMapClientTemplate().queryForList(GETUNITDIR, idEntity);
	
	return result;
    }
}

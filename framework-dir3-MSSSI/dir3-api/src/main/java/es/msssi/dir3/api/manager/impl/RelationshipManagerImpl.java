/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.api.manager.impl;

import java.io.Serializable;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import es.msssi.dir3.api.dao.RelationshipDao;
import es.msssi.dir3.api.manager.RelationshipManager;
import es.msssi.dir3.api.vo.RelationshipOFIUOVO;
import es.msssi.dir3.api.vo.RelationshipsOFIUOVO;
import es.msssi.dir3.core.errors.DIR3ErrorCode;
import es.msssi.dir3.core.errors.DIR3Exception;
import es.msssi.dir3.core.errors.ErrorConstants;

/**
 * Implementación del manager de la relaciones entre oficinas y unidades.
 * 
 * @author cmorenog
 * 
 */
public class RelationshipManagerImpl implements RelationshipManager, Serializable {

    /**
     * 
     */
    private static final long serialVersionUID = -1647188278639651806L;
    /**
     * Logger de la clase.
     */
    private static final Logger LOG = Logger.getLogger(RelationshipManagerImpl.class);
    private RelationshipDao relationshipDao;

    /**
     * Constructor.
     * 
     */
    public RelationshipManagerImpl() {

    }

    /**
     * Actualiza o inserta los datos de las relaciones obtenidas del DCO.
     * 
     * @param relationshipsVO
     *            listado de relaciones a actualizar.
     * @throws DIR3Exception .
     */
    public void updateRelationships(
	RelationshipsOFIUOVO relationshipsVO)
	throws DIR3Exception {
	if (relationshipsVO != null) {
	    Map<String, String> ids = null;
	    for (RelationshipOFIUOVO relation : relationshipsVO.getRelationships()) {
		try{
    		ids = new HashMap<String, String>();
    		ids.put(
    		    "unitId", relation.getUnitId());
    		ids.put(
    		    "officeId", relation.getOfficeId());
    		boolean exist = exists(ids);
    		if (exist) {
    		    update(relation);
    		}
    		else {
    		    save(relation);
    		}
		}catch(DIR3Exception exception){
		    LOG.error(
			    ErrorConstants.INSERT_RELATIONSHIP_ERROR_MESSAGE +
				"OfficeId:" + relation.getOfficeId() + " UnitId:" +
				relation.getUnitId() + "; ", exception);
		}
	    }
	}

    }

    /**
     * Inserta una relación.
     * 
     * @param relación
     *            relación a insertar.
     * @throws DIR3Exception
     * 
     */
    public void save(
	RelationshipOFIUOVO relation)
	throws DIR3Exception {
	try {
	    if (relation != null) {
		relationshipDao.startTransaction();
		relationshipDao.save(relation);
		relationshipDao.commitTransaction();
	    }
	}
	catch (SQLException sqlException) {
	    LOG.error(
		ErrorConstants.INSERT_RELATIONSHIP_ERROR_MESSAGE, sqlException);
	    LOG.error("ERROR Insertando la relación: unitId" +
		    relation.getUnitId() + "; officeId: " + relation.getOfficeId());
	    throw new DIR3Exception(
		DIR3ErrorCode.INSERT_RELATIONSHIP_ERROR,
		ErrorConstants.INSERT_RELATIONSHIP_ERROR_MESSAGE, sqlException);
	}
	finally {
	    relationshipDao.endTransaction();
	}
    }

    /**
     * Modifica una relación.
     * 
     * @param relación
     *            relación a modificar.
     * @throws DIR3Exception .
     * 
     */
    public void update(
	RelationshipOFIUOVO relation)
	throws DIR3Exception {
	try {
	    if (relation != null) {
		LOG.info("Modificamos la relación: unitId" +
		    relation.getUnitId() + "; officeId: " + relation.getOfficeId());
		relationshipDao.startTransaction();
		relationshipDao.update(relation);
		relationshipDao.commitTransaction();
	    }
	}
	catch (SQLException sqlException) {
	    LOG.error(
		ErrorConstants.UPDATE_RELATIONSHIP_ERROR_MESSAGE, sqlException);
	    LOG.error("ERROR Modificando la relación: unitId" +
		    relation.getUnitId() + "; officeId: " + relation.getOfficeId());
	    throw new DIR3Exception(
		DIR3ErrorCode.UPDATE_RELATIONSHIP_ERROR,
		ErrorConstants.UPDATE_RELATIONSHIP_ERROR_MESSAGE, sqlException);
	}
	finally {
	    relationshipDao.endTransaction();
	}
    }

    /**
     * Método genérico para recuperar una relación.
     * 
     * @param ids
     *            identificador de la unidad orgánica a recuperar.
     * @return el objeto recuperado.
     * @throws DIR3Exception .
     */
    public RelationshipOFIUOVO get(
	Map<String, String> ids)
	throws DIR3Exception {
	RelationshipOFIUOVO result = null;
	try {
	    if (ids != null &&
		(ids.get("unitId") != null && ids.get("officeId") != null)) {
		LOG.info("Recuperando una relación: unitId: " +
		    ids.get("unitId") + "; officeId: " + ids.get("officeId"));
		// Buscamos datos basicos de la unidad organica
		result = (RelationshipOFIUOVO) relationshipDao.get(ids);
	    }
	}
	catch (SQLException sqlException) {
	    LOG.error(
		ErrorConstants.GET_RELATIONSHIP_ERROR_MESSAGE, sqlException);
	    throw new DIR3Exception(
		DIR3ErrorCode.GET_RELATIONSHIP_ERROR,
		ErrorConstants.GET_RELATIONSHIP_ERROR_MESSAGE, sqlException);
	}
	return result;
    }

    /**
     * Comprueba la existencia de una relación con los identificadores dados.
     * 
     * @param anId
     *            identificadores del objeto.
     * @return <code>true</code> si el objeto existe, <code>false</code> en caso
     *         contrario.
     * @throws DIR3Exception .
     */
    public boolean exists(
	Map<String, String> anId)
	throws DIR3Exception {
	boolean result = false;
	try {
	    LOG.info("Comprobando la existencia de una relación.");
	    // Buscamos datos sobre la unidad orgánica
	    result = relationshipDao.exists(anId);
	}
	catch (SQLException sqlException) {
	    LOG.error(
		ErrorConstants.EXISTS_RELATIONSHIP_ERROR_MESSAGE, sqlException);
	    throw new DIR3Exception(
		DIR3ErrorCode.EXISTS_RELATIONSHIP_ERROR,
		ErrorConstants.EXISTS_RELATIONSHIP_ERROR_MESSAGE, sqlException);
	}
	return result;
    }

    /**
     * Obtiene el valor del parámetro relationshipDao.
     * 
     * @return relationshipDao valor del campo a obtener.
     */
    public RelationshipDao getRelationshipDao() {
	return relationshipDao;
    }

    /**
     * Guarda el valor del parámetro relationshipDao.
     * 
     * @param relationshipDao
     *            valor del campo a guardar.
     */
    public void setRelationshipDao(
	RelationshipDao relationshipDao) {
	this.relationshipDao = relationshipDao;
    }

}

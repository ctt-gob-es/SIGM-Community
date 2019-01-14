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
import java.util.Map;

import org.apache.log4j.Logger;

import es.msssi.dir3.api.dao.HistoryDao;
import es.msssi.dir3.api.dao.SqlMapClientBaseDao;
import es.msssi.dir3.api.vo.HistoryVO;

/**
 * DAO de datos básicos del histórico de unidades orgánicas.
 * 
 * @author cmorenog
 * 
 */
public class HistoryUODaoImpl extends SqlMapClientBaseDao implements HistoryDao {

    /**
     * Nombre de la query
     * */
    protected static final String GETHISTORY = "HistoryVO.getHistoryUOVO";
    protected static final String INSERTHISTORY = "HistoryVO.addHistoryUOVO";
    protected static final String UPDATEHISTORY = "HistoryVO.updateHistoryUOVO";
    protected static final String GETCAUSE = "HistoryVO.getCauseVO";
    protected static final String EXISTS = "HistoryVO.getExistsUOVO";

    /**
     * Logger de la clase.
     */
    private static final Logger logger = Logger.getLogger(HistoryUODaoImpl.class);

    /**
     * Constructor.
     * 
     */
    public HistoryUODaoImpl() {
	super();
    }

    /**
     * Método  para recuperar un histórico basándonos en su
     * identificador.
     * 
     * @param ids
     *            identificadores del histórico: previousId y lastId.
     * @return el histórico recuperado.
     * @throws SQLException.
     */
    public HistoryVO get(
	Map<String,String> ids)
	throws SQLException {
	logger.info("Obteniendo el histórico con el id ultimo: " +
	    ids.get("lastId") +" ; id antiguo: "+  ids.get("previousId"));
	HistoryVO history;
	history = (HistoryVO) getSqlMapClientTemplate().queryForObject(
	    GETHISTORY, ids);

	return history;
    }

    /**
     * Comprueba la existencia de un histórico con el identificador dado.
     * 
     * @param ids
     *            identificadores del histórico: previousId y lastId.
     * @return <code>true</code> si el objeto existe, <code>false</code> en caso
     *         contrario.
     * @throws SQLException .
     */
    public boolean exists(
	Map<String,String> ids)
	throws SQLException {
	logger.info("Obteniendo si existe un histórico con el id ultimo: " +
	    ids.get("lastId") +" ; id antiguo: "+  ids.get("previousId"));
	boolean exist = false;
	String exists = null;
	exists = (String) getSqlMapClientTemplate().queryForObject(
	    EXISTS, ids);
	if (exists != null) {
	    exist = true;
	}
	return exist;
    }

    /**
     * Método  para guardar un histórico.
     * 
     * @param entity
     *            histórico a guardar.
     * @throws SQLException.
     */
    public void save(
	HistoryVO entity)
	throws SQLException {
	logger.info("Guardar un histórico");
	getSqlMapClient().insert(
	    INSERTHISTORY, entity);
    }

    /**
     * Método  para actualizar un histórico de la clase dada.
     * 
     * @param entity
     *            histórico a actualizar.
     * @throws SQLException.
     */
    public void update(
	HistoryVO entity)
	throws SQLException {
	logger.info("Modifico un histórico:" +
	    entity.getId());
	getSqlMapClient().update(
	    UPDATEHISTORY, entity);
    }

    /**
     * Devuelve el código de la causa de baja que corresponde a la descripción
     * que entra como parámetro.
     * 
     * @param cause
     *            descripción de la causa.
     * @return Código de la causa de baja.
     * @throws SQLException
     */
    public String getCauseId(
	String cause)
	throws SQLException {
	logger.info("Cojo el id de la causa de baja:" +
	    cause);
	String code = null;
	code = (String) getSqlMapClientTemplate().queryForObject(
	    GETCAUSE, cause);
	return code;
    }

}

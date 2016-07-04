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

import org.apache.log4j.Logger;

import es.msssi.dir3.api.dao.ServiceDao;
import es.msssi.dir3.api.dao.SqlMapClientBaseDao;
import es.msssi.dir3.api.vo.ServiceVO;

/**
 * DAO de datos básicos de servicios de oficinas.
 * 
 * @author cmorenog
 * 
 */
public class ServiceDaoImpl extends SqlMapClientBaseDao implements ServiceDao {

    /**
     * Nombre de la query.
     * */
    protected static final String GETSERVICE = "ServiceVO.getServiceVO";
    protected static final String LISTSERVICES = "ServiceVO.getServicesVO";
    protected static final String INSERTSERVICE = "ServiceVO.addServiceVO";
    protected static final String UPDATESERVICE = "ServiceVO.updateServiceVO";
    protected static final String DELETESERVICE = "ServiceVO.deleteServicesVO";
    /**
     * Logger de la clase.
     */
    private static final Logger logger = Logger.getLogger(ServiceDaoImpl.class);

    /**
     * Constructor.
     * 
     */
    public ServiceDaoImpl() {
	super();
    }

    /**
     * Método genérico para recuperar un servicio basándonos en su
     * identificador.
     * 
     * @param ids
     *            identificador de la oficina y del servicio.
     * @return el objeto recuperado.
     * @throws SQLException .
     */
    public ServiceVO get(
	HashMap<String, String> ids)
	throws SQLException {
	    logger.info("Obteniendo un servicio con el id de oficina: " +
		    ids.get("officeId") + ";idservicio"+ids.get("service"));
	ServiceVO service;
	service = (ServiceVO) getSqlMapClientTemplate().queryForObject(
	    GETSERVICE, ids);

	return service;
    }

    /**
     * Comprueba la existencia de un servicio con el identificador dado.
     * 
     * @param ids
     *            identificador de la oficina y del servicio.
     * @return <code>true</code> si el objeto existe, <code>false</code> en caso
     *         contrario
     * @throws SQLException .
     */
    public boolean exists(
	HashMap<String, String> ids)
	throws SQLException {
	logger.info("Obteniendo si existe un servicio con el id de oficina: " +
	    ids.get("officeId") + ";idservicio"+ids.get("service"));
	boolean exist = false;
	ServiceVO service;
	service = (ServiceVO) getSqlMapClientTemplate().queryForObject(
	    GETSERVICE, ids);
	if (service != null) {
	    exist = true;
	}
	return exist;
    }

    /**
     * Método para guardar un servicio.
     * 
     * @param entity
     *            servicio a guardar.
     * @throws SQLException .
     */
    public void save(
	ServiceVO entity)
	throws SQLException {
	logger.info("Guardar un servivio");
	getSqlMapClient().insert(
	    INSERTSERVICE, entity);
    }

    /**
     * Método para actualizar un servicio de la clase dada.
     * 
     * @param entity
     *            objeto a actualizar.
     * @throws SQLException .
     */
    public void update(
	ServiceVO entity)
	throws SQLException {
	logger.info("Modifico un servicio:" +
	    entity.getId() + " y oficina: " + entity.getOfficeId());
	getSqlMapClient().update(
	    UPDATESERVICE, entity);
    }

    /**
     * Se eliminan los servicios de la oficina que pasa como parámetro.
     * @param id
     * 	código de la oficina.
     * @throws SQLException .
     */
    public void deleteServices(
	String id)
	throws SQLException {
	logger.info("Borrar los servicios de la oficina:" +
	    id);
	getSqlMapClient().update(
	    DELETESERVICE, id);
    }

    /**
     * Se devuelven los servicios de la oficina que pasa como parámetro.
     * @param codigo
     * 	código de la oficina.
     * @throws SQLException .
     */
    @SuppressWarnings("unchecked")
    public List<ServiceVO> getServices(
	String id)
	throws SQLException {
	logger.info("Devuelve los servicios de la oficina:" +
	    id);
	List<ServiceVO> list;
	list = (List<ServiceVO>) getSqlMapClientTemplate().queryForList(
	    LISTSERVICES, id);
	return list;
    }
}

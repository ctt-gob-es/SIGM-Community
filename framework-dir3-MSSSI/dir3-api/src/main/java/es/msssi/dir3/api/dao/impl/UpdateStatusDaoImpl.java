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

import es.msssi.dir3.api.dao.SqlMapClientBaseDao;
import es.msssi.dir3.api.dao.UpdateStatusDAO;
import es.msssi.dir3.api.vo.UpdateStatusVO;

/**
 * DAO de datos de las actualizaciones.
 * 
 * @author cmorenog
 * 
 */
public class UpdateStatusDaoImpl extends SqlMapClientBaseDao implements UpdateStatusDAO {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	
	protected static final String GETLASTSUCCESSUPDATE = "UpdateStatusVO.getLastSuccessUpdate";
	protected static final String INSERTLASTSUCCESSUPDATE = "UpdateStatusVO.addUpdateStatusVO";
	protected static final String GETSTATE = "UpdateStatusVO.getUpdateStatusVO";
	protected static final String UPDATESTATE = "UpdateStatusVO.updateUpdateStatusVO";
	protected static final String COUNTSTATE = "UpdateStatusVO.countUpdateStatusVO";
	protected static final String EXISTSSTATE = "UpdateStatusVO.existsUpdateStatusVO";

	/**
	 * Constructor.
	 */
	public UpdateStatusDaoImpl() {
		super();
	}

	/**
	 * 
	 * Obtiene la información con la fecha de la última actualización correcta.
	 * 
	 * @return EstadoActualizacionDcoVO la última actualización.
	 * @throws SQLException .
	 */
	public UpdateStatusVO getLastSuccessUpdate() throws SQLException {
		return (UpdateStatusVO) getSqlMapClientTemplate().queryForObject(
				GETLASTSUCCESSUPDATE);
	}

	/**
	 * Método genérico para recuperar una actualización basándonos en su
	 * identificador.
	 * 
	 * @param anId
	 *            identificador del objeto a recuperar
	 * @return el objeto recuperado.
	 * @throws SQLException.
	 */
	public UpdateStatusVO get(String anId) throws SQLException {
		return (UpdateStatusVO) getSqlMapClientTemplate().queryForObject(
				GETSTATE, anId);
	}

	/**
	 * Comprueba la existencia de una actualización con el identificador dado.
	 * 
	 * @param id
	 *            identificador del objeto.
	 * @return <code>true</code> si el objeto existe, <code>false</code> en caso
	 *         contrario
	 * @throws SQLException .
	 */
	public boolean exists(String id) throws SQLException {
		boolean result = false;
		Integer state = (Integer) getSqlMapClientTemplate().queryForObject(
				EXISTSSTATE, id);
		if (!Integer.valueOf(0).equals(state)) {
			result = true;
		}
		return result;
	}

	/**
	 * Método para guardar una actualización.
	 * 
	 * @param entity
	 *            unidad orgánica a guardar.
	 * @throws SQLException.
	 */
	public void save(UpdateStatusVO entity) throws SQLException {
		getSqlMapClient().insert(INSERTLASTSUCCESSUPDATE, entity);
	}

	/**
	 * Método genérico para actualizar una actualización de la clase dada.
	 * 
	 * @param entity
	 *            objeto a actualizar.
	 * @throws SQLException.
	 */
	public void update(UpdateStatusVO entity) throws SQLException {
		getSqlMapClient().update(UPDATESTATE, entity);
	}

}
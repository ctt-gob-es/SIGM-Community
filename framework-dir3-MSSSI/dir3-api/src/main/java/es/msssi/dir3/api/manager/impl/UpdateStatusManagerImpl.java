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

import org.apache.log4j.Logger;

import es.msssi.dir3.api.dao.UpdateStatusDAO;
import es.msssi.dir3.api.dao.impl.UpdateStatusDaoImpl;
import es.msssi.dir3.api.manager.UpdateStatusManager;
import es.msssi.dir3.api.vo.UpdateStatusVO;
import es.msssi.dir3.core.errors.DIR3ErrorCode;
import es.msssi.dir3.core.errors.DIR3Exception;
import es.msssi.dir3.core.errors.ErrorConstants;

/**
 * Implementación del manager de datos básicos de actualizaciones.
 * 
 * @author cmorenog
 * 
 */
public class UpdateStatusManagerImpl implements UpdateStatusManager,
		Serializable {
	/**
     * 
     */
	private static final long serialVersionUID = 3262543240409392144L;

	/**
	 * Logger de la clase.
	 */
	private static final Logger logger = Logger
			.getLogger(UpdateStatusManagerImpl.class);

	private UpdateStatusDAO updateStatusDAO;

	/**
	 * Constructor.
	 * 
	 */
	public UpdateStatusManagerImpl() {
		updateStatusDAO = new UpdateStatusDaoImpl();
	}

	/**
	 * Devuelve la última actualización válida.
	 * 
	 * @return EstadoActualizacionDcoVO la actualización.
	 * @throws DIR3Exception .
	 */
	public UpdateStatusVO getLastSuccessUpdate() throws DIR3Exception {
		logger.info("Obtener la última actualización.");
		UpdateStatusVO result = null;

		try {
			result = updateStatusDAO.getLastSuccessUpdate();
		} catch (SQLException sqlException) {
			logger.error(ErrorConstants.GET_LAST_UPDATE_ERROR_MESSAGE,
					sqlException);
			throw new DIR3Exception(DIR3ErrorCode.GET_LAST_UPDATE_ERROR,
					ErrorConstants.GET_LAST_UPDATE_ERROR_MESSAGE, sqlException);
		}
		return result;
	}

	/**
	 * Método genérico para recuperar una actualización basándonos en su
	 * identificador.
	 * 
	 * @param id
	 *            identificador de la actualización a recuperar.
	 * @return el objeto recuperado.
	 * @throws DIR3Exception .
	 */
	public UpdateStatusVO get(String id) throws DIR3Exception {
		UpdateStatusVO result = null;
		try {
			logger.info("Recuperando una actualización: " + id);
			// Buscamos datos basicos de la unidad organica
			result = (UpdateStatusVO) updateStatusDAO.get(id);
		} catch (SQLException sqlException) {
			logger.error(ErrorConstants.GET_UPDATE_ERROR_MESSAGE, sqlException);
			throw new DIR3Exception(DIR3ErrorCode.GET_UPDATE_ERROR,
					ErrorConstants.GET_UPDATE_ERROR_MESSAGE, sqlException);
		}
		return result;
	}

	/**
	 * Comprueba la existencia de una actualización con el identificador dado.
	 * 
	 * @param id
	 *            identificador del objeto.
	 * @return <code>true</code> si el objeto existe, <code>false</code> en caso
	 *         contrario.
	 * @throws DIR3Exception .
	 */
	public boolean exists(String id) throws DIR3Exception {
		boolean result = false;
		try {
			logger.info("Comprobando la existencia de una actualización: " + id);
			// Buscamos datos
			result = updateStatusDAO.exists(id);
		} catch (SQLException sqlException) {
			logger.error(ErrorConstants.EXISTS_UPDATE_ERROR_MESSAGE,
					sqlException);
			throw new DIR3Exception(DIR3ErrorCode.EXISTS_UPDATE_ERROR,
					ErrorConstants.EXISTS_UPDATE_ERROR_MESSAGE, sqlException);
		}
		return result;
	}

	/**
	 * Obtiene el valor del parámetro updateStatusDAO.
	 * 
	 * @return updateStatusDAO valor del campo a obtener.
	 */
	public UpdateStatusDAO getUpdateStatusDAO() {
		return updateStatusDAO;
	}

	/**
	 * Guarda el valor del parámetro estadoActualizacionDcoDAO.
	 * 
	 * @param estadoActualizacionDcoDAO
	 *            valor del campo a guardar.
	 */
	public void setUpdateStatusDAO(UpdateStatusDAO updateStatusDAO) {
		this.updateStatusDAO = updateStatusDAO;
	}

	/**
	 * Guarda una actualización.
	 * 
	 * @param anEntity
	 *            valor del campo a guardar.
	 * @throws DIR3Exception .
	 */
	public void save(UpdateStatusVO anEntity) throws DIR3Exception {
		logger.info("Guardando la actulización.");
		try {
			updateStatusDAO.startTransaction();
			updateStatusDAO.save(anEntity);
			updateStatusDAO.commitTransaction();

		} catch (SQLException sqlException) {
			logger.error(ErrorConstants.SAVE_UPDATE_ERROR_MESSAGE, sqlException);
			throw new DIR3Exception(DIR3ErrorCode.SAVE_UPDATE_ERROR,
					ErrorConstants.SAVE_UPDATE_ERROR_MESSAGE, sqlException);
		} finally {
			updateStatusDAO.endTransaction();
		}
	}

	/**
	 * Modifica una actualización.
	 * 
	 * @param entity
	 *            la actualización a modificar.
	 * @throws DIR3Exception.
	 */
	public void update(UpdateStatusVO entity) throws DIR3Exception {
		logger.info("Modificando la actulización: " + entity.getId());
		try {
			updateStatusDAO.startTransaction();
			updateStatusDAO.update(entity);
			updateStatusDAO.commitTransaction();
		} catch (SQLException sqlException) {
			logger.error(ErrorConstants.UPDATE_UPDATE_ERROR_MESSAGE,
					sqlException);
			throw new DIR3Exception(DIR3ErrorCode.UPDATE_UPDATE_ERROR,
					ErrorConstants.UPDATE_UPDATE_ERROR_MESSAGE, sqlException);
		} finally {
			updateStatusDAO.endTransaction();
		}
	}

}

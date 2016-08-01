package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.db.DbQuery;
import ieci.tdw.ispac.ispaclib.util.ISPACConfiguration;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import java.util.Vector;

import org.apache.log4j.Logger;

public class AccesoBBDDeTramitacion{
	/** Logger de la clase. */
	private static final Logger logger = Logger.getLogger(AccesoBBDDeTramitacion.class);
	
	/** Nombre del origen de datos por defecto. */
	private static final String DEFAULT_DATASOURCE_NAME = "java:comp/env/jdbc/eTramitacionDS_";
	
	/** Nombre del origen de datos. */
	protected String dsName = DEFAULT_DATASOURCE_NAME;
	
	/**
	 * Constructor.
	 * @exception ISPACException si ocurre algún error.
	 */
	public AccesoBBDDeTramitacion() throws ISPACException {
		super();
		
		// Nombre del datasource de SICRES
		this.dsName = ISPACConfiguration.getInstance().get("THIRDPARTY_API_DATASOURCE_NAME",DEFAULT_DATASOURCE_NAME);
		
	}

	/**
	 * Constructor.
	 * @param dsName Nombre del origen de datos.
	 * @exception ISPACException si ocurre algún error.
	 */
	public AccesoBBDDeTramitacion(String entidad) throws ISPACException {
		super();
		
		// Nombre del datasource de SICRES
		this.dsName = ISPACConfiguration.getInstance().get("THIRDPARTY_API_DATASOURCE_NAME",DEFAULT_DATASOURCE_NAME)+entidad;
	}
	
	/**
	 * 
	 * @param asunto: Asunto que va asociado al trámite del registro telématico
	 * @return Listado de procedimientos que estan asociados a ese tipo de asunto.
	 * @throws ISPACException
	 */
	public Vector<String> getIdProcedimiento(String asunto) throws ISPACException{
		DbCnt cnt = new DbCnt(dsName);
		DbQuery dbQuery = null;
		Vector<String> idProcedimiento = new Vector<String>();
		
		try {
			cnt.getConnection();
			dbQuery = cnt.executeDbQuery("SELECT ID_PROCEDIMIENTO FROM SGMRTCATALOGO_TRAMITES WHERE ASUNTO = '" + asunto + "'");
			while (dbQuery.next()) {
				String id_procedimiento = dbQuery.getString("ID_PROCEDIMIENTO");
				if( StringUtils.isNotEmpty(id_procedimiento)){
					idProcedimiento.add(dbQuery.getString("ID_PROCEDIMIENTO"));
				}
			}
		} catch (ISPACException e) {
			logger.error("Error en la búsqueda del asunto. " + e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			logger.error("Error en la búsqueda del asunto. " + e.getMessage(), e);
			throw new ISPACException("Error en la búsqueda del asunto. " + e.getMessage(), e);
		} finally {
			if (dbQuery != null) {
				dbQuery.close();
			}
			cnt.closeConnection();
		}
		return idProcedimiento;		
	}
	
	public void actualizaRegistroTelematico (String nreg, String estado, String campo, String valor) throws ISPACException{
		DbCnt cnt = new DbCnt(dsName);
		
		try {
			cnt.getConnection();
			StringBuffer consulta = new StringBuffer();
			consulta.append("UPDATE SGMRTREGISTRO SET " + campo + " = '" + valor + "' WHERE NUMERO_REGISTRO = '" + nreg + "'");
			if(StringUtils.isNotEmpty(estado)){
				consulta.append(" AND ESTADO = '" + estado + "'");
			}
			cnt.execute(consulta.toString());
		} catch (ISPACException e) {
			logger.error("Error al actualizar el campo: " + campo + " al valor: " + valor + " del registro " + nreg + ". " + e.getMessage(), e);
			throw new ISPACException("Error al actualizar el campo: " + campo + " al valor: " + valor + " del registro " + nreg + ". " + e.getMessage(), e);
		} finally {
			cnt.closeConnection();
		}
	}
}

package es.dipucr.sigem.api.rule.common;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.db.DbQuery;
import ieci.tdw.ispac.ispaclib.util.ISPACConfiguration;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.object.Expediente;
import es.dipucr.sigem.registroTelematicoWeb.formulario.common.Ayuntamiento;
import es.dipucr.sigem.registroTelematicoWeb.formulario.common.Dato;

public class AccesoBBDDTramitador{
	/** Logger de la clase. */
	private static final Logger LOGGER = Logger.getLogger(AccesoBBDDTramitador.class);
	
	/** Nombre del origen de datos por defecto. */
	private static final String DEFAULT_DATASOURCE_NAME = "java:comp/env/jdbc/tramitadorDS_";
	
	/** Nombre del origen de datos. */
	protected String dsName = DEFAULT_DATASOURCE_NAME;
	
	/**
	 * Constructor.
	 * @exception ISPACException si ocurre algún error.
	 */
	public AccesoBBDDTramitador() throws ISPACException {
		super();
		
		// Nombre del datasource de SICRES
		this.dsName = ISPACConfiguration.getInstance().get("THIRDPARTY_API_DATASOURCE_NAME",DEFAULT_DATASOURCE_NAME);
		
	}

	/**
	 * Constructor.
	 * @param dsName Nombre del origen de datos.
	 * @exception ISPACException si ocurre algún error.
	 */
	public AccesoBBDDTramitador(String entidad) throws ISPACException {
		super();
		
		// Nombre del datasource de SICRES
		this.dsName = ISPACConfiguration.getInstance().get("THIRDPARTY_API_DATASOURCE_NAME",DEFAULT_DATASOURCE_NAME)+entidad;
	}
	

	public Vector<Ayuntamiento> getListAyuntamiento() throws ISPACException{
		DbCnt cnt = new DbCnt(dsName);
		DbQuery dbQuery = null;
		String valor = "";
		String sustituto = "";
		Vector<Ayuntamiento> listAyunt = new Vector<Ayuntamiento>();
		
		try {
			
			cnt.getConnection();
			dbQuery = cnt.executeDbQuery("SELECT VALOR, SUSTITUTO FROM REC_VLDTBL_MUNICIPIOS ORDER BY SUSTITUTO");
			while (dbQuery.next()) {
				valor = dbQuery.getString("VALOR");
				sustituto = dbQuery.getString("SUSTITUTO");
				Ayuntamiento ayto = new Ayuntamiento(valor, sustituto);
				listAyunt.add(ayto);
			}
		} catch (ISPACException e) {
			LOGGER.error("Error en la búsqueda del asunto. " + e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("Error en la búsqueda del asunto. " + e.getMessage(), e);
			throw new ISPACException("Error en la búsqueda del asunto. " + e.getMessage(), e);
		} finally {
			if (dbQuery != null) {
				dbQuery.close();
			}
			cnt.closeConnection();
		}
		return listAyunt;		
	}
	
	public String getAsuntoNumexp (String numexp) throws ISPACException{
		String asunto = "";
		
		DbCnt cnt = new DbCnt(dsName);
		DbQuery dbQuery = null;
		
		try {
			
			cnt.getConnection();
			dbQuery = cnt.executeDbQuery("SELECT ASUNTO FROM SPAC_EXPEDIENTES WHERE NUMEXP='"+numexp+"'");
			while (dbQuery.next()) {
				asunto = dbQuery.getString("ASUNTO");
			}
		} catch (ISPACException e) {
			LOGGER.error("Error en la búsqueda del asunto. " + e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("Error en la búsqueda del asunto. " + e.getMessage(), e);
			throw new ISPACException("Error en la búsqueda del asunto. " + e.getMessage(), e);
		} finally {
			if (dbQuery != null) {
				dbQuery.close();
			}
			cnt.closeConnection();
		}
		
		return asunto;		
	}
	public Vector<Expediente> getExpedienteNumexpReg (String numexpReg) throws ISPACException{
		
		DbCnt cnt = new DbCnt(dsName);
		DbQuery dbQuery = null;
		Vector <Expediente> vExpediente = new Vector<Expediente>();
		ArrayList<String> aNumexp = new ArrayList<String> ();
		DbQuery dbQueryExp = null;
		DbQuery dbQueryDoc = null;
		
		try {
			
			cnt.getConnection();
			String sql = "SELECT ASUNTO,NUMEXP,NOMBREPROCEDIMIENTO FROM SPAC_EXPEDIENTES WHERE (NUMEXP='"+numexpReg+"' OR NREG='"+numexpReg+"') AND FCIERRE IS null";
			dbQuery = cnt.executeDbQuery(sql);
			while (dbQuery.next()) {
				String numexp = dbQuery.getString("NUMEXP");
				if(!aNumexp.contains(numexp)){
					Expediente expediente = new Expediente();
					expediente.setAsunto(dbQuery.getString("ASUNTO"));
					expediente.setNumexp(dbQuery.getString("NUMEXP"));
					expediente.setNombreProcedimientos(dbQuery.getString("NOMBREPROCEDIMIENTO"));
					aNumexp.add(dbQuery.getString("NUMEXP"));
					vExpediente.add(expediente);
				}
			}
			sql = "SELECT NUMEXP FROM SPAC_DT_DOCUMENTOS WHERE NREG='"+numexpReg+"'";
			dbQueryDoc = cnt.executeDbQuery(sql);
			while (dbQueryDoc.next()) {
				String numexp = dbQueryDoc.getString("NUMEXP");
				if(!aNumexp.contains(numexp)){
					String sqlExp = "SELECT ASUNTO,NOMBREPROCEDIMIENTO FROM SPAC_EXPEDIENTES WHERE NUMEXP='"+numexp+"' AND FCIERRE IS null";
					dbQueryExp = cnt.executeDbQuery(sqlExp);
					while(dbQueryExp.next()){
						Expediente expediente = new Expediente();
						expediente.setNumexp(numexp);
						expediente.setAsunto(dbQueryExp.getString("ASUNTO"));
						expediente.setNombreProcedimientos(dbQueryExp.getString("NOMBREPROCEDIMIENTO"));
						aNumexp.add(numexp);
						vExpediente.add(expediente);
					}
					
				}
			}
			
		} catch (ISPACException e) {
			LOGGER.error("Error en la búsqueda del asunto. " + e.getMessage(), e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("Error en la búsqueda del asunto. " + e.getMessage(), e);
			throw new ISPACException("Error en la búsqueda del asunto. " + e.getMessage(), e);
		} finally {
			if (dbQuery != null) {
				dbQuery.close();
			}
			cnt.closeConnection();
		}
		
		return vExpediente;		
	}
	
	/**
	 * [eCenpri-Felipe #743]
	 * Sobrecargamos el método para que no tenga impacto en las llamadas que ya se hacen
	 * @param nombreTabla
	 * @return
	 * @throws ISPACException
	 */
	@SuppressWarnings("rawtypes")
	public Vector getDatosTablaValidacion(String nombreTabla) throws ISPACException{
		return getDatosTablaValidacion(nombreTabla, "SUSTITUTO");
	}
	
	/**
	 * [eCenpri-Felipe #743]
	 * Devuelve la tabla de validación ordenada por su ORDEN
	 * @param nombreTabla
	 * @return
	 * @throws ISPACException
	 */
	@SuppressWarnings("rawtypes")
	public Vector getDatosTablaValidacionByOrden(String nombreTabla) throws ISPACException{
		return getDatosTablaValidacion(nombreTabla, "ORDEN");
	}
	
	
	/**
	 * Devuelve los datos de una tabla de validación
	 * @param nombreTabla
	 * @param orderColumn [eCenpri-Felipe #743]
	 * @return
	 * @throws ISPACException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector getDatosTablaValidacion(String nombreTabla, String orderColumn) throws ISPACException{
		DbCnt cnt = new DbCnt(dsName);
		DbQuery dbQuery = null;
		String valor = "";
		String sustituto = "";
		Vector listDatos = new Vector();
		LOGGER.info("consulta: SELECT VALOR, SUSTITUTO FROM "+nombreTabla+" ORDER BY " + orderColumn);
		try {			
			cnt.getConnection();
			dbQuery = cnt.executeDbQuery("SELECT VALOR, SUSTITUTO FROM "+nombreTabla+" ORDER BY " + orderColumn);
			while (dbQuery.next()) {
				valor = dbQuery.getString("VALOR");
				sustituto = dbQuery.getString("SUSTITUTO");
				Dato ayto = new Dato(valor, sustituto);
				listDatos.add(ayto);
			}
		} catch (ISPACException e) {
			LOGGER.error("Error en la búsqueda del asunto", e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("Error en la búsqueda del asunto", e);
			throw new ISPACException("Error en la búsqueda del asunto", e);
		} finally {
			if (dbQuery != null) {
				dbQuery.close();
			}
			cnt.closeConnection();
		}
		return listDatos;		
	}
	
	/**
	 * Ejecuta consulta sobre la BBDD de Tramitación
	 * @param consulta
	 * @return
	 * @throws ISPACException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector ejecutaConsulta(String consulta) throws ISPACException{
		DbCnt cnt = new DbCnt(dsName);
		DbQuery dbQuery = null;
		String valor = "";
		String sustituto = "";
		Vector listDatos = new Vector();
		try {			
			cnt.getConnection();
			dbQuery = cnt.executeDbQuery(consulta);
			while (dbQuery.next()) {
				valor = dbQuery.getString(1);
				sustituto = dbQuery.getString(2);
				Dato ayto = new Dato(valor, sustituto);
				listDatos.add(ayto);
			}
		} catch (ISPACException e) {
			LOGGER.error("Error en la búsqueda del asunto", e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("Error en la búsqueda del asunto", e);
			throw new ISPACException("Error en la búsqueda del asunto", e);
		} finally {
			if (dbQuery != null) {
				dbQuery.close();
			}
			cnt.closeConnection();
		}
		return listDatos;		
	}
	
	/**
	 * Ejecuta consulta sobre la BBDD de Tramitación
	 * @param consulta
	 * @return
	 * @throws ISPACException
	 */
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public Vector ejecutaConsulta(String consulta, int numColumnas) throws ISPACException{
		DbCnt cnt = new DbCnt(dsName);
		DbQuery dbQuery = null;
		String valor = "";
		String sustituto = "";
		Vector listDatos = new Vector();
		try {			
			cnt.getConnection();
			dbQuery = cnt.executeDbQuery(consulta);
			while (dbQuery.next()) {				
				valor = dbQuery.getString(1);
				sustituto = dbQuery.getString(2);
				
				List listaResultados = new ArrayList();
				for(int i = 3 ; i <= numColumnas + 2; i++){
					listaResultados.add(dbQuery.getString(i));
				}
					
				Dato ayto = new Dato(valor, sustituto, listaResultados);
				listDatos.add(ayto);
			}
		} catch (ISPACException e) {
			LOGGER.error("Error en la búsqueda del asunto", e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("Error en la búsqueda del asunto", e);
			throw new ISPACException("Error en la búsqueda del asunto", e);
		} finally {
			if (dbQuery != null) {
				dbQuery.close();
			}
			cnt.closeConnection();
		}
		return listDatos;		
	}
	
	/**
	 * [eCenpri-Felipe]
	 * Ejecuta consulta sobre la BBDD de Tramitación
	 * y obtiene el primero de los resultados recuperados
	 * @param consulta
	 * @return Primer y único campo del primer registro recuperado por la query
	 * @throws ISPACException
	 */
	public String ejecutaConsultaDatoUnico(String consulta) throws ISPACException{
		DbCnt cnt = new DbCnt(dsName);
		DbQuery dbQuery = null;
		String result = "";
		try {			
			cnt.getConnection();
			dbQuery = cnt.executeDbQuery(consulta);
			if (dbQuery.next()) {
				result = dbQuery.getString(1);
			}
		} catch (ISPACException e) {
			LOGGER.error("Error al realizar la consulta " + consulta, e);
			throw e;
		} catch (Exception e) {
			LOGGER.error("Error al realizar la consulta " + consulta, e);
			throw new ISPACException("Error al realizar la consulta " + consulta, e);
		} finally {
			if (dbQuery != null) {
				dbQuery.close();
			}
			cnt.closeConnection();
		}
		return result;		
	}
	
	/**
	 * Borra el bloqueo de un objeto de la BBDD
	 * @param tipoObjeto
	 * @param idObjeto
	 * @return
	 * @throws ISPACException
	 */
	public boolean borrarBloqueo(int tipoObjeto, int idObjeto) throws ISPACException{

		DbCnt cnt = new DbCnt(dsName);
		StringBuilder sbConsulta = new StringBuilder();

		try {			
			cnt.getConnection();
			sbConsulta.append("DELETE FROM SPAC_S_BLOQUEOS WHERE TP_OBJ=");
			sbConsulta.append(tipoObjeto);
			sbConsulta.append(" AND ID_OBJ=");
			sbConsulta.append(idObjeto);
			
			return cnt.execute(sbConsulta.toString());
			
		} catch (ISPACException e) {
			LOGGER.error("Error al realizar la consulta " + sbConsulta.toString(), e);
			throw e;
		}
		finally {
			cnt.closeConnection();
		}		
	}
}

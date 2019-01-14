package es.dipucr.sigem.api.rule.common;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.db.DbQuery;
import ieci.tdw.ispac.ispaclib.util.ISPACConfiguration;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import org.apache.log4j.Logger;

import es.ieci.tecdoc.fwktd.dir3.core.vo.DatosBasicosUnidadOrganica;

public class AccesoBBDDfwktdDIR3{
	
	/** Logger de la clase. */
	private static final Logger logger = Logger.getLogger(AccesoBBDDfwktdDIR3.class);
	
	/** Nombre del origen de datos por defecto. */
	private static final String DEFAULT_DATASOURCE_NAME = "java:comp/env/jdbc/fwktd-dir3DS";
	
	/** Nombre del origen de datos. */
	protected String dsName = DEFAULT_DATASOURCE_NAME;
	
	/**
	 * Constructor.
	 * @exception ISPACException si ocurre algún error.
	 */
	public AccesoBBDDfwktdDIR3() throws ISPACException {
		super();
		// Nombre del datasource de SICRES
		this.dsName = ISPACConfiguration.getInstance().get("API_DIR3", DEFAULT_DATASOURCE_NAME);
		
	}
	
	
	public List<DatosBasicosUnidadOrganica> getUnidadOrganicaByCodeLike(String code) throws ISPACException{
		
		DbCnt cnt = new DbCnt(dsName);
		DbQuery dbQuery = null;
		List<DatosBasicosUnidadOrganica> listUnidades = new ArrayList<DatosBasicosUnidadOrganica>();
		
		try {
			cnt.getConnection();
			
			StringBuilder sbQuery = new StringBuilder();
			sbQuery.append("SELECT CODIGO_UNIDAD_ORGANICA, NOMBRE_UNIDAD_ORGANICA ");
			sbQuery.append("FROM DIR_UNIDAD_ORGANICA ");
			sbQuery.append("WHERE CODIGO_UNIDAD_ORGANICA LIKE '%" + code + "%'");
			dbQuery = cnt.executeDbQuery(sbQuery.toString());
			
			while (dbQuery.next()) {
				DatosBasicosUnidadOrganica objUnidad = new DatosBasicosUnidadOrganica();
				objUnidad.setId(dbQuery.getString("CODIGO_UNIDAD_ORGANICA"));
				objUnidad.setNombre(dbQuery.getString("NOMBRE_UNIDAD_ORGANICA"));
				listUnidades.add(objUnidad);
			}
		} catch (ISPACException e) {
			logger.error("Error en la búsqueda de unidad orgánica por código LIKE: " + code, e);
			throw e;
		} catch (Exception e) {
			logger.error("Error en la búsqueda de unidad orgánica por código LIKE: " + code, e);
			throw new ISPACException("Error en la búsqueda de unidad orgánica por código LIKE: " + code, e);
		} finally {
			if (dbQuery != null) {
				dbQuery.close();
			}
			cnt.closeConnection();
		}
		return listUnidades;		
	}
}

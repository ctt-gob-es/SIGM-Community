/* Copyright (C) 2012-13 MINHAP, Gobierno de España
   This program is licensed and may be used, modified and redistributed under the terms
   of the European Public License (EUPL), either version 1.1 or (at your
   option) any later version as soon as they are approved by the European Commission.
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
   or implied. See the License for the specific language governing permissions and
   more details.
   You should have received a copy of the EUPL1.1 license
   along with this program; if not, you may find it at
   http://joinup.ec.europa.eu/software/page/eupl/licence-eupl */

package es.seap.minhap.portafirmas.utils.stats;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfProvinceDTO;
import es.seap.minhap.portafirmas.utils.Util;

public class StatsQueryUtil {

	protected static final Log log = LogFactory.getLog(StatsQueryUtil.class);
	
	private static final String CAMPOS_USUARIO = " USU.D_NOMBRE || ' ' || USU.D_APELL1 || ' ' || USU.D_APELL2 ";
	
	private static final String DATE_FORMAT = "DD/MM/YYYY HH24:MI:SS";
	
	/**
	 * Query para obtener peticiones por usuario remitente
	 * @param desglose indica si se quiere desglosada o totalizadas.
	 * @param filtro Filtro de la consulta
	 * @return query
	 */
	public static String obtenerQueryPxU (boolean desglose, StatsQueryFilter filter) {
		
		String sedes = listaSedesParentesis(filter.getSedesList());
		
		String query = "";
		
		String campos = "select ";
		String from = " from ";
		String where = " where ";
		String order = " order by ";
		String group = " group by ";
		
		// Siempre obtenemos el nombre y apellidos de los usuarios.
		campos += CAMPOS_USUARIO + ", ";
		// Si es desglose, obtenemos el asunto y referencia de cada peticion
		if (desglose) {
			campos +=  "PET.D_ASUNTO AS ASUNTO, PET.D_REFERENCIA AS REFERENCIA ";
		// Si es total, obtenemos el total de peticiones
		} else {
			campos +=  "count(REM.X_USUARIO_REMITENTE) N_PETICIONES ";
		}
		
		// Tablas comunes del from
		from += " PF_USUARIOS USU, PF_USUARIOS_REMITENTE REM";
		
		// Si es desglose, también unimos la tabla de peticiones (porque necesitamos el asunto y la referencia)
		if (desglose || !Util.esVacioONulo(filter.getPkAplicacion())) {
			from += ", PF_PETICIONES PET"; 
		}
		
		// Where común.
		where += " USU.X_USUARIO = REM.USU_X_USUARIO and " +
				 " USU.PROV_X_PROVINCIA IN " + sedes + " and " +
				 " USU.C_TIPO = 'USUARIO'" ;
				 
		// Si es desglose, metemos la cláusula de cruzar la tabla de peticiones con la de remitentes.
		if (desglose || !Util.esVacioONulo(filter.getPkAplicacion())) {
			where += " and PET.X_PETICION = REM.PET_X_PETICION ";
		}
		
		where += addFiltroFecha ( "REM", "F_CREADO", ">=", filter.getfInicio(), DATE_FORMAT, "and");
		where += addFiltroFecha ( "REM", "F_CREADO", "<=", filter.getfFin(), DATE_FORMAT, "and");
		where += addFiltroGenerico ("PET", "APL_X_APLICACION", "=", filter.getPkAplicacion(), "and");
		where += addFiltroGenerico ("USU", "X_USUARIO", "=", filter.getPkUsuario(), "and");
		
		// Siempre ordenaremos por usuario
		order += CAMPOS_USUARIO;
		
		// Si es total, agruparemos por usuario.
		if (!desglose) {
			group += CAMPOS_USUARIO;
		}
				
		// Formamos la query.
		query = construirConsulta (campos, from, where, order, group, desglose);
		
		log.debug (query);
		
		return query;
		
	}
	
	
	/**
	 * Query para obtener firmas por usuario 
	 * @param desglose indica si se quiere desglosada o totalizadas.
	 * @param filtro Filtro de la consulta
	 * @return query
	 */

	public static String obtenerQueryFxU (boolean desglose, 
										  StatsQueryFilter filter) {
		
		String sedes = listaSedesParentesis(filter.getSedesList());
		
		String query = "";
		
		String campos = "select ";
		String from = " from ";
		String where = " where ";
		String order = " order by ";
		String group = " group by ";
		
		// Siempre obtenemos el nombre y apellidos de los usuarios.

		campos += CAMPOS_USUARIO + ", ";
		// Si es desglose, obtenemos el asunto de la petición y el documento
		if (desglose) {
			campos +=  "PET.D_ASUNTO AS ASUNTO, DOC.D_NOMBRE AS DOCUMENTO ";
		// Si es total, obtenemos el total de firmas
		} else {
			campos +=  "count(FIR.X_FIRMA) N_FIRMAS ";
		}
		
		// Tablas comunes del from
		from += " PF_USUARIOS USU, PF_FIRMAS FIR";
		
		// Si es desglose, también unimos la tabla de peticiones (porque necesitamos el asunto)
		// y la de documentos.
		if (desglose || !Util.esVacioONulo(filter.getPkAplicacion())) {
			from += ", PF_DOCUMENTOS DOC, PF_PETICIONES PET"; 
		}
		
		// Where común.
		where += " USU.X_USUARIO = FIR.USU_X_USUARIO and " +
				 " USU.PROV_X_PROVINCIA IN " + sedes + " and " +
				 " USU.C_TIPO = 'USUARIO' and " +
				 " FIR.C_TIPO != 'VISTOBUENO'";
				 
		// Si es desglose, metemos la cláusula de cruzar la tabla de firmas con la de documentos,
		// y la de documentos con petición.
		if (desglose || !Util.esVacioONulo(filter.getPkAplicacion())) {
			where += " and FIR.DOC_X_DOCUMENTO = DOC.X_DOCUMENTO ";
			where += " and DOC.PET_X_PETICION = PET.X_PETICION ";
		}
		
		where += addFiltroFecha ( "FIR", "F_CREADO", ">=", filter.getfInicio(), DATE_FORMAT, "and");
		where += addFiltroFecha ( "FIR", "F_CREADO", "<=", filter.getfFin(), DATE_FORMAT, "and");
		where += addFiltroGenerico ("PET", "APL_X_APLICACION", "=", filter.getPkAplicacion(), "and");
		where += addFiltroGenerico ("USU", "X_USUARIO", "=", filter.getPkUsuario(), "and");
		
		// Siempre ordenaremos por usuario
		order += CAMPOS_USUARIO;
		
		// Si es total, agruparemos por usuario.
		if (!desglose) {
			group += CAMPOS_USUARIO;
		}
		
		
		// Formamos la query.
		query = construirConsulta (campos, from, where, order, group, desglose);
		
		log.debug (query);
		
		return query;
		
	}
	
	
	/**
	 * Query para obtener peticiones por sede del remitente 
	 * @param desglose indica si se quiere desglosada o totalizadas.
	 *@param filtro Filtro de la consulta
	 * @return query
	 */
	public static String obtenerQueryPxS (boolean desglose, 
										  StatsQueryFilter filter) {
		
		
		String sedes = listaSedesParentesis(filter.getSedesList());
		
		String query = "";
		
		String campos = "select ";
		String from = " from ";
		String where = " where ";
		String order = " order by ";
		String group = " group by ";
		
		// Siempre obtenemos el nombre de la sede.
		campos += "PROV.C_NOMBRE SEDE, ";
		// Si es desglose, obtenemos el asunto y referencia de cada peticion
		if (desglose) {
			campos +=  //"USU.D_NOMBRE || ' ' || USU.D_APELL1 || ' ' || USU.D_APELL2 AS REMITENTE, " + 
						"PET.D_ASUNTO AS ASUNTO, PET.D_REFERENCIA AS REFERENCIA ";
		// Si es total, obtenemos el total de peticiones
		} else {
			campos +=  "count(REM.X_USUARIO_REMITENTE) N_PETICIONES ";
		}
		
		// Tablas comunes del from
		from += " PF_USUARIOS USU, PF_USUARIOS_REMITENTE REM, PF_PROVINCIA PROV";
		
		// Si es desglose, también unimos la tabla de peticiones (porque necesitamos el asunto y la referencia)
		if (desglose || !Util.esVacioONulo(filter.getPkAplicacion())) {
			from += ", PF_PETICIONES PET"; 
		}
		
		// Where común.
		where += " USU.X_USUARIO = REM.USU_X_USUARIO and " +
				 " USU.PROV_X_PROVINCIA IN " + sedes + " and " +
				 " USU.PROV_X_PROVINCIA = PROV.X_PROVINCIA and " +
				 " USU.C_TIPO = 'USUARIO'" ;
				 
		// Si es desglose, metemos la cláusula de cruzar la tabla de peticiones con la de remitentes.
		if (desglose || !Util.esVacioONulo(filter.getPkAplicacion())) {
			where += " and PET.X_PETICION = REM.PET_X_PETICION ";
		}
		
		where += addFiltroFecha ( "REM", "F_CREADO", ">=", filter.getfInicio(), DATE_FORMAT, "and");
		where += addFiltroFecha ( "REM", "F_CREADO", "<=", filter.getfFin(), DATE_FORMAT, "and");
		where += addFiltroGenerico ("PET", "APL_X_APLICACION", "=", filter.getPkAplicacion(), "and");
		where += addFiltroGenerico ("USU", "X_USUARIO", "=", filter.getPkUsuario(), "and");
				
		// Siempre ordenaremos por sede
		order += " PROV.C_NOMBRE ";
		
		// Si es total, agruparemos por sede
		if (!desglose) {
			group += " PROV.C_NOMBRE ";
		}
		
		
		// Formamos la query.
		query = construirConsulta (campos, from, where, order, group, desglose);
		
		log.debug (query);
		
		return query;
		
	}
	
	/**
	 * Query para obtener firmas por sede del firmante 
	 * @param desglose indica si se quiere desglosada o totalizadas.
	 * @param filtro Filtro de la consulta
	 * @return query
	 */
	public static String obtenerQueryFxS (boolean desglose, 
										  StatsQueryFilter filter) {
		
		
		String sedes = listaSedesParentesis(filter.getSedesList());
		
		String query = "";
		
		String campos = "select ";
		String from = " from ";
		String where = " where ";
		String order = " order by ";
		String group = " group by ";
		
		// Siempre obtenemos el nombre y apellidos de los usuarios.
		campos += "PROV.C_NOMBRE, ";
		// Si es desglose, obtenemos el asunto de la petición y el documento
		if (desglose) {
			campos +=  	" PET.D_ASUNTO AS ASUNTO, DOC.D_NOMBRE AS DOCUMENTO ";
		// Si es total, obtenemos el total de firmas
		} else {
			campos +=  "count(FIR.X_FIRMA) N_FIRMAS ";
		}
		
		// Tablas comunes del from
		from += " PF_USUARIOS USU, PF_FIRMAS FIR, PF_PROVINCIA PROV";
		
		// Si es desglose, también unimos la tabla de peticiones (porque necesitamos el asunto)
		// y la de documentos.
		if (desglose || !Util.esVacioONulo(filter.getPkAplicacion())) {
			from += ", PF_DOCUMENTOS DOC, PF_PETICIONES PET"; 
		}
		
		// Where común.
		where += " USU.X_USUARIO = FIR.USU_X_USUARIO and " +
				 " USU.PROV_X_PROVINCIA IN " + sedes + " and " +
				 " USU.PROV_X_PROVINCIA = PROV.X_PROVINCIA and " +
				 " USU.C_TIPO = 'USUARIO' and " +
				 " FIR.C_TIPO != 'VISTOBUENO'" ;
				 
				 
		// Si es desglose, metemos la cláusula de cruzar la tabla de firmas con la de documentos,
		// y la de documentos con petición.
		if (desglose || !Util.esVacioONulo(filter.getPkAplicacion())) {
			where += " and FIR.DOC_X_DOCUMENTO = DOC.X_DOCUMENTO ";
			where += " and DOC.PET_X_PETICION = PET.X_PETICION ";
		}
		
		where += addFiltroFecha ( "FIR", "F_CREADO", ">=", filter.getfInicio(), DATE_FORMAT, "and");
		where += addFiltroFecha ( "FIR", "F_CREADO", "<=", filter.getfFin(), DATE_FORMAT, "and");
		where += addFiltroGenerico ("PET", "APL_X_APLICACION", "=", filter.getPkAplicacion(), "and");
		where += addFiltroGenerico ("USU", "X_USUARIO", "=", filter.getPkUsuario(), "and");
		//where += addFiltroAnio  (where, "FIR", "F_CREADO", anio, "and");
				
		// Siempre ordenaremos por sede
		order += " PROV.C_NOMBRE ";
		
		// Si es total, agruparemos por sede.
		if (!desglose) {
			group += " PROV.C_NOMBRE ";
		}
		
		
		// Formamos la query.
		query = construirConsulta (campos, from, where, order, group, desglose);
		
		log.debug (query);
		
		return query;
		
	}
	
	/**
	 * Query para obtener peticiones por sede del remitente 
	 * @param desglose indica si se quiere desglosada o totalizadas.
	 * @param filtro Filtro de la consulta
	 * @param porMes agrupado por mes y año
	 * @return query
	 */
	public static String obtenerQueryPxSU (boolean desglose,
										  StatsQueryFilter filter,
										  boolean porMes) {
		String sedes = listaSedesParentesis(filter.getSedesList());
		
		String query = "";
		
		String campos = "select ";
		String from = " from ";
		String where = " where ";
		String order = " order by ";
		String group = " group by ";
		
		// Siempre obtenemos el nombre de la sede.
		campos += "PROV.C_NOMBRE SEDE, " + CAMPOS_USUARIO;
		if (porMes) {
			campos += ", EXTRACT(YEAR FROM REM.F_CREADO), EXTRACT(MONTH FROM REM.F_CREADO) ";
		}		

		// Si es desglose, obtenemos el asunto y referencia de cada peticion
		if (desglose) {
			campos += ", PET.D_ASUNTO AS ASUNTO, PET.D_REFERENCIA AS REFERENCIA ";
		// Si es total, obtenemos el total de peticiones
		} else {
			campos +=  ", count(REM.X_USUARIO_REMITENTE) N_PETICIONES ";
		}
		
		// Tablas comunes del from
		from += " PF_USUARIOS USU, PF_USUARIOS_REMITENTE REM, PF_PROVINCIA PROV";
		
		// Si es desglose, también unimos la tabla de peticiones (porque necesitamos el asunto y la referencia)
		if (desglose || !Util.esVacioONulo(filter.getPkAplicacion())) {
			from += ", PF_PETICIONES PET"; 
		}
		
		// Where común.
		where += " USU.X_USUARIO = REM.USU_X_USUARIO and " +
				 " USU.PROV_X_PROVINCIA IN " + sedes + " and " +
				 " USU.PROV_X_PROVINCIA = PROV.X_PROVINCIA and " +
				 " USU.C_TIPO = 'USUARIO'" ;
				 
		// Si es desglose, metemos la cláusula de cruzar la tabla de peticiones con la de remitentes.
		if (desglose || !Util.esVacioONulo(filter.getPkAplicacion())) {
			where += " and PET.X_PETICION = REM.PET_X_PETICION ";
		}
		
		where += addFiltroFecha ( "REM", "F_CREADO", ">=", filter.getfInicio(), DATE_FORMAT, "and");
		where += addFiltroFecha ( "REM", "F_CREADO", "<=", filter.getfFin(), DATE_FORMAT, "and");
		where += addFiltroGenerico ("PET", "APL_X_APLICACION", "=", filter.getPkAplicacion(), "and");
		where += addFiltroGenerico ("USU", "X_USUARIO", "=", filter.getPkUsuario(), "and");
		//where += addFiltroAnio  (where, "REM", "F_CREADO", anio, "and");
		
		// Siempre ordenaremos por sede
		order += " PROV.C_NOMBRE, " + CAMPOS_USUARIO;
		if (porMes) {
			order += ", EXTRACT(YEAR FROM REM.F_CREADO), EXTRACT(MONTH FROM REM.F_CREADO)";
		}
		
		// Si es total, agruparemos por sede
		if (!desglose) {
			group += " PROV.C_NOMBRE, " + CAMPOS_USUARIO;
			if (porMes) {
				group += ", EXTRACT(YEAR FROM REM.F_CREADO), EXTRACT(MONTH FROM REM.F_CREADO)";
			}
		}
		
		// Formamos la query.
		query = construirConsulta (campos, from, where, order, group, desglose);
		
		log.debug (query);
		
		return query;
	}
	
	/**
	 * Query para obtener firmas por sede delf irmante 
	 * @param desglose indica si se quiere desglosada o totalizadas.
	 * @param filtro Filtro de la consulta
	 * @porMes agrupado por mes y año
	 * @return query
	 */
	public static String obtenerQueryFxSU (boolean desglose, 
										   StatsQueryFilter filter,
										   boolean porMes) {
		
		
		String sedes = listaSedesParentesis(filter.getSedesList());
		
		String query = "";
		
		String campos = "select ";
		String from = " from ";
		String where = " where ";
		String order = " order by ";
		String group = " group by ";
		
		// Siempre obtenemos la sede y el nombre y apellidos de los usuarios.		
		campos += "PROV.C_NOMBRE, " + CAMPOS_USUARIO;
		
		if (porMes) {
			campos += ", EXTRACT(YEAR FROM FIR.F_CREADO), EXTRACT(MONTH FROM FIR.F_CREADO) ";
		}
		
		
		// Si es desglose, obtenemos el asunto de la petición y el documento
		if (desglose) {
			campos +=  ", PET.D_ASUNTO AS ASUNTO, DOC.D_NOMBRE AS DOCUMENTO ";
		// Si es total, obtenemos el total de firmas
		} else {
			campos +=  ", count(FIR.X_FIRMA) N_FIRMAS ";
		}
		
		// Tablas comunes del from
		from += " PF_USUARIOS USU, PF_FIRMAS FIR, PF_PROVINCIA PROV";
		
		// Si es desglose, también unimos la tabla de peticiones (porque necesitamos el asunto)
		// y la de documentos.
		if (desglose || !Util.esVacioONulo(filter.getPkAplicacion())) {
			from += ", PF_DOCUMENTOS DOC, PF_PETICIONES PET"; 
		}
		
		// Where común.
		where += " USU.X_USUARIO = FIR.USU_X_USUARIO and " +
				 " USU.PROV_X_PROVINCIA IN " + sedes + " and " +
				 " USU.PROV_X_PROVINCIA = PROV.X_PROVINCIA and " +
				 " USU.C_TIPO = 'USUARIO' and " +
				 " FIR.C_TIPO != 'VISTOBUENO'" ;
				 
				 
		// Si es desglose, metemos la cláusula de cruzar la tabla de firmas con la de documentos,
		// y la de documentos con petición.
		if (desglose || !Util.esVacioONulo(filter.getPkAplicacion())) {
			where += " and FIR.DOC_X_DOCUMENTO = DOC.X_DOCUMENTO ";
			where += " and DOC.PET_X_PETICION = PET.X_PETICION ";
		}
		
		where += addFiltroFecha ( "FIR", "F_CREADO", ">=", filter.getfInicio(), DATE_FORMAT, "and");
		where += addFiltroFecha ( "FIR", "F_CREADO", "<=", filter.getfFin(), DATE_FORMAT, "and");
		where += addFiltroGenerico ("PET", "APL_X_APLICACION", "=", filter.getPkAplicacion(), "and");
		where += addFiltroGenerico ("USU", "X_USUARIO", "=", filter.getPkUsuario(), "and");

		
		
		// Siempre ordenaremos por sede
		order += " PROV.C_NOMBRE , " + CAMPOS_USUARIO;
		
		if (porMes) {
			order +=  ", EXTRACT(YEAR FROM FIR.F_CREADO), EXTRACT(MONTH FROM FIR.F_CREADO) ";
		}
		
		// Si es total, agruparemos por sede.
		if (!desglose) {
			group += " PROV.C_NOMBRE, " + CAMPOS_USUARIO;
			if (porMes) {
				group +=  ", EXTRACT(YEAR FROM FIR.F_CREADO), EXTRACT(MONTH FROM FIR.F_CREADO)";
			}
		}
		
		
		// Formamos la query.
		query = construirConsulta (campos, from, where, order, group, desglose);
		
		log.debug (query);
		
		return query;
		
	}
	
	
	
	
	
	/**
	 * Query para obtener peticiones por sede del firmante y aplicación 
	 * @param desglose indica si se quiere desglosada o totalizadas.
	 * @param filtro Filtro de la consulta
	 * @param porMes agrupado por mes y año
	 * @return query
	 */
	public static String obtenerQueryPxSA(boolean desglose, 
										  StatsQueryFilter filter,										  										   
										  boolean porMes 
										  ) {
		// TODO desglose no se va a permitir.
		String query = parteComunPxSA (desglose, filter, porMes) ;
		query += " ORDER BY PROVINCIA, APLICACION";		
		log.debug (query);
		return query;
	}
	
	
	private static String parteComunPxSA (boolean desglose, 	
			 StatsQueryFilter filter,										  										   
			  boolean porMes ) {
		
		// TODO desglose no se va a permitir.
										  //boolean pFirma) {
		String sedes = listaSedesParentesis(filter.getSedesList());
		
		String query = "";
		
		query = "(SELECT q1.provincia AS PROVINCIA," +
				"  q1.aplicacion AS APLICACION,";

		if (porMes) {
			query += 
				"  EXTRACT(YEAR FROM q1.fecha)," +
				"  EXTRACT(MONTH FROM q1.fecha),";
		}

		if (desglose) {
			query += 
				" q1.asunto          AS asunto, "+
				" q1.referencia      AS referencia ";
		} else {
			query += 
				" COUNT(*) AS N_PETICIONES ";
		}

		query +=			    
			"FROM " + 
			"(SELECT " +
			"(SELECT prov.c_nombre as nombre " +
			"FROM pf_firmantes fir," +
			"	  pf_usuarios u," +
			"     pf_provincia prov " +
			"WHERE fir.lfir_x_linea_firma = q2.linea_firma " +
			"AND u.x_usuario              = fir.usu_x_usuario " +
			"AND prov.x_provincia         = u.prov_x_provincia " +
			"AND prov.x_provincia in " + sedes + " " +
			addFiltroGenerico ("U", "X_USUARIO", "=", filter.getPkUsuario(), "and") +
			" AND ROWNUM                   = 1 " +
			")             AS provincia," ;

		if (desglose) {
			query +=
				"q2.asunto AS asunto," +
				"q2.referencia      AS referencia, ";
		}

		query +=	    
			"q2.aplicacion AS aplicacion," +
			"q2.fecha      AS fecha " +
			"FROM " +
			"(SELECT q3.aplicacion AS aplicacion,";
		if (desglose) {
			query +=
				"q3.asunto AS asunto,"+
				"q3.referencia AS referencia, ";
		}	    


		query+=
			"q3.fecha      AS fecha," +
			"(SELECT x_linea_firma " +
			"FROM pf_lineas_firma " +
			"WHERE q3.peticion = pet_x_peticion " +
			"AND ROWNUM        = 1 " +
			") AS linea_firma " +
			"FROM " +
			"(SELECT pet.x_peticion AS peticion,";


			query +=
				"		  pet.d_asunto AS asunto," +
				"		  pet.d_referencia AS referencia,";
			query += "    app.d_aplicacion     AS aplicacion,";

		
		query +=

			"        pet.f_creado         AS fecha " +
			" FROM pf_peticiones pet," +
			"      pf_aplicaciones app " +
			" WHERE app.x_aplicacion = pet.apl_x_aplicacion ";
		
		if (!Util.esVacioONulo(filter.getPkAplicacion())) {
			query += addFiltroGenerico ("app", "x_aplicacion", "=", filter.getPkAplicacion(), "and");
		}
		
		
		query +="  ) q3 " + 
		    " ) q2 " +
		    ") q1 ";
		
		query += " WHERE 1=1 ";
		
		query += addFiltroFecha ( "q1", "fecha", ">=", filter.getfInicio(), DATE_FORMAT, "and");
		query += addFiltroFecha ( "q1", "fecha", "<=", filter.getfFin(), DATE_FORMAT, "and");
		query += " and provincia is not null ";
		query +=
				"GROUP BY q1.provincia, q1.aplicacion ";
		
		if (porMes) {	
				query += 
			    "		  ,EXTRACT(YEAR FROM q1.fecha)" +
			    "		  ,EXTRACT(MONTH FROM q1.fecha)";
		}
		
		query +=
		") ";
		
		return query;
		
	}
	
	/**
	 * Query para obtener firmas por sede del firmante y aplicación 
	 * @param desglose indica si se quiere desglosada o totalizadas.
	 * @param filtro Filtro de la consulta
	 * @boolean porMes agrupado por mes y año
	 * 
	 * @return query
	 */
	public static String obtenerQueryFxSA(boolean desglose, 
			 							  StatsQueryFilter filter,										  										   
			 							  boolean porMes 
										  ) {
		// TODO desglose no se va a permitir.
		String query = parteComunFxSA (desglose, filter, porMes);
		query +=  " order by Sede";
		
		log.debug(query);
		
		return query;

	}
		
	private static String parteComunFxSA (boolean desglose, 
			 							  StatsQueryFilter filter,										  										   
			 							  boolean porMes ) {
										  //boolean pFirma) {
		// TODO desglose no se va a permitir.
		String sedes = listaSedesParentesis(filter.getSedesList());
		
		String query = "";
		
		query +=
				   "(select prov.c_nombre as Sede,";
		query +=
					"app.d_aplicacion as Aplicacion," ;
	        	   
	        	   
	    if (porMes) {
	    	query +=
	        	   "EXTRACT(YEAR FROM fir.f_creado)," +
	        	   "EXTRACT(MONTH FROM fir.f_creado),";
	    }
	    if (desglose) {
	    	query +=
	    		"pet.d_asunto asunto, " +
	    		"doc.d_nombre documento ";	        	   
	    } else {
	    	query +=
	    	   "count(fir.x_firma) as N_Firmas ";
	    }
	        	  
	    query +=
	        	   
	        "from pf_aplicaciones app " +
	        "join pf_peticiones pet on (pet.apl_x_aplicacion = app.x_aplicacion) " +
	        "join pf_lineas_firma lfir on (lfir.pet_x_peticion = pet.x_peticion) " +
	        "join pf_firmantes firm on (firm.lfir_x_linea_firma = lfir.x_linea_firma) " +
	        "join pf_firmas fir on (fir.fir_x_firmante = firm.x_firmante) " +
	        "join pf_usuarios usu on (usu.x_usuario = fir.usu_x_usuario) " +
	        "join pf_provincia prov on (usu.prov_x_provincia = prov.x_provincia) ";
	        
	   if (desglose) {
		   query +=
		    "join pf_documentos doc on (pet.x_peticion = doc.pet_x_peticion) ";
	   }
	    
	   query += " where prov.x_provincia in " + sedes + " ";
	   
	   query += addFiltroGenerico ("app", "x_aplicacion", "=", filter.getPkAplicacion(), "and");
	   query += addFiltroGenerico ("USU", "X_USUARIO", "=", filter.getPkUsuario(), "and");
	   
	   
	   query += addFiltroFecha ("fir", "f_creado", ">=", filter.getfInicio(), DATE_FORMAT, "and");
	   query += addFiltroFecha ("fir", "f_creado", "<=", filter.getfFin(), DATE_FORMAT, "and");
	   query +=
		   "group by prov.c_nombre,app.d_aplicacion";
	   if (porMes) {
		   query +=
			   "  ,EXTRACT(YEAR FROM fir.f_creado) " +
			   "  ,EXTRACT(MONTH FROM fir.f_creado) ";			   
	   }
	   
	   query += ")";
	   
	   return query;
		
	}
	
	/**
	 * Peticiones de sedes afectadas, que es la UNION de:
	 * - Peticiones en las que el remitente pertenece a una de las sedes.
	 * - Peticiones en las que el destinatario pertenece a una de las sedes.
	 * @param seats
	 * @return
	 */
	public static String obtenerQueryRequests (List<AbstractBaseDTO> seats) {
		
		String query = "";
		
		String sedes = listaSedesParentesis(seats);
		
		query = 
			//"select count (1) from ( " +
			
			  "(select distinct(x_peticion) from pf_peticiones pet, " +  
			          "          pf_usuarios_remitente rem, " +  
			          "          pf_usuarios usu," +  
			          "          pf_provincia prov" +  
			                    
			   " where  x_peticion = rem.pet_x_peticion and " +  
			           "rem.usu_x_usuario = usu.x_usuario and " +  
			           "usu.prov_x_provincia = prov.x_provincia and +  " +  
			           "x_provincia in " + sedes + 
			  ") "+
			  
			  " UNION " +
			  
			  "(select distinct(x_peticion) from pf_peticiones pet, " +    
			  "                  pf_etiquetas_peticion eti, " +
			  "                  pf_usuarios usu, " +
			  "                  pf_provincia prov " +
			                    
			  "where  x_peticion = eti.pet_x_peticion and " +
			  "       eti.usu_x_usuario = usu.x_usuario and " +
			  "       usu.prov_x_provincia = prov.x_provincia and " +
			  "       x_provincia in " + sedes + 
			  ") ";
			  
		  //") ";
		
		return query;
		
	}
	
	/**
	 * Firmas de sedes afectadas, que es la UNION de:
	 * - Firmas en las que el remitente de la petición pertenece a una de las sedes.
	 * - Firmas en las que el usuario que hace la firma a una de las sedes.
	 * @param seats
	 * @return
	 */
	public static String obtenerQuerySignatures (List<AbstractBaseDTO> seats) {
		
		String query = "";
		
		String sedes = listaSedesParentesis(seats);
		
		query =  
				// firmas de peticiones en las que el remitente pertenece a una de las sedes.
				"(select distinct(x_firma)   from pf_peticiones pet, " +   
                "  pf_usuarios_remitente rem,  " +
                "  pf_usuarios usu, " +
                "  pf_provincia prov, " +
                "  pf_lineas_firma lin, " +
                "  pf_firmantes firm, " +
                "  pf_firmas fir " +
                  
				"where  x_peticion = rem.pet_x_peticion and " +
				"       rem.usu_x_usuario = usu.x_usuario and " +
				"       usu.prov_x_provincia = prov.x_provincia and " +
				"       lin.pet_x_peticion = x_peticion and " +
				"       firm.lfir_x_linea_firma = lin.x_linea_firma and " +
				"       fir.fir_x_firmante = firm.x_firmante and " +
				"       fir.c_tipo != 'VISTOBUENO' and " +				
				"       x_provincia in " + sedes + 
				") " +
				
				" UNION " +
				// firmas en las que el usuario firmante pertenece a una de las sedes.
				"(select distinct(x_firma) from " +                     
                "  pf_usuarios usu, " +
                "  pf_provincia prov, " +                                      
                "  pf_firmas fir  " +
                  
                "where  fir.usu_x_usuario = usu.x_usuario and " +         
                "   usu.prov_x_provincia = prov.x_provincia and " +
                "   fir.c_tipo != 'VISTOBUENO' and " +
                "   x_provincia in " + sedes + 
				") ";
		
		return query;
		
	}
	

	
	
	/**
	 * Aplicaciones que no son derivadas de PFIRMA	 * 
	 * @return
	 */
	public static String obtenerQueryApplications () {
		
		String query = "";
		// Aplicaciones, sin contar las derivadas de PFIRMA (PFIRMA_XADES, PFIRMA_CADES, etc).
		query = 
				" select x_aplicacion, c_aplicacion from pf_aplicaciones " +
				" where c_aplicacion NOT LIKE '%PFIRMA_%' ";
		
		return query;
		
	}
	
	/**
	 * Devuelve una cadena con la sentencia para el filtro de una fecha.
	 * @param nombreTabla
	 * @param nombreCampo
	 * @param comparador
	 * @param fecha
	 * @param dateFormat
	 * @param relacionalPrevio
	 * @return
	 */
	private static String addFiltroFecha (final String nombreTabla, 
										 final String nombreCampo, 
										 final String comparador,
										 final String fecha,
										 final String dateFormat,
										 final String relacionalPrevio
										 ) {
		
		String concat = "";
		if (!Util.esVacioONulo(fecha)) {
			
			concat = " " + relacionalPrevio + " " + nombreTabla + "." + nombreCampo + " " + comparador + " to_date('" + fecha + "', '" + dateFormat + "')";
		}
		return concat;
	}
	
	/**
	 * Devuelve una cadena con la sentencia para un filtro generico
	 * @param nombreTabla
	 * @param nombreCampo
	 * @param comparador
	 * @param valor
	 * @param relacionalPrevio
	 * @return
	 */
	private static String addFiltroGenerico   (final String nombreTabla,
											   final String nombreCampo,
											   final String comparador,
											   final String valor,											   
											   final String relacionalPrevio) {
		String concat = "";
		if (!Util.esVacioONulo(valor)) {			
			concat = " " + relacionalPrevio + " " + nombreTabla + "." + nombreCampo + " " + comparador + " " + valor;
		}
		return concat;
	}
	
	/**
	 * Devuelve una cadena, con las pk de las sedes concatenadas por el carácter "sep"
	 * @param seats
	 * @param sep
	 * @return
	 */
	private static String seatsConcat (List<AbstractBaseDTO> seats, String sep) {
		boolean primero = true;
		String sedes = "";
		for (AbstractBaseDTO dto : seats) {
			PfProvinceDTO seat = (PfProvinceDTO) dto;
			if (primero) {
				sedes = sedes + seat.getPrimaryKeyString();
				primero = false;
			} else {
				sedes = sedes + sep + seat.getPrimaryKeyString();
			}
		}
		return sedes;
	}
	
	private static String envuelveConParentesis (String cadena) {
		return "(" + cadena + ")";
	}
	
	private static String listaSedesParentesis (List<AbstractBaseDTO> seats) {
		return envuelveConParentesis (seatsConcat (seats, ","));
	}
	
	private static String construirConsulta (String campos, String from, String where, String order, String group, boolean desglose) {
		// Formamos la query.
		String query = campos + from + where;
		
		if (desglose) {
			query += order;  
		} else {
			query += group + order;
		}
		return query;
	}
}

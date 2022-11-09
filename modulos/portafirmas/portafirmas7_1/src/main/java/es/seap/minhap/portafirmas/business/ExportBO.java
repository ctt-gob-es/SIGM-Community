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

package es.seap.minhap.portafirmas.business;

import java.util.Properties;

import javax.annotation.Resource;

import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;
//import es.seap.minhap.portafirmas.utils.export.ExportService;
//import es.seap.minhap.portafirmas.utils.export.ExportServiceFactory;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class ExportBO{


//	@Autowired
//	private ExportServiceFactory exportServiceFactory;

	@Resource(name = "messageProperties")
	private Properties messages;


//	@Autowired
//	private StatisticsVO statisticsVO;
//
//	@Autowired
//	private InboxVO inboxVO;

	

//	public void exportStatistics(HttpServletRequest request, HttpServletResponse response)
//	throws Exception {
//
//		response.resetBuffer();
//
//		// Parámetros de la estadística //
//		Map<String, Object> parameters = new HashMap<String, Object>();
//		parameters.put("seats", statisticsVO.getSeats());
//
//		ExportService exportService = exportServiceFactory.createStatisticsExportService();
//
//		// statistic type
//		String type = request.getParameter("type");
//
//		// Year filter
//		String year = request.getParameter("year");
//		
//		// create table
//		TableProperties properties = TableProperties.getInstance(request);
//		TableModel tableModel = exportService.createTableModel(properties,
//				request.getCharacterEncoding());
//
//		String[] headersShow = null;
//		List<Object[]> rows = null;
//
//		// Peticiones por usuario //
//		if (type.equals(Constants.REQUESTS_BY_USER)) {
//			headersShow = new String[]{ Constants.USER, Constants.N_REQUESTS };
//			if (!year.equals("Todos") && !year.equals("") && year != null) {
//				parameters.put("year", Integer.parseInt(year));
//				rows = baseDAO.queryStandardResult("request.requestsByUserAndYear", parameters);
//			} else {
//				rows = baseDAO.queryStandardResult("request.requestsByUser", parameters);
//			}
//		} else if (type.equals(Constants.REQUESTS_BY_SEAT)) {
//			headersShow = new String[]{ Constants.SEAT, Constants.N_REQUESTS };
//			if (!year.equals("Todos") && !year.equals("") && year != null) {
//				parameters.put("year", Integer.parseInt(year));
//				rows = baseDAO.queryStandardResult("request.requestsBySeatAndYear", parameters);
//			} else {
//				rows = baseDAO.queryStandardResult("request.requestsBySeat", parameters);
//			}
//		} else if (type.equals(Constants.REQUESTS_BY_DATE_USER_SEAT)) {
//			headersShow = new String[]{ Constants.SEAT, Constants.USER, Constants.MONTH, Constants.YEAR, Constants.N_REQUESTS };
//			if (!year.equals("Todos") && !year.equals("") && year != null) {
//				parameters.put("year", Integer.parseInt(year));
//				rows = baseDAO.queryStandardResult("request.requestsByDateUserSeatAndYear", parameters);
//			} else {
//				rows = baseDAO.queryStandardResult("request.requestsByDateUserSeat", parameters);
//			}
//		} else if (type.equals(Constants.SIGNS_BY_USER)) {
//			headersShow = new String[]{ Constants.USER, Constants.N_SIGNS };
//			if (!year.equals("Todos") && !year.equals("") && year != null) {
//				parameters.put("year", Integer.parseInt(year));
//				rows = baseDAO.queryStandardResult("request.signsByUserAndYear", parameters);
//			} else {
//				rows = baseDAO.queryStandardResult("request.signsByUser", parameters);
//			}
//		} else if (type.equals(Constants.SIGNS_BY_SEAT)) {
//			headersShow = new String[]{ Constants.SEAT, Constants.N_SIGNS };
//			if (!year.equals("Todos") && !year.equals("") && year != null) {
//				parameters.put("year", Integer.parseInt(year));
//				rows = baseDAO.queryStandardResult("request.signsBySeatAndYear", parameters);
//			} else {
//				rows = baseDAO.queryStandardResult("request.signsBySeat", parameters);
//			}
//		} else if (type.equals(Constants.SIGNS_BY_DATE_USER_SEAT)) {
//			headersShow = new String[]{ Constants.SEAT, Constants.USER, Constants.MONTH, Constants.YEAR, Constants.N_SIGNS };
//			if (!year.equals("Todos") && !year.equals("") && year != null) {
//				parameters.put("year", Integer.parseInt(year));
//				rows = baseDAO.queryStandardResult("request.signsByDateUserSeatAndYear", parameters);
//			} else {
//				rows = baseDAO.queryStandardResult("request.signsByDateUserSeat", parameters);
//			}
//		} else if (type.equals(Constants.SIGNS_AND_REQUESTS_BY_APP_SEAT)) {
//			headersShow = new String[]{ Constants.SEAT, Constants.APPLICATION, Constants.N_SIGNS, Constants.N_REQUESTS };
//			if (!year.equals("Todos") && !year.equals("") && year != null) {
//				rows = baseDAO.querySQLStandardResult(obtenerQueryFPxSAY(statisticsVO.getSeats(), year));
//			} else {
//				rows = baseDAO.querySQLStandardResult(obtenerQueryFPxSA(statisticsVO.getSeats()));
//			}
//		} else if (type.equals(Constants.SIGNS_AND_REQUESTS_BY_APP_SEAT_DATE)) {
//			headersShow = new String[]{ Constants.SEAT, Constants.APPLICATION,
//										Constants.MONTH, Constants.YEAR,
//										Constants.N_SIGNS, Constants.N_REQUESTS };
//			if (!year.equals("Todos") && !year.equals("") && year != null) {
//				rows = baseDAO.querySQLStandardResult(obtenerQueryFPxSAFY(statisticsVO.getSeats(), year));
//			} else {
//				rows = baseDAO.querySQLStandardResult(obtenerQueryFPxSAF(statisticsVO.getSeats()));
//			}
//		} else if (type.equals(Constants.SIGNS_BY_APP_SEAT_DATE)) {
//			headersShow = new String[]{ Constants.SEAT, Constants.APPLICATION,
//					Constants.MONTH, Constants.YEAR, Constants.N_SIGNS };
//			if (!year.equals("Todos") && !year.equals("") && year != null) {
//				rows = baseDAO.querySQLStandardResult(obtenerQueryFxASFY(statisticsVO.getSeats(), year));
//			} else {
//				rows = baseDAO.querySQLStandardResult(obtenerQueryFxASF(statisticsVO.getSeats()));
//			}
//		} else if (type.equals(Constants.SIGNS_BY_APP_SEAT)) {
//			headersShow = new String[]{ Constants.SEAT, Constants.APPLICATION, Constants.N_SIGNS };
//			if (!year.equals("Todos") && !year.equals("") && year != null) {
//				rows = baseDAO.querySQLStandardResult(obtenerQueryFxASY(statisticsVO.getSeats(), year));
//			} else {
//				rows = baseDAO.querySQLStandardResult(obtenerQueryFxAS(statisticsVO.getSeats()));
//			}
//		} else if (type.equals(Constants.REQUESTS_BY_APP_SEAT_DATE)) {
//			headersShow = new String[]{ Constants.SEAT, Constants.APPLICATION,
//					Constants.MONTH, Constants.YEAR, Constants.N_REQUESTS };
//			if (!year.equals("Todos") && !year.equals("") && year != null) {
//				rows = baseDAO.querySQLStandardResult(obtenerQueryPxASFY(statisticsVO.getSeats(), year));
//			} else {
//				rows = baseDAO.querySQLStandardResult(obtenerQueryPxASF(statisticsVO.getSeats()));
//			}
//		} else if (type.equals(Constants.REQUESTS_BY_APP_SEAT)) {
//			headersShow = new String[]{ Constants.SEAT, Constants.APPLICATION, Constants.N_REQUESTS };
//			if (!year.equals("Todos") && !year.equals("") && year != null) {
//				rows = baseDAO.querySQLStandardResult(obtenerQueryPxASY(statisticsVO.getSeats(), year));
//			} else {
//				rows = baseDAO.querySQLStandardResult(obtenerQueryPxAS(statisticsVO.getSeats()));
//			}
//		} else {
//			headersShow = new String[]{ Constants.SEAT, Constants.N_USERS };
//			rows = baseDAO.queryStandardResult("request.usersBySeat", parameters);
//		}
//
//		// load headers
//		exportService.loadHeaders(tableModel, headersShow);
//
//		// load rows
//		exportService.loadRowsStatistics(tableModel, rows);
//		
//		exportService.export(response, tableModel, type);
//		
//		response.flushBuffer();
//	}

	/**
	 * Método que obtiene la consulta nativa SQL para la estadística de peticiones y firmas por sede, aplicación y fecha
	 * @param parameters Parámetros de la consulta (sedes)
	 * @return Consulta sql nativa
	 */
//	private String obtenerQueryFPxSAF(List<AbstractBaseDTO> seats) {
//		String query = "";
//
//		query = query + "select " +
//						 "c_nombre as Sede," +
//						 "c_aplicacion as Aplicacion," +
//						 "mes as Mes," +
//						 "anio as Anio," +
//						 "sum(firmas) as Firmas," +
//						 "sum(peticiones) as Peticiones " +
//						 "from (" +
//							"select " +
//							    "q1.c_nombre," +
//							    "q1.c_aplicacion," +
//							    "q1.x_provincia," +
//							    "q1.x_aplicacion," +
//							    "extract(MONTH from q1.f_creado) as Mes," +
//							    "extract(YEAR from q1.f_creado) as Anio," +
//							    
//							    "(select count(1) from pf_usuarios_remitente urem " +
//							    "inner join pf_peticiones pet on urem.pet_x_peticion = pet.x_peticion " +
//							    "inner join pf_usuarios u on urem.usu_x_usuario = u.x_usuario " +
//							    "where " +
//							       "pet.apl_x_aplicacion = q1.x_aplicacion and " +
//							       "u.prov_x_provincia = q1.x_provincia and " +
//							       "pet.x_peticion = q1.x_peticion) as Peticiones," +    
//							    
//							    "(select count(1) from pf_usuarios u " +
//							    "inner join pf_firmas f on f.usu_x_usuario = u.x_usuario " +
//							    "inner join pf_firmantes fir on f.fir_x_firmante = fir.x_firmante " +
//							    "inner join pf_lineas_firma lf on fir.lfir_x_linea_firma = lf.x_linea_firma " +
//							    "inner join pf_peticiones pet on lf.pet_x_peticion = pet.x_peticion " +
//							    "where " + 
//							      "pet.apl_x_aplicacion = q1.x_aplicacion and " +
//							      "u.prov_x_provincia = q1.x_provincia and " +
//							      "pet.x_peticion = q1.x_peticion) as Firmas " +
//							"from " +
//							"(" +
//							  "select " +
//							      "p.c_nombre," +
//							      "a.c_aplicacion," +
//							      "p.x_provincia," +
//							      "a.x_aplicacion," +
//							      "peti.x_peticion," +
//							      "trunc(peti.f_creado, 'MM') f_creado " +
//							    "from pf_aplicaciones a, pf_peticiones peti, pf_provincia p " +
//							    "where a.x_aplicacion = peti.apl_x_aplicacion and " +
//							          "p.x_provincia in (";
//
//		// Indicamos las sedes
//		boolean primero = true;
//		String sedes = "";
//		for (AbstractBaseDTO dto : seats) {
//			PfProvinceDTO seat = (PfProvinceDTO) dto;
//
//			if (primero) {
//				sedes = sedes + seat.getPrimaryKeyString();
//				primero = false;
//			} else {
//				sedes = sedes + "," + seat.getPrimaryKeyString();
//			}
//		}
//
//		query = query + sedes + ") " +
//								") q1 " +
//								") q2 " +
//								"group by c_nombre, c_aplicacion, mes, anio " +
//								"order by c_aplicacion, anio, mes";
//
//		return query;
//	}

	/**
	 * Método que obtiene la consulta nativa SQL para la estadística de peticiones y firmas por sede, aplicación y fecha
	 * @param parameters Parámetros de la consulta (sedes)
	 * @return Consulta sql nativa
	 */
//	private String obtenerQueryFPxSAFY(List<AbstractBaseDTO> seats, String year) {
//		String query = "";
//
//		query = query + "select " +
//						 "c_nombre as Sede," +
//						 "c_aplicacion as Aplicacion," +
//						 "mes as Mes," +
//						 "anio as Anio," +
//						 "sum(firmas) as Firmas," +
//						 "sum(peticiones) as Peticiones " +
//						 "from (" +
//							"select " +
//							    "q1.c_nombre," +
//							    "q1.c_aplicacion," +
//							    "q1.x_provincia," +
//							    "q1.x_aplicacion," +
//							    "extract(MONTH from q1.f_creado) as Mes," +
//							    "extract(YEAR from q1.f_creado) as Anio," +
//							    
//							    "(select count(1) from pf_usuarios_remitente urem " +
//							    "inner join pf_peticiones pet on urem.pet_x_peticion = pet.x_peticion " +
//							    "inner join pf_usuarios u on urem.usu_x_usuario = u.x_usuario " +
//							    "where " +
//							       "pet.apl_x_aplicacion = q1.x_aplicacion and " +
//							       "u.prov_x_provincia = q1.x_provincia and " +
//							       "pet.x_peticion = q1.x_peticion) as Peticiones," +    
//							    
//							    "(select count(1) from pf_usuarios u " +
//							    "inner join pf_firmas f on f.usu_x_usuario = u.x_usuario " +
//							    "inner join pf_firmantes fir on f.fir_x_firmante = fir.x_firmante " +
//							    "inner join pf_lineas_firma lf on fir.lfir_x_linea_firma = lf.x_linea_firma " +
//							    "inner join pf_peticiones pet on lf.pet_x_peticion = pet.x_peticion " +
//							    "where " + 
//							      "pet.apl_x_aplicacion = q1.x_aplicacion and " +
//							      "u.prov_x_provincia = q1.x_provincia and " +
//							      "pet.x_peticion = q1.x_peticion and " +
//							      "extract(YEAR from f.f_creado) = " + year + 
//							      ") as Firmas " +
//							"from " +
//							"(" +
//							  "select " +
//							      "p.c_nombre," +
//							      "a.c_aplicacion," +
//							      "p.x_provincia," +
//							      "a.x_aplicacion," +
//							      "peti.x_peticion," +
//							      "trunc(peti.f_creado, 'MM') f_creado " +
//							    "from pf_aplicaciones a, pf_peticiones peti, pf_provincia p " +
//							    "where a.x_aplicacion = peti.apl_x_aplicacion and " +
//							    	  "extract(YEAR from peti.f_creado) = " + year +
//							          " and p.x_provincia in (";
//
//		// Indicamos las sedes
//		boolean primero = true;
//		String sedes = "";
//		for (AbstractBaseDTO dto : seats) {
//			PfProvinceDTO seat = (PfProvinceDTO) dto;
//
//			if (primero) {
//				sedes = sedes + seat.getPrimaryKeyString();
//				primero = false;
//			} else {
//				sedes = sedes + "," + seat.getPrimaryKeyString();
//			}
//		}
//
//		query = query + sedes + ") " +
//								") q1 " +
//								") q2 " +
//								"group by c_nombre, c_aplicacion, mes, anio " +
//								"order by c_aplicacion, anio, mes";
//
//		return query;
//	}

	/**
	 * Método que obtiene la consulta nativa SQL para la estadística de peticiones y firmas por sede y aplicación
	 * @param parameters Parámetros de la consulta (sedes)
	 * @return Consulta sql nativa
	 */
//	private String obtenerQueryFPxSA(List<AbstractBaseDTO> seats) {
//		String query = "";
//
//		query = query + "select " +
//					    "p.c_nombre as Sede," +
//					    "a.c_aplicacion as Aplicacion," +
//					
//					    "(select count(1) from pf_usuarios u " +
//					    "inner join pf_firmas f on f.usu_x_usuario = u.x_usuario " +
//					    "inner join pf_firmantes fir on f.fir_x_firmante = fir.x_firmante " +
//					    "inner join pf_lineas_firma lf on fir.lfir_x_linea_firma = lf.x_linea_firma " +
//					    "inner join pf_peticiones pet on lf.pet_x_peticion = pet.x_peticion " +
//					    "where " + 
//					      "pet.apl_x_aplicacion = a.x_aplicacion and u.prov_x_provincia = p.x_provincia) as Firmas ," +
//					  
//					    "(select count(1) from pf_usuarios_remitente urem " +
//					    "inner join pf_peticiones pet on urem.pet_x_peticion = pet.x_peticion " +
//					    "inner join pf_usuarios u on urem.usu_x_usuario = u.x_usuario " +
//					    "where " +
//					      "pet.apl_x_aplicacion = a.x_aplicacion and u.prov_x_provincia = p.x_provincia) as Peticiones " +
//					    
//					  "from pf_aplicaciones a, pf_provincia p " +
//					  "where p.x_provincia in (";
//		
//		// Indicamos las sedes
//		boolean primero = true;
//		String sedes = "";
//		for (AbstractBaseDTO dto : seats) {
//			PfProvinceDTO seat = (PfProvinceDTO) dto;
//
//			if (primero) {
//				sedes = sedes + seat.getPrimaryKeyString();
//				primero = false;
//			} else {
//				sedes = sedes + "," + seat.getPrimaryKeyString();
//			}
//		}
//		
//		query = query + sedes + ") " +
//								"group by " +
//					    		"p.x_provincia," +
//							    "p.c_nombre," +
//							    "a.x_aplicacion," +
//							    "a.c_aplicacion," +
//							    "a.d_aplicacion";
//
//		return query;
//	}

	/**
	 * Método que obtiene la consulta nativa SQL para la estadística de peticiones y firmas por sede y aplicación
	 * @param parameters Parámetros de la consulta (sedes)
	 * @return Consulta sql nativa
	 */
//	private String obtenerQueryFPxSAY(List<AbstractBaseDTO> seats, String year) {
//		String query = "";
//
//		query = query + "select " +
//					    "p.c_nombre as Sede," +
//					    "a.c_aplicacion as Aplicacion," +
//					
//					    "(select count(1) from pf_usuarios u " +
//					    "inner join pf_firmas f on f.usu_x_usuario = u.x_usuario " +
//					    "inner join pf_firmantes fir on f.fir_x_firmante = fir.x_firmante " +
//					    "inner join pf_lineas_firma lf on fir.lfir_x_linea_firma = lf.x_linea_firma " +
//					    "inner join pf_peticiones pet on lf.pet_x_peticion = pet.x_peticion " +
//					    "where " + 
//					      "pet.apl_x_aplicacion = a.x_aplicacion and u.prov_x_provincia = p.x_provincia " +
//					    "and" +
//					      "extract(YEAR from f.f_creado) = " + year + 
//					    " ) as Firmas ," +
//					  
//					    "(select count(1) from pf_usuarios_remitente urem " +
//					    "inner join pf_peticiones pet on urem.pet_x_peticion = pet.x_peticion " +
//					    "inner join pf_usuarios u on urem.usu_x_usuario = u.x_usuario " +
//					    "where " +
//					      "pet.apl_x_aplicacion = a.x_aplicacion and u.prov_x_provincia = p.x_provincia) as Peticiones " +
//					    
//					  "from pf_aplicaciones a, pf_provincia p " +
//					  "where p.x_provincia in (";
//		
//		// Indicamos las sedes
//		boolean primero = true;
//		String sedes = "";
//		for (AbstractBaseDTO dto : seats) {
//			PfProvinceDTO seat = (PfProvinceDTO) dto;
//
//			if (primero) {
//				sedes = sedes + seat.getPrimaryKeyString();
//				primero = false;
//			} else {
//				sedes = sedes + "," + seat.getPrimaryKeyString();
//			}
//		}
//		
//		query = query + sedes + ") " +
//								"group by " +
//					    		"p.x_provincia," +
//							    "p.c_nombre," +
//							    "a.x_aplicacion," +
//							    "a.c_aplicacion," +
//							    "a.d_aplicacion";
//
//		return query;
//	}

	/**
	 * Método que devuelve la query para obtener las firmas por aplicación, fecha y sede
	 * @param seats Listado de sedes
	 * @param year Año
	 * @return Listado de firmas
	 */
//	private String obtenerQueryFxASFY(List<AbstractBaseDTO> seats, String year) {
//		String query = "";
//
//		String sedes = querySeats(seats);
//		
//		query ="(select prov.c_nombre as Sede," +
//		        	   "app.d_aplicacion as Aplicacion," +
//		        	   "EXTRACT(MONTH FROM fir.f_creado)," +
//		        	   "EXTRACT(YEAR FROM fir.f_creado)," +
//		        	   "count(fir.x_firma) as N_Firmas " +
//		        "from pf_aplicaciones app " +
//		        "join pf_peticiones pet on (pet.apl_x_aplicacion = app.x_aplicacion) " +
//		        "join pf_lineas_firma lfir on (lfir.pet_x_peticion = pet.x_peticion) " +
//		        "join pf_firmantes firm on (firm.lfir_x_linea_firma = lfir.x_linea_firma) " +
//		        "join pf_firmas fir on (fir.fir_x_firmante = firm.x_firmante) " +
//		        "join pf_usuarios usu on (usu.x_usuario = fir.usu_x_usuario) " +
//		        "join pf_provincia prov on (usu.prov_x_provincia = prov.x_provincia) " +
//		        "where app.c_aplicacion not like 'PFIRMA%' " +
//		        "and   app.c_aplicacion not like 'APLIFICTICIA' " +
//		        "and   app.c_aplicacion not like 'PF_DGA' " +
//		        "and   EXTRACT(YEAR FROM fir.f_creado) = " + year + " " +
//		        "and   prov.x_provincia in " + sedes + " " +
//		        "group by prov.c_nombre,app.d_aplicacion," +
//		        "		  EXTRACT(MONTH FROM fir.f_creado)," +
//		        "		  EXTRACT(YEAR FROM fir.f_creado)) " +
//				"UNION " +
//				"(select prov.c_nombre as Sede," +
//				        "'Portafirmas' as Aplicacion," +
//				        "EXTRACT(MONTH FROM fir.f_creado)," +
//			        	"EXTRACT(YEAR FROM fir.f_creado)," +
//				        "count(fir.x_firma) as N_Firmas " +
//				"from pf_aplicaciones app " +
//				"join pf_peticiones pet on (pet.apl_x_aplicacion = app.x_aplicacion) " +
//				"join pf_lineas_firma lfir on (lfir.pet_x_peticion = pet.x_peticion) " +
//				"join pf_firmantes firm on (firm.lfir_x_linea_firma = lfir.x_linea_firma) " +
//				"join pf_firmas fir on (fir.fir_x_firmante = firm.x_firmante) " +
//				"join pf_usuarios usu on (usu.x_usuario = fir.usu_x_usuario) " +
//				"join pf_provincia prov on (usu.prov_x_provincia = prov.x_provincia) " +
//				"where app.c_aplicacion like 'PFIRMA%' " +
//				"and   app.c_aplicacion not like 'APLIFICTICIA' " +
//				"and   app.c_aplicacion not like 'PF_DGA' " +
//				"and   EXTRACT(YEAR FROM fir.f_creado) = " + year + " " +
//				"and   prov.x_provincia in " + sedes + " " +
//				"group by prov.c_nombre, 'Portafirmas'," +
//				"		  EXTRACT(MONTH FROM fir.f_creado)," +
//		        "		  EXTRACT(YEAR FROM fir.f_creado)) " +
//				"order by Sede";
//
//		return query;
//	}

	/**
	 * Método que devuelve la query para obtener las firmas por aplicación, fecha y sede
	 * @param seats Listado de sedes
	 * @return Listado de firmas
	 */
//	private String obtenerQueryFxASF(List<AbstractBaseDTO> seats) {
//		String query = "";
//
//		String sedes = querySeats(seats);
//		
//		query ="(select prov.c_nombre as Sede," +
//		        	   "app.d_aplicacion as Aplicacion," +
//		        	   "EXTRACT(MONTH FROM fir.f_creado)," +
//		        	   "EXTRACT(YEAR FROM fir.f_creado)," +
//		        	   "count(fir.x_firma) as N_Firmas " +
//		        "from pf_aplicaciones app " +
//		        "join pf_peticiones pet on (pet.apl_x_aplicacion = app.x_aplicacion) " +
//		        "join pf_lineas_firma lfir on (lfir.pet_x_peticion = pet.x_peticion) " +
//		        "join pf_firmantes firm on (firm.lfir_x_linea_firma = lfir.x_linea_firma) " +
//		        "join pf_firmas fir on (fir.fir_x_firmante = firm.x_firmante) " +
//		        "join pf_usuarios usu on (usu.x_usuario = fir.usu_x_usuario) " +
//		        "join pf_provincia prov on (usu.prov_x_provincia = prov.x_provincia) " +
//		        "where app.c_aplicacion not like 'PFIRMA%' " +
//		        "and   app.c_aplicacion not like 'APLIFICTICIA' " +
//		        "and   app.c_aplicacion not like 'PF_DGA' " +
//		        "and   prov.x_provincia in " + sedes + " " +
//		        "group by prov.c_nombre,app.d_aplicacion," +
//		        "		  EXTRACT(MONTH FROM fir.f_creado)," +
//		        "		  EXTRACT(YEAR FROM fir.f_creado)) " +
//				"UNION " +
//				"(select prov.c_nombre as Sede," +
//				        "'Portafirmas' as Aplicacion," +
//				        "EXTRACT(MONTH FROM fir.f_creado)," +
//			        	"EXTRACT(YEAR FROM fir.f_creado)," +
//				        "count(fir.x_firma) as N_Firmas " +
//				"from pf_aplicaciones app " +
//				"join pf_peticiones pet on (pet.apl_x_aplicacion = app.x_aplicacion) " +
//				"join pf_lineas_firma lfir on (lfir.pet_x_peticion = pet.x_peticion) " +
//				"join pf_firmantes firm on (firm.lfir_x_linea_firma = lfir.x_linea_firma) " +
//				"join pf_firmas fir on (fir.fir_x_firmante = firm.x_firmante) " +
//				"join pf_usuarios usu on (usu.x_usuario = fir.usu_x_usuario) " +
//				"join pf_provincia prov on (usu.prov_x_provincia = prov.x_provincia) " +
//				"where app.c_aplicacion like 'PFIRMA%' " +
//				"and   app.c_aplicacion not like 'APLIFICTICIA' " +
//				"and   app.c_aplicacion not like 'PF_DGA' " +
//				"and   prov.x_provincia in " + sedes + " " +
//				"group by prov.c_nombre, 'Portafirmas'," +
//				"		  EXTRACT(MONTH FROM fir.f_creado)," +
//		        "		  EXTRACT(YEAR FROM fir.f_creado)) " +
//				"order by Sede";
//
//		return query;
//	}

	/**
	 * Método que devuelve la query para obtener las firmas por aplicación y sede
	 * @param seats Listado de sedes
	 * @return Listado de firmas
	 */
//	private String obtenerQueryFxASY(List<AbstractBaseDTO> seats, String year) {
//		String query = "";
//
//		String sedes = querySeats(seats);
//		
//		query ="(select prov.c_nombre as Sede," +
//		        	   "app.d_aplicacion as Aplicacion," +
//		        	   "count(fir.x_firma) as N_Firmas " +
//		        "from pf_aplicaciones app " +
//		        "join pf_peticiones pet on (pet.apl_x_aplicacion = app.x_aplicacion) " +
//		        "join pf_lineas_firma lfir on (lfir.pet_x_peticion = pet.x_peticion) " +
//		        "join pf_firmantes firm on (firm.lfir_x_linea_firma = lfir.x_linea_firma) " +
//		        "join pf_firmas fir on (fir.fir_x_firmante = firm.x_firmante) " +
//		        "join pf_usuarios usu on (usu.x_usuario = fir.usu_x_usuario) " +
//		        "join pf_provincia prov on (usu.prov_x_provincia = prov.x_provincia) " +
//		        "where app.c_aplicacion not like 'PFIRMA%' " +
//		        "and   app.c_aplicacion not like 'APLIFICTICIA' " +
//		        "and   app.c_aplicacion not like 'PF_DGA' " +
//		        "and   EXTRACT(YEAR FROM fir.f_creado) = " + year + " " +
//		        "and   prov.x_provincia in " + sedes + " " +
//		        "group by prov.c_nombre,app.d_aplicacion) " +
//				"UNION " +
//				"(select prov.c_nombre as Sede," +
//				        "'Portafirmas' as Aplicacion," +
//				        "count(fir.x_firma) as N_Firmas " +
//				"from pf_aplicaciones app " +
//				"join pf_peticiones pet on (pet.apl_x_aplicacion = app.x_aplicacion) " +
//				"join pf_lineas_firma lfir on (lfir.pet_x_peticion = pet.x_peticion) " +
//				"join pf_firmantes firm on (firm.lfir_x_linea_firma = lfir.x_linea_firma) " +
//				"join pf_firmas fir on (fir.fir_x_firmante = firm.x_firmante) " +
//				"join pf_usuarios usu on (usu.x_usuario = fir.usu_x_usuario) " +
//				"join pf_provincia prov on (usu.prov_x_provincia = prov.x_provincia) " +
//				"where app.c_aplicacion like 'PFIRMA%' " +
//				"and   app.c_aplicacion not like 'APLIFICTICIA' " +
//				"and   app.c_aplicacion not like 'PF_DGA' " +
//				"and   EXTRACT(YEAR FROM fir.f_creado) = " + year + " " +
//				"and   prov.x_provincia in " + sedes + " " +
//				"group by prov.c_nombre, 'Portafirmas') " +
//				"order by Sede";
//
//		return query;
//	}

	/**
	 * Método que devuelve la query para obtener las firmas por aplicación y sede
	 * @param seats Listado de sedes
	 * @return Listado de firmas
	 */
//	private String obtenerQueryFxAS(List<AbstractBaseDTO> seats) {
//		String query = "";
//
//		String sedes = querySeats(seats);
//		
//		query ="(select prov.c_nombre as Sede," +
//		        	   "app.d_aplicacion as Aplicacion," +
//		        	   "count(fir.x_firma) as N_Firmas " +
//		        "from pf_aplicaciones app " +
//		        "join pf_peticiones pet on (pet.apl_x_aplicacion = app.x_aplicacion) " +
//		        "join pf_lineas_firma lfir on (lfir.pet_x_peticion = pet.x_peticion) " +
//		        "join pf_firmantes firm on (firm.lfir_x_linea_firma = lfir.x_linea_firma) " +
//		        "join pf_firmas fir on (fir.fir_x_firmante = firm.x_firmante) " +
//		        "join pf_usuarios usu on (usu.x_usuario = fir.usu_x_usuario) " +
//		        "join pf_provincia prov on (usu.prov_x_provincia = prov.x_provincia) " +
//		        "where app.c_aplicacion not like 'PFIRMA%' " +
//		        "and   app.c_aplicacion not like 'APLIFICTICIA' " +
//		        "and   app.c_aplicacion not like 'PF_DGA' " +
//		        "and   prov.x_provincia in " + sedes + " " +
//		        "group by prov.c_nombre,app.d_aplicacion) " +
//				"UNION " +
//				"(select prov.c_nombre as Sede," +
//				        "'Portafirmas' as Aplicacion," +
//				        "count(fir.x_firma) as N_Firmas " +
//				"from pf_aplicaciones app " +
//				"join pf_peticiones pet on (pet.apl_x_aplicacion = app.x_aplicacion) " +
//				"join pf_lineas_firma lfir on (lfir.pet_x_peticion = pet.x_peticion) " +
//				"join pf_firmantes firm on (firm.lfir_x_linea_firma = lfir.x_linea_firma) " +
//				"join pf_firmas fir on (fir.fir_x_firmante = firm.x_firmante) " +
//				"join pf_usuarios usu on (usu.x_usuario = fir.usu_x_usuario) " +
//				"join pf_provincia prov on (usu.prov_x_provincia = prov.x_provincia) " +
//				"where app.c_aplicacion like 'PFIRMA%' " +
//				"and   app.c_aplicacion not like 'APLIFICTICIA' " +
//				"and   app.c_aplicacion not like 'PF_DGA' " +
//				"and   prov.x_provincia in " + sedes + " " +
//				"group by prov.c_nombre, 'Portafirmas') " +
//				"order by Sede";
//
//		return query;
//	}

	/**
	 * Método que devuelve la query para obtener las peticiones por aplicación, fecha y sede
	 * @param seats Listado de sedes
	 * @param year Año
	 * @return Listado de peticiones
	 */
//	private String obtenerQueryPxASFY(List<AbstractBaseDTO> seats, String year) {
//		String query = "";
//
//		String sedes = querySeats(seats);
//
//		query = "(SELECT q1.provincia AS PROVINCIA," +
//			    "  q1.aplicacion AS APLICACION," +
//			    "  EXTRACT(MONTH FROM q1.fecha)," +
//	        	"  EXTRACT(YEAR FROM q1.fecha)," +
//			    "  COUNT(*) AS N_PETICIONES " +
//			    "FROM " + 
//			    "(SELECT " +
//			    "(SELECT prov.c_nombre as nombre " +
//			    "FROM pf_firmantes fir," +
//			    "	  pf_usuarios u," +
//			    "     pf_provincia prov " +
//			    "WHERE fir.lfir_x_linea_firma = q2.linea_firma " +
//			    "AND u.x_usuario              = fir.usu_x_usuario " +
//			    "AND prov.x_provincia         = u.prov_x_provincia " +
//			    "AND prov.x_provincia in " + sedes + " " +
//			    "AND ROWNUM                   = 1 " +
//			    ")             AS provincia," +
//			    "q2.aplicacion AS aplicacion," +
//			    "q2.fecha      AS fecha " +
//			    "FROM " +
//			    "(SELECT q3.aplicacion AS aplicacion," +
//			    "        q3.fecha            AS fecha," +
//			    "(SELECT x_linea_firma " +
//			     "FROM pf_lineas_firma " +
//			     "WHERE q3.peticion = pet_x_peticion " +
//			     "AND ROWNUM        = 1 " +
//			     ") AS linea_firma " +
//			    "FROM " +
//			     "(SELECT pet.x_peticion AS peticion," +
//			     "        app.c_aplicacion     AS aplicacion," +
//			     "        pet.f_creado         AS fecha " +
//			     " FROM pf_peticiones pet," +
//			     "      pf_aplicaciones app " +
//			     " WHERE app.x_aplicacion = pet.apl_x_aplicacion " +
//			     " AND app.c_aplicacion NOT LIKE 'PFIRMA%' " +
//			     " AND app.c_aplicacion NOT LIKE 'APLIFICTICIA' " +
//			     " AND app.c_aplicacion NOT LIKE 'PF_DGA' " +
//			     " ) q3 " +
//			     ") q2 " +
//			    ") q1 " +
//			    "WHERE EXTRACT(YEAR FROM q1.fecha) = " + year + " " +
//			    "GROUP BY q1.provincia, q1.aplicacion," +
//			    "		  EXTRACT(MONTH FROM q1.fecha)," +
//			    "		  EXTRACT(YEAR FROM q1.fecha)) " +
//			    "UNION " +
//			    "(SELECT q1.provincia AS PROVINCIA," +
//			    "		 q1.aplicacion AS APLICACION," +
//			    "  		 EXTRACT(MONTH FROM q1.fecha)," +
//	        	"  		 EXTRACT(YEAR FROM q1.fecha)," +
//			    "   	 COUNT(*) AS N_PETICIONES " +
//			    "FROM " + 
//			    "(SELECT " +
//			    "(SELECT prov.c_nombre as nombre " +
//			    " FROM pf_firmantes fir, " +
//			    " pf_usuarios u," +
//			    " pf_provincia prov " +
//			    " WHERE fir.lfir_x_linea_firma = q2.linea_firma " +
//			    " AND u.x_usuario              = fir.usu_x_usuario " +
//			    " AND prov.x_provincia         = u.prov_x_provincia " +
//			    " AND prov.x_provincia in " + sedes + " " +
//			    " AND ROWNUM                   = 1 " +
//			    ")             AS provincia, " +
//			    "q2.aplicacion AS aplicacion," +
//			    "q2.fecha      AS fecha " +
//			    "FROM " +
//			    "(SELECT q3.aplicacion AS aplicacion, " +
//			    "        q3.fecha            AS fecha, " +
//			    "(SELECT x_linea_firma " +
//			    " FROM pf_lineas_firma " +
//			    " WHERE q3.peticion = pet_x_peticion " +
//			    " AND ROWNUM        = 1 " +
//			    " ) AS linea_firma " +
//			    " FROM " +
//			    " (SELECT pet.x_peticion AS peticion, " +
//			    "         'PORTAFIRMAS'  AS aplicacion, " +
//			    "         pet.f_creado   AS fecha " +
//			    "  FROM pf_peticiones pet, " +
//			    "	    pf_aplicaciones app " +
//			    "  WHERE app.x_aplicacion = pet.apl_x_aplicacion " +
//			    "  AND app.c_aplicacion LIKE 'PFIRMA%' " +
//			    "  AND app.c_aplicacion NOT LIKE 'APLIFICTICIA' " +
//			    "  AND app.c_aplicacion NOT LIKE 'PF_DGA' " +
//			    "  ) q3 " + 
//			    " ) q2 " +
//			    ") q1 " +
//			    "WHERE EXTRACT(YEAR FROM q1.fecha) = " + year + " " +
//			    "GROUP BY q1.provincia, q1.aplicacion," +
//			    "		  EXTRACT(MONTH FROM q1.fecha)," +
//			    "		  EXTRACT(YEAR FROM q1.fecha)) " +
//			    "ORDER BY PROVINCIA";
//
//		return query;
//	}

	/**
	 * Método que devuelve la query para obtener las peticiones por aplicación, y sede
	 * @param seats Listado de sedes
	 * @return Listado de peticiones
	 */
//	private String obtenerQueryPxASF(List<AbstractBaseDTO> seats) {
//		String query = "";
//
//		String sedes = querySeats(seats);
//
//		query = "(SELECT q1.provincia AS PROVINCIA," +
//			    "  q1.aplicacion AS APLICACION," +
//			    "  EXTRACT(MONTH FROM q1.fecha)," +
//	        	"  EXTRACT(YEAR FROM q1.fecha)," +
//			    "  COUNT(*) AS N_PETICIONES " +
//			    "FROM " + 
//			    "(SELECT " +
//			    "(SELECT prov.c_nombre as nombre " +
//			    "FROM pf_firmantes fir," +
//			    "	  pf_usuarios u," +
//			    "     pf_provincia prov " +
//			    "WHERE fir.lfir_x_linea_firma = q2.linea_firma " +
//			    "AND u.x_usuario              = fir.usu_x_usuario " +
//			    "AND prov.x_provincia         = u.prov_x_provincia " +
//			    "AND prov.x_provincia in " + sedes + " " +
//			    "AND ROWNUM                   = 1 " +
//			    ")             AS provincia," +
//			    "q2.aplicacion AS aplicacion," +
//			    "q2.fecha      AS fecha " +
//			    "FROM " +
//			    "(SELECT q3.aplicacion AS aplicacion," +
//			    "        q3.fecha            AS fecha," +
//			    "(SELECT x_linea_firma " +
//			     "FROM pf_lineas_firma " +
//			     "WHERE q3.peticion = pet_x_peticion " +
//			     "AND ROWNUM        = 1 " +
//			     ") AS linea_firma " +
//			    "FROM " +
//			     "(SELECT pet.x_peticion AS peticion," +
//			     "        app.c_aplicacion     AS aplicacion," +
//			     "        pet.f_creado         AS fecha " +
//			     " FROM pf_peticiones pet," +
//			     "      pf_aplicaciones app " +
//			     " WHERE app.x_aplicacion = pet.apl_x_aplicacion " +
//			     " AND app.c_aplicacion NOT LIKE 'PFIRMA%' " +
//			     " AND app.c_aplicacion NOT LIKE 'APLIFICTICIA' " +
//			     " AND app.c_aplicacion NOT LIKE 'PF_DGA' " +
//			     " ) q3 " +
//			     ") q2 " +
//			    ") q1 " +
//			    "GROUP BY q1.provincia, q1.aplicacion," +
//			    "		  EXTRACT(MONTH FROM q1.fecha)," +
//			    "		  EXTRACT(YEAR FROM q1.fecha)) " +
//			    "UNION " +
//			    "(SELECT q1.provincia AS PROVINCIA," +
//			    "		 q1.aplicacion AS APLICACION," +
//			    "		 EXTRACT(MONTH FROM q1.fecha)," +
//	        	"  		 EXTRACT(YEAR FROM q1.fecha)," +
//			    "   	 COUNT(*) AS N_PETICIONES " +
//			    "FROM " + 
//			    "(SELECT " +
//			    "(SELECT prov.c_nombre as nombre " +
//			    " FROM pf_firmantes fir, " +
//			    " pf_usuarios u," +
//			    " pf_provincia prov " +
//			    " WHERE fir.lfir_x_linea_firma = q2.linea_firma " +
//			    " AND u.x_usuario              = fir.usu_x_usuario " +
//			    " AND prov.x_provincia         = u.prov_x_provincia " +
//			    " AND prov.x_provincia in " + sedes + " " +
//			    " AND ROWNUM                   = 1 " +
//			    ")             AS provincia, " +
//			    "q2.aplicacion AS aplicacion," +
//			    "q2.fecha      AS fecha " +
//			    "FROM " +
//			    "(SELECT q3.aplicacion AS aplicacion, " +
//			    "        q3.fecha            AS fecha, " +
//			    "(SELECT x_linea_firma " +
//			    " FROM pf_lineas_firma " +
//			    " WHERE q3.peticion = pet_x_peticion " +
//			    " AND ROWNUM        = 1 " +
//			    " ) AS linea_firma " +
//			    " FROM " +
//			    " (SELECT pet.x_peticion AS peticion, " +
//			    "         'PORTAFIRMAS'  AS aplicacion, " +
//			    "         pet.f_creado   AS fecha " +
//			    "  FROM pf_peticiones pet, " +
//			    "	    pf_aplicaciones app " +
//			    "  WHERE app.x_aplicacion = pet.apl_x_aplicacion " +
//			    "  AND app.c_aplicacion LIKE 'PFIRMA%' " +
//			    "  AND app.c_aplicacion NOT LIKE 'APLIFICTICIA' " +
//			    "  AND app.c_aplicacion NOT LIKE 'PF_DGA' " +
//			    "  ) q3 " + 
//			    " ) q2 " +
//			    ") q1 " +
//			    "GROUP BY q1.provincia, q1.aplicacion," +
//			    "		  EXTRACT(MONTH FROM q1.fecha)," +
//			    "		  EXTRACT(YEAR FROM q1.fecha)) " +
//			    "ORDER BY PROVINCIA";
//
//		return query;
//	}

	/**
	 * Método que devuelve la query para obtener las peticiones por aplicación, fecha y sede
	 * @param seats Listado de sedes
	 * @param year Año
	 * @return Listado de peticiones
	 */
//	private String obtenerQueryPxASY(List<AbstractBaseDTO> seats, String year) {
//		String query = "";
//
//		String sedes = querySeats(seats);
//
//		query = "(SELECT q1.provincia AS PROVINCIA," +
//			    "  q1.aplicacion AS APLICACION," +
//			    "  COUNT(*) AS N_PETICIONES " +
//			    "FROM " + 
//			    "(SELECT " +
//			    "(SELECT prov.c_nombre as nombre " +
//			    "FROM pf_firmantes fir," +
//			    "	  pf_usuarios u," +
//			    "     pf_provincia prov " +
//			    "WHERE fir.lfir_x_linea_firma = q2.linea_firma " +
//			    "AND u.x_usuario              = fir.usu_x_usuario " +
//			    "AND prov.x_provincia         = u.prov_x_provincia " +
//			    "AND prov.x_provincia in " + sedes + " " +
//			    "AND ROWNUM                   = 1 " +
//			    ")             AS provincia," +
//			    "q2.aplicacion AS aplicacion," +
//			    "q2.fecha      AS fecha " +
//			    "FROM " +
//			    "(SELECT q3.aplicacion AS aplicacion," +
//			    "        q3.fecha            AS fecha," +
//			    "(SELECT x_linea_firma " +
//			     "FROM pf_lineas_firma " +
//			     "WHERE q3.peticion = pet_x_peticion " +
//			     "AND ROWNUM        = 1 " +
//			     ") AS linea_firma " +
//			    "FROM " +
//			     "(SELECT pet.x_peticion AS peticion," +
//			     "        app.c_aplicacion     AS aplicacion," +
//			     "        pet.f_creado         AS fecha " +
//			     " FROM pf_peticiones pet," +
//			     "      pf_aplicaciones app " +
//			     " WHERE app.x_aplicacion = pet.apl_x_aplicacion " +
//			     " AND app.c_aplicacion NOT LIKE 'PFIRMA%' " +
//			     " AND app.c_aplicacion NOT LIKE 'APLIFICTICIA' " +
//			     " AND app.c_aplicacion NOT LIKE 'PF_DGA' " +
//			     " ) q3 " +
//			     ") q2 " +
//			    ") q1 " +
//			    "WHERE EXTRACT(YEAR FROM q1.fecha) = " + year + " " +
//			    "GROUP BY q1.provincia, q1.aplicacion) " +
//			    "UNION " +
//			    "(SELECT q1.provincia AS PROVINCIA," +
//			    "		 q1.aplicacion AS APLICACION," +
//			    "   	 COUNT(*) AS N_PETICIONES " +
//			    "FROM " + 
//			    "(SELECT " +
//			    "(SELECT prov.c_nombre as nombre " +
//			    " FROM pf_firmantes fir, " +
//			    " pf_usuarios u," +
//			    " pf_provincia prov " +
//			    " WHERE fir.lfir_x_linea_firma = q2.linea_firma " +
//			    " AND u.x_usuario              = fir.usu_x_usuario " +
//			    " AND prov.x_provincia         = u.prov_x_provincia " +
//			    " AND prov.x_provincia in " + sedes + " " +
//			    " AND ROWNUM                   = 1 " +
//			    ")             AS provincia, " +
//			    "q2.aplicacion AS aplicacion," +
//			    "q2.fecha      AS fecha " +
//			    "FROM " +
//			    "(SELECT q3.aplicacion AS aplicacion, " +
//			    "        q3.fecha            AS fecha, " +
//			    "(SELECT x_linea_firma " +
//			    " FROM pf_lineas_firma " +
//			    " WHERE q3.peticion = pet_x_peticion " +
//			    " AND ROWNUM        = 1 " +
//			    " ) AS linea_firma " +
//			    " FROM " +
//			    " (SELECT pet.x_peticion AS peticion, " +
//			    "         'PORTAFIRMAS'  AS aplicacion, " +
//			    "         pet.f_creado   AS fecha " +
//			    "  FROM pf_peticiones pet, " +
//			    "	    pf_aplicaciones app " +
//			    "  WHERE app.x_aplicacion = pet.apl_x_aplicacion " +
//			    "  AND app.c_aplicacion LIKE 'PFIRMA%' " +
//			    "  AND app.c_aplicacion NOT LIKE 'APLIFICTICIA' " +
//			    "  AND app.c_aplicacion NOT LIKE 'PF_DGA' " +
//			    "  ) q3 " + 
//			    " ) q2 " +
//			    ") q1 " +
//			    "WHERE EXTRACT(YEAR FROM q1.fecha) = " + year + " " +
//			    "GROUP BY q1.provincia, q1.aplicacion) " +
//			    "ORDER BY PROVINCIA";
//
//		return query;
//	}

	/**
	 * Método que devuelve la query para obtener las peticiones por aplicación, fecha y sede
	 * @param seats Listado de sedes
	 * @param year Año
	 * @return Listado de peticiones
	 */
//	private String obtenerQueryPxAS(List<AbstractBaseDTO> seats) {
//		String query = "";
//
//		String sedes = querySeats(seats);
//
//		query = "(SELECT q1.provincia AS PROVINCIA," +
//			    "  q1.aplicacion AS APLICACION," +
//			    "  COUNT(*) AS N_PETICIONES " +
//			    "FROM " + 
//			    "(SELECT " +
//			    "(SELECT prov.c_nombre as nombre " +
//			    "FROM pf_firmantes fir," +
//			    "	  pf_usuarios u," +
//			    "     pf_provincia prov " +
//			    "WHERE fir.lfir_x_linea_firma = q2.linea_firma " +
//			    "AND u.x_usuario              = fir.usu_x_usuario " +
//			    "AND prov.x_provincia         = u.prov_x_provincia " +
//			    "AND prov.x_provincia in " + sedes + " " +
//			    "AND ROWNUM                   = 1 " +
//			    ")             AS provincia," +
//			    "q2.aplicacion AS aplicacion," +
//			    "q2.fecha      AS fecha " +
//			    "FROM " +
//			    "(SELECT q3.aplicacion AS aplicacion," +
//			    "        q3.fecha            AS fecha," +
//			    "(SELECT x_linea_firma " +
//			     "FROM pf_lineas_firma " +
//			     "WHERE q3.peticion = pet_x_peticion " +
//			     "AND ROWNUM        = 1 " +
//			     ") AS linea_firma " +
//			    "FROM " +
//			     "(SELECT pet.x_peticion AS peticion," +
//			     "        app.c_aplicacion     AS aplicacion," +
//			     "        pet.f_creado         AS fecha " +
//			     " FROM pf_peticiones pet," +
//			     "      pf_aplicaciones app " +
//			     " WHERE app.x_aplicacion = pet.apl_x_aplicacion " +
//			     " AND app.c_aplicacion NOT LIKE 'PFIRMA%' " +
//			     " AND app.c_aplicacion NOT LIKE 'APLIFICTICIA' " +
//			     " AND app.c_aplicacion NOT LIKE 'PF_DGA' " +
//			     " ) q3 " +
//			     ") q2 " +
//			    ") q1 " +
//			    "GROUP BY q1.provincia, q1.aplicacion) " +
//			    "UNION " +
//			    "(SELECT q1.provincia AS PROVINCIA," +
//			    "		 q1.aplicacion AS APLICACION," +
//			    "   	 COUNT(*) AS N_PETICIONES " +
//			    "FROM " + 
//			    "(SELECT " +
//			    "(SELECT prov.c_nombre as nombre " +
//			    " FROM pf_firmantes fir, " +
//			    " pf_usuarios u," +
//			    " pf_provincia prov " +
//			    " WHERE fir.lfir_x_linea_firma = q2.linea_firma " +
//			    " AND u.x_usuario              = fir.usu_x_usuario " +
//			    " AND prov.x_provincia         = u.prov_x_provincia " +
//			    " AND prov.x_provincia in " + sedes + " " +
//			    " AND ROWNUM                   = 1 " +
//			    ")             AS provincia, " +
//			    "q2.aplicacion AS aplicacion," +
//			    "q2.fecha      AS fecha " +
//			    "FROM " +
//			    "(SELECT q3.aplicacion AS aplicacion, " +
//			    "        q3.fecha            AS fecha, " +
//			    "(SELECT x_linea_firma " +
//			    " FROM pf_lineas_firma " +
//			    " WHERE q3.peticion = pet_x_peticion " +
//			    " AND ROWNUM        = 1 " +
//			    " ) AS linea_firma " +
//			    " FROM " +
//			    " (SELECT pet.x_peticion AS peticion, " +
//			    "         'PORTAFIRMAS'  AS aplicacion, " +
//			    "         pet.f_creado   AS fecha " +
//			    "  FROM pf_peticiones pet, " +
//			    "	    pf_aplicaciones app " +
//			    "  WHERE app.x_aplicacion = pet.apl_x_aplicacion " +
//			    "  AND app.c_aplicacion LIKE 'PFIRMA%' " +
//			    "  AND app.c_aplicacion NOT LIKE 'APLIFICTICIA' " +
//			    "  AND app.c_aplicacion NOT LIKE 'PF_DGA' " +
//			    "  ) q3 " + 
//			    " ) q2 " +
//			    ") q1 " +
//			    "GROUP BY q1.provincia, q1.aplicacion) " +
//			    "ORDER BY PROVINCIA";
//
//		return query;
//	}

	/**
	 * Método que genera la cadena con los identificadores de las sedes para incluir en una consulta
	 * @param seats Listado de sedes
	 * @return Query
	 */
//	private String querySeats(List<AbstractBaseDTO> seats) {
//
//		boolean primero = true;
//		String sedes = "(";
//		for (AbstractBaseDTO dto : seats) {
//			PfProvinceDTO seat = (PfProvinceDTO) dto;
//
//			if (primero) {
//				sedes = sedes + seat.getPrimaryKeyString();
//				primero = false;
//			} else {
//				sedes = sedes + "," + seat.getPrimaryKeyString();
//			}
//		}
//
//		return sedes + ")";
//	}
}
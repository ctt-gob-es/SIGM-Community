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

package es.seap.minhap.portafirmas.business.administration;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.List;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.displaytag.model.TableModel;
import org.displaytag.properties.TableProperties;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.stereotype.Service;

import es.seap.minhap.portafirmas.business.ProvinceBO;
import es.seap.minhap.portafirmas.dao.BaseDAO;
import es.seap.minhap.portafirmas.domain.AbstractBaseDTO;
import es.seap.minhap.portafirmas.domain.PfApplicationsDTO;
import es.seap.minhap.portafirmas.domain.PfProvinceDTO;
import es.seap.minhap.portafirmas.domain.PfUsersDTO;
import es.seap.minhap.portafirmas.utils.Constants;
import es.seap.minhap.portafirmas.utils.Util;

import es.seap.minhap.portafirmas.utils.export.DisplayTagExportServiceImpl;
import es.seap.minhap.portafirmas.utils.stats.StatsQueryFilter;
import es.seap.minhap.portafirmas.utils.stats.StatsQueryUtil;
import es.seap.minhap.portafirmas.web.beans.ParameterStat;

@Service
@Scope(proxyMode=ScopedProxyMode.TARGET_CLASS)
public class StatsBO {
	
	protected final Log log = LogFactory.getLog(getClass());

	@Autowired
	BaseDAO baseDAO;
	
	@Autowired
	UserAdmBO userAdmBO;
	
	@Autowired
	ProvinceBO provinceBO;
	
	@Autowired
	DisplayTagExportServiceImpl exportService;
	
	private static String DESGLOSE = "desglose";
	
	private String HEADER_USER = "USUARIO";
	private String HEADER_N_REQUESTS ="N_PETICIONES";
	private String HEADER_N_SIGNATURES ="N_FIRMAS";
	private String HEADER_SUBJECT ="ASUNTO_PETICION";
	private String HEADER_REFERENCE= "REFERENCIA";
	private String HEADER_DOCUMENT= "DOCUMENTO";
	private String HEADER_SEAT= "SEDE";	
	private String HEADER_MONTH = "MES";
	private String HEADER_YEAR = "AÑO";
	private String HEADER_APPLICATION = "APLICACIÓN";
	
	/**
	 * Número de usuarios para las que un usuario tiene visibilidad 
	 * (todos, si es administrador, o los usuarios de las sedes que administra, si es administrador de sede).
	 * @param user Usuario del que se quiere saber el número de usuarios visibles.
	 * @return
	 */
	public Long countUsers (List<AbstractBaseDTO>seatList) {		
		/*List<AbstractBaseDTO> seatList = getSeatsAffected (user);*/		
		return userAdmBO.countListByProvinceList(seatList);
	}
	
	/**
	 * Número de peticiones para las que un usuario tiene visibilidad, que son aquellas enviadas por un usuario de
	 * las sedes para las que el usuario tiene visibilidad, o bien aquellas en que el destinatario pertenece a alguna
	 * de las sedes para las que el usuario tiene visibilidad.
	 * @param user Usuario del que se quiere saber el número de peticiones visibles.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Long countRequests (List<AbstractBaseDTO>seatList) {
		/*List<AbstractBaseDTO> seatList = getSeatsAffected (user);*/	
		List<Object[]> results = baseDAO.querySQLStandardResult(StatsQueryUtil.obtenerQueryRequests(seatList));
		return Long.valueOf(results.size());		
	}
	
	/**
	 * Número de firmas para las que un usuario tiene visibilidad, que son aquellas en las que la petición fue enviada por un usuario de
	 * las sedes para las que el usuario tiene visibilidad, o bien aquellas firmas realizadas por usuarios de las sedes para las que el usuario
	 * tiene visilibilidad.
	 * @param user Usuario del que se quiere saber el número de firmas visibles.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public Long countSignatures (List<AbstractBaseDTO>seatList) {
		/*List<AbstractBaseDTO> seatList = getSeatsAffected (user);*/	
		List<Object[]> results = baseDAO.querySQLStandardResult(StatsQueryUtil.obtenerQuerySignatures(seatList));
		return new Long(results.size());
	}
	
	/**
	 * Número de años desde que la aplicación está en uso.
	 */
	public Long countYears () {
		return new Long (getYearList().size());		
	}
	
	/**
	 * Número de aplicaciones
	 * 
	 */
	@SuppressWarnings("unchecked")
	public Long countApplications () {
		List<Object[]> results = baseDAO.querySQLStandardResult(StatsQueryUtil.obtenerQueryApplications());
		return new Long(results.size());
	}
	
	public List<AbstractBaseDTO> searchUsers (List<AbstractBaseDTO> provinceList, String find) {
		return userAdmBO.queryUsersByNameByProvince(provinceList, find);
	}
	
	/**
	 * Calcula las estadísticas
	 */
	@SuppressWarnings("unchecked")
	public void stats (List<AbstractBaseDTO> seatlist, ParameterStat pStat, List<String> headers, List<Object[]> rows) {
		boolean desglose = pStat.getTotDesg().contentEquals(DESGLOSE);
		String fInicio = Util.esVacioONulo(pStat.getfStart()) ? null : pStat.getfStart() + " 00:00:00";
		String fFin = Util.esVacioONulo(pStat.getfEnd()) ? null : pStat.getfEnd() + " 23:59:59";
		String pkApplication = Util.esVacioONulo(pStat.getApplication()) ? null : pStat.getApplication();
		String pkUser = Util.esVacioONulo(pStat.getUser()) ? null : pStat.getUser();
		List<AbstractBaseDTO> seats =Util.esVacioONulo(pStat.getSeat()) ? seatlist : createSeatListOneElement(pStat.getSeat());

		StatsQueryFilter filter = new StatsQueryFilter (fInicio, fFin, pkApplication, pkUser, seats);
		
		if (pStat.getReqSig().contentEquals(Constants.REQUESTS)) {
				// Peticiones por usuario //
			if (pStat.getType().contentEquals(Constants.XU)) {
				rows.addAll(baseDAO.querySQLStandardResult(StatsQueryUtil.obtenerQueryPxU (desglose, filter)));
				// Peticiones por SEDE //
			} else if (pStat.getType().contentEquals(Constants.XS)) {
				rows.addAll(baseDAO.querySQLStandardResult(StatsQueryUtil.obtenerQueryPxS (desglose, filter)));
				// Peticiones por sede y usuario
			} else if (pStat.getType().contentEquals(Constants.XSU)) {
				rows.addAll(baseDAO.querySQLStandardResult(StatsQueryUtil.obtenerQueryPxSU (desglose, filter, false)));
				// Peticiones por sede, usuario y fecha (mes).
			} else if (pStat.getType().contentEquals(Constants.XSUF)) {
				rows.addAll(baseDAO.querySQLStandardResult(StatsQueryUtil.obtenerQueryPxSU (desglose, filter, true)));
				// Peticiones por sede y aplicación
			} else if (pStat.getType().contentEquals(Constants.XSA)) {
				rows.addAll(baseDAO.querySQLStandardResult(StatsQueryUtil.obtenerQueryPxSA (desglose, filter, false)));
				// Peticiones por sede, aplicación y fecha
			} else if (pStat.getType().contentEquals(Constants.XSAF)) {
				rows.addAll(baseDAO.querySQLStandardResult(StatsQueryUtil.obtenerQueryPxSA (desglose, filter, true)));
			}
			
		} else if (pStat.getReqSig().contentEquals(Constants.SIGNATURES)) {
			// Firmas por usuario
			if (pStat.getType().contentEquals(Constants.XU)) {
				rows.addAll(baseDAO.querySQLStandardResult(StatsQueryUtil.obtenerQueryFxU (desglose, filter)));
			// Firmas por sede
			} else if (pStat.getType().contentEquals(Constants.XS)) {
				rows.addAll(baseDAO.querySQLStandardResult(StatsQueryUtil.obtenerQueryFxS (desglose, filter)));
			// Firmas por sede y usuario
			} else if (pStat.getType().contentEquals(Constants.XSU)) {
				rows.addAll(baseDAO.querySQLStandardResult(StatsQueryUtil.obtenerQueryFxSU (desglose, filter, false)));
			// Firmas por sede, usuario y fecha (mes)
			} else if (pStat.getType().contentEquals(Constants.XSUF)) {
				rows.addAll(baseDAO.querySQLStandardResult(StatsQueryUtil.obtenerQueryFxSU (desglose, filter, true)));
			// Firmas por sede y aplicación.
			} else if (pStat.getType().contentEquals (Constants.XSA)) {				
				rows.addAll(baseDAO.querySQLStandardResult(StatsQueryUtil.obtenerQueryFxSA (desglose, filter, false)));
			// Firmas por sede, aplicación y fecha.
			} else if (pStat.getType().contentEquals (Constants.XSAF)) {
				rows.addAll(baseDAO.querySQLStandardResult(StatsQueryUtil.obtenerQueryFxSA (desglose, filter, true)));
			}

		}
		
		fillHeaders (pStat.getReqSig(), pStat.getType(), desglose, headers);
					
	}
	
	/**
	 * Lista con una sede.
	 * @param pkSeat
	 * @return
	 */
	private List<AbstractBaseDTO> createSeatListOneElement (String pkSeat) {
		PfProvinceDTO seatDTO = provinceBO.querySeatByPk (Long.parseLong(pkSeat));
		List<AbstractBaseDTO> seatListOneElement = new ArrayList<AbstractBaseDTO> ();
		seatListOneElement.add(seatDTO);
		return seatListOneElement;
	}
	
	private void fillHeaders (final String reqSig,
							  final String type, 
							  final boolean desglose, 
							  List<String> headers) {
		
		if (type.contentEquals(Constants.XU)) {
			headers.add(HEADER_USER);
		} else {
			headers.add(HEADER_SEAT);
		}
		
		if (type.contentEquals(Constants.XSU) || type.contentEquals(Constants.XSUF)) {
			headers.add(HEADER_USER);
		} else if (type.contentEquals(Constants.XSA) || type.contentEquals(Constants.XSAF)) {
			headers.add(HEADER_APPLICATION);
		}
		
		if (type.contentEquals(Constants.XSUF) || type.contentEquals(Constants.XSAF)) {
			headers.add(HEADER_YEAR);
			headers.add(HEADER_MONTH);
		}
		
		if (desglose) {
			if (reqSig.contentEquals(Constants.REQUESTS)) {
				headers.add(HEADER_SUBJECT);
				headers.add(HEADER_REFERENCE);
			} else if(reqSig.contentEquals(Constants.SIGNATURES)) {
				headers.add(HEADER_SUBJECT);
				headers.add(HEADER_DOCUMENT);				
			}
		} else {
			if (reqSig.contentEquals(Constants.REQUESTS)) {
				headers.add(this.HEADER_N_REQUESTS);
			} else if(reqSig.contentEquals(Constants.SIGNATURES)) {
				headers.add(this.HEADER_N_SIGNATURES);
			}
		}
	
	}
	
	
	public List<AbstractBaseDTO> getSeatsAffected (PfUsersDTO user) {
		return provinceBO.getSeatList(user);
	}
	
	public List<String> getYearList () {
		List<String> yearList = new ArrayList<String> ();
		
		int firstYear = 2010;
		
		GregorianCalendar date = new GregorianCalendar();
		int finalYear = date.get(Calendar.YEAR);

		int current = firstYear;
		while (current <= finalYear) {
			yearList.add(current + "");
			current ++;
		}
		
		return yearList;
	}
	
	public List<PfApplicationsDTO> getApplicationList () {
		return baseDAO.queryListOneParameter("administration.applicationList", null, null);
	}
	
	public void export (HttpServletRequest request,
				   	    HttpServletResponse response,
				   	    final ParameterStat parameterStat,
				   	    final List<String> headers, 
				   	    final List<Object[]> rows) {		
		
		TableProperties properties = TableProperties.getInstance(request);		
		TableModel table = exportService.createTableModel(properties, request.getCharacterEncoding());
		exportService.loadHeaders(table, headers.toArray(new String[]{""}));
		exportService.loadRows(table, rows);
		exportService.export(response, table, parameterStat.getFormat());
		
	}
}
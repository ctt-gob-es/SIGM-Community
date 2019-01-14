/*
 * Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
 * Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto sean aprobadas por laComisión Europea- versiones posteriores de la EUPL (la «Licencia»); 
 * Solo podrá usarse esta obra si se respeta la Licencia. 
 * Puede obtenerse una copia de la Licencia en: 
 * http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
 * Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
 * Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
 */

package es.msssi.sgm.registropresencial.daos;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.faces.context.FacesContext;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.log4j.Logger;

import com.ibatis.sqlmap.engine.execution.SqlExecutor;
import com.ieci.tecdoc.common.invesdoc.Iuserdepthdr;
import com.ieci.tecdoc.common.invesicres.ScrCa;
import com.ieci.tecdoc.common.invesicres.ScrOfic;
import com.ieci.tecdoc.common.invesicres.ScrOrg;
import com.ieci.tecdoc.common.invesicres.ScrTt;
import com.ieci.tecdoc.common.keys.ConfigurationKeys;
import com.ieci.tecdoc.common.utils.Configurator;
import com.ieci.tecdoc.common.utils.ISicresQueries;
import com.ieci.tecdoc.utils.HibernateUtil;

import es.msssi.sgm.registropresencial.actions.ReportsInputRegisterAction;
import es.msssi.sgm.registropresencial.actions.ValidationListAction;
import es.msssi.sgm.registropresencial.beans.ibatis.DirOrgs;
import es.msssi.sgm.registropresencial.beans.ibatis.InputRegisterReportsCert;
import es.msssi.sgm.registropresencial.beans.ibatis.OutputRegisterReportsCert;
import es.msssi.sgm.registropresencial.dao.SqlMapClientBaseDao;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.utils.Utils;

/**
 * Clase que realiza las consultas de informes a base de datos.
 * 
 * @author jortizs
 */
public class ReportDAO extends SqlMapClientBaseDao {
	
	private static final Logger LOG = Logger.getLogger(ReportsInputRegisterAction.class.getName());
	
	/** Constante de propiedades que indica el máximo de filas a mostrar. */
	private static Integer MAX_REPORT_REGISTER = new Integer(Configurator.getInstance().getProperty(ConfigurationKeys.KEY_DESKTOP_MAX_REPORT_REGISTERS));

	/**
	 * Obtiene la lista de certificados de informes de registros de entrada.
	 * 
	 * @param fdridList
	 *            Lista de Ids de registros a obtener.
	 * @param bookId
	 *            Id del libro.
	 * @param entidad
	 *            Entidad.
	 * @param isInputRegisterSearch
	 *            si es llamado desde la búsqueda
	 * @return listReports Lista de datos de certificados obtenidos, un informe
	 *         por registro de entrada.
	 */
	@SuppressWarnings("unchecked")
	public List<LinkedHashMap<String, Object>> getRegisterCertReports(List<Integer> fdridList, Integer bookId, boolean isInputRegisterSearch) {
		
		LOG.trace("Entrando en ReportDAO.getRegisterCertReports()");
		
		List<LinkedHashMap<String, Object>> reportsList = new ArrayList<LinkedHashMap<String, Object>>();
		List<InputRegisterReportsCert> iRegReportsCertResultsList = new ArrayList<InputRegisterReportsCert>();
		List<OutputRegisterReportsCert> oRegReportsCertResultsList = new ArrayList<OutputRegisterReportsCert>();
		
		try {
			HashMap<String, Object> reportsRegisterCertParams = new HashMap<String, Object>();
			reportsRegisterCertParams.put("idBook", bookId);
			// reportsRegisterCertParams.put("maxReportRegisters",
			// MAX_REPORT_REGISTER);
			reportsRegisterCertParams.put("fdridList", fdridList);
		
			if (isInputRegisterSearch) {
				iRegReportsCertResultsList = (List<InputRegisterReportsCert>) getSqlMapClientTemplate().queryForList("InputRegisterReportsCert.selectIRegReportsCert", reportsRegisterCertParams, SqlExecutor.NO_SKIPPED_RESULTS, MAX_REPORT_REGISTER);
				if (iRegReportsCertResultsList != null && iRegReportsCertResultsList.size() > 0) {
					for (InputRegisterReportsCert iRegReportsCertResults : iRegReportsCertResultsList) {
						reportsList.add(fillInputRegisterReportsList(iRegReportsCertResults));
					}
				}
			} else {
				oRegReportsCertResultsList = (List<OutputRegisterReportsCert>) getSqlMapClientTemplate().queryForList("OutputRegisterReportsCert.selectORegReportsCert", reportsRegisterCertParams);
				if (oRegReportsCertResultsList != null && oRegReportsCertResultsList.size() > 0) {
					for (OutputRegisterReportsCert oRegReportsCertResults : oRegReportsCertResultsList) {
						reportsList.add(fillOutputRegisterReportsList(oRegReportsCertResults));
					}
				}
			}
			
		} catch (SQLException sqlException) {
			LOG.error(ErrorConstants.REPORT_DATA_ERROR, sqlException);
			Utils.redirectToErrorPage(null, null, sqlException);
		}
		return reportsList;
	}

	/**
	 * Mapea los valores de registros de entrada devueltos por la consulta para
	 * generar los informes de certificados.
	 * 
	 * @param inputRegisterReportsCert
	 *            resultado de base de datos.
	 * @return inputRegisterHashMap datos mapeados.
	 * @throws SQLException
	 *             Si se produce un error al obtener los datos.
	 */
	private LinkedHashMap<String, Object> fillInputRegisterReportsList( InputRegisterReportsCert inputRegisterReportsCert) throws SQLException {
		
		LOG.trace("Entrando en ReportDAO.fillInputRegisterReportsList()");
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		LinkedHashMap<String, Object> inputRegisterHashMap = new LinkedHashMap<String, Object>();
		
		// CORRESPONDENCIA DE VALOR DE VISTA EN BBDD CON NOMBRE DE CAMPO
		// -------------------------------------------------------------
		// 1 IDBOOK NUMBER - Id. de libro.
		inputRegisterHashMap.put("IDBOOK", inputRegisterReportsCert.getIdBook());
		// 2 FDRID NUMBER - Id.
		inputRegisterHashMap.put("FDRID", inputRegisterReportsCert.getFdrid());
		// 3 TIMESTAMP DATE - Timestamp.
		inputRegisterHashMap.put("TIMESTAMP", inputRegisterReportsCert.getTimestamp());
		// 4 FLD1 VARCHAR2 - Número de registro.
		inputRegisterHashMap.put("FLD1", inputRegisterReportsCert.getFld1());
		// 5 FLD2 DATE - Fecha de registro.
		inputRegisterHashMap.put("FLD2", formatter.format(inputRegisterReportsCert.getFld2()));
		// 6 FLD3 VARCHAR2 - Usuario.
		inputRegisterHashMap.put("FLD3", inputRegisterReportsCert.getFld3());
		// 7 FLD4 DATE - Fecha de trabajo.
		inputRegisterHashMap.put("FLD4", inputRegisterReportsCert.getFld4());
		// 8 FLD5 NUMBER - Oficina de Registro.
		ScrOfic scrRegOfic = new ScrOfic();
		scrRegOfic.setId(inputRegisterReportsCert.getFld5());
		inputRegisterHashMap.put("FLD5", scrRegOfic);
		// 9 FLD5_TEXT VARCHAR2 - Nombre oficina de Registro.
		inputRegisterHashMap.put("FLD5_TEXT", inputRegisterReportsCert.getFld5_text());
		// 10 FLD5_ADDRESS VARCHAR2 - Dirección oficina.
		// 11 FLD5_CITY VARCHAR2 - Ciudad oficina.
		// 12 FLD5_ZIP VARCHAR2 - Cód. postal oficina.
		// 13 FLD5_COUNTRY VARCHAR2 - Provincia oficina.
		// 14 FLD5_TELEPHONE VARCHAR2 - Teléfono oficina.
		// 15 FLD5_FAX VARCHAR2 - Fax oficina.
		// 16 FLD5_EMAIL VARCHAR2 - Email oficina.
		inputRegisterHashMap.put( "FLD5_ADDRESS", formatAddressOfic(inputRegisterReportsCert.getFld5_address(),
						inputRegisterReportsCert.getFld5_city(),
						inputRegisterReportsCert.getFld5_zip(),
						inputRegisterReportsCert.getFld5_country(),
						inputRegisterReportsCert.getFld5_telephone(),
						inputRegisterReportsCert.getFld5_fax(),
						inputRegisterReportsCert.getFld5_email())
				);
		
		// 17 FLD6 NUMBER - Estado.
		inputRegisterHashMap.put("FLD6", inputRegisterReportsCert.getFld6());
		// 18 FLD7 NUMBER - Origen.
		ScrOrg scrOrig = new ScrOrg();
		scrOrig.setId(inputRegisterReportsCert.getFld7());
		inputRegisterHashMap.put("FLD7", scrOrig);
		// 19 FLD7_TEXT VARCHAR2 - Nombre organismo origen.
		inputRegisterHashMap.put("FLD7_TEXT", inputRegisterReportsCert.getFld7_text());
		// 20 FLD8 NUMBER - Destino.
		ScrOrg scrDest = new ScrOrg();
		scrDest.setId(inputRegisterReportsCert.getFld8());
		inputRegisterHashMap.put("FLD8", scrDest);
		// 21 FLD8_TEXT VARCHAR2 - Nombre organismo destino.
		inputRegisterHashMap.put("FLD8_TEXT", inputRegisterReportsCert.getFld8_text());
		// 22 FLD9 VARCHAR2 - Remitentes.
		inputRegisterHashMap.put("FLD9", inputRegisterReportsCert.getFld9());
		// 23 FLD10 VARCHAR2 - Número de Registro original.
		inputRegisterHashMap.put("FLD10", inputRegisterReportsCert.getFld10());
		// 24 FLD11 NUMBER - Tipo de Registro original.
		inputRegisterHashMap.put("FLD11", inputRegisterReportsCert.getFld11());
		// 25 FLD12 DATE - Fecha de Registro original.
		inputRegisterHashMap.put( "FLD12", inputRegisterReportsCert.getFld12() != null ? formatter.format(inputRegisterReportsCert.getFld12()) : null);
		// 26 FLD13 NUMBER - Registro original.
		ScrOrg scrOriginalReg = new ScrOrg();
		scrOriginalReg.setId(inputRegisterReportsCert.getFld13());
		// 27 FLD13_TEXT VARCHAR2 - Nombre Registro original.
		inputRegisterHashMap.put("FLD13_TEXT", inputRegisterReportsCert.getFld13_text());
		inputRegisterHashMap.put("FLD13", scrOriginalReg);
		// 28 FLD14 VARCHAR2 - Tipos de transporte.
		inputRegisterHashMap.put("FLD14", inputRegisterReportsCert.getFld14());
		inputRegisterHashMap.put("FLD14_TEXT", getTransport(inputRegisterReportsCert.getFld14()));
		// 29 FLD15 VARCHAR2 - Número de transporte.
		inputRegisterHashMap.put("FLD15", inputRegisterReportsCert.getFld15());
		// 30 FLD16 NUMBER - Tipo de asunto.
		ScrCa scrCa = new ScrCa();
		scrCa.setId(inputRegisterReportsCert.getFld16());
		inputRegisterHashMap.put("FLD16", scrCa);
		// 31 FLD16_TEXT VARCHAR2 - Nombre de asunto.
		inputRegisterHashMap.put("FLD16_TEXT", inputRegisterReportsCert.getFld16_text());
		// 32 FLD17 VARCHAR2 - Resumen.
		inputRegisterHashMap.put("FLD17", inputRegisterReportsCert.getFld17());
		// 33 FLD19 VARCHAR2 - Referencia al Expediente.
		inputRegisterHashMap.put("FLD19", inputRegisterReportsCert.getFld19());
		// 34 FLD20 DATE - Fecha del documento.
		inputRegisterHashMap.put("FLD20", inputRegisterReportsCert.getFld20());
		// 35 FLD18 VARCHAR2 - observaciones.
		inputRegisterHashMap.put("FLD18", inputRegisterReportsCert.getFld18());
		// 35 FLD501 VARCHAR2 - expone.
		inputRegisterHashMap.put("FLD501", inputRegisterReportsCert.getFld501());
		// 35 FLD502 VARCHAR2 - solicita.
		inputRegisterHashMap.put("FLD502", inputRegisterReportsCert.getFld502());
		// 36 FLD504 VARCHAR2 - rojo.
		inputRegisterHashMap.put("FLD504", inputRegisterReportsCert.getFld504());
		// 37 FLD505 VARCHAR2 - amarillo.
		inputRegisterHashMap.put("FLD505", inputRegisterReportsCert.getFld505());
		// 38 FLD506 VARCHAR2 - verde.
		inputRegisterHashMap.put("FLD506", inputRegisterReportsCert.getFld506());
		// 39 FLD503 VARCHAR2 - Es para intercambio Registral
		inputRegisterHashMap.put("FLD503", inputRegisterReportsCert.getFld503());
		
		return inputRegisterHashMap;
	}

	private String getTransport(String id) {
		String transporte = "";
		if (id != null) {
	
			List<ScrTt> transportes = ((ValidationListAction) FacesContext.getCurrentInstance().getExternalContext().getSessionMap().get("validationListAction")).getListTransportes();
			
			for (ScrTt scrTt : transportes) {
				if (String.valueOf(scrTt.getId()).equals(id) || ("0" + String.valueOf(scrTt.getId())).equals(id)) {
					transporte = scrTt.getTransport();
				}
			}
		}
		
		return transporte;
	}

	/**
	 * Mapea los valores de registros de salida devueltos por la consulta para
	 * generar los informes de certificados.
	 * 
	 * @param outputRegisterReportsCert
	 *            resultado de base de datos.
	 * @return outputRegisterHashMap datos mapeados.
	 * @throws SQLException
	 *             Si se produce un error al obtener los datos.
	 */
	private LinkedHashMap<String, Object> fillOutputRegisterReportsList(OutputRegisterReportsCert outputRegisterReportsCert) throws SQLException {
		
		LOG.trace("Entrando en ReportDAO.fillOutputRegisterReportsList()");
		
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
		LinkedHashMap<String, Object> outputRegisterHashMap = new LinkedHashMap<String, Object>();
		
		// CORRESPONDENCIA DE VALOR DE VISTA EN BBDD CON NOMBRE DE CAMPO
		// -------------------------------------------------------------
		// 1 IDBOOK NUMBER - Id de libro.
		outputRegisterHashMap.put("IDBOOK", outputRegisterReportsCert.getIdBook());
		// 2 FDRID NUMBER - Id.
		outputRegisterHashMap.put("FDRID", outputRegisterReportsCert.getFdrid());
		// 3 TIMESTAMP DATE - Timestamp.
		outputRegisterHashMap.put("TIMESTAMP", outputRegisterReportsCert.getTimestamp());
		// 4 FLD1 VARCHAR2 - Número de registro.
		outputRegisterHashMap.put("FLD1", outputRegisterReportsCert.getFld1());
		// 5 FLD2 DATE - Fecha de registro.
		outputRegisterHashMap.put("FLD2", formatter.format(outputRegisterReportsCert.getFld2()));
		// 6 FLD3 VARCHAR2 - Usuario.
		outputRegisterHashMap.put("FLD3", outputRegisterReportsCert.getFld3());
		// 7 FLD4 DATE - Fecha de trabajo.
		outputRegisterHashMap.put("FLD4", outputRegisterReportsCert.getFld4());
		// 8 FLD5 NUMBER - Oficina de Registro.
		ScrOfic scrRegOfic = new ScrOfic();
		scrRegOfic.setId(outputRegisterReportsCert.getFld5());
		outputRegisterHashMap.put("FLD5", scrRegOfic);
		// 9 FLD5_TEXT VARCHAR2 - Nombre oficina de Registro.
		outputRegisterHashMap.put("FLD5_TEXT", outputRegisterReportsCert.getFld5_text());
		// 10 FLD5_ADDRESS VARCHAR2 - Dirección oficina.
		// 11 FLD5_CITY VARCHAR2 - Ciudad oficina.
		// 12 FLD5_ZIP VARCHAR2 - Cód. postal oficina.
		// 13 FLD5_COUNTRY VARCHAR2 - Provincia oficina.
		// 14 FLD5_TELEPHONE VARCHAR2 - Teléfono oficina.
		// 15 FLD5_FAX VARCHAR2 - Fax oficina.
		// 16 FLD5_EMAIL VARCHAR2 - Email oficina.
		outputRegisterHashMap.put("FLD5_ADDRESS",formatAddressOfic(outputRegisterReportsCert.getFld5_address(),
						outputRegisterReportsCert.getFld5_city(),
						outputRegisterReportsCert.getFld5_zip(),
						outputRegisterReportsCert.getFld5_country(),
						outputRegisterReportsCert.getFld5_telephone(),
						outputRegisterReportsCert.getFld5_fax(),
						outputRegisterReportsCert.getFld5_email())
				);
		// 17 FLD6 NUMBER - Estado.
		outputRegisterHashMap.put("FLD6", outputRegisterReportsCert.getFld6());
		// 18 FLD7 NUMBER - Origen.
		ScrOrg scrOrig = new ScrOrg();
		scrOrig.setId(outputRegisterReportsCert.getFld7());
		outputRegisterHashMap.put("FLD7", scrOrig);
		// 19 FLD7_TEXT VARCHAR2 - Nombre origen.
		outputRegisterHashMap.put("FLD7_TEXT", outputRegisterReportsCert.getFld7_text());
		// 20 FLD8 NUMBER - Destino.
		ScrOrg scrDest = new ScrOrg();
		scrDest.setId(outputRegisterReportsCert.getFld8());
		outputRegisterHashMap.put("FLD8", scrDest);
		// 21 FLD8_TEXT VARCHAR2 - Nombre destino.
		outputRegisterHashMap.put("FLD8_TEXT", outputRegisterReportsCert.getFld8_text());
		// 22 FLD9 VARCHAR2 - Remitentes.
		outputRegisterHashMap.put("FLD9", outputRegisterReportsCert.getFld9());
		// 23 FLD10 VARCHAR2 - Tipos de transporte.
		outputRegisterHashMap.put("FLD10", outputRegisterReportsCert.getFld10());
		outputRegisterHashMap.put("FLD10_TEXT", getTransport(outputRegisterReportsCert.getFld10()));
		// 24 FLD11 VARCHAR2 - Número de transporte.
		outputRegisterHashMap.put("FLD11", outputRegisterReportsCert.getFld11());
		// 25 FLD12 NUMBER - Tipo de asunto.
		ScrCa scrCa = new ScrCa();
		scrCa.setId(outputRegisterReportsCert.getFld12());
		outputRegisterHashMap.put("FLD12", scrCa);
		// 26 FLD12 VARCHAR2 - Asunto.
		outputRegisterHashMap.put("FLD12_TEXT", outputRegisterReportsCert.getFld12_text());		
		// 27 FLD13 VARCHAR2 - Resumen.
		outputRegisterHashMap.put("FLD13", outputRegisterReportsCert.getFld13());
		// 28 FLD15 DATE - Fecha del documento.
		outputRegisterHashMap.put("FLD15", outputRegisterReportsCert.getFld15());
		// 35 FLD18 VARCHAR2 - observaciones.
		outputRegisterHashMap.put("FLD14", outputRegisterReportsCert.getFld14());
		// 36 FLD501 VARCHAR2 - expone.
		outputRegisterHashMap.put("FLD501", outputRegisterReportsCert.getFld501());
		// 36 FLD502 VARCHAR2 - solicita.
		outputRegisterHashMap.put("FLD502", outputRegisterReportsCert.getFld502());
		// 36 FLD504 VARCHAR2 - rojo.
		outputRegisterHashMap.put("FLD504", outputRegisterReportsCert.getFld504());
		// 37 FLD505 VARCHAR2 - amarillo.
		outputRegisterHashMap.put("FLD505", outputRegisterReportsCert.getFld505());
		// 38 FLD506 VARCHAR2 - verde.
		outputRegisterHashMap.put("FLD506", outputRegisterReportsCert.getFld506());
		// 39 FLD503 VARCHAR2 - Es para intercambio Registral
		outputRegisterHashMap.put("FLD503", outputRegisterReportsCert.getFld503());
	
		return outputRegisterHashMap;
	}

	/**
	 * Rellena la dirección completa de una oficina.
	 * 
	 * @param address
	 *            Dirección de la oficina.
	 * @param city
	 *            Ciudad de la oficina.
	 * @param zip
	 *            Cód. postal de la oficina.
	 * @param country
	 *            País de la oficina (realmente se utiliza como provincia).
	 * @param telephone
	 *            Teléfono de la oficina.
	 * @param fax
	 *            Fax de la oficina.
	 * @param mail
	 *            Email de la oficina.
	 * @return la dirección de la oficina.
	 */
	public String formatAddressOfic(String address, String city, String zip, String country, String telephone, String fax, String mail) {
		
		LOG.trace("Entrando en ReportDAO.formatAddressOrg()");
		
		String addressOrg = "";
		if (address != null) {
			addressOrg += address;
		}
		
		if (city != null) {
			addressOrg += ", " + city;
		}
		
		if (zip != null) {
			addressOrg += " (" + zip + ")";
		}
		
		if (country != null) {
			addressOrg += " " + country;
		}
		
		if (telephone != null) {
			addressOrg += " Tel:" + telephone;
		}
		
		if (fax != null) {
			addressOrg += " Fax:" + fax;
		}
		
		if (mail != null) {
			addressOrg += " Email:" + mail;
		}
		
		LOG.trace("Saliendo en ReportDAO.formatAddressOrg()");
		
		return addressOrg;
	}

	/**
	 * Devuelve la dirección completa de un organismo.
	 * 
	 * @param idOrg
	 *            id del organismo.
	 * @return la dirección del organismo.
	 */
	@SuppressWarnings("unchecked")
	public String getAddressOrg(Integer idOrg) {
		
		LOG.trace("Entrando en ReportDAO.getAddressOrg()");
		
		String address = null;
		String addressOrg = "";
		String city = null;
		String zip = null;
		String country = null;
		String telephone = null;
		String fax = null;
		String mail = null;
		List<DirOrgs> dirOrgsList = new ArrayList<DirOrgs>();
		
		dirOrgsList = (List<DirOrgs>) getSqlMapClientTemplate().queryForList( "DirOrgs.selectDirOrgs", idOrg);
		
		if (dirOrgsList != null && dirOrgsList.size() > 0) {
			for (DirOrgs dirOrgs : dirOrgsList) {
				address = dirOrgs.getAddress();
				city = dirOrgs.getCity();
				zip = dirOrgs.getZip();
				country = dirOrgs.getCountry();
				telephone = dirOrgs.getTelephone();
				fax = dirOrgs.getFax();
				mail = dirOrgs.getEmail();
			}
			
			if (address != null) {
				addressOrg += address;
			}
			
			if (city != null) {
				addressOrg += ", " + city;
			}
			
			if (zip != null) {
				addressOrg += " (" + zip + ")";
			}
			
			if (country != null) {
				addressOrg += " " + country;
			}
			
			if (telephone != null) {
				addressOrg += " Tef:" + telephone;
			}
			
			if (fax != null) {
				addressOrg += " Fax:" + fax;
			}
			
			if (mail != null) {
				addressOrg += " Email:" + mail;
			}
			
		}
		
		LOG.trace("Saliendo en ReportDAO.getAddressOrg()");
		
		return addressOrg;
	}

	/**
	 * Devuelve el nombre del departamento padre de un organismo.
	 * 
	 * @param idDep
	 *            Id del departamento.
	 * @param entidad
	 *            Entidad.
	 * @return El nombre del departamento padre del departamento.
	 */
	public String getFatherNameDep(Integer idDep, String entidad) {
	
		LOG.trace("Entrando en ReportsDAO.getFatherNameDep()");
		
		String fatherName = "";
		Transaction tran = null;
		
		try {
			Session session = HibernateUtil.currentSession(entidad);
			tran = session.beginTransaction();
			Iuserdepthdr deptHdr = ISicresQueries.getUserDeptHdrByDeptId(session, idDep);

			if (deptHdr != null) {
				fatherName = deptHdr.getNameDepFather();
				
			} else {
				LOG.info("La consulta no ha devuelto resultados");
			}
			
			HibernateUtil.commitTransaction(tran);
			
		} catch (HibernateException sqlException) {
			LOG.error(ErrorConstants.REPORT_DATA_ERROR, sqlException);
			Utils.redirectToErrorPage(null, null, sqlException);
		}

		finally {
			HibernateUtil.closeSession(entidad);
		}
		
		LOG.trace("Saliendo en ReportsDAO.getFatherNameDep()");
		
		return fatherName;
	}

}
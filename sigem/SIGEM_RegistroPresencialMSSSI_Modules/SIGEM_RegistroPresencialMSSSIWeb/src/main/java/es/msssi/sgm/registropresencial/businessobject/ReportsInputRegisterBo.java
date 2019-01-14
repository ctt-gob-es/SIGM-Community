/*
 * Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
 * Licencia con arreglo a la EUPL, Versión 1.1 o -en cuanto sean aprobadas por laComisión Europea- versiones posteriores de la EUPL (la «Licencia»); 
 * Solo podrá usarse esta obra si se respeta la Licencia. 
 * Puede obtenerse una copia de la Licencia en: 
 * http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
 * Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
 * Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
 */

package es.msssi.sgm.registropresencial.businessobject;

import ieci.tecdoc.sgm.core.config.impl.spring.SigemConfigFilePathResolver;
import ieci.tecdoc.sgm.xml.notificaciondocumento.Estado;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.faces.context.FacesContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRXmlDataSource;

import org.apache.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.LazyDataModel;
import org.primefaces.model.StreamedContent;
import org.springframework.context.ApplicationContext;
import org.xml.sax.InputSource;

import beans.SignResponse;

import com.ieci.tecdoc.common.exception.BookException;
import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.TecDocException;
import com.ieci.tecdoc.common.exception.ValidationException;
import com.ieci.tecdoc.common.invesicres.ScrCa;
import com.ieci.tecdoc.common.invesicres.ScrOfic;
import com.ieci.tecdoc.common.invesicres.ScrOrg;
import com.ieci.tecdoc.common.invesicres.ScrRegstate;
import com.ieci.tecdoc.common.isicres.AxSf;
import com.ieci.tecdoc.common.isicres.AxSfIn;
import com.ieci.tecdoc.common.isicres.AxSfQuery;
import com.ieci.tecdoc.common.isicres.AxSfQueryResults;
import com.ieci.tecdoc.common.keys.ConfigurationKeys;
import com.ieci.tecdoc.common.utils.Configurator;
import com.ieci.tecdoc.isicres.desktopweb.Keys;
import com.ieci.tecdoc.isicres.desktopweb.utils.RBUtil;
import com.ieci.tecdoc.isicres.session.folder.FolderSession;
import com.ieci.tecdoc.isicres.usecase.UseCaseConf;
import com.ieci.tecdoc.utils.cache.CacheBag;
import com.ieci.tecdoc.utils.cache.CacheFactory;

import es.dipucr.metadatos.beans.MetadatosDocumentoBean;
import es.dipucr.metadatos.bussinessobject.MetadatosBo;
import es.dipucr.sigem.api.rule.common.utils.FileUtils;
import es.ieci.tecdoc.fwktd.core.spring.configuration.jdbc.datasource.MultiEntityContextHolder;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.DocumentoElectronicoAnexoVO;
import es.msssi.sgm.registropresencial.beans.InputRegisterBean;
import es.msssi.sgm.registropresencial.beans.Interesado;
import es.msssi.sgm.registropresencial.beans.RowSearchInputRegisterBean;
import es.msssi.sgm.registropresencial.beans.SearchInputRegisterBean;
import es.msssi.sgm.registropresencial.beans.ibatis.Axdoch;
import es.msssi.sgm.registropresencial.beans.ibatis.Axpageh;
import es.msssi.sgm.registropresencial.beans.ibatis.ScrDirofic;
import es.msssi.sgm.registropresencial.config.RegistroPresencialMSSSIWebSpringApplicationContext;
import es.msssi.sgm.registropresencial.daos.ReportDAO;
import es.msssi.sgm.registropresencial.daos.ScrDiroficDAO;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.errors.RPRegisterException;
import es.msssi.sgm.registropresencial.errors.RPReportsErrorCode;
import es.msssi.sgm.registropresencial.errors.RPReportsException;
import es.msssi.sgm.registropresencial.signature.ReportsSignature;
import es.msssi.sgm.registropresencial.utils.Constantes;
import es.msssi.sgm.registropresencial.utils.KeysRP;
import es.msssi.sgm.registropresencial.utils.ResourceRP;
import es.msssi.sgm.registropresencial.utils.Utils;
import es.msssi.sgm.registropresencial.utils.XmlHandler;
import gnu.trove.THashMap;

/**
 * Clase que contiene los métodos para organizar los datos y generar los
 * informes para registros de entrada.
 * 
 * @author jortizs
 * 
 */
public class ReportsInputRegisterBo extends LazyDataModel<RowSearchInputRegisterBean> implements IGenericBo, Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOG = Logger.getLogger(RegisterBo.class.getName());

	/** Criterio de ordenación para informe de relaciones por destino. */
	private static final String ORDERBYDES = "FLD7,FLD4,FLD1";
	/** Criterio de ordenación para informe de relaciones por origen. */
	private static final String ORDERBYORG = "FLD8,FLD4,FLD1";
	/** Tipo de informe de relaciones por destino. */
	private static final int OPTION_TYPE_RMD = 4;
	/** Tipo de informe de relaciones por origen. */
	private static final int OPTION_TYPE_RMO = 5;

	/** Ruta absoluta de las plantillas de informes. */
	private static String REPORT_TEMPLATE_PATH = null;
	/**
	 * Ruta Xpath del fichero XML temporal del que se obtienen los datos de los
	 * registros obtenidos para generar el informe de registros certificados.
	 */
	private static final String REPORT_EXPRESSION_CERT = "/INFORME_RP/REGISTRO";
	/**
	 * Ruta Xpath del fichero XML temporal del que se obtienen los datos de los
	 * registros obtenidos para generar el informe de relaciones por origen.
	 */
	private static final String REPORT_EXPRESSION_ORIGIN_RELATIONS = "/INFORME_RP/FLD8";

	private static final String REPORT_EXPRESSION_DESTINATION_RELATIONS = "/INFORME_RP/FLD7";

	private static ApplicationContext appContext;
	/** Contexto de faces. */
	private FacesContext facesContext;
	/** Libro en sesión. */
	private ScrRegstate book;
	/** Variable con la configuración de la aplicación. */
	private UseCaseConf useCaseConf = null;
	/** Bean con los criterios del buscador. */
	private SearchInputRegisterBean searchInputRegister = new SearchInputRegisterBean();
	/** Lista de interesados. */
	private InterestedBo interestedBo;

	/** Clase que contiene los métodos para generar el XML. */
	private XmlHandler myXmlHandler = null;
	/** Indicador sobre si se incluyen las diligencias o no. */
	private boolean includeProceedingClause = false;

	private ReportDAO reportDAO;
	private ScrDiroficDAO scrDiroficDAO;
	private String acuseJson = "";

	static {
		appContext = RegistroPresencialMSSSIWebSpringApplicationContext.getInstance().getApplicationContext();
		
		if (REPORT_TEMPLATE_PATH == null) {
			// REPORT_TEMPLATE_PATH = (String)
			// WebParameter.getEntryParameter("PATH_REPO") +
			// (String) WebParameter.getEntryParameter("PathReports");
			REPORT_TEMPLATE_PATH = Constantes.RESOURCES_PATH + Constantes.REPORTS_TEMPLATE_PATH;
		}
	}

	/**
	 * Constructor.
	 */
	public ReportsInputRegisterBo() {
		reportDAO = (ReportDAO) appContext.getBean("reportDAO");
		interestedBo = new InterestedBo();
		init();
	}

	/**
	 * Inicializa el contexto de faces.
	 */
	private void init() {
		
		LOG.trace("Entrando en ReportsBo.init() para iniciar el contexto de faces.");
		
		if (searchInputRegister == null) {
			LOG.info("Bean searchInputRegister nulo");
			searchInputRegister = new SearchInputRegisterBean();
		}
		
		if (facesContext == null) {
			facesContext = FacesContext.getCurrentInstance();
			Map<String, Object> map = facesContext.getExternalContext().getSessionMap();
			useCaseConf = (UseCaseConf) map.get(Keys.J_USECASECONF);
			book = (ScrRegstate) facesContext.getExternalContext().getSessionMap().get(KeysRP.J_BOOK);
		}
	}

	/**
	 * Abre la query en SIGM.
	 * 
	 * @return AxSfQuery Información de la query.
	 */
	private AxSfQuery openQuery() {
	
		LOG.trace("Entrando en ReportsBo.openQuery()");
		
		init();
		AxSfQuery axsfQuery = null;

		List<Integer> bookIds = new ArrayList<Integer>();
		bookIds.add(book.getIdocarchhdr().getId());
		
		try {
			axsfQuery = loadAsSfQuery(searchInputRegister, book.getIdocarchhdr().getId());
			axsfQuery.setSelectDefWhere2("FLD8 IS NOT NULL");
			axsfQuery.setOrderBy(" fld1 DESC");
			
			LOG.info("Se intenta abrir la consulta de registros");
			
			int size = FolderSession.openRegistersQuery( useCaseConf.getSessionID(), axsfQuery, bookIds, 0, useCaseConf.getEntidadId());
			this.setRowCount(size);
			
		} catch (ValidationException validationException) {
			LOG.error(ErrorConstants.OPEN_INPUT_REGISTER_QUERY_ERROR_MESSAGE, validationException);
			Utils.redirectToErrorPage(null, validationException, null);
			
		} catch (BookException bookException) {
			LOG.error(ErrorConstants.OPEN_INPUT_REGISTER_QUERY_ERROR_MESSAGE, bookException);
			Utils.redirectToErrorPage(null, bookException, null);
			
		} catch (SessionException sessionException) {
			LOG.error(ErrorConstants.OPEN_INPUT_REGISTER_QUERY_ERROR_MESSAGE, sessionException);
			Utils.redirectToErrorPage(null, sessionException, null);
		}
		
		return axsfQuery;
	}

	/**
	 * Carga los criterios de búsqueda del formulario en el objeto que forma la
	 * query.
	 * 
	 * @param searchInputRegister
	 *            Criterios de búsqueda.
	 * @param idBook
	 *            Id del libro.
	 * 
	 * @return axsfQuery Query completa con los criterios introducidos.
	 * 
	 * @throws BookException
	 *             si ha habido algún problema con el libro de registro.
	 */
	private AxSfQuery loadAsSfQuery( SearchInputRegisterBean searchInputRegister, Integer idBook) throws BookException {
		
		LOG.trace("Entrando en ReportsBo.loadAsSfQuery()");
		
		AxSfQuery axsfQuery = new AxSfQuery();
		axsfQuery.setBookId(idBook);
		axsfQuery.setPageSize(Integer.parseInt(Configurator.getInstance().getProperty(ConfigurationKeys.KEY_DESKTOP_DEFAULT_PAGE_TABLE_RESULTS_SIZE)));
		
		try {
			/* número de registro */
			axsfQuery.addField(searchInputRegister.fieldtoQuery("fld1", idBook));
			/* fecha de registro */
			axsfQuery.addField(searchInputRegister.fieldtoQuery("fld2", idBook));
			/* usuario */
			axsfQuery.addField(searchInputRegister.fieldtoQuery("fld3", idBook));
			/* Fecha de Trabajo */
			axsfQuery.addField(searchInputRegister.fieldtoQuery("fld4", idBook));
			/* Oficina de Registro */
			axsfQuery.addField(searchInputRegister.fieldtoQuery("fld5", idBook));
			/* Estado */
			axsfQuery.addField(searchInputRegister.fieldtoQuery("fld6", idBook));
			/* Origen */
			axsfQuery.addField(searchInputRegister.fieldtoQuery("fld7", idBook));
			/* Destino */
			axsfQuery.addField(searchInputRegister.fieldtoQuery("fld8", idBook));
			/* Remitentes */
			axsfQuery.addField(searchInputRegister.fieldtoQuery("fld9", idBook));
			/* Tipos de Asunto */
			axsfQuery.addField(searchInputRegister.fieldtoQuery("fld14", idBook));
			/* Resumen */
			axsfQuery.addField(searchInputRegister.fieldtoQuery("fld15", idBook));
			/* Tipos de Asunto */
			axsfQuery.addField(searchInputRegister.fieldtoQuery("fld16", idBook));
			/* Resumen */
			axsfQuery.addField(searchInputRegister.fieldtoQuery("fld17", idBook));
			/* Referencia Expediente */
			axsfQuery.addField(searchInputRegister.fieldtoQuery("fld19", idBook));
			/* Referencia Expediente */
			axsfQuery.addField(searchInputRegister.fieldtoQuery("fld503",idBook));
			
		} catch (IllegalArgumentException illegalArgumentException) {
			LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_QUERY_ERROR_MESSAGE, illegalArgumentException);
			Utils.redirectToErrorPage(null, null, illegalArgumentException);
		}

		if (axsfQuery.getPageSize() <= 0) {
			throw new BookException(BookException.ERROR_PAGE_SIZE);
		}

		return axsfQuery;
	}

	/**
	 * Devuelve una lista de resultados de la query que cumplen con los
	 * criterios de búsqueda.
	 * 
	 * @param bookId
	 *            Id del libro.
	 * @params sInputRegister Criterios de búsqueda introducidos.
	 * @return list Lista de resultados.
	 */
	public List<RowSearchInputRegisterBean> getRegistersForRelationReports( Integer bookId, SearchInputRegisterBean sInputRegister) {
		
		LOG.trace("Entrando en ReportsBo.getRegistersForRelationReports()");
		// Se asigna el bean de la búsqueda que llega desde el action al propio
		// de la
		// clase
		searchInputRegister = sInputRegister;
		/*
		 * if (searchInputRegister.getFld2ValueDesde() != null &&
		 * !"".equals(searchInputRegister.getFld2ValueDesde())) {
		 * GregorianCalendar fecha = new GregorianCalendar();
		 * fecha.setTime(searchInputRegister.getFld2ValueDesde()); fecha.set(
		 * Calendar.HOUR_OF_DAY, 0); fecha.set( Calendar.MINUTE, 0); fecha.set(
		 * Calendar.SECOND, 0); Date fromDate = fecha.getTime();
		 * searchInputRegister.setFld2ValueDesde(fromDate); fecha.set(
		 * Calendar.HOUR_OF_DAY, HOUR_OF_DAY); fecha.set( Calendar.MINUTE,
		 * MINUTE_SECOND); fecha.set( Calendar.SECOND, MINUTE_SECOND); Date
		 * toDate = fecha.getTime();
		 * searchInputRegister.setFld2ValueHasta(toDate); } else { if
		 * (searchInputRegister.getFld2ValueDesde() == null &&
		 * !"".equals(searchInputRegister.getFld2ValueDesde()) &&
		 * searchInputRegister.getFld2ValueHasta() == null &&
		 * !"".equals(searchInputRegister.getFld2ValueHasta())) {
		 * LOG.info("Fecha nula. Se buscan los registros del día actual.");
		 * GregorianCalendar fecha = new GregorianCalendar(); fecha.set(
		 * Calendar.HOUR_OF_DAY, 0); fecha.set( Calendar.MINUTE, 0); fecha.set(
		 * Calendar.SECOND, 0); Date fromDate = fecha.getTime();
		 * LOG.info("Fecha desde: " + fecha.getTime().toString());
		 * searchInputRegister.setFld2ValueDesde(fromDate); fecha.set(
		 * Calendar.HOUR_OF_DAY, HOUR_OF_DAY); fecha.set( Calendar.MINUTE,
		 * MINUTE_SECOND); fecha.set( Calendar.SECOND, MINUTE_SECOND); Date
		 * toDate = fecha.getTime(); LOG.info("Fecha hasta: " +
		 * fecha.getTime().toString());
		 * searchInputRegister.setFld2ValueHasta(toDate); }
		 */

		// Se crea la query, siendo necesario cerrarla primero, y se inicializan
		// los
		// objetos necesarios para hacer la búsqueda
		closeQuery();
		AxSfQuery axsfQuery = null;
		axsfQuery = openQuery();
		List<RowSearchInputRegisterBean> registersList = new ArrayList<RowSearchInputRegisterBean>();

		/*
		 * try { axsfQuery = loadAsSfQuery(searchInputRegister, bookId); //
		 * Recuperamos la sesión
		 */
		CacheBag cacheBag;
		try {
			cacheBag = CacheFactory.getCacheInterface().getCacheEntry( useCaseConf.getSessionID());
			THashMap bookInformation = (THashMap) cacheBag.get(axsfQuery.getBookId());
			bookInformation.put("AXSF_QUERY", axsfQuery);
			
		} catch (SessionException sessionException) {
			LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_QUERY_ERROR_MESSAGE, sessionException);
			Utils.redirectToErrorPage(null, sessionException, null);
			
		} catch (TecDocException tecDocException) {
			LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_QUERY_ERROR_MESSAGE, tecDocException);
			Utils.redirectToErrorPage(null, tecDocException, null);
		}
		
		/*
		 * } catch (BookException e) { e.printStackTrace(); }
		 */
		if (this.getRowCount() > 0 && this.getRowCount() <= KeysRP.NUM_REGISTERINFORM) {
			String orderBy = "";

			// Se establece la ordenación según el tipo de informe
			if (OPTION_TYPE_RMD == searchInputRegister.getReportTypeValue()) {
				orderBy = ORDERBYDES;
				orderBy += " ASC ";
				axsfQuery.setOrderBy(orderBy);
				
			} else if (OPTION_TYPE_RMO == searchInputRegister.getReportTypeValue()) {
				orderBy = ORDERBYORG;
				orderBy += " ASC ";
				axsfQuery.setOrderBy(orderBy);
			}
			
			// Se añaden las diligencias, si se ha indicado en el formulario
			if (searchInputRegister.isIncludeProceedingValue()) {
				includeProceedingClause = true;
			}

			try {
				// Se obtienen los registros y se añaden en la lista final
				AxSfQueryResults queryResults = FolderSession.navigateRegistersQuery(useCaseConf.getSessionID(), bookId, com.ieci.tecdoc.common.isicres.Keys.QUERY_ALL, useCaseConf.getLocale(), useCaseConf.getEntidadId(), orderBy);
				registersList = loadQueryResulttoList(queryResults, useCaseConf.getLocale());
				
			} catch (ValidationException validationException) {
				LOG.error( ErrorConstants.NAVIGATE_TO_INPUT_REGISTERS_ROW_ERROR_MESSAGE, validationException);
				Utils.redirectToErrorPage(null, validationException, null);
				
			} catch (BookException bookException) {
				LOG.error( ErrorConstants.NAVIGATE_TO_INPUT_REGISTERS_ROW_ERROR_MESSAGE, bookException);
				Utils.redirectToErrorPage(null, bookException, null);
				
			} catch (SessionException sessionException) {
				LOG.error( ErrorConstants.NAVIGATE_TO_INPUT_REGISTERS_ROW_ERROR_MESSAGE, sessionException);
				Utils.redirectToErrorPage(null, sessionException, null);
			}
		}
		
		return registersList;
	}

	/**
	 * Mapea los resultados de las búsquedas de SIGM con el nuevo aplicativo.
	 * 
	 * @param queryResults
	 *            objeto del core de sigem que contiene los resultados de la
	 *            búsqueda.
	 * @param locale
	 *            el locale de la búsqueda.
	 * 
	 * @return data Lista con los resultados de la búsqueda.
	 */
	@SuppressWarnings("unchecked")
	private List<RowSearchInputRegisterBean> loadQueryResulttoList( AxSfQueryResults queryResults, Locale locale) {
		
		LOG.trace("Entrando en ReportsBo.loadQueryResulttoList()");
		
		List<RowSearchInputRegisterBean> data = new ArrayList<RowSearchInputRegisterBean>();
		AxSfIn axSfIn;
		RowSearchInputRegisterBean bean;
		
		for (Iterator<AxSfIn> it = queryResults.getResults().iterator(); it.hasNext();) {
			
			axSfIn = (AxSfIn) it.next();
			bean = new RowSearchInputRegisterBean();
			bean.setFdrid(new Integer(axSfIn.getAttributeValueAsString("fdrid")));
			bean.setFld1(axSfIn.getAttributeValueAsString("fld1"));
			bean.setFld2((axSfIn.getAttributeValue("fld2") != null) ? (Date) axSfIn.getAttributeValue("fld2") : null);
			bean.setFld3(axSfIn.getAttributeValueAsString("fld3"));
			bean.setFld5(axSfIn.getFld5());
			bean.setFld5Name(axSfIn.getFld5Name());
			bean.setFld6(axSfIn.getAttributeValueAsString("fld6"));
			bean.setFld6Name(RBUtil.getInstance(locale).getProperty( "book.fld6." + axSfIn.getAttributeValueAsString("fld6")));
			bean.setFld7(axSfIn.getFld7());
			bean.setFld7Name(axSfIn.getFld7Name());
			bean.setFld8(axSfIn.getFld8());
			bean.setFld8Name(axSfIn.getFld8Name());
			bean.setFld9(axSfIn.getAttributeValueAsString("fld9"));
			bean.setFld13(axSfIn.getFld13());
			bean.setFld13Name((axSfIn.getFld13() != null) ? axSfIn.getFld13().getName() : null);
			bean.setFld16(axSfIn.getFld16());
			bean.setFld16Name((axSfIn.getFld16() != null) ? axSfIn.getFld16().getCode() : null);
			bean.setFld17(axSfIn.getAttributeValueAsString("fld17"));
			bean.setFld19(axSfIn.getAttributeValueAsString("fld19"));
			data.add(bean);
		}
		
		return data;
	}

	/**
	 * Cierra la query en SIGM.
	 */
	public void closeQuery() {
		LOG.trace("Entrando en ReportsBo.closeQuery()");
		
		try {
			init();
			FolderSession.closeRegistersQuery(useCaseConf.getSessionID(), book.getIdocarchhdr().getId());
			
		} catch (ValidationException validationException) {
			LOG.error(ErrorConstants.CLOSE_INPUT_REGISTER_QUERY_ERROR_MESSAGE, validationException);
			Utils.redirectToErrorPage(null, validationException, null);
			
		} catch (BookException bookException) {
			LOG.error(ErrorConstants.CLOSE_INPUT_REGISTER_QUERY_ERROR_MESSAGE, bookException);
			Utils.redirectToErrorPage(null, bookException, null);
			
		} catch (SessionException sessionException) {
			LOG.error(ErrorConstants.CLOSE_INPUT_REGISTER_QUERY_ERROR_MESSAGE, sessionException);
			Utils.redirectToErrorPage(null, sessionException, null);
		}
	}

	/**
	 * Elimina los registros que no contengan organismo origen o destino, en una
	 * lista para informe por certificado o por relaciones de origen o destino.
	 * 
	 * @param relationsList
	 *            Lista de registros para informe de relaciones.
	 * @return finalList Lista final de registros.
	 */
	// public List<RowSearchInputRegisterBean> removeRegistersWithoutOrganisms(
	// List<RowSearchInputRegisterBean> relationsList) {
	// List<RowSearchInputRegisterBean> finalRelationsList = null;
	// int deletedRegistersCounter = 0;
	// int initialRegistersListLength = 0;
	// if (relationsList != null &&
	// relationsList.size() > 0) {
	// finalRelationsList = relationsList;
	// Iterator<RowSearchInputRegisterBean> registersListIterator =
	// finalRelationsList.iterator();
	// initialRegistersListLength = finalRelationsList.size();
	// while (registersListIterator.hasNext()) {
	// final RowSearchInputRegisterBean rowSIRBean =
	// (RowSearchInputRegisterBean) registersListIterator.next();
	// if (OPTION_TYPE_RMD == searchInputRegister.getReportTypeValue()) {
	// if (rowSIRBean.getFld8() == null ||
	// (rowSIRBean.getFld8() != null && rowSIRBean.getFld7() == null)) {
	// registersListIterator.remove();
	// deletedRegistersCounter++;
	// }
	// }
	// else if (OPTION_TYPE_RMO == searchInputRegister.getReportTypeValue()) {
	// if (rowSIRBean.getFld7() == null ||
	// (rowSIRBean.getFld7() != null && rowSIRBean.getFld8() == null)) {
	// registersListIterator.remove();
	// deletedRegistersCounter++;
	// }
	// }
	// }
	// }
	// LOG.info("Se han eliminado " +
	// deletedRegistersCounter + " registros de un total de " +
	// initialRegistersListLength +
	// " al no tener los datos necesarios para estar en el informe");
	// return relationsList;
	// }

	/**
	 * Construye el documento XML correspondiente a los datos del informe.
	 * 
	 * @param reportResults
	 *            Datos del registro para generar el acuse de recibo.
	 * @param idReg
	 *            id de registro.
	 */
	private void buildXmlCertificateReportDocument(HashMap<String, Object> reportResults, Integer idReg) {
		
		LOG.trace("Entrando en ReportsBo.buildXmlCertificateReportDocument()");
		
		myXmlHandler = new XmlHandler("INFORME_RP");
		myXmlHandler.addBeginXmlHandler("REGISTRO");
		Map<String, Object> map = reportResults;
		
		for (Entry<String, Object> entry : map.entrySet()) {
			if (entry.getValue() != null && !"FLD9".equals(entry.getKey())) {
				if (entry.getValue() instanceof ScrOfic) {
					ScrOfic scrOfic = (ScrOfic) entry.getValue();
					myXmlHandler.addNode(String.valueOf(entry.getKey()), scrOfic.getId());
					
				} else if (entry.getValue() instanceof ScrOrg) {
					ScrOrg scrOrg = (ScrOrg) entry.getValue();
					myXmlHandler.addNode(String.valueOf(entry.getKey()), scrOrg.getId());
					
				} else if (entry.getValue() instanceof ScrCa) {
					ScrCa scrCa = (ScrCa) entry.getValue();
					myXmlHandler.addNode(String.valueOf(entry.getKey()), scrCa.getId());
					
				} else if (entry.getValue() instanceof Timestamp) {
					myXmlHandler.addNode(String.valueOf(entry.getKey()), Utils.formatTimeStampInString((Timestamp) entry.getValue()));
					
				} else if (entry.getValue() instanceof Date) {
					myXmlHandler.addNode(String.valueOf(entry.getKey()), Utils.formatDateInString((Date) entry.getValue()));

				} else {
					if (!"FLD9".equals(entry.getKey())) {
						myXmlHandler.addNode(String.valueOf(entry.getKey()), entry.getValue());
					}
				}
				
			} else if ("FLD9".equals(entry.getKey())) {
				myXmlHandler.addNode( String.valueOf(entry.getKey()), interestedBo.fillSenderFieldFromSenderListToRelationsReport( idReg, book.getIdocarchhdr().getId(), useCaseConf));
			}
		}
		
		List<LinkedHashMap<String, Object>> attacheds = getRegisterAttachedDocuments(book.getIdocarchhdr().getId(), idReg);
		
		if (attacheds != null) {
			myXmlHandler.addBeginXmlHandler("DOCUMENTOS");
			
			for (LinkedHashMap<String, Object> attached : attacheds) {			
				myXmlHandler.addBeginXmlHandler("DOCUMENTO");
				myXmlHandler.addNode("NOMBRE", attached.get("NAME"));
				
				if (attached.get("SIZE") != null) {
					myXmlHandler.addNode("TAMANIO", attached.get("SIZE"));
				}
				
				if (attached.get("HASH") != null) {
					myXmlHandler.addNode("HASH", attached.get("HASH"));
				}
				
				if (attached.get("ESTADO_ELABORACION") != null) {
					myXmlHandler.addNode( "ESTADO_ELABORACION", attached.get("ESTADO_ELABORACION"));
				}
				
				if (attached.get("COMMENT") != null) {
					myXmlHandler.addNode("COMENTARIOS", attached.get("COMMENT"));
				}
				
				if (attached.get("CSV") != null) {
					myXmlHandler.addNode("CSV", attached.get("CSV"));
				}
				
				if (attached.get("FECHA_ALTA") != null) {
					myXmlHandler.addNode("FECHA_ALTA", attached.get("FECHA_ALTA"));
				}
				
				if (attached.get("FECHA_FIRMA") != null) {
					myXmlHandler.addNode("FECHA_FIRMA", attached.get("FECHA_FIRMA"));
				}
				
				myXmlHandler.addEndXmlHandler("DOCUMENTO");
			}
			
			myXmlHandler.addEndXmlHandler("DOCUMENTOS");
		}

		myXmlHandler.addNode("PROCEEDING", KeysRP.REPORT_PROCEEEDING_TEXT);
		myXmlHandler.addEndXmlHandler("REGISTRO");
		
		LOG.warn("XML Constuido: " + myXmlHandler.toString());
	}

	/**
	 * Obtiene los documentos asociados a un registro.
	 */
	public List<LinkedHashMap<String, Object>> getRegisterAttachedDocuments( Integer idBook, Integer idReg) {
		
		List<Axdoch> listDocuments = new ArrayList<Axdoch>();
		RegisterDocumentsBo registerDocumentsBo = null;
		List<LinkedHashMap<String, Object>> documents = new ArrayList<LinkedHashMap<String, Object>>();
		LinkedHashMap<String, Object> document = null;
		
		if (registerDocumentsBo == null) {
			registerDocumentsBo = new RegisterDocumentsBo();
		}
		
		try {
			LOG.info("Cargando documentos asociados al registro " + idReg);
			
			listDocuments = registerDocumentsBo.getDocumentsBasicInfo( useCaseConf, idBook, idReg, false, new Integer(1));

			for (Axdoch doc : listDocuments) {
				for (Axpageh page : doc.getPages()) {
//					if (page.getPageSignedId() == null || page.getPageSignedId().equals(page.getId())) {
						
						document = new LinkedHashMap<String, Object>();
						document.put("NAME", page.getName());
						document.put("SIZE", page.getFileSizeKB());
						document.put("VALIDTYPE", page.getValidityType());
						document.put("DOCUMENTTYPE", page.getDocumentType());
						document.put("COMMENT", page.getComments());
						document.put("HASH", (page.getHash() != null) ? new String(page.getHash()) : null);
						
						MetadatosDocumentoBean metadatos = new MetadatosDocumentoBean(idBook, idReg, page.getPageId().intValue(), page.getFileId().intValue(), useCaseConf.getEntidadId(), page.getName()); 
						MetadatosBo.getMetadatos(metadatos);
						
						String csv = "";
						String fechaAlta = "";
						String fechaFirma = "";
						String estadoElaboracion = "";
						
						if(null != page.getCrtndate()){
							fechaAlta = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(page.getCrtndate());
						}
						if(null != page.getFechaFirma()){
							fechaFirma = new String(page.getFechaFirma());
						}
						if(null != metadatos.getCsv()){
							csv = metadatos.getCsv();
						}						
						if(null != metadatos.getEstadoElaboracion()){
							estadoElaboracion = metadatos.getEstadoElaboracion();
						}

						document.put("CSV", csv);
						document.put("FECHA_ALTA", fechaAlta);
						document.put("FECHA_FIRMA", fechaFirma);
						document.put("ESTADO_ELABORACION", estadoElaboracion);
						
						documents.add(document);
//					}
				}
			}
			
			if (documents.size() == 0) {
				documents = null;
			}
			
		} catch (RPRegisterException rPRegisterException) {
			LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE, rPRegisterException);
		}
		
		return documents;
	}

	/**
	 * Construye el documento XML correspondiente a los datos de los informes.
	 * 
	 * @param registersList
	 *            Lista de datos de certificados obtenidos, un informe por
	 *            registro.
	 * @param isOriginReport
	 *            si es un informe de relación por origen.
	 */
	private void buildXmlRelationsReportDocument( List<RowSearchInputRegisterBean> registersList, boolean isOriginReport) {
		
		LOG.trace("Entrando en ReportsBo.buildXmlRelationsReportDocument()");
		
		myXmlHandler = new XmlHandler("INFORME_RP");
		
		if (isOriginReport) {
			LOG.info("Es un informe de relaciones por origen. "	+ "Se rellena el XML para el informe teniendo en cuenta esto");
			fillFldForOriginNodes(registersList);
			
		} else {
			LOG.info("Es un informe de relaciones por destino. " + "Se rellena el XML para el informe teniendo en cuenta esto");
			fillFldForDestinationNodes(registersList);
		}
		
		LOG.warn("XML Constuido: " + myXmlHandler.toString());
	}

	/**
	 * Construye el esquema XML con el que se rellenará el informe de relaciones
	 * por origen.
	 * 
	 * @param registersList
	 *            Lista de registros.
	 */
	private void fillFldForOriginNodes( List<RowSearchInputRegisterBean> registersList) {
		
		myXmlHandler.addBeginXmlHandler("FLD8");
		myXmlHandler.addNode("FLD8_TEXT", registersList.get(0).getFld8Name() != null ? registersList.get(0).getFld8Name() : "");
		myXmlHandler.addNode("FLD8_FATHERNAME", registersList.get(0).getFld8().getNameOrgFather());
		myXmlHandler.addNode("FLD8_ADDRESS", reportDAO.getAddressOrg(registersList.get(0).getFld8().getId()));

		// Se añade un nodo FLD5 con todos los datos. Contendrá el nombre de la
		// oficina,
		// diligencias y órganos.
		myXmlHandler.addNode("FLD5_TEXT", registersList.get(0).getFld5Name());
		ScrDirofic direccion = getDireccionOfic(registersList.get(0).getFld5().getId());
		
		if (direccion != null) {
			myXmlHandler.addNode("FLD5_ADDRESS", reportDAO.formatAddressOfic( direccion.getAddress(), direccion.getCity(), direccion.getZip(), direccion.getCountry(), direccion.getTelephone(), direccion.getFax(), direccion.getEmail()));
		}
		
		// Se añade el texto de las diligencias, que podrá ser modificado por
		// cualquier otro texto
		if (includeProceedingClause) {
			LOG.info("Se añaden las diligencias");
			myXmlHandler.addNode("PROCEEDING", KeysRP.REPORT_PROCEEEDING_TEXT);
		}
		
		int numOrganoOrigen = ((ScrOrg) registersList.get(0).getFld8()).getId().intValue();
		int finalRegistersListLength = registersList.size();
		int registerCounter = 0;
		
		LOG.warn("finalRegistersListLength es: " + finalRegistersListLength);
		
		// Se añaden los datos de los registros para cada oficina
		for (int i = 0; i < finalRegistersListLength; i++) {
			if (numOrganoOrigen == registersList.get(i).getFld8().getId()) {
				registerCounter++;
				fillRegisterNode(registersList.get(i), true);
				
			} else {
				numOrganoOrigen = registersList.get(i).getFld8().getId();
				myXmlHandler.addNode("TOTAL_REGISTROS", registerCounter);
				myXmlHandler.addEndXmlHandler("FLD8");
				myXmlHandler.addBeginXmlHandler("FLD8");
				myXmlHandler.addNode("FLD8_TEXT", registersList.get(i).getFld8Name() != null ? registersList.get(i).getFld8Name() : "");
				myXmlHandler.addNode("FLD8_FATHERNAME", registersList.get(i).getFld8().getNameOrgFather());
				myXmlHandler.addNode("FLD8_ADDRESS", reportDAO.getAddressOrg(registersList.get(i).getFld8().getId()));
				
				// Se añade otro nodo FLD5 con todos los datos. Contendrá el
				// nombre de la
				// oficina, diligencias y órganos.
				myXmlHandler.addNode("FLD5_TEXT", registersList.get(i).getFld5Name());
				ScrDirofic direccion1 = getDireccionOfic(registersList.get(0).getFld5().getId());
				
				if (direccion1 != null) {
					myXmlHandler.addNode("FLD5_ADDRESS", reportDAO.formatAddressOfic(direccion1.getAddress(), direccion1.getCity(), direccion1.getZip(), direccion1.getCountry(), direccion1.getTelephone(), direccion1.getFax(), direccion1.getEmail()));
				}
				
				// Se añade el texto de las diligencias, que podrá ser
				// modificado por
				// cualquier otro texto
				if (includeProceedingClause) {
					LOG.info("Se añaden las diligencias");
					myXmlHandler.addNode("PROCEEDING", KeysRP.REPORT_PROCEEEDING_TEXT);
				}
				
				fillRegisterNode(registersList.get(i), true);
				registerCounter = 1;
			}
		}
		
		myXmlHandler.addNode("TOTAL_REGISTROS", registerCounter);
		myXmlHandler.addEndXmlHandler("FLD8");
	}

	/**
	 * Construye el esquema XML con el que se rellenará el informe de relaciones
	 * por destino.
	 * 
	 * @param registersList
	 *            Lista de registros.
	 */
	private void fillFldForDestinationNodes( List<RowSearchInputRegisterBean> registersList) {
		
		myXmlHandler.addBeginXmlHandler("FLD7");
		myXmlHandler.addNode("FLD7_TEXT", registersList.get(0).getFld7Name() != null ? registersList.get(0).getFld7Name() : "");
		myXmlHandler.addNode("FLD7_FATHERNAME", registersList.get(0).getFld7() != null ? registersList.get(0).getFld7().getNameOrgFather() : "");
		myXmlHandler.addNode("FLD7_ADDRESS", registersList.get(0).getFld7() != null ? reportDAO.getAddressOrg(registersList.get(0).getFld7().getId()) : "");
		
		// Se añade un nodo FLD5 con todos los datos. Contendrá el nombre de la
		// oficina,
		// diligencias y órganos.
		myXmlHandler.addNode("FLD5_TEXT", registersList.get(0).getFld5Name());
		ScrDirofic direccion = getDireccionOfic(registersList.get(0).getFld5().getId());
		
		if (direccion != null) {
			myXmlHandler.addNode("FLD5_ADDRESS", reportDAO.formatAddressOfic( direccion.getAddress(), direccion.getCity(), direccion.getZip(), direccion.getCountry(), direccion.getTelephone(), direccion.getFax(), direccion.getEmail()));
		}
		
		// Se añade el texto de las diligencias, que podrá ser modificado por
		// cualquier otro texto
		if (includeProceedingClause) {
			LOG.info("Se añaden las diligencias");
			myXmlHandler.addNode("PROCEEDING", KeysRP.REPORT_PROCEEEDING_TEXT);
		}
		
		int numOrganoOrigen = registersList.get(0).getFld7() != null ? ((ScrOrg) registersList.get(0).getFld7()).getId().intValue() : 0;
		int finalRegistersListLength = registersList.size();
		int registerCounter = 0;
		int numOrganoOrigenNew = 0;
		
		// Se añaden los datos de los registros para cada oficina
		for (int i = 0; i < finalRegistersListLength; i++) {
			numOrganoOrigenNew = registersList.get(i).getFld7() != null ? ((ScrOrg) registersList.get(i).getFld7()).getId().intValue() : 0;
			
			if (numOrganoOrigen == numOrganoOrigenNew) {
				registerCounter++;
				fillRegisterNode(registersList.get(i), false);
				
			} else {
				numOrganoOrigen = numOrganoOrigenNew;
				myXmlHandler.addNode("TOTAL_REGISTROS", registerCounter);
				myXmlHandler.addEndXmlHandler("FLD7");
				myXmlHandler.addBeginXmlHandler("FLD7");
				myXmlHandler.addNode("FLD7_TEXT", registersList.get(i).getFld7Name() != null ? registersList.get(i).getFld7Name() : "");
				myXmlHandler.addNode("FLD7_FATHERNAME", registersList.get(i).getFld7() != null ? registersList.get(i).getFld7().getNameOrgFather() : "");
				myXmlHandler.addNode("FLD7_ADDRESS", registersList.get(i).getFld7() != null ? reportDAO.getAddressOrg(registersList.get(i).getFld7().getId()) : "");
				
				// Se añade otro nodo FLD5 con todos los datos. Contendrá el
				// nombre de la
				// oficina, diligencias y órganos.
				myXmlHandler.addNode("FLD5_TEXT", registersList.get(i).getFld5Name());
				ScrDirofic direccion1 = getDireccionOfic(registersList.get(0).getFld5().getId());
				
				if (direccion1 != null) {
					myXmlHandler.addNode("FLD5_ADDRESS", reportDAO.formatAddressOfic(direccion1.getAddress(), direccion1.getCity(), direccion1.getZip(), direccion1.getCountry(), direccion1.getTelephone(), direccion1.getFax(), direccion1.getEmail()));
				}
				
				// Se añade el texto de las diligencias, que podrá ser
				// modificado por
				// cualquier otro texto
				if (includeProceedingClause) {
					LOG.info("Se añaden las diligencias");
					myXmlHandler.addNode("PROCEEDING", KeysRP.REPORT_PROCEEEDING_TEXT);
				}
				
				fillRegisterNode(registersList.get(i), true);
				registerCounter = 1;
			}
		}
		
		myXmlHandler.addNode("TOTAL_REGISTROS", registerCounter);
		myXmlHandler.addEndXmlHandler("FLD7");
	}

	/**
	 * Rellena los nodos REGISTRO de cada oficina, para los informes de
	 * relaciones.
	 * 
	 * @param rowSIRBean
	 *            Bean con los datos a rellenar.
	 * @param isOriginReport
	 *            Si es un informe de relación por origen.
	 */
	private void fillRegisterNode(RowSearchInputRegisterBean rowSIRBean, boolean isOriginReport) {
		
		myXmlHandler.addBeginXmlHandler("REGISTRO");
		myXmlHandler.addNode("FDRID", rowSIRBean.getFdrid() != null ? rowSIRBean.getFdrid() : "");
		myXmlHandler.addNode("FLD1", rowSIRBean.getFld1() != null ? rowSIRBean.getFld1() : "");
		myXmlHandler.addNode("FLD2", rowSIRBean.getFld2() != null ? Utils.formatDateInString((Date) rowSIRBean.getFld2()) : "");
		myXmlHandler.addNode("FLD3", rowSIRBean.getFld3() != null ? rowSIRBean.getFld3() : "");
		myXmlHandler.addNode("FLD4", rowSIRBean.getFld4() != null ? Utils.formatDateInString((Date) rowSIRBean.getFld4()) : "");
		myXmlHandler.addNode("FLD6", rowSIRBean.getFld6() != null ? String.valueOf(rowSIRBean.getFld6()) : "");
		
		if (isOriginReport) {
			myXmlHandler.addNode("FLD7", rowSIRBean.getFld7() != null ? String.valueOf(((ScrOrg) rowSIRBean.getFld7()).getId().intValue()) : "");
			
		} else {
			myXmlHandler.addNode("FLD8", rowSIRBean.getFld8() != null ? String.valueOf(((ScrOrg) rowSIRBean.getFld8()).getId().intValue()) : "");
		}
		
		myXmlHandler.addNode("FLD9", interestedBo.fillSenderFieldFromSenderListToRelationsReport( rowSIRBean.getFdrid(), book.getIdocarchhdr().getId(), useCaseConf));
		myXmlHandler.addNode("FLD10", rowSIRBean.getFld10() != null ? rowSIRBean.getFld10() : "");
		myXmlHandler.addNode("FLD11", rowSIRBean.getFld11() != null ? String.valueOf(rowSIRBean.getFld11()) : "");
		myXmlHandler.addNode("FLD12", rowSIRBean.getFld12() != null ? Utils.formatDateInString((Date) rowSIRBean.getFld12()) : "");
		myXmlHandler.addNode("FLD13", rowSIRBean.getFld13() != null ? String.valueOf(((ScrOrg) rowSIRBean.getFld13()).getId().intValue()) : "");
		myXmlHandler.addNode("FLD14", rowSIRBean.getFld14() != null ? rowSIRBean.getFld14() : "");
		myXmlHandler.addNode("FLD15", rowSIRBean.getFld15() != null ? rowSIRBean.getFld15() : "");
		myXmlHandler.addNode("FLD16", rowSIRBean.getFld16() != null ? String.valueOf(((ScrCa) rowSIRBean.getFld16()).getId().intValue()) : "");
		myXmlHandler.addNode("FLD17", rowSIRBean.getFld17() != null ? rowSIRBean.getFld17() : "");
		myXmlHandler.addNode("FLD19", rowSIRBean.getFld19() != null ? rowSIRBean.getFld19() : "");
		myXmlHandler.addNode("FLD7_TEXT", rowSIRBean.getFld7Name() != null ? rowSIRBean.getFld7Name() : "");
		myXmlHandler.addNode("FLD8_TEXT", rowSIRBean.getFld8Name() != null ? rowSIRBean.getFld8Name() : "");
		myXmlHandler.addNode("FLD13_TEXT", rowSIRBean.getFld13Name() != null ? rowSIRBean.getFld13Name() : "");
		myXmlHandler.addNode("FLD16_TEXT", rowSIRBean.getFld16Name() != null ? rowSIRBean.getFld16Name() : "");
		myXmlHandler.addEndXmlHandler("REGISTRO");
	}

	/**
	 * Construye el informe mediante Jasper y lo envía en la respuesta en
	 * formato PDF.
	 * 
	 * @param bookId
	 *            Id. del libro.
	 * @param reportResults
	 *            Lista de registro de un certificado.
	 * @param registersList
	 *            Lista de registro de una relación.
	 * @return StreamedContent fichero que contiene el informe.
	 * @throws Exception
	 *             Si se ha producido un error generando el informe.
	 */
	public StreamedContent buildJasperReport(ScrRegstate book, List<LinkedHashMap<String, Object>> reportResults, List<RowSearchInputRegisterBean> registersList) throws Exception {
		
		LOG.trace("Entrando en ReportsBo.buildJasperReport()");
		
		byte[] pdfSignedReportByteArray = null;
		byte[] pdfReport = null;
		String reportName = "";
		Date fecha_firma = null;
		String reportTemplateName = "";
		String reportExpression = "";
		StreamedContent file = null;
		List<StreamedContent> filesToUpload = new ArrayList<StreamedContent>();
		LinkedHashMap<String, Object> certReport = null;
		InputRegisterBo inputRegisterBo = null;
		InputRegisterBean inputRegisterBean = null;
		AxSf registerData = null;
		RegisterDocumentsBo registerDocumentsBo = null;

		StreamedContent content = null;
		
		init();
		
		Integer numAcuses = 0;
		if (reportResults != null && reportResults.size() > 0) {
			if (reportResults.size() == 1) {
				// GENERAR UN CERTIFICADO
				certReport = buildAndSignCertificateReport(reportResults.get(0));
				pdfSignedReportByteArray = (byte[]) certReport.get("DATA");
				reportName = (String) certReport.get("NAME");				
				fecha_firma = (Date) certReport.get("FECHA_FIRMA");

				inputRegisterBo = new InputRegisterBo();
				inputRegisterBean = inputRegisterBo.loadInputRegisterBean( useCaseConf, book, (Integer) reportResults.get(0).get("FDRID"));

				registerData = Utils.mappingInputRegisterToAxSF(inputRegisterBean);
				registerDocumentsBo = new RegisterDocumentsBo();

				numAcuses = registerDocumentsBo.countPageReport(useCaseConf, book.getIdocarchhdr().getId(), inputRegisterBean.getFdrid());
				String nombre = null;
				
				if (numAcuses > 0) {
					nombre = reportName;
					numAcuses = numAcuses + 1; // es el siguiente
					nombre = nombre.substring(0, nombre.indexOf(".")) + "_" + numAcuses + ".pdf";
					content = new DefaultStreamedContent( new ByteArrayInputStream(pdfSignedReportByteArray), KeysRP.MIME_TYPE_PDF, nombre);
					
				} else {
					nombre = reportName;
					numAcuses = numAcuses + 1; // es el siguiente
					content = new DefaultStreamedContent( new ByteArrayInputStream(pdfSignedReportByteArray), KeysRP.MIME_TYPE_PDF, nombre);
				}
				
				filesToUpload.add(content);
				DocumentoElectronicoAnexoVO documentSaved = registerDocumentsBo.saveStreamedContentDocuments(useCaseConf.getSessionID(), book.getIdocarchhdr().getId(), (Integer) reportResults.get(0).get("FDRID"), filesToUpload, registerData, useCaseConf.getLocale(), useCaseConf.getEntidadId(), fecha_firma);
				
				if (documentSaved != null) {
					/*
					 * Axdoch acuseDoc = null; acuseDoc =
					 * registerDocumentsBo.getLastReport(useCaseConf,
					 * book.getIdocarchhdr().getId(),
					 * inputRegisterBean.getFdrid());
					 * 
					 * 
					 * if ( acuseDoc != null){
					 */
					
					
					MetadatosBo.insertaMetadatosJustificante(useCaseConf.getSessionID(), book.getIdocarchhdr().getId(), (Integer) reportResults.get(0).get("FDRID"), documentSaved.getId().getIdPagina().intValue(), documentSaved.getId().getIdFile().intValue(), useCaseConf.getEntidadId(), fecha_firma, FileUtils.getExtensionByNombreDoc(nombre),(String) certReport.get("CSV"));
				        
					acuseJson = "[{\"iddoc\":" + documentSaved.getId().getId() + ",\"idpag\":" + documentSaved.getId().getIdPagina() + ",\"csv\":\"" + (String) certReport.get("CSV") + "\"}]";
					
					InputRegisterBean inputBeanUpdate = new InputRegisterBean();
					inputBeanUpdate.setFdrid(inputRegisterBean.getFdrid());
					inputBeanUpdate.setFld1(inputRegisterBean.getFld1());
					inputBeanUpdate.setFld1004(acuseJson);
					
					if (inputRegisterBean.getInteresados() != null && inputRegisterBean.getInteresados().size() > 0) {
						inputBeanUpdate.setInteresados(inputRegisterBean.getInteresados());
						
					} else {
						if (inputRegisterBean.getFld9() != null && !"".equals(inputRegisterBean.getFld9().trim())) {							
							List<Interesado> interesados = new ArrayList<Interesado>();
							Interesado interesado = new Interesado();
							interesado.setNombre(inputRegisterBean.getFld9());
							interesados.add(interesado);
							inputBeanUpdate.setInteresados(interesados);
						}
					}
					inputRegisterBo.updateOnlyFolder(useCaseConf, book.getIdocarchhdr().getId(), inputRegisterBean.getFdrid(), inputBeanUpdate);
					registerDocumentsBo.updateFlag(useCaseConf, book.getIdocarchhdr().getId(), inputRegisterBean.getFdrid(), Long.valueOf(documentSaved.getId().getIdPagina()).intValue(), numAcuses);
					// }
				}

				file = new DefaultStreamedContent(new ByteArrayInputStream( pdfSignedReportByteArray), KeysRP.MIME_TYPE_PDF, nombre);
				
			} else {
				// GENERAR UN ZIP CON TODOS LOS CERTIFICADOS
				ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
				ZipOutputStream zipOutputStream = new ZipOutputStream( baOutputStream);
				String zipName = KeysRP.IR_REPORT_CERTIFICATE_NAME + KeysRP.REPORT_CERTIFICATE_EXTENSION_ZIP;
				
				for (int i = 0; i < reportResults.size(); i++) {
					certReport = buildAndSignCertificateReport(reportResults.get(i));
					pdfSignedReportByteArray = (byte[]) certReport.get("DATA");
					reportName = (String) certReport.get("NAME");
					
					generateCertificatesZipFile(zipOutputStream, pdfSignedReportByteArray, reportName);
				}
				
				zipOutputStream.close();
				
				LOG.info("Generado Zip de acuses de recibo.");
				
				file = new DefaultStreamedContent(new ByteArrayInputStream( baOutputStream.toByteArray()), KeysRP.MIME_TYPE_ZIP, zipName);
			}
			
		} else if (registersList != null && registersList.size() > 0) {
			// GENERAR UN INFORME DE DESTINO
			if (OPTION_TYPE_RMD == searchInputRegister.getReportTypeValue()) {
			
				buildXmlRelationsReportDocument(registersList, false);
				
				reportName = KeysRP.IR_REPORT_DESTINATION_RELATIONS_NAME;
				reportTemplateName = KeysRP.IR_REPORT_DESTINATION_RELATIONS_TEMPLATE_NAME;
				reportExpression = REPORT_EXPRESSION_DESTINATION_RELATIONS;
				
				LOG.info("Vienen datos referentes a un informe de relaciones por destino. " + "Nombre informe: " + KeysRP.IR_REPORT_DESTINATION_RELATIONS_NAME + "; nombre plantilla: " + KeysRP.IR_REPORT_DESTINATION_RELATIONS_TEMPLATE_NAME + ", Xpath: " + REPORT_EXPRESSION_DESTINATION_RELATIONS);

			} else if (OPTION_TYPE_RMO == searchInputRegister.getReportTypeValue()) {
				
				// GENERAR UN INFORME DE ORIGEN
				buildXmlRelationsReportDocument(registersList, true);
				
				reportName = KeysRP.IR_REPORT_ORIGIN_RELATIONS_NAME;
				reportTemplateName = KeysRP.IR_REPORT_ORIGIN_RELATIONS_TEMPLATE_NAME;
				reportExpression = REPORT_EXPRESSION_ORIGIN_RELATIONS;
				
				LOG.info("Vienen datos referentes a un informe de relaciones por origen. " + "Nombre informe: " + KeysRP.IR_REPORT_ORIGIN_RELATIONS_NAME + "; nombre plantilla: " + KeysRP.IR_REPORT_ORIGIN_RELATIONS_TEMPLATE_NAME + ", Xpath: " + REPORT_EXPRESSION_ORIGIN_RELATIONS);
			}
			
			pdfReport = buildReportToPdf(reportName, reportTemplateName, reportExpression);
			// SignResponse signResponse =
			// ReportsSignature.signReport(pdfReport, reportName);
			// pdfSignedReportByteArray = signResponse.getSignData();

			file = new DefaultStreamedContent(new ByteArrayInputStream( pdfReport), KeysRP.MIME_TYPE_PDF, reportName);
			
		} else {
			LOG.error(ErrorConstants.REPORT_DATA_ERROR);
			throw new RPReportsException(RPReportsErrorCode.REPORT_DATA_ERROR, ErrorConstants.REPORT_DATA_ERROR);
		}
		
		return file;
	}

	/**
	 * Construye el informe de acuse de recibo y lo firma.
	 * 
	 * @param report
	 *            Datos del informe.
	 * @return certReportWithName Informe firmado con su nombre.
	 * @throws Exception
	 *             Si se ha producido algún error al construir el informe o al
	 *             firmarlo.
	 */
	private LinkedHashMap<String, Object> buildAndSignCertificateReport( LinkedHashMap<String, Object> report) throws Exception {
		
		String regNumber = (String) report.get("FLD1");
		Integer idReg = (Integer) report.get("FDRID");
		String reportName = "";
		String reportTemplateName = "";
		String reportExpression = "";
		byte[] pdfReport = null;
		LinkedHashMap<String, Object> certReportWithName = new LinkedHashMap<String, Object>();
		
		buildXmlCertificateReportDocument(report, idReg);
		
		reportName = KeysRP.IR_REPORT_CERTIFICATE_NAME + regNumber + KeysRP.REPORT_CERTIFICATE_EXTENSION_FILE;
		reportTemplateName = KeysRP.IR_REPORT_CERTIFICATE_TEMPLATE_NAME;
		reportExpression = REPORT_EXPRESSION_CERT;
		
		LOG.info("Acuse de recibo a generar. Nombre informe: " + KeysRP.IR_REPORT_CERTIFICATE_NAME + "; nombre plantilla: " + KeysRP.IR_REPORT_CERTIFICATE_TEMPLATE_NAME + ", Xpath: " + REPORT_EXPRESSION_CERT);
		
		pdfReport = buildReportToPdf(reportName, reportTemplateName, reportExpression);
		
		LOG.debug("Generado el informe. Se procede a firmar");
		
		// MQE
		SignResponse signResponse = ReportsSignature.signReport(pdfReport, reportName, useCaseConf.getEntidadId());
		
		byte[] pdfSignedReportByteArray = signResponse.getSignData();
		
		LOG.debug("Documento firmado");
		
		certReportWithName.put("NAME", reportName);
		certReportWithName.put("DATA", pdfSignedReportByteArray);
		certReportWithName.put("CSV", signResponse.getDocumentId());
		
		if(null != signResponse.getFechaFirma()){
			certReportWithName.put("FECHA_FIRMA", signResponse.getFechaFirma());
		}
		
		return certReportWithName;
	}

	/**
	 * Guarda un informe de acuse de recibo en un archivo ZIP.
	 * 
	 * @param zipOutputStream
	 *            Stream del archivo zip donde guardar los informes.
	 * @param pdfReport
	 *            Informe a guardar.
	 * @param reportName
	 *            Nombre del informe a guardar.
	 * @throws Exception
	 *             Si se ha producido un error al guardar el informe.
	 */
	private void generateCertificatesZipFile(ZipOutputStream zipOutputStream, byte[] pdfReport, String reportName) throws Exception {
		
		ZipEntry zipEntry = new ZipEntry(reportName);
		
		try {		
			LOG.info("Generado archivo dentro del Zip de acuses de recibo.");
			
			zipOutputStream.putNextEntry(zipEntry);
			byte[] buffer = new byte[BYTES];
			int len = 0;
			ByteArrayInputStream baInputStream = new ByteArrayInputStream(pdfReport);
			
			while ((len = baInputStream.read(buffer)) > 0) {
				zipOutputStream.write(buffer, 0, len);
			}
			
			baInputStream.close();
			zipOutputStream.closeEntry();
			
		} catch (IOException e) {
			LOG.error(ErrorConstants.REPORT_DATA_ERROR);
			throw new RPReportsException(RPReportsErrorCode.REPORT_DATA_ERROR, ErrorConstants.REPORT_DATA_ERROR);
		}
	}

	/**
	 * Construye el informe y lo devuelve a la página para que el usuario lo
	 * guarde.
	 * 
	 * @param reportName
	 *            Nombre del informe.
	 * @param reportTemplateName
	 *            Plantilla para generar el informe.
	 * @param reportExpression
	 *            Datos internos del informe.
	 * @return file array de bytes que forman el informe.
	 * @throws Exception
	 *             Si se ha producido un error al generar el informe.
	 */
	private byte[] buildReportToPdf(String reportName, String reportTemplateName, String reportExpression) throws Exception {
		
		String xmlDocument = myXmlHandler.getDomDocument();
		
		LOG.info("Documento XML para rellenar el informe: \n" + xmlDocument);
		
		JRXmlDataSource dataSource;
		// JasperReport jasperReport = null;
		JasperPrint jasperPrint = null;
		Map<Object, Object> params = new HashMap<Object, Object>();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder;
		builder = factory.newDocumentBuilder();
		
		dataSource = new JRXmlDataSource(builder.parse(new InputSource( new StringReader(xmlDocument))), reportExpression);
		
		LOG.info("Ruta de la plantilla del informe a compilar: " + REPORT_TEMPLATE_PATH + reportTemplateName);
		/*
		 * try { jasperReport =
		 * JasperCompileManager.compileReport(REPORT_TEMPLATE_PATH +
		 * reportTemplateName); } catch (Exception jrexception) {
		 * LOG.error("ERROR DE COMPILACION " + jrexception.getMessage());
		 * LOG.error("ERROR DE COMPILACION " + jrexception.getCause()); }
		 */
		byte[] pdfReportByteArray = null;
		// MQE
		// File reportsDir = new File(
		// REPORT_TEMPLATE_PATH);
		// if (!reportsDir.exists()) {
		// throw new FileNotFoundException(
		// String.valueOf(reportsDir));
		// }
		// params.put(
		// "REPORT_FILE_RESOLVER", new SimpleFileResolver(
		// reportsDir));

		params.put( "REPORT_IMG_PATH", SigemConfigFilePathResolver.getInstance().resolveFullPath( "skinEntidad_" + MultiEntityContextHolder.getEntity(), "/SIGEM_RegistroPresencialMSSSIWeb"));

		jasperPrint = JasperFillManager.fillReport(REPORT_TEMPLATE_PATH + reportTemplateName, params, dataSource);
		pdfReportByteArray = JasperExportManager.exportReportToPdf(jasperPrint);

		LOG.debug("fichero convertido a pdf");
		return pdfReportByteArray;
	}

	/**
	 * Devuelve una lista de CCAA.
	 * 
	 * @return list Listado de CCAA.
	 * 
	 * @throws ValidationException
	 *             si ha habido algún problema.
	 */
	private ScrDirofic getDireccionOfic(Integer idOfic) {
		ScrDirofic result = null;
		
		try {
			scrDiroficDAO = (ScrDiroficDAO) appContext.getBean("scrDirOficDAO");
			result = scrDiroficDAO.getDireccionOfic(idOfic);
		
		} catch (Exception exception) {
			LOG.error(ErrorConstants.GET_DIR_MESSAGE, exception);
		}
		
		return result;
	}

	/**
	 * Obtiene el valor del parámetro searchInputRegister.
	 * 
	 * @return searchInputRegister valor del campo a obtener.
	 */
	public SearchInputRegisterBean getSearchInputRegister() {
		return searchInputRegister;
	}

	/**
	 * Guarda el valor del parámetro searchInputRegister.
	 * 
	 * @param searchInputRegister
	 *            del campo a guardar.
	 */
	public void setSearchInputRegister( SearchInputRegisterBean searchInputRegister) {		
		this.searchInputRegister = searchInputRegister;
	}

	/**
	 * Construye el informe mediante Jasper y lo envía en la respuesta en
	 * formato PDF.
	 * 
	 * @param bookId
	 *            Id. del libro.
	 * @param reportResults
	 *            Lista de registro de un certificado.
	 * @param registersList
	 *            Lista de registro de una relación.
	 * @return StreamedContent fichero que contiene el informe.
	 * @throws Exception
	 *             Si se ha producido un error generando el informe.
	 */
	public LinkedHashMap<String, Object> buildJasperReportReturnMap( Integer bookId, List<LinkedHashMap<String, Object>> reportResults) throws Exception {
		
		LOG.trace("Entrando en ReportsBo.buildJasperReport()");
		
		LinkedHashMap<String, Object> certReport = null;
		init();
		
		if (reportResults != null && reportResults.size() > 0) {
			if (reportResults.size() == 1) {
				// GENERAR UN CERTIFICADO
				certReport = buildAndSignCertificateReport(reportResults.get(0));
			}
			
		} else {
			LOG.error(ErrorConstants.REPORT_DATA_ERROR);
			throw new RPReportsException(RPReportsErrorCode.REPORT_DATA_ERROR, ErrorConstants.REPORT_DATA_ERROR);
		}
		
		return certReport;
	}

	public String getAcuseJson() {
		return acuseJson;
	}

	public void setAcuseJson(String acuseJson) {
		this.acuseJson = acuseJson;
	}
}
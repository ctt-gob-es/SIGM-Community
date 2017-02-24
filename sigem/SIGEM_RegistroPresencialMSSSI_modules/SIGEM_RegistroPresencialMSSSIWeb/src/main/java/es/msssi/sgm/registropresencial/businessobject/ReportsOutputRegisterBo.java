/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.businessobject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.Serializable;
import java.io.StringReader;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
import net.sf.jasperreports.engine.util.SimpleFileResolver;

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
import com.ieci.tecdoc.common.isicres.AxSfOut;
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

import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.DocumentoElectronicoAnexoVO;
import es.msssi.sgm.registropresencial.beans.Interesado;
import es.msssi.sgm.registropresencial.beans.OutputRegisterBean;
import es.msssi.sgm.registropresencial.beans.RowSearchOutputRegisterBean;
import es.msssi.sgm.registropresencial.beans.SearchOutputRegisterBean;
import es.msssi.sgm.registropresencial.beans.WebParameter;
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
import es.msssi.sgm.registropresencial.utils.KeysRP;
import es.msssi.sgm.registropresencial.utils.ResourceRP;
import es.msssi.sgm.registropresencial.utils.Utils;
import es.msssi.sgm.registropresencial.utils.XmlHandler;
import gnu.trove.THashMap;

/**
 * Clase que contiene los métodos para organizar los datos y generar los
 * informes para registros de salida.
 * 
 * @author jortizs
 * 
 */
public class ReportsOutputRegisterBo extends LazyDataModel<RowSearchOutputRegisterBean> implements
    IGenericBo, Serializable {
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
    private SearchOutputRegisterBean searchOutputRegister = new SearchOutputRegisterBean();
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
	appContext =
	    RegistroPresencialMSSSIWebSpringApplicationContext.getInstance()
		.getApplicationContext();
	if (REPORT_TEMPLATE_PATH == null) {
	    REPORT_TEMPLATE_PATH = (String) WebParameter.getEntryParameter("PATH_REPO") +
		(String) WebParameter.getEntryParameter("PathReports");
	}
    }

    /**
     * Constructor.
     */
    public ReportsOutputRegisterBo() {
	reportDAO = (ReportDAO) appContext.getBean("reportDAO");
	interestedBo = new InterestedBo();
	init();
    }

    /**
     * Inicializa el contexto de faces.
     */
    private void init() {
	LOG.trace("Entrando en ReportsBo.init() para iniciar el contexto de faces.");
	if (searchOutputRegister == null) {
	    LOG.info("Bean searchOutputRegister nulo");
	    searchOutputRegister = new SearchOutputRegisterBean();
	}
	if (facesContext == null) {
	    facesContext = FacesContext.getCurrentInstance();
	    Map<String, Object> map = facesContext.getExternalContext().getSessionMap();
	    useCaseConf = (UseCaseConf) map.get(Keys.J_USECASECONF);
	    book = (ScrRegstate) facesContext.getExternalContext().getSessionMap().get(
		KeysRP.J_BOOK);
	}
    }

    /**
     * Abre la query en SIGM.
     * 
     * @return el resultado de la query.
     */
    @SuppressWarnings("unchecked")
    private AxSfQuery openQuery() {
	LOG.trace("Entrando en ReportsBo.openQuery()");
	init();
	AxSfQuery axsfQuery = null;
	@SuppressWarnings("rawtypes")
	List bookIds = new ArrayList();
	bookIds.add(book.getId());
	try {
	    axsfQuery = loadAsSfQuery(
		searchOutputRegister, book.getId());
	    axsfQuery.setSelectDefWhere2("FLD7 IS NOT NULL");
	    axsfQuery.setOrderBy(" fld1 DESC");
	    LOG.info("Se intenta abrir la consulta de registros");
	    int size = FolderSession.openRegistersQuery(
		useCaseConf.getSessionID(), axsfQuery, bookIds, 0, useCaseConf.getEntidadId());
	    this.setRowCount(size);
	}
	catch (ValidationException validationException) {
	    LOG.error(
		ErrorConstants.GET_OUTPUT_REGISTER_ERROR_MESSAGE, validationException);
	}
	catch (BookException bookException) {
	    LOG.error(
		ErrorConstants.GET_OUTPUT_REGISTER_ERROR_MESSAGE, bookException);
	}
	catch (SessionException sessionException) {
	    LOG.error(
		ErrorConstants.GET_OUTPUT_REGISTER_ERROR_MESSAGE, sessionException);
	}
	return axsfQuery;
    }

    /**
     * Carga los criterios de búsqueda del formulario en el objeto que forma la
     * query.
     * 
     * @param searchOutputRegister
     *            Criterios de búsqueda.
     * @param idBook
     *            Id del libro.
     * 
     * @return axsfQuery Query completa con los criterios introducidos.
     * 
     * @throws BookException
     *             si ha habido algún problema con el libro de registro.
     */
    private AxSfQuery loadAsSfQuery(
	SearchOutputRegisterBean searchOutputRegister, Integer idBook)
	throws BookException {
	LOG.trace("Entrando en ReportsBo.loadAsSfQuery()");
	AxSfQuery axsfQuery = new AxSfQuery();
	axsfQuery.setBookId(idBook);
	axsfQuery.setPageSize(Integer.parseInt(Configurator.getInstance().getProperty(
	    ConfigurationKeys.KEY_DESKTOP_DEFAULT_PAGE_TABLE_RESULTS_SIZE)));
	try {
	    /* número de registro */
	    axsfQuery.addField(searchOutputRegister.fieldtoQuery(
		"fld1", idBook));
	    /* fecha de registro */
	    axsfQuery.addField(searchOutputRegister.fieldtoQuery(
		"fld2", idBook));
	    /* usuario */
	    axsfQuery.addField(searchOutputRegister.fieldtoQuery(
		"fld3", idBook));
	    /* Fecha de Trabajo */
	    axsfQuery.addField(searchOutputRegister.fieldtoQuery(
		"fld4", idBook));
	    /* Oficina de Registro */
	    axsfQuery.addField(searchOutputRegister.fieldtoQuery(
		"fld5", idBook));
	    /* Estado */
	    axsfQuery.addField(searchOutputRegister.fieldtoQuery(
		"fld6", idBook));
	    /* Origen */
	    axsfQuery.addField(searchOutputRegister.fieldtoQuery(
		"fld7", idBook));
	    /* Destino */
	    axsfQuery.addField(searchOutputRegister.fieldtoQuery(
		"fld8", idBook));
	    /* Remitentes */
	    axsfQuery.addField(searchOutputRegister.fieldtoQuery(
		"fld9", idBook));
	    /* Tipos de Transporte */
	    axsfQuery.addField(searchOutputRegister.fieldtoQuery(
		"fld10", idBook));
	    /* Numero Transporte */
	    axsfQuery.addField(searchOutputRegister.fieldtoQuery(
		"fld11", idBook));
	    /* Tipos de Asunto */
	    axsfQuery.addField(searchOutputRegister.fieldtoQuery(
		"fld12", idBook));
	    /* Resumen */
	    axsfQuery.addField(searchOutputRegister.fieldtoQuery(
		"fld13", idBook));

	}
	catch (IllegalArgumentException illegalArgumentException) {
	    LOG.error(
		ErrorConstants.LOAD_OUTPUT_REGISTER_QUERY_ERROR_MESSAGE, illegalArgumentException);
	    Utils.redirectToErrorPage(
		null, null, illegalArgumentException);
	}

	if (axsfQuery.getPageSize() <= 0) {
	    throw new BookException(
		BookException.ERROR_PAGE_SIZE);
	}
	return axsfQuery;
    }

    /**
     * 
     * @param bookId
     *            Id del libro.
     * @param sOutputRegister
     *            Bean con los datos del informe.
     * @return list lista de resultados para el informe.
     */
    public List<RowSearchOutputRegisterBean> getRegistersForRelationReports(
	Integer bookId, SearchOutputRegisterBean sOutputRegister) {
	LOG.trace("Entrando en ReportsBo.getRegistersForRelationReports()");
	// Se asigna el bean de la búsqueda que llega desde el action al propio
	// de la
	// clase
	searchOutputRegister = sOutputRegister;
	/*if (searchOutputRegister.getFld2ValueDesde() != null &&
	    !"".equals(searchOutputRegister.getFld2ValueDesde())) {
	    GregorianCalendar fecha = new GregorianCalendar();
	    fecha.setTime(searchOutputRegister.getFld2ValueDesde());
	    fecha.set(
		Calendar.HOUR_OF_DAY, 0);
	    fecha.set(
		Calendar.MINUTE, 0);
	    fecha.set(
		Calendar.SECOND, 0);
	    Date fromDate = fecha.getTime();
	    searchOutputRegister.setFld2ValueDesde(fromDate);
	    fecha.set(
		Calendar.HOUR_OF_DAY, HOUR_OF_DAY);
	    fecha.set(
		Calendar.MINUTE, MINUTE_SECOND);
	    fecha.set(
		Calendar.SECOND, MINUTE_SECOND);
	    Date toDate = fecha.getTime();
	    searchOutputRegister.setFld2ValueHasta(toDate);
	}
	else {*/
	if (searchOutputRegister.getFld2ValueDesde() != null &&
		    !"".equals(searchOutputRegister.getFld2ValueDesde())
		    && searchOutputRegister.getFld2ValueHasta() != null &&
			    !"".equals(searchOutputRegister.getFld2ValueHasta())) {
	    LOG.info("Fecha nula. Se buscan los registros del día actual.");
	    GregorianCalendar fecha = new GregorianCalendar();
	    fecha.set(
		Calendar.HOUR_OF_DAY, 0);
	    fecha.set(
		Calendar.MINUTE, 0);
	    fecha.set(
		Calendar.SECOND, 0);
	    Date fromDate = fecha.getTime();
	    LOG.info("Fecha desde: " +
		fecha.getTime().toString());
	    searchOutputRegister.setFld2ValueDesde(fromDate);
	    fecha.set(
		Calendar.HOUR_OF_DAY, HOUR_OF_DAY);
	    fecha.set(
		Calendar.MINUTE, MINUTE_SECOND);
	    fecha.set(
		Calendar.SECOND, MINUTE_SECOND);
	    Date toDate = fecha.getTime();
	    LOG.info("Fecha hasta: " +
		fecha.getTime().toString());
	    searchOutputRegister.setFld2ValueHasta(toDate);
	}

	// Se crea la query, siendo necesario cerrarla primero, y se inicializan
	// los
	// objetos necesarios para hacer la búsqueda
	closeQuery();
	AxSfQuery axsfQuery = null;
	axsfQuery = openQuery();
	// Recuperamos la sesión
	CacheBag cacheBag;
	try {
	    cacheBag = CacheFactory.getCacheInterface().getCacheEntry(
		useCaseConf.getSessionID());
	    THashMap bookInformation = (THashMap) cacheBag.get(axsfQuery.getBookId());
	    bookInformation.put(
		"AXSF_QUERY", axsfQuery);
	}
	catch (SessionException sessionException) {
	    LOG.error(
		ErrorConstants.LOAD_OUTPUT_REGISTER_QUERY_ERROR_MESSAGE, sessionException);
	    Utils.redirectToErrorPage(
		null, sessionException, null);
	}
	catch (TecDocException tecDocException) {
	    LOG.error(
		ErrorConstants.LOAD_OUTPUT_REGISTER_QUERY_ERROR_MESSAGE, tecDocException);
	    Utils.redirectToErrorPage(
		null, tecDocException, null);
	}

	List<RowSearchOutputRegisterBean> registersList =
	    new ArrayList<RowSearchOutputRegisterBean>();
	String orderBy = "";

	// Se establece la ordenación según el tipo de informe
	if (OPTION_TYPE_RMD == searchOutputRegister.getReportTypeValue()) {
	    orderBy = ORDERBYDES;
	    orderBy += " ASC ";
	    axsfQuery.setOrderBy(orderBy);
	}
	else if (OPTION_TYPE_RMO == searchOutputRegister.getReportTypeValue()) {
	    orderBy = ORDERBYORG;
	    orderBy += " ASC ";
	    axsfQuery.setOrderBy(orderBy);
	}
	// Se añaden las diligencias, si se ha indicado en el formulario
	if (searchOutputRegister.isIncludeProceedingValue()) {
	    includeProceedingClause = true;
	}

	try {
	    // Se obtienen los registros y se añaden en la lista final
	    AxSfQueryResults queryResults =
		FolderSession.navigateRegistersQuery(
		    useCaseConf.getSessionID(), bookId,
		    com.ieci.tecdoc.common.isicres.Keys.QUERY_ALL, useCaseConf.getLocale(),
		    useCaseConf.getEntidadId(), orderBy);
	    registersList = loadQueryResulttoList(
		queryResults, useCaseConf.getLocale());
	}
	catch (ValidationException validationException) {
	    LOG.error(
		ErrorConstants.NAVIGATE_TO_OUTPUT_REGISTERS_ROW_ERROR_MESSAGE, validationException);
	}
	catch (BookException bookException) {
	    LOG.error(
		ErrorConstants.NAVIGATE_TO_OUTPUT_REGISTERS_ROW_ERROR_MESSAGE, bookException);
	}
	catch (SessionException sessionException) {
	    LOG.error(
		ErrorConstants.NAVIGATE_TO_OUTPUT_REGISTERS_ROW_ERROR_MESSAGE, sessionException);
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
    private List<RowSearchOutputRegisterBean> loadQueryResulttoList(
	AxSfQueryResults queryResults, Locale locale) {
	LOG.trace("Entrando en SearchOutputRegisterBo.loadQueryResulttoList()");
	List<RowSearchOutputRegisterBean> data = new ArrayList<RowSearchOutputRegisterBean>();
	AxSfOut axSfOut;
	RowSearchOutputRegisterBean bean;
	for (Iterator<AxSfOut> it = queryResults.getResults().iterator(); it.hasNext();) {
	    axSfOut = (AxSfOut) it.next();
	    bean = new RowSearchOutputRegisterBean();
	    bean.setFdrid(new Integer(
		axSfOut.getAttributeValueAsString("fdrid")));
	    bean.setFld1(axSfOut.getAttributeValueAsString("fld1"));
	    bean.setFld2((Date) axSfOut.getAttributeValue("fld2"));
	    bean.setFld3(axSfOut.getAttributeValueAsString("fld3"));
	    bean.setFld5(axSfOut.getFld5());
	    bean.setFld5Name(axSfOut.getFld5Name());
	    bean.setFld6(axSfOut.getAttributeValueAsString("fld6"));
	    bean.setFld6Name(RBUtil.getInstance(
		locale).getProperty(
		"book.fld6." +
		    axSfOut.getAttributeValueAsString("fld6")));
	    bean.setFld7(axSfOut.getFld7());
	    bean.setFld7Name(axSfOut.getFld7Name());
	    bean.setFld8(axSfOut.getFld8());
	    bean.setFld8Name(axSfOut.getFld8Name());
	    bean.setFld9(axSfOut.getAttributeValueAsString("fld9"));
	    bean.setFld12(axSfOut.getFld12());
	    bean.setFld12Name((axSfOut.getFld12() != null)
		? axSfOut.getFld12().getCode() : null);
	    bean.setFld13(axSfOut.getAttributeValueAsString("fld13"));
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
	    FolderSession.closeRegistersQuery(
		useCaseConf.getSessionID(), book.getId());
	}
	catch (ValidationException validationException) {
	    LOG.error(
		ErrorConstants.CLOSE_OUTPUT_REGISTER_QUERY_ERROR_MESSAGE, validationException);
	    Utils.redirectToErrorPage(
		null, validationException, null);
	}
	catch (BookException bookException) {
	    LOG.error(
		ErrorConstants.CLOSE_OUTPUT_REGISTER_QUERY_ERROR_MESSAGE, bookException);
	    Utils.redirectToErrorPage(
		null, bookException, null);
	}
	catch (SessionException sessionException) {
	    LOG.error(
		ErrorConstants.CLOSE_OUTPUT_REGISTER_QUERY_ERROR_MESSAGE, sessionException);
	    Utils.redirectToErrorPage(
		null, sessionException, null);
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
    // public List<RowSearchOutputRegisterBean> removeRegistersWithoutOrganisms(
    // List<RowSearchOutputRegisterBean> relationsList) {
    // List<RowSearchOutputRegisterBean> finalRelationsList = null;
    // int deletedRegistersCounter = 0;
    // int initialRegistersListLength = 0;
    // if (relationsList != null &&
    // relationsList.size() > 0) {
    // finalRelationsList = relationsList;
    // Iterator<RowSearchOutputRegisterBean> registersListIterator =
    // finalRelationsList.iterator();
    // initialRegistersListLength = finalRelationsList.size();
    // while (registersListIterator.hasNext()) {
    // final RowSearchOutputRegisterBean rowSIRBean =
    // (RowSearchOutputRegisterBean) registersListIterator.next();
    // if (OPTION_TYPE_RMD == searchOutputRegister.getReportTypeValue()) {
    // if (rowSIRBean.getFld8() == null ||
    // (rowSIRBean.getFld8() != null && rowSIRBean.getFld7() == null)) {
    // registersListIterator.remove();
    // deletedRegistersCounter++;
    // }
    // }
    // else if (OPTION_TYPE_RMO == searchOutputRegister.getReportTypeValue()) {
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
     * Construye el documento XML correspondiente a los datos de los informes.
     * 
     * @param reportResults
     *            Lista de datos de certificados obtenidos, un informe por
     *            registro.
     * @param idReg
     *            id de registro.
     * */
    private void buildXmlCertificateReportDocument(
	HashMap<String, Object> reportResults, Integer idReg) {
	LOG.trace("Entrando en ReportsBo.buildXmlCertificateReportDocument()");
	myXmlHandler = new XmlHandler(
	    "INFORME_RP");
	myXmlHandler.addBeginXmlHandler("REGISTRO");
	Map<String, Object> map = reportResults;
	for (Entry<String, Object> entry : map.entrySet()) {
	    if (entry.getValue() != null && !"FLD9".equals(entry.getKey())) {
		if (entry.getValue() instanceof ScrOfic) {
		    ScrOfic scrOfic = (ScrOfic) entry.getValue();
		    myXmlHandler.addNode(
			String.valueOf(entry.getKey()), scrOfic.getId());
		}
		else if (entry.getValue() instanceof ScrOrg) {
		    ScrOrg scrOrg = (ScrOrg) entry.getValue();
		    myXmlHandler.addNode(
			String.valueOf(entry.getKey()), scrOrg.getId());
		}
		else if (entry.getValue() instanceof ScrCa) {
		    ScrCa scrCa = (ScrCa) entry.getValue();
		    myXmlHandler.addNode(
			String.valueOf(entry.getKey()), scrCa.getId());
		}
		else if (entry.getValue() instanceof Timestamp) {
		    myXmlHandler.addNode(
			String.valueOf(entry.getKey()),
			Utils.formatTimeStampInString((Timestamp) entry.getValue()));
		}
		else {
		    if (!"FLD9".equals(entry.getKey())) {
			myXmlHandler.addNode(
			    String.valueOf(entry.getKey()), entry.getValue());
		    }
		}
	    }
	    else if ("FLD9".equals(entry.getKey())) {
		myXmlHandler.addNode(
		    String.valueOf(entry.getKey()),
		    interestedBo.fillSenderFieldFromSenderListToRelationsReport(idReg,book.getId(),useCaseConf));
	    }
	}
	List<LinkedHashMap<String, Object>> attacheds = getRegisterAttachedDocuments(book.getId(), idReg );
	if (attacheds != null){
	    myXmlHandler.addBeginXmlHandler("DOCUMENTOS");
	    for (LinkedHashMap<String, Object>attached : attacheds){
		 myXmlHandler.addBeginXmlHandler("DOCUMENTO");
		 myXmlHandler.addNode("NOMBRE", attached.get("NAME"));
		 if ( attached.get("SIZE") != null){
		     myXmlHandler.addNode("TAMANIO", attached.get("SIZE"));
		 }
		 if ( attached.get("HASH") != null){
		     myXmlHandler.addNode("HASH", attached.get("HASH"));
		 }
		 if ( attached.get("VALIDTYPE") != null){
		     myXmlHandler.addNode("VALIDEZ", ResourceRP.getInstance(
				useCaseConf.getLocale()).getProperty(
				KeysRP.I18N_VALIDATEDOCUMENT+"."+attached.get("VALIDTYPE")));
		 }
		 if ( attached.get("DOCUMENTTYPE") != null){
		     myXmlHandler.addNode("TIPO", ResourceRP.getInstance(
				useCaseConf.getLocale()).getProperty(
				KeysRP.I18N_TYPEDOCUMENT+"."+attached.get("DOCUMENTTYPE")));
		 }
		 if ( attached.get("COMMENT") != null){
		     myXmlHandler.addNode("COMENTARIOS", attached.get("COMMENT"));
		 }
		 myXmlHandler.addEndXmlHandler("DOCUMENTO");
	    }
	    myXmlHandler.addEndXmlHandler("DOCUMENTOS");
	}
	myXmlHandler.addNode(
	    "PROCEEDING", KeysRP.REPORT_PROCEEEDING_TEXT);
	myXmlHandler.addEndXmlHandler("REGISTRO");
	LOG.warn("XML Constuido: " +
	    myXmlHandler.toString());
    }
    
    /**
     * Obtiene los documentos asociados a un registro.
     */
    public List<LinkedHashMap<String, Object>> getRegisterAttachedDocuments(Integer idBook, Integer idReg ) {
	List<Axdoch>listDocuments = new ArrayList<Axdoch>();
	RegisterDocumentsBo registerDocumentsBo = null;
	List<LinkedHashMap<String, Object>> documents = new ArrayList<LinkedHashMap<String, Object>>();
	LinkedHashMap<String, Object> document = null;
	if (registerDocumentsBo == null) {
	    registerDocumentsBo = new RegisterDocumentsBo();
	}
	try {
	    LOG.info("Cargando documentos asociados al registro " + idReg);
	    listDocuments =
		    registerDocumentsBo.getDocumentsBasicInfo(useCaseConf, idBook,
			    idReg, false, new Integer(1));
	    
	    for (Axdoch doc:listDocuments){
		for (Axpageh page: doc.getPages()){
		    if (page.getPageSignedId()== null || page.getPageSignedId().equals(page.getId())){
			document = new LinkedHashMap<String, Object>();
			document.put("NAME", page.getName());
			document.put("SIZE", page.getFileSizeKB());
			document.put("VALIDTYPE", page.getValidityType());
			document.put("DOCUMENTTYPE", page.getDocumentType());
			document.put("COMMENT", page.getComments());
			document.put("HASH", (page.getHash()!= null)?new String(page.getHash()):null);
			documents.add(document);
		    }
		}
	    }
	    if (documents.size()== 0){
		documents = null;
	    }
	}
	catch (RPRegisterException rPRegisterException) {
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
     *            true si es un informe de relación por origen.
     */
    private void buildXmlRelationsReportDocument(
	List<RowSearchOutputRegisterBean> registersList, boolean isOriginReport) {
	LOG.trace("Entrando en ReportsBo.buildXmlRelationsReportDocument()");
	myXmlHandler = new XmlHandler(
	    "INFORME_RP");
	if (isOriginReport) {
	    LOG.info("Es un informe de relaciones por origen. "
		+ "Se rellena el XML para el informe teniendo en cuenta esto");
	    fillFldForOriginNodes(registersList);
	}
	else {
	    LOG.info("Es un informe de relaciones por destino. "
		+ "Se rellena el XML para el informe teniendo en cuenta esto");
	    fillFldForDestinationNodes(registersList);
	}
	LOG.warn("XML Constuido: " +
	    myXmlHandler.toString());
    }

    /**
     * Rellenar los nodos del xml del informe con los datos de origen.
     * 
     * @param registersList
     *            Lista de Ids. de registros.
     */
    private void fillFldForOriginNodes(
	List<RowSearchOutputRegisterBean> registersList) {
	myXmlHandler.addBeginXmlHandler("FLD8");
	myXmlHandler.addNode(
	    "FLD8_TEXT", registersList.get(
		0).getFld8Name() != null
		? registersList.get(
		    0).getFld8Name() : "");
	myXmlHandler.addNode(
	    "FLD8_FATHERNAME", registersList.get(
		0).getFld8() != null
		? registersList.get(
		    0).getFld8().getNameOrgFather() : "");
	myXmlHandler.addNode(
	    "FLD8_ADDRESS", registersList.get(
		0).getFld8() != null
		? reportDAO.getAddressOrg(registersList.get(
		    0).getFld8().getId()) : "");
	// Se añade un nodo FLD5 con todos los datos. Contendrá el nombre de la
	// oficina,
	// diligencias y órganos.
	myXmlHandler.addNode(
	    "FLD5_TEXT", registersList.get(
		0).getFld5Name());
	ScrDirofic direccion = getDireccionOfic( registersList.get(0).getFld5().getId());
	if (direccion != null){
    		myXmlHandler.addNode(
    			"FLD5_ADDRESS",
    				reportDAO.formatAddressOfic(direccion.getAddress(),
    				direccion.getCity(),direccion.getZip(),
    				direccion.getCountry(), direccion.getTelephone(),
    				direccion.getFax(), direccion.getEmail()));
	}
	// Se añade el texto de las diligencias, que podrá ser modificado por
	// cualquier otro texto
	if (includeProceedingClause) {
	    LOG.info("Se añaden las diligencias");
	    myXmlHandler.addNode(
		"PROCEEDING", KeysRP.REPORT_PROCEEEDING_TEXT);
	}
	int numOrganoOrigen = registersList.get(
	    0).getFld8() != null
	    ? ((ScrOrg) registersList.get(
		0).getFld8()).getId().intValue() : 0;
	int finalRegistersListLength = registersList.size();
	int registerCounter = 0;
	LOG.warn("finalRegistersListLength es: " +
	    finalRegistersListLength);
	int numOrganoOrigenNew = 0;
	// Se añaden los datos de los registros para cada oficina
	for (int i = 0; i < finalRegistersListLength; i++) {
	    numOrganoOrigenNew = registersList.get(
		i).getFld8() != null
		? registersList.get(
		    i).getFld8().getId() : 0;
	    if (numOrganoOrigen == numOrganoOrigenNew) {
		registerCounter++;
		fillRegisterNode(
		    registersList.get(i), true);
	    }
	    else {
		numOrganoOrigen = numOrganoOrigenNew;
		myXmlHandler.addNode(
		    "TOTAL_REGISTROS", registerCounter);
		myXmlHandler.addEndXmlHandler("FLD8");
		myXmlHandler.addBeginXmlHandler("FLD8");
		myXmlHandler.addNode(
		    "FLD8_TEXT", registersList.get(
			i).getFld8Name() != null
			? registersList.get(
			    i).getFld8Name() : "");
		myXmlHandler.addNode(
		    "FLD8_FATHERNAME", registersList.get(
			i).getFld8() != null
			? registersList.get(
			    i).getFld8().getNameOrgFather() : "");
		myXmlHandler.addNode(
		    "FLD8_ADDRESS", registersList.get(
			i).getFld8() != null
			? reportDAO.getAddressOrg(registersList.get(
			    i).getFld8().getId()) : "");
		// Se añade otro nodo FLD5 con todos los datos. Contendrá el
		// nombre de la
		// oficina, diligencias y órganos.
		myXmlHandler.addNode(
		    "FLD5_TEXT", registersList.get(
			i).getFld5Name());
		ScrDirofic direccion1 = getDireccionOfic( registersList.get(0).getFld5().getId());
		if (direccion1 != null){
	    		myXmlHandler.addNode(
	    			"FLD5_ADDRESS",
	    				reportDAO.formatAddressOfic(direccion1.getAddress(),
	    				direccion1.getCity(),direccion1.getZip(),
	    				direccion1.getCountry(), direccion1.getTelephone(),
	    				direccion1.getFax(), direccion1.getEmail()));
		}
		// Se añade el texto de las diligencias, que podrá ser
		// modificado por
		// cualquier otro texto
		if (includeProceedingClause) {
		    LOG.info("Se añaden las diligencias");
		    myXmlHandler.addNode(
			"PROCEEDING", KeysRP.REPORT_PROCEEEDING_TEXT);
		}
		fillRegisterNode(
		    registersList.get(i), true);
		registerCounter = 1;
	    }
	}
	myXmlHandler.addNode(
	    "TOTAL_REGISTROS", registerCounter);
	myXmlHandler.addEndXmlHandler("FLD8");
    }

    /**
     * Rellenar los nodos del xml del informe con los datos de destino.
     * 
     * @param registersList
     *            Lista de Ids. de registros.
     */
    private void fillFldForDestinationNodes(
	List<RowSearchOutputRegisterBean> registersList) {
	myXmlHandler.addBeginXmlHandler("FLD7");
	myXmlHandler.addNode(
	    "FLD7_TEXT", registersList.get(
		0).getFld7Name() != null
		? registersList.get(
		    0).getFld7Name() : "");
	myXmlHandler.addNode(
	    "FLD7_FATHERNAME", registersList.get(
		0).getFld7() != null
		? registersList.get(
		    0).getFld7().getNameOrgFather() : "");
	myXmlHandler.addNode(
	    "FLD7_ADDRESS", registersList.get(
		0).getFld7() != null
		? reportDAO.getAddressOrg(registersList.get(
		    0).getFld7().getId()) : "");
	// Se añade un nodo FLD5 con todos los datos. Contendrá el nombre de la
	// oficina,
	// diligencias y órganos.
	myXmlHandler.addNode(
	    "FLD5_TEXT", registersList.get(
		0).getFld5Name());
	ScrDirofic direccion = getDireccionOfic( registersList.get(0).getFld5().getId());
	if (direccion != null){
    		myXmlHandler.addNode(
    			"FLD5_ADDRESS",
    				reportDAO.formatAddressOfic(direccion.getAddress(),
    				direccion.getCity(),direccion.getZip(),
    				direccion.getCountry(), direccion.getTelephone(),
    				direccion.getFax(), direccion.getEmail()));
	}
	// Se añade el texto de las diligencias, que podrá ser modificado por
	// cualquier otro texto
	if (includeProceedingClause) {
	    LOG.info("Se añaden las diligencias");
	    myXmlHandler.addNode(
		"PROCEEDING", KeysRP.REPORT_PROCEEEDING_TEXT);
	}
	int numOrganoOrigen = registersList.get(
	    0).getFld7() != null
	    ? ((ScrOrg) registersList.get(
		0).getFld7()).getId().intValue() : 0;
	int finalRegistersListLength = registersList.size();
	int registerCounter = 0;
	int numOrganoOrigenNew = 0;
	// Se añaden los datos de los registros para cada oficina
	for (int i = 0; i < finalRegistersListLength; i++) {
	    numOrganoOrigenNew = registersList.get(
		i).getFld7() != null
		? registersList.get(
		    i).getFld7().getId() : 0;
	    if (numOrganoOrigen == numOrganoOrigenNew) {
		registerCounter++;
		fillRegisterNode(
		    registersList.get(i), false);
	    }
	    else {
		numOrganoOrigen = numOrganoOrigenNew;
		myXmlHandler.addNode(
		    "TOTAL_REGISTROS", registerCounter);
		myXmlHandler.addEndXmlHandler("FLD7");
		myXmlHandler.addBeginXmlHandler("FLD7");
		myXmlHandler.addNode(
		    "FLD7_TEXT", registersList.get(
			i).getFld7Name() != null
			? registersList.get(
			    i).getFld7Name() : "");
		myXmlHandler.addNode(
		    "FLD7_FATHERNAME", registersList.get(
			i).getFld7() != null
			? registersList.get(
			    i).getFld7().getNameOrgFather() : "");
		myXmlHandler.addNode(
		    "FLD7_ADDRESS", registersList.get(
			i).getFld7() != null
			? reportDAO.getAddressOrg(registersList.get(
			    i).getFld7().getId()) : "");
		// Se añade otro nodo FLD5 con todos los datos. Contendrá el
		// nombre de la
		// oficina, diligencias y órganos.
		myXmlHandler.addNode(
		    "FLD5_TEXT", registersList.get(
			i).getFld5Name());
		ScrDirofic direccion1 = getDireccionOfic( registersList.get(0).getFld5().getId());
		if (direccion1 != null){
	    		myXmlHandler.addNode(
	    			"FLD5_ADDRESS",
	    				reportDAO.formatAddressOfic(direccion1.getAddress(),
	    				direccion1.getCity(),direccion1.getZip(),
	    				direccion1.getCountry(), direccion1.getTelephone(),
	    				direccion1.getFax(), direccion1.getEmail()));
		}
		// Se añade el texto de las diligencias, que podrá ser
		// modificado por
		// cualquier otro texto
		if (includeProceedingClause) {
		    LOG.info("Se añaden las diligencias");
		    myXmlHandler.addNode(
			"PROCEEDING", KeysRP.REPORT_PROCEEEDING_TEXT);
		}
		fillRegisterNode(
		    registersList.get(i), true);
		registerCounter = 1;
	    }
	}
	myXmlHandler.addNode(
	    "TOTAL_REGISTROS", registerCounter);
	myXmlHandler.addEndXmlHandler("FLD7");
    }

    /**
     * Rellena los nodos REGISTRO de cada oficina, para los informes de
     * relaciones.
     * 
     * @param rowSORBean
     *            Bean con los datos a rellenar.
     * @param isOriginReport
     *            true si es un informe de origen.
     */
    private void fillRegisterNode(
	RowSearchOutputRegisterBean rowSORBean, boolean isOriginReport) {
	myXmlHandler.addBeginXmlHandler("REGISTRO");
	myXmlHandler.addNode(
	    "FDRID", rowSORBean.getFdrid() != null
		? rowSORBean.getFdrid() : "");
	myXmlHandler.addNode(
	    "FLD1", rowSORBean.getFld1() != null
		? rowSORBean.getFld1() : "");
	myXmlHandler.addNode(
	    "FLD2", rowSORBean.getFld2() != null
		? Utils.formatDateInString((Date) rowSORBean.getFld2()) : "");
	myXmlHandler.addNode(
	    "FLD3", rowSORBean.getFld3() != null
		? rowSORBean.getFld3() : "");
	myXmlHandler.addNode(
	    "FLD4", rowSORBean.getFld4() != null
		? Utils.formatDateInString((Date) rowSORBean.getFld4()) : "");
	myXmlHandler.addNode(
	    "FLD6", rowSORBean.getFld6() != null
		? String.valueOf(rowSORBean.getFld6()) : "");
	if (isOriginReport) {
	    myXmlHandler.addNode(
		"FLD7", rowSORBean.getFld7() != null
		    ? String.valueOf(((ScrOrg) rowSORBean.getFld7()).getId().intValue()) : "");
	}
	else {
	    myXmlHandler.addNode(
		"FLD8", rowSORBean.getFld8() != null
		    ? String.valueOf(((ScrOrg) rowSORBean.getFld8()).getId().intValue()) : "");
	}
	myXmlHandler.addNode(
	    "FLD9",  interestedBo.fillSenderFieldFromSenderListToRelationsReport(rowSORBean.getFdrid(),book.getId(),useCaseConf));
	myXmlHandler.addNode(
	    "FLD10", rowSORBean.getFld10() != null
		? rowSORBean.getFld10() : "");
	myXmlHandler.addNode(
	    "FLD11", rowSORBean.getFld11() != null
		? String.valueOf(rowSORBean.getFld11()) : "");
	myXmlHandler.addNode(
	    "FLD12", rowSORBean.getFld12() != null
		? String.valueOf(((ScrCa) rowSORBean.getFld12()).getId().intValue()) : "");
	myXmlHandler.addNode(
	    "FLD13", rowSORBean.getFld13() != null
		? rowSORBean.getFld13() : "");
	myXmlHandler.addNode(
	    "FLD14", rowSORBean.getFld14() != null
		? rowSORBean.getFld14() : "");
	myXmlHandler.addNode(
	    "FLD7_TEXT", rowSORBean.getFld7Name() != null
		? rowSORBean.getFld7Name() : "");
	myXmlHandler.addNode(
	    "FLD8_TEXT", rowSORBean.getFld8Name() != null
		? rowSORBean.getFld8Name() : "");
	myXmlHandler.addEndXmlHandler("REGISTRO");
    }

    /**
     * Construye el informe mediante Jasper y lo envía en la respuesta en
     * formato PDF.
     * 
     * @param bookId
     *            Id. del libro.
     * @param reportResults
     *            resultado del report.
     * @param registersList
     *            Lista de Ids. de registros.
     * @return file
     * 		el informe.
     * @throws Exception
     *             Si se ha producido un error generando el informe.
     */
    public StreamedContent buildJasperReport(
	    ScrRegstate book, List<LinkedHashMap<String, Object>> reportResults,
	List<RowSearchOutputRegisterBean> registersList)
	throws Exception {
	LOG.trace("Entrando en ReportsBo.buildJasperReport()");
	byte[] pdfSignedReportByteArray = null;
	byte[] pdfReport = null;
	String reportName = "";
	String reportTemplateName = "";
	String reportExpression = "";
	LinkedHashMap<String, Object> certReport = null;
	StreamedContent file = null;
	OutputRegisterBo outputRegisterBo = null;
	OutputRegisterBean outputRegisterBean = null;
	AxSf registerData = null;
	RegisterDocumentsBo registerDocumentsBo = null;

	List<StreamedContent> filesToUpload = new ArrayList<StreamedContent>();
	StreamedContent content = null;
	init();
	Integer numAcuses = 0;
	if (reportResults != null &&
	    reportResults.size() > 0) {
	    if (reportResults.size() == 1) {
		certReport = buildAndSignCertificateReport(reportResults.get(0));
		pdfSignedReportByteArray = (byte[]) certReport.get("DATA");
		reportName = (String) certReport.get("NAME");
		 outputRegisterBo = new OutputRegisterBo();
		 outputRegisterBean =
			 outputRegisterBo.loadOutputRegisterBean(useCaseConf,book,
				    (Integer)reportResults.get(0).get("FDRID"));
		    
		registerData = Utils.mappingOutputRegisterToAxSF( outputRegisterBean);
		registerDocumentsBo = new RegisterDocumentsBo();

		numAcuses =
		registerDocumentsBo.countPageReport(useCaseConf, book.getIdocarchhdr().getId(),
					    outputRegisterBean.getFdrid());
		String nombre = null;
		if (numAcuses > 0){
		    nombre = reportName;
		    numAcuses = numAcuses + 1; //es el siguiente  
		    nombre = nombre.substring(0, nombre.indexOf("."))+"_"+numAcuses + ".pdf";
		    content = new DefaultStreamedContent(
				new ByteArrayInputStream(pdfSignedReportByteArray), KeysRP.MIME_TYPE_PDF, nombre);
		}else {
		    nombre = reportName;
		    numAcuses = numAcuses + 1; //es el siguiente  
		    content = new DefaultStreamedContent(
				new ByteArrayInputStream(pdfSignedReportByteArray), KeysRP.MIME_TYPE_PDF,nombre);
		}
		filesToUpload.add(content);
		DocumentoElectronicoAnexoVO documentSaved =
				registerDocumentsBo.saveStreamedContentDocuments(
					useCaseConf.getSessionID(), book.getIdocarchhdr().getId(),
					(Integer)reportResults.get(0).get("FDRID"), filesToUpload, registerData,
					useCaseConf.getLocale(), useCaseConf.getEntidadId());

		if (documentSaved != null){
		    /*  Axdoch  acuseDoc = null;
		    acuseDoc = registerDocumentsBo.getLastReport(useCaseConf, book.getIdocarchhdr().getId(),
    				    outputRegisterBean.getFdrid());
    		  
    		    if ( documentSaved != null){*/
		    acuseJson =  "[{\"iddoc\":"+documentSaved.getId().getId()+",\"idpag\":"+documentSaved.getId().getIdPagina()+",\"csv\":\""+(String) certReport.get("CSV")+"\"}]";
    			OutputRegisterBean outputBeanUpdate = new OutputRegisterBean();
    			outputBeanUpdate.setFdrid( outputRegisterBean.getFdrid());
    			outputBeanUpdate.setFld1(outputRegisterBean.getFld1());
    			outputBeanUpdate.setFld1004(acuseJson);
    				if (outputRegisterBean.getInteresados() != null && outputRegisterBean.getInteresados().size()>0) {
        				List<Interesado> interesados = new ArrayList<Interesado>();
        				Interesado interesado = new Interesado();
        				interesado.setNombre(outputRegisterBean.getInteresados().get(0).getNombre()
        					+" "+outputRegisterBean.getInteresados().get(0).getPapellido());
        				interesados.add(interesado);
        				outputBeanUpdate.setInteresados(interesados );
    				} else {
        			    if (outputRegisterBean.getFld9() != null && !"".equals(outputRegisterBean.getFld9().trim())){
        				List<Interesado> interesados = new ArrayList<Interesado>();
        				Interesado interesado = new Interesado();
        				interesado.setNombre(outputRegisterBean.getFld9());
        				interesados.add(interesado);
        				outputBeanUpdate.setInteresados(interesados );
        			    }
    				}
    			    outputRegisterBo.updateOnlyFolder(useCaseConf, book.getIdocarchhdr().getId(),
    				    outputRegisterBean.getFdrid(),outputBeanUpdate);
    			 registerDocumentsBo.updateFlag(useCaseConf, book.getIdocarchhdr().getId(),
    				outputRegisterBean.getFdrid(),Long.valueOf(documentSaved.getId().getIdPagina()).intValue(), numAcuses);
    			}
		//}
		    
		file = new DefaultStreamedContent(
				new ByteArrayInputStream(pdfSignedReportByteArray), KeysRP.MIME_TYPE_PDF,nombre);

	    }
	    else {
		ByteArrayOutputStream baOutputStream = new ByteArrayOutputStream();
		ZipOutputStream zipOutputStream = new ZipOutputStream(
		    baOutputStream);
		String zipName = KeysRP.OR_REPORT_CERTIFICATE_NAME +
		    KeysRP.REPORT_CERTIFICATE_EXTENSION_ZIP;
		for (int i = 0; i < reportResults.size(); i++) {
		    certReport = buildAndSignCertificateReport(reportResults.get(i));
		    pdfSignedReportByteArray = (byte[]) certReport.get("DATA");
		    reportName = (String) certReport.get("NAME");
		    generateCertificatesZipFile(
			zipOutputStream, pdfSignedReportByteArray, reportName);
		}
		zipOutputStream.close();
		LOG.info("Generado Zip de acuses de recibo.");
		file = new DefaultStreamedContent(
		    new ByteArrayInputStream(
			baOutputStream.toByteArray()), KeysRP.MIME_TYPE_ZIP, zipName);
	    }
	}
	else if (registersList != null &&
	    registersList.size() > 0) {
	    if (OPTION_TYPE_RMD == searchOutputRegister.getReportTypeValue()) {
		buildXmlRelationsReportDocument(
		    registersList, false);
		reportName = KeysRP.OR_REPORT_DESTINATION_RELATIONS_NAME;
		reportTemplateName = KeysRP.OR_REPORT_DESTINATION_RELATIONS_TEMPLATE_NAME;
		reportExpression = REPORT_EXPRESSION_DESTINATION_RELATIONS;
		LOG.info("Vienen datos referentes a un informe de relaciones por destino. " +
		    "Nombre informe: " + KeysRP.OR_REPORT_DESTINATION_RELATIONS_NAME +
		    "; nombre plantilla: " + KeysRP.OR_REPORT_DESTINATION_RELATIONS_TEMPLATE_NAME +
		    ", Xpath: " + REPORT_EXPRESSION_DESTINATION_RELATIONS);
	    }
	    else if (OPTION_TYPE_RMO == searchOutputRegister.getReportTypeValue()) {
		buildXmlRelationsReportDocument(
		    registersList, true);
		reportName = KeysRP.OR_REPORT_ORIGIN_RELATIONS_NAME;
		reportTemplateName = KeysRP.OR_REPORT_ORIGIN_RELATIONS_TEMPLATE_NAME;
		reportExpression = REPORT_EXPRESSION_ORIGIN_RELATIONS;
		LOG.info("Vienen datos referentes a un informe de relaciones por origen. " +
		    "Nombre informe: " + KeysRP.OR_REPORT_ORIGIN_RELATIONS_NAME +
		    "; nombre plantilla: " + KeysRP.OR_REPORT_ORIGIN_RELATIONS_TEMPLATE_NAME +
		    ", Xpath: " + REPORT_EXPRESSION_ORIGIN_RELATIONS);
	    }
	    pdfReport = buildReportToPdf(
		reportName, reportTemplateName, reportExpression);
	    //SignResponse signResponse = ReportsSignature.signReport(pdfReport, reportName);
	    //pdfSignedReportByteArray = signResponse.getSignData();
	    file = new DefaultStreamedContent(
		new ByteArrayInputStream(pdfReport), KeysRP.MIME_TYPE_PDF, reportName);
	}
	else {
	    LOG.error(ErrorConstants.REPORT_DATA_ERROR);
	    throw new RPReportsException(
		RPReportsErrorCode.REPORT_DATA_ERROR, ErrorConstants.REPORT_DATA_ERROR);
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
    private LinkedHashMap<String, Object> buildAndSignCertificateReport(
	LinkedHashMap<String, Object> report)
	throws Exception {
	String regNumber = (String) report.get("FLD1");
	Integer idReg = (Integer) report.get("FDRID");
	String reportName = "";
	String reportTemplateName = "";
	String reportExpression = "";
	byte[] pdfReport = null;
	LinkedHashMap<String, Object> certReportWithName = new LinkedHashMap<String, Object>();

	buildXmlCertificateReportDocument(
	    report, idReg);
	reportName = KeysRP.OR_REPORT_CERTIFICATE_NAME +
	    regNumber + KeysRP.REPORT_CERTIFICATE_EXTENSION_FILE;
	reportTemplateName = KeysRP.OR_REPORT_CERTIFICATE_TEMPLATE_NAME;
	reportExpression = REPORT_EXPRESSION_CERT;
	LOG.info("Acuse de recibo a generar. Nombre informe: " +
	    KeysRP.OR_REPORT_CERTIFICATE_NAME + "; nombre plantilla: " +
	    KeysRP.OR_REPORT_CERTIFICATE_TEMPLATE_NAME + ", Xpath: " + REPORT_EXPRESSION_CERT);
	pdfReport = buildReportToPdf(
	    reportName, reportTemplateName, reportExpression);
	SignResponse signResponse = ReportsSignature.signReport(pdfReport,reportName);
	byte[] pdfSignedReportByteArray = signResponse.getSignData();
	certReportWithName.put(
	    "NAME", reportName);
	certReportWithName.put(
	    "DATA", pdfSignedReportByteArray);
	certReportWithName.put(
		    "CSV", signResponse.getDocumentId());
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
    private void generateCertificatesZipFile(
	ZipOutputStream zipOutputStream, byte[] pdfReport, String reportName)
	throws Exception {
	ZipEntry zipEntry = new ZipEntry(
	    reportName);
	try {
	    LOG.info("Generado archivo dentro del Zip de acuses de recibo.");
	    zipOutputStream.putNextEntry(zipEntry);
	    byte[] buffer = new byte[BYTES];
	    int len = 0;
	    ByteArrayInputStream baInputStream = new ByteArrayInputStream(
		pdfReport);
	    while ((len = baInputStream.read(buffer)) > 0) {
		zipOutputStream.write(
		    buffer, 0, len);
	    }
	    baInputStream.close();
	    zipOutputStream.closeEntry();
	}
	catch (IOException e) {
	    LOG.error(ErrorConstants.REPORT_DATA_ERROR);
	    throw new RPReportsException(
		RPReportsErrorCode.REPORT_DATA_ERROR, ErrorConstants.REPORT_DATA_ERROR);
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
     * @return array bytes el informe en pdf.
     * @throws Exception
     *             Si se ha producido un error al generar el informe.
     */
    private byte[] buildReportToPdf(
	String reportName, String reportTemplateName, String reportExpression)
	throws Exception {
	String xmlDocument = myXmlHandler.getDomDocument();
	LOG.info("Documento XML para rellenar el informe: \n" +
	    xmlDocument);
	JRXmlDataSource dataSource;
	//JasperReport jasperReport;
	JasperPrint jasperPrint;
	Map<Object, Object> params = new HashMap<Object, Object>();
	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	DocumentBuilder builder;
	builder = factory.newDocumentBuilder();
	dataSource = new JRXmlDataSource(
	    builder.parse(new InputSource(
		new StringReader(
		    xmlDocument))), reportExpression);
	LOG.info("Ruta de la plantilla del informe a compilar: " +
	    REPORT_TEMPLATE_PATH + reportTemplateName);
	//jasperReport = JasperCompileManager.compileReport(REPORT_TEMPLATE_PATH +
	//    reportTemplateName);
	File reportsDir = new File(
	    REPORT_TEMPLATE_PATH);
	if (!reportsDir.exists()) {
	    throw new FileNotFoundException(
		String.valueOf(reportsDir));
	}
	params.put(
	    "REPORT_FILE_RESOLVER", new SimpleFileResolver(
		reportsDir));
	jasperPrint = JasperFillManager.fillReport(
	    REPORT_TEMPLATE_PATH + reportTemplateName, params, dataSource);
	// JasperExportManager.exportReportToPdfFile(jasperPrint,
	// REPORT_TEMPLATE_PATH +
	// REPORT_ORIGIN_RELATIONS_NAME);
	byte[] pdfReportByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
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
    private ScrDirofic getDireccionOfic(Integer idOfic){
	ScrDirofic result = null;
	try {
	    scrDiroficDAO = (ScrDiroficDAO) appContext.getBean("scrDirOficDAO");
	    result = scrDiroficDAO.getDireccionOfic(idOfic);
	}
	catch (Exception exception) {
	    LOG.error(
		ErrorConstants.GET_DIR_MESSAGE, exception);
	}
	return result;
    }
    
    /**
     * Obtiene el valor del parámetro searchOutputRegister.
     * 
     * @return searchOutputRegister valor del campo a obtener.
     */
    public SearchOutputRegisterBean getsearchOutputRegister() {
	return searchOutputRegister;
    }

    /**
     * Guarda el valor del parámetro searchOutputRegister.
     * 
     * @param searchOutputRegister
     *            del campo a guardar.
     */
    public void setsearchOutputRegister(
	SearchOutputRegisterBean searchOutputRegister) {
	this.searchOutputRegister = searchOutputRegister;
    }

    public LinkedHashMap<String, Object> buildJasperReportReturnMap(Integer id,
	    List<LinkedHashMap<String, Object>> reportResults) 
		    throws Exception  {
	LOG.trace("Entrando en ReportsBo.buildJasperReport()");
	LinkedHashMap<String, Object> certReport = null;
	init();
	if (reportResults != null &&
	    reportResults.size() > 0) {
	    if (reportResults.size() == 1) {
		// GENERAR UN CERTIFICADO
		certReport = buildAndSignCertificateReport(reportResults.get(0));
	    }
	}
	else {
	    LOG.error(ErrorConstants.REPORT_DATA_ERROR);
	    throw new RPReportsException(
		RPReportsErrorCode.REPORT_DATA_ERROR, ErrorConstants.REPORT_DATA_ERROR);
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
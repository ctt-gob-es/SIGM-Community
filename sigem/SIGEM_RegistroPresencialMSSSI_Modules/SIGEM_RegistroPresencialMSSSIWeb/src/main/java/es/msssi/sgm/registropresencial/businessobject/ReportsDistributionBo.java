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

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.faces.validator.ValidatorException;
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

import com.ieci.tecdoc.common.exception.DistributionException;
import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.ValidationException;
import com.ieci.tecdoc.common.isicres.AxSfIn;
import com.ieci.tecdoc.common.isicres.AxSfOut;
import com.ieci.tecdoc.common.keys.ConfigurationKeys;
import com.ieci.tecdoc.common.utils.Configurator;
import com.ieci.tecdoc.isicres.desktopweb.Keys;
import com.ieci.tecdoc.isicres.usecase.UseCaseConf;
import com.ieci.tecdoc.utils.Validator;

import es.msssi.sgm.registropresencial.beans.DistributionResultsBean;
import es.msssi.sgm.registropresencial.beans.Interesado;
import es.msssi.sgm.registropresencial.beans.Representante;
import es.msssi.sgm.registropresencial.beans.RowSearchDistributionBean;
import es.msssi.sgm.registropresencial.beans.RowSearchInputRegisterBean;
import es.msssi.sgm.registropresencial.beans.SearchDistributionBean;
import es.msssi.sgm.registropresencial.beans.ibatis.ScrDirofic;
import es.msssi.sgm.registropresencial.config.RegistroPresencialMSSSIWebSpringApplicationContext;
import es.msssi.sgm.registropresencial.daos.ReportDAO;
import es.msssi.sgm.registropresencial.daos.ScrDiroficDAO;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.errors.RPDistributionErrorCode;
import es.msssi.sgm.registropresencial.errors.RPReportsErrorCode;
import es.msssi.sgm.registropresencial.errors.RPReportsException;
import es.msssi.sgm.registropresencial.utils.Constantes;
import es.msssi.sgm.registropresencial.utils.KeysRP;
import es.msssi.sgm.registropresencial.utils.Utils;
import es.msssi.sgm.registropresencial.utils.XmlHandler;

/**
 * Clase que contiene los métodos para organizar los datos y generar los
 * informes para registros de entrada.
 * 
 * @author jortizs
 * 
 */
public class ReportsDistributionBo extends LazyDataModel<RowSearchInputRegisterBean> implements
    IGenericBo, Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(RegisterBo.class.getName());

    /** Criterio de ordenación para informe de relaciones por destino. */
    private static final String ORDERBYDES = "ID_ORIG0_, ID_FDR0_, DIST_DATE0_";
    /** Criterio de ordenación para informe de relaciones por origen. */
    private static final String ORDERBYORG = "ID_DEST0_, ID_FDR0_, DIST_DATE0_";
    /** Tipo de informe de relaciones por destino. */
    private static final int OPTION_TYPE_RMD = 4;
    /** Tipo de informe de relaciones por origen. */
    private static final int OPTION_TYPE_RMO = 5;

    /** Ruta absoluta de las plantillas de informes. */
    private static String REPORT_TEMPLATE_PATH = null;
    /** Nombre de la plantilla de informe de certificados. */
    private static final String REPORT_ORIGIN_RELATIONS_TEMPLATE_NAME =
	"DistributionOriginRelationsReport.jasper";
    /** Nombre del informe final en formato PDF. */
    private static final String REPORT_ORIGIN_RELATIONS_NAME =
	"InformeRelacionesOrigenDistribuciones.pdf";
    /** Nombre de la plantilla de informe de certificados. */
    private static final String REPORT_DESTINATION_RELATIONS_TEMPLATE_NAME =
	"DistributionDestinationRelationsReport.jasper";
    /** Nombre del informe final en formato PDF. */
    private static final String REPORT_DESTINATION_RELATIONS_NAME =
	"InformeRelacionesDestinoDistribuciones.pdf";
    /**
     * Ruta Xpath del fichero XML temporal del que se obtienen los datos de los
     * registros obtenidos para generar el informe de relaciones por origen.
     */
    private static final String REPORT_EXPRESSION_ORIGIN_RELATIONS = "/INFORME_RP/FLD8";
    /**
     * Ruta Xpath del fichero XML temporal del que se obtienen los datos de los
     * registros obtenidos para generar el informe de relaciones por destino.
     */
    private static final String REPORT_EXPRESSION_DESTINATION_RELATIONS = "/INFORME_RP/FLD7";

    private static ApplicationContext appContext;
    /** Contexto de faces. */
    private FacesContext facesContext;
    /** Variable con la configuración de la aplicación. */
    private UseCaseConf useCaseConf = null;
    /** Bean con los resultados obtenidos. */
    private SearchDistributionBean searchDistributionBean = new SearchDistributionBean();
    /** Clase que contiene la lógica para obtener los interesados. */
    private InterestedBo interestedBo = new InterestedBo();
    /** Clase que contiene los métodos para generar el XML. */
    private XmlHandler myXmlHandler = null;
    /** Indicador sobre si se incluyen las diligencias o no. */
    private boolean includeProceedingClause = false;
    /** objeto clase de negocio. */
    private DistributionBo distributionBo;
    /** objeto clase de negocio. */
    private ReportDAO reportDAO;
    private ScrDiroficDAO scrDiroficDAO;
    
    static {
	appContext = RegistroPresencialMSSSIWebSpringApplicationContext.getInstance().getApplicationContext();
   	if (REPORT_TEMPLATE_PATH == null) {
   		REPORT_TEMPLATE_PATH = Constantes.RESOURCES_PATH + Constantes.REPORTS_TEMPLATE_PATH;
   	}
       }
    
    /**
     * Constructor.
     */
    public ReportsDistributionBo() {
	init();
	reportDAO = (ReportDAO) appContext.getBean("reportDAO");
    }

    /**
     * Inicializa el contexto de faces.
     */
    private void init() {
	LOG.trace("Entrando en ReportsDistributionBo.init() para iniciar el contexto de faces.");
	if (searchDistributionBean == null) {
	    LOG.info("Bean searchInputRegister nulo");
	    searchDistributionBean = new SearchDistributionBean();
	}
	if (facesContext == null) {
	    facesContext = FacesContext.getCurrentInstance();
	    Map<String, Object> map = facesContext.getExternalContext().getSessionMap();
	    useCaseConf = (UseCaseConf) map.get(Keys.J_USECASECONF);
	}
    }

    /**
     * Obtiene los registros para generar un informe de distribución de
     * relaciones por origen o destino.
     * 
     * @param searchDistribution
     *            Datos de la búsqueda.
     * @return distributionBean bean de registros obtenidos.
     */
    public DistributionResultsBean getRegistersForRelationReports(
	SearchDistributionBean searchDistribution) {
	LOG.trace("Entrando en ReportsDistributionBo.getRegistersForRelationReports()");
	DistributionResultsBean distributionList = new DistributionResultsBean();
	searchDistributionBean = searchDistribution;
	if (searchDistribution.isIncludeProceedingValue()){
	    includeProceedingClause = true;
	}
	    
	String orderBy = null;
	try {
	    if (OPTION_TYPE_RMD == searchDistributionBean.getReportTypeValue()) {
		orderBy = ORDERBYDES;
		orderBy += " ASC ";
	    }
	    else if (OPTION_TYPE_RMO == searchDistributionBean.getReportTypeValue()) {
		orderBy = ORDERBYORG;
		orderBy += " ASC  ";
	    }
	    distributionList = getDistribution(
		useCaseConf, 0, orderBy);
	}
	catch (ValidationException e) {
	    LOG.error(RPDistributionErrorCode.GET_DISTRIBUTION_ERROR);
	}
	catch (DistributionException e) {
	    LOG.error(RPDistributionErrorCode.GET_DISTRIBUTION_ERROR);
	}
	catch (SessionException e) {
	    LOG.error(RPDistributionErrorCode.GET_DISTRIBUTION_ERROR);
	}
	return distributionList;
    }

    /**
     * Método que construye la query y controla los permisos para la búsqueda de
     * la distribución.
     * 
     * @param useCaseConf
     *            configuración de la aplicación.
     * @param firstRow
     *            primera fila.
     * @param sortField
     *            campo que ordena.
     * @return objeto con los datos necesarios para la búsqueda.
     * @throws ValidationException
     *             error en la validación
     * @throws DistributionException
     *             error en la distribución
     * @throws SessionException
     *             error de sesión
     */
    public DistributionResultsBean getDistribution(
	UseCaseConf useCaseConf, int firstRow, String sortField)
	throws ValidationException, DistributionException, SessionException {

	Validator.validate_String_NotNull_LengthMayorZero(
	    useCaseConf.getSessionID(), ValidationException.ATTRIBUTE_SESSION);
	distributionBo = new DistributionBo();

	boolean isOfficeAsoc = Boolean.valueOf(
	    Configurator.getInstance().getProperty(
		ConfigurationKeys.KEY_DESKTOP_DISTRIBUTION_OFFICE_ASOC)).booleanValue();

	int pageSize = KeysRP.REPORT_MAX_RESULTS;
	DistributionResultsBean distributionResults = distributionBo.getDistribution(
	    useCaseConf, searchDistributionBean, firstRow, pageSize, isOfficeAsoc, sortField);

	LOG.debug("distributionResults.getTotalSize():" +
	    distributionResults.getTotalSize());
	return distributionResults;
    }

    /**
     * Construye el documento XML correspondiente a los datos de los informes.
     * 
     * @param distributionList
     *            Lista de datos de certificados obtenidos, un informe por
     *            registro.
     * @param isOriginReport
     *            si es un informe de origen.
     */
    private void buildXmlRelationsReportDocument(
	DistributionResultsBean distributionList, boolean isOriginReport) {
	LOG.trace("Entrando en ReportsDistributionBo.buildXmlRelationsReportDocument()");
	myXmlHandler = new XmlHandler(
	    "INFORME_RP");
	List<RowSearchDistributionBean> registersDistList = distributionList.getRows();
	Iterator<RowSearchDistributionBean> registersDistListIterator =
	    registersDistList.iterator();
	int deletedRegistersCounter = 0;
	int initialRegistersListLength = distributionList.getTotalSize();
	while (registersDistListIterator.hasNext()) {
	    RowSearchDistributionBean rowSDBean = registersDistListIterator.next();
	    if (isOriginReport) {
		if (rowSDBean.getDestinoDist() == null ||
		    (rowSDBean.getDestinoDist() != null && rowSDBean.getOrigenDist() == null)) {
		    registersDistListIterator.remove();
		    deletedRegistersCounter++;
		}
	    }
	    else {
		if (rowSDBean.getOrigenDist() == null ||
		    (rowSDBean.getOrigenDist() != null && rowSDBean.getDestinoDist() == null)) {
		    registersDistListIterator.remove();
		    deletedRegistersCounter++;
		}
	    }
	}
	if (distributionList.getTotalSize() == 0) {
	    // Si los resultados son nulos, se lanza excepción
	    LOG.error(ErrorConstants.REPORT_DATA_ERROR);
	    FacesMessage message = new FacesMessage();
	    FacesContext facesContext = FacesContext.getCurrentInstance();
	    message.setSeverity(FacesMessage.SEVERITY_ERROR);
	    message.setSummary(ErrorConstants.REPORT_DATA_ERROR);
	    message.setDetail("No hay registros para la búsqueda realizada");
	    facesContext.addMessage(
		"Informe de registros", message);
	    LOG.error(message.getDetail() +
		SPACE + ErrorConstants.REPORT_DATA_ERROR);
	    throw new ValidatorException(
		message);
	}
	LOG.info("Se han eliminado " +
	    deletedRegistersCounter + " registros de un total de " + initialRegistersListLength +
	    " al no tener los datos necesarios para estar en el informe");
	if (isOriginReport) {
	    LOG.info("Es un informe de relaciones por origen. "
		+ "Se rellena el XML para el informe teniendo en cuenta esto");
	    fillFldForOriginNodes(registersDistList);
	}
	else {
	    LOG.info("Es un informe de relaciones por destino. "
		+ "Se rellena el XML para el informe teniendo en cuenta esto");
	    fillFldForDestinationNodes(registersDistList);
	}
	LOG.warn("XML Constuido: " +
	    myXmlHandler.toString());
    }

    /**
     * Construye el esquema XML con el que se rellenará el informe de relaciones
     * por origen.
     * 
     * @param registersList
     *            Lista de registros pendientes de distribución.
     */
    private void fillFldForOriginNodes(
	List<RowSearchDistributionBean> registersList) {
	myXmlHandler.addBeginXmlHandler("FLD8");
	myXmlHandler.addNode(
	    "FLD8_TEXT", registersList.get(
		0).getDestinoDist() != null
		? registersList.get(
		    0).getDestinoDist() : "");
	myXmlHandler.addNode(
	    "FLD8_FATHERNAME", reportDAO.getFatherNameDep(
			registersList.get(
				    0).getIdDest(), useCaseConf.getEntidadId()));
	myXmlHandler.addNode(
	    "FLD8_ADDRESS", reportDAO.getAddressOrg(registersList.get(
			0).getIdDest()));
	myXmlHandler.addNode(
	    "FLD5_TEXT", registersList.get(
		0).getAxsf().getFld5Name());
	ScrDirofic direccion = getDireccionOfic( registersList.get(0).getAxsf().getFld5().getId());
	if (direccion != null){
    		myXmlHandler.addNode(
    			"FLD5_ADDRESS",
    				reportDAO.formatAddressOfic(direccion.getAddress(),
    				direccion.getCity(),direccion.getZip(),
    				direccion.getCountry(), direccion.getTelephone(),
    				direccion.getFax(), direccion.getEmail()));
	}

	if (includeProceedingClause) {
	    LOG.info("Se añaden las diligencias");
	    myXmlHandler.addNode(
		"PROCEEDING", KeysRP.REPORT_PROCEEEDING_TEXT);
	}
	int numOrganoOrigen = registersList.get(
	    0).getIdDest();
	int finalRegistersListLength = registersList.size();
	int registerCounter = 0;
	LOG.warn("finalRegistersListLength es: " +
	    finalRegistersListLength);
	for (int i = 0; i < finalRegistersListLength; i++) {
	    if (numOrganoOrigen == registersList.get(
		i).getIdDest()) {
		registerCounter++;
		fillRegisterNode(
		    registersList.get(i), true);
	    }
	    else {
		numOrganoOrigen = registersList.get(
		    i).getIdDest();
		myXmlHandler.addNode(
		    "TOTAL_REGISTROS", registerCounter);
		myXmlHandler.addEndXmlHandler("FLD8");
		myXmlHandler.addBeginXmlHandler("FLD8");
		myXmlHandler.addNode(
		    "FLD8_TEXT", registersList.get(
			i).getDestinoDist() != null
			? registersList.get(
			    i).getDestinoDist() : "");
		myXmlHandler.addNode(
		    "FLD8_FATHERNAME", reportDAO.getFatherNameDep(
			registersList.get(
			    0).getIdDest(), useCaseConf.getEntidadId()));
		myXmlHandler.addNode(
		    "FLD8_ADDRESS", "");
		myXmlHandler.addNode(
		    "FLD5_TEXT", registersList.get(
			i).getAxsf().getFld5Name());
		ScrDirofic direccion1 = getDireccionOfic( registersList.get(0).getAxsf().getFld5().getId());
		if (direccion1 != null){
	    		myXmlHandler.addNode(
	    			"FLD5_ADDRESS",
	    				reportDAO.formatAddressOfic(direccion1.getAddress(),
	    				direccion1.getCity(),direccion1.getZip(),
	    				direccion1.getCountry(), direccion1.getTelephone(),
	    				direccion1.getFax(), direccion1.getEmail()));
		}
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
     * Construye el esquema XML con el que se rellenará el informe de relaciones
     * por destino.
     * 
     * @param registersList
     *            Lista de registros pendientes de distribución.
     */
    private void fillFldForDestinationNodes(
	List<RowSearchDistributionBean> registersList) {
	myXmlHandler.addBeginXmlHandler("FLD7");
	myXmlHandler.addNode(
	    "FLD7_TEXT", registersList.get(
		0).getOrigenDist() != null
		? registersList.get(
		    0).getOrigenDist() : "");
	myXmlHandler.addNode(
	    "FLD7_FATHERNAME", reportDAO.getFatherNameDep(
		registersList.get(
		    0).getIdOrig(), useCaseConf.getEntidadId()));
	myXmlHandler.addNode(
	    "FLD7_ADDRESS", reportDAO.getAddressOrg(registersList.get(
			0).getIdOrig()));
	myXmlHandler.addNode(
	    "FLD5_TEXT", registersList.get(
		0).getAxsf().getFld5Name());
	ScrDirofic direccion = getDireccionOfic( registersList.get(0).getAxsf().getFld5().getId());
	if (direccion != null){
    		myXmlHandler.addNode(
    			"FLD5_ADDRESS",
    				reportDAO.formatAddressOfic(direccion.getAddress(),
    				direccion.getCity(),direccion.getZip(),
    				direccion.getCountry(), direccion.getTelephone(),
    				direccion.getFax(), direccion.getEmail()));
	}
	if (includeProceedingClause) {
	    LOG.info("Se añaden las diligencias");
	    myXmlHandler.addNode(
		"PROCEEDING", KeysRP.REPORT_PROCEEEDING_TEXT);
	}
	int numOrganoOrigen = registersList.get(
	    0).getIdOrig();
	int finalRegistersListLength = registersList.size();
	int registerCounter = 0;
	// Se añaden los datos de los registros para cada oficina
	for (int i = 0; i < finalRegistersListLength; i++) {
	    if (numOrganoOrigen == registersList.get(
		i).getIdOrig()) {
		registerCounter++;
		fillRegisterNode(
		    registersList.get(i), false);
	    }
	    else {
		numOrganoOrigen = registersList.get(
		    i).getIdOrig();
		myXmlHandler.addNode(
		    "TOTAL_REGISTROS", registerCounter);
		myXmlHandler.addEndXmlHandler("FLD7");
		myXmlHandler.addBeginXmlHandler("FLD7");
		myXmlHandler.addNode(
		    "FLD7_TEXT", registersList.get(
			i).getOrigenDist() != null
			? registersList.get(
			    i).getOrigenDist() : "");
		myXmlHandler.addNode(
		    "FLD7_FATHERNAME", reportDAO.getFatherNameDep(
			registersList.get(
			    0).getIdOrig(), useCaseConf.getEntidadId()));
		myXmlHandler.addNode(
		    "FLD7_ADDRESS", "");
		myXmlHandler.addNode(
		    "FLD5_TEXT", registersList.get(
			i).getAxsf().getFld5Name());
		ScrDirofic direccion1 = getDireccionOfic( registersList.get(0).getAxsf().getFld5().getId());
		if (direccion1 != null){
	    		myXmlHandler.addNode(
	    			"FLD5_ADDRESS",
	    				reportDAO.formatAddressOfic(direccion1.getAddress(),
	    				direccion1.getCity(),direccion1.getZip(),
	    				direccion1.getCountry(), direccion1.getTelephone(),
	    				direccion1.getFax(), direccion1.getEmail()));
		}
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
     * @param rowSDBean
     *            Bean con los datos a rellenar.
     * @param isOriginReport
     *            Si es un informe de origen.
     */
    @SuppressWarnings("unchecked")
    private void fillRegisterNode(
	RowSearchDistributionBean rowSDBean, boolean isOriginReport) {
	Map<String, Object> attributesMap = rowSDBean.getAxsf().getAttributesValues();
	myXmlHandler.addBeginXmlHandler("REGISTRO");
	myXmlHandler.addNode(
	    "FDRID", rowSDBean.getIdFdr() > 0
		? rowSDBean.getIdFdr() : "");
	myXmlHandler.addNode(
	    "STATE", rowSDBean.getState() > 0
		? rowSDBean.getState() : "");
	myXmlHandler.addNode(
	    "DISTDATE", rowSDBean.getDistDate() != null
		? Utils.formatDateInString(rowSDBean.getDistDate()) : "");
	myXmlHandler.addNode(
	    "FLD1", attributesMap.get("fld1") != null
		? attributesMap.get("fld1") : "");
	myXmlHandler.addNode(
	    "FLD2", attributesMap.get("fld2") != null
		? Utils.formatDateInString((Date) attributesMap.get("fld2")) : "");
	myXmlHandler.addNode(
	    "FLD3", attributesMap.get("fld3") != null
		? attributesMap.get("fld3") : "");
	myXmlHandler.addNode(
	    "FLD4", attributesMap.get("fld4") != null
		? attributesMap.get("fld4") : "");
	myXmlHandler.addNode(
	    "FLD6", attributesMap.get("fld6") != null
		? attributesMap.get("fld6") : "");
	if (rowSDBean.getAxsf() instanceof AxSfIn){
        	myXmlHandler.addNode("TYPE", "E");
	} else {
	    myXmlHandler.addNode("TYPE", "S");  
       }
	
	if (isOriginReport) {
	    myXmlHandler.addNode(
		"FLD7", rowSDBean.getIdOrig() > 0
		    ? rowSDBean.getIdOrig() : "");
	    myXmlHandler.addNode(
		"FLD7_TEXT", rowSDBean.getOrigenDist() != null
		    ? rowSDBean.getOrigenDist() : "");
	}
	else {
	    myXmlHandler.addNode(
		"FLD8", rowSDBean.getIdDest() > 0
		    ? rowSDBean.getIdDest() : "");
	    myXmlHandler.addNode(
		"FLD8_TEXT", rowSDBean.getDestinoDist() != null
		    ? rowSDBean.getDestinoDist() : "");
	}
	myXmlHandler.addNode(
	    "FLD9", fillSenderFieldFromSenderListToRelationsReport(
		rowSDBean.getIdArch(), rowSDBean.getIdFdr()));
	if (rowSDBean.getAxsf() instanceof AxSfIn){
        	myXmlHandler.addNode(
        	    "FLD10", attributesMap.get("fld10") != null
        		? attributesMap.get("fld10") : "");
        	myXmlHandler.addNode(
        	    "FLD11", attributesMap.get("fld11") != null
        		? attributesMap.get("fld11") : "");
        	myXmlHandler.addNode(
        	    "FLD12", attributesMap.get("fld12") != null
        		? attributesMap.get("fld12") : "");
        	myXmlHandler.addNode(
        	    "FLD13", attributesMap.get("fld13") != null
        		? attributesMap.get("fld13") : "");
        	myXmlHandler.addNode(
        	    "FLD14", attributesMap.get("fld14") != null
        		? attributesMap.get("fld14") : "");
        	myXmlHandler.addNode(
        	    "FLD15", attributesMap.get("fld15") != null
        		? attributesMap.get("fld15") : "");
        	myXmlHandler.addNode(
        	    "FLD17", attributesMap.get("fld17") != null
        		? attributesMap.get("fld17") : "");
        	myXmlHandler.addNode(
        	    "FLD19", attributesMap.get("fld19") != null
        		? attributesMap.get("fld19") : "");
        	myXmlHandler.addNode(
        	    "FLD13_TEXT", attributesMap.get("fld13Name") != null
        		? attributesMap.get("fld13Name") : "");
	
		myXmlHandler.addNode(
			    "FLD16", ((AxSfIn)rowSDBean.getAxsf()).getFld16() != null
				? ((AxSfIn)rowSDBean.getAxsf()).getFld16().getMatter() : "");
	}
	else {
	    myXmlHandler.addNode(
        	    "FLD14", attributesMap.get("fld10") != null
        		? attributesMap.get("fld10") : "");
        	myXmlHandler.addNode(
        	    "FLD15", attributesMap.get("fld11") != null
        		? attributesMap.get("fld11") : "");
        	myXmlHandler.addNode(
        	    "FLD12", attributesMap.get("fld12") != null
        		? attributesMap.get("fld12") : "");
        	myXmlHandler.addNode(
        	    "FLD17", attributesMap.get("fld13") != null
        		? attributesMap.get("fld13") : "");

	    myXmlHandler.addNode(
		    "FLD16", ((AxSfOut)rowSDBean.getAxsf()).getFld12() != null
			? ((AxSfOut)rowSDBean.getAxsf()).getFld12().getMatter() : "");
	}
	myXmlHandler.addEndXmlHandler("REGISTRO");
    }

    /**
     * Construye el informe mediante Jasper y lo envía en la respuesta en
     * formato PDF.
     * 
     * @param distributionList
     *            Lista de Ids. de registros pendientes de distribución.
     * @return file
     * 		informe.
     * @throws Exception
     *             Si se ha producido un error generando el informe.
     */
    public StreamedContent buildJasperReport(
	DistributionResultsBean distributionList)
	throws Exception {
	LOG.trace("Entrando en ReportsDistributionBo.buildJasperReport()");
	// Se definen los parámetros para Jasper
	JRXmlDataSource dataSource;
	//JasperReport jasperReport;
	JasperPrint jasperPrint;
	Map<Object, Object> params = new HashMap<Object, Object>();
	String reportName = "";
	String reportTemplateName = "";
	String reportExpression = "";
	StreamedContent file = null;
	
	if (distributionList != null &&
	    distributionList.getTotalSize() > 0) {
	    if (OPTION_TYPE_RMD == searchDistributionBean.getReportTypeValue()) {
		buildXmlRelationsReportDocument(
		    distributionList, false);
		reportName = REPORT_DESTINATION_RELATIONS_NAME;
		reportTemplateName = REPORT_DESTINATION_RELATIONS_TEMPLATE_NAME;
		reportExpression = REPORT_EXPRESSION_DESTINATION_RELATIONS;
		LOG.info("Vienen datos referentes a un informe de relaciones por destino. " +
		    "Nombre informe: " + REPORT_DESTINATION_RELATIONS_NAME +
		    "; nombre plantilla: " + REPORT_DESTINATION_RELATIONS_TEMPLATE_NAME +
		    ", Xpath: " + REPORT_EXPRESSION_DESTINATION_RELATIONS);
	    }
	    else if (OPTION_TYPE_RMO == searchDistributionBean.getReportTypeValue()) {
		buildXmlRelationsReportDocument(
		    distributionList, true);
		reportName = REPORT_ORIGIN_RELATIONS_NAME;
		reportTemplateName = REPORT_ORIGIN_RELATIONS_TEMPLATE_NAME;
		reportExpression = REPORT_EXPRESSION_ORIGIN_RELATIONS;
		LOG.info("Vienen datos referentes a un informe de relaciones por origen." +
		    " Nombre informe: " + REPORT_ORIGIN_RELATIONS_NAME + "; nombre plantilla: " +
		    REPORT_ORIGIN_RELATIONS_TEMPLATE_NAME + ", Xpath: " +
		    REPORT_EXPRESSION_ORIGIN_RELATIONS);
	    }
	}
	else {
	    LOG.error(ErrorConstants.REPORT_DATA_ERROR);
	    throw new RPReportsException(
		RPReportsErrorCode.REPORT_DATA_ERROR, ErrorConstants.REPORT_DATA_ERROR);
	}
	String xmlDocuemnt = myXmlHandler.getDomDocument();
	LOG.info("Documento XML para rellenar el informe: \n" +
	    xmlDocuemnt);

	// Se llena el datasource con la información
	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	DocumentBuilder builder = factory.newDocumentBuilder();
	dataSource = new JRXmlDataSource(
	    builder.parse(new InputSource(
		new StringReader(
		    xmlDocuemnt))), reportExpression);

	// Se compila el archivo XML y se carga en memoria
	LOG.info("Ruta de la plantilla del informe a compilar: " +
	    REPORT_TEMPLATE_PATH + reportTemplateName);
	//jasperReport = JasperCompileManager.compileReport(REPORT_TEMPLATE_PATH +
	//    reportTemplateName);

	// Se llena el reporte con la información de la colección y parámetros
	// necesarios
	// para la consulta
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

	// Se exporta el informe a pdf:
	// 1.- Se guarda en disco
	// JasperExportManager.exportReportToPdfFile(jasperPrint,
	// REPORT_TEMPLATE_PATH +
	// REPORT_ORIGIN_RELATIONS_NAME);
	// 2.1 - Se genera el informe en PDF
	byte[] pdfReportByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
	// 2.2 - Se firma el informe y se guarda
	//SignResponse signResponse = ReportsSignature.signReport(pdfReportByteArray,reportName);
	//byte[] pdfSignedReportByteArray = signResponse.getSignData();
	 file = new DefaultStreamedContent(
		new ByteArrayInputStream(pdfReportByteArray), KeysRP.MIME_TYPE_PDF, reportName);
	 return file;
    }

    /**
     * Clase que obtiene los interesados y sus representantes, de un registro y
     * los mapea a la lista de resultados.
     * 
     * @param bookId
     *            id del libro.
     * @param registerId
     *            Id del registro del que se quieren obtener los datos.
     * @return senderField Lista de interesados y representantes, si los
     *         hubiera.
     */
    private String fillSenderFieldFromSenderListToRelationsReport(
	Integer bookId, Integer registerId) {
	LOG.trace("Entrando en "
	    + "ReportsDistributionBo.fillSenderFieldFromSenderListToRelationsReport()");
	String senderField = "";
	List<Interesado> interesados = interestedBo.getAllInterested(
	    bookId, registerId, useCaseConf);
	if (interesados != null &&
	    interesados.size() > 0) {
	    Iterator<Interesado> interesadosIterator = interesados.iterator();
	    while (interesadosIterator.hasNext()) {
		Interesado interesadoToString = interesadosIterator.next();
		if ("P".equals(interesadoToString.getTipo())) {
		    if (interesadoToString.getNombre() != null &&
			!"".equals(interesadoToString.getNombre())) {
			senderField += interesadoToString.getNombre();
		    }
		    if (interesadoToString.getPapellido() != null &&
			!"".equals(interesadoToString.getPapellido())) {
			senderField += " " +
			    interesadoToString.getPapellido();
		    }
		    if (interesadoToString.getSapellido() != null &&
			!"".equals(interesadoToString.getSapellido())) {
			senderField += " " +
			    interesadoToString.getSapellido();
		    }
		}
		else {
		    if (interesadoToString.getRazonSocial() != null &&
			!"".equals(interesadoToString.getRazonSocial())) {
			senderField += interesadoToString.getRazonSocial();
		    }
		}
		if (interesadoToString.getRepresentante() != null 
			&&  !"N".equals(interesadoToString.getRepresentante().getTipo())) {
		    Representante senderAgent = interesadoToString.getRepresentante();
		    if ("P".equals(senderAgent.getTipo())) {
			if (senderAgent.getNombre() != null &&
			    !"".equals(senderAgent.getNombre())) {
			    senderField += " representado o firmado por " +
				senderAgent.getNombre();
			}
			if (senderAgent.getPapellido() != null &&
			    !"".equals(senderAgent.getPapellido())) {
			    senderField += " " +
				senderAgent.getPapellido();
			}
			if (senderAgent.getSapellido() != null &&
			    !"".equals(senderAgent.getSapellido())) {
			    senderField += " " +
				senderAgent.getSapellido();
			}
		    }
		    else {
			 if ("J".equals(senderAgent.getTipo())) {
        			if (senderAgent.getRazonSocial() != null &&
        			    !"".equals(senderAgent.getRazonSocial())) {
        			    senderField += " representado o firmado por " +
        				senderAgent.getRazonSocial();
        			}
			 }
		    }
		}
		senderField += "\n";
	    }
	}
	LOG.info("Lista de interesados para el registro de id. " +
	    registerId + ": " + senderField);
	return senderField;
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
    
}
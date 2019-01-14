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
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.entidades.Entidad;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.sf.jasperreports.engine.JRExporter;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.export.JRPdfExporterParameter;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;
import org.springframework.context.ApplicationContext;
import org.xml.sax.InputSource;

import com.ieci.tecdoc.common.invesicres.ScrRegstate;
import com.ieci.tecdoc.common.isicres.AxSf;
import com.ieci.tecdoc.isicres.usecase.UseCaseConf;

import es.dipucr.sigem.api.rule.common.correo.CorreoConfiguration;
import es.ieci.tecdoc.fwktd.core.spring.configuration.jdbc.datasource.MultiEntityContextHolder;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.DocumentoElectronicoAnexoVO;
import es.msssi.sgm.registropresencial.beans.InputRegisterBean;
import es.msssi.sgm.registropresencial.beans.Interesado;
import es.msssi.sgm.registropresencial.beans.OutputRegisterBean;
import es.msssi.sgm.registropresencial.beans.ibatis.Axdoch;
import es.msssi.sgm.registropresencial.config.RegistroPresencialMSSSIWebSpringApplicationContext;
import es.msssi.sgm.registropresencial.daos.ReportDAO;
import es.msssi.sgm.registropresencial.utils.Constantes;
import es.msssi.sgm.registropresencial.utils.KeysRP;
import es.msssi.sgm.registropresencial.utils.QRCodeUtils;
import es.msssi.sgm.registropresencial.utils.Utils;
import es.msssi.sgm.registropresencial.utils.XmlHandler;

/**
 * Clase que contiene los métodos para organizar los datos y generar los
 * informes para registros de entrada.
 * 
 * @author cmorenog
 * 
 */
public class ReportsLabelBo implements IGenericBo, Serializable {
    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(RegisterBo.class.getName());

    /** Ruta absoluta de las plantillas de informes. */
    private static String REPORT_TEMPLATE_PATH = null;
    /**
     * Ruta Xpath del fichero XML temporal del que se obtienen los datos de los
     * registros obtenidos para generar el informe de registros certificados.
     */
    private static final String REPORT_EXPRESSION_LABEL = "/ETIQUETA";
    private static ApplicationContext appContext;
    /**
     * Clase para obtener los datos de informes de certificados mediante acceso
     * a base de datos.
     */
    private static ReportDAO reportDAO;
    /** Clase con la lógica de negocio. */
    private ReportsInputRegisterBo reportsInputBo;
    private ReportsOutputRegisterBo reportsOuputBo;

    /** Clase que contiene los métodos para generar el XML. */
    private XmlHandler myXmlHandler = null;
    private String acuseJson = "";

    static {
	appContext =
		RegistroPresencialMSSSIWebSpringApplicationContext.getInstance()
			.getApplicationContext();
	if (REPORT_TEMPLATE_PATH == null) {
		REPORT_TEMPLATE_PATH = Constantes.RESOURCES_PATH + Constantes.REPORTS_TEMPLATE_PATH;
	}
    }

    public ReportsLabelBo() {
	if (reportDAO == null) {
	    reportDAO = (ReportDAO) appContext.getBean("reportDAO");
	}

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
    public byte[] buildLabelJasperReport(LinkedHashMap<String, Object> reportResults,
	    ScrRegstate libro, String idRegistro, UseCaseConf useCaseConf, String ultimoAcuse)
	    throws Exception {
	LOG.trace("Entrando en ReportsBo.buildJasperReport()");
	byte[] file = null;
	String csv = null;
	// GENERAR UN CERTIFICADO
	if (ultimoAcuse == null) {
	    csv =
		    addReportRegister(idRegistro, libro,
			    String.valueOf((Integer) reportResults.get("IDOFICINA")), useCaseConf,
			    (String) reportResults.get("NUMERO_REGISTRO"), ultimoAcuse);
	}
	else {
	    JSONParser parser = new JSONParser();
	    JSONArray arrayContent = (JSONArray) parser.parse(ultimoAcuse);
	    csv = (String) ((JSONObject) arrayContent.get(0)).get("csv");
	    acuseJson = ultimoAcuse;
	}
	reportResults.put("CSV", csv);
	// GENERAR ETIQUETA
	LinkedHashMap<String, Object> report = buildLabelReport(reportResults);
	file = (byte[]) report.get("DATA");
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
    private LinkedHashMap<String, Object> buildLabelReport(LinkedHashMap<String, Object> report)
	    throws Exception {
	String reportName = "";
	String reportTemplateName = "";
	String reportExpression = "";
	byte[] pdfReport = null;
	LinkedHashMap<String, Object> certReportWithName = new LinkedHashMap<String, Object>();
	buildXmlLabelDocument(report);

	reportName = KeysRP.LABEL_NAME;
	reportTemplateName = KeysRP.LABEL_TEMPLATE_NAME;
	reportExpression = REPORT_EXPRESSION_LABEL;
	LOG.info("Label a generar. Nombre informe: " + KeysRP.LABEL_NAME + "; nombre plantilla: "
		+ KeysRP.LABEL_TEMPLATE_NAME + ", Xpath: " + REPORT_EXPRESSION_LABEL);
	pdfReport = buildReportToPdf(reportName, reportTemplateName, reportExpression, true);
	LOG.debug("Generado el informe. Se procede a firmar");
	certReportWithName.put("NAME", reportName);
	certReportWithName.put("DATA", pdfReport);
	return certReportWithName;
    }

    /**
     * Construye el documento XML correspondiente a los datos del informe.
     * 
     * @param reportResults
     *            Datos del registro para generar el acuse de recibo.
     */
    private void buildXmlLabelDocument(HashMap<String, Object> reportResults) {
	LOG.trace("Entrando en ReportsLabelBo.buildXmlLabelDocument()");
	myXmlHandler = new XmlHandler();
	myXmlHandler.addBeginXmlHandler("ETIQUETA");
	Map<String, Object> map = reportResults;
	for (Entry<String, Object> entry : map.entrySet()) {
	    if (entry.getValue() != null) {
		if (entry.getValue() instanceof String) {
		    myXmlHandler.addNode(String.valueOf(entry.getKey()), entry.getValue());
		}
		else if (entry.getValue() instanceof Integer) {
		    myXmlHandler
			    .addNode(String.valueOf(entry.getKey()), (Integer) entry.getValue());
		}
	    }
	}
	QRCodeUtils qrCodeUtils = new QRCodeUtils();
	myXmlHandler.addNode("QRCODE",
		Base64.encodeBase64String(qrCodeUtils.genetareQRCode(reportResults)));
	myXmlHandler.addEndXmlHandler("ETIQUETA");
	LOG.warn("XML Constuido: " + myXmlHandler.toString());
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
    private byte[] buildReportToPdf(String reportName, String reportTemplateName,
	    String reportExpression, boolean printer) throws Exception {
	String xmlDocument = myXmlHandler.getDomDocument();
	LOG.info("Documento XML para rellenar el informe: \n" + xmlDocument);
	JRXmlDataSource dataSource;
	// JasperReport jasperReport = null;
	JasperPrint jasperPrint = null;
	Map<Object, Object> params = new HashMap<Object, Object>();
	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	DocumentBuilder builder;
	builder = factory.newDocumentBuilder();
	dataSource =
		new JRXmlDataSource(builder.parse(new InputSource(new StringReader(xmlDocument))),
			reportExpression);
	LOG.info("Ruta de la plantilla del informe a compilar: " + REPORT_TEMPLATE_PATH
		+ reportTemplateName);

	byte[] pdfReportByteArray = null;
	File reportsDir = new File(REPORT_TEMPLATE_PATH);
	if (!reportsDir.exists()) {
	    throw new FileNotFoundException(String.valueOf(reportsDir));
	}
//	params.put("REPORT_FILE_RESOLVER", new SimpleFileResolver(reportsDir));
	params.put("REPORT_IMG_PATH", SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + MultiEntityContextHolder.getEntity(), "/SIGEM_RegistroPresencialMSSSIWeb"));
	Entidad entidad = LocalizadorServicios.getServicioEntidades().obtenerEntidad(MultiEntityContextHolder.getEntity());
	params.put("DESCRIPCION_ENTIDAD", entidad.getNombreLargo());
	CorreoConfiguration correo = CorreoConfiguration.getInstance(MultiEntityContextHolder.getEntity());
	params.put("SEDE_ENTIDAD", correo.getProperty(CorreoConfiguration.CONV_SEDE));
	
	jasperPrint =
		JasperFillManager.fillReport(REPORT_TEMPLATE_PATH + reportTemplateName, params,
			dataSource);
	if (printer) {
	    JRExporter exporter = null;
	    exporter = new JRPdfExporter();
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    exporter.setParameter(JRExporterParameter.JASPER_PRINT, jasperPrint);
	    exporter.setParameter(JRPdfExporterParameter.PDF_JAVASCRIPT, "this.print();");
	    exporter.setParameter(JRExporterParameter.OUTPUT_STREAM, out);
	    exporter.exportReport();
	    pdfReportByteArray = out.toByteArray();

	}
	else {
	    pdfReportByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
	}
	LOG.debug("fichero convertido a pdf");
	return pdfReportByteArray;
    }

    private String addReportRegister(String idRegistro, ScrRegstate libro, String idOficina,
	    UseCaseConf useCaseConf, String fld1, String ultimoAcuse) {
	// GENERAR ACUSE DE RECIBO
	List<StreamedContent> filesToUpload = new ArrayList<StreamedContent>();
	InputRegisterBo inputRegisterBo = null;
	InputRegisterBean inputRegisterBean = null;
	OutputRegisterBo outputRegisterBo = null;
	OutputRegisterBean outputRegisterBean = null;
	List<Integer> fdridList = new ArrayList<Integer>();

	List<LinkedHashMap<String, Object>> reportResults = null;
	AxSf registerData = null;
	RegisterDocumentsBo registerDocumentsBo = null;
	LinkedHashMap<String, Object> acuse = null;
	Integer numAcuses = 0;
	try {
	    registerDocumentsBo = new RegisterDocumentsBo();
	    fdridList.add(Integer.parseInt(idRegistro));
	    LOG.info("Hay " + fdridList.size() + " Ids");
	    if (libro.getIdocarchhdr().getType() == 1) {
		reportResults =
			reportDAO.getRegisterCertReports(fdridList, libro.getIdocarchhdr().getId(),
				true);
		reportsInputBo = new ReportsInputRegisterBo();
		acuse =
			reportsInputBo.buildJasperReportReturnMap(libro.getIdocarchhdr().getId(),
				reportResults);

	    }
	    else {
		reportResults =
			reportDAO.getRegisterCertReports(fdridList, libro.getIdocarchhdr().getId(),
				false);
		reportsOuputBo = new ReportsOutputRegisterBo();
		acuse =
			reportsOuputBo.buildJasperReportReturnMap(libro.getIdocarchhdr().getId(),
				reportResults);
	    }
	    byte[] data = (byte[]) acuse.get("DATA");

	    if (libro.getIdocarchhdr().getType() == 1) {

		inputRegisterBo = new InputRegisterBo();
		inputRegisterBean =
			inputRegisterBo.loadInputRegisterBean(useCaseConf, libro,
				Integer.parseInt(idRegistro));
		registerData = Utils.mappingInputRegisterToAxSF(inputRegisterBean);
		numAcuses =
			registerDocumentsBo.countPageReport(useCaseConf, libro.getIdocarchhdr()
				.getId(), inputRegisterBean.getFdrid());
		StreamedContent content = null;
		String nombre = null;
		if (numAcuses > 0) {
		    nombre = (String) acuse.get("NAME");
		    numAcuses = numAcuses + 1; //es el siguiente  
		    nombre = nombre.substring(0, nombre.indexOf(".")) + "_" + numAcuses + ".pdf";
		    content =
			    new DefaultStreamedContent(new ByteArrayInputStream(data),
				    KeysRP.MIME_TYPE_PDF, nombre);
		}
		else {
		    numAcuses = numAcuses + 1; //es el siguiente  
		    content =
			    new DefaultStreamedContent(new ByteArrayInputStream(data),
				    KeysRP.MIME_TYPE_PDF, (String) acuse.get("NAME"));
		}
		filesToUpload.add(content);
		DocumentoElectronicoAnexoVO documentSaved =
			registerDocumentsBo.saveStreamedContentDocuments(
				useCaseConf.getSessionID(), libro.getIdocarchhdr().getId(),
				Integer.parseInt(idRegistro), filesToUpload, registerData,
				useCaseConf.getLocale(), useCaseConf.getEntidadId());

		if (documentSaved != null){

		    List<Axdoch> listDocuments;
		    listDocuments =
			    registerDocumentsBo.getDocumentsBasicInfo(useCaseConf, libro
				    .getIdocarchhdr().getId(), inputRegisterBean.getFdrid(), false);
		    Axdoch acuseDoc = null;

		    for (Axdoch doc : listDocuments) {
			if (doc.getName().startsWith(KeysRP.IR_REPORT_CERTIFICATE_NAME + fld1)) {
			    if (acuseDoc == null) {
				acuseDoc = doc;
			    }
			    else {
				if (doc.getId() > acuseDoc.getId()) {
				    acuseDoc = doc;
				}
			    }
			}
		    }
		    if (acuseDoc != null) {
			acuseJson =
				"[{\"iddoc\":" + acuseDoc.getId() + ",\"idpag\":"
					+ acuseDoc.getPages().get(0).getId() + ",\"csv\":\""
					+ (String) acuse.get("CSV") + "\"}]";
			InputRegisterBean inputBeanUpdate = new InputRegisterBean();
			inputBeanUpdate.setFdrid(inputRegisterBean.getFdrid());
			inputBeanUpdate.setFld1(inputRegisterBean.getFld1());
			inputBeanUpdate.setFld1004(acuseJson);
			if (inputRegisterBean.getFld9() != null
				&& !"".equals(inputRegisterBean.getFld9().trim())) {
			    List<Interesado> interesados = new ArrayList<Interesado>();
			    Interesado interesado = new Interesado();
			    interesado.setNombre(inputRegisterBean.getFld9());
			    interesados.add(interesado);
			    inputBeanUpdate.setInteresados(interesados);
			}
			inputRegisterBo.updateOnlyFolder(useCaseConf, libro.getIdocarchhdr()
				.getId(), inputRegisterBean.getFdrid(), inputBeanUpdate);
			registerDocumentsBo.updateFlag(useCaseConf, libro.getIdocarchhdr().getId(),
				inputRegisterBean.getFdrid(),acuseDoc.getPages().get(0).getId(), numAcuses);
		    }
		}
	    }
	    else {
		outputRegisterBo = new OutputRegisterBo();
		outputRegisterBean =
			outputRegisterBo.loadOutputRegisterBean(useCaseConf, libro,
				Integer.parseInt(idRegistro));
		registerData = Utils.mappingOutputRegisterToAxSF(outputRegisterBean);
		
		numAcuses =
			registerDocumentsBo.countPageReport(useCaseConf, libro.getIdocarchhdr()
				.getId(), outputRegisterBean.getFdrid());
		StreamedContent content = null;
		String nombre = null;
		if (numAcuses > 0) {
		    nombre = (String) acuse.get("NAME");
		    nombre = nombre.substring(0, nombre.indexOf(".")) + "_" + numAcuses + ".pdf";
		    content =
			    new DefaultStreamedContent(new ByteArrayInputStream(data),
				    KeysRP.MIME_TYPE_PDF, nombre);
		}
		else {
		    content =
			    new DefaultStreamedContent(new ByteArrayInputStream(data),
				    KeysRP.MIME_TYPE_PDF, (String) acuse.get("NAME"));
		}
		numAcuses = numAcuses +1;
		filesToUpload.add(content);
		DocumentoElectronicoAnexoVO documentSaved =
			registerDocumentsBo.saveStreamedContentDocuments(
				useCaseConf.getSessionID(), libro.getIdocarchhdr().getId(),
				Integer.parseInt(idRegistro), filesToUpload, registerData,
				useCaseConf.getLocale(), useCaseConf.getEntidadId());
		if (documentSaved != null) {
		    List<Axdoch> listDocuments;
		    listDocuments =
			    registerDocumentsBo
				    .getDocumentsBasicInfo(useCaseConf, libro.getIdocarchhdr()
					    .getId(), outputRegisterBean.getFdrid(), false);
		    Axdoch acuseDoc = null;

		    for (Axdoch doc : listDocuments) {
			if (doc.getName().startsWith(KeysRP.OR_REPORT_CERTIFICATE_NAME + fld1)) {
			    if (acuseDoc == null) {
				acuseDoc = doc;
			    }
			    else {
				if (doc.getId() > acuseDoc.getId()) {
				    acuseDoc = doc;
				}
			    }
			}
		    }
		    if (acuseDoc != null) {
			acuseJson =
				"[{\"iddoc\":" + acuseDoc.getId() + ",\"idpag\":"
					+ acuseDoc.getPages().get(0).getId() + ",\"csv\":\""
					+ (String) acuse.get("CSV") + "\"}]";
			OutputRegisterBean outputBeanUpdate = new OutputRegisterBean();
			outputBeanUpdate.setFdrid(outputRegisterBean.getFdrid());
			outputBeanUpdate.setFld1(outputRegisterBean.getFld1());
			outputBeanUpdate.setFld1004(acuseJson);
			if (outputRegisterBean.getFld9() != null
				&& !"".equals(outputRegisterBean.getFld9().trim())) {
			    List<Interesado> interesados = new ArrayList<Interesado>();
			    Interesado interesado = new Interesado();
			    interesado.setNombre(outputRegisterBean.getFld9());
			    interesados.add(interesado);
			    outputBeanUpdate.setInteresados(interesados);
			}
			outputRegisterBo.updateOnlyFolder(useCaseConf, libro.getIdocarchhdr()
				.getId(), outputRegisterBean.getFdrid(), outputBeanUpdate);
			registerDocumentsBo.updateFlag(useCaseConf, libro.getIdocarchhdr().getId(),
				outputRegisterBean.getFdrid(),acuseDoc.getPages().get(0).getId(), numAcuses);
		    }
		}
	    }

	}
	catch (Exception exception) {
	    LOG.error("Error al generar el informe: ", exception);
	    Utils.redirectToErrorPage(null, null, exception);
	}
	return (String) acuse.get("CSV");
    }

    public String getAcuseJson() {
	return acuseJson;
    }

    public void setAcuseJson(String acuseJson) {
	this.acuseJson = acuseJson;
    }
}
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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import net.sf.jasperreports.engine.data.JRXmlDataSource;
import net.sf.jasperreports.engine.util.SimpleFileResolver;

import org.apache.log4j.Logger;
import org.xml.sax.InputSource;

import beans.SignResponse;

import es.msssi.sgm.registropresencial.signature.ReportsSignature;
import es.msssi.sgm.registropresencial.utils.KeysRP;

public class Report {

    private String REPORT_TEMPLATE_PATH = "";
    private static final Logger LOG = Logger.getLogger(Report.class.getName());
    /**
     * Ruta Xpath del fichero XML temporal del que se obtienen los datos de los
     * registros obtenidos para generar el informe de registros certificados.
     */
    private static final String REPORT_EXPRESSION_CERT = "/INFORME_RP/REGISTRO";
    
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
    private byte[] buildReportToPdf(
	String xmlDocument,
	String reportName, String reportTemplateName, String reportExpression)
	throws Exception {
	LOG.info("Documento XML para rellenar el informe: \n" +
	    xmlDocument);
	JRXmlDataSource dataSource;
	JasperReport jasperReport = null;
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
	
	File reportsDir = new File(
	    REPORT_TEMPLATE_PATH);
	if (!reportsDir.exists()) {
	    throw new FileNotFoundException(
		String.valueOf(reportsDir));
	}
	params.put(
	    "REPORT_FILE_RESOLVER", new SimpleFileResolver(
		reportsDir));
	params.put( "JRExporterParameter.CHARACTER_ENCODING", "UTF-8");
	jasperPrint = JasperFillManager.fillReport(
		REPORT_TEMPLATE_PATH +
		reportTemplateName, params, dataSource);

	byte[] pdfReportByteArray = JasperExportManager.exportReportToPdf(jasperPrint);
	LOG.debug("fichero convertido a pdf");
	return pdfReportByteArray;
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
    private String buildAndSignCertificateReport(
	String xmlDocument)
	throws Exception {
	String reportName = "";
	String reportTemplateName = "";
	String reportExpression = "";
	byte[] pdfReport = null;
	String fileReturn = null;
	reportName = "NOMBRE_REPORT.pdf";
	reportTemplateName = KeysRP.IR_REPORT_CERTIFICATE_TEMPLATE_NAME;
	reportExpression = REPORT_EXPRESSION_CERT;
	LOG.info("Acuse de recibo a generar. Nombre informe: " +
	    KeysRP.IR_REPORT_CERTIFICATE_NAME + "; nombre plantilla: " +
	    KeysRP.IR_REPORT_CERTIFICATE_TEMPLATE_NAME + ", Xpath: " + REPORT_EXPRESSION_CERT);
	pdfReport = buildReportToPdf(xmlDocument,
	    reportName, reportTemplateName, reportExpression);
	LOG.debug("Generado el informe. Se procede a firmar");
	SignResponse signResponse = ReportsSignature.signReport(pdfReport);
	
	byte[] pdfSignedReportByteArray = signResponse.getSignData();
	//GUARDAR FICHERO A DISCO
	LOG.debug("Documento firmado");
	return fileReturn;
    }
}

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="org.springframework.web.context.support.*"%>
<%@ page import="org.springframework.web.context.*"%>
<%@ page import="es.scsp.common.config.*"%>
<%@ page import="es.scsp.client.test.excel.*"%>
<%
String cif = request.getParameter("cif");
String certificado = request.getParameter("certificado");
ExcelConfiguration excelConfig = ExcelConfiguration.getInstance(request);
String templateExcel = "";
String urlDownloadExcel = null;
try { templateExcel = excelConfig.getExcelTemplate(cif, certificado); } catch(Exception e) {}
if(templateExcel != null && !"".equals(templateExcel)) {
	String excelParams = "?action=template&cif=" + cif + "&certificado=" + certificado;
	urlDownloadExcel = "<a href=\"excel" + excelParams + "\">Descargar plantilla</a>";
	//urlDownloadExcel = "<img alt=\"\" src=\"images/download-icon.gif\"/>" + urlDownloadExcel;
} else {
	urlDownloadExcel = "<a href=\"templates/scsp-request-common.xls\">Descargar plantilla</a>";
}
out.print(urlDownloadExcel);
%>

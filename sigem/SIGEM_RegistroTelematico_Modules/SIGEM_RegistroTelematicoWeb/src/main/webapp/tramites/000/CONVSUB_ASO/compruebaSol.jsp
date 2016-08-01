<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="es.dipucr.sigem.api.rule.common.AccesoBBDDRegistro"%>
<%@page import="ieci.tdw.ispac.ispaclib.thirdparty.IThirdPartyAdapter"%>
<%@page import="ieci.tecdoc.sgm.tram.thirdparty.SigemThirdPartyAPI"%>
<%@page import="es.dipucr.sigem.registroTelematicoWeb.formulario.common.XmlCargaDatos"%>
<%@page import="org.w3c.dom.Document"%>
<%@page import="javax.xml.parsers.DocumentBuilderFactory"%>
<%@page import="javax.xml.parsers.DocumentBuilder"%>
<%@page import="org.w3c.dom.Element"%>
<%@page import="org.w3c.dom.NodeList"%>

<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>

<body>
Recuperando datos de solicitudes anteriores.
</body>
<script type="text/javascript">
<%

	String valor = "";
	if(request.getParameter("valor")!=null){
		valor = (String)request.getParameter("valor");
	}
	
	String [] cadenaSplit = valor.split(";");
	String valueAyto = cadenaSplit[0];
	String entidad = cadenaSplit[1];	
	String convocatoria = cadenaSplit[2];

	String ficheroSolAyto = XmlCargaDatos.getDatosConsulta("SolicitudesAyto", "select a.numexp, '' from dpcr_sol_conv_sub a, spac_expedientes b where a.numexp = b.numexp and a.numexp_padre = '"+convocatoria+"' and upper(b.nifciftitular) like upper('%"+valueAyto+"%') and b.estadoadm in ('PR','RS','AP','NT','NE','JF','DC','DI','ES','EJ','CT') and b.fcierre is null and b.codprocedimiento not in (#CDJ-SOLFSF7#,#CDJ-FSF73001#,#SOL-CDJ-ESCDEP#,#SOL-CDJ-7000MONM#)",entidad);
	
	Document dom;
	DocumentBuilderFactory dbf;
	DocumentBuilder db;
	
	dbf = DocumentBuilderFactory.newInstance();
	
	try{
		db = dbf.newDocumentBuilder();
		dom = db.parse(ficheroSolAyto);
	
		Element rootElement = dom.getDocumentElement();

		NodeList nodeList = rootElement.getElementsByTagName("dato");

		if(nodeList != null && nodeList.getLength() > 0){
%>
	alert('Ya ha presentado una solicitud para la convocatoria seleccionada.\nSeleccione otra Convocatoria o realice una solicitud de Subsanción, Modificación o Justificación');
	opener.document.getElementById('convocatoria').value = '-------';
<%
		}
	}
	catch(Exception ex) {}%>


window.close();
</script>

</html>
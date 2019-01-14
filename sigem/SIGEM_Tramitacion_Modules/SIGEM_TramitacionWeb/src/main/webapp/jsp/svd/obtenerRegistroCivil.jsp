<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="es.dipucr.sigem.registroTelematicoWeb.formulario.DatosClienteLigero"%>
<%@page import="org.w3c.dom.Document"%>
<%@page import="javax.xml.parsers.DocumentBuilderFactory"%>
<%@page import="javax.xml.parsers.DocumentBuilder"%>
<%@page import="org.w3c.dom.Element"%>
<%@page import="org.w3c.dom.Element"%>
<%@page import="org.w3c.dom.NodeList"%>
<%@page import="org.w3c.dom.Node"%>

<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>

<body>
Recuperando datos de solicitudes anteriores.
</body>
<script type="text/javascript">
var registro = opener.document.getElementById('registro');
<%

	String valor = "";
	if(request.getParameter("valor")!=null){
		valor = (String)request.getParameter("valor");
	}
	String [] vValor = valor.split(";");
	
	String ficheroSolAyto = "";
	
	String idProvincia = vValor[0];
	String idMunicipio = vValor[1];
	
	String registroMuni = DatosClienteLigero.getRegistroCivil(idProvincia, idMunicipio);
	%>
	
	registro.value= '<%=registroMuni%>';


window.close();
</script>

</html>
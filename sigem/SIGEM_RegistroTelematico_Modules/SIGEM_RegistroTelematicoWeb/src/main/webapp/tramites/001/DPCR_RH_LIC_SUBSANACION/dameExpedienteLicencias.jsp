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

	var numExpediente = opener.document.getElementById('numExpediente');

<%
	String valor = "";
	if(request.getParameter("valor")!=null){
		valor = (String)request.getParameter("valor");
	}
	
	String [] cadenaSplit = valor.split(";");
	String idSolicitud = cadenaSplit[0];
	String entidad = cadenaSplit[1];	
	
	String consulta = " select exp.numexp from spac_expedientes exp, spac_ct_procedimientos pcd, rrhh_licencias lic" +
			    " where exp.id_pcd = pcd.id" +
			    " and pcd.nombre = 'RRHH - Solicitud de Licencias'" +
			    " and lic.numexp = exp.numexp" +
			    " and id_solicitud = '" + idSolicitud + "'";
	
	try{
		String numExp = XmlCargaDatos.getDatoUnico(consulta, entidad);
		%>						
		numExpediente.value = '<%=numExp%>';
		<%
	}
	catch(Exception ex) {
		%>						
		numExpediente.value = '';
		<%
	}
%>


window.close();
</script>

</html>
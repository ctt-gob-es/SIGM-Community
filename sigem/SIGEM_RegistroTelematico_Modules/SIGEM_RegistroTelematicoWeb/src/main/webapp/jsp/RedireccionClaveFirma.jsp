<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ page import="ieci.tecdoc.sgm.registro.utils.Defs" %>

<%
String rutaEstilos = (String)session.getAttribute("PARAMETRO_RUTA_ESTILOS");
if (rutaEstilos == null) rutaEstilos = "";
String rutaImagenes = (String)session.getAttribute("PARAMETRO_RUTA_IMAGENES");
if (rutaImagenes == null) rutaImagenes = "";
%>

<html:html locale="true">
<head>	
	<link rel="stylesheet" href="css/<%=rutaEstilos%>estilos.css" type="text/css" />
	<%		
		String dir = (String)request.getSession().getAttribute(Defs.CLAVE_FIRMA_URL);
	%>    
    <script language="Javascript">
			function redirige(){
				//[DipuCR-Agustin] #548 integrar Cl@ve autentificacion, indicar puerto de no autentificacion cliente, conexion segura sin indicar certificado cliente
				parent.document.location.href = '<%=dir%>';
				
			}
	</script>
</head>
<body onLoad="javascript:redirige()">
	<p><bean:message key="cargando"/></p>
</body>    
</html:html>

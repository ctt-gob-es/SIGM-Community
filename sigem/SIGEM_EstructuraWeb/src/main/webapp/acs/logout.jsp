<%@ page contentType="text/html;charset=ISO-8859-1" language="java" %>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/ieci.tld" prefix="ieci"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ page import="javax.naming.InitialContext" %>
<%@ page import="javax.naming.Context" %>
<%@ page import="ieci.tecdoc.sgm.core.admin.web.AutenticacionAdministracion" %>
<%@ page import="ieci.tecdoc.sgm.core.services.gestion_administracion.*"%>

<%
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setDateHeader ("Expires", 0); //prevents caching at the proxy server

String url = (String)request.getAttribute("urlLogout");
if (url == null || url.equals(""))
	url = ieci.tecdoc.sgm.core.admin.web.AutenticacionAdministracion.obtenerUrlLogout(request);	

url+="?desconectar=si";

session.removeAttribute(ConstantesGestionUsuariosAdministracion.PARAMETRO_ID_APLICACION);
session.removeAttribute(ConstantesGestionUsuariosAdministracion.PARAMETRO_ID_ENTIDAD);
session.removeAttribute(ConstantesGestionUsuariosAdministracion.PARAMETRO_USUARIO);
session.removeAttribute(ConstantesGestionUsuariosAdministracion.PARAMETRO_PASSWORD);

%>

<html>
	<head>
		 <script>
				window.name="ParentWindow";
				function redirigir(){
					document.forms[0].submit();			
				}
		  </script>
	</head>
	<body onload="javascript:setTimeout('redirigir()', 3000);">
		<p>
			<bean:message key="cargando"/>
			<form action="<html:rewrite href="<%=url%>"/>" method="POST">
				<input type="hidden" name="<%=ConstantesGestionUsuariosAdministracion.PARAMETRO_ID_ENTIDAD%>" id="<%=ConstantesGestionUsuariosAdministracion.PARAMETRO_ID_ENTIDAD%>" value="000" />
				<input type="hidden" name="<%=ConstantesGestionUsuariosAdministracion.PARAMETRO_ID_APLICACION%>" id="<%=ConstantesGestionUsuariosAdministracion.PARAMETRO_ID_APLICACION%>" value="<%=ConstantesGestionUsuariosAdministracion.APLICACION_ESTRUCTURA_ORGANIZATIVA%>" />
			</form>
		</p>
	</body>
</html>
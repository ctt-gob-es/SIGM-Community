<%@taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>

<!-- [Manu #814] INICIO - SIGEM Administración - Poner nombre entidad en la que estamos en el Catálogo de Procedimientos. -->

<%@page import="ieci.tecdoc.sgm.core.admin.web.AutenticacionAdministracion" %>
<%@page import="ieci.tecdoc.sgm.core.services.LocalizadorServicios" %>

<% 
	String entidad = AutenticacionAdministracion.obtenerDatosEntidad(request).getIdEntidad();
	String descEntidad = LocalizadorServicios.getServicioEntidades().obtenerEntidad(entidad).getNombreCorto();
%>
<!-- [Manu #814] FIN - SIGEM Administración - Poner nombre entidad en la que estamos en el Catálogo de Procedimientos. -->

	<div id="cabecera">

		<img src="img/logo.gif" alt="sigem" />

		<h3>&nbsp;</h3>
		<p class="salir"><a href='<html:rewrite page="/desconectar.do"/>'><bean:message key="salir"/></a></p>
	</div>
	<div id="usuario">
		<div id="barra_usuario">
			<h3><bean:message key="procedures.title"/></h3>
			<p class="ayuda">
			<!-- [Manu #814] INICIO - SIGEM Administración - Poner nombre entidad en la que estamos en el Catálogo de Procedimientos. -->			
				Entidad: <%=descEntidad%>
			<!-- [Manu #814] FIN - SIGEM Administración - Poner nombre entidad en la que estamos en el Catálogo de Procedimientos. -->
				<a href="#" target="_blank">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>
			</p>
		</div>
	</div>


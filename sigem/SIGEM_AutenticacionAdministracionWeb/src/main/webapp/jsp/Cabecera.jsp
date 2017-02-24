<%@taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<%@ page import="javax.naming.InitialContext" %>
<%@ page import="javax.naming.Context" %>
<%@page import="java.util.Locale"%>
<%@page import="ieci.tecdoc.sgm.core.services.idioma.Idioma"%>
<%@page import="ieci.tecdoc.sgm.core.services.idioma.ConstantesIdioma"%>

	<div id="cabecera">		
		<h1>&nbsp;</h1>
		<h3>&nbsp;</h3>
		<p class="salir"><a href="logout.do"><bean:message key="salir"/></a></p>
	</div>

	<div id="usuario">
		<div id="barra_usuario">
			<h3><bean:message key="aplicacion"/></h3>
			<p class="ayuda">
				<a href="<%=((Context) new InitialContext().lookup("java:comp/env")).lookup("URLRoot")%>/ayuda/AccesoSistemaTramitacion.htm" target="_blank">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</a>
			</p>
		</div>
	</div>

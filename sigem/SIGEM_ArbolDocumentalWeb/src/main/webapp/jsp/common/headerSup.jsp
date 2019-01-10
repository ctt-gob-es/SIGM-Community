<%@ taglib prefix="html" uri="/WEB-INF/struts-html.tld" %>
<%@ taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%@ taglib prefix="logic" uri="/WEB-INF/struts-logic.tld" %>
<%@ taglib prefix="utils-admin" uri="http://www.ieci.es/tecdoc/smg/utilidadesadministracion" %>

<%@page import="ieci.tecdoc.sgm.core.services.gestion_backoffice.ConstantesGestionUsuariosBackOffice"%>

<%

	// [eCenpri-Manu Ticket #125] - INICIO - ALSIGM3 - Modifcar la opción salir para que no lleve a la pantalla del portal de AL-SIGM 
	String key = (String)session.getAttribute(ConstantesGestionUsuariosBackOffice.PARAMETRO_KEY_SESION_USUARIO);
	session.removeAttribute(ConstantesGestionUsuariosBackOffice.PARAMETRO_KEY_SESION_USUARIO);
	if (null != key && !key.equals("")) {
		ieci.tecdoc.sgm.core.services.admsesion.backoffice.ServicioAdministracionSesionesBackOffice oClient = ieci.tecdoc.sgm.core.services.LocalizadorServicios.getServicioAdministracionSesionesBackOffice();
		oClient.caducarSesion(key);
	}
	// [eCenpri-Manu Ticket #125] - FIN - ALSIGM3 - Modifcar la opción salir para que no lleve a la pantalla del portal de AL-SIGM 

%>

	<div id="cabecera">
		<utils-admin:eco-logo/>
		<p class="logoSIGM">					
		<!-- [eCenpri-Manu Ticket#267] + ALSIGM3 Logotipo de la entidad sale feas en registro presencial y tramitador -->
			<!-- <img src="<%=request.getContextPath()%>/resourceServlet/logos/logo.gif" alt="sigem" width="129px" height="56px"/> -->
			<img src="<%=request.getContextPath()%>/resourceServlet/logos/logo.gif" alt="SIGM" style="width: auto; max-width: 200px; height: 59px; max-height: 200px;" />
		<!-- [eCenpri-Manu Ticket#267] + ALSIGM3 Logotipo de la entidad sale feas en registro presencial y tramitador -->
		</p>
	</div>

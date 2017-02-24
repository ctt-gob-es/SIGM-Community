<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib prefix="utils-admin" uri="http://www.ieci.es/tecdoc/smg/utilidadesadministracion" %>

<%@ page import="ieci.tecdoc.idoc.admin.api.EstructuraOrganizativaLdapManager" %>
<%@ page import="ieci.tecdoc.sgm.core.services.gestion_backoffice.ConstantesGestionUsuariosBackOffice" %>
<%@ page import="ieci.tecdoc.sgm.backoffice.utils.Defs" %>
<%@ page import="ieci.tecdoc.sgm.backoffice.utils.Utilidades" %>
<%@ page import="javax.naming.InitialContext" %>
<%@ page import="javax.naming.Context" %>

<bean:parameter id="url" name="url" />

<%
	String idAplicacion = (String)request.getParameter(ConstantesGestionUsuariosBackOffice.PARAMETRO_ID_APLICACION);
	
	if(Utilidades.isNuloOVacio(idAplicacion))
		idAplicacion = (String)request.getSession().getAttribute(ConstantesGestionUsuariosBackOffice.PARAMETRO_ID_APLICACION);
	
	String tituloAplicacionCabecera="";
	String tituloAplicacion="";
	
	if(!Utilidades.isNuloOVacio(idAplicacion) && idAplicacion.equals(ConstantesGestionUsuariosBackOffice.APLICACION_REGISTRO_PRESENCIAL)){
		tituloAplicacionCabecera="Aplicaci&oacute;n de Registro";
		tituloAplicacion="Entrada a la aplicaci&oacute;n de Registro";
	}else if(!Utilidades.isNuloOVacio(idAplicacion) && idAplicacion.equals(ConstantesGestionUsuariosBackOffice.APLICACION_TRAMITADOR_MANAGER)){
		tituloAplicacionCabecera="Aplicaci&oacute;n de Tramitaci&oacute;n de Expedientes";
		tituloAplicacion="Entrada a la aplicaci&oacute;n de Tramitaci&oacute;n de Expedientes";
	}else if(!Utilidades.isNuloOVacio(idAplicacion) && idAplicacion.equals(ConstantesGestionUsuariosBackOffice.APLICACION_CONSULTA_EXPEDIENTES)){
		tituloAplicacionCabecera="Aplicaci&oacute;n de Consulta de Expedientes";
		tituloAplicacion="Entrada a la aplicaci&oacute;n de Consulta de Expedientes";
	}else if(!Utilidades.isNuloOVacio(idAplicacion) && idAplicacion.equals(ConstantesGestionUsuariosBackOffice.APLICACION_CONSULTA_REGISTROS_TELEMATICOS)){
		tituloAplicacionCabecera="Aplicaci&oacute;n de Consulta de Registros Telem&aacute;ticos";
		tituloAplicacion="Entrada a la aplicaci&oacute;n de Consulta de Registros Telem&aacute;ticos";
	}
%>
<html xml:lang="es" xmlns="http://www.w3.org/1999/xhtml" lang="es">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-15" />
<meta http-equiv="Pragma" content="no-cache" /> 
<meta http-equiv="Cache-Control" content="no-cache" />		
<meta content="Ministerio de Sanidad, Servicios Sociales e Igualdad" name="description"/>
<meta content="Ministerio de Sanidad, Servicios Sociales e Igualdad" name="keywords"/>
<title>Ministerio de Sanidad, Servicios Sociales e Igualdad</title>
	<%
	String path = request.getContextPath();
	String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
	%>
	<base href="<%= basePath %>" />
	<script type="text/javascript" language="javascript" src="<%=((Context) new InitialContext().lookup("java:comp/env")).lookup("URLRoot")%>/js/idioma.js"></script>
	<link rel="stylesheet" type="text/css" href="<%=((Context) new InitialContext().lookup("java:comp/env")).lookup("URLRoot")%>/css/intranet-sigm.css" media="screen,projection" />
	<link rel="stylesheet" type="text/css" href="<%=((Context) new InitialContext().lookup("java:comp/env")).lookup("URLRoot")%>/css/intranet-sigm-imprimir.css" media="print" />
	<script type="text/javascript" language="javascript">
			//<!--
				function go() {
					window.location = '<html:rewrite href="<%=url%>"/>'
				}
			//-->
	</script>
</head>

<body>
	<div id="contenedor">
		
		<!-- Cabecera -->
		<div id="cabecera">
			<div id="escudo"><a title="Ministerio de Sanidad, Servicios Sociales e Igualdad - Gobierno de Espa&ntilde;a" href="#"><img alt="Ministerio de Sanidad, Servicios Sociales e Igualdad - Gobierno de Espa&ntilde;a" src="<%=((Context) new InitialContext().lookup("java:comp/env")).lookup("URLRoot")%>/img/logo_ministerio.jpg" /></a></div>
			<div id="titulo"><h1><%=tituloAplicacionCabecera%></h1></div>
		</div>
		<!-- Cabecera -->
		
		<!-- Menu horizontal -->
		<div id="menuhorizontal">
			<div class="izquierdo"><h2><%=tituloAplicacion%></h2></div>
			<div class="derecho">
			
			</div>
		</div>
		<!-- Fin menu horizontal -->
		<div id="login">
			<div class="usuario">
				<form >
            		<label style="position:relative; left:340px" class="error"><html:errors/></label>
					<fieldset>
						<legend><bean:message key="cambioClave.mensaje.title"/></legend>
						<label style="text-align: center;"><bean:message key="cambioClave.mensaje.resultado"/>
						</label>
						<div class="botones">
							<input type="button" class="boton" value='<bean:message key="cambioClave.aceptar" />' onclick="javascript:go()"/>
						</div>
					</fieldset>
				</form>
			</div>
			<div class="sombra"></div>
		</div>
		<!-- fin usuario-->
		
		</div>
	 <div id="pie">
		<p>&copy; Ministerio de Sanidad, Servicios Sociales e Igualdad</p>
	</div>
</body>
</html>


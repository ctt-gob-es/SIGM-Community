<%@ page import="ieci.tecdoc.sgm.core.config.ports.PortsConfig"%>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"%>

<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>

<%
String rutaEstilos = (String)session.getAttribute("PARAMETRO_RUTA_ESTILOS");
if (rutaEstilos == null) rutaEstilos = "";
String rutaImagenes = (String)session.getAttribute("PARAMETRO_RUTA_IMAGENES");
if (rutaImagenes == null) rutaImagenes = "";
%>



<html:html>
<head>




	<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-1" />
	<title>
		<bean:message key="listadoExpedientes"/>
	</title>
	
	<link href="css/<%=rutaEstilos%>estilos.css" rel="stylesheet" type="text/css" />

	<!--[if lte IE 5]>
		<link rel="stylesheet" type="text/css" href="css/estilos_ie5.css"/>
	<![endif]-->	

	<!--[if IE 6]>
		<link rel="stylesheet" type="text/css" href="css/estilos_ie6.css"/>
	<![endif]-->	
	
	<!--[if IE 7]>
		<link rel="stylesheet" type="text/css" href="css/estilos_ie7.css"/>
	<![endif]-->	


	<script type="text/javascript" language="javascript" src="js/navegador.js"></script>
	<script type="text/javascript" language="javascript" src="js/idioma.js"></script>
	<script type="text/javascript" language="javascript" src="js/utilsTabla.js"></script>

	<%
	boolean logado = true;
	String sessionIdIni = "";
   	sessionIdIni = (String)session.getAttribute("SESION_ID");
	if(sessionIdIni == null || sessionIdIni.equals("") || sessionIdIni.equals("null")){
		String url_parcial = (String)request.getSession().getServletContext().getAttribute("redirAutenticacion");
		String http_port = PortsConfig.getHttpPort();
		String dir = "http://"+request.getServerName()+":"+http_port+"/" + url_parcial;
		logado = false;
	%>
		<META HTTP-EQUIV="Refresh" CONTENT="0;URL=<%=dir%>">
	<%
	}
	%>
</head>
<body>

<%if (logado){%>

	<div id="contenedora">
		<jsp:include flush="true" page="Cabecera.jsp"></jsp:include>
		
		
		<div class="centered">
		<div class="contenedor_centrado">


			<div class="cuerpo">
      			<div class="cuerporight">
        			<div class="cuerpomid">

				<h1><bean:message key="expedientesInteresado"/></h1>

				<div class="tabs_submenus">
					<ul>
						<li class="subOn">
							<h3><a><bean:message key="listaExpedientes"/></a></h3>
						</li>
						<!-- Ticket 858 INICIO -->
						<li class="subOff">
							<h3><a href="ListadoActasAudio.do"><bean:message key="listadoActas"/></a></h3>
						</li>
						<!-- Ticket 858 FIN-->
					</ul>
				</div>

				<div class="cuerpo_subs clearfix" >


					<!--bean:message key="application.consultaExpedientesDe"/-->
					  
					<!--% 
						java.util.Locale myLocale = (java.util.Locale)session.getAttribute("org.apache.struts.action.LOCALE");
						out.write(myLocale.toString());
					%-->
			
				
					<%if(request.getAttribute("MENSAJE_ERROR") != null){%>
						<label class="error"><%=request.getAttribute("MENSAJE_ERROR")%></label>
					<%}%>
		
					<div class="seccion">

					 <%-- comprobamos si existe algun expediente, en el caso que no sea asi desplegamos mensaje  --%>
					 <logic:empty name="ListaExpedientes" property="expedientes">
						 <label class="error"><bean:message key="noHayNingunExpediente"/></label>
					 </logic:empty>
					 
					 <logic:notEmpty name="ListaExpedientes" property="expedientes">
					 	 <% int i = 0; %>
						 <display:table name="expedientes" class="table-display-tag" uid="expediente" 
							pagesize="10" 
							requestURI="ListaExpedientes.do"
							sort="list" >
								<display:column paramId="tabla<%=i%>" paramName="tabla<%=i%>"  media="html" titleKey="numeroExpediente" sortable="true" headerClass="cabeceraTabla" sortProperty="numero" comparator="es.dipucr.tecdoc.sgm.ct.utilities.NumexpComparator">
									<table id="tabla0<%=i%>" width="100%" height="100%" onmouseover="javascript:changeColor(<%=i%>, 'true');" onmouseout="javascript:changeColor(<%=i%>, 'false');">
										<tr height="100%" style="cursor:pointer;" onClick="document.location.href='DetalleExpediente.do?do=DetalleExpediente&id=<bean:write name="expediente" property="numero" />&registro=<bean:write name="expediente" property="numeroRegistro" />&busqueda=false&nombre=<bean:write name="expediente" property="procedimiento" /> - <bean:write name="expediente" property="fechaConvocatoria" format="dd/MM/yyyy" />';">
											<td>
												<bean:write name="expediente" property="numero" />
											</td>
										</tr>
									</table>
								</display:column>
								<display:column paramId="tabla<%=i%>" paramName="tabla<%=i%>"  media="html" titleKey="procedimiento" sortable="true" headerClass="cabeceraTabla" sortProperty="procedimiento">
									<table id="tabla1<%=i%>" width="100%" height="100%" onmouseover="javascript:changeColor(<%=i%>, 'true');" onmouseout="javascript:changeColor(<%=i%>, 'false');">
										<tr height="100%" style="cursor:pointer;" onClick="document.location.href='DetalleExpediente.do?do=DetalleExpediente&id=<bean:write name="expediente" property="numero" />&registro=<bean:write name="expediente" property="numeroRegistro" />&busqueda=false&nombre=<bean:write name="expediente" property="procedimiento" /> - <bean:write name="expediente" property="fechaConvocatoria" format="dd/MM/yyyy" />';">
											<td>
												<bean:write name="expediente" property="procedimiento" />
											</td>
										</tr>
									</table>
								</display:column>
								<display:column paramId="tabla<%=i%>" paramName="tabla<%=i%>"  media="html" titleKey="fechaConvocatoria" sortable="true" headerClass="cabeceraTabla" sortProperty="procedimiento" format="{0,date,dd-MM-yyyy}">
									<table id="tabla2<%=i%>" width="100%" height="100%" onmouseover="javascript:changeColor(<%=i%>, 'true');" onmouseout="javascript:changeColor(<%=i%>, 'false');">
										<tr height="100%" style="cursor:pointer;" onClick="document.location.href='DetalleExpediente.do?do=DetalleExpediente&id=<bean:write name="expediente" property="numero" />&registro=<bean:write name="expediente" property="numeroRegistro" />&busqueda=false&nombre=<bean:write name="expediente" property="procedimiento" /> - <bean:write name="expediente" property="fechaConvocatoria" format="dd/MM/yyyy" />';">
											<td>
												<bean:write name="expediente" property="fechaConvocatoria" format="dd/MM/yyyy" />
											</td>
										</tr>
									</table>								
								</display:column>
								<display:column paramId="tabla<%=i%>" paramName="tabla<%=i%>"  media="html" titleKey="horaConvocatoria" sortable="true" headerClass="cabeceraTabla" sortProperty="procedimiento">
									<table id="tabla3<%=i%>" width="100%" height="100%" onmouseover="javascript:changeColor(<%=i%>, 'true');" onmouseout="javascript:changeColor(<%=i%>, 'false');">
										<tr height="100%" style="cursor:pointer;" onClick="document.location.href='DetalleExpediente.do?do=DetalleExpediente&id=<bean:write name="expediente" property="numero" />&registro=<bean:write name="expediente" property="numeroRegistro" />&busqueda=false&nombre=<bean:write name="expediente" property="procedimiento" /> - <bean:write name="expediente" property="fechaConvocatoria" format="dd/MM/yyyy" />';">
											<td>
												<bean:write name="expediente" property="horaConvocatoria" />
											</td>
										</tr>
									</table>
								</display:column>
								<% i++; %>
						</display:table>						

					 </logic:notEmpty>
					 
					 <%-- end interate --%>
					</div>


				</div>
			</div>
      			</div>
    		</div>
    		<div class="cuerpobt">
      			<div class="cuerporightbt">
        			<div class="cuerpomidbt">&nbsp;</div>
      			</div>
		</div>		


	    	</div>
	    	 <%@ include file="Pie.jsp" %>
    		</div>
   		
    	</div>



  <!-- Fin Contenido -->
  
  <script language="javascript">
  	
  </script>
  <%}else{%>
	<p><bean:message key="cargando"/></p>
  <%}%>
</body>
</html:html> 
<%@ page import="ieci.tecdoc.sgm.core.config.ports.PortsConfig"%>
<%@ page import="java.util.*" %>
<%@ page import="ieci.tecdoc.sgm.core.services.consulta.*" %>


<%@ page language="java" contentType="text/html; charset=ISO-8859-1"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>
<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="http://displaytag.sf.net" prefix="display" %>
<%@ taglib uri="http://java.sun.com/jstl/core_rt" prefix="c"%>


<%@page import="java.util.Locale"%>

<%
String rutaEstilos = (String)session.getAttribute("PARAMETRO_RUTA_ESTILOS");
if (rutaEstilos == null) rutaEstilos = "";
String rutaImagenes = (String)session.getAttribute("PARAMETRO_RUTA_IMAGENES");
if (rutaImagenes == null) rutaImagenes = "";

Locale idioma = (Locale)session.getAttribute("org.apache.struts.action.LOCALE");
String lang = idioma.getLanguage();
String country = idioma.getCountry();
%>

<html:html> 

<head>
	<title><bean:message key="listaExpedientes"/></title>
	<link href="css/<%=rutaEstilos%>estilos.css" rel="stylesheet" type="text/css" />

	<script type="text/javascript" language="javascript" src="js/idioma.js"></script>
	
	<script language='JavaScript' type='text/javascript'><!--
	
		function cargaDocumento(guid, nombreDoc)
		{
			document.detalleDoc.guid.value = guid;
			document.detalleDoc.nombreDoc.value = nombreDoc;
			document.detalleDoc.action = "RecogerDocumento.do";
			document.detalleDoc.submit();
		     
		}
	
	//--></script>
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
<form name="detalleDoc"  method="post">
<input name="guid" type="hidden" value=""> 
<input name="nombreDoc" type="hidden" value=""> 
<%if (logado){%>

	<div id="contenedora">
		<jsp:include flush="true" page="Cabecera.jsp"></jsp:include>
		<div style="text-align:center;">
			<div class="contenedor_centrado">
				<div class="cuerpo">
	      			<div class="cuerporight">
	        			<div class="cuerpomid">
	        				<h1>Listado Actas Audio</h1>		
							<div class="tabs_submenus">
								<ul>
									<li class="subOff">
										<h3><a href="ListaExpedientes.do"><bean:message key="listaExpedientes"/></a></h3>
									</li>
									<li class="subOn">
										<h3><a><bean:message key="listadoActas"/></a></h3>
									</li>
								</ul>
							</div>
							
							<div class="cuerpo_subs clearfix" >
								<div class="seccion">
									<%if(request.getAttribute("MENSAJE_ERROR") != null){%>
										<label class="error_rojo"><%=request.getAttribute("MENSAJE_ERROR")%></label>
									<%}%>

									<table class="tablaListado">
										<logic:notEmpty name="ListadoActasAudio" property="actas">
											<tr> 
												<th class="cabeceraTabla anchoDes">Extracto Acta Audio</th>
											</tr>
											<!-- TABLA DE actas -->
											<bean:define name="ListadoActasAudio" id="actas" type="Actas" property="actas"/>
											<%
												List listadoactas = (List)actas.getActas();
												Iterator iterProp = listadoactas.iterator();
												while (iterProp.hasNext()){
													Acta propues = (Acta)iterProp.next();
													FicheroPropuesta ficheros = (FicheroPropuesta)propues.getDocumentoActa();
													%>
													<tr>
														<td class="borde">														
															<%=propues.getExtracto()%>
															<a onclick="javascript:cargaDocumento('<%=ficheros.getGuid()%>','<%=ficheros.getTitulo()%>')"  class="menuhead" target="_blank" >
															<img src="/SIGEM_TramitacionWeb/ispac/skin1/img/viewDoc.gif" border="0" style="cursor:pointer" title="Ver documento"/>
															</a>
														</td>			
													</tr>
													<%	
												}
											%>
										</logic:notEmpty>
									</table>
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
<%}else{%>
	<p><bean:message key="cargando"/></p>
<%}%>

</form>
</body>
</html:html> 
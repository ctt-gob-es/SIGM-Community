<%@ page import="ieci.tecdoc.sgm.core.config.ports.PortsConfig"%>
<%@ page language="java"%>
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
	<title><bean:message key="busquedaExpedientes"/></title>
	<link href="css/<%=rutaEstilos%>estilos.css" rel="stylesheet" type="text/css" />

	<!--[if lte IE 5]>
		<link rel="stylesheet" type="text/css" href="css/<%=rutaEstilos%>estilos_ie5.css"/>
	<![endif]-->	

	<!--[if IE 6]>
		<link rel="stylesheet" type="text/css" href="css/<%=rutaEstilos%>estilos_ie6.css"/>
	<![endif]-->	
	
	<!--[if IE 7]>
		<link rel="stylesheet" type="text/css" href="css/<%=rutaEstilos%>estilos_ie7.css"/>
	<![endif]-->	

	<link rel="stylesheet" type="text/css" media="all" href="calendario/skins/aqua/theme.css" title="Aqua" />
	<!-- import the calendar script -->
	<script type="text/javascript" src="calendario/calendar.js"></script>
	<script type="text/javascript" src="calendario/lang/calendar-es.js"></script>
	<script type="text/javascript" src="calendario/calendario.js"></script>
	<script type="text/javascript" language="javascript" src="js/idioma.js"></script>
	<script type="text/javascript" language="javascript" src="js/utilsTabla.js"></script>
	
	<script language="javascript">
	
		var oldLink = null;
		//setActiveStyleSheet(document.getElementById("defaultTheme"), "Aqua");
		
		function limpiarFormulario(){
			var fd = document.getElementById('fechaDesde');
			var fhs = document.getElementById('fechaHasta');
			var fh = document.getElementById('fechaH');
			var est = document.getElementById('estado');
			var op = document.getElementById('operadorConsulta');

	  		fd.value="";
	  		fhs.value="";
			fh.style.visibility = "hidden";
			est.selectedIndex=0;
			op.selectedIndex=0;
		}
		
		function mostrarHasta(){

	  		var seleccion = document.getElementById('operadorConsulta');
			var indice = seleccion.selectedIndex ;
	
	  		var fh = document.getElementById('fechaH');
			
	
		  	if (seleccion.options[indice].value == 'entre'){
	  			fh.style.visibility = "visible";
	  		} else {
	  			fh.style.visibility= "hidden";
	  		}
  		}
		
		
		function validarFormulario() {
			var campoFechaDesde = document.forms[0].fechaDesde;
			var campoFechaHasta = document.forms[0].fechaHasta;
			var campoOperadorConsulta = document.forms[0].operadorConsulta;
			var campoEstado = document.forms[0].estado;
			
			if (campoOperadorConsulta.value == "" && (campoFechaHasta.value != "" || campoFechaDesde.value != "")) {
				alert("<bean:message key="tipoFecha"/>");
				return false;
			} else if (campoEstado.value == "" && (campoOperadorConsulta.value == "" || (campoOperadorConsulta.value != "" && campoFechaDesde.value == ""))){
				alert("<bean:message key="rellenarCampos"/>");
				return false;
			} else if (campoOperadorConsulta.value == "entre" && campoFechaHasta.value == "") {
				alert("<bean:message key="campoFechaHasta"/>");
				return false;
			} else if(campoOperadorConsulta.value != "" && campoFechaDesde.value == "") {
				alert("<bean:message key="formatoFechaIncorrecto"/>");
				return false;
			} else if(campoOperadorConsulta.value == "entre" && fechaAnterior(campoFechaDesde.value, campoFechaHasta.value) == false){
				alert("<bean:message key="finalMenor"/>");
				return false;
			}
		}
		
		
		function fechaAnterior(fechaInicio, fechaFin){
			if (fechaInicio == "" || fechaFin == "")
				return false;
			
			var iDia = fechaInicio.substr(0,2);
			var iMes = fechaInicio.substr(3,2);
			var iAnio = fechaInicio.substr(6,4);
			var fDia = fechaFin.substr(0,2);
			var fMes = fechaFin.substr(3,2);
			var fAnio = fechaFin.substr(6,4);
			
			var iFecha = iAnio + iMes + iDia;
			var fFecha = fAnio + fMes + fDia;
			
			if (iFecha > fFecha)
				return false;
			return true;
		}
		
	</script>
	
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
						<li class="subOff">
							<h3><a href="ListaExpedientes.do"><bean:message key="listaExpedientes"/></a></h3>
						</li>
					</ul>
				</div>

				<div class="cuerpo_subs clearfix" >

					<div class="seccion">
						<html:messages id="error">
							 <label class="error_rojo"><bean:write name="error"/></label>
						</html:messages> 
						<%if(request.getAttribute("MENSAJE_ERROR") != null){%>
							 <label class="error_rojo"><%=request.getAttribute("MENSAJE_ERROR")%></label>
						<%}%>
						<form action="ListaExpedientes.do" method="post">

						<div class="cuerpo_sec fila_sub clearfix">
							<ul>
								<li>
									<input type="button" value='<bean:message key="limpiar"/>' id="limpiar_busqueda" class="ok" onclick="javascript:limpiarFormulario();"/>
								</li>
								<li>
									<input type="submit" value='<bean:message key="buscar"/>' onClick="javascript: return validarFormulario();" id="busqueda_expedientes" class="ok" />
								</li>
							</ul>
						</div>

						<p class="fila_sub">
			                		<label class="sml"><bean:message key="NIF"/>: </label>
							<html:text property="NIF" size="2" styleClass="sml disabled" name="BusquedaExpedientes" disabled="true"></html:text>
						</p>
						<p class="fila_sub">
					             	<%String op = (String)session.getAttribute("operadorConsulta"); %>
					                <label for="fecha" class="sml"><bean:message key="fecha"/>: </label>
							<select id="operadorConsulta" name="operadorConsulta" class="single" onChange="javascript:mostrarHasta()">
								<option value="" selected></option>
								<option value="&lt;" <%=("<".equals(op))?"selected":""%>>&lt;</option>
								<option value="=" <%=("=".equals(op))?"selected":""%>>=</option>
								<option value="&gt;" <%=(">".equals(op))?"selected":""%>>&gt;</option>
								<option value="entre" <%=("entre".equals(op))?"selected":""%>><bean:message key="entre"/></option>
							</select>

							<%String fd = (String)session.getAttribute("fechaDesdeBusqueda"); %>
							<input type="text" name="fechaDesde" id="fechaDesde" class="sml disabled" maxlength="10" READONLY value='<%=(fd!=null && !"null".equals(fd))?fd:""%>'/><img src="img/<%=rutaImagenes%>calendario.bmp" onclick="return showCalendar('fechaDesde', '%d-%m-%Y');" style="vertical-align: bottom; cursor: hand;" /><span name="Desde" id="f"/></spanp>


							<%String fh = (String)session.getAttribute("fechaHastaBusqueda"); %>
							<span id="fechaH" style="visibility:hidden">
								<label class="frase xsml"><bean:message key="hasta"/>:</label>
								<input type="text" name="fechaHasta" id="fechaHasta" class="sml disabled" maxlength="10" READONLY value='<%=(fh!=null && !"null".equals(fh))?fh:""%>'/><img id="botonFechaHasta" src="img/<%=rutaImagenes%>calendario.bmp" onclick="return showCalendar('fechaHasta', '%d-%m-%Y');" style="vertical-align: bottom; cursor: hand;" /><span name="Desde" id="f"></span>
							</span>
						</p>
						<p class="fila_sub">
							<%String est = (String)session.getAttribute("estado"); %>
							<label class="sml" ><bean:message key="estado"/>: </label>
							<select id="estado" name="estado" class="single" >
								<option value="" selected></option>
								<option value="0" <%=("0".equals(est))?"selected":""%>><bean:message key="abierto"/></option>
								<option value="1" <%=("1".equals(est))?"selected":""%>><bean:message key="cerrado"/></option>
							</select>
						</p>
						</form>
					</div>
					<%if("true".equals(request.getAttribute("buscado"))){%>
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
										export="false" 
										sort="list">
										<display:column paramId="tabla<%=i%>" paramName="tabla<%=i%>"  media="html" titleKey="numeroExpediente" sortable="true" headerClass="cabeceraTabla" sortProperty="numero" comparator="ieci.tecdoc.sgm.ct.utilities.NumexpComparator">
											<table id="tabla0<%=i%>" width="100%" height="100%" onmouseover="javascript:changeColor(<%=i%>, 'true');" onmouseout="javascript:changeColor(<%=i%>, 'false');">
												<tr style="cursor:pointer;" onClick="document.location.href='DetalleExpediente.do?do=DetalleExpediente&id=<bean:write name="expediente" property="numero" />&registro=<bean:write name="expediente" property="numeroRegistro" />&busqueda=true';">
													<td>
														<bean:write name="expediente" property="numero" />
													</td>
												</tr>
											</table>
										</display:column>
										<display:column media="html" titleKey="procedimiento" sortable="true" headerClass="cabeceraTabla" sortProperty="procedimiento">
											<table id="tabla1<%=i%>" width="100%" height="100%" onmouseover="javascript:changeColor(<%=i%>, 'true');" onmouseout="javascript:changeColor(<%=i%>, 'false');">
												<tr style="cursor:pointer;" onClick="document.location.href='DetalleExpediente.do?do=DetalleExpediente&id=<bean:write name="expediente" property="numero" />&registro=<bean:write name="expediente" property="numeroRegistro" />&busqueda=true';">
													<td>
														<bean:write name="expediente" property="procedimiento" />
													</td>
												</tr>
											</table>
										</display:column>
										<display:column media="html" titleKey="fechaIniciacion" sortable="true" headerClass="cabeceraTabla" style="width:15%" sortProperty="fechaInicio" format="{0,date,yyyy-MM-dd}">
											<table id="tabla2<%=i%>" width="100%" height="100%" onmouseover="javascript:changeColor(<%=i%>, 'true');" onmouseout="javascript:changeColor(<%=i%>, 'false');">
												<tr style="cursor:pointer;" onClick="document.location.href='DetalleExpediente.do?do=DetalleExpediente&id=<bean:write name="expediente" property="numero" />&registro=<bean:write name="expediente" property="numeroRegistro" />&busqueda=true';">
													<td>
														<bean:write name="expediente" property="fechaInicio" format="dd/MM/yyyy" />
													</td>
												</tr>
											</table>								
										</display:column>
										<display:column media="html" titleKey="estado" sortable="true" headerClass="cabeceraTabla" sortProperty="estado">
											<table id="tabla3<%=i%>" width="100%" height="100%" onmouseover="javascript:changeColor(<%=i%>, 'true');" onmouseout="javascript:changeColor(<%=i%>, 'false');">
												<tr style="cursor:pointer;" onClick="document.location.href='DetalleExpediente.do?do=DetalleExpediente&id=<bean:write name="expediente" property="numero" />&registro=<bean:write name="expediente" property="numeroRegistro" />&busqueda=true';">
													<td>
														<logic:equal name="expediente" property="estado" value="cerrado">
															<bean:message key="cerrado" />	
														</logic:equal>
														<logic:notEqual name="expediente" property="estado" value="cerrado">
															<bean:message key="abierto" />	
														</logic:notEqual>
													</td>
												</tr>
											</table>								
										</display:column>
										<display:column media="html" titleKey="avisos" sortable="false" headerClass="cabeceraTabla">
											<table id="tabla4<%=i%>" width="100%" height="100%" onmouseover="javascript:changeColor(<%=i%>, 'true');" onmouseout="javascript:changeColor(<%=i%>, 'false');">
												<tr style="cursor:pointer;" onClick="document.location.href='DetalleExpediente.do?do=DetalleExpediente&id=<bean:write name="expediente" property="numero" />&registro=<bean:write name="expediente" property="numeroRegistro" />&busqueda=true';">
													<td align="center">
														<logic:equal name="expediente" property="aportacion" value="S">
															<img src="img/<%=rutaImagenes%>documento.jpg" alt='<bean:message key="subsanacion" />' />
														</logic:equal>
														<logic:equal name="expediente" property="notificacion" value="S">
															<logic:equal name="expediente" property="aportacion" value="S">&nbsp;&nbsp;&nbsp;&nbsp;</logic:equal>
															<img src="img/<%=rutaImagenes%>correos.jpg" alt='<bean:message key="notificacion" />' />
														</logic:equal>
														<logic:equal name="expediente" property="pagos" value="S">
															<logic:equal name="expediente" property="aportacion" value="S">&nbsp;&nbsp;&nbsp;&nbsp;</logic:equal>
															<logic:notEqual name="expediente" property="aportacion" value="S">
																<logic:equal name="expediente" property="notificacion" value="S">, </logic:equal>
															</logic:notEqual>
															<img src="img/<%=rutaImagenes%>billete.jpg" alt='<bean:message key="pago" />' />
														</logic:equal>
														<logic:notEqual name="expediente" property="aportacion" value="S">
															<logic:notEqual name="expediente" property="notificacion" value="S">
																<logic:notEqual name="expediente" property="pagos" value="S">
																	&nbsp;
																</logic:notEqual>
															</logic:notEqual>
														</logic:notEqual>
													</td>
												</tr>	
											</table>
										</display:column>
										<% i++; %>
									</display:table>						
								 </logic:notEmpty>
							</table>
						</div>
					<%}%>
		  			<!--Fin cuadro central con formulario-->



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
    		</div>
    	</div>

		 

  	<%@ include file="Pie.jsp" %>
  	
  	<script language="Javascript">

  		var fh = document.getElementById('fechaH');

 		var op = document.getElementById('operadorConsulta');
		var indice = op.selectedIndex ;

	  	if (op.options[indice].value == 'entre'){
  			fh.style.visibility = "visible";
  		} else {
  			fh.style.visibility= "hidden";
  		}
  	</script>
  	<%}else{%>
    	<p><bean:message key="cargando"/></p>
    <%}%>
</body>

</html:html> 
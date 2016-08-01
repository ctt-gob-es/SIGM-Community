<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>
<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display"%>

<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>
	<head>
	<meta http-equiv="Content-Type" content="text/html">
	<title>Página de búsqueda</title>
	<link rel="stylesheet" href='<ispac:rewrite href="css/styles.css"/>' />
	<link rel="stylesheet" href='<ispac:rewrite href="css/estilos.css"/>' />
	
	<script type="text/javascript"
		src='<ispac:rewrite href="../scripts/utilities.js"/>'> </script>
	
	<!--[if lte IE 5]>
	 		<link rel="stylesheet" type="text/css" href='<ispac:rewrite href="css/estilos_ie5.css"/>'/>
	
		<![endif]-->
	
	<!--[if IE 6]>
			<link rel="stylesheet" type="text/css" href='<ispac:rewrite href="css/estilos_ie6.css"/>'>
	
		<![endif]-->
	
	<!--[if IE 7]>
			<link rel="stylesheet" type="text/css" href='<ispac:rewrite href="css/estilos_ie7.css"/>'>
		
		<![endif]-->
	
	<script type="text/javascript"
		src='<ispac:rewrite href="../scripts/jquery-1.3.2.min.js"/>'></script>
	<script type="text/javascript"
		src='<ispac:rewrite href="../scripts/jquery-ui-1.7.2.custom.min.js"/>'></script>
	<script type="text/javascript"
		src='<ispac:rewrite href="../scripts/jquery.alerts.js"/>'></script>
	<script type="text/javascript"
		src='<ispac:rewrite href="../scripts/sorttable.js"/>'></script>
	
	<%
		// Nombre la variable de sesión donde se han salvado
		// los parámetros que utiliza el tag actionframe
		String parameters = request.getParameter("parameters");
		String multivalueId = request.getParameter("multivalueId");
		String taskId = (String)request.getAttribute("taskId");
		String numexp = (String)request.getAttribute("numexp");
		//id de la Entidad SVD
		String entity = (String)request.getAttribute("entidad");
	%>
	
	<ispac:rewrite id="setProcedimiento" action="cargaDatosServicios.do" />
	
	<script language='JavaScript' type='text/javascript'><!--
				
			function SelectValue(procedimiento, nombre) {
				document.selectvalue.action = '<%=setProcedimiento%>' + "?procedimiento="+procedimiento+"&nombre="+nombre;
				document.selectvalue.submit();
			}
			function volverInicio() {
				document.selectvalue.action = "showTask.do?taskId=<%=taskId%>&entity=<%=entity%>&numexp=<%=numexp%>";
				document.selectvalue.submit();
			}
				
	//--></script>
	
	</head>
	
	<body>
	<div id="contenido" class="move">
		<div class="ficha">
			<div class="encabezado_ficha">
				<div class="titulo_ficha">
				<h4>Procedimientos</h4>
				<div class="acciones_ficha">
					<input type="button"
						value='<bean:message key="common.message.close"/>' class="btnCancel"
						onclick="javascript:volverInicio()" />

				</div>
				<%--fin acciones ficha --%></div>
				<%--fin titulo ficha --%>
			</div>
			<%--fin encabezado ficha --%>
			
			<div class="cuerpo_ficha">
			<div class="seccion_ficha">
			<form name="selectvalue" method="post">
				<input type="hidden" name="entity" value='<%=entity%>' /> 
				<input type="hidden" name="parameters" value='<%=parameters%>' /> 
				<input type="hidden" name="multivalueId" value='<%=multivalueId%>' /> 
				<!-- displayTag con formateador -->
				<bean:define name="Formatter" id="formatter"
					type="ieci.tdw.ispac.ispaclib.bean.BeanFormatter" /> <display:table
					name="ValueList" id="value" export='<%=formatter.getExport()%>'
					class='<%=formatter.getStyleClass()%>' sort='<%=formatter.getSort()%>'
					pagesize='<%=formatter.getPageSize()%>'
					defaultorder='<%=formatter.getDefaultOrder()%>'
					defaultsort='<%=formatter.getDefaultSort()%>' requestURI=''>
				
					<logic:iterate name="Formatter" id="format"
						type="ieci.tdw.ispac.ispaclib.bean.BeanPropertyFmt">
				
						<!-- ENLACE -->
						<logic:equal name="format" property="fieldType" value="LINK">
				
							<display:column titleKey='<%=format.getTitleKey()%>'
								media='<%=format.getMedia()%>'
								headerClass='<%=format.getHeaderClass()%>'
								sortable='<%=format.getSortable()%>'
								sortProperty='<%=format.getPropertyName()%>'
								class='<%=format.getColumnClass()%>'
								decorator='<%=format.getDecorator()%>'
								comparator='<%=format.getComparator()%>'>
				
								<bean:define name="value" property="property(NOMBRE)" id="nombre" />
								<bean:define name="value" property="property(PROCEDIMIENTO)" id="procedimiento" />
								<bean:define name="value" property="property(EMISOR)" id="emisor" />
								<bean:define name="value" property="property(DNI)" id="dni" />
								<bean:define name="value" property="property(NOMBREDNI)" id="nombreDni" />
				
								<html:hidden property="dniValue" value="<%=dni.toString()%>" />
								<html:hidden property="nombreDniValue" value="<%=nombreDni.toString()%>" />
								<html:hidden property="emisorServicio" value="<%=emisor.toString()%>" />
				
				
								<a class="element"
									href="javascript:SelectValue('<%=procedimiento.toString()%>', '<%=nombre.toString()%>')">
								<%=format.formatProperty(value)%> </a>
				
				
							</display:column>
				
						</logic:equal>
				
						<!-- DATO DE LA LISTA -->
						<logic:equal name="format" property="fieldType" value="LIST">
				
							<display:column titleKey='<%=format.getTitleKey()%>'
								media='<%=format.getMedia()%>'
								headerClass='<%=format.getHeaderClass()%>'
								sortable='<%=format.getSortable()%>'
								sortProperty='<%=format.getPropertyName()%>'
								class='<%=format.getColumnClass()%>'
								decorator='<%=format.getDecorator()%>'
								comparator='<%=format.getComparator()%>'>
				
								<%=format.formatProperty(value)%>
				
							</display:column>
				
						</logic:equal>
				
					</logic:iterate>
				
				</display:table>
			</form>
			</div>
			</div>
		</div>
	</div>
	</body>
</html>

<script>
	positionMiddleScreen('contenido');
	$(document).ready(function(){
		$("#contenido").draggable();
	});
</script>
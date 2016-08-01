<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>
<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c"%>

<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html">
<title>Página de búsqueda</title>
<link rel="stylesheet" href='<ispac:rewrite href="css/styles.css"/>' />
<link rel="stylesheet" href='<ispac:rewrite href="css/estilos.css"/>' />
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
<script type="text/javascript" src='<ispac:rewrite href="../scripts/utilities.js"/>'></script>
	
<%
			// Entidad de sustituto
			String entity = request.getParameter("entity");
			
			// Nombre la variable de sesión donde se han salvado
			// los parámetros que utilizará el updateFields
			String parameters = request.getParameter("parameters");
						
			
			String multivalueId = request.getParameter("multivalueId");
			
			String entidad = request.getParameter("multivalueId");
			
			// Action a ejecutar al seleccionar
			String setAction = request.getParameter("setAction");
			if (setAction == null) {
				
				setAction = "setSubstituteContratacion.do";
			}
			
			// Action a ejecutar al buscar
			String searchAction = request.getParameter("searchAction");
			if (searchAction == null) {
				
				searchAction = "searchSubstitute.do";
			}
			
			// Clave para el título de la pantalla
			String captionKey = request.getParameter("captionKey");
			if (captionKey == null) {
				
				captionKey = "select.substitute.title";
			}
			
			// Mostrar el buscador
			String showSearch = request.getParameter("showSearch");
			if (showSearch == null) {
				
				showSearch = "true";
			}
						
			String displayTagURI = searchAction + "?entity=" + entity 
								 + "&parameters=" + parameters
			 					 + "&setAction=" + setAction
			 					 + "&searchAction=" + searchAction
			 					 + "&captionKey=" + captionKey
			 					 + "&showSearch=" + showSearch;
			
			// Clave para la etiqueta del buscador
			String searchKey = request.getParameter("searchKey");
			if (showSearch.equals("true")) {

				if (searchKey == null) {
					
					searchKey = "search.generic.containsTextSustituto";
				}
				
				displayTagURI += "&searchKey=" + searchKey;
			}
			else {
				searchKey = "";
			}

			// Parámetros de búsqueda
			String operador = (String) request.getAttribute("operador");
			if (operador != null) {
				
				displayTagURI += "&operador=" + operador;
			}
			else {
				operador = "";
			}
			String criterio = (String) request.getAttribute("criterio");	
			if (criterio != null) {
				
				displayTagURI += "&criterio=" + criterio;
			}
			else {
				criterio = "";
			}
			
			// Formateador para la lista
			String xmlFormatter = request.getParameter("xmlFormatter");
			if ((xmlFormatter != null) &&
				(xmlFormatter != "")) {
				
				displayTagURI += "&xmlFormatter=" + xmlFormatter;
			}
			else {
				xmlFormatter = "";
			}

			// Valor del campo implicado en la búsqueda
			String field = request.getParameter("field");
			if ((field != null) &&
				(field != "")) {

				displayTagURI += "&field=" + field;
			}
			else {
				field = "";
			}
		%>

<ispac:rewrite id="searchSubstitute" action='<%=searchAction%>' />
<ispac:rewrite id="setSubstitute" action='<%=setAction%>' />

<script language='JavaScript' type='text/javascript'><!--
			
			function SelectSubstitute(value, sustituto, subtipoCont) {
				if(subtipoCont != null){
					document.defaultForm.action = '<%=setSubstitute%>' + "?value=" + value +"&sustituto=" + sustituto +"&subtipoCont=" + subtipoCont;
				}
				else{
					document.defaultForm.action = '<%=setSubstitute%>' + "?value=" + value +"&sustituto=" + sustituto;
				}
				document.defaultForm.submit();
			}
			
		//--></script>

</head>

<body>
<div id="contenido" class="move">
	<div class="ficha">
		<div class="encabezado_ficha">
			<div class="titulo_ficha">
				<h4><bean:message key='<%=captionKey%>' /></h4>
			<div class="acciones_ficha">
				<input type="button"
					value='<bean:message key="common.message.close"/>' class="btnCancel"
					onclick='<ispac:hideframe/>' />
			</div><%--fin acciones ficha --%>
		</div><%--fin titulo ficha --%>
		</div><%--fin encabezado ficha --%>

	<div class="cuerpo_ficha">
		<div class="seccion_ficha">
			<div class="cabecera_seccion">
						<h4><bean:message key='<%=searchKey%>' /> </h4>
			</div>
			<html:form action='<%=searchAction%>'
			method="post">
				<input type="hidden" name="entity" value='<%=entity%>' />
				<input type="hidden" name="parameters" value='<%=parameters%>' />
				<input type="hidden" name="multivalueId" value='<%=multivalueId%>' />
				<input type="hidden" name="setAction" value='<%=setAction%>' />
				<input type="hidden" name="searchAction" value='<%=searchAction%>' />
				<input type="hidden" name="captionKey" value='<%=captionKey%>' />
				<input type="hidden" name="xmlFormatter" value='<%=xmlFormatter%>' />
				<input type="hidden" name="showSearch" value='<%=showSearch%>' />
				<input type="hidden" name="searchKey" value='<%=searchKey%>' />
				<input type="hidden" name="operador" value='<%=operador%>' />
				<input type="hidden" name="criterio" value='<%=criterio%>' />
				<input type="hidden" name="field" value='<%=field%>' />
			
				<c:set var="_filterId"><c:out value="${param.filterId}" default=""/></c:set>
				<jsp:useBean id="_filterId" type="java.lang.String"/>
				<c:set var="_hierarchicalName"><c:out value="${param.hierarchicalName}" default=""/></c:set>
				<jsp:useBean id="_hierarchicalName" type="java.lang.String"/>
				<c:set var="_loadNotAssociated"><c:out value="${param.loadNotAssociated}" default=""/></c:set>
				<jsp:useBean id="_loadNotAssociated" type="java.lang.String"/>
				
				<%-- Añadimos  el parametro hierarchialId para mantener compatibilidad con versiones anteriores --%>
				<c:set var="_hierarchicalId"><c:out value="${param.hierarchicalId}" default=""/></c:set>
				<jsp:useBean id="_hierarchicalId" type="java.lang.String"/>


				
				<input type="hidden" name="filterId" value='<%=_filterId%>'/>
				<input type="hidden" name="hierarchicalName" value='<%=_hierarchicalName%>'/>
				<input type="hidden" name="hierarchicalId" value='<%=_hierarchicalId%>'/>
				<input type="hidden" name="loadNotAssociated" value='<%=_loadNotAssociated%>'/>
			
				<c:set var="_showSearch"><%=showSearch%></c:set>
				
				<!-- displayTag con formateador -->
				<bean:define name="Formatter" id="formatter"
					type="ieci.tdw.ispac.ispaclib.bean.BeanFormatter" />
			<div class="tableDisplay">
				<display:table name="SubstituteList" id="substitute"
					export='<%=formatter.getExport()%>'
					class='<%=formatter.getStyleClass()%>' sort='<%=formatter.getSort()%>'
					pagesize='<%=formatter.getPageSize()%>'
					defaultorder='<%=formatter.getDefaultOrder()%>'
					defaultsort='<%=formatter.getDefaultSort()%>'
					requestURI='<%=displayTagURI%>' excludedParams="*">
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
								<bean:define name="substitute" property="property(VALOR)" id="value" />
								<bean:define name="substitute" property="property(SUSTITUTO)" id="sustituto"/>
								<bean:define name="substitute" property="property(CONTRATO_SUMIN)" id="subtipoCont"/>
								<a class="element"
									href="javascript:SelectSubstitute('<%= value.toString() %>', '<%= sustituto.toString() %>', '<%= subtipoCont.toString() %>');">
								<%=format.formatProperty(substitute)%> </a>
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
								<%=format.formatProperty(substitute)%>
							</display:column>
						</logic:equal>
						<logic:equal name="format" property="fieldType" value="BOOLEAN_S-N">
			
							<display:column titleKey='<%=format.getTitleKey()%>'
								media='<%=format.getMedia()%>'
								headerClass='<%=format.getHeaderClass()%>'
								sortable='<%=format.getSortable()%>'
								sortProperty='<%=format.getPropertyName()%>'
								class='<%=format.getColumnClass()%>'
								decorator='<%=format.getDecorator()%>'
								comparator='<%=format.getComparator()%>'>
			
								<% if ("S".equals(format.formatProperty(substitute))) { %>
								<bean:message key="bool.yes" />
								<% } else { %>
								<bean:message key="bool.no" />
								<% } %>
			
							</display:column>
			
						</logic:equal>
					</logic:iterate>
				</display:table>
				<div>
		
		</html:form>
		<ispac:layer />
	</div><%--seccion ficha --%> 
	</div><%--fin cuerpo ficha --%>

	</div><%--fin  ficha --%>
<div><%--fin contenido --%>
</body>

</html>

<script>
	positionMiddleScreen('contenido');
	$(document).ready(function(){
		$("#contenido").draggable();
	});
</script>
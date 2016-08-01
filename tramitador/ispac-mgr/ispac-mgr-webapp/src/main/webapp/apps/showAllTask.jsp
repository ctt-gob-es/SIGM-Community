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
<title>Trámites</title>
<link rel="stylesheet" href='<ispac:rewrite href="css/styles.css"/>' />
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
	src='<ispac:rewrite href="../scripts/jquery.alerts.js"/>'></script>
<script type="text/javascript"
	src='<ispac:rewrite href="../scripts/jquery-ui-1.7.2.custom.min.js"/>'></script>
<script type="text/javascript"
	src='<ispac:rewrite href="../scripts/sorttable.js"/>'></script>
<script type="text/javascript"
	src='<ispac:rewrite href="../scripts/utilities.js"/>'></script>

</head>
<body>
<form>
<div id="contenido" class="move">
<div class="ficha">
<div class="encabezado_ficha">
<div class="titulo_ficha">
<h4>Trámites</h4>
<div class="acciones_ficha"><a href="#" id="btnCancel"
	onclick='<ispac:hideframe refresh="true"/>' class="btnCancel"><bean:message
	key="common.message.close" /></a></div>
</div>
</div>

<div class="cuerpo_ficha">
<div class="seccion_ficha"><logic:messagesPresent>
	<div class="infoError"><bean:message
		key="forms.errors.messagesPresent" /></div>
</logic:messagesPresent> <logic:present name="ValueList">

	<logic:notEmpty name="ValueList">
		<bean:define name="Formatter" id="formatter"
			type="ieci.tdw.ispac.ispaclib.bean.BeanFormatter" />
		<!-- displayTag con formateador -->
		<display:table name="ValueList" id="value" export="true"
			pagesize='<%=formatter.getPageSize()%>' class="tableDisplay"
			requestURI='' defaultsort="1" >
			<logic:iterate name="Formatter" id="format"
				type="ieci.tdw.ispac.ispaclib.bean.BeanPropertyFmt">

				<logic:equal name="format" property="fieldType" value="LINK">

					<display:column titleKey='<%=format.getTitleKey()%>'
						media='<%=format.getMedia()%>'
						sortable='<%=format.getSortable()%>'
						sortProperty='<%=format.getPropertyName()%>'
						decorator='<%=format.getDecorator()%>'
						headerClass='<%=format.getHeaderClass()%>'
						class='<%=format.getColumnClass()%>'>

						<a class="tdlink"
							href="javascript:redirect(<%="'"+request.getContextPath()+"/showTask.do?key="%><bean:write name='value' property='property(ID)'/>
							<%="&taskId="%><bean:write name='value' property='property(ID_TRAM_EXP)'/>
							<%="&stagePcdIdActual="%><bean:write name='value' property='property(ID_FASE_PCD)'/>');">
						<%=format.formatProperty(value)%> </a>

					</display:column>

				</logic:equal>

				<logic:equal name="format" property="fieldType" value="LIST">

					<display:column titleKey='<%=format.getTitleKey()%>'
						media='<%=format.getMedia()%>'
						sortable='<%=format.getSortable()%>'
						sortProperty='<%=format.getPropertyName()%>'
						decorator='<%=format.getDecorator()%>'
						headerClass='<%=format.getHeaderClass()%>'
						class='<%=format.getColumnClass()%>'>

						<%=format.formatProperty(value)%>

					</display:column>

				</logic:equal>			
				
			</logic:iterate>

		</display:table>

	</logic:notEmpty>

	<logic:empty name="ValueList">
		No existen trámites asociados
		<br>
		<br>
	</logic:empty>

</logic:present></div>
</div>
</div>
</div>


</form>
</body>
</html>

<script>
	function redirect(url){
		        top.window.location = url;
		  	}
	positionMiddleScreen('contenido');
	$(document).ready(function(){
		$("#contenido").draggable();
	});
	</script>
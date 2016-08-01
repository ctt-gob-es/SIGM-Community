<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>

<link rel="stylesheet" href='<ispac:rewrite href="css/styles.css"/>'/>

<style type="text/css">
.move{
	height:450px;
	width:700px;
	left: 150px;
	top: 70px;
	z-index: 99999
}

.ficha2 {
	zoom: 1;
	background: url("ispac/skin1/img/bottom_right.gif") bottom right no-repeat;
}

.ficha2 {
    margin: 10px 0px 10px 0px;
}

.ficha2 .encabezado_ficha2,.encabezado_ficha2, .ficha_tabs2 .encabezado_ficha2, .inner_ficha_tabs2 .encabezado_ficha2 {
	background: url("ispac/skin1/img/top_right.gif") top right no-repeat;
}

.encabezado_ficha2 .titulo_ficha2 {
	/*background: #E8E9EE;*/
	background: url("ispac/skin1/img/top_left.gif") left 0px no-repeat;
	padding: 0px 0px 6px 0px;
	margin: 0px;
}

.encabezado_ficha2 .titulo_ficha2 h4 {
	float: left;
	margin: 0px;
	padding-left: 10px;
	padding-top: 8px;
	font-size: 100%;
	color: #6C6C6C;
}

.ficha2 div.cuerpo_ficha2, .ficha_tabs2 div.cuerpo_ficha2 {
	background: url("ispac/skin1/img/bottom_left.gif") bottom left no-repeat;
	padding-bottom: 6px;
	clear:right;
}

div.cuerpo_ficha2 div.seccion_ficha2 {
	border: 1px solid #c7d9d3;
	background-color: #ffffff;
	margin: 0px 6px 1px 5px;
	padding-bottom:15px;
}

.encabezado_ficha2 .acciones_ficha2 , .acciones_ficha2{
	text-align: right;
	padding: 8px 12px 0px 0px;
	margin: 0px;
}

.encabezado_ficha2 .acciones_ficha2 a {
	text-decoration: none;
	font-weight: bold;
	color: #6C6C6C;
	font-size: 90%;
	padding: 1px 10px;
}

.encabezado_ficha2 .acciones_ficha2 a.btnCancel {
	background: url("ispac/skin1/img/ico_cancel.gif") top left no-repeat;
	padding: 4px 10px 0px 19px;
}
</style>

<%
String ext = request.getParameter("extension");
String documento = request.getParameter("document");
String readonly = request.getParameter("readonly");
%>
<script>
alert('<bean:message key="seleccionarEditor.mensajeInicio" /> <%=ext%>');
</script>

<div id="contenido" class="move">
	<div class="ficha2"> 
		<div class="encabezado_ficha2">
			<div class="titulo_ficha2">
				<h4><label alt = '<bean:message key="seleccionarEditor.titleCorto" /> <%=ext%>' title = '<bean:message key="seleccionarEditor.titleCorto" /> <%=ext%>'><bean:message key="seleccionarEditor.title" /> <%=ext%></label></h4>
				<div class="acciones_ficha2">					
					<a href="#" id="btnCancel" onclick='javascript:top.ispac_needToConfirm=false;<ispac:hideframe refresh="false"/>' class="btnCancel" alt = '<bean:message key="forms.button.close" />' title = '<bean:message key="forms.button.close" />'><bean:message key="forms.button.close" /></a>
				</div>
			</div>
		</div>
		<div class="cuerpo_ficha2">
			<div class="seccion_ficha2">
				<table cellspacing="0" cellpadding="5" align="center" width="100%" class="tableDisplay"">
					<thead>
						<tr>
							<th class="sortable sorted order1">
								<label alt = '<bean:message key="seleccionarEditor.openOfficeTitle" />' title = '<bean:message key="seleccionarEditor.openOfficeTitle" />'><bean:message key="seleccionarEditor.openOfficeTitle" /></label>
							</th>
							<!-- <th class="sortable">
								<label alt = '<bean:message key="seleccionarEditor.msOffice" />' title = '<bean:message key="seleccionarEditor.msOffice" />'><bean:message key="seleccionarEditor.msOffice" /></label>
							</th>-->
						</tr>
					</thead>
					<tbody>
						<tr class='odd'>
							<td class="width50percent">
								&nbsp;
								<label alt = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.openOffice" />' title = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.openOffice" />'>
								<img src="<ispac:rewrite href='img/docs/doc_odt.gif'/>" class="imgTextBottom" alt = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.docODT" />' title = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.docODT" />'/>
								<img src="<ispac:rewrite href='img/docs/doc_ods.gif'/>" class="imgTextBottom" alt = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.docODS" />' title = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.docODS" />'/> <bean:message key="seleccionarEditor.openOffice" /> <input id="editorRD" type="radio" value="sofficeSIGEM: " onclick="seteaValor(this.value);"/></label>
							</td>
							<!-- <td class="width50percent">
								<label alt = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.word" />' title = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.word" />'><img src="<ispac:rewrite href='img/docs/doc_doc.gif'/>" class="imgTextBottom" alt = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.word" />' title = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.word" />'/> <bean:message key="seleccionarEditor.word" /><input id="editorRD" type="radio" value="ms-word:ofe|u|" onclick="seteaValor(this.value);"/></label>
								<br>
							</td>
						</tr>
						<tr class="even">
							<td class="width50percent">&nbsp;</td>
							<td class="width50percent">
								<label alt = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.excel" />' title = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.excel" />'><img src="<ispac:rewrite href='img/docs/doc_xls.gif'/>" class="imgTextBottom" alt = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.excel" />' title = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.excel" />'/> <bean:message key="seleccionarEditor.excel" /><input id="editorRD" type="radio" value="ms-excel:ofe|u|" onclick="seteaValor(this.value);"/></label>
							</td>
						</tr>
						<tr class="odd">
							<td class="width50percent">&nbsp;</td>
							<td class="width50percent">
								<label alt = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.powerPoint" />' title = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.powerPoint" />'><img src="<ispac:rewrite href='img/docs/doc_ppt.gif'/>" class="imgTextBottom" alt = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.powerPoint" />' title = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.powerPoint" />'/> <bean:message key="seleccionarEditor.powerPoint" /><input id="editorRD" type="radio" value="ms-powerpoint:ofe|u|" onclick="seteaValor(this.value);"/></label>
							</td>
						</tr>
						<tr class="even">
							<td class="width50percent">&nbsp;</td>
							<td class="width50percent">
								<label alt = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.project" />' title = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.project" />'><img src="<ispac:rewrite href='img/docs/doc_unknown.gif'/>" class="imgTextBottom" alt = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.project" />' title =  '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.project" />'/> <bean:message key="seleccionarEditor.project" /><input id="editorRD" type="radio" value="ms-project:ofe|u|" onclick="seteaValor(this.value);"/></label>
							</td>
						</tr>
						<tr class="odd">
							<td class="width50percent">&nbsp;</td>
							<td class="width50percent">
								<label alt =  '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.access" />' title =  '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.access" />'><img src="<ispac:rewrite href='img/docs/doc_unknown.gif'/>" class="imgTextBottom" alt =  '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.access" />' title = '<bean:message key="seleccionarEditor.documento" /> <bean:message key="seleccionarEditor.access" />'/> <bean:message key="seleccionarEditor.access" /><input id="editorRD" type="radio" value="ms-access:ofe|u|" onclick="seteaValor(this.value);"/></label>								
							</td>-->
						</tr>
					</tbody>										
				</table>
			</div>
		</div>
	</div>
</div>

<script>
	function seteaValor(valor){
		localStorage.setItem('<%=ext%>', valor );
		var documento = '<%=documento%>';
		var readonly = '<%=readonly%>';
		alert('<bean:message key="seleccionarEditor.mensajeCorrecto" />');
		var url = '<%= request.getContextPath()%>' + '/editTemplate.do?template=' + documento;     

		parent.document.forms[0].target = "editTemplateFrame";
		parent.document.forms[0].action = url;
		parent.document.forms[0].submit();
		<ispac:hideframe refresh="false"/>
	}
</script>

<script>
positionMiddleScreen('contenido');
	$(document).ready(function(){
		$("#contenido").draggable();
	});
</script>
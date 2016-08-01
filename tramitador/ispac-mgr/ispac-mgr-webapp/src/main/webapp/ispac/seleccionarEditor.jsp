<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>

<%
String ext = request.getParameter("extension");
String documento = request.getParameter("document");
String readonly = request.getParameter("readonly");
%>
<script>
alert('<bean:message key="seleccionarEditor.mensajeInicio" /> <%=ext%>');
</script>

<div id="contenido" class="move" >
	<div class="ficha"> 
		<div class="encabezado_ficha">
			<div class="titulo_ficha">
				<h4><label alt = '<bean:message key="seleccionarEditor.titleCorto" /> <%=ext%>' title = '<bean:message key="seleccionarEditor.titleCorto" /> <%=ext%>'><bean:message key="seleccionarEditor.title" /> <%=ext%></label></h4>
				<div class="acciones_ficha">
					<a href="#" id="btnCancel" onclick='javascript:top.ispac_needToConfirm=false;<ispac:hideframe refresh="true"/>' class="btnCancel" alt = '<bean:message key="common.message.close" />' title = '<bean:message key="common.message.close" />'><bean:message key="common.message.close" /></a>
				</div>
			</div>
		</div>
		<div class="cuerpo_ficha">
			<div class="seccion_ficha">
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
	
		if(String(parent.document.getElementById('workframe_document'))!='null'){
			parent.document.getElementById('workframe_document').src = '/SIGEM_TramitacionWeb/getDocument.do?document='+documento+'&readonly='+readonly;
		}
		else{
			parent.document.getElementById('workframe').src = '/SIGEM_TramitacionWeb/getDocument.do?document='+documento+'&readonly='+readonly;
		}
		top.ispac_needToConfirm=false;<ispac:hideframe refresh="true"/>
	}
</script>

<script>
positionMiddleScreen('contenido');
	$(document).ready(function(){
		$("#contenido").draggable();
	});
</script>
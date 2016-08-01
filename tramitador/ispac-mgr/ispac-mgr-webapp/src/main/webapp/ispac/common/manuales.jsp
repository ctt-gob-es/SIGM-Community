<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>


<div id="contenido" class="move" >
<div class="ficha">
<div class="encabezado_ficha">
<div class="titulo_ficha">
<h4><bean:message key="manual.title" /></h4>
<div class="acciones_ficha"><a href="#" id="btnCancel"
	onclick='javascript:top.ispac_needToConfirm=false;<ispac:hideframe refresh="true"/>'
	class="btnCancel"><bean:message key="common.message.close" /></a></div>
<%--fin acciones ficha --%></div>
<%--fin titulo ficha --%></div>
<%--fin encabezado ficha --%>

<div class="cuerpo_ficha">
<div class="seccion_ficha">
<html:form action="/showManualesList.do">
<display:table name="ManualesList"
	id="manual" export="true" class="tableDisplay" sort="list"
	pagesize="20" requestURI="/showManualesList.do"
	decorator="ieci.tdw.ispac.ispacweb.decorators.SelectedRowTableDecorator">
	
	<display:column titleKey="manual.nombre" headerClass="sortable" sortable="true" media="html">
		<logic:present name="manual" property="property(ID)">
			
			<bean:define id="idManual" name="manual" property="property(ID)" />
			<a href='<%="showManual.do?id=" + idManual %>' target='_blank'>
				<ispac:documenticon imageSrc="img/docs/" extension="pdf" styleClass="imgTextBottom" />
					<bean:write name="manual" property="property(NOMBRE)" />
			</a>
	
		</logic:present>
	</display:column>
	
	<display:column titleKey="manual.nombre" headerClass="sortable" 
			sortable="true" group="1" media="csv xml excel pdf">
			
		<logic:notEmpty name="manual" property="property(NOMBRE)">
			<bean:write name="manual" property="property(NOMBRE)" />
		</logic:notEmpty>
	</display:column>
	
	<display:column titleKey="manual.description" headerClass="sortable" sortable="true">
		<logic:notEmpty name="manual" property="property(DESCRIPCION)">
			<bean:write name="manual" property="property(DESCRIPCION)" />
		</logic:notEmpty>
	</display:column>
	
</display:table>
</html:form>
</div>
<%--seccion ficha --%> 
</div>
<%--fin cuerpo ficha --%></div>
<%--fin  ficha --%>
<div><%--fin contenido --%> <script>


// function  executeManual(action) {
// 			document.manualForm.action = action;
// 			document.manualForm.submit();
// }
		
positionMiddleScreen('contenido');
	$(document).ready(function(){
		$("#contenido").draggable();
	});
	

</script>
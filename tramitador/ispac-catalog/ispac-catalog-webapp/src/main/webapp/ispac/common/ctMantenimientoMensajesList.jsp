<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac" %>

<script>
	function redirect(url) {
		submit('<%=request.getContextPath()%>' + url);
	}
	
	function deleteMensaje(url) {
		if (checkboxElement(document.defaultForm.multibox) == "") {
			jAlert('<bean:message key="error.entity.noSelected"/>', '<bean:message key="common.alert"/>' , '<bean:message key="common.message.ok"/>' , '<bean:message key="common.message.cancel"/>');
		} else {
			var frm = document.forms[0];
			var url = '<%=request.getContextPath()%>' + '/deleteMensaje.do?method=deleteMensajes';
		
			jConfirm('<bean:message key="entity.confirm.eliminarMensaje"/>', '<bean:message key="common.confirm"/>', '<bean:message key="common.message.ok"/>' , '<bean:message key="common.message.cancel"/>', function(r) {
				if(r){
					frm.action = url;
					frm.submit();		
				}
			});			
		}		
	}
</script>

<div id="navmenutitle">
	<bean:message key="title.mantenimientoMensajeslist"/>
</div>
<div id="navSubMenuTitle">
</div>
<div id="navmenu">
	<ispac:hasFunction functions="FUNC_COMP_REPORTS_EDIT">
	<ul class="actionsMenuList">
		<li>
			<a href="javascript:redirect('/showCTEntityUp.do?entityId=67&regId=-1')">
				<bean:message key="new.mantenimientoMensajes"/>
			</a>
		</li>
		<li>
			<a href="javascript:deleteMensaje()">
				<bean:message key="delete.mantenimientoMensajes" />
			</a>
		</li>
		<li>
			<a href="javascript:redirect('/showCTEntityUp.do?entityId=67&regId=-1&masivo=true')">
				<bean:message key="newMasivo.mantenimientoMensajes"/>
			</a>
		</li>
	</ul>
	</ispac:hasFunction>
</div>
<html:form action='/showCTMantenimientoMensajesList.do'>
	
	<!-- BUSCADOR -->
	<tiles:insert page="ajax/ajaxSearchTile.jsp" ignore="true" flush="false" >	
		<tiles:put name="suggestEntityId" value="65"/>
	</tiles:insert>
	
	<!-- LISTA DE REGLAS -->
	<tiles:insert page="tiles/displaytagList.jsp" ignore="true" flush="false">
		<tiles:put name="ItemListAttr" value="CTMantenimientoMensajesList"/>
		<tiles:put name="ItemFormatterAttr" value="CTMantenimientoMensajesListFormatter"/>
		<tiles:put name="ItemActionAttr" value="/showCTMantenimientoMensajesList.do"/>
	</tiles:insert>

</html:form>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c1"%>
<c1:set var="aux0">
	<bean:write name="defaultForm"
		property="entityApp.label(FIRMA_DOC_EXTERNO:FIRMA_DOC_EXTERNO)" />
</c1:set><jsp:useBean id="aux0" type="java.lang.String" />
<ispac:tabs>
	<ispac:tab title='<%=aux0%>'>
		<div id="dataBlock_FIRMA_DOC_EXTERNO"
			style="position: relative; height: 110px; width: 600px">
			<div id="label_FIRMA_DOC_EXTERNO:DNI"
				style="position: absolute; top: 10px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(FIRMA_DOC_EXTERNO:DNI)" />
				:
			</div>
			<div id="data_FIRMA_DOC_EXTERNO:DNI"
				style="position: absolute; top: 10px; left: 130px; width: 100%;">
				<nobr>
					<ispac:htmlTextImageFrame
						property="property(FIRMA_DOC_EXTERNO:DNI)" readonly="true"
						readonlyTag="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="80"
						id="SEARCH_FIRMA_DOC_EXTERNO_DNI" target="workframe"
						action="selectParticipantes.do" image="img/search-mg.gif"
						titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
						<ispac:parameter name="SEARCH_FIRMA_DOC_EXTERNO_DNI"
							id="property(FIRMA_DOC_EXTERNO:DNI)" property="SUSTITUTO" />
						<ispac:parameter name="SEARCH_FIRMA_DOC_EXTERNO_DNI"
							id="property(FIRMA_DOC_EXTERNO:CARGO)" property="CARGO" />
							<ispac:parameter name="SEARCH_FIRMA_DOC_EXTERNO_DNI"
							id="property(FIRMA_DOC_EXTERNO:DESCRIPCION_CARGO)" property="DESCRIPCION_CARGO" />
					</ispac:htmlTextImageFrame>
				</nobr>
			</div>
			<div id="label_FIRMA_DOC_EXTERNO:CARGO"
				style="position: absolute; top: 35px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(FIRMA_DOC_EXTERNO:CARGO)" />
				:
			</div>
			<div id="data_FIRMA_DOC_EXTERNO:CARGO"
				style="position: absolute; top: 35px; left: 130px; width: 100%;">
				<ispac:htmlText property="property(FIRMA_DOC_EXTERNO:CARGO)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="40" maxlength="24">
				</ispac:htmlText>
			</div>

			<div id="label_FIRMA_DOC_EXTERNO:DESCRIPCION_CARGO"
				style="position: absolute; top: 35px; left: 420px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(FIRMA_DOC_EXTERNO:DESCRIPCION_CARGO)" />
				:
			</div>
			<div id="data_FIRMA_DOC_EXTERNO:DESCRIPCION_CARGO"
				style="position: absolute; top: 35px; left: 540px; width: 100%;">
				<ispac:htmlText
					property="property(FIRMA_DOC_EXTERNO:DESCRIPCION_CARGO)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="100">
				</ispac:htmlText>
			</div>

			<div id="label_FIRMA_DOC_EXTERNO:ORDEN_FIRMA"
				style="position: absolute; top: 60px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(FIRMA_DOC_EXTERNO:ORDEN_FIRMA)" />
				:
			</div>
			<div id="data_FIRMA_DOC_EXTERNO:ORDEN_FIRMA"
				style="position: absolute; top: 60px; left: 130px; width: 100%;">
				<nobr>
					<ispac:htmlTextImageFrame
						property="property(FIRMA_DOC_EXTERNO:ORDEN_FIRMA)" readonly="true"
						readonlyTag="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="80"
						id="SEARCH_FIRMA_DOC_EXTERNO_ORDEN_FIRMA" target="workframe"
						action="selectValue.do?entity=SPAC_VLDTBL_NUMERO"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
						<ispac:parameter name="SEARCH_FIRMA_DOC_EXTERNO_ORDEN_FIRMA"
							id="property(FIRMA_DOC_EXTERNO:ORDEN_FIRMA)" property="VALOR" />
					</ispac:htmlTextImageFrame>
				</nobr>
			</div>
			<div id="enlaces" style="position: absolute; top: 90px; left: 10px; width:100% ;">
				<div id="enlace1" style="position: absolute; top: 15px; left: 0px; width:100% ;">
					<html:link href="javascript:ejecutar('EnviarDocPortafirmasRule');" styleClass="formaction">
						Enviar documento a firmar
					</html:link>
				</div>				
			</div>	
		</div>
	</ispac:tab>
</ispac:tabs>
<%
	String taskId = request.getParameter("taskId");
%>
<script language='JavaScript' type='text/javascript'>
function  hideShowFrameCPV3(target, action, width, height, msgConfirm, doSubmit, needToConfirm, form) {
	document.forms[0].elements['property(FIRMA_DOC_EXTERNO:CARGO)'].value = "";
	document.forms[0].elements['property(FIRMA_DOC_EXTERNO:DESCRIPCION_CARGO)'].value = "";
	showFrame(target, action, width, height, msgConfirm, doSubmit, needToConfirm, form) ;

}
function ejecutar(regla) 
{
	if (confirm("¿Confirma que desea ejecutar esta acción?")) 
	{
			var taskId = <%= taskId %>;
			document.defaultForm.action = "ExecuteRule.do?taskId="+taskId+"&rule="+regla;
			document.defaultForm.submit();
	}
}
</script>
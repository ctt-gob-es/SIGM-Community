<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c1"%>
<c1:set var="aux0">
	<bean:write name="defaultForm"
		property="entityApp.label(FIRMA_DOC_MASFIRMANTES:FIRMA_DOC_MASFIRMANTES)" />
</c1:set><jsp:useBean id="aux0" type="java.lang.String" />
<ispac:tabs>
	<ispac:tab title='<%=aux0%>'>
		<div id="dataBlock_FIRMA_DOC_MASFIRMANTES"
			style="position: relative; height: 60px; width: 600px">
			<style type="text/css">
.labelPosition_FIRMA_DOC_MASFIRMANTES {
	position: relative;
	left: 10px;
	top: 15px;
}

.inputPosition_FIRMA_DOC_MASFIRMANTES {
	position: relative;
	left: 180px;
}
</style>
			<div id="label_FIRMA_DOC_MASFIRMANTES:FIRMA_FINAL"
				class="formsTitleB labelPosition_FIRMA_DOC_MASFIRMANTES">
				<bean:write name="defaultForm"
					property="entityApp.label(FIRMA_DOC_MASFIRMANTES:FIRMA_FINAL)" />
				:
			</div>
			<div id="data_FIRMA_DOC_MASFIRMANTES:FIRMA_FINAL"
				class="inputPosition_FIRMA_DOC_MASFIRMANTES">
				<nobr>
					<ispac:htmlTextImageFrame
						property="property(FIRMA_DOC_MASFIRMANTES:FIRMA_FINAL)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" size="80"
						imageTabIndex="true"
						id="SEARCH_FIRMA_DOC_MASFIRMANTES_FIRMA_FINAL" target="workframe"
						action="selectValue.do?entity=SPAC_TBL_009"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
						<ispac:parameter name="SEARCH_FIRMA_DOC_MASFIRMANTES_FIRMA_FINAL"
							id="property(FIRMA_DOC_MASFIRMANTES:FIRMA_FINAL)"
							property="VALOR" />
					</ispac:htmlTextImageFrame>
				</nobr>
				
			</div>
			<br>
			<div style="WIDTH: 700px; COLOR: red">
			Es obligatorio especificar si el documento que es firmado externamente va a requerir la firma a continuación por la entidad.
			</div>
		</div>
	</ispac:tab>
</ispac:tabs>
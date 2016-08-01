<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c1"%>
<c1:set var="aux0">
	<bean:write name="defaultForm"
		property="entityApp.label(CONTRATACION_DOC_ADICIONALES:CONTRATACION_DOC_ADICIONALES)" />
</c1:set><jsp:useBean id="aux0" type="java.lang.String" />
<ispac:tabs>
	<ispac:tab title='<%=aux0%>'>
		<div id="dataBlock_CONTRATACION_DOC_ADICIONALES"
			style="position: relative; height: 60px; width: 600px">
			<div id="label_CONTRATACION_DOC_ADICIONALES:TIPO_DOC"
				style="position: absolute; top: 10px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DOC_ADICIONALES:TIPO_DOC)" />
				:
			</div>
			<div id="data_CONTRATACION_DOC_ADICIONALES:TIPO_DOC"
				style="position: absolute; top: 10px; left: 130px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(CONTRATACION_DOC_ADICIONALES:TIPO_DOC)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="1" cols="80" 
					id="SEARCH_CONTRATACION_DOC_ADICIONALES_TIPO_DOC" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_DOC_ADIC" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CONTRATACION_DOC_ADICIONALES_TIPO_DOC" id="property(CONTRATACION_DOC_ADICIONALES:TIPO_DOC)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
		</div>
	</ispac:tab>
</ispac:tabs>
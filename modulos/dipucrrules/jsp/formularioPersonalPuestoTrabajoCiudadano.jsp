<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c1"%>
<c1:set var="aux0">
	<bean:write name="defaultForm"
		property="entityApp.label(PERSONAL_PUESTO_TRABAJO:PERSONAL_PUESTO_TRABAJO)" />
</c1:set><jsp:useBean id="aux0" type="java.lang.String" />
<ispac:tabs>
	<ispac:tab title='<%=aux0%>'>
		<div id="dataBlock_PERSONAL_PUESTO_TRABAJO"
			style="position: relative; height: 85px; width: 600px">
			<style type="text/css">
.labelPosition_PERSONAL_PUESTO_TRABAJO {
	position: relative;
	left: 10px;
	top: 15px;
}

.inputPosition_PERSONAL_PUESTO_TRABAJO {
	position: relative;
	left: 180px;
}
</style>

			<div id="label_PERSONAL_PUESTO_TRABAJO:DEPARTAMENTO"
				class="formsTitleB labelPosition_PERSONAL_PUESTO_TRABAJO">
				<bean:write name="defaultForm"
					property="entityApp.label(PERSONAL_PUESTO_TRABAJO:DEPARTAMENTO)" />
				:
			</div>
			<div id="data_PERSONAL_PUESTO_TRABAJO:DEPARTAMENTO"
				class="inputPosition_PERSONAL_PUESTO_TRABAJO">
				<ispac:htmlText
					property="property(PERSONAL_PUESTO_TRABAJO:PUESTOTRABAJO)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="255">
				</ispac:htmlText>
			</div>
			<div id="label_PERSONAL_PUESTO_TRABAJO:NUMEXP_PADRE"
				class="formsTitleB labelPosition_PERSONAL_PUESTO_TRABAJO">
				<bean:write name="defaultForm"
					property="entityApp.label(PERSONAL_PUESTO_TRABAJO:NUMEXP_PADRE)" />
				:
			</div>
			<div id="data_PERSONAL_PUESTO_TRABAJO:NUMEXP_PADRE"
				class="inputPosition_PERSONAL_PUESTO_TRABAJO">
				<ispac:htmlText
					property="property(PERSONAL_PUESTO_TRABAJO:NUMEXP_PADRE)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="20" maxlength="20">
				</ispac:htmlText>
			</div>
			<div id="label_PERSONAL_PUESTO_TRABAJO:EXPONE"
				class="formsTitleB labelPosition_PERSONAL_PUESTO_TRABAJO">
				<bean:write name="defaultForm"
					property="entityApp.label(PERSONAL_PUESTO_TRABAJO:EXPONE)" />
				:
			</div>
			<div id="data_PERSONAL_PUESTO_TRABAJO:EXPONE"
				class="inputPosition_PERSONAL_PUESTO_TRABAJO">
				<ispac:htmlTextarea
					property="property(PERSONAL_PUESTO_TRABAJO:EXPONE)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" rows="4" cols="80">
				</ispac:htmlTextarea>
			</div>
			<div id="label_PERSONAL_PUESTO_TRABAJO:SOLICITA"
				class="formsTitleB labelPosition_PERSONAL_PUESTO_TRABAJO">
				<bean:write name="defaultForm"
					property="entityApp.label(PERSONAL_PUESTO_TRABAJO:SOLICITA)" />
				:
			</div>
			<div id="data_PERSONAL_PUESTO_TRABAJO:SOLICITA"
				class="inputPosition_PERSONAL_PUESTO_TRABAJO">
				<ispac:htmlTextarea
					property="property(PERSONAL_PUESTO_TRABAJO:SOLICITA)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" rows="4" cols="80">
				</ispac:htmlTextarea>
			</div>

			<div id="label_PERSONAL_PUESTO_TRABAJO:ADMITIDO"
				class="formsTitleB labelPosition_PERSONAL_PUESTO_TRABAJO">
				<bean:write name="defaultForm"
					property="entityApp.label(PERSONAL_PUESTO_TRABAJO:ADMITIDO)" />
				:
			</div>
			<div id="data_PERSONAL_PUESTO_TRABAJO:ADMITIDO"
				class="inputPosition_PERSONAL_PUESTO_TRABAJO">
				<nobr>
					<ispac:htmlTextImageFrame
						property="property(PERSONAL_PUESTO_TRABAJO:ADMITIDO)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" size="80"
						imageTabIndex="true" id="SEARCH_PERSONAL_PUESTO_TRABAJO_ADMITIDO"
						target="workframe" action="selectValue.do?entity=SPAC_TBL_009"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
						<ispac:parameter name="SEARCH_PERSONAL_PUESTO_TRABAJO_ADMITIDO"
							id="property(PERSONAL_PUESTO_TRABAJO:ADMITIDO)" property="VALOR" />
					</ispac:htmlTextImageFrame>
				</nobr>
			</div>
			
			<div id="label_PERSONAL_PUESTO_TRABAJO:MOTIVO_RECHAZO"
				class="formsTitleB labelPosition_PERSONAL_PUESTO_TRABAJO">
				<bean:write name="defaultForm"
					property="entityApp.label(PERSONAL_PUESTO_TRABAJO:MOTIVO_RECHAZO)" />
				:
			</div>
			<div id="data_PERSONAL_PUESTO_TRABAJO:MOTIVO_RECHAZO"
				class="inputPosition_PERSONAL_PUESTO_TRABAJO">
				<ispac:htmlTextarea
					property="property(PERSONAL_PUESTO_TRABAJO:MOTIVO_RECHAZO)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" rows="4" cols="80">
				</ispac:htmlTextarea>
			</div>
	</ispac:tab>
</ispac:tabs>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c1"%>
<c1:set var="aux0">
	<bean:write name="defaultForm"
		property="entityApp.label(TABLON_EDICTAL_BOE_PARRAFOS:TABLON_EDICTAL_BOE_PARRAFOS)" />
</c1:set><jsp:useBean id="aux0" type="java.lang.String" />
<ispac:tabs>
	<ispac:tab title='<%=aux0%>'>
		<div id="dataBlock_TABLON_EDICTAL_BOE_PARRAFOS"
			style="position: relative; height: 85px; width: 600px">
			<style type="text/css">
.labelPosition_TABLON_EDICTAL_BOE_PARRAFOS {
	position: relative;
	left: 10px;
	top: 15px;
}

.inputPosition_TABLON_EDICTAL_BOE_PARRAFOS {
	position: relative;
	left: 180px;
}
</style>
			<div id="label_TABLON_EDICTAL_BOE_PARRAFOS:PARRAFO"
				class="formsTitleB labelPosition_TABLON_EDICTAL_BOE_PARRAFOS">
				<bean:write name="defaultForm"
					property="entityApp.label(TABLON_EDICTAL_BOE_PARRAFOS:PARRAFO)" />
				:
			</div>
			<div id="data_TABLON_EDICTAL_BOE_PARRAFOS:PARRAFO"
				class="inputPosition_TABLON_EDICTAL_BOE_PARRAFOS">
				<ispac:htmlTextarea
					property="property(TABLON_EDICTAL_BOE_PARRAFOS:PARRAFO)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" rows="8" cols="150">
				</ispac:htmlTextarea>
			</div>
		</div>
	</ispac:tab>
</ispac:tabs>
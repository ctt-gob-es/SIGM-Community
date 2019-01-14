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

			<div id="label_PERSONAL_PUESTO_TRABAJO:ANYO"
				class="formsTitleB labelPosition_PERSONAL_PUESTO_TRABAJO">
				<bean:write name="defaultForm"
					property="entityApp.label(PERSONAL_PUESTO_TRABAJO:ANYO)" />
				:
			</div>
			<div id="data_PERSONAL_PUESTO_TRABAJO:ANYO"
				class="inputPosition_PERSONAL_PUESTO_TRABAJO">
				<ispac:htmlText property="property(PERSONAL_PUESTO_TRABAJO:ANYO)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="10" maxlength="4">
				</ispac:htmlText>
			</div>

			<div id="label_PERSONAL_PUESTO_TRABAJO:DEPARTAMENTO"
				class="formsTitleB labelPosition_PERSONAL_PUESTO_TRABAJO">
				<bean:write name="defaultForm"
					property="entityApp.label(PERSONAL_PUESTO_TRABAJO:DEPARTAMENTO)" />
				:
			</div>
			<div id="data_PERSONAL_PUESTO_TRABAJO:DEPARTAMENTO"
				class="inputPosition_PERSONAL_PUESTO_TRABAJO">
				<nobr>
					<ispac:htmlTextareaImageFrame
						property="property(PERSONAL_PUESTO_TRABAJO:DEPARTAMENTO)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" rows="1"
						cols="80" id="SEARCH_PERSONAL_PUESTO_TRABAJO:DEPARTAMENTO"
						target="workframe" jsShowFrame="hideShowFrameDepartamento"
						action="SelectListadoServicesPersonalPuestoTrabajo.do?campo=SERVICIOS"
						jsDelete="deleteDepartamento" image="img/search-mg.gif"
						titleKeyLink="select.rol" imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480" tabindex="113">
						<ispac:parameter
							name="SEARCH_PERSONAL_PUESTO_TRABAJO:DEPARTAMENTO"
							id="property(PERSONAL_PUESTO_TRABAJO:DEPARTAMENTO)"
							property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			<div id="DATA_PERSONAL_PUESTO_TRABAJO:PUESTOTRABAJO"
				class="inputPosition_PERSONAL_PUESTO_TRABAJO">
				<nobr>
					<ispac:htmlTextareaImageFrame
						property="property(PERSONAL_PUESTO_TRABAJO:PUESTOTRABAJO)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" rows="1"
						cols="80" id="SEARCH_PERSONAL_PUESTO_TRABAJO:PUESTOTRABAJO"
						target="workframe"
						action="SelectListadoServicesPersonalPuestoTrabajo.do?campo=PUESTO&departamento='+document.forms[0].elements['property(PERSONAL_PUESTO_TRABAJO:DEPARTAMENTO)'].value+'&anyo='+document.forms[0].elements['property(PERSONAL_PUESTO_TRABAJO:ANYO)'].value+'"
						jsDelete="deletePuesto" image="img/search-mg.gif"
						titleKeyLink="select.rol" imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480" tabindex="113">
						<ispac:parameter name="SEARCH_PERSONAL_PUESTO_TRABAJO:PUESTOTRABAJO" id="property(PERSONAL_PUESTO_TRABAJO:PUESTOTRABAJO)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			<div id="label_PERSONAL_PUESTO_TRABAJO:GRUPO"
				class="formsTitleB labelPosition_PERSONAL_PUESTO_TRABAJO">
				<bean:write name="defaultForm"
					property="entityApp.label(PERSONAL_PUESTO_TRABAJO:GRUPO)" />
				:
			</div>
			<div id="data_PERSONAL_PUESTO_TRABAJO:GRUPO"
				class="inputPosition_PERSONAL_PUESTO_TRABAJO">
				<ispac:htmlText property="property(PERSONAL_PUESTO_TRABAJO:GRUPO)"
					readonly="true" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="255">
				</ispac:htmlText>
			</div>
			<div id="label_PERSONAL_PUESTO_TRABAJO:PROVISION"
				class="formsTitleB labelPosition_PERSONAL_PUESTO_TRABAJO">
				<bean:write name="defaultForm"
					property="entityApp.label(PERSONAL_PUESTO_TRABAJO:PROVISION)" />
				:
			</div>
			<div id="data_PERSONAL_PUESTO_TRABAJO:PROVISION"
				class="inputPosition_PERSONAL_PUESTO_TRABAJO">
				<ispac:htmlText property="property(PERSONAL_PUESTO_TRABAJO:PROVISION)"
					readonly="true" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="255">
				</ispac:htmlText>
			</div>
			
			<div id="label_PERSONAL_PUESTO_TRABAJO:COMP_ESP"
				class="formsTitleB labelPosition_PERSONAL_PUESTO_TRABAJO">
				<bean:write name="defaultForm"
					property="entityApp.label(PERSONAL_PUESTO_TRABAJO:COMP_ESP)" />
				:
			</div>
			<div id="data_PERSONAL_PUESTO_TRABAJO:COMP_ESP"
				class="inputPosition_PERSONAL_PUESTO_TRABAJO">
				<ispac:htmlText property="property(PERSONAL_PUESTO_TRABAJO:COMP_ESP)"
					readonly="true" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="255">
				</ispac:htmlText>
			</div>
			<div id="label_PERSONAL_PUESTO_TRABAJO:NIVEL_ESP"
				class="formsTitleB labelPosition_PERSONAL_PUESTO_TRABAJO">
				<bean:write name="defaultForm"
					property="entityApp.label(PERSONAL_PUESTO_TRABAJO:NIVEL_ESP)" />
				:
			</div>
			<div id="data_PERSONAL_PUESTO_TRABAJO:NIVEL_ESP"
				class="inputPosition_PERSONAL_PUESTO_TRABAJO">
				<ispac:htmlText property="property(PERSONAL_PUESTO_TRABAJO:NIVEL_ESP)"
					readonly="true" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="255">
				</ispac:htmlText>
			</div>
			<div id="label_PERSONAL_PUESTO_TRABAJO:COMP_DEST"
				class="formsTitleB labelPosition_PERSONAL_PUESTO_TRABAJO">
				<bean:write name="defaultForm"
					property="entityApp.label(PERSONAL_PUESTO_TRABAJO:COMP_DEST)" />
				:
			</div>
			<div id="data_PERSONAL_PUESTO_TRABAJO:COMP_DEST"
				class="inputPosition_PERSONAL_PUESTO_TRABAJO">
				<ispac:htmlText property="property(PERSONAL_PUESTO_TRABAJO:COMP_DEST)"
					readonly="true" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="255">
				</ispac:htmlText>
			</div>
			
			<div id="label_PERSONAL_PUESTO_TRABAJO:TIPO_PUESTO"
				class="formsTitleB labelPosition_PERSONAL_PUESTO_TRABAJO">
				<bean:write name="defaultForm"
					property="entityApp.label(PERSONAL_PUESTO_TRABAJO:TIPO_PUESTO)" />
				:
			</div>
			<div id="data_PERSONAL_PUESTO_TRABAJO:TIPO_PUESTO"
				class="inputPosition_PERSONAL_PUESTO_TRABAJO">
				<ispac:htmlText property="property(PERSONAL_PUESTO_TRABAJO:TIPO_PUESTO)"
					readonly="true" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="255">
				</ispac:htmlText>
			</div>
			
			<div id="label_PERSONAL_PUESTO_TRABAJO:ESCALA"
				class="formsTitleB labelPosition_PERSONAL_PUESTO_TRABAJO">
				<bean:write name="defaultForm"
					property="entityApp.label(PERSONAL_PUESTO_TRABAJO:ESCALA)" />
				:
			</div>
			<div id="data_PERSONAL_PUESTO_TRABAJO:ESCALA"
				class="inputPosition_PERSONAL_PUESTO_TRABAJO">
				<ispac:htmlText property="property(PERSONAL_PUESTO_TRABAJO:ESCALA)"
					readonly="true" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="255">
				</ispac:htmlText>
			</div>
			
			<div id="label_PERSONAL_PUESTO_TRABAJO:SUBESCALA"
				class="formsTitleB labelPosition_PERSONAL_PUESTO_TRABAJO">
				<bean:write name="defaultForm"
					property="entityApp.label(PERSONAL_PUESTO_TRABAJO:SUBESCALA)" />
				:
			</div>
			<div id="data_PERSONAL_PUESTO_TRABAJO:SUBESCALA"
				class="inputPosition_PERSONAL_PUESTO_TRABAJO">
				<ispac:htmlText property="property(PERSONAL_PUESTO_TRABAJO:SUBESCALA)"
					readonly="true" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="255">
				</ispac:htmlText>
			</div>
			
			
			<div id="label_PERSONAL_PUESTO_TRABAJO:CLASE"
				class="formsTitleB labelPosition_PERSONAL_PUESTO_TRABAJO">
				<bean:write name="defaultForm"
					property="entityApp.label(PERSONAL_PUESTO_TRABAJO:CLASE)" />
				:
			</div>
			<div id="data_PERSONAL_PUESTO_TRABAJO:CLASE"
				class="inputPosition_PERSONAL_PUESTO_TRABAJO">
				<ispac:htmlText property="property(PERSONAL_PUESTO_TRABAJO:CLASE)"
					readonly="true" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="255">
				</ispac:htmlText>
			</div>
			
			<div id="label_PERSONAL_PUESTO_TRABAJO:CATEGORIA"
				class="formsTitleB labelPosition_PERSONAL_PUESTO_TRABAJO">
				<bean:write name="defaultForm"
					property="entityApp.label(PERSONAL_PUESTO_TRABAJO:CATEGORIA)" />
				:
			</div>
			<div id="data_PERSONAL_PUESTO_TRABAJO:CATEGORIA"
				class="inputPosition_PERSONAL_PUESTO_TRABAJO">
				<ispac:htmlText property="property(PERSONAL_PUESTO_TRABAJO:CATEGORIA)"
					readonly="true" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="255">
				</ispac:htmlText>
			</div>
			
			<div id="label_PERSONAL_PUESTO_TRABAJO:TITULACION"
				class="formsTitleB labelPosition_PERSONAL_PUESTO_TRABAJO">
				<bean:write name="defaultForm"
					property="entityApp.label(PERSONAL_PUESTO_TRABAJO:TITULACION)" />
				:
			</div>
			<div id="data_PERSONAL_PUESTO_TRABAJO:TITULACION"
				class="inputPosition_PERSONAL_PUESTO_TRABAJO">
				<ispac:htmlText property="property(PERSONAL_PUESTO_TRABAJO:TITULACION)"
					readonly="true" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="255">
				</ispac:htmlText>
			</div>
			
			<div id="label_PERSONAL_PUESTO_TRABAJO:JORNADA"
				class="formsTitleB labelPosition_PERSONAL_PUESTO_TRABAJO">
				<bean:write name="defaultForm"
					property="entityApp.label(PERSONAL_PUESTO_TRABAJO:JORNADA)" />
				:
			</div>
			<div id="data_PERSONAL_PUESTO_TRABAJO:JORNADA"
				class="inputPosition_PERSONAL_PUESTO_TRABAJO">
				<ispac:htmlText property="property(PERSONAL_PUESTO_TRABAJO:JORNADA)"
					readonly="true" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="255">
				</ispac:htmlText>
			</div>
			<div id="label_PERSONAL_PUESTO_TRABAJO:UBICACION"
				class="formsTitleB labelPosition_PERSONAL_PUESTO_TRABAJO">
				<bean:write name="defaultForm"
					property="entityApp.label(PERSONAL_PUESTO_TRABAJO:UBICACION)" />
				:
			</div>
			<div id="data_PERSONAL_PUESTO_TRABAJO:UBICACION"
				class="inputPosition_PERSONAL_PUESTO_TRABAJO">
				<ispac:htmlTextarea
					property="property(PERSONAL_PUESTO_TRABAJO:UBICACION)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" rows="2" cols="80">
				</ispac:htmlTextarea>
			</div>
			<div id="label_PERSONAL_PUESTO_TRABAJO:ORGANO"
				class="formsTitleB labelPosition_PERSONAL_PUESTO_TRABAJO">
				<bean:write name="defaultForm"
					property="entityApp.label(PERSONAL_PUESTO_TRABAJO:ORGANO)" />
				:
			</div>
			<div id="data_PERSONAL_PUESTO_TRABAJO:ORGANO"
				class="inputPosition_PERSONAL_PUESTO_TRABAJO">
				<nobr>
					<html:hidden property="property(PERSONAL_PUESTO_TRABAJO:ORGANO)" />
					<ispac:htmlTextImageFrame
						property="property(ORGANO_PERSONAL_ORGANO_COMPETENTE:SUSTITUTO)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" size="80"
						imageTabIndex="true" id="SEARCH_PERSONAL_PUESTO_TRABAJO_ORGANO"
						target="workframe"
						action="selectSubstitute.do?entity=PERSONAL_ORGANO_COMPETENTE"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
						<ispac:parameter name="SEARCH_PERSONAL_PUESTO_TRABAJO_ORGANO"
							id="property(PERSONAL_PUESTO_TRABAJO:ORGANO)" property="VALOR" />
						<ispac:parameter name="SEARCH_PERSONAL_PUESTO_TRABAJO_ORGANO"
							id="property(ORGANO_PERSONAL_ORGANO_COMPETENTE:SUSTITUTO)"
							property="SUSTITUTO" />
					</ispac:htmlTextImageFrame>
				</nobr>
			</div>
		</div>
	</ispac:tab>
</ispac:tabs>

<script language='JavaScript' type='text/javascript'>

	function deleteGrupo() {
		document.forms[0].elements['property(PERSONAL_PUESTO_TRABAJO:GRUPO)'].value = "";
	}
	function deleteDepartamento() {
		document.forms[0].elements['property(PERSONAL_PUESTO_TRABAJO:DEPARTAMENTO)'].value = "";
	}

	function deletePuesto() {
		document.forms[0].elements['property(PERSONAL_PUESTO_TRABAJO:PUESTOTRABAJO)'].value = "";
	}

	function hideShowFrameDepartamento(target, action, width, height,
			msgConfirm, doSubmit, needToConfirm, form) {
		document.forms[0].elements['property(PERSONAL_PUESTO_TRABAJO:PUESTOTRABAJO)'].value = "";
		showFrame(target, action, width, height, msgConfirm, doSubmit, needToConfirm, form);
	}
</script>
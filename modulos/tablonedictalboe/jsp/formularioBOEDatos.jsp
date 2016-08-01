<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c1"%>
<c1:set var="aux0">
	<bean:write name="defaultForm"
		property="entityApp.label(TABLON_EDICTAL_BOE_DATOS:TABLON_EDICTAL_BOE_DATOS)" />
</c1:set><jsp:useBean id="aux0" type="java.lang.String" />
<ispac:tabs>
	<ispac:tab title='<%=aux0%>'>
		<div id="dataBlock_TABLON_EDICTAL_BOE_DATOS"
			style="position: relative; height: 450px; width: 600px">
			<style type="text/css">
.labelPosition_TABLON_EDICTAL_BOE_DATOS {
	position: relative;
	left: 10px;
	top: 15px;
	width: 150px;
}

.inputPosition_TABLON_EDICTAL_BOE_DATOS {
	position: relative;
	left: 180px;
}
</style>
			<div id="label_TABLON_EDICTAL_BOE_DATOS:PROCEDIMIENTO"
				class="formsTitleB labelPosition_TABLON_EDICTAL_BOE_DATOS">
				<bean:write name="defaultForm"
					property="entityApp.label(TABLON_EDICTAL_BOE_DATOS:PROCEDIMIENTO)" />
				:
			</div>
			<div id="data_TABLON_EDICTAL_BOE_DATOS:PROCEDIMIENTO"
				class="inputPosition_TABLON_EDICTAL_BOE_DATOS">
				<ispac:htmlText
					property="property(TABLON_EDICTAL_BOE_DATOS:PROCEDIMIENTO)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="400">
				</ispac:htmlText>
			</div>
			<div id="label_TABLON_EDICTAL_BOE_DATOS:FECHAPUBLICACION"
				class="formsTitleB labelPosition_TABLON_EDICTAL_BOE_DATOS">
				<bean:write name="defaultForm"
					property="entityApp.label(TABLON_EDICTAL_BOE_DATOS:FECHAPUBLICACION)" />
				:
			</div>
			<div id="data_TABLON_EDICTAL_BOE_DATOS:FECHAPUBLICACION"
				class="inputPosition_TABLON_EDICTAL_BOE_DATOS">
				<nobr>
					<ispac:htmlTextCalendar
						property="property(TABLON_EDICTAL_BOE_DATOS:FECHAPUBLICACION)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%= buttoncalendar %>' format="dd/mm/yyyy"
						enablePast="true">
					</ispac:htmlTextCalendar>
				</nobr>
			</div>
			
			<div id="label_TABLON_EDICTAL_BOE_DATOS:FORMA_PUBLICACION"
				class="formsTitleB labelPosition_TABLON_EDICTAL_BOE_DATOS">
				<bean:write name="defaultForm"
					property="entityApp.label(TABLON_EDICTAL_BOE_DATOS:FORMA_PUBLICACION)" />
				:
			</div>
			<div id="data_TABLON_EDICTAL_BOE_DATOS:FORMA_PUBLICACION"
				class="inputPosition_TABLON_EDICTAL_BOE_DATOS">
				<nobr>
					<html:hidden
						property="property(TABLON_EDICTAL_BOE_DATOS:FORMA_PUBLICACION)" />
					<ispac:htmlTextImageFrame
						property="property(FORMA_PUBLICACION_TABLON_EDICTAL_BOE_FORMPUBLI:SUSTITUTO)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" size="80"
						imageTabIndex="true"
						id="SEARCH_TABLON_EDICTAL_BOE_DATOS_FORMA_PUBLICACION"
						target="workframe"
						action="selectSubstitute.do?entity=TABLON_EDICTAL_BOE_FORMPUBLI"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
						<ispac:parameter
							name="SEARCH_TABLON_EDICTAL_BOE_DATOS_FORMA_PUBLICACION"
							id="property(TABLON_EDICTAL_BOE_DATOS:FORMA_PUBLICACION)"
							property="VALOR" />
						<ispac:parameter
							name="SEARCH_TABLON_EDICTAL_BOE_DATOS_FORMA_PUBLICACION"
							id="property(FORMA_PUBLICACION_TABLON_EDICTAL_BOE_FORMPUBLI:SUSTITUTO)"
							property="SUSTITUTO" />
					</ispac:htmlTextImageFrame>
				</nobr>
			</div>
			<div id="label_TABLON_EDICTAL_BOE_DATOS:DATOSPERSONALES"
				class="formsTitleB labelPosition_TABLON_EDICTAL_BOE_DATOS">
				<bean:write name="defaultForm"
					property="entityApp.label(TABLON_EDICTAL_BOE_DATOS:DATOSPERSONALES)" />
				:
			</div>
			<div id="data_TABLON_EDICTAL_BOE_DATOS:DATOSPERSONALES"
				class="inputPosition_TABLON_EDICTAL_BOE_DATOS">
				<nobr>
					<ispac:htmlTextImageFrame
						property="property(TABLON_EDICTAL_BOE_DATOS:DATOSPERSONALES)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" size="80"
						imageTabIndex="true"
						id="SEARCH_TABLON_EDICTAL_BOE_DATOS_DATOSPERSONALES"
						target="workframe" action="selectValue.do?entity=SPAC_TBL_009"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
						<ispac:parameter
							name="SEARCH_TABLON_EDICTAL_BOE_DATOS_DATOSPERSONALES"
							id="property(TABLON_EDICTAL_BOE_DATOS:DATOSPERSONALES)"
							property="VALOR" />
					</ispac:htmlTextImageFrame>
				</nobr>
			</div>
			<div id="label_TABLON_EDICTAL_BOE_DATOS:TIPOANUNCIO"
				class="formsTitleB labelPosition_TABLON_EDICTAL_BOE_DATOS">
				<bean:write name="defaultForm"
					property="entityApp.label(TABLON_EDICTAL_BOE_DATOS:TIPOANUNCIO)" />
				:
			</div>
			<div id="data_TABLON_EDICTAL_BOE_DATOS:TIPOANUNCIO"
				class="inputPosition_TABLON_EDICTAL_BOE_DATOS">
				<ispac:htmlText
					property="property(TABLON_EDICTAL_BOE_DATOS:TIPOANUNCIO)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="255">
				</ispac:htmlText>
			</div>
			<div id="label_TABLON_EDICTAL_BOE_DATOS:LGT"
				class="formsTitleB labelPosition_TABLON_EDICTAL_BOE_DATOS">
				<bean:write name="defaultForm"
					property="entityApp.label(TABLON_EDICTAL_BOE_DATOS:LGT)" />
				:
			</div>
			<div id="data_TABLON_EDICTAL_BOE_DATOS:LGT"
				class="inputPosition_TABLON_EDICTAL_BOE_DATOS">
				<nobr>
					<ispac:htmlTextImageFrame
						property="property(TABLON_EDICTAL_BOE_DATOS:LGT)" readonly="true"
						readonlyTag="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="80" imageTabIndex="true"
						id="SEARCH_TABLON_EDICTAL_BOE_DATOS_LGT" target="workframe"
						action="selectValue.do?entity=SPAC_TBL_009"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
						<ispac:parameter name="SEARCH_TABLON_EDICTAL_BOE_DATOS_LGT"
							id="property(TABLON_EDICTAL_BOE_DATOS:LGT)" property="VALOR" />
					</ispac:htmlTextImageFrame>
				</nobr>
			</div>
			
			<div id="label_TABLON_EDICTAL_BOE_DATOS:LUGAR"
				class="formsTitleB labelPosition_TABLON_EDICTAL_BOE_DATOS">
				<bean:write name="defaultForm"
					property="entityApp.label(TABLON_EDICTAL_BOE_DATOS:LUGAR)" />
				:
			</div>
			<div id="data_TABLON_EDICTAL_BOE_DATOS:LUGAR"
				class="inputPosition_TABLON_EDICTAL_BOE_DATOS">
				<ispac:htmlText property="property(TABLON_EDICTAL_BOE_DATOS:LUGAR)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="150">
				</ispac:htmlText>
			</div>
			<div id="label_TABLON_EDICTAL_BOE_DATOS:CARGONOMBRE"
				class="formsTitleB labelPosition_TABLON_EDICTAL_BOE_DATOS">
				<bean:write name="defaultForm"
					property="entityApp.label(TABLON_EDICTAL_BOE_DATOS:CARGONOMBRE)" />
				:
			</div>
			<div id="data_TABLON_EDICTAL_BOE_DATOS:CARGONOMBRE"
				class="inputPosition_TABLON_EDICTAL_BOE_DATOS">
				<ispac:htmlText
					property="property(TABLON_EDICTAL_BOE_DATOS:CARGONOMBRE)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="500">
				</ispac:htmlText>
			</div>
			
			<div id="label_TABLON_EDICTAL_BOE_DATOS:FECHAFIRMA"
				class="formsTitleB labelPosition_TABLON_EDICTAL_BOE_DATOS">
				<bean:write name="defaultForm"
					property="entityApp.label(TABLON_EDICTAL_BOE_DATOS:FECHAFIRMA)" />
				:
			</div>
			<div id="data_TABLON_EDICTAL_BOE_DATOS:FECHAFIRMA"
				class="inputPosition_TABLON_EDICTAL_BOE_DATOS">
				<nobr>
					<ispac:htmlTextCalendar
						property="property(TABLON_EDICTAL_BOE_DATOS:FECHAFIRMA)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%= buttoncalendar %>' format="dd/mm/yyyy"
						enablePast="true">
					</ispac:htmlTextCalendar>
				</nobr>
			</div>
			<div id="label_TABLON_EDICTAL_BOE_DATOS:EMAIL_INCIDENCIAS"
				class="formsTitleB labelPosition_TABLON_EDICTAL_BOE_DATOS">
				<bean:write name="defaultForm"
					property="entityApp.label(TABLON_EDICTAL_BOE_DATOS:EMAIL_INCIDENCIAS)" />
				:
			</div>
			<div id="data_TABLON_EDICTAL_BOE_DATOS:EMAIL_INCIDENCIAS"
				class="inputPosition_TABLON_EDICTAL_BOE_DATOS">
				<ispac:htmlText
					property="property(TABLON_EDICTAL_BOE_DATOS:EMAIL_INCIDENCIAS)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="100">
				</ispac:htmlText>
			</div>
			
			<div id="label_TABLON_EDICTAL_BOE_DATOS:IDENTIFICADORBOE"
				class="formsTitleB labelPosition_TABLON_EDICTAL_BOE_DATOS">
				<bean:write name="defaultForm"
					property="entityApp.label(TABLON_EDICTAL_BOE_DATOS:IDENTIFICADORBOE)" />
				:
			</div>
			<div id="data_TABLON_EDICTAL_BOE_DATOS:IDENTIFICADORBOE"
				class="inputPosition_TABLON_EDICTAL_BOE_DATOS">
				<ispac:htmlText
					property="property(TABLON_EDICTAL_BOE_DATOS:IDENTIFICADORBOE)"
					readonly="true" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="255">
				</ispac:htmlText>
			</div>
			
			<div id="label_TABLON_EDICTAL_BOE_DATOS:IDENTIFICADORANUNCIOBOE"
				class="formsTitleB labelPosition_TABLON_EDICTAL_BOE_DATOS">
				<bean:write name="defaultForm"
					property="entityApp.label(TABLON_EDICTAL_BOE_DATOS:IDENTIFICADORANUNCIOBOE)" />
				:
			</div>
			<div id="data_TABLON_EDICTAL_BOE_DATOS:IDENTIFICADORANUNCIOBOE"
				class="inputPosition_TABLON_EDICTAL_BOE_DATOS">
				<ispac:htmlText
					property="property(TABLON_EDICTAL_BOE_DATOS:IDENTIFICADORANUNCIOBOE)"
					readonly="true" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="255">
				</ispac:htmlText>
			</div>
			
			<div id="label_TABLON_EDICTAL_BOE_DATOS:IDENT_DEPART_DIR3"
				class="formsTitleB labelPosition_TABLON_EDICTAL_BOE_DATOS">
				<bean:write name="defaultForm"
					property="entityApp.label(TABLON_EDICTAL_BOE_DATOS:IDENT_DEPART_DIR3)" />
				:
			</div>
			<div id="data_TABLON_EDICTAL_BOE_DATOS:IDENT_DEPART_DIR3"
				class="inputPosition_TABLON_EDICTAL_BOE_DATOS">
				<ispac:htmlText
					property="property(TABLON_EDICTAL_BOE_DATOS:IDENT_DEPART_DIR3)"
					readonly="true" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="15" maxlength="255">
				</ispac:htmlText>
				<ispac:htmlText
					property="property(TABLON_EDICTAL_BOE_DATOS:NAME_DEPART_DIR3)"
					readonly="true" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="255">
				</ispac:htmlText>
			</div>	
			
		</div>
	</ispac:tab>
</ispac:tabs>
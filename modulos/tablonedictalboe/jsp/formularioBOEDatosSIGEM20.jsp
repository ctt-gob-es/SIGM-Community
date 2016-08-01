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
			style="position: relative; height: 460px; width: 600px">

			<div id="label_TABLON_EDICTAL_BOE_DATOS:PROCEDIMIENTO"
				style="position: absolute; top: 10px; left: 10px; width: 180px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(TABLON_EDICTAL_BOE_DATOS:PROCEDIMIENTO)" />
				:
			</div>
			<div id="data_TABLON_EDICTAL_BOE_DATOS:PROCEDIMIENTO"
				style="position: absolute; top: 10px; left: 170px; width: 100%;">
				<ispac:htmlText
					property="property(TABLON_EDICTAL_BOE_DATOS:PROCEDIMIENTO)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="400">
				</ispac:htmlText>
			</div>
			
			<div id="label_TABLON_EDICTAL_BOE_DATOS:FECHAPUBLICACION"
				style="position: absolute; top: 45px; left: 10px; width: 150px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(TABLON_EDICTAL_BOE_DATOS:FECHAPUBLICACION)" />
				:
			</div>
			<div id="data_TABLON_EDICTAL_BOE_DATOS:FECHAPUBLICACION"
				style="position: absolute; top: 45px; left: 170px; width: 100%;">
				<nobr>
					<ispac:htmlTextCalendar
						property="property(TABLON_EDICTAL_BOE_DATOS:FECHAPUBLICACION)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%=buttoncalendar%>' format="dd/mm/yyyy" enablePast="true">
					</ispac:htmlTextCalendar>
				</nobr>
			</div>
			
			<div id="label_TABLON_EDICTAL_BOE_DATOS:FORMA_PUBLICACION"
				style="position: absolute; top: 80px; left: 10px; width: 150px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(TABLON_EDICTAL_BOE_DATOS:FORMA_PUBLICACION)" />
				:
			</div>
			<div id="data_TABLON_EDICTAL_BOE_DATOS:FORMA_PUBLICACION"
				style="position: absolute; top: 80px; left: 170px; width: 100%;">
				<nobr>
					<html:hidden
						property="property(TABLON_EDICTAL_BOE_DATOS:FORMA_PUBLICACION)" />
					<ispac:htmlTextImageFrame
						property="property(FORMA_PUBLICACION_TABLON_EDICTAL_BOE_FORMPUBLI:SUSTITUTO)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" size="80"
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
				style="position: absolute; top: 115px; left: 10px; width: 150px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(TABLON_EDICTAL_BOE_DATOS:DATOSPERSONALES)" />
				:
			</div>
			<div id="data_TABLON_EDICTAL_BOE_DATOS:DATOSPERSONALES"
				style="position: absolute; top: 115px; left: 170px; width: 100%;">
				<nobr>
					<ispac:htmlTextImageFrame
						property="property(TABLON_EDICTAL_BOE_DATOS:DATOSPERSONALES)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" size="80"
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
				style="position: absolute; top: 150px; left: 10px; width: 150px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(TABLON_EDICTAL_BOE_DATOS:TIPOANUNCIO)" />
				:
			</div>
			<div id="data_TABLON_EDICTAL_BOE_DATOS:TIPOANUNCIO"
				style="position: absolute; top: 150px; left: 170px; width: 100%;">
				<ispac:htmlText
					property="property(TABLON_EDICTAL_BOE_DATOS:TIPOANUNCIO)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="255">
				</ispac:htmlText>
			</div>
			
			<div id="label_TABLON_EDICTAL_BOE_DATOS:LGT"
				style="position: absolute; top: 185px; left: 10px; width: 150px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(TABLON_EDICTAL_BOE_DATOS:LGT)" />
				:
			</div>
			<div id="data_TABLON_EDICTAL_BOE_DATOS:LGT"
				style="position: absolute; top: 185px; left: 170px; width: 100%;">
				<nobr>
					<ispac:htmlTextImageFrame
						property="property(TABLON_EDICTAL_BOE_DATOS:LGT)" readonly="true"
						readonlyTag="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="80"
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
				style="position: absolute; top: 220px; left: 10px; width: 150px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(TABLON_EDICTAL_BOE_DATOS:LUGAR)" />
				:
			</div>
			<div id="data_TABLON_EDICTAL_BOE_DATOS:LUGAR"
				style="position: absolute; top: 220px; left: 170px; width: 100%;">
				<ispac:htmlText property="property(TABLON_EDICTAL_BOE_DATOS:LUGAR)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="150">
				</ispac:htmlText>
			</div>
			
			<div id="label_TABLON_EDICTAL_BOE_DATOS:CARGONOMBRE"
				style="position: absolute; top: 255px; left: 10px; width: 150px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(TABLON_EDICTAL_BOE_DATOS:CARGONOMBRE)" />
				:
			</div>
			<div id="data_TABLON_EDICTAL_BOE_DATOS:CARGONOMBRE"
				style="position: absolute; top: 255px; left: 170px; width: 100%;">
				<ispac:htmlText
					property="property(TABLON_EDICTAL_BOE_DATOS:CARGONOMBRE)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="500">
				</ispac:htmlText>
			</div>
			
			<div id="label_TABLON_EDICTAL_BOE_DATOS:FECHAFIRMA"
				style="position: absolute; top: 290px; left: 10px; width: 150px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(TABLON_EDICTAL_BOE_DATOS:FECHAFIRMA)" />
				:
			</div>
			<div id="data_TABLON_EDICTAL_BOE_DATOS:FECHAFIRMA"
				style="position: absolute; top: 290px; left: 170px; width: 100%;">
				<nobr>
					<ispac:htmlTextCalendar
						property="property(TABLON_EDICTAL_BOE_DATOS:FECHAFIRMA)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%=buttoncalendar%>' format="dd/mm/yyyy" enablePast="true">
					</ispac:htmlTextCalendar>
				</nobr>
			</div>			

			<div id="label_TABLON_EDICTAL_BOE_DATOS:EMAIL_INCIDENCIAS"
				style="position: absolute; top: 325px; left: 10px; width: 150px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(TABLON_EDICTAL_BOE_DATOS:EMAIL_INCIDENCIAS)" />
				:
			</div>
			<div id="data_TABLON_EDICTAL_BOE_DATOS:EMAIL_INCIDENCIAS"
				style="position: absolute; top: 325px; left: 170px; width: 100%;">
				<ispac:htmlText
					property="property(TABLON_EDICTAL_BOE_DATOS:EMAIL_INCIDENCIAS)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="100">
				</ispac:htmlText>
			</div>			
			
			<div id="label_TABLON_EDICTAL_BOE_DATOS:IDENTIFICADORBOE"
				style="position: absolute; top: 360px; left: 10px; width: 150px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(TABLON_EDICTAL_BOE_DATOS:IDENTIFICADORBOE)" />
				:
			</div>
			<div id="data_TABLON_EDICTAL_BOE_DATOS:IDENTIFICADORBOE"
				style="position: absolute; top: 360px; left: 170px; width: 100%;">
				<ispac:htmlText
					property="property(TABLON_EDICTAL_BOE_DATOS:IDENTIFICADORBOE)"
					readonly="true" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="255">
				</ispac:htmlText>
			</div>
			<div id="label_TABLON_EDICTAL_BOE_DATOS:IDENTIFICADORANUNCIOBOE"
				style="position: absolute; top: 395px; left: 10px; width: 150px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(TABLON_EDICTAL_BOE_DATOS:IDENTIFICADORANUNCIOBOE)" />
				:
			</div>
			<div id="data_TABLON_EDICTAL_BOE_DATOS:IDENTIFICADORANUNCIOBOE"
				style="position: absolute; top: 395px; left: 170px; width: 100%;">
				<ispac:htmlText
					property="property(TABLON_EDICTAL_BOE_DATOS:IDENTIFICADORANUNCIOBOE)"
					readonly="true" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="255">
				</ispac:htmlText>
			</div>
			
			<div id="label_TABLON_EDICTAL_BOE_DATOS:IDENT_DEPART_DIR3"
				style="position: absolute; top: 430px; left: 10px; width: 150px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(TABLON_EDICTAL_BOE_DATOS:IDENT_DEPART_DIR3)" />
				:
			</div>
			<div id="data_TABLON_EDICTAL_BOE_DATOS:IDENT_DEPART_DIR3"
				style="position: absolute; top: 430px; left: 170px; width: 100%;">
				<ispac:htmlText
					property="property(TABLON_EDICTAL_BOE_DATOS:IDENT_DEPART_DIR3)"
					readonly="true" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="10">
				</ispac:htmlText>
			</div>

		</div>
	</ispac:tab>
</ispac:tabs>
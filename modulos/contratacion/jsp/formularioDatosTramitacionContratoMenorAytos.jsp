<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c1"%>
<c1:set var="aux0">
	<bean:write name="defaultForm"
		property="entityApp.label(CONTRATACION_DATOS_TRAMIT:CONTRATACION_DATOS_TRAMIT)" />
</c1:set><jsp:useBean id="aux0" type="java.lang.String" /><c1:set
	var="aux1">
	<bean:write name="defaultForm"
		property="entityApp.label(CONTRATACION_DATOS_CONTRATO:CONTRATACION_DATOS_CONTRATO)" />
</c1:set><jsp:useBean id="aux1" type="java.lang.String" />
<ispac:tabs>
	<ispac:tab title='<%=aux0%>'>
		<div id="dataBlock_CONTRATACION_DATOS_TRAMIT" style="position: relative; height: 260px; width: 600px">
			<div id="label_CONTRATACION_DATOS_TRAMIT:PERIODO_CONTRATO"
				style="position: absolute; top: 10px; left: 10px; width: 170px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DATOS_TRAMIT:PERIODO_CONTRATO)" />
				(*):
			</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:PERIODOCONTRATO_CONTRATO"
				style="position: absolute; top: 10px; left: 180px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DATOS_TRAMIT:PERIODO_CONTRATO)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="5" maxlength="50">
				</ispac:htmlText>
				(*)
			</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:PERIODUNITCODE_CONTRATO"
				style="position: absolute; top: 8px; left: 230px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(CONTRATACION_DATOS_TRAMIT:PERIODUNITCODE_CONTRATO)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="1" cols="7" 
					id="SEARCH_CONTRATACION_DATOS_TRAMIT:PERIODUNITCOD_CONTRATO" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_PERIODUNITCODE" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CONTRATACION_DATOS_TRAMIT:PERIODUNITCOD_CONTRATO" id="property(CONTRATACION_DATOS_TRAMIT:PERIODUNITCODE_CONTRATO)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:NUMOFERTAS"
				style="position: absolute; top: 35px; left: 10px; width: 170px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_TRAMIT:NUMOFERTAS)" />:</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:NUMOFERTAS"
				style="position: absolute; top: 35px; left: 180px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DATOS_TRAMIT:NUMOFERTAS)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="5" maxlength="500">
				</ispac:htmlText>
			</div>
			
			<!-- -------------------------------------------------------------------------------------------- -->
			<div id="label_SPAC_EXPEDIENTES:SEP_INTERESADO_PRINCIPAL"
				style="position: absolute; top: 65px; left: 10px; width: 620px"
				class="textbar">
				ADJUDICACIÓN
				<hr class="formbar" />
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:NIF_ADJUDICATARIA"
				style="position: absolute; top: 110px; left: 10px; width: 270px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DATOS_TRAMIT:NIF_ADJUDICATARIA)" />
				:
			</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:NIF_ADJUDICATARIA"
				style="position: absolute; top: 110px; left: 240px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DATOS_TRAMIT:NIF_ADJUDICATARIA)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="40" maxlength="10">
				</ispac:htmlText>
			</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:TIPOIDENTIFICADOR"
				style="position: absolute; top: 110px; left: 470px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(CONTRATACION_DATOS_TRAMIT:TIPOIDENTIFICADOR)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="1" cols="10" 
					id="SEARCH_CONTRATACION_DATOS_TRAMIT:TIPOIDENTIFICADOR" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_TIPO_IDENT" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CONTRATACION_DATOS_TRAMIT:TIPOIDENTIFICADOR" id="property(CONTRATACION_DATOS_TRAMIT:TIPOIDENTIFICADOR)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:EMP_ADJ_CONT"
				style="position: absolute; top: 135px; left: 10px; width: 410px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_TRAMIT:EMP_ADJ_CONT)" />:</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:EMP_ADJ_CONT"
				style="position: absolute; top: 135px; left: 240px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DATOS_TRAMIT:EMP_ADJ_CONT)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="300">
				</ispac:htmlText>
			</div>
			
			
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:IMP_ADJ_CONIVA"
				style="position: absolute; top: 160px; left: 10px; width: 410px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_TRAMIT:IMP_ADJ_CONIVA)" />:</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:IMP_ADJ_CONIVA"
				style="position: absolute; top: 160px; left: 240px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DATOS_TRAMIT:IMP_ADJ_CONIVA)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="30" maxlength="10">
				</ispac:htmlText>
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:IMP_ADJ_SINIVA"
				style="position: absolute; top: 160px; left: 500px; width: 410px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_TRAMIT:IMP_ADJ_SINIVA)" />:</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:IMP_ADJ_SINIVA"
				style="position: absolute; top: 160px; left: 730px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DATOS_TRAMIT:IMP_ADJ_SINIVA)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="30" maxlength="10">
				</ispac:htmlText>
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:FECHA_ADJUDICACION"
				style="position: absolute; top: 185px; left: 10px; width: 170px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DATOS_TRAMIT:FECHA_ADJUDICACION)" />
				:
			</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:FECHA_ADJUDICACION"
				style="position: absolute; top: 185px; left: 240px; width: 100%;">
				<nobr>
					<ispac:htmlTextCalendar
						property="property(CONTRATACION_DATOS_TRAMIT:FECHA_ADJUDICACION)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%=buttoncalendar%>' format="dd/mm/yyyy"
						enablePast="true">
					</ispac:htmlTextCalendar>
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:DOMICILIO_NOTIF_ADJ"
				style="position: absolute; top: 210px; left: 10px; width: 170px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_TRAMIT:DOMICILIO_NOTIF_ADJ)" />:</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:DOMICILIO_NOTIF_ADJ"
				style="position: absolute; top: 210px; left: 240px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DATOS_TRAMIT:DOMICILIO_NOTIF_ADJ)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="500">
				</ispac:htmlText>
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:NUM_CALLE_ADJ"
				style="position: absolute; top: 210px; left: 680px; width: 170px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_TRAMIT:NUM_CALLE_ADJ)" />:</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:NUM_CALLE_ADJ"
				style="position: absolute; top: 210px; left: 730px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DATOS_TRAMIT:NUM_CALLE_ADJ)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="10" maxlength="500">
				</ispac:htmlText>
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:CP"
				style="position: absolute; top: 210px; left: 820px; width: 170px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_TRAMIT:CP)" />:</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:CP"
				style="position: absolute; top: 210px; left: 860px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DATOS_TRAMIT:CP)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="10" maxlength="500">
				</ispac:htmlText>
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:NUTS_ADJ"
				style="position: absolute; top: 245px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DATOS_TRAMIT:NUTS_ADJ)" />:
			</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:NUTS_ADJ"
				style="position: absolute; top: 245px; left: 240px; width: 100%;">
				<nobr>
					<ispac:htmlTextImageFrame
						property="property(CONTRATACION_DATOS_TRAMIT:NUTS_ADJ)" readonly="true"
						readonlyTag="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="15"
						id="SEARCH_CONTRATACION_DATOS_TRAMIT_NUTS_ADJ" target="workframe"
						action="selectListadoCodicePliego.do?atributo=NUTS"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
						<ispac:parameter name="SEARCH_CONTRATACION_DATOS_TRAMIT_NUTS_ADJ" id="property(CONTRATACION_DATOS_TRAMIT:NUTS_ADJ)" property="SUSTITUTO" />
					</ispac:htmlTextImageFrame>
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:PAIS_ADJ"
				style="position: absolute; top: 245px; left: 400px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DATOS_TRAMIT:PAIS_ADJ)" />:
			</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:PAIS_ADJ"
				style="position: absolute; top: 245px; left: 460px; width: 100%;">
				<nobr>
					<ispac:htmlTextImageFrame
						property="property(CONTRATACION_DATOS_TRAMIT:PAIS_ADJ)" readonly="true"
						readonlyTag="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="15"
						id="SEARCH_CONTRATACION_DATOS_TRAMIT_PAIS_ADJ" target="workframe"
						action="selectListadoCodicePliego.do?atributo=COD_PAIS"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
						<ispac:parameter name="SEARCH_CONTRATACION_DATOS_TRAMIT_PAIS_ADJ" id="property(CONTRATACION_DATOS_TRAMIT:PAIS_ADJ)" property="SUSTITUTO" />
					</ispac:htmlTextImageFrame>
				</nobr>
			</div>
		</div>	
	</ispac:tab>
	
	<ispac:tab title='<%=aux1%>'>
		<div id="dataBlock_CONTRATACION_DATOS_CONTRATO"
			style="position: relative; height: 100px; width: 600px">
			<style type="text/css">
				.labelPosition_CONTRATACION_DATOS_CONTRATO {
					position: relative;
					left: 10px;
					top: 15px;
				}

				.inputPosition_CONTRATACION_DATOS_CONTRATO {
					position: relative;
					left: 180px;
				}
			</style>
			
			<div id="label_CONTRATACION_DATOS_CONTRATO:PROC_ADJ"
				style="position: absolute; top: 10px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DATOS_CONTRATO:PROC_ADJ)" />:
			</div>
			<div id="data_CONTRATACION_DATOS_CONTRATO:PROC_ADJ"
				style="position: absolute; top: 10px; left: 180px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(CONTRATACION_DATOS_CONTRATO:PROC_ADJ)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="1" cols="80" 
					id="SEARCH_CODICE_PLIEGOS_PROC_CONT_SECPUB" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_PROC_CONT_SECPUB" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CODICE_PLIEGOS_PROC_CONT_SECPUB" id="property(CONTRATACION_DATOS_CONTRATO:PROC_ADJ)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_DATOS_CONTRATO:TIPO_CONTRATO"
				style="position: absolute; top: 35px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DATOS_CONTRATO:TIPO_CONTRATO)" />:
			</div>
			<div id="data_CONTRATACION_DATOS_CONTRATO:TIPO_CONTRATO"
				style="position: absolute; top: 35px; left: 180px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(CONTRATACION_DATOS_CONTRATO:TIPO_CONTRATO)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="1" cols="80" 
					id="SEARCH_CODICE_PLIEGOS_TIPO_CONTRATO" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_TIPO_CONTRATO" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CODICE_PLIEGOS_TIPO_CONTRATO" id="property(CONTRATACION_DATOS_CONTRATO:TIPO_CONTRATO)" property="SUSTITUTO" />
						<ispac:parameter name="SEARCH_CODICE_PLIEGOS_TIPO_CONTRATO" id="property(CONTRATACION_DATOS_CONTRATO:CONTRATO_SUMIN)" property="CONTRATO_SUMIN" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_DATOS_CONTRATO:ORGANO_CONTRATACION"
				style="position: absolute; top: 60px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DATOS_CONTRATO:ORGANO_CONTRATACION)" />:
			</div>
			<div id="data_CONTRATACION_DATOS_CONTRATO:ORGANO_CONTRATACION"
				style="position: absolute; top: 60px; left: 180px; width: 100%;">
				<nobr>
					<html:hidden
						property="property(CONTRATACION_DATOS_CONTRATO:ORGANO_CONTRATACION)" />
					<ispac:htmlTextImageFrame
						property="property(ORGANO_CONTRATACION_CONTRATACION_ORGANO:SUSTITUTO)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" size="80"
						imageTabIndex="true"
						id="SEARCH_CONTRATACION_DATOS_CONTRATO_ORGANO_CONTRATACION"
						target="workframe"
						action="selectSubstitute.do?entity=CONTRATACION_ORGANO"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
						<ispac:parameter
							name="SEARCH_CONTRATACION_DATOS_CONTRATO_ORGANO_CONTRATACION"
							id="property(CONTRATACION_DATOS_CONTRATO:ORGANO_CONTRATACION)"
							property="VALOR" />
						<ispac:parameter
							name="SEARCH_CONTRATACION_DATOS_CONTRATO_ORGANO_CONTRATACION"
							id="property(ORGANO_CONTRATACION_CONTRATACION_ORGANO:SUSTITUTO)"
							property="SUSTITUTO" />
					</ispac:htmlTextImageFrame>
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_DATOS_CONTRATO:PROVINCIA_CONTRATO"
				style="position: absolute; top: 85px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DATOS_CONTRATO:PROVINCIA_CONTRATO)" />:
			</div>
			<div id="data_CONTRATACION_DATOS_CONTRATO:PROVINCIA_CONTRATO"
				style="position: absolute; top: 85px; left: 180px; width: 100%;">
				<nobr>
					<ispac:htmlTextImageFrame
						property="property(CONTRATACION_DATOS_CONTRATO:PROVINCIA_CONTRATO)" readonly="true"
						readonlyTag="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="25"
						id="SEARCH_CONTRATACION_DATOS_CONTRATO_PROVINCIA_CONTRATO" target="workframe"
						action="selectListadoCodicePliego.do?atributo=NUTS"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
						<ispac:parameter name="SEARCH_CONTRATACION_DATOS_CONTRATO_PROVINCIA_CONTRATO" id="property(CONTRATACION_DATOS_CONTRATO:PROVINCIA_CONTRATO)" property="SUSTITUTO" />
					</ispac:htmlTextImageFrame>
				</nobr>
			</div>
			
			
		</div>
	</ispac:tab>
</ispac:tabs>
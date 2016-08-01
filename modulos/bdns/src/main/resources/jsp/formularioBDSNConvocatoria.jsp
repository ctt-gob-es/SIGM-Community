<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c1"%>
<c1:set var="aux0">
	<bean:write name="defaultForm"
		property="entityApp.label(BDNS_IGAE_CONVOCATORIA:BDNS_IGAE_CONVOCATORIA)" />
</c1:set><jsp:useBean id="aux0" type="java.lang.String" />
<ispac:tabs>
	<ispac:tab title='<%=aux0%>'>
		<div id="dataBlock_BDNS_IGAE_CONVOCATORIA"
			style="position: relative; height: 1500px; width: 600px">
			<style type="text/css">
.labelPosition_BDNS_IGAE_CONVOCATORIA {
	position: relative;
	left: 10px;
	top: 15px;
}

.inputPosition_BDNS_IGAE_CONVOCATORIA {
	position: relative;
	left: 180px;
}
</style>
			<div id="label_BDNS_IGAE_CONVOCATORIA:IDCONVOCATORIA"
				class="formsTitleB labelPosition_BDNS_IGAE_CONVOCATORIA">
				<bean:write name="defaultForm"
					property="entityApp.label(BDNS_IGAE_CONVOCATORIA:IDCONVOCATORIA)" />
				:
			</div>
			<div id="data_BDNS_IGAE_CONVOCATORIA:IDCONVOCATORIA"
				class="inputPosition_BDNS_IGAE_CONVOCATORIA">
				<ispac:htmlText
					property="property(BDNS_IGAE_CONVOCATORIA:IDCONVOCATORIA)"
					readonly="true" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="40" maxlength="18">
				</ispac:htmlText>
			</div>
			<div id="label_BDNS_IGAE_CONVOCATORIA:DESCRIPCIONCOV"
				class="formsTitleB labelPosition_BDNS_IGAE_CONVOCATORIA">
				<bean:write name="defaultForm"
					property="entityApp.label(BDNS_IGAE_CONVOCATORIA:DESCRIPCIONCOV)" />
				:
			</div>
			<div id="data_BDNS_IGAE_CONVOCATORIA:DESCRIPCIONCOV"
				class="inputPosition_BDNS_IGAE_CONVOCATORIA">
				<ispac:htmlTextarea
					property="property(BDNS_IGAE_CONVOCATORIA:DESCRIPCIONCOV)"
					readonly="false" propertyReadonly="readonly"
					styleClass="textareaAsunto" styleClassReadonly="textareaAsuntoRO"
					rows="4" cols="80" tabindex="255">
				</ispac:htmlTextarea>
			</div>
			<br />
			<br />
			<div id="label_SPAC_EXPEDIENTES:SEP_INTERESADO_PRINCIPAL"
				class="textbar">
				INFORMACION DE LAS BASES REGULADORAS
				<hr class="formbar" />
			</div>
			<br />

			<div id="label_BDNS_IGAE_CONVOCATORIA:NOMENCLATURA"
				class="formsTitleB labelPosition_BDNS_IGAE_CONVOCATORIA">
				<bean:write name="defaultForm"
					property="entityApp.label(BDNS_IGAE_CONVOCATORIA:NOMENCLATURA)" />
				:
			</div>
			<div id="data_BDNS_IGAE_CONVOCATORIA:NOMENCLATURA"
				class="inputPosition_BDNS_IGAE_CONVOCATORIA">
				<ispac:htmlText
					property="property(BDNS_IGAE_CONVOCATORIA:NOMENCLATURA)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="50">
				</ispac:htmlText>
			</div>
			<div id="label_BDNS_IGAE_CONVOCATORIA:DIARIOOFICIALBR"
				class="formsTitleB labelPosition_BDNS_IGAE_CONVOCATORIA">
				<bean:write name="defaultForm"
					property="entityApp.label(BDNS_IGAE_CONVOCATORIA:DIARIOOFICIALBR)" />
				:
			</div>
			<div id="data_BDNS_IGAE_CONVOCATORIA:DIARIOOFICIALBR"
				class="inputPosition_BDNS_IGAE_CONVOCATORIA">
				<ispac:htmlText
					property="property(BDNS_IGAE_CONVOCATORIA:DIARIOOFICIALBR)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="50" value="34 - B.O.P. DE CIUDAD REAL">
				</ispac:htmlText>				
			</div>
			<div id="label_BDNS_IGAE_CONVOCATORIA:DESCRIPCIONBR"
				class="formsTitleB labelPosition_BDNS_IGAE_CONVOCATORIA">
				<bean:write name="defaultForm"
					property="entityApp.label(BDNS_IGAE_CONVOCATORIA:DESCRIPCIONBR)" />
				:
			</div>
			<div id="data_BDNS_IGAE_CONVOCATORIA:DESCRIPCIONBR"
				class="inputPosition_BDNS_IGAE_CONVOCATORIA">
				<ispac:htmlTextarea
					property="property(BDNS_IGAE_CONVOCATORIA:DESCRIPCIONBR)"
					readonly="false" propertyReadonly="readonly"
					styleClass="textareaAsunto" styleClassReadonly="textareaAsuntoRO"
					rows="4" cols="80" tabindex="255">
				</ispac:htmlTextarea>
			</div>
			<div id="label_BDNS_IGAE_CONVOCATORIA:URLESPBR"
				class="formsTitleB labelPosition_BDNS_IGAE_CONVOCATORIA">
				<bean:write name="defaultForm"
					property="entityApp.label(BDNS_IGAE_CONVOCATORIA:URLESPBR)" />
				:
			</div>
			<div id="data_BDNS_IGAE_CONVOCATORIA:URLESPBR"
				class="inputPosition_BDNS_IGAE_CONVOCATORIA">
				<ispac:htmlText property="property(BDNS_IGAE_CONVOCATORIA:URLESPBR)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="500">
				</ispac:htmlText>
			</div>

			<br />
			<br />
			<div id="label_SPAC_EXPEDIENTES:SEP_INTERESADO_PRINCIPAL"
				class="textbar">
				IDENTIFICACIÓN ASOCIADA A LAS SOLICITUDES
				<hr class="formbar" />
			</div>
			<br />

			<div id="label_BDNS_IGAE_CONVOCATORIA:ABIERTO"
				class="formsTitleB labelPosition_BDNS_IGAE_CONVOCATORIA">
				<bean:write name="defaultForm"
					property="entityApp.label(BDNS_IGAE_CONVOCATORIA:ABIERTO)" />
				:
			</div>
			<div id="data_BDNS_IGAE_CONVOCATORIA:ABIERTO"
				class="inputPosition_BDNS_IGAE_CONVOCATORIA">
				<nobr>
					<ispac:htmlTextImageFrame
						property="property(BDNS_IGAE_CONVOCATORIA:ABIERTO)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" size="80"
						imageTabIndex="true" id="SEARCH_BDNS_IGAE_CONVOCATORIA_ABIERTO"
						target="workframe" action="selectValue.do?entity=SPAC_TBL_009"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
						<ispac:parameter name="SEARCH_BDNS_IGAE_CONVOCATORIA_ABIERTO"
							id="property(BDNS_IGAE_CONVOCATORIA:ABIERTO)" property="VALOR" />
					</ispac:htmlTextImageFrame>
				</nobr>
			</div>
			<div id="label_BDNS_IGAE_CONVOCATORIA:INICIOSOLICITUD"
				class="formsTitleB labelPosition_BDNS_IGAE_CONVOCATORIA">
				<bean:write name="defaultForm"
					property="entityApp.label(BDNS_IGAE_CONVOCATORIA:INICIOSOLICITUD)" />
				:
			</div>
			<div id="data_BDNS_IGAE_CONVOCATORIA:INICIOSOLICITUD"
				class="inputPosition_BDNS_IGAE_CONVOCATORIA">
				<ispac:htmlText
					property="property(BDNS_IGAE_CONVOCATORIA:INICIOSOLICITUD)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="100">
				</ispac:htmlText>
			</div>
			<div id="label_BDNS_IGAE_CONVOCATORIA:FECHAINICIOSOLICITUD"
				class="formsTitleB labelPosition_BDNS_IGAE_CONVOCATORIA">
				<bean:write name="defaultForm"
					property="entityApp.label(BDNS_IGAE_CONVOCATORIA:FECHAINICIOSOLICITUD)" />
				:
			</div>
			<div id="data_BDNS_IGAE_CONVOCATORIA:FECHAINICIOSOLICITUD"
				class="inputPosition_BDNS_IGAE_CONVOCATORIA">
				<nobr>
					<ispac:htmlTextCalendar
						property="property(BDNS_IGAE_CONVOCATORIA:FECHAINICIOSOLICITUD)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%=buttoncalendar%>' format="dd/mm/yyyy" enablePast="true">
					</ispac:htmlTextCalendar>
				</nobr>
			</div>
			<div id="label_BDNS_IGAE_CONVOCATORIA:FINSOLICITUD"
				class="formsTitleB labelPosition_BDNS_IGAE_CONVOCATORIA">
				<bean:write name="defaultForm"
					property="entityApp.label(BDNS_IGAE_CONVOCATORIA:FINSOLICITUD)" />
				:
			</div>
			<div id="data_BDNS_IGAE_CONVOCATORIA:FINSOLICITUD"
				class="inputPosition_BDNS_IGAE_CONVOCATORIA">
				<ispac:htmlText
					property="property(BDNS_IGAE_CONVOCATORIA:FINSOLICITUD)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="100">
				</ispac:htmlText>
			</div>
			<div id="label_BDNS_IGAE_CONVOCATORIA:FECHAFINSOLICITUD"
				class="formsTitleB labelPosition_BDNS_IGAE_CONVOCATORIA">
				<bean:write name="defaultForm"
					property="entityApp.label(BDNS_IGAE_CONVOCATORIA:FECHAFINSOLICITUD)" />
				:
			</div>
			<div id="data_BDNS_IGAE_CONVOCATORIA:FECHAFINSOLICITUD"
				class="inputPosition_BDNS_IGAE_CONVOCATORIA">
				<nobr>
					<ispac:htmlTextCalendar
						property="property(BDNS_IGAE_CONVOCATORIA:FECHAFINSOLICITUD)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%=buttoncalendar%>' format="dd/mm/yyyy" enablePast="true">
					</ispac:htmlTextCalendar>
				</nobr>
			</div>
			<div id="label_BDNS_IGAE_CONVOCATORIA:SEDE"
				class="formsTitleB labelPosition_BDNS_IGAE_CONVOCATORIA">
				<bean:write name="defaultForm"
					property="entityApp.label(BDNS_IGAE_CONVOCATORIA:SEDE)" />
				:
			</div>
			<div id="data_BDNS_IGAE_CONVOCATORIA:SEDE"
				class="inputPosition_BDNS_IGAE_CONVOCATORIA">
				<ispac:htmlText property="property(BDNS_IGAE_CONVOCATORIA:SEDE)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="500">
				</ispac:htmlText>
			</div>
			<div id="label_BDNS_IGAE_CONVOCATORIA:JUSTIFICACION"
				class="formsTitleB labelPosition_BDNS_IGAE_CONVOCATORIA">
				<bean:write name="defaultForm"
					property="entityApp.label(BDNS_IGAE_CONVOCATORIA:JUSTIFICACION)" />
				:
			</div>
			<div id="data_BDNS_IGAE_CONVOCATORIA:JUSTIFICACION"
				class="inputPosition_BDNS_IGAE_CONVOCATORIA">
				<nobr>
					<html:hidden
						property="property(BDNS_IGAE_CONVOCATORIA:JUSTIFICACION)" />
					<ispac:htmlTextImageFrame
						property="property(JUSTIFICACION_BDNS_MOMENTOS_JUSTIFICACION:SUSTITUTO)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" size="80"
						imageTabIndex="true"
						id="SEARCH_BDNS_IGAE_CONVOCATORIA_JUSTIFICACION"
						target="workframe"
						action="selectSubstitute.do?entity=BDNS_MOMENTOS_JUSTIFICACION"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
						<ispac:parameter
							name="SEARCH_BDNS_IGAE_CONVOCATORIA_JUSTIFICACION"
							id="property(BDNS_IGAE_CONVOCATORIA:JUSTIFICACION)"
							property="VALOR" />
						<ispac:parameter
							name="SEARCH_BDNS_IGAE_CONVOCATORIA_JUSTIFICACION"
							id="property(JUSTIFICACION_BDNS_MOMENTOS_JUSTIFICACION:SUSTITUTO)"
							property="SUSTITUTO" />
					</ispac:htmlTextImageFrame>
				</nobr>
				
			</div>
			<div id="label_BDNS_IGAE_CONVOCATORIA:FECHAJUSTIFICACION"
				class="formsTitleB labelPosition_BDNS_IGAE_CONVOCATORIA">
				<bean:write name="defaultForm"
					property="entityApp.label(BDNS_IGAE_CONVOCATORIA:FECHAJUSTIFICACION)" />
				:
			</div>
			<div id="data_BDNS_IGAE_CONVOCATORIA:FECHAJUSTIFICACION"
				class="inputPosition_BDNS_IGAE_CONVOCATORIA">
				<nobr>
					<ispac:htmlTextCalendar
						property="property(BDNS_IGAE_CONVOCATORIA:FECHAJUSTIFICACION)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%=buttoncalendar%>' format="dd/mm/yyyy" enablePast="true">
					</ispac:htmlTextCalendar>
				</nobr>
			</div>

		<!-- 	<br />
			<br />
			
			
			<div id="label_SPAC_EXPEDIENTES:SEP_INTERESADO_PRINCIPAL"
				class="textbar">
				INFORMACION DE UN TIPO DE FINANCIACION
				<hr class="formbar" />
			</div>
			<br />

			<div id="label_BDNS_IGAE_CONVOCATORIA:TIPOFINANCIACION"
				class="formsTitleB labelPosition_BDNS_IGAE_CONVOCATORIA">
				<bean:write name="defaultForm"
					property="entityApp.label(BDNS_IGAE_CONVOCATORIA:TIPOFINANCIACION)" />
				:
			</div>
			<div id="data_BDNS_IGAE_CONVOCATORIA:TIPOFINANCIACION"
				class="inputPosition_BDNS_IGAE_CONVOCATORIA">
				<nobr>
					<ispac:htmlTextMultivalueImageFrame
						tableValidationType="substitute"
						property="propertyMultivalue(TIPOFINANCIACION_BDNS_TIPO_FINANCIACION:SUSTITUTO)"
						propertyDestination="propertyMultivalue(BDNS_IGAE_CONVOCATORIA:TIPOFINANCIACION)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" size="80"
						imageTabIndex="true"
						id="SEARCH_BDNS_IGAE_CONVOCATORIA_TIPOFINANCIACION"
						target="workframe"
						action="selectSubstitute.do?entity=BDNS_TIPO_FINANCIACION"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480" divWidth="700">
						<ispac:parameterMultivalue
							name="SEARCH_BDNS_IGAE_CONVOCATORIA_TIPOFINANCIACION"
							id="propertyMultivalue(BDNS_IGAE_CONVOCATORIA:TIPOFINANCIACION)"
							setMethod="id" property="VALOR"
							propertyDestination="propertyMultivalue(BDNS_IGAE_CONVOCATORIA:TIPOFINANCIACION)" />
						<ispac:parameterMultivalue
							name="SEARCH_BDNS_IGAE_CONVOCATORIA_TIPOFINANCIACION"
							id="propertyMultivalue(TIPOFINANCIACION_BDNS_TIPO_FINANCIACION:SUSTITUTO)"
							setMethod="id" property="SUSTITUTO"
							propertyDestination="propertyMultivalue(BDNS_IGAE_CONVOCATORIA:TIPOFINANCIACION)" />
					</ispac:htmlTextMultivalueImageFrame>
				</nobr>
			</div>
			<div id="label_BDNS_IGAE_CONVOCATORIA:IMPORTEFINANCIACION"
				style="position: absolute; top: 660px; left: 700px; width: 100%;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(BDNS_IGAE_CONVOCATORIA:IMPORTEFINANCIACION)" />
				:
			</div>
			<div id="data_BDNS_IGAE_CONVOCATORIA:IMPORTEFINANCIACION"
				style="position: absolute; top: 660px; left: 850px; width: 100%;">
				<ispac:htmlTextMultivalue
					property="propertyMultivalue(BDNS_IGAE_CONVOCATORIA:IMPORTEFINANCIACION)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="30" divWidth="200"
					maxlength="1024">
				</ispac:htmlTextMultivalue>
			</div>

			<br />
			<br />
			<div id="label_SPAC_EXPEDIENTES:SEP_INTERESADO_PRINCIPAL"
				class="textbar">
				INFORMACION DE LOS FONDOS UE CONFINANCIADORES DE LA CONVOCATORIA
				<hr class="formbar" />
			</div>
			<br />

			<div id="label_BDNS_IGAE_CONVOCATORIA:TIPOFONDO"
				class="formsTitleB labelPosition_BDNS_IGAE_CONVOCATORIA">
				<bean:write name="defaultForm"
					property="entityApp.label(BDNS_IGAE_CONVOCATORIA:TIPOFONDO)" />
				:
			</div>
			<div id="data_BDNS_IGAE_CONVOCATORIA:TIPOFONDO"
				class="inputPosition_BDNS_IGAE_CONVOCATORIA">
				<nobr>
					<ispac:htmlTextMultivalueImageFrame
						tableValidationType="substitute"
						property="propertyMultivalue(TIPOFONDO_BDNS_TIPO_FONDO:SUSTITUTO)"
						propertyDestination="propertyMultivalue(BDNS_IGAE_CONVOCATORIA:TIPOFONDO)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" size="80"
						imageTabIndex="true" id="SEARCH_BDNS_IGAE_CONVOCATORIA_TIPOFONDO"
						target="workframe"
						action="selectSubstitute.do?entity=BDNS_TIPO_FONDO"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480" divWidth="700">
						<ispac:parameterMultivalue
							name="SEARCH_BDNS_IGAE_CONVOCATORIA_TIPOFONDO"
							id="propertyMultivalue(BDNS_IGAE_CONVOCATORIA:TIPOFONDO)"
							setMethod="id" property="VALOR"
							propertyDestination="propertyMultivalue(BDNS_IGAE_CONVOCATORIA:TIPOFONDO)" />
						<ispac:parameterMultivalue
							name="SEARCH_BDNS_IGAE_CONVOCATORIA_TIPOFONDO"
							id="propertyMultivalue(TIPOFONDO_BDNS_TIPO_FONDO:SUSTITUTO)"
							setMethod="id" property="SUSTITUTO"
							propertyDestination="propertyMultivalue(BDNS_IGAE_CONVOCATORIA:TIPOFONDO)" />
					</ispac:htmlTextMultivalueImageFrame>
				</nobr>
			</div>

			<div id="label_BDNS_IGAE_CONVOCATORIA:IMPORTEFONDO"
				style="position: absolute; top: 835px; left: 700px; width: 100%;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(BDNS_IGAE_CONVOCATORIA:IMPORTEFONDO)" />
				:
			</div>
			<div id="data_BDNS_IGAE_CONVOCATORIA:IMPORTEFONDO"
				style="position: absolute; top: 835px; left: 850px; width: 100%;">
				<ispac:htmlTextMultivalue
					property="propertyMultivalue(BDNS_IGAE_CONVOCATORIA:IMPORTEFONDO)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="30" divWidth="200"
					maxlength="1024">
				</ispac:htmlTextMultivalue>
			</div>
			
			-->
			<br />
			<br />
			<div id="label_SPAC_EXPEDIENTES:SEP_INTERESADO_PRINCIPAL"
				class="textbar">
				INFORMACION DE LAS ACTIVIDADES ECONOMICAS
				<hr class="formbar" />
			</div>
			<br />

			<div id="label_BDNS_IGAE_CONVOCATORIA:SECTOR"
				class="formsTitleB labelPosition_BDNS_IGAE_CONVOCATORIA">
				<bean:write name="defaultForm"
					property="entityApp.label(BDNS_IGAE_CONVOCATORIA:SECTOR)" />
				:
			</div>
			<div id="data_BDNS_IGAE_CONVOCATORIA:SECTOR"
				class="inputPosition_BDNS_IGAE_CONVOCATORIA">
				<nobr>
					<ispac:htmlTextMultivalueImageFrame
						tableValidationType="substitute"
						property="propertyMultivalue(SECTOR_BDNS_SECT_ECONOM:SUSTITUTO)"
						propertyDestination="propertyMultivalue(BDNS_IGAE_CONVOCATORIA:SECTOR)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" size="80"
						imageTabIndex="true" id="SEARCH_BDNS_IGAE_CONVOCATORIA_SECTOR"
						target="workframe"
						action="selectSubstitute.do?entity=BDNS_SECT_ECONOM"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480" divWidth="700">
						<ispac:parameterMultivalue
							name="SEARCH_BDNS_IGAE_CONVOCATORIA_SECTOR"
							id="propertyMultivalue(BDNS_IGAE_CONVOCATORIA:SECTOR)"
							setMethod="id" property="VALOR"
							propertyDestination="propertyMultivalue(BDNS_IGAE_CONVOCATORIA:SECTOR)" />
						<ispac:parameterMultivalue
							name="SEARCH_BDNS_IGAE_CONVOCATORIA_SECTOR"
							id="propertyMultivalue(SECTOR_BDNS_SECT_ECONOM:SUSTITUTO)"
							setMethod="id" property="SUSTITUTO"
							propertyDestination="propertyMultivalue(BDNS_IGAE_CONVOCATORIA:SECTOR)" />
					</ispac:htmlTextMultivalueImageFrame>
				</nobr>
			</div>
			
			
			<br />
			<br />
			<div id="label_SPAC_EXPEDIENTES:SEP_INTERESADO_PRINCIPAL"
				class="textbar">
				IDENTIFICACIÓN DE LAS REGIONES GEOGRAFICAS
				<hr class="formbar" />
			</div>
			<br />

			<div id="label_BDNS_IGAE_CONVOCATORIA:REGION"
				class="formsTitleB labelPosition_BDNS_IGAE_CONVOCATORIA">
				<bean:write name="defaultForm"
					property="entityApp.label(BDNS_IGAE_CONVOCATORIA:REGION)" />
				:
			</div>
			<div id="data_BDNS_IGAE_CONVOCATORIA:REGION"
				class="inputPosition_BDNS_IGAE_CONVOCATORIA">
				<ispac:htmlText
					property="property(BDNS_IGAE_CONVOCATORIA:REGION)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="500" value="ES422 - Ciudad Real">
				</ispac:htmlText>
			</div>

			<br />
			<br />
	<!-- 		<div id="label_SPAC_EXPEDIENTES:SEP_INTERESADO_PRINCIPAL"
				class="textbar">
				INFORMACION PARA AYUDAS DE TIPO ADE
				<hr class="formbar" />
			</div>
			<br />

			<div id="label_BDNS_IGAE_CONVOCATORIA:AUTORIZACIONADE"
				class="formsTitleB labelPosition_BDNS_IGAE_CONVOCATORIA">
				<bean:write name="defaultForm"
					property="entityApp.label(BDNS_IGAE_CONVOCATORIA:AUTORIZACIONADE)" />
				:
			</div>
			<div id="data_BDNS_IGAE_CONVOCATORIA:AUTORIZACIONADE"
				class="inputPosition_BDNS_IGAE_CONVOCATORIA">
				<nobr>
					<html:hidden
						property="property(BDNS_IGAE_CONVOCATORIA:AUTORIZACIONADE)" />
					<ispac:htmlTextImageFrame
						property="property(AUTORIZACIONADE_BDNS_AUTORIZACIONADE:SUSTITUTO)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" size="80"
						imageTabIndex="true"
						id="SEARCH_BDNS_IGAE_CONVOCATORIA_AUTORIZACIONADE"
						target="workframe"
						action="selectSubstitute.do?entity=BDNS_AUTORIZACIONADE"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
						<ispac:parameter
							name="SEARCH_BDNS_IGAE_CONVOCATORIA_AUTORIZACIONADE"
							id="property(BDNS_IGAE_CONVOCATORIA:AUTORIZACIONADE)"
							property="VALOR" />
						<ispac:parameter
							name="SEARCH_BDNS_IGAE_CONVOCATORIA_AUTORIZACIONADE"
							id="property(AUTORIZACIONADE_BDNS_AUTORIZACIONADE:SUSTITUTO)"
							property="SUSTITUTO" />
					</ispac:htmlTextImageFrame>
				</nobr>
			</div>
			<div id="label_BDNS_IGAE_CONVOCATORIA:REFERENCIAUE"
				class="formsTitleB labelPosition_BDNS_IGAE_CONVOCATORIA">
				<bean:write name="defaultForm"
					property="entityApp.label(BDNS_IGAE_CONVOCATORIA:REFERENCIAUE)" />
				:
			</div>
			<div id="data_BDNS_IGAE_CONVOCATORIA:REFERENCIAUE"
				class="inputPosition_BDNS_IGAE_CONVOCATORIA">
				<nobr>
					<html:hidden
						property="property(BDNS_IGAE_CONVOCATORIA:REFERENCIAUE)" />
					<ispac:htmlTextImageFrame
						property="property(REFERENCIAUE_BDNS_REGLAMENTOUE:SUSTITUTO)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" size="80"
						imageTabIndex="true"
						id="SEARCH_BDNS_IGAE_CONVOCATORIA_REFERENCIAUE" target="workframe"
						action="selectSubstitute.do?entity=BDNS_REGLAMENTOUE"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
						<ispac:parameter name="SEARCH_BDNS_IGAE_CONVOCATORIA_REFERENCIAUE"
							id="property(BDNS_IGAE_CONVOCATORIA:REFERENCIAUE)"
							property="VALOR" />
						<ispac:parameter name="SEARCH_BDNS_IGAE_CONVOCATORIA_REFERENCIAUE"
							id="property(REFERENCIAUE_BDNS_REGLAMENTOUE:SUSTITUTO)"
							property="SUSTITUTO" />
					</ispac:htmlTextImageFrame>
				</nobr>
			</div>
			<div id="label_BDNS_IGAE_CONVOCATORIA:REGLAMENTOUE"
				class="formsTitleB labelPosition_BDNS_IGAE_CONVOCATORIA">
				<bean:write name="defaultForm"
					property="entityApp.label(BDNS_IGAE_CONVOCATORIA:REGLAMENTOUE)" />
				:
			</div>
			<div id="data_BDNS_IGAE_CONVOCATORIA:REGLAMENTOUE"
				class="inputPosition_BDNS_IGAE_CONVOCATORIA">
				<ispac:htmlText
					property="property(BDNS_IGAE_CONVOCATORIA:REGLAMENTOUE)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="500">
				</ispac:htmlText>
			</div>
 
			<br />
			<br />
			<div id="label_SPAC_EXPEDIENTES:SEP_INTERESADO_PRINCIPAL"
				class="textbar">
				INFORMACION DE OBJETIVOS DEL REGLAMENTO DE EXENCION POR CATEGORIA DE
				AYUDA
				<hr class="formbar" />
			</div>
			<br />

			<div id="label_BDNS_IGAE_CONVOCATORIA:OBJETIVO"
				class="formsTitleB labelPosition_BDNS_IGAE_CONVOCATORIA">
				<bean:write name="defaultForm"
					property="entityApp.label(BDNS_IGAE_CONVOCATORIA:OBJETIVO)" />
				:
			</div>
			<div id="data_BDNS_IGAE_CONVOCATORIA:OBJETIVO"
				class="inputPosition_BDNS_IGAE_CONVOCATORIA">
				<nobr>
					<ispac:htmlTextMultivalueImageFrame
						tableValidationType="substitute"
						property="propertyMultivalue(OBJETIVO_BDNS_OBJETIVOS_REGLAMENT:SUSTITUTO)"
						propertyDestination="propertyMultivalue(BDNS_IGAE_CONVOCATORIA:OBJETIVO)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" size="80"
						imageTabIndex="true" id="SEARCH_BDNS_IGAE_CONVOCATORIA_OBJETIVO"
						target="workframe"
						action="selectSubstitute.do?entity=BDNS_OBJETIVOS_REGLAMENT"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480" divWidth="700">
						<ispac:parameterMultivalue
							name="SEARCH_BDNS_IGAE_CONVOCATORIA_OBJETIVO"
							id="propertyMultivalue(BDNS_IGAE_CONVOCATORIA:OBJETIVO)"
							setMethod="id" property="VALOR"
							propertyDestination="propertyMultivalue(BDNS_IGAE_CONVOCATORIA:OBJETIVO)" />
						<ispac:parameterMultivalue
							name="SEARCH_BDNS_IGAE_CONVOCATORIA_OBJETIVO"
							id="propertyMultivalue(OBJETIVO_BDNS_OBJETIVOS_REGLAMENT:SUSTITUTO)"
							setMethod="id" property="SUSTITUTO"
							propertyDestination="propertyMultivalue(BDNS_IGAE_CONVOCATORIA:OBJETIVO)" />
					</ispac:htmlTextMultivalueImageFrame>
				</nobr>
			</div>
-->
			<br />
			<br />
			<div id="label_SPAC_EXPEDIENTES:SEP_INTERESADO_PRINCIPAL"
				class="textbar">
				INSTRUMENTOS DE AYUDA
				<hr class="formbar" />
			</div>
			<br />

			<div id="label_BDNS_IGAE_CONVOCATORIA:INSTRUMENTO"
				class="formsTitleB labelPosition_BDNS_IGAE_CONVOCATORIA">
				<bean:write name="defaultForm"
					property="entityApp.label(BDNS_IGAE_CONVOCATORIA:INSTRUMENTO)" />
				:
			</div>
			<div id="data_BDNS_IGAE_CONVOCATORIA:INSTRUMENTO"
				class="inputPosition_BDNS_IGAE_CONVOCATORIA">
				<nobr>
					<ispac:htmlTextMultivalueImageFrame
						tableValidationType="substitute"
						property="propertyMultivalue(INSTRUMENTO_BDNS_INSTRUMENTO:SUSTITUTO)"
						propertyDestination="propertyMultivalue(BDNS_IGAE_CONVOCATORIA:INSTRUMENTO)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" size="80"
						imageTabIndex="true"
						id="SEARCH_BDNS_IGAE_CONVOCATORIA_INSTRUMENTO" target="workframe"
						action="selectSubstitute.do?entity=BDNS_INSTRUMENTO"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480" divWidth="700">
						<ispac:parameterMultivalue
							name="SEARCH_BDNS_IGAE_CONVOCATORIA_INSTRUMENTO"
							id="propertyMultivalue(BDNS_IGAE_CONVOCATORIA:INSTRUMENTO)"
							setMethod="id" property="VALOR"
							propertyDestination="propertyMultivalue(BDNS_IGAE_CONVOCATORIA:INSTRUMENTO)" />
						<ispac:parameterMultivalue
							name="SEARCH_BDNS_IGAE_CONVOCATORIA_INSTRUMENTO"
							id="propertyMultivalue(INSTRUMENTO_BDNS_INSTRUMENTO:SUSTITUTO)"
							setMethod="id" property="SUSTITUTO"
							propertyDestination="propertyMultivalue(BDNS_IGAE_CONVOCATORIA:INSTRUMENTO)" />
					</ispac:htmlTextMultivalueImageFrame>
				</nobr>
			</div>
			<div id="label_BDNS_IGAE_CONVOCATORIA:TIPOBENEFICIARIO"
				class="formsTitleB labelPosition_BDNS_IGAE_CONVOCATORIA">
				<bean:write name="defaultForm"
					property="entityApp.label(BDNS_IGAE_CONVOCATORIA:TIPOBENEFICIARIO)" />
				:
			</div>
			<div id="data_BDNS_IGAE_CONVOCATORIA:TIPOBENEFICIARIO"
				class="inputPosition_BDNS_IGAE_CONVOCATORIA">
				<nobr>
					<ispac:htmlTextMultivalueImageFrame
						tableValidationType="substitute"
						property="propertyMultivalue(TIPOBENEFICIARIO_BDNS_BENEFICIARIO:SUSTITUTO)"
						propertyDestination="propertyMultivalue(BDNS_IGAE_CONVOCATORIA:TIPOBENEFICIARIO)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" size="80"
						imageTabIndex="true"
						id="SEARCH_BDNS_IGAE_CONVOCATORIA_TIPOBENEFICIARIO"
						target="workframe"
						action="selectSubstitute.do?entity=BDNS_BENEFICIARIO"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480" divWidth="700">
						<ispac:parameterMultivalue
							name="SEARCH_BDNS_IGAE_CONVOCATORIA_TIPOBENEFICIARIO"
							id="propertyMultivalue(BDNS_IGAE_CONVOCATORIA:TIPOBENEFICIARIO)"
							setMethod="id" property="VALOR"
							propertyDestination="propertyMultivalue(BDNS_IGAE_CONVOCATORIA:TIPOBENEFICIARIO)" />
						<ispac:parameterMultivalue
							name="SEARCH_BDNS_IGAE_CONVOCATORIA_TIPOBENEFICIARIO"
							id="propertyMultivalue(TIPOBENEFICIARIO_BDNS_BENEFICIARIO:SUSTITUTO)"
							setMethod="id" property="SUSTITUTO"
							propertyDestination="propertyMultivalue(BDNS_IGAE_CONVOCATORIA:TIPOBENEFICIARIO)" />
					</ispac:htmlTextMultivalueImageFrame>
				</nobr>
			</div>
			<div id="label_BDNS_IGAE_CONVOCATORIA:FINALIDAD"
				class="formsTitleB labelPosition_BDNS_IGAE_CONVOCATORIA">
				<bean:write name="defaultForm"
					property="entityApp.label(BDNS_IGAE_CONVOCATORIA:FINALIDAD)" />
				:
			</div>
			<div id="data_BDNS_IGAE_CONVOCATORIA:FINALIDAD"
				class="inputPosition_BDNS_IGAE_CONVOCATORIA">
				<nobr>
					<html:hidden property="property(BDNS_IGAE_CONVOCATORIA:FINALIDAD)" />
					<ispac:htmlTextImageFrame
						property="property(FINALIDAD_BDNS_FINALIDAD:SUSTITUTO)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" size="80"
						imageTabIndex="true" id="SEARCH_BDNS_IGAE_CONVOCATORIA_FINALIDAD"
						target="workframe"
						action="selectSubstitute.do?entity=BDNS_FINALIDAD"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
						<ispac:parameter name="SEARCH_BDNS_IGAE_CONVOCATORIA_FINALIDAD"
							id="property(BDNS_IGAE_CONVOCATORIA:FINALIDAD)" property="VALOR" />
						<ispac:parameter name="SEARCH_BDNS_IGAE_CONVOCATORIA_FINALIDAD"
							id="property(FINALIDAD_BDNS_FINALIDAD:SUSTITUTO)"
							property="SUSTITUTO" />
					</ispac:htmlTextImageFrame>
				</nobr>
			</div>
			<div id="label_BDNS_IGAE_CONVOCATORIA:IMPACTOGENERO"
				class="formsTitleB labelPosition_BDNS_IGAE_CONVOCATORIA">
				<bean:write name="defaultForm"
					property="entityApp.label(BDNS_IGAE_CONVOCATORIA:IMPACTOGENERO)" />
				:
			</div>
			<div id="data_BDNS_IGAE_CONVOCATORIA:IMPACTOGENERO"
				class="inputPosition_BDNS_IGAE_CONVOCATORIA">
				<nobr>
					<html:hidden
						property="property(BDNS_IGAE_CONVOCATORIA:IMPACTOGENERO)" />
					<ispac:htmlTextImageFrame
						property="property(IMPACTOGENERO_BDNS_IMPACTOGENERO:SUSTITUTO)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" size="80"
						imageTabIndex="true"
						id="SEARCH_BDNS_IGAE_CONVOCATORIA_IMPACTOGENERO"
						target="workframe"
						action="selectSubstitute.do?entity=BDNS_IMPACTOGENERO"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
						<ispac:parameter
							name="SEARCH_BDNS_IGAE_CONVOCATORIA_IMPACTOGENERO"
							id="property(BDNS_IGAE_CONVOCATORIA:IMPACTOGENERO)"
							property="VALOR" />
						<ispac:parameter
							name="SEARCH_BDNS_IGAE_CONVOCATORIA_IMPACTOGENERO"
							id="property(IMPACTOGENERO_BDNS_IMPACTOGENERO:SUSTITUTO)"
							property="SUSTITUTO" />
					</ispac:htmlTextImageFrame>
				</nobr>
			</div>
			<div id="label_BDNS_IGAE_CONVOCATORIA:CONCESIONPUBLICABLE"
				class="formsTitleB labelPosition_BDNS_IGAE_CONVOCATORIA">
				<bean:write name="defaultForm"
					property="entityApp.label(BDNS_IGAE_CONVOCATORIA:CONCESIONPUBLICABLE)" />
				:
			</div>
			<div id="data_BDNS_IGAE_CONVOCATORIA:CONCESIONPUBLICABLE"
				class="inputPosition_BDNS_IGAE_CONVOCATORIA">
				<nobr>
					<ispac:htmlTextImageFrame
						property="property(BDNS_IGAE_CONVOCATORIA:CONCESIONPUBLICABLE)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" size="80"
						imageTabIndex="true"
						id="SEARCH_BDNS_IGAE_CONVOCATORIA_CONCESIONPUBLICABLE"
						target="workframe" action="selectValue.do?entity=SPAC_TBL_009"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
						<ispac:parameter
							name="SEARCH_BDNS_IGAE_CONVOCATORIA_CONCESIONPUBLICABLE"
							id="property(BDNS_IGAE_CONVOCATORIA:CONCESIONPUBLICABLE)"
							property="VALOR" />
					</ispac:htmlTextImageFrame>
				</nobr>
			</div>
			<div id="label_BDNS_IGAE_CONVOCATORIA:SUBVENCIONNOMINATIVA"
				class="formsTitleB labelPosition_BDNS_IGAE_CONVOCATORIA">
				<bean:write name="defaultForm"
					property="entityApp.label(BDNS_IGAE_CONVOCATORIA:SUBVENCIONNOMINATIVA)" />
				:
			</div>
			<div id="data_BDNS_IGAE_CONVOCATORIA:SUBVENCIONNOMINATIVA"
				class="inputPosition_BDNS_IGAE_CONVOCATORIA">
				<nobr>
					<ispac:htmlTextImageFrame
						property="property(BDNS_IGAE_CONVOCATORIA:SUBVENCIONNOMINATIVA)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" size="80"
						imageTabIndex="true"
						id="SEARCH_BDNS_IGAE_CONVOCATORIA_SUBVENCIONNOMINATIVA"
						target="workframe" action="selectValue.do?entity=SPAC_TBL_009"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
						<ispac:parameter
							name="SEARCH_BDNS_IGAE_CONVOCATORIA_SUBVENCIONNOMINATIVA"
							id="property(BDNS_IGAE_CONVOCATORIA:SUBVENCIONNOMINATIVA)"
							property="VALOR" />
					</ispac:htmlTextImageFrame>
				</nobr>
			</div>

			<br />
			<br />
			<div id="label_SPAC_EXPEDIENTES:SEP_INTERESADO_PRINCIPAL"
				class="textbar">
				INFORMACION DEL EXTRACTO DE LA CONVOCATORIA
				<hr class="formbar" />
			</div>
			<br />

			<div id="label_BDNS_IGAE_CONVOCATORIA:DIARIOOFICIAL"
				class="formsTitleB labelPosition_BDNS_IGAE_CONVOCATORIA">
				<bean:write name="defaultForm"
					property="entityApp.label(BDNS_IGAE_CONVOCATORIA:DIARIOOFICIAL)" />
				:
			</div>
			<div id="data_BDNS_IGAE_CONVOCATORIA:DIARIOOFICIAL"
				class="inputPosition_BDNS_IGAE_CONVOCATORIA">
				<ispac:htmlText
					property="property(BDNS_IGAE_CONVOCATORIA:DIARIOOFICIAL)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="500" value="34 - B.O.P. DE CIUDAD REAL">
				</ispac:htmlText>
			</div>
			<div id="label_BDNS_IGAE_CONVOCATORIA:TITULOEXTRACTO"
				class="formsTitleB labelPosition_BDNS_IGAE_CONVOCATORIA">
				<bean:write name="defaultForm"
					property="entityApp.label(BDNS_IGAE_CONVOCATORIA:TITULOEXTRACTO)" />
				:
			</div>
			<div id="data_BDNS_IGAE_CONVOCATORIA:TITULOEXTRACTO"
				class="inputPosition_BDNS_IGAE_CONVOCATORIA">
				<ispac:htmlTextarea
					property="property(BDNS_IGAE_CONVOCATORIA:TITULOEXTRACTO)"
					readonly="false" propertyReadonly="readonly"
					styleClass="textareaAsunto" styleClassReadonly="textareaAsuntoRO"
					rows="4" cols="80" tabindex="500">
				</ispac:htmlTextarea>
			</div>
			<br>
			<br>
			<FONT COLOR="red">Se deberá copiar el texto del documento OpenOffice y no del pdf para insertarlo en el campo 'Texto Extracto'</FONT>			
			<div id="label_BDNS_IGAE_CONVOCATORIA:TEXTOEXTRACTO"
				class="formsTitleB labelPosition_BDNS_IGAE_CONVOCATORIA">
				<bean:write name="defaultForm"
					property="entityApp.label(BDNS_IGAE_CONVOCATORIA:TEXTOEXTRACTO)" />
				:
			</div>
			<div id="data_BDNS_IGAE_CONVOCATORIA:TEXTOEXTRACTO"
				class="inputPosition_BDNS_IGAE_CONVOCATORIA">
				<ispac:htmlTextarea
					property="property(BDNS_IGAE_CONVOCATORIA:TEXTOEXTRACTO)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" rows="30" cols="150">
				</ispac:htmlTextarea>
			</div>
			<div id="label_BDNS_IGAE_CONVOCATORIA:FECHAFIRMA"
				class="formsTitleB labelPosition_BDNS_IGAE_CONVOCATORIA">
				<bean:write name="defaultForm"
					property="entityApp.label(BDNS_IGAE_CONVOCATORIA:FECHAFIRMA)" />
				:
			</div>
			<div id="data_BDNS_IGAE_CONVOCATORIA:FECHAFIRMA"
				class="inputPosition_BDNS_IGAE_CONVOCATORIA">
				<nobr>
					<ispac:htmlTextCalendar
						property="property(BDNS_IGAE_CONVOCATORIA:FECHAFIRMA)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%=buttoncalendar%>' format="dd/mm/yyyy" enablePast="true">
					</ispac:htmlTextCalendar>
				</nobr>
			</div>
			<div id="label_BDNS_IGAE_CONVOCATORIA:LUGARFIRMA"
				class="formsTitleB labelPosition_BDNS_IGAE_CONVOCATORIA">
				<bean:write name="defaultForm"
					property="entityApp.label(BDNS_IGAE_CONVOCATORIA:LUGARFIRMA)" />
				:
			</div>
			<div id="data_BDNS_IGAE_CONVOCATORIA:LUGARFIRMA"
				class="inputPosition_BDNS_IGAE_CONVOCATORIA">
				<ispac:htmlText
					property="property(BDNS_IGAE_CONVOCATORIA:LUGARFIRMA)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="100">
				</ispac:htmlText>
			</div>
			<div id="label_BDNS_IGAE_CONVOCATORIA:NOMBREFIRMANTE"
				class="formsTitleB labelPosition_BDNS_IGAE_CONVOCATORIA">
				<bean:write name="defaultForm"
					property="entityApp.label(BDNS_IGAE_CONVOCATORIA:NOMBREFIRMANTE)" />
				:
			</div>
			<div id="data_BDNS_IGAE_CONVOCATORIA:NOMBREFIRMANTE"
				class="inputPosition_BDNS_IGAE_CONVOCATORIA">
				<ispac:htmlText
					property="property(BDNS_IGAE_CONVOCATORIA:NOMBREFIRMANTE)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="255">
				</ispac:htmlText>
			</div>
		</div>
	</ispac:tab>
</ispac:tabs>
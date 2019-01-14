<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c1"%>
<script type="text/javascript"
	src='<ispac:rewrite href="../scripts/utils.js"/>'></script>
<link rel="stylesheet" href='<ispac:rewrite href="css/estilos.css"/>' />
<c1:set var="aux0">
	<bean:write name="defaultForm"
		property="entityApp.label(EXPR_EEF:EXPR_EEF)" />
</c1:set><jsp:useBean id="aux0" type="java.lang.String" />
<ispac:tabs>
	<ispac:tab title='<%=aux0%>'>
		<div id="dataBlock_EXPR_EEF"
			style="position: relative; height: 1100px; width: 600px;">
			<div id="label_EXPR_EEF:EEF"
				style="position: absolute; top: 10px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(EXPR_EEF:EEF)" />
				:
			</div>
			<div id="data_EXPR_EEF:EEF"
				style="position: absolute; top: 10px; left: 130px; width: 100%;">
				<ispac:htmlText property="property(EXPR_EEF:EEF)" readonly="false"
					propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="50">
				</ispac:htmlText>
			</div>
			<div id="label_EXPR_EEF:NOMBRE_OBRA"
				style="position: absolute; top: 35px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(EXPR_EEF:NOMBRE_OBRA)" />
				:
			</div>
			<div id="data_EXPR_EEF:NOMBRE_OBRA"
				style="position: absolute; top: 35px; left: 130px; width: 100%;">
				<ispac:htmlText property="property(EXPR_EEF:NOMBRE_OBRA)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="255">
				</ispac:htmlText>
			</div>
			<div id="label_EXPR_EEF:EXPEDIENTE_OBRA"
				style="position: absolute; top: 60px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(EXPR_EEF:EXPEDIENTE_OBRA)" />
				:
			</div>
			<div id="data_EXPR_EEF:EXPEDIENTE_OBRA"
				style="position: absolute; top: 60px; left: 130px; width: 100%;">
				<ispac:htmlText property="property(EXPR_EEF:EXPEDIENTE_OBRA)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="50">
				</ispac:htmlText>
			</div>
			<div id="label_EXPR_EEF:APROBACION"
				style="position: absolute; top: 85px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(EXPR_EEF:APROBACION)" />
				:
			</div>
			<div id="data_EXPR_EEF:APROBACION"
				style="position: absolute; top: 85px; left: 130px; width: 100%;">
				<ispac:htmlText property="property(EXPR_EEF:APROBACION)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="255">
				</ispac:htmlText>
			</div>
			<div id="label_EXPR_EEF:FECHA_INICIACION"
				style="position: absolute; top: 110px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(EXPR_EEF:FECHA_INICIACION)" />
				:
			</div>
			<div id="data_EXPR_EEF:FECHA_INICIACION"
				style="position: absolute; top: 110px; left: 130px; width: 100%;">
				<nobr>
					<ispac:htmlTextCalendar
						property="property(EXPR_EEF:FECHA_INICIACION)" readonly="false"
						propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%=buttoncalendar%>' format="dd/mm/yyyy"
						enablePast="true">
					</ispac:htmlTextCalendar>
				</nobr>
			</div>
			<div id="label_EXPR_EEF:FECHA_APROBACION"
				style="position: absolute; top: 135px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(EXPR_EEF:FECHA_APROBACION)" />
				:
			</div>
			<div id="data_EXPR_EEF:FECHA_APROBACION"
				style="position: absolute; top: 135px; left: 130px; width: 100%;">
				<nobr>
					<ispac:htmlTextCalendar
						property="property(EXPR_EEF:FECHA_APROBACION)" readonly="false"
						propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%=buttoncalendar%>' format="dd/mm/yyyy"
						enablePast="true">
					</ispac:htmlTextCalendar>
				</nobr>
			</div>
			<div id="label_EXPR_EEF:PLAZO_ALEGACIONES"
				style="position: absolute; top: 175px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(EXPR_EEF:PLAZO_ALEGACIONES)" />
				:
			</div>
			<div id="data_EXPR_EEF:PLAZO_ALEGACIONES"
				style="position: absolute; top: 175px; left: 130px; width: 100%;">
				<ispac:htmlText property="property(EXPR_EEF:PLAZO_ALEGACIONES)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="10">
				</ispac:htmlText>
			</div>
			<div id="label_EXPR_EEF:FECHA_FINALIZACION"
				style="position: absolute; top: 225px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(EXPR_EEF:FECHA_FINALIZACION)" />
				:
			</div>
			<div id="data_EXPR_EEF:FECHA_FINALIZACION"
				style="position: absolute; top: 225px; left: 130px; width: 100%;">
				<nobr>
					<ispac:htmlTextCalendar
						property="property(EXPR_EEF:FECHA_FINALIZACION)" readonly="false"
						propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%=buttoncalendar%>' format="dd/mm/yyyy"
						enablePast="true">
					</ispac:htmlTextCalendar>
				</nobr>
			</div>
			<div id="label_EXPR_EEF:FECHA_APROBACION_DEFINITIVA"
				style="position: absolute; top: 250px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(EXPR_EEF:FECHA_APROBACION_DEFINITIVA)" />
				:
			</div>
			<div id="data_EXPR_EEF:FECHA_APROBACION_DEFINITIVA"
				style="position: absolute; top: 250px; left: 130px; width: 100%;">
				<nobr>
					<ispac:htmlTextCalendar
						property="property(EXPR_EEF:FECHA_APROBACION_DEFINITIVA)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%=buttoncalendar%>' format="dd/mm/yyyy"
						enablePast="true">
					</ispac:htmlTextCalendar>
				</nobr>
			</div>
			<div id="label_EXPR_EEF:FECHA_ANUNCIO_LEVANTAMIENTO"
				style="position: absolute; top: 290px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(EXPR_EEF:FECHA_ANUNCIO_LEVANTAMIENTO)" />
				:
			</div>
			<div id="data_EXPR_EEF:FECHA_ANUNCIO_LEVANTAMIENTO"
				style="position: absolute; top: 290px; left: 130px; width: 100%;">
				<nobr>
					<ispac:htmlTextCalendar
						property="property(EXPR_EEF:FECHA_ANUNCIO_LEVANTAMIENTO)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%=buttoncalendar%>' format="dd/mm/yyyy"
						enablePast="true">
					</ispac:htmlTextCalendar>
				</nobr>
			</div>
			<div id="label_EXPR_EEF:COMPARACENCIA_LUGAR"
				style="position: absolute; top: 340px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(EXPR_EEF:COMPARACENCIA_LUGAR)" />
				:
			</div>
			<div id="data_EXPR_EEF:COMPARACENCIA_LUGAR"
				style="position: absolute; top: 340px; left: 130px; width: 100%;">
				<ispac:htmlTextarea
					property="property(EXPR_EEF:COMPARACENCIA_LUGAR)" readonly="false"
					propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" rows="2" cols="80">
				</ispac:htmlTextarea>
			</div>
			<div id="label_EXPR_EEF:COMPARACENCIA_FECHA"
				style="position: absolute; top: 395px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(EXPR_EEF:COMPARACENCIA_FECHA)" />
				:
			</div>
			<div id="data_EXPR_EEF:COMPARACENCIA_FECHA"
				style="position: absolute; top: 395px; left: 130px; width: 100%;">
				<nobr>
					<ispac:htmlTextCalendar
						property="property(EXPR_EEF:COMPARACENCIA_FECHA)" readonly="false"
						propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="14"
						image='<%=buttoncalendar%>' format="dd/mm/yyyy"
						enablePast="true">
					</ispac:htmlTextCalendar>
				</nobr>
			</div>
			<div id="label_EXPR_EEF:FECHA_FIRMA"
				style="position: absolute; top: 445px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(EXPR_EEF:FECHA_FIRMA)" />
				:
			</div>
			<div id="data_EXPR_EEF:FECHA_FIRMA"
				style="position: absolute; top: 445px; left: 130px; width: 100%;">
				<nobr>
					<ispac:htmlTextCalendar property="property(EXPR_EEF:FECHA_FIRMA)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%=buttoncalendar%>' format="dd/mm/yyyy"
						enablePast="true">
					</ispac:htmlTextCalendar>
				</nobr>
			</div>
			<div id="label_EXPR_EEF:CARGO"
				style="position: absolute; top: 500px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(EXPR_EEF:CARGO)" />
				:
			</div>
			<div id="data_EXPR_EEF:CARGO"
				style="position: absolute; top: 500px; left: 130px; width: 100%;">
				<ispac:htmlText property="property(EXPR_EEF:CARGO)" readonly="false"
					propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="255">
				</ispac:htmlText>
			</div>
			<div id="label_EXPR_EEF:PERSONA"
				style="position: absolute; top: 525px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(EXPR_EEF:PERSONA)" />
				:
			</div>
			<div id="data_EXPR_EEF:PERSONA"
				style="position: absolute; top: 525px; left: 130px; width: 100%;">
				<ispac:htmlText property="property(EXPR_EEF:PERSONA)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="255">
				</ispac:htmlText>
			</div>
			<div id="label_EXPR_EEF:FECHA_PLENO_RESOLUCION"
				style="position: absolute; top: 550px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(EXPR_EEF:FECHA_PLENO_RESOLUCION)" />
				:
			</div>
			<div id="data_EXPR_EEF:FECHA_PLENO_RESOLUCION"
				style="position: absolute; top: 550px; left: 130px; width: 100%;">
				<nobr>
					<ispac:htmlTextCalendar
						property="property(EXPR_EEF:FECHA_PLENO_RESOLUCION)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%=buttoncalendar%>' format="dd/mm/yyyy"
						enablePast="true">
					</ispac:htmlTextCalendar>
				</nobr>
			</div>
			<div id="label_EXPR_EEF:FECHA_FIRMA_RESOLUCION"
				style="position: absolute; top: 590px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(EXPR_EEF:FECHA_FIRMA_RESOLUCION)" />
				:
			</div>
			<div id="data_EXPR_EEF:FECHA_FIRMA_RESOLUCION"
				style="position: absolute; top: 590px; left: 130px; width: 100%;">
				<nobr>
					<ispac:htmlTextCalendar
						property="property(EXPR_EEF:FECHA_FIRMA_RESOLUCION)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%=buttoncalendar%>' format="dd/mm/yyyy"
						enablePast="true">
					</ispac:htmlTextCalendar>
				</nobr>
			</div>
			<div id="label_EXPR_EEF:FECHA_FIRMA_DECRETO"
				style="position: absolute; top: 630px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(EXPR_EEF:FECHA_FIRMA_DECRETO)" />
				:
			</div>
			<div id="data_EXPR_EEF:FECHA_FIRMA_DECRETO"
				style="position: absolute; top: 630px; left: 130px; width: 100%;">
				<nobr>
					<ispac:htmlTextCalendar
						property="property(EXPR_EEF:FECHA_FIRMA_DECRETO)" readonly="false"
						propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%=buttoncalendar%>' format="dd/mm/yyyy"
						enablePast="true">
					</ispac:htmlTextCalendar>
				</nobr>
			</div>
			<div id="label_EXPR_EEF:INGENIERO_RESPONSABLE"
				style="position: absolute; top: 670px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(EXPR_EEF:INGENIERO_RESPONSABLE)" />
				:
			</div>
			<div id="data_EXPR_EEF:INGENIERO_RESPONSABLE"
				style="position: absolute; top: 670px; left: 130px; width: 100%;">
				<ispac:htmlText property="property(EXPR_EEF:INGENIERO_RESPONSABLE)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="255">
				</ispac:htmlText>
			</div>
			<div id="label_EXPR_EEF:FECHA_PUBLICACION_ANUNCIO"
				style="position: absolute; top: 695px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(EXPR_EEF:FECHA_PUBLICACION_ANUNCIO)" />
				:
			</div>
			<div id="data_EXPR_EEF:FECHA_PUBLICACION_ANUNCIO"
				style="position: absolute; top: 695px; left: 130px; width: 100%;">
				<nobr>
					<ispac:htmlTextCalendar
						property="property(EXPR_EEF:FECHA_PUBLICACION_ANUNCIO)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%=buttoncalendar%>' format="dd/mm/yyyy"
						enablePast="true">
					</ispac:htmlTextCalendar>
				</nobr>
			</div>
			<div id="label_EXPR_EEF:FECHA_CONSIGNA"
				style="position: absolute; top: 725px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(EXPR_EEF:FECHA_CONSIGNA)" />
				:
			</div>
			<div id="data_EXPR_EEF:FECHA_CONSIGNA"
				style="position: absolute; top: 725px; left: 130px; width: 100%;">
				<nobr>
					<ispac:htmlTextCalendar
						property="property(EXPR_EEF:FECHA_CONSIGNA)" readonly="false"
						propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%=buttoncalendar%>' format="dd/mm/yyyy"
						enablePast="true">
					</ispac:htmlTextCalendar>
				</nobr>
			</div>
			<div id="label_EXPR_EEF:FECHA_TRANSFERENCIA"
				style="position: absolute; top: 750px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(EXPR_EEF:FECHA_TRANSFERENCIA)" />
				:
			</div>
			<div id="data_EXPR_EEF:FECHA_TRANSFERENCIA"
				style="position: absolute; top: 750px; left: 130px; width: 100%;">
				<nobr>
					<ispac:htmlTextCalendar
						property="property(EXPR_EEF:FECHA_TRANSFERENCIA)" readonly="false"
						propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%=buttoncalendar%>' format="dd/mm/yyyy"
						enablePast="true">
					</ispac:htmlTextCalendar>
				</nobr>
			</div>
			<div id="label_EXPR_EEF:FECHA_HORA_OCUPACION"
				style="position: absolute; top: 780px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(EXPR_EEF:FECHA_HORA_OCUPACION)" />
				:
			</div>
			<div id="data_EXPR_EEF:FECHA_HORA_OCUPACION"
				style="position: absolute; top: 780px; left: 130px; width: 100%;">
				<ispac:htmlText property="property(EXPR_EEF:FECHA_HORA_OCUPACION)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="50">
				</ispac:htmlText>
			</div>
			<div id="label_EXPR_EEF:LUGAR_OCUPACION"
				style="position: absolute; top: 810px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(EXPR_EEF:LUGAR_OCUPACION)" />
				:
			</div>
			<div id="data_EXPR_EEF:LUGAR_OCUPACION"
				style="position: absolute; top: 810px; left: 130px; width: 100%;">
				<ispac:htmlText property="property(EXPR_EEF:LUGAR_OCUPACION)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="255">
				</ispac:htmlText>
			</div>
			<div id="label_EXPR_EEF:ACTAS_PREVIAS_DIAS_NECESARIOS"
				style="position: absolute; top: 835px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(EXPR_EEF:ACTAS_PREVIAS_DIAS_NECESARIOS)" />
				:
			</div>
			<div id="data_EXPR_EEF:ACTAS_PREVIAS_DIAS_NECESARIOS"
				style="position: absolute; top: 835px; left: 130px; width: 100%;">
				<ispac:htmlText
					property="property(EXPR_EEF:ACTAS_PREVIAS_DIAS_NECESARIOS)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="3">
				</ispac:htmlText>
			</div>
			<div id="label_EXPR_EEF:ACTAS_PREVIAS_DIAS_SELECCIONAD"
				style="position: absolute; top: 860px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(EXPR_EEF:ACTAS_PREVIAS_DIAS_SELECCIONAD)" />
				:
			</div>
			<div id="data_EXPR_EEF:ACTAS_PREVIAS_DIAS_SELECCIONAD"
				style="position: absolute; top: 860px; left: 130px; width: 100%;">
				<ispac:htmlTextarea
					property="property(EXPR_EEF:ACTAS_PREVIAS_DIAS_SELECCIONAD)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" rows="2" cols="80">
				</ispac:htmlTextarea>
			</div>
			<div id="label_EXPR_EEF:OCUPACION_DIAS_NECESARIOS"
				style="position: absolute; top: 915px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(EXPR_EEF:OCUPACION_DIAS_NECESARIOS)" />
				:
			</div>
			<div id="data_EXPR_EEF:OCUPACION_DIAS_NECESARIOS"
				style="position: absolute; top: 915px; left: 130px; width: 100%;">
				<ispac:htmlText
					property="property(EXPR_EEF:OCUPACION_DIAS_NECESARIOS)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="3">
				</ispac:htmlText>
			</div>
			<div id="label_EXPR_EEF:OCUPACION_DIAS_SELECCIONAD"
				style="position: absolute; top: 945px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(EXPR_EEF:OCUPACION_DIAS_SELECCIONAD)" />
				:
			</div>
			<div id="data_EXPR_EEF:OCUPACION_DIAS_SELECCIONAD"
				style="position: absolute; top: 945px; left: 130px; width: 100%;">
				<ispac:htmlTextarea
					property="property(EXPR_EEF:OCUPACION_DIAS_SELECCIONAD)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" rows="2" cols="80">
				</ispac:htmlTextarea>
			</div>
			<div id="label_EXPR_EEF:INTERVALO_MUNICIPIOS"
				style="position: absolute; top: 1000px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(EXPR_EEF:INTERVALO_MUNICIPIOS)" />
				:
			</div>
			<div id="data_EXPR_EEF:INTERVALO_MUNICIPIOS"
				style="position: absolute; top: 1000px; left: 130px; width: 100%;">
				<ispac:htmlText property="property(EXPR_EEF:INTERVALO_MUNICIPIOS)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="5">
				</ispac:htmlText>
			</div>
			
			<div id="label_EXPR_EEF:INTERVALO_FINCA"
				style="position: absolute; top: 1055px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(EXPR_EEF:INTERVALO_FINCA)" />
				:
			</div>
			<div id="data_EXPR_EEF:INTERVALO_FINCA"
				style="position: absolute; top: 1055px; left: 130px; width: 100%;">
				<ispac:htmlText property="property(EXPR_EEF:INTERVALO_FINCA)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="5">
				</ispac:htmlText>
			</div>
		</div>
	</ispac:tab>
</ispac:tabs>
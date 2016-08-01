<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c1"%>
<c1:set var="aux0">
	<bean:write name="defaultForm"
		property="entityApp.label(CONTRATACION_RENDCUENT_CONTA:CONTRATACION_RENDCUENT_CONTA)" />
</c1:set><jsp:useBean id="aux0" type="java.lang.String" />
<ispac:tabs>
	<ispac:tab title='<%=aux0%>'>
		<div id="dataBlock_CONTRATACION_RENDCUENT_CONTA"
			style="position: relative; height: 285px; width: 600px">
			<div id="label_CONTRATACION_RENDCUENT_CONTA:NOMBRE"
				style="position: absolute; top: 10px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_RENDCUENT_CONTA:NOMBRE)" />
				:
			</div>
			<div id="data_CONTRATACION_RENDCUENT_CONTA:NOMBRE"
				style="position: absolute; top: 10px; left: 130px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_RENDCUENT_CONTA:NOMBRE)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="100">
				</ispac:htmlText>
			</div>
			<div id="label_CONTRATACION_RENDCUENT_CONTA:APELLIDO1"
				style="position: absolute; top: 35px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_RENDCUENT_CONTA:APELLIDO1)" />
				:
			</div>
			<div id="data_CONTRATACION_RENDCUENT_CONTA:APELLIDO1"
				style="position: absolute; top: 35px; left: 130px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_RENDCUENT_CONTA:APELLIDO1)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="100">
				</ispac:htmlText>
			</div>
			<div id="label_CONTRATACION_RENDCUENT_CONTA:APELLIDO2"
				style="position: absolute; top: 60px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_RENDCUENT_CONTA:APELLIDO2)" />
				:
			</div>
			<div id="data_CONTRATACION_RENDCUENT_CONTA:APELLIDO2"
				style="position: absolute; top: 60px; left: 130px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_RENDCUENT_CONTA:APELLIDO2)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="100">
				</ispac:htmlText>
			</div>
			<div id="label_CONTRATACION_RENDCUENT_CONTA:CARGO"
				style="position: absolute; top: 85px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_RENDCUENT_CONTA:CARGO)" />
				:
			</div>
			<div id="data_CONTRATACION_RENDCUENT_CONTA:CARGO"
				style="position: absolute; top: 85px; left: 130px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_RENDCUENT_CONTA:CARGO)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="500">
				</ispac:htmlText>
			</div>
			<div id="label_CONTRATACION_RENDCUENT_CONTA:DIRECCION"
				style="position: absolute; top: 110px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_RENDCUENT_CONTA:DIRECCION)" />
				:
			</div>
			<div id="data_CONTRATACION_RENDCUENT_CONTA:DIRECCION"
				style="position: absolute; top: 110px; left: 130px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_RENDCUENT_CONTA:DIRECCION)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="255">
				</ispac:htmlText>
			</div>
			<div id="label_CONTRATACION_RENDCUENT_CONTA:PROVINCIA"
				style="position: absolute; top: 135px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_RENDCUENT_CONTA:PROVINCIA)" />
				:
			</div>
			<div id="data_CONTRATACION_RENDCUENT_CONTA:PROVINCIA"
				style="position: absolute; top: 135px; left: 130px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_RENDCUENT_CONTA:PROVINCIA)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="255">
				</ispac:htmlText>
			</div>
			<div id="label_CONTRATACION_RENDCUENT_CONTA:MUNICIPIO"
				style="position: absolute; top: 160px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_RENDCUENT_CONTA:MUNICIPIO)" />
				:
			</div>
			<div id="data_CONTRATACION_RENDCUENT_CONTA:MUNICIPIO"
				style="position: absolute; top: 160px; left: 130px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_RENDCUENT_CONTA:MUNICIPIO)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="255">
				</ispac:htmlText>
			</div>
			<div id="label_CONTRATACION_RENDCUENT_CONTA:CODIGOPOSTAL"
				style="position: absolute; top: 185px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_RENDCUENT_CONTA:CODIGOPOSTAL)" />
				:
			</div>
			<div id="data_CONTRATACION_RENDCUENT_CONTA:CODIGOPOSTAL"
				style="position: absolute; top: 185px; left: 130px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_RENDCUENT_CONTA:CODIGOPOSTAL)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="10">
				</ispac:htmlText>
			</div>
			<div id="label_CONTRATACION_RENDCUENT_CONTA:TELEFONO"
				style="position: absolute; top: 210px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_RENDCUENT_CONTA:TELEFONO)" />
				:
			</div>
			<div id="data_CONTRATACION_RENDCUENT_CONTA:TELEFONO"
				style="position: absolute; top: 210px; left: 130px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_RENDCUENT_CONTA:TELEFONO)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="100">
				</ispac:htmlText>
			</div>
			<div id="label_CONTRATACION_RENDCUENT_CONTA:EMAIL"
				style="position: absolute; top: 235px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_RENDCUENT_CONTA:EMAIL)" />
				:
			</div>
			<div id="data_CONTRATACION_RENDCUENT_CONTA:EMAIL"
				style="position: absolute; top: 235px; left: 130px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_RENDCUENT_CONTA:EMAIL)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="100">
				</ispac:htmlText>
			</div>
		</div>
	</ispac:tab>
</ispac:tabs>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c1"%>

<script language='JavaScript' type='text/javascript'><!--

	function save() {
		var presupuesto = document.forms[0].elements['property(CONTRATACION_GARANTIAS:IMPORTEGARANTIA)'].value;
		presupuesto = presupuesto.replace(",",".");
		document.forms[0].elements['property(CONTRATACION_GARANTIAS:IMPORTEGARANTIA)'].value=presupuesto;

		document.defaultForm.target = "ParentWindow";
		document.defaultForm.action = "storeEntity.do";
		document.defaultForm.submit();
		ispac_needToConfirm = true;
	}

//--></script>

<c1:set var="aux0">
	<bean:write name="defaultForm"
		property="entityApp.label(CONTRATACION_GARANTIAS:CONTRATACION_GARANTIAS)" />
</c1:set><jsp:useBean id="aux0" type="java.lang.String" />
<ispac:tabs>
	<ispac:tab title='<%=aux0%>'>
		<div id="dataBlock_CONTRATACION_GARANTIAS"
			style="position: relative; height: 160px; width: 600px">
			<div id="label_CONTRATACION_GARANTIAS:TIPO_GARANTIA"
				style="position: absolute; top: 10px; left: 10px; width: 150px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_GARANTIAS:TIPO_GARANTIA)" />
				:
			</div>
			<div id="data_CONTRATACION_GARANTIAS:TIPO_GARANTIA"
				style="position: absolute; top: 10px; left: 170px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(CONTRATACION_GARANTIAS:TIPO_GARANTIA)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="1" cols="80" 
					id="SEARCH_CONTRATACION_GARANTIAS:TIPO_GARANTIA" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_TIP_GARANTIA" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CONTRATACION_GARANTIAS:TIPO_GARANTIA" id="property(CONTRATACION_GARANTIAS:TIPO_GARANTIA)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			<div id="label_CONTRATACION_GARANTIAS:AMOUNTRATE"
				style="position: absolute; top: 35px; left: 10px; width: 150px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_GARANTIAS:AMOUNTRATE)" />
				:
			</div>
			<div id="data_CONTRATACION_GARANTIAS:AMOUNTRATE"
				style="position: absolute; top: 35px; left: 170px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_GARANTIAS:AMOUNTRATE)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="100">
				</ispac:htmlText>
			</div>
			<div id="label_CONTRATACION_GARANTIAS:CONSTITUTIONPERIOD"
				style="position: absolute; top: 60px; left: 10px; width: 150px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_GARANTIAS:CONSTITUTIONPERIOD)" />
				:
			</div>
			<div id="data_CONTRATACION_GARANTIAS:CONSTITUTIONPERIOD"
				style="position: absolute; top: 60px; left: 170px; width: 100%;">
				<nobr>
					<ispac:htmlTextCalendar
						property="property(CONTRATACION_GARANTIAS:CONSTITUTIONPERIOD)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%=buttoncalendar%>' format="dd/mm/yyyy"
						enablePast="true">
					</ispac:htmlTextCalendar>
				</nobr>
			</div>
			<div id="label_CONTRATACION_GARANTIAS:IMPORTEGARANTIA"
				style="position: absolute; top: 85px; left: 10px; width: 150px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_GARANTIAS:IMPORTEGARANTIA)" />
				:
			</div>
			<div id="data_CONTRATACION_GARANTIAS:IMPORTEGARANTIA"
				style="position: absolute; top: 85px; left: 170px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_GARANTIAS:IMPORTEGARANTIA)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="100">
				</ispac:htmlText>Euros
			</div>
			<div id="label_CONTRATACION_GARANTIAS:PERIODOGARANTIA"
				style="position: absolute; top: 110px; left: 10px; width: 150px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_GARANTIAS:PERIODOGARANTIA)" />
				:
			</div>
			<div id="data_CONTRATACION_GARANTIAS:PERIODOGARANTIA"
				style="position: absolute; top: 110px; left: 170px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_GARANTIAS:PERIODOGARANTIA)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="100">
				</ispac:htmlText>
			</div>
			<div id="data_CONTRATACION_GARANTIAS:TIPO_GARANTIA"
				style="position: absolute; top: 107px; left: 600px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(CONTRATACION_GARANTIAS:PERIODUNITCODE)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="1" cols="15" 
					id="SEARCH_CONTRATACION_GARANTIAS:PERIODUNITCODE" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_PERIODUNITCODE" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CONTRATACION_GARANTIAS:PERIODUNITCODE" id="property(CONTRATACION_GARANTIAS:PERIODUNITCODE)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			<div id="label_CONTRATACION_GARANTIAS:DESCRIPCION"
				style="position: absolute; top: 135px; left: 10px; width: 150px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_GARANTIAS:DESCRIPCION)" />
				:
			</div>
			<div id="data_CONTRATACION_GARANTIAS:DESCRIPCION"
				style="position: absolute; top: 135px; left: 170px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_GARANTIAS:DESCRIPCION)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="200">
				</ispac:htmlText>
			</div>
		</div>
	</ispac:tab>
</ispac:tabs>
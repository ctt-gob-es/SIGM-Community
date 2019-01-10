<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c1"%>


<script language='JavaScript' type='text/javascript'>
	function save() {
		if(document.forms[0].elements['property(CONTRATACION_PETICION:IVA)'].value!="MAN."){
			var presupuesto = document.forms[0].elements['property(CONTRATACION_PETICION:PRESUPUESTO)'].value;
			presupuesto = presupuesto.replace(",", ".");
			var iva = document.forms[0].elements['property(CONTRATACION_PETICION:IVA)'].value;
			var presupFloat = parseFloat(presupuesto);
			var ivaFloat = parseFloat(iva)
			var ivaTotal = presupFloat * (ivaFloat / 100);
			var total = parseFloat(presupuesto) + parseFloat(ivaTotal);
			total = redondear2decimales(total);
			document.forms[0].elements['property(CONTRATACION_PETICION:TOTAL)'].value = total;
			ivaTotal = redondear2decimales(ivaTotal);
			document.forms[0].elements['property(CONTRATACION_PETICION:IMPORTEIVA)'].value = ivaTotal;
			document.forms[0].elements['property(CONTRATACION_PETICION:PRESUPUESTO)'].value = presupuesto;
			
		}

		document.defaultForm.target = "ParentWindow";
		document.defaultForm.action = "storeEntity.do";
		document.defaultForm.submit();
		var is_chrome= navigator.userAgent.toLowerCase().indexOf('chrome/') > -1;
		 if (is_chrome){
		  ispac_needToConfirm = false;
		 }
		 else{
		  ispac_needToConfirm = true;
		 }
		
	}

	function redondear2decimales(numero) {
		var original = parseFloat(numero);
		var result = Math.round(original * 100) / 100;
		return result;
	}
</script>

<c1:set var="aux0">
	<bean:write name="defaultForm"
		property="entityApp.label(CONTRATACION_PETICION:CONTRATACION_PETICION)" />
</c1:set><jsp:useBean id="aux0" type="java.lang.String" />
<ispac:tabs>
	<ispac:tab title='<%=aux0%>'>
		<div id="dataBlock_CONTRATACION_PETICION"
			style="position: relative; height: 260px; width: 600px">

			<div id="label_CONTRATACION_PETICION:SERVICIO_RESPONSABLE"
				style="position: absolute; top: 10px; left: 10px; width: 180px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_PETICION:SERVICIO_RESPONSABLE)" />
				:
			</div>
			<div id="data_CONTRATACION_PETICION:SERVICIO_RESPONSABLE"
				style="position: absolute; top: 10px; left: 200px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_PETICION:SERVICIO_RESPONSABLE)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="200">
				</ispac:htmlText>
			</div>

			<div id="label_CONTRATACION_PETICION:MOTIVO_PETICION"
				style="position: absolute; top: 35px; left: 10px; width: 180px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_PETICION:MOTIVO_PETICION)" />
				:
			</div>
			<div id="data_CONTRATACION_PETICION:MOTIVO_PETICION"
				style="position: absolute; top: 35px; left: 200px; width: 100%;">
				<ispac:htmlTextarea
					property="property(CONTRATACION_PETICION:MOTIVO_PETICION)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" rows="5" cols="80">
				</ispac:htmlTextarea>
			</div>

			<div id="label_CONTRATACION_PETICION:PRESUPUESTO"
				style="position: absolute; top: 105px; left: 10px; width: 200px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_PETICION:PRESUPUESTO)" />
			</div>
			<div id="data_CONTRATACION_PETICION:PRESUPUESTO"
				style="position: absolute; top: 105px; left: 200px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_PETICION:PRESUPUESTO)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="40" maxlength="20">
				</ispac:htmlText>
				Euros
			</div>

			<div id="label_CONTRATACION_PETICION:IVA"
				style="position: absolute; top: 130px; left: 10px; width: 180px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_PETICION:IVA)" />
				:
			</div>
			<div id="data_CONTRATACION_PETICION:IVA"
				style="position: absolute; top: 130px; left: 200px; width: 100%;">
				<nobr>
					<ispac:htmlTextImageFrame
						property="property(CONTRATACION_PETICION:IVA)" readonly="true"
						readonlyTag="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="40"
						id="SEARCH_CONTRATACION_PETICION_IVA" target="workframe"
						action="selectValue.do?entity=CONT_VLDTBL_PORCENTAJESIVA"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection" 
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
						<ispac:parameter name="SEARCH_CONTRATACION_PETICION_IVA"
							id="property(CONTRATACION_PETICION:IVA)" property="VALOR" />
					</ispac:htmlTextImageFrame>
				</nobr>
			</div>

			<div id="data_CONTRATACION_PETICION:IMPORTEIVA"
				style="position: absolute; top: 130px; left: 500px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_PETICION:IMPORTEIVA)"
					readonly="false" styleClass="input" size="40" maxlength="50">
				</ispac:htmlText>
				Euros
			</div>

			<div id="label_CONTRATACION_PETICION:TOTAL"
				style="position: absolute; top: 155px; left: 10px; width: 180px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_PETICION:TOTAL)" />
				:
			</div>
			<div id="data_CONTRATACION_PETICION:TOTAL"
				style="position: absolute; top: 155px; left: 200px; width: 100%;">
				<ispac:htmlText property="property(CONTRATACION_PETICION:TOTAL)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="40" maxlength="50">
				</ispac:htmlText>
				Euros
			</div>

			<div id="label_CONTRATACION_PETICION:RESP_CONTRATO"
				style="position: absolute; top: 180px; left: 10px; width: 180px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_PETICION:RESP_CONTRATO)" />
				:
			</div>
			<div id="data_CONTRATACION_PETICION:RESP_CONTRATO"
				style="position: absolute; top: 180px; left: 200px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_PETICION:RESP_CONTRATO)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="50">
				</ispac:htmlText>
			</div>

			<div id="label_CONTRATACION_PETICION:DIRECTOR_OBRA"
				style="position: absolute; top: 205px; left: 10px; width: 180px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_PETICION:DIRECTOR_OBRA)" />
				:
			</div>
			<div id="data_CONTRATACION_PETICION:DIRECTOR_OBRA"
				style="position: absolute; top: 205px; left: 200px; width: 100%;">
				<ispac:htmlTextarea
					property="property(CONTRATACION_PETICION:DIRECTOR_OBRA)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" rows="5" cols="80">
				</ispac:htmlTextarea>
			</div>
		</div>
	</ispac:tab>
</ispac:tabs>
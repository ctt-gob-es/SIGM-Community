<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c1"%>
<c1:set var="aux0">
	<bean:write name="defaultForm"
		property="entityApp.label(CONTRATACION_PETICION:CONTRATACION_PETICION)" />
</c1:set><jsp:useBean id="aux0" type="java.lang.String" />
<ispac:tabs>
	<ispac:tab title='<%=aux0%>'>
		<div id="dataBlock_CONTRATACION_PETICION" style="position: relative; height: 285px; width: 600px">
		
<style type="text/css">
.labelPosition_CONTRATACION_PETICION {
	position: relative;
	left: 10px;
	top: 15px;
	width: 190px;
}

.inputPosition_CONTRATACION_PETICION {
	position: relative;
	left: 180px;
}
</style>

<script language='JavaScript' type='text/javascript'>
	function save() {
		if(document.forms[0].elements['property(CONTRATACION_PETICION:IVA)'].value!="MAN."){
			var presupuesto = document.forms[0].elements['property(CONTRATACION_PETICION:PRESUPUESTO)'].value;
			presupuesto = presupuesto.replace(",", ".");
			var iva = document.forms[0].elements['property(CONTRATACION_PETICION:IVA)'].value;
			var presupFloat = 0.0;
			if(!isNaN(presupuesto)){
				if(presupuesto>0){
					presupFloat=parseFloat(presupuesto);
				}
			}
			else{
				alert("El valor del Presupuesto Estimado no es númerico. "+presupuesto);
			}
			
			
			var ivaFloat = 0;
			if(iva>0){
				ivaFloat=parseFloat(iva)
			}
			var ivaTotal = 0;
			if(presupFloat>0 && ivaFloat>0){
				ivaTotal = presupFloat * (ivaFloat / 100);
			}
			var total = parseFloat(presupuesto) + parseFloat(ivaTotal);
			total = redondear2decimales(total);
			if(total>0){
				document.forms[0].elements['property(CONTRATACION_PETICION:TOTAL)'].value = total;
			}
			
			if(ivaTotal>0){
				ivaTotal = redondear2decimales(ivaTotal);
				document.forms[0].elements['property(CONTRATACION_PETICION:IMPORTEIVA)'].value = ivaTotal;
			}
			if(presupuesto>0){
				document.forms[0].elements['property(CONTRATACION_PETICION:PRESUPUESTO)'].value = presupuesto;
			}
		}
		else{
			var presupuesto = document.forms[0].elements['property(CONTRATACION_PETICION:PRESUPUESTO)'].value;
			presupuesto = presupuesto.replace(",", ".");
			var iva = document.forms[0].elements['property(CONTRATACION_PETICION:IMPORTEIVA)'].value;
			iva = iva.replace(",", ".");
			var presupFloat = 0.0;
			var ivaFloat = 0.0
			if(!isNaN(presupuesto)){
				if(!isNaN(iva)){
					if(presupuesto>0 && iva>0){
						presupFloat=parseFloat(presupuesto);
						ivaFloat=parseFloat(iva);
						var total = presupFloat + ivaFloat;
						total = redondear2decimales(total);
						if(total>0){
							document.forms[0].elements['property(CONTRATACION_PETICION:TOTAL)'].value = total;
						}
					}
				}									
			}
			else{
				alert("El valor del Presupuesto Estimado no es númerico. "+presupuesto);
			}
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
			<div id="label_CONTRATACION_PETICION:SERVICIO_RESPONSABLE"
				class="formsTitleB labelPosition_CONTRATACION_PETICION">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_PETICION:SERVICIO_RESPONSABLE)" />
				:
			</div>
			<div id="data_CONTRATACION_PETICION:SERVICIO_RESPONSABLE"
				class="inputPosition_CONTRATACION_PETICION">
				<ispac:htmlText
					property="property(CONTRATACION_PETICION:SERVICIO_RESPONSABLE)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="255">
				</ispac:htmlText>
			</div>
			<div id="label_CONTRATACION_PETICION:MOTIVO_PETICION"
				class="formsTitleB labelPosition_CONTRATACION_PETICION">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_PETICION:MOTIVO_PETICION)" />
				:
			</div>
			<div id="data_CONTRATACION_PETICION:MOTIVO_PETICION"
				class="inputPosition_CONTRATACION_PETICION">
				<ispac:htmlTextarea
					property="property(CONTRATACION_PETICION:MOTIVO_PETICION)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" rows="2" cols="80">
				</ispac:htmlTextarea>
			</div>
			<div id="label_CONTRATACION_PETICION:TIPO_CONTRATO"
				class="formsTitleB labelPosition_CONTRATACION_PETICION">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_PETICION:TIPO_CONTRATO)" />
				:
			</div>
			<div id="data_CONTRATACION_PETICION:TIPO_CONTRATO"
				class="inputPosition_CONTRATACION_PETICION">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(CONTRATACION_PETICION:TIPO_CONTRATO)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="1" cols="80" 
					id="SEARCH_CODICE_PLIEGOS_TIPO_CONTRATO" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_TIPO_CONTRATO" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CODICE_PLIEGOS_TIPO_CONTRATO" id="property(CONTRATACION_PETICION:TIPO_CONTRATO)" property="SUSTITUTO" />
						<ispac:parameter name="SEARCH_CODICE_PLIEGOS_TIPO_CONTRATO" id="property(CONTRATACION_PETICION:CONTRATO_SUMIN)" property="CONTRATO_SUMIN" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			<div id="label_CONTRATACION_PETICION:PRESUPUESTO"
				class="formsTitleB labelPosition_CONTRATACION_PETICION">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_PETICION:PRESUPUESTO)" />
				:
			</div>
			<div id="data_CONTRATACION_PETICION:PRESUPUESTO"
				class="inputPosition_CONTRATACION_PETICION">
				<ispac:htmlText
					property="property(CONTRATACION_PETICION:PRESUPUESTO)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="50">
				</ispac:htmlText>
			</div>
			<div id="label_CONTRATACION_PETICION:IVA"
				class="formsTitleB labelPosition_CONTRATACION_PETICION">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_PETICION:IVA)" />
				:
			</div>
			<div id="data_CONTRATACION_PETICION:IVA"
				class="inputPosition_CONTRATACION_PETICION">
				<nobr>
					<ispac:htmlTextImageFrame
						property="property(CONTRATACION_PETICION:IVA)" readonly="true"
						readonlyTag="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="20"
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
				class="inputPosition_CONTRATACION_PETICION">
				<ispac:htmlText
					property="property(CONTRATACION_PETICION:IMPORTEIVA)"
					readonly="false" styleClass="input" size="20" maxlength="50">
				</ispac:htmlText>
				Euros
			</div>
			
			<div id="label_CONTRATACION_PETICION:TOTAL"
				class="formsTitleB labelPosition_CONTRATACION_PETICION">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_PETICION:TOTAL)" />
				:
			</div>
			<div id="data_CONTRATACION_PETICION:TOTAL"
				class="inputPosition_CONTRATACION_PETICION">
				<ispac:htmlText property="property(CONTRATACION_PETICION:TOTAL)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="50">
				</ispac:htmlText>
			</div>
			<div id="label_CONTRATACION_PETICION:RESP_CONTRATO"
				class="formsTitleB labelPosition_CONTRATACION_PETICION">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_PETICION:RESP_CONTRATO)" />
				:
			</div>
			<div id="data_CONTRATACION_PETICION:RESP_CONTRATO"
				class="inputPosition_CONTRATACION_PETICION">
				<ispac:htmlTextarea
					property="property(CONTRATACION_PETICION:RESP_CONTRATO)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" rows="5" cols="80">
				</ispac:htmlTextarea>
			</div>
		</div>
	</ispac:tab>
</ispac:tabs>
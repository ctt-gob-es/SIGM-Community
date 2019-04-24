<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c1"%>
<c1:set var="aux0">
	<bean:write name="defaultForm"
		property="entityApp.label(CONTRATACION_LOTES:CONTRATACION_LOTES)" />
</c1:set><jsp:useBean id="aux0" type="java.lang.String" />
<ispac:tabs>
	<ispac:tab title='<%=aux0%>'>
		<div id="dataBlock_CONTRATACION_LOTES"
			style="position: relative; height: 350px; width: 600px">
			<style type="text/css">
.labelPosition_CONTRATACION_LOTES {
	position: relative;
	left: 10px;
	top: 15px;
}

.inputPosition_CONTRATACION_LOTES {
	position: relative;
	left: 180px;
}
</style>
			<div id="label_CONTRATACION_LOTES:NUM_LOTE"
				class="formsTitleB labelPosition_CONTRATACION_LOTES">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_LOTES:NUM_LOTE)" />
				:
			</div>
			<div id="data_CONTRATACION_LOTES:NUM_LOTE"
				class="inputPosition_CONTRATACION_LOTES">
				<ispac:htmlText property="property(CONTRATACION_LOTES:NUM_LOTE)"
					readonly="true" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="50">
				</ispac:htmlText>
			</div>
			<div id="label_CONTRATACION_LOTES:DESCRIPCION"
				class="formsTitleB labelPosition_CONTRATACION_LOTES">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_LOTES:DESCRIPCION)" />
				:
			</div>
			<div id="data_CONTRATACION_LOTES:DESCRIPCION"
				class="inputPosition_CONTRATACION_LOTES">
				<ispac:htmlTextarea
					property="property(CONTRATACION_LOTES:DESCRIPCION)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" rows="2" cols="80">
				</ispac:htmlTextarea>
			</div>
			<div id="label_CONTRATACION_LOTES:IMPORTE_SINIVA"
				class="formsTitleB labelPosition_CONTRATACION_LOTES">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_LOTES:IMPORTE_SINIVA)" />
				:
			</div>
			<div id="data_CONTRATACION_LOTES:IMPORTE_SINIVA"
				class="inputPosition_CONTRATACION_LOTES">
				<ispac:htmlText
					property="property(CONTRATACION_LOTES:IMPORTE_SINIVA)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="50">
				</ispac:htmlText>
			</div>
			<div id="label_CONTRATACION_LOTES:IMPORTE_CONIVA"
				class="formsTitleB labelPosition_CONTRATACION_LOTES">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_LOTES:IMPORTE_CONIVA)" />
				:
			</div>
			<div id="data_CONTRATACION_LOTES:IMPORTE_CONIVA"
				class="inputPosition_CONTRATACION_LOTES">
				<ispac:htmlText
					property="property(CONTRATACION_LOTES:IMPORTE_CONIVA)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="100">
				</ispac:htmlText>
			</div>
			<div id="label_CONTRATACION_LOTES:IMPUESTOS"
				class="formsTitleB labelPosition_CONTRATACION_LOTES">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_LOTES:IMPUESTOS)" />
				:
			</div>
			<div id="data_CONTRATACION_LOTES:IMPUESTOS"
				class="inputPosition_CONTRATACION_LOTES">
				<ispac:htmlText property="property(CONTRATACION_LOTES:IMPUESTOS)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="100">
				</ispac:htmlText>
			</div>

			<div id="label_CONTRATACION_LOTES:CPV"
				style="position: absolute; top: 180px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_LOTES:CPV)" />
				:
			</div>

			<div id="data_CONTRATACION_LOTES:COD_CPV1"
				style="position: absolute; top: 180px; left: 180px; width: 100%;">
				<html:hidden property="property(CONTRATACION_LOTES:COD_CPV1)"
					onchange="alert('onchange1')" />
				<nobr>
					<ispac:htmlTextareaImageFrame
						property="property(CONTRATACION_LOTES:CPV1)" readonly="true"
						readonlyTag="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" rows="3" cols="50"
						id="SEARCH_CONTRATACION_LOTES_CPV1" target="workframe"
						action="SelectListadoCodicePliegoCPVAction.do?atributo=COD_CPV&caracteres=2&cadenaInicio=0"
						image="img/search-mg.gif" titleKeyLink="select.rol"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480" tabindex="113"
						jsShowFrame="hideShowFrameCPV1" jsDelete="hideDeleteCPV1">
						<ispac:parameter name="SEARCH_CONTRATACION_LOTES_CPV1"
							id="property(CONTRATACION_LOTES:COD_CPV1)" property="VALOR" />
						<ispac:parameter name="SEARCH_CONTRATACION_LOTES_CPV1"
							id="property(CONTRATACION_LOTES:CPV1)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>

			<div id="data_CONTRATACION_LOTES:COD_CPV2"
				style="position: absolute; top: 220px; left: 180px; width: 100%;">
				<html:hidden property="property(CONTRATACION_LOTES:COD_CPV2)" />
				<nobr>
					<ispac:htmlTextareaImageFrame
						property="property(CONTRATACION_LOTES:CPV2)" readonly="true"
						readonlyTag="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" rows="3" cols="50"
						id="SEARCH_CONTRATACION_LOTES_CPV2" target="workframe"
						action="SelectListadoCodicePliegoCPVAction.do?atributo=COD_CPV&caracteres=2&cadenaInicio=1&cod_cpv='+document.forms[0].elements['property(CONTRATACION_LOTES:COD_CPV1)'].value+'"
						image="img/search-mg.gif" titleKeyLink="select.rol"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480" tabindex="113"
						jsShowFrame="hideShowFrameCPV2" jsDelete="hideDeleteCPV2">
						<ispac:parameter name="SEARCH_CONTRATACION_LOTES_CPV2"
							id="property(CONTRATACION_LOTES:COD_CPV2)" property="VALOR" />
						<ispac:parameter name="SEARCH_CONTRATACION_LOTES_CPV2"
							id="property(CONTRATACION_LOTES:CPV2)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			<div id="data_CONTRATACION_LOTES:COD_CPV3"
				style="position: absolute; top: 260px; left: 180px; width: 100%;">
				<html:hidden property="property(CONTRATACION_LOTES:COD_CPV3)" />
				<nobr>
					<ispac:htmlTextareaImageFrame
						property="property(CONTRATACION_LOTES:CPV3)" readonly="true"
						readonlyTag="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" rows="3" cols="50"
						id="SEARCH_CONTRATACION_LOTES_CPV3" target="workframe"
						action="SelectListadoCodicePliegoCPVAction.do?atributo=COD_CPV&caracteres=2&cadenaInicio=2&cod_cpv='+document.forms[0].elements['property(CONTRATACION_LOTES:COD_CPV2)'].value+'"
						image="img/search-mg.gif" titleKeyLink="select.rol"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480" tabindex="113"
						jsShowFrame="hideShowFrameCPV3" jsDelete="hideDeleteCPV3">
						<ispac:parameter name="SEARCH_CONTRATACION_LOTES_CPV3"
							id="property(CONTRATACION_LOTES:COD_CPV3)" property="VALOR" />
						<ispac:parameter name="SEARCH_CONTRATACION_LOTES_CPV3"
							id="property(CONTRATACION_LOTES:CPV3)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			<div id="data_CONTRATACION_LOTES:COD_CPV4"
				style="position: absolute; top: 300px; left: 180px; width: 100%;">
				<html:hidden property="property(CONTRATACION_LOTES:COD_CPV4)" />
				<nobr>
					<ispac:htmlTextareaImageFrame
						property="property(CONTRATACION_LOTES:CPV4)" readonly="true"
						readonlyTag="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" rows="3" cols="50"
						id="SEARCH_CONTRATACION_LOTES_CPV4" target="workframe"
						action="SelectListadoCodicePliegoCPVAction.do?atributo=COD_CPV&caracteres=2&cadenaInicio=3&cod_cpv='+document.forms[0].elements['property(CONTRATACION_LOTES:COD_CPV3)'].value+'"
						image="img/search-mg.gif" titleKeyLink="select.rol"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480" tabindex="113"
						jsShowFrame="hideShowFrameCPV4" jsDelete="hideDeleteCPV4">
						<ispac:parameter name="SEARCH_CONTRATACION_LOTES_CPV4"
							id="property(CONTRATACION_LOTES:COD_CPV4)" property="VALOR" />
						<ispac:parameter name="SEARCH_CONTRATACION_LOTES_CPV4"
							id="property(CONTRATACION_LOTES:CPV4)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>

			<div style="POSITION: absolute; WIDTH: 100%; TOP: 180px; LEFT: 570px"
				id=data_CONTRATACION_LOTES:CPV>
				<ispac:htmlTextMultivalue
					property="propertyMultivalue(CONTRATACION_LOTES:CPV)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" divWidth="700"
					maxlength="1024">
				</ispac:htmlTextMultivalue>
			</div>
		</div>
	</ispac:tab>
</ispac:tabs>

<script language='JavaScript' type='text/javascript'>
	function hideShowFrameCPV1(target, action, width, height, msgConfirm,
			doSubmit, needToConfirm, form) {
		document.forms[0].elements['property(CONTRATACION_LOTES:COD_CPV1)'].value = "";
		document.forms[0].elements['property(CONTRATACION_LOTES:CPV1)'].value = "";
		document.forms[0].elements['property(CONTRATACION_LOTES:COD_CPV2)'].value = "";
		document.forms[0].elements['property(CONTRATACION_LOTES:CPV2)'].value = "";
		document.forms[0].elements['property(CONTRATACION_LOTES:COD_CPV3)'].value = "";
		document.forms[0].elements['property(CONTRATACION_LOTES:CPV3)'].value = "";
		document.forms[0].elements['property(CONTRATACION_LOTES:COD_CPV4)'].value = "";
		document.forms[0].elements['property(CONTRATACION_LOTES:CPV4)'].value = "";
		showFrame(target, action, width, height, msgConfirm, doSubmit,
				needToConfirm, form);
	}
	function hideShowFrameCPV2(target, action, width, height, msgConfirm,
			doSubmit, needToConfirm, form) {
		if (document.forms[0].elements['property(CONTRATACION_LOTES:CPV1)'].value != "") {
			document.forms[0].elements['property(CONTRATACION_LOTES:COD_CPV2)'].value = "";
			document.forms[0].elements['property(CONTRATACION_LOTES:CPV2)'].value = "";
			document.forms[0].elements['property(CONTRATACION_LOTES:COD_CPV3)'].value = "";
			document.forms[0].elements['property(CONTRATACION_LOTES:CPV3)'].value = "";
			document.forms[0].elements['property(CONTRATACION_LOTES:COD_CPV4)'].value = "";
			document.forms[0].elements['property(CONTRATACION_LOTES:CPV4)'].value = "";
			showFrame(target, action, width, height, msgConfirm, doSubmit,
					needToConfirm, form);
		} else {
			alert("Quedan por rellenar campos");
		}
	}
	function hideShowFrameCPV3(target, action, width, height, msgConfirm,
			doSubmit, needToConfirm, form) {
		if (document.forms[0].elements['property(CONTRATACION_LOTES:CPV1)'].value != ""
				&& document.forms[0].elements['property(CONTRATACION_LOTES:CPV2)'].value != "") {
			document.forms[0].elements['property(CONTRATACION_LOTES:COD_CPV3)'].value = "";
			document.forms[0].elements['property(CONTRATACION_LOTES:CPV3)'].value = "";
			document.forms[0].elements['property(CONTRATACION_LOTES:COD_CPV4)'].value = "";
			document.forms[0].elements['property(CONTRATACION_LOTES:CPV4)'].value = "";
			showFrame(target, action, width, height, msgConfirm, doSubmit,
					needToConfirm, form);
		} else {
			alert("Quedan por rellenar campos");
		}
	}
	function hideShowFrameCPV4(target, action, width, height, msgConfirm,
			doSubmit, needToConfirm, form) {
		if (document.forms[0].elements['property(CONTRATACION_LOTES:CPV1)'].value != ""
				&& document.forms[0].elements['property(CONTRATACION_LOTES:CPV2)'].value != ""
				&& document.forms[0].elements['property(CONTRATACION_LOTES:CPV3)'].value != "") {
			document.forms[0].elements['property(CONTRATACION_LOTES:COD_CPV4)'].value = "";
			document.forms[0].elements['property(CONTRATACION_LOTES:CPV4)'].value = "";
			showFrame(target, action, width, height, msgConfirm, doSubmit,
					needToConfirm, form);
		} else {
			alert("Quedan por rellenar campos");
		}
	}
	function hideDeleteCPV1() {
		document.forms[0].elements['property(CONTRATACION_LOTES:COD_CPV1)'].value = "";
		document.forms[0].elements['property(CONTRATACION_LOTES:CPV1)'].value = "";
		document.forms[0].elements['property(CONTRATACION_LOTES:COD_CPV2)'].value = "";
		document.forms[0].elements['property(CONTRATACION_LOTES:CPV2)'].value = "";
		document.forms[0].elements['property(CONTRATACION_LOTES:COD_CPV3)'].value = "";
		document.forms[0].elements['property(CONTRATACION_LOTES:CPV3)'].value = "";
		document.forms[0].elements['property(CONTRATACION_LOTES:COD_CPV4)'].value = "";
		document.forms[0].elements['property(CONTRATACION_LOTES:CPV4)'].value = "";
	}
	function hideDeleteCPV2() {
		document.forms[0].elements['property(CONTRATACION_LOTES:COD_CPV2)'].value = "";
		document.forms[0].elements['property(CONTRATACION_LOTES:CPV2)'].value = "";
		document.forms[0].elements['property(CONTRATACION_LOTES:COD_CPV3)'].value = "";
		document.forms[0].elements['property(CONTRATACION_LOTES:CPV3)'].value = "";
		document.forms[0].elements['property(CONTRATACION_LOTES:COD_CPV4)'].value = "";
		document.forms[0].elements['property(CONTRATACION_LOTES:CPV4)'].value = "";
	}
	function hideDeleteCPV3() {
		document.forms[0].elements['property(CONTRATACION_LOTES:COD_CPV3)'].value = "";
		document.forms[0].elements['property(CONTRATACION_LOTES:CPV3)'].value = "";
		document.forms[0].elements['property(CONTRATACION_LOTES:COD_CPV4)'].value = "";
		document.forms[0].elements['property(CONTRATACION_LOTES:CPV4)'].value = "";
	}
	function hideDeleteCPV4() {
		document.forms[0].elements['property(CONTRATACION_LOTES:COD_CPV4)'].value = "";
		document.forms[0].elements['property(CONTRATACION_LOTES:CPV4)'].value = "";
	}

	function insertMultivalueElement(name, propertyName, size, maxlength,
			inputClass) {
		insertCommonMultivalueElement(name, propertyName, size, maxlength,
				inputClass, '');
	}
	function insertCommonMultivalueElement(name, propertyName, size, maxlength,
			inputClass, blockHTML) {
		var valor = "";
		var codigo = "";
		if (name == 'CONTRATACION_LOTES_CPV') {
			//valor del cpv  
			if (document.forms[0].elements['property(CONTRATACION_LOTES:CPV1)'].value != "") {
				if (document.forms[0].elements['property(CONTRATACION_LOTES:CPV2)'].value != "") {
					if (document.forms[0].elements['property(CONTRATACION_LOTES:CPV3)'].value != "") {
						if (document.forms[0].elements['property(CONTRATACION_LOTES:CPV4)'].value != "") {
							valor = document.forms[0].elements['property(CONTRATACION_LOTES:CPV4)'].value;
							codigo = document.forms[0].elements['property(CONTRATACION_LOTES:COD_CPV4)'].value;
						} else {
							valor = document.forms[0].elements['property(CONTRATACION_LOTES:CPV3)'].value;
							codigo = document.forms[0].elements['property(CONTRATACION_LOTES:COD_CPV3)'].value;
						}
					} else {
						valor = document.forms[0].elements['property(CONTRATACION_LOTES:CPV2)'].value;
						codigo = document.forms[0].elements['property(CONTRATACION_LOTES:COD_CPV2)'].value;
					}
				} else {
					valor = document.forms[0].elements['property(CONTRATACION_LOTES:CPV1)'].value;
					codigo = document.forms[0].elements['property(CONTRATACION_LOTES:COD_CPV1)'].value;
				}
			}
		}

		var newdiv = document.createElement("div");
		id = eval('max' + name);
		newdiv.setAttribute('id', 'div_' + name + '_' + id);
		styleId = propertyName.replace(')', '_' + id + ')');
		if (valor != "" && codigo != "") {
			newdiv.innerHTML = '<input type="checkbox" value="' + id + '" name="checkbox_'+name+'"/><input id="'+styleId+'" type="text" readonly class="'+inputClass+'" size="'+size+'" maxlength="'+maxlength+'" name="' + propertyName + '" value="'+codigo+" - "+valor+'"/>'
					+ blockHTML;
		} else {
			newdiv.innerHTML = '<input type="checkbox" value="' + id + '" name="checkbox_'+name+'"/><input id="'+styleId+'" type="text" readonly class="'+inputClass+'" size="'+size+'" maxlength="'+maxlength+'" name="' + propertyName + '"/>'
					+ blockHTML;
		}
		obj = document.getElementById("div_" + name);
		obj.appendChild(newdiv);
		eval('max' + name + '= id + 1');

		document.forms[0].elements['property(CONTRATACION_LOTES:CPV1)'].value = "";
		document.forms[0].elements['property(CONTRATACION_LOTES:CPV2)'].value = "";
		document.forms[0].elements['property(CONTRATACION_LOTES:CPV3)'].value = "";
		document.forms[0].elements['property(CONTRATACION_LOTES:CPV4)'].value = "";
		document.forms[0].elements['property(CONTRATACION_LOTES:COD_CPV1)'].value = "";
		document.forms[0].elements['property(CONTRATACION_LOTES:COD_CPV2)'].value = "";
		document.forms[0].elements['property(CONTRATACION_LOTES:COD_CPV3)'].value = "";
		document.forms[0].elements['property(CONTRATACION_LOTES:COD_CPV4)'].value = "";

	}
</script>
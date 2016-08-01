<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c1"%>

<script language='JavaScript' type='text/javascript'><!--

	function save() {
		if(document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:CLAS_EMP1)'].value!=""){
			alert("Códigos de Clasificación de Empresas quedan sin insertar");
		}
		else{
			document.defaultForm.target = "ParentWindow";
			document.defaultForm.action = "storeEntity.do";
			document.defaultForm.submit();
			ispac_needToConfirm = true;
		}
	}

	function  hideShowFrameCLAS_EMP1(target, action, width, height, msgConfirm, doSubmit, needToConfirm, form) {
		document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:COD_CLAS_EMP1)'].value = "";
		document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:CLAS_EMP1)'].value = "";
		document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:COD_CLAS_EMP2)'].value = "";
		document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:CLAS_EMP2)'].value = "";
		document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:COD_CLAS_EMP3)'].value = "";
		document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:CLAS_EMP3)'].value = "";
		showFrame(target, action, width, height, msgConfirm, doSubmit, needToConfirm, form) ;
	}
	function  hideShowFrameCLAS_EMP2(target, action, width, height, msgConfirm, doSubmit, needToConfirm, form) {
		if(document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:CLAS_EMP1)'].value != ""){
			document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:COD_CLAS_EMP2)'].value = "";
			document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:CLAS_EMP2)'].value = "";
			document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:COD_CLAS_EMP3)'].value = "";
			document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:CLAS_EMP3)'].value = "";
			showFrame(target, action, width, height, msgConfirm, doSubmit, needToConfirm, form) ;
		}
		else{
			alert("Quedan por rellenar campos");
		}
	}
	function  hideShowFrameCLAS_EMP3(target, action, width, height, msgConfirm, doSubmit, needToConfirm, form) {
		if(document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:CLAS_EMP1)'].value != "" && document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:CLAS_EMP2)'].value != ""){
			document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:COD_CLAS_EMP3)'].value = "";
			document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:CLAS_EMP3)'].value = "";
			showFrame(target, action, width, height, msgConfirm, doSubmit, needToConfirm, form) ;
		}
		else{
			alert("Quedan por rellenar campos");
		}
	}

	function  hideDeleteCLAS_EMP1() {
		document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:COD_CLAS_EMP1)'].value = "";
		document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:CLAS_EMP1)'].value = "";
		document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:COD_CLAS_EMP2)'].value = "";
		document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:CLAS_EMP2)'].value = "";
		document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:COD_CLAS_EMP3)'].value = "";
		document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:CLAS_EMP3)'].value = "";
	}
	function  hideDeleteCLAS_EMP2() {
		document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:COD_CLAS_EMP2)'].value = "";
		document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:CLAS_EMP2)'].value = "";
		document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:COD_CLAS_EMP3)'].value = "";
		document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:CLAS_EMP3)'].value = "";
	}
	function  hideDeleteCLAS_EMP3() {
		document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:COD_CLAS_EMP3)'].value = "";
		document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:CLAS_EMP3)'].value = "";
	}


	function insertMultivalueElement(name, propertyName, size, maxlength, inputClass){
		insertCommonMultivalueElement(name, propertyName, size, maxlength, inputClass, '');
	}
	function insertCommonMultivalueElement(name, propertyName, size, maxlength, inputClass, blockHTML){
		var valor = "";
		var codigo = "";
		if(name=='CONTRATACION_INF_EMPRESA_CPV'){
			//valor del cpv  
			   if(document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:CPV1)'].value!=""){
				   if(document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:CPV2)'].value!=""){
					   if(document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:CPV3)'].value!=""){
						   if(document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:CPV4)'].value!=""){
							   valor = document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:CPV4)'].value;
							   codigo = document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:COD_CPV4)'].value;
						   }
						   else{
							   valor = document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:CPV3)'].value;
							   codigo = document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:COD_CPV3)'].value;
						   }
					   }
					   else{
						   valor = document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:CPV2)'].value;
						   codigo = document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:COD_CPV2)'].value;
					   }
				   }
				   else{
					   valor = document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:CPV1)'].value;
					   codigo = document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:COD_CPV1)'].value;
				   }
			}
		}
		if(name=='CONTRATACION_INF_EMPRESA_CLAS_EMP'){
			if(document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:CLAS_EMP1)'].value!=""){
				   if(document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:CLAS_EMP2)'].value!=""){
					   if(document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:CLAS_EMP3)'].value!=""){
						   valor = document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:CLAS_EMP3)'].value;
						   codigo = document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:COD_CLAS_EMP3)'].value;
					   }
					   else{
						   valor = document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:CLAS_EMP2)'].value;
						   codigo = document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:COD_CLAS_EMP2)'].value;
					   }
				   }
				   else{
					   valor = document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:CLAS_EMP1)'].value;
					   codigo = document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:COD_CLAS_EMP1)'].value;
				   }
			}

		}
   

	   var newdiv = document.createElement("div");
	   id = eval('max'+name);
	   newdiv.setAttribute('id','div_'+name+'_'+id);
	   styleId = propertyName.replace(')', '_'+id+')');
	   if(valor != "" && codigo != ""){
		   newdiv.innerHTML = '<input type="checkbox" value="' + id + '" name="checkbox_'+name+'"/><input id="'+styleId+'" type="text" readonly class="'+inputClass+'" size="'+size+'" maxlength="'+maxlength+'" name="' + propertyName + '" value="'+codigo+" - "+valor+'"/>'
		   +blockHTML;
	   }
	   else{
		   newdiv.innerHTML = '<input type="checkbox" value="' + id + '" name="checkbox_'+name+'"/><input id="'+styleId+'" type="text" readonly class="'+inputClass+'" size="'+size+'" maxlength="'+maxlength+'" name="' + propertyName + '"/>'
		   +blockHTML;
	   }
	   obj = document.getElementById("div_"+name);
	   obj.appendChild(newdiv);
	   eval('max'+name +'= id + 1' );
	   
	   if(name=='CONTRATACION_INF_EMPRESA_CPV'){
		   document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:CPV1)'].value = "";
		   document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:CPV2)'].value = "";
		   document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:CPV3)'].value = "";
		   document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:CPV4)'].value = "";
		   document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:COD_CPV1)'].value = "";
		   document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:COD_CPV2)'].value = "";
		   document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:COD_CPV3)'].value = "";
		   document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:COD_CPV4)'].value = "";
	   }
	   else{
		   document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:CLAS_EMP1)'].value = "";
		   document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:CLAS_EMP2)'].value = "";
		   document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:CLAS_EMP3)'].value = "";
		   document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:COD_CLAS_EMP1)'].value = "";
		   document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:COD_CLAS_EMP2)'].value = "";
		   document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:COD_CLAS_EMP3)'].value = "";
	   }

		   
		   
	}
//--></script>


<c1:set var="aux0">
	<bean:write name="defaultForm"
		property="entityApp.label(CONTRATACION_INF_EMPRESA:CONTRATACION_INF_EMPRESA)" />
</c1:set><jsp:useBean id="aux0" type="java.lang.String" />
<ispac:tabs>
	<ispac:tab title='<%=aux0%>'>
		<div id="dataBlock_CONTRATACION_INF_EMPRESA" style="position: relative; height: 450px; width: 600px">
		
			<div id="label_SPAC_EXPEDIENTES:SEP_INTERESADO_PRINCIPAL" style="position: absolute; top: 10px; left: 10px; width: 620px" class="textbar">
				INFORMACIÓN GENERAL A CUMPLIR
				<hr class="formbar"/>
			</div>

			<div id="label_CONTRATACION_INF_EMPRESA:LEGALFORM"
				title="Descripción textual de la forma legal requerida a los operadores económicos que participan en este procedimiento."
				style="position: absolute; top: 50px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_INF_EMPRESA:LEGALFORM)" />
				:
			</div>
			<div id="data_CONTRATACION_INF_EMPRESA:LEGALFORM"
				style="position: absolute; top: 50px; left: 140px; width: 100%;">
				<ispac:htmlText property="property(CONTRATACION_INF_EMPRESA:LEGALFORM)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="256">
				</ispac:htmlText>
			</div>

			<div id="label_CONTRATACION_INF_EMPRESA:PERSONALSITUATION"
				title="Descripción textual de la situación personal requerida para el operador económico."
				style="position: absolute; top: 75px; left: 10px; width: 120px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_INF_EMPRESA:PERSONALSITUATION)" />
				:
			</div>
			<div id="data_CONTRATACION_INF_EMPRESA:PERSONALSITUATION"
				style="position: absolute; top: 75px; left: 140px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_INF_EMPRESA:PERSONALSITUATION)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="256">
				</ispac:htmlText>
			</div>


			<div id="label_CONTRATACION_INF_EMPRESA:OPERATINGYEARSQUANTITY"
				title="Años de experiencia requeridos al operador en el ejercicio de su actividad relacionada con el objeto del contrato."
				style="position: absolute; top: 100px; left: 10px; width: 130px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_INF_EMPRESA:OPERATINGYEARSQUANTITY)" />
				:
			</div>
			<div id="data_CONTRATACION_INF_EMPRESA:OPERATINGYEARSQUANTITY"
				style="position: absolute; top: 100px; left: 140px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_INF_EMPRESA:OPERATINGYEARSQUANTITY)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="20" maxlength="10">
				</ispac:htmlText>
			</div>
			<div id="label_CONTRATACION_INF_EMPRESA:EMPLOYEEQUANTITY"
				title="Número mínimo de empleados que se requiere que tenga el operador económico."
				style="position: absolute; top: 100px; left: 300px; width: 200px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_INF_EMPRESA:EMPLOYEEQUANTITY)" />
				:
			</div>
			<div id="data_CONTRATACION_INF_EMPRESA:EMPLOYEEQUANTITY"
				style="position: absolute; top: 100px; left: 500px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_INF_EMPRESA:EMPLOYEEQUANTITY)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="20" maxlength="10">
				</ispac:htmlText>
			</div>

			<div id="label_CONTRATACION_INF_EMPRESA:DESCRIPTION"
				title="Descripción textual de la información y trámites necesarios para evaluar si se cumplen los requisitos de capacidad."
				style="position: absolute; top: 125px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_INF_EMPRESA:DESCRIPTION)" />
				:
			</div>
			<div id="data_CONTRATACION_INF_EMPRESA:DESCRIPTION"
				style="position: absolute; top: 125px; left: 140px; width: 100%;">
				<ispac:htmlText property="property(CONTRATACION_INF_EMPRESA:DESCRIPTION)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="256">
				</ispac:htmlText>
			</div>

			  <div id="label_SPAC_EXPEDIENTES:SEP_INTERESADO_PRINCIPAL" style="position: absolute; top: 170px; left: 10px; width: 620px" class="textbar">
				REQUISITOS ESPECÍFICOS Y DECLARACIONES REQUERIDAS
				<hr class="formbar"/>
			</div>
			
			<div id="label_CONTRATACION_INF_EMPRESA:COD_TIP_DECLARACION"
				title="Valor codificado que describe el requisito específico para el licitador"
				style="position: absolute; top: 200px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_INF_EMPRESA:TIP_DECLARACION)" />:
			</div>
			<div id="data_CONTRATACION_INF_EMPRESA:COD_TIP_DECLARACION" style="position: absolute; top: 200px; left: 220px; width: 100%;">
				<nobr>
					<ispac:htmlTextMultivalueImageFrame
						property="propertyMultivalue(CONTRATACION_INF_EMPRESA:TIP_DECLARACION)"
						propertyDestination="propertyMultivalue(CONTRATACION_INF_EMPRESA:TIP_DECLARACION)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" size="77"
						id="SEARCH_CONTRATACION_INF_EMPRESA_TIP_DECLARACION" target="workframe"
						action="selectListadoCodicePliego.do?atributo=COD_TIP_DECLARACION"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" divWidth="500" divHeight="50">
						<ispac:parameterMultivalue name="SEARCH_CONTRATACION_INF_EMPRESA_TIP_DECLARACION" id="propertyMultivalue(CONTRATACION_INF_EMPRESA:TIP_DECLARACION)" 
						setMethod="id" property="SUSTITUTO" propertyDestination="propertyMultivalue(CONTRATACION_INF_EMPRESA:TIP_DECLARACION)" />
					</ispac:htmlTextMultivalueImageFrame> 
				</nobr>
			</div>
			<div id="label_SPAC_EXPEDIENTES:SEP_INTERESADO_PRINCIPAL" style="position: absolute; top: 275px; left: 10px; width: 620px" class="textbar">
				CLASIFICACIÓN EMPRESARIAL
				<hr class="formbar"/>
			</div>
			<div id="label_CONTRATACION_INF_EMPRESA:COD_CLAS_EMP1"
				style="position: absolute; top: 315px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_INF_EMPRESA:CLAS_EMP1)" />(*):
			</div>
			<div id="data_CONTRATACION_INF_EMPRESA:COD_CLAS_EMP1" style="position: absolute; top: 305px; left: 130px; width: 100%;">
				<html:hidden property="property(CONTRATACION_INF_EMPRESA:COD_CLAS_EMP1)"/>
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(CONTRATACION_INF_EMPRESA:CLAS_EMP1)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="3" cols="50" 
					id="SEARCH_CONTRATACION_INF_EMPRESA_CLAS_EMP1" target="workframe" 
					action="SelectListadoCodiceClasificacionEmpresaAction.do?atributo=COD_CLAS_EMP&caracteres=1&cadenaInicio=0" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113" jsShowFrame="hideShowFrameCLAS_EMP1"  jsDelete="hideDeleteCLAS_EMP1">
						<ispac:parameter name="SEARCH_CONTRATACION_INF_EMPRESA_CLAS_EMP1" id="property(CONTRATACION_INF_EMPRESA:COD_CLAS_EMP1)" property="VALOR" />
						<ispac:parameter name="SEARCH_CONTRATACION_INF_EMPRESA_CLAS_EMP1" id="property(CONTRATACION_INF_EMPRESA:CLAS_EMP1)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			<div id="data_CONTRATACION_INF_EMPRESA:COD_CLAS_EMP2" style="position: absolute; top: 350px; left: 130px; width: 100%;">
				<html:hidden property="property(CONTRATACION_INF_EMPRESA:COD_CLAS_EMP2)"/>
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(CONTRATACION_INF_EMPRESA:CLAS_EMP2)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="3" cols="50" 
					id="SEARCH_CONTRATACION_INF_EMPRESA_CLAS_EMP2" target="workframe" 
					action="SelectListadoCodiceClasificacionEmpresaAction.do?atributo=COD_CLAS_EMP&caracteres=1&cadenaInicio=1&cod_cpv='+document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:COD_CLAS_EMP1)'].value+'" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113" jsShowFrame="hideShowFrameCLAS_EMP2"  jsDelete="hideDeleteCLAS_EMP2">
						<ispac:parameter name="SEARCH_CONTRATACION_INF_EMPRESA_CLAS_EMP2" id="property(CONTRATACION_INF_EMPRESA:COD_CLAS_EMP2)" property="VALOR" />
						<ispac:parameter name="SEARCH_CONTRATACION_INF_EMPRESA_CLAS_EMP2" id="property(CONTRATACION_INF_EMPRESA:CLAS_EMP2)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			<div id="data_CONTRATACION_INF_EMPRESA:COD_CLAS_EMP3" style="position: absolute; top: 395px; left: 130px; width: 100%;">
				<html:hidden property="property(CONTRATACION_INF_EMPRESA:COD_CLAS_EMP3)"/>
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(CONTRATACION_INF_EMPRESA:CLAS_EMP3)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="3" cols="50"
					id="SEARCH_CONTRATACION_INF_EMPRESA_CLAS_EMP3" target="workframe" 
					action="SelectListadoCodiceClasificacionEmpresaAction.do?atributo=COD_CLAS_EMP&caracteres=1&cadenaInicio=2&cod_cpv='+document.forms[0].elements['property(CONTRATACION_INF_EMPRESA:COD_CLAS_EMP2)'].value+'" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113" jsShowFrame="hideShowFrameCLAS_EMP3"  jsDelete="hideDeleteCLAS_EMP3">
						<ispac:parameter name="SEARCH_CONTRATACION_INF_EMPRESA_CLAS_EMP3" id="property(CONTRATACION_INF_EMPRESA:COD_CLAS_EMP3)" property="VALOR" />
						<ispac:parameter name="SEARCH_CONTRATACION_INF_EMPRESA_CLAS_EMP3" id="property(CONTRATACION_INF_EMPRESA:CLAS_EMP3)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			
			
			<div style="POSITION: absolute; WIDTH: 100%; TOP: 305px; LEFT:460px" id=data_CONTRATACION_INF_EMPRESA:CLAS_EMP>
				<ispac:htmlTextMultivalue
					property="propertyMultivalue(CONTRATACION_INF_EMPRESA:CLAS_EMP)" readonly="false"
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly"  size="50" divWidth="334" 
					maxlength="1024">
				</ispac:htmlTextMultivalue>
			</div>
			
		</div>
	</ispac:tab>
</ispac:tabs>






















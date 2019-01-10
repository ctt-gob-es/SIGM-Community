<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c1"%>

<script language='JavaScript' type='text/javascript'><!--

	function save() {
		if(document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CPV1)'].value!="" || document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CLAS_EMP1)'].value!=""){
			alert("Los campos Códigos CPV o Códigos de Clasificación de Empresas quedan sin insertar");
		}
		else{
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
	}


	function  hideShowFrameCPV1(target, action, width, height, msgConfirm, doSubmit, needToConfirm, form) {
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CPV1)'].value = "";
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CPV1)'].value = "";
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CPV2)'].value = "";
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CPV2)'].value = "";
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CPV3)'].value = "";
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CPV3)'].value = "";
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CPV4)'].value = "";
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CPV4)'].value = "";
		showFrame(target, action, width, height, msgConfirm, doSubmit, needToConfirm, form) ;
	}
	function  hideShowFrameCPV2(target, action, width, height, msgConfirm, doSubmit, needToConfirm, form) {
		if(document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CPV1)'].value != ""){
			document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CPV2)'].value = "";
			document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CPV2)'].value = "";
			document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CPV3)'].value = "";
			document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CPV3)'].value = "";
			document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CPV4)'].value = "";
			document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CPV4)'].value = "";
			showFrame(target, action, width, height, msgConfirm, doSubmit, needToConfirm, form) ;
		}
		else{
			alert("Quedan por rellenar campos");
		}
	}
	function  hideShowFrameCPV3(target, action, width, height, msgConfirm, doSubmit, needToConfirm, form) {
		if(document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CPV1)'].value != "" && document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CPV2)'].value != ""){
			document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CPV3)'].value = "";
			document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CPV3)'].value = "";
			document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CPV4)'].value = "";
			document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CPV4)'].value = "";
			showFrame(target, action, width, height, msgConfirm, doSubmit, needToConfirm, form) ;
		}
		else{
			alert("Quedan por rellenar campos");
		}
	}
	function  hideShowFrameCPV4(target, action, width, height, msgConfirm, doSubmit, needToConfirm, form) {
		if(document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CPV1)'].value != "" && document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CPV2)'].value != "" && document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CPV3)'].value != ""){
			document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CPV4)'].value = "";
			document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CPV4)'].value = "";
			showFrame(target, action, width, height, msgConfirm, doSubmit, needToConfirm, form) ;
		}
		else{
			alert("Quedan por rellenar campos");
		}
	}
	function  hideDeleteCPV1() {
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CPV1)'].value = "";
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CPV1)'].value = "";
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CPV2)'].value = "";
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CPV2)'].value = "";
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CPV3)'].value = "";
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CPV3)'].value = "";
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CPV4)'].value = "";
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CPV4)'].value = "";
	}
	function  hideDeleteCPV2() {
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CPV2)'].value = "";
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CPV2)'].value = "";
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CPV3)'].value = "";
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CPV3)'].value = "";
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CPV4)'].value = "";
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CPV4)'].value = "";
	}
	function  hideDeleteCPV3() {
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CPV3)'].value = "";
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CPV3)'].value = "";
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CPV4)'].value = "";
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CPV4)'].value = "";
	}
	function  hideDeleteCPV4() {
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CPV4)'].value = "";
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CPV4)'].value = "";
	}

	function  hideShowFrameCLAS_EMP1(target, action, width, height, msgConfirm, doSubmit, needToConfirm, form) {
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CLAS_EMP1)'].value = "";
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CLAS_EMP1)'].value = "";
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CLAS_EMP2)'].value = "";
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CLAS_EMP2)'].value = "";
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CLAS_EMP3)'].value = "";
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CLAS_EMP3)'].value = "";
		showFrame(target, action, width, height, msgConfirm, doSubmit, needToConfirm, form) ;
	}
	function  hideShowFrameCLAS_EMP2(target, action, width, height, msgConfirm, doSubmit, needToConfirm, form) {
		if(document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CLAS_EMP1)'].value != ""){
			document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CLAS_EMP2)'].value = "";
			document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CLAS_EMP2)'].value = "";
			document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CLAS_EMP3)'].value = "";
			document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CLAS_EMP3)'].value = "";
			showFrame(target, action, width, height, msgConfirm, doSubmit, needToConfirm, form) ;
		}
		else{
			alert("Quedan por rellenar campos");
		}
	}
	function  hideShowFrameCLAS_EMP3(target, action, width, height, msgConfirm, doSubmit, needToConfirm, form) {
		if(document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CLAS_EMP1)'].value != "" && document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CLAS_EMP2)'].value != ""){
			document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CLAS_EMP3)'].value = "";
			document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CLAS_EMP3)'].value = "";
			showFrame(target, action, width, height, msgConfirm, doSubmit, needToConfirm, form) ;
		}
		else{
			alert("Quedan por rellenar campos");
		}
	}

	function  hideDeleteCLAS_EMP1() {
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CLAS_EMP1)'].value = "";
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CLAS_EMP1)'].value = "";
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CLAS_EMP2)'].value = "";
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CLAS_EMP2)'].value = "";
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CLAS_EMP3)'].value = "";
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CLAS_EMP3)'].value = "";
	}
	function  hideDeleteCLAS_EMP2() {
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CLAS_EMP2)'].value = "";
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CLAS_EMP2)'].value = "";
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CLAS_EMP3)'].value = "";
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CLAS_EMP3)'].value = "";
	}
	function  hideDeleteCLAS_EMP3() {
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CLAS_EMP3)'].value = "";
		document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CLAS_EMP3)'].value = "";
	}


	function insertMultivalueElement(name, propertyName, size, maxlength, inputClass){
		insertCommonMultivalueElement(name, propertyName, size, maxlength, inputClass, '');
	}
	function insertCommonMultivalueElement(name, propertyName, size, maxlength, inputClass, blockHTML){
		var valor = "";
		var codigo = "";
		if(name=='DPCR_CODICE_EMPRESA_CPV'){
			//valor del cpv  
			   if(document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CPV1)'].value!=""){
				   if(document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CPV2)'].value!=""){
					   if(document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CPV3)'].value!=""){
						   if(document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CPV4)'].value!=""){
							   valor = document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CPV4)'].value;
							   codigo = document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CPV4)'].value;
						   }
						   else{
							   valor = document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CPV3)'].value;
							   codigo = document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CPV3)'].value;
						   }
					   }
					   else{
						   valor = document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CPV2)'].value;
						   codigo = document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CPV2)'].value;
					   }
				   }
				   else{
					   valor = document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CPV1)'].value;
					   codigo = document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CPV1)'].value;
				   }
			}
		}
		if(name=='DPCR_CODICE_EMPRESA_CLAS_EMP'){
			if(document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CLAS_EMP1)'].value!=""){
				   if(document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CLAS_EMP2)'].value!=""){
					   if(document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CLAS_EMP3)'].value!=""){
						   valor = document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CLAS_EMP3)'].value;
						   codigo = document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CLAS_EMP3)'].value;
					   }
					   else{
						   valor = document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CLAS_EMP2)'].value;
						   codigo = document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CLAS_EMP2)'].value;
					   }
				   }
				   else{
					   valor = document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CLAS_EMP1)'].value;
					   codigo = document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CLAS_EMP1)'].value;
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
	   
	   if(name=='DPCR_CODICE_EMPRESA_CPV'){
		   document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CPV1)'].value = "";
		   document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CPV2)'].value = "";
		   document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CPV3)'].value = "";
		   document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CPV4)'].value = "";
		   document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CPV1)'].value = "";
		   document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CPV2)'].value = "";
		   document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CPV3)'].value = "";
		   document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CPV4)'].value = "";
	   }
	   else{
		   document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CLAS_EMP1)'].value = "";
		   document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CLAS_EMP2)'].value = "";
		   document.forms[0].elements['property(DPCR_CODICE_EMPRESA:CLAS_EMP3)'].value = "";
		   document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CLAS_EMP1)'].value = "";
		   document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CLAS_EMP2)'].value = "";
		   document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CLAS_EMP3)'].value = "";
	   }

		   
		   
	}
//--></script>


<c1:set var="aux0">
	<bean:write name="defaultForm"
		property="entityApp.label(DPCR_CODICE_EMPRESA:DPCR_CODICE_EMPRESA)" />
</c1:set><jsp:useBean id="aux0" type="java.lang.String" />
<ispac:tabs>
	<ispac:tab title='<%=aux0%>'>
		<div id="dataBlock_DPCR_CODICE_EMPRESA" style="position: relative; height: 910px; width: 600px">
		
		
			<div id="label_DPCR_CODICE_EMPRESA:COD_CPV1"
				style="position: absolute; top: 10px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(DPCR_CODICE_EMPRESA:CPV1)" />:
			</div>
			
			<div id="data_DPCR_CODICE_EMPRESA:COD_CPV1" style="position: absolute; top: 10px; left: 220px; width: 100%;">
				<html:hidden property="property(DPCR_CODICE_EMPRESA:COD_CPV1)" onchange="alert('onchange1')"/>
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(DPCR_CODICE_EMPRESA:CPV1)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="3" cols="50" 
					id="SEARCH_DPCR_CODICE_EMPRESA_CPV1" target="workframe" 
					action="SelectListadoCodicePliegoCPVAction.do?atributo=COD_CPV&caracteres=2&cadenaInicio=0" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113" jsShowFrame="hideShowFrameCPV1" jsDelete="hideDeleteCPV1">
						<ispac:parameter name="SEARCH_DPCR_CODICE_EMPRESA_CPV1" id="property(DPCR_CODICE_EMPRESA:COD_CPV1)" property="VALOR" />
						<ispac:parameter name="SEARCH_DPCR_CODICE_EMPRESA_CPV1" id="property(DPCR_CODICE_EMPRESA:CPV1)" property="SUSTITUTO"/>
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>	
			
			<div id="data_DPCR_CODICE_EMPRESA:COD_CPV2" style="position: absolute; top: 50px; left: 220px; width: 100%;">
				<html:hidden property="property(DPCR_CODICE_EMPRESA:COD_CPV2)"/>
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(DPCR_CODICE_EMPRESA:CPV2)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="3" cols="50" 
					id="SEARCH_DPCR_CODICE_EMPRESA_CPV2" target="workframe" 
					action="SelectListadoCodicePliegoCPVAction.do?atributo=COD_CPV&caracteres=2&cadenaInicio=1&cod_cpv='+document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CPV1)'].value+'"
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113" jsShowFrame="hideShowFrameCPV2" jsDelete="hideDeleteCPV2">
						<ispac:parameter name="SEARCH_DPCR_CODICE_EMPRESA_CPV2" id="property(DPCR_CODICE_EMPRESA:COD_CPV2)" property="VALOR" />
						<ispac:parameter name="SEARCH_DPCR_CODICE_EMPRESA_CPV2" id="property(DPCR_CODICE_EMPRESA:CPV2)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			<div id="data_DPCR_CODICE_EMPRESA:COD_CPV3" style="position: absolute; top: 90px; left: 220px; width: 100%;">
				<html:hidden property="property(DPCR_CODICE_EMPRESA:COD_CPV3)"/>
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(DPCR_CODICE_EMPRESA:CPV3)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="3" cols="50" 
					id="SEARCH_DPCR_CODICE_EMPRESA_CPV3" target="workframe" 
					action="SelectListadoCodicePliegoCPVAction.do?atributo=COD_CPV&caracteres=2&cadenaInicio=2&cod_cpv='+document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CPV2)'].value+'"
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113" jsShowFrame="hideShowFrameCPV3" jsDelete="hideDeleteCPV3">
						<ispac:parameter name="SEARCH_DPCR_CODICE_EMPRESA_CPV3" id="property(DPCR_CODICE_EMPRESA:COD_CPV3)" property="VALOR" />
						<ispac:parameter name="SEARCH_DPCR_CODICE_EMPRESA_CPV3" id="property(DPCR_CODICE_EMPRESA:CPV3)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			<div id="data_DPCR_CODICE_EMPRESA:COD_CPV4" style="position: absolute; top: 130px; left: 220px; width: 100%;">
				<html:hidden property="property(DPCR_CODICE_EMPRESA:COD_CPV4)"/>
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(DPCR_CODICE_EMPRESA:CPV4)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="3" cols="50" 
					id="SEARCH_DPCR_CODICE_EMPRESA_CPV4" target="workframe" 
					action="SelectListadoCodicePliegoCPVAction.do?atributo=COD_CPV&caracteres=2&cadenaInicio=3&cod_cpv='+document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CPV3)'].value+'"
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113" jsShowFrame="hideShowFrameCPV4" jsDelete="hideDeleteCPV4">
						<ispac:parameter name="SEARCH_DPCR_CODICE_EMPRESA_CPV4" id="property(DPCR_CODICE_EMPRESA:COD_CPV4)" property="VALOR" />
						<ispac:parameter name="SEARCH_DPCR_CODICE_EMPRESA_CPV4" id="property(DPCR_CODICE_EMPRESA:CPV4)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			
			<div style="POSITION: absolute; WIDTH: 100%; TOP: 10px; LEFT:550px" id=data_DPCR_CODICE_EMPRESA:CPV>
				<ispac:htmlTextMultivalue
					property="propertyMultivalue(DPCR_CODICE_EMPRESA:CPV)" readonly="false"
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly"  size="50" divWidth="334" 
					maxlength="1024">
				</ispac:htmlTextMultivalue>
			</div>
			
			

		<div id="label_DPCR_CODICE_EMPRESA:COD_CLAS_EMP1"
				style="position: absolute; top: 180px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(DPCR_CODICE_EMPRESA:CLAS_EMP1)" />:
			</div>
			<div id="data_DPCR_CODICE_EMPRESA:COD_CLAS_EMP1" style="position: absolute; top: 180px; left: 220px; width: 100%;">
				<html:hidden property="property(DPCR_CODICE_EMPRESA:COD_CLAS_EMP1)"/>
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(DPCR_CODICE_EMPRESA:CLAS_EMP1)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="3" cols="50" 
					id="SEARCH_DPCR_CODICE_EMPRESA_CLAS_EMP1" target="workframe" 
					action="SelectListadoCodicePliegoCPVAction.do?atributo=COD_CLAS_EMP&caracteres=1&cadenaInicio=0" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113" jsShowFrame="hideShowFrameCLAS_EMP1"  jsDelete="hideDeleteCLAS_EMP1">
						<ispac:parameter name="SEARCH_DPCR_CODICE_EMPRESA_CLAS_EMP1" id="property(DPCR_CODICE_EMPRESA:COD_CLAS_EMP1)" property="VALOR" />
						<ispac:parameter name="SEARCH_DPCR_CODICE_EMPRESA_CLAS_EMP1" id="property(DPCR_CODICE_EMPRESA:CLAS_EMP1)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			<div id="data_DPCR_CODICE_EMPRESA:COD_CLAS_EMP2" style="position: absolute; top: 220px; left: 220px; width: 100%;">
				<html:hidden property="property(DPCR_CODICE_EMPRESA:COD_CLAS_EMP2)"/>
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(DPCR_CODICE_EMPRESA:CLAS_EMP2)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="3" cols="50" 
					id="SEARCH_DPCR_CODICE_EMPRESA_CLAS_EMP2" target="workframe" 
					action="SelectListadoCodicePliegoCPVAction.do?atributo=COD_CLAS_EMP&caracteres=1&cadenaInicio=1&cod_cpv='+document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CLAS_EMP1)'].value+'" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113" jsShowFrame="hideShowFrameCLAS_EMP2"  jsDelete="hideDeleteCLAS_EMP2">
						<ispac:parameter name="SEARCH_DPCR_CODICE_EMPRESA_CLAS_EMP2" id="property(DPCR_CODICE_EMPRESA:COD_CLAS_EMP2)" property="VALOR" />
						<ispac:parameter name="SEARCH_DPCR_CODICE_EMPRESA_CLAS_EMP2" id="property(DPCR_CODICE_EMPRESA:CLAS_EMP2)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			<div id="data_DPCR_CODICE_EMPRESA:COD_CLAS_EMP3" style="position: absolute; top: 260px; left: 220px; width: 100%;">
				<html:hidden property="property(DPCR_CODICE_EMPRESA:COD_CLAS_EMP3)"/>
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(DPCR_CODICE_EMPRESA:CLAS_EMP3)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="3" cols="50"
					id="SEARCH_DPCR_CODICE_EMPRESA_CLAS_EMP3" target="workframe" 
					action="SelectListadoCodicePliegoCPVAction.do?atributo=COD_CLAS_EMP&caracteres=1&cadenaInicio=2&cod_cpv='+document.forms[0].elements['property(DPCR_CODICE_EMPRESA:COD_CLAS_EMP2)'].value+'" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113" jsShowFrame="hideShowFrameCLAS_EMP3"  jsDelete="hideDeleteCLAS_EMP3">
						<ispac:parameter name="SEARCH_DPCR_CODICE_EMPRESA_CLAS_EMP3" id="property(DPCR_CODICE_EMPRESA:COD_CLAS_EMP3)" property="VALOR" />
						<ispac:parameter name="SEARCH_DPCR_CODICE_EMPRESA_CLAS_EMP3" id="property(DPCR_CODICE_EMPRESA:CLAS_EMP3)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			
			
			<div style="POSITION: absolute; WIDTH: 100%; TOP: 180px; LEFT:550px" id=data_DPCR_CODICE_EMPRESA:CLAS_EMP>
				<ispac:htmlTextMultivalue
					property="propertyMultivalue(DPCR_CODICE_EMPRESA:CLAS_EMP)" readonly="false"
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly"  size="50" divWidth="334" 
					maxlength="1024">
				</ispac:htmlTextMultivalue>
			</div>
			
			
			<div id="label_DPCR_CODICE_EMPRESA:COD_TIP_CAP_TECN"
				style="position: absolute; top: 310px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(DPCR_CODICE_EMPRESA:TIP_CAP_TECN)" />:
			</div>
			<div id="data_DPCR_CODICE_EMPRESA:COD_TIP_CAP_TECN" style="position: absolute; top: 310px; left: 220px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(DPCR_CODICE_EMPRESA:TIP_CAP_TECN)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="2" cols="80" 
					id="SEARCH_DPCR_CODICE_EMPRESA_TIP_CAP_TECN" target="workframe" 
					action="selectListadoCodicePliego.do?atributo=COD_TIP_CAP_TECN" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_DPCR_CODICE_EMPRESA_TIP_CAP_TECN" id="property(DPCR_CODICE_EMPRESA:TIP_CAP_TECN)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			
			<div id="label_DPCR_CODICE_EMPRESA:COD_TIP_DECLARACION"
				style="position: absolute; top: 350px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(DPCR_CODICE_EMPRESA:TIP_DECLARACION)" />:
			</div>
			<div id="data_DPCR_CODICE_EMPRESA:COD_TIP_DECLARACION" style="position: absolute; top: 350px; left: 220px; width: 100%;">
				<nobr>
					<ispac:htmlTextMultivalueImageFrame
						property="propertyMultivalue(DPCR_CODICE_EMPRESA:TIP_DECLARACION)"
						propertyDestination="propertyMultivalue(DPCR_CODICE_EMPRESA:TIP_DECLARACION)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" size="77"
						id="SEARCH_DPCR_CODICE_EMPRESA_TIP_DECLARACION" target="workframe"
						action="selectListadoCodicePliego.do?atributo=COD_TIP_DECLARACION"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" divWidth="500" divHeight="50">
						<ispac:parameterMultivalue name="SEARCH_DPCR_CODICE_EMPRESA_TIP_DECLARACION" id="propertyMultivalue(DPCR_CODICE_EMPRESA:TIP_DECLARACION)" 
						setMethod="id" property="SUSTITUTO" propertyDestination="propertyMultivalue(DPCR_CODICE_EMPRESA:TIP_DECLARACION)" />
					</ispac:htmlTextMultivalueImageFrame> 
				</nobr>
			</div>
			
			<div id="label_DPCR_CODICE_EMPRESA:COD_TIP_EVENTOS"
				style="position: absolute; top: 440px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(DPCR_CODICE_EMPRESA:TIP_EVENTOS)" />:
			</div>
			<div id="data_DPCR_CODICE_EMPRESA:COD_TIP_EVENTOS" style="position: absolute; top: 440px; left: 220px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(DPCR_CODICE_EMPRESA:TIP_EVENTOS)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="2" cols="80" 
					id="SEARCH_DPCR_CODICE_EMPRESA_TIP_EVENTOS" target="workframe" 
					action="selectListadoCodicePliego.do?atributo=COD_TIP_EVENTOS" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_DPCR_CODICE_EMPRESA_TIP_EVENTOS" id="property(DPCR_CODICE_EMPRESA:TIP_EVENTOS)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			
			
			<div id="label_DPCR_CODICE_EMPRESA:COD_TIP_EVID_ADM"
				style="position: absolute; top: 480px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(DPCR_CODICE_EMPRESA:TIP_EVID_ADM)" />:
			</div>
			<div id="data_DPCR_CODICE_EMPRESA:COD_TIP_EVID_ADM" style="position: absolute; top: 480px; left: 220px; width: 100%;">
				<nobr>
					<ispac:htmlTextMultivalueImageFrame
						property="propertyMultivalue(DPCR_CODICE_EMPRESA:TIP_EVID_ADM)"
						propertyDestination="propertyMultivalue(DPCR_CODICE_EMPRESA:TIP_EVID_ADM)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" size="77"
						id="SEARCH_DPCR_CODICE_EMPRESA_TIP_EVID_ADM" target="workframe"
						action="selectListadoCodicePliego.do?atributo=COD_TIP_EVID_ADM"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" divWidth="500" divHeight="50">
						<ispac:parameterMultivalue name="SEARCH_DPCR_CODICE_EMPRESA_TIP_EVID_ADM" id="propertyMultivalue(DPCR_CODICE_EMPRESA:TIP_EVID_ADM)" 
						setMethod="id" property="SUSTITUTO" propertyDestination="propertyMultivalue(DPCR_CODICE_EMPRESA:TIP_EVID_ADM)" />
					</ispac:htmlTextMultivalueImageFrame> 
				</nobr>
			</div>
			
			<div id="label_DPCR_CODICE_EMPRESA:COD_REQ_EJEC"
				style="position: absolute; top:570px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(DPCR_CODICE_EMPRESA:REQ_EJEC)" />:
			</div>
			<div id="data_DPCR_CODICE_EMPRESA:COD_REQ_EJEC" style="position: absolute; top: 570px; left: 220px; width: 100%;">
				<nobr>
					<ispac:htmlTextMultivalueImageFrame
						property="propertyMultivalue(DPCR_CODICE_EMPRESA:REQ_EJEC)"
						propertyDestination="propertyMultivalue(DPCR_CODICE_EMPRESA:REQ_EJEC)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" size="77"
						id="SEARCH_DPCR_CODICE_EMPRESA_REQ_EJEC" target="workframe"
						action="selectListadoCodicePliego.do?atributo=COD_REQ_EJEC"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" divWidth="500" divHeight="50">
						<ispac:parameterMultivalue name="SEARCH_DPCR_CODICE_EMPRESA_REQ_EJEC" id="propertyMultivalue(DPCR_CODICE_EMPRESA:REQ_EJEC)" 
						setMethod="id" property="SUSTITUTO" propertyDestination="propertyMultivalue(DPCR_CODICE_EMPRESA:REQ_EJEC)" />
					</ispac:htmlTextMultivalueImageFrame> 
				</nobr>
			</div>
			
			<div id="label_DPCR_CODICE_EMPRESA:COD_CAP_FINAN"
				style="position: absolute; top: 660px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(DPCR_CODICE_EMPRESA:CAP_FINAN)" />:
			</div>
			<div id="data_DPCR_CODICE_EMPRESA:COD_CAP_FINAN" style="position: absolute; top: 660px; left: 220px; width: 100%;">
				<nobr>
					<ispac:htmlTextMultivalueImageFrame
						property="propertyMultivalue(DPCR_CODICE_EMPRESA:CAP_FINAN)"
						propertyDestination="propertyMultivalue(DPCR_CODICE_EMPRESA:CAP_FINAN)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" size="77"
						id="SEARCH_DPCR_CODICE_EMPRESA_CAP_FINAN" target="workframe"
						action="selectListadoCodicePliego.do?atributo=COD_CAP_FINAN"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" divWidth="500" divHeight="50">
						<ispac:parameterMultivalue name="SEARCH_DPCR_CODICE_EMPRESA_CAP_FINAN" id="propertyMultivalue(DPCR_CODICE_EMPRESA:CAP_FINAN)" 
						setMethod="id" property="SUSTITUTO" propertyDestination="propertyMultivalue(DPCR_CODICE_EMPRESA:CAP_FINAN)" />
					</ispac:htmlTextMultivalueImageFrame>
				</nobr>
			</div>
			
			<div id="label_DPCR_CODICE_EMPRESA:COD_CONST_GARANT"
				style="position: absolute; top: 750px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(DPCR_CODICE_EMPRESA:CONST_GARANT)" />:
			</div>
			<div id="data_DPCR_CODICE_EMPRESA:COD_CONST_GARANT" style="position: absolute; top: 750px; left: 220px; width: 100%;">
				<nobr>
					<ispac:htmlTextMultivalueImageFrame
						property="propertyMultivalue(DPCR_CODICE_EMPRESA:CONST_GARANT)"
						propertyDestination="propertyMultivalue(DPCR_CODICE_EMPRESA:CONST_GARANT)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" size="77"
						id="SEARCH_DPCR_CODICE_EMPRESA_CONST_GARANT" target="workframe"
						action="selectListadoCodicePliego.do?atributo=COD_CONST_GARANT"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" divWidth="500" divHeight="50">
						<ispac:parameterMultivalue name="SEARCH_DPCR_CODICE_EMPRESA_CONST_GARANT" id="propertyMultivalue(DPCR_CODICE_EMPRESA:CONST_GARANT)" 
						setMethod="id" property="SUSTITUTO" propertyDestination="propertyMultivalue(DPCR_CODICE_EMPRESA:CONST_GARANT)" />
					</ispac:htmlTextMultivalueImageFrame>
				</nobr>
			</div>
			
			
			<div id="label_DPCR_CODICE_EMPRESA:COD_TIP_GARANTIA"
				style="position: absolute; top: 840px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(DPCR_CODICE_EMPRESA:TIP_GARANTIA)" />:
			</div>
			<div id="data_DPCR_CODICE_EMPRESA:COD_TIP_GARANTIA" style="position: absolute; top: 840px; left: 220px; width: 100%;">
				<nobr>
					<ispac:htmlTextMultivalueImageFrame
						property="propertyMultivalue(DPCR_CODICE_EMPRESA:TIP_GARANTIA)"
						propertyDestination="propertyMultivalue(DPCR_CODICE_EMPRESA:TIP_GARANTIA)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" size="77"
						id="SEARCH_DPCR_CODICE_EMPRESA_TIP_GARANTIA" target="workframe"
						action="selectListadoCodicePliego.do?atributo=COD_TIP_GARANTIA"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" divWidth="500" divHeight="50">
						<ispac:parameterMultivalue name="SEARCH_DPCR_CODICE_EMPRESA_TIP_GARANTIA" id="propertyMultivalue(DPCR_CODICE_EMPRESA:TIP_GARANTIA)" 
						setMethod="id" property="SUSTITUTO" propertyDestination="propertyMultivalue(DPCR_CODICE_EMPRESA:TIP_GARANTIA)" />
					</ispac:htmlTextMultivalueImageFrame>
				</nobr>
			</div>
		
		</div>
	</ispac:tab>
</ispac:tabs>
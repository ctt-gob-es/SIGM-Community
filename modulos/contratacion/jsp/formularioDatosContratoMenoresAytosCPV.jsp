<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c1"%>


<c1:set var="aux0">
	<bean:write name="defaultForm"
		property="entityApp.label(CONTRATACION_DATOS_CONTRATO:CONTRATACION_DATOS_CONTRATO)" />
</c1:set><jsp:useBean id="aux0" type="java.lang.String" />
<ispac:tabs>
	<ispac:tab title='<%=aux0%>'>
		<div id="dataBlock_CONTRATACION_DATOS_CONTRATO" style="position: relative; height: 300px; width: 600px">
			<div id="label_CONTRATACION_DATOS_CONTRATO:NCONTRATO"
				style="position: absolute; top: 10px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DATOS_CONTRATO:NCONTRATO)" />:
			</div>
			<div id="data_CONTRATACION_DATOS_CONTRATO:NCONTRATO"
				style="position: absolute; top: 10px; left: 200px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DATOS_CONTRATO:NCONTRATO)"
					readonly="true" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="255">
				</ispac:htmlText>
			</div>

			<div id="label_CONTRATACION_DATOS_CONTRATO:TIPO_CONTRATO"
				style="position: absolute; top: 35px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DATOS_CONTRATO:TIPO_CONTRATO)" />:
			</div>
			<div id="data_CONTRATACION_DATOS_CONTRATO:TIPO_CONTRATO"
				style="position: absolute; top: 35px; left: 200px; width: 100%;">
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

			<div id="label_CONTRATACION_DATOS_CONTRATO:OBJETO_CONTRATO"
				style="position: absolute; top: 65px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DATOS_CONTRATO:OBJETO_CONTRATO)" />:
			</div>
			<div id="data_CONTRATACION_DATOS_CONTRATO:OBJETO_CONTRATO"
				style="position: absolute; top: 65px; left: 200px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DATOS_CONTRATO:OBJETO_CONTRATO)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="300">
				</ispac:htmlText>
			</div>
				
			<div id="label_CONTRATACION_DATOS_CONTRATO:PRECIO_ESTIMADO_CONTRATO"
				style="position: absolute; top: 95px; left: 10px; width: 210px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_CONTRATO:PRECIO_ESTIMADO_CONTRATO)" />:</div>
			<div id="data_CONTRATACION_DATOS_CONTRATO:PRECIO_ESTIMADO_CONTRATO"
				style="position: absolute; top: 95px; left: 200px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DATOS_CONTRATO:PRECIO_ESTIMADO_CONTRATO)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="30" maxlength="50">
				</ispac:htmlText>
			</div>	
			
			<div id="label_CONTRATACION_DATOS_CONTRATO:PRESUPUESTOCONIMPUESTO"
				style="position: absolute; top: 125px; left: 10px; width: 210px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_CONTRATO:PRESUPUESTOCONIMPUESTO)" />:</div>
			<div id="data_CONTRATACION_DATOS_CONTRATO:PRESUPUESTOCONIMPUESTO"
				style="position: absolute; top: 125px; left: 200px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DATOS_CONTRATO:PRESUPUESTOCONIMPUESTO)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="30" maxlength="50">
				</ispac:htmlText>
			</div>	
			
			<div id="label_CONTRATACION_DATOS_CONTRATO:PRESUPUESTOSINIMPUESTO"
				style="position: absolute; top: 155px; left: 10px; width: 210px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_CONTRATO:PRESUPUESTOSINIMPUESTO)" />:</div>
			<div id="data_CONTRATACION_DATOS_CONTRATO:PRESUPUESTOSINIMPUESTO"
				style="position: absolute; top: 155px; left: 200px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DATOS_CONTRATO:PRESUPUESTOSINIMPUESTO)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="30" maxlength="50">
				</ispac:htmlText>
			</div>	
			
			<div id="label_CONTRATACION_DATOS_CONTRATO:CPV"
				style="position: absolute; top: 185px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DATOS_CONTRATO:CPV)" />:
			</div>
			
			<div id="data_CONTRATACION_DATOS_CONTRATO:COD_CPV1" style="position: absolute; top: 185px; left: 200px; width: 100%;">
				<html:hidden property="property(CONTRATACION_DATOS_CONTRATO:COD_CPV1)" onchange="alert('onchange1')"/>
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(CONTRATACION_DATOS_CONTRATO:CPV1)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="3" cols="80" 
					id="SEARCH_CONTRATACION_DATOS_CONTRATO_CPV1" target="workframe" 
					action="SelectListadoCodicePliegoCPVAction.do?atributo=COD_CPV&caracteres=2&cadenaInicio=0" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113" jsShowFrame="hideShowFrameCPV1" jsDelete="hideDeleteCPV1">
						<ispac:parameter name="SEARCH_CONTRATACION_DATOS_CONTRATO_CPV1" id="property(CONTRATACION_DATOS_CONTRATO:COD_CPV1)" property="VALOR" />
						<ispac:parameter name="SEARCH_CONTRATACION_DATOS_CONTRATO_CPV1" id="property(CONTRATACION_DATOS_CONTRATO:CPV1)" property="SUSTITUTO"/>
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
		
			
			<div style="POSITION: absolute; WIDTH: 100%; TOP: 185px; LEFT:700px" id=data_CONTRATACION_DATOS_CONTRATO:CPV>
				<ispac:htmlTextMultivalue
					property="propertyMultivalue(CONTRATACION_DATOS_CONTRATO:CPV)" readonly="false"
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly"  size="50" divWidth="334" 
					maxlength="1024">
				</ispac:htmlTextMultivalue>
			</div>
			
		</div>
	</ispac:tab>
</ispac:tabs>

<script language='JavaScript' type='text/javascript'><!--


function  hideShowFrameCPV1(target, action, width, height, msgConfirm, doSubmit, needToConfirm, form) {
	document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:COD_CPV1)'].value = "";
	document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:CPV1)'].value = "";
	showFrame(target, action, width, height, msgConfirm, doSubmit, needToConfirm, form) ;
}
function  hideDeleteCPV1() {
	document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:COD_CPV1)'].value = "";
	document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:CPV1)'].value = "";
}


function insertMultivalueElement(name, propertyName, size, maxlength, inputClass){
	insertCommonMultivalueElement(name, propertyName, size, maxlength, inputClass, '');
}
function insertCommonMultivalueElement(name, propertyName, size, maxlength, inputClass, blockHTML){
	var valor = "";
	var codigo = "";
	if(name=='CONTRATACION_DATOS_CONTRATO_CPV'){
		//valor del cpv  
		   if(document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:CPV1)'].value!=""){
			   valor = document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:CPV1)'].value;
			   codigo = document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:COD_CPV1)'].value;
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
   
   document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:CPV1)'].value = "";
   document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:COD_CPV1)'].value = "";   
	   
}


	//--></script>
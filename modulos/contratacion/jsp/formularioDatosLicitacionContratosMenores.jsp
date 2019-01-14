<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c1"%>
<c1:set var="aux0">
	<bean:write name="defaultForm"
		property="entityApp.label(CONTRATACION_DATOS_LIC:CONTRATACION_DATOS_LIC)" />
</c1:set><jsp:useBean id="aux0" type="java.lang.String" />
<ispac:tabs>
	<ispac:tab title='<%=aux0%>'>
		<div id="dataBlock_CONTRATACION_DATOS_LIC" style="position: relative; height: 220px; width: 600px">
		
		<div id="label_CONTRATACION_DATOS_LIC:COD_PRESENT_OFERTA"
				style="position: absolute; top: 10px; left: 10px; width: 220px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DATOS_LIC:PRESENT_OFERTA)" /> (*):
			</div>
			<div id="data_CONTRATACION_DATOS_LIC:COD_PRESENT_OFERTA" style="position: absolute; top: 10px; left: 240px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(CONTRATACION_DATOS_LIC:PRESENT_OFERTA)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="1" cols="80" 
					id="SEARCH_CODICE_LICITACION_PRESENT_OFERTA" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_PRES_OFERT_ORG_CONTRAT" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CODICE_LICITACION_PRESENT_OFERTA" id="property(CONTRATACION_DATOS_LIC:PRESENT_OFERTA)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			<div id="label_SPAC_EXPEDIENTES:SEP_INTERESADO_PRINCIPAL" style="position: absolute; top: 35px; left: 10px; width: 620px" class="textbar">
			ORGANO ASISTENCIA
			<hr class="formbar"/>
			</div>
			
			<div id="label_CONTRATACION_DATOS_LIC:IDENT_CM_OA"
				style="position: absolute; top: 65px; left: 10px; width: 230px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DATOS_LIC:IDENT_CM_OA)" />:
			</div>
			<div id="data_CONTRATACION_DATOS_LIC:IDENT_CM_OA"
				style="position: absolute; top: 65px; left: 170px;">
				<ispac:htmlText property="property(CONTRATACION_DATOS_LIC:IDENT_CM_OA)"
					readonly="true" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="81" maxlength="256"
					tabindex="116">
				</ispac:htmlText>
			</div>
			
			<div id="label_CONTRATACION_DATOS_LIC:NOMBRE_CM_OA"
				style="position: absolute; top: 95px; left: 10px; width: 230px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DATOS_LIC:NOMBRE_CM_OA)" />:
			</div>
			<div id="data_CONTRATACION_DATOS_LIC:NOMBRE_CM_OA"
				style="position: absolute; top: 95px; left: 170px;">
				<ispac:htmlText property="property(CONTRATACION_DATOS_LIC:NOMBRE_CM_OA)"
					readonly="true" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="81" maxlength="256"
					tabindex="116">
				</ispac:htmlText>
			</div>
			
			<div id="label_SPAC_EXPEDIENTES:SEP_INTERESADO_PRINCIPAL" style="position: absolute; top: 120px; left: 10px; width: 620px" class="textbar">
			APLICACIÓN PRESUPUESTARIA
			<hr class="formbar"/>
			</div>
			
			<div id="label_CONTRATACION_DATOS_LIC:APLICAPRESUP"
				style="position: absolute; top: 150px; left: 10px; width: 410px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DATOS_LIC:APLICAPRESUP)" /> (aplipres-anualidad-importe):
			</div>
			
			<div id="data_CONTRATACION_DATOS_LIC:APLICAPRESUP"
				style="position: absolute; top: 150px; left: 350px; width: 100%;">
				<nobr>
					<ispac:htmlTextMultivalue
						property="propertyMultivalue(CONTRATACION_DATOS_LIC:APLICAPRESUP)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="60" divWidth="500" divHeight="60"
						maxlength="1024">
					</ispac:htmlTextMultivalue>
				</nobr>
			</div>
			
	</ispac:tab>
</ispac:tabs>
<script> 
financiacion = document.forms[0].elements['property(CONTRATACION_DATOS_LIC:PROGRAMA_FINANCIACION)'].value;
if(financiacion == "EU - Subvencionado por la UE"){
	programa = document.forms[0].elements['property(CONTRATACION_DATOS_LIC:PROGRAMA)'].value; 
   	if (programa == ""){ 
      	//si era la cadena vacía es que no era válido. Lo aviso 
      	alert("Es obligatorio rellenar el programa cuando es subvencionado por la UE") 
      	//selecciono el texto 
      	document.forms[0].elements['property(CONTRATACION_DATOS_LIC:PROGRAMA)'].select(); 
      	//coloco otra vez el foco 
      	document.forms[0].elements['property(CONTRATACION_DATOS_LIC:PROGRAMA)'].focus();
      	document.forms[0].elements['property(CONTRATACION_DATOS_LIC:PROGRAMA)'].style.borderColor = 'red';
   	}else 
      	document.f1.numero.value = enteroValidado 
}    
</script> 

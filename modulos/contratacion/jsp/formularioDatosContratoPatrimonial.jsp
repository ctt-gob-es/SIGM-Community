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
		<div id="dataBlock_CONTRATACION_DATOS_CONTRATO"
			style="position: relative; height: 340px; width: 400px">
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
			
			<div id="label_CONTRATACION_DATOS_CONTRATO:PROVINCIA_CONTRATO"
				style="position: absolute; top: 10px; left: 650px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DATOS_CONTRATO:PROVINCIA_CONTRATO)" />:
			</div>
			<div id="data_CONTRATACION_DATOS_CONTRATO:PROVINCIA_CONTRATO"
				style="position: absolute; top: 10px; left: 760px; width: 100%;">
				<nobr>
					<ispac:htmlTextImageFrame
						property="property(CONTRATACION_DATOS_CONTRATO:PROVINCIA_CONTRATO)" readonly="true"
						readonlyTag="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="25"
						id="SEARCH_CONTRATACION_DATOS_CONTRATO_PROVINCIA_CONTRATO" target="workframe"
						action="selectListadoCodicePliego.do?atributo=NUTS"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
						<ispac:parameter name="SEARCH_CONTRATACION_DATOS_CONTRATO_PROVINCIA_CONTRATO" id="property(CONTRATACION_DATOS_CONTRATO:PROVINCIA_CONTRATO)" property="SUSTITUTO" />
					</ispac:htmlTextImageFrame>
				</nobr>
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

			<div id="label_CONTRATACION_DATOS_CONTRATO:COD_CONTRATO_SUMIN"
				style="position: absolute; top: 60px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DATOS_CONTRATO:CONTRATO_SUMIN)" />:
			</div>
			<div id="data_CONTRATACION_DATOS_CONTRATO:COD_CONTRATO_SUMIN" style="position: absolute; top: 60px; left: 200px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(CONTRATACION_DATOS_CONTRATO:CONTRATO_SUMIN)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="1" cols="80" 
					id="SEARCH_CODICE_PLIEGOS_CONTRATO_SUMIN" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_CONTRATO_SUMIN&tipoContrato='+document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:TIPO_CONTRATO)'].value+'" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CODICE_PLIEGOS_CONTRATO_SUMIN" id="property(CONTRATACION_DATOS_CONTRATO:CONTRATO_SUMIN)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_DATOS_CONTRATO:OBJETO_CONTRATO"
				style="position: absolute; top: 85px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DATOS_CONTRATO:OBJETO_CONTRATO)" />:
			</div>
			<div id="data_CONTRATACION_DATOS_CONTRATO:OBJETO_CONTRATO"
				style="position: absolute; top: 85px; left: 200px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DATOS_CONTRATO:OBJETO_CONTRATO)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="300">
				</ispac:htmlText>
			</div>
			

			<div id="label_CONTRATACION_DATOS_CONTRATO:PROC_ADJ"
				style="position: absolute; top: 110px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DATOS_CONTRATO:PROC_ADJ)" />:
			</div>
			<div id="data_CONTRATACION_DATOS_CONTRATO:PROC_ADJ"
				style="position: absolute; top: 110px; left: 200px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(CONTRATACION_DATOS_CONTRATO:PROC_ADJ)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="1" cols="80" 
					id="SEARCH_CODICE_PLIEGOS_PROC_CONT_SECPUB" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_PROC_CONT_SECPUB" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CODICE_PLIEGOS_PROC_CONT_SECPUB" id="property(CONTRATACION_DATOS_CONTRATO:PROC_ADJ)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_DATOS_CONTRATO:FORMA_TRAMITACION"
				style="position: absolute; top: 135px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DATOS_CONTRATO:FORMA_TRAMITACION)" />:
			</div>
			<div id="data_CONTRATACION_DATOS_CONTRATO:FORMA_TRAMITACION"
				style="position: absolute; top: 135px; left: 200px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(CONTRATACION_DATOS_CONTRATO:FORMA_TRAMITACION)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="1" cols="80" 
					id="SEARCH_CODICE_PLIEGOS_FORMA_TRAMITACION" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_PROCLICIT" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CODICE_PLIEGOS_FORMA_TRAMITACION" id="property(CONTRATACION_DATOS_CONTRATO:FORMA_TRAMITACION)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_DATOS_CONTRATO:COD_TRAM_GASTO"
				style="position: absolute; top: 160px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DATOS_CONTRATO:TRAM_GASTO)" />:
			</div>
			<div id="data_CONTRATACION_DATOS_CONTRATO:COD_TRAM_GASTO" style="position: absolute; top: 160px; left: 200px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(CONTRATACION_DATOS_CONTRATO:TRAM_GASTO)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="1" cols="80" 
					id="SEARCH_CODICE_PLIEGOS_TRAM_GASTO" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_TRAM_GASTO" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CODICE_PLIEGOS_TRAM_GASTO" id="property(CONTRATACION_DATOS_CONTRATO:TRAM_GASTO)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			

			<div id="label_CONTRATACION_DATOS_CONTRATO:ORGANO_CONTRATACION"
				style="position: absolute; top: 185px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DATOS_CONTRATO:ORGANO_CONTRATACION)" />:
			</div>
			<div id="data_CONTRATACION_DATOS_CONTRATO:ORGANO_CONTRATACION"
				style="position: absolute; top: 185px; left: 200px; width: 100%;">
				<nobr> 
					<html:hidden property="property(CONTRATACION_DATOS_CONTRATO:ORGANO_CONTRATACION)" />
					<ispac:htmlTextImageFrame
						property="property(ORGANO_CONTRATACION_SECR_VLDTBL_ORGANOS:SUSTITUTO)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" size="80"
						id="SEARCH_CONTRATACION_DATOS_CONTRATO_ORGANO_CONTRATACION"
						target="workframe"
						action="selectSubstitute.do?entity=CONTRATACION_ORGANO"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
							<ispac:parameter
								name="SEARCH_CONTRATACION_DATOS_CONTRATO_ORGANO_CONTRATACION"
								id="property(CONTRATACION_DATOS_CONTRATO:ORGANO_CONTRATACION)"
								property="VALOR" />
							<ispac:parameter
								name="SEARCH_CONTRATACION_DATOS_CONTRATO_ORGANO_CONTRATACION"
								id="property(ORGANO_CONTRATACION_SECR_VLDTBL_ORGANOS:SUSTITUTO)"
								property="SUSTITUTO" />
					</ispac:htmlTextImageFrame> 
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_DATOS_CONTRATO:CONT_SUJ_REG_ARMO"
				style="position: absolute; top: 210px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DATOS_CONTRATO:CONT_SUJ_REG_ARMO)" />:
			</div>
			<div id="data_CONTRATACION_DATOS_CONTRATO:CONT_SUJ_REG_ARMO"
				style="position: absolute; top: 210px; left: 200px; width: 100%;">
				<nobr> 
					<ispac:htmlTextImageFrame
						property="property(CONTRATACION_DATOS_CONTRATO:CONT_SUJ_REG_ARMO)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" size="80"
						id="SEARCH_CONTRATACION_DATOS_CONTRATO_CONT_SUJ_REG_ARMO"
						target="workframe" action="selectValue.do?entity=SPAC_TBL_009"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
							<ispac:parameter
								name="SEARCH_CONTRATACION_DATOS_CONTRATO_CONT_SUJ_REG_ARMO"
								id="property(CONTRATACION_DATOS_CONTRATO:CONT_SUJ_REG_ARMO)"
								property="VALOR" />
					</ispac:htmlTextImageFrame> 
				</nobr>
			</div>
			<div id="label_CONTRATACION_DATOS_CONTRATO:COD_ARC_CLASE_NOMENC"
				style="position: absolute; top: 235px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DATOS_CONTRATO:COD_ARC_CLASE_NOMENC)" />:
			</div>
			<div id="data_CONTRATACION_DATOS_CONTRATO:COD_ARC_CLASE_NOMENC"
				style="position: absolute; top: 235px; left: 200px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DATOS_CONTRATO:COD_ARC_CLASE_NOMENC)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="300">
				</ispac:htmlText>
			</div>
			
			<div id="label_CONTRATACION_DATOS_CONTRATO:PRECIO_ESTIMADO_CONTRATO"
				style="position: absolute; top: 260px; left: 10px; width: 210px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_CONTRATO:PRECIO_ESTIMADO_CONTRATO)" />:</div>
			<div id="data_CONTRATACION_DATOS_CONTRATO:PRECIO_ESTIMADO_CONTRATO"
				style="position: absolute; top: 260px; left: 200px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DATOS_CONTRATO:PRECIO_ESTIMADO_CONTRATO)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="30" maxlength="50">
				</ispac:htmlText>
			</div>	
			
			<div id="label_CONTRATACION_DATOS_CONTRATO:PRESUPUESTOCONIMPUESTO"
				style="position: absolute; top: 285px; left: 10px; width: 210px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_CONTRATO:PRESUPUESTOCONIMPUESTO)" />:</div>
			<div id="data_CONTRATACION_DATOS_CONTRATO:PRESUPUESTOCONIMPUESTO"
				style="position: absolute; top: 285px; left: 200px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DATOS_CONTRATO:PRESUPUESTOCONIMPUESTO)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="30" maxlength="50">
				</ispac:htmlText>
			</div>	
			
			<div id="label_CONTRATACION_DATOS_CONTRATO:PRESUPUESTOSINIMPUESTO"
				style="position: absolute; top: 310px; left: 10px; width: 210px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_CONTRATO:PRESUPUESTOSINIMPUESTO)" />:</div>
			<div id="data_CONTRATACION_DATOS_CONTRATO:PRESUPUESTOSINIMPUESTO"
				style="position: absolute; top: 310px; left: 200px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DATOS_CONTRATO:PRESUPUESTOSINIMPUESTO)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="30" maxlength="50">
				</ispac:htmlText>
			</div>			
		</div>
	</ispac:tab>
</ispac:tabs>

<script language='JavaScript' type='text/javascript'><!--

function clearContSum() {
	document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:CONTRATO_SUMIN)'].value = "";
}

function save() {
	var presupuesto = document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:PRECIO_ESTIMADO_CONTRATO)'].value;
	presupuesto = presupuesto.replace(",",".");
	document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:PRECIO_ESTIMADO_CONTRATO)'].value=presupuesto;
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
//--></script>
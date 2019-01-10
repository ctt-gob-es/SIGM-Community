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
			style="position: relative; height: 590px; width: 400px">
			<div id="label_CONTRATACION_DATOS_CONTRATO:NCONTRATO"
				style="position: absolute; top: 10px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DATOS_CONTRATO:NCONTRATO)" />:
			</div>
			<div id="data_CONTRATACION_DATOS_CONTRATO:NCONTRATO"
				style="position: absolute; top: 10px; left: 180px; width: 100%;">
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
				style="position: absolute; top: 35px; left: 180px; width: 100%;">
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
			<div id="data_CONTRATACION_DATOS_CONTRATO:COD_CONTRATO_SUMIN" style="position: absolute; top: 60px; left: 180px; width: 100%;">
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
				style="position: absolute; top: 85px; left: 180px; width: 100%;">
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
				style="position: absolute; top: 110px; left: 180px; width: 100%;">
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
				style="position: absolute; top: 135px; left: 180px; width: 100%;">
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
			<div id="data_CONTRATACION_DATOS_CONTRATO:COD_TRAM_GASTO" style="position: absolute; top: 160px; left: 180px; width: 100%;">
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
				style="position: absolute; top: 185px; left: 180px; width: 100%;">
				<nobr>
					<html:hidden
						property="property(CONTRATACION_DATOS_CONTRATO:ORGANO_CONTRATACION)" />
					<ispac:htmlTextImageFrame
						property="property(ORGANO_CONTRATACION_CONTRATACION_ORGANO:SUSTITUTO)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" size="80"
						imageTabIndex="true"
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
							id="property(ORGANO_CONTRATACION_CONTRATACION_ORGANO:SUSTITUTO)"
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
				style="position: absolute; top: 210px; left: 180px; width: 100%;">
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
				style="position: absolute; top: 235px; left: 180px; width: 100%;">
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
				style="position: absolute; top: 260px; left: 180px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DATOS_CONTRATO:PRECIO_ESTIMADO_CONTRATO)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="30" maxlength="50">
				</ispac:htmlText>
			</div>
			
			<div id="label_CONTRATACION_DATOS_CONTRATO:CPV"
				style="position: absolute; top: 285px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DATOS_CONTRATO:CPV)" />:
			</div>
			
			<div id="data_CONTRATACION_DATOS_CONTRATO:COD_CPV1" style="position: absolute; top: 285px; left: 180px; width: 100%;">
				<html:hidden property="property(CONTRATACION_DATOS_CONTRATO:COD_CPV1)" onchange="alert('onchange1')"/>
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(CONTRATACION_DATOS_CONTRATO:CPV1)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="3" cols="50" 
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
			
			<div id="data_CONTRATACION_DATOS_CONTRATO:COD_CPV2" style="position: absolute; top: 325px; left: 180px; width: 100%;">
				<html:hidden property="property(CONTRATACION_DATOS_CONTRATO:COD_CPV2)"/>
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(CONTRATACION_DATOS_CONTRATO:CPV2)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="3" cols="50" 
					id="SEARCH_CONTRATACION_DATOS_CONTRATO_CPV2" target="workframe" 
					action="SelectListadoCodicePliegoCPVAction.do?atributo=COD_CPV&caracteres=2&cadenaInicio=1&cod_cpv='+document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:COD_CPV1)'].value+'"
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113" jsShowFrame="hideShowFrameCPV2" jsDelete="hideDeleteCPV2">
						<ispac:parameter name="SEARCH_CONTRATACION_DATOS_CONTRATO_CPV2" id="property(CONTRATACION_DATOS_CONTRATO:COD_CPV2)" property="VALOR" />
						<ispac:parameter name="SEARCH_CONTRATACION_DATOS_CONTRATO_CPV2" id="property(CONTRATACION_DATOS_CONTRATO:CPV2)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			<div id="data_CONTRATACION_DATOS_CONTRATO:COD_CPV3" style="position: absolute; top: 365px; left: 180px; width: 100%;">
				<html:hidden property="property(CONTRATACION_DATOS_CONTRATO:COD_CPV3)"/>
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(CONTRATACION_DATOS_CONTRATO:CPV3)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="3" cols="50" 
					id="SEARCH_CONTRATACION_DATOS_CONTRATO_CPV3" target="workframe" 
					action="SelectListadoCodicePliegoCPVAction.do?atributo=COD_CPV&caracteres=2&cadenaInicio=2&cod_cpv='+document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:COD_CPV2)'].value+'"
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113" jsShowFrame="hideShowFrameCPV3" jsDelete="hideDeleteCPV3">
						<ispac:parameter name="SEARCH_CONTRATACION_DATOS_CONTRATO_CPV3" id="property(CONTRATACION_DATOS_CONTRATO:COD_CPV3)" property="VALOR" />
						<ispac:parameter name="SEARCH_CONTRATACION_DATOS_CONTRATO_CPV3" id="property(CONTRATACION_DATOS_CONTRATO:CPV3)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			<div id="data_CONTRATACION_DATOS_CONTRATO:COD_CPV4" style="position: absolute; top: 405px; left: 180px; width: 100%;">
				<html:hidden property="property(CONTRATACION_DATOS_CONTRATO:COD_CPV4)"/>
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(CONTRATACION_DATOS_CONTRATO:CPV4)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="3" cols="50" 
					id="SEARCH_CONTRATACION_DATOS_CONTRATO_CPV4" target="workframe" 
					action="SelectListadoCodicePliegoCPVAction.do?atributo=COD_CPV&caracteres=2&cadenaInicio=3&cod_cpv='+document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:COD_CPV3)'].value+'"
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113" jsShowFrame="hideShowFrameCPV4" jsDelete="hideDeleteCPV4">
						<ispac:parameter name="SEARCH_CONTRATACION_DATOS_CONTRATO_CPV4" id="property(CONTRATACION_DATOS_CONTRATO:COD_CPV4)" property="VALOR" />
						<ispac:parameter name="SEARCH_CONTRATACION_DATOS_CONTRATO_CPV4" id="property(CONTRATACION_DATOS_CONTRATO:CPV4)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			
			<div style="POSITION: absolute; WIDTH: 100%; TOP: 285px; LEFT:570px" id=data_CONTRATACION_DATOS_CONTRATO:CPV>
				<ispac:htmlTextMultivalue
					property="propertyMultivalue(CONTRATACION_DATOS_CONTRATO:CPV)" readonly="false"
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly"  size="45" divWidth="334" 
					maxlength="1024">
				</ispac:htmlTextMultivalue>
			</div>
			
			<div id="label_CONTRATACION_DATOS_CONTRATO:NUEVA_LEY"
				style="position: absolute; top: 470px; left: 10px; width: 250px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DATOS_CONTRATO:NUEVA_LEY)" />:
			</div>
			<div id="data_CONTRATACION_DATOS_CONTRATO:NUEVA_LEY"
				style="position: absolute; top: 466px; left: 280px; width: 100%;">
				<nobr> 
					<ispac:htmlTextImageFrame
						property="property(CONTRATACION_DATOS_CONTRATO:NUEVA_LEY)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" size="1"
						id="SEARCH_CONTRATACION_DATOS_CONTRATO_NUEVA_LEY"
						target="workframe" action="selectValue.do?entity=SPAC_TBL_009"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
							<ispac:parameter
								name="SEARCH_CONTRATACION_DATOS_CONTRATO_NUEVA_LEY"
								id="property(CONTRATACION_DATOS_CONTRATO:NUEVA_LEY)"
								property="VALOR" />
					</ispac:htmlTextImageFrame> 
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_DATOS_CONTRATO:PROVINCIA_CONTRATO"
				style="position: absolute; top: 495px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DATOS_CONTRATO:PROVINCIA_CONTRATO)" />:
			</div>
			<div id="data_CONTRATACION_DATOS_CONTRATO:PROVINCIA_CONTRATO"
				style="position: absolute; top: 495px; left: 180px; width: 100%;">
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
			
			<div id="LABEL_CONTRATACION_DATOS_CONTRATO:CARACTERISTICA_BIENES_RENDCUEN"
				style="position: absolute; top: 520px; left: 10px; width: 160px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DATOS_CONTRATO:CARACTERISTICA_BIENES_RENDCUEN)" />
				:
			</div>
			<div id="data_CONTRATACION_DATOS_CONTRATO:CARACTERISTICA_BIENES_RENDCUEN"
				style="position: absolute; top: 520px; left: 180px; width: 100%;">
				<nobr>
					<ispac:htmlTextImageFrame
						property="property(CONTRATACION_DATOS_CONTRATO:CARACTERISTICA_BIENES_RENDCUEN)" readonly="true"
						readonlyTag="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="25"
						id="SEARCH_CONTRATACION_DATOS_CONTRATO_CARACTERISTICA_BIENES_RENDCUEN" target="workframe"
						action="selectValue.do?entity=RENDCUENTAS_VLDTBL_CARACBIEN"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
						<ispac:parameter name="SEARCH_CONTRATACION_DATOS_CONTRATO_CARACTERISTICA_BIENES_RENDCUEN"
							id="property(CONTRATACION_DATOS_CONTRATO:CARACTERISTICA_BIENES_RENDCUEN)" property="VALOR" />
					</ispac:htmlTextImageFrame>
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_DATOS_CONTRATO:PROCNEGARTICULO"
				style="position: absolute; top: 545px; left: 10px; width: 210px;" 
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DATOS_CONTRATO:PROCNEGARTICULO)" />:
			</div>
			<div id="data_CONTRATACION_DATOS_CONTRATO:PROCNEGARTICULO"
				style="position: absolute; top: 545px; left: 180px; width: 100%;">
				<nobr>
					<ispac:htmlTextImageFrame
						property="property(CONTRATACION_DATOS_CONTRATO:PROCNEGARTICULO)" readonly="true"
						readonlyTag="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="25"
						id="SEARCH_CONTRATACION_DATOS_CONTRATO_PROCNEGARTICULO" target="workframe"
						action="selectValue.do?entity=CONTRATACION_RENDC_PROCNEGCA"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
						<ispac:parameter name="SEARCH_CONTRATACION_DATOS_CONTRATO_PROCNEGARTICULO"
							id="property(CONTRATACION_DATOS_CONTRATO:PROCNEGARTICULO)" property="VALOR" />
					</ispac:htmlTextImageFrame>
				</nobr>
			</div>
			
			<div id="LABEL_CONTRATACION_DATOS_CONTRATO:ABIERTO_CRITERIOS_MULTIPLES"
				style="position: absolute; top: 570px; left: 10px; width: 160px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DATOS_CONTRATO:ABIERTO_CRITERIOS_MULTIPLES)" />
				:
			</div>
			<div id="data_CONTRATACION_DATOS_CONTRATO:ABIERTO_CRITERIOS_MULTIPLES"
				style="position: absolute; top: 570px; left: 180px; width: 100%;">
				<nobr>
					<ispac:htmlTextImageFrame
						property="property(CONTRATACION_DATOS_CONTRATO:ABIERTO_CRITERIOS_MULTIPLES)" readonly="true"
						readonlyTag="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="1"
						id="SEARCH_CONTRATACION_DATOS_CONTRATO_ABIERTO_CRITERIOS_MULTIPLES" target="workframe"
						action="selectValue.do?entity=SPAC_TBL_009"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
						<ispac:parameter name="SEARCH_CONTRATACION_DATOS_CONTRATO_ABIERTO_CRITERIOS_MULTIPLES"
							id="property(CONTRATACION_DATOS_CONTRATO:ABIERTO_CRITERIOS_MULTIPLES)" property="VALOR" />
					</ispac:htmlTextImageFrame>
				</nobr>
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


function  hideShowFrameCPV1(target, action, width, height, msgConfirm, doSubmit, needToConfirm, form) {
	document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:COD_CPV1)'].value = "";
	document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:CPV1)'].value = "";
	document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:COD_CPV2)'].value = "";
	document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:CPV2)'].value = "";
	document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:COD_CPV3)'].value = "";
	document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:CPV3)'].value = "";
	document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:COD_CPV4)'].value = "";
	document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:CPV4)'].value = "";
	showFrame(target, action, width, height, msgConfirm, doSubmit, needToConfirm, form) ;
}
function  hideShowFrameCPV2(target, action, width, height, msgConfirm, doSubmit, needToConfirm, form) {
	if(document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:CPV1)'].value != ""){
		document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:COD_CPV2)'].value = "";
		document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:CPV2)'].value = "";
		document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:COD_CPV3)'].value = "";
		document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:CPV3)'].value = "";
		document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:COD_CPV4)'].value = "";
		document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:CPV4)'].value = "";
		showFrame(target, action, width, height, msgConfirm, doSubmit, needToConfirm, form) ;
	}
	else{
		alert("Quedan por rellenar campos");
	}
}
function  hideShowFrameCPV3(target, action, width, height, msgConfirm, doSubmit, needToConfirm, form) {
	if(document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:CPV1)'].value != "" && document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:CPV2)'].value != ""){
		document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:COD_CPV3)'].value = "";
		document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:CPV3)'].value = "";
		document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:COD_CPV4)'].value = "";
		document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:CPV4)'].value = "";
		showFrame(target, action, width, height, msgConfirm, doSubmit, needToConfirm, form) ;
	}
	else{
		alert("Quedan por rellenar campos");
	}
}
function  hideShowFrameCPV4(target, action, width, height, msgConfirm, doSubmit, needToConfirm, form) {
	if(document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:CPV1)'].value != "" && document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:CPV2)'].value != "" && document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:CPV3)'].value != ""){
		document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:COD_CPV4)'].value = "";
		document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:CPV4)'].value = "";
		showFrame(target, action, width, height, msgConfirm, doSubmit, needToConfirm, form) ;
	}
	else{
		alert("Quedan por rellenar campos");
	}
}
function  hideDeleteCPV1() {
	document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:COD_CPV1)'].value = "";
	document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:CPV1)'].value = "";
	document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:COD_CPV2)'].value = "";
	document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:CPV2)'].value = "";
	document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:COD_CPV3)'].value = "";
	document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:CPV3)'].value = "";
	document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:COD_CPV4)'].value = "";
	document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:CPV4)'].value = "";
}
function  hideDeleteCPV2() {
	document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:COD_CPV2)'].value = "";
	document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:CPV2)'].value = "";
	document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:COD_CPV3)'].value = "";
	document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:CPV3)'].value = "";
	document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:COD_CPV4)'].value = "";
	document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:CPV4)'].value = "";
}
function  hideDeleteCPV3() {
	document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:COD_CPV3)'].value = "";
	document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:CPV3)'].value = "";
	document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:COD_CPV4)'].value = "";
	document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:CPV4)'].value = "";
}
function  hideDeleteCPV4() {
	document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:COD_CPV4)'].value = "";
	document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:CPV4)'].value = "";
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
			   if(document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:CPV2)'].value!=""){
				   if(document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:CPV3)'].value!=""){
					   if(document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:CPV4)'].value!=""){
						   valor = document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:CPV4)'].value;
						   codigo = document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:COD_CPV4)'].value;
					   }
					   else{
						   valor = document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:CPV3)'].value;
						   codigo = document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:COD_CPV3)'].value;
					   }
				   }
				   else{
					   valor = document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:CPV2)'].value;
					   codigo = document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:COD_CPV2)'].value;
				   }
			   }
			   else{
				   valor = document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:CPV1)'].value;
				   codigo = document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:COD_CPV1)'].value;
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
   
   document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:CPV1)'].value = "";
   document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:CPV2)'].value = "";
   document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:CPV3)'].value = "";
   document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:CPV4)'].value = "";
   document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:COD_CPV1)'].value = "";
   document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:COD_CPV2)'].value = "";
   document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:COD_CPV3)'].value = "";
   document.forms[0].elements['property(CONTRATACION_DATOS_CONTRATO:COD_CPV4)'].value = "";
   
	   
}


	//--></script>
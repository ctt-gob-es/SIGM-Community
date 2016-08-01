<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c1"%>
<c1:set var="aux0">
	<bean:write name="defaultForm"
		property="entityApp.label(CONTRATACION_PLICA:CONTRATACION_PLICA)" />
</c1:set><jsp:useBean id="aux0" type="java.lang.String" />
<ispac:tabs>
	<ispac:tab title='<%=aux0%>'>
		<div id="dataBlock_CONTRATACION_PLICA"
			style="position: relative; height: 300px; width: 600px">
			<div id="label_CONTRATACION_PLICA:REPRESENTANTE"
				style="position: absolute; top: 10px; left: 10px; width: 110px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_PLICA:REPRESENTANTE)" />:</div>
			<div id="data_CONTRATACION_PLICA:REPRESENTANTE"
				style="position: absolute; top: 10px; left: 130px; width: 100%;">
			<ispac:htmlText property="property(CONTRATACION_PLICA:REPRESENTANTE)"
				readonly="false" propertyReadonly="readonly" styleClass="input"
				styleClassReadonly="inputReadOnly" size="80" maxlength="255">
			</ispac:htmlText></div>
			
			<div id="label_REC_APLAZ_FRACC:VOLUNTARIA" style="padding-top:35px; padding-left:10px; width: 200px;" class="textbar1">
			<nobr>Documentación Aportada</nobr>
			<hr class="formbar1"/>
			</div>
			
			
			<div id="label_CONTRATACION_PLICA:DNI"
				style="position: absolute; top: 60px; left: 10px; width: 110px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_PLICA:DNI)" />:</div>
			<div id="data_CONTRATACION_PLICA:DNI"
				style="position: absolute; top: 60px; left: 130px; width: 100%;">
			<nobr> <ispac:htmlTextImageFrame
				property="property(CONTRATACION_PLICA:DNI)" readonly="true"
				readonlyTag="false" propertyReadonly="readonly" styleClass="input"
				styleClassReadonly="inputReadOnly" size="2"
				id="SEARCH_CONTRATACION_PLICA_DNI" target="workframe"
				action="selectValue.do?entity=SPAC_TBL_009" image="img/search-mg.gif"
				titleKeyLink="title.link.data.selection" imageDelete="img/borrar.gif"
				titleKeyImageDelete="title.delete.data.selection"
				styleClassDeleteLink="tdlink"
				confirmDeleteKey="msg.delete.data.selection" showDelete="true"
				showFrame="true" width="640" height="480">
				<ispac:parameter name="SEARCH_CONTRATACION_PLICA_DNI"
					id="property(CONTRATACION_PLICA:DNI)" property="VALOR" />
			</ispac:htmlTextImageFrame> </nobr></div>
			
			<div id="label_CONTRATACION_PLICA:ESCRITURA"
				style="position: absolute; top: 60px; left: 230px; width: 110px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_PLICA:ESCRITURA)" />:</div>
			<div id="data_CONTRATACION_PLICA:ESCRITURA"
				style="position: absolute; top: 60px; left: 350px; width: 100%;">
			<nobr> <ispac:htmlTextImageFrame
				property="property(CONTRATACION_PLICA:ESCRITURA)" readonly="true"
				readonlyTag="false" propertyReadonly="readonly" styleClass="input"
				styleClassReadonly="inputReadOnly" size="2"
				id="SEARCH_CONTRATACION_PLICA_ESCRITURA" target="workframe"
				action="selectValue.do?entity=SPAC_TBL_009" image="img/search-mg.gif"
				titleKeyLink="title.link.data.selection" imageDelete="img/borrar.gif"
				titleKeyImageDelete="title.delete.data.selection"
				styleClassDeleteLink="tdlink"
				confirmDeleteKey="msg.delete.data.selection" showDelete="true"
				showFrame="true" width="640" height="480">
				<ispac:parameter name="SEARCH_CONTRATACION_PLICA_ESCRITURA"
					id="property(CONTRATACION_PLICA:ESCRITURA)" property="VALOR" />
			</ispac:htmlTextImageFrame> </nobr></div>
			
			<div id="label_CONTRATACION_PLICA:PODER"
				style="position: absolute; top: 60px; left: 450px; width: 110px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_PLICA:PODER)" />:</div>
			<div id="data_CONTRATACION_PLICA:PODER"
				style="position: absolute; top: 60px; left: 570px; width: 100%;">
			<nobr> <ispac:htmlTextImageFrame
				property="property(CONTRATACION_PLICA:PODER)" readonly="true"
				readonlyTag="false" propertyReadonly="readonly" styleClass="input"
				styleClassReadonly="inputReadOnly" size="2"
				id="SEARCH_CONTRATACION_PLICA_PODER" target="workframe"
				action="selectValue.do?entity=SPAC_TBL_009" image="img/search-mg.gif"
				titleKeyLink="title.link.data.selection" imageDelete="img/borrar.gif"
				titleKeyImageDelete="title.delete.data.selection"
				styleClassDeleteLink="tdlink"
				confirmDeleteKey="msg.delete.data.selection" showDelete="true"
				showFrame="true" width="640" height="480">
				<ispac:parameter name="SEARCH_CONTRATACION_PLICA_PODER"
					id="property(CONTRATACION_PLICA:PODER)" property="VALOR" />
			</ispac:htmlTextImageFrame> </nobr></div>
			
			<div id="label_CONTRATACION_PLICA:DECLARACION_JURADA"
				style="position: absolute; top: 85px; left: 10px; width: 110px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_PLICA:DECLARACION_JURADA)" />:</div>
			<div id="data_CONTRATACION_PLICA:DECLARACION_JURADA"
				style="position: absolute; top: 85px; left: 130px; width: 100%;">
			<nobr> <ispac:htmlTextImageFrame
				property="property(CONTRATACION_PLICA:DECLARACION_JURADA)"
				readonly="true" readonlyTag="false" propertyReadonly="readonly"
				styleClass="input" styleClassReadonly="inputReadOnly" size="2"
				id="SEARCH_CONTRATACION_PLICA_DECLARACION_JURADA" target="workframe"
				action="selectValue.do?entity=SPAC_TBL_009" image="img/search-mg.gif"
				titleKeyLink="title.link.data.selection" imageDelete="img/borrar.gif"
				titleKeyImageDelete="title.delete.data.selection"
				styleClassDeleteLink="tdlink"
				confirmDeleteKey="msg.delete.data.selection" showDelete="true"
				showFrame="true" width="640" height="480">
				<ispac:parameter name="SEARCH_CONTRATACION_PLICA_DECLARACION_JURADA"
					id="property(CONTRATACION_PLICA:DECLARACION_JURADA)"
					property="VALOR" />
			</ispac:htmlTextImageFrame> </nobr></div>
			
			<div id="label_CONTRATACION_PLICA:SOLVENCIA_ECONOMICA"
				style="position: absolute; top: 85px; left: 230px; width: 110px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_PLICA:SOLVENCIA_ECONOMICA)" />:</div>
			<div id="data_CONTRATACION_PLICA:SOLVENCIA_ECONOMICA"
				style="position: absolute; top: 85px; left: 350px; width: 100%;">
			<nobr> <ispac:htmlTextImageFrame
				property="property(CONTRATACION_PLICA:SOLVENCIA_ECONOMICA)"
				readonly="true" readonlyTag="false" propertyReadonly="readonly"
				styleClass="input" styleClassReadonly="inputReadOnly" size="2"
				id="SEARCH_CONTRATACION_PLICA_SOLVENCIA_ECONOMICA" target="workframe"
				action="selectValue.do?entity=SPAC_TBL_009" image="img/search-mg.gif"
				titleKeyLink="title.link.data.selection" imageDelete="img/borrar.gif"
				titleKeyImageDelete="title.delete.data.selection"
				styleClassDeleteLink="tdlink"
				confirmDeleteKey="msg.delete.data.selection" showDelete="true"
				showFrame="true" width="640" height="480">
				<ispac:parameter name="SEARCH_CONTRATACION_PLICA_SOLVENCIA_ECONOMICA"
					id="property(CONTRATACION_PLICA:SOLVENCIA_ECONOMICA)"
					property="VALOR" />
			</ispac:htmlTextImageFrame> </nobr></div>
			
			<div id="label_CONTRATACION_PLICA:SOLVENCIA_TECNICA"
				style="position: absolute; top: 85px; left: 450px; width: 110px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_PLICA:SOLVENCIA_TECNICA)" />:</div>
			<div id="data_CONTRATACION_PLICA:SOLVENCIA_TECNICA"
				style="position: absolute; top: 85px; left: 570px; width: 100%;">
			<nobr> <ispac:htmlTextImageFrame
				property="property(CONTRATACION_PLICA:SOLVENCIA_TECNICA)"
				readonly="true" readonlyTag="false" propertyReadonly="readonly"
				styleClass="input" styleClassReadonly="inputReadOnly" size="2"
				id="SEARCH_CONTRATACION_PLICA_SOLVENCIA_TECNICA" target="workframe"
				action="selectValue.do?entity=SPAC_TBL_009" image="img/search-mg.gif"
				titleKeyLink="title.link.data.selection" imageDelete="img/borrar.gif"
				titleKeyImageDelete="title.delete.data.selection"
				styleClassDeleteLink="tdlink"
				confirmDeleteKey="msg.delete.data.selection" showDelete="true"
				showFrame="true" width="640" height="480">
				<ispac:parameter name="SEARCH_CONTRATACION_PLICA_SOLVENCIA_TECNICA"
					id="property(CONTRATACION_PLICA:SOLVENCIA_TECNICA)" property="VALOR" />
			</ispac:htmlTextImageFrame> </nobr></div>
			
			<div id="label_CONTRATACION_PLICA:DECLARACION_VINCULACION"
				style="position: absolute; top: 110px; left: 10px; width: 110px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_PLICA:DECLARACION_VINCULACION)" />:</div>
			<div id="data_CONTRATACION_PLICA:DECLARACION_VINCULACION"
				style="position: absolute; top: 110px; left: 130px; width: 100%;">
			<nobr> <ispac:htmlTextImageFrame
				property="property(CONTRATACION_PLICA:DECLARACION_VINCULACION)"
				readonly="true" readonlyTag="false" propertyReadonly="readonly"
				styleClass="input" styleClassReadonly="inputReadOnly" size="2"
				id="SEARCH_CONTRATACION_PLICA_DECLARACION_VINCULACION" target="workframe"
				action="selectValue.do?entity=SPAC_TBL_009" image="img/search-mg.gif"
				titleKeyLink="title.link.data.selection" imageDelete="img/borrar.gif"
				titleKeyImageDelete="title.delete.data.selection"
				styleClassDeleteLink="tdlink"
				confirmDeleteKey="msg.delete.data.selection" showDelete="true"
				showFrame="true" width="640" height="480">
				<ispac:parameter name="SEARCH_CONTRATACION_PLICA_DECLARACION_VINCULACION"
					id="property(CONTRATACION_PLICA:DECLARACION_VINCULACION)" property="VALOR" />
			</ispac:htmlTextImageFrame> </nobr></div>
			
			<div id="label_CONTRATACION_PLICA:DECLARACION_CONDICIONES"
				style="position: absolute; top: 110px; left: 230px; width: 110px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_PLICA:DECLARACION_CONDICIONES)" />:</div>
			<div id="data_CONTRATACION_PLICA:DECLARACION_CONDICIONES"
				style="position: absolute; top: 110px; left: 350px; width: 100%;">
			<nobr> <ispac:htmlTextImageFrame
				property="property(CONTRATACION_PLICA:DECLARACION_CONDICIONES)"
				readonly="true" readonlyTag="false" propertyReadonly="readonly"
				styleClass="input" styleClassReadonly="inputReadOnly" size="2"
				id="SEARCH_CONTRATACION_PLICA_DECLARACION_CONDICIONES"
				target="workframe" action="selectValue.do?entity=SPAC_TBL_009"
				image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
				imageDelete="img/borrar.gif"
				titleKeyImageDelete="title.delete.data.selection"
				styleClassDeleteLink="tdlink"
				confirmDeleteKey="msg.delete.data.selection" showDelete="true"
				showFrame="true" width="640" height="480">
				<ispac:parameter
					name="SEARCH_CONTRATACION_PLICA_DECLARACION_CONDICIONES"
					id="property(CONTRATACION_PLICA:DECLARACION_CONDICIONES)"
					property="VALOR" />
			</ispac:htmlTextImageFrame> </nobr></div>
			
			<div id="label_CONTRATACION_PLICA:CLASIFICACION"
				style="position: absolute; top: 110px; left: 450px; width: 110px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_PLICA:CLASIFICACION)" />:</div>
			<div id="data_CONTRATACION_PLICA:CLASIFICACION"
				style="position: absolute; top: 110px; left: 570px; width: 100%;">
			<ispac:htmlText property="property(CONTRATACION_PLICA:CLASIFICACION)"
				readonly="false" propertyReadonly="readonly" styleClass="input"
				styleClassReadonly="inputReadOnly" size="2" maxlength="255">
			</ispac:htmlText></div>
			
			<div id="label_CONTRATACION_PLICA:CLAS_EMPRESARIAL"
				style="position: absolute; top: 155px; left: 10px; width: 110px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_PLICA:CLAS_EMPRESARIAL)" />:</div>
			<div id="data_CONTRATACION_PLICA:CLAS_EMPRESARIAL"
				style="position: absolute; top: 155px; left: 130px; width: 100%;">
			<nobr> <ispac:htmlTextImageFrame
				property="property(CONTRATACION_PLICA:CLAS_EMPRESARIAL)"
				readonly="true" readonlyTag="false" propertyReadonly="readonly"
				styleClass="input" styleClassReadonly="inputReadOnly" size="2"
				id="SEARCH_CONTRATACION_PLICA_CLAS_EMPRESARIAL" target="workframe"
				action="selectValue.do?entity=SPAC_TBL_009" image="img/search-mg.gif"
				titleKeyLink="title.link.data.selection" imageDelete="img/borrar.gif"
				titleKeyImageDelete="title.delete.data.selection"
				styleClassDeleteLink="tdlink"
				confirmDeleteKey="msg.delete.data.selection" showDelete="true"
				showFrame="true" width="640" height="480">
				<ispac:parameter name="SEARCH_CONTRATACION_PLICA_CLAS_EMPRESARIAL"
					id="property(CONTRATACION_PLICA:CLAS_EMPRESARIAL)" property="VALOR" />
			</ispac:htmlTextImageFrame> </nobr></div>
			<div id="data_CONTRATACION_PLICA:CLAS_EMPRESARIAL1"
				style="position: absolute; top: 155px; left: 230px; width: 100%;">
			<ispac:htmlText property="property(CONTRATACION_PLICA:CLAS_EMPRESARIAL1)"
				readonly="false" propertyReadonly="readonly" styleClass="input"
				styleClassReadonly="inputReadOnly" size="2" maxlength="255">
			</ispac:htmlText></div>
			
			
			<div id="label_CONTRATACION_PLICA:REGISTROLICITADORES"
				style="position: absolute; top: 195px; left: 10px; width: 450px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_PLICA:REGISTROLICITADORES)" />:</div>
			<div id="data_CONTRATACION_PLICA:DECLARACION_VINCULACION"
				style="position: absolute; top: 190px; left: 440px; width: 100%;">
			<nobr> <ispac:htmlTextImageFrame
				property="property(CONTRATACION_PLICA:REGISTROLICITADORES)"
				readonly="true" readonlyTag="false" propertyReadonly="readonly"
				styleClass="input" styleClassReadonly="inputReadOnly" size="2"
				id="SEARCH_CONTRATACION_PLICA_REGISTROLICITADORES" target="workframe"
				action="selectValue.do?entity=SPAC_TBL_009" image="img/search-mg.gif"
				titleKeyLink="title.link.data.selection" imageDelete="img/borrar.gif"
				titleKeyImageDelete="title.delete.data.selection"
				styleClassDeleteLink="tdlink"
				confirmDeleteKey="msg.delete.data.selection" showDelete="true"
				showFrame="true" width="640" height="480">
				<ispac:parameter name="SEARCH_CONTRATACION_PLICA_REGISTROLICITADORES"
					id="property(CONTRATACION_PLICA:REGISTROLICITADORES)" property="VALOR" />
			</ispac:htmlTextImageFrame> </nobr></div>
			
			<div id="label_CONTRATACION_PLICA:DOCCONFIDENCIALES"
				style="position: absolute; top: 235px; left: 10px; width: 450px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_PLICA:DOCCONFIDENCIALES)" />:</div>
			<div id="data_CONTRATACION_PLICA:DOCCONFIDENCIALES"
				style="position: absolute; top: 235px; left: 440px; width: 100%;">
			<nobr> <ispac:htmlTextImageFrame
				property="property(CONTRATACION_PLICA:DOCCONFIDENCIALES)"
				readonly="true" readonlyTag="false" propertyReadonly="readonly"
				styleClass="input" styleClassReadonly="inputReadOnly" size="2"
				id="SEARCH_CONTRATACION_PLICA_DOCCONFIDENCIALES" target="workframe"
				action="selectValue.do?entity=SPAC_TBL_009" image="img/search-mg.gif"
				titleKeyLink="title.link.data.selection" imageDelete="img/borrar.gif"
				titleKeyImageDelete="title.delete.data.selection"
				styleClassDeleteLink="tdlink"
				confirmDeleteKey="msg.delete.data.selection" showDelete="true"
				showFrame="true" width="640" height="480">
				<ispac:parameter name="SEARCH_CONTRATACION_PLICA_DOCCONFIDENCIALESS"
					id="property(CONTRATACION_PLICA:DOCCONFIDENCIALES)" property="VALOR" />
			</ispac:htmlTextImageFrame> </nobr></div>
			
			<div id="label_REC_APLAZ_FRACC:VOLUNTARIA" style="padding-top:195px; padding-left:10px; width: 100px;" class="textbar1">
			<nobr>Licitador</nobr>
			<hr class="formbar1"/>
			</div>
			
			<div id="label_CONTRATACION_PLICA:APTO"
				style="position: absolute; top: 280px; left: 10px; width: 110px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_PLICA:APTO)" />:</div>
			<div id="data_CONTRATACION_PLICA:APTO"
				style="position: absolute; top: 280px; left: 130px; width: 100%;">
			<nobr> <ispac:htmlTextImageFrame
				property="property(CONTRATACION_PLICA:APTO)"
				readonly="true" readonlyTag="false" propertyReadonly="readonly"
				styleClass="input" styleClassReadonly="inputReadOnly" size="2"
				id="SEARCH_CONTRATACION_PLICA_APTO"
				target="workframe" action="selectValue.do?entity=SPAC_TBL_009"
				image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
				imageDelete="img/borrar.gif"
				titleKeyImageDelete="title.delete.data.selection"
				styleClassDeleteLink="tdlink"
				confirmDeleteKey="msg.delete.data.selection" showDelete="true"
				showFrame="true" width="640" height="480">
				<ispac:parameter
					name="SEARCH_CONTRATACION_PLICA_APTO"
					id="property(CONTRATACION_PLICA:APTO)"
					property="VALOR" />
			</ispac:htmlTextImageFrame> </nobr></div>
			
		</div>
	</ispac:tab>
</ispac:tabs>
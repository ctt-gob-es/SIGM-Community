<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c1"%>
<c1:set var="aux0">
	<bean:write name="defaultForm"
		property="entityApp.label(DPCR_CODICE_PLIEGOS:DPCR_CODICE_PLIEGOS)" />
</c1:set><jsp:useBean id="aux0" type="java.lang.String" />
<ispac:tabs>
	<ispac:tab title='<%=aux0%>'>
		<div id="dataBlock_DPCR_CODICE_PLIEGOS"
			style="position: relative; height: 305px; width: 600px">
			
		<div id="label_DPCR_CODICE_PLIEGOS:COD_TIPO_ADJUDICACION"
			style="position: absolute; top: 10px; left: 10px; width: 110px;"
			class="formsTitleB">
			<bean:write name="defaultForm" property="entityApp.label(DPCR_CODICE_PLIEGOS:TIPO_ADJUDICACION)" />:
		</div>
		<div id="data_DPCR_CODICE_PLIEGOS:COD_TIPO_ADJUDICACION" style="position: absolute; top: 10px; left: 130px; width: 100%;">
		
			<nobr>
				<ispac:htmlTextareaImageFrame property="property(DPCR_CODICE_PLIEGOS:TIPO_ADJUDICACION)" readonly="true" readonlyTag="false" 
				propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="2" cols="80" 
				id="SEARCH_CODICE_PLIEGOS" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_TIPO_ADJUDICACION" 
				image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
				styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
				tabindex="113">
					<ispac:parameter name="SEARCH_CODICE_PLIEGOS" id="property(DPCR_CODICE_PLIEGOS:COD_TIPO_ADJUDICACION)" property="VALOR" />
					<ispac:parameter name="SEARCH_CODICE_PLIEGOS" id="property(DPCR_CODICE_PLIEGOS:TIPO_ADJUDICACION)" property="SUSTITUTO" />
				</ispac:htmlTextareaImageFrame>
			</nobr>
		</div>
		
		
		<div id="label_DPCR_CODICE_PLIEGOS:COD_EXPRESIONES_CALCULO_VAL"
			style="position: absolute; top: 60px; left: 10px; width: 110px;"
			class="formsTitleB"><bean:write name="defaultForm"
			property="entityApp.label(DPCR_CODICE_PLIEGOS:COD_EXPRESIONES_CALCULO_VAL)" />:</div>
		<div id="data_DPCR_CODICE_PLIEGOS:COD_EXPRESIONES_CALCULO_VAL"
			style="position: absolute; top: 60px; left: 130px; width: 100%;">
		<ispac:htmlText
			property="property(DPCR_CODICE_PLIEGOS:COD_EXPRESIONES_CALCULO_VAL)"
			readonly="false" propertyReadonly="readonly" styleClass="input"
			styleClassReadonly="inputReadOnly" size="80" maxlength="20">
		</ispac:htmlText></div>
		
		<div id="label_DPCR_CODICE_PLIEGOS:EXPRESIONES_CALCULO_VAL"
			style="position: absolute; top: 85px; left: 10px; width: 110px;"
			class="formsTitleB"><bean:write name="defaultForm"
			property="entityApp.label(DPCR_CODICE_PLIEGOS:EXPRESIONES_CALCULO_VAL)" />:</div>
		<div id="data_DPCR_CODICE_PLIEGOS:EXPRESIONES_CALCULO_VAL"
			style="position: absolute; top: 85px; left: 130px; width: 100%;">
		<ispac:htmlText
			property="property(DPCR_CODICE_PLIEGOS:EXPRESIONES_CALCULO_VAL)"
			readonly="false" propertyReadonly="readonly" styleClass="input"
			styleClassReadonly="inputReadOnly" size="80" maxlength="255">
		</ispac:htmlText></div>
		
		<div id="label_DPCR_CODICE_PLIEGOS:COD_TIPO_CONTRATO"
			style="position: absolute; top: 110px; left: 10px; width: 110px;"
			class="formsTitleB"><bean:write name="defaultForm"
			property="entityApp.label(DPCR_CODICE_PLIEGOS:COD_TIPO_CONTRATO)" />:</div>
		<div id="data_DPCR_CODICE_PLIEGOS:COD_TIPO_CONTRATO"
			style="position: absolute; top: 110px; left: 130px; width: 100%;">
		<ispac:htmlText
			property="property(DPCR_CODICE_PLIEGOS:COD_TIPO_CONTRATO)"
			readonly="false" propertyReadonly="readonly" styleClass="input"
			styleClassReadonly="inputReadOnly" size="80" maxlength="20">
		</ispac:htmlText></div>
		
		<div id="label_DPCR_CODICE_PLIEGOS:TIPO_CONTRATO"
			style="position: absolute; top: 135px; left: 10px; width: 110px;"
			class="formsTitleB"><bean:write name="defaultForm"
			property="entityApp.label(DPCR_CODICE_PLIEGOS:TIPO_CONTRATO)" />:</div>
		<div id="data_DPCR_CODICE_PLIEGOS:TIPO_CONTRATO"
			style="position: absolute; top: 135px; left: 130px; width: 100%;">
		<ispac:htmlText property="property(DPCR_CODICE_PLIEGOS:TIPO_CONTRATO)"
			readonly="false" propertyReadonly="readonly" styleClass="input"
			styleClassReadonly="inputReadOnly" size="80" maxlength="255">
		</ispac:htmlText></div>
		
		<div id="label_DPCR_CODICE_PLIEGOS:COD_CPV"
			style="position: absolute; top: 160px; left: 10px; width: 110px;"
			class="formsTitleB"><bean:write name="defaultForm"
			property="entityApp.label(DPCR_CODICE_PLIEGOS:COD_CPV)" />:</div>
		<div id="data_DPCR_CODICE_PLIEGOS:COD_CPV"
			style="position: absolute; top: 160px; left: 130px; width: 100%;">
		<ispac:htmlText property="property(DPCR_CODICE_PLIEGOS:COD_CPV)"
			readonly="false" propertyReadonly="readonly" styleClass="input"
			styleClassReadonly="inputReadOnly" size="80" maxlength="20">
		</ispac:htmlText></div>
		
		<div id="label_DPCR_CODICE_PLIEGOS:CPV"
			style="position: absolute; top: 185px; left: 10px; width: 110px;"
			class="formsTitleB"><bean:write name="defaultForm"
			property="entityApp.label(DPCR_CODICE_PLIEGOS:CPV)" />:</div>
		<div id="data_DPCR_CODICE_PLIEGOS:CPV"
			style="position: absolute; top: 185px; left: 130px; width: 100%;">
		<ispac:htmlText property="property(DPCR_CODICE_PLIEGOS:CPV)"
			readonly="false" propertyReadonly="readonly" styleClass="input"
			styleClassReadonly="inputReadOnly" size="80" maxlength="255">
		</ispac:htmlText></div>
		</div>
	</ispac:tab>
</ispac:tabs>
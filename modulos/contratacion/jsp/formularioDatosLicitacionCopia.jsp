<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c1"%>
<c1:set var="aux0">
	<bean:write name="defaultForm"
		property="entityApp.label(DPCR_CODICE_LICITACION:DPCR_CODICE_LICITACION)" />
</c1:set><jsp:useBean id="aux0" type="java.lang.String" />
<ispac:tabs>
	<ispac:tab title='<%=aux0%>'>
		<div id="dataBlock_DPCR_CODICE_LICITACION" style="position: relative; height: 305px; width: 600px">
			
			<div id="label_DPCR_CODICE_LICITACION:COD_EXPRESIONES_CALCULO_VAL"
				style="position: absolute; top: 10px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(DPCR_CODICE_LICITACION:EXPRESIONES_CALCULO_VAL)" />:
			</div>
			<div id="data_DPCR_CODICE_LICITACION:COD_EXPRESIONES_CALCULO_VAL" style="position: absolute; top: 10px; left: 220px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(DPCR_CODICE_LICITACION:EXPRESIONES_CALCULO_VAL)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="2" cols="80" 
					id="SEARCH_CODICE_LICITACION_COD_EXPRESIONES_CALCULO_VAL" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_EXPRESIONES_CALCULO_VAL" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CODICE_LICITACION_COD_EXPRESIONES_CALCULO_VAL" id="property(DPCR_CODICE_LICITACION:EXPRESIONES_CALCULO_VAL)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			
			<div id="label_DPCR_CODICE_LICITACION:COD_JUST_PROCESO"
				style="position: absolute; top: 40px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(DPCR_CODICE_LICITACION:JUST_PROCESO)" />:
			</div>
			<div id="data_DPCR_CODICE_LICITACION:COD_JUST_PROCESO" style="position: absolute; top: 40px; left: 220px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(DPCR_CODICE_LICITACION:JUST_PROCESO)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="2" cols="80" 
					id="SEARCH_CODICE_LICITACION_JUST_PROCESO" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_JUST_PROCESO" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CODICE_LICITACION_JUST_PROCESO" id="property(DPCR_CODICE_LICITACION:JUST_PROCESO)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			
			<div id="label_DPCR_CODICE_LICITACION:COD_DESC_INI_EVENTOS"
				style="position: absolute; top: 70px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(DPCR_CODICE_LICITACION:DESC_INI_EVENTOS)" />:
			</div>
			<div id="data_DPCR_CODICE_LICITACION:COD_DESC_INI_EVENTOS" style="position: absolute; top: 70px; left: 220px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(DPCR_CODICE_LICITACION:DESC_INI_EVENTOS)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="2" cols="80" 
					id="SEARCH_CODICE_LICITACION_DESC_INI_EVENTOS" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_DESC_INI_EVENTOS" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CODICE_LICITACION_DESC_INI_EVENTOS" id="property(DPCR_CODICE_LICITACION:DESC_INI_EVENTOS)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			
			<div id="label_DPCR_CODICE_LICITACION:COD_PRES_OFERT_ORG_CONTRAT"
				style="position: absolute; top: 100px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(DPCR_CODICE_LICITACION:PRES_OFERT_ORG_CONTRAT)" />:
			</div>
			<div id="data_DPCR_CODICE_LICITACION:COD_PRES_OFERT_ORG_CONTRAT" style="position: absolute; top: 100px; left: 220px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(DPCR_CODICE_LICITACION:PRES_OFERT_ORG_CONTRAT)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="2" cols="80" 
					id="SEARCH_CODICE_LICITACION_PRES_OFERT_ORG_CONTRAT" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_PRES_OFERT_ORG_CONTRAT" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CODICE_LICITACION_PRES_OFERT_ORG_CONTRAT" id="property(DPCR_CODICE_LICITACION:PRES_OFERT_ORG_CONTRAT)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			
			<div id="label_DPCR_CODICE_LICITACION:COD_PRESENT_OFERTA"
				style="position: absolute; top: 130px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(DPCR_CODICE_LICITACION:PRESENT_OFERTA)" />:
			</div>
			<div id="data_DPCR_CODICE_LICITACION:COD_PRESENT_OFERTA" style="position: absolute; top: 130px; left: 220px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(DPCR_CODICE_LICITACION:PRESENT_OFERTA)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="2" cols="80" 
					id="SEARCH_CODICE_LICITACION_PRESENT_OFERTA" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_PRESENT_OFERTA" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CODICE_LICITACION_PRESENT_OFERTA" id="property(DPCR_CODICE_LICITACION:PRESENT_OFERTA)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			
			<div id="label_DPCR_CODICE_LICITACION:COD_TRAM_GASTO"
				style="position: absolute; top: 160px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(DPCR_CODICE_LICITACION:TRAM_GASTO)" />:
			</div>
			<div id="data_DPCR_CODICE_LICITACION:COD_TRAM_GASTO" style="position: absolute; top: 160px; left: 220px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(DPCR_CODICE_LICITACION:TRAM_GASTO)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="2" cols="80" 
					id="SEARCH_CODICE_LICITACION_TRAM_GASTO" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_TRAM_GASTO" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CODICE_LICITACION_TRAM_GASTO" id="property(DPCR_CODICE_LICITACION:TRAM_GASTO)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			
			<div id="label_DPCR_CODICE_LICITACION:COD_FREC_PAGOS"
				style="position: absolute; top: 190px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(DPCR_CODICE_LICITACION:FREC_PAGOS)" />:
			</div>
			<div id="data_DPCR_CODICE_LICITACION:COD_FREC_PAGOS" style="position: absolute; top: 190px; left: 220px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(DPCR_CODICE_LICITACION:FREC_PAGOS)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="2" cols="80" 
					id="SEARCH_CODICE_LICITACION_FREC_PAGOS" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_FREC_PAGOS" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CODICE_LICITACION_FREC_PAGOS" id="property(DPCR_CODICE_LICITACION:FREC_PAGOS)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			
			<div id="label_DPCR_CODICE_LICITACION:COD_TIPO_OFERTA"
				style="position: absolute; top: 220px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(DPCR_CODICE_LICITACION:TIPO_OFERTA)" />:
			</div>
			<div id="data_DPCR_CODICE_LICITACION:COD_TIPO_OFERTA" style="position: absolute; top: 220px; left: 220px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(DPCR_CODICE_LICITACION:TIPO_OFERTA)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="2" cols="80" 
					id="SEARCH_CODICE_LICITACION_TIPO_OFERTA" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_TIPO_OFERTA" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CODICE_LICITACION_TIPO_OFERTA" id="property(DPCR_CODICE_LICITACION:TIPO_OFERTA)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			
			<div id="label_DPCR_CODICE_LICITACION:COD_VAL_TIP_OFERTA"
				style="position: absolute; top: 250px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(DPCR_CODICE_LICITACION:VAL_TIP_OFERTA)" />:
			</div>
			<div id="data_DPCR_CODICE_LICITACION:COD_VAL_TIP_OFERTA" style="position: absolute; top: 250px; left: 220px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(DPCR_CODICE_LICITACION:VAL_TIP_OFERTA)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="2" cols="80" 
					id="SEARCH_CODICE_LICITACION_VAL_TIP_OFERTA" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_VAL_TIP_OFERTA" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CODICE_LICITACION_VAL_TIP_OFERTA" id="property(DPCR_CODICE_LICITACION:VAL_TIP_OFERTA)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			
			<div id="label_DPCR_CODICE_LICITACION:COD_ALG_CALC_POND"
				style="position: absolute; top: 280px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(DPCR_CODICE_LICITACION:ALG_CALC_POND)" />:
			</div>
			<div id="data_DPCR_CODICE_LICITACION:COD_ALG_CALC_POND" style="position: absolute; top: 280px; left: 220px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(DPCR_CODICE_LICITACION:ALG_CALC_POND)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="2" cols="80" 
					id="SEARCH_CODICE_LICITACION_ALG_CALC_POND" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_ALG_CALC_POND" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CODICE_LICITACION_ALG_CALC_POND" id="property(DPCR_CODICE_LICITACION:ALG_CALC_POND)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
		
		</div>
	</ispac:tab>
</ispac:tabs>
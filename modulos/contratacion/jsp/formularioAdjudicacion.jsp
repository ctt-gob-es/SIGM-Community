<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c1"%>
<c1:set var="aux0">
	<bean:write name="defaultForm"
		property="entityApp.label(CONTRATACION_ADJUDICACION:CONTRATACION_ADJUDICACION)" />
</c1:set><jsp:useBean id="aux0" type="java.lang.String" />
<ispac:tabs>
	<ispac:tab title='<%=aux0%>'>
		<div id="dataBlock_CONTRATACION_ADJUDICACION"
			style="position: relative; height: 120px; width: 600px">
			
			<div id="label_SPAC_EXPEDIENTES:SEP_INTERESADO_PRINCIPAL"
				title="Criterios para evaluar la solvencia técnica que determina la capacidad de un operador económico para participar en la licitación."
				style="position: absolute; top: 10px; left: 10px; width: 620px"
				class="textbar">
				ENVÍO ANUNCIO A LA PLATAFORMA DE CONTRATACIÓN
				<hr class="formbar" />
			</div>

			
			<div id="label_CONTRATACION_ADJUDICACION:ENVIOANUNCIOPLACE"
				style="position: absolute; top: 40px; left: 10px; width: 230px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_ADJUDICACION:ENVIOANUNCIOPLACE)" />
				:
			</div>
			<div id="data_CONTRATACION_ADJUDICACION:ENVIOANUNCIOPLACE"
				style="position: absolute; top: 40px; left: 250px; width: 100%;">
				<nobr>
					<ispac:htmlTextImageFrame
						property="property(CONTRATACION_ADJUDICACION:ENVIOANUNCIOPLACE)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" size="20"
						id="SEARCH_CONTRATACION_ADJUDICACION:ENVIOANUNCIOPLACE"
						target="workframe" action="selectValue.do?entity=SPAC_TBL_009"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true" jsDelete="deleteAnuncio"
						showFrame="true" width="640" height="480">
						<ispac:parameter
							name="SEARCH_CONTRATACION_ADJUDICACION:ENVIOANUNCIOPLACE"
							id="property(CONTRATACION_ADJUDICACION:ENVIOANUNCIOPLACE)"
							property="VALOR" />
					</ispac:htmlTextImageFrame>
				</nobr>
			</div>
			
			<div id="label_SPAC_EXPEDIENTES:SEP_INTERESADO_PRINCIPAL"
				style="position: absolute; top: 80px; left: 10px; width: 620px"
				class="textbar">
				RESULTADO LICITACIÓN
				<hr class="formbar" />
			</div>
			
			<div id="label_CONTRATACION_ADJUDICACION:RES_LICITACION"
				style="position: absolute; top: 110px; left: 10px; width: 250px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_ADJUDICACION:RES_LICITACION)" />
				:
			</div>
			<div id="data_CONTRATACION_ADJUDICACION:RES_LICITACION"
				style="position: absolute; top: 110px; left: 250px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(CONTRATACION_ADJUDICACION:RES_LICITACION)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="1" cols="80" 
					id="SEARCH_CONTRATACION_ADJUDICACION:RES_LICITACION" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_RESULTADO_LICITACION" 
					image="img/search-mg.gif" jsDelete="deleteAdjudicado" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CONTRATACION_ADJUDICACION:RES_LICITACION" id="property(CONTRATACION_ADJUDICACION:RES_LICITACION)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
		</div>
	</ispac:tab>
</ispac:tabs>

<script language='JavaScript' type='text/javascript'>
function  deleteAdjudicado() {
	document.forms[0].elements['property(CONTRATACION_ADJUDICACION:RES_LICITACION)'].value = "";
}

function  deleteAnuncio() {
	document.forms[0].elements['property(CONTRATACION_ADJUDICACION:ENVIOANUNCIOPLACE)'].value = "";
}
</script>
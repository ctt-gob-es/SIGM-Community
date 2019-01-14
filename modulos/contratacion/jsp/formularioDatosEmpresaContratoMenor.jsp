<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c1"%>

<c1:set var="aux0">
	<bean:write name="defaultForm"
		property="entityApp.label(CONTRATACION_INF_EMPRESA:CONTRATACION_INF_EMPRESA)" />
</c1:set><jsp:useBean id="aux0" type="java.lang.String" />
<ispac:tabs>
	<ispac:tab title='<%=aux0%>'>
		<div id="dataBlock_CONTRATACION_INF_EMPRESA" style="position: relative; height: 170px; width: 600px">
		
			<div id="label_SPAC_EXPEDIENTES:SEP_INTERESADO_PRINCIPAL" style="position: absolute; top: 10px; left: 10px; width: 620px" class="textbar">
				REQUISITOS ESPECÍFICOS Y DECLARACIONES REQUERIDAS
				<hr class="formbar"/>
			</div>
			
			<div id="label_CONTRATACION_INF_EMPRESA:COD_TIP_DECLARACION"
				title="Valor codificado que describe el requisito específico para el licitador"
				style="position: absolute; top: 40px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_INF_EMPRESA:TIP_DECLARACION)" />:
			</div>
			<div id="data_CONTRATACION_INF_EMPRESA:COD_TIP_DECLARACION" style="position: absolute; top: 40px; left: 220px; width: 100%;">
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
						showFrame="true" divWidth="500" divHeight="70">
						<ispac:parameterMultivalue name="SEARCH_CONTRATACION_INF_EMPRESA_TIP_DECLARACION" id="propertyMultivalue(CONTRATACION_INF_EMPRESA:TIP_DECLARACION)" 
						setMethod="id" property="SUSTITUTO" propertyDestination="propertyMultivalue(CONTRATACION_INF_EMPRESA:TIP_DECLARACION)" />
					</ispac:htmlTextMultivalueImageFrame> 
				</nobr>
			</div>
			
			<div id="label_SPAC_EXPEDIENTES:SEP_INTERESADO_PRINCIPAL" style="position: absolute; top: 130px; left: 10px; width: 720px">
				Para acreditar el requisito es necesario seleccionar al menos uno de los siguientes: (SOLO en caso de Licitación Electrónica).
			</div>
			<div id="label_CONTRATACION_INF_EMPRESA:EVIDENCE_CAPAC_CONT"
				style="position: absolute; top: 150px; left: 10px; width: 220px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_INF_EMPRESA:EVIDENCE_CAPAC_CONT)" /> (*):
			</div>
			<div id="data_CONTRATACION_INF_EMPRESA:EVIDENCE_CAPAC_CONT" style="position: absolute; top: 150px; left: 240px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(CONTRATACION_INF_EMPRESA:EVIDENCE_CAPAC_CONT)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="1" cols="80" 
					id="SEARCH_CODICE_LICITACION_EVIDENCE_CAPAC_CONT" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_EVIDENCE" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CODICE_LICITACION_EVIDENCE_CAPAC_CONT" id="property(CONTRATACION_INF_EMPRESA:EVIDENCE_CAPAC_CONT)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
 			
		</div>
	</ispac:tab>
</ispac:tabs>






















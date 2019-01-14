<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c1"%>


<c1:set var="aux0">
	<bean:write name="defaultForm"
		property="entityApp.label(CONTRATACION_CRIT_ADJ:CONTRATACION_CRIT_ADJ)" />
</c1:set><jsp:useBean id="aux0" type="java.lang.String" />
<ispac:tabs>
	<ispac:tab title='<%=aux0%>'>
		<div id="dataBlock_CONTRATACION_CRIT_ADJ" style="position: relative; height: 100px; width: 600px">
			<div id="label_CONTRATACION_CRIT_ADJ:CRIT_ADJ"
			title="Código que tipifica el criterio de adjudicación,puede ser objetivo o subjetivo. Los criterios objetivos son los que se pueden valorar mediante la aplicación de fórmulas objetivas mientras que los
		criterios subjetivos requieren de la participación de un comité de expertos para la valoración subjetiva de las ofertas. La inclusión de criterios subjetivos en unos pliegos obliga a la creación de un comité de expertos que pueda
		realizar la valoración de las ofertas y a establecer distintos sobres que permitan realizar el análisis técnico subjetivo de las ofertas recibidas antes de abrir los sobres donde se encuentra la valoración económica y los criterios técnicos objetivos. Este código también permite indicar si el criterio
		será evaluado mediante subasta electrónica."
				style="position: absolute; top: 10px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_CRIT_ADJ:CRIT_ADJ)" />
				:
			</div>
			<div id="data_CONTRATACION_CRIT_ADJ:CRIT_ADJ"
				style="position: absolute; top: 10px; left: 200px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(CONTRATACION_CRIT_ADJ:CRIT_ADJ)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="1" cols="50" 
					id="SEARCH_CONTRATACION_CRIT_ADJ:CRIT_ADJ" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_CRITERIO_CODIGO" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CONTRATACION_CRIT_ADJ:CRIT_ADJ" id="property(CONTRATACION_CRIT_ADJ:CRIT_ADJ)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			<div id="label_CONTRATACION_CRIT_ADJ:CRIT_ADJ_SUB"
				style="position: absolute; top: 10px; left: 520px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_CRIT_ADJ:CRIT_ADJ_SUB)" />
				:
			</div>
			<div id="data_CONTRATACION_CRIT_ADJ:CRIT_ADJ_SUB"
				style="position: absolute; top: 10px; left: 600px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(CONTRATACION_CRIT_ADJ:CRIT_ADJ_SUB)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="1" cols="50" 
					id="SEARCH_CONTRATACION_CRIT_ADJ:CRIT_ADJ_SUB" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_SUB_CRITERIO_CODIGO" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CONTRATACION_CRIT_ADJ:CRIT_ADJ_SUB" id="property(CONTRATACION_CRIT_ADJ:CRIT_ADJ_SUB)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_CRIT_ADJ:DESCRIPCION"
			title="Descripción textual del criterio de adjudicación."
				style="position: absolute; top: 35px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_CRIT_ADJ:DESCRIPCION)" />
				:
			</div>
			<div id="data_CONTRATACION_CRIT_ADJ:DESCRIPCION"
				style="position: absolute; top: 35px; left: 200px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_CRIT_ADJ:DESCRIPCION)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="50" maxlength="256">
				</ispac:htmlText>
			</div>
			<div id="label_CONTRATACION_CRIT_ADJ:PONDERACION"
			title="Es la ponderación o valor asignado al cumplimiento de este criterio de adjudicación."
				style="position: absolute; top: 60px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_CRIT_ADJ:PONDERACION)" />
				:
			</div>
			<div id="data_CONTRATACION_CRIT_ADJ:PONDERACION"
				style="position: absolute; top: 60px; left: 200px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_CRIT_ADJ:PONDERACION)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="50" maxlength="500">
				</ispac:htmlText>
			</div>
			
			<div id="label_CONTRATACION_CRIT_ADJ:TEXTO_VALOR"
				style="position: absolute; top: 85px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_CRIT_ADJ:TEXTO_VALOR)" />:
			</div>
			<div id="data_CONTRATACION_CRIT_ADJ:TEXTO_VALOR"
				style="position: absolute; top: 85px; left: 200px; width: 100%;">
				<nobr> 
					<ispac:htmlTextImageFrame
						property="property(CONTRATACION_CRIT_ADJ:TEXTO_VALOR)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" size="50"
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
								id="property(CONTRATACION_CRIT_ADJ:TEXTO_VALOR)"
								property="VALOR" />
					</ispac:htmlTextImageFrame> 
				</nobr>
			</div>


		</div>
	</ispac:tab>
</ispac:tabs>
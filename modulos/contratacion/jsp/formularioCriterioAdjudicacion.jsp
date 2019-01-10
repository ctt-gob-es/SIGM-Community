<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c1"%>

<script language='JavaScript' type='text/javascript'><!--

	function save() {
		var presupuesto = document.forms[0].elements['property(CONTRATACION_CRIT_ADJ:IMP_MINIMO)'].value;
		presupuesto = presupuesto.replace(",",".");
		document.forms[0].elements['property(CONTRATACION_CRIT_ADJ:IMP_MINIMO)'].value=presupuesto;
		var impMaximo = document.forms[0].elements['property(CONTRATACION_CRIT_ADJ:IMP_MAXIMO)'].value;
		impMaximo = impMaximo.replace(",",".");
		document.forms[0].elements['property(CONTRATACION_CRIT_ADJ:IMP_MAXIMO)'].value=impMaximo;
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


<c1:set var="aux0">
	<bean:write name="defaultForm"
		property="entityApp.label(CONTRATACION_CRIT_ADJ:CONTRATACION_CRIT_ADJ)" />
</c1:set><jsp:useBean id="aux0" type="java.lang.String" />
<ispac:tabs>
	<ispac:tab title='<%=aux0%>'>
		<div id="dataBlock_CONTRATACION_CRIT_ADJ"
	
			style="position: relative; height: 310px; width: 600px">
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
				style="position: absolute; top: 10px; left: 180px; width: 100%;">
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
				style="position: absolute; top: 35px; left: 180px; width: 100%;">
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
				style="position: absolute; top: 60px; left: 180px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_CRIT_ADJ:PONDERACION)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="50" maxlength="500">
				</ispac:htmlText>
			</div>
			<div id="label_CONTRATACION_CRIT_ADJ:DESC_PONDERACION"
			title="Descripción textual de la ponderación de este criterio de adjudicación."
				style="position: absolute; top: 85px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_CRIT_ADJ:DESC_PONDERACION)" />
				:
			</div>
			<div id="data_CONTRATACION_CRIT_ADJ:DESC_PONDERACION"
				style="position: absolute; top: 85px; left: 180px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_CRIT_ADJ:DESC_PONDERACION)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="50" maxlength="256">
				</ispac:htmlText>
			</div>
			<div id="label_CONTRATACION_CRIT_ADJ:EXP_MAT"
			title="Descripción textual o expresión matemática que servirá para calcular la puntuación para este criterio en función de los valores ofertados por los distintos licitadores."
				style="position: absolute; top: 110px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_CRIT_ADJ:EXP_MAT)" />
				:
			</div>
			<div id="data_CONTRATACION_CRIT_ADJ:EXP_MAT"
				style="position: absolute; top: 110px; left: 180px; width: 100%;">
				<ispac:htmlText property="property(CONTRATACION_CRIT_ADJ:EXP_MAT)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="50" maxlength="256">
				</ispac:htmlText>
			</div>
			<div id="label_CONTRATACION_CRIT_ADJ:COD_EXP"
			title="Código de normalización, expresión matemática que servirá para calcular la puntuación para este criterio en función de los valores ofertados por los distintos licitadores. La definición de cada uno de los códigos establece cómo se calcula la puntuación de una oferta para este criterio en función de los distintos valores ofertados, y de los umbrales."
				style="position: absolute; top: 135px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_CRIT_ADJ:COD_EXP)" />
				:
			</div>
			<div id="data_CONTRATACION_CRIT_ADJ:COD_EXP"
				style="position: absolute; top: 135px; left: 180px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(CONTRATACION_CRIT_ADJ:COD_EXP)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="1" cols="50" 
					id="SEARCH_CONTRATACION_CRIT_ADJ:COD_EXP" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_EXPRESIONES_CALCULO_VAL" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CONTRATACION_CRIT_ADJ:COD_EXP" id="property(CONTRATACION_CRIT_ADJ:COD_EXP)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			<div id="label_CONTRATACION_CRIT_ADJ:CANT_MIN"
			title="Cantidad umbral mínima para este criterio de adjudicación"
				style="position: absolute; top: 160px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_CRIT_ADJ:CANT_MIN)" />
				:
			</div>
			<div id="data_CONTRATACION_CRIT_ADJ:CANT_MIN"
				style="position: absolute; top: 160px; left: 180px; width: 100%;">
				<ispac:htmlText property="property(CONTRATACION_CRIT_ADJ:CANT_MIN)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="50" maxlength="50">
				</ispac:htmlText>
			</div>
			<div id="label_CONTRATACION_CRIT_ADJ:CANT_MAX"
			title="Cantidad umbral máxima para este criterio de adjudicación"
				style="position: absolute; top: 185px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_CRIT_ADJ:CANT_MAX)" />
				:
			</div>
			<div id="data_CONTRATACION_CRIT_ADJ:CANT_MAX"
				style="position: absolute; top: 185px; left: 180px; width: 100%;">
				<ispac:htmlText property="property(CONTRATACION_CRIT_ADJ:CANT_MAX)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="50" maxlength="100">
				</ispac:htmlText>
			</div>
			<div id="label_CONTRATACION_CRIT_ADJ:IMP_MINIMO"
			title="Importe umbral mínimo para este criterio de adjudicación"
				style="position: absolute; top: 210px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_CRIT_ADJ:IMP_MINIMO)" />
				:
			</div>
			<div id="data_CONTRATACION_CRIT_ADJ:IMP_MINIMO"
				style="position: absolute; top: 210px; left: 180px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_CRIT_ADJ:IMP_MINIMO)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="50" maxlength="100">
				</ispac:htmlText>
			</div>
			<div id="label_CONTRATACION_CRIT_ADJ:IMP_MAXIMO"
			title="Importe umbral máximo para este criterio de adjudicación"
				style="position: absolute; top: 235px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_CRIT_ADJ:IMP_MAXIMO)" />
				:
			</div>
			<div id="data_CONTRATACION_CRIT_ADJ:IMP_MAXIMO"
				style="position: absolute; top: 235px; left: 180px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_CRIT_ADJ:IMP_MAXIMO)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="50" maxlength="100">
				</ispac:htmlText>
			</div>
			<div id="label_CONTRATACION_CRIT_ADJ:DESC_PUJA_MIN"
			title="Descripción textual de la puja mínima que se puede realizar para este criterio de adjudicación si se utiliza subasta electrónica en el proceso de adjudicación."
				style="position: absolute; top: 260px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_CRIT_ADJ:DESC_PUJA_MIN)" />
				:
			</div>
			<div id="data_CONTRATACION_CRIT_ADJ:DESC_PUJA_MIN"
				style="position: absolute; top: 260px; left: 180px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_CRIT_ADJ:DESC_PUJA_MIN)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="50" maxlength="256">
				</ispac:htmlText>
			</div>
		</div>
	</ispac:tab>
</ispac:tabs>
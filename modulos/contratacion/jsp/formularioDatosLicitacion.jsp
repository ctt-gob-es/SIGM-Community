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
		<div id="dataBlock_CONTRATACION_DATOS_LIC" style="position: relative; height: 780px; width: 600px">
		
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
			APLICACIÓN PRESUPUESTARIA
			<hr class="formbar"/>
			</div>
			
			<div id="label_CONTRATACION_DATOS_LIC:APLICAPRESUP"
				style="position: absolute; top: 60px; left: 10px; width: 410px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DATOS_LIC:APLICAPRESUP)" /> (aplipres-anualidad-importe):
			</div>
			
			<div id="data_CONTRATACION_DATOS_LIC:APLICAPRESUP"
				style="position: absolute; top: 60px; left: 350px; width: 100%;">
				<nobr>
					<ispac:htmlTextMultivalue
						property="propertyMultivalue(CONTRATACION_DATOS_LIC:APLICAPRESUP)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="60" divWidth="500" divHeight="60"
						maxlength="1024">
					</ispac:htmlTextMultivalue>
				</nobr>
			</div>

			
			<div id="label_CONTRATACION_DATOS_LIC:CRITERIOS" style="position: absolute; top: 140px; left: 10px; width: 620px" class="textbar">
			CRITERIOS DE ADJUDICACIÓN
			<hr class="formbar"/>
			</div>
			
			<div id="label_CONTRATACION_DATOS_LIC:COD_CONTRATO_SUMIN"
				style="position: absolute; top: 170px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DATOS_LIC:PROC_ADJUDICACION)" />:
			</div>
			<div id="data_CONTRATACION_DATOS_LIC:PROC_ADJUDICACION" style="position: absolute; top: 170px; left: 200px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(CONTRATACION_DATOS_LIC:PROC_ADJUDICACION)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="1" cols="80" 
					id="SEARCH_CODICE_PLIEGOS_CONTRATO_SUMIN" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_TIPO_ADJUDICACION"
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CODICE_PLIEGOS_CONTRATO_SUMIN" id="property(CONTRATACION_DATOS_LIC:PROC_ADJUDICACION)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_DATOS_LIC:EVA_ECO"
				style="position: absolute; top: 195px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DATOS_LIC:EVA_ECO)" />:
			</div>
			<div id="data_CONTRATACION_DATOS_LIC:EVA_ECO" style="position: absolute; top: 195px; left: 200px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(CONTRATACION_DATOS_LIC:EVA_ECO)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="1" cols="80" 
					id="SEARCH_CODICE_PLIEGOS_EVA_ECO" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_VAL_TIP_OFERTA"
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CODICE_PLIEGOS_EVA_ECO" id="property(CONTRATACION_DATOS_LIC:EVA_ECO)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_DATOS_LIC:TERMADJ"
					style="position: absolute; top: 225px; left: 10px; width: 400px"
					class="textbar1">
					CONDICIONES DE ADJUDICACIÓN
					<hr class="formbar1" />
			</div>
			
			<div id="label_CONTRATACION_DATOS_LIC:COD_ALG_CALC_POND"
			title="Código de algoritmo de ponderación, describe el algoritmo de ponderación de los criterios de adjudicación establecidos por el 
			órgano de contratación. Únicamente se utiliza en caso de adjudicación por multiplicidad de
criterios. Indica cómo se calcula la puntuación total de una oferta a partir de la puntuación obtenida en cada uno de los criterios y su ponderación."
				style="position: absolute; top: 255px; left: 10px; width: 230px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DATOS_LIC:ALG_CALC_POND)" />:
			</div>
			<div id="data_CONTRATACION_DATOS_LIC:COD_ALG_CALC_POND" style="position: absolute; top: 255px; left: 250px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(CONTRATACION_DATOS_LIC:ALG_CALC_POND)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="1" cols="80" 
					id="SEARCH_CODICE_LICITACION_ALG_CALC_POND" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_ALG_CALC_POND" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CODICE_LICITACION_ALG_CALC_POND" id="property(CONTRATACION_DATOS_LIC:ALG_CALC_POND)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_DATOS_LIC:DESCRIPCION"
				title="Descripción textual de las condiciones de adjudicación."
				style="position: absolute; top: 280px; left: 10px; width: 230px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DATOS_LIC:DESCRIPCION)" />:
			</div>
			<div id="data_CONTRATACION_DATOS_LIC:DESCRIPCION"
				style="position: absolute; top: 280px; left: 250px;">
				<ispac:htmlText property="property(CONTRATACION_DATOS_LIC:APLICPRES)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="81" maxlength="256"
					tabindex="116">
				</ispac:htmlText>
			</div>
			
			<div id="label_CONTRATACION_DATOS_LIC:DESCCOMITE"
				title="Descripción textual del comité de expertos que realizará la evaluación técnica y de los criterios evaluables mediante un juicio de  valor (subjetivos) de las ofertas recibidas. Únicamente es necesario describir el comité técnico en el caso que el contrato se adjudique 
				por multiplicidad de criterios y 
				además existan criterios subjetivos en la lista de criterios de adjudicación."
				style="position: absolute; top: 305px; left: 10px; width: 230px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DATOS_LIC:DESCCOMITE)" />:
			</div>
			<div id="data_CONTRATACION_DATOS_LIC:DESCCOMITE"
				style="position: absolute; top: 305px; left: 250px;">
				<ispac:htmlText property="property(CONTRATACION_DATOS_LIC:DESCCOMITE)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="81" maxlength="256"
					tabindex="116">
				</ispac:htmlText>
			</div>
			
			<div id="label_CONTRATACION_DATOS_LIC:DESCRIPCION_BAJA"
				title="Descripción textual del sistema que establecerá el órgano de contratación para delimitar las ofertas que se considere que incurren en baja temeraria."
				style="position: absolute; top: 330px; left: 10px; width: 230px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DATOS_LIC:DESCRIPCION_BAJA)" />:
			</div>
			<div id="data_CONTRATACION_DATOS_LIC:DESCRIPCION_BAJA"
				style="position: absolute; top: 330px; left: 250px;">
				<ispac:htmlText property="property(CONTRATACION_DATOS_LIC:DESCRIPCION_BAJA)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="81" maxlength="256"
					tabindex="116">
				</ispac:htmlText>
			</div>

			<div id="label_CONTRATACION_DATOS_LIC:COMITEEXPERTOS"
				title="Describir a las personas del Comité de Expertos, necesarios encargados de evaluar los criterios de adjudicación susceptibles de un juicio de valor."
				style="position: absolute; top: 355px; left: 10px; width: 230px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DATOS_LIC:COMITEEXPERTOS)" />
				:
			</div>
			<div id="data_CONTRATACION_DATOS_LIC:COMITEEXPERTOS"

				style="position: absolute; top: 355px; left: 250px; width: 100%;">
				<nobr>
					<ispac:htmlTextMultivalue
						property="propertyMultivalue(CONTRATACION_DATOS_LIC:COMITEEXPERTOS)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="60" divWidth="500" divHeight="60"
						maxlength="1024">
					</ispac:htmlTextMultivalue>
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_DATOS_LIC:VARIANTES" style="position: absolute; top: 435px; left: 10px; width: 620px" class="textbar">
			VARIANTES
			<hr class="formbar"/>
			</div>

			<div id="label_CONTRATACION_DATOS_LIC:VARIANTEOFERTA"
				title="Es una propiedad lógica (verdadero o falso) que sirve para indicar que se aceptan variantes a las ofertas presentadas por parte de los operadores económicos."
				style="position: absolute; top: 465px; left: 10px; width: 230px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DATOS_LIC:VARIANTEOFERTA)" />
				:
			</div>
			<div id="data_CONTRATACION_DATOS_LIC:VARIANTEOFERTA"
				style="position: absolute; top: 465px; left: 250px; width: 100%;">
				<nobr>
					<ispac:htmlTextImageFrame
						property="property(CONTRATACION_DATOS_LIC:VARIANTEOFERTA)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" size="80"
						id="SEARCH_CONTRATACION_DATOS_LIC_VARIANTEOFERTA"
						target="workframe" action="selectValue.do?entity=SPAC_TBL_009"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
						<ispac:parameter
							name="SEARCH_CONTRATACION_DATOS_LIC_VARIANTEOFERTA"
							id="property(CONTRATACION_DATOS_LIC:VARIANTEOFERTA)"
							property="VALOR" />
					</ispac:htmlTextImageFrame>
				</nobr>
			</div>
			<div id="label_CONTRATACION_DATOS_LIC:NUM_VAR"
			title="Sirve para indicar el número máximo de variantes permitido en las ofertas presentadas por un licitador."
				style="position: absolute; top:490px; left: 10px; width: 230px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DATOS_LIC:NUM_VAR)" />
				:
			</div>
			<div id="data_CONTRATACION_DATOS_LIC:NUM_VAR"
				style="position: absolute; top: 490px; left: 250px; width: 100%;">
				<ispac:htmlText property="property(CONTRATACION_DATOS_LIC:NUM_VAR)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="50">
				</ispac:htmlText>
			</div>
			<div id="label_CONTRATACION_DATOS_LIC:ELEM_ACEP"
			title="Describe sobre qué elementos se permitirá que el operador económico presente variantes a sus ofertas."
				style="position: absolute; top: 515px; left: 10px; width: 230px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DATOS_LIC:ELEM_ACEP)" />
				:
			</div>
			<div id="data_CONTRATACION_DATOS_LIC:ELEM_ACEP"
				style="position: absolute; top: 515px; left: 250px; width: 100%;">
				<ispac:htmlTextMultivalue
					property="propertyMultivalue(CONTRATACION_DATOS_LIC:ELEM_ACEP)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" divWidth="480" divHeight="60"
					maxlength="1024">
				</ispac:htmlTextMultivalue>
			</div>
			
			<div id="label_CONTRATACION_DATOS_LIC:PROGRAMA_FINANCIACION" style="position: absolute; top: 600px; left: 10px; width: 620px" class="textbar">
			PROGRAMA FINANCIACIÓN
			<hr class="formbar"/>
			</div>
			
			<div id="label_CONTRATACION_DATOS_LIC:PROGRAMA_FINANCIACION"
				style="position: absolute; top: 630px; left: 10px; width: 575px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DATOS_LIC:PROGRAMA_FINANCIACION)" />:
			</div>
			<div id="data_CONTRATACION_DATOS_LIC:PROGRAMA_FINANCIACION" style="position: absolute; top: 630px; left: 240px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(CONTRATACION_DATOS_LIC:PROGRAMA_FINANCIACION)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="1" cols="80" 
					id="SEARCH_CODICE_PROGRAMA_FINANCIACION" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_PROGRAMA_FINANCIACION" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CODICE_PROGRAMA_FINANCIACION" id="property(CONTRATACION_DATOS_LIC:PROGRAMA_FINANCIACION)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_DATOS_LIC:PROGRAMA"
				style="position: absolute; top:655px; left: 10px; width: 230px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DATOS_LIC:PROGRAMA)" />
				:
			</div>
			
			<div id="data_CONTRATACION_DATOS_LIC:PROGRAMA"
				style="position: absolute; top: 655px; left: 240px; width: 100%;">
				<ispac:htmlTextarea
					property="property(CONTRATACION_DATOS_LIC:PROGRAMA)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" rows="4" cols="80">
				</ispac:htmlTextarea>
			</div>
			
			
			<div id="label_CONTRATACION_DATOS_LIC:REVISION_PRECIOS" style="position: absolute; top: 700px; left: 10px; width: 620px" class="textbar">
			REVISIÓN PRECIOS
			<hr class="formbar"/>
			</div>
			
			<div id="label_CONTRATACION_DATOS_LIC:REV_PRECIOS"
				style="position: absolute; top:735px; left: 10px; width: 230px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DATOS_LIC:REV_PRECIOS)" />
				:
			</div>
			<div id="data_CONTRATACION_DATOS_LIC:REV_PRECIOS"
				style="position: absolute; top: 735px; left: 240px; width: 100%;">
				<ispac:htmlText property="property(CONTRATACION_DATOS_LIC:REV_PRECIOS)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="255">
				</ispac:htmlText>
			</div>

		</div>
	</ispac:tab>
</ispac:tabs>
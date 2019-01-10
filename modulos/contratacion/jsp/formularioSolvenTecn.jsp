<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c1"%>

<script language='JavaScript' type='text/javascript'><!--

	function save() {
		var presupuesto = document.forms[0].elements['property(CONTRATACION_SOLVENCIA_TECN:VALORUMBRALIMPORTE)'].value;
		presupuesto = presupuesto.replace(",",".");
		document.forms[0].elements['property(CONTRATACION_SOLVENCIA_TECN:VALORUMBRALIMPORTE)'].value=presupuesto;
		
		var impMaximo = document.forms[0].elements['property(CONTRATACION_SOLVENCIA_TECN:VALORUMBRALNOIMPORTE)'].value;
		impMaximo = impMaximo.replace(",",".");
		document.forms[0].elements['property(CONTRATACION_SOLVENCIA_TECN:VALORUMBRALNOIMPORTE)'].value=impMaximo;
		
		var valorUmbralNoImporteEco = document.forms[0].elements['property(CONTRATACION_SOLVENCIA_TECN:VALORUMBRALNOIMPORTE_ECO)'].value;
		valorUmbralNoImporteEco = valorUmbralNoImporteEco.replace(",",".");
		document.forms[0].elements['property(CONTRATACION_SOLVENCIA_TECN:VALORUMBRALNOIMPORTE_ECO)'].value=valorUmbralNoImporteEco;
		
		var valorUmbralImporteEco = document.forms[0].elements['property(CONTRATACION_SOLVENCIA_TECN:VALORUMBRALIMPORTE_ECO)'].value;
		valorUmbralImporteEco = valorUmbralImporteEco.replace(",",".");
		document.forms[0].elements['property(CONTRATACION_SOLVENCIA_TECN:VALORUMBRALIMPORTE_ECO)'].value=valorUmbralImporteEco;
		
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
		property="entityApp.label(CONTRATACION_SOLVENCIA_TECN:CONTRATACION_SOLVENCIA_TECN)" />
</c1:set><jsp:useBean id="aux0" type="java.lang.String" />
<ispac:tabs>
	<ispac:tab title='<%=aux0%>'>
		<div id="dataBlock_CONTRATACION_SOLVENCIA_TECN"
			style="position: relative; height: 550px; width: 600px">

			<div id="label_SPAC_EXPEDIENTES:SEP_INTERESADO_PRINCIPAL"
				title="Criterios para evaluar la solvencia técnica que determina la capacidad de un operador económico para participar en la licitación."
				style="position: absolute; top: 10px; left: 10px; width: 620px"
				class="textbar">
				CRITERIOS DE SOLVENCIA TÉCNICA
				<hr class="formbar" />
			</div>

			<div id="label_CONTRATACION_SOLVENCIA_TECN:CRIT_SOLVENCIA"
				title="Código que tipifica el criterio de solvencia. En este caso se trata de un criterio de solvencia técnico-profesional"
				style="position: absolute; top: 40px; left: 10px; width: 160px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_SOLVENCIA_TECN:CRIT_SOLVENCIA)" />
				:
			</div>
			<div id="data_CONTRATACION_SOLVENCIA_TECN:CRIT_SOLVENCIA"
				style="position: absolute; top: 40px; left: 170px; width: 100%;">

				<nobr>
					<ispac:htmlTextareaImageFrame
						property="property(CONTRATACION_SOLVENCIA_TECN:CRIT_SOLVENCIA)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" rows="1"
						cols="80" id="SEARCH_CONTRATACION_SOLVENCIA_TECN_CRIT_SOLVENCIA"
						target="workframe"
						action="selectListadoCodicePliego.do?atributo=COD_TIP_CAP_TECN"
						image="img/search-mg.gif" titleKeyLink="select.rol"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480" tabindex="113">
						<ispac:parameter
							name="SEARCH_CONTRATACION_SOLVENCIA_TECN_CRIT_SOLVENCIA"
							id="property(CONTRATACION_SOLVENCIA_TECN:CRIT_SOLVENCIA)"
							property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>

			</div>
			<div id="label_CONTRATACION_SOLVENCIA_TECN:DESCRIPCION"
				title="Descripción textual del criterio de solvencia técnica."
				style="position: absolute; top: 65px; left: 10px; width: 160px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_SOLVENCIA_TECN:DESCRIPCION)" />
				:
			</div>
			<div id="data_CONTRATACION_SOLVENCIA_TECN:DESCRIPCION"
				style="position: absolute; top: 65px; left: 170px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_SOLVENCIA_TECN:DESCRIPCION)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="256">
				</ispac:htmlText>
			</div>
			<div id="label_CONTRATACION_SOLVENCIA_TECN:VALORUMBRALIMPORTE"
				title="Valor umbral del criterio cuando se trata de un importe, como por ejemplo el valor del volumen de negocios."
				style="position: absolute; top: 90px; left: 10px; width: 160px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_SOLVENCIA_TECN:VALORUMBRALIMPORTE)" />
				:
			</div>
			<div id="data_CONTRATACION_SOLVENCIA_TECN:VALORUMBRALIMPORTE"
				style="position: absolute; top: 90px; left: 170px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_SOLVENCIA_TECN:VALORUMBRALIMPORTE)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="50">
				</ispac:htmlText>
			</div>
			<div id="label_CONTRATACION_SOLVENCIA_TECN:VALORUMBRALNOIMPORTE"
				title="Valor umbral del criterio cuando no es un importe, como por ejemplo el número de ingenieros superiores dedicados al proyecto."
				style="position: absolute; top: 115px; left: 10px; width: 160px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_SOLVENCIA_TECN:VALORUMBRALNOIMPORTE)" />
				:
			</div>
			<div id="data_CONTRATACION_SOLVENCIA_TECN:VALORUMBRALNOIMPORTE"
				style="position: absolute; top: 115px; left: 170px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_SOLVENCIA_TECN:VALORUMBRALNOIMPORTE)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="50">
				</ispac:htmlText>
			</div>

			<div id="label_CONTRATACION_SOLVENCIA_TECN:PERIODODURACION"
				title="Período o lapso de tiempo de aplicabilidad del criterio"
				style="position: absolute; top: 140px; left: 10px; width: 160px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_SOLVENCIA_TECN:PERIODODURACION)" />
				:
			</div>
			<div id="data_CONTRATACION_SOLVENCIA_TECN:PERIODODURACION"
				style="position: absolute; top: 140px; left: 170px; width: 100%;">
				<nobr>
					<ispac:htmlTextCalendar
						property="property(CONTRATACION_SOLVENCIA_TECN:PERIODODURACION)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%=buttoncalendar%>' format="dd/mm/yyyy"
						enablePast="true">
					</ispac:htmlTextCalendar>
				</nobr>
			</div>

			<div id="label_CONTRATACION_SOLVENCIA_TECN:EXPMAT"
				title="Código que define la expresión matemática que servirá para evaluar el criterio de evaluación. Hace referencia a como se tratará el valor umbral, si es máximo o mínimo."
				style="position: absolute; top: 165px; left: 10px; width: 160px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_SOLVENCIA_TECN:EXPMAT)" />
				:
			</div>

			<div id="data_CONTRATACION_SOLVENCIA_TECN:EXPMAT"
				style="position: absolute; top: 165px; left: 170px; width: 100%;">

				<nobr>
					<ispac:htmlTextareaImageFrame
						property="property(CONTRATACION_SOLVENCIA_TECN:EXPMAT)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" rows="1"
						cols="80" id="SEARCH_CONTRATACION_SOLVENCIA_TECN_EXPMAT"
						target="workframe"
						action="selectListadoCodicePliego.do?atributo=COD_EXP"
						image="img/search-mg.gif" titleKeyLink="select.rol"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480" tabindex="113">
						<ispac:parameter name="SEARCH_CONTRATACION_SOLVENCIA_TECN_EXPMAT"
							id="property(CONTRATACION_SOLVENCIA_TECN:EXPMAT)"
							property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			
			
			 
  			<div id="label_SPAC_EXPEDIENTES:SEP_INTERESADO_PRINCIPAL" style="position: absolute; top: 205px; left: 10px; width: 720px">
				Para acreditar el requisito es necesario seleccionar al menos uno de los siguientes: (SOLO en caso de Licitación Electrónica).
			</div>
			
			<div id="label_CONTRATACION_SOLVENCIA_TECN:EVIDENCE_CRIT_SOLVENCIA"
				style="position: absolute; top: 230px; left: 10px; width: 220px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_SOLVENCIA_TECN:EVIDENCE_CRIT_SOLVENCIA)" /> (*):
			</div>
			<div id="data_CONTRATACION_SOLVENCIA_TECN:EVIDENCE_CRIT_SOLVENCIA" style="position: absolute; top: 230px; left: 240px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(CONTRATACION_SOLVENCIA_TECN:EVIDENCE_CRIT_SOLVENCIA)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="1" cols="80" 
					id="SEARCH_CODICE_LICITACION_EVIDENCE_CRIT_SOLVENCIA" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_EVIDENCE" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CODICE_LICITACION_EVIDENCE_CRIT_SOLVENCIA" id="property(CONTRATACION_SOLVENCIA_TECN:EVIDENCE_CRIT_SOLVENCIA)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			
			
		<div id="label_SPAC_EXPEDIENTES:SEP_INTERESADO_PRINCIPAL" 
			title="Criterios económico-financieros requeridos a un operador económico para evaluar su solvencia 
			económica y determinar su capacidad de participar en la licitación." 
			style="position: absolute; top: 290px; left: 10px; width: 620px" class="textbar">
				CRITERIOS DE SOLVENCIA ECONÓMICA Y FINANCIERA
				<hr class="formbar"/>
			</div>
			
			<div id="label_CONTRATACION_SOLVENCIA_TECN:CRIT_SOLVENCIA_ECO"
				title="Código que tipifica el criterio de solvencia. En este caso se trata de un criterio de solvencia económico-financiera."
				style="position: absolute; top: 325px; left: 10px; width: 160px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_SOLVENCIA_TECN:CRIT_SOLVENCIA_ECO)" />
				:
			</div>
			<div id="data_CONTRATACION_SOLVENCIA_TECN:CRIT_SOLVENCIA_ECO"
				style="position: absolute; top: 325px; left: 170px; width: 100%;">

				<nobr>
					<ispac:htmlTextareaImageFrame
						property="property(CONTRATACION_SOLVENCIA_TECN:CRIT_SOLVENCIA_ECO)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" rows="1"
						cols="80" id="SEARCH_CONTRATACION_SOLVENCIA_TECN_CRIT_SOLVENCIA_ECO"
						target="workframe"
						action="selectListadoCodicePliego.do?atributo=COD_CAP_FINAN"
						image="img/search-mg.gif" titleKeyLink="select.rol"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480" tabindex="113">
						<ispac:parameter
							name="SEARCH_CONTRATACION_SOLVENCIA_TECN_CRIT_SOLVENCIA_ECO"
							id="property(CONTRATACION_SOLVENCIA_TECN:CRIT_SOLVENCIA_ECO)"
							property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>

			</div>
			<div id="label_CONTRATACION_SOLVENCIA_TECN:DESCRIPCION_ECO"
				title="Descripción textual del criterio de solvencia técnica."
				style="position: absolute; top: 350px; left: 10px; width: 160px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_SOLVENCIA_TECN:DESCRIPCION_ECO)" />
				:
			</div>
			<div id="data_CONTRATACION_SOLVENCIA_TECN:DESCRIPCIO_ECON"
				style="position: absolute; top: 350px; left: 170px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_SOLVENCIA_TECN:DESCRIPCION_ECO)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="256">
				</ispac:htmlText>
			</div>
			<div id="label_CONTRATACION_SOLVENCIA_TECN:VALORUMBRALIMPORTE_ECO"
				title="Valor umbral del criterio cuando se trata de un importe, como por ejemplo el valor del volumen de negocios."
				style="position: absolute; top: 375px; left: 10px; width: 160px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_SOLVENCIA_TECN:VALORUMBRALIMPORTE_ECO)" />
				:
			</div>
			<div id="data_CONTRATACION_SOLVENCIA_TECN:VALORUMBRALIMPORTE_ECO"
				style="position: absolute; top: 375px; left: 170px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_SOLVENCIA_TECN:VALORUMBRALIMPORTE_ECO)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="50">
				</ispac:htmlText>
			</div>
			<div id="label_CONTRATACION_SOLVENCIA_TECN:VALORUMBRALNOIMPORTE_ECO"
				title="Valor umbral del criterio cuando no es un importe, como por ejemplo el número de ingenieros superiores dedicados al proyecto."
				style="position: absolute; top: 400px; left: 10px; width: 160px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_SOLVENCIA_TECN:VALORUMBRALNOIMPORTE_ECO)" />
				:
			</div>
			<div id="data_CONTRATACION_SOLVENCIA_TECN:VALORUMBRALNOIMPORTE_ECO"
				style="position: absolute; top: 400px; left: 170px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_SOLVENCIA_TECN:VALORUMBRALNOIMPORTE_ECO)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="50">
				</ispac:htmlText>
			</div>

			<div id="label_CONTRATACION_SOLVENCIA_TECN:PERIODODURACION_ECO"
				title="Período o lapso de tiempo de aplicabilidad del criterio"
				style="position: absolute; top: 425px; left: 10px; width: 160px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_SOLVENCIA_TECN:PERIODODURACION_ECO)" />
				:
			</div>
			<div id="data_CONTRATACION_SOLVENCIA_TECN:PERIODODURACION_ECO"
				style="position: absolute; top: 425px; left: 170px; width: 100%;">
				<nobr>
					<ispac:htmlTextCalendar
						property="property(CONTRATACION_SOLVENCIA_TECN:PERIODODURACION_ECO)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%=buttoncalendar%>' format="dd/mm/yyyy"
						enablePast="true">
					</ispac:htmlTextCalendar>
				</nobr>
			</div>

			<div id="label_CONTRATACION_SOLVENCIA_TECN:EXPMAT_ECO"
				title="Código que define la expresión matemática que servirá para evaluar el criterio de evaluación. Hace referencia a como se tratará el valor umbral, si es máximo o mínimo."
				style="position: absolute; top: 450px; left: 10px; width: 160px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_SOLVENCIA_TECN:EXPMAT_ECO)" />
				:
			</div>

			<div id="data_CONTRATACION_SOLVENCIA_TECN:EXPMAT_ECO"
				style="position: absolute; top: 450px; left: 170px; width: 100%;">

				<nobr>
					<ispac:htmlTextareaImageFrame
						property="property(CONTRATACION_SOLVENCIA_TECN:EXPMAT_ECO)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" rows="1"
						cols="80" id="SEARCH_CONTRATACION_SOLVENCIA_TECN_EXPMAT_ECO"
						target="workframe"
						action="selectListadoCodicePliego.do?atributo=COD_EXP"
						image="img/search-mg.gif" titleKeyLink="select.rol"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480" tabindex="113">
						<ispac:parameter name="SEARCH_CONTRATACION_SOLVENCIA_TECN_EXPMAT_ECO"
							id="property(CONTRATACION_SOLVENCIA_TECN:EXPMAT_ECO)"
							property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>			
			 
  			<div id="label_SPAC_EXPEDIENTES:SEP_INTERESADO_PRINCIPAL" style="position: absolute; top: 490px; left: 10px; width: 720px">
				Para acreditar el requisito es necesario seleccionar al menos uno de los siguientes: (SOLO en caso de Licitación Electrónica).
			</div>
			
			<div id="label_CONTRATACION_SOLVENCIA_TECN:EVIDENCE_CRIT_SOLVENCIA_ECO"
				style="position: absolute; top: 515px; left: 10px; width: 220px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_SOLVENCIA_TECN:EVIDENCE_CRIT_SOLVENCIA_ECO)" /> (*):
			</div>
			<div id="data_CONTRATACION_SOLVENCIA_TECN:EVIDENCE_CRIT_SOLVENCIA_ECO" style="position: absolute; top: 515px; left: 240px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(CONTRATACION_SOLVENCIA_TECN:EVIDENCE_CRIT_SOLVENCIA_ECO)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="1" cols="80" 
					id="SEARCH_CODICE_LICITACION_EVIDENCE_CRIT_SOLVENCIA_ECO" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_EVIDENCE" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CODICE_LICITACION_EVIDENCE_CRIT_SOLVENCIA_ECO" id="property(CONTRATACION_SOLVENCIA_TECN:EVIDENCE_CRIT_SOLVENCIA_ECO)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>			

		</div>
	</ispac:tab>
</ispac:tabs>
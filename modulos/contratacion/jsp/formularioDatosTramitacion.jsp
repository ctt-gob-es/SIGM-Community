<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c1"%>


<script language='JavaScript' type='text/javascript'><!--

	/* function save() {
		var presupuesto = document.forms[0].elements['property(CONTRATACION_DATOS_TRAMIT:IMP_ADJ_SINIVA)'].value;
		presupuesto = presupuesto.replace(",",".");
		document.forms[0].elements['property(CONTRATACION_DATOS_TRAMIT:IMP_ADJ_SINIVA)'].value=presupuesto;
		
		var presupuesto2 = document.forms[0].elements['property(CONTRATACION_DATOS_TRAMIT:IMP_ADJ_CONIVA)'].value;
		presupuesto2 = presupuesto2.replace(",",".");
		document.forms[0].elements['property(CONTRATACION_DATOS_TRAMIT:IMP_ADJ_CONIVA)'].value=presupuesto2;
		
		var presupuesto3 = document.forms[0].elements['property(CONTRATACION_DATOS_TRAMIT:IMP_OFERTA_BAJA)'].value;
		presupuesto3 = presupuesto3.replace(",",".");
		document.forms[0].elements['property(CONTRATACION_DATOS_TRAMIT:IMP_OFERTA_BAJA)'].value=presupuesto3;
		
		var presupuesto4 = document.forms[0].elements['property(CONTRATACION_DATOS_TRAMIT:IMP_OFERTA_ALTA)'].value;
		presupuesto4 = presupuesto4.replace(",",".");
		document.forms[0].elements['property(CONTRATACION_DATOS_TRAMIT:IMP_OFERTA_ALTA)'].value=presupuesto4;

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
	} */

//--></script>

<c1:set var="aux0">
	<bean:write name="defaultForm"
		property="entityApp.label(CONTRATACION_DATOS_TRAMIT:CONTRATACION_DATOS_TRAMIT)" />
</c1:set><jsp:useBean id="aux0" type="java.lang.String" />
<ispac:tabs>
	<ispac:tab title='<%=aux0%>'>
		<div id="dataBlock_CONTRATACION_DATOS_TRAMIT" style="position: relative; height: 900px; width: 600px">
		
		<div id="label_CONTRATACION_DATOS_TRAMIT:F_APROBACION_PROYECTO"
				style="position: absolute; top: 10px; left: 10px; width: 410px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DATOS_TRAMIT:F_APROBACION_PROYECTO)" />:
			</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:FECHA_APROBACION_PROYECTO"
				style="position: absolute; top: 10px; left: 380px; width: 100%;">
				<nobr> 
					<ispac:htmlTextCalendar
						property="property(CONTRATACION_DATOS_TRAMIT:F_APROBACION_PROYECTO)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%= buttoncalendar %>' format="dd/mm/yyyy" enablePast="true">
					</ispac:htmlTextCalendar> 
				</nobr>
			</div>
		
			<div id="label_CONTRATACION_DATOS_TRAMIT:F_APRO_EXP_CONT"
				style="position: absolute; top: 35px; left: 10px; width: 410px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_TRAMIT:F_APRO_EXP_CONT)" />:</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:F_APRO_EXP_CONT"
				style="position: absolute; top: 35px; left: 380px; width: 100%;">
				<nobr> 
					<ispac:htmlTextCalendar
						property="property(CONTRATACION_DATOS_TRAMIT:F_APRO_EXP_CONT)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%= buttoncalendar %>' format="dd/mm/yyyy" enablePast="true">
					</ispac:htmlTextCalendar> 
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:F_PUB_BOP_EXP_CONT"
				style="position: absolute; top: 60px; left: 10px; width: 410px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_TRAMIT:F_PUB_BOP_EXP_CONT)" />:</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:F_PUB_BOP_EXP_CONT"
				style="position: absolute; top: 60px; left: 380px; width: 100%;">
				<nobr> 
					<ispac:htmlTextCalendar
						property="property(CONTRATACION_DATOS_TRAMIT:F_PUB_BOP_EXP_CONT)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%= buttoncalendar %>' format="dd/mm/yyyy" enablePast="true">
					</ispac:htmlTextCalendar> 
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:PERIODO_CONTRATO"
				style="position: absolute; top: 60px; left: 560px; width: 150px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DATOS_TRAMIT:PERIODO_CONTRATO)" />
				(meses):
			</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:PERIODOCONTRATO_CONTRATO"
				style="position: absolute; top: 60px; left: 680px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DATOS_TRAMIT:PERIODO_CONTRATO)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="5" maxlength="50">
				</ispac:htmlText>
			</div>
			
			<div id="data_CONTRATACION_DATOS_TRAMIT:PERIODUNITCODE_CONTRATO"
				style="position: absolute; top: 57px; left: 730px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(CONTRATACION_DATOS_TRAMIT:PERIODUNITCODE_CONTRATO)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="1" cols="7" 
					id="SEARCH_CONTRATACION_DATOS_TRAMIT:PERIODUNITCOD_CONTRATO" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_PERIODUNITCODE" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CONTRATACION_DATOS_TRAMIT:PERIODUNITCOD_CONTRATO" id="property(CONTRATACION_DATOS_TRAMIT:PERIODUNITCODE_CONTRATO)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>

			
			<div id="label_CONTRATACION_DATOS_TRAMIT:F_APERT_PROPOS"
				style="position: absolute; top: 85px; left: 10px; width: 410px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_TRAMIT:F_APERT_PROPOS)" />(*):</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:F_APERT_PROPOS"
				style="position: absolute; top: 85px; left: 380px; width: 100%;">
				<nobr> 
					<ispac:htmlTextCalendar
						property="property(CONTRATACION_DATOS_TRAMIT:F_APERT_PROPOS)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%= buttoncalendar %>' format="dd/mm/yyyy" enablePast="true">
					</ispac:htmlTextCalendar> 
				</nobr>
			</div>
			
			<!-- -------------------------------------------------------------------------------------------- -->
			<div id="label_CONTRATACION_DATOS_TRAMIT:PRESENTACION_OFERTAS"
				style="position: absolute; top: 140px; left: 10px; width: 620px"
				class="textbar">
				PERIODO DE PRESENTACIÓN DE OFERTAS
			</div>

			<div id="label_CONTRATACION_DATOS_TRAMIT:F_INICIO_PRES_PROP"
				style="position: absolute; top: 160px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DATOS_TRAMIT:F_INICIO_PRES_PROP)" />
				:
			</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:F_INICIO_PRES_PROP"
				style="position: absolute; top: 160px; left: 110px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DATOS_TRAMIT:F_INICIO_PRES_PROP)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="40">
				</ispac:htmlText>
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:F_FIN_PRES_PROP"
				style="position: absolute; top: 160px; left: 350px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DATOS_TRAMIT:F_FIN_PRES_PROP)" />
				:
			</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:F_FIN_PRES_PROP"
				style="position: absolute; top: 160px; left: 420px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DATOS_TRAMIT:F_FIN_PRES_PROP)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="40">
				</ispac:htmlText>
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:PERIODO"
				style="position: absolute; top: 160px; left: 650px; width: 150px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DATOS_TRAMIT:PERIODO)" />
				:
			</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:PERIODO"
				style="position: absolute; top: 160px; left: 720px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DATOS_TRAMIT:PERIODO)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="5" maxlength="50">
				</ispac:htmlText>
			</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:PERIODUNITCODE"
				style="position: absolute; top: 157px; left: 770px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(CONTRATACION_DATOS_TRAMIT:PERIODUNITCODE)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="1" cols="7" 
					id="SEARCH_CONTRATACION_DATOS_TRAMIT:PERIODUNITCODE" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_PERIODUNITCODE" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CONTRATACION_DATOS_TRAMIT:PERIODUNITCODE" id="property(CONTRATACION_DATOS_TRAMIT:PERIODUNITCODE)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			
			<!--
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:F_TERM_PAZO_PRESEN_PROP"
				style="position: absolute; top: 110px; left: 10px; width: 410px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_TRAMIT:F_TERM_PAZO_PRESEN_PROP)" />(*):</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:F_TERM_PAZO_PRESEN_PROP"
				style="position: absolute; top: 110px; left: 380px; width: 100%;">
				<nobr> 
					<ispac:htmlTextCalendar
						property="property(CONTRATACION_DATOS_TRAMIT:F_TERM_PAZO_PRESEN_PROP)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%= buttoncalendar %>' format="dd/mm/yyyy" enablePast="true">
					</ispac:htmlTextCalendar> 
				</nobr>
			</div>
			
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:F_APERT_PROPOS"
				style="position: absolute; top: 135px; left: 10px; width: 410px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_TRAMIT:F_APERT_PROPOS)" />(*):</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:F_APERT_PROPOS"
				style="position: absolute; top: 135px; left: 380px; width: 100%;">
				<nobr> 
					<ispac:htmlTextCalendar
						property="property(CONTRATACION_DATOS_TRAMIT:F_APERT_PROPOS)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%= buttoncalendar %>' format="dd/mm/yyyy" enablePast="true">
					</ispac:htmlTextCalendar> 
				</nobr>
			</div>
			<!-- -------------------------------------------------------------------------------------------- -->
			<div id="label_SPAC_EXPEDIENTES:SEP_INTERESADO_PRINCIPAL"
				style="position: absolute; top: 210px; left: 10px; width: 620px"
				class="textbar">
				ADJUDICACIÓN
				<hr class="formbar" />
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:NIF_ADJUDICATARIA"
				style="position: absolute; top: 240px; left: 10px; width: 270px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DATOS_TRAMIT:NIF_ADJUDICATARIA)" />
				:
			</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:NIF_ADJUDICATARIA"
				style="position: absolute; top: 240px; left: 240px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DATOS_TRAMIT:NIF_ADJUDICATARIA)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="40" maxlength="10">
				</ispac:htmlText>
			</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:TIPOIDENTIFICADOR"
				style="position: absolute; top: 240px; left: 460px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(CONTRATACION_DATOS_TRAMIT:TIPOIDENTIFICADOR)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="1" cols="10" 
					id="SEARCH_CONTRATACION_DATOS_TRAMIT:TIPOIDENTIFICADOR" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_TIPO_IDENT" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CONTRATACION_DATOS_TRAMIT:TIPOIDENTIFICADOR" id="property(CONTRATACION_DATOS_TRAMIT:TIPOIDENTIFICADOR)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:EMP_ADJ_CONT"
				style="position: absolute; top: 265px; left: 10px; width: 410px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_TRAMIT:EMP_ADJ_CONT)" />:</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:EMP_ADJ_CONT"
				style="position: absolute; top: 265px; left: 240px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DATOS_TRAMIT:EMP_ADJ_CONT)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="300">
				</ispac:htmlText>
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:REPRE_ADJUDICATARIA"
				style="position: absolute; top: 290px; left: 10px; width: 410px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_TRAMIT:REPRE_ADJUDICATARIA)" />:</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:REPRE_ADJUDICATARIA"
				style="position: absolute; top: 290px; left: 240px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DATOS_TRAMIT:REPRE_ADJUDICATARIA)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="300">
				</ispac:htmlText>
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:FECHA_ADJUDICACION"
				style="position: absolute; top: 315px; left: 10px; width: 170px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DATOS_TRAMIT:FECHA_ADJUDICACION)" />
				:
			</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:FECHA_ADJUDICACION"
				style="position: absolute; top: 315px; left: 240px; width: 100%;">
				<nobr>
					<ispac:htmlTextCalendar
						property="property(CONTRATACION_DATOS_TRAMIT:FECHA_ADJUDICACION)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%=buttoncalendar%>' format="dd/mm/yyyy"
						enablePast="true">
					</ispac:htmlTextCalendar>
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:FECHA_FIN_FORMALIZACION"
				style="position: absolute; top: 340px; left: 10px; width: 170px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DATOS_TRAMIT:FECHA_FIN_FORMALIZACION)" />
				:
			</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:FECHA_FIN_FORMALIZACION"
				style="position: absolute; top: 340px; left: 240px; width: 100%;">
				<nobr>
					<ispac:htmlTextCalendar
						property="property(CONTRATACION_DATOS_TRAMIT:FECHA_FIN_FORMALIZACION)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%=buttoncalendar%>' format="dd/mm/yyyy"
						enablePast="true">
					</ispac:htmlTextCalendar>
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:IMP_ADJ_SINIVA"
				style="position: absolute; top: 365px; left: 10px; width: 410px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_TRAMIT:IMP_ADJ_SINIVA)" />:</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:IMP_ADJ_SINIVA"
				style="position: absolute; top: 365px; left: 240px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DATOS_TRAMIT:IMP_ADJ_SINIVA)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="30" maxlength="10">
				</ispac:htmlText>
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:IMP_ADJ_CONIVA"
				style="position: absolute; top: 390px; left: 10px; width: 410px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_TRAMIT:IMP_ADJ_CONIVA)" />:</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:IMP_ADJ_CONIVA"
				style="position: absolute; top: 390px; left: 240px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DATOS_TRAMIT:IMP_ADJ_CONIVA)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="30" maxlength="10">
				</ispac:htmlText>
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:DOMICILIO_NOTIF_ADJ"
				style="position: absolute; top: 415px; left: 10px; width: 170px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_TRAMIT:DOMICILIO_NOTIF_ADJ)" />:</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:DOMICILIO_NOTIF_ADJ"
				style="position: absolute; top: 415px; left: 240px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DATOS_TRAMIT:DOMICILIO_NOTIF_ADJ)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="55" maxlength="500">
				</ispac:htmlText>
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:NUM_CALLE_ADJ"
				style="position: absolute; top: 415px; left: 550px; width: 170px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_TRAMIT:NUM_CALLE_ADJ)" />:</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:NUM_CALLE_ADJ"
				style="position: absolute; top: 415px; left: 600px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DATOS_TRAMIT:NUM_CALLE_ADJ)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="10" maxlength="500">
				</ispac:htmlText>
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:CP"
				style="position: absolute; top: 415px; left: 680px; width: 170px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_TRAMIT:CP)" />:</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:CP"
				style="position: absolute; top: 411px; left: 710px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DATOS_TRAMIT:CP)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="10" maxlength="500">
				</ispac:htmlText>
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:NUTS_ADJ"
				style="position: absolute; top: 415px; left: 790px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DATOS_TRAMIT:NUTS_ADJ)" />:
			</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:NUTS_ADJ"
				style="position: absolute; top: 411px; left: 840px; width: 100%;">
				<nobr>
					<ispac:htmlTextImageFrame
						property="property(CONTRATACION_DATOS_TRAMIT:NUTS_ADJ)" readonly="true"
						readonlyTag="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="15"
						id="SEARCH_CONTRATACION_DATOS_TRAMIT_NUTS_ADJ" target="workframe"
						action="selectListadoCodicePliego.do?atributo=NUTS"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
						<ispac:parameter name="SEARCH_CONTRATACION_DATOS_TRAMIT_NUTS_ADJ" id="property(CONTRATACION_DATOS_TRAMIT:NUTS_ADJ)" property="SUSTITUTO" />
					</ispac:htmlTextImageFrame>
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:PAIS_ADJ"
				style="position: absolute; top: 415px; left: 1000px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DATOS_TRAMIT:PAIS_ADJ)" />:
			</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:PAIS_ADJ"
				style="position: absolute; top: 411px; left: 1040px; width: 100%;">
				<nobr>
					<ispac:htmlTextImageFrame
						property="property(CONTRATACION_DATOS_TRAMIT:PAIS_ADJ)" readonly="true"
						readonlyTag="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="15"
						id="SEARCH_CONTRATACION_DATOS_TRAMIT_PAIS_ADJ" target="workframe"
						action="selectListadoCodicePliego.do?atributo=COD_PAIS"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
						<ispac:parameter name="SEARCH_CONTRATACION_DATOS_TRAMIT_PAIS_ADJ" id="property(CONTRATACION_DATOS_TRAMIT:PAIS_ADJ)" property="SUSTITUTO" />
					</ispac:htmlTextImageFrame>
				</nobr>
			</div>

			<div id="label_CONTRATACION_DATOS_TRAMIT:MOTIVACION"
				style="position: absolute; top: 440px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DATOS_TRAMIT:MOTIVACION)" />
				:
			</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:MOTIVACION"
				style="position: absolute; top: 440px; left: 240px; width: 100%;">
				<ispac:htmlTextarea
					property="property(CONTRATACION_DATOS_TRAMIT:MOTIVACION)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" rows="3" cols="80">
				</ispac:htmlTextarea>
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:TEXTO_ACUERDO"
			title="Texto del acuerdo en adjudicaciones, desiertos, renuncias y desistimientos. Si se trata de Renuncia o desistimiento es obligatorio."
				style="position: absolute; top: 490px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DATOS_TRAMIT:TEXTO_ACUERDO)" />
				:
			</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:TEXTO_ACUERDO" 
				style="position: absolute; top: 490px; left: 240px; width: 100%;">
				<ispac:htmlTextarea
					property="property(CONTRATACION_DATOS_TRAMIT:TEXTO_ACUERDO)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" rows="4" cols="80">
				</ispac:htmlTextarea>
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:JUSTIFICACION_DESCRIPCION"
				style="position: absolute; top: 560px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DATOS_TRAMIT:JUSTIFICACION_DESCRIPCION)" />
				:
			</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:JUSTIFICACION_DESCRIPCION" 
				style="position: absolute; top: 560px; left: 240px; width: 100%;">
				<ispac:htmlTextarea
					property="property(CONTRATACION_DATOS_TRAMIT:JUSTIFICACION_DESCRIPCION)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" rows="4" cols="80">
				</ispac:htmlTextarea>
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:JUSTIFICACION_PROCESO"
			title="Código que especifica el tipo de documento oferta que debe anexarse en el sobre."
				style="position: absolute; top: 630px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DATOS_TRAMIT:JUSTIFICACION_PROCESO)" />
				:
			</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:JUSTIFICACION_PROCESO"
				style="position: absolute; top: 630px; left: 240px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(CONTRATACION_DATOS_TRAMIT:JUSTIFICACION_PROCESO)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="1" cols="80" 
					id="SEARCH_CONTRATACION_DATOS_TRAMIT:JUSTIFICACION_PROCESO" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_JUSTIFICACION_PROCESO" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CONTRATACION_DATOS_TRAMIT:JUSTIFICACION_PROCESO" id="property(CONTRATACION_DATOS_TRAMIT:JUSTIFICACION_PROCESO)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:INVITACIONES_LICITAR"
				style="position: absolute; top: 660px; left: 10px; width: 170px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_TRAMIT:INVITACIONES_LICITAR)" />:</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:INVITACIONES_LICITAR"
				style="position: absolute; top: 660px; left: 140px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DATOS_TRAMIT:INVITACIONES_LICITAR)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="5" maxlength="500">
				</ispac:htmlText>
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:NUMOFERTAS"
				style="position: absolute; top: 660px; left: 220px; width: 170px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_TRAMIT:NUMOFERTAS)" />:</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:NUMOFERTAS"
				style="position: absolute; top: 660px; left: 350px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DATOS_TRAMIT:NUMOFERTAS)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="5" maxlength="500">
				</ispac:htmlText>
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:IMP_OFERTA_BAJA"
				style="position: absolute; top: 660px; left: 440px; width: 200px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_TRAMIT:IMP_OFERTA_BAJA)" />:</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:IMP_OFERTA_BAJA"
				style="position: absolute; top: 660px; left: 620px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DATOS_TRAMIT:IMP_OFERTA_BAJA)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="15" maxlength="500">
				</ispac:htmlText>
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:IMP_OFERTA_ALTA"
				style="position: absolute; top: 660px; left: 720px; width: 200px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_TRAMIT:IMP_OFERTA_ALTA)" />:</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:DOMICILIO_NOTIF_ADJ"
				style="position: absolute; top: 660px; left: 900px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DATOS_TRAMIT:IMP_OFERTA_ALTA)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="13" maxlength="500">
				</ispac:htmlText>
			</div>
			
			<!-- ------------------------------------------------------------------------------------------------ -->

			<div id="label_SPAC_EXPEDIENTES:SEP_INTERESADO_PRINCIPAL"
				style="position: absolute; top: 690px; left: 10px; width: 620px"
				class="textbar">
				FORMALIZACIÓN
				<hr class="formbar" />
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:F_CONTRATO"
				style="position: absolute; top: 720px; left: 10px; width: 410px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_TRAMIT:F_CONTRATO)" />:</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:F_CONTRATO"
				style="position: absolute; top: 720px; left: 240px; width: 100%;">
				<nobr> 
					<ispac:htmlTextCalendar
						property="property(CONTRATACION_DATOS_TRAMIT:F_CONTRATO)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%= buttoncalendar %>' format="dd/mm/yyyy" enablePast="true">
					</ispac:htmlTextCalendar> 
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:F_PUB_DOCE_EXP_CONT"
				style="position: absolute; top: 722px; left: 402px; width: 410px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_TRAMIT:F_PUB_DOCE_EXP_CONT)" />(*):</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:F_APERT_PROPOS"
				style="position: absolute; top: 720px; left: 600px; width: 100%;">
				<nobr> 
					<ispac:htmlTextCalendar
						property="property(CONTRATACION_DATOS_TRAMIT:F_PUB_DOCE_EXP_CONT)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%= buttoncalendar %>' format="dd/mm/yyyy" enablePast="true">
					</ispac:htmlTextCalendar> 
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:FECHA_INICIO_CONTRATO"
				style="position: absolute; top: 745px; left: 10px; width: 410px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_TRAMIT:FECHA_INICIO_CONTRATO)" />:</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:FECHA_INICIO_CONTRATO"
				style="position: absolute; top: 745px; left: 240px; width: 100%;">
				<nobr> 
					<ispac:htmlTextCalendar
						property="property(CONTRATACION_DATOS_TRAMIT:FECHA_INICIO_CONTRATO)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%= buttoncalendar %>' format="dd/mm/yyyy" enablePast="true">
					</ispac:htmlTextCalendar> 
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:FECHA_FIN_CONTRATO"
				style="position: absolute; top: 770px; left: 10px; width: 410px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_TRAMIT:FECHA_FIN_CONTRATO)" />:</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:FECHA_FIN_CONTRATO"
				style="position: absolute; top: 770px; left: 240px; width: 100%;">
				<nobr> 
					<ispac:htmlTextCalendar
						property="property(CONTRATACION_DATOS_TRAMIT:FECHA_FIN_CONTRATO)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%= buttoncalendar %>' format="dd/mm/yyyy" enablePast="true">
					</ispac:htmlTextCalendar> 
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:PORCENTAJE_SUBCONTRATACION"
				style="position: absolute; top: 795px; left: 10px; width: 410px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_TRAMIT:PORCENTAJE_SUBCONTRATACION)" />:</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:PORCENTAJE_SUBCONTRATACION"
				style="position: absolute; top: 795px; left: 240px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DATOS_TRAMIT:PORCENTAJE_SUBCONTRATACION)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="14" maxlength="500">
				</ispac:htmlText>
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:TEXTO_ACUERDO_FORMALIZACION"
			title="Texto del acuerdo en adjudicaciones, desiertos, renuncias y desistimientos. Si se trata de Renuncia o desistimiento es obligatorio."
				style="position: absolute; top: 820px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DATOS_TRAMIT:TEXTO_ACUERDO_FORMALIZACION)" />
				:
			</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:TEXTO_ACUERDO_FORMALIZACION" 
				style="position: absolute; top: 820px; left: 240px; width: 100%;">
				<ispac:htmlTextarea
					property="property(CONTRATACION_DATOS_TRAMIT:TEXTO_ACUERDO_FORMALIZACION)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" rows="4" cols="80">
				</ispac:htmlTextarea>
			</div>
			
			<div id="LABEL_CONTRATACION_DATOS_TRAMIT:PRORROGA"
				style="position: absolute; top: 880px; left: 10px; width: 160px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DATOS_TRAMIT:PRORROGA)" />
				:
			</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:PRORROGA"
				style="position: absolute; top: 880px; left: 240px; width: 100%;">
				<nobr>
					<ispac:htmlTextImageFrame
						property="property(CONTRATACION_DATOS_TRAMIT:PRORROGA)" readonly="true"
						readonlyTag="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="1"
						id="SEARCH_CONTRATACION_DATOS_TRAMIT_PRORROGA" target="workframe"
						action="selectValue.do?entity=SPAC_TBL_009"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
						<ispac:parameter name="SEARCH_CONTRATACION_DATOS_TRAMIT_PRORROGA"
							id="property(CONTRATACION_DATOS_TRAMIT:PRORROGA)" property="VALOR" />
					</ispac:htmlTextImageFrame>
				</nobr>
			</div>
			
			<div id="LABEL_CONTRATACION_DATOS_TRAMIT:TIEMPOPRORROGA"
				style="position: absolute; top: 880px; left: 350px; width: 410px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_TRAMIT:TIEMPOPRORROGA)" />:</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:TIEMPOPRORROGA"
				style="position: absolute; top: 880px; left: 460px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DATOS_TRAMIT:TIEMPOPRORROGA)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="10" maxlength="50">
				</ispac:htmlText>meses
			</div>
			
			
			
			<!-- -------------------------------------------------------------------------------------- -->
			<!--<div id="label_SPAC_EXPEDIENTES:SEP_INTERESADO_PRINCIPAL"
				style="position: absolute; top: 510px; left: 10px; width: 620px"
				class="textbar">
				
				<hr class="formbar" />
			</div>
			
			
			 					
			<div id="label_CONTRATACION_DATOS_TRAMIT:PLAZO_EJEC_CONTRAT"
				style="position: absolute; top: 540px; left: 10px; width: 410px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_TRAMIT:PLAZO_EJEC_CONTRAT)" />:</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:PLAZO_EJEC_CONTRAT"
				style="position: absolute; top: 540px; left: 250px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DATOS_TRAMIT:PLAZO_EJEC_CONTRAT)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="100">
				</ispac:htmlText>
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:F_FIN_PLAZO_EJEC"
				style="position: absolute; top: 565px; left: 10px; width: 410px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_TRAMIT:F_FIN_PLAZO_EJEC)" />:</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:F_FIN_PLAZO_EJEC"
				style="position: absolute; top: 565px; left: 250px; width: 100%;">
				<nobr> 
					<ispac:htmlTextCalendar
						property="property(CONTRATACION_DATOS_TRAMIT:F_FIN_PLAZO_EJEC)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%= buttoncalendar %>' format="dd/mm/yyyy" enablePast="true">
					</ispac:htmlTextCalendar> 
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:F_ACTA_COMPR_REPLANTEO"
				style="position: absolute; top: 590px; left: 10px; width: 410px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_TRAMIT:F_ACTA_COMPR_REPLANTEO)" />:</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:F_ACTA_COMPR_REPLANTEO"
				style="position: absolute; top: 590px; left: 250px; width: 100%;">
				<nobr> 
					<ispac:htmlTextCalendar
						property="property(CONTRATACION_DATOS_TRAMIT:F_ACTA_COMPR_REPLANTEO)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%= buttoncalendar %>' format="dd/mm/yyyy" enablePast="true">
					</ispac:htmlTextCalendar> 
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:F_RECEPCION_PROV"
				style="position: absolute; top: 615px; left: 10px; width: 410px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_TRAMIT:F_RECEPCION_PROV)" />:</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:F_RECEPCION_PROV"
				style="position: absolute; top: 615px; left: 250px; width: 100%;">
				<nobr> 
					<ispac:htmlTextCalendar
						property="property(CONTRATACION_DATOS_TRAMIT:F_RECEPCION_PROV)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%= buttoncalendar %>' format="dd/mm/yyyy" enablePast="true">
					</ispac:htmlTextCalendar> 
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:F_RECEPCION_DEFINITIVA"
				style="position: absolute; top: 640px; left: 10px; width: 410px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_TRAMIT:F_RECEPCION_DEFINITIVA)" />:</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:F_RECEPCION_DEFINITIVA"
				style="position: absolute; top: 640px; left: 250px; width: 100%;">
				<nobr> 
					<ispac:htmlTextCalendar
						property="property(CONTRATACION_DATOS_TRAMIT:F_RECEPCION_DEFINITIVA)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%= buttoncalendar %>' format="dd/mm/yyyy" enablePast="true">
					</ispac:htmlTextCalendar> 
				</nobr>
			</div>
			-->
			
			<!--
			<div id="label_CONTRATACION_DATOS_TRAMIT:F_DEV_GARANT_DEFIN"
				style="position: absolute; top: 485px; left: 10px; width: 410px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_TRAMIT:F_DEV_GARANT_DEFIN)" />:</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:F_DEV_GARANT_DEFIN"
				style="position: absolute; top: 485px; left: 380px; width: 100%;">
				<nobr> 
					<ispac:htmlTextCalendar
						property="property(CONTRATACION_DATOS_TRAMIT:F_DEV_GARANT_DEFIN)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%= buttoncalendar %>' format="dd/mm/yyyy" enablePast="true">
					</ispac:htmlTextCalendar> 
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:F_DEV_GARANT_COMPL"
				style="position: absolute; top: 510px; left: 10px; width: 410px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_TRAMIT:F_DEV_GARANT_COMPL)" />:</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:F_DEV_GARANT_COMPL"
				style="position: absolute; top: 510px; left: 380px; width: 100%;">
				<nobr> 
					<ispac:htmlTextCalendar
						property="property(CONTRATACION_DATOS_TRAMIT:F_DEV_GARANT_COMPL)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%= buttoncalendar %>' format="dd/mm/yyyy" enablePast="true">
					</ispac:htmlTextCalendar> 
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:GARANTIA_DEFINITIVA"
				style="position: absolute; top: 310px; left: 10px; width: 410px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_TRAMIT:GARANTIA_DEFINITIVA)" />:</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:GARANTIA_DEFINITIVA"
				style="position: absolute; top: 310px; left: 380px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DATOS_TRAMIT:GARANTIA_DEFINITIVA)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="100">
				</ispac:htmlText>
			</div>
			
			<div id="label_CONTRATACION_DATOS_TRAMIT:GARANTIA_COMPLEMENTARIA"
				style="position: absolute; top: 335px; left: 10px; width: 410px;"
				class="formsTitleB"><bean:write name="defaultForm"
				property="entityApp.label(CONTRATACION_DATOS_TRAMIT:GARANTIA_COMPLEMENTARIA)" />:</div>
			<div id="data_CONTRATACION_DATOS_TRAMIT:GARANTIA_COMPLEMENTARIA"
				style="position: absolute; top: 335px; left: 380px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DATOS_TRAMIT:GARANTIA_COMPLEMENTARIA)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="100">
				</ispac:htmlText>
			</div>
			
			 -->
			
		</div>
	</ispac:tab>
</ispac:tabs>
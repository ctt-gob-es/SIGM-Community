<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c1"%>
<c1:set var="aux0">
	<bean:write name="defaultForm"
		property="entityApp.label(CONTRATACION_PERS_CONTACTO:CONTRATACION_PERS_CONTACTO)" />
</c1:set><jsp:useBean id="aux0" type="java.lang.String" />
<ispac:tabs>
	<ispac:tab title='<%=aux0%>'>
		<div id="dataBlock_CONTRATACION_PERS_CONTACTO"
			style="position: relative; height: 500px; width: 600px">
			<div id="label_SPAC_EXPEDIENTES:SEP_INTERESADO_PRINCIPAL"
				style="position: absolute; top: 10px; left: 10px; width: 620px"
				class="textbar">
				CONTRATACIÓN Y COMPRAS
				<hr class="formbar" />
			</div>
			
			<div id="label_CONTRATACION_PERS_CONTACTO:NOMBRE"
				style="position: absolute; top: 40px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_PERS_CONTACTO:NOMBRE)" />
				:
			</div>
			<div id="data_CONTRATACION_PERS_CONTACTO:NOMBRE"
				style="position: absolute; top: 40px; left: 150px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_PERS_CONTACTO:NOMBRE)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="255">
				</ispac:htmlText>
			</div>
			<div id="label_CONTRATACION_PERS_CONTACTO:EMAIL"
				style="position: absolute; top: 65px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_PERS_CONTACTO:EMAIL)" />
				:
			</div>
			<div id="data_CONTRATACION_PERS_CONTACTO:EMAIL"
				style="position: absolute; top: 65px; left: 150px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_PERS_CONTACTO:EMAIL)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="100">
				</ispac:htmlText>
			</div>
			<div id="label_CONTRATACION_PERS_CONTACTO:CALLE"
				style="position: absolute; top: 90px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_PERS_CONTACTO:CALLE)" />
				:
			</div>
			<div id="data_CONTRATACION_PERS_CONTACTO:CALLE"
				style="position: absolute; top: 90px; left: 150px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_PERS_CONTACTO:CALLE)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="255">
				</ispac:htmlText>
			</div>
			<div id="label_CONTRATACION_PERS_CONTACTO:CP"
				style="position: absolute; top: 115px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_PERS_CONTACTO:CP)" />
				:
			</div>
			<div id="data_CONTRATACION_PERS_CONTACTO:CP"
				style="position: absolute; top: 115px; left: 150px; width: 100%;">
				<ispac:htmlText property="property(CONTRATACION_PERS_CONTACTO:CP)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="5">
				</ispac:htmlText>
			</div>
			<div id="label_CONTRATACION_PERS_CONTACTO:LOCALIDAD"
				style="position: absolute; top: 140px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_PERS_CONTACTO:LOCALIDAD)" />
				:
			</div>
			<div id="data_CONTRATACION_PERS_CONTACTO:LOCALIDAD"
				style="position: absolute; top: 140px; left: 150px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_PERS_CONTACTO:LOCALIDAD)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="255">
				</ispac:htmlText>
			</div>
			<div id="label_CONTRATACION_PERS_CONTACTO:PROVINCIA"
				style="position: absolute; top: 165px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_PERS_CONTACTO:PROVINCIA)" />
				:
			</div>
			<div id="data_CONTRATACION_PERS_CONTACTO:PROVINCIA"
				style="position: absolute; top: 165px; left: 150px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_PERS_CONTACTO:PROVINCIA)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="255">
				</ispac:htmlText>
			</div>
			
			<div id="label_CONTRATACION_PERS_CONTACTO:LOCALIZACIONGEOGRAFICA"
				style="position: absolute; top: 190px; left: 10px; width: 250px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_PERS_CONTACTO:LOCALIZACIONGEOGRAFICA)" />
				:
			</div>
			<div id="data_CONTRATACION_PERS_CONTACTO:LOCALIZACIONGEOGRAFICA"
				style="position: absolute; top: 190px; left: 150px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(CONTRATACION_PERS_CONTACTO:LOCALIZACIONGEOGRAFICA)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="1" cols="80" 
					id="SEARCH_CONTRATACION_PERS_CONTACTO:LOCALIZACIONGEOGRAFICA" target="workframe"
					action="selectListadoCodicePliego.do?atributo=COD_LOCALIZACIONGEOGRAFICA" 
					image="img/search-mg.gif" jsDelete="deleteAdjudicado" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CONTRATACION_PERS_CONTACTO:LOCALIZACIONGEOGRAFICA" id="property(CONTRATACION_PERS_CONTACTO:LOCALIZACIONGEOGRAFICA)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_PERS_CONTACTO:MOVIL"
				style="position: absolute; top: 215px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_PERS_CONTACTO:MOVIL)" />
				:
			</div>
			<div id="data_CONTRATACION_PERS_CONTACTO:MOVIL"
				style="position: absolute; top: 215px; left: 150px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_PERS_CONTACTO:MOVIL)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="10">
				</ispac:htmlText>
			</div>
			
			<div id="label_SPAC_EXPEDIENTES:SEP_INTERESADO_PRINCIPAL"
				style="position: absolute; top: 240px; left: 10px; width: 620px"
				class="textbar">
				REGISTRO E INFORMACIÓN Y PATRIMONIO 
				<hr class="formbar" />
			</div>
			
			
			<div id="label_CONTRATACION_PERS_CONTACTO:NOMBRESECRE"
				style="position: absolute; top: 270px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_PERS_CONTACTO:NOMBRESECRE)" />
				:
			</div>
			<div id="data_CONTRATACION_PERS_CONTACTO:NOMBRESECRE"
				style="position: absolute; top: 270px; left: 150px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_PERS_CONTACTO:NOMBRESECRE)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="255">
				</ispac:htmlText>
			</div>
			<div id="label_CONTRATACION_PERS_CONTACTO:EMAILSECRE"
				style="position: absolute; top: 295px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_PERS_CONTACTO:EMAILSECRE)" />
				:
			</div>
			<div id="data_CONTRATACION_PERS_CONTACTO:EMAILSECRE"
				style="position: absolute; top: 295px; left: 150px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_PERS_CONTACTO:EMAILSECRE)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="100">
				</ispac:htmlText>
			</div>
			<div id="label_CONTRATACION_PERS_CONTACTO:CALLESECRE"
				style="position: absolute; top: 320px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_PERS_CONTACTO:CALLESECRE)" />
				:
			</div>
			<div id="data_CONTRATACION_PERS_CONTACTO:CALLESECRE"
				style="position: absolute; top: 320px; left: 150px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_PERS_CONTACTO:CALLESECRE)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="255">
				</ispac:htmlText>
			</div>
			<div id="label_CONTRATACION_PERS_CONTACTO:CPSECRE"
				style="position: absolute; top: 345px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_PERS_CONTACTO:CPSECRE)" />
				:
			</div>
			<div id="data_CONTRATACION_PERS_CONTACTO:CPSECRE"
				style="position: absolute; top: 345px; left: 150px; width: 100%;">
				<ispac:htmlText property="property(CONTRATACION_PERS_CONTACTO:CPSECRE)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="5">
				</ispac:htmlText>
			</div>
			<div id="label_CONTRATACION_PERS_CONTACTO:LOCALIDADSECRE"
				style="position: absolute; top: 370px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_PERS_CONTACTO:LOCALIDADSECRE)" />
				:
			</div>
			<div id="data_CONTRATACION_PERS_CONTACTO:LOCALIDADSECRE"
				style="position: absolute; top: 370px; left: 150px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_PERS_CONTACTO:LOCALIDADSECRE)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="255">
				</ispac:htmlText>
			</div>
			<div id="label_CONTRATACION_PERS_CONTACTO:PROVINCIASECRE"
				style="position: absolute; top: 395px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_PERS_CONTACTO:PROVINCIASECRE)" />
				:
			</div>
			<div id="data_CONTRATACION_PERS_CONTACTO:PROVINCIASECRE"
				style="position: absolute; top: 395px; left: 150px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_PERS_CONTACTO:PROVINCIASECRE)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="255">
				</ispac:htmlText>
			</div>
			
			<div id="label_CONTRATACION_PERS_CONTACTO:LOCALIZACIONGEOGRAFICASECRE"
				style="position: absolute; top: 420px; left: 10px; width: 250px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_PERS_CONTACTO:LOCALIZACIONGEOGRAFICASECRE)" />
				:
			</div>
			<div id="data_CONTRATACION_PERS_CONTACTO:LOCALIZACIONGEOGRAFICASECRE"
				style="position: absolute; top: 420px; left: 150px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(CONTRATACION_PERS_CONTACTO:LOCALIZACIONGEOGRAFICASECRE)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="1" cols="80" 
					id="SEARCH_CONTRATACION_PERS_CONTACTO:LOCALIZACIONGEOGRAFICASECRE" target="workframe"
					action="selectListadoCodicePliego.do?atributo=COD_LOCALIZACIONGEOGRAFICA" 
					image="img/search-mg.gif" jsDelete="deleteAdjudicado" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CONTRATACION_PERS_CONTACTO:LOCALIZACIONGEOGRAFICASECRE" id="property(CONTRATACION_PERS_CONTACTO:LOCALIZACIONGEOGRAFICASECRE)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_PERS_CONTACTO:MOVILSECRE"
				style="position: absolute; top: 445px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_PERS_CONTACTO:MOVILSECRE)" />
				:
			</div>
			<div id="data_CONTRATACION_PERS_CONTACTO:MOVILSECRE"
				style="position: absolute; top: 445px; left: 150px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_PERS_CONTACTO:MOVILSECRE)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="10">
				</ispac:htmlText>
			</div>
			
			
		</div>
	</ispac:tab>
</ispac:tabs>
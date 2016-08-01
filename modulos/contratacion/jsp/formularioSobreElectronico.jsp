<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c1"%>
<c1:set var="aux0">
	<bean:write name="defaultForm"
		property="entityApp.label(CONTRATACION_DOC_PRESENTAR:CONTRATACION_DOC_PRESENTAR)" />
</c1:set><jsp:useBean id="aux0" type="java.lang.String" />
<ispac:tabs>
	<ispac:tab title='<%=aux0%>'>
		<div id="dataBlock_CONTRATACION_DOC_PRESENTAR"
			style="position: relative; height: 280px; width: 600px">
			<div id="label_CONTRATACION_DOC_PRESENTAR:NOMBRESOBRE"
			title="Identificador del sobre especificado por el órgano de contratación."
				style="position: absolute; top: 10px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DOC_PRESENTAR:NOMBRESOBRE)" />
				:
			</div>
			<div id="data_CONTRATACION_DOC_PRESENTAR:NOMBRESOBRE"
				style="position: absolute; top: 10px; left: 180px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DOC_PRESENTAR:NOMBRESOBRE)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="256">
				</ispac:htmlText>
			</div>
			
			<div id="label_CONTRATACION_DOC_PRESENTAR:DESCRIPCION"
				style="position: absolute; top: 35px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DOC_PRESENTAR:DESCRIPCION)" />
				:
			</div>
			<div id="data_CONTRATACION_DOC_PRESENTAR:DESCRIPCION"
				style="position: absolute; top: 35px; left: 180px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DOC_PRESENTAR:DESCRIPCION)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="256">
				</ispac:htmlText>
			</div>
			
			<div id="label_CONTRATACION_DOC_PRESENTAR:TIPO_DOC"
			title="Código que especifica el tipo de documento oferta que debe anexarse en el sobre."
				style="position: absolute; top: 60px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DOC_PRESENTAR:TIPO_DOC)" />
				:
			</div>
			<div id="data_CONTRATACION_DOC_PRESENTAR:TIPO_DOC"
				style="position: absolute; top: 60px; left: 180px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(CONTRATACION_DOC_PRESENTAR:TIPO_DOC)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="1" cols="80" 
					id="SEARCH_CONTRATACION_DOC_PRESENTAR:TIPO_DOC" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_TIP_SOBRE" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CONTRATACION_DOC_PRESENTAR:TIPO_DOC" id="property(CONTRATACION_DOC_PRESENTAR:TIPO_DOC)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			<div id="label_CONTRATACION_DOC_PRESENTAR:LIST_DOC"
			title="Lista de documentos que es necesario aportar en el sobre."
				style="position: absolute; top: 85px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DOC_PRESENTAR:LIST_DOC)" />
				:
			</div>
			<div id="data_CONTRATACION_DOC_PRESENTAR:LIST_DOC"
				style="position: absolute; top: 85px; left: 180px; width: 100%;">
				<ispac:htmlTextMultivalue
					property="propertyMultivalue(CONTRATACION_DOC_PRESENTAR:LIST_DOC)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" divWidth="480" divHeight="60"
					maxlength="1024">
				</ispac:htmlTextMultivalue>
			</div>

			<div id="label_CONTRATACION_DOC_PRESENTAR:F_APERTURA"
				style="position: absolute; top: 170px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DOC_PRESENTAR:F_APERTURA)" />
				:
			</div>
			<div id="data_CONTRATACION_DOC_PRESENTAR:F_APERTURA"
				style="position: absolute; top: 170px; left: 180px; width: 100%;">
				<nobr>
					<ispac:htmlTextCalendar
						property="property(CONTRATACION_DOC_PRESENTAR:F_APERTURA)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%=buttoncalendar%>' format="dd/mm/yyyy"
						enablePast="true">
					</ispac:htmlTextCalendar>
				</nobr>
			</div>
			<div id="label_CONTRATACION_DOC_PRESENTAR:HORA_APERT"
				style="position: absolute; top: 170px; left: 340px; width: 150px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DOC_PRESENTAR:HORA_APERT)" />
				(hh:mm) : 
			</div>
			<div id="data_CONTRATACION_DOC_PRESENTAR:HORA_APERT"
				style="position: absolute; top: 170px; left: 490px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DOC_PRESENTAR:HORA_APERT)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="10" maxlength="50">
				</ispac:htmlText>
			</div>
			
			<div id="label_CONTRATACION_DOC_PRESENTAR:LUGAR"
				style="position: absolute; top: 195px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DOC_PRESENTAR:LUGAR)" />
				:
			</div>
			<div id="data_CONTRATACION_DOC_PRESENTAR:LUGAR"
				style="position: absolute; top: 195px; left: 180px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DOC_PRESENTAR:LUGAR)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" value="Diputación Provincial de Ciudad Real" size="80" maxlength="300">
				</ispac:htmlText>
			</div>
			
			<div id="label_CONTRATACION_DOC_PRESENTAR:CALLE"
				style="position: absolute; top: 220px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DOC_PRESENTAR:CALLE)" />
				:
			</div>
			<div id="data_CONTRATACION_DOC_PRESENTAR:CALLE"
				style="position: absolute; top: 220px; left: 180px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DOC_PRESENTAR:CALLE)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" value="C/Toledo, nº1" maxlength="300">
				</ispac:htmlText>
			</div>
			
			<div id="label_CONTRATACION_DOC_PRESENTAR:LOCALIDAD"
				style="position: absolute; top: 245px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DOC_PRESENTAR:LOCALIDAD)" />
				:
			</div>
			<div id="data_CONTRATACION_DOC_PRESENTAR:LOCALIDAD"
				style="position: absolute; top: 245px; left: 180px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DOC_PRESENTAR:LOCALIDAD)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" value="Ciudad Real" maxlength="300">
				</ispac:htmlText>
			</div>
			
			<div id="label_CONTRATACION_DOC_PRESENTAR:PROVINCIA"
				style="position: absolute; top: 270px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DOC_PRESENTAR:PROVINCIA)" />
				:
			</div>
			<div id="data_CONTRATACION_DOC_PRESENTAR:PROVINCIA"
				style="position: absolute; top: 270px; left: 180px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DOC_PRESENTAR:PROVINCIA)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" value="Ciudad Real" maxlength="300">
				</ispac:htmlText>
			</div>
			
			<div id="label_CONTRATACION_DOC_PRESENTAR:CP"
				style="position: absolute; top: 270px; left: 680px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DOC_PRESENTAR:CP)" />
				:
			</div>
			<div id="data_CONTRATACION_DOC_PRESENTAR:CP"
				style="position: absolute; top: 270px; left: 730px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DOC_PRESENTAR:CP)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="10" value="13003" maxlength="10">
				</ispac:htmlText>
			</div>
		</div>
	</ispac:tab>
</ispac:tabs>
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
		<div id="dataBlock_CONTRATACION_DOC_PRESENTAR" style="position: relative; height: 550px; width: 600px">
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
			
			<div id="label_SPAC_EXPEDIENTES:SEP_INTERESADO_PRINCIPAL" style="position: absolute; top: 180px; left: 10px; width: 620px" class="textbar">
			EVENTO APERTURA
			<hr class="formbar"/>
			</div>
			
			<div id="label_CONTRATACION_DOC_PRESENTAR:TIPO_EVENTO"
			title="Código que especifica el tipo de documento oferta que debe anexarse en el sobre."
				style="position: absolute; top: 210px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DOC_PRESENTAR:TIPO_EVENTO)" />
				:
			</div>
			<div id="data_CONTRATACION_DOC_PRESENTAR:TIPO_EVENTO"
				style="position: absolute; top: 210px; left: 180px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(CONTRATACION_DOC_PRESENTAR:TIPO_EVENTO)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="1" cols="80" 
					id="SEARCH_CONTRATACION_DOC_PRESENTAR:TIPO_EVENTO" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_TIPO_EVENTO" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CONTRATACION_DOC_PRESENTAR:TIPO_EVENTO" id="property(CONTRATACION_DOC_PRESENTAR:TIPO_EVENTO)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>

			<div id="label_CONTRATACION_DOC_PRESENTAR:F_APERTURA"
				style="position: absolute; top: 270px; left: 10px; width: 110px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DOC_PRESENTAR:F_APERTURA)" />
				:
			</div>
			<div id="data_CONTRATACION_DOC_PRESENTAR:F_APERTURA"
				style="position: absolute; top: 270px; left: 180px; width: 100%;">
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
				style="position: absolute; top: 270px; left: 340px; width: 150px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DOC_PRESENTAR:HORA_APERT)" />
				(hh:mm) : 
			</div>
			<div id="data_CONTRATACION_DOC_PRESENTAR:HORA_APERT"
				style="position: absolute; top: 270px; left: 490px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DOC_PRESENTAR:HORA_APERT)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="10" maxlength="50">
				</ispac:htmlText>
			</div>
			
			<div id="label_CONTRATACION_DOC_PRESENTAR:LUGAR"
				style="position: absolute; top: 295px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DOC_PRESENTAR:LUGAR)" />
				:
			</div>
			<div id="data_CONTRATACION_DOC_PRESENTAR:LUGAR"
				style="position: absolute; top: 295px; left: 180px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DOC_PRESENTAR:LUGAR)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="300">
				</ispac:htmlText>
			</div>
			
			<div id="label_CONTRATACION_DOC_PRESENTAR:CALLE"
				style="position: absolute; top: 320px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DOC_PRESENTAR:CALLE)" />
				:
			</div>
			<div id="data_CONTRATACION_DOC_PRESENTAR:CALLE"
				style="position: absolute; top: 320px; left: 180px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DOC_PRESENTAR:CALLE)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="300">
				</ispac:htmlText>
			</div>
			
			<div id="label_CONTRATACION_DOC_PRESENTAR:LOCALIDAD"
				style="position: absolute; top: 345px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DOC_PRESENTAR:LOCALIDAD)" />
				:
			</div>
			<div id="data_CONTRATACION_DOC_PRESENTAR:LOCALIDAD"
				style="position: absolute; top: 345px; left: 180px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DOC_PRESENTAR:LOCALIDAD)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="300">
				</ispac:htmlText>
			</div>
			
			<div id="label_CONTRATACION_DOC_PRESENTAR:PROVINCIA"
				style="position: absolute; top: 370px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DOC_PRESENTAR:PROVINCIA)" />
				:
			</div>
			<div id="data_CONTRATACION_DOC_PRESENTAR:PROVINCIA"
				style="position: absolute; top: 370px; left: 180px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DOC_PRESENTAR:PROVINCIA)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="300">
				</ispac:htmlText>
			</div>
			
			<div id="label_CONTRATACION_DOC_PRESENTAR:CP"
				style="position: absolute; top: 370px; left: 680px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DOC_PRESENTAR:CP)" />
				:
			</div>
			<div id="data_CONTRATACION_DOC_PRESENTAR:CP"
				style="position: absolute; top: 370px; left: 730px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DOC_PRESENTAR:CP)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="10" maxlength="10">
				</ispac:htmlText>
			</div>
			
			<div id="label_SPAC_EXPEDIENTES:SEP_INTERESADO_PRINCIPAL" style="position: absolute; top: 395px; left: 10px; width: 620px" class="textbar">
			CONDICIONES DE PRESENTACIÓN
			<hr class="formbar"/>
			</div>
			<div id="label_CONTRATACION_DOC_PRESENTAR:PRESEN_SOBRE"
				style="position: absolute; top: 425px; left: 10px; width: 310px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DOC_PRESENTAR:PRESEN_SOBRE)" />:
			</div>
			<div id="data_CONTRATACION_DOC_PRESENTAR:PRESEN_SOBRE"
				style="position: absolute; top: 425px; left: 300px; width: 100%;">
				<nobr>
					<html:hidden
						property="property(CONTRATACION_DOC_PRESENTAR:PRESEN_SOBRE)" />
					<ispac:htmlTextImageFrame
						property="property(PRESEN_SOBRE_CONTRATACION_PRESENT_SOBRE:SUSTITUTO)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" size="80"
						imageTabIndex="true"
						id="SEARCH_CONTRATACION_DOC_PRESENTAR_PRESEN_SOBRE"
						target="workframe"
						action="selectSubstitute.do?entity=CONTRATACION_PRESENT_SOBRE"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
						<ispac:parameter
							name="SEARCH_CONTRATACION_DOC_PRESENTAR_PRESEN_SOBRE"
							id="property(CONTRATACION_DOC_PRESENTAR:PRESEN_SOBRE)"
							property="VALOR" />
						<ispac:parameter
							name="SEARCH_CONTRATACION_DOC_PRESENTAR_PRESEN_SOBRE"
							id="property(PRESEN_SOBRE_CONTRATACION_PRESENT_SOBRE:SUSTITUTO)"
							property="SUSTITUTO" />
					</ispac:htmlTextImageFrame>
				</nobr>
			</div>
			
			<div id="label_SPAC_EXPEDIENTES:SEP_INTERESADO_PRINCIPAL" style="position: absolute; top: 455px; left: 10px; width: 620px" class="textbar">
			Firma y cifrado
			</div>
			
			<div id="label_CONTRATACION_DOC_PRESENTAR:FIRMA"
				style="position: absolute; top: 485px; left: 10px; width: 510px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DOC_PRESENTAR:FIRMA)" />:
			</div>
			<div id="data_CONTRATACION_DOC_PRESENTAR:FIRMA"
				style="position: absolute; top: 485px; left: 510px; width: 100%;">
				<nobr> 
					<ispac:htmlTextImageFrame
						property="property(CONTRATACION_DOC_PRESENTAR:FIRMA)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" size="10"
						id="SEARCH_CONTRATACION_DOC_PRESENTAR_FIRMA"
						target="workframe" action="selectValue.do?entity=SPAC_TBL_009"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
							<ispac:parameter
								name="SEARCH_CONTRATACION_DOC_PRESENTAR_FIRMA"
								id="property(CONTRATACION_DOC_PRESENTAR:FIRMA)"
								property="VALOR" />
					</ispac:htmlTextImageFrame> 
				</nobr>
			</div>
			
			
			<div id="label_CONTRATACION_DOC_PRESENTAR:CIFRADO"
				style="position: absolute; top: 510px; left: 10px; width: 510px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DOC_PRESENTAR:CIFRADO)" />:
			</div>
			<div id="data_CONTRATACION_DOC_PRESENTAR:CIFRADO"
				style="position: absolute; top: 510px; left: 510px; width: 100%;">
				<nobr> 
					<ispac:htmlTextImageFrame
						property="property(CONTRATACION_DOC_PRESENTAR:CIFRADO)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" size="10"
						id="SEARCH_CONTRATACION_DOC_PRESENTAR_CIFRADO"
						target="workframe" action="selectValue.do?entity=SPAC_TBL_009"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
							<ispac:parameter
								name="SEARCH_CONTRATACION_DOC_PRESENTAR_CIFRADO"
								id="property(CONTRATACION_DOC_PRESENTAR:CIFRADO)"
								property="VALOR" />
					</ispac:htmlTextImageFrame> 
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_DOC_PRESENTAR:ANONIMO"
				style="position: absolute; top: 535px; left: 10px; width: 510px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DOC_PRESENTAR:ANONIMO)" />:
			</div>
			<div id="data_CONTRATACION_DOC_PRESENTAR:ANONIMO"
				style="position: absolute; top: 535px; left: 510px; width: 100%;">
				<nobr> 
					<ispac:htmlTextImageFrame
						property="property(CONTRATACION_DOC_PRESENTAR:ANONIMO)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" size="10"
						id="SEARCH_CONTRATACION_DOC_PRESENTAR_ANONIMO"
						target="workframe" action="selectValue.do?entity=SPAC_TBL_009"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
							<ispac:parameter
								name="SEARCH_CONTRATACION_DOC_PRESENTAR_ANONIMO"
								id="property(CONTRATACION_DOC_PRESENTAR:ANONIMO)"
								property="VALOR" />
					</ispac:htmlTextImageFrame> 
				</nobr>
			</div>
		</div>
	</ispac:tab>
</ispac:tabs>














































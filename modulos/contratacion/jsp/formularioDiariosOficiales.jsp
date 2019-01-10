<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c1"%>
<c1:set var="aux0">
	<bean:write name="defaultForm" property="entityApp.label(DOUE:DOUE)" />
</c1:set><jsp:useBean id="aux0" type="java.lang.String" />
<ispac:tabs>
	<ispac:tab title='<%=aux0%>'>
		<div id="dataBlock_CONTRATACION_DOUE"
			style="position: relative; height: 580px; width: 650px">
			<div id="label_CONTRATACION_DOUE:PUB_ANUNCIO"
				style="position: absolute; top: 10px; left: 10px; width: 160px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DOUE:PUB_ANUNCIO)" />
				:
			</div>
			<div id="data_CONTRATACION_DOUE:PUB_ANUNCIO"
				style="position: absolute; top: 10px; left: 180px; width: 100%;">
				<nobr>
					<ispac:htmlTextImageFrame
						property="property(CONTRATACION_DOUE:PUB_ANUNCIO)" readonly="true"
						readonlyTag="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="10"
						id="SEARCH_CONTRATACION_DOUE_PUB_ANUNCIO" target="workframe"
						action="selectValue.do?entity=SPAC_TBL_009"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
						<ispac:parameter name="SEARCH_CONTRATACION_DOUE_PUB_ANUNCIO"
							id="property(CONTRATACION_DOUE:PUB_ANUNCIO)" property="VALOR" />
					</ispac:htmlTextImageFrame>
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_DOUE:PUB_ANUNCIO_BOP"
				style="position: absolute; top: 40px; left: 10px; width: 160px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DOUE:PUB_ANUNCIO_BOP)" />
				:
			</div>
			<div id="data_CONTRATACION_DOUE:PUB_ANUNCIO_BOP"
				style="position: absolute; top: 40px; left: 180px; width: 100%;">
				<nobr>
					<ispac:htmlTextImageFrame
						property="property(CONTRATACION_DOUE:PUB_ANUNCIO_BOP)" readonly="true"
						readonlyTag="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="10"
						id="SEARCH_CONTRATACION_DOUE:PUB_ANUNCIO_BOP" target="workframe"
						action="selectValue.do?entity=SPAC_TBL_009"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
						<ispac:parameter name="SEARCH_CONTRATACION_DOUE:PUB_ANUNCIO_BOP"
							id="property(CONTRATACION_DOUE:PUB_ANUNCIO_BOP)" property="VALOR" />
					</ispac:htmlTextImageFrame>
				</nobr>
			</div>
			<div id="label_SPAC_EXPEDIENTES:SEP_INTERESADO_PRINCIPAL" style="position: absolute; top: 80px; left: 10px; width: 650px" class="textbar"/>
			HA SIDO PUBLICADO (Señala si se ha publicado)
			<hr class="formbar"/>
			
			<div id="LABEL_CONTRATACION_DOUE:PUBLICADOANUNCIOLICITACION"
				style="position: absolute; top: 70px; left: 10px; width: 220px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DOUE:PUBLICADOANUNCIOLICITACION)" />
				:
			</div>
			<div id="data_CONTRATACION_DOUE:PUBLICADOANUNCIOLICITACION"
				style="position: absolute; top: 70px; left: 240px; width: 100%;">
				<nobr>
					<ispac:htmlTextImageFrame
						property="property(CONTRATACION_DOUE:PUBLICADOANUNCIOLICITACION)" readonly="true"
						readonlyTag="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="10"
						id="SEARCH_CONTRATACION_DOUE:PUBLICADOANUNCIOLICITACION" target="workframe"
						action="selectValue.do?entity=SPAC_TBL_009"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
						<ispac:parameter name="SEARCH_CONTRATACION_DOUE:PUBLICADOANUNCIOLICITACION"
							id="property(CONTRATACION_DOUE:PUBLICADOANUNCIOLICITACION)" property="VALOR" />
					</ispac:htmlTextImageFrame>
				</nobr>
			</div>
			
			<div id="LABEL_CONTRATACION_DOUE:PUBLICADOANUNCIOLICITACIONBOE"
				style="position: absolute; top: 100px; left: 10px; width: 220px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DOUE:PUBLICADOANUNCIOLICITACIONBOE)" />
				:
			</div>
			<div id="data_CONTRATACION_DOUE:PUBLICADOANUNCIOLICITACIONBOE"
				style="position: absolute; top: 100px; left: 240px; width: 100%;">
				<nobr>
					<ispac:htmlTextImageFrame
						property="property(CONTRATACION_DOUE:PUBLICADOANUNCIOLICITACIONBOE)" readonly="true"
						readonlyTag="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="10"
						id="SEARCH_CONTRATACION_DOUE:PUBLICADOANUNCIOLICITACIONBOE" target="workframe"
						action="selectValue.do?entity=SPAC_TBL_009"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
						<ispac:parameter name="SEARCH_CONTRATACION_DOUE:PUBLICADOANUNCIOLICITACIONBOE"
							id="property(CONTRATACION_DOUE:PUBLICADOANUNCIOLICITACIONBOE)" property="VALOR" />
					</ispac:htmlTextImageFrame>
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_DOUE:FECHA_PUBLICACION_ANUN_LIC_DOU"
				style="position: absolute; top: 70px; left: 400px; width: 410px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DOUE:FECHA_PUBLICACION_ANUN_LIC_DOU)" />:
			</div>
			<div id="data_CONTRATACION_DOUE:FECHA_PUBLICACION_ANUN_LIC_DOU"
				style="position: absolute; top: 70px; left: 650px; width: 100%;">
				<nobr> 
					<ispac:htmlTextCalendar
						property="property(CONTRATACION_DOUE:FECHA_PUBLICACION_ANUN_LIC_DOU)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%= buttoncalendar %>' format="dd/mm/yyyy" enablePast="true">
					</ispac:htmlTextCalendar> 
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_DOUE:FECHA_PUBLICACION_ANUN_LIC_BOE"
				style="position: absolute; top: 100px; left: 400px; width: 410px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DOUE:FECHA_PUBLICACION_ANUN_LIC_BOE)" />:
			</div>
			<div id="data_CONTRATACION_DOUE:FECHA_PUBLICACION_ANUN_LIC_BOE"
				style="position: absolute; top: 100px; left: 650px; width: 100%;">
				<nobr> 
					<ispac:htmlTextCalendar
						property="property(CONTRATACION_DOUE:FECHA_PUBLICACION_ANUN_LIC_BOE)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%= buttoncalendar %>' format="dd/mm/yyyy" enablePast="true">
					</ispac:htmlTextCalendar> 
				</nobr>
			</div>
			
			
			
			
			
			
			
			
			
			
			
			<div id="LABEL_CONTRATACION_DOUE:PUBLICADOANUNCIOFORM_DOUE"
				style="position: absolute; top: 130px; left: 10px; width: 250px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DOUE:PUBLICADOANUNCIOFORM_DOUE)" />
				:
			</div>
			<div id="data_CONTRATACION_DOUE:PUBLICADOANUNCIOFORM_DOUE"
				style="position: absolute; top: 130px; left: 260px; width: 100%;">
				<nobr>
					<ispac:htmlTextImageFrame
						property="property(CONTRATACION_DOUE:PUBLICADOANUNCIOFORM_DOUE)" readonly="true"
						readonlyTag="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="10"
						id="SEARCH_CONTRATACION_DOUE:PUBLICADOANUNCIOFORM_DOUE" target="workframe"
						action="selectValue.do?entity=SPAC_TBL_009"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
						<ispac:parameter name="SEARCH_CONTRATACION_DOUE:PUBLICADOANUNCIOFORM_DOUE"
							id="property(CONTRATACION_DOUE:PUBLICADOANUNCIOFORM_DOUE)" property="VALOR" />
					</ispac:htmlTextImageFrame>
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_DOUE:FECHA_PUBLICACION_ANUN_FOR_DOU"
				style="position: absolute; top: 130px; left: 400px; width: 450px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DOUE:FECHA_PUBLICACION_ANUN_FOR_DOU)" />:
			</div>
			<div id="data_CONTRATACION_DOUE:FECHA_PUBLICACION_ANUN_FOR_DOU"
				style="position: absolute; top: 130px; left: 680px; width: 100%;">
				<nobr> 
					<ispac:htmlTextCalendar
						property="property(CONTRATACION_DOUE:FECHA_PUBLICACION_ANUN_FOR_DOU)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%= buttoncalendar %>' format="dd/mm/yyyy" enablePast="true">
					</ispac:htmlTextCalendar> 
				</nobr>
			</div>
			
			<div id="LABEL_CONTRATACION_DOUE:PUBLICADOANUNCIOFORM_BOE"
				style="position: absolute; top: 160px; left: 10px; width: 250px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DOUE:PUBLICADOANUNCIOFORM_BOE)" />
				:
			</div>
			<div id="data_CONTRATACION_DOUE:PUBLICADOANUNCIOFORM_BOE"
				style="position: absolute; top: 160px; left: 260px; width: 100%;">
				<nobr>
					<ispac:htmlTextImageFrame
						property="property(CONTRATACION_DOUE:PUBLICADOANUNCIOFORM_BOE)" readonly="true"
						readonlyTag="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="10"
						id="SEARCH_CONTRATACION_DOUE:PUBLICADOANUNCIOFORM_BOE" target="workframe"
						action="selectValue.do?entity=SPAC_TBL_009"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
						<ispac:parameter name="SEARCH_CONTRATACION_DOUE:PUBLICADOANUNCIOFORM_BOE"
							id="property(CONTRATACION_DOUE:PUBLICADOANUNCIOFORM_BOE)" property="VALOR" />
					</ispac:htmlTextImageFrame>
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_DOUE:FECHA_PUBLICACION_ANUN_FOR_BOE"
				style="position: absolute; top: 160px; left: 400px; width: 450px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DOUE:FECHA_PUBLICACION_ANUN_FOR_BOE)" />:
			</div>
			<div id="data_CONTRATACION_DOUE:FECHA_PUBLICACION_ANUN_FOR_BOE"
				style="position: absolute; top: 160px; left: 680px; width: 100%;">
				<nobr> 
					<ispac:htmlTextCalendar
						property="property(CONTRATACION_DOUE:FECHA_PUBLICACION_ANUN_FOR_BOE)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%= buttoncalendar %>' format="dd/mm/yyyy" enablePast="true">
					</ispac:htmlTextCalendar> 
				</nobr>
			</div>
			
			
			
			
			
			
			
			
			
			
			<div id="LABEL_CONTRATACION_DOUE:PUBLICADOANUNCIOADJ_DOUE"
				style="position: absolute; top: 185px; left: 10px; width: 250px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DOUE:PUBLICADOANUNCIOADJ_DOUE)" />
				:
			</div>
			<div id="data_CONTRATACION_DOUE:PUBLICADOANUNCIOADJ_DOUE"
				style="position: absolute; top: 185px; left: 260px; width: 100%;">
				<nobr>
					<ispac:htmlTextImageFrame
						property="property(CONTRATACION_DOUE:PUBLICADOANUNCIOADJ_DOUE)" readonly="true"
						readonlyTag="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="10"
						id="SEARCH_CONTRATACION_DOUE:PUBLICADOANUNCIOADJ_DOUE" target="workframe"
						action="selectValue.do?entity=SPAC_TBL_009"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
						<ispac:parameter name="SEARCH_CONTRATACION_DOUE:PUBLICADOANUNCIOADJ_DOUE"
							id="property(CONTRATACION_DOUE:PUBLICADOANUNCIOADJ_DOUE)" property="VALOR" />
					</ispac:htmlTextImageFrame>
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_DOUE:FECHA_PUBLICACION_ANUN_ADJ_DOU"
				style="position: absolute; top: 185px; left: 400px; width: 450px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DOUE:FECHA_PUBLICACION_ANUN_ADJ_DOU)" />:
			</div>
			<div id="data_CONTRATACION_DOUE:FECHA_PUBLICACION_ANUN_ADJ_DOU"
				style="position: absolute; top: 185px; left: 680px; width: 100%;">
				<nobr> 
					<ispac:htmlTextCalendar
						property="property(CONTRATACION_DOUE:FECHA_PUBLICACION_ANUN_ADJ_DOU)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%= buttoncalendar %>' format="dd/mm/yyyy" enablePast="true">
					</ispac:htmlTextCalendar> 
				</nobr>
			</div>
			
			<div id="LABEL_CONTRATACION_DOUE:PUBLICADOANUNCIOADJ_BOE"
				style="position: absolute; top: 210px; left: 10px; width: 250px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DOUE:PUBLICADOANUNCIOADJ_BOE)" />
				:
			</div>
			<div id="data_CONTRATACION_DOUE:PUBLICADOANUNCIOADJ_BOE"
				style="position: absolute; top: 210px; left: 260px; width: 100%;">
				<nobr>
					<ispac:htmlTextImageFrame
						property="property(CONTRATACION_DOUE:PUBLICADOANUNCIOADJ_BOE)" readonly="true"
						readonlyTag="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="10"
						id="SEARCH_CONTRATACION_DOUE:PUBLICADOANUNCIOADJ_BOE" target="workframe"
						action="selectValue.do?entity=SPAC_TBL_009"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
						<ispac:parameter name="SEARCH_CONTRATACION_DOUE:PUBLICADOANUNCIOADJ_BOE"
							id="property(CONTRATACION_DOUE:PUBLICADOANUNCIOADJ_BOE)" property="VALOR" />
					</ispac:htmlTextImageFrame>
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_DOUE:FECHA_PUBLICACION_ANUN_ADJ_BO"
				style="position: absolute; top: 210px; left: 400px; width: 450px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DOUE:FECHA_PUBLICACION_ANUN_ADJ_BO)" />:
			</div>
			<div id="data_CONTRATACION_DOUE:FECHA_PUBLICACION_ANUN_ADJ_BO"
				style="position: absolute; top: 210px; left: 680px; width: 100%;">
				<nobr> 
					<ispac:htmlTextCalendar
						property="property(CONTRATACION_DOUE:FECHA_PUBLICACION_ANUN_ADJ_BO)"
						readonly="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="14" maxlength="10"
						image='<%= buttoncalendar %>' format="dd/mm/yyyy" enablePast="true">
					</ispac:htmlTextCalendar> 
				</nobr>
			</div>
			
			<div id="label_SPAC_EXPEDIENTES:SEP_INTERESADO_PRINCIPAL" style="position: absolute; top: 250px; left: 1px; width: 450px;" class="textbar"/>
			INFORMACIÓN A RELLENAR PARA EL D.O.U.E
			<hr class="formbar"/>
			
			<div id="label_CONTRATACION_DOUE:DIRECTIVA" style="position: absolute; top: 40px; left: 1px; width: 450px;" class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DOUE:DIRECTIVA)" />:
			</div>
			<div id="data_CONTRATACION_DOUE:DIRECTIVA" style="position: absolute; top: 36px; left: 180px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(CONTRATACION_DOUE:DIRECTIVA)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="1" cols="80" 
					id="SEARCH_CONTRATACION_DOUE_DIRECTIVA" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_DIRECTIVA" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CONTRATACION_DOUE_DIRECTIVA" id="property(CONTRATACION_DOUE:DIRECTIVA)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			
			<div id="label_CONTRATACION_DOUE:ADJPYME"
				style="position: absolute; top: 75px; left: 1px; width: 160px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DOUE:ADJPYME)" />
				:
			</div>
			<div id="data_CONTRATACION_DOUE:ADJPYME"
				style="position: absolute; top: 72px; left: 180px; width: 100%;">
				<nobr>
					<ispac:htmlTextImageFrame
						property="property(CONTRATACION_DOUE:ADJPYME)" readonly="true"
						readonlyTag="false" propertyReadonly="readonly" styleClass="input"
						styleClassReadonly="inputReadOnly" size="10"
						id="SEARCH_CONTRATACION_DOUE_ADJPYME" target="workframe"
						action="selectValue.do?entity=SPAC_TBL_009"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" width="640" height="480">
						<ispac:parameter name="SEARCH_CONTRATACION_DOUE_ADJPYME"
							id="property(CONTRATACION_DOUE:ADJPYME)" property="VALOR" />
					</ispac:htmlTextImageFrame>
				</nobr>
			</div>
			<div id="label_SPAC_EXPEDIENTES:SEP_INTERESADO_PRINCIPAL" style="position: absolute; top: 110px; left: 1px; width: 300px;" class="textbar"/>
			Dirección Adjudicatario
			
			<div id="label_CONTRATACION_DOUE:DIR_POSTAL"
				style="position: absolute; top: 35px; left: 1px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DOUE:DIR_POSTAL)" />:
			</div>
			<div id="data_CONTRATACION_DOUE:DIR_POSTAL"
				style="position: absolute; top: 35px; left: 180px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DOUE:DIR_POSTAL)"
					readonly="false" propertyReadonly="readonly" styleClass="input"
					styleClassReadonly="inputReadOnly" size="80" maxlength="300">
				</ispac:htmlText>
			</div>
			<div id="label_CONTRATACION_DOUE:PAIS"
				style="position: absolute; top: 65px; left: 1px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(CONTRATACION_DOUE:PAIS)" />:
			</div>
			<div id="data_CONTRATACION_DOUE:PAIS"
				style="position: absolute; top: 65px; left: 180px; width: 100%;">
				<ispac:htmlText
					property="property(CONTRATACION_DOUE:PAIS)"
					readonly="false" propertyReadonly="readonly" value="ES - SPAIN" styleClass="input"
					styleClassReadonly="inputReadOnly" size="20" maxlength="300">
				</ispac:htmlText>
			</div>
			
			<div id="label_CONTRATACION_DOUE:CIUDAD"
				style="position: absolute; top: 95px; left: 1px; width: 250px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_DOUE:CIUDAD)" />
				:
			</div>
			<div id="data_CONTRATACION_DOUE:CIUDAD"
				style="position: absolute; top: 95px; left: 180px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(CONTRATACION_DOUE:CIUDAD)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="1" cols="80" 
					id="SEARCH_CONTRATACION_DOUE:CIUDAD" target="workframe"
					action="selectListadoCodicePliego.do?atributo=COD_LOCALIZACIONGEOGRAFICA" 
					image="img/search-mg.gif" jsDelete="deleteAdjudicado" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CONTRATACION_DOUE:CIUDAD" id="property(CONTRATACION_DOUE:CIUDAD)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			
		</div>
	</ispac:tab>
</ispac:tabs>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c1"%>
<c1:set var="aux0">
	<bean:write name="defaultForm"
		property="entityApp.label(DPCR_CODICE_PLIEGOS:DPCR_CODICE_PLIEGOS)" />
</c1:set><jsp:useBean id="aux0" type="java.lang.String" />
<ispac:tabs>
	<ispac:tab title='<%=aux0%>'>
		<div id="dataBlock_DPCR_CODICE_PLIEGOS" style="position: relative; height: 340px; width: 600px">
			
			
			<div id="label_DPCR_CODICE_PLIEGOS:COD_TIPO_CONTRATO"
				style="position: absolute; top: 10px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(DPCR_CODICE_PLIEGOS:TIPO_CONTRATO)" />:
			</div>
			<div id="data_DPCR_CODICE_PLIEGOS:COD_TIPO_CONTRATO" style="position: absolute; top: 10px; left: 220px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(DPCR_CODICE_PLIEGOS:TIPO_CONTRATO)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="2" cols="80" 
					id="SEARCH_CODICE_PLIEGOS_TIPO_CONTRATO" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_TIPO_CONTRATO" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CODICE_PLIEGOS_TIPO_CONTRATO" id="property(DPCR_CODICE_PLIEGOS:TIPO_CONTRATO)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			
			<div id="label_DPCR_CODICE_PLIEGOS:COD_CONTRATO_SUMIN"
				style="position: absolute; top: 40px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(DPCR_CODICE_PLIEGOS:CONTRATO_SUMIN)" />:
			</div>
			<div id="data_DPCR_CODICE_PLIEGOS:COD_CONTRATO_SUMIN" style="position: absolute; top: 40px; left: 220px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(DPCR_CODICE_PLIEGOS:CONTRATO_SUMIN)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="2" cols="80" 
					id="SEARCH_CODICE_PLIEGOS_CONTRATO_SUMIN" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_CONTRATO_SUMIN" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CODICE_PLIEGOS_CONTRATO_SUMIN" id="property(DPCR_CODICE_PLIEGOS:CONTRATO_SUMIN)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			
			<div id="label_DPCR_CODICE_PLIEGOS:COD_CONTRATO_OBRAS"
				style="position: absolute; top: 70px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(DPCR_CODICE_PLIEGOS:CONTRATO_OBRAS)" />:
			</div>
			<div id="data_DPCR_CODICE_PLIEGOS:COD_CONTRATO_OBRAS" style="position: absolute; top: 70px; left: 220px; width: 100%;">
				<nobr>
					<ispac:htmlTextMultivalueImageFrame
						property="propertyMultivalue(DPCR_CODICE_PLIEGOS:CONTRATO_OBRAS)"
						propertyDestination="propertyMultivalue(DPCR_CODICE_PLIEGOS:CONTRATO_OBRAS)"
						readonly="true" readonlyTag="false" propertyReadonly="readonly"
						styleClass="input" styleClassReadonly="inputReadOnly" size="77"
						id="SEARCH_DPCR_CODICE_PLIEGOS_CONTRATO_OBRAS" target="workframe"
						action="selectListadoCodicePliego.do?atributo=COD_CONTRATO_OBRAS"
						image="img/search-mg.gif" titleKeyLink="title.link.data.selection"
						imageDelete="img/borrar.gif"
						titleKeyImageDelete="title.delete.data.selection"
						styleClassDeleteLink="tdlink"
						confirmDeleteKey="msg.delete.data.selection" showDelete="true"
						showFrame="true" divWidth="500" divHeight="50">
						<ispac:parameterMultivalue name="SEARCH_DPCR_CODICE_PLIEGOS_CONTRATO_OBRAS" id="propertyMultivalue(DPCR_CODICE_PLIEGOS:CONTRATO_OBRAS)" 
						setMethod="id" property="SUSTITUTO" propertyDestination="propertyMultivalue(DPCR_CODICE_PLIEGOS:CONTRATO_OBRAS)" />
					</ispac:htmlTextMultivalueImageFrame> 
				</nobr>
			</div>
			
			<div id="label_DPCR_CODICE_PLIEGOS:COD_CONTRATO_SECTPUB"
				style="position: absolute; top: 160px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(DPCR_CODICE_PLIEGOS:CONTRATO_SECTPUB)" />:
			</div>
			<div id="data_DPCR_CODICE_PLIEGOS:COD_CONTRATO_SECTPUB" style="position: absolute; top: 160px; left: 220px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(DPCR_CODICE_PLIEGOS:CONTRATO_SECTPUB)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="2" cols="80" 
					id="SEARCH_CODICE_PLIEGOS_CONTRATO_SECTPUB" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_CONTRATO_SECTPUB" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CODICE_PLIEGOS_CONTRATO_SECTPUB" id="property(DPCR_CODICE_PLIEGOS:CONTRATO_SECTPUB)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			
			<div id="label_DPCR_CODICE_PLIEGOS:COD_PROCLICIT"
				style="position: absolute; top: 190px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(DPCR_CODICE_PLIEGOS:PROCLICIT)" />:
			</div>
			<div id="data_DPCR_CODICE_PLIEGOS:COD_PROCLICIT" style="position: absolute; top: 190px; left: 220px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(DPCR_CODICE_PLIEGOS:PROCLICIT)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="2" cols="80" 
					id="SEARCH_CODICE_PLIEGOS_PROCLICIT" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_PROCLICIT" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CODICE_PLIEGOS_PROCLICIT" id="property(DPCR_CODICE_PLIEGOS:PROCLICIT)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			
			<div id="label_DPCR_CODICE_PLIEGOS:COD_PROC_CONT_SECPUB"
				style="position: absolute; top: 220px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(DPCR_CODICE_PLIEGOS:PROC_CONT_SECPUB)" />:
			</div>
			<div id="data_DPCR_CODICE_PLIEGOS:COD_PROC_CONT_SECPUB" style="position: absolute; top: 220px; left: 220px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(DPCR_CODICE_PLIEGOS:PROC_CONT_SECPUB)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="2" cols="80" 
					id="SEARCH_CODICE_PLIEGOS_PROC_CONT_SECPUB" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_PROC_CONT_SECPUB" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CODICE_PLIEGOS_PROC_CONT_SECPUB" id="property(DPCR_CODICE_PLIEGOS:PROC_CONT_SECPUB)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			
			<div id="label_DPCR_CODICE_PLIEGOS:COD_CRITERIO_CODIGO"
				style="position: absolute; top: 250px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(DPCR_CODICE_PLIEGOS:CRITERIO_CODIGO)" />:
			</div>
			<div id="data_DPCR_CODICE_PLIEGOS:COD_CRITERIO_CODIGO" style="position: absolute; top: 250px; left: 220px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(DPCR_CODICE_PLIEGOS:CRITERIO_CODIGO)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="2" cols="80" 
					id="SEARCH_CODICE_PLIEGOS_CRITERIO_CODIGO" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_CRITERIO_CODIGO" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CODICE_PLIEGOS_CRITERIO_CODIGO" id="property(DPCR_CODICE_PLIEGOS:CRITERIO_CODIGO)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
						
				
			<div id="label_DPCR_CODICE_PLIEGOS:COD_TIPO_ADJUDICACION"
				style="position: absolute; top: 280px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(DPCR_CODICE_PLIEGOS:TIPO_ADJUDICACION)" />:
			</div>
			<div id="data_DPCR_CODICE_PLIEGOS:COD_TIPO_ADJUDICACION" style="position: absolute; top: 280px; left: 220px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(DPCR_CODICE_PLIEGOS:TIPO_ADJUDICACION)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="2" cols="80" 
					id="SEARCH_CODICE_PLIEGOS_TIPO_ADJUDICACION" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_TIPO_ADJUDICACION" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CODICE_PLIEGOS_TIPO_ADJUDICACION" id="property(DPCR_CODICE_PLIEGOS:TIPO_ADJUDICACION)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
			
			
			<div id="label_DPCR_CODICE_PLIEGOS:COD_ORG_CONTRAT"
				style="position: absolute; top: 310px; left: 10px; width: 210px;"
				class="formsTitleB">
				<bean:write name="defaultForm" property="entityApp.label(DPCR_CODICE_PLIEGOS:ORG_CONTRAT)" />:
			</div>
			<div id="data_DPCR_CODICE_PLIEGOS:COD_ORG_CONTRAT" style="position: absolute; top: 310px; left: 220px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(DPCR_CODICE_PLIEGOS:ORG_CONTRAT)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="2" cols="80" 
					id="SEARCH_CODICE_PLIEGOS_ORG_CONTRAT" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_ORG_CONTRAT" 
					image="img/search-mg.gif" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CODICE_PLIEGOS_ORG_CONTRAT" id="property(DPCR_CODICE_PLIEGOS:ORG_CONTRAT)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>

		</div>
	</ispac:tab>
</ispac:tabs>
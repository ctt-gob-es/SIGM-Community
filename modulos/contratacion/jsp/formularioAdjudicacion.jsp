<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c1"%>
<c1:set var="aux0">
	<bean:write name="defaultForm"
		property="entityApp.label(CONTRATACION_ADJUDICACION:CONTRATACION_ADJUDICACION)" />
</c1:set><jsp:useBean id="aux0" type="java.lang.String" />
<ispac:tabs>
	<ispac:tab title='<%=aux0%>'>
		<div id="dataBlock_CONTRATACION_ADJUDICACION"
			style="position: relative; height: 55px; width: 600px">
			
			<div id="label_CONTRATACION_ADJUDICACION:RES_LICITACION"
				style="position: absolute; top: 10px; left: 10px; width: 250px;"
				class="formsTitleB">
				<bean:write name="defaultForm"
					property="entityApp.label(CONTRATACION_ADJUDICACION:RES_LICITACION)" />
				:
			</div>
			<div id="data_CONTRATACION_ADJUDICACION:RES_LICITACION"
				style="position: absolute; top: 10px; left: 250px; width: 100%;">
				<nobr>
					<ispac:htmlTextareaImageFrame property="property(CONTRATACION_ADJUDICACION:RES_LICITACION)" readonly="true" readonlyTag="false" 
					propertyReadonly="readonly" styleClass="input" styleClassReadonly="inputReadOnly" rows="1" cols="80" 
					id="SEARCH_CONTRATACION_ADJUDICACION:RES_LICITACION" target="workframe" action="selectListadoCodicePliego.do?atributo=COD_RESULTADO_LICITACION" 
					image="img/search-mg.gif" jsDelete="deleteAdjudicado" titleKeyLink="select.rol" imageDelete="img/borrar.gif" titleKeyImageDelete="title.delete.data.selection" 
					styleClassDeleteLink="tdlink" confirmDeleteKey="msg.delete.data.selection" showDelete="true" showFrame="true" width="640" height="480" 
					tabindex="113">
						<ispac:parameter name="SEARCH_CONTRATACION_ADJUDICACION:RES_LICITACION" id="property(CONTRATACION_ADJUDICACION:RES_LICITACION)" property="SUSTITUTO" />
					</ispac:htmlTextareaImageFrame>
				</nobr>
			</div>
		</div>
	</ispac:tab>
</ispac:tabs>

<script language='JavaScript' type='text/javascript'><!--
function  deleteAdjudicado() {
	document.forms[0].elements['property(CONTRATACION_ADJUDICACION:RES_LICITACION)'].value = "";
}
//--></script>
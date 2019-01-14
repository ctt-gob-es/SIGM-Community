<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac" %>
<%@ taglib uri="/WEB-INF/struts-tiles.tld" prefix="tiles" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>

<ispac:hasFunction var="editionMode" functions="FUNC_INV_DOCTYPES_EDIT, FUNC_INV_TEMPLATES_EDIT" />

<c:set var="activeScreen" value="${sessionScope['ATTR_ACTIVE_SCREEN']}"/>

<bean:parameter id="typedocument" name="type"/>

<script>
function editTemplate()
{
	<c:if test="${!empty NUM_PROC}">
		jConfirm('<bean:message key="form.template.edicionTemplateEspecifica"/>', '<bean:message key="common.confirm"/>' , '<bean:message key="common.message.ok"/>' , '<bean:message key="common.message.cancel"/>', function(r) {
			if(r) {
	</c:if>
	
	var url = '<%= request.getContextPath()%>' + '/editTemplate.do?template=' + '<bean:write name="uploadForm" property="key"/>';
	        
	document.forms[0].target = "editTemplateFrame";
	document.forms[0].action = url;
	document.forms[0].submit();

	<c:if test="${!empty NUM_PROC}">
		    }
		});	
	</c:if>
}

function saveTemplateRepositorioComun() {
	try {
		var url = '<%= request.getContextPath()%>' + '/storeTemplateRepositorioComun.do?type=' + '<%=typedocument%>';
	            
		document.forms[0].action = url;
  		document.forms[0].submit();
  	} catch(e) {
  		jAlert('<bean:message key="exception.template.document.invalidPathFile"/>', '<bean:message key="common.alert"/>', '<bean:message key="common.message.ok"/>' , '<bean:message key="common.message.cancel"/>');
  	}
}

function showTemplatesRepositorioComun() {
	var url = '<%=request.getContextPath()%>' + '/showTemplatesRepositorioComunList.do?type=' + '<%=typedocument%>';
    
	document.forms[0].action = url;
	document.forms[0].submit();
}

</script>

<div id="navmenutitle">
	<bean:message key="form.template.mainTitle"/>
</div>

<%-- BREAD CRUMBS --%>
<logic:present name="BreadCrumbs">
<tiles:insert page="/ispac/common/tiles/breadCrumbsTile.jsp" ignore="true" flush="false"/>
</logic:present>

<div id="navmenu">
	<ul class="actionsMenuList">
		<logic:equal name="uploadForm" property="property(ID)" value="-1">
			<c:if test="${editionMode}">
			<li>
				<a href="javascript:saveTemplateRepositorioComun()">
					<nobr><bean:message key="forms.button.accept"/></nobr>
				</a>
			</li>
			</c:if>
			<li>
				<a href="javascript:showTemplatesRepositorioComun()">
					<nobr><bean:message key="forms.button.cancel"/></nobr>
				</a>
		  	</li>
		</logic:equal>
		<logic:notEqual name="uploadForm" property="property(ID)" value="-1">
			<c:if test="${editionMode}">
			<li>
				<a href="javascript:saveTemplateRepositorioComun()">
					<nobr><bean:message key="forms.button.save"/></nobr>
				</a>
			</li>
			</c:if>
			<li>
				<a href="javascript:showTemplatesRepositorioComun()">
					<nobr><bean:message key="forms.button.close"/></nobr>
				</a>
		  	</li>	  	
			<!--<c:if test="${editionMode}">
			<li>
				<a href='javascript:editTemplate()'>
					<bean:message key="forms.button.edit"/>
				</a>
			</li>			
			</c:if>-->
		</logic:notEqual>
	</ul>
</div>

<html:form action='/showCTEntityUp.do' enctype="multipart/form-data">

	<%--
		Nombre de Aplicación.
		 Necesario para realizar la validación
	--%>
	<html:hidden property="entityAppName" value="EditCTTemplate"/> 
	<!-- Identificador de la entidad -->
	<html:hidden property="entity"/>
	<!-- Identificador del registro -->
	<html:hidden property="key"/>

	<table cellpadding="1" cellspacing="0" border="0" width="100%">
		<tr>
			<td>
				<table class="box" width="100%" border="0" cellspacing="1" cellpadding="0">
					<!-- FORMULARIO -->
					<tr>
						<td class="blank">
							<table width="100%" border="0" cellspacing="2" cellpadding="2">
								<tr>
									<td height="5px" colspan="3"></td>
								</tr>
								<tr>
									<td width="8"><img height="1" width="8px" src='<ispac:rewrite href="img/pixel.gif"/>'/></td>
									<td width="100%">
										<table width="100%" border="0" class="formtable" cellspacing="0" cellpadding="0">
											<tr>
												<td>
												<logic:messagesPresent>
													<div id="formErrorsMessage">
														<bean:message key="forms.errors.formErrorsMessage"/>
													</div>	
												</logic:messagesPresent>
												</td>
											</tr>
											<tr>
												<td>
													<div style="display:block" id="page1">
														<table border="0" cellspacing="0" cellpadding="0" align="center" width="90%">
															<tr>
																<td>
																	<table border="0" cellspacing="0" cellpadding="0" width="100%">																	
																		<tr>
																			<td colspan="2"><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" height="8px"/></td>
																		</tr>
																		<tr>
																			<td class="formsTitle">
																				<ispac:tooltipLabel labelKey="form.template.propertyLabel.documento" tooltipKey="form.template.propertyLabel.documento.tooltip"/>
																			</td>
																			<td>
																				&nbsp;&nbsp;<html:file property="uploadFile" styleClass="input"/>
																				<div id="formErrors">
																						<html:errors property="uploadFile"/>
																				</div>
																			</td>
																		</tr>																		
																		<tr>
																			<td colspan="2"><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" height="8px"/></td>
																		</tr>
																	</table>
																</td>
															</tr>
														</table>
													</div>
												</td>
											</tr>
											<tr>
												<td height="15px"><img src='<ispac:rewrite href="img/pixel.gif"/>'/></td>
											</tr>
										</table>
									</td>
									<td width="8"><img height="1" width="8px" src='<ispac:rewrite href="img/pixel.gif"/>'/></td>
								</tr>
								<tr>
									<td height="5px" colspan="3"><img src='<ispac:rewrite href="img/pixel.gif"/>' height="5px"/></td>
								</tr>
							</table>
						</td>
					</tr>
				</table>
			</td>
		</tr>
	</table>
</html:form>

<iframe src='' id='editTemplateFrame' name='editTemplateFrame' style='visibility:visible;height:0px;margin:0px;padding:0px;border:none;' allowtransparency='true'></iframe>
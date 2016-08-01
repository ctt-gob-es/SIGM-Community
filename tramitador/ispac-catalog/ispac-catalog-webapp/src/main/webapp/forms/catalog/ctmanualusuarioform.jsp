<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ page import="ieci.tdw.ispac.ispaclib.dao.cat.CTManualUsuarioDAO" %>

<script>
	var controlPaso = true;
	var controlFile = "";
			
	function showResponsables(){
	   if(document.getElementById("responsables")!=null){
			document.getElementById("responsables").style.display='block';
		}
		if(document.getElementById("tituloResponsables")!=null){
			document.getElementById("tituloResponsables").style.display='block';
		}
		if(document.getElementById("accionesResponsables")!=null){
			document.getElementById("accionesResponsables").style.display='block';
		}
	}
	function hideResponsables(){
		if(document.getElementById("responsables")!=null){
			document.getElementById("responsables").style.display='none';
		}
		if(document.getElementById("tituloResponsables")!=null){
			document.getElementById("tituloResponsables").style.display='none';
		}
		if(document.getElementById("accionesResponsables")!=null){
			document.getElementById("accionesResponsables").style.display='none';
		}
	}
	function ispac_loadFile(){
		if (controlPaso) {
			if (document.getElementById("FileUp").value != "" && document.getElementById("FileUp").value != controlFile) {
				controlFile = document.getElementById("FileUp").value;
				controlPaso = false;
				document.forms[0].action ='<%= request.getContextPath() + "/loadFile.do"%>';
				document.forms[0].submit();
			}
		}
		else{
			controlFile = document.getElementById("FileUp").value;
			controlPaso = true;
		}
	}
	// Responsables asociados al informe
	function deletePermission(myform) {
    	var checked = false;
		if (typeof myform.uids == 'undefined'){
			jAlert('<bean:message key="catalog.report.alert.noResponsibles"/>', '<bean:message key="common.alert"/>', '<bean:message key="common.message.ok"/>' , '<bean:message key="common.message.cancel"/>');
			return;
		}

		mymultibox = myform.uids;
		
		// cuando tenemos un checkbox
		if (typeof mymultibox.length == 'undefined'){
      		if (mymultibox.checked)
      			checked = true;
      	}
      	else{
	    	for (var i=0; i < mymultibox.length; i++)
		        if (myform[i].checked)
		        	checked = true;
	    }
	    if (checked)
	    	myform.submit();
	    else{
	   		 jAlert('<bean:message key="catalog.report.alert.selResponsibles"/>', '<bean:message key="common.alert"/>', '<bean:message key="common.message.ok"/>' , '<bean:message key="common.message.cancel"/>');
		}
	}
		
</script>

<div id="navmenutitle">
	<bean:message key="form.manualusuario.mainTitle"/>
</div>

<div id="navSubMenuTitle">
</div>

<div id="navmenu" >
	<c:set var="tipoAsociativo"></c:set>
	<jsp:useBean id="tipoAsociativo" type="java.lang.String"></jsp:useBean>
	<logic:equal name="uploadForm" property="property(TIPO)"  value="2" >
		<c:set var="tipoAsociativo">true</c:set>
	</logic:equal>
	<logic:equal name="uploadForm" property="property(TIPO)"  value="4" >
		<c:set var="tipoAsociativo" >true</c:set>
	</logic:equal>
	
	<c:url value="showComponentsUseList.do" var="_inUse">
			<c:param name="entityId">
				<bean:write name="uploadForm" property="entity" />
			</c:param>
			<c:param name="regId">
				<bean:write name="uploadForm" property="key" />
			</c:param>
	</c:url>
	<ul class="actionsMenuList">
		<logic:equal name="uploadForm" property="key" value="-1">
			<ispac:hasFunction functions="FUNC_COMP_REPORTS_EDIT">
			<li>
				<a href="javascript:submit('<%= request.getContextPath() + "/storeCTManualUsuario.do"%>')">
					<nobr><bean:message key="forms.button.accept"/></nobr>
				</a>
			</li>
			</ispac:hasFunction>
			<li>
				<a href='<%=request.getContextPath() + "/showCTManualesUsuarioList.do"%>'>
					<nobr><bean:message key="forms.button.cancel"/></nobr>
				</a>
			</li>
		</logic:equal>
		<logic:notEqual name="uploadForm" property="key" value="-1">
			<ispac:hasFunction functions="FUNC_COMP_REPORTS_EDIT">
			<li>
				<a href="javascript:submit('<%= request.getContextPath() + "/storeCTManualUsuario.do"%>')">
					<nobr><bean:message key="forms.button.save"/></nobr>
				</a>
			</li>
			</ispac:hasFunction>
			<li>
				<a href='<%=request.getContextPath() + "/showCTManualesUsuarioList.do"%>'>
					<nobr><bean:message key="forms.button.close"/></nobr>
				</a>
			</li>
		
			<c:if test='${tipoAsociativo}'>
			<li>
				<a href="javascript:submit('<c:out value="${_inUse}"/>')">
					<nobr><bean:message key="forms.button.use"/></nobr>
				</a>		
			</li>
			</c:if>
			
			<ispac:hasFunction functions="FUNC_COMP_REPORTS_EDIT">
		  	<li>
				<a href="javascript:query('<%= request.getContextPath() + "/deleteManualUsuario.do"%>', '<bean:message key="message.deleteConfirm"/>','<bean:message key="common.confirm"/>', '<bean:message key="common.message.ok"/>','<bean:message key="common.message.cancel"/>')">
					<nobr><bean:message key="forms.button.delete"/></nobr>
				</a>
		  	</li>
		  	</ispac:hasFunction>
		  	<li>
		 	<c:url value="getManualUsuario.do" var="_link">
					<c:param name="manualUsuarioId">
						<bean:write name="uploadForm" property="key" />
					</c:param>
			</c:url>
			<a href='<c:out value="${_link}"/>' ><bean:message key="form.manualusuario.descargar"/></a>
		  	</li>
	  	</logic:notEqual>
	</ul>
</div>
<html:form action='/showCTEntityUp.do' enctype="multipart/form-data">
	<%--
		Nombre de Aplicación.
		 Necesario para realizar la validación
 	--%>
	<html:hidden property="entityAppName"/>
	<!-- Identificador de la entidad -->
	<html:hidden property="entity"/>
	<!-- Identificador del registro -->
	<html:hidden property="key"/>
	<!-- Registro de solo lectura-->
	<html:hidden property="readonly"/>
	<!-- Registro de distribución -->
	<html:hidden property="property(ID)"/>
	
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
																	<table border="0" cellspacing="0" cellpadding="0">
																	<logic:notEqual name="uploadForm" property="key" value="-1">
																		<tr>
																			<td height="20" class="formsTitle">
																				<ispac:tooltipLabel labelKey="form.manualusuario.propertyLabel.id" tooltipKey="form.manualusuario.propertyLabel.id.tooltip"/>
																			</td>
																			<td height="20">
																				&nbsp;&nbsp;<html:text property="property(ID)" styleClass="inputReadOnly" size="5" readonly="true" maxlength="10"/>
																				<div id="formErrors">
																					<html:errors property="property(ID)"/>
																				</div>
																			</td>
																		</tr>
																	</logic:notEqual>
																		<tr>
																			<td colspan="2"><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" height="8px"/></td>
																		</tr>
																		<tr>
																			<td height="20" class="formsTitle">
																				<ispac:tooltipLabel labelKey="form.manualusuario.propertyLabel.name" tooltipKey="form.manualusuario.propertyLabel.name.tooltip"/>
																			</td>
																			<td height="20">
																				&nbsp;&nbsp;<html:text property="property(NOMBRE)" styleClass="inputSelectS" readonly="false" maxlength="250"/>&nbsp; <bean:message key="catalog.data.obligatory"/>
																				<div id="formErrors">
																					<html:errors property="property(NOMBRE)"/>
																				</div>
																			</td>
																		</tr>
																		<tr>
																			<td colspan="2"><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" height="8px"/></td>
																		</tr>
																		<tr>
																			<td height="20" class="formsTitle" style="vertical-align:top">
																				<ispac:tooltipLabel labelKey="form.manualusuario.propertyLabel.description" tooltipKey="form.manualusuario.propertyLabel.description.tooltip"/>
																			</td>
																			<td height="20">
																				&nbsp;&nbsp;<html:textarea property="property(DESCRIPCION)" styleClass="inputSelectS" readonly="false" rows="6" cols="40"
																					styleId="desc" onkeypress="javascript:maxlength('desc', 255)"/>
																				<div id="formErrors">
																					<html:errors property="property(DESCRIPCION)"/>
																				</div>
																			</td>
																		</tr>
																		<tr>
																			<td colspan="2"><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" height="8px"/></td>
																		</tr>
																		
																		<tr>
																			<td height="20" class="formsTitle">
																				<ispac:tooltipLabel labelKey="form.manualusuario.propertyLabel.type" tooltipKey="form.manualusuario.propertyLabel.description.tooltip"/>
																			</td>
																			<td height="20">
																				&nbsp;&nbsp;
																					<html:select styleClass="input" property="property(TIPO)" >
																						<html:option value="1"><bean:message key="form.manualusuario.propertyLabel.generico"/></html:option>
																						<html:option value="2"><bean:message key="form.manualusuario.propertyLabel.especifico"/></html:option>
																						<html:option value="3"><bean:message key="form.manualusuario.propertyLabel.global"/></html:option>
																						<html:option value="4"><bean:message key="form.manualusuario.propertyLabel.busqueda"/></html:option>
																					</html:select>
																				<div id="formErrors">
																					<html:errors property="property(TIPO)"/>
																				</div>
																			</td>
																		</tr>
																		<tr>
																			<td colspan="2"><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" height="8px"/></td>
																		</tr>
																		
																		<logic:notEqual name="uploadForm" property="key" value="-1">
																		<tr>
																			<td height="20" class="formsTitle">
																				<ispac:tooltipLabel labelKey="form.manualusuario.propertyLabel.date" tooltipKey="form.manualusuario.propertyLabel.date.tooltip"/>
																			</td>
																			<td height="20">
																				&nbsp;&nbsp;<html:text property="property(FECHA)" styleClass="inputReadOnly" size="25" readonly="true" maxlength="10"/>
																			</td>
																		</tr>
																		<tr>
																			<td colspan="2"><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" height="8px"/></td>
																		</tr>
																		</logic:notEqual>
																		<tr>
																			<td height="20" class="formsTitle">
																				<ispac:tooltipLabel labelKey="form.manualusuario.propertyLabel.version" tooltipKey="form.manualusuario.propertyLabel.version.tooltip"/>
																			</td>
																			<td height="20">
																				&nbsp;&nbsp;<html:text property="property(VERSION)" styleClass="input" readonly="false" size="5" maxlength="2"/>
																				<div id="formErrors">
																					<html:errors property="property(VERSION)"/>
																				</div>
																			</td>
																		</tr>
																		<tr>
																			<td colspan="2"><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" height="8px"/></td>
																		</tr>
																		<tr>
																			<td height="20" class="formsTitle" style="vertical-align:top">
																				<ispac:tooltipLabel labelKey="form.manualusuario.propertyLabel.documento" tooltipKey="form.manualusuario.propertyLabel.documento.tooltip"/>
																			</td>
																			<td height="20">
																				&nbsp;&nbsp;<html:file styleClass="input" property="uploadFile" styleId="FileUp"/><br>
																				<div id="formErrors">
																					<html:errors property="uploadFile"/>
																				</div>
																			</td>
																		</tr>
																		<tr>
																			<td colspan="2"><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" height="8px"/></td>
																		</tr>
																		<tr>
																			<td height="20" class="formsTitle">
																				<ispac:tooltipLabel labelKey="form.manualusuario.propertyLabel.url" tooltipKey="form.manualusuario.propertyLabel.url.tooltip"/>
																			</td>
																			<td height="20">
																				&nbsp;&nbsp;<html:text property="property(URL)" styleClass="inputSelectS" readonly="false" maxlength="250"/>
																				<div id="formErrors">
																					<html:errors property="property(URL)"/>
																				</div>
																			</td>
																		</tr>
																		<tr>
																			<td colspan="2"><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" height="8px"/></td>
																		</tr>	
																		
																		<tr>
																		 	<td>
																				<html:radio  property="property(VISIBILIDAD)" value="1">
																				<ispac:tooltipLabel labelKey="catalog.manualusuario.visibilidad.publica" tooltipKey="catalog.manualusuario.visibilidad.publica.tooltip"/>
																				</html:radio>
																			</td>
																			<td>
																				<html:radio   property="property(VISIBILIDAD)" value="0">
																				<ispac:tooltipLabel labelKey="catalog.manualusuario.visibilidad.restringida" tooltipKey="catalog.manualusuario.visibilidad.restringida.tooltip"/>
																				</html:radio>
																			</td>
																		</tr>
																		<tr>
																			<td colspan="2"><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" height="8px"/></td>
																		</tr>																	
																	</html:form>
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

																	

	
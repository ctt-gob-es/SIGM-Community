<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ page import="ieci.tdw.ispac.ispaclib.dao.cat.CTMensajeDAO" %>

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
	<bean:message key="form.mensajes.mainTitle"/>
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
				<a href="javascript:submit('<%= request.getContextPath() + "/storeMensaje.do"%>')">
					<nobr><bean:message key="forms.button.accept"/></nobr>
				</a>
			</li>
			</ispac:hasFunction>
			<li>
				<a href='<%=request.getContextPath() + "/showCTMantenimientoMensajesList.do"%>'>
					<nobr><bean:message key="forms.button.cancel"/></nobr>
				</a>
			</li>
		</logic:equal>
		<logic:notEqual name="uploadForm" property="key" value="-1">
			<ispac:hasFunction functions="FUNC_COMP_REPORTS_EDIT">
			<li>
				<a href="javascript:submit('<%= request.getContextPath() + "/storeMensaje.do"%>')">
					<nobr><bean:message key="forms.button.save"/></nobr>
				</a>
			</li>
			</ispac:hasFunction>
			<li>
				<a href='<%=request.getContextPath() + "/showCTMantenimientoMensajesList.do"%>'>
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
				<a href="javascript:query('<%= request.getContextPath() + "/deleteMensaje.do?method=deleteMensaje"%>', '<bean:message key="message.deleteConfirm"/>','<bean:message key="common.confirm"/>', '<bean:message key="common.message.ok"/>','<bean:message key="common.message.cancel"/>')">
					<nobr><bean:message key="forms.button.delete"/></nobr>
				</a>
		  	</li>
		  	</ispac:hasFunction>
	  	</logic:notEqual>
	</ul>
</div>
<html:form action='/showCTEntityUp.do' >
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
																	
																	<c:choose>
																		<c:when test="${param.masivo == true}">
																			<html:hidden property="property(MASIVO)" value="true"/>
																			<tr>
																				<td height="20" class="formsTitle">
																					<ispac:tooltipLabel labelKey="form.mensaje.propertyLabel.password" tooltipKey="form.mensaje.propertyLabel.password.tooltip"/>
																				</td>
																				<td height="20">
																					&nbsp;&nbsp;<html:password property="property(PASSADM)" styleClass="inputSelectS" redisplay="true"/>&nbsp; <bean:message key="catalog.data.obligatory"/>
																				</td>
																			</tr>
																		</c:when>
																	</c:choose>
																	
																	<logic:notEqual name="uploadForm" property="key" value="-1">
																		<tr>
																			<td height="20" class="formsTitle">
																				<ispac:tooltipLabel labelKey="form.mensaje.propertyLabel.id" tooltipKey="form.mensaje.propertyLabel.id.tooltip"/>
																			</td>
																			<td height="20">
																				&nbsp;&nbsp;<html:text property="property(ID)" styleClass="inputReadOnly" size="5" readonly="true" maxlength="10"/>
																				<div id="formErrors">
																					<html:errors property="property(ID)"/>
																				</div>
																			</td>
																		</tr>
																		<tr>
																			<td colspan="2"><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" height="8px"/></td>
																		</tr>
																		<tr>
																			<td height="20" class="formsTitle">
																				<ispac:tooltipLabel labelKey="form.mensaje.propertyLabel.sesion" tooltipKey="form.mensaje.propertyLabel.sesion.tooltip"/>
																			</td>
																			<td height="20">
																				&nbsp;&nbsp;<html:text property="property(ID_SESION)" styleClass="inputReadOnly" readonly="true" maxlength="250"/>&nbsp; <bean:message key="catalog.data.obligatory"/>
																				<div id="formErrors">
																					<html:errors property="property(ID_SESION)"/>
																				</div>
																			</td>
																		</tr>
																	</logic:notEqual>
																	<logic:equal name="uploadForm" property="key" value="-1">
																		<c:choose>
																			<c:when test="${empty param.masivo}">
																				<tr>
																				 	<td height="20" class="formsTitle">
																						<html:radio property="property(SOLO_CONECTADOS)" value="1">
																						<ispac:tooltipLabel labelKey="form.mensaje.propertyLabel.soloConectados" tooltipKey="form.mensaje.propertyLabel.tooltip"/>
																						</html:radio>
																					</td>
																					<td height="20" class="formsTitle">
																						<html:radio property="property(SOLO_CONECTADOS)" value="0">
																						<ispac:tooltipLabel labelKey="form.mensaje.propertyLabel.todosUsuarios" tooltipKey="form.mensaje.propertyLabel.todosUsuarios.tooltip"/>
																						</html:radio>
																					</td>
																				</tr>
																			</c:when>
																		</c:choose>
																	</logic:equal>																		
																		<tr>
																			<td colspan="2"><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" height="8px"/></td>
																		</tr>
																		<tr>
																			<td height="20" class="formsTitle" style="vertical-align:top">
																				<ispac:tooltipLabel labelKey="form.mensaje.propertyLabel.mensaje" tooltipKey="form.mensaje.propertyLabel.mensaje.tooltip"/>
																			</td>
																			<td height="20">
																				&nbsp;&nbsp;<html:textarea property="property(TEXTO)" styleClass="inputSelectS" readonly="false" rows="6" cols="40"
																					styleId="desc" onkeypress="javascript:maxlength('desc', 255)"/>
																				<div id="formErrors">
																					<html:errors property="property(TEXTO)"/>
																				</div>
																			</td>
																		</tr>
																		<tr>
																			<td colspan="2"><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" height="8px"/></td>
																		</tr>
																		<c:choose>
																			<c:when test="${empty param.masivo}">
																			<tr>
																				<td height="20" class="formsTitle">
																					<ispac:tooltipLabel labelKey="form.mensaje.propertyLabel.usuario" tooltipKey="form.mensaje.propertyLabel.usuario.tooltip"/>
																					<br>
																					<ispac:tooltipLabel labelKey="form.mensaje.propertyLabel.usuario.aviso" tooltipKey="form.mensaje.propertyLabel.usuario.aviso.tooltip"/>	
																				</td>
																				<td height="20">
																					<html:hidden property="property(ID_USUARIO)"/>
																					&nbsp;&nbsp;<html:text property="property(USUARIO)" styleClass="inputReadOnly" readonly="true" maxlength="250"/>&nbsp; <bean:message key="catalog.data.obligatory"/>
																					<div id="formErrors">
																						<html:errors property="property(USUARIO)"/>
																					</div>																
																					
																					<logic:equal name="uploadForm" property="key" value="-1">
																							<%String URL = "viewUsersManager.do?captionkey=usrmgr.responsable.caption&formName=uploadForm";%>
																							&nbsp;&nbsp;<ispac:linkframe id="USRMANAGER" 
																										styleClass="form_button_white"
																										target="workframe"
																										action='<%=URL%>'
																										titleKey="form.mantenimientoMensajes.propertyLabel.seleccionUsuario"
																										width="640"
																										height="480"
																										showFrame="true">
		
																										<ispac:parameter name="USRMANAGER" id="property(ID_USUARIO)" property="UID"/>
																								 		<ispac:parameter name="USRMANAGER" id="property(USUARIO)" property="RESPNAME"/>
		
																									</ispac:linkframe>
																						</logic:equal>
																					</td>
																				</tr>
																			</c:when>
																		</c:choose>
																		<tr>
																			<td colspan="2"><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" height="8px"/></td>
																		</tr>
																		
																		<tr>
																			<td height="20" class="formsTitle">
																				<ispac:tooltipLabel labelKey="form.mensaje.propertyLabel.type" tooltipKey="form.mensaje.propertyLabel.description.tooltip"/>
																			</td>
																			<td height="20">
																				&nbsp;&nbsp;
																					<html:select styleClass="input" property="property(TIPO)" >
																						<html:option value="form.mensaje.propertyLabel.info"><bean:message key="form.mensaje.propertyLabel.info"/></html:option>
																						<html:option value="form.mensaje.propertyLabel.aviso"><bean:message key="form.mensaje.propertyLabel.aviso"/></html:option>
																						<html:option value="form.mensaje.propertyLabel.urgente"><bean:message key="form.mensaje.propertyLabel.urgente"/></html:option>
																						<html:option value="form.mensaje.propertyLabel.critico"><bean:message key="form.mensaje.propertyLabel.critico"/></html:option>
																					</html:select>
																				<div id="formErrors">
																					<html:errors property="property(TIPO)"/>
																				</div>
																			</td>
																		</tr>
																		<tr>
																			<td colspan="2"><img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" height="8px"/></td>
																		</tr>
																		<tr>
																			<td height="20" class="formsTitle">
																				<ispac:tooltipLabel labelKey="form.mensaje.propertyLabel.date" tooltipKey="form.mensaje.propertyLabel.date.tooltip"/>
																			</td>
																			<td height="20">
																				&nbsp;&nbsp;<html:text property="property(FECHA_MENSAJE)" styleClass="inputReadOnly" size="25" readonly="true" maxlength="10"/>
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

																	

	
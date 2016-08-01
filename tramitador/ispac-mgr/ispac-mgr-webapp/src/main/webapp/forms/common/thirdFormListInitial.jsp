<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="http://java.sun.com/jstl/core" prefix="c" %>
<%@ page import='ieci.tdw.ispac.api.ISPACEntities'%>

<c:set var="thirdPartyAPIClass" value="${ISPACConfiguration.THIRDPARTY_API_CLASS}" />

<script language='JavaScript' type='text/javascript'><!--
	
	function save() {
		
		document.defaultForm.target = "ParentWindow";
		document.defaultForm.action = "storeEntity.do";
		document.defaultForm.name = "Intervinientes";

		//INICIO [eCenpri-Felipe #735]
		var is_chrome= navigator.userAgent.toLowerCase().indexOf('chrome/') > -1;
		var form;
		if (is_chrome){
			form = document.Intervinientes;
		}
		else{
			form = document.defaultForm;
		}

		<logic:notEmpty scope="request" name="displayTagOrderParams">
			form.action = form.action + "?" + '<bean:write scope="request" name="displayTagOrderParams" filter="false"/>';
		</logic:notEmpty>

		if (validateIntervinientes(form)) {
		
			form.submit();
		}

		if (is_chrome){
			ispac_needToConfirm = false;
		}
		else{
			ispac_needToConfirm = true;
		}
		//FIN [eCenpri-Felipe #735]
		
	}
	
	function selectThirdParty(target, action) {
		

		
		/*
		ndoc = document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:NDOC)' ];
		
		if (!ndoc.readOnly) {
			
			executeFrame(target, action);
		}
		*/
		
		nombre = document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:NOMBRE)' ];
		
			
			showFrame(target, action);
		
	}
	
	function delete_THIRD_SEARCH_THIRD_PARTY() {
		
		/*
		idext = document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:ID_EXT)' ];
		if (idext.value != '') {
		*/
		jConfirm('<bean:message key="forms.participantes.msg.delete.data.thirdParty"/>', '<bean:message key="common.confirm"/>', '<bean:message key="common.message.ok"/>' , '<bean:message key="common.message.cancel"/>', function(r) {
		if(r){
			checkRadioById('validatedThirdParty');
	 		ndoc = document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:NDOC)' ];
	 		setNotReadOnly(ndoc);
	 		setReadOnlyIdentidad(document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:NOMBRE)' ]);
			clearThirdParty();
	 		ndoc.focus();
		}
							
		});	
	

	 	ispac_needToConfirm = true;
	}
	
	function clearThirdParty() {
	
		document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:NDOC)' ].value = '';
		clearSearchedDataThirdParty();
	}
	
	function clearSearchedDataThirdParty() {
	
		document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:ID_EXT)' ].value = '';
		document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:TIPO_PERSONA)' ].value = '';
		document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:NOMBRE)' ].value = '';
		document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:DIRNOT)' ].value = '';
		document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:LOCALIDAD)' ].value = '';
		document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:C_POSTAL)' ].value = '';
		document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:CAUT)' ].value = '';
		document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:TFNO_FIJO)' ].value = '';
		document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:TFNO_MOVIL)' ].value = '';
		document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:DIRECCIONTELEMATICA)' ].value = '';
		document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:TIPO_DIRECCION)' ].value = '';
		document.defaultForm.elements[ 'property(TIPO_DIRECCION_SPAC_TBL_005:SUSTITUTO)' ].value = '';
		document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:ROL)' ].value = '';
		document.defaultForm.elements[ 'property(ROL_SPAC_TBL_002:SUSTITUTO)' ].value = '';
	}
	
	function return_THIRD_SEARCH_THIRD_PARTY() {
	
		ndoc = document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:NDOC)' ];
		ndoc.value = ndoc.value.toUpperCase();
		setReadOnly(ndoc);
		setReadOnlyIdentidad(document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:NOMBRE)' ]);
	}
	
	function clickValidatedThirdParty() {
	
		/*
		if (document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:ID_EXT)' ].value == '') {
	
			clearThirdParty();
		}
		
		setReadOnlyIdentidad(document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:NOMBRE)' ]);
		ndoc = document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:NDOC)' ];
		setNotReadOnly(ndoc);
		ndoc.focus();
		*/
		
		nombre = document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:NOMBRE)' ];
		
		if (!nombre.readOnly) {
		
			clearSearchedDataThirdParty();
			setReadOnlyIdentidad(nombre);
			ndoc = document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:NDOC)' ];
			setNotReadOnly(ndoc);
			ndoc.focus();
		}
		enableSearchPostal();
	}
	
	function clickNotValidatedThirdParty() {
	
		if (document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:ID_EXT)' ].value != '') {
	
			clearThirdParty();
		}
		
		//setReadOnly(document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:NDOC)' ]);
		nombre = document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:NOMBRE)' ];
		setNotReadOnlyIdentidad(nombre);
		//nombre.focus();
		ndoc = document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:NDOC)' ];
		setNotReadOnly(ndoc);
		ndoc.focus();
		disableSearchPostal(true);
	}


	function clearSearchedDataPostalAddressThirdParty() {
		document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:IDDIRECCIONPOSTAL)' ].value = '';
		document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:DIRNOT)' ].value = '';
		document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:LOCALIDAD)' ].value = '';
		document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:CAUT)' ].value = '';
		document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:C_POSTAL)' ].value = '';
		document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:DIRECCIONTELEMATICA)' ].value = '';
		document.defaultForm.elements[ 'property(TIPO_DIRECCION_SPAC_TBL_005:SUSTITUTO)' ].value = '';
		document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:TFNO_FIJO)' ].value = '';
		document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:TFNO_MOVIL)' ].value = '';
		document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:TIPO_DIRECCION)' ].value = '';
		document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:TIPO_PERSONA)' ].value = '';
	}

	function setReadOnlyPostalAddressFields(){
		field = document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:IDDIRECCIONPOSTAL)' ];
		setReadOnly(field);
		field = document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:DIRNOT)' ];
		setReadOnlyDir(field);
		field = document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:C_POSTAL)' ];
		setReadOnly(field);
		field = document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:LOCALIDAD)' ];
		setReadOnly(field);
		field = document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:CAUT)' ];
		setReadOnly(field);
		field = document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:TFNO_FIJO)' ];
		setReadOnly(field);
		field = document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:TFNO_MOVIL)' ];
		setReadOnly(field);
	}

	function setNotReadOnlyPostalAddressFields(){
		field = document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:IDDIRECCIONPOSTAL)' ];
		setNotReadOnly(field);
		field = document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:DIRNOT)' ];
		setNotReadOnlyDir(field);
		field = document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:C_POSTAL)' ];
		setNotReadOnly(field);
		field = document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:LOCALIDAD)' ];
		setNotReadOnly(field);
		field = document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:CAUT)' ];
		setNotReadOnly(field);
		field = document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:TFNO_FIJO)' ];
		setNotReadOnly(field);
		field = document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:TFNO_MOVIL)' ];
		setNotReadOnly(field);


	}


	function clickValidatedPostalAddressThirdParty(){
		field = document.defaultForm.elements[ 'property(SPAC_DT_INTERVINIENTES:DIRNOT)' ];
		if (field.readOnly != true){
			clearSearchedDataPostalAddressThirdParty();
			setReadOnlyPostalAddressFields();
		}
	}

	function clickNotValidatedPostalAddressThirdParty(){
		clickNotValidatedPostalAddressThirdPartyClean(true);
	}

	
	
	function clickNotValidatedPostalAddressThirdPartyClean(clean){
		if (clean == true){
			clearSearchedDataPostalAddressThirdParty();
		}
		setNotReadOnlyPostalAddressFields();
	}
	
	



	function disableSearchPostal(clean){
		if(document.getElementById('notValidatedPostalAddressThirdParty')!=null
		  && document.getElementById('validatedPostalAddressThirdParty')!=null ){
				document.getElementById('notValidatedPostalAddressThirdParty').disabled = true;
				document.getElementById('notValidatedPostalAddressThirdParty').checked= true;
				document.getElementById('validatedPostalAddressThirdParty').disabled = true;
				clickNotValidatedPostalAddressThirdPartyClean(clean);
			}
	}
	
	function enableSearchPostal(){
		if(document.getElementById('notValidatedPostalAddressThirdParty')!=null
		  && document.getElementById('validatedPostalAddressThirdParty')!=null ){
			if  (document.getElementById('notValidatedPostalAddressThirdParty').disabled == true){
				document.getElementById('notValidatedPostalAddressThirdParty').disabled = false;
				document.getElementById('validatedPostalAddressThirdParty').disabled = false;
				document.getElementById('validatedPostalAddressThirdParty').checked= true;
				clickValidatedPostalAddressThirdParty();
			}
		}	
	}



//--></script>

<ispac:rewrite id="imgcalendar" href="img/calendar/"/>
<ispac:rewrite id="jscalendar" href="../scripts/calendar.js"/>
<ispac:rewrite id="buttoncalendar" href="img/calendar/calendarM.gif"/>
<ispac:calendar-config imgDir='<%= imgcalendar %>' scriptFile='<%= jscalendar %>'/>

<html:form action="storeEntity.do">

	<!-- Nombre del formulario, necesario para realizar la validación -->
	<html:javascript formName="Intervinientes"/>

	<!-- Solapa que se muestra -->
	<html:hidden property="block" value="1"/>
	
	<!-- Nombre de Aplicación -->
	<html:hidden property="entityAppName"/>
	<!-- Identificador de la entidad -->
	<html:hidden property="entity"/>
	<!-- Identificador del registro -->
	<html:hidden property="key"/>
	<!-- Indicador de sólo lectura -->
	<html:hidden property="readonly"/>
	
	<html:hidden property="property(SPAC_DT_INTERVINIENTES:ID_EXT)"/>
	<html:hidden property="property(SPAC_DT_INTERVINIENTES:TIPO_PERSONA)"/>
	<html:hidden property="property(SPAC_DT_INTERVINIENTES:IDDIRECCIONPOSTAL)"/>

	
	<c:set var="ENTITY_NULLREGKEYID" scope="page"><%=ISPACEntities.ENTITY_NULLREGKEYID%></c:set>

	<table cellpadding="0" cellspacing="0" width="100%">
	
		<tr>
			<td>
			
				<table class="boxTab" width="100%" border="0" cellspacing="0" cellpadding="0">
				
					<tr>
						<td class="title" height="28px">
						
							<table width="100%" border="0" cellspacing="0" cellpadding="0" height="100%">
							
								<tr>
									<td>
									
										<table align="center" cellpadding="0" cellspacing="0" border="0" width="100%">
										
											<tr>
												<td>
												
													<!-- COMIENZO DE LAS ACCIONES -->
													<table cellpadding="0" cellspacing="0" border="0" height="100%">
													
														<tr>
															<td width="4px" height="28px"><img height="1" width="4px" src='<ispac:rewrite href="img/pixel.gif"/>'/></td>
																		
															<logic:equal value="false" property="readonly" name="defaultForm">
	
																<!--ACCION NUEVO -->
																<td class="formaction" height="28px" width="70px">
																	
																	<c:url value="${urlExpDisplayTagOrderParams}" var="link">
																		<c:if test="${!empty param.stageId}">
																			<c:param name="stageId" value='${param.stageId}'/>
																		</c:if>
																		<c:if test="${!empty param.taskId}">
																			<c:param name="taskId" value='${param.taskId}'/>
																		</c:if>
																		<c:if test="${!empty param.activityId}">
																			<c:param name="activityId" value='${param.activityId}'/>
																		</c:if>
																		<c:param name="entity" value="${defaultForm.entity}"/>
																		<c:param name="key" value="${ENTITY_NULLREGKEYID}"/>
																	</c:url>
																	
																	<a href='<c:out value="${link}"/>' class="formaction"><bean:message key="forms.button.new"/></a>
																	
																</td>
																<!--FIN ACCION NUEVO -->
																
																<td height="28px"><img height="1" src='<ispac:rewrite href="img/pixel.gif"/>'/></td>
																
																<!--ACCION SALVAR -->
																<td class="formaction" height="28px" width="70px">
																
																	<a onclick="javascript: ispac_needToConfirm = false;" href="javascript:save();" class="formaction">
																		<bean:message key="forms.button.save"/>
																	</a>
																	
																</td>
																<!--FIN ACCION SALVAR -->
																
																<!--ACCION ELIMINAR -->
																<c:if test="${defaultForm.key != ENTITY_NULLREGKEYID}">
																
																	<td height="28px"><img height="1" src='<ispac:rewrite href="img/pixel.gif"/>'/></td>
																	
																	<td class="formaction" height="28px" width="70px">
																	
																		<c:url value='deleteRegEntity.do' var="deleteLink">
																			<c:if test="${!empty param.stageId}">
																				<c:param name="stageId" value='${param.stageId}'/>
																			</c:if>
																			<c:if test="${!empty param.taskId}">
																				<c:param name="taskId" value='${param.taskId}'/>
																			</c:if>
																			<c:if test="${!empty param.activityId}">
																				<c:param name="activityId" value='${param.activityId}'/>
																			</c:if>
																			<c:param name="entity" value="${defaultForm.entity}"/>
																			<c:param name="key" value="${defaultForm.key}"/>
																		</c:url>
																		
																		<logic:notEmpty scope="request" name="displayTagOrderParams">
																		
																			<a onclick="javascript: ispac_needToConfirm = false;" href="javascript: _confirm('<c:out value="${deleteLink}"/>' + '&' + '<bean:write scope="request" name="displayTagOrderParams" filter="false"/>', '<bean:message key="ispac.action.entity.delete"/>', true , '<bean:message key="common.confirm"/>','<bean:message key="common.message.ok"/>','<bean:message key="common.message.cancel"/>');" class="formaction">
																				<bean:message key="forms.button.delete"/>
																			</a>
																			
																		</logic:notEmpty>
																		
																		<logic:empty scope="request" name="displayTagOrderParams">
																		
																			<a onclick="javascript: ispac_needToConfirm = false;" href="javascript: _confirm('<c:out value="${deleteLink}"/>', '<bean:message key="ispac.action.entity.delete"/>', true , '<bean:message key="common.confirm"/>','<bean:message key="common.message.ok"/>','<bean:message key="common.message.cancel"/>');" class="formaction">
																				<bean:message key="forms.button.delete"/>
																			</a>
																			
																		</logic:empty>
																		
																	</td>
																	
																</c:if>
																<!--FIN ACCION ELIMINAR -->
																
															</logic:equal>
															
															<td height="28px"><img height="1" src='<ispac:rewrite href="img/pixel.gif"/>'/></td>
															
															<!--MQE #127 añadimos la opción copiar los participantes de otro expediente-->
																	<td class="formaction" height="28px" width="75px">

																	<c:url value="/importarParticipantes.do" var="link">
																		<c:if test="${!empty param.stageId}">
																			<c:param name="stageId" value='${param.stageId}'/>
																		</c:if>
																		<c:if test="${!empty param.taskId}">
																			<c:param name="taskId" value='${param.taskId}'/>
																		</c:if>
																		<c:if test="${!empty param.activityId}">
																			<c:param name="activityId" value='${param.activityId}'/>
																		</c:if>
																		<c:param name="entity" value="${defaultForm.entity}"/>
																		<c:param name="key" value="${ENTITY_NULLREGKEYID}"/>
																		<c:param name="method" value="expedienteAction"/>
																	</c:url>
																<a class="formaction" href="javascript:showFrame('workframe','<c:out value="${link}"/>', '', false);"><bean:message key="es.dipucr.importarParticipantes.boton.importar"/></a>
															</td>
															<!--MQE fin modificaciones ticket #127-->
															
															<!--MQE #511 añadimos la opción copiar los participantes de otro expediente-->
																	<td class="formaction" height="28px" width="100%">
																	<c:url value='borrarParticipantes.do' var="link">
																			<c:if test="${!empty param.stageId}">
																				<c:param name="stageId" value='${param.stageId}'/>
																			</c:if>
																			<c:if test="${!empty param.taskId}">
																				<c:param name="taskId" value='${param.taskId}'/>
																			</c:if>
																			<c:if test="${!empty param.activityId}">
																				<c:param name="activityId" value='${param.activityId}'/>
																			</c:if>
																			<c:param name="entity" value="${defaultForm.entity}"/>
																			<c:param name="key" value="${defaultForm.key}"/>
																			<c:param name="method" value="borrarParticipantes"/>
																		</c:url>
																<a class="formaction" href="javascript: _confirm('<c:out value="${link}"/>', '<bean:message key="es.dipucr.borrarParticipantes.confirmacionBorrar"/>', true , '<bean:message key="common.confirm"/>','<bean:message key="common.message.ok"/>','<bean:message key="common.message.cancel"/>');"><bean:message key="es.dipucr.borrarParticipantes.boton.borrar"/></a>
															</td>
															<!--MQE fin modificaciones ticket #511-->
														</tr>
														
													</table>
													<!-- FINAL DE LAS ACCIONES -->
													
												</td>
											</tr>
											
										</table>
										
									</td>
								</tr>
								
							</table>
							
						</td>
					</tr>
					
					<!-- FORMULARIO -->
					<tr>
						<td class="blank">
						
							<table width="100%" border="0" cellspacing="2" cellpadding="2">
							
								<tr>
									<td height="5px" colspan="3"><html:errors/></td>
								</tr>
								<tr>
									<td width="8"><img height="1" width="8px" src='<ispac:rewrite href="img/pixel.gif"/>'/></td>
									<td width="100%">

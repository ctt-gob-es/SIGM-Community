<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<title><s:property value="%{getText('tituloId')}" /></title>
<script type="text/javascript" src="js/script.js"></script>
<script language="javascript" src="js/jquery-1.2.6.pack.js"
	type="text/javascript"></script>
<script language="javascript" src="js/dd.js" type="text/javascript"></script>
<link href="css/minhap.css" rel="stylesheet" type="text/css" />

</head>
<script>
	var maxTabs = 50;
	function activaTab(unTab) {
		try {
			//Los elementos div de todas las pestañas están todos juntos en una
			//única celda de la segunda fila de la tabla de estructura de pestañas.
			//Hemos de buscar la seleccionada, ponerle display block y al resto
			//ponerle display none.
			var id = unTab.id;
			if (id) {
				var idTab = id.split("tabck-");
				var numTab = parseInt(idTab[1]);
				//Las "tabdiv" son los bloques interiores mientras que los "tabck"
				//son las pestañas.
				var esteTabDiv = document.getElementById("tabdiv-" + numTab);
				for (var i = 1; i < maxTabs; i++) {
					var tabdiv = document.getElementById("tabdiv-" + i);
					if (tabdiv) {
						var tabck = document.getElementById("tabck-" + i);
						if (tabdiv.id == esteTabDiv.id) {
							tabdiv.style.display = "inline";
							tabck.style.color = "black";
							tabck.style.backgroundColor = "rgb(235, 235, 225)";
							tabck.style.borderBottomColor = "rgb(235, 235, 225)";
						} else {
							tabdiv.style.display = "none";
							tabck.style.color = "white";
							tabck.style.backgroundColor = "gray";
							tabck.style.borderBottomColor = "gray";
						}
					} else {
						break;
					}
				}
			}
		} catch (e) {
			alert("Error al activar una pestaña. " + e.message);
		}
	}

	function requestAll(attList) {
		switch (attList) {
		case "personal":
			<s:iterator value="attributeList">
			if ('<s:property value="type" />' == 'personal') {
				document.IndexPage.<s:property value="name" />Type[0].click();
			}
			</s:iterator>
			break;
		case "business":
			<s:iterator value="attributeList">
			if ('<s:property value="type" />' == 'business') {
				document.IndexPage.<s:property value="name" />Type[0].click();
			}
			</s:iterator>
			break;
		case "legal":
			<s:iterator value="attributeList">
			if ('<s:property value="type" />' == 'legal') {
				document.IndexPage.<s:property value="name" />Type[0].click();
			}
			</s:iterator>
			break;
		}

	}

	function optionalAll(attList) {
		switch (attList) {
		case "personal":
			<s:iterator value="attributeList">
			if ('<s:property value="type" />' == 'personal') {
				document.IndexPage.<s:property value="name" />Type[1].click();
			}
			</s:iterator>
			break;
		case "business":
			<s:iterator value="attributeList">
			if ('<s:property value="type" />' == 'business') {
				document.IndexPage.<s:property value="name" />Type[1].click();
			}
			</s:iterator>
			break;
		case "legal":
			<s:iterator value="attributeList">
			if ('<s:property value="type" />' == 'legal') {
				document.IndexPage.<s:property value="name" />Type[1].click();
			}
			</s:iterator>
			break;
		}

	}

	function noRequestAll(attList) {
		switch (attList) {
		case "personal":
			<s:iterator value="attributeList">
			if ('<s:property value="type" />' == 'personal') {
				document.IndexPage.<s:property value="name" />Type[2].click();
			}
			</s:iterator>
			break;
		case "business":
			<s:iterator value="attributeList">
			if ('<s:property value="type" />' == 'business') {
				document.IndexPage.<s:property value="name" />Type[2].click();
			}
			</s:iterator>
			break;
		case "legal":
			<s:iterator value="attributeList">
			if ('<s:property value="type" />' == 'legal') {
				document.IndexPage.<s:property value="name" />Type[2].click();
			}
			</s:iterator>
			break;
		}

	}

	$(function() {
		var tabContainers = $('div.tabs > div');

		$('div.tabs ul.tabNavigation a').click(function() {
			tabContainers.hide().filter(this.hash).show();

			$('div.tabs ul.tabNavigation a').removeClass('selected');
			$(this).addClass('selected');

			return false;
		}).filter(':first').click();
	});
</script>

<body onload="activaTab(document.getElementById('tabck-1'));">
	<div class="container">

		<jsp:include page="WEB-INF/header.jsp" />

		<!-- Parte central -->
		<div class="central">
			<div class="contenido">
				<div class="titulo1">
					<hr class="linea_titulo1">
					<h2 class="titulo1">
						<span class="titulo"
							id="labelTituloActualidadctl00_PlaceHolderMain_labelTituloActualidad">CL@VE-SP</span>
					</h2>
				</div>

				<div class="tabs">
					<!-- tab containers -->
					<div id="tab1">

						<!-- ******************************************************************************************************************************** -->
						<!-- ***************************************************************TABBED PANEL 1*************************************************** -->
						<!-- ******************************************************************************************************************************** -->

						<s:form action="IndexPage" theme="simple">
							<input type="hidden" name="sptab" value="tab1" />
							<table border="0" cellpadding="8" cellspacing="3" width="100%">
								<s:hidden name="pepsUrl" id="input"></s:hidden>
								<s:hidden name="returnUrl" id="input"></s:hidden>

								<tr id="altura">
									<td>
										<h2 class="textoRojoExtra">
											<s:property value="%{getText('qaaLevelId')}" />
											:
										</h2>
									</td>

									<td colspan="2"><s:textfield key="qaa" id="input" /></td>
									
									<td><s:checkbox name="forceAuth" value="false" />forceAuth</td>
								</tr>
							</table>
							<s:checkbox name="allowLegalPerson" />Permitir certificados de persona jur&iacute;dica
							<br/>
							<p class="textoRojoExtra">Forzar un IdP deseado:</p>
							<s:select headerKey="none" headerValue="Seleccione un IdP"
								list="opcionesIdP" name="selectIdP"></s:select>
							<h6 class="textoRojoExtra">O marcar un IDP para excluirlo:</h6>
							<table border="0" cellpadding="8" cellspacing="3" width="100%">
								<tr>
									<td><s:checkbox name="checkAFirma" value="false" />@Firma</td>
									<td><s:checkbox name="checkStork" value="false" />Stork</td>
								</tr>
								<tr>
									<td><s:checkbox name="checkSS" value="false" />Seguridad
										Social</td>
									<td><s:checkbox name="checkAEAT" value="false" />Agencia
										Tributaria</td>
								</tr>

								<tr>
									<td colspan="3">
										<table class="tabs">
											<tr>
												<th class="tabcks">&nbsp;</th>
												<th class="tabck" id="tabck-1" onclick="activaTab(this)">PERSONAL</th>
												<th class="tabcks">&nbsp;</th>
												<th class="tabck" id="tabck-2" onclick="activaTab(this)">BUSINESS</th>
												<th class="tabcks">&nbsp;</th>
												<th class="tabck" id="tabck-3" onclick="activaTab(this)">LEGAL</th>
											</tr>
											<tr class="filadiv">
												<td colspan="6">
													<div class="tabdiv" id="tabdiv-1">
														<table class="attributes">
															<tr id="altura">
																<td>
																	<h2>
																		<s:property value="%{getText('attributesId')}" />
																		:
																	</h2>
																</td>
																<td colspan="2"><b> <input type="radio"
																		name="allTypePersonal" id="Mandatory_allPersonal"
																		value="true" onclick="requestAll('personal')" /> <label
																		for="Mandatory_allPersonal"><s:property
																				value="%{getText('mandatoryId')}" /></label> <input
																		type="radio" name="allTypePersonal"
																		id="Optional_allPersonal" value="false"
																		onclick="optionalAll('personal')" /> <label
																		for="Optional_allPersonal"><s:property
																				value="%{getText('optionalId')}" /></label> <input
																		type="radio" name="allTypePersonal"
																		id="NoRequest_allPersonal" value="none"
																		checked="checked" onclick="noRequestAll('personal')" />
																		<label for="NoRequest_allPersonal"><s:property
																				value="%{getText('doNotRequestId')}" /></label>
																</b></td>
															</tr>
															<s:iterator value="attributeList">
																<s:set var="enabled" value="%{'true'}" />
																<s:if test="%{type=='personal'}">
																	<tr id="altura">
																		<s:if test="%{value[0]!=''}">
																			<td><input type="text"
																				name="<s:property value="name"/>"
																				value="<s:property value="name"/>" id="input" /> <input
																				type="text" name="<s:property value="name"/>Value"
																				value="<s:property value="value[0]"/>" id="input" />
																			</td>
																		</s:if>
																		<s:else>

																			<td><s:if test="%{name=='eIdentifier'}">
																					<input type="hidden"
																						name="<s:property value="name"/>"
																						value="<s:property value="name"/>" id="input" />
																						DNI
																					<s:set var="enabled" value="%{'true'}" />
																				</s:if> <s:elseif test="%{name=='givenName'}">
																					<input type="hidden"
																						name="<s:property value="name"/>"
																						value="<s:property value="name"/>" id="input" />
																						Nombre
																					<s:set var="enabled" value="%{'true'}" />
																				</s:elseif> <s:elseif test="%{name=='surname'}">
																					<input type="hidden"
																						name="<s:property value="name"/>"
																						value="<s:property value="name"/>" id="input" />
																						Apellidos
																					<s:set var="enabled" value="%{'true'}" />
																				</s:elseif> <s:else>
																					<input type="text"
																						name="<s:property value="name"/>"
																						value="<s:property value="name"/>" id="input" />
																				</s:else></td>
																		</s:else>
																		<td colspan="2"><s:if test="%{#enabled=='true'}">
																				<input type="radio"
																					name="<s:property value="name" />Type"
																					id="Mandatory_<s:property value="name" />"
																					value="true" />
																				<label for="Mandatory_<s:property value="name" />"><s:property
																						value="%{getText('mandatoryId')}" /></label>
																				<input type="radio"
																					name="<s:property value="name" />Type"
																					id="Optional_<s:property value="name" />"
																					value="false" />
																				<label for="Optional_<s:property value="name" />"><s:property
																						value="%{getText('optionalId')}" /></label>
																				<input type="radio"
																					name="<s:property value="name" />Type"
																					id="NoRequest_<s:property value="name" />"
																					value="none" checked="checked" />
																				<label for="NoRequest_<s:property value="name" />"><s:property
																						value="%{getText('doNotRequestId')}" /></label>

																			</s:if> <s:else>
																				<input type="radio"
																					name="<s:property value="name" />Type"
																					id="Mandatory_<s:property value="name" />"
																					value="true" disabled="disabled" />
																				<label for="Mandatory_<s:property value="name" />"><s:property
																						value="%{getText('mandatoryId')}" /></label>
																				<input type="radio"
																					name="<s:property value="name" />Type"
																					id="Optional_<s:property value="name" />"
																					value="false" disabled="disabled" />
																				<label for="Optional_<s:property value="name" />"><s:property
																						value="%{getText('optionalId')}" /></label>
																				<input type="radio"
																					name="<s:property value="name" />Type"
																					id="NoRequest_<s:property value="name" />"
																					value="none" checked="checked" disabled="disabled" />
																				<label for="NoRequest_<s:property value="name" />"><s:property
																						value="%{getText('doNotRequestId')}" /></label>
																			</s:else></td>
																	</tr>
																</s:if>
															</s:iterator>
														</table>
													</div>



													<div class="tabdiv" id="tabdiv-2">
														<table class="attributes">
															<tr id="altura">
																<td>
																	<h2>
																		<s:property value="%{getText('attributesId')}" />
																		:
																	</h2>
																</td>
																<td colspan="2"><b> <input type="radio"
																		name="allTypeBusiness" id="Mandatory_allBusiness"
																		value="true" onclick="requestAll('business')"
																		disabled="disabled" /> <label
																		for="Mandatory_allBusiness"><s:property
																				value="%{getText('mandatoryId')}" /></label> <input
																		type="radio" name="allTypeBusiness"
																		id="Optional_allBusiness" value="false"
																		onclick="optionalAll('business')" disabled="disabled" />
																		<label for="Optional_allBusiness"><s:property
																				value="%{getText('optionalId')}" /></label> <input
																		type="radio" name="allTypeBusiness"
																		id="NoRequest_allBusiness" value="none"
																		checked="checked" onclick="noRequestAll('business')"
																		disabled="disabled" /> <label
																		for="NoRequest_allBusiness"><s:property
																				value="%{getText('doNotRequestId')}" /></label>
																</b></td>
															</tr>
															<s:iterator value="attributeList">
																<s:if test="%{type=='business'}">
																	<tr id="altura">
																		<s:if test="%{value[0]!=''}">
																			<td><input type="text"
																				name="<s:property value="name"/>"
																				value="<s:property value="name"/>" id="input" /> <input
																				type="text" name="<s:property value="name"/>Value"
																				value="<s:property value="value[0]"/>" id="input" />
																			</td>
																		</s:if>
																		<s:else>
																			<td><input type="text"
																				name="<s:property value="name"/>"
																				value="<s:property value="name"/>" id="input" /></td>
																		</s:else>
																		<td colspan="2"><input type="radio"
																			name="<s:property value="name" />Type"
																			id="Mandatory_<s:property value="name" />"
																			value="true" disabled="disabled" /> <label
																			for="Mandatory_<s:property value="name" />"><s:property
																					value="%{getText('mandatoryId')}" /></label> <input
																			type="radio" name="<s:property value="name" />Type"
																			id="Optional_<s:property value="name" />"
																			value="false" disabled="disabled" /> <label
																			for="Optional_<s:property value="name" />"><s:property
																					value="%{getText('optionalId')}" /></label> <input
																			type="radio" name="<s:property value="name" />Type"
																			id="NoRequest_<s:property value="name" />"
																			value="none" checked="checked" disabled="disabled" />
																			<label for="NoRequest_<s:property value="name" />"><s:property
																					value="%{getText('doNotRequestId')}" /></label></td>
																	</tr>
																</s:if>
															</s:iterator>
														</table>
													</div>




													<div class="tabdiv" id="tabdiv-3">
														<table class="attributes">
															<tr id="altura">
																<td>
																	<h2>
																		<s:property value="%{getText('attributesId')}" />
																		:
																	</h2>
																</td>
																<td colspan="2"><b> <input type="radio"
																		name="allTypeLegal" id="Mandatory_allLegal"
																		value="true" onclick="requestAll('legal')"
																		disabled="disabled" /> <label
																		for="Mandatory_allLegal"><s:property
																				value="%{getText('mandatoryId')}" /></label> <input
																		type="radio" name="allTypeLegal"
																		id="Optional_allLegal" value="false"
																		onclick="optionalAll('legal')" disabled="disabled" />
																		<label for="Optional_allLegal"><s:property
																				value="%{getText('optionalId')}" /></label> <input
																		type="radio" name="allTypeLegal"
																		id="NoRequest_allLegal" value="none" checked="checked"
																		onclick="noRequestAll('legal')" disabled="disabled" />
																		<label for="NoRequest_allLegal"><s:property
																				value="%{getText('doNotRequestId')}" /></label>
																</b></td>
															</tr>
															<s:iterator value="attributeList">
																<s:if test="%{type=='legal'}">
																	<tr id="altura">
																		<s:if test="%{value[0]!=''}">
																			<td><input type="text"
																				name="<s:property value="name"/>"
																				value="<s:property value="name"/>" id="input" /> <input
																				type="text" name="<s:property value="name"/>Value"
																				value="<s:property value="value[0]"/>" id="input" />
																			</td>
																		</s:if>
																		<s:else>
																			<td><input type="text"
																				name="<s:property value="name"/>"
																				value="<s:property value="name"/>" id="input" /></td>
																		</s:else>
																		<td colspan="2"><input type="radio"
																			name="<s:property value="name" />Type"
																			id="Mandatory_<s:property value="name" />"
																			value="true" disabled="disabled" /> <label
																			for="Mandatory_<s:property value="name" />"><s:property
																					value="%{getText('mandatoryId')}" /></label> <input
																			type="radio" name="<s:property value="name" />Type"
																			id="Optional_<s:property value="name" />"
																			value="false" disabled="disabled" /> <label
																			for="Optional_<s:property value="name" />"><s:property
																					value="%{getText('optionalId')}" /></label> <input
																			type="radio" name="<s:property value="name" />Type"
																			id="NoRequest_<s:property value="name" />"
																			value="none" checked="checked" disabled="disabled" />
																			<label for="NoRequest_<s:property value="name" />"><s:property
																					value="%{getText('doNotRequestId')}" /></label></td>
																	</tr>
																</s:if>
															</s:iterator>
														</table>
													</div>
												</td>
											</tr>
										</table>

									</td>
								</tr>
								<tr>
									<td colspan="3">
										<div id="botones">
											<input type="submit" value="Lanzar petici&oacute;n" />
										</div>
									</td>
								</tr>
								<tr>
									<td colspan="3"><s:fielderror /></td>
								</tr>
							</table>
						</s:form>
					</div>
				</div>
			</div>

			<!-- Bloques derecha -->
			<jsp:include page="WEB-INF/banners.jsp" />

			<div class="clear"></div>
		</div>
		<div class="clear"></div>
		<div class="clear"></div>

		<jsp:include page="WEB-INF/footer.jsp" />

	</div>
</body>
</html>

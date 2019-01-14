<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<title><s:property value="%{getText('tituloId')}" /></title>
<script type="text/javascript" src="js/script.js"></script>
<link href="css/minhap.css" rel="stylesheet" type="text/css" />
</head>
<body>

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

				<div id="contenedor">
					<div id="borde">
						<div id="principal">
							<div id="margen">
								<br />
								<div class="titulo2">
									<h2 class="titulo2">
										<s:property value="%{getText('loginSucceededId')}" />
										-
										<s:property value="scope" />
									</h2>
								</div>
								<div id="space"></div>
								<form id="logout" name="logout"
									action="logoutexecute" target="_parent" method="post">
									<div id="botones">
										<p class="textoRojoExtra">Seleccione el entorno al que hacer logout:</p>
										<select name="proxySelected">
											<option value="https://localhost/">Localhost</option>
											<option value="https://des-pasarela.clave.gob.es/">DES</option>
											<option value="https://pre-pasarela.clave.gob.es/">PRE</option>
											<option value="https://se-pasarela.clave.gob.es/">SE</option>
											<option value="https://pasarela.clave.gob.es/">PRO</option>
										</select>
										<s:hidden name="sessionId" value="%{sessionId}" />
										<input type="submit" value="Logout" />
									</div>
								</form>
								<h2>
									<s:property value="%{getText('requestedDataId')}" />
									:
								</h2>
								<div id="space"></div>
																
								<s:set name="isLegalPerson" value="isLegalPerson"/>
								<s:set name="oid" value="oid"/>
								
								<table class="tabla" cellpadding="4">
									<tr class="filatit">
										<td><s:property value="%{getText('attributeId')}" /></td>
										<td><s:property value="%{getText('valuesId')}" /></td>
										<td><s:property value="%{getText('complexValuesId')}" /></td>
									</tr>
									<s:iterator value="attrList" status="idx">
										<tr class="filaresult">
											<td><s:property value="spanishAttrList[#idx.index].name" /></td>
											<td><s:property value="attrList[#idx.index].value" /></td>
											<td><s:property value="attrList[#idx.index].complexValue" /></td>
										</tr>
									</s:iterator>
									
									
									<s:if test="%{#isLegalPerson=='true'}">
										<tr class="filaresult">
											<td><s:property value="%{getText('legalPerson')}" /></td>
											<td><s:property value="#isLegalPerson" /></td>
											<td><s:property value="" /></td>
										</tr>
										<tr class="filaresult">
											<td><s:property value="%{getText('oid')}" /></td>
											<td><s:property value="#oid" /></td>
											<td><s:property value="" /></td>
										</tr>
									</s:if>
									<s:elseif test="%{#isLegalPerson=='false'}">
										<tr class="filaresult">
											<td><s:property value="%{getText('legalPerson')}" /></td>
											<td><s:property value="#isLegalPerson" /></td>
											<td><s:property value="" /></td>
										</tr>
									</s:elseif>									
								</table>
								<p>
									<s:property value="%{getText('errorMessage1Id')}" />
									<a href="<s:property value="%{homepage}"/>"><s:property
											value="%{getText('errorMessage2Id')}" /> </a>
									<s:property value="%{getText('errorMessage3Id')}" />
								</p>
							</div>
						</div>
					</div>
				</div>
			</div>
			<!-- Bloques derecha -->
			<jsp:include page="WEB-INF/banners.jsp" />

		</div>
		<div class="clear"></div>
	</div>
	<div class="clear"></div>
	<div class="clear"></div>

	<jsp:include page="WEB-INF/footer.jsp" />

</body>
</html>

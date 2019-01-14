<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>
<title><s:property value="%{getText('tituloId')}" /></title>
<script type="text/javascript" src="js/script.js"></script>
<script type="text/javascript" src="js/base64.js"></script>
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
								<div id="space"></div>
								<table border="0" cellpadding="1" cellspacing="1" class="borde"
									width="100%">
									<tr>
										<td><form id="countrySelector" name="countrySelector"
												action="<s:property value="pepsUrl" />" target="_parent"
												method="post">
												<table>
													<tr>
														<td>
															<s:hidden name="excludedIdPList" value="%{excludedIdPList}" />
															<s:hidden name="forcedIdP" value="%{forcedIdP}" /></td>
															<s:hidden name="allowLegalPerson" value="%{allowLegalPerson}" />
														<td class="tdLabel"><s:property
																value="%{getText('SAMLRequestId')}" /></td>
														<td><textarea name="SAMLRequest" cols="80" rows="5"><s:property
																	value="SAMLRequest" /></textarea></td>
													</tr>

													<tr>
														<td></td>
														<td colspan=2>
															<div id="botones">
																<input type="submit" value="Submit" />
															</div>
														</td>
													</tr>
												</table>
											</form></td>
									</tr>
									<tr>
										<td>
											<form>
												<table>
													<tr>
														<td></td>
														<td>
															<div id="botones">
																<input type="button" value="Encode"
																	OnClick=encodeSAMLRequest(); /> <input type="button"
																	value="Decode" OnClick=decodeSAMLRequest(); />
															</div>
															<div id="space"></div>
														</td>
													</tr>
													<tr>
														<td class="tdLabel"><s:property
																value="%{getText('SAMLRequestXMLId')}" /></td>
														<td><textarea name="samlRequestXML" cols="80"
																rows="20"><s:property value="samlRequestXML" /></textarea></td>
													</tr>
												</table>
											</form>
								</table>

							</div>
						</div>
					</div>
				</div>

				<!-- Aqui el contenido de Cl@ve -->

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

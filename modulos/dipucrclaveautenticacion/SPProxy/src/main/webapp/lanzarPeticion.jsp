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

<body>
<script>
	function before_submit() {
		var proxySelected = document.getElementsByName("proxySelected")[0].value;
		if ("none" == proxySelected) {
			alert("Antes tienes que seleccionar la URL de destino");
			return false;
		}
		var inputs = document.getElementsByName("selectUrl");

		for (var i = 0; i < inputs.length; i++) {
			inputs[i].value = proxySelected;
		}
		return true;
	}
</script>
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
					<div id="noId">

						<table border="0" cellpadding="8" cellspacing="3" width="100%">

							<tr>
								<td colspan="3">
									<div id="botonesLanzarPeticion">
										<s:form action="defaultRequestIndexPage" theme="simple" onsubmit="return before_submit();">
											<s:hidden name="selectUrl" value="" />
											<input type="submit" value="Lanzar petici&oacute;n" />
										</s:form>
									</div>
								</td>

								<td colspan="3">
									<div id="botonesLanzarPeticion">
										<s:form action="populateIndexPage" theme="simple" onsubmit="return before_submit();">
											<s:hidden name="selectUrl" value="" />
											<input type="submit" value="Petici&oacute;n a medida" />
										</s:form>
									</div>
								</td>
							</tr>
							<tr>
								<td colspan="3"><s:fielderror /></td>
							</tr>
						</table>
						<p class="textoRojoExtra">Seleccione la URL de destino:</p>
						<s:select headerKey="none" headerValue="Seleccione una URL"
							list="proxyUrls" name="proxySelected"></s:select>
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

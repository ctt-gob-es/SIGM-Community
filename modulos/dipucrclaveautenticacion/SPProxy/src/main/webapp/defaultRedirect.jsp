<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html dir="ltr">

<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>Cl@ve: Ministerio de Hacienda y Administraciones
	P&uacute;blicas - Selector de pais de origen</title>
<meta name="title" content="Clave">
<meta name="keywords"
	content="ESPA&Nacute;A, CLAVE, CL@VE, ADMINISTRACI&Oacute;N P&Uacute;BLICA, HACIENDA">
<meta name="description"
	content="Servicio de claves electr&oacute;nicas para la autenticaci&oacute;n del ciudadano">
<meta name="subject" content="2014-08-27">
<link href="./css/minhap.css" rel="stylesheet" type="text/css">
</head>

<body id="home" onload="document.redirectForm.submit();">
	<s:form name="redirectForm" method="post" action="%{pepsUrl}">
		<s:hidden name="excludedIdPList" value="%{excludedIdPList}" />
		<s:hidden name="forcedIdP" value="%{forcedIdP}" />
		<s:hidden name="SAMLRequest" value="%{SAMLRequest}" />
	</s:form>
	<div class="container">

		<jsp:include page="WEB-INF/header.jsp" />

		<!-- Parte central -->
		<div class="central">
			<div class="contenido">
				<div class="titulo1">
					<hr class="linea_titulo1" />
					<h2 class="titulo1">
						<span class="titulo"
							id="labelTituloActualidadctl00_PlaceHolderMain_labelTituloActualidad">CL@VE-PROXY</span>
					</h2>
				</div>

				<div class="titulo2">
					<h2 class="titulo2">
						<span
							id="labelTitulo2Actualidadctl00_PlaceHolderMain_labelTitulo2Actualidad">Redirigiendo...</span>
					</h2>
				</div>

				<br />
				<div align="center">
					<img src="resources/loading.gif" alt="Loading" title="Loading" />
				</div>
			</div>

			<!-- Bloques derecha -->
			<jsp:include page="WEB-INF/banners.jsp" />

		</div>
		<div class="clear"></div>
		<div class="clear"></div>
		<div class="clear"></div>

		<jsp:include page="WEB-INF/footer.jsp" />

	</div>
</body>
</html>
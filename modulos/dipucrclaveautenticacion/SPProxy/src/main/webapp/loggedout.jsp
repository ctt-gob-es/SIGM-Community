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

				<br/><br/>
				<h3 class="textoRojoExtra">
					Desconectado
				</h3>
				<h4>
					Se ha desconectado satisfactoriamente.
					</h4>
				<p>
					<s:property value="%{getText('errorMessage1Id')}" />
					<a href="<s:property value="%{homepage}"/>"><s:property
							value="%{getText('errorMessage2Id')}" /> </a>
					<s:property value="%{getText('errorMessage3Id')}" />
				</p>
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
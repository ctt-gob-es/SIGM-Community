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

				<div class="tabs">
					<!-- tab containers -->
					<div id="noId">

						<h1 class="h1Error">
							<s:property value="%{getText('errorId')}" />
						</h1>
						<h2 class="textoRojoExtra">
							<s:property value="%{exception.title}" />
						</h2>
						<div id="space"></div>
						<s:actionerror />
						<s:property value="%{exception.message}" />

						<p>
							<s:property value="%{getText('errorMessage1Id')}" />
							<a href="<s:property value="%{homepage}"/>"><s:property
									value="%{getText('errorMessage2Id')}" /></a>
							<s:property value="%{getText('errorMessage3Id')}" />
						</p>

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





	<!-- 		<div id="borde"> -->
	<!-- 			<div id="principal"> -->
	<!-- 				<div id="margen"> -->
	<!-- 					<h1> -->
	<%-- 						<s:property value="%{getText('errorId')}" /> --%>
	<!-- 					</h1> -->
	<!-- 					<h2> -->
	<%-- 						<s:property value="%{exception.title}" /> --%>
	<!-- 					</h2> -->
	<!-- 					<div id="space"></div> -->
	<%-- 					<s:actionerror /> --%>
	<%-- 					<s:property value="%{exception.message}" /> --%>

	<!-- 					<p> -->
	<%-- 						<s:property value="%{getText('errorMessage1Id')}" /> --%>
	<%-- 						<a href="<s:property value="%{homepage}"/>"><s:property --%>
	<%-- 								value="%{getText('errorMessage2Id')}" /></a> --%>
	<%-- 						<s:property value="%{getText('errorMessage3Id')}" /> --%>
	<!-- 					</p> -->

	<!-- 				</div> -->
	<!-- 			</div> -->
	<!-- 		</div> -->
	<!-- 	</div> -->

</body>
</html>

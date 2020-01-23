<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html lang="en">
<head>
</head>
<body>
	<!--START HEADER-->
	<header class="header"> </header>
	<!--END HEADER-->
	<div class="window-header fixd_12" align="center">
		<h2>Redirigiendo...</h2>
		<span><img src="./resources/skin0/img/loading.gif" alt="Loading"
			title="Loading" /></span>
		<form id="redirectForm" name="redirectForm" method="${binding}"
			action="${nodeServiceUrl}">
			<input type="hidden" id="relayState" name="RelayState"
				value="${RelayState}"> <input type="hidden" id="SAMLRequest"
				name="SAMLRequest" value="${samlRequest}" />
		</form>
		<noscript>
			<form id="redirectForm" name="redirectForm" method="${binding}"
				action="${nodeServiceUrl}">
				<input type="hidden" id="relayState" name="RelayState"
					value="${RelayState}"> <input type="hidden"
					id="SAMLRequest" name="SAMLRequest" value="${samlRequest}" />

				<p class="box-btn">
					<input type="submit" id="redirectValue_button" class="btn btn-next"
						value="Enviar" />
				</p>
			</form>
		</noscript>
	</div>
</body>
<script type="text/javascript" src="js/redirectCallbackOnload.js"></script>
</html>

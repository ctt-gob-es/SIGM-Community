<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>

<html lang="en">

<head>
	<jsp:include page="htmlHead.jsp"/>
	<title><s:property value="%{getText('tituloId')}"/></title>
	<script type="text/javascript" src="js/sp.js"></script>
	<script type="text/javascript" src="js/base64.js"></script>
	<%--<script type="text/javascript" src="js/script.js"></script>--%>
</head>
<body>

<!--START HEADER-->
<header class="header">
	<div class="container">
		<h1><s:property value="%{getText('tituloCabeceraId')}"/></h1>
	</div>
</header>
<!--END HEADER-->
<div class="container">
	<div class="row">
		<div class="tab-content">
			<div role="tabpanel" class="tab-pane fade in active" id="tab-02">
				<div class="col-md-12">
					<h2 class="text-error"><s:property value="%{getText('errorId')}"/></h2>
					<h3 class="m-top-0 text-error"><s:property value="%{exception.title}"/></h3>
					<p><s:actionerror />
					<s:if test="%{getText(exception.message)!=''}">
						<s:property value="%{getText(exception.message)}"/>
					</s:if>
					<s:else>
						<s:property value="%{exception.message}"/>
					</s:else>
					</p>
					<p>
						<s:property value="%{getText('errorMessage1Id')}"/><a href="."><s:property value="%{getText('errorMessage2Id')}"/></a> <s:property value="%{getText('errorMessage3Id')}"/>
					</p>
				</div>
			</div>
		</div>
	</div>
</div>
</body>	
</html>

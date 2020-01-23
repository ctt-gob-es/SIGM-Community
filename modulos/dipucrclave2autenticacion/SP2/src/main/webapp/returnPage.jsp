<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<html>
<head>
	<jsp:include page="htmlHead.jsp"/>
	<title>Integración Cl@ve</title>
</head>
<body>

<!--START HEADER-->
<header class="header">
	<div class="container">
		<h1>Proveedor de servicios de ejemplo</h1>
	</div>
</header>
<!--END HEADER-->
<div class="container">
	<div class="row">
		<div class="tab-content">
			<div role="tabpanel" class="tab-pane fade in active" id="tab-02">
				<div class="col-md-12">
					<h2>${providerName}&nbsp;
						Sesión iniciada con éxito
					</h2>
					<h3 class="m-top-0">Información solicitada</h3>
					<div class="table-responsive">
						<table class="table table-striped">
							<thead>
								<tr>
									<th>
										Atributo
									</th>
									<th>
										Valor
									</th>
								</tr>
							</thead>
							<tbody>
								<c:forEach items="${attrMap}" var="val">
									<tr>
										<td>
											<c:set var="friendlyName" value="${val.key.friendlyName}" />
											${friendlyName}
										</td>
										<td>
											<c:set var="attrValue" value="${val.value}" />
											${attrValue}
										</td>
									</tr>
								</c:forEach>
							</tbody>
						</table>
					</div>
						<form id="redirectForm" name="redirectForm" method="${binding}" action="${nodeServiceUrl}">
							<input type="hidden" id="relayState" name="RelayState" value="${RelayState}"/>
							<input type="hidden" id="logoutRequest" name="logoutRequest" value="${logoutRequest}"/>
							<button id="submit" type="submit" class="btn btn-default btn-block">Logout</button>
						</form>
						<p>Por favor, pulse <a href="./populateIndexPage">aqui</a> para volver a la ventana principal</p>
				</div>
			</div>
		</div>
	</div>
</div>
</body>
</html>
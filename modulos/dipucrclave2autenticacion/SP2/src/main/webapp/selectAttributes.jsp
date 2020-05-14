<!DOCTYPE html>
<%@ page contentType="text/html;charset=UTF-8" language="java"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<html>
<head>
<jsp:include page="htmlHead.jsp" />
	<title>Integraci&oacute;n eIDAS - Cl@ve</title>
</head>
<body>
	<header class="header">
		<div class="container">
			<h1>
				Proveedor de servicios de ejemplo
			</h1>
		</div>
	</header>
	<div class="container">
		<div>
			<br />
			<br />
			<p>Bienvenido al proveedor de servicios de ejemplo que ilustra el
				proceso de integración con la plataforma del MINHAFP Cl@ve.
				A continuación puede enviar una solicitud por defecto
				haciendo clic en el botón "Iniciar sesión" o puede configurar una
				petición a medida.</p>
			<br />
			<div class="col-md-12 center-content">
				<h5>
					Id de este Proveedor de Servicios: ${providerName}
				</h5>
				<h5>
					Nombre de aplicaci&oacute;n: ${spApplication}
				</h5>
				<h5>
					URL del servicio: ${nodeServiceUrl}
				</h5>
				<h5>
					URL de retorno: ${returnUrl}
				</h5>
				<br/>
			</div>
			<form action="IndexPage" id="formTab2" method="post">
				<input type="hidden" id="returnUrl" name="returnUrl" value="${returnUrl}"/>
				<input type="hidden" id="providerName" name="providerName" value="${providerName}"/>
				<input type="hidden" id="spApplication" name="spApplication" value="${spApplication}"/>
				<div class="form-group right-content" id="eidasForceAuthDiv">
					<input name="forceCheck" id="forceCheck" type="checkbox" value="true"/>
					<label for="forceCheck">
						Forzar autenticaci&oacute;n
					</label>
				</div>
				<table class="table-options">
					<tr>
						<th><input type="hidden" id="nodeServiceUrl" name="nodeServiceUrl" value="${nodeServiceUrl}"/>
						<div class="form-group" id="eidasLoAIdDiv">
							<label for="eidasloa">Nivel de calidad de la autenticaci&oacute;n (LoA)</label>
							<select name="eidasloa" id="eidasloa" class="form-control option-max-width">
								<option value="label">Escoja el nivel</option>
								<option selected value="http://eidas.europa.eu/LoA/low">
									http://eidas.europa.eu/LoA/low</option>
								<option value="http://eidas.europa.eu/LoA/substantial">
									http://eidas.europa.eu/LoA/substantial</option>
								<option value="http://eidas.europa.eu/LoA/high">
									http://eidas.europa.eu/LoA/high</option>
							</select>
						</div>
						</th>
						<th><div class="form-group" id="nameIdPolicyDiv">
							<label for="nameIDPolicy">
								Pol&iacute;tica de nombres
							</label>
							<select name="nameIDPolicy"
								id="nameIDPolicy" class="form-control option-max-width">
								<option value="urn:oasis:names:tc:SAML:2.0:nameid-format:persistent">Persistent</option>
								<option value="urn:oasis:names:tc:SAML:2.0:nameid-format:transient">Transient</option>
								<option value="urn:oasis:names:tc:SAML:1.1:nameid-format:unspecified" selected>Unspecified</option>
							</select>
						</div>
					</th>
				</tr>
				</table>
				<div class="form-group" id="disableIdPsDiv">
					<label for="activeIdP">
						Deshabilitar IdPs
					</label><br/>
					<table class="table-options">
						<tr>
							<th><input name="afirmaCheck" id="afirmaCheck"
								type="checkbox" value="true" /> <label for="afirmaCheck">@Firma</label><br />
								<input name="gissCheck" id="gissCheck" type="checkbox"
								value="true" /> <label for="gissCheck">Clave permanente</label><br />
							</th>
							<th><input name="aeatCheck" id="aeatCheck" type="checkbox"
								value="true" /> <label for="aeatCheck">PIN 24H</label><br /> <input
								name="eidasCheck" id="eidasCheck" type="checkbox" value="true" />
								<label for="eidasCheck">eIDAS</label><br /></th>
						</tr>
					</table>
				</div>
				<button id="submit_tab2" type="button"
					class="btn btn-default btn-lg btn-block">Iniciar sesión</button>
			</form>
		</div>
	</div>
</body>
</html>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jstl/fmt" %>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN"
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">

<html xmlns="http://www.w3.org/1999/xhtml">

<head>

	<title><fmt:message key="head.title"/></title>

	<meta http-equiv="Content Type" content="text/html; charset=iso-8859-1" />
	<meta name="author" content='<fmt:message key="head.author"/>' />

<!--  [eCenpri-Manu Ticket #133] - INICIO - ALSIGM3 Problema con Apache tomcat 8 y la pantalla de inicio. -->
<!--  <link rel="stylesheet" type="text/css" href="css/estilos_portal.css">-->
	<link rel="stylesheet" type="text/css" href="/portal/css/estilos_portal.css"/>
<!--  [eCenpri-Manu Ticket #133] - FIN - ALSIGM3 Problema con Apache tomcat 8 y la pantalla de inicio. -->

	<!--[if IE 6]>
		<link rel="stylesheet" type="text/css" href="css/estilos_portal_ie6.css">
	<![endif]-->

	<!--[if IE 7]>
		<link rel="stylesheet" type="text/css" href="css/estilos_portal_ie7.css">
	<![endif]-->
</head>

<body>

	<div id="cabecera">
		<h1><fmt:message key="header.title"/></h1>
		<h2><fmt:message key="header.subtitle"/></h2>
		<p class="des_adm"><a href='../SIGEM_AutenticacionAdministracionWeb/logout.do?desconectar=true'><fmt:message key="header.adm.logout"/></a></p>
		<p class="des_bck"><a href='../SIGEM_AutenticacionBackOfficeWeb/logout.do?desconectar=true'><fmt:message key="header.bo.logout"/></a></p>
	</div>

	<div id="usuario">
		<div class="usuarioleft">
			<div id="barra_usuario">
				<h1><fmt:message key="user.title"/></h1>
				<h2><fmt:message key="header.subtitle"/></h2>
				<!-- BOTÓN DE AYUDA
				<p><a href="#">&nbsp;</a></p>
				-->
			</div>
		</div>
	</div>

	<div id="contenido_centrado">

		<div class="migas">
			<ul>
				<li><fmt:message key="apps.bo.breadscrumb"/></li>
			</ul>
		</div>

		<div class="bloque">
			<div class="encabezado"><h3> </h3></div> <!-- fin encabezado -->

			<div class="cuerpo">
				<div class="contenido_cuerpo">

					<div class="seccion">
						<div class="encabezado_sec"><fmt:message key="apps.bo.registroPresencial.title"/></div>
						<div class="cuerpo_sec">
							<ul>
								<li><a href="../SIGEM_RegistroPresencialWeb"><fmt:message key="apps.bo.registroPresencial.name"/></a></li>
							</ul>
						</div>
					</div> <!-- fin seccion -->

					<div class="seccion">
						<div class="encabezado_sec"><fmt:message key="apps.bo.gestionExpedientes.title"/></div>
						<div class="cuerpo_sec">
							<ul>
								<li><a href="../SIGEM_TramitacionWeb"><fmt:message key="apps.bo.gestionExpedientes.name"/></a></li>
							</ul>
							<%--
							<ul>
								<li><a href="../SIGEM_BuscadorDocsWeb"><fmt:message key="apps.bo.buscadorDocumentos.name"/></a></li>
							</ul>
							--%>
						</div>
					</div> <!-- fin seccion -->

				</div> <!-- contenido_cuerpo -->
			</div> <!-- fin cuerpo -->
		</div> <!-- fin bloque -->

	</div>

</body>

</html>

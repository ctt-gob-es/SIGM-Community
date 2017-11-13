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
				<li><fmt:message key="apps.adm.breadscrumb"/></li>
			</ul>
		</div>

		<div class="bloque">
			<div class="encabezado"><h3> </h3></div> <!-- fin encabezado -->

			<div class="cuerpo">
				<div class="contenido_cuerpo">

					<div class="seccion">
						<div class="encabezado_sec"><fmt:message key="apps.adm.administracionEntidades.title"/></div>
						<div class="cuerpo_sec">
							<ul>
								<li><a href="../SIGEM_AdministracionWeb"><fmt:message key="apps.adm.administracionEntidades.name"/></a></li>
							</ul>
							<p><fmt:message key="apps.adm.administracionEntidades.description"/></p>
						</div>
					</div> <!-- fin seccion -->

					<div class="seccion">
						<div class="encabezado_sec"><fmt:message key="apps.adm.catalogoTramites.title"/></div>
						<div class="cuerpo_sec">
							<ul>
								<li><a href="../SIGEM_CatalogoTramitesWeb"><fmt:message key="apps.adm.catalogoTramites.name"/></a></li>
							</ul>
							<p><fmt:message key="apps.adm.catalogoTramites.description"/></p>
						</div>
					</div> <!-- fin seccion -->

					<div class="clear">
					</div> <!-- fin clear -->

					<div class="seccion">
						<div class="encabezado_sec"><fmt:message key="apps.adm.catalogoProcedimientos.title"/></div>
						<div class="cuerpo_sec">
							<ul>
								<li><a href="../SIGEM_CatalogoProcedimientosWeb"><fmt:message key="apps.adm.catalogoProcedimientos.name"/></a></li>
							</ul>
							<p><fmt:message key="apps.adm.catalogoProcedimientos.description"/></p>
						</div>
					</div> <!-- fin seccion -->

					<div class="seccion">
						<div class="encabezado_sec"><fmt:message key="apps.adm.repositorioDocumental.title"/></div>
						<div class="cuerpo_sec">
							<ul>
								<li><a href="../SIGEM_RepositoriosDocumentalesWeb"><fmt:message key="apps.adm.repositorioDocumental.name"/></a></li>
							</ul>
							<p><fmt:message key="apps.adm.repositorioDocumental.description"/></p>
						</div>
					</div> <!-- fin seccion -->

					<div class="clear">
					</div> <!-- fin clear -->

					<div class="seccion">
						<div class="encabezado_sec"><fmt:message key="apps.adm.estructuraOrganizativa.title"/></div>
						<div class="cuerpo_sec">
							<ul>
								<li><a href="../SIGEM_EstructuraWeb"><fmt:message key="apps.adm.estructuraOrganizativa.name"/></a></li>
							</ul>
							<p><fmt:message key="apps.adm.estructuraOrganizativa.description"/></p>
						</div>
					</div> <!-- fin seccion -->

					<div class="seccion">
						<div class="encabezado_sec"><fmt:message key="apps.adm.registroPresencial.title"/></div>
						<div class="cuerpo_sec">
							<ul>
								<li><a href="../SIGEM_RegistroPresencialAdminWeb"><fmt:message key="apps.adm.registroPresencial.name"/></a></li>
							</ul>
							<p><fmt:message key="apps.adm.registroPresencial.description"/></p>
						</div>
					</div> <!-- fin seccion -->

					<div class="clear">
					</div> <!-- fin clear -->

					<div class="seccion">
						<div class="encabezado_sec"><fmt:message key="apps.adm.administracionUsuarios.title"/></div>
						<div class="cuerpo_sec">
							<ul>
								<li><a href="../SIGEM_AdministracionUsuariosWeb"><fmt:message key="apps.adm.administracionUsuarios.name"/></a></li>
							</ul>
							<p><fmt:message key="apps.adm.administracionUsuarios.description"/></p>
						</div>
					</div> <!-- fin seccion -->

					<div class="seccion">
						<div class="encabezado_sec"><fmt:message key="apps.adm.archivo.title"/></div>
						<div class="cuerpo_sec">
							<ul>
								<li><a href="../SIGEM_ArchivoWeb/admin.jsp"><fmt:message key="apps.adm.archivo.name"/></a></li>
							</ul>
							<p><fmt:message key="apps.adm.archivo.description"/></p>
						</div>
					</div> <!-- fin seccion -->

					<div class="clear">
					</div> <!-- fin clear -->

				</div> <!-- contenido_cuerpo -->
			</div> <!-- fin cuerpo -->
		</div> <!-- fin bloque -->

		<div class="migas">
			<ul>
				<li><fmt:message key="apps.bo.breadscrumb"/></li>
			</ul>
		</div>

		<div class="bloque">
			<div class="encabezado"><h3> </h3></div> <!-- fin encabezado -->

			<div class="cuerpo">
				<div class="contenido_cuerpo">

					<div class="seccion_short">
						<div class="encabezado_sec"><fmt:message key="apps.bo.registroPresencial.title"/></div>
						<div class="cuerpo_sec">
							<ul>
								<li><a href="../SIGEM_RegistroPresencialWeb"><fmt:message key="apps.bo.registroPresencial.name"/></a></li>
							</ul>
						</div>
					</div> <!-- fin seccion -->

					<div class="seccion_short">
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


					<div class="seccion_short">
						<div class="encabezado_sec"><fmt:message key="apps.bo.archivo.title"/></div>
						<div class="cuerpo_sec">
							<ul>
								<li><a href="../SIGEM_ArchivoWeb"><fmt:message key="apps.bo.archivo.name"/></a></li>
							</ul>
						</div>
					</div> <!-- fin seccion -->

					<div class="clear">
					</div> <!-- fin clear -->

					<div class="seccion">
						<div class="encabezado_sec"><fmt:message key="apps.bo.consultaRegistrosTelematicos.title"/></div>
						<div class="cuerpo_sec">
							<ul>
								<li><a href="../SIGEM_ConsultaRegistroTelematicoBackOfficeWeb"><fmt:message key="apps.bo.consultaRegistrosTelematicos.name"/></a></li>
							</ul>
						</div>
					</div> <!-- fin seccion -->

					<div class="seccion">
						<div class="encabezado_sec"><fmt:message key="apps.bo.consultaExpedientes.title"/></div>
						<div class="cuerpo_sec">
							<ul>
								<li><a href="../SIGEM_ConsultaExpedienteBackOfficeWeb"><fmt:message key="apps.bo.consultaExpedientes.name"/></a></li>
							</ul>
						</div>
					</div> <!-- fin seccion -->

					<div class="seccion">
						<div class="encabezado_sec"><fmt:message key="apps.bo.consultaDocumentos.title"/></div>
						<div class="cuerpo_sec">
							<ul>
								<li><a href="../SIGEM_GestionCSVWeb"><fmt:message key="apps.bo.consultaDocumentos.name"/></a></li>
							</ul>
						</div>
					</div> <!-- fin seccion -->
				<!-- #[eCenpri-Manu Ticket #295] - INICIO - ALSIGM3 Nuevo proyecto Árbol Documental.-->
					<div class="seccion">
						<div class="encabezado_sec"><fmt:message key="apps.bo.arbolDocumental.title"/></div>
						<div class="cuerpo_sec">
							<ul>
								<li><a href="../SIGEM_ArbolDocumentalWeb"><fmt:message key="apps.bo.arbolDocumental.name"/></a></li>
							</ul>
						</div>
					</div> <!-- fin seccion -->
				<!-- #[eCenpri-Manu Ticket #295] - FIN - ALSIGM3 Nuevo proyecto Árbol Documental.-->

					<div class="clear">
					</div> <!-- fin clear -->

				</div> <!-- contenido_cuerpo -->
			</div> <!-- fin cuerpo -->
		</div> <!-- fin bloque -->

		<div class="migas">
			<ul>
				<li><fmt:message key="apps.citizen.breadscrumb"/></li>
			</ul>
		</div>

		<div class="bloque">
			<div class="encabezado"><h3> </h3></div> <!-- fin encabezado -->

			<div class="cuerpo">
				<div class="contenido_cuerpo">

					<div class="seccion">
						<div class="encabezado_sec"><fmt:message key="apps.citizen.registroTelematico.title"/></div>
						<div class="cuerpo_sec">
							<ul>
								<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=TRAM_1"><fmt:message key="apps.citizen.registroTelematico.TRAM_1"/></a></li>

								<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=TRAM_3"><fmt:message key="apps.citizen.registroTelematico.TRAM_3"/></a></li>

								<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=TRAM_4"><fmt:message key="apps.citizen.registroTelematico.TRAM_4"/></a></li>

								<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=TRAM_7"><fmt:message key="apps.citizen.registroTelematico.TRAM_7"/></a></li>

								<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=TRAM_8"><fmt:message key="apps.citizen.registroTelematico.TRAM_8"/></a></li>

								<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=TRAM_9"><fmt:message key="apps.citizen.registroTelematico.TRAM_9"/></a></li>

								<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=TRAM_10"><fmt:message key="apps.citizen.registroTelematico.TRAM_10"/></a></li>

								<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=TRAM_11"><fmt:message key="apps.citizen.registroTelematico.TRAM_11"/></a></li>

								<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=TRAM_12"><fmt:message key="apps.citizen.registroTelematico.TRAM_12"/></a></li>

								<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=TRAM_13"><fmt:message key="apps.citizen.registroTelematico.TRAM_13"/></a></li>

								<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=TRAM_14"><fmt:message key="apps.citizen.registroTelematico.TRAM_14"/></a></li>

								<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=TRAM_15"><fmt:message key="apps.citizen.registroTelematico.TRAM_15"/></a></li>

								<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=TRAM_16"><fmt:message key="apps.citizen.registroTelematico.TRAM_16"/></a></li>

								<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=TRAM_5"><fmt:message key="apps.citizen.registroTelematico.TRAM_5"/></a></li>

								<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=TRAM_6"><fmt:message key="apps.citizen.registroTelematico.TRAM_6"/></a></li>

								<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=TRAM_17"><fmt:message key="apps.citizen.registroTelematico.TRAM_17"/></a></li>

								<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=TRAM_18"><fmt:message key="apps.citizen.registroTelematico.TRAM_18"/></a></li>

								<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=TRAM_19"><fmt:message key="apps.citizen.registroTelematico.TRAM_19"/></a></li>
								<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=SECR_1"><fmt:message key="apps.citizen.registroTelematico.SECR_1"/></a></li>
								<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=DPCR_SRS"><fmt:message key="apps.citizen.registroTelematico.DPCR_SRS"/></a></li>
								<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=SUBV_1"><fmt:message key="apps.citizen.registroTelematico.SUBV_1"/></a></li>
								<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=BOP_1"><fmt:message key="apps.citizen.registroTelematico.BOP_1"/></a></li>
								<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=SUBV-INT"><fmt:message key="apps.citizen.registroTelematico.SUBV_INT"/></a></li>
							</ul>
						</div>
						<div class="encabezado_sec"><fmt:message key="apps.citizen.registroTelematico.title.dipucr.viasobras"/></div>
							<div class="cuerpo_sec _inner">
								<ul>
									<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=VYO_USOSEXCP">Solicitud de Autorización de Usos Excepcionales</a></li>						
								</ul>
							</div>

						<div class="encabezado_sec"><fmt:message key="apps.citizen.registroTelematico.title.dipucr.imprentabop"/></div>
						<div class="cuerpo_sec _inner">
							<ul>
								<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=SOL_TRAB_IMPR_ASO">Solicitud de Trabajos a Imprenta de Asociaciones</a></li>
								<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=SOL_TRAB_IMPR_AYTO">Solicitud de Trabajos a Imprenta de Ayuntamientos</a></li>
							</ul>
						</div>
						
						<div class="encabezado_sec"><fmt:message key="apps.citizen.registroTelematico.title.dipucr.serso"/></div>
						<div class="cuerpo_sec _inner">
							<ul>
								<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=SERSO_PE"><fmt:message key="apps.citizen.registroTelematico.SERSO_PE"/></a></li>
								<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=SERSO_VG"><fmt:message key="apps.citizen.registroTelematico.SERSO_VG"/></a></li>									
							</ul>
						</div>
						
						<div class="encabezado_sec"><fmt:message key="apps.citizen.registroTelematico.title.dipucr.subvenciones"/></div>
						<div class="cuerpo_sec _inner">
							<ul>
								<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=CONVSUB_01">Convocatoria de Subvenciones a EELL</a></li>
								<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=CONVSUB_ASO">Convocatoria de Subvenciones para Asociaciones</a></li>
								<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=CONVSUB_CIU">Convocatoria de Subvenciones para Personas Físicas</a></li>									
							</ul>
						</div>
						
						<div class="encabezado_sec"><fmt:message key="apps.citizen.registroTelematico.title.dipucr.cdj"/></div>
						<div class="cuerpo_sec _inner">
							<ul>
								<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=CDJ_INSCRCC"><fmt:message key="apps.citizen.registroTelematico.CDJ_INSCRCC"/></a></li>
							</ul>
						</div>
					</div> <!-- fin seccion -->

					<div class="seccion">
						<div class="cuerpo_sec _outter">
						
						<div class="encabezado_sec"><fmt:message key="apps.citizen.registroTelematico.title.dipucr"/></div>
							<div class="cuerpo_sec _inner">
								<ul>
									<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=CONTRATACION">Solicitud Contratación</a></li>
									<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=CR_ASES_01"><fmt:message key="apps.citizen.registroTelematico.CR_ASES_01"/></a></li>																							
									<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=SUBSANACION_JUSTIFICACION">Solicitud de Subsanación, Modificación o Justificación para Ayuntamientos</a></li>
									<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=SUBSANACION_JUST_REPRESENT">Solicitud de Subsanación o Modificación para Ciudadanos</a></li>
									<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=SUBSANACION_JUST_REPRESENT">Solicitud de Subsanación o Modificación para Asociaciones</a></li>
									<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=ACCTELEXPAYTOS">Acceso Telemático a Expedientes - AYUNTAMIENTOS</a></li>
									<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=ACCTELEXPCIU">Acceso Telemático a Expedientes - CIUDADANOS</a></li>									
									<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=ACCTELEXPEMP">Acceso Telemático a Expedientes - OTROS</a></li>
									<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=SOLALTMODBAJCOMP">Solicitud de alta, modificación y baja de terceros en Comparece para ayuntamientos, asociaciones y empresas</a></li>									
								</ul>
							</div>
							
							<div class="encabezado_sec"><fmt:message key="apps.citizen.registroTelematico.title.dipucr.gtr"/></div>
								<div class="cuerpo_sec _inner">
									<ul>
										<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=REC_FRAC_IBI"><fmt:message key="apps.citizen.registroTelematico.REC_FRAC_IBI"/></a></li>
										<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=REC_02_VOL"><fmt:message key="apps.citizen.registroTelematico.REC_02_VOL"/></a></li>
										<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=REC_02_EJE"><fmt:message key="apps.citizen.registroTelematico.REC_02_EJE"/></a></li>
										<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=REC_DOMI"><fmt:message key="apps.citizen.registroTelematico.REC_DOMI"/></a></li>
										<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=REC_05_I_INTERNO"><fmt:message key="apps.citizen.registroTelematico.REC_05_I_INTERNO"/></a></li>
										<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=REC_05_II_INTERNO"><fmt:message key="apps.citizen.registroTelematico.REC_05_II_INTERNO"/></a></li>
										<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=REC_05_III_INTERNO"><fmt:message key="apps.citizen.registroTelematico.REC_05_III_INTERNO"/></a></li>
										<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=REC_05_IV_INTERNO"><fmt:message key="apps.citizen.registroTelematico.REC_05_IV_INTERNO"/></a></li>
										<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=REC_05_V_INTERNO"><fmt:message key="apps.citizen.registroTelematico.REC_05_V_INTERNO"/></a></li>
										<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=REC_06_EJE"><fmt:message key="apps.citizen.registroTelematico.REC_06_EJE"/></a></li>
										<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=REC_01"><fmt:message key="apps.citizen.registroTelematico.REC_01"/></a></li>
										<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=REC_10"><fmt:message key="apps.citizen.registroTelematico.REC_10"/></a></li>
										<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=REC_03"><fmt:message key="apps.citizen.registroTelematico.REC_03"/></a></li>
										<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=REC_ESP"><fmt:message key="apps.citizen.registroTelematico.REC_ESP"/></a></li>
										<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=REC_04"><fmt:message key="apps.citizen.registroTelematico.REC_04"/></a></li>
										<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=REC_OTR_SOL"><fmt:message key="apps.citizen.registroTelematico.REC_OTR_SOL"/></a></li>
										<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=REC_OTR_SOL_AYTO"><fmt:message key="apps.citizen.registroTelematico.REC_OTR_SOL_AYTO"/></a></li>
										<li><a href="../SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=REC_OTR_SOL_CIUD"><fmt:message key="apps.citizen.registroTelematico.REC_OTR_SOL_CIUD"/></a></li>
									</ul>
								</div>					

							<div class="encabezado_sec"><fmt:message key="apps.citizen.consultaExpedientes.title"/></div>
							<div class="cuerpo_sec _inner">
								<ul>
									<li><a href="../SIGEM_ConsultaWeb"><fmt:message key="apps.citizen.consultaExpedientes.name"/></a></li>
									<li><a href="../SIGEM_ConsultaWebMiembros">Portal del Diputado</a></li>
									<li><a href="../SIGEM_ConsultaRegistroTelematicoWeb"><fmt:message key="apps.citizen.consultaRegistros.name"/></a></li>
								</ul>
							</div>

							<div class="encabezado_sec"><fmt:message key="apps.citizen.pagoElectronico.title"/></div>
							<div class="cuerpo_sec _inner">
								<ul>
									<li><a href="../SIGEM_PagoElectronicoWeb"><fmt:message key="apps.citizen.pagoElectronico.name"/></a></li>
								</ul>
							</div>

							<div class="encabezado_sec"><fmt:message key="apps.citizen.notificacionElectronica.title"/></div>
							<div class="cuerpo_sec _inner">
								<ul>
									<li><a href="../SIGEM_NotificacionWeb"><fmt:message key="apps.citizen.notificacionElectronica.name"/></a></li>
								</ul>
							</div>

							<div class="encabezado_sec"><fmt:message key="apps.citizen.certificacion.title"/></div>
							<div class="cuerpo_sec _inner">
								<ul>
									<li><a href="../SIGEM_CertificacionWeb"><fmt:message key="apps.citizen.certificacion.name"/></a></li>
								</ul>
							</div>

						</div>
					</div> <!-- fin seccion -->

					<div class="clear">
					</div> <!-- fin clear -->

				</div> <!-- contenido_cuerpo -->
			</div> <!-- fin cuerpo -->
		</div> <!-- fin bloque -->

		<div class="migas">
			<ul>
				<li><fmt:message key="apps.services.breadscrumb"/></li>
			</ul>
		</div>

		<div class="bloque">
			<div class="encabezado"><h3> </h3></div> <!-- fin encabezado -->

			<div class="cuerpo">
				<div class="contenido_cuerpo">

					<div class="seccion_large">
						<div class="encabezado_sec"><fmt:message key="apps.services.title"/></div>
					</div>
					<div class="seccion_top">
						<div class="cuerpo_sec">
							<ul>
								<li><a href="../SIGEM_AdministracionSesionesAdmWS/services/AdministracionSesionesAdministradorWebService?wsdl"><fmt:message key="apps.services.administracionSesionesAdm.name"/></a></li>

								<li><a href="../SIGEM_AdministracionSesionesBackOfficeWS/services/AdministracionSesionesBackOfficeWebService?wsdl"><fmt:message key="apps.services.administracionSesionesBackOffice.name"/></a></li>

                                <li><a href="../SIGEM_AntivirusWS/services/AntivirusWebService?wsdl"><fmt:message key="apps.services.antivirus.name"/></a></li>

								<li><a href="../SIGEM_AutenticacionUsuariosWS/services/AdministracionUsuariosPortalWebService?wsdl"><fmt:message key="apps.services.autenticacionUsuarios.name"/></a></li>

                                <li><a href="../SIGEM_CalendarioWS/services/CalendarioWebService?wsdl"><fmt:message key="apps.services.calendarioTramites.name"/></a></li>

								<li><a href="../SIGEM_CatalogoTramitesWS/services/CatalogoTramitesWebService?wsdl"><fmt:message key="apps.services.catalogoTramites.name"/></a></li>

								<li><a href="../SIGEM_CertificacionWS/services/CertificacionWebService?wsdl"><fmt:message key="apps.services.certificacion.name"/></a></li>

								<li><a href="../SIGEM_ConsultaWS/services/ConsultaExpedientesWebService?wsdl"><fmt:message key="apps.services.consulta.name"/></a></li>

								<li><a href="../SIGEM_CriptoValidacionWS/services/CriptoValidacionWebService?wsdl"><fmt:message key="apps.services.criptoValidacion.name"/></a></li>

								<li><a href="../SIGEM_EntidadesWS/services/EntidadesWebService?wsdl"><fmt:message key="apps.services.entidades.name"/></a></li>

                                <li><a href="../SIGEM_EstructuraOrganizativaWS/services/EstructuraOrganizativaWebService?wsdl"><fmt:message key="apps.services.estructuraOrganizativa.name"/></a></li>

								<li><a href="../SIGEM_FirmaDigitalWS/services/FirmaDigitalWebService?wsdl"><fmt:message key="apps.services.firmaDigital.name"/></a></li>

                                <li><a href="../SIGEM_GeoLocalizacionWS/services/GeoLocalizacionWebService?wsdl"><fmt:message key="apps.services.geoLocalizacion.name"/></a></li>

                                <li><a href="../SIGEM_MensajesCortosWS/services/MensajesCortosWebService?wsdl"><fmt:message key="apps.services.mensajesCortos.name"/></a></li>

                                <li><a href="../SIGEM_NotificacionWS/services/NotificacionesWebService?wsdl"><fmt:message key="apps.services.notificacion.name"/></a></li>
							</ul>
						</div>
					</div> <!-- fin seccion -->

					<div class="seccion_top">
						<div class="cuerpo_sec">
							<ul>
								<li><a href="../SIGEM_PagoElectronicoWS/services/PagoTelematicoWebService?wsdl"><fmt:message key="apps.services.pagoElectronico.name"/></a></li>

                                <li><a href="../SIGEM_PublicadorWS/services/PublicadorWebService?wsdl"><fmt:message key="apps.services.publicador.name"/></a></li>

								<li><a href="../SIGEM_RdeWS/services/RepositorioDocumentosWebService?wsdl"><fmt:message key="apps.services.rde.name"/></a></li>

                                <li><a href="../SIGEM_RegistroPresencialWS/services/ServicioRegistroWebService?wsdl"><fmt:message key="apps.services.registroPresencial.name"/></a></li>

								<li><a href="../SIGEM_RegistroTelematicoWS/services/RegistroTelematicoWebService?wsdl"><fmt:message key="apps.services.registroTelematico.name"/></a></li>

								<li><a href="../SIGEM_RegistroTelematicoDefaultTercerosConnectorWS/ServicioRegistroTelematicoTercerosConnectorSicresService?wsdl"><fmt:message key="apps.services.registroTelematico.terceros.name"/></a></li>

								<li><a href="../SIGEM_SessionUsuarioWS/services/SesionUsuarioWebService?wsdl"><fmt:message key="apps.services.sessionUsuarios.name"/></a></li>

								<li><a href="../SIGEM_TercerosWS/services/TercerosWebService?wsdl"><fmt:message key="apps.services.terceros.name"/></a></li>

								<li><a href="../SIGEM_TramitacionWS/services/TramitacionWebService?wsdl"><fmt:message key="apps.services.tramitacion.name"/></a></li>

								<li><a href="../SIGEM_GestionCSVWS/services/GestionCSVWebService?wsdl"><fmt:message key="apps.services.gestionCSV.name"/></a></li>

								<li><a href="../SIGEM_RegistroTelematicoWS/services/AplicacionExternaCSVConnectorWS?wsdl"><fmt:message key="apps.services.registroTelematico.csv.name"/></a></li>

								<li><a href="../SIGEM_TramitacionWS/services/AplicacionExternaCSVConnectorWS?wsdl"><fmt:message key="apps.services.tramitacion.csv.name"/></a></li>

								<li><a href="../SIGEM_ArchivoWeb/services/WSTransferencias?wsdl"><fmt:message key="apps.services.archivo.transferencias.documentos.electronicos.name"/></a></li>

								<li><a href="../SIGEM_SignoWS/services/LiquidacionPlusvalias.wsdl"><fmt:message key="apps.services.notariado.signo.name"/></a></li>
							</ul>
						</div>
					</div> <!-- fin seccion -->


					<div class="clear">
					</div> <!-- fin clear -->

				</div> <!-- contenido_cuerpo -->
			</div> <!-- fin cuerpo -->
		</div> <!-- fin bloque -->

	</div>

</body>

</html>

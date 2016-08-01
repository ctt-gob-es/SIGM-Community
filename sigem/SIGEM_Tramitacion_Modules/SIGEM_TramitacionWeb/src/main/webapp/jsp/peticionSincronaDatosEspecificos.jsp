<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac"%>
<%@ taglib uri="/WEB-INF/displaytag.tld" prefix="display"%>


<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html">
		<title>Formulario SVD</title>
		<link rel="stylesheet" href='<ispac:rewrite href="css/styles.css"/>' />
		<link rel="stylesheet" href='<ispac:rewrite href="css/estilos.css"/>' />
		<script type="text/javascript" src='<ispac:rewrite href="../scripts/utilities.js"/>'> </script>
		<script type="text/javascript" src='<ispac:rewrite href="../scripts/jquery-1.3.2.min.js"/>'></script>
		<script type="text/javascript" src='<ispac:rewrite href="../scripts/jquery-ui-1.7.2.custom.min.js"/>'></script>
		<script type="text/javascript" src='<ispac:rewrite href="../scripts/jquery.alerts.js"/>'></script>
		<script type="text/javascript" src='<ispac:rewrite href="../scripts/sorttable.js"/>'></script>
		<script type="text/javascript" src="/SIGEM_TramitacionWeb/js/comunidades-ajax.js"></script>
		
		<%
			String codigoCertificado = (String)request.getAttribute("codigoCertificado");
			String coreDescripcion = (String)request.getAttribute("coreDescripcion");
			String descripcion = (String)request.getAttribute("descripcion");
			String nombreSolicitante = (String)request.getAttribute("nombreSolicitante");
			String cifSolicitante = (String)request.getAttribute("cifSolicitante");
			String ndocPartic = (String)request.getAttribute("ndocPartic");
			String version = (String)request.getAttribute("version");
		%>	
	</head>

	<body>	
	<div id="contenido"  class="move">
		<div  class="ficha">
			<div  class="encabezado_ficha">
				<h4>Generaci&oacute;n de peticiones s&iacute;ncronas del certificado <%=codigoCertificado%> (<%=coreDescripcion%>)</h4>
				<p><%=descripcion%></p>
				<%--fin acciones ficha --%>
			</div>
			<div class="cuerpo_ficha">
				<div class="seccion_ficha">
					<form id="formulario" name="formulario" method="get" action="cargaDatosEspecificos.do">
						<jsp:include page="datosGenericosALSIGM.jsp" >
							<jsp:param name="sincrono" value="true" />
							<jsp:param name="nombreRequirente" value='<%=nombreSolicitante%>' />
							<jsp:param name="cifRequirente" value='<%=cifSolicitante%>' />
							<jsp:param name="codigoCertificado" value='<%=codigoCertificado%>' />
						</jsp:include>
						<input type="hidden" name="nombreProc" value="<%=coreDescripcion%>" />
						<input type="hidden" name="version" value="<%=version%>" />
					</form>
					<%--<jsp:include page="footer.jsp" /> --%>
					<div id="msgAlert"></div>
					<div id="divTransparente" style="display:none">
						<center>Enviando...</center>
					</div>
				</div>
			</div>
		</div>
	</div>
	</body>
</html>

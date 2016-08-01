<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>


<!DOCTYPE HTML PUBLIC "-//w3c//dtd html 4.0 transitional//en">
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html">
		<title></title>
		<link rel="stylesheet" href='<ispac:rewrite href="css/styles.css"/>'/>
			<link rel="stylesheet" href='<ispac:rewrite href="css/estilos.css"/>'/>
		<!--[if lte IE 5]>
			<link rel="stylesheet" type="text/css" href='<ispac:rewrite href="css/estilos_ie5.css"/>'/>
		<![endif]-->	

		<!--[if IE 6]>
			<link rel="stylesheet" type="text/css" href='<ispac:rewrite href="css/estilos_ie6.css"/>'>
		<![endif]-->	

		<!--[if IE 7]>
			<link rel="stylesheet" type="text/css" href='<ispac:rewrite href="css/estilos_ie7.css"/>'>
		<![endif]-->
	<script type="text/javascript" src='<ispac:rewrite href="../scripts/jquery-1.3.2.min.js"/>'></script>
 	<script type="text/javascript" src='<ispac:rewrite href="../scripts/jquery-ui-1.7.2.custom.min.js"/>'></script>
 	<script type="text/javascript" src='<ispac:rewrite href="../scripts/jquery.alerts.js"/>'></script>
	<script type="text/javascript" src='<ispac:rewrite href="../scripts/utilities.js"/>'></script>
	
	</head>
	<body>
		<div id="contenido" class="move">
			<div class="ficha">
				<div class="encabezado_ficha">
					<div class="titulo_ficha">
						<h4><bean:message key="es.dipucr.registrar.salida"/></h4>
						<div class="acciones_ficha">
							<logic:notPresent name="ERROR">
								<a href="#" id="btnCloseAndRefresh" class="btnClose"><bean:message key="common.message.close"/></a>
							</logic:notPresent>							
							<logic:present name="ERROR">
								<a href="#" id="btnClose" class="btnClose"><bean:message key="common.message.close"/></a>
							</logic:present>
						</div>
					</div>
				</div>
				<div class="cuerpo_ficha">
					<div class="seccion_ficha">	
						<logic:present name="ERROR">
							<logic:equal name="ERROR" value="ERROR_REGISTRO" scope="request">
								<p class="error">
									<span><bean:message key="registro.salida.notcreate"/></span>
								</p>
							</logic:equal>
						</logic:present>
	
					  <%-- REGISTRO DE SALIDA CREADO --%>
						<logic:notPresent name="ERROR">
							<p align='center'>
								<nobr>
									<label class="titleBig"><bean:message key="es.dipucr.registrarTodo.exito"/></label>
								</nobr>								
							</p>	
						</logic:notPresent>	
						<br><br>								
					</div>
				</div>
			</div>
		</div>
	</body>
</html>
<script type="text/javascript">

		$(document).ready(function() {
			$("#btnCloseAndRefresh").click(function() {
				<ispac:hideframe refresh="true"/>
			});
			 
			$("#btnClose").click(function() {
				<ispac:hideframe/>
			});
		});
			$("#contenido").draggable();
		  positionMiddleScreen('contenido');
	</script>
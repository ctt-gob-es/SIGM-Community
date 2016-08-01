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
		<html:form action="/importarParticipantes.do" method="refrescar">
			<div id="contenido" class="move">
				<div class="ficha">
					<div class="encabezado_ficha">
						<div class="titulo_ficha">					
							&nbsp;
						</div>
					</div>				
					<div class="cuerpo_ficha">
						<div class="seccion_ficha">
							<p align='center'>
								<nobr>
									<label class="titleBig"><bean:message key="es.dipucr.importarParticipantes.mensaje.importacionIncorrecta"/></label>
								</nobr>
								<br><br>
							</p>		
							<div class="firma">
								<html:hidden property="method" value="refrescar"/>
								<html:submit styleId="submit" styleClass="form_button_white" onclick="document.getElementById('method').value='expedienteAction';">
									<bean:message key="es.dipucr.importarParticipantes.boton.seleccionarOtroExp"/>
								</html:submit>
								<html:submit styleId="submit" styleClass="form_button_white" onclick="document.getElementById('method').value='refrescar';">
									<bean:message key="forms.button.aceptar"/>
								</html:submit>					
							</div>				
						</div>
					</div>
				</div>
			</div>
		</html:form>
	</body>
</html>
<script>
	positionMiddleScreen('contenido');
	$(document).ready(function(){
			$("#contenido").draggable();
		});
</script>
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
		<html:form action="/importarParticipantes.do" method="importarParticipantes">	
			<div id="contenido" class="move">
				<div class="ficha">
					<div class="encabezado_ficha">
						<div class="titulo_ficha">
							<div class="acciones_ficha">
								<a href="#" id="btnCancel" onclick='<ispac:hideframe/>' class="btnCancel">
									<bean:message key="common.message.close" />
								</a>
							</div>
						</div>
					</div>
					<div class="cuerpo_ficha">
						<div class="seccion_ficha">			
							<logic:messagesPresent>
								<div class="infoError">
									<bean:message key="forms.errors.messagesPresent" />
								</div>						
							</logic:messagesPresent>
							<label class="titleBig"><bean:message key="es.dipucr.importarParticipantes.mensaje.expediente"/></label>
							<html:text property="expedienteDestino" readonly="false" styleClass="textarea.gr" size='55' maxlength='55' onkeydown="if(this.value.length>50)this.value=this.value.substring(0,50)" onkeyup="if(this.value.length>50)this.value=this.value.substring(0,50)"/>
							<html:hidden property="method" value="importarParticipantes"/>
							<div class="firma">
								<html:submit styleId="submit" styleClass="form_button_white" onclick="document.getElementById('method').value='importarParticipantes';">
									<bean:message key="es.dipucr.importarParticipantes.boton.importar" />
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
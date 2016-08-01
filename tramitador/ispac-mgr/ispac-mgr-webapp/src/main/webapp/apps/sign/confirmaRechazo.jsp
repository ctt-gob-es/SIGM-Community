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
	<html:form action="/batchSignRechazar">

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
					<p>
						<label class="titleBig"><bean:message key="es.dipucr.rechazo.motivo"/>:</label>
					</p>

					<html:textarea property="motivoRechazo" readonly="false" styleClass="textarea.gr" cols="90" rows='4' 
						onkeydown="if(this.value.length>255)this.value=this.value.substring(0,255)" 
						onkeyup="if(this.value.length>255)this.value=this.value.substring(0,255)"/>

					<html:hidden property="method" value="rechazarFirma"/>
		
					<div class="firma">

						<a href="#" id="btnSubmit"
							class="form_button_white"
							onclick="javascript:
								var MIN_CHARACTERS = 15;
								if(document.getElementsByName('motivoRechazo')[0].value.length < MIN_CHARACTERS){
									alert('Debe introducir un mínimo de ' + MIN_CHARACTERS + ' caracteres');
								}
								else{
									if (confirm('¿Está seguro de que desea rechazar los documentos seleccionados?')){
										document.batchSignRechazarForm.submit();
									}
								}">
							<bean:message key="ispac.action.batchsign.rechazar"/>
						</a>					
					</div>
		
				</div>
			</div>
		</div>
	</div>
	</html:form>	

	</body>
</html>

	<script type="text/javascript" language="javascript">
	//<!--
		positionMiddleScreen('contenido');
		$(document).ready(
			function(){
				$("#contenido").draggable();
			}
		);
	//--!>
	</script>
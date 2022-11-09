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
					<c:url var="_link" value="signAllDocument3Fases.do">
						<c:param name="method" value="${appConstants.actions.INIT_SIGN}"></c:param>
					</c:url>
					<a class="linkSign" href='<c:out value="${_link}"/>' onclick="this.style.visibility='hidden';document.getElementById('circuito').style.visibility='hidden';document.getElementById('etiqueta').style.display='';document.getElementById('btnCancel').style.display='none';"><bean:message key="sign.document.now3Fases"/></a>
					<c:url var="_link" value="signAllDocument3Fases.do">
						<c:param name="method" value="${appConstants.actions.SELECT_SIGN_CIRCUIT}"></c:param>
					</c:url>
					<a class="link" href='<c:out value="${_link}"/>' id='circuito'><bean:message key="sign.document.circuit.init"/></a>
					
						<div id='etiqueta' style='display:none;text-align:center;color:#0033FF;font-size:13px'>
							<label>Preparando documentos para la firma, por favor espere.</label>
							<br/><br/>
							<img src="apps/sign/ajax-loader.gif" alt='Preparando documentos para la firma, por favor espere.'/>
						</div>
					
					</p>
				</div>
	   		</div>
		</div>
	</div>

	</body>
</html>

<script>
	positionMiddleScreen('contenido');
	$(document).ready(function(){
		$("#contenido").draggable();
	});
	</script>
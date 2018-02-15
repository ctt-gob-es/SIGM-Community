<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<html>
<head>
	<link rel="stylesheet" href="css/estilos.css" type="text/css" />
	<script type="text/javascript" language="javascript" src="js/idioma.js"></script>

	<script language="Javascript">
		function redirigir(){
			document.forms[0].submit();			
		}
	</script>	
</head>

<body onload="javascript:redirigir();">
	<p><bean:message key="cargando"/></p>
	<form action="../portal/" method="POST">
	</form>
	
</body>
</html>

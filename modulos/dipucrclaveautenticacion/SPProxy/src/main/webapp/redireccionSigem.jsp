<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>

<html:html locale="true">
	<head>
		<script language="Javascript">
			function redirige(){
				//[DipuCR-Agustin] #548 integrar Cl@ve autentificacion, ir de SPProxy a AutenticacionWeb
				document.location.href = '<%="https://"+request.getServerName()+":4443/SIGEM_AutenticacionWeb/validacionClave.do"%>';
			}
		</script>
	</head>
	<body onLoad="javascript:redirige()">
	</body>
</html:html>
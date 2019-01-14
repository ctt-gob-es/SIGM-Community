<%@ page language="java"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ page import="ieci.tecdoc.sgm.core.config.ports.PortsConfig"%>

<html:html>
	<head>
		<script language="Javascript">
			function redirige(){
				document.location.href = '/portal/';
			}
		</script>
	</head>
	<body onLoad="javascript:redirige()">
	</body>
</html:html>
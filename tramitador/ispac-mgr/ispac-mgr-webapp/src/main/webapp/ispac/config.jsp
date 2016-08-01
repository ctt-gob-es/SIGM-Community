<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<%@ page import="ieci.tdw.ispac.ispaclib.util.ISPACConfiguration" %>

<html>
	<head>
		<title><bean:message key="head.title"/></title>
		<link rel="stylesheet" href='<ispac:rewrite href="css/styles.css"/>'/>
	</head>
	<body onload="document.getElementById('protocolLink').click()">								
		<a id='protocolLink' href='sigemEditLauncher:lang=<%= response.getLocale().getLanguage()%>&urlCheckForUpdates=<%= ISPACConfiguration.getInstance().get(ISPACConfiguration.COMPONETES_USUARIO_URL_DESCARGA)%>'></a>	
	</body>
</html>
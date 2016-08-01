<%@ taglib uri="/tags/struts-logic" prefix="logic"%>
<%@ taglib uri="/tags/struts-bean" prefix="bean"%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>

<html:html>
<head>
<!--logic:noempty name="CargarDocumento" property="url"-->
    <META HTTP-EQUIV="Refresh" CONTENT="0;URL=<bean:write name="CargarDocumento" property="URL" />">
<!--/logic:noempty-->
</head>

<body>
<p><bean:message key="cargando"/></p>
</body>
</html:html>

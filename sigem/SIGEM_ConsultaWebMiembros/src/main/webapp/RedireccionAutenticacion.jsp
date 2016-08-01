<%@ page contentType="text/html; charset=ISO-8859-1" %>
<%@taglib prefix="bean" uri="/WEB-INF/struts-bean.tld" %>
<%-- <%@ taglib uri="/tags/struts-bean" prefix="bean"%> --%>
<%@ taglib uri="/tags/struts-html" prefix="html"%>

<%--<html:html>
<head>
	<%
		String url_parcial = (String)request.getSession().getServletContext().getAttribute("redirAutenticacion");
		String dir = "http://"+request.getServerName()+":8080/" + url_parcial;
	%>
    <META HTTP-EQUIV="Refresh" CONTENT="0;URL=<%=dir%>">
</head>


<body>
<p><bean:message key="cargando"/></p>
</body>
</html:html> --%>
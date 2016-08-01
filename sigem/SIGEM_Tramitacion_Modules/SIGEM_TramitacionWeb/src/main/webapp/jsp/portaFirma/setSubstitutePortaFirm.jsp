<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html">
	<c:set var="_parameters" value="${param['parameters']}"/>
	<jsp:useBean id="_parameters" type="java.lang.String"/>
	<logic:present name="Value">
		<c:choose>
			<c:when test="${!empty param['multivalueId']}">
				<c:set var="_multivalueId" value="${param['multivalueId']}"/>
				<jsp:useBean id="_multivalueId" type="java.lang.String"/>
				<ispac:updatefields name="Value" parameters='<%= _parameters %>' multivalueId='<%= _multivalueId %>'/>
			</c:when>
			<c:otherwise>
				<ispac:updatefields name="Value" parameters='<%= _parameters %>'/>
			</c:otherwise>
		</c:choose>
	</logic:present>
	
	<ispac:hideframe event="false"/>
</head>
<body/>
</html>
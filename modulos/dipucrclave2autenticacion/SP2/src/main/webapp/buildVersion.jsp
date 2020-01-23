<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="org.slf4j.Logger,org.slf4j.LoggerFactory" %>

<%! private static final Logger LOGGER = LoggerFactory.getLogger("buildVersion.jsp");%>

<%
    final String SEPARATOR = "-";
    String buildDate = application.getInitParameter("buildDate");
    String server = request.getServerName();
    String projectVersion = application.getInitParameter("projectVersion");
    String samlVersion = application.getInitParameter("samlVersion");

    StringBuilder buildVersion = new StringBuilder();
    buildVersion.append(buildDate).append(SEPARATOR)
            .append(server).append(SEPARATOR)
            .append(projectVersion).append(SEPARATOR)
            .append(samlVersion);
    LOGGER.info("Build version: {}", buildVersion.toString());
%>

<div hidden="true">Build version: <%= buildVersion.toString()%></div>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<html>
<head>
<script type="text/javascript">
<%
String grupo = (String)request.getAttribute("GRUPO");
String formaProvision = (String)request.getAttribute("PROVISION");
String complementoEspecifico = (String)request.getAttribute("COMP_ESP");
String complementoDestino = (String)request.getAttribute("COMP_DEST");
String nivelEspecifico = (String)request.getAttribute("NIVEL_ESP");
String tipoPuesto = (String)request.getAttribute("TIPO_PUESTO");
String escala = (String)request.getAttribute("ESCALA");
String subescala = (String)request.getAttribute("SUBESCALA");
String clase = (String)request.getAttribute("CLASE");
String categoria = (String)request.getAttribute("CATEGORIA");
String titulacion = (String)request.getAttribute("TITULACION");
String tipoJornada = (String)request.getAttribute("JORNADA");	
%>

parent.document.getElementsByName('property(PERSONAL_PUESTO_TRABAJO:GRUPO)')[0].value = '<%=grupo%>';
parent.document.getElementsByName('property(PERSONAL_PUESTO_TRABAJO:PROVISION)')[0].value = '<%=formaProvision%>';
parent.document.getElementsByName('property(PERSONAL_PUESTO_TRABAJO:COMP_ESP)')[0].value = '<%=complementoEspecifico%>';
parent.document.getElementsByName('property(PERSONAL_PUESTO_TRABAJO:NIVEL_ESP)')[0].value = '<%=nivelEspecifico%>';
parent.document.getElementsByName('property(PERSONAL_PUESTO_TRABAJO:COMP_DEST)')[0].value = '<%=complementoDestino%>';
parent.document.getElementsByName('property(PERSONAL_PUESTO_TRABAJO:TIPO_PUESTO)')[0].value = '<%=tipoPuesto%>';
parent.document.getElementsByName('property(PERSONAL_PUESTO_TRABAJO:ESCALA)')[0].value = '<%=escala%>';
parent.document.getElementsByName('property(PERSONAL_PUESTO_TRABAJO:SUBESCALA)')[0].value = '<%=subescala%>';
parent.document.getElementsByName('property(PERSONAL_PUESTO_TRABAJO:CLASE)')[0].value = '<%=clase%>';
parent.document.getElementsByName('property(PERSONAL_PUESTO_TRABAJO:CATEGORIA)')[0].value = '<%=categoria%>';
parent.document.getElementsByName('property(PERSONAL_PUESTO_TRABAJO:TITULACION)')[0].value = '<%=titulacion%>';
parent.document.getElementsByName('property(PERSONAL_PUESTO_TRABAJO:JORNADA)')[0].value = '<%=tipoJornada%>';

</script>

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
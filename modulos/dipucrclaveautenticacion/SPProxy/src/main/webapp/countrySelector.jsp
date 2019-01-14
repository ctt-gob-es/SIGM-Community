<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html dir="ltr">
<head>
<title><s:property value="%{getText('tituloId')}" /></title>
<link href="css/estilos.css" rel="stylesheet" type="text/css" />
</head>
<body>

<div id="contenedor">
<div id="cabecera">
<div class="logo"></div>
<div class="tituloCabecera"><s:property
	value="%{getText('tituloCabeceraId')}" /></div>
</div>
<div id="borde">
<div id="principal">
<div id="margen">


<h1><s:property value="%{providerName}" /></h1>
<br />
<%

String providerName = (String) request.getAttribute("providerName");
String spId = (String) request.getAttribute("spId");
String spUrl= (String) request.getAttribute("spUrl");
String spQAALevel = (String) request.getAttribute("spQAALevel");
String pepsCountryForm = (String) request.getAttribute("pepsCountryForm");
String attrList = (String) request.getAttribute("attrList");
String spSector = (String) request.getAttribute("spSector");
String spInstitution = (String) request.getAttribute("spInstitution");
String spApplication = (String) request.getAttribute("spApplication");
String spCountry = (String) request.getAttribute("spCountry");

 %>
<h2><s:property value="%{getText('selectCountryId')}" />:</h2>
<br />
<iframe
	src="<%=pepsCountryForm%>?spId=<%=spId%>&providerName=<%=providerName%>&spUrl=<%=spUrl%>&SPQAALevel=<%=spQAALevel%>&attrList=<%=attrList%>&spSector=<%=spSector%>&spInstitution=<%=spInstitution%>&spApplication=<%=spApplication%>&spCountry=<%=spCountry%>"
	width="100%" height="100px" style="border: 0px;"></iframe></div>
</div>
</div>
</div>


</body>
</html>

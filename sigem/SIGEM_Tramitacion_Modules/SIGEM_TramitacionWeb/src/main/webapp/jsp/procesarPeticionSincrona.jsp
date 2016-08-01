<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="es.scsp.client.test.*"%>
<%@page import="java.util.Map"%>

<%
	out.println("aqui");
	String nombre="",certificado ="";
	try {
		request.setAttribute("botonActivo","opt1");
		JspTestProcessorSync procesador = new JspTestProcessorSync();
		procesador.procesar(request, response);
		Map<String, String> mapRequest = (Map<String, String>) request.getAttribute("parameterRequestMap");
		certificado  = "".equals(mapRequest.get("codigoProcedimiento")) ? null : mapRequest.get("codigoProcedimiento");
		nombre = "".equals(mapRequest.get("name")) ? null : mapRequest.get("name");
	} catch (Exception e) {
		request.setAttribute("exception", e);
		e.printStackTrace();
	}
%>
<html>
<body>
	<div class="divTopContent">
		<h1>Respuesta del servicio síncrono <%=nombre%> (<%=certificado%> )</h1>
	</div>	

</body>
</html>
<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="es.scsp.client.test.datosespecificos.DatosEspecificosAdapter"%>
<%@page import="ieci.tdw.ispac.api.impl.SessionAPI"%>
	<div class="divDatosEspecificosForm">
		<%
				String cif = (String)request.getAttribute("ndocPartic");
				String certificado = (String)request.getAttribute("codigoCertificado");
				String classname = (String)request.getAttribute("xmlDatosEspecificos");
				String emisor = (String)request.getAttribute("emisor");
				String datosEspecificos = (String)request.getAttribute("strDatosEspecificos");
				String version = (String)request.getAttribute("version");
				String codigoProcedimiento = (String)request.getAttribute("codigoProcedimiento");
				String idExpediente = (String)request.getAttribute("idExpediente");
				request.setAttribute("session",(SessionAPI)request.getAttribute("session"));
				String msg = "<p>Este servicio no requiere introducir un formulario datos específicos o no se ha configurado para ello.</p>";
				
				
				if(classname != null && !"".equals(classname)) {
			try {
				Object obj = Class.forName(classname).newInstance();
				DatosEspecificosAdapter adapter = (DatosEspecificosAdapter)obj;
				String html = adapter.createHtmlController(request);
				if(html != null && !"".equals(html)) {
					out.println(html);	
				} else {
					out.println(msg);
				}
				
			}catch(Exception e) {
				e.printStackTrace();//TODO
			}
				} else {
		%>
		<script>
		document.getElementById('tituloFormulario').innerHTML="Este servicio no requiere introducir un formulario datos específicos o no se ha configurado para ello.";
		</script>
		<%}%>
	<input type="hidden" name="certificado" value="<%=certificado%>" />
	<input type="hidden" name="xmlDatosEspecificos" value="<%=classname%>" />
	<input type="hidden" name="emisor" value="<%=emisor%>" />  
	<input type="hidden" name="version" value="<%=version%>" /> 
	<input type="hidden" name="codigoProcedimiento" value="<%=codigoProcedimiento%>" />
	<input type="hidden" name="idExpediente" value="<%=idExpediente%>" /> 
	</div>
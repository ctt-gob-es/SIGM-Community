<%@page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@page import="es.dipucr.core.datos.DatosEspecificosAdapter"%>
<%@page import="ieci.tdw.ispac.api.impl.SessionAPI"%>
<%@page import="es.dipucr.core.datos.*"%>
<%@page import="org.apache.log4j.Logger"%>
<%@page import="ieci.tdw.ispac.api.errors.ISPACRuleException"%>

	<div class="divDatosEspecificosForm">
		<%
				Logger logger = Logger.getLogger("datosEspecificosALSIGM.jsp");
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
						String resultado = classname.substring(0,1).toUpperCase() + classname.substring(1);

						Object obj = Class.forName("es.dipucr.core.datos."+resultado).newInstance();
						DatosEspecificosAdapter adapter = (DatosEspecificosAdapter)obj;
						String html = adapter.createHtmlController(request);
						if(html != null && !"".equals(html)) {
							out.println(html);	
						} else {
							out.println(msg);
						}
				
					}catch(Exception e) {
						logger.warn("Error en la jsp. cif "+cif+ " certificado "+certificado+" classname "+classname+ " emisor "+emisor+ " datosEspecificos "+datosEspecificos+ " version "+version+ " codigoProcedimiento "+codigoProcedimiento+ " idExpediente "+idExpediente +" - "+e.getMessage(),e);
						throw new ISPACRuleException("Error en la jsp. cif "+cif+ " certificado "+certificado+" classname "+classname+ " emisor "+emisor+ " datosEspecificos "+datosEspecificos+ " version "+version+ " codigoProcedimiento "+codigoProcedimiento+ " idExpediente "+idExpediente +" - "+e.getMessage(),e);
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
	<!-- <input type="hidden" name="strDatosEspecificos" value="<%=datosEspecificos%>" />  -->
	</div>
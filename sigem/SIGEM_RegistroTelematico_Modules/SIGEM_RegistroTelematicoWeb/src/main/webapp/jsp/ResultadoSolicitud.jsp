<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

<%@ page import="java.text.DecimalFormat" %>
<%@ page import="ieci.tecdoc.sgm.registro.utils.Defs" %>
<%@ page import="ieci.tecdoc.sgm.autenticacion.util.TipoAutenticacionCodigos" %>
<%@ page import="ieci.tecdoc.sgm.core.services.telematico.PeticionDocumento" %>
<%@ page import="ieci.tecdoc.sgm.core.services.telematico.PeticionDocumentos" %>
<%@ page import="ieci.tecdoc.sgm.core.config.ports.PortsConfig"%>

<%
		String rutaEstilos = (String)session.getAttribute("PARAMETRO_RUTA_ESTILOS");
		if (rutaEstilos == null) rutaEstilos = "";
		String rutaImagenes = (String)session.getAttribute("PARAMETRO_RUTA_IMAGENES");
		if (rutaImagenes == null) rutaImagenes = "";
		
		boolean bFirma = true;
		String firmar_solicitud = (String)session.getAttribute(Defs.FIRMAR_SOLICITUD);
		if (firmar_solicitud == null || firmar_solicitud.equals(""))
			firmar_solicitud = "1";
		if (firmar_solicitud.equals("0"))
			bFirma = false;
		
		boolean bVirus = false;
		bVirus = ((Boolean)session.getAttribute(Defs.HAY_VIRUS)).booleanValue();
		
		boolean bRegistrar = false;
		try {
			bRegistrar = new Boolean((String)session.getServletContext().getAttribute(Defs.PLUGIN_REGISTRARCONVIRUS)).booleanValue();
		} catch(Exception e) { }		
%>

<html:html>

<%
  	String serverPort = String.valueOf(request.getServerPort()); 
		String proxyHttpPort = PortsConfig.getHttpFrontendPort();
		String proxyHttpsNoCertPort = PortsConfig.getHttpsFrontendPort();
		String proxyHttpsSiCertPort = PortsConfig.getHttpsFrontendAuthclientPort();
		
		if ((proxyHttpPort != null && proxyHttpPort.equals(serverPort)) ||
				(proxyHttpsNoCertPort != null && proxyHttpsNoCertPort.equals(serverPort)) ||
				(proxyHttpsSiCertPort != null && proxyHttpsSiCertPort.equals(serverPort))) {
			
				//Servidor Frontend por delante del Servidor de Aplicaciones (Ej: APACHE + TOMCAT)
				serverPort = proxyHttpPort;
		}
		else {
				serverPort = PortsConfig.getHttpPort();
		}
			
		boolean logado = true;
		String sessionIdIni = "";
		sessionIdIni = (String)session.getAttribute(Defs.SESION_ID);
		String tramiteId = (String)session.getAttribute(Defs.TRAMITE_ID);
			
		if (sessionIdIni == null || sessionIdIni.equals("") || sessionIdIni.equals("null")) {
				
		if (tramiteId == null || tramiteId.equals("null")) {
				tramiteId = new String("");
		}
				
		String url_parcial = (String)request.getSession().getServletContext().getAttribute("redirAutenticacion");
		String dir = "http://" + request.getServerName() + ":" + serverPort + "/" + url_parcial + "&" + Defs.TRAMITE_ID + "=" + tramiteId;
					
		logado = false;		
%>
		<META HTTP-EQUIV="Refresh" CONTENT="0;URL=<%=dir%>">
<%
		}
%>	
					
					<link rel="stylesheet" href="css/<%=rutaEstilos%>estilos.css" type="text/css" />
					<script type="text/javascript" language="javascript" src="js/idioma.js"></script>
					<script type="text/javascript" language="javascript" src="install_files/common-js/firmaBasica.js"></script>
					<script type="text/javascript" language="javascript" src="install_files/common-js/miniapplet.js"></script>		
					<script type="text/javascript" language="javascript">
					  
						function volver(firmar) {				
<%if (!"SUBSANACION".equals(tramiteId)) {%>
							document.forms[0].action = '<html:rewrite page="/prepararSolicitudRegistro.do"/>';
<%}else{%>
							document.forms[0].action = '<html:rewrite page="/prepararSubsanacion.do"/>';				
<%}%>
							document.forms[0].submit();
						}				
<% 	
if (bFirma) {	
%>			 								
									function FirmarReg()
									{										  
											//alert("SE VA A FIRMA CON FIRE");
											return true;
									}
<%}else{%>
									function FirmarReg()
									{										  
											//alert("REGISTRAR SIN FIRMA");
											//document.getElementById('overlay').style.display='';
											//document.getElementById('divConsultaApodera').style.display='';
											document.getElementById('form_acept_request').action='<html:rewrite page="/registrarSolicitud.do"/>';
											return true;
									}
<%
	}
%>
						clienteFirmaCargadoShowButtons = false;								
						function mostrarSolicitud() 
						{						
								parent.mostrarSolicitud();
						}
						
						//[dipucr-Felipe #940] Descargar anexos
						function obtenerAnexo(filename, code){
							document.location.href = "descargarAnexo.do?filename=" + filename + '&code=' + code;
						}
						
					</script>
		
	</head>
	
	<%
	if (!bVirus || (bVirus && bRegistrar)){	
	%>
		<body>
			<script type="text/javascript">				
				cargarMiniApplet();		
			</script>   
	<%
	}else{	
	%>
		<body>
	<%
	}
	%>
	
	<%  	
	if (logado){ 
	%>

	<div id="contenedora">
		
		<%--
		<jsp:include flush="true" page="Cabecera.jsp"></jsp:include>
		--%>
		
		<div class="centered">
		
			<div class="contenedor_centrado">

				<form id="form_acept_request" name="form_acept_request" action="<html:rewrite page="/firmarSolicitud.do"/>" method="post" onsubmit="javascript:return FirmarReg();">

					<div class="cuerpo">
		      			<div class="cuerporight">
		        			<div class="cuerpomid">
		        			
		        				<div id="acciones" class="acciones"><a href="javascript:mostrarSolicitud();" class="cerrar">&nbsp;</a></div>
		          				
		          				<h1><bean:message key="resultado.validar"/></h1>
								
								<div class="cuadro">
								
									<%												
									    if (bVirus) {%>
										<div class="seccion">
											<%  
												if (!bRegistrar) { %>
												<label class="error_rojo"><bean:message key="mensaje.virus"/></label>
											<%} else { %>
												<label class="error_rojo"><bean:message key="mensaje.virus_informativo"/></label>
											<%}%>
											
											<ul class="error_rojo">
											<logic:iterate id="document" name="<%= Defs.HAY_VIRUS_DOCUMENTOS %>">
												<li><bean:write name="document"/></li>
											</logic:iterate>
											</ul>
										</div>
									<%}%>
									
									<input type="hidden" value="" id="<%= Defs.FIRMA %>" name="<%= Defs.FIRMA %>" />
									<input type="hidden" value="" id="<%= Defs.CERTIFICADO_DESC %>" name="<%= Defs.CERTIFICADO_DESC %>" />
									
									
									<%= request.getSession().getAttribute(Defs.SOLICITUD_REGISTRO) %>
									
									<%									
										PeticionDocumentos petDocs = (PeticionDocumentos)session.getAttribute(Defs.DOCUMENTOS_REQUEST);	
									    session.setAttribute(Defs.DOCUMENTOS_REQUEST, petDocs);//[dipucr-Felipe #940]
										if(petDocs != null && petDocs.count()>0){
									%>
											<div class="submenu">
									   			<h1><bean:message key="resultado.documentos_anexos"/></h1>
	   										</div>
											
									<%
											for(int h=0; h<petDocs.count(); h++){
												PeticionDocumento petDoc = petDocs.get(h);
									%>
		
										   		<div class="col clearfix">
													<label class="gr">
														<bean:message key="resultado.documento"/><%=h+1%>:
													</label>
													<label>
														<a onclick="javascript:obtenerAnexo('<%=petDoc.getFileName()%>','<%=petDoc.getCode()%>');" style="cursor:pointer"><%=petDoc.getFileName()%></a>
														<% if ((petDoc.getFileContent().length / 1024) < 1024){ %>
															(<%= petDoc.getFileContent().length / 1024 %> KB)
														<% } else{ %>
															(<%= new DecimalFormat("#.00").format((double) petDoc.getFileContent().length / (1024*1024)) %> MB)
														<%}%>
													</label>
												</div>
									<%
											}
									%>
									<%
										}
									%>
								</div>
							</div>
						</div>
		      		</div>

					<div class="cuerpobt" id="divButtons" style="display: block;">
      					<div class="cuerporightbt">
        					<div class="cuerpomidbt">      					
								<input type="button" value="<bean:message key="resultado.ver_solicitud"/>" class="ok" id="aceptar_registro" onclick="window.open('<%=request.getSession().getAttribute(Defs.XML_REQUEST_FILE)%>','ventana1','width=600px,height=800px,scrollbars=yes');"/>
								<%									
								if (!bVirus || (bVirus && bRegistrar)) {
									
									if (!bFirma) { %>
									<input type="submit" value="<bean:message key="resultado.aceptar"/>" class="ok" id="aceptar_solicitud" name="aceptar_solicitud" />
									<%
									}
									else {
									%>
									<input type="submit" value="<bean:message key="resultado.firmar"/>" class="ok" id="firmar_solicitud" name="firmar_solicitud" />
									&nbsp;&nbsp;&nbsp;&nbsp;									
									<%
									}
								}
								%>
								<%
								if (!bFirma){ %>
									<input class="ok" type="button" id="corregir" value="<bean:message key="resultado.corregir"/>" onClick="javascript:mostrarSolicitud();">
								<%
								}
								else {
								%>
									<input class="ok" type="button" id="corregir" value="<bean:message key="resultado.corregir"/>" onClick="javascript:mostrarSolicitud();">
								<%
								}
								%>
								
        					</div>
      					</div>
					</div>
					
					<div class="cuerpobt" id="divInfoInstall" style="display: none;">
      					<div class="cuerporightbt">
        					<div>
        						<img src="img/<%=rutaImagenes%>bg-wait.gif" />
	      						<label>
	      							<h2 id="lblInfoInstall"><bean:message key="mensaje.appletFirma.instalando"/></h2>
	      						</label>
      						<div>
        				</div>
        			</div>

				</form>
			</div>
		</div>
	</div>	
	
	<%}else{%>
		<p><bean:message key="cargando"/></p>
		<img src="img/<%=rutaImagenes%>bg-wait.gif" />
	<%}%>
	
	</body>

</html:html>
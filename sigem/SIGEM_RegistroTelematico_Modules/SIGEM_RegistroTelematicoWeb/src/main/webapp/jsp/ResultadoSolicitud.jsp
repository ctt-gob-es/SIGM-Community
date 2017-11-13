<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html"%>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic"%>

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
										try {
															document.getElementById('acciones').style.visibility = 'hidden';				
															document.getElementById('aceptar_registro').disabled = true;
															document.getElementById('firmar_solicitud').disabled = true;
															document.getElementById('corregir').disabled = true;
																												
															//alert("Ha pasado el echo");															
															
															dataB64 ="<%=request.getSession().getAttribute(Defs.DATOS_A_FIRMAR)%>";		
															
															//alert("ANTES DE FIRMAR: Este es el contenido de dataB64: "+dataB64);				
															
															var parametros = "expPolicy=" + "FirmaAGE";		
															
															var datasignB64 = firmaBasicaXades(dataB64);
																
															//alert("DESPUES DE FIRMAR: Este es el contenido de signB64: "+datasignB64);
															var campoFirma = document.getElementById("<%= Defs.FIRMA %>");
															
															//En el script de firma ya cargo el contenido de la firma														
															//campoFirma.value = datasignB64;
															
															//alert("DESPUES DE FIRMAR: Este es el contenido de campoFirma.value: "+campoFirma.value);
															
															
														} catch(e) {				
															alert('ERROR AL REALIZAR LA FIRMA, es posible que usted haya cancelado el proceso, en ese caso para poder hacer el registro debe cerrar el navegador y volver a realizar la solicitud. Si le aparece este mensaje siempre sin raz\u00f3n ponga una incidencia en la siguiente direcci\u00f3n administracion.electronica@unavarra.es');
															//Descomenta esta linea para ver la excepcion															
															//alert("ERROR EN LA FIRMA EXCEPCION: "+e);
															document.getElementById('aceptar_registro').disabled = false;
															document.getElementById('firmar_solicitud').disabled = false;									
															document.getElementById('corregir').disabled = false;				
															document.getElementById('acciones').style.visibility = 'visible';
															
															return false;
										}
														
										
										return false;		
									}
<%}else{%>
									function FirmarReg()
									{										  
											alert("SE HA CANCELADO EL PROCESO DE FIRMA");
											return false;
									}
<%
	}
%>
						clienteFirmaCargadoShowButtons = false;								
						function mostrarSolicitud() 
						{						
								parent.mostrarSolicitud();
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

				<form id="form_acept_request" name="form_acept_request" action="<html:rewrite page="/registrarSolicitud.do"/>" method="post" onsubmit="javascript:return FirmarReg();">

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
														<%=petDoc.getFileName()%>
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
        					<!--
        						<br/>
											  <p style="color:red;text-align:justify;margin-left:15px;margin-right:15px"> 
												ATENCIÓN: A PARTIR DEL 18 de ENERO de 2016 se introducen modificaciones técnicas que simplifican y facilitan el proceso de presentación y recepción de documentos electrónicos.
											  <br/>
											  <br/>
												Descargue e instale en su ordenador el software AUTOFIRMA, la versión actual es la 1.4.2. Si ya tenía instalado AUTOFIRMA y tiene problemas al firmar descargue e instale de nuevo AUTOFIRMA. Autofirma requiere version 11 de Internet Explorer y superior, también funciona en otros navegadores como Google Chrome. 
											  <br/>
											  <br/>
											  Haga click en el siguiente enlace y siga las instrucciones:</p>
											  <br/>
											  <br/>
												<a style="color:#0080FF;font-size:12px"
													 target="_blank" 
													 href="https://cloud.dipucr.es/owncloud/index.php/s/BT7tN8BfI58CUKg">DESCARGAR SOFTWARE AUTOFIRMA</a>	
											  <br/>	
											  <br/>											  
											  <a style="color:#0080FF"
													 target="_blank" 
													 href="https://www.youtube.com/watch?v=i0Uebz-HyiU">En caso de duda puede consultar este video tutorial</a>	
											  <br/>
											  <br/>										    
								<br/>
        					-->
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
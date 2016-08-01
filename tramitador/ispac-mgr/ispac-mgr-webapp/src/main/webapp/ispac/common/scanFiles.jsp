<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ taglib uri="/WEB-INF/ispac-util.tld" prefix="ispac" %>
<%@ taglib uri="/WEB-INF/c.tld" prefix="c" %>
<%@ page import="ieci.tdw.ispac.ispaclib.util.ISPACConfiguration"%>

<html>
  <head>
    <link rel="stylesheet" href='<ispac:rewrite href="css/styles.css"/>'/>
    <link rel="stylesheet" href='<ispac:rewrite href="css/menus.css"/>'/>
    <link rel="stylesheet" href='<ispac:rewrite href="css/nicetabs.css"/>'/>
    <link rel="stylesheet" href='<ispac:rewrite href="css/estilos.css"/>'/>

	
	<script type="text/javascript" src='<ispac:rewrite href="../scripts/jquery-1.3.2.min.js"/>'></script>
  	<script type="text/javascript" src='<ispac:rewrite href="../scripts/jquery-ui-1.7.2.custom.min.js"/>'></script>
    <script type="text/javascript" src='<ispac:rewrite href="../scripts/menus.js"/>'></script>
    <script type="text/javascript" src='<ispac:rewrite href="../scripts/sorttable.js"/>'> </script>
    <script type="text/javascript" src='<ispac:rewrite href="../scripts/utilities.js"/>'> </script>
    <ispac:javascriptLanguage/>


    <style>
		#scanner {
			padding: 10px 10px 10px 10px;
			text-align: center;
			visibility: hidden;
		}

		#uploadlist {
		  border-top: 1px solid blue;
		  align:left;
		}

		#uploadlist img {
			border:none;
		  	vertical-align:middle;
		}

		tr.scanner {
			background-color:#639ACE;
			color:white;
		}
    </style>

 
	
	 <%
		String server = request.getScheme() + "://" + request.getServerName() +":" + request.getServerPort();
		String serverName = request.getServerName();
		
		
		String documentTypeId = (String)request.getAttribute("documentTypeId");
		String urlUploadAction = request.getScheme() + "://" + request.getServerName() +":" + request.getServerPort() + request.getContextPath() + "/uploadScanned.do";
		String cookiesPath = request.getContextPath() ;
		String cookiesDomain = request.getServerName();
		
		String urlCheckForUpdates = ISPACConfiguration.getInstance().get(ISPACConfiguration.COMPONETES_USUARIO_URL_DESCARGA);
		if (urlCheckForUpdates == null)		 {
			urlCheckForUpdates = "";
		}	
	%>
	
	
	<script type="text/javascript">
		var protocolo = 'sigemScan';
		var destinoUpload = 'TRAMITADOR';
		var documentTypeId = '<%=documentTypeId%>';
		var urlUploadAction = '<%=urlUploadAction%>';
		var cookiesPath = '<%=cookiesPath%>';
		var cookiesDomain = '<%=cookiesDomain%>';
		var sessionId = '<%=request.getSession().getId()%>';
		var locale = '<c:out value="${pageContext.response.locale.language}"/>';
		var urlCheckForUpdates = '<%=urlCheckForUpdates%>';
		
		var prepararEnlacesInvocacionPorProtocolo = function() {
			var argumentosInvocacionConfigurar= 'config=true' + '&' +
				'lang='+encodeURI(locale);
			
			var argumentosInvocacionEscanear = 'config=false' + '&' +
				'documentTypeId=' + encodeURI(documentTypeId) + '&' +
				'urlUploadAction=' + encodeURI(urlUploadAction) + '&' +
				'destinoUpload=' + encodeURI(destinoUpload) + '&' +
				'sessionId=' + encodeURI(sessionId) + '&' +
				'cookies=' + encodeURI(document.cookie) + '&' +
				'cookiesPath=' + encodeURI(cookiesPath) + '&' +
				'cookiesDomain=' + encodeURI(cookiesDomain) + '&' +
				'urlCheckForUpdates=' + encodeURI(urlCheckForUpdates)  + '&' + 
				'lang='+encodeURI(locale);			 			 			 	
			
			document.getElementById("protocolLinkConfigureScan").href=protocolo + ':' + argumentosInvocacionConfigurar;	
			document.getElementById("protocolLinkScan").href=protocolo + ':' + argumentosInvocacionEscanear;
		}
			
		var  lanzarConfiguracionEscaner = function() {
			document.getElementById('protocolLinkConfigureScan').click();
		}
		
		var lanzarEscaneado = function() {
			document.getElementById('protocolLinkScan').click();		 
		}
			
	</script>
  </head>

  <body onload="prepararEnlacesInvocacionPorProtocolo()">	
	<a id='protocolLinkScan' 			href='#' style="display:none"></a>
	<a id='protocolLinkConfigureScan' 	href='#' style="display:none"></a>
	
	
	<div class="ficha">
		<div class="encabezado_ficha">
			<div class="titulo_ficha">
				<h4><nobr><bean:message key="scanner.title"/></nobr></h4>
				<div class="acciones_ficha">
					<input type="button" value='<bean:message key="common.message.cancel"/>' class="btnCancel"  onclick='parent.location.reload();'/>	
				</div>
			</div>
		</div>

		<div class="cuerpo_ficha">
			<div class="seccion_ficha">	
				<table cellpadding="0" cellspacing="1" border="0"  width="99%" style="margin-top:6px; margin-left:4px">
					<tr>
						<td width="100%" >
							<table border="0" cellspacing="2" cellpadding="2" width="100%">
								<tr>
									<td width="100%" style="text-align:center">
										<input type="button" value='<bean:message key="scanner.button.config"/>' class="form_button_white" onclick="lanzarConfiguracionEscaner()"/>
										<img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" width="10px"/>
										
										<input type="button" value='<bean:message key="scanner.button.scan"/>' class="form_button_white" onclick="lanzarEscaneado()"/>
										<img src='<ispac:rewrite href="img/pixel.gif"/>' border="0" width="10px"/>																			
									</td>
								</tr>					
							</table>
						</td>
					</tr>
				</table>
			</div>
		</div>
	</div>	
  </body>

</html>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>

<%@ page import="com.ieci.tecdoc.common.utils.Configurator"%>
<%@ page import="com.ieci.tecdoc.common.keys.ConfigurationKeys"%>


<%	
	String urlServer = request.getScheme() + "://" + request.getServerName() +":" + request.getServerPort() + request.getContextPath();
	String urlCheckForUpdates = Configurator.getInstance().getProperty(ConfigurationKeys.KEY_COMPONETES_USUARIO_URL_DESCARGA);	
%>
	
	
<script type="text/javascript">
	var protocolo = 'sigemScan';
	var folderId = '<%=(String)request.getParameter("idRegistro")%>';
	var sessionPId = '<%=(String)request.getParameter("sessionPId")%>';
	var urlServer = '<%=urlServer%>';
	var destinoUpload = 'REGISTRO';			
	var sessionId = '<%=request.getSession().getId()%>'
	var urlCheckForUpdates = '<%=urlCheckForUpdates%>'
	var locale = '${pageContext.response.locale.language}';
	
	
	
	var prepararEnlaceLanzarEscanerPorProtocolo = function(idEnlace, pathUpload) {		
		var argumentosInvocacionEscanear = 'config=false' + '&' +
			'folderId=' + encodeURI(folderId) + '&' +
			'sessionPId=' + encodeURI(sessionPId) + '&' +
			'urlUploadAction=' + encodeURI(urlServer + pathUpload) + '&' +
			'destinoUpload=' + encodeURI(destinoUpload) + '&' +
			'sessionId=' + encodeURI(sessionId) + '&' +
			'urlCheckForUpdates=' + encodeURI(urlCheckForUpdates) + '&' +			
			'lang='+encodeURI(locale);
								 			
		document.getElementById(idEnlace).href=protocolo + ':' + argumentosInvocacionEscanear;
	}
	
	
	var prepararEnlaceConfigurarEscanerPorProtocolo = function(idEnlace) {		
		var argumentosInvocacionEscanear = 'config=true' + '&' +
		    'urlCheckForUpdates=' + encodeURI(urlCheckForUpdates) + '&' +
			'lang='+encodeURI(locale);
								 			
		document.getElementById(idEnlace).href=protocolo + ':' + argumentosInvocacionEscanear;
	}
	
	
		
	var lanzarEnlaceEscaner = function(idEnlace) {
		document.getElementById(idEnlace).click();
	}
	
</script>
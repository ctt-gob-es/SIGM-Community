<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" 
"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page import="ieci.tecdoc.sgm.backoffice.utils.ResourceLDAP"%>
<%@ page import="javax.naming.InitialContext"%>
<%@ page import="javax.naming.Context"%>

<html xml:lang="es" xmlns="http://www.w3.org/1999/xhtml" lang="es">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=edge" />
<meta http-equiv="Content-Type" content="text/html; charset=iso-8859-15" />
<meta http-equiv="Pragma" content="no-cache" />
<meta http-equiv="Cache-Control" content="no-cache" />
<meta content="Ministerio de Sanidad, Consumo y Bienestar Social"
	name="description" />
<meta content="Ministerio de Sanidad, Consumo y Bienestar Social"
	name="keywords" />
<title>Ministerio de Sanidad, Consumo y Bienestar Social</title>

		<%@ include file="header.jsp" %>
				<p>Se ha producido el siguiente error:</p>
				<p><%=request.getAttribute ("ERROR") != null?request.getAttribute ("ERROR"):""%></p>
              <% // no se me ocurre otra forma de identificar este error
              if (request.getAttribute ("ERROR") != null && ((String) request.getAttribute("ERROR")).contains("No tiene permisos")){ %>
                <p><b>Si considera que tiene que tener permisos para acceder, p&oacute;ngase en contacto con 
                <a style="color: -webkit-link; text-decoration: underline;" href="mailto:cau-servicios@mscbs.es?subject=[SIGM-REG] Acceso Certificado SIGM">cau-servicios@mscbs.es</a>
                </b></p>
              <% } %>
		<%@ include file="footer.jsp" %>

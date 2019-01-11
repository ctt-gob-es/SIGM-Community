<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

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
<style> /*no funciona, pero no queda tan mal tampoco*/
.botonPortafirmas{
    background: #AAD169;
    border: none;
    font-size: 0.85em;
    font-weight: normal;
    color: #123152;
    padding: 0.7em 2em;
}
</style>
	<%@ include file="header.jsp" %>

		

				<form action="<%= (String) new InitialContext().lookup("java:comp/env/LOGIN_CERT_PATH")%>/login/loginCertServlet" method="post">
					<%if (request.getAttribute("ERROR") != null){%>
							<label style="position:relative; left:300px" class="error"><%=request.getAttribute("ERROR") %></label>
							<% // no se me ocurre otra forma de identificar este error
							if (((String) request.getAttribute("ERROR")).contains("o tiene permisos")||
							    ((String) request.getAttribute("ERROR")).contains("datos de identidad")){ %>
							  <label class="error">Si considera que tiene que tener permisos para acceder, p&oacute;ngase en contacto con 
                <a style="color: -webkit-link; text-decoration: underline;" href="mailto:cau-servicios@mscbs.es?subject=[SIGM-REG] Acceso Certificado SIGM">cau-servicios@mscbs.es</a>
							  </label>
							<% } %>
					<%}%>
					<fieldset>
						<h3><center>Bienvenidos/as al Sistema de Registro del Ministerio</center></h3>
            <p>Para acceder es necesario disponer de un certificado digital emitido por una de las autoridades certificadoras admitidas por el Ministerio (<a style="color: -webkit-link; text-decoration: underline;" href="https://sede.mscbs.gob.es/asesoramientoElectronico/certificadosAdmitidos.htm">consultar certificados admitidos</a>).</p>
              <p><b>Si usted dispone de un certificado v&aacute;lido, se encuentra dado de alta en la aplicaci&oacute;n y aun as&iacute; obtiene un mensaje indicando que no tiene permisos, p&oacute;ngase en contacto con 
              <a style="color: -webkit-link; text-decoration: underline;" href="mailto:cau-servicios@mscbs.es?subject=[SIGM-REG] Acceso Certificado SIGM">cau-servicios@mscbs.es</a> indicando su nombre, apellidos, DNI, unidad a la que pertenece y tr&aacute;mite del que es responsable.
           </b></p>
						<div class="botones">
							<input type="submit" class="botonPortafirmas"
								value="Acceder" />
						</div>
					</fieldset>
				</form>
			
	<%@ include file="footer.jsp" %>

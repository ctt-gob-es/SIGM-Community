<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<%@ page import="ieci.tecdoc.sgm.backoffice.utils.ResourceLDAP"%>
<%@ page import="javax.naming.InitialContext"%>
<%@ page import="javax.naming.Context"%>

<% if ("localhost".equals(request.getServerName()) || Boolean.parseBoolean((String) new InitialContext().lookup("java:comp/env/IS_LOGIN_CERT_ACTIVE")))
  response.sendRedirect("cert.jsp");%>

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

		

				<form action="loginLdapServlet" method="post">
					<%if (request.getAttribute("ERROR") != null){%>
							<label style="position:relative; left:300px" class="error"><%=request.getAttribute("ERROR") %></label>
					<%}%>
					<fieldset>
						<legend><%=ResourceLDAP.getInstance().getProperty("autenticacion.introducir")%></legend>
						<!-- label for="username"><span><%=ResourceLDAP.getInstance().getProperty("autenticacion.conectarse")%>:</span>
							<select type="dominio" name="dominio" id="dominio">
								<option value="">SANIDAD.MSC</option>
								<option value="consumo-inc">CONSUMO-INC.MSC</option>
								<option value="medicamento">MEDICAMENTO.MSC</option>
								<option value="trasplante">TRASPLANTE.MSC</option>
								<option value="asistencia">ASISTENCIA.MSC</option>
								<option value="imujer">IMUJER.AGE</option>
								<option value="injuvent">INJUVENT.DOM</option>
						</select> </label --> <label for="username"><span><%=ResourceLDAP.getInstance().getProperty("autenticacion.username")%>:</span>
							<input type="username" name="username" id="username" autofocus="" AUTOCOMPLETE="off"
							spellcheck="false" /> </label> <label for="password"><span><%=ResourceLDAP.getInstance().getProperty("autenticacion.password")%>:</span>
							<input type="password" name="password" id="password" AUTOCOMPLETE="off" /> </label>
						<div class="botones">
							<input type="submit" class="boton"
								value="<%=ResourceLDAP.getInstance().getProperty("autenticacion.aceptar")%>" />
						</div>
					</fieldset>
				</form>
			
	<%@ include file="footer.jsp" %>

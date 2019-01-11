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
<meta content="Ministerio de Sanidad, Servicios Sociales e Igualdad"
	name="description" />
<meta content="Ministerio de Sanidad, Servicios Sociales e Igualdad"
	name="keywords" />
<title>Ministerio de Sanidad, Servicios Sociales e Igualdad</title>

	<%@ include file="header.jsp" %>
				<form action="acceptObligServlet" method="post">
					<fieldset>
						<legend style="font-weight: normal;">
							<%=ResourceLDAP.getInstance().getProperty("mensaje_informativo.obligation")%>
						</legend>
						<div class="botones">
							<input type="hidden" id="accept" name="accept" value="S" />
							<input type="submit" class="boton" onclick="document.getElementById('accept').value = 'S'"
								value="<%=ResourceLDAP.getInstance().getProperty("autenticacion.aceptar")%>" /> 
								<input type="submit"
								class="boton"
								onclick="document.getElementById('accept').value = 'N'"
								value="<%=ResourceLDAP.getInstance().getProperty("autenticacion.rechazar")%>" />
						</div>
					</fieldset>
				</form>
	<%@ include file="footer.jsp" %>

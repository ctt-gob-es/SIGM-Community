<%@ page import="javax.naming.InitialContext" %>
<%@ page import="javax.naming.Context" %>
<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>

<link rel="stylesheet" href="<%=((Context) new InitialContext().lookup("java:comp/env")).lookup("URLRoot")%>/include/cssTemp/estilos.css" type="text/css" />

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>
<body>

    <div id="contenedora">
		<div id="cabecera">
	   		<div id="logo">
	   			<img src="<%=((Context) new InitialContext().lookup("java:comp/env")).lookup("URLRoot")%>/include/img/logo.gif" alt="sigem" />
	   		</div>
			<div class="salir">
				<img src="<%=((Context) new InitialContext().lookup("java:comp/env")).lookup("URLRoot")%>/include/img/exit.gif" alt="salir" width="26" height="20" class="icono" />
				<span class="titular">
					<a href="/SIGEM_EstructuraOrganizativaWeb/desconectar.do">Salir</a>
				</span>
			</div>
	 		</div>
	 		<div class="usuario">
	   		<div class="usuarioleft">
	     		<p>Cat&aacute;logo de tr&aacute;mites</p>
			</div>
    		<div class="usuarioright">
      			<div style="padding-top: 8px; padding-right: 24px;">
		     		<a href="#"><img src="<%=((Context) new InitialContext().lookup("java:comp/env")).lookup("URLRoot")%>/include/img/help.gif" style="border: 0px" alt="ayuda" width="16" height="16" /></a>
		     	</div>
    		</div>
	 	</div>
	 	<br />

	</div>
	
	<p class="ROJO">asfasdf</p>

</body>
</html>
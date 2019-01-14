<%@ page contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="s" uri="/struts-tags"%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<html>
<head>

<%
request.setAttribute("parm4", "hola");
request.getSession().setAttribute("parm3", "hola");

%>
<script language="Javascript">
			function redirige(){
				//[DipuCR-Agustin] #548 integrar Cl@ve autentificacion, ir de SPProxy a AutenticacionWeb
				//Con esta opcion no crea la sesion, hay que crearla después de validar contra Cl@ve
				//document.location.href = '<%="https://"+request.getServerName()+":4443/SIGEM_AutenticacionWeb/jsp/RedireccionClaveRT.jsp"%>';
				//Voy al action ValidacionClave para crear la sesion
				document.location.href = '<%="https://"+request.getServerName()+":4443/SIGEM_AutenticacionWeb/validacionClave.do"%>';				
			}			
</script>

</head>

<body>

<form action="<%="https://" + request.getServerName() + ":4443/SIGEM_AutenticacionWeb/validacionClave.do"%>">
<input id="boton" type="submit" value='boton value'>hola</input>

<input id="parm2" type="hidden" value="5" />

<script>
alert('HOLA');
alert(document.getElementById('boton').value);
document.getElementById('boton').click();
</script>
</form>


	

		
</body>
</html>
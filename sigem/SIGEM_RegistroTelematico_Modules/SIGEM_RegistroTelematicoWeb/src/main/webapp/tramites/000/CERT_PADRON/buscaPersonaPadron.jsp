<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="es.dipucr.sigem.api.rule.procedures.certificadosPadron.PadronUtils"%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Buscando ...</title>
</head>
<body>
	Buscando ...
</body>
<script type="text/javascript">
<%
	String nombre = "";
	String nif = "", valor ="", codInstitucion="";
	String[] cadenaSplit;
	if(request.getParameter("valor")!=null){
		valor = (String)request.getParameter("valor");
	}

	cadenaSplit = valor.split(";");
	nif = cadenaSplit[0];
	codInstitucion = cadenaSplit[1];	

	try{
		nombre = PadronUtils.getNombrePersona(codInstitucion, nif);
	}
	catch(Exception ex){
		//No hacemos nada
	}
	
%>

var nombre = '<%=nombre%>';
if (nombre == ''){
	window.close();
	opener.alert('No se ha encontrado ninguna persona que coincida con el NIF/NIE introducido.');
}
else{
	opener.document.getElementById('nombreSolicitante').value = nombre;
	window.close();
}

</script>

</html>
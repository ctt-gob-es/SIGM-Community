<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="es.dipucr.sigem.api.rule.common.AccesoBBDDTramitador"%>
<%@page import="java.util.Vector"%>
<%@page import="es.dipucr.sigem.api.object.Expediente"%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">

</head>
<form action="">


<%

	String value = "";
	if(request.getParameter("valor")!=null){
		value = (String)request.getParameter("valor");
	}
	
	String [] cadenaSplit = value.split(";");
	String valuenumExp = cadenaSplit[0];
	String entidad = cadenaSplit[1];	
	
	AccesoBBDDTramitador accsTramitador = new AccesoBBDDTramitador(entidad);
	Vector expediente =  accsTramitador.getExpedienteNumexpReg(valuenumExp);
	if(expediente.size() == 0){
		%>
		<script type="text/javascript">
			alert("Introduce el número de expediente o número de registro correcto");
			window.close();
		</script>
		<%
	}
	if(expediente.size() == 1){
		Expediente exp= (Expediente) expediente.get(0);
		String asunto = exp.getAsunto();
		String numexp = exp.getNumexp();
		String nombreProc = exp.getNombreProcedimientos();
		if(asunto.equals("")){
			asunto = nombreProc;
		}
		%>
		<script type="text/javascript">
			opener.document.getElementById('asunto').value = '<%=asunto%>';
			opener.document.getElementById('numExpediente').value = '<%=numexp%>';
			window.close();
		</script>
		<%
	}
	else{
		%>
		Seleccione el expediente que desea Subsanar o Justificar: <br/>
		 
		<%
		for(int i = 0; i < expediente.size(); i++){
			Expediente exp= (Expediente) expediente.get(i);
			String numexp = exp.getNumexp();
			String asunto = exp.getAsunto();
			String nombreProc = exp.getNombreProcedimientos();
			if(asunto.equals("")){
				asunto = nombreProc;
			}
			%>
			<input type="radio" name="expedientes" onClick="peticion(this.form)" id="<%=asunto%>" value="<%=numexp%>" /> <%=asunto%> <br/>
			 
			<%
		}
	}
%>

<script type="text/javascript">
function peticion(form) {
	
	 for (var i = 0; i < form.expedientes.length; i++) {
        if (form.expedientes[i].checked) {
            var numexp = form.expedientes[i].value;
            var asunto = form.expedientes[i].id;
            opener.document.getElementById('asunto').value = asunto;
			opener.document.getElementById('numExpediente').value = numexp;
			window.close();
        }
	 }

}


		
</script>
</form>
</html>
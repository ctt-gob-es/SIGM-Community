<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="es.dipucr.sigem.api.rule.common.AccesoBBDDRegistro"%>
<%@page import="ieci.tdw.ispac.ispaclib.thirdparty.IThirdPartyAdapter"%>
<%@page import="ieci.tecdoc.sgm.tram.thirdparty.SigemThirdPartyAPI"%>
<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>

<body>
Hola mundo
</body>
<script type="text/javascript">
<%

	String valor = "";
	if(request.getParameter("valor")!=null){
		valor = (String)request.getParameter("valor");
	}
	
	String [] cadenaSplit = valor.split(";");
	String valueAyto = cadenaSplit[0];
	String entidad = cadenaSplit[1];	
	
	AccesoBBDDRegistro accsRegistro = new AccesoBBDDRegistro(entidad);
	IThirdPartyAdapter tercero =  accsRegistro.getDatosTercero(valueAyto);

	
	String cif = "";
	String domicilio = "";
	String localidad = "";
	String provincia = "";
	String cp = "";
	String telefono = "";
	String email = "";
	if(tercero.getIdentificacion()!=null)cif=tercero.getIdentificacion();
	
	if(tercero.getDefaultDireccionPostal()!=null){
		if(tercero.getDefaultDireccionPostal().getDireccionPostal()!=null)domicilio=tercero.getDefaultDireccionPostal().getDireccionPostal();
		if(tercero.getDefaultDireccionPostal().getMunicipio()!=null)localidad=tercero.getDefaultDireccionPostal().getMunicipio();
		if(tercero.getDefaultDireccionPostal().getProvincia()!=null)provincia=tercero.getDefaultDireccionPostal().getProvincia();
		if(tercero.getDefaultDireccionPostal().getCodigoPostal()!=null)cp=tercero.getDefaultDireccionPostal().getCodigoPostal();
		if(tercero.getDefaultDireccionPostal().getTelefono()!=null)telefono=tercero.getDefaultDireccionPostal().getTelefono();
	}
	if(tercero.getDefaultDireccionElectronica()!=null)
		if(tercero.getDefaultDireccionElectronica().getDireccion()!=null)email=tercero.getDefaultDireccionElectronica().getDireccion();
	
	
%>


opener.document.getElementById('cif').value = '<%=cif%>';
opener.document.getElementById('domicilioNotificacion').value = '<%=domicilio%>';
opener.document.getElementById('localidad').value = '<%=localidad%>';
opener.document.getElementById('provincia').value = '<%=provincia%>';
opener.document.getElementById('codigoPostal').value = '<%=cp%>';
opener.document.getElementById('telefono').value = '<%=telefono%>';
opener.document.getElementById('emailSolicitante').value = '<%=email%>';
//alert('despues '+opener.document.getElementById('adios').value);
window.close();
</script>

</html>
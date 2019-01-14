<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="es.dipucr.sigem.api.rule.common.AccesoBBDDRegistro"%>
<%@page import="ieci.tdw.ispac.ispaclib.thirdparty.IThirdPartyAdapter"%>
<%@page import="ieci.tecdoc.sgm.tram.thirdparty.SigemThirdPartyAPI"%>
<%@page import="ieci.tdw.ispac.ispaclib.thirdparty.IElectronicAddressAdapter"%>
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
	String nif = "", valor ="", entidad="";
	String[] cadenaSplit;
	if(request.getParameter("valor")!=null){
		valor = (String)request.getParameter("valor");
	}

	cadenaSplit = valor.split(";");
	nif = cadenaSplit[0];
	entidad = cadenaSplit[1];	

	AccesoBBDDRegistro accsRegistro = new AccesoBBDDRegistro(entidad);
	IThirdPartyAdapter tercero =  accsRegistro.getDatosTerceroByNif(nif);

	String cif = nif;
	String nombre = "";
	String domicilio = "";
	String calle = "";
	String numero = "";
	String piso = "";
	String puerta = "";
	String localidad = "";
	String provincia = "";
	String cp = "";
	String telefono = "";
	String email = "";

	if(tercero != null){
		if(tercero.getIdentificacion()!=null)cif = tercero.getIdentificacion();
		if(tercero.getNombreCompleto()!=null)nombre = tercero.getApellidos()+", "+tercero.getNombre();
		
		if(tercero.getDefaultDireccionPostal()!=null){
			if(tercero.getDefaultDireccionPostal().getDireccionPostal()!=null) domicilio = tercero.getDefaultDireccionPostal().getDireccionPostal();
			if(tercero.getDefaultDireccionPostal().getVia()!=null) calle = tercero.getDefaultDireccionPostal().getVia();
			//if(tercero.getDefaultDireccionPostal().getBloque()!=null)numero = tercero.getDefaultDireccionPostal().getBloque();
			//if(tercero.getDefaultDireccionPostal().getPiso()!=null)piso = tercero.getDefaultDireccionPostal().getPiso();
			//if(tercero.getDefaultDireccionPostal().getPuerta()!=null)puerta = tercero.getDefaultDireccionPostal().getPuerta();
			if(tercero.getDefaultDireccionPostal().getMunicipio()!=null) localidad = tercero.getDefaultDireccionPostal().getMunicipio();
			if(tercero.getDefaultDireccionPostal().getProvincia()!=null) provincia = tercero.getDefaultDireccionPostal().getPais();
			if(provincia == null || provincia.equals("")) provincia = "España";
			if(tercero.getDefaultDireccionPostal().getCodigoPostal()!=null )cp = tercero.getDefaultDireccionPostal().getCodigoPostal();

			IElectronicAddressAdapter[] direccionesElectronicas = tercero.getDireccionesElectronicas();
			if(direccionesElectronicas != null){
				for(int i=0; i< direccionesElectronicas.length; i++){
					try{
						if(direccionesElectronicas[i].getTipo()==IElectronicAddressAdapter.MAIL_TYPE)
							email = direccionesElectronicas[i].getDireccion();
						else if (direccionesElectronicas[i].getTipo()==IElectronicAddressAdapter.MOBILE_PHONE_TYPE)
							telefono = direccionesElectronicas[i].getDireccion();
						else if (direccionesElectronicas[i].getTipo()==IElectronicAddressAdapter.PHONE_TYPE)
							telefono = direccionesElectronicas[i].getDireccion();
					}
					catch(Exception e){}
				}
			}
		}
//		if(tercero.getDefaultDireccionElectronica()!=null)
//			if(tercero.getDefaultDireccionElectronica().getDireccion()!=null)email=tercero.getDefaultDireccionElectronica().getDireccion();
	}
%>

opener.document.getElementById('nif').value = '<%=cif%>';
opener.document.getElementById('nombre').value = '<%=nombre%>';
opener.document.getElementById('calle').value = '<%=domicilio%>';
//opener.document.getElementById('numero').value = '<%=numero%>';
//opener.document.getElementById('numero').value = '';
//opener.document.getElementById('escalera').value = '';
//opener.document.getElementById('planta_puerta').value = '<%=piso%>/<%=puerta%>';
//opener.document.getElementById('planta_puerta').value = '';
opener.document.getElementById('ciudad').value = '<%=localidad%>';
opener.document.getElementById('region').value = '<%=provincia%>';
opener.document.getElementById('c_postal').value = '<%=cp%>';
opener.document.getElementById('movil').value = '<%=telefono%>';
opener.document.getElementById('d_email').value = '<%=email%>';
window.close();
</script>

</html>
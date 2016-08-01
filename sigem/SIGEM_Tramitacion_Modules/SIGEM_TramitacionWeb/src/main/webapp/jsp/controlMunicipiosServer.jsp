<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@ page import="java.io.*"%>
<%@ page import="java.util.*"%>
<%@ page import="org.springframework.web.context.support.*"%>
<%@ page import="org.springframework.web.context.*"%>
<%@ page import="es.dipucr.verifdatos.services.ClienteLigeroProxy"%>
<%@ page import="es.dipucr.verifdatos.model.dao.DatosGeograficosManager"%>
<%@ page import="es.dipucr.verifdatos.model.dao.Generico"%>

<%

String mode=request.getParameter("mode");
String catastro=request.getParameter("catastro");
//out.println("catastro. "+catastro);
boolean iscatastro = catastro!=null?true:false;
//out.println("iscatastro. "+iscatastro);
String auxcode=request.getParameter("auxcode");
//out.println("auxcode. "+auxcode);

StringBuffer sb = new StringBuffer();
ClienteLigeroProxy clienteLigero = new ClienteLigeroProxy();
DatosGeograficosManager datosGeograficos = clienteLigero.getDatosGeograficosManager();

//Thread.sleep(1000);


if("comunidad".equals(mode)) {
	Generico [] dataProvincias = datosGeograficos.getComunidades();
	sb.append("<select id=\"vComunidad\" name=\"vComunidad\" onchange=\"loadProvincia();\">");
	sb.append("<option value=\"\"></option>");	
	for(int i = 0; i < dataProvincias.length; i++) {
		Generico comunidad = dataProvincias[i];
		sb.append("<option value=\"" + comunidad.getCodigo() + "\">" + comunidad.getDescripcion() + "</option>");
	}
	sb.append("</select>");
	
		
		
} 	else if("provincia".equals(mode)) {
	String codComunidad = request.getParameter("codComunidad");
	if(!"".equals(codComunidad)) {
		Generico[] dataComunidad = clienteLigero.getProvinciasByComunidad(datosGeograficos.getProvincias(), codComunidad);
		sb.append("<select id=\"vProvincia\"  datoespecifico=\"1\" name=\"vProvincia\" onchange=\"loadMunicipio();\">");
		sb.append("<option value=\"\"></option>");	
		for(int i = 0; i < dataComunidad.length; i++) {
			Generico provincia = dataComunidad[i];
			sb.append("<option value=\"" + provincia.getCodigo() + "\">" + provincia.getDescripcion() + "</option>");
		}
		sb.append("</select>");
		
	}
} else if("municipio".equals(mode)) {
	String codProvincia = request.getParameter("codProvincia");
	if(!"".equals(codProvincia)) {
		Generico[] dataProvincias = clienteLigero.getMuniciposByProvincia(datosGeograficos.getMunicipios(), codProvincia);
		sb.append("<select id=\"vMunicipio\"  datoespecifico=\"1\"  name=\"vMunicipio\">");
		sb.append("<option value=\"\"></option>");	
		for(int i = 0; i < dataProvincias.length; i++) {
			Generico municipio = dataProvincias[i];
			sb.append("<option value=\"" +  municipio.getCodigo() + "\">" + municipio.getDescripcion() + "</option>");
		}
		sb.append("</select>");
		
				  
	}
} else if("listComunidad".equals(mode)) {
	Generico[] dataComunidades = datosGeograficos.getComunidades();
	sb.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><list>");
	for(int i = 0; i < dataComunidades.length; i++) {
		Generico comunidades = dataComunidades[i];
		sb.append("<item codigo=\"" + comunidades.getCodigo() + "\" value=\"" + comunidades.getDescripcion() + "\"/>");
	}
	sb.append("</list>");
}else if("pais".equals(mode)) {
	Generico[] dataPaises = datosGeograficos.getPaises();
	String espain = datosGeograficos.getCOD_ESPANIA();
	sb.append("<select id=\"vPais\"  datoespecifico=\"1\"  name=\"vPais\" onchange=\"selectPais('"+espain+"');\">");
	sb.append("<option value=\"\"></option>");	
	for(int i = 0; i < dataPaises.length; i++) {
		Generico pais = dataPaises[i];
		sb.append("<option value=\"" + pais.getCodigo() + "\">" + pais.getDescripcion() + "</option>"); 
	}
	sb.append("</select>");
}

out.println(sb.toString());


%>
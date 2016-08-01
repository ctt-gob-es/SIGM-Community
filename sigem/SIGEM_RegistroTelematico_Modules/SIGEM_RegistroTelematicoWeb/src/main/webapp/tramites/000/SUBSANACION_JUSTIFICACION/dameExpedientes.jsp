<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="es.dipucr.sigem.api.rule.common.AccesoBBDDRegistro"%>
<%@page import="ieci.tdw.ispac.ispaclib.thirdparty.IThirdPartyAdapter"%>
<%@page import="ieci.tecdoc.sgm.tram.thirdparty.SigemThirdPartyAPI"%>
<%@page import="es.dipucr.sigem.registroTelematicoWeb.formulario.common.XmlCargaDatos"%>
<%@page import="org.w3c.dom.Document"%>
<%@page import="javax.xml.parsers.DocumentBuilderFactory"%>
<%@page import="javax.xml.parsers.DocumentBuilder"%>
<%@page import="org.w3c.dom.Element"%>
<%@page import="org.w3c.dom.NodeList"%>
<%@page import="org.w3c.dom.Node"%>

<html>
<head>

<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Insert title here</title>
</head>

<body>
Recuperando datos de solicitudes anteriores.
</body>
<script type="text/javascript">
	var expedientes = opener.document.getElementById('expedientes');
	expedientes.options.length=1;
<%

	String valor = "";
	if(request.getParameter("valor")!=null){
		valor = (String)request.getParameter("valor");
	}
	
	String [] cadenaSplit = valor.split(";");
	String nifCif = cadenaSplit[0];
	String anio = cadenaSplit[1];	
	String entidad = cadenaSplit[2];	

	//Borramos las opciones que había y añadimos las nuevas
	String ficheroSolAyto = XmlCargaDatos.getDatosConsulta("Subsanacion", "select a.numexp, replace(replace(replace(case when (select trim(a.numexp || ' - ' ||d.asunto|| ' - ' ||a.asunto) from dpcr_sol_conv_sub c, spac_expedientes d where c.numexp = a.numexp and c.numexp_padre = d.numexp) is null then trim(a.numexp || ' - ' ||a.asunto) else (select trim(a.numexp || ' - ' ||d.asunto|| ' - ' ||a.asunto) from dpcr_sol_conv_sub c, spac_expedientes d where c.numexp = a.numexp and c.numexp_padre = d.numexp) end , '</br>',' '),'</b>',' '),'<b>',' ') from  spac_expedientes a where upper(a.nifciftitular) like upper(#%"+ nifCif +"%#) and a.numexp like #DPCR"+anio+"/%# and a.estadoadm in (#PR#,#RS#,#AP#,#NT#,#NE#,#JF#,#DC#,#DI#,#ES#,#EJ#,#CT#,#JP#,#VE#,#PENREI#) and a.fcierre is null and nombreprocedimiento not like #%BOP - Solicitud de inserción de anuncio%# and nombreprocedimiento not like #%Datos del Tercero y Gestión de Representantes%# and nombreprocedimiento not like #%Comunicación administrativa electrónica Registro (Carta digital)%#",entidad);	
	Document dom;
	DocumentBuilderFactory dbf;
	DocumentBuilder db;
	
	dbf = DocumentBuilderFactory.newInstance();
	
	try{
		db = dbf.newDocumentBuilder();
		dom = db.parse(ficheroSolAyto);
	
		Element rootElement = dom.getDocumentElement();

		NodeList nodeList = rootElement.getElementsByTagName("dato");

		if(nodeList != null && nodeList.getLength() > 0){
			for(int i = 0; i < nodeList.getLength(); i++){
				Node exp = nodeList.item(i);
				if( exp.getNodeType() == Node.ELEMENT_NODE){
					Element elemento = (Element) exp;	
		
					NodeList lista = elemento.getElementsByTagName("valor").item(0).getChildNodes(); 
					Node datoValor = (Node) lista.item(0); 
					String numexp =  datoValor.getNodeValue();
					
					lista = elemento.getElementsByTagName("sustituto").item(0).getChildNodes(); 
					datoValor = (Node) lista.item(0); 
					String asunto =  datoValor.getNodeValue();
					%>						
						op = opener.document.createElement('option');  
						op.value = '<%=numexp%>'; 
						op.text = '<%=asunto.replaceAll("[\n\r]","")%>'; 
						expedientes.add(op); 
					<%
				}
			}

		}
	}
	catch(Exception ex) {}%>


window.close();
</script>

</html>
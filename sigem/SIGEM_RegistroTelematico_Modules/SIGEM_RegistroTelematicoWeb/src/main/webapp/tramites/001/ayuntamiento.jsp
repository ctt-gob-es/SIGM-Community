<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="es.dipucr.sigem.api.rule.common.AccesoBBDDRegistro"%>
<%@page import="ieci.tecdoc.sgm.core.services.terceros.dto.Tercero"%>
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
Recuperando datos del Ayuntamiento
</body>
<script type="text/javascript">
	var expedientes = opener.document.getElementById('expediente');

	if(expedientes){
		expedientes.options.length=1;
	}

	var op;
<%

	String valor = "";
	if(request.getParameter("valor")!=null){
		valor = (String)request.getParameter("valor");
	}
	
	String [] cadenaSplit = valor.split(";");
	String valueAyto = cadenaSplit[0];
	String entidad = cadenaSplit[1];	
	
	AccesoBBDDRegistro accsRegistro = new AccesoBBDDRegistro(entidad);
	Tercero tercero =  accsRegistro.getDatosTercero(valueAyto);

	
	String cif = "";
	String domicilio = "";
	String localidad = "";
	String provincia = "";
	String cp = "";
	String telefono = "";
	String email = "";
	
	if(null != tercero){
		if(tercero.getIdentificacion()!=null)cif=tercero.getIdentificacion();
		
		if(tercero.getDireccionPostalPredeterminada()!=null){
			if(tercero.getDireccionPostalPredeterminada().getDireccionPostal()!=null){
				domicilio=tercero.getDireccionPostalPredeterminada().getDireccionPostal();
			}
			if(tercero.getDireccionPostalPredeterminada().getMunicipio()!=null){
				localidad=tercero.getDireccionPostalPredeterminada().getMunicipio();
			}
			if(tercero.getDireccionPostalPredeterminada().getProvincia()!=null){
				provincia=tercero.getDireccionPostalPredeterminada().getProvincia();
			}
			if(tercero.getDireccionPostalPredeterminada().getCodigoPostal()!=null){
				cp=tercero.getDireccionPostalPredeterminada().getCodigoPostal();
			}
			if(tercero.getDireccionPostalPredeterminada().getTelefono()!=null){
				telefono=tercero.getDireccionPostalPredeterminada().getTelefono();
			}
		}
		if(tercero.getDireccionElectronicaPredeterminada()!=null){
			if(tercero.getDireccionElectronicaPredeterminada().getDireccion()!=null){
				email=tercero.getDireccionElectronicaPredeterminada().getDireccion();
			}
		}
	}
	
	//Borramos las opciones que había y añadimos las nuevas
	//String ficheroSolAyto = XmlCargaDatos.getDatosConsulta("Expedientes", "select a.numexp, case when (select trim(a.numexp || # - # ||d.asunto|| # - # ||a.asunto) from  dpcr_sol_conv_sub c, spac_expedientes d where c.numexp = a.numexp and c.numexp_padre = d.numexp) is null then trim(a.numexp || # - # ||a.asunto) else (select trim(a.numexp || # - # ||d.asunto|| # - # ||a.asunto) from  dpcr_sol_conv_sub c, spac_expedientes d where c.numexp = a.numexp and c.numexp_padre = d.numexp) end  from  spac_expedientes a where upper(a.nifciftitular) like upper(#%"+cif+"%#) and a.estadoadm in (#PR#,#RS#,#AP#,#NT#,#NE#,#JF#,#DC#,#DI#,#ES#,#EJ#,#CT#,#JP#,#VE#) and a.fcierre is null and nombreprocedimiento not like #%BOP - Solicitud de inserción de anuncio%# and nombreprocedimiento not like #%Datos del Tercero y Gestión de Representantes%# and nombreprocedimiento not like #%Comunicación administrativa electrónica Registro (Carta digital)%#",entidad);	
	//Document dom;
	//DocumentBuilderFactory dbf;
	//DocumentBuilder db;
	
	//dbf = DocumentBuilderFactory.newInstance();
	
	try{
	/*	db = dbf.newDocumentBuilder();
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
						op.text = '<%=asunto.replaceAll("[\n\r]","").replaceAll("\'","\\\\'")%>'; 
						expedientes.add(op); 
					<%
				}
			}

		}
	*/
%>

if(opener.document.getElementById('numExpediente')){
	opener.document.getElementById('numExpediente').value = '';
}

opener.document.getElementById('cif').value = '<%=cif%>';
opener.document.getElementById('domicilioNotificacion').value = '<%=domicilio%>';
opener.document.getElementById('localidad').value = '<%=localidad%>';
opener.document.getElementById('provincia').value = '<%=provincia%>';
opener.document.getElementById('codigoPostal').value = '<%=cp%>';
opener.document.getElementById('telefono').value = '<%=telefono%>';
opener.document.getElementById('emailSolicitante').value = '<%=email%>';

<%
	}
	catch(Exception ex) {
		ex.printStackTrace();
	}%>

window.close();
</script>

</html>
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
<title>Recuperando los Representados</title>
</head>

<body>
Recuperando los Representados...
</body>
<script type="text/javascript">
	var ayuntamiento = opener.document.getElementById('ayuntamiento');
	var asociaciones_div = opener.document.getElementById('asociaciones_div');
	var ayuntamiento_div = opener.document.getElementById('ayuntamiento_div');
	var remitente_div = opener.document.getElementById('remitente_div');
	var mensajeRepresente_div = opener.document.getElementById('mensajeRepresente_div');
	
	ayuntamiento.options.length=1;
	asociaciones_div.style.display = 'none';
	
<%

	String valor = "";
	if(request.getParameter("valor")!=null){
		valor = (String)request.getParameter("valor");
	}
	
	String [] cadenaSplit = valor.split(";");
	String nifCif = cadenaSplit[0];
	String entidad = cadenaSplit[1];
	
	String consulta = "";
	consulta = "SELECT EXP.IDENTIDADTITULAR, EXP.NIFCIFTITULAR FROM SPAC_DT_INTERVINIENTES PART JOIN SPAC_EXPEDIENTES EXP ON PART.NDOC=#"+nifCif+"# AND PART.NUMEXP = EXP.NUMEXP AND EXP.CODPROCEDIMIENTO IN (SELECT COD_PCD FROM SPAC_CT_PROCEDIMIENTOS WHERE ID_PADRE IN (select ID from spac_ct_procedimientos where cod_pcd=#REPRESENTANTES#)) GROUP BY EXP.IDENTIDADTITULAR, EXP.NIFCIFTITULAR";
	//Borramos las opciones que había y añadimos las nuevas
	String ficheroSolAyto = XmlCargaDatos.getDatosConsulta("Subsanacion", consulta,entidad);	
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
			%>
				ayuntamiento_div.style.display = '';
				remitente_div.style.display = '';			
			<%

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
						op.value = '<%=asunto%>'; 
						op.text = '<%=numexp%>'; 
						ayuntamiento.add(op); 
					<%
				}
			}
		} else {
			%>				
				mensajeRepresente_div.style.display='';
			<%
		}
	}
	catch(Exception ex) {}%>


window.close();
</script>

</html>
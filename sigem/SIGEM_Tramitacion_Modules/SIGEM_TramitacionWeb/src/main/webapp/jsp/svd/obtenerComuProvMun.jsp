<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%@page import="es.dipucr.sigem.registroTelematicoWeb.formulario.DatosClienteLigero"%>
<%@page import="org.w3c.dom.Document"%>
<%@page import="javax.xml.parsers.DocumentBuilderFactory"%>
<%@page import="javax.xml.parsers.DocumentBuilder"%>
<%@page import="org.w3c.dom.Element"%>
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
var provincia = opener.document.getElementById('provincia');
var municipio = opener.document.getElementById('municipio');
var provinciaNacimiento = opener.document.getElementById('provinciaNacimiento');
var municipioNacimiento = opener.document.getElementById('municipioNacimiento');
<%

	String valor = "";
	if(request.getParameter("valor")!=null){
		valor = (String)request.getParameter("valor");
	}
	String [] vValor = valor.split(";");
	
	String ficheroSolAyto = "";
	if(vValor[0].equals("COMUNIDAD")){
		ficheroSolAyto = DatosClienteLigero.getDatosProvinciaByComunidad(vValor[1]);
	}
	if(vValor[0].equals("PROVINCIA")){
		ficheroSolAyto = DatosClienteLigero.getDatosMunicipioByProvincia(vValor[1]);
	}
	
	String hecho = vValor[2];
	Document dom;
	DocumentBuilderFactory dbf;
	DocumentBuilder db;
	
	dbf = DocumentBuilderFactory.newInstance();
	
	String idValue = "";
	String sustituto = "";
	
	try{
		db = dbf.newDocumentBuilder();
		dom = db.parse(ficheroSolAyto);
	
		Element rootElement = dom.getDocumentElement();

		NodeList nodeList = rootElement.getElementsByTagName("dato");

		if(nodeList != null && nodeList.getLength() > 0){
			
			%>						
			op = opener.document.createElement('option');  
			op.value = '-------------------------'; 
			op.text = '-';
			<%if(hecho.equals("REGISTRO") && vValor[0].equals("COMUNIDAD")){%>
				provincia.add(op); 
			<%}
			if(hecho.equals("REGISTRO") && vValor[0].equals("PROVINCIA")){%>
				municipio.add(op); 
			<%}
			if(hecho.equals("NACIMIENTO") && vValor[0].equals("COMUNIDAD")){%>
				provinciaNacimiento.add(op); 
			<%}
			if(hecho.equals("NACIMIENTO") && vValor[0].equals("PROVINCIA")){%>
				municipioNacimiento.add(op); 
			<%}
			
			for(int i = 0; i < nodeList.getLength(); i++){
				Node exp = nodeList.item(i);
				if( exp.getNodeType() == Node.ELEMENT_NODE){
					Element elemento = (Element) exp;	
		
					NodeList lista = elemento.getElementsByTagName("valor").item(0).getChildNodes(); 
					Node datoValor = (Node) lista.item(0); 
					idValue =  datoValor.getNodeValue();
					
					NodeList lista2 = elemento.getElementsByTagName("sustituto").item(0).getChildNodes(); 
					Node datoValor2 = (Node) lista2.item(0); 
					sustituto =  datoValor2.getNodeValue();
				%>						
						op = opener.document.createElement('option');  
						op.value = '<%=idValue%>'; 
						op.text = '<%=sustituto%>';
						<%if(hecho.equals("REGISTRO") && vValor[0].equals("COMUNIDAD")){%>
							provincia.add(op); 
						<%}
						if(hecho.equals("REGISTRO") && vValor[0].equals("PROVINCIA")){%>
							municipio.add(op); 
						<%}
						if(hecho.equals("NACIMIENTO") && vValor[0].equals("COMUNIDAD")){%>
							provinciaNacimiento.add(op); 
						<%}
						if(hecho.equals("NACIMIENTO") && vValor[0].equals("PROVINCIA")){%>
							municipioNacimiento.add(op); 
						<%}
				}
			}
		}
	}
	catch(Exception ex) {}%>


window.close();
</script>

</html>
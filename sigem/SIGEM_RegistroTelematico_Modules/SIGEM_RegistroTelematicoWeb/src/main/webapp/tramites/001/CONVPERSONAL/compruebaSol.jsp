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
var puestoTrabajo = opener.document.getElementById('puestoTrabajo');
puestoTrabajo.options.length=1;
<%

	String valor = "";
	if(request.getParameter("valor")!=null){
		valor = (String)request.getParameter("valor");
	}
	
	String [] cadenaSplit = valor.split(";");
	String entidad = cadenaSplit[0];	
	String convocatoria = cadenaSplit[1];

	String ficheroSolAyto = XmlCargaDatos.getDatosConsulta("SolicitudesAyto", "select puestotrabajo, departamento from personal_puesto_trabajo where numexp='"+convocatoria+"'",entidad);
	
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
					
					NodeList listaDepartamento = elemento.getElementsByTagName("sustituto").item(0).getChildNodes();
					
					Node datoValorDepartamento = (Node) listaDepartamento.item(0); 
					String departamento =  datoValorDepartamento.getNodeValue();
					String [] vDepartamento = departamento.split(" - ");
					String resDepartamento = "";
					if(vDepartamento.length>=2){
						resDepartamento = vDepartamento[0];
					}
					
					Node datoValor = (Node) lista.item(0); 
					String puesto =  datoValor.getNodeValue();
					String [] vPuesto = puesto.split(" - ");
					String resPuesto = "";
					if(vPuesto.length>=2){
						resPuesto = resDepartamento.substring(0, 1)+"."+resDepartamento.substring(1, 3)+"."+vPuesto[0]+ " - " + vPuesto[1];
					}
					
					
					
				%>						
						op = opener.document.createElement('option');  
						op.value = '<%=convocatoria%>'; 
						op.text = '<%=resPuesto%>'; 
						puestoTrabajo.add(op); 
				<%
				}
			}
		}
	}
	catch(Exception ex) {}%>


window.close();
</script>

</html>
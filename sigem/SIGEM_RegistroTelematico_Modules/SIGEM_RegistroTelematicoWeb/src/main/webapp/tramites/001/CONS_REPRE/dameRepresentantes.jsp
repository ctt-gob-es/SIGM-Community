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
		<title>Recuperando la lista de representados</title>
	</head>

	<body>
		Recuperando el listado de representados, por favor espere un momento.
	</body>
	<script type="text/javascript">
		var asociaciones_jasper = opener.document.getElementById('asociaciones_jasper');
		var asociaciones_xsd = opener.document.getElementById('asociaciones_xsd');
		var asociaciones_lbl = opener.document.getElementById('asociaciones_lbl');
		var aviso_img = opener.document.getElementById('aviso_img');
		<%
			String valor = "";
			if(request.getParameter("valor")!=null){
				valor = (String)request.getParameter("valor");
			}
		
			String [] cadenaSplit = valor.split(";");
			String nifCif = cadenaSplit[0];
			String entidad = cadenaSplit[1];
		
			String consulta = "";
			consulta = "SELECT EXP.IDENTIDADTITULAR, EXP.NIFCIFTITULAR FROM SPAC_DT_INTERVINIENTES PART JOIN SPAC_EXPEDIENTES EXP ON PART.NDOC=#"+nifCif+"# AND PART.NUMEXP = EXP.NUMEXP AND EXP.CODPROCEDIMIENTO IN (SELECT COD_PCD FROM SPAC_CT_PROCEDIMIENTOS WHERE ID_PADRE IN (select ID from spac_ct_procedimientos where cod_pcd=#REPRESENTANTES#)) GROUP BY EXP.IDENTIDADTITULAR, EXP.NIFCIFTITULAR ORDER BY EXP.IDENTIDADTITULAR";
			//Borramos las opciones que había y añadimos las nuevas
			String ficheroSolAyto = XmlCargaDatos.getDatosConsulta("DatosRepresentados", consulta, entidad);	
			Document dom;
			DocumentBuilderFactory dbf;
			DocumentBuilder db;

			dbf = DocumentBuilderFactory.newInstance();

			try{
				db = dbf.newDocumentBuilder();
				dom = db.parse(ficheroSolAyto);
		
				Element rootElement = dom.getDocumentElement();
				NodeList nodeList = rootElement.getElementsByTagName("dato");

				%>
					asociaciones_lbl.innerHTML = '';
				<% 

				if(nodeList != null && nodeList.getLength() > 0){
					%>
						asociaciones_lbl.innerHTML = '<p>El titular del documento número <b><%=nifCif%></b> figura registrado en esta Corporación Local como representante o autorizado para recepción de notificaciones de las siguientes entidades: </p>';
						asociaciones_xsd.innerHTML = '<p>El titular del documento número <b><%=nifCif%></b> figura registrado en esta Corporación Local como representante o autorizado para recepción de notificaciones de las siguientes entidades: </p>';
						asociaciones_jasper.innerHTML = 'El titular del documento número <style isBold="true" pdfFontName="Helvetica-Bold"><%=nifCif%></style> figura registrado en esta Corporación Local como representante o autorizado para recepción de notificaciones de las siguientes entidades: <br/>';
					<%

					for(int i = 1; i <= nodeList.getLength(); i++){
						Node exp = nodeList.item(i-1);
						if( exp.getNodeType() == Node.ELEMENT_NODE){
							Element elemento = (Element) exp;	
			
							NodeList lista = elemento.getElementsByTagName("valor").item(0).getChildNodes(); 
							Node datoValor = (Node) lista.item(0); 
							String nombreAsociacion =  datoValor.getNodeValue();
						
							lista = elemento.getElementsByTagName("sustituto").item(0).getChildNodes(); 
							datoValor = (Node) lista.item(0); 
							String nifAsociacion =  datoValor.getNodeValue();
							%>
								asociaciones_lbl.innerHTML = asociaciones_lbl.innerHTML + '<li><%=i%>.- <b><%=nifAsociacion%></b> - <%=nombreAsociacion%></li>';
								asociaciones_xsd.innerHTML = asociaciones_lbl.innerHTML + '<li><%=i%>.- <b><%=nifAsociacion%></b> - <%=nombreAsociacion%></li>';
								asociaciones_jasper.innerHTML = asociaciones_jasper.innerHTML + '    <%=i%>.- <style isBold="true" pdfFontName="Helvetica-Bold"><%=nifAsociacion%></style> - <%=nombreAsociacion%><br/>';
							<%
						}
					}
				}
				else{
					%>
						asociaciones_lbl.innerHTML = '<p>El titular del documento número <b><%=nifCif%> no figura registrado en esta Corporación Local como representante o autorizado para recepción de notificaciones de ninguna entidad.</b></p> Para dar de alta representantes se debe acceder a <b>COMPARECE</b> y rellenar el <a href="http://se1.dipucr.es:8080/SIGEM_RegistroTelematicoWeb/realizarSolicitudRegistro.do?tramiteId=SOLALTMODBAJCOMP" style="position: relative; width:100%; font-size:11px; font-weight:bold; text-decoration:underline; cursor:hand; color:#006699;">formulario de alta de representantes</a>.';
						asociaciones_xsd.innerHTML = '<p>El titular del documento número <b><%=nifCif%> no figura registrado en esta Corporación Local como representante o autorizado para recepción de notificaciones de ninguna entidad.</b></p> Para dar de alta representantes se debe acceder a <b>COMPARECE</b> y rellenar el <b>FORMULARIO DE ALTA DE REPRESENTANTES</b>.';
						asociaciones_jasper.innerHTML = 'El titular del documento número <style isBold="true" pdfFontName="Helvetica-Bold"><%=nifCif%> no figura registrado en esta Corporación Local como representante o autorizado para recepción de notificaciones de ninguna entidad</style>.<br/>Para dar de alta representantes se debe acceder a la sede electrónica de la Diputación Provincial de Ciudad Real <style isBold="true" pdfFontName="Helvetica-Bold">(https://sede.dipucr.es)</style> y una vez en ella acceder a <style isBold="true" pdfFontName="Helvetica-Bold">COMPARECE</style> y rellenar el <style isBold="true" pdfFontName="Helvetica-Bold">FORMULARIO DE ALTA DE REPRESENTANTES</style>.';
					<%
				}
			}
			catch(Exception ex) {}
		%>
		aviso_img.style.display="none";
		window.close();
	</script>
</html>
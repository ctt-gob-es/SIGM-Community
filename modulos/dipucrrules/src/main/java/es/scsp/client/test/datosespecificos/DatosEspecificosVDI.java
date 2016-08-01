package es.scsp.client.test.datosespecificos;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DatosEspecificosVDI extends DatosEspecificosAdapterBase {
	private String EXTRANJERO = "66";
	public String createHtmlController(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		sb.append("<link rel=\"stylesheet\" type=\"text/css\" media=\"all\" href=\"js/jscalendar-1.0/skins/aqua/theme.css\" title=\"Aqua\" />");
		sb.append("<link rel=\"alternate stylesheet\" type=\"text/css\" media=\"all\" href=\"js/jscalendar-1.0/calendar-blue.css\" title=\"winter\" />");
		sb.append("<link rel=\"alternate stylesheet\" type=\"text/css\" media=\"all\" href=\"js/jscalendar-1.0/calendar-blue2.css\" title=\"blue\" />");
		sb.append("<link rel=\"alternate stylesheet\" type=\"text/css\" media=\"all\" href=\"js/jscalendar-1.0/calendar-brown.css\" title=\"summer\" />");
		sb.append("<link rel=\"alternate stylesheet\" type=\"text/css\" media=\"all\" href=\"js/jscalendar-1.0/calendar-green.css\" title=\"green\" />");
		sb.append("<link rel=\"alternate stylesheet\" type=\"text/css\" media=\"all\" href=\"js/jscalendar-1.0/calendar-win2k-1.css\" title=\"win2k-1\" />");
		sb.append("<link rel=\"alternate stylesheet\" type=\"text/css\" media=\"all\" href=\"js/jscalendar-1.0/calendar-win2k-2.css\" title=\"win2k-2\" />");
		sb.append("<link rel=\"alternate stylesheet\" type=\"text/css\" media=\"all\" href=\"js/jscalendar-1.0/calendar-win2k-cold-1.css\" title=\"win2k-cold-1\" />");
		sb.append("<link rel=\"alternate stylesheet\" type=\"text/css\" media=\"all\" href=\"js/jscalendar-1.0/calendar-win2k-cold-2.css\" title=\"win2k-cold-2\" />");
		sb.append("<link rel=\"alternate stylesheet\" type=\"text/css\" media=\"all\" href=\"js/jscalendar-1.0/calendar-system.css\" title=\"system\" />");
		sb.append("<script type=\"text/javascript\" src=\"js/jscalendar-1.0/calendar.js\"></script>");
		sb.append("<script type=\"text/javascript\" src=\"js/jscalendar-1.0/calendarioSCSP.js\"></script>");
		sb.append("<script type=\"text/javascript\" src=\"js/jscalendar-1.0/lang/calendar-en.js\"></script>");
		sb.append("<div>");
		sb.append("<table>");

		sb.append("<tr>");
		sb.append("<td class=\"label\">Sexo:</td>");
		sb.append("<td><select  style=\"width:100px\" name=\"sexo\">");
		sb.append("<Option value=\"\"></Option>");
		sb.append("<Option value=\"M\">Masculino</Option>");
		sb.append("<Option value=\"F\">Femenino</Option>");
		sb.append("<Option value=\"I\">Desconocido</Option>");
		sb.append("</select><span></span></td>");
		sb.append("</tr>");

		sb.append("<tr>");
		sb.append("<td class=\"label\">N&uacute;mero de serie del soporte (IDESP):</td>");
		sb.append("<td><input name=\"idesp\"  style=\"width:100px\" value=\"\" maxlength=\"9\"></input></td>");
		sb.append("</tr>");

		sb.append("<tr>");
		sb.append("<td class=\"label\">Fecha de nacimiento:</td>");
		sb.append("<td><input style=\"width:100px\" id=\"fechaNacimiento\" name=\"fechaNacimiento\" value=\"\" maxlength=\"8\" onclick=\"return showCalendar('fechaNacimiento', '%Y%m%d');\"></input></td>");
		sb.append("<td class=\"label\"></td>");
		//sb.append("<td><input id=\"jsCal2\" style=\"width:5em\" type=\"reset\" value=\"...\" width=\"5\" onclick=\"return showCalendar('fechaNacimiento', '%Y%m%d');\"></td>");
		sb.append("</tr>");
		
		sb.append("<tr>");
		sb.append("<td class=\"label\">Pa&iacute;s de nacimiento:</td>");
		//sb.append("<td><input name=\"paisNacimiento\" value=\"\" maxlength=\"8\"></input></td>");
		sb.append("<td><input id=\"paisNacimiento\" name=\"paisNacimiento\" type=\"hidden\" value=\"\" maxlength=\"8\"></input>" +
				"<input id=\"extranjero\" name=\"extranjero\" type=\"hidden\" value=\"\" ></input>" +
				"<div class=\"divControlPaises\" id=\"divControlPaises\"/></td>");
		sb.append("</tr>");
		/*
		sb.append("<tr>");
		sb.append("<td class=\"label\">Provincia de nacimiento:</td>");
		sb.append("<td><input name=\"provinciaNacimiento\" value=\"\" maxlength=\"8\"></input></td>");
		sb.append("</tr>");
		*/
		
		sb.append("<tr>");
		sb.append("<td class=\"label\">Comunidad aut&oacute;noma de nacimiento:</td>");
		sb.append("<td><div class=\"divControlComunidades\" id=\"divControlComunidades\"/></td>");
		sb.append("</tr>");
		
		sb.append("<tr>");
		sb.append("<td class=\"label\">Provincia de nacimiento:</td>");
		sb.append("<td><div class=\"divControlProvincias\" id=\"divControlProvincias\"/></td>");
		sb.append("</tr>");

		sb.append("<tr>");
		sb.append("<td class=\"label\"></td>");
		sb.append("<td><div class=\"divControlMunicipios\" id=\"divControlMunicipios\"/></td>");
		sb.append("</tr>");
		

		sb.append("</table>");
		sb.append("</div>");
		
		sb.append("<script>");
		sb.append("loadPaises();");
		sb.append("loadComunidad();");
		sb.append("ocultaDiv('divControlMunicipios');");
		sb.append("</script>");
		return sb.toString();
	
		
	}

		public Element parseXmlDatosEspecificos(HttpServletRequest request, String version) {
		try {
			String sexo = readParameter("sexo", request);
			String idesp = readParameter("idesp", request);
			String fechaNac = readParameter("fechaNacimiento", request);
			
			String provinciaNac = readParameter("vProvincia", request);
			String paisNac = readParameter("paisNacimiento", request);
			String extranjero = readParameter("extranjero", request);
			if(extranjero != null &&extranjero.equals("true")){
				provinciaNac = EXTRANJERO;
			}
			String solNombre = readParameter("solNombre", request);
			String solApellido1 = readParameter("solApellido1", request);
			String solApellido2 = readParameter("solApellido2", request);
			String solIdFuncionario = readParameter("solIdFuncionario", request);
			String solNumFuncionario = readParameter("solNumFuncionario", request);
			String solTelefono = readParameter("solTelefono", request);
			String codigoOrganizacion = readParameter("codigoOrganizacion", request);
			String nombreOrganizacion = readParameter("nombreOrganizacion", request);
			String tipoVia = readParameter("tipoVia", request);
			String nombreVia = readParameter("nombreVia", request);
			String provinciaResidencia = readParameter("provinciaResidencia", request);
			String paisResidencia = readParameter("paisResidencia", request);

			StringBuilder sb = new StringBuilder();
			sb.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
			sb.append("<DatosEspecificos xmlns=\"" + getXmlnsDatosEspecificos(version) + "\">");
			sb.append("<SolicitanteDatos>");
			sb.append("<Tipo>app</Tipo>");
			sb.append(solNombre == null ? "" : "<Nombre>" + solNombre + "</Nombre>");
			sb.append(solApellido1 == null ? "" : "<Apellido1>" + solApellido1 + "</Apellido1>");
			sb.append(solApellido2 == null ? "" : "<Apellido2>" + solApellido1 + "</Apellido2>");
			sb.append(solIdFuncionario == null ? "" : "<IdFuncionario>" + solIdFuncionario + "</IdFuncionario>");
			sb.append(solNumFuncionario == null ? "" : "<NumFuncionario>" + solNumFuncionario + "</NumFuncionario>");
			sb.append(solTelefono == null ? "" : "<Telefono>" + solIdFuncionario + "</Telefono>");
			if(codigoOrganizacion != null || nombreOrganizacion != null) {
				sb.append("<Organizacion>");
				sb.append(codigoOrganizacion == null ? "" : "<CodOrganizacion>" + codigoOrganizacion + "</CodOrganizacion>");
				sb.append(nombreOrganizacion == null ? "" : "<NombreOrganizacion>" + nombreOrganizacion + "</NombreOrganizacion>");
				sb.append("</Organizacion>");
			}
			sb.append("</SolicitanteDatos>");
			sb.append("<Solicitud>");
			sb.append(sexo == null ? "" : "<Sexo>" + sexo + "</Sexo>");
			sb.append(idesp == null ? "" : "<NumSoporte>" + idesp + "</NumSoporte>");
			if (fechaNac != null || provinciaNac != null || paisNac != null) {
				sb.append("<DatosNacimiento>");
				sb.append(fechaNac == null ? "" : "<Fecha>" + fechaNac + "</Fecha>");
				sb.append(provinciaNac == null ? "" : "<Provincia>" + provinciaNac + "</Provincia>");
				sb.append(paisNac == null ? "" : "<Pais>" + paisNac + "</Pais>");
				sb.append("</DatosNacimiento>");
			}
			if(tipoVia != null || nombreVia != null || provinciaResidencia != null || paisResidencia != null) {
				sb.append("<Direccion>");
				sb.append(tipoVia == null ? "" : "<TipoVia>" + tipoVia + "</TipoVia>");
				sb.append(nombreVia == null ? "" : "<NombreVia>" + nombreVia + "</NombreVia>");
				sb.append(provinciaResidencia == null ? "" : "<Provincia>" + provinciaResidencia + "</Provincia>");
				sb.append(paisResidencia == null ? "" : "<Pais>" + paisResidencia + "</Pais>");
				sb.append("</Direccion>");
			}
			sb.append("</Solicitud>");
			sb.append("</DatosEspecificos>");
			InputStream in = new ByteArrayInputStream(sb.toString().getBytes());
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
			return doc.getDocumentElement();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}

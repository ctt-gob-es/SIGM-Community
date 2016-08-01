package es.scsp.client.test.datosespecificos;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DatosEspecificosVDR extends DatosEspecificosAdapterBase {

	public String createHtmlController(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		sb.append("<!-- enlaces para el uso del calendario-->");
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
		sb.append("<td class=\"label\">Espa&ntilde;ol:</td>");
		sb.append("<td><select name=\"esp\">");
		sb.append("<Option value=\"s\">Si</Option>");
		sb.append("<Option value=\"n\">No</Option>");
		sb.append("</select><span>&nbsp;*</span></td>");
		sb.append("</tr>");
		
		//controles ajax
		sb.append("<tr>");
		sb.append("<td >Comunidad aut&oacute;noma de residencia:</td>");
		sb.append("<td style=\"width:20px\"><div class=\"divControlComunidades\" id=\"divControlComunidades\"/></td>");
		sb.append("</tr>");
		sb.append("<tr>");
		sb.append("<td >Provincia de residencia:</td>");
		sb.append("<td style=\"width:20px\"><div class=\"divControlProvincias\" id=\"divControlProvincias\"/></td>");
		sb.append("</tr>");
		sb.append("<tr>");
		sb.append("<td >Municipio de residencia:</td>");
		sb.append("<td style=\"width:20px\"><div class=\"divControlMunicipios\" id=\"divControlMunicipios\"/></td>");
		sb.append("</tr>");
		
		
		sb.append("<tr>");
		sb.append("<td class=\"label\">Fecha de nacimiento:</td>");
		sb.append("<td><input  style=\"width:80px\" id=\"fechaNacimiento\" onclick=\"return showCalendar('fechaNacimiento', '%Y%m%d');\" name=\"fechaNacimiento\" value=\"\" maxlength=\"8\"></input></td>");
		sb.append("<td> </td>");
		
		sb.append("</tr>");
		
		//controles ajax
		sb.append("<tr>");
		sb.append("<td>Comunidad aut&oacute;noma de nacimiento:</td>");
		sb.append("<td style=\"width:20px\"><div class=\"divControlComunidadesNacimiento\" id=\"divControlComunidadesNacimiento\"/></td>");
		sb.append("</tr>");
		sb.append("<tr>");
		sb.append("<td >Provincia de nacimiento:</td>");
		sb.append("<td style=\"width:20px\"><div class=\"divControlProvinciasNacimiento\" id=\"divControlProvinciasNacimiento\"/></td>");
		sb.append("</tr>");
		sb.append("<tr>");
		sb.append("<td>Municipio de nacimiento:</td>");
		sb.append("<td style=\"width:20px\"><div class=\"divControlMunicipiosNacimiento\" id=\"divControlMunicipiosNacimiento\"/></td>");
		sb.append("</tr>");
		//fin controles ajax
				
		sb.append("</table>");
		sb.append("</div>");
		
		//llamada a javascript para inicializar los controles
		sb.append("<script>");
		sb.append("loadComunidad();");
		//sb.append("while(comunidadesCargadas == false){setTimeout('',100);}");
		
		sb.append("loadComunidadNac();");
		sb.append("</script>");
		return sb.toString();
	}

	public Element parseXmlDatosEspecificos(HttpServletRequest request, String version) {
		try {
			String esp = readParameter("esp", request);
			String provinciaResidencia = readParameter("vProvincia", request);
			String municipioResidencia = readParameter("vMunicipio", request);
			String fechaNacimiento = readParameter("fechaNacimiento", request);
			String provinciaNacimiento = readParameter("vProvinciaNacimiento", request);
			String municipioNacimiento = readParameter("vMunicipioNacimiento", request);
			String solNombre = readParameter("solNombre", request);
			String solApellido1 = readParameter("solApellido1", request);
			String solApellido2 = readParameter("solApellido2", request);
			String solIdFuncionario = readParameter("solIdFuncionario", request);
			String solNumFuncionario = readParameter("solNumFuncionario", request);
			String solTelefono = readParameter("solTelefono", request);
			String codigoOrganizacion = readParameter("codigoOrganizacion", request);
			String nombreOrganizacion = readParameter("nombreOrganizacion", request);
			
			StringBuilder sb = new StringBuilder();
			sb.append("<?xml version=\"1.0\" encoding=\"iso-8859-1\"?><DatosEspecificos xmlns=\"" + getXmlnsDatosEspecificos(version) + "\">");
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
			sb.append("</SolicitanteDatos>");;
			sb.append("<Solicitud>");
			sb.append("<Espanol>" + esp + "</Espanol>");
			if(provinciaResidencia != null || municipioResidencia != null) {
				sb.append("<Residencia>");
				sb.append(provinciaResidencia == null ? "" : "<Provincia>" + provinciaResidencia + "</Provincia>");
				sb.append(municipioResidencia == null ? "" : "<Municipio>" + municipioResidencia + "</Municipio>");
				sb.append("</Residencia>");
			}
			if(fechaNacimiento != null || provinciaNacimiento != null || municipioNacimiento != null) {
				sb.append("<Nacimiento>");
				sb.append(fechaNacimiento == null ? "" : "<Fecha>" + fechaNacimiento + "</Fecha>");
				sb.append(provinciaNacimiento == null ? "" : "<Provincia>" + provinciaNacimiento + "</Provincia>");
				sb.append(municipioNacimiento == null ? "" : "<Municipio>" + municipioNacimiento + "</Municipio>");
				sb.append("</Nacimiento>");
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

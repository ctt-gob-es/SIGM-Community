package es.scsp.client.test.datosespecificos;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DatosEspecificosInemImportesPeriodos extends DatosEspecificosAdapterBase {

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
		sb.append("<td>Inicio</td>");
		sb.append("<td><input style=\"width:80px\" required=\"1\" datoespecifico=\"1\" type=\"text\" name=\"fInicio\" id=\"fInicio\" onclick=\"return showCalendar('fInicio',  '%m/%Y');\"  maxlength=\"7\"  size=\"7\"></input><span>&nbsp;*</span></td>");
		sb.append("</tr>");
		sb.append("<tr>");
		sb.append("<td>Fin</td>");
		sb.append("<td><input type=\"text\" style=\"width:80px\"  required=\"1\" datoespecifico=\"1\" name=\"fFin\" id=\"fFin\" onclick=\"return showCalendar('fFin',  '%m/%Y');\" maxlength=\"7\" size=\"7\"></input><span>&nbsp;*</span></td>");
		sb.append("</tr>");
		sb.append("</table>");
		sb.append("<p>");
		sb.append("El formato de las fechas ha de ser MM/AAAA.");
		sb.append("</p>");
		sb.append("</div>");
		return sb.toString();
	}

	public Element parseXmlDatosEspecificos(HttpServletRequest request, String version) {
		try {
			String inicio = readParameter("fInicio", request);
			String fin = readParameter("fFin", request);
			StringBuilder sb = new StringBuilder();
			sb.append("<?xml version=\"1.0\" encoding=\"iso-8859-1\"?>" +
					"<DatosEspecificos xmlns=\"" + getXmlnsDatosEspecificos(version) + "\">");
			sb.append("<DatosImportePeriodo>");
			sb.append("<FXINICIO>" + inicio + "</FXINICIO> ");
			sb.append("<FXFINAL>" + fin + "</FXFINAL> ");
			sb.append("</DatosImportePeriodo>");
			sb.append("</DatosEspecificos>");
			InputStream in = new ByteArrayInputStream(sb.toString().getBytes());
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
			return doc.getDocumentElement();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}

package es.scsp.client.test.datosespecificos;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DatosEspecificosTitularidadCatastral extends DatosEspecificosAdapterBase {

	public String createHtmlController(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		sb.append("<div id=\"datosEspecificosTitCatastral\">");
		sb.append("<table>");

		//controles ajax
		sb.append("<tr>");
		sb.append("<td  >Comunidad aut&oacute;noma:</td>");
		sb.append("<td align=\"left\"  style=\"width:20px\"><div class=\"divControlComunidades\" id=\"divControlComunidades\"/><span>&nbsp;*</span></td>");
		sb.append("</tr>");
		sb.append("<tr>");
		sb.append("<td >Provincia:</td>");
		sb.append("<td align=\"left\" style=\"width:20px\"><div class=\"divControlProvincias\" id=\"divControlProvincias\"/><span>&nbsp;*</span></td>");
		sb.append("</tr>");
		sb.append("<tr>");
		sb.append("<td>Municipio:</td>");
		sb.append("<td align=\"left\" style=\"width:20px\"><div class=\"divControlMunicipios\" id=\"divControlMunicipios\"/><span>&nbsp;*</span></td>");
		sb.append("</tr>");
		//fin controles ajax

		sb.append("<tr>");
		sb.append("<td class=\"tdlabel\">Tipo de bien inmueble:</td>");
		sb.append("<td  class=\"tdvalores\"><select name=\"cn\">");
		sb.append("<Option value=\"\"></Option>");
		sb.append("<Option value=\"UR\">Urbano</Option>");
		sb.append("<Option value=\"RU\">Rural</Option>");
		sb.append("<Option value=\"BI\">Bienes inmuebles especiales</Option>");
		//sb.append("</select><span>&nbsp;*</span></td>");
		sb.append("</select></td>");
		sb.append("</tr>");

		sb.append("</table>");
		sb.append("</div>");

		//llamada a javascript para inicializar los controles
		sb.append("<script>");
		sb.append("loadComunidadCatastro();");
		sb.append("</script>");
		return sb.toString();
	}

	public Element parseXmlDatosEspecificos(HttpServletRequest request, String version) {
		try {
			String ccaa = readParameter("vComunidad", request);
			String cp = readParameter("vProvincia", request);
			String cm = readParameter("vMunicipio", request);
			String cn = readParameter("cn", request);
			ccaa = ccaa == null ? "" : ccaa;
			cp = cp == null ? "" : cp;
			cm = cm == null ? "" : cm;
			cn = cn == null ? "" : cn;
			if ("".equals(ccaa) && "".equals(cp) && "".equals(cm) && "".equals(cn)) {
				return null;
			}
			StringBuilder sb = new StringBuilder();
			sb.append("<?xml version=\"1.0\" encoding=\"iso-8859-1\"?><DatosEspecificos xmlns=\"" + getXmlnsDatosEspecificos(version) + "\">");
			sb.append("<DatosEntrada>");
			if (!"".equals(ccaa) || !"".equals(cp) || !"".equals(cm)) {
				sb.append("<ambito>");
				sb.append("".equals(ccaa) ? "" : "<ccaa>" + ccaa + "</ccaa>");
				sb.append("".equals(cp) ? "" : "<cp>" + cp + "</cp>");
				sb.append("".equals(cm) ? "" : "<cm>" + cm + "</cm>");
				sb.append("</ambito>");
			}
			sb.append("".equals(cn) ? "" : "<cn>" + cn + "</cn>");
			sb.append("</DatosEntrada>");
			sb.append("</DatosEspecificos>");
			InputStream in = new ByteArrayInputStream(sb.toString().getBytes());
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
			return doc.getDocumentElement();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

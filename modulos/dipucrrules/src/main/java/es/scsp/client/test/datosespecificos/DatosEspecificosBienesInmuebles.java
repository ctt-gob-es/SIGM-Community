package es.scsp.client.test.datosespecificos;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DatosEspecificosBienesInmuebles extends DatosEspecificosAdapterBase {

	public String createHtmlController(HttpServletRequest request) {
		StringBuffer sb = new StringBuffer();
		sb.append("<div>");

		sb.append("<table>");
		sb.append("<tr><td colspan=\"2\"><h4>Referencia catastral</h4></td>");
		sb.append("<td style=\"width:20px\" ><input class=\"checkbox\" type=\"radio\" name=\"tipoCosulta\" checked onclick=\"habilitaConsultaBienesInmueble('catastral');\"/></td>");
		sb.append("<tr>");
		sb.append("<td class=\"label\">Parcela catastral 1:</td>");
		sb.append("<td><input style=\"width:70px\"  datoespecifico=\"1\"   required=\"1\" name=\"pcatastral1\"  id=\"pcatastral1\"  maxlength=\"7\" /> <span>&nbsp;*</span></td>");
		sb.append("</tr>");

		sb.append("<tr>");
		sb.append("<td class=\"label\">Parcela catastral 2:</td>");
		sb.append("<td><input style=\"width:70px\"  datoespecifico=\"1\"  required=\"1\"  name=\"pcatastral2\" id=\"pcatastral2\" maxlength=\"7\" /><span>&nbsp;*</span></td>");
		sb.append("</tr>");

		sb.append("<tr>");
		sb.append("<td class=\"label\">Cargo de la referencia catastral:</td>");
		sb.append("<td><input style=\"width:40px\" name=\"car\" id=\"car\" maxlength=\"4\" /></td>");
		sb.append("</tr>");

		sb.append("<tr>");
		sb.append("<td class=\"label\">D&iacute;gito de control 1:</td>");
		sb.append("<td><input style=\"width:20px\" name=\"cc1\"  id=\"cc1\" maxlength=\"1\" /></td>");
		sb.append("</tr>");

		sb.append("<tr>");
		sb.append("<td class=\"label\">D&iacute;gito de control 2:</td>");
		sb.append("<td><input style=\"width:20px\" name=\"cc2\" id=\"cc2\" maxlength=\"1\" /></td>");
		sb.append("</tr>");

		sb.append("<tr><td colspan=\"2\"><h4>Referencia rústica</h4></td>");
		sb.append("<td style=\"width:20px\" ><input class=\"checkbox\" type=\"radio\" name=\"tipoCosulta\" checked onclick=\"habilitaConsultaBienesInmueble('rustica');\"/></td>");
		sb.append("<tr>");
		sb.append("<td class=\"label\">Comunidad aut&oacute;noma:</td>");
		sb.append("<td><div required=\"1\" class=\"divControlComunidades\" id=\"divControlComunidades\"/></td><td><span>&nbsp;*</span></td>");
		sb.append("</tr>");
		sb.append("<tr>");
		sb.append("<td class=\"label\">Provincia:</td>");
		sb.append("<td><div required=\"1\" datoespecifico=\"1\" class=\"divControlProvincias\" id=\"divControlProvincias\"/> </td><td><span>&nbsp;*</span></td> ");
		sb.append("</tr>");
		sb.append("<tr>");
		sb.append("<td class=\"label\">Municipio:</td>");
		sb.append("<td><div required=\"1\" datoespecifico=\"1\"  class=\"divControlMunicipios\" id=\"divControlMunicipios\"/> </td><td><span>&nbsp;*</span></td>");
		sb.append("</tr>");
		
		sb.append("<tr>");
		sb.append("<td class=\"label\">C&oacute;digo del pol&iacute;gono:</td>");
		sb.append("<td><input required=\"1\"  style=\"width:30px\" id=\"codPoligono\" name=\"codPoligono\" maxlength=\"3\" /> </td><td><span>&nbsp;*</span></td>");
		sb.append("</tr>");

		sb.append("<tr>");
		sb.append("<td class=\"label\">C&oacute;digo de parcela:</td>");
		sb.append("<td><input   style=\"width:50px\" id=\"codParcela\" name=\"codParcela\" maxlength=\"5\" /></td>");
		sb.append("</tr>");

		sb.append("</table>");
		sb.append("</div>");
		sb.append("<script>");
		sb.append("loadComunidadCatastro();");
		sb.append("habilitaConsultaBienesInmueble('rustica');");
		sb.append("</script>");
		return sb.toString();
	}

	public Element parseXmlDatosEspecificos(HttpServletRequest request, String version) {
		try {
			String pcatastral1 = readParameter("pcatastral1", request);
			String pcatastral2 = readParameter("pcatastral2", request);
			String car = readParameter("car", request);
			String cc1 = readParameter("cc1", request);
			String cc2 = readParameter("cc2", request);
			String codProvincia = readParameter("vProvincia", request);
			String codMunicipio = readParameter("vMunicipio", request);
			String codPoligono = readParameter("codPoligono", request);
			String codParcela = readParameter("codParcela", request);

			StringBuilder sb = new StringBuilder();
			sb.append("<?xml version=\"1.0\" encoding=\"iso-8859-1\"?><DatosEspecificos xmlns=\"" + getXmlnsDatosEspecificos(version) + "\">");
			sb.append("<DatosEntrada>");
			if (pcatastral1 != null || pcatastral2 != null) {
				sb.append("<referenciaCatastral>");
				sb.append("<pc1>" + pcatastral1 + "</pc1>");
				sb.append("<pc2>" + pcatastral2 + "</pc2>");
				sb.append(car == null ? "" : "<car>" + car + "</car>");
				sb.append(cc1 == null ? "" : "<cc1>" + cc1 + "</cc1>");
				sb.append(cc2 == null ? "" : "<cc2>" + cc1 + "</cc2>");
				sb.append("</referenciaCatastral>");
			}
			if (codPoligono != null || codParcela != null) {
				sb.append("<referenciaRustica>");
				sb.append("<localizacionINE>");
				sb.append(codProvincia == null ? "" : "<cp>" + codProvincia + "</cp>");
				sb.append(codMunicipio == null ? "" : "<cm>" + codMunicipio + "</cm>");
				sb.append("</localizacionINE>");
				sb.append("<cpp>");
				sb.append(codPoligono == null ? "" : "<cpo>" + codPoligono + "</cpo>");
				sb.append(codParcela == null ? "" : "<cpa>" + codParcela + "</cpa>");
				sb.append("</cpp>");
				sb.append("</referenciaRustica>");
			}
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

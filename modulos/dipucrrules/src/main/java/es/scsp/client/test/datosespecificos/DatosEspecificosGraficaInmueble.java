package es.scsp.client.test.datosespecificos;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DatosEspecificosGraficaInmueble extends DatosEspecificosAdapterBase {

	public String createHtmlController(HttpServletRequest request) {
		StringBuffer sb = new StringBuffer();
		sb.append("<div>");
		sb.append("<script type=\"text/javascript\" src=\"js/scsp.js\"></script>");
		sb.append("<table>");
		
		sb.append("<tr><td colspan=\"2\"><b>Localización seg&uacute;n INE</b></td></tr>");
		
		//controles ajax
		sb.append("<tr>");
		sb.append("<td>Comunidad aut&oacute;noma:</td>");
		sb.append("<td style=\"width:20px\"><div required=\"1\"  id=\"divControlComunidades\"/></td><td><span>&nbsp;*</span></td>");
		sb.append("</tr>");
		sb.append("<tr>");
		sb.append("<td>Provincia:</td>");
		sb.append("<td style=\"width:20px\"><div  required=\"1\" datoespecifico=\"1\"  class=\"divControlProvincias\" id=\"divControlProvincias\"/></td><td><span>&nbsp;*</span></td>");
		sb.append("</tr>");
		sb.append("<tr>");
		sb.append("<td>Municipio:</td>");
		sb.append("<td style=\"width:20px\"><div  required=\"1\"  datoespecifico=\"1\" class=\"divControlMunicipios\" id=\"divControlMunicipios\"/></td><td><span>&nbsp;*</span></td>");
		sb.append("</tr>");
		//fin controles ajax
//		sb.append("</table>");
//		
//		
//		sb.append("<table>");
		sb.append("<tr><td style=\"width:20px\" ><b>Referencia catastral</b></td>");
		sb.append("<td style=\"width:20px\" ><input class=\"checkbox\" type=\"radio\" name=\"tipoCosulta\" checked onclick=\"habilitaConsultaGraficaInmueble('catastral');\"/></td>");
		sb.append("</tr>");
		
		
		sb.append("<tr>");
		sb.append("<td class=\"tdlabel\">Parcela catastral 1:</td>");
		sb.append("<td class=\"tdvalores\" ><input name=\"pcatastral1\" id=\"pcatastral1\" style=\"width:70px\" maxlength=\"7\" /><span>&nbsp;*</span></td>");
		sb.append("</tr>");
		
		sb.append("<tr>");
		sb.append("<td class=\"tdlabel\">Parcela catastral 2:</td>");
		sb.append("<td class=\"tdvalores\" ><input name=\"pcatastral2\" id=\"pcatastral2\"  style=\"width:70px\" maxlength=\"7\"/><span>&nbsp;*</span></td>");
		sb.append("</tr>");
		
		sb.append("<tr>");
		sb.append("<td class=\"tdlabel\">Cargo de la referencia catastral:</td>");
		sb.append("<td class=\"tdvalores\"><input name=\"car\" id=\"car\"  style=\"width:40px\" maxlength=\"4\"/><span>&nbsp;*</span></td>");
		sb.append("</tr>");
		
		sb.append("<tr>");
		sb.append("<td class=\"tdlabel\">D&iacute;gito de control 1:</td>");
		sb.append("<td class=\"tdvalores\"><input name=\"cc1\" id=\"cc1\"  style=\"width:20px\" maxlength=\"1\"/></td>");
		sb.append("</tr>");
		
		sb.append("<tr>");
		sb.append("<td class=\"tdlabel\">D&iacute;gito de control 2:</td>");
		sb.append("<td class=\"tdvalores\"><input name=\"cc2\" id=\"cc2\" style=\"width:20px\" maxlength=\"1\"/></td>");
		sb.append("</tr>");
		
		sb.append("<tr><td style=\"width:20px\"><b>Referencia r&uacute;stica</b></td>");
		sb.append("<td  style=\"width:20px\"><input class=\"checkbox\" type=\"radio\" name=\"tipoCosulta\" checked onclick=\"habilitaConsultaGraficaInmueble('rustica');\"/></td>");
		sb.append("</tr>");
		sb.append("<tr>");
		sb.append("<td class=\"tdlabel\">C&oacute;digo del pol&iacute;gono:</td>");
		sb.append("<td class=\"tdvalores\"><input id=\"codPoligono\" name=\"codPoligono\" style=\"width:30px\"  maxlength=\"3\"/><span>&nbsp;*</span></td>");
		sb.append("</tr>");
		
		sb.append("<tr>");
		sb.append("<td class=\"tdlabel\">C&oacute;digo de parcela:</td>");
		sb.append("<td class=\"tdvalores\"><input id=\"codParcela\" name=\"codParcela\"  style=\"width:50px\" maxlength=\"5\"/><span>&nbsp;*</span></td>");
		sb.append("</tr>");
		
		sb.append("</table>");
		
		sb.append("</div>");
		sb.append("<script>");
		sb.append("loadComunidadCatastro();");
		sb.append("habilitaConsultaGraficaInmueble('rustica');");
		
		
		sb.append("</script>");
		return sb.toString();
	}

	public Element parseXmlDatosEspecificos(HttpServletRequest request, String version) {
		try {		
			StringBuilder sb = new StringBuilder();
			String pcatastral1 = readParameter("pcatastral1", request);
			String pcatastral2 = readParameter("pcatastral2", request);
			String car = readParameter("car", request);
			String cc1 = readParameter("cc1", request);
			String cc2 = readParameter("cc2", request);
			String codProvincia = readParameter("vProvincia", request);
			String codMunicipio = readParameter("vMunicipio", request);
			String codPoligono = readParameter("codPoligono", request);
			String codParcela = readParameter("codParcela", request);

			sb.append("<?xml version=\"1.0\" encoding=\"iso-8859-1\"?><DatosEspecificos xmlns=\"" + getXmlnsDatosEspecificos(version) + "\">");
			sb.append("<DatosEntrada>");
			sb.append("<localizacionINE>");
			sb.append(codProvincia == null ? "" : "<cp>" + codProvincia + "</cp>");
			sb.append(codMunicipio == null ? "" : "<cm>" + codMunicipio + "</cm>");
			sb.append("</localizacionINE>");
			
			if (pcatastral1 != null || pcatastral2 != null) {
				sb.append("<referenciaCatastral>");
				sb.append("<pc1>" + pcatastral1 + "</pc1>");
				sb.append("<pc2>" + pcatastral2 + "</pc2>");
				sb.append(car == null ? "" : "<car>" + car + "</car>");
				sb.append(cc1 == null ? "" : "<cc1>" + cc1 + "</cc1>");
				sb.append(cc2 == null ? "" : "<cc2>" + cc2 + "</cc2>");
				sb.append("</referenciaCatastral>");
			}
			if (codPoligono != null || codParcela != null) {
				sb.append("<referenciaRustica>");
				sb.append(codPoligono == null ? "" : "<cpo>" + codPoligono + "</cpo>");
				sb.append(codParcela == null ? "" : "<cpa>" + codParcela + "</cpa>");
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

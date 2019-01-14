package es.scsp.client.test.datosespecificos;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Element;

public class DatosEspecificosAEAT extends DatosEspecificosAdapterBase {

	public String createHtmlController(HttpServletRequest request) {
		StringBuffer sb = new StringBuffer();
		sb.append("<script>setRequiredNames();");
		
		sb.append("document.getElementById('tituloFormulario').innerHTML=\"Este servicio no requiere introducir un formulario datos espec&iacute;ficos o no se ha configurado para ello.\";");
		sb.append("</script>");
		return sb.toString();
	}

	public Element parseXmlDatosEspecificos(HttpServletRequest request, String version) {
		return null;
	}
}

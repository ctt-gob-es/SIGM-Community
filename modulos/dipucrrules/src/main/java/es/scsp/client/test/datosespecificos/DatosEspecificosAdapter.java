package es.scsp.client.test.datosespecificos;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Element;

public interface DatosEspecificosAdapter {
	
	String createHtmlController(HttpServletRequest request);
	
	Element parseXmlDatosEspecificos(HttpServletRequest request, String version);

}

package es.dipucr.core.datos;

import javax.servlet.http.HttpServletRequest;

import org.apache.axiom.om.OMElement;

public class DatosDGPCI extends DatosEspecificosAdapterBase{

	public String createHtmlController(HttpServletRequest request) {
		
		return "";
	}
	
	public OMElement parseXmlDatosEspecificos(HttpServletRequest request, String version) {
		return null;
	}
}

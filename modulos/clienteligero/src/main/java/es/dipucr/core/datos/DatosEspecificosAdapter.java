package es.dipucr.core.datos;

import javax.servlet.http.HttpServletRequest;

import org.apache.axiom.om.OMElement;
import org.w3c.dom.Element;

public abstract interface DatosEspecificosAdapter {
	  public abstract String createHtmlController(HttpServletRequest paramHttpServletRequest);

	  public abstract OMElement parseXmlDatosEspecificos(HttpServletRequest paramHttpServletRequest, String version);
	}

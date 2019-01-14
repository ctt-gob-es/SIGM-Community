package es.dipucr.core.datos;

import ieci.tdw.ispac.api.errors.ISPACRuleException;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public abstract class DatosEspecificosAdapterBase implements DatosEspecificosAdapter {
	
	protected String readParameter(String name, HttpServletRequest request)
	  {
	    if (request.getParameter(name) != null) {
	      return ("".equals(request.getParameter(name))) ? null : request.getParameter(name);
	    }
	    Map map = (Map)request.getAttribute("parameterRequestMap");
	    if (map == null) {
	      return null;
	    }
	    return ("".equals(map.get(name))) ? null : (String)map.get(name);
	  }	
	
	protected String getXmlnsDatosEspecificos(HttpServletRequest request) throws ISPACRuleException {
	    String version = readParameter("version", request);
	    if (version.contains("V2")) {
	      return "http://www.map.es/scsp/esquemas/datosespecificos";
	    }
	    return "http://intermediacion.redsara.es/scsp/esquemas/datosespecificos";
	  }

}

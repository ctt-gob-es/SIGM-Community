package es.scsp.client.test.datosespecificos;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

public abstract class DatosEspecificosAdapterBase implements DatosEspecificosAdapter {
		
	protected String getXmlnsDatosEspecificos(String version) throws Exception {

		if(version.contains("V2")) {
			return "http://www.map.es/scsp/esquemas/datosespecificos";
		} else {
			return "http://intermediacion.redsara.es/scsp/esquemas/datosespecificos";
		}
	}
	
	protected String readParameter(String name, HttpServletRequest request) {
		if(request.getParameter(name) != null) {
			return "".equals(request.getParameter(name)) ? null : request.getParameter(name);
		}
		Map<String,String> map = (Map<String, String>) request.getAttribute("parameterRequestMap");
		if(map == null) {
			return null;
		}
		return "".equals(map.get(name)) ? null : map.get(name);
	}

}

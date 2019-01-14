package es.dipucr.core.datos;

import javax.servlet.http.HttpServletRequest;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;

import ieci.tdw.ispac.api.errors.ISPACRuleException;

public class DatosDemandanteFecha extends DatosEspecificosAdapterBase{

	public String createHtmlController(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();

		sb.append("<div>");
		sb.append("<label> Fecha de consulta (yyyMMdd)(*):</label>");
		sb.append("<input type='text' name='fechaHechoRegistral' id='fechaHechoRegistral' size='12' />");
		sb.append("</div>");
		return sb.toString();
	}
	
	public OMElement parseXmlDatosEspecificos(HttpServletRequest request, String version) {
		try {
			String fechaHechoRegistral = readParameter("fechaHechoRegistral", request);
			if ((fechaHechoRegistral == null) || ("".equals(fechaHechoRegistral))) {
				String[] msg = { "El campo Fecha hecho registral es obligatorio con el formato  (yyyMMdd)." };
				throw new ISPACRuleException("Se produjo una excepción al parsear el mensaje" +msg);
			}
			
			String ruta = "";
			if(version.contains("V2")) {
				ruta = "http://www.map.es/scsp/esquemas/datosespecificos";
			} else {
				ruta = "http://intermediacion.redsara.es/scsp/esquemas/datosespecificos";;
			}
			
			OMFactory fac = OMAbstractFactory.getOMFactory();
			OMNamespace omNsDe = fac.createOMNamespace(ruta, "");
			OMElement datosEspecificos = fac.createOMElement("DatosEspecificos", omNsDe);
			
			
			OMElement consulta = fac.createOMElement("Consulta", omNsDe);
			OMElement fechaConsulta = fac.createOMElement("FechaConsulta", omNsDe);
			fechaConsulta.setText(fechaHechoRegistral);			
			consulta.addChild(fechaConsulta);
			datosEspecificos.addChild(consulta);
			
			return datosEspecificos;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

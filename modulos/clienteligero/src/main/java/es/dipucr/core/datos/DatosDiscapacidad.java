package es.dipucr.core.datos;

import javax.servlet.http.HttpServletRequest;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;

public class DatosDiscapacidad extends DatosEspecificosAdapterBase{

	public String createHtmlController(HttpServletRequest request) {
		
		return "";
	}
	
	public OMElement parseXmlDatosEspecificos(HttpServletRequest request, String version) {
		try {
			
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
			
			OMElement codigoComunidadAutonoma = fac.createOMElement("CodigoComunidadAutonoma", omNsDe);
			codigoComunidadAutonoma.setText("08");
			consulta.addChild(codigoComunidadAutonoma);
			
			OMElement consentimientoTiposDiscapacidad = fac.createOMElement("ConsentimientoTiposDiscapacidad", omNsDe);
			consentimientoTiposDiscapacidad.setText("S");
			consulta.addChild(consentimientoTiposDiscapacidad);

			datosEspecificos.addChild(consulta);
			
			return datosEspecificos;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

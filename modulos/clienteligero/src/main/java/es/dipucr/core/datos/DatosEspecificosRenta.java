package es.dipucr.core.datos;

import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;

import ieci.tdw.ispac.api.errors.ISPACRuleException;

public class DatosEspecificosRenta extends DatosEspecificosAdapterBase{

	public String createHtmlController(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();

		Calendar fecha = new GregorianCalendar();
		int year = fecha.get(Calendar.YEAR);
		String headerSelect = "<SELECT id='ejercicio' datoespecifico='1' class='classSelect' NAME='ejercicio' label='Periodo' SIZE='1'>";
		String bodySelect = "";
		String finishSelect = "</SELECT>";
		for (int i = 0; i <=6; i++) {			
			bodySelect = bodySelect + "<OPTION VALUE=" + year + ">" + year + "</OPTION> ";
			year = year - 1;
		}
		String select = headerSelect + bodySelect + finishSelect;

		sb.append("<div>");
		sb.append("</span>Periodo de consulta:&nbsp;&nbsp;&nbsp;&nbsp;</span>");
		sb.append(select);
		sb.append("</div>");
		return sb.toString();
	}
	
	public OMElement parseXmlDatosEspecificos(HttpServletRequest request, String version) {
		try {		
			String ejercicio = readParameter("ejercicio", request);
			if ((ejercicio == null) || ("".equals(ejercicio))) {
				String[] msg = { "El campo ejercicio es obligatorio." };
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
			
			
			OMElement consulta = fac.createOMElement("Ejercicio", omNsDe);
			consulta.setText(ejercicio);
			consulta.addChild(consulta);
			datosEspecificos.addChild(consulta);
			
			return datosEspecificos;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}
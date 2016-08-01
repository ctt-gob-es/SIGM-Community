package es.scsp.client.test.datosespecificos;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import es.dipucr.verifdatos.services.ClienteLigeroProxy;
import es.scsp.common.exceptions.ScspException;
import es.scsp.common.exceptions.ScspExceptionConstants;

public class DatosEspecificosRenta extends DatosEspecificosAdapterBase {
	
	private static final Logger logger = Logger.getLogger(DatosEspecificosRenta.class);

	public String createHtmlController(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		
		ClienteLigeroProxy clienteLigero = new ClienteLigeroProxy();
		int[] rentaAnio = null;
		try {
			rentaAnio = clienteLigero.selectAllYear();
		} catch (RemoteException e) {
			logger.error(e.getMessage(), e);
		}
		
		String headerSelect ="<SELECT id='ejercicio' datoespecifico='1' class='classSelect' NAME='ejercicio' label='Periodo' SIZE='1'>";
		String bodySelect ="";
		String finishSelect="</SELECT>";
		if(rentaAnio !=null && rentaAnio.length > 0){
			for(int i = 0; i < rentaAnio.length; i++){
				bodySelect+="<OPTION VALUE="+rentaAnio[i]+">"+rentaAnio[i]+"</OPTION> ";
			}
		}
		String select = headerSelect+ bodySelect+finishSelect;
		
		sb.append("<div>");
		sb.append("</span>Periodo de consulta:&nbsp;&nbsp;&nbsp;&nbsp;</span>");
		sb.append(select);
		sb.append("</div>");
		sb.append("<script>setRequiredNames();</script>");
		return sb.toString();
	}

	public Element parseXmlDatosEspecificos(HttpServletRequest request, String version) {
		try {
			String ejercicio = readParameter("ejercicio", request);
			if(ejercicio == null || "".equals(ejercicio)) {
				String [] msg = { "El campo ejercicio es obligatorio."};
				throw   ScspException.getScspException( ScspExceptionConstants.ErrorMissingCampoObligatorio,  msg);
			}
			StringBuilder sb = new StringBuilder();
			sb.append("<?xml version=\"1.0\" encoding=\"iso-8859-1\"?><DatosEspecificos xmlns=\"" + getXmlnsDatosEspecificos(version) + "\">");
			sb.append("<Ejercicio>" + ejercicio + "</Ejercicio>");
			sb.append("</DatosEspecificos>");
			InputStream in = new ByteArrayInputStream(sb.toString().getBytes());
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
			return doc.getDocumentElement();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

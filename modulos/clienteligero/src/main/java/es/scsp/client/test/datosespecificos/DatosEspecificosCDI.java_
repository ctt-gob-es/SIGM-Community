package es.scsp.client.test.datosespecificos;

import java.io.ByteArrayInputStream;
import java.io.InputStream;

import javax.servlet.http.HttpServletRequest;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public class DatosEspecificosCDI extends DatosEspecificosAdapterBase {

	public String createHtmlController(HttpServletRequest request) {
		StringBuffer sb = new StringBuffer();
		sb.append("<span>N&uacute;mero de serie del Soporte(IDESP): </span>");
		sb.append("<input type=\"text\" style=\"width:100px\" required=\"1\" datoespecifico=\"1\" name=\"idesp\" id=\"idesp\" maxLength=\"9\" value=\"\"></input><span>&nbsp;*</span>");
		return sb.toString();
	}

	public Element parseXmlDatosEspecificos(HttpServletRequest request, String version) {
		try {
			String idesp = readParameter("idesp", request);
			String solNombre = readParameter("solNombre", request);
			String solApellido1 = readParameter("solApellido1", request);
			String solApellido2 = readParameter("solApellido2", request);
			String solIdFuncionario = readParameter("solIdFuncionario", request);
			String solNumFuncionario = readParameter("solNumFuncionario", request);
			String solTelefono = readParameter("solTelefono", request);
			String codigoOrganizacion = readParameter("codigoOrganizacion", request);
			String nombreOrganizacion = readParameter("nombreOrganizacion", request);
			
			StringBuilder sb = new StringBuilder();
			sb.append("<?xml version=\"1.0\" encoding=\"iso-8859-1\"?><DatosEspecificos xmlns=\"" + getXmlnsDatosEspecificos(version) + "\">");
			sb.append("<SolicitanteDatos>");
			sb.append("<Tipo>app</Tipo>");
			sb.append(solNombre == null ? "" : "<Nombre>" + solNombre + "</Nombre>");
			sb.append(solApellido1 == null ? "" : "<Apellido1>" + solApellido1 + "</Apellido1>");
			sb.append(solApellido2 == null ? "" : "<Apellido2>" + solApellido1 + "</Apellido2>");
			sb.append(solIdFuncionario == null ? "" : "<IdFuncionario>" + solIdFuncionario + "</IdFuncionario>");
			sb.append(solNumFuncionario == null ? "" : "<NumFuncionario>" + solNumFuncionario + "</NumFuncionario>");
			sb.append(solTelefono == null ? "" : "<Telefono>" + solIdFuncionario + "</Telefono>");
			if(codigoOrganizacion != null || nombreOrganizacion != null) {
				sb.append("<Organizacion>");
				sb.append(codigoOrganizacion == null ? "" : "<CodOrganizacion>" + codigoOrganizacion + "</CodOrganizacion>");
				sb.append(nombreOrganizacion == null ? "" : "<NombreOrganizacion>" + nombreOrganizacion + "</NombreOrganizacion>");
				sb.append("</Organizacion>");
			}
			sb.append("</SolicitanteDatos>");
			sb.append("<Solicitud>");
			sb.append(idesp == null ? "<NumSoporte/>" : "<NumSoporte>" + idesp + "</NumSoporte>");
			sb.append("</Solicitud>");
			sb.append("</DatosEspecificos>");
			InputStream in = new ByteArrayInputStream(sb.toString().getBytes());
			Document doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
			return doc.getDocumentElement();
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

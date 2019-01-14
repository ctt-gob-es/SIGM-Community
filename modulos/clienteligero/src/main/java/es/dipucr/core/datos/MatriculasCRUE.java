package es.dipucr.core.datos;

import java.rmi.RemoteException;
import java.util.Calendar;
import java.util.GregorianCalendar;

import javax.servlet.http.HttpServletRequest;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.log4j.Logger;

import es.dipucr.verifdatos.services.ClienteLigeroProxy;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

public class MatriculasCRUE extends DatosEspecificosAdapterBase{
	
	private Logger logger = Logger.getLogger(MatriculasCRUE.class);

	public String createHtmlController(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();

		sb.append("<div>");
		
		Calendar fecha = new GregorianCalendar();
		int year = fecha.get(Calendar.YEAR);
		//year = year - 1;
		
		String headerSelect = "<SELECT id='curso' name='curso' datoespecifico='1' class='classSelect' label='Periodo' SIZE='1'>";
		String bodySelect = "";
		String finishSelect = "</SELECT>";
		for (int i = 0; i <=16; i++) {
			String sYear = year+"";
			String ayho = sYear.substring(2, 4);
			int finAnyo = Integer.parseInt(ayho);
			bodySelect = bodySelect + "<OPTION VALUE=" + year+"-"+(finAnyo+1)+ ">" + year+"-"+(finAnyo+1)+ "</OPTION> ";
			year = year - 1;
		}
		
		String select = headerSelect + bodySelect + finishSelect;
		
		sb.append("</span>Curso(*):</span>");
		sb.append(select);
		
		sb.append("<br/>");
		
		ClienteLigeroProxy clienteLigero = new ClienteLigeroProxy();
		String[] universidades = null;
		try {
			universidades = clienteLigero.getUniversidadesCRUE();
		} catch (RemoteException e) {
			logger.error("Error. "+e.getMessage(), e);
			throw new RuntimeException("Error. "+e.getMessage(), e);
		}

		sb.append("</span>Seleccione centro:</span>");			
		
		String headerSelect2 = "<SELECT id='centro' name='centro' datoespecifico='1' class='classSelect' label='Periodo' SIZE='1'>";
		String bodySelect2 = "";
		String finishSelect2 = "</SELECT>";
		bodySelect2 = bodySelect2 + "<OPTION VALUE=''></OPTION> ";
		for(int i=0; i<universidades.length;i++){
			String datosUni = universidades[i];
			String [] vecUni = datosUni.split("#");
			bodySelect2 = bodySelect2 + "<OPTION VALUE=" + vecUni[0] + ">" + vecUni[1]+ "</OPTION> ";
		}
		
		String select2 = headerSelect2 + bodySelect2 + finishSelect2;
		sb.append(select2);
				
		
		sb.append("</div>");
		return sb.toString();
	}
	
	public OMElement parseXmlDatosEspecificos(HttpServletRequest request, String version) {
		try {
			String curso = readParameter("curso", request);
			//String centro = readParameter("centro", request);
			if ((curso == null) || ("".equals(curso))) {
				String[] msg = { "El campo Fecha hecho registral es obligatorio." };
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
			
			String centro = readParameter("centro", request);	
			if(StringUtils.isNotEmpty(centro)){	
				OMElement listaIdentificadoresUniversidad = fac.createOMElement("ListaIdentificadoresUniversidad", omNsDe);			
				OMElement identificadoresUniversidad = fac.createOMElement("IdentificadorUniversidad", omNsDe);		
				identificadoresUniversidad.setText(centro);
				listaIdentificadoresUniversidad.addChild(identificadoresUniversidad);			
				consulta.addChild(listaIdentificadoresUniversidad);
			}			
			
			
			OMElement cursoAcademico = fac.createOMElement("CursoAcademico", omNsDe);
			cursoAcademico.setText(curso);
			consulta.addChild(cursoAcademico);
			
			OMElement incluyeCursosAcademicosPrevios = fac.createOMElement("IncluyeCursosAcademicosPrevios", omNsDe);
			incluyeCursosAcademicosPrevios.setText("false");
			consulta.addChild(incluyeCursosAcademicosPrevios);
			
			
			datosEspecificos.addChild(consulta);
			
			
			return datosEspecificos;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

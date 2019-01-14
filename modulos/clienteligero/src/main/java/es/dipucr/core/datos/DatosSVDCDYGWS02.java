package es.dipucr.core.datos;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;

import es.dipucr.verifdatos.services.ClienteLigeroProxy;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

public class DatosSVDCDYGWS02 extends DatosEspecificosAdapterBase{

	public String createHtmlController(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();

		
		ClienteLigeroProxy clienteLigero = new ClienteLigeroProxy();
		//Ciudad Real
		String[] municipio= null;
		try {
			municipio = clienteLigero.getMunicipio("13");
		} catch (RemoteException e) {
			throw new RuntimeException("Error al obtener los municipios. "+e.getMessage(),e);
		}
		
		String headerSelect = "<SELECT id='munic' name='munic' datoespecifico='1' class='classSelect' label='Periodo' SIZE='1' style='width:330'>";
		String bodySelect = "";
		String finishSelect = "</SELECT>";
		for (int i = 0; i <=municipio.length-1; i++) {
			String datosMunicipio = municipio[i];
			String [] vecmunic = datosMunicipio.split("#");
			bodySelect = bodySelect + "<OPTION VALUE=" + vecmunic[0]+ ">" + vecmunic[1]+ "</OPTION> ";
		}
		
		String select = headerSelect + bodySelect + finishSelect;
		sb.append("<table class='tableDatos'>");
		sb.append("<tr>");
		sb.append("<td class='formsTitle'>Municipio(*):</td>");
		sb.append("<td>"+select+"</br>");		
		sb.append("</tr>");
		
		sb.append("<tr></br>");
		
		sb.append("<tr>");
		sb.append("<td colspan='2' style='font-weight: bold'>");
		sb.append("REFERENCIA CATASTRAL.");
		sb.append("</br>");
		sb.append("Si conoce la referencia catastral del bien inmueble a consultar, indíquela en la pestaña 'Referencia catastral' junto con el tipo de bien inmueble.");
		sb.append("</td>");
		sb.append("</tr>");

		/*sb.append("<tr>");
		sb.append("<td class='formsTitle'>Tipo de bien inmueble(*):</td>");
		sb.append("<td>");
		sb.append("<select id='tipInm' name='tipInm' datoespecifico='1' class='classSelect' label='Periodo' SIZE='1' style='width:330'>");
		sb.append("<option value='-1' selected='selected'></option>");
		sb.append("<option value='URBANA'>Referencia catastral</option>");
		sb.append("<option value='ESPECIAL'>Especial</option>");
		sb.append("<option value='RUSTICA'>Localización del bien inmueble rústico</option>");
		sb.append("</select>");
		sb.append("</td>");
		sb.append("</tr>");*/

		sb.append("<tr>");		
		sb.append("<td colspan='2'>");
		sb.append("Corresponde con los siete primeros dígitos de la referencia catastral e identifican la finca o parcela del bien inmueble consultado");
		sb.append("</td>");
		sb.append("</tr>");
		sb.append("<tr>");
		sb.append("<td class='formsTitle'> Parcela Catastro 1(*):</td>");
		sb.append("<td><input type='text' name='parc1' maxlength='7' id='parc1' size='10' /></td>");
		sb.append("</tr>");
		sb.append("<tr>");
		sb.append("<td colspan='2'>");
		sb.append("Siete dígitos siguientes de la referencia catastral e indican la hoja del plano donde se ubica el bien inmueble");
		sb.append("</td>");
		sb.append("</tr>");
		sb.append("<tr>");
		sb.append("<td class='formsTitle'> Parcela Catastro 2(*):</td>");
		sb.append("<td><input type='text' name='parc2' maxlength='7' id='parc2' size='10' /></td>");
		sb.append("</tr>");
		sb.append("<tr>");
		sb.append("<td colspan='2'>");
		sb.append("Cuatro dígitos siguientes de la referencia catastral que identifican el inmueble dentro de la finca");
		sb.append("</td>");
		sb.append("</tr>");
		sb.append("<tr>");
		sb.append("<td class='formsTitle'> Cargo de la Referencia(*):</td>");
		sb.append("<td><input type='text' maxlength='4' name='car' id='car' size='10'/></td>");
		sb.append("</tr>");
		
		sb.append("<tr>");
		sb.append("<td colspan='2' style='font-weight: bold'>");
		sb.append("BIEN INMUEBLE RÚSTICO.");
		sb.append("</br>");
		sb.append("Si se trata de un bien inmueble rústico y no conoce su referencia catastral, seleccione la pestaña 'Localización bien inmueble rústico' y rellene los datos de Comunidad autónoma, provincia, municipio y parcela donde se encuentra el bien inmueble");
		sb.append("</td>");
		sb.append("</tr>");
		
		sb.append("<tr>");
		sb.append("<td class='formsTitle'> Código de polígono(*):</td>");
		sb.append("<td><input type='text' name='poligono' id='poligono' size='60' /></td>");
		sb.append("</tr>");
		
		sb.append("<tr>");
		sb.append("<td class='formsTitle'> Código de parcela(*):</td>");
		sb.append("<td><input type='text' name='parcela' id='parcela' size='60' /></td>");
		sb.append("</tr>");
		
		sb.append("</table>");
		return sb.toString();
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
			OMElement datosEntrada = fac.createOMElement("DatosEntrada", omNsDe);
			
			
			String munic = readParameter("munic", request);
			if(StringUtils.isNotEmpty(munic)){
				OMElement localizacionINE = fac.createOMElement("localizacionINE", omNsDe);
				OMElement cp = fac.createOMElement("cp", omNsDe);
				//cp.setText("13");
				cp.setText("11");
				localizacionINE.addChild(cp);
				OMElement cm = fac.createOMElement("cm", omNsDe);
				//cm.setText(munic);
				cm.setText("12");
				localizacionINE.addChild(cm);
				datosEntrada.addChild(localizacionINE);
			}
			
			String car = readParameter("car", request);
			String parc2 = readParameter("parc2", request);
			String parc1 = readParameter("parc1", request);
			if(StringUtils.isNotEmpty(parc1) && StringUtils.isNotEmpty(parc2) && StringUtils.isNotEmpty(car)){
				OMElement referenciaCatastral = fac.createOMElement("referenciaCatastral", omNsDe);
				OMElement pc1 = fac.createOMElement("pc1", omNsDe);
				pc1.setText(parc1);
				referenciaCatastral.addChild(pc1);
				OMElement pc2 = fac.createOMElement("pc2", omNsDe);
				pc2.setText(parc2);
				referenciaCatastral.addChild(pc2);
				OMElement elemcar = fac.createOMElement("car", omNsDe);
				elemcar.setText(car);
				referenciaCatastral.addChild(elemcar);
				datosEntrada.addChild(referenciaCatastral);
			}
			
			String poligono = readParameter("poligono", request);
			String parcela = readParameter("parcela", request);
			if(StringUtils.isNotEmpty(poligono) && StringUtils.isNotEmpty(parcela)){
				OMElement referenciaRustica = fac.createOMElement("referenciaRustica", omNsDe);
				OMElement elepoligono = fac.createOMElement("cpo", omNsDe);
				elepoligono.setText(poligono);
				referenciaRustica.addChild(elepoligono);
				OMElement eleparcela = fac.createOMElement("cpa", omNsDe);
				eleparcela.setText(parcela);
				referenciaRustica.addChild(eleparcela);
				datosEntrada.addChild(referenciaRustica);
			}
			
			datosEspecificos.addChild(datosEntrada);
			
			
			return datosEspecificos;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

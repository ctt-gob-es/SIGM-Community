package es.dipucr.core.datos;

import java.rmi.RemoteException;

import javax.servlet.http.HttpServletRequest;

import org.apache.axiom.om.OMAbstractFactory;
import org.apache.axiom.om.OMElement;
import org.apache.axiom.om.OMFactory;
import org.apache.axiom.om.OMNamespace;
import org.apache.log4j.Logger;

import es.dipucr.verifdatos.services.ClienteLigeroProxy;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;

public class DatosRegistrosCiviles extends DatosEspecificosAdapterBase{
	
	 private Logger logger = Logger.getLogger(DatosRegistrosCiviles.class);

	public String createHtmlController(HttpServletRequest request) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("<script>");
		sb.append("function obtenerProvincia(comunidad, hecho){ ");
			sb.append("var provincia = document.getElementById('provincia');");
			sb.append("if(hecho=='REGISTRO'){");
				sb.append("for (var i = 0; i < provincia.children.length; ++i) {");
				sb.append("provincia.remove(i)");
				sb.append("}");
				sb.append("provincia.options.length = 0;");
			sb.append("}");
			sb.append("var provinciaNacimiento = document.getElementById('provinciaNacimiento');");
			sb.append("if(hecho=='NACIMIENTO'){");
				sb.append("for (var i = 0; i < provinciaNacimiento.children.length; ++i) {");
				sb.append("provinciaNacimiento.remove(i)");
				sb.append("}");
				sb.append("provinciaNacimiento.options.length = 0;");
			sb.append("}");
			
			sb.append("var municipio = document.getElementById('municipio');");
			sb.append("if(hecho=='REGISTRO'){");
				sb.append("for (var i = 0; i < municipio.children.length; ++i) {");
				sb.append("municipio.remove(i)");
				sb.append("}");
				sb.append("municipio.options.length = 0;");
			sb.append("}");
			sb.append("var municipioNacimiento = document.getElementById('municipioNacimiento');");
			sb.append("if(hecho=='NACIMIENTO'){");
				sb.append("for (var i = 0; i < municipioNacimiento.children.length; ++i) {");
				sb.append("municipioNacimiento.remove(i)");
				sb.append("}");
				sb.append("municipioNacimiento.options.length = 0;");
			sb.append("}");
			
			sb.append("if(comunidad.options[comunidad.selectedIndex].value == '' || comunidad.options[comunidad.selectedIndex].value == '-------------------------'){");
				sb.append("alert('Debe introducir una comunidad autónima válida');");
			sb.append("}");
			sb.append("else{");
				sb.append("window.open('jsp/svd/obtenerComuProvMun.jsp?valor=COMUNIDAD;'+comunidad.options[comunidad.selectedIndex].value+';'+hecho,'','width=3,height=3,top='+(screen.height/2-100)+',left='+(screen.width/2-100));");
			sb.append("}");
		sb.append("}");
		sb.append("function obtenerMunicipio(provincia, hecho){ ");
			sb.append("var municipio = document.getElementById('municipio');");
			sb.append("if(hecho=='REGISTRO'){");
				sb.append("for (var i = 0; i < municipio.children.length; ++i) {");
				sb.append("municipio.remove(i)");
				sb.append("}");			
				sb.append("municipio.options.length = 0;");
			sb.append("}");
			sb.append("var municipioNacimiento = document.getElementById('municipioNacimiento');");
			sb.append("if(hecho=='NACIMIENTO'){");
				sb.append("for (var i = 0; i < municipioNacimiento.children.length; ++i) {");
				sb.append("municipioNacimiento.remove(i)");
				sb.append("}");
				sb.append("municipioNacimiento.options.length = 0;");
			sb.append("}");
			
			sb.append("if(provincia.options[provincia.selectedIndex].value == '' || provincia.options[provincia.selectedIndex].value == '-------------------------'){");
				sb.append("alert('Debe introducir una comunidad autónima válida');");
			sb.append("}");
			sb.append("else{");
				sb.append("window.open('jsp/svd/obtenerComuProvMun.jsp?valor=PROVINCIA;'+provincia.options[provincia.selectedIndex].value+';'+hecho,'','width=3,height=3,top='+(screen.height/2-100)+',left='+(screen.width/2-100));");
			sb.append("}");
		sb.append("}");
		
		sb.append("function obtenerRegistro( provincia, municipio){ ");
		sb.append("var provincia = document.getElementById('provincia');");
		sb.append("var municipio = document.getElementById('municipio');");		
		sb.append("if(provincia.options[provincia.selectedIndex].value == '' || provincia.options[provincia.selectedIndex].value == '-------------------------'){");
			sb.append("alert('Debe introducir una comunidad autónima válida');");
		sb.append("}");
		sb.append("else{");
			sb.append("window.open('jsp/svd/obtenerRegistroCivil.jsp?valor='+provincia.options[provincia.selectedIndex].value+';'+municipio.options[municipio.selectedIndex].value,'','width=3,height=3,top='+(screen.height/2-100)+',left='+(screen.width/2-100));");
		sb.append("}");
	sb.append("}");
		
		sb.append("function prueba(){ ");
		sb.append("alert('prueba');");
		sb.append("}");
		sb.append("</script>");
		
		sb.append("</br></br>");
		sb.append("<b>DATOS HECHO REGISTRAL</b>");
		sb.append("<div>");
		sb.append("<label> Fecha del evento (aaaa-mm-dd)(*):</label>");
		sb.append("<input type='text' name='fechaHechoRegistral' id='fechaHechoRegistral' size='12' />");
		sb.append("</div>");
		
		sb.append("</br>");
				
		String headerSelect = "<SELECT  onchange='obtenerProvincia(comunidad, \"REGISTRO\")'  id='comunidad' datoespecifico='1' class='classSelect' NAME='comunidad' label='Periodo' SIZE='1'>";
		String bodySelect = "<OPTION VALUE=''></OPTION>";
		String finishSelect = "</SELECT>";
		ClienteLigeroProxy clienteLigero = new ClienteLigeroProxy();
		String[] comunidad = null;
		try {
			comunidad = clienteLigero.getComunidad();
		} catch (RemoteException e) {
			logger.error("Error. "+e.getMessage(), e);
			throw new RuntimeException("Error. "+e.getMessage(), e);
		}
		for(int i=0; i<comunidad.length; i++){
			String comun = comunidad[i];
			String [] vComun = comun.split("#");
			bodySelect = bodySelect + "<OPTION VALUE='" + vComun[0] + "'>" + vComun[1] + "</OPTION>";
		}

		String select = headerSelect + bodySelect + finishSelect;

		sb.append("<div>");
		sb.append("</span>Comunidad aútonoma (*)&nbsp;&nbsp;&nbsp;&nbsp;</span>");
		sb.append(select);

		
		String headerSelectProvincia = "<SELECT  onchange='obtenerMunicipio(provincia, \"REGISTRO\")'  id='provincia' datoespecifico='1' class='classSelect' NAME='provincia' label='Periodo' SIZE='1'>";
		String bodySelectProvincia = "<OPTION VALUE=''></OPTION>";
		String finishSelectProvincia = "</SELECT>";
		
		String selectProvincia = headerSelectProvincia + bodySelectProvincia + finishSelectProvincia;
		

		sb.append("</span>&nbsp;&nbsp;&nbsp;&nbsp;Provincia (*)&nbsp;&nbsp;&nbsp;&nbsp;</span>");
		sb.append(selectProvincia);
		sb.append("</div>");
		
		
		String headerSelectMunicipio = "<SELECT onchange='obtenerRegistro(provincia, municipio)'  id='municipio' datoespecifico='1' class='classSelect' NAME='municipio' label='Periodo' SIZE='1'>";
		String bodySelectMunicipio = "<OPTION VALUE=''></OPTION>";
		String finishSelectMunicipio = "</SELECT>";
		
		String selectMunicipio = headerSelectMunicipio + bodySelectMunicipio + finishSelectMunicipio;
		
		sb.append("<div>");
		sb.append("</span>&nbsp;&nbsp;&nbsp;&nbsp;Municipio (*)&nbsp;&nbsp;&nbsp;&nbsp;</span>");
		sb.append(selectMunicipio);
		sb.append("</div>");
		sb.append("</br>");
		
		sb.append("<div>");
		sb.append("</span>&nbsp;&nbsp;&nbsp;&nbsp;Registro Civil (*)&nbsp;&nbsp;&nbsp;&nbsp;</span>");
		sb.append("<input type='text' readonly='readonly' name='registro' id='registro' size='16' />");
		sb.append("</br></br>");
		sb.append("</div>");
		
		sb.append("<div>");
		sb.append("</span>&nbsp;&nbsp;&nbsp;&nbsp;Tomo (*)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</span>");
		sb.append("<input type='text' name='tomo' id='tomo' size='12' />");
		sb.append("</br>");
		sb.append("<font size='0.5'>Debe rellenarse mínimo con 5 caracteres, si el tomo posee menos de 5 caracteres se rellenará hasta 5 caracteres con ceros a la izquierda. Opcionalmente puede rellenarse con espacios en blanco, ceros o guines bajos hasta 10 caracteres.</font>");
		sb.append("</br>");
		sb.append("</div>");
		
		
		sb.append("<div>");
		sb.append("<label>&nbsp;&nbsp;&nbsp;&nbsp;Página(*)&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</label>");
		sb.append("<input type='text' name='pagina' id='pagina' size='12' />");
		sb.append("</br>");
		sb.append("<font size='0.5'>Debe rellenarse mínimo con 3 dígitos, si el tomo posee menos de 3 dígitos se rellenará hasta 3 dígitos con ceros a la izquierda. Opcionalmente puede rellenarse con espacios en blanco, ceros o guines bajos hasta 10 caracteres.</font>");
		sb.append("</br>");
		sb.append("</div>");
		
		
		sb.append("</br></br>");
		/*
		sb.append("<b>DATOS DE NACIMIENTO</b>");
		sb.append("<div>");
		sb.append("<label> Fecha nacimiento(aaaa-mm-dd)(*):</label>");
		sb.append("<input type='text' name='fechaNacimiento' id='fechaNacimiento' size='12' />");
		sb.append("</div>");
		
		
		sb.append("</br>");
		
		String headerSelectNacimiento = "<SELECT  onchange='obtenerProvincia(comunidadNacimiento, \"NACIMIENTO\")'  id='comunidadNacimiento' datoespecifico='1' class='classSelect' NAME='comunidadNacimiento' label='Periodo' SIZE='1'>";
		String bodySelectNacimiento = "<OPTION VALUE=''></OPTION>";
		String finishSelectNacimiento = "</SELECT>";
		String[] comunidadNacimiento = null;
		try {
			comunidadNacimiento = clienteLigero.getComunidad();
		} catch (RemoteException e) {
			throw new RuntimeException(e);
		}
		for(int i=0; i<comunidadNacimiento.length; i++){
			String comun = comunidadNacimiento[i];
			String [] vComun = comun.split("#");
			bodySelectNacimiento = bodySelectNacimiento + "<OPTION VALUE='" + vComun[0] + "'>" + vComun[1] + "</OPTION>";
		}

		String selectNacimiento = headerSelectNacimiento + bodySelectNacimiento + finishSelectNacimiento;

		sb.append("<div>");
		sb.append("</span>Comunidad aútonoma (*)&nbsp;&nbsp;&nbsp;&nbsp;</span>");
		sb.append(selectNacimiento);

		
		String headerSelectProvinciaNacimiento = "<SELECT  onchange='obtenerMunicipio(provinciaNacimiento, \"NACIMIENTO\")'  id='provinciaNacimiento' datoespecifico='1' class='classSelect' NAME='provinciaNacimiento' label='Periodo' SIZE='1'>";
		String bodySelectProvinciaNacimiento = "<OPTION VALUE=''></OPTION>";
		String finishSelectProvinciaNacimiento = "</SELECT>";
		
		String selectProvinciaNacimiento = headerSelectProvinciaNacimiento + bodySelectProvinciaNacimiento + finishSelectProvinciaNacimiento;
		

		sb.append("</span>&nbsp;&nbsp;&nbsp;&nbsp;Provincia (*)&nbsp;&nbsp;&nbsp;&nbsp;</span>");
		sb.append(selectProvinciaNacimiento);
		sb.append("</div>");
		
		
		String headerSelectMunicipioNacimiento = "<SELECT  id='municipioNacimiento' datoespecifico='1' class='classSelect' NAME='municipioNacimiento' label='Periodo' SIZE='1'>";
		String bodySelectMunicipioNacimiento = "<OPTION VALUE=''></OPTION>";
		String finishSelectMunicipioNacimiento = "</SELECT>";
		
		String selectMunicipioNacimiento = headerSelectMunicipioNacimiento + bodySelectMunicipioNacimiento + finishSelectMunicipioNacimiento;
		
		sb.append("<div>");
		sb.append("</span>&nbsp;&nbsp;&nbsp;&nbsp;Municipio (*)&nbsp;&nbsp;&nbsp;&nbsp;</span>");
		sb.append(selectMunicipioNacimiento);
		sb.append("</div>");		
		
		sb.append("</br></br>");
		*/
		return sb.toString();
	}
	
	public OMElement parseXmlDatosEspecificos(HttpServletRequest request, String version) {
		try {
			String fechaHechoRegistral = readParameter("fechaHechoRegistral", request);
			String certificado = readParameter("certificado", request);
			if ((fechaHechoRegistral == null) || ("".equals(fechaHechoRegistral))) {
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
			OMElement datosTitular = null;
			//Consulta de matrimonio
			if(certificado.equals("SVDSCCMWS01")){
				datosTitular = fac.createOMElement("DatosAdicionalesTitular", omNsDe);
			}
			//Consulta de nacimiento y Consulta de defunción
			if(certificado.equals("SVDSCCNWS01") || certificado.equals("SVDSCCDWS01")){
				datosTitular = fac.createOMElement("DatosAdicionalesTitularConsulta", omNsDe);
			}

			
			//OMElement tomo = fac.createOMElement();

			if(StringUtils.isNotEmpty(fechaHechoRegistral)){	
				OMElement fechaRegistral = fac.createOMElement("FechaHechoRegistral", omNsDe);
				fechaRegistral.setText(fechaHechoRegistral);
				datosTitular.addChild(fechaRegistral);
			}
			
			OMElement ausenciaSegunApellido = fac.createOMElement("AusenciaSegundoApellido", omNsDe);
			ausenciaSegunApellido.setText("false");
			datosTitular.addChild(ausenciaSegunApellido);
			
			consulta.addChild(datosTitular);
			
			OMElement consultaPorDatosRegistrales = fac.createOMElement("ConsultaPorDatosRegistrales",omNsDe);
			OMElement registro = fac.createOMElement("RegistroCivil",omNsDe);
			String sRegistro = readParameter("registro", request);
			registro.setText(sRegistro);
			consultaPorDatosRegistrales.addChild(registro);
			
			OMElement tomo = fac.createOMElement("Tomo",omNsDe);
			String sTomo = readParameter("tomo", request);
			tomo.setText(sTomo);
			consultaPorDatosRegistrales.addChild(tomo);
			
			OMElement pagina = fac.createOMElement("Pagina",omNsDe);
			String sPagina = readParameter("pagina", request);
			pagina.setText(sPagina);
			consultaPorDatosRegistrales.addChild(pagina);
			consulta.addChild(consultaPorDatosRegistrales);
			
			
			/*OMElement consultaPorOtrosDatos = fac.createOMElement("ConsultaPorOtrosDatos", omNsDe);
			
			String provinciaHechoReg = readParameter("provincia", request);
			String municipioHechoReg = readParameter("municipio", request);
			
			if(StringUtils.isNotEmpty(provinciaHechoReg) && StringUtils.isNotEmpty(municipioHechoReg)){
				OMElement poblacionHechoRegistral = fac.createOMElement("PoblacionHechoRegistral", omNsDe);
				poblacionHechoRegistral.setText(provinciaHechoReg+municipioHechoReg);
				consultaPorOtrosDatos.addChild(poblacionHechoRegistral);
			}
			
			String sfechaNacimiento = readParameter("fechaNacimiento", request);
			if(StringUtils.isNotEmpty(sfechaNacimiento)){				
				OMElement fechaNacimiento = fac.createOMElement("FechaNacimiento", omNsDe);
				fechaNacimiento.setText(sfechaNacimiento);
				consultaPorOtrosDatos.addChild(fechaNacimiento);
			}
			
			String provinciaNacimiento = readParameter("provinciaNacimiento", request);
			String municipioNacimiento = readParameter("municipioNacimiento", request);
			
			if(StringUtils.isNotEmpty(provinciaNacimiento) && StringUtils.isNotEmpty(municipioNacimiento)){
				OMElement poblacionNacimiento = fac.createOMElement("PoblacionNacimiento", omNsDe);
				poblacionNacimiento.setText(provinciaNacimiento+municipioNacimiento);
				consultaPorOtrosDatos.addChild(poblacionNacimiento);
			}		
			
			consulta.addChild(consultaPorOtrosDatos);*/
			
			datosEspecificos.addChild(consulta);
			
			return datosEspecificos;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
}

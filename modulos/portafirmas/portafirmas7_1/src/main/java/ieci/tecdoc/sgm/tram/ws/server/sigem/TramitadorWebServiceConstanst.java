package ieci.tecdoc.sgm.tram.ws.server.sigem;

import java.util.Collections;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.annotation.Resource;

public class TramitadorWebServiceConstanst {
	
	@Resource(name = "tramitacionWebServiceProperties")
	private Properties tramitacionWebServiceProperties;
	
	private final Map<String, String> WSDL_URL = initMap();
	
	public Map<String, String> getWsdlUrl() {
		return WSDL_URL;
	}

	private Map<String, String> initMap() {
		
	        Map<String, String> map = new HashMap<>();
	        map.put("001","https://sigempruebas2.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("002","https://sei3.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("003","https://sei4.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("004","https://sei3.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("005","https://sei1.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("006","https://sei3.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("007","https://sei3.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("008","https://sei7.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("009","https://sei7.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("010","https://sei7.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("011","https://sei7.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("012","https://sei4.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("013","https://sei4.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("014","https://sei4.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("015","https://sei6.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("016","https://sei3.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("017","https://sei3.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("018","https://sei7.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("019","https://sei3.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("020","https://sei6.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("021","https://sei3.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("022","https://sei6.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("023","https://sei7.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("024","https://sei7.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("025","https://sei7.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("026","https://sei5.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("027","https://sei4.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("028","https://sei4.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("029","https://sei3.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("030","https://sei7.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("031","https://sei6.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("032","https://sei7.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("033","https://sei6.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("034","https://sei4.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("035","https://sei7.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("036","https://sei4.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("037","https://sei5.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("038","https://sei3.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("039","https://sei3.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("040","https://sei5.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("041","https://sei4.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("042","https://sei4.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("043","https://sei6.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("044","https://sei7.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("045","https://sei5.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("046","https://sei7.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("047","https://sei3.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("048","https://sei5.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("049","https://sei7.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("050","https://sei6.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("051","https://sei4.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("052","https://sei4.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("053","https://sei3.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("054","https://sei5.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("055","https://sei6.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("056","https://sei7.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("057","https://sei7.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("058","https://sei7.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("059","https://sei6.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("060","https://sei7.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("061","https://sei4.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("062","https://sei3.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("063","https://sei6.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("064","https://sei7.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("065","https://sei6.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("066","https://sei4.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("067","https://sei4.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("068","https://sei4.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("069","https://sei4.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("070","https://sei7.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("071","https://sei6.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("072","https://sei1.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("073","https://sei1.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("074","https://sei4.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("075","https://sei3.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("076","https://sei3.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("077","https://sei1.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("078","https://sei3.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("079","https://sei6.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("080","https://sei7.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("081","https://sei3.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("082","https://sei3.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("083","https://sei6.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("084","https://sei4.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("085","https://sei5.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("086","https://sei4.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("087","https://sei7.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("088","https://sei3.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("089","https://sei4.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("090","https://sei1.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("091","https://sei7.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("092","https://sei6.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("093","https://sei6.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("094","https://sei4.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("095","https://sei7.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("096","https://sei4.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("097","https://sei3.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("098","https://sei6.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("099","https://sei3.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("100","https://sei3.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("101","https://sei7.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("102","https://sei3.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("103","https://sei5.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("104","https://sei5.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("105","https://sei5.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("106","https://sei5.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("107","https://sei4.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("108","https://sei5.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("109","https://sei5.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("110","https://sei5.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("111","https://sei5.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("112","https://sei5.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("113","https://sei5.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("114","https://sei7.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("115","https://sei5.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("116","https://sei5.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("117","https://sei5.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("118","https://sei1.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("119","https://sei5.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("120","https://sei6.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("121","https://sei6.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("122","https://sei4.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("123","https://sei6.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("124","https://sei6.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");
	        map.put("125","https://sei6.dipucr.es:4443/SIGEM_TramitacionWS/services/TramitacionWebService?wsdl");	        
	        return Collections.unmodifiableMap(map);
	        
	}
	
private Map<String, TramitacionWebServiceEntidad> initMap2() {
		
		
		Map<String, TramitacionWebServiceEntidad> map = new HashMap<>();
		Enumeration e = tramitacionWebServiceProperties.propertyNames();
		
		while (e.hasMoreElements()) {
			String keyEntidad = (String) e.nextElement();
		
			TramitacionWebServiceEntidad entidad = new TramitacionWebServiceEntidad();
			String propiedadEntidad = tramitacionWebServiceProperties.getProperty(keyEntidad);
			String [] arrDatosEntidad = propiedadEntidad.split(";");
			//entidad.xxx = id;dominio;ip;puerto;nombre
			entidad.setId(arrDatosEntidad[0]);
			entidad.setWsdl(arrDatosEntidad[1]);
			entidad.setIp(arrDatosEntidad[2]);
			entidad.setPuerto(arrDatosEntidad[3]);
			entidad.setNombre(arrDatosEntidad[4]);
			map.put(entidad.getId(), entidad);
		}	
		
		return Collections.unmodifiableMap(map);
	}
	
}
	



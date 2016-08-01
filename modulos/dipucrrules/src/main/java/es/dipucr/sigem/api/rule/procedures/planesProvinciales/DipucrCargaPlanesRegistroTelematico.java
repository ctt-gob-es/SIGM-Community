package es.dipucr.sigem.api.rule.procedures.planesProvinciales;

import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;

import java.io.BufferedWriter;
import java.io.FileWriter;

import org.apache.log4j.Logger;

import es.dipucr.domain.planesProvinciales.MunicipioPlanesProvinciales;
import es.dipucr.domain.planesProvinciales.PlanProvincial;
import es.dipucr.services.server.planesProvinciales.PlanProvincialServicioProxy;

public class DipucrCargaPlanesRegistroTelematico {
	protected static final Logger logger = Logger.getLogger(DipucrCargaPlanesRegistroTelematico.class);

	public static String getAllPlanesByAnio(String anio){		

		FileWriter fichero = null;

		PlanProvincialServicioProxy planService;
		PlanProvincial[] planes;
		
		String fileName = null;
		
		try {			
			logger.info("MQE vamos a recuperar los planes");
			planService = new PlanProvincialServicioProxy();
        	planes = planService.getPlanesProvincialesByAnio(anio);
        	
			fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/Planes-"+anio+".xml";		
			fichero = new FileWriter(fileName);
			  
		    StringBuffer sbDatos = new StringBuffer();
		    sbDatos.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><listado>");
	        
	        for(int i= 0; i<planes.length;i++){
	        	PlanProvincial plan = planes[i];
	        	sbDatos.append("<dato><valor>"+plan.getNombreMunicipio()+"</valor><sustituto>"+plan.getDenominacion()+"</sustituto></dato>");
	        }		
	        sbDatos.append("</listado>");
			
			BufferedWriter bw = new BufferedWriter(fichero);
			bw.write(sbDatos.toString());
			bw.close();
			logger.info("fileName "+fileName);			
		} catch (Exception e) {
        	logger.error(e.getMessage(), e);
		} finally {
			try {
				if (null != fichero)
					fichero.close();
			} catch (Exception e2) {
            	logger.error(e2.getMessage(), e2);
			}
		}
		return fileName;
	}
	
	public static String getPlanesByMpioAnio(String municipio, String anio){		

		FileWriter fichero = null;

		PlanProvincialServicioProxy planService;
		PlanProvincial[] planes;
		
		String fileName = null;
		
		try {			
			logger.info("MQE vamos a recuperar los planes");
			planService = new PlanProvincialServicioProxy();
//        	planes = planService.getPlanesProvincialesByAnio(anio);
			planes = planService.getPlanProvincial(municipio,anio);
        	
			fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/Planes-"+municipio+"-"+anio+".xml";		
			fichero = new FileWriter(fileName);
			  
		    StringBuffer sbDatos = new StringBuffer();
		    sbDatos.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><listado>");
	        
	        for(int i= 0; i<planes.length;i++){
	        	PlanProvincial plan = planes[i];
	        	sbDatos.append("<dato><valor>"+i+"</valor><sustituto>"+plan.getDenominacion()+"</sustituto></dato>");
	        }		
	        sbDatos.append("</listado>");
			
			BufferedWriter bw = new BufferedWriter(fichero);
			bw.write(sbDatos.toString());
			bw.close();
			logger.info("fileName "+fileName);			
		} catch (Exception e) {
        	logger.error(e.getMessage(), e);
		} finally {
			try {
				if (null != fichero)
					fichero.close();
			} catch (Exception e2) {
            	logger.error(e2.getMessage(), e2);
			}
		}
		return fileName;
	}
	
	public static String getMunicipios(){
		
		String anio="2011";

		FileWriter fichero = null;

		PlanProvincialServicioProxy planService;
		MunicipioPlanesProvinciales[] municipios;
		
		String fileName = null;
		
		try {			
			logger.info("MQE vamos a recuperar los planes");
			planService = new PlanProvincialServicioProxy();
//        	planes = planService.getPlanesProvincialesByAnio(anio);
			municipios = planService.getAllMunicipios();
        	
			fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/Planes-"+anio+".xml";		
			fichero = new FileWriter(fileName);
			  
		    StringBuffer sbDatos = new StringBuffer();
		    sbDatos.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><listado>");
	        
	        for(int i= 0; i<municipios.length;i++){
	        	MunicipioPlanesProvinciales municipio = municipios[i];
	        	sbDatos.append("<dato><valor>"+municipio.getCodigo()+"</valor><sustituto>"+municipio.getNombreMunicipio()+"</sustituto></dato>");
	        }		
	        sbDatos.append("</listado>");
			
			BufferedWriter bw = new BufferedWriter(fichero);
			bw.write(sbDatos.toString());
			bw.close();
			logger.info("fileName "+fileName);			
		} catch (Exception e) {
        	logger.error(e.getMessage(), e);
		} finally {
			try {
				if (null != fichero)
					fichero.close();
			} catch (Exception e2) {
            	logger.error(e2.getMessage(), e2);
			}
		}
		return fileName;
	}
	
}

package es.dipucr.sigem.registroTelematicoWeb.formulario.common;

import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Vector;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.AccesoBBDDTramitador;

public class XmlAyuntamiento{
	protected static final Logger logger = Logger.getLogger(XmlAyuntamiento.class);

	@SuppressWarnings("rawtypes")
	public static String getAyuntamientos(){
		FileWriter fichero = null;

		String fileName = null;
		
		try {
			fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/ayuntamiento.xml";		
			fichero = new FileWriter(fileName);
			  
		    StringBuilder sbAyu = new StringBuilder();
	        sbAyu.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><listado>");
	        
	        Vector listAyuntamiento = new Vector();
	        
	        String entidad = "001";

	        AccesoBBDDTramitador accsTramitador = new AccesoBBDDTramitador(entidad);
	        listAyuntamiento = accsTramitador.getDatosTablaValidacion("REC_VLD_TBL_MUNICIPIOS");
	        
	        for(int i= 0; i<listAyuntamiento.size();i++){
	        	Ayuntamiento ayuntamiento = (Ayuntamiento)listAyuntamiento.get(i);
	        	sbAyu.append("<ayuntamiento><valor>"+ayuntamiento.getValor()+"</valor><sustituto>"+ayuntamiento.getSustituto()+"</sustituto></ayuntamiento>");
	        }		
	        sbAyu.append("</listado>");
			
			BufferedWriter bw = new BufferedWriter(fichero);
			bw.write(sbAyu.toString());
			bw.close();
			logger.info("fileName "+fileName);
			
	        } catch (Exception e) {
				logger.error(e.getMessage(), e);
	        } finally {
	           try {
	           // Nuevamente aprovechamos el finally para 
	           // asegurarnos que se cierra el fichero.
	           if (null != fichero)
	              fichero.close();
	           } catch (Exception e2) {
	   				logger.error(e2.getMessage(), e2);
	           }
	        }
	        return fileName;
		}
	}

package es.dipucr.sigem.registroTelematicoWeb.formulario;



import java.io.BufferedWriter;
import java.io.FileWriter;
import java.rmi.RemoteException;
import java.util.Vector;

import org.apache.log4j.Logger;

import es.dipucr.verifdatos.services.ClienteLigeroProxy;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;

public class DatosClienteLigero{
	
	/** Constantes **/
	public static String COD_ERROR = "[ERROR]";
	
	/** Logger **/
	protected static final Logger LOGGER = Logger.getLogger(DatosClienteLigero.class);
	
	/**
	 * Devuelve los datos de una tabla de validación con sustituto
	 * @param nombreTabla
	 * @param entidad
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String getDatosProvinciaByComunidad(String idComunidad){
		
		FileWriter fichero = null;
		String fileName = null;
		
		try {
			fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/"+ FileTemporaryManager.getInstance().newFileName()+".xml";		
			fichero = new FileWriter(fileName);
			  
		    StringBuffer sbDatos = new StringBuffer();
		    sbDatos.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><listado>");
	        Vector listDatos = new Vector();

	        ClienteLigeroProxy clienteLigero = new ClienteLigeroProxy();
			String[] provincia = null;
			try {
				provincia = clienteLigero.getProvincia(idComunidad);
			} catch (RemoteException e) {
				throw new RuntimeException(e);
			}
			for(int i=0; i<provincia.length; i++){
				String sProvincia = provincia[i];
				String [] vProvincia = sProvincia.split("#");
				String prov = vProvincia[1].replace("'", "");
				prov = prov.replace("`", "");
				sbDatos.append("<dato><valor>"+vProvincia[0]+"</valor><sustituto>"+prov+"</sustituto></dato>");
	        }		
	        sbDatos.append("</listado>");
			
			BufferedWriter bw = new BufferedWriter(fichero);
			bw.write(sbDatos.toString());
			bw.close();
			LOGGER.info("fileName "+fileName);			
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			try {
	           // Nuevamente aprovechamos el finally para 
	           // asegurarnos que se cierra el fichero.
				if (null != fichero)
					fichero.close();
			} catch (Exception e2) {
				LOGGER.error(e2.getMessage(), e2);
			}
		}
		return fileName;
	}
	
	public static String getDatosMunicipioByProvincia(String idProvincia){
		
		FileWriter fichero = null;
		String fileName = null;
		
		try {
			fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/"+ FileTemporaryManager.getInstance().newFileName()+".xml";		
			fichero = new FileWriter(fileName);
			  
		    StringBuffer sbDatos = new StringBuffer();
		    sbDatos.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><listado>");
	        Vector listDatos = new Vector();

	        ClienteLigeroProxy clienteLigero = new ClienteLigeroProxy();
			String[] municio = null;
			try {
				municio = clienteLigero.getMunicipio(idProvincia);
			} catch (RemoteException e) {
				throw new RuntimeException(e);
			}
			for(int i=0; i<municio.length; i++){
				String sMunicipio = municio[i];
				String [] vMunicipio = sMunicipio.split("#");
				String munic = vMunicipio[1].replace("'", "");
				munic = munic.replace("`", "");
	        	sbDatos.append("<dato><valor>"+vMunicipio[0]+"</valor><sustituto>"+munic+"</sustituto></dato>");
	        }		
	        sbDatos.append("</listado>");
			
			BufferedWriter bw = new BufferedWriter(fichero);
			bw.write(sbDatos.toString());
			bw.close();
			LOGGER.info("fileName "+fileName);			
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			try {
	           // Nuevamente aprovechamos el finally para 
	           // asegurarnos que se cierra el fichero.
				if (null != fichero)
					fichero.close();
			} catch (Exception e2) {
				LOGGER.error(e2.getMessage(), e2);
			}
		}
		return fileName;
	}
	
	public static String getRegistroCivil(String idProvincia, String idMunicipio){
		
		String registro = "";
		
		try {
			  
	        ClienteLigeroProxy clienteLigero = new ClienteLigeroProxy();
			try {
				registro = clienteLigero.getRegistroCivil(idProvincia, idMunicipio);
			} catch (RemoteException e) {
				throw new RuntimeException(e);
			}
					
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return registro;
	}
}
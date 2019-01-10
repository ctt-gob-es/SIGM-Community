package es.dipucr.sigem.registroTelematicoWeb.formulario.common;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import es.dipucr.sigem.api.rule.common.AccesoBBDDTramitador;

public class XmlCargaDatos{
	
	/** Constantes **/
	public static String COD_ERROR = "[ERROR]";
	
	/** Logger **/
	protected static final Logger LOGGER = Logger.getLogger(XmlCargaDatos.class);
	
	/**
	 * Devuelve los datos de una tabla de validación con sustituto
	 * @param nombreTabla
	 * @param entidad
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String getDatosTablaValidacionConSustituto(String nombreTabla, String entidad){
		
		FileWriter fichero = null;
		String fileName = null;
		
		try {
			fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/"+nombreTabla+".xml";		
			fichero = new FileWriter(fileName);
			  
		    StringBuilder sbDatos = new StringBuilder();
		    sbDatos.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><listado>");
	        
	        Vector listDatos = new Vector();

	        AccesoBBDDTramitador accsTramitador = new AccesoBBDDTramitador(entidad);
	        listDatos = accsTramitador.getDatosTablaValidacion(nombreTabla);
	        
	        for(int i= 0; i<listDatos.size();i++){
	        	Dato ayuntamiento = (Dato)listDatos.get(i);
	        	sbDatos.append("<dato><valor>"+ayuntamiento.getValor()+"</valor><sustituto>"+ayuntamiento.getSustituto()+"</sustituto></dato>");
	        }		
	        sbDatos.append("</listado>");
			
			BufferedWriter bw = new BufferedWriter(fichero);
			bw.write(sbDatos.toString());
			bw.close();
			LOGGER.info("fileName " + fileName);			
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		} finally {
			try {
				if (null != fichero){
					fichero.close();
				}
			} catch (Exception e2) {
				LOGGER.error(e2.getMessage(), e2);
			}
		}
		return fileName;
	}
	
	/**
	 * [eCenpri-Felipe #743]
	 * Devuelve los datos de una tabla de validación con sustituto
	 * @param nombreTabla
	 * @param entidad
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String getDatosVldtblConSustitutoByOrden(String nombreTabla, String entidad){
		
		FileWriter fichero = null;
		String fileName = null;
		
		try {
			fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/"+nombreTabla+".xml";		
			fichero = new FileWriter(fileName);
			  
		    StringBuilder sbDatos = new StringBuilder();
		    sbDatos.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><listado>");
	        
	        Vector listDatos = new Vector();

	        AccesoBBDDTramitador accsTramitador = new AccesoBBDDTramitador(entidad);
	        listDatos = accsTramitador.getDatosTablaValidacionByOrden(nombreTabla);
	        
	        for(int i= 0; i<listDatos.size();i++){
	        	Dato dato = (Dato)listDatos.get(i);
	        	sbDatos.append("<dato>");
	        	sbDatos.append("<valor>" + dato.getValor() + "</valor>");
	        	sbDatos.append("<sustituto>" + dato.getSustituto() + "</sustituto>");
	        	sbDatos.append("</dato>");
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
				if (null != fichero){
					fichero.close();
				}
			} catch (Exception e2) {
				LOGGER.error(e2.getMessage(), e2);
			}
		}
		return fileName;
	}
	
	/**
	 * Devuelve los datos de una consulta
	 * @param nombre
	 * @param consulta
	 * @param entidad
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String getDatosConsulta(String nombre, String consulta, String entidad){
		
		FileWriter fichero = null;
		String fileName = null;
		
		try {
			fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/"+nombre+".xml";		
			fichero = new FileWriter(fileName);
			
			consulta = consulta.replace("#", "'");
			  
		    StringBuilder sbDatos = new StringBuilder();
		    sbDatos.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><listado>");
	        
	        Vector listDatos = new Vector();

	        AccesoBBDDTramitador accsTramitador = new AccesoBBDDTramitador(entidad);
	        listDatos = accsTramitador.ejecutaConsulta(consulta);
	        
	        for(int i= 0; i<listDatos.size();i++){
	        	Dato ayuntamiento = (Dato)listDatos.get(i);
	        	sbDatos.append("<dato><valor>"+ayuntamiento.getValor()+"</valor><sustituto>"+ayuntamiento.getSustituto()+"</sustituto></dato>");
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
				if (null != fichero){
					fichero.close();
				}
			} catch (Exception e2) {
				LOGGER.error(e2.getMessage(), e2);
			}
		}
		return fileName;
	}
	
	/**
	 * Devuelve los datos de una consulta
	 * @param nombre
	 * @param consulta
	 * @param entidad
	 * @return
	 */
	@SuppressWarnings("rawtypes")
	public static String getDatosConsulta(String nombre, String consulta, int numColumnas, String entidad){
		
		FileWriter fichero = null;
		String fileName = null;
		
		try {
			fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/"+nombre+".xml";		
			fichero = new FileWriter(fileName);
			
			consulta = consulta.replace("#", "'");
			  
		    StringBuilder sbDatos = new StringBuilder();
		    sbDatos.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?><listado>");
	        
	        Vector listDatos = new Vector();

	        AccesoBBDDTramitador accsTramitador = new AccesoBBDDTramitador(entidad);
	        listDatos = accsTramitador.ejecutaConsulta(consulta, numColumnas);
	        
	        for(int i= 0; i<listDatos.size();i++){
	        	Dato ayuntamiento = (Dato)listDatos.get(i);
	        	sbDatos.append("<dato><valor>"+ayuntamiento.getValor()+"</valor><sustituto>"+ayuntamiento.getSustituto()+"</sustituto>");
	        	
	        	if(numColumnas > 0){
	        		List<String> resultados = ayuntamiento.getListaResultados();
	        		int count = 1;
	        		for(String resultado : resultados){
	        			sbDatos.append("<columna" + count + ">" + resultado + "</columna" + count + ">");
	        			count++;
	        		}
	        	}
	        	sbDatos.append("</dato>");
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
				if (null != fichero){
					fichero.close();
				}
			} catch (Exception e2) {
				LOGGER.error(e2.getMessage(), e2);
			}
		}
		return fileName;
	}
	
	/**
	 * [eCenpri-Felipe]
	 * Devuelve el resultado único de la consulta en formato cadena
	 * @param nombre
	 * @param consulta
	 * @param entidad
	 * @return
	 * @throws ISPACException 
	 */
	public static String getDatoUnico(String consulta, String entidad) throws ISPACException{
		
		String result = null;
		
		//Acceso a la BBDD de tramitador de la entidad
		AccesoBBDDTramitador accsTramitador = new AccesoBBDDTramitador(entidad);
		
		//Consulta
		consulta = consulta.replace("#", "'");
        result = accsTramitador.ejecutaConsultaDatoUnico(consulta);
	        
		return result;
	}
}

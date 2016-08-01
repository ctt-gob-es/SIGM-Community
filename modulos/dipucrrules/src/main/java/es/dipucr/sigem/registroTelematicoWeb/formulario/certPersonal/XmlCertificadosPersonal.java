package es.dipucr.sigem.registroTelematicoWeb.formulario.certPersonal;

import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;

import java.io.BufferedWriter;
import java.io.FileWriter;

import org.apache.log4j.Logger;

import es.dipucr.webempleado.services.certificadosPersonal.CertificadosPersonalWSProxy;
import es.dipucr.webempleado.services.certificadosPersonal.ItemLista;

public class XmlCertificadosPersonal {

	protected static final Logger logger = Logger.getLogger(XmlCertificadosPersonal.class);
	
	/**
	 * Devuelve los tipos de certificado dados de alta en la BBDD de personal
	 * @param nombreTabla
	 * @param entidad
	 * @return
	 */
	public static String getDatosTiposCert(){
		
		FileWriter fichero = null;
		String fileName = null;
		
		try {
			fileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/tiposCertPersonal.xml";		
			fichero = new FileWriter(fileName);
			  
		    StringBuffer sbDatos = new StringBuffer();
		    sbDatos.append("<?xml version=\"1.0\" encoding=\"ISO-8859-1\"?>");
		    sbDatos.append("<listado>");
	        
	        CertificadosPersonalWSProxy certPersoWS = new CertificadosPersonalWSProxy();
	        ItemLista[] arrDatos = certPersoWS.getTiposCertificado();
	        
	        for(int i= 0; i<arrDatos.length; i++){
	        	ItemLista tipoCert = arrDatos[i];
	        	sbDatos.append("<dato>");
	        	sbDatos.append("<valor>" + tipoCert.getValor() + "</valor>");
	        	sbDatos.append("<sustituto>" + tipoCert.getDescripcion() + "</sustituto>");
	        	sbDatos.append("</dato>");
	        }		
	        sbDatos.append("</listado>");
			
			BufferedWriter bw = new BufferedWriter(fichero);
			bw.write(sbDatos.toString());
			bw.close();
			logger.warn("fileName " + fileName);
			
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

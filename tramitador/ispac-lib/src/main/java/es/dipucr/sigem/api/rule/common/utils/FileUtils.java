package es.dipucr.sigem.api.rule.common.utils;

import ieci.tdw.ispac.ispaclib.utils.StringUtils;

import org.apache.log4j.Logger;

/**
 * Clase común de la que heredan otras clases que usan ficheros
 * Por ejemplo: ZipUtils, PdfUtil, etc
 * @author Felipe
 * @since [dipucr-Felipe #1148] 30.09.14
 */
public class FileUtils {
	
	public static final Logger LOGGER = Logger.getLogger(FileUtils.class);

	/**
	 * [eCenpri-Felipe] Normalizar los nombres para los anexos
	 * @param name
	 * @return
	 */
	public static String normalizarNombre(String filename){
		
		String result = filename;
		result = StringUtils.replace(result, "/", "_");
		result = StringUtils.replace(result, "\\", "_");
		return result;
	}

	public static String getExtensionByNombreDoc(String nombreFichero) {
		
		String extension =  "";
		
		try{
			if(StringUtils.isNotEmpty(nombreFichero)){
				extension = StringUtils.substring( nombreFichero, StringUtils.lastIndexOf( nombreFichero, '.') + 1);
			}
		} catch( Exception e){
			LOGGER.error("Error al recuperar la extensión del nombre del documento: " + nombreFichero + ". " + e.getMessage(), e);
		}
		return extension;
	}
}

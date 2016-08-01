package es.dipucr.sigem.api.rule.common.utils;

import ieci.tdw.ispac.ispaclib.utils.StringUtils;

/**
 * Clase común de la que heredan otras clases que usan ficheros
 * Por ejemplo: ZipUtils, PdfUtil, etc
 * @author Felipe
 * @since [dipucr-Felipe #1148] 30.09.14
 */
public class FileUtils {

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
}

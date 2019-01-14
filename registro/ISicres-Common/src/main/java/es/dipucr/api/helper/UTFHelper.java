/*
 * Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
 * Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
 * Solo podrá usarse esta obra si se respeta la Licencia. 
 * Puede obtenerse una copia de la Licencia en: 
 * http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
 * Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
 * Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
 */

package es.dipucr.api.helper;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

/**
 * Clase de utilidad para la transformación de objetos con la información de
 * unidades orgánicas.
 * 
 * @author cmorenog
 * 
 */
public class UTFHelper {
	
	public static final Logger LOGGER = Logger.getLogger(UTFHelper.class);
	
	public static String parseUTF8ToISO885916(String cadena){
		if(StringUtils.isNotEmpty(cadena)){

			cadena = cadena.replaceAll("´", "'");
			cadena = cadena.replaceAll("´", "'");
			cadena = cadena.replaceAll("’", "'");

			cadena = cadena.replaceAll("“", "\"");
			cadena = cadena.replaceAll("”", "\"");

			cadena = cadena.replaceAll("“", "\"");
			cadena = cadena.replaceAll("”", "\"");

			cadena = cadena.replaceAll("–", "-");

			cadena = cadena.replaceAll("…", ".");

			cadena = cadena.replaceAll("€", "¤");

			cadena = cadena.replaceAll("Ã¼", "ü");

			cadena = cadena.replaceAll("\u200B", "");

			cadena = cadena.replaceAll("​", "");

			cadena = cadena.replaceAll("–", "-");

			cadena = cadena.replaceAll("-", "-");

			cadena = cadena.replaceAll("-", "-");

			cadena = cadena.replaceAll("–", "-");

			cadena = cadena.replaceAll("\u2013", "-");

			cadena = cadena.replaceAll("\u201a", "'");

			cadena = cadena.replaceAll("\u201c", "\"");
			cadena = cadena.replaceAll("\u201d", "\"");
			cadena = cadena.replaceAll("\u201e", "\"");

			char caracterEuro = (char)(-92 & 0xFF);
	
			String cadenaResultado = "";
			for (char car : cadena.toCharArray()){
				if (car == caracterEuro){
					cadenaResultado += "EUR";
				} else {
					cadenaResultado += car;
				}
			}
			cadena = cadenaResultado;
		}
		return cadena;
	}
}
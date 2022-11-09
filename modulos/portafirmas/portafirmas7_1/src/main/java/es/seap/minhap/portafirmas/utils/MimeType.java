/* Copyright (C) 2012-13 MINHAP, Gobierno de España
   This program is licensed and may be used, modified and redistributed under the terms
   of the European Public License (EUPL), either version 1.1 or (at your
   option) any later version as soon as they are approved by the European Commission.
   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
   or implied. See the License for the specific language governing permissions and
   more details.
   You should have received a copy of the EUPL1.1 license
   along with this program; if not, you may find it at
   http://joinup.ec.europa.eu/software/page/eupl/licence-eupl */

/*

 Empresa desarrolladora: GuadalTEL S.A.

 Autor: Junta de AndalucÃ­a

 Derechos de explotaciÃ³n propiedad de la Junta de AndalucÃ­a.

 Ã‰ste programa es software libre: usted tiene derecho a redistribuirlo y/o modificarlo bajo los tÃ©rminos de la Licencia EUPL European Public License publicada 
 por el organismo IDABC de la ComisiÃ³n Europea, en su versiÃ³n 1.0. o posteriores.

 Ã‰ste programa se distribuye de buena fe, pero SIN NINGUNA GARANTÃ�A, incluso sin las presuntas garantÃ­as implÃ­citas de USABILIDAD o ADECUACIÃ“N A PROPÃ“SITO 
 CONCRETO. Para mas informaciÃ³n consulte la Licencia EUPL European Public License.

 Usted recibe una copia de la Licencia EUPL European Public License junto con este programa, si por algÃºn motivo no le es posible visualizarla, puede 
 consultarla en la siguiente URL: http://ec.europa.eu/idabc/servlets/Doc?id=31099

 You should have received a copy of the EUPL European Public License along with this program. If not, see http://ec.europa.eu/idabc/servlets/Doc?id=31096

 Vous devez avoir reÃ§u une copie de la EUPL European Public License avec ce programme. Si non, voir http://ec.europa.eu/idabc/servlets/Doc?id=31205

 Sie sollten eine Kopie der EUPL European Public License zusammen mit diesem Programm. Wenn nicht, finden Sie da 
 http://ec.europa.eu/idabc/servlets/Doc?id=29919

 */

package es.seap.minhap.portafirmas.utils;

import java.util.HashMap;

public class MimeType {
	/**
	 * Array de dos dimensiones con los tipos de contenidos del estandar MIME, 
	 * la primera columna del array es su extensi&oacute;n y la segunda es el Content-Type
	 * El valor de esta constante es {@value}.
	 */
	private final String mimeTypes[][] = {
			{ "txt", "text/plain" },
			{ "html", "text/html" },
			{ "xhtml", "application/xhtml+xml" },
			{ "ps", "application/postscript" },
			{ "aiff", "audio/x-aiff" },
			{ "acp", "application/acp" },
			{ "au", "audio/basic" },
			{ "avi", "video/x-msvideo" },
			{ "asf", "video/x-ms-asf" },
			{ "wmv", "video/x-ms-wmv" },
			{ "wma", "video/x-ms-wma" },
			{ "avx", "video/x-rad-screenplay" },
			{ "bcpio", "application/x-bcpio" },
			{ "bin", "application/octet-stream" },
			{ "cdf", "application/x-netcdf" },
			{ "cer", "application/x-x509-ca-cert" },
			{ "cgm", "image/cgm" },
			{ "class", "application/java" },
			{ "cpio", "application/x-cpio" },
			{ "csh", "application/x-csh" },
			{ "css", "text/css" },
			{ "doc", "application/msword" },
			{ "docx", "application/vnd.openxmlformats-officedocument.wordprocessingml.document" },
			{ "xml", "text/xml" },
			{ "dvi", "application/x-dvi" },
			{ "etx", "text/x-setext" },
			{ "gif", "image/gif" },
			{ "gml", "application/sgml" },
			{ "gtar", "application/x-gtar" },
			{ "gzip", "application/x-gzip" },
			{ "hdf", "application/x-hdf" },
			{ "hqx", "application/mac-binhex40" },
			{ "ief", "image/ief" },
			{ "bmp", "image/bmp" },
			{ "jpg", "image/jpeg" },
			{ "js", "application/x-javascript" },
			{ "latex", "application/x-latex" },
			{ "man", "application/x-troff-man" },
			{ "me", "application/x-troff-me" },
			{ "ms", "application/x-troff-mes" },
			{ "mif", "application/x-mif" },
			{ "mpg", "video/mpeg" },
			{ "mp3", "audio/x-mpeg" },
			{ "mp4", "video/mp4" },
			{ "mpeg2", "video/mpeg2" },
			{ "mov", "video/quicktime" },
			{ "movie", "video/x-sgi-movie" },
			{ "oda", "application/oda" },
			{ "pbm", "image/x-portable-bitmap" },
			{ "pdf", "application/pdf" },
			{ "pgm", "image/x-portable-graymap" },
			{ "png", "image/png" },
			{ "pnm", "image/x-portable-anymap" },
			{ "ppm", "image/x-portable-pixmap" },
			{ "ppt", "application/vnd.powerpoint" },
			{ "ras", "image/x-cmu-raster" },
			{ "rgb", "image/x-rgb" },
			{ "tr", "application/x-troff" },
			{ "rtf", "application/rtf" },
			{ "rtx", "text/richtext" },
			{ "sgml", "text/sgml" },
			{ "sh", "application/x-sh" },
			{ "shar", "application/x-shar" },
			{ "src", "application/x-wais-source" },
			{ "sv4cpio", "application/x-sv4cpio" },
			{ "sv4crc", "application/x-sv4crc" },
			{ "swf", "application/x-shockwave-flash" },
			{ "tar", "application/x-tar" },
			{ "tcl", "application/x-tcl" },
			{ "tex", "application/x-tex" },
			{ "texinfo", "application/x-texinfo" },
			{ "tcn", "text/tcn" },
			{ "tiff", "image/tiff" },
			{ "tsv", "text/tab-separated-values" },
			{ "ustar", "application/x-ustar" },
			{ "wav", "audio/x-wav" },
			{ "wrl", "x-world/x-vrml" },
			{ "xbm", "image/x-xbitmap" },
			{ "xls", "application/vnd.excel" },
			{ "xpm", "image/x-xpixmap" },
			{ "xwd", "image/x-xwindowdump" },
			{ "z", "application/x-compress" },
			{ "zip", "application/zip" },
			{ "dwg", "image/x-dwg" },
			{ "dwt", "image/x-dwt" },
			{ "msg", "message/rfc822" },
			{ "odt", "application/vnd.oasis.opendocument.text" },
			{ "ott", "application/vnd.oasis.opendocument.text-template" },
			{ "oth", "application/vnd.oasis.opendocument.text-web" },
			{ "odm", "application/vnd.oasis.opendocument.text-master" },
			{ "odg", "application/vnd.oasis.opendocument.graphics" },
			{ "otg", "application/vnd.oasis.opendocument.graphics-template" },
			{ "odp", "application/vnd.oasis.opendocument.presentation" },
			{ "otp", "application/vnd.oasis.opendocument.presentation-template" },
			{ "ods", "application/vnd.oasis.opendocument.spreadsheet" },
			{ "ots", "application/vnd.oasis.opendocument.spreadsheet-template" },
			{ "odc", "application/vnd.oasis.opendocument.chart" },
			{ "odf", "application/vnd.oasis.opendocument.formula" },
			{ "odb", "application/vnd.oasis.opendocument.database" },
			{ "odi", "application/vnd.oasis.opendocument.image" },
			{ "sxc", "application/vnd.sun.xml.calc" },
			{ "sxd", "application/vnd.sun.xml.draw" },
			{ "sxi", "application/vnd.sun.xml.impress" },
			{ "sxw", "application/vnd.sun.xml.writer" },
			{ "sda", "application/vnd.stardivision.draw" },
			{ "sdc", "application/vnd.stardivision.calc" },
			{ "sdd", "application/vnd.stardivision.impress" },
			{ "sdp", "application/vnd.stardivision.impress-packed" },
			{ "sds", "application/vnd.stardivision.chart" },
			{ "sdw", "application/vnd.stardivision.writer" },
			{ "sgl", "application/vnd.stardivision.writer-global" },
			{ "smf", "application/vnd.stardivision.math" }, 
			
			{ "dot", "application/msword" },
			{ "dotx", "application/vnd.openxmlformats-officedocument.wordprocessingml.template" },
			{ "docm", "application/vnd.ms-word.document.macroEnabled.12" },
			{ "dotm", "application/vnd.ms-word.template.macroEnabled.12" },
			{ "xlt", "application/vnd.ms-excel" },
			{ "xla", "application/vnd.ms-excel" },
			{ "xlsx", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" },
			{ "xltx", "application/vnd.openxmlformats-officedocument.spreadsheetml.template" },
			{ "xlsm", "application/vnd.ms-excel.sheet.macroEnabled.12" },
			{ "xltm", "application/vnd.ms-excel.template.macroEnabled.12" },
			{ "xlam", "application/vnd.ms-excel.addin.macroEnabled.12" },
			{ "xlsb", "application/vnd.ms-excel.sheet.binary.macroEnabled.12" },
			{ "pot", "application/vnd.ms-powerpoint" },
			{ "pps", "application/vnd.ms-powerpoint" },
			{ "ppa", "application/vnd.ms-powerpoint" },
			{ "pptx", "application/vnd.openxmlformats-officedocument.presentationml.presentation" },
			{ "potx", "application/vnd.openxmlformats-officedocument.presentationml.template" },
			{ "ppsx", "application/vnd.openxmlformats-officedocument.presentationml.slideshow" },
			{ "ppam", "application/vnd.ms-powerpoint.addin.macroEnabled.12" },
			{ "pptm", "application/vnd.ms-powerpoint.presentation.macroEnabled.12" },
			{ "potm", "application/vnd.ms-powerpoint.template.macroEnabled.12" },
			{ "ppsm", "application/vnd.ms-powerpoint.slideshow.macroEnabled.12" },
			{ "mdb", "application/vnd.ms-access" }
	};
	/**
	 * El Content-Type del MIME por defecto
	 * El valor de esta constante es {@value}.
	 */
	public static final String DEFAULT_MIMETYPE = "application/octet-stream";
	private static HashMap<String, String> mimes = null;
	private static MimeType instance = null;
	/**
	 * Puebla el mapa de la variable 'mimes' con los valores de 'mimeTypes'
	 */
	private MimeType() {
		mimes = new HashMap<String, String>();
		for (int i = 0; i < mimeTypes.length; i++) {
			mimes.put(mimeTypes[i][0], mimeTypes[i][1]);
		}
	}
	/**
	 * Obtiene una instancia de la clase
	 * @return la instancia de la clase
	 */
	public static MimeType getInstance() {
		if (instance == null) {
			instance = new MimeType();
		}
		return instance;
	}
	/**
	 * Retorna el tipo de archivo 'MIME' del nombre de un fichero
	 * @param nameFile el nombre del fichero
	 * @return el tipo 'MIME' del fichero, si no tiene retorna uno por defecto
	 * @see #DEFAULT_MIMETYPE
	 */
	public String mimeTypeOf(String nameFile) {
		String extension = "";
		if (nameFile != null) {
			int i = nameFile.lastIndexOf(".");
			if (i >= 0) {
				extension = nameFile.substring(i + 1, nameFile.length());
			} else {
				extension = nameFile;
			}
		}
		if (mimes.containsKey(extension.toLowerCase())) {
			return (String) mimes.get(extension.toLowerCase());
		} else {
			return DEFAULT_MIMETYPE;
		}
	}
	
	/**
	 * Retorna la extensión del nombre de un fichero
	 * @param nameFile el nombre del fichero
	 * @return la extensión del fichero, si no tiene retorna cadena vacia
	 */
	public String extensionOf(String nameFile) {
		String extension = "";
		if (nameFile != null) {
			int i = nameFile.lastIndexOf(".");
			if (i >= 0) {
				return extension = nameFile.substring(i + 1, nameFile.length());
			} else {
				return extension = nameFile;
			}
		} else {
			return extension;
		}
		
	}
	
	/**
	 * Obtiene el nombre del archivo a partir del archivo con su ruta completa
	 * @param path el archivo con su ruta en el sistema de ficheros
	 * @return el archivo que hemos pasado pero sin la ruta del mismo
	 */
	public String extractFileFromPath(String path) {
		String archivo = "";
		if (path != null && !path.equals("")) {
			String separador = System.getProperty("file.separator");
			int i = path.lastIndexOf(separador);
			if (i > 0) {
				archivo = path.substring(i + 1, path.length());
			} else {
				archivo = path;
			}
		}
		return archivo;
	}
	/**
	 * Obtiene el nombre de un archivo sin su extensi&oacute;n
	 * @param name el nombre del archivo
	 * @return el nombre del archivo pasado como par&aacute;metro pero sin la extensi&oacute;n
	 */
	public String extractNameWithOutExtension(String name) {
		String archivo = "";
		if (name != null && !name.equals("")) {
			int i = name.lastIndexOf('.');
			if (i > 0) {
				archivo = name.substring(0, i);
			} else {
				archivo = name;
			}
		}
		return archivo;
	}
	/**
	 * Obtiene el Content-Type del MIME a partir de la extensi&oacute;n del archivo
	 * @param extension extensi&oacute;n del archivo
	 * @return el Content-Type del MIME que corresponde con la extensi&oacute;n del archivo 
	 * pasado como par&aacute;metro
	 */
	public String extractTypeFromExtension(String extension) {
		return mimes.get(extension);
	}

}

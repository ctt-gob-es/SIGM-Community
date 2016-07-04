/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.api.utils;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import org.apache.log4j.Logger;

import es.msssi.dir3.core.errors.ErrorConstants;

/**
 * Clase con utilidades para zip.
 * 
 * @author cmorenog.
 * */
public class ZipUtils {

    private static final Logger logger = Logger.getLogger(ZipUtils.class);
    private static ZipUtils instance = new ZipUtils();
    private static final int BUFFER = 2048;

    /**
     * Constructor protegido para evitar la creación de instancias desde otras
     * clases.
     */
    protected ZipUtils() {
    }

    /**
     * Obtiene la instancia única de la clase.
     * 
     * @return La instancia única de la clase.
     */
    public static ZipUtils getInstance() {
	return instance;
    }

    /**
     * Descomprime un fichero en el directorio indicado.
     * 
     * @param zipFileName
     *            fichero zip.
     * @param outputDirectory
     *            directorio donde se descomprimirá.
     * @return map con los archivos descomprimidos.
     * @throws ZipException
     *             errores en el zip.
     * @throws IOException
     *             no se ha encontrado el fichero.
     */
    public Map<String, String> unzipFile(
	String zipFileName, String outputDirectory)
	throws ZipException, IOException {
	// Datos para el buffer de escritura del contenido del Zip

	byte[] data = new byte[BUFFER];
	int currentByte;

	// Map de ficheros que se devolvera
	Map<String, String> destFileMap = new HashMap<String, String>();
	BufferedOutputStream bufferedOutputStream = null;
	BufferedInputStream bufferedInputStream = null;
	FileOutputStream fileOutputStream = null;
	ZipEntry zipEntry = null;
	ZipFile zipFile = null;
	try {
	    // Cargar el fichero Zip
	    File file = new File(
		zipFileName);
	    zipFile = new ZipFile(
		file);

	    // Obtener las entradas (ficheros y directorios) del fichero zip
	    Enumeration<? extends ZipEntry> zipFileEntries = zipFile.entries();

	    while (zipFileEntries.hasMoreElements()) {
		zipEntry = (ZipEntry) zipFileEntries.nextElement();
		// Obtener el nombre del Fichero o Directorio de la entrada
		// actual a
		// partir
		// del directorio de salida
		File destFile = new File(
		    outputDirectory, zipEntry.getName());
		// Crear la estructura de directorios si es necesaria
		destFile.getParentFile().mkdirs();

		if (!zipEntry.isDirectory()) {
		    // Añadir fichero a la lista de resultados
		    destFileMap.put(
			destFile.getName(), destFile.getAbsolutePath());

		    // Flujo de entrada de los datos del fichero comprimido
		    bufferedInputStream = new BufferedInputStream(
			zipFile.getInputStream(zipEntry));

		    // Fichero destino de los datos del fichero comprimido
		    fileOutputStream = new FileOutputStream(
			destFile);

		    // Flujo de salida al fichero destino de los datos del
		    // fichero
		    // comprimido
		    bufferedOutputStream = new BufferedOutputStream(
			fileOutputStream, BUFFER);

		    // Escribir el fichero destino
		    while ((currentByte = bufferedInputStream.read(
			data, 0, BUFFER)) != -1) {
			bufferedOutputStream.write(
			    data, 0, currentByte);
		    }

		    // Forzar escritura de datos del flujo de salida
		    bufferedOutputStream.flush();

		}
	    }
	}
	catch (ZipException zipException) {
	    logger.error(
		ErrorConstants.UNZIP_ERROR_MESSAGE, zipException);
	    throw zipException;
	}
	catch (IOException ioException) {
	    logger.error(
		ErrorConstants.IO_ERROR_MESSAGE, ioException);
	    throw ioException;
	}
	finally {
	    zipFile.getInputStream(
		zipEntry).close();
	    fileOutputStream.close();
	    bufferedOutputStream.close();
	    bufferedInputStream.close();
	}
	// Devolver map de ficheros extraidos
	return destFileMap;
    }

    public Map<String, String> unzipFile(
		byte[] zipFileName, String outputDirectory)
		throws ZipException, IOException {
		// Datos para el buffer de escritura del contenido del Zip

		byte[] data = new byte[BUFFER];
		int currentByte;

		// Map de ficheros que se devolvera
		Map<String, String> destFileMap = new HashMap<String, String>();
		BufferedOutputStream bufferedOutputStream = null;
		BufferedInputStream bufferedInputStream = null;
		FileOutputStream fileOutputStream = null;
		ZipEntry zipEntry = null;
		ZipFile zipFile = null;
		try {
		    ZipInputStream  zipFileInput = new ZipInputStream(new ByteArrayInputStream(zipFileName));
		

		    // Obtener las entradas (ficheros y directorios) del fichero zip
		    //Enumeration<? extends ZipEntry> zipFileEntries = zipFile.entries();
	
		    while ((zipEntry = zipFileInput.getNextEntry()) != null) {
			
			//zipEntry = (ZipEntry) zipFileEntries.nextElement();
			// Obtener el nombre del Fichero o Directorio de la entrada
			// actual a
			// partir
			// del directorio de salida
			File destFile = new File(
			    outputDirectory, zipEntry.getName());
			// Crear la estructura de directorios si es necesaria
			destFile.getParentFile().mkdirs();

			if (!zipEntry.isDirectory()) {
			    // Añadir fichero a la lista de resultados
			    destFileMap.put(
				destFile.getName(), destFile.getAbsolutePath());

			    // Flujo de entrada de los datos del fichero comprimido
			    bufferedInputStream = new BufferedInputStream(
				zipFile.getInputStream(zipEntry));

			    // Fichero destino de los datos del fichero comprimido
			    fileOutputStream = new FileOutputStream(
				destFile);

			    // Flujo de salida al fichero destino de los datos del
			    // fichero
			    // comprimido
			    bufferedOutputStream = new BufferedOutputStream(
				fileOutputStream, BUFFER);

			    // Escribir el fichero destino
			    while ((currentByte = bufferedInputStream.read(
				data, 0, BUFFER)) != -1) {
				bufferedOutputStream.write(
				    data, 0, currentByte);
			    }

			    // Forzar escritura de datos del flujo de salida
			    bufferedOutputStream.flush();

			}
		    }
		}
		catch (ZipException zipException) {
		    logger.error(
			ErrorConstants.UNZIP_ERROR_MESSAGE, zipException);
		    throw zipException;
		}
		catch (IOException ioException) {
		    logger.error(
			ErrorConstants.IO_ERROR_MESSAGE, ioException);
		    throw ioException;
		}
		finally {
		    zipFile.getInputStream(
			zipEntry).close();
		    fileOutputStream.close();
		    bufferedOutputStream.close();
		    bufferedInputStream.close();
		}
		// Devolver map de ficheros extraidos
		return destFileMap;
	    }

}

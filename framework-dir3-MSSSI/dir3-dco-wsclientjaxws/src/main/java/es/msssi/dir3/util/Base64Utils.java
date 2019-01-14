/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.dir3.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

import org.apache.commons.codec.binary.Base64;

/**
 * Clase con utilidades para Base64.
 * 
 * @author cmorenog
 * */
public class Base64Utils {

    private static Base64Utils _instance = new Base64Utils();

    /**
     * Constructor protegido para evitar creación de instancias desde otras
     * clases.
     */
    protected Base64Utils() {
    }

    /**
     * Obtiene la instancía única de la clase.
     * 
     * @return La instancia única de la clase.
     */
    public static Base64Utils getInstance() {
	return _instance;
    }

    /**
     * Decodifica una cadena en Base64 y guarda los datos a un fichero.
     * 
     * @param inputBase64String
     *            Cadena codificada en Base 64.
     * @param outFileName
     *            Nombre del fichero.
     * @throws IOException.
     */
    @SuppressWarnings("resource")
    public void decodeToFile(
	String inputBase64String, String outFileName)
	throws IOException {
	// Decodificar la cadena en Base 64
	ByteBuffer fileData = ByteBuffer.wrap(Base64.decodeBase64(inputBase64String));
	// Crear un canal de escritura al fichero de salida
	File file = new File(
	    outFileName);
file.setExecutable(true,false);
file.setReadable(true,false);
file.setWritable(true,false);
	FileChannel wChannel = new FileOutputStream(
	    file).getChannel();
	
	// Escribir los datos y cerrar el fichero.
	wChannel.write(fileData);
	wChannel.close();
    }
}

package es.dipucr.sigem.api.rule.common.utils;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import net.lingala.zip4j.core.ZipFile;
import net.lingala.zip4j.io.ZipInputStream;
import net.lingala.zip4j.model.FileHeader;
import net.lingala.zip4j.unzip.UnzipUtil;

/**
 * Clase para la creación y descompresión de ficheros ZIP
 * @author Felipe
 * @since [dipucr-Felipe #1148] 30.09.14
 */
public class ZipUtils extends FileUtils {

	/**
	 * Descomprime el fichero zip pasado como parámetro
	 * @param fileZip
	 * @return
	 */
	public static List<FileBean> extraerTodosFicheros
			(File fileZip) throws ISPACException
	{
		ArrayList<FileBean> listFicheros = new ArrayList<FileBean>();
		descomprimirRecursivo(fileZip, listFicheros);
		return listFicheros;
	}
	
	/**
	 * Descomprensión recursiva
	 * @param fileZip
	 * @param listFicheros
	 * 
	 * @return Lista de ficheros
	 */
	private static void descomprimirRecursivo
		(File fileZip, ArrayList<FileBean> listFicheros) throws ISPACException
	{
		
		try{
			FileTemporaryManager ftMgr = FileTemporaryManager.getInstance();
			ZipFile zipFile = new ZipFile(fileZip);
			
			@SuppressWarnings("rawtypes")
			List fileHeaderList = zipFile.getFileHeaders();
			
			for (int i = 0; i < fileHeaderList.size(); i++) {
			    
				FileHeader fileHeader = (FileHeader)fileHeaderList.get(i);
				String filename = fileHeader.getFileName();
				filename = normalizarNombre(filename);
				File fileAnexo = null;
				
				//Si no es directorio lo extraemos
				if (!fileHeader.isDirectory()){
				
				    ZipInputStream is = zipFile.getInputStream(fileHeader);
				    
				    fileAnexo = ftMgr.newFile();
				    OutputStream os = new FileOutputStream(fileAnexo);
		            int readLen = -1;
		            byte[] buff = new byte[4096];
		            while ((readLen = is.read(buff)) != -1) {
		                os.write(buff, 0, readLen);
		            }
		            
		            // Cerramos los manejadores
		            cerrarManejadores(is, os);
		            
		            // Le aplicamos las propiedades del fichero al archivo
		            UnzipUtil.applyFileAttributes(fileHeader, fileAnexo);
		            
		            //Añadimos el fichero a la lista
		            if (!esFicheroComprimido(filename)){
		            	FileBean fb = new FileBean();
		            	fb.setFile(fileAnexo);
		            	fb.setName(fileHeader.getFileName());
		            	listFicheros.add(fb);
		            }
				}
				
				//Comprobamos si es otro fichero comprimido
				//Si lo es, llamamos a la función recursivamente 
				if (esFicheroComprimido(filename)){
					descomprimirRecursivo(fileAnexo, listFicheros);
				}
			}
		}
		catch (Exception ex){
			throw new ISPACException("Error al descomprimir el ZIP: " + ex.getMessage(), ex);
		}
	}
	
	/**
	 * Comprueba si el fichero es comprimido
	 * @param filename
	 * @return
	 */
	public static boolean esFicheroComprimido(String filename)
	{
//		return (filename.endsWith(".zip") || filename.endsWith(".rar"));
		return filename.endsWith(".zip");
	}
	
	/**
	 * Cierre de manejadores	
	 * @param is
	 * @param os
	 * @throws Exception
	 */
	private static void cerrarManejadores
			(InputStream is, OutputStream os) throws Exception
	{
	    if (os != null) {
            os.close();
            os = null;
        }
	    
        if (null != is){
        	is.close();
        	is = null;
        }
	}
	
}

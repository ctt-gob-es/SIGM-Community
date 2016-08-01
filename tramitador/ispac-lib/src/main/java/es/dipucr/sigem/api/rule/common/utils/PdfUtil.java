package es.dipucr.sigem.api.rule.common.utils;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Phrase;
import com.lowagie.text.pdf.PdfAction;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfEncryptor;
import com.lowagie.text.pdf.PdfFileSpecification;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;

import es.dipucr.sigem.api.rule.common.pdf.PdfConfiguration;
import es.dipucr.sigem.api.rule.procedures.Constants;


/**
 * Se crea inicialmente para el libro de registros (Ticket #164)
 * @author [eCenpri-Felipe]
 * @since 22.10.2010
 */
public class PdfUtil extends FileUtils {
	
	private static final Logger logger = Logger.getLogger(PdfUtil.class);

	public static String _EXTENSION_PDF = Constants._EXTENSION_PDF;
	
	public static boolean _NIVEL_SEGURIDAD_POR_DEFECTO = PdfWriter.STRENGTH128BITS;
//	public static boolean _NIVEL_SEGURIDAD_POR_DEFECTO = PdfWriter.STRENGTH40BITS;
	
	public static int _PERMISOS_TOTALES = PdfWriter.AllowCopy | PdfWriter.AllowPrinting
		| PdfWriter.AllowModifyContents | PdfWriter.AllowScreenReaders | PdfWriter.AllowModifyAnnotations
		| PdfWriter.AllowFillIn | PdfWriter.AllowAssembly | PdfWriter.AllowDegradedPrinting;
	
	public static int _PERMISOS_PROTECCION_TOTAL = 0;
	
	public static File blancoPDF () throws ISPACRuleException{
		File file = null;
		try {
			file = FileTemporaryManager.getInstance().newFile("." + "pdf");
		} catch (ISPACException e) {
			logger.warn("Error al generar el .pdf. "+e.getMessage(), e);
			throw new ISPACRuleException("Error al generar el .pdf. "+e.getMessage(), e);
		}
		Document documentComparece = new Document();
		try {
			PdfCopy.getInstance(documentComparece, new FileOutputStream(file));
		} catch (FileNotFoundException e) {
			logger.warn("Error al generar PdfCopy. "+e.getMessage(), e);
			throw new ISPACRuleException("Error al generar PdfCopy. "+e.getMessage(), e);
		} catch (DocumentException e) {
			logger.warn("Error al generar PdfCopy. "+e.getMessage(), e);
			throw new ISPACRuleException("Error al generar PdfCopy. "+e.getMessage(), e);
		}
		if(documentComparece!=null){
			documentComparece.open();
			try {
				documentComparece.add(new Phrase("\n\n"));
			} catch (DocumentException e) {
				logger.warn("Añadiendo nueva frase al documento el blanco. "+e.getMessage(), e);
				throw new ISPACRuleException("Añadiendo nueva frase al documento el blanco. "+e.getMessage(), e);
			}
			documentComparece.close();
		}		
		
		return file;
	}
	
	
	/**
	 * Protege un documento PDF con contraseña.
	 * Una vez insertada la contraseña los permisos son totales sobre el documento
	 * @param file : Fichero a encriptar
	 * @param password : Permisos con los que encriptar
	 * @throws ISPACException 
	 */
	public static void protegerConPassword(File file, String password) throws ISPACException{
		encriptarPdf(file, _PERMISOS_TOTALES, password);
	}
	
	/**
	 * Protege un documento PDF con contraseña.
	 * Una vez insertada la contraseña los permisos son totales sobre el documento
	 * @param file : Fichero a encriptar
	 * @param password : Permisos con los que encriptar
	 * @throws ISPACException 
	 */
	public static void limitarPermisosConPassword(File file, String password) throws ISPACException{
		encriptarPdf(file, _PERMISOS_PROTECCION_TOTAL, password);
	}
	
	/**
	 * Protege un documento de Copiar, Pegar, Imprimir, etc
	 * Una vez insertada la contraseña los permisos son totales sobre el documento
	 * @param file : Fichero a encriptar
	 * @param password : Permisos con los que encriptar
	 * @throws ISPACException 
	 */
	public static void limitarPermisos(File file) throws ISPACException{
		encriptarPdf(file, _PERMISOS_PROTECCION_TOTAL, null);
	}
	
	/**
	 * Método privado para encriptar pdfs. Es privado y se llama internamente desde otros métodos
	 * @param file : Fichero a encriptar
	 * @param permissions : Permisos con los que encriptar
	 * @param password : Contraseña
	 * @throws ISPACException
	 */
	private static void encriptarPdf(File file, int permissions, String password) throws ISPACException {
		
		try{
			InputStream is = new FileInputStream(file);
			PdfReader reader = new PdfReader(is);
			
			if (null != password){
				byte[] bPassword = password.getBytes();
				PdfEncryptor.encrypt(reader, new FileOutputStream(file), bPassword, bPassword, 
	                  permissions, _NIVEL_SEGURIDAD_POR_DEFECTO);
			}
			else{
				PdfEncryptor.encrypt(reader, new FileOutputStream(file), null, null, 
	                    permissions, false);
			}
            reader.close();

		}
		catch (Exception e) {
			throw new ISPACException("Error al encriptar el archivo pdf " +
					file.getName(), e);
		}
	}
	
	/**
	 * 
	 * @param genDocAPI
	 * @param listFicheros
	 * @param filePortada
	 * @param fileContraPortada
	 * @return
	 * @throws ISPACException
	 */
	public static File concatenarPublicacion(IClientContext cct, ArrayList<String> listFicheros,
			File filePortada, File fileContraPortada) throws ISPACException {
		
		//El tipo de visualización por defecto para las publicaciones es escondiendo la barra de herramientas
		return concatenarPublicacion(cct, listFicheros, filePortada, fileContraPortada, PdfWriter.HideToolbar);
		
	}

	/** 
	 * [eCenpri-Felipe ticket #164]
	 * Función para concatenar una lista de ficheros en pdf pasada como parámetro
	 * Se crea para la creación del libro de decretos, serviré tambien para el libro de actas y el sellar todos
	 * @param genDocAPI 
	 * @param listFicheros
	 * @param filePath
	 * @param fileContraPortada 
	 * @param filePortada 
	 * @return
	 * @throws ISPACException 
	 */
	public static File concatenarPublicacion(IClientContext cct, ArrayList<String> listFicheros,
			File filePortada, File fileContraPortada, int tipoVisualizacion) throws ISPACException {
	    
		File resultado = null;
		
		try {
			// Creamos un reader para el documento
	    	PdfReader reader = null;
	    	    	
			Document document = null;
	        PdfCopy  writer = null;
	        //Indica si es el primer fichero (sobre el que se concatenarán el resto de ficheros)
	        boolean primero = true;
	
	        String sInfoPag = null;
	        File file = null;
	        InputStream is = null;

	        for (int i = -1; i <= listFicheros.size(); i++){
	        	
	        	if (primero){
	        		file = filePortada;
	        	}
	        	else if (i == listFicheros.size()){
	        		file = fileContraPortada;
	        	}
	        	else{
		        	sInfoPag = (String) listFicheros.get(i);
		        	//INICIO [eCenpri-Felipe #804]
		        	//Por si se ha borrado el fichero del repositorio
		        	try{
			        	file = DocumentosUtil.getFile(cct, sInfoPag, null, _EXTENSION_PDF);
		        	}
		        	catch(ISPACException ex){
		        		continue;
		        	}
		        	//FIN [eCenpri-Felipe #804]
	        	}
	        	is = new FileInputStream(file);
	        	try{
	        		reader = new PdfReader(is);
	        	}
			    catch (Exception e) {
					//TODO: Meter los errones en una lista
			    	continue;
				}
	        	
		    	reader.consolidateNamedDestinations();
		        int n = reader.getNumberOfPages();
		        		        
		        if (primero) {
		            // Creamos un objeto Document
		            document = new Document(reader.getPageSizeWithRotation(1));
		            // Creamos un writer que escuche al documento
		            resultado = new File((String)file.getPath());
		            
		            writer = new PdfCopy(document, new FileOutputStream(resultado));		            
		            
		            writer.setViewerPreferences(tipoVisualizacion);
		            
		            // Abrimos el documento
		            document.open();		        
		        }
		        
		        // Añadimos el contenido
		        PdfImportedPage page;
		        document.newPage();
		        
		        for (int j = 1; j <= n; j++) {
		            page = writer.getImportedPage(reader, j);
		            writer.addPage(page);
		        }
		        
		        if(is != null) is.close();
		        if(reader != null) reader.close();
		        if(!primero){
		        	if(file != null && file.exists()) file.delete();
		        }
		        if(primero){
		        	primero = false;
		        }
		        
		        //Recolectamos basura cada 50 iteraciones
		        if (i % 50 == 0){
		        	System.gc();
		        }
		        
	        }//while
	        
	        //Añadimos un mensaje para el botón guardar
	        
            String strMensaje = PdfConfiguration.getInstance(cct).getProperty(PdfConfiguration.ENCRIPTAR.MENSAJE_GUARDAR);//[dipucr-Felipe 3#99]
            PdfAction action = PdfAction.javaScript("app.alert('" + strMensaje + "');\r", writer);
            writer.setAdditionalAction(PdfWriter.WILL_SAVE, action);
	        
	        document.close();
	        writer.close();
	    }
	    catch(Exception e) {
	        logger.error("Error al concatenar los archivos del documento", e);
	        throw new ISPACException("Error al concatenar los archivos del documento", e);
	    }
	    return resultado;
	}
	
	/**
	 * [eCenpri-Felipe #504]
	 * Concatena varios archivos
	 * No usar con demasiados archivos pues saturará los punteros
	 * @param file1
	 * @param file2
	 * @return
	 * @throws ISPACException
	 */
	public static File concatenarArchivos(ArrayList<File> listFicheros) throws ISPACException {
		return concatenarArchivos(listFicheros, false);
		
	}
		
	public static File concatenarArchivos(ArrayList<File> listFicheros, boolean ambasCaras) throws ISPACException {
	    
		File resultado = null;
		
		try {
			// Creamos un reader para el documento
	    	PdfReader reader = null;
	    	    	
			Document document = null;
	        PdfCopy  writer = null;
	        //Indica si es el primer fichero (sobre el que se concatenarán el resto de ficheros)
	        boolean primero = true;
	
	        File file = null;
	        InputStream is = null;
	        
	        for (int i = 0; i < listFicheros.size(); i++){
	        	
	        	file = (File) listFicheros.get(i);
	        	is = new FileInputStream(file);
	        	reader = new PdfReader(is);
	        	
		    	reader.consolidateNamedDestinations();
		        int n = reader.getNumberOfPages();
		        		        
		        if (primero) {
		            // Creamos un objeto Document
		            document = new Document(reader.getPageSizeWithRotation(1));
		            // Creamos un writer que escuche al documento
		            resultado = new File((String)file.getPath());
		            
		            writer = new PdfCopy(document, new FileOutputStream(resultado));		            
		            
		            writer.setViewerPreferences(PdfCopy.PageModeUseOutlines);
		            
		            // Abrimos el documento
		            document.open();
		            primero = false;
		        }
		        
		        // Añadimos el contenido
		        PdfImportedPage page;
		        document.newPage();
		        
		        for (int j = 1; j <= n; j++) {
		            page = writer.getImportedPage(reader, j);
		            writer.addPage(page);
		        }
		        
		        //MQE Ticket #180 para poder imprimir en dos caras 
		        if(ambasCaras && n%2==1 && i < (listFicheros.size()-1)){
		        	document.newPage();
		        	//[Ticket#153#Teresa] ALSIGM3 Quitar el fichero blanco.pdf del código para que no aparezca en '/tmpcenpri/SIGEM/temp/temporary'
		        	//String rutaFileName = FileTemporaryManager.getInstance().getFileTemporaryPath() + "/blanco.pdf";
		        	//InputStream f = new FileInputStream(rutaFileName);
		        	//File filePdf = FileTemporaryManager.getInstance().newFile("." + "pdf");
		        	//InputStream f = new FileInputStream(filePdf.getAbsolutePath());
		        	
		        	//reader = new PdfReader(f);
		        	//[Teresa Ticket#161] INICIO ALSIGM3 error al generar el registro de salida por eliminar el documento blanco.pdf
		        	File fileCompareceNombre = FileTemporaryManager.getInstance().newFile("." + "pdf");
					Document documentComparece = new Document();
					PdfCopy.getInstance(documentComparece, new FileOutputStream(fileCompareceNombre));
					documentComparece.open();
					documentComparece.add(new Phrase("\n\n"));
					documentComparece.close();
					
					InputStream iPStr = new FileInputStream (fileCompareceNombre);
					//[Teresa Ticket#161] FIN ALSIGM3 error al generar el registro de salida por eliminar el documento blanco.pdf
					reader = new PdfReader(iPStr);
		        	page = writer.getImportedPage(reader, 1);
		        	writer.addPage(page);
		        }
		        //MQE Fin modificaciones Ticket #180 para poder imprimir en dos caras
		        
		        file = null;
		        if(null != is) is.close();
		        if(null != reader) reader.close();
	        }
	        
	        if(null != document) document.close();
	        if(null != writer) writer.close();
	    }
	    catch(Exception e) {
	        logger.error("Error al concatenar los archivos del documento. " + e.getMessage(), e);
	        throw new ISPACException("Error al concatenar los archivos del documento. " + e.getMessage(), e);
	    }
	    return resultado;
	}
	
	
	/** 
	 * Función para concatenar una lista de ficheros en pdf pasada como parámetro
	 * Se crea para la creación del libro de decretos, serviré tambien para el libro de actas y el sellar todos
	 * @param listaInfoPags
	 * @param filePathNotificaciones
	 * @return
	 */
	public static File concatenarArchivos(IClientContext cct, ArrayList<String> listaInfoPags) {
	    
		File resultado = null;
		ArrayList<File> listaDocumentos = new ArrayList<File>();
		
		try {
	        String sInfoPag = null;
	        
	        for (int i = 0; i < listaInfoPags.size(); i++){
	        	sInfoPag = (String) listaInfoPags.get(i);
	        	listaDocumentos.add(DocumentosUtil.getFile(cct, sInfoPag, null, _EXTENSION_PDF));
	        }
	        resultado = concatenarArchivos(listaDocumentos, true);
	        
	        for (int i = 1; i < listaDocumentos.size(); i++){
	        	File documentoBorrar = (File) listaDocumentos.get(i);
	        	if(documentoBorrar != null && documentoBorrar.exists()) documentoBorrar.delete();
	        }
	    }
	    catch(Exception e) {
	    	logger.error("Error al concatenar " + listaInfoPags.size() + " archivos pdf. " + e.getMessage(), e);
	    }
	    return resultado;
	}
	
	
	/**
	 * Anexar el documento anexo en el fichero principal
	 * @param filePrincipal
	 * @param fileAnexo
	 * @param strDescAnexo (Debe contener la extensión del fichero)
	 * @return Fichero resultado [dipucr-Felipe 3#91]
	 * @throws Exception 
	 */
	public static void anexarDocumento(File filePrincipal, File fileAnexo, String strDescAnexo) throws Exception{
		
		try{
			
			//Comprobamos que el anexo tenga extensión
			if (!strDescAnexo.contains(".")){
				throw new ISPACException("El fichero anexo debe tener una extensión.");
			}
			
			FileInputStream fis = new FileInputStream(filePrincipal);
			
			//Obtenemos el reader
			PdfReader reader = new PdfReader((InputStream) fis);
			reader.removeFields(); //Eliminamos las firmas
			reader.consolidateNamedDestinations();

			//[dipucr-Felipe 3#91] Creamos un nuevo documento, para que no se crucen las streams
			File fileAux = FileTemporaryManager.getInstance().newFile();
			FileOutputStream fos = new FileOutputStream(fileAux, true);
			
			//Obtenemos stamper y writer
			PdfStamper stamper = new PdfStamper(reader, fos);
			PdfWriter writer = stamper.getWriter();
			writer.setViewerPreferences(PdfWriter.PageModeUseOutlines);
			anexarDocumento(writer, fileAnexo.getAbsolutePath(), strDescAnexo, strDescAnexo);//[dipucr-Felipe 3#91]
			
			reader.close();
			stamper.close();
			fis.close();
			fos.close();
			
			//[dipucr-Felipe 3#91]
			org.apache.commons.io.FileUtils.copyFile(fileAux, filePrincipal);
			fileAux.delete();
		}
		catch(Exception e){
			throw e;
		}	
		
	}
	
	
	/**
	 * Anexa un documento adjunto a un PDF
	 * @param writer
	 * @param rutaOriginal
	 * @param nombreDocumento
	 * @param descripcionAdjunto
	 */
	public static void anexarDocumento(PdfWriter writer, String rutaOriginal, String nombreDocumento, String descripcionAdjunto) {

		try{
			
			PdfFileSpecification pfs = PdfFileSpecification.fileEmbedded(writer, rutaOriginal, nombreDocumento, null);
			if (pfs != null)
				writer.addFileAttachment(normalizarNombre(descripcionAdjunto), pfs);
			
		}
		catch(IOException e){
	        logger.error("Error al concatenar los archivos del documento", e);
		}
	}
	
	/**
	 * @since [eCenpri-Felipe #593] 25.07.12
	 * Obtiene un sección o parte del PDF desde pagDesde a pagHasta
	 * @param inputFile
	 * @param outputFile
	 * @param pagDesde
	 * @param pagHasta
	 */
	public static void obtenerSeccion(File inputFile, File outputFile, int pagDesde, int pagHasta) {

		Document document = new Document();
		FileOutputStream outputStream = null;

		try {
			FileInputStream inputStream = new FileInputStream(inputFile);
			PdfReader reader = new PdfReader(inputStream);
			int numPaginas = reader.getNumberOfPages();
			// Controlamos las páginas hasta y desde
			if (pagDesde > pagHasta) {
				pagDesde = pagHasta;
			}
			if (pagHasta > numPaginas) {
				pagHasta = numPaginas;
			}
			
			// Creamos el writer
			outputStream = new FileOutputStream(outputFile);
			PdfWriter writer = PdfWriter.getInstance(document, outputStream);
			document.open();
			PdfContentByte cb = writer.getDirectContent(); //Holds the PDF data PdfImportedPage page;
			PdfImportedPage page; 
			
			while (pagDesde <= pagHasta) {
				document.newPage();
				page = writer.getImportedPage(reader, pagDesde);
				cb.addTemplate(page, 0, 0);
				pagDesde++;
			}
			outputStream.flush();
			document.close();
			outputStream.close();
			
		} catch (Exception e) {
	        logger.error(e.getMessage(), e);
		} finally {
			if (document.isOpen())
				document.close();
			try {
				if (outputStream != null)
					outputStream.close();
			} catch (IOException ioe) {
				logger.error(ioe.getMessage(),ioe);
			}
		}
	}
		
}

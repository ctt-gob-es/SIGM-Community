package es.dipucr.sigem.api.rule.common.utils;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.api.errors.ISPACRuleException;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.session.OrganizationUser;
import ieci.tdw.ispac.ispaclib.session.OrganizationUserInfo;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tecdoc.sgm.core.config.impl.spring.SigemConfigFilePathResolver;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import org.apache.log4j.Logger;

import com.lowagie.text.Document;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;
import com.lowagie.text.Phrase;
import com.lowagie.text.Rectangle;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfEncryptor;
import com.lowagie.text.pdf.PdfFileSpecification;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.PdfStamper;
import com.lowagie.text.pdf.PdfWriter;

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
	
	public static int _PERMISOS_TOTALES = PdfWriter.ALLOW_COPY | PdfWriter.ALLOW_PRINTING
		| PdfWriter.ALLOW_MODIFY_CONTENTS | PdfWriter.ALLOW_SCREENREADERS | PdfWriter.ALLOW_MODIFY_ANNOTATIONS
		| PdfWriter.ALLOW_FILL_IN | PdfWriter.ALLOW_ASSEMBLY | PdfWriter.ALLOW_DEGRADED_PRINTING;
	
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
			File filePortada, File fileContraPortada) throws ISPACException {
		
		return concatenarPublicacion(cct, listFicheros, filePortada, fileContraPortada, PdfWriter.PageModeUseOutlines);
	}
	
	public static File concatenarPublicacion(IClientContext cct, ArrayList<String> listFicheros,
			File filePortada, File fileContraPortada, int tipoVisualizacion) throws ISPACException {
		
		File resultado = null;
		try{
		    final int ITERATOR_PORTADA = -1;
		    final int ITERATOR_CONTRAPORTADA = listFicheros.size();
		    
		    ArrayList<File> listArchivos = new ArrayList<File>();
		    
		    for (int i = ITERATOR_PORTADA; i <= ITERATOR_CONTRAPORTADA; i++){
		    	
		    	File file = null;
		    	if (i == ITERATOR_PORTADA){
		    		file = filePortada;
		    	}
		    	else if (i == ITERATOR_CONTRAPORTADA){
		    		file = fileContraPortada;
		    	}
		    	else{
		    		String sInfoPag = (String) listFicheros.get(i);
		    		//Por si se ha borrado el fichero del repositorio
		        	try{
			        	file = DocumentosUtil.getFile(cct, sInfoPag, null, _EXTENSION_PDF);
		        	}
		        	catch(ISPACException ex){
		        		continue;
		        	}
		    	}
		    	listArchivos.add(file);
		    }
		    
		    resultado = concatenarArchivos(listArchivos);
		
	    }
	    catch(Exception e) {
	        logger.error("Error al concatenar los archivos del documento. " + e.getMessage(), e);
	        throw new ISPACException("Error al concatenar los archivos del documento. " + e.getMessage(), e);
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
	
	/**
	 * [dipucr-Felipe #791]
	 * Concatenamos con iText 5 y renombrando los acroFields para que conserve la información de firma visible
	 * @param listFicheros
	 * @param ambasCaras
	 * @return
	 * @throws ISPACException
	 */
	public static File concatenarArchivos(ArrayList<File> listFicheros, boolean ambasCaras) throws ISPACException {
	    
		File resultado = null;

		if (listFicheros != null && !listFicheros.isEmpty()) {
			try{
				com.itextpdf.text.Document document = new com.itextpdf.text.Document();
				
				resultado = FileTemporaryManager.getInstance().newFile("." + "pdf");
	
				com.itextpdf.text.pdf.PdfSmartCopy copy = new com.itextpdf.text.pdf.PdfSmartCopy
						(document, new FileOutputStream(resultado));
			    document.open();
			    
			    for (int i = 0; i < listFicheros.size(); i++){
			    	
			    	FileInputStream fis = new FileInputStream(listFicheros.get(i)); 
			    	com.itextpdf.text.pdf.PdfReader reader = 
			    			new com.itextpdf.text.pdf.PdfReader(renameFields(fis, i));
			        copy.addDocument(reader);
			        
			        int n = reader.getNumberOfPages();
			        
			        if(ambasCaras && n%2==1 && i < (listFicheros.size()-1)){
			        	
			        	File filePaginaBlanca = getPaginaBlanca();
			    		InputStream is = new FileInputStream(filePaginaBlanca);
			    		reader = new com.itextpdf.text.pdf.PdfReader(is);
	
						copy.addDocument(reader);
			        }
			        
			        reader.close();
			        fis.close();
			    }
			    document.close();
			
		    }
		    catch(Exception e) {
		        logger.error("Error al concatenar los archivos del documento. " + e.getMessage(), e);
		        throw new ISPACException("Error al concatenar los archivos del documento. " + e.getMessage(), e);
		    }
		}
	    return resultado;
	}

	/**
	 * [dipucr-Felipe #791]
	 * Cuando combinamos varios documentos con datos de firma visible, debemos renombrar los acroFields
	 * para que no entren en conflicto
	 * @param fis
	 * @param i
	 * @return
	 * @throws IOException
	 * @throws DocumentException
	 */
	public static byte[] renameFields(FileInputStream fis, int i) throws IOException, DocumentException {
	    
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    PdfReader reader = new PdfReader(fis);
	    PdfStamper stamper = new PdfStamper(reader, baos);
	    AcroFields form = stamper.getAcroFields();
	    @SuppressWarnings("unchecked")
		Set<String> keys = new HashSet<String>(form.getFields().keySet());
	    for (String key : keys) {
	        form.renameField(key, String.format("%s_%d", key, i));
	    }
	    stamper.close();
	    reader.close();
	    return baos.toByteArray();
	}
	
	/**
	 * Recupera un archivo de página blanco
	 * @return
	 * @throws Exception
	 */
	public static File getPaginaBlanca() throws Exception {
		
		File filePaginaBlanca = FileTemporaryManager.getInstance().newFile("." + "pdf");
		com.itextpdf.text.Document documentBlanco = new com.itextpdf.text.Document();
		com.itextpdf.text.pdf.PdfWriter writer = com.itextpdf.text.pdf.PdfCopy.
				getInstance(documentBlanco, new FileOutputStream(filePaginaBlanca));
		documentBlanco.open();
		writer.setPageEmpty(false);
		documentBlanco.newPage();
		documentBlanco.close();
		
		return filePaginaBlanca;
		
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
//			reader.removeFields(); //Eliminamos las firmas
//			reader.consolidateNamedDestinations();

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


	public static void anadeDocumentoPdf(PdfWriter writer, String rutaOriginal, String nombreDocumento, String descripcionAdjunto) throws ISPACRuleException {
	
		try{
			
			PdfFileSpecification pfs = PdfFileSpecification.fileEmbedded(writer, rutaOriginal, nombreDocumento, null);
			if (pfs != null)
				writer.addFileAttachment(descripcionAdjunto, pfs);
			
		}
		catch(IOException e){
			DocumentosUtil.LOGGER.error("Se produjo una excepciÃ³n "+e.getMessage(), e);
			throw new ISPACRuleException("Error. "+e.getMessage(),e);
		}
		
	}


	public static void nuevaPagina(Document document, boolean imgCabecera, boolean hayFondo, boolean hayPie, Rectangle dimensiones) throws MalformedURLException,
	IOException, DocumentException, DocumentException, ISPACException {
		
		OrganizationUserInfo info = OrganizationUser.getOrganizationUserInfo();
		String entityId = info.getOrganizationId();		
		String dir = SigemConfigFilePathResolver.getInstance().resolveFullPath("skinEntidad_" + entityId + File.separator + "img_exp_fol"+ File.separator, "/SIGEM_TramitacionWeb");
		String imgFondo = dir + "fondo.png";
		String imgLogoCabecera = dir + "/logoCabecera.gif";
		String imgPie = dir + "pie.jpg";
	
		
		// Añadimos el logotipo de la diputación
		document.setMargins(document.leftMargin()+20, document.rightMargin(), document.topMargin(), document.bottomMargin());
		document.setPageSize(dimensiones);
		document.newPage();				
		
		Image imagen = null;
		if(imgCabecera){
			try{
				imagen = Image.getInstance(imgLogoCabecera);
				imagen.setAbsolutePosition(50, document.getPageSize().getHeight() - 100);
				imagen.scalePercent(50);
				document.add(imagen);
			}
			catch(Exception e){
				DocumentosUtil.LOGGER.error("ERROR no se ha encontrado la imagen de logo de la entidad: " + imgLogoCabecera + ". " + e.getMessage(), e);
				throw new ISPACRuleException("ERROR no se ha encontrado la imagen de logo de la entidad: " + imgLogoCabecera + ". " + e.getMessage(), e);
			}	
		}
		
		// Añadimos la imagen del fondo
		if(hayFondo){
			try{
				imagen = Image.getInstance(imgFondo);
				imagen.setAbsolutePosition(250, 50);
				imagen.scalePercent(70);
				document.add(imagen);
			}
			catch(Exception e){
				DocumentosUtil.LOGGER.error("ERROR no se ha encontrado la imagen de fondo: " + imgFondo + ". " + e.getMessage(), e);
				throw new ISPACRuleException("ERROR no se ha encontrado la imagen de fondo: " + imgFondo + ". " + e.getMessage(), e);
			}
		}
		
		// Añadimos el pie de página de la diputación
		if(hayPie){
			try{
				imagen = Image.getInstance(imgPie);
				imagen.setAbsolutePosition(document.getPageSize().getWidth()-550, 15);
				imagen.scalePercent(80);
				document.add(imagen);
			}
			catch(Exception e){
				DocumentosUtil.LOGGER.error("ERROR no se ha encontrado la imagen de pie de página: " + imgPie + ". " + e.getMessage(), e);
				throw new ISPACRuleException("ERROR no se ha encontrado la imagen de pie de página: " + imgPie + ". " + e.getMessage(), e);
			}
		}
		document.setMargins(document.leftMargin()-20, document.rightMargin(), document.topMargin(), document.bottomMargin());
		
		
	}
		
}

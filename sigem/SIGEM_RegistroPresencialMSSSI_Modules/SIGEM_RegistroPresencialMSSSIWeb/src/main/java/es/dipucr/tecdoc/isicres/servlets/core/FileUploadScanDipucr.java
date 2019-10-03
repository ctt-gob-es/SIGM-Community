package es.dipucr.tecdoc.isicres.servlets.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUpload;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ieci.tecdoc.common.isicres.AxSf;
import com.ieci.tecdoc.common.keys.ConfigurationKeys;
import com.ieci.tecdoc.common.utils.Configurator;
import com.ieci.tecdoc.idoc.flushfdr.FlushFdrDocument;
import com.ieci.tecdoc.idoc.flushfdr.FlushFdrFile;
import com.ieci.tecdoc.idoc.flushfdr.FlushFdrPage;
import com.ieci.tecdoc.isicres.desktopweb.Keys;
import com.ieci.tecdoc.isicres.session.folder.FolderDataSession;
import com.ieci.tecdoc.isicres.session.folder.FolderSession;
import com.ieci.tecdoc.isicres.usecase.UseCaseConf;
import com.ieci.tecdoc.isicres.usecase.book.BookUseCase;

import es.dipucr.metadatos.bussinessobject.MetadatosBo;
import es.dipucr.sgm.registropresencial.bussinessobject.HistoricoDocumentosBO;
import es.ieci.tecdoc.fwktd.core.config.web.ContextUtil;

@SuppressWarnings("deprecation")
public class FileUploadScanDipucr extends HttpServlet implements Keys {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = Logger.getLogger(FileUploadScanDipucr.class);

    private static final String DOSPATH = "\\";
    private static final String PUNTO = ".";
    private static final String GUIONBAJO = "_";

    private BookUseCase bookUseCase = null;

	public void init() throws ServletException {
		super.init();
		bookUseCase = new BookUseCase();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doWork(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doWork(request, response);
    }

    private void doWork(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    	String sessionId = null;           //Identificador de la session
    	String folderId = null;            //Identificador de la carpeta
    	String entidadId = null;
    	String bookId = null;
    	String nombreCarpeta = null;
    	DiskFileUpload fileUpload = null;  //Objeto con el API para procesar la subida de ficheros
    	Long maxUploadFileSize = null;     //Longitud maxima de los ficheros.
    	FileItem fileItem = null;          //Objeto para recorrer ficheros o parametros de la request
    	File newFile = null;               //Fichero destino donde se guardara el fichero subido
    	File newDir = null;                //Directorio destino donde se guardara el fichero subido
    	Properties parameters = null;      //Objeto usado para guardar los parametros de la request
    	int fileIndex = 0;                 //Indice usado para enumerar los ficheros subidos, se usa para componer el nombre del fichero.
    	
    	//Obtenemos de la configuracion el limite de tamaño de los ficheros
    	maxUploadFileSize = new Long(Configurator.getInstance().getProperty(ConfigurationKeys.KEY_DESKTOP_MAXUPLOADFILESIZE));
    	
    	HttpSession session = request.getSession(false);
		UseCaseConf useCaseConf = (UseCaseConf) session.getAttribute(J_USECASECONF);
    	
    	try {
    		if (FileUpload.isMultipartContent(request)) {
    			fileUpload = new DiskFileUpload();
    			//Si el tamaño del fichero excede el limite configurado se lanzara una excepcion    			
    			fileUpload.setSizeMax(maxUploadFileSize);
    			//Obtener los "FileItem" de la request.
    			List<?> fileItems = fileUpload.parseRequest(request);
    			
    			if (fileItems != null) {
    				
	    			//Recorrer los fileItems primero para obtener los parametros
    				parameters = new Properties();
	    			for (int i = 0; i < fileItems.size(); i++) {
	    				fileItem = (FileItem)fileItems.get(i);
	    				//Solo procesar si es un parametro
	    				if (fileItem.isFormField()) {
	    					parameters.setProperty(fileItem.getFieldName(), fileItem.getString());
	    				}
	    			}
	    			
	    			//Recoger los parametros
	    			sessionId = parameters.getProperty("SessionPId");
	    			folderId = parameters.getProperty("FolderId");
	    			entidadId = parameters.getProperty("EntidadId");
	    			bookId = parameters.getProperty("BookId");
	    			nombreCarpeta = parameters.getProperty("NombreCarpeta");
	    			
	    			if(null == useCaseConf){
	    				useCaseConf = new UseCaseConf();
	    				
	    				useCaseConf.setSessionID(sessionId);
	    				useCaseConf.setLocale(request.getLocale());
	    				useCaseConf.setEntidadId(entidadId);
	    			}
	    			
	    			AxSf axsf = bookUseCase.getBookFolder(useCaseConf, new Integer(bookId), Integer.parseInt(folderId));
	        		String nreg = axsf.getAttributeValueAsString("fld1");
	    			
	    			//Guardar los ficheros
	    			Map<String, Object> documentos = new HashMap<String, Object>();
	    			List<String> listaNombreDocs = new ArrayList<String>();
	    			
	    			for (int i = 0; i < fileItems.size(); i++) {
	    				fileItem = (FileItem)fileItems.get(i);
	    				
	    				//Solo procesar si es un fichero.
	    				if (!fileItem.isFormField()) {
	    					
	    					//Componer nombre del fichero
	                        String fileNameFis = getFileNameFis(sessionId, folderId.toString(), fileIndex, fileItem.getName());	                        
	                        fileIndex++;

	                        //Obtener directorio temporal para guardar el fichero
	                        if (Configurator.getInstance().getPropertyBoolean(ConfigurationKeys.KEY_DESKTOP_ISRELATIVE_TEMPORAL_DIR)) {
	                        	newDir = new File(ContextUtil.getRealPath(getServletContext(), Configurator.getInstance().getProperty(ConfigurationKeys.KEY_DESKTOP_TEMPORALDIRECTORYNAME)));
	                        	
	                        } else {
	                            newDir = new File(Configurator.getInstance().getProperty( ConfigurationKeys.KEY_DESKTOP_TEMPORALDIRECTORYNAME));
	                        }
	                        
	                        //Si el directorio temporal no existe, crearlo.
	                        if (!newDir.exists()) {
	                            newDir.mkdir();
	                        }
	                        
	                        //Obtener el fichero destino
	                        if (Configurator.getInstance().getPropertyBoolean(ConfigurationKeys.KEY_DESKTOP_ISRELATIVE_TEMPORAL_DIR)) {
	                             newFile = new File(ContextUtil.getRealPath(getServletContext(), Configurator.getInstance().getProperty(ConfigurationKeys.KEY_DESKTOP_TEMPORALDIRECTORYNAME)), fileNameFis);
	                        } else {
	                            newFile = new File( Configurator.getInstance().getProperty(ConfigurationKeys.KEY_DESKTOP_TEMPORALDIRECTORYNAME), fileNameFis);	                            
	                        }
	                        //Escribir el fichero
	                        newFile.deleteOnExit();
	                        fileItem.write(newFile);
	                        
	                        
	                	    List<FlushFdrPage> pages = new ArrayList<FlushFdrPage>();
	                	    String fileExtension = FilenameUtils.getExtension(fileItem.getName());
	                	    String docName = FilenameUtils.getBaseName(fileItem.getName());
	                	    
	                	    FlushFdrFile documentDataToUpload = new FlushFdrFile();
//	                	    documentDataToUpload.setBuffer(IOUtils.toByteArray(fileItem.getInputStream()));
	                	    documentDataToUpload.setBuffer(IOUtils.toByteArray(new FileInputStream(newFile)));
	                	    documentDataToUpload.setExtension(fileExtension); 
	                	    documentDataToUpload.setFileNameFis(fileItem.getName());
	                	    documentDataToUpload.setFileNameLog(fileItem.getName());

	                	    FlushFdrPage documentPageToUpload = new FlushFdrPage();
	                	    documentPageToUpload.setPageName(docName);
	                	    documentPageToUpload.setFile(documentDataToUpload);
	                	    pages.add(documentPageToUpload);

	                	    FlushFdrDocument documentToUpload = new FlushFdrDocument();
	                	    documentToUpload.setDocumentName(fileItem.getName());
	                	    documentToUpload.setPages(pages);
	                	    documentos.put(fileItem.getName(), documentToUpload);
	                	    
	                	    listaNombreDocs.add(fileItem.getName());
	                	    
	                	    if(StringUtils.isEmpty(nombreCarpeta)){
	                	    	HistoricoDocumentosBO.historicoDocCrearEvent(useCaseConf, nreg, Integer.parseInt(bookId), HistoricoDocumentosBO.TIPO_CARPETA, fileItem.getName());
	                	    }	                	    
	    			        HistoricoDocumentosBO.historicoDocEscanearEvent(useCaseConf, nreg, Integer.parseInt(bookId), HistoricoDocumentosBO.TIPO_DOCUMENTO, fileItem.getName());	    			        
	    				}
	    			}
	    			FolderDataSession folderDataSession = null;
	    			if(StringUtils.isEmpty(nombreCarpeta)){
	    				folderDataSession = FolderSession.addDocument(sessionId, Integer.parseInt(bookId), Integer.parseInt(folderId), documentos, new AxSf(), request.getLocale(), entidadId);
	    			} else {
	    				folderDataSession = FolderSession.addPage(sessionId, Integer.parseInt(bookId), Integer.parseInt(folderId), nombreCarpeta, documentos, new AxSf(), request.getLocale(), entidadId);
	    			}
	    			
	    			for(String nombreDoc : listaNombreDocs){
	    				
	    				FlushFdrDocument documento = (FlushFdrDocument) documentos.get(nombreDoc);
	    				
	    				FlushFdrPage page = (FlushFdrPage) documento.getPages().get(0);
	    				String pageId = page.getFile().getPageID();
	    				String fileId = page.getFile().getFileID();
	    				String extension = page.getFile().getExtension();

						// Adaptación a Alfresco. El identificador de alfresco es alfanumérico y no entero.
						try {
							Integer.parseInt(fileId);
						} catch (NumberFormatException e) {
							fileId = calculateFileIDAlfresco(folderId, Integer.parseInt(pageId));
						}
	    				
	    	        	MetadatosBo.insertaMetadatosEscaneo(useCaseConf.getSessionID(), Integer.parseInt(bookId), Integer.parseInt(folderId), Integer.parseInt(pageId), Integer.parseInt(fileId), entidadId, folderDataSession.getCurrentDate(), extension);
	    			}
				}
    		}
    	} catch (FileUploadException e) {
            LOGGER.fatal("Error cargando ficheros", e);
            
        } catch (Exception e) {
            LOGGER.fatal("Error cargando ficheros", e);
        }
    }
    
    private String getFileNameFis(String sessionId, String folderId, int order, String name) {
    	String extension = getExtension(name); 
        StringBuffer buffer = new StringBuffer();
        buffer.append(sessionId);
        buffer.append(GUIONBAJO);
        buffer.append(folderId);
        buffer.append(GUIONBAJO);
        buffer.append(order);
        
        if (!extension.equals("-")){
        	buffer.append(PUNTO);
        	buffer.append(extension);
        }

        return buffer.toString();
    }

    private String getExtension(String name) {
        String extension = name.substring(name.lastIndexOf(DOSPATH) + 1, name.length());
        if (extension.indexOf(PUNTO) == -1){
        	extension = "-";
        } else {
        	extension = extension.substring(extension.lastIndexOf(PUNTO) + 1, extension.length());
        }
        return extension;
    }
    
	/** Se toma como identificador para Alfresco el fdrId seguido por un id de tres dígitos 
	 * 
	 * @param fdrId
	 * @param id Identificador de página del fichero
	 * @return identificador con formato para alfresco.
	 */
	private static String calculateFileIDAlfresco(String fdrId, int id) {
	
		String newFileId = fdrId;
		String idAux = Integer.toString(id);
		for (int i=idAux.length(); i < 3 ;i++) {
			newFileId = newFileId + "0";
		}
		newFileId = newFileId + idAux;
		return newFileId;

	}
	
    

}
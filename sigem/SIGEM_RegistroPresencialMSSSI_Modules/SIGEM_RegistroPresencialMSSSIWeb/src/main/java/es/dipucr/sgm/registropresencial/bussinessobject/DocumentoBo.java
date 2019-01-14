package es.dipucr.sgm.registropresencial.bussinessobject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import com.ieci.tecdoc.idoc.flushfdr.FlushFdrDocument;
import com.ieci.tecdoc.idoc.flushfdr.FlushFdrFile;
import com.ieci.tecdoc.idoc.flushfdr.FlushFdrPage;
import com.ieci.tecdoc.isicres.session.folder.FolderDataSession;

import es.dipucr.metadatos.beans.MetadatosDocumentoBean;
import es.dipucr.metadatos.bussinessobject.MetadatosBo;
import es.dipucr.sgm.registropresencial.beans.DocumentoBean;
import es.msssi.sgm.registropresencial.arboldocumentos.Document;
import es.msssi.sgm.registropresencial.beans.InputRegisterBean;
import es.msssi.sgm.registropresencial.beans.OutputRegisterBean;
import es.msssi.sgm.registropresencial.beans.RowSearchInputRegisterBean;
import es.msssi.sgm.registropresencial.beans.RowSearchOutputRegisterBean;
import es.msssi.sgm.registropresencial.beans.ibatis.Axdoch;
import es.msssi.sgm.registropresencial.beans.ibatis.Axpageh;
import es.msssi.sgm.registropresencial.businessobject.IGenericBo;
import es.msssi.sgm.registropresencial.businessobject.InputRegisterBo;
import es.msssi.sgm.registropresencial.businessobject.RegisterDocumentsBo;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.errors.RPRegisterException;

public class DocumentoBo implements IGenericBo, Serializable{
	
	private static final long serialVersionUID = 1L;
	
	private static final Logger LOGGER = Logger.getLogger(DocumentoBo.class);
	
	public static void modificarNombre(DocumentoBean documentoBean){
		if(null != documentoBean.getSelectAnexo() && StringUtils.isNotEmpty(documentoBean.getNombreModificarDocumento())){
			
			Integer folderId = getFdrId(documentoBean);
			String nreg = getNumreg(documentoBean);
			
			String nombreViejoDocumento = "";
			String nombreModificarCarpeta = documentoBean.getNombreModificarDocumento();
			
			if(documentoBean.getSelectAnexo().isEsDocumento()){
				
				nombreViejoDocumento = documentoBean.getSelectAnexo().getDocumento().getName();
				
				if(-1 == nombreModificarCarpeta.lastIndexOf(".")){
					nombreModificarCarpeta += "." + nombreViejoDocumento.substring(nombreViejoDocumento.lastIndexOf(".") + 1, nombreViejoDocumento.length());
				}
				
				if(!existeDocumento(documentoBean.getArbolDocumentos(), nombreModificarCarpeta)){
					InputRegisterBo.modificarNombreDocumento(documentoBean.getUseCaseConf(), documentoBean.getBookId(), folderId, documentoBean.getSelectAnexo().getDocumento().getDocId(), nombreViejoDocumento, nombreModificarCarpeta);
					HistoricoDocumentosBO.historicoDocEditarEvent(documentoBean.getUseCaseConf(), nreg, documentoBean.getBookId(), HistoricoDocumentosBO.TIPO_DOCUMENTO, nombreViejoDocumento, nombreModificarCarpeta);
				}
				
			} else if(!existeCarpeta(documentoBean.getArbolDocumentos(), nombreModificarCarpeta)){
				nombreViejoDocumento = documentoBean.getSelectAnexo().getDoch().getName();
				
				InputRegisterBo.modificarNombreCarpeta(documentoBean.getUseCaseConf(), documentoBean.getBookId(), folderId, nombreViejoDocumento, nombreModificarCarpeta);
				HistoricoDocumentosBO.historicoDocEditarEvent(documentoBean.getUseCaseConf(), nreg, documentoBean.getBookId(), HistoricoDocumentosBO.TIPO_CARPETA, nombreViejoDocumento, nombreModificarCarpeta);
			}
		}
		
		documentoBean.setNombreModificarDocumento("");
		documentoBean.setSelectAnexo(null);
	}
	
	public static void crearCarpeta(DocumentoBean documentoBean) {
		
		if(!existeCarpeta(documentoBean.getArbolDocumentos(), documentoBean.getNombreNuevoDocumento())){
			InputRegisterBo.crearCarpeta(documentoBean.getUseCaseConf(), documentoBean.getBookId(), getFdrId(documentoBean), documentoBean.getNombreNuevoDocumento());
		
        	HistoricoDocumentosBO.historicoDocCrearEvent(documentoBean.getUseCaseConf(), getNumreg(documentoBean), documentoBean.getBookId(), HistoricoDocumentosBO.TIPO_CARPETA, documentoBean.getNombreNuevoDocumento());
		}

		documentoBean.setNombreNuevoDocumento("");
	}	
	
	public static void borrar(DocumentoBean documentoBean){

		if(null != documentoBean.getSelectAnexo()){
		
			if(documentoBean.getSelectAnexo().isEsDocumento()){				
				deletePage(documentoBean);					
			} else {
				deleteDocument(documentoBean);					
			}
		}
		
		documentoBean.setSelectAnexo(null);
	}
		
	public static void subirDocumento(DocumentoBean documentoBean){
		
		FolderDataSession folderDataSession = null;

		Map<String, Object> createAttachedDocumentMap = createAttachedDocumentMap(documentoBean.getFile().getFileName(), documentoBean.getFile().getContents());
		
		Integer folderId = getFdrId(documentoBean);
		String nreg = getNumreg(documentoBean);
		
		if(StringUtils.isNotEmpty(documentoBean.getCarpetaAnexarDoc())){
			if(!existeDocumento(documentoBean.getArbolDocumentos(), documentoBean.getFile().getFileName())){
				folderDataSession = InputRegisterBo.subirDocumentosCarpeta(documentoBean.getUseCaseConf(), documentoBean.getBookId(), folderId, documentoBean.getCarpetaAnexarDoc(), createAttachedDocumentMap);
		        HistoricoDocumentosBO.historicoDocAjuntarEvent(documentoBean.getUseCaseConf(), nreg, documentoBean.getBookId(), HistoricoDocumentosBO.TIPO_DOCUMENTO, documentoBean.getFile().getFileName());
			}
			
		} else {
			if(!existeCarpeta(documentoBean.getArbolDocumentos(), documentoBean.getFile().getFileName()) && !existeDocumento(documentoBean.getArbolDocumentos(), documentoBean.getFile().getFileName())){
				folderDataSession = InputRegisterBo.subirDocumentos(documentoBean.getUseCaseConf(), documentoBean.getBookId(), folderId, createAttachedDocumentMap);
			
				HistoricoDocumentosBO.historicoDocCrearEvent(documentoBean.getUseCaseConf(), nreg, documentoBean.getBookId(), HistoricoDocumentosBO.TIPO_CARPETA, documentoBean.getFile().getFileName());
				HistoricoDocumentosBO.historicoDocAjuntarEvent(documentoBean.getUseCaseConf(), nreg, documentoBean.getBookId(), HistoricoDocumentosBO.TIPO_DOCUMENTO, documentoBean.getFile().getFileName());
			}
		}
		
		FlushFdrDocument documento = (FlushFdrDocument) createAttachedDocumentMap.get(documentoBean.getFile().getFileName());
		
		FlushFdrPage page = (FlushFdrPage) documento.getPages().get(0);
		String pageId = page.getFile().getPageID();
		String fileId = page.getFile().getFileID();
		String extension = page.getFile().getExtension();

    	MetadatosBo.insertaMetadatosAnexar(documentoBean.getUseCaseConf().getSessionID(), documentoBean.getBookId(), folderId, Integer.parseInt(pageId), Integer.parseInt(fileId), documentoBean.getEntidadId(), folderDataSession.getCurrentDate(), extension);
	}
	
	public static void consultaDocumento(DocumentoBean documentoBean) {
		HistoricoDocumentosBO.historicoDocConsultarEvent(documentoBean.getUseCaseConf(), getNumreg(documentoBean), documentoBean.getBookId(), HistoricoDocumentosBO.TIPO_DOCUMENTO, documentoBean.getCarpetaAnexarDoc());		
	}

	public static boolean existeCarpeta(TreeNode arbolDocumentos, String nombreCarpeta){
		
		if(null!= arbolDocumentos && null != arbolDocumentos.getChildren()){
			return existeCarpetaODocumento(true, nombreCarpeta, arbolDocumentos.getChildren());
		}
		return false;
	}
	
	public static boolean existeDocumento(TreeNode arbolDocumentos, String nombreDocumento){
		
		if(null != arbolDocumentos && null != arbolDocumentos.getChildren()){
			return existeCarpetaODocumento(false, nombreDocumento, arbolDocumentos.getChildren());
		}
		return false;
	}
	
	private static boolean existeCarpetaODocumento(boolean esObjetivo, String nombre, List<TreeNode> listaHijos) {
		boolean existe = false;
		
		for(TreeNode nodo : listaHijos){				
			Document documento = (Document) nodo.getData();
		
			if(null != documento){
				if(esObjetivo){
					 if(StringUtils.equalsIgnoreCase(nombre, documento.getNombre())){
						 return true;
					 }
				} else {
					if(null != nodo.getChildren() && existeCarpetaODocumento(true, nombre, nodo.getChildren())){
						return true;
					}		
				}
			}
		}
		
		return existe;
	}
	
	public static Integer getFdrId(DocumentoBean documentoBean){
		
		Integer folderId = new Integer(-1);
		
		if( null == documentoBean.getRegisterBean()){
			folderId = new Integer(-1);
			
		} else if(documentoBean.getRegisterBean() instanceof InputRegisterBean){
			folderId = ((InputRegisterBean)documentoBean.getRegisterBean()).getFdrid();
			
		} else if(documentoBean.getRegisterBean() instanceof OutputRegisterBean){
			folderId = ((OutputRegisterBean)documentoBean.getRegisterBean()).getFdrid();
			
		} else if(documentoBean.getRegisterBean() instanceof RowSearchInputRegisterBean){
			folderId = ((RowSearchInputRegisterBean) documentoBean.getRegisterBean()).getFdrid();
			
		} else if(documentoBean.getRegisterBean() instanceof RowSearchOutputRegisterBean){
			folderId = ((RowSearchOutputRegisterBean) documentoBean.getRegisterBean()).getFdrid();
		}
		
		return folderId;
	}
	
	public static String getNumreg(DocumentoBean documentoBean){
		
		String nreg = "";
		
		if( null == documentoBean.getRegisterBean()){
			nreg = "";
			
		} else if(documentoBean.getRegisterBean() instanceof InputRegisterBean){
			nreg = ((InputRegisterBean)documentoBean.getRegisterBean()).getFld1();
			
		} else if(documentoBean.getRegisterBean() instanceof OutputRegisterBean){
			nreg = ((OutputRegisterBean)documentoBean.getRegisterBean()).getFld1();
			
		} else if(documentoBean.getRegisterBean() instanceof RowSearchInputRegisterBean){
			nreg = ((RowSearchInputRegisterBean) documentoBean.getRegisterBean()).getFld1();
			
		} else if(documentoBean.getRegisterBean() instanceof RowSearchOutputRegisterBean){
			nreg = ((RowSearchOutputRegisterBean) documentoBean.getRegisterBean()).getFld1();
		}
		
		return nreg;
	}
	
	/**
	 * Crea un adjunto a partir de un documento en byte[]
	 * @param nombre
	 * @param contenidoDocumento
	 * @return
	 * @throws SigmWSException 
	 */
	public static Map<String, Object> createAttachedDocumentMap(String nombre, byte[] contenidoDocumento) {
		
	    Map<String, Object> result = new HashMap<String, Object>();
	    List<FlushFdrPage> pages = new ArrayList<FlushFdrPage>();
	    String fileExtension = FilenameUtils.getExtension(nombre);
	    String docName = FilenameUtils.getBaseName(nombre);
	    
	    FlushFdrFile documentDataToUpload = new FlushFdrFile();
	    documentDataToUpload.setBuffer(contenidoDocumento);
	    documentDataToUpload.setExtension(fileExtension); 
	    documentDataToUpload.setFileNameFis(nombre);
	    documentDataToUpload.setFileNameLog(nombre);

	    FlushFdrPage documentPageToUpload = new FlushFdrPage();
	    documentPageToUpload.setPageName(docName);
	    documentPageToUpload.setFile(documentDataToUpload);
	    pages.add(documentPageToUpload);

	    FlushFdrDocument documentToUpload = new FlushFdrDocument();
	    documentToUpload.setDocumentName(nombre);
	    documentToUpload.setPages(pages);
	    result.put(nombre, documentToUpload);
	    
		return result;
	}
	
	/**
	 * borra la carpeta del registro si se encuentra vacía.
	 */
	public static void deleteDocument(DocumentoBean documentoBean) {
		RegisterDocumentsBo registerDocumentsBo = new RegisterDocumentsBo();
		
		try {
			Integer folderId = getFdrId(documentoBean);
			String nreg = getNumreg(documentoBean);
			
			Axdoch selectDocument = documentoBean.getSelectAnexo().getDoch();
			
			if (esCarpetaVacia(selectDocument)){				
				LOGGER.info("borramos el documento registro " + folderId);
				
				registerDocumentsBo.deleteDocument(documentoBean.getUseCaseConf(), documentoBean.getBookId(), folderId, selectDocument.getId().intValue());
				HistoricoDocumentosBO.historicoDocBorrarEvent(documentoBean.getUseCaseConf(), nreg, documentoBean.getBookId(), HistoricoDocumentosBO.TIPO_CARPETA, selectDocument.getName());
			}
			
		} catch (RPRegisterException rPRegisterException) {
			LOGGER.error(ErrorConstants.DELETE_REGISTER_DOCUMENTS_ERROR_MESSAGE, rPRegisterException);
		}
	}
	
	private static boolean esCarpetaVacia(Axdoch documentoSeleccionado) {
		return (null != documentoSeleccionado && (null == documentoSeleccionado.getPages() || documentoSeleccionado.getPages().size() == 0));
	}

	/**
	 * borra el documento del registro.
	 * @param axpageh 
	 */
	public static void deletePage(DocumentoBean documentoBean) {
		RegisterDocumentsBo registerDocumentsBo = new RegisterDocumentsBo();

		try {
			Axpageh pagina = documentoBean.getSelectAnexo().getDocumento();
			
			Integer folderId = getFdrId(documentoBean);
			String nreg = getNumreg(documentoBean);
			
			if(null != pagina && !documentoBean.getSelectAnexo().isEsFirmado()){					
				LOGGER.info("borramos el documento registro " + folderId);
				
				registerDocumentsBo.deletePage(documentoBean.getUseCaseConf(), documentoBean.getBookId(), folderId, pagina.getDocId(), pagina.getFileId());	
		        HistoricoDocumentosBO.historicoDocBorrarEvent(documentoBean.getUseCaseConf(), nreg, documentoBean.getBookId(), HistoricoDocumentosBO.TIPO_DOCUMENTO, documentoBean.getSelectAnexo().getDocumento().getName());
		        
		        MetadatosDocumentoBean metadatosDocumento = new MetadatosDocumentoBean(documentoBean.getBookId(), folderId, pagina.getId(), pagina.getFileId(), documentoBean.getEntidadId(), null);
		        
		        MetadatosBo.borrarMetadatos(metadatosDocumento);
			}
			
		} catch (RPRegisterException rPRegisterException) {
			LOGGER.error(ErrorConstants.DELETE_REGISTER_DOCUMENTS_ERROR_MESSAGE, rPRegisterException);
		}
	}
	
	public static void actualizaArbol(boolean openFolderDtr, DocumentoBean documentoBean){
		
		RegisterDocumentsBo registerDocumentsBo = new RegisterDocumentsBo();
		
		List<Axdoch> listaArbol = new ArrayList<Axdoch>();

		try {
			if(null != documentoBean.getRegisterBean() && null != getFdrId(documentoBean)){
				listaArbol = registerDocumentsBo.getAllDocumentsYFolders(documentoBean.getUseCaseConf(), documentoBean.getBookId(), getFdrId(documentoBean), openFolderDtr);
			}
			
			documentoBean.setArbolDocumentos(createDocumentsTree(listaArbol, documentoBean));
			
		} catch (RPRegisterException rPRegisterException) {
			LOGGER.error(ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE, rPRegisterException);
		}
	}
	
	public static TreeNode createDocumentsTree(List<Axdoch> listaArbol, DocumentoBean documentoBean) {
		Document raizDoc = new Document("Documentos", "", new Integer(0), new Integer(0), "", new Date(), false, null, null, null, null, null, documentoBean.getBookId(), documentoBean.getEntidadId());
		TreeNode raiz = new DefaultTreeNode(raizDoc, null);

		for (Axdoch carpeta : listaArbol) {			
			Document carpt = new Document(carpeta);
			raizDoc.getListaDocumentos().add(carpt);
			
			TreeNode nodoCarpeta = new DefaultTreeNode(carpt, raiz);
			
			for (Axpageh documento : carpeta.getPages()) {
				Document doc = new Document(documento, documentoBean.getBookId(), documentoBean.getEntidadId());
				carpt.getListaDocumentos().add(doc);
				
				new DefaultTreeNode(doc, nodoCarpeta);
			}
		}

		return raiz;
	}
}


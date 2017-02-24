/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sgm.registropresencial.businessobject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.faces.context.FacesContext;

import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;
import org.springframework.context.ApplicationContext;

import com.ieci.tecdoc.common.entity.AxDochEntity;
import com.ieci.tecdoc.common.exception.BookException;
import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.ValidationException;
import com.ieci.tecdoc.common.isicres.AxDoch;
import com.ieci.tecdoc.common.isicres.AxPKById;
import com.ieci.tecdoc.common.isicres.AxSf;
import com.ieci.tecdoc.idoc.flushfdr.FlushFdrDocument;
import com.ieci.tecdoc.idoc.flushfdr.FlushFdrFile;
import com.ieci.tecdoc.idoc.flushfdr.FlushFdrPage;
import com.ieci.tecdoc.isicres.session.folder.FolderFileSession;
import com.ieci.tecdoc.isicres.session.folder.FolderSession;
import com.ieci.tecdoc.isicres.session.security.SecuritySession;
import com.ieci.tecdoc.isicres.usecase.UseCaseConf;
import com.ieci.tecdoc.utils.HibernateUtil;
import com.ieci.tecdoc.utils.Validator;
import com.ieci.tecdoc.utils.cache.CacheBag;
import com.ieci.tecdoc.utils.cache.CacheFactory;
import es.ieci.tecdoc.isicres.api.business.manager.IsicresManagerProvider;
import es.ieci.tecdoc.isicres.api.business.vo.UsuarioVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.manager.DocumentoElectronicoAnexoManager;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.ConfiguracionCreateDocumentoElectronicoAnexoVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.DocumentoElectronicoAnexoContenidoVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.DocumentoElectronicoAnexoDatosFirmaVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.DocumentoElectronicoAnexoVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.IdentificadorDocumentoElectronicoAnexoVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.TipoDocumentoAnexoEnumVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.TipoValidezDocumentoAnexoEnumVO;
import es.msssi.sgm.registropresencial.beans.ContentTypeEnum;
import es.msssi.sgm.registropresencial.beans.ibatis.Axdoch;
import es.msssi.sgm.registropresencial.beans.ibatis.Axpageh;
import es.msssi.sgm.registropresencial.config.RegistroPresencialMSSSIWebSpringApplicationContext;
import es.msssi.sgm.registropresencial.daos.DocumentDAO;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.errors.RPRegisterErrorCode;
import es.msssi.sgm.registropresencial.errors.RPRegisterException;
import es.msssi.sgm.registropresencial.utils.Utils;
import es.msssi.sgm.registropresencial.utils.UtilsHash;

/**
 * Clase que implementa IGenericBo que contiene los métodos relacionados con los
 * documentos de los registros.
 * 
 * @author cmorenog
 */
public class RegisterDocumentsBo implements IGenericBo {
    private static final Logger LOG = Logger.getLogger(RegisterDocumentsBo.class.getName());
    private DocumentDAO docsDao = new DocumentDAO();
    private static ApplicationContext appContext;
    private static DocumentoElectronicoAnexoManager documentoElectronicoAnexoManager;
    
    static {
	appContext =
		RegistroPresencialMSSSIWebSpringApplicationContext.getInstance()
			.getApplicationContext();
	if (documentoElectronicoAnexoManager == null) {
	    documentoElectronicoAnexoManager =
		    IsicresManagerProvider.getInstance().getDocumentoElectronicoAnexoManager();
	}
    }

    /**
     * Metodo que obtiene la lista de documentos de un registro.
     * 
     * @param useCaseConf
     *            - Datos de conexión del usuario.
     * @param bookId
     *            - ID del libro.
     * @param folderId
     *            - Identificador del registro/carpeta.
     * @param openFolderDtr
     *            - Indicador de libro abierto.
     * @return lista de los documentos anexos al registro.
     * 
     * @throws RPRegisterException
     *             Si se ha producido un error al obtener los documentos anexos.
     */
    @SuppressWarnings("unchecked")
    public List<AxDoch> getDocuments(UseCaseConf useCaseConf, Integer bookId, int folderId,
	    boolean openFolderDtr) throws RPRegisterException {
	List<AxDoch> docs = null;
	try {
	    AxSf axsf =
		    FolderSession.getBookFolder(useCaseConf.getSessionID(), bookId, folderId,
			    useCaseConf.getLocale(), useCaseConf.getEntidadId());
	    boolean permShowDocuments =
		    SecuritySession.permisionShowDocuments(useCaseConf.getSessionID(), axsf);

	    if (permShowDocuments || openFolderDtr) {
		docs =
			FolderFileSession.getBookFolderDocsWithPages(useCaseConf.getSessionID(),
				bookId, folderId, useCaseConf.getEntidadId());
	    }
	}
	catch (ValidationException validationException) {
	    LOG.error(ErrorConstants.GET_REGISTER_DOCUMENTS_ERROR_MESSAGE, validationException);
	    throw new RPRegisterException(RPRegisterErrorCode.GET_REGISTER_DOCUMENTS_ERROR,
		    ErrorConstants.GET_REGISTER_DOCUMENTS_ERROR_MESSAGE, validationException);
	}
	catch (BookException bookException) {
	    LOG.error(ErrorConstants.GET_REGISTER_DOCUMENTS_ERROR_MESSAGE, bookException);
	    throw new RPRegisterException(RPRegisterErrorCode.GET_REGISTER_DOCUMENTS_ERROR,
		    ErrorConstants.GET_REGISTER_DOCUMENTS_ERROR_MESSAGE, bookException);
	}
	catch (SessionException sessionException) {
	    LOG.error(ErrorConstants.GET_REGISTER_DOCUMENTS_ERROR_MESSAGE, sessionException);
	    throw new RPRegisterException(RPRegisterErrorCode.GET_REGISTER_DOCUMENTS_ERROR,
		    ErrorConstants.GET_REGISTER_DOCUMENTS_ERROR_MESSAGE, sessionException);
	}
	return docs;
    }

    /**
     * Obtiene la información básica de los documentos de un registro, para
     * mostrarlas en un listado.
     * 
     * @param useCaseConf
     *            - Datos de conexión del usuario.
     * @param bookId
     *            - Id. del libro.
     * @param folderId
     *            - Id. del registro.
     * @param openFolderDtr
     *            - Indicador de libro abierto.
     * @param flag
     * 		indicador de si es un fichero interno no imprimible en los acuses.
     * @return lista de información básica de los documentos anexos al registro.
     * @throws RPRegisterException
     *             Si se ha producido un error al obtener los documentos anexos.
     */
    @SuppressWarnings("unchecked")
    public List<Axdoch> getDocumentsBasicInfo(UseCaseConf useCaseConf, Integer bookId,
	    int folderId, boolean openFolderDtr, Integer flag) throws RPRegisterException {
	List<Axdoch> result = new ArrayList<Axdoch>();
	Transaction tran = null;
	String entity = useCaseConf.getEntidadId();
	HibernateUtil hibernateUtil = new HibernateUtil();
	try {

	    Validator.validate_String_NotNull_LengthMayorZero(useCaseConf.getSessionID(),
		    ValidationException.ATTRIBUTE_SESSION);
	    Validator.validate_Integer(bookId, ValidationException.ATTRIBUTE_BOOK);
	    AxSf axsf =
		    FolderSession.getBookFolder(useCaseConf.getSessionID(), bookId, folderId,
			    useCaseConf.getLocale(), useCaseConf.getEntidadId());
	    boolean permShowDocuments =
		    SecuritySession.permisionShowDocuments(useCaseConf.getSessionID(), axsf);
	    Session session = hibernateUtil.currentSession(entity);
	    tran = session.beginTransaction();
	    // Recuperamos la sesión
	    CacheBag cacheBag =
		    CacheFactory.getCacheInterface().getCacheEntry(useCaseConf.getSessionID());
	    // Es necesario tener el libro abierto para consultar su contenido.
	    if (!cacheBag.containsKey(bookId)) {
		throw new BookException(BookException.ERROR_BOOK_NOT_OPEN);
	    }
	    if (permShowDocuments || openFolderDtr) {
		AxDochEntity axDochEntity = new AxDochEntity();
		Collection<AxPKById> docs = null;
		docsDao = (DocumentDAO) appContext.getBean("documentDAO");
		docs = axDochEntity.findByFdrid(bookId, folderId, entity);
		for (AxPKById axPKByIdDoc : docs) {
		    Axdoch axdoch = docsDao.getDocBasicInfo(axPKByIdDoc);

		    List<Axpageh> listPage = null;
		    List<Axpageh> listPageTemp = new ArrayList<Axpageh>();
		    listPage = docsDao.getPagesBasicInfo(axPKByIdDoc, flag);

		    for (Axpageh page : listPage) {
			if (page.getPageSignedId() == null
				|| (page.getPageSignedId() != null && page.getPageSignedId()
					.equals(page.getId()))) {
			    for (Axpageh pageSign : listPage) {
				if (pageSign.getPageSignedId() != null
					&& !pageSign.getPageId().equals(page.getId())
					&& pageSign.getPageSignedId().equals(page.getId())) {
				    page.setPageSigned(pageSign);
				}
			    }
			    listPageTemp.add(page);
			}
		    }

		    if (listPageTemp != null && listPageTemp.size() > 0) {
			axdoch.setPages(listPageTemp);
		    }

		    if (axdoch.getPages() != null && axdoch.getPages().size() > 0) {
			result.add(axdoch);
		    }
		}
		hibernateUtil.commitTransaction(tran);
	    }
	}
	catch (BookException bookException) {
	    hibernateUtil.rollbackTransaction(tran);
	    LOG.error(ErrorConstants.GET_REGISTER_DOCUMENTS_ERROR_MESSAGE, bookException);
	    throw new RPRegisterException(RPRegisterErrorCode.GET_REGISTER_DOCUMENTS_ERROR,
		    ErrorConstants.GET_REGISTER_DOCUMENTS_ERROR_MESSAGE, bookException);
	}
	catch (SessionException sessionException) {
	    hibernateUtil.rollbackTransaction(tran);
	    LOG.error(ErrorConstants.GET_REGISTER_DOCUMENTS_ERROR_MESSAGE, sessionException);
	    throw new RPRegisterException(RPRegisterErrorCode.GET_REGISTER_DOCUMENTS_ERROR,
		    ErrorConstants.GET_REGISTER_DOCUMENTS_ERROR_MESSAGE, sessionException);
	}
	catch (ValidationException validationException) {
	    LOG.error(ErrorConstants.GET_REGISTER_DOCUMENTS_ERROR_MESSAGE, validationException);
	    throw new RPRegisterException(RPRegisterErrorCode.GET_REGISTER_DOCUMENTS_ERROR,
		    ErrorConstants.GET_REGISTER_DOCUMENTS_ERROR_MESSAGE, validationException);
	}
	catch (Exception exception) {
	    hibernateUtil.rollbackTransaction(tran);
	    LOG.error(ErrorConstants.GET_REGISTER_DOCUMENTS_ERROR_MESSAGE, exception);
	    throw new RPRegisterException(RPRegisterErrorCode.GET_REGISTER_DOCUMENTS_ERROR,
		    ErrorConstants.GET_REGISTER_DOCUMENTS_ERROR_MESSAGE, exception);
	}
	finally {
	    hibernateUtil.closeSession(entity);
	}
	return result;
    }

    /**
     * Obtiene la información básica de los documentos de un registro, para
     * mostrarlas en un listado.
     * 
     * @param useCaseConf
     *            - Datos de conexión del usuario.
     * @param bookId
     *            - Id. del libro.
     * @param folderId
     *            - Id. del registro.
     * @param openFolderDtr
     *            - Indicador de libro abierto.
     * @return lista de información básica de los documentos anexos al registro.
     * @throws RPRegisterException
     *             Si se ha producido un error al obtener los documentos anexos.
     */
    public List<Axdoch> getDocumentsBasicInfo(UseCaseConf useCaseConf, Integer bookId,
	    int folderId, boolean openFolderDtr) throws RPRegisterException {
	List<Axdoch> result = new ArrayList<Axdoch>();
	result = getDocumentsBasicInfo(useCaseConf, bookId, folderId, openFolderDtr, null);
	return result;
    }
    
    /**
     * Guarda un documento adjunto a un registro.
     * 
     * @param sessionID
     *            - Id de sesión.
     * @param bookID
     *            - Id de libro.
     * @param folderId
     *            - Id de registro.
     * @param filesToUpload
     *            - Documentos a adjuntar.
     * @param axsf
     *            - Datos del registro.
     * @param locale
     *            - Idioma.
     * @param entidad
     *            - Entidad.
     * @return boolean si se ha guardado correctamente.
     * @throws RPRegisterException
     *             - Si se ha producido un error al adjuntar el documento.
     */
    public boolean saveDocuments(String sessionID, Integer bookID, Integer folderId,
	    List<UploadedFile> filesToUpload, AxSf axsf, Locale locale, String entidad)
	    throws RPRegisterException {
	try {
	    int it = 0;
	    boolean isContentTypeFound = false;
	    Map<String, Object> documentMap = new HashMap<String, Object>();
	    for (UploadedFile fileToUpload : filesToUpload) {
		String fileToUploadContentType = fileToUpload.getContentType();
		do {
		    ContentTypeEnum supportedContentType = ContentTypeEnum.values()[it];
		    if (supportedContentType.getContentType().equals(fileToUploadContentType)) {
			LOG.info("Content Type del documento: " + fileToUploadContentType
				+ "encontrado con :" + supportedContentType.getContentType());
			isContentTypeFound = true;
		    }
		    it++;
		} while (!isContentTypeFound && it < ContentTypeEnum.values().length);
		if (isContentTypeFound) {
		    List<FlushFdrPage> pages = new ArrayList<FlushFdrPage>();
		    byte[] fileBytes =
			    Utils.convertInputStreamToByeArray(fileToUpload.getInputstream());
		    String fileFullName = fileToUpload.getFileName();
		    String fileExtension =
			    fileFullName.substring(fileFullName.lastIndexOf(".") + 1,
				    fileFullName.length());
		    String docName = fileFullName.substring(0, fileFullName.lastIndexOf("."));
		    FlushFdrFile documentDataToUpload = new FlushFdrFile();
		    documentDataToUpload.setBuffer(fileBytes);
		    documentDataToUpload.setExtension(fileExtension);
		    documentDataToUpload.setFileNameFis(fileFullName);
		    documentDataToUpload.setFileNameLog(fileFullName);
		    FlushFdrPage documentPageToUpload = new FlushFdrPage();
		    documentPageToUpload.setPageName(fileFullName);
		    documentPageToUpload.setFile(documentDataToUpload);
		    pages.add(documentPageToUpload);
		    FlushFdrDocument documentToUpload = new FlushFdrDocument();
		    documentToUpload.setDocumentName(docName);
		    documentToUpload.setPages(pages);
		    documentMap.put(docName, documentToUpload);
		}
		FolderSession.addDocument(sessionID, bookID, folderId, documentMap, axsf, locale,
			entidad);
	    }
	    return isContentTypeFound;
	}
	catch (BookException bookException) {
	    LOG.error(ErrorConstants.UPLOADFILE_ERROR_MESSAGE, bookException);
	    throw new RPRegisterException(RPRegisterErrorCode.ADD_REGISTER_DOCUMENTS_ERROR,
		    ErrorConstants.UPLOADFILE_ERROR_MESSAGE, bookException);
	}
	catch (SessionException sessionException) {
	    LOG.error(ErrorConstants.UPLOADFILE_ERROR_MESSAGE, sessionException);
	    throw new RPRegisterException(RPRegisterErrorCode.ADD_REGISTER_DOCUMENTS_ERROR,
		    ErrorConstants.UPLOADFILE_ERROR_MESSAGE, sessionException);
	}
	catch (ValidationException validationException) {
	    LOG.error(ErrorConstants.UPLOADFILE_ERROR_MESSAGE, validationException);
	    throw new RPRegisterException(RPRegisterErrorCode.ADD_REGISTER_DOCUMENTS_ERROR,
		    ErrorConstants.UPLOADFILE_ERROR_MESSAGE, validationException);
	}
	catch (IOException ioException) {
	    LOG.error(ErrorConstants.UPLOADFILE_ERROR_MESSAGE, ioException);
	    throw new RPRegisterException(RPRegisterErrorCode.ADD_REGISTER_DOCUMENTS_ERROR,
		    ErrorConstants.UPLOADFILE_ERROR_MESSAGE, ioException);
	}
    }

    /**
     * Guarda un documento adjunto a un registro.
     * 
     * @param sessionID
     *            - Id de sesión.
     * @param bookID
     *            - Id de libro.
     * @param folderId
     *            - Id de registro.
     * @param filesToUpload
     *            - Documentos a adjuntar.
     * @param axsf
     *            - Datos del registro.
     * @param locale
     *            - Idioma.
     * @param entidad
     *            - Entidad.
     * @flag
     * 		indica si es un acuse o no
     * @return DocumentoElectronicoAnexoVO.
     * @throws RPRegisterException
     *             - Si se ha producido un error al adjuntar el documento.
     */
    public DocumentoElectronicoAnexoVO saveStreamedContentDocuments(String sessionID, Integer bookID, Integer folderId,
	    List<StreamedContent> filesToUpload, AxSf axsf, Locale locale, String entidad)
	    throws RPRegisterException {
	DocumentoElectronicoAnexoVO doc = null;
	try {
	    int it = 0;
	    boolean isContentTypeFound = false;
	    DocumentoElectronicoAnexoVO documentoElectronico = new DocumentoElectronicoAnexoVO();
	    
	    for (StreamedContent fileToUpload : filesToUpload) {
		String fileToUploadContentType = fileToUpload.getContentType();
		do {
		    ContentTypeEnum supportedContentType = ContentTypeEnum.values()[it];
		    if (supportedContentType.getContentType().equals(fileToUploadContentType)) {
			LOG.info("Content Type del documento: " + fileToUploadContentType
				+ "encontrado con :" + supportedContentType.getContentType());
			isContentTypeFound = true;
		    }
		    it++;
		} while (!isContentTypeFound && it < ContentTypeEnum.values().length);
		if (isContentTypeFound) {
		  //identificadorDocumentoElectronicoAnexoVO
        	    IdentificadorDocumentoElectronicoAnexoVO identificadorDocumentoElectronicoAnexoVO = 
        		    new IdentificadorDocumentoElectronicoAnexoVO ();
        	    identificadorDocumentoElectronicoAnexoVO.setIdLibro(Long.parseLong(String.valueOf(bookID)));
        	    identificadorDocumentoElectronicoAnexoVO.setIdRegistro(Long.parseLong(String.valueOf(folderId)));
        	    documentoElectronico.setId(identificadorDocumentoElectronicoAnexoVO);
        	    documentoElectronico.setName(fileToUpload.getName());
        	    //CODE NAME
        	    String codeName=("codeName"+fileToUpload.getName());
        	    codeName=StringUtils.abbreviate(codeName, 21);
        	    documentoElectronico.setCodeName(codeName);
        	    
        	  //extension
        	    String extension=fileToUpload.getName().substring(fileToUpload.getName().lastIndexOf(".") + 1,
        		    fileToUpload.getName().length());
        	    documentoElectronico.setExtension(extension);
        	
        	    //contenido
        	    DocumentoElectronicoAnexoContenidoVO contenido=new DocumentoElectronicoAnexoContenidoVO();
        	    // Contenido del fichero
        	    byte[] contenidoAnexo = Utils.convertInputStreamToByeArray(fileToUpload.getStream());
        	    contenido.setContent(contenidoAnexo);
                	//contenido.setDocUID(documentoCompulsar.getDocumentoOriginal().getLocation());
        	    documentoElectronico.setContenido(contenido);
        	    
        	    //TIPO MIME
        	   documentoElectronico.setMimeType(ContentTypeEnum.PDF.getContentType());
        	    
        	    //TIPO DOCUMENTO
        	   documentoElectronico.setTipoDocumentoAnexo(TipoDocumentoAnexoEnumVO.FICHERO_TECNICO);
	    
        	   documentoElectronico.setTipoValidez(TipoValidezDocumentoAnexoEnumVO.ORIGINAL);

        	    
        	  //datos hash
        	    DocumentoElectronicoAnexoDatosFirmaVO datosFirmaDocumento= new DocumentoElectronicoAnexoDatosFirmaVO();
        	    UtilsHash utilHash = new UtilsHash ();
        	    String hashDocumento= UtilsHash.getBase64Sring(utilHash.generarHash(contenidoAnexo));
        	    datosFirmaDocumento.setHash(hashDocumento);

        	    documentoElectronico.setDatosFirma(datosFirmaDocumento);
        	   
        	    UsuarioVO usuario =
			(UsuarioVO) FacesContext.getCurrentInstance().getExternalContext()
				.getSessionMap().get("USERVO");
        	    
        	    doc = saveDocuments(sessionID, bookID,
				folderId, documentoElectronico, axsf,
				locale, entidad,usuario);
		}
	    }
	}
	catch (Exception ioException) {
	    LOG.error(ErrorConstants.UPLOADFILE_ERROR_MESSAGE, ioException);
	    throw new RPRegisterException(RPRegisterErrorCode.ADD_REGISTER_DOCUMENTS_ERROR,
		    ErrorConstants.UPLOADFILE_ERROR_MESSAGE, ioException);
	}
	 return doc;
    }

    public DocumentoElectronicoAnexoVO saveDocuments(String sessionID, Integer bookID, Integer folderId,
	    DocumentoElectronicoAnexoVO documentoElectronico, AxSf axsf, Locale locale,
	    String entidad, UsuarioVO usuario) throws RPRegisterException {
	try {
	    DocumentoElectronicoAnexoVO doc;
	    ConfiguracionCreateDocumentoElectronicoAnexoVO cfg=new ConfiguracionCreateDocumentoElectronicoAnexoVO();
		
		//seteamos el nombre de la carpeta/clasificador de sicres sobre el que se guardaran
		
		String clasificador = null;
		cfg.setClasificador(clasificador);

		doc = documentoElectronicoAnexoManager.create(documentoElectronico, cfg, usuario);

	    return doc;
	}
	
	catch (Exception exception) {
	    LOG.error(ErrorConstants.UPLOADFILE_ERROR_MESSAGE, exception);
	    throw new RPRegisterException(RPRegisterErrorCode.ADD_REGISTER_DOCUMENTS_ERROR,
		    ErrorConstants.UPLOADFILE_ERROR_MESSAGE, exception);
	}
    }
    
    public void deleteDocument (UseCaseConf useCaseConf, Integer bookId,
	    int folderId, int iddoc)  throws RPRegisterException {

	try {

	    Validator.validate_String_NotNull_LengthMayorZero(useCaseConf.getSessionID(),
		    ValidationException.ATTRIBUTE_SESSION);
	    Validator.validate_Integer(bookId, ValidationException.ATTRIBUTE_BOOK);

	    // Recuperamos la sesión
	    CacheBag cacheBag =
		    CacheFactory.getCacheInterface().getCacheEntry(useCaseConf.getSessionID());
	    // Es necesario tener el libro abierto para consultar su contenido.
	    if (!cacheBag.containsKey(bookId)) {
		throw new BookException(BookException.ERROR_BOOK_NOT_OPEN);
	    }

		docsDao = (DocumentDAO) appContext.getBean("documentDAO");
		AxPKById axPKByIdDoc = new AxPKById();
		axPKByIdDoc.setFdrId(folderId);
		axPKByIdDoc.setType(String.valueOf(bookId));
		axPKByIdDoc.setId(iddoc);
		docsDao.deleteDocument(axPKByIdDoc);

	}
	catch (Exception exception) {
	    LOG.error(ErrorConstants.DELETE_REGISTER_DOCUMENTS_ERROR_MESSAGE, exception);
	    throw new RPRegisterException(RPRegisterErrorCode.DELETE_REGISTER_DOCUMENTS_ERROR,
		    ErrorConstants.DELETE_REGISTER_DOCUMENTS_ERROR_MESSAGE, exception);
	}
    }
    
    /**
     * Obtiene el numero de acuses de un registro.
     * 
     * @param useCaseConf
     *            - Datos de conexión del usuario.
     * @param bookId
     *            - Id. del libro.
     * @param folderId
     *            - Id. del registro.
     * @return Numero de acuses que hay en el registro.
     * @throws RPRegisterException
     *             Si se ha producido un error al obtener los documentos anexos.
     */
    public Integer countPageReport(UseCaseConf useCaseConf, Integer bookId,
	    int folderId) throws RPRegisterException {
	Integer result = null;
	Transaction tran = null;
	String entity = useCaseConf.getEntidadId();
	HibernateUtil hibernateUtil = new HibernateUtil();
	try {

	    Validator.validate_String_NotNull_LengthMayorZero(useCaseConf.getSessionID(),
		    ValidationException.ATTRIBUTE_SESSION);
	    Validator.validate_Integer(bookId, ValidationException.ATTRIBUTE_BOOK);

	    Session session = hibernateUtil.currentSession(entity);
	    tran = session.beginTransaction();
	    // Recuperamos la sesión
	    CacheBag cacheBag =
		    CacheFactory.getCacheInterface().getCacheEntry(useCaseConf.getSessionID());
	    // Es necesario tener el libro abierto para consultar su contenido.
	    if (!cacheBag.containsKey(bookId)) {
		throw new BookException(BookException.ERROR_BOOK_NOT_OPEN);
	    }
	    
	    docsDao = (DocumentDAO) appContext.getBean("documentDAO");
	    AxPKById axPKByIdDoc = new AxPKById();
	    axPKByIdDoc.setFdrId(folderId);
	    axPKByIdDoc.setType(String.valueOf(bookId));
	    result = docsDao.countPageReport(axPKByIdDoc);

	    hibernateUtil.commitTransaction(tran);
	}
	catch (BookException bookException) {
	    hibernateUtil.rollbackTransaction(tran);
	    LOG.error(ErrorConstants.GET_REGISTER_DOCUMENTS_ERROR_MESSAGE, bookException);
	    throw new RPRegisterException(RPRegisterErrorCode.GET_REGISTER_DOCUMENTS_ERROR,
		    ErrorConstants.GET_REGISTER_DOCUMENTS_ERROR_MESSAGE, bookException);
	}
	catch (SessionException sessionException) {
	    hibernateUtil.rollbackTransaction(tran);
	    LOG.error(ErrorConstants.GET_REGISTER_DOCUMENTS_ERROR_MESSAGE, sessionException);
	    throw new RPRegisterException(RPRegisterErrorCode.GET_REGISTER_DOCUMENTS_ERROR,
		    ErrorConstants.GET_REGISTER_DOCUMENTS_ERROR_MESSAGE, sessionException);
	}
	catch (ValidationException validationException) {
	    LOG.error(ErrorConstants.GET_REGISTER_DOCUMENTS_ERROR_MESSAGE, validationException);
	    throw new RPRegisterException(RPRegisterErrorCode.GET_REGISTER_DOCUMENTS_ERROR,
		    ErrorConstants.GET_REGISTER_DOCUMENTS_ERROR_MESSAGE, validationException);
	}
	catch (Exception exception) {
	    hibernateUtil.rollbackTransaction(tran);
	    LOG.error(ErrorConstants.GET_REGISTER_DOCUMENTS_ERROR_MESSAGE, exception);
	    throw new RPRegisterException(RPRegisterErrorCode.GET_REGISTER_DOCUMENTS_ERROR,
		    ErrorConstants.GET_REGISTER_DOCUMENTS_ERROR_MESSAGE, exception);
	}
	finally {
	    hibernateUtil.closeSession(entity);
	}
	return result;
    }
    
    /**
     * Obtiene el numero de acuses de un registro.
     * 
     * @param useCaseConf
     *            - Datos de conexión del usuario.
     * @param bookId
     *            - Id. del libro.
     * @param folderId
     *            - Id. del registro.
     * @param idpage
     *            - Id. pagina.
     * @param numAcuse
     *            - Numero de acuse.         
     * @throws RPRegisterException
     *             Si se ha producido un error al obtener los documentos anexos.
     */
    public void updateFlag(UseCaseConf useCaseConf, Integer bookId,
	    int folderId, int idpage, Integer numAcuse) throws RPRegisterException {

	Transaction tran = null;
	String entity = useCaseConf.getEntidadId();
	HibernateUtil hibernateUtil = new HibernateUtil();
	try {

	    Validator.validate_String_NotNull_LengthMayorZero(useCaseConf.getSessionID(),
		    ValidationException.ATTRIBUTE_SESSION);
	    Validator.validate_Integer(bookId, ValidationException.ATTRIBUTE_BOOK);

	    Session session = hibernateUtil.currentSession(entity);
	    tran = session.beginTransaction();
	    // Recuperamos la sesión
	    CacheBag cacheBag =
		    CacheFactory.getCacheInterface().getCacheEntry(useCaseConf.getSessionID());
	    // Es necesario tener el libro abierto para consultar su contenido.
	    if (!cacheBag.containsKey(bookId)) {
		throw new BookException(BookException.ERROR_BOOK_NOT_OPEN);
	    }
	    
	    docsDao = (DocumentDAO) appContext.getBean("documentDAO");
	    AxPKById axPKByIdDoc = new AxPKById();
	    axPKByIdDoc.setFdrId(folderId);
	    axPKByIdDoc.setType(String.valueOf(bookId));
	    docsDao.updateFlag(axPKByIdDoc, idpage, numAcuse);
	    hibernateUtil.commitTransaction(tran);
	}
	catch (BookException bookException) {
	    hibernateUtil.rollbackTransaction(tran);
	    LOG.error(ErrorConstants.UPDATE_REGISTER_DOCUMENTS_ERROR_MESSAGE, bookException);
	    throw new RPRegisterException(RPRegisterErrorCode.UPDATE_REGISTER_DOCUMENTS_ERROR,
		    ErrorConstants.UPDATE_REGISTER_DOCUMENTS_ERROR_MESSAGE, bookException);
	}
	catch (SessionException sessionException) {
	    hibernateUtil.rollbackTransaction(tran);
	    LOG.error(ErrorConstants.UPDATE_REGISTER_DOCUMENTS_ERROR_MESSAGE, sessionException);
	    throw new RPRegisterException(RPRegisterErrorCode.UPDATE_REGISTER_DOCUMENTS_ERROR,
		    ErrorConstants.UPDATE_REGISTER_DOCUMENTS_ERROR_MESSAGE, sessionException);
	}
	catch (ValidationException validationException) {
	    LOG.error(ErrorConstants.UPDATE_REGISTER_DOCUMENTS_ERROR_MESSAGE, validationException);
	    throw new RPRegisterException(RPRegisterErrorCode.UPDATE_REGISTER_DOCUMENTS_ERROR,
		    ErrorConstants.UPDATE_REGISTER_DOCUMENTS_ERROR_MESSAGE, validationException);
	}
	catch (Exception exception) {
	    hibernateUtil.rollbackTransaction(tran);
	    LOG.error(ErrorConstants.UPDATE_REGISTER_DOCUMENTS_ERROR_MESSAGE, exception);
	    throw new RPRegisterException(RPRegisterErrorCode.GET_REGISTER_DOCUMENTS_ERROR,
		    ErrorConstants.UPDATE_REGISTER_DOCUMENTS_ERROR_MESSAGE, exception);
	}
	finally {
	    hibernateUtil.closeSession(entity);
	}
    }
}
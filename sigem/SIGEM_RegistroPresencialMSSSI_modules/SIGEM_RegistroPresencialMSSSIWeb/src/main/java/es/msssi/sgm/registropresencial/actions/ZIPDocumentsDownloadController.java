/*
 * Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
 * Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
 * Solo podrá usarse esta obra si se respeta la Licencia. 
 * Puede obtenerse una copia de la Licencia en: 
 * http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
 * Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
 * Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
 */

package es.msssi.sgm.registropresencial.actions;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.apache.log4j.Logger;
import org.primefaces.model.DefaultStreamedContent;
import org.primefaces.model.StreamedContent;

import com.ieci.tecdoc.common.exception.BookException;
import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.ValidationException;
import com.ieci.tecdoc.common.invesicres.ScrRegstate;
import com.ieci.tecdoc.common.isicres.AxDoch;
import com.ieci.tecdoc.common.isicres.AxPageh;
import com.ieci.tecdoc.isicres.usecase.UseCaseConf;
import com.ieci.tecdoc.isicres.usecase.book.BookUseCase;

import es.msssi.sgm.registropresencial.beans.WebParameter;
import es.msssi.sgm.registropresencial.beans.ibatis.Axdoch;
import es.msssi.sgm.registropresencial.beans.ibatis.Axpageh;
import es.msssi.sgm.registropresencial.businessobject.RegisterDocumentsBo;
import es.msssi.sgm.registropresencial.errors.ErrorConstants;
import es.msssi.sgm.registropresencial.errors.RPRegisterException;
import es.msssi.sgm.registropresencial.errors.RPRegistralExchangeErrorCode;
import es.msssi.sgm.registropresencial.errors.RPRegistralExchangeException;
import es.msssi.sgm.registropresencial.utils.KeysRP;

/**
 * Clase que implementa la descarga de documentos de un registro.
 * 
 * @author cmorenog
 * 
 */
public class ZIPDocumentsDownloadController extends GenericActions {

    private static final long serialVersionUID = 1L;
    private static final Logger LOG = Logger.getLogger(ZIPDocumentsDownloadController.class
	    .getName());
    /** fichero. */
    private StreamedContent file;
    private ScrRegstate book;
    private BookUseCase bookUseCase = null;
    private RegisterDocumentsBo registerDocumentsBo;

    private String fld1 = null;
    private Integer idRegister = null;
    private static String TMPDIR;

    static {
	TMPDIR =
		(String) WebParameter.getEntryParameter("PATH_REPO")
			+ (String) WebParameter.getEntryParameter("PathTmp");
    }

    /**
     * constructor.
     */
    public ZIPDocumentsDownloadController() {
	bookUseCase = new BookUseCase();
    }

    /**
     * devuelve el contenido del documento a descargar.
     * 
     * @return Contenido.
     * @throws SessionException
     *             errores de sesión.
     * @throws RPRegistralExchangeException
     *             error de descarga.
     */
    public StreamedContent getFileDownload() throws SessionException, RPRegistralExchangeException {
	File tempZipFile;
	try {
	    if (idRegister == null) {
		throw new SessionException(SessionException.ERROR_SESSION_EXPIRED);
	    }
	    init();
	    book =
		    (ScrRegstate) facesContext.getExternalContext().getSessionMap()
			    .get(KeysRP.J_BOOK);
	    if (book == null) {
		throw new SessionException(SessionException.ERROR_SESSION_EXPIRED);
	    }

	    List<AxDoch> listDocuments = getRegisterAttachedDocuments(idRegister);

	    if (listDocuments != null){
        	    tempZipFile = File.createTempFile("reg", ".zip", new File(TMPDIR));
        	    LOG.info("Fichero temporal:" + tempZipFile.getAbsolutePath());
        
        	    FileOutputStream fileObject = new FileOutputStream(tempZipFile);
        	    ZipOutputStream zipFile = new ZipOutputStream(fileObject);
        	    for (AxDoch doc : listDocuments) {
        		docToZip(doc, useCaseConf, book.getIdocarchhdr().getId(), zipFile);
        	    }
        	    zipFile.close();
        	    file =
        		    new DefaultStreamedContent(new FileInputStream(tempZipFile), "application/zip",
        			    "docs_e_" + fld1 +".zip");
        	    tempZipFile.delete();
	    }
	}
	catch (FileNotFoundException e) {
	    LOG.error(RPRegistralExchangeErrorCode.DOWNLOADFILE_ERROR_MESSAGE);
	    throw new RPRegistralExchangeException(
		    RPRegistralExchangeErrorCode.DOWNLOADFILE_ERROR_MESSAGE,
		    ErrorConstants.DOWNLOADFILE_ERROR_MESSAGE);
	}
	catch (ValidationException e) {
	    LOG.error(RPRegistralExchangeErrorCode.DOWNLOADFILE_ERROR_MESSAGE);
	    throw new RPRegistralExchangeException(
		    RPRegistralExchangeErrorCode.DOWNLOADFILE_ERROR_MESSAGE,
		    ErrorConstants.DOWNLOADFILE_ERROR_MESSAGE);
	}
	catch (BookException e) {
	    LOG.error(RPRegistralExchangeErrorCode.DOWNLOADFILE_ERROR_MESSAGE);
	    throw new RPRegistralExchangeException(
		    RPRegistralExchangeErrorCode.DOWNLOADFILE_ERROR_MESSAGE,
		    ErrorConstants.DOWNLOADFILE_ERROR_MESSAGE);
	}
	catch (IOException e) {
	    LOG.error(RPRegistralExchangeErrorCode.DOWNLOADFILE_ERROR_MESSAGE);
	    throw new RPRegistralExchangeException(
		    RPRegistralExchangeErrorCode.DOWNLOADFILE_ERROR_MESSAGE,
		    ErrorConstants.DOWNLOADFILE_ERROR_MESSAGE);
	}
	return file;
    }

    /**
     * Guarda los datos a un fichero.
     * 
     * @param inputBase64String
     *            Doc .
     * @param outFileName
     *            Nombre del fichero.
     * @throws IOException.
     */
    public void docToZip(AxDoch doc, UseCaseConf useCaseConf, Integer idBook, ZipOutputStream zipFile)
	    throws IOException, ValidationException, BookException, SessionException {

	byte[] content;
	
	ZipEntry zipEntry = null;

	for (AxPageh page : (List<AxPageh>)doc.getPages()) {
	    content =
		    bookUseCase.getFile(useCaseConf, idBook, doc.getFdrId(), doc.getId(),
			    page.getId());
	    try {
		zipEntry = new ZipEntry(page.getName());
		zipEntry.setSize(content.length);
		zipFile.putNextEntry(zipEntry);
		zipFile.write(content);
		zipFile.closeEntry();
	    }
	    catch (IOException e) {
	    }
	}
    }

    /**
     * Obtiene los documentos asociados a un registro.
     */
    public List<AxDoch> getRegisterAttachedDocuments(Integer idRegister) {
	List<AxDoch> listDocuments = null;
	if (registerDocumentsBo == null) {
	    registerDocumentsBo = new RegisterDocumentsBo();
	}
	try {
	    LOG.info("Cargando documentos asociados al registro " + idRegister);
	    listDocuments = new ArrayList<AxDoch>();
	    listDocuments =
		    registerDocumentsBo.getDocuments(useCaseConf, book.getId(),
			    idRegister, false);

	}
	catch (RPRegisterException rPRegisterException) {
	    LOG.error(ErrorConstants.LOAD_INPUT_REGISTER_ERROR_MESSAGE, rPRegisterException);
	}
	return listDocuments;
    }

    /**
     * @return the idRegister
     */
    public Integer getIdRegister() {
	return idRegister;
    }

    /**
     * @param idRegister
     *            the idRegister to set
     */
    public void setIdRegister(Integer idRegister) {
	this.idRegister = idRegister;
    }

    /**
     * @return the fld1
     */
    public String getFld1() {
	return fld1;
    }

    /**
     * @param fld1
     *            the fld1 to set
     */
    public void setFld1(String fld1) {
	this.fld1 = fld1;
    }

}

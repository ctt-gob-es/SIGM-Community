package es.dipucr.tecdoc.isicres.servlets.core;

import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tecdoc.sgm.core.exception.SigemException;
import ieci.tecdoc.sgm.core.services.LocalizadorServicios;
import ieci.tecdoc.sgm.core.services.dto.Entidad;
import ieci.tecdoc.sgm.core.services.entidades.EntidadesException;
import ieci.tecdoc.sgm.core.services.entidades.ServicioEntidades;
import ieci.tecdoc.sgm.core.services.gestioncsv.CSVException;
import ieci.tecdoc.sgm.core.services.gestioncsv.InfoDocumentoCSV;
import ieci.tecdoc.sgm.core.services.gestioncsv.ServicioGestionCSV;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.security.GeneralSecurityException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.cert.X509Certificate;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.hibernate.HibernateException;
import net.sf.hibernate.Session;
import net.sf.hibernate.Transaction;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import com.ieci.tecdoc.common.AuthenticationUser;
import com.ieci.tecdoc.common.entity.AxDochEntity;
import com.ieci.tecdoc.common.entity.AxPagehEntity;
import com.ieci.tecdoc.common.entity.dao.DBEntityDAOFactory;
import com.ieci.tecdoc.common.exception.BookException;
import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.ValidationException;
import com.ieci.tecdoc.common.isicres.AxPKById;
import com.ieci.tecdoc.common.isicres.AxSf;
import com.ieci.tecdoc.common.keys.HibernateKeys;
import com.ieci.tecdoc.common.utils.ISicresSaveQueries;
import com.ieci.tecdoc.idoc.flushfdr.FlushFdrDocument;
import com.ieci.tecdoc.idoc.flushfdr.FlushFdrFile;
import com.ieci.tecdoc.idoc.flushfdr.FlushFdrPage;
import com.ieci.tecdoc.isicres.desktopweb.Keys;
import com.ieci.tecdoc.isicres.desktopweb.utils.RBUtil;
import com.ieci.tecdoc.isicres.desktopweb.utils.RequestUtils;
import com.ieci.tecdoc.isicres.desktopweb.utils.ResponseUtils;
import com.ieci.tecdoc.isicres.usecase.UseCaseConf;
import com.ieci.tecdoc.isicres.usecase.book.BookUseCase;
import com.ieci.tecdoc.utils.HibernateUtil;
import com.ieci.tecdoc.utils.Validator;
import com.ieci.tecdoc.utils.cache.CacheBag;
import com.ieci.tecdoc.utils.cache.CacheFactory;
import com.lowagie.text.pdf.AcroFields;
import com.lowagie.text.pdf.PdfPKCS7;
import com.lowagie.text.pdf.PdfReader;

import es.dipucr.metadatos.beans.MetadatosDocumentoBean;
import es.dipucr.metadatos.bussinessobject.MetadatosBo;
import es.dipucr.sgm.registropresencial.bussinessobject.HistoricoDocumentosBO;
import es.ieci.tecdoc.fwktd.dm.business.util.MimeTypeUtils;
import es.ieci.tecdoc.fwktd.util.hash.HashUtils;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.DocumentoElectronicoAnexoContenidoVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.DocumentoElectronicoAnexoDatosFirmaVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.DocumentoElectronicoAnexoVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.IdentificadorDocumentoElectronicoAnexoVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.TipoDocumentoAnexoEnumVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.TipoValidezDocumentoAnexoEnumVO;
import es.msssi.sgm.registropresencial.businessobject.RegisterDocumentsBo;

public class FileUploadCompulsaDipucr extends HttpServlet implements Keys {

	private static final long serialVersionUID = 1L;

	private static final Logger LOGGER = Logger.getLogger(FileUploadCompulsaDipucr.class);

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

		Integer bookId = RequestUtils.parseRequestParameterAsInteger(request, "bookId");
		Integer folderId = RequestUtils.parseRequestParameterAsInteger(request, "folderId");
		
		// Obtenemos la sesión asociada al usuario.
		HttpSession session = request.getSession(false);

		UseCaseConf useCaseConf = (UseCaseConf) session.getAttribute(J_USECASECONF);

		PrintWriter writer = response.getWriter();

		try {
			guardarFicheros(useCaseConf, bookId, folderId);
			
			writer.println("<script type='text/javascript'>");			
			writer.println("window.opener.PF('statusDialog').hide();");			
			writer.println("window.close();");
			writer.println("</script>");
			 
		} catch (Exception e) {
			LOGGER.fatal("Error compulsando ficheros", e);
			ResponseUtils.generateJavaScriptCompulsa(writer, RBUtil.getInstance(useCaseConf.getLocale()).getProperty(Keys.I18N_ISICRESSRV_ERR_COMPUL_UPLOAD_OBJ));
		}
	}
	
	private List<FileItem> guardarFicheros(UseCaseConf useCaseConf, Integer bookId, Integer folderId) {
		List<FileItem> resultado = new ArrayList<FileItem>();
		String entidadId = useCaseConf.getEntidadId();
		
		List<String> nombreDocs = new ArrayList<String>();
		RegisterDocumentsBo registerDocumentsBo = new RegisterDocumentsBo();
		
		try {
			
			nombreDocs = getNombreAllDocuments(useCaseConf, bookId, folderId);
	
			for(String nombreDoc : nombreDocs){
				List<FlushFdrPage> pages = new ArrayList<FlushFdrPage>();
				Map<String, Object> documentos = new HashMap<String, Object>();

				String[] nombreDocSplit = nombreDoc.split("_");
			    int pageId = Integer.parseInt(nombreDocSplit[4]);
			    int docId = Integer.parseInt(nombreDocSplit[3]);
				    
			    FlushFdrFile documentDataToUpload = new FlushFdrFile();
			    
			    //Borrar el documento viejo.
			    AxPagehEntity axPagehEntity = new AxPagehEntity();
				AxPKById docPk = new AxPKById(bookId.toString(), folderId, pageId);
					
				axPagehEntity.load(docPk, entidadId);
					
				String nombre = axPagehEntity.getName();
				String fileExtension = FilenameUtils.getExtension(nombre);
			    String docName = FilenameUtils.getBaseName(nombre);
				    
			    File ficheroFirmado = new File(FileTemporaryManager.getInstance().getFileTemporaryPath(), nombreDoc + "." + fileExtension);
			    
			    MetadatosDocumentoBean metadatosDocumentoOriginal = new MetadatosDocumentoBean(bookId, folderId, pageId, axPagehEntity.getFileId(), useCaseConf.getEntidadId(), nombreDoc + "." + fileExtension);
			    MetadatosBo.getMetadatos(metadatosDocumentoOriginal);
			    
				if(null != ficheroFirmado && ficheroFirmado.exists()){
				    axPagehEntity.remove(entidadId);
					
					//Se crea el documento nuevo.			    
				    documentDataToUpload.setFileNameFis(nombre);
				    documentDataToUpload.setFileNameLog(nombre);
				    InputStream copiaStream = new FileInputStream(ficheroFirmado);
				    
				    documentDataToUpload.setBuffer(IOUtils.toByteArray(copiaStream));
				    documentDataToUpload.setExtension(fileExtension);
			
				    FlushFdrPage documentPageToUpload = new FlushFdrPage();
				    documentPageToUpload.setPageName(docName);
				    documentPageToUpload.setFile(documentDataToUpload);
				    pages.add(documentPageToUpload);
			
				    FlushFdrDocument documentToUpload = new FlushFdrDocument();
				    documentToUpload.setDocumentName(nombre);
				    documentToUpload.setPages(pages);

				    documentos.put(nombre, documentToUpload);

				    IdentificadorDocumentoElectronicoAnexoVO identificadorDocumentoElectronicoAnexoVO = new IdentificadorDocumentoElectronicoAnexoVO();
					identificadorDocumentoElectronicoAnexoVO.setIdLibro(bookId.longValue());
					identificadorDocumentoElectronicoAnexoVO.setIdRegistro(folderId.longValue());
					identificadorDocumentoElectronicoAnexoVO.setIdPagina(new Long(docId));

					String csv = getCSV(useCaseConf, ficheroFirmado);

				    DocumentoElectronicoAnexoVO documentoElectronico = getDocumentoElectronico(identificadorDocumentoElectronicoAnexoVO, nombreDoc, csv, fileExtension, ficheroFirmado, metadatosDocumentoOriginal);

				    DocumentoElectronicoAnexoVO documentoGuardado = registerDocumentsBo.saveDocumentCompulsa(useCaseConf, documentoElectronico, documentos);

				    AxSf axsf = bookUseCase.getBookFolder(useCaseConf, bookId, folderId.intValue());

				    insertaScrModifReg(useCaseConf, bookId, folderId, nombre, csv, documentoGuardado);
			        HistoricoDocumentosBO.historicoDocCompulsarEvent(useCaseConf, axsf.getAttributeValueAsString("fld1"), bookId, HistoricoDocumentosBO.TIPO_DOCUMENTO, nombre);

			        MetadatosDocumentoBean metadatosDocumentoNuevo = new MetadatosDocumentoBean(bookId, folderId, documentoGuardado.getId().getIdPagina().intValue(), documentoGuardado.getId().getIdFile().intValue(), entidadId, nombreDoc + "." + fileExtension);
			        MetadatosBo.actualizaMetadatosCompulsa(metadatosDocumentoOriginal, metadatosDocumentoNuevo, "" + axPagehEntity.getFileId(), fileExtension, csv);


				    if(null != copiaStream){
				    	copiaStream.close();
				    }

					ficheroFirmado.delete();					
				}
			}

		} catch (ISPACException e) {
			LOGGER.error("ERROR al subir los documentos firmados: " + e.getMessage(), e);
		} catch (FileNotFoundException e) {
			LOGGER.error("ERROR al subir los documentos firmados: " + e.getMessage(), e);
		} catch (IOException e) {
			LOGGER.error("ERROR al subir los documentos firmados: " + e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error("ERROR al subir los documentos firmados: " + e.getMessage(), e);
		}	    
		
		return resultado;
	}

	private void insertaScrModifReg(UseCaseConf useCaseConf, Integer bookId, Integer folderId, String nombre, String csv, DocumentoElectronicoAnexoVO documentoGuardado) {
		
		Transaction tran = null;
		String entidadId = useCaseConf.getEntidadId();
    	
    	try {
    		AxPagehEntity axPagehEntitiy = new AxPagehEntity();
    		Collection<?> pages = axPagehEntitiy.findByFileID(bookId, documentoGuardado.getId().getIdFile().intValue(), entidadId);
	    		
    		long idPag = 0;
		    for (Object page : pages) {
		    	AxPagehEntity pagina = new AxPagehEntity();
		    	pagina.load((AxPKById) page, entidadId);
		    	
		    	if(idPag < pagina.getId()){
		    		idPag = pagina.getId();
		    	}
		    }
		    
		    documentoGuardado.getId().setIdPagina(idPag);
    		
    	    String acuseJson = "[{\"iddoc\":" + documentoGuardado.getId().getId() + ",\"idpag\":" + idPag + ",\"csv\":\"" + csv + "\"}]";
		    
		    AxSf axsf = bookUseCase.getBookFolder(useCaseConf, bookId, folderId.intValue());
	        
	        CacheBag cacheBag = CacheFactory.getCacheInterface().getCacheEntry(useCaseConf.getSessionID());
			AuthenticationUser user = (AuthenticationUser) cacheBag.get(HibernateKeys.HIBERNATE_Iuseruserhdr);
			
			Integer updateAuditId = new Integer(DBEntityDAOFactory.getCurrentDBEntityDAO().getNextIdForScrModifreg( user.getId(), entidadId));
			
			int scrOficId = 0;
			if(StringUtils.isNumeric(axsf.getAttributeValueAsString("fld5"))){
				scrOficId = Integer.parseInt(axsf.getAttributeValueAsString("fld5"));
			}
			
			Session session = HibernateUtil.currentSession(entidadId);    	    
    	    tran = session.beginTransaction();
    	    
	        ISicresSaveQueries.saveScrModifreg(session, updateAuditId, user.getName(), new Date(), axsf.getAttributeValueAsString("fld1"), 1004, bookId.intValue(), scrOficId, 0);
	        
	        if(session.isOpen()){
				HibernateUtil.commitTransaction(tran);
			}
	        
			DBEntityDAOFactory.getCurrentDBEntityDAO().insertAudit( updateAuditId, "", acuseJson, entidadId);
			
		} catch (HibernateException e){
			HibernateUtil.rollbackTransaction(tran);
			LOGGER.error("ERROR al insertar el histórico de modificación del registro. " + e.getMessage(), e);
		} catch (SQLException e) {
			HibernateUtil.rollbackTransaction(tran);
			LOGGER.error("ERROR al insertar el histórico de modificación del registro. " + e.getMessage(), e);
		} catch (Exception e) {
			HibernateUtil.rollbackTransaction(tran);
			LOGGER.error("ERROR al insertar el histórico de modificación del registro. " + e.getMessage(), e);
		} finally {
			  HibernateUtil.closeSession(entidadId);
		}
	}

	private String getCSV(UseCaseConf useCaseConf, File ficheroFirmar) {
		String csv = "";
		
		try{
			ServicioGestionCSV servicioGestionCSV = LocalizadorServicios.getServicioGestionCSV();
			ServicioEntidades servicioEntidades = LocalizadorServicios.getServicioEntidades();
			
			// Generar el CSV
			ieci.tecdoc.sgm.core.services.entidades.Entidad entidadAdm = servicioEntidades.obtenerEntidad(useCaseConf.getEntidadId());
			Entidad entidad = new Entidad();
			entidad.setIdentificador(useCaseConf.getEntidadId());
			entidad.setNombreCorto(entidadAdm.getNombreCorto());
			entidad.setNombreLargo(entidadAdm.getNombreLargo());
			
			InfoDocumentoCSV infoDocumento = servicioGestionCSV.getInfoDocumentoByNombre(entidad, ficheroFirmar.getName());
			
			if( null != infoDocumento && null != infoDocumento.getCsv()){
				csv = infoDocumento.getCsv();
			}

		} catch(EntidadesException e){
			LOGGER.error("ERROR al recuperar el contenido del documento " + ficheroFirmar.getName() + ". " + e.getMessage(), e);
		} catch (CSVException e) {
			LOGGER.error("ERROR al recuperar el contenido del documento " + ficheroFirmar.getName() + ". " + e.getMessage(), e);
		} catch (SigemException e) {
			LOGGER.error("ERROR al recuperar el contenido del documento " + ficheroFirmar.getName() + ". " + e.getMessage(), e);
		} catch (Exception e) {
			LOGGER.error("ERROR al recuperar el contenido del documento " + ficheroFirmar.getName() + ". " + e.getMessage(), e);
		}
		
		return csv;
	}

	public DocumentoElectronicoAnexoVO getDocumentoElectronico(IdentificadorDocumentoElectronicoAnexoVO identificadorDocumentoElectronicoAnexoVO, String nombre, String csv, String fileExtension, File ficheroFirmado, MetadatosDocumentoBean metadatosDocumentoBean) {
		
		DocumentoElectronicoAnexoVO documentoElectronico = new DocumentoElectronicoAnexoVO();
		try{
			documentoElectronico.setId(identificadorDocumentoElectronicoAnexoVO);
			
		    documentoElectronico.setName(nombre + "." + fileExtension);
		    documentoElectronico.setExtension(fileExtension);
		    documentoElectronico.setMimeType(MimeTypeUtils.getExtensionMimeType(fileExtension));
			
			String codeName = ("codeName" + documentoElectronico.getName());
			codeName = StringUtils.abbreviate(codeName, 21);
			documentoElectronico.setCodeName(codeName);
			
			//Contenido del documento
			DocumentoElectronicoAnexoContenidoVO contenido = new DocumentoElectronicoAnexoContenidoVO();
			FileInputStream fis = new FileInputStream(ficheroFirmado);
			contenido.setContent(IOUtils.toByteArray(fis));
			documentoElectronico.setContenido(contenido);
			
			documentoElectronico.setTipoDocumentoAnexo(TipoDocumentoAnexoEnumVO.FORMULARIO);
			documentoElectronico.setTipoValidez(TipoValidezDocumentoAnexoEnumVO.COPIA_COMPULSADA);
			
			FileInputStream fis2 = new FileInputStream(ficheroFirmado);
			
			String hashDocumento = HashUtils.generateHashBase64(fis2, HashUtils.SHA1_ALGORITHM);
				
			List<PdfPKCS7> listaFirmas = verifySignatures(ficheroFirmado);
			PdfPKCS7 firma = listaFirmas.get(0);
			DocumentoElectronicoAnexoDatosFirmaVO datosFirmaDocumentoFirma = new DocumentoElectronicoAnexoDatosFirmaVO();
			
			X509Certificate cert = (X509Certificate) firma.getSigningCertificate();
			datosFirmaDocumentoFirma.setCertificado(cert.toString());
		
//			datosFirmaDocumentoFirma.setAlgFirma(pkcs7.getDigestAlgorithm());
			datosFirmaDocumentoFirma.setAlgFirma("01");

			// Si la firma es XAdES o alguna externa va aquí, si va en el propio documento (como en PAdES) aquí nada.
			// Se puede utilizar para meter los metadato que nos de la gana.
			datosFirmaDocumentoFirma.setFirma("<FIRMA><METADATOS></METADATOS><CSV>" + csv + "</CSV></FIRMA>");
//			datosFirmaDocumentoFirma.setFormatoFirma("PAdES");
			datosFirmaDocumentoFirma.setFormatoFirma("02");
			
//			datosFirmaDocumentoFirma.setHashAlg(pkcs7.getHashAlgorithm());
			datosFirmaDocumentoFirma.setHashAlg("03");
			datosFirmaDocumentoFirma.setHash(hashDocumento);		
			
//			datosFirmaDocumentoFirma.setOcspValidation(pkcs7.getOcsp().getSignatureAlgName());
			datosFirmaDocumentoFirma.setSelloTiempo(new SimpleDateFormat("dd/MM/yyyy HH:mm:ss").format(firma.getSignDate().getTime()));
	
			documentoElectronico.setDatosFirma(datosFirmaDocumentoFirma);
			
			metadatosDocumentoBean.setFechaCaptura(firma.getSignDate().getTime());
			
			//[Dipucr-Manu] Para cuando aceptemos documetos con más de una firma.
//			documentoElectronico.setDatosFirma(datosFirmaDocumento);
//			documentoElectronico.setFirmas(getFirmas(identificadorDocumentoElectronicoAnexoVO, nombre, hashDocumento, ficheroFirmado));
			
			if(null != fis){
				fis.close();				
			}
			if(null != fis2){
				fis2.close();				
			}
			
		} catch (IOException e){
			LOGGER.error("ERROR al almacenar el documento firmado bookId: " + documentoElectronico.getId().getIdLibro() + ", folderId: " + documentoElectronico.getId().getIdRegistro()  + ", fileId: " + documentoElectronico.getId().getIdFile() + ", nombre doc: " + nombre + ". " + e.getMessage(), e);
		} catch (NoSuchAlgorithmException e) {
			LOGGER.error("ERROR al almacenar el documento firmado bookId: " + documentoElectronico.getId().getIdLibro() + ", folderId: " + documentoElectronico.getId().getIdRegistro()  + ", fileId: " + documentoElectronico.getId().getIdFile() + ", nombre doc: " + nombre + ". " + e.getMessage(), e);
		} catch (NoSuchProviderException e) {
			LOGGER.error("ERROR al almacenar el documento firmado bookId: " + documentoElectronico.getId().getIdLibro() + ", folderId: " + documentoElectronico.getId().getIdRegistro()  + ", fileId: " + documentoElectronico.getId().getIdFile() + ", nombre doc: " + nombre + ". " + e.getMessage(), e);
		} catch (GeneralSecurityException e) {
			LOGGER.error("ERROR al almacenar el documento firmado bookId: " + documentoElectronico.getId().getIdLibro() + ", folderId: " + documentoElectronico.getId().getIdRegistro()  + ", fileId: " + documentoElectronico.getId().getIdFile() + ", nombre doc: " + nombre + ". " + e.getMessage(), e);
		}
		
		return documentoElectronico;
	}
	
	private List<PdfPKCS7> verifySignatures(File ficheroFirmado) throws IOException, GeneralSecurityException {
		List<PdfPKCS7> pkcs7 = new ArrayList<PdfPKCS7>();
		FileInputStream fis = new FileInputStream(ficheroFirmado);
        PdfReader reader = new PdfReader(fis);
        
        AcroFields fields = reader.getAcroFields();
        ArrayList<?> names = fields.getSignatureNames();
		
        for (Object name : names) {
        	LOGGER.debug("===== " + (String)name + " =====");
			pkcs7.add(verifySignature(fields, (String)name));
		}
        reader.close();
        fis.close();
        
		return pkcs7;
	}
	
	private PdfPKCS7 verifySignature(AcroFields fields, String name) throws GeneralSecurityException, IOException {
		
		LOGGER.debug("Signature covers whole document: " + fields.signatureCoversWholeDocument(name));
		LOGGER.debug("Document revision: " + fields.getRevision(name) + " of " + fields.getTotalRevisions());
		
        PdfPKCS7 pkcs7 = fields.verifySignature(name);
        
        LOGGER.debug("Integrity check OK? " + pkcs7.verify());
        LOGGER.debug("Firmante: " + pkcs7.getSignName());
        LOGGER.debug("Fecha firma: " + new SimpleDateFormat("dd/MM/yyyy hh:mm:ss").format(pkcs7.getSignDate().getTime()));
        
        return pkcs7;
	}

	private List<String> getNombreAllDocuments(UseCaseConf useCaseConf, Integer bookId, int folderId) {
		List<String> nombreDocs = new ArrayList<String>();
		String entidadId = useCaseConf.getEntidadId();
    	
    	try {
    	    Validator.validate_String_NotNull_LengthMayorZero(useCaseConf.getSessionID(), ValidationException.ATTRIBUTE_SESSION);
    	    Validator.validate_Integer(bookId, ValidationException.ATTRIBUTE_BOOK);
    	    
   	    	AxDochEntity axDochEntity = new AxDochEntity();
   	    	Collection<?> docs = axDochEntity.findByFdrid(bookId, folderId, entidadId);
    	    	
   	    	for (Object axPKByIdDoc : docs) {
   	    		AxPagehEntity axPagehEntitiy = new AxPagehEntity();
   	    		Collection<?> pages = axPagehEntitiy.findByFdridDocid(bookId, folderId, ((AxPKById)axPKByIdDoc).getId(), entidadId);
    	    		
    		    for (Object page : pages) {
    		    	AxPagehEntity pagina = new AxPagehEntity();
    		    	pagina.load((AxPKById) page, entidadId);
    		    	nombreDocs.add(entidadId + "_" + bookId + "_" + pagina.getFdrId() + "_" + pagina.getDocId() + "_" + pagina.getId());
    		    }
    		}
    		
    	} catch (BookException bookException) {
    	    LOGGER.error("ERROR al recuperar el nombre de los documentos: " + bookException.getMessage(), bookException);
    	} catch (SessionException sessionException) {
    	    LOGGER.error("ERROR al recuperar el nombre de los documentos: " + sessionException.getMessage(), sessionException);
    	} catch (ValidationException validationException) {
    		LOGGER.error("ERROR al recuperar el nombre de los documentos: " + validationException.getMessage(), validationException);
    	} catch (Exception exception) {
    	    LOGGER.error("ERROR al recuperar el nombre de los documentos: " + exception.getMessage(), exception);
    	}
    	
    	return nombreDocs;
    } 
}

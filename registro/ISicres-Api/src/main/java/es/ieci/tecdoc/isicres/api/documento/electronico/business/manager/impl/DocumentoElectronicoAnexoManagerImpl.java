package es.ieci.tecdoc.isicres.api.documento.electronico.business.manager.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.springframework.jdbc.support.incrementer.DataFieldMaxValueIncrementer;

import com.ieci.tecdoc.common.exception.BookException;
import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.ValidationException;
import com.ieci.tecdoc.common.isicres.AxSf;
import com.ieci.tecdoc.idoc.flushfdr.FlushFdrDocument;
import com.ieci.tecdoc.idoc.flushfdr.FlushFdrPage;
import com.ieci.tecdoc.isicres.session.folder.FolderDataSession;
import com.ieci.tecdoc.isicres.session.folder.FolderFileSession;
import com.ieci.tecdoc.isicres.session.folder.FolderSession;
import com.ieci.tecdoc.isicres.usecase.UseCaseConf;

import es.ieci.tecdoc.isicres.api.business.manager.RegistroManager;
import es.ieci.tecdoc.isicres.api.business.vo.DocumentoFisicoVO;
import es.ieci.tecdoc.isicres.api.business.vo.DocumentoRegistroVO;
import es.ieci.tecdoc.isicres.api.business.vo.IdentificadorRegistroVO;
import es.ieci.tecdoc.isicres.api.business.vo.PaginaDocumentoRegistroVO;
import es.ieci.tecdoc.isicres.api.business.vo.UsuarioVO;
import es.ieci.tecdoc.isicres.api.contextholder.IsicresContextHolder;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.dao.DocumentoElectronicoAnexoDAO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.manager.DocumentoElectronicoAnexoManager;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.ConfiguracionCreateDocumentoElectronicoAnexoVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.DocumentoElectronicoAnexoContenidoVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.DocumentoElectronicoAnexoDatosFirmaVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.DocumentoElectronicoAnexoVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.IdentificadorDocumentoElectronicoAnexoVO;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.util.ISPACConfiguration;


public class DocumentoElectronicoAnexoManagerImpl implements DocumentoElectronicoAnexoManager {
	
	private static final Logger LOGGER = Logger.getLogger(DocumentoElectronicoAnexoManagerImpl.class);

	protected RegistroManager registroManager;
	protected DocumentoElectronicoAnexoDAO documentoElectronicoAnexoDAO;
	protected DataFieldMaxValueIncrementer documentoElectronicoAnexoIncrementer;
	protected DataFieldMaxValueIncrementer documentoElectronicoAnexoDatosFirmaIncrementer;

	/* (non-Javadoc)
	 * @see es.ieci.tecdoc.isicres.api.documento.electronico.business.manager.DocumentoElectronicoAnexoManager#create(es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.DocumentoElectronicoAnexoVO, es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.ConfiguracionCreateDocumentoElectronicoAnexoVO)
	 */
	public DocumentoElectronicoAnexoVO create(DocumentoElectronicoAnexoVO documento,ConfiguracionCreateDocumentoElectronicoAnexoVO cfg) {

		// TODO mapear de VOs de DocumentoElectronico a documento de registro, obtener usuario de registro
		//el mapper tb seteará el nombre del documento documentoRegistro.setName(cfg.getClasificador());
		DocumentoRegistroVO documentoRegistro=null;

		documentoRegistro= new DocumentoRegistroVO();


		populateDocumentoRegistroVO(documento, cfg, documentoRegistro);

		//TODO setear el usuario en el contexto de la aplicación
		UsuarioVO usuarioRegistro=IsicresContextHolder.getContextoAplicacion().getUsuarioActual();


		IdentificadorRegistroVO idRegistro=documentoRegistro.getIdRegistro();
		//adjuntamos los documentos al registro
		documentoRegistro=registroManager.attachDocument(idRegistro, documentoRegistro, usuarioRegistro);


		//se obtienen los id para el documento y para la informacion de la firma
		Long idDocumento=documentoElectronicoAnexoIncrementer.nextLongValue();
		Long idDatosFirma=documentoElectronicoAnexoDatosFirmaIncrementer.nextLongValue();

		//seteamos el identificador del documento
		documento.getId().setId(idDocumento);

		// los documentos en isicres tienen paginas y estas paginas son los ficheros fisicos
		//queremos obtener el idPagina que se ha insertado, para eso buscamos por nombre
		String namePagina=documento.getName();
		Long idPagina=null;
		List paginasDocumento = documentoRegistro.getPaginas();
		for (Iterator iterator = paginasDocumento.iterator(); iterator.hasNext();) {
			PaginaDocumentoRegistroVO paginaIt = (PaginaDocumentoRegistroVO) iterator.next();
			if (paginaIt.getName().equals(namePagina)){
				idPagina=Long.parseLong(paginaIt.getId());
			}
		}

		documento.getId().setIdPagina(idPagina);
		documento.getDatosFirma().setIdAttachment(idDocumento);
		documento.getDatosFirma().setId(idDatosFirma);

		//si el IdAttachmentFirmado es distinto de vacio o nulo es q no es documento firma de ningun otro, es auto firmado
		//en caso contrario será seteado por el id del documento al que firma posteriormente
		if (null==documento.getDatosFirma().getIdAttachmentFirmado()){
			documento.getDatosFirma().setIdAttachmentFirmado(idDocumento);
		}
		//insertamos el documento anexo
		getDocumentoElectronicoAnexoDAO().save(documento);

		//insertamos las firmas, para ello se le indica que el id del documento firmado es el actual
		List<DocumentoElectronicoAnexoVO> firmas = documento.getFirmas();
		if (firmas!=null){
			for (Iterator iterator = firmas.iterator(); iterator.hasNext();) {
				DocumentoElectronicoAnexoVO documentoFirma = (DocumentoElectronicoAnexoVO) iterator
						.next();

				//seteamos a quien firma la firma
				documentoFirma.getDatosFirma().setIdAttachmentFirmado(documento.getId().getId());
				 if (documento.getName().indexOf(".") != -1){
				     cfg.setClasificador(documento.getName().substring(0, documento.getName().indexOf(".")));
				 } else {
				     cfg.setClasificador(documento.getName());
				 }
				create(documentoFirma,cfg);
				cfg.setClasificador(null);
			}
		}

		return documento;
	}
	
	public DocumentoElectronicoAnexoVO createDocumentCompulsa (UseCaseConf useCaseConf, DocumentoElectronicoAnexoVO documento, ConfiguracionCreateDocumentoElectronicoAnexoVO cfg, Map<String, Object> documentos) {

		// TODO mapear de VOs de DocumentoElectronico a documento de registro, obtener usuario de registro
		//el mapper tb seteará el nombre del documento documentoRegistro.setName(cfg.getClasificador());
		DocumentoRegistroVO documentoRegistro=null;

		documentoRegistro= new DocumentoRegistroVO();

		populateDocumentoRegistroVO(documento, cfg, documentoRegistro);

		FolderDataSession docGuardado = null;
		try {
			if(null != documentos){
				docGuardado = FolderSession.addPage(useCaseConf.getSessionID(), documento.getId().getIdLibro().intValue(), documento.getId().getIdRegistro().intValue(), documento.getId().getIdPagina().intValue(), documentos, new AxSf(), useCaseConf.getLocale(), useCaseConf.getEntidadId());
			}
		} catch (NumberFormatException e) {
			LOGGER.error("ERROR al almacenar el documento: " + e.getMessage(), e);
		} catch (BookException e) {
			LOGGER.error("ERROR al almacenar el documento: " + e.getMessage(), e);
		} catch (SessionException e) {
			LOGGER.error("ERROR al almacenar el documento: " + e.getMessage(), e);
		} catch (ValidationException e) {
			LOGGER.error("ERROR al almacenar el documento: " + e.getMessage(), e);
		}

		//se obtienen los id para el documento y para la informacion de la firma
		Long idDocumento=documentoElectronicoAnexoIncrementer.nextLongValue();
		Long idDatosFirma=documentoElectronicoAnexoDatosFirmaIncrementer.nextLongValue();

		documento.getId().setId(idDocumento);
		
		// [Josemi-UPNA] Bug por el que se graba el id de carpeta en lugar del idPage del documento en base de datos
		// 				Se asigna el valor correcto en el bloque try-catch siguiente
		//documento.getId().setIdPagina(documento.getId().getIdPagina());
		
		try{
			Map<?, ?> docs = docGuardado.getDocumentsUpdate();
			Iterator<?> docsClaves = (Iterator<?>) docs.keySet().iterator();
			String key = (String) docsClaves.next();
			FlushFdrDocument document = (FlushFdrDocument) docs.get(key);
			List<?> pages = document.getPages();
			
			documento.getId().setIdPagina(Long.parseLong(((FlushFdrPage)pages.get(0)).getFile().getPageID()));
			
			// Adaptación a Alfresco.
			try {
				Integer.parseInt(((FlushFdrPage)pages.get(0)).getFile().getFileID());
				// Si el fileId es de tipo entero, se mantiene el código original
				documento.getId().setIdFile(Long.decode(((FlushFdrPage)pages.get(0)).getFile().getFileID()));

			} catch (NumberFormatException e) {
				// El fileId no es entero. Se calcula el ID para Alfresco
				String fileIdAlfresco = calculateFileIDAlfresco(documento.getId().getIdRegistro().intValue(), Integer.parseInt(((FlushFdrPage)pages.get(0)).getFile().getPageID()));
				documento.getId().setIdFile(new Long(fileIdAlfresco));
			}

		} catch (Exception e){
			
		}
			
		documento.getDatosFirma().setIdAttachment(idDocumento);
		documento.getDatosFirma().setId(idDatosFirma);

		//si el IdAttachmentFirmado es distinto de vacio o nulo es q no es documento firma de ningun otro, es auto firmado
		//en caso contrario será seteado por el id del documento al que firma posteriormente
		if (null==documento.getDatosFirma().getIdAttachmentFirmado()){
			documento.getDatosFirma().setIdAttachmentFirmado(idDocumento);
		}
		//insertamos el documento anexo
		getDocumentoElectronicoAnexoDAO().save(documento);

		//insertamos las firmas, para ello se le indica que el id del documento firmado es el actual
		List<DocumentoElectronicoAnexoVO> firmas = documento.getFirmas();
		if (firmas!=null){
			for (Iterator<DocumentoElectronicoAnexoVO> iterator = firmas.iterator(); iterator.hasNext();) {
				DocumentoElectronicoAnexoVO documentoFirma = (DocumentoElectronicoAnexoVO) iterator.next();

				//seteamos a quien firma la firma
				documentoFirma.getDatosFirma().setIdAttachmentFirmado(documento.getId().getId());
				documentoFirma.setId(documento.getId());
				 if (documento.getName().indexOf(".") != -1){
				     cfg.setClasificador(documento.getName().substring(0, documento.getName().indexOf(".")));
				 } else {
				     cfg.setClasificador(documento.getName());
				 }
				 createDocumentCompulsa(useCaseConf, documentoFirma, cfg, null);
				 cfg.setClasificador(null);
			}
		}

		return documento;
	}
	
	protected void populateDocumentoRegistroVO(DocumentoElectronicoAnexoVO documento,ConfiguracionCreateDocumentoElectronicoAnexoVO cfg,DocumentoRegistroVO documentoRegistro){
		IdentificadorRegistroVO idRegistro= new IdentificadorRegistroVO();
		idRegistro.setIdLibro(documento.getId().getIdLibro().toString());
		idRegistro.setIdRegistro(documento.getId().getIdRegistro().toString());

		documentoRegistro.setIdRegistro(idRegistro);
		if (cfg.getClasificador() != null){
		    documentoRegistro.setName(cfg.getClasificador());
		}else {
		    if (documento.getName().indexOf(".") != -1){
			documentoRegistro.setName(documento.getName().substring(0, documento.getName().indexOf(".")));
		    }else {
			documentoRegistro.setName(documento.getName());
		    }
		}

		List paginas= new ArrayList<PaginaDocumentoRegistroVO>();
		PaginaDocumentoRegistroVO pagina= new PaginaDocumentoRegistroVO();
		String namePagina=documento.getName();
		pagina.setName(namePagina);

		DocumentoFisicoVO documentoFisico= new DocumentoFisicoVO();
		byte[] content=documento.getContenido().getContent();
		documentoFisico.setContent(content);
		String extension=documento.getExtension();
		documentoFisico.setExtension(extension);
		String location=documento.getContenido().getDocUID();
		documentoFisico.setLocation(location);
		documentoFisico.setExtension(extension);
		pagina.setDocumentoFisico(documentoFisico);
		paginas.add(pagina);

		documentoRegistro.setPaginas(paginas);

	}



	public DocumentoElectronicoAnexoVO retrieve(IdentificadorDocumentoElectronicoAnexoVO idDocumentoAnexo) {
		DocumentoElectronicoAnexoVO result=null;
		result=getDocumentoElectronicoAnexoDAO().get(idDocumentoAnexo);
		if (result!=null){
			List<DocumentoElectronicoAnexoVO> firmas = getDocumentoElectronicoAnexoDAO().getFirmas(result.getId().getId());
			result.setFirmas(firmas);
		}

		return result;
	}


	public DocumentoElectronicoAnexoVO getDocumentoFirmado(Long idDocumentoFirma){
		DocumentoElectronicoAnexoVO result=null;
		result=getDocumentoElectronicoAnexoDAO().getDocumentoFirmado(idDocumentoFirma);
		return result;
	}

	public List<DocumentoElectronicoAnexoVO> getDocumentosElectronicoAnexoByRegistro(
			Long idLibro, Long idRegistro) {
		return getDocumentoElectronicoAnexoDAO().getDocumentosElectronicoAnexoByRegistro(idLibro, idRegistro);
	}

	public RegistroManager getRegistroManager() {
		return registroManager;
	}

	public void setRegistroManager(RegistroManager registroManager) {
		this.registroManager = registroManager;
	}


	public DataFieldMaxValueIncrementer getDocumentoElectronicoAnexoIncrementer() {
		return documentoElectronicoAnexoIncrementer;
	}

	public void setDocumentoElectronicoAnexoIncrementer(
			DataFieldMaxValueIncrementer documentoElectronicoAnexoIncrementer) {
		this.documentoElectronicoAnexoIncrementer = documentoElectronicoAnexoIncrementer;
	}

	public DataFieldMaxValueIncrementer getDocumentoElectronicoAnexoDatosFirmaIncrementer() {
		return documentoElectronicoAnexoDatosFirmaIncrementer;
	}

	public void setDocumentoElectronicoAnexoDatosFirmaIncrementer(
			DataFieldMaxValueIncrementer documentoElectronicoAnexoDatosFirmaIncrementer) {
		this.documentoElectronicoAnexoDatosFirmaIncrementer = documentoElectronicoAnexoDatosFirmaIncrementer;
	}

	public DocumentoElectronicoAnexoDAO getDocumentoElectronicoAnexoDAO() {
		return documentoElectronicoAnexoDAO;
	}

	public void setDocumentoElectronicoAnexoDAO(
			DocumentoElectronicoAnexoDAO documentoElectronicoAnexoDAO) {
		this.documentoElectronicoAnexoDAO = documentoElectronicoAnexoDAO;
	}


	/* (non-Javadoc)
	 * @see es.ieci.tecdoc.isicres.api.documento.electronico.business.manager.DocumentoElectronicoAnexoManager#create(es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.DocumentoElectronicoAnexoVO, es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.ConfiguracionCreateDocumentoElectronicoAnexoVO)
	 */
	public DocumentoElectronicoAnexoVO create(DocumentoElectronicoAnexoVO documento,ConfiguracionCreateDocumentoElectronicoAnexoVO cfg,UsuarioVO usuarioRegistro) {

		// TODO mapear de VOs de DocumentoElectronico a documento de registro, obtener usuario de registro
		//el mapper tb seteará el nombre del documento documentoRegistro.setName(cfg.getClasificador());
		DocumentoRegistroVO documentoRegistro=null;

		documentoRegistro= new DocumentoRegistroVO();


		populateDocumentoRegistroVO(documento, cfg, documentoRegistro);

		IdentificadorRegistroVO idRegistro=documentoRegistro.getIdRegistro();
		//adjuntamos los documentos al registro
		documentoRegistro=registroManager.attachDocument(idRegistro, documentoRegistro, usuarioRegistro);


		//se obtienen los id para el documento y para la informacion de la firma
		Long idDocumento=documentoElectronicoAnexoIncrementer.nextLongValue();
		Long idDatosFirma=documentoElectronicoAnexoDatosFirmaIncrementer.nextLongValue();

		//seteamos el identificador del documento
		documento.getId().setId(idDocumento);

		// los documentos en isicres tienen paginas y estas paginas son los ficheros fisicos
		//queremos obtener el idPagina que se ha insertado, para eso buscamos por nombre
		String namePagina=documento.getName();
		Long idPagina = null;
		Long idFile = null;
		List paginasDocumento = documentoRegistro.getPaginas();
		for (Iterator iterator = paginasDocumento.iterator(); iterator.hasNext();) {
			PaginaDocumentoRegistroVO paginaIt = (PaginaDocumentoRegistroVO) iterator.next();
			if (paginaIt.getName().equals(namePagina)){
				idPagina=Long.parseLong(paginaIt.getId());
				
				// Adaptación a Alfresco. Si el conector no es de alfresco, se mantiene la anterior funcionalidad
				String connectorManager = null;
				try {
					connectorManager = ISPACConfiguration.getInstance().get(ISPACConfiguration.CONNECTOR_MANAGER);
				} catch (ISPACException e) {
					LOGGER.error(e);
				}
				if (connectorManager != null && (connectorManager.equalsIgnoreCase("alfresco") || 
												connectorManager.equalsIgnoreCase("alfrescoCMIS"))) {
					idFile = new Long(idDocumento);
				} else {	
					idFile = Long.parseLong(paginaIt.getDocumentoFisico().getLocation());
				}
				
			}
		}

		documento.getId().setIdPagina(idPagina);
		documento.getId().setIdFile(idFile);
		documento.getDatosFirma().setIdAttachment(idDocumento);
		documento.getDatosFirma().setId(idDatosFirma);

		//si el IdAttachmentFirmado es distinto de vacio o nulo es q no es documento firma de ningun otro, es auto firmado
		//en caso contrario será seteado por el id del documento al que firma posteriormente
		if (null==documento.getDatosFirma().getIdAttachmentFirmado()){
			documento.getDatosFirma().setIdAttachmentFirmado(idDocumento);
		}
		//insertamos el documento anexo
		getDocumentoElectronicoAnexoDAO().save(documento);

		//insertamos las firmas, para ello se le indica que el id del documento firmado es el actual
		List<DocumentoElectronicoAnexoVO> firmas = documento.getFirmas();
		if (firmas!=null){
			for (Iterator iterator = firmas.iterator(); iterator.hasNext();) {
				DocumentoElectronicoAnexoVO documentoFirma = (DocumentoElectronicoAnexoVO) iterator
						.next();

				//seteamos a quien firma la firma
				documentoFirma.getDatosFirma().setIdAttachmentFirmado(documento.getId().getId());
				 if (documento.getName().indexOf(".") != -1){
				     cfg.setClasificador(documento.getName().substring(0, documento.getName().indexOf(".")));
				 } else {
				     cfg.setClasificador(documento.getName());
				 }
				create(documentoFirma,cfg, usuarioRegistro);
				cfg.setClasificador(null);
			}
		}

		return documento;
	}

	public void copyDocumentosElectronicoAnexoByRegistro(Long idLibroOrigen,
		Long idRegistroOrigen, Long idLibroDestino, Long idRegistroDestino, UsuarioVO usuarioRegistro) {
	    
	    List<DocumentoElectronicoAnexoVO> documentosOrigen= 
		    getDocumentoElectronicoAnexoDAO()
		    	.getDocumentosElectronicoAnexoByRegistro(idLibroOrigen, idRegistroOrigen);
	    ConfiguracionCreateDocumentoElectronicoAnexoVO cfg=new ConfiguracionCreateDocumentoElectronicoAnexoVO();
		
		//seteamos el nombre de la carpeta/clasificador de sicres sobre el que se guardaran
		
		String clasificador = null;
		cfg.setClasificador(clasificador);
		byte[] content = null;
		UseCaseConf useCaseConf = new UseCaseConf(
			usuarioRegistro.getConfiguracionUsuario().getLocale());
		useCaseConf.setSessionID(usuarioRegistro.getConfiguracionUsuario().getSessionID());
		useCaseConf.setEntidadId(usuarioRegistro.getConfiguracionUsuario().getIdEntidad());
		
	    if (documentosOrigen != null){
		DocumentoElectronicoAnexoVO docDestino = null;
		DocumentoElectronicoAnexoContenidoVO anexo = null;
		DocumentoElectronicoAnexoDatosFirmaVO datosFirma = null;
		List<DocumentoElectronicoAnexoVO> listaAnexosFirma = null;

		for (DocumentoElectronicoAnexoVO docOrigen:documentosOrigen){
		    if (docOrigen.getDatosFirma().getIdAttachmentFirmado() == null || docOrigen.getDatosFirma().getIdAttachment().equals( docOrigen.getDatosFirma().getIdAttachmentFirmado())){
        		    try {
        			docDestino = new DocumentoElectronicoAnexoVO();
        			BeanUtils.copyProperties(docDestino, docOrigen);
        		    }
        		    catch (IllegalAccessException e1) {
        		    }
        		    catch (InvocationTargetException e1) {
        		    }
        		   
        
        		    try {
        			content= FolderFileSession.getFile(useCaseConf.getSessionID(), idLibroOrigen.intValue(),
                			idRegistroOrigen.intValue(), docOrigen.getId().getIdPagina().intValue(), useCaseConf.getEntidadId());
                		anexo =  new DocumentoElectronicoAnexoContenidoVO();
                		anexo.setContent(content);
                		docDestino.setContenido(anexo);
                		String extension=docOrigen.getName().substring(docOrigen.getName().lastIndexOf(".") + 1,
                			  docOrigen.getName().length());
                		  docDestino.setExtension(extension);
        		    }
        		    catch (ValidationException e) {
        		    }
        		    catch (BookException e) {
        		    }
        		    catch (SessionException e) {
        		    }
        		    if (docOrigen.getDatosFirma() != null){
                		    datosFirma = docOrigen.getDatosFirma();
                		    datosFirma.setId(null);
                		    datosFirma.setIdAttachment(null);
                		    datosFirma.setIdAttachmentFirmado(null);
                		    docDestino.setDatosFirma(datosFirma);
                		    DocumentoElectronicoAnexoVO  doc = retrieve(docOrigen.getId());
                		    if ( doc.getFirmas() != null){
                        		try {
                        		    listaAnexosFirma = new ArrayList<DocumentoElectronicoAnexoVO>();
                        		    DocumentoElectronicoAnexoVO anexoFirma1 = null;
                        		    for (DocumentoElectronicoAnexoVO anexoFirma: doc.getFirmas()){
                        			 try {
                        			     anexoFirma1 = new DocumentoElectronicoAnexoVO();
                        			     BeanUtils.copyProperties(anexoFirma1, anexoFirma);
                        			  }
                        			  catch (IllegalAccessException e1) {
                        			 }
                        			  catch (InvocationTargetException e1) {
                        			 }
        
                				 content=  FolderFileSession.getFile(useCaseConf.getSessionID(), idLibroOrigen.intValue(),
                				    	idRegistroOrigen.intValue(), anexoFirma.getId().getIdPagina().intValue(), useCaseConf.getEntidadId());
                				anexo = new DocumentoElectronicoAnexoContenidoVO();
                				anexo.setContent(content);
                        			anexoFirma1.setContenido(anexo);
                        			String extension=anexoFirma.getName().substring(anexoFirma.getName().lastIndexOf(".") + 1,
                        				anexoFirma.getName().length());
                        			anexoFirma1.setExtension(extension);
                        			IdentificadorDocumentoElectronicoAnexoVO id = new IdentificadorDocumentoElectronicoAnexoVO();
                        	     		    id.setIdLibro(idLibroDestino);
                        	     		    id.setIdRegistro(idRegistroDestino);
                        			anexoFirma1.setId(id);
                        			anexoFirma1.getDatosFirma().setId(null);
                        			anexoFirma1.getDatosFirma().setIdAttachment(null);
                        			anexoFirma1.getDatosFirma().setIdAttachmentFirmado(null);
                        			listaAnexosFirma.add(anexoFirma1);
                        			
                        		    }
                        		    docDestino.setFirmas(listaAnexosFirma);
                        		}
                        		catch (ValidationException e) {
                        		}
                        		catch (BookException e) {
                        		}
                			catch (SessionException e) {
                			}
                		    }
        		    }
        		    IdentificadorDocumentoElectronicoAnexoVO id = new IdentificadorDocumentoElectronicoAnexoVO();
             		    id.setIdLibro(idLibroDestino);
             		    id.setIdRegistro(idRegistroDestino);
             		    docDestino.setId(id);
        		    create(docDestino, cfg, usuarioRegistro);
        		}
		}
	    }
	}

	
	/** Se toma como identificador para Alfresco el fdrId seguido por un id de tres dígitos 
	 * 
	 * @param fdrId
	 * @param id Identificador de página del fichero
	 * @return identificador con formato para alfresco.
	 */
	private static String calculateFileIDAlfresco(int fdrId, int id) {
	
		String newFileId =Integer.toString(fdrId);
		String idAux = Integer.toString(id);
		for (int i=idAux.length(); i < 3 ;i++) {
			newFileId = newFileId + "0";
		}
		newFileId = newFileId + idAux;
		return newFileId;

	}



}


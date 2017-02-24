/*
* Copyright 2016 Ministerio de Sanidad, Servicios Sociales e Igualdad 
* Licencia con arreglo a la EUPL, Versión 1.1 o –en cuanto sean aprobadas por laComisión Europea– versiones posteriores de la EUPL (la «Licencia»); 
* Solo podrá usarse esta obra si se respeta la Licencia. 
* Puede obtenerse una copia de la Licencia en: 
* http://joinup.ec.europa.eu/software/page/eupl/licence-eupl 
* Salvo cuando lo exija la legislación aplicable o se acuerde por escrito, el programa distribuido con arreglo a la Licencia se distribuye «TAL CUAL», SIN GARANTÍAS NI CONDICIONES DE NINGÚN TIPO, ni expresas ni implícitas. 
* Véase la Licencia en el idioma concreto que rige los permisos y limitaciones que establece la Licencia. 
*/

package es.msssi.sigm.core.connector;

import ieci.tecdoc.sgm.registropresencial.autenticacion.Login;
import ieci.tecdoc.sgm.registropresencial.autenticacion.User;
import ieci.tecdoc.sgm.registropresencial.register.RegisterServicesUtil;

import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.apache.log4j.Logger;

import sigm.dao.dataaccess.domain.XtField;
import sigm.dao.dataaccess.service.SIGMServiceManager;
import sigm.dao.exception.DaoException;

import com.ieci.tecdoc.common.AuthenticationUser;
import com.ieci.tecdoc.common.entity.AxXfEntity;
import com.ieci.tecdoc.common.entity.dao.DBEntityDAOFactory;
import com.ieci.tecdoc.common.exception.BookException;
import com.ieci.tecdoc.common.exception.SecurityException;
import com.ieci.tecdoc.common.exception.SessionException;
import com.ieci.tecdoc.common.exception.TecDocException;
import com.ieci.tecdoc.common.exception.ValidationException;
import com.ieci.tecdoc.common.isicres.AxDoch;
import com.ieci.tecdoc.common.isicres.AxPageh;
import com.ieci.tecdoc.common.isicres.AxSf;
import com.ieci.tecdoc.common.isicres.AxSfQuery;
import com.ieci.tecdoc.common.isicres.AxSfQueryResults;
import com.ieci.tecdoc.common.isicres.AxXfPK;
import com.ieci.tecdoc.common.keys.HibernateKeys;
import com.ieci.tecdoc.idoc.flushfdr.FlushFdrField;
import com.ieci.tecdoc.isicres.session.folder.FolderFileSession;
import com.ieci.tecdoc.isicres.session.folder.FolderSession;
import com.ieci.tecdoc.isicres.session.security.SecuritySession;
import com.ieci.tecdoc.isicres.usecase.UseCaseConf;
import com.ieci.tecdoc.isicres.usecase.book.BookUseCase;
import com.ieci.tecdoc.utils.cache.CacheBag;
import com.ieci.tecdoc.utils.cache.CacheFactory;

import core.tools.hasher.Hasher;
import es.ieci.tecdoc.fwktd.sir.core.types.ContentTypeEnum;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.TipoDocumentoAnexoEnumVO;
import es.ieci.tecdoc.isicres.api.documento.electronico.business.vo.TipoValidezDocumentoAnexoEnumVO;
import es.msssi.sgm.registropresencial.beans.InputRegisterBean;
import es.msssi.sgm.registropresencial.beans.OutputRegisterBean;
import es.msssi.sgm.registropresencial.beans.SearchInputRegisterBean;
import es.msssi.sgm.registropresencial.beans.SearchOutputRegisterBean;
import es.msssi.sgm.registropresencial.businessobject.BooksBo;
import es.msssi.sgm.registropresencial.businessobject.InputRegisterBo;
import es.msssi.sgm.registropresencial.businessobject.OutputRegisterBo;
import es.msssi.sgm.registropresencial.errors.RPBookException;
import es.msssi.sgm.registropresencial.errors.RPGenericException;
import es.msssi.sgm.registropresencial.errors.RPInputRegisterException;
import es.msssi.sgm.registropresencial.errors.RPOutputRegisterException;
import es.msssi.sigm.core.exception.SigmWSException;
import es.msssi.sigm.core.util.Constants;
import es.msssi.sigm.core.util.DocElectronicoUtil;
import es.msssi.sigm.core.util.SigmUtil;
import es.msssi.sigm.ws.beans.Acuse;
import es.msssi.sigm.ws.beans.Fichero;
import es.msssi.sigm.ws.beans.FullInfoRegister;
import es.msssi.sigm.ws.beans.PeticionBusqueda;
import es.msssi.sigm.ws.beans.PeticionDetalle;
import es.msssi.sigm.ws.beans.PeticionGenerarAcuse;
import es.msssi.sigm.ws.beans.Registro;
import es.msssi.sigm.ws.beans.jaxb.solicitud.ElementoDatoExtendido;
import es.msssi.sigm.ws.beans.jaxb.solicitud.ElementoDatos;
import es.msssi.sigm.ws.beans.jaxb.solicitud.ElementoFichero;
import es.msssi.sigm.ws.beans.jaxb.solicitud.TypeAlgorithm;
 
public class SigmConnector {
	private static Logger log = Logger.getLogger(SigmConnector.class.getName());
	
	public void adjuntarDocumento(String user, String pass, int idBook, String numeroRegistro,
			String nombreDocumento, byte[] contenidoDocumento) throws SigmWSException {
		
	    User applicationUser = SigmUtil.getUser(user, pass,Constants.SIGEM_LOCALE); 
	    UseCaseConf useCaseConf = SigmUtil.getUseCaseConf(user,pass, Constants.SIGEM_ENTIDAD_MSSSI, Constants.SIGEM_LOCALE);
	    		
	    String sessionID = null;
	    BooksBo bo = null;	
	    
	
	    try {
		
	    	sessionID = Login.login(applicationUser, Constants.SIGEM_ENTIDAD_MSSSI);
			
	    	useCaseConf.setSessionID(sessionID);
			
	    	bo = new BooksBo();
			
	    	bo.openBook(useCaseConf, idBook);				
			
	    	// Recuperar FDRID
			Integer fdrid = null;
			fdrid = SigmUtil.getFdrdid(useCaseConf, idBook, numeroRegistro);
			if(fdrid == null)
				throw new SigmWSException("err.sigm.registro.noValid", new String[]{numeroRegistro});

			Map<String, Object> createAttachedDocumentMap = SigmUtil.createAttachedDocumentMap(nombreDocumento, contenidoDocumento);		
			
			// Adjuntar documento al registro
	    	FolderSession.addDocument(sessionID, idBook, fdrid, createAttachedDocumentMap, new AxSf(), new Locale(Constants.SIGEM_LOCALE), Constants.SIGEM_ENTIDAD_MSSSI);

	    } catch (SecurityException e) {
	    	throw new SigmWSException("err.sigm.security", e);
	    } catch (ValidationException e) {
	    	throw new SigmWSException("err.sigm.validation", e);
	    } catch (RPGenericException e) {
	    	throw new SigmWSException("err.sigm.rpgeneric", e);
	    } catch (RPBookException e) {
	    	throw new SigmWSException("err.sigm.rpbook", e);
	    } catch (BookException e) {
	    	throw new SigmWSException("err.sigm.book", e);
	    } catch (SessionException e) {
	    	throw new SigmWSException("err.sigm.session", e);
		}
		finally{
	    	try {
				bo.closeBook(useCaseConf, idBook);
				SecuritySession.logout(sessionID, Constants.SIGEM_ENTIDAD_MSSSI);
		    } catch (RPGenericException e) {
		    	throw new SigmWSException("err.sigm.rpgeneric", e);
		    } catch (RPBookException e) {
		    	throw new SigmWSException("err.sigm.rpbook", e);
		    } catch (SecurityException e) {
		    	throw new SigmWSException("err.sigm.security", e);
			}
		}
		
		
	}

	public byte[] recuperarDocumento(String userName, String userPassword, int idBook, String numeroRegistro, String nombreDocumento) throws SigmWSException {
		
		byte[] contentFile = null;
		
	    User applicationUser = SigmUtil.getUser(userName, userPassword,Constants.SIGEM_LOCALE); 
	    UseCaseConf useCaseConf = SigmUtil.getUseCaseConf(userName,userPassword, Constants.SIGEM_ENTIDAD_MSSSI, Constants.SIGEM_LOCALE);
	    		
	    String sessionID = null;
	    BooksBo bo = null;
	    
	    try {
	    	sessionID = Login.login(applicationUser, Constants.SIGEM_ENTIDAD_MSSSI);
	    	useCaseConf.setSessionID(sessionID);
	    	bo = new BooksBo();	
	    	bo.openBook(useCaseConf, idBook);			
	    	
//			CacheBag cacheBag = CacheFactory.getCacheInterface().getCacheEntry(
//					sessionID);
//			THashMap bookInformation = (THashMap) cacheBag.get(idBook);
//			ISicresAPerms aPerms = (ISicresAPerms) bookInformation
//					.get(ServerKeys.APERMS_USER);
//			
//			ISicresGenPerms genPerms = (ISicresGenPerms) cacheBag
//					.get("GenPermsUser");
//			
//    	    if (!genPerms.isCanShowDocuments())
//    	    	throw new SigmWSException("no permisos documentos");
			
			
//	    	AxSf axsfNew = new AxSf();
//			
//	    	axsfNew.addAttributeName(Constants.FLD1_FIELD);
//		    List searchFields = new ArrayList() ;
		    
//		    AxSfQueryField queryField = null;
		    
			//OFICINA DE REGISTRO
//		    queryField = new AxSfQueryField();
//		    queryField.setFldId(String.valueOf(SigmUtil.NUM_REGISTRO));
//		    queryField.setBookId(idBook);
//		    queryField.setValue(numeroRegistro);
//		    queryField.setOperator(Keys.QUERY_EQUAL_TEXT_VALUE);
//		    searchFields.add(queryField);
//		
//		    // Buscar registro en BBDD
//	    	InfoRegister[] infoRegister = RegisterServices.findFolder(applicationUser, idBook, searchFields, Constants.SIGEM_ENTIDAD_MSSSI);	    	
		
//	    	SigmUtil.

	    	
//			AxSf axsf = new AxSf();
//			axsfNew.setFld5Name("999");				
//		    boolean permShowDocuments = SecuritySession.permisionShowDocuments(
//		    		useCaseConf.getSessionID(), axsf);
//
//    	    if (!permShowDocuments)
//    	    	throw new SigmWSException("no permisos documentos");
    	    
	    	
	    	// Recuperar FDRID
			Integer fdrid = null;
			fdrid = SigmUtil.getFdrdid(useCaseConf, idBook, numeroRegistro);
			if(fdrid == null)
				throw new SigmWSException("err.sigm.registro.noValid", new String[]{numeroRegistro});

			List<AxDoch> docs = null;	
			docs = FolderFileSession.getBookFolderDocsWithPages(sessionID, idBook, fdrid, Constants.SIGEM_ENTIDAD_MSSSI);
	    	
			if(docs != null && docs.size() != 0){
				
				log.debug("Documentos: "+docs.size());
				
				for(AxDoch doc: docs){
					List pages = doc.getPages();
					
					log.debug("Documento. Id: " + doc.getId()+"; Nombre completo: " + doc.getName());
					if(pages.size() == 0)
							continue;
						
					AxPageh page = (AxPageh) pages.get(0);

					if(!nombreDocumento.equalsIgnoreCase(page.getName()))
						continue;
					
					BookUseCase bookUseCase = new BookUseCase();
					contentFile = bookUseCase.getFile(useCaseConf, idBook , page.getFdrId(), page.getDocId(), page.getId());

					
					log.debug("Nombre completo: " + page.getName() );
					
					break;
				}
				if(contentFile == null)
					throw new SigmWSException("err.sigm.documento.notFound", new String[]{nombreDocumento});
			}
	    
		} catch (SecurityException e) {
			throw new SigmWSException("err.sigm.security", e);
		} catch (ValidationException e) {
			throw new SigmWSException("err.sigm.validation", e);
		} catch (RPGenericException e) {
			throw new SigmWSException("err.sigm.rpgeneric", e);
		} catch (RPBookException e) {
			throw new SigmWSException("err.sigm.rpbook", e);
		} catch (BookException e) {
			throw new SigmWSException("err.sigm.book", e);
		} catch (SessionException e) {
			throw new SigmWSException("err.sigm.session", e);
		}
		finally{
	    	try {
				bo.closeBook(useCaseConf, idBook);
				SecuritySession.logout(sessionID, Constants.SIGEM_ENTIDAD_MSSSI);
		    } catch (RPGenericException e) {
		    	throw new SigmWSException("err.sigm.rpgeneric", e);
		    } catch (RPBookException e) {
		    	throw new SigmWSException("err.sigm.rpbook", e);
		    } catch (SecurityException e) {
		    	throw new SigmWSException("err.sigm.security", e);
			}
		}
	    
	    return contentFile;
	}


	public FullInfoRegister registrarSolicitud(String userName, String userPassword, int idBook,
			ElementoDatos datosSolicitud, Map<String, Fichero> adjuntos, List<ElementoFichero> listElementoFichero, byte[] solicitudFile, byte[] firmaFile) throws SigmWSException {
		
		FullInfoRegister fullInfoRegister = null; 
		
	    User applicationUser = SigmUtil.getUser(userName, userPassword,Constants.SIGEM_LOCALE); 
	    UseCaseConf useCaseConf = SigmUtil.getUseCaseConf(userName,userPassword, Constants.SIGEM_ENTIDAD_MSSSI, Constants.SIGEM_LOCALE);
		

		String sessionID = null;
		BooksBo bo = null;
        Integer fdrid = null;


		try {
			sessionID = Login.login(applicationUser, Constants.SIGEM_ENTIDAD_MSSSI);
			
	    	useCaseConf.setSessionID(sessionID);
			
			// si nos llega la oficina
			// de registro para logear al usuario con dicha oficina
//			Cambiamos la oficina preferente por la oficina que llega como parametro
//			ValidationSessionEx.validateOfficeCode(sessionID,
//					datosSolicitud.getOficina(), Constants.SIGEM_ENTIDAD_MSSSI);
			
			
	    	bo = new BooksBo();
	    	bo.openBook(useCaseConf, idBook);				
	    	 	    	
	    	// MAPEAR DATOS DE ENTRADA AL OBJETO BEAN (INTERESADOS,...)
	    	InputRegisterBean inputRegisterBean = null;
	    	OutputRegisterBean outputRegisterBean = null;
	    	
	    	if(idBook == Constants.REGISTRO_ELECTRONICO_ENTRADA){
		    	inputRegisterBean = SigmUtil.crearInputDataRegister(datosSolicitud);	    		
	    	} else {	    		
		    	outputRegisterBean = SigmUtil.crearOutputDataRegister(datosSolicitud);
	    	}

	    	//Extraer datos extendidos
	    	Map<String, String> extendedField = null;
	    	if(datosSolicitud.getDatosExtendidos() != null){
				extendedField = getCamposExtendidosFromRequest(datosSolicitud.getDatosExtendidos().getDatoExtendido());	    		
	    	}
	    	
			// Adjuntar documentos
	    	Map<?, ?> mapDocuments = null;
	    	if(adjuntos != null)
	    		mapDocuments= SigmUtil.createAttachedDocumentMap(adjuntos);	    		

			// Llamar a SIGEM para registrar
	    	fdrid = registrarCompleto(idBook, useCaseConf, inputRegisterBean, outputRegisterBean, extendedField, mapDocuments, adjuntos, listElementoFichero);
			
	    	
			CacheBag cacheBag = CacheFactory.getCacheInterface().getCacheEntry(sessionID);

			// Recuperamos los datos de conexion de la cache.
			AuthenticationUser user = (AuthenticationUser) cacheBag.get(HibernateKeys.HIBERNATE_Iuseruserhdr);
//			ScrOfic scrofic = (ScrOfic) cacheBag.get(HibernateKeys.HIBERNATE_ScrOfic);
			
			AxSf axsf = FolderSession.getBookFolder(sessionID, idBook, fdrid, new Locale(Constants.SIGEM_LOCALE), Constants.SIGEM_ENTIDAD_MSSSI);
	    	
	    	fullInfoRegister = SigmUtil.consultRegisterInfo(idBook, user, axsf,  new Locale(Constants.SIGEM_LOCALE));
	    	
	    	
			SigmUtil sigmUtil = new SigmUtil();
			
			Acuse acuse = sigmUtil.generarAcuse(useCaseConf, datosSolicitud.getTipoRegistro(), idBook, fullInfoRegister.getNumRegistro(), fdrid);

			
			//////////// GUARDAMOS EL 'ACUSE DE RECIBO' COMO DOCUMENTO ELECTRONICO 
			ElementoFichero elementoFichero = new ElementoFichero();			
			elementoFichero.setNombre("ACUSE_REGISTRO.pdf");
			elementoFichero.setComentario("Acuse generado");
			elementoFichero.setHashAlgorithm(TypeAlgorithm.SHA_1);
			
			byte[] generateHash;
			try {
				generateHash = Hasher.generateHash(acuse.getContenido(), elementoFichero.getHashAlgorithm().value());
			} catch (NoSuchAlgorithmException e) {
				throw new SigmWSException("excepcion.algoritmo", e);
			}
			String hashValue = core.tools.Conversor.bytesToHex(generateHash);
			elementoFichero.setHashValue(hashValue);
			
			elementoFichero.setTipoDocumento(TipoDocumentoAnexoEnumVO.FICHERO_TECNICO_NAME);
			elementoFichero.setValidezDocumento(TipoValidezDocumentoAnexoEnumVO.ORIGINAL_NAME);
			
			DocElectronicoUtil.saveDocument(useCaseConf, idBook, fdrid, elementoFichero, acuse.getContenido(), ContentTypeEnum.PDF.getContentType(), null);

			
			
			//////////// GUARDAMOS LA 'SOLICITUD' Y 'FIRMA' COMO DOCUMENTO ELECTRONICO 			
			elementoFichero = new ElementoFichero();			
			elementoFichero.setNombre(Constants.FILE_NAME_SOLICITUD);
			elementoFichero.setComentario(Constants.COMENTARIO_SOLICITUD);
			elementoFichero.setHashAlgorithm(TypeAlgorithm.SHA_1);			
			try {
				generateHash = Hasher.generateHash(solicitudFile, elementoFichero.getHashAlgorithm().value());
			} catch (NoSuchAlgorithmException e) {
				throw new SigmWSException("excepcion.algoritmo", e);
			}
			hashValue = core.tools.Conversor.bytesToHex(generateHash);
			elementoFichero.setHashValue(hashValue);
			
			elementoFichero.setTipoDocumento(TipoDocumentoAnexoEnumVO.FICHERO_TECNICO_NAME);
			
			DocElectronicoUtil.saveDocument(useCaseConf, idBook, fdrid, elementoFichero, solicitudFile, ContentTypeEnum.XML.getContentType(), firmaFile);
			
			
			
			
			
			fullInfoRegister.setAcuse(acuse);
			
		} catch (SigmWSException e) {
			throw e;
		} catch (RPGenericException e) {
			throw new SigmWSException("err.sigm.rpgeneric", e);
		} catch (RPInputRegisterException e) {
			throw new SigmWSException("err.sigm.rpinputregister", e);
		} catch (SecurityException e) {
			throw new SigmWSException("err.sigm.security", e);
		} catch (ValidationException e) {
			throw new SigmWSException("err.sigm.validation", e);
		} catch (RPBookException e) {
			throw new SigmWSException("err.sigm.rpbook", e);
		} catch (BookException e) {
			throw new SigmWSException("err.sigm.book", e);
		} catch (SessionException e) {
			throw new SigmWSException("err.sigm.session", e);
		} catch (SQLException e) {
			throw new SigmWSException("err.sigm.sql", e);
		} catch (TecDocException e) {
	    	throw new SigmWSException("err.sigm.TecDocException", e);
		} catch (RPOutputRegisterException e) {
	    	throw new SigmWSException("err.sigm.RPOutputRegisterException", e);
		} catch (DaoException e) {
			throw new SigmWSException("err.dao.certData", e);
		}
		finally{
	    	try {
				bo.closeBook(useCaseConf, idBook);
				SecuritySession.logout(sessionID, Constants.SIGEM_ENTIDAD_MSSSI);
		    } catch (RPGenericException e) {
		    	throw new SigmWSException("err.sigm.rpgeneric", e);
		    } catch (RPBookException e) {
		    	throw new SigmWSException("err.sigm.rpbook", e);
		    } catch (SecurityException e) {
		    	throw new SigmWSException("err.sigm.security", e);
			}
		}
	
		return fullInfoRegister;
	}
	public List<Registro> buscar(String userName, String userPassword, int idBook,
			PeticionBusqueda peticion) throws SigmWSException {
		
		List<Registro> listRegistro = null;		
		listRegistro = new ArrayList<Registro>();
		
		int maxRegistros= Constants.MAX_REPORT_REGISTER;
		
		
	    User applicationUser = SigmUtil.getUser(userName, userPassword,Constants.SIGEM_LOCALE); 
	    UseCaseConf useCaseConf = SigmUtil.getUseCaseConf(userName,userPassword, Constants.SIGEM_ENTIDAD_MSSSI, Constants.SIGEM_LOCALE);
		
		String sessionID = null;
		BooksBo bo = null;

		try {

			sessionID = Login.login(applicationUser, Constants.SIGEM_ENTIDAD_MSSSI);

			useCaseConf.setSessionID(sessionID);

			bo = new BooksBo();
			bo.openBook(useCaseConf, idBook);

			// Crear la consulta
			AxSfQuery axsfQuery = SigmUtil.getFormattedQuery(peticion, idBook);

			List<Integer> bookIds = new ArrayList<Integer>();
			bookIds.add(idBook);

			int size;

			// Lanzar la consulta
			size = FolderSession.openRegistersQuery(useCaseConf.getSessionID(), axsfQuery, bookIds,
					Constants.REPORT_OPTION_0, useCaseConf.getEntidadId());

			// queryResults.setPageSize

			if(size >= maxRegistros)
				throw new SigmWSException("err.sigm.busqueda.maxResult");
			
			// Mapear los resultados
			log.debug("Registros encontrados: " + size);
			if (size != 0) {
				String orderBy = "";
				orderBy = "FLD1";
				orderBy += " ASC ";

				AxSfQueryResults queryResults = null;

				queryResults = FolderSession.navigateRegistersQuery(useCaseConf.getSessionID(),
						idBook, com.ieci.tecdoc.common.isicres.Keys.QUERY_ALL,
						useCaseConf.getLocale(), useCaseConf.getEntidadId(), orderBy);

				SigmUtil.fillQueryResult(useCaseConf.getLocale(), sessionID, idBook, queryResults,
						listRegistro);
			}

		} catch (SecurityException e) {
			throw new SigmWSException("err.sigm.security", e);
		} catch (ValidationException e) {
			throw new SigmWSException("err.sigm.validation", e);
		} catch (RPGenericException e) {
			throw new SigmWSException("err.sigm.rpgeneric", e);
		} catch (RPBookException e) {
			throw new SigmWSException("err.sigm.rpbook", e);
		} catch (BookException e) {
			throw new SigmWSException("err.sigm.book", e);
		} catch (SessionException e) {
			throw new SigmWSException("err.sigm.session", e);
		}
		finally{
	    	try {
				bo.closeBook(useCaseConf, idBook);
				SecuritySession.logout(sessionID, Constants.SIGEM_ENTIDAD_MSSSI);
		    } catch (RPGenericException e) {
		    	throw new SigmWSException("err.sigm.rpgeneric", e);
		    } catch (RPBookException e) {
		    	throw new SigmWSException("err.sigm.rpbook", e);
		    } catch (SecurityException e) {
		    	throw new SigmWSException("err.sigm.security", e);
			}
		}
		
		return listRegistro;
	}



	public List<Registro> detallar(String user, String pass, String tipoRegistro, int idBook, PeticionDetalle peticion) throws SigmWSException {

		ArrayList<Registro> listRegistro = null;
		
	    User applicationUser = SigmUtil.getUser(user, pass,Constants.SIGEM_LOCALE); 
	    UseCaseConf useCaseConf = SigmUtil.getUseCaseConf(user,pass, Constants.SIGEM_ENTIDAD_MSSSI, Constants.SIGEM_LOCALE);

		String sessionID = null;
		BooksBo bo = null;

		
		try {
			sessionID = Login.login(applicationUser, Constants.SIGEM_ENTIDAD_MSSSI);
	    	useCaseConf.setSessionID(sessionID);
			
	    	bo = new BooksBo();
	    	bo.openBook(useCaseConf, idBook);				
	    	
	    	// Crear la consulta
			AxSfQuery axsfQuery = new AxSfQuery();
			
	    	if(tipoRegistro.equalsIgnoreCase(Constants.REGISTRO_ENTRADA))
	    	{
			    SearchInputRegisterBean searchInputRegister = new SearchInputRegisterBean();
		    	searchInputRegister.setFld1Value(peticion.getNumeroRegistro());
				axsfQuery.setBookId(idBook);				
				axsfQuery.addField(searchInputRegister.fieldtoQuery(Registro.NUM_REGISTRO, idBook));
	    	} 
	    	else if(tipoRegistro.equalsIgnoreCase(Constants.REGISTRO_SALIDA))
	    	{
			    SearchOutputRegisterBean searchOutputRegister = new SearchOutputRegisterBean();			    
			    searchOutputRegister.setFld1Value(peticion.getNumeroRegistro());
				axsfQuery.setBookId(idBook);
			    axsfQuery.addField(searchOutputRegister.fieldtoQuery(Registro.NUM_REGISTRO, idBook));
	    	}
	    			    
			List<Integer> bookIds = new ArrayList<Integer>();
			bookIds.add(idBook);
			
			// Lanzar la consulta
			int size;			
			size = FolderSession.openRegistersQuery(useCaseConf.getSessionID(), axsfQuery, bookIds, Constants.REPORT_OPTION_0, useCaseConf.getEntidadId());

			// Mapear los resultados
			log.debug("Registros encontrados: "+ size);
			if(size !=0){
				
				AxSfQueryResults queryResults = null;
	
				queryResults = FolderSession.navigateRegistersQuery(
					useCaseConf.getSessionID(), idBook,
					com.ieci.tecdoc.common.isicres.Keys.QUERY_ALL, useCaseConf.getLocale(),
					useCaseConf.getEntidadId(), null);
									
					listRegistro = new ArrayList<Registro>();
					SigmUtil.fillQueryDetailResult(useCaseConf, sessionID, idBook, tipoRegistro, queryResults, listRegistro);	
					
			} else {
				throw new SigmWSException("err.sigm.registro.noValid", new String[]{peticion.getNumeroRegistro()});				
			}
	    	
		} catch (SecurityException e) {
			throw new SigmWSException("err.sigm.security", e);
		} catch (ValidationException e) {
			throw new SigmWSException("err.sigm.validation", e);
		} catch (RPGenericException e) {
			throw new SigmWSException("err.sigm.rpgeneric", e);
		} catch (RPBookException e) {
			throw new SigmWSException("err.sigm.rpbook", e);
		} catch (BookException e) {
			throw new SigmWSException("err.sigm.book", e);
		} catch (SessionException e) {
			throw new SigmWSException("err.sigm.session", e);
		}
		finally{
	    	try {
				bo.closeBook(useCaseConf, idBook);
				SecuritySession.logout(sessionID, Constants.SIGEM_ENTIDAD_MSSSI);
		    } catch (RPGenericException e) {
		    	throw new SigmWSException("err.sigm.rpgeneric", e);
		    } catch (RPBookException e) {
		    	throw new SigmWSException("err.sigm.rpbook", e);
		    } catch (SecurityException e) {
		    	throw new SigmWSException("err.sigm.security", e);
			}
		}
		
		return listRegistro;
	
	}

	public Acuse acuse(String user, String pass, String tipoRegistro, int idBook, PeticionGenerarAcuse peticion) throws SigmWSException {
		

		String numeroRegistro = peticion.getNumeroRegistro();
		Acuse result = null;
		
 
		
		List<XtField> listXtfield = new ArrayList<XtField>();
			
	    User applicationUser = SigmUtil.getUser(user, pass,Constants.SIGEM_LOCALE); 
	    UseCaseConf useCaseConf = SigmUtil.getUseCaseConf(user, pass, Constants.SIGEM_ENTIDAD_MSSSI, Constants.SIGEM_LOCALE);
	    		
	    String sessionID = null;
    	BooksBo bo = null;
		Integer fdrid = null;
	
	    try {
		
	    	sessionID = Login.login(applicationUser, Constants.SIGEM_ENTIDAD_MSSSI);
			
	    	useCaseConf.setSessionID(sessionID);
			
			bo = new BooksBo();
			
	    	bo.openBook(useCaseConf, idBook);				

	    	
	    	// Recuperar FDRID
			fdrid = SigmUtil.getFdrdid(useCaseConf, idBook, numeroRegistro);
			
			if(fdrid == null)
				throw new SigmWSException("err.sigm.registro.noValid", new String[]{numeroRegistro});
			
			
			SigmUtil sigmUtil = new SigmUtil();

			result = sigmUtil.generarAcuse(useCaseConf, tipoRegistro, idBook, numeroRegistro, fdrid);

			 
	    } catch (SecurityException e) {
	    	throw new SigmWSException("err.sigm.security", e);
	    } catch (ValidationException e) {
	    	throw new SigmWSException("err.sigm.validation", e);
	    } catch (RPGenericException e) {
	    	throw new SigmWSException("err.sigm.rpgeneric", e);
	    } catch (RPBookException e) {
	    	throw new SigmWSException("err.sigm.rpbook", e);
	    } catch (BookException e) {
	    	throw new SigmWSException("err.sigm.book", e);
	    } catch (SessionException e) {
	    	throw new SigmWSException("err.sigm.session", e);
//	    } catch (AttributesException e) {
//	    	throw new SigmWSException("AttributesException", e);
		} catch (DaoException e) {
			throw new SigmWSException("err.dao.certData", new String[]{String.valueOf(fdrid)}, e);
		}
		finally{
	    	try {
				bo.closeBook(useCaseConf, idBook);
				SecuritySession.logout(sessionID, Constants.SIGEM_ENTIDAD_MSSSI);
		    } catch (RPGenericException e) {
		    	throw new SigmWSException("err.sigm.rpgeneric", e);
		    } catch (RPBookException e) {
		    	throw new SigmWSException("err.sigm.rpbook", e);
		    } catch (SecurityException e) {
		    	throw new SigmWSException("err.sigm.security", e);
			}
		}
		
		
		return result;
		
	}
	
	public Map<String, String> getCamposExtendidosFromRequest(
			List<ElementoDatoExtendido> listDatoExtendido) throws SigmWSException {
	
		Map<String, String> extendendField = new HashMap<String, String>();
			
	
		//procesar campos extendidos		
		int idField = 0;
		
		log.debug("Procesando CAMPOS EXTENDIDOS...");
		
		for(ElementoDatoExtendido dato: listDatoExtendido){
			
			log.debug("Campo: "+dato.getCampo());
			log.debug("Valor: "+dato.getValor());
			log.debug("Seccion: "+dato.getSeccion());
			log.debug("Descripcion: "+dato.getDescripcion());
			XtField xtField = new XtField();
			xtField.setName(dato.getCampo());
			xtField.setDescripcion(dato.getDescripcion());
			xtField.setSeccion(dato.getSeccion());
			
	
			try{
				XtField result = SIGMServiceManager.getXtfieldService().getByXtfield(xtField);
				if(result == null){
					SIGMServiceManager.getXtfieldService().insert(xtField);
					
					log.debug("Campo nuevo no encontrado. Id nuevo: "+ xtField.getFldid());
					
				}else
				{
					xtField = result;
					log.debug("No insertamos el registro nuevo en la tabla XtField");
				}
	//				SIGMServiceManager.getXtfieldService().insert(new XtField("NUEVO"+new Date().getTime()));
				
				idField = xtField.getFldid();
			
			}catch(Exception e){
				throw new SigmWSException("err.service.sigm", e);
			}				
			
			extendendField.put(String.valueOf(idField), dato.getValor());
		}
		
		return extendendField;

	}
	
	public Integer registrarCompleto(int idBook, UseCaseConf useCaseConf,
			InputRegisterBean inputRegisterBean, OutputRegisterBean outputRegisterBean, Map<String, String> extendendField, Map<?, ?> documents, Map<String, Fichero> adjuntos, List<ElementoFichero> listElementoFichero) throws SQLException, SigmWSException, SessionException, BookException, ValidationException, TecDocException, RPGenericException, RPInputRegisterException, RPOutputRegisterException {
		
		Integer fdrid = null;
		
		// Convertimos los datos de entrada para las validaciones
		List<FlushFdrField> atts = null;
		if(idBook == Constants.REGISTRO_ELECTRONICO_ENTRADA){
			atts = SigmUtil.toFlushfdrfield(inputRegisterBean, null);
		} else {
			atts = SigmUtil.toFlushfdrfield(null, outputRegisterBean);
		}

		
		// Comprobamos los permisos y validamos los campos
		RegisterServicesUtil.canCreateFolder(useCaseConf.getSessionID(), idBook, documents,
				SigmUtil.getUser(useCaseConf.getUserName(), useCaseConf.getPassword(),Constants.SIGEM_LOCALE), atts, Constants.SIGEM_ENTIDAD_MSSSI, false);		
		
		// 1- Creamos el registro
		if(idBook == Constants.REGISTRO_ELECTRONICO_ENTRADA){
			
//			InputRegisterBo inputRegisterBo = new InputRegisterBo();
			fdrid = InputRegisterBo.saveOrUpdateFolder(useCaseConf, idBook, null, null, inputRegisterBean,
				    inputRegisterBean.getInteresados(), null);					
					log.debug("Registro creado: "+fdrid);	
					
		} else if(idBook == Constants.REGISTRO_ELECTRONICO_SALIDA){
//			OutputRegisterBo outputRegisterBo = new OutputRegisterBo();	
			fdrid = OutputRegisterBo.saveOrUpdateFolder(useCaseConf, idBook, null, null, outputRegisterBean,
					outputRegisterBean.getInteresados(), null);					
					log.debug("Registro creado: "+fdrid);	
		}
		    	
		if(fdrid != null){
		 // 2- Guardar datos extendidos  		    
			if(extendendField != null){
				SigmConnector.guardarCamposExtendidos(idBook,String.valueOf(fdrid),String.valueOf(Constants.SIGEM_ENTIDAD_MSSSI),extendendField);
			}
		    			    	
		 // 3- Anadimos los documentos
			if(listElementoFichero != null && listElementoFichero.size() > 0){
												
				for(ElementoFichero elementoFichero : listElementoFichero){
					Fichero fichero = adjuntos.get(elementoFichero.getNombre());
										
					DocElectronicoUtil.saveDocument(useCaseConf, idBook, fdrid, elementoFichero, fichero.getContenido(), fichero.getFormato(), null);
					
				}
			}
		}
		
		return fdrid;
		
	}

	private static synchronized void guardarCamposExtendidos(Integer bookId, String folderId, String entidad, Map<String, String> extendedField) throws SigmWSException {
		try {
			String dataBaseType = DBEntityDAOFactory.getCurrentDBEntityDAO()
				.getDataBaseType();
				
			
			for (Map.Entry<String, String> entry : extendedField.entrySet()) {
				
				log.debug("Campo Extendido a guradar: "+entry.getKey()+" _ "+entry.getValue());
				
					AxXfPK axxpk = new AxXfPK();
					axxpk.setType(bookId.toString());
					axxpk.setFdrId(Integer.parseInt(folderId));
					axxpk.setFldId(Integer.parseInt(entry.getKey()));
	
					int nextId = DBEntityDAOFactory.getCurrentDBEntityDAO()
								.getNextIdForExtendedField(bookId, entidad);
					AxXfEntity axXfEntity2 = new AxXfEntity();
								axXfEntity2.create(axxpk, nextId, entry.getValue(), DBEntityDAOFactory
										.getCurrentDBEntityDAO().getDBServerDate(entidad),
										entidad, dataBaseType);
			}
		} catch (Exception e) {
			throw new SigmWSException("err.sigm.sql", e);
		}
	}


}

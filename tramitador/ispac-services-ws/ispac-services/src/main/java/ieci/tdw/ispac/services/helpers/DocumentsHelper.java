package ieci.tdw.ispac.services.helpers;

import ieci.tdw.ispac.api.IGenDocAPI;
import ieci.tdw.ispac.api.ISignAPI;
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.context.IClientContext;
import ieci.tdw.ispac.ispaclib.db.DbCnt;
import ieci.tdw.ispac.ispaclib.sign.ISignConnector;
import ieci.tdw.ispac.ispaclib.sign.SignConnectorFactory;
import ieci.tdw.ispac.ispaclib.sign.SignDocument;
import ieci.tdw.ispac.ispaclib.util.FileTemporaryManager;
import ieci.tdw.ispac.ispaclib.utils.ArrayUtils;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispaclib.utils.TypeConverter;
import ieci.tdw.ispac.ispaclib.utils.XmlFacade;
import ieci.tdw.ispac.services.db.DbCommand;
import ieci.tdw.ispac.services.db.DocumentosDAO;
import ieci.tdw.ispac.services.dto.Documento;
import ieci.tdw.ispac.services.dto.Firma;
import ieci.tdw.ispac.services.dto.InfoOcupacion;
import ieci.tdw.ispac.services.vo.DocumentoVO;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.bouncycastle.util.encoders.Base64;


/**
 * Utilidad para el manejo de documentos.
 */
public class DocumentsHelper {

	/** Logger de la clase. */
	private static final Logger logger = Logger.getLogger(DocumentsHelper.class);
    
	
	/**
     * Obtiene el contenido del documento.
     * @param context Contexto de cliente.
     * @param guid GUID del documento.
     * @return Contenido del documento.
     * @throws ISPACException si ocurre alg�n error.
     */
    public static byte [] getContenidoDocumento(IClientContext context, 
    		String guid) throws ISPACException {

    	ByteArrayOutputStream out = new ByteArrayOutputStream();
    	
    	if (logger.isInfoEnabled()) {
    		logger.info("Se va a obtener el contenido del documento: " + guid);
    	}

		IGenDocAPI genDocAPI = context.getAPI().getGenDocAPI();
		Object connectorSession = null;
		try {
			connectorSession = genDocAPI.createConnectorSession();
			genDocAPI.getDocument(connectorSession, guid, out);
			out.close();
    	} catch (Exception e) {
    		logger.error(
    				"Error al obtener el contenido del documento: " + guid, e);
    		throw new ISPACException(
    				"Error al obtener el contenido del documento: " + guid, e);
		} finally {
			if (connectorSession != null) {
				genDocAPI.closeConnectorSession(connectorSession);
			}
    	}		        	

		return out.toByteArray();
    }
    
    
	/**
	 * [DipuCR-Agustin #1297]
     * Obtiene el contenido del documento.
     * @param context Contexto de cliente.
     * @param guid GUID del documento.
     * @return Contenido del documento.
     * @throws ISPACException si ocurre alg�n error.
     */
    public static byte [] getContenidoDocumentoJustificanteFirma(IClientContext context, 
    		String guid) throws ISPACException {

    	ByteArrayOutputStream out = new ByteArrayOutputStream();
    	final String id_documento = guid;
    	
    	if (logger.isInfoEnabled()) {
    		logger.info("Se va a obtener el contenido del documento justificante de firma: " + guid);
    	}

		IGenDocAPI genDocAPI = context.getAPI().getGenDocAPI();
		Object connectorSession = genDocAPI.createConnectorSession();
		try {
			
			//Obtengo el infoPag por mediaci�n el infoPagRde TODO revisar	    	
	    	DbCommand sCommand = new DbCommand(context) {
	    		
				public Object logic(DbCnt cnt) throws ISPACException {
					return DocumentosDAO.getDocumentoJustificanteFirma(cnt, id_documento);
				}
			};
			
			DocumentoVO docOriginal = (DocumentoVO) sCommand.exec();
	    	
	    	// Contenido del documento original
		    //byte [] content = getContenidoDocumento(context, docOriginal.getGuid());
			genDocAPI.getDocument(connectorSession, docOriginal.getGuid(), out);
			
			
    	} catch (Exception e) {
    		logger.error(
    				"Error al obtener el contenido del documento justificante de firma: " + guid, e);
    		throw new ISPACException(
    				"Error al obtener el contenido del documento justificante de firma: " + guid, e);
		} finally {
			if (connectorSession != null) {
				genDocAPI.closeConnectorSession(connectorSession);
			}
    	}		        	

		return out.toByteArray();
    }
    

    /**
     * Obtiene los metadatos del documento.
     * @param context Contexto de cliente.
     * @param guid GUID del documento.
     * @return Metadatos del documento.
     * @throws ISPACException si ocurre alg�n error.
     */
    public static String getMetadatosDocumento(IClientContext context, 
    		String guid) throws ISPACException {

    	if (logger.isInfoEnabled()) {
    		logger.info("Se van a obtener los metadatos del documento: " + guid);
    	}

		IGenDocAPI genDocAPI = context.getAPI().getGenDocAPI();
		Object connectorSession = null;
		try {
			connectorSession = genDocAPI.createConnectorSession();
			return genDocAPI.getDocumentProperties(connectorSession, guid);
    	} catch (Exception e) {
    		logger.error(
    				"Error al obtener la propiedad del documento: " + guid, e);
    		throw new ISPACException(
    				"Error al obtener la propiedad del documento: " + guid, e);
		} finally {
			if (connectorSession != null) {
				genDocAPI.closeConnectorSession(connectorSession);
			}
    	}		        	
    }

    /**
     * Obtiene la informaci�n de un documento.
     * @param context Contexto de cliente.
     * @param guid GUID del documento.
     * @return Informaci�n del documento.
     * @throws ISPACException si ocurre alg�n error.
     */
    public static DocumentoVO getInfoDocumento(IClientContext context,
    		final String guid) throws ISPACException {

    	if (logger.isInfoEnabled()) {
    		logger.info("Se va a obtener la informaci�n del documento: " + guid);
    	}

    	DbCommand sCommand = new DbCommand(context) {
			public Object logic(DbCnt cnt) throws ISPACException {
				return DocumentosDAO.getDocumentoFirmado(cnt, guid);
			}
		};

		DocumentoVO doc = (DocumentoVO) sCommand.exec();
		if (doc != null) {
			
			String properties = getMetadatosDocumento(context, guid);
			if (StringUtils.isNotBlank(properties)) {
				XmlFacade xml = new XmlFacade(properties);
				
				// Fecha de alta
				String fechaAlta = xml.get(
						"/doc_properties/property[name='Fecha Creaci�n']/value");
				if (StringUtils.isNotBlank(fechaAlta)) {
					doc.setFechaAlta(TypeConverter.toDate(fechaAlta,
							"dd-MM-yyyy"));
				}
				
//				// Firmas
//				String firma = xml.get(
//					"/doc_properties/property[name='Firma']/value");
//				if (StringUtils.isNotBlank(firma)) {
//					XmlFacade signXml = new XmlFacade(firma);
//					List firmas = signXml.getList("/firmas/firma");
//					doc.setFirmas((String[])firmas.toArray(new String[firmas.size()]));
//				}
			}
		}

		return doc;
    }

    /**
     * Obtiene las firmas del documento.
     * @param context Contexto del cliente.
     * @param doc Informaci�n del documento.
     * @return Firmas del documento.
     * @throws ISPACException si ocurre alg�n error.
     */
    public static Firma[] getFirmas(IClientContext context, final Documento doc) 
    		throws ISPACException {

    	if (logger.isInfoEnabled()) {
    		logger.info("Se van a obtener las firmas del documento: " 
    				+ doc.getGuid());
    	}
    	IGenDocAPI genDocAPI = context.getAPI().getGenDocAPI();
    	Object connectorSession=genDocAPI.createConnectorSession();
    	
    	//Obtengo el infoPag por mediaci�n el infoPagRde TODO revisar
    	
    	DbCommand sCommand = new DbCommand(context) {
			public Object logic(DbCnt cnt) throws ISPACException {
				return DocumentosDAO.getDocumentoOriginal(cnt, doc.getGuid());
			}
		};
		
		DocumentoVO docOriginal = (DocumentoVO) sCommand.exec();
    	String signProperty = genDocAPI.getDocumentProperty(connectorSession, doc.getGuid(), "Firma");
		    
	    XmlFacade xmlFacade = new XmlFacade(signProperty);
	    	
	    List firmas = xmlFacade.getList("/" + SignDocument.TAG_FIRMAS + "/" + SignDocument.TAG_FIRMA);
    	List firmasTratadas= new ArrayList();
    	
    	   	// Contenido del documento original
	    byte [] content = getContenidoDocumento(context, docOriginal.getGuid());
	    byte signedContent [] = Base64.encode (content) ;
	    for (int i = 0; i < firmas.size(); i++) {
	    	
	       String firma= (String) firmas.get(i);
	        try {
		       Firma firmaVO = new Firma();
		       ISignConnector signConnector= SignConnectorFactory.getInstance(context).getSignConnector();
		       Map datosVerificarFirma= signConnector.verify(firma, new String(Base64.encode(signedContent)));
		       String integridad=(String) datosVerificarFirma.get(ISignAPI.INTEGRIDAD);
		       firmaVO.setAutenticada(StringUtils.equalsIgnoreCase(ISignAPI.INTEGRIDAD_OK, integridad));
		       if (StringUtils.isNotEmpty( (String) datosVerificarFirma.get(ISignAPI.DN))){
		    	   firmaVO.setAutor((String) datosVerificarFirma.get(ISignAPI.DN));	
					
				}else{
					if(datosVerificarFirma.get(ISignAPI.NOMBRE)!=null){
					 firmaVO.setAutor(datosVerificarFirma.get(ISignAPI.NOMBRE) + " " + datosVerificarFirma.get(ISignAPI.APELLIDOS));
					}
					else{
						 firmaVO.setAutor("");
					}
				}
		        firmasTratadas.add(firmaVO);
	        } catch (Exception e) {
	        	logger.warn("Error al obtener el certificado de la firma digital", e);
	        }
	    }
	    
    	
    	
    	return (Firma[]) firmasTratadas.toArray(new Firma[firmasTratadas.size()]);
    }

    /**
     * Elimina los documentos determinados por los localizadores.
     * @param context Contexto del cliente.
     * @param guids Lista de localizadores de documentos.
     * @throws ISPACException si ocurre alg�n error.
     */
    public static void removeFicheros(IClientContext context, 
    		String [] guids) throws ISPACException {

    	if (logger.isInfoEnabled()) {
    		logger.info("Se van a eliminar los ficheros: " 
    				+ StringUtils.join(guids, ","));
    	}

    	if (!ArrayUtils.isEmpty(guids)) {
    		for (int i = 0; i < guids.length; i++) {
    			removeFichero(context, guids[i]);
    		}
    	}
    }

    /**
     * Elimina el documentos determinado por el localizador.
     * @param context Contexto del cliente.
     * @param guid Localizador del documento.
     * @throws ISPACException si ocurre alg�n error.
     */
    public static void removeFichero(IClientContext context, 
    		String guid) throws ISPACException {

    	if (logger.isInfoEnabled()) {
    		logger.info("Se va a eliminar el fichero: " + guid);
    	}
    	
		IGenDocAPI genDocAPI = context.getAPI().getGenDocAPI();
		Object connectorSession = null;
		try {
			connectorSession = genDocAPI.createConnectorSession();
			genDocAPI.deleteDocument(connectorSession, guid);
    	} catch (Exception e) {
    		logger.error("Error al eliminar el documento: " + guid, e);
		} finally {
			if (connectorSession != null) {
				genDocAPI.closeConnectorSession(connectorSession);
			}
    	}		        	
    }

	/**
     * Obtiene la ocupaci�n del repositorio de documentos firmados.
     * @param context Contexto de cliente.
     * @param repId Identificador del repositorio de documentos.
     * @return Informaci�n de ocupaci�n.
     * @throws ISPACException si ocurre alg�n error.
     */
    public static InfoOcupacion getInfoOcupacion(IClientContext context, 
    		String repId) throws ISPACException {

    	if (logger.isInfoEnabled()) {
    		logger.info("Se va a obtener la informaci�n de ocupaci�n del repositorio: " + repId);
    	}

    	InfoOcupacion info = null;
    	
    	if (StringUtils.isNotBlank(repId)) {

    		// Obtener la informaci�n del repositorio
			IGenDocAPI genDocAPI = context.getAPI().getGenDocAPI();
			Object connectorSession = null;

			try {
				connectorSession = genDocAPI.createConnectorSession();
    			String repInfo = genDocAPI.getRepositoryInfo(connectorSession, repId);
    			if (logger.isDebugEnabled()) {
    				logger.debug("Informaci�n de ocupaci�n: " + repInfo);
    			}

    			// Componer la informaci�n de ocupaci�n
    			if (StringUtils.isNotBlank(repInfo)) {
    				
    				// XML con la informaci�n
    				XmlFacade xml = new XmlFacade(repInfo);
    				
    				// Informaci�n de ocupaci�n
    				info = new InfoOcupacion();
    	  	        info.setEspacioTotal(TypeConverter.parseInt(
    	  	        		xml.get("/repository/length"), 0));
		  	        info.setEspacioOcupado(TypeConverter.parseInt(
		  	        		xml.get("/repository/size"), 0));
		  	        info.setNumeroFicheros(TypeConverter.parseInt(
		  	        		xml.get("/repository/files"), 0));
    			}
    			
        	} catch (Exception e) {
        		logger.error("Error al obtener la informaci�n de ocupaci�n del repositorio: " + repId, e);
        		throw new ISPACException("Error al obtener la informaci�n de ocupaci�n del repositorio: " + repId, e);
			} finally {
				if (connectorSession != null) {
					genDocAPI.closeConnectorSession(connectorSession);
				}
	    	}		        	
    	}		

		return info;
    }
    
    /**
     * [Dipucr-Agustin S2#781]
     * Obtiene el contenido del documento temporal pasando el nombre.
     * @param context Contexto de cliente.
     * @param guid nombre del documento.
     * @return Contenido del documento.
     * @throws ISPACException si ocurre alg�n error.
     */
    public static byte [] getContenidoDocumentoTemp(IClientContext context, 
    		String path) throws ISPACException {

    	ByteArrayOutputStream out = new ByteArrayOutputStream();
    	
    	if (logger.isInfoEnabled()) {
    		logger.info("Se va a obtener el contenido del documento temporal: " + path);
    	}

		IGenDocAPI genDocAPI = context.getAPI().getGenDocAPI();
		Object connectorSession = null;
		try {
			
			connectorSession = genDocAPI.createConnectorSession();
			//genDocAPI.getDocument(connectorSession, path, out);
			FileTemporaryManager.getInstance().get(out, path);
				
			out.close();
    	} catch (Exception e) {
    		logger.error(
    				"Error al obtener el contenido del documento temporal: " + path, e);
    		throw new ISPACException(
    				"Error al obtener el contenido del documento temporal: " + path, e);
		} finally {
			if (connectorSession != null) {
				genDocAPI.closeConnectorSession(connectorSession);
			}
    	}		        	

		return out.toByteArray();
    }
    
    
    /**
     * [Dipucr-Agustin #781]
     * Guarda el contenido del documento temporal pasando byte array.
     * @param context Contexto de cliente.
     * @param guid nombre del documento.
     * @return Contenido del documento.
     * @throws ISPACException si ocurre alg�n error.
     */
    public static boolean setContenidoDocumentoTemp(IClientContext context, 
    		String path, byte[] data) throws ISPACException {
    	
    	boolean result = false;
    	ByteArrayInputStream in = new ByteArrayInputStream(data);
    	    	
    	if (logger.isInfoEnabled()) {
    		logger.info("Se va a obtener el contenido del documento temporal: " + path);
    	}

		IGenDocAPI genDocAPI = context.getAPI().getGenDocAPI();
		Object connectorSession = null;
		try {
			
			connectorSession = genDocAPI.createConnectorSession();
			result = FileTemporaryManager.getInstance().put(in,path);
				
			in.close();
    	} catch (Exception e) {
    		logger.error(
    				"Error al guardar el contenido del documento temporal: " + path, e);
    		throw new ISPACException(
    				"Error al guardar el contenido del documento temporal: " + path, e);
		} finally {
			if (connectorSession != null) {
				genDocAPI.closeConnectorSession(connectorSession);
			}
    	}		        	

		return result;
    }

}

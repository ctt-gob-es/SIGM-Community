package ieci.tdw.ispac.ispaclib.alfresco.doc;
 
import ieci.tdw.ispac.api.errors.ISPACException;
import ieci.tdw.ispac.ispaclib.alfresco.doc.helper.AlfrescoCMISHelper;
import ieci.tdw.ispac.ispaclib.context.ClientContext;
import ieci.tdw.ispac.ispaclib.gendoc.config.Repository;
import ieci.tdw.ispac.ispaclib.utils.FileUtils;
import ieci.tdw.ispac.ispaclib.utils.MapUtils;
import ieci.tdw.ispac.ispaclib.utils.XmlTag;

import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

import org.apache.log4j.Logger;

import es.ieci.tecdoc.isicres.document.connector.alfresco.vo.AlfrescoDatosEspecificosVO;
import es.ieci.tecdoc.isicres.document.connector.alfresco.vo.AlfrescoDatosEspecificosValueVO;
import es.ieci.tecdoc.isicres.document.connector.alfresco.vo.AlfrescoDocumentVO;
import es.ieci.tecdoc.isicres.document.connector.alfrescoCMIS.AlfrescoCMISDocumentConnector;
import es.ieci.tecdoc.isicres.document.connector.vo.ISicresAbstractDocumentVO;

/**
 * Conector documental con Alfresco.
 *
 */
public class AlfrescoCMISConnector extends AlfrescoConnector {

	/**
	 * Logger de la clase.
	 */
	private static final Logger logger = Logger.getLogger(AlfrescoCMISConnector.class);


	/**
	 * Constructor.
	 * @param ctx Contexto de cliente.
	 * @param repository Información del repositorio documental.
	 */
	protected AlfrescoCMISConnector(Repository repository) {
		super(repository);
		connector = new AlfrescoCMISDocumentConnector();
	}

	/**
	 * Constructor.
	 * @param ctx Contexto de cliente.
	 * @throws ISPACException si ocurre algún error.
	 */
	protected AlfrescoCMISConnector(ClientContext ctx) throws ISPACException {
		super(ctx);
		connector = new AlfrescoCMISDocumentConnector();
	}

	
	protected AlfrescoCMISConnector(ClientContext ctx, String repositoryAlias) throws ISPACException {
		super(ctx, repositoryAlias);		
		connector = new AlfrescoCMISDocumentConnector();
	}

	protected AlfrescoCMISConnector(ClientContext ctx, Integer repositoryId) throws ISPACException {
		super(ctx, repositoryId);
		connector = new AlfrescoCMISDocumentConnector();
	}
	
	
	/**
	 * Obtiene una instancia del conector.
	 * @param ctx Contexto de cliente.
	 * @return Instancia del conector.
	 * @throws ISPACException si ocurre algún error.
	 */
	public static synchronized AlfrescoCMISConnector getInstance(ClientContext ctx) throws ISPACException {
		return new AlfrescoCMISConnector(ctx);
	}

	/**
	 * Obtiene una instancia del conector.
	 * @param ctx Contexto de cliente.
	 * @param repositoryAlias Alias del repositorio.
	 * @return Instancia del conector.
	 * @throws ISPACException si ocurre algún error.
	 */
	public static synchronized AlfrescoCMISConnector getInstance(ClientContext ctx, String repositoryAlias) throws ISPACException {
		return new AlfrescoCMISConnector(ctx, repositoryAlias);
	}

	/**
	 * Obtiene una instancia del conector.
	 * @param ctx Contexto de cliente.
	 * @param repositoryId Identificador del repositorio.
	 * @return Instancia del conector.
	 * @throws ISPACException si ocurre algún error.
	 */
	public static synchronized AlfrescoCMISConnector getInstance(ClientContext ctx, Integer repositoryId) throws ISPACException {
		return new AlfrescoCMISConnector(ctx, repositoryId);
	}

	/**
	 * Crea una sesión de trabajo. NO HACE NADA
	 */
	public Object createSession() throws ISPACException {
		return null;
	}

	/**
	 * Cierra la sesión de trabajo. NO HACE NADA
	 * @param session Sesión de trabajo.
	 * @throws ISPACException si ocurre algún error.
	 */
	public void closeSession(Object session) throws ISPACException {
		return;
	}


	/**
	 * Comprueba si existe un documento.
	 * @param session Sesión de trabajo.
	 * @param uid UID del documento.
	 * @return true si el documento existe, false en caso contrario.
	 * @throws ISPACException si ocurre algún error.
	 */
	public boolean existsDocument(Object session, String uid) throws ISPACException {
		
		boolean exists = false;
		
		try {
			
			exists = AlfrescoCMISHelper.existsDocument(repository, uid);

			if (logger.isInfoEnabled()) {
				logger.info("Documento [" + uid + "] existe? => " + exists);
			}

		} catch (Exception e) {
			logger.error("Error al comprobar si existe el documento", e);
			throw new ISPACException("Error al comprobar si existe el documento", e);
		}
		
		return exists;
	}

	// UPCT - Añadido para pruebas unitarias
	public void checkFolderPath(Repository repository, String properties) throws Exception {
		AlfrescoCMISHelper.checkFolderPath(repository, properties);
	}
	
	/**
	 * Crea un nuevo documento.
	 * @param session Sesión de trabajo.
	 * @param in InputStream para obtener el contenido del documento.
	 * @param length Tamaño del documento en bytes.
	 * @param properties Metadatos del documento.
	 * @return UID del documento creado.
	 * @throws ISPACException si ocurre algún error.
	 */
	public String newDocument(Object session, InputStream in, int length, String properties) throws ISPACException {

		try {
			
			String uid = null;
			
			// Comprobar si existe el espacio
			AlfrescoCMISHelper.checkFolderPath(repository, properties);
			
			AlfrescoDocumentVO alfrescoDocumentVO = new AlfrescoDocumentVO();

			alfrescoDocumentVO.setName(AlfrescoCMISHelper.getDocName(repository, properties));
			alfrescoDocumentVO.setContent(FileUtils.retrieveFile(in));
			alfrescoDocumentVO.setConfiguration(AlfrescoCMISHelper.getConfiguration(repository));
			alfrescoDocumentVO.setDatosEspecificos(AlfrescoCMISHelper.getDatosEspecificos(repository, properties));

			ISicresAbstractDocumentVO result = connector.create(alfrescoDocumentVO);
			if (result != null) {
				uid = result.getId();
				if (logger.isInfoEnabled()) {
					logger.info("Documento [" + uid + "] creado correctamente");
				}
			}

			return uid;
		    
		} catch (Exception e) {
			logger.error("Error al crear el documento", e);
			throw new ISPACException("Error al crear el documento", e);
		}
	}

	/**
	 * Modifica un documento
	 * @param session Sesión de trabajo.
	 * @param uid UID del documento.
	 * @param in InputStream para obtener el contenido del documento.
	 * @param length Tamaño del documento en bytes.
	 * @param properties Metadatos del documento.
	 * @return UID del documento modificado.
	 * @throws ISPACException si ocurre algún error.
	 */
	public String updateDocument(Object session, String uid, InputStream in, int length, String properties) throws ISPACException {

		try {
			
			if (logger.isDebugEnabled()) {
				logger.debug("Metadatos a actualizar [" + uid + "]: " + properties);
			}

			// Actualizar el XML de los metadatos almacenados con la nueva información
			properties = AlfrescoCMISHelper.updatePropertiesXML(repository, uid, properties);
			
			if (logger.isDebugEnabled()) {
				logger.debug("Metadatos completos actualizados [" + uid + "]: " + properties);
			}

			// Comprobar si existe el espacio
			AlfrescoCMISHelper.checkFolderPath(repository, properties);

			AlfrescoDocumentVO alfrescoDocumentVO = new AlfrescoDocumentVO();

			alfrescoDocumentVO.setId(uid);
			alfrescoDocumentVO.setName(AlfrescoCMISHelper.getDocName(repository, properties));
			alfrescoDocumentVO.setContent(FileUtils.retrieveFile(in));
			alfrescoDocumentVO.setConfiguration(AlfrescoCMISHelper.getConfiguration(repository));
			alfrescoDocumentVO.setDatosEspecificos(AlfrescoCMISHelper.getDatosEspecificos(repository, properties));

			ISicresAbstractDocumentVO result = connector.update(alfrescoDocumentVO);

			if (result != null) {
				uid = result.getId();
				if (logger.isInfoEnabled()) {
					logger.info("Documento [" + uid + "] actualizado correctamente");
				}
			}

			return uid;
		    
		} catch (Exception e) {
			logger.error("Error al actualizar el documento", e);
			throw new ISPACException("Error al actualizar el documento", e);
		}
	}

	/**
	 * Elimina un documento
	 * @param session Sesión de trabajo.
	 * @param uid UID del documento.
	 * @throws ISPACException si ocurre algún error.
	 */
	public void deleteDocument(Object session, String uid) throws ISPACException {

		try {
			
			AlfrescoDocumentVO alfrescoDocumentVO = new AlfrescoDocumentVO();
			alfrescoDocumentVO.setConfiguration(AlfrescoCMISHelper.getConfiguration(repository));
			alfrescoDocumentVO.setId(uid);

			connector.delete(alfrescoDocumentVO);
			
			if (logger.isInfoEnabled()) {
				logger.info("Documento [" + uid + "] eliminado correctamente");
			}
			
		} catch (Exception e) {
			logger.error("Error al eliminar el documento", e);
			throw new ISPACException("Error al eliminar el documento", e);
		}
	}

	/**
	 * Obtiene el tamaño de un documento
	 * @param session Sesión de trabajo.
	 * @param uid UID del documento.
	 * @return Tamaño del documento en bytes.
	 * @throws ISPACException si ocurre algún error.
	 */
	public int getDocumentSize(Object session, String uid) throws ISPACException {
		
		try {
			
			int size = AlfrescoCMISHelper.getDocumentSize(repository, uid);

			if (logger.isInfoEnabled()) {
				logger.info("Tamaño del documento [" + uid + "]: " + size + " bytes");
			}

			return size;
			
		} catch (Exception e) {
			logger.error("Error al obtener el tamaño del documento", e);
			throw new ISPACException("Error al obtener el tamaño del documento", e);
		}
	}

	/**
	 * Obtiene el tipo MIME de un documento
	 * @param session Sesión de trabajo.
	 * @param uid UID del documento.
	 * @return Tipo MIME del documento.
	 * @throws ISPACException si ocurre algún error.
	 */
	public String getMimeType(Object session, String uid) throws ISPACException {

		try {
			
			String mimeType = AlfrescoCMISHelper.getDocumentMimeType(repository, uid);

			if (logger.isInfoEnabled()) {
				logger.info("Tipo MIME del documento [" + uid + "]: " + mimeType);
			}
			
			return mimeType;

		} catch (Exception e) {
			logger.error("Error al obtener el tipo MIME del documento", e);
			throw new ISPACException("Error al obtener el tipo MIME del documento", e);
		}
	}

	/**
	 * Obtiene los metadatos de un documento
	 * @param session Sesión de trabajo.
	 * @param uid UID del documento.
	 * @return Metadatos del documento.
	 * @throws ISPACException si ocurre algún error.
	 */
	@SuppressWarnings("unchecked")
	public String getProperties(Object session, String uid) throws ISPACException {

		try {
			
		    String properties = "";

			AlfrescoDatosEspecificosVO datosEspecificos = AlfrescoCMISHelper.getMetadatos(repository, uid);
			if (datosEspecificos != null) {
				Map<String, AlfrescoDatosEspecificosValueVO> values = datosEspecificos.getValues();
				if (MapUtils.isNotEmpty(values)) {
					for (Iterator<String> keyIt = values.keySet().iterator(); keyIt.hasNext();) {
						String name = keyIt.next();
						String value = "";
						
						AlfrescoDatosEspecificosValueVO alfrescoDatosEspecificosValueVO = (AlfrescoDatosEspecificosValueVO) values.get(name);
						if (alfrescoDatosEspecificosValueVO != null) {
							value = alfrescoDatosEspecificosValueVO.getValue();
						}
						
                    	properties += XmlTag.newTag("property", XmlTag.newTag("name", name) 
                    			+ XmlTag.newTag("value", XmlTag.newCDATA(value)));
					}
				}
			}

			String xml = XmlTag.getXmlInstruction("ISO-8859-1") + XmlTag.newTag("doc_properties", properties);
			
			if (logger.isInfoEnabled()) {
				logger.info("Propiedades leídas correctamente [" + uid + "]");
				if (logger.isDebugEnabled()) {
					logger.debug("Metadatos [" + uid + "]: " + xml);
				}
			}
			
			return xml;

		} catch (Exception e) {
			logger.error("Error al obtener los metadatos del documento", e);
			throw new ISPACException("Error al obtener los metadatos del documento", e);
		}
	}

	/**
	 * Obtiene un metadato de un documento
	 * @param session Sesión de trabajo.
	 * @param uid UID del documento.
	 * @param property Nombre del metadato.
	 * @return Valor del metadato del documento.
	 * @throws ISPACException si ocurre algún error.
	 */
	public String getProperty(Object session, String uid, String property) throws ISPACException {

		try {
			
			String value = AlfrescoCMISHelper.getMetadato(repository, uid, property);
			
			if (logger.isInfoEnabled()) {
				logger.info("Metadato [" + uid + "]: [" + property + "] = ["+ value + "]");
			}

			return value;

		} catch (Exception e) {
			logger.error("Error al obtener el valor del metadato [" + uid + "]: [" + property + "]", e);
			throw new ISPACException("Error al obtener el metadato [" + uid + "]: [" + property + "]", e);
		}
	}

	/**
	 * Establece un metadato de un documento.
	 * @param session Sesión de trabajo.
	 * @param uid UID del documento.
	 * @param name Nombre del metadato.
	 * @param value Valor del metadato.
	 * @throws ISPACException si ocurre algún error.
	 */
	public void setProperty(Object session, String uid, String name, String value) throws ISPACException {

		try {
			//[Ruben CMIS] Para leer el metadato de Firma en tramitación es necesario añadir el prefijo "iflow:"
			// Si se usará para leer algún metadato de Registro no sería necesario y habría que añadir un IF 
			//como en AlfrescoCMISDocumentConnector.java en el metodo create (prefix)
			String prefix= "iflow";
			name = prefix + ":" + name; // En TRAMITACIÓN se le añade el prefijo "iflow:"
			
			AlfrescoCMISHelper.setMetadato(repository, uid, name, value);
			
			if (logger.isInfoEnabled()) {
				logger.info("Metadato establecido [" + uid + "]: [" + name + "] = ["+ value + "]");
			}

		} catch (Exception e) {
			logger.error("Error al establecer el valor del metadato [" + uid + "]: [" + name + "]", e);
			throw new ISPACException("Error al establecer el metadato [" + uid + "]: [" + name + "]", e);
		}
	}

	public Repository getRepository() {
		return repository;
	}

	public void setRepository(Repository repository) {
		this.repository = repository;
	}

}

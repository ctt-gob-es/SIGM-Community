package es.ieci.tecdoc.isicres.document.connector.alfrescoCMIS;
        
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;

//import ctsi.ExceptionCTSI;
import ctsi.alfresco.cmis.CatalogoAlfrescoCmis;
import es.ieci.tecdoc.isicres.document.connector.ISicresDocumentConnector;
import es.ieci.tecdoc.isicres.document.connector.alfresco.utils.UtilsAlfresco;
import es.ieci.tecdoc.isicres.document.connector.alfresco.vo.AlfrescoAspectVO;
import es.ieci.tecdoc.isicres.document.connector.alfresco.vo.AlfrescoConnectorConfigurationVO;
import es.ieci.tecdoc.isicres.document.connector.alfresco.vo.AlfrescoDatosEspecificosVO;
import es.ieci.tecdoc.isicres.document.connector.alfresco.vo.AlfrescoDatosEspecificosValueVO;
import es.ieci.tecdoc.isicres.document.connector.exception.IsicresDocumentConnectorException;
import es.ieci.tecdoc.isicres.document.connector.vo.ISicresAbstractCriterioBusquedaVO;
import es.ieci.tecdoc.isicres.document.connector.vo.ISicresAbstractDocumentVO;

	   
/**
 * 
 * @author Iecisa 
 * @version $Revision$ 
 *
 */
public class AlfrescoCMISDocumentConnector implements ISicresDocumentConnector{

	/*private static final String ASPECT_ASPECTDOCUMENTO = "aspectDocumento";
	private static final String ASPECT_ASPECTFIRMADOCUMENTO = "aspectFirmaDocumento";
	private static final String ASPECT_ASPECTREGISTRO = "aspectRegistro";
	
	private static final String ASPECT_PREFIX_IFLOW = "P:iflow:";
	private static final String ASPECT_METADATA_PREFIX_IFLOW = "iflow:";
	
	private static final String ASPECT_PREFIX_ISICRES = "P:isicres:";
	private static final String ASPECT_METADATA_PREFIX_ISICRES = "";*/
	
	private static final String ASPECT_CMIS_PREFIX = "P:";
	
	private static final Logger log = Logger.getLogger(AlfrescoCMISDocumentConnector.class);
	
	//private Properties properties;
	//private CatalogoAlfrescoCmis ctlAlfrescoCmis;
	
	public enum STORES_CMIS {
        TEST("/Espacios personales/Sigem/PRUEBAS");

        private String value;

        private STORES_CMIS(String value) {
            this.value = value;
        }        
	};
        
	
	/*public AlfrescoCMISDocumentConnector() {
		super();
		
		// Cargamos el fichero de propiedades
		properties = new Properties();
		try{
			java.net.URL file = this.getClass().getResource("alfresco.properties");
			properties.load(file.openStream());
		}catch (IOException ioe){
			throw new IsicresDocumentConnectorException("No se ha podido cargar el fichero de propiedades", ioe);
		}
				
		try{
				
			String server = properties.getProperty("ALFRESCO_SERVER");
			String port =  properties.getProperty("ALFRESCO_PORT");
			String user =  properties.getProperty("ALFRESCO_USER");
			String pwd =  properties.getProperty("ALFRESCO_PWD");
			// Para versión WS
			//String store =  properties.getProperty("ALFRESCO_STORE");
			
			// Para versión CMIS
			String version =  properties.getProperty("ALFRESCO_VERSION");
			
			ctlAlfrescoCmis = new CatalogoAlfrescoCmis(server,  port, user, pwd, version);
		} catch(Exception e){
			throw new IsicresDocumentConnectorException("No se ha podido iniciar el catalogo para la conexión con alfresco", e);
		}
	}*/
	
	// Transforma el contentName XXXXX.model en XXXXX
	// Ej: isicres.model en isicres
	private String contentNameToPrefix(String modelName) {
		try {
			return modelName.substring(0, modelName.indexOf('.'));
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * Inserta un nuevo contenido en el gestor
	 * @throws Exception 
	 */
	public ISicresAbstractDocumentVO create(ISicresAbstractDocumentVO document) {
		// Instancias
		AlfrescoCMISConnection alfrescoConnection = new AlfrescoCMISConnection();

		AlfrescoConnectorConfigurationVO configuration = (AlfrescoConnectorConfigurationVO)document.getConfiguration();

		// Se establece la conexion con alfresco
		try {
			alfrescoConnection.connection(configuration);
		} catch (Exception e) {
			throw new IsicresDocumentConnectorException(
					"Impossible to save the file  [" + document.getName() + "]",
					e);
		}
		
		// Construimos el mapa de metadatos
		AlfrescoDatosEspecificosVO datosEspecificos = (AlfrescoDatosEspecificosVO)document.getDatosEspecificos();
		List<AlfrescoAspectVO> listaAspectos = datosEspecificos.getListAspects();
		List<String> listaAspectosStr = new ArrayList<String>();
		/*boolean isTramitacion = false;
		boolean isRegistro = false;*/
		for (AlfrescoAspectVO aspect : listaAspectos) {
			/*if (ASPECT_ASPECTDOCUMENTO.equals(aspect.getNameAspect()) ||
				ASPECT_ASPECTFIRMADOCUMENTO.equals(aspect.getNameAspect())) {
				listaAspectosStr.add(ASPECT_PREFIX_IFLOW + aspect.getNameAspect());
				isTramitacion = true;
			} 
			else if (ASPECT_ASPECTREGISTRO.equals(aspect.getNameAspect())) {
				listaAspectosStr.add(ASPECT_PREFIX_ISICRES + aspect.getNameAspect());
				isRegistro = true;
			}
			else {
				listaAspectosStr.add(aspect.getNameAspect());
			}*/
			String aspectoCmis = ASPECT_CMIS_PREFIX + contentNameToPrefix(aspect.getNameContent()) + ":" + aspect.getNameAspect(); 
			listaAspectosStr.add(aspectoCmis);
		}
		
		Iterator it = datosEspecificos.getValues().values().iterator();
		Map<String, Object> metadatosMap = new HashMap<String, Object>();
		while(it.hasNext()){
			AlfrescoDatosEspecificosValueVO metadatoVO = (AlfrescoDatosEspecificosValueVO) it.next();
			
			String prefix = contentNameToPrefix(metadatoVO.getContentName());
			String metadatoCmisNombre = metadatoVO.getName();		// En REGISTRO se queda con el nombre que viene
			if (prefix != null) {
				metadatoCmisNombre = prefix + ":" + metadatoVO.getName();	// En TRAMITACIÓN se le añade el prefijo "iflow:"
			}
			metadatosMap.put(metadatoCmisNombre, metadatoVO.getValue());
		}		
		
		String guid = null;
		try {
			// SIGEM-1129 Grabar en directorios según fecha en TRAMITADOR (YYYY/MM/DD)
			String rootPath = datosEspecificos.getPathSpace();
			String childPath = UtilsAlfresco.getFolderPathYYYYMMDD();
			String fullPath = rootPath + "/" + childPath;
			
			CatalogoAlfrescoCmis catalogoCmis = alfrescoConnection.getCatalogoAlfrescoCmis();
			// Creamos el path si no existe
			if (catalogoCmis.getSpaceFromPath(fullPath) == null) {
				catalogoCmis.createSpaceFromPATH(rootPath, childPath, childPath);
			}
			
			//guid = alfrescoConnection.getCatalogoAlfrescoCmis().writeFile(new ByteArrayInputStream(document.getContent()), document.getName(), document.getName(), datosEspecificos.getPathSpace());
			guid = alfrescoConnection.getCatalogoAlfrescoCmis().writeFileWithAspectAndMetadata(new ByteArrayInputStream(document.getContent()), 
												document.getName(), document.getName(), fullPath, listaAspectosStr, metadatosMap,  true);
			
		} catch (Exception e) {
			throw new IsicresDocumentConnectorException(
					"Impossible to save the file  [" + document.getName() + "]",
					e);
		}
	
		document.setId(guid);
		
		return document;
		
		
		/*// Instancias
		UtilsAlfresco utilsAlfresco = new UtilsAlfresco();		
		AlfrescoConnection alfrescoConnection = new AlfrescoConnection();

		// Se establece la conexion con alfresco
		try {
			alfrescoConnection.connection((AlfrescoConnectorConfigurationVO)document.getConfiguration());
		} catch (Exception e) {
			throw new IsicresDocumentConnectorException(
					"Impossible to save the file  [" + document.getName() + "]",
					e);
		}
		
		// Se recupera el repositorio
		RepositoryServiceSoapBindingStub repository = alfrescoConnection.getRepository();
		ContentServiceSoapBindingStub contentRepository = alfrescoConnection.getContentRepository();
			
		// Carga del objeto CML de Alfresco
		CML cml = utilsAlfresco.createCML(document);		
		
		// Se inserta el objeto en el repositorio
		UpdateResult[] result;
		try {
			result = repository.update(cml);		
			String uuid = result[0].getDestination().getUuid();
		
			// Se asigna el uuid al document
			document.setId(uuid);
			
			// Insertar bytes en el objeto		
			Reference contentNode = result[0].getDestination();
			MimeTypes mimeTypes = new MimeTypes();
			String mimeType = mimeTypes.getMimeType(document.getName());
			ContentFormat format = new ContentFormat(mimeType, AlfrescoKeys.ENCODING_UTF8);		
			Content contentRef  = contentRepository.write(contentNode,Constants.PROP_CONTENT, document.getContent(), format);
			byte[] bytes = UtilsFile.getByteContent(contentRef);
			document.setContent(bytes);
			
			// Convertir en versionable el contenido
			cml = utilsAlfresco.makeVersionable(document);
			repository.update(cml);
			
			// Se cierra la conexion con alfresco
			alfrescoConnection.endConnection();
		} catch (RepositoryFault e) {
			// TODO Auto-generated catch block
			log.error("Error en el repositorio",e);
			throw new IsicresDocumentConnectorException(
					"Impossible to save the file  [" + document.getName() + "]",
					e);
		} catch (RemoteException e) {
			log.error("Error en la conexion con Alfresco",e);
			throw new IsicresDocumentConnectorException(
					"Impossible to save the file  [" + document.getName() + "]",
					e);
		} catch (Exception e){
			log.error(e);
			throw new IsicresDocumentConnectorException(
					"Impossible to save the file  [" + document.getName() + "]",
					e);
		}		
		
		return document;*/
	}

	/**
	 * Elimina un contenido del gestor
	 */
	public void delete(ISicresAbstractDocumentVO document){	
		// Instancias
		AlfrescoCMISConnection alfrescoConnection = new AlfrescoCMISConnection();

		// Se establece la conexion con alfresco
		try {
			alfrescoConnection.connection((AlfrescoConnectorConfigurationVO)document.getConfiguration());
		} catch (Exception e) {
			throw new IsicresDocumentConnectorException(
					"Impossible to save the file  [" + document.getName() + "]",
					e);
		}
				
		String guid = document.getId();
		try {
			alfrescoConnection.getCatalogoAlfrescoCmis().deleteSpaceFromGUID(guid);
		} catch (Exception e) {
			throw new IsicresDocumentConnectorException(
					"Impossible to delete the file  [" + document.getName() + "]",
					e);
		}

		/*// Instancias
		UtilsAlfresco utilsAlfresco = new UtilsAlfresco();		
		AlfrescoConnection alfrescoConnection = new AlfrescoConnection();

		// Se establece la conexion con alfresco
		try {
			alfrescoConnection.connection((AlfrescoConnectorConfigurationVO)document.getConfiguration());
		} catch (Exception e) {
			throw new IsicresDocumentConnectorException(
					"Impossible to save the file  [" + document.getName() + "]",
					e);
		}
		
		// Se recupera el repositorio
		RepositoryServiceSoapBindingStub repository = alfrescoConnection.getRepository();
		
		// Carga del objeto CML de Alfresco
		CML cml = utilsAlfresco.deleteCML(document);
		
		// Se elimina el objeto en el repositorio
		try {
			repository.update(cml);
		} catch (RepositoryFault e) {
			// TODO Auto-generated catch block
			log.error("Error en el repositorio",e);
			throw new IsicresDocumentConnectorException(
					"Impossible to save the file  [" + document.getName() + "]",
					e);
		} catch (RemoteException e) {
			log.error("Error en la conexion con Alfresco",e);
			throw new IsicresDocumentConnectorException(
					"Impossible to save the file  [" + document.getName() + "]",
					e);
		} 		*/
	}

	
	public List find(ISicresAbstractCriterioBusquedaVO criterioBusqueda) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * @throws Exception 
	 * 
	 */
	public ISicresAbstractDocumentVO retrieve(ISicresAbstractDocumentVO document) {
		// Instancias
		AlfrescoCMISConnection alfrescoConnection = new AlfrescoCMISConnection();

		// Se establece la conexion con alfresco
		try {
			alfrescoConnection.connection((AlfrescoConnectorConfigurationVO)document.getConfiguration());
		} catch (Exception e) {
			throw new IsicresDocumentConnectorException(
					"Impossible to retrieve the file  [" + document.getName() + "]",
					e);
		}
				
		String guid = document.getId();
		Document docAlfresco = null;
		try {
			// Recuperamos por GUID
			docAlfresco = alfrescoConnection.getCatalogoAlfrescoCmis().readFile(guid, null);
			
			// Convertimos en array de Bytes
			byte[] content = IOUtils.toByteArray(docAlfresco.getContentStream().getStream());
			
			// Asignamos el contenido
			document.setContent(content);
		} catch (Exception e) {
			throw new IsicresDocumentConnectorException(
					"Impossible to retrieve the file  [" + document.getName() + "]",
					e);
		}

		
		return document;
		// Instancias
		/*UtilsAlfresco utilsAlfresco = new UtilsAlfresco();		
		AlfrescoConnection alfrescoConnection = new AlfrescoConnection();

		// Se establece la conexion con alfresco
		try {
			alfrescoConnection.connection((AlfrescoConnectorConfigurationVO)document.getConfiguration());
		} catch (Exception e) {
			throw new IsicresDocumentConnectorException(
					"Impossible to save the file  [" + document.getName() + "]",
					e);
		}
		
		// Se recupera el repositorio
		RepositoryServiceSoapBindingStub repository = alfrescoConnection.getRepository();
		ContentServiceSoapBindingStub contentRepository = alfrescoConnection.getContentRepository();
		
		// Se recuperan los bytes y se cargarn en document		
		try {
			document = utilsAlfresco.search(document, null, contentRepository,repository);		
		} catch (Exception e) {
			log.error("Error al retornar los byte",e);
			throw new IsicresDocumentConnectorException(
					"Impossible to save the file  [" + document.getName() + "]",
					e);
		}
		return document;*/
	}

	/**
	 * 
	 */
	public ISicresAbstractDocumentVO update(ISicresAbstractDocumentVO document) {		
		
		// Instancias
		AlfrescoCMISConnection alfrescoConnection = new AlfrescoCMISConnection();

		// Se establece la conexion con alfresco
		try {
			alfrescoConnection.connection((AlfrescoConnectorConfigurationVO)document.getConfiguration());
		} catch (Exception e) {
			throw new IsicresDocumentConnectorException(
					"Impossible to save the file  [" + document.getName() + "]",
					e);
		}
		
		// Se actualiza el documento en el repositorio
		try {
			// Convertimos el array de bytes en un InputStream
			InputStream newInputStream = new ByteArrayInputStream(document.getContent());
			alfrescoConnection.getCatalogoAlfrescoCmis().updateFile(document.getId(), newInputStream);
		} catch (Exception e) {
			throw new IsicresDocumentConnectorException(
					"Impossible to save the file  [" + document.getName() + "]",
					e);
		}
				
		return document;
		/*// Instancias
		UtilsAlfresco utilsAlfresco = new UtilsAlfresco();		
		AlfrescoConnection alfrescoConnection = new AlfrescoConnection();

		// Se establece la conexion con alfresco
		try {
			alfrescoConnection.connection((AlfrescoConnectorConfigurationVO)document.getConfiguration());
		} catch (Exception e) {
			throw new IsicresDocumentConnectorException(
					"Impossible to save the file  [" + document.getName() + "]",
					e);
		}
		
		// Se recupera el repositorio		
		AuthoringServiceSoapBindingStub authoringService = alfrescoConnection.getAuthoringService();
		ContentServiceSoapBindingStub contentRepository = alfrescoConnection.getContentRepository();
		
		// Se actualiza el repositorio
		try {
			document = utilsAlfresco.updateCML(document, authoringService, contentRepository);
		} catch (Exception e) {
			throw new IsicresDocumentConnectorException(
					"Impossible to save the file  [" + document.getName() + "]",
					e);
		}
				
		return document;*/
	}
}

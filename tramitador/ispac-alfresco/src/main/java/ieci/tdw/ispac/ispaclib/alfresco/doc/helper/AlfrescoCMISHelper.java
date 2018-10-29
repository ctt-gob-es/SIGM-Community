package ieci.tdw.ispac.ispaclib.alfresco.doc.helper;

import ieci.tdw.ispac.ispaclib.gendoc.config.Repository;
import ieci.tdw.ispac.ispaclib.utils.MapUtils;
import ieci.tdw.ispac.ispaclib.utils.StringUtils;
import ieci.tdw.ispac.ispaclib.utils.XmlFacade;
import ieci.tdw.ispac.ispaclib.utils.XmlTag;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.log4j.Logger;

//import ctsi.StringCTSI;
import ctsi.alfresco.cmis.CatalogoAlfrescoCmis;
import es.ieci.tecdoc.isicres.document.connector.alfresco.utils.UtilsAlfresco;
import es.ieci.tecdoc.isicres.document.connector.alfresco.vo.AlfrescoDatosEspecificosVO;
import es.ieci.tecdoc.isicres.document.connector.alfresco.vo.AlfrescoDatosEspecificosValueVO;
import es.ieci.tecdoc.isicres.document.connector.alfrescoCMIS.AlfrescoCMISConnection;

public class AlfrescoCMISHelper extends AlfrescoHelper {

	/**
	 * Logger de la clase.
	 */
	private static final Logger logger = Logger.getLogger(AlfrescoCMISHelper.class);

	public static boolean existsDocument(Repository repository, String uid) throws Exception {
		return (getDocument(repository, uid) != null);
	}

	public static AlfrescoDatosEspecificosVO getMetadatos(Repository repository, String uid) throws Exception {
		
		Document document = getDocument(repository, uid);
		
		List<Property<?>> metadatos = null;
		if (document != null) {
			metadatos = document.getProperties();
		}
		
//	    Node[] nodes = getNodes(repository, uid);
		AlfrescoDatosEspecificosVO datosEspecificosVO = getDatosEspecificos(repository);
	    
	    Map<String, AlfrescoDatosEspecificosValueVO> metadatosOutputMap = new HashMap<String, AlfrescoDatosEspecificosValueVO>();
	    
	    if (metadatos != null) {
	    	Iterator<Property<?>> itMetadatos = metadatos.iterator();
	    	while (itMetadatos.hasNext()) {
	    		Property<?> metadato = itMetadatos.next();
	    		//Object value = property.getValue();
	    		
	    		// Obtenemos el nombre del metadato
	    		String metadatoName = metadato.getLocalName();
	    		String metadatoValor = metadato.getValueAsString();
	    		
	    		// Comprobamos si está en la definición
	    		AlfrescoDatosEspecificosValueVO alfrescoDatosEspecificosValueVO = (AlfrescoDatosEspecificosValueVO) datosEspecificosVO.getValues().get(metadatoName);
	    		if (alfrescoDatosEspecificosValueVO!=null) {
                	alfrescoDatosEspecificosValueVO.setValue(metadatoValor);
                	metadatosOutputMap.put(metadatoName, alfrescoDatosEspecificosValueVO);
                }
	    	}
	    }
	    /*if (metadatos != null) {
	    	for(int i=0;i<nodes.length;i++){
	    		Node node = nodes[i];
	    		NamedValue[] prop = node.getProperties();	  
	    		for(int f=0;f<prop.length;f++){
	    			NamedValue namedValue = (NamedValue)prop[f];
	    			String name = namedValue.getName().split("}")[1];
	    			AlfrescoDatosEspecificosValueVO alfrescoDatosEspecificosValueVO = null;			
	    			alfrescoDatosEspecificosValueVO = (AlfrescoDatosEspecificosValueVO) datosEspecificosVO.getValues().get(name);
                    if (alfrescoDatosEspecificosValueVO!=null) {
                    	alfrescoDatosEspecificosValueVO.setValue(namedValue.getValue());
                    	metadata.put(name, alfrescoDatosEspecificosValueVO);
                    }
                }
            }
        }*/
	    
	    datosEspecificosVO.setValues(metadatosOutputMap);
	    
		return datosEspecificosVO;
	}

	// [Ruben CMIS] Incorporo el metodo getMetadato que teníamos de CR en SigemV4 adaptado a CMIS 
public static String getMetadato(Repository repository, String uid, String key) throws Exception {
		
		Document document = getDocument(repository, uid);
		
		List<Property<?>> metadatos = null;
		if (document != null) {
			metadatos = document.getProperties();
		}
		
	    if (metadatos != null) {
	    	Iterator<Property<?>> itMetadatos = metadatos.iterator();
	    	while (itMetadatos.hasNext()) {
	    		Property<?> metadato = itMetadatos.next();
	    		
	    		// Obtenemos el nombre del metadato
	    		String metadatoName = metadato.getLocalName();
	    		if (metadatoName.equals(key)) {
	    			return metadato.getValueAsString();
                }
	    	}
	    }
		
		return null;
	}

//[Ruben CMIS] Incorporo el metodo setMetadato que teníamos de CR en SigemV4 adaptado a CMIS
public static void setMetadato(Repository repository, String uid, String name, String value) throws Exception {

	Document document = getDocument(repository, uid);
	
	if (!document.equals(null)) {
		Map<String, Object> new_properties = new HashMap<String, Object>();
		
		if (name != null && !name.isEmpty()) {
			new_properties.put(name, value);
			document.updateProperties(new_properties);
		}
	}
	
}

	public static void checkFolderPath(Repository repository, String properties) throws Exception {
		
		AlfrescoCMISConnection alfrescoConnection = new AlfrescoCMISConnection();
		
		try {
			
			alfrescoConnection.connection(getConfiguration(repository));

			String rootFolder = repository.getFolderPath();
			
			String newFolder = UtilsAlfresco.getFolderPathYYYYMMDD();
			
			if (logger.isInfoEnabled()) {
				logger.info("folderPath: " + rootFolder + "/" + newFolder);
			}
 
			// El folderPath debe tener este formato "/Espacios personales/Sigem/Documentos"
			// Este método crea el espacio si no existe
			//folder = "/Espacios personales/Sigem/Documentos/Test1";
			checkFolderPath(alfrescoConnection, rootFolder, newFolder);

		} finally {
			alfrescoConnection.endConnection();
		}
	}

	private static void checkFolderPath(AlfrescoCMISConnection alfrescoConnection, String rootPath, String childPath) 
			throws Exception {
		
		// Servicio de gestión del repositorio
		//RepositoryServiceSoapBindingStub repositoryService = alfrescoConnection.getRepository();
		CatalogoAlfrescoCmis catalogoCmis = alfrescoConnection.getCatalogoAlfrescoCmis();
		
		if (StringUtils.isNotBlank(rootPath)) {
			
			// Comprobar si existe el espacio
			//Reference reference = new Reference(STORE, null, folderPath);
			//repositoryService.get(new Predicate(new Reference[] { reference }, STORE, null));
			Folder rootFolder = catalogoCmis.getSpaceFromPath(rootPath);
			
			if (rootFolder == null) {
				throw new Exception("No existe el rootPath " + rootPath);
			}
		}
		
		if (StringUtils.isNotBlank(childPath)) {
			// Parte variable, la creamos si no existe
			String fullPath = rootPath + "/" + childPath;
			if (catalogoCmis.getSpaceFromPath(fullPath) == null) {
				logger.info("Creamos el childPath " + childPath);
				catalogoCmis.createSpaceFromPATH(rootPath, childPath, childPath);
			}
		}
	}


	public static String getDocumentMimeType(Repository repository, String uid) throws Exception {
		
		String mimeType = null;
		
		Document document = getDocument(repository, uid);
		if (document != null) {
			mimeType = document.getContentStreamMimeType();
		}

		return mimeType;
	}
	
	public static int getDocumentSize(Repository repository, String uid) throws Exception {
		
		int size = 0;
		
		Document document = getDocument(repository, uid);
		if (document != null) {
			size = (int)document.getContentStreamLength();
		}

		return size;
	}

	private static Document getDocument(Repository repository, String uid) throws Exception {

		Document doc = null; 
		
		if (StringUtils.isNotBlank(uid)) {
			
			AlfrescoCMISConnection alfrescoConnection = new AlfrescoCMISConnection();
			
			try { 
				alfrescoConnection.connection(getConfiguration(repository));
				
				// Catálogo de servicios
				CatalogoAlfrescoCmis catalogoAlfrescoCmis = alfrescoConnection.getCatalogoAlfrescoCmis();
				
				doc = catalogoAlfrescoCmis.readFile(uid, null);
				
				return doc;
				// Servicio de gestión de contenidos
				//ContentServiceSoapBindingStub contentService = alfrescoConnection.getContentRepository();
				
				//Reference reference = new Reference(STORE, uid, null);	
			    //Content[] readResult = contentService.read(new Predicate(new Reference[]{reference}, STORE, null), Constants.PROP_CONTENT);
			    //if (readResult != null) {
				//   content = (Content) readResult[0];		    
				//}
			    
			} finally {
				alfrescoConnection.endConnection();
			}
		}
		
		return doc;
	}

    // [Josemi #615496] Adaptación de CMIS para la actualización de documentos  
	@SuppressWarnings("unchecked")
	public static String updatePropertiesXML(Repository repository, String uid, String newProperties) throws Exception {

	    String properties = "";

	    XmlFacade newPropertiesXML = null;
	    if (StringUtils.isNotBlank(newProperties)) {
	    	newPropertiesXML = new XmlFacade(newProperties);
	    }
	    
		AlfrescoDatosEspecificosVO datosEspecificos = getMetadatos(repository, uid);
		if (datosEspecificos != null) {
			Map<String, AlfrescoDatosEspecificosValueVO> values = datosEspecificos.getValues();
			if (MapUtils.isNotEmpty(values)) {
				for (Iterator<String> keyIt = values.keySet().iterator(); keyIt.hasNext();) {
					String name = keyIt.next();
					
					String value = null;
					
					if (newPropertiesXML != null) {
						value = newPropertiesXML.get("/doc_properties/property[name=\"" + name + "\"]/value");
					} 

					if (value == null) {
						AlfrescoDatosEspecificosValueVO alfrescoDatosEspecificosValueVO = (AlfrescoDatosEspecificosValueVO) values.get(name);
						if (alfrescoDatosEspecificosValueVO != null) {
							value = alfrescoDatosEspecificosValueVO.getValue();
						}
					}
					
                	properties += XmlTag.newTag("property", XmlTag.newTag("name", name) 
                			+ XmlTag.newTag("value", XmlTag.newCDATA(StringUtils.nullToEmpty(value))));
				}
			}
		}

		String xml = XmlTag.getXmlInstruction("ISO-8859-1") + XmlTag.newTag("doc_properties", properties);
		
		return xml;
	}

	
}

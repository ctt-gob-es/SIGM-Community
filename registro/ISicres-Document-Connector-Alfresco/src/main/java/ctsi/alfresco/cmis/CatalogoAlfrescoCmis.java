package ctsi.alfresco.cmis;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.chemistry.opencmis.client.api.CmisObject;
import org.apache.chemistry.opencmis.client.api.Document;
import org.apache.chemistry.opencmis.client.api.Folder;
import org.apache.chemistry.opencmis.client.api.ItemIterable;
import org.apache.chemistry.opencmis.client.api.ObjectId;
import org.apache.chemistry.opencmis.client.api.OperationContext;
import org.apache.chemistry.opencmis.client.api.Property;
import org.apache.chemistry.opencmis.client.api.QueryResult;
import org.apache.chemistry.opencmis.client.runtime.OperationContextImpl;
import org.apache.chemistry.opencmis.commons.PropertyIds;
import org.apache.chemistry.opencmis.commons.data.Ace;
import org.apache.chemistry.opencmis.commons.data.Acl;
import org.apache.chemistry.opencmis.commons.data.ContentStream;
import org.apache.chemistry.opencmis.commons.enums.AclPropagation;
import org.apache.chemistry.opencmis.commons.enums.BaseTypeId;
import org.apache.chemistry.opencmis.commons.enums.VersioningState;
import org.apache.chemistry.opencmis.commons.exceptions.CmisContentAlreadyExistsException;
import org.apache.chemistry.opencmis.commons.exceptions.CmisObjectNotFoundException;
import org.apache.chemistry.opencmis.commons.impl.dataobjects.ContentStreamImpl;
import org.apache.log4j.Logger;
//import org.codehaus.jackson.map.ObjectMapper;
//import org.codehaus.jackson.map.annotate.JsonSerialize.Inclusion;

import com.google.api.client.http.ByteArrayContent;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpContent;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpResponse;
import com.google.api.client.http.HttpResponseException;

//import ctsi.CryptoCTSI;
//import ctsi.ExceptionCTSI;
//import ctsi.GeneralCTSI;
//import ctsi.alfresco.rest.model.GroupChildrens;
//import ctsi.alfresco.rest.model.ListUsers;
//import ctsi.alfresco.rest.model.User;

public class CatalogoAlfrescoCmis extends BaseCmis{
	private static Logger log = Logger.getLogger(CatalogoAlfrescoCmis.class);
	
	public CatalogoAlfrescoCmis(Properties p) throws Exception{
		super (p);	
	}
	public CatalogoAlfrescoCmis(String alfrescoServer, String alfrescoPort, String alfrescoUser, String alfrescoPwd, String alfrescoVersion) throws Exception{
		super(alfrescoServer, alfrescoPort, alfrescoUser, alfrescoPwd, alfrescoVersion);
	}
	
	
	/**
	 * ###############################################################################################################################
	 * **************************** CMIS: FICHEROS (lectura / escritura / aspectos y metadatos) **************************************
	 * ###############################################################################################################################
	 * **/	
	
	public Document readFile(String guid) throws Exception {
		return readFile (guid, null);
	}
		
	/**
	 * Permite leer un fichero desde alfresco identificado por su guid o por su path
	 * 
	 * @param guid - Identificador del documento
	 * @param alfrescoPath - Path del fichero en alfresco (No es necesario)
	 * @return AlfrescoFile object
	 * @throws ExceptionCTSI
	 */
	public Document readFile(String guid, String alfrescoPath) throws Exception {	
		if(!guid.isEmpty()){
			return (Document) getCmisSession().getObject(guid);
		}else{
			if(!alfrescoPath.isEmpty()){
				CmisObject obj =  getCmisSession().getObjectByPath(alfrescoPath);
				if(obj.getBaseTypeId().equals(BaseTypeId.CMIS_DOCUMENT)) return (Document)obj;
				else throw new Exception("No se ha encontrado un elemento tipo documento en el path indicado. Tipo encontrado:" + obj.getBaseType().getDisplayName());
			}else{
				throw new Exception("Debe indicar el guid o el path del fichero a consultar");
			}
		}
	}
	
	/**
	 * 
	 * Permite leer las propiedades title y description de un nodo sin obtener su contenido
	 * 
	 * @param guid - Identificador del documento
	 * @param alfrescoPath - Path del fichero en alfresco (No es obligatorio)
	 * @return AlfrescoFile object
	 * @throws ExceptionCTSI
	 */
	//TODO
	@Deprecated
	public Document readFileWithoutContent(String guid, String alfrescoPath) throws Exception {
		return readFile(guid, alfrescoPath);
	}
	
	/**
	 * Permite escribir un fichero en un path de alfresco
	 * 
	 * @param f documento documento a escribir
	 * @param file_description pequeña descripción del documento en alfresco
	 * @param alfrescoPath Path del documento ej:/SUITEDATOS/TEST
	 * @throws ExceptionCTSI
	 */
	public String writeFile(File f, String file_description, String alfrescoPath) throws Exception {
		try{
			return writeFile(new FileInputStream(f), f.getName(), file_description, alfrescoPath);
		}catch(FileNotFoundException fne){
			throw new Exception("No se ha podido recuperar el fichero a escribir", fne);
		}
	}
	
	/**
	 * Permite escribir un fichero en un path de alfresco y configurar si queremos añadir un timestamp al inicio del nodo o no
	 * 
	 * @param f documento documento a escribir
	 * @param file_description pequeña descripción del documento en alfresco
	 * @param alfrescoPath Path del documento ej:/SUITEDATOS/TEST
	 * @param addTimestamp Permite configurar si añadimos o no un timestamp al inicio del nodo.
	 * @throws ExceptionCTSI
	 */
	public String writeFile(File f, String file_description, String alfrescoPath,  boolean addTimestamp) throws Exception {
		try{
			return writeFile(new FileInputStream(f), f.getName(), file_description, alfrescoPath, addTimestamp);
		}catch(FileNotFoundException fne){
			throw new Exception("No se ha podido recuperar el fichero a escribir", fne);
		}
	}
	
	/**
	 * Permite escribir un fichero en un path de alfresco
	 * NOTA: Añade un timestamp al inicio del nodo para evitar duplicidades.
	 * 
	 * @param is contenido del fichero
	 * @param name_file nombre del fichero
	 * @param file_description pequeña descripción del documento en alfresco
	 * @param alfrescoPath Path del documento ej:/SUITEDATOS/TEST
	 * @throws ExceptionCTSI
	 */
	public String writeFile(InputStream is, String name_file, String file_description, String alfrescoPath) throws Exception {
		return writeFile(is, name_file, file_description, alfrescoPath, true);
	}
	
	/**
	 * Permite escribir un fichero en un path de alfresco y configurar si queremos añadir un timestamp al inicio del nodo o no
	 *  
	 * @param is contenido del fichero
	 * @param name_file nombre del fichero
	 * @param file_description pequeña descripción del documento en alfresco
	 * @param alfrescoPath Path del documento ej:/SUITEDATOS/TEST
	 * @param addTimestamp Permite configurar si añadimos o no un timestamp al inicio del nodo.
	 * @throws ExceptionCTSI
	 */
	public String writeFile(InputStream is, String name_file, String file_description, String alfrescoPath, boolean addTimestamp) throws Exception {
		return writeFileWithAspect(is, name_file, file_description, alfrescoPath, null, addTimestamp);
		
	}
	
	/**
	 * Permite escrbir un fichero en un path de alfresco y asociarle un aspecto 
	 * 
	 * @param f documento documento a almacenar
	 * @param file_description pequeña descripción del documento en alfresco
	 * @param aspect aspecto a añadir (debe extender la clase IUpctAspect)
	 * @param alfrescoPath Path del documento ej:/SUITEDATOS/TEST
	 * @return
	 * @throws ExceptionCTSI
	 */
	public String writeFileWithAspect(File f, String file_description, List<String> aspect, String alfrescoPath, boolean addTimestamp) throws Exception {
		try{	
			return writeFileWithAspect(new FileInputStream(f), f.getName(), file_description, alfrescoPath, aspect, addTimestamp);
		}catch(FileNotFoundException fne){
			throw new Exception("No se ha podido recuperar el fichero a escribir", fne);
		}
	}
	
	
	/**
	 * Permite escrbir un fichero en un path de alfresco y asociarle un aspecto 
	 * 
	 * @param is contenido del fichero
	 * @param name_file nombre del fichero
	 * @param aspects Lista de aspectos 
	 * @param alfrescoPath Path del documento ej:/SUITEDATOS/TEST
	 * @return
	 * @throws ExceptionCTSI
	 */
	public String writeFileWithAspect(InputStream is, String name_file, String file_description, String alfrescoPath, List<String> aspects, boolean addTimestamp) throws Exception {
		return writeFileWithAspectAndMetadata(is, name_file, file_description, alfrescoPath, aspects, null, addTimestamp);
	}
	
	/**
	 * Permite escrbir un fichero en un path de alfresco, asociarle un aspecto y añadirle metadatos 
	 * 
	 * @param is contenido del fichero
	 * @param name_file nombre del fichero
	 * @param aspects Lista de aspectos
	 * @param metadata Mapa de metadatos a añadir
	 * @param alfrescoPath Path del documento ej:/SUITEDATOS/TEST
	 * @return
	 * @throws ExceptionCTSI
	 */
	public String writeFileWithAspectAndMetadata(InputStream is, String name_file, String file_description, String alfrescoPath, List<String> aspects, Map<String, Object> metadata, boolean addTimestamp) throws Exception {
		Map<String, Object> properties = new HashMap<String, Object>();
		properties.put(PropertyIds.OBJECT_TYPE_ID, CMIS_DOCUMENT);
		
		// Assign name
		//Se parsea el nombre del archivo para evitar la introducción de carácteres incorrectos
		String nameFile = name_file.replaceAll("[\\/:*?\"<>|]", "_");
        if(addTimestamp) nameFile = System.currentTimeMillis() + "#_"+ nameFile;
        properties.put(PropertyIds.NAME, nameFile);
        properties.put(PropertyIds.DESCRIPTION, file_description);
        
        final Folder folder = (Folder)getCmisSession().getObjectByPath(alfrescoPath);
		
        if(!aspects.isEmpty()){
        	properties.put(PropertyIds.SECONDARY_OBJECT_TYPE_IDS, aspects);
        }
        
        if(metadata != null && !metadata.isEmpty()){
        	for (Map.Entry<String, Object> entry : metadata.entrySet()) {
        		properties.put(entry.getKey(), entry.getValue());
        	}
        }
               
        //Content 
        final String mymetype = this.mediaTypes.getContentType(name_file);
        final ContentStream contentStream = new ContentStreamImpl(nameFile, null, mymetype, is);
        
        Document d = null;
        try {
        	d = folder.createDocument(properties, contentStream, VersioningState.MAJOR);
        } catch (CmisContentAlreadyExistsException ccaee) {
            d = (Document) getCmisSession().getObjectByPath(folder.getPath() + "/" + nameFile);
            log.info("El Documento ya existe: " + nameFile);
        }
        return BaseCmis.getGuid(d.getId());        
	}
	
	/**
	 * Permite actualizar el contenido de un fichero (Sin versionado)
	 * 
	 * @param guid Documento sobre el que actuar
	 * @param newInputStream Nuevo contenido
	 * @return Documento con los nuevos datos actualizados
	 * @throws ExceptionCTSI
	 */
	public String updateFile(String guid, InputStream newInputStream) throws Exception {
		return 	updateFileWithVersion(guid, newInputStream, false, null);
	}
	
	
	/**
	 * Permite actualizar el contenido de un fichero versionando el mismo
	 * 
	 * @param guid Documento sobre el que actuar
	 * @param newInputStream Nuevo contenido
	 * @param doVersions flag para aplicar versionado o no
	 * @param checkinComments Comentarios de la nueva versión
	 * @return Documento con los nuevos datos actualizados
	 * @throws ExceptionCTSI
	 */
	public String updateFileWithVersion(String guid, InputStream newInputStream, boolean doVersions, String checkinComment) throws Exception {
		Document documento = readFile(guid);
		ContentStream contentStreamNew = new ContentStreamImpl(documento.getName(), null, documento.getContentStreamMimeType(), newInputStream);
		
		if(doVersions){		
			ObjectId pwcId = documento.checkOut();
			Document pwc = (Document) getCmisSession().getObject(pwcId);
			pwc.setContentStream(contentStreamNew, true);
			ObjectId newVersionId = pwc.checkIn(true, null, null, checkinComment);
			return BaseCmis.getGuid(String.valueOf(newVersionId.getId()));
		}else{
			documento.setContentStream(contentStreamNew, true);
			return BaseCmis.getGuid(documento.getId());
		}
	}
	
	

	
	/**
	 * Permite eliminar un documento de alfreso
	 * 
	 * @param doc Documento a eliminar
	 * @throws ExceptionCTSI
	 */
	public void deleteDocument(Document doc) throws Exception {
		getCmisSession().delete(doc);
	}
	
	/**
	 * Permite añadir un aspecto a un documento alfresco
	 * 
	 * @param doc Documento sobre el que actuar
	 * @param new_aspect Aspecto a añadir.
	 * @return Documento con los nuevos datos actualizados
	 * @throws ExceptionCTSI
	 */
	public Document addAspectToExistingDocument(Document doc, String new_aspect){
		
		List<Object> aspects = doc.getProperty(PropertyIds.SECONDARY_OBJECT_TYPE_IDS).getValues();
		Map<String, Object> properties = new HashMap<String, Object>();		
		
		if(!aspects.isEmpty()){
			if (!aspects.contains(new_aspect)) {
				aspects.add(new_aspect);
				properties.put(PropertyIds.SECONDARY_OBJECT_TYPE_IDS, aspects);
				doc.updateProperties(properties);
			}else{
	            log.info("Aspecto " + new_aspect + " ya está aplicado a " + doc.getPaths()+"/");
			}
		}
		return doc;
	}
	
	/**
	 * Permite añadir un aspecto a un documento alfresco
	 * 
	 * @param doc Documento sobre el que actuar
	 * @param new_aspect Aspecto a añadir.
	 * @return Documento con los nuevos datos actualizados
	 * @throws ExceptionCTSI
	 */
	public Document addAspectToExistingDocument(Document doc, String aspect, String property, Object value){
		
		List<Object> aspects = doc.getProperty(PropertyIds.SECONDARY_OBJECT_TYPE_IDS).getValues();
		Map<String, Object> properties = new HashMap<String, Object>();		
		
		if(!aspects.isEmpty()){
			aspects.add(aspect);
			properties.put(PropertyIds.SECONDARY_OBJECT_TYPE_IDS, aspects);
			properties.put(property, value);
			doc.updateProperties(properties);
		}
		return doc;
	}
	
	/**
	 * Permite añadir metadatos a un documento alfresco
	 * 
	 * @param doc Documento sobre el que actuar
	 * @param metadata Metadatos a añadir.
	 * @return Documento con los nuevos datos actualizados
	 * @throws ExceptionCTSI
	 */
	public Document addMetadataToExistingDocument(Document doc, Map<String, Object> metadata){
		Map<String, Object> new_properties = new HashMap<String, Object>();		
		
		if(metadata != null && !metadata.isEmpty()){
        	for (Map.Entry<String, Object> entry : metadata.entrySet()) {
        		log.debug("Añadiento el metadato " + entry.getKey());
        		new_properties.put(entry.getKey(), entry.getValue());
        	}
        	doc.updateProperties(new_properties);
        }
		
		return doc;
	}
	
	/**
	 * Permite consultar el valor de un metadato de un documento alfresco
	 * 
	 * @param doc Documento sobre el que consultar
	 * @param metadata_key Metadato a consultar.
	 * @return Property con los campos del metadato
	 * @throws ExceptionCTSI
	 */
	public Property<?> getDocumentMetadata(Document doc, String metadata_key){
		List<Property<?>> Lmetadata = doc.getProperties();
		
		for(Property<?> metadata : Lmetadata){
			if (metadata.getId().equals(metadata_key)) return metadata;
		}
		
		return null;
	}
	
	/**
	 * ###############################################################################################################################
	 * **************************** CMIS: ESPACIOS (LISTADO, CREACIÓN, MODIFICACIÓN Y ELIMINACIÓN)****************************************
	 * ###############################################################################################################################
	 * **/
	/***
	 * Permite obtener un espacion identificado por su GUID
	 * 
	 * @param GUID identificador del espacio
	 * 
	 * @return espacio
	 * @throws ExceptionCTSI
	 */
	public Folder getSpaceFromGUID(String guid) throws Exception{
		try{
			CmisObject object = getCmisSession().getObject(guid);
			if(object.getBaseTypeId().equals(BaseTypeId.CMIS_FOLDER)){
				return (Folder)object;
			}else{
				throw new Exception("El identificador consultado no contiene un objeto de tipo Folder.");
			}
		}catch(CmisObjectNotFoundException confe){
			return null;
		}
	}
	
	/***
	 * Permite obtener un espacion identificado por su PATH
	 * 
	 * @param GUID identificador del espacio
	 * 
	 * @return espacio
	 * @throws ExceptionCTSI
	 */
	public Folder getSpaceFromPath(String alfrescoPath) throws Exception{
		try{
			CmisObject object = getCmisSession().getObjectByPath(alfrescoPath);
			if(object.getBaseTypeId().equals(BaseTypeId.CMIS_FOLDER)){
				return (Folder)object;
			}else{
				throw new Exception("El identificador consultado no contiene un objeto de tipo Folder.");
			}
		}catch(CmisObjectNotFoundException confe){
			return null;
		}
	}
	
	
	/***
	 * Permite obtener los espacios hijos de un espacio identificado por su GUID.
	 * 
	 * @param GUID identificador del espacio padre
	 * 
	 * @return lista de espacios
	 * @throws ExceptionCTSI
	 */
	public ItemIterable<CmisObject> getChildrenSpacesFromGUID(String guid) throws Exception{
		try{
			final CmisObject rootObject = (Folder)getCmisSession().getObject(guid);
        	if(rootObject.getBaseTypeId().equals(BaseTypeId.CMIS_FOLDER)){
        		final Folder rootFolder = (Folder) rootObject;
        		return rootFolder.getChildren();
        	}else{
        		throw new Exception("El identificador consultado no contiene un objeto de tipo Folder.");
        	}
		}catch(Exception ex){
			String err = "Se ha producido un error al intentar hacer el listado para el guid " + guid;
	    	log.error(err,ex);
	    	throw new Exception(err,ex);
		}  
	}
	
	/***
	 * Permite obtener los espacios hijos de un espacio identificado por su PATH.
	 * 
	 * @param path path del espacio padre
	 * 
	 * @return lista de espacios
	 * @throws ExceptionCTSI
	 */
	public ItemIterable<CmisObject> getChildrenSpacesFromPath(String path) throws Exception{
		try{
			final CmisObject rootObject = (Folder)getCmisSession().getObjectByPath(path);
        	if(rootObject.getBaseTypeId().equals(BaseTypeId.CMIS_FOLDER)){
        		final Folder rootFolder = (Folder) rootObject;
        		return rootFolder.getChildren();
        	}else{
        		throw new Exception("El path consultado no contiene un objeto de tipo Folder.");
        	}
		}catch(Exception ex){
			String err = "Se ha producido un error al intentar hacer el listado para los hijos de " + path;
	    	log.error(err,ex);
	    	throw new Exception(err,ex);
		}
	}
	
	/***
	 * Permite crear un espacio identificando el padre por su GUID
	 * 
	 * @param parentGUID GUID del padre donde crear el espacio
	 * @param nameOfNewFolder Nombre del nuevo espacio
	 * @param titleOfNewFolder Título para el nuevo espacio
	 * @returnGUID del espacio creado
	 * @throws ExceptionCTSI
	 */
	public String createSpaceFromGUID(String parentGUID, String nameOfNewFolder, String titleOfNewFolder) throws Exception{
		return createSpace(null, parentGUID, nameOfNewFolder, titleOfNewFolder);
		
	}
	/***
	 * Permite crear un espacio identificando al padre por su path.
	 * 
	 * @param parentPath Path del padre donde crear el espacio
	 * @param nameOfNewFolder Nombre del nuevo espacio
	 * @param titleOfNewFolder Título para el nuevo espacio
	 * @return GUID del espacio creado
	 * @throws ExceptionCTSI
	 */
	public String createSpaceFromPATH(String parentPath, String nameOfNewFolder, String titleOfNewFolder) throws Exception{
		return createSpace(parentPath, null, nameOfNewFolder, titleOfNewFolder);
	}
	private String createSpace(String parentPath, String parentGUID, String nameOfNewFolder, String titleOfNewFolder) throws Exception{
		CmisObject rootObject;			
		if(parentPath != null && parentGUID== null){
			rootObject = getSpaceFromPath(parentPath);
			if(rootObject == null) throw new Exception("No se ha encontrado el nodo en el path " + parentPath);
		}else{
			rootObject = getSpaceFromGUID(parentGUID);
			if(rootObject == null) throw new Exception("No se ha encontrado el nodo identificado por " + parentGUID);
		}
		if(!rootObject.getBaseTypeId().equals(BaseTypeId.CMIS_FOLDER)){
			throw new Exception("El objecto padre no contiene un objeto de tipo Folder.");
    	}
		Folder rootFolder = (Folder) rootObject;
		
		
		//Comprobamos que el nombre no contenga "/", lo que implicaría que es un path en lugar de solo un folder
		if(nameOfNewFolder.contains("/")){
			String [] path = nameOfNewFolder.split("/");
			Folder newFolderOnPath = rootFolder;
			String auxPath = parentPath;
			for(int i = 0; i< path.length; i++){
				Folder auxFolder = getSpaceFromPath(auxPath + "/" + path[i]);
				if(auxFolder == null){
					newFolderOnPath = createFolder(newFolderOnPath, path[i], null);
				}else{
					newFolderOnPath = auxFolder;
				}
				auxPath = auxPath+"/"+path[i];
			}
			return BaseCmis.getGuid(newFolderOnPath.getId());
		}else{
			//Comprobamos que no exista el nodo hijo
			Folder newFolder = getSpaceFromPath(rootFolder.getPath() + "/" + nameOfNewFolder);
			
			if(newFolder == null){
				newFolder = createFolder(rootFolder, nameOfNewFolder, titleOfNewFolder);
			}
			
			return BaseCmis.getGuid(newFolder.getId());
			
		}
		//Comprobamos que no exista el nodo hijo
		/*QueryStatement stmt = getCmisSession().createQueryStatement("IN_FOLDER(?) AND cmis:name=?");
		stmt.setString(1, rootFolder.getId());
		stmt.setString(2, nameOfNewFolder);
		
		ItemIterable<CmisObject> childs = getCmisSession().queryObjects("cmis:folder", stmt.toString(), false, getCmisSession().getDefaultContext());
		if(childs.getTotalNumItems() == 0)*/
	}
	
	private Folder createFolder(Folder rootFolder, String nameOfNewFolder, String titleOfNewFolder){
		Map<String,String> newFolderProps = new HashMap<String, String>();
		newFolderProps.put(PropertyIds.OBJECT_TYPE_ID, "cmis:folder");
		newFolderProps.put(PropertyIds.NAME , nameOfNewFolder);
		newFolderProps.put(PropertyIds.DESCRIPTION , (titleOfNewFolder != null)?titleOfNewFolder:nameOfNewFolder);
		
		return rootFolder.createFolder(newFolderProps);
	}
	
	
	/**
	 * Elimina un Espacio idenfitado por su path
	 * 
	 * @param path Path del espacio en alfresco
	 * @return GUID del espacio borrado
	 * */
	public String deleteSpaceFromPath(String path)throws Exception{
		CmisObject object = getCmisSession().getObjectByPath(path);
		String guid = object.getId();
		object.delete();
		return BaseCmis.getGuid(guid);
	}	
	
	/**
	 * Elimina un Espacio idenfitado por su GUID
	 * 
	 * @param guid GUID del espacio en alfresco
	 * */
	public void deleteSpaceFromGUID(String guid)throws Exception{
		CmisObject object = getCmisSession().getObject(guid);
		object.delete();
	}
	
	
	/**
	 * ###############################################################################################################################
	 * **************************** CMIS: PERMISOS (establecer y consultar permisos en espacios)****************************************
	 * ###############################################################################################################################
	 * **/
	/**
	 * Permite obtener una lista de permisos del nodo(identificado por su guid) indicado por parámetro
	 * 
	 * @param guid GUID del nodo sobre el que queremos obtener los permisos
	 * @return Lista de permisos o null si no hay datos
	 * @throws ExceptionCTSI
	 */
	public Acl getPermissionFromGUID(String guid) throws Exception{
		OperationContext operationContext = new OperationContextImpl();
		operationContext.setIncludeAcls(true);
		CmisObject object = getCmisSession().getObject(guid, operationContext);
		
		return object.getAcl();
	}
	
	/**
	 * Permite obtener una lista de permisos del path indicado por parámetro
	 * 
	 * @param path Path del nodo sobre el que obtener los permisos
	 * @return Lista de permisos
	 * @throws ExceptionCTSI
	 */
	public Acl getPermissionFromPATH(String path) throws Exception{  
		OperationContext operationContext = new OperationContextImpl();
		operationContext.setIncludeAcls(true);
		CmisObject object = getCmisSession().getObjectByPath(path, operationContext);
		
		return object.getAcl();
	}
	
	/**
	 * Permite añadir permisos a un nodo identificado por su guid
	 * 
	 * @param guid identificador del nodo o Store
	 * @param acl Lista de permisos a añadir
	 * @return lista de permisos
	 * @throws ExceptionCTSI
	 */
	public Acl addPermissionFromGUID(String guid, Ace ace) throws Exception{
		try{
			CmisObject object = getCmisSession().getObject(guid);
			List<Ace> aces = new LinkedList<Ace>();
			aces.add(ace);
			return object.addAcl(aces, AclPropagation.OBJECTONLY);
	  }catch(Exception ex){
		  String err = "Se ha producido un error al intentar añadir permisos ";
		  log.error(err,ex);
		  throw new Exception(err,ex);	
	  }    
	}	
	
	/**
	 * Permite añadir permisos a un nodo identificado por su PATH
	 * 
	 * @param path path del nodo o Store
	 * @param acl Lista de permisos a añadir
	 * @return Lista de permisos
	 * @throws ExceptionCTSI
	 */
	public Acl addPermissionFromPATH(String path, Ace ace) throws Exception{
		try{
			CmisObject object = getCmisSession().getObjectByPath(path);
			List<Ace> aces = new LinkedList<Ace>();
			aces.add(ace);
			return object.addAcl(aces, AclPropagation.OBJECTONLY);
	  }catch(Exception ex){
		  String err = "Se ha producido un error al intentar añadir permisos ";
		  log.error(err,ex);
		  throw new Exception(err,ex);	
	  }     
	}
	
	/****
	 * Permite eliminar permisos a un nodo identificado por su GUID
	 * 
	 * @param guid identificador del noto o Store
	 * @param acl Lista de permisos a eliminar
	 * @return Lista de permisos
	 * @throws ExceptionCTSI
	 */
	public Acl removePermissionFromGUID(String guid, List<Ace> ace) throws Exception{
		try{
			CmisObject object = getCmisSession().getObject(guid);
			return object.removeAcl(ace, AclPropagation.OBJECTONLY);
		}catch(Exception ex){
			String err = "Se ha producido un error al intentar eliminar los permisos ";
			log.error(err,ex);
			throw new Exception(err,ex);	
		}      
	}
	
	/****
	 * Permite eliminar permisos a un nodo identificado por su Path
	 * 
	 * @param path path del noto o Store
	 * @param acl Lista de permisos a eliminar
	 * @return Lista de permisos
	 * @throws ExceptionCTSI
	 */
	public Acl removePermissionFromPATH(String path, List<Ace> ace) throws Exception{
		try{
			CmisObject object = getCmisSession().getObjectByPath(path);
			return object.removeAcl(ace, AclPropagation.OBJECTONLY);
		}catch(Exception ex){
			String err = "Se ha producido un error al intentar eliminar los permisos ";
			log.error(err,ex);
			throw new Exception(err,ex);	
		}       
	}
	
	/****
	 * Permite establecer o eliminar la herencia de permisos sobre un nodo identificado por su GUID
	 * 
	 * @param guid GUID del noto o Store
	 * @param activo estado de la herencia
	 * @return Lista de permisos
	 * @throws ExceptionCTSI
	 */
	public boolean setHerenciaPermisosFromGUID(String guid, boolean activo) throws Exception{
		try{
			String url = getAlfrescoSlingshotUrl() + "doclib/permissions/{store_type}/{store_id}/{id}";
			url = url.replace("{store_type}", "workspace");
			url = url.replace("{store_id}", "SpacesStore");
			url = url.replace("{id}", guid);
			
			String jsonData = "{\"permissions\":[],\"isInherited\":#STATUS#}";
			if(activo){
				jsonData = jsonData.replace("#STATUS#", "true");
			}else{
				jsonData = jsonData.replace("#STATUS#", "false");
			}
			
			//System.out.println("URL WEB SCRIPT:" + url);
			HttpContent body = new ByteArrayContent("application/json", jsonData.getBytes());
			HttpRequest request = getRequestFactory().buildPostRequest(new GenericUrl(url), body);
			HttpResponse response = request.execute();
			log.debug("RESPONSE: " + response.parseAsString());
			if(response.isSuccessStatusCode()){
				return true;
			}else{
				return false;
			}
		}catch(HttpResponseException httpre){
			log.info(httpre.getMessage());
			return false;
		}catch(Exception ex){
			String err = "Se ha producido un error al intentar establecer la herencia de permissos sobre "+ guid;
			log.error(err,ex);
			throw new Exception(err,ex);	
		}
	}
	
	/****
	 * Permite establecer o eliminar la herencia de permisos sobre un nodo identificado por su path
	 * 
	 * @param path path del noto o Store
	 * @param activo estado de la herencia
	 * @return Lista de permisos
	 * @throws ExceptionCTSI
	 */
	public boolean setHerenciaPermisosFromPATH(String path, boolean activo) throws Exception{
		CmisObject object = getCmisSession().getObjectByPath(path);
		return this.setHerenciaPermisosFromGUID(object.getId(), activo);
		      	
	}
	
	
	/*public Group getGroupInfo(String shortName, boolean includeAuthorities) throws ExceptionCTSI {
		try {
			String url = getAlfrescoAPIUrl() + "groups/{shortName}";
			if(includeAuthorities) url = url + "/children?authorityType=USER";
			url = url.replace("{shortName}", shortName);
			
			HttpRequest request = getRequestFactory().buildGetRequest(new GenericUrl(url));
			System.out.println(request.execute().parseAsString());
			return request.execute().parseAs(Group.class);
		}catch(Exception ex){
			String err = "Se ha producido un error al consultar el grupo "+ shortName;
			log.error(err,ex);
			throw new ExceptionCTSI(err,ex);	
		}
	}*/
	
	/**
	 * ###############################################################################################################################
	 * ********************************************* CMIS: QUERYS *****************************************************************
	 * ###############################################################################################################################
	 * **/
	/**
	 * Permite realizar un query CMIS al repositorio.
	 * @param q query CMIS 
	 * 
	 * */
	public ItemIterable<QueryResult> doQuery(String q) throws Exception{
		return getCmisSession().query(q, false);
	}
	
	
	
	/**
	 * ###############################################################################################################################
	 * ********************************** REST: USUARIOS (CREACIÓN, MODIFICACIÓN Y ELIMINACIÓN)****************************************
	 * ###############################################################################################################################
	 * **/
	/****
	 * Permite comprobar si existe un usuario en Alfresco. 
	 * 
	 * @param userName identificador del usuario
	 */
	public boolean isUserCreated(String userName) throws Exception{
		try {
			String url = getAlfrescoAPIUrl() + "people/{username}";
			url = url.replace("{username}", userName);
			
			HttpRequest request = getRequestFactory().buildGetRequest(new GenericUrl(url));
			HttpResponse response = request.execute();
			log.debug(response.parseAsString());
			if(response.isSuccessStatusCode()){
				return true;
			}else{
				return false;
			}
		}catch(HttpResponseException httpre){
			log.info(httpre.getMessage());
			return false;
		}catch(Exception ex){
			String err = "Se ha producido un error al consultar el usuario "+ userName;
			log.error(err,ex);
			throw new Exception(err,ex);	
		}
	}
	
	/****
	 * Permite añadir un usuario a Alfresco
 	 * NOTA: Es obligatorio lanzar es función como admin
	 * 
	 * @param u User Usuario a añadir
	 * 
	 * @return boolean
	 */
	/*
	public boolean addUser(User u) throws Exception{
		try {
			//Comprobamos los datos obligatorios
			if(GeneralCTSI.vacio(u.getUserName()) || GeneralCTSI.vacio(u.getEmail()) || GeneralCTSI.vacio(u.getFirstName()) || GeneralCTSI.vacio(u.getLastName())) 
				throw new Exception("Los siguientes datos son obligatorios: Username, Email, FirstName y LastName");
			
			
			//Comprobamos que el usuario no esté ya creado
			if(isUserCreated(u.getUserName())) return true;
			else{
				String url = getAlfrescoAPIUrl() + "people";
				
				//Establecemos un password aleatorio
				u.setPassword(CryptoCTSI.getCSV(6));
				
				ObjectMapper mapper = new ObjectMapper();
				mapper.setSerializationInclusion(Inclusion.NON_NULL);
				String jsonUser = mapper.writeValueAsString(u);
				System.out.println("Usuario en formato json: " + jsonUser);
				HttpContent body = new ByteArrayContent("application/json", jsonUser.getBytes("UTF-8"));
				HttpRequest request = getRequestFactory().buildPostRequest(new GenericUrl(url), body);
				HttpResponse response = request.execute();
				
				log.debug("RESPONSE: " + response.parseAsString());
				if(response.isSuccessStatusCode()){
					return true;
				}else{
					return false;
				}
			}
		}catch(Exception ex){
			String err = "Se ha producido un error intentan añadir el usuario " + u.getUserName();
			String privateInfo = null;
			if(ex instanceof HttpResponseException){
				privateInfo = ((HttpResponseException)ex).getMessage();
			}
			log.error(err,ex);
			throw new Exception(err, privateInfo, ex, false);	
		}
	}
	*/
	
	
	/****
	 * Permite obtener los usuarios de alfresco
	 * 
	 * @param filterQuery query para filtar el resultado
	 * @return ListUsers
	 */
	/*
	public ListUsers getUsers(String filterQuery) throws Exception{
		try {
			String url = getAlfrescoAPIUrl() + "people";
			if(GeneralCTSI.noVacio(filterQuery)) url = url + "?filter="+filterQuery;
			
			HttpRequest request = getRequestFactory().buildGetRequest(new GenericUrl(url));
			//System.out.println("RESPONSE: " + request.execute().parseAsString());
			return request.execute().parseAs(ListUsers.class);
		}catch(Exception ex){
			String err = "Se ha producido un error al consultar los usuarios";
			String privateInfo = null;
			if(ex instanceof HttpResponseException){
				privateInfo = ((HttpResponseException)ex).getMessage();
			}
			log.error(err,ex);
			throw new Exception(err, privateInfo, ex, false);	
		}
	}
	*/
	
	
	
	/****
	 * Permite Obtener la información de un usuario de alfresco
	 * 
	 * @param userName identificador del usuario
	 * @param groupInfo boolean que indica si añadimos la información de los grupos del usuario o no a la respuesta
	 * 
	 * @return User
	 */
	/*
	public User getUserInfo(String userName, boolean groupInfo) throws Exception{
		try {
			String url = getAlfrescoAPIUrl() + "people/{username}";
			if(groupInfo) url = url + "?groups=true";
			url = url.replace("{username}", userName);
			
			HttpRequest request = getRequestFactory().buildGetRequest(new GenericUrl(url));
			return request.execute().parseAs(User.class);
		}catch(Exception ex){
			String err = "Se ha producido un error al consultar el usuario "+ userName;
			String privateInfo = null;
			if(ex instanceof HttpResponseException){
				privateInfo = ((HttpResponseException)ex).getMessage();
			}
			log.error(err,ex);
			throw new Exception(err, privateInfo, ex, false);	
		}
	}
	*/
	
	/****
	 * Permite consultar los grupos de alfresco
	 * 
	 * @param filterQuery query para filtar el resultado
	 * @return GroupChildrens
	 */
	/*
	public GroupChildrens getGroups(String filterQuery) throws Exception{
		try{
			String url = getAlfrescoAPIUrl() + "groups";
			if(GeneralCTSI.noVacio(filterQuery)) url = url + "?filter="+filterQuery;
			
			HttpRequest request = getRequestFactory().buildGetRequest(new GenericUrl(url));
			//System.out.println("RESPONSE: " + request.execute().parseAsString());
			return request.execute().parseAs(GroupChildrens.class);
		}catch(Exception ex){
	    	String err = "Se ha producido un error al intentar consultar los grupos";
	    	String privateInfo = null;
			if(ex instanceof HttpResponseException){
				privateInfo = ((HttpResponseException)ex).getMessage();
			}
			log.error(err,ex);
			throw new Exception(err, privateInfo, ex, false);		
	    }     
	}
	*/
	
	
	
	/****
	 * Permite consultar los usuarios de un grupo
	 * 
	 * @param groupName nombre del grupo (el group_prefix no es necesario)
	 * @return  Lista de usuarios
	 * @throws ExceptionCTSI
	 */
	/*
	public GroupChildrens getUsersInGroup(String groupName) throws Exception{
		try{
			String url = getAlfrescoAPIUrl() + "groups/{group}/children?authorityType=USER";
			url = url.replace("{group}", groupName);
			
			HttpRequest request = getRequestFactory().buildGetRequest(new GenericUrl(url));
			return request.execute().parseAs(GroupChildrens.class);
		}catch(Exception ex){
	    	String err = "Se ha producido un error al intentar consultar los usurios del grupo " + groupName;
	    	String privateInfo = null;
			if(ex instanceof HttpResponseException){
				privateInfo = ((HttpResponseException)ex).getMessage();
			}
			log.error(err,ex);
			throw new Exception(err, privateInfo, ex, false);		
	    }     
	}
	*/
	
	/****
	 * Permite añadir un usuario a un grupo
	 * NOTA: Es obligatorio lanzar es función como admin
	 * 
	 * @param username nombre del usuario
	 * @param group nombre del grupo
	 * @throws ExceptionCTSI
	 */
	public boolean addUserToGroup(String username, String group) throws Exception{
		try {
			//Comprobamos que exista el usuario
			if(!isUserCreated(username)) throw new Exception("No se ha encontrado el usuario indentificado por " + username);
			//if(getGroupInfo(group, false) == null) throw new ExceptionCTSI("No se ha encontrado el grupo " + group);
			
			String url = getAlfrescoAPIUrl() + "groups/{group}/children/{username}";
			url = url.replace("{username}", username);
			url = url.replace("{group}", group);
			
			HttpContent body = new ByteArrayContent("application/json", "{}".getBytes("UTF-8"));
			HttpRequest request = getRequestFactory().buildPostRequest(new GenericUrl(url), body);
			HttpResponse response = request.execute();
			
			System.out.println("RESPONSE: " + response.parseAsString());
			if(response.isSuccessStatusCode()){
				return true;
			}else{
				return false;
			}
		}catch(Exception ex){
			String err = "Se ha producido un error al añadir el usuario "+ username + " al grupo " + group;
			log.error(err,ex);
			throw new Exception(err,ex);	
		}
	}
	
	/****
	 * Permite eliminar un usuario de un grupo
	 * NOTA: Es obligatorio lanzar es función como admin
	 * 
	 * @param username nombre del usuario
	 * @param group nombre del grupo
	 * @throws ExceptionCTSI
	 */
	public boolean deleteUserFromGroup(String username, String group) throws Exception{
		try {
			//Comprobamos que exista el usuario
			if(!isUserCreated(username)) throw new Exception("No se ha encontrado el usuario indentificado por " + username);
			//if(getGroupInfo(group, false) == null) throw new ExceptionCTSI("No se ha encontrado el grupo " + group);
			
			String url = getAlfrescoAPIUrl() + "groups/{group}/children/{username}";
			url = url.replace("{username}", username);
			url = url.replace("{group}", group);
			
			HttpRequest request = getRequestFactory().buildDeleteRequest(new GenericUrl(url));
			HttpResponse response = request.execute();
			
			System.out.println("RESPONSE: " + response.parseAsString());
			if(response.isSuccessStatusCode()){
				return true;
			}else{
				return false;
			}
		}catch(Exception ex){
			String err = "Se ha producido un error al añadir el usuario "+ username + " al grupo " + group;
			log.error(err,ex);
			throw new Exception(err,ex);	
		}
	}
	
	/****
	 * Permite crear un Objeto ACE de alfresco
	 * 
	 * @param username nombre usuario
	 * @param permission permiso a establecer
	 * @return  Ace Object 
	 * @throws ExceptionCTSI
	 */
	public Ace createInstancePermission(String username, String permission) throws Exception{
		return this.createInstancePermission(username, permission, false);
	}
	
	/****
	 * Permite crear un Objeto ACE de alfresco
	 * 
	 * @param userORGrupName nombre usuario o grupo
	 * @param permission permiso a establecer
	 * @return  Ace Object 
	 * @throws ExceptionCTSI
	 */
	public Ace createInstancePermission(String userORGroupName, String permission, boolean isGroup) throws Exception{
		List<String> permissions = new LinkedList<String>();
		permissions.add(permission);
		Ace ace = getCmisSession().getObjectFactory().createAce((isGroup?GROUP_PREFIX:"")+userORGroupName, permissions);
		return ace;
	}	
	
	/**
	 * ###############################################################################################################################
	 * ********************************** REST: DATALIST ****************************************
	 * ###############################################################################################################################
	 * **/
	/****
	 * Permite consultar las listas de datos existentes en la UPCT
	 * 
	 * @param groupName nombre del grupo (el group_prefix no es necesario)
	 * @return  Lista de usuarios
	 * @throws ExceptionCTSI
	 */
	protected void getDatalistUpct() throws Exception{
		try{
			String url = getAlfrescoSlingshotUrl() + "datalists/lists/site/{site}/{container}";
			url = url.replace("{site}", "diccionario");
			url = url.replace("{container}", "dataLists");
			//url = url.replace("{list}", listName);
			
			//System.out.println("URL WEB SCRIPT:" + url);
			HttpRequest request = getRequestFactory().buildGetRequest(new GenericUrl(url));
			HttpResponse response = request.execute();
			System.out.println("RESPONSE: " + response.parseAsString());
		
		}catch(HttpResponseException httpre){
			System.out.println(httpre.getMessage());
		}catch(Exception ex){
			ex.printStackTrace();
			String err = "Se ha producido un error al intentar consultar el datalist UPCT ";
			log.error(err,ex);
			throw new Exception(err,ex);	
		}
	}
	
	/*public void getDatalistDataUpct(String datalistGUID) throws ExceptionCTSI{
		try{
			String url = getAlfrescoSlingshotUrl() + "datalists/data/site/{site}/{container}/{list}";
			url = url.replace("{site}", "diccionario");
			url = url.replace("{container}", "dataLists");
			url = url.replace("{list}", datalistGUID);
			
			//System.out.println("URL WEB SCRIPT:" + url);
			String jsonData = "{\"fields\":[],\"filter\":{\"filterData\":\"\", \"filterId\":\"all\"}}";
			HttpContent body = new ByteArrayContent("application/json", jsonData.getBytes());
			HttpRequest request = getRequestFactory().buildPostRequest(new GenericUrl(url),body);
			HttpResponse response = request.execute();
			System.out.println("RESPONSE: " + response.parseAsString());
		
		}catch(HttpResponseException httpre){
			System.out.println(httpre.getMessage());
		}catch(Exception ex){
			ex.printStackTrace();
			String err = "Se ha producido un error al intentar consultar el datalist "+ datalistGUID;
			log.error(err,ex);
			throw new ExceptionCTSI(err,ex);	
		}
	}	*/
	/****
	 * Permite consultar los datos de una lista identificada por su title (Keensoft webScript)
	 * 
	 * @param datalistName nombre de la lista
	 * @throws ExceptionCTSI
	 */
	protected void getDatalistDataUpct(String datalistName) throws Exception{
		try{
			String url = getAlfrescoKensoftUrl() + "datalist/{datalistName}";
			url = url.replace("{datalistName}", datalistName);
			
			//System.out.println("URL WEB SCRIPT:" + url);
			HttpRequest request = getRequestFactory().buildGetRequest(new GenericUrl(url));
			HttpResponse response = request.execute();
			System.out.println("RESPONSE: " + response.parseAsString());
		
		}catch(HttpResponseException httpre){
			System.out.println(httpre.getMessage());
		}catch(Exception ex){
			ex.printStackTrace();
			String err = "Se ha producido un error al intentar consultar el datalist "+ datalistName;
			log.error(err,ex);
			throw new Exception(err,ex);	
		}
	}	
}

